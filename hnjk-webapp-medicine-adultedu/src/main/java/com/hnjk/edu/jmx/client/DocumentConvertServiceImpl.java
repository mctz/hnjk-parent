package com.hnjk.edu.jmx.client;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.FtpUtils;
import com.hnjk.core.foundation.utils.JmxClientUtils;
import com.hnjk.core.rao.configuration.property.ConfigPropertyUtil;

/**
 * 使用文档转换服务。
 * <p>
 * 先将本地服务器上的文件FTP到远程服务器，然后调用远程接口将文档转换，最后下载到本地服务器
 * <code>DocumentConvertService</code><p>
 * 
 * @author：广东学苑教育发展有限公司.
 * @since： 2011-10-27 下午02:25:40
 * @see 
 * @version 1.0
 */
public class DocumentConvertServiceImpl implements IDocumentConvertService {

	protected static Logger logger = LoggerFactory.getLogger(DocumentConvertServiceImpl.class);
	
	private String ftpServerIp;//FTP服务器IP
	
	private Integer ftpServerPort;//FTP服务器端口
	
	private String ftpUsername;//FTP用户名
	
	private String ftpPassword;//FTP密码
	
	
	@Override
	public boolean isServerRuning(int ftpPort) throws Exception {
		String url = "service:jmx:rmi:///jndi/rmi://"+ConfigPropertyUtil.getInstance().getProperty("jxm.rmi.clent.convert.ip")+":"+ConfigPropertyUtil.getInstance().getProperty("jxm.rmi.clent.convert.port")+"/edu3document";
		JmxClientUtils client = null;
		try{
			client = new JmxClientUtils(url);
			DocumentConvertServiceMBean convertService = client.getMBeanProxy(DocumentConvertServiceMBean.OBJNAME, DocumentConvertServiceMBean.class);
			if(convertService.isServerRuning(ftpServerPort)>0){
				return true;
			}
				
		}catch(Exception e){			
			return false;
		}finally{
			if(null != client) {
				client.close();
			}
		}
		
		return false;
	}

	@Override
	public String convertDocToHtml(String localZipFolder, String localFile, String remoteFile) throws ServiceException{
		try {
			//1.上传本地文件
			FtpUtils.getFtpConnect(ftpServerIp,ftpServerPort, ftpUsername, ftpPassword, "srcfiles");
			FtpUtils.upload(localFile, remoteFile);
			FtpUtils.close();
			logger.info(localFile+" --> upload the ftp server...ok");
			//2.调用远程接口
			String result = doConvert(remoteFile);
			logger.info("document convert of remove rmi process...ok");
			//3.下载到本地
			FtpUtils.getFtpConnect(ftpServerIp,ftpServerPort, ftpUsername, ftpPassword, "");
			FtpUtils.download("distfiles", result, localZipFolder+File.separator+result);
			logger.info(result+" --> download from ftp server ... ok");
			FtpUtils.close();
			return localZipFolder+File.separator+result;
		} catch (Exception e) {
			logger.error("convert doc to html error:"+e.fillInStackTrace());
		}
		
		return null;
	}
	
	
	
	private String doConvert(String srcFile) throws Exception{
		String url = "service:jmx:rmi:///jndi/rmi://"+ConfigPropertyUtil.getInstance().getProperty("jxm.rmi.clent.convert.ip")+":"+ConfigPropertyUtil.getInstance().getProperty("jxm.rmi.clent.convert.port")+"/edu3document";
		JmxClientUtils client = new JmxClientUtils(url);
		
		DocumentConvertServiceMBean convertService = client.getMBeanProxy(DocumentConvertServiceMBean.OBJNAME, DocumentConvertServiceMBean.class);
		String convertFilePath = convertService.convertToHtml(srcFile);
		client.close();
		return convertFilePath;
	}

	/**
	 * @return the ftpServerIp
	 */
	public String getFtpServerIp() {
		return ftpServerIp;
	}

	/**
	 * @param ftpServerIp the ftpServerIp to set
	 */
	public void setFtpServerIp(String ftpServerIp) {
		this.ftpServerIp = ftpServerIp;
	}

	/**
	 * @return the ftpServerPort
	 */
	public Integer getFtpServerPort() {
		return ftpServerPort;
	}

	/**
	 * @param ftpServerPort the ftpServerPort to set
	 */
	public void setFtpServerPort(Integer ftpServerPort) {
		this.ftpServerPort = ftpServerPort;
	}

	/**
	 * @return the ftpUsername
	 */
	public String getFtpUsername() {
		return ftpUsername;
	}

	/**
	 * @param ftpUsername the ftpUsername to set
	 */
	public void setFtpUsername(String ftpUsername) {
		this.ftpUsername = ftpUsername;
	}

	/**
	 * @return the ftpPassword
	 */
	public String getFtpPassword() {
		return ftpPassword;
	}

	/**
	 * @param ftpPassword the ftpPassword to set
	 */
	public void setFtpPassword(String ftpPassword) {
		this.ftpPassword = ftpPassword;
	}
	
	
}
