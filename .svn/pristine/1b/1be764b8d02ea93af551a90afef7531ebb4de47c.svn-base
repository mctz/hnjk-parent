package com.hnjk.edu.teaching.model;

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
import com.hnjk.security.model.User;

/**
 * 学生异常成绩-缓考申请表
 * <code>AbnormalExam</code>
 * 
 * @author Zik，广东学苑教育发展有限公司
 * @since 2015-06-17 上午 10:08:31
 * @see
 * @version 1.0
 */
@Entity
@Table(name="EDU_TEACH_ABNORMALEXAM")
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AbnormalExam extends BaseModel {

	private static final long serialVersionUID = 5381023364076856375L;
	
	public static final String ABNORMALEXAM_CHECKSTATUS_NOAUDIT = "0";// 未审核
	public static final String ABNORMALEXAM_CHECKSTATUS_PAST = "1";// 审核通过
	public static final String ABNORMALEXAM_CHECKSTATUS_NOPAST = "2";// 审核不通过
	public static final String ABNORMALEXAM_CHECKSTATUS_REVOKE = "3";// 撤销审核
	
	/** 
	 * 分值类型 :10-数值型, 11-百分制, 12-150分制,
	 * 20-字符型, 22-二分制, 23-三分制, 24-四分制, 25-五分制
	 */
	@Column(name="COURSESCORETYPE")
	private String courseScoreType;
	/** 总评成绩**/
	@Column(name="SCOREFORCOUNT")
	private Double scoreForCount;
	/** 是否当前专业课**/
	@Column(name="ISMAJORCOURSE")
	private String isMajorCourse;
	/** 是否计划外课程**/
	@Column(name="ISOUTPLANCOURSE")
	private String isOutPlanCourse;
	/** 
	 * 考试类型：N-正考，Y-一补，T-二补，Q-结补
	 * 对应的字典表编码为CodeAbnormalExamType
	 **/
	@Column(name="EXAMTYPE")
	private String examType;
	/** 申请时间**/
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="APPLYDATE")
	private Date applyDate;
	/** 申请名字**/
	@Column(name="APPLYMANNAME")
	private String applyManName;
	/** 成绩状态**/
	@Column(name="CHECKSTATUS")
	private String checkStatus;
	/** 申请理由**/
	@Column(name="REASON")
	private String reason;
	/** 
	 * 异常类型：1-缓考，对应的字典表编码为CodeAbnormalType
	 * TODO: 目前只有缓考，以后有同样的需求就往后加
	 **/
	@Column(name="ABNORMALTYPE")
	private String abnormalType;
	/** 审核人名字**/
	@Column(name="CHECKMANNAME")
	private String checkManName;
	/** 审核意见**/
	@Column(name="CHECKOPINION")
	private String checkOpinion;
	/** 审核日期**/
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CHECKEDATE")
	private Date checkDate;
	/** 学生学籍信息**/
	@OneToOne(optional=true, cascade={CascadeType.MERGE, CascadeType.PERSIST},
			fetch=FetchType.LAZY)
	@JoinColumn(name="STUDENTID")
	private StudentInfo studentInfo;
	/** 课程**/
	@OneToOne(optional=true, cascade={CascadeType.MERGE, CascadeType.PERSIST},
			fetch=FetchType.LAZY)
	@JoinColumn(name="COURSEID")
	private Course course;
	/** 教学计划课程**/
	@OneToOne(optional=true, cascade={CascadeType.MERGE, CascadeType.PERSIST},
			fetch=FetchType.LAZY)
	@JoinColumn(name="PLANCOURSEID")
	private TeachingPlanCourse teachingPlanCourse;
	/** 申请人**/
	@OneToOne(optional=true, cascade={CascadeType.MERGE, CascadeType.PERSIST},
			fetch=FetchType.LAZY)
	@JoinColumn(name="APPLYMANID")
	private User applyMan;
	/** 审核人**/
	@OneToOne(optional=true, cascade={CascadeType.MERGE, CascadeType.PERSIST},
			fetch=FetchType.LAZY)
	@JoinColumn(name="CHECKMANID")
	private User checkMan;
	
	@Transient
	private String applyDateStr;
	
	@Transient
	private String checkDateStr;
	
	public AbnormalExam() {
	}
	
	public AbnormalExam(StudentInfo studentInfo) {
		super();
		this.studentInfo = studentInfo;
	}
	
	public String getCourseScoreType() {
		return courseScoreType;
	}
	public void setCourseScoreType(String courseScoreType) {
		this.courseScoreType = courseScoreType;
	}
	public Double getScoreForCount() {
		return scoreForCount;
	}
	public void setScoreForCount(Double scoreForCount) {
		this.scoreForCount = scoreForCount;
	}
	public String getIsMajorCourse() {
		return isMajorCourse;
	}
	public void setIsMajorCourse(String isMajorCourse) {
		this.isMajorCourse = isMajorCourse;
	}
	public String getIsOutPlanCourse() {
		return isOutPlanCourse;
	}
	public void setIsOutPlanCourse(String isOutPlanCourse) {
		this.isOutPlanCourse = isOutPlanCourse;
	}
	public String getExamType() {
		return examType;
	}
	public void setExamType(String examType) {
		this.examType = examType;
	}
	public Date getApplyDate() {
		return applyDate;
	}
	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}
	public String getApplyManName() {
		return applyManName;
	}
	public void setApplyManName(String applyManName) {
		this.applyManName = applyManName;
	}
	public String getCheckStatus() {
		return checkStatus;
	}
	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getAbnormalType() {
		return abnormalType;
	}
	public void setAbnormalType(String abnormalType) {
		this.abnormalType = abnormalType;
	}
	public String getCheckManName() {
		return checkManName;
	}
	public void setCheckManName(String checkManName) {
		this.checkManName = checkManName;
	}
	public String getCheckOpinion() {
		return checkOpinion;
	}
	public void setCheckOpinion(String checkOpinion) {
		this.checkOpinion = checkOpinion;
	}
	public Date getCheckDate() {
		return checkDate;
	}
	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
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
	public User getApplyMan() {
		return applyMan;
	}
	public void setApplyMan(User applyMan) {
		this.applyMan = applyMan;
	}
	public User getCheckMan() {
		return checkMan;
	}
	public void setCheckMan(User checkMan) {
		this.checkMan = checkMan;
	}

	public String getApplyDateStr() {
		if(null != this.applyDate){
			try {
				this.applyDateStr = ExDateUtils.formatDateStr(this.applyDate, ExDateUtils.PATTREN_DATE_TIME);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return applyDateStr;
	}

	public String getCheckDateStr() {
		if(null != this.checkDate){
			try {
				this.checkDateStr = ExDateUtils.formatDateStr(this.checkDate, ExDateUtils.PATTREN_DATE_TIME);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return checkDateStr;
	}
	
}
