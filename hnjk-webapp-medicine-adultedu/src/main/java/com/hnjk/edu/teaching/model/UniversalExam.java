package com.hnjk.edu.teaching.model;

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
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.teaching.util.ScoreEncryptionDecryptionUtil;
import com.hnjk.security.model.User;

/**
 * 统考成绩表--教学计划内课程
 * <code> UniversalExam </code>
 * 
 * @author Zik ，广东学苑教育发展有限公司
 * @since 2015-7-9 上午 09:45:30
 * @see
 * @version 1.0
 */
@Entity
@Table(name="EDU_TEACH_UNIVERSALEXAM")
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UniversalExam extends BaseModel {

	private static final long serialVersionUID = 4757412431662966716L;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="OPERATEDATE")
	private Date operateDate; // 操作时间
	
	@Column(name="OPERATORNAME")
	private String operatorName; // 操作人名称
	
	@Column(name="CHECKSTATUS")
	private String checkStatus; // 成绩状态
	
	@Column(name="SCORE")
	private String score; // 分数
	
	@Column(name="CERTIFICATENO")
	private String certificateNo; // 证书编号
	
	@Temporal(TemporalType.DATE)
	@Column(name="EXAMDATE")
	private Date examDate; // 考试日期
	
	@Column(name="WHICHTIME")
	private String whichTime; // 第几次考试
	
	@ManyToOne(optional=false, cascade={CascadeType.MERGE, CascadeType.PERSIST },fetch=FetchType.LAZY)
	@JoinColumn(name="STUDENTID")
	private StudentInfo studentInfo; // 学籍
	
	@OneToOne(optional=false, cascade={CascadeType.MERGE, CascadeType.PERSIST}, fetch=FetchType.LAZY)
	@JoinColumn(name="COURSEID")
	private Course course; // 基础课程
	
	@OneToOne(optional=false, cascade={CascadeType.MERGE, CascadeType.PERSIST}, fetch=FetchType.LAZY)
	@JoinColumn(name="PLANCOURSEID")
	private TeachingPlanCourse teachingPlanCourse; // 教学计划课程
	
	@OneToOne(optional=false, cascade={CascadeType.MERGE, CascadeType.PERSIST}, fetch=FetchType.LAZY)
	@JoinColumn(name="OPERATORID")
	private User operator; // 操作人
	
	@OneToOne(optional=false, cascade={CascadeType.MERGE, CascadeType.PERSIST}, fetch=FetchType.LAZY)
	@JoinColumn(name="EXAMRESULTSID")
	private ExamResults examResults;
	
	@Transient
	private String isPass;

	public UniversalExam() {
		
	}

	public String getIsPass(){
//		if(Integer.parseInt(ScoreEncryptionDecryptionUtil.getInstance().encrypt(this.studentInfo.getResourceid(),this.score))>=60){
//			return "通过";
//		}
//		else return "不通过";
		return isPass = Integer.parseInt(this.score)>=60?"Y":"N";
	}
	
	public Date getOperateDate() {
		return operateDate;
	}

	public void setOperateDate(Date operateDate) {
		this.operateDate = operateDate;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getCertificateNo() {
		return certificateNo;
	}

	public void setCertificateNo(String certificateNo) {
		this.certificateNo = certificateNo;
	}

	public Date getExamDate() {
		return examDate;
	}

	public void setExamDate(Date examDate) {
		this.examDate = examDate;
	}

	public String getWhichTime() {
		return whichTime;
	}

	public void setWhichTime(String whichTime) {
		this.whichTime = whichTime;
	}

	public StudentInfo getStudentInfo() {
		return studentInfo;
	}

	public void setStudentInfo(StudentInfo studentInfo) {
		this.studentInfo = studentInfo;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public TeachingPlanCourse getTeachingPlanCourse() {
		return teachingPlanCourse;
	}

	public void setTeachingPlanCourse(TeachingPlanCourse teachingPlanCourse) {
		this.teachingPlanCourse = teachingPlanCourse;
	}

	public User getOperator() {
		return operator;
	}

	public void setOperator(User operator) {
		this.operator = operator;
	}

	public ExamResults getExamResults() {
		return examResults;
	}

	public void setExamResults(ExamResults examResults) {
		this.examResults = examResults;
	}
	
}
