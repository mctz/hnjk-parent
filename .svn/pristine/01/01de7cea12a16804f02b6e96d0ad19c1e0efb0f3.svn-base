package com.hnjk.edu.recruit.model;

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

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Where;

import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.Grade;
import com.hnjk.edu.basedata.model.YearInfo;

/**
 * <code>RecruitPlan</code>招生计划.<p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-3-15 上午10:10:12
 * @see 
 * @version 1.0
 */
@Entity
@Table(name="EDU_RECRUIT_RECRUITPLAN")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class RecruitPlan  extends BaseModel {

	private static final long serialVersionUID = -5036207703001091557L;
	
	 @Column(name="RECRUITPLANNAME",nullable=false)
     private String recruitPlanname;//招生计划名称(批次)
	 
	 @Temporal(TemporalType.DATE)
	 @Column(name="STARTDATE",nullable=false)
     private Date startDate;//开始日期
	 
	 @Temporal(TemporalType.DATE)
	 @Column(name="ENDDATE",nullable=false)
     private Date endDate;//结束日期
	 
	 @Temporal(TemporalType.DATE)
	 @Column(name="PUBLISHDATE")
     private Date publishDate;//发布日期
	 
	 @Temporal(TemporalType.DATE)
	 @Column(name="ENROLLSTARTTIME")
	 private Date enrollStartTime;//录取查询开放时间
	 
	 @Temporal(TemporalType.DATE)
	 @Column(name="ENROLLENDTIME")
	 private Date enrollEndTime;//录取查询截止时间
	 
	 @Temporal(TemporalType.DATE)
	 @Column(name="REPORTtIME")
	 private Date reportTime;   //报到开始时间
	 
	 @Temporal(TemporalType.DATE)
	 @Column(name="ENDTIME")
	 private Date endTime;   //报到结束时间
	 


	@Column(name="TERM")		 
     private String term;//学期
	 
	 /**3.0.3 去掉该字段*/
//	 @Column(name="FIXEDSCORE",nullable=false)
//     private Double fixedScore;//固定分数，额外加分
	 
	 @Column(name="WEBREGMEMO")
     private String webregMemo;//网上报名注意事项
	 
	 @Column(name="FILLINMAN")
     private String fillinMan;//填写人
	 
	 @Column(name="FILLINMANID")
     private String fillinManId;//填写人ID
	 
	 @Temporal(TemporalType.TIMESTAMP)
	 @Column(name="FILLINDATE")
     private Date fillinDate;//填写日期
	 
	 @Column(name="UPDATEMAN")
     private String updateMan;//更新人
	 
	 @Column(name="UPDATEMANID")
     private String updateManId;//更新人ID
	 
	 @Temporal(TemporalType.TIMESTAMP)
	 @Column(name="UPDATEDATE")
     private Date updateDate;//更新日期
		
	 @OneToOne(optional = true, cascade = {CascadeType.MERGE,CascadeType.PERSIST}, fetch = FetchType.LAZY)  
	 @JoinColumn(name = "YEARID") 
     private YearInfo yearInfo;//所属年度
	 
	 @OneToMany(fetch = FetchType.LAZY, mappedBy = "recruitPlan")
	 @org.hibernate.annotations.Cascade(value={org.hibernate.annotations.CascadeType.DELETE_ORPHAN,org.hibernate.annotations.CascadeType.ALL})
	 @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	 @OrderBy("classic ASC")
	 @Where(clause="isDeleted=0")
	 private Set<RecruitMajor> recruitMajor = new HashSet<RecruitMajor>(0);//招生计划：招生专业 1:n
	 
	 /** 3.0.3 调整与增加的字段
	  *  3.1.4 将分数线、系数拆分为子表
	  *  */
//	 @Column(name="MATRICULATELINE")
//	 private Long matriculateLine;//录取学分线，不在分专业和地区，统一分数线
	 
//	 @Column(name="COEFFICIENT",scale=2)
//	 private Double coefficient = 1.0;//系数X 
	 
	 
	 /**3.0.4调整字段*/
	 @OneToOne(optional = false, cascade = {CascadeType.MERGE,CascadeType.PERSIST}, fetch = FetchType.LAZY)  
	 @JoinColumn(name = "GRADEID") 
	 private Grade grade;
	 
	 @Column(name="ISPUBLISHED")
	 private String isPublished = Constants.BOOLEAN_YES;//是否开放状态 默认情况
	 
	 /**3.0.8新增字段*/
	 @Column(name="ISOPENSEARCH")
	 private String isOpenSearch = Constants.BOOLEAN_NO;//是否开放录取查询
	 
	 /**3.1.1 新增校外学习中心*/
	@Column(name="BRSCHOOLIDS")
	 private String brSchoolIds;//校外学习中心ID，使用","隔开
	 
	 /**3.1.3新增教学模式*/
	 @Column(name="TEACHINGTYPE")
	 private String teachingType;//教学模式 字典值，可以多个
	 
	 /**3.1.4 关联到招生计划-分数线表 1:n*/
	// @OneToMany(fetch = FetchType.LAZY, mappedBy = "recruitPlan")
	// @org.hibernate.annotations.Cascade(value={org.hibernate.annotations.CascadeType.DELETE_ORPHAN,org.hibernate.annotations.CascadeType.ALL})
	// @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)	
	 //@Where(clause="isDeleted=0")
	// private Set<RecruitPlanLine> recruitPlanLine = new LinkedHashSet<RecruitPlanLine>(0);//招生计划：分数线 1:n
	 
	 @Column(name="ISSPECIAL",length=1)
	 private String isSpecial = Constants.BOOLEAN_NO;//是否特批,默认情况下非特批

	 /**3.1.8新增学习中心申报截止日期*/
	 @Column(name="BRSCHOOLAPPLYCLOSETIME")
	 @Temporal(TemporalType.TIMESTAMP)
	 private Date brSchoolApplyCloseTime;//申报截止日期
	 
	 
	 
	/**
	 * @return the brSchoolApplyCloseTime
	 */
	public Date getBrSchoolApplyCloseTime() {
		return brSchoolApplyCloseTime;
	}

	/**
	 * @param brSchoolApplyCloseTime the brSchoolApplyCloseTime to set
	 */
	public void setBrSchoolApplyCloseTime(Date brSchoolApplyCloseTime) {
		this.brSchoolApplyCloseTime = brSchoolApplyCloseTime;
	}

	
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	/**
	 * @return the isSpecial
	 */
	public String getIsSpecial() {
		return isSpecial;
	}

	/**
	 * @param isSpecial the isSpecial to set
	 */
	public void setIsSpecial(String isSpecial) {
		this.isSpecial = isSpecial;
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

	 public Date getReportTime() {
			return reportTime;
		}

		public void setReportTime(Date reportTime) {
			this.reportTime = reportTime;
		}

	/**
	 * @return the brSchoolIds
	 */
	public String getBrSchoolIds() {
		return brSchoolIds;
	}

	/**
	 * @param brSchoolIds the brSchoolIds to set
	 */
	public void setBrSchoolIds(String brSchoolIds) {
		this.brSchoolIds = brSchoolIds;
	}

	public String getIsOpenSearch() {
		return isOpenSearch;
	}

	public void setIsOpenSearch(String isOpenSearch) {
		this.isOpenSearch = isOpenSearch;
	}

	public String getIsPublished() {
		return isPublished;
	}

	public void setIsPublished(String isPublished) {
		this.isPublished = isPublished;
	}

	public Grade getGrade() {
		return grade;
	}

	public void setGrade(Grade grade) {
		this.grade = grade;
	}



	public Set<RecruitMajor> getRecruitMajor() {
		return recruitMajor;
	}
	
	public void addRecruitMajor(RecruitMajor recruitMajor){
		getRecruitMajor().add(recruitMajor);
	}
		
	public void setRecruitMajor(Set<RecruitMajor> recruitMajor) {
		this.recruitMajor = recruitMajor;
	}


	public Date getEndDate() {
		return endDate;
	}


	public void setEndDate(Date endDate) {
		this.endDate = endDate;
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


//	public Double getFixedScore() {
//		return fixedScore;
//	}
//
//
//	public void setFixedScore(Double fixedScore) {
//		this.fixedScore = fixedScore;
//	}


	public Date getPublishDate() {
		return publishDate;
	}


	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}


	public String getRecruitPlanname() {
		return recruitPlanname;
	}


	public void setRecruitPlanname(String recruitPlanname) {
		this.recruitPlanname = recruitPlanname;
	}


	public Date getStartDate() {
		return startDate;
	}


	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}


	public String getTerm() {
		return term;
	}


	public void setTerm(String term) {
		this.term = term;
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


	public String getWebregMemo() {
		return webregMemo;
	}


	public void setWebregMemo(String webregMemo) {
		this.webregMemo = webregMemo;
	}


	public YearInfo getYearInfo() {
		return yearInfo;
	}


	public void setYearInfo(YearInfo yearInfo) {
		this.yearInfo = yearInfo;
	}

	public Date getEnrollEndTime() {
		return enrollEndTime;
	}

	public void setEnrollEndTime(Date enrollEndTime) {
		this.enrollEndTime = enrollEndTime;
	}

	public Date getEnrollStartTime() {
		return enrollStartTime;
	}

	public void setEnrollStartTime(Date enrollStartTime) {
		this.enrollStartTime = enrollStartTime;
	}
	 
	@Override
	public String getResourceid() {
		return super.getResourceid();
	} 
   
}