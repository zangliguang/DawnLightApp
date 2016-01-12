package com.youku.login.widget;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.example.youkuloginsdk.R;
import com.youku.login.activity.LoginRegistCardViewDialogActivity;
import com.youku.login.network.HttpIntent;
import com.youku.login.network.HttpRequestManager;
import com.youku.login.network.IHttpRequest.IHttpRequestCallBack;
import com.youku.login.network.URLContainer;
import com.youku.login.service.ILogin;
import com.youku.login.service.ILogin.ICallBack;
import com.youku.login.service.LoginException;
import com.youku.login.service.YoukuService;
import com.youku.login.util.ErrorCodeUtil;
import com.youku.login.util.Logger;
import com.youku.login.util.Youku;
import com.youku.login.util.YoukuUtil;


@SuppressLint("HandlerLeak")
public class CardRegistUIView extends LinearLayout {

	/* 注册条款 */
	public static final String AGREEMENT_URL = "http://www.youku.com/pub/youku/service/agreement.shtml ";
	/* 版权 */
	public static final String COPYRIGHT_URL = "http://www.youku.com/pub/youku/service/copyright.shtml ";

	public final static int NICK_NAME_MIN_LENGTH = 4; // 昵称最小长度规则
	// 按字符的算（中文的需要自己在方法里换算）
	public final static int NICK_NAME_MAX_LENGTH = 16;// 昵称最大长度规则
		// 按字符的算（中文的需要自己在方法里换算）
	public final static int PWD_MIN_LENGTH = 6;// 密码最小长度规则
	public final static int PWD_MAX_LENGTH = 16;// 密码最大长度规则
	public final static int VERIFICATION_CODE_LENGTH = 4;// 验证码长度规则
	
	private static String emailRecord = "";// 记录用户名
	private static String nameRecord = "";// 记录用户名
	private static boolean namevalidate = true;// 记录用户名
	private static boolean emailvalidate = true;// 记录邮箱
	private static boolean passvalidate = true;// 记录密码
	private static boolean codevalidate = true;// 记录验证码

	// 注册失败的原因 bitmap
	private int bit_auth_error = 4; // 认证错误
	private int bit_username_exists = 8; // 用户已经存在
	private int bit_username_invalid = 16; // 用户名不可用
	private int bit_email_exists = 32; // email已经存在
	private int bit_email_invalid = 64; // email非法

	private View btn_regist;// 注册按钮

	private View RegistView;// fragment的整体布局
	private View layout_phone_number_regist_view,layout_email_address_regist_view;
	private RelativeLayout layout_phone_number_regist;// 手机号码注册
	private RelativeLayout layout_email_address_regist;// 邮箱地址注册
	private TextView layout_phone_number_regist_txtview;// 手机号码注册
	private TextView layout_email_address_regist_txtview;// 邮箱地址注册
//	private TextView reback_login_textview;// 返回登录界面的按钮
	private TextView user_regist_get_authcode_textview;// 发送验证码
	private ImageView user_regist_get_authcode_line;// 发送验证码文字的左边
	private EditText regist_email;// 邮箱的输入框
	private EditText regist_name;// 昵称的输入框
	private EditText regist_pwd1;// 密码的输入框
	private ImageView email_left_img;// 邮箱下边的线的左边
	private ImageView email_right_img;// 邮箱下边的线的右边
	private ImageView email_mid_img;// 邮箱下边的线
	private ImageView name_left_img;// 用户名下边的线的左边
	private ImageView name_right_img;// 用户名下边的线的右边
	private ImageView name_mid_img;// 用户名下边的线
	private ImageView pass_left_img;// 密码下边的线的左边
	private ImageView pass_right_img;// 密码下边的线的右边
	private ImageView pass_mid_img;// 密码下边的线
	private TextView register_email_tip;// 邮箱底下的红色提示
	private TextView register_name_tip;// 昵称底下的红色提示
	private TextView register_pwd1_tip;// 密码底下的红色提示
	private TextView copyright;// 版权
	private TextView agreement;// 注册条款
	private CountTimerTextView mCountTimerTextView;// 倒计时计时器

	private boolean isregist = false;// 标志位，防止用户不停点击注册而进行不必要的联网操作
	// 是否是手机号码注册
	private boolean isPhoneNumberRegist = true;
	
	private RegistHandler registHandler = new RegistHandler();

	
	public CardRegistUIView(Context context) {
		super(context);
		initViewLayout();
	}
	
	public CardRegistUIView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initViewLayout();
	}

	private void initViewLayout(){
		View view = LayoutInflater.from(getContext()).inflate(R.layout.card_view_regist_layout, null);
		initView(view);
		addView(view);
		setEmailColor(true);
		setNameColor(true);
		setPassColor(true);
		AddListener();
//		hideSoftWare();
	}
	
	//隐藏软键盘
	private void hideSoftWare(){
		((InputMethodManager)getContext().getSystemService(getContext().INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
				regist_email.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}
	
	//显示软键盘
	public void showSoftWare(View edit){
		edit.requestFocus();  
		edit.setFocusable(true);  
		edit.setFocusableInTouchMode(true);
		edit.setClickable(true);
		edit.setSelected(true);
         InputMethodManager inputManager = (InputMethodManager)edit.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);  
         Logger.lxf("====注册里面的==软键盘是否活动===isActive===="+inputManager.isActive(edit));
         if(!inputManager.isActive(edit)){
//        	 inputManager.showSoftInput(edit, InputMethodManager.SHOW_FORCED);
        	 inputManager.toggleSoftInput(InputMethodManager.SHOW_FORCED,0); 
         }
//         boolean result = inputManager.showSoftInput(edit, InputMethodManager.SHOW_FORCED);  
         
//         Logger.lxf("====注册里面的==软键盘执行的结果===result===="+result);
//         if(inputManager.isActive(edit)){
//        	 inputManager.showSoftInput(edit, 0);  
//         }
	}
	
	
	private Handler handerSoftWare=new Handler(){  
        public void handleMessage(android.os.Message msg) {
        	View view = (View)msg.obj;
        	showSoftWare(view); 
        };  
    };  
	
	//初始化View
	private void initView(View v) {
		layout_phone_number_regist = (RelativeLayout) v.findViewById(R.id.layout_phone_number_regist);
		layout_email_address_regist = (RelativeLayout) v.findViewById(R.id.layout_email_address_regist);
		layout_phone_number_regist_txtview = (TextView) v.findViewById(R.id.layout_phone_number_regist_txtview);
		layout_email_address_regist_txtview = (TextView) v.findViewById(R.id.layout_email_address_regist_txtview);
		layout_phone_number_regist_view = (View) v.findViewById(R.id.layout_phone_number_regist_view);
		layout_email_address_regist_view = (View) v.findViewById(R.id.layout_email_address_regist_view);

		user_regist_get_authcode_textview = (TextView) v.findViewById(R.id.user_regist_get_authcode_textview);
		user_regist_get_authcode_line = (ImageView) v.findViewById(R.id.user_regist_get_authcode_line);
//		reback_login_textview = (TextView) v.findViewById(R.id.reback_login_page);
		regist_email = (EditText) v.findViewById(R.id.regist_email);
		regist_name = (EditText) v.findViewById(R.id.regist_name);
		regist_pwd1 = (EditText) v.findViewById(R.id.regist_pwd1);
		// regist_pwd2 = (EditText)v.findViewById(R.id.regist_pwd2);
		btn_regist = v.findViewById(R.id.user_regist);

		name_left_img = (ImageView) v.findViewById(R.id.name_left_line);
		name_right_img = (ImageView) v.findViewById(R.id.name_right_line);
		name_mid_img = (ImageView) v.findViewById(R.id.name_mid_line);
		email_left_img = (ImageView) v.findViewById(R.id.email_left_line);
		email_right_img = (ImageView) v.findViewById(R.id.email_right_line);
		email_mid_img = (ImageView) v.findViewById(R.id.email_mid_line);
		pass_left_img = (ImageView) v.findViewById(R.id.pass_left_line);
		pass_right_img = (ImageView) v.findViewById(R.id.pass_right_line);
		pass_mid_img = (ImageView) v.findViewById(R.id.pass_mid_line);

		register_email_tip = (TextView) v.findViewById(R.id.regist_tip_email);
		register_pwd1_tip = (TextView) v.findViewById(R.id.regist_tip_pwd);
		register_name_tip = (TextView) v.findViewById(R.id.regist_tip_name);

		copyright = (TextView) v.findViewById(R.id.copyright);
		agreement = (TextView) v.findViewById(R.id.agreement);
		mCountTimerTextView = new CountTimerTextView(60000,1000);
		showDifferentView();
	}
	
	
	public TextView getAutoCodeText(){
		return user_regist_get_authcode_textview;
	}
	public EditText getEmailEditText(){
		return regist_email;
	}
	
	//显示不同的UI布局
	public void showDifferentView(){
		if (isPhoneNumberRegist) {
			if(layout_email_address_regist_txtview.isSelected()){
				showEmailAddressRegistUI();
			}else{
				showPhoneNumberRegistUI();
			}
		}else{
			showEmailAddressRegistUI();
		}
		
	}
	
	/**
	 * 设置颜色 正确是绿色，错误是红色
	 * 
	 * @param Correct
	 *            昵称是否正确
	 */
	private void setNameColor(boolean Correct) {
		if(null==getContext()){
			return;
		}
		try {
			if (Correct) {
				regist_name.setTextColor(getContext().getResources().getColor(R.color.text_color_gray_7));
				name_left_img.setBackgroundColor(getContext().getResources().getColor(R.color.login_n_register_line_correct));
				name_right_img.setBackgroundColor(getContext().getResources().getColor(R.color.login_n_register_line_correct));
				name_mid_img.setBackgroundColor(getContext().getResources().getColor(R.color.login_n_register_line_correct));
				namevalidate = true;
			} else {
				regist_name.setTextColor(getContext().getResources().getColor(R.color.text_color_red_1));
				name_left_img.setBackgroundColor(getContext().getResources().getColor(R.color.login_n_register_line_wrong));
				name_right_img.setBackgroundColor(getContext().getResources().getColor(R.color.login_n_register_line_wrong));
				name_mid_img.setBackgroundColor(getContext().getResources().getColor(R.color.login_n_register_line_wrong));
				namevalidate = false;
			}
		} catch (NullPointerException e) {
			Logger.d("RegisterFragment", "setNameColor--NullPointerException");
		}
	}

	
	/**
	 * 设置颜色 正确是绿色，错误是红色
	 * 
	 * @param Correct
	 *            邮箱是否正确
	 */
	private void setEmailColor(boolean Correct) {
		if(null==getContext()){
			return;
		}
		try {
			if (Correct) {
				email_left_img.setBackgroundColor(getContext().getResources().getColor(R.color.login_n_register_line_correct));
				email_right_img.setBackgroundColor(getContext().getResources().getColor(R.color.login_n_register_line_correct));
				email_mid_img.setBackgroundColor(getContext().getResources().getColor(R.color.login_n_register_line_correct));
				regist_email.setTextColor(getContext().getResources().getColor(R.color.text_color_gray_7));
				user_regist_get_authcode_line.setBackgroundColor(getContext().getResources().getColor(R.color.login_n_register_line_correct));
				emailvalidate = true;
			} else {
				regist_email.setTextColor(getContext().getResources().getColor(R.color.text_color_red_1));
				email_left_img.setBackgroundColor(getContext().getResources().getColor(R.color.login_n_register_line_wrong));
				email_right_img.setBackgroundColor(getContext().getResources().getColor(R.color.login_n_register_line_wrong));
				email_mid_img.setBackgroundColor(getContext().getResources().getColor(R.color.login_n_register_line_wrong));
				user_regist_get_authcode_line.setBackgroundColor(getContext().getResources().getColor(R.color.login_n_register_line_wrong));
				emailvalidate = false;
			}
		} catch (NullPointerException e) {
			Logger.d("RegisterFragment", "setEmailColor--NullPointerException");
		}
	}
	
	/**
	 * 设置颜色 正确是绿色，错误是红色
	 * 
	 * @param Correct
	 *            密码是否正确
	 */
	private void setPassColor(boolean Correct) {
		if(null==getContext()){
			return;
		}
		try {
			if (Correct) {
				regist_pwd1.setTextColor(getContext().getResources().getColor(R.color.text_color_gray_7));
				pass_left_img.setBackgroundColor(getContext().getResources().getColor(R.color.login_n_register_line_correct));
				pass_right_img.setBackgroundColor(getContext().getResources().getColor(R.color.login_n_register_line_correct));
				pass_mid_img.setBackgroundColor(getContext().getResources().getColor(R.color.login_n_register_line_correct));
				passvalidate = true;
			} else {
				regist_pwd1.setTextColor(getContext().getResources().getColor(R.color.text_color_red_1));
				pass_left_img.setBackgroundColor(getContext().getResources().getColor(R.color.login_n_register_line_wrong));
				pass_right_img.setBackgroundColor(getContext().getResources().getColor(R.color.login_n_register_line_wrong));
				pass_mid_img.setBackgroundColor(getContext().getResources().getColor(R.color.login_n_register_line_wrong));
				passvalidate = false;
			}
		} catch (NullPointerException e) {
			Logger.d("RegisterFragment", "setPassColor--NullPointerException");
		}
	}

	
	// 显示手机号码注册时候的UI
	private void showPhoneNumberRegistUI() {
		isPhoneNumberRegist = true;
		initRegistUI();
		regist_email.setHint(getContext().getString(R.string.tips_please_input_phone_number));
		regist_name.setHint(getContext().getString(R.string.tips_write_auth_code_by_phone_number));
		layout_phone_number_regist_txtview.setSelected(true);
		layout_email_address_regist_txtview.setSelected(false);
		user_regist_get_authcode_textview.setVisibility(View.VISIBLE);
		user_regist_get_authcode_line.setVisibility(View.VISIBLE);
		layout_phone_number_regist_view.setVisibility(View.VISIBLE);
	}

	// 显示邮箱注册时候的UI
	private void showEmailAddressRegistUI() {
		isPhoneNumberRegist = false;
		initRegistUI();
		regist_email.setHint(getContext().getString(R.string.regist_user_email_hint));
		regist_name.setHint(getContext().getString(R.string.regist_user_name_hint));
		layout_email_address_regist_txtview.setSelected(true);
		layout_phone_number_regist_txtview.setSelected(false);
		user_regist_get_authcode_textview.setVisibility(View.GONE);
		user_regist_get_authcode_line.setVisibility(View.GONE);
		layout_email_address_regist_view.setVisibility(View.VISIBLE);
	}

	//初始化UI
	private void initRegistUI(){
		if(null!=mCountTimerTextView&&isCountTimer){
			mCountTimerTextView.cancel();
			mCountTimerTextView.onFinish();
			isCountTimer = false;
		}
		regist_email.setText("");
		regist_name.setText("");
		regist_pwd1.setText("");
		setEmailColor(true);
		setPassColor(true);
		setNameColor(true);
		register_email_tip.setText("");
		register_name_tip.setText("");
		register_pwd1_tip.setText("");
		layout_email_address_regist_view.setVisibility(View.GONE);
		layout_phone_number_regist_view.setVisibility(View.GONE);
	}
	
	//清除手机号码的焦点
	public void clearAllFocus(){
		clearChildFocus(regist_email);
		clearChildFocus(regist_name);
		clearChildFocus(regist_pwd1);
		initRegistUI();
	}
	
//	public void setAllViewFocusabInTouch(){
//		setFocusableInTouchMode(true);
//		regist_email.setFocusableInTouchMode(true);
//		regist_name.setFocusableInTouchMode(true);
//		regist_pwd1.setFocusableInTouchMode(true);
//	}
//	
//	
	// 检测手机号码是否正确
	private boolean checkPhoneNumber(EditText mEditText,boolean passvalue) {
		String email = mEditText.getText().toString();
		// email为空
		if (email == null || "".equals(email)) {
			setEmailColor(false);
			YoukuUtil.showTips(getContext().getString(R.string.tips_phone_number_not_fit_rule));
//			register_email_tip.setText(getContext().getString(R.string.tips_phone_number_not_fit_rule));
			passvalue = false;
		} else if (!isMobileNO(email)) {// email不符合规则
			setEmailColor(false);
			YoukuUtil.showTips(getContext().getString(R.string.tips_phone_number_not_fit_rule));
//			register_email_tip.setText(getContext().getString(R.string.tips_phone_number_not_fit_rule));
			passvalue = false;
		}else {
			passvalue = true;
			setEmailColor(true);
			register_email_tip.setText(" ");
		}
		return passvalue;
	}
	
	// 检测手机验证码格式是否正确
	private boolean checkPhoneAuthCode(EditText mEditText,boolean passvalue) {
		String name = mEditText.getText().toString();
		// 名字为空
		if (name == null || "".equals(name)) {
			setNameColor(false);
			YoukuUtil.showTips(getContext().getString(R.string.tips_auth_code_cannot_empty));
//			register_name_tip.setText(getContext().getString(R.string.tips_auth_code_cannot_empty));
			passvalue = false;
		} else if (!isMobileAuthCode(name)) {// 用户名不和规则
			setNameColor(false);
			YoukuUtil.showTips(getContext().getString(R.string.tips_auth_code_not_fit_rule));
//			register_name_tip.setText(getContext().getString(R.string.tips_auth_code_not_fit_rule));
			passvalue = false;
		} else {
			setNameColor(true);
			register_name_tip.setText(" ");
			passvalue = true;
		}
		return passvalue;
	}

	/**
	 * 密码，昵称，邮箱，验证码都是失去焦点就进行本地判断 如果点击注册并且邮箱密码验证码都通过了本地验证，才会去和服务器交互
	 */
	public void AddListener() {
		// 手机号码注册
		layout_phone_number_regist.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showPhoneNumberRegistUI();
			}
		});

		// 邮箱地址注册
		layout_email_address_regist.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showEmailAddressRegistUI();
			}
		});

		// 用户发送手机验证码
		user_regist_get_authcode_textview.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!YoukuUtil.hasInternet()) {
					YoukuUtil.showTips(getContext().getString(R.string.tips_no_network));
					return;
				}
				if(isCountTimer){
					return;
				}
				if (isPhoneNumberRegist&&checkPhoneNumber(regist_email,false)) {
					doGetPhoneNumberAuthCode(regist_email.getText().toString());
				}
			}
		});

//		((LinearLayout) reback_login_textview.getParent()).setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				reback_login_textview.performClick();
//			}
//		});
//		reback_login_textview.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				handler.sendEmptyMessage(LoginRegistCardViewDialogActivity.GO_LOGIN);
//			}
//		});

//		regist_email.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
////				Message message = new Message();
////				message.obj = regist_email; 
////				handerSoftWare.sendMessageDelayed(message, 500);
////				showSoftWare(regist_email);
//				Logger.lxf("====注册用户名被点击了============");
//			}
//		});
//		
		regist_email.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					String email = regist_email.getText().toString().trim();
					if (!isPhoneNumberRegist) {
						// email为空
						if (email == null || "".equals(email)) {
							setEmailColor(false);
							YoukuUtil.showTips(getContext().getString(R.string.regist_user_email_tip_empty));
//							register_email_tip.setText(R.string.regist_user_email_tip_empty);
						} else if (!checkEmail(email)) {// email不符合规则
							setEmailColor(false);
							YoukuUtil.showTips(getContext().getString(R.string.regist_user_email_tip));
//							register_email_tip.setText(R.string.regist_user_email_tip);
						} else if (!"".equals(emailRecord) && emailRecord.equals(email)) {
							;// 如果此项的内容较上次没有改变，状态就不变了（此用户名已注册的错误）
						} else {
							setEmailColor(true);
							register_email_tip.setText(" ");
						}
					} else {
						checkPhoneNumber(regist_email,false);
					}
				}
			}
		});

		regist_pwd1.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					regist_pwd1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);// 暗码
					String pwd = regist_pwd1.getText().toString().trim();
					// 密码为空
					if (pwd == null || "".equals(pwd)) {
						setPassColor(false);
						YoukuUtil.showTips(getContext().getString(R.string.regist_user_pwd1_tip_empty));
//						register_pwd1_tip.setText(R.string.regist_user_pwd1_tip_empty);
					} else if (!checkPassword(pwd, PWD_MIN_LENGTH, PWD_MAX_LENGTH)) {// 密码不符合规则
						setPassColor(false);
						YoukuUtil.showTips(getContext().getString(R.string.regist_user_pwd1_tip));
//						register_pwd1_tip.setText(R.string.regist_user_pwd1_tip);
					} else {
						setPassColor(true);
						register_pwd1_tip.setText(" ");
					}
				} else {
					regist_pwd1.setInputType(InputType.TYPE_CLASS_TEXT);// 明码
				}
			}
		});

		regist_name.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					String name = regist_name.getText().toString().trim();
					if (!isPhoneNumberRegist) {
						// 名字为空
						if (name == null || "".equals(name)) {
							setNameColor(false);
							YoukuUtil.showTips(getContext().getString(R.string.regist_user_name_tip_empty));
//							register_name_tip.setText(R.string.regist_user_name_tip_empty);
						} else if (!checkUserNickName(name, NICK_NAME_MIN_LENGTH, NICK_NAME_MAX_LENGTH)) {// 用户名不和规则
							setNameColor(false);
							YoukuUtil.showTips(getContext().getString(R.string.regist_user_name_tip));
//							register_name_tip.setText(R.string.regist_user_name_tip);
						} else if (!"".equals(nameRecord) && nameRecord.equals(name)) {
							;// 如果此项的内容较上次没有改变，状态就不变了（此用户名已注册的错误）
						} else {
							setNameColor(true);
							register_name_tip.setText(" ");
						}
					} else {
						checkPhoneAuthCode(regist_name,false);
					}
				}
			}
		});

		/**
		 * 当焦点在email上的时候，软键盘的回车功能是下一个
		 */
		regist_email.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_NEXT) {
					regist_name.requestFocus();
				}
				return false;
			}
		});

		/**
		 * 当焦点在昵称上的时候，软键盘的回车功能是下一个
		 */
		regist_name.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_NEXT) {
					regist_pwd1.requestFocus();
				}
				return false;
			}
		});

		/**
		 * 当焦点在email上的时候，软键盘的回车功能是下一个 有些机型键盘不可以变为下一个，仍然响应的是回车
		 */
		regist_email.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER) {
					if (event.getAction() == KeyEvent.ACTION_UP)
						regist_name.requestFocus();
					return true;
				}
				return false;
			}
		});
		// 如果内容变化过，那么就设置成正确的样式
		regist_email.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				setEmailColor(true);
				register_email_tip.setText(" ");
			}
		});
		// 如果内容变化过，那么就设置成正确的样式
		regist_name.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				setNameColor(true);
				register_name_tip.setText(" ");
			}
		});
		/**
		 * 当焦点在昵称上的时候，软键盘的回车功能是下一个 有些机型键盘不可以变为下一个，仍然响应的是回车
		 */
		regist_name.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER) {
					if (event.getAction() == KeyEvent.ACTION_UP)
						regist_pwd1.requestFocus();
					return true;
				}
				return false;
			}
		});

		/**
		 * 当焦点在密码框上的时候，软键盘的完成功能为点击登录
		 */
		regist_pwd1.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER) {
					InputMethodManager imm = (InputMethodManager) Youku.mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
					if (imm.isActive()) {
						imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
					}
					if (event.getAction() == KeyEvent.ACTION_UP)
						btn_regist.performClick();
					return true;
				}
				return false;
			}
		});

		/**
		 * 当焦点在密码框上的时候，软键盘的完成功能为点击登录
		 */
		regist_pwd1.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					InputMethodManager imm = (InputMethodManager) Youku.mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
					if (imm.isActive()) {
						imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
					}
					btn_regist.performClick();
				}
				return false;
			}
		});

		// 注册按钮的点击
		btn_regist.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!YoukuUtil.hasInternet()) {// 无网提示
					YoukuUtil.showTips(getContext().getString(R.string.tips_no_network));
					return;
				}
				String email = regist_email.getText().toString().trim();
				String username = regist_name.getText().toString().trim();
				String pwd1 = regist_pwd1.getText().toString().trim();
				// String pwd2 = regist_pwd2.getText().toString().trim();
				boolean pass = true;
				if(!isPhoneNumberRegist){
					// 邮箱地址验证
					if (email == null || "".equals(email)) {
						YoukuUtil.showTips(getContext().getString(R.string.regist_user_email_tip_empty));
//						register_email_tip.setText(R.string.regist_user_email_tip_empty);
						pass = false;
						setEmailColor(false);
						// return;
					} else if (!checkEmail(email)) {
						YoukuUtil.showTips(getContext().getString(R.string.regist_user_email_tip));
//						register_email_tip.setText(R.string.regist_user_email_tip);
						pass = false;
						setEmailColor(false);
						// return;
					}
					
					// 用户名验证
					if (username == null || "".equals(username)) {
						YoukuUtil.showTips(getContext().getString(R.string.regist_user_name_tip_empty));
//						register_name_tip.setText(R.string.regist_user_name_tip_empty);
						pass = false;
						setNameColor(false);
						// return;
					} else if (!checkUserNickName(username, NICK_NAME_MIN_LENGTH, NICK_NAME_MAX_LENGTH)) {
						YoukuUtil.showTips(getContext().getString(R.string.regist_user_name_tip));
//						register_name_tip.setText(R.string.regist_user_name_tip);
						pass = false;
						setNameColor(false);
						// return;
					}
				}else{
					pass = checkPhoneNumber(regist_email,pass);
					if(!pass)return;
					pass = checkPhoneAuthCode(regist_name,pass);
					if(!pass)return;
				}

				// 密码验证
				if (pwd1 == null || "".equals(pwd1)) {
					YoukuUtil.showTips(getContext().getString(R.string.regist_user_pwd1_tip_empty));
//					register_pwd1_tip.setText(R.string.regist_user_pwd1_tip_empty);
					pass = false;
					setPassColor(false);
					// return;
				} else if (!checkPassword(pwd1, PWD_MIN_LENGTH, PWD_MAX_LENGTH)) {
					YoukuUtil.showTips(getContext().getString(R.string.regist_user_pwd1_tip));
//					register_pwd1_tip.setText(R.string.regist_user_pwd1_tip);
					pass = false;
					setPassColor(false);
					// return;
				}
				if (pass) {
					emailRecord = email;
					nameRecord = username;
					if(!isPhoneNumberRegist){
						doRegist(username, pwd1, email);
					}else{
						doRegistByPhoneNumber(emailRecord, nameRecord, pwd1);
					}
				}
			}
		});
		copyright.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				YoukuUtil.goWebView(getContext(), COPYRIGHT_URL);
			}
		});
		agreement.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				YoukuUtil.goWebView(getContext(), AGREEMENT_URL);
			}
		});

	}
	
	@SuppressLint("HandlerLeak")
	private class RegistHandler extends Handler{

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case LoginRegistCardViewDialogActivity.REGIST_SUCCESS:
				YoukuUtil.showTips(getContext().getString(R.string.register_success));
				YoukuLoading.dismiss();
				finishSelf();
				Logger.lxf("======REGIST_SUCCESS===========");
				break;
			case LoginRegistCardViewDialogActivity.REGIST_FAIL:
				YoukuLoading.dismiss();
				Logger.lxf("======REGIST_FAIL===========");
				break;
			default:
				break;
			}
			
			
		}
		
	}
	
	private void finishSelf(){
		removeAllViews();
		setVisibility(View.GONE);
		((Activity)getContext()).finish();
	}
	
	private void doRegist(final String username, final String password, final String email) {

		Logger.d("Youku", "doRegist name = " + username + " pwd = " + password + " email " + email);
		if (isregist) {// 防止用户不停点击注册按钮
			return;
		}
		isregist = true;
		YoukuLoading.show(getContext());
		ILogin ilogin = YoukuService.getService(ILogin.class, true);
		ilogin.register(username, password, email, new ICallBack() {
 
			@Override
			public void onSuccess() {
				Logger.d("Youku", "doRegist onSuccess");
				registHandler.sendEmptyMessage(LoginRegistCardViewDialogActivity.REGIST_SUCCESS);
				isregist = false;
			}

			@Override
			public void onFailed(LoginException e) {
				// bit_captcha_wrong= 1<<0 #验证码错误
				// bit_captcha_timeout=1<<1 #验证码超时
				// bit_auth_error = 1<<2 #认证错误
				// bit_username_exists = 1<<3 #用户已经存在
				// bit_username_invalid = 1<<4 #用户名不可用
				// bit_email_exists = 1<<5 #email已经存在
				// bit_email_invalid = 1<<6 #email非法
				// bit_unkonw_error = 1<<7 # 未知错误
				Logger.d("Youku", "doRegist onFailed code = " + e.getErrorCode() + "info = " + e.getErrorInfo());
				isregist = false;
				final int result = e.getErrorCode();
				int focus = 0;// 记录验证完成的焦点在哪
				if (result == -1) {
					YoukuUtil.showTips(getContext().getString(R.string.login_error_unknown));
				} else if (result == R.string.tips_not_responding) {
					YoukuUtil.showTips(getContext().getString(R.string.tips_not_responding));
				} else {
					Logger.d("Youku","result & bit_auth_error: " + (bit_auth_error));
					Logger.d("Youku","result & bit_username_exists: " + (bit_username_exists));
					Logger.d("Youku","result & bit_username_invalid: " + (bit_username_invalid));
					Logger.d("Youku","result & bit_email_exists: " + (bit_email_exists));
					Logger.d("Youku","result & bit_email_invalid: " + (bit_email_invalid));
					
					if ((result & bit_auth_error) == bit_auth_error) { // 认证错误
						YoukuUtil.showTips(getContext().getString(R.string.login_error_unknown));
					}
					if ((result & bit_username_exists) == bit_username_exists) { // 用户已经存在
						setNameColor(false);
						YoukuUtil.showTips(getContext().getString(R.string.regist_user_name_tip_exists));
//						register_name_tip.setText(R.string.regist_user_name_tip_exists);
						focus = 2;
					}
					if ((result & bit_username_invalid) == bit_username_invalid) { // 用户名不可用
						setNameColor(false);
						YoukuUtil.showTips(getContext().getString(R.string.regist_user_name_tip));
//						register_name_tip.setText(R.string.regist_user_name_tip);
						focus = 2;
					}
					if ((result & bit_email_exists) == bit_email_exists) { // email已经存在
						setEmailColor(false);
						YoukuUtil.showTips(getContext().getString(R.string.regist_user_email_tip_exists));
//						register_email_tip.setText(R.string.regist_user_email_tip_exists);
						focus = 3;
					}
					if ((result & bit_email_invalid) == bit_email_invalid) { // email非法
						setEmailColor(false);
						YoukuUtil.showTips(getContext().getString(R.string.user_login_error_wrong_email));
//						register_email_tip.setText(R.string.user_login_error_wrong_email);
						focus = 3;
					}
				}
				registHandler.sendEmptyMessage(LoginRegistCardViewDialogActivity.REGIST_FAIL);
				if (focus == 3) {// 焦点在邮箱
					regist_email.requestFocus();
				} else if (focus == 2) {// 焦点在名称
					regist_name.requestFocus();
				}
			}
		});
	}
	
	
//	@Override
//	protected void onDraw(Canvas canvas) {
//		super.onDraw(canvas);
//	}
//
//	@Override
//	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//	}
//
//	@Override
//	protected void onLayout(boolean changed, int l, int t, int r, int b) {
//		super.onLayout(changed, l, t, r, b);
//		showSoftWare(regist_email);
//	}
//	
	// 获得手机验证码
	private void doGetPhoneNumberAuthCode(String mobileStr) {
		HttpRequestManager httpRequestManager = new HttpRequestManager();
		httpRequestManager.setParseErrorCode(true);
		httpRequestManager.request(new HttpIntent(URLContainer.getPhoneAthoriteCodeUrl(mobileStr), HttpRequestManager.METHOD_POST, true), new IHttpRequestCallBack() {
			@Override
			public void onSuccess(HttpRequestManager httpRequestManager) {
				String result = httpRequestManager.getDataString();
				Logger.lxf("=====success====" + result);
				JSONObject obj;
				try {
					obj = new JSONObject(result);
					String status = obj.optString("status");
					int state_code = obj.optInt("code");
					if (!status.equals("success")) {
						if(status.equals("error")){
							int state_code_value = obj.optInt("code");
							ErrorCodeUtil.getInstance().showErrorMessage4PhoneRegist(""+state_code_value);
						}
						return ;
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				YoukuUtil.showTips(getContext().getString(R.string.tips_auth_code_has_been_send));
				YoukuLoading.dismiss();
				mCountTimerTextView.start();
			}

			@Override
			public void onFailed(String failReason) {
				Logger.lxf("=====failReason====" + failReason);
				ErrorCodeUtil.getInstance().showErrorMessage4PhoneRegist(failReason);
				YoukuUtil.showTips(getContext().getString(R.string.tips_auth_code_fail_send));
				YoukuLoading.dismiss();
			}
		});
	}
	
	/**
	 * 通过手机号码进行注册
	 * @param mobileStr
	 * @param codeStr
	 * @param passwordStr
	 */
	private void doRegistByPhoneNumber(final String mobileStr,String codeStr,final String passwordStr) {
		HttpRequestManager httpRequestManager = new HttpRequestManager();
		httpRequestManager.setParseErrorCode(true);
		httpRequestManager.setSaveCookie(true);
		httpRequestManager.request(new HttpIntent(URLContainer.getPhoneRegistLoginUrl(mobileStr, codeStr, passwordStr), HttpRequestManager.METHOD_POST, true), new IHttpRequestCallBack() {
			@Override
			public void onSuccess(HttpRequestManager httpRequestManager) {
				String result = httpRequestManager.getDataString();
				Logger.lxf("=====success===result====" + result);
				JSONObject obj;
				try {
					obj = new JSONObject(result);
					String status = obj.optString("status");
					int state_code = obj.optInt("code");
					if (!status.equals("success")) {
						if(status.equals("error")){
							int state_code_value = obj.optInt("code");
							ErrorCodeUtil.getInstance().showErrorMessage4LoginRegist(""+state_code_value);
						}
						return ;
					}
					// Logger.lxf("==注册成功后返回的数据==json="+obj);
					obj = obj.getJSONObject("results");
					Youku.userName = obj.optString("username");
					String uid = obj.optString("userid");
					String userIcon = obj.optString("icon_large");
					Youku.isLogined = true;
					Youku.loginAccount = mobileStr;
					Youku.savePreference("loginAccount", Youku.loginAccount);
					Youku.savePreference("loginPassword",
							YoukuUtil.md5(passwordStr));
					Youku.savePreference("isNotAutoLogin", false);
					Youku.savePreference("isLogined", true);
					Youku.savePreference("cookie", Youku.COOKIE);
					Youku.savePreference("userName", Youku.userName);
					Youku.savePreference("uid", uid);
					Youku.savePreference("userIcon", userIcon);
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				registHandler.sendEmptyMessage(LoginRegistCardViewDialogActivity.REGIST_SUCCESS);
				isregist = false;
				YoukuLoading.dismiss();
			}
			
			@Override
			public void onFailed(String failReason) {
				Logger.lxf("=====failReason====" + failReason);
				ErrorCodeUtil.getInstance().showErrorMessage4LoginRegist(failReason);
				YoukuLoading.dismiss();
			}
		});
	}
	
	/**
	 * 验证手机格式
	 */
	public static boolean isMobileNO(String mobiles) {
		/*
		 * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		 * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
		 * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
		 */
		String telRegex = "[1][358]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
		if (TextUtils.isEmpty(mobiles))
			return false;
		else
			return mobiles.matches(telRegex);
	}
	/**
	 * 验证手机验证码
	 */
	public static boolean isMobileAuthCode(String mobiles) {
		String telRegex = "[0-9]{6}";// 
		if (TextUtils.isEmpty(mobiles))
			return false;
		else
			return mobiles.matches(telRegex);
	}

	/**
	 * email的检查 xx.xx@xxx.xxx或者 xxx@xx.xxx
	 * 
	 * @param line
	 * @return
	 */
	private boolean checkEmail(String line) {
		if (getChineseCharCount(line) != 0) {
			return false;
		}
		// Pattern p = Pattern.compile("\\w|\\.+@(\\w+\\.)+[a-z]{2,3}");
		Pattern p = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
		Matcher m = p.matcher(line);
		boolean b = m.matches();
		return b;
	}

	/**
	 * 密码不能为中文, 不能有空格,此类中为6-16位，参数参考 LoginRegistCardViewDialogActivity
	 * 
	 * @param line
	 * @param minlength
	 *            最短
	 * @param maxlength
	 *            最长
	 * @return
	 */
	private boolean checkPassword(String line, int minlength, int maxlength) {
		if (line.contains(" ")) {
			return false;
		}
		Pattern p = Pattern.compile("[\\u4e00-\\u9fa5]");
		Matcher m = p.matcher(line);
		return !m.find() && line.length() <= maxlength && line.length() >= minlength;
	}

	/**
	 * \\u3400-\\u9FBF 昵称 2-15个汉字或4-30个字符（中文，字母，数字，下划线，减好）
	 * 
	 * @param line
	 *            输入
	 * @param minlength
	 *            最少几个字符
	 * @param maxlength
	 *            最多几个字符
	 * @return
	 */
	private boolean checkUserNickName(String line, int minlength, int maxlength) {
		if (line.contains(" ")) {
			return false;
		}
		Pattern p = Pattern.compile("[^0-9a-zA-Z_\u4e00-\u9fa5\\-]");
		Matcher m = p.matcher(line);
		if (!m.find()) {
			// Pattern pa = Pattern.compile("[\\u4e00-\\u9fa5]");
			// Matcher ma = p.matcher(line);
			int count = getChineseCharCount(line);
			int length = line.length();
			if (count * 2 + (length - count) >= minlength && count * 2 + (length - count) <= maxlength)
				return true;
			else
				return false;
		} else {
			return false;
		}
	}

	/**
	 * 返回有多少个汉字
	 * 
	 * @param str
	 * @return
	 */
	private int getChineseCharCount(String str) {
		String tempStr;
		int count = 0;
		for (int i = 0; i < str.length(); i++) {
			tempStr = String.valueOf(str.charAt(i));
			if (tempStr.getBytes().length == 3) {
				count++;
			}
		}
		return count;
	}
	
	boolean isCountTimer = false;
	//倒计时用的计时器
	public class CountTimerTextView extends CountDownTimer {  
		
        public CountTimerTextView(long millisInFuture, long countDownInterval) {  
            super(millisInFuture, countDownInterval);  
        }  
  
        @Override  
        public void onFinish() { 
        	isCountTimer = false;
        	user_regist_get_authcode_textview.setText("发送验证码");
        	if(null!=getContext()){
        		user_regist_get_authcode_textview.setTextColor(getContext().getResources().getColor(R.color.login_regist_default_light_bule));
        	}
        }  
  
        @Override  
        public void onTick(long millisUntilFinished) {
        	isCountTimer = true;
        	if(null!=getContext()){
        		user_regist_get_authcode_textview.setTextColor(getContext().getResources().getColor(R.color.black_gray));
        	}
        	user_regist_get_authcode_textview.setText("60秒倒计时 (" + millisUntilFinished / 1000 + ")");  
        }  
  
    }  
	
}
