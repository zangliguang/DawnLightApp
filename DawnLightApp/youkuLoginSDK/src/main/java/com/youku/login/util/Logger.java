package com.youku.login.util;


import android.util.Log;


public class Logger {
 
	public static int flags = Youku.flags;

	public static final String TAG = Youku.TAG_GLOBAL;

	/**
	 * The override method of Logger.
	 * 
	 * The default level of any tag is set to LOGLEVEL 5. This means that any
	 * level log will be logged. if your set the LOGLEVEL to 0 , no log will be
	 * print out.
	 */
	public static int LOGLEVEL = 5;//BuildConfig.DEBUG ? 5 : 0;

	public static boolean VERBOSE = LOGLEVEL > 4;
	public static boolean DEBUG = LOGLEVEL > 3;
	public static boolean INFO = LOGLEVEL > 2;
	public static boolean WARN = LOGLEVEL > 1;
	public static boolean ERROR = LOGLEVEL > 0;

	// private ArrayList<LoggerItem> logs;
	// public static final String LEVEL_D = "d";
	// public static final String LEVEL_E = "e";

	public static void v(String tag, String msg) {
		if (Youku.isShowLog)
			Log.v(tag, msg == null ? "" : msg);
	}

	public static void v(String tag, String msg, Throwable tr) {
		if (Youku.isShowLog)
			Log.v(tag, msg == null ? "" : msg, tr);
	}

	public static void v(String msg) {
		if (Youku.isShowLog)
			Log.v(TAG, msg == null ? "" : msg);
	}

	public static void v(String msg, Throwable tr) {
		if (Youku.isShowLog)
			Log.v(TAG, msg == null ? "" : msg, tr);
	}

	public static void d(String tag, String msg) {
		if (Youku.isShowLog)
			Log.d(tag, msg == null ? "" : msg);
	}
	public static void lxf(String msg) {
		if (Youku.isShowLog)
			Log.i("LXF", msg == null ? "" : msg);
	}

	public static void d(String tag, String msg, Throwable tr) {
		if (Youku.isShowLog)
			Log.d(tag, msg == null ? "" : msg, tr);
	}

	public static void d(String msg) {
		if (Youku.isShowLog)
			Log.d(TAG, msg == null ? "" : msg);
	}

	public static void d(String msg, Throwable tr) {
		if (Youku.isShowLog)
			Log.d(TAG, msg == null ? "" : msg, tr);
	}

	public static void e(String tag, String msg) {
		if (Youku.isShowLog)
			Log.e(tag, msg == null ? "" : msg);
	}

	public static void e(String tag, String msg, Throwable tr) {
		if (Youku.isShowLog)
			Log.e(tag, msg == null ? "" : msg, tr);
	}

	public static void e(String msg) {
		if (Youku.isShowLog)
			Log.e(TAG, msg == null ? "" : msg);
	}

	public static void e(String msg, Throwable tr) {
		if (Youku.isShowLog)
			Log.e(TAG, msg == null ? "" : msg, tr);
	}
	
	public static void banana(String msg) {
		if (Youku.isShowLog)
			Log.d("banana", msg == null ? "" : msg);
	}

}