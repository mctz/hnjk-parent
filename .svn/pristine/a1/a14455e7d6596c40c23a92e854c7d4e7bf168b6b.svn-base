package com.hnjk.extend.plugin.excel.service;

import java.io.File;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.hnjk.extend.plugin.excel.config.ExcelConfigInfo;
import com.hnjk.extend.plugin.excel.config.MergeCellsParam;
import com.hnjk.extend.plugin.excel.file.ModelToExcel;

/**
 * excel组件-导出excel服务接口. <p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2009-4-30上午08:48:18
 * @see 
 * @version 1.0
 */
public interface IExportExcelService {
	
	/**
	 * 初始化导出服务(按文件方式)
	 * @param fileName 生成的目标文件
	 * @param modelName 配置excel-config.xml中的 model id.
	 * @param modelList 输出为excel的数据列.
	 * @param valueMap 转换键值对，如字典值
	 */
	public abstract void initParmasByfile(File fileName, String modelName,List modelList,Map valueMap);
	
	/**
	 * 初始化导出服务（按文件方式）
	 * @param fileName 生成的目标文件
	 * @param modelName 配置excel-config.xml中的 model id.
	 * @param modelList 输出为excel的数据列.
	 * @param valueMap 转换键值对，如字典值
	 * @param filterColumnList 过滤列名称，不想输出的excel列
	 */
	public abstract void initParmasByfile(File fileName, String modelName,List modelList,Map valueMap,List<String> filterColumnList);
	
	/**
	 * 初始化导出服务(按文件方式)
	 * @param fileName 生成的目标文件
	 * @param modelName 配置excel-config.xml中的 model id.
	 * @param modelList 输出为excel的数据列.
	 * @param valueMap 转换键值对，如字典值
	 * @param filterColumnList 过滤列名称，不想输出的excel列
	 * @param mergeCellsParams 需要合并的单元格坐标 ，参见 {@link com.hnjk.extend.plugin.excel.config.MergeCellsParam}
	 */
	public abstract void initParamsByfile(File fileName,String modelName,List modelList,Map valueMap,List<String> filterColumnList,List<MergeCellsParam> mergeCellsParams);
	
	/**
	 * 初始化方法（文件流方式）<p>注意这个方法已废弃.	 
	 * @param excelOutputStream 目标文件流
	 * @param modelName  配置excel-config.xml中的 model id.
	 * @param modelList 输出为excel的数据列.
	 * @param valueMap 转换键值对	
	 */
	public abstract void initParmasByStream(OutputStream excelOutputStream,	String modelName, List modelList,Map valueMap);
		

	/**
	 * 初始化方法(配置对象方式)<p>注意这个方法已废弃.
	 * @param fileName 目标文件
	 * @param excelConfigInfo 配置excel-config.xml中的 model id.
	 * @param modelList 输出为excel的数据列.
	 * @param valueMap 转换键值对
	 */
	public abstract void initParmasByExcelConfigInfo(File fileName, ExcelConfigInfo excelConfigInfo,List modelList,Map valueMap);
	
	/**
	 * 按模型名称设置模型列表
	 * @param modelName
	 * @param modelList
	 */
	public abstract void setModelName_List(String modelName, List modelList);

	/**
	 * 获取目标excel文件
	 * <p> 将导出文件写到硬盘对应的目录中
	 * @return
	 */
	public abstract File getExcelFile();
	
	public ModelToExcel getModelToExcel();
	
	public void setModelToExcel(ModelToExcel modelToExcel) ;


	ExcelConfigInfo getExcelConfigInfo(Map<String, Object> dynamicTitleMap);
}