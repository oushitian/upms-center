package com.jolly.upms.manager.vo;

/**
 * @author chenjc
 * @since 2017-07-20
 */
public class UserRoleVO {

    private Integer roleId;

    private Integer userId;

    private String userName;

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

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
}
