package com.hnjk.platform.taglib;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;

import com.hnjk.core.foundation.template.FreeMarkerConfig;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.support.context.Constants;
import com.hnjk.extend.taglib.BaseTagSupport;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.Attachs;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;

import freemarker.template.Template;

/**
 * 大附件上传标签.
 * @author hzg
 *
 */
public class BigFileUploadTag extends BaseTagSupport{

	private static final long serialVersionUID = 3894421451861748855L;

	private String hostName =  CacheAppManager.getSysConfigurationByCode("web.bigfileupload.ftphost").getParamValue();//FTP主机	
	private int ftpport = Integer.parseInt(CacheAppManager.getSysConfigurationByCode("web.bigfileupload.ftpport").getParamValue());//FTP端口
	private String ftpuser = CacheAppManager.getSysConfigurationByCode("web.bigfileupload.ftpuser").getParamValue();//FTP用户	
	private String ftppass = CacheAppManager.getSysConfigurationByCode("web.bigfileupload.ftppass").getParamValue();//FTP密码	
	private String fileAccept = CacheAppManager.getSysConfigurationByCode("web.uploadfile.accept").getParamValue();//接受的文件类型	
	private String folderpath = CacheAppManager.getSysConfigurationByCode("web.bigfileupload.ftpforder").getParamValue();//目标文件夹	
		
	private long maxSize = 0x20000000L;//512MB 允许上传的最大尺寸	
	private String height = "250";//applet高度	
	private String width = "400";//applet宽度
	
	private String formType;//表单类型
	private String formId;//表单ID
	private String isStoreToDatabase  = Constants.BOOLEAN_YES;//是否需要存储数据库 Y(默认)|N
	private String pageType = "sys";//页面类型：sys - 系统 , other - 其他
	private String hiddenInputName = "attachId";//隐藏字段名
	transient private List<Attachs> attachList;//附件列表
	
	@Override
	public int doEndTag() throws JspException {
		try {
			Template template = FreeMarkerConfig.getDefaultTemplate("system"+File.separator+"bigfileupload.ftl");
			User cureentUser = SpringSecurityHelper.getCurrentUser();
			
			Map root = new HashMap();
			root.put("basePath", getBaseUrl());
			root.put("height", height);
			root.put("width", width);
			root.put("maxSize", maxSize);			
			root.put("fileAccept", fileAccept);
			root.put("folderpath", folderpath);
			
			root.put("hiddenInputName", hiddenInputName);
			root.put("formType", ExStringUtils.trimToEmpty(formType));
			root.put("formId", ExStringUtils.trimToEmpty(formId));
			root.put("isStoreToDatabase", isStoreToDatabase);
			root.put("pageType", pageType);
			root.put("fillinName",cureentUser.getCnName());
			root.put("fillinNameId",cureentUser.getResourceid());
			root.put("attachList",attachList);
			
			root.put("hostName", hostName);
			//root.put("ftpport", ftpport);
			root.put("ftpport", String.valueOf(ftpport)); //modify by Daniel Zhuang@20141113
			root.put("ftpuser", ftpuser);
			root.put("ftppass", ftppass);
			
			String tocken = ExStringUtils.getRandomString(6);//构造页面随机数
			root.put("tocken", tocken);
			template.process(root, super.pageContext.getOut());
		} catch (Exception e) {
			logger.error("大附件上传标签输出出错:{}",e.fillInStackTrace());
		}
		
		
		return EVAL_PAGE;
	}

	
	
	/**
	 * @return the hiddenInputName
	 */
	public String getHiddenInputName() {
		return hiddenInputName;
	}



	/**
	 * @param hiddenInputName the hiddenInputName to set
	 */
	public void setHiddenInputName(String hiddenInputName) {
		this.hiddenInputName = hiddenInputName;
	}



	/**
	 * @return the height
	 */
	public String getHeight() {
		return height;
	}



	/**
	 * @param height the height to set
	 */
	public void setHeight(String height) {
		this.height = height;
	}



	/**
	 * @return the width
	 */
	public String getWidth() {
		return width;
	}



	/**
	 * @param width the width to set
	 */
	public void setWidth(String width) {
		this.width = width;
	}



	/**
	 * @return the hostName
	 */
	public String getHostName() {
		return hostName;
	}

	/**
	 * @param hostName the hostName to set
	 */
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	/**
	 * @return the ftpport
	 */
	public int getFtpport() {
		return ftpport;
	}

	/**
	 * @param ftpport the ftpport to set
	 */
	public void setFtpport(int ftpport) {
		this.ftpport = ftpport;
	}

	/**
	 * @return the ftpuser
	 */
	public String getFtpuser() {
		return ftpuser;
	}

	/**
	 * @param ftpuser the ftpuser to set
	 */
	public void setFtpuser(String ftpuser) {
		this.ftpuser = ftpuser;
	}

	/**
	 * @return the ftppass
	 */
	public String getFtppass() {
		return ftppass;
	}

	/**
	 * @param ftppass the ftppass to set
	 */
	public void setFtppass(String ftppass) {
		this.ftppass = ftppass;
	}

	/**
	 * @return the fileAccept
	 */
	public String getFileAccept() {
		return fileAccept;
	}

	/**
	 * @param fileAccept the fileAccept to set
	 */
	public void setFileAccept(String fileAccept) {
		this.fileAccept = fileAccept;
	}

	/**
	 * @return the folderpath
	 */
	public String getFolderpath() {
		return folderpath;
	}

	/**
	 * @param folderpath the folderpath to set
	 */
	public void setFolderpath(String folderpath) {
		this.folderpath = folderpath;
	}


	/**
	 * @return the maxSize
	 */
	public long getMaxSize() {
		return maxSize;
	}

	/**
	 * @param maxSize the maxSize to set
	 */
	public void setMaxSize(long maxSize) {
		this.maxSize = maxSize;
	}



	/**
	 * @return the formType
	 */
	public String getFormType() {
		return formType;
	}



	/**
	 * @param formType the formType to set
	 */
	public void setFormType(String formType) {
		this.formType = formType;
	}



	/**
	 * @return the formId
	 */
	public String getFormId() {
		return formId;
	}



	/**
	 * @param formId the formId to set
	 */
	public void setFormId(String formId) {
		this.formId = formId;
	}



	/**
	 * @return the isStoreToDatabase
	 */
	public String getIsStoreToDatabase() {
		return isStoreToDatabase;
	}



	/**
	 * @param isStoreToDatabase the isStoreToDatabase to set
	 */
	public void setIsStoreToDatabase(String isStoreToDatabase) {
		this.isStoreToDatabase = isStoreToDatabase;
	}



	/**
	 * @return the pageType
	 */
	public String getPageType() {
		return pageType;
	}



	/**
	 * @param pageType the pageType to set
	 */
	public void setPageType(String pageType) {
		this.pageType = pageType;
	}



	/**
	 * @return the attachList
	 */
	public List<Attachs> getAttachList() {
		return attachList;
	}



	/**
	 * @param attachList the attachList to set
	 */
	public void setAttachList(List<Attachs> attachList) {
		this.attachList = attachList;
	}
	
	
}
