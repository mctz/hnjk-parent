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

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.edu.basedata.model.YearInfo;

/**
 * 网上学习时间设置表。
 * <code>LearningTimeSetting</code><p>
 * 
 * @author：广东学苑教育发展有限公司.
 * @since： 2012-1-6 下午02:25:57
 * @see 
 * @version 1.0
 */
@Entity
@Table(name="EDU_LEAR_TIMESETTING")  
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class LearningTimeSetting extends BaseModel{

	private static final long serialVersionUID = -9094663387348959870L;

	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "YEARID")	
	private YearInfo yearInfo;//所属年度
	
	@Column(name="TERM")
	private String term;//所属学期
	
	@Column(name="STARTTIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date startTime;//开始时间
	
	@Column(name="ENDTIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date endTime;//截止时间
	
	@Column(name="LEARNTYPE")
	private String learnType;//学习类型，如随堂练习、作业、网络辅导等
	
	@Column(name="MEMO")
	private String memo;//备注

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
	 * @return the learnType
	 */
	public String getLearnType() {
		return learnType;
	}

	/**
	 * @param learnType the learnType to set
	 */
	public void setLearnType(String learnType) {
		this.learnType = learnType;
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
