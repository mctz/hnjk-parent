package com.hnjk.edu.learning.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.learning.model.CourseExamRulesDetails;
import com.hnjk.edu.learning.service.ICourseExamRulesDetailsService;
/**
 * 成卷规则明细管理服务接口实现.
 * <code>CourseExamRulesDetailsService</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2011-7-26 下午04:49:19
 * @see 
 * @version 1.0
 */
@Transactional
@Service("courseExamRulesDetailsService")
public class CourseExamRulesDetailsServiceImpl extends BaseServiceImpl<CourseExamRulesDetails> implements ICourseExamRulesDetailsService {

}
