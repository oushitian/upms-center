/**
 * qccr.com Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package com.jolly.upms.facade.util.httpclient.config;

import com.google.common.base.Objects;
import com.jolly.upms.facade.util.httpclient.HttpClientHolder;
import com.jolly.upms.facade.util.httpclient.handler.HttpRetryHandler;
import org.apache.http.conn.HttpClientConnectionManager;

import javax.net.ssl.SSLContext;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 在配置HttpClient时的相关配置
 */
public class Config {
    /**
     * 必要字段不能为空，要保证全局唯一, 使用建议：
     *  1> APP_ID+MODULE_ID+BIZ_TYPE
     *  2> UUID.randomUUID()
     */
	private String key;
	/** http connection相关的配置*/
	private HttpClientConnectionManager connectionManager;
	/** ssl的相关配置 */
	private SSLContext sslContext;
	/** 重试处理器 */
	private HttpRetryHandler retryHandler;

	/**
	 *  使用建议：
     *  要保证Key全局唯一, 并缓存Config对象.
     *
     *  key的生成策略 :
     *  <ul>
     *  	<li>APP_ID+MODULE_ID+BIZ_TYPE</li>
     *  	<li>UUID.randomUUID()</li>
     *  </ul>
     *
     */
	public Config(String key) {
		checkNotNull(key, "config key should not be null");
		checkArgument(
						!HttpClientHolder.isExist(key),
						String.format("duplicated config key [%s] was found", key));

		this.key = key;
	}

	public String getKey() {
		return key;
	}

    public HttpClientConnectionManager getConnectionManager() {
        return connectionManager;
    }

    public void setConnectionManager(HttpClientConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    public SSLContext getSslContext() {
        return sslContext;
    }

    public void setSslContext(SSLContext sslContext) {
        this.sslContext = sslContext;
    }

    public HttpRetryHandler getRetryHandler() {
		return retryHandler;
	}

	public void setRetryHandler(HttpRetryHandler retryHandler) {
		this.retryHandler = retryHandler;
	}

	@Override
    public int hashCode() {
        return Objects.hashCode(key);
    }

    @Override
    public boolean equals(Object obj) {
         if (obj == null) {
            return false;
         }

         if (getClass() != obj.getClass()) {
            return false;
         }

         final Config other = (Config) obj;

         return  Objects.equal(this.key, other.key);
    }
}
