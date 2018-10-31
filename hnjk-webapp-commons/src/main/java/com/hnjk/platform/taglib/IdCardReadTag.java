package com.hnjk.platform.taglib;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.jsp.JspException;

import com.hnjk.core.foundation.template.FreeMarkerConfig;
import com.hnjk.extend.taglib.BaseTagSupport;

import freemarker.template.Template;
/**
 * 身份证读取
 * <code>IdCardReadTag</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2013-3-21 上午10:13:27
 * @see 
 * @version 1.0
 */
public class IdCardReadTag extends BaseTagSupport {
	private static final long serialVersionUID = 2077730612303118612L;
	
	private String uploadurl ;//上传URL		
	private String storePath;//存储路径	
	private String jsCallbackFunc;//回调函数
	
	@Override
	public int doStartTag() throws JspException {
		try {
			Template template = FreeMarkerConfig.getDefaultTemplate("system"+File.separator+"idcardread.ftl");;
			Map<String, Object> root = new HashMap<String, Object>();
			root.put("basePath", getBaseUrl());
			root.put("uploadurl", uploadurl);
			root.put("storePath", storePath);
			root.put("jsCallbackFunc", jsCallbackFunc);
			template.process(root, super.pageContext.getOut());
		} catch (Exception e) {
			logger.error("输出身份证读取按钮出错：{}",e.fillInStackTrace());
		}
		return EVAL_PAGE;
	}
	
	public String getUploadurl() {
		return uploadurl;
	}
	public void setUploadurl(String uploadurl) {
		this.uploadurl = uploadurl;
	}
	public String getStorePath() {
		return storePath;
	}
	public void setStorePath(String storePath) {
		this.storePath = storePath;
	}	
	public String getJsCallbackFunc() {
		return jsCallbackFunc;
	}
	public void setJsCallbackFunc(String jsCallbackFunc) {
		this.jsCallbackFunc = jsCallbackFunc;
	}

}
