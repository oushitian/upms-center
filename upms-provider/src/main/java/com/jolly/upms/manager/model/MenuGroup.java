package com.jolly.upms.manager.model;

import com.jolly.upms.manager.model.base.BaseEntity;

public class MenuGroup extends BaseEntity {
    private Integer menuGroupId;

    private String name;

    private Integer gmtCreated;

    private Integer applicationId;

    public Integer getMenuGroupId() {
        return menuGroupId;
    }

    public void setMenuGroupId(Integer menuGroupId) {
        this.menuGroupId = menuGroupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getGmtCreated() {
        return gmtCreated;
    }

    public void setGmtCreated(Integer gmtCreated) {
        this.gmtCreated = gmtCreated;
    }

    public Integer getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Integer applicationId) {
        this.applicationId = applicationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MenuGroup menuGroup = (MenuGroup) o;

        return menuGroupId != null ? menuGroupId.equals(menuGroup.menuGroupId) : menuGroup.menuGroupId == null;
    }

    @Override
    public int hashCode() {
        return menuGroupId != null ? menuGroupId.hashCode() : 0;
    }
}