package com.jolly.upms.facade.dto;


import java.io.Serializable;
import java.util.List;

public class UserRespDTO implements Serializable {

    private static final long serialVersionUID = -7216991922539139642L;

    private Integer userId;

    private String userName;

    private Byte isSuppUser;

    private String suppCode;

    /**
     * 用户角色名集合
     */
    private List<String> roleNameList;

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
        this.suppCode = suppCode;
    }

    public List<String> getRoleNameList() {
        return roleNameList;
    }

    public void setRoleNameList(List<String> roleNameList) {
        this.roleNameList = roleNameList;
    }
}