package com.youku.login.share;

import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.youku.login.network.URLContainer;
import com.youku.login.util.Youku;
import com.youku.login.util.YoukuUtil;


/**
 * 分享到第三方的工具类
 * 
 * @author afei
 *
 */
public class ShareAppUtil {

	public static String imageUrl = "";
	public static ShareVideoInfo selfVideoInfo = null;
	public static ShareAppUtil shareAppUtil = null;
	public static final int MESSAGE_SHARE_IMAGE_DRAWALE = 1000019;//从网络上下载下来图片消息
	
	public static ShareAppUtil getInstance() {
		if (shareAppUtil == null) {
			shareAppUtil = new ShareAppUtil();
		}
		return shareAppUtil;
	}
	
	public static void clear() {
		imageUrl = "";
		selfVideoInfo = null;
		shareAppUtil = null;
		shareAppUtil = null;
	}

	/**
	 * 根据Vid生成VideoInfo
	 * 
	 * @param vid
	 * @return
	 */
	public ShareVideoInfo getDetailVideoInfo(final String vid) {
		selfVideoInfo = new ShareVideoInfo();
		if (YoukuUtil.hasInternet()) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						URL url = new URL(URLContainer.getVideoDownloadDetailUrl(vid));
						HttpURLConnection conn = (HttpURLConnection) url.openConnection();
						conn.setConnectTimeout(Youku.TIMEOUT);
						conn.setReadTimeout(Youku.TIMEOUT);
						conn.setRequestProperty("User-Agent", Youku.User_Agent);
						if (conn.getResponseCode() == HttpURLConnection.HTTP_NOT_FOUND) {

						}
						String json = YoukuUtil.convertStreamToString(conn.getInputStream());
						JSONObject o = new JSONObject(json);
						o = o.getJSONObject("results");
						selfVideoInfo.setShowId(o.optString("showid"));
						selfVideoInfo.setShowName(o.optString("showname"));
						selfVideoInfo.setShowVideoseq(o.optInt("show_videoseq"));
						selfVideoInfo.setShowepisodeTotal(o.optInt("showepisode_total"));
						selfVideoInfo.setCats(o.optString("cats"));
						selfVideoInfo.setImgDrawable(o.optString("img_hd"));
						imageUrl = o.optString("img_hd");
						getDrawableFromUrl(null,imageUrl);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();
		}

		return selfVideoInfo;
	}

	/**
	 * 从网络路径中获取Drwable
	 * 
	 * @param imgStr
	 * @return
	 */
	public  Drawable getDrawableFromUrl(final Handler handler,final String imgStr) {
		if (!TextUtils.isEmpty(imgStr)) {
			ImageLoader.getInstance().loadImage(imgStr, new SimpleImageLoadingListener() {

				@Override
				public void onLoadingComplete(String imageUri, View view,
						Bitmap loadedImage) {
					super.onLoadingComplete(imageUri, view, loadedImage);
					if (null != loadedImage) {
			            saveImageToLocal(loadedImage, imgStr);
			            if(null!=handler){
			            	Message message = new Message();
			            	message.obj = loadedImage;
			            	message.what = MESSAGE_SHARE_IMAGE_DRAWALE;
			            	handler.sendMessage(message);
			            }
//			            shareDrawable = new BitmapDrawable(Youku.context.getResources(), loadedImage);
			        }
				}
				
			});
			
		}
		return null;
	}

	public static void saveImageToLocal(Bitmap bitmap, String imageUrlStr) {
		new LocalImageLoader().saveImageToCache(bitmap, imageUrlStr);
	}

}
