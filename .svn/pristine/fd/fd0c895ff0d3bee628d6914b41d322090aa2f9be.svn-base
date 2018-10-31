package com.hnjk.core.foundation.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

import lombok.Cleanup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HttpUrlConnection工具类
 * @author ZIk
 *
 */
public class HttpUrlConnectionUtils {
	private static Logger logger = LoggerFactory.getLogger(HttpUrlConnectionUtils.class);
    private static HttpURLConnection conn;
    private static String ENCODING_UTF8="UTF-8";
	 
    /**   
     * 编码   
     * @param source   
     * @param encoding   
     * @return   
     */   
    public static String urlEncode(String source,String encoding) {    
        String result = source;    
        try {    
            result = java.net.URLEncoder.encode(source,encoding);    
        } catch (UnsupportedEncodingException e) {    
            e.printStackTrace();    
            return "0";    
        }    
        return result;    
    }  
    
    /**
     * 编码 （默认是UTF-8）
     * @param source
     * @return
     */
    public static String urlEncode(String source) {    
        String result = source;    
        try {    
            result = java.net.URLEncoder.encode(source,ENCODING_UTF8);    
        } catch (UnsupportedEncodingException e) {    
            e.printStackTrace();    
            return "0";    
        }    
        return result;    
    }  
    
    /**
     *  GET请求
     * @param strUrl
     * @return
     * */
    public static String getRequest(String strUrl) {
	    StringBuffer result = new StringBuffer("");
	    try {
		      URL url = new URL(strUrl);
		      
		      conn = (HttpURLConnection) url.openConnection();
		      conn.setDoInput(true);
		      conn.setConnectTimeout(10000);
		      conn.setReadTimeout(15000);
		      conn.setRequestProperty("accept", "*/*");
		      conn.connect();
		      @Cleanup InputStream stream=conn.getInputStream();
			  @Cleanup InputStreamReader inReader=new InputStreamReader(stream);
		      @Cleanup BufferedReader buffer=new BufferedReader(inReader);
		      String strLine=null;
		      while((strLine=buffer.readLine())!=null)
		      {
		    	  result.append(strLine);
		      }
	    } catch (Exception e) {
	      logger.error("GET请求出错", e);
	    } finally {
	    	if(conn!=null){
	    		conn.disconnect();
	    	}
	    }
	    
	    return result.toString();
	}
	
    /**
     * post请求
     * @param strUrl
     * @param param eg:data=xxx&ff=1
     * @return
     */
	  public static String postRequest(String strUrl, String param) {
		    StringBuffer result = new StringBuffer("");
		    try {
			      URL url = new URL(strUrl);
			      conn = (HttpURLConnection) url.openConnection();
			      // 设置是否从httpUrlConnection读入，默认情况下是true; 
			      conn.setDoInput(true);
			      // 设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在   
			      // http正文内，因此需要设为true, 默认情况下是false; 
			      conn.setDoOutput(true);
			      // 设定请求的方法为"POST"，默认是GET 
			      conn.setRequestMethod("POST");
			      //设置超时
			      conn.setConnectTimeout(3000);
			      conn.setReadTimeout(4000);
			      // Post 请求不能使用缓存 
			      conn.setUseCaches(false);
			      conn.setInstanceFollowRedirects(true);
			      // 设定传送的内容类型是可序列化的java对象   
			      // (如果不设此项,在传送序列化对象时,当WEB服务默认的不是这种类型时可能抛java.io.EOFException)  
			      conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
			      // 连接，从上述第2条中url.openConnection()至此的配置必须要在connect之前完成，
			      conn.connect();
			      // 获取URLConnection对象对应的输出流
				 @Cleanup OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
	             // 发送请求参数  
	             out.write(param);  
	             // flush输出流的缓冲  
	            out.flush();  
		        @Cleanup InputStream in = conn.getInputStream();
				@Cleanup InputStreamReader inStream = new InputStreamReader(in);
		        @Cleanup BufferedReader buffer = new BufferedReader(inStream);
		        String strLine=null;
		        while((strLine=buffer.readLine())!=null) {
		    	  result.append(strLine);
		        }
		    } catch (IOException ex) {
		     logger.error("进行POST请求出错", ex);
		    } finally {
		    	if(conn!=null){
		    		conn.disconnect();
		    	}
		    }
		    
		    return result.toString();
	  }
	  
	  
	  /**
	     * post请求
	     * @param strUrl
	     * @param param eg:data=xxx&ff=1
	     * @param CharsetName GBK,返回结果解码
	     * @return
	     */
		  public static String postRequestCharset(String strUrl, String param,String CharsetName) {
			    StringBuffer result = new StringBuffer("");
			    try {
				      URL url = new URL(strUrl);
				      conn = (HttpURLConnection) url.openConnection();
				      // 设置是否从httpUrlConnection读入，默认情况下是true; 
				      conn.setDoInput(true);
				      // 设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在   
				      // http正文内，因此需要设为true, 默认情况下是false; 
				      conn.setDoOutput(true);
				      // 设定请求的方法为"POST"，默认是GET 
				      conn.setRequestMethod("POST");
				      //设置超时
				      conn.setConnectTimeout(3000);
				      conn.setReadTimeout(4000);
				      // Post 请求不能使用缓存 
				      conn.setUseCaches(false);
				      conn.setInstanceFollowRedirects(true);
				      // 设定传送的内容类型是可序列化的java对象   
				      // (如果不设此项,在传送序列化对象时,当WEB服务默认的不是这种类型时可能抛java.io.EOFException)  
				      conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
				      // 连接，从上述第2条中url.openConnection()至此的配置必须要在connect之前完成，
				      conn.connect();
				      // 获取URLConnection对象对应的输出流  
					 @Cleanup OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
		             // 发送请求参数  
		             out.write(param);  
		             // flush输出流的缓冲  
		            out.flush();  
			        @Cleanup InputStream in = conn.getInputStream();
//			        inStream = new InputStreamReader(in);
					@Cleanup InputStreamReader inStream = new InputStreamReader(in,CharsetName);
			        @Cleanup BufferedReader buffer = new BufferedReader(inStream);
			        String strLine=null;
			        while((strLine=buffer.readLine())!=null) {
			    	  result.append(strLine);
			        }
			    } catch (IOException ex) {
			     logger.error("进行POST请求出错", ex);
			    } finally {
			    	if(conn!=null){
			    		conn.disconnect();
			    	}
			    }
			    return result.toString();
		  }
		  
		  /**
	       * 请求方法  
	       * 
	       * @param requestUrl
	       * @param requestMethod
	       * @param outputStr
	       * @return
	       */
	      public static String httpsRequest(String requestUrl, String requestMethod, String outputStr) {  
	            try {  
	                 
	                URL url = new URL(requestUrl);  
	                HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
	                
	                conn.setDoOutput(true);  
	                conn.setDoInput(true);  
	                conn.setUseCaches(false);  
	                // 设置请求方式（GET/POST）  
	                conn.setRequestMethod(requestMethod);  
	                conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");  
	                // 当outputStr不为null时向输出流写数据  
	                if (null != outputStr) {  
	                    @Cleanup OutputStream outputStream = conn.getOutputStream();
	                    // 注意编码格式  
	                    outputStream.write(outputStr.getBytes("UTF-8"));
	                }  
	                // 从输入流读取返回内容  
	                @Cleanup InputStream inputStream = conn.getInputStream();
	                @Cleanup InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
	                @Cleanup BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
	                String str = null;  
	                StringBuffer buffer = new StringBuffer();  
	                while ((str = bufferedReader.readLine()) != null) {  
	                    buffer.append(str);  
	                }  

	                conn.disconnect();  
	                return buffer.toString();  
	            } catch (ConnectException ce) {  
	            	System.out.println("连接超时：{}"+ ce);  
	            } catch (Exception e) {  
	                System.out.println("https请求异常：{}"+ e);  
	            }  
	            return null;  
	        }  
}
