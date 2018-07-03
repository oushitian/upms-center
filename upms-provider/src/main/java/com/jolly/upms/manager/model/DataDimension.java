package com.jolly.upms.manager.model;

import com.jolly.upms.manager.model.base.BaseEntity;
import org.hibernate.validator.constraints.NotBlank;

public class DataDimension extends BaseEntity {
    private Integer dimensionId;
    @NotBlank
    private String displayName;
    @NotBlank
    private String attributeName;

    private String description;

    private Integer gmtCreated;

    private Integer gmtModified;
    private String modifier;

    private int isDeleted;

    public Integer getDimensionId() {
        return dimensionId;
    }

    public void setDimensionId(Integer dimensionId) {
        this.dimensionId = dimensionId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public DataDimension setDisplayName_(String displayName) {
        this.displayName = displayName == null ? null : displayName.trim();
        return this;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public DataDimension setAttributeName_(String attributeName) {
        this.attributeName = attributeName == null ? null : attributeName.trim();
        return this;
    }

    public String getDescription() {
        return description;
    }

    public DataDimension setDescription_(String description) {
        this.description = description == null ? null : description.trim();
        return this;
    }

    public Integer getGmtCreated() {
        return gmtCreated;
    }

    public DataDimension setGmtCreated_(Integer gmtCreated) {
        this.gmtCreated = gmtCreated;
        return this;
    }

    public Integer getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Integer gmtModified) {
        this.gmtModified = gmtModified;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataDimension that = (DataDimension) o;

        return dimensionId != null ? dimensionId.equals(that.dimensionId) : that.dimensionId == null;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setGmtCreated(Integer gmtCreated) {
        this.gmtCreated = gmtCreated;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Override
    public int hashCode() {
        return dimensionId != null ? dimensionId.hashCode() : 0;
    }
}