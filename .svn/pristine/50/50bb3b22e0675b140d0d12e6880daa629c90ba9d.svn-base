package com.hnjk.edu.finance.model;

import com.hnjk.core.annotation.Historizable;
import com.hnjk.core.rao.dao.listener.OperationType;
import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.Grade;
import com.hnjk.edu.basedata.model.Major;
import com.hnjk.edu.basedata.model.YearInfo;
import com.hnjk.edu.recruit.model.EnrolleeInfo;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.security.model.OrgUnit;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

/** 
 * 学生缴费临时表（供录取新生使用）
 * @author Zik, 广东学苑教育发展有限公司
 * @since Nov 8, 2016 6:44:44 PM 
 * 
 */
@Entity
@Table(name="EDU_FEE_TEMPSTUDENTFEE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Historizable(logable=true,value={OperationType.UPDATE})
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class TempStudentFee extends BaseModel {
	
	
	private static final long serialVersionUID = -7975698608323281066L;
	
	public static final String HANDLESTATUS_TOAUDIT = "toAudit";
	public static final String HANDLESTATUS_PASS = "pass";
	public static final String HANDLESTATUS_NOPASS = "noPass";
	public static final String HANDLESTATUS_NONEED = "noNeed";
	
	@OneToOne(optional = false, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "GRADEID")
	private Grade grade;// 年级
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "ENROLLEEINFOID")
	private EnrolleeInfo enrolleeInfo;// 年级
	
	@OneToOne(optional = false, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "UNITID")
	private OrgUnit unit;// 教学点
	
	@Column(name = "EXAMCERTIFICATENO")
	private String examCertificateNo;// 长号即准考证号或考生号
	
	@Column(name = "STUDENTNAME")
	private String studentName;// 学生名字
	
	@Column(name = "AMOUNT", scale = 2)
	private Double amount;// 应缴金额
	
	@Column(name = "REMARK")
	private String remark;// 备注
	
	@Column(name = "ISUPLOADED")
	private String isUploaded="N";// 是否已同步到通联系统
	
	@Column(name = "EDUOREDERNO")
	private String eduOrderNo;// 教育系统订单号    20171027：对应公众号缴费中的银行流水号，唯一
	
	@Column(name = "BATCHNO")
	private String batchNo;// 学校内部批次编号
	
	@Column(name = "PAYSTATUS")
	private String payStatus=Constants.FEE_PAYSTATUS_UNPAY;// 支付状态，1：未付款, 2：已付款
	
	@Column(name = "SCHOOLORDERNO")
	private String schoolOrderNo;// 学校订单号
	
	@Column(name = "HASSTUDENTINFO")
	private String hasStudentInfo=Constants.BOOLEAN_NO;// 是否有学籍
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDENTINFOID")
	private StudentInfo studentInfo;// 学籍信息
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "MAJORID")
	private Major major;// 专业
	
	@Column(name="CERTNUM")
	private String certNum;// 身份证号（18级以及部分15、16级教学点）作为通联支付系统对接的学号
	
	@Column(name="STUDYNOTYPE")
	private String studyNoType;// 学号类型，1：准考证号；2：身份证号（通联支付系统对接的学号）
	//20171030 增加
	@Column(name="ISRECONCILIATION")
	private String isReconciliation=Constants.BOOLEAN_NO;// 是否对账  
	
	// 订单处理状态，对应字典表CodeHandlePaymentStatus,用于线下缴费订单的导入
	@Column(name="HANDLESTATUS")
	private String handleStatus;
	
	//20180731添加收费项、是否开单位发票、单位名称字段
	@Column(name="CHARGINGITEMS")
	private String chargingItems;// 收费项，字典表CodeChargingItems
	
	@Column(name="ISINVOICING")
	private String isInvoicing;// 是否开单位发票
	
	@Column(name="INVOICETITLE")
	private String invoiceTitle;// 单位名称
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "YEARID")
	private YearInfo yearInfo;// 年度
	
	@Column(name="PAYPASSWORD")
	private String payPassword;// 广外支付码
}
