package com.jolly.upms.manager.vo;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by dengjunbo
 * on 16/5/19.
 */
public class AccessTokenVO extends SignVO {

    @NotEmpty
    private String appKey;

    @NotEmpty
    private String token;

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
