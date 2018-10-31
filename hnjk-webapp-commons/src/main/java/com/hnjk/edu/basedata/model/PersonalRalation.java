package com.hnjk.edu.basedata.model;

import javax.persistence.CascadeType;
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
 *  <code>PersonalRalation</code>基础数据-个人关系.<p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2013-1-7 下午02:28:30
 * @see 
 * @version 1.0
 */
/**
 * @author Msl
 *
 */
@Entity
@Table(name="EDU_BASE_PERSONANRALATION")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PersonalRalation extends BaseModel{

	private static final long serialVersionUID = -407152135804116594L;
	public static final String RALATIONTYPE_F = "F";//家庭关系 
	public static final String RALATIONTYPE_S = "S";//社会关系 
	
	@ManyToOne(optional = false, cascade = { CascadeType.MERGE, CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDENTBASEINFOID")
	private StudentBaseInfo studentBaseInfo;//学生基本信息 
	
	@Column(name = "NAME")
	private String name ;//姓名
	
	@Column(name="GENDER")
    private String gender;//性别
	
	@Column(name = "RALATIONTYPE")
	private String ralationType;//关系类型：家庭关系、社会关系
	
	@Column(name = "RALATION")
	private String ralation;//关系:父母、兄弟、姐妹、朋友、同事....(字典值:CodePersonalRalation)
	
	@Column(name = "WORKPLACE")
	private String workPlace;//工作单位

	@Column(name="title")
	private String title;//职务
	
	@Column(name = "CONTACT")
	private String contact;//联系方式 
	
	@Column(name = "MEMO")
	private String memo;//备注
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public StudentBaseInfo getStudentBaseInfo() {
		return studentBaseInfo;
	}
	public void setStudentBaseInfo(StudentBaseInfo studentBaseInfo) {
		this.studentBaseInfo = studentBaseInfo;
	}
	public String getRalationType() {
		return ralationType;
	}
	public void setRalationType(String ralationType) {
		this.ralationType = ralationType;
	}
	public String getRalation() {
		return ralation;
	}
	public void setRalation(String ralation) {
		this.ralation = ralation;
	}
	public String getWorkPlace() {
		return workPlace;
	}
	public void setWorkPlace(String workPlace) {
		this.workPlace = workPlace;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
}
