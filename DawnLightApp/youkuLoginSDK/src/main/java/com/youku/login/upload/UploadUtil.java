package com.youku.login.upload;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;



public class UploadUtil {

//	public static void out(String str) {
//		if(UploadConfig.DEBUG_MODE_OPENED)
//			Logger.d("YoukuUpload", str);
//	}
//	public static ArrayList<Category> getCatesFromXml(InputStream inputStream) {
//		if (inputStream == null)
//			return null;
//		ArrayList<Category> cates = new ArrayList<Category>();
//		DocumentBuilderFactory factory = null;
//		DocumentBuilder builder = null;
//		Document document = null;
//		factory = DocumentBuilderFactory.newInstance();
//		try {
//			builder = factory.newDocumentBuilder();
//			document = builder.parse(inputStream);
//
//			Element root = document.getDocumentElement();
//			NodeList nodes = root.getElementsByTagName("item");
//
//			Category cate = null;
//			for (int i = 0; i < nodes.getLength(); i++) {
//				Element cateElement = (Element) (nodes.item(i));
//				cate = new Category();
//
//				Element id = (Element) cateElement.getElementsByTagName("id")
//						.item(0);
//				cate.setId(Integer.parseInt(id.getFirstChild().getNodeValue()));
//
//				Element name = (Element) cateElement.getElementsByTagName(
//						"name").item(0);
//				cate.setName(name.getFirstChild().getNodeValue());
//
//				cates.add(cate);
//			}
//		} catch (IOException e) {
//			Logger.e("UploadUtil.getCatesFromXml()", e);
//		} catch (SAXException e) {
//			Logger.e("UploadUtil.getCatesFromXml()", e);
//		} catch (ParserConfigurationException e) {
//			Logger.e("UploadUtil.getCatesFromXml()", e);
//		} finally {
//			try {
//				if (inputStream != null)
//					inputStream.close();
//			} catch (IOException e) {
//				Logger.e("UploadUtil.getCatesFromXml()", e);
//			}
//		}
//		return cates;
//	}
//
//
//	public static InetAddress getServerIP(String url) {
//		try {
//			return InetAddress.getByName(url);
//		} catch (UnknownHostException e) {
//			return null;
//		}
//	}
//	public static String getFileMD5String(String fileName) {
//		InputStream fis;
//		try {
//			fis = new FileInputStream(fileName);
//			byte[] buffer = new byte[1024];
//			MessageDigest md5 = MessageDigest.getInstance("MD5");
//			int numRead = 0;
//			while ((numRead = fis.read(buffer)) > 0) {
//				md5.update(buffer, 0, numRead);
//			}
//			fis.close();
//			return bufferToHex(md5.digest());
//		} catch (FileNotFoundException e) {
//			Logger.e("UploadUtil.getFileMD5String()", e);
//			e.printStackTrace();
//		} catch (NoSuchAlgorithmException e) {
//			Logger.e("UploadUtil.getFileMD5String()", e);
//			e.printStackTrace();
//		} catch (IOException e) {
//			Logger.e("UploadUtil.getFileMD5String()", e);
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//	private static String bufferToHex(byte bytes[]) {
//		return bufferToHex(bytes, 0, bytes.length);
//	}
//
//	private static String bufferToHex(byte bytes[], int m, int n) {
//		StringBuffer stringbuffer = new StringBuffer(2 * n);
//		int k = m + n;
//		for (int l = m; l < k; l++) {
//			appendHexPair(bytes[l], stringbuffer);
//		}
//		return stringbuffer.toString();
//	}
//
//	private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
//		char c0 = md5Chars[(bt & 0xf0) >> 4];
//		char c1 = md5Chars[bt & 0xf];
//		stringbuffer.append(c0);
//		stringbuffer.append(c1);
//	}
//
//	private static char md5Chars[] = { '0', '1', '2', '3', '4', '5', '6', '7',
//			'8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
//	
//	/**
//	 * https请求中，信任所有主机-对于任何证书都不做检查
//	 * @Title: trustAllHosts
//	 * @return void
//	 * @date 2012-3-6 下午7:47:41
//	 */
//	public static void trustAllHosts() {
//		// Create a trust manager that does not validate certificate chains
//		// Android 采用X509的证书信息机制
//		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
//			
//			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
//				return new java.security.cert.X509Certificate[] {};
//			}
//
//			@Override
//			public void checkClientTrusted(
//					java.security.cert.X509Certificate[] chain, String authType)
//					throws java.security.cert.CertificateException {
//				// TODO Auto-generated method stub
//
//			}
//
//			@Override
//			public void checkServerTrusted(
//					java.security.cert.X509Certificate[] chain, String authType)
//					throws java.security.cert.CertificateException {
//				// TODO Auto-generated method stub
//				
//			}
//		}};
//
//		// Install the all-trusting trust manager
//		try {
//			SSLContext sc = SSLContext.getInstance("TLS");
//			sc.init(null, trustAllCerts, new java.security.SecureRandom());
//			HttpsURLConnection
//					.setDefaultSSLSocketFactory(sc.getSocketFactory());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
	public static String list2String(List<Integer> list){
		if(list == null || list.size() == 0)
			return "";
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < list.size(); i++){
			Integer obj = list.get(i);
			sb.append(obj.toString());
			if(i != list.size() - 1)
				sb.append(",");
		}
		return sb.toString();
	}
	
	public static List<Integer> string2List(String list){
		List<Integer> array = new ArrayList<Integer>();
		if(list == null || list.equals(""))
			return array;
		String[] lists = list.split(",");
		for(String s : lists){
			array.add(Integer.parseInt(s));
		}
		return array;
	}
	
	public static String parseSize(long size){
		long sizeKB = size/1024;
		if(sizeKB >= 1024 * 1024){
			float sizeKBFloat = Float.parseFloat(sizeKB + "");
			float sizeGB = new BigDecimal(sizeKBFloat/1024f/1024f).setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
			return sizeGB + "GB";
		}else if(sizeKB >= 1024){
			float sizeKBFloat = Float.parseFloat(sizeKB + "");
			float sizeMB = new BigDecimal(sizeKBFloat/1024f).setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
			return sizeMB + "MB";
		}else
			return sizeKB + "KB";
	}
//	
//	public static String parseSpeed(int speed){
//		if(speed >= 1024){
//			float speedFloat = Float.parseFloat(speed + "");
//			float speedM = new BigDecimal(speedFloat/1024f).setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
//			return speedM + "MB/S";
//		}else
//			return speed + "KB/S";
//	}
//	
//	public static int[] parseTime(int second){
//		if (second >= 3600)
//			return new int[]{second / 3600, R.string.hour};
//		if (second >= 60)
//			return new int[]{second / 60, R.string.minute};
//		return new int[]{second, R.string.second};
//	}
//	
//	public static Bitmap loadThumbnail(Activity activity, String videoName) {
//		String[] proj = { MediaStore.Video.Media._ID,
//				MediaStore.Video.Media.DISPLAY_NAME };
//		Cursor videocursor = activity.managedQuery(
//				MediaStore.Video.Media.EXTERNAL_CONTENT_URI, proj,
//				MediaStore.Video.Media.DISPLAY_NAME + "='" + videoName + "'",
//				null, null);
//		Bitmap curThumb = null;
//
//		if (videocursor.getCount() > 0) {
//			videocursor.moveToFirst();
//			ContentResolver crThumb = activity.getContentResolver();
//			BitmapFactory.Options options = new BitmapFactory.Options();
//			options.inSampleSize = 1;
//			curThumb = MediaStore.Video.Thumbnails.getThumbnail(crThumb,
//					videocursor.getInt(0),
//					MediaStore.Video.Thumbnails.MICRO_KIND, options);
//
//		}
//		videocursor.close();
//		return curThumb;
//	}
//	
//	public static Bitmap getRCB(Bitmap bitmap, float roundPX) {
//		Bitmap dstbmp = Bitmap.createBitmap(bitmap.getWidth(),
//				bitmap.getHeight(), Config.ARGB_8888);
//		Canvas canvas = new Canvas(dstbmp);
//
//		final int color = 0xff424242;
//		final Paint paint = new Paint();
//		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
//		final RectF rectF = new RectF(rect);
//		paint.setAntiAlias(true);
//		canvas.drawARGB(0, 0, 0, 0);
//		paint.setColor(color);
//		canvas.drawRoundRect(rectF, roundPX, roundPX, paint);
//		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
//		canvas.drawBitmap(bitmap, rect, rect, paint);
//
//		return dstbmp;
//	}
}
