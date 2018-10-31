package com.hnjk.core.foundation.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.net.TelnetInputStream;
import sun.net.TelnetOutputStream;
import sun.net.ftp.FtpClient;

/**
 * 提供一个ftp客户端上传的工具类. <p>
 * 使用方法:
 * <pre>FtpClient f = FtpClientUtil.getFtpConnect("192.168.2.234",21, "gdcn", "pass", "test");
	FtpClientUtil.upload("C:\\temp.doc", "test.doc");
	FtpClientUtil.close();</pre>
 * @author： 广东学苑教育发展有限公司.
 * @since： 2009-2-25下午05:32:48
 * @version 1.0
 */
public class FtpUtils {
	protected static Logger logger = LoggerFactory.getLogger(FtpUtils.class);
	
	/**ftp client 实例*/
	private static FtpClient ftpClient = null;
	
	/**
	 * 获取ftpclient
	 * @return
	 */
	public static FtpClient getFtpClient(){
		return ftpClient;
	} 
	
	/**
	 * 获取Ftp client实例.
	 * @param server 服务器IP/domain
	 * @param port 端口
	 * @param user	用户
	 * @param password	密码
	 * @param path 目标路径
	 * @return 一个已经通过认证的FTP client实例.
	 * @throws IOException
	 */
	public static void getFtpConnect(String server,int port, String user, String password,
			   String path) throws IOException{
		if(ftpClient == null){
			ftpClient = new FtpClient();			
		}
		ftpClient.openServer(server, port);
		ftpClient.login(user,password);			
		if(ExStringUtils.isNotBlank(path)){
			ftpClient.cd(path);
		}
		ftpClient.binary();	
		
	}
	
	/**
	 * 上传文件.
	 * @param srcFilename 原文件路径
	 * @param distFilename 目标文件路径
	 * @return 是否上传成功 0-成功，-1不成功
	 */
	public static int upload(String srcFilename,String distFilename){
		int ret = 0; 
		TelnetOutputStream os = null;
		FileInputStream is = null;		
		  if(null == srcFilename || "".equals(srcFilename)){
			  return -1;
		  }
		  if(null == distFilename || "".equals(distFilename)){
			  distFilename = srcFilename;
		  }
		  try{
			  os = ftpClient.put(distFilename);
	
			  java.io.File file_in = new java.io.File(srcFilename);
			  is = new FileInputStream(file_in);
			  byte[] bytes = new byte[1024];
			  int c;
			  while ((c = is.read(bytes)) != -1) {
				   os.write(bytes, 0, c);
			  }			  
		  }catch(Exception ex){
			  logger.error("Upload file "+srcFilename+" error:"+ex);
			  ret = -1;
		  } finally {
			if (is != null) {
				try{
				    is.close();
				}catch(Exception ex){}
			 }
			 if (os != null) {
				 try{
					 os.close();
				 }catch(Exception ex){}
			  }
		  }
		return ret;
	}
	
	/**
	 * 下载文件
	 * @param remotePath 远程目录
	 * @param remoteFile 远程文件
	 * @param localFile 下载为本地文件
	 * @return
	 */
	public  static int download(String remotePath, String remoteFile, String localFile) {
		int ret = 0; 
        try {
            if (remotePath.length() != 0) {
				ftpClient.cd(remotePath);
			}
            TelnetInputStream is = ftpClient.get(remoteFile);
            java.io.File file_in = new java.io.File(localFile);
            FileOutputStream os = new FileOutputStream(file_in);
            byte[] bytes = new byte[1024];
            int c;
            while ((c = is.read(bytes)) != -1) {               
                os.write(bytes, 0, c);
            }

            logger.debug("download success");
            os.close();
            is.close();
        } catch (IOException ex) {
        	ret=-1;
           logger.error(""+ex.fillInStackTrace());
        }
        return ret;
    }
	
	/**
	 * 删除FTP服务器上文件.
	 * @param filePath 文件全路径,ex: /test/t/a.txt
	 */
	public static void delete(String filePath){
		ftpClient.sendServer("DELE " + filePath + "\r\n");
	}
	
	/**
	 * 关闭资源.
	 * @throws IOException
	 */
	public static void close() throws IOException{
		ftpClient.closeServer();
		ftpClient = null;		
	}
	
}
