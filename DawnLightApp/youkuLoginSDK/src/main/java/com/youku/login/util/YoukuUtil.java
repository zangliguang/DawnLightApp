package com.youku.login.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.youku.login.activity.WebViewActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

public class YoukuUtil {
	private final static String LINE_SEPARATOR = System.getProperty("line.separator");
	
	
	public static String md5(final String s) {
		try {
			MessageDigest digest = java.security.MessageDigest
					.getInstance("MD5");
			digest.update(s.getBytes());
			byte messageDigest[] = digest.digest();

			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < messageDigest.length; i++) {
				String h = Integer.toHexString(0xFF & messageDigest[i]);
				while (h.length() < 2)
					h = "0" + h;
				hexString.append(h);
			}
			return hexString.toString();
		} catch (NoSuchAlgorithmException e) {
		}
		return "";
	}
	
	public static String convertStreamToString(InputStream is) {
		/*
		 * To convert the InputStream to String we use the
		 * BufferedReader.readLine() method. We iterate until the BufferedReader
		 * return null which means there's no more data to read. Each line will
		 * appended to a StringBuilder and returned as String.
		 */
		final BufferedReader r = new BufferedReader(new InputStreamReader(is));
		final StringBuilder b = new StringBuilder();
		String line = null;
		try {
			while ((line = r.readLine()) != null) {
				b.append(line);
				b.append(LINE_SEPARATOR);
			}
		} catch (IOException e) {
			return null;
		} finally {
			try {
				is.close();
			} catch (IOException e) {
			}
		}
		return b.toString();
	}
	
	public static void showTips(int stringId) {
		showTips(Youku.mContext.getString(stringId), -1l);
	}

	public static void showTips(String tipsString) {
		showTips(tipsString, -1L);
	}
	
	/** threshold the downloadtask create time */
	public static void showTips(String tipsString, long threshold) {
		Logger.d("Youku.showTips():" + tipsString);
		Message msg = Message.obtain();
		msg.what = 1;
		Bundle bundle = new Bundle();
		bundle.putString("tipsString", tipsString);
		bundle.putLong("threshold", threshold);
		msg.setData(bundle);
		MsgHandler msgHandler = new MsgHandler(Looper.getMainLooper());
		msgHandler.sendMessage(msg);
	}
	
	private static class MsgHandler extends Handler {

		public MsgHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 0:
				Toast.makeText(Youku.mContext,
						msg.getData().getString("ToastMsg"), Toast.LENGTH_LONG)
						.show();
				break;
			case 1:
				handleShowTipsEvents(msg);
				break;
			}
			super.handleMessage(msg);
		}

		private long previousToastShow;
		private String previousToastString = "";

		private void handleShowTipsEvents(Message msg) {
			long thisTime = System.currentTimeMillis();
			String thisTimeMsg = msg.getData().getString("tipsString");
			// long threshold = msg.getData().getLong("threshold", -1L);
			String temp = previousToastString;
			previousToastString = thisTimeMsg;
			long tempTime = previousToastShow;
			previousToastShow = thisTime;
			if (thisTimeMsg == null || thisTime - tempTime <= 3500
					&& thisTimeMsg.equalsIgnoreCase(temp)) {
				previousToastString = temp;
				previousToastShow = tempTime;
				return;
			}
			Toast.makeText(Youku.mContext,
					msg.getData().getString("tipsString"), Toast.LENGTH_SHORT)
					.show();
			previousToastShow = thisTime;
		}
	}
	
	public static boolean deleteFile(final File file) {
		if (file == null)
			return false;
		if (file.exists()) { // 判断文件是否存在
			if (file.isFile()) { // 判断是否是文件
				return file.delete();
			} else if (file.isDirectory()) { // 否则如果它是一个目录
				File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
				if (files != null) {
					for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
						if (deleteFile(files[i]) == false) {// 把每个文件用这个方法进行递归
							return false;
						}
					}
				}
			}
			return file.delete();
		}
		return false;
	}
	
	/**
	 * TODO 判断网络状态是否可用
	 * 
	 * @return true: 网络可用 ; false: 网络不可用
	 */
	public static boolean hasInternet() {
		ConnectivityManager m = (ConnectivityManager) Youku.mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (m == null) {
			Logger.d("NetWorkState", "Unavailabel");
			return false;
		} else {
			NetworkInfo[] info = m.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						Logger.d("NetWorkState", "Availabel");
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * 跳转到自定义的WebView页,不会传递cookie
	 * 
	 * @param context
	 * @param url
	 */
	public static void goWebView(Context context, String url) {
//		try {
//			Intent i = new Intent(context, WebViewActivity.class);
//			i.putExtra("url", url);
//			context.startActivity(i);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}
	
	/**
	 * 跳转到自定义的WebView页,传递title
	 * 
	 * @param context
	 * @param url
	 */
	public static void goWebViewWithParameter(Context context, String url,String title) {
		try {
			Intent i = new Intent(context, WebViewActivity.class);
			i.putExtra("url", url);
			i.putExtra("title", title);
			context.startActivity(i);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static final float scale = Youku.mContext.getResources()
			.getDisplayMetrics().density;
	
	public static int dip2px(float dipValue) {
		return (int) (dipValue * scale + 0.5f);
	}
	
	/**
	 * @return 是否是wifi网络
	 */
	public static boolean isWifi() {
		ConnectivityManager m = (ConnectivityManager) Youku.mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo n = m.getActiveNetworkInfo();
		return (n != null && n.getType() == ConnectivityManager.TYPE_WIFI);
	}

	public static String join(Object... objs) {
		if (objs == null)
			return "";
		StringBuffer s = new StringBuffer();
		for (int i = 0, n = objs.length; i < n; i++) {
			s.append(objs[i]);
			if (i != n - 1)
				s.append(",");
		}
		return s.toString();
	}

	public static String join(int[] objs) {
		if (objs == null)
			return "";
		StringBuffer s = new StringBuffer();
		for (int i = 0, n = objs.length; i < n; i++) {
			s.append(objs[i]);
			if (i != n - 1)
				s.append(",");
		}
		return s.toString();
	}

	public static String join(long[] objs) {
		if (objs == null)
			return "";
		StringBuffer s = new StringBuffer();
		for (int i = 0, n = objs.length; i < n; i++) {
			s.append(objs[i]);
			if (i != n - 1)
				s.append(",");
		}
		return s.toString();
	}
	
	/**
	 * 传入秒, 返回格式化的String
	 **/

	public static String formatTime(long time) {
		String minute = String.valueOf(time / 60);
		String second = String.valueOf(time % 60);
		if (minute.length() == 1)
			minute = "0" + minute;
		if (second.length() == 1)
			second = "0" + second;
		return minute + "分" + second + "秒";
	}

	
	public static void startActivity(Context context, Intent intent) {
		context.startActivity(intent);
	}
	
	public static void gotoWeb(Context context, String url) {
		try {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse(url));
			intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
					| Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_FROM_BACKGROUND);
			// intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
}
