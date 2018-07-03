package com.jolly.upms.manager.param;

import com.jolly.upms.manager.util.Constant;

import java.util.Map;

/**
 * Created by Feiwei on 2/6/16.
 */
public class EmailForwardMqParam {

    String email;
    String templateId;
    String language;
    int channel;
    int siteType;
    Map<String, String> paramsJson;

    public  EmailForwardMqParam(String email, String templateId, String language) {
        this.email = email;
        this.templateId = templateId;
        this.language = language;
        this.channel = 0;
        this.siteType = Constant.SITE_TYPE_PC;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }


    public Map<String, String> getParamsJson() {
        return paramsJson;
    }
    public void setParamsJson(Map<String, String> paramsJson) {

        this.paramsJson = paramsJson;
    }
}
