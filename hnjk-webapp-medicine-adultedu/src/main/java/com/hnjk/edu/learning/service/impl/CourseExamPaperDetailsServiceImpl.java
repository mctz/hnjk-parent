package com.hnjk.edu.learning.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.learning.model.CourseExamPaperDetails;
import com.hnjk.edu.learning.service.ICourseExamPaperDetailsService;
/**
 * 试卷内容管理服务接口实现.
 * <code>CourseExamPaperDetailsServiceImpl</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2011-4-14 上午11:29:01
 * @see 
 * @version 1.0
 */
@Transactional
@Service("courseExamPaperDetailsService")
public class CourseExamPaperDetailsServiceImpl extends BaseServiceImpl<CourseExamPaperDetails> implements ICourseExamPaperDetailsService {

}
