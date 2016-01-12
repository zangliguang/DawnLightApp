package com.youku.login.Zxing;

import java.io.IOException;
import java.util.Vector;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.AssetFileDescriptor;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.baseproject.utils.UIUtils;
import com.example.youkuloginsdk.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.youku.login.Zxing.camera.CameraManager;
import com.youku.login.Zxing.decoding.CaptureActivityHandler;
import com.youku.login.Zxing.decoding.InactivityTimer;
import com.youku.login.Zxing.view.CustomDialog;
import com.youku.login.Zxing.view.ViewfinderView;
import com.youku.login.activity.CaptureResultAcitvity;
import com.youku.login.activity.LoginRegistCardViewDialogActivity;
import com.youku.login.activity.TestActivity;
import com.youku.login.base.YoukuLoginBaseActivity;
import com.youku.login.network.HttpIntent;
import com.youku.login.network.HttpRequestManager;
import com.youku.login.network.IHttpRequest;
import com.youku.login.network.IHttpRequest.IHttpRequestCallBack;
import com.youku.login.network.URLContainer;
import com.youku.login.service.ILogin;
import com.youku.login.service.YoukuService;
import com.youku.login.upload.UploadInfo;
import com.youku.login.util.Logger;
import com.youku.login.util.Youku;
import com.youku.login.util.YoukuUtil;

public class CaptureActivity extends YoukuLoginBaseActivity implements Callback {

	public static final String TAG = CaptureActivity.class.getSimpleName();
	public static final  int INTENT_TV_LOGIN_BACK=101;
	public static final  int REQUEST_TV_SCAN_CODE_LENGTH =32;//扫描TV客户端二维码的长度
	public static String LOGIN_BROADCAST = "login_broadcast";
    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
//    private TextView txtResult;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;
    public static boolean IsLandSpace=false;
    private TextView notice_first;
    private TextView notice_second;
    private TextView notice_third;
    private  FrameLayout corner_blue;
    public static int HT=0;
    public static int Wt=0;
    public static int Dp=0;
    private boolean isTip=false;
    private View viewfinder_view_block;
    private RelativeLayout viewfinder_view_linearlayout;
    private static HttpRequestManager mRequestAuthorizTask;
//    private final String PhoneToPCUrl = "http://youku.com/";//手机登陆，扫描网站网站登陆URL地址
    private final String PhoneToPCUrl = "http://q.youku.com/";//手机登陆，扫描网站网站登陆URL地址
    private Rect rect;
	private MyOrientationDetector myOrienta;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Logger.d(TAG, "onCreate:" + savedInstanceState);
        TAG_BaseActivity = TAG_CaptureActivity;
        setOrientation(this);
        checkDeviceOrientation();
        super.onCreate(savedInstanceState);
//        if (Youku.isTablet) {
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
//        }
        
        setContentView(R.layout.capture);
        
     
        Logger.d(TAG, "Wt:"+Wt+",ht:"+HT+",IsLandSpace:"+IsLandSpace);
        CameraManager.init(this);
        CameraManager c=CameraManager.get();
        rect=c.getpicRect();
        
        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
        viewfinder_view_block = (View) findViewById(R.id.viewfinder_view_block);
        viewfinder_view_linearlayout = (RelativeLayout) findViewById(R.id.viewfinder_view_linearlayout);
        corner_blue=(FrameLayout)findViewById(R.id.corner_blue);
        notice_first=(TextView) findViewById(R.id.notice_first);
        notice_second=(TextView) findViewById(R.id.notice_second);
        notice_third=(TextView) findViewById(R.id.notice_third);
        
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(rect.right+16,
                rect.top+rect.right-rect.left+16);
        corner_blue.setLayoutParams(params);
        corner_blue.setPadding(rect.left-16, rect.top-16, 0, 0);
//        notice_first.setWidth(rect.right-rect.left+32);
//        notice_second.setWidth(rect.right-rect.left+32);
        
//        matchUIAdapter(rect);
        Logger.lxf("==rect=右侧==="+rect.right+"==rect=左侧==="+rect.left+"==rect=顶部==="+rect.top+"==rect=底部==="+rect.bottom);
//        if(Youku.isLogined){
//        	notice_first.setText(getString(R.string.qrcode_scan_pc_or_tv_login));
//		}else{
			notice_first.setText(getString(R.string.qrcode_scan_phone_login));
//		}
        notice_second.setText(getString(R.string.qrcode_scan_app_play_login));
        notice_third.setText(getString(R.string.qrcode_scan_app_tv_login));
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
    }
    
    //检查当前设备适合的屏幕方向
    private void checkDeviceOrientation(){
    	 if (UIUtils.hasGingerbread()) {
         	if (this.getRequestedOrientation()==ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE) {
         		Logger.lxf("====CaptureActicity=======isLandSpace=true===");
         		IsLandSpace=true;
         	}else {
         		Logger.lxf("====CaptureActicity=======isLandSpace=false===");
         		IsLandSpace=false;
         	}
         }else{
         	if (this.getRequestedOrientation()==ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
         		Logger.lxf("====CaptureActicity=======isLandSpace=true===");
         		IsLandSpace=true;
         	}else {
         		Logger.lxf("====CaptureActicity=======isLandSpace=false===");
         		IsLandSpace=false;
         	}
         }
    	 
    	 
    	 DisplayMetrics dm = new DisplayMetrics();
         dm = this.getResources().getDisplayMetrics();
         if(Youku.isTablet){
         	myOrienta = new MyOrientationDetector(this);
         }
         getWindowManager().getDefaultDisplay().getMetrics(dm);
         HT = 0;
         Wt = 0;
         if(Youku.isTablet){
        	 if(!IsLandSpace){
        		 if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
        			 Wt = dm.widthPixels;
        			 HT = dm.heightPixels;
        		 }else{
//        			 HT = dm.widthPixels;
//        			 Wt = dm.heightPixels;
        			 HT = dm.heightPixels;
        			 Wt = dm.widthPixels;
        		 }
        	 }else{
	         	if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
	         		HT = dm.widthPixels;
	         		Wt = dm.heightPixels;
	         	}else{
	         		Wt = dm.widthPixels;
	         		HT = dm.heightPixels;
	         	}
        	 }
         }else{
        	 if(IsLandSpace){
        		 HT = dm.widthPixels;
        		 Wt = dm.heightPixels;
        	 }else{
        		 Wt = dm.widthPixels;
        		 HT = dm.heightPixels;
        	 }
         }
         Dp = dm.densityDpi;
         Logger.lxf("=====宽====Wt:===="+Wt+",=====高=====ht:==="+HT);
         Logger.lxf("=======dm.widthPixels:===="+dm.widthPixels+",====dm.heightPixels==="+dm.heightPixels);
    }
    
    private void matchUIAdapter(Rect rect){
    	 int tempValue = 0;
    	 int tempMarginLeft = 0 ;
    	 if(!IsLandSpace){
    		tempMarginLeft = (HT-tempValue)/2;
    		tempValue = rect.right-rect.left+32;
         	LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(rect.right+16,
         			rect.top+rect.right-rect.left+16);
         	params.leftMargin = -tempMarginLeft;
         	corner_blue.setLayoutParams(params);
         	corner_blue.setPadding(rect.left-16, rect.top-16, 0, 0);
         	
         	
         	RelativeLayout.LayoutParams paramsFrameLayout = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
         	paramsFrameLayout.leftMargin = -tempMarginLeft;
         	viewfinderView.setLayoutParams(paramsFrameLayout);
         	
         	viewfinder_view_block.setVisibility(View.VISIBLE);
         	RelativeLayout.LayoutParams paramsView = new RelativeLayout.LayoutParams(tempMarginLeft,LayoutParams.MATCH_PARENT);
         	paramsView.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
         	viewfinder_view_block.setLayoutParams(paramsView);
         	
//         	notice_first.setWidth(tempValue);
//         	notice_second.setWidth(tempValue);
         }else{
        	tempValue = rect.right-rect.left+32;
         	LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(rect.right+16,
         			rect.top+rect.right-rect.left+16);
         	corner_blue.setLayoutParams(params);
         	corner_blue.setPadding(rect.left-16, rect.top-16, 0, 0);
         	
         	RelativeLayout.LayoutParams paramsFrameLayout = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
         	viewfinderView.setLayoutParams(paramsFrameLayout);
         	
//         	viewfinder_view_block.setVisibility(View.VISIBLE);
//         	RelativeLayout.LayoutParams paramsView = new RelativeLayout.LayoutParams((HT-(rect.right-rect.left+32))/2-32,LayoutParams.MATCH_PARENT);
//         	viewfinder_view_block.setLayoutParams(paramsView);
         	
//         	notice_first.setWidth(tempValue);
//         	notice_second.setWidth(tempValue);
         }
    }
    
    /***
     * 二维码请求同步登录接口
     * @param urlStr
     * 
     */
    public void requestAuthorizeTask(String urlStr){
    	Logger.d("sgh","saosao: requestAuthorizeTask()");
    	mRequestAuthorizTask = (HttpRequestManager) YoukuService.getService(IHttpRequest.class, true);
    	mRequestAuthorizTask.setSaveCookie(true);
    	mRequestAuthorizTask.setParseErrorCode(true);
    	mRequestAuthorizTask.request(new HttpIntent(URLContainer.getThirdLoginOrAuthorizUrl(urlStr),Youku.isLogined),
				new IHttpRequestCallBack() {

					@Override
					public void onSuccess(HttpRequestManager request) {
						pareResult(request);
						Intent intent = new Intent();
						//intent.setClass(CaptureActivity.this, HomePageActivity.class);
//						intent.setClass(CaptureActivity.this, TestActivity.class);
//						intent.putExtra("tab", 1);
//						startActivity(intent);
						finish();
					}

					@Override
					public void onFailed(String failReason) {
						CaptureUtil.getInstance().showErrorMessageTip(failReason);
					}
		});
    }
    
    
    /**
     * 解析扫描二维码结果并刷新登录成功界面
     * @param jsonStr
     */
    
    @SuppressLint("NewApi")
	private void pareResult(HttpRequestManager request){
    	String jsonStr = request.getDataString();
    	try {
    		if(!TextUtils.isEmpty(jsonStr)){
    			JSONObject jsonObject = new JSONObject(jsonStr);
    			if(jsonObject.has("status")&&"success".equals(jsonObject.optString("status"))){
    				if(!jsonObject.has("results")&&jsonObject.has("code")&&0==jsonObject.optInt("code")){
    					if(Youku.isLogined){
							YoukuUtil.showTips("PC端已经同步登录成功了");
						}else{
							YoukuUtil.showTips("App端已经同步登录成功了");
						}
    					return ;
    				}
    				YoukuUtil.showTips("App端已经同步登录成功了");
    				logout();
//    				invalidateOptionsMenu();
    				
    				JSONObject resultJsonObject = jsonObject.optJSONObject("results");
    				Logger.lxf("===新的登录信息===resultJsonObject==="+resultJsonObject);
    				String userName = getJsonValueStr(resultJsonObject, "username");
    				Youku.savePreference("userName", userName);
    				Youku.savePreference("userIcon", getJsonValueStr(resultJsonObject, "icon_large"));
    				final String uid = getJsonValueStr(resultJsonObject, "userid");
    				Youku.savePreference("uid", uid);
    				Youku.savePreference("isNotAutoLogin", false);
					Youku.savePreference("isLogined", true);

    				Youku.isLogined = true;
					Youku.loginAccount = userName;
					
					
					// 发送登入成功广播
					Youku.mContext.sendBroadcast(new Intent(LOGIN_BROADCAST));
					
					/*new Thread(new Runnable() {

						@Override
						public void run() {
							try {
								DefaultHttpClient httpClient = new DefaultHttpClient(
										createHttpParams());
								HttpPost p = new HttpPost(URLContainer
										.getPushCollectionURL(2, null));
								p.setHeader("User-Agent", Youku.User_Agent);
								HttpResponse r = httpClient.execute(p);
								if (r.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
								}
							} catch (ClientProtocolException e) {
								Logger.e("LoginManagerImpl", e);
							} catch (IOException e) {
								Logger.e("LoginManagerImpl", e);
							}catch (Exception e) {
								Logger.e("LoginManagerImpl", e);
							}
						}
					}).start();*/
					
    			}else{
    				CaptureUtil.getInstance().showErrorMessageTip(jsonStr);
    				return;
    			}
    		}
			
		} catch (JSONException e) {
			CaptureUtil.getInstance().showErrorMessageTip(jsonStr);
			e.printStackTrace();
		}
    }
    
    /**
     * 登出操作
     * 
     * 不含清除cookie的登出
     * 
     */
    public void logout() {
		// 取消正在下载的视频
/*		UploadInfo info = UploadProcessor.getUploadingTask();
		if (info != null) {
			info.setStatus(UploadInfo.STATE_PAUSE);
		}
		if (null != info && !TextUtils.isEmpty(info.getTaskId())) {
			NotificationManager nm = (NotificationManager) Youku.context
					.getSystemService(Context.NOTIFICATION_SERVICE);
			nm.cancel(Integer.parseInt(info.getTaskId()));
		}
		UploadProcessor.cancelUploadNotifaction();*/

		// 改变登录状态
		Youku.isLogined = false;
		Youku.userName = "";
		// 将登录状态存到本地
		Youku.savePreference("isNotAutoLogin", true);
		Youku.savePreference("isLogined", false);

		Youku.savePreference("uploadAccessToken", "");
		Youku.savePreference("uploadRefreshToken", "");
		Youku.savePreference("uid", "");
		Youku.savePreference("userIcon", "");
		
		Youku.savePreference("loginAccount", "");
		Youku.savePreference("loginPassword", "");

		Youku.userprofile = null;
		// 发送登出成功广播
		Youku.mContext.sendBroadcast(new Intent(ILogin.LOGOUT_BROADCAST));

		/*new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					DefaultHttpClient httpClient = new DefaultHttpClient(
							createHttpParams());
					HttpPost p = new HttpPost(
							URLContainer.getPushCollectionURL(3, null));
					p.setHeader("User-Agent", Youku.User_Agent);
					HttpResponse r = httpClient.execute(p);
					if (r.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
						// Youku.showTips("login collection success");
					}
				} catch (ClientProtocolException e) {
					Logger.e("LoginManagerImpl", e);
				} catch (IOException e) {
					Logger.e("LoginManagerImpl", e);
				}
			}
		}).start();*/
	}

    
    
    private static final int CON_TIME_OUT_MS = 15 * 1000;
	private static final int SO_TIME_OUT_MS = 15 * 1000;
	private static final int SOCKET_BUFFER_SIZE = 8 * 1024;
    
    private HttpParams createHttpParams() {
		HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
		HttpConnectionParams.setConnectionTimeout(params, CON_TIME_OUT_MS);
		HttpConnectionParams.setSoTimeout(params, SO_TIME_OUT_MS);
		HttpConnectionParams.setSocketBufferSize(params, SOCKET_BUFFER_SIZE);
		return params;
	}
    
 // 返回JSONObject的值
 	public String getJsonValueStr(JSONObject resultJsonObject, String str) {
 		if (null != resultJsonObject && resultJsonObject.has(str)
 				&& !TextUtils.isEmpty(resultJsonObject.optString(str))) {
 			return resultJsonObject.optString(str);
 		}
 		return "";
 	}
    
    @Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
		/*menuUtil=new MenuService(this);
		menuUtil.setMenuVisiblity(MenuService.menu_saosao, false);
		menuUtil.creatMenu(menu);*/
//        getSupportActionBar().setDisplayUseLogoEnabled(false);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        return true;
    }
    @Override
    protected void onResume() {
    	Logger.d(TAG, "onResume:hasSurface:" + hasSurface);
        super.onResume();
        if(null!=myOrienta){
        	myOrienta.enable();
        }
        doResume();
    }
	private void doResume() {
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;
	}

    @Override
    protected void onPause() {
    	Logger.d(TAG, "onPause:handler:" + handler);
        super.onPause();
        if(null!=myOrienta){
        	myOrienta.disable();
        }
        doPause();
    }
	private void doPause() {
		if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
	}
    
    @Override
    protected void onStop() {
    	Logger.d(TAG, "onStop");
    	super.onStop();
    	TAG_BaseActivity = 0;
    }
    
    @Override
    protected void onDestroy() {
    	Logger.d(TAG, "onDestroy");
        inactivityTimer.shutdown();
        TAG_BaseActivity=TAG_HomePageActivity;
        super.onDestroy();
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
    	Logger.d(TAG, "initCamera:handler:" + handler);
        try {
            CameraManager.get().openDriver(surfaceHolder);
//            CameraManager.get().openDriverSecond(surfaceHolder);
        } catch (IOException ioe) {
        	YoukuUtil.showTips(R.string.camera_error_tip);
        	finish();
            return;
        } catch (RuntimeException e) {
        	YoukuUtil.showTips(R.string.camera_error_tip);
        	finish();
            return;
        } catch (Exception e) { 
        	YoukuUtil.showTips(R.string.camera_error_tip);
        	finish();
        	return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats, characterSet);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    	Logger.d(TAG, "surfaceChanged");
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    	Logger.d(TAG, "surfaceCreated:hasSurface:" + hasSurface);
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    	Logger.d(TAG, "surfaceDestroyed");
        hasSurface = false;
    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();

    }

    String url;
    public void handleDecode(Result obj, Bitmap barcode) {
        inactivityTimer.onActivity();
//        viewfinderView.drawResultBitmap(barcode);
        playBeepSoundAndVibrate();
        if (!YoukuUtil.hasInternet()) {
			YoukuUtil.showTips(R.string.tips_no_network);
			return;
		}
//      txtResult.setText(obj.getBarcodeFormat().toString() + ":" + obj.getText());
        url=obj.getText();
    	if(!TextUtils.isEmpty(url)&&REQUEST_TV_SCAN_CODE_LENGTH==url.length()&&!url.contains("http")){
    		if(!Youku.isLogined){
    			CustomDialog.showSokuDialog(CaptureActivity.this,"您尚未登录，是否马上登录", new View.OnClickListener() {
    				@Override
    				public void onClick(View v) {
    					final Intent logIntent = new Intent(CaptureActivity.this, LoginRegistCardViewDialogActivity.class);
    					logIntent.putExtra("curfragment", LoginRegistCardViewDialogActivity.INTENT_LOGIN);
    					CaptureActivity.this.startActivityForResult(logIntent,LoginRegistCardViewDialogActivity.INTENT_LOGIN);
    				}
    			}, new View.OnClickListener() {
    				@Override
    				public void onClick(View v) {
    						doPause();
    						doResume();
    				}
    			});
    		}else {
    			goResultActivity(url,true);
			}
			return;
    	}
    	if(url.startsWith(PhoneToPCUrl)){
    		requestAuthorizeTask(url);
    	}else{
    		goResultActivity(url,false);
    	}
    }
    private void goResultActivity(String url,boolean isForResult) {
    	Logger.e("goResultActivity", url);
//        Intent intent=new Intent();
//        intent.setClass(this, CaptureResultAcitvity.class);
//        Bundle extras=new Bundle();
//        extras.putString("url", url);
//        intent.putExtras(extras);
//        if (isForResult) {
//        	startActivityForResult(intent, INTENT_TV_LOGIN_BACK);
//		}else {
//			startActivity(intent);
//		}
    }
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK
				&& requestCode == LoginRegistCardViewDialogActivity.INTENT_LOGIN
				&& Youku.isLogined) {
			   goResultActivity(url,true);
		}
		if (resultCode == RESULT_OK
				&& requestCode == INTENT_TV_LOGIN_BACK) {
			Logger.lxf("====onActivityResult=========finisht()==================");
			finish();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
            try { 
                mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final OnCompletionListener beepListener = new OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

    @Override
    public String getPageName() {
        // TODO Auto-generated method stub
        return null;
    }
    
    private int currentOrientation = 0;//当前旋转的角度数
    private int tempOrientation = 0;//临时旋转角度
    class MyOrientationDetector extends OrientationEventListener{
	    public MyOrientationDetector( Context context ) {
	        super(context );
	    }
	    @Override
	    public void onOrientationChanged(int orientation) {
	    	
	    	if(orientation == OrientationEventListener.ORIENTATION_UNKNOWN) {
	    	    return;  //手机平放时，检测不到有效的角度
	    	}
	    	//只检测是否有四个角度的改变
	    	if( orientation > 350 || orientation< 10 ) { //0度
	    	     orientation = 0;
	    	}  
	    	else if( orientation > 80 &&orientation < 100 ) { //90度
	    	    orientation= 90;
	    	}
	    	else if( orientation > 170 &&orientation < 190 ) { //180度
	    	    orientation= 180;
	    	}
	    	else if( orientation > 260 &&orientation < 280  ) { //270度
	    	    orientation= 270;
	    	}
	    	else {
	    	    return;
	    	}
	    	currentOrientation = orientation;
	    	if(currentOrientation!=tempOrientation){
	    		Logger.lxf("===被旋转了====="+currentOrientation);
	    		if(null!=CameraManager.getCamera()){
	    			CameraManager.getCamera().stopPreview();
	    			if(IsLandSpace){
	    				if(tempOrientation==180){
	    					if(tempOrientation-currentOrientation==90||currentOrientation-tempOrientation==90){
	    						CameraManager.getCamera().setDisplayOrientation(180);
	    					}else{
	    						CameraManager.getCamera().setDisplayOrientation(currentOrientation);
	    					}
	    				}else{
	    					if(currentOrientation==270){
	    						CameraManager.getCamera().setDisplayOrientation(0);
	    					}else if(currentOrientation==90){
	    						CameraManager.getCamera().setDisplayOrientation(0);
	    					}else{
	    						CameraManager.getCamera().setDisplayOrientation(currentOrientation);
	    					}
	    				}
	    			}else{
	    				if(tempOrientation==90||tempOrientation == 270){
	    					if(currentOrientation == 0){
	    						if(tempOrientation-currentOrientation==90||tempOrientation -currentOrientation==270){
	    							CameraManager.getCamera().setDisplayOrientation(90);
	    						}
	    					}else if(currentOrientation == 180){
	    						CameraManager.getCamera().setDisplayOrientation(270);
	    					}
	    				}else if(tempOrientation==180){
	    					if(currentOrientation == 90||currentOrientation == 270){
	    						CameraManager.getCamera().setDisplayOrientation(270);
	    					}
	    				}else{
	    					if(currentOrientation==270){
	    						CameraManager.getCamera().setDisplayOrientation(90);
	    					}else if(currentOrientation==90){
	    						CameraManager.getCamera().setDisplayOrientation(90);
	    					}else if(currentOrientation==180||currentOrientation==0){
	    						CameraManager.getCamera().setDisplayOrientation(0);
	    					}else{
	    						CameraManager.getCamera().setDisplayOrientation(currentOrientation);
	    					}
	    				}
	    				
//	    				}
	    			}
	    			CameraManager.getCamera().startPreview();
	    		}
	    		tempOrientation = currentOrientation;
	    	}
	        Logger.lxf("===onOrientationChanged:==="+orientation);
	        Logger.lxf("===tempOrientation:==="+tempOrientation);
	        Logger.lxf("===当前旋转角度====currentOrientation:==="+currentOrientation);
	    }
	}
    
    //设置适合pad的屏幕方向
    public void setOrientation(Activity activity) {
		if ((Configuration.ORIENTATION_PORTRAIT == getDefaultOrientation(activity))) {
			// 默认适合竖屏
			if (UIUtils.hasGingerbread()) {
				activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
			} else {
				activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			}
		} else {
			// 默认适合横屏
			if (UIUtils.hasGingerbread()) {
				activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
			} else {
				activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			}
		}
	}
	
	//获取默认的屏幕方向，默认为2，横屏
	public int getDefaultOrientation(Context context) {
		int capture_orientation = 2;
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Configuration config = context.getResources().getConfiguration();
		int rotation = windowManager.getDefaultDisplay().getRotation();
		if (((rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180) && config.orientation == Configuration.ORIENTATION_LANDSCAPE)
				|| ((rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270) && config.orientation == Configuration.ORIENTATION_PORTRAIT)) {
			capture_orientation = Configuration.ORIENTATION_LANDSCAPE;
		} else {
			capture_orientation = Configuration.ORIENTATION_PORTRAIT;
		}
		return capture_orientation;
	}

}