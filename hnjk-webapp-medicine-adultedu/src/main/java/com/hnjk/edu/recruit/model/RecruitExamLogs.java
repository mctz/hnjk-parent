package com.hnjk.edu.recruit.model;

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
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.Examroom;
import com.hnjk.edu.learning.model.CourseExamPapers;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.teaching.model.ExamInfo;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.security.model.OrgUnit;

/**
 * 入学考试日志记录表.
 * <code>RecruitExamLogs</code><p>
 * @author：广东学苑教育发展有限公司.
 * @since： 2011-7-19 下午04:54:46
 * @see 
 * @version 1.0
 */
@Entity
@Table(name="EDU_RECRUIT_STUSTATES")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class RecruitExamLogs extends BaseModel {

	private static final long serialVersionUID = 4803503444001877020L;
	
//	@OneToOne(optional = true, cascade = {CascadeType.MERGE,CascadeType.PERSIST}, fetch = FetchType.LAZY)  
//	@JoinColumn(name = "EXAMPLANID") 
//	private RecruitExamPlan recruitExamPlan;//考试计划
	
	@Column(name="COURSENAME")
	private String courseName;//考试科目
	
	@OneToOne(optional = true, cascade = {CascadeType.MERGE,CascadeType.PERSIST}, fetch = FetchType.LAZY)  
	@JoinColumn(name = "BRSCHOOLID") 
	private OrgUnit brSchool;//校外学习中心
	
	@OneToOne(optional = true, cascade = {CascadeType.MERGE,CascadeType.PERSIST}, fetch = FetchType.LAZY)  
	@JoinColumn(name = "EXAMROOMID") 
	private Examroom examroom;//所在考场
	
	@OneToOne(optional = true, cascade = {CascadeType.MERGE,CascadeType.PERSIST}, fetch = FetchType.LAZY)  
	@JoinColumn(name = "ENROLLEEINFOID") 
	private EnrolleeInfo enrolleeInfo;//学生报名信息
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="LOGINTIME")
	private Date loginTime;//登录时间
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="LOGOUTTIME")
	private Date logoutTime;//退出时间
	
	@Column(name="LOGINIP")
	private String loginIp;//登录IP
	
	@Column(name="STATUS")
	private Integer status = -1; //0-在考，1-退出，2-已交卷， -1 默认状态

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="STARTTIME")
	private Date startTime;//开始时间
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="ENDTIME")
	private Date endTime;//结束时间
	
//	@OneToOne(optional = true, cascade = {CascadeType.MERGE,CascadeType.PERSIST}, fetch = FetchType.LAZY)  
//	@JoinColumn(name = "EXAMROOMPLANID")
//	private ExamRoomPlan examRoomPlan;//考场安排计划表
	
	@OneToOne(optional = true, cascade = {CascadeType.MERGE,CascadeType.PERSIST}, fetch = FetchType.LAZY)  
	@JoinColumn(name = "EXAMPAPERID")
	private CourseExamPapers courseExamPapers;//试卷
	
	/**3.1.10 新增考试信息，学生学籍*/
	@OneToOne(optional = true, cascade = {CascadeType.MERGE,CascadeType.PERSIST}, fetch = FetchType.LAZY)  
	@JoinColumn(name = "STUDENTINFOID")
	private StudentInfo studentInfo;//学生学籍
	
	@OneToOne(optional = true, cascade = {CascadeType.MERGE,CascadeType.PERSIST}, fetch = FetchType.LAZY)  
	@JoinColumn(name = "EXAMINFOID")
	private ExamInfo examInfo;//考试信息
	
	/**3.1.11 新增成绩字段*/
	@Column(name="EXAMSCORE",length=6)
	private Double examScore;//成绩
	
	@Column(name="MEMO",length=500)
	private String memo;//备注
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="LASTUPDATETIME")
	private Date lastUpdateTime;//最新状态更新时间
	
	@Transient
	private String isExamEnd;//是否已过考试时间
	@Transient
	private String finalStatus;//最终状态
	@Transient
	private String currentStatus;//当前状态
	@Transient
	private String isExceptional = Constants.BOOLEAN_NO;;//是否异常退出
	@Transient
	private String loginTimeStr; //登录时间(字符形式)
	@Transient
	private String logoutTimeStr;//退出时间(字符形式)
	
	/**
	 * @return the memo
	 */
	public String getMemo() {
		return memo;
	}

	/**
	 * @param memo the memo to set
	 */
	public void setMemo(String memo) {
		this.memo = memo;
	}

	/**
	 * @return the examScore
	 */
	public Double getExamScore() {
		return examScore;
	}

	/**
	 * @param examScore the examScore to set
	 */
	public void setExamScore(Double examScore) {
		this.examScore = examScore;
	}

	/**
	 * @return the studentInfo
	 */
	public StudentInfo getStudentInfo() {
		return studentInfo;
	}

	/**
	 * @param studentInfo the studentInfo to set
	 */
	public void setStudentInfo(StudentInfo studentInfo) {
		this.studentInfo = studentInfo;
	}

	/**
	 * @return the examInfo
	 */
	public ExamInfo getExamInfo() {
		return examInfo;
	}

	/**
	 * @param examInfo the examInfo to set
	 */
	public void setExamInfo(ExamInfo examInfo) {
		this.examInfo = examInfo;
	}

	/**
	 * @return the courseExamPapers
	 */
	public CourseExamPapers getCourseExamPapers() {
		return courseExamPapers;
	}

	/**
	 * @param courseExamPapers the courseExamPapers to set
	 */
	public void setCourseExamPapers(CourseExamPapers courseExamPapers) {
		this.courseExamPapers = courseExamPapers;
	}

	/**
	 * @return the startTime
	 */
	public Date getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the endTime
	 */
	public Date getEndTime() {
		return endTime;
	}

	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
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
	 * @return the brSchool
	 */
	public OrgUnit getBrSchool() {
		return brSchool;
	}

	/**
	 * @param brSchool the brSchool to set
	 */
	public void setBrSchool(OrgUnit brSchool) {
		this.brSchool = brSchool;
	}

	/**
	 * @return the examroom
	 */
	public Examroom getExamroom() {
		return examroom;
	}

	/**
	 * @param examroom the examroom to set
	 */
	public void setExamroom(Examroom examroom) {
		this.examroom = examroom;
	}

	/**
	 * @return the enrolleeInfo
	 */
	public EnrolleeInfo getEnrolleeInfo() {
		return enrolleeInfo;
	}

	/**
	 * @param enrolleeInfo the enrolleeInfo to set
	 */
	public void setEnrolleeInfo(EnrolleeInfo enrolleeInfo) {
		this.enrolleeInfo = enrolleeInfo;
	}

	/**
	 * @return the loginTime
	 */
	public Date getLoginTime() {
		return loginTime;
	}

	/**
	 * @param loginTime the loginTime to set
	 */
	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	/**
	 * @return the logoutTime
	 */
	public Date getLogoutTime() {
		return logoutTime;
	}

	/**
	 * @param logoutTime the logoutTime to set
	 */
	public void setLogoutTime(Date logoutTime) {
		this.logoutTime = logoutTime;
	}

	/**
	 * @return the loginIp
	 */
	public String getLoginIp() {
		return loginIp;
	}

	/**
	 * @param loginIp the loginIp to set
	 */
	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	/**
	 * @return the status
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public String getIsExamEnd() {
		Date today = new Date();
		if(getEndTime()!=null && today.after(getEndTime()) || getExamInfo()!=null && getExamInfo().getExamEndTime()!=null && today.after(getExamInfo().getExamEndTime())){
			return Constants.BOOLEAN_YES;
		} 
		return Constants.BOOLEAN_NO;
	}
	
	public String getFinalStatus() {
		if(getStatus()==2){//已交卷
			return "已考";
		} else {
			if(Constants.BOOLEAN_YES.equals(getIsExamEnd())){//考试结束
				return "缺考";
			} else {
				return "-";
			}
		}
	}
	public String getCurrentStatus() {
	
		switch (this.status) {

		case -1:
			this.currentStatus = "未登录";
			break;
		case 0:
			this.currentStatus = "考试中";
			break;
		case 1:
			this.currentStatus = "退出";
			break;
		case 2:
			this.currentStatus = "已交卷";
			break;
		default:
			this.currentStatus = "-";
			break;
		}
		if (Constants.BOOLEAN_YES.equals(getIsExceptional())) {
			currentStatus     = "断线";
		}
		return this.currentStatus;
	}
	public String getIsExceptional() {		
		try {
			long overtime = getOvertime();//超时间隔时间
			Date today = new Date();
			Date lastTime = getLastUpdateTime()!=null?getLastUpdateTime():getLoginTime();
			if(getStatus()!=null && (getStatus()==0 || getStatus()==1) 
					&& lastTime!=null && (today.getTime() - lastTime.getTime())>overtime*1000){//考试中非正常退出
				return Constants.BOOLEAN_YES;
			}
		} catch (Exception e) {			
		}
		return Constants.BOOLEAN_NO;
	}
	public long getOvertime() {
		long overtime = 130;
		try {
			String overtimeStr = CacheAppManager.getSysConfigurationByCode("exam.stustatus.overtime").getParamValue();
			overtimeStr = ExStringUtils.trimToEmpty(overtimeStr);
			if(ExStringUtils.isNotBlank(overtimeStr)){
				overtime = Long.valueOf(overtimeStr);
			}
		} catch (Exception e) {		
		}
		return overtime;
	}
	public String getLoginTimeStr() {
		try {
			if(getLoginTime() != null){
				this.loginTimeStr = ExDateUtils.formatDateStr(getLoginTime() , ExDateUtils.PATTREN_DATE_TIME);
			}
		} catch (ParseException e) {			
		}		
		return this.loginTimeStr;
	}

	public String getLogoutTimeStr() {
		try {
			if(getLogoutTime() != null){
				this.logoutTimeStr = ExDateUtils.formatDateStr(getLogoutTime() ,ExDateUtils.PATTREN_DATE_TIME);
			}
		} catch (ParseException e) {			
		}
		return this.logoutTimeStr;
	}
}
