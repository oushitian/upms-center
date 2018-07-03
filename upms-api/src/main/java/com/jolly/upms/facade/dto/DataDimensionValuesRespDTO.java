package com.jolly.upms.facade.dto;

import java.io.Serializable;

/**
 * @author chenjc
 * @since 2018-04-10
 */
public class DataDimensionValuesRespDTO implements Serializable {

    private static final long serialVersionUID = -8705181971425053651L;

    /**
     * 数据维度值显示名
     */
    private String displayName;

    /**
     * 数据维度值属性值
     */
    private String attributeValue;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }
}
