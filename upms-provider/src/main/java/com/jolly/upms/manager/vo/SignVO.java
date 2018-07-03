package com.jolly.upms.manager.vo;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * Created by moon
 * on 2016/3/17.
 */
public class SignVO {

    @NotEmpty(message="ip不能为空")
    private String ip;

    @NotEmpty(message="ip不能为空")
    private String charset;

    //签名方式
    @NotNull(message="signType不能为空")
    private SignType signType ;

    @NotBlank(message="sign不能为空")
    @NotNull
    private String sign;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public SignType getSignType() {
        return signType;
    }

    public void setSignType(SignType signType) {
        this.signType = signType;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
