package com.hnjk.edu.teaching.vo;

import com.hnjk.core.foundation.utils.ExStringUtils;

/**
 * 毕业论文预约统计导出VO
 * <code>GraduatePapersOrderVo</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2012-2-7 下午02:41:07
 * @see 
 * @version 1.0
 */
public class GraduatePapersOrderVo {
	private String batchName;//论文批次
	private String majorName;//专业
	private String guidTeacherName;//指导老师
	private String email;
	private String mobile;
	private String teacherInfo;//老师姓名+email+联系电话
	private String unitName;//教学中心
	private String unitShortName;
	private String studentName;
	private String studyNo;
	private String studentInfo;//学生姓名+学号
	
	public String getBatchName() {
		return batchName;
	}
	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}
	public String getMajorName() {
		return majorName;
	}
	public void setMajorName(String majorName) {
		this.majorName = majorName;
	}
	public String getGuidTeacherName() {
		return guidTeacherName;
	}
	public void setGuidTeacherName(String guidTeacherName) {
		this.guidTeacherName = guidTeacherName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	public String getUnitShortName() {
		return unitShortName;
	}
	public void setUnitShortName(String unitShortName) {
		this.unitShortName = unitShortName;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public String getStudyNo() {
		return studyNo;
	}
	public void setStudyNo(String studyNo) {
		this.studyNo = studyNo;
	}
	public String getTeacherInfo() {
		return ExStringUtils.trimToEmpty(guidTeacherName)+"\nE-mail: "+ExStringUtils.trimToEmpty(email)+"\n手  机: "+ExStringUtils.trimToEmpty(mobile);
	}
	public String getStudentInfo() {
		return studentName+"("+studyNo+")";
	}
}
