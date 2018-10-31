package com.hnjk.edu.finance.model;

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

import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.edu.basedata.model.Major;
import com.hnjk.edu.basedata.model.YearInfo;

/** 
 * 年教材费标准
 * 
 * @author Zik, 广东学苑教育发展有限公司
 * @since 2018年7月31日 下午3:34:22 
 * 
 */
@Entity
@Table(name = "EDU_FEE_TEXTBOOKFEE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class TextbookFee extends BaseModel {

	private static final long serialVersionUID = 6377784915743097344L;
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "YEARID")
	private YearInfo yearInfo;// 年级
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "MAJORID")
	private Major major;// 专业
	
	@Column(name="MONEY",scale=2)
	private Double money;// 金额
	
	@Transient
	private String yearId;
	
	@Transient
	private String majorId;

}
