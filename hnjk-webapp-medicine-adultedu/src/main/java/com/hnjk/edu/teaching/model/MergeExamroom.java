package com.hnjk.edu.teaching.model;

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
import com.hnjk.edu.recruit.model.RecruitPlan;
import com.hnjk.security.model.OrgUnit;

/**
 * 考试考务-合并考场记录表
 * <code>MergeExamroom</code><p>
 * 
 * @author：广东学苑教育发展有限公司.
 * @since： 2011-8-17 下午02:46:04
 * @see 
 * @version 1.0
 */
@Entity
@Table(name="EDU_TEACH_MERGEROOM")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MergeExamroom extends BaseModel {

	private static final long serialVersionUID = 1284662494837134415L;

	@Column(name="EXAMTYPE",nullable=false,length=1)
	private Integer examType;//考试类型 1- 入学考试，2-期末考试
	
	@OneToOne(optional = true, cascade = {CascadeType.MERGE,CascadeType.PERSIST}, fetch = FetchType.LAZY)  
	@JoinColumn(name = "OUTBRSCHOOLID") 
	private OrgUnit outBrSchool;//转出的学习中心
	
	@OneToOne(optional = true, cascade = {CascadeType.MERGE,CascadeType.PERSIST}, fetch = FetchType.LAZY)  
	@JoinColumn(name = "INBRSCHOOLID")
	private OrgUnit inBrSchool;//转入的学习中心
	
	@OneToOne(optional = true, cascade = {CascadeType.MERGE,CascadeType.PERSIST}, fetch = FetchType.LAZY)  
	@JoinColumn(name = "EXAMSUBID")
	private ExamSub examSub;//期末考试批次
	
	@OneToOne(optional = true, cascade = {CascadeType.MERGE,CascadeType.PERSIST}, fetch = FetchType.LAZY)  
	@JoinColumn(name = "RECRUITPLANID")
	private RecruitPlan recruitPlan;//入学考试招生批次
	
//	@OneToOne(optional = true, cascade = {CascadeType.MERGE,CascadeType.PERSIST}, fetch = FetchType.LAZY)  
//	@JoinColumn(name = "EXAMPLANID")
//	private RecruitExamPlan recruitExamPlan;//考试场次

	/**
	 * @return the examType
	 */
	public Integer getExamType() {
		return examType;
	}

	/**
	 * @param examType the examType to set
	 */
	public void setExamType(Integer examType) {
		this.examType = examType;
	}

	/**
	 * @return the outBrSchool
	 */
	public OrgUnit getOutBrSchool() {
		return outBrSchool;
	}

	/**
	 * @param outBrSchool the outBrSchool to set
	 */
	public void setOutBrSchool(OrgUnit outBrSchool) {
		this.outBrSchool = outBrSchool;
	}

	/**
	 * @return the inBrSchool
	 */
	public OrgUnit getInBrSchool() {
		return inBrSchool;
	}

	/**
	 * @param inBrSchool the inBrSchool to set
	 */
	public void setInBrSchool(OrgUnit inBrSchool) {
		this.inBrSchool = inBrSchool;
	}

	/**
	 * @return the examSub
	 */
	public ExamSub getExamSub() {
		return examSub;
	}

	/**
	 * @param examSub the examSub to set
	 */
	public void setExamSub(ExamSub examSub) {
		this.examSub = examSub;
	}

	/**
	 * @return the recruitPlan
	 */
	public RecruitPlan getRecruitPlan() {
		return recruitPlan;
	}

	/**
	 * @param recruitPlan the recruitPlan to set
	 */
	public void setRecruitPlan(RecruitPlan recruitPlan) {
		this.recruitPlan = recruitPlan;
	}

//	/**
//	 * @return the recruitExamPlan
//	 */
//	public RecruitExamPlan getRecruitExamPlan() {
//		return recruitExamPlan;
//	}
//
//	/**
//	 * @param recruitExamPlan the recruitExamPlan to set
//	 */
//	public void setRecruitExamPlan(RecruitExamPlan recruitExamPlan) {
//		this.recruitExamPlan = recruitExamPlan;
//	}
	
	
}
