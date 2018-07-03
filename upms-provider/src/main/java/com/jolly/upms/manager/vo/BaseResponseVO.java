package com.jolly.upms.manager.vo;

import java.io.Serializable;

public class BaseResponseVO implements Serializable {

    protected byte result; // 1: 成功  0:失败

    protected String message = "";

    private Object data;

    public BaseResponseVO() {
    }

    public BaseResponseVO(byte result, String message) {
        this.result = result;
        this.message = message;
    }

    public byte getResult() {
        return result;
    }

    public BaseResponseVO setResult(byte result) {
        this.result = result;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public BaseResponseVO setMessage(String message) {
        this.message = message;
        return this;
    }

    public Object getData() {
        return data;
    }

    public BaseResponseVO setData(Object data) {
        this.data = data;
        return this;
    }
}
