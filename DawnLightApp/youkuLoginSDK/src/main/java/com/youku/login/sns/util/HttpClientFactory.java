package com.youku.login.sns.util;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

public class HttpClientFactory {
	private static HttpClient httpClient;
	private static int DEFAULT_SOCKET_BUFFER_SIZE = 8169;

	// ThreadSafeClientConnManager线程安全管理类
	public static synchronized HttpClient getInstance()
			throws KeyStoreException, KeyManagementException,
			UnrecoverableKeyException, NoSuchAlgorithmException,
			CertificateException, IOException {
		if (httpClient == null) {
			HttpParams httpParams = new BasicHttpParams();
			// 设置超时
			ConnManagerParams.setTimeout(httpParams, 10 * 1000);
			HttpConnectionParams.setConnectionTimeout(httpParams, 10 * 1000);
			HttpConnectionParams.setSoTimeout(httpParams, 10 * 1000);
			// 多线程最大连接数
			ConnManagerParams.setMaxConnectionsPerRoute(httpParams,
					new ConnPerRouteBean(5));
			// 多线程总连接数
			ConnManagerParams.setMaxTotalConnections(httpParams, 10);
			// 设置异常处理机制
			HttpProtocolParams.setUseExpectContinue(httpParams, true);
			// 设置是否检查旧连接
			HttpConnectionParams.setStaleCheckingEnabled(httpParams, false);
			// 设置版本
			HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
			// 设置编码
			HttpProtocolParams.setContentCharset(httpParams, HTTP.UTF_8);
			// 设置重定向
			HttpClientParams.setRedirecting(httpParams, false);
			// 设置userAgent
//			String userAgent = "Mozilla/5.0(Linux;U;Android 2.2.1;en-us;Nexus One Build.FRG83) AppleWebKit/553.1(KHTML,like Gecko) Version/4.0 Mobile Safari/533.1";
//			HttpProtocolParams.setUserAgent(httpParams, userAgent);
			// 设置是否延迟发送
			HttpConnectionParams.setTcpNoDelay(httpParams, true);
			// 设置缓存大小
			HttpConnectionParams.setSocketBufferSize(httpParams,
					DEFAULT_SOCKET_BUFFER_SIZE);

			// 允许所有证书
			KeyStore trustStore = KeyStore.getInstance(KeyStore
					.getDefaultType());
			trustStore.load(null, null);
			SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

			// 支持http与https
			SchemeRegistry schemeRegistry = new SchemeRegistry();
			schemeRegistry.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			schemeRegistry.register(new Scheme("https", sf, 443));
			// ThreadSafeClientConnManager线程安全管理类
			ClientConnectionManager manager = new ThreadSafeClientConnManager(
					httpParams, schemeRegistry);
			httpClient = new DefaultHttpClient(manager, httpParams);
		}
		return httpClient;
	}
}
