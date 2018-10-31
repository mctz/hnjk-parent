package com.hnjk.edu.evaluate.model;

import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.edu.basedata.model.YearInfo;
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
@Table(name ="EDU_EVALUATE_BATCH")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class QuestionnaireBatch extends BaseModel {
	
	@Column(name="SHOWORDER")
	private Integer showOrder = 0;//排序号
	
	@Column(name="UPDATEDATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatDate;//最后更新时间

	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "yearid")
	private YearInfo yearInfo;//年度
	
	@Column(name="TERM")
	private String term;//学期
	
	@Column(name="MEMO")
	private String memo;//备注
	
	@Column(name="ISPUBLISH")
	private String isPublish="N";//是否发布
	
	@Column(name="FACESTARTTIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date faceStartTime;//面授类开始时间
	
	@Column(name="FACEENDTIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date faceEndTime;//面授类结束时间
	
	@Column(name="NETSTARTTIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date netStartTime;//网络类开始时间
	
	@Column(name="NETENDTIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date netEndTime;//网络类结束时间
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="questionnaireBatch")
	@Cascade(value={org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
	@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
	@Where(clause="isDeleted=0")
	@OrderBy("showOrder")
	private Set<Questionnaire> stuQuestionBankSet = new HashSet<Questionnaire>(0);//问卷题目

}
