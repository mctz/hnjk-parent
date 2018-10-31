package com.hnjk.edu.teaching.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Formula;

import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.roll.model.StudentInfo;

/**
 * 学生补考名单表.
 * <code>CourseOrder</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2014-5-14 下午05:40:18
 * @see 
 * @version 1.0
 */
@Entity
@Table(name="EDU_TEACH_MAKEUPLIST")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class StudentMakeupList extends BaseModel {
	
	private static final long serialVersionUID = -4465075735016169141L;
	
//	@Column(name = "STUDYNO")
//	private String studyNo;// 学生号
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDENTID")     
	private StudentInfo studentInfo;//学生学籍信息
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "COURSEID")
	private Course course;// 考试课程
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RESULTSID")
	private ExamResults examResults;//学生最后一次考试不通过的成绩
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST },fetch = FetchType.LAZY)
	@JoinColumn(name = "PLANSOURCEID")
	private TeachingPlanCourse teachingPlanCourse;//所修教学计划课程
	
	@Column(name="NEXTEXAMSUBID")
	private String nextExamSubId;//下一次补考考试批次ID
	
	@Column(name="ISPASS")
	private String isPass = "N";//是否补考通过
	
	@Column(name="ISMACHINEEXAM")
	private String isMachineExam = Constants.BOOLEAN_NO;// 是否机考
	
	  @Formula("(select count(*) from EDU_TEACH_EXAMRESULTS re where re.isdeleted=0 and re.COURSEID=COURSEID and re.STUDENTID=STUDENTID and re.checkstatus='4')")
	 	private Integer studentNum;//学生同一课程考试次数  
	
	  
	@Column(name="MEMO")
	private String memo;
	    
	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	  
	public TeachingPlanCourse getTeachingPlanCourse() {
		return teachingPlanCourse;
	}

	public void setTeachingPlanCourse(TeachingPlanCourse teachingPlanCourse) {
		this.teachingPlanCourse = teachingPlanCourse;
	}

	public ExamResults getExamResults() {
		return examResults;
	}

	public void setExamResults(ExamResults examResults) {
		this.examResults = examResults;
	}

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

	public void setStudentNum(Integer studentNum) {
		this.studentNum = studentNum;
	}

	public Integer getStudentNum() {
		return studentNum;
	}

	public String getNextExamSubId() {
		return nextExamSubId;
	}

	public void setNextExamSubId(String nextExamSubId) {
		this.nextExamSubId = nextExamSubId;
	}

	public String getIsPass() {
		return isPass;
	}

	public void setIsPass(String isPass) {
		this.isPass = isPass;
	}

	public String getIsMachineExam() {
		return isMachineExam;
	}

	public void setIsMachineExam(String isMachineExam) {
		this.isMachineExam = isMachineExam;
	}


//	public String getStudyNo() {
//		return studyNo;
//	}
//
//	public void setStudyNo(String studyNo) {
//		this.studyNo = studyNo;
//	}

	
	
}
