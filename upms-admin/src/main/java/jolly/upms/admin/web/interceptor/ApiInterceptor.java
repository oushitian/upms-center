package jolly.upms.admin.web.interceptor;

import com.alibaba.fastjson.JSON;
import com.jolly.upms.facade.dto.BaseResponseDTO;
import com.jolly.upms.facade.util.EncryptAndDecryptUtil;
import com.jolly.upms.manager.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * @author chenjc
 * @since 2017-06-26
 */
public class ApiInterceptor implements HandlerInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiInterceptor.class);

    private String key = "+7HI5RMMEUmMJG6AI2OIpQ==";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        String plain = request.getParameter("plain");
        String sign = request.getParameter("sign");
        if (StringUtils.isBlank(plain) || StringUtils.isBlank(sign)) {
            LOGGER.error("plain or sign不能为空，url={}，token={}", request.getRequestURL(), request.getParameter("token"));
            BaseResponseDTO dto = new BaseResponseDTO(BaseResponseDTO.RespCode.FAILURE.getCode(), "plain or sign不能为空空");
            writeResponse(response, dto);
            return false;
        }
        if (!EncryptAndDecryptUtil.decrypt(key, plain, sign)) {
            LOGGER.error("解密失败，url={}，token={}", request.getRequestURL(), request.getParameter("token"));
            BaseResponseDTO dto = new BaseResponseDTO(BaseResponseDTO.RespCode.FAILURE.getCode(), "解密失败");
            writeResponse(response, dto);
            return false;
        }
        return true;
    }

    private void writeResponse(HttpServletResponse response, BaseResponseDTO dto) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();
        writer.write(JSON.toJSONString(dto));
        writer.flush();
        writer.close();
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
