package jolly.upms.admin.web.controller;

import com.alibaba.fastjson.JSON;
import com.jolly.upms.manager.model.User;
import com.jolly.upms.manager.service.AuthService;
import com.jolly.upms.manager.service.UserService;
import com.jolly.upms.manager.util.Constant;
import com.jolly.upms.manager.util.DateUtils;
import com.jolly.upms.manager.util.MD5Util;
import com.jolly.upms.manager.util.WebUtils;
import com.jolly.upms.manager.vo.AccessTokenVO;
import com.jolly.upms.manager.vo.AuthResultVO;
import com.jolly.upms.manager.vo.BaseResponseVO;
import com.jolly.upms.manager.vo.LoginVO;
import jolly.upms.admin.common.base.controller.BaseController;
import jolly.upms.admin.web.vo.SSOLoginVO;
import jolly.upms.admin.web.vo.UserDTO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * 统一身份认证接口
 * <p>
 * Created by dengjunbo
 * on 16/5/19.
 */
@RestController
@RequestMapping("/auth")
public class AuthController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService service;

    @Resource
    private UserService userService;


    /**
     * 新登录
     *
     * @param ssoLoginVO
     * @param request
     * @param response
     * @param result
     * @param map
     * @return
     */
    @RequestMapping(value = "/ssoLogin", method = RequestMethod.POST)
    public ModelAndView ssoLogin(@Valid SSOLoginVO ssoLoginVO, BindingResult result, HttpServletRequest request, HttpServletResponse response, ModelMap map) {
        if (result.hasErrors()) {
            return loginFail(ssoLoginVO.getUserName(), result.getAllErrors().get(0).getDefaultMessage(), request, map);
        }
        try {
            ssoLoginVO.setPassword(MD5Util.getMD5(ssoLoginVO.getPassword()));
            LoginVO loginVO = new LoginVO();
            loginVO.setUserName(ssoLoginVO.getUserName());
            loginVO.setPassword(ssoLoginVO.getPassword());
            loginVO.setAppKey(ssoLoginVO.getAppKey());
            BaseResponseVO loginResult = service.login(loginVO, true);
            if (Constant.STATUS_OK != loginResult.getResult()) {
                return loginFail(ssoLoginVO.getUserName(), loginResult.getMessage(), request, map);
            }
            AuthResultVO resultVO = (AuthResultVO) loginResult;
            WebUtils.setAuthCookie(request, response, resultVO.getToken());
            User user = new User();
            user.setUserId(resultVO.getUserId());
            user.setLastIp(request.getRemoteAddr());
            user.setLastLogin(DateUtils.getCurrentSecondIntValue());
            userService.updateSelective(user);
            response.sendRedirect(request.getContextPath() + "/user/getPermitApp");
            return null;
        } catch (Exception e) {
            logger.error("页面登录异常", e);
            return loginFail(ssoLoginVO.getUserName(), "系统异常，请联系管理员", request, map);
        }
    }


    private ModelAndView loginFail(String userName, String flashMessage, HttpServletRequest request, ModelMap map) {
        logger.info("Authentication failure for user '" + userName + "', message:" + flashMessage);
        map.addAttribute("flash_message", flashMessage);
        map.addAttribute("userName", userName);
        return new ModelAndView("forward:/api/loginUI");
    }

    /**
     * 登陆接口
     *
     * @param loginVO 登陆参数VO对象
     * @param result  参数绑定信息
     * @return 登陆成功或失败信息
     */
    @RequestMapping("/login")
    public BaseResponseVO login(@Valid LoginVO loginVO, BindingResult result) {
        logger.info("老版本的登录,请求数据为{}", JSON.toJSONString(loginVO));
        if (result.hasErrors()) {
            return new BaseResponseVO().setResult(Constant.STATUS_FAIL).setMessage(result.getAllErrors().get(0).getDefaultMessage());
        }

        return service.login(loginVO, false);
    }


    /**
     * 验证访问token有效性
     *
     * @param tokenVO 验证token信息
     * @param result  参数绑定信息
     * @return
     */
    @RequestMapping("/accessToken")
    @ResponseBody
    public BaseResponseVO accessToken(@Valid AccessTokenVO tokenVO, BindingResult result) {
        if (result.hasErrors()) {
            return new BaseResponseVO().setResult(Constant.STATUS_FAIL).setMessage(result.getAllErrors().get(0).getDefaultMessage());
        }
        return service.accessToken(tokenVO);
    }

    @RequestMapping("/addUser")
    @ResponseBody
    public BaseResponseVO addUser(@Valid UserDTO userDTO, BindingResult result) {
        if (result.hasErrors()) {
            return new BaseResponseVO().setResult(Constant.STATUS_FAIL).setMessage(result.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            User user = new User();
            user.setUserName(userDTO.getUserName());
            user.setPassword(userDTO.getPassword());
            user.setEmail(StringUtils.isBlank(userDTO.getEmail()) ? " " : userDTO.getEmail());
            user.setAddTime(DateUtils.getCurrentSecondIntValue());
            String suppCode = userDTO.getSuppCode();
            if (StringUtils.isNotBlank(suppCode)) {
                user.setSuppCode(suppCode);
                user.setIsSuppUser((byte) 1);
            }
            return userService.addUser(user);
        } catch (Exception e) {
            logger.error("新增或编辑用户异常，user=" + JSON.toJSONString(userDTO), e);
            return new BaseResponseVO().setResult(Constant.STATUS_FAIL).setMessage("新增用户异常，" + e.getMessage());
        }
    }


}
