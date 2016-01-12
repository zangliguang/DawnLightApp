package com.youku.login.network;


/**
 * 
 * @author 张宇
 * @create-time Oct 15, 2012 10:22:46 AM
 * @version $Id
 * 
 */
public interface IHttpRequest {

	public static final String METHOD_GET = "GET";
	public static final String METHOD_POST = "POST";

	public void request(HttpIntent httpIntent, IHttpRequestCallBack callBack);

	public <T> T parse(T dataObject);

	/**
	 * 获得接口未解析时的数据String
	 * 
	 * @return
	 */
	public String getDataString();

	public void cancel();

	public boolean isCancel();

	public abstract class IHttpRequestCallBack {

		public abstract void onSuccess(HttpRequestManager request);

		public abstract void onFailed(String failReason);

		private HttpRequestManager m;

		public final void setHttpRequestManager(HttpRequestManager m) {
			this.m = m;
		}

		public final void localLoad() {
			onLocalLoad(m);
		}

		/** 本地加载(当无网络时会自动执行，若手动执行请使用IHttpRequestCallBack{@link #localLoad()}) */
		public void onLocalLoad(IHttpRequest request) {
			onSuccess((HttpRequestManager) request);
		}

	}

}
