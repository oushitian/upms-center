package jolly.upms.admin.web.controller;

import com.jolly.upms.manager.dao.RbacUserDao;
import com.jolly.upms.manager.model.AuthUser;
import com.jolly.upms.manager.model.User;
import com.jolly.upms.manager.service.AuthService;
import com.jolly.upms.manager.service.CacheService;
import com.jolly.upms.manager.service.UserService;
import com.jolly.upms.manager.util.Constant;
import com.jolly.upms.manager.util.StringUtils;
import jolly.upms.admin.web.vo.RespResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 2017/6/30.
 */
@Controller
@RequestMapping("/authenticationAccess")
public class AuthenticationAccessController {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationAccessController.class);

    @Autowired
    private AuthService authService;
    @Resource
    private UserService userService;

    @Resource
    private CacheService cacheService;

    @Resource
    private RbacUserDao rbacUserDao;

    @ModelAttribute("authUser")
    public AuthUser getUser(HttpServletRequest request) {
        return authService.getAuthUser(request);
    }

    /**
     * 修改密码页面
     *
     * @return
     */
    @RequestMapping("/userModifyPwd")
    public String userModifyPwd() {
        return "authenticationAccess/userModifyPwd";
    }


    /**
     * 修改登录用户密码
     */
    @RequestMapping("/password")
    public String password(@RequestParam String username, @RequestParam String password, @RequestParam String newPassword, ModelMap map) {
        User user = userService.queryByUserName(username);
        if (user == null) {
            map.addAttribute("username_wrong", "Invalid username.");
            return "authenticationAccess/userModifyPwd";
        }
        int count = userService.updatePassword(user.getUserId().longValue(), password, newPassword);
        if (count == 0) {
            map.addAttribute("password_wrong", "Incorrect old password.");
            return "authenticationAccess/userModifyPwd";
        }
        try {
            //同步到who_rbac_user库，兼容jcm系统
            User user4Update = new User();
            user4Update.setUserId(user.getUserId());
            user4Update.setPassword(newPassword);
            rbacUserDao.updateSelective(user4Update);
        } catch (Exception e) {
            logger.error("同步修改who_rbac_user表密码异常，username={}", username, e);
        }
        //删除redis中已存在的该用户的token
        cacheService.deleteTokenByPrefix(Constant.AUTH_TOKEN_CACHE_PREFIX + "appKey_upms:userId_" + user.getUserId() + ":");
        return "redirect:/api/loginUI";
    }

    /**
     * 根据用户名验证用户是否存在，提供给前线客服开发组使用
     *
     * @param user
     * @return
     */
    @RequestMapping(value = "/validateUser", method = RequestMethod.POST)
    @ResponseBody
    public RespResult validateUser(@RequestBody User user) {
        String userName = user.getUserName();
        if (StringUtils.isBlank(userName)) {
            return new RespResult(RespResult.RespCode.PARAM_EXCEPTION.getCode(), "用户名不能为空");
        }
        User userInDB = userService.queryByUserName(userName);
        if (userInDB == null) {
            return new RespResult(RespResult.RespCode.NO_ENTITY.getCode(), "用户不存在");
        }
        return new RespResult();
    }

}
