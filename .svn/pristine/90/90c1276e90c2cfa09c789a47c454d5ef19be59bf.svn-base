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

import com.hnjk.edu.teaching.model.TeachingPlanCourseTimePeriod;
/**
 * 排课模版详情
 */
@Entity
@Table(name="edu_arrange_templatedetail")
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ArrangeTemplateDetail extends SuperBaseModel{

	private static final long serialVersionUID = 1L;

	@ManyToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "TEMPLATEID",nullable=false)	
	private ArrangeTemplate arrangeTemplate;//所属排课模版
	
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "TIMEPERIODID") 
	@org.hibernate.annotations.Cascade(value={org.hibernate.annotations.CascadeType.ALL,org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@Where(clause="isDeleted=0")
	private Set<TeachingPlanCourseTimePeriod> timePeriods = new HashSet<TeachingPlanCourseTimePeriod>(0);//	时间段	
	
	@Column(name="STARTDATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date startDate;//具体开始时间
	
	@Column(name="ENDDATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date endDate;//具体结束时间
	
	@Column(name="WEEK")
	private Integer week;//周
	
	@Column(name="DAY")
	private Integer day;//星期几
	
	public ArrangeTemplate getArrangeTemplate() {
		return arrangeTemplate;
	}

	public void setArrangeTemplate(ArrangeTemplate arrangeTemplate) {
		this.arrangeTemplate = arrangeTemplate;
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

	public Integer getWeek() {
		return week;
	}

	public void setWeek(Integer week) {
		this.week = week;
	}

	public Integer getDay() {
		return day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}

	public Set<TeachingPlanCourseTimePeriod> getTimePeriods() {
		return timePeriods;
	}

	public void setTimePeriods(Set<TeachingPlanCourseTimePeriod> timePeriods) {
		this.timePeriods = timePeriods;
	}
	
}
