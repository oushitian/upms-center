/**
 * qccr.com Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package com.jolly.upms.facade.util.httpclient;

import com.jolly.upms.facade.util.httpclient.config.Config;
import com.jolly.upms.facade.util.httpclient.config.RequestConfigBuilders;
import com.jolly.upms.facade.util.httpclient.handler.HttpRetryHandler;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;

import java.net.URISyntaxException;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * http执行 入口
 * 
 * 在用户不指定自定义的{@link Config}的情况下，将启用默认配置
 * 
 * 设置如下(具体详见{@link DefaultConfigs}):
 * 	<ul>
 *     <li>最大连接数500</li>
 * 	   <li>每个Route的默认最大连接数200</li>
 *     <li>从pool中获取连接最多等待时间为2s</li>
 *     <li>连接超时时间为1.5s</li>
 *     <li>数据返回超时时间为5s</li>
 *     <li>SSL默认设置为信任所有</li>
 * 	</ul>
 */
public class HttpRequest {
    /** 默认配置 */
    private Config config = DefaultConfigs.get();
    /** 目前只有post和get */
	private HttpRequestBase httpMethod;
	/** 方便拼接http请求的param */
	private URIBuilder uriBuilder;
	/** 用于cookie的存储 */
	private CookieStore cookieStore = new BasicCookieStore();
	
	private HttpRequest(HttpRequestBase httpMethod, String uri) {
		this.httpMethod = httpMethod;
		try {
            this.uriBuilder = new URIBuilder(uri);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
	}

	/** Get 请求入口*/
    public static HttpRequest Get(final String uri) {
    	return new HttpRequest(new HttpGet(), uri);
    }

    /** Post 请求入口 */
    public static HttpRequest Post(final String uri) {
        return new HttpRequest(new HttpPost(), uri);
    }

   /**
    * post请求 提交表单
    * @return
    * @date: 2016年7月15日 下午6:06:51
    */
    public HttpRequest form(HttpEntity httpEntity) {
        checkNotNull(httpEntity, "httpEntity must be not NULL");
        ((HttpEntityEnclosingRequest)this.httpMethod).setEntity(httpEntity);
        return this;
    }

    /** 添加http请求的头部信息 */
    public HttpRequest addHeader(final String name, final String value) {
        this.httpMethod.addHeader(name, value);
        return this;
    }

    /** 设置http请求的头部信息 */
    public HttpRequest setHeader(final String name, final String value) {
        this.httpMethod.setHeader(name, value);
        return this;
    }
    
    /** 添加http的请求参数 */
    public HttpRequest addParam(String param, String value) {
    	this.uriBuilder.addParameter(param, value);
        return this;
    }

    /** 添加http请求时的cookie信息 */
    public HttpRequest addCookie(String name, String value, String domain) {
        BasicClientCookie cookie = new BasicClientCookie(name, value);
        cookie.setDomain(domain);
        cookieStore.addCookie(cookie);
        return this;
    }
    
    /** 批量添加cookie {@link CookieBuilder}*/
    public HttpRequest addCookies(List<Cookie> cookies) {
        for(Cookie cookie : cookies) {
            cookieStore.addCookie(cookie);
        }
        return this;
    }

    /** 在请求失败的情况下，重试的次数 */
	public HttpRequest retryIfFailed(int retryTimes) {
		HttpRetryHandler httpRetryHandler = config.getRetryHandler();
		checkNotNull(httpRetryHandler, "retry handler was not provided");
		httpRetryHandler.accept(retryTimes);

		return this;
	}

    /** 使用基础的RequestConfig，执行Http请求 */
    public HttpResult execute() {
    	return execute(RequestConfigBuilders.baseBuilder().build());
    }

    /**
     * 根据requestConfig，执行Http请求
     * 
     * @param requestConfig 本次执行的Http请求的配置
     * @return HttpResult 执行结果
     */
    public HttpResult execute(RequestConfig requestConfig) {
    	checkNotNull(requestConfig, "requestConfig should not be null");

    	HttpResponse httpResp = null;
        try {
            httpMethod.setURI(uriBuilder.build());
            httpMethod.setConfig(requestConfig);

            HttpClient httpClient = HttpClientHolder.get(config);

            HttpClientContext context = HttpClientContext.create();
            //添加对cookie的支持
            context.setCookieStore(cookieStore);

            httpResp = httpClient.execute(httpMethod, context);
            return new HttpResult(httpResp, context);
        } catch(Exception e) {
            throw new RuntimeException(e);
        } finally {
            HttpClientUtils.closeQuietly(httpResp);
        }
    }
}

