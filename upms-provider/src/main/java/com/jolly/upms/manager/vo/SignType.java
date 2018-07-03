package com.jolly.upms.manager.vo;

/**
 * Created by lichong on 15/5/27.
 * 签名编码方式
 */
public enum SignType {

    SHA256("SHA256"),

    MD5("MD5");

    private final String value ;

    SignType(String s) {
        value = s ;
    }

    public String getValue() {
        return value ;
    }

}
