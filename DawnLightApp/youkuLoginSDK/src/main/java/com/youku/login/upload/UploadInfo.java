package com.youku.login.upload;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;

public class UploadInfo implements Cloneable,Parcelable{
	
	/**
	 * 上传任务状态-未初始化
	 */
	public final static int STATE_INIT = -1;
	/**
	 * 上传任务状态-上传中
	 */
	public final static int STATE_UPLOADING = 0;
	/**
	 * 上传任务状态-上传完成
	 */
	public final static int STATE_UPLOADED = 1;
	/**
	 * 上传任务状态-异常等待
	 */
	public final static int STATE_EXCEPTION = 2;
	/**
	 * 上传任务状态-队列等待中
	 */
	public final static int STATE_WAIT = 3;
	/**
	 * 上传任务状态-任务取消或不可能成功
	 */
	public final static int STATE_CANCEL = 4;
	/**
	 * 上传任务状态-上传暂停
	 */
	public final static int STATE_PAUSE = 5;
	/**
	 * 上传任务状态-已删除
	 */
	public final static int STATE_DELETE = 6;
	
	private String taskId;
	private String title;
	private String desc;
	private String tag = UploadConfig.TAG;
	private int category = UploadConfig.CATEGORY;
	private String userName;
	private int privacy;
	private boolean push;
	private String videoPassword;
	private String longitude;
	private String latitude;
	
	private String filePath;
	private String fileName;
	private String filePostfix;
	private long size;
	
	private String uploadToken;
	private String targetHost;
	private String targetIpAddr = null;
	private String md5;
	private boolean instantUpload = false;
	private long uploadedSize = 0;
	private boolean isCreatedFile = false;
	
	private int status = STATE_INIT;
	private long createTime = 0l;
	private long startTime = 0l;
	private long finishTime = 0l;
	private int progress = 0;
	private int exceptionCode;
	
	private Object exceptionDetail;
	private UploadListener uploadListener;
	private long lastUpdateTime = 0l;
	
	private String locationName = "";//地理位置的名字
	private String locationAddress = "";//地理位置的详细信息
	private List<Integer> breakedSliceIds = new ArrayList<Integer>();//中断的上传分片ID
	
	private int speed;
	private String speedDesc = "0KB";
	private String sizeDesc = "0KB";
	private String uploadedSizeDesc = "0KB";
	private int sliceSize;
	private int remainTime;
	private long duration;
	
	private boolean isNewVideo = true;

	/**
	 * 缩略图
	 */
	
//	public Bitmap getThumbImg(Activity activity, String filePath) {
//		if (android.os.Build.VERSION.SDK_INT < 8) {
//			return UploadUtil.loadThumbnail(activity, filePath
//					.substring(filePath.lastIndexOf("/") + 1));
//		}
//		return android.media.ThumbnailUtils.createVideoThumbnail(
//				this.getFilePath(), MediaStore.Video.Thumbnails.MICRO_KIND);
//	}
//	
//	/**
//	 * 上传状态是否可能会转变
//	 * @Title: stateMaybeChange
//	 * @return boolean
//	 * @date 2012-7-12 下午3:16:21
//	 */
//	public boolean stateMaybeChange(){
//		return this.getStatus() == STATE_UPLOADING
//				|| this.getStatus() == STATE_WAIT
//				|| this.getStatus() == STATE_EXCEPTION
//				|| this.getStatus() == STATE_INIT
//				|| this.getStatus() == STATE_PAUSE;
//	}
//	
//	/**
//	 * 可否被自动上传
//	 * @Title: canAutoUpload
//	 * @return boolean
//	 * @date 2012-7-12 下午3:16:51
//	 */
//	public boolean canAutoUpload(){
//		return this.getStatus() == STATE_UPLOADING
//				|| this.getStatus() == STATE_WAIT
//				|| this.getStatus() == STATE_EXCEPTION
//				|| this.getStatus() == STATE_INIT;
//	}
//	
	public void setStatus(final int status) {
		if(this.status != status){
			this.status = status;
				if(status == STATE_UPLOADING){
					if(getUploadListener() != null)getUploadListener().onStart();
				}else if(status == STATE_PAUSE){
					if(getUploadListener() != null)getUploadListener().onPause();
				}else if(status == STATE_UPLOADED){
					if(getUploadListener() != null)getUploadListener().onFinish();
				}else if(status == STATE_CANCEL){
					if(getUploadListener() != null)getUploadListener().onCancel();
				}else if(status == STATE_EXCEPTION){
					if(getUploadListener() != null)getUploadListener().onException();
				}else if(status == STATE_WAIT){
					if(getUploadListener() != null)getUploadListener().onWait();
				}
		}else
			this.status = status;
		this.status = status;
	}
	public void setUploadedSize(long uploadedSize) {
		this.uploadedSize = uploadedSize;
		if(this.getSize() == 0){
			return;
		}
		
		uploadedSizeDesc = UploadUtil.parseSize(uploadedSize);
		this.setProgress((int)(uploadedSize*100/this.getSize()));
	}
	public void setProgress(int progress) {
		if(this.progress != progress){
			this.progress = progress;
			if(this.getStatus() == UploadInfo.STATE_UPLOADING){
				if(this.getUploadListener() != null)this.getUploadListener().onProgressChange();
			}
		}else
			this.progress = progress;
	}
//	public void setSpeed(int speed) {
//		if(this.speed == speed)
//			return;
//		this.speed = speed;
//		
//		speedDesc = UploadUtil.parseSpeed(speed);
//		remainTime = speed <= 0 ? 24 * 60 * 60 : (int)(size - uploadedSize)/1000/speed;
//		
//		if(this.getUploadListener() != null)this.getUploadListener().onUploadSpeedChange();
//	}
	public void setSize(long size) {
		this.size = size;
		sizeDesc = UploadUtil.parseSize(size);;
	}
	public void setFilePath(String filePath) {
		String fileOrgName = filePath.substring(filePath.lastIndexOf("/") + 1);
		this.setFileName(java.net.URLEncoder.encode(fileOrgName.substring(0,fileOrgName.indexOf("."))));
		this.setFilePostfix(fileOrgName.substring(fileOrgName.lastIndexOf(".") + 1));
		this.filePath = filePath;
	}
	
	public boolean isInstantUpload() {
		return instantUpload;
	}
	public void setInstantUpload(boolean instantUpload) {
		this.instantUpload = instantUpload;
	}
	public UploadListener getUploadListener() {
		return uploadListener;
	}
	public void setUploadListener(UploadListener uploadListener) {
		this.uploadListener = uploadListener;
	}
	public String getLocationName() {
		return locationName;
	}
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	public String getLocationAddress() {
		return locationAddress;
	}
	public void setLocationAddress(String locationAddress) {
		this.locationAddress = locationAddress;
	}
	public String getTargetIpAddr() {
		return targetIpAddr;
	}
	public void setTargetIpAddr(String targetIpAddr) {
		this.targetIpAddr = targetIpAddr;
	}
	public String getMd5() {
		return md5;
	}
	public void setMd5(String md5) {
		this.md5 = md5;
	}
	public String getUploadToken() {
		return uploadToken;
	}
	public void setUploadToken(String uploadToken) {
		this.uploadToken = uploadToken;
	}
	public long getUploadedSize() {
		return uploadedSize;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getTargetHost() {
		return targetHost;
	}
	public void setTargetHost(String targetHost) {
		this.targetHost = targetHost;
	}
	public long getSize() {
		return size;
	}
	public int getStatus() {
		return status;
	}
	public String getFilePath() {
		return filePath;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.setDesc(title);
		this.title = title;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public int getCategory() {
		return category;
	}
	public void setCategory(int category) {
		this.category = category;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFilePostfix() {
		return filePostfix;
	}
	public void setFilePostfix(String filePostfix) {
		this.filePostfix = filePostfix;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	public long getFinishTime() {
		return finishTime;
	}
	public void setFinishTime(long finishTime) {
		this.finishTime = finishTime;
	}
	public int getProgress() {
		return progress;
	}
	public int getExceptionCode() {
		return exceptionCode;
	}
	public void setExceptionCode(int exceptionCode) {
		this.exceptionCode = exceptionCode;
	}
	public Object getExceptionDetail() {
		return exceptionDetail;
	}
	public void setExceptionDetail(Object exceptionDetail) {
		this.exceptionDetail = exceptionDetail;
	}
	public int getPrivacy() {
		return privacy;
	}
	public void setPrivacy(int privacy) {
		this.privacy = privacy;
	}
	public boolean isPush() {
		return push;
	}
	public void setPush(boolean push) {
		this.push = push;
	}
	public String getVideoPassword() {
		return videoPassword;
	}
	public void setVideoPassword(String videoPassword) {
		this.videoPassword = videoPassword;
	}
	public long getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(long lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public boolean isCreatedFile() {
		return isCreatedFile;
	}
	public void setCreatedFile(boolean isCreatedFile) {
		this.isCreatedFile = isCreatedFile;
	}
	public int getSpeed() {
		return speed;
	}
	public String getSpeedDesc() {
		return speedDesc;
	}
	public void setSpeedDesc(String desc) {
		this.speedDesc = desc;
	}
	public void setSizeDesc(String desc) {
		this.sizeDesc = desc;
	}
	public String getSizeDesc() {
		return sizeDesc;
	}
	public int getSliceSize() {
		return sliceSize;
	}
	public void setSliceSize(int sliceSize) {
		this.sliceSize = sliceSize;
	}
	public List<Integer> getBreakedSliceIds() {
		return breakedSliceIds;
	}
	public void setBreakedSliceIds(List<Integer> breakedSliceIds) {
		this.breakedSliceIds = breakedSliceIds;
	}
	public String getUploadedSizeDesc() {
		return uploadedSizeDesc;
	}
	public void setUploadedSizeDesc(String uploadedSizeDesc) {
		this.uploadedSizeDesc = uploadedSizeDesc;
	}
	public int getRemainTime() {
		return remainTime;
	}
	public void setRemainTime(int remainTime) {
		this.remainTime = remainTime;
	}
	public long getDuration() {
		return duration;
	}
	public void setDuration(long duration) {
		this.duration = duration;
	}
	public boolean isNewVideo() {
		return isNewVideo;
	}
	public void setNewVideo(boolean isNewVideo) {
		this.isNewVideo = isNewVideo;
	}

	public UploadInfo clone() {
		UploadInfo o = null;
		try {
			o = (UploadInfo) super.clone();// Object中的clone()识别出你要复制的是哪一个对象。
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return o;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
//	
//	public static final Parcelable.Creator<UploadInfo> CREATOR = new Creator<UploadInfo>() {
//		public UploadInfo createFromParcel(Parcel source) {
//			UploadInfo info = new UploadInfo();
//			info.setTaskId(source.readString());
//			info.setTitle(source.readString());
//			info.setDesc(source.readString());
//			info.setTag(source.readString());
//			info.setCategory(source.readInt());
//			info.setUserName(source.readString());
//			info.setPrivacy(source.readInt());
//			info.setVideoPassword(source.readString());
//			info.setLongitude(source.readString());
//			info.setLatitude(source.readString());
//			
//			info.setFilePath(source.readString());
//			info.setFileName(source.readString());
//			info.setFilePostfix(source.readString());
//			info.setSize(source.readLong());
//			
//			info.setUploadToken(source.readString());
//			info.setTargetHost(source.readString());
//			info.setTargetIpAddr(source.readString());
//			info.setMd5(source.readString());
//			info.setUploadedSize(source.readLong());
//			
//			info.setStatus(source.readInt());
//			info.setCreateTime(source.readLong());
//			info.setStartTime(source.readLong());
//			info.setFinishTime(source.readLong());
//			info.setProgress(source.readInt());
//			info.setExceptionCode(source.readInt());
//			
//			info.setSpeed(source.readInt());
//			info.setSpeedDesc(source.readString());
//			info.setSizeDesc(source.readString());
//			info.setUploadedSizeDesc(source.readString());
//			info.setRemainTime(source.readInt());
//			info.setDuration(source.readLong());
//			return info;
//		}
//		public UploadInfo[] newArray(int size) {
//			return new UploadInfo[size];
//		}
//	};
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(this.getTaskId());
		dest.writeString(this.getTitle());
		dest.writeString(this.getDesc());
		dest.writeString(this.getTag());
		dest.writeInt(this.getCategory());
		dest.writeString(this.getUserName());
		dest.writeInt(this.getPrivacy());
		dest.writeString(this.getVideoPassword());
		dest.writeString(this.getLongitude());
		dest.writeString(this.getLatitude());
		
		dest.writeString(this.getFilePath());
		dest.writeString(this.getFileName());
		dest.writeString(this.getFilePostfix());
		dest.writeLong(this.getSize());
		
		dest.writeString(this.getUploadToken());
		dest.writeString(this.getTargetHost());
		dest.writeString(this.getTargetIpAddr());
		dest.writeString(this.getMd5());
		dest.writeLong(this.getUploadedSize());
		
		dest.writeInt(this.getStatus());
		dest.writeLong(this.getCreateTime());
		dest.writeLong(this.getStartTime());
		dest.writeLong(this.getFinishTime());
		dest.writeInt(this.getProgress());
		dest.writeInt(this.getExceptionCode());
		
		dest.writeInt(this.getSpeed());
		dest.writeString(this.getSpeedDesc());
		dest.writeString(this.getSizeDesc());
		dest.writeString(this.getUploadedSizeDesc());
		dest.writeInt(this.getRemainTime());
		dest.writeLong(this.getDuration());
	}

}
