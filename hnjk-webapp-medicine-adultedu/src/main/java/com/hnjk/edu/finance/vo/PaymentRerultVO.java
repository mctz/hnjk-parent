package com.hnjk.edu.finance.vo;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/** 
 * @author Zik, 广东学苑教育发展有限公司
 * @since 2018年1月19日 下午5:03:58 
 * 
 */
@ToString
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class PaymentRerultVO implements Serializable {

	private String sn;// 订单号
	private String createTime;// 订单创建时间
	private String idCard;// 身份证号
	private List<EnquiryPaymentDetailsVO> records = new ArrayList<EnquiryPaymentDetailsVO>(5);// 缴费记录
	private String sysId;// 系统号
	private String sign;// 签名

}
