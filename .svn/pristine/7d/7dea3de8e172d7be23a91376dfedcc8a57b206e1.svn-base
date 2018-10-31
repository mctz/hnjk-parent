package com.hnjk.edu.learning.model;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.edu.basedata.model.Course;

/**
 * 课程概况表
 * <code>CourseOverview</code><p>
 * 用来描述某个课程的扩展介绍.
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-8-24 上午09:26:09
 * @see 
 * @version 1.0
 */
@Entity
@Table(name="EDU_LEAR_OVERVIEW")  
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class CourseOverview extends BaseModel{

	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "COURSEID")
	private Course course;//所属课程
	
	@Column(name="TYPE")
	private String type;//类型 取自字典CodeCourseOverviewType： 教师队伍/学习方法/自学周历/参考教材/考试大纲/拓展资料
	
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name="CONTENT")
	private String content;//内容
	
	@Column(name="FILLINMAN")
	private String fillinMan;//填写人
	
	@Column(name="FILLINMANID")
	private String fillinManId;//填写人ID
	
	@Column(name="FILLINDATE")
	private Date fillinDate = new Date();//填写日期
	
}
