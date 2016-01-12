package com.youku.login.upload;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.widget.RemoteViews;

import com.youku.login.util.Youku;
import com.youku.login.util.YoukuUtil;


	
//public class UploadProcessor extends UploadBase {
//
//	private File file;
//	private volatile UploadInfo info;
//	private volatile NotificationManager nm;
//	private volatile Notification n;
//	private volatile PowerManager.WakeLock wakeLock;
//	private volatile WifiManager.WifiLock wifiLock;
//	private volatile Context context;
//
//	/**
//	 * 在一次自动上传session中，记录失败的任务，防止重复重试失败的任务
//	 */
//	private static List<String> ON_SESSION_ERROR = new ArrayList<String>();
//
//	/**
//	 * 初始化一个UploadProcessor
//	 * 
//	 * @Title: UploadProcessor
//	 * @param info
//	 * @return void
//	 * @date 2012-7-12 下午4:06:03
//	 */
//	public UploadProcessor(UploadInfo info) {
//		if (info != null)
//			this.info = info.clone();
//		if (UploadConfig.DEBUG_MODE_OPENED)
//			UploadUtil.out("UploadProcessor::新任务->" + info.getTitle());
//		context = UploadConfig.getContext();
//	}
//
//	/**
//	 * 初始化一次上传轮巡
//	 * 
//	 * @Title: UploadProcessor
//	 * @param info
//	 * @param session
//	 * @return void
//	 * @date 2012-7-12 下午4:07:10
//	 */
//	public UploadProcessor(UploadInfo info, long session) {
//		if (info != null)
//			this.info = info.clone();
//		context = UploadConfig.getContext();
//		this.session = session;
//	}
//
//	public void run() {
//		if (info == null || info.getFilePath() == null
//				|| "".equals(info.getFilePath())) {
//			return;
//		}
//
//		if (info.getStatus() == UploadInfo.STATE_CANCEL) {
//			UploadConfig.showTips(UploadConfig.R_STRING_9);
//			updateUI(info);
//			return;
//		}
//
//		if (isUploadingTask(info)) {
//			updateUI(info);
//			return;
//		}
//
//		if (!UploadConfig.hasInternet()) {// 无网时点继续上传
//			info.setStatus(UploadInfo.STATE_WAIT);
//			SESSION_ERROR_TASKS.remove(info.getTaskId());
//			updateAll(context.getString(UploadConfig.R_STRING_2));
//			return;
//		}
//
//		if (!UploadConfig.uploadSettingIsOk()) {
//			UploadConfig.showTips(UploadConfig.R_STRING_10);
//			info.setStatus(UploadInfo.STATE_WAIT);
//			SESSION_ERROR_TASKS.remove(info.getTaskId());
//			updateAll(context.getString(UploadConfig.R_STRING_2));
//			return;
//		}
//
//		if (UPLOADING_TASK != null) {// 如果当前有正在上传的任务，就置为等待
//			info.setStatus(UploadInfo.STATE_WAIT);
//			SESSION_ERROR_TASKS.remove(info.getTaskId());
//			updateAll(context.getString(UploadConfig.R_STRING_2));
//			return;
//		}
//
//		UPLOADING_TASK = info;// 设置正在上传的任务，防止有其他任务上传
//
//		if (!prepare()) {
//			UPLOADING_TASK = null;
//			if (info.getStatus() != UploadInfo.STATE_CANCEL)// 排除上传彻底失败的任务
//				info.setStatus(UploadInfo.STATE_EXCEPTION);
//			updateAll(context.getString(UploadConfig.R_STRING_2));
//			startUploadTask(session, info);
//			return;
//		}
//
//		if (!info.isInstantUpload()) {
//			if (isContinueTask) {
//				UPLOADING_TASK = info;
//				int checkResult = check();
//				if (checkResult == CHECK_RESULT_UNFINISH) {
//					upload();
//				} else if (checkResult == CHECK_RESULT_FINISHED) {
//					if (!finish(false))
//						info.setStatus(UploadInfo.STATE_EXCEPTION);
//					UPLOADING_TASK = null;
//					startUploadTask(session, info);
//				} else {
//					updateNotification(context
//							.getString(UploadConfig.R_STRING_2));
//				}
//			} else {
//				upload();
//			}
//		} else {// 秒传直接commit
//			if (!finish(false))
//				info.setStatus(UploadInfo.STATE_EXCEPTION);
//			UPLOADING_TASK = null;
//			startUploadTask(session, info);
//		}
//	}
//
//	@Override
//	protected boolean prepare() {
//		// TODO Auto-generated method stub
//		UploadApi.reset();
//		if (!"".equals(UploadConfig.getUploadAccessToken())) {// 获取access_token
//			UploadApi.access_token = UploadConfig.getUploadAccessToken();
//		} else {
//			if (UploadConfig.DEBUG_MODE_OPENED)
//				UploadUtil.out("第一步：获取access_token");
//			if (!UploadApi.login()) {
//				info.setExceptionCode(ERROR_CODE_OPENAPI_LOGIN);
//
//				if (UploadApi.ERROR_CODE == UploadApi.LOGIN_ERROR_1)
//					UploadConfig.showTips(UploadConfig.R_STRING_5);
//
//				return false;
//			}
//		}
//
//		file = new File(info.getFilePath());
//		if (!file.exists()) {
//			info.setExceptionCode(ERROR_CODE_FILE_NOT_FOUND);
//			UploadConfig.showTips(UploadConfig.R_STRING_6);
//			return false;
//		}
//
//		UploadInfo dbInfo = UploadDB.getItem(info.getTaskId());
//		if (dbInfo != null) {
//			long cacheSize = dbInfo.getSize();
//			info = dbInfo.clone();
//			info.setSize(file.length());
//			UPLOADING_TASK = info;// 更新对象
//			if (cacheSize == info.getSize() && info.getMd5() != null) {
//				if (UploadConfig.DEBUG_MODE_OPENED)
//					UploadUtil.out("<!--续传任务-->");
//				UploadApi.upload_token = info.getUploadToken();
//
//				if (!getUploadServerIp())
//					return false;
//
//				if (!info.isCreatedFile()) {// 检查create_file是否成功
//					if (!create_file()) {
//						return false;
//					}
//				}
//				isContinueTask = true;
//				return true;
//			}
//		} else
//			info.setSize(file.length());
//
//		if (UploadConfig.DEBUG_MODE_OPENED)
//			UploadUtil.out("第二步： create");
//		long time = 0l;
//		if (UploadConfig.DEBUG_MODE_OPENED)
//			time = System.currentTimeMillis();
//		String md5 = UploadUtil.getFileMD5String(info.getFilePath());
//		if (UploadConfig.DEBUG_MODE_OPENED)
//			UploadUtil.out("MD5耗时::" + (System.currentTimeMillis() - time)
//					/ 1000);
//		if (!UploadApi
//				.create(info.getTitle(),
//						info.getTag(),
//						UploadConfig.PRIVACY_MAP.get(info.getPrivacy()),
//						UploadConfig.COPYRIGHT_ORIGINAL,
//						(info.getVideoPassword() == null ? "" : info
//								.getVideoPassword()),
//						"", // 简介
//						md5,
//						// info.getFileName(),
//						(info.getFileName() == null ? "" : info.getFileName()),
//						info.getSize(), UploadConfig.CATEGORY_ORIGINAL, info
//								.getLatitude(), info.getLongitude())) {
//
//			if (UploadApi.ERROR_CODE == UploadApi.CREATE_ERROR_1) {
//				UploadConfig.showTips(UploadConfig.R_STRING_8);
//				info.setStatus(UploadInfo.STATE_CANCEL);// 设为不可上传状态
//			}
//			info.setExceptionCode(ERROR_CODE_OPENAPI_CREATE);
//			return false;
//		} else {
//			new Thread(new Runnable() {
//				@Override
//				public void run() {
//					// TODO Auto-generated method stub
//					UploadConfig.googleStatCreate();
//					UploadConfig.youkuStatCreate();
//				}
//			}).start();
//		}
//		info.setMd5(md5);
//		info.setUploadToken(UploadApi.upload_token);
//		info.setTargetHost(UploadApi.upload_server_uri);
//		info.setInstantUpload("yes".equals(UploadApi.instant_upload_ok + ""));
//		UploadDB.update(info);
//		if (info.isInstantUpload())
//			return true;
//
//		if (!getUploadServerIp())
//			return false;
//
//		if (!create_file()) {
//			return false;
//		}
//
//		return true;
//	}
//
//	@Override
//	protected void upload() {
//		// TODO Auto-generated method stub
//		initListener();
//		keepNetConnecting();
//		info.setStatus(UploadInfo.STATE_INIT);
//		info.setStatus(UploadInfo.STATE_UPLOADING);
//
//		if (isContinueTask) {
//			List<SliceInfo> slices = UploadApi.slices();
//			int[] sliceIds = new int[slices.size()];
//			if (slices != null && slices.size() > 0) {
//				int i = 0;
//				for (SliceInfo info : slices) {
//					sliceIds[i] = info.slice_task_id;
//					i++;
//				}
//				String ids = YoukuUtil.join(sliceIds);
//				if (UploadConfig.DEBUG_MODE_OPENED)
//					UploadUtil.out("重置分片任务->" + ids);
//				UploadApi.resetSlice(ids);
//			}
//		}
//
//		for (int i = 0; i < UploadConfig.MAX_THREAD_COUNT; i++) {
//			if (UploadConfig.DEBUG_MODE_OPENED)
//				UploadUtil.out("线程启动->" + (i + 1));
//			new Thread(new Runnable() {
//
//				@Override
//				public void run() {
//					// TODO Auto-generated method stub
//					boolean uploadResult = uploadSlice(null);
//					if (currentThreadCount == 0) {// 当线程都结束
//						releaseNetLock();
//						if (uploadResult) {
//							if (!finish(true))
//								info.setStatus(UploadInfo.STATE_EXCEPTION);
//							UPLOADING_TASK = null;
//							startUploadTask(session, info);
//						} else {
//							if (info.getStatus() != UploadInfo.STATE_PAUSE
//									&& info.getStatus() != UploadInfo.STATE_CANCEL) {
//								info.setStatus(UploadInfo.STATE_EXCEPTION);
//							}
//							UPLOADING_TASK = null;
//							startUploadTask(session, info);
//						}
//					} else {// 有活的线程，其中一个失败，就暂停任务
//						if (!uploadResult)
//							UPLOADING_TASK = null;
//						if (!uploadResult
//								&& info.getStatus() != UploadInfo.STATE_PAUSE
//								&& info.getStatus() != UploadInfo.STATE_CANCEL) {
//							info.setStatus(UploadInfo.STATE_EXCEPTION);
//						}
//					}
//					updateUI(info);
//				}
//			}).start();
//
//			if (info.getSize() <= info.getSliceSize() * 1024)// 如果只有一个分片，只启动一个线程
//				break;
//		}
//	}
//
//	private boolean uploadSlice(SliceInfo nextSlice) {
//		currentThreadCount++;
//		if (nextSlice == null) {
//			if (UploadConfig.DEBUG_MODE_OPENED)
//				UploadUtil.out("第四步： new_slice");
//			nextSlice = UploadApi.new_slice();
//			if (nextSlice == null) {
//				info.setExceptionCode(ERROR_CODE_IKU_NEW_SLICE);
//				currentThreadCount--;
//				return false;
//			}
//			if (nextSlice.slice_task_id == 0) {// 如果没有分片，则认为已经传完
//				currentThreadCount--;
//				return true;
//			}
//		}
//
//		if (UploadConfig.DEBUG_MODE_OPENED)
//			UploadUtil.out("第五步： upload_slice");
//		if (!uploadData(nextSlice)) {
//			info.setExceptionCode(ERROR_CODE_IKU_UPLOAD_SLICE);
//			currentThreadCount--;
//			return false;
//		}
//		currentThreadCount--;
//		return true;
//	}
//
//	@Override
//	protected boolean finish(boolean needCheck) {
//		// TODO Auto-generated method stub
//		if (info.getUploadListener() == null)
//			initListener();
//
//		if (needCheck) {
//			if (check() != CHECK_RESULT_FINISHED) {
//				info.setExceptionCode(ERROR_CODE_IKU_CHECK);
//				return false;
//			}
//		}
//
//		if (UploadConfig.DEBUG_MODE_OPENED)
//			UploadUtil.out("第七步： commit");
//		if (!UploadApi.commit()) {
//			info.setExceptionCode(ERROR_CODE_OPENAPI_COMMIT);
//			return false;
//		}
//
//		info.setStatus(UploadInfo.STATE_UPLOADED);
//		delete(info);
//		return true;
//	}
//
//	private boolean create_file() {
//		if (UploadConfig.DEBUG_MODE_OPENED)
//			UploadUtil.out("第三步： create_file");
//		int sliceSize = UploadConfig.getSliceSize();
//		if (!UploadApi.create_file(info.getSize(), info.getFilePostfix(),
//				sliceSize)) {
//			info.setExceptionCode(ERROR_CODE_IKU_CREATE_FILE);
//			return false;
//		}
//		info.setSliceSize(sliceSize);
//		info.setCreatedFile(true);
//		UploadDB.update(info);
//		return true;
//	}
//
//	private boolean uploadData(SliceInfo sliceInfo) {
//		boolean hasNext = true;
//		byte[] data = null;
//		while (hasNext) {
//			if (info == null || info.getStatus() == UploadInfo.STATE_PAUSE
//					|| info.getStatus() == UploadInfo.STATE_CANCEL
//					|| info.getStatus() == UploadInfo.STATE_EXCEPTION
//					|| info.getStatus() == UploadInfo.STATE_WAIT) {
//				return false;
//			}
//			UPLOADING_TASK = info;
//			if (UploadConfig.DEBUG_MODE_OPENED)
//				UploadUtil.out("UploadProcessor::upload->从 " + sliceInfo.offset
//						/ 1024 + "KB 读取了 " + sliceInfo.length / 1024 + "KB");
//
//			data = new byte[sliceInfo.length];
//			FileInputStream fis = null;
//			try {
//				fis = new FileInputStream(file);
//				fis.skip(sliceInfo.offset);
//				fis.read(data);
//				fis.close();
//			} catch (IOException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//				return false;
//			}
//
//			// File folder = new File(Environment.getExternalStorageDirectory()
//			// + "/youku/debug/" + info.getUploadToken());
//			// if(!folder.exists() || !folder.isDirectory()){
//			// folder.mkdirs();
//			// }
//			// try{
//			// FileOutputStream fos = new
//			// FileOutputStream(Environment.getExternalStorageDirectory() +
//			// "/youku/debug/" + info.getUploadToken() + "/" +
//			// sliceInfo.slice_task_id);
//			// fos.write(data);
//			// fos.close();
//			// } catch (FileNotFoundException e1) {
//			// // TODO Auto-generated catch block
//			// e1.printStackTrace();
//			// } catch (IOException e) {
//			// // TODO Auto-generated catch block
//			// e.printStackTrace();
//			// }
//			sliceInfo = UploadApi.upload_slice(sliceInfo, data);
//			if (sliceInfo == null) {
//				try {
//					if (fis != null)
//						fis.close();
//					fis = null;
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				return false;
//			}
//
//			if (info.getUploadedSize() < sliceInfo.transferred)// 防止线程先后结束导致进度乱跳，保证进度总是在增长
//				info.setUploadedSize(sliceInfo.transferred);
//
//			if (sliceInfo.finished || sliceInfo.slice_task_id == 0) {
//				hasNext = false;
//				info.setUploadedSize(info.getSize());
//				try {
//					if (fis != null)
//						fis.close();
//					fis = null;
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				if (UploadConfig.DEBUG_MODE_OPENED)
//					UploadUtil
//							.out("上传总耗时->"
//									+ (System.currentTimeMillis() - info
//											.getStartTime()) / 1000 + "秒");
//				return true;
//			}
//		}
//		return false;
//	}
//
//	@SuppressWarnings("static-access")
//	private int check() {
//		if (UploadConfig.DEBUG_MODE_OPENED)
//			UploadUtil.out("Check...");
//		if (info.isInstantUpload())// 如果是秒传就不用check
//			return CHECK_RESULT_FINISHED;
//		if (!UploadApi.check())
//			return CHECK_RESULT_ERROR;
//		if (UploadApi.status == 1) {// 完全成功
//			return CHECK_RESULT_FINISHED;
//		} else if (UploadApi.status == 2) {// 上传进度
//			if (UploadApi.transferred_percent == 100) {
//				try {
//					Thread.currentThread().sleep(2000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				return check();
//			} else {
//				return CHECK_RESULT_UNFINISH;
//			}
//		} else if (UploadApi.status == 3) {// 写入磁盘进度
//			if (UploadApi.confirmed_percent == 100)
//				return CHECK_RESULT_FINISHED;
//			else {
//				try {
//					Thread.currentThread().sleep(2000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				return check();
//			}
//		} else if (UploadApi.status == 4) {// 有分片未传完
//			return CHECK_RESULT_UNFINISH;
//		}
//		return CHECK_RESULT_ERROR;
//	}
//
//	@Override
//	protected void updateUI(UploadInfo info) {
//		// TODO Auto-generated method stub
//		Intent intent = new Intent(UploadConfig.UPDATE_UI_BROADCAST_NAME);
//		Bundle mBundle = new Bundle();
//		mBundle.putParcelable(UploadInfo.class.getName(), info);
//		intent.putExtras(mBundle);
//		context.sendBroadcast(intent);
//	}
//
//	/**
//	 * 更新DB及UI，和通知（暂停和等待、异常时） 3.0版本没有用这个at all
//	 * 
//	 * @Title: sendBroadCast
//	 * @return void
//	 * @date 2012-3-8 下午9:59:12
//	 */
//	public static void updateDBAndUIAndNitification(UploadInfo info) {
//		if (info.getStatus() == UploadInfo.STATE_PAUSE
//				|| info.getStatus() == UploadInfo.STATE_WAIT
//				|| info.getStatus() == UploadInfo.STATE_EXCEPTION) {
//			String notifiStatusText = "";
//			boolean canClear = false;
//			if (info.getStatus() == UploadInfo.STATE_PAUSE) {
//				notifiStatusText = Youku.context.getString(R.string.pause);
//			} else if (info.getStatus() == UploadInfo.STATE_WAIT) {
//				notifiStatusText = Youku.context.getString(R.string.wait);
//			} else {
//				notifiStatusText = Youku.context.getString(R.string.wait);
//				canClear = true;
//				ON_SESSION_ERROR.add(info.getTaskId());
//			}
//			sendOnceNotification(info, "", notifiStatusText, canClear, false);
//		}
//		UploadDB.update(info);
//		sendBroadCast(info);
//	}
//
//	/**
//	 * 发送广播更新我的本地视频UI
//	 * 
//	 * @Title: sendBroadCast
//	 * @return void
//	 * @date 2012-3-8 下午9:59:12
//	 */
//	public static void sendBroadCast(UploadInfo info) {
//		Logger.e(Youku.TAG_GLOBAL, "UploadProcessor#sendBroadCast()");
//		Intent intent = new Intent(MyUploadPageActivity.BROADCAST_UPLOAD_CHANGE);
//		Bundle mBundle = new Bundle();
//		mBundle.putParcelable(UploadInfo.class.getName(), info);
//		intent.putExtras(mBundle);
//		Youku.context.sendBroadcast(intent);
//	}
//
//	/**
//	 * 创建一个零时的notification
//	 * 
//	 * @Title: createNotification
//	 * @param info
//	 * @param barText
//	 *            状态栏文字
//	 * @param notify_state
//	 *            上传状态
//	 * @param playSound
//	 *            是否可被手动清除
//	 * @param iconRunning
//	 *            状态栏图标是否动画
//	 * @return void
//	 * @date 2012-3-30 下午2:03:58
//	 */
//	private static void sendOnceNotification(UploadInfo info, String barText,
//			String notify_state, boolean playSound, boolean iconRunning) {
//		NotificationManager nm = (NotificationManager) Youku.context
//				.getSystemService(Context.NOTIFICATION_SERVICE);
//		Notification n = new Notification();
//		n.icon = iconRunning ? android.R.drawable.stat_sys_upload
//				: android.R.drawable.stat_sys_upload_done;
//		n.flags = playSound ? Notification.FLAG_AUTO_CANCEL
//				: Notification.FLAG_NO_CLEAR;
//		n.tickerText = barText;
//
//		Intent intent = new Intent(Youku.context, MyUploadPageActivity.class);
//		intent.putExtra("taskID", info.getTaskId());
//		n.contentIntent = PendingIntent.getActivity(Youku.context, 4, intent,
//				PendingIntent.FLAG_UPDATE_CURRENT);
//		n.contentView = new RemoteViews(Youku.context.getPackageName(),
//				R.layout.upload_notify);
//
//		n.contentView.setTextViewText(R.id.notify_text, info.getTitle());
//		n.contentView.setTextViewText(R.id.notify_state, notify_state);
//		n.contentView.setProgressBar(
//				R.id.notify_processbar,
//				100,
//				info.getStatus() == UploadInfo.STATE_UPLOADED ? 100 : info
//						.getProgress(),
//				info.getStatus() == UploadInfo.STATE_WAIT
//						|| info.getStatus() == UploadInfo.STATE_PAUSE);
//		nm.notify(Integer.parseInt(info.getTaskId()), n);
//		if (info.getStatus() == UploadInfo.STATE_PAUSE) {
//			nm.cancel(Integer.parseInt(info.getTaskId()));
//		}
//	}
//
//	@Override
//	protected void initListener() {
//		// TODO Auto-generated method stub
//		info.setUploadListener(new UploadListener() {
//
//			@Override
//			public void onStart() {
//				// TODO Auto-generated method stub
//				if (info.getStartTime() == 0l)
//					info.setStartTime(System.currentTimeMillis());
//
//				updateAll(context.getString(UploadConfig.R_STRING_1) + " - "
//						+ info.getProgress() + "%");
//
//				if (!UploadConfig.isAlertedNet && !UploadConfig.isWifi()) {
//					UploadConfig.isAlertedNet = true;
//					UploadConfig.showTips(UploadConfig.R_STRING_7);
//				}
//				broadCastNewTaskStart(info);
//				new UploadSpeedThread().start();
//			}
//
//			@Override
//			public void onPause() {
//				// TODO Auto-generated method stub
//				forceCloseConn();
//				updateAll(context.getString(UploadConfig.R_STRING_3));
//				nm.cancel(Integer.parseInt(info.getTaskId()));
//			}
//
//			@Override
//			public void onWait() {
//				// TODO Auto-generated method stub
//				forceCloseConn();
//				updateAll(context.getString(UploadConfig.R_STRING_2));
//			}
//
//			@Override
//			public void onException() {
//				// TODO Auto-generated method stub
//				forceCloseConn();
//				updateAll(context.getString(UploadConfig.R_STRING_2));
//			}
//
//			@SuppressWarnings("static-access")
//			@Override
//			public void onFinish() {
//				// TODO Auto-generated method stub
//				delete(info);
//
//				updateNotification(context.getString(UploadConfig.R_STRING_4));
//
//				UploadConfig.youkuStatFinish(info, UploadApi.video_id);
//
//				new Thread(new Runnable() {
//					@Override
//					public void run() {
//						// TODO Auto-generated method stub
//						UploadInfo cacheInfo = info.clone();
//						try {// 等服务器刷出数据
//							Thread.currentThread().sleep(2000);
//						} catch (InterruptedException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//
//						broadCastTaskFinish(cacheInfo);
//						updateUI(cacheInfo);
//					}
//				}).start();
//			}
//
//			@Override
//			public void onCancel() {
//				// TODO Auto-generated method stub
//				delete(info);
//				forceCloseConn();
//				nm.cancel(Integer.parseInt(info.getTaskId()));
//				info.setDesc("delete");// temp
//				updateUI(info);
//			}
//
//			@Override
//			public void onProgressChange() {
//				// TODO Auto-generated method stub
//				long time = System.currentTimeMillis();
//				if (info.getProgress() != 100
//						&& info.getStatus() == UploadInfo.STATE_UPLOADING
//						&& info.getLastUpdateTime() + 2000 >= time)
//					return;
//
//				info.setLastUpdateTime(time);
//				updateAll(context.getString(UploadConfig.R_STRING_1) + " - "
//						+ info.getProgress() + "%");
//			}
//
//			@Override
//			public void onUploadSpeedChange() {
//				// TODO Auto-generated method stub
//				updateNotification(context.getString(UploadConfig.R_STRING_1)
//						+ " - " + info.getProgress() + "%");
//				updateUI(info);
//			}
//
//		});
//	}
//
//	@Override
//	protected void updateNotification(String notificationText) {
//		// TODO Auto-generated method stub
//		if (n == null) {
//			nm = (NotificationManager) context
//					.getSystemService(Context.NOTIFICATION_SERVICE);
//			n = new Notification();
//
//			// 更新界面
//
//			Intent intent = new Intent(context, MyUploadPageActivity.class);
//			intent.putExtra("taskID", info.getTaskId());
//			n.contentIntent = PendingIntent.getActivity(context, 4, intent,
//					PendingIntent.FLAG_UPDATE_CURRENT);
//			n.contentView = new RemoteViews(context.getPackageName(),
//					R.layout.upload_notify);
//		}
//
//		n.icon = android.R.drawable.stat_sys_upload_done;
//		n.flags = !info.stateMaybeChange() ? Notification.FLAG_AUTO_CANCEL
//				: Notification.FLAG_NO_CLEAR;
//		n.tickerText = null;
//		if (info.getStatus() == UploadInfo.STATE_UPLOADED
//				&& getUploadTasks().size() == 0)
//			n.defaults = Notification.DEFAULT_SOUND;
//		n.contentView.setTextViewText(UploadConfig.R_ID_1, info.getTitle());
//		n.contentView.setTextViewText(UploadConfig.R_ID_2, notificationText);
//		// n.contentView.setTextViewText(UploadConfig.R_ID_3,
//		// info.getStatus() == UploadInfo.STATE_UPLOADING &&
//		// UploadSpeedThread.TESTING
//		// ? info.getSpeedDesc() + "/" + info.getSizeDesc() : "");
//		n.contentView.setProgressBar(
//				UploadConfig.R_ID_4,
//				100,
//				info.getStatus() == UploadInfo.STATE_UPLOADED ? 100 : info
//						.getProgress(), false);
//		nm.notify(Integer.parseInt(info.getTaskId()), n);
//
//		if (info.getStatus() == UploadInfo.STATE_UPLOADING) {
//			n.icon = android.R.drawable.stat_sys_upload;
//			nm.notify(Integer.parseInt(info.getTaskId()), n);
//		}
//		// 为了只显示当前上传中的通知（3.0需求）
//		if (info.getStatus() != UploadInfo.STATE_UPLOADING
//				&& info.getStatus() != UploadInfo.STATE_INIT) {
//			nm.cancel(Integer.parseInt(info.getTaskId()));
//		}
//	}
//
//	@Override
//	// 状态改变的时候驱动的
//	public void updateAll(String notificationText) {
//		// TODO Auto-generated method stub
//		updateUI(info);
//		UploadDB.update(info);
//		updateNotification(notificationText);
//	}
//
//	@Override
//	protected boolean getUploadServerIp() {
//		if (info.getTargetIpAddr() != null
//				&& !"".equals(info.getTargetIpAddr())) {
//			UploadUtil.out("UploadProcessor::getIpAddrByHostName->"
//					+ info.getTargetHost() + ">>" + info.getTargetIpAddr());
//			UploadApi.upload_server_uri = info.getTargetIpAddr();
//			return true;
//		}
//		try {
//			String ip = InetAddress.getByName(info.getTargetHost())
//					.getHostAddress();
//			info.setTargetIpAddr(ip);
//			// info.setTargetIpAddr("10.11.11.67:8081");
//			UploadApi.upload_server_uri = info.getTargetIpAddr();
//			UploadDB.update(info);
//			UploadUtil.out("UploadProcessor::getIpAddrByHostName->"
//					+ info.getTargetHost() + ">>" + ip);
//			return true;
//		} catch (UnknownHostException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//
//			info.setExceptionCode(ERROR_CODE_TRANS_IP);
//			return false;
//		}
//	}
//
//	@Override
//	protected void keepNetConnecting() {
//		// TODO Auto-generated method stub
//		wakeLock = ((PowerManager) context
//				.getSystemService(Context.POWER_SERVICE)).newWakeLock(
//				PowerManager.SCREEN_DIM_WAKE_LOCK,
//				"Paike_Upload_Lock_" + info.getTitle());
//		wifiLock = ((WifiManager) context
//				.getSystemService(Context.WIFI_SERVICE))
//				.createWifiLock("Paike_Upload_Lock_" + info.getTitle());
//		wakeLock.acquire();
//		wifiLock.acquire();
//	}
//
//	@Override
//	protected void releaseNetLock() {
//		// TODO Auto-generated method stub
//		if (wakeLock != null && wakeLock.isHeld())
//			wakeLock.release();
//		if (wifiLock != null && wifiLock.isHeld())
//			wifiLock.release();
//	}
//
//	public static void resetChecking() {
//		CHECKING = true;
//
//	}
//
//	/**
//	 * 退出应用后取消上传的消息框提示 登出个人账号同样动作
//	 */
//	@SuppressWarnings("static-access")
//	public static void cancelUploadNotifaction() {
//		List<UploadInfo> uploadInfos = UploadProcessor.getUploadTasks();
//		if (null != uploadInfos && uploadInfos.size() > 0) {
//			for (int i = 0; i < uploadInfos.size(); i++) {
//				uploadInfos.get(i).setStatus(UploadInfo.STATE_PAUSE);
//				((NotificationManager) Youku.context
//						.getSystemService(Youku.context.NOTIFICATION_SERVICE))
//						.cancel(Integer.valueOf(uploadInfos.get(i).getTaskId()));
//			}
//		}
//	}
//}