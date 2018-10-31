package com.hnjk.edu.basedata.vo;

import java.io.Serializable;

public class ClassroomVo implements Serializable {

	private Long layerNo;// 所在楼层
	private Long unitNo;// 所在房号
	private String classroomName;// 课室名称
	private Long seatNum = 0L;// 座位数
	private Long singleSeatNum = 0L;// 单座位数
	private Long doubleSeatNum = 0L;// 双座位数
	private String unitName;// 组织单位名
	private String message;
	
	public Long getLayerNo() {
		return layerNo;
	}
	public void setLayerNo(Long layerNo) {
		this.layerNo = layerNo;
	}
	public Long getUnitNo() {
		return unitNo;
	}
	public void setUnitNo(Long unitNo) {
		this.unitNo = unitNo;
	}
	public String getClassroomName() {
		return classroomName;
	}
	public void setClassroomName(String classroomName) {
		this.classroomName = classroomName;
	}
	public Long getSeatNum() {
		return seatNum;
	}
	public void setSeatNum(Long seatNum) {
		this.seatNum = seatNum;
	}
	public Long getSingleSeatNum() {
		return singleSeatNum;
	}
	public void setSingleSeatNum(Long singleSeatNum) {
		this.singleSeatNum = singleSeatNum;
	}
	public Long getDoubleSeatNum() {
		return doubleSeatNum;
	}
	public void setDoubleSeatNum(Long doubleSeatNum) {
		this.doubleSeatNum = doubleSeatNum;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}
