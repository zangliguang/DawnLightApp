package com.youku.login.statics;

import java.io.UnsupportedEncodingException;
import java.util.Random;

public class IStaticUtil {
	
	public static final boolean isSessionOpen = true; //是否开启session
	public static final boolean isDebugOpen = true; //是否开启Debug
	public static final boolean isTestOpen = true; //是否开启TestOpen
	public static final boolean isTestHostOpen = true; //是否开启TestHostOpen
	
	/****
	 * 获取aaid的算法
	 * App为每个搜索页生成的唯一id，生成规则为13位时间戳+8位数字随机数（0-9之间取值）
	 * @return
	 */
	public static String getAaid() {
		Random rd1 = new Random();
		StringBuilder sb = new StringBuilder();
		long currentTime = System.currentTimeMillis();
		sb.append(currentTime);
		// 指定随机数产生的范围
		for (int i = 0; i < 8; i++) {
			// Random的nextInt(int n)方法返回一个[0, n)范围内的随机数
			sb.append(rd1.nextInt(10));
		}
		return sb.toString();
	}

	/**
	 * 判断是否为空
	 * @param s
	 * @return
	 */
	public static boolean isEmpty(String s) {
		return (null != s && s.length() > 0) ? false : true;
	}
	
	/**
	 * 做URLEncode编码
	 * @param s
	 * @return
	 */
	public static String URLEncoder(String s) {
		if (s == null || s.length() == 0)
			return "";
		try {
			s = java.net.URLEncoder.encode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return "";
		} catch (NullPointerException e) {
			return "";
		}
		return s;
	}
}
