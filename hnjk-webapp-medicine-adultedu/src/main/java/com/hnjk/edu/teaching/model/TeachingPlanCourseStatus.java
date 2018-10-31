package com.hnjk.edu.teaching.model;

import com.hnjk.core.support.base.model.BaseModel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 年级教学计划课程开课表
 * @author marco
 *
 */
@Entity
@Table(name = "EDU_TEACH_COURSESTATUS")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class TeachingPlanCourseStatus extends BaseModel implements Serializable{

	private static final long serialVersionUID = 3089193981557501049L;

	/**
	 * 所属年级教学计划
	 */
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,	CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "GUIPLANID")
	private TeachingGuidePlan teachingGuidePlan;

	/**
	 * 教学计划课程
	 */
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,	CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "PLANCOURSEID")
	private TeachingPlanCourse teachingPlanCourse;

	/**
	 * 是否开课  开课状态为开课和审核通过的才是真正的开课
	 */
	@Column(name="ISOPEN",length=1)
	private String isOpen;

	/**
	 * 开课学期
	 */
	@Column(name="TERM")
	private String term;

	/**
	 * 审核状态
	 * 开课通过/开课不通过/开课待审核   openY/openN/openW
	 * 取消开课通过/取消开课不通过/取消开课待审核  cancelY/cancelN/cancelW
	 * 调整开课通过/调整开课不通过/调整开课待审核	updateY/updateN/updateW
	 */
	@Column(name="CHECKSTATUS")
	private String checkStatus;

	/**
	 * 开课状态   Y：开课 N：不开课
	 */
	@Column(name="OPENSTATUS")
	private String openStatus;

	/**
	 * 开课教学点名称
	 */
	@Column(name="SCHOOLNAME")
	private String schoolName;

	/**
	 * 开课教学点id
	 */
	@Column(name="SCHOOLIDS")
	private String schoolIds;

	/**
	 * 字典：CodeCourseTeachType，课程类型：面授课程-faceTeach, 网络课程-networkTeach,默认面授
	 */
	@Column(name="TEACHTYPE")
	private String teachType = "faceTeach";

	/**
	 * 字典：CodeExamForm，课程考试形式：网上考试-0, 线下考试-1，网上+线下-2，机考-3，,默认线下考试
	 */
	@Column(name="EXAMFORM")
	private Integer examForm = 1;

	/**
	 * 教学计划课程id
	 */
	@Transient
	private String tpcid;

	/**
	 * 调整之前的学期
	 */
	@Transient
	private String afterterm;

}
