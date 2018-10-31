package com.hnjk.extend.taglib.tree;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.hnjk.core.foundation.template.FreeMarkerConfig;

import freemarker.template.Template;

/**
 *  <code>TreePickTag</code>树型选择对话框.<p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-3-24 下午05:08:33
 * @see 
 * @version 1.0
 */
public class TreePickTag extends BodyTagSupport {
	
	/** 选择类型 例如：只能选择文件夹，用folder,只能选择文件，用file */
	private String pickType;
	
	/** 选择树Id */
	private String treeId;
	
	/** 是否单选 */
	private Boolean isSingle;
	
	/** 回调函数 */
	private String callBackFun; 

	private static final long serialVersionUID = 6763998697122058971L;
	
	private Map root = new HashMap();
	
	@Override
	@SuppressWarnings("unchecked")
	public int doStartTag()throws JspException {
		try {
			Template template = FreeMarkerConfig.getDefaultTemplate("system"+File.separator+"treePickTag_start.ftl");
			//Template template = FreeMarkerConfig.getDefaultTemplate("treePickTag_start.ftl");
			root.put("pickType", pickType);
			root.put("treeId", treeId);
			root.put("id", id);
			root.put("isSingle", isSingle);
			root.put("callBackFun", callBackFun);
			template.process(root, super.pageContext.getOut());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return EVAL_BODY_INCLUDE;
	}
	
	@Override
	public int doEndTag()throws JspException {
		try {
			Template template = FreeMarkerConfig.getDefaultTemplate("system"+File.separator+"treePickTag_end.ftl");
			//Template template = FreeMarkerConfig.getDefaultTemplate("treePickTag_end.ftl");
			template.process(root, super.pageContext.getOut());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}

	public void setPickType(String pickType) {
		this.pickType = pickType;
	}

	public void setTreeId(String treeId) {
		this.treeId = treeId;
	}

	public void setIsSingle(Boolean isSingle) {
		this.isSingle = isSingle;
	}

	public void setCallBackFun(String callBackFun) {
		this.callBackFun = callBackFun;
	}

}
