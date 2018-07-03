package com.jolly.upms.facade.dto;

/**
 * @author chenjc
 * @since 2017-07-18
 */
public class GetUserReqDTO extends BaseRequestDTO {

    private static final long serialVersionUID = -3467517363579988690L;

    /**
     * 如果非空，则返回此用户ID对应的用户信息
     */
    private Integer userId;

    /**
     * 如果非空，则返回此用户名对应的用户信息，userId和userName选其一即可
     */
    private String userName;

    /**
     * 只在userId非空时生效。如果为true，查询用户表时不排除已删除的用户
     */
    private Boolean includeDeleted;

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
        this.userName = userName;
    }

    public Boolean getIncludeDeleted() {
        return includeDeleted;
    }

    public void setIncludeDeleted(Boolean includeDeleted) {
        this.includeDeleted = includeDeleted;
    }
}
