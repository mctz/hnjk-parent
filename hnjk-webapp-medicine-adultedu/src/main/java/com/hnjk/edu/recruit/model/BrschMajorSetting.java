package com.hnjk.edu.recruit.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.Classic;
import com.hnjk.edu.basedata.model.Major;
import com.hnjk.security.model.OrgUnit;

/**
 * 校外学习招生专业限制设置.
 * <code>BrschMajorSetting</code><p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2011-1-11 上午11:13:11
 * @see 
 * @version 1.0
 */
@Entity
@Table(name="EDU_RECRUIT_BRSCHMAJORSET")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class BrschMajorSetting extends BaseModel{

	private static final long serialVersionUID = 2124238052139586773L;
	
	@OneToOne(optional = false, cascade = {CascadeType.MERGE,CascadeType.PERSIST}, fetch = FetchType.LAZY)  
	@JoinColumn(name = "BRSCHOOLID") 
	private OrgUnit brSchool;//校外学习中心
	
	@OneToOne(optional = true, cascade = {CascadeType.MERGE,CascadeType.PERSIST}, fetch = FetchType.LAZY)  
	@JoinColumn(name = "CLASSICID") 
	private Classic classic;//层次
	
	@OneToOne(optional = true, cascade = {CascadeType.MERGE,CascadeType.PERSIST}, fetch = FetchType.LAZY)  
	@JoinColumn(name = "MAJORID") 
	private Major major;//专业
	
	@Column(name="SCHOOLTYPE")
	private String schoolType;//办学模式 字典值
	
	@Column(name="LIMITNUM")
	private Integer limitNum;//招生指标数
	
	@Column(name="ISOPENED")
	private String isOpened = Constants.BOOLEAN_YES;//状态，默认开启
	
	@Column(name="MEMO")
	private String memo;//备注

	public OrgUnit getBrSchool() {
		return brSchool;
	}

	public void setBrSchool(OrgUnit brSchool) {
		this.brSchool = brSchool;
	}

	public Classic getClassic() {
		return classic;
	}

	public void setClassic(Classic classic) {
		this.classic = classic;
	}

	public Integer getLimitNum() {
		return limitNum;
	}

	public void setLimitNum(Integer limitNum) {
		this.limitNum = limitNum;
	}

	public Major getMajor() {
		return major;
	}

	public void setMajor(Major major) {
		this.major = major;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getSchoolType() {
		return schoolType;
	}

	public void setSchoolType(String schoolType) {
		this.schoolType = schoolType;
	}

	public String getIsOpened() {
		return isOpened;
	}

	public void setIsOpened(String isOpened) {
		this.isOpened = isOpened;
	}

	
	
	
}
