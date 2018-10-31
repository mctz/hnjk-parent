package com.hnjk.edu.recruit.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.edu.basedata.model.Classic;
import com.hnjk.edu.basedata.model.Grade;
import com.hnjk.edu.basedata.model.Major;
import com.hnjk.security.model.OrgUnit;
import com.hnjk.security.model.User;

/** 
 * 招生报读预约信息表
 * 
 * @author Zik, 广东学苑教育发展有限公司
 * @since 2018年6月28日 下午2:36:33 
 * 
 */
@Entity
@Table(name = "EDU_ENROLLMENTBOOK_INFO")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class EnrollmentBookInfo  extends BaseModel{

	private static final long serialVersionUID = 2115403209144170883L;
	
	@Column(name="STUDENTNAME")
	private String studentName;// 学生姓名
	
	@Column(name="CERTNUM")
	private String certNum;// 身份证号
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST ,CascadeType.REFRESH}, fetch = FetchType.LAZY)
	@JoinColumn(name = "CLASSICID")
	private Classic classic;// 层次
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST ,CascadeType.REFRESH}, fetch = FetchType.LAZY)
	@JoinColumn(name = "MAJORID")
	private Major  major;// 专业
	
	@Column(name="PHONE")
	private String phone;// 联系电话
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST ,CascadeType.REFRESH}, fetch = FetchType.LAZY)
	@JoinColumn(name = "UNITID")
	private OrgUnit unit;// 教学点
	
	@Column(name="MEMO")
	private String memo;// 备注
	
	@Column(name="OPERATORNAME")
	private String operatorName;// 操作人姓名
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST ,CascadeType.REFRESH}, fetch = FetchType.LAZY)
	@JoinColumn(name = "OPERATORID")
	private User operator;// 操作人
	
	@Column(name="CREATEDATE")
	@Temporal(TemporalType.DATE)
	private Date createDate;// 创建日期
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST ,CascadeType.REFRESH}, fetch = FetchType.LAZY)
	@JoinColumn(name = "GRADEID")
	private Grade grade;// 年级
	
	@Transient
	private String unitId;
	
	@Transient
	private String gradeId;
	
	@Transient
	private String classicId;
	
	@Transient
	private String majorId;
	
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public String getCertNum() {
		return certNum;
	}
	public void setCertNum(String certNum) {
		this.certNum = certNum;
	}
	public Classic getClassic() {
		return classic;
	}
	public void setClassic(Classic classic) {
		this.classic = classic;
	}
	public Major getMajor() {
		return major;
	}
	public void setMajor(Major major) {
		this.major = major;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public OrgUnit getUnit() {
		return unit;
	}
	public void setUnit(OrgUnit unit) {
		this.unit = unit;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getOperatorName() {
		return operatorName;
	}
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
	public User getOperator() {
		return operator;
	}
	public void setOperator(User operator) {
		this.operator = operator;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Grade getGrade() {
		return grade;
	}
	public void setGrade(Grade grade) {
		this.grade = grade;
	}
	public String getUnitId() {
		return unitId;
	}
	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}
	public String getGradeId() {
		return gradeId;
	}
	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}
	public String getClassicId() {
		return classicId;
	}
	public void setClassicId(String classicId) {
		this.classicId = classicId;
	}
	public String getMajorId() {
		return majorId;
	}
	public void setMajorId(String majorId) {
		this.majorId = majorId;
	}

}
