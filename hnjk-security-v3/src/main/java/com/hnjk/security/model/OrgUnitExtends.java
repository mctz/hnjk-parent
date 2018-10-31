package com.hnjk.security.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.hnjk.core.support.base.model.BaseModel;

/**
 * 系统组织单位扩展,用来扩展特殊的组织属性.
 * @author hzg
 *
 */
@Entity
@Table(name = "hnjk_sys_unitextend")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class OrgUnitExtends extends BaseModel {

	private static final long serialVersionUID = -4488104211514929232L;
	/**教务员姓名*/
	public final static String UNIT_EXTENDCODE_TEACHINGMAN = "teachingMan";
	/**教务员电话*/
	public final static String UNIT_EXTENDCODE_TEACHINGTEL = "teachingTel";
	/**教务员Email*/
	public final static String UNIT_EXTENDCODE_TEACHINGEMAIL = "teachingEmail";
	/**教务员QQ*/
	public final static String UNIT_EXTENDCODE_TEACHINGQQ = "teachingQQ";
	/**专业主任信息*/
	public final static String UNIT_EXTENDCODE_MAJORDIRECTOR = "majorDirector";

	@Column(name="EXCODE")
	private String exCode;//扩展键
	
	@Column(name="EXVALUE")
	private String exValue;//扩展值
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "UNITID")
	private OrgUnit orgUnit;//所属组织

	public OrgUnitExtends(){}
	
	public OrgUnitExtends(String exCode,String exValue,OrgUnit orgUnit){
		this.exCode = exCode;
		this.exValue = exValue;
		this.orgUnit = orgUnit;
	}
	
	/**
	 * @return the exCode
	 */
	public String getExCode() {
		return exCode;
	}

	/**
	 * @param exCode the exCode to set
	 */
	public void setExCode(String exCode) {
		this.exCode = exCode;
	}

	/**
	 * @return the exValue
	 */
	public String getExValue() {
		return exValue;
	}

	/**
	 * @param exValue the exValue to set
	 */
	public void setExValue(String exValue) {
		this.exValue = exValue;
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
	
	
	
	
}
