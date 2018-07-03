package jolly.upms.admin.web.controller.menugroup;

import com.jolly.upms.manager.annotation.SysLog;
import com.jolly.upms.manager.model.Application;
import com.jolly.upms.manager.model.DataDimension;
import com.jolly.upms.manager.model.MenuGroup;
import com.jolly.upms.manager.service.ApplicationService;
import com.jolly.upms.manager.service.AuthService;
import com.jolly.upms.manager.service.MenuGroupService;
import com.jolly.upms.manager.vo.MenuGroupSaveVO;
import com.jolly.upms.manager.vo.Pagination;
import com.jolly.upms.manager.vo.TreeNode;
import jolly.upms.admin.web.controller.menugroup.vo.MenuGroupParamVO;
import jolly.upms.admin.web.controller.menugroup.vo.MenuGroupVO;
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
@RequestMapping("/menuGroup")
public class MenuGroupController {

    private static final Logger logger = LoggerFactory.getLogger(MenuGroupController.class);

    @Resource
    private MenuGroupService menuGroupService;

    @Resource
    private ApplicationService applicationService;
    @Resource
    private AuthService authService;
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
        Set<String> permissionStrings= (Set<String>) authService.getFunctionPermission(request).getData();
        model.addAttribute("permissionStrings", permissionStrings);
        return "menuGroup/menuGroupList";
    }

    @RequestMapping("/listData")
    @ResponseBody
    public PageRespResult listData(MenuGroupParamVO paramVO, HttpServletRequest request) {
        List<MenuGroupVO> menuGroupVOList = new ArrayList<>();
        Set<Integer> permitApplicationIds = authService.getRolePermitApplicationIds(request);
        if (permitApplicationIds.isEmpty()) {
            return new PageRespResult(menuGroupVOList, 0);
        }
        Map params = new HashMap();
        params.put("applicationId", paramVO.getApplicationId());
        params.put("name", paramVO.getName());
        params.put("permitApplicationIds", permitApplicationIds);
        Pagination<MenuGroup> pagination = menuGroupService.queryList(params, paramVO.getStart(), paramVO.getRows());
        List<MenuGroup> menuGroupList = pagination.getResult();
        for (MenuGroup menuGroup : menuGroupList) {
            MenuGroupVO menuGroupVO = new MenuGroupVO();
            BeanUtils.copyProperties(menuGroup, menuGroupVO);
            //通过菜单组ID查询包含的菜单，进而查询菜单下的数据维度
            List<DataDimension> dataDimensionList = menuGroupService.queryDataDimensions(menuGroup.getMenuGroupId());
            if (!dataDimensionList.isEmpty()) {
                Set<DataDimension> set = new HashSet<>(dataDimensionList);//去重
                StringBuilder sb = new StringBuilder();
                for (DataDimension dataDimension : set) {
                    sb.append(dataDimension.getDisplayName()).append(",");
                }
                sb.deleteCharAt(sb.lastIndexOf(","));
                menuGroupVO.setDataDimensions(sb.toString());
            }
            menuGroupVO.setAppName(applicationService.get(menuGroup.getApplicationId()).getAppName());
            menuGroupVOList.add(menuGroupVO);
        }
        return new PageRespResult(menuGroupVOList, pagination.getTotalCount());
    }

    @RequestMapping("/queryByApplicationId")
    @ResponseBody
    public RespResult queryByApplicationId(Integer applicationId) {
        Map params = new HashMap();
        params.put("applicationId", applicationId);
        List<MenuGroup> menuGroupList = menuGroupService.queryList(params);
        return new RespResult(menuGroupList);
    }

    @RequestMapping("/getMenuGroups")
    @ResponseBody
    public List<TreeNode> getMenuGroups(Integer applicationId, Integer roleId) {
        List<MenuGroup> list = new ArrayList<>();
        if (roleId != null) {
            list = menuGroupService.getByRoleId(roleId);
        }
        List<TreeNode> nodeList = new ArrayList<>();
        Map params = new HashMap();
        params.put("applicationId", applicationId);
        List<MenuGroup> menuGroupList = menuGroupService.queryList(params);
        for (MenuGroup menuGroup : menuGroupList) {
            TreeNode node = new TreeNode();
            node.setId(menuGroup.getMenuGroupId());
            node.setText(menuGroup.getName());
            if (list.contains(menuGroup)) {
                node.setChecked(true);
            }
            nodeList.add(node);
        }
        return nodeList;
    }

    @RequestMapping("/edit")
    @ResponseBody
    @SysLog(SysLog.Operate.MENU_GROUP_EDIT)
    public RespResult edit(MenuGroupSaveVO menuGroupSaveVO) {
        try {
            menuGroupService.saveOrUpdate(menuGroupSaveVO);
        } catch (Exception e) {
            return new RespResult(RespResult.RespCode.SYS_EXCEPTION.getCode(), e.getMessage());
        }
        return new RespResult();
    }

    @RequestMapping("/delete")
    @ResponseBody
    @SysLog(SysLog.Operate.MENU_GROUP_DELETE)
    public RespResult delete(Integer menuGroupId) {
        try {
            menuGroupService.deleteMenuGroup(menuGroupId);
        } catch (Exception e) {
            return new RespResult(RespResult.RespCode.SYS_EXCEPTION.getCode(), e.getMessage());
        }
        return new RespResult();
    }

}
