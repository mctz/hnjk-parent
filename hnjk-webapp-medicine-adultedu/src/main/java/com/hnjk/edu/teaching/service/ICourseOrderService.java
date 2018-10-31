package com.hnjk.edu.teaching.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.teaching.model.CourseOrder;
/**
 * 
 * <code>学生预约课程接口Service</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-8-9 下午04:22:17
 * @see 
 * @version 1.0
 */
public interface ICourseOrderService extends IBaseService<CourseOrder>{
	/**
	 * 根据学号获取学生预约课程
	 * @param params
	 * @return
	 * @throws ServiceException
	 */
	public CourseOrder getCourseOrderByStuIdAndCourseId(String [] params) throws ServiceException;
	
	/**
	 * 根据学号查询学生是否交费
	 * @param matriculateNoticeNo
	 * @return
	 * @throws ServiceException
	 */
	public Map checkFeeByMatriculateNoticeNo(String matriculateNoticeNo) throws ServiceException;
	/**
	 * 根据学号查询学生是否交费--学生预约时使用
	 * @param matriculateNoticeNo
	 * @return
	 * @throws ServiceException
	 */
	public Map checkFeeByMatriculateNoticeNoForCourseOrder(String matriculateNoticeNo) throws ServiceException;
	/**
	 * 根据学习计划ID删除课程预约及相关的记录
	 * @param leanrPlanId
	 * @return
	 * @throws ServiceException
	 */
	public Map<String,Object> delCourseOrderByLearnPlanId(Map<String,Object> condition)throws Exception;
	
	/**
	 * 查询一个课程预约记录(删除预约)
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	public CourseOrder findCourseOrderForDelCourse(Map<String,Object> condition)throws ServiceException;
	
	/**
	 * 根据学习计划ID删除考试预约及相关的记录
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	public Map<String,Object> delExamOrderByLearnPlanId(Map<String,Object> condition)throws ServiceException;
	
	/**
	 * 根据学习计划ID删除毕业论文预约及相关的记录
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	public Map<String,Object> delGraduatePaperByLearnPlanId(Map<String,Object> condition)throws ServiceException;
	/**
	 * 预约计划外课程
	 * @param info         学籍对象
	 * @param courseId     课程ID
	 * @param orderType    预约类型
	 * @return
	 * @throws ServiceException
	 */
	public Map<String,Object> orderOutPlanCourse(StudentInfo info,String courseId,String orderType)throws ServiceException;
	
	/**
	 * 根据条件查询课程预约-返回PAGE
	 * @param condition
	 * @param page
	 * @return
	 * @throws ServiceException
	 */
	public Page findCourseOrderByCondition(Map<String,Object> condition,Page page)throws ServiceException;
	/**
	 * 根据条件查询课程预约-返回List
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	public List<CourseOrder> findCourseOrderByCondition(Map<String,Object> condition)throws ServiceException;
}
