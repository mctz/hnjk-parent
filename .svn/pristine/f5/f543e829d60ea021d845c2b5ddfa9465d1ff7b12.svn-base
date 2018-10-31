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

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.edu.roll.model.StudentInfo;

/**
 * 课程 - 随堂练习-学生回答情况
 * <code>MateResource</code>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-3-12 下午02:22:08
 * @see 
 * @version 1.0
 */
@Entity
@Table(name="EDU_LEAR_STUDENTCOURSEEXAM")  
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class StudentActiveCourseExam extends BaseModel{
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDENTID")
	private StudentInfo studentInfo;//学生学籍信息
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "COURSEEXAMID")
	private ActiveCourseExam activeCourseExam;//学生回答的习题主干
	
	@Column(name="ANSWER")
	private String answer;//学生答案
	
	@Column(name="ISCORRECT")
	private String isCorrect;//是否回答正确
	
	@Column(name="RESULT")
	private Double result;//得分      ----华教对此处的设计：保存状态时，result为 null，不为null时，则是提交状态
	
	@Column(name="SHOWORDER")
	private Integer showOrder;//排序号
	
	/**3.2.5 新增答题时间*/
	@Column(name="ANSWERTIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date answerTime;//答题时间

}
