package com.hnjk.edu.finance.vo;

import java.math.BigDecimal;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;


/** 
 * @author Zik, 广东学苑教育发展有限公司
 * @since 2018年8月20日 下午4:58:51 
 * 
 */
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class MajorFeeInfoVO {

	private String majorId;// 专业ID
	private String paymentType;// 缴费类别
	private BigDecimal creditFee;// 学费
	private String teachingType;// 学习形式
	
}
