package com.youku.login.activity;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.TextUtils;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.SslErrorHandler;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebViewDatabase;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.youkuloginsdk.R;
import com.youku.login.base.YoukuLoginBaseActivity;
import com.youku.login.network.HttpIntent;
import com.youku.login.network.HttpRequestManager;
import com.youku.login.network.IHttpRequest.IHttpRequestCallBack;
import com.youku.login.network.URLContainer;
import com.youku.login.share.ShareAppUtil;
import com.youku.login.util.Logger;
import com.youku.login.util.MD5;
import com.youku.login.util.Utils;
import com.youku.login.util.Youku;
import com.youku.login.util.YoukuUtil;
import com.youku.login.util.ui.AlwaysMarqueeTextView;


/**
 * 用于显示连接的页面 需要传递的参数有 StringExtra("url")* 如果需要传递cookie的话BooleanExtra("getCookie"
 * 
 * @author 张宇
 * @create-time Nov 7, 2012 3:43:52 PM
 * @version $Id
 * 
 */
public class WebViewActivity extends YoukuLoginBaseActivity {

	// private final static String TAG = "WebViewActivity";
	private WebView mWebView;
	private ProgressBar mprogreBar;// 上方的进度细条
	private Context mContext;
	private String url;
	private int countNumber = 0;// 加载次数
	private boolean isOtherSite = false;
	private boolean isAdver = false;//是否是广告
	
	private static final int MSG_REDIRECTED_PLAY = 100;
	private static final long TIME_REDIRECTED_PLAY = 1000;
	private static final int MSG_HANDLER_SHORT_LINK = 2222222;

	private final int JUMP_BROWSE_URL_ID = 111;//跳转到浏览器id
	private AlwaysMarqueeTextView mAlwaysMarqueeTextView = null;
	
	private boolean isBaiduKuaibo = false; //是否百度快播计时两秒后跳转后app内播放
	private String showid = null;
	private String showname = null;
//	private SearchDirectAllOtherSiteSeries mSearchDirectAllOtherSiteSeries = null;
//	private SearchDirectAllOtherSiteEpisode mSearchDirectAllOtherSiteEpisode = null;

	
//	private ImageView webview_refresh_icon,webview_share_icon,webview_more_icon;
//	private ImageView webview_close_icon;
	public String title = "";
//private SimpleMenuDialog mSimpleMenuDialog;	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.youku.ui.BaseActivity#onCreate(android.os.Bundle)
	 */
	/**
	 * intent -------string类型的 url, boolean类型的getCookie
	 */
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_webview);
		mContext = this;
		mWebView = (WebView) findViewById(R.id.webView);
		mprogreBar = (ProgressBar) findViewById(R.id.progress);

		Intent intent = getIntent();
		url = intent.getStringExtra("url");
		Logger.lxf("===长连接地址==="+url);
		isOtherSite = intent.getBooleanExtra("isOtherSite", false);
		isAdver = intent.getBooleanExtra("isAdver", false);
		isBaiduKuaibo = intent.getBooleanExtra("isBaiduKuaibo", false);
		setWebViewCategoryEvent();
			
		final boolean getCookie = intent.getBooleanExtra("getCookie", false);
		final boolean isFromPaidActivity = intent.getBooleanExtra("isFromPaidActivity", false);
		if (url != null && !"".equals(url) && URLUtil.isNetworkUrl(url)) {
			// 有网
			if (YoukuUtil.hasInternet()) {
				if (!YoukuUtil.isWifi()) {// 3g
					YoukuUtil.showTips(R.string.tips_use_3g);
				}
				
				
				// 展示进度以及连接不成功的提示语
				mWebView.setWebViewClient(new WebViewClient() {
					@Override
					public void onPageFinished(WebView view, String url) {
						Logger.d("WebViewActivity", "onPageFinished");
						mprogreBar.setVisibility(View.GONE);
						super.onPageFinished(view, url);
					}

					@Override
					public void onReceivedError(WebView view, int errorCode,
							String description, String failingUrl) {
						mprogreBar.setVisibility(View.GONE);
						YoukuUtil.showTips(R.string.tips_not_responding);
						Logger.d("WebViewActivity", "onReceivedError->code:"
								+ errorCode + "->description:" + description
								+ "->failingUrl:" + failingUrl);
						super.onReceivedError(view, errorCode, description,
								failingUrl);
					}

					@Override
					// 接收任何证书 用于2.2以前接收https， ssl需要认证的地址，直接不认证了
					public void onReceivedSslError(WebView view,SslErrorHandler handler, SslError error) {
						handler.proceed();
						// super.onReceivedSslError(view, handler, error);
					}
					
					@Override
					public boolean shouldOverrideUrlLoading(WebView view,
							String url) {
						if(null!=view){
							Logger.lxf("======load url====hasFocus======"+view.hasFocus());
							view.setFocusable(false);
						}
						if (url == null || "".equals(url)) {
							return true;
						}
						// 发送邮箱
						if (url.startsWith("mailto:")) {
							try {
								Intent intent = new Intent(
										Intent.ACTION_SENDTO, Uri.parse(url));
								startActivity(intent);
							} catch (ActivityNotFoundException e) {
								YoukuUtil
										.showTips(R.string.webview_mail_app_not_found);
							}
							return true;
						} else if (url.startsWith("tel:")) { // 打电话
							try {
								Intent intent = new Intent(Intent.ACTION_DIAL,
										Uri.parse(url));
								startActivity(intent);
							} catch (ActivityNotFoundException e) {
								YoukuUtil.showTips("");
							}
							return true;
						}
						// 如果传来的url是iapk结尾的，就启动下载
						if (url.substring(url.length() - 4).equals(".apk")
								&& countNumber == 0) {
							countNumber++;
							Uri uri = Uri.parse(url);
							Intent intent = new Intent(Intent.ACTION_VIEW, uri);
							startActivity(intent);
							// finish();
						}
						mprogreBar.setVisibility(View.VISIBLE);
						return super.shouldOverrideUrlLoading(view, url);
					}

				});
				mWebView.setWebChromeClient(new WebChromeClient() {

					@Override
					public void onProgressChanged(WebView view, int newProgress) {
						mprogreBar.setProgress(newProgress);
						if (newProgress == 100) {
							mprogreBar.setVisibility(View.GONE);
						}
					}

					@Override
					public void onReceivedTitle(WebView view, String title) {
						super.onReceivedTitle(view, title);
						Logger.lxf("===title====="+title);
						if (!TextUtils.isEmpty(title)) {
							setTitle(title);
							showCustomWebViewTitle(title);
//									setWebViewTitle(title);
						}
					}

				});
				final Handler handler = new Handler(new Callback() {

					@Override
					public boolean handleMessage(Message msg) {

						mWebView.loadUrl(url);
//							if(isAdver){
							createShortLink(url);
//							}
						return false;
					}
				});
				Thread temp = new Thread(new Runnable() {

					@Override
					public void run() {
						CookieSyncManager.createInstance(mContext);
						CookieManager cookieManager = CookieManager
								.getInstance();
						cookieManager.removeAllCookie();
						if (getCookie) {// cookie加入头里
							
							if(isFromPaidActivity){
								//cookieManager.setCookie(url, PaidFragment.getCookies());
								cookieManager.setCookie(url, getCookies());
							}else if (Youku.COOKIE != null
									&& !"".equals(Youku.COOKIE)) {
								String[] cookies = Youku.COOKIE.split(";");
								for (int i = 0; i < cookies.length; i++) {
									cookieManager.setCookie(url, cookies[i]);
								}
							}
							CookieSyncManager.getInstance().sync();
						}
						handler.sendEmptyMessageDelayed(0, 50);
					}
				});
				temp.run();
				if (isOtherSite) {
					YoukuUtil.showTips(R.string.webview_tip);
				}
			} else {// 无网
				// Util.showTips(R.string.none_network);
				YoukuUtil.showTips(R.string.tips_no_network);
				finish();
			}
		} else {// 地址不是http
			YoukuUtil.showTips(R.string.webview_wrong_address);
			finish();
		}
		title = intent.getStringExtra("title");
//		if (null!=title&&title.length()>0) {
//			showCustomTitle();
		showCustomWebViewTitle(getCustomTitleName());
//		}
		
	}
	
	public final static String KEY_WEBVIEW_SIGN_COOKIE = "72c1a30ba31eb088650105d238f6edc4";
	
	/**
	 * <ul>
	 * <li>1. 采用cookie方式 在 youku.com的域名下种cookie</li>
	 * <li>2. cookie名vctag 内容字符串 <br/>
	 * vctag样例 <br/>
	 * platform=1&userid=111111&isvip=1&time=1222223&sign=qwqsdfasdfefwqef<br/>
	 * sign生成规则<br/>
	 * md5(platform=1&userid=111111&isvip=1&time=1222223&key=
	 * qwqsdfasdfefwqef)</li>
	 * 
	 * 
	 * <li>3. key通过邮件方式有双方确认</li>
	 * <li>4. 参数解释：<br/>
	 * platform 平台值 1优酷 2土豆<br/>
	 * userid passportid <br/>
	 * isvip 是否收费会员 1 是 0否<br/>
	 * time unix时间戳<br/>
	 * sign 加密签名<br/>
	 * vctag 生成的字符串需要base64 后url encode<br/>
	 * </li>
	 * </ul>
	 * 
	 * @return
	 */
	public static String getCookies() {

		StringBuilder sb = new StringBuilder();

		sb.append("platform=").append(1);
		sb.append("&userid=")
				.append(Youku.userprofile != null ? Youku.userprofile.results.userid
						: "");
		sb.append("&isvip=").append(Utils.isVipUser() ? 1 : 0);
		sb.append("&time=").append(System.currentTimeMillis());

		String signStr = sb.toString() + "&key=" + KEY_WEBVIEW_SIGN_COOKIE;
		String sign = MD5.md5(signStr.getBytes());

		sb.append("&sign=").append(sign);

		String result = "vctag=";

		result += Base64.encodeToString(sb.toString().getBytes(),
				Base64.DEFAULT);

		result = URLEncoder.encode(result);

		return "vctag=" + result;
	}
	
	
	
	//设置webview的一些属性事件
	@SuppressWarnings("deprecation")
	private void setWebViewCategoryEvent(){
		// 可缩放
		mWebView.getSettings().setBuiltInZoomControls(true);
		mWebView.getSettings().setSupportZoom(true);
		mWebView.getSettings().setUseWideViewPort(true);
		mWebView.getSettings().setLoadWithOverviewMode(true);
		mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		// 客串文件
		mWebView.getSettings().setAllowFileAccess(true);
		// mWebView.getSettings().setDefaultZoom(ZoomDensity.CLOSE);
//		if (Build.VERSION.SDK_INT >= 11) {
//			mWebView.getSettings().setEnableSmoothTransition(true);
//		}
		// 可解析js
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setPluginState(PluginState.ON);
		mWebView.getSettings().setLightTouchEnabled(true);
		mWebView.setFocusable(false);//设置touchEvent事件，使webview中的弹出框正常弹出。。
		mWebView.setOnTouchListener(new View.OnTouchListener(){


			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
		        switch (event.getAction()){
	            case MotionEvent.ACTION_DOWN:
	            case MotionEvent.ACTION_UP:
	            	Logger.lxf("====1111==v.hasFocus()=="+v.hasFocus());
	                if (!v.hasFocus()){
	                    v.requestFocus();
	                    v.setFocusable(true);
	                    v.setFocusableInTouchMode(true);
	                    Logger.lxf("====2222==v.hasFocus()=="+v.hasFocus());
	                }
	                break;
	        }
	        return false;
	    }
		});
	}
	
	private Handler webViewHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_HANDLER_SHORT_LINK:
				url = (String)msg.obj;
				break;

			default:
				break;
			}
		}
		
	};
	
	//创建短链接。。
	private void sendHandlerMessage(String shortUrlAddress){
//		String shortUrlAddress = YoukuUtil.createShortLink(YoukuUtil.URLEncoder(url));
//		try {
//			Thread.sleep(5000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		Message message = new Message();
		Logger.lxf("=======shortUrladdress====地址======"+shortUrlAddress);
		message.obj = shortUrlAddress;
		message.what = MSG_HANDLER_SHORT_LINK;
		webViewHandler.sendMessage(message);
				
	}
	
	public void setTitle(String titleStr){
		this.title = titleStr;
	}
	
	@Override
	public String getCustomTitleName() {
		// TODO Auto-generated method stub
		return title;
	}
	
	// 返回的时候返回到上一层页面而不是退出
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (null!=mWebView && mWebView.canGoBack()
				&& event.getKeyCode() == KeyEvent.KEYCODE_BACK
				&& event.getRepeatCount() == 0) {
			mWebView.goBack();
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public void goBack() {
		super.goBack();
//		if(isBaiduKuaibo) {
//			endRedirect();
//		}
	}
	@Override
	public void onBackPressed() {
		super.onBackPressed();
//		if(isBaiduKuaibo) {
//			endRedirect();
//		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
//		if(isBaiduKuaibo) {
//			endRedirect();
//		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mWebView != null) {
			mWebView.onResume();
		}
		countNumber = 0;// 修复会进行两次apk地址请求bug
	}

	@Override
	protected void onPause() {
		super.onPause();

		if (mWebView != null) {
			mWebView.onPause();
		}
	}
	

	//显示新版的webview头部布局
	public void showCustomWebViewTitle(String title) {
		TextView channel_custom_title_txt = (AlwaysMarqueeTextView) findViewById(R.id.webview_custom_title_txt);
//		webview_close_icon = (ImageView) findViewById(R.id.webview_close_icon);
//		webview_refresh_icon = (ImageView) findViewById(R.id.webview_refresh_icon);
//		webview_share_icon = (ImageView) findViewById(R.id.webview_share_icon);
//		webview_more_icon = (ImageView) findViewById(R.id.webview_more_icon);
//      channel_custom_title_txt.setText(title);
		setOnclickEvent();
	}

	//监听头部view点击事件
	public void setOnclickEvent(){
		/*webview_close_icon.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				WebViewActivity.this.finish();
			}
		});
		webview_refresh_icon.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (!YoukuUtil.hasInternet()) {
					YoukuUtil.showTips(R.string.tips_no_network);
					return ;
				}
				if (mWebView != null) {
					clearWebviewCookie();
					mWebView.reload();
				}
				
			}
		});*/
/*		webview_share_icon.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int drawableId = 0;
				if(isAdver){
					drawableId = R.drawable.webview_share_default_icon;
				}else{
					drawableId = R.drawable.icon;
				}
				String returnName = saveImage2LocalStore(drawableId);
				new ShareManager(WebViewActivity.this, webview_share_icon, url, getCustomTitleName(), null).
					shareWebViewUrl(getCustomTitleName(),returnName, url, isAdver);
				
			}
		});*/
/*		webview_more_icon.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				mSimpleMenuDialog =	new SimpleMenuDialog(WebViewActivity.this, getListMenuObjects(), new SimpleMenuDialog.MenuClick() {
					
					@Override
					public void click(int id) {
						menuClickEvent(id);
					}
				});
				mSimpleMenuDialog.show();
			}
		});*/
	}
/*	*//**
	 * 头部布局更多按钮
	 * 
	 * 点击事件
	 *//*
	private void menuClickEvent(int selectId){
		switch (selectId) {
		case JUMP_BROWSE_URL_ID:
			if (!YoukuUtil.hasInternet()) {
				YoukuUtil.showTips(R.string.tips_no_network);
			}
			if(!TextUtils.isEmpty(url)){
				YoukuUtil.gotoWeb(WebViewActivity.this, url);
			}
			if(null!=mSimpleMenuDialog){
				mSimpleMenuDialog.dismiss();
			}
			break; 
		default:
			break;
		}
	}*/
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
//		switch (item.getItemId()) {
//		case R.id.menu_refreshweb:
//			if (!YoukuUtil.hasInternet()) {
//				YoukuUtil.showTips(R.string.tips_no_network);
//				return true;
//			}
//			if (mWebView != null) {
//				mWebView.reload();
//			}
//			return true;
//		case R.id.menu_gotoweb:
//			if (!YoukuUtil.hasInternet()) {
//				YoukuUtil.showTips(R.string.tips_no_network);
//				return true;
//			}
//			YoukuUtil.gotoWeb(WebViewActivity.this, url);
//			return true;
//		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public String getPageName() {
		return "webview页";
	}

	private void setWebViewTitle(String title) {
//		getSupportActionBar().setDisplayShowCustomEnabled(true);
//		getSupportActionBar().setCustomView(R.layout.custom_title);
		mAlwaysMarqueeTextView = (AlwaysMarqueeTextView) findViewById(R.id.custom_title);
		if (mAlwaysMarqueeTextView != null) {
			mAlwaysMarqueeTextView.setVisibility(View.VISIBLE);
			mAlwaysMarqueeTextView.setText(title);
		}
	}
	
//	private Handler mHandler = new Handler() {
//		@Override
//		public void handleMessage(Message msg) {
//			super.handleMessage(msg);
//			switch (msg.what) {
//			case MSG_REDIRECTED_PLAY:
//				doRedirectPlay();
//				break;
//			default:
//				break;
//			}
//		}
//	};
	
	//百度快播跳转到app内播放
//	private void doRedirectPlay() {
//		if (!YoukuUtil.hasInternet()) {
//			YoukuUtil.showTips(R.string.tips_no_network);
//			finish();
//			return;
//		}
//		if(!Youku.isHighEnd)//低端机
//		{
//			YoukuUtil.showTips("暂不支持该机型");
//			finish();
//			return;
//		}
//		if(null != mSearchDirectAllOtherSiteSeries)
//		{
//			String url = mSearchDirectAllOtherSiteSeries.getUrl();//即将播放视频的url
//			if(TextUtils.isEmpty(url))
//			{
//				YoukuUtil.showTips("抱歉，暂不支持本集播放");
//				finish();
//				return;
//			}
//			if(url.toUpperCase().endsWith(".QMV"))
//			{
//				finish();
//				YoukuUtil.showTips("暂不支持该格式");
//			}
//		}
////		PlayerHelperForBaiduQvod mPlayerHelperForBaiduQvod = new PlayerHelperForBaiduQvod(this,null);
////		if(null!=mPlayerHelperForBaiduQvod)
////		{
////			mPlayerHelperForBaiduQvod.setmAllOtherSiteSeries(mSearchDirectAllOtherSiteSeries);
////		}
//		Intent mIntent = new Intent(this, DetailActivity.class);
//		mIntent.putExtra("showid", showid);
//		mIntent.putExtra("showname", showname);
//		mIntent.putExtra("SearchDirectAllOtherSiteSeries", mSearchDirectAllOtherSiteSeries);
//		mIntent.putExtra("SearchDirectAllOtherSiteEpisode", mSearchDirectAllOtherSiteEpisode);
//		mIntent.putExtra("isBaiduKuaibo", true);
//		mIntent.putExtra("isFromLocal", true);
//		startActivity(mIntent);
//		finish();
//	}
//	public void endRedirect(){
//		mHandler.removeMessages(MSG_REDIRECTED_PLAY);
//	}
//	
//	public void startRedirect() {
//		endRedirect();
//		mHandler.sendEmptyMessageDelayed(MSG_REDIRECTED_PLAY, TIME_REDIRECTED_PLAY);
//	}
	
	@Override
	public void finish() {
	    mWebView.loadUrl("about:blank");
	    super.finish();   
	}
	
	
/*	//组装数据，跳转到浏览器
	public List<MenuItemObect> getListMenuObjects(){
		List<MenuItemObect> itemObjectList = new ArrayList<MenuItemObect>();
		MenuItemObect itemObect = new MenuService(WebViewActivity.this).new MenuItemObect();
		itemObect.mMenuListId = JUMP_BROWSE_URL_ID;
		itemObect.mMenuListDrawble = R.drawable.topbar_brower_icon;
		itemObect.mMenuListName = "在浏览器打开";
		itemObjectList.add(itemObect);
		return itemObjectList;
	}*/
	
	//保存app中的drawable到本地路径中去，方便弹对话框时候调用
	public String saveImage2LocalStore(int drawableId){
		Drawable defaultDrawable = getResources().getDrawable(drawableId);
		Bitmap bitmapDefault = ((BitmapDrawable)defaultDrawable).getBitmap();
		String defaultName = ""+drawableId;
		ShareAppUtil.saveImageToLocal(bitmapDefault,defaultName);
		return defaultName;
	}
	
	/**
	 * 去除webview的cookie，防止出现自动登录上次账号的情况
	 * 
	 * @Title: clearWebviewCookie
	 * @return void
	 * @date 2013-7-16 下午5:58:41
	 */
	private void clearWebviewCookie() {
		if (mWebView != null) {
			if(mWebView.hasFocus()){
				mWebView.setFocusable(false);
			}
			Logger.lxf("======3333=====clear=====mWebView==="+mWebView.hasFocus());
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
		if (mWebView == null)
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

			mWebView.clearSslPreferences();
			mWebView.clearView();

			mWebView.clearFormData();
			mWebView.clearHistory();
			mWebView.clearCache(true);
			mWebView.clearMatches();

			mWebView.freeMemory();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	//创建短连接，无需登录
	public String resultEnd = "";
	public String createShortLink(String longLink){
		
		new HttpRequestManager().request(new HttpIntent(URLContainer.getShortLinkUrl(longLink), true), new IHttpRequestCallBack() {

					@Override
					public void onSuccess(HttpRequestManager request) {
						String result = request.getDataString();
						if(!TextUtils.isEmpty(result)){
							org.json.JSONObject jsonObject;
							try {
								jsonObject = new org.json.JSONObject(result);
								if(jsonObject.has("status")&&"success".equals(jsonObject.opt("status"))){
									if(jsonObject.has("results")&&!TextUtils.isEmpty(""+jsonObject.opt("results"))){
										org.json.JSONObject subJsonObject = jsonObject.optJSONObject("results");
										if(subJsonObject.has("shortURL")&&!TextUtils.isEmpty(""+subJsonObject.opt("shortURL"))){
											resultEnd = subJsonObject.optString("shortURL");
											sendHandlerMessage(resultEnd);
										}
									}
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
							
						}
						Logger.lxf("======create short link success====result======="+result);
						Logger.lxf("======create short link success====resultEndUrl======="+resultEnd);
						
					}

					@Override
					public void onFailed(String failReason) {
						Logger.lxf("======create short link fail=====failReason===="+failReason);
						Logger.lxf("======create short link fail================");
					}
			
		});
		return resultEnd;
	}
}
