package com.hnjk.edu.roll.model;

import java.io.Serializable;
import java.util.Calendar;
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
import org.hibernate.annotations.Cascade;

import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.edu.basedata.model.YearInfo;
import com.hnjk.platform.system.cache.CacheAppManager;

/**
 * 学生注册信息表
 * <code>Reginfo</code>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2010-4-19 下午05:55:34
 * @see
 * @version 1.0
 */
@Entity
@Table(name = "EDU_ROLL_REGINFO")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Reginfo extends BaseModel implements Serializable {

	private static final long serialVersionUID = 8053571312388293507L;

	@OneToOne(optional = false, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "YEARID")
	private YearInfo yearInfo;// 注册年度

	@OneToOne(fetch = FetchType.LAZY)
	@Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
	@JoinColumn(name = "STUDENTID")
	private StudentInfo studentInfo;// 学生学籍

	@Column(name = "TERM")
	private String term;// 学期

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "REGDATE")
	private Date regDate = new Date();// 注册日期

	@Column(name = "ABNORMALMEMO")
	private String abnormalMemo;// 注册异常说明

	@Column(name = "MEMO")
	private String memo;// 备注

	@Column(name = "FILLINMAN")
	private String fillinMan;// 操作人

	@Column(name = "FILLINMANID")
	private String fillinManId;// 操作人ID

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FILLINDATE")
	private Date fillinDate; // 操作时间

	@Transient
	private String province = "44";

	@Transient
	private String schoolCode ;//学校代码

	@Transient
	private String schoolName; //学校名称

	@Transient
	private String stuLimite ;//学制

	@Transient
	private String emptyStr = "0";

	@Transient
	private String season = "";

	@Transient
	private String age = "";

	public String getAbnormalMemo() {
		return abnormalMemo;
	}

	public String getAge() {
		age = "";
		Date d = getStudentInfo().getStudentBaseInfo().getBornDay();
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		age = (ExDateUtils.getCurrentYear() - cal.get(Calendar.YEAR))+"";
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public void setAbnormalMemo(String abnormalMemo) {
		this.abnormalMemo = abnormalMemo;
	}

	public Date getFillinDate() {
		return fillinDate;
	}

	public void setFillinDate(Date fillinDate) {
		this.fillinDate = fillinDate;
	}

	public String getFillinMan() {
		return fillinMan;
	}

	public void setFillinMan(String fillinMan) {
		this.fillinMan = fillinMan;
	}

	public String getFillinManId() {
		return fillinManId;
	}

	public void setFillinManId(String fillinManId) {
		this.fillinManId = fillinManId;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Date getRegDate() {
		return regDate;
	}

	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public StudentInfo getStudentInfo() {
		return studentInfo;
	}

	public void setStudentInfo(StudentInfo studentInfo) {
		this.studentInfo = studentInfo;
	}

	public YearInfo getYearInfo() {
		return yearInfo;
	}

	public void setYearInfo(YearInfo yearInfo) {
		this.yearInfo = yearInfo;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getSchoolCode() {
		return CacheAppManager.getSysConfigurationByCode("graduateData.schoolCode").getParamValue();
	}

	public void setSchoolCode(String schoolCode) {
		this.schoolCode = schoolCode;
	}

	public String getSchoolName() {
		return CacheAppManager.getSysConfigurationByCode("graduateData.schoolName").getParamValue();
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public String getStuLimite() {
		return CacheAppManager.getSysConfigurationByCode("graduateData.transactTime").getParamValue();
	}

	public void setStuLimite(String stuLimite) {
		this.stuLimite = stuLimite;
	}

	public String getEmptyStr() {
		return emptyStr;
	}

	public void setEmptyStr(String emptyStr) {
		this.emptyStr = emptyStr;
	}

	public String getSeason() {
		season = "";
		String temp = "";
		if(null != getStudentInfo() && null!=getStudentInfo().getGrade()){
			temp =  getStudentInfo().getGrade().getGradeName();
			season = temp.substring(4)+ "季";
		}
		return season ;
	}

	public void setSeason(String season) {
		this.season = season;
	}
	
}