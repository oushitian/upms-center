package com.jolly.upms.facade.dto;

import java.io.Serializable;

/**
 * @author chenjc
 * @since 2017-12-15
 */
public class FavoriteRespDTO implements Serializable {

    private static final long serialVersionUID = -4350171916799622902L;

    private Integer menuId;

    private String name;

    private String url;

    private String permissionString;

    private Integer gmtCreated;

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPermissionString() {
        return permissionString;
    }

    public void setPermissionString(String permissionString) {
        this.permissionString = permissionString;
    }

    public Integer getGmtCreated() {
        return gmtCreated;
    }

    public void setGmtCreated(Integer gmtCreated) {
        this.gmtCreated = gmtCreated;
    }
}
