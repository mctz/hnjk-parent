package com.hnjk.edu.finance.model;

import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.edu.recruit.model.RecruitMajor;
import com.hnjk.security.model.OrgUnit;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

/**
 * 学费优惠表
 * @author hzg
 *
 */
@Entity
@Table(name = "EDU_FEE_PRIVILEGE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class PaymentFeePrivilege extends BaseModel {

	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "BRSCHOLLID")
	private OrgUnit brSchool;//关联学习中心，可针对学校中心优惠，即该学习中心下所有学生都采用
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "RECRUITMAJORID")
	private RecruitMajor recruitMajor;//关联招生专业，可针对招生专业设置优惠，即该学习中心该专业的学生采用，以上优惠不能重叠
	
	@Column(name="BEFOREPRIVILEGEFEE",scale=2)
	private Double beforePrivilegeFee;//优惠前每学分学费
	
	@Column(name="AFTERPRIVILEGEFEE",scale=2)
	private Double afterPrivilegeFee;//优惠后每学分学费
	
	@Column(name="TOTALPRIVILEGEFEE",scale=2)
	private Double totalPrivilegeFee;//优惠总额
	
	@Column(name="MEMO")
	private String memo;//备注

}
