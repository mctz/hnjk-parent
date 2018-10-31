package com.hnjk.edu.recruit.model;

import java.io.Serializable;
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
import com.hnjk.security.model.OrgUnit;
import com.hnjk.security.model.User;

@Entity
@Table(name = "EDU_RECRUIT_PREDISTRIBUTION")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Predistribution extends BaseModel implements Serializable{

	/**
	 * 
	 */
	public static final String APPLY_WAIT="0";
	public static final String APPLY_PASS="1";
	public static final String APPLY_NOPASS="2";
	
	private static final long serialVersionUID = 1054556826518562108L;
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST ,CascadeType.REFRESH}, fetch = FetchType.LAZY)
	@JoinColumn(name = "PREUNITID")
	private OrgUnit preUnit;
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST ,CascadeType.REFRESH}, fetch = FetchType.LAZY)
	@JoinColumn(name = "NEXTUNITID")
	private OrgUnit nextUnit;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="APPLYDATE")
	private Date applyDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="AUDITDATE")
	private Date auditDate;
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST ,CascadeType.REFRESH}, fetch = FetchType.LAZY)
	@JoinColumn(name = "APPLYUSERID")
	private User applyUser;
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST ,CascadeType.REFRESH}, fetch = FetchType.LAZY)
	@JoinColumn(name = "AUDITUSERID")
	private User auditUser;
	
	@Column(name = "APPLYREASON")
	private String applyReason;
	
	@Column(name = "AUDITSTATUS")
	private String auditStatus;//0 -待审核  1 -通过  2 -不通过

	public OrgUnit getPreUnit() {
		return preUnit;
	}

	public void setPreUnit(OrgUnit preUnit) {
		this.preUnit = preUnit;
	}

	public OrgUnit getNextUnit() {
		return nextUnit;
	}

	public void setNextUnit(OrgUnit nextUnit) {
		this.nextUnit = nextUnit;
	}

	public Date getApplyDate() {
		return applyDate;
	}

	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}

	public Date getAuditDate() {
		return auditDate;
	}

	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}

	public User getApplyUser() {
		return applyUser;
	}

	public void setApplyUser(User applyUser) {
		this.applyUser = applyUser;
	}

	public User getAuditUser() {
		return auditUser;
	}

	public void setAuditUser(User auditUser) {
		this.auditUser = auditUser;
	}

	public String getApplyReason() {
		return applyReason;
	}

	public void setApplyReason(String applyReason) {
		this.applyReason = applyReason;
	}

	public String getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}
	
	
}
