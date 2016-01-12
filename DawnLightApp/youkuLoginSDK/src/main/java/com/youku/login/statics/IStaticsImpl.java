package com.youku.login.statics;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;

import com.youku.analytics.AnalyticsAgent;
import com.youku.login.util.Youku;

public abstract class IStaticsImpl implements IStatistics {

	private static String userID = "";
	private static String appName = "";
	
	static{
		userID = Youku.getPreference("uid");
		appName = (Youku.isTablet ? "Youku HD" : "Youku");
		
	}
	
	@Override
	public void init(Context context, String userAgent, String pid) {
		AnalyticsAgent.init(context, userAgent, pid, appName);
	}
	
	@Override
	public void startSession(Activity context, String pageName) {
		AnalyticsAgent.startSession(context, pageName, userID);
	}

	@Override
	public void endSession(Activity context) {
		AnalyticsAgent.endSession(context, userID);
	}

	@Override
	public void trackCustomEvent(Context context, String name, String page, String target) {
		// TODO Auto-generated method stub
		AnalyticsAgent.trackCustomEvent(context, name, page, target, userID);
	}

	@Override
	public void trackExtendCustomEvent(Context context, String name, String page, String target, HashMap<String, String> extend) {
		// TODO Auto-generated method stub
		AnalyticsAgent.trackExtendCustomEvent(context, name, page, target, userID, extend);
	}
	
	public void trackExtendCustomEventWithUidImpl(Context context, String name, String page, String target, HashMap<String, String> extend,String uid) {
		// TODO Auto-generated method stub
		AnalyticsAgent.trackExtendCustomEvent(context, name, page, target, uid, extend);
	}
	
	@Override
	public void trackExtendCustomEventWithSession(Context context, String name, String page, String target, HashMap<String, String> extend, String session) {
		AnalyticsAgent.trackExtendCustomEventWithSession(context, name, page, target, page, session, extend);
	}

	@Override
	public void playRequest(Context context, String vid, String playType) {
		// TODO Auto-generated method stub
		AnalyticsAgent.playRequest(context, vid, playType, userID);
	}

	@Override
	public void playStart(Context context, String vid, String playcode) {
		// TODO Auto-generated method stub
		AnalyticsAgent.playStart(context, vid, playcode, userID);
	}

	@Override
	public void playPause(Context context, String vid) {
		// TODO Auto-generated method stub
		AnalyticsAgent.playPause(context, vid, userID);
	}

	@Override
	public void playContinue(Context context, String vid, String playcode) {
		// TODO Auto-generated method stub
		AnalyticsAgent.playContinue(context, vid, playcode, userID);
	}

	@Override
	public void playEnd(Context context, String vid, boolean complete) {
		// TODO Auto-generated method stub
//		AnalyticsAgent.playEnd(context, vid, complete, vid, advBeforeDuration, beforeDuration, videoTime, duration, playLoadEvents, playRates, playSDTimes, playSDDuration, playHDTimes, playHDDuration, playHD2Times, playHD2Duration);
	}

	@Override
	public void adPlayStart(Context context, String vid, boolean complete, ArrayList<String> adUrls) {
		// TODO Auto-generated method stub
		AnalyticsAgent.adPlayStart(context, vid, complete, adUrls, userID);
	}

	@Override
	public void adPlayEnd(Context context, String vid, boolean complete, ArrayList<String> adUrls) {
		// TODO Auto-generated method stub
		AnalyticsAgent.adPlayEnd(context, vid, complete, adUrls, userID);
	}

	@Override
	public void playHeart(Context context, String vid) {
		// TODO Auto-generated method stub
		AnalyticsAgent.playHeart(context, vid, userID);
	}

	@Override
	public void setDebug(boolean debug) {
		// TODO Auto-generated method stub
		AnalyticsAgent.setDebugMode(debug);
	}

	@Override
	public void setContinueSessionMillis(long time) {
		// TODO Auto-generated method stub
		AnalyticsAgent.setContinueSessionMillis(time);
	}

	@Override
	public void setTrackLocation(boolean isTrack) {
		// TODO Auto-generated method stub
		AnalyticsAgent.setTrackLocation(isTrack);
	}

	@Override
	public void setCacheSize(int size) {
		// TODO Auto-generated method stub
		AnalyticsAgent.setCaCheSize(size);
	}

	@Override
	public void setEventSize(int size) {
		// TODO Auto-generated method stub
		AnalyticsAgent.setEventSize(size);
	}

	@Override
	public void setCachePersentage(float persentage) {
		// TODO Auto-generated method stub
		AnalyticsAgent.setCachePersentage(persentage);
	}

	@Override
	public void setUserAgent(Context context, String userAgent) {
		// TODO Auto-generated method stub
		AnalyticsAgent.setUserAgent(context, userAgent);
	}

	@Override
	public void onKillProcess(Context context) {
		// TODO Auto-generated method stub
		AnalyticsAgent.onKillProcess(context, userID);
	}

	@Override
	public void setTestHost(boolean debug) {
		AnalyticsAgent.setTestHost(debug);
	}

	@Override
	public void setTest(boolean debug) {
		AnalyticsAgent.setTest(debug);
	}

	
	
}
