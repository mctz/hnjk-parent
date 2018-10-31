package com.hnjk.edu.teaching.model;

import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.platform.taglib.JstlCustomFunction;
import com.hnjk.security.model.OrgUnit;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 学习中心时间段
 * @author Git
 *
 */
@Entity
@Table(name = "EDU_TEACH_TIMEPERIOD")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class TeachingPlanCourseTimePeriod extends BaseModel implements Serializable{

	private static final long serialVersionUID = 6058347806353887640L;

	@OneToOne(cascade={CascadeType.MERGE,CascadeType.PERSIST},fetch=FetchType.LAZY)
	@JoinColumn(name="ORGUNITID")
	private OrgUnit brSchool;//关联学习中心
	
	@Column(name="TIMEPERIOD")
	private String timePeriod;//时间段
	
	@Column(name="TIMENAME")
	private String timeName;//时间段名称	

	@Column(name="STARTTIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date startTime;//开始时间
	
	@Column(name="ENDTIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date endTime;//结束时间
	
	@Column(name = "STYDYHOUR")
	private Integer stydyHour;// 学时
	
	@Transient
	private String courseTimeName;

	public String getCourseTimeName() {
		String courseTimeName = JstlCustomFunction.dictionaryCode2Value("CodeCourseTimePeriod", this.timePeriod)+this.timeName;
		try {
			courseTimeName += ExDateUtils.formatDateStr(this.startTime, "HH:mm")+"-"+ExDateUtils.formatDateStr(this.endTime, "HH:mm");
		} catch (Exception e) {
		}
		return courseTimeName;
	}
}
