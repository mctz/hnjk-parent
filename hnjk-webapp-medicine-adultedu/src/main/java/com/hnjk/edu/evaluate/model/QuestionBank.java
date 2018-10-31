package com.hnjk.edu.evaluate.model;

import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.security.model.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name ="EDU_EVALUATE_QUESTIONBANK")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class QuestionBank extends BaseModel {
	
	@Column(name="SHOWORDER")
	private Integer showOrder = 0;//排序号
	
	@Column(name="QUESTIONTARGET",nullable=false)
	private String questionTarget = "0";//CodeQuestionBankQuestion 一级指标  0-无  \ 1-教学态度\2-教学能力与教学方法 \3-教学效果
	
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name="QUESTION",nullable=false)
	private String question;//指标内涵
	
	
	@Column(name="MEMO")
	private String memo;//备注
	
	@Column(name="UPDATEDATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatDate;//最后更新时间
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "UPDATEMANID")
	private User updateMan;
	
	@Column(name="SCORE",scale=1)
	private double score=10.0;//评分
	
	@Column(name="COURSETYPE")
	private String courseType="0";//课程类型：0-面授  1-网络
	
}
