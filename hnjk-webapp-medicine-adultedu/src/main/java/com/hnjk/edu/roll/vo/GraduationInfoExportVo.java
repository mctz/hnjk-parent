package com.hnjk.edu.roll.vo;

public class GraduationInfoExportVo {
	private String studyno;
	private String studentName;
	private String graduateauditstatus;
	private String graduateauditmemo;
	private String thegraduationstatis;
	private String thegraduationmemo;
	private String confirm;
	private String eduYear;
	private String studentStatus;
	private String finishedcredithour;
	private String finishednecesscredithour;
	private String thegraduationscore;
	private String gradename;
	private String classicname;
	private String majorname;
	private String unitname;
	private String courseCount;
	private String courselist;
	public String getStudyno() {
		return studyno;
	}
	public void setStudyno(String studyno) {
		this.studyno = studyno;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public String getGraduateauditstatus() {
		return graduateauditstatus;
	}
	public void setGraduateauditstatus(String graduateauditstatus) {
		this.graduateauditstatus = graduateauditstatus;
	}
	public String getGraduateauditmemo() {
		return graduateauditmemo==null?"":graduateauditmemo.toString().replaceAll("<font color='blue'>", "").replaceAll("</font>", "").replaceAll("<font color='red'>", "").split("\\|")[0];
	}
	public void setGraduateauditmemo(String graduateauditmemo) {
		this.graduateauditmemo = graduateauditmemo;
	}
	public String getThegraduationstatis() {
		return thegraduationstatis;
	}
	public void setThegraduationstatis(String thegraduationstatis) {
		this.thegraduationstatis = thegraduationstatis;
	}
	public String getThegraduationmemo() {
		
		return thegraduationmemo==null?"":thegraduationmemo.toString().replaceAll("<font color='blue'>", "").replaceAll("</font>", "").replaceAll("<font color='red'>", "").split("\\|")[0];
	}
	public void setThegraduationmemo(String thegraduationmemo) {
		this.thegraduationmemo = thegraduationmemo;
	}
	public String getConfirm() {
		return confirm;
	}
	public void setConfirm(String confirm) {
		this.confirm = confirm;
	}
	public String getEduYear() {
		return eduYear;
	}
	public void setEduYear(String eduYear) {
		this.eduYear = eduYear;
	}
	public String getStudentStatus() {
		return studentStatus;
	}
	public void setStudentStatus(String studentStatus) {
		this.studentStatus = studentStatus;
	}
	public String getFinishedcredithour() {
		return finishedcredithour;
	}
	public void setFinishedcredithour(String finishedcredithour) {
		this.finishedcredithour = finishedcredithour;
	}
	public String getFinishednecesscredithour() {
		return finishednecesscredithour;
	}
	public void setFinishednecesscredithour(String finishednecesscredithour) {
		this.finishednecesscredithour = finishednecesscredithour;
	}
	public String getThegraduationscore() {
		return thegraduationscore;
	}
	public void setThegraduationscore(String thegraduationscore) {
		this.thegraduationscore = thegraduationscore;
	}
	public String getGradename() {
		return gradename;
	}
	public void setGradename(String gradename) {
		this.gradename = gradename;
	}
	public String getClassicname() {
		return classicname;
	}
	public void setClassicname(String classicname) {
		this.classicname = classicname;
	}
	public String getMajorname() {
		return majorname;
	}
	public void setMajorname(String majorname) {
		this.majorname = majorname;
	}
	public String getUnitname() {
		return unitname;
	}
	public void setUnitname(String unitname) {
		this.unitname = unitname;
	}
	public String getCourseCount() {
		return courseCount;
	}
	public void setCourseCount(String courseCount) {
		this.courseCount = courseCount;
	}
	public String getCourselist() {
		return courselist;
	}
	public void setCourselist(String courselist) {
		this.courselist = courselist;
	}
	
	
}
