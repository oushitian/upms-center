package jolly.upms.admin.web.interceptor;

import com.alibaba.fastjson.JSON;
import com.jolly.upms.facade.constant.UpmsConstant;
import com.jolly.upms.manager.model.Application;
import com.jolly.upms.manager.model.AuthUser;
import com.jolly.upms.manager.service.AuthService;
import com.jolly.upms.manager.util.Constant;
import com.jolly.upms.manager.util.LogUtils;
import com.jolly.upms.manager.util.OriginalObjectHolder;
import com.jolly.upms.manager.util.RequestUtil;
import jolly.upms.admin.web.vo.RespResult;
import org.apache.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.WebUtils;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Set;

/**
 * @author chenjc
 * @since 2018-03-28
 */
public class UpmsAccessInterceptor extends HandlerInterceptorAdapter {

    private static Logger logger = Logger.getLogger(UpmsAccessInterceptor.class);

    @Resource
    private AuthService authService;

    /**
     * 只需要校验token的url
     */
    private Set<String> onlyNeedValidateTokenUrls;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        String servletPath = request.getServletPath();
        Cookie cookie = WebUtils.getCookie(request, UpmsConstant.TOKEN_NAME);
        if (cookie != null) {
            String token = cookie.getValue();
            //token校验
            AuthUser authUser = authService.getAuthUser(token);
            if (authUser == null) {
                logger.info("无效的token，请重新登录！token=" + token + "，请求url=" + servletPath);
                rejectAccess(request, response);
                return false;
            }
            if (onlyNeedValidateTokenUrls.contains(servletPath)) {
                return true;
            }
            //权限校验
            Application application = authService.getApplication(Constant.UPMS_APPKEY);
            boolean b = authService.validatePermission(authUser, application, servletPath, null);
            if (!b) {
                logger.info("没有权限！token=" + token + "，请求url=" + servletPath);
                rejectPerm(request, response);
                return false;
            }
            return true;
        }
        logger.info("请先登录！请求url=" + servletPath);
        rejectAccess(request, response);
        return false;
    }

    private void rejectPerm(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //AJAX请求
        if (RequestUtil.isAjax(request)) {
            printResp("Permission Denied", response);
        } else {
            response.sendRedirect(request.getContextPath() + "/refuse");
        }
    }

    private void rejectAccess(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //AJAX请求
        if (RequestUtil.isAjax(request)) {
            printResp("Need Login", response);
        } else {
            //不同于response.sendRedirect()，该方法可以跳出iframe
            PrintWriter writer = response.getWriter();
            writer.write("<html><script>window.open('" + request.getContextPath() + "/api/loginUI','_top');</script></html>");
            writer.flush();
        }
    }

    private void printResp(String msg, HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        RespResult respResult = new RespResult(RespResult.RespCode.NO_PRIVILEGE.getCode(), msg);
        PrintWriter writer = response.getWriter();
        writer.write(JSON.toJSONString(respResult));
        writer.flush();
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 保存日志
        LogUtils.saveLog(request, handler, ex);
        OriginalObjectHolder.removeOriginal();
        OriginalObjectHolder.removeNew();
    }

    public void setOnlyNeedValidateTokenUrls(Set<String> onlyNeedValidateTokenUrls) {
        this.onlyNeedValidateTokenUrls = onlyNeedValidateTokenUrls;
    }
}