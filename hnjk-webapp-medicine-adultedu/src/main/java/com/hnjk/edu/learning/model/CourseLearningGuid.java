package com.hnjk.edu.learning.model;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.edu.teaching.model.Syllabus;

/**
 * 课程学习指南表
 * <code>CourseLearningGuid</code><p>
 * 用于课程某个章节的扩展介绍.
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-8-24 上午09:21:52
 * @see 
 * @version 1.0
 */
@Entity
@Table(name="EDU_LEAR_COURSEGUID")  
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class CourseLearningGuid extends BaseModel{
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "SYLLABUSTREEID")
	private Syllabus syllabus;//所属知识结构节点
	
	@Column(name="TYPE")
	private String type;//类型 取自字典CodeCourseLearningGuidType 本章引导/学习目标/重点提示/授课教案
	
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name="CONTENT")
	private String content;//内容
	
	@Column(name="FILLINMAN")
	private String fillinMan;//填写人 取当前操作人
	
	@Column(name="FILLINMANID")
	private String fillinManId;//填写人ID 取当前操作人
	
	@Column(name="FILLINDATE")
	private Date fillinDate = new Date();//填写时间

	
}
