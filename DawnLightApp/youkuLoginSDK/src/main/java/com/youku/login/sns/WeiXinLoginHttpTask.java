package com.youku.login.sns;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.youku.login.network.URLContainer;
import com.youku.login.sns.util.SnsUtil;
import com.youku.login.util.Logger;
import com.youku.login.util.Youku;
import com.youku.login.util.YoukuUtil;
import com.youku.login.widget.YoukuLoading;


public class WeiXinLoginHttpTask extends AsyncTask<String, Void, Integer> {

	private static final String TAG = WeiXinLoginHttpTask.class.getSimpleName();

	private static String LOGIN_BROADCAST = "login_broadcast";

	private static final int CON_TIME_OUT_MS = 15 * 1000;
	private static final int SO_TIME_OUT_MS = 15 * 1000;
	private static final int SOCKET_BUFFER_SIZE = 8 * 1024;

	private static final int WEIXIN_LOGIN_SUCCESS = 100;
	private static final int WEIXIN_LOGIN_FAIL = 101;

	@Override
	protected Integer doInBackground(String... params) {

		try {
			String code = params[0];
			Logger.e(TAG, "WeiXinLoginHttpTask:code:" + code);

			if (!YoukuUtil.hasInternet() || TextUtils.isEmpty(code)) {
				Logger.e(TAG, "WeiXinLoginHttpTask:code:FAIL:YoukuUtil.hasInternet():" + YoukuUtil.hasInternet());
				return WEIXIN_LOGIN_FAIL;
			}

			DefaultHttpClient httpClient = new DefaultHttpClient(createHttpParams());

			String url = URLContainer.getWeiXinLoginUrl(code);
			Logger.e(TAG, "WeiXinLoginHttpTask:url:" + url);

			HttpPost httpPost = new HttpPost(url);
			httpPost.setHeader("User-Agent", Youku.User_Agent);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			InputStream is = httpResponse.getEntity().getContent();
			String jsonString = YoukuUtil.convertStreamToString(is);

			Logger.e(TAG, "WeiXinLoginHttpTask:jsonString:" + jsonString);

			String status = null;
			int statusCode = -1;
			JSONObject jsonObj = null;

			try {
				jsonObj = new JSONObject(jsonString);
				status = jsonObj.optString("status");
				statusCode = jsonObj.optInt("code");
			} catch (Exception e) {
				e.printStackTrace();
			}

			Logger.e(TAG, "WeiXinLoginHttpTask:status:" + status + ",code:" + statusCode);

			if (!TextUtils.isEmpty(status) && "success".equalsIgnoreCase(status) && statusCode == 1) {
				int httpStatusCode = httpResponse.getStatusLine().getStatusCode();

				Logger.e(TAG, "WeiXinLoginHttpTask:httpStatusCode:" + httpStatusCode);

				if (httpStatusCode == HttpStatus.SC_OK) {
					SnsUtil.getInstance().logout();
					Youku.COOKIE = getCookie(httpClient);
					Youku.savePreference("cookie", Youku.COOKIE);
					JSONObject jsonResultObj = jsonObj.getJSONObject("results");
					Youku.userName = jsonResultObj.optString("username");
					String uid = jsonResultObj.optString("uid");
					String userIcon = jsonResultObj.optString("icon_large");
					Youku.isLogined = true;
					Youku.savePreference("userName", Youku.userName);
					Youku.savePreference("isNotAutoLogin", false);
					Youku.savePreference("isLogined", true);
					Youku.savePreference("uid", uid);
					Youku.savePreference("userIcon", userIcon);

					Logger.e(TAG, "WeiXinLoginHttpTask:Youku.userName:" + Youku.userName);
					Logger.e(TAG, "WeiXinLoginHttpTask:uid:" + uid);
					Logger.e(TAG, "WeiXinLoginHttpTask:Youku.COOKIE:" + Youku.COOKIE);
					Youku.iStaticsManager.trackLoginPageLoginClick();
					if (Youku.mContext != null) {
						Youku.mContext.sendBroadcast(new Intent(LOGIN_BROADCAST));
					}

					/*new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								Logger.e(TAG, "WeiXinLoginHttpTask:getPushCollectionURL.start........");
								HttpClient httpClient = new DefaultHttpClient(createHttpParams());
								HttpPost httpPost = new HttpPost(URLContainer.getPushCollectionURL(2, null));
								httpPost.setHeader("User-Agent", Youku.User_Agent);
								HttpResponse httpResponse = httpClient.execute(httpPost);
								int httpStatusCode = httpResponse.getStatusLine().getStatusCode();
								Logger.e(TAG, "WeiXinLoginHttpTask:getPushCollectionURL.httpStatusCode:" + httpStatusCode);
								if (httpStatusCode == HttpStatus.SC_OK) {
									Logger.e(TAG, "WeiXinLoginHttpTask:getPushCollectionURL.success");
								}
							} catch (ClientProtocolException e) {
								e.printStackTrace();
								Logger.e(TAG, "WeiXinLoginHttpTask:getPushCollectionURL.ClientProtocolException:" + e);
							} catch (IOException e) {
								e.printStackTrace();
								Logger.e(TAG, "WeiXinLoginHttpTask:getPushCollectionURL.IOException:" + e);
							} finally {
								Logger.e(TAG, "WeiXinLoginHttpTask:getPushCollectionURL.end........");
							}
						}
					}).start();*/

					return WEIXIN_LOGIN_SUCCESS;
				} else {
					Logger.e(TAG, "WeiXinLoginHttpTask:FAIL:httpStatusCode:" + httpStatusCode);
					return WEIXIN_LOGIN_FAIL;
				}
			} else {
				Logger.e(TAG, "WeiXinLoginHttpTask:FAIL:status:" + status + ",statusCode:" + statusCode);
				return WEIXIN_LOGIN_FAIL;
			}
		} catch (Exception e) {
			Logger.e(TAG, "WeiXinLoginHttpTask:FAIL:Exception:" + e);
			return WEIXIN_LOGIN_FAIL;
		}
	}

	@Override
	protected void onPostExecute(Integer result) {
		super.onPostExecute(result);

		YoukuLoading.dismiss();

		if (WEIXIN_LOGIN_SUCCESS == result) {
			YoukuUtil.showTips("登录成功");
		} else {
			YoukuUtil.showTips("登录失败");
		}
	}

	private HttpParams createHttpParams() {
		HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
		HttpConnectionParams.setConnectionTimeout(params, CON_TIME_OUT_MS);
		HttpConnectionParams.setSoTimeout(params, SO_TIME_OUT_MS);
		HttpConnectionParams.setSocketBufferSize(params, SOCKET_BUFFER_SIZE);
		return params;
	}

	/**
	 * 获取标准 Cookie 并存储
	 * 
	 * @param httpClient
	 */
	private String getCookie(DefaultHttpClient httpClient) {
		final List<Cookie> cookies = httpClient.getCookieStore().getCookies();
		final StringBuilder s = new StringBuilder();
		for (int i = 0, n = cookies.size(); i < n; i++) {
			final Cookie cookie = cookies.get(i);
			final String cookieName = cookie.getName();
			final String cookieValue = cookie.getValue();
			if (cookieName != null && cookieValue != null && cookieName.length() != 0 && cookieValue.length() != 0) {
				s.append(cookieName).append("=").append(cookieValue).append(";");
			}
		}
		return s.toString();
	}
	
	
}
