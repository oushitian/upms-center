package com.jolly.upms.facade.dto;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author chenjc
 * @since 2017-12-21
 */
public class SimpleUserReqDTO implements Serializable {

    private static final long serialVersionUID = -5521026159126765523L;

    @NotEmpty
    private String appKey;

    @NotNull
    private Integer userId;

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
