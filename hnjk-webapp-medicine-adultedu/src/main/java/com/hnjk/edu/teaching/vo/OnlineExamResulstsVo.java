package com.hnjk.edu.teaching.vo;

import com.hnjk.edu.teaching.util.ScoreEncryptionDecryptionUtil;

public class OnlineExamResulstsVo {
	private String resourceid;//成绩id
	private String studentId;//学生id
	private String studyNo;//学号
	private String studentName;//学生姓名	
	private String examSubId;//考试批次id
	private String batchName;//考试批次名称
	private String courseId;//课程id
	private String courseName;//课程名称
	private String writtenScore;//卷面成绩
	private String examAbnormity;//成绩异常
	private String writtenMachineScore;//机考客观题成绩
	private String writtenOnlineHandworkScore; //机考的在线作答成绩
	private String checkStatus;//成绩状态
	
	public String getWrittenOnlineHandworkScore() {
		return ScoreEncryptionDecryptionUtil.getInstance().decrypt(this.studentId,this.writtenOnlineHandworkScore);
	}
	public void setWrittenOnlineHandworkScore(String writtenOnlineHandworkScore) {
		this.writtenOnlineHandworkScore = writtenOnlineHandworkScore;
	}
	public String getWrittenMachineScore() {
		return ScoreEncryptionDecryptionUtil.getInstance().decrypt(this.studentId,this.writtenMachineScore);
	}
	public void setWrittenMachineScore(String writtenMachineScore) {
		this.writtenMachineScore = writtenMachineScore;
	}
	public String getResourceid() {
		return resourceid;
	}
	public void setResourceid(String resourceid) {
		this.resourceid = resourceid;
	}
	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	public String getStudyNo() {
		return studyNo;
	}
	public void setStudyNo(String studyNo) {
		this.studyNo = studyNo;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public String getWrittenScore() {
		return ScoreEncryptionDecryptionUtil.getInstance().decrypt(this.studentId,this.writtenScore);
	}
	public void setWrittenScore(String writtenScore) {
		this.writtenScore = writtenScore;
	}
	public String getExamAbnormity() {
		return examAbnormity;
	}
	public void setExamAbnormity(String examAbnormity) {
		this.examAbnormity = examAbnormity;
	}
	public String getExamSubId() {
		return examSubId;
	}
	public void setExamSubId(String examSubId) {
		this.examSubId = examSubId;
	}
	public String getBatchName() {
		return batchName;
	}
	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}
	public String getCourseId() {
		return courseId;
	}
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public String getCheckStatus() {
		return checkStatus;
	}
	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}
}
