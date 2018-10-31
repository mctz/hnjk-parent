package com.hnjk.edu.teaching.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Where;

import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.security.model.OrgUnit;

/**
 * 考试计划设置表
 * <code>ExamSetting</code><p>
 * 考试计划设置表，作为考试计划课程设置的模板，用于批量生成考试批次中的课程。格式如：<p>
 *    上午1(9:00-11:00)   下午1(14:00-16:00)   	上午2(9:00-11:00)	下午2(14:00-16:00)
 * 		计算机原理（0123）   	大学英语（0236）		 高等数学(一)（0563）	   大学语文(0254)			
 * 		...						...					...					...
 *    
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-8-16 上午11:25:28
 * @see 
 * @version 1.0
 */
@Entity
@Table(name="EDU_TEACH_EXAMSETTING")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ExamSetting   extends BaseModel{

	private static final long serialVersionUID = -5495743184856504904L;

	@Column(name="SETTINGNAME",unique=true)
	private String settingName;//设置名称
	
	@Column(name="TIMESEGMENT")
	private String timeSegment;//时间段，从字典取，上午/下午/晚上
	
	@Column(name="STARTTIME")
	@Temporal(TemporalType.TIME)
	private Date startTime;//开始时间 HH:mm:ss
	
	@Column(name="ENDTIME")	
	@Temporal(TemporalType.TIME)
	private Date endTime;//结束时间 HH:mm：ss
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "examSetting")
	@org.hibernate.annotations.Cascade(value = {  org.hibernate.annotations.CascadeType.DELETE_ORPHAN,org.hibernate.annotations.CascadeType.ALL })
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@OrderBy("showOrder ASC")
	@Where(clause = "isDeleted=0")
	private Set<ExamSettingDetails> details = new HashSet<ExamSettingDetails>(0);
	
	/**3.0.9新增所属单位*/
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "BRSCHOOLID")
	private OrgUnit brSchool;//所属校外学习中心
	
	@Transient
	private Date examDate;     //考试时间(临时变量)，用于编号排序
	@Transient
	private String examDateStr;//考试时间(临时变量，字符串形式)，用于编号排序
	
	public String getExamDateStr() {
		return examDateStr;
	}

	public void setExamDateStr(String examDateStr) {
		this.examDateStr = examDateStr;
	}

	public Date getExamDate() {
		return examDate;
	}

	public void setExamDate(Date examDate) {
		this.examDate = examDate;
	}

	public OrgUnit getBrSchool() {
		return brSchool;
	}

	public void setBrSchool(OrgUnit brSchool) {
		this.brSchool = brSchool;
	}

	public Set<ExamSettingDetails> getDetails() {
		return details;
	}

	public void setDetails(Set<ExamSettingDetails> details) {
		this.details = details;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getSettingName() {
		return settingName;
	}

	public void setSettingName(String settingName) {
		this.settingName = settingName;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public String getTimeSegment() {
		return timeSegment;
	}

	public void setTimeSegment(String timeSegment) {
		this.timeSegment = timeSegment;
	}
	
	@Override
	public String getResourceid() {
		return super.getResourceid();
	}
	
	
}
