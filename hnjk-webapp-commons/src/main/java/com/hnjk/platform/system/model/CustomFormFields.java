package com.hnjk.platform.system.model;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.hnjk.core.support.base.model.BaseModel;

/**
 * 自定义表单属性定义表.
 * @author hzg
 *
 */
@Entity
@Table(name = "HNJK_SYS_FORMFIELDS")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CustomFormFields extends BaseModel{

	private static final long serialVersionUID = -7213835676000725138L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FORMID")
	private CustomFormDefine customFormDefine;//所属表单
	
	@Column(name="FIELDNAME",nullable=false)
	private String fieldName;//属性名
	
	@Column(name="FIELDCODE",nullable=false)
	private String fieldCode;//属性编码
	
	@Column(name="DATATYPE")
	private String dataType;//数据类型,字典值
	
	@Column(name="FORMDOMTYPE")
	private String formDomType;//表单元素类型,字典值
	
	@Column(name="HTMLSTYLE")
	private String htmlStyle;//html style
	
	@Column(name="CHECKTYPE")
	private String checkType;//数据校验类型,字典
	
	@Column(name="GROUPNUM")
	private Integer groupNum;//分组号
	
	@Column(name="GROUPNAME")
	private String groupName;//分组名
	
	@Column(name="STATUS")
	private Integer status = 0;//状态
	
	@Column(name="SHOWORDER")
	private Integer showOrder = 0;//序号
	
	@Column(name="DICTCODE")
	private String dictCode;//数据字典编码
	
	@Column(name="CUSTOMVAR")
	private String customVar;//自定义遍历，可能是单个对象也可能是一组对象
	
	@Column(name="FORMAT")
	private String format;//格式数据，如日期可以为:yyyy-MM-dd,yyyy-MM-dd hh:mm:ss，数字可以为####.##等
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PARENTID")
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private CustomFormFields parent;//父
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
	@org.hibernate.annotations.Cascade(value={org.hibernate.annotations.CascadeType.DELETE_ORPHAN,org.hibernate.annotations.CascadeType.ALL})
	@OrderBy("showOrder asc")
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private Set<CustomFormFields> childs = new LinkedHashSet<CustomFormFields>(0);//子

	/**
	 * 2012-11-07 gu
	 * 添加 tips 列为存储提示信息
	 */
	@Column(name="TIPS")
	private String tips;
	
	@Column(name="INPUTLENGTH")
	private Integer inputLength;//输入长度限制，前台使用方式：<input type='text' name='test' maxlength='20'/>
	
	
	
	/**
	 * @return the inputLength
	 */
	public Integer getInputLength() {
		return inputLength;
	}

	/**
	 * @param inputLength the inputLength to set
	 */
	public void setInputLength(Integer inputLength) {
		this.inputLength = inputLength;
	}

	/**
	 * @return the format
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * @param format the format to set
	 */
	public void setFormat(String format) {
		this.format = format;
	}

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
	 * @return the fieldName
	 */
	public String getFieldName() {
		return fieldName;
	}

	/**
	 * @param fieldName the fieldName to set
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	/**
	 * @return the fieldCode
	 */
	public String getFieldCode() {
		return fieldCode;
	}

	/**
	 * @param fieldCode the fieldCode to set
	 */
	public void setFieldCode(String fieldCode) {
		this.fieldCode = fieldCode;
	}

	/**
	 * @return the dataType
	 */
	public String getDataType() {
		return dataType;
	}

	/**
	 * @param dataType the dataType to set
	 */
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	/**
	 * @return the formDomType
	 */
	public String getFormDomType() {
		return formDomType;
	}

	/**
	 * @param formDomType the formDomType to set
	 */
	public void setFormDomType(String formDomType) {
		this.formDomType = formDomType;
	}

	/**
	 * @return the htmlStyle
	 */
	public String getHtmlStyle() {
		return htmlStyle;
	}

	/**
	 * @param htmlStyle the htmlStyle to set
	 */
	public void setHtmlStyle(String htmlStyle) {
		this.htmlStyle = htmlStyle;
	}

	/**
	 * @return the checkType
	 */
	public String getCheckType() {
		return checkType;
	}

	/**
	 * @param checkType the checkType to set
	 */
	public void setCheckType(String checkType) {
		this.checkType = checkType;
	}

	

	/**
	 * @return the groupNum
	 */
	public Integer getGroupNum() {
		return groupNum;
	}

	/**
	 * @param groupNum the groupNum to set
	 */
	public void setGroupNum(Integer groupNum) {
		this.groupNum = groupNum;
	}

	/**
	 * @return the groupName
	 */
	public String getGroupName() {
		return groupName;
	}

	/**
	 * @param groupName the groupName to set
	 */
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	/**
	 * @return the status
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * @return the showOrder
	 */
	public Integer getShowOrder() {
		return showOrder;
	}

	/**
	 * @param showOrder the showOrder to set
	 */
	public void setShowOrder(Integer showOrder) {
		this.showOrder = showOrder;
	}

	/**
	 * @return the dictCode
	 */
	public String getDictCode() {
		return dictCode;
	}

	/**
	 * @param dictCode the dictCode to set
	 */
	public void setDictCode(String dictCode) {
		this.dictCode = dictCode;
	}

	/**
	 * @return the customVar
	 */
	public String getCustomVar() {
		return customVar;
	}

	/**
	 * @param customVar the customVar to set
	 */
	public void setCustomVar(String customVar) {
		this.customVar = customVar;
	}

	/**
	 * @return the parent
	 */
	public CustomFormFields getParent() {
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(CustomFormFields parent) {
		this.parent = parent;
	}

	/**
	 * @return the childs
	 */
	public Set<CustomFormFields> getChilds() {
		return childs;
	}

	/**
	 * @param childs the childs to set
	 */
	public void setChilds(Set<CustomFormFields> childs) {
		this.childs = childs;
	}

	/**
	 * @return the tips
	 */
	public String getTips() {
		return tips;
	}

	/**
	 * @param tips the tips to set
	 */
	public void setTips(String tips) {
		this.tips = tips;
	}
	
	
}
