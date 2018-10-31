package com.hnjk.edu.learning.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Where;

import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.basedata.model.YearInfo;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.teaching.model.ExamInfo;
import com.hnjk.edu.teaching.model.ExamResults;
import com.hnjk.edu.teaching.model.TeachingPlanCourse;
import com.hnjk.edu.teaching.model.UsualResults;
import com.hnjk.edu.teaching.util.ScoreEncryptionDecryptionUtil;

/**
 * 学生学习计划表
 * <code>StudentLearnPlan</code><p>
 * 用于学生主页显示及统计.
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-8-6 下午02:48:31
 * @see 
 * @version 1.0
 */

@Entity
@Table(name = "EDU_LEARN_STUPLAN")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class StudentLearnPlan extends BaseModel{
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDENTID")
	private StudentInfo studentInfo;//学生学籍信息
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST },fetch = FetchType.LAZY)
	@JoinColumn(name = "PLANSOURCEID")
	private TeachingPlanCourse teachingPlanCourse;//所修教学计划课程
	
	@Column(name="STATUS")
	private Integer status = 1;//1 - 预约学习 2 - 预约考试 3 - 考试已结束
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST },fetch = FetchType.LAZY)
	@JoinColumn(name = "EXAMRESULTSID")
	private ExamResults examResults;//考试分数信息
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST },fetch = FetchType.LAZY)
	@JoinColumn(name = "EXAMINFOID")
	private ExamInfo examInfo;//课程考试信息
	
	/**2010-10-15 v3.0.8 新增预约学习年度学期*/
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST },fetch = FetchType.LAZY)
	@JoinColumn(name = "YEARID",nullable=false)
	private YearInfo yearInfo;//预约学习年度
	
	@Column(name="TERM",nullable=false)
	private String term;//预约学习学期
	
	/**3.1.1 新增平时成绩*/
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST },fetch = FetchType.LAZY)
	@JoinColumn(name = "USUALRESULTSID")
	private UsualResults usualResults;//平时成绩
	
	/**3.1.6 新增计划外课程*/
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST },fetch = FetchType.LAZY)
	@JoinColumn(name = "PLANOUTCOURSEID")
	private Course planoutCourse;//计划外课程

	/**3.1.9新增预约考试年度、学期*/
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST },fetch = FetchType.LAZY)
	@JoinColumn(name = "ORDEREXAMYEAR")
	private YearInfo orderExamYear;//预约考试年度
	
	@Column(name="ORDEREXAMTERM")
	private String orderExamTerm;//预约考试学期	
	
	/**3.1.11 新增重考学生是否重做随堂练习字段*/
	@Column(name="ISREDOCOURSEEXAM",length=1)
	private String isRedoCourseExam;//是否重做随堂练习
	
	/**冗余字段，用来拼装SQL返回结果集*/
	@Transient
	private String studentId;
	
	@Transient
	private Integer statusInteger;//学习计划状态
	
	@Transient
	private String yearId;//年度ID	
	
	@Transient
	private String planSourceId;//教学计划课程ID
	
	@Transient
	private String integratedScore;//综合成绩
	
	@Transient
	private String usuallyScore;//平时成绩
	
	@Transient
	private String writtenScore;//卷面成绩
	
	@Transient
	private String examAbnormity;//成绩异常代码
	
	@Transient
	private String checkStatus;//成绩审核状态码
	
	@Transient
	private String courseScoreType;//成绩类型
	
	@Transient
	private String courseId;//教学计划课程-课程ID
	
	@Transient
	private Date examStartTime;//考试开始时间
	
	@Transient
	private Date examEndTime;//考试结束时间
	
	@Transient
	private Date auditDate;//成绩审核时间
	@Transient
	private String planOutcourseId;//计划外课程ID
	@Transient
	private String courseName;//计划外课程名
	@Transient
	private String examType;//计划外课程考试类型
	@Transient
	private Double planoutCreditHour;//计划外课程学分
	
	
	/**3.1.9新增预约考试年度、学期*/
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST },fetch = FetchType.LAZY)
	@JoinColumn(name = "STUCOURSEEXAMCOUNTID")
	@Where(clause = "isDeleted=0")
	private StuActiveCourseExamCount stuActiveCourseExamCount;//随堂练习统计表
	
	/**新增课程最终成绩以及标识成绩是否通过*/
	@Column(name="FINALSCORE")
	private String finalScore;//课程最终成绩
	
	@Column(name="ISPASS")
	private String isPass = "N";//成绩最终是否是否通过
	
	/**
	 * @return the usuallyScore
	 */
	public String getUsuallyScore() {
		return ScoreEncryptionDecryptionUtil.getInstance().decrypt(this.studentId,ExStringUtils.defaultIfEmpty(this.usuallyScore, ""));
	}

	/**
	 * @param usuallyScore the usuallyScore to set
	 */
	public void setUsuallyScore(String usuallyScore) {
		this.usuallyScore = usuallyScore;
	}

	/**
	 * @return the writtenScore
	 */
	public String getWrittenScore() {
		return ScoreEncryptionDecryptionUtil.getInstance().decrypt(this.studentId,ExStringUtils.defaultIfEmpty(this.writtenScore,""));
	}

	/**
	 * @param writtenScore the writtenScore to set
	 */
	public void setWrittenScore(String writtenScore) {
		this.writtenScore = writtenScore;
	}

	/**
	 * @return the planOutcourseId
	 */
	public String getPlanOutcourseId() {
		return planOutcourseId;
	}

	/**
	 * @param planOutcourseId the planOutcourseId to set
	 */
	public void setPlanOutcourseId(String planOutcourseId) {
		this.planOutcourseId = planOutcourseId;
	}

	/**
	 * @return the courseName
	 */
	public String getCourseName() {
		return courseName;
	}

	/**
	 * @param courseName the courseName to set
	 */
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	/**
	 * @return the examType
	 */
	public String getExamType() {
		return examType;
	}

	/**
	 * @param examType the examType to set
	 */
	public void setExamType(String examType) {
		this.examType = examType;
	}

	/**
	 * @return the planoutCreditHour
	 */
	public Double getPlanoutCreditHour() {
		return planoutCreditHour;
	}

	/**
	 * @param planoutCreditHour the planoutCreditHour to set
	 */
	public void setPlanoutCreditHour(Double planoutCreditHour) {
		this.planoutCreditHour = planoutCreditHour;
	}

	/**
	 * @return the studentId
	 */
	public String getStudentId() {
		return studentId;
	}

	/**
	 * @param studentId the studentId to set
	 */
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	/**
	 * @return the auditDate
	 */
	public Date getAuditDate() {
		return auditDate;
	}

	/**
	 * @param auditDate the auditDate to set
	 */
	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}

	/**
	 * @return the statusInteger
	 */
	public Integer getStatusInteger() {
		return statusInteger;
	}

	/**
	 * @param statusInteger the statusInteger to set
	 */
	public void setStatusInteger(Integer statusInteger) {
		this.statusInteger = statusInteger;
	}

	/**
	 * @return the yearId
	 */
	public String getYearId() {
		return yearId;
	}

	/**
	 * @param yearId the yearId to set
	 */
	public void setYearId(String yearId) {
		this.yearId = yearId;
	}

	/**
	 * @return the planSourceId
	 */
	public String getPlanSourceId() {
		return planSourceId;
	}

	/**
	 * @param planSourceId the planSourceId to set
	 */
	public void setPlanSourceId(String planSourceId) {
		this.planSourceId = planSourceId;
	}

	/**
	 * @return the integratedScore
	 */
	public String getIntegratedScore() {
		
		return ScoreEncryptionDecryptionUtil.getInstance().decrypt(this.studentId,ExStringUtils.defaultIfEmpty(this.integratedScore, ""));
	}

	/**
	 * @param integratedScore the integratedScore to set
	 */
	public void setIntegratedScore(String integratedScore) {
		this.integratedScore = integratedScore;
	}

	/**
	 * @return the examAbnormity
	 */
	public String getExamAbnormity() {
		return examAbnormity;
	}

	/**
	 * @param examAbnormity the examAbnormity to set
	 */
	public void setExamAbnormity(String examAbnormity) {
		this.examAbnormity = examAbnormity;
	}

	/**
	 * @return the checkStatus
	 */
	public String getCheckStatus() {
		return checkStatus;
	}

	/**
	 * @param checkStatus the checkStatus to set
	 */
	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}

	/**
	 * @return the courseScoreType
	 */
	public String getCourseScoreType() {
		return courseScoreType;
	}

	/**
	 * @param courseScoreType the courseScoreType to set
	 */
	public void setCourseScoreType(String courseScoreType) {
		this.courseScoreType = courseScoreType;
	}

	/**
	 * @return the courseId
	 */
	public String getCourseId() {
		return courseId;
	}

	/**
	 * @param courseId the courseId to set
	 */
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	/**
	 * @return the examStartTime
	 */
	public Date getExamStartTime() {
		return examStartTime;
	}

	/**
	 * @param examStartTime the examStartTime to set
	 */
	public void setExamStartTime(Date examStartTime) {
		this.examStartTime = examStartTime;
	}

	/**
	 * @return the examEndTime
	 */
	public Date getExamEndTime() {
		return examEndTime;
	}

	/**
	 * @param examEndTime the examEndTime to set
	 */
	public void setExamEndTime(Date examEndTime) {
		this.examEndTime = examEndTime;
	}

	/**
	 * @return the orderExamYear
	 */
	public YearInfo getOrderExamYear() {
		return orderExamYear;
	}

	/**
	 * @param orderExamYear the orderExamYear to set
	 */
	public void setOrderExamYear(YearInfo orderExamYear) {
		this.orderExamYear = orderExamYear;
	}

	/**
	 * @return the orderExamTerm
	 */
	public String getOrderExamTerm() {
		return orderExamTerm;
	}

	/**
	 * @param orderExamTerm the orderExamTerm to set
	 */
	public void setOrderExamTerm(String orderExamTerm) {
		this.orderExamTerm = orderExamTerm;
	}

	/**
	 * @return the planoutCourse
	 */
	public Course getPlanoutCourse() {
		return planoutCourse;
	}

	/**
	 * @param planoutCourse the planoutCourse to set
	 */
	public void setPlanoutCourse(Course planoutCourse) {
		this.planoutCourse = planoutCourse;
	}

	public UsualResults getUsualResults() {
		return usualResults;
	}

	public void setUsualResults(UsualResults usualResults) {
		this.usualResults = usualResults;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public YearInfo getYearInfo() {
		return yearInfo;
	}

	public void setYearInfo(YearInfo yearInfo) {
		this.yearInfo = yearInfo;
	}

	public ExamInfo getExamInfo() {
		return examInfo;
	}

	public void setExamInfo(ExamInfo examInfo) {
		this.examInfo = examInfo;
	}


	public ExamResults getExamResults() {
		return examResults;
	}

	public void setExamResults(ExamResults examResults) {
		this.examResults = examResults;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public StudentInfo getStudentInfo() {
		return studentInfo;
	}

	public void setStudentInfo(StudentInfo studentInfo) {
		this.studentInfo = studentInfo;
	}

	public TeachingPlanCourse getTeachingPlanCourse() {
		return teachingPlanCourse;
	}

	public void setTeachingPlanCourse(TeachingPlanCourse teachingPlanCourse) {
		this.teachingPlanCourse = teachingPlanCourse;
	}

	public String getIsRedoCourseExam() {
		return isRedoCourseExam;
	}

	public void setIsRedoCourseExam(String isRedoCourseExam) {
		this.isRedoCourseExam = isRedoCourseExam;
	}

	public StuActiveCourseExamCount getStuActiveCourseExamCount() {
		return stuActiveCourseExamCount;
	}

	public void setStuActiveCourseExamCount(
			StuActiveCourseExamCount stuActiveCourseExamCount) {
		this.stuActiveCourseExamCount = stuActiveCourseExamCount;
	}
	public String getFinalScore() {
		return finalScore;
	}

	public void setFinalScore(String finalScore) {
		this.finalScore = finalScore;
	}

	public String getIsPass() {
		return isPass;
	}

	public void setIsPass(String isPass) {
		this.isPass = isPass;
	}


}
