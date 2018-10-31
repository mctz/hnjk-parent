package com.hnjk.edu.learning.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.hnjk.core.support.base.model.BaseModel;
/**
 * 成卷规则明细表
 * <code>CourseExamRulesDetails</code><p>
 * @author：广东学苑教育发展有限公司.
 * @since： 2011-7-26 下午04:08:44
 * @see 
 * @version 1.0
 */
@Entity
@Table(name="EDU_LEAR_EXRULESDETAILS")  
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class CourseExamRulesDetails extends BaseModel{

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RULEID")
	private CourseExamRules courseExamRules;//成卷规则
	
	@Column(name="EXAMNODETYPE")
	private String examNodeType;//试题类型
	
	@Column(name="EXAMTYPE")
	private String examType;//试题题型
	
	@Column(name="EXAMNUM")
	private Integer examNum;//试题数
	
	@Column(name="EXAMVALUE",scale=2)
	private Double examValue;//试题分数

	@Column(name="showOrder")
	private Integer showOrder;//排序号

}
