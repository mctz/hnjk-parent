package com.hnjk.edu.arrange.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.security.model.User;

/**
 * 教学意愿
 */
@Entity
@Table(name="EDU_ARRANGE_WILLINGNESS")
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TeachingWillingness extends BaseModel {
	
	//教学班
	@ManyToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "TEACHCOURSEID",nullable=false)	
	private TeachCourse teachCourse;//
	
	/*// 申请人
	@OneToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "edu_arrange_willusers", joinColumns = { @JoinColumn(name = "willId") }, inverseJoinColumns = { @JoinColumn(name = "userId") })
	private Set<User> proposer = new LinkedHashSet<User>(0);*/
	
	// 申请人
	@OneToOne(optional = true, cascade = { CascadeType.MERGE, CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "PROPOSERID")
	private User proposer;
	
	// 课室类型
	@Column(name = "CLASSROOMTYPE")
	private String classroomType;
	
	@Column(name="DAYS")
	private String days;//星期几字符串
	
	@Column(name="TIMEPERIODIDS")
	private String timePeriodids;//时间段字符串
	
	@Column(name="TIMEPERIODNAMES")
	private String timePeriodNames;//时间段名字字符串
	
	//意愿信息
	@Column(name="INFO")
	private String info;
	
	// 操作人
	@OneToOne(optional = true, cascade = { CascadeType.MERGE, CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "OPERATORID")
	private User operator;
	
	// 创建时间
	@Column(name="CREATEDATE", updatable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;

	public TeachCourse getTeachCourse() {
		return teachCourse;
	}

	public void setTeachCourse(TeachCourse teachCourse) {
		this.teachCourse = teachCourse;
	}
	public User getProposer() {
		return proposer;
	}

	public void setProposer(User proposer) {
		this.proposer = proposer;
	}

	public String getClassroomType() {
		return classroomType;
	}

	public void setClassroomType(String classroomType) {
		this.classroomType = classroomType;
	}
	
	public String getDays() {
		return days;
	}

	public void setDays(String days) {
		this.days = days;
	}

	public String getDaysName() {
		return ExStringUtils.getDaysName(days);
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

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public User getOperator() {
		return operator;
	}

	public void setOperator(User operator) {
		this.operator = operator;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
}
