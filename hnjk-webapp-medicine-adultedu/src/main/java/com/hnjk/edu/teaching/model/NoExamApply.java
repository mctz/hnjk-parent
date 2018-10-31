package com.hnjk.edu.teaching.model;

// default package

import java.text.ParseException;
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

import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.roll.model.StudentInfo;

/**
 * 学生免考申请表
 * <code>NoExamApply</code>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-6-21 下午05:14:16
 * @see
 * @version 1.0
 */
@Entity
@Table(name = "EDU_TEACH_NOEXAM")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class NoExamApply extends BaseModel {

	private static final long serialVersionUID = -3514611520319922267L;

	@Column(name = "COURSESCORETYPE")
	private String courseScoreType;// 分值类型 :10-数值型 11-百分制
									// 12-150分制20-字符型22-二分制23-三分制24-四分制 25-五分制

	@Column(name = "SCOREFORCOUNT")
	private Double scoreForCount;// 总评成绩

	@Column(name = "ISMAJORCOURSE")
	private String isMajorCourse;// 是否当前专业课

	@Column(name = "ISOUTPLANCOURSE")
	private String isOutplancourse;// 是否计划外课程

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "SUBJECTTIME")
	private Date subjectTime = new Date();// 申请时间

	@Column(name = "CHECKSTATUS")
	private String checkStatus = "0";// 审核状态 字典：CodeCheckStatus 0:待审核 1:审核通过 2:审核未通过

	@Column(name = "CHECKMAN")
	private String checkMan;// 审核人

	@Column(name = "CHECKMANID")
	private String checkManId;// 审核人ID

	@Column(name = "CHECKOPINION")
	private String checkOpinion;// 审核意见

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CHECKTIME")
	private Date checkTime;// 审核时间

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CANCLEDATE")
	private Date cancleDate;// 取消审核时间

	@Column(name = "MEMO")
	private String memo;// 备注

	@Column(name = "UNSCORE")
	private String unScore;// #免修类型 0-通过1-免考2-免修3-代修

	@OneToOne(optional = true, cascade = { CascadeType.MERGE,
			CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDENTID")
	private StudentInfo studentInfo;// 学生学籍信息

	@OneToOne(optional = true, cascade = { CascadeType.MERGE,
			CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "COURSEID")
	private Course course;// 关联课程

	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,
			CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "STATEXAMId")
	private StateExamResults stateExamResults;// 统考成绩

	@Column(name = "MAJORCOURSEID")
	private String majorCourseId;// 专业课程ID
	
	@Transient
	private String subjectTimeStr;

	@Transient
	private String checkTimeStr;
	
	
	
	public String getSubjectTimeStr() {
		if (null!=this.subjectTime) {
			try {
				this.subjectTimeStr = ExDateUtils.formatDateStr(this.subjectTime, ExDateUtils.PATTREN_DATE_TIME);
			} catch (ParseException e) {
			}
		}
		return this.subjectTimeStr;
	}

	public String getCheckTimeStr() {
		if (null!=this.checkTime) {
			try {
				this.checkTimeStr = ExDateUtils.formatDateStr(this.checkTime, ExDateUtils.PATTREN_DATE_TIME);
			} catch (ParseException e) {
			}
		}
		return checkTimeStr;
	}

	public NoExamApply() {
	}

	public NoExamApply(StudentInfo studentInfo) {
		super();
		this.studentInfo = studentInfo;
	}

	public Date getCancleDate() {
		return cancleDate;
	}

	public void setCancleDate(Date cancleDate) {
		this.cancleDate = cancleDate;
	}

	public String getCheckMan() {
		return checkMan;
	}

	public void setCheckMan(String checkMan) {
		this.checkMan = checkMan;
	}

	public String getCheckManId() {
		return checkManId;
	}

	public void setCheckManId(String checkManId) {
		this.checkManId = checkManId;
	}

	public String getCheckOpinion() {
		return checkOpinion;
	}

	public void setCheckOpinion(String checkOpinion) {
		this.checkOpinion = checkOpinion;
	}

	public String getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}

	public Date getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(Date checkTime) {
		this.checkTime = checkTime;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public String getCourseScoreType() {
		return courseScoreType;
	}

	public void setCourseScoreType(String courseScoreType) {
		this.courseScoreType = courseScoreType;
	}

	public String getIsMajorCourse() {
		return isMajorCourse;
	}

	public void setIsMajorCourse(String isMajorCourse) {
		this.isMajorCourse = isMajorCourse;
	}

	public String getIsOutplancourse() {
		return isOutplancourse;
	}

	public void setIsOutplancourse(String isOutplancourse) {
		this.isOutplancourse = isOutplancourse;
	}

	public String getMajorCourseId() {
		return majorCourseId;
	}

	public void setMajorCourseId(String majorCourseId) {
		this.majorCourseId = majorCourseId;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Double getScoreForCount() {
		return scoreForCount;
	}

	public void setScoreForCount(Double scoreForCount) {
		this.scoreForCount = scoreForCount;
	}

	public StateExamResults getStateExamResults() {
		return stateExamResults;
	}

	public void setStateExamResults(StateExamResults stateExamResults) {
		this.stateExamResults = stateExamResults;
	}

	public StudentInfo getStudentInfo() {
		return studentInfo;
	}

	public void setStudentInfo(StudentInfo studentInfo) {
		this.studentInfo = studentInfo;
	}

	public Date getSubjectTime() {
		return subjectTime;
	}

	public void setSubjectTime(Date subjectTime) {
		this.subjectTime = subjectTime;
	}

	public String getUnScore() {
		return unScore;
	}

	public void setUnScore(String unScore) {
		this.unScore = unScore;
	}

}