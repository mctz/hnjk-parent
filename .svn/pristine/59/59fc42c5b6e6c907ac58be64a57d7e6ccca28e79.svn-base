package com.hnjk.edu.recruit.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.edu.basedata.model.Classic;
import com.hnjk.edu.basedata.model.Major;
import com.hnjk.security.model.OrgUnit;


/**
 *  <code>RecruitMajor</code>招生计划-招生专业.<p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-3-15 上午10:23:15
 * @see 
 * @version 1.0
 */
@Entity
@Table(name="EDU_RECRUIT_MAJOR")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class RecruitMajor  extends BaseModel {
	
	 private static final long serialVersionUID = 2429779892437557950L;
	 
	 @Column(name="RECRUITMAJORNAME",nullable=false)
     private String recruitMajorName;//招生专业名称
          
	 @Column(name="STUDYPERIOD",nullable=false)
     private Double studyperiod;//学制
     
	 @Column(name="LIMITNUM")
     private Long limitNum;//上限人数
	 
	 @Column(name="LOWERNUM")
     private Long lowerNum;//下限人数
	 
	 @Column(name="TUITIONFEE")	 
     private Double tuitionFee;//学费
	 
	 @Column(name="STATUS")
     private Long status = 0L;//招生专业的省厅审批结果:1-通过，0-不通过
	 
	 @Column(name="MEMO")
     private String memo;//备注
	 
	 @Column(name="FILLINMAN")
     private String fillinMan;//填写人
	 
	 @Column(name="FILLINMANID")
     private String fillinManId;//填写人ID
	 
	 @Temporal(TemporalType.TIMESTAMP)
	 @Column(name="FILLINDATE")	 
     private Date fillinDate;//填写时间
	 
	 @Column(name="UPDATEMAN")
     private String updateMan;//更新人
	 
	 @Column(name="UPDATEMANID")
     private String updateManId;//更新人ID
	 
	 @Temporal(TemporalType.TIMESTAMP)
	 @Column(name="UPDATEDATE")
     private Date updateDate;//更新时间
	 
	 @Column(name="SHOWORDER")
     private Long showOrder;//排序号	 
	
	 @OneToOne(optional = true, cascade = {CascadeType.MERGE,CascadeType.PERSIST}, fetch = FetchType.LAZY)  
	 @JoinColumn(name = "MAJORID") 
     private Major major;//所属专业，一对一单向关联
	 
     @OneToOne(optional = true, cascade = {CascadeType.MERGE,CascadeType.PERSIST}, fetch = FetchType.LAZY)  
	 @JoinColumn(name = "CLASSIC")
     private Classic classic;//所属层次
          
     @ManyToOne(fetch = FetchType.LAZY)
     @JoinColumn(name = "RECRUITPLANID")
     private RecruitPlan recruitPlan;//所属招生计划
     
//     @OneToMany(fetch = FetchType.LAZY, mappedBy = "recruitMajor")
//	 @org.hibernate.annotations.Cascade(value={org.hibernate.annotations.CascadeType.DELETE_ORPHAN,org.hibernate.annotations.CascadeType.ALL})
//	 @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
//	 @OrderBy("showOrder ASC")
//	 @Where(clause="isDeleted=0")
//     private Set<ExamSubject> examSubject = new HashSet<ExamSubject>(0);//招生计划专业：该专业入学考试科目 1:n
     
     /**3.1.4 新增教学模式字段*/
     @Column(name="TEACHINGTYPE")
	 private String teachingType;

     /**3.1.8 成教版调整*/
     @Column(name="RECRUITMAJORCODE")
     private String recruitMajorCode;//招生专业编码
     
     @OneToOne(optional = true, cascade = {CascadeType.MERGE,CascadeType.PERSIST}, fetch = FetchType.LAZY)  
	 @JoinColumn(name = "BRSCHOOLID") 
     private OrgUnit brSchool;//校外学习中心
     
     @Transient
 	 private String brSchoolid;
     
     @Column(name="MAJORATTRIBUTE")
     private String majorAttribute;//专业属性
     
     @Column(name="MAJORCATOGERY")
     private String majorCatogery;//招生类别
     
     // TODO:2.0会结合招生计划大改
     @Column(name="MAJORCODEFORSTUDYNO")
     private String majorCodeForStudyNo;//学号专业编码
     
     
	/**
	 * @return the majorAttribute
	 */
	public String getMajorAttribute() {
		return majorAttribute;
	}

	/**
	 * @param majorAttribute the majorAttribute to set
	 */
	public void setMajorAttribute(String majorAttribute) {
		this.majorAttribute = majorAttribute;
	}

	/**
	 * @return the majorCatogery
	 */
	public String getMajorCatogery() {
		return majorCatogery;
	}

	/**
	 * @param majorCatogery the majorCatogery to set
	 */
	public void setMajorCatogery(String majorCatogery) {
		this.majorCatogery = majorCatogery;
	}

	/**
	 * @return the recruitMajorCode
	 */
	public String getRecruitMajorCode() {
		return recruitMajorCode;
	}

	/**
	 * @param recruitMajorCode the recruitMajorCode to set
	 */
	public void setRecruitMajorCode(String recruitMajorCode) {
		this.recruitMajorCode = recruitMajorCode;
	}

	/**
	 * @return the brSchool
	 */
	public OrgUnit getBrSchool() {
		return brSchool;
	}

	/**
	 * @param brSchool the brSchool to set
	 */
	public void setBrSchool(OrgUnit brSchool) {
		this.brSchool = brSchool;
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

	

	public Classic getClassic() {
		return classic;
	}

	public void setClassic(Classic classic) {
		this.classic = classic;
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

	public Long getLimitNum() {
		return limitNum;
	}

	public void setLimitNum(Long limitNum) {
		this.limitNum = limitNum;
	}

	public Long getLowerNum() {
		return lowerNum;
	}

	public void setLowerNum(Long lowerNum) {
		this.lowerNum = lowerNum;
	}

	public Major getMajor() {
		return major;
	}

	public void setMajor(Major major) {
		this.major = major;
	}

//	public Long getMatriculateLine() {
//		return matriculateLine;
//	}
//
//	public void setMatriculateLine(Long matriculateLine) {
//		this.matriculateLine = matriculateLine;
//	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getRecruitMajorName() {
		return recruitMajorName;
	}

	public void setRecruitMajorName(String recruitMajorName) {
		this.recruitMajorName = recruitMajorName;
	}

	public RecruitPlan getRecruitPlan() {
		return recruitPlan;
	}

	public void setRecruitPlan(RecruitPlan recruitPlan) {
		this.recruitPlan = recruitPlan;
	}

	public Long getShowOrder() {
		return showOrder;
	}

	public void setShowOrder(Long showOrder) {
		this.showOrder = showOrder;
	}

	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public Double getStudyperiod() {
		return studyperiod;
	}

	public void setStudyperiod(Double studyperiod) {
		this.studyperiod = studyperiod;
	}

	public Double getTuitionFee() {
		return tuitionFee;
	}

	public void setTuitionFee(Double tuitionFee) {
		this.tuitionFee = tuitionFee;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getUpdateMan() {
		return updateMan;
	}

	public void setUpdateMan(String updateMan) {
		this.updateMan = updateMan;
	}

	public String getUpdateManId() {
		return updateManId;
	}

	public void setUpdateManId(String updateManId) {
		this.updateManId = updateManId;
	}
    @Override
    public String getResourceid() {
    	return super.getResourceid();
    }

//	public String getMajorCodeForStudyNo() {
//		return majorCodeForStudyNo;
//	}
//
//	public void setMajorCodeForStudyNo(String majorCodeForStudyNo) {
//		this.majorCodeForStudyNo = majorCodeForStudyNo;
//	}
    
    
}