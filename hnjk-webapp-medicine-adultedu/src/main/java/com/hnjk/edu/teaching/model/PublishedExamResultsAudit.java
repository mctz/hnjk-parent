package com.hnjk.edu.teaching.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * 发布成绩审核记录
 * <code>PublishedExamResultsAudit</code><p>
 * 
 * @author：广东学苑教育发展有限公司.
 * @since： 2012-1-16 下午03:17:33
 * @see 
 * @version 1.0
 */
@Entity
@DiscriminatorValue("1")
public class PublishedExamResultsAudit extends ExamResultsAuditParent{
	
	private static final long serialVersionUID = 2685040267127998641L;
	
}
