package com.jolly.upms.manager.util;

import com.jolly.upms.manager.model.AuthUser;

import java.util.UUID;

/**
 * Created by dengjunbo
 * on 16/5/19.
 */
public class TokenUtil {

    public static String genToken() {
        return UUID.randomUUID().toString();
    }

    public static String genToken(AuthUser user, String appKey) {
        if(user==null|| org.apache.commons.lang3.StringUtils.isBlank(appKey)){
          return  Constant.AUTH_TOKEN_CACHE_PREFIX+genToken();
        }
        return  Constant.AUTH_TOKEN_CACHE_PREFIX+"appKey_"+appKey+":userId_"+user.getUserId()+":"+UUID.randomUUID().toString();
    }
}
