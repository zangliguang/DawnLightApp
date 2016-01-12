package com.youku.login.upload;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;

/**
 * UploadProcessor基类
 * @Package com.youku.paike.upload
 * @ClassName: UploadInterface
 * @author Beethoven
 * @mail zhanghuitao@youku.com
 * @date 2012-7-12 下午3:27:44
 */
abstract class UploadBase extends Thread{
	
	public static final int ERROR_CODE_DB_INSERT = 100;
	public static final int ERROR_CODE_DB_UPDATE = 101;
	public static final int ERROR_CODE_TRANS_IP = 102;
	public static final int ERROR_CODE_FILE_NOT_FOUND = 103;
	
	public static final int ERROR_CODE_OPENAPI_LOGIN = 1000;
	public static final int ERROR_CODE_OPENAPI_CREATE = 1001;
	public static final int ERROR_CODE_OPENAPI_COMMIT = 1002;
	public static final int ERROR_CODE_OPENAPI_CANCEL = 1003;
	public static final int ERROR_CODE_OPENAPI_SPEC = 1004;
	public static final int ERROR_CODE_OPENAPI_REFRESH_TOKEN = 1005;
	
	public static final int ERROR_CODE_IKU_CREATE_FILE = 2000;
	public static final int ERROR_CODE_IKU_NEW_SLICE = 2001;
	public static final int ERROR_CODE_IKU_UPLOAD_SLICE = 2002;
	public static final int ERROR_CODE_IKU_CHECK = 2003;
	
	public static final int CHECK_RESULT_ERROR = 0;
	public static final int CHECK_RESULT_FINISHED = 1;
	public static final int CHECK_RESULT_UNFINISH = 2;
	
	protected static volatile String STATUS_BAR_TEXT = "";
	protected static UploadInfo UPLOADING_TASK = null;
	protected volatile boolean isContinueTask = false;
	protected volatile int currentThreadCount = 0;
	
	/**
	 *  在一次自动上传session中，记录失败的任务，防止重复重试失败的任务
	 */
	protected static Map<String, Object> SESSION_ERROR_TASKS = new HashMap<String, Object>();
	protected long session = 0l;
	
	/**
	 * 是否正在轮巡
	 */
	protected static volatile boolean CHECKING = false;

	/**
	 * 准备工作
	 * @Title: prepare
	 * @return void
	 * @date 2012-7-12 下午6:13:54
	 */
	protected abstract boolean prepare();
	
	/**
	 * 上传文件分片
	 * @Title: upload
	 * @return void
	 * @date 2012-7-12 下午6:14:13
	 */
	protected abstract void upload();
	
	/**
	 * 完成上传
	 * @Title: finish
	 * @return void
	 * @date 2012-7-12 下午6:14:28
	 */
	protected abstract boolean finish(boolean needCheck);
	
	/**
	 * 初始化内部监听者
	 * @Title: initListener
	 * @return void
	 * @date 2012-7-16 下午5:01:43
	 */
	protected abstract void initListener();
	
	/**
	 * 当任务发生变化时，更新UI
	 * @Title: updateUI
	 * @return void
	 * @date 2012-7-12 下午4:35:17
	 */
	protected abstract void updateUI(UploadInfo info);
	
	/**
	 * 当任务发生变化时，更新通知栏
	 * @Title: updateNotification
	 * @return void
	 * @date 2012-7-12 下午4:35:47
	 */
	protected abstract void updateNotification(String notificationText);
	
	/**
	 * 更新DB、UI、NOTIFICATION
	 * @Title: updateAll
	 * @param statusBarText
	 * @param notificationText
	 * @return void
	 * @date 2012-7-17 下午10:14:11
	 */
	protected abstract void updateAll(String notificationText);
	
	/**
	 * 获得上传服务器IP地址
	 * @Title: getUploadServerIp
	 * @return
	 * @return boolean
	 * @date 2012-7-18 下午2:29:07
	 */
	protected abstract boolean getUploadServerIp();
	
	/**
	 * 开启网络锁，防止上传中网络休眠
	 * @Title: keepNetConnecting
	 * @return void
	 * @date 2012-7-12 下午4:39:18
	 */
	protected abstract void keepNetConnecting();
	
	/**
	 * 当前任务停止时（包括完成或暂停等），释放网络锁
	 * @Title: releaseNetLock
	 * @return void
	 * @date 2012-7-12 下午4:39:51
	 */
	protected abstract void releaseNetLock();
	
	/**
	 * 创建新任务
	 * @Title: add
	 * @param info
	 * @return boolean
	 * @date 2012-7-12 下午4:08:35
	 */
	public static boolean add(UploadInfo info){
		broadCastNewTaskStart(info);
		return UploadDB.insert(info);
	}
	
	/**
	 * 删除一个上传任务
	 * @Title: delete
	 * @param info
	 * @return boolean
	 * @date 2012-7-12 下午4:40:38
	 */
	public static boolean delete(UploadInfo info){
		boolean success = UploadDB.delete(info.getTaskId());
		broadCastFinish(true);
		return success;
	}
	
	/**
	 * 释放数据库
	 * @Title: releaseDB
	 * @return void
	 * @date 2012-8-14 下午5:58:07
	 */
	public static void releaseDB(){
		UploadDB.closeDB();
	}
	
	/**
	 * 删除一个用户的所有删除上传任务
	 * @Title: deleteByUser
	 * @param userId
	 * @return boolean
	 * @date 2012-7-12 下午4:41:29
	 */
	public static boolean deleteByUser(String userId){
		return UploadDB.deleteUserData(userId);
	}
	
	/**
	 * 获得正在上传的任务
	 * @Title: getUPLOADING_TASK
	 * @return UploadInfo
	 * @date 2012-7-12 下午5:41:54
	 */
	public static UploadInfo getUploadingTask() {
		return UPLOADING_TASK;
	}

	/**
	 * 获取上传队列
	 * @Title: getUploadTaskList
	 * @return List<UploadInfo>
	 * @date 2012-7-12 下午4:41:50
	 */
	public static List<UploadInfo> getUploadTasks(){
		return UploadDB.getUnFinishedItems();
	}
	
	/**
	 * 获取暂停中的任务
	 * @Title: getPausedUploadTasks
	 * @return List<UploadInfo>
	 * @date 2012-7-12 下午4:41:50
	 */
	public static List<UploadInfo> getPausedUploadTasks(){
		return UploadDB.getPausedItems();
	}
	
	/**
	 * 获取所有上传任务，不包含已完成的
	 * @Title: getTasks
	 * @return List<UploadInfo>
	 * @date 2012-7-12 下午4:41:50
	 */
	public static List<UploadInfo> getTasks(){
		return UploadDB.getList();
	}
	
	public static int getCount(){
		return UploadDB.getCount();
	}
	/**
	 * 获取所有上传任务，包含所有任务状态
	 * @Title: getAllTasks
	 * @return List<UploadInfo>
	 * @date 2012-7-12 下午4:41:50
	 */
	public static List<UploadInfo> getAllTasks(){
		return UploadDB.getListContainDelTask();
	}
	
	/**
	 * 是否正在上传的任务
	 * @Title: isUploadingTask
	 * @param info
	 * @return boolean
	 * @date 2012-7-19 下午5:42:28
	 */
	public static boolean isUploadingTask(UploadInfo info){
		if(UPLOADING_TASK != null && UPLOADING_TASK.getTaskId().equals(info.getTaskId()))
			return true;
		return false;
	}
	
	/**
	 * 强制关闭正在上传的连接
	 * @Title: forceCloseConn
	 * @return void
	 * @date 2014-9-29 下午5:12:42
	 */
//	protected void forceCloseConn(){
//		try{//防止并发修改数组异常
//			if(UploadApi.conns != null && UploadApi.conns.size() > 0){
//				for(HttpURLConnection conn : UploadApi.conns){
//					if(UploadConfig.DEBUG_MODE_OPENED)UploadUtil.out("UploadProcessor::强制关闭连接");
//					UploadApi.conns.remove(conn);
//					if(conn != null)
//						conn.disconnect();
//					conn = null;
//				}
//			}
//		}catch(java.util.ConcurrentModificationException e){
//			e.printStackTrace();
//		}
//	}
	
	/**
	 * 当队列有新增任务时发出广播
	 * @Title: broadCastNewTaskStart
	 * @param info
	 * @return void
	 * @date 2012-9-1 下午4:33:27
	 */
	public static void broadCastNewTaskStart(UploadInfo info) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(UploadConfig.UPLOAD_START_BROADCAST);
		Bundle mBundle = new Bundle();
		mBundle.putParcelable(UploadInfo.class.getName(), info);
		intent.putExtras(mBundle);
		UploadConfig.getContext().sendBroadcast(intent);
	}
	
	/**
	 * 当一个上传任务成功时发出广播
	 * @Title: broadCastFinish
	 * @param needCheck 是否需要检查上传队列为空
	 * @return void
	 * @date 2012-9-1 下午4:35:46
	 */
	public static void broadCastTaskFinish(UploadInfo info) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(UploadConfig.UPLOAD_TASK_SUCCESS_BROADCAST);
		Bundle mBundle = new Bundle();
		mBundle.putParcelable(UploadInfo.class.getName(), info);
		intent.putExtras(mBundle);
		UploadConfig.getContext().sendBroadcast(intent);
	}
	
	/**
	 * 当队列为空或进入到上传界面时发出广播
	 * @Title: broadCastFinish
	 * @param needCheck 是否需要检查上传队列为空
	 * @return void
	 * @date 2012-9-1 下午4:35:46
	 */
	public static void broadCastFinish(boolean needCheck) {
		// TODO Auto-generated method stub
		if(needCheck){
			List<UploadInfo> infos = getUploadTasks();
			if(infos != null && infos.size() > 0){//无可上传任务关闭轮巡
				return ;
			}
		}
		Intent intent = new Intent(UploadConfig.UPLOAD_FINISH_BROADCAST);
		UploadConfig.getContext().sendBroadcast(intent);
	}
	
	/**
	 * 前一个任务传完后（成功或失败），启动一个等待队列的任务
	 * @Title: startNewTask
	 * @param session
	 * @param info 前一个任务,当session=0l时，上传新任务
	 * @return boolean 是否启动成功
	 * @date 2014-9-29 下午8:34:20
	 */
//	@SuppressWarnings("static-access")
//	public static boolean startUploadTask(long session, UploadInfo info){
//		if(UploadConfig.DEBUG_MODE_OPENED)UploadUtil.out("UploadProcessor::开始轮巡队列->" + UPLOADING_TASK);
//		if(CHECKING)
//			return false;
//		CHECKING = true;
//		
//		
//		if(!UploadConfig.hasInternet()){//无网关闭轮巡
//			CHECKING = false;
//			return false;
//		}
//		
//		if(!UploadConfig.uploadSettingIsOk()){//不符合上传网络设置
//			CHECKING = false;
//			return false;
//		}
//		
//		if(UPLOADING_TASK != null){//当前有正在上传的任务关闭轮巡
//			CHECKING = false;
//			return false;
//		}
//		
//		if(info != null && info.getStatus() != UploadInfo.STATE_UPLOADED && info.canAutoUpload()){
//			SESSION_ERROR_TASKS.put(info.getTaskId(), 0);
//			if(UploadConfig.DEBUG_MODE_OPENED)UploadUtil.out("UploadProcessor::加入异常session队列->" + info.getTitle());
//		}
//		
//		List<UploadInfo> infos = getUploadTasks();
//		if(infos == null || infos.size() ==0){//无可上传任务关闭轮巡
//			CHECKING = false;
//			broadCastFinish(false);
//			return false;
//		}
//		
//		for(int i = 0; i < infos.size(); i++){
//			UploadInfo info_  = infos.get(i);
//			if(SESSION_ERROR_TASKS.get(info_.getTaskId()) == null && info_.canAutoUpload()){//当前session中未异常过 && 可被上传的
//				if(UploadConfig.DEBUG_MODE_OPENED)UploadUtil.out("UploadProcessor::轮巡上传->" + info_.getTitle() + ">>" + info_.getStatus());
//				new UploadProcessor(info_, session).start();
//				CHECKING = false;
//				return true;
//			}
//		}
//		
//		SESSION_ERROR_TASKS.clear();
//		new Thread(new Runnable(){
//
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				if(UploadConfig.DEBUG_MODE_OPENED)UploadUtil.out("UploadProcessor::轮巡等待");
//				try {
//					Thread.currentThread().sleep(UploadConfig.CHECK_INTERVAL * 1000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				CHECKING = false;
//				startUploadTask(System.currentTimeMillis(), null);
//			}
//			
//		}).start();
//		return false;
//	}
	
}
