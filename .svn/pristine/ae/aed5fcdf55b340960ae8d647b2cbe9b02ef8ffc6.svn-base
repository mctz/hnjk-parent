package com.hnjk.edu.finance.vo;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 专科学费明细vo
 * @author Administrator
 *
 */
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class FeeCommissionJuniorInfoVo implements Serializable {
	
	private String classicName;
	private BigDecimal studentNumSubtotal;//年级人数小计
	private BigDecimal studentFeeRule;//学生缴费标准
	private BigDecimal shouldFees;//应收学费全额
	private BigDecimal realFees;//实收学费全额
	private BigDecimal payNum;//实交人数
	private BigDecimal notPayNum;//实交人数
	private BigDecimal notFees;//未交学费全额
	private GradeNumVo gradeNumVo1;
	private GradeNumVo gradeNumVo2;
	private GradeNumVo gradeNumVo3;
	
	public String getClassicName() {
		return "本科";
	}
	public BigDecimal getStudentNumSubtotal() {
		return this.getGradeNumVo1().getStudentNum().add(this.getGradeNumVo2().getStudentNum()).add(this.getGradeNumVo3().getStudentNum());
	}
	public BigDecimal getStudentFeeRule() {
		if(this.studentFeeRule==null){
			return BigDecimal.valueOf(3250);// 广东医专科学费默认3250
		}
		return studentFeeRule;
	}

	public BigDecimal getShouldFees() {
		return this.getStudentFeeRule().multiply(this.getStudentNumSubtotal());
	}
	public BigDecimal getRealFees() {
		return this.getGradeNumVo1().getFees().add(this.getGradeNumVo2().getFees()).add(this.getGradeNumVo3().getFees());
	}

	public BigDecimal getPayNum() {
		return this.getGradeNumVo1().getPayNum().add(this.getGradeNumVo2().getPayNum()).add(this.getGradeNumVo3().getPayNum());
	}
	
	public BigDecimal getNotPayNum() {
		return this.getStudentNumSubtotal().subtract(this.getPayNum());
	}

	public BigDecimal getNotFees() {
		return this.getNotPayNum().multiply(this.getStudentFeeRule());
	}

	public GradeNumVo getGradeNumVo1() {
		if(this.gradeNumVo1==null){
			return new GradeNumVo();
		}
		return gradeNumVo1;
	}

	public GradeNumVo getGradeNumVo2() {
		if(this.gradeNumVo2==null){
			return new GradeNumVo();
		}
		return gradeNumVo2;
	}

	public GradeNumVo getGradeNumVo3() {
		if(this.gradeNumVo3==null){
			return new GradeNumVo();
		}
		return gradeNumVo3;
	}

}
