package com.jolly.upms.facade.dto;

import org.hibernate.validator.constraints.NotEmpty;

import java.util.Set;

/**
 * @author chenjc
 * @since 2017-07-18
 */
public class UserReqDTO extends BaseRequestDTO {

    private static final long serialVersionUID = -3467517363579988690L;

    @NotEmpty
    private Set<Integer> roleIds;

    private Integer userId;

    public Set<Integer> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(Set<Integer> roleIds) {
        this.roleIds = roleIds;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
