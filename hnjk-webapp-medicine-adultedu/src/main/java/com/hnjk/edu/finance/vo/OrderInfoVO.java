package com.hnjk.edu.finance.vo;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 订单信息
 * 
 * @author Zik, 广东学苑教育发展有限公司
 * @since Nov 8, 2016 2:13:05 PM 
 * 
 */
@ToString
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class OrderInfoVO implements Serializable {

	private String orderNo;// 学校订单号，必填
	private String studentId;// 学号，系统已存在学号，用于指定将要新增订单的学生，必填
	// 金额,填入DEF表示使用缴费项目金额，也可自行设定金额，单位分，必填
	private String amount;
	private String remark;// 备注
	private String schoolOrderNo;// 学校订单号
	
}
