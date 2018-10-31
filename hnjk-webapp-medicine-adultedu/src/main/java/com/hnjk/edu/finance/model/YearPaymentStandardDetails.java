package com.hnjk.edu.finance.model;

import com.hnjk.core.support.base.model.BaseModel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Date;

/**
 * 年度缴费标准明细
 * @author hzg
 *
 */
@Entity
@Table(name = "EDU_FEE_PAYYEARDETAILS")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class YearPaymentStandardDetails extends BaseModel {

	private static final long serialVersionUID = -3770023338654233452L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PAYYEARID")
	private YearPaymentStandard yearPaymentStandard;//所属缴费标准
	
	@Column(name="FEETERM")
	private Integer feeTerm;//所属缴费期
	
	@Column(name="CREDITFEE",scale=2)
	private Double creditFee;//缴费金额
	
	@Column(name="CREDITENDDATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date creditEndDate;//截止缴费日期
	
	@Column(name="SHOWORDER")
	private Integer showOrder;//排序号
	
	/**2015/11/19新增开始缴费日期字段**/
	@Column(name="PAYBEGINDATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date payBeginDate;// 开始缴费日期
	
	@Transient
	private Double totalRecpayFee;// 总的应缴金额
	
}
