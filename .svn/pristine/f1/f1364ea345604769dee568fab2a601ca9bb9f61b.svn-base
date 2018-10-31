package com.hnjk.edu.teaching.service;

import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.teaching.model.CourseOrderStat;

/**
 * 学生预约情况统计表，主要用来生成教学任务书。
 * <code>ICourseOrderStatService</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2010-7-19 上午11:45:39
 * @see 
 * @version 1.0
 */
public interface ICourseOrderStatService extends IBaseService<CourseOrderStat>{

	Page findCourseOrderStatByCondition(Map<String, Object> condition, Page objPage);

	//void saveCollections(List<CourseOrderStat> list, List<TeachTask> taskList);
	
	public CourseOrderStat findCourseOrderStatByYearInfoAndCourse (String yearInfo,String courseId,String term)throws ServiceException;

	void batchSaveOrUpdateCourseOrdersAndTeachTask(String[] resourceids)throws ServiceException;
}
