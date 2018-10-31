package com.hnjk.edu.teaching.vo;

import java.text.ParseException;
import java.util.Date;

import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.roll.model.StudentInfo;
/**
 * 
 * <code>统考成绩Vo</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-11-15 下午05:27:03
 * @see 
 * @version 1.0
 */
public class StateExamResultsVo {
	
	private String examCertificateNo;//准考证号
	private String studyNo;		 //学号
	private String studentName; 			 //姓名
	private String gender;//性别
	private String gradeName;//年级
	private String classicName;//层次
	private String teachingType;//学习形式
	private String majorName;//专业
	private String className;//班级
	private String studentStatus;//学籍状态
	private String courseName;//课程名称
	private String unitName;		 //所属校外学习中心
	private Date passtime;		 //通过时间
	private Course course;           //统考课程
	private String courseResult;     //统考课程成绩
	private StudentInfo studentInfo; //学籍
	private String memo;
	private String scoreType;
	private String stateExamid; //resourceid
	
	public String getScoreType() {
		return scoreType;
	}
	public void setScoreType(String scoreType) {
		this.scoreType = scoreType;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getGradeName() {
		return gradeName;
	}
	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}
	public String getClassicName() {
		return classicName;
	}
	public void setClassicName(String classicName) {
		this.classicName = classicName;
	}
	public String getTeachingType() {
		return teachingType;
	}
	public void setTeachingType(String teachingType) {
		this.teachingType = teachingType;
	}
	public String getMajorName() {
		return majorName;
	}
	public void setMajorName(String majorName) {
		this.majorName = majorName;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getStudentStatus() {
		return studentStatus;
	}
	public void setStudentStatus(String studentStatus) {
		this.studentStatus = studentStatus;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public StudentInfo getStudentInfo() {
		return studentInfo;
	}
	public void setStudentInfo(StudentInfo studentInfo) {
		this.studentInfo = studentInfo;
	}
	

	
	
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	public String getStudyNo() {
		return studyNo;
	}
	public void setStudyNo(String studyNo) {
		this.studyNo = studyNo;
	}
	public Course getCourse() {
		return course;
	}
	public void setCourse(Course course) {
		this.course = course;
	}
	public String getCourseResult() {
		return courseResult;
	}
	public void setCourseResult(String courseResult) {
		this.courseResult = courseResult;
	}
	public String getExamCertificateNo() {
		return examCertificateNo;
	}
	public void setExamCertificateNo(String examCertificateNo) {
		this.examCertificateNo = examCertificateNo;
	}
	public Date getPasstime() throws ParseException {		
		return ExDateUtils.parseDate(passtime.toString(), ExDateUtils.PATTREN_DATE);
	}
	public void setPasstime(Date passtime) {
		this.passtime = passtime;
	}
	public String getStateExamid() {
		return stateExamid;
	}
	public void setStateExamid(String stateExamid) {
		this.stateExamid = stateExamid;
	}
	
	
}
