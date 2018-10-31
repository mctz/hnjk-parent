package com.hnjk.edu.recruit.model;

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
import com.hnjk.edu.basedata.model.Classic;
import com.hnjk.edu.basedata.model.Major;
import com.hnjk.security.model.OrgUnit;
import com.hnjk.security.model.User;


/**
 *  <code>ExamineeChangeInfo</code>考生异动信息<p>
 * 
 * @author：zik 广东学苑教育发展有限公司.
 * @since： 2015-12-31 下午15:11:18
 * @see 
 * @version 1.0
 */
@Entity
@Table(name="EDU_RECRUIT_EXAMINEECHANGEINFO")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ExamineeChangeInfo  extends BaseModel {
	
	private static final long serialVersionUID = -4767756023490299425L;

	@OneToOne(optional = true, cascade = {CascadeType.MERGE,CascadeType.PERSIST}, fetch = FetchType.LAZY)  
	 @JoinColumn(name = "RECRUITPLANID") 
     private RecruitPlan recruitPlan;// 招生批次
          
	 @OneToOne(optional = true, cascade = {CascadeType.MERGE,CascadeType.PERSIST}, fetch = FetchType.LAZY)  
	 @JoinColumn(name = "ENROLLEEINFOID") 
	 private EnrolleeInfo enrolleeInfo;// 学生报名信息
	 
	 @Column(name="EXAMINEENAME",nullable=false)
     private String examineeName;// 考生姓名
	 
	 @OneToOne(optional = true, cascade = {CascadeType.MERGE,CascadeType.PERSIST}, fetch = FetchType.LAZY)  
	 @JoinColumn(name = "ROLLOUTSCHOOLID") 
	 private OrgUnit rolloutSchool;// 转出教学点
     
	 @OneToOne(optional = true, cascade = {CascadeType.MERGE,CascadeType.PERSIST}, fetch = FetchType.LAZY)  
	 @JoinColumn(name = "ROLLINSCHOOLID") 
	 private OrgUnit rollinSchool;// 转入教学点
	 
	 @OneToOne(optional = true, cascade = {CascadeType.MERGE,CascadeType.PERSIST}, fetch = FetchType.LAZY)  
	 @JoinColumn(name = "MAJORID") 
     private Major major;// 所属专业
	 
     @OneToOne(optional = true, cascade = {CascadeType.MERGE,CascadeType.PERSIST}, fetch = FetchType.LAZY)  
	 @JoinColumn(name = "CLASSICID")
     private Classic classic;// 培养层次
	 
     @OneToOne(optional = true, cascade = {CascadeType.MERGE,CascadeType.PERSIST}, fetch = FetchType.LAZY)  
     @JoinColumn(name = "OPERATORID")
     private User operator;// 操作人
     
	 @Column(name="OPERATORNAME")
     private String operatorName;// 操作人名称
	 
	 @Temporal(TemporalType.TIMESTAMP)
	 @Column(name="OPERATEDATE")
     private Date operateDate;// 操作时间

	public RecruitPlan getRecruitPlan() {
		return recruitPlan;
	}

	public void setRecruitPlan(RecruitPlan recruitPlan) {
		this.recruitPlan = recruitPlan;
	}

	public EnrolleeInfo getEnrolleeInfo() {
		return enrolleeInfo;
	}

	public void setEnrolleeInfo(EnrolleeInfo enrolleeInfo) {
		this.enrolleeInfo = enrolleeInfo;
	}

	public String getExamineeName() {
		return examineeName;
	}

	public void setExamineeName(String examineeName) {
		this.examineeName = examineeName;
	}

	public OrgUnit getRolloutSchool() {
		return rolloutSchool;
	}

	public void setRolloutSchool(OrgUnit rolloutSchool) {
		this.rolloutSchool = rolloutSchool;
	}

	public OrgUnit getRollinSchool() {
		return rollinSchool;
	}

	public void setRollinSchool(OrgUnit rollinSchool) {
		this.rollinSchool = rollinSchool;
	}

	public Major getMajor() {
		return major;
	}

	public void setMajor(Major major) {
		this.major = major;
	}

	public Classic getClassic() {
		return classic;
	}

	public void setClassic(Classic classic) {
		this.classic = classic;
	}

	public User getOperator() {
		return operator;
	}

	public void setOperator(User operator) {
		this.operator = operator;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public Date getOperateDate() {
		return operateDate;
	}

	public void setOperateDate(Date operateDate) {
		this.operateDate = operateDate;
	}
	 
}