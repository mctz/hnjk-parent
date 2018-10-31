package com.hnjk.edu.basedata.model;

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
import com.hnjk.security.model.OrgUnit;

/**
 * <code>CourseBook</code>基础数据-课程-课程教材<p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-3-12 下午01:51:12
 * @see 
 * @version 1.0
 */
@Entity
@Table(name="EDU_BASE_COURSEBOOK")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CourseBook  extends BaseModel {

	 private static final long serialVersionUID = 653052874835050808L;
	 
	 @Column(name="BOOKNAME", nullable=false)
	 private String bookName;//教材名称
	 
	 @Column(name="BOOKTYPE",nullable=false)
     private String bookType;//教材类型,取自基础数据
	 
	 @Column(name="PUBLISHCOMPANY",nullable=false)
     private String publishCompany;//出版社
	 
	 @Column(name="AUTHOR", nullable=false)
     private String author;//作者
	 
	 @Column(name="BOOKSERIAL")
     private String bookSerial;//书刊号
	 
	 @Temporal(TemporalType.DATE)
	 @Column(name="PUBLISHDATE")
     private Date publishDate;//出版时间
	 
	 @Column(name="EDITIONNO")
     private String editionNo;//版本号
	 
	 @Column(name="STATUS")
     private Long status;//状态
	 
	 @Column(name="MEMO")
     private String memo;//备注
	 
	 @Column(name="PRICE", nullable=false)
     private Double price;//价格
	 
	 @Column(name="DISCOUNT", nullable=false)
     private Double discount;//折扣
	 
	 @Column(name="SHOWORDER")
     private Long showOrder;//排序号
     
	 @OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST },fetch = FetchType.LAZY)
     @JoinColumn(name = "COURSEID")
     @Where(clause="isDeleted=0")
     private Course course;//所属课程 1:1

	
	 /**3.0.8 新增改版更换及时间*/
	 @Column(name="CHANGEVERSION")
	 private String changeVersion;//改版或更换
	 
	 @Column(name="CHANGEDATE")
	 private Date changeDate;//改版时间
	 
	 /**3.1.6新增教学模式和教学中心*/
	 @Column(name="SCHOOLTYPE")
	 private String schoolType;//教学模式，取字典 CodeTeachingType
	 
	 @OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	 @JoinColumn(name = "BRSCHOOL")
	 private OrgUnit brSchool;//校外学习中心
	 
	 /**新增导出时序号的字段*/
	 @Transient 
	 private Long showOrderNo;
	 public Long getShowOrderNo(){
		 return showOrderNo;
	 }
	 public void setShowOrderNo(Long showOrderNo){
		 this.showOrderNo = showOrderNo;
	 }
	 /**
	 * @return the schoolType
	 */
	public String getSchoolType() {
		return schoolType;
	}

	/**
	 * @param schoolType the schoolType to set
	 */
	public void setSchoolType(String schoolType) {
		this.schoolType = schoolType;
	}

	/**
	 * @return the brSchool
	 */
	public OrgUnit getBrSchool() {
		return brSchool;
	}

	/**
	 * @param brSchool the brSchool to set
	 */
	public void setBrSchool(OrgUnit brSchool) {
		this.brSchool = brSchool;
	}

	@Transient
	private String courseId;

	 
	public Date getChangeDate() {
		return changeDate;
	}

	public void setChangeDate(Date changeDate) {
		this.changeDate = changeDate;
	}

	public String getChangeVersion() {
		return changeVersion;
	}

	public void setChangeVersion(String changeVersion) {
		this.changeVersion = changeVersion;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	public String getBookSerial() {
		return bookSerial;
	}

	public void setBookSerial(String bookSerial) {
		this.bookSerial = bookSerial;
	}

	public String getBookType() {
		return bookType;
	}

	public void setBookType(String bookType) {
		this.bookType = bookType;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public String getEditionNo() {
		return editionNo;
	}

	public void setEditionNo(String editionNo) {
		this.editionNo = editionNo;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getPublishCompany() {
		return publishCompany;
	}

	public void setPublishCompany(String publishCompany) {
		this.publishCompany = publishCompany;
	}

	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	public Long getShowOrder() {
		return showOrder;
	}

	public void setShowOrder(Long showOrder) {
		this.showOrder = showOrder;
	}

	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return getBookName();
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	
	@Override
	public String getResourceid() {
		return super.getResourceid();
	}
 
     

}