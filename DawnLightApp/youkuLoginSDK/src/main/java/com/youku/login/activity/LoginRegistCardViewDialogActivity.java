package com.youku.login.activity;

import java.util.LinkedList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.youkuloginsdk.R;
import com.youku.login.service.ILogin;
import com.youku.login.sns.WeiXinLoginManager;
import com.youku.login.statics.StaticsConfigFile;
import com.youku.login.util.Logger;
import com.youku.login.util.YoukuUtil;
import com.youku.login.widget.YoukuLoading;


public class LoginRegistCardViewDialogActivity extends Activity {
//	private CardLoginRegistView login_regist_card_view;
	 
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


	// 埋点的统计
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
	
	/**个人中心用户等级触发登录的标志 */
	public final static int INTENT_OPEN_LEVEL = 1017;

	/** 从哪个页面来的key */
	public static final String KEY_FROM = "from";
	/** 从哪个页面 统计使用 */
	public static final String TRACK_LOGIN_SOURCE = "track_login_source";
	/** 收藏跳过来的VID */
	public static final String KEY_FAVOR_VID = "FAVOR_VID";
	
	/** 收藏跳过来的SHOWID */
	public static final String KEY_FAVOR_SHOWID = "FAVOR_SHOWID";
	
	private int from = 0; // 记录从哪个页面启动的登录注册模块

	private int curfragment = Login_Fragment; // 如果没有intent默认是登录页面
	
	public static final int LOGIN_REQUEST = 2000;
	
//	public final static LinkedList<Runnable> favorateRunnable = new LinkedList<Runnable>();
	
	public static LoginRegistCardViewDialogActivity instance = null;
	
	@SuppressLint("InlinedApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		setFullscreen();
		if(android.os.Build.VERSION.SDK_INT>14){
			setTheme(android.R.style.Theme_DeviceDefault_Light_Dialog);
		}else{
			setTheme(android.R.style.Theme_Dialog);
		}
		setContentView(R.layout.card_view_login_or_regist_activity);
		//login_regist_card_view = (CardLoginRegistView)findViewById(R.id.login_regist_card_view);
		
//		setHeightLoginRegistView();
//		showFromTip();
		registBroadReceiver();
	}

	private void registBroadReceiver(){
		IntentFilter filter = new IntentFilter();
		filter.addAction(ILogin.LOGOUT_BROADCAST);
		filter.addAction(ILogin.LOGIN_BROADCAST);
		registerReceiver(receiver, filter);
	}
	
	/** 登录的广播接收（刷新个人中心和频道） */
	private BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(ILogin.LOGIN_BROADCAST)) {
				Logger.lxf("========登录成功之后关闭==登录的广播接收（刷新个人中心和频道）=======");
//				LoginRegistCardViewDialogActivity.this.setResult(Activity.RESULT_OK);
//				doSuccess();
				finish();
			}
			if(action.equals(ILogin.LOGOUT_BROADCAST)){
				Logger.lxf("========登出界面=======");
			}
		}
	};
	

	
	/**登录成功要做的事情*/
	private void doSuccess(){
		if(getIntent().getIntExtra(KEY_FROM, -1)==INTENT_DETAIL_ACTIVITY_FAVORITE
				||getIntent().getIntExtra(KEY_FROM, -1)==INTENT_HOME_YOUKU_GUESS_TAB){
//			while (!favorateRunnable.isEmpty()) {
//				favorateRunnable.poll().run();
//			}
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
	}




	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Logger.lxf("=========登录界面的onBackPressed()===========");
	}

//	private void setHeightLoginRegistView(){
//		Logger.lxf("=====屏幕高===="+Device.ht);
//		int heightScreen = Device.ht/2;
//		Logger.lxf("=====heightScreen===="+heightScreen);
//		
//		login_regist_card_view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,heightScreen)); 
//	}
	
	public void setFullscreen() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(null!=receiver){
			unregisterReceiver(receiver);
		}
		WeiXinLoginManager.getInstance().clear();
//AlipayLoginManager.getInstance().clear();
		YoukuLoading.dismiss();
//		favorateRunnable.clear();
		instance = null;
		Logger.lxf("====登录成功之后关闭=========");
	}

}
