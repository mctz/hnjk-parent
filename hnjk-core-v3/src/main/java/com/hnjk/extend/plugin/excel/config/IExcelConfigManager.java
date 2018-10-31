package com.hnjk.extend.plugin.excel.config;

import org.dom4j.Element;

/**
 * excel组件配置管理接口. <p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2009-4-29下午02:01:12
 * @see 
 * @version 1.0
 */
public interface IExcelConfigManager {
	
	/**
	 * 获取指定模型节点配置元素
	 * @param modelName 模型名称
	 * @return 节点元素
	 */
	public Element getModelElement(String modelName);
	
	/**
	 * 获取指定模型的配置信息
	 * @param modelName
	 * @param flag
	 * @return 配置实体
	 */
	public ExcelConfigInfo getModel(String modelName,String flag);
}
