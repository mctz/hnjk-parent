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
import com.hnjk.extend.taglib.BaseTagSupport;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;
/**
 * 课程自动填充组件.
 * <code>CourseAutocompleteTag</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2011-4-1 上午10:19:25
 * @see 
 * @version 1.0
 */
public class CourseAutocompleteTag extends BaseTagSupport{

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
	
	private String hasResource;
	
	private String courseType;
	
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
			
			StringBuffer hql 		  = new StringBuffer(" from " + Course.class.getName() + " c where c.isDeleted=0 and c.status=1 ");
			if(ExStringUtils.isNotBlank(hasResource)){
				hql.append(" and c.hasResource='"+hasResource+"'");
			}
			if(ExStringUtils.isNotBlank(courseType)){
				hql.append(" and c.courseType in("+courseType+")");
			}
			if(ExStringUtils.isNotBlank(condition)){
				hql.append(" and "+condition+" ");
			}
			User user 		  = SpringSecurityHelper.getCurrentUser();
			String roleCode   = CacheAppManager.getSysConfigurationByCode("sysuser.role.teacher").getParamValue();//获取角色编码
			
			/* 版本一 isFilterTeacher与taskCondition这两个条件不是并列条件
			if(Constants.BOOLEAN_YES.equalsIgnoreCase(isFilterTeacher)){
			User user = SpringSecurityHelper.getCurrentUser();
			String roleCode = CacheAppManager.getSysConfigurationByCode("sysuser.role.teacher").getParamValue();//获取角色编码
			if(SpringSecurityHelper.isUserInRole(roleCode)){//如果是老师,只查询任课老师列表
				hql += " and exists ( from "+TeachTask.class.getSimpleName()+" t where t.isDeleted=0 and t.taskStatus=3 and t.course.resourceid=c.resourceid ";
				hql += " and (t.teacherId like '%"+user.getResourceid()+"%' or t.assistantIds like '%"+user.getResourceid()+"%' )  ";
				
				if(null!= taskCondition && !taskCondition.equals("") && taskCondition.length()>0){ //任务书条件
					String conditions[] = taskCondition.split(",");
					for(String str : conditions){
						String[] ss1 = str.split("<>");
						String[] ss2 = str.split("=");

						if(ss1.length>1 && !ss1[1].equals("''")){ 
							hql+=" and t."+str; 
						} else if(ss2.length>1 && !ss2[1].equals("''")){ 
							hql+=" and t."+str; 
						}else if(ss1.length==1 && ss2.length==1){
							hql+=" and t."+str;
						}
					}
				}
				hql += " ) ";
			}			
		    }*/	
			
			//版本二 为了适应之前的写法，且可以让isFilterTeacher与taskCondition这两个条件成为并列条件，修改如下
			/*if(Constants.BOOLEAN_YES.equalsIgnoreCase(isFilterTeacher)&&ExStringUtils.isNotBlank(taskCondition)){
				if(SpringSecurityHelper.isUserInRole(roleCode)){//如果是老师,只查询任课老师列表
					//hql += " and exists ( from "+TeachTask.class.getSimpleName()+" t where t.isDeleted=0 and t.taskStatus=3 and t.course.resourceid=c.resourceid ";
					//hql += " and (t.teacherId like '%"+user.getResourceid()+"%' or t.assistantIds like '%"+user.getResourceid()+"%' )  ";
					
//					String conditions[] = taskCondition.split(",");
//					for(String str : conditions){
//						String[] ss1 = str.split("<>");
//						String[] ss2 = str.split("=");
//
//						if(ss1.length>1 && !ss1[1].equals("''")){ 
//							hql+=" and t."+str; 
//						} else if(ss2.length>1 && !ss2[1].equals("''")){ 
//							hql+=" and t."+str; 
//						}else if(ss1.length==1 && ss2.length==1){
//							hql+=" and t."+str;
//						}
//					}
//					
//					hql += " ) ";
				}	
			}else if (Constants.BOOLEAN_YES.equalsIgnoreCase(isFilterTeacher)&&ExStringUtils.isBlank(taskCondition)) {
				if(SpringSecurityHelper.isUserInRole(roleCode)){//如果是老师,只查询任课老师列表
					//hql += " and exists ( from "+TeachTask.class.getSimpleName()+" t where t.isDeleted=0 and t.taskStatus=3 and t.course.resourceid=c.resourceid ";
					//hql += " and (t.teacherId like '%"+user.getResourceid()+"%' or t.assistantIds like '%"+user.getResourceid()+"%' )  ";
					//hql += " ) ";
				}	
			}else if (ExStringUtils.isNotBlank(taskCondition)&&!Constants.BOOLEAN_YES.equalsIgnoreCase(isFilterTeacher)) {
				//hql += " and exists ( from "+TeachTask.class.getSimpleName()+" t where t.isDeleted=0 and t.taskStatus=3 and t.course.resourceid=c.resourceid ";
				
//				String conditions[] = taskCondition.split(",");
//				for(String str : conditions){
//					String[] ss1 = str.split("<>");
//					String[] ss2 = str.split("=");
//
//					if(ss1.length>1 && !ss1[1].equals("''")){ 
//						hql+=" and t."+str; 
//					} else if(ss2.length>1 && !ss2[1].equals("''")){ 
//						hql+=" and t."+str; 
//					}else if(ss1.length==1 && ss2.length==1){
//						hql+=" and t."+str;
//					}
//				}
//				hql += " ) ";
			}*/
			
			
			hql.append(" order by c.courseCode ");
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
			logger.error("输出课程自动填充组件出错："+e.fillInStackTrace());
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

	public String getHasResource() {
		return hasResource;
	}

	public String getDisplayType() {
		return displayType;
	}

	public void setDisplayType(String displayType) {
		this.displayType = displayType;
	}

	public String isHasResource() {
		return hasResource;
	}

	public void setHasResource(String hasResource) {
		this.hasResource = hasResource;
	}

	public String getCourseType() {
		return courseType;
	}

	public void setCourseType(String courseType) {
		this.courseType = courseType;
	}
}
