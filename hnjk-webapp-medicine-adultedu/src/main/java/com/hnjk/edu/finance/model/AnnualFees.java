package com.hnjk.edu.finance.model;

import com.hnjk.core.annotation.Historizable;
import com.hnjk.core.rao.dao.listener.OperationType;
import com.hnjk.edu.basedata.model.YearInfo;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.platform.system.model.BaseLogHistoryModel;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/*
 * 学生年度缴费记录
 */
@Entity
@Table(name = "EDU_FEE_ANNUALFEES")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Historizable(logable=true,value={OperationType.UPDATE})
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class AnnualFees extends BaseLogHistoryModel implements Serializable {

	private static final long serialVersionUID = -8348840895284786737L;

	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDENTID")
	private StudentInfo studentInfo;//学生
	
	@Column(name="STUDYNO")
	private String studyNo;//学号
	
	@Column(name="CHARGEYEAR")
	private Integer chargeYear;//缴费学年
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "YEARID")
	private YearInfo yearInfo;//所属缴费年度
	
	@Column(name="RECPAYFEE",scale=2)
	private Double recpayFee;//应缴金额
	
	@Column(name="FACEPAYFEE",scale=2)
	private Double facepayFee;//实缴金额
	
	@Column(name="DERATEFEE",scale=2)
	private Double derateFee;//减免金额
	
	@Column(name="RETURNPREMIUMFEE",scale=2)
	private Double returnPremiumFee;//退费金额
	
	@Column(name="CHARGESTATUS")
	private Integer chargeStatus;//缴费状态

	@Column(name="MEMO")
	private String memo;//备注
	
	@Transient
	private Double unpaidFee;// 欠费金额

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
	
	//20180731添加收费项、补交金额字段
	@Column(name="CHARGINGITEMS")
	private String chargingItems;// 收费项，字典表CodeChargingItems
	
	@Column(name="PAYAMOUNT",scale=2)
	private Double payAmount;// 补交金额

}
