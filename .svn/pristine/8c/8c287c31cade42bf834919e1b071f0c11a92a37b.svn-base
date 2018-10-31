package com.hnjk.edu.recruit.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.core.support.context.Constants;

/**
 * 校外学习中心招生专业
 * <code>BranchSchoolMajor</code>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-3-15 下午01:43:27
 * @see 
 * @version 1.0 * 
 */
@Entity
@Table(name="EDU_RECRUIT_BRSCHMAJOR")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class BranchSchoolMajor  extends BaseModel {

	 private static final long serialVersionUID = 9119856721866495668L;
	      
	 @OneToOne(optional = false, cascade = {CascadeType.MERGE,CascadeType.PERSIST}, fetch = FetchType.LAZY)  
	 @JoinColumn(name = "RECRUITMAJORID") 
     private RecruitMajor recruitMajor;//开设专业
  
	 @Column(name="TUITIONFEE")
     private Double tuitionFee;//学费

	 @Column(name="LIMITNUM")
	 private Long limitNum;//上限人数
	 
	 @Column(name="LOWERNUM")
	 private Long lowerNum;//下限人数
	 
	 @ManyToOne(fetch = FetchType.LAZY)
     @JoinColumn(name = "BRSCHPLANID")
     private BranchSchoolPlan branchSchoolPlan;//校外学习中心批次ID	 
	 
	 /**3.1.9 新增是否审核通过功能*/
	 @Column(name="ISPASSED",length=1)
	 private String isPassed = Constants.BOOLEAN_WAIT;//是否审核通过
	 
	 
	 
//	 @Column(name="SHOWORDER")
//     private Long showOrder;//排序号    
//	 
//
//	public Long getShowOrder() {
//		return showOrder;
//	}
//
//	public void setShowOrder(Long showOrder) {
//		this.showOrder = showOrder;
//	}
 

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

	public BranchSchoolPlan getBranchSchoolPlan() {
		return branchSchoolPlan;
	}

	public void setBranchSchoolPlan(BranchSchoolPlan branchSchoolPlan) {
		this.branchSchoolPlan = branchSchoolPlan;
	}

	public RecruitMajor getRecruitMajor() {
		return recruitMajor;
	}

	public void setRecruitMajor(RecruitMajor recruitMajor) {
		this.recruitMajor = recruitMajor;
	}


	public Double getTuitionFee() {
		return tuitionFee;
	}

	public void setTuitionFee(Double tuitionFee) {
		this.tuitionFee = tuitionFee;
	}
	 
	 
    

}