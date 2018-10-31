package com.hnjk.edu.recruit.helper;

import javax.xml.bind.annotation.XmlAttribute;
/**
 * 考生信息数据检查.
 * <code>ExameeInfoField</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2012-9-6 下午05:36:16
 * @see 
 * @version 1.0
 */
public class ExameeInfoField {
	private String name;//字段名称(必须)
	private String titleName;//字段含义
	private String isNull;//是否允许为空(Y/N)
	private String codeTableName;//字典值编码(检查字典值是否存在)
	private String referenceName;//关联数据名称(检查外键关联)
	private String modelType;//对应model类型
	private String mappingFiled;//对应model的字段
	private String displayName;//字段值对应显示名称(如层次编码对应的名称)
	
	@XmlAttribute(name="name",required=true)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@XmlAttribute(name="titleName")
	public String getTitleName() {
		return titleName;
	}
	public void setTitleName(String titleName) {
		this.titleName = titleName;
	}
	@XmlAttribute(name="isNull")
	public String getIsNull() {
		return isNull;
	}
	public void setIsNull(String isNull) {
		this.isNull = isNull;
	}
	@XmlAttribute(name="codeTableName")
	public String getCodeTableName() {
		return codeTableName;
	}
	public void setCodeTableName(String codeTableName) {
		this.codeTableName = codeTableName;
	}
	@XmlAttribute(name="referenceName")
	public String getReferenceName() {
		return referenceName;
	}
	public void setReferenceName(String referenceName) {
		this.referenceName = referenceName;
	}
	@XmlAttribute(name="modelType")
	public String getModelType() {
		return modelType;
	}
	public void setModelType(String modelType) {
		this.modelType = modelType;
	}
	@XmlAttribute(name="mappingFiled")
	public String getMappingFiled() {
		return mappingFiled;
	}
	public void setMappingFiled(String mappingFiled) {
		this.mappingFiled = mappingFiled;
	}
	@XmlAttribute(name="displayName")
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
}
