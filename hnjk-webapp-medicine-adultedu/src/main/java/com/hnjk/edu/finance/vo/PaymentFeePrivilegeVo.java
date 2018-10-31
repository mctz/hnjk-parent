package com.hnjk.edu.finance.vo;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 学费优惠设置vo
 * <code>PaymentFeePrivilegeVo</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2012-11-9 上午10:21:48
 * @see 
 * @version 1.0
 */
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class PaymentFeePrivilegeVo implements Serializable {
	private String paymentFeePrivilegeId;//优惠设置id
	private String unitId;//学习中心id
	private String unitCode;//学习中心编码
	private String unitName;//学习中心名称
	private String unitShortName;//学习中心简称
	private String recruitMajorId;//招生专业id
	private String recruitMajorName;//招生专业名称
	private String recruitPlanname;//招生批次名称
	private Double beforePrivilegeFee;//优惠前每学分学费
	private Double afterPrivilegeFee;//优惠后每学分学费
	private Double totalPrivilegeFee;//优惠总额
	private String memo;//备注

}
