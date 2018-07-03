package com.jolly.upms.manager.vo;

import java.util.Map;
import java.util.Set;

/**
 * @author chenjc
 * @since 2017-05-25
 */
public class PermissionVO {

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

    public PermissionVO() {
    }

    public PermissionVO(String permissionString) {
        this.permissionString = permissionString;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PermissionVO that = (PermissionVO) o;

        return permissionString != null ? permissionString.equals(that.permissionString) : that.permissionString == null;
    }

    @Override
    public int hashCode() {
        return permissionString != null ? permissionString.hashCode() : 0;
    }
}
