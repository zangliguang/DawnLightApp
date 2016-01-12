package com.youku.login.share;

import android.app.Activity;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;

import com.example.youkuloginsdk.R;
import com.youku.login.util.Logger;
import com.youku.login.util.YoukuUtil;


public class ShareManager {
	
	private Activity activity;
	private Handler handler;
	// private Callback callback;
	// private DetailVideoInfo videoInfo;
	private View view;

	public ShareInfo2ThirdManager info2ThirdUtil;
	public ChooserPopuwindow chooserPopuwindow;

	private String videoId;
	private String title;
	private String webUrl;
	 
	/**
	 * 分享WebView地址
	 * 
	 * @param videoId
	 * @param videoName
	 **/
/*	public void shareWebViewUrl(String titleStr,String imgURl,String webUrl,boolean isAdver) {
		if (!YoukuUtil.hasInternet()) {
			YoukuUtil.showTips(R.string.tips_no_network);
			return;
		}
		if (YoukuUtil.checkClickEvent()) {
			ShareVideoInfo info = new ShareVideoInfo(); 
			ShareAppUtil.imageUrl = imgURl;
			info2ThirdUtil = new ShareInfo2ThirdManager(info, null, null, null, false, activity, view, true);
			VideoUrlInfo urlInfo = null;
			//if (TextUtils.isEmpty(this.webUrl) || TextUtils.isEmpty(this.title)) {// 播放未成功
				// Logger.e(TAG, "null == mediaPlayerDelegate");
				if (!TextUtils.isEmpty(titleStr)) {
					urlInfo = new VideoUrlInfo();
					urlInfo.setWeburl(webUrl);
					urlInfo.setTitle(getShareAdvText(titleStr, webUrl,isAdver));
					// Logger.e(TAG, urlInfo.getWeburl() + "===========");
				}
			} 
			if (null != urlInfo && !TextUtils.isEmpty(urlInfo.getWeburl()) && !TextUtils.isEmpty(urlInfo.getTitle())) {
				info2ThirdUtil.getResolveInfo(urlInfo, info);
				chooserPopuwindow = info2ThirdUtil.getChooserPopuwindow();
			}
		}
		
	}
	
	public static String getShareAdvText(String title, String webUrl,boolean isotherSite) {
		StringBuilder sb = new StringBuilder();
		if(isotherSite){
			sb.append("【推广】");
			Logger.lxf("===short link====webUrl=========="+webUrl);
		}
		sb.append(title);
		sb.append("\b "+webUrl);
		return sb.toString();
	}*/

/*	private Activity activity;
	private Handler handler;
	// private Callback callback;
	// private DetailVideoInfo videoInfo;
	private View view;

	public ShareInfo2ThirdManager info2ThirdUtil;
	public ChooserPopuwindow chooserPopuwindow;

	private String videoId;
	private String title;
	private String webUrl;

	public ShareManager(Activity activity, View view, MediaPlayerDelegate mediaPlayerDelegate) {

		if (mediaPlayerDelegate != null && mediaPlayerDelegate.videoInfo != null) {
			this.videoId = mediaPlayerDelegate.videoInfo.getVid();
			this.title = mediaPlayerDelegate.videoInfo.getTitle();
			this.webUrl = mediaPlayerDelegate.videoInfo.getWeburl();
		}

		this.activity = activity;
		this.view = view;

	}

	public ShareManager(Activity activity, View view, String videoId, String title, String webUrl) {
		this.activity = activity;
		this.view = view;
		this.videoId = videoId;
		this.title = title;
		this.webUrl = webUrl;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}
	
	*//**
	 * 分享视频
	 * 
	 * @param videoId
	 * @param videoName
	 *//*
	public void shareVideo(String videoId, String videoName) {
		if (!YoukuUtil.hasInternet()) {
			YoukuUtil.showTips(R.string.tips_no_network);
			return;
		}
		if (YoukuUtil.checkClickEvent()) {
			ShareVideoInfo info = getInitDrawable(videoId);
			info2ThirdUtil = new ShareInfo2ThirdManager(info, null, null, null, false, activity, view, true);
			VideoUrlInfo urlInfo = null;
			if (TextUtils.isEmpty(this.webUrl) || TextUtils.isEmpty(this.title)) {// 播放未成功
				if (!TextUtils.isEmpty(videoId) && !TextUtils.isEmpty(videoName)) {
					urlInfo = new VideoUrlInfo();
					urlInfo.setWeburl("http://v.youku.com/v_show/id_" + videoId + ".html?x&sharefrom=android");
					urlInfo.setTitle(getShareText(videoName, "http://v.youku.com/v_show/id_" + videoId + ".html?x&sharefrom=android"));
				}
			} else {
				urlInfo = new VideoUrlInfo();
				urlInfo.setWeburl(this.webUrl);
				urlInfo.setTitle(getShareText(title, webUrl));
			}
			if (null != urlInfo && !TextUtils.isEmpty(urlInfo.getWeburl()) && !TextUtils.isEmpty(urlInfo.getTitle())) {
				info2ThirdUtil.getResolveInfo(urlInfo, info);
				chooserPopuwindow = info2ThirdUtil.getChooserPopuwindow();
			}
		}

	}
	*//**
	 * 分享直播视频
	 * 
	 * @param videoId
	 * @param videoName
	 *//*
	public void shareLiveVideo(String videoName,String liveUrl,String imgUrl) {
		if (!YoukuUtil.hasInternet()) {
			YoukuUtil.showTips(R.string.tips_no_network);
			return;
		}
		if (YoukuUtil.checkClickEvent()) {
			ShareVideoInfo info = new ShareVideoInfo();
			ShareAppUtil.imageUrl = imgUrl;
			info2ThirdUtil = new ShareInfo2ThirdManager(info, null, null, null, false, activity, view, true);
			VideoUrlInfo urlInfo = null;
			if (TextUtils.isEmpty(this.webUrl) || TextUtils.isEmpty(this.title)) {// 播放未成功
				// Logger.e(TAG, "null == mediaPlayerDelegate");
				if (!TextUtils.isEmpty(videoId) && !TextUtils.isEmpty(videoName)) {
					urlInfo = new VideoUrlInfo();
					urlInfo.setWeburl(liveUrl);
					urlInfo.setTitle(getShareText(videoName, liveUrl));
					// Logger.e(TAG, urlInfo.getWeburl() + "===========");
				}
			} 
			if (null != urlInfo && !TextUtils.isEmpty(urlInfo.getWeburl()) && !TextUtils.isEmpty(urlInfo.getTitle())) {
				info2ThirdUtil.getResolveInfo(urlInfo, info);
				chooserPopuwindow = info2ThirdUtil.getChooserPopuwindow();
			}
		}
		
	}
	

	
	public static String getShareText(String title, String webUrl) {
		StringBuilder sb = new StringBuilder();
		sb.append("【视频：");
		sb.append(title);
		sb.append("】");
		sb.append(webUrl);
		sb.append("（来自于优酷安卓客户端");
		// sb.append(Youku.versionName);
		sb.append("）");
		return sb.toString();
	}
	
	
	
	
	private ShareVideoInfo getInitDrawable(String videoId) {
//		info2ThirdUtil = new ShareInfo2ThirdManager(activity);
		ShareVideoInfo shareVideoInfo = null;
		if (!TextUtils.isEmpty(this.videoId)) {
			shareVideoInfo = ShareAppUtil.getInstance().getDetailVideoInfo(this.videoId);
		} else if (null != videoId) {
			shareVideoInfo = ShareAppUtil.getInstance().getDetailVideoInfo(videoId);
		}
//		if (!TextUtils.isEmpty(ShareAppUtil.imageUrl)) {
//			ShareAppUtil.getDrawableFromUrl(ShareAppUtil.imageUrl);
//		}
		return shareVideoInfo;
	}*/

}
