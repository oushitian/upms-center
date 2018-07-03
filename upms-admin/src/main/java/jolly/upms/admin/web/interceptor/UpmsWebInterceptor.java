package jolly.upms.admin.web.interceptor;

import com.jolly.upms.manager.util.RequestUtil;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author chenjc
 * @since 2017-03-16
 */
public class UpmsWebInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (!RequestUtil.isAjax(request)) {
            if (modelAndView != null && modelAndView.getViewName() != null && !modelAndView.getViewName().contains("forward") && !modelAndView.getViewName().contains("redirect")) {
                modelAndView.addObject("static", request.getContextPath() + "/static");
                modelAndView.addObject("ctx", request.getContextPath());
            }
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
