package com.youku.login.util;

import com.youku.login.activity.TestActivity;

import android.content.Context;
import android.content.Intent;


public class DetailUtil {
	
	/**
	 * 如果不需要point，请调用{@link #goPlayer(Context, String, String)}
	 * 带point信息将忽略其他播放记录
	 * 
	 * @param context
	 * @param id
	 * @param title
	 * @param point
	 *            重播时为 -1 单位为毫秒
	 */
	public static void goPlayerWithpoint(Context context, String id, String title, int point, boolean isPay) {
		//Intent intent = new Intent(context, DetailActivity.class);
		Intent intent = new Intent(context, TestActivity.class);
		intent.putExtra("video_id", id);
		intent.putExtra("title", title);
		intent.putExtra("point", point);
		intent.putExtra("isPay", isPay);
		YoukuUtil.startActivity(context, intent);
	}
	
	
	
}
