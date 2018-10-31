package com.hnjk.edu.teaching.vo;
/**
 * 
 * <code>预约权限屏蔽Vo</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-8-10 下午05:27:03
 * @see 
 * @version 1.0
 */
public class CourseOrderSearchVo {
	
	private String configStatus;//屏蔽类型 1 学习 2 考试
	private String orderCourseStatusConfigType ;//预约状态设置类型  personal个人  grade年级
	private String orderCourseStatus ;//预约状态 0屏蔽 1开放  
	private String isOpen; //预约状态 N屏蔽 Y开放  
	private String branchSchool;//学习中心
	private String major;//专业
	private String classic;//层次
	private String gradeid;//年级
	private String name;//姓名 
	private String sturesourceId;//学员ID
	private String matriculateNoticeNo;//学号
	private String settingId;//年度预约权限设置ID
	private String isAbleOrderSubject;//毕业论文预约权限
	
	
	public String getConfigStatus() {
		return configStatus;
	}
	public void setConfigStatus(String configStatus) {
		this.configStatus = configStatus;
	}
	public String getIsAbleOrderSubject() {
		return isAbleOrderSubject;
	}
	public void setIsAbleOrderSubject(String isAbleOrderSubject) {
		this.isAbleOrderSubject = isAbleOrderSubject;
	}

	
	public String getMatriculateNoticeNo() {
		return matriculateNoticeNo;
	}
	public void setMatriculateNoticeNo(String matriculateNoticeNo) {
		this.matriculateNoticeNo = matriculateNoticeNo;
	}
	public String getBranchSchool() {
		return branchSchool;
	}
	public void setBranchSchool(String branchSchool) {
		this.branchSchool = branchSchool;
	}
	public String getClassic() {
		return classic;
	}
	public void setClassic(String classic) {
		this.classic = classic;
	}
	public String getGradeid() {
		return gradeid;
	}
	public void setGradeid(String gradeid) {
		this.gradeid = gradeid;
	}
	public String getMajor() {
		return major;
	}
	public void setMajor(String major) {
		this.major = major;
	}


	
	public String getSturesourceId() {
		return sturesourceId;
	}
	public void setSturesourceId(String sturesourceId) {
		this.sturesourceId = sturesourceId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getOrderCourseStatus() {
		return orderCourseStatus;
	}
	public void setOrderCourseStatus(String orderCourseStatus) {
		this.orderCourseStatus = orderCourseStatus;
	}
	public String getOrderCourseStatusConfigType() {
		return orderCourseStatusConfigType;
	}
	public void setOrderCourseStatusConfigType(String orderCourseStatusConfigType) {
		this.orderCourseStatusConfigType = orderCourseStatusConfigType;
	}
	public String getIsOpen() {
		return isOpen;
	}
	public void setIsOpen(String isOpen) {
		this.isOpen = isOpen;
	}
	public String getSettingId() {
		return settingId;
	}
	public void setSettingId(String settingId) {
		this.settingId = settingId;
	}

}
