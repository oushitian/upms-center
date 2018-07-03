package com.jolly.upms.facade.dto;


import org.hibernate.validator.constraints.NotEmpty;

import java.util.Set;

/**
 * @author chenjc
 * @since 2017-12-14
 */
public class FavoriteReqDTO extends BaseRequestDTO {

    private static final long serialVersionUID = 8946450406668099618L;

    /**
     * 需要添加到收藏夹的菜单ID
     */
    @NotEmpty
    private Set<Integer> menuIds;

    public Set<Integer> getMenuIds() {
        return menuIds;
    }

    public void setMenuIds(Set<Integer> menuIds) {
        this.menuIds = menuIds;
    }
}
