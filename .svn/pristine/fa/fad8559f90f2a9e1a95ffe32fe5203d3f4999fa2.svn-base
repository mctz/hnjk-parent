package com.hnjk.edu.teaching.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.teaching.model.TeachingGuidePlan;

/**
 * 年度指导性教学计划表
 * <code>ITeachingGuidePlanService</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2010-6-25 下午03:42:36
 * @see 
 * @version 1.0
 */
public interface ITeachingGuidePlanService extends IBaseService<TeachingGuidePlan>{

	Page findTeachingGradeByCondition(Map<String, Object> condition, Page objPage);
		
	void batchCascadeDelete(String[] split) throws ServiceException;
	
	/**
	 * 获取年级教学计划中第一学期课程名
	 * @param condition
	 * @return
	 */
	public Map<String, Object> getGuideTeahingFirstTermCourseNameAndID(Map<String, Object> condition);
	
	/**
	 * 获取年级教学计划信息 如：年级计划名 学制 层次...
	 * @param condition
	 * @return
	 */
	public Map<String,Object> getGuideTeachingPlanInfo(Map<String,Object> condition);
	
	/**
	 * 查询所有年级教学计划
	 * @return
	 */
	public List<TeachingGuidePlan> findAlTeachingGradePlan();
	
	/**
	 * 审核发布年级教学计划
	 * @param teachingGuidePlan
	 * @throws ServiceException
	 */
	public void publishGuideTeachingPlan(TeachingGuidePlan teachingGuidePlan) throws ServiceException;

	Page findTeachingGradeWithUnitByCondition(Map<String, Object> condition, Page objPage);
	
	Page findGuiPlanByCondition(Map<String, Object> condition, Page objPage);
	
	/**
	 * 更新统考课程对应表
	 * @param teachingGuiPlanId
	 * @param stateCourseMap 统考课程对应表 key - 教学计划课程ID value - 统考课程ID
	 * @throws ServiceException
	 */
	//public void updateStateCourse(String teachingGuiPlanId,Map<String, String> stateCourseMap) throws ServiceException;
}
