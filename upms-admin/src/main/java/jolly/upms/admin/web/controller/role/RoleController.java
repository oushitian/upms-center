package jolly.upms.admin.web.controller.role;

import com.jolly.upms.manager.annotation.SysLog;
import com.jolly.upms.manager.dao.RolePermitApplicationDao;
import com.jolly.upms.manager.model.Application;
import com.jolly.upms.manager.model.MenuGroup;
import com.jolly.upms.manager.model.Role;
import com.jolly.upms.manager.model.RolePermitApplication;
import com.jolly.upms.manager.service.ApplicationService;
import com.jolly.upms.manager.service.AuthService;
import com.jolly.upms.manager.service.MenuGroupService;
import com.jolly.upms.manager.service.RoleService;
import com.jolly.upms.manager.vo.Pagination;
import com.jolly.upms.manager.vo.RoleSaveVO;
import com.jolly.upms.manager.vo.TreeNode;
import jolly.upms.admin.web.controller.role.vo.RoleParamVO;
import jolly.upms.admin.web.controller.role.vo.RoleVO;
import jolly.upms.admin.web.vo.PageRespResult;
import jolly.upms.admin.web.vo.RespResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author chenjc
 * @since 2017-03-16
 */
@Controller
@RequestMapping("/role")
public class RoleController {

    private static final Logger logger = LoggerFactory.getLogger(RoleController.class);

    @Resource
    private ApplicationService applicationService;

    @Resource
    private RoleService roleService;

    @Resource
    private MenuGroupService menuGroupService;

    @Resource
    private AuthService authService;

    @Resource
    private RolePermitApplicationDao rolePermitApplicationDao;

    @RequestMapping("/list")
    public String list(Model model, HttpServletRequest request) {
        List<Application> applicationList = new ArrayList<>();
        Set<Integer> permitApplicationIds = authService.getRolePermitApplicationIds(request);
        if (!permitApplicationIds.isEmpty()) {
            Map params = new HashMap();
            params.put("permitApplicationIds", permitApplicationIds);
            applicationList = applicationService.queryList(params);
        }
        model.addAttribute("applicationList", applicationList);
        model.addAttribute("allApplicationList", applicationService.queryList(null));
        Set<String> permissionStrings= (Set<String>) authService.getFunctionPermission(request).getData();
        model.addAttribute("permissionStrings", permissionStrings);
        return "role/roleList";
    }

    @RequestMapping("/listData")
    @ResponseBody
    public PageRespResult listData(RoleParamVO roleParamVO, HttpServletRequest request) {
        List<RoleVO> roleVOList = new ArrayList<>();
        Set<Integer> permitApplicationIds = authService.getRolePermitApplicationIds(request);
        if (permitApplicationIds.isEmpty()) {
            return new PageRespResult(roleVOList, 0);
        }
        Map params = new HashMap();
        params.put("applicationId", roleParamVO.getApplicationId());
        params.put("menuGroupId", roleParamVO.getMenuGroupId());
        params.put("name", roleParamVO.getName());
        params.put("sort", roleParamVO.getSort());
        params.put("order", roleParamVO.getOrder());
        params.put("permitApplicationIds", permitApplicationIds);
        Pagination<Role> pagination = roleService.queryList(params, roleParamVO.getStart(), roleParamVO.getRows());
        List<Role> roleList = pagination.getResult();
        for (Role role : roleList) {
            RoleVO roleVO = new RoleVO();
            BeanUtils.copyProperties(role, roleVO);
            Application application = applicationService.get(role.getApplicationId());
            roleVO.setAppName(application.getAppName());
            roleVO.setAppKey(application.getAppKey());
            List<RolePermitApplication> permitApplicationList = rolePermitApplicationDao.getByRoleId(role.getRoleId());
            if (!permitApplicationList.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (RolePermitApplication rolePermitApplication : permitApplicationList) {
                    sb.append(rolePermitApplication.getApplicationId()).append(",");
                }
                sb.deleteCharAt(sb.lastIndexOf(","));
                roleVO.setPermitApplicationIds(sb.toString());
            }
            List<MenuGroup> menuGroupList = menuGroupService.getByRoleId(role.getRoleId());
            if (!menuGroupList.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                StringBuilder sbIds = new StringBuilder();
                for (MenuGroup menuGroup : menuGroupList) {
                    sb.append(menuGroup.getName()).append(",");
                    sbIds.append(menuGroup.getMenuGroupId()).append(",");
                }
                sb.deleteCharAt(sb.lastIndexOf(","));
                sbIds.deleteCharAt(sbIds.lastIndexOf(","));
                roleVO.setOwnMenuGroups(sb.toString());
                roleVO.setOwnMenuGroupIds(sbIds.toString());
            }
            roleVOList.add(roleVO);
        }
        return new PageRespResult(roleVOList, pagination.getTotalCount());
    }

    @RequestMapping("/edit")
    @ResponseBody
    @SysLog(SysLog.Operate.ROLE_EDIT)
    public RespResult edit(RoleSaveVO roleSaveVO) {
        try {
            roleService.saveOrUpdate(roleSaveVO);
        } catch (Exception e) {
            return new RespResult(RespResult.RespCode.SYS_EXCEPTION.getCode(), e.getMessage());
        }
        return new RespResult();
    }

    @RequestMapping("/delete")
    @ResponseBody
    @SysLog(SysLog.Operate.ROLE_DELETE)
    public RespResult delete(Integer roleId) {
        try {
            roleService.deleteRole(roleId);
        } catch (Exception e) {
            return new RespResult(RespResult.RespCode.SYS_EXCEPTION.getCode(), e.getMessage());
        }
        return new RespResult();
    }

    @RequestMapping("/savePermitApplication")
    @ResponseBody
    @SysLog(SysLog.Operate.ROLE_APPLICATION_UPDATE)
    public RespResult savePermitApplication(Integer roleId, String applicationIds) {
        try {
            roleService.savePermitApplication(roleId, applicationIds);
        } catch (Exception e) {
            return new RespResult(RespResult.RespCode.SYS_EXCEPTION.getCode(), e.getMessage());
        }
        return new RespResult();
    }

    @RequestMapping("/getRoles")
    @ResponseBody
    public List<TreeNode> getRoles(Integer userId, HttpServletRequest request) {
        List<Integer> roleIds = new ArrayList<>();//存放用户的角色ID
        if (userId != null && userId > 0) {
            //查找用户对应的角色
            List<Role> roleList = roleService.queryRoles(userId);
            for (Role role : roleList) {
                roleIds.add(role.getRoleId());
            }
        }
        List<Role> roleList = roleService.queryList(null);
        List<TreeNode> nodeList = new ArrayList<>();
        Map<Integer, List<Role>> map = new HashMap<>();//按应用系统将角色分组
        for (Role role : roleList) {
            Integer applicationId = role.getApplicationId();
            List<Role> list = map.get(applicationId);
            if (list == null) {
                list = new ArrayList<>();
                list.add(role);
                map.put(applicationId, list);
            } else {
                list.add(role);
            }
        }
        //当前用户可访问的应用
        Set<Integer> permitApplicationIds = authService.getRolePermitApplicationIds(request);
        for (Map.Entry<Integer, List<Role>> entry : map.entrySet()) {
            Integer appId = entry.getKey();
            if (!permitApplicationIds.contains(appId)) {
                continue;
            }
            TreeNode node1 = new TreeNode();
            node1.setId(appId);
            node1.setText(applicationService.get(appId).getAppName());
            List<TreeNode> children4Node1 = new ArrayList<>();
            List<Role> list = entry.getValue();
            for (Role role : list) {
                TreeNode node2 = new TreeNode();
                Integer roleId = role.getRoleId();
                node2.setId(roleId);
                node2.setText(role.getName());
                if (roleIds.contains(roleId))
                    node2.setChecked(true);
                node2.setChildren(new ArrayList<TreeNode>());
                children4Node1.add(node2);
            }
            node1.setChildren(children4Node1);
            nodeList.add(node1);
        }
        return nodeList;
    }

}
