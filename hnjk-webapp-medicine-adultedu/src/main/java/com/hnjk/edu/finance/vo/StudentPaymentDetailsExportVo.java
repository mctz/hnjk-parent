package com.hnjk.edu.finance.vo;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 学生缴费明细简明报表
 * <code>StudentPaymentDetailsExportVo</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @version 1.0
 */
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class StudentPaymentDetailsExportVo implements Serializable {
	private String unitName;// 教学点
	private String receiptNumber;// 票据号
	private String year;// 学年
	private String paymentMethod;// 收款方式
	private String cardType;// 卡类型
	private String studentNo;// 学号
	private String studentName;// 姓名
	private String classic;// 培养层次
	private String classes;// 班级
	private String drawer;// 开票人
	private Double payAmount;// 学费
	private Date operateDate;// 缴费时间
	private Date printDate;// 打印时间
	private Double chargeMoney;// 手续费
	private String examCertificateNo;// 准考证号
	private Integer showOrder;//序号
	
}
