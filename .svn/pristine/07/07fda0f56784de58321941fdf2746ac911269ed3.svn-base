package com.hnjk.edu.teaching.service;

import java.util.Map;

import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.basedata.model.Course;

/**
 * 考试计划设置课程明细表
 * <code>IExamCourseService</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-8-17 下午03:58:46
 * @see 
 * @version 1.0
 */
public interface IExamCourseService  extends IBaseService<Course>{

	Page findExamCourseByCondition(Map<String, Object> condition, Page objPage);

	@Override
	void batchDelete(String[] ids);
}
