package com.jolly.upms.manager.util;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by moon
 * on 2015/6/23.
 */
public class Constant {

    public static String CHARSET = "utf-8";
    public static final String KEY_NAME = "key";
    public static String KEY_VALUE;

    //发送成功状态
    public static final byte STATUS_OK = 1;
    public static final byte STATUS_FAIL = 0;

    //token redis 前缀名称
    public static String AUTH_TOKEN_CACHE_PREFIX = "auth_token:";

    //token cooke名称
    public static String AUTH_SSO_COOKIE_NAME = "auth_token_new";

    public static final String UPMS_APPKEY = "upms";

    //==========================================发邮件=============================================================
    //邮件来源
    public static final Integer SITE_TYPE_PC = 1;

    //邮件模板参数
    public static final Map<String, String> MAIL_PARAM = new HashMap<String, String>();

    public static final String DEFAULT_ROUTE_KEY = "email.forward";

    public static void init() throws IOException {
        Properties properties = PropertiesLoaderUtils.loadProperties(new ClassPathResource("config/upms-context.properties"));
        KEY_VALUE = properties.getProperty("api.sign.key");
    }

}
