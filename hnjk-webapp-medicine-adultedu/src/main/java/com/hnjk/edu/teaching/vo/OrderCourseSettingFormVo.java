package com.hnjk.edu.teaching.vo;
/**
 * 
 * <code>年级预约权限FormVo</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-8-13 下午02:47:30
 * @see 
 * @version 1.0
 */
public class OrderCourseSettingFormVo {
	private String resourceid;
	private String grade;//对应年级
	private String yearInfo;//年度
	private String term; //学期
	private String startDate;//开始预约时间
	private String endDate;//截止预约时间
	private String isOpened;//是否开放
	private String limitOrderNum;//限制预约个数
	
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getIsOpened() {
		return isOpened;
	}
	public void setIsOpened(String isOpened) {
		this.isOpened = isOpened;
	}
	public String getLimitOrderNum() {
		return limitOrderNum;
	}
	public void setLimitOrderNum(String limitOrderNum) {
		this.limitOrderNum = limitOrderNum;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getResourceid() {
		return resourceid;
	}
	public void setResourceid(String resourceid) {
		this.resourceid = resourceid;
	}
	public String getYearInfo() {
		return yearInfo;
	}
	public void setYearInfo(String yearInfo) {
		this.yearInfo = yearInfo;
	}
	public String getTerm() {
		return term;
	}
	public void setTerm(String term) {
		this.term = term;
	}
	
}
