package com.hnjk.platform.taglib;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.jsp.JspException;

import com.hnjk.core.foundation.cache.EhCacheManager;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.extend.taglib.BaseTagSupport;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.security.cache.CacheSecManager;
import com.hnjk.security.model.OrgUnit;

/**
 * 校外学习中心自动填充标签.
 * <code>BrSchoolAutocompleteTag</code><p>
 * 
 * @author：广东学苑教育发展有限公司.
 * @since： 2011-3-18 下午02:13:21
 * @see 
 * @version 1.0
 */
public class OrgUnitAutocompleteTag extends BaseTagSupport{

	private static final long serialVersionUID = 1884420540792974726L;
	
	private String id;//ID
	
	private String name;//name
	
	private String tabindex;//排序号，唯一
	
	private String defaultValue;//默认值
	
	private String scope = "brschool";//范围：默认为校外学习中心 ,all - 所有 brschool-校外学习中心
	
	private String style;//样式
	
	private String onchange="";//js
	
	private String displayType = "name";//默认显示类型:name-名称,code-代码+名称
	
	private String classCss = "";// 样式
	
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
		bf.append(" \" id="+id+" name="+name+" onchange=\""+onchange+"\" tabindex="+tabindex+"  ");
		if(ExStringUtils.isNotEmpty(style)){
			bf.append(" style=\""+style+"\"");
		}
		bf.append(" >");
		bf.append("<option value=''></option>");
		try {
				//遍历组织单位缓存
			@SuppressWarnings("unchecked")
			List<OrgUnit> orgList = (List<OrgUnit>)EhCacheManager.getCache(CacheSecManager.CACHE_SEC_ORGS).get(CacheSecManager.CACHE_SEC_ORGS);
				if(null != orgList && orgList.size()>0){
					// TODO:自定义排序，默认按unitCode升序排序(注：以后会完善按所传的参数排序)
					Collections.sort(orgList, new Comparator<OrgUnit>() {
						@Override
						public int compare(OrgUnit unit1, OrgUnit unit2) {
							if(unit1==null || unit2==null || unit1.getUnitCode()==null ||unit2.getUnitCode() ==null){
								return 0;
							}else{
								return unit1.getUnitCode().compareTo(unit2.getUnitCode());
							}
						}
					});
					
					for(OrgUnit orgUnit : orgList){
						if("brschool".equals(scope)){
							if(!CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(orgUnit.getUnitType())){//过滤非校外学习中心的
								continue;
							}
						}
						bf.append("<option value='"+orgUnit.getResourceid()+"'");
						if(ExStringUtils.isNotEmpty(defaultValue) && defaultValue.equals(orgUnit.getResourceid())){
							bf.append(" selected ");
						}
						
						if("name".equalsIgnoreCase(ExStringUtils.trimToEmpty(displayType))){
							bf.append(">"+orgUnit.getUnitName()+"</option>");
						} else {
							bf.append(">"+orgUnit.getUnitCode()+"-"+orgUnit.getUnitName()+"</option>");
						}						
					}
				}
				bf.append("</select>");		
		
			this.pageContext.getOut().append(bf.toString());
		} catch (IOException e) {
			logger.error("输出校外学习中心自动填充组件出错："+e.fillInStackTrace());
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

	/**
	 * @return the scope
	 */
	public String getScope() {
		return scope;
	}

	/**
	 * @param scope the scope to set
	 */
	public void setScope(String scope) {
		this.scope = scope;
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



	public String getClassCss() {
		return classCss;
	}



	public void setClassCss(String classCss) {
		this.classCss = classCss;
	}
	
	

}
