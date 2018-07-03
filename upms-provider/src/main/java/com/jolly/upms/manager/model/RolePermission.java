package com.jolly.upms.manager.model;

import com.jolly.upms.manager.model.base.BaseEntity;

public class RolePermission extends BaseEntity {

    private Integer recId;

    private Integer roleId;

    private String permissionString;

    private String attributeName;

    private String attributeValues;

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

    public String getPermissionString() {
        return permissionString;
    }

    public void setPermissionString(String permissionString) {
        this.permissionString = permissionString == null ? null : permissionString.trim();
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName == null ? null : attributeName.trim();
    }

    public String getAttributeValues() {
        return attributeValues;
    }

    public void setAttributeValues(String attributeValues) {
        this.attributeValues = attributeValues == null ? null : attributeValues.trim();
    }
}