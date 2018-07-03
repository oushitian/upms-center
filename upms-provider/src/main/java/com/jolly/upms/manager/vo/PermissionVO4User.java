package com.jolly.upms.manager.vo;

import java.util.Map;
import java.util.Set;

/**
 * @author chenjc
 * @since 2018-02-07
 */
public class PermissionVO4User {

    /**
     * 应用ID
     */
    private Integer applicationId;

    /**
     * 权限串
     */
    private String permissionString;

    /**
     * 数据权限集合
     * key存放数据维度属性名
     * value存放数据维度属性值集合
     */
    private Map<String, Set<String>> dataDimensionMap;

    public PermissionVO4User() {
    }

    public PermissionVO4User(Integer applicationId, String permissionString) {
        this.applicationId = applicationId;
        this.permissionString = permissionString;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PermissionVO4User that = (PermissionVO4User) o;

        if (applicationId != null ? !applicationId.equals(that.applicationId) : that.applicationId != null)
            return false;
        return permissionString != null ? permissionString.equals(that.permissionString) : that.permissionString == null;
    }

    @Override
    public int hashCode() {
        int result = applicationId != null ? applicationId.hashCode() : 0;
        result = 31 * result + (permissionString != null ? permissionString.hashCode() : 0);
        return result;
    }

    public Integer getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Integer applicationId) {
        this.applicationId = applicationId;
    }

    public String getPermissionString() {
        return permissionString;
    }

    public void setPermissionString(String permissionString) {
        this.permissionString = permissionString;
    }

    public Map<String, Set<String>> getDataDimensionMap() {
        return dataDimensionMap;
    }

    public void setDataDimensionMap(Map<String, Set<String>> dataDimensionMap) {
        this.dataDimensionMap = dataDimensionMap;
    }
}
