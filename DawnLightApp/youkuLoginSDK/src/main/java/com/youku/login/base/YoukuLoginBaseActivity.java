package com.youku.login.base;


import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.youkuloginsdk.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.youku.login.Zxing.CaptureActivity;
import com.youku.login.util.Logger;

public abstract class YoukuLoginBaseActivity extends Activity{
	
	private ImageLoader mImageLoader;
	
	//public static int THEMEBACK = R.style.Theme_YoukuBack;
	public static int TAG_HomePageActivity = 0x80;
	public static int TAG_SearchActivity = 0x81;
	public int TAG_BaseActivity;
	//private ActionBar actionBar;
	private String key_currentString = "";
	public static final int TAG_CaptureActivity = 0x82;
	//public MenuService menuUtil;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setTheme(YoukuLoginBaseActivity.THEMEBACK);
//		actionBar = getSupportActionBar();
//		//actionBar.setDisplayShowHomeEnabled(true);
//		actionBar.setDisplayHomeAsUpEnabled(false);
	}
	
	@Override 
	public boolean onOptionsItemSelected(MenuItem item) {
		menuClick(item.getItemId());
		return super.onOptionsItemSelected(item); 
	}
 
	private boolean menuClick(int id){
		switch (id) {
			case android.R.id.home:
				Logger.e("NewDetailActivity", "goBack() onOptionsItemSelected");
				Logger.lxf("=====返回按钮被点击了=========");
				goBack();
				break;
		}
		return true;
	}
	
	public void goBack() {
		onGoToOtherActivity();
		key_currentString = "";
		if (TAG_BaseActivity != TAG_HomePageActivity) {
			Logger.lxf("=BaseActivity==onBackPressed()=========");
			super.onBackPressed(); 
		}
	} 
	
	/**
	 * 上传详情页面在p7500总是闪现软键盘 此处是处理跳转至其他页面会闪软件盘的问题
	 */
	public void onGoToOtherActivity() {
		Logger.lxf("=BaseActivity==onGoToOtherActivity()=========");
	}
	
	abstract public String getPageName();
	
	public ImageLoader getImageLoader() {
		return mImageLoader;
	}
	
	public void onMenuSaoSaoClick() {
		/*if (TAG_BaseActivity == TAG_CaptureActivity) {
			NEEDCloseSearch();
			return;
		}*/
		onGoToOtherActivity();
		Intent i = new Intent();
		i.setClass(this, CaptureActivity.class);
		this.startActivity(i);
	}
	
	public String getCustomTitleName() {
		return "";
	}
}
