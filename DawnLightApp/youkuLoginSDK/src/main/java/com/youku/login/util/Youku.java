package com.youku.login.util;


import com.baseproject.utils.UIUtils;
import com.youku.analytics.utils.Tools;
import com.youku.login.network.URLContainer;
import com.youku.login.statics.IStaticsManager;
import com.youku.login.vo.UserProfile;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;

public class Youku{
	
	public static Context context;
	public static String User_Agent;
	public static String versionName;
	public static String GUID = "";
	/** 是否是平板电脑 */
	public static boolean isTablet;
	public static Context mContext;
	public static String COOKIE = null;// 服务器返回的 Cookie 串
	private static SharedPreferences s;
	private static Editor e;
	public static String userName = "";// 用户昵称
	public static String loginAccount = null;// 登录账号
	public static boolean isLogined = false;// 是否登录
	public static boolean isShowLog = true;
	/** 是否是高端机型 */
	public static boolean isHighEnd;
	// Flags associated with the application.
	public static int flags = 7;
	public static IStaticsManager iStaticsManager = null;  
	
	public static final String TAG_GLOBAL = "Youku";
	public static final int TIMEOUT = 30000;
	 
	/** 用户个人中心数据 */ 
	public static UserProfile userprofile;
	
	
	public static void init(Context context){
		mContext = context;
		s = context.getSharedPreferences(context.getPackageName()
				+ "_preferences", UIUtils.hasGingerbread() ? Context.MODE_MULTI_PROCESS
				: Context.MODE_PRIVATE);
		
		e = s.edit();
		
//		try {
//			Youku.versionName = context.getPackageManager().getPackageInfo(
//					context.getPackageName(), PackageManager.GET_META_DATA).versionName;
//		} catch (NameNotFoundException e) {
			Youku.versionName = "4.5";
			//Logger.e(TAG_GLOBAL, e);
//		}
		isTablet = (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
		
		iStaticsManager = IStaticsManager.getInstance(Youku.mContext);
		
		Youku.GUID = Tools.getGUID(context);
		URLContainer.init();
		
		User_Agent = (isTablet ? "Youku HD;" : "Youku;") + versionName
				+ ";Android;" + android.os.Build.VERSION.RELEASE + ";"
				+ android.os.Build.MODEL;
		
		
		if (false) {
			isShowLog = true;
			setTest1Api();
		} else {
			isShowLog = true;
			setOfficialApi();
		}
	}
	
	
	@SuppressLint("NewApi")
	public static void savePreference(String key, String value) {
		if (UIUtils.hasGingerbread()) {
			e.putString(key, value).apply();
		} else {
			e.putString(key, value).commit();
		}
	}
	
	@SuppressLint("NewApi")
	public static void savePreference(String key, Boolean value) {
		if (UIUtils.hasGingerbread()) {
			e.putBoolean(key, value).apply();
		} else {
			e.putBoolean(key, value).commit();
		}
	}
	
	public static String getPreference(String key) {
		if(s == null){
			return "";
		}
		return s.getString(key, "");
	}
	
	private static void setTest1Api() {
		URLContainer.YOUKU_DOMAIN = URLContainer.TEST1_YOUKU_DOMAIN;
		URLContainer.YOUKU_USER_DOMAIN = URLContainer.TEST1_YOUKU_DOMAIN;
	}
	
	private static void setOfficialApi() {
		URLContainer.YOUKU_DOMAIN = URLContainer.OFFICIAL_YOUKU_DOMAIN;
		URLContainer.YOUKU_USER_DOMAIN = URLContainer.OFFICIAL_YOUKU_USER_DOMAIN;
	} 
	
	public static int getPreferenceInt(String key) {
	    return s.getInt(key, 0);
	  }
	
	public static boolean getPreferenceBoolean(String key) {
	    return s.getBoolean(key, false);
	}
	
	 public static String getPreference(String key, String def) {
		    return s.getString(key, def);
	 }
	
}
