package com.jolly.upms.facade.dto;

import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;

/**
 * @author chenjc
 * @since 2017-06-12
 */
public class BaseRequestDTO implements Serializable {

    private static final long serialVersionUID = -1048864123974441889L;

    @NotEmpty
    private String token;

    @NotEmpty
    private String appKey;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }
}
