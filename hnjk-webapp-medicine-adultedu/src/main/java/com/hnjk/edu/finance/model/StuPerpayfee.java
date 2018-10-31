package com.hnjk.edu.finance.model;

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
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.edu.roll.model.StudentInfo;

/**
 * 学生预交费用表
 * <code>StuPerpayfee</code>
 * <p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-4-19 下午07:15:58
 * @see
 * @version 1.0
 */
@Entity
@Table(name = "EDU_ROLL_STUPAYDETAIL")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Deprecated
public class StuPerpayfee extends BaseModel implements Serializable {

	private static final long serialVersionUID = -6465618973284293401L;

	@OneToOne(optional = false, cascade = { CascadeType.MERGE,
			CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDENTINFOID")
	private StudentInfo studentInfo;// 学生学籍信息

	@Column(name = "STUDYNO")
	private String studyNo;// 学生学号

	@Column(name = "PAYABLEFEE", scale = 2)
	private Double payableFee;// 应缴金额

	@Column(name = "PAYFEESCALE")
	private Integer payFeeScale;// 缴费期数

	@Column(name = "PAYEDFEE")
	private Double payedFee;// 已缴费用金额

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "GATHERINGDATE")
	private Date gatheringDate;// 缴费日期

	@Column(name = "PAYEDNO")
	private String payedNo;// 缴费单号

	@Column(name = "PAYEDYEAR")
	private Integer payedYear;// 缴费年度

	@Column(name = "PAYEDTERM")
	private String payedTerm;// 缴费所在学期 上学期，下学期

	/**3.1.6 新增学生缴费标准*/
	@OneToOne(optional = false, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "FEERULEID")
	private StudentFeeRule studentFeeRule;//学生缴费标准
	
	@Transient
	private String emptyStr;
	
	@Transient
	private String stuLimite = "2.5";

	
	
	/**
	 * @return the studentFeeRule
	 */
	public StudentFeeRule getStudentFeeRule() {
		return studentFeeRule;
	}

	/**
	 * @param studentFeeRule the studentFeeRule to set
	 */
	public void setStudentFeeRule(StudentFeeRule studentFeeRule) {
		this.studentFeeRule = studentFeeRule;
	}

	public Date getGatheringDate() {
		return gatheringDate;
	}

	public void setGatheringDate(Date gatheringDate) {
		this.gatheringDate = gatheringDate;
	}

	public Double getPayableFee() {
		return payableFee;
	}

	public void setPayableFee(Double payableFee) {
		this.payableFee = payableFee;
	}

	public Double getPayedFee() {		
		return payedFee;
	}

	public void setPayedFee(Double payedFee) {
		this.payedFee = payedFee;
	}

	public String getPayedNo() {
		return payedNo;
	}

	public void setPayedNo(String payedNo) {
		this.payedNo = payedNo;
	}

	public String getPayedTerm() {
		return payedTerm;
	}

	public void setPayedTerm(String payedTerm) {
		this.payedTerm = payedTerm;
	}

	public Integer getPayedYear() {
		return payedYear;
	}

	public void setPayedYear(Integer payedYear) {
		this.payedYear = payedYear;
	}

	public Integer getPayFeeScale() {
		return payFeeScale;
	}

	public void setPayFeeScale(Integer payFeeScale) {
		this.payFeeScale = payFeeScale;
	}

	public StudentInfo getStudentInfo() {
		return studentInfo;
	}

	public void setStudentInfo(StudentInfo studentInfo) {
		this.studentInfo = studentInfo;
	}

	public String getStudyNo() {
		return studyNo;
	}

	public void setStudyNo(String studyNo) {
		this.studyNo = studyNo;
	}

	public String getEmptyStr() {
		return emptyStr;
	}

	public void setEmptyStr(String emptyStr) {
		this.emptyStr = emptyStr;
	}

	public String getStuLimite() {
		return stuLimite;
	}

	public void setStuLimite(String stuLimite) {
		this.stuLimite = stuLimite;
	}

}