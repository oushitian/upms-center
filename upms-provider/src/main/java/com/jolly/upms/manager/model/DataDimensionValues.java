package com.jolly.upms.manager.model;

import com.jolly.upms.manager.model.base.BaseEntity;

public class DataDimensionValues extends BaseEntity {
    private Integer recId;

    private Integer dimensionId;

    private String displayName;

    private String attributeValue;

    private String modifier;

    private Integer gmtModified;

    private int isDeleted;

    public Integer getRecId() {
        return recId;
    }

    public void setRecId(Integer recId) {
        this.recId = recId;
    }

    public Integer getDimensionId() {
        return dimensionId;
    }

    public DataDimensionValues setDimensionId_(Integer dimensionId) {
        this.dimensionId = dimensionId;
        return this;
    }

    public String getDisplayName() {
        return displayName;
    }

    public DataDimensionValues setDisplayName_(String displayName) {
        this.displayName = displayName == null ? null : displayName.trim();
        return this;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public DataDimensionValues setAttributeValue_(String attributeValue) {
        this.attributeValue = attributeValue == null ? null : attributeValue.trim();
        return this;
    }

    public void setDimensionId(Integer dimensionId) {
        this.dimensionId = dimensionId;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public Integer getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Integer gmtModified) {
        this.gmtModified = gmtModified;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }
}