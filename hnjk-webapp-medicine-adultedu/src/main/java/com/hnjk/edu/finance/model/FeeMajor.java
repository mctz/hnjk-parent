package com.hnjk.edu.finance.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Where;

import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.edu.basedata.model.Major;

/**
 * 课程缴费类别
 * @author hzg
 *
 */
@Entity
@Table(name="EDU_FEE_MAJOR")
//@PrimaryKeyJoinColumn(name = "BASEMAJORID")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
//public class FeeMajor extends Major{
public class FeeMajor extends BaseModel implements Serializable {
 
	private static final long serialVersionUID = -5556797352097091470L;
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE, CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "BASEMAJORID")	
	@Where(clause="isDeleted=0")
	private Major major;
	
	@Column(name="PAYMENTTYPE")
	private String paymentType;//缴费类别:字典值
	
	@Column(name="TEACHINGTYPE")
	private String teachingType;// 学习形式，目前只应用于广西医

	@Transient
	private String majorId;
	
	/**
	 * @return the paymentType
	 */
	public String getPaymentType() {
		return paymentType;
	}

	/**
	 * @param paymentType the paymentType to set
	 */
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	
	
}
