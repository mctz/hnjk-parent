package com.hnjk.edu.teaching.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Where;

import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.basedata.model.YearInfo;

/**
 * 学生预约情况统计表，主要用来生成教学任务书。 <code>CourseOrderStat</code>
 * <p>
 * 统计表的数据来自：1）新生预约：默认为教学计划的第一学期所有课程
 * 2）老生预约：学生预约课程同时更新统计表，如果学生预约的课程在统计表中不存在，则插入一条，否则，更新统计人数
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-7-13 下午03:01:11
 * @see
 * @version 1.0
 */
@Entity
@Table(name = "EDU_TEACH_COURSESTAT")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CourseOrderStat extends BaseModel {

	private static final long serialVersionUID = -1580481451874945653L;

	@OneToOne(optional = true, cascade = { CascadeType.MERGE,
			CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "YEARID")
	private YearInfo yearInfo;// 所在年度

	@Column(name = "TERM")
	private String term;// 所在学期 取自数据字典

	@OneToOne(optional = true, cascade = { CascadeType.MERGE,
			CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "COURSEID")
	private Course course;// 学生预约的课程

	
	@Column(name = "ORDERNUM")
	private Integer orderNum;// 本课程学生预约人数

	@Column(name = "FLAG")
	private String generatorFlag = Constants.BOOLEAN_NO;//是否生成任务书
	

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "courseOrderStat")
	@org.hibernate.annotations.Cascade(value = {org.hibernate.annotations.CascadeType.ALL,org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@OrderBy("showOrder ASC")
	@Where(clause = "isDeleted=0")
	private Set<CourseOrder> courseOrders = new HashSet<CourseOrder>(0);// 学生预约明细表

		
	public Set<CourseOrder> getCourseOrders() {
		return courseOrders;
	}

	public void setCourseOrders(Set<CourseOrder> courseOrders) {
		this.courseOrders = courseOrders;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public YearInfo getYearInfo() {
		return yearInfo;
	}

	public void setYearInfo(YearInfo yearInfo) {
		this.yearInfo = yearInfo;
	}

	public Integer getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

	public String getGeneratorFlag() {
		return ExStringUtils.isNotEmpty(generatorFlag)?generatorFlag.trim():"";
	}

	public void setGeneratorFlag(String generatorFlag) {
		this.generatorFlag = generatorFlag;
	}

}
