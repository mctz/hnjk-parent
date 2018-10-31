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

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.hnjk.core.annotation.Historizable;
import com.hnjk.core.rao.dao.listener.OperationType;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.Classic;
import com.hnjk.edu.basedata.model.Grade;
import com.hnjk.edu.basedata.model.Major;
import com.hnjk.edu.basedata.model.YearInfo;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.platform.system.model.BaseLogHistoryModel;
import com.hnjk.security.model.OrgUnit;

//学生缴费记录
@Entity
@Table(name = "EDU_FEE_STUFEE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Historizable(logable=true,value={OperationType.UPDATE})
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class StudentPayment extends BaseLogHistoryModel implements Serializable {
	
	/** TODO: 新的缴费流程用不到 **/
	@Deprecated
	@Column(name="STUDYNO",nullable=false)
	private String studyNo;//学号
	
	/** TODO: 新的缴费流程用不到 **/
	@Deprecated
	@Column(name="CHARGEYEAR")
	private Integer chargeYear;//缴费自然年
	
	@Column(name="CHARGETERM")
	private Integer chargeTerm;//缴费期数
	
	@Column(name="RECPAYFEE",scale=2)
	private Double recpayFee;//应缴金额
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "YEARID")
	private YearInfo yearInfo;//所属缴费年度
	
	/** TODO: 新的缴费流程用不到 **/
	@Deprecated
	@Column(name="TERM")
	private String term;//所属缴费学期
	
	/** TODO: 新的缴费流程用不到 **/
	@Deprecated
	@Column(name="CHARGEENDDATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date chargeEndDate;//缴费截止日
	
	@Column(name="ISDEFER")
	private String isDefer = Constants.BOOLEAN_NO;//是否暂缓
	
	@Column(name="BEFORETOTALFEE",scale=2)
	private Double beforeTotalFee;//调整前总金额
	
	@Column(name="DEFERENDDATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date deferEndDate;//暂缓截止日
	
	@Column(name="CHARGESTATUS")
	private String chargeStatus = "0";//缴费状态,字典值,0-未缴费,1-已缴费,-1-欠费
	
	@Column(name="MEMO")
	private String memo;//备注
	
	@Column(name="DERATEFEE",scale=2)
	private Double derateFee;//减免费用
	
	@Column(name="DERETEREASON")
	private String dereteReason;//减免理由
	
	/*~~~~~~~~~~~从报名信息表中冗余的字段 ，用于条件查询~~~~~~~~~~~*/
	
	/** TODO: 新的缴费流程用不到 **/
	@Deprecated
	@Column(name="NAME")
	private String name;//学生姓名
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST ,CascadeType.REFRESH}, fetch = FetchType.LAZY)
	@JoinColumn(name = "GRADEID")
	private Grade grade;//年级
	
	@Column(name="TEACHINGTYPE")
	private String teachingType;//办学模式
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST ,CascadeType.REFRESH}, fetch = FetchType.LAZY)
	@JoinColumn(name = "BRANCHSCHOOLID")
	private OrgUnit branchSchool;//学习中心
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST ,CascadeType.REFRESH}, fetch = FetchType.LAZY)
	@JoinColumn(name = "CLASSICID")
	private Classic classic;//层次
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST ,CascadeType.REFRESH}, fetch = FetchType.LAZY)
	@JoinColumn(name = "MAJORID")
	private Major major;//专业
	
	
	/*~~~~~~~以下项需要同步更新~~~~*/
	@Column(name="FACEPAYFEE")
	private Double facepayFee;//实缴金额
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDENTID")
	private StudentInfo studentInfo;//学籍
	
	@Column(name="CHARGETIME")
	private Date chargeTime;// 生成缴费记录日期
	
	@Column(name="ISMULTIDEFER",length=1)
	private String isMultiDefer = Constants.BOOLEAN_NO;//是否多次缓缴
	
	@Column(name="RETURNPREMIUMFEE")
	private Double returnPremiumFee;//退费金额
	
	@Transient
	private Double unpaidFee;// 欠费金额

	@Column(name="STUDYNOTYPE")
	private String studyNoType;// 学号类型（1：准考证号；2：身份证号）作为联通对接的学号


	public Double getUnpaidFee() {
		if(this.recpayFee==null) {
			unpaidFee =0d;
		}else if(this.facepayFee==null){
			unpaidFee = this.recpayFee - 0;
		}else {
			unpaidFee = this.recpayFee - this.facepayFee;
		}
		return unpaidFee<0?0:unpaidFee;
	}
	
	@Column(name="PAYAMOUNT",scale=2)
	private Double payAmount;// 补交金额
	
}
