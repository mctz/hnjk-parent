package com.hnjk.edu.finance.vo;

import java.math.BigDecimal;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/** 
 * 转教学点学费信息
 * 
 * @author Zik, 广东学苑教育发展有限公司
 * @since 2018年8月31日 上午11:30:46 
 * 
 */
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class TransferSchoolFeeVo {

	private String beforeSchoolId;// 之前教学点
	private String schoolId;// 转后教学点
	private BigDecimal money;// 收到学费的一半
	
}
