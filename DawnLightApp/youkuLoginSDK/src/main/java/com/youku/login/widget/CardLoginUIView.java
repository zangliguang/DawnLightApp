package com.youku.login.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.example.youkuloginsdk.R;
import com.youku.login.activity.LoginActivity;
import com.youku.login.network.URLContainer;
import com.youku.login.service.ILogin;
import com.youku.login.service.ILogin.ICallBack;
import com.youku.login.service.LoginException;
import com.youku.login.service.YoukuService;
import com.youku.login.sns.AuthorizationLoginActivity;
import com.youku.login.sns.LoginByQQAccount;
import com.youku.login.sns.LoginBySinaWeibo;
import com.youku.login.sns.WeiXinLoginManager;
import com.youku.login.sns.util.ConfigUtil;
import com.youku.login.statics.StaticsConfigFile;
import com.youku.login.util.Logger;
import com.youku.login.util.Youku;
import com.youku.login.util.YoukuUtil;
 

public class CardLoginUIView extends LinearLayout{
	
	private static String emailRecord = "";// 记录用户名
	private static String passRecord = "";// 记录密码
	private static boolean emailvalidate = true;// 记录用户名
	private static boolean passvalidate = true;// 记录密码
	private View loginView;// fragment的layout
	private View Btn_login;// 登录按钮
	private View login_more;// 登录按钮
	private View sns_login_linearlayout,sns_sina_login_view,
					sns_qq_login_view,sns_weixin_login_view,sns_alipay_login_view;// sns登录布局
//	private View regist_new_account;// 注册新账号按钮
//	private View Btn_ToRegist;// 去注册页面按钮
//	private View Btn_ToForgetPasword;// 去忘记密码界面
	private EditText loginname;// 用户名输入框
	private EditText loginpwd;// 密码输入框
	private ImageView email_left_img;// 用户名下边的线的左边
	private ImageView email_right_img;// 用户名下边的线的右边
	private ImageView email_mid_img;// 用户名下边的线
	private ImageView pass_left_img;// 密码下边的线的左边
	private ImageView pass_right_img;// 密码下边的线的右边
	private ImageView pass_mid_img;// 密码下边的线
	private TextView login_tip_psw;// 密码下面的提示语
	private TextView login_tip_name;// 密用户名下面的提示语
	private ImageView user_forget_password_line;// 忘记密码按钮
	private TextView user_forget_password_textview;// 忘记密码按钮
	
	private boolean islogin = false; // 标志位，防止用户不停点击登录而进行不必要的联网操作
	private int typeScle;
	
	public boolean isFromMeToRegister;
	private PopupMenu mPopupMenu;
	private YoukuPopupMenu mYoukuPopupMenu;
	
	public CardLoginUIView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initViewLayout();
	}
	
	private void initViewLayout(){
		View view = LayoutInflater.from(getContext()).inflate(R.layout.card_view_login_ui_layout, null);
		initView(view);
		setOnclickEvent();
		addView(view);
	}
	
	private void initView(View v) {
		Btn_login = v.findViewById(R.id.user_login);
		login_more = v.findViewById(R.id.login_more);
		sns_login_linearlayout = v.findViewById(R.id.sns_login_linearlayout);
		sns_sina_login_view = v.findViewById(R.id.sns_sina_login_view);
		sns_qq_login_view = v.findViewById(R.id.sns_qq_login_view);
		sns_weixin_login_view = v.findViewById(R.id.sns_weixin_login_view);
		sns_alipay_login_view = v.findViewById(R.id.sns_alipay_login_view);
//		regist_new_account = v.findViewById(R.id.regist_new_account);
//		Btn_ToRegist = v.findViewById(R.id.user_regist);
//		Btn_ToForgetPasword = v.findViewById(R.id.user_forget_password);
		loginname = (EditText) v.findViewById(R.id.login_name);
		loginpwd = (EditText) v.findViewById(R.id.login_pwd);
		email_left_img = (ImageView) v.findViewById(R.id.email_left_line);
		email_right_img = (ImageView) v.findViewById(R.id.email_right_line);
		email_mid_img = (ImageView) v.findViewById(R.id.email_mid_line);
		pass_left_img = (ImageView) v.findViewById(R.id.pass_left_line);
		pass_right_img = (ImageView) v.findViewById(R.id.pass_right_line);
		pass_mid_img = (ImageView) v.findViewById(R.id.pass_mid_line);
		login_tip_name = (TextView) v.findViewById(R.id.login_tip_name);
		login_tip_psw = (TextView) v.findViewById(R.id.login_tip_psw);
		user_forget_password_textview = (TextView) v.findViewById(R.id.user_forget_password_textview);
		user_forget_password_line = (ImageView) v.findViewById(R.id.user_forget_password_line);
		
	

		autoSetLoginedUserName();
	}
	

	
	//如果登陆过了，就自动填充登录用户名
	private void autoSetLoginedUserName(){
		if (!Youku.getPreference("loginAccount").equals("")) {
			loginname.setText(Youku.getPreference("loginAccount"));
			loginpwd.requestFocus();
		}
	}
	
	public View getSNSLineaLayout(){
		return sns_login_linearlayout;
	}
	
	public void clearAllFocus(){
		clearChildFocus(loginname);
		clearChildFocus(loginpwd);
		setFocusableInTouchMode(false);
		initLoginUIView();
	}
	
//	public void setAllViewFocusabInTouch(){
//		setFocusableInTouchMode(true);
//		loginpwd.setFocusableInTouchMode(true);
//		loginpwd.setFocusableInTouchMode(true);
//	}
	
	//初始化登录界面的UI
	public void initLoginUIView(){
		loginname.setText("");
		loginpwd.setText("");
		setEmailColor(true);
		setPassColor(true);
	}
	
//	//显示软键盘
//	public void showSoftWare(View edit){
//		edit.setFocusable(true);  
//		edit.setFocusableInTouchMode(true);  
//		edit.requestFocus(); 
//		edit.setClickable(true);
//		edit.setSelected(true);
//	    InputMethodManager inputManager = (InputMethodManager)edit.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);  
//	    Logger.lxf("====Login界面===软键盘是否活动===isActive===="+inputManager.isActive(edit));
//	    if(!inputManager.isActive(edit)){
//	    	inputManager.toggleSoftInput(InputMethodManager.SHOW_FORCED,0); 
////	    	inputManager.showSoftInput(edit, InputMethodManager.SHOW_FORCED);
//	    }
//	}
//	
//	public TextView getCopRightText(){
//		return user_forget_password_textview;
//	}
//	
//	private Handler handerSoftWare=new Handler(){  
//        public void handleMessage(android.os.Message msg) {
//        	View view = (View)msg.obj;
//        	showSoftWare(view); 
//        };  
//    };  
//	
	private void setOnclickEvent(){
		// 当用户名错误的时候，用户修改任意用户名红色不显示
		loginname.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				Logger.lxf("====用户名的焦点===hasFocus=="+hasFocus);
				if (!hasFocus) {
					final String ls = loginname.getText().toString().trim();
					if (!TextUtils.isEmpty(ls)) {
						login_tip_name.setText("");
						if (!emailRecord.equals(ls)) {
							setEmailColor(true);
						}
					}else {
						YoukuUtil.showTips(R.string.enter_user_name);
//						login_tip_name.setText(R.string.enter_user_name);
						setEmailColor(false);
					}
				}
			}
		});
		loginname.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				Message message = new Message();
//				message.obj = loginname; 
//				handerSoftWare.sendMessageDelayed(message, 500);
//				showSoftWare(loginname);
				Logger.lxf("====输入用户名被点击了=============");
//				setAllViewFocusabInTouch();
			}
		});
		// 当密码错误的时候，用户修改任意密码，红色不显示
		loginpwd.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					final String ps = loginpwd.getText().toString().trim();
					if (!TextUtils.isEmpty(ps)) {
						login_tip_psw.setText("");
						if (!passRecord.equals(ps)) {
							setPassColor(true);
						}
					} else {
						YoukuUtil.showTips(R.string.regist_user_pwd1_tip_empty);
//						login_tip_psw
//								.setText(R.string.regist_user_pwd1_tip_empty);
						setPassColor(false);
					}
				}
			}
		});
		Btn_login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				StaticsConfigFile.loginPath = StaticsConfigFile.LOGIN_PATH_YOUKU_OR_TUDOU_LOGIN;
				StaticsConfigFile.loginType = StaticsConfigFile.LOGIN_TYPE_ACTIVE_LOGIN;
				if (!YoukuUtil.hasInternet()) {// 无网提示
					YoukuUtil.showTips(R.string.tips_no_network);
					return;
				}
				String pass_word = loginpwd.getText().toString().trim();
				String user_name = loginname.getText().toString().trim();
				setEmailColor(true);
				setPassColor(true);

				// 验证用户名是否为空
				if (user_name == null || "".equals(user_name)) {
					YoukuUtil.showTips(R.string.enter_user_name);
//					login_tip_name.setText(R.string.enter_user_name);
					// Util.showTips(R.string.enter_user_name);
					setEmailColor(false);
					return;
				} else {
					login_tip_name.setText("");
					setEmailColor(true);
				}
				// 验证密码是否为空
				if (pass_word == null || "".equals(pass_word)) {
					// alertLoginError(R.string.text_password_null);
					// Util.showTips(R.string.enter_password);
					YoukuUtil.showTips(R.string.regist_user_pwd1_tip_empty);
//					login_tip_psw.setText(R.string.regist_user_pwd1_tip_empty);
					setPassColor(false);
					return;
				} else {
					login_tip_psw.setText("");
					setPassColor(true);
				}
				passRecord = pass_word;
				emailRecord = user_name;
				doLogin(user_name, pass_word);
			}
		});
		
	
		
//		// 点击注册按钮
//		((RelativeLayout)regist_new_account.getParent()).setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				regist_new_account.performClick();
//			}
//		});
		// 点击注册按钮
//		regist_new_account.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				isFromMeToRegister = true;
//				new LoginHandler().sendEmptyMessage(LoginActivity.GO_REGIST);
//			}
//		});
//		// 点击注册按钮
//		Btn_ToRegist.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				isFromMeToRegister = true;
//				handler.sendEmptyMessage(LoginActivity.GO_REGIST);
//			}
//		});
		// 点击忘记密码
		user_forget_password_textview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				YoukuUtil.goWebViewWithParameter(getContext(),
						URLContainer.getForgetPasswordPageUrl(),"找回密码");
			}
		});
//		// 点击忘记密码
//		Btn_ToForgetPasword.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				YoukuUtil.goWebView(getContext(),
//						URLContainer.getForgetPasswordPageUrl());
//			}
//		});
		

		/**
		 * 当焦点在昵称上的时候，软键盘的完成功能为下一个
		 */
		loginname.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER) {
					if (event.getAction() == KeyEvent.ACTION_UP)
						loginpwd.requestFocus();
					return true;
				}
				return false;
			}
		});

		/**
		 * 焦点在用户名上的时候，软件盘显示的为下一个
		 */
		loginname.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_NEXT) {
					loginpwd.requestFocus();
				}
				return false;
			}
		});
		
		/**
         * 当焦点在密码框上的时候，软键盘的完成功能为点击登录
         */
        loginpwd.setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
					InputMethodManager imm = (InputMethodManager) Youku.mContext
							.getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        imm.hideSoftInputFromWindow(
                                v.getApplicationWindowToken(), 0);
                    }
                    if (event.getAction() == KeyEvent.ACTION_UP)
                        Btn_login.performClick();
                    return true;
                }
                return false;
            }
        });
        
        /**
         * 当焦点在密码框上的时候，软键盘的完成功能为点击登录
         */
        loginpwd.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                    KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager imm = (InputMethodManager)Youku.mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        imm.hideSoftInputFromWindow(
                                v.getApplicationWindowToken(), 0);
                    }
                    Btn_login.performClick();
                }
                return false;
            }
        });
        

		// 点击新浪微博登录入口
		sns_sina_login_view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				StaticsConfigFile.loginPath = StaticsConfigFile.LOGIN_PATH_SINA_WEIBO_LOGIN;
				StaticsConfigFile.loginType = StaticsConfigFile.LOGIN_TYPE_ACTIVE_LOGIN;
				YoukuLoading.show(getContext());
				if(!YoukuUtil.hasInternet()) {
					YoukuUtil.showTips(R.string.tips_no_network);
					YoukuLoading.dismiss();
					return;
				}
				ConfigUtil.oauthInter = new LoginBySinaWeibo();
				Intent intent = new Intent(getContext(), AuthorizationLoginActivity.class);
				getContext().startActivity(intent);
				YoukuLoading.dismiss();
			}
		});

		// 点击QQ账号登录入口
		sns_qq_login_view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				StaticsConfigFile.loginPath = StaticsConfigFile.LOGIN_PATH_QQ_ACCOUNT_LOGIN;
				StaticsConfigFile.loginType = StaticsConfigFile.LOGIN_TYPE_ACTIVE_LOGIN;
				YoukuLoading.show(getContext());
				if (!YoukuUtil.hasInternet()) {
					YoukuUtil.showTips(R.string.tips_no_network);
					YoukuLoading.dismiss();
					return;
				}
				ConfigUtil.oauthInter = new LoginByQQAccount();
				Intent intent = new Intent(getContext(), AuthorizationLoginActivity.class);
				getContext().startActivity(intent);
				YoukuLoading.dismiss();
			}
		});
       
		// 点击微信账号登录入口
		sns_weixin_login_view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				StaticsConfigFile.loginPath = StaticsConfigFile.LOGIN_PATH_WEIXIN_LOGIN;
				StaticsConfigFile.loginType = StaticsConfigFile.LOGIN_TYPE_ACTIVE_LOGIN;
				WeiXinLoginManager.getInstance().doLogin(getContext());
			}
		});
		
/**		// 点击支付宝账号登录入口
		sns_alipay_login_view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				StaticsConfigFile.loginPath = StaticsConfigFile.LOGIN_PATH_ALI_PAY_LOGIN;
				StaticsConfigFile.loginType = StaticsConfigFile.LOGIN_TYPE_ACTIVE_LOGIN;
				AlipayLoginManager.getInstance().doLogin((LoginRegistCardViewDialogActivity)getContext());
			}
		});
*/
	}
	

	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		setFocusableInTouchMode(true);
		loginname.setFocusableInTouchMode(true);
		loginpwd.setFocusableInTouchMode(true);
	}

	/**
	 * 登录
	 * 
	 * @param username
	 * @param password
	 */
	private void doLogin(String username, String password) {

		if (password.length() != 32)
			password = YoukuUtil.md5(password);
		YoukuLoading.show(getContext());
		if (islogin) {// 防止用户不停电机登录按钮
			return;
		}
		Logger.d("sgh","doLogin()");
		islogin = true;
		ILogin ilogin = YoukuService.getService(ILogin.class, true);
		ilogin.login(username, password, new ICallBack() {

			@Override
			public void onSuccess() {
				new LoginHandler().sendEmptyMessage(LoginActivity.LOGIN_SUCCESS);
				islogin = false;
			}

			@Override
			public void onFailed(LoginException e) {
				setEmailColor(true);
				setPassColor(true);
				switch (e.getErrorCode()) {
				case 0:
					YoukuUtil.showTips(R.string.user_login_fail);// 登录失败，用户名或密码错误
					setEmailColor(false);
					setPassColor(false);
					break;
				case -401:
					YoukuUtil.showTips(R.string.user_login_fail);// 密码错误
					setPassColor(false);
					break;
				case -402:
					YoukuUtil.showTips(R.string.user_login_error_no_user);// 用户不存在
					setEmailColor(false);
					setPassColor(false);
					break;
				case -403:
					YoukuUtil.showTips(R.string.login_error_unknown);// 用户被屏蔽
					break;
				case -404:
					YoukuUtil.showTips(R.string.login_error_unknown);// 连续5次输入错误密码，请稍候再试!
					setPassColor(false);
					break;
				case -405:
					YoukuUtil.showTips(R.string.login_error_unknown);// 该IP一小时内登录错误帐号次数太多
					break;
				case -406:
					YoukuUtil.showTips(R.string.login_error_unknown);// 该IP当日登录帐号数已达上线
					break;
				case -407:
					YoukuUtil
							.showTips(R.string.text_login_err_email_not_verified);// 邮箱未验证,请先验证再登录
					setEmailColor(false);
					break;
				case 0x7f0b0039: //case R.string.tips_not_responding:
					YoukuUtil.showTips(R.string.tips_not_responding); // 弱网
					break;
				default:
					YoukuUtil.showTips(R.string.login_error_unknown);// 位置错误
					break;
				}
				islogin = false;
				new LoginHandler().sendEmptyMessage(LoginActivity.LOGIN_FAIL);
			}
		});
	}

	private class LoginHandler extends Handler{

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case LoginActivity.LOGIN_SUCCESS:
				YoukuUtil.showTips(R.string.login_success);
				YoukuLoading.dismiss();
				finishSelf();
				Logger.lxf("======login_success===========");
				break;
			case LoginActivity.LOGIN_FAIL:
				YoukuLoading.dismiss();
				Logger.lxf("======Login fail===========");
				break;
			case LoginActivity.GO_REGIST:
				Logger.lxf("======GO_REGIST==========");
				break;
			default:
				break;
			}
			
			
		}
		
	}
	
	public void finishSelf(){
		removeAllViews();
		setVisibility(View.GONE);
		Logger.lxf("======getContext()======="+getContext());
//		((LoginActivity)getContext()).setResult(100);
//		((LoginActivity)getContext()).finish();
	}
	
	
	/**
	 * 设置颜色 正确是绿色，错误是红色
	 * 
	 * @param Correct
	 *            昵称是否正确
	 */
	private void setEmailColor(boolean Correct) {
		if(null==getContext()){
			return;
		}
		try {
			if (Correct) {
				email_left_img.setBackgroundColor(getContext().getResources()
						.getColor(R.color.login_n_register_line_correct));
				email_right_img.setBackgroundColor(getContext().getResources()
						.getColor(R.color.login_n_register_line_correct));
				email_mid_img.setBackgroundColor(getContext().getResources()
						.getColor(R.color.login_n_register_line_correct));
				emailvalidate = true;
				loginname.setTextColor(getContext().getResources().getColor(R.color.text_color_gray_7));
//				loginname.setTextAppearance(getContext(),
//						R.style.Font3_gray_999999);
			} else {
				loginname.setTextColor(getContext().getResources().getColor(R.color.text_color_red_1));
				email_left_img.setBackgroundColor(getContext().getResources()
						.getColor(R.color.login_n_register_line_wrong));
				email_right_img.setBackgroundColor(getContext().getResources()
						.getColor(R.color.login_n_register_line_wrong));
				email_mid_img.setBackgroundColor(getContext().getResources()
						.getColor(R.color.login_n_register_line_wrong));
				emailvalidate = false;
			}
		} catch (NullPointerException e) {
			Logger.d("LoginFragment", "setEmailColor--NullPointerException");
		}
	}

	/**
	 * 获取当前的email是否错误
	 * 
	 * @return
	 */
	private boolean getEmailValidation() {
		return emailvalidate;
	}

	/**
	 * 设置颜色 正确是绿色，错误是红色
	 * 
	 * @param Correct
	 *            昵称是否正确
	 */
	private void setPassColor(boolean Correct) {
		if(null==getContext()){
			return;
		}
		try {
			if (Correct) {
				loginpwd.setTextColor(getContext().getResources().getColor(R.color.text_color_gray_7));
				pass_left_img.setBackgroundColor(getContext().getResources()
						.getColor(R.color.login_n_register_line_correct));
				pass_right_img.setBackgroundColor(getContext().getResources()
						.getColor(R.color.login_n_register_line_correct));
				pass_mid_img.setBackgroundColor(getContext().getResources()
						.getColor(R.color.login_n_register_line_correct));
				user_forget_password_line.setBackgroundColor(getContext().getResources()
						.getColor(R.color.login_n_register_line_correct));
				passvalidate = true;
			} else {
				loginpwd.setTextColor(getContext().getResources().getColor(R.color.text_color_red_1));
				pass_left_img.setBackgroundColor(getContext().getResources()
						.getColor(R.color.login_n_register_line_wrong));
				pass_right_img.setBackgroundColor(getContext().getResources()
						.getColor(R.color.login_n_register_line_wrong));
				pass_mid_img.setBackgroundColor(getContext().getResources()
						.getColor(R.color.login_n_register_line_wrong));
				user_forget_password_line.setBackgroundColor(getContext().getResources()
						.getColor(R.color.login_n_register_line_wrong));
				passvalidate = false;
			}
		} catch (NullPointerException e) {
			Logger.d("LoginFragment", "setPassColor--NullPointerException");
		}
	}

	/**
	 * 获取当前的密码是否错误
	 * 
	 * @return
	 */
	private boolean getPassValidation() {
		return passvalidate;
	}

	public View getLoginMenuMor(){
		return login_more;
	}

	
}
