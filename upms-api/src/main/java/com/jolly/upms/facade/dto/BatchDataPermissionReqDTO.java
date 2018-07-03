package com.jolly.upms.facade.dto;

import org.hibernate.validator.constraints.NotEmpty;

import java.util.Set;

/**
 * @author chenjc
 * @since 2017-06-30
 */
public class BatchDataPermissionReqDTO extends BaseRequestDTO {

    private static final long serialVersionUID = -3467517363579988690L;

    @NotEmpty
    private Set<String> urls;

    public Set<String> getUrls() {
        return urls;
    }

    public void setUrls(Set<String> urls) {
        this.urls = urls;
    }
}
