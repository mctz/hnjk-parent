package com.hnjk.edu.teaching.model;

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

import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.edu.basedata.model.Grade;
import com.hnjk.edu.basedata.model.YearInfo;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.teaching.util.ScoreEncryptionDecryptionUtil;
import com.hnjk.security.model.OrgUnit;

@Entity
@Table(name="EDU_TEACH_BACHELOREXAMRESULTS")  
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class BachelorExamResults extends BaseModel{
	
	private static final long serialVersionUID = 5191283689679093230L;
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "YEARID")	
	private YearInfo yearInfo;//年度
	
	@Column(name="TERM")
	private Integer term;//学期
	
	@Column(name = "ORDINAL")
	private Integer ordinal;//序号
	
	@Column(name = "EXAMNO")
	private String examNo;//考场号
	
	@Column(name = "SEATNO")
	private Integer seatNo;//座位号
	
	@Transient
//	@OneToOne(optional = false, cascade = { CascadeType.MERGE, CascadeType.PERSIST }, fetch = FetchType.LAZY)
//	@JoinColumn(name = "STUDENTID")
	private StudentInfo studentInfo;//学号
	
	@Column(name = "STUDENTNAME")
	private String studentName;//姓名
	
	@Transient
//	@OneToOne(optional = false, cascade = { CascadeType.MERGE, CascadeType.PERSIST }, fetch = FetchType.LAZY)
//	@JoinColumn(name = "UNITID")
	private OrgUnit unit;//教学点
	
	@Column(name = "EXAMRESULTS")
	private String examResults;//成绩
	
	@Column(name = "EXAMTIME")
	private String examTime;//时间
	
	@Column(name="BACHELORTYPE")
	private Integer bachelorType=0;// 成绩类型,正常-0，异常-1
	
	@Column(name = "STUDENTNO")
	private String studentNO;
	
	@Column(name = "UNITNAME")
	private String unitName;
	
	public YearInfo getYearInfo() {
		return yearInfo;
	}

	public void setYearInfo(YearInfo yearInfo) {
		this.yearInfo = yearInfo;
	}

	public Integer getTerm() {
		return term;
	}

	public void setTerm(Integer term) {
		this.term = term;
	}
	
	public Integer getOrdinal() {
		return ordinal;
	}

	public void setOrdinal(Integer ordinal) {
		this.ordinal = ordinal;
	}

	public String getExamNo() {
		return examNo;
	}

	public void setExamNo(String examNo) {
		this.examNo = examNo;
	}

	public Integer getSeatNo() {
		return seatNo;
	}

	public void setSeatNo(Integer seatNo) {
		this.seatNo = seatNo;
	}

	public StudentInfo getStudentInfo() {
		return studentInfo;
	}

	public void setStudentInfo(StudentInfo studentInfo) {
		this.studentInfo = studentInfo;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public OrgUnit getUnit() {
		return unit;
	}

	public void setUnit(OrgUnit unit) {
		this.unit = unit;
	}

	public String getExamResults() {
		String examResultsStr = null;
		if(this.bachelorType==0) {
			examResultsStr = examResults;
//			examResultsStr = ScoreEncryptionDecryptionUtil.getInstance().decrypt(this.studentInfo.getResourceid(),this.examResults);
		} else if(this.bachelorType==1){
			examResultsStr = "记0";
		}
		return examResultsStr;
	}

	public void setExamResults(String examResults) {
		this.examResults = examResults;
//		this.examResults = ScoreEncryptionDecryptionUtil.getInstance().encrypt(this.studentInfo.getResourceid(),examResults);
	}

	public String getExamTime() {
		return examTime;
	}

	public void setExamTime(String examTime) {
		this.examTime = examTime;
	}

	public Integer getBachelorType() {
		return bachelorType;
	}

	public void setBachelorType(Integer bachelorType) {
		this.bachelorType = bachelorType;
	}

	public String getStudentNO() {
		return studentNO;
	}

	public void setStudentNO(String studentNO) {
		this.studentNO = studentNO;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

}
