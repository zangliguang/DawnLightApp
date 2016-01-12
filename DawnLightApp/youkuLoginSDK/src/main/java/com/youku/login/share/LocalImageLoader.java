package com.youku.login.share;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;

/**
 * 本地存储图片类
 * @author afei
 *
 */
public class LocalImageLoader {
	
	private final long MB = 1024*1024;
	private boolean canSave = true;
	private boolean canRead = true;
	private static final long CLEAR_THRESHOLD_TIME = 7 * 24 * 60 * 60 * 1000;	// 清理本地缓存阀值 7 天
//	private static final long CLEAR_THRESHOLD_TIME = 60 * 1000;	// 测试使用 60 秒
	
	private File CACHE_DIR;
	private final String CACHE_SUBNAME = "youku_phone_share_cache";
	private final String TEMP_NO_MEDIA_FILE = ".nomedia";
	private List<String> subDirNameList = new ArrayList<String>();
	
	public LocalImageLoader(){
		
		// 初始化子目录
		initSubDirName();
		
		// 创建总目录
		CACHE_DIR = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), CACHE_SUBNAME);
//		CACHE_DIR = new File(Youku.DIRECTORY_PICTURES, CACHE_SUBNAME);
		if(!isDirExist(CACHE_DIR))
			CACHE_DIR.mkdir();
		//创建隐藏图片不被系统图库发现的的.nomedia文件
		if(isDirExist(CACHE_DIR)){
			try {
			File tempNoMedia = new File(CACHE_DIR,TEMP_NO_MEDIA_FILE);
				tempNoMedia.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			CACHE_DIR.mkdir();
		}
		
	}
	
	/**
	 * 
	 * 初始化支持的子目录名
	 * @Title: initSubDirName
	 * @return void
	 * @date 2012-7-31 下午4:26:49
	 */
	private void initSubDirName() {
		for(int i=0; i<10; i++){
			subDirNameList.add(i+"");
		}
		subDirNameList.add("a");
		subDirNameList.add("b");
		subDirNameList.add("c");
		subDirNameList.add("d");
		subDirNameList.add("e");
		subDirNameList.add("f");
	}
	
	/**
	 * 
	 * 保存图片
	 * @Title: saveImageToCache
	 * @param bitmap	图片对象
	 * @param imageUrl	图片URL
	 * @return void
	 * @date 2012-7-31 下午4:27:11
	 */
	public void saveImageToCache(final Bitmap bitmap, final String imageUrl){
		
		if(TextUtils.isEmpty(imageUrl) || bitmap == null)
			return;
	
		if(!isSDMounted() || !isDirExist(CACHE_DIR) || !canSave)
			return;
		
		// 开启线程保存图片至本地,因为可能存在多个同时保存的操作,放在主线程中会阻塞
	    new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					// 保存图片到本地缓存
					final String fileName = convertUrlToFileName(imageUrl);
//					File subDir = null;
					
//					if(fileName.length() > 0) {
//						String subDirName = fileName.substring(0, 1);
//						if(subDirNameList.contains(subDirName)){
//							subDir = new File(CACHE_DIR, subDirName);
//						}
//					}
//							subDir = new File(CACHE_DIR, fileName);
//					
//					if(subDir == null)
//						return;
					
					if(isDirExist(CACHE_DIR)) {
//						F.o("saveImageToCache fileName >> " + fileName + " , subDirName >> " + subDir.getName());
						File file = new File(CACHE_DIR, fileName+".png");
						file.createNewFile();
						OutputStream outStream = new FileOutputStream(file);
//						bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
						bitmap.compress(Bitmap.CompressFormat.PNG, 75, outStream);
						outStream.flush();
						outStream.close();
					}else{
						CACHE_DIR.mkdir();
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
	    }).start();

	}
	
	/**
	 * 
	 * 获取图片
	 * @Title: getImageFromCache
	 * @param imageUrl	图片URL
	 * @return	图片对象
	 * @throws OutOfMemoryError	获取图片后，可能因为大图片导致OOM
	 * @return Bitmap
	 * @date 2012-7-31 下午4:27:45
	 */
	public Bitmap getImageFromCache(String imageUrl) throws OutOfMemoryError {
		
		if(TextUtils.isEmpty(imageUrl))
			return null;
		
		if(!isSDMounted() || !isDirExist(CACHE_DIR) || !canRead)
			return null;
			
		// 获取图片
		String fileName = convertUrlToFileName(imageUrl);
		
		File subDir = null;
		
		if(fileName.length() > 0) {
			String subDirName = fileName.substring(0, 1);
			if(subDirNameList.contains(subDirName)){
				subDir = new File(CACHE_DIR, subDirName);
			}
		}
		
		if(subDir == null)
			return null;
		
		File file = new File(subDir, fileName);
		if(!file.exists())
			return null;
		
		// 更新图片最后修改时间
		updateFileTime(file);
		
		Bitmap bitmap = null;
		try {
			InputStream outStream = new FileInputStream(file);
			bitmap = BitmapFactory.decodeStream(outStream);
//			F.o("getImageFromCache fileName >> " + fileName + " , subDirName >> " + subDir.getName());
			return bitmap;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	public boolean isImageExist(String imageUrl) {
		
		if(TextUtils.isEmpty(imageUrl))
			return false;
		
		String fileName = convertUrlToFileName(imageUrl);
		File subDir = null;
//		String subDirName = fileName.substring(0, 1);
//		subDir = new File(CACHE_DIR, subDirName);
		subDir = new File(CACHE_DIR, CACHE_SUBNAME);
		File file = new File(subDir, fileName);
		return file.exists();
		
	}
	
	/**
	 * 
	 * 清除本地图片
	 * @Title: clearImageCache
	 * @return void
	 * @date 2012-7-31 下午9:02:44
	 */
	public void clearImageCache() {
		
		// 图片清除策略：图片最后使用时间与当前时间差值超过阀值,即执行删除
		
		if(!isDirExist(CACHE_DIR)){
			return;
		}
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				canSave = canRead = false;	// 删除开始，不能进行保存和读取图片
				
				try {
					
					ClearFileFilter filter = new ClearFileFilter();
					for(String subDirName : subDirNameList) {
						File subDir = new File(CACHE_DIR, subDirName);
						if(isDirExist(subDir)){
//							F.o("clearImageCache subDirName >> " + subDir.getName());
							File[] files = subDir.listFiles(filter);
							if(files == null || files.length == 0) {
								continue;
							}
							else{
								for(File file : files){
//									F.o("clearImageCache fileName >> " + file.getName() + " , delete ...");
									file.delete();
								}
							}
						}
					}
					
				} catch (Exception e) {
				}
				
				canSave = canRead = true;	// 删除结束，可以进行保存和读取图片
				
			}
		}).start();
		
	}
	
	/**
	 * 
	 * 清除图片策略过滤类
	 * @ClassName: ClearFileFilter
	 * @author luke
	 * @mail luchen@youku.com
	 * @date 2012-7-31 下午9:26:41
	 */
	class ClearFileFilter implements FileFilter {
		@Override
		public boolean accept(File file) {
			if(file.isDirectory())
				return false;
			
			long nowTime = System.currentTimeMillis();
			long fileLastModifiedTime = file.lastModified();
			if(Math.abs(nowTime - fileLastModifiedTime) > CLEAR_THRESHOLD_TIME) {
//				F.o("ClearFileFilter fileName >> " + file.getName() + ", GapTime = " + (nowTime - fileLastModifiedTime)/1000);
				return true;
			}
			return false;
		}
	}
	
	
	/***********************************************************************************/
	/***********************************************************************************/
	/***********************************************************************************/
	
	@Deprecated
	private double getSDCacheTotalSpaceNative() {
		
		try {
			String dirPath = CACHE_DIR.getAbsolutePath();
			String command = "du -sk " + dirPath;
			Process process = Runtime.getRuntime().exec(command);
			BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = br.readLine();
			if(!TextUtils.isEmpty(line))
				return Double.parseDouble(line)/1000;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return -1;
		
	}
	
	/**
	 * 
	 * 计算当前SD卡上的剩余空间
	 * @Title: freeSpaceOnSd
	 * @return
	 * @return int	剩余的空间,单位 MB
	 * @date 2012-3-26 下午02:17:43
	 */
	private double getSDFreeTotalSpace() {
	    StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath()); 
	    double sdFreeMB = ((double)stat.getAvailableBlocks() * (double) stat.getBlockSize()) / MB; 
	    return sdFreeMB;
	} 
	
	/**
	 * 
	 * 更新图片的最后修改时间
	 * @Title: updateFileTime
	 * @param file	需要更新时间的文件
	 * @return void
	 * @date 2012-3-26 下午05:29:42
	 */
	private void updateFileTime(File file) {
	    long newModifiedTime = System.currentTimeMillis();
	    file.setLastModified(newModifiedTime);
	}
	
	/**
	 * 
	 * 将图片Url转换成带有Tag的文件名
	 * @Title: convertUrlToFileName
	 * @param imageUrl
	 * @return
	 * @return String
	 * @date 2012-3-26 下午02:12:36
	 */
	private String convertUrlToFileName(String imageUrl) {
		StringBuilder builder = new StringBuilder();
		builder.append(md5(imageUrl));
//		String replace = imageUrl.replaceAll("[?:+,./=_-]+", "");
//		builder.append(replace);
		return builder.toString();
	}

	/**
	 * 
	 * 判断目录是否存在
	 * @Title: isDirExist
	 * @param dir
	 * @return
	 * @return boolean
	 * @date 2012-7-31 下午2:07:48
	 */
    public static boolean isDirExist(File dir){
    	if(dir == null)
    		return false;
        if(dir.exists()){
        	return true;
        }
        return false;
    }
    
    /**
     * 
     * 判断外部存储设备是否挂载
     * @Title: isSDMounted
     * @return
     * @return boolean	true挂载
     * @date 2012-7-31 上午9:58:33
     */
    public static boolean isSDMounted(){
    	if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
			return true;
    	return false;
    }
    
    /**
     * 
     * MD5加密，转换成16进制字符串
     * @Title: md5
     * @param s	需加密的字符串
     * @return
     * @return String	加密后的字符串
     * @date 2012-3-26 下午02:19:48
     */
    private String md5(String s) {
		try {
			MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
			digest.update(s.getBytes());
			byte messageDigest[] = digest.digest();

			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < messageDigest.length; i++) {
				String h = Integer.toHexString(0xFF & messageDigest[i]);
				while (h.length() < 2)
					h = "0" + h;
				hexString.append(h);
			}
			return hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}
	
    /**
     * 
     * 文件修改时间排序比较器
     * @ClassName: FileLastModifSort
     * @author luchen
     * @mail luchen@youku.com
     * @date 2012-3-26 下午02:20:11
     */
    class FileLastModifSort implements Comparator <File> {
        public int compare(File arg0, File arg1) { 
            if (arg0.lastModified() > arg1.lastModified()) { 
                return 1; 
            } else if (arg0.lastModified() ==arg1.lastModified()) { 
                return 0; 
            } else { 
                return -1; 
            } 
        } 
    }
    
    /**
	 * 
	 * 清除本地全部图片
	 * @Title: clearTotalImageCache
	 * @return void
	 * @date 2012-7-31 下午9:02:44
	 */
	public void clearTotalImageCache() {
		
		if(!isDirExist(CACHE_DIR)){
			return;
		}
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				canSave = canRead = false;	// 删除开始，不能进行保存和读取图片
				
				try {
					
					for(String subDirName : subDirNameList) {
						File subDir = new File(CACHE_DIR, subDirName);
						if(isDirExist(subDir)){
//							F.o("clearImageCache subDirName >> " + subDir.getName());
							File[] files = subDir.listFiles();
							if(files == null || files.length == 0) {
								continue;
							}
							else{
								for(File file : files){
//									F.o("clearImageCache fileName >> " + file.getName() + " , delete ...");
									file.delete();
								}
							}
						}
					}
					
				} catch (Exception e) {
				}
				
				canSave = canRead = true;	// 删除结束，可以进行保存和读取图片
			}
		}).start();
		
	}
    
}
