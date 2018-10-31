package com.hnjk.edu.finance.vo;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class GradeNumVo implements Serializable {
	private String grade;
	/**
	 * 学生人数
	 */
	private BigDecimal studentNum;
	/**
	 * 实交人数
	 */
	private BigDecimal payNum;
	/**
	 * 实交金额
	 */
	private BigDecimal fees;
	public String getGrade() {
		if(this.grade==null){
			return "";
		}
		return grade;
	}

	public BigDecimal getStudentNum() {
		if(this.studentNum==null){
			return BigDecimal.valueOf(0);
		}
		return studentNum;
	}

	public BigDecimal getPayNum() {
		if(this.payNum==null){
			return BigDecimal.valueOf(0);	
		}
		return payNum;
	}

	public BigDecimal getFees() {
		if(this.fees==null){
			return BigDecimal.valueOf(0);
		}
		return fees;
	}
	
}
