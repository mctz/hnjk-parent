package com.hnjk.edu.finance.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/** 
 * 通联http连接
 * @author Zik, 广东学苑教育发展有限公司
 * @since 2017年2月18日 下午3:39:54 
 * 
 */
public class HttpURLConnectionTL {
	public HttpURLConnection getHttpsURLConnection(URL url) {

		try {
			HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();

			if ("https".equals(url.getProtocol())) // 如果是https协议
			{
				HttpsURLConnection httpsConn = (HttpsURLConnection) httpConnection;
				TrustManager[] managers = { new MyX509TrustManager() };// 证书过滤
				SSLContext sslContext;
				sslContext = SSLContext.getInstance("TLS");
				sslContext.init(null, managers, new SecureRandom());
				SSLSocketFactory ssf = sslContext.getSocketFactory();
				httpsConn.setSSLSocketFactory(ssf);
				httpsConn.setHostnameVerifier(new MyHostnameVerifier());// 主机名过滤
				return httpsConn;

			}
			return httpConnection;

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return null;

	}

	public class MyX509TrustManager implements X509TrustManager {

		/**
		 * 该方法体为空时信任所有客户端证书
		 * 
		 * @param chain
		 * @param authType
		 * @throws CertificateException
		 */
		@Override
		public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		}

		/**
		 * 该方法体为空时信任所有服务器证书
		 * 
		 * @param chain
		 * @param authType
		 * @throws CertificateException
		 */

		@Override
		public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		}

		/**
		 * 返回信任的证书
		 * 
		 * @return
		 */
		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}
	}

	private class MyHostnameVerifier implements HostnameVerifier {

		/**
		 * 返回true时为通过认证 当方法体为空时，信任所有的主机名
		 * 
		 * @param hostname
		 * @param session
		 * @return
		 */
		@Override
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}

	}
}
