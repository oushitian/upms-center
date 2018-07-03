package com.jolly.upms.manager.model;

import com.jolly.upms.manager.model.base.BaseEntity;

public class UserMenu extends BaseEntity {
    private Integer userId;

    private Integer menuId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }
}