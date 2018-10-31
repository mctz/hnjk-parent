package com.hnjk.edu.teaching.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.teaching.model.GraduateMentorDetails;
import com.hnjk.edu.teaching.service.IGraduateMentorDetailsService;

/**
 * 毕业指导老师分配的学生明细表.
 * <code>GraduateMentorDetails</code><p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-10-19 下午04:41:50
 * @see 
 * @version 1.0
 */
@Transactional
@Service("graduatementordetailsservice")
public class GraduateMentorDetailsServiceImpl extends BaseServiceImpl<GraduateMentorDetails> implements
		IGraduateMentorDetailsService {
	
}
