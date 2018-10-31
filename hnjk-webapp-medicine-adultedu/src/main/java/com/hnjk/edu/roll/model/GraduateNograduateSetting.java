package com.hnjk.edu.roll.model;


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
 * 延迟毕业申请设置表.
 * @author hzg
 *
 */
@Entity
@Table(name = "EDU_ROLL_NOGRADSETTING")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class GraduateNograduateSetting extends BaseModel{

	private static final long serialVersionUID = -7771524829070943637L;
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "YEARID")
	private YearInfo yearInfo;//年度
	
	@Column(name = "TERM")
	private String term;//学期
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "STARTDATE")
	private Date startDate;//申请开始时间
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ENDDATE")
	private Date endDate;//申请截止时间
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "REVOKEDATE")
	private Date revokeDate;//撤销申请时间
	
	@Column(name = "MEMO")
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
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the revokeDate
	 */
	public Date getRevokeDate() {
		return revokeDate;
	}

	/**
	 * @param revokeDate the revokeDate to set
	 */
	public void setRevokeDate(Date revokeDate) {
		this.revokeDate = revokeDate;
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
