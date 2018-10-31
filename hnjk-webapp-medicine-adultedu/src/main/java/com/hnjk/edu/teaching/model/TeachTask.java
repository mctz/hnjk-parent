package com.hnjk.edu.teaching.model;

// default package

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Where;

import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.basedata.model.YearInfo;

/**
 * 教学计划 - 教学任务书
 * <code>TeachTask</code>
 * 当学生预约学习结束时，教务管理人员通过“生成任务书课程”来获取当学期教学任务书的课程，然后给课程分配老师-->发送任务书(过滤掉成人面授的老师)；
	初始化的任务书，需要逐个分配，以后可以复制沿用；
	老师收到任务书后，填写任务，发回给教务管理人员审核。
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2010-6-21 下午02:42:04
 * @see
 * @version 1.0
 */
@Entity
@Table(name = "EDU_TEACH_TEACHTASK")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TeachTask extends BaseModel implements Serializable {

	private static final long serialVersionUID = -7915163425403199885L;

	@OneToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "COURSEID")
	private Course course;// 教学计划的课程

	
	@Column(name = "TEACHERNAME")
	private String teacherName;// 负责老师姓名

	@Column(name = "TEACHERID")
	private String teacherId;// 负责老师ID

	@Column(name = "TASKSTATUS")
	private Long taskStatus = 0L;// 任务状态  0-生成/1-发送给老师/2-老师发回/3-审核发布

	@Column(name = "MEMO")
	private String memo;// 备注

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "RETURNTIME")
	private Date returnTime;// 要求返回时间

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "REALRETURNTIME")
	private Date realReturnTime;// 实际返回时间

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "teachTask")
	@org.hibernate.annotations.Cascade(value = { org.hibernate.annotations.CascadeType.ALL })
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@OrderBy("showOrder ASC")
	@Where(clause = "isDeleted=0")
	private Set<TeachTaskDetails> teachTaskDetails = new HashSet<TeachTaskDetails>(0);

	/**3.0.9 新增字段，将老师与课程的关联调整到教学任务书中*/
	 @Column(name="DEFAULTTEACHERIDS")
	 private String defaultTeacherIds;//主讲老师ID集合，使用","隔开
	 
	 @Column(name="DEFAULTTEACHERNAMES")
	 private String defaultTeacherNames;//主讲老师姓名集合,使用","隔开
	
	 @Column(name="ASSISTANTNAMES")
	private String assistantNames;//辅导老师姓名,使用","隔开
		 
	@Column(name="ASSISTANTIDS")
	private String assistantIds;//辅导老师ID，使用","隔开	
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "YEARID")
	private YearInfo yearInfo;//年度
	
	@Column(name="TERM")
	private String term;//学期
	
	@Column(name="AUDITMAN")
	private String auditMan;//审核人
	
	@Column(name="AUDITMANID")
	private String auditManId;//审核人ID
	
	@Column(name="AUDITDATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date auditDate;//审核时间
	
	/**3.1.1 将教学任务书明细的建议移动到主表*/	 
	 @Lob
	 @Basic(fetch = FetchType.LAZY)
	 @Column(name="TASKADVISE")
	 private String taskAdvise;//教学任务建议 由教务管理人员填写
	 	 
	 
	 /**3.1.9新增任务书模板*/
	 @Column(name="ISTEMPLATE")
	 private String isTemplate = Constants.BOOLEAN_NO;//是否为模板
	 
	 
	 
	/**
	 * @return the isTemplate
	 */
	public String getIsTemplate() {
		return isTemplate;
	}



	/**
	 * @param isTemplate the isTemplate to set
	 */
	public void setIsTemplate(String isTemplate) {
		this.isTemplate = isTemplate;
	}



	public String getTaskAdvise() {
		return taskAdvise;
	}



	public void setTaskAdvise(String taskAdvise) {
		this.taskAdvise = taskAdvise;
	}



	public TeachTask() {
	}

	
	
	public String getAssistantIds() {
		return assistantIds;
	}



	public void setAssistantIds(String assistantIds) {
		this.assistantIds = assistantIds;
	}



	public String getAssistantNames() {
		return assistantNames;
	}



	public void setAssistantNames(String assistantNames) {
		this.assistantNames = assistantNames;
	}



	public Date getAuditDate() {
		return auditDate;
	}



	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
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



	public String getDefaultTeacherIds() {
		return defaultTeacherIds;
	}



	public void setDefaultTeacherIds(String defaultTeacherIds) {
		this.defaultTeacherIds = defaultTeacherIds;
	}



	public String getDefaultTeacherNames() {
		return defaultTeacherNames;
	}



	public void setDefaultTeacherNames(String defaultTeacherNames) {
		this.defaultTeacherNames = defaultTeacherNames;
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



	public TeachTask(Date returnTime) {
		this.returnTime = returnTime;
	}

	public Date getRealReturnTime() {
		return realReturnTime;
	}

	public void setRealReturnTime(Date realReturnTime) {
		this.realReturnTime = realReturnTime;
	}

	public Date getReturnTime() {
		return returnTime;
	}

	public void setReturnTime(Date returnTime) {
		this.returnTime = returnTime;
	}

	public Set<TeachTaskDetails> getTeachTaskDetails() {
		return teachTaskDetails;
	}

	public void setTeachTaskDetails(Set<TeachTaskDetails> teachTaskDetails) {
		this.teachTaskDetails = teachTaskDetails;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Long getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(Long taskStatus) {
		this.taskStatus = taskStatus;
	}

	public String getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	//获取老师id列表
	public Set<String> getTeacherIds(){
		String ids = "";
//		if(ExStringUtils.isNotEmpty(this.getDefaultTeacherIds()))
//			ids += this.getDefaultTeacherIds();
		if(ExStringUtils.isNotEmpty(this.getAssistantIds())) {
			ids += this.getAssistantIds();
		}
		if(ExStringUtils.isNotEmpty(this.getTeacherId())) {
			ids += this.getTeacherId();
		}
		if(ExStringUtils.isNotEmpty(ids)){
			return new HashSet<String>(Arrays.asList(ids.split(",")));
		} else {
			return new HashSet<String>(0);
		}
	}
	
	public Long getNextShowOrder(){
		Long order = 0L;
		for (TeachTaskDetails d : getTeachTaskDetails()) {
			if(d.getShowOrder()>order) {
				order = d.getShowOrder();
			}
		}
		return ++order;
	}
}