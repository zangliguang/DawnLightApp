/*
 * Copyright © 2012-2013 LiuZhongnan. All rights reserved.
 * 
 * Email:qq81595157@126.com
 * 
 * PROPRIETARY/CONFIDENTIAL.
 */

package com.youku.service.download;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.baseproject.utils.AesUtils;
import com.baseproject.utils.Logger;
import com.baseproject.utils.UIUtils;
import com.youku.player.YoukuPlayerConfiguration;
import com.youku.player.util.PlayerUtil;
import com.youku.player.util.URLContainer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.DecimalFormat;

/**
 * DownloadUtils.下载工具类
 *
 * @author 刘仲男 qq81595157@126.com
 * @version v3.5
 * @created time 2012-11-5 下午1:16:02
 */
public class DownloadUtils {

    public static final String SHARE_PREFERENCE_DOWNLOAD_LOGIN_NAME = "DownloadLogin";
    public static final String SHARE_PREFERENCE_DOWNLOAD_LOGIN_CLICKCOUNT_KEY = "clickCount";
    public static final String SHARE_PREFERENCE_DOWNLOAD_LOGIN_MAXCOUNT_KEY = "maxCount";
    public static final String SHARE_PREFERENCE_PLAY_LOGIN_MAXCOUNT_KEY = "playMaxCount";
    public static final int CLICK_MAX_COUNT = 5;
    private static final String TAG = "Download_Utils";
    /**
     * 保留一位小数格式化对象
     */
    private static final DecimalFormat df = new DecimalFormat("0.0");

    /**
     * TODO 获得真实地址
     *
     * @param segUrl
     * @return 302跳转后的地址
     */
    public static String getLocation(String segUrl) {
        try {
            URL url = new URL(segUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(20000);
            conn.setConnectTimeout(15000);
            conn.setInstanceFollowRedirects(false);
            conn.setRequestMethod("HEAD");
            return conn.getHeaderField("Location");
        } catch (IOException e) {
            Logger.e(TAG, "DownloadUtils#getLocation()", e);
        }
        return null;
    }

    /**
     * TODO 获得下载地址和下载信息
     *
     * @param info
     * @return
     */
    public static boolean getDownloadData(DownloadInfo info) {
        Logger.d("DownloadFlow", "DownloadUtil: getDownloadData()");
        try {
            URL url = new URL(URLContainer.getDownloadURL(info.videoid,
                    info.format, info.language));
            Logger.d("DownloadFlow", "download_url: " + url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(YoukuPlayerConfiguration.TIMEOUT);
            conn.setReadTimeout(YoukuPlayerConfiguration.TIMEOUT);
            conn.setRequestProperty("User-Agent", YoukuPlayerConfiguration.User_Agent);
            if (conn.getResponseCode() == HttpURLConnection.HTTP_NOT_FOUND) {
                info.setExceptionId(DownloadInfo.EXCEPTION_HTTP_NOT_FOUND);
                return false;
            } else if (conn.getResponseCode() == HttpURLConnection.HTTP_GONE) {
                InputStream is = conn.getErrorStream();
                double d = Double.parseDouble(PlayerUtil
                        .convertStreamToString(is));
                URLContainer.TIMESTAMP = (long) d - System.currentTimeMillis()
                        / 1000;
                getDownloadData(info);
                if (is != null) {
                    is.close();
                    is = null;
                }
            }
            Logger.d(TAG, conn.getResponseCode() + "");
            String json = PlayerUtil
                    .convertStreamToString(conn.getInputStream());
            JSONObject obj = new JSONObject(json);
            String data = obj.getString("data");
            json = AesUtils.decrypt(data);// 解密
            obj = new JSONObject(json);
            JSONObject o = obj.getJSONObject("sid_data");
            info.token = o.getString("token");
            info.oip = o.getString("oip");
            info.sid = o.getString("sid");
            JSONArray datas = obj.getJSONObject("results").getJSONArray(
                    DownloadInfo.FORMAT_STRINGS[info.format]);
            final int segCount = datas.length();// 分片数量
            // 若无高清则向下下载，例如无超清下高清，无高清自动下标清
            if (segCount == 0) {
                if (info.format == DownloadInfo.FORMAT_HD2) {
                    info.format = DownloadInfo.FORMAT_MP4;
                    return getDownloadData(info);
                } else if (info.format == DownloadInfo.FORMAT_MP4) {
                    info.format = DownloadInfo.FORMAT_FLV;
                    return getDownloadData(info);
                } else {
                    info.segCount = segCount;
                    info.setExceptionId(DownloadInfo.EXCEPTION_NO_RESOURCES);
                    return false;
                }
            }
            info.seconds = (int) obj.getDouble("totalseconds");
            info.segCount = segCount;
            long[] segsSize = new long[segCount];
            String[] segsUrl = new String[segCount];
            String[] segsfileId = new String[segCount];
            int[] segsSeconds = new int[segCount];
            long size = 0l;
            for (int i = 0; i < segCount; i++) {
                obj = datas.getJSONObject(i);
                int segId = obj.getInt("id") - 1;
                segsUrl[segId] = obj.getString("url");
                segsSeconds[segId] = obj.getInt("seconds");
                segsfileId[segId] = obj.getString("fileid");
                long segSize = obj.getLong("size");
                segsSize[segId] = segSize;
                size += segSize;
            }
            info.segsSize = segsSize;
            info.segsUrl = segsUrl;
            info.segsSeconds = segsSeconds;
            info.segsfileId = segsfileId;
            info.size = size;
            info.getUrlTime = System.currentTimeMillis();
        } catch (SocketTimeoutException e) {
            Logger.e(TAG, "getDownloadData():" + info.title + "/"
                    + info.videoid, e);
            info.setExceptionId(DownloadInfo.EXCEPTION_TIMEOUT);
            // info.setState(DownloadInfo.STATE_EXCEPTION);
            return false;
        } catch (UnknownHostException e) {
            Logger.e(TAG, "getDownloadData():" + info.title + "/"
                    + info.videoid, e);
            info.setExceptionId(DownloadInfo.EXCEPTION_NO_NETWORK);
            return false;
        } catch (Exception e) {
            Logger.e(TAG, "getDownloadData():" + info.title + "/"
                    + info.videoid, e);
            info.setExceptionId(DownloadInfo.EXCEPTION_HTTP_NOT_FOUND);
            // info.setState(DownloadInfo.STATE_EXCEPTION);
            return false;
        }
        return true;
    }

    /**
     * TODO 获得视频信息
     *
     * @param info
     * @return
     */
    public static boolean getVideoInfo(DownloadInfo info) {
        Logger.d("DownloadFlow", "DownloadUtil: getVideoInfo()");
        try {
            URL url = new URL(
                    URLContainer.getVideoDownloadDetailUrl(info.videoid));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(YoukuPlayerConfiguration.TIMEOUT);
            conn.setReadTimeout(YoukuPlayerConfiguration.TIMEOUT);
            conn.setRequestProperty("User-Agent", YoukuPlayerConfiguration.User_Agent);
            if (conn.getResponseCode() == HttpURLConnection.HTTP_NOT_FOUND) {
                info.setExceptionId(DownloadInfo.EXCEPTION_HTTP_NOT_FOUND);
                return false;
            }
            String json = PlayerUtil
                    .convertStreamToString(conn.getInputStream());
            JSONObject o = new JSONObject(json);
            o = o.getJSONObject("results");
            info.showid = o.optString("showid");
            info.showname = o.optString("showname");
            info.show_videoseq = o.optInt("show_videoseq");
            info.showepisode_total = o.optInt("showepisode_total");
            info.cats = o.optString("cats");
            info.imgUrl = o.optString("img_hd");
            return true;
        } catch (Exception e) {
            Logger.e(TAG, "DownloadUtils#getShowInfo()", e);
            info.setExceptionId(DownloadInfo.EXCEPTION_UNKNOWN_ERROR);
            return false;
        }
    }

    // /**
    // * 获取某一视频的下载速度
    // *
    // * @param downloadInfo
    // * @return
    // */
    // public static long getSpeed(DownloadInfo downloadInfo) {
    // final long speed = downloadInfo.downloadedSize
    // / (System.currentTimeMillis() - downloadInfo.getStartTime());
    // return speed;
    // }
    //
    // /**
    // * FIXME 获取视频下载的进度
    // *
    // * @param downloadInfo
    // * @return
    // */
    // public static long getLeftTime(DownloadInfo info) {
    // long restSize = info.getSize() - info.downloadedSize;
    // return restSize / getSpeed(info);
    // }

    /***
     * TODO 创建视频缩略图
     *
     * @param imgUrl
     * @param videoid
     */
    public static void createVideoThumbnail(String imgUrl, String savePath) {
        Logger.d("DownloadFlow", "DownloadUtil: createVideoThumbnail()");
        File f = null;
        Logger.d(TAG, "createVideoThumbnail()/imgUrl ：" + imgUrl);
        f = new File(savePath);
        if (!f.exists())
            f.mkdirs();
        f = new File(savePath + IDownload.THUMBNAIL_NAME);
        if (f.exists()) {
            if (f.length() != 0)
                return;
            f.delete();
        }
        FileOutputStream fos = null;
        InputStream i = null;
        HttpURLConnection httConn;
        try {
            URL url = new URL(imgUrl);
            httConn = (HttpURLConnection) url.openConnection();
            httConn.setConnectTimeout(15 * 1000);
            httConn.setReadTimeout(15 * 1000);
            httConn.setDoInput(true);
            httConn.connect();
            i = (InputStream) httConn.getInputStream();
            fos = new FileOutputStream(f);
            byte[] buf = new byte[1024];
            int len = 0;
            while ((len = i.read(buf)) > 0) {
                fos.write(buf, 0, len);
            }
            fos.flush();
            // 缩略图下载完成的广播
            YoukuPlayerConfiguration.context.sendBroadcast(new Intent(
                    IDownload.ACTION_THUMBNAIL_COMPLETE));
        } catch (Exception e) {
            Logger.e(TAG, "createVideoThumbnail()", e);
            if (f.exists())
                f.delete();
        } finally {
            try {
                if (fos != null)
                    fos.close();
                if (i != null)
                    i.close();
            } catch (IOException e) {
            }
        }
    }

    // /**
    // * 更新下载进度文件
    // *
    // * @param info
    // * @return
    // */
    // public static boolean makeDownloadProgressFile(DownloadInfo info) {
    // if (info == null)
    // return false;
    // BufferedWriter w = null;
    // try {
    // File f = new File(info.savePath + "progress");
    // if (!f.exists()) {
    // File d = new File(info.savePath);
    // if (!d.exists())
    // d.mkdirs();
    // }
    // w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
    // f)));
    // w.write(info.getProgressJSONFile());
    // } catch (IOException e) {
    // Logger.e(TAG, e);
    // info.setExceptionId(DownloadInfo.EXCEPTION_WRITE_ERROR);
    // return false;
    // } finally {
    // try {
    // if (w != null) {
    // w.close();
    // }
    //
    // } catch (IOException e) {
    // Logger.e(TAG, e);
    // }
    // }
    // return true;
    // }

    /**
     * 创建下载文件
     *
     * @param info
     * @return 是否创建成功
     */
    public static boolean makeDownloadInfoFile(DownloadInfo info) {
        Logger.d("DownloadFlow", "DownloadUtil: makeDownloadInfoFile()");
        if (info == null)
            return false;
        BufferedWriter w = null;
        try {
            File f = new File(info.savePath + IDownload.FILE_NAME);
            if (!f.exists()) {
                File d = new File(info.savePath);
                if (!d.exists())
                    d.mkdirs();
            }
            w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
                    f)));
            w.write(info.toString());
        } catch (FileNotFoundException e) {// SD卡被拔出
            Logger.e(TAG, e);
            info.setExceptionId(DownloadInfo.EXCEPTION_NO_SDCARD);
            return false;
        } catch (IOException e) {
            Logger.e(TAG, e);
            info.setExceptionId(DownloadInfo.EXCEPTION_WRITE_ERROR);
            return false;
        } finally {
            try {
                if (w != null) {
                    w.close();
                }
            } catch (IOException e) {
                Logger.e(TAG, e);
            }
        }
        return true;
    }

    /**
     * TODO 创建M3U8文件
     *
     * @param info
     * @param ifNeedUpdate
     */
    public static void makeM3U8File(DownloadInfo info) {
        Logger.d("DownloadFlow", "DownloadUtil: makeM3U8File()");
        // 如果是高端机型，下载flv分片 、mp4分片、hd2分片，采用m3u8封装方式播放。
        if (info == null
                || !((info.format == DownloadInfo.FORMAT_FLV
                || info.format == DownloadInfo.FORMAT_MP4 || info.format == DownloadInfo.FORMAT_HD2) && YoukuPlayerConfiguration.isHighEnd)) {
            return;
        }
        File f = new File(info.savePath);
        if (!f.exists()) {
            f.mkdirs();
        }
        f = new File(info.savePath + "youku.m3u8");
        if (f.exists() && f.isFile()) {
            f.delete();
        }
        BufferedWriter bw = null;
        try {
            f.createNewFile();
            bw = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(f)));
            bw.write("#PLSEXTM3U\r\n");
            bw.write("#EXT-X-TARGETDURATION:" + info.seconds + "\r\n");
            bw.write("#EXT-X-VERSION:2\r\n");
            bw.write("#EXT-X-DISCONTINUITY\r\n");
            final int[] s = info.segsSeconds;
            for (int i = 0, n = s.length; i < n; i++) {
                bw.write("#EXTINF:" + s[i] + "\r\n");
                bw.write(info.savePath + (i + 1) + "."
                        + DownloadInfo.FORMAT_POSTFIX[info.format] + "\r\n");
            }
            bw.write("#EXT-X-ENDLIST\r\n");
        } catch (IOException e) {
            Logger.e(TAG, "makeM3U8File fail", e);
        } finally {
            try {
                if (bw != null)
                    bw.close();
            } catch (IOException e) {
            }
        }
    }

    /**
     * 获得进度
     *
     * @param info
     * @return 保留一位小数
     */
    public static String getProgress(DownloadInfo info) {
        return df.format(info.getProgress());
    }

    public static String getDownloadLanguageName() {
        return LanguageBean.ALL_LANGAUGE[getDownloadLanguage()].code;
    }

    public static int getDownloadFormat() {
        int i;
        if (YoukuPlayerConfiguration.isHighEnd) {
            try {
                i = YoukuPlayerConfiguration.getPreferenceInt("definition");
            } catch (Exception e) {
                i = Integer.parseInt(YoukuPlayerConfiguration.getPreference("definition"));
            }
            if (i == 0)
                i = YoukuPlayerConfiguration.FORMAT_FLV;// 默认标清
        } else {
            i = YoukuPlayerConfiguration.FORMAT_3GPHD;
        }
        return i;
    }

    public static void setDownloadFormat(int format) {
        YoukuPlayerConfiguration.savePreference("definition", format);
    }

    public static int getDownloadLanguage() {
        return YoukuPlayerConfiguration.getPreferenceInt("cachepreferlanguage", 0);
    }

    public static void setDownloadLanguage(int language) {
        YoukuPlayerConfiguration.savePreference("cachepreferlanguage", language);
    }

    public static void doDownloadLogin(final Context context,
                                       final DownloadLoginListener mDownloadLoginListener) {
        if (YoukuPlayerConfiguration.isLogined) {
            if (mDownloadLoginListener != null) {
                mDownloadLoginListener.doDownload();
            }
        } else {
            int clickCount = getPreference(YoukuPlayerConfiguration.context,
                    SHARE_PREFERENCE_DOWNLOAD_LOGIN_CLICKCOUNT_KEY, 0);
            int maxCount = getPreference(context,
                    SHARE_PREFERENCE_DOWNLOAD_LOGIN_MAXCOUNT_KEY,
                    CLICK_MAX_COUNT);
            if (clickCount >= maxCount - 1) {
                clickCount = 0;
                savePreference(YoukuPlayerConfiguration.context,
                        SHARE_PREFERENCE_DOWNLOAD_LOGIN_CLICKCOUNT_KEY,
                        clickCount);

//				final YoukuDialog dialog = new YoukuDialog(context, TYPE.normal);
//				dialog.setMessage(R.string.download_login_tips);
//				dialog.setNormalPositiveBtn(R.string.download_login_left,
//						new View.OnClickListener() {
//							@Override
//							public void onClick(View v) {
//								dialog.dismiss();
//								if (mDownloadLoginListener != null) {
//									mDownloadLoginListener.doDownload();
//								}
//							}
//						});
//				dialog.setNormalNegtiveBtn(R.string.download_login_right,
//						new View.OnClickListener() {
//							@Override
//							public void onClick(View v) {
//								dialog.dismiss();
//								Intent intent = new Intent(context,
//										LoginActivity.class);
//								context.startActivity(intent);
//							}
//						});
//				dialog.show();
            } else {
                clickCount++;
                savePreference(context,
                        SHARE_PREFERENCE_DOWNLOAD_LOGIN_CLICKCOUNT_KEY,
                        clickCount);
                if (mDownloadLoginListener != null) {
                    mDownloadLoginListener.doDownload();
                }
            }
        }
    }

    public static void savePreference(Context context, String key, int value) {
        SharedPreferences sp = context.getSharedPreferences(
                SHARE_PREFERENCE_DOWNLOAD_LOGIN_NAME,
                UIUtils.hasGingerbread() ? Context.MODE_MULTI_PROCESS
                        : Context.MODE_PRIVATE);
        sp.edit().putInt(key, value).commit();
    }

    public static int getPreference(Context context, String key,
                                    int defaultValue) {
        SharedPreferences sp = context.getSharedPreferences(
                SHARE_PREFERENCE_DOWNLOAD_LOGIN_NAME,
                UIUtils.hasGingerbread() ? Context.MODE_MULTI_PROCESS
                        : Context.MODE_PRIVATE);
        return sp.getInt(key, defaultValue);
    }
}
