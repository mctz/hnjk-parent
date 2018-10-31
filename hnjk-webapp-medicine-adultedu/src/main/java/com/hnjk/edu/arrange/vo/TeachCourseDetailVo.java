package com.hnjk.edu.arrange.vo;

import java.util.Date;
import java.util.List;

import com.hnjk.edu.arrange.model.TeachCourse;
import com.hnjk.edu.basedata.model.Edumanager;
import com.hnjk.security.model.User;

public class TeachCourseDetailVo {
	private TeachCourse teachCourse;//教学班
	private String classTitleName;//班级名称
	private String className;//班级名称
	private List<String> weeks;//周数
	private List<String> timePeriod;//时间段
	private List<String> classroom;//上课地点
	private Edumanager teacher;//老师
	private String teacherName;//老师名称
	private User arrangement;//排课人
	private String arrangementName;//排课人
	public TeachCourse getTeachCourse() {
		return teachCourse;
	}
	public void setTeachCourse(TeachCourse teachCourse) {
		this.teachCourse = teachCourse;
	}
	public String getClassTitleName() {
		return classTitleName;
	}
	public void setClassTitleName(String classTitleName) {
		this.classTitleName = classTitleName;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public List<String> getTimePeriod() {
		return timePeriod;
	}
	public void setTimePeriod(List<String> timePeriod) {
		this.timePeriod = timePeriod;
	}
	public List<String> getWeeks() {
		return weeks;
	}
	public void setWeeks(List<String> weeks) {
		this.weeks = weeks;
	}
	public Edumanager getTeacher() {
		return teacher;
	}
	public void setTeacher(Edumanager teacher) {
		this.teacher = teacher;
	}
	public String getTeacherName() {
		return teacherName;
	}
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}
	public List<String> getClassroom() {
		return classroom;
	}
	public void setClassroom(List<String> classroom) {
		this.classroom = classroom;
	}
	public User getArrangement() {
		return arrangement;
	}
	public void setArrangement(User arrangement) {
		this.arrangement = arrangement;
	}
	public String getArrangementName() {
		return arrangementName;
	}
	public void setArrangementName(String arrangementName) {
		this.arrangementName = arrangementName;
	}
}
