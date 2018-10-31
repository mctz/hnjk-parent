package com.hnjk.platform.taglib;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.ReflectionUtils;
import com.hnjk.core.support.context.SpringContextHolder;
import com.hnjk.extend.taglib.BaseTagSupport;

/**
 * 通过model指定属性生成select 列表
 * <code>SelectModelTag</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2010-3-25 下午03:30:42
 * @see 
 * @version 1.0
 */
public class SelectModelTag extends BaseTagSupport {

	private static final long serialVersionUID = -722382027206810755L;

	private String id;

	private String name;

	private String classCss; // 样式相当html中class

	private String style;

	private String disabled;

	private String onblur;

	private String onchange;

	private String onclick;

	private String ondblclick;

	private String onfocus;

	private String bindValue; // 绑定的值

	private String displayValue; // 显示的值

	private String modelClass; // 类

	private String condition; // 条件
	
	private String value;
	
	private String validate; //验证类型
	
	private String mes; //验证信息
	
	private String initVal; //默认值
	
	private String orderBy; //排序字符串
	
	private String choose="Y";
	
	private String size="10";
	
	private String multiple;//多选列表
	
	private String isSubOptionText;//是否需要截去一部分长度显示option的值
	
	private String defaultOptionText = "请选择";//默认option的显示值
	
	private String tabindex;//flexselect 专用
	
	private String exCondition;//扩展查询条件，hql字符串

	@Override
	public int doEndTag() throws JspException {
		StringBuilder sb = new StringBuilder("<select");
			
		// 默认将id 设置为 name的值  jquery 必须要id来操作
		if (name != null && !"".equals(name)) {
			if (ExStringUtils.isEmpty(id)) {
				sb.append(" id='" + name + "'");
			} else {
				sb.append(" id='" + id + "'");
			}
			sb.append(" name='" + name + "'");
		}
		if (classCss != null && !"".equals(classCss)) {
			sb.append(" class='" + classCss + "'");
		}
		if (style != null && !"".equals(style)) {
			sb.append(" style='" + style + "'");
		}
		if(ExStringUtils.isNotEmpty(tabindex)){
			sb.append(" tabindex = "+tabindex);
		}
		if (disabled != null && !"".equals(disabled) && ("true".equals(disabled) || "disabled".equals(disabled))) {
			sb.append(" disabled='" + disabled + "'");
		}
		if (onblur != null && !"".equals(onblur)) {
			sb.append(" onblur='" + onblur + "'");
		}
		if (onchange != null && !"".equals(onchange)) {
			sb.append(" onchange='" + onchange + "'");
		}
		if (onclick != null && !"".equals(onclick)) {
			sb.append(" onclick='" + onclick + "'");
		}
		if (ondblclick != null && !"".equals(ondblclick)) {
			sb.append(" ondblclick='" + ondblclick + "'");
		}
		if (onfocus != null && !"".equals(onfocus)) {
			sb.append(" onfocus='" + onfocus + "'");
		}
		if (validate != null && !"".equals(validate)) {
			sb.append(" validate='" + validate + "'");
		}
		if (mes != null && !"".equals(mes)) {
			sb.append(" mes='" + mes + "'");
		}

		if (multiple != null && !"".equals(multiple)) {
			sb.append(" multiple='" + multiple + "'");
		}
		sb.append(" >");
		if("Y".equals(choose)){
			sb.append("<option value=''>"+defaultOptionText+"</option>");
		}
		
		SessionFactory sf = (SessionFactory) SpringContextHolder.getBean("sessionFactory");
		Session session = sf.openSession();
		try {
			Object objClass = ReflectionUtils.newInstance(modelClass); //初始化对象
			StringBuilder hql = new StringBuilder("from " + objClass.getClass().getName() + " t where t.isDeleted=0");
			if(null!=exCondition && !"".equals(exCondition.trim()) && exCondition.length()>0){
				hql.append(" ").append(exCondition).append(" ");
			}
			if(null!= condition && !"".equals(condition) && condition.length()>0){ //条件
				String[] conditions = condition.split(",");
				for(String str : conditions){
					String[] ss1 = str.split("<>");
					String[] ss2 = str.split("=");
					String[] ss3 = str.split("not like");

					if(ss1.length>1 && !"''".equals(ss1[1])){
						hql.append(" and "+str); 
					} else if(ss2.length>1 && !"''".equals(ss2[1])){
						hql.append(" and "+str); 
					} else if(ss3.length>1 && !"''".equals(ss3[1])){
						hql.append(" and "+str); 
					} else if(ss1.length==1 && ss2.length==1){
						hql.append(" and "+str);
					}
				}
			}
			if(orderBy!=null && orderBy.length()>0){ //排序
				hql.append(" order by ");
				hql.append(orderBy);
			}else{
				hql.append(" order by resourceid asc");				
			}
			
			Query query = session.createQuery(hql.toString()); //HQL查询
			List list = query.list();
			if(!list.isEmpty()){
				for(int index=0;index<list.size();index++){
					Object obj = list.get(index);
					//String bindObj = ReflectionUtils.invokeGetMethod(obj, bindValue).toString(); //反射取属性GET方法中值,如取父类中属性值，请覆盖父类属性中GET方法
					//String displayObj = ReflectionUtils.invokeGetMethod(obj, displayValue).toString();					
					//改用BeanWrapper方式取属性值,可直接取到父类的属性值
					String[] displayValues = ExStringUtils.trimToEmpty(displayValue).split(","); 
					BeanWrapper wrapper = new BeanWrapperImpl(obj);
					String bindObj = wrapper.getPropertyValue(bindValue).toString();
					String displayObj = "";
					for (String value : displayValues) {
						displayObj += ExStringUtils.stripToEmpty(wrapper.getPropertyValue(value).toString())+" - ";
					}
					displayObj = displayObj.substring(0, displayObj.length()-3);
					sb.append("<option title='"+displayObj+"' value='"+ExStringUtils.stripToEmpty(bindObj)+"'");
					
					if (multiple != null && orderBy.length() > 0) {
						if(value!=null && !"".equals(value)){ //初始化默认值
							for(String str : value.split(",")){
								if(str.indexOf(bindObj)>-1) {
									sb.append(" selected='selected'");
								}
							}
						}else if(initVal!=null && initVal.length()>0){
							for(String str : initVal.split(",")){
								if(str.indexOf(bindObj)>-1) {
									sb.append(" selected='selected'");
								}
							}
						}
						
					}else{
						if(value!=null && !"".equals(value) && value.equals(bindObj)){ //初始化默认值
							if(value.indexOf(bindObj)>-1) {
								sb.append(" selected='selected'");
							}
						}else if(initVal!=null && initVal.length()>0){
							if(initVal.equals(bindObj)) {
								sb.append(" selected='selected'");
							}
						}
					}
					
					sb.append(">");
					if("Y".equals(isSubOptionText)&&displayObj.length()>10) {
						sb.append(displayObj.substring(0, 9)+"...");
					}else{
						sb.append(displayObj);
					}					
					sb.append("</option>");
				}
				
			}			
		} catch (Exception e1) {
			e1.printStackTrace();
//			logger.error("生成select列表出错：{}",e1.fillInStackTrace());
		}finally {
			session.close();
		}

		sb.append("</select>");
		JspWriter writer = this.pageContext.getOut();
		try {
			writer.append(sb.toString());
		} catch (IOException e) {
			logger.error("生成select列表出错:{}",e.fillInStackTrace());
		}
		return EVAL_PAGE;
	}

	
	
	/**
	 * @return the defaultOptionText
	 */
	public String getDefaultOptionText() {
		return defaultOptionText;
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
	 * @param defaultOptionText the defaultOptionText to set
	 */
	public void setDefaultOptionText(String defaultOptionText) {
		this.defaultOptionText = defaultOptionText;
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

	public String getClassCss() {
		return classCss;
	}

	public void setClassCss(String classCss) {
		this.classCss = classCss;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getDisabled() {
		return disabled;
	}

	public void setDisabled(String disabled) {
		this.disabled = disabled;
	}

	public String getOnblur() {
		return onblur;
	}

	public void setOnblur(String onblur) {
		this.onblur = onblur;
	}

	public String getOnchange() {
		return onchange;
	}

	public void setOnchange(String onchange) {
		this.onchange = onchange;
	}

	public String getOnclick() {
		return onclick;
	}

	public void setOnclick(String onclick) {
		this.onclick = onclick;
	}

	public String getOndblclick() {
		return ondblclick;
	}

	public void setOndblclick(String ondblclick) {
		this.ondblclick = ondblclick;
	}

	public String getOnfocus() {
		return onfocus;
	}

	public void setOnfocus(String onfocus) {
		this.onfocus = onfocus;
	}

	public String getBindValue() {
		return bindValue;
	}

	public void setBindValue(String bindValue) {
		this.bindValue = bindValue;
	}

	public String getDisplayValue() {
		return displayValue;
	}

	public void setDisplayValue(String displayValue) {
		this.displayValue = displayValue;
	}

	public String getModelClass() {
		return modelClass;
	}

	public void setModelClass(String modelClass) {
		this.modelClass = modelClass;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValidate() {
		return validate;
	}

	public void setValidate(String validate) {
		this.validate = validate;
	}

	public String getMes() {
		return mes;
	}

	public void setMes(String mes) {
		this.mes = mes;
	}

	public String getInitVal() {
		return initVal;
	}

	public void setInitVal(String initVal) {
		this.initVal = initVal;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public String getChoose() {
		return choose;
	}

	public void setChoose(String choose) {
		this.choose = choose;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}
	
	public String getMultiple() {
		return multiple;
	}

	public void setMultiple(String multiple) {
		this.multiple = multiple;
	}
	
	public String getIsSubOptionText() {
		return isSubOptionText;
	}

	public void setIsSubOptionText(String isSubOptionText) {
		this.isSubOptionText = isSubOptionText;
	}
	
	public void setExCondition(String exCondition) {
		this.exCondition = exCondition;
	}
	public String getExCondition() {
		return exCondition;
	}
}
