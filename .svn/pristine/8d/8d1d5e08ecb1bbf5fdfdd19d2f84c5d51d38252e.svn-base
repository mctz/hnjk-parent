package com.hnjk.edu.finance.vo;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 收费汇总表vo
 * @author Administrator
 *
 */
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class ChargeSummaryVo implements Serializable {

	private String paymentMethod;//缴费方式
	private int totalNum;//总笔数
	private int stuCount ;//总人数
	private BigDecimal undergraduateFee;//本科学费
	private BigDecimal educationFee;//专科学费
	private BigDecimal totalFee;//合计学费
	private String accommodationFee;//住宿费
	private String materialFee;//教材费
	private String insuranceFee;//保险费
	private String beginReceiptNum;//开始票据号
	private String endReceiptNum;//结束票据号
	private Date beginDate;//开始时间
	private Date endDate;//结束时间
	

	public BigDecimal getUndergraduateFee() {
		if(undergraduateFee==null){
			return BigDecimal.valueOf(0);
		}
		return undergraduateFee;
	}

	public BigDecimal getEducationFee() {
		if(educationFee==null){
			return BigDecimal.valueOf(0);
		}
		return educationFee;
	}

	public String getBeginReceiptNum() {
		if(beginReceiptNum==null){
			return "";
		}
		return beginReceiptNum;
	}
	public void setBeginReceiptNum(String beginReceiptNum) {
		this.beginReceiptNum = beginReceiptNum;
	}
	public String getEndReceiptNum() {
		if(endReceiptNum==null){
			return "";
		}
		return endReceiptNum;
	}
}
