package com.hnjk.edu.roll.service;

import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.roll.model.StudentGraduateAndDegreeAudit;
/**
 * 学生毕业、学位审核service.
 * @author hzg
 *
 */
public interface IStudentGraduateAndDegreeAuditService extends IBaseService<StudentGraduateAndDegreeAudit>{
	public Page findGraduateAuditByCondition(Map<String, Object> condition,	Page objPage) throws ServiceException;
	public Page findDegreeAuditByCondition(Map<String, Object> condition,	Page objPage) throws ServiceException;
}
