package com.hnjk.edu.teaching.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.hnjk.core.support.base.model.BaseModel;

/**
 * 开课审核表
 *
 */
@Entity
@Table(name = "EDU_TEACH_CHECKOPENCOURSE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CheckOpenCourse extends BaseModel {

	private static final long serialVersionUID = -5665323950035801442L;
//
//	@Column(name = "OPENSTATUS")
//	private String openStatus ; //开课状态
	
	@Column(name="CHECKSTATUS")
	private String checkStatus; //开课审核状态
	
	@Column(name="OPERATE")
	private String operate; //操作
	
	@Column(name="TERM")
	private String term; //调整前的学期
	
	@Column(name="UPDATETERM")
	private String updateTerm; //调整后的学期

	@Column(name="CHECKNAME")
	private String checkName; //审核人

	@Column(name="CHECKTIME")
	private Date checkTime; //审核时间


//	@OneToOne(optional = true, cascade = { CascadeType.MERGE,	CascadeType.PERSIST }, fetch = FetchType.LAZY)
//	@JoinColumn(name = "BRSCHOOLID")
//	private OrgUnit unit;//教学点
	
	
	@Column(name="APPLYTIME")
	private Date applyTime; //申请时间
	
	@Column(name="APPLYNAME")
	private String applyName; //申请人
	
	@Column(name="APPLYID")
	private String applyid;//申请人id
	
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,	CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "COURSESTATUSID")
	private TeachingPlanCourseStatus courseStatusId;//年级教学计划课程开课表id

//	public String getOpenStatus() {
//		return openStatus;
//	}
//
//	public void setOpenStatus(String openStatus) {
//		this.openStatus = openStatus;
//	}

	public String getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}

	public String getOperate() {
		return operate;
	}

	public void setOperate(String operate) {
		this.operate = operate;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

//	public OrgUnit getUnit() {
//		return unit;
//	}
//
//	public void setUnit(OrgUnit unit) {
//		this.unit = unit;
//	}

	public TeachingPlanCourseStatus getCourseStatusId() {
		return courseStatusId;
	}

	public void setCourseStatusId(TeachingPlanCourseStatus courseStatusId) {
		this.courseStatusId = courseStatusId;
	}

	public String getCheckName() {
		return checkName;
	}

	public void setCheckName(String checkName) {
		this.checkName = checkName;
	}

	public Date getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(Date checkTime) {
		this.checkTime = checkTime;
	}

	public String getUpdateTerm() {
		return updateTerm;
	}

	public void setUpdateTerm(String updateTerm) {
		this.updateTerm = updateTerm;
	}

	public Date getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}

	public String getApplyName() {
		return applyName;
	}

	public void setApplyName(String applyName) {
		this.applyName = applyName;
	}

	public String getApplyid() {
		return applyid;
	}

	public void setApplyid(String applyid) {
		this.applyid = applyid;
	}


	
	
}

