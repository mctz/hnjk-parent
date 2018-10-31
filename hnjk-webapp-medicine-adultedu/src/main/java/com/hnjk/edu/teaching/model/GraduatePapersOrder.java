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
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Formula;

import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.basedata.model.Edumanager;
import com.hnjk.edu.roll.model.StudentInfo;

/**
 * 毕业生论文预约信息表
 * <code>GraduatePapersOrder</code>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-10-19 下午02:31:30
 * @see
 * @version 1.0
 */
@Entity
@Table(name = "EDU_TEACH_PAPERSORDER")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class GraduatePapersOrder extends BaseModel {

	private static final long serialVersionUID = -8639004847300872423L;

	@OneToOne(optional = true, cascade = { CascadeType.MERGE,
			CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDENTID")
	private StudentInfo studentInfo;// 学生学籍

	@OneToOne(optional = true, cascade = { CascadeType.MERGE,
			CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "EXAMSUBID")
	private ExamSub examSub;// 毕业论文批次表
	
	/**3.0.9修改为关联课程*/
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "COURSEID")
	private Course course;// 关联课程

	
	@Column(name = "CURRENTTACHE")
	private String currentTache;// 所处毕业论文环节 取自字典：提纲初稿/二稿/定稿

			
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "GUIDTEACHERID")
	private Edumanager teacher;//指导老师
	
	@Column(name = "GUIDTEACHERNAME")
	private String guidTeacherName;// 指导老师姓名

	@Column(name = "AUDITMANID")
	private String auditManId;// 审核人ID

	@Column(name = "ORDERTIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date orderTime = new Date();// 预约时间

	@Column(name = "AUDITMAN")
	private String auditMan;// 审核人

	@Column(name = "STATUS")
	private Integer status = Constants.BOOLEAN_FALSE;// 审核状态 0 - 未审核 1-已审核

	@Column(name = "AUDITTIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date auditTime;// 审核时间
	
	/**3.1.11 新增毕业论文成绩*/
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST },fetch = FetchType.LAZY)
	@JoinColumn(name = "EXAMRESULTSID")
	private ExamResults examResults;//毕业论文成绩
	
	@Column(name="FIRSTSCORE")
	private Double firstScore;//初评成绩
	
	@Column(name="SECONDSCORE")
	private Double secondScore;//答辩成绩
	
	@Transient
	private String integratedScore;
	
	@Transient
	private String guidTeacherId;// 指导老师ID
	
	@Transient
	private String studentId;
	@Transient
	private String examSubId;

	//@Formula("(select max(m.SENDTIME) from EDU_TEACH_GRADUATEMSG m where m.PAPERSORDERID = resourceid and m.ISGROUPTOPIC = 'N')")
	//private Date lastUpdateTime;//最后更新时间
	
	//@Formula("(select m.FILLINMAN from EDU_TEACH_GRADUATEMSG m where m.PAPERSORDERID = resourceid and m.ISGROUPTOPIC = 'N' and m.sendtime = (select max(m.SENDTIME) from EDU_TEACH_GRADUATEMSG m where m.PAPERSORDERID = resourceid and m.ISGROUPTOPIC = 'N'))")
	@Formula("( select concat(to_char(max(m.SENDTIME),'yyyy-MM-dd')||',',m.FILLINMAN) from EDU_TEACH_GRADUATEMSG m  where m.PAPERSORDERID = resourceid and m.ISGROUPTOPIC = 'N' and m.sendtime= (select max(s.sendtime) from EDU_TEACH_GRADUATEMSG s where s.PAPERSORDERID = resourceid and s.isgrouptopic='N') group by m.sendtime,m.fillinman)")
	private String lastUpdateInfo;



	//public Date getLastUpdateTime() {
	//	return lastUpdateTime;
	//}

	
	//public void setLastUpdateTime(Date lastUpdateTime) {
	//	this.lastUpdateTime = lastUpdateTime;
	//}

	/**
	 * @return the lastUpdateInfo
	 */
	public String getLastUpdateInfo() {
		return lastUpdateInfo;
	}

	/**
	 * @param lastUpdateInfo the lastUpdateInfo to set
	 */
	public void setLastUpdateInfo(String lastUpdateInfo) {
		this.lastUpdateInfo = lastUpdateInfo;
	}

	/**
	 * @return the guidTeacherId
	 */
	public String getGuidTeacherId() {
		return guidTeacherId;
	}

	/**
	 * @param guidTeacherId the guidTeacherId to set
	 */
	public void setGuidTeacherId(String guidTeacherId) {
		this.guidTeacherId = guidTeacherId;
	}

	public String getAuditMan() {
		return auditMan;
	}

	public void setAuditMan(String auditMan) {
		this.auditMan = auditMan;
	}

	public String getAuditManId() {
		return auditManId;
	}

	public void setAuditManId(String auditManId) {
		this.auditManId = auditManId;
	}

	public Date getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}

	public String getCurrentTache() {
		return currentTache;
	}

	public void setCurrentTache(String currentTache) {
		this.currentTache = currentTache;
	}

	public ExamSub getExamSub() {
		return examSub;
	}

	public void setExamSub(ExamSub examSub) {
		this.examSub = examSub;
	}

	
	
	/**
	 * @return the teacher
	 */
	public Edumanager getTeacher() {
		return teacher;
	}

	/**
	 * @param teacher the teacher to set
	 */
	public void setTeacher(Edumanager teacher) {
		this.teacher = teacher;
	}

	public String getGuidTeacherName() {
		return guidTeacherName;
	}

	public void setGuidTeacherName(String guidTeacherName) {
		this.guidTeacherName = guidTeacherName;
	}

	public Date getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
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

	

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getExamSubId() {
		return examSubId;
	}

	public void setExamSubId(String examSubId) {
		this.examSubId = examSubId;
	}

	public ExamResults getExamResults() {
		return examResults;
	}

	public void setExamResults(ExamResults examResults) {
		this.examResults = examResults;
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
	
	public String getIntegratedScore() {
		if(getExamResults()!=null){
			return getExamResults().getIntegratedScore();
		}
		return "";
	}
	
}
