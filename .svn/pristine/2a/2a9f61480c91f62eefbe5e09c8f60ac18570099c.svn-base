package com.hnjk.edu.teaching.model;

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

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.security.model.OrgUnit;
import com.hnjk.security.model.User;

/**
 * 考试考务-监巡考人员表
 * <code>ExamStaff</code><p>
 * 
 * @author：广东学苑教育发展有限公司.
 * @since： 2011-8-17 下午02:57:12
 * @see 
 * @version 1.0
 */
@Entity
@Table(name="EDU_TEACH_EXAMSTAFF")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ExamStaff extends BaseModel {
	
	/**监考*/
	public static final Integer EXAMSTAFF_TYPE_MONITOR = 1;
	
	/**巡考*/
	public static final Integer EXAMSTAFF_TYPE_PATROL = 2;
	
	private static final long serialVersionUID = 6018567972534488364L;

	@Column(name="NAME",nullable=false,length=50)
	private String name;//姓名
	
	@OneToOne(optional = true, cascade = {CascadeType.MERGE,CascadeType.PERSIST}, fetch = FetchType.LAZY)  
	@JoinColumn(name = "SYSUSERID")
	private User sysUser;//关联的系统用户
	
	@Column(name="TELELPHONE",nullable=false,length=50)
	private String telelphone;//联系电话
	
	@Column(name="EMAIL",length=255)
	private String email;//邮件
	
	@Column(name="IDCARDNUM",length=18)
	private String idcardNum;//身份证号
	
	@OneToOne(optional = true, cascade = {CascadeType.MERGE,CascadeType.PERSIST}, fetch = FetchType.LAZY)  
	@JoinColumn(name = "ORGUNITID")
	private OrgUnit orgUnit;//所属单位
	
	@Column(name="ORGUNITNAME",length=255)
	private String orgUnitName;//所属单位名称
	
	@Column(name="GENDER")
	private String gender;//性别 字典值
	
	@Column(name="BORNDAY")
	@Temporal(TemporalType.DATE)
	private Date bornDay;//生日
	
	@Column(name="EDUCATION")
	private String education;//学历
	
	@Column(name="HEALTH")
	private String health;//健康状况 字典值
	
	@Column(name="HASEXAMSTAFF",length=1)
	private String hasExamstaff;//是否有过监考
	
	@Column(name="WORKLEVEL")
	private String workLevel;//工作级别 字典值 正常/红色/黑色
	
	@Column(name="MEMO",length=500)
	private String memo;//备注

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the sysUser
	 */
	public User getSysUser() {
		return sysUser;
	}

	/**
	 * @param sysUser the sysUser to set
	 */
	public void setSysUser(User sysUser) {
		this.sysUser = sysUser;
	}

	/**
	 * @return the telelphone
	 */
	public String getTelelphone() {
		return telelphone;
	}

	/**
	 * @param telelphone the telelphone to set
	 */
	public void setTelelphone(String telelphone) {
		this.telelphone = telelphone;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the idcardNum
	 */
	public String getIdcardNum() {
		return idcardNum;
	}

	/**
	 * @param idcardNum the idcardNum to set
	 */
	public void setIdcardNum(String idcardNum) {
		this.idcardNum = idcardNum;
	}

	/**
	 * @return the orgUnit
	 */
	public OrgUnit getOrgUnit() {
		return orgUnit;
	}

	/**
	 * @param orgUnit the orgUnit to set
	 */
	public void setOrgUnit(OrgUnit orgUnit) {
		this.orgUnit = orgUnit;
	}

	/**
	 * @return the orgUnitName
	 */
	public String getOrgUnitName() {
		return orgUnitName;
	}

	/**
	 * @param orgUnitName the orgUnitName to set
	 */
	public void setOrgUnitName(String orgUnitName) {
		this.orgUnitName = orgUnitName;
	}

	/**
	 * @return the gender
	 */
	public String getGender() {
		return gender;
	}

	/**
	 * @param gender the gender to set
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}

	/**
	 * @return the bornDay
	 */
	public Date getBornDay() {
		return bornDay;
	}

	/**
	 * @param bornDay the bornDay to set
	 */
	public void setBornDay(Date bornDay) {
		this.bornDay = bornDay;
	}

	/**
	 * @return the education
	 */
	public String getEducation() {
		return education;
	}

	/**
	 * @param education the education to set
	 */
	public void setEducation(String education) {
		this.education = education;
	}

	/**
	 * @return the health
	 */
	public String getHealth() {
		return health;
	}

	/**
	 * @param health the health to set
	 */
	public void setHealth(String health) {
		this.health = health;
	}

	/**
	 * @return the hasExamstaff
	 */
	public String getHasExamstaff() {
		return hasExamstaff;
	}

	/**
	 * @param hasExamstaff the hasExamstaff to set
	 */
	public void setHasExamstaff(String hasExamstaff) {
		this.hasExamstaff = hasExamstaff;
	}

	/**
	 * @return the workLevel
	 */
	public String getWorkLevel() {
		return workLevel;
	}

	/**
	 * @param workLevel the workLevel to set
	 */
	public void setWorkLevel(String workLevel) {
		this.workLevel = workLevel;
	}

	/**
	 * @return the memo
	 */
	public String getMemo() {
		return memo;
	}

	/**
	 * @param memo the memo to set
	 */
	public void setMemo(String memo) {
		this.memo = memo;
	}
	
	
}
