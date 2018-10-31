package com.hnjk.edu.arrange.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.roll.model.Classes;
import com.hnjk.edu.teaching.model.TeachingPlanCourse;
import com.hnjk.security.model.User;

/**
 * 排课班级关系
 */
@Entity
@Table(name="edu_arrange_teachclasses")
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TeachCourseClasses extends SuperBaseModel {

	private static final long serialVersionUID = 1L;

	@ManyToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "teachCourseid",nullable=false)	
	TeachCourse teachCourse;//	排课教学	
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE, CascadeType.PERSIST }, fetch = FetchType.LAZY)   
	@JoinColumn(name = "classesid") 
	Classes classes;//	班级
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE, CascadeType.PERSIST }, fetch = FetchType.LAZY)   
	@JoinColumn(name = "courseid") 
	Course course;//	基础课程
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE, CascadeType.PERSIST }, fetch = FetchType.LAZY)   
	@JoinColumn(name = "plancourseid") 
	TeachingPlanCourse planCourse;//	教学计划课程
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE, CascadeType.PERSIST }, fetch = FetchType.LAZY)   
	@JoinColumn(name = "recordScorerid") 
	User recordScorer;//	登分老师
	
	@Column(name="recordScorerName")
	String recordScorerName;//	登分老师名	
	
	@Column(name="arrangeStatus")
	Integer arrangeStatus;//	排课状态	字典表，0-未排课,1-已排课
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="teachEndDate")
	Date teachEndDate;//	上课结束日期	
	
	@Column(name="teachEndWeek")
	Integer teachEndWeek;//	上课结束周	

	public TeachCourse getTeachCourse() {
		return teachCourse;
	}

	public void setTeachCourse(TeachCourse teachCourse) {
		this.teachCourse = teachCourse;
	}

	public Classes getClasses() {
		return classes;
	}

	public void setClasses(Classes classes) {
		this.classes = classes;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public TeachingPlanCourse getPlanCourse() {
		return planCourse;
	}

	public void setPlanCourse(TeachingPlanCourse planCourse) {
		this.planCourse = planCourse;
	}

	public User getRecordScorer() {
		return recordScorer;
	}

	public void setRecordScorer(User recordScorer) {
		this.recordScorer = recordScorer;
	}

	public String getRecordScorerName() {
		return recordScorerName;
	}

	public void setRecordScorerName(String recordScorerName) {
		this.recordScorerName = recordScorerName;
	}

	public Integer getArrangeStatus() {
		return arrangeStatus;
	}

	public void setArrangeStatus(Integer arrangeStatus) {
		this.arrangeStatus = arrangeStatus;
	}

	public Date getTeachEndDate() {
		return teachEndDate;
	}

	public void setTeachEndDate(Date teachEndDate) {
		this.teachEndDate = teachEndDate;
	}

	public Integer getTeachEndWeek() {
		return teachEndWeek;
	}

	public void setTeachEndWeek(Integer teachEndWeek) {
		this.teachEndWeek = teachEndWeek;
	}
}
