package com.jolly.upms.facade.dto;

import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;

/**
 * @author chenjc
 * @since 2018-04-10
 */
public class DataDimensionValuesReqDTO implements Serializable {

    private static final long serialVersionUID = 3382003011155467399L;

    @NotEmpty
    private String appKey;

    /**
     * 数据维度属性名
     */
    @NotEmpty
    private String dimensionAttributeName;

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getDimensionAttributeName() {
        return dimensionAttributeName;
    }

    public void setDimensionAttributeName(String dimensionAttributeName) {
        this.dimensionAttributeName = dimensionAttributeName;
    }
}
