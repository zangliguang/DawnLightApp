package com.youku.login.sns;

public abstract class BaseSns {

	public final String encoding = "utf-8"; // URL编码方式

	/**
	 * 获取请求授权界面的URL
	 * */
	public abstract String getAuthorizeURL();

	/**
	 * 获取回调地址
	 * */
	public abstract String getRedirectURI();

}
