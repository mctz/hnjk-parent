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
 * 考生信息表志愿子表
 * @author Administrator
 *
 */
@Entity
@Table(name = "EDU_RECRUIT_EXAMINEEWISH")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ExameeInfoWish extends BaseModel{

	private static final long serialVersionUID = -2273060140485228023L;

	@Column(name="ZYBM")
	private String ZYBM;//志愿信息编码
	
	@Column(name="ZYMC")
	private String ZYMC;//自愿信息名称
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EXAMINEEID")
	private ExameeInfo exameeInfo;//考生信息ID

	@Column(name="SHOWORDER")
	private Integer showOrder = 0;
	
	
	
	/**
	 * @return the showOrder
	 */
	public Integer getShowOrder() {
		return showOrder;
	}

	/**
	 * @param showOrder the showOrder to set
	 */
	public void setShowOrder(Integer showOrder) {
		this.showOrder = showOrder;
	}

	/**
	 * @return the zYBM
	 */
	public String getZYBM() {
		return ZYBM;
	}

	/**
	 * @param zYBM the zYBM to set
	 */
	public void setZYBM(String zYBM) {
		ZYBM = zYBM;
	}

	/**
	 * @return the zYMC
	 */
	public String getZYMC() {
		return ZYMC;
	}

	/**
	 * @param zYMC the zYMC to set
	 */
	public void setZYMC(String zYMC) {
		ZYMC = zYMC;
	}

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
	
	
}
