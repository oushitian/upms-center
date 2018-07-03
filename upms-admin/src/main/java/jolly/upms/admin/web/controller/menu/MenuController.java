package jolly.upms.admin.web.controller.menu;

import com.jolly.upms.manager.annotation.SysLog;
import com.jolly.upms.manager.dao.MenuDao;
import com.jolly.upms.manager.model.Application;
import com.jolly.upms.manager.model.Menu;
import com.jolly.upms.manager.service.ApplicationService;
import com.jolly.upms.manager.service.AuthService;
import com.jolly.upms.manager.service.MenuService;
import com.jolly.upms.manager.vo.DataDimensionVO;
import com.jolly.upms.manager.vo.Pagination;
import com.jolly.upms.manager.vo.TreeNode;
import jolly.upms.admin.web.controller.menu.vo.MenuParamVO;
import jolly.upms.admin.web.controller.menu.vo.MenuVO;
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
@RequestMapping("/menu")
public class MenuController {

    private static final Logger logger = LoggerFactory.getLogger(MenuController.class);

    @Resource
    private MenuService menuService;

    @Resource
    private MenuDao menuDao;

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
        Set<String> permissionStrings = (Set<String>) authService.getFunctionPermission(request).getData();
        model.addAttribute("permissionStrings", permissionStrings);
        return "menu/menuList";
    }

    @RequestMapping("/listData")
    @ResponseBody
    public PageRespResult listData(MenuParamVO menuParamVO, HttpServletRequest request) {
        List<MenuVO> menuVOList = new ArrayList<>();
        Set<Integer> permitApplicationIds = authService.getRolePermitApplicationIds(request);
        if (permitApplicationIds.isEmpty()) {
            return new PageRespResult(menuVOList, 0);
        }
        Map params = new HashMap();
        params.put("applicationId", menuParamVO.getApplicationId());
        params.put("type", menuParamVO.getType());
        params.put("name", menuParamVO.getName());
        params.put("parentName", menuParamVO.getParentName());
        params.put("url", menuParamVO.getUrl());
        params.put("permitApplicationIds", permitApplicationIds);
        Pagination<Menu> pagination = menuService.queryList(params, menuParamVO.getStart(), menuParamVO.getRows());
        List<Menu> menuList = pagination.getResult();
        for (Menu menu : menuList) {
            MenuVO menuVO = new MenuVO();
            BeanUtils.copyProperties(menu, menuVO);
            menuVO.setTypeName(this.getTypeName(menu.getType()));
            Integer parentId = menu.getParentId();
            if (parentId != null && parentId > 0) {
                menuVO.setParentName(menuService.get(parentId).getName());
            }
            menuVO.setAppName(applicationService.get(menu.getApplicationId()).getAppName());
            if (menu.getType() == 3) {
                Set<Integer> set = new HashSet<>();
                set.add(menu.getMenuId());
                List<DataDimensionVO> dataDimensionVOS = menuDao.queryMenuDataDimensions(set);
                if (!dataDimensionVOS.isEmpty()) {
                    StringBuilder sb = new StringBuilder();
                    for (DataDimensionVO dataDimensionVO : dataDimensionVOS) {
                        sb.append(dataDimensionVO.getDisplayName()).append(",");
                    }
                    sb.deleteCharAt(sb.lastIndexOf(","));
                    menuVO.setDimensionNames(sb.toString());
                }
            }
            menuVOList.add(menuVO);
        }
        return new PageRespResult(menuVOList, pagination.getTotalCount());
    }

    private String getTypeName(Byte type) {
        switch (type) {
            case 1:
                return "一级菜单";
            case 2:
                return "二级菜单";
            case 3:
                return "按钮";
            default:
                return "";
        }
    }

    @RequestMapping("/getParentMenuByType")
    @ResponseBody
    public RespResult getParentMenuByType(Byte type, Integer applicationId) {
        List<Menu> menuList = menuService.getParentMenuByType(type, applicationId);
        return new RespResult(menuList);
    }

    @RequestMapping("/edit")
    @ResponseBody
    @SysLog(SysLog.Operate.MENU_EDIT)
    public RespResult edit(Menu menu, String dimensionIds) {
        try {
            menuService.saveOrUpdate(menu, dimensionIds);
        } catch (Exception e) {
            return new RespResult(RespResult.RespCode.SYS_EXCEPTION.getCode(), e.getMessage());
        }
        return new RespResult();
    }

    /**
     * 目前有三种应用场景：
     * 1. applicationId!=null&&userId==null，表示按应用查找菜单
     * 2. applicationId==null&&userId!=null，表示查找全部应用的菜单，并按应用分组，且标记用户已有的菜单节点为选中状态
     * 3. applicationId!=null&&menuGroupId!=null，表示按应用查找菜单，且标记权限组已有的菜单节点为选中状态
     *
     * @param applicationId
     * @param userId
     * @return
     */
    @RequestMapping("/getMenus")
    @ResponseBody
    public List<TreeNode> getMenus(Integer applicationId, Integer userId, Integer menuGroupId, HttpServletRequest request) {
        if (applicationId != null && menuGroupId != null) {
            return menuService.getMenus(applicationId, menuGroupId);
        }
        if (applicationId != null && userId == null) {
            return menuService.getMenus(applicationId, null);
        }
        if (applicationId == null && userId != null) {
            return menuService.getMenusGroupByApplication(userId, request);
        }
        return new ArrayList<>();
    }

    /**
     * 只查询有数据维度的菜单
     *
     * @param menuGroupIds
     * @return
     */
    @RequestMapping("/getDimensionMenus")
    @ResponseBody
    public List<TreeNode> getDimensionMenus(String menuGroupIds, Integer roleId) {
        return menuService.getDimensionMenus(menuGroupIds, roleId);
    }

    @RequestMapping("/delete")
    @ResponseBody
    @SysLog(SysLog.Operate.MENU_DELETE)
    public RespResult delete(Integer menuId) {
        try {
            menuService.deleteMenu(menuId);
        } catch (Exception e) {
            return new RespResult(RespResult.RespCode.SYS_EXCEPTION.getCode(), e.getMessage());
        }
        return new RespResult();
    }
}
