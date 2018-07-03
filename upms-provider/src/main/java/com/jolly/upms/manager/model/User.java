package com.jolly.upms.manager.model;


import com.jolly.upms.manager.model.base.BaseEntity;

public class User extends BaseEntity {
    private Integer userId;

    private String userName;

    private String email;

    private String password;

    private Integer addTime;

    private Integer lastLogin;

    private String lastIp;

    private Byte isDeleted;

    private Integer filialeId;

    private Byte isSuppUser;

    private String suppCode;

    private String navList;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public Integer getAddTime() {
        return addTime;
    }

    public void setAddTime(Integer addTime) {
        this.addTime = addTime;
    }

    public Integer getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Integer lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getLastIp() {
        return lastIp;
    }

    public void setLastIp(String lastIp) {
        this.lastIp = lastIp == null ? null : lastIp.trim();
    }

    public Byte getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Byte isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Integer getFilialeId() {
        return filialeId;
    }

    public void setFilialeId(Integer filialeId) {
        this.filialeId = filialeId;
    }

    public Byte getIsSuppUser() {
        return isSuppUser;
    }

    public void setIsSuppUser(Byte isSuppUser) {
        this.isSuppUser = isSuppUser;
    }

    public String getSuppCode() {
        return suppCode;
    }

    public void setSuppCode(String suppCode) {
        this.suppCode = suppCode == null ? null : suppCode.trim();
    }

    public String getNavList() {
        return navList;
    }

    public void setNavList(String navList) {
        this.navList = navList == null ? null : navList.trim();
    }
}