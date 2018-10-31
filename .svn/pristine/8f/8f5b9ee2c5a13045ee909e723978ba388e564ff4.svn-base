package com.hnjk.edu.work.model;

import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.edu.basedata.model.YearInfo;
import com.hnjk.edu.roll.model.StudentInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Where;

import javax.persistence.*;

/**
 * Function : 学生评优信息[实体类]
 * <p>Author : Administrator
 * <p>Date   : 2018-07-23
 * <p>Description :EDU_WORK_APPRAISING
 */

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "EDU_WORK_APPRAISING")
@Cache(usage= CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Appraising extends BaseModel {

	/**
	 * 年度
	 */
	@OneToOne(optional = true, cascade = { CascadeType.MERGE, CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "YEARID")
	@Where(clause="isDeleted=0")
	private YearInfo yearInfo;

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
	 * 评优类型
	 * <p>数据字典Code.WorkManage.appraisingType :年度优秀学生、优秀毕业生
	 */
	@Column
	private String type;

	/**
	 * 成绩平均分[非统考]
	 */
	@Column
	private String avgScore;

	/**
	 * 统考课程情况
	 */
	@Column
	private String courseCondition;

	/**
	 * 是否班干部
	 */
	@Column
	private String isClassLeader;

	/**
	 * 审核状态
	 * <p>数据字典 CodeAuditStatus2：W:待审核、R;待复审、Y:审核通过、N:审核不通过
	 */
	@Column
	private String auditStatus;

	/**
	 * 自我鉴定
	 */
	@Lob
	@Column(name = "SELFASSESSMENT",length = 3000)
	private String selfAssessment;

	/**
	 * 备注
	 */
	@Column
	private String memo;

	@Transient
	private String errorMessage;

	/**
	 * 序号
	 */
	@Transient int order;
}
