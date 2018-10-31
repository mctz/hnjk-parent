package com.hnjk.edu.recruit.taglib;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.support.context.SpringContextHolder;
import com.hnjk.edu.basedata.service.IEdumanagerService;
import com.hnjk.edu.roll.service.IClassesService;
import com.hnjk.extend.taglib.BaseTagSupport;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;

/**
 * 班级自动填充标签.
 * <code>ClassesAutocompleteTag</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2011-3-18 下午02:13:21
 * @see 
 * @version 1.0
 */
public class ClassesAutocompleteTag extends BaseTagSupport{
	
	private static final long serialVersionUID = 1308585716821051947L;
	
	private IClassesService classesService = (IClassesService)SpringContextHolder.getBean("classesService");
	
	private IEdumanagerService edumanagerService = (IEdumanagerService)SpringContextHolder.getBean("edumanagerService");

	private String id;//ID
	
	private String name;//name
	
	private String tabindex;//排序号，唯一
	
	private String defaultValue;//默认值
	
//	private String scope = "brschool";//范围：默认为校外学习中心 ,all - 所有 brschool-校外学习中心
	
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
//		bf.append("<select  class=\"flexselect\" id="+id+" name="+name+" onchange="+onchange+" tabindex="+tabindex+"  ");
		bf.append("<select  class=\"flexselect ");
		if(ExStringUtils.isNotEmpty(classCss)){
			bf.append(" class='" + classCss + "'");
		}
		bf.append(" \" id="+id+" name="+name+" onchange="+onchange+" tabindex="+tabindex+"  ");
		if(ExStringUtils.isNotEmpty(style)){
			bf.append(" style='" + style + "'");
		}
		bf.append(" >");
		bf.append("<option value=''></option>");
		Map<String,Object> conditions = new HashMap<String, Object>();
		try {
			User user=SpringSecurityHelper.getCurrentUser();
			if(edumanagerService.isBrSchool(user)){// 是否是校外学习中心的教师
				conditions.put("brSchoolid", user.getOrgUnit().getResourceid());
			}
			if(ExStringUtils.isNotEmpty(exCondition)){
				conditions.put("addSql", exCondition);
			}
			if(ExStringUtils.isNotEmpty(orderBy)){
				conditions.put("orderBy", orderBy);
			}
			String options = classesService.constructOptions(conditions,defaultValue,true,null);
			bf.append(options);
			bf.append("</select>");
			this.pageContext.getOut().append(bf.toString());
				//SessionFactory sf = (SessionFactory) SpringContextHolder.getBean("sessionFactory");
				//Session session = sf.openSession();
//				StringBuilder hql = new StringBuilder("from " + Classes.class.getSimpleName());
//				if(ExStringUtils.isNotBlank(exCondition)){
//					hql.append(" where " + exCondition);
//				}
				//Query query = session.createQuery(hql.toString()); //HQL查询
				/*StringBuffer sql = new StringBuffer();
				sql.append("select cl.resourceid,cl.isDeleted,cl.classesname classname ")
					.append("from EDU_ROLL_CLASSES cl  ").append("where cl.isdeleted=:isdeleted");
				User user=SpringSecurityHelper.getCurrentUser();
				if(!user.getUsername().equals("administrator")){
					conditions.put("ORGUNITID", SpringSecurityHelper.getCurrentUser().getUnitId());
					sql.append(" and cl.ORGUNITID=:ORGUNITID ");
				}
				if(exCondition!=null){
					sql.append(exCondition);
				}
				IBaseSupportJdbcDao baseJdbcDao = (IBaseSupportJdbcDao)SpringContextHolder.getBean("baseSupportJdbcDao");
				List<Classes> classList = baseJdbcDao.getBaseJdbcTemplate().findList(sql.toString(), Classes.class, conditions);
				if(null != classList && classList.size()>0){
					for(Classes classes : classList){
						bf.append("<option value='"+classes.getResourceid()+"'");
						if(ExStringUtils.isNotEmpty(defaultValue) && defaultValue.equals(classes.getResourceid())){
							bf.append(" selected ");
						}
						bf.append(">"+classes.getClassname()+"</option>");
					
					}
				}
				bf.append("</select>");		
		
			this.pageContext.getOut().append(bf.toString());*/
			//session.close();
		} catch (Exception e) {
			logger.error("输出班级自动填充组件出错："+e.fillInStackTrace());
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

/*	*//**
	 * @return the scope
	 *//*
	public String getScope() {
		return scope;
	}

	*//**
	 * @param scope the scope to set
	 *//*
	public void setScope(String scope) {
		this.scope = scope;
	}*/



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
