package com.jolly.upms.manager.result;

/**
 * upms系统常量枚举类
 * Created by jolly on 2017/2/18.
 */
public class UpmsResult extends BaseResult {

    public UpmsResult(UpmsResultCode upmsResultCode, Object data) {
        super(upmsResultCode.getCode(), upmsResultCode.getMessage(), data);
    }

    public UpmsResult(UpmsResultCode upmsResultCode) {
        super(upmsResultCode.getCode(), upmsResultCode.getMessage(), upmsResultCode.getLocaleMessage());
    }

}
