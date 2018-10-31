package com.hnjk.edu.evaluate.model;

import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.edu.roll.model.StudentInfo;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name ="EDU_EVALUATE_STUQUESTIONNAIRE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class StuQuestionnaire extends BaseModel{

	@Column(name="SHOWORDER")
	private Integer showOrder = 0;//排序号
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDENTINFOID")
	private StudentInfo studentInfo;//学籍信息
	
	@Column(name="MEMO")
	private String memo;//备注
	
	@Column(name="UPDATEDATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatDate;//最后更新时间
	
	@Column(name="totalScore",scale=1)
	private double totalScore;//评分
	
	@Column(name="COMMENTLABEL")
	private String commentlabel;//评价
	
	@Column(name="ISVALID")
	private String isvalid="Y";//是否有效
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "QUESTIONNAIREID")
	private Questionnaire questionnaire;//问卷
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="stuQuestionnaire")
	@Cascade(value={org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
	@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
	@Where(clause="isDeleted=0")
	@OrderBy("showOrder")
	private Set<StuQuestionBank> stuQuestionBankSet = new HashSet<StuQuestionBank>(0);//学生提交的问卷题目

	
}
