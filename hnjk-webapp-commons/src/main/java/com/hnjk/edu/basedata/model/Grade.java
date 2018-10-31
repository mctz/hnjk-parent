package com.hnjk.edu.basedata.model;

import java.io.Serializable;
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
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Where;

import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.core.support.context.Constants;
import com.hnjk.platform.taglib.JstlCustomFunction;

/**
 * <code>BaseGrade</code>基础数据-年级信息表
 * <p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-3-12 上午10:15:19
 * @see
 * @version 1.0
 */
@Entity
@Table(name = "EDU_BASE_GRADE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Grade extends BaseModel implements Serializable {

	private static final long serialVersionUID = -1255539407426587802L;

	@Column(name = "GRADENAME", nullable = false, unique = true)
	private String gradeName;// 年级名称

	@OneToOne(optional = true, cascade = { CascadeType.MERGE, CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "YEARID")	
	@Where(clause="isDeleted=0")
	private YearInfo yearInfo;// 所属年度

	@Column(name = "TERM", nullable = false, insertable = true, updatable = true, length = 50)
	private String term;// #入学学期,使用基础数据

	@Column(name = "MEMO")
	private String memo;// 备注

	@Column(name = "ISBOOKINGEXAME")
	private String isBookingExame = Constants.BOOLEAN_YES;// 是否可预约考试,Y-是,N-否
	
	@Column(name="ISDEFAULTGRADE")
	private String isDefaultGrade= Constants.BOOLEAN_NO;//是否默认年级
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="INDATE",length=10)
	private Date indate; //默认入学日期
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="GRADUATEDATE",length=10)
	private Date graduateDate; // 默认毕业日期

	@Transient
	private String yearNameAndTerm;
	
	@Transient
	private String yearInfoId;

	public String getIsDefaultGrade() {
		return isDefaultGrade;
	}

	public void setIsDefaultGrade(String isDefaultGrade) {
		this.isDefaultGrade = isDefaultGrade;
	}

	public String getGradeName() {
		return gradeName;
	}

	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}

	public String getIsBookingExame() {
		return isBookingExame;
	}

	public void setIsBookingExame(String isBookingExame) {
		this.isBookingExame = isBookingExame;
	}

	public String getTerm() {
		return this.term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public YearInfo getYearInfo() {
		return yearInfo;
	}

	public void setYearInfo(YearInfo yearInfo) {
		this.yearInfo = yearInfo;
	}

	public String getYearInfoId() {
		return yearInfoId;
	}

	public void setYearInfoId(String yearInfoId) {
		this.yearInfoId = yearInfoId;
	}

	@Override
	public String toString() {
		return getGradeName();
	}

	@Override
	public String getResourceid() {
		return super.getResourceid();
	}

	public String getYearNameAndTerm() {
		return this.yearInfo.getYearName()+JstlCustomFunction.dictionaryCode2Value("CodeTerm",this.getTerm());
	}

	public Date getIndate() {
		return indate;
	}

	public void setIndate(Date indate) {
		this.indate = indate;
	}

	public Date getGraduateDate() {
		return graduateDate;
	}

	public void setGraduateDate(Date graduateDate) {
		this.graduateDate = graduateDate;
	}
	
}