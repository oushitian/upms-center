package jolly.upms.admin.web.controller;

import com.alibaba.fastjson.JSON;
import com.jolly.upms.manager.model.Application;
import com.jolly.upms.manager.model.AuthUser;
import com.jolly.upms.manager.model.Menu;
import com.jolly.upms.manager.model.Role;
import com.jolly.upms.manager.service.*;
import com.jolly.upms.manager.util.Assert;
import com.jolly.upms.manager.util.Constant;
import jolly.upms.admin.common.util.MenuUtil;
import jolly.upms.admin.web.vo.MenuNode;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chenjc
 * @since 2017-05-31
 */
@Controller
public class IndexController {
    @Resource
    private AuthService authService;
    @Resource
    private ApplicationService applicationService;
    @Resource
    private MenuService menuService;

    @Resource
    private RoleService roleService;

    @Resource
    private CacheService cacheService;

    /**
     * 登陆页面
     * @return
     */
    @RequestMapping("/refuse")
    public String refuse() {
        return "/refuse";
    }

    private List<Menu> getUserMenus_(String appKey, String token) {
        String cache = cacheService.getToken(token);
        Assert.notBlank(cache, "\"invalid token\"");
        AuthUser authUser = JSON.parseObject(cache, AuthUser.class);
        Map map = new HashMap();
        map.put("appKey", appKey);
        List<Application> applications = applicationService.queryList(map);
        Assert.isTrue(CollectionUtils.isNotEmpty(applications), "invalid appKey");
        Integer applicationId = applications.get(0).getApplicationId();
        return menuService.queryFirstAndSecondLevel(authUser.getUserId(), applicationId);

    }

    @RequestMapping("/index")
    public ModelAndView index(ModelAndView view, @CookieValue(value = "auth_token_new") String token, HttpServletRequest request) {
        List<Menu> allMenu =  getUserMenus_(Constant.UPMS_APPKEY, token);
        // 最后的结果
        List<MenuNode> menuList = new ArrayList<>();
        // 先找到所有的一级菜单
        for (int i = 0; i < allMenu.size(); i++) {
            // 一级菜单没有parentId
            MenuNode addMenuDTO=new MenuNode();
            if (allMenu.get(i).getParentId()==0) {
                BeanUtils.copyProperties(allMenu.get(i),addMenuDTO);
                menuList.add(addMenuDTO);
            }
        }
        // 为一级菜单设置子菜单，getChild是递归调用的
        for (MenuNode menu : menuList) {
            menu.setChildrenMenus(MenuUtil.getChild(menu.getMenuId(), allMenu));
        }
        view.getModelMap().addAttribute("userMenus_",menuList);
        view.getModelMap().addAttribute("token",token);
        AuthUser authUser = authService.getAuthUser(request);
        view.getModelMap().addAttribute("user", authUser);
        Integer applicationId = authService.getApplication(Constant.UPMS_APPKEY).getApplicationId();
        if(applicationId!=null){
            List<Role> roles = roleService.queryRoles(authUser.getUserName(), applicationId);
            view.getModelMap().addAttribute("roleList",roles);
        }
        view.setViewName("index");
        return view;
    }

    /**
     * 登出
     *
     * @param token
     * @param appKey
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/logout")
    @ResponseBody
    public void logout(@RequestParam String token, @RequestParam String appKey, HttpServletResponse response, HttpServletRequest request) throws Exception {
        authService.logout(token, appKey);
        response.sendRedirect(request.getContextPath() + "/api/loginUI");
    }

}
