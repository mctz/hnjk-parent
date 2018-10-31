package com.hnjk.edu.learning.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.edu.basedata.model.Course;

/**
 * 课程模拟试题表.
 * <code>CourseMockTest</code><p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-11-9 下午04:29:25
 * @see 
 * @version 1.0
 */
@Entity
@Table(name="EDU_LEAR_MOCKTEST")  
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class CourseMockTest extends BaseModel{
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "COURSEID")
	private Course course;//所属课程
	
	
	@Column(name="MOCKTESTNAME",length=255,nullable=false)
	private String mocktestName;//模拟试题名称
	
	@Column(name="SYLLABUSTREEID")
	private String syllabusTreeId;//知识结构树，备用，不用显示给用户
	
	@Column(name="MATEURL",length=255,nullable=false)
	private String mateUrl;//资源路径

	@Column(name="NETMEATEURL")
	private String netMeateUrl;//教育网地址
	
	@Transient
	private String realUrl;

	
}
