package com.hnjk.edu.teaching.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.teaching.model.TeachingPlanCourseStatus;
import com.hnjk.edu.teaching.model.TeachingPlanCourseTimetable;
import com.hnjk.edu.teaching.vo.TeachingPlanCourseCodeVo;
import com.hnjk.security.model.OrgUnit;
import com.hnjk.security.model.User;
/**
 * 年级教学计划课程开课服务接口.
 * <code>ITeachingPlanCourseStatusService</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2013-4-10 下午03:28:45
 * @see 
 * @version 1.0
 */
public interface ITeachingPlanCourseStatusService extends IBaseService<TeachingPlanCourseStatus> {
	/**
	 * sql分页查询年级教学计划课程开课状态
	 * @param condition
	 * @param objPage
	 * @return
	 */
	Page findTeachingPlanCourseStatusMapByCondition(Map<String, Object> condition, Page objPage) throws ServiceException;
	List<Map<String, Object>> findTeachingPlanCourseByCondition(Map<String, Object> condition) throws ServiceException;
	Page findTeachingPlanCourseStatusMapByConditionNew(	Map<String, Object> condition, Page objPage) throws ServiceException;
	List<Map<String, Object>> findTeachingPlanCourseStatusMapByConditionNew(Map<String, Object> condition) throws ServiceException;
	void saveOrUpdateCourseStatusList(List<TeachingPlanCourseStatus> courseStatusList,List<TeachingPlanCourseTimetable> timetableList) throws ServiceException;
	
	/**
	 * 开课为学生补充预约计划
	 * @param courseStatusList
	 * @throws ServiceException
	 */
	void saveOrupdateStudentInfoStuPlan(List<TeachingPlanCourseStatus> courseStatusList) throws ServiceException;
	
	/**
	 * 根据条件获取开课记录
	 * @param guiplanId 年级教学计划
	 * @param planCourseId 教学计划课程
	 * @param unitId 教学点
	 * @param isOpen 开课状态
	 * @return
	 * @throws ServiceException
	 */
	List<TeachingPlanCourseStatus> findByCondition(String guiplanId,String planCourseId,String unitId,String isOpen) throws Exception;
	
	/**
	 * 处理自动开课逻辑
	 * @param openCourseInfoList
	 * @param user
	 * @param orgUnit
	 * @return
	 * @throws Exception
	 */
	Map<String,String> handleAutoOpenCourses(List<Map<String, Object>> openCourseInfoList, User user, OrgUnit orgUnit ) throws Exception;
	
	/**
	 * 获取学生没有学习计划但已经开课的开课信息
	 * @param studentIds
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> findByStudentInfoIds(String studentIds) throws Exception;
	
	/**
	 * 根据条件获取开课年度和学期
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	Map<String, Object> findYearInfoAndTerm(Map<String, Object> condition) throws Exception;
	
	/**
	 * 设置课程教学类型
	 * @param courseTeachType
	 * @param resourceids
	 * @throws Exception
	 */
	void setCourseTeachType(String courseTeachType, String resourceids) throws Exception;
	
	/**
	 * 设置课程考试形式
	 * @param courseExamForm
	 * @param resourceids
	 * @throws Exception
	 */
	void saveCourseExamForm(String courseExamForm, String resourceids);
	
	/**
	 * 根据条件获取某条开课记录
	 * @param gradeId
	 * @param planId
	 * @param planCourseId
	 * @param schoolId
	 * @return
	 * @throws Exception
	 */
	TeachingPlanCourseStatus findOneByCondition(String gradeId,String planId, String planCourseId, String schoolId);
	
	/**
	 * 获取学习计划但已经开课的开课信息
	 * @param studentId
	 * @param gradeId
	 * @param schoolId
	 * @param teachPlanId
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> findPlanInfoByCondition(String studentId,String gradeId,String schoolId,String teachPlanId,String planCouseId) throws Exception ;
	
	/**
	 * 获取某个教学点某个年级某门课程的开课学期（年度Id和学期)
	 * @param planCourseId
	 * @param gradeId
	 * @param schoolId
	 * @param teachPlanId
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> findYearAndTerm(String planCourseId,String gradeId,String schoolId,String teachPlanId) throws Exception ;
	
	/**
	 * 根据条件获取某条开课记录
	 * @param gradeId
	 * @param planId
	 * @param courseId
	 * @param schoolId
	 * @return
	 */
	TeachingPlanCourseStatus findOneByCondition2(String gradeId,String planId, String courseId, String schoolId);
	
	/**
	 * 获取教学计划课程序列号（汕大）
	 * @param condition
	 * @return
	 */
	List<TeachingPlanCourseCodeVo> findTeachingPlanCourseCodeByCondition(Map<String, Object> condition);
	Page findTeachingPlanCourseCodeByCondition(Map<String, Object> condition,Page page);
	
}
