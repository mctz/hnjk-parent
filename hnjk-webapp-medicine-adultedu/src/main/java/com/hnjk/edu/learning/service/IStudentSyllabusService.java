package com.hnjk.edu.learning.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.learning.model.StudentSyllabus;

public interface IStudentSyllabusService extends IBaseService<StudentSyllabus>{
	/**
	 * 查询学生每个章节的完成情况
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	public List<StudentSyllabus> findListByCondition(Map<String,Object> condition) throws ServiceException;
	
}
