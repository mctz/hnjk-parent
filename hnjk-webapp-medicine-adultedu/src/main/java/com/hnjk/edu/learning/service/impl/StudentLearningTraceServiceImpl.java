package com.hnjk.edu.learning.service.impl;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.ExCollectionUtils;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.learning.model.StudentLearningTrace;
import com.hnjk.edu.learning.service.IStudentLearningTraceService;
/**
 * 学生学习跟踪记录服务接口实现.
 * <code>StudentLearningTraceServiceImpl</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2012-12-13 下午03:13:11
 * @see 
 * @version 1.0
 */
@Transactional
@Service("studentLearningTraceService")
public class StudentLearningTraceServiceImpl extends BaseServiceImpl<StudentLearningTrace> implements IStudentLearningTraceService {

	@Override
	@Transactional(readOnly=true)
	public StudentLearningTrace getStudentLearningTrace(String syllabusId, String studentId) throws ServiceException {
		List<StudentLearningTrace> list = findByCriteria(Restrictions.eq("isDeleted", 0),Restrictions.eq("studentInfo.resourceid", studentId),Restrictions.eq("syllabus.resourceid", syllabusId));
		return ExCollectionUtils.isNotEmpty(list) ? list.get(0) : null;
	}

}
