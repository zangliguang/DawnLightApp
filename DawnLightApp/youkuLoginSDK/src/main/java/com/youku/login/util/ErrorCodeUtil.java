package com.youku.login.util;

import android.text.TextUtils;

public class ErrorCodeUtil {

	public static ErrorCodeUtil mErrorCodeRegistLogin;
	
	public ErrorCodeUtil(){};
	
	public static ErrorCodeUtil getInstance(){
		if(mErrorCodeRegistLogin == null){
			mErrorCodeRegistLogin = new ErrorCodeUtil();
		}
		return mErrorCodeRegistLogin;
	}
	
	
	/**
	 * 第三方登录接口返回的错误码
	 * 
	 * -501 参数uid为空请 输入正确用户名 -502 参数appid为空 参数不正确，稍后重试 -503 参数tuid为空 请输入正确用户名
	 * -504 存在重复的appid+tuid 参数不正确稍后重试 -505 存在重复的appid+uid 参数不正确稍后重试
	 * -506因为其他原因，添加失败， 非常抱歉，操作失败请稍后重试。
	 */
	public final String SNS_LOGIN_ERRORCODE_UID_BE_EMPTY = "-501";
	public final String SNS_LOGIN_ERRORCODE_APPID_EMPTY = "-502";
	public final String SNS_LOGIN_ERRORCODE_TUID_EMPTY = "-503";
	public final String SNS_LOGIN_ERRORCODE_APPID_TUID_ERROR = "-504";
	public final String SNS_LOGIN_ERRORCODE_APPID_UID_ERROR = "-505";
	public final String SNS_LOGIN_ERRORCODE_OTHER_REASON = "-506";

	public final String TIP_MESSAGE_INPUT_RIGHT_USERNAME = "输入正确用户名";
	public final String TIP_MESSAGE_PARAMETER_WRONG = "参数不正确，稍后重试";
	public final String TIP_MESSAGE_SORRY_FAIL_TRY_AGAIN_LATER = " 非常抱歉，操作失败请稍后重试。";

	//显示ErrorCode信息
	public void showErrorMessage4SNSLogin(String errorCode){
		String errorResult = "";
		if(TextUtils.isEmpty(errorCode)){
    		YoukuUtil.showTips("操作失败，请稍后再试 ");
    		return;
    	}
		if(SNS_LOGIN_ERRORCODE_UID_BE_EMPTY.equals(errorCode)){
			errorResult = TIP_MESSAGE_INPUT_RIGHT_USERNAME;
		}else if(SNS_LOGIN_ERRORCODE_APPID_EMPTY.equals(errorCode)){
			errorResult = TIP_MESSAGE_PARAMETER_WRONG;
		}else if(SNS_LOGIN_ERRORCODE_TUID_EMPTY.equals(errorCode)){
			errorResult = TIP_MESSAGE_INPUT_RIGHT_USERNAME;
		}else if(SNS_LOGIN_ERRORCODE_APPID_TUID_ERROR.equals(errorCode)){
			errorResult = TIP_MESSAGE_PARAMETER_WRONG;
		}else if(SNS_LOGIN_ERRORCODE_APPID_UID_ERROR.equals(errorCode)){
			errorResult = TIP_MESSAGE_PARAMETER_WRONG;
		}else if(SNS_LOGIN_ERRORCODE_OTHER_REASON.equals(errorCode)){
			errorResult = TIP_MESSAGE_PARAMETER_WRONG;
		}else{
			errorResult = TIP_MESSAGE_SORRY_FAIL_TRY_AGAIN_LATER;
		}
		YoukuUtil.showTips(errorResult);
	}
	
	/**
	 * 
	 * 优酷帐号绑定错误码
	 * 
	 * -401 缺少appid，uid，tuid中某个或几个参数 非常抱歉 参数错误请重试！ -402 操作失败 操作失败，请重试！
	 * -403appid或uid参数格式错误 非常抱歉，您输入的格式不正确！
	 */
	public final String YOUKU_BIND_ERRORCODE_NO_PARAMETER = "-401";
	public final String YOUKU_BIND_ERRORCODE_OPERATE_FAIL = "-402";
	public final String YOUKU_BIND_ERRORCODE_APPID_OR_UID_ERROR = "-403";

	public final String TIP_MESSAGE_YOUKU_BIND_PARAMETER_WRONG = "非常抱歉 参数错误请重试！";
	public final String TIP_MESSAGE_YOUKU_BIND_OPERATE_FAIL = "操作失败，请重试！";
	public final String TIP_MESSAGE_YOUKU_BIND_YOUR_FORMAT_WRONG = "非常抱歉，您输入的格式不正确！";

	
	//显示ErrorCode信息
	public void showErrorMessage4YoukuBind(String errorCode){
		String errorResult = "";
		if(TextUtils.isEmpty(errorCode)){
    		YoukuUtil.showTips("操作失败，请稍后再试 ");
    		return;
    	}
		if(YOUKU_BIND_ERRORCODE_NO_PARAMETER.equals(errorCode)){
			errorResult = TIP_MESSAGE_YOUKU_BIND_PARAMETER_WRONG;
		}else if(YOUKU_BIND_ERRORCODE_OPERATE_FAIL.equals(errorCode)){
			errorResult = TIP_MESSAGE_YOUKU_BIND_OPERATE_FAIL;
		}else if(YOUKU_BIND_ERRORCODE_APPID_OR_UID_ERROR.equals(errorCode)){
			errorResult = TIP_MESSAGE_YOUKU_BIND_YOUR_FORMAT_WRONG;
		}else{
			errorResult = TIP_MESSAGE_SORRY_FAIL_TRY_AGAIN_LATER;
		}
		YoukuUtil.showTips(errorResult);
	}
		
	
	/**
	 * 手机号码注册时
	 * 
	 * 下发注册验证码时错误码
	 * 
	 * 
	 * -100 缺少mobile参数 请输入正确的11位手机号码。 -101 缺少tcode 参数 -102 缺少msgmodel参数您输入的 -103
	 * 缺少IP参数 -200 IP请求过于频繁 请求过于频繁，请休息会再重试 -201 手机号码不合法 请输入正确的11位手机号码 -202
	 * 短信下发失败 非常抱歉短信发送失败，稍后重试 -203 tcode不合法 -204 msgmodel不合法 非常抱歉你输入的信息不正确 -205
	 * msgmodel超长 非常抱歉，您输入信息超长
	 */
	public final String PHONE_REGIST_AUTH_CODE_NO_MOBILE_PARAMETER = "-100";
	public final String PHONE_REGIST_AUTH_CODE_NO_TCODE = "-101";
	public final String PHONE_REGIST_AUTH_CODE_NO_MSGMODE = "-102";
	public final String PHONE_REGIST_AUTH_CODE_NO_IP_PARAMETER = "-103";
	public final String PHONE_REGIST_AUTH_CODE_IP_REQUEST_TOO_FREQUENT = "-200";
	public final String PHONE_REGIST_AUTH_CODE_MOBILE_NUMBER_NO_RIGHT = "-201";
	public final String PHONE_REGIST_AUTH_CODE_MESSAGE_GET_FAIL = "-202";
	public final String PHONE_REGIST_AUTH_CODE_TCODE_NOT_RIGHT = "-203";
	public final String PHONE_REGIST_AUTH_CODE_MSGMODEL_TOO_LONG = "-205";
	public final String PHONE_REGIST_AUTH_CODE_REQUEST_FREQUENTLY = "-214";
	public final String PHONE_REGIST_AUTH_CODE_OVER_CONTROL = "-301";
	public final String PHONE_REGIST_AUTH_CODE_MSGMODEL_NOT_RIGHT = "-409";

	public final String TIP_MESSAGE_AUTH_CODE_INPUT_RIGHT_NUMBER = "请输入正确的11位手机号码。";
	public final String TIP_MESSAGE_AUTH_CODE_REQUEST_FRQUNTLY = "请求过于频繁，请休息会再重试";
	public final String TIP_MESSAGE_AUTH_CODE_SORRY_MESSAGE_SEND_ERROR = "非常抱歉短信发送失败，稍后重试";
	public final String TIP_MESSAGE_AUTH_CODE_SORRY_MESSAGE_WRONG = "非常抱歉你输入的信息不正确";
	public final String TIP_MESSAGE_AUTH_CODE_MESSAGE_TOO_LONG = "非常抱歉，您输入信息超长";
	public final String TIP_MESSAGE_AUTH_CODE_OPERATE_FAIL = "操作失败，请重试！";
	public final String TIP_MESSAGE_AUTH_CODE_OVER_CONTROL = "每天最多发送三次，请明天再试";

	
	//显示ErrorCode信息
	public void showErrorMessage4PhoneRegist(String errorCode){
		String errorResult = "";
		if(TextUtils.isEmpty(errorCode)){
    		YoukuUtil.showTips("操作失败，请稍后再试 ");
    		return;
    	}
		if(PHONE_REGIST_AUTH_CODE_NO_MOBILE_PARAMETER.equals(errorCode)){
			errorResult = TIP_MESSAGE_AUTH_CODE_INPUT_RIGHT_NUMBER;
		}else if(PHONE_REGIST_AUTH_CODE_NO_TCODE.equals(errorCode)){
			errorResult = TIP_MESSAGE_AUTH_CODE_OPERATE_FAIL;
		}else if(PHONE_REGIST_AUTH_CODE_NO_MSGMODE.equals(errorCode)){
			errorResult = TIP_MESSAGE_AUTH_CODE_OPERATE_FAIL;
		}else if(PHONE_REGIST_AUTH_CODE_NO_IP_PARAMETER.equals(errorCode)){
			errorResult = TIP_MESSAGE_AUTH_CODE_OPERATE_FAIL;
		}else if(PHONE_REGIST_AUTH_CODE_IP_REQUEST_TOO_FREQUENT.equals(errorCode)){
			errorResult = TIP_MESSAGE_AUTH_CODE_REQUEST_FRQUNTLY;
		}else if(PHONE_REGIST_AUTH_CODE_REQUEST_FREQUENTLY.equals(errorCode)){
			errorResult = TIP_MESSAGE_AUTH_CODE_REQUEST_FRQUNTLY;
		}else if(PHONE_REGIST_AUTH_CODE_MOBILE_NUMBER_NO_RIGHT.equals(errorCode)){
			errorResult = TIP_MESSAGE_AUTH_CODE_INPUT_RIGHT_NUMBER;
		}else if(PHONE_REGIST_AUTH_CODE_MESSAGE_GET_FAIL.equals(errorCode)){
			errorResult = TIP_MESSAGE_AUTH_CODE_SORRY_MESSAGE_SEND_ERROR;
		}else if(PHONE_REGIST_AUTH_CODE_TCODE_NOT_RIGHT.equals(errorCode)){
			errorResult = TIP_MESSAGE_AUTH_CODE_OPERATE_FAIL;
		}else if(PHONE_REGIST_AUTH_CODE_MSGMODEL_NOT_RIGHT.equals(errorCode)){
			errorResult = TIP_MESSAGE_AUTH_CODE_SORRY_MESSAGE_WRONG;
		}else if(PHONE_REGIST_AUTH_CODE_OVER_CONTROL.equals(errorCode)){
			errorResult = TIP_MESSAGE_AUTH_CODE_REQUEST_FRQUNTLY;
		}else if(PHONE_REGIST_AUTH_CODE_MSGMODEL_TOO_LONG.equals(errorCode)){
			errorResult = TIP_MESSAGE_AUTH_CODE_MESSAGE_TOO_LONG;
		}else{
			errorResult = TIP_MESSAGE_AUTH_CODE_OPERATE_FAIL;
		}
		YoukuUtil.showTips(errorResult);
	}
	
	/**
	 * 手机号码注册时
	 * 
	 * 检验手机验证码，返回的错误码
	 * 
	 * -100 缺少mobile参数 请填写正确的11位手机号码 -101 缺少tcode 参数 -103 缺少IP参数 -104 缺少code参数
	 * 请输入验证码 -200 IP请求过于频繁 您请求的太快了，稍后重试！ -201 手机号码不合法 非常抱歉，您的手机号注册失败。 -206
	 * Code校验错误 请填写正确的验证码
	 * 
	 */
	public final String PHONE_REGIST_CHECK_AUTH_CODE_NO_MOBILE = "-100";
	public final String PHONE_REGIST_CHECK_AUTH_CODE_NO_TCODE = "-101";
	public final String PHONE_REGIST_CHECK_AUTH_CODE_NO_IP = "-103";
	public final String PHONE_REGIST_CHECK_AUTH_CODE_NO_CODE = "-104";
	public final String PHONE_REGIST_CHECK_AUTH_CODE_IP_REQUEST_FREQUNTLY = "-200";
	public final String PHONE_REGIST_CHECK_AUTH_CODE_MOBILE_WRONG = "-201";
	public final String PHONE_REGIST_CHECK_AUTH_CODE_CODE_WRONG = "-206";

	public final String TIP_MESSAGE_REGIST_INPUT_RIGHT_NUMBER = "请填写正确的11位手机号码";
	public final String TIP_MESSAGE_REGIST_INPUT_CODE = "请输入验证码";
	public final String TIP_MESSAGE_REGIST_REQUES_FREQUNTLY = "请求过于频繁，请休息会再重试";
	public final String TIP_MESSAGE_REGIST_PHONE_WRONG = "非常抱歉，您的手机号注册失败。";
	public final String TIP_MESSAGE_REGIST_ERROE_CODE = "请填写正确的验证码";
	public final String TIP_MESSAGE_REGIST_OPERATE_FAIL = "操作失败，请重试！";

	//显示ErrorCode信息
	public void showErrorMessage4AuthCode(String errorCode){
		String errorResult = "";
		if(TextUtils.isEmpty(errorCode)){
    		YoukuUtil.showTips("操作失败，请稍后再试 ");
    		return;
    	}
		if(PHONE_REGIST_CHECK_AUTH_CODE_NO_MOBILE.equals(errorCode)){
			errorResult =  TIP_MESSAGE_REGIST_INPUT_RIGHT_NUMBER;
		}else if(PHONE_REGIST_CHECK_AUTH_CODE_NO_TCODE.equals(errorCode)){
			errorResult =  TIP_MESSAGE_REGIST_OPERATE_FAIL;
		}else if(PHONE_REGIST_CHECK_AUTH_CODE_NO_IP.equals(errorCode)){
			errorResult =  TIP_MESSAGE_REGIST_OPERATE_FAIL;
		}else if(PHONE_REGIST_CHECK_AUTH_CODE_NO_CODE.equals(errorCode)){
			errorResult =  TIP_MESSAGE_REGIST_INPUT_CODE;
		}else if(PHONE_REGIST_CHECK_AUTH_CODE_IP_REQUEST_FREQUNTLY.equals(errorCode)){
			errorResult =  TIP_MESSAGE_REGIST_REQUES_FREQUNTLY;
		}else if(PHONE_REGIST_CHECK_AUTH_CODE_MOBILE_WRONG.equals(errorCode)){
			errorResult =  TIP_MESSAGE_REGIST_PHONE_WRONG;
		}else if(PHONE_REGIST_CHECK_AUTH_CODE_CODE_WRONG.equals(errorCode)){
			errorResult =  TIP_MESSAGE_REGIST_ERROE_CODE;
		}else{
			errorResult =  TIP_MESSAGE_YOUKU_BIND_OPERATE_FAIL;
		}
		YoukuUtil.showTips(errorResult);
	}
	
	
	/**
	 * 手机号码注册时
	 * 
	 * 注册并登录时返回的错误码
	 * 
	 * 
	 * -100 缺少mobile参数请填写11位手机号 -101 缺少tcode 参数 -103 缺少ip参数 -104 缺少code参数
	 * 请填写正确验证码 -200 IP请求过于频繁您的操作过于频繁，请稍后重试 -201 手机号码不合法 非常抱歉，您输入的手机号码不正确。 -206
	 * Code校验错误 请填写正确验证码！ -207 用户被屏蔽非常抱歉，您的手机号注册失败。
	 */
	public final String PHONE_REGIST_AND_LOGIN_NO_MOBILE = "-100";
	public final String PHONE_REGIST_AND_LOGIN_NO_TCODE = "-101";
	public final String PHONE_REGIST_AND_LOGIN_NO_IP = "-103";
	public final String PHONE_REGIST_AND_LOGIN_NO_CODE = "-104";
	public final String PHONE_REGIST_AND_LOGIN_IP_REQUEST_FREQUNTLY = "-200";
	public final String PHONE_REGIST_AND_LOGIN_MOBILE_WRONG = "-201";
	public final String PHONE_REGIST_AND_LOGIN_CODE_WRONG = "-206";
	public final String PHONE_REGIST_AND_LOGIN_USER_SHIELD = "-207";
	public final String PHONE_REGIST_AND_LOGIN_USER_HAS_BEEN_USED = "-208";

	public final String TIP_MESSAGE_REGIST_AND_LOGIN_INPUT_RIGHT_NUMBER = "请填写正确的11位手机号码";
	public final String TIP_MESSAGE_REGIST_AND_LOGIN_REQUES_FREQUNTLY = "请求过于频繁，请休息会再重试";
	public final String TIP_MESSAGE_REGIST_AND_LOGIN_PHONE_WRONG = "非常抱歉，您输入的手机号码不正确。";
	public final String TIP_MESSAGE_REGIST_AND_LOGIN_ERROR_CODE = "请填写正确验证码！";
	public final String TIP_MESSAGE_REGIST_AND_LOGIN_SHIELD_USER = "非常抱歉，您的手机号注册失败。";
	public final String TIP_MESSAGE_REGIST_AND_LOGIN_USER_HAS_USED = "此手机号码已经被注册过";

	//显示ErrorCode信息
	public void showErrorMessage4LoginRegist(String errorCode){
		String errorResult = "";
		if(TextUtils.isEmpty(errorCode)){
    		YoukuUtil.showTips("操作失败，请稍后再试 ");
    		return;
    	}
		if(PHONE_REGIST_AND_LOGIN_NO_MOBILE.equals(errorCode)){
			errorResult =  TIP_MESSAGE_REGIST_AND_LOGIN_INPUT_RIGHT_NUMBER;
		}else if(PHONE_REGIST_AND_LOGIN_NO_TCODE.equals(errorCode)){
			errorResult =  TIP_MESSAGE_REGIST_OPERATE_FAIL;
		}else if(PHONE_REGIST_AND_LOGIN_NO_IP.equals(errorCode)){
			errorResult =  TIP_MESSAGE_REGIST_OPERATE_FAIL;
		}else if(PHONE_REGIST_AND_LOGIN_NO_CODE.equals(errorCode)){
			errorResult =  TIP_MESSAGE_REGIST_AND_LOGIN_ERROR_CODE;
		}else if(PHONE_REGIST_AND_LOGIN_IP_REQUEST_FREQUNTLY.equals(errorCode)){
			errorResult =  TIP_MESSAGE_REGIST_AND_LOGIN_REQUES_FREQUNTLY;
		}else if(PHONE_REGIST_AND_LOGIN_MOBILE_WRONG.equals(errorCode)){
			errorResult =  TIP_MESSAGE_REGIST_AND_LOGIN_PHONE_WRONG;
		}else if(PHONE_REGIST_AND_LOGIN_CODE_WRONG.equals(errorCode)){
			errorResult =  TIP_MESSAGE_REGIST_AND_LOGIN_ERROR_CODE;
		}else if(PHONE_REGIST_AND_LOGIN_USER_SHIELD.equals(errorCode)){
			errorResult =  TIP_MESSAGE_REGIST_AND_LOGIN_SHIELD_USER;
		}else if(PHONE_REGIST_AND_LOGIN_USER_HAS_BEEN_USED.equals(errorCode)){
			errorResult =  TIP_MESSAGE_REGIST_AND_LOGIN_USER_HAS_USED;
		}else{
			errorResult = TIP_MESSAGE_REGIST_OPERATE_FAIL;
		}
		YoukuUtil.showTips(errorResult);
	}
	
	
	
	
}
