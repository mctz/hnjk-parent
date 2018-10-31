package com.hnjk.edu.finance.vo;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Function : 学费类别详细信息-广外
 * <p>Author : msl
 * <p>Date   : 2018-08-21
 * <p>Description :
 */
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class FeeCommissionTypeInfoVo implements Serializable {
	private Double royaltyRate;//分成比例（非外语类）
	private BigDecimal royaltyRatePay;//分成金额

	private Double royaltyRate2;//外语类分成比例
	private BigDecimal royaltyRate2Pay;//外语类分成金额

	private Double reserveRatio;//预留分成比例
	//private BigDecimal reserveRatioPay;//预留分成金额
	//private BigDecimal shouldReturnPay;//应返学费总金额

	public BigDecimal getShouldReturnPay() {
		return BigDecimal.valueOf(royaltyRate).multiply(royaltyRatePay).add(BigDecimal.valueOf(royaltyRate2).multiply(royaltyRate2Pay)).divide(BigDecimal.valueOf(100)).setScale(2,BigDecimal.ROUND_HALF_UP);
	}

	public BigDecimal getReserveRatioPay() {
		return BigDecimal.valueOf(reserveRatio).multiply(getShouldReturnPay()).divide(BigDecimal.valueOf(100)).setScale(2,BigDecimal.ROUND_HALF_UP);
	}

	public BigDecimal getRoyaltyRatePay() {
		if (this.royaltyRatePay == null) {
			return BigDecimal.valueOf(0);
		}
		return this.royaltyRatePay;
	}

	public BigDecimal getRoyaltyRate2Pay() {
		if (this.royaltyRate2Pay == null) {
			return BigDecimal.valueOf(0);
		}
		return this.royaltyRate2Pay;
	}

}
