package com.jolly.upms.facade.dto;

/**
 * @author chenjc
 * @since 2017-09-06
 */
public class QueryUserReqDTO extends BaseRequestDTO {

    private static final long serialVersionUID = -3467517363579988690L;

    /**
     * 模糊查询
     */
    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
