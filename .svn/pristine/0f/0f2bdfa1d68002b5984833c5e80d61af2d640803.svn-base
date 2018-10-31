package com.hnjk.edu.recruit.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.hnjk.core.support.base.model.BaseModel;

/**
 * 考生信息表成绩子表
 * @author hzg
 *
 */
@Entity
@Table(name = "EDU_RECRUIT_EXAMINEESCORE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ExameeInfoScore extends BaseModel{

	private static final long serialVersionUID = 259776447922445352L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EXAMINEEID")
	private ExameeInfo exameeInfo;//考生信息ID
	
	@Column(name="SCORECODE")
	private String scoreCode;
	
	@Column(name="SCOREVALUE",length=6,scale=2)
	private Double scoreValue;

	/**
	 * @return the exameeInfo
	 */
	public ExameeInfo getExameeInfo() {
		return exameeInfo;
	}

	/**
	 * @param exameeInfo the exameeInfo to set
	 */
	public void setExameeInfo(ExameeInfo exameeInfo) {
		this.exameeInfo = exameeInfo;
	}

	/**
	 * @return the scoreCode
	 */
	public String getScoreCode() {
		return scoreCode;
	}

	/**
	 * @param scoreCode the scoreCode to set
	 */
	public void setScoreCode(String scoreCode) {
		this.scoreCode = scoreCode;
	}

	/**
	 * @return the scoreValue
	 */
	public Double getScoreValue() {
		return scoreValue;
	}

	/**
	 * @param scoreValue the scoreValue to set
	 */
	public void setScoreValue(Double scoreValue) {
		this.scoreValue = scoreValue;
	}
	
	
}
