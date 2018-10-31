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

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.security.model.OrgUnit;

/**
 * 补预约设置表.
 * <code>ReOrderSetting</code><p>
 * 
 * @author：广东学苑教育发展有限公司.
 * @since： 2011-11-22 下午04:19:43
 * @see 
 * @version 1.0
 */
@Entity
@Table(name="EDU_TEACH_REORDERSETTING")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ReOrderSetting extends BaseModel{

	private static final long serialVersionUID = 2600671598861971807L;
	
	@Column(name="REORDERTYPE",length=50,nullable=false)
	private String reOrderType;//预约类型  1预约学习 2预约考试
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "ORDERCOURSESETTINGID")
	private OrderCourseSetting orderCourseSetting;//预约学习批次
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "EXAMSUBID")
	private ExamSub examSub;//预约考试批次
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "BRSCHOOLID")
	private OrgUnit brSchool;//学习中心
	
	@Column(name="STARTTIME",nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date startTime;//开始时间
	
	@Column(name="ENDTIME",nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date endTime;//结束时间
	
	@Column(name="FILLINMAN")
	private String fillinMan;//填写人
	
	@Column(name="FILLINMANID")
	private String fillinManId;//填写人ID
	
	@Column(name="FILLINDATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date fillinDate;//填写日期

	/**
	 * @return the reOrderType
	 */
	public String getReOrderType() {
		return reOrderType;
	}

	/**
	 * @param reOrderType the reOrderType to set
	 */
	public void setReOrderType(String reOrderType) {
		this.reOrderType = reOrderType;
	}

	/**
	 * @return the orderCourseSetting
	 */
	public OrderCourseSetting getOrderCourseSetting() {
		return orderCourseSetting;
	}

	/**
	 * @param orderCourseSetting the orderCourseSetting to set
	 */
	public void setOrderCourseSetting(OrderCourseSetting orderCourseSetting) {
		this.orderCourseSetting = orderCourseSetting;
	}

	/**
	 * @return the examSub
	 */
	public ExamSub getExamSub() {
		return examSub;
	}

	/**
	 * @param examSub the examSub to set
	 */
	public void setExamSub(ExamSub examSub) {
		this.examSub = examSub;
	}

	/**
	 * @return the brSchool
	 */
	public OrgUnit getBrSchool() {
		return brSchool;
	}

	/**
	 * @param brSchool the brSchool to set
	 */
	public void setBrSchool(OrgUnit brSchool) {
		this.brSchool = brSchool;
	}

	/**
	 * @return the startTime
	 */
	public Date getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the endTime
	 */
	public Date getEndTime() {
		return endTime;
	}

	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	/**
	 * @return the fillinMan
	 */
	public String getFillinMan() {
		return fillinMan;
	}

	/**
	 * @param fillinMan the fillinMan to set
	 */
	public void setFillinMan(String fillinMan) {
		this.fillinMan = fillinMan;
	}

	/**
	 * @return the fillinManId
	 */
	public String getFillinManId() {
		return fillinManId;
	}

	/**
	 * @param fillinManId the fillinManId to set
	 */
	public void setFillinManId(String fillinManId) {
		this.fillinManId = fillinManId;
	}

	/**
	 * @return the fillinDate
	 */
	public Date getFillinDate() {
		return fillinDate;
	}

	/**
	 * @param fillinDate the fillinDate to set
	 */
	public void setFillinDate(Date fillinDate) {
		this.fillinDate = fillinDate;
	}
	
	
}
