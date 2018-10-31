package com.hnjk.edu.teaching.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 *  <code>ExamResultsAudit</code><p>
 * 学生成绩变更审核记录表.(设置变更成绩字段之前一定要先设置成绩字段)
 * @author：广东学苑教育发展有限公司.
 * @since： 2011-12-22 上午10:22:58
 * @see 
 * @version 1.0
 */
@Entity
@DiscriminatorValue("0")
public class ExamResultsAudit extends ExamResultsAuditParent{

	private static final long serialVersionUID = -9131576901302165929L;
	
}
