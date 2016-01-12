package com.youku.login.upload;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class UploadDB extends SQLiteOpenHelper{
	
	private static SQLiteDatabase db;
	private final static  String DB_NAME = UploadConfig.DB_NAME;
	private final static int DB_VERSION = UploadConfig.DB_VERSION;
	private final static String table_upload = "upload";
	
	private static UploadDB instance;
	private static UploadDB getInstance(Context context){
		if(instance == null)
			instance = new UploadDB(context);
		return instance;
	}
	
	private static final String sql_create_table_upload = 
			" CREATE TABLE IF NOT EXISTS " + table_upload + " ("
			+ " taskid VARCHAR PRIMARY KEY, "
			+ " title VARCHAR, "
			+ " desc VARCHAR, "//保存用户lbs中文信息
			+ " tag VARCHAR,"
			+ " category INTEGER,"//未用到
			+ " username VARCHAR,"
			+ " privacy INTEGER,"
			+ " push INTEGER,"//未用到
			+ " videopassword VARCHAR,"
			+ " longitude VARCHAR,"
			+ " latitude VARCHAR,"
			
			+ " filepath VARCHAR,"
			+ " filename VARCHAR, "
			+ " filepostfix VARCHAR, "
			+ " size INTEGER,"
			
			+ " fileid VARCHAR,"//新上传中保存upload_token
			+ " sid INTEGER,"//新上传中保存exception_code
			+ " targethost VARCHAR,"
			+ " targetipaddr VARCHAR,"
			+ " md5 VARCHAR,"
			+ " iscreated INTEGER,"////新上传中保存是否秒传
			+ " uploadedsize INTEGER,"
			+ " offset INTEGER,"//新上传中保存是否create_file成功
			+ " segmentsize INTEHER,"//新上传中保存分片大小
			
			+ " status INTEHER,"
			+ " createtime INTEGER,"
			+ " starttime INTEGER,"
			+ " finishtime INTEGER,"
			+ " progress INTEGER,"
			+ " locationame VARCHAR, "
			+ " locationaddress VARCHAR,"
			+ " breakedsliceids VARCHAR,"
			+ " duration INTEGER,"
			+ " isnewvideo INTEGER"
			+ ")";
//	private final String sql_select_all = "select * from " + table_upload;
	private final static String sql_select_item = "select * from " + table_upload + " where taskid = ?";
//	private final static String sql_select_file = "select * from " + table_upload + " where filepath = ?";
	private final static String sql_select_all = "select * from " + table_upload 
			+ " where username = ?"
			+ " and status != " + UploadInfo.STATE_DELETE
			+ " order by createtime desc";
	private final static String sql_get_count_wo_delete_n_cancel = "select * from " + table_upload 
			+ " where username = ?"
			+ " and status != " + UploadInfo.STATE_DELETE
			+" and status != " + UploadInfo.STATE_CANCEL
			+ " order by createtime desc";
	private final static String sql_select_unfinished = "select * from " + table_upload 
			+ " where status not in("+UploadInfo.STATE_PAUSE+", "+UploadInfo.STATE_CANCEL+","+UploadInfo.STATE_DELETE+") "
			+ " and username = ?"
			+ " order by createtime asc";
	private final static String sql_select_paused = "select * from " + table_upload 
			+ " where status = "+UploadInfo.STATE_PAUSE//暂停和取消的
			+ " and status != " + UploadInfo.STATE_DELETE
			+ " and username = ?"
			+ " order by createtime asc";
	private final static String sql_select_all_contain_delete = "select * from " + table_upload 
			+ " where username = ?"
			+ " order by createtime desc";
	
	private static ContentValues downloadInfo2Cv(UploadInfo info){
		ContentValues cv = new ContentValues();
		cv.put("taskid", info.getTaskId());
		cv.put("title", info.getTitle());
		cv.put("desc", info.getDesc());
		cv.put("tag", info.getTag());
		cv.put("category", info.getCategory());
		cv.put("username", info.getUserName());
		cv.put("privacy", info.getPrivacy());
		cv.put("push", info.isPush() ? 1 : 0);
		cv.put("videopassword", info.getVideoPassword());
		cv.put("longitude", info.getLongitude());
		cv.put("latitude", info.getLatitude());
		
		cv.put("filepath", info.getFilePath());
		cv.put("filename", info.getFileName());
		cv.put("filepostfix", info.getFilePostfix());
		cv.put("size", info.getSize());
		
		cv.put("fileid", info.getUploadToken());
		cv.put("sid", info.getExceptionCode());
		cv.put("targethost", info.getTargetHost());
		cv.put("targetipaddr", info.getTargetIpAddr());
		cv.put("md5", info.getMd5());
		cv.put("iscreated", info.isInstantUpload() ? 1 : 0);
		cv.put("uploadedsize", info.getUploadedSize());
		cv.put("offset", info.isCreatedFile() ? 1 : 0);
		cv.put("segmentsize", info.getSliceSize());
		
		cv.put("status", info.getStatus());
		cv.put("createtime", info.getCreateTime());
		cv.put("starttime", info.getStartTime());
		cv.put("finishtime", info.getFinishTime());
		cv.put("progress", info.getProgress());
		
		//数据库版本2 新增字段
		cv.put("locationame", info.getLocationName());
		cv.put("locationaddress", info.getLocationAddress());
		
		//数据库版本3 新增字段
		cv.put("breakedsliceids", UploadUtil.list2String(info.getBreakedSliceIds()));
		 
		//数据库版本4 新增字段
		cv.put("duration", info.getDuration());
		cv.put("isnewvideo", info.isNewVideo() ? 1 : 0);
		return cv;
	}
	
	private static UploadInfo cursor2UploadInfo(Cursor cursor){
		UploadInfo info = new UploadInfo();
		info.setTaskId(cursor.getString(cursor.getColumnIndex("taskid")));
		info.setTitle(cursor.getString(cursor.getColumnIndex("title")));
		info.setDesc(cursor.getString(cursor.getColumnIndex("desc")));
		info.setTag(cursor.getString(cursor.getColumnIndex("tag")));
		info.setCategory(cursor.getInt(cursor.getColumnIndex("category")));
		info.setUserName(cursor.getString(cursor.getColumnIndex("username")));
		info.setPrivacy(cursor.getInt(cursor.getColumnIndex("privacy")));
		info.setPush(cursor.getInt(cursor.getColumnIndex("push")) == 1);
		info.setVideoPassword(cursor.getString(cursor.getColumnIndex("videopassword")));
		info.setLongitude(cursor.getString(cursor.getColumnIndex("longitude")));
		info.setLatitude(cursor.getString(cursor.getColumnIndex("latitude")));
		
		info.setFilePath(cursor.getString(cursor.getColumnIndex("filepath")));
		info.setFileName(cursor.getString(cursor.getColumnIndex("filename")));
		info.setFilePostfix(cursor.getString(cursor.getColumnIndex("filepostfix")));
		info.setSize(cursor.getLong(cursor.getColumnIndex("size")));
		
		info.setUploadToken(cursor.getString(cursor.getColumnIndex("fileid")));
		info.setExceptionCode(cursor.getInt(cursor.getColumnIndex("sid")));
		info.setTargetHost(cursor.getString(cursor.getColumnIndex("targethost")));
		info.setTargetIpAddr(cursor.getString(cursor.getColumnIndex("targetipaddr")));
		info.setMd5(cursor.getString(cursor.getColumnIndex("md5")));
		info.setInstantUpload(cursor.getInt(cursor.getColumnIndex("iscreated")) == 1);
		info.setUploadedSize(cursor.getInt(cursor.getColumnIndex("uploadedsize")));
		info.setCreatedFile(cursor.getInt(cursor.getColumnIndex("offset")) == 1);
		info.setSliceSize(cursor.getInt(cursor.getColumnIndex("segmentsize")));
		
		info.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
		info.setCreateTime(cursor.getLong(cursor.getColumnIndex("createtime")));
		info.setStartTime(cursor.getLong(cursor.getColumnIndex("starttime")));
		info.setFinishTime(cursor.getLong(cursor.getColumnIndex("finishtime")));
		info.setProgress(cursor.getInt(cursor.getColumnIndex("progress")));
		
		//数据库版本2 新增字段
		info.setLocationName(cursor.getString(cursor.getColumnIndex("locationame")));
		info.setLocationAddress(cursor.getString(cursor.getColumnIndex("locationaddress")));
		
		//数据库版本3 新增字段
		info.setBreakedSliceIds(UploadUtil.string2List(cursor.getString(cursor.getColumnIndex("breakedsliceids"))));
		
		//数据库版本4 新增字段
		info.setDuration(cursor.getInt(cursor.getColumnIndex("duration")));
		info.setNewVideo(cursor.getInt(cursor.getColumnIndex("isnewvideo")) == 1);
		return info;
	}
	
	protected static List<UploadInfo> getList(){
		open();
		Cursor cursor = db.rawQuery(sql_select_all, new String[]{UploadConfig.getUserID()});
		List<UploadInfo> infos = new ArrayList<UploadInfo>();
        try {
			while(cursor.moveToNext())
				infos.add(cursor2UploadInfo(cursor));       
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return infos;
	}
	
	protected static List<UploadInfo> getListContainDelTask(){
		open();
		Cursor cursor = db.rawQuery(sql_select_all_contain_delete, new String[]{UploadConfig.getUserID()});
		List<UploadInfo> infos = new ArrayList<UploadInfo>();
        while(cursor.moveToNext())
        	infos.add(cursor2UploadInfo(cursor));       
        cursor.close();
		return infos;
	}
	
	protected static UploadInfo getItem(String taskId){
		open();
		Cursor cursor = db.rawQuery(sql_select_item, new String[]{taskId});
		if(cursor.getCount() < 1){
			cursor.close();
			return null;
		}else{
			cursor.moveToFirst();
			UploadInfo info = cursor2UploadInfo(cursor);
			cursor.close();
	        return info;
		}
	}
	
//	protected static UploadInfo getItemByFile(String filePath){
//		open();
//		Cursor cursor = db.rawQuery(sql_select_file, new String[]{filePath});
//		if(cursor.getCount() < 1){
//			cursor.close();
//			return null;
//		}else{
//			cursor.moveToFirst();
//			UploadInfo info = cursor2UploadInfo(cursor);
//			cursor.close();
//	        return info;
//		}
//	}
	
	protected static List<UploadInfo> getUnFinishedItems(){
		open();
//		db.execSQL("delete from " +  table_upload);
//		db.execSQL("DROP TABLE " + table_upload);
//		db.execSQL(sql_create_table_upload);
		List<UploadInfo> infos = new ArrayList<UploadInfo>();
		if(UploadConfig.getUserID().equals(""))
			return infos;
		Cursor cursor = db.rawQuery(sql_select_unfinished, new String[]{UploadConfig.getUserID()});
		if(cursor.getCount() < 1){
			cursor.close();
			return infos;
		}else{
			while(cursor.moveToNext()){
	        	infos.add(cursor2UploadInfo(cursor));
			}
	        cursor.close();
	        return infos;
		}
	}
	
	protected static List<UploadInfo> getPausedItems(){
		open();
		List<UploadInfo> infos = new ArrayList<UploadInfo>();
		if(UploadConfig.getUserID().equals(""))
			return infos;
		Cursor cursor = db.rawQuery(sql_select_paused, new String[]{UploadConfig.getUserID()});
		if(cursor.getCount() < 1){
			cursor.close();
			return infos;
		}else{
			while(cursor.moveToNext()){
	        	infos.add(cursor2UploadInfo(cursor));
			}
	        cursor.close();
	        return infos;
		}
	}
	
	protected static boolean delete(String taskId){
		open();
		ContentValues cv = new ContentValues();
		cv.put("status", UploadInfo.STATE_DELETE);
		return db.update(table_upload, cv, "taskId=?", new String[]{taskId}) > 0;
	}
	
	protected static boolean deleteUserData(String uid){
		open();
		ContentValues cv = new ContentValues();
		cv.put("status", UploadInfo.STATE_DELETE);
		return db.update(table_upload, cv, "username=?", new String[]{uid}) > 0;
	}
	
	protected static boolean update(UploadInfo info){
		open();
		return db.update(table_upload, downloadInfo2Cv(info), "taskId=?", new String[]{info.getTaskId() + ""}) == 1;
	}

	protected static boolean insert(UploadInfo info){
		open();
		return db.insert(table_upload, null, downloadInfo2Cv(info)) != -1;
	}
	
	private UploadDB(Context context){
		super(context, DB_NAME, null, DB_VERSION);
	}
	
	//03-07 17:12:34.942: DEBUG/Database(13828): dbopen(): sqlite3_exec to set journal_mode to WAL for /data/data/com.youku.paike/databases/paike.db
	private static void open(){
		getInstance(UploadConfig.getContext());
		if(db == null || !db.isOpen()){
			db = instance.getWritableDatabase();
//			db.rawQuery("pragma journal_mode", null);//保证数据完整性
		}
	}
	
	protected static void closeDB(){
		if(db != null && db.isOpen())
			db.close();
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
//		db.execSQL("delete from " +  table_upload);
		db.execSQL(sql_create_table_upload);
		UploadDB.db = db;
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		try{
			db.execSQL("alter table "+ table_upload + " add locationame VARCHAR");
		}catch(Exception e){
			e.printStackTrace();
		}
		try{
			db.execSQL("alter table "+ table_upload + " add locationaddress VARCHAR");
		}catch(Exception e){
			e.printStackTrace();
		}
		try{
			db.execSQL("alter table "+ table_upload + " add breakedsliceids VARCHAR");
		}catch(Exception e){
			e.printStackTrace();
		}
		try{
			db.execSQL("alter table "+ table_upload + " add duration INTEGER");
			db.execSQL("alter table "+ table_upload + " add isnewvideo INTEGER");
		}catch(Exception e){
			e.printStackTrace();
		}
		try{//2.6的state暂停是8，新版的是5，需要替换
		    //update backupfile set url=REPLACE(url,'http://www.maidq.com','http://maidq.com')
            db.execSQL("update "+ table_upload + " set status=REPLACE(status, 8, 5)");
        }catch(Exception e){
            e.printStackTrace();
        }
	}
	
	@Override
	public void onOpen(SQLiteDatabase db) {
	}
	//获取所有上传任务，不包含已完成的,和已cancel的
	protected static int getCount(){
		int count = 0;
		open();
		Cursor cursor = db.rawQuery(sql_get_count_wo_delete_n_cancel, new String[]{UploadConfig.getUserID()});     
        if(cursor!=null){
        	count = cursor.getCount();
        	cursor.close();
        }
		return count;
	}
}
