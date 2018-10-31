package com.hnjk.edu.finance.model;

import com.hnjk.core.annotation.Historizable;
import com.hnjk.core.rao.dao.listener.OperationType;
import com.hnjk.edu.basedata.model.YearInfo;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.platform.system.model.BaseLogHistoryModel;
import com.hnjk.security.model.User;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Date;

/*
 * 学生退费记录
 */
@Entity
@Table(name = "EDU_FEE_RETURNPREMIUM")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Historizable(logable=true,value={OperationType.UPDATE})
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class ReturnPremium extends BaseLogHistoryModel implements Serializable {
	
	private static final long serialVersionUID = -1585174671339636837L;
	// 处理类型
	public static final String PROCESSTYPE_RETURNPREMIUM = "returnPremium";// 退费
	public static final String PROCESSTYPE_AFTERPAYMENT = "afterPayment";// 补交
	public static final String PROCESSTYPE_NONEEDDEAL = "noNeedDeal";// 无需处理

	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDENTINFOID")
	private StudentInfo studentInfo;//学生
	
	@Column(name="STUDYNO")
	private String studyNo;//学号
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "YEARID")
	private YearInfo yearInfo;//所属缴费年度
	
	@Column(name="RECPAYFEE",scale=2)
	private Double recpayFee;//应缴金额
	
	@Column(name="FACEPAYFEE")
	private Double facepayFee;//实缴金额
	
	@Column(name="RETURNPREMIUMFEE")
	private Double returnPremiumFee;//退费金额
	
	@Column(name="CREATEDATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;//创建时间
	
	@OneToOne(optional=false, cascade={CascadeType.MERGE, CascadeType.PERSIST}, fetch=FetchType.LAZY)
	@JoinColumn(name="OPERATORID")
	private User operator; // 操作人
	
	@Column(name="OPERATORNAME")
	private String operatorName;//操作人名称
	
	@Column(name="SHOWORDER")
	private Integer showOrder;//排序号
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PRINTDATE")
	private Date printDate;// 打印时间
	
	@Column(name = "RECEIPTNUMBER")
	private String receiptNumber;// 票据号
	
	@Column(name = "DRAWER")
	private String drawer;// 开票人
	
	@Column(name = "PAYMENTMETHOD")
	private String paymentMethod;// 缴费类型
	
	@Column(name = "CLASSNAME")
	private String className;
	
	// 20180731新增处理类型、补交金额字段
	@Column(name = "PROCESSTYPE")
	private String processType;// 处理类型,字典表CodeProcessType
	
	@Column(name="PAYAMOUNT",scale=2)
	private Double payAmount;// 补交金额
	
	@Column(name="ORDERNO")
	private String orderNo;// 订单号
	
	@Column(name="CHARGINGITEMS")
	private String chargingItems;// 收费项
	
}
