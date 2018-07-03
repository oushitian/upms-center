package com.jolly.upms.manager.util;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * berkeley
 */
public class WebUtils extends org.springframework.web.util.WebUtils {
	//主域名正则
	private static String regex1 = "[0-9a-zA-Z]+((\\.com)|(\\.cn)|(\\.org)|(\\.net)|(\\.edu)|(\\.com.cn))";
	/**
	 * 判断是否ajax请求
	 * spring ajax 返回含有 ResponseBody 或者 RestController注解
	 * @param handlerMethod HandlerMethod
	 * @return 是否ajax请求
	 */
	public static boolean isAjax(HandlerMethod handlerMethod) {
		ResponseBody responseBody = handlerMethod.getMethodAnnotation(ResponseBody.class);
		if (null != responseBody) {
			return true;
		}
		RestController restAnnotation = handlerMethod.getBeanType().getAnnotation(RestController.class);
		if (null != restAnnotation) {
			return true;
		}
		return false;
	}
	
	/**
	 * 读取cookie
	 * @param request
	 * @param
	 * @return
	 */
	public static String getCookieValue(HttpServletRequest request, String name) {
		Cookie cookie = getCookie(request, name);
		return cookie != null ? cookie.getValue() : null;
	}

	/**
	 * 清除 某个指定的cookie 
	 * @param response
	 * @param key
	 */
	public static void removeCookie(HttpServletResponse response, String key) {
		setCookie(response, key, null, 0);
	}

	/**
	 * 设置cookie
	 * @param response
	 * @param name
	 * @param value
	 * @param maxAgeInSeconds
	 */
	public static void setCookie(HttpServletResponse response, String name, String value, int maxAgeInSeconds) {
		Cookie cookie = new Cookie(name, value);
		cookie.setPath("/");
		cookie.setMaxAge(maxAgeInSeconds);
		cookie.setHttpOnly(true);
		response.addCookie(cookie);
	}

	public static void setAuthCookie(HttpServletRequest request, HttpServletResponse response, String token) {
		Cookie cookie = new Cookie(Constant.AUTH_SSO_COOKIE_NAME, token);
		cookie.setDomain(getPrimaryDomain4Cookie(request.getServerName()));
		cookie.setMaxAge(-1);
		cookie.setPath("/");
		cookie.setHttpOnly(true);
		response.addCookie(cookie);
	}

	/**
	 * 获取种cookie的主域名
	 *
	 * @param serverName 访问域名
	 * @return 如:  ".jollycorp.com"
	 */
	public static String getPrimaryDomain4Cookie(String serverName) {
		Pattern p = Pattern.compile(regex1);
		Matcher m = p.matcher(serverName);
		List<String> strList = new ArrayList<>();
		while (m.find()) {
			strList.add(m.group());
		}
		String primaryDomain = strList.toString();
		primaryDomain = primaryDomain.substring(1, primaryDomain.length() - 1);
		if (primaryDomain.isEmpty()) return serverName;//返回IP地址或localhost
		return primaryDomain;
	}
}
