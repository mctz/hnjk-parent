package com.hnjk.platform.taglib;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.jsp.JspException;

import com.hnjk.core.foundation.template.FreeMarkerConfig;
import com.hnjk.extend.taglib.BaseTagSupport;

import freemarker.template.Template;

/**
 * 图像采集.
 * <code>WebcamTag</code><p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-10-15 下午02:00:04
 * @see 
 * @version 1.0
 */
public class webcamInputCutSizeTag extends BaseTagSupport{

	private static final long serialVersionUID = 8431163171440507878L;

	private String uploadurl ;//上传URL
	
	private String id ;//学生ID
	
	private String storePath;//存储路径
	
	private String replaceName;//替换文件名
	
	private String studentName;//学生姓名
	
	private String rectWidth;//设置截取图像宽度
	
	private String rectHeight;//设置截取图像高度
	
	@Override
	public int doStartTag() throws JspException {
		try {
			Template template = FreeMarkerConfig.getDefaultTemplate("system"+File.separator+"webcamInputCutSize.ftl");;
			Map root = new HashMap();
			root.put("basePath", getBaseUrl());
			root.put("uploadurl", uploadurl);
			root.put("id", id);
			root.put("storePath", storePath);
			root.put("replaceName", replaceName);
			root.put("studentName", studentName);
			root.put("rectWidth", rectWidth);
			root.put("rectHeight", rectHeight);
			template.process(root, super.pageContext.getOut());
		} catch (Exception e) {
			logger.error("输出图像采集出错：{}",e.fillInStackTrace());
		}
		return EVAL_PAGE;
	}

	/**
	 * @return the uploadurl
	 */
	public String getUploadurl() {
		return uploadurl;
	}

	/**
	 * @param uploadurl the uploadurl to set
	 */
	public void setUploadurl(String uploadurl) {
		this.uploadurl = uploadurl;
	}

	/**
	 * @return the id
	 */
	@Override
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	@Override
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the storePath
	 */
	public String getStorePath() {
		return storePath;
	}

	/**
	 * @param storePath the storePath to set
	 */
	public void setStorePath(String storePath) {
		this.storePath = storePath;
	}

	/**
	 * @return the replaceName
	 */
	public String getReplaceName() {
		return replaceName;
	}

	/**
	 * @param replaceName the replaceName to set
	 */
	public void setReplaceName(String replaceName) {
		this.replaceName = replaceName;
	}

	/**
	 * @return the studentName
	 */
	public String getStudentName() {
		return studentName;
	}

	/**
	 * @param studentName the studentName to set
	 */
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getRectWidth() {
		return rectWidth;
	}

	public void setRectWidth(String rectWidth) {
		this.rectWidth = rectWidth;
	}

	public String getRectHeight() {
		return rectHeight;
	}

	public void setRectHeight(String rectHeight) {
		this.rectHeight = rectHeight;
	}
}
