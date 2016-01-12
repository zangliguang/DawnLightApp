package com.youku.login.sns;

import java.net.URLEncoder;

import android.text.TextUtils;

import com.youku.login.sns.util.ConfigUtil;
import com.youku.login.util.Logger;



public class LoginBySinaWeibo extends BaseSns {
	/**
	 * 是否强制用户重新登录，true：是，false：否。默认false。
	 * */
	private final boolean forcelogin = false;
	/**
	 * 使用的权限
	 * */
	private final String scope = "all";
	/**
	 * 请求access_token时的请求的类型，填写authorization_code
	 * */
	private final String grant_type = "authorization_code";
	/**
	 * 用于保持请求和回调的状态，在回调时，会在Query Parameter中回传该参数。
	 * */
	private String state;
	/**
	 * 授权页语言，缺省为中文简体版，en为英文版
	 * */
	private String language;
	/**
	 * 手机移动终端的授权页面，适用于支持html5的手机
	 * */
	public final String display = "mobile";
	/**
	 * 调用authorize获得的code值。
	 * */
	private String code;

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String getAuthorizeURL() {
		StringBuilder sb = new StringBuilder(ConfigUtil.sina_Authoriz_url + "?");
		/*
		 * 添加参数
		 */
		try {
			sb.append("client_id="
					+ URLEncoder.encode(ConfigUtil.sina_client_id, encoding));
			sb.append("&redirect_uri="
					+ URLEncoder.encode(ConfigUtil.sina_redirect_uri, encoding));
			sb.append("&forcelogin=" + URLEncoder.encode(forcelogin+"",encoding));
			sb.append("&display=" + URLEncoder.encode(display, encoding));
			if (!TextUtils.isEmpty(scope))
				sb.append("&scope=" + URLEncoder.encode(scope, encoding));
			if (!TextUtils.isEmpty(state))
				sb.append("&state=" + URLEncoder.encode(state, encoding));
			if (!TextUtils.isEmpty(language))
				sb.append("&language=" + URLEncoder.encode(language, encoding));
		} catch (Exception ex) {
			Logger.lxf( ex.getMessage());
		}
		return sb.toString();
	}

	@Override
	public String getRedirectURI() {
		return ConfigUtil.sina_redirect_uri;
	}
}
