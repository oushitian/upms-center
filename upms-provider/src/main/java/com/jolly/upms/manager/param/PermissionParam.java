package com.jolly.upms.manager.param;

import com.jolly.upms.manager.vo.SignVO;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by berkeley on 2017/6/14.
 */
public class PermissionParam extends SignVO {
    @NotEmpty
    private String appKey;

    @NotEmpty
    private String token;
    @NotEmpty(message ="url不能为空")
    private String url;

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
