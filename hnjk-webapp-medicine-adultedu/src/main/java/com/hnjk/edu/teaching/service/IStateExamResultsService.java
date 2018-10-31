package com.hnjk.edu.teaching.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.teaching.model.StateExamResults;
import com.hnjk.edu.teaching.vo.DegreeCourseResultVo;
import com.hnjk.edu.teaching.vo.StateExamResultsVo;


/**
 * 统考成绩Service
 * <code>IStateExamResultsService</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2011-1-6 上午10:31:21
 * @see 
 * @version 1.0
 */
public interface IStateExamResultsService extends IBaseService<StateExamResults> {
	
	/**
	 * 保存统考成绩认定
	 * @param stateExamResultsIds
	 * @param courseId
	 * @throws Exception
	 */
	public void maintainExamInationCourseSave(List<String> stateExamResultsIds, String courseId)throws Exception;
	
	/**
	 * 解析上传的EXCEL文件，写入临时记录表
	 * @param excelFilePath  文件路径
	 * @param attId  		   附件ID
	 * @param alp            允许的成绩选项值
	 * @return
	 */
	public Map<String,String> analysisExamInationFile(String excelFilePath, String attId, Map<String, String> alp) throws ServiceException;


	/**
	 * 根据VO导入统考成绩
	 * @param stateExamResults   统考课程Map
	 * @param replaceCourse_1          统考课程代替的课程Map
	 * @param replaceCourse_2          统考课程代替的课程Map(高起本的大英统考代替的课程)
	 * @return
	 * @throws ServiceException
	 */
	public  Map<String,String>  batchInputStateResults(Map<String, String> stateExamResults, Map<String, String> replaceCourse_1, Map<String, String> replaceCourse_2, String attId, String passDate) throws Exception;


	/**
	 * 根据条件查询统考成绩 -返回分页对象
	 * @return
	 * @throws ServiceException
	 */
	public Page findStateResultsByCondition(Map<String, Object> condition, Page objPage)throws ServiceException;

	/**
	 * 根据条件查询统考成绩 -返回集合对象
	 * @return
	 * @throws ServiceException
	 */
	public List<StateExamResults> findStateExamResultsByCondition(Map<String, Object> condition) throws ServiceException;
	/**
	 * 根据条件查询返回统考成绩列表，用于下载
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	public List<StateExamResultsVo> findStateExamResultsVoByHql(Map<String, Object> condition) throws ServiceException;
	/**
	 * 根据条件查询专业与教学计划不匹配的学生
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	public List<StateExamResultsVo> findStateExamResultsMismatching(Map<String, Object> condition) throws ServiceException;
	
	/**
	 * 根据学籍、年级和教学计划获取学位外语课程成绩信息
	 * @param studentId
	 * @param gradeId
	 * @param planId
	 * @return
	 */
	public StateExamResults findDegreeForeignLanguage(String studentId, String gradeId,String planId);
	
	/**
	 * 处理导入学位外语成绩
	 * @param degreeCourseResultList
	 * @param passDate
	 * @return
	 */
	public Map<String, Object> handleDegreeCourseResultImport(List<DegreeCourseResultVo> degreeCourseResultList,String passDate);
}
