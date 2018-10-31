package com.hnjk.edu.learning.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.learning.model.ActiveCourseExam;
import com.hnjk.edu.learning.model.CourseExam;

/**
 * 随堂练习题干服务接口
 * <code>IActiveCourseExamService</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-8-4 下午04:45:11
 * @see 
 * @version 1.0
 */
public interface IActiveCourseExamService extends IBaseService<ActiveCourseExam> {
	/**
	 * 随堂练习题干分页查询
	 * @param condition
	 * @param objPage
	 * @return
	 * @throws ServiceException
	 */
	Page findActiveCourseExamByCondition(Map<String, Object> condition,Page objPage) throws ServiceException;
	
	/**
	 * 批量删除随堂练习
	 * @param ids
	 * @throws ServiceException
	 */
	void batchCascadeDelete(String[] ids) throws ServiceException;
	/**
	 * 获取该节点下习题的下一排序号
	 * @param syllabusId
	 * @return
	 * @throws ServiceException
	 */
	Integer getNextShowOrder(String syllabusId) throws ServiceException;
	/**
	 * 查询课程总练习数
	 * @param courseId
	 * @return
	 * @throws ServiceException
	 */
	Long getTotalCount(String courseId) throws ServiceException;
	/**
	 * 随堂练习列表
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	List<ActiveCourseExam> findActiveCourseExamByCondition(Map<String, Object> condition) throws ServiceException;
	/**
	 * 节点下是否已经存在该题
	 * @param syllabusId
	 * @param courseExamId
	 * @return
	 * @throws ServiceException
	 */
	boolean isExsitAcitveCoruseExam(String syllabusId,String courseExamId) throws ServiceException;
	/**
	 * 保存导入的随堂练习
	 * @param list1
	 * @param list2
	 * @throws ServiceException
	 */
	void saveImportAcitveCoruseExam(List<ActiveCourseExam> list1,List<CourseExam> list2) throws ServiceException;
	/**
	 * 移动随堂练习
	 * @param ids 移动的练习id
	 * @param fromSyllabusId 批量移动知识节点下的所有随堂练习
	 * @param toSyllabusId
	 * @throws ServiceException
	 */
	void moveActiveCourseExam(String[] ids,String fromSyllabusId,String toSyllabusId) throws ServiceException;
	
	/**
	 * 审核发布/取消发布随堂练习
	 * @param ids
	 * @param isPublished
	 * @throws ServiceException
	 */
	void publishActiveCourseExam(String[] ids,String isPublished) throws ServiceException;
	
	/**
	 * 根据条件查找课程
	 * @param sql
	 * @param parmas
	 * @return
	 * @throws ServiceException
	 */
	List<Map<String, Object>> getTeacherOnlineCourse(Map<String,Object> parmas) throws ServiceException;
	/**
	 * 根据条件查找章节中的题目数量
	 */
	public long getSyllabusCount(Map<String,Object> condition) throws ServiceException;
}
