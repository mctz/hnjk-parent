package com.hnjk.core.foundation.utils;

import lombok.Cleanup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 抓取网站页面内容.
 * @author hzg.
 *
 */
public final class SnatchWebContentUtils {
		
	   private static String proxyHost;//代理服务器主机
	  
	   private static String proxyPort;//代理服务器端口
	
	   private static String proxyUser;//代理用户
	
	   private static String proxyPassword;//代理 密码
	   
	   /**
	    * 获取网页内容.
	    * @param urlString 网页URL
	    * @param charset 编码
	    * @param timeout 超时时间
	    */
	   public static String getWebContent(String urlString, final String charset, int timeout) throws IOException {
	       if (urlString == null || urlString.length() == 0) {
	           return null;
	       }
	       urlString = (urlString.startsWith("http://") || urlString.startsWith("https://")) ? urlString : ("http://" + urlString).intern();
	       URL url = new URL(urlString);

	       HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	       getProxy();
	       conn.setRequestProperty(
	               "User-Agent",
	               "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727)");//增加报头，模拟浏览器，防止屏蔽
	       conn.setRequestProperty("Accept", "text/html");//只接受text/html类型，当然也可以接受图片,pdf,*/*任意，就是tomcat/conf/web里面定义那些
	       conn.setConnectTimeout(timeout);
	       try {
	           if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
	               return null;
	           }
	       } catch (IOException e) {
	           e.printStackTrace();
	           return null;
	       }
	       @Cleanup InputStream input = conn.getInputStream();
	       @Cleanup BufferedReader reader = new BufferedReader(new InputStreamReader(input,
	               charset));
	       String line = null;
	       StringBuffer sb = new StringBuffer();
	       while ((line = reader.readLine()) != null) {
	           sb.append(line).append("\r\n");
	       }
	       if (conn != null) {
	           conn.disconnect();
	       }
	       return sb.toString();

	   }

	   /**
	    * 网页抓取方法
	    * @param urlString      要抓取的url地址
	    * @return               抓取的网页内容
	    * @throws IOException   抓取异常
	    */
	   public static String getWebContent(String urlString) throws IOException {
	       return getWebContent(urlString, "iso-8859-1", 5000);
	   }

	   /**
	    * 网页抓取方法
	    * @param urlString      要抓取的url地址
	    * @param pageCharset  目标网页编码方式
	    * @return               抓取的网页内容
	    * @throws IOException   抓取异常
	    */
	   public static String getWebContent(String urlString, String pageCharset) throws IOException {
	       String strHTML = getWebContent(urlString, "iso-8859-1", 5000);
	       if(strHTML==null || strHTML.length()==0) {
			   return strHTML;
		   }
	       String charset = getPageCharSet(strHTML);//尝试获取页面编码
	       if(ExStringUtils.isBlank(charset)) {
			   charset = pageCharset;//获取不到编码时使用默认编码
		   }
	       String StrEncode = new String(strHTML.getBytes("iso-8859-1"), charset);
	       return StrEncode;
	   }
	   /**
	    * 尝试获取页面实际编码
	    * @param strHTML
	    * @return
	    */
	   public static String getPageCharSet(String strHTML) {
		   //"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=gb2312\">";
		   try {
			   Pattern pattern = Pattern.compile("\\<meta[^>]*>(?i)");
			   Matcher matcher = pattern.matcher(strHTML);		
				while(matcher.find()){			
					String str = matcher.group().toLowerCase();		
					str = ExStringUtils.deleteWhitespace(str);
					if(str.contains("http-equiv") && str.contains("content-type") && str.contains("text/html;charset=")){
						return ExStringUtils.substringBetween(str, "text/html;charset=", "\"");
					}
				}
			} catch (Exception e) {
				
			}		   	
			return null;
	   }

	   /**
	    * 设定代理服务器
	    * @param proxyHost
	    * @param proxyPort
	    */
	   public static void setProxy(String proxyHost, String proxyPort) {
	       setProxy(proxyHost, proxyPort, null, null);
	   }

	   /**
	    * 设定代理服务器
	    * @param proxyHost       代理服务器的地址
	    * @param proxyPort       代理服务器的端口
	    * @param proxyUser       代理服务器用户名
	    * @param proxyPassword   代理服务器密码
	    */
	   public static void setProxy(String sproxyHost, String sproxyPort, String sproxyUser, String sproxyPassword) {
	       proxyHost = sproxyHost;
	       proxyPort = sproxyPort;
	       if (sproxyPassword != null && sproxyPassword.length() > 0) {
	           proxyUser = sproxyUser;
	           proxyPassword = sproxyPassword;
	       }
	   }

	   /**
	    * 取得代理设定
	    * @return
	    */
	   private static Properties getProxy() {
	       Properties propRet = null;
	       if (proxyHost != null && proxyHost.length() > 0) {
	           propRet = System.getProperties();
	           // 设置http访问要使用的代理服务器的地址
	           propRet.setProperty("http.proxyHost", proxyHost);
	           // 设置http访问要使用的代理服务器的端口
	           propRet.setProperty("http.proxyPort", proxyPort);
	           if (proxyUser != null && proxyUser.length() > 0) {
	               //用户名密码
	               propRet.setProperty("http.proxyUser", proxyUser);
	               propRet.setProperty("http.proxyPassword", proxyPassword);
	           }
	       }

	       return propRet;
	   }
	   
	   public static void main(String[] args) throws Exception{
		   String s = getWebContent("http://cs.scutde.net/t3courses/0313-cdfaalfclg/chapter01/neirong1-2.html", "gbk");     
	       System.out.println(s);
		   System.out.println(getPageCharSet(s));
	   }
}
