package com.hnjk.edu.learning.service;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.learning.model.StudentLearningTrace;
/**
 * 学生学习跟踪记录服务接口.
 * <code>IStudentLearningTraceService</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2012-12-13 下午03:10:59
 * @see 
 * @version 1.0
 */
public interface IStudentLearningTraceService extends IBaseService<StudentLearningTrace> {
	/**
	 * 查询学生知识节点的学习记录
	 * @param syllabusId
	 * @param studentId
	 * @return
	 * @throws ServiceException
	 */
	StudentLearningTrace getStudentLearningTrace(String syllabusId, String studentId) throws ServiceException;
}
