package com.hnjk.extend.plugin.excel.config.impl;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.extend.plugin.excel.config.ConfigConstant;
import com.hnjk.extend.plugin.excel.config.ExcelConfigInfo;
import com.hnjk.extend.plugin.excel.config.ExcelConfigPropertyParam;
import com.hnjk.extend.plugin.excel.config.IExcelConfigManager;
import com.hnjk.extend.plugin.excel.util.ValidateColumn;


/**
 * excel组件配置管理实现. <p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2009-4-29下午02:11:18：
 * @see 
 * @version 1.0
 */
@Component("excelConfigManager")
public class ExcelConfigManagerImpl implements IExcelConfigManager {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
    private String configName = "excel/excel-config.xml";//excel to model映射配置

    private static Long lastModifyTime;
    
    private File configrationFile;
    
    private SAXReader saxReader;

    private Document doc;

    private Element root;
    
    /**
     * 初始化配置，读取配置文件
     *
     */
    @PostConstruct
    public void initConfig() { 
    	String filePath = Thread.currentThread().getContextClassLoader().getResource(configName).getFile();
    	if(ExStringUtils.isNotBlank(filePath)) {
			this.configrationFile = new File(filePath);
		}
    	init();
    	new WatchConfig().start();                  
    }
    
    protected void init(){
    	          
         if(null == configrationFile){
         	logger.warn("Excel组件未启用...");
         }else{        	         	       	
         	 saxReader = new SAXReader();
              try {             	  
                  doc = saxReader.read(configrationFile);
                  saxReader.setValidation(true);
                  lastModifyTime = configrationFile.lastModified();                  
              } catch (DocumentException e) {
                  logger.error(e.getMessage(),e);
              }
              root = doc.getRootElement();
         }
    }
    
   
  
    /**
     * 获取指定模块节点配置元素
     */
    @Override
	public Element getModelElement(String modelName) {
        logger.debug("modelName = " + modelName + "-----------");
        List list = root.elements();
        Element model = null;
        Element returnModel = null;
        for (Iterator it = list.iterator(); it.hasNext();) {
            model = (Element) it.next();
            logger.debug(model.attributeValue("id"));
            if (model.attributeValue("id").equals(modelName)) {
                returnModel = model;
                break;
            }
        }
        if(returnModel==null){
           logger.debug("modelName : " + modelName + " 没有配置");
        }
        return returnModel;
    }

    /**
     * 获取模型的配置信息
     */
    @Override
	public ExcelConfigInfo getModel(String modelName, String flag) {
        Element model = this.getModelElement(modelName);
        ExcelConfigInfo result = new ExcelConfigInfo();
        if (model != null) {
           result.setClassName(model.attributeValue(ConfigConstant.MODEL_CLASS));      
            this.setPropertyMap(result, model);
        }
        return result;
    }

    /*
     * 将配置节点元素写入配置实体
     */
    private void setPropertyMap(ExcelConfigInfo result, Element model) {
        Map<String, ExcelConfigPropertyParam> propertyMap = new HashMap<String, ExcelConfigPropertyParam>();
        Map<String, ExcelConfigPropertyParam> columnMap = new LinkedHashMap<String, ExcelConfigPropertyParam>();
        List list = model.elements();
        Element property = null;

        for (Iterator it = list.iterator(); it.hasNext();) {
            property = (Element) it.next();
            logger.debug("Name = " + property.getName());
            if ("property".equals(property.getName())) {//属性
            	//<property name="sort" column="11" excelTitleName="排序" isNull="Y"  columnWidth="6" maxLength="50" dataType="Integer" fixity="NO" codeTableName="" default="" />
                ExcelConfigPropertyParam modelProperty = new ExcelConfigPropertyParam();
                modelProperty.setName(property.attributeValue(ConfigConstant.PROPERTY_NAME));
                modelProperty.setColumn(property.attributeValue(ConfigConstant.PROPERTY_CLOUMN));
                modelProperty.setExcelTitleName(property.attributeValue(ConfigConstant.PROPERTY_EXCEL_TITLE_NAME));
                modelProperty.setIsNull(property.attributeValue(ConfigConstant.PROPERTY_ISNULL));
                modelProperty.setColumnWidth(property.attributeValue(ConfigConstant.PROPERTY_COLUMN_WIDTH));
                modelProperty.setDataType(property.attributeValue(ConfigConstant.PROPERTY_DATA_TYPE));
                modelProperty.setPattern(property.attributeValue(ConfigConstant.PROPERTY_DATA_PATTERN));
                modelProperty.setMaxLength(property.attributeValue(ConfigConstant.PROPERTY_MAX_LENGTH));
                modelProperty.setFixity(property.attributeValue(ConfigConstant.PROPERTY_FIXITY));
                modelProperty.setCodeTableName(property.attributeValue(ConfigConstant.PROPERTY_CODE_TABLE_NAME));
                modelProperty.setDefaultValue(property.attributeValue(ConfigConstant.PROPERTY_DEFAULT));

                logger.debug("name = "+property.attributeValue("name"));
                
                String excelTitle = ValidateColumn.configValidate(propertyMap,modelProperty.getExcelTitleName());
                propertyMap.put(excelTitle, modelProperty);
                
                logger.debug("column = " + modelProperty.getColumn());
                columnMap.put(modelProperty.getColumn(), modelProperty);
            }
            if ("flag".equals(property.getName())) {//标示
                Map<String, String> flagMap = new HashMap<String, String>();
                flagMap.put(ConfigConstant.PROPERTY_NAME, property.attributeValue(ConfigConstant.PROPERTY_NAME));
                result.setFlagMap(flagMap);
            }
            if ("message".equals(property.getName())) {//提示消息
                Map<String, String> messageMap = new HashMap<String, String>();
                messageMap.put(ConfigConstant.PROPERTY_NAME, property.attributeValue(ConfigConstant.PROPERTY_NAME));
                messageMap.put(ConfigConstant.PROPERTY_EXCEL_TITLE_NAME, property.attributeValue(ConfigConstant.PROPERTY_EXCEL_TITLE_NAME));
                result.setMessageMap(messageMap);
            }
        }
        result.setPropertyMap(propertyMap);
        result.setColumnMap(columnMap);
    }

    class WatchConfig extends Thread{
	    	public WatchConfig(){
	    		super("Excel配置跟踪器已启动...");
	    		setDaemon(true);
	    	}
	    	
	    	@Override
			public void run(){
	    		long sleepTime = 10 * 1000; //10秒扫描一次
				while (true) {
					try {
						Thread.sleep(sleepTime);
						
						 if(null != configrationFile){									 
							 if (configrationFile.lastModified() > lastModifyTime) {
									init();
									logger.info(configrationFile.getName() + " 已更改，重新载入...");
								} 
						 }
						
					} catch (Throwable throwable) {
						logger.error("Excel配置跟踪器错误：" + throwable, throwable);
					}
	
	    	}
	    }
    }
}
