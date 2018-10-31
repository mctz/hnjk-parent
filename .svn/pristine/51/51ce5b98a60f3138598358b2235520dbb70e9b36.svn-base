package com.hnjk.edu.learning.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.teaching.model.Syllabus;

/**
 * 课程 - 随堂练习题干
 * <code>MateResource</code>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-3-12 下午02:22:08
 * @see 
 * @version 1.0
 */
@Entity
@Table(name="EDU_LEAR_COURSEEXAM")  
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class ActiveCourseExam  extends BaseModel{

	/**
	 * 所属课程教学大纲知识节点
	 */
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "SYLLABUSTREEID")
	private Syllabus syllabus;
	
	@Transient
	private String syllabusId;

	/**
	 * 分值
	 */
	@Column(name="SCORE")
	private Long score = 0L;

	/**
	 * 排序号
	 */
	@Column(name="SHOWORDER")
	private Integer showOrder = 0;
	
	/**3.1.1 新增关联的知识点*/
	@Column(name="REFERSYLLABUSTREEIDS")
	private String referSyllabusTreeIds;//关联知识节点ID集合，使用","分割
	
	@Column(name="REFERSYLLABUSTREENAMES")
	private String referSyllabusTreeNames;//关联知识点名称集合,使用","分割
	
	/**3.1.2 新增题库试题*/	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "EXAMID")
	private CourseExam courseExam;//试题
	
	/**3.1.11 新增发布状态及审核人等*/
	@Column(name="ISPUBLISHED",length=1)
	private String isPublished = Constants.BOOLEAN_NO;

	/**
	 * 审核人姓名
	 */
	@Column(name="AUDITMAN")
	private String auditMan;

	/**
	 * 审核人ID
	 */
	@Column(name="AUDITMANID")
	private String auditManId;

	/**
	 * 审核日期
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="AUDITDATE")
	private Date auditDate;
	
	/**
	 * 修改人账号
	 * 3.1.11 新增审计信息
	 */
	@Column(name="MODIFYMAN",length=50)
	private String modifyMan;

	/**
	 * 修改时间
	 */
	@Column(name="MODIFYDATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifyDate;
	
}
