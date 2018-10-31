package com.hnjk.edu.arrange.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Where;

import com.hnjk.edu.basedata.model.Classroom;
import com.hnjk.edu.teaching.model.TeachingPlanCourseTimePeriod;
import com.hnjk.security.model.User;

/**
 * 排课详情细分
 */
@Entity
@Table(name="edu_arrange_detailSubdivision")
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TeachCourseDetailSubdivision extends SuperBaseModel {
	@JoinColumn(name="detailid",nullable=false)
	@ManyToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	private TeachCourseDetail teachCourseDetail;//	排课详情
	
	@JoinColumn(name="teacherid")
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	private User teacher;//	主讲老师id	
	
	@JoinColumn(name="classroomid")
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	private Classroom classroomid;//	课室id	
	
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "timePeriodid") 
	@org.hibernate.annotations.Cascade(value={org.hibernate.annotations.CascadeType.ALL,org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@Where(clause="isDeleted=0")
	private Set<TeachingPlanCourseTimePeriod> timePeriods = new HashSet<TeachingPlanCourseTimePeriod>(0);//	时间段	
	
	@Column(name="startDate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date startDate;//	具体开始时间	
	
	@Column(name="endDate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date endDate;//	具体结束时间	
	
	/**
	 * 周
	 */
	@Column(name="week")
	private Integer week;//	第几周	
	
	@Column(name="day")
	private Integer day;//	星期几	

	public TeachCourseDetail getTeachCourseDetail() {
		return teachCourseDetail;
	}

	public void setTeachCourseDetail(TeachCourseDetail teachCourseDetail) {
		this.teachCourseDetail = teachCourseDetail;
	}

	public User getTeacher() {
		return teacher;
	}

	public void setTeacher(User teacher) {
		this.teacher = teacher;
	}

	public Classroom getClassroomid() {
		return classroomid;
	}

	public void setClassroomid(Classroom classroomid) {
		this.classroomid = classroomid;
	}

	public Set<TeachingPlanCourseTimePeriod> getTimePeriods() {
		return timePeriods;
	}

	public void setTimePeriods(Set<TeachingPlanCourseTimePeriod> timePeriods) {
		this.timePeriods = timePeriods;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Integer getWeek() {
		return week;
	}

	public void setWeek(Integer week) {
		this.week = week;
	}

	public Integer getDay() {
		return day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}
}
