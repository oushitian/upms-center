package com.jolly.upms.manager.model;


import com.jolly.upms.manager.model.base.BaseEntity;

public class Application extends BaseEntity {
    private Integer applicationId;

    private String appName;

    private String appKey;

    private String description;

    private Integer gmtCreated;

    private String modifier;

    private Integer gmtModified;

    private int isDeleted;

    private String domainName;
    public Integer getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Integer applicationId) {
        this.applicationId = applicationId;
    }

    public String getAppName() {
        return appName;
    }

    public Application setThisAppName(String appName) {
        this.appName = appName;
        return this;
    }


    public String getAppKey() {
        return appKey;
    }

    public Application setThisAppKey(String appKey) {
        this.appKey = appKey;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Application setThisDescription(String description) {
        this.description = description;
        return this;
    }

    public Integer getGmtCreated() {
        return gmtCreated;
    }

    public Application setThisGmtCreated(Integer gmtCreated) {
        this.gmtCreated = gmtCreated;
        return this;
    }

    public String getModifier(){
        return modifier;
    }

    public Application setThisModifier(String modifier) {
        this.modifier = modifier;
        return  this;
    }

    public Integer getGmtModified() {
        return gmtModified;
    }

    public Application setThisGmtModified(Integer gmtModified) {
        this.gmtModified = gmtModified;
        return  this;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setGmtCreated(Integer gmtCreated) {
        this.gmtCreated = gmtCreated;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
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

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Application that = (Application) o;

        return applicationId != null ? applicationId.equals(that.applicationId) : that.applicationId == null;
    }

    @Override
    public int hashCode() {
        return applicationId != null ? applicationId.hashCode() : 0;
    }
}