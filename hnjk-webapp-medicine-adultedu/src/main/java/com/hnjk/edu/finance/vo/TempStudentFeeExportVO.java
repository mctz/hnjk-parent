package com.hnjk.edu.finance.vo;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author  广东学苑教育发展有限公司
 * 
 */
@ToString
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class TempStudentFeeExportVO implements Serializable {
	
	private String certNum;// 身份证号
	private String examCertificateNo;// 准考证号
	private String studentName;// 学生名称
	private String unitName;// 教学点名称
	private String gradeName;// 年级名称.
	private String majorName;//专业
	private Double amount;//金额
	private String eduOrderNo;//教育系统订单号
	private String schoolOrderNo;//学校订单号
	private String batchNo;//批次编号
	private String isUploaded;//已同步
	private String payStatus;//支付状态
	
}
