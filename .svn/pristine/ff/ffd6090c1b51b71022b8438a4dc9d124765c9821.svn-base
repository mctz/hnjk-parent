package com.hnjk.edu.evaluate.model;

import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.roll.model.Classes;
import com.hnjk.security.model.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Date;
@Entity
@Table(name ="EDU_EVALUATE_QUESTIONNAIRE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class Questionnaire extends BaseModel {

	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "CLASSESID")
	private Classes classes;//班级信息
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "COURSEID")
	private Course course;//课程信息
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "TEACHERID")
	private User teacher;//教师信息
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EVALUATEBATCHID")
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	private QuestionnaireBatch questionnaireBatch;//教评批次	
	
	@Column(name="STARTTIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date startTime;//开始时间
	
	@Column(name="ENDTIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date endTime;//结束时间
	
	@Column(name="COURSETYPE")
	private String courseType="0";//课程类型：0-面授  1-网络
	
	@Column(name="SHOWORDER")
	private Integer showOrder = 0;//排序号

	@Column(name="MEMO")
	private String memo;//备注
	
	@Column(name="ISPUBLISH")
	private String isPublish="Y";//是否发布
	
	@Column(name="UPDATEDATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatDate;//最后更新时间
	
}
