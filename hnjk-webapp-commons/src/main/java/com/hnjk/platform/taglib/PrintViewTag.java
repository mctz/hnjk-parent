package com.hnjk.platform.taglib;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.jsp.JspException;

import com.hnjk.core.foundation.template.FreeMarkerConfig;
import com.hnjk.extend.taglib.BaseTagSupport;

import freemarker.template.Template;

/**
 * 打印预览.
 * <code>PrintViewTag</code><p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-10-15 下午02:00:04
 * @see 
 * @version 1.0
 */
public class PrintViewTag extends BaseTagSupport{

	private static final long serialVersionUID = 8431163171440507878L;

	private String height = "100%";//高度
	
	private String width = "100%";//宽度
	
	private String reportUrl;//打印URL
	
	@Override
	public int doStartTag() throws JspException {
		try {
			Template template = FreeMarkerConfig.getDefaultTemplate("system"+File.separator+"printview.ftl");;
			Map root = new HashMap();
			root.put("basePath", getBaseUrl());
			root.put("height", height);
			root.put("width", width);
			root.put("reportUrl", reportUrl);
			//sessioinid
			root.put("SID", getrequest().getSession(false).getId());
			template.process(root, super.pageContext.getOut());
		} catch (Exception e) {
			logger.error("输出打印预览出错：{}",e.fillInStackTrace());
		}
		return EVAL_PAGE;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getReportUrl() {
		return reportUrl;
	}

	public void setReportUrl(String reportUrl) {
		this.reportUrl = reportUrl;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}
	
	
}
