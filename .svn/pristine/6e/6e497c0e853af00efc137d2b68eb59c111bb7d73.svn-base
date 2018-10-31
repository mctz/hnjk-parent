package com.hnjk.edu.teaching.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.edu.basedata.model.Course;

/**
 * 考试计划设置明细表
 * <code>ExamSettingDetails</code><p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-8-16 上午11:30:37
 * @see 
 * @version 1.0
 */
@Entity
@Table(name="EDU_TEACH_EXAMCOURSES")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ExamSettingDetails  extends BaseModel{

	private static final long serialVersionUID = 8199255139822569130L;
	
	@Override
	public String getResourceid() {
		return super.getResourceid();
	}

	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
 	@JoinColumn(name = "COURSEID") 
	private Course courseId;//课程ID
	
	
	@Column(name="COURSENAME",nullable=false)
	private String courseName;//课程名称
	
	@Column(name="SHOWORDER")
	private Integer showOrder;//排序号
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SETTINGID")
	private ExamSetting examSetting;//所属考试计划设置
	
	/**3.1.1 新增是否特殊批次字段*/
	@Column(name="ISSPECIAL")
	private String isSpecial;//是否特殊批次
	
	
	public String getIsSpecial() {
		return isSpecial;
	}

	public void setIsSpecial(String isSpecial) {
		this.isSpecial = isSpecial;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public ExamSetting getExamSetting() {
		return examSetting;
	}

	public void setExamSetting(ExamSetting examSetting) {
		this.examSetting = examSetting;
	}

	public Integer getShowOrder() {
		return showOrder;
	}

	public void setShowOrder(Integer showOrder) {
		this.showOrder = showOrder;
	}

	public Course getCourseId() {
		return courseId;
	}

	public void setCourseId(Course courseId) {
		this.courseId = courseId;
	}
	
	
}
