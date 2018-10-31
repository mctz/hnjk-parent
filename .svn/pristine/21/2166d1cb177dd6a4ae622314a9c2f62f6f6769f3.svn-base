package com.hnjk.edu.learning.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
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
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.platform.system.model.Attachs;

/**
 * 课程 - 课后作业 -学生作业回答情况表
 * <code>MateResource</code>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-3-12 下午02:22:08
 * @see 
 * @version 1.0
 */
@Entity
@Table(name="EDU_LEAR_STUDENTEXERCISE")  
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class StudentExercise extends BaseModel{
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDENTID")
	private StudentInfo studentInfo;//学生学籍信息
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "EXERCISEID")
	private  Exercise exercise;//作业id，如果为空，则为该批次总评记录
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "EXERCISEBATCHID")
	private ExerciseBatch exerciseBatch;//作业批次
	
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name="ANSWER")
	private String answer;//答案
		
//	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
//	@JoinColumn(name = "ATTACHID")
//	private Attachs attachs;//
	
	@Column(name="RESULT")
	private Double result;//得分
	
	
	@Transient
	private List<Attachs> attachs = new ArrayList<Attachs>();//上传的附件信息
	@Transient
	private String[] attachIds;
	
	/**3.1.1 新增优秀、典型、老师点评*/
	@Column(name="ISTYPICAL")
	private String isTypical = Constants.BOOLEAN_NO;//是否典型
	
	@Column(name="ISEXCELL")
	private String isExcell = Constants.BOOLEAN_NO;//是否优秀
	
	@Column(name="TEACHERADVISE")
	private String teacherAdvise;//老师评语
	
	/**3.1.11 新增学生作业状态*/
	@Column(name="STATUS")
	private Integer status = 0;//学生作业状态 字典CodeStudentExerciseStatus:0-现在做，1-待批改，2-已批改
	
	/**3.1.12 新增学生提交时间*/
	@Column(name="COMITDATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date comitDate;//学生提交时间
	
	@Column(name="CHECKTIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date checkTime;//成绩保存时间
	
	@JoinColumn(name = "CHECKMANID")
	private String checkMANID;//作业批改人账号ID
	
	@JoinColumn(name = "CHECKMAN")
	private String checkMAN;//作业批改人名字

}
