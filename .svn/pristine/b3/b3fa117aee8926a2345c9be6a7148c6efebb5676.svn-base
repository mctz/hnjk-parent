package com.hnjk.edu.basedata.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.hnjk.core.support.base.model.BaseModel;

/**
 * <code>BaseYear</code>基础数据-年度表
 * <p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-3-12 上午10:01:06
 * @see
 * @version 1.0
 */
@Entity
@Table(name = "EDU_BASE_YEAR")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class YearInfo extends BaseModel implements Serializable{

	private static final long serialVersionUID = 1L;

	@Column(name = "YEARNAME", nullable = false, unique = true)
	private String yearName;// 年度名称

	@Column(name = "FIRSTYEAR", nullable = false,unique = true, precision = 22, scale = 0)
	private Long firstYear;// 起始年份

	@Temporal(TemporalType.DATE)
	@Column(name = "FIRSTMONDAYOFFIRSTTERM", nullable = false)
	private Date firstMondayOffirstTerm;// 第一学期首周一日期

	@Column(name = "FIRSTTERMWEEKNUM", nullable = false, precision = 22, scale = 0)
	private Long firstTermWeekNum;// 第一学期周数

	@Temporal(TemporalType.DATE)
	@Column(name = "FIRSTMONDAYOFSECONDTERM", nullable = false)
	private Date firstMondayOfSecondTerm;// 第二学期首周一日期

	@Column(name = "SECONDTERMWEEKNUM", nullable = false, precision = 22, scale = 0)
	private Long secondTermWeekNum;// 第二学期周数

	@Column(name = "MEMO")		
	private String memo;// 备注
	
	@Temporal(TemporalType.DATE)
	@Column(name = "REGISTRATIONDATE")
	private Date registrationDate;// 报到注册日

	public Date getFirstMondayOffirstTerm() {
		return firstMondayOffirstTerm;
	}

	public void setFirstMondayOffirstTerm(Date firstMondayOffirstTerm) {
		this.firstMondayOffirstTerm = firstMondayOffirstTerm;
	}

	public Date getFirstMondayOfSecondTerm() {
		return firstMondayOfSecondTerm;
	}

	public void setFirstMondayOfSecondTerm(Date firstMondayOfSecondTerm) {
		this.firstMondayOfSecondTerm = firstMondayOfSecondTerm;
	}

	public Long getFirstTermWeekNum() {
		return firstTermWeekNum;
	}

	public void setFirstTermWeekNum(Long firstTermWeekNum) {
		this.firstTermWeekNum = firstTermWeekNum;
	}

	public Long getFirstYear() {
		return firstYear;
	}

	public void setFirstYear(Long firstYear) {
		this.firstYear = firstYear;
	}

	public Long getSecondTermWeekNum() {
		return secondTermWeekNum;
	}

	public void setSecondTermWeekNum(Long secondTermWeekNum) {
		this.secondTermWeekNum = secondTermWeekNum;
	}

	public String getYearName() {
		return yearName;
	}

	public void setYearName(String yearName) {
		this.yearName = yearName;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Override
	public String toString() {
		return getYearName();
	}

	@Override
	public String getResourceid() {
		return super.getResourceid();
	}

	public Date getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}
	

}