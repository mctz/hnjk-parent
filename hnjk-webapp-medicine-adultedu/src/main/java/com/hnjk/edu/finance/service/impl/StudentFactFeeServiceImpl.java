package com.hnjk.edu.finance.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.finance.service.IStudentFactFeeService;
import com.hnjk.edu.roll.model.StudentFactFee;

/**
 * 学生在银校通的缴费信息.
 * <code>studentfactfeeservice</code><p>
 * 
 */
@Transactional
@Service("studentfactfeeservice")
public class StudentFactFeeServiceImpl extends BaseServiceImpl<StudentFactFee> implements IStudentFactFeeService{
	
}
