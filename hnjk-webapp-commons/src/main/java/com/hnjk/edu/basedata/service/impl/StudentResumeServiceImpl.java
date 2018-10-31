package com.hnjk.edu.basedata.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.basedata.model.StudentResume;
import com.hnjk.edu.basedata.service.IStudentResumeService;


/**
 * 学生简历服务接口实现
 * <code>StudentResumeServiceImpl</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2010-5-5 下午07:38:59
 * @see 
 * @version 1.0
*/
@Service("studentResumeService")
@Transactional
public class StudentResumeServiceImpl extends BaseServiceImpl<StudentResume>
                  implements IStudentResumeService{

}
