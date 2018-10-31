package com.hnjk.edu.learning.taglib;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hnjk.core.foundation.template.FreeMarkerConfig;
import com.hnjk.core.support.context.SpringContextHolder;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.learning.model.BbsSection;
import com.hnjk.extend.taglib.BaseTagSupport;
import com.hnjk.platform.system.cache.CacheAppManager;

import freemarker.template.Template;
/**
 * 课程论坛版块
 * <code>CourseBbsSectionTag</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2011-5-16 上午10:15:55
 * @see 
 * @version 1.0
 */
public class CourseBbsSectionTag extends BaseTagSupport{

	private static final long serialVersionUID = -1849533544001702170L;
	private static Logger logger = LoggerFactory.getLogger(CourseBbsSectionTag.class);
	
	private Course course;
	
	
	@Override
	public int doStartTag() throws JspException {
		if (course == null) {
			return super.doStartTag();
		}
		
		List list = null;
		try {
			SessionFactory sf = (SessionFactory) SpringContextHolder.getBean("sessionFactory");
			Session session = sf.openSession();
			
			String feedbackSectionCode = CacheAppManager.getSysConfigurationByCode("web.bbs.sectioncode.feedback").getParamValue();//获取版块编码
			String hql = " from "+BbsSection.class.getSimpleName()+" where isDeleted=0 and isCourseSection='Y' and parent is not null and sectionCode<>:feedbackSectionCode order by sectionLevel,parent,showOrder ";			
			Query query = session.createQuery(hql.toString()); 
			query.setParameter("feedbackSectionCode", feedbackSectionCode);
			list = query.list();			
			
			session.close();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		if (list == null) {
			return super.doStartTag();
		}
		try {
			Template template = FreeMarkerConfig.getDefaultTemplate("bbs"+File.separator+"courseBbsSection.ftl");
			Map root = new HashMap();
			root.put("baseUrl", getBaseUrl());
			root.put("sections", list);
			root.put("course", course);						
			template.process(root, super.pageContext.getOut());
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return super.doStartTag();
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}
	
}
