package com.hnjk.edu.recruit.taglib;

import java.util.List;

import javax.servlet.jsp.JspException;

import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.context.SpringContextHolder;
import com.hnjk.edu.teaching.model.ExamSub;
import com.hnjk.extend.taglib.BaseTagSupport;

public class SemesterAutoCompleteTag extends BaseTagSupport{

	private static final long serialVersionUID = 8840103506768947878L;
	
	private String id;//ID
	
	private String name;//name
	
	private String tabindex;//排序号，唯一
	
	private String defaultValue;//默认值
	
	private String scope = "brschool";//范围：默认为校外学习中心 ,all - 所有 brschool-校外学习中心
	
	private String style;//样式
	
	private String onchange="";//js
	
	private String displayType = "name";//默认显示类型:name-名称,code-代码+名称
	
	private String exCondition;//查询条件
	
	@Override
	public int doStartTag() throws JspException {
		StringBuffer bf = new StringBuffer();
		bf.append("<script type=\"text/javascript\">");
		bf.append("$(document).ready(function(){");
		bf.append("$(\"select[class*=flexselect]\").flexselect();");
		bf.append("});");
		bf.append("</script>");
		bf.append("<select  class=\"flexselect\" id="+id+" name="+name+" onchange="+onchange+" tabindex="+tabindex+"  ");
		if(ExStringUtils.isNotEmpty(style)){
			bf.append(" style="+style+"");
		}
		bf.append(" >");
		bf.append("<option value=''></option>");
		try {
				StringBuffer sql = new StringBuffer();
				sql.append("select * from  EDU_TEACH_EXAMSUB e where isDeleted='0' and batchType='exam' and examType='N' order by examinputStartTime desc");
				IBaseSupportJdbcDao baseJdbcDao = (IBaseSupportJdbcDao)SpringContextHolder.getBean("baseSupportJdbcDao");
				List<ExamSub> examSubList = baseJdbcDao.getBaseJdbcTemplate().findList(sql.toString(), ExamSub.class, null);
				if(null != examSubList && examSubList.size()>0){
					for(ExamSub classes : examSubList){
						bf.append("<option value='"+classes.getResourceid()+"'");
						if(ExStringUtils.isNotEmpty(defaultValue) && defaultValue.equals(classes.getResourceid())){
							bf.append(" selected ");
						}
						bf.append(">"+classes.getBatchName()+"</option>");
					
					}
				}
				bf.append("</select>");		
		
			this.pageContext.getOut().append(bf.toString());
			//session.close();
		} catch (Exception e) {
			logger.error("输出学期自动填充组件出错："+e.fillInStackTrace());
		}
		return EVAL_PAGE;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTabindex() {
		return tabindex;
	}

	public void setTabindex(String tabindex) {
		this.tabindex = tabindex;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getOnchange() {
		return onchange;
	}

	public void setOnchange(String onchange) {
		this.onchange = onchange;
	}

	public String getDisplayType() {
		return displayType;
	}

	public void setDisplayType(String displayType) {
		this.displayType = displayType;
	}

	public String getExCondition() {
		return exCondition;
	}

	public void setExCondition(String exCondition) {
		this.exCondition = exCondition;
	}
	
	
}
