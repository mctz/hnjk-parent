package com.hnjk.edu.basedata.model;

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
 *  <code>StudentResume</code>基础数据-学生简历.<p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-3-15 上午09:40:30
 * @see 
 * @version 1.0
 */
@Entity
@Table(name="EDU_BASE_STUDENTRESUME")  
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class StudentResume  extends BaseModel {

	 private static final long serialVersionUID = 6643044915110794720L;
	 
	 @Column(name="STARTYEAR")		 
     private Long startYear;//开始年
     
	 @Column(name="STARTMONTH")
     private Long startMonth;//开始月
     
	 @Column(name="ENDYEAR")
     private Long endYear;//结束年
     
	 @Column(name="ENDMONTH")
     private Long endMonth;//结束月
     
	 @Column(name="COMPANY")
     private String company;//公司名称
     
	 @Column(name="TITLE")
     private String title;//职务
	 
	 @Column(name="attestator")
	 private String attestator;//证明人
     
	 @Column(name="SHOWORDER")
     private Long showOrder;//排序号
     
     @ManyToOne(fetch = FetchType.LAZY)
     @JoinColumn(name = "STUDENTID")
     private StudentBaseInfo student;//所属学生

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public Long getEndMonth() {
		return endMonth;
	}

	public void setEndMonth(Long endMonth) {
		this.endMonth = endMonth;
	}

	public Long getEndYear() {
		return endYear;
	}

	public void setEndYear(Long endYear) {
		this.endYear = endYear;
	}

	public Long getShowOrder() {
		return showOrder;
	}

	public void setShowOrder(Long showOrder) {
		this.showOrder = showOrder;
	}

	public Long getStartMonth() {
		return startMonth;
	}

	public void setStartMonth(Long startMonth) {
		this.startMonth = startMonth;
	}

	public Long getStartYear() {
		return startYear;
	}

	public void setStartYear(Long startYear) {
		this.startYear = startYear;
	}

	public StudentBaseInfo getStudent() {
		return student;
	}

	public void setStudent(StudentBaseInfo student) {
		this.student = student;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAttestator() {
		return attestator;
	}

	public void setAttestator(String attestator) {
		this.attestator = attestator;
	}
	
     
}