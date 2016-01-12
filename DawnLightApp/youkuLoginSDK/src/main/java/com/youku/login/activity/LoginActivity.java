/*
 * @(#)LoginActivity.java	 2012-11-5
 *
 * Copyright 2005-2012 YOUKU.com
 * All rights reserved.
 * 
 * YOUKU.com PROPRIETARY/CONFIDENTIAL.
 */

package com.youku.login.activity;

import java.util.LinkedList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.youkuloginsdk.R;
import com.youku.login.util.Logger;
import com.youku.login.util.YoukuUtil;
import com.youku.login.widget.YoukuLoading;


/**
 * 启动这个页面需要的intent 启动哪个页面-intent.getIntExtra("curfragment", Login_Fragment);
 * 从什么activity启动的-from =intent.getIntExtra("from", 0);
 * 
 * @author 刘仲男修改
 * 
 */
public class LoginActivity {//extends BaseActivity {

	public final static int LOGIN_SUCCESS = 2000;// 登录成功msng标识
	public final static int LOGIN_FAIL = 2001;// 登录失败msg标识
	public final static int GO_REGIST = 2002;// 去注册页面msg标识
	public final static int GO_LOGIN = 2003;// 去登录页面msg标识
	public final static int REGIST_SUCCESS = 2004;// 注册成功msg标识
	public final static int REGIST_FAIL = 2005;// 注册失败msg标识
	public final static int NO_NETWORK = 2006;// 无网标识

	private final static int Login_Fragment = 0;// 启动登录intent的内容
	private final static int Regist_Fragment = 1;// 启动注册intent的内容

	public final static int INTENT_LOGIN = 0;// 启动登录intent的内容
	public final static int INTENT_REGISTER = 1;// 启动注册intent的内容

	public final static int NICK_NAME_MIN_LENGTH = 4; // 昵称最小长度规则
														// 按字符的算（中文的需要自己在方法里换算）
	public final static int NICK_NAME_MAX_LENGTH = 16;// 昵称最大长度规则
														// 按字符的算（中文的需要自己在方法里换算）
	public final static int PWD_MIN_LENGTH = 6;// 密码最小长度规则
	public final static int PWD_MAX_LENGTH = 16;// 密码最大长度规则
	public final static int VERIFICATION_CODE_LENGTH = 4;// 验证码长度规则

	/**
	 * 埋点的统计
	 */
	public final static int INTENT_UPLOAD_ACTIVITY = 1000; // 上传页面的标志
	public final static int INTENT_FAVORITE_ACTIVITY = 1001;// 收藏页面的标志
	public final static int INTENT_CHANNEL_ACTIVITY = 1002;// 大此页面的标志
	public final static int INTENT_HOME_PAGE_ACTIVITY = 1003;// 首页的标志
	public final static int INTENT_DETAIL_ACTIVITY_COMMENT = 1004;// 详情评论触发登录的标志
	public final static int INTENT_DETAIL_ACTIVITY_SHARE = 1005;// 详情分享触发登录的标志
	public final static int INTENT_DETAIL_ACTIVITY_FAVORITE = 1006;// 详情收藏触发登录的标志
	public final static int INTENT_PLAYER_ACTIVITY_SHARE = 1007;// 播放器分享触发登录的标志
	public final static int INTENT_PLAYER_ACTIVITY_FAVORITE = 1008;// 播放器收藏触发登录的标志
	public final static int INTENT_HAVEBUY_ACTIVITY = 1009; // 已购页面的标志
	public final static int INTENT_CAPTURERESULT_ACTIVITY = 1010; // 扫一扫结果页面的标志
	public final static int INTENT_NEED_PAY = 1011; // 需要付费的标志
	public final static int INTENT_BUY_VIP = 1012; // 需要购买会员的标志
	public final static int INTENT_SUBSCRIBE = 1013; // 我的订阅的标志
	public final static int INTENT_ADD_SUBSCRIBE = 1014; // 添加订阅的标志
	
	public final static int INTENT_HOME_YOUKU_GUESS_TAB = 1015;// 优酷猜
	
	public final static int INTENT_INTERACT_POINT = 1016;// 互动娱乐

	/** 从哪个页面来的key */
	public static final String KEY_FROM = "from";
	/** 收藏跳过来的VID */
	public static final String KEY_FAVOR_VID = "FAVOR_VID";
	/** 收藏跳过来的SHOWID */
	public static final String KEY_FAVOR_SHOWID = "FAVOR_SHOWID";
	private int from = 0; // 记录从哪个页面启动的登录注册模块

	private int curfragment = Login_Fragment; // 如果没有intent默认是登录页面
	// private String starterActivity = "";// 记录启动此页面的activity，用于埋点
	private static final String TAG = "LoginActivity";
	
	public static final int LOGIN_REQUEST = 2000;

//	private LoginFragment loginfragment;// 登录页面
//	private RegistFragment registfragment;// 注册页面
	
//	public final static LinkedList<Runnable> favorateRunnable = new LinkedList<Runnable>();
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_login_regist);
//		Intent intent = getIntent();
//		curfragment = intent.getIntExtra("curfragment", Login_Fragment);
//		from = intent.getIntExtra(KEY_FROM, 0);
//
//		if (savedInstanceState != null) {
//			curfragment = savedInstanceState.getInt("cur_fragment");
//			Logger.d(TAG, "activity onSaveInstanceState onCreate = "
//					+ curfragment);
//			if (curfragment == Login_Fragment) {
//				loginfragment = (LoginFragment) getSupportFragmentManager()
//						.findFragmentById(R.id.login_curview);
//				registfragment = new RegistFragment();
//			} else {
//				loginfragment = new LoginFragment();
//				registfragment = (RegistFragment) getSupportFragmentManager()
//						.findFragmentById(R.id.login_curview);
//			}
//
//		} else {// 为了提示语
//			if (from == INTENT_FAVORITE_ACTIVITY
//					|| from == INTENT_PLAYER_ACTIVITY_FAVORITE
//					|| from == INTENT_DETAIL_ACTIVITY_FAVORITE) {
//				YoukuUtil.showTips(R.string.user_login_tip_favorite);
//			} else if (from == INTENT_UPLOAD_ACTIVITY) {
//				YoukuUtil.showTips(R.string.user_login_tip_upload);
//			} else if (from == INTENT_CHANNEL_ACTIVITY) {
//				YoukuUtil.showTips(R.string.tips_add_tag_need_login);
//			} else if (from == INTENT_HOME_PAGE_ACTIVITY) {
//				YoukuUtil.showTips(R.string.tips_add_tag_need_login);
//			} else if (from == INTENT_DETAIL_ACTIVITY_COMMENT) {
//				YoukuUtil.showTips(R.string.user_login_tip_comment);
//			} else if (from == INTENT_PLAYER_ACTIVITY_SHARE
//					|| from == INTENT_DETAIL_ACTIVITY_SHARE) {
//				YoukuUtil.showTips(R.string.user_login_tip_share);
//			} else if (from == INTENT_HAVEBUY_ACTIVITY) {
//				YoukuUtil.showTips(R.string.user_login_tip_pay);
//			} else if (from == INTENT_CAPTURERESULT_ACTIVITY) {
//				YoukuUtil.showTips(R.string.user_login_tip_pay);
//			} else if (from == INTENT_NEED_PAY) {
//				YoukuUtil.showTips(R.string.user_login_tip_pay);
//			} else if (from == INTENT_BUY_VIP) {
//				YoukuUtil.showTips(R.string.user_login_tip_buy_vip);
//			}else if (from == INTENT_SUBSCRIBE) {
//				YoukuUtil.showTips(R.string.user_login_tip_subscribe);
//			}else if (from == INTENT_ADD_SUBSCRIBE) {
//				YoukuUtil.showTips(R.string.other_person_info_follow_need_login);
//			}else if (from == INTENT_INTERACT_POINT) {
//				YoukuUtil.showTips(R.string.interactpoint_login_tips);
//			}
//
//			loginfragment = new LoginFragment();
//			registfragment = new RegistFragment();
//			changeFragment(curfragment);
//		}
//		loginfragment.setHanlder(msgHandler);
//		registfragment.setHandler(msgHandler);
//	}
//
//	/**
//	 * 用于埋点
//	 */
//	private void trackEvent(int event) {
//		if (event == LOGIN_SUCCESS) { // 登录成功
//			switch (from) {
//			case INTENT_CHANNEL_ACTIVITY:
//				break;
//			case INTENT_FAVORITE_ACTIVITY:
//				break;
//			case INTENT_UPLOAD_ACTIVITY:
//				break;
//			case INTENT_HOME_PAGE_ACTIVITY:
//				break;
//			case INTENT_DETAIL_ACTIVITY_COMMENT:
//				break;
//			case INTENT_DETAIL_ACTIVITY_SHARE:
//				break;
//			case INTENT_DETAIL_ACTIVITY_FAVORITE:
//				break;
//			case INTENT_PLAYER_ACTIVITY_FAVORITE:
//				break;
//			case INTENT_PLAYER_ACTIVITY_SHARE:
//				break;
//			case INTENT_HAVEBUY_ACTIVITY:
//
//				break;
//			case INTENT_CAPTURERESULT_ACTIVITY:
//				break;
//			case INTENT_NEED_PAY:
//				break;
//			default:
//				break;
//			}
//		} else if (event == GO_REGIST) {// 去注册页面
//			switch (from) {
//			case INTENT_CHANNEL_ACTIVITY:
//				break;
//			case INTENT_FAVORITE_ACTIVITY:
//				break;
//			case INTENT_UPLOAD_ACTIVITY:
//				break;
//			case INTENT_HOME_PAGE_ACTIVITY:
//				break;
//			case INTENT_DETAIL_ACTIVITY_COMMENT:
//				break;
//			case INTENT_DETAIL_ACTIVITY_SHARE:
//				break;
//			case INTENT_DETAIL_ACTIVITY_FAVORITE:
//				break;
//			case INTENT_PLAYER_ACTIVITY_FAVORITE:
//				break;
//			case INTENT_PLAYER_ACTIVITY_SHARE:
//				break;
//			case INTENT_HAVEBUY_ACTIVITY:
//
//				break;
//			case INTENT_CAPTURERESULT_ACTIVITY:
//				break;
//			case INTENT_NEED_PAY:
//				break;
//			default:
//				break;
//			}
//		} else if (event == REGIST_SUCCESS) {// 注册成功
//			switch (from) {
//			case INTENT_CHANNEL_ACTIVITY:
//				break;
//			case INTENT_FAVORITE_ACTIVITY:
//				break;
//			case INTENT_UPLOAD_ACTIVITY:
//				break;
//			case INTENT_HOME_PAGE_ACTIVITY:
//				break;
//			case INTENT_DETAIL_ACTIVITY_COMMENT:
//				break;
//			case INTENT_DETAIL_ACTIVITY_SHARE:
//				break;
//			case INTENT_DETAIL_ACTIVITY_FAVORITE:
//				break;
//			case INTENT_PLAYER_ACTIVITY_FAVORITE:
//				break;
//			case INTENT_PLAYER_ACTIVITY_SHARE:
//				break;
//			case INTENT_HAVEBUY_ACTIVITY:
//
//				break;
//			case INTENT_CAPTURERESULT_ACTIVITY:
//				break;
//			case INTENT_NEED_PAY:
//				break;
//			default:
//				break;
//			}
//		}
//	}
//
//	@Override
//	public void showCustomTitle() {
//		getSupportActionBar().setDisplayShowCustomEnabled(true);
//		getSupportActionBar().setCustomView(R.layout.channel_custom_title);
//		View channel_custom_title_img = findViewById(R.id.channel_custom_title_img);
//		channel_custom_title_img.setVisibility(View.GONE);
//		TextView channel_custom_title_txt = (TextView) findViewById(R.id.channel_custom_title_txt);
//		if(Login_Fragment == curfragment){
//			channel_custom_title_txt.setText(getString(R.string.login));
//		}else{
//			channel_custom_title_txt.setText(getString(R.string.register));
//		}
//	}
//
//	private Handler msgHandler = new Handler() {
//		public void handleMessage(Message msg) {
//			switch (msg.what) {
//			case LOGIN_SUCCESS:
//				YoukuUtil.showTips(R.string.login_success);
//				trackEvent(LOGIN_SUCCESS);
//				YoukuLoading.dismiss();
//				doSuccess();
//				setsuccessResult();
//				finish();
//				break;
//			case LOGIN_FAIL:
//				// Util.showTips(R.string.user_login_fail);
//				YoukuLoading.dismiss();
//				break;
//			case GO_REGIST:
//				changeFragment(Regist_Fragment);
//				trackEvent(GO_REGIST);
//				break;
//			case GO_LOGIN:
//				changeFragment(Login_Fragment);
//				break;
//			case REGIST_SUCCESS:
//				YoukuUtil.showTips(R.string.register_success);
//				trackEvent(REGIST_SUCCESS);
//				YoukuLoading.dismiss();
//				doSuccess();
//				setsuccessResult();
//				finish();
//				break;
//			case REGIST_FAIL:
//				YoukuLoading.dismiss();
//				break;
//			case NO_NETWORK:
//				YoukuUtil.showTips(R.string.tips_no_network);
//				break;
//			}
//		}
//	};
//
//	public void setsuccessResult() {
//		setResult(Activity.RESULT_OK);
//	}
//	
//	/**登录成功要做的事情*/
//	private void doSuccess(){
//		if(getIntent().getIntExtra(KEY_FROM, -1)==INTENT_DETAIL_ACTIVITY_FAVORITE
//				||getIntent().getIntExtra(KEY_FROM, -1)==INTENT_HOME_YOUKU_GUESS_TAB){
//			while (!favorateRunnable.isEmpty()) {
//				favorateRunnable.poll().run();
//			}
//		}
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// TODO Auto-generated method stub
//		if (item.getItemId() == android.R.id.home) {
//			if (loginfragment.isFromMeToRegister) {
//				if (curfragment == Login_Fragment) {
//				} else {
//					changeFragment(Login_Fragment);
//					return true;
//				}
//			}
//			setResult(0);
//		}
//		return super.onOptionsItemSelected(item);
//	}
//
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
//			if (loginfragment.isFromMeToRegister) {
//				if (curfragment == Login_Fragment) {
//				} else {
//					changeFragment(Login_Fragment);
//					return true;
//				}
//			}
//			setResult(0);
//		}
//		return super.onKeyDown(keyCode, event);
//	}
//
//	/**
//	 * 更换页为注册还是登陆
//	 * 
//	 * @param key
//	 */
//	public void changeFragment(int key) {
//		switch (key) {
//		case Login_Fragment:
//			curfragment = Login_Fragment;
//			showCustomTitle();
//			if (registfragment.isResumed() && 0 != registfragment.getId()) {
//				getSupportFragmentManager().beginTransaction()
//						.replace(registfragment.getId(), loginfragment)
//						.commit();
//			} else {
//				getSupportFragmentManager().beginTransaction()
//						.replace(R.id.login_curview, loginfragment).commit();
//			}
//			break;
//		case Regist_Fragment:
//			curfragment = Regist_Fragment;
//			showCustomTitle();
//			if (loginfragment.isResumed() && 0 != loginfragment.getId()) {
//				getSupportFragmentManager().beginTransaction()
//						.replace(loginfragment.getId(), registfragment)
//						.commit();
//			} else {
//				getSupportFragmentManager().beginTransaction()
//						.replace(R.id.login_curview, registfragment).commit();
//			}
//			break;
//		default:
//			break;
//		}
//	}
//
//	public static Bitmap getBitmap(String iconBase64) {
//		Bitmap bitmap = null;
//		try {
//			byte[] bitmapArray;
//			bitmapArray = Base64.decode(iconBase64, Base64.DEFAULT);
//			bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0,
//					bitmapArray.length);
//		} catch (Exception e) {
//			Logger.e(TAG, e);
//		}
//		return bitmap;
//	}
//
//	@Override
//	protected void onSaveInstanceState(Bundle outState) {
//		Logger.d(TAG, "activity onSaveInstanceState curfragment = "
//				+ curfragment);
//		outState.putInt("cur_fragment", curfragment);
//		YoukuLoading.dismiss();
//		super.onSaveInstanceState(outState);
//	}
//
//	@Override
//	public String getPageName() {
//		return "登录页";
//	}
//
//	@Override
//	protected void onDestroy() {
//		msgHandler.removeCallbacksAndMessages(null);
//		msgHandler = null;
//		YoukuLoading.dismiss();
//		super.onDestroy();
//	}
}
