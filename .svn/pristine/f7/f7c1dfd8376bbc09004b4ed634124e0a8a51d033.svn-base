package com.hnjk.edu.teaching.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.hnjk.core.support.base.model.BaseModel;

/**
 * 学生平时成绩日志表.
 * <code>UsualResults</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 
 * @see 
 * @version 1.0
 */
@Entity
@Table(name="HNJK_SYS_USRESULTSHISTORY")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UsualResultsHistory extends BaseModel{
	
	private static final long serialVersionUID = 1L;

	@Column(name="COURSEID")
	private String courseId;//专业课程ID
	
	@Column(name="STUDENTID")
	private String studentid;//学生ID
	
	@Column(name="YEARID")
	private String yearId;//年度ID
	
	@Column(name="TERM")
	private String term;//学期
	
	@Column(name="EXESCORE")
	private String exeScore;//作业练习分
	
	@Column(name="LIANXISCORE")
	private String lianxiScore;//随堂练习分
	
	@Column(name="AQSCORE")
	private String aqScore;//随堂问答分

	@Column(name="BBSSCORE")
	private String bbsScore;//网络辅导分
	
	@Column(name="USSCORE")
	private String usScore;//平时分总分
	
	
	@Column(name="STULEARNPLANID")
	private String stuLearnPlanId;//平时分总分
	
	
	
	
	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public String getStudentid() {
		return studentid;
	}

	public void setStudentid(String studentid) {
		this.studentid = studentid;
	}

	public String getYearId() {
		return yearId;
	}

	public void setYearId(String yearId) {
		this.yearId = yearId;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public String getExeScore() {
		return exeScore;
	}

	public void setExeScore(String exeScore) {
		this.exeScore = exeScore;
	}

	public String getLianxiScore() {
		return lianxiScore;
	}

	public void setLianxiScore(String lianxiScore) {
		this.lianxiScore = lianxiScore;
	}

	public String getAqScore() {
		return aqScore;
	}

	public void setAqScore(String aqScore) {
		this.aqScore = aqScore;
	}

	public String getBbsScore() {
		return bbsScore;
	}

	public void setBbsScore(String bbsScore) {
		this.bbsScore = bbsScore;
	}

	public String getUsScore() {
		return usScore;
	}

	public void setUsScore(String usScore) {
		this.usScore = usScore;
	}


	
	public void setUsualResultsHistory(UsualResults us,String stuLearnPlanId){
		this.courseId = us.getCourse().getResourceid();
		this.studentid = us.getStudentInfo().getResourceid();
		this.yearId = us.getYearInfo().getResourceid();
		this.aqScore = us.getAskQuestionResults();
		this.bbsScore = us.getBbsResults();
		this.exeScore = us.getExerciseResults();
		this.lianxiScore = us.getCourseExamResults();
		this.term = us.getTerm();
		this.usScore = us.getUsualResults();
		this.stuLearnPlanId = stuLearnPlanId;
	}

	public void setStuLearnPlanId(String stuLearnPlanId) {
		this.stuLearnPlanId = stuLearnPlanId;
	}

	public String getStuLearnPlanId() {
		return stuLearnPlanId;
	}



}
