package jolly.upms.admin.common.base.interceptor;

import com.alibaba.fastjson.JSON;
import com.jolly.upms.manager.util.Constant;
import com.jolly.upms.manager.util.SignUtil;
import com.jolly.upms.manager.vo.BaseResponseVO;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator
 * on 2016/5/19.
 */
public abstract class SignValid extends HandlerInterceptorAdapter {

    private String signParamName = "sign";

    private List<String> paramsKey = new ArrayList<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {


        Map<String, String> params = new LinkedHashMap<>();

        for (String key : paramsKey) {
            params.put(key, request.getParameter(key));
        }

        String sign = request.getParameter(signParamName);

        if (validSign(sign, params)) {
            return true;
        } else {
            response.getWriter().print(signFailedPrint());
            return false;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

    private String signFailedPrint() {
        BaseResponseVO baseResponseVO = new BaseResponseVO();
        baseResponseVO.setResult(Constant.STATUS_FAIL).setMessage("sign failed");
        return JSON.toJSONString(baseResponseVO);
    }


    protected boolean validSign(String sign, Map<String, String> params) {
        StringBuilder signBuffer = new StringBuilder();

        for (String key : params.keySet()) {
            //如果是密钥参数，则拼接配置的密钥
            if (addKey(key, signBuffer)) {
                continue;
            }

            signBuffer.append(key).append("=").append(params.get(key)).append("&");
        }

        String signStr = signBuffer.toString();
        signStr = signStr.substring(0, signStr.length() - 1);
        String signType = params.get("signType");
        if ("SHA256".equals(signType)) {
            signType = "SHA-256";
        }
        signStr = SignUtil.sign(signType, signStr);

        return sign != null && sign.equalsIgnoreCase(signStr);
    }

    /**
     * 如果是密钥参数，则拼接配置的密钥
     *
     * @param paramName 参数名
     * @param signBuffer  签名串
     */
    private Boolean addKey(String paramName, StringBuilder signBuffer) {
        if (paramName.equals(Constant.KEY_NAME)) {
            signBuffer.append("key").append("=").append(Constant.KEY_VALUE).append("&");
            return true;
        }

        return false;
    }

    protected String getSignParamName() {
        return signParamName;
    }

    protected void setSignParamName(String signParamName) {
        this.signParamName = signParamName;
    }

    protected List<String> getParamsKey() {
        return paramsKey;
    }

    protected void setParamsKey(List<String> paramsKey) {
        this.paramsKey = paramsKey;
    }

}
