package com.hnjk.edu.recruit.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
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
import com.hnjk.core.support.context.Constants;
import com.hnjk.platform.system.model.Attachs;
import com.hnjk.security.model.OrgUnit;


/**
 * 校外学习中心招生批次表.
 * 走流程.
 * <code>BranchSchoolPlan</code><p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-6-11 上午11:26:44
 * @see 
 * @version 1.0
 */
@Entity
@Table(name="EDU_RECRUIT_BRSCHPLAN")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class BranchSchoolPlan extends BaseModel{

	private static final long serialVersionUID = 7579344222750731278L;
	
	@Column(name="WF_ID",nullable=false)
	private Long wfid = 0L;//流程实例ID
	
	 @OneToOne(optional = true, cascade = {CascadeType.MERGE,CascadeType.PERSIST}, fetch = FetchType.LAZY)  
	 @JoinColumn(name = "BRANCHSCHOOLID") 
     private OrgUnit branchSchool;//所属校外学习中心
	
	 @Column(name="RECRUITPLANNAME")
	 private String recruitplanName;//招生批次名称 对应招生计划表中的这个字段
	 
	 @OneToOne(optional = true, cascade = {CascadeType.MERGE,CascadeType.PERSIST}, fetch = FetchType.LAZY)  
	 @JoinColumn(name="RECRUITPLANID")
	 private RecruitPlan recruitplan;//对应的招生批次
	 
	 @Column(name="DOCUMENTCODE")
     private String documentCode;//文号	
	
	 @Column(name="FILLINMAN")
     private String fillinMan;//填写人
	 
	 @Column(name="FILLINMANID")
     private String fillinManId;//填写人ID
	 
	 @Temporal(TemporalType.TIMESTAMP)
	 @Column(name="FILLINDATE")
     private Date fillinDate ;//填写日期
	 
	 @Column(name="ISAUDITED")
	 private String isAudited = Constants.BOOLEAN_NO;//是否审核通过
	 
	 @OneToMany(fetch = FetchType.LAZY, mappedBy = "branchSchoolPlan")
	 @org.hibernate.annotations.Cascade(value={org.hibernate.annotations.CascadeType.DELETE_ORPHAN,org.hibernate.annotations.CascadeType.ALL})
	 @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	 @Where(clause="isDeleted=0")
	 private Set<BranchSchoolMajor> branchSchoolMajor = new LinkedHashSet<BranchSchoolMajor>(0);//校外学习中心招生批次 ：校外学习中心招生专业 n:1
	 	 	 
	 /**3.1.3新增教学模式*/
	 @Column(name="TEACHINGTYPE")
	 private String teachingType;//教学模式 字典值
	 
	 /**3.1.4 新增校外学习中，新专业申报*/
	 @OneToMany(fetch = FetchType.LAZY, mappedBy = "branchSchoolPlan")
	 @org.hibernate.annotations.Cascade(value={org.hibernate.annotations.CascadeType.DELETE_ORPHAN,org.hibernate.annotations.CascadeType.ALL})
	 @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	 @Where(clause="isDeleted=0")	
	 @OrderBy("teachingType ASC")
	 private Set<BranchShoolNewMajor> branchShoolNewMajor = new LinkedHashSet<BranchShoolNewMajor>(0);//校外学习中心招生批次 ：新专业申报 n:1
	 
	 @Transient
	 private List<Attachs> attachs = new ArrayList<Attachs>();//申报新专业时上传的附件
	 
	 
	/**
	 * @return the attachs
	 */
	public List<Attachs> getAttachs() {
		return attachs;
	}

	/**
	 * @param attachs the attachs to set
	 */
	public void setAttachs(List<Attachs> attachs) {
		this.attachs = attachs;
	}
	 
	/**
	 * @return the branchShoolNewMajor
	 */
	public Set<BranchShoolNewMajor> getBranchShoolNewMajor() {
		return branchShoolNewMajor;
	}

	/**
	 * @param branchShoolNewMajor the branchShoolNewMajor to set
	 */
	public void setBranchShoolNewMajor(Set<BranchShoolNewMajor> branchShoolNewMajor) {
		this.branchShoolNewMajor = branchShoolNewMajor;
	}

	/**
	 * @return the teachingType
	 */
	public String getTeachingType() {
		return teachingType;
	}

	/**
	 * @param teachingType the teachingType to set
	 */
	public void setTeachingType(String teachingType) {
		this.teachingType = teachingType;
	}

	public String getIsAudited() {
		return isAudited;
	}

	public void setIsAudited(String isAudited) {
		this.isAudited = isAudited;
	}

	public Set<BranchSchoolMajor> getBranchSchoolMajor() {
		return branchSchoolMajor;
	}

	public void addBranchSchoolMajor(BranchSchoolMajor branchSchoolMajor){
		getBranchSchoolMajor().add(branchSchoolMajor);
	}
	
	public void setBranchSchoolMajor(Set<BranchSchoolMajor> branchSchoolMajor) {
		this.branchSchoolMajor = branchSchoolMajor;
	}

	public RecruitPlan getRecruitplan() {
		return recruitplan;
	}

	public void setRecruitplan(RecruitPlan recruitplan) {
		this.recruitplan = recruitplan;
	}

	public String getRecruitplanName() {
		return recruitplanName;
	}

	public void setRecruitplanName(String recruitplanName) {
		this.recruitplanName = recruitplanName;
	}

	public OrgUnit getBranchSchool() {
		return branchSchool;
	}

	public void setBranchSchool(OrgUnit branchSchool) {
		this.branchSchool = branchSchool;
	}

	public String getDocumentCode() {
		return documentCode;
	}

	public void setDocumentCode(String documentCode) {
		this.documentCode = documentCode;
	}

	public Date getFillinDate() {
		return fillinDate;
	}

	public void setFillinDate(Date fillinDate) {
		this.fillinDate = fillinDate;
	}

	public String getFillinMan() {
		return fillinMan;
	}

	public void setFillinMan(String fillinMan) {
		this.fillinMan = fillinMan;
	}

	public String getFillinManId() {
		return fillinManId;
	}

	public void setFillinManId(String fillinManId) {
		this.fillinManId = fillinManId;
	}

	public Long getWfid() {
		return wfid;
	}

	public void setWfid(Long wfid) {
		this.wfid = wfid;
	}
	
	 
	 

}
