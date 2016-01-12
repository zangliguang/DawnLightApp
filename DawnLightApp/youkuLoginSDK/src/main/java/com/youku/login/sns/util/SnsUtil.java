package com.youku.login.sns.util;

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

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.youku.login.network.URLContainer;
import com.youku.login.service.ILogin;
import com.youku.login.upload.UploadInfo;
import com.youku.login.util.Logger;
import com.youku.login.util.Youku;


public class SnsUtil {
	
	public static SnsUtil mSnsUtil;
	
	public SnsUtil(){};
	
	public static SnsUtil getInstance(){
		if(mSnsUtil==null){
			mSnsUtil = new SnsUtil();
		}
		return mSnsUtil;
	}
	
	 /**
     * 登出操作
     * 
     * 不含清除cookie的登出
     * 
     */
    public void logout() {
		// 取消正在下载的视频
/*		UploadInfo info = UploadProcessor.getUploadingTask();
		if (info != null) {
			info.setStatus(UploadInfo.STATE_PAUSE);
		}
		if (null != info && !TextUtils.isEmpty(info.getTaskId())) {
			NotificationManager nm = (NotificationManager) Youku.context
					.getSystemService(Context.NOTIFICATION_SERVICE);
			nm.cancel(Integer.parseInt(info.getTaskId()));
		}
		UploadProcessor.cancelUploadNotifaction();*/

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

}
