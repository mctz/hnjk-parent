package com.hnjk.edu.basedata.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.hnjk.core.support.base.model.BaseModel;

/**
 * 国家专业代码库.
 * <code>NationMajor</code><p>
 * 
 * @author：广东学苑教育发展有限公司.
 * @since： 2011-4-15 下午02:21:33
 * @see 
 * @version 1.0
 */
@Entity
@Table(name = "EDU_BASE_NATIONMAJOR")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class NationMajor extends BaseModel{

	private static final long serialVersionUID = -2967119776429498128L;
	
	@Column(name="NATIONMAJORCODE",length=50,nullable=false)
	private String nationMajorCode;//专业编码
	
	@Column(name="NATIONMAJORNAME",nullable=false)
	private String nationMajorName;//专业名称
	
	@OneToOne(optional = true, cascade = { CascadeType.MERGE, CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "CLASSICID")
	private Classic classic;//层次
	
	@Column(name="NATIONMAJORTYPE")
	private String nationMajorType;//科类,字典
	
	@Column(name="NATIONMAJORDICT")
	private String nationMajorDict;//专业方向
	
	@Column(name="MEMO")
	private String memo;//备注
	
	@Column(name="MAJORCODE",unique=true,length=50, nullable=false)
	private String majorCode;//专业代码 UK

	@Column(name="BIGCATALOG")
	private String parentCatalog;//专业大类
	
	@Column(name="CATALOG")
	private String childCatalog;//专业小类
	
	
	
	/**
	 * @return the parentCatalog
	 */
	public String getParentCatalog() {
		return parentCatalog;
	}

	/**
	 * @param parentCatalog the parentCatalog to set
	 */
	public void setParentCatalog(String parentCatalog) {
		this.parentCatalog = parentCatalog;
	}


	/**
	 * @return the childCatalog
	 */
	public String getChildCatalog() {
		return childCatalog;
	}

	/**
	 * @param childCatalog the childCatalog to set
	 */
	public void setChildCatalog(String childCatalog) {
		this.childCatalog = childCatalog;
	}

	/**
	 * @return the majorCode
	 */
	public String getMajorCode() {
		return majorCode;
	}

	/**
	 * @param majorCode the majorCode to set
	 */
	public void setMajorCode(String majorCode) {
		this.majorCode = majorCode;
	}

	/**
	 * @return the nationMajorCode
	 */
	public String getNationMajorCode() {
		return nationMajorCode;
	}

	/**
	 * @param nationMajorCode the nationMajorCode to set
	 */
	public void setNationMajorCode(String nationMajorCode) {
		this.nationMajorCode = nationMajorCode;
	}

	/**
	 * @return the nationMajorName
	 */
	public String getNationMajorName() {
		return nationMajorName;
	}

	/**
	 * @param nationMajorName the nationMajorName to set
	 */
	public void setNationMajorName(String nationMajorName) {
		this.nationMajorName = nationMajorName;
	}

	/**
	 * @return the classic
	 */
	public Classic getClassic() {
		return classic;
	}

	/**
	 * @param classic the classic to set
	 */
	public void setClassic(Classic classic) {
		this.classic = classic;
	}

	/**
	 * @return the nationMajorType
	 */
	public String getNationMajorType() {
		return nationMajorType;
	}

	/**
	 * @param nationMajorType the nationMajorType to set
	 */
	public void setNationMajorType(String nationMajorType) {
		this.nationMajorType = nationMajorType;
	}

	/**
	 * @return the nationMajorDict
	 */
	public String getNationMajorDict() {
		return nationMajorDict;
	}

	/**
	 * @param nationMajorDict the nationMajorDict to set
	 */
	public void setNationMajorDict(String nationMajorDict) {
		this.nationMajorDict = nationMajorDict;
	}

	/**
	 * @return the memo
	 */
	public String getMemo() {
		return memo;
	}

	/**
	 * @param memo the memo to set
	 */
	public void setMemo(String memo) {
		this.memo = memo;
	}
	
	@Override
	public String getResourceid() {
		return super.getResourceid();
	}
	
}
