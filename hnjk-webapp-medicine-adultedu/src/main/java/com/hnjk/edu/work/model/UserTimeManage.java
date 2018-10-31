package com.hnjk.edu.work.model;

import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.edu.basedata.model.YearInfo;
import com.hnjk.security.model.OrgUnit;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;

/**
 * function : 学生工作时间管理[实体类]
 * <p>author : msl
 * <p>date   : 2018/7/19
 * <p>description : EDU_WORK_USERTIME
 */

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "EDU_WORK_USERTIME")
@Cache(usage= CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UserTimeManage extends BaseModel {

	/**
	 * 教学点
	 */
	@OneToOne(optional = false, cascade = { CascadeType.MERGE, CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "UNITID",nullable=false)
	private OrgUnit unit;

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
	 * 活动类型
	 * 数据字典Code.WorkManage.workType :1-招新、2-年度评优
	 */
	@Column
	private String workType;

	/**
	 * 活动开始时间
	 */
	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Date startTime;

	/**
	 * 活动结束时间
	 */
	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Date endTime;

	/**
	 * 竞选时间
	 */
	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Date joinTime;

	/**
	 * 活动地点
	 */
	@Column
	private String workPlace;

}
