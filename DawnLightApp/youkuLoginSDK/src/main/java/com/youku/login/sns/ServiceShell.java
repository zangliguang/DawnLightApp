package com.youku.login.sns;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
import com.youku.login.sns.bean.SinaWeiboToken;
import com.youku.login.sns.util.ConfigUtil;
import com.youku.login.sns.util.HttpClientFactory;


public class ServiceShell {
	private ServiceShell() {

	}

	/**
	 * 获取授权信息请求
	 */
	public static void getSinaTokenInfo(ServiceShellListener<SinaWeiboToken> serviceShellListener) {
		StringBuilder sb = new StringBuilder("https://api.weibo.com/oauth2/access_token?");
		sb.append("client_id=" + ConfigUtil.sina_client_id);
		sb.append("&client_secret=" + ConfigUtil.sina_client_secret);
		sb.append("&grant_type=authorization_code");
		sb.append("&code=" + ((LoginBySinaWeibo) ConfigUtil.oauthInter).getCode());
		sb.append("&redirect_uri=" + ConfigUtil.sina_redirect_uri);
		HttpPost post = new HttpPost(sb.toString());
		send(serviceShellListener, post, SinaWeiboToken.class);
	}

//	/**
//	 * 获取用户的信息
//	 * */
//	public static void getWeiboUserInfo(String access_token, ServiceShellListener<UserSM> serviceShellListener) {
//		StringBuilder sb = new StringBuilder("https://api.weibo.com/2/users/show.json?");
//		try {
//			sb.append("access_token=" + access_token + "&screen_name=" + URLEncoder.encode("披沙拣金", HTTP.UTF_8));
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		HttpGet post = new HttpGet(sb.toString());
//		/*
//		 * 有关Content-Type属性值可以如下两种编码类型： （1）“application/x-www-form-urlencoded”：
//		 * 表单数据向服务器提交时所采用的编码类型， 默认的缺省值就是“application/x-www-form-urlencoded”。
//		 * 然而，在向服务器发送大量的文本、包含非ASCII字符的文本或二进制数据时这种编码方式效率很低。
//		 * （2）“multipart/form-data”： 在文件上载时，所使用的编码类型应当是“multipart/form-data”，
//		 * 它既可以发送文本数据，也支持二进制数据上载。 当提交为单单数据时，
//		 * 可以使用“application/x-www-form-urlencoded”；
//		 * 当提交的是文件时，就需要使用“multipart/form-data”编码类型。
//		 * 在Content-Type属性当中还是指定提交内容的charset字符编码。
//		 * 一般不进行设置，它只是告诉web服务器post提交的数据采用的何种字符编码。
//		 */
//		post.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
//		send(serviceShellListener, post, UserSM.class);
//	}

	/**
	 * 发送请求
	 * */
	@SuppressWarnings("unchecked")
	private static void send(ServiceShellListener listenner, HttpUriRequest request, Class clazz) {
		try {
			HttpClient httpClient = HttpClientFactory.getInstance();
			HttpResponse response = httpClient.execute(request);
			if (response.getStatusLine().getStatusCode() == 200) {
				String msg = EntityUtils.toString(response.getEntity(), "UTF-8");
				listenner.completed(JSON.parseObject(msg, clazz));
			} else {
				listenner.failed("网络问题");
			}
		} catch (Exception e) {
			listenner.failed(e.getMessage());
		}
	}
	
	
	
}
