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

import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.edu.basedata.model.YearInfo;

/**
 * 教学活动环节中的时间设置.不同的教学环节可能有不同的起止时间，都需要在这里进行设置.<p>
 * TODO:需要把以前涉及到时间设置的各业务，统一转到这个Model中设置.
 * @see com.hnjk.edu.learning.model.LearningTimeSetting
 * @author hzg
 *
 */

@Entity
@Table(name="EDU_TEACH_TIMESETTING")  
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TeachingActivityTimeSetting extends BaseModel{

	private static final long serialVersionUID = 6308905659189746792L;
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "YEARID")	
	private YearInfo yearInfo;//年度
	
	@Column(name="TERM",nullable=false)
	private String term;//学期
	
	@Column(name="MAINPROCESSTYPE",nullable=false)
	private String mainProcessType;//主业务类型，如：网上学习 
	
	@Column(name="SUBPROCESSTYPE")
	private String subProcessType;//分支业务类型，如：网上学习  -  学习类型
	
	@Column(name="STARTTIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date startTime;//开始时间
	
	@Column(name="ENDTIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date endTime;//结束时间
	
	@Column(name="WARNINGTIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date warningTime;//告警时间，如有些业务可能需要设置一个时间提前预警.
	
	@Column(name="MEMO")
	private String memo;//备注

	@Transient
	private String yearInfoId;
	
	public String getYearInfoId() {
		return yearInfoId;
	}

	public void setYearInfoId(String yearInfoId) {
		this.yearInfoId = yearInfoId;
	}

	/**
	 * @return the yearInfo
	 */
	public YearInfo getYearInfo() {
		return yearInfo;
	}

	/**
	 * @param yearInfo the yearInfo to set
	 */
	public void setYearInfo(YearInfo yearInfo) {
		this.yearInfo = yearInfo;
	}

	/**
	 * @return the term
	 */
	public String getTerm() {
		return term;
	}

	/**
	 * @param term the term to set
	 */
	public void setTerm(String term) {
		this.term = term;
	}

	/**
	 * @return the mainProcessType
	 */
	public String getMainProcessType() {
		return mainProcessType;
	}

	/**
	 * @param mainProcessType the mainProcessType to set
	 */
	public void setMainProcessType(String mainProcessType) {
		this.mainProcessType = mainProcessType;
	}

	/**
	 * @return the subProcessType
	 */
	public String getSubProcessType() {
		return subProcessType;
	}

	/**
	 * @param subProcessType the subProcessType to set
	 */
	public void setSubProcessType(String subProcessType) {
		this.subProcessType = subProcessType;
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
	 * @return the warningTime
	 */
	public Date getWarningTime() {
		return warningTime;
	}

	/**
	 * @param warningTime the warningTime to set
	 */
	public void setWarningTime(Date warningTime) {
		this.warningTime = warningTime;
	}

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
	
	
}
