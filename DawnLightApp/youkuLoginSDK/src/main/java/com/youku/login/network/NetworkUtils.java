package com.youku.login.network;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Comparator;

import android.os.Environment;

import com.youku.login.util.Logger;
import com.youku.login.util.Youku;
import com.youku.login.util.YoukuUtil;

public class NetworkUtils {

	public static String URLCacheDataPath = Environment
			.getExternalStorageDirectory().getPath() + "/youku/cacheData/";

	/**
	 * 处理请求的uri信息，移除部分变量字段
	 */
	public static String formatURL(String url) {
		StringBuffer urlStr = new StringBuffer(url);
		int i = urlStr.indexOf("_t_");
		int j;
		if (i != -1) {
			// System.out.println(i);
			if ((j = urlStr.indexOf("&", i)) != -1) {
				urlStr.delete(i, j + 1);
			} else {
				urlStr.delete(i, urlStr.length());
			}
			// System.out.println(urlStr.delete(i, j + 1));
		}

		i = urlStr.indexOf("_s_");
		// System.out.println(i);
		if (i != -1) {
			if ((j = urlStr.indexOf("&", i)) != -1) {
				urlStr.delete(i, j + 1);
			} else {
				urlStr.delete(i, urlStr.length());
			}
			// System.out.println(urlStr.delete(i, j + 1));
		}
		i = urlStr.indexOf("ver");
		// System.out.println(i);
		if (i != -1) {
			if ((j = urlStr.indexOf("&", i)) != -1) {
				urlStr.delete(i, j + 1);
			} else {
				urlStr.delete(i, urlStr.length());
			}
			// System.out.println(urlStr.delete(i, j + 1));
		}
		i = urlStr.indexOf("network");
		// System.out.println(i);
		if (i != -1) {
			if ((j = urlStr.indexOf("&", i)) != -1) {
				urlStr.delete(i, j + 1);
			} else {
				urlStr.delete(i, urlStr.length());
			}
		}
		i = urlStr.indexOf("operator");
		// System.out.println(i);
		if (i != -1) {
			if ((j = urlStr.indexOf("&", i)) != -1) {
				urlStr.delete(i, j + 1);
			} else {
				urlStr.delete(i, urlStr.length());
			}
		}
		// System.out.println(urlStr.delete(i, j + 1));
		return YoukuUtil.md5(urlStr.toString());
	}

	/**
	 * 处理请求的uri信息，移除部分变量字段
	 */
	public static String formatURL(String url, boolean isSetCookie) {
		StringBuffer urlStr = new StringBuffer(url);
		int i = urlStr.indexOf("_t_");
		int j;
		if (i != -1) {
			// System.out.println(i);
			if ((j = urlStr.indexOf("&", i)) != -1) {
				urlStr.delete(i, j + 1);
			} else {
				urlStr.delete(i, urlStr.length());
			}
			// System.out.println(urlStr.delete(i, j + 1));
		}

		i = urlStr.indexOf("_s_");
		// System.out.println(i);
		if (i != -1) {
			if ((j = urlStr.indexOf("&", i)) != -1) {
				urlStr.delete(i, j + 1);
			} else {
				urlStr.delete(i, urlStr.length());
			}
			// System.out.println(urlStr.delete(i, j + 1));
		}
		i = urlStr.indexOf("ver");
		// System.out.println(i);
		if (i != -1) {
			if ((j = urlStr.indexOf("&", i)) != -1) {
				urlStr.delete(i, j + 1);
			} else {
				urlStr.delete(i, urlStr.length());
			}
			// System.out.println(urlStr.delete(i, j + 1));
		}
		i = urlStr.indexOf("network");
		// System.out.println(i);
		if (i != -1) {
			if ((j = urlStr.indexOf("&", i)) != -1) {
				urlStr.delete(i, j + 1);
			} else {
				urlStr.delete(i, urlStr.length());
			}
		}
		i = urlStr.indexOf("operator");
		// System.out.println(i);
		if (i != -1) {
			if ((j = urlStr.indexOf("&", i)) != -1) {
				urlStr.delete(i, j + 1);
			} else {
				urlStr.delete(i, urlStr.length());
			}
		}
		if (isSetCookie) {
			urlStr.append(Youku.COOKIE);
		}
		// System.out.println(urlStr.delete(i, j + 1));
		return YoukuUtil.md5(urlStr.toString());
	}

	/**
	 * 从本地读取文件，并返回字符串。
	 */
	public static String readUrlCacheFromLocal(String fileName)
			throws Exception {
		File file = new File(URLCacheDataPath + fileName);
		InputStreamReader isr = new InputStreamReader(
				new FileInputStream(file), "utf-8");
		BufferedReader read = new BufferedReader(isr);
		// FileInputStream fis = new FileInputStream(file);
		int c;
		StringBuffer sb = new StringBuffer();
		while ((c = read.read()) != -1) {
			sb.append((char) c);
		}
		read.close();

		return sb.toString();
	}

	/**
	 * 存储请求结果到本地。其中文件名 采用 经过 formatURL 方法处理的返回结果
	 */
	public static void saveUrlCacheToLocal(final String filename,
			final String content) {
		new Thread() {
			public void run() {
				File filePath = null;
				try {
					filePath = new File(URLCacheDataPath);
					if (!filePath.exists()) {
						Logger.d("testcache", filePath.mkdir() + " ");
					}
				} catch (Exception e) {

				}
				File file = new File(filePath, filename);
				OutputStream out;
				try {
					out = new FileOutputStream(file);
					out.write(content.getBytes("utf-8"));
					out.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				controlUrlCacheFilesSize(file);
			}
		}.start();

	}

	/**
	 * 保存url数据
	 * 
	 * @param filename
	 * @param eTag
	 * @param content
	 */
	public static void saveUrlCacheToLocal(String filename, String eTag,
			String content) {
		if (eTag != null && content != null) {
		Youku.savePreference(filename, eTag);
			NetworkUtils.saveUrlCacheToLocal(filename, content);
		}
	}

	/** 清楚缓存文件 */
	public static void cleanrCaches() {
		YoukuUtil.deleteFile(new File(URLCacheDataPath));
	}

	private static int CACHEDATA_SIZE = 0;

	/*
	 * 如果本地缓存的URL数据文件的大小大于10M的时候，按照最后的修改时间 删除 最旧的40%文件。
	 * 如果返回true，提示本地缓存数据的大小大于等于10M，清除旧文件 成功 如果返回false，提示本地缓存的数据大小小于10M，不清楚旧文件
	 */
	public static void controlUrlCacheFilesSize(final File file) {
		new Thread() {
			public void run() {
				File cacheFilesDir = new File(URLCacheDataPath);
				File[] cacheFiles = cacheFilesDir.listFiles();
				if (CACHEDATA_SIZE == 0) {
					if (cacheFiles == null) {
						return;
					}
					int dirSize = 0;
					for (int i = 0; i < cacheFiles.length; i++) {
						dirSize += cacheFiles[i].length();
					}
					CACHEDATA_SIZE = dirSize;
				} else {
					if (file != null) {
						CACHEDATA_SIZE = (int) (CACHEDATA_SIZE + file.length());
						Logger.d("Youku", "cacheData after add file "
								+ CACHEDATA_SIZE);
					}
				}
				if (CACHEDATA_SIZE >= 1024 * 1024 * 10) {
					int removeFactor = (int) ((0.4 * cacheFiles.length) + 1);
					System.setProperty("java.util.Arrays.useLegacyMergeSort",
							"true");
					try {
						Arrays.sort(cacheFiles, new FileLastModifSort());
					} catch (Exception e) {
						Logger.e("NetworkUtils", e);
					}
					for (int i = 0; i < removeFactor; i++) {
						cacheFiles[i].delete();
					}
					CACHEDATA_SIZE = 0;
					controlUrlCacheFilesSize(null);
				}
			}
		}.start();

	}

	static class FileLastModifSort implements Comparator<File> {
		public int compare(File arg0, File arg1) {
			if (arg0.lastModified() > arg1.lastModified()) {
				return 1;
			} else if (arg0.lastModified() == arg1.lastModified()) {
				return 0;
			} else {
				return -1;
			}
		}
	}
}
