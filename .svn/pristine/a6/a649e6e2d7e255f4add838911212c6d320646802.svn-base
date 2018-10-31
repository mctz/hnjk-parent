package com.hnjk.edu.roll.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.edu.recruit.model.EnrolleeInfo;
/**
 * 学生在银校通的缴费信息 
 * <code>StudentFactFee</code>
 * @author：广东学苑教育发展有限公司.
 * @since： 2011-8-4 上午11:47:50
 * @see 
 * @version 1.0
 */
@Entity
@Table(name = "EDU_ROLL_FISTUDENTFEE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class StudentFactFee extends BaseModel{

	private static final long serialVersionUID = -6889489647166764333L;
	
	@Column(name="STUDENTID")
	private String studyNo;//学籍学号
		
	@Column(name="RECPAYFEE",scale=2)
	private Double recpayFee;//应缴金额
	
	@Column(name="FACTPAYFEE",scale=2)
	private Double facepayFee;//实缴金额
	
	@Column(name="CHARGEYEAR")
	private Integer chargeyear;//缴费年度
	
	@Column(name="DERATEFEE",scale=2)
	private Double derateFee;//减免费用

	@OneToOne(optional = false, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDENTINFOID")
	private StudentInfo studentInfo;//学生学籍
	
	@Transient
	private String studentid;//学号
	
	@Transient
	private String studentInfoId;
	
	@Transient
	private EnrolleeInfo enrolleeInfo;
	
	
	
	/**
	 * @return the enrolleeInfo
	 */
	public EnrolleeInfo getEnrolleeInfo() {
		return enrolleeInfo;
	}

	/**
	 * @param enrolleeInfo the enrolleeInfo to set
	 */
	public void setEnrolleeInfo(EnrolleeInfo enrolleeInfo) {
		this.enrolleeInfo = enrolleeInfo;
	}

	/**
	 * @return the studentInfoId
	 */
	public String getStudentInfoId() {
		return studentInfoId;
	}

	/**
	 * @param studentInfoId the studentInfoId to set
	 */
	public void setStudentInfoId(String studentInfoId) {
		this.studentInfoId = studentInfoId;
	}

	/**
	 * @return the studentid
	 */
	public String getStudentid() {
		return studentid;
	}

	/**
	 * @param studentid the studentid to set
	 */
	public void setStudentid(String studentid) {
		this.studentid = studentid;
	}

	/**
	 * @return the studentInfo
	 */
	public StudentInfo getStudentInfo() {
		return studentInfo;
	}

	/**
	 * @param studentInfo the studentInfo to set
	 */
	public void setStudentInfo(StudentInfo studentInfo) {
		this.studentInfo = studentInfo;
	}

	/**
	 * @return the studyNo
	 */
	public String getStudyNo() {
		return studyNo;
	}

	/**
	 * @param studyNo the studyNo to set
	 */
	public void setStudyNo(String studyNo) {
		this.studyNo = studyNo;
	}

	/**
	 * @return the recpayFee
	 */
	public Double getRecpayFee() {
		return recpayFee;
	}

	/**
	 * @param recpayFee the recpayFee to set
	 */
	public void setRecpayFee(Double recpayFee) {
		this.recpayFee = recpayFee;
	}

	/**
	 * @return the facepayFee
	 */
	public Double getFacepayFee() {
		return facepayFee;
	}

	/**
	 * @param facepayFee the facepayFee to set
	 */
	public void setFacepayFee(Double facepayFee) {
		this.facepayFee = facepayFee;
	}

	

	/**
	 * @return the chargeyear
	 */
	public Integer getChargeyear() {
		return chargeyear;
	}

	/**
	 * @param chargeyear the chargeyear to set
	 */
	public void setChargeyear(Integer chargeyear) {
		this.chargeyear = chargeyear;
	}

	/**
	 * @return the derateFee
	 */
	public Double getDerateFee() {
		return derateFee;
	}

	/**
	 * @param derateFee the derateFee to set
	 */
	public void setDerateFee(Double derateFee) {
		this.derateFee = derateFee;
	}
	
	
	

}
