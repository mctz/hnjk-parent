package com.hnjk.edu.learning.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.exception.SetFirstTermCourseForStuRegException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.learning.model.StudentLearnPlan;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.teaching.model.TeachingActivityTimeSetting;
import com.hnjk.edu.teaching.model.TeachingPlan;
import com.hnjk.edu.teaching.model.UsualResults;
/**
 * 
 * <code>学员学习计划IStudentLearnPlanService</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-8-6 下午03:23:08
 * @see 
 * @version 1.0
 */
public interface IStudentLearnPlanService extends IBaseService<StudentLearnPlan> {
	/**
	 * 根据学籍ID获取学员的学习计划
	 * @param studentId
	 * @param teachingPlanId
	 * @return
	 * @throws ServiceException
	 */
	public List<StudentLearnPlan> getStudentLearnPlanList(String studentId,String teachingPlanId)throws ServiceException;

	public Page findStudentLearnPlanByCondition(Map<String,Object> paramMap,Page objePage)throws ServiceException;
	/**
	 * 根据学籍ID，教学计划ID获取学生学习计划(SQL)
	 * @param studentId
	 * @param teachingPlanId
	 * @return
	 * @throws Exception
	 */
	public List<StudentLearnPlan> getStudentLearnPlanListBySql(String studentId,String teachingPlanId)throws Exception;
	/**
	 * 根据学籍ID获取学员的学习计划(计划外课程)
	 * @param studentId
	 * @param teachingPlanId
	 * @return
	 * @throws ServiceException
	 */
	public List<StudentLearnPlan> getStudentPlanOutLearnPlanList(String studentId)throws ServiceException;
	/**
	 * 把学生的教学计划及学习计划转换为TeachingPlanVo对象集合，供页面中以ajax的形式调用
	 * @param learnPlanList
	 * @param teachingPlan
	 * @return
	 * @throws ServiceException
	 */
	public Map<String,Object> changeTeachingPlanToTeachingPlanVo(StudentInfo stu,List<StudentLearnPlan> learnPlanList,TeachingPlan teachingPlan)throws ServiceException;

	
	/**
	 * 预约学习-检查是否允许预约所选课程
	 * @param teachingPlanId
	 * @param teachingPlanCourseId
	 * @param teachingPlanCourseStatus
	 * @return Map
	 * @throws ServiceException
	 */
	public Map<String, Object> isAllowOrderCourse(Map<String, Object> paramMap)throws ServiceException;
	
	/**
	 * 预约学习-条件满足时的增加或更新操作
	 * @param paramMap
	 * @return
	 * @throws ServiceException
	 */
	public Map<String,Object> allowOrderCourseOperate(Map<String, Object> paramMap)throws ServiceException;
	
	/**
	 * 预约考试-检查是否允许预约考试
	 * @param paramMap
	 * @return
	 * @throws ServiceException
	 */
	public Map<String,Object> isAllowOrderExam(Map<String, Object> paramMap)throws ServiceException;
	
	/**
	 * 预约考试-条件满足时的增加或更新操作
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> allowOrderExamOperate(Map<String, Object> paramMap)throws ServiceException;
	
	/**
	 * 预约毕业论文-检查是否允许预约毕业论文
	 * @param paramMap
	 * @return
	 * @throws ServiceException
	 */
	public Map<String,Object> isAllowOrderGraduatePaper(Map<String, Object> paramMap)throws ServiceException;
	/**
	 * 预约毕业论文-条件满足时的增加或更新操作
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> allowOrderGraduatePaper(Map<String, Object> paramMap)throws ServiceException;
	
	/**
	 * 注册学籍时预约第一学期的课程及相关操作
	 * @param studentInfo
	 * @throws SetFirstTermCourseForStuRegException
	 */
	public Map<String,Object> setFirstTermCourseOperaterForStudentReg(Map<String, Object> paramMap)throws SetFirstTermCourseForStuRegException;

	/**
	 * 根据条件查询学生学习计划
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	List<StudentLearnPlan> findStudentLearnPlanByCondition(Map<String,Object> condition)throws ServiceException;
	
	/**
	 * 检查是否达到预约上限
	 * @param paramMap
	 * @return
	 */
	public Map<String, Object> checkIsFullInStudentLearnPlan(Map<String, Object> paramMap);
	/**
	 * 获取当前正在学习该门课程的学生人数
	 * @param courseId
	 * @return
	 * @throws ServiceException
	 */
	Integer getOrderCourseNum(String courseId,String yearInfoId,String term) throws ServiceException;
	/**
	 * 获取学生对应课程的学习计划
	 * @param courseId
	 * @param id
	 * @param idType 学籍id=studentId 用户id=userId
	 * @return
	 * @throws ServiceException
	 */
	StudentLearnPlan getStudentLearnPlanByCourse(String courseId,String id,String idType) throws ServiceException;
	
	
	/**
	 * 查询默认年级下类型为学籍卡修改开放时间的活动信息
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	List<TeachingActivityTimeSetting> findTeachingactivitytime(Map<String,Object> condition)throws ServiceException;
	
	
	
	 /**
	 * 获取当前正在学习该门课程的学生人数
	 * @param courseId
	 * @return
	 * @throws ServiceException
	 */
	Integer getOnlineStudentNum(String courseId,String yearInfoId,String term,String teacherid,String classids) throws ServiceException;
	
	public Page findStudentBBSgroupLeader(Map<String, Object> paramMap,Page objePage) throws ServiceException;
	 
	/**
	 * 为学生创建学习计划
	 * @param studentInfoList
	 * @param studentIds
	 * @throws Exception
	 */
	public void generateStuPlan(List<StudentInfo> studentInfoList, String studentIds,List<UsualResults> urs) throws Exception;
	
	/**
	 * 根据条件创建学习计划
	 * @param studentInfoList
	 * @param studentId
	 * @param gradeId
	 * @param schoolId
	 * @param teachPlanId
	 * @throws Exception
	 */
	public void generateStudyPlanByCondition(List<StudentInfo> studentInfoList, String studentId, String gradeId, String schoolId, String teachPlanId, String planCourseId,List<UsualResults> urs) throws Exception;
}
