package com.youku.login.network;

import android.content.Intent;

/**
 * 
 * @author 张宇
 * @create-time Oct 15, 2012 6:05:45 PM
 * @version $Id
 * 
 */
public class HttpIntent extends Intent {

	public static final String URI = "uri";

	public static final String METHOD = "method";

	public static final String IS_SET_COOKIE = "is_set_cookie";

	public static final String READ_TIMEOUT = "read_timeout";

	public static final String CONNECT_TIMEOUT = "connect_timeout";

	/**
	 * 是否缓存接口返回数据到本地
	 */
	public static final String IS_CACHE_DATA = "is_cache_data";

	// private Object parseObject;

	public HttpIntent(String uri) {
		this(uri, IHttpRequest.METHOD_GET, false, true);
	}

	public HttpIntent(String uri, String reqMethod) {
		this(uri, reqMethod, false, true);
	}

	public HttpIntent(String uri, boolean isSetCookie) {
		this(uri, IHttpRequest.METHOD_GET, isSetCookie, true);
	}

	public HttpIntent(String uri, boolean isSetCookie, boolean isCacheData) {
		this(uri, IHttpRequest.METHOD_GET, isSetCookie, isCacheData);
	}

	public HttpIntent(String uri, String reqMethod, boolean isSetCookie) {
		this(uri, reqMethod, isSetCookie, true);
	}

	public HttpIntent(boolean isAdIntent, String uri) {
		this(uri, IHttpRequest.METHOD_GET, false, true);
	}

	public HttpIntent(String uri, String reqMethod, boolean isSetCookie,
			boolean isCacheData) {
		putExtra(URI, uri);
		putExtra(METHOD, reqMethod);
		putExtra(IS_SET_COOKIE, isSetCookie);
		putExtra(IS_CACHE_DATA, isCacheData);
		putExtra(CONNECT_TIMEOUT, 10000);
		putExtra(READ_TIMEOUT, 10000);
	}

	/**
	 * @param connectTimeout
	 *            the connectTimeout to set
	 */
	public HttpIntent setConnectTimeout(int connectTimeout) {
		putExtra(CONNECT_TIMEOUT, connectTimeout);
		return this;
	}

	/**
	 * @param readTimeout
	 *            the readTimeout to set
	 */
	public HttpIntent setReadTimeout(int readTimeout) {
		putExtra(READ_TIMEOUT, readTimeout);
		return this;
	}

	/**
	 * 设置是否需要缓存接口数据，缺省值是true
	 * 
	 * @param isCache
	 */
	public HttpIntent setCache(boolean isCacheData) {
		putExtra(IS_CACHE_DATA, isCacheData);
		return this;
	}

	// public void putParseObject(Object o) {
	// parseObject = o;
	// }
	//
	// public Object getParseObject() {
	// return parseObject;
	// }

}
