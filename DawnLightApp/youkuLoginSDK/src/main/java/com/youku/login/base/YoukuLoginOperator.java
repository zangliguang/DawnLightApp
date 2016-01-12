package com.youku.login.base;

import android.content.Context;
import android.content.Intent;

import com.youku.login.activity.LoginRegistCardViewDialogActivity;
import com.youku.login.util.Youku;

public class YoukuLoginOperator {
	
	public static void initYoukuLogin(Context context){
		Youku.init(context);
	}
	
	/**
	 * 
	 * @param context    上下文
	 * @param keyFrom    页面key的来源
	 * @param trackFrom  埋点来源
	 */
	
	public static void showLoginView(Context context, String keyFrom, int trackFrom){
		Intent mIntent = new Intent(context, LoginRegistCardViewDialogActivity.class);
		mIntent.putExtra(keyFrom, trackFrom);
		context.startActivity(mIntent);  
	}
	
	public static void showLoginView(Context context){
		Intent mIntent = new Intent(context, LoginRegistCardViewDialogActivity.class);
		context.startActivity(mIntent);  
	}
}
