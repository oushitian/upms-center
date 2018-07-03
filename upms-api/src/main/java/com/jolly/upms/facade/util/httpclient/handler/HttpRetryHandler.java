/**
 * qccr.com Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package com.jolly.upms.facade.util.httpclient.handler;

import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.net.SocketTimeoutException;

/**
 * http请求重试处理类
 */
public class HttpRetryHandler implements HttpRequestRetryHandler {
	private final static ThreadLocal<Integer> retryTimes = new ThreadLocal<>();

	@Override
	public boolean retryRequest(IOException exception, int executionCount,
                                HttpContext context) {
		Integer maxTries = retryTimes.get();
		if(maxTries == null) { return false; }

		if ((exception instanceof ConnectTimeoutException ||
			 exception instanceof SocketTimeoutException)
                && executionCount <= maxTries) {

			return true;
		}

		return false;
	}
	
	public void accept(int retryTimes) {
		HttpRetryHandler.retryTimes.set(retryTimes);
	}

}
