package com.jolly.upms.manager.vo;

/**
 * @author chenjc
 * @since 2018-04-09
 */
public class UserVO4BatchDelete {

    private String userName;

    private String email;

    private String failReason;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFailReason() {
        return failReason;
    }

    public void setFailReason(String failReason) {
        this.failReason = failReason;
    }
}
