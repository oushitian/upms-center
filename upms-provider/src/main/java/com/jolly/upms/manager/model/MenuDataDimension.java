package com.jolly.upms.manager.model;

import com.jolly.upms.manager.model.base.BaseEntity;

public class MenuDataDimension extends BaseEntity {
    private Integer menuId;

    private Integer dimensionId;

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

    public Integer getDimensionId() {
        return dimensionId;
    }

    public void setDimensionId(Integer dimensionId) {
        this.dimensionId = dimensionId;
    }
}