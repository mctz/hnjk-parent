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
import com.hnjk.edu.teaching.model.Syllabus;

/**
 * 学生学习跟踪记录表
 * @author hzg
 *
 */
@Entity
@Table(name="EDU_LEAR_STUTRACE")  
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class StudentLearningTrace extends BaseModel{

	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDENTID")
	private StudentInfo studentInfo;
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "SYLLABUSTREEID")
	private Syllabus syllabus;//所属课程教学大纲知识节点
	
	@Column(name="LEARHANDOUTTIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date learHandoutTime;//学习讲义时间
	
	@Column(name="LEARMETETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date learMeteTime;//学习授课时间
	
	@Column(name="LEARCOURSEEXAMTIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date learCourseexamTime;//提交随堂练习时间
	
}
