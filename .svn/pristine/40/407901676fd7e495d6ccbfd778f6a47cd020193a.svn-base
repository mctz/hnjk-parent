package com.hnjk.edu.learning.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.learning.model.StudentSyllabus;
import com.hnjk.edu.learning.service.IStudentSyllabusService;


@Transactional
@Service("studentSyllabusService")
public class StudentSyllabusServiceImpl extends BaseServiceImpl<StudentSyllabus> implements IStudentSyllabusService{

	@Override
	public List<StudentSyllabus> findListByCondition(Map<String,Object> condition) throws ServiceException{
		String hql1 = "from "+StudentSyllabus.class.getSimpleName()+" where isDeleted = 0 and stuInfo.resourceid =:studentInfoId and syllabus.resourceid=:syllabusId ";
		List<StudentSyllabus> ssList = findByHql(hql1,condition);
		return ssList;
	}
}
