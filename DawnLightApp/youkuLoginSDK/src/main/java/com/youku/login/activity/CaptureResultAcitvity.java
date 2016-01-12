package com.youku.login.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.youkuloginsdk.R;
import com.youku.login.base.YoukuLoginBaseActivity;
import com.youku.login.data.Saosao;
import com.youku.login.data.SaosaoResult;
import com.youku.login.network.HttpIntent;
import com.youku.login.network.HttpRequestManager;
import com.youku.login.network.IHttpRequest;
import com.youku.login.network.IHttpRequest.IHttpRequestCallBack;
import com.youku.login.network.URLContainer;
import com.youku.login.service.YoukuService;
import com.youku.login.statics.StaticsConfigFile;
import com.youku.login.util.DetailUtil;
import com.youku.login.util.Logger;
import com.youku.login.util.Youku;
import com.youku.login.util.YoukuUtil;
import com.youku.login.widget.YoukuLoading;

public class CaptureResultAcitvity extends YoukuLoginBaseActivity implements OnClickListener {

	private Button mResaosao;// 重新扫描按钮

	private View mNoResult;// 无结果页面

	private View mHasResult;// 有结果页面

	private View mNORight;// 无版权

	private TextView title;

	private TextView seeToo;

	private ImageView img;

	private Button play;

	private ImageView img_play;

	private Saosao saoJson;

	private int margin16 = 16;

	private int margin20 = 20;

	private int margin32 = 32;

	private int margin24 = 24;

	private int margin26 = 26;

	private int margin28 = 28;

	private int margin30 = 30;

	private int margin34 = 34;

	private int margin60 = 60;

	private int margin80 = 80;

	private int margin84 = 84;

	private int margin120 = 120;

	private int margin100 = 100;

	private int margin176 = 176;

	private int img_width = 716;

	private int img_hight = 400;

	private int btn_width;

	private int btn_hight;

	private int tishi_img_hight = 378;

	private int tishi_img_width = 524;

	private int img_play_width = 112;

	private int img_play_hight = 108;

	public final static int REQUESTCODE_CAPTURE_LOGIN = 1000;
	
	private final int REQUEST_TV_SCAN_CODE_LENGTH = 32;//扫描TV二维码之后获得的32位的结果

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		String url = getBundleValue(getIntent().getExtras(), "url", "", false);
		if (!TextUtils.isEmpty(url)&&REQUEST_TV_SCAN_CODE_LENGTH==url.length()&&!url.contains("http")) {
			setContentView(R.layout.activity_captureresult_tvlogin);
			View cancel = findViewById(R.id.layout_soku_dialog_cancel);
			cancel.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					finish();
				} 
			});
			View confrim = findViewById(R.id.layout_soku_dialog_goplay);
			confrim.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					toPullLogin();
				}
			});
		} else {
			setContentView(R.layout.activity_captureresult);
			initParm();
			initView();
			mResaosao.setOnClickListener(this);
			img_play.setOnClickListener(this);
			play.setOnClickListener(this);
			getVideo();
		}
	}

	@Override
	protected void onStart() {
		super.onStart();

	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == R.id.menu_saosao) {
			CaptureResultAcitvity.this.finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	} 

	private void initView() {
		setWhiteANDTextPrarms();
		mNORight = findViewById(R.id.no_right);
		mNORight.setPadding(16, margin32, 16, 16);
		mResaosao = (Button) findViewById(R.id.re_saosao);// 重新扫描
		mNoResult = findViewById(R.id.no_result);
		mHasResult = findViewById(R.id.has_result);
		title = (TextView) findViewById(R.id.title);
		seeToo = (TextView) findViewById(R.id.seeToo);
		img = (ImageView) findViewById(R.id.img);
		play = (Button) findViewById(R.id.play);
		img_play = (ImageView) findViewById(R.id.img_play);
		setTitleParams();
		setSeeTooParams();
		setBtnPlayParams();
		setBtnReSaoParams();
		setImgParams();
		setImg_playParams();
	}

	private void setBtnReSaoParams() {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(btn_width, btn_hight);
		params.gravity = Gravity.CENTER_HORIZONTAL;
		params.topMargin = margin60;
		mResaosao.setLayoutParams(params);
	}

	private void setBtnPlayParams() {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(btn_width, btn_hight);
		params.gravity = Gravity.CENTER_HORIZONTAL;
		play.setLayoutParams(params);
	}

	private void setWhiteANDTextPrarms() {
		// 适配
		LinearLayout has_result_white = (LinearLayout) findViewById(R.id.has_result_white);
		has_result_white.setPadding(16, margin32, 16, margin34);
		LinearLayout no_result_white = (LinearLayout) findViewById(R.id.no_result_white);
		no_result_white.setPadding(16, margin80, 16, margin80);
		ImageView no_result_img_tishi = (ImageView) findViewById(R.id.no_result_img_tishi);
		LinearLayout.LayoutParams no_result_img_tishi_params = new LinearLayout.LayoutParams(tishi_img_width, tishi_img_hight);
		no_result_img_tishi_params.leftMargin = margin100;
		no_result_img_tishi_params.rightMargin = margin176;
		no_result_img_tishi_params.topMargin = margin84;
		no_result_img_tishi.setLayoutParams(no_result_img_tishi_params);
	}

	private void setSeeTooParams() {
		seeToo.setPadding(0, margin20, 0, margin30);
	}

	private void setTitleParams() {
		title.setPadding(0, 0, 0, margin20);
	}

	private void setImgParams() {
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(img_width, img_hight);
		// Log.e("ImgParams", "img_width:" + img_width + ",img_hight:" +
		// img_hight);
		img.setLayoutParams(params);
	}

	private void setImg_playParams() {
		FrameLayout.LayoutParams img_play_params = new FrameLayout.LayoutParams(img_play_width, img_play_hight);
		img_play_params.gravity = Gravity.BOTTOM;
		img_play_params.bottomMargin = margin28;
		img_play_params.leftMargin = margin28;
		img_play.setLayoutParams(img_play_params);
	}

	private void initParm() {
		// TODO 适配参数
		DisplayMetrics dm = new DisplayMetrics();
		dm = this.getResources().getDisplayMetrics();
		if (dm.widthPixels > dm.heightPixels) {
			dm.widthPixels = dm.heightPixels;
		}
		img_width = dm.widthPixels - 16 * 2 - 16 * 2;
		img_hight = img_width * 400 / 716;
		if ((img_width - 112) >= 560) {
			btn_width = 560;
			btn_hight = 88;
		} else {
			btn_width = img_width - 112;
			btn_hight = 88 * btn_width / 560;
		}
		btn_width = 560 * img_hight / 400;
		btn_hight = 88 * img_hight / 400;
		img_play_width = img_play_width * img_hight / 400;
		img_play_hight = img_play_hight * img_hight / 400;
		tishi_img_hight = tishi_img_hight * img_hight / 400;
		tishi_img_width = tishi_img_width * img_hight / 400;
		margin20 = 20 * img_hight / 400;
		margin24 = 24 * img_hight / 400;
		margin26 = 26 * img_hight / 400;
		margin28 = 28 * img_hight / 400;
		margin30 = 30 * img_hight / 400;
		margin32 = 32 * img_hight / 400;
		margin34 = 34 * img_hight / 400;
		margin60 = 60 * img_hight / 400;
		margin84 = 84 * img_hight / 400;
		margin80 = 80 * img_hight / 400;
		margin120 = 120 * img_hight / 400;
		margin100 = 68 * img_hight / 400 + 32;
		margin176 = 144 * img_hight / 400 + 32;
	}

	void showNoResult(boolean IsShowNoResult) {
		if (IsShowNoResult) {
			mNoResult.setVisibility(View.VISIBLE);
			mHasResult.setVisibility(View.GONE);
		} else {
			mNoResult.setVisibility(View.GONE);
			mHasResult.setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		YoukuLoading.dismiss();
		super.onDestroy();
	}

	private void toPullLogin() {
		if (!YoukuUtil.hasInternet()) {
			YoukuUtil.showTips("没有网络，无法发送信息，请检查网络连接。");
			return;
		}
		YoukuLoading.show(this);
		IHttpRequest httpRequest = YoukuService.getService(IHttpRequest.class, true);
		String posturl = getBundleValue(getIntent().getExtras(), "url", "", false);
		String url = URLContainer.getSaosaoTvLoginUrl(posturl, Youku.getPreference("loginAccount"), Youku.getPreference("loginPassword"));
		HttpIntent httpIntent = new HttpIntent(url);
		httpRequest.request(httpIntent, new IHttpRequestCallBack() {
			@Override
			public void onSuccess(HttpRequestManager httpRequestManager) {
				YoukuLoading.dismiss();
				setResult(RESULT_OK);
				finish();

			}

			@Override
			public void onFailed(String failReason) {
				YoukuLoading.dismiss();
				YoukuUtil.showTips(failReason);
				setResult(RESULT_OK);
				finish();
			}
		});
	}

	private void getVideo() {
		mNoResult.setVisibility(View.GONE);
		mHasResult.setVisibility(View.GONE);
		if (!YoukuUtil.hasInternet()) {
			// Util.showTips(R.string.download_no_network);
			TextView no_result_white_tt = (TextView) findViewById(R.id.no_result_white_tt);
			no_result_white_tt.setText("没有网络，无法查询视频信息，请检查网络连接。");
			// ImageView no_result_img_tishi=(ImageView)
			// findViewById(R.id.no_result_img_tishi);
			// no_result_img_tishi.setVisibility(View.GONE) ;
			// TextView no_result_tt_tishi=(TextView)
			// findViewById(R.id.no_result_tt_tishi);
			// no_result_tt_tishi.setVisibility(View.GONE) ;
			showNoResult(true);
			return;
		}
		YoukuLoading.show(this);
		String posturl = getBundleValue(getIntent().getExtras(), "url", "", false);
		posturl = URLEncode(posturl);
		// Logger.d("二维码", "result:" + posturl);
		IHttpRequest httpRequest = YoukuService.getService(IHttpRequest.class, true);
		String url = URLContainer.getSaosaoUrl(posturl);
		// Logger.d("二维码", "url:" + url);
		HttpIntent httpIntent = new HttpIntent(url);
		httpRequest.request(httpIntent, new IHttpRequestCallBack() {
			@Override
			public void onSuccess(HttpRequestManager httpRequestManager) {
				String str = httpRequestManager.getDataString();
				// Logger.e("二维码", "请求成功  str:" + str);
				YoukuLoading.dismiss();
				try {
					saoJson = JSON.parseObject(str, Saosao.class);
					// Logger.e("二维码", "请求成功 status:" + saoJson.status);
					if (null != saoJson && TextUtils.equals("success", saoJson.status)) {
						// if (saoJson.result.paid == 1) {
						// showPaidView();
						// return;
						// }
						title.setText(saoJson.result.title);
						seeToo.setText("观看到：" + YoukuUtil.formatTime(saoJson.result.firsttime));
						if (saoJson.result.limit >= 4) {
							mNORight.setVisibility(View.VISIBLE);
						} else {
							mNORight.setVisibility(View.GONE);
						}
						getImageLoader().displayImage(saoJson.result.img, img);
						showNoResult(false);
					} else {
						showNoResult(true);
					}
				} catch (Exception e) {
					Logger.e("二维码", "请求成功 parseObject error");
					showNoResult(true);
				}
			}

			@Override
			public void onFailed(String failReason) {
				// Util.showTips(failReason);
				// Logger.e("二维码", "请求失败 原因：" + failReason);
				YoukuLoading.dismiss();
				showNoResult(true);
			}
		});
	}

	private void showPaidView() {
		TextView no_result_white_tt = (TextView) findViewById(R.id.no_result_white_tt);
		no_result_white_tt.setText("非常抱歉，我们暂时不能支持付费视频的播放。");
		// TextView no_result_white_tt2=(TextView)
		// findViewById(R.id.no_result_white_tt2);
		// no_result_white_tt2.setVisibility(View.GONE);
		ImageView no_result_img_tishi = (ImageView) findViewById(R.id.no_result_img_tishi);
		no_result_img_tishi.setVisibility(View.GONE);
		findViewById(R.id.no_result_tt_tishi).setVisibility(View.GONE);
		findViewById(R.id.no_result_tt_tishi2).setVisibility(View.GONE);
		findViewById(R.id.no_result_tt_tishi3).setVisibility(View.GONE);
		showNoResult(true);
	}

	public static String URLEncode(String text) {
		StringBuffer StrUrl = new StringBuffer();
		for (int i = 0; i < text.length(); ++i) {
			switch (text.charAt(i)) {
			case '/':
				StrUrl.append("%2F");
				break;
			case ':':
				StrUrl.append("%3A");
				break;
			case '?':
				StrUrl.append("%3F");
				break;
			case '=':
				StrUrl.append("%3D");
				break;
			case '&':
				StrUrl.append("%26");
				break;
			default:
				StrUrl.append(text.charAt(i));
				break;
			}
		}
		return StrUrl.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.actionbarsherlock.app.SherlockFragmentActivity#onCreateOptionsMenu
	 * (com.actionbarsherlock.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
//		menuUtil=new MenuService(this);
//		menuUtil.creatMenu(menu);
//		getSupportActionBar().setDisplayUseLogoEnabled(false);
//		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		return true;
	}

	@Override
	public String getPageName() {
		return "二维码搜索结果页";
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if(id == R.id.re_saosao){
			onMenuSaoSaoClick();
		}else if(id == R.id.img_play){
			goPlayer();
		}else if(id == R.id.play){
			goPlayer();
		}
		
		/*switch (v.getId()) {
		case R.id.re_saosao:
			onMenuSaoSaoClick();
			break;
		case R.id.img_play:
			goPlayer();
			break;
		case R.id.play:
			goPlayer();
			break;
		default:
			break;
		}*/

	}

	@Override
	public void onMenuSaoSaoClick() {
		CaptureResultAcitvity.this.finish();
	}

	void goPlayer() {
		if (null == saoJson) {
			return;
		}

		if (!YoukuUtil.hasInternet()) {
			YoukuUtil.showTips(R.string.tips_no_network);
			return;
		}

		if (saoJson.result.paid == SaosaoResult.PAY_YES && !Youku.isLogined) {
			Intent intent = new Intent(this, LoginRegistCardViewDialogActivity.class);
			intent.putExtra(LoginRegistCardViewDialogActivity.KEY_FROM, LoginRegistCardViewDialogActivity.INTENT_CAPTURERESULT_ACTIVITY);
			startActivityForResult(intent, REQUESTCODE_CAPTURE_LOGIN);
			return;
		}

		Youku.iStaticsManager.TrackCommonClickEvent(StaticsConfigFile.SCAN_AND_SCAN_VIDEO_PLAY_CLICK, StaticsConfigFile.SCAN_AND_SCAN_PAGE,
				Youku.iStaticsManager.getHashMapStyleValue("vid", saoJson.result.videoid), StaticsConfigFile.SCAN_AND_SCAN_VIDEO_PLAY_ENCODE_VALUE);
		goPlayer(saoJson.result.videoid, saoJson.result.title, (int) saoJson.result.firsttime, saoJson.result.paid == SaosaoResult.PAY_YES);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == REQUESTCODE_CAPTURE_LOGIN) {
				goPlayer();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	void goPlayer(String id, String title, int point, boolean isPay) {
		DetailUtil.goPlayerWithpoint(this, id, title, point * 1000, isPay);
	}

	public String getBundleValue(Bundle bundle, String label, String Default, boolean throwException) {
		String value = null;
		try {
			value = bundle.getString(label);
		} catch (Exception e) {
			if (throwException) {
				Logger.e(Youku.TAG_GLOBAL, "F.getBundleValue()", e);
			} else {
				Logger.e(Youku.TAG_GLOBAL, "throw Exception:  get String Bundle label " + label + " is null");
			}
		}
		if (value == null || value.length() == 0) {
			return Default;
		}
		return value;
	}
}
