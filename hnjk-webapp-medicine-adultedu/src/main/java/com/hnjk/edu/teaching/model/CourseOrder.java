package com.hnjk.edu.teaching.model;

// default package

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

import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.YearInfo;
import com.hnjk.edu.roll.model.StudentInfo;

/**
 * 学生预约课程表
 * <code>CourseOrder</code>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-6-21 下午01:40:18
 * @see 
 * @version 1.0
 */
@Entity
@Table(name="EDU_TEACH_ORDERCOURSE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CourseOrder   extends BaseModel {

	private static final long serialVersionUID = 325787544268337706L;
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDENTID")
	private StudentInfo studentInfo;// 学生学籍
	
    
    @Column(name="STATUS")    
    private Long status;//学习状态 1-预约学习 2-预约考试
    
    @Column(name="SHOWORDER")
    private Integer showOrder;//排序号

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COURSESTATID")
    private CourseOrderStat courseOrderStat;//所属预约统计表
    
	/**3.1.9 新增预约学习/考试时间、年度、学期，用于统计*/
	@Column(name="ORDERCOURSETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date orderCourseTime ;	//预约学习时间
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "ORDERCOURSEYEAR")
	private YearInfo orderCourseYear;//预约学习所在年度
	
	@Column(name="ORDERCOURSETERM")
	private String orderCourseTerm;//预约学习所在学期
	
	@Column(name="ORDEREXAMTIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date orderExamTime;//预约考试时间
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "ORDEREXAMYEAR")
	private YearInfo orderExamYear;//预约考试所在年度
	
	@Column(name="ORDEREXAMTERM")
	private String orderExamTerm;//预约考试所在学期
	
	/**3.1.11 新增是否机考*/
	@Column(name="ISMACHINEEXAM")
	private String isMachineExam = Constants.BOOLEAN_NO;//是否机考,安排机考后需要更新这个字段

	
	
	/**
	 * @return the isMachineExam
	 */
	public String getIsMachineExam() {
		return isMachineExam;
	}

	/**
	 * @param isMachineExam the isMachineExam to set
	 */
	public void setIsMachineExam(String isMachineExam) {
		this.isMachineExam = isMachineExam;
	}

	/**
	 * @return the orderCourseTime
	 */
	public Date getOrderCourseTime() {
		return orderCourseTime;
	}

	/**
	 * @param orderCourseTime the orderCourseTime to set
	 */
	public void setOrderCourseTime(Date orderCourseTime) {
		this.orderCourseTime = orderCourseTime;
	}

	/**
	 * @return the orderCourseYear
	 */
	public YearInfo getOrderCourseYear() {
		return orderCourseYear;
	}

	/**
	 * @param orderCourseYear the orderCourseYear to set
	 */
	public void setOrderCourseYear(YearInfo orderCourseYear) {
		this.orderCourseYear = orderCourseYear;
	}

	/**
	 * @return the orderCourseTerm
	 */
	public String getOrderCourseTerm() {
		return orderCourseTerm;
	}

	/**
	 * @param orderCourseTerm the orderCourseTerm to set
	 */
	public void setOrderCourseTerm(String orderCourseTerm) {
		this.orderCourseTerm = orderCourseTerm;
	}

	/**
	 * @return the orderExamTime
	 */
	public Date getOrderExamTime() {
		return orderExamTime;
	}

	/**
	 * @param orderExamTime the orderExamTime to set
	 */
	public void setOrderExamTime(Date orderExamTime) {
		this.orderExamTime = orderExamTime;
	}

	/**
	 * @return the orderExamYear
	 */
	public YearInfo getOrderExamYear() {
		return orderExamYear;
	}

	/**
	 * @param orderExamYear the orderExamYear to set
	 */
	public void setOrderExamYear(YearInfo orderExamYear) {
		this.orderExamYear = orderExamYear;
	}

	/**
	 * @return the orderExamTerm
	 */
	public String getOrderExamTerm() {
		return orderExamTerm;
	}

	/**
	 * @param orderExamTerm the orderExamTerm to set
	 */
	public void setOrderExamTerm(String orderExamTerm) {
		this.orderExamTerm = orderExamTerm;
	}

	public Integer getShowOrder() {
		return showOrder;
	}

	public void setShowOrder(Integer showOrder) {
		this.showOrder = showOrder;
	}

	public CourseOrderStat getCourseOrderStat() {
		return courseOrderStat;
	}

	public void setCourseOrderStat(CourseOrderStat courseOrderStat) {
		this.courseOrderStat = courseOrderStat;
	}

	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public StudentInfo getStudentInfo() {
		return studentInfo;
	}

	public void setStudentInfo(StudentInfo studentInfo) {
		this.studentInfo = studentInfo;
	}

}