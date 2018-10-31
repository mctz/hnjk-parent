package com.hnjk.edu.recruit.vo;

public class EnrollStatisticalVo {

	private String rowNum;
	private String unitName;
	private String majorName;
	private String classicName;
	private String teachingtype;
	private int total;
	private int regNum;
	private int notRegNum;
	private String proportion;
	
	public String getRowNum() {
		return rowNum;
	}
	public void setRowNum(String rowNum) {
		this.rowNum = rowNum;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	public String getMajorName() {
		return majorName;
	}
	public void setMajorName(String majorName) {
		this.majorName = majorName;
	}
	public String getClassicName() {
		return classicName;
	}
	public void setClassicName(String classicName) {
		this.classicName = classicName;
	}
	public String getTeachingtype() {
		return teachingtype;
	}
	public void setTeachingtype(String teachingtype) {
		this.teachingtype = teachingtype;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getRegNum() {
		return regNum;
	}
	public void setRegNum(int regNum) {
		this.regNum = regNum;
	}
	public int getNotRegNum() {
		return notRegNum;
	}
	public void setNotRegNum(int notRegNum) {
		this.notRegNum = notRegNum;
	}
	public String getProportion() {
		return proportion;
	}
	public void setProportion(String proportion) {//数据库查询结果整数部分为0缺失
		if(proportion.startsWith(".")){
			proportion="0"+proportion;
		}
		this.proportion = proportion;
	}
}
