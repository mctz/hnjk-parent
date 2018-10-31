package com.hnjk.edu.teaching.vo;

import java.util.Date;

import com.hnjk.edu.teaching.util.ScoreEncryptionDecryptionUtil;

/**
 * 毕业论文成绩Vo
 * <code>ThesisExamResultsVo</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2012-6-13 下午04:44:08
 * @see 
 * @version 1.0
 */
public class ThesisExamResultsVo {	

	private String studentId;//学生id
	private String branchSchool;//教学中心
	private String gradeName;//年级
	private String majorName;//专业
	private String classicName;//层次
	private String stuStudyNo;//学号
	private String name;//姓名
	private String examSubId;//论文批次ID
	private String courseName;//课程名称
	private String firstScore;//初评成绩
	private String secondScore;//答辩成绩
	private String integratedScore;//终评成绩
	private String checkStatus;//审核状态
	private String courseId;//课程ID 
	private String examResultsId;//成绩ID
	private Integer examType;//考试形式
	private String majorCourseId;//教学计划课程id
	private Double creditHour;//学分
	private String courseType;//课程性质
	private String teachType;
	private Integer stydyHour;
	private String auditMan;//审核人
	private Date auditDate;//审核日期	
	
	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	public String getBranchSchool() {
		return branchSchool;
	}
	public void setBranchSchool(String branchSchool) {
		this.branchSchool = branchSchool;
	}
	public String getGradeName() {
		return gradeName;
	}
	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}
	public String getMajorName() {
		return majorName;
	}
	public void setMajorName(String majorName) {
		this.majorName = majorName;
	}
	public String getClassicName() {
		return classicName;
	}
	public void setClassicName(String classicName) {
		this.classicName = classicName;
	}
	public String getStuStudyNo() {
		return stuStudyNo;
	}
	public void setStuStudyNo(String stuStudyNo) {
		this.stuStudyNo = stuStudyNo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getExamSubId() {
		return examSubId;
	}
	public void setExamSubId(String examSubId) {
		this.examSubId = examSubId;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public String getFirstScore() {
		return firstScore;
	}
	public void setFirstScore(String firstScore) {
		this.firstScore = firstScore;
	}
	public String getSecondScore() {
		return secondScore;
	}
	public void setSecondScore(String secondScore) {
		this.secondScore = secondScore;
	}
	public String getIntegratedScore() {
		return ScoreEncryptionDecryptionUtil.getInstance().decrypt(studentId,integratedScore);
	}
	public void setIntegratedScore(String integratedScore) {
		this.integratedScore = integratedScore;
	}
	public String getCheckStatus() {
		return checkStatus;
	}
	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}
	public String getCourseId() {
		return courseId;
	}
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	public String getExamResultsId() {
		return examResultsId;
	}
	public void setExamResultsId(String examResultsId) {
		this.examResultsId = examResultsId;
	}
	public Integer getExamType() {
		return examType;
	}
	public void setExamType(Integer examType) {
		this.examType = examType;
	}
	public String getMajorCourseId() {
		return majorCourseId;
	}
	public void setMajorCourseId(String majorCourseId) {
		this.majorCourseId = majorCourseId;
	}
	public Double getCreditHour() {
		return creditHour;
	}
	public void setCreditHour(Double creditHour) {
		this.creditHour = creditHour;
	}
	public String getCourseType() {
		return courseType;
	}
	public void setCourseType(String courseType) {
		this.courseType = courseType;
	}
	public String getAuditMan() {
		return auditMan;
	}
	public void setAuditMan(String auditMan) {
		this.auditMan = auditMan;
	}
	public Date getAuditDate() {
		return auditDate;
	}
	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}
	public String getTeachType() {
		return teachType;
	}
	public void setTeachType(String teachType) {
		this.teachType = teachType;
	}
	public Integer getStydyHour() {
		return stydyHour;
	}
	public void setStydyHour(Integer stydyHour) {
		this.stydyHour = stydyHour;
	}

	
}
