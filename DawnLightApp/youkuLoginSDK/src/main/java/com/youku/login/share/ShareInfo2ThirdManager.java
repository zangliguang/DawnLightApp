package com.youku.login.share;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.example.youkuloginsdk.R;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXVideoObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.youku.login.util.Logger;
import com.youku.login.util.YoukuUtil;
import com.youku.login.widget.YoukuPopupMenu.OnItemSelectedListener;

/**
 * 分享到第三方的功能实现类
 * 
 * @author afei
 * 
 */
public class ShareInfo2ThirdManager extends Application {

	private static final int WEIXIN_WIDTH_THUMB_SIZE = 120;
	private static final int WEIXIN_HEIGHT_THUMB_SIZE = 90;
	public final static int SHOW_LOADING_IMAGE = 1000114;
	public final static int DISSMISS_LOADING_IMAGE = 1000115;
	public IWXAPI weixinApi;
	public static final int HEAD_DATA_NUMBER = 3;// 抬头显示的数据数
	private static int RESULT_SELECT_FILE = 1;

	// private String drawableStr;
	// private String weixinTitle;
	// private String weixinFriendTitle;
	// private String videoUrl;
	// private String imageUrl;
	public ChooserPopuwindow dialog;

	private Activity mActivity;
	private View mView;
	public List<Drawable> cachedImageList = new ArrayList<Drawable>();
	public boolean isFullScreen;
	private Handler mhandler;
	// private ShareVideoInfo shareVideoInfo;
	private View controlLayout;
	private View interactView;
	private View titleLayout;
	private boolean isActionBar = false;

	public ShareInfo2ThirdManager(Activity mActivity) {
		this.mActivity = mActivity;
	}

	public ShareInfo2ThirdManager(ShareVideoInfo shareVideoInfo, View interactView, View titleLayout,
			View controlLayout, boolean isFullScreen, Activity mActivity, View mView,
			boolean actionbar) {
		this.interactView = interactView;
		this.titleLayout = titleLayout;
		this.controlLayout = controlLayout;
		this.mActivity = mActivity;
		this.mView = mView;
		this.isFullScreen = isFullScreen;
		// this.shareVideoInfo = shareVideoInfo;
		this.isActionBar = actionbar;
		registerToWeixin();
	}

	
//	Bitmap loadedImage;
//	private Handler shareHandler = new Handler(){
//
//		@Override
//		public void handleMessage(Message msg) {
//			super.handleMessage(msg);
//			switch (msg.what) {
//			case ShareAppUtil.MESSAGE_SHARE_IMAGE_DRAWALE:
//				loadedImage = (Bitmap)msg.obj;
//				Logger.lxf("===shareHandler===loadedImage======网络分享出来的图片===="+loadedImage);
//				break;
//
//			default:
//				break;
//			}
//		}
//		
//	};
//	
//	/**
//	 * 弹出带有第三方app分享的popupWindow窗口
//	 * 
//	 * @param activity
//	 * @param view
//	 */
//	public void getResolveInfo(final VideoUrlInfo videoInfo, final ShareVideoInfo shareVideoInfo) {
//
//		// shareVideoInfo = ShareAppUtil.getDetailVideoInfo(videoInfo.getVid());
//		ShareAppUtil.getInstance().getDrawableFromUrl(shareHandler,ShareAppUtil.imageUrl);
//
//		dialog = new ChooserPopuwindow(interactView, titleLayout, controlLayout, isFullScreen, mActivity,
//				isActionBar);
//		ChooserAdapter adapter = new ChooserAdapter(mActivity);
//
//		Intent intent = new Intent(Intent.ACTION_SEND);
//		intent.setType("text/plain");
//		List<ShareResolveInfo> data = getFliteredResolveInfoList(intent);
//		// final List<ShareResolveInfo> moreData = new
//		// ArrayList<ShareResolveInfo>();
//		// if (data.size() > HEAD_DATA_NUMBER) {
//		// // 显示查看更多
//		// for (int i = 0; i < HEAD_DATA_NUMBER; i++) {
//		// headData.add(data.get(i));
//		// }
//		//
//		// for (int i = 0; i < data.size() - HEAD_DATA_NUMBER; i++) {
//		// moreData.add(data.get(HEAD_DATA_NUMBER + i));
//		// }
//		// adapter.setData(headData);
//		// dialog.setAdapter(adapter);
//		// // dialog.addFootView();
//		// } else {
//		// 隐藏查看更多
//		adapter.setData(data);
//		dialog.setAdapter(adapter);
//		// }
//
//		dialog.setOnItemSelectedListener(new OnItemSelectedListener() {
//
//			@Override
//			public void onItemSelected(ChooserAdapter adapter, int position) {
//				Logger.e("DetailActivity", videoInfo.getWeburl() + "我来分享===========");
//				// if (position == adapter.getCount()) {
//				// headData.addAll(moreData);
//				// dialog.setAdapter(adapter);
//				// dialog.removeFootView();
//				// } else {
//				if (null != videoInfo) {
//					ShareResolveInfo selfResolveInfo = (ShareResolveInfo) adapter.getItem(position);
//					if (selfResolveInfo.resolvePackageName.equals(ShareConfig.WEIXIN_PACKAGE_NAME)) {
//						if (!weixinApi.isWXAppInstalled()) {
//							showInstallWeixinDialog(mActivity
//									.getString(R.string.videoinfo_share_use_weixin_install_title));
//						} else if (!weixinApi.isWXAppSupportAPI()) {
//							showInstallWeixinDialog(mActivity
//									.getString(R.string.videoinfo_share_use_weixin_upinstall_title));
//						} else {
//							sendSeqToWeixin(mActivity, null!=loadedImage?new BitmapDrawable(loadedImage):null,
//									videoInfo.getTitle(), null != shareVideoInfo ? shareVideoInfo.getShowName()
//											: mActivity.getString(R.string.share_video_title), videoInfo
//											.getWeburl(), false);
//						}
//					} else if (selfResolveInfo.resolvePackageName.equals(ShareConfig.WEIXIN_FRIEND_PACKAGE_NAME)) {
//						if (!weixinApi.isWXAppInstalled()) {
//							showInstallWeixinDialog(mActivity
//									.getString(R.string.videoinfo_share_use_weixin_install_title));
//						} else if (!weixinApi.isWXAppSupportAPI()) {
//							showInstallWeixinDialog(mActivity
//									.getString(R.string.videoinfo_share_use_weixin_upinstall_title));
//						} else {
//							sendSeqToWeixin(mActivity,null!=loadedImage?new BitmapDrawable(loadedImage):null,
//									videoInfo.getTitle(), null != shareVideoInfo ? shareVideoInfo.getShowName()
//											: mActivity.getString(R.string.share_video_title), videoInfo
//											.getWeburl(), true);
//						}
//					} else {
//						Intent targetIntent = new Intent();
//						String pkg = selfResolveInfo.resolveInfo.activityInfo.packageName;
//						String cls = selfResolveInfo.resolveInfo.activityInfo.name;
//						targetIntent.setComponent(new ComponentName(pkg, cls));
//						targetIntent.setAction(Intent.ACTION_SEND);
//						// 设置MIME数据类型
//						// targetIntent.setType("text/plain");
//						if (hasNeedFilterApp(pkg)) {
//							if (ShareConfig.PACKAGE_NAME_SINA_WEIBO.equals(pkg)) {
//
//							}
//							targetIntent.setType("image/png");
//							targetIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//							File dir = Environment
//									.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//							File file = new File(dir + "/youku_phone_share_cache/", YoukuUtil
//									.md5(ShareAppUtil.imageUrl) + ".png");
//							Uri screenshotUri = Uri.fromFile(file);
//							// Uri screenshotUri = Uri.fromFile(new
//							// File("file:///storage/sdcard0/test.jpg"));
//							// Uri screenshotUri = Uri.fromFile(new
//							// File(getSdcardFile(ShareAppUtil.imageUrl)));
//							targetIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
//
//						} else {
//							targetIntent.setType("text/plain");
//							targetIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//						}
//						// 设置主题
//						targetIntent.putExtra(Intent.EXTRA_SUBJECT, mActivity.getString(R.string.share));
//						// 设置内容
//						targetIntent.putExtra(Intent.EXTRA_TEXT,
//								videoInfo.getTitle());
////						targetIntent.putExtra(Intent.ACTION_VIEW,
////								Uri.parse(videoInfo.getWeburl()));
//						try {
//							mActivity.startActivityForResult(targetIntent, RESULT_SELECT_FILE);
//							dialog.setPerformChooserAction(true);
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//
//					}
//
//					dialog.dismissPopuwindow();
//				}
//				// }
//			}
//
//		});
//		dialog.showAsDropDown(mView);
//	}
//
//	/**
//	 * 生成最终的第三方分享App列表
//	 * 
//	 * @param target
//	 * @return
//	 */
//	private List<ShareResolveInfo> getFliteredResolveInfoList(Intent target) {
//		List<ResolveInfo> targetedResolveInfoList = getFilterResolveInfoList(target);
//		List<ShareResolveInfo> targetedSelfResolveInfos = new ArrayList<ShareResolveInfo>();
//
//		ShareResolveInfo weixinResolveInfo = new ShareResolveInfo();
//		weixinResolveInfo.resolveInfoDrawable = mActivity.getResources().getDrawable(R.drawable.share_weixin_icon);
//		weixinResolveInfo.resolveInfoTitle = mActivity.getResources().getString(R.string.share_third_weixin);
//		weixinResolveInfo.resolvePackageName = ShareConfig.WEIXIN_PACKAGE_NAME;
//		targetedSelfResolveInfos.add(weixinResolveInfo);
//
//		ShareResolveInfo weixinfriendResolveInfo = new ShareResolveInfo();
//		weixinfriendResolveInfo.resolveInfoDrawable = mActivity.getResources().getDrawable(
//				R.drawable.share_weixin_time_line_icon);
//		weixinfriendResolveInfo.resolveInfoTitle = mActivity.getResources().getString(
//				R.string.share_third_weixin_timeline);
//		weixinfriendResolveInfo.resolvePackageName = ShareConfig.WEIXIN_FRIEND_PACKAGE_NAME;
//		targetedSelfResolveInfos.add(weixinfriendResolveInfo);
//
//		if (null != targetedResolveInfoList && targetedResolveInfoList.size() > 0) {
//			for (int i = 0; i < targetedResolveInfoList.size(); i++) {
//				targetedSelfResolveInfos.add(getSelfResloveInfo(targetedResolveInfoList.get(i)));
//			}
//		}
//
//		return targetedSelfResolveInfos;
//	}
//
//	/**
//	 * 去重
//	 * */
//	private List<ResolveInfo> removeRepeatInfo(List<ResolveInfo> infos) {
//		if (null == infos || infos.isEmpty())
//			return null;
//		List<ResolveInfo> resultResolveInfos = new ArrayList<ResolveInfo>();
//		resultResolveInfos.add(infos.get(0));
//		for (int i = 1; i < infos.size(); i++) {
//			boolean have = false;
//			for (int j = resultResolveInfos.size() - 1; j >= 0; j--) {
//				if ((infos.get(i).activityInfo.packageName)
//						.equals(resultResolveInfos.get(j).activityInfo.packageName)) {
//					have = true;
//					break;
//				}
//			}
//			if (!have) {
//				resultResolveInfos.add(infos.get(i));
//			}
//		}
//		return resultResolveInfos;
//	}
//
//	/**
//	 * 过滤掉不显示的第三方分享app
//	 * 
//	 * @param target
//	 * @return
//	 */
//	private List<ResolveInfo> getFilterResolveInfoList(Intent target) {
//		List<ResolveInfo> targetedResolveInfos = mActivity.getPackageManager().queryIntentActivities(target, 0);
//		targetedResolveInfos = removeRepeatInfo(targetedResolveInfos);
//		List<ResolveInfo> targetArrayList = new ArrayList<ResolveInfo>();
//		if (null != targetedResolveInfos && targetedResolveInfos.size() > 0) {
//			targetArrayList.addAll(targetedResolveInfos);
//			for (int i = 0; i < targetArrayList.size(); i++) {
//				ResolveInfo resolveInfo = targetArrayList.get(i);
//				if (mActivity.getPackageName().equals(resolveInfo.activityInfo.packageName)) {
//					targetArrayList.remove(resolveInfo);
//				}
//
//				if (ShareConfig.WEIXIN_PACKAGE_NAME.equals(resolveInfo.activityInfo.packageName)) {
//					targetArrayList.remove(resolveInfo);
//				}
//
//				if (ShareConfig.PACKAGE_NAME_MMS.equals(resolveInfo.activityInfo.packageName)) {
//					targetArrayList.remove(resolveInfo);
//				}
//
//				if (ShareConfig.PACKAGE_NAME_BLUETOOH.equals(resolveInfo.activityInfo.packageName)) {
//					targetArrayList.remove(resolveInfo);
//				}
//
//				if (ShareConfig.PACKAGE_NAME_BLUETOOTH_MEDIATEK.equals(resolveInfo.activityInfo.packageName)) {
//					targetArrayList.remove(resolveInfo);
//				}
//
//				if (ShareConfig.PACKAGE_NAME_FILE_SHARE_CLIENT.equals(resolveInfo.activityInfo.packageName)) {
//					targetArrayList.remove(resolveInfo);
//				}
//
//				if (ShareConfig.PACKAGE_NAME_MEMO.equals(resolveInfo.activityInfo.packageName)) {
//					targetArrayList.remove(resolveInfo);
//				}
//
//			}
//		}
//		return targetArrayList;
//	}
//
//	/**
//	 * 获取自定义的ResolveInfo
//	 * 
//	 * @param resolveInfo
//	 * @return
//	 */
//	public ShareResolveInfo getSelfResloveInfo(ResolveInfo resolveInfo) {
//		ShareResolveInfo selfResolveInfo = new ShareResolveInfo();
//		selfResolveInfo.resolvePackageName = resolveInfo.activityInfo.packageName;
//		selfResolveInfo.resolveInfo = resolveInfo;
//		selfResolveInfo.resolveInfoDrawable = resolveInfo.activityInfo.applicationInfo.loadIcon(mActivity
//				.getPackageManager());
//		selfResolveInfo.resolveInfoTitle = (String) resolveInfo.activityInfo.applicationInfo.loadLabel(mActivity
//				.getPackageManager());
//		return selfResolveInfo;
//	}
//
//	/**
//	 * 判断是否是需要被过滤的app
//	 * 
//	 * @param pkNameStr
//	 * @return
//	 */
//	public boolean hasNeedFilterApp(String pkNameStr) {
//		// if(ShareConfig.PACKAGE_NAME_KAIXIN.equals(pkNameStr)){
//		// return true;
//		// }else
//		if (ShareConfig.PACKAGE_NAME_QZONE.equals(pkNameStr)) {
//			return true;
//		} else if (ShareConfig.PACKAGE_NAME_RENREN.equals(pkNameStr)) {
//			return true;
//		} else if (ShareConfig.PACKAGE_NAME_SINA_WEIBO.equals(pkNameStr)) {
//			return true;
//		} else {
//			return false;
//		}
//	}

	/**
	 * 注册主客户端到微信
	 * 
	 * @Title: registerToWeixin
	 * @return void
	 */
	public void registerToWeixin() {
		weixinApi = WXAPIFactory.createWXAPI(mActivity, ShareConfig.WEIXIN_APP_ID);
		weixinApi.registerApp(ShareConfig.WEIXIN_APP_ID);
	}

//	/**
//	 * 发送微信
//	 * 
//	 * @Title: sendSeqToWeixin
//	 * @param bitmap
//	 * @param title
//	 * @param description
//	 * @param pageUrl
//	 * @param isTimeline
//	 *            是否分享到朋友圈
//	 * @return void
//	 */
//	public void sendSeqToWeixin(Context context, Drawable drawable, String titleStr, String descriptionContent,
//			String videoUrl, boolean isTimeline) {
//
//		BitmapDrawable bd = null;
//
//		WXVideoObject videoObj = new WXVideoObject();
//		videoObj.videoUrl = videoUrl;
//
//		WXMediaMessage msg = new WXMediaMessage(videoObj);
//		// msg.title = getString(R.string.video_info_share_weixin_title);
//		// msg.title = mActivity.getResources().getString(R.string.share_video);
//		// msg.description = descriptionContent;
//		msg.title = titleStr;
//		msg.description = descriptionContent;
//
//		if (null != drawable) {
//			bd = (BitmapDrawable) drawable;
//		} else {
//			bd = (BitmapDrawable) context.getResources().getDrawable(R.drawable.webview_share_default_icon);
//		}
//		Bitmap bitmap = bd.getBitmap();
//		Bitmap thumbBmp = Bitmap
//				.createScaledBitmap(bitmap, WEIXIN_WIDTH_THUMB_SIZE, WEIXIN_HEIGHT_THUMB_SIZE, true);
//		msg.thumbData = bmpToByteArray(thumbBmp, false);
//
//		SendMessageToWX.Req req = new SendMessageToWX.Req();
//		req.transaction = buildTransaction("image");
//		req.message = msg;
//		req.scene = isTimeline ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
//		weixinApi.sendReq(req);
//	}
//
//	public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
//		ByteArrayOutputStream output = new ByteArrayOutputStream();
//		bmp.compress(CompressFormat.PNG, 100, output);
//		if (needRecycle) {
//			bmp.recycle();
//		}
//
//		byte[] result = output.toByteArray();
//		try {
//			output.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return result;
//	}
//
//	private String buildTransaction(final String type) {
//		return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
//	}
//
	public ChooserPopuwindow getChooserPopuwindow() {
		return dialog != null ? dialog : new ChooserPopuwindow(interactView, titleLayout, controlLayout,
				isFullScreen, mActivity, isActionBar);
	}
//
//	public boolean isWeixinInstall() {
//		if (!weixinApi.isWXAppInstalled()) {
//			showInstallWeixinDialog(getString(R.string.videoinfo_share_use_weixin_install_title));
//			return false;
//		} else if (!weixinApi.isWXAppSupportAPI()) {
//			showInstallWeixinDialog(getString(R.string.videoinfo_share_use_weixin_upinstall_title));
//			return false;
//		}
//		return true;
//	}
//
//	/**
//	 * 显示安装微信的dialog
//	 * 
//	 * @param str
//	 */
//	public void showInstallWeixinDialog(String str) {
//
//		AlertDialog aDialog = new AlertDialog.Builder(mActivity).setTitle(mActivity.getString(R.string.prompt))
//				.setIcon(android.R.drawable.ic_dialog_info).setMessage(str)
//				.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog, int whichButton) {
//						dialog.dismiss();
//
//						Uri uri = Uri.parse(ShareConfig.WEIXIN_INSTALL_URL);
//						Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//						mActivity.startActivity(intent);
//
//					}
//				}).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog, int whichButton) {
//
//						dialog.dismiss();
//
//					}
//				}).setCancelable(false).show();
//
//	}
}
