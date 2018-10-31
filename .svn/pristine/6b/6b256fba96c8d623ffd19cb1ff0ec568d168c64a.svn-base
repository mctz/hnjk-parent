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

import com.hnjk.core.annotation.Historizable;
import com.hnjk.core.rao.dao.listener.OperationType;
import com.hnjk.core.support.base.model.BaseModel;

/**
 * 学生毕业、学位审核表.
 * @author hzg
 *
 */
@Entity
@Table(name = "EDU_ROLL_STUAUDIT")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Historizable(logable=true,value={OperationType.UPDATE})
public class StudentGraduateAndDegreeAudit extends BaseModel{

	private static final long serialVersionUID = -8140261734271492373L;
	
	@OneToOne(optional = false, cascade = { CascadeType.MERGE, CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDENTINFOID")
	private StudentInfo studentInfo;//学生学籍
	
	@OneToOne(optional = false, cascade = { CascadeType.MERGE, CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "GRADUATEDATAID")
	private GraduateData graduateData;//学生毕业生信息
	
	@Column(name="GRADUATEAUDITSTATUS")
	private Integer graduateAuditStatus;//毕业审核状态 
	
	@Column(name="theGraduationStatis")
	private Integer theGraduationStatis;//结业审核状态
	
	@Column(name="theGraduationMemo")
	private String theGraduationMemo;//结业审核备注 
	
	@Column(name="DEGREEAUDITSTATUS")
	private Integer degreeAuditStatus;//学位审核状态
	
	@Column(name="GRADUATEAUDITMEMO")
	private String graduateAuditMemo;//毕业审核备注
	
	@Column(name="DEGREEAUDITMEMO")
	private String degreeAuditMemo;//学位审核备注
	
	/**3.2.6 新增审计信息*/
	@Column(name="AUDITMAN")
	private String auditMan;//审核人信息
	
	@Column(name="AUDITMANID")
	private String auditManId;//审核人ID
	
	@Column(name="AUDITTIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date auditTime;//审核时间
	
	@Column(name="CONFIRM")
	private String comfirm = "0";//是否确认  0否则空的时候是未确认/1确认

	@Column(name="STATUS")
	private String status;//记录审核前的学籍的一些状态  学籍状态##账号状态##用户状态
	
	
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getComfirm() {
		return comfirm;
	}

	public void setComfirm(String comfirm) {
		this.comfirm = comfirm;
	}

	public Integer getTheGraduationStatis() {
		return theGraduationStatis;
	}

	public void setTheGraduationStatis(Integer theGraduationStatis) {
		this.theGraduationStatis = theGraduationStatis;
	}

	public String getTheGraduationMemo() {
		return theGraduationMemo;
	}

	public void setTheGraduationMemo(String theGraduationMemo) {
		this.theGraduationMemo = theGraduationMemo;
	}

	/**
	 * @return the auditMan
	 */
	public String getAuditMan() {
		return auditMan;
	}

	/**
	 * @param auditMan the auditMan to set
	 */
	public void setAuditMan(String auditMan) {
		this.auditMan = auditMan;
	}

	/**
	 * @return the auditManId
	 */
	public String getAuditManId() {
		return auditManId;
	}

	/**
	 * @param auditManId the auditManId to set
	 */
	public void setAuditManId(String auditManId) {
		this.auditManId = auditManId;
	}

	/**
	 * @return the auditTime
	 */
	public Date getAuditTime() {
		return auditTime;
	}

	/**
	 * @param auditTime the auditTime to set
	 */
	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
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
	 * @return the graduateData
	 */
	public GraduateData getGraduateData() {
		return graduateData;
	}

	/**
	 * @param graduateData the graduateData to set
	 */
	public void setGraduateData(GraduateData graduateData) {
		this.graduateData = graduateData;
	}

	/**
	 * @return the graduateAuditStatus
	 */
	public Integer getGraduateAuditStatus() {
		return graduateAuditStatus;
	}

	/**
	 * @param graduateAuditStatus the graduateAuditStatus to set
	 */
	public void setGraduateAuditStatus(Integer graduateAuditStatus) {
		this.graduateAuditStatus = graduateAuditStatus;
	}

	/**
	 * @return the degreeAuditStatus
	 */
	public Integer getDegreeAuditStatus() {
		return degreeAuditStatus;
	}

	/**
	 * @param degreeAuditStatus the degreeAuditStatus to set
	 */
	public void setDegreeAuditStatus(Integer degreeAuditStatus) {
		this.degreeAuditStatus = degreeAuditStatus;
	}

	/**
	 * @return the graduateAuditMemo
	 */
	public String getGraduateAuditMemo() {
		return graduateAuditMemo;
	}

	/**
	 * @param graduateAuditMemo the graduateAuditMemo to set
	 */
	public void setGraduateAuditMemo(String graduateAuditMemo) {
		this.graduateAuditMemo = graduateAuditMemo;
	}

	/**
	 * @return the degreeAuditMemo
	 */
	public String getDegreeAuditMemo() {
		return degreeAuditMemo;
	}

	/**
	 * @param degreeAuditMemo the degreeAuditMemo to set
	 */
	public void setDegreeAuditMemo(String degreeAuditMemo) {
		this.degreeAuditMemo = degreeAuditMemo;
	}
	
	
}
