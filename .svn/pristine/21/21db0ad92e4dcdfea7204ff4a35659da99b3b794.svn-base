package com.hnjk.edu.learning.model;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Where;

import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.core.support.context.Constants;

/**
 * 论坛版块表
 * <code>BbsSection</code><p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-9-6 下午03:04:53
 * @see 
 * @version 1.0
 */
@Entity
@Table(name="EDU_BBS_SECTION")  
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class BbsSection extends BaseModel{

	/**
	 * 论坛版块名称
	 */
	@Column(name="SECTIONNAME",nullable=false)
	private String sectionName;

	/**
	 * 论坛编码
	 */
	@Column(name="SECTIONCODE",nullable=false,unique=true)
	private String sectionCode;

	/**
	 * 父子关系
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PARENTID")
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	private BbsSection parent;

	/**
	 * 版块层级
	 */
	@Column(name="SECTIONLEVEL")
	private Long sectionLevel = 0L;

	/**
	 * 论坛版块介绍
	 */
	@Column(name="SECTIONDESCRIPT")
	private String sectionDescript;

	/**
	 * 是否课程论坛版块
	 */
	@Column(name="ISCOURSESECTION",length=1)
    private String isCourseSection = Constants.BOOLEAN_NO;

	/**
	 * 版主名
	 */
	@Column(name="MASTERNAME")
	private String masterName;

	/**
	 * 版主ID
	 */
	@Column(name="MASTERID")
	private String masterId;

	/**
	 * 点击量
	 */
	@Column(name="CLICKCOUNT")
	private Integer clickCount = 0;

	/**
	 * 主题总量
	 */
	@Column(name="TOPICCOUNT")
	private Integer topicCount = 0;

	/**
	 * 今日发帖总量(主题+回复)
	 */
	@Transient
	private Integer todayCount = 0;

	/**
	 * 课程主题总量
	 */
	@Transient
	private Integer statTopicCount = 0;

	/**
	 * 发帖总量(主题+回复)
	 */
	@Transient
	private Integer invitationCount = 0;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "parent")
	@org.hibernate.annotations.Cascade(value={org.hibernate.annotations.CascadeType.ALL})
	@Where(clause="isDeleted=0")
	@OrderBy("sectionCode ASC")
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private Set<BbsSection> childs = new LinkedHashSet<BbsSection>(0);
	
	/**3.0.9 新增排序字段*/
	@Column(name="SHOWORDER")
	private Integer showOrder = 0;//排序
	
	/**3.1.6新增帖子數量的冗餘字段*/
	@Column(name="TODAYTOPICCOUNT")
	private Integer todayTopicCount = 0;//今日帖子數
	
	@Temporal(TemporalType.DATE)
	@Column(name="TODAYTOPICDATE")
	private Date todayTopicDate = new Date();//今日帖子日期
	
	@Column(name="TOPICANDREPLYCOUNT")
	private Integer topicAndReplyCount = 0;//帖子总量
		
	/**3.1.8 新增论坛是否外部可见及是否只读字段*/
	@Column(name="ISVISIBLE",length=1)
	private String isVisible = Constants.BOOLEAN_YES;//在外部论坛是否可见
	
	@Column(name="ISREADONLY",length=1)
	private String isReadonly = Constants.BOOLEAN_NO;//是否只读
	
}
