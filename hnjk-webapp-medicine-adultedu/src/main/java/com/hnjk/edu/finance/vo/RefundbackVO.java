package com.hnjk.edu.finance.vo;

import java.math.BigDecimal;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/** 
 * 预退费补交订单VO
 * @author Zik, 广东学苑教育发展有限公司
 * @since 2018年8月14日 上午9:40:48 
 * 
 */
@ToString
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class RefundbackVO {

	private String showOrder;// 序号
	private String unitName;// 教学点名称
	private String studyNo;// 学号
	private String studentName;// 学生姓名
	private String changeType;// 异动类型
	private BigDecimal money; // 退费金额
	private String accountName;// 开户名
	private String openingBank;// 开户行
	private String bankAccount;// 银行账号
	private String  auditingDate;// 教学点审批日期
	
	private String yearName;// 学年
	private String chargingItems;// 收费项
	private String processType;// 处理类型
	private String processStatus;// 处理状态
}
