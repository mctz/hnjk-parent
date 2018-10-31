package com.hnjk.edu.finance.model;

import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.edu.basedata.model.Grade;
import com.hnjk.edu.basedata.model.YearInfo;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 年度缴费标准
 * @author hzg
 *
 */
@Entity
@Table(name = "EDU_FEE_PAYYEAR")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class YearPaymentStandard extends BaseModel{
	
	private static final long serialVersionUID = -7879399556842460780L;

	@Column(name="PAYNAME",nullable=false)
	private String standerdName;//标准名称
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "YEARID")
	private YearInfo yearInfo;//所属年度
	
	@Column(name="PAYMENTTYPE",nullable=false)
	private String paymentType;//缴费类型
	
	@Column(name="MEMO")
	private String memo;//备注
	
	/** 2015-11-24 新增年级字段 **/
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "GRADEID")
	private Grade grade;//所属年级
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "yearPaymentStandard")
	@org.hibernate.annotations.Cascade(value={org.hibernate.annotations.CascadeType.DELETE_ORPHAN,org.hibernate.annotations.CascadeType.ALL})
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@OrderBy("showOrder ASC")
	@Where(clause = "isDeleted=0")
	private Set<YearPaymentStandardDetails> yearPaymentStandardDetails = new LinkedHashSet<YearPaymentStandardDetails>(0);//标准明细
	
}
