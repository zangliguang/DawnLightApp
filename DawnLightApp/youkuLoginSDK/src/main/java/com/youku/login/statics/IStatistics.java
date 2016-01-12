package com.youku.login.statics;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;

public interface IStatistics {
	
	/**
	 * 初始化统计SDK
	 * @param context
	 * @param userAgent
	 * @param pid
	 */
	public void init(Context context, String userAgent, String pid);
	
	/**
	 * 在Activity的onResume方法中调用。
	 * context - Activity引用，不要传递ApplicationContext
	 * page - 当前所在页面的名称pagename，由客户端定义，应清晰表明页面名称属性，如pagename_load， 其中pagename必须与产品PRD页面名称定义一致
	 * userID - 用户id，若没有登录则传null或””
	 */
	public void  startSession(Activity context, String pageName);
	
	/**
	 * 在Activity的onPause方法中调用
	 * context - Activity引用，不要传递ApplicationContext
	 * page - 当前所在页面的名称pagename，由客户端定义，应清晰表明页面名称属性，如pagename_load， 其中pagename必须与产品PRD页面名称定义一致
	 * userID - 用户id，若没有登录则传null或””
	 */
	public void endSession(Activity context);
	
	/**
	 * 自定义事件，不带extend参数
	 *  context - Activity或ApplicationContext引用
	 *  name - 事件名称
     *  page - 所在页面
     *  target - "target_操作对象名称", #操作对象名称必须与产品PRD页面名称定义一致
     *  userID - 用户id，若没有登录则传null或”“
	 */
	public void trackCustomEvent(Context context, String name, String page, String target);
	
	/**
	 * 自定义事件，带extend参数
	 *  context - Activity或ApplicationContext引用
	 *  name - 事件名称
     *  page - 所在页面
     *  target - "target_操作对象名称", #操作对象名称必须与产品PRD页面名称定义一致
     *  userID - 用户id，若没有登录则传null或”“
	 *  entend - 扩展字段，自定义键值对
	 */
	public void trackExtendCustomEvent(Context context, String name, String page, String target, HashMap<String, String> extend);
	
	/**
	 * 自定义事件，带extend参数
	 *  context - Activity或ApplicationContext引用
	 *  name - 事件名称
     *  page - 所在页面
     *  target - "target_操作对象名称", #操作对象名称必须与产品PRD页面名称定义一致
     *  userID - 用户id，若没有登录则传null或”“
	 *  entend - 扩展字段，自定义键值对
	 *  session - 指定的 session
	 */
	public void trackExtendCustomEventWithSession(Context context, String name, String page, String target, HashMap<String, String> extend, String session);
	
	/**
	 * 播放请求
	 * context - Activity或ApplicationContext引用
	 * vid - 视频id
	 * playType - 播放类型
	 * userID - 用户id，若没有登录则传null或”“
	 */
	public void playRequest(Context context, String vid, String playType);
	
	/**
	 * 播放开始
	 * context - Activity或ApplicationContext引用
     * vid - 视频id
     * playType - 播放类型
     * userID - 用户id，若没有登录则传null或”“
	 */
	public void playStart(Context context, String vid, String playcode);
	
	/**
	 * 播放暂停
	 * context - Activity或ApplicationContext引用
     * vid - 视频id
     * userID - 用户id，若没有登录则传null或”“
	 */
	public void playPause(Context context, String vid);
	
	/**
	 * 继续播放
	 * context - Activity或ApplicationContext引用
     * vid - 视频id
     * playcode - 播放状态码
     * userID - 用户id，若没有登录则传null或”“
	 */
	public void playContinue(Context context, String vid, String playcode);
	
	/**
	 * 播放结束
	 * context - Activity或ApplicationContext引用
     * vid - 视频id
     * complete - 是否播放完
     * userID - 用户id，若没有登录则传null或”“
	 */
	public void playEnd(Context context, String vid, boolean complete);
	
	/**
	 * 广告开始播放
	 * context - Activity或ApplicationContext引用
     * vid - 视频id
     * complete - 是否播放完
     * adUrls - 广告系统返回的相关URL
     * userID - 用户id，若没有登录则传null或”“
	 */
	public void adPlayStart(Context context, String vid, boolean complete, ArrayList<String> adUrls);
	
	/**
	 * 广告播放结束
	 * context - Activity或ApplicationContext引用
     * vid - 视频id
     * complete - 是否播放完
     * adUrls - 广告系统返回的相关URL
     * userID - 用户id，若没有登录则传null或”“
	 */
	public void adPlayEnd(Context context, String vid, boolean complete,	ArrayList<String> adUrls);
	
	/**
	 * 播放心跳包，每分钟发送一次
	 * context - Activity或ApplicationContext引用
     * vid - 视频id
     * userID - 用户id，若没有登录则传null或”“
	 */
	public void playHeart(Context context, String vid);
	
	/**
	 * 是否开启日志显示
	 * @param debug
	 */
	public void setDebug(boolean debug);
	/**
	 * 是否是Host类型的
	 * @param debug
	 */
	public void setTestHost(boolean debug);
	/**
	 * 是否是测试日志
	 * @param debug
	 */
	public void setTest(boolean debug);
	
	/**
	 * 设置定时发送间隔，单位毫秒
	 * @param time
	 */
	public void setContinueSessionMillis(long time);
	
	/**
	 * 设置是否收集位置信息
	 * @param isTrack
	 */
	public void setTrackLocation(boolean isTrack);
	
	/**
	 * 设置文件缓存上限，单位字节
	 * @param size
	 */
	public void setCacheSize(int size);
	
	/**
	 * 设置在一个页面中事件存储上限，如果达到上限则立即发送
	 * @param size
	 */
	public void setEventSize(int size);
	
	/**
	 * 设置缓存发送的百分比，当存储达到百分比则发送数据，persentage大于0小于1
	 * @param persentage
	 */
	public void setCachePersentage(float persentage);
	
	/**
	 * 设置userAgent，UA由【软件名称;软件版本;操作系统;系统版本;终端型号】5个参数组成，各参数之间以“；”分号分割，详情见http://wiki.1verge.net/wireless:stat:ua
	 * @param context
	 * @param userAgent
	 */
	public void setUserAgent(Context context, String userAgent);
	
	/**
	 * 在调用android.os.Process.killProcess前调用，用来保存数据，若不调用会造成数据丢失
	 * context - Activity或ApplicationContext引用
	 * userID - 用户id，若没有登录则传null或””
	 */
	public void onKillProcess(Context context);
	
	
	
}
