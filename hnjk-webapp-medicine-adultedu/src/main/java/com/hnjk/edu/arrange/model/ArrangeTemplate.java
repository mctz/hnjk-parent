package com.hnjk.edu.arrange.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Where;

import com.hnjk.core.foundation.utils.ExStringUtils;
/**
 * 排课模版
 */
@Entity
@Table(name="edu_arrange_template")
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ArrangeTemplate extends SuperBaseModel{

	private static final long serialVersionUID = 1L;

	@ManyToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "SCHOOLCALENDARID",nullable=false)	
	private SchoolCalendar schoolCalendar;//所属院历
	
	@Column(name="TEMPLATENAME")
	private String templateName;//模板名称
	
	@Column(name="TIMEPERIODIDS")
	private String timePeriodids;//时间段字符串
	
	@Column(name="TIMEPERIODNAMES")
	private String timePeriodNames;//时间段名字字符串
	
	@Column(name="dateType")
	private Integer dateType;//	排课日期类型	周/日期，0-周，1-日期

	@Column(name="DAYS")
	private String days;//星期几字符串
	
	@Column(name="WEEKS")
	private String weeks;//第几周字符串
	
	@Column(name="startDate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date startDate;//开始时间
	
	@Column(name="endDate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date endDate;//结束时间
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "arrangeTemplate")
	@org.hibernate.annotations.Cascade(value={org.hibernate.annotations.CascadeType.ALL,org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@Where(clause="isDeleted=0")
	private Set<ArrangeTemplateDetail> TemplateDetais = new HashSet<ArrangeTemplateDetail>(0);//模版详情

	public SchoolCalendar getSchoolCalendar() {
		return schoolCalendar;
	}

	public void setSchoolCalendar(SchoolCalendar schoolCalendar) {
		this.schoolCalendar = schoolCalendar;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getTimePeriodids() {
		return timePeriodids;
	}

	public void setTimePeriodids(String timePeriodids) {
		this.timePeriodids = timePeriodids;
	}

	public String getTimePeriodNames() {
		return timePeriodNames;
	}

	public void setTimePeriodNames(String timePeriodNames) {
		this.timePeriodNames = timePeriodNames;
	}

	public Integer getDateType() {
		return dateType;
	}

	public void setDateType(Integer dateType) {
		this.dateType = dateType;
	}

	public String getDays(){
		return days;
	}
	public String getDaysName() {
		return ExStringUtils.getDaysName(days);
	}

	public void setDays(String days) {
		this.days = days;
	}

	public String getWeeks() {
		return weeks;
	}
	public String getWeeksName() {
		return ExStringUtils.getWeeksName(weeks);
	}

	public void setWeeks(String weeks) {
		this.weeks = weeks;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Set<ArrangeTemplateDetail> getTemplateDetais() {
		return TemplateDetais;
	}

	public void setTemplateDetais(Set<ArrangeTemplateDetail> templateDetais) {
		TemplateDetais = templateDetais;
	}
}
