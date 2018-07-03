package com.jolly.upms.manager.model;

import com.jolly.upms.manager.model.base.BaseEntity;

public class MenuGroupDetail extends BaseEntity {
    private Integer menuGroupId;

    private Integer menuId;

    public Integer getMenuGroupId() {
        return menuGroupId;
    }

    public void setMenuGroupId(Integer menuGroupId) {
        this.menuGroupId = menuGroupId;
    }

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }
}