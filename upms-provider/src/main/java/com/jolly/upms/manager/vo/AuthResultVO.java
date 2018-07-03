package com.jolly.upms.manager.vo;

/**
 * 认证返回参数
 *
 * Created by dengjunbo
 * on 16/5/19.
 */
public class AuthResultVO extends BaseResponseVO {

    private Integer userId;
    private String userName;
    private String token;

    public AuthResultVO(byte result) {
        super(result, null);
    }

    public Integer getUserId() {
        return userId;
    }

    public AuthResultVO setUserId(Integer userId) {
        this.userId = userId;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public AuthResultVO setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public String getToken() {
        return token;
    }

    public AuthResultVO setToken(String token) {
        this.token = token;
        return this;
    }
}
