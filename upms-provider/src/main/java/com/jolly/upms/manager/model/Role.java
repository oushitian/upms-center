package com.jolly.upms.manager.model;

import com.jolly.upms.manager.model.base.BaseEntity;

public class Role extends BaseEntity {
    private Integer roleId;

    private String name;

    private Integer gmtCreated;

    private Integer gmtModified;

    private Integer applicationId;

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
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

    public Integer getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Integer gmtModified) {
        this.gmtModified = gmtModified;
    }

    public Integer getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Integer applicationId) {
        this.applicationId = applicationId;
    }
}