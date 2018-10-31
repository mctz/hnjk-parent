package com.hnjk.edu.finance.model;

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

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.edu.basedata.model.YearInfo;
import com.hnjk.edu.roll.model.StuChangeInfo;
import com.hnjk.edu.roll.model.StudentInfo;

/** 
 * 预退费补交订单
 * 
 * @author Zik, 广东学苑教育发展有限公司
 * @since 2018年7月31日 下午3:42:05 
 * 
 */
@Entity
@Table(name = "EDU_FEE_REFUNDBACK")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class Refundback extends BaseModel {

	private static final long serialVersionUID = -5514820772886839786L;
	// 处理状态
	public static final String PROCESSSTATUS_PENDING = "pending";// 未处理
	public static final String PROCESSSTATUS_HANDLED = "handled";// 已处理
	
	
	@Column(name="STUDYNO")
	private String studyNo;// 学号
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDENTINFOID")
	private StudentInfo studentInfo;// 学籍
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "YEARID")
	private YearInfo yearInfo;// 年度
	
	@Column(name="PROCESSTYPE")
	private String processType;// 处理类型
	
	@Column(name="MONEY",scale=2)
	private Double money;// 金额
	
	@Column(name="PAYMENTMETHOD")
	private String paymentMethod;// 付款方式
	
	@Column(name="PROCESSSTATUS")
	private String processStatus;// 处理状态
	
	@Column(name="CHARGINGITEMS")
	private String chargingItems;// 收费项
	
	@Column(name="CREATEDATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;// 创建时间
	
	@Column(name="ORDERNO")
	private String orderNo;// 订单号
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "STUCHANGEID")
	private StuChangeInfo changeInfo;

}
