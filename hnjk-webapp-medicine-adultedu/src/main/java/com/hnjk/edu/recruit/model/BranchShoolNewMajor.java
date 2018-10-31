package com.hnjk.edu.recruit.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.Classic;

/**
 * 校外学习中心申请新专业表.与专业申报走一个流程.
 * <code>BranchShoolNewMajor</code><p>
 * 
 * @author：广东学苑教育发展有限公司.
 * @since： 2011-4-7 下午02:06:07
 * @see 
 * @version 1.0
 */
@Entity
@Table(name="EDU_RECRUIT_BRNEWMAJOR")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class BranchShoolNewMajor extends BaseModel{

	private static final long serialVersionUID = -2248564152286194098L;

	@OneToOne(optional = true, cascade = {CascadeType.MERGE,CascadeType.PERSIST}, fetch = FetchType.LAZY)  
	@JoinColumn(name = "CLASSICID") 
	private Classic classic;//层次
	
	@Column(name="SHAPE")
	private String shape;//形式
	
	@Column(name="MAJORNAME")
	private String majorName;//新专业名称，从国家专业库中选，字典中有
	
	@Column(name="DICRECT")
	private String dicrect;//专业方向
	
	@Column(name="MAJORCLASS")
	private String majorClass;//专业类别 取字典 majorClass
	
	@Column(name="SCOPE")
	private String scope;//招生范围
	
	@Column(name="ADDRESS")
	private String address;//办学地址
	
	@Column(name="TEACHINGTYPE")
	private String teachingType;//办学模式：取字典
	
	@Column(name="MEMO")
	private String memo;//备注
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BRSCHPLANID")
	private BranchSchoolPlan branchSchoolPlan;//关联校外学习中心招生批次

	
	/**3.0.7新增大类，专业类别*/
	@Column(name="BIGCATALOG")
	private String parentCatalog;
	
	@Column(name="CATALOG")
	private String childCatalog;
	
	@Column(name="BASEMAJORID")
	private String baseMajor;//基础专业库中的专业ID
	
	@Column(name="LIMITNUM")
	private Long limitNum;//指标数
	 
	@Column(name="LOWERNUM")
	private Long lowerNum;//下限人数
		
	@Column(name="ISINTORECRUITPLAN")
	private String isIntoRecruitPlan=Constants.BOOLEAN_NO;//是否已转入招生批次
	
	/**3.1.9 新增是否审核通过标示*/
	@Column(name="ISPASSED",length=1)
	private String isPassed = Constants.BOOLEAN_WAIT;
	
	/**新增学制*/
	 @Column(name="STUDYPERIOD",nullable=false)
     private Double studyperiod;//学制

	 @Column(name="MAJORDESCRIPT")
	 private String majorDescript;//专业介绍
	 
	 @Transient
	 private String classicid;
	 @Transient
	 private String classicName;
	 
	 
	/**
	 * @return the majorDescript
	 */
	public String getMajorDescript() {
		return majorDescript;
	}

	/**
	 * @param majorDescript the majorDescript to set
	 */
	public void setMajorDescript(String majorDescript) {
		this.majorDescript = majorDescript;
	}

	/**
	 * @return the studyperiod
	 */
	public Double getStudyperiod() {
		return studyperiod;
	}

	/**
	 * @param studyperiod the studyperiod to set
	 */
	public void setStudyperiod(Double studyperiod) {
		this.studyperiod = studyperiod;
	}

	/**
	 * @return the isPassed
	 */
	public String getIsPassed() {
		return isPassed;
	}

	/**
	 * @param isPassed the isPassed to set
	 */
	public void setIsPassed(String isPassed) {
		this.isPassed = isPassed;
	}

	public String getIsIntoRecruitPlan() {
		return isIntoRecruitPlan;
	}

	public void setIsIntoRecruitPlan(String isIntoRecruitPlan) {
		this.isIntoRecruitPlan = isIntoRecruitPlan;
	}

	public String getBaseMajor() {
		return baseMajor;
	}

	public void setBaseMajor(String baseMajor) {
		this.baseMajor = baseMajor;
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

	/**
	 * @return the parentCatalog
	 */
	public String getParentCatalog() {
		return parentCatalog;
	}

	/**
	 * @param parentCatalog the parentCatalog to set
	 */
	public void setParentCatalog(String parentCatalog) {
		this.parentCatalog = parentCatalog;
	}

	/**
	 * @return the childCatalog
	 */
	public String getChildCatalog() {
		return childCatalog;
	}

	/**
	 * @param childCatalog the childCatalog to set
	 */
	public void setChildCatalog(String childCatalog) {
		this.childCatalog = childCatalog;
	}

	/**
	 * @return the classic
	 */
	public Classic getClassic() {
		return classic;
	}

	/**
	 * @param classic the classic to set
	 */
	public void setClassic(Classic classic) {
		this.classic = classic;
	}

	/**
	 * @return the shape
	 */
	public String getShape() {
		return shape;
	}

	/**
	 * @param shape the shape to set
	 */
	public void setShape(String shape) {
		this.shape = shape;
	}

	/**
	 * @return the majorName
	 */
	public String getMajorName() {
		return majorName;
	}

	/**
	 * @param majorName the majorName to set
	 */
	public void setMajorName(String majorName) {
		this.majorName = majorName;
	}

	/**
	 * @return the dicrect
	 */
	public String getDicrect() {
		return dicrect;
	}

	/**
	 * @param dicrect the dicrect to set
	 */
	public void setDicrect(String dicrect) {
		this.dicrect = dicrect;
	}

	/**
	 * @return the majorClass
	 */
	public String getMajorClass() {
		return majorClass;
	}

	/**
	 * @param majorClass the majorClass to set
	 */
	public void setMajorClass(String majorClass) {
		this.majorClass = majorClass;
	}

	/**
	 * @return the scope
	 */
	public String getScope() {
		return scope;
	}

	/**
	 * @param scope the scope to set
	 */
	public void setScope(String scope) {
		this.scope = scope;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
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

	/**
	 * @return the branchSchoolPlan
	 */
	public BranchSchoolPlan getBranchSchoolPlan() {
		return branchSchoolPlan;
	}

	/**
	 * @param branchSchoolPlan the branchSchoolPlan to set
	 */
	public void setBranchSchoolPlan(BranchSchoolPlan branchSchoolPlan) {
		this.branchSchoolPlan = branchSchoolPlan;
	}

	public String getClassicid() {
		return classicid;
	}

	public void setClassicid(String classicid) {
		this.classicid = classicid;
	}

	public String getClassicName() {
		return classicName;
	}

	public void setClassicName(String classicName) {
		this.classicName = classicName;
	}
	
	
}
