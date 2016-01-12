package com.youku.login.upload;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;

import com.example.youkuloginsdk.BuildConfig;
import com.example.youkuloginsdk.R;
import com.youku.login.network.URLContainer;
import com.youku.login.util.Logger;
import com.youku.login.util.Youku;
import com.youku.login.util.YoukuUtil;


/**
 * 上传配置类
 * 
 * @ClassName: UploadConfig
 * @author Beethoven
 * @mail zhanghuitao@youku.com
 * @date 2012-3-6 下午9:34:34
 */
public class UploadConfig {

	public static final String BROADCAST_UPLOAD_CHANGE = "com.youku.paike.broadcast_upload_event_fragment";

	// 上传设置 flag : 0任何网络、 1 仅wifi
	public final static int UPLOAD_ANY = 0;
	public final static int UPLOAD_WIFI = 1;

	/**
	 * Android Phone Key
	 */
	public final static String CLIENT_ID = "8020077c0bbf7fc2";
	public final static String CLIENT_SECRET = "567d95ceab365bbb795e41a680ae6f75";
	
    public final static String getClientId() {
		return Youku.isTablet ? "898fb8dc903a7311" : "8020077c0bbf7fc2";
	}

    public static String getClientSecret() {
    	return Youku.isTablet ? "e9029534c1a6cb35929a9dfbd3cad9d4" : "567d95ceab365bbb795e41a680ae6f75";
	}
	/**
	 * API服务器
	 */
	public static final String URL = "http://pkapi.m.youku.com/"; // 正式服务器

	public static String getUrlUploadTaskAdd() {
		return URL + "openapi-wireless/user/uploads/add";
	}

	public static String getUrlUploadTaskStart() {
		return "http://statis.api.3g.youku.com/" + "openapi-wireless/statis/video-uploads";
	}

	/************************** 配置项 ******************************/

	/**
	 * 获取用户名
	 * 
	 * @Title: getUserName
	 * @return String
	 * @date 2012-7-12 下午6:23:10
	 */
	public static String getUserID() {
		return Youku.getPreference("uid");
	}

	/**
	 * 获取用户名
	 * 
	 * @Title: getUserName
	 * @return String
	 * @date 2012-7-12 下午6:23:10
	 */
	public static String getUserName() {
		return Youku.getPreference("userName");
	}

	/**
	 * 获取用户密码
	 * 
	 * @Title: getUserPassword
	 * @return String
	 * @date 2012-7-12 下午6:24:34
	 */
	public static String getUserPassword() {
		String s = Youku.getPreference("userhash");
		if (!"".equals(s))
			return s;
		s = Youku.getPreference("loginPassword");
		if (!"".equals(s))
			return s;
		return "";
	}

	/**
	 * 记录上传access token
	 * 
	 * @Title: saveUploadAccessToken
	 * @param access_token
	 * @return void
	 * @date 2012-7-12 下午6:21:37
	 */
	public static void saveUploadAccessToken(String access_token) {
		Youku.savePreference("uploadAccessToken", access_token);
	}

	/**
	 * 获取上传access token
	 * 
	 * @Title: getUploadAccessToken
	 * @return String
	 * @date 2012-7-12 下午6:22:00
	 */
	public static String getUploadAccessToken() {
		return Youku.getPreference("uploadAccessToken");
	}

	/**
	 * 记录上传refresh token
	 * 
	 * @Title: saveUploadRefreshToken
	 * @param refresh_token
	 * @return void
	 * @date 2012-7-12 下午6:22:11
	 */
	public static void saveUploadRefreshToken(String refresh_token) {
		Youku.savePreference("uploadRefreshToken", refresh_token);
	}

	public static void showTips(String text) {
		YoukuUtil.showTips(text);
	}

	public static void showTips(int textId) {
		YoukuUtil.showTips(textId);
	}

	public static Context getContext() {
		return Youku.mContext;
	}

	public static boolean hasInternet() {
		return YoukuUtil.hasInternet();
	}

	public static boolean isWifi() {
		return YoukuUtil.isWifi();
	}

	public static boolean post(String url, String paras) {
		return postApi(url, paras);
	}

	/**
	 * 发送一个http post请求，返回是否成功
	 * 
	 * @Title: postApi
	 * @param url_
	 * @param postParas
	 *            post参数 格式为aa=1&bb=2，只需要传递业务参数，自动添加统计参数
	 * @return boolean
	 * @date 2012-2-20 下午4:54:29
	 */
	private static boolean postApi(String url, String postStr) {
		// url = appendApiExtraParas(url);
		String postParas = postStr + "&" + URLContainer.getStatisticsParameter(URLContainer.METHOD_POST,"/statis/video-uploads");
		Logger.d("HTTP POST::" + url);
		Logger.d("HTTP POST DATA::" + postParas);
		try {
			URL url_ = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) url_.openConnection();
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setConnectTimeout(Youku.TIMEOUT);
			conn.setReadTimeout(Youku.TIMEOUT);
			conn.setRequestProperty("Cookie", Youku.COOKIE);
			conn.setRequestProperty("User-agent", Youku.User_Agent);

			conn.setDoOutput(true);
			DataOutputStream out = new DataOutputStream(conn.getOutputStream());
			out.writeBytes(postParas);
			out.flush();
			out.close();

			Logger.d("HTTP POST RESULT::" + conn.getResponseCode());
			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK)
				return true;
			else
				return false;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void googleStatCreate() {
		// DataStatistics.getInstance().doUploadCreateSuccessEvent(Youku.mContext);
		// 统计数据

	}

	public static void youkuStatCreate() {
		UploadConfig.post(UploadConfig.STAT_START_UPLOAD, "");
	}

	@SuppressWarnings("deprecation")
	public static void youkuStatFinish(UploadInfo info, String videoId) {// 提交两次，零时解决上传视频无地理位置信息问题
		if (!UploadConfig
				.post(UploadConfig.STAT_COMPETE_UPLOAD,
						"vid="
								+ videoId
								+ "&md5="
								+ info.getMd5()
								+ (info.getLongitude() != null ? (info
										.isNewVideo() ? "&longitude="
										+ info.getLongitude() + "&latitude="
										+ info.getLatitude() : "")
										+ "&loc_lng="
										+ info.getLongitude()
										+ "&loc_lat=" + info.getLatitude() : "")// 向无线接口发送成功信息
								+ (info.getLocationName() != null ? "&loc_name="
										+ URLEncoder.encode(info
												.getLocationName())
										: "")// 地址名称
								+ (info.getLocationAddress() != null ? "&loc_address="
										+ URLEncoder.encode(info
												.getLocationAddress())
										: ""))) {// 地址详细地址

			UploadConfig
					.post(UploadConfig.STAT_COMPETE_UPLOAD,
							"vid="
									+ videoId
									+ "&md5="
									+ info.getMd5()
									+ (info.getLongitude() != null ? (info
											.isNewVideo() ? "&longitude="
											+ info.getLongitude()
											+ "&latitude=" + info.getLatitude()
											: "")
											+ "&loc_lng="
											+ info.getLongitude()
											+ "&loc_lat=" + info.getLatitude()
											: "")// 向无线接口发送成功信息
									+ (info.getLocationName() != null ? "&loc_name="
											+ URLEncoder.encode(info
													.getLocationName())
											: "")// 地址名称
									+ (info.getLocationAddress() != null ? "&loc_address="
											+ URLEncoder.encode(info
													.getLocationAddress())
											: ""));
		}
	}

	public static int getSliceSize() {
		return YoukuUtil.isWifi() ? 1024 : 256;
	}

	/**
	 * 获取上传refresh token
	 * 
	 * @Title: getUploadRefreshToken
	 * @return
	 * @return String
	 * @date 2012-7-12 下午6:22:37
	 */
	public static String getUploadRefreshToken() {
		return Youku.getPreference("uploadRefreshToken");
	}

	// public static String getUrlUploadTaskAdd() {
	// return URL + "openapi-wireless/user/uploads/add";
	// }
	//
	// public static String getUrlUploadTaskStart() {
	// return URL + "openapi-wireless/statis/video-uploads";
	// }

	public static String STAT_START_UPLOAD = getUrlUploadTaskStart();

	public static String STAT_COMPETE_UPLOAD = getUrlUploadTaskAdd();

	public static final boolean DEBUG_MODE_OPENED = BuildConfig.DEBUG;

	public static final int TIMEOUT = 30 * 1000;

	public static final int TIMEOUT_UPLOAD_DATA = 2 * 60 * 1000;

	public static final String UPDATE_UI_BROADCAST_NAME = BROADCAST_UPLOAD_CHANGE;

	public static final String UPLOAD_FINISH_BROADCAST = "UPLOAD_FINISH_BROADCAST";
	public static final String UPLOAD_START_BROADCAST = "UPLOAD_START_BROADCAST";
	public static final String UPLOAD_TASK_SUCCESS_BROADCAST = "UPLOAD_TASK_SUCCESS_BROADCAST";

	// public static final Class<Jumper> JUMPER_CLASS = Jumper.class;
	/**
	 * 视频上传默认分类（默认原创）
	 */
	public static final int CATEGORY = 92;
	/**
	 * 视频上传默认标签
	 */
	public static final String TAG = "优酷拍客 Android 拍客 原创";

	/**
	 * 提示文案-上传中
	 */
	public static final int R_STRING_1 = R.string.uploading;
	/**
	 * 提示文案-等待上传
	 */
	public static final int R_STRING_2 = R.string.wait;
	/**
	 * 提示文案-暂停中
	 */
	public static final int R_STRING_3 = R.string.pause;
	/**
	 * 提示文案-上传完成
	 */
	public static final int R_STRING_4 = R.string.uploaded;
	/**
	 * 提示文案-上传账号发生变更
	 */
	public static final int R_STRING_5 = R.string.account_changed;
	/**
	 * 提示文案-源文件不存在
	 */
	public static final int R_STRING_6 = R.string.src_file_not_exists;
	/**
	 * 提示文案-运营商网络警告
	 */
	public static final int R_STRING_7 = R.string.upload_alert_network;
	/**
	 * 提示文案-重复上传
	 */
	public static final int R_STRING_8 = R.string.duplicate_upload;
	/**
	 * 提示文案-上传彻底失败
	 */
	public static final int R_STRING_9 = R.string.upload_task_scrap;
	/**
	 * 提示文案-上传彻底失败
	 */
	public static final int R_STRING_10 = R.string.upload_setting_not_allow;

	public static final int R_LAYOUT_1 = R.layout.notify;
	public static final int R_ID_1 = R.id.notify_text;
	public static final int R_ID_2 = R.id.notify_state;
	public static final int R_ID_3 = R.id.notify_speed;
	public static final int R_ID_4 = R.id.notify_processbar;

	/************************** END ******************************/

	public final static String DB_NAME = "paike.db";
	public final static int DB_VERSION = 2;

	public static final int MAX_THREAD_COUNT = 2;// wifi=2-40 1-53 3-38 5-39
													// 10-38
	public static final int CHECK_INTERVAL = 60;// 秒
	public static boolean isAlertedNet = false;

	public final static String URL_LOGIN = "https://openapi.youku.com/v2/oauth2/token";
	public final static String URL_CREATE = "https://openapi.youku.com/v2/uploads/create.json";
	public final static String URL_CREATE_FILE = "http://upload_server_uri/create_file";
	public final static String URL_NEW_SLICE = "http://upload_server_uri/new_slice";
	public final static String URL_UPLOAD_SLICE = "http://upload_server_uri/upload_slice";
	public final static String URL_CHECK = "http://upload_server_uri/check";

	public final static String URL_SLICES = "http://upload_server_uri/slices";
	public final static String URL_RESET_SLICE = "http://upload_server_uri/reset_slice";

	public final static String URL_COMMIT = "https://openapi.youku.com/v2/uploads/commit.json";
	public final static String URL_CANCEL = "http://openapi.youku.com/v2/uploads/cancel.json";
	public final static String URL_SPEC = "http://openapi.youku.com/v2/schemas/upload/spec.json";

	public final static String TASK_TYPE_PARA_NAME = "TASK_TYPE";
	public final static String TASK_PARA_INFO_PARA_NAME = "TASK_INFO";
	public final static String TASK_PARA_SESSION_PARA_NAME = "TASK_SESSION";
	public final static int TASK_TYPE_NEW = 0;
	public final static int TASK_TYPE_QUEUE = 1;
	public final static int TASK_TYPE_QUEUE_SESSION = 2;
	public final static int TASK_TYPE_QUEUE_SESSION_PRETASK = 3;

	/**
	 * 当前网络是否满足上传设置
	 * 
	 * @Title: uploadSettingIsOk
	 * @return boolean
	 * @date 2012-7-25 下午3:19:53
	 */
	public static boolean uploadSettingIsOk() {
		// 上传设置 (0任何网络上传、1仅WIFI)
		int state = Youku.getPreferenceInt("uploadModeState");
		if (state != UPLOAD_ANY
				&& !(state == UPLOAD_WIFI && YoukuUtil.isWifi()))
			return false;
		return true;
	}

	/**
	 * 视频隐私 完全公开
	 */
	public static final int PRIVACY_PUBLIC = 0;
	/**
	 * 视频隐私 仅好友开放
	 */
	public static final int PRIVACY_SOME = 1;
	/**
	 * 视频隐私 有密码
	 */
	public static final int PRIVACY_PASSWORD = 4;
	/**
	 * 版权 原创
	 */
	public static final String COPYRIGHT_ORIGINAL = "original";
	/**
	 * 版权 转载
	 */
	public static final String COPYRIGHT_REPRODUCED = "reproduced";
	/**
	 * 视频分类 原创
	 */
	public static final String CATEGORY_ORIGINAL = "Original";
	/**
	 * 视频隐私 编号与单词映射
	 */
	@SuppressLint("UseSparseArrays")
	public static final Map<Integer, String> PRIVACY_MAP = new HashMap<Integer, String>();
	static {
		PRIVACY_MAP.put(PRIVACY_PUBLIC, "all");
		PRIVACY_MAP.put(PRIVACY_SOME, "friend");
		PRIVACY_MAP.put(PRIVACY_PASSWORD, "password");
	}
}
