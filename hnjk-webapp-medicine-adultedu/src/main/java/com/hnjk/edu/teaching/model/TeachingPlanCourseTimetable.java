package com.hnjk.edu.teaching.model;

import com.hnjk.core.annotation.Historizable;
import com.hnjk.core.rao.dao.listener.OperationType;
import com.hnjk.edu.basedata.model.Classroom;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.roll.model.Classes;
import com.hnjk.platform.system.model.BaseLogHistoryModel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 教学计划课程排课表
 * @author marco
 *
 */
@Entity
@Table(name = "EDU_TEACH_TIMETABLE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Historizable(logable=true,value={OperationType.UPDATE})
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class TeachingPlanCourseTimetable extends BaseLogHistoryModel implements Serializable{

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
	 * 上课老师
	 */
	@Column(name="TEACHERNAME")
	private String teacherName;

	/**
	 * 上课老师id
	 */
	@Column(name="TEACHERID")
	private String teacherId;

	/**
	 * 上课地址，已舍弃
	 */
	//@Column(name="ADDRESS")
	@Transient
	@Deprecated
	private String address;

	/**
	 * 时间段，已舍弃
	 */
	//@Column(name="TIMEPERIOD")
	@Transient
	@Deprecated
	private String timePeriod;

	/**
	 * 开始时间
	 */
	@Column(name="STARTTIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date startTime;

	/**
	 * 结束时间
	 */
	@Column(name="ENDTIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date endTime;

	/**
	 * 是否停课
	 */
	@Column(name="ISSTOPED",length=1)
	private String isStoped;

	/**
	 * 是否发布
	 */
	@Column(name="ISPUBLISHED",length=1)
	private String isPublished;

	/**
	 * 上课课室
	 */
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,	CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "CLASSROOMID")
	private Classroom classroom;

	/**
	 * 上课日期
	 */
	@Column(name="TEACHDATE")
	private String teachDate;

	/**
	 * 2012-04-13 新增学习中心时间段
	 */
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,	CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "TIMEPERIODID")
	private TeachingPlanCourseTimePeriod unitTimePeriod;

	/**
	 * 上课星期
	 */
	@Column(name="WEEK")
	private String week;

	/**
	 * 开课学期
	 */
	@Column(name="TERM")
	private String term;

	/**
	 * 临时调课备注
	 */
	@Column(name="MEMO")
	private String memo;

	/**
	 * 排课人
	 */
	@Column(name="OPERATORNAME")
	private String operatorName;

	/**
	 * 合班情况
	 */
	@Column(name="MERGEMEMO")
	private String mergeMemo;

	@Transient
	private String classesid;
	@Transient
	private String plancourseid;
	@Transient
	private String classroomid;	
	@Transient
	private String startTimeStr;	
	@Transient
	private String endTimeStr;	
	@Transient
	private String beforeContent;	
	@Transient
	private String afterContent;
	@Transient
	private String timePeriodid;
	@Transient 
	private Integer stuNumber;
	
}
