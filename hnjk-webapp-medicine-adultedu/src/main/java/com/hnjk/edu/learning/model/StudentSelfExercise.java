package com.hnjk.edu.learning.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.platform.system.model.Attachs;

/**
 * 学生自荐作业表
 * (临时，为应付精品课程)
 * @author hzg
 *
 */
@Entity
@Table(name="EDU_LEAR_SELFEXERCISE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class StudentSelfExercise extends BaseModel{

	@OneToOne(optional = true, cascade = {CascadeType.MERGE,CascadeType.PERSIST}, fetch = FetchType.LAZY)  
	@JoinColumn(name = "STUDENTID") 
	private StudentInfo studentInfo;//提交学生
	
	@OneToOne(optional = true, cascade = {CascadeType.MERGE,CascadeType.PERSIST}, fetch = FetchType.LAZY)  
	@JoinColumn(name = "COURSEID")
	private Course course;//关联课程
	
	@Column(name="DESCRIP")
	private String descrip;//作业描述
	
	@Column(name="COMMENTS")
	private String comments;//老师点评
	
	@Column(name="TEACHERID")
	private String teacherId;//点评老师ID
	
	@Column(name="TEACHERNAME")
	private String teacherName;//点评老师姓名
	
	@Column(name="ISPUBLISHED",length=1)
	private String isPublished = Constants.BOOLEAN_NO;//是否审核发布
	
	@Column(name="PUBLISHDATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date publishDate;//审核发布日期

	@Transient
	private List<Attachs> attachs = new ArrayList<Attachs>();//提交的作业附件
	
}
