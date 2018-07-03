package com.jolly.upms.manager.vo;

/**
 * @author chenjc
 * @since 2017-06-19
 */
public class RoleSaveVO {

    private Integer roleId;
    private String name;
    private Integer applicationId;
    private String menuGroupIds;
    private String dimensionValueDetails;

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
        this.name = name;
    }

    public Integer getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Integer applicationId) {
        this.applicationId = applicationId;
    }

    public String getMenuGroupIds() {
        return menuGroupIds;
    }

    public void setMenuGroupIds(String menuGroupIds) {
        this.menuGroupIds = menuGroupIds;
    }

    public String getDimensionValueDetails() {
        return dimensionValueDetails;
    }

    public void setDimensionValueDetails(String dimensionValueDetails) {
        this.dimensionValueDetails = dimensionValueDetails;
    }
}
