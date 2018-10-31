package com.hnjk.edu.arrange.model;

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.OrderBy;
import org.hibernate.annotations.Where;

import com.hnjk.core.annotation.DeleteChild;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.edu.basedata.model.Classroom;
import com.hnjk.edu.teaching.model.TeachingPlanCourseTimePeriod;
import com.hnjk.security.model.Role;
import com.hnjk.security.model.User;

/**
 * 排课详情
 */
@Entity
@Table(name="EDU_ARRANGE_DETAIL")
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TeachCourseDetail extends SuperBaseModel {
	
	private static final long serialVersionUID = 1L;

	@ManyToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "teachCourseid",nullable=false)
	private TeachCourse teachCourse;//	排课教学id	
	
	@Column(name="studyHour")
	private Integer studyHour;//	学时	
	
	@JoinColumn(name="classroomid")
	@OneToOne(optional = false, cascade = { CascadeType.MERGE, CascadeType.PERSIST }, fetch = FetchType.LAZY)
	private Classroom classroom;//	课室id	
	
	@Column(name="dateType")
	private Integer dateType;//	排课日期类型	周/日期，0-周，1-日期
	
	@Column(name="detailtype")
	private Integer detailtype;//	类型	0排课（默认）， 1排考
	
	@JoinColumn(name="teacherid")
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	private User teacher;//	主讲老师id	
	
	@Column(name="timePeriodids")
	private String timePeriodids;//	时间段id
	
	@Column(name="timeperiodnames")
	private String timePeriodNames;//	时间段名字	
	
	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "edu_arrange_detailTimePeriod", joinColumns = { @JoinColumn(name = "detailId") }, inverseJoinColumns = { @JoinColumn(name = "timePeriodId") })
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@Where(clause="isDeleted=0")
	@DeleteChild(deleteable=false)
	private Set<TeachingPlanCourseTimePeriod> timePeriods = new HashSet<TeachingPlanCourseTimePeriod>(0);//	时间段
	
	@Column(name="days")
	private String days;//	星期几	
	
	@Column(name="weeks")
	private String weeks;//	周数	
	
	@Column(name="startdate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date startdate;//	开始时间	
	
	@Column(name="enddate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date enddate;//	结束时间	
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "teachCourseDetail")
	@org.hibernate.annotations.Cascade(value={org.hibernate.annotations.CascadeType.ALL,org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@Where(clause="isDeleted=0")
	private Set<TeachCourseDetailSubdivision> detailSubdivisions = new HashSet<TeachCourseDetailSubdivision>(0);

	public TeachCourse getTeachCourse() {
		return teachCourse;
	}

	public void setTeachCourse(TeachCourse teachCourse) {
		this.teachCourse = teachCourse;
	}

	public String getTimePeriodids() {
		return timePeriodids;
	}

	public void setTimePeriodids(String timePeriodids) {
		this.timePeriodids = timePeriodids;
	}

	public Integer getStudyHour() {
		return studyHour;
	}

	public void setStudyHour(Integer studyHour) {
		this.studyHour = studyHour;
	}

	public Classroom getClassroom() {
		return classroom;
	}

	public void setClassroom(Classroom classroom) {
		this.classroom = classroom;
	}

	public Integer getDateType() {
		return dateType;
	}

	public void setDateType(Integer dateType) {
		this.dateType = dateType;
	}

	public Integer getDetailtype() {
		return detailtype;
	}

	public void setDetailtype(Integer detailtype) {
		this.detailtype = detailtype;
	}

	public User getTeacher() {
		return teacher;
	}

	public void setTeacher(User teacher) {
		this.teacher = teacher;
	}

	public String getTimePeriodNames() {
		return timePeriodNames;
	}

	public void setTimePeriodNames(String timeperiodnames) {
		this.timePeriodNames = timeperiodnames;
	}

	public String getDays() {
		return days;
	}

	public String getDaysName() {
		return ExStringUtils.getDaysName(days);
	}
	
	public void setDays(String days) {
		this.days = days;
	}

	public String getWeeks() {
		return weeks;
	}

	public String getWeeksName() {
		return ExStringUtils.getWeeksName(weeks);
	}
	public void setWeeks(String weeks) {
		this.weeks = weeks;
	}

	public Date getStartdate() {
		return startdate;
	}

	public void setStartdate(Date startdate) {
		this.startdate = startdate;
	}

	public Date getEnddate() {
		return enddate;
	}

	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}

	public Set<TeachCourseDetailSubdivision> getDetailSubdivisions() {
		return detailSubdivisions;
	}

	public void setDetailSubdivisions(
			Set<TeachCourseDetailSubdivision> detailSubdivisions) {
		this.detailSubdivisions = detailSubdivisions;
	}

	public Set<TeachingPlanCourseTimePeriod> getTimePeriods() {
		return timePeriods;
	}

	public void setTimePeriods(Set<TeachingPlanCourseTimePeriod> timePeriods) {
		this.timePeriods = timePeriods;
	}
}
