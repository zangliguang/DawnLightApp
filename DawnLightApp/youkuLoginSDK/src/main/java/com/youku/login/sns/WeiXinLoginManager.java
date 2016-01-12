package com.youku.login.sns;

import android.content.Context;

import com.example.youkuloginsdk.R;
import com.tencent.mm.sdk.constants.Build;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.youku.login.share.ShareConfig;
import com.youku.login.util.YoukuUtil;
import com.youku.login.widget.YoukuLoading;

public class WeiXinLoginManager {

	private static WeiXinLoginManager mInstance = null;

	private static final Object mInstanceSync = new Object();

	private IWXAPI mIWXAPI = null;

	private Context mContext = null;

	public static WeiXinLoginManager getInstance() {
		synchronized (mInstanceSync) {
			if (mInstance != null) {
				return mInstance;
			}
			mInstance = new WeiXinLoginManager();
		}
		return mInstance;
	}

	public void doLogin(Context mContext) {

		if (!YoukuUtil.hasInternet()) {
			YoukuUtil.showTips(R.string.tips_no_network);
			return;
		}

		this.mContext = mContext;
		createIWXAPI(mContext);

		boolean isWXAppInstalled = mIWXAPI.isWXAppInstalled();
		if (!isWXAppInstalled) {
			YoukuUtil.showTips("请先安装并登录微信");
			return;
		}
 
		boolean isPaySupported = mIWXAPI.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
		if (!isPaySupported) {
			YoukuUtil.showTips("请使用最新版本微信");
			return;
		}
   
		SendAuth.Req req = new SendAuth.Req();
		req.scope = "snsapi_userinfo";
		mIWXAPI.sendReq(req);
		
	}

	public void performWXAppLogin(String code) {

		YoukuLoading.show(mContext);

		new WeiXinLoginHttpTask().execute(code);
	}

	private void createIWXAPI(Context mContext) {
		if (mIWXAPI == null) {
			mIWXAPI = WXAPIFactory.createWXAPI(mContext, ShareConfig.WEIXIN_APP_ID);
			mIWXAPI.registerApp(ShareConfig.WEIXIN_APP_ID);
		}
	}

	private void removeIWXAPI() {
		if (mIWXAPI != null) {
			mIWXAPI.detach();
			mIWXAPI = null;
		}
	}

	public void clear() {
		removeIWXAPI();
		mContext = null;
		mInstance = null;
	}

}
