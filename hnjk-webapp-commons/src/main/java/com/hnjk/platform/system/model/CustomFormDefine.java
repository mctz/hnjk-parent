package com.hnjk.platform.system.model;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Where;

import com.hnjk.core.support.base.model.BaseModel;

/**
 * 自定义表单表.
 * @author hzg
 *
 */
@Entity
@Table(name = "HNJK_SYS_FORM")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CustomFormDefine extends BaseModel {

	private static final long serialVersionUID = 4136853793254719975L;

	@Column(name="FORMCODE",nullable=false,unique=true)
	private String formCode;//表单编号
	
	@Column(name="FORMNAME",nullable=false)
	private String formName;//表单名称
	
	@Column(name="ACTIONURL",nullable=false)
	private String actionUrl;//提交 URL
	
	@Column(name="VALIDATECALLBACK")
	private String validateCallback;//自定义校验JS,默认为validateCallback
	
	@Column(name="MOUDLE")
	private String moudle;//所属模块
	
	@Column(name="FILLINMAN")
	private String fillinMan;//创建人
	
	@Column(name="FILLINMANID")
	private String fillinManId;//创建人ID
	
	@Column(name="FILLINDATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date fillinDate ;//创建日期
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "customFormDefine")
	@org.hibernate.annotations.Cascade(value={org.hibernate.annotations.CascadeType.DELETE_ORPHAN,org.hibernate.annotations.CascadeType.ALL})
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@Where(clause = "isDeleted=0")
	@OrderBy("showOrder ASC")
	private Set<CustomFormFields> fields = new LinkedHashSet<CustomFormFields>(0);//属性描述

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "customFormDefine")
	@org.hibernate.annotations.Cascade(value={org.hibernate.annotations.CascadeType.DELETE_ORPHAN,org.hibernate.annotations.CascadeType.ALL})
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@Where(clause = "isDeleted=0")
	private Set<CustomFormDatas> datas = new LinkedHashSet<CustomFormDatas>(0);//数据
	
	
	
	
	/**
	 * @return the datas
	 */
	public Set<CustomFormDatas> getDatas() {
		return datas;
	}

	/**
	 * @param datas the datas to set
	 */
	public void setDatas(Set<CustomFormDatas> datas) {
		this.datas = datas;
	}

	/**
	 * @return the fields
	 */
	public Set<CustomFormFields> getFields() {
		return fields;
	}

	/**
	 * @param fields the fields to set
	 */
	public void setFields(Set<CustomFormFields> fields) {
		this.fields = fields;
	}

	/**
	 * @return the formCode
	 */
	public String getFormCode() {
		return formCode;
	}

	/**
	 * @param formCode the formCode to set
	 */
	public void setFormCode(String formCode) {
		this.formCode = formCode;
	}

	/**
	 * @return the formName
	 */
	public String getFormName() {
		return formName;
	}

	/**
	 * @param formName the formName to set
	 */
	public void setFormName(String formName) {
		this.formName = formName;
	}

	/**
	 * @return the actionUrl
	 */
	public String getActionUrl() {
		return actionUrl;
	}

	/**
	 * @param actionUrl the actionUrl to set
	 */
	public void setActionUrl(String actionUrl) {
		this.actionUrl = actionUrl;
	}

	/**
	 * @return the validateCallback
	 */
	public String getValidateCallback() {
		return validateCallback;
	}

	/**
	 * @param validateCallback the validateCallback to set
	 */
	public void setValidateCallback(String validateCallback) {
		this.validateCallback = validateCallback;
	}

	/**
	 * @return the moudle
	 */
	public String getMoudle() {
		return moudle;
	}

	/**
	 * @param moudle the moudle to set
	 */
	public void setMoudle(String moudle) {
		this.moudle = moudle;
	}

	/**
	 * @return the fillinMan
	 */
	public String getFillinMan() {
		return fillinMan;
	}

	/**
	 * @param fillinMan the fillinMan to set
	 */
	public void setFillinMan(String fillinMan) {
		this.fillinMan = fillinMan;
	}

	/**
	 * @return the fillinManId
	 */
	public String getFillinManId() {
		return fillinManId;
	}

	/**
	 * @param fillinManId the fillinManId to set
	 */
	public void setFillinManId(String fillinManId) {
		this.fillinManId = fillinManId;
	}

	/**
	 * @return the fillinDate
	 */
	public Date getFillinDate() {
		return fillinDate;
	}

	/**
	 * @param fillinDate the fillinDate to set
	 */
	public void setFillinDate(Date fillinDate) {
		this.fillinDate = fillinDate;
	}
	

	@Override
	public String getResourceid() {
		return super.getResourceid();
	}

	
}
