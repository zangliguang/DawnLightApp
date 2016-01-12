package com.youku.login.sns;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebViewDatabase;

import com.example.youkuloginsdk.R;
import com.youku.login.base.YoukuLoginBaseActivity;
import com.youku.login.network.HttpIntent;
import com.youku.login.network.HttpRequestManager;
import com.youku.login.network.IHttpRequest.IHttpRequestCallBack;
import com.youku.login.network.URLContainer;
import com.youku.login.service.ILogin;
import com.youku.login.sns.bean.QQAccountToken;
import com.youku.login.sns.bean.SinaWeiboToken;
import com.youku.login.sns.util.ConfigUtil;
import com.youku.login.util.ErrorCodeUtil;
import com.youku.login.util.Logger;
import com.youku.login.util.Youku;
import com.youku.login.util.YoukuUtil;
import com.youku.login.widget.YoukuLoading;


/**
 * 授权登录页面
 * 
 * @author afei
 * 
 */
public class AuthorizationLoginActivity extends YoukuLoginBaseActivity {

	private final String LOGTAG = "AuthorizationAct";
	private WebView authorizationView;
//	private SharedPreferences sp;
	public static String LOGIN_BROADCAST = "login_broadcast";
	private final static int SHOW_LOADING_PROGRESS = 100;
	private final static int DISSMISS_LOADING_PROGRESS = 200;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_authorization_sns);
		initView(ConfigUtil.oauthInter.getAuthorizeURL());
	}  
  
	private void initView(String url) { 
//		sp = getSharedPreferences(ConfigUtil.sharePreferencesName, MODE_PRIVATE);
		authorizationView = (WebView) findViewById(R.id.authorizationView);
		mHandler.sendEmptyMessage(SHOW_LOADING_PROGRESS);
		CookieSyncManager.createInstance(this);
		CookieSyncManager.getInstance().startSync();
		if (CookieManager.getInstance().hasCookies()) {
			CookieManager.getInstance().removeSessionCookie();
			CookieManager.getInstance().removeAllCookie();
			CookieManager.getInstance().removeExpiredCookie();
		} 
		authorizationView.clearCache(true);
		authorizationView.clearHistory();
		authorizationView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		authorizationView.getSettings().setJavaScriptEnabled(true);
		authorizationView.setWebViewClient(new WebViewC());
		authorizationView.loadUrl(url);
	}
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
//		getSupportActionBar().setDisplayUseLogoEnabled(false);
//		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		return true;
	}

	Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case SHOW_LOADING_PROGRESS:
				YoukuLoading.show(AuthorizationLoginActivity.this);
				break;
			case DISSMISS_LOADING_PROGRESS:
				YoukuLoading.dismiss();
				break;

			default:
				break;
			}
			
		}
		
	};
	
	class WebViewC extends WebViewClient {
		private int index = 0;

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {

			view.loadUrl(url);
			return true;
		}

		/**
		 * 由于腾讯授权页面采用https协议 执行此方法接受所有证书
		 */
		public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
			handler.proceed();
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
//			super.onPageStarted(view, url, favicon);
			/**
			 * url.contains(ConfigUtil.callBackUrl) 如果授权成功url中包含之前设置的callbackurl
			 * 包含：授权成功 index == 0 由于该方法onPageStarted可能被多次调用造成重复跳转 则添加此标示
			 */
			Logger.lxf("======url请求地址==========="+url);
			mHandler.sendEmptyMessage(SHOW_LOADING_PROGRESS);
			if (!YoukuUtil.hasInternet()) {
				YoukuUtil.showTips(R.string.tips_no_network);
				mHandler.sendEmptyMessage(DISSMISS_LOADING_PROGRESS);
				return;
			}
			if (url.startsWith(ConfigUtil.oauthInter.getRedirectURI()) && index == 0) {
				++index;
				if (ConfigUtil.oauthInter instanceof LoginByQQAccount) {
					String[] params = url.split("#")[1].split("&");
					QQAccountToken qqTokenSM = new QQAccountToken();
					int flagCount = 0;
					for (String str : params) {
						String[] strArr = str.split("=");
						if ("access_token".equals(strArr[0])&&!TextUtils.isEmpty(strArr[1])) {
							qqTokenSM.access_token = strArr[1];
							flagCount++;
						}
						if ("expires_in".equals(strArr[0])&&!TextUtils.isEmpty(strArr[1])) {
							qqTokenSM.expires_in = strArr[1];
							flagCount++;
						} 
						if ("openid".equals(strArr[0])&&!TextUtils.isEmpty(strArr[1])) {
							qqTokenSM.openid = strArr[1];
							flagCount++;
						}
					}
					requestBindOrLogin("Qzone", "", qqTokenSM.access_token, "", qqTokenSM.expires_in + "", qqTokenSM.expires_in + "");
					Logger.lxf("=======QQToken等于========="+qqTokenSM);
					return;
				}else if (ConfigUtil.oauthInter instanceof LoginBySinaWeibo) {
					Uri uri = Uri.parse(url);
					((LoginBySinaWeibo) ConfigUtil.oauthInter).setCode(uri.getQueryParameter("code"));
					((LoginBySinaWeibo) ConfigUtil.oauthInter).setState(uri.getQueryParameter("state"));
					new Thread(new Runnable() {
						@Override
						public void run() {
							ServiceShell.getSinaTokenInfo(new ServiceShellListener<SinaWeiboToken>() {

								@Override
								public void completed(SinaWeiboToken data) {
									ConfigUtil.tokenInfo = data;
									Logger.lxf("=====data=======返回的token结果是======="+data);
									requestBindOrLogin("sina", data.uid, data.access_token, "", data.expires_in + "", data.expires_in + "");
									((LoginBySinaWeibo) ConfigUtil.oauthInter).setCode("");
									((LoginBySinaWeibo) ConfigUtil.oauthInter).setState("");
								}

								@Override
								public boolean failed(String message) {
									return false;
								}
							});
						}
					}).start();
				}
			}
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			mHandler.sendEmptyMessage(DISSMISS_LOADING_PROGRESS);
		}

	}
    
    /**
     * 登出操作
     * 
     * 不含清除cookie的登出
     * 
     */
    public void logout() {
		// 取消正在下载的视频
//		UploadInfo info = UploadProcessor.getUploadingTask();
//		if (info != null) {
//			info.setStatus(UploadInfo.STATE_PAUSE);
//		}
//		if (null != info && !TextUtils.isEmpty(info.getTaskId())) {
//			NotificationManager nm = (NotificationManager) Youku.context
//					.getSystemService(Context.NOTIFICATION_SERVICE);
//			nm.cancel(Integer.parseInt(info.getTaskId()));
//		}
//		UploadProcessor.cancelUploadNotifaction();

		// 改变登录状态
		Youku.isLogined = false;
		Youku.userName = "";
		// 将登录状态存到本地
		Youku.savePreference("isNotAutoLogin", true);
		Youku.savePreference("isLogined", false);

		Youku.savePreference("uploadAccessToken", "");
		Youku.savePreference("uploadRefreshToken", "");
		Youku.savePreference("uid", "");
		Youku.savePreference("userIcon", "");
		
		Youku.savePreference("loginAccount", "");
		Youku.savePreference("loginPassword", "");

		Youku.userprofile = null;
		// 发送登出成功广播
		Youku.mContext.sendBroadcast(new Intent(ILogin.LOGOUT_BROADCAST));

		/*new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					DefaultHttpClient httpClient = new DefaultHttpClient(
							createHttpParams());
					HttpPost p = new HttpPost(
							URLContainer.getPushCollectionURL(3, null));
					p.setHeader("User-Agent", Youku.User_Agent);
					HttpResponse r = httpClient.execute(p);
					if (r.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
						// Youku.showTips("login collection success");
					}
				} catch (ClientProtocolException e) {
					Logger.e("LoginManagerImpl", e);
				} catch (IOException e) {
					Logger.e("LoginManagerImpl", e);
				}
			}
		}).start();*/
	}
	
	// 请求绑定或者登录第三方SNS社交app
	private void requestBindOrLogin(String snsType, String tuid, String access_token, String refresh_token, String expire_in, String r_expire_in) {
		HttpRequestManager httpRequestManager = new HttpRequestManager();
		httpRequestManager.setParseErrorCode(true);
		httpRequestManager.setSaveCookie(true);
		httpRequestManager.request(new HttpIntent(URLContainer.getBindOrLoginUrl(snsType, tuid, access_token, refresh_token, expire_in, r_expire_in), HttpRequestManager.METHOD_POST, true),
				new IHttpRequestCallBack() {
					@Override
					public void onSuccess(HttpRequestManager httpRequestManager) {
						String result = httpRequestManager.getDataString();
						pareResult(result);
						Logger.lxf("=====success===result====" + result);
//						Intent intent = new Intent();
//						intent.setClass(AuthorizationLoginActivity.this, HomePageActivity.class);
////						intent.putExtra("tab", 1);
//						startActivity(intent);
						finish();
						mHandler.sendEmptyMessage(DISSMISS_LOADING_PROGRESS);
					}

					@Override
					public void onFailed(String failReason) {
						Logger.lxf("=====failReason====" + failReason);
						ErrorCodeUtil.getInstance().showErrorMessage4SNSLogin(failReason);
						mHandler.sendEmptyMessage(DISSMISS_LOADING_PROGRESS);
					}
				});
	}

	
	
	/**
	 * 第三方登录成功之后跳转
	 * @param jsonStr
	 */
	private void pareResult(String jsonStr) {
		try {
			if (!TextUtils.isEmpty(jsonStr)) {
				JSONObject jsonObject = new JSONObject(jsonStr);
				if (jsonObject.has("status") && "success".equals(jsonObject.optString("status"))) {
					logout();
					JSONObject resultJsonObject = jsonObject.optJSONObject("results");
					String userName = getJsonValueStr(resultJsonObject, "username");
					Youku.savePreference("userName", userName);
					Youku.savePreference("userIcon", getJsonValueStr(resultJsonObject, "icon_large"));
					Youku.savePreference("uid", getJsonValueStr(resultJsonObject, "userid"));
					Youku.savePreference("isNotAutoLogin", false);
					Youku.savePreference("isLogined", true);
					Youku.isLogined = true;
					Youku.loginAccount = userName;
					// 发送登入成功广播
					Youku.mContext.sendBroadcast(new Intent(LOGIN_BROADCAST));
					Youku.iStaticsManager.trackLoginPageLoginClick();
					
					YoukuUtil.showTips(getString(R.string.login_success));
					/*new Thread(new Runnable() {

						@Override
						public void run() {
							try {
								DefaultHttpClient httpClient = new DefaultHttpClient(createHttpParams());
								HttpPost p = new HttpPost(URLContainer.getPushCollectionURL(2, null));
								p.setHeader("User-Agent", Youku.User_Agent);
								HttpResponse r = httpClient.execute(p);
								if (r.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
								}
							} catch (ClientProtocolException e) {
								Logger.e("LoginManagerImpl", e);
							} catch (IOException e) {
								Logger.e("LoginManagerImpl", e);
							} catch (Exception e) {
								Logger.e("LoginManagerImpl", e);
							}
						}
					}).start();*/

				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private static final int CON_TIME_OUT_MS = 15 * 1000;
	private static final int SO_TIME_OUT_MS = 15 * 1000;
	private static final int SOCKET_BUFFER_SIZE = 8 * 1024;

	private HttpParams createHttpParams() {
		HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
		HttpConnectionParams.setConnectionTimeout(params, CON_TIME_OUT_MS);
		HttpConnectionParams.setSoTimeout(params, SO_TIME_OUT_MS);
		HttpConnectionParams.setSocketBufferSize(params, SOCKET_BUFFER_SIZE);
		return params;
	}

	// 返回JSONObject的值
	public String getJsonValueStr(JSONObject resultJsonObject, String str) {
		if (null != resultJsonObject && resultJsonObject.has(str) && !TextUtils.isEmpty(resultJsonObject.optString(str))) {
			return resultJsonObject.optString(str);
		}
		return "";
	}

	//@Override
	public String getPageName() {
		// TODO Auto-generated method stub
		return "AuthorizationLoginActivity";
	}

	@Override
	protected void onDestroy() {
		authorizationView.setVisibility(View.GONE);
		super.onDestroy();
		Logger.lxf("===WebView 验证界面执行 onDestory()====");
		clearWebviewCookie();
		if (authorizationView != null){
			authorizationView.removeAllViews();
			authorizationView.destroy();
		}
	}

	/**
	 * 去除webview的cookie，防止出现自动登录上次账号的情况
	 * 
	 * @Title: clearWebviewCookie
	 * @return void
	 * @date 2013-7-16 下午5:58:41
	 */
	private void clearWebviewCookie() {
		if (authorizationView != null) {
			CookieSyncManager.createInstance(this);
			CookieSyncManager.getInstance().startSync();
			if (CookieManager.getInstance().hasCookies()) {
				CookieManager.getInstance().removeSessionCookie();
				CookieManager.getInstance().removeAllCookie();
				CookieManager.getInstance().removeExpiredCookie();
			}
			clearWebViewCache();
		}
	}

	/**
	 * 去除webview缓存
	 * 
	 * @Title: clearWebViewCache
	 * @return void
	 * @date 2013-7-16 下午6:00:11
	 */
	private void clearWebViewCache() {
		if (authorizationView == null)
			return;
		try {
			CookieManager.getInstance().removeAllCookie();
			CookieManager.getInstance().removeExpiredCookie();
			CookieManager.getInstance().removeSessionCookie();
			String[] fileList = this.fileList();
			for (String file : fileList) {
				this.deleteFile(file);
			}
			WebViewDatabase.getInstance(this).clearUsernamePassword();
			WebViewDatabase.getInstance(this).clearHttpAuthUsernamePassword();

			authorizationView.clearSslPreferences();
			authorizationView.clearView();

			authorizationView.clearFormData();
			authorizationView.clearHistory();
			authorizationView.clearCache(true);
			authorizationView.clearMatches();

			authorizationView.freeMemory();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
}
