package com.jolly.upms.manager.vo;

/**
 * User: FeiWei
 * Date: 27/1/16
 */
public enum ResultCodeType {

    SUCCESS((byte)0),
    FAIL((byte)1);

    private byte code;

    ResultCodeType(byte code){
        this.code = code;
    }


    public byte getCode() {
        return code;
    }

    public void setCode(byte code) {
        this.code = code;
    }
}
