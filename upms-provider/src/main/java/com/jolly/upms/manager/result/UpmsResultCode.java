package com.jolly.upms.manager.result;

/**
 * upms系统接口结果常量枚举类
 * Created by jolly on 2017/2/18.
 */
public enum UpmsResultCode {

    FAILED(0, "failed","失败"),
    SUCCESS(1, "success","成功"),

    INVALID_LENGTH(10001, "Invalid length","无效的长度"),
    EMPTY_USERNAME(10101, "Username cannot be empty","用户名为空"),
    EMPTY_PASSWORD(10102, "Password cannot be empty","空的密码"),
    INVALID_USERNAME(10103, "Account does not exist","无效的用户名"),
    INVALID_PASSWORD(10104, "Password error","无效的密码"),

    INVALID_TOKEN(-110, "invalid_token","无效的token"),
    PERMISSION_DENIED(-111, "permission_denied","没有权限");



    public int code;

    public String message;

    public String localeMessage;

    UpmsResultCode(int code, String message, String localeMessage) {
        this.code = code;
        this.message = message;
        this.localeMessage=localeMessage;
    }
    UpmsResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLocaleMessage() {
        return localeMessage;
    }

    public void setLocaleMessage(String localeMessage) {
        this.localeMessage = localeMessage;
    }
}
