package com.hnjk.edu.jmx.client;

import com.hnjk.core.exception.ServiceException;

public interface IDocumentConvertService {

	public boolean isServerRuning(int ftpPort) throws Exception;
	
	/**
	 * 将DOC转化为html并打包成zip
	 * @param localZipFolder 将打包好的html存放在服务器何处
	 * @param localFile 需要转换的doc文件
	 * @param remoteFile 上传到远程ftp的文件
	 * @return 转换好的zip包
	 * @throws ServiceException
	 */
	public String convertDocToHtml(String localZipFolder,String localFile,String remoteFile) throws ServiceException;
}
