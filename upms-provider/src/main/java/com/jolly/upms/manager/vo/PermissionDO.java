package com.jolly.upms.manager.vo;

public class PermissionDO {

    private String permissionString;

    private String attributeName;

    private String attributeValues;

    public PermissionDO() {
    }

    public PermissionDO(String permissionString) {
        this.permissionString = permissionString;
    }

    public String getPermissionString() {
        return permissionString;
    }

    public void setPermissionString(String permissionString) {
        this.permissionString = permissionString;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getAttributeValues() {
        return attributeValues;
    }

    public void setAttributeValues(String attributeValues) {
        this.attributeValues = attributeValues;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PermissionDO that = (PermissionDO) o;

        return permissionString != null ? permissionString.equals(that.permissionString) : that.permissionString == null;
    }

    @Override
    public int hashCode() {
        return permissionString != null ? permissionString.hashCode() : 0;
    }
}