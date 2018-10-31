package com.hnjk.edu.work.model;

import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.edu.basedata.model.YearInfo;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.security.model.OrgUnit;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;

/**
 * function : 学生干部信息[实体类]
 * <p>author : msl
 * <p>date   : 2018/7/19
 * <p>description : EDU_WORK_CADREINFO
 */

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "EDU_WORK_CADREINFO")
@Cache(usage= CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CadreInfo extends BaseModel {

	/**
	 * 年度
	 */
	@OneToOne(optional = true, cascade = { CascadeType.MERGE, CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "YEARID")
	@Where(clause="isDeleted=0")
	private YearInfo yearInfo;

	/**
	 * 学期
	 */
	@Column
	private String term;

	/**
	 * 学号
	 */
	@Column
	private String studyNo;

	/**
	 * 姓名
	 */
	@Column
	private String studentName;

	/**
	 * 学籍信息
	 */
	@OneToOne(optional = false, cascade = { CascadeType.MERGE, CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDENTID",nullable=false)
	@Where(clause = "isDeleted=0")
	private StudentInfo studentInfo;

	/**
	 * 竞选部门
	 * <p>数据字典Code.WorkManage.department
	 */
	@Column
	private String department;

	/**
	 * 竞选职位
	 * <p>数据字典Code.WorkManage.position
	 */
	@Column
	private String position;

	/**
	 * 在校获奖情况
	 */
	@Column(length = 500)
	private String awards;

	/**
	 * 主要工作经历
	 */
	@Column(length = 500)
	private String workExperience;

	/**
	 * 对竞聘职务认识及工作设想
	 */
	@Column(length = 1000)
	private String intention;

	/**
	 * 第一学期平均分
	 */
	@Column
	private String avgScore1;

	/**
	 * 第二学期平均分
	 */
	@Column
	private String avgScore2;

	/**
	 * 现任部门
	 * <p>数据字典Code.WorkManage.department
	 */
	@Column
	private String department_current;

	/**
	 * 现任职位
	 * <p>数据字典Code.WorkManage.position
	 */
	@Column
	private String position_current;

	/**
	 * 调剂职位
	 * <p>数据字典Code.WorkManage.position
	 */
	@Column
	private String position_adjust;

	/**
	 * 是否候选人
	 * <p>数据字典yesOrNo_default
	 */
	@Column
	private String isCandidate;

	/**
	 * 是否任用
	 * <p>数据字典yesOrNo_default
	 */
	@Column
	private String isAppoint;

	/**
	 * 任职时间
	 */
	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Date jobTime;

	/**
	 * 组织
	 * <p>数据字典Code.WorkManage.organization :总会、分会
	 */
	@Column
	private String organization;

	/**
	 * 职位状态
	 * <p>数据字典Code.WorkManage.positionStatus: 在职、停职、离职
	 */
	@Column
	private String status;

	/**
	 * 备注
	 */
	@Column
	private String memo;

}
