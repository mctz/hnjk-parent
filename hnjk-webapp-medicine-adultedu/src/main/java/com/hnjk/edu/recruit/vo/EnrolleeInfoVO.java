package com.hnjk.edu.recruit.vo;

import com.hnjk.edu.recruit.model.EnrolleeInfo;
import lombok.ToString;

@ToString
public class EnrolleeInfoVO {
	private EnrolleeInfo enrolleeInfo;
	private Double ksh;
	private Double  total;
	public EnrolleeInfo getEnrolleeInfo() {
		return enrolleeInfo;
	}
	public void setEnrolleeInfo(EnrolleeInfo enrolleeInfo) {
		this.enrolleeInfo = enrolleeInfo;
	}
	public Double getKsh() {
		return ksh;
	}
	public void setKsh(Double ksh) {
		this.ksh = ksh;
	}
	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
	}
}
