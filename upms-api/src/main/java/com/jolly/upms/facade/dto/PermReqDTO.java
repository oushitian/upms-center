package com.jolly.upms.facade.dto;

/**
 * @author chenjc
 * @since 2017-11-27
 */
public class PermReqDTO extends BaseRequestDTO {

    private static final long serialVersionUID = 268340399313982479L;

    /**
     * 2.获取用户拥有的全部二级菜单url
     * 3.获取用户拥有的全部按钮url；如果父菜单URL有值，则获取指定父菜单下的按钮url
     * null.获取用户拥有的全部url；如果父菜单URL有值，则获取指定父菜单下的全部url
     */
    private Byte menuType;

    /**
     * 父菜单URL
     */
    private String parentMenuUrl;

    public String getParentMenuUrl() {
        return parentMenuUrl;
    }

    public void setParentMenuUrl(String parentMenuUrl) {
        this.parentMenuUrl = parentMenuUrl;
    }

    public Byte getMenuType() {
        return menuType;
    }

    public void setMenuType(Byte menuType) {
        this.menuType = menuType;
    }
}
