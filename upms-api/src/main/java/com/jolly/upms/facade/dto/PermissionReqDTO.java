package com.jolly.upms.facade.dto;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author chenjc
 * @since 2017-06-30
 */
public class PermissionReqDTO extends BaseRequestDTO {

    private static final long serialVersionUID = -3467517363579988690L;

    @NotEmpty
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
