package com.hnjk.edu.finance.vo;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 学费明细vo
 * @author Administrator
 *
 */
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class FeeCommissionInfoVo implements Serializable {

	private static final long serialVersionUID = -3887821534715102312L;
	private int ordinal;
	private String unitName;
	private String unitid;
	//广东医使用
	private FeeCommissionJuniorInfoVo juniorInfo;//专科-广东医使用
	private FeeCommissionUniversityInfoVo universityInfo;//本科-广东医使用
	//private BigDecimal studentFullNum;//人数合计
	//private BigDecimal shouldFullFees;//应收学费全额合计
	//private BigDecimal realFullFees;//实收学费全额合计
	//private BigDecimal notPayFullNum;//未交人数
	private BigDecimal notFullFees;//未交金额
	//private Double schoolProportion;//学校分成比例
	//private BigDecimal schoolProportionPay;//学校分成金额
	private Double proportion;//教学点分成比例
	private BigDecimal proportionPay;//教学点分成金额
	private String strSchoolPro;//教学点分成比例展示，主要供（1/3,2/3这种使用）
	private String strGDYPro;//广东医分成比例展示，主要供（1/3,2/3这种使用）

	//广外使用
	private BigDecimal realFullFees_tuition;//学费
	private BigDecimal realFullFees_others;//非学费项（教材费等）
	private FeeCommissionTypeInfoVo typeInfoVo;//学费类别-广外使用
	private BigDecimal firstReturnPay;//第一次已反学费-广外使用
	private BigDecimal secondReturnPay;//第二次已反学费-广外使用

	private String memo;

	public Double getSchoolProportion() {
		return 100-this.proportion;
	}
	public BigDecimal getProportionPay() {
		if (this.getProportion() == null) {
			return BigDecimal.valueOf(0);
		}
		return (BigDecimal.valueOf(this.getProportion()).multiply(this.getRealFullFees()).divide(BigDecimal.valueOf(100))).setScale(2,BigDecimal.ROUND_HALF_UP);
	}
	public BigDecimal getRealFullFees_tuition() {
		if (realFullFees_tuition == null) {
			return BigDecimal.valueOf(0);
		} else {
			return this.realFullFees_tuition;
		}
	}
	public BigDecimal getRealFullFees_others() {
		if (realFullFees_others == null) {
			return BigDecimal.valueOf(0);
		} else {
			return this.realFullFees_others;
		}
	}
	public BigDecimal getSchoolProportionPay() {
		return this.getRealFullFees().subtract(this.getProportionPay());
	}
	public BigDecimal getStudentFullNum() {
		return juniorInfo.getStudentNumSubtotal().add(universityInfo.getStudentNumSubtotal());
	}
	public BigDecimal getShouldFullFees() {
		return juniorInfo.getShouldFees().add(universityInfo.getShouldFees()).setScale(0,BigDecimal.ROUND_HALF_UP);
	}
	public BigDecimal getRealFullFees() {
		return juniorInfo.getRealFees().add(universityInfo.getRealFees()).setScale(0,BigDecimal.ROUND_HALF_UP);
	}
	public BigDecimal getNotPayFullNum() {
		return juniorInfo.getNotPayNum().add(universityInfo.getNotPayNum());
	}
	public BigDecimal getNotFullFees() {
		return juniorInfo.getNotFees().add(universityInfo.getNotFees()).setScale(0,BigDecimal.ROUND_HALF_UP);
	}

	public BigDecimal getShouldReturnFees() {
		return typeInfoVo.getShouldReturnPay().subtract(typeInfoVo.getReserveRatioPay()).subtract(firstReturnPay).subtract(secondReturnPay);
	}

	public String getStrSchoolPro() {
		BigDecimal _sp = (BigDecimal.valueOf(this.proportion)).setScale(12,BigDecimal.ROUND_HALF_UP);
		BigDecimal thr = new BigDecimal(33.333333333333).setScale(12,BigDecimal.ROUND_HALF_UP);;
		BigDecimal six = new BigDecimal(66.666666666667).setScale(12,BigDecimal.ROUND_HALF_UP);;
		if(_sp.compareTo(thr)==0){
			this.strSchoolPro = "1/3";
		}else if(_sp.compareTo(six)==0){
			this.strSchoolPro = "2/3";
		}else{
			this.strSchoolPro =String.valueOf(this.proportion);
		}
		
		return strSchoolPro;
	}

	public String getStrGDYPro() {
		if("1/3".equals(this.getStrSchoolPro())){
			this.strGDYPro="2/3";
		}else if("2/3".equals(this.getStrSchoolPro())){
			this.strGDYPro="1/3";
		}else{
			this.strGDYPro = String.valueOf(100-this.proportion);
		}
		return strGDYPro;
	}
	
}
