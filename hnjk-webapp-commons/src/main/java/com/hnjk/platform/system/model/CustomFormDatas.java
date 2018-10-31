package com.hnjk.platform.system.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.hnjk.core.support.base.model.BaseModel;

/**
 * 自定义表单数据表.
 * @author hzg
 *
 */
@Entity
@Table(name = "HNJK_SYS_FORMDATA")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CustomFormDatas extends BaseModel {

	private static final long serialVersionUID = 7479381596307570117L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FORMID")
	private CustomFormDefine customFormDefine;//所属表单
	
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name="FORMDATA")
	private String formData;//数据，JSON字符串

	/**
	 * @return the customFormDefine
	 */
	public CustomFormDefine getCustomFormDefine() {
		return customFormDefine;
	}

	/**
	 * @param customFormDefine the customFormDefine to set
	 */
	public void setCustomFormDefine(CustomFormDefine customFormDefine) {
		this.customFormDefine = customFormDefine;
	}

	/**
	 * @return the formData
	 */
	public String getFormData() {
		return formData;
	}

	/**
	 * @param formData the formData to set
	 */
	public void setFormData(String formData) {
		this.formData = formData;
	}
	
	
}
