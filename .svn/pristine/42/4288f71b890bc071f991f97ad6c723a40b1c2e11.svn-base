package com.hnjk.edu.basedata.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.security.model.User;
@Entity
@Table(name="EDU_BASE_TEXTBOOK")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TextBook extends BaseModel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6011833635689444693L;

	@Column(name="BOOKNAME")
	private String bookName;//书名
	
	@Column(name="BOOKSERIAL")
	private String bookSerial;//书号
	
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name="courseid")
	private Course course;//关联的课程
	
	@Column(name="EDITOR")
	private String editor;//主编
	
	@Column(name="PRICE")
	private String price;//单价
	
	@Column(name="PRESS")
	private String press;//出版社
	
	@Column(name="ISUSED")
	private String isUsed="N";//是否使用  默认为否
	
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name="UPDATEDATE")
    private Date updatedate;//更新日期
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE,CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name="UPDATEMANID")
	private User user;//操作人员

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

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getPress() {
		return press;
	}

	public void setPress(String press) {
		this.press = press;
	}

	public String getIsUsed() {
		return isUsed;
	}

	public void setIsUsed(String isUsed) {
		this.isUsed = isUsed;
	}

	public Date getUpdatedate() {
		return updatedate;
	}

	public void setUpdatedate(Date updatedate) {
		this.updatedate = updatedate;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	
}
