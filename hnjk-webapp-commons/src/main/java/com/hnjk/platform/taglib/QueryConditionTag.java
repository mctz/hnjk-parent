package com.hnjk.platform.taglib;

import javax.servlet.jsp.JspException;

import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.ReflectionUtils;
import com.hnjk.extend.taglib.BaseTagSupport;

/**
 *  <code>QueryConditionTag</code>列表查询条件标签.<p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-3-30 上午11:21:11
 * @see 
 * @version 1.0
 */
public class QueryConditionTag extends BaseTagSupport{

	private static final long serialVersionUID = 1L;
	
	private String queryFormName;//查询表单名
	
	private String queryColumns;//查询字段，用","分开
	
	private String isImport;//是否导入
	
	private String importJs;//导入JS function名
	
	private String isExport;//是否导出
	
	private String exportJs;//导出js function名
	
	@Override
	public int doEndTag() throws JspException{
		try {
			Object objClass = ReflectionUtils.newInstance(queryFormName);
			
		} catch (Exception e) {			
			logger.error("输出查询条件标签错误:"+e.getStackTrace());
		}
		if(!ExStringUtils.isEmpty(queryColumns)){//如果传入的查询条件不为空
			
			
		}
		return 0;
	}

	public String getExportJs() {
		return exportJs;
	}

	public void setExportJs(String exportJs) {
		this.exportJs = exportJs;
	}

	public String getImportJs() {
		return importJs;
	}

	public void setImportJs(String importJs) {
		this.importJs = importJs;
	}

	public String getIsExport() {
		return isExport;
	}

	public void setIsExport(String isExport) {
		this.isExport = isExport;
	}

	public String getIsImport() {
		return isImport;
	}

	public void setIsImport(String isImport) {
		this.isImport = isImport;
	}


	public String getQueryColumns() {
		return queryColumns;
	}

	public void setQueryColumns(String queryColumns) {
		this.queryColumns = queryColumns;
	}

	public String getQueryFormName() {
		return queryFormName;
	}

	public void setQueryFormName(String queryFormName) {
		this.queryFormName = queryFormName;
	}
	
	
}
