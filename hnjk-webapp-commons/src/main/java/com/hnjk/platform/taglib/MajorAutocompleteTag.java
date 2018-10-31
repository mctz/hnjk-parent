package com.hnjk.platform.taglib;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;

import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.support.context.SpringContextHolder;
import com.hnjk.edu.basedata.model.Major;
import com.hnjk.edu.basedata.service.IMajorService;
import com.hnjk.extend.taglib.BaseTagSupport;

/**
 * 专业自动填充标签.
 * <code>MajorAutocompleteTag</code><p>
 * 
 * @author 
 */
public class MajorAutocompleteTag extends BaseTagSupport{
	
	private static final long serialVersionUID = 1308585716821051947L;
	
	private IMajorService majorService = (IMajorService)SpringContextHolder.getBean("majorService");
	
	private String id;//ID
	
	private String name;//name
	
	private String tabindex;//排序号，唯一
	
	private String defaultValue;//默认值
	
	private String style;//样式
	
	private String onchange="";//js
	
	private String displayType = "name";//默认显示类型:name-名称,code-代码+名称
	
	private String exCondition;//查询条件
	
	private String classCss = "";// 样式
	
	private String orderBy = "";// 字段排序
	
	@Override
	public int doStartTag() throws JspException {
		StringBuffer bf = new StringBuffer();
		bf.append("<script type=\"text/javascript\">");
		bf.append("$(document).ready(function(){");
		bf.append("$(\"select.flexselect\").each(function(){$(this).flexselect();});");
		bf.append("});");
		bf.append("</script>");
		bf.append("<select  class=\"flexselect ");
		if(ExStringUtils.isNotEmpty(classCss)){
			bf.append(classCss);
		}
		bf.append(" \" id='").append(id).append("' name='").append(name).append("' ");
		if(ExStringUtils.isNotEmpty(onchange)){
			bf.append("onchange='").append(onchange).append("' ");
		}
		if(ExStringUtils.isNotEmpty(tabindex)){
			bf.append("tabindex='").append(tabindex).append("' ");
		}
		if(ExStringUtils.isNotEmpty(style)){
			bf.append(" style='"+style+"' ");
		}
		bf.append(" >");
		bf.append("<option value=''></option>");
		Map<String,Object> conditions = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotEmpty(exCondition)){
				conditions.put("addSql", exCondition);
			}
			if(ExStringUtils.isNotEmpty(orderBy)){
				conditions.put("orderBy", orderBy);
			}
			List<Major> majorList = majorService.findMajorByCondition(conditions);
			String options = majorService.constructOptions(majorList, defaultValue, displayType);
			bf.append(options);
			bf.append("</select>");
			this.pageContext.getOut().append(bf.toString());
		} catch (Exception e) {
			logger.error("输出专业自动填充组件出错："+e.fillInStackTrace());
		}
		return EVAL_PAGE;
	}
	
	/**
	 * @return the style
	 */
	public String getStyle() {
		return style;
	}

	/**
	 * @param style the style to set
	 */
	public void setStyle(String style) {
		this.style = style;
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the tabindex
	 */
	public String getTabindex() {
		return tabindex;
	}

	/**
	 * @param tabindex the tabindex to set
	 */
	public void setTabindex(String tabindex) {
		this.tabindex = tabindex;
	}

	/**
	 * @return the defaultValue
	 */
	public String getDefaultValue() {
		return defaultValue;
	}

	/**
	 * @param defaultValue the defaultValue to set
	 */
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}


	public String getDisplayType() {
		return displayType;
	}

	public void setDisplayType(String displayType) {
		this.displayType = displayType;
	}

	public String getOnchange() {
		return onchange;
	}

	public void setOnchange(String onchange) {
		this.onchange = onchange;
	}

	public String getExCondition() {
		return exCondition;
	}

	public void setExCondition(String exCondition) {
		this.exCondition = exCondition;
	}

	public String getClassCss() {
		return classCss;
	}

	public void setClassCss(String classCss) {
		this.classCss = classCss;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

}
