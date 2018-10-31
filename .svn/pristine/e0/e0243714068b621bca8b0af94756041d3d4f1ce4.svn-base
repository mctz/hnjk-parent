package com.hnjk.edu.teaching.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.edu.basedata.model.Classroom;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.basedata.model.Edumanager;
import com.hnjk.edu.basedata.model.Grade;
import com.hnjk.edu.basedata.model.Major;
import com.hnjk.edu.roll.model.Classes;
import com.hnjk.security.model.OrgUnit;
import com.hnjk.security.model.User;

/**教学记录
 * <code>TeachingRecords</code>
 * @author Administrator
 * @version 1.0
 */
@Entity
@Table(name="EDU_TEACH_TeachingRecords")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TeachingRecords extends BaseModel {

	private static final long serialVersionUID = 1L;
	
	@OneToOne(optional = false, cascade = { CascadeType.MERGE, CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "BRANCHSCHOOLID")
	private OrgUnit unit;//教学点
	
	@OneToOne(optional = false, cascade = { CascadeType.MERGE, CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "GRADEID")
	private Grade grade;//上课年级
	
	@OneToOne(optional = false, cascade = { CascadeType.MERGE, CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "MAJORID")
	private Major major;//专业
	
	@OneToOne(optional = false, cascade = { CascadeType.MERGE, CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "planCourseid")
	private TeachingPlanCourse planCourse;//教学计划课程
	
	@Column(name="teachType")
	private String teachType;//教学手段 
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE, CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "userid")
	private Edumanager teacher;//教师
	
	@Column(name="classroom")
	private String classroom;//地点
	
	@Column(name="contents")
	private String contents;//教学内容
	
	@Column(name="TERM")
	private String term;//学期
	
	@Column(name="week")
	private int week;//周次
	
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name="timeperiod")
	private Date timeperiod;//日期（细致到月份）
	
	public OrgUnit getUnit() {
		return unit;
	}
	public void setUnit(OrgUnit unit) {
		this.unit = unit;
	}
	public Grade getGrade() {
		return grade;
	}
	public void setGrade(Grade grade) {
		this.grade = grade;
	}
	public TeachingPlanCourse getPlanCourse() {
		return planCourse;
	}
	public void setPlanCourse(TeachingPlanCourse planCourse) {
		this.planCourse = planCourse;
	}
	public User getTeacher() {
		return teacher;
	}
	public void setTeacher(Edumanager teacher) {
		this.teacher = teacher;
	}
	public Major getMajor() {
		return major;
	}
	public void setMajor(Major major) {
		this.major = major;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	public String getTerm() {
		return term;
	}
	public void setTerm(String term) {
		this.term = term;
	}
	public int getWeek() {
		return week;
	}
	public void setWeek(int week) {
		this.week = week;
	}
	public String getTeachType() {
		return teachType;
	}
	public void setTeachType(String teachType) {
		this.teachType = teachType;
	}
	public Date getTimeperiod() {
		return timeperiod;
	}
	public void setTimeperiod(Date timeperiod) {
		this.timeperiod = timeperiod;
	}
	public String getClassroom() {
		return classroom;
	}
	public void setClassroom(String classroom) {
		this.classroom = classroom;
	}
}
