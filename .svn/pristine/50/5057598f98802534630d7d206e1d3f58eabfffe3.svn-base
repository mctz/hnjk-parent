package com.hnjk.edu.finance.model;

import java.io.Serializable;
import java.math.BigDecimal;
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

import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.edu.roll.model.StudentInfo;

/**
 * 学生缴费明细
 * <code>StudentPaymentDetails</code>
 * <p>
 * 
 * @author： zik, 广东学苑教育发展有限公司
 * @since： 2015-11-16 16:10
 * @see
 * @version 1.0
 */
@Entity
@Table(name = "EDU_FEE_STUDENGPAYMENTDETAILS")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class StudentPaymentDetails extends BaseModel implements Serializable {

	private static final long serialVersionUID = -6478516357371304038L;
	
	public static final Integer PAYTYPE_UPDATE = 1;//缴费类型: 直接更新
	public static final Integer PAYTYPE_OVERLAY = 2;// 缴费类型:叠加

	@OneToOne(optional = false, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDENTINFOID")
	private StudentInfo studentInfo;// 学生学籍信息

	@Column(name = "PAYTYPE")
	private Integer payType;// 缴费类型
	
	@Column(name = "SHOULDPAYAMOUNT", scale = 2)
	private Double shouldPayAmount;// 应缴金额

	@Column(name = "PAIDAMOUNT", scale = 2)
	private Double paidAmount;// 已缴金额

	@Column(name = "PAYAMOUNT", scale = 2)
	private Double payAmount;// 缴费金额

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "OPERATEDATE")
	private Date operateDate;// 操作日期

	@Column(name = "OPERATORNAME")
	private String operatorName;// 操作人名字

	@Column(name = "OPERATORID")
	private String operatorId;// 操作人ID
	
	@Column(name = "POSSERIALNUMBER")
	private String posSerialNumber;// pos流水号
	
	@Column(name = "RECEIPTNUMBER")
	private String receiptNumber;// 收据号
	
	@Column(name = "CARRTERMNUM")
	private String carrTermNum;// pos终端号
	
	@Column(name = "CARRCARDNO")
	private String carrCardNo;// 银行卡号
	
	@Column(name = "PAYMENTMETHOD")
	private String paymentMethod = "1";// 付款方式,默认值为1:现金
	
	@Column(name = "CHECKPAYABLE")
	private String checkPayable;// 票据抬头
	
	@Column(name = "MEMO")
	private String memo;// 备注
	
	@Column(name = "EDUOREDERNO")
	private String eduOrederNo;// 教育系统订单号
	
	@Column(name = "DRAWER")
	private String drawer;// 开票人
	
	@Column(name = "CARDTYPE")
	private String cardType;// 卡类型
	
	@Column(name = "CHARGEMONEY", scale = 2)
	private Double chargeMoney;// 手续费
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PRINTDATE")
	private Date printDate;// 打印时间
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATEDATE")
	private Date createDate;// 创建时间
	
	@Transient
	private BigDecimal printPayAmount;//打印收据-付款
	
	@Column(name="YEAR")
	private String year;//学年
	
	@Column(name="PAYNO")
	private String payNo;// 支付流水号
	
	@Column(name="CLASSNAME")
	private String className;// 班别
	
	//20180731添加收费项、是否开单位发票、单位名称字段
	@Column(name="CHARGINGITEMS")
	private String chargingItems;// 收费项，字典表 CodeChargingItems
	
	@Column(name="ISINVOICING")
	private String isInvoicing;// 是否开单位发票
	
	@Column(name="INVOICETITLE")
	private String invoiceTitle;// 单位名称
	
}