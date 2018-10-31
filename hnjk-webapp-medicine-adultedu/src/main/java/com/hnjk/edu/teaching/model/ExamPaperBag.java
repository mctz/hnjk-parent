package com.hnjk.edu.teaching.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Where;

import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.security.model.OrgUnit;

/**
 * 试卷袋标签统计表
 * <code>ExamPaperBag</code><p>
 * @author：广东学苑教育发展有限公司
 * @since： 2011-10-30 上午10:30:28
 * @see 
 * @version 1.0
 */
@Entity
@Table(name="EDU_TEACH_EXAMPAPERBAGSTAT")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ExamPaperBag extends BaseModel{

	private static final long serialVersionUID = -8301528171704346474L;
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name="UNITID")
	private OrgUnit unit;				//学习中心
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name="EXAMINFOID")
	private ExamInfo  examInfo;         //考试信息
	
	@Column(name="BAGNUM")
	private Integer bagNum;             //包数
	
	@Column(name="ORDERNUM")
	private Integer orderNum;           //预约人数
	
	@Column(name="PAPERNUM")
	private Integer paperNum;           //试卷份数
	

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "examPaperBag")
	@org.hibernate.annotations.Cascade(value = {org.hibernate.annotations.CascadeType.ALL,org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@OrderBy("bagIndex ASC")
	@Where(clause = "isDeleted=0")
	private Set<ExamPaperBagDetails> examPaperBags = new HashSet<ExamPaperBagDetails>(); //试卷袋标签明细
	

	public OrgUnit getUnit() {
		return unit;
	}


	public void setUnit(OrgUnit unit) {
		this.unit = unit;
	}


	public ExamInfo getExamInfo() {
		return examInfo;
	}


	public void setExamInfo(ExamInfo examInfo) {
		this.examInfo = examInfo;
	}


	public Integer getBagNum() {
		return bagNum;
	}


	public void setBagNum(Integer bagNum) {
		this.bagNum = bagNum;
	}
	
	

	public Integer getOrderNum() {
		return orderNum;
	}


	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}


	public Integer getPaperNum() {
		return paperNum;
	}


	public void setPaperNum(Integer paperNum) {
		this.paperNum = paperNum;
	}


	public Set<ExamPaperBagDetails> getExamPaperBags() {
		return examPaperBags;
	}


	public void setExamPaperBags(Set<ExamPaperBagDetails> examPaperBags) {
		this.examPaperBags = examPaperBags;
	}
	
	
}
