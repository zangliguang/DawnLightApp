package com.youku.login.network;

import java.io.UnsupportedEncodingException;

import android.content.pm.ApplicationInfo;
import android.text.TextUtils;

import com.youku.analytics.data.Device;
import com.youku.login.config.Profile;
import com.youku.login.statics.IStaticUtil;
import com.youku.login.util.Logger;
import com.youku.login.util.Youku;
import com.youku.login.util.YoukuUtil;

public class URLContainer {
	
	public static String YOUKU_PUSH_DOMAIN;
	public static String YOUKU_DOMAIN;
//	public static final String TEST_YOUKU_DOMAIN = "http://test.api.3g.youku.com";
	public static final String METHOD_POST = "POST";
	public static String YOUKU_AD_DOMAIN = "http://ad.api.3g.youku.com";
	public static final String TEST1_YOUKU_DOMAIN = "http://test1.api.3g.youku.com";
	public static final String TEST_YOUKU_PUSH_DOMAIN = "http://211.151.146.169";
	
	/**
	 * 正式服务器地址
	 */
	public static final String OFFICIAL_YOUKU_DOMAIN = "http://api.mobile.youku.com";
	public static final String OFFICIAL_YOUKU_USER_DOMAIN = "http://user.api.3g.youku.com";
	public static final String OFFICIAL_YOUKU_PUSH_DOMAIN = "http://push.m.youku.com";
	
	public static String YOUKU_USER_DOMAIN = OFFICIAL_YOUKU_USER_DOMAIN;
	
	
	/**	  新版本 防止盗链	 */
	public static final String NEWSECRET = "631l1i1x3fv5vs2dxlj5v8x81jqfs2om";
	
	
	/**  本地时间与服务器时间戳 单位 秒	 */
	public static long TIMESTAMP = 0;
	private static String initData;
	private static final String SECRET_TYPE = "md5";
	public static final String METHOD_GET = "GET";
	
	
	public static void init() {
		final StringBuilder s = new StringBuilder();
		s.append("pid=").append(Profile.Wireless_pid);  
		if (!TextUtils.isEmpty(Youku.GUID))
			s.append("&guid=").append(Youku.GUID);
		s.append("&mac=").append(Device.mac).append("&imei=")
				.append(Device.imei).append("&ver=").append(Youku.versionName);
		initData = s.toString();
		Logger.d("URLContainer#statistic", initData);
	}
	
	/**
	 * 用户登录
	 * 
	 * @return
	 */
	public static String getLoginURL(String username, String password) {
		StringBuilder s = new StringBuilder();
		s.append(YOUKU_USER_DOMAIN)
				.append(getStatisticsParameter(METHOD_POST, "/user/passport/login"))
				.append("&uname=").append(URLEncoder(username)).append("&pwd=")
				.append(URLEncoder(password));
		return s.toString();
	}
	
	
	/**
	 * 初始化统计需求参数 pid guid ver operator network
	 */
	public static String getStatisticsParameter(String requestMethod,
			String relativePath) {
		init();
		long tmp = System.currentTimeMillis() / 1000 + TIMESTAMP;
		String timeStamp = String.valueOf(tmp);// 时间戳
		String numRaw = requestMethod + ":" + relativePath + ":" + timeStamp
				+ ":" + NEWSECRET;
		String md5NumRaw = YoukuUtil.md5(numRaw);
		StringBuilder s = new StringBuilder();
		s.append(relativePath).append("?");
		s.append(initData);
		s.append("&_t_=").append(timeStamp);
		s.append("&e=").append(SECRET_TYPE);
		s.append("&_s_=").append(md5NumRaw);
		if (!TextUtils.isEmpty(Device.operator))
			s.append("&operator=").append(Device.operator);
		if (!TextUtils.isEmpty(Device.network))
			s.append("&network=").append(Device.network);
		return s.toString();
	}
	
	private static String URLEncoder(String s) {
		if (s == null || s.length() == 0)
			return "";
		try {
			s = java.net.URLEncoder.encode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return "";
		} catch (NullPointerException e) {
			return "";
		}
		return s;
	}
	
/*	*//**
	 * push系统上传collectionApi参数
	 * 
	 * @param actionType
	 *            1 用户启动应用 2 用户登录 3 用户登出 4 用户切换应用中的推送状态
	 * @param status
	 *            PUSH状态，action=4时必填。ENABLED 可用，DISABLED 禁用
	 * @return
	 *//*
	public static String getPushCollectionURL(int actionType, String status) {
		StringBuilder s = new StringBuilder();
		s.append(YOUKU_PUSH_DOMAIN)
				.append(getStatisticsParameter(METHOD_POST,
						"/collect-api/v1/guid_devices"))
				.append("&app=1&platform=")
				.append(Youku.isTablet ? 4 : 3)
				.append("&action=")
				.append(actionType)
				.append("&gdid=")
				.append(Device.gdid)
				.append("&version=")
				.append(Youku.versionName)
				.append("&token=")
				.append(Device.deviceid + 1)
				.append("&grade=")
				.append(Youku.isHighEnd ? "1" : "9")
				.append("&test=")
				.append((Youku.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0 ? "0"
						: "0").append("&payload_type=1,3,4,5,6,7,9,10").append(Youku.isHighEnd ? ",8" : "").append("&qos=1");
		if (actionType == 2 || actionType == 3) {
			s.append("&user_id=").append(Youku.getPreference("uid"));
		}
		if (actionType == 4 && status != null)
			s.append("&status=").append(status);
		String url = s.toString();
		if (actionType == 4)
			Logger.d("Push_getPushCollectionURL()", url);
		return url;
	}*/
	
	private static boolean isSearchUrl(String url) {
		return url.contains("/search/");
	}
	
	public static String updateUrl(String url, String requestMethod) {
		String relativePath = url.substring(7);
		relativePath = relativePath.substring(relativePath.indexOf("/"),
				relativePath.indexOf("?"));
		if (isSearchUrl(url)) {
			String key = relativePath.substring(relativePath.lastIndexOf("/"));
			relativePath = relativePath.substring(0,
					relativePath.lastIndexOf("/"))
					+ URLDecoder(key);
		}
		long tmp = System.currentTimeMillis() / 1000 + TIMESTAMP;
		String timeStamp = String.valueOf(tmp);// 时间戳
		String numRaw = requestMethod + ":" + relativePath + ":" + timeStamp
				+ ":" + NEWSECRET;
		String md5NumRaw = YoukuUtil.md5(numRaw);
		StringBuffer urlStr = new StringBuffer(url);
		int i = urlStr.indexOf("_t_");
		int j = urlStr.indexOf("&", i);
		urlStr.delete(i, j + 1);
		i = urlStr.indexOf("_s_");
		j = urlStr.indexOf("&", i);
		urlStr.delete(i, j + 1);
		urlStr.append("&_t_=").append(timeStamp);
		urlStr.append("&_s_=").append(md5NumRaw);
		url = urlStr.toString();
		return url;
	}
	
	private static String URLDecoder(String s) {
		if (s == null || s.length() == 0)
			return "";
		try {
			s = java.net.URLDecoder.decode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return "";
		} catch (NullPointerException e) {
			return "";
		}
		return s;
	}
	
	/**
	 * 忘记密码请求URL
	 * 
	 * @return
	 */
	public static String getForgetPasswordPageUrl() {
		return "http://www.youku.com/user_getPwd/";
	}
	
	public static String getWeiXinLoginUrl(String code) {
		StringBuilder sb = new StringBuilder();
		sb.append(YOUKU_USER_DOMAIN);
		sb.append(getStatisticsParameter(METHOD_POST, "/user/third/weixin/bindorlogin"));
		sb.append("&client=1");
		sb.append("&code=" + code);
		return sb.toString();
	}
	
	//获取手机验证码
	public static String getPhoneAthoriteCodeUrl(String mobileStr){
		StringBuilder sb = new StringBuilder();
		sb.append(YOUKU_USER_DOMAIN);
		sb.append(getStatisticsParameter(METHOD_POST, "/user/third/mobile/code"));
		sb.append("&mobile="+mobileStr);
		Logger.lxf("===获取手机验证码=========="+sb.toString());
		return sb.toString();
	}
	
	//获取手机号码注册登录URL
	public static String getPhoneRegistLoginUrl(String mobileStr,String codeStr,String passwordStr){
		StringBuilder sb = new StringBuilder();
		sb.append(YOUKU_USER_DOMAIN);
		sb.append(getStatisticsParameter(METHOD_POST, "/user/third/mobile/register"));
		sb.append("&mobile="+URLEncoder(mobileStr));
		sb.append("&code="+URLEncoder(codeStr));
//		sb.append("&password="+URLEncoder(passwordStr));
		sb.append("&password="+URLEncoder(YoukuUtil.md5(passwordStr)));
		Logger.lxf("===获取手机号码注册登录URL=========="+sb.toString());
		return sb.toString();
	}
	
	/**
	 * 用户注册
	 * 
	 * @return
	 */
	public static String getRegistURL(String username, String password,
			String email) {
		StringBuilder s = new StringBuilder();
		s.append(YOUKU_USER_DOMAIN)
				.append(getStatisticsParameter(METHOD_POST,
						"/openapi-wireless/user/register")).append("&puid=")
				.append(URLEncoder(username)).append("&email=")
				.append(URLEncoder(email)).append("&pwd=")
				.append(YoukuUtil.md5(URLEncoder(password)));
		Logger.d("Youku","register url: " + s.toString());
		return s.toString();
	}
	
	//获取用户绑定或者登录地址
	public static String getBindOrLoginUrl(String snsType,String tuid,String access_token,
			String refresh_token, String expire_in,String r_expire_in){
		StringBuilder sb = new StringBuilder();
		sb.append(YOUKU_USER_DOMAIN);
		sb.append(getStatisticsParameter(METHOD_POST, "/user/third/bindorlogin"));
		sb.append("&app="+snsType);
		sb.append("&tuid="+tuid);
		sb.append("&access_token="+access_token);
		sb.append("&refresh_token="+refresh_token);
		sb.append("&expire_in="+expire_in);
		sb.append("&r_expire_in="+r_expire_in);
		Logger.lxf("===获取用户绑定或者登录地址========="+sb.toString());
		return sb.toString();
	}
	
	//获取第三方登录URL，包括授权，登录
	 public static String getThirdLoginOrAuthorizUrl(String urlStr){
			StringBuilder sb = new StringBuilder();
			String urlQCode = "";
			sb.append(YOUKU_DOMAIN);
			sb.append(getStatisticsParameter(METHOD_GET, "/thirdpart/snapshot"));
			if(Youku.getPreferenceBoolean("isLogined")){
				sb.append("&account="+Youku.getPreference("loginAccount", ""))
				.append("&password="+Youku.getPreference("loginPassword", ""));
				sb.append("&source_guid=" + IStaticUtil.URLEncoder(urlStr));
			}else{
				sb.append("&source_guid=" + IStaticUtil.URLEncoder(urlStr));
			}
			Logger.d("sgh","third party: "+sb.toString());
			return sb.toString();
	}
	 
	 public static String getSaosaoTvLoginUrl(String source_guid,
				String account, String password) {
			return YOUKU_USER_DOMAIN
					+ getStatisticsParameter(METHOD_GET, "/thirdpart/snapshot")
					+ "&source_guid=" + source_guid + "&account="
					+ URLEncoder(account) + "&password=" + password;
	}
	 
	 /**
	 * 
	 * TODO 二维码扫描
	 * 
	 * @return
	 * @author 郝金莲
	 * @Created 2013-3-27 下午05:19:59
	 */
	public static String getSaosaoUrl(String tiny) {
		return YOUKU_DOMAIN
				+ getStatisticsParameter(METHOD_GET, "/videos/qrcode")
				+ "&tiny=" + tiny;
	}
	
	/** 获取短链接*/
	public static String getShortLinkUrl(String longURL) {
		StringBuilder builder = new StringBuilder(URLContainer.YOUKU_DOMAIN);
		builder.append(getStatisticsParameter(METHOD_GET, "/user/shorturl"));
		builder.append("&url="+URLEncoder(longURL));
		return builder.toString();
	}
	
	/**
	 * 视频详情接口地址v3.0
	 * 
	 * @param videoid
	 * @return DetailUrl
	 */
	public static String getVideoDownloadDetailUrl(String videoid) {
		String u = YOUKU_DOMAIN
				+ getStatisticsParameter(METHOD_GET,
						"/openapi-wireless/videos/" + videoid + "/v3");
		Logger.d("Download_getVideoDownloadDetailUrl()", u);
		return u;
	}
	
}
