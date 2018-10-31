package com.hnjk.edu.teaching.vo;

import java.util.ArrayList;
import java.util.List;

public class MajorSetInfo {

	private Integer statusCode;
	private List<MajorInfo> junior = new ArrayList<MajorInfo>(0); // 专科
	private List<MajorInfo> undergraduate = new ArrayList<MajorInfo>(0);// 本科
	
	
	public Integer getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}
	public List<MajorInfo> getJunior() {
		return junior;
	}
	public void setJunior(List<MajorInfo> junior) {
		this.junior = junior;
	}
	public List<MajorInfo> getUndergraduate() {
		return undergraduate;
	}
	public void setUndergraduate(List<MajorInfo> undergraduate) {
		this.undergraduate = undergraduate;
	}
	
	
}
