package com.hnjk.edu.finance.vo;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

/** 
 * @author Zik, 广东学苑教育发展有限公司
 * @since 2017年2月20日 下午2:00:44 
 * 
 */
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
@ToString
public class CheckFileTotalVO implements Serializable {

	private String tradeDate;// 交易日期
	private Integer tradeItems;// 交易笔数
	private BigDecimal totalFee;// 手续费
	private BigDecimal totalAmount;// 交易金额

}
