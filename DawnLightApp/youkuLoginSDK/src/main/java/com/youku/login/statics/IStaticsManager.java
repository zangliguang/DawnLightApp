package com.youku.login.statics;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import cn.com.iresearch.mapptracker.IRMonitor;

import com.comscore.analytics.comScore;
import com.youku.analytics.data.Device;
import com.youku.login.config.Profile;
import com.youku.login.util.Logger;
import com.youku.login.util.Youku;
/**
 * 埋点统计管理类
 * @author malijie
 *
 */
public class IStaticsManager extends IStaticsImpl {
	public static IStaticsManager iStaticsManager = null;
	private static Context context;
	private static final String REFRENCE_CODE = "refercode";
	
	private IStaticsManager(Context context) {
		this.context = context;
		initTrack();
	};
	
	public synchronized static IStaticsManager getInstance(Context context) {
		if (iStaticsManager == null) {
			iStaticsManager = new IStaticsManager(context);
		}
		return iStaticsManager;
	}
	
	/**
	 * 初始化Android SDK统计埋点
	 */
	public void initTrack() {
		init(context, Youku.User_Agent, Profile.Wireless_pid);
		setDebug(IStaticUtil.isDebugOpen);
		setTestHost(IStaticUtil.isTestHostOpen);
		setTest(IStaticUtil.isTestOpen);// 置为true不进行加密并将日志写到本地,测试完成后置为false
		setContinueSessionMillis(10*60*1000);
		Logger.lxf("===initTrack()========初始化统计埋点=====");
	}
	
	//统计登录页面中登录埋点
	public void trackLoginPageLoginClick(){
		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("eventType", "interact");
		hashMap.put("actionType", "loginSuccess");
		hashMap.put("loginType", StaticsConfigFile.loginType);//登录类型
		hashMap.put("loginPath", StaticsConfigFile.loginPath);//登录方式
		hashMap.put("loginSource", StaticsConfigFile.loginSource);//登录来源
//		hashMap.put("isMember", StaticsConfigFile.isMember);//是否会员
		String encodeStr = StaticsConfigFile.LOGIN_PAGE_LOGIN_BUTTON_ENCODE_VALUE;
		if(StaticsConfigFile.loginType!=StaticsConfigFile.LOGIN_TYPE_AUTO_LOGIN){//自动登录不统计
			TrackCommonClickEventWithUid(StaticsConfigFile.LOGIN_PAGE_LOGIN_BUTTON_CLICK, StaticsConfigFile.LOGIN_PAGE, hashMap, encodeStr);
		}
	}
	
	/**
	 * 统计SDK
	 * 为登录事件添加的带UID
	 * 
	 * @param baseStaticsEvent
	 */
	public void TrackCommonClickEventWithUid(String name, String page, HashMap<String, String> extendMap, String encodeStr) {
		StringBuilder sb = new StringBuilder();
		LinkedHashMap<String, String> extendParameter = new LinkedHashMap<String, String>();
		try {
			if(!TextUtils.isEmpty(encodeStr)){
				extendParameter.put(REFRENCE_CODE, encodeStr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(null!=extendMap){
			Iterator it = extendMap.entrySet().iterator();
			while (it.hasNext()) {
				Entry obj = (Entry) it.next();
				extendParameter.put(obj.getKey() + "", obj.getValue() + "");
				sb.append(obj.getKey() + " = "+ obj.getValue() + "  ");
			}
		}
		
		Logger.lxf("========统计信息==name== "+name+" ==page== "+page+" ==扩展参数== "+sb.toString()+" ==加码示例== "+encodeStr);
		Logger.lxf("=统计登录事件中的======uid====="+Youku.getPreference("uid"));
		trackExtendCustomEventWithUidImpl(context, name, page, "", extendParameter,Youku.getPreference("uid"));
	}
	
	public HashMap<String, String> getHashMapStyleValue(String key, String value) {
		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put(key, value);
		return hashMap;
	}
	
	/**
	 * 统计SDK 普通点击事件
	 * 
	 * @param baseStaticsEvent
	 */
	public void TrackCommonClickEvent(String name, String page,
			HashMap<String, String> extendMap, String encodeStr) {
		StringBuilder sb = new StringBuilder();
		LinkedHashMap<String, String> extendParameter = new LinkedHashMap<String, String>();
		try {
			if (!TextUtils.isEmpty(encodeStr)) {
				extendParameter.put(REFRENCE_CODE, encodeStr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (null != extendMap) {
			Iterator it = extendMap.entrySet().iterator();
			while (it.hasNext()) {
				Entry obj = (Entry) it.next();
				extendParameter.put(obj.getKey() + "", obj.getValue() + "");
				sb.append(obj.getKey() + " = " + obj.getValue() + "  ");
			}
		}
		
//		Logger.lxf("========统计信息==name== " + name + " ==page== " + page
//				+ " ==扩展参数== " + sb.toString() + " ==加码示例== " + encodeStr);
		trackExtendCustomEvent(context, name, page, "", extendParameter);
	}

}
