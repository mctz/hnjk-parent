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
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.Grade;
import com.hnjk.edu.basedata.model.YearInfo;
import com.hnjk.platform.taglib.JstlCustomFunction;

/**
 * 预约管理表
 * <code>OrderCourseSetting</code><p>
 * 学生预约学习前，管理人员对下学期的预约进行控制，如设置预约年度，学期，及开始、结束时间等。<p>
 * 同一时刻，只允许一条记录状态为开放.
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-8-10 下午02:03:44
 * @see 
 * @version 1.0
 */
@Entity
@Table(name="EDU_TEACH_ORDERSETTING")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class OrderCourseSetting extends BaseModel{
	
	private static final long serialVersionUID = 4094754559701303682L;
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "GRADEID")
	private Grade grade;//对应年级 （保留）
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "STARTTIME",nullable=false)
	private Date startDate;//开始预约时间
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ENDTIME",nullable=false)
	private Date endDate;//截止预约时间
	
	@Column(name="ISOPENED")
	private String isOpened = Constants.BOOLEAN_NO;//是否开放
	
	@Column(name="LIMITORDERNUM")
	private Integer limitOrderNum = 6;//限制预约个数

	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "YEARID",nullable=false)
	private YearInfo yearInfo;//预约年度
	
	@Column(name="TERM",nullable=false)
	private String term;//所在学期
	
	@Transient
	private String settingName;//年度名称+学期
	
	
	public void setSettingName(String settingName) {
		this.settingName = settingName;
	}

	public String getSettingName() {
		return this.yearInfo.getYearName()+" "+JstlCustomFunction.dictionaryCode2Value("CodeTerm", this.term);
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public YearInfo getYearInfo() {
		return yearInfo;
	}

	public void setYearInfo(YearInfo yearInfo) {
		this.yearInfo = yearInfo;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Grade getGrade() {
		return grade;
	}

	public void setGrade(Grade grade) {
		this.grade = grade;
	}

	public String getIsOpened() {
		return isOpened;
	}

	public void setIsOpened(String isOpened) {
		this.isOpened = isOpened;
	}

	public Integer getLimitOrderNum() {
		return limitOrderNum;
	}

	public void setLimitOrderNum(Integer limitOrderNum) {
		this.limitOrderNum = limitOrderNum;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	
	@Override
	public String getResourceid() {
		return super.getResourceid();
	}

}
