package com.hnjk.edu.learning.service;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.learning.model.StudentLearningNote;
/**
 * 学习笔记服务接口.
 * <code>IStudentLearningNoteService</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2012-12-13 上午10:50:04
 * @see 
 * @version 1.0
 */
public interface IStudentLearningNoteService extends IBaseService<StudentLearningNote>{
	/**
	 * 获取知识节点下的学习笔记.
	 * @param syllabusId
	 * @param studentId
	 * @return
	 * @throws ServiceException
	 */
	StudentLearningNote getStudentLearningNote(String syllabusId, String studentId) throws ServiceException;
}
