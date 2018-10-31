package com.hnjk.edu.teaching.taglib;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.support.context.Constants;
import com.hnjk.core.support.context.SpringContextHolder;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.teaching.model.TeachingPlanCourse;
import com.hnjk.extend.taglib.BaseTagSupport;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;
/**
 * 课程自动填充组件.
 * <code>CourseAutocompleteTag</code><p>
 * 目前只适用于广外的统考课程  20180611
 * @author： 广东学苑教育发展有限公司
 * @since： 2011-4-1 上午10:19:25
 * @see 
 * @version 1.0
 */
public class TKCourseAutocompleteTag extends BaseTagSupport{

	private static final long serialVersionUID = -5111789819040143837L;

	private String id;//ID
	
	private String name;//name
	
	private String tabindex;//排序号，唯一
	
	private String value;//默认值
	
	private String style;//样式
	
	private String classCss; //样式相当html中class
	
	private String taskCondition; // 任务书条件
	
	private String condition; 
	
	private String isFilterTeacher = Constants.BOOLEAN_NO;//是否只查询任课老师列表
	
	private String displayType = "name";//默认显示类型:name-名称,code-代码+名称
		
	@Override
	public int doStartTag() throws JspException {
		StringBuffer bf = new StringBuffer();
		bf.append("<script type=\"text/javascript\">");
		bf.append("$(document).ready(function(){");
		bf.append("$(\"select[class*=flexselect]\").flexselect();");
		bf.append("});");
		bf.append("</script>");
		bf.append("<select  class='flexselect");		
		if(ExStringUtils.isNotEmpty(classCss)){
			bf.append(" "+classCss);
		}
		bf.append("' id='"+id+"' name='"+name+"' tabindex='"+tabindex+"' ");
		if(ExStringUtils.isNotEmpty(style)){
			bf.append(" style='"+style+"' ");
		}
		bf.append(" >");
		bf.append("<option value=''></option>");
		try {	
			SessionFactory sf = (SessionFactory) SpringContextHolder.getBean("sessionFactory");
			Session session   = sf.openSession();
			
			StringBuffer hql 		  = new StringBuffer("select distinct pc.course from " + TeachingPlanCourse.class.getSimpleName()+ " pc where pc.isDeleted=0 and pc.course.isDeleted=0 and pc.examClassType = '3' ");
			User user 		  = SpringSecurityHelper.getCurrentUser();
			String roleCode   = CacheAppManager.getSysConfigurationByCode("sysuser.role.teacher").getParamValue();//获取角色编码
			
			hql.append(" order by pc.course.courseCode ");
			Query query = session.createQuery(hql.toString()); 
			List list = query.list();
			
			if(null!=list && !list.isEmpty()){
				for (Object obj : list) {
					Course course = (Course)obj;
					bf.append("<option value='"+course.getResourceid()+"' title='"+course.getCourseName()+"' ");
					if(course.getResourceid().equals(value)){
						bf.append(" selected='selected' ");
					}
					if("name".equalsIgnoreCase(ExStringUtils.trimToEmpty(displayType))){
						bf.append(">"+course.getCourseName()+"</option>");
					} else {
						bf.append(">"+course.getCourseCode()+"-"+course.getCourseName()+"</option>");
					}
				}				
			}	
			
			bf.append("</select>");
			session.close();
			
			this.pageContext.getOut().append(bf.toString());
		} catch (IOException e) {
			logger.error("输出统考课程自动填充组件出错："+e.fillInStackTrace());
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

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getClassCss() {
		return classCss;
	}

	public void setClassCss(String classCss) {
		this.classCss = classCss;
	}

	public String getIsFilterTeacher() {
		return isFilterTeacher;
	}

	public void setIsFilterTeacher(String isFilterTeacher) {
		this.isFilterTeacher = isFilterTeacher;
	}

	public String getTaskCondition() {
		return taskCondition;
	}

	public void setTaskCondition(String taskCondition) {
		this.taskCondition = taskCondition;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}


	public String getDisplayType() {
		return displayType;
	}

	public void setDisplayType(String displayType) {
		this.displayType = displayType;
	}

}
