package com.hnjk.edu.resources.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.configuration.property.ConfigPropertyUtil;
import com.hnjk.extend.taglib.BaseTagSupport;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.SysConfiguration;
/**
 * 加载精品课程样式
 * <code>LoadCourseCssTag</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2012-12-30 下午02:10:22
 * @see 
 * @version 1.0
 */
public class LoadCourseCssTag extends BaseTagSupport {

	private static final long serialVersionUID = -2298992738710919355L;
	
	private String courseId;//课程id
	
	@Override
	public int doStartTag() throws JspException {
		try {
			StringBuilder sb = new StringBuilder();
			SysConfiguration sysConfig = null;
			if(ExStringUtils.isNotBlank(courseId)){
				sysConfig = CacheAppManager.getSysConfigurationByCode("course_"+courseId);
			}		
			String name = sysConfig != null?ExStringUtils.trimToEmpty(sysConfig.getParamValue()):"course_default";
			sb.append("<link rel=\"stylesheet\" href=\""+getrequest().getContextPath()+"/style/default/resources/"+name+"/base.css\" />\n");
			JspWriter writer = this.pageContext.getOut();
			writer.append(sb.toString());
		} catch (Exception e) {
			logger.error("载入课程样式出错：{}",e.fillInStackTrace());
		}				
		return EVAL_PAGE;
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}	
}
