package com.hnjk.edu.teaching.service;

import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.teaching.model.AbnormalExam;
import com.hnjk.security.model.User;

/**
 * 学生异常成绩-缓考申请表
 * <code>IAbnormalExam</code>
 * 
 * @author Zik，广东学苑教育发展有限公司
 * @since 2015-06-17 上午 11:10:11
 * @see
 * @version 1.0
 */
public interface IAbnormalExamService extends IBaseService<AbnormalExam> {
	
	/**
	 * 保存/更新异常成绩申请（目前只用于缓考）
	 * 
	 * @param map
	 * @param courseTypes
	 * @param abnormalTypes
	 * @param courseIds
	 * @param planCourseIds
	 * @param noExamIds
	 * @param scores
	 * @param AEexamTypes
	 * @param reasons
	 * @param studentId
	 * @throws Exception
	 */
	void saveAbnormalExamApply(Map<String, Object> map,String[] courseTypes, String[] abnormalTypes, String[] courseIds,
			 String[] planCourseIds,String [] noExamIds, String[] scores,String [] AEexamTypes,String [] reasons, String studentId) throws Exception;

	/**
	 * 根据条件查询异常成绩申请列表，返回 Page
	 * 
	 * @param condition
	 * @param objPage
	 * @return
	 * @throws ServiceException
	 */
	Page findAbnormalExamByCondition(Map<String, Object> condition, Page objPage) throws ServiceException;
	
	/**
	 * 批量撤销
	 * 
	 * @param abnormalExamIds
	 * @return
	 * @throws ServiceException
	 */
	Map<String ,Object> batchRevoke(String[] abnormalExamIds, User user) throws ServiceException;
	
	/**
	 * 批量审核
	 * 
	 * @param abnormalExamIds
	 * @param user
	 * @param checkStatus 审核状态
	 * @return
	 * @throws ServiceException
	 */
	Map<String ,Object> batchAudit(String[] abnormalExamIds, User user, String checkStatus) throws ServiceException;
	
	/**
	 * 保存/更新缓考申请
	 * 
	 * @param abnormalExam
	 * @param studentId
	 * @param courseId
	 * @param planCourseId
	 * @param user
	 * @return
	 * @throws ServiceException
	 */
	Map<String ,Object> saveAbnormalExamApply(AbnormalExam abnormalExam, String studentId, String courseId,
			String planCourseId, User user) throws ServiceException;
}
