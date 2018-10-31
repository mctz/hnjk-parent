package com.hnjk.extend.plugin.excel.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.hnjk.extend.plugin.excel.config.ConfigParam;
import com.hnjk.extend.plugin.excel.config.IExcelConfigManager;
import com.hnjk.extend.plugin.excel.file.ExcelToModel;
import com.hnjk.extend.plugin.excel.file.ModelToExcelMessage;
import com.hnjk.extend.plugin.excel.service.IImportExcelService;
import com.hnjk.extend.plugin.excel.util.DateUtils;

/**
 * excel组件导入管理实现. <p>
 * 实现了读取excel并转成list。
 * @author： 广东学苑教育发展有限公司.
 * @since： 2009-4-29下午12:14:39
 * @see ExcelToModel
 * @version 1.0
 */
@Component("importExcelService")
public class ImportExcelServiceImpl implements IImportExcelService {
       
    @Autowired
	@Qualifier("excelConfigManager")
    private IExcelConfigManager configManager;//配置类    
        
    public void setConfigManager(IExcelConfigManager configManager) {
		this.configManager = configManager;
	}
    
    private ConfigParam configParam;//配置参数
    
    private ExcelToModel excelToModel;//Excel转换为Model的Action方法
	
    private ModelToExcelMessage modelToExcelMessage;//将Model转为Excel的方法    
    
    
  	@Override
	public ExcelToModel getExcelToModel() {
		return excelToModel;
	}

	@Override
	public void setExcelToModel(ExcelToModel excelToModel) {
		this.excelToModel = excelToModel;
	}

	public ConfigParam getConfigParam() {
		return configParam;
	}

  	public void setConfigParam(ConfigParam configParam) {
		this.configParam = configParam;
	}


	/*
	 * 初始化
	 * (non-Javadoc)
	 * @see com.gdcn.components.excel.IImportExcelService#initParmas(java.io.File, java.lang.String, java.util.Map)
	 */
	@Override
	public void initParmas(File fileName, String modelName, Map valueMap){
		//配置参数
		ConfigParam param = new ConfigParam();
		param.setExcelConfig(configManager.getModel(modelName, ""));
		param.setExcelFile(fileName);
		param.setValueMap(valueMap);
		setConfigParam(param);
		this.excelToModel = new ExcelToModel(param);
	}

  
    /* (non-Javadoc)
	 * @see com.gdcn.components.excel.IExportExcelService#getModelList()
	 */
   @Override
   public List getModelList() {
        if (isValidateExcelFormat()) {          
            return excelToModel.getModelList();
        }
        return null;
    }
    
    /* (non-Javadoc)
	 * @see com.gdcn.components.excel.IExportExcelService#isValidateExcelFormat()
	 */
    @Override
	public boolean isValidateExcelFormat() {
        return excelToModel.validateExcelFormat();
    }

    /* (non-Javadoc)
	 * @see com.gdcn.components.excel.IExportExcelService#getConfigTitle()
	 */
    @Override
	public Map getConfigTitle() {
        return excelToModel.getConfigTitle();
    }
    
    /* (non-Javadoc)
	 * @see com.gdcn.components.excel.IExportExcelService#getSucessModelList()
	 */
    @Override
	public List getSucessModelList() {
        if (isValidateExcelFormat()) {
            return excelToModel.getSuccessModelList();
        }
        return null;
    }

    /* (non-Javadoc)
	 * @see com.gdcn.components.excel.IExportExcelService#getErrorModelList()
	 */
    @Override
	public List getErrorModelList() {
        if (isValidateExcelFormat()) {
            return excelToModel.getErrorModelList();
        }
        return null;
    }
    
   
    /* 
     * (non-Javadoc)
	 * @see com.gdcn.components.excel.IExportExcelService#getExcelFile(java.io.File, java.util.List)
	 */
    @Override
	public File getExcelFile(File excelFile, List modelList) {
        return this.getFile(excelFile, modelList, "-1");
    }
    
    /* (non-Javadoc)
	 * @see com.gdcn.components.excel.IExportExcelService#getSucessExcelFile(java.io.File, java.util.List)
	 */
    @Override
	public File getSucessExcelFile(File excelFile, List modelList) {
        return this.getFile(excelFile, modelList, "0");
    }
    
    /* (non-Javadoc)
	 * @see com.gdcn.components.excel.IExportExcelService#getErrorExcelFile(java.io.File, java.util.List)
	 */
    @Override
	public File getErrorExcelFile(File excelFile, List modelList) {
        return this.getFile(excelFile, modelList, "1");
    }
    
    /* (non-Javadoc)
	 * @see com.gdcn.components.excel.IExportExcelService#getExcelFile(java.util.List)
	 */
    @Override
	public File getExcelFile(List modelList) {
        return this.getFile(null, modelList, "-1");
    }
    
    /* (non-Javadoc)
	 * @see com.gdcn.components.excel.IExportExcelService#getSucessExcelFile(java.util.List)
	 */
    @Override
	public File getSucessExcelFile(List modelList) {
        return this.getFile(null, modelList, "0");
    }

    /* (non-Javadoc)
	 * @see com.gdcn.components.excel.IExportExcelService#getErrorExcelFile(java.util.List)
	 */
    @Override
	public File getErrorExcelFile(List modelList) {
        return this.getFile(null, modelList, "1");
    }

    /**
     * 获取转换后的excel文件
     * @param excelFile
     * @param modelList
     * @param strFlag
     * @return
     */
    private File getFile(File excelFile, List modelList, String strFlag) {    	
        if (modelToExcelMessage == null) {
            if (excelFile == null){
            	modelToExcelMessage = new ModelToExcelMessage(getConfigParam());
            } else{
            	ConfigParam param = getConfigParam();
            	param.setExcelFile(excelFile);
            	modelToExcelMessage = new ModelToExcelMessage(param);
            }
        }
        modelToExcelMessage.setStartTitleRow(excelToModel.getStartTitleRow());
        modelToExcelMessage.setIntSheet(excelToModel.getIntSheet());
        if ("-1".equals(strFlag)) {
            return modelToExcelMessage.getFile();
        } else if ("0".equals(strFlag)) {
            return modelToExcelMessage.getSuccessFile();
        } else {
            return modelToExcelMessage.getErrorFile();
        }
    }

    
    /**
     * 配制文件加强,一是可以传一个固定值到所有Bena中. 配制一个固定值.把固定值在RuturnConfig 单设成一个Map
     * ,配制文件中,对固定值的配制必须有默认值, ExcelToModelImpl
     * 实现时,根据Excel列,属性设置完成后,对Map循环,设置其值.首先从传参Map中取值,没有取默认值设置.
     * 二是可以配制转换对应的码表值.如:excel中传的值为"长期" 可以配制成 "bgqx长期"做为键值 "C" 做为 Bean 设置的值.
     */
    public static void main(String[] args) {
    	   Map<String, String> map = new HashMap<String, String>();
           map.put("deptNo", "1");
           map.put("bgqx永久", "Y");
           map.put("bgqx长期", "C");
           map.put("bgqx短期", "D");
           ImportExcelServiceImpl test = new ImportExcelServiceImpl();
           test.initParmas(new File("D:\\work\\workspace\\excelfile\\test.xls"), "deptModel", map);
           //ExcelManager test = new ExcelManager("D:\\work\\workspace\\excelfile\\ym.xls", "importExcelForm", map);
           //设置对应取第几个sheet内的值。
           test.excelToModel.setSheet(0);
           //设置取目标sheet的名称
           //test.getEtm().setSheet("Sheet2");
           // 按列读取
           // test.getEtm().setUseColumn(true);
           List modelList = test.getModelList();

//           System.out.println("--" + ToStringBuilder.reflectionToString(test.getConfigTitle().entrySet().toArray()));
//           // System.out.println("size = " + modelList.size());
           for (int i = 0; i < modelList.size(); i++) {
               Object obj = modelList.get(i);
               System.out.println("--" + obj.getClass().getSimpleName());
               System.out.println("--" + ToStringBuilder.reflectionToString(obj));
           }
           // 如果Class配制成HashMap，下面可以测试。
           for (int i = 0; i < modelList.size(); i++) {
               Object oo = modelList.get(i);
               if (oo instanceof Map) {
                   Map obj = (Map) oo;
                   System.out.println("--" + obj.getClass().getSimpleName());
                   System.out.println("--" + ToStringBuilder.reflectionToString(obj.keySet().toArray()));
                   System.out.println("--" + ToStringBuilder.reflectionToString(obj.entrySet().toArray()));
               }
           }
           System.out.println("---------======================--------------------");
       
            test.getExcelFile(modelList);

    }

    /* (non-imediatelyModelExcelImport)
     * 针对无法直接转化为model的excel表导入时，获取excel表内容
     */
   @Override
   public List getModelListWithoutConfig() {
	   Logger logger = LoggerFactory.getLogger(getClass());
	   List<Map<String,String>> resultList = new ArrayList<Map<String,String>>(0);
	   try {
           Workbook book = Workbook.getWorkbook(excelToModel.getParam().getExcelFile());//获取excel book	          
           Sheet sheet;	            
           if(StringUtils.isNotBlank(excelToModel.getStrSheet())){//获取excel sheet
               sheet = book.getSheet(excelToModel.getStrSheet());
           }else{
               sheet = book.getSheet(excelToModel.getIntSheet());
           }  	            
           logger.debug("JXL is running, excel version : " + Workbook.getVersion());
           
           //遍历sheet行
           logger.info("获取Excel sheet表格{}行...",sheet.getRows());
           for (int i = 0; i < sheet.getRows(); i++) {
               // 遍历excel每一列的值进行取值,并做数据校验.
               Map<String,String> map = new HashMap<String, String>(0);
        	   for (int j = 0; j < sheet.getColumns(); j++) {	                    
                   String excelTitleName = sheet.getCell(j, 0).getContents().trim();// 取得Excel表头	                    
                   String value = sheet.getCell(j, i).getContents();   // 取得Excel值  
                   Object objvalue = value;
                   Cell strCell = sheet.getCell(j, i);
                   if (strCell.getType() == CellType.DATE) {
                       DateCell dateCell = (DateCell) strCell;
                       Date date = dateCell.getDate();
                       long time=(date.getTime()/1000)-60*60*8;//Excel中的时间要减去8小时，为什么呢?
                       date.setTime(time*1000);
                       try{
                           	objvalue = DateUtils.getFormatDate(date, "yyyy-MM-dd");	
                           }catch(Exception e){
                           	logger.error(e.getMessage());
                         }
                   } 
                   map.put(String.valueOf(j), objvalue.toString());
                   logger.debug("get value:{}",objvalue);
               }
               resultList.add(map);
           }
           book.close();
       } catch (BiffException e) {
         logger.error(e.getMessage(),e);
       } catch (IOException e) {
       	logger.error(e.getMessage(),e);
       }        
      return resultList;
       
   }

}
