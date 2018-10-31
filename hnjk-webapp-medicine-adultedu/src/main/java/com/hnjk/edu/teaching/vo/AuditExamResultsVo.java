package com.hnjk.edu.teaching.vo;

import com.hnjk.edu.teaching.util.ScoreEncryptionDecryptionUtil;

/**
 * 更正成绩vo
 * <code>AuditExamResultsVo</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2012-1-12 下午05:42:50
 * @see 
 * @version 1.0
 */
public class AuditExamResultsVo {
	private String resourceid;//成绩id
	private String studentId;//学生id
	private String studyNo;//学号
	private String studentName;//学生姓名
	private String writtenScore;//卷面成绩
	private String usuallyScore;//平时成绩
	private String integratedScore;//综合成绩
	private String examAbnormity;//成绩异常
	private String changedWrittenScore;//变更后卷面成绩
	private String changedUsuallyScore;//变更后平时成绩
	private String changedIntegratedScore;//变更后综合成绩
	private String changedExamAbnormity;//变更后成绩异常
	private String examSubId;//考试批次id
	private String batchName;//考试批次名称
	private String courseId;//课程id
	private String courseName;//课程名称
	private String teachType;//教学方式
	private String isMixTrue;//是否混合机考成绩
	private String writtenHandworkScore; //混合机考的笔考成绩	
	private Double firstScore;//毕业论文初评成绩
	private Double secondScore;//毕业论文答辩成绩
	private Double changedFirstScore;//变更后毕业论文初评成绩
	private Double changedSecondScore;//变更后毕业论文答辩成绩
	private String changedThesisScore;//变更后毕业论文终评成绩
	private String resultAuditId;//成绩审核id
	private String paperOrderId;//毕业论文预约id
	private String memo;
	private String classesname;
	private String gradeName;
	private String majorName;
	private String classicName;
	private String courseTeachType;//课程类型:CodeCourseTeachType
	private String examClassType;
	
	public String getClassesname() {
		return classesname;
	}
	public void setClassesname(String classesname) {
		this.classesname = classesname;
	}
	public String getIsMixTrue() {
		return isMixTrue;
	}
	public void setIsMixTrue(String isMixTrue) {
		this.isMixTrue = isMixTrue;
	}
	
	public String getWrittenHandworkScore() {
		return ScoreEncryptionDecryptionUtil.getInstance().decrypt(this.studentId,this.writtenHandworkScore);
	}
	public void setWrittenHandworkScore(String writtenHandworkScore) {
		this.writtenHandworkScore = writtenHandworkScore;
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
	public String getUsuallyScore() {
		return ScoreEncryptionDecryptionUtil.getInstance().decrypt(this.studentId,this.usuallyScore);
	}
	public void setUsuallyScore(String usuallyScore) {
		this.usuallyScore = usuallyScore;
	}
	public String getIntegratedScore() {
		return ScoreEncryptionDecryptionUtil.getInstance().decrypt(this.studentId,this.integratedScore);
	}
	public void setIntegratedScore(String integratedScore) {
		this.integratedScore = integratedScore;
	}
	public String getExamAbnormity() {
		return examAbnormity;
	}
	public void setExamAbnormity(String examAbnormity) {
		this.examAbnormity = examAbnormity;
	}
	public String getChangedWrittenScore() {
		return ScoreEncryptionDecryptionUtil.getInstance().decrypt(this.studentId,this.changedWrittenScore);
	}
	public void setChangedWrittenScore(String changedWrittenScore) {
		this.changedWrittenScore = changedWrittenScore;
	}
	public String getChangedUsuallyScore() {
		return ScoreEncryptionDecryptionUtil.getInstance().decrypt(this.studentId,this.changedUsuallyScore);
	}
	public void setChangedUsuallyScore(String changedUsuallyScore) {
		this.changedUsuallyScore = changedUsuallyScore;
	}
	public String getChangedIntegratedScore() {
		return ScoreEncryptionDecryptionUtil.getInstance().decrypt(this.studentId,this.changedIntegratedScore);
	}
	public void setChangedIntegratedScore(String changedIntegratedScore) {
		this.changedIntegratedScore = changedIntegratedScore;
	}
	public String getChangedExamAbnormity() {
		return changedExamAbnormity;
	}
	public void setChangedExamAbnormity(String changedExamAbnormity) {
		this.changedExamAbnormity = changedExamAbnormity;
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
	public String getTeachType() {
		return teachType;
	}
	public void setTeachType(String teachType) {
		this.teachType = teachType;
	}
	public String getResultAuditId() {
		return resultAuditId;
	}
	public void setResultAuditId(String resultAuditId) {
		this.resultAuditId = resultAuditId;
	}
	public String getPaperOrderId() {
		return paperOrderId;
	}
	public void setPaperOrderId(String paperOrderId) {
		this.paperOrderId = paperOrderId;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public Double getFirstScore() {
		return firstScore;
	}
	public void setFirstScore(Double firstScore) {
		this.firstScore = firstScore;
	}
	public Double getSecondScore() {
		return secondScore;
	}
	public void setSecondScore(Double secondScore) {
		this.secondScore = secondScore;
	}
	public Double getChangedFirstScore() {
		return changedFirstScore;
	}
	public void setChangedFirstScore(Double changedFirstScore) {
		this.changedFirstScore = changedFirstScore;
	}
	public Double getChangedSecondScore() {
		return changedSecondScore;
	}
	public void setChangedSecondScore(Double changedSecondScore) {
		this.changedSecondScore = changedSecondScore;
	}
	public void setChangedThesisScore(String changedThesisScore) {
		this.changedThesisScore = changedThesisScore;
	}
	public String getChangedThesisScore() {
		return changedThesisScore;
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
	public String getCourseTeachType() {
		return courseTeachType;
	}
	public void setCourseTeachType(String courseTeachType) {
		this.courseTeachType = courseTeachType;
	}
	public String getExamClassType() {
		return examClassType;
	}
	public void setExamClassType(String examClassType) {
		this.examClassType = examClassType;
	}
}
