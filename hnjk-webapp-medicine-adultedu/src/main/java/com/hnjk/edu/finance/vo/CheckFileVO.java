package com.hnjk.edu.finance.vo;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/** 
 * 对账VO
 * 
 * @author Zik, 广东学苑教育发展有限公司
 * @since 2017年2月20日 下午1:12:48 
 * 
 */
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
@ToString
public class CheckFileVO implements Serializable {

	private String studyNo;// 学号
	private String eduOrederNo;// 教育系统订单号
	private String orderNo;// 订单号
	private Double payAmount;// 缴费金额
	private Double fee;// 手续费
	private String payMethod;// 付款方式
	private String payTime;// 支付时间
	private String payNo;// 支付流水号
	
}
