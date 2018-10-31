package com.hnjk.extend.plugin.excel.config;

/**
 * excel组件配置属性实体. <p>
 * 请参见excel-config.xml配置.
 * @author： 广东学苑教育发展有限公司.
 * @since： 2009-4-29下午02:28:40
 * @see 
 * @version 1.0
 */
public class ExcelConfigPropertyParam implements java.io.Serializable{
	
	private static final long serialVersionUID = -4365807268488654217L;

	private String name; //名称
	
	private String column;//所在列
	
	private String excelTitleName;//列标题
	
	private String isNull;//是否允许为空
	
	private String columnWidth;//列宽
	
	private String dataType ;//数据类型
	
	private String pattern;//数据格式，将数据格式和pattern拆分开来，灵活处理 //FIXED by hzg 2009-9-18
	
	private String maxLength ;//最长字数限制
	
	private String fixity ;//是否固定列
	
	private String codeTableName ;//列编码
	
	private String defaultValue ;//默认值
	
	public String getColumn() {
		return column;
	}
	public void setColumn(String column) {
		this.column = column;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	public String getExcelTitleName() {
		return excelTitleName;
	}
	public void setExcelTitleName(String excelTitleName) {
		this.excelTitleName = excelTitleName;
	}
	public String getMaxLength() {
		return maxLength;
	}
	public void setMaxLength(String maxLength) {
		this.maxLength = maxLength;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCodeTableName() {
		return codeTableName;
	}
	public void setCodeTableName(String codeTableName) {
		this.codeTableName = codeTableName;
	}
	public String getFixity() {
		return fixity;
	}
	public void setFixity(String fixity) {
		this.fixity = fixity;
	}
	public String getIsNull() {
		return isNull;
	}
	public void setIsNull(String isNull) {
		this.isNull = isNull;
	}
	public String getColumnWidth() {
		return columnWidth;
	}
	public void setColumnWidth(String columnWidth) {
		this.columnWidth = columnWidth;
	}
	public String getPattern() {
		return pattern;
	}
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	
	
}
