package com.jolly.upms.manager.model;

import com.jolly.upms.manager.model.base.BaseEntity;

public class RolePermitApplication extends BaseEntity {
    private Integer recId;

    private Integer roleId;

    private Integer applicationId;

    private Integer gmtCreated;

    public Integer getRecId() {
        return recId;
    }

    public void setRecId(Integer recId) {
        this.recId = recId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Integer applicationId) {
        this.applicationId = applicationId;
    }

    public Integer getGmtCreated() {
        return gmtCreated;
    }

    public void setGmtCreated(Integer gmtCreated) {
        this.gmtCreated = gmtCreated;
    }
}