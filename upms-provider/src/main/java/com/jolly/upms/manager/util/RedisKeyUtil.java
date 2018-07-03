package com.jolly.upms.manager.util;

/**
 * @author chenjc
 * @since 2017-07-04
 */
public class RedisKeyUtil {

    public static String applicationKey(String appKey) {
        return "upms:app:" + appKey;
    }

    public static String userPermitApplicationKey(Integer userId) {
        return "upms:user:permit:app:" + userId;
    }

    public static String permissionKey(Integer applicationId, Integer userId) {
        return "upms:perm:user:" + userId + ":app:" + applicationId;
    }

    public static String dataPermissionKey(Integer applicationId, Integer userId, String url) {
        return "upms:data:perm:user:" + userId + ":app:" + applicationId + ":url:" + url;
    }
}
