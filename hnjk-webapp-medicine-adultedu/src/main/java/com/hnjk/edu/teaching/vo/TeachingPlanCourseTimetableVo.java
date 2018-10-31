package com.hnjk.edu.teaching.vo;

import java.util.Date;

public class TeachingPlanCourseTimetableVo {
	
	private String teachName;//上课老师
	private String address;//上课地点
	private Date schoolTime;//上课日期
	private String schollWeek;//上课星期
	private String schoolTimeSlot;//上课时间段
	private String remark;//临时调课备注
    private String mergeMemo;//合班情况
	
	
	//导入时用的
	private String flag;
	private String message;
	
	public String getTeachName() {
		return teachName;
	}
	public void setTeachName(String teachName) {
		this.teachName = teachName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	public Date getSchoolTime() {
		return schoolTime;
	}
	public void setSchoolTime(Date schoolTime) {
		this.schoolTime = schoolTime;
	}
	public String getSchollWeek() {
		return schollWeek;
	}
	public void setSchollWeek(String schollWeek) {
		this.schollWeek = schollWeek;
	}
	public String getSchoolTimeSlot() {
		return schoolTimeSlot;
	}
	public void setSchoolTimeSlot(String schoolTimeSlot) {
		this.schoolTimeSlot = schoolTimeSlot;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getMergeMemo() {
		return mergeMemo;
	}
	public void setMergeMemo(String mergeMemo) {
		this.mergeMemo = mergeMemo;
	}

}
