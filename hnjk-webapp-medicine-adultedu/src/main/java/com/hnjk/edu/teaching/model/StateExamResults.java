package com.hnjk.edu.teaching.model;

// default package

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
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.roll.model.StudentInfo;


/**
 * 学生统考成绩表
 * <code>StateExamResults</code><p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-6-21 下午05:09:01
 * @see 
 * @version 1.0
 */
@Entity
@Table(name="EDU_TEACH_STATEXAMRESULTS")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class StateExamResults   extends BaseModel {

	private static final long serialVersionUID = 4616139794825753517L;
	public static final String SCORETYPE_PASS="0";    //通过
	public static final String SCORETYPE_NOEXAM="1";  //免考
	public static final String EXEMPTION="2";         //免修
	public static final String REPLACE  ="3";         //代修
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.EAGER)
 	@JoinColumn(name = "STUDENTID")     
	private StudentInfo studentInfo;//学生学籍信息
     
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
 	@JoinColumn(name = "COURSEID")   
    private Course course;//统考课程
     
	
	@Column(name="SCORETYPE")
    private String scoreType;//成绩类型 0-通过 1 - 免考
     
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="PASSTIME")
    private Date passtime;//通过时间	 
	
	@Column(name="ISIDENTED")
	private String isIdented=Constants.BOOLEAN_NO;//是否认定
	
	@Column(name="MEMO")
	private String memo;//备注 
	
	@Column(name="CANDIDATENO")
	private String candidateNo;// 准考证号
	
	@Column(name="SCORE",scale=1)
	private Double score;// 成绩
	
	@Transient
	private String courseId;
	@Transient
	private String courseName;
	
	public String getIsIdented() {
		return isIdented;
	}

	public void setIsIdented(String isIdented) {
		this.isIdented = isIdented;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public Date getPasstime() {
		return passtime;
	}

	public void setPasstime(Date passtime) {
		this.passtime = passtime;
	}

	public String getScoreType() {
		return scoreType;
	}

	public void setScoreType(String scoreType) {
		this.scoreType = scoreType;
	}

	public StudentInfo getStudentInfo() {
		return studentInfo;
	}

	public void setStudentInfo(StudentInfo studentInfo) {
		this.studentInfo = studentInfo;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getCandidateNo() {
		return candidateNo;
	}

	public void setCandidateNo(String candidateNo) {
		this.candidateNo = candidateNo;
	}

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
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

	
}