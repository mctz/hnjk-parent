package com.hnjk.edu.roll.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
/**
 * 学籍模块JDBC查询服务接口
 * <code>IRollJDBCService</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2012-4-12 下午03:17:32
 * @see 
 * @version 1.0
 */
public interface IRollJDBCService {

	/**
	 * * 学生缴费情况统计	 
	 * @param year
	 * @param byType schoolType=办学模式,majorClass=专业类别,classic=层次,brSchool=学习中心
	 * @param brSchool 校外学习中心
	 * @return
	 * @throws ServiceException
	 */
	 List<Map<String, Object>> statStudentFactFee(Integer year, String byType, String brSchool) throws ServiceException;
	 /**
	  * 学习中心替学生申请免修免考,查找学生的教学计划课程
	  * @param resourceid
	  * @return
	  * @throws ServiceException
	  */
	 List<Map<String,Object>> findStudentPlanCourseForNoExamApp(String resourceid)throws Exception;
	 
	 /**
	  * 申请缓考,查找学生的教学计划课程
	  * @param resourceid
	  * @return
	  * @throws ServiceException
	  */
	 List<Map<String,Object>> findPlanCourseForAbnormalExam(String resourceid)throws Exception;

	 /**
		 * 根据用户ID获取当前学籍的班级
		 * @param userId
		 * @return
		 * @throws Exception
		 */
	 public Map<String, Object> findClassesByUserId(String userId) throws Exception;
	 
	 /**
	  * 根据条件获取学籍的有关信息
	  * @param condition
	  * @return
	  * @throws ServiceException
	  */
	 public List<Map<String,Object>> findStudentInfoByCondition(Map<String, Object> condition) throws Exception;
}
