package com.hnjk.edu.finance.model;

import com.hnjk.core.support.base.model.BaseModel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 学生缴费设置-缴费明细。
 * <code>StudentFeeRuleDetails</code><p>
 * 
 * 说明：管理人员设置好缴费标准后，需要设置缴费期数及缴费百分百，系统根据这个设置，往学生的交费明细表
 * 中插入对应年度的应缴金额。<p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-8-13 上午10:35:32
 * @see 
 * @version 1.0
 */
@Entity
@Table(name = "EDU_ROLL_FEERULEDETAILS")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class StudentFeeRuleDetails extends BaseModel implements Serializable{
	
	@Column(name="FEETERMNUM")
	private Integer feeTermNum;//缴费期数，默认为两期，1-第一期，2-第二期，3-第三期
	
	@Column(name="FEESCALE",scale=2)
	private Double feeScale;//缴费比例
	
	@Column(name="SHOWORDER")
	private Integer showOrder;//排序号

	 
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FEERULEID")
	private StudentFeeRule studentFeeRule;//缴费标准
    
}
