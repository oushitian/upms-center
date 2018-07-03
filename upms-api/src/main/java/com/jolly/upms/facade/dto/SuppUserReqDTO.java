package com.jolly.upms.facade.dto;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * @author chenjc
 * @since 2017-09-06
 */
public class SuppUserReqDTO extends BaseRequestDTO {

    private static final long serialVersionUID = -3467517363579988690L;

    @NotEmpty
    private String suppCode;

    @NotNull
    private Boolean deleteUser;

    public String getSuppCode() {
        return suppCode;
    }

    public void setSuppCode(String suppCode) {
        this.suppCode = suppCode;
    }

    public Boolean getDeleteUser() {
        return deleteUser;
    }

    public void setDeleteUser(Boolean deleteUser) {
        this.deleteUser = deleteUser;
    }
}
