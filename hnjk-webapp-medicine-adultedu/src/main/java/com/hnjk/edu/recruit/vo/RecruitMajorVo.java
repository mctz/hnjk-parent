package com.hnjk.edu.recruit.vo;

public class RecruitMajorVo {

	private String recruitPlanname;//招生批次
	private String branchSchool;//教学站
	private String classicName;//层次
	private String recruitMajorCode;//招生专业编码
	private String recruitMajorName;//招生专业名称
	private String majorName;//专业
	private String teachingType;//学习形式
	private Double studyPeriod;//学制
	private Long limitNum;//上限人数
	private Long lowerNum;//下限人数
	private Double tuitionFee;//总学费
	private String errorMsg;//错误信息
	
	private String flag ;
	private String message;
	
	public String getRecruitPlanname() {
		return recruitPlanname;
	}
	public void setRecruitPlanname(String recruitPlanname) {
		this.recruitPlanname = recruitPlanname;
	}
	public String getBranchSchool() {
		return branchSchool;
	}
	public void setBranchSchool(String branchSchool) {
		this.branchSchool = branchSchool;
	}
	public String getClassicName() {
		return classicName;
	}
	public void setClassicName(String classicName) {
		this.classicName = classicName;
	}
	public String getRecruitMajorCode() {
		return recruitMajorCode;
	}
	public void setRecruitMajorCode(String recruitMajorCode) {
		this.recruitMajorCode = recruitMajorCode;
	}
	public String getRecruitMajorName() {
		return recruitMajorName;
	}
	public void setRecruitMajorName(String recruitMajorName) {
		this.recruitMajorName = recruitMajorName;
	}
	public String getMajorName() {
		return majorName;
	}
	public void setMajorName(String majorName) {
		this.majorName = majorName;
	}
	public String getTeachingType() {
		return teachingType;
	}
	public void setTeachingType(String teachingType) {
		this.teachingType = teachingType;
	}
	public Double getStudyPeriod() {
		return studyPeriod;
	}
	public void setStudyPeriod(Double studyPeriod) {
		this.studyPeriod = studyPeriod;
	}
	public Long getLimitNum() {
		return limitNum;
	}
	public void setLimitNum(Long limitNum) {
		this.limitNum = limitNum;
	}
	public Long getLowerNum() {
		return lowerNum;
	}
	public void setLowerNum(Long lowerNum) {
		this.lowerNum = lowerNum;
	}
	public Double getTuitionFee() {
		return tuitionFee;
	}
	public void setTuitionFee(Double tuitionFee) {
		this.tuitionFee = tuitionFee;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
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
	
}
