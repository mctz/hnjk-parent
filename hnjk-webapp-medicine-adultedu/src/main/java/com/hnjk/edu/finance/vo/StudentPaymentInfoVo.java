package com.hnjk.edu.finance.vo;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 学生缴费记录有关信息
 * <code>StudentPaymentInfoVo</code><p>
 * 
 * @author：zik, 广东学苑教育发展有限公司
 * @since： 2015-11-20 15:01
 * @see 
 * @version 1.0
 */
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class StudentPaymentInfoVo implements Serializable {
	private static final long serialVersionUID = 3895445363001367447L;
	private String studentPaymentId;// 学生缴费记录ID
	private Double creditFee;// 缴费标准中的应缴金额
	private Date creditEndDate;// 缴费标准中的截止缴费日期
	private Integer feeTerm;// 缴费期数
	private Double recpayFee;// 缴费记录中的应缴金额
	private Double facepayFee;// 缴费记录中的实缴金额
	private Double derateFee;// 缴费记录中的减免金额
	private String originalGrade;// 原始年级ID
	private String gradeId;// 现在的年级ID
	private String originalMajor;// 原始专业ID
	private String majorId;// 现在的专业ID
	private Date payBeginDate;// 开始缴费日期
	private String teachingType;// 学习形式

}
