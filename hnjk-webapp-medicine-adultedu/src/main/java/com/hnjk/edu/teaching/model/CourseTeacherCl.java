package com.hnjk.edu.teaching.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.roll.model.Classes;

/**
 * 课程-班级-教师  关联表
 *
 */
@Entity
@Table(name = "EDU_TEACH_COURSETEACHERCL")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CourseTeacherCl extends BaseModel {

	private static final long serialVersionUID = 5912848213073822650L;

	@OneToOne(optional = true, cascade = { CascadeType.MERGE,	CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "COURSEID")
	private Course courseid; //课程id
	
	@Column(name="TEACHERID")
	private String teachid; //登分教师id
	
	@Column(name="TEACHERNAME")
	private String teacherName; //登分教师名称
	
	@Column(name="LECTURERID")
	private String lecturerid; //任课教师id
	
	@Column(name="LECTURERNAME")
	private String lecturerName; //任课教师名称
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,	CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "CLASSESID")
	private Classes classesId;//班级id
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,	CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "COURSESTATUSID")
	private TeachingPlanCourseStatus courseStatusId;//年级教学计划课程开课表id

	public Course getCourseid() {
		return courseid;
	}

	public void setCourseid(Course courseid) {
		this.courseid = courseid;
	}

	public String getTeachid() {
		return teachid;
	}

	public void setTeachid(String teachid) {
		this.teachid = teachid;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public Classes getClassesId() {
		return classesId;
	}

	public void setClassesId(Classes classesId) {
		this.classesId = classesId;
	}

	public TeachingPlanCourseStatus getCourseStatusId() {
		return courseStatusId;
	}

	public void setCourseStatusId(TeachingPlanCourseStatus courseStatusId) {
		this.courseStatusId = courseStatusId;
	}
	
	public void setLecturerId(String lecturer) {
		this.lecturerid = lecturer;
	}

	public String getLecturerName() {
		return lecturerName;
	}

	public void setLecturerName(String lecturerName) {
		this.lecturerName = lecturerName;
	}

	public String getLecturer() {
		return lecturerid;
	}
	
	
	
}

