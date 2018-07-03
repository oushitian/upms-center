package com.jolly.upms.manager.model;

import com.jolly.upms.manager.model.base.BaseEntity;

public class RoleMenuGroup extends BaseEntity {
    private Integer roleId;

    private Integer menuGroupId;

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getMenuGroupId() {
        return menuGroupId;
    }

    public void setMenuGroupId(Integer menuGroupId) {
        this.menuGroupId = menuGroupId;
    }
}