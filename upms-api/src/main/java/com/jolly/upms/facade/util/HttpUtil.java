package com.jolly.upms.facade.util;

import com.jolly.upms.facade.util.httpclient.HttpRequest;
import com.jolly.upms.facade.util.httpclient.HttpResult;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author mars
 * @create 2017-06-22 11:16 AM
 **/
public class HttpUtil {

    private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    /**
     * 通过url，请求服务，获取json对象
     *
     * @param params
     * @param url
     * @return
     */
    public static String getUrlValue(String url, Map<String, String> params) {

        try {
            List<NameValuePair> formContent = new ArrayList<>();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                formContent.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            HttpEntity httpEntity = new UrlEncodedFormEntity(formContent, "UTF-8");
            //读取远程数据
            HttpResult httpResult = HttpRequest.Post(url).form(httpEntity).execute();

            //请求失败则直接返回  判断http响应码
            if (!httpResult.isSuccess()) {
                return null;
            }
            return httpResult.getData();
        } catch (Throwable e) {
            logger.error("getUrlValue error url= " + url, e);
        }
        return null;
    }


}
