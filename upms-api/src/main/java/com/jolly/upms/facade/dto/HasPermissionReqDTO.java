package com.jolly.upms.facade.dto;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author chenjc
 * @since 2018-04-24
 */
public class HasPermissionReqDTO extends BaseRequestDTO {

    private static final long serialVersionUID = -3201459377755028027L;

    @NotEmpty
    private String permissionString;

    public String getPermissionString() {
        return permissionString;
    }

    public void setPermissionString(String permissionString) {
        this.permissionString = permissionString;
    }
}
