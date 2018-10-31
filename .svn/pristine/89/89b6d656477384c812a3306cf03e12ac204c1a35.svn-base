package com.hnjk.edu.learning.service.impl;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.ExCollectionUtils;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.learning.model.StudentLearningNote;
import com.hnjk.edu.learning.service.IStudentLearningNoteService;
/**
 * 学习笔记服务接口实现.
 * <code>StudentLearningNoteServiceImpl</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2012-12-13 上午10:51:10
 * @see 
 * @version 1.0
 */
@Transactional
@Service("studentLearningNoteService")
public class StudentLearningNoteServiceImpl extends BaseServiceImpl<StudentLearningNote> implements IStudentLearningNoteService {
	
	@Override
	@Transactional(readOnly=true)
	public StudentLearningNote getStudentLearningNote(String syllabusId, String studentId) throws ServiceException {
		List<StudentLearningNote> list = findByCriteria(Restrictions.eq("isDeleted", 0),Restrictions.eq("studentInfo.resourceid", studentId),Restrictions.eq("syllabus.resourceid", syllabusId));
		return ExCollectionUtils.isNotEmpty(list)? list.get(0) : null;
	}
	
}
