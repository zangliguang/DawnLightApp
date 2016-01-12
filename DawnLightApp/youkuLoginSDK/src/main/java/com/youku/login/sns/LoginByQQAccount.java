package com.youku.login.sns;

import java.net.URLEncoder;

import android.text.TextUtils;

import com.youku.login.sns.util.ConfigUtil;
import com.youku.login.util.Logger;


public class LoginByQQAccount extends BaseSns {
	/**
	 * 授权对象
	 * */
	private final QQAuthorize authorize = new QQAuthorize();

	public QQAuthorize getAuthorize() {
		return authorize;
	}
	/**
	 * 获取授权界面的URL
	 * */
	@Override
	public String getAuthorizeURL() {
		StringBuilder sb = new StringBuilder(ConfigUtil.qq_Authoriz_url + "?");
		/*
		 * 添加参数
		 */
		try {
			sb.append("client_id="
					+ URLEncoder.encode(ConfigUtil.qq_client_id, encoding));
			sb.append("&response_type=" + authorize.response_type);
			sb.append("&state=" + "youku");
			sb.append("&redirect_uri="
					+ URLEncoder.encode(ConfigUtil.qq_redirect_uri, encoding));
			if (!TextUtils.isEmpty(authorize.scope))
				sb.append("&scope=" + authorize.scope);
		} catch (Exception ex) {
			Logger.lxf(ex.getMessage());
		}
		return sb.toString();
	}

	@Override
	public String getRedirectURI() {
		return ConfigUtil.qq_redirect_uri;
	}

	/**
	 * 授权对象相关的字段
	 * */
	public class QQAuthorize {
		/**
		 * 授权类型，此值固定为“token”
		 * */
		private final String response_type = "token";
		/**
		 * 使用的权限
		 * */
		private String scope = "get_user_info";
		/**
		 * 授权类型，在本步骤中，此值为“authorization_code”
		 * */
		private final String grant_type = "authorization_code";
	}

}
