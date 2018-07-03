package com.jolly.upms.manager.vo;

import com.jolly.upms.manager.model.MenuGroup;

/**
 * @author chenjc
 * @since 2017-06-12
 */
public class MenuGroupSaveVO extends MenuGroup {

    private String menuIds;

    public String getMenuIds() {
        return menuIds;
    }

    public void setMenuIds(String menuIds) {
        this.menuIds = menuIds;
    }
}
