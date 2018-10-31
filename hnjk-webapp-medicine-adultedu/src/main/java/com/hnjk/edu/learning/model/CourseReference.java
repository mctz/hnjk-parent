package com.hnjk.edu.learning.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.teaching.model.Syllabus;

/**
 * 课程-参考资料.
 * <code>CourseReference</code><p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-11-18 下午06:28:42
 * @see 
 * @version 1.0
 */
@Entity
@Table(name="EDU_LEAR_REFERENCE")  
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class CourseReference extends BaseModel{
	
	@Column(name="REFERENCENAME",length=255,nullable=false)
	private String referenceName;//参考资料名称
	
	@Column(name="URL",length=255,nullable=false)
	private String url;//参考资料
	
	@Column(name="REFERENCETYPE")
	private String referenceType;//资料类型 取自字典CodeReferenceType，字典配置 reference_doc -  参考文献 / reference_web - 参考网站
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "COURSEID")
	private Course course;//所属课程
	
//	@Column(name="SYLLABUSTREEID")
//	private String syllabusTreeId;//知识结构树，备用，不用显示给用户
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "SYLLABUSTREEID")
	private Syllabus syllabus;//所属课程教学大纲知识节点

}
