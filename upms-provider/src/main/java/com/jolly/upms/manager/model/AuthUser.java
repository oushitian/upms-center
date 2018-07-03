package com.jolly.upms.manager.model;

import java.util.Map;

/**
 * Created by dengjunbo
 * on 16/5/19.
 */
public class AuthUser {

    private Integer userId;
    private String userName;

    private Map<String,Menu> permissionMap;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Map<String, Menu> getPermissionMap() {
        return permissionMap;
    }

    public void setPermissionMap(Map<String, Menu> permissionMap) {
        this.permissionMap = permissionMap;
    }
}
