package com.jolly.upms.manager.result;

/**
 * 统一返回结果类
 * Created by berkeley on 2017/2/18.
 */
public class BaseResult {

    // 状态码：1成功，其他为失败
    public int result;

    // 成功为success，其他为失败原因
    public String message;

    // 成功为success，其他为失败原因
    public String localeMessage;


    // 数据结果集
    public Object data;

    public BaseResult() {
    }

    public BaseResult(int result) {
        this.result = result;
    }

    public BaseResult(int result, String message, Object data) {
        this.result = result;
        this.message = message;
        this.data = data;
    }

    public BaseResult(int result, String message, String localeMessage) {
        this.result = result;
        this.message = message;
        this.localeMessage=localeMessage;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getLocaleMessage() {
        return localeMessage;
    }

    public void setLocaleMessage(String localeMessage) {
        this.localeMessage = localeMessage;
    }


}
