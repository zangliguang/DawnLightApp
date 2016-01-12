package com.youku.login.widget;


import com.example.youkuloginsdk.R;
import com.youku.login.Zxing.CaptureActivity;
import com.youku.login.activity.LoginRegistCardViewDialogActivity;
import com.youku.login.util.Logger;
import com.youku.login.util.YoukuUtil;
import com.youku.login.widget.YoukuPopupMenu.OnItemSelectedListener;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

public class CardLoginRegistView extends LinearLayout {

	public static final int UI_NORMAL_LOGIN = 10;//显示注册，关闭，扫一扫item
	public static final int UI_DETAIL_LOGIN = 20;//显示注册，扫一扫
	public static final int UI_VIP_LOGIN = 30;//显示注册，关闭
	public static final int UI_VIP_PORTAIN_LOGIN = 40;//显示注册
	
	private CardLoginUIView loginUICardView;
	private CardRegistUIView registCardView;
	private View login_more;
	private TextView reback_login_textview;
	private PopupMenu mPopupMenu;
	private YoukuPopupMenu mYoukuPopupMenu;
	private LinearLayout child_view_total;
	private int typeScale = -1;
	
	public CardLoginRegistView(Context context, AttributeSet attrs) {
		super(context, attrs);
		typeScale = attrs.getAttributeIntValue(context.getString(R.string.current_namespace), "type", 10);
		initViewLayout();
	}
	
	
	private void initViewLayout(){
		View view = LayoutInflater.from(getContext()).inflate(R.layout.card_view_login_regist_layout, null);
		initView(view);
		addView(view);
	}
	
	private void initView(View view){
		
		child_view_total = (LinearLayout)view.findViewById(R.id.child_view_total);
		child_view_total.addView(getLoginUIView());
//		Logger.lxf("====typeScale======"+typeScale);
//		Logger.lxf("====isNormalScale()======"+isShowScanView());
		if(isShowScanView()){
			loginUICardView.getSNSLineaLayout().setVisibility(View.VISIBLE);
		}else{
			loginUICardView.getSNSLineaLayout().setVisibility(View.GONE);
		}
	}
	
	
	private View getLoginUIView(){
		loginUICardView = new CardLoginUIView(getContext(), null);
		login_more = loginUICardView.findViewById(R.id.login_more);
		setOnclickEvent();
		return loginUICardView;
	}
	
	private View getRegistUIView(){
		registCardView = new CardRegistUIView(getContext(), null);
		reback_login_textview = (TextView)registCardView.findViewById(R.id.reback_login_page);
		setRegistOnClickListener();
		return registCardView;
	}
	
	
	/**
	 * 是否显示扫一扫按钮
	 * @return
	 */
	private boolean isShowScanView(){
		return typeScale == UI_NORMAL_LOGIN||typeScale == UI_DETAIL_LOGIN;
	}
	
	
	private void setOnclickEvent(){
		loginUICardView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
		setMoreMenu(login_more);
	}
	
	
	private void setRegistOnClickListener(){
		((LinearLayout) reback_login_textview.getParent()).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				reback_login_textview.performClick();
			}
		});
		reback_login_textview.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Logger.lxf("=======返回按钮========");
				handler.sendEmptyMessage(LoginRegistCardViewDialogActivity.GO_LOGIN);
			}
		});
	}
	
	private static long time1 = 0l;
	private static long time2 = 0l;

	@SuppressLint("NewApi")
	private void setMoreMenu(final View view) {
		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				long now = System.currentTimeMillis();
				if ((now - time2) < 500l) {
					return;
				} else {
					time2 = now;
				}
//				if (android.os.Build.VERSION.SDK_INT <= 13) {
//					mPopupMenu = new PopupMenu(getContext(), view);
//					MenuInflater inflater = mPopupMenu.getMenuInflater();
//					inflater.inflate(R.menu.login_moremenu, mPopupMenu.getMenu());
//					if(isShowScanView()){
//						if(typeScale == UI_DETAIL_LOGIN){
//							mPopupMenu.getMenu().getItem(2).setVisible(false);
//						}
//					}else{
//						if(typeScale==UI_VIP_LOGIN){
//							mPopupMenu.getMenu().getItem(1).setVisible(false);
//						}else{
//							mPopupMenu.getMenu().getItem(1).setVisible(false);
//							mPopupMenu.getMenu().getItem(2).setVisible(false);
//						}
//					}
//					mPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//						@Override
//						public boolean onMenuItemClick(MenuItem item) {
//							long now = System.currentTimeMillis();
//							if ((now - time1) < 500l) {
//								return false;
//							} else {
//								time1 = now;
//							}
//							Message m = Message.obtain();
//							m.what = item.getItemId();
//							handler.sendMessage(m);
//							return true;
//						}
//					});
//					mPopupMenu.show();
//				} else {
					// Create Instance
					mYoukuPopupMenu = new YoukuPopupMenu(getContext());
					mYoukuPopupMenu.setWidth(205);
					mYoukuPopupMenu.add(0, R.string.register);
					if(isShowScanView()){
						if(typeScale == UI_DETAIL_LOGIN){
							mYoukuPopupMenu.add(1, R.string.saosao);
						}else{
							mYoukuPopupMenu.add(1, R.string.saosao);
//							mYoukuPopupMenu.add(2, R.string.close);
						}
					}else{
						if(typeScale==UI_VIP_LOGIN){
//							mYoukuPopupMenu.add(1, R.string.close);
						}
					}
					// Set Listener
					mYoukuPopupMenu.setOnItemSelectedListener(new OnItemSelectedListener() {

						@Override
						public void onItemSelected(
								YoukuPopupMenu.MenuItem item) {
							long now = System.currentTimeMillis();
							if ((now - time1) < 500l) {
								return;
							} else {
								time1 = now;
							}
							Message m = Message.obtain();
							m.what = item.getItemId();
							handler.sendMessage(m);
						}
					});
					// Add Menu (Android menu like style)
//					mYoukuPopupMenu.showDialogMenu(view);
					mYoukuPopupMenu.showAsDropDown(view,Gravity.RIGHT|Gravity.TOP);
//				}
			}
		});
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			int what = msg.what;
			if(what==0 || what==R.id.menu1){
				loginUICardView.clearAllFocus();
				child_view_total.removeAllViews();
				loginUICardView = null;
				child_view_total.addView(getRegistUIView());
				registCardView.showDifferentView();
			}else if(what==1 || what==R.id.menu2){
				Intent intent = new Intent(); 
				intent.setClass(getContext(), CaptureActivity.class);
				getContext().startActivity(intent);
			}else if(what==2 || what==R.id.menu3){
				YoukuUtil.showTips("被关闭");
			}else if(what==LoginRegistCardViewDialogActivity.GO_LOGIN){
				registCardView.clearAllFocus();
				child_view_total.removeAllViews();
				registCardView = null;
				child_view_total.addView(getLoginUIView());
			}
			
			/*switch (msg.what) {
			case 0:// 注册
			case R.id.menu1:// 去注册UI
				loginUICardView.clearAllFocus();
				child_view_total.removeAllViews();
				loginUICardView = null;
				child_view_total.addView(getRegistUIView());
				registCardView.showDifferentView();
				break;
			case 1:// 
			case R.id.menu2:// 
				Intent intent = new Intent(); 
				intent.setClass(getContext(), CaptureActivity.class);
				getContext().startActivity(intent);
				break;
			case 2:// 
			case R.id.menu3://
				YoukuUtil.showTips("被关闭");
				break;
			case LoginRegistCardViewDialogActivity.GO_LOGIN://去登录UI
				registCardView.clearAllFocus();
				child_view_total.removeAllViews();
				registCardView = null;
				child_view_total.addView(getLoginUIView());
				break;
			default:
				break;
			}*/
		}
	};
	
	
	
	
}
