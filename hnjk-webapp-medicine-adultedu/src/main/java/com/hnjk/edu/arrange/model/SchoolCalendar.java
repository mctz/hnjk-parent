package com.hnjk.edu.arrange.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Where;

import com.hnjk.core.beans.DatePeriod;
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.edu.basedata.model.YearInfo;
import com.hnjk.security.model.OrgUnit;

/**
 * 院历
 */
@Entity
@Table(name="EDU_ARRANGE_SCHOOLCALENDAR")
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SchoolCalendar extends SuperBaseModel {

	private static final long serialVersionUID = 6226830433926416297L;

	/**院历名字**/
	@Column(name="NAME")
	private String name;
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "YEARID",nullable=false)	
	private YearInfo yearInfo;//年度
	
	@Column(name="TERM",nullable=false)
	private String term;//学期
	
	@OneToOne(optional = false, cascade = { CascadeType.MERGE, CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "UNITID",nullable=false)
	private OrgUnit unit;//教学点
	
	/**每周第一天**/
	@Column(name="FIRSTDAY")
	private Integer firstDay;
	
	/**周数**/
	@Column(name="WEEKS")
	private Integer weeks;
	
	/**毕业教学周数**/
	@Column(name="GRADUATEWEEKS")
	private Integer graduateWeeks;
	
	/**学期开始时间**/
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="termStartDate")
	private Date termStartDate;
	
	/**学期结束时间**/
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="termEndDate")
	private Date termEndDate;
	
	/**教学周开始时间**/
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="STARTDATE")
	private Date startDate;
	
	/**教学周结束时间**/
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="ENDDATE")
	private Date endDate;
	
	/**毕业教学周开始时间**/
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="graduateStartDate")
	private Date graduateStartDate;
	
	/**毕业教学周结束时间**/
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="graduateEndDate")
	private Date graduateEndDate;
	
	/**发布状态**/
	@Column(name="STATUS")
	private Integer status;//CodePublishStatus 0：待发布；1：已发布；2：未生成
	
	@Column(name="isUsed")
	private String isUsed;//是否在排课中使用 Y/N
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "schoolCalendar")
	@org.hibernate.annotations.Cascade(value={org.hibernate.annotations.CascadeType.ALL,org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@Where(clause="isDeleted=0")
	private Set<CalendarEvent> calendarEvents = new HashSet<CalendarEvent>(0);//事件
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public YearInfo getYearInfo() {
		return yearInfo;
	}

	public void setYearInfo(YearInfo yearInfo) {
		this.yearInfo = yearInfo;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public Integer getFirstDay() {
		return firstDay;
	}

	public void setFirstDay(Integer firstDay) {
		this.firstDay = firstDay;
	}

	public Integer getWeeks() {
		return weeks;
	}

	public void setWeeks(Integer weeks) {
		this.weeks = weeks;
	}

	public Integer getGraduateWeeks() {
		return graduateWeeks;
	}

	public void setGraduateWeeks(Integer graduateWeeks) {
		this.graduateWeeks = graduateWeeks;
	}

	public Date getTermStartDate() {
		return termStartDate;
	}

	public void setTermStartDate(Date termStartDate) {
		this.termStartDate = termStartDate;
	}

	public Date getTermEndDate() {
		return termEndDate;
	}

	public void setTermEndDate(Date termEndDate) {
		this.termEndDate = termEndDate;
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

	public Date getGraduateStartDate() {
		return graduateStartDate;
	}

	public void setGraduateStartDate(Date graduateStartDate) {
		this.graduateStartDate = graduateStartDate;
	}

	public Date getGraduateEndDate() {
		return graduateEndDate;
	}

	public void setGraduateEndDate(Date graduateEndDate) {
		this.graduateEndDate = graduateEndDate;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public OrgUnit getUnit() {
		return unit;
	}

	public void setUnit(OrgUnit unit) {
		this.unit = unit;
	}

	public Set<CalendarEvent> getCalendarEvents() {
		return calendarEvents;
	}

	public void setCalendarEvents(Set<CalendarEvent> calendarEvents) {
		this.calendarEvents = calendarEvents;
	}

	public String getIsUsed() {
		return isUsed;
	}

	public void setIsUsed(String isUsed) {
		this.isUsed = isUsed;
	}

	/**
	 * 根据类型获取院历事件的可用时间段
	 * @param types 包含types类型时间段
	 * @param _types 不包含_types类型时间段
	 * -1:不能进行排课和排考	0:排课和排考都行	1:只能排课	2:只能排考
	 * @return
	 */
	public List<DatePeriod> getDatePeriodBytype(String[]types,String[]_types) {
		List<DatePeriod> tPeriodList = new ArrayList<DatePeriod>();
		if(calendarEvents!=null){
			DatePeriod datePeriod = null;
			List<DatePeriod> datePeriods = new ArrayList<DatePeriod>();
			List<DatePeriod> _datePeriods = new ArrayList<DatePeriod>();
			List<String> typeList = new ArrayList<String>();
			for (String type : types) {
				typeList.add(type);
			}
			List<String> _typeList = new ArrayList<String>();
			for (String type : _types) {
				_typeList.add(type);
			}
			for (CalendarEvent calendarEvent : calendarEvents) {
				if(calendarEvent.getStartDate()!=null && calendarEvent.getEndDate()!=null){
					datePeriod = new DatePeriod();
					datePeriod.setStartDate(calendarEvent.getStartDate());
					datePeriod.setEndDate(calendarEvent.getEndDate());
					if(typeList.contains(calendarEvent.getType().toString())){
						datePeriods.add(datePeriod);
					}
					if(_typeList.contains(calendarEvent.getType().toString())){
						_datePeriods.add(datePeriod);
					}
				}
				if(calendarEvent.getStartDate2()!=null && calendarEvent.getEndDate2()!=null){
					datePeriod = new DatePeriod();
					datePeriod.setStartDate(calendarEvent.getStartDate2());
					datePeriod.setEndDate(calendarEvent.getEndDate2());
					if(typeList.contains(calendarEvent.getType2().toString())){
						datePeriods.add(datePeriod);
					}
					if(_typeList.contains(calendarEvent.getType2().toString())){
						_datePeriods.add(datePeriod);
					}
				}
				if(calendarEvent.getStartDate3()!=null && calendarEvent.getEndDate3()!=null){
					datePeriod = new DatePeriod();
					datePeriod.setStartDate(calendarEvent.getStartDate3());
					datePeriod.setEndDate(calendarEvent.getEndDate3());
					if(typeList.contains(calendarEvent.getType3().toString())){
						datePeriods.add(datePeriod);
					}
					if(_typeList.contains(calendarEvent.getType3().toString())){
						_datePeriods.add(datePeriod);
					}
				}
			}
			for (DatePeriod dp : datePeriods) {
				if(_datePeriods.size()>0){
					for (DatePeriod _dp : _datePeriods) {
						tPeriodList.addAll(ExDateUtils.date4Subtraction(dp, _dp));
					}
				}else {
					tPeriodList = datePeriods;
				}
				
			}
		}
		return tPeriodList;
	}

	/**
	 * 根据院历和时间段获取教学周
	 * @param calendar 院历
	 * @param timeperiodList 时间段
	 * @return Map
	 */
	public Map<String,String> getWeeksInDatePeriodsBystartDate(SchoolCalendar calendar,List<DatePeriod> timeperiodList) {
		Map<String,String> map = new HashMap<String, String>();
		int firstDay = calendar.getFirstDay();
		Date startDate = calendar.getStartDate();
		try {
			String dateStr = ExDateUtils.formatDateStr(startDate,"yyyy-MM-dd");
			dateStr = ExDateUtils.getWeekDay(dateStr,firstDay+1,1);//第一天上课日期
			String weeks = "";
			for (DatePeriod datePeriod : timeperiodList) {
				long days1 = 0;
				long days2 = 0;
				if(datePeriod.getStartDate()!=null && datePeriod.getEndDate()!=null){
					days1 = ExDateUtils.getDateDiffNum(dateStr, ExDateUtils.formatDateStr(datePeriod.getStartDate(),"yyyy-MM-dd"));
					days2 = ExDateUtils.getDateDiffNum(dateStr, ExDateUtils.formatDateStr(datePeriod.getEndDate(),"yyyy-MM-dd"));
					int week1 = (int) (days1/7);//起始周
					int week2 = (int) (days2/7);//结束周(不包括最后一周)
					//for (int i = 0; i < week2-week1 && days2-days1>6; i++) {
					for (int i = 0; i <= week2-week1; i++) {
						weeks += i+week1+1+",";
					}
				}
			}
			if(ExStringUtils.isNotEmpty(weeks)){
				weeks = weeks.substring(0, weeks.length()-1);
				for (int k = 1; k <= 7; k++) {
					map.put(k+"", weeks);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}
}
