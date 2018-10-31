package com.hnjk.edu.arrange.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.hnjk.core.support.base.model.BaseModel;

/**
 * 院历事件
 */
@Entity
@Table(name="EDU_ARRANGE_CALENDAREVENT")
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CalendarEvent extends SuperBaseModel {
	private static final long serialVersionUID = 239672134794911762L;
	
	@ManyToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "SCHOOLCALENDARID",nullable=false)	
	private SchoolCalendar schoolCalendar;//院历
	
	//事件名字
	@Column(name="NAME")
	private String name;
	
	//事件内容
	@Column(name="CONTENT") 
	private String content;
	
	//类型
	@Column(name="TYPE")
	private Integer type;//-1:不能进行排课和排考	0:排课和排考都行	1:只能排课	2:只能排考
	
	//开始时间
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="STARTDATE")
	private Date startDate;
	
	//结束时间
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="ENDDATE")
	private Date endDate;
	
	//类型2
	@Column(name="TYPE2")
	private Integer type2;//-1:不能进行排课和排考	0:排课和排考都行	1:只能排课	2:只能排考
	
	//开始时间2
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="STARTDATE2")
	private Date startDate2;
	
	//结束时间2
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="ENDDATE2")
	private Date endDate2;
	
	//类型3
	@Column(name="TYPE3")
	private Integer type3;//-1:不能进行排课和排考	0:排课和排考都行	1:只能排课	2:只能排考
	
	//开始时间3
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="STARTDATE3")
	private Date startDate3;
	
	//结束时间3
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="ENDDATE3")
	private Date endDate3;
	
	public SchoolCalendar getSchoolCalendar() {
		return schoolCalendar;
	}

	public void setSchoolCalendar(SchoolCalendar schoolCalendar) {
		this.schoolCalendar = schoolCalendar;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
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

	public Integer getType2() {
		return type2;
	}

	public void setType2(Integer type2) {
		this.type2 = type2;
	}

	public Date getStartDate2() {
		return startDate2;
	}

	public void setStartDate2(Date startDate2) {
		this.startDate2 = startDate2;
	}

	public Date getEndDate2() {
		return endDate2;
	}

	public void setEndDate2(Date endDate2) {
		this.endDate2 = endDate2;
	}

	public Integer getType3() {
		return type3;
	}

	public void setType3(Integer type3) {
		this.type3 = type3;
	}

	public Date getStartDate3() {
		return startDate3;
	}

	public void setStartDate3(Date startDate3) {
		this.startDate3 = startDate3;
	}

	public Date getEndDate3() {
		return endDate3;
	}

	public void setEndDate3(Date endDate3) {
		this.endDate3 = endDate3;
	}
	
}
