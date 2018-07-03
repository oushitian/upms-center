package com.jolly.upms.facade.dto;

import java.io.Serializable;

public class BaseResponseDTO<T> implements Serializable {

    private static final long serialVersionUID = -6390376766952858533L;

    public enum RespCode {

        SUCCESS(1, "success"),
        FAILURE(0, "failure"),
        EXCEPTION(-1, "exception"),
        INVALID_TOKEN(-2, "invalid token");

        private final int code;

        private final String msg;

        RespCode(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
    }

    private Integer result = RespCode.SUCCESS.getCode();

    private String message = RespCode.SUCCESS.getMsg();

    private T data;

    public BaseResponseDTO() {
    }

    public BaseResponseDTO(Integer result, String message, T data) {
        this.result = result;
        this.message = message;
        this.data = data;
    }

    public BaseResponseDTO(Integer result, String message) {
        this.result = result;
        this.message = message;
    }

    public BaseResponseDTO(T data) {
        this.data = data;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean success() {
        return this.result == RespCode.SUCCESS.getCode();
    }

    public boolean invalidToken() {
        return this.result == RespCode.INVALID_TOKEN.getCode();
    }

    public boolean failure() {
        return this.result == RespCode.FAILURE.getCode();
    }

}
