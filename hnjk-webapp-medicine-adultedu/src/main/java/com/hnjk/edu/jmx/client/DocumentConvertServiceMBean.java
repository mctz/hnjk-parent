package com.hnjk.edu.jmx.client;


public interface DocumentConvertServiceMBean {
	
	public final static String OBJNAME = "com.hnjk.service:name=ConvertService";
	
	/**
	 * 服务端是否允许
	 * @return
	 * @throws Exception
	 */
	public int isServerRuning(int ftpPort) throws Exception;
	
	/**
	 * 将MS word文件转成html
	 * @param srcFilePah	
	 * @return
	 * @throws ServiceException
	 */
	public String convertToHtml(String srcFilePah) throws Exception;
		
	
}
