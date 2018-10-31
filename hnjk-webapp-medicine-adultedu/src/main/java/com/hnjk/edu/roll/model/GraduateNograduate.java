package com.hnjk.edu.roll.model;

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

/**
 * 学生不毕业申请信息表
 * <code>GraduateNograduate</code>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-10-19 下午02:33:00
 * @see
 * @version 1.0
 */
@Entity
@Table(name = "EDU_TEACH_NOGRADUATE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class GraduateNograduate extends BaseModel {

	private static final long serialVersionUID = 4483197741278770746L;

	@OneToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDENTID")
	private StudentInfo studentInfo;// 学生学籍

	@Temporal(TemporalType.DATE)
	@Column(name = "APPLAYDATE")
	private Date applayDate = new Date();// 申请日期

	@Column(name = "APPLAYCAUSE")
	private String applayCause;// 申请理由

	@Column(name = "ISPASS")
	private String isPass;// 是否通过

	@Column(name = "AUDITMANID")
	private String auditManId;// 审核人ID

	@Column(name = "AUDITMAN")
	private String auditMan;// 审核人

	/**3.2.4 新增延迟毕业申请设置*/
	@OneToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "SETTINGID")
	private GraduateNograduateSetting graduateNograduateSetting;//延迟毕业申请设置
	
	
	
	/**
	 * @return the graduateNograduateSetting
	 */
	public GraduateNograduateSetting getGraduateNograduateSetting() {
		return graduateNograduateSetting;
	}

	/**
	 * @param graduateNograduateSetting the graduateNograduateSetting to set
	 */
	public void setGraduateNograduateSetting(
			GraduateNograduateSetting graduateNograduateSetting) {
		this.graduateNograduateSetting = graduateNograduateSetting;
	}

	public String getApplayCause() {
		return applayCause;
	}

	public void setApplayCause(String applayCause) {
		this.applayCause = applayCause;
	}

	public Date getApplayDate() {
		return applayDate;
	}

	public void setApplayDate(Date applayDate) {
		this.applayDate = applayDate;
	}

	public String getAuditMan() {
		return auditMan;
	}

	public void setAuditMan(String auditMan) {
		this.auditMan = auditMan;
	}

	public String getAuditManId() {
		return auditManId;
	}

	public void setAuditManId(String auditManId) {
		this.auditManId = auditManId;
	}

	public String getIsPass() {
		return isPass;
	}

	public void setIsPass(String isPass) {
		this.isPass = isPass;
	}

	public StudentInfo getStudentInfo() {
		return studentInfo;
	}

	public void setStudentInfo(StudentInfo studentInfo) {
		this.studentInfo = studentInfo;
	}

}
