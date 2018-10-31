package com.hnjk.edu.finance.model;

import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.edu.basedata.model.YearInfo;
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
 * Function : 已返学费金额 - 实体类
 * <p>Author : msl
 * <p>Date   : 2018-08-22
 * <p>Description :
 */
@Entity
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@Table(name = "EDU_FEE_RETURNAMOUNTS")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ReturnAmounts extends BaseModel implements Serializable {

	@OneToOne(cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "UNITID")
	private OrgUnit unit;

	/**
	 * 所属缴费年度
	 */
	@OneToOne(cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "YEARID")
	private YearInfo yearInfo;

	/**
	 * 次数（1，2）
	 */
	@Column
	private Integer count;

	/**
	 * 已返金额
	 */
	@Column
	private Double amounts;

	/**
	 * 操作日期
	 */
	@Column
	private Date operateDate;
}
