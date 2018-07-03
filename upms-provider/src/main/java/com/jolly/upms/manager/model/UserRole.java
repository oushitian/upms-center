package com.jolly.upms.manager.model;

import com.jolly.upms.manager.model.base.BaseEntity;

public class UserRole extends BaseEntity {
    private Integer roleId;

    private Integer userId;

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
}