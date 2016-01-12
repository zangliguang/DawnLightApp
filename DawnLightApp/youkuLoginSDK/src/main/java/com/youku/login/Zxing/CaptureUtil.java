package com.youku.login.Zxing;

import android.content.res.Configuration;
import android.text.TextUtils;

import com.youku.login.util.Youku;
import com.youku.login.util.YoukuUtil;


public class CaptureUtil {

	/**
	 * -200 二维码错误——非常抱歉，没有扫描到视频或登录信息 
	 * -300 手机授权问题——操作失败，请稍后再试
	 * -301 code无效——暂时未能识别该二维码，请重试 
	 * -302 uid无效 ——操作失败，请稍后再试 
	 * -306 pc端已经登录——您的电脑已登录，不需要重新登录
	 * 
	 * -400 PC授权手机登陆——操作失败，请稍后再试 
	 * -401 code无效，——暂时未能识别该二维码，请重试 
	 * -402 code未授权或pc端未登录，pc端捕获后需要继续轮询，——请确认您的电脑是否已登录，如已登录请稍后再试
	 * -405   登录失败，——操作失败，请稍后再试 
	 * -409 mobile已经扫描code,但未授权——操作失败，请稍后再试 
	 * -100  代表CMS开关控制不能扫描PC让移动登录——非常抱歉该功能暂时关闭，请在移动设备登录页登录
	 * -500  代表app和pc端都没登陆——请登陆手机端或者电脑端
	 * 
	 */

	public static final String MESSAGE_QCODE_ERROR = "-200";
	public static final String MESSAGE_PHONE_AUTHORIZATE_ERROR = "-300";
	public static final String MESSAGE_CODE_VOID_ERROR = "-301";
	public static final String MESSAGE_UID_VOID_ERROR = "-302";
	public static final String MESSAGE_PC_HAS_LOGINED_ERROR = "-306";
	public static final String MESSAGE_LOGIN_SUCCESS = "-400";
	public static final String MESSAGE_PC_CODE_VOID_ERROR = "-401";
	public static final String MESSAGE_NEED_POLL_ERROR = "-402";
	public static final String MESSAGE_LOGIN_FAIL_ERROR = "-405";
	public static final String MESSAGE_NO_AUTHORIZATE_ERROR = "-409";
	public static final String MESSAGE_NO_CMS_CONTROL_ERROR = "-100";
	public static final String MESSAGE_NO_APP_OR_PC_LOGIN = "-500";

	public static final  int REQUEST_CMCC_LENGTH =200;//为修复连接到CMCC的WiFi的时候问题
	
	public static CaptureUtil mCaptureUtil = null;

	public CaptureUtil() {
		
	};

	public static CaptureUtil getInstance() {
		if (null == mCaptureUtil) {
			mCaptureUtil = new CaptureUtil();
		}
		return mCaptureUtil;
	}

	public static boolean getOrientionConfiguration() {
		if (Youku.mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			return true;
		}
		return false;
	}

	
    /**
     * 显示错误信息提示
     * @param str
     */
    public void showErrorMessageTip(String str){
    	if(TextUtils.isEmpty(str)){
    		YoukuUtil.showTips("操作失败，请稍后再试 ");
    		return ;
    	}
    	
		if(CaptureUtil.MESSAGE_QCODE_ERROR.equals(str)){
			YoukuUtil.showTips("非常抱歉，没有扫描到视频或登录信息 ");
		}else if(CaptureUtil.MESSAGE_PHONE_AUTHORIZATE_ERROR.equals(str)){
			YoukuUtil.showTips("操作失败，请稍后再试 ");
		}else if(CaptureUtil.MESSAGE_CODE_VOID_ERROR.equals(str)){
			YoukuUtil.showTips("暂时未能识别该二维码，请重试");
		}else if(CaptureUtil.MESSAGE_UID_VOID_ERROR.equals(str)){
			YoukuUtil.showTips("操作失败，请稍后再试");
		}else if(CaptureUtil.MESSAGE_PC_HAS_LOGINED_ERROR.equals(str)){
			YoukuUtil.showTips("您的电脑已登录，不需要重新登录 ");
		}else if(CaptureUtil.MESSAGE_LOGIN_SUCCESS.equals(str)){
			YoukuUtil.showTips("操作失败，请稍后再试");
		}else if(CaptureUtil.MESSAGE_PC_CODE_VOID_ERROR.equals(str)){
			YoukuUtil.showTips("暂时未能识别该二维码，请重试 ");
		}else if(CaptureUtil.MESSAGE_NEED_POLL_ERROR.equals(str)){
			YoukuUtil.showTips("请确认您的电脑是否已登录，如已登录请稍后再试 ");
		}else if(CaptureUtil.MESSAGE_LOGIN_FAIL_ERROR.equals(str)){
			YoukuUtil.showTips("操作失败，请稍后再试");
		}else if(CaptureUtil.MESSAGE_NO_AUTHORIZATE_ERROR.equals(str)){
			YoukuUtil.showTips("操作失败，请稍后再试 ");
		}else if(CaptureUtil.MESSAGE_NO_CMS_CONTROL_ERROR.equals(str)){
			YoukuUtil.showTips("非常抱歉该功能暂时关闭，请在移动设备登录页登录");
		}else if(CaptureUtil.MESSAGE_NO_APP_OR_PC_LOGIN.equals(str)){
			YoukuUtil.showTips("请确认您的电脑或手机任何一方已登录");
		}else if(str.length()>REQUEST_CMCC_LENGTH){
			YoukuUtil.showTips("操作失败，请检查网络是否通畅，稍后再试  ");
		}else{
			YoukuUtil.showTips("操作失败，请稍后再试 ");
		}
    }
    
}
