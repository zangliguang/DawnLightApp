package com.youku.login.util;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Set;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.BatteryManager;
import android.preference.PreferenceManager;
import android.provider.MediaStore.Audio.Media;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.baseproject.utils.UIUtils;
import com.example.youkuloginsdk.R;
import com.youku.analytics.Constants;
import com.youku.login.config.Profile;
import com.youku.login.network.HttpIntent;
import com.youku.login.network.HttpRequestManager;
import com.youku.login.network.IHttpRequest;
import com.youku.login.network.IHttpRequest.IHttpRequestCallBack;
import com.youku.login.network.URLContainer;
import com.youku.login.service.YoukuService;

public final class Utils {

	private Utils() {
	}
	
	
	public static boolean isVipUser() {
		boolean isVipUser = false;
		if (!TextUtils.isEmpty(Youku.COOKIE)) {
			try {
				// cookie里边的yktk 键的内容是由| 分隔的。用base64
				// 把第4个内容解析出来。解析出来后的内容格式是id:XXX,nn:XXX,vip:true,ytid:XXX,tid:XXX。vip
				// 字段内容true 代表vip .false代表不是。
				org.json.JSONObject objContent = BaseHelper.string2JSON(Youku.COOKIE, ";");
				String yktk = objContent.getString("yktk");
				String decode_yktk = URLDecoder(yktk);
				String[] decode_yktk_array = decode_yktk.split("\\|");
				String base64_temp = decode_yktk_array[3];
				String temp = new String(Base64.decode(base64_temp, Base64.DEFAULT), "utf-8");
				isVipUser = temp.contains("vip:true");
			} catch (Exception e) {
				e.printStackTrace();
				isVipUser = false;
			}
		}
		return isVipUser;
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

/*	public static void setSwipeRefreshLayoutHeight(Context mContext, SwipeRefreshLayout mSwipeRefreshLayout) {
		try {
			Class<SwipeRefreshLayout> mClass = SwipeRefreshLayout.class;
			Field field = mClass.getDeclaredField("mProgressBarHeight");
			field.setAccessible(true);
			DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
			field.set(mSwipeRefreshLayout, (int) (metrics.density * 2F));
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getFormatTime(long millseconds) {
		long seconds = millseconds / 1000;
		StringBuffer buf = new StringBuffer();
		// long hour = seconds / 3600;
		long min = seconds / 60 - hour * 60;
		long sec = seconds - hour * 3600- min * 60;
		
		 * if (hour < 10) { buf.append("0"); } buf.append(hour).append(":");
		 
		if (min < 10) {
			buf.append("0");
		}
		buf.append(min).append(":");
		if (sec < 10) {
			buf.append("0");
		}
		buf.append(sec);
		return buf.toString();
	}

	public static String getFormatTimeForGesture(long millseconds) {
		long tempSeconds = millseconds / 1000;

		StringBuffer buf = new StringBuffer();

		long seconds = tempSeconds % 60;

		long minutes = tempSeconds / 60;

		if (minutes < 10) {
			buf.append("0");
		}
		buf.append(minutes).append(":");
		if (seconds < 10) {
			buf.append("0");
		}
		buf.append(seconds);
		return buf.toString();
	}

	*//**
	 * 是否是剧集网格样式
	 * 
	 * @return
	 *//*
	public static boolean isSeriesGrid() {
		if (DetailDataSource.mDetailVideoInfo != null) {
			switch (DetailDataSource.mDetailVideoInfo.getType()) {
			case Constants.CARTOON_MANY:
			case Constants.CARTOON_SINGLE:
			case Constants.SHOW_MANY:
			case Constants.SHOW_SINLE:
				return true;
			default:
				break;
			}
		}
		return false;
	}

	private static boolean isVideoInfoDataValid(PluginOverlay mPluginOverlay) {
		return mPluginOverlay != null && mPluginOverlay.mMediaPlayerDelegate != null && mPluginOverlay.mMediaPlayerDelegate.videoInfo != null;
	}

	*//**
	 * 是否是专辑
	 * 
	 * @return
	 *//*
	public static boolean isAlbum(PluginOverlay mPluginOverlay) {
		if (isVideoInfoDataValid(mPluginOverlay)) {
			return !TextUtils.isEmpty(mPluginOverlay.mMediaPlayerDelegate.videoInfo.playlistId);
		}
		return false;
	}

	*//**
	 * 是否是Show
	 * 
	 * @return
	 *//*
	public static boolean isShow(PluginOverlay mPluginOverlay) {
		if (isVideoInfoDataValid(mPluginOverlay)) {
			return !TextUtils.isEmpty(mPluginOverlay.mMediaPlayerDelegate.videoInfo.getShowId());
		}
		return false;
	}

	*//**
	 * 是否是UGC
	 * 
	 * @return
	 *//*
	public static boolean isUGC(PluginOverlay mPluginOverlay) {
		return !isAlbum(mPluginOverlay) && !isShow(mPluginOverlay) && !isMusic();
	}

	*//**
	 * 是否播放的是本地视频
	 * 
	 * @return
	 *//*
	public static boolean isPlayLocalType(PluginOverlay mPluginOverlay) {
		return mPluginOverlay != null && mPluginOverlay.mMediaPlayerDelegate != null && mPluginOverlay.mMediaPlayerDelegate.isPlayLocalType();
	}

	*//**
	 * 是否播放的是本机视频
	 * 
	 * @return
	 *//*
	public static boolean isPlayExternalVideo(PluginOverlay mPluginOverlay) {
		return mPluginOverlay != null && mPluginOverlay.mMediaPlayerDelegate != null && mPluginOverlay.mMediaPlayerDelegate.videoInfo != null && mPluginOverlay.mMediaPlayerDelegate.videoInfo.isExternalVideo;
	}

	*//**
	 * 是否播放的是直播
	 * 
	 * @return
	 *//*
	public static boolean isPlayLive(PluginOverlay mPluginOverlay) {
		return mPluginOverlay != null && mPluginOverlay.mMediaPlayerDelegate != null && mPluginOverlay.mMediaPlayerDelegate.videoInfo != null && mPluginOverlay.mMediaPlayerDelegate.videoInfo.isHLS;
	}

	*//**
	 * 是否是音乐
	 * 
	 * @return
	 *//*
	public static boolean isMusic() {
		if (DetailDataSource.mDetailVideoInfo != null) {
			switch (DetailDataSource.mDetailVideoInfo.getType()) {
			case Constants.MUSIC:
				return true;
			default:
				break;
			}
		}
		return false;
	}

	*//**
	 * 是否是音乐类型且没有歌手
	 * 
	 * @return
	 *//*
	public static boolean isMusicNoSinger() {
		return isMusic() && TextUtils.isEmpty(DetailDataSource.mDetailVideoInfo.getSinger());
	}

	*//**
	 * 是否请求相关片段
	 * 
	 * @return
	 *//*
	public static boolean isRequestRelatedPart(PluginOverlay mPluginOverlay) {
		if (isPlayingRelatedPart(mPluginOverlay) || isDetailDataValid() && !isMusic() && !TextUtils.isEmpty(DetailDataSource.mDetailVideoInfo.getShow_videotype()) && !"正片".equals(DetailDataSource.mDetailVideoInfo.getShow_videotype())) {
			return true;
		}
		return false;
	}

	*//**
	 * 是否剧集是倒序
	 * 
	 * @return
	 *//*
	public static boolean isSeriesReversed() {
		if (DetailDataSource.mSeriesVideoDataInfo != null) {
			return DetailDataSource.mSeriesVideoDataInfo.isReversed();
		}
		return false;
	}

	public static boolean isDetailDataValid() {
		return DetailDataSource.mDetailVideoInfo != null;
	}

	public static String[] getCacheSelectedVids(Set<CacheSelectedInfo> cacheSelectedInfos) {
		String[] vidStrings = new String[cacheSelectedInfos.size()];
		int i = 0;
		for (CacheSelectedInfo cacheSelectedInfo : cacheSelectedInfos) {
			vidStrings[i] = cacheSelectedInfo.getVid();
			i++;
		}
		return vidStrings;
	}

	public static String[] getCacheSelectedTitles(Set<CacheSelectedInfo> cacheSelectedInfos) {
		String[] titleStrings = new String[cacheSelectedInfos.size()];
		int i = 0;
		for (CacheSelectedInfo cacheSelectedInfo : cacheSelectedInfos) {
			titleStrings[i] = cacheSelectedInfo.getTitle();
			i++;
		}
		return titleStrings;
	}

	*//**
	 * 获取缓存默认设置格式
	 * 
	 * @param mContext
	 * @return
	 *//*
	public static String getDownloadFormatString(Context mContext) {
		String downloadFormatString = null;
		switch (DownloadManager.getInstance().getDownloadFormat()) {
		case Youku.FORMAT_3GPHD:
		case Youku.FORMAT_FLV:
			downloadFormatString = mContext.getResources().getString(R.string.standard_definition);
			break;
		case Youku.FORMAT_MP4:
			downloadFormatString = mContext.getResources().getString(R.string.high_definition);
			break;
		case Youku.FORMAT_HD2:
			downloadFormatString = mContext.getResources().getString(R.string.super_definition);
			break;
		default:
			downloadFormatString = mContext.getResources().getString(R.string.standard_definition);
			break;
		}
		return downloadFormatString;
	}

	*//**
	 * 锁屏
	 * 
	 * @param mActivity
	 *//*
	public static void lockScreen(Activity mActivity) {
		switch (mActivity.getResources().getConfiguration().orientation) {
		case Configuration.ORIENTATION_LANDSCAPE:
			if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.FROYO) {
				mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			} else {
				int rotation = mActivity.getWindowManager().getDefaultDisplay().getRotation();
				if (rotation == android.view.Surface.ROTATION_0 || rotation == android.view.Surface.ROTATION_90) {
					mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
				} else {
					mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
				}
			}
			break;
		}
	}

	*//**
	 * 解锁屏
	 * 
	 * @param mActivity
	 *//*
	public static void unlockScreen(Activity mActivity) {
		if (Configuration.ORIENTATION_LANDSCAPE == UIUtils.getDeviceDefaultOrientation(mActivity)) {
			mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
		} else {
			mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
		}
	}

	public static boolean checkNetPlayVideo(String videoid) {

		if (TextUtils.isEmpty(videoid)) {
			return false;
		}

		if (!YoukuUtil.hasInternet()) {// 无网
			if (!DownloadManager.getInstance().isDownloadFinished(videoid)) {// 无缓存
				YoukuUtil.showTips(R.string.tips_no_network);
				return false;
			}
		}
		if (YoukuUtil.hasInternet() && !YoukuUtil.isWifi()) {// 3g
			boolean isAllow3GPlay = PreferenceManager.getDefaultSharedPreferences(Youku.context).getBoolean("allowONline3G", true);
			if (!isAllow3GPlay) {// 禁止3g播放
				if (!DownloadManager.getInstance().isDownloadFinished(videoid)) {// 无缓存
					YoukuUtil.showTips(R.string.detail_3g_tips);
					return false;
				}
			}
		}
		return true;
	}

	public static String isHasNextAlbum(PluginOverlay mPluginOverlay) {
		String nextVideoId = null;
		if (Utils.isAlbum(mPluginOverlay)) {
			String vid = mPluginOverlay.mMediaPlayerDelegate.videoInfo.getVid();
			int length = DetailDataSource.allSeriesVideos.size();
			if (length > 0 && !TextUtils.isEmpty(vid)) {
				int index = -1;
				for (int i = 0; i < length; i++) {
					if (TextUtils.equals(vid, DetailDataSource.allSeriesVideos.get(i).getVideoid())) {
						index = i;
						break;
					}
				}
				if (index >= 0 && index + 1 < length) {
					nextVideoId = DetailDataSource.allSeriesVideos.get(index + 1).getVideoid();
				}
			}
		}
		return nextVideoId;
	}

	public static boolean isInShowSeries(PluginOverlay mPluginOverlay) {
		if (Utils.isShow(mPluginOverlay)) {
			String vid = mPluginOverlay.mMediaPlayerDelegate.videoInfo.getVid();
			int length = DetailDataSource.allSeriesVideos.size();
			if (length > 0 && !TextUtils.isEmpty(vid)) {
				for (int i = 0; i < length; i++) {
					if (TextUtils.equals(vid, DetailDataSource.allSeriesVideos.get(i).getVideoid())) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static boolean isInShowPart(PluginOverlay mPluginOverlay) {
		if (Utils.isShow(mPluginOverlay)) {
			String vid = mPluginOverlay.mMediaPlayerDelegate.videoInfo.getVid();
			int length = DetailDataSource.playRelatedParts.size();
			if (length > 0 && !TextUtils.isEmpty(vid)) {
				for (int i = 0; i < length; i++) {
					if (TextUtils.equals(vid, DetailDataSource.playRelatedParts.get(i).getVideoid())) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static String isHasNextShowSeries(PluginOverlay mPluginOverlay) {
		String nextVideoId = null;
		if (Utils.isShow(mPluginOverlay)) {
			String vid = mPluginOverlay.mMediaPlayerDelegate.videoInfo.getVid();
			int length = DetailDataSource.allSeriesVideos.size();
			if (length > 0 && !TextUtils.isEmpty(vid)) {
				int index = -1;
				if (Utils.isSeriesReversed()) {
					for (int i = length - 1; i >= 0; i--) {
						if (TextUtils.equals(vid, DetailDataSource.allSeriesVideos.get(i).getVideoid())) {
							index = i;
							break;
						}
					}
					if (index >= 0 && index - 1 >= 0) {
						nextVideoId = DetailDataSource.allSeriesVideos.get(index - 1).getVideoid();
					}
				} else {
					for (int i = 0; i < length; i++) {
						if (TextUtils.equals(vid, DetailDataSource.allSeriesVideos.get(i).getVideoid())) {
							index = i;
							break;
						}
					}
					if (index >= 0 && index + 1 < length) {
						nextVideoId = DetailDataSource.allSeriesVideos.get(index + 1).getVideoid();
					}
				}
			}
		}
		return nextVideoId;
	}

	public static String isHasNextUGC(PluginOverlay mPluginOverlay) {
		String nextVideoId = null;
		if (Utils.isUGC(mPluginOverlay)) {
			if (DetailDataSource.mPlayRelatedVideoDataInfo != null) {
				int length = DetailDataSource.mPlayRelatedVideoDataInfo.getPlayRelatedVideos().size();
				for (int i = 0; i < length; i++) {
					String videoid = DetailDataSource.mPlayRelatedVideoDataInfo.getPlayRelatedVideos().get(i).getVideoid();
					String showid = DetailDataSource.mPlayRelatedVideoDataInfo.getPlayRelatedVideos().get(i).getShowid();
					if (!TextUtils.isEmpty(videoid) || !TextUtils.isEmpty(showid)) {
						nextVideoId = TextUtils.isEmpty(videoid) ? showid : videoid;
						break;
					}
				}
			}
		}
		return nextVideoId;
	}

	public static String isHasFirstShowPart(PluginOverlay mPluginOverlay) {
		String nextVideoId = null;
		if (Utils.isShow(mPluginOverlay)) {
			int length = DetailDataSource.playRelatedParts.size();
			if (length > 0) {
				nextVideoId = DetailDataSource.playRelatedParts.get(0).getVideoid();
			}
		}
		return nextVideoId;
	}

	public static String isHasNextShowPart(PluginOverlay mPluginOverlay) {
		String nextVideoId = null;
		if (Utils.isShow(mPluginOverlay)) {
			String vid = mPluginOverlay.mMediaPlayerDelegate.videoInfo.getVid();
			int length = DetailDataSource.playRelatedParts.size();
			if (length > 0 && !TextUtils.isEmpty(vid)) {
				int index = -1;
				for (int i = 0; i < length; i++) {
					if (TextUtils.equals(vid, DetailDataSource.playRelatedParts.get(i).getVideoid())) {
						index = i;
						break;
					}
				}
				if (index >= 0 && index + 1 < length) {
					nextVideoId = DetailDataSource.playRelatedParts.get(index + 1).getVideoid();
				}
			}
		}
		return nextVideoId;
	}

	public static boolean isPlayingRelatedPart(PluginOverlay mPluginOverlay) {
		if (Utils.isShow(mPluginOverlay)) {
			String vid = mPluginOverlay.mMediaPlayerDelegate.videoInfo.getVid();
			int length = DetailDataSource.playRelatedParts.size();
			if (length > 0 && !TextUtils.isEmpty(vid)) {
				for (int i = 0; i < length; i++) {
					if (TextUtils.equals(vid, DetailDataSource.playRelatedParts.get(i).getVideoid())) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static String isHasNextMusic(PluginOverlay mPluginOverlay) {
		String nextVideoId = null;
		if (Utils.isMusic() && !TextUtils.isEmpty(DetailDataSource.mDetailVideoInfo.getSinger())) {
			String vid = mPluginOverlay.mMediaPlayerDelegate.videoInfo.getVid();
			int length = DetailDataSource.allSeriesVideos.size();
			if (length > 0 && !TextUtils.isEmpty(vid)) {
				int index = -1;
				for (int i = 0; i < length; i++) {
					if (TextUtils.equals(vid, DetailDataSource.allSeriesVideos.get(i).getVideoid())) {
						index = i;
						break;
					}
				}
				if (index >= 0 && index + 1 <= length) {
					if (index + 1 == length) {
						if (Profile.ALWAYSHOOKUP == Youku.getPreferenceInt("music_play_mode", Profile.ALWAYSHOOKUP)) {
							nextVideoId = DetailDataSource.allSeriesVideos.get(0).getVideoid();
						}
					} else {
						nextVideoId = DetailDataSource.allSeriesVideos.get(index + 1).getVideoid();
					}
				}
			}
		}
		return nextVideoId;
	}

	public static String isHasNextShow(PluginOverlay mPluginOverlay) {
		String nextVideoId = null;
		if (Utils.isShow(mPluginOverlay)) {
			if (isInShowSeries(mPluginOverlay)) {
				nextVideoId = isHasNextShowSeries(mPluginOverlay);
				if (TextUtils.isEmpty(nextVideoId)) {
					nextVideoId = isHasFirstShowPart(mPluginOverlay);
				}
			} else if (isInShowPart(mPluginOverlay)) {
				nextVideoId = isHasNextShowPart(mPluginOverlay);
			}
		}
		return nextVideoId;
	}

	public static String isHasNextVideo(PluginOverlay mPluginOverlay) {
		String nextVideoId = null;
		if (Utils.isPlayExternalVideo(mPluginOverlay)) {
			nextVideoId = isHasNextExternalVideo(mPluginOverlay);
		} else if (Utils.isPlayLocalType(mPluginOverlay)) {
			nextVideoId = isHasNextLocalVideo(mPluginOverlay);
		} else {
			nextVideoId = isHasNextNetVideo(mPluginOverlay);
		}
		return nextVideoId;
	}

	public static String isHasNextNetVideo(PluginOverlay mPluginOverlay) {
		String nextVideoId = null;
		if (isVideoInfoDataValid(mPluginOverlay)) {
			if (Utils.isAlbum(mPluginOverlay)) {
				nextVideoId = isHasNextAlbum(mPluginOverlay);
			} else if (Utils.isShow(mPluginOverlay)) {
				nextVideoId = isHasNextShow(mPluginOverlay);
			} else if (Utils.isMusic() && !TextUtils.isEmpty(DetailDataSource.mDetailVideoInfo.getSinger())) {
				nextVideoId = isHasNextMusic(mPluginOverlay);
			} else {
				nextVideoId = isHasNextUGC(mPluginOverlay);
			}

			if (TextUtils.isEmpty(nextVideoId)) {
				nextVideoId = mPluginOverlay.mMediaPlayerDelegate.videoInfo.nextVideoId;
			}
		}
		return nextVideoId;
	}

	public static String isHasNextLocalVideo(PluginOverlay mPluginOverlay) {
		String nextVideoId = null;
		if (isVideoInfoDataValid(mPluginOverlay)) {
			if (YoukuUtil.hasInternet()) {
				DownloadInfo nextInfo = DownloadManager.getInstance().getDownloadInfo(mPluginOverlay.mMediaPlayerDelegate.videoInfo.getShowId(), mPluginOverlay.mMediaPlayerDelegate.videoInfo.getShow_videoseq() + 1);
				if (nextInfo != null) {
					nextVideoId = nextInfo.videoid;
				} else {
					DownloadInfo currentInfo = DownloadManager.getInstance().getDownloadInfo(mPluginOverlay.mMediaPlayerDelegate.videoInfo.getVid());
					if (currentInfo != null && currentInfo.show_videoseq < currentInfo.showepisode_total) {
						nextVideoId = currentInfo.videoid;
					} else {
						DownloadInfo info = DownloadManager.getInstance().getNextDownloadInfo(mPluginOverlay.mMediaPlayerDelegate.videoInfo.getVid());
						if (info != null) {
							nextVideoId = info.videoid;
						}
					}
				}
			} else {
				DownloadInfo info = DownloadManager.getInstance().getNextDownloadInfo(mPluginOverlay.mMediaPlayerDelegate.videoInfo.getVid());
				if (info != null) {
					nextVideoId = info.videoid;
				}
			}
		}
		return nextVideoId;
	}

	public static String isHasNextExternalVideo(PluginOverlay mPluginOverlay) {
		String nextVideoId = null;
		if (isVideoInfoDataValid(mPluginOverlay)) {
			Media nextMedia = FragmentLocalVideoList.getNextVideo(mPluginOverlay.mMediaPlayerDelegate.videoInfo.getVid());
			if (nextMedia != null) {
				nextVideoId = nextMedia.getLocation();
			}
		}
		return nextVideoId;
	}

	public static PlayRelatedVideo getPlayRelatedVideo(String id) {
		PlayRelatedVideo mPlayRelatedVideo = null;
		if (DetailDataSource.mPlayRelatedVideoDataInfo != null && !TextUtils.isEmpty(id)) {
			int length = DetailDataSource.mPlayRelatedVideoDataInfo.getPlayRelatedVideos().size();
			for (int i = 0; i < length; i++) {
				String videoid = DetailDataSource.mPlayRelatedVideoDataInfo.getPlayRelatedVideos().get(i).getVideoid();
				String showid = DetailDataSource.mPlayRelatedVideoDataInfo.getPlayRelatedVideos().get(i).getShowid();
				if (TextUtils.equals(id, videoid) || TextUtils.equals(id, showid)) {
					mPlayRelatedVideo = DetailDataSource.mPlayRelatedVideoDataInfo.getPlayRelatedVideos().get(i);
					break;
				}
			}
		}
		return mPlayRelatedVideo;
	}

	public static long lastClickTime = 0;

	public static long currentClickTime = 0;

	public static boolean checkClickEvent() {
		return checkClickEvent(1000);
	}

	public static boolean checkClickEvent(long interval) {
		currentClickTime = System.currentTimeMillis();
		if (currentClickTime - lastClickTime > interval) {
			lastClickTime = currentClickTime;
			return true;
		} else {
			lastClickTime = currentClickTime;
			return false;
		}
	}

	public static long lastPlayClickTime = 0;

	public static long currentPlayClickTime = 0;

	public static boolean checkPlayClickEvent() {
		return checkClickPlayEvent(1000);
	}

	public static boolean checkClickPlayEvent(long interval) {
		currentPlayClickTime = System.currentTimeMillis();
		if (currentPlayClickTime - lastPlayClickTime > interval) {
			lastPlayClickTime = currentPlayClickTime;
			return true;
		} else {
			lastPlayClickTime = currentPlayClickTime;
			return false;
		}
	}

	public static String getStringTwo(String strIn) {
		String strResult;
		if (strIn.length() >= 2) {
			strResult = strIn;
		} else {
			strResult = "0".concat(String.valueOf(String.valueOf(strIn)));
		}
		return strResult;
	}

	public static void changeBatteryState(int status, int value, ImageView mImageView) {
		if (mImageView != null) {
			boolean isCharging = false;

			switch (status) {
			case BatteryManager.BATTERY_STATUS_CHARGING:// 充电状态
				isCharging = true;
				break;
			case BatteryManager.BATTERY_STATUS_DISCHARGING:// 放电状态
				break;
			case BatteryManager.BATTERY_STATUS_NOT_CHARGING:// 未充电
				break;
			case BatteryManager.BATTERY_STATUS_FULL:// 充满电
				break;
			case BatteryManager.BATTERY_STATUS_UNKNOWN:// 未知状态
				break;
			default:
				break;
			}

			if (isCharging) {
				mImageView.setImageResource(R.drawable.battery_charge);
			} else {
				if (value >= 90) {
					mImageView.setImageResource(R.drawable.battery3);
				} else if (value >= 60) {
					mImageView.setImageResource(R.drawable.battery2);
				} else {
					mImageView.setImageResource(R.drawable.battery1);
				}
			}
		}
	}

	public static void changeTimeState(TextView mTextView) {
		if (mTextView != null) {
			Calendar mCalendar = Calendar.getInstance();
			mCalendar.setTimeInMillis(System.currentTimeMillis());
			int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
			int minite = mCalendar.get(Calendar.MINUTE);
			mTextView.setText(getStringTwo(String.valueOf(hour)) + ":" + getStringTwo(String.valueOf(minite)));
		}
	}

	public static void showPlayNextDialog(final DetailActivity context, final DownloadInfo currentInfo, final MediaPlayerDelegate mMediaPlayerDelegate) {
		final YoukuDialog dialog = new YoukuDialog(context, TYPE.normal);
		context.tempYoukuDialog = dialog;
		dialog.setNormalPositiveBtn("在线续播", new OnClickListener() {

			@Override
			public void onClick(View v) {
				boolean isAllow3GPlay = PreferenceManager.getDefaultSharedPreferences(Youku.context).getBoolean("allowONline3G", true);
				dialog.dismiss();
				if (!isAllow3GPlay && YoukuUtil.hasInternet() && !YoukuUtil.isWifi()) {// 有网
					YoukuUtil.showTips(R.string.detail_3g_play_toast);
					context.finish();
				}
				YoukuService.getService(IHttpRequest.class, true).request(new HttpIntent(URLContainer.getNextSeries(currentInfo.showid, currentInfo.videoid)), new IHttpRequestCallBack() {

					@Override
					public void onSuccess(HttpRequestManager httpRequestManager) {
						String str = httpRequestManager.getDataString();
						try {
							final JSONObject jsonObject = JSONObject.parseObject(str);
							JSONObject result = jsonObject.getJSONObject("result");
							String nextVid = result.getString("next_videoid");
							if (TextUtils.isEmpty(nextVid)) {
								YoukuUtil.showTips("当前已是最新一集");
								mMediaPlayerDelegate.finishActivity();
								return;
							}
							mMediaPlayerDelegate.videoInfo.playType = StaticsUtil.PLAY_TYPE_NET;
							context.on3gStartPlay(nextVid);
							context.getDetailLayoutData();
						} catch (Exception e) {
							mMediaPlayerDelegate.finishActivity();
							Logger.e(Youku.TAG_GLOBAL, "FullScreenUtils#showPlayNextDialog()", e);
						}
					}

					@Override
					public void onFailed(String failReason) {
						context.finish();
						YoukuUtil.showTips(failReason);
					}

				});
			}
		});
		dialog.setNormalNegtiveTextColor(context.getResources().getColor(R.color.cancel_text_color));
		dialog.setNormalNegtiveBackGround(R.drawable.btn_vip_dialog_cancel);
		dialog.setNormalNegtiveBtn("立即下载", new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				YoukuService.getService(IHttpRequest.class, true).request(new HttpIntent(URLContainer.getNextSeries(currentInfo.showid, currentInfo.videoid)), new IHttpRequestCallBack() {

					@Override
					public void onSuccess(HttpRequestManager httpRequestManager) {
						String str = httpRequestManager.getDataString();
						try {
							final JSONObject jsonObject = JSONObject.parseObject(str);
							JSONObject result = jsonObject.getJSONObject("result");
							String nextVid = result.getString("next_videoid");
							String title = result.getString("next_title");
							if (TextUtils.isEmpty(nextVid)) {
								YoukuUtil.showTips("当前已是最新一集");
								mMediaPlayerDelegate.finishActivity();
								return;
							}

							DownloadManager download = DownloadManager.getInstance();
							download.createDownload(nextVid, title, new OnCreateDownloadListener() {
								@Override
								public void onfinish(boolean isNeedRefresh) {
									Intent godown = new Intent(context, CachePageActivity.class);
									context.startActivity(godown);
									context.finish();
								}

								@Override
								public void onOneFailed() {
									Intent godown = new Intent(context, CachePageActivity.class);
									context.startActivity(godown);
									context.finish();
								}
							});
						} catch (Exception e) {
							Logger.e(Youku.TAG_GLOBAL, "FullScreenUtils.showPlayNextDialog(...).new OnClickListener() {...}.onClick(...).new IHttpRequestCallBack() {...}#onSuccess()", e);
						}
					}

					@Override
					public void onFailed(String failReason) {
						context.finish();
						YoukuUtil.showTips(failReason);
					}

				});
			}
		});
		dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				context.tempYoukuDialog = null;
			}
		});
		dialog.setMessage("是否续播下一集");
		dialog.setCanceledOnTouchOutside(true);
		dialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				mMediaPlayerDelegate.finishActivity();
			}
		});
		dialog.show();
	}

	public static boolean isVipUser() {
		boolean isVipUser = false;
		if (!TextUtils.isEmpty(Youku.COOKIE)) {
			try {
				// cookie里边的yktk 键的内容是由| 分隔的。用base64
				// 把第4个内容解析出来。解析出来后的内容格式是id:XXX,nn:XXX,vip:true,ytid:XXX,tid:XXX。vip
				// 字段内容true 代表vip .false代表不是。
				org.json.JSONObject objContent = BaseHelper.string2JSON(Youku.COOKIE, ";");
				String yktk = objContent.getString("yktk");
				String decode_yktk = URLDecoder(yktk);
				String[] decode_yktk_array = decode_yktk.split("\\|");
				String base64_temp = decode_yktk_array[3];
				String temp = new String(Base64.decode(base64_temp, Base64.DEFAULT), "utf-8");
				isVipUser = temp.contains("vip:true");
			} catch (Exception e) {
				e.printStackTrace();
				isVipUser = false;
			}
		}
		return isVipUser;
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

	@SuppressLint("NewApi")
	public static int getRealScreenHeight(Activity activity) {
		int realScreenHeight = 0;

		int heightPixels = 0;
		final Point size = new Point();
		try {
			activity.getWindowManager().getDefaultDisplay().getRealSize(size);
			heightPixels = size.y;
		} catch (NoSuchMethodError e) {
			heightPixels = activity.getResources().getDisplayMetrics().heightPixels;
		}

		if (Youku.isTablet) {
			boolean hasVirtualButtonBar = false;
			if (android.os.Build.VERSION.SDK_INT >= 14) {
				hasVirtualButtonBar = !ViewConfiguration.get(activity).hasPermanentMenuKey();
			} else if (android.os.Build.VERSION.SDK_INT >= 11 && android.os.Build.VERSION.SDK_INT <= 13) {
				hasVirtualButtonBar = true;
			} else {
				hasVirtualButtonBar = false;
			}
			if (hasVirtualButtonBar) {
				realScreenHeight = heightPixels - getNavigationBarHeight(activity);
			} else {
				realScreenHeight = heightPixels;
			}
		} else {
			realScreenHeight = heightPixels;
		}
		return realScreenHeight;
	}

	public static int getNavigationBarHeight(Context mContext) {
		int navigationBarHeight = 0;
		if (mContext != null) {
			if (android.os.Build.VERSION.SDK_INT >= 11 && android.os.Build.VERSION.SDK_INT <= 13) {
				Resources resources = mContext.getResources();
				navigationBarHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48, resources.getDisplayMetrics());
			} else {
				if ("MI PAD".equalsIgnoreCase(android.os.Build.MODEL)) {
					navigationBarHeight = 0;
				} else {
					Resources resources = mContext.getResources();
					int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
					if (resourceId > 0) {
						navigationBarHeight = resources.getDimensionPixelSize(resourceId);
					}
				}
			}
		}
		return navigationBarHeight;
	}*/
}
