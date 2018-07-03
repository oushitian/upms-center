package com.jolly.upms.manager.vo;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * 登陆接口参数对象
 *
 * Created by dengjunbo
 * on 16/5/19.
 */
public class LoginVO extends SignVO {

    @NotEmpty(message = "用户名不能为空")
    private String userName;

    @NotEmpty(message = "密码不能为空")
    private String password;

    @NotEmpty
    private String appKey;


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }
}
