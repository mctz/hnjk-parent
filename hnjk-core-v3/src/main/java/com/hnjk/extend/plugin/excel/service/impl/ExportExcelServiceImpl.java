package com.hnjk.extend.plugin.excel.service.impl;

import java.io.File;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.hnjk.core.support.context.Constants;
import com.hnjk.extend.plugin.excel.config.*;
import com.hnjk.extend.plugin.excel.util.ValidateColumn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.hnjk.extend.plugin.excel.file.ModelToExcel;
import com.hnjk.extend.plugin.excel.service.IExportExcelService;

/**
 * excel组件-模型管理类. <p>
 * excel导入时，映射到model中去。
 * @author： 广东学苑教育发展有限公司.
 * @since： 2009-4-29下午12:17:20
 * @see 
 * @version 1.0
 */
@Component("exportExcelService")
public class ExportExcelServiceImpl implements IExportExcelService {	
	
	 @Autowired
	 @Qualifier("excelConfigManager")
	 private IExcelConfigManager configManager ;//配置管理类	  	  
	 public void setConfigManager(IExcelConfigManager configManager) {
		this.configManager = configManager;
	}
	
	private ConfigParam param;//配置参数	
	 
	private ModelToExcel modelToExcel;//模型转Excel action方法	
	

	@Override
	public ModelToExcel getModelToExcel() {
		return modelToExcel;
	}

	@Override
	public void setModelToExcel(ModelToExcel modelToExcel) {
		this.modelToExcel = modelToExcel;
	}


	@Override
	 public void initParmasByfile(File fileName, String modelName, List modelList, Map valueMap) {
		ConfigParam param = new ConfigParam();//配置参数
		param.setExcelConfig(configManager.getModel(modelName, ""));
		param.setExcelFile(fileName);
		param.setModelList(modelList);
		setParam(param);  
		param.setValueMap(valueMap);
	    this.modelToExcel = new ModelToExcel(param);
	  }
	 
	/**
	 * @deprecated 请使用 {@link #initParmasByfile(fileName, modelName, modelList, valueMap)} .
	 */
	 @Override
	 public void initParmasByStream(OutputStream excelOutputStream, String modelName, List modelList, Map valueMap) {
		 ConfigParam param = new ConfigParam();//配置参数
		 param.setExcelConfig(configManager.getModel(modelName, ""));
		 param.setExcelOutputStream(excelOutputStream);
		 param.setModelList(modelList);
		 setParam(param); 
		 param.setValueMap(valueMap);
		 this.modelToExcel = new ModelToExcel(param);
	  }
	 
	/**
	 * @deprecated 请使用 {@link #initParmasByfile(fileName, modelName, modelList, valueMap)} .
	 */
	 @Override
	 public void initParmasByExcelConfigInfo(File fileName, ExcelConfigInfo excelConfigInfo, List modelList, Map valueMap) {
		 ConfigParam param = new ConfigParam();//配置参数
		 param.setExcelConfig(excelConfigInfo);
		 param.setExcelFile(fileName);
		 param.setModelList(modelList);
		 setParam(param);  
		 param.setValueMap(valueMap);
	     this.modelToExcel = new ModelToExcel(param);
	 }

	/**
	 * 获取自定义配置
	 * @param dynamicTitleMap
	 * @return
	 */
	@Override
	public ExcelConfigInfo getExcelConfigInfo(Map<String, Object> dynamicTitleMap) {

		ExcelConfigInfo result = new ExcelConfigInfo();

		Map<String, ExcelConfigPropertyParam> propertyMap = new HashMap<String, ExcelConfigPropertyParam>();
		Map<String, ExcelConfigPropertyParam> columnMap = new LinkedHashMap<String, ExcelConfigPropertyParam>();

		int index = 1;
		for (String key : dynamicTitleMap.keySet()) {
			ExcelConfigPropertyParam modelProperty = new ExcelConfigPropertyParam();
			modelProperty.setName(key);
			modelProperty.setColumn(Integer.toString(index++));
			modelProperty.setExcelTitleName(dynamicTitleMap.get(key).toString());
			modelProperty.setIsNull(Constants.BOOLEAN_YES);
			modelProperty.setColumnWidth("15");
			modelProperty.setMaxLength("500");
			modelProperty.setDataType("String");

			String excelTitle = ValidateColumn.configValidate(propertyMap,modelProperty.getExcelTitleName());
			propertyMap.put(excelTitle, modelProperty);

			columnMap.put(modelProperty.getColumn(), modelProperty);
		}
		//标示
		Map<String, String> flagMap = new HashMap<String, String>();
		flagMap.put(ConfigConstant.PROPERTY_NAME, "flag");
		result.setFlagMap(flagMap);

		//提示消息
		Map<String, String> messageMap = new HashMap<String, String>();
		messageMap.put(ConfigConstant.PROPERTY_NAME, "message");
		messageMap.put(ConfigConstant.PROPERTY_EXCEL_TITLE_NAME, "操作结果");
		result.setMessageMap(messageMap);
		result.setPropertyMap(propertyMap);
		result.setColumnMap(columnMap);

		return result;
	}
	 
	  @Override
	  public void initParmasByfile(File fileName, String modelName, List modelList, Map valueMap, List<String> filterColumnList) {
		  ExcelConfigInfo excelConfigInfo = configManager.getModel(modelName, "");
		  
		  if(null != filterColumnList && !filterColumnList.isEmpty()){
			  //处理过滤列
			  Map<String,ExcelConfigPropertyParam> columnMap = excelConfigInfo.getColumnMap();
			  Map<String, ExcelConfigPropertyParam> cloneColumnMap = new LinkedHashMap<String, ExcelConfigPropertyParam>();//重新组织的新MAP
			  Assert.notEmpty(columnMap, "不可抛出的异常!");
			
			  int i=1;
			  for(String key : columnMap.keySet()){//for map
				  for(String columnName : filterColumnList){//for list
					  if(columnMap.get(key).getName().equals(columnName)){
						  cloneColumnMap.put(String.valueOf(i), columnMap.get(key));
						  i++;
						 break;
						}
				  }			 			  
				} 
			  excelConfigInfo.setColumnMap(cloneColumnMap);
		  }		 
		
		  ConfigParam param = new ConfigParam();//配置参数
		  param.setExcelConfig(excelConfigInfo);
		  param.setExcelFile(fileName);
		  param.setModelList(modelList);
		  setParam(param);  
		  param.setValueMap(valueMap);
		  
		  this.modelToExcel = new ModelToExcel(param);
	}

	  
	  
	
	@Override
	public void initParamsByfile(File fileName, String modelName,List modelList, Map valueMap, 
			List<String> filterColumnList,List<MergeCellsParam> mergeCellsParams) {
		
		ExcelConfigInfo excelConfigInfo = configManager.getModel(modelName, "");
		  
		  if(null != filterColumnList && !filterColumnList.isEmpty()){
			  //处理过滤列
			  Map<String,ExcelConfigPropertyParam> columnMap = excelConfigInfo.getColumnMap();
			  Map<String, ExcelConfigPropertyParam> cloneColumnMap = new LinkedHashMap<String, ExcelConfigPropertyParam>();//重新组织的新MAP
			  Assert.notEmpty(columnMap, "不可抛出的异常!");
			
			  int i=1;
			  for(String key : columnMap.keySet()){//for map
				  for(String columnName : filterColumnList){//for list
					  if(columnMap.get(key).getName().equals(columnName)){
						  cloneColumnMap.put(String.valueOf(i), columnMap.get(key));
						  i++;
						 break;
						}
				  }			 			  
				} 
			  excelConfigInfo.setColumnMap(cloneColumnMap);
		  }		 
		
		  ConfigParam param = new ConfigParam();//配置参数
		  param.setExcelConfig(excelConfigInfo);
		  param.setExcelFile(fileName);
		  param.setModelList(modelList);
		  setParam(param);  
		  param.setValueMap(valueMap);
		  param.setMergeCellsParams(mergeCellsParams);//设置合并坐标
		  this.modelToExcel = new ModelToExcel(param);		  
	}

	/* (non-Javadoc)
	 * @see com.gdcn.components.excel.IExportExcelService#setModelName_List(java.lang.String, java.util.List)
	 */
	@Override
	public void setModelName_List(String modelName, List modelList){
	      this.modelToExcel = null;
	      ConfigParam param = getParam();
	      param.setExcelConfig(configManager.getModel(modelName, ""));
	      param.setModelList(modelList);
	  }
	  
	  
	  /* (non-Javadoc)
	 * @see com.gdcn.components.excel.IExportExcelService#getExcelFile()
	 */
	@Override
	public File getExcelFile(){
	    return modelToExcel.getExcelfile();
	  }
	  
		
	public ConfigParam getParam() {
		return param;
	}

	public void setParam(ConfigParam param) {
		this.param = param;
	}
}
