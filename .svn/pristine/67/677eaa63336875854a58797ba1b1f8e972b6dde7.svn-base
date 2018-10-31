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
import org.omg.CORBA.PRIVATE_MEMBER;

import com.hnjk.core.annotation.Historizable;
import com.hnjk.core.rao.dao.listener.OperationType;
import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.platform.system.model.BaseLogHistoryModel;

/**
 * 选修课成绩表
 * <code>ElectiveExamResults</code><p>
 */
/**
 * @author Administrator
 *
 */
@Entity
@Table(name="EDU_TEACH_ELECTIVEEXAMRESULTS")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Historizable(logable=true,value={OperationType.UPDATE})
public class ElectiveExamResults extends BaseModel{

	private static final long serialVersionUID = 1L;

	@OneToOne(optional = false, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "COURSEID")
	private Course course;// 考试课程
	
	@OneToOne(optional = false, cascade = { CascadeType.MERGE, CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDENTID")
	private StudentInfo studentInfo;//学号
	
	@Column(name = "writtenScore")
	private String writtenScore;//卷面成绩
	
	@Column(name = "usuallyScore")
    private String usuallyScore;//平时成绩
    
	@Column(name = "integratedScore")
	private String integratedScore;//总评成绩
	
	@Column(name="examAbnormity")
	private String examAbnormity;//#考试异常情况 0-正常 1-作弊 2-缺考 3-无卷 4-其它 5-缓考 6-免考 7-未修 8-免修
	
	@OneToOne(optional = false, cascade = { CascadeType.MERGE, CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "examSubid")
	private ExamSub examSub;
	
	@Column(name="COURSESCORETYPE")
	private String courseScoreType;//#考试成绩类型 10-数值型11-百分制12-150分制20-字符型22-二分制23-三分制24-四分制     25-五分制 30-等级制
	
	@Column(name="CHECKSTATUS")
    private String checkStatus;//审核状态 -1、成绩默认状态 0、保存 1、提交 2、待审核 3、审核通过 4、发布
	
	@Column(name="printDate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date printDate;
	
	@Column(name="MEMO")
	private String memo;
	
	public Course getCourse() {
		return course;
	}
	public void setCourse(Course course) {
		this.course = course;
	}

	public StudentInfo getStudentInfo() {
		return studentInfo;
	}
	public void setStudentInfo(StudentInfo studentInfo) {
		this.studentInfo = studentInfo;
	}

	public String getWrittenScore() {
		return writtenScore;
	}
	public void setWrittenScore(String writtenScore) {
		this.writtenScore = writtenScore;
	}

	public String getUsuallyScore() {
		return usuallyScore;
	}
	public void setUsuallyScore(String usuallyScore) {
		this.usuallyScore = usuallyScore;
	}

	public String getIntegratedScore() {
		return integratedScore;
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
	
	public ExamSub getExamSub() {
		return examSub;
	}
	public void setExamSub(ExamSub examSub) {
		this.examSub = examSub;
	}

	public String getCourseScoreType() {
		return courseScoreType;
	}
	public void setCourseScoreType(String courseScoreType) {
		this.courseScoreType = courseScoreType;
	}
	
	public String getCheckStatus() {
		return checkStatus;
	}
	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}
	
	public Date getPrintDate() {
		return printDate;
	}
	public void setPrintDate(Date printDate) {
		this.printDate = printDate;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	
}