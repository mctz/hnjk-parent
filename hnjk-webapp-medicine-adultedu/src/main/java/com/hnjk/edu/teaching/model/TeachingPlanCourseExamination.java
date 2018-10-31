package com.hnjk.edu.teaching.model;

import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.roll.model.Classes;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 教学计划课程排考表
 * @author msl
 */
@javax.persistence.Entity
@javax.persistence.Table(name="EDU_TEACH_COURSEEXAMINATION")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class TeachingPlanCourseExamination extends BaseModel implements Serializable{

	/**
	 * 所属班级
	 */
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,	CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "CLASSESID")
	private Classes classes;

	/**
	 * 教学计划课程
	 */
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,	CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "PLANCOURSEID")
	private TeachingPlanCourse teachingPlanCourse;

	/**
	 * 关联课程，冗余一个，用于查询统计等
	 */
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,	CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "COURSEID")
	private Course course;

	/**
	 * 排考人
	 */
	@Column(name="OPERATORNAME")
	private String operatorName;

	/**
	 * 开始日期
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "STARTEXAMDATE")
	private Date startExamDate;

	/**
	 * 结束日期
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "ENDEXAMDATE")
	private Date endExamDate;

	/**
	 * 当天时间段开始时间
	 */
	@Column(name = "STARTTIMEPERIOD")
	@Temporal(TemporalType.TIMESTAMP)
	private Date startTimePeriod;

	/**
	 * 当天时间段截止时间
	 */
	@Column(name = "ENDTIMEPERIOD")
	@Temporal(TemporalType.TIMESTAMP)
	private Date endTimePeriod;

	/**
	 * 人数
	 */
	@Column(name = "STUDENTNUM")
	private String studentNum;

	/**
	 * 地点
	 */
	@Column(name = "LOCATION")
	private String location;

	/**
	 * 课室
	 */
	@Column(name = "CLASSROOM")
	private String classroom;

	/**
	 * 监考教师编号
	 */
	@Column(name = "TEACHERCODE")
	private  String teacherCode;

	/**
	 * 监考教师
	 */
	@Column(name = "TEACHER")
	private String teacher;

	/**
	 * 备注
	 */
	@Column(name="MEMO")
	private String memo;

	//@Transient
	//private String classesid;
	@Transient
	private String plancourseid;

}
