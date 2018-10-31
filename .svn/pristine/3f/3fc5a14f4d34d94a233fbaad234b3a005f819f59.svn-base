package com.hnjk.edu.learning.taglib;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.support.context.SpringContextHolder;
import com.hnjk.edu.learning.model.BbsSection;
import com.hnjk.extend.taglib.BaseTagSupport;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.security.SpringSecurityHelper;
/**
 * 论坛版块选择列表.
 * <code>BbsSectionSelectTag</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2011-8-16 下午05:01:27
 * @see 
 * @version 1.0
 */
public class BbsSectionSelectTag extends BaseTagSupport {

	private static final long serialVersionUID = 105449059783801480L;	
	private static Logger logger = LoggerFactory.getLogger(CourseBbsSectionTag.class);
	
	private String id;
	private String name;
	private String classCss; // 样式相当html中class
	private String style;
	private String size;
	private String value;	
	private String multiple;//多选列表
	private String scope;//范围
	private String isCourseSection;
	
	@Override
	public int doEndTag() throws JspException {
		StringBuilder sb = new StringBuilder("<select");
		if(ExStringUtils.isNotEmpty(name)){
			if (ExStringUtils.isEmpty(id)) {
				sb.append(" id='" + name + "' ");
			} else {
				sb.append(" id='" + id + "'");
			}
			sb.append(" name='"+name+"' ");
		}
		if(ExStringUtils.isNotEmpty(classCss)){
			sb.append(" class='"+classCss+"' ");
		}
		if(ExStringUtils.isNotEmpty(style)){
			sb.append(" style='"+style+"' ");
		}
		if(ExStringUtils.isNotEmpty(size)){
			sb.append(" size='"+size+"' ");
		}
		if (ExStringUtils.isNotEmpty(multiple)) {
			sb.append(" multiple='" + multiple + "'");
		}
		sb.append(" >");
		sb.append("<option value=''>选取版块</option>");
		try {	
			SessionFactory sf = (SessionFactory) SpringContextHolder.getBean("sessionFactory");
			Session session = sf.openSession();			
			
			if(ExStringUtils.isEmpty(isCourseSection) && ExStringUtils.isEmpty(scope)){ //不设定时
				if(SpringSecurityHelper.isUserInRole(CacheAppManager.getSysConfigurationByCode("sysuser.role.teacher").getParamValue())){//如果是老师
					isCourseSection = "Y";
				} 
			}
			String sql = "select * from EDU_BBS_SECTION s where s.isdeleted=0 ";
			if(ExStringUtils.isNotEmpty(isCourseSection)){
				sql += " and isCourseSection='"+isCourseSection+"' ";
			}
			sql += " start with s.parentid is null connect by prior s.resourceid=s.parentid order siblings by s.sectionlevel,s.showorder ";
			Query query = session.createSQLQuery(sql).addEntity(BbsSection.class);
			List list = query.list();
			
			if(null != list && !list.isEmpty()){
				for (Object obj : list) {
					BbsSection section = (BbsSection) obj;
					sb.append("<option value=\""+section.getResourceid()+"\" ");
					if(ExStringUtils.isNotEmpty(value) && section.getResourceid().equals(value)){
						sb.append(" selected=\"selected\"");
					}
					sb.append(">");
					if(section.getSectionLevel()==0){
						sb.append("╋");
					} else {
						for (int i = 0; i < section.getSectionLevel(); i++) {
							sb.append("&nbsp;&nbsp;");
						}
						sb.append("├");
					}					
					sb.append(section.getSectionName()+"</option>");
				}
			}
			sb.append("</select>");
			
			session.close();			
			this.pageContext.getOut().append(sb.toString());
		} catch (IOException e) {
			logger.error("输出论坛版块选择列表出错："+e.fillInStackTrace());
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

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getMultiple() {
		return multiple;
	}

	public void setMultiple(String multiple) {
		this.multiple = multiple;
	}

	public String getIsCourseSection() {
		return isCourseSection;
	}

	public void setIsCourseSection(String isCourseSection) {
		this.isCourseSection = isCourseSection;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}
	
}
