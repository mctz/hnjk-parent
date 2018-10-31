package com.hnjk.edu.evaluate.model;

import com.hnjk.core.support.base.model.BaseModel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name ="EDU_EVALUATE_STUQUESTIONBANK")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class StuQuestionBank extends BaseModel{

	@Column(name="SHOWORDER")
	private Integer showOrder = 0;//排序号
	
	@Column(name="QUESTIONSHOWORDER")
	private Integer questionShowOrder;//问卷题目排序号
	
	@Column(name="QUESTIONTARGET",nullable=false)
	private String questionTarget = "0";//CodeQuestionBankQuestion 一级指标  0-无  \ 1-教学态度\2-教学能力与教学方法 \3-教学效果
	
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name="QUESTION",nullable=false)
	private String question;//指标内涵
	
	@Column(name="QUESTIONSCORE",scale=1)
	private double questionScore;//评分
	
	
	@Column(name="STUSCORE",scale=1)
	private double stuScore;//学生提交的评分
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STUQUESTIONNAIREID")
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	private StuQuestionnaire stuQuestionnaire;//学生提交的问卷
	
	@Column(name="MEMO")
	private String memo;//备注
	
	@Column(name="UPDATEDATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;//最后更新时间
	
}