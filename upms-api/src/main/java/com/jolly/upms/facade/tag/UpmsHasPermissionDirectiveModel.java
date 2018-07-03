package com.jolly.upms.facade.tag;

import com.alibaba.fastjson.JSON;
import com.jolly.upms.facade.constant.UpmsConstant;
import com.jolly.upms.facade.dto.BaseResponseDTO;
import com.jolly.upms.facade.util.HttpUtil;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 支持freemarker的自定义权限标签<br/>
 * 使用方法：
 * <ul>
 * <li>
 * 1. 在spring配置文件的freemarkerConfigurer中添加以下配置：<br/>
 * &lt;property name="freemarkerVariables"&gt;
 * &lt;map&gt;
 * &lt;entry key="hasPermission"&gt;
 * &lt;bean class="com.jolly.upms.facade.tag.UpmsHasPermissionDirectiveModel"&gt;
 * &lt;property name="appKey" value="应用Key"/&gt;
 * &lt;property name="baseUrl" value="http://172.31.0.91:8080"/&gt;
 * &lt;/bean&gt;
 * &lt;/entry&gt;
 * &lt;/map&gt;
 * &lt;/property&gt;
 * </li>
 * <li>
 * 2. 在ftl页面这样使用标签：<br/>
 * &lt;@hasPermission name="user:edit"&gt;标签体内容&lt;/@hasPermission&gt;
 * </li>
 * </ul>
 *
 * @author chenjc
 * @since 2018-04-23
 */
public class UpmsHasPermissionDirectiveModel implements TemplateDirectiveModel {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpmsHasPermissionDirectiveModel.class);

    @Resource
    private HttpServletRequest request;

    private String appKey;

    private String baseUrl;

    @Override
    public void execute(Environment env, Map params, TemplateModel[] model,
                        TemplateDirectiveBody body) throws TemplateException, IOException {
        if (StringUtils.isBlank(appKey)) {
            return;
        }
        if (StringUtils.isBlank(baseUrl)) {
            return;
        }
        Cookie cookie = getCookie(request, UpmsConstant.TOKEN_NAME);
        if (cookie == null) {
            return;
        }
        Object value = params.get("name");
        if (value == null || StringUtils.isBlank(value.toString())) {
            return;
        }
        final String token = cookie.getValue();
        String userId = StringUtils.substringBetween(token, "userId_", ":");
        final String permissionString = value.toString().trim();
        Byte data = CacheManager.getData(Integer.valueOf(userId), permissionString, new CacheManager.Load() {
            @Override
            public Byte load() {
                Map<String, String> reqMap = new HashMap<>(3);
                try {
                    String url = baseUrl + "/api/hasPermission";
                    reqMap.put("token", token);
                    reqMap.put("appKey", appKey);
                    reqMap.put("permissionString", permissionString);
                    String resp = HttpUtil.getUrlValue(url, reqMap);
                    BaseResponseDTO respDTO = JSON.parseObject(resp, BaseResponseDTO.class);
                    boolean b = respDTO != null && (respDTO.success() || respDTO.failure());
                    if (b) {
                        return respDTO.getResult().byteValue();
                    }
                } catch (Exception e) {
                    LOGGER.error("权限标签远程请求异常，请求数据为：{}", JSON.toJSONString(reqMap), e);
                }
                return null;
            }
        });
        //校验通过
        if (data != null && data.intValue() == BaseResponseDTO.RespCode.SUCCESS.getCode()) {
            body.render(env.getOut());
        }
    }

    private Cookie getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    return cookie;
                }
            }
        }
        return null;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}