package com.hnjk.edu.finance.vo;

import com.hnjk.core.foundation.utils.ExDateUtils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * 学费明细vo
 * @author Administrator
 *
 */
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class StuReturnFeeCommissionInfoVo implements Serializable {

	private String unitName;//教学点
	private String gradeName;//年级
	private String classicName;//层次
	private String majorName;//专业
	private String studyNo;//学号
	private String studentName;//姓名
	private String mobile;//联系电话
	private Date inDate;//
	private Date applicationDate;//申请日期
	private String applicationDateStr;//申请日期
	private BigDecimal creditFee;//年缴费标准
	private Double royaltyRate;//教学点分成比例
	//private Double schoolProportion;//广东医分成比例
	
	private int inMonth;//在读月数
	//private int refundMonth;//退款月数
	private BigDecimal proportionPay;//教学点退费金额
	//private BigDecimal schoolProportionPay;//广东医退费金额
	
	private String strSchoolPro;//教学点分成比例展示，主要供（1/3,2/3这种使用）
	private String strGDYPro;//广东医分成比例展示，主要供（1/3,2/3这种使用）
	
	private String bankName;//所属银行
	private String cardNo;//银行卡号
	private String bankAddress;//开户行名称
	

	public String getGradeName() {
		return gradeName.replace("级","");
	}

	public String getApplicationDateStr() {
		try {
			return ExDateUtils.formatDateStr(this.applicationDate, 1);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "";
	}
	public BigDecimal getCreditFee() {
		return this.creditFee.multiply(BigDecimal.valueOf(10-this.getInMonth())).divide(BigDecimal.valueOf(10));
	}

	public int getInMonth() {
		//if("".equals(this.inMonth) || this.inMonth==0){
			Calendar calendar = Calendar.getInstance();
			if(this.getApplicationDate()==null){
				calendar.setTime(new Date());
			}else {
				calendar.setTime(this.applicationDate);
			}
			//申请日期
			int month = calendar.get(Calendar.MONTH)+1;
			if(month>=3){
				if(month<9 && month>6){
					month = 5;
				}else if(month>8){
					month = month-3;
				}else {
					month = month-2;
				}
			}else {
				month = 10;
			}
			this.setInMonth(month);
		//}
		return inMonth;
	}

	public int getRefundMonth() {
		return 10-this.getInMonth();
	}
	
	public Double getSchoolProportion() {
		return 100-this.royaltyRate;
	}
	public BigDecimal getProportionPay() {
		if(this.proportionPay==null){
			this.proportionPay = (BigDecimal.valueOf(this.getRoyaltyRate()).multiply(this.getCreditFee()).divide(BigDecimal.valueOf(100))).setScale(2,BigDecimal.ROUND_HALF_UP);
		}
		return proportionPay;
	}
	public BigDecimal getSchoolProportionPay() {
		return this.getCreditFee().subtract(this.getProportionPay());
	}
	
	public String getStrSchoolPro() {
		BigDecimal _sp = (BigDecimal.valueOf(this.royaltyRate)).setScale(12,BigDecimal.ROUND_HALF_UP);
		BigDecimal thr = new BigDecimal(33.333333333333).setScale(12,BigDecimal.ROUND_HALF_UP);;
		BigDecimal six = new BigDecimal(66.666666666667).setScale(12,BigDecimal.ROUND_HALF_UP);;
		if(_sp.compareTo(thr)==0){
			this.strSchoolPro = "1/3";
		}else if(_sp.compareTo(six)==0){
			this.strSchoolPro = "2/3";
		}else{
			this.strSchoolPro =String.valueOf(this.royaltyRate);
		}
		
		return strSchoolPro;
	}
	public String getStrGDYPro() {
		if("1/3".equals(this.getStrSchoolPro())){
			this.strGDYPro="2/3";
		}else if("2/3".equals(this.getStrSchoolPro())){
			this.strGDYPro="1/3";
		}else{
			this.strGDYPro = String.valueOf(100-this.royaltyRate);
		}
		return strGDYPro;
	}

}
