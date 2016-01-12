package com.youku.login.sns.util;

import com.youku.login.sns.BaseSns;



public class ConfigUtil {
	
	public enum SnsType{
		SINA_WEIBO,QQ_ACCOUNT,TUDOU,RENREN,TECENT_WEIBO,QZONE
	}

	/**
	 * 当前操作的社交网站对象
	 * */
	public static BaseSns oauthInter;
	/**
	 * 请求的token信息
	 * */
	public static Object tokenInfo;
	/**
	 * SharedPreferences的文件名
	 * */
	public static final String sharePreferencesName="config";
	// ---------------------sina
	//App Key：3465353328
	//App Secret：566c4364acc59d1c27898ea2061c363c
//	public static final String sina_client_id = "3465353328";//appKey
//	public static final String sina_client_secret="566c4364acc59d1c27898ea2061c363c";//appSecret
	public static final String sina_client_id = "2684493555";//appKey
	public static final String sina_client_secret="3bfe84ed86b5e33dfc6b608736fec550";//appSecret
	public static final String sina_Authoriz_url = "https://open.weibo.cn/oauth2/authorize";
	public static final String sina_Access_token_url = "https://api.weibo.com/oauth2/access_token";
	public static final String sina_redirect_uri = "http://www.youku.com";
	//----------------------qq
	public static final String qq_client_id = "200004";
	public static final String qq_client_secret="1334d6fe7a1143bf8c8e33fe873b06d4";
//	public static final String qq_Authoriz_url = "https://openmobile.qq.com/oauth2.0/m_authorize";
	public static final String qq_Authoriz_url = "https://graph.qq.com/oauth2.0/authorize";
	public static final String qq_Authoriz_openid_url = "https://graph.qq.com/oauth2.0/me";
	public static final String qq_get_userinfo_url = "https://openmobile.qq.com/user/get_user_info?access_token=B67C3A8D1B1C34763DF6F2B1366D8D6E&oauth_consumer_key=200004&openid=78124E8B01D69FB578D454B43F04E8FA";
	public static final String qq_redirect_uri = "http://www.youku.com";

	
	
}
