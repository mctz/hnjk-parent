package com.hnjk.extend.plugin.excel.file;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.extend.plugin.excel.config.ConfigParam;
import com.hnjk.extend.plugin.excel.config.ExcelConfigPropertyParam;
import com.hnjk.extend.plugin.excel.util.DateUtils;
import com.hnjk.extend.plugin.excel.util.ValidateColumn;

/**
 * excel转换为model <p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2009-4-29上午11:09:05
 * @see 
 * @version 1.0
 */
public class ExcelToModel {
		protected Logger logger = LoggerFactory.getLogger(getClass());		
	 	
	    private List fixityList = null;//固定列
	    // 完整的list
	    private List<Object> modelList = new ArrayList<Object>();
	    // 验证成功的List
	    private List<Object> successModelList = new ArrayList<Object>();
	    // 验证失败的List
	    private List<Object> errorList = new ArrayList<Object>();
	    // 表头验证List
	    private List<String> messageList = null;
	    
	    /*
	     * 是否按列读取，当出现有列标题重复时，可以按些方法来操作
	     * isUserColumn = true时，按配制文件中配制的列号读取
	     */
	    private boolean isUseColumn = false;
	    // 读取操作是否执行，执行完成后，会填充modelList,successModelList,errorList数据。
	    private boolean isRead = false;
	    // 支持带有标题的Excel文件时，保存规则数据开始的行数，及有效列数（就是配制文件中配制的excel列头在实际excel文件出的第几列数）
	    private int startTitleRow = 0;	  
	    
	   

		//支持传入sheet 值，指取第几个sheet内的值。　修改时间　2008－1－24	    	   
	    private int intSheet=0;// intSheet　sheet　在 Excel中第几个
	    
	    private String strSheet=null;// strSheet　sheet 在Excel中的名称	    
	    
	    private List excelTitleList = new ArrayList();//为excel文件中，出现重复列配制
	    
	    private ConfigParam param;//配置参数
	    	    
	    public ConfigParam getParam() {
			return param;
		}

		public void setParam(ConfigParam param) {
			this.param = param;
		}
		
		public ExcelToModel(){}
		
		/**
		 * 构造方法
		 * @param param 配置参数
		 */
		public ExcelToModel(ConfigParam param){
			this.param = param;
		}
		
		/*
	     * 对于配置文件中,指定取固定值的属性列,先取出放到List中.无则为空
	     */
	    private void setFixity() {
	        List<ExcelConfigPropertyParam> list = new ArrayList<ExcelConfigPropertyParam>();
	        Map pmap = param.getExcelConfig().getPropertyMap();
	        for (Iterator it = pmap.values().iterator(); it.hasNext();) {
	            ExcelConfigPropertyParam propertyBean = (ExcelConfigPropertyParam) it.next();
	            if (null != propertyBean.getFixity() && "YES".equals(propertyBean.getFixity().toUpperCase().trim())) {
	                list.add(propertyBean);
	            }
	        }
	        this.fixityList = list;
	    }
	    
	    /*
	     * 根据标题方式读取EXCEL
	     *
	     */
	    @SuppressWarnings({ "unchecked", "static-access" })
		private void getExcelToModelListByTitle() {	      
	        logger.info("Excel文件开始读取行数：" + (startTitleRow + 1));
	        try {
	            Workbook book;
	            book = Workbook.getWorkbook(param.getExcelFile());	            
	          
	            Sheet sheet;
	            if(StringUtils.isNotBlank(this.getStrSheet())){
	                sheet = book.getSheet(this.getStrSheet());
	            }else{
	                sheet = book.getSheet(this.getIntSheet());
	            }           
	             
	            logger.info("JXL version : " + Workbook.getVersion());
	            // 设置连续空行的变量，如果有连续10个空行，就停止取数
	            int intContinueCount = 0;
				// 错误消息
				StringBuilder strMessage = new StringBuilder();
	            for (int i = startTitleRow + 1; i < sheet.getRows(); i++) {
	                int intcount = getColumnByValue(sheet.getRow(i));
	                if (intcount < 1) {
	                    intContinueCount++;
	                    if (intContinueCount < 11) {
	                        continue;
	                    } else {
	                        break;
	                    }
	                } else {
	                    intContinueCount = 0;
	                }

	                Object obj = this.getModelClass(param.getExcelConfig().getClassName());//获取配置中的POJO

	                // 错误标志
	                boolean flag = false;
					strMessage.setLength(0);

	                // 对excel每一列的值进行取值.
	                for (int j = 0; j < sheet.getColumns(); j++) {	                   
	                	String excelTitleName = (String) this.excelTitleList.get(j);//获取excel标题头
	                    String value = sheet.getCell(j, i).getContents().trim();// 取得Excel值
	                    if(logger.isDebugEnabled()) {
							logger.debug("get value:{}",value);
						}
	                   
	                    Object objValue = value; //定义一个对象，用来临时存储转换后的值	                 			
	                   
	                    // 取得配置属性
	                    ExcelConfigPropertyParam propertyBean = (ExcelConfigPropertyParam) param.getExcelConfig().getPropertyMap().get(excelTitleName);
                
	                    // 列错误标志
	                    boolean columnFlag = false;
	                    if (propertyBean != null) {	                   
	                        // 1.判断是否非空，非空标志 ,取值为空时设置为false
	                        boolean nullFlag = true;
	                        if ("N".equals(propertyBean.getIsNull())) {
	                            if (ExStringUtils.isEmpty(ExStringUtils.trimToEmpty(value))) {	   
	                                nullFlag = false; // 非空标志
	                                flag = true;
	                                strMessage.append("第[").append(i+1).append("]行[").append(j+1).append("]列:").append(excelTitleName).append(" - 不能为空！");
	                                continue;
	                            }
	                        }
	                        
	                        //2.做出判断,是否需要 Text/Value 值转换.
	                        if (null!=propertyBean.getCodeTableName()&&propertyBean.getCodeTableName().length() > 1) {
	                            String valueKey = propertyBean.getCodeTableName().trim()+"_" + value;	                           
	                            value = ExStringUtils.defaultIfEmpty((null!=param.getValueMap()&&param.getValueMap().containsKey(valueKey))?param.getValueMap().get(valueKey).toString():"", "");
	                            logger.info("转换值:"+value);
	                        }
	                        //3.设置默认值 value 先是取值
	                        if (value == null || value.length() < 1) {
	                            value = propertyBean.getDefaultValue();
	                        }
	                        //4.如果取值不空
	                        if (nullFlag) {
	                            //4.1长度判断
	                            int intLength = 0;
	                            if (StringUtils.isNotBlank(propertyBean.getMaxLength()) && (NumberUtils.isNumber(propertyBean.getMaxLength()))) {
	                                intLength = Integer.parseInt(propertyBean.getMaxLength());
	                            }
	                            //4.2如果设置了最大长度，从右边截取指定的字符长度
	                            if (intLength > 0 && value.length() > intLength) {
	                                value = value.substring(0, intLength);
	                            }
	                            //4.3判断数据类型 支持 String ,Integer,Date
	                            if (propertyBean.getDataType().indexOf("Date") > -1) { //日期	                                
	                                Cell strCell = sheet.getCell(j, i);
	                                if (strCell.getType() == CellType.DATE) {
	                                    DateCell dateCell = (DateCell) strCell;
	                                    Date date = dateCell.getDate();	       
	                                    long time=(date.getTime()/1000)-60*60*8;//Excel中的时间要减去8小时，why?
	                                    date.setTime(time*1000);	                                   
	                                    try{
	                                    if(!ExStringUtils.isEmpty(propertyBean.getPattern())){
	                                    		SimpleDateFormat sdf = new SimpleDateFormat(propertyBean.getPattern(),java.util.Locale.CHINA);
	                                    		objValue = sdf.parse(DateUtils.getFormatDate(date, ""));	
	                                    	}else{
	                                    		objValue = ExDateUtils.DATE_FORMAT.parse(DateUtils.getFormatDate(date, ""));	
	                                    	}
	                                    }catch(Exception e){
	                                    	logger.error(e.getMessage());
	                                    }
	                                } else {	                                	
	                                	 flag = true;
		                                 columnFlag = true;
		                                 strMessage.append("第[").append(i+1).append("]行[").append(j+1).append("]列:").append(excelTitleName).append("-日期格式不正确！");
		                                 continue;
	                                }	                                                              
	                            }else if ("Integer".equals(propertyBean.getDataType())) {//数字
	                                if (!NumberUtils.isNumber(value)) {
	                                    flag = true;
	                                    columnFlag = true;
	                                    strMessage.append("第[").append(i+1).append("]行[").append(j+1).append("]列:").append(excelTitleName).append("-不是数字！");
	                                    continue;
	                                }
	                                objValue = new Integer(value);
	                            }else{
	                            	objValue = value;
	                            }
	                            
	                        }
	                        //根据不同类型，做对象的封装，(以后还要提供对float类型的支持)便于转换 FIXME 2009.7.15
	                        //if(propertyBean.getDataType().equals("Integer")){
	                        //	objValue = new Integer(value);
	                      //  }else if(propertyBean.getDataType().indexOf("Date") > -1){
	                      //  	objValue = ExDateUtils.convertToDate(value);
	                      //  }else{
	                      //  	objValue = value;
	                     //  }                      	                       
	                        
	                        if (!columnFlag) {	                          	                        	
	                            this.setPropertyValue(obj, propertyBean.getName(), objValue);
	                            if(logger.isDebugEnabled()) {
									logger.debug(propertyBean.getName()+ " = "+ objValue +" = "+propertyBean.getDataType());
								}
	                        }
	                    }
	                }

	                /*
	                 * 设置错误标志，及错误消息
	                 * 1 - 表示有错误
	                 * 0 - 没错误
	                 */
	                if (flag) {//如果有错误
	                    String propertyFlag = (String) param.getExcelConfig().getFlagMap().get("name");
	                    String propertyMessage = (String) param.getExcelConfig().getMessageMap().get("name");
	                    // 调整支持Map
	                    if (StringUtils.isNotBlank(propertyFlag)) {
	                        this.setPropertyValue(obj, propertyFlag, "1");
	                    }

	                    // 调整支持Map
	                    if (StringUtils.isNotBlank(propertyMessage)) {
	                        this.setPropertyValue(obj, propertyMessage, strMessage.toString());
	                    }
	                    //直接抛出异常，显示错误信息
	                    throw new WebException(strMessage.toString());

	                } else {//如果没有错误
	                    String propertyFlag = (String) param.getExcelConfig().getFlagMap().get("name");
	                    String propertyMessage = (String) param.getExcelConfig().getMessageMap().get("name");
	                    // 调整支持Map     
	                    if (StringUtils.isNotBlank(propertyFlag)) {
	                        this.setPropertyValue(obj, propertyFlag, "0");
	                    }

	                    // 支持Map时，保持Map中每个属性都存在。
	                    if (StringUtils.isNotBlank(propertyMessage)) {
	                        this.setPropertyValue(obj, propertyMessage, "");
	                    }
	                }
	               
	                //对在配制文件中指定需要设置为固定值的属性，设置属性中的固定值，根据传入的Map中取出，无则取其默认值	               
	                for (Iterator it = this.fixityList.iterator(); it.hasNext();) {
	                    ExcelConfigPropertyParam propertyBean = (ExcelConfigPropertyParam) it.next();
	                    Object value = this.param.getValueMap().get(propertyBean.getName());
	                    if (value == null || value.toString().length() < 1) {
	                        value = propertyBean.getDefaultValue();
	                    }
	                    // 调整支持Map	                    
	                    this.setPropertyValue(obj, propertyBean.getName(), value);
	                }
	                 this.modelList.add(obj);
	                if (flag) {//添加到错误列表中
	                    this.errorList.add(obj);
	                } else {
	                    this.successModelList.add(obj);
	                }
	            }
	            book.close();
	        } catch (BiffException e) {
	        	logger.error(e.getMessage(),e);
	        } catch (IOException e) {
	        	logger.error(e.getMessage(),e);
	        }
	    }
	    
	   
	    /*
	     *按配置列读取excel的数据 
	     *
	     */
	    @SuppressWarnings("static-access")
		private void getExcelToModelListByColumn() {
	        this.setFixity();	      
	        try {
	            Workbook book = Workbook.getWorkbook(param.getExcelFile());//获取excel book	          
	            Sheet sheet;	            
	            if(StringUtils.isNotBlank(this.getStrSheet())){//获取excel sheet
	                sheet = book.getSheet(this.getStrSheet());
	            }else{
	                sheet = book.getSheet(this.getIntSheet());
	            }  	            
	            logger.debug("JXL is running, excel version : " + Workbook.getVersion());
	            
	            //遍历sheet行
	            logger.info("获取Excel sheet表格{}行...",sheet.getRows());
				StringBuilder strMessage = new StringBuilder();// 错误消息
	            for (int i = 1; i < sheet.getRows(); i++) {
	                Object obj = this.getModelClass(param.getExcelConfig().getClassName());//excel model
         
	                boolean flag = false; // 错误标志	                
					strMessage.setLength(0);

	                // 遍历excel每一列的值进行取值,并做数据校验.
	                for (int j = 0; j < sheet.getColumns(); j++) {	                    
	                    String excelTitleName = sheet.getCell(j, 0).getContents().trim();// 取得Excel表头	                    
	                    String value = sheet.getCell(j, i).getContents().trim();   // 取得Excel值  
	                    logger.debug("get value:{}",value);
	                    
	                    Object objValue = value;                   
	                    
	                    // 按配制列号读取Excel文件，只修改了下面的几行代码，注释上面一行代码。	                    
	                    String keycolumn = String.valueOf(j + 1);
	                    ExcelConfigPropertyParam propertyBean = (ExcelConfigPropertyParam) param.getExcelConfig().getColumnMap().get(keycolumn);
	                    logger.debug("get column: {} 's" , keycolumn);	                    

	                    // 列错误标志
	                    boolean columnFlag = false;
	                    if (propertyBean != null) {
	                        logger.debug("propertyName :{}" , propertyBean.getName());
	                        // 判断是否非空，非空标志 ,取值为空时设置为false
	                        boolean nullFlag = true;
	                        //1.判断非空
	                        if ("N".equals(propertyBean.getIsNull())) {
	                            if (ExStringUtils.isEmpty(value)) {	                               
	                                nullFlag = false; // 非空标志
	                                flag = true;
	                                strMessage.append(";列：").append(excelTitleName).append("-为空！");
	                                continue;
	                            }
	                        }
	                        //4.是否需要 Text/Value 值转换，传入的key必须是codetableName+_+值
	                        if (propertyBean.getCodeTableName().length() > 1) {
	                            String valueKey = propertyBean.getCodeTableName().trim() + "_"+value;	                          
	                            value = ExStringUtils.defaultIfEmpty(this.param.getValueMap().get(valueKey).toString(), "");
	                            logger.debug("转换值: {}",value);
	                        }
	                        // 设置默认值 value 先是取值，再判断是否有码表对换值
	                        if (value == null || value.length() < 1) {
	                            value = propertyBean.getDefaultValue();
	                        }
	                        // 如果取值不空
	                        if (nullFlag) {
	                            //2.判断长度
	                            int intLength = 0;
	                            if (StringUtils.isNotBlank(propertyBean.getMaxLength()) && (NumberUtils.isNumber(propertyBean.getMaxLength()))) {
	                                intLength = Integer.parseInt(propertyBean.getMaxLength());
	                            }
	                            // 如果设置了最大长度，从右边截取指定的字符长度
	                            if (intLength > 0 && value.length() > intLength) {
	                                value = value.substring(0, intLength);
	                            }
	                            //3.判断数据类型： 支持 String ,Integer,Date
	                            if (propertyBean.getDataType().indexOf("Date") > -1) {//日期 
	                                Cell strCell = sheet.getCell(j, i);
	                                if (strCell.getType() == CellType.DATE) {
	                                    DateCell dateCell = (DateCell) strCell;
	                                    Date date = dateCell.getDate();
	                                    long time=(date.getTime()/1000)-60*60*8;//Excel中的时间要减去8小时，why?
	                                    date.setTime(time*1000);
	                                    try{
		                                    if(!ExStringUtils.isEmpty(propertyBean.getPattern())){
		                                    		SimpleDateFormat sdf = new SimpleDateFormat(propertyBean.getPattern());
		                                    		objValue = sdf.parse(DateUtils.getFormatDate(date, ""));	
		                                    	}else{
		                                    		objValue = ExDateUtils.DATE_FORMAT.parse(DateUtils.getFormatDate(date, ""));	
		                                    	}
		                                    }catch(Exception e){
		                                    	logger.error(e.getMessage());
		                                  }
	                                } 
	                                if (value == null) {
	                                    flag = true;
	                                    columnFlag = true;
	                                    strMessage.append("列：").append(excelTitleName).append("-日期格式不正确！");
	                                    continue;
	                                }	                            
	                            }else if ("Integer".equals(propertyBean.getDataType())) {//整数
	                                if (!NumberUtils.isNumber(value)) {
	                                    flag = true;
	                                    columnFlag = true;
	                                    strMessage.append("列：").append(excelTitleName).append("-不是数字！");
	                                    continue;
	                                }
	                                objValue = new Integer(value);
	                            }else{//其他
	                            	objValue = value;
	                            }
	                        }
	                        	
	                        //  根据不同类型，做对象的封装，便于转换 FIXME 2009.7.15
	                      //  if(propertyBean.getDataType().equals("Integer")){
	                     //   	objValue = new Integer(value);
	                     //  }else if(propertyBean.getDataType().indexOf("Date") > -1){
	                     //   	objValue = ExDateUtils.convertToDate(value);
	                     //   }else{
	                     //   	objValue = value;
	                     //   }  
	                        if (!columnFlag) {
	                            // 调整支持Map	                          
	                            this.setPropertyValue(obj, propertyBean.getName(), value);	                           
	                        }
	                    }

	                }
	                /*
	                 * 设置错误标志，及错误消息
	                 */
	                if (flag) {//如果有错误
	                    String propertyFlag = (String) param.getExcelConfig().getFlagMap().get("name");
	                    String propertyMessage = (String) param.getExcelConfig().getMessageMap().get("name");
	                    // 调整支持Map	             
	                    this.setPropertyValue(obj, propertyFlag, "1");
	                   logger.debug(propertyMessage + " = " + strMessage.toString());
	                    // 调整支持Map	                 
	                    this.setPropertyValue(obj, propertyMessage, strMessage.toString());
	                    
	                    //直接抛出异常，显示错误信息
	                    throw new WebException(strMessage.toString());
	                } else {
	                    String propertyFlag = (String) param.getExcelConfig().getFlagMap().get("name");
	                    String propertyMessage = (String) param.getExcelConfig().getMessageMap().get("name");
	                    // 调整支持Map	               
	                    this.setPropertyValue(obj, propertyFlag, "0");
	                    // 支持Map时，保持Map中每个属性都存在。
	                    this.setPropertyValue(obj, propertyMessage, "");
	                }
	                /*
	                 * 对在配制文件中指定需要设置为固定值的属性，设置属性中的固定值，根据传入的Map中取出，无则取其默认值
	                 */
	                for (Iterator it = this.fixityList.iterator(); it.hasNext();) {
	                    ExcelConfigPropertyParam propertyBean = (ExcelConfigPropertyParam) it.next();
	                    Object value = this.param.getValueMap().get(propertyBean.getName());
	                    if (value == null || value.toString().length() < 1) {
	                        value = propertyBean.getDefaultValue();
	                    }
	                    // 调整支持Map	               
	                    this.setPropertyValue(obj, propertyBean.getName(), value);
	                }
	                this.modelList.add(obj);
	                if (flag) {
	                    this.errorList.add(obj);
	                } else {
	                    this.successModelList.add(obj);
	                }

	            }
	            book.close();
	        } catch (BiffException e) {
	          logger.error(e.getMessage(),e);
	        } catch (IOException e) {
	        	logger.error(e.getMessage(),e);
	        }
	    }

	    /**
	     * 设置对象属性
	     * @param obj
	     * @param property
	     * @param value
	     */
	    @SuppressWarnings("unchecked")
		private void setPropertyValue(Object obj, String property, Object value) {
	        if (obj instanceof Map) {
	            ((Map) obj).put(property, value);
	        } else {
	            BeanWrapper bw = new BeanWrapperImpl(obj);	            
	            bw.setPropertyValue(property, value);
	        }
	    }
	    
	    /**
	     * 获取模型类
	     * @param className
	     * @return
	     */
	    private Object getModelClass(String className) {
	        Object obj = null;
	        try {
	            obj = Class.forName(className).newInstance();
	            logger.info("init Class = " + className);
	        } catch (ClassNotFoundException e) {
	           logger.error(e.getMessage(),e);
	        } catch (InstantiationException e) {
	        	logger.error(e.getMessage(),e);
	        } catch (IllegalAccessException e) {
	        	logger.error(e.getMessage(),e);
	        }
	        return obj;
	    }

	    /*
	     * 验证excel标头格式是否正确
	     * (non-Javadoc)
	     * @see com.gdcn.components.excel.file.ExcelToModel#validateExcelFormat()
	     */
	    public boolean validateExcelFormat() {	      
	        boolean boolResult = true;
	        List titleList = this.getExcelTitle();	        
	        
	        // 对excel列做处理，把出现相同的列加编号 如果出现两个　姓名 做成 姓名，姓名_1	        
	        List<String> tempList = new ArrayList<String>();
	        for(int i=0;i<titleList.size();i++){
	        	String temp = ValidateColumn.columnValidate(tempList,titleList.get(i).toString());
	        	tempList.add(temp);
	        }
	        
	        messageList = new ArrayList<String>();
	        Map pmap = this.param.getExcelConfig().getPropertyMap();
	        for (Iterator it = pmap.keySet().iterator(); it.hasNext();) {
	            String title = (String) it.next();	        
	            if (!tempList.contains(title)) {
	                boolResult = false;
	                messageList.add("Excel中不存在 " + title + " 列！");
	                logger.info("Excel中不存在 " + title + " 列!");
	            }
	        }
	        // 保存验证通过的，已经被处理的excel标题	        
	        setExcelTitleList(tempList);	        
	        return boolResult;
	    }
	    
	    /*
	     * 获取excel标题
	     */
	    private List getExcelTitle() {
	        this.setFixity();
	        List<String> titleList = new ArrayList<String>();
	        try {
	            Workbook book;
	            book = Workbook.getWorkbook(param.getExcelFile()); 
	            Sheet sheet;
	            if(StringUtils.isNotBlank(this.getStrSheet())){
	                sheet = book.getSheet(this.getStrSheet());
	            }else{
	                sheet = book.getSheet(this.getIntSheet());
	            }    
	            // 验证列头开始的行数，并设置列头对应的有效列数
	            for (int i = 0; i < sheet.getRows(); i++) {
	                // 验证一行有值的列数
	                int intcount = this.getColumnByValue(sheet.getRow(i));
	                // 属性列 - 固定值列数 = 必须配制的列数
	                int intNotNullColumn = param.getExcelConfig().getPropertyMap().size() - fixityList.size();
	                if (intNotNullColumn <= intcount) {	                   
	                    // 验证时，只要出现的第一行的有值列数大于配制文件中配制的列数，就认为是列头
	                    startTitleRow = i;
	                    break;
	                }
	            }
	            //遍历，取出列头
	            for (int j = 0; j < sheet.getColumns(); j++) {
	                String title = sheet.getCell(j, startTitleRow).getContents().trim();
	                titleList.add(title);
	            }
	            book.close();
	        } catch (BiffException e) {
	           logger.error(e.getMessage(),e);
	        } catch (IOException e) {
	        	logger.error(e.getMessage(),e);
	        }catch(Exception e){
	        	logger.error(e.getMessage(), e);
	        }
	        return titleList;
	    }
	    
	    /**
	     * 得到配置标题
	     */
	    public Map getConfigTitle() {	      
	        Map<String, String> titleMap = new HashMap<String, String>();
	        Map pmap = param.getExcelConfig().getPropertyMap();
	        for (Iterator it = pmap.keySet().iterator(); it.hasNext();) {
	            String configTitle = (String) it.next();
	            ExcelConfigPropertyParam propertyBean = (ExcelConfigPropertyParam) param.getExcelConfig().getPropertyMap().get(configTitle);
	            titleMap.put(propertyBean.getName(), configTitle);
	        }
	        return titleMap;
	    }

	    public List getModelList() {
	        return this.getExcelToList(0);
	    }

	    public List getErrorModelList() {	        
	        return this.getExcelToList(2);
	    }

	    public List getSuccessModelList() {	  
	        return this.getExcelToList(1);
	    }
	    
	    /*
	     * 获取操作列表
	     */
	    private List getExcelToList(int intFlag) {
	        if (!isRead) {
	            this.isRead = true;
	            getExcelToModelList();//全部
	        }
	        if (intFlag == 0) {
	            return this.modelList;
	        } else if (intFlag == 1) {
	            return this.successModelList;
	        } else if (intFlag == 2) {
	            return this.errorList;
	        } else {
	            return new ArrayList();
	        }
	    }

	    /*
	     * 获取EXCEL转换成model的列表
	     * 支持按列方式和按标题方式
	     */
	    private void getExcelToModelList() {
	        if (isUseColumn) {
	            getExcelToModelListByColumn();
	        } else {
	            getExcelToModelListByTitle();
	        }
	    }
	    
	   
	    public boolean isUseColumn() {
	        return isUseColumn;
	    }

	    public void setUseColumn(boolean isUseColumn) {
	        this.isUseColumn = isUseColumn;
	    }

	    // 取得当前行有值的列数
	    private int getColumnByValue(Cell[] rowCell) {
	        int column = rowCell.length;
	        int intResult = column;
	        for (int i = 0; i < column; i++) {
	            String str = rowCell[i].getContents();
	            if (ExStringUtils.isEmpty(str)) {
	                intResult--;
	            }
	        }
	        return intResult;
	    }

	    public int getStartTitleRow() {	    
	        return startTitleRow;
	    }

	    public int getIntSheet() {
	        return intSheet;
	    }

	    public void setSheet(int intSheet) {
	        this.strSheet = null;
	        this.intSheet = intSheet;
	    }

	    public String getStrSheet() {        
	        return strSheet;
	    }

	    public void setSheet(String strSheet) {
	        this.intSheet = 0;
	        this.strSheet = strSheet;
	    }

		private void setExcelTitleList(List excelTitleList) {
			this.excelTitleList = excelTitleList;
		}
		
		public void setStartTitleRow(int startTitleRow) {
			this.startTitleRow = startTitleRow;
		}
	        
}
