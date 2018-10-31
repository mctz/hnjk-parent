package com.hnjk.extend.plugin.excel.config;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * excel组件配置实体. <p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2009-4-29下午02:23:56
 * @see 
 * @version 1.0
 */
public class ExcelConfigInfo {
	
	private String className = null;//类名，含包名

	private Map<String, ExcelConfigPropertyParam> propertyMap = new HashMap<String, ExcelConfigPropertyParam>();//属性
	
	private Map<String,ExcelConfigPropertyParam> columnMap = new LinkedHashMap<String,ExcelConfigPropertyParam>();//行
	
	private Map<String,String> flagMap = new HashMap<String,String>();//标示
	
	private Map<String,String> messageMap = new HashMap<String,String>();//提示消息
	
	
	

	public Map<String, String> getFlagMap() {
		return flagMap;
	}

	public void setFlagMap(Map<String, String> flagMap) {
		this.flagMap = flagMap;
	}

	public Map<String, String> getMessageMap() {
		return messageMap;
	}

	public void setMessageMap(Map<String, String> messageMap) {
		this.messageMap = messageMap;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	

	public Map<String, ExcelConfigPropertyParam> getPropertyMap() {
		return propertyMap;
	}

	public void setPropertyMap(Map<String, ExcelConfigPropertyParam> propertyMap) {
		this.propertyMap = propertyMap;
	}

	public Map<String, ExcelConfigPropertyParam> getColumnMap() {
		return columnMap;
	}

	public void setColumnMap(Map<String, ExcelConfigPropertyParam> columnMap) {
		this.columnMap = columnMap;
	}

	
	
	
}
