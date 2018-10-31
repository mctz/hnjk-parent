package com.hnjk.extend.plugin.excel.file;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hnjk.core.support.context.SystemContextHolder;

import jxl.Cell;
import jxl.CellType;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.NullValueInNestedPathException;

import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.support.context.Constants;
import com.hnjk.extend.plugin.excel.config.ConfigParam;
import com.hnjk.extend.plugin.excel.config.ExcelConfigPropertyParam;
import com.hnjk.extend.plugin.excel.config.MergeCellsParam;

/**
 * model转成excel对象. <p>
 *
 * @author： 广东学苑教育发展有限公司.
 * @since： 2009-4-29上午11:55:51
 * @see
 * @version 1.0
 */
public class ModelToExcel {
		protected Logger logger = LoggerFactory.getLogger(getClass());

	    private WritableCellFormat headerFormat;//excel写入列标题格式

	    private WritableCellFormat titleFormat;//excel标题格式

	    private WritableCellFormat normolFormat;//excel数据默认格式

	    private String header;//标题

	    private int intHeight = 350;//初始高度

	    private String secondHeader;//二级标题

	    private WritableCellFormat secondHeaderFormat;//二级标题的样式

	    private int intSecondHeaderHeight = 300;//二级标题高

	    // 默认按配制文件输出，传入的dynamicTitleMap（property,exceltitlename）
	    // 只是按javabean对应的属性改变输出标题
	    // 如果 isDynamicTitle = true ，按传入的dynamicTitleMap 给出的列输出，只是列显示排序按配制文件设置。
	    // isDynamicTitle = true 要验证，dynamicTitleMap size
	    // 大于0,且其中设置的属性在javabean中存在的列必须大于0
	    private boolean isDynamicTitle = false;

	    private Map dynamicTitleMap = new HashMap();//动态列标题参数

	    /*
	     * 文件导出时，支持模板导出
	     * isTemplate = true ,按传入的模板文件格式输出，　
	     * templateFile　模板文件，
	     * startRow　开始输出的行数，
	     * paramMap　模板中的参数。map.put("Name","尹景民")如模板中有一个单元格为　　制表人　#Name# ,输出时为：制表人　尹景民
	     */
	    private boolean isTemplate = false;

	    private String templateFile = null;//模板文件名

	    private int startRow = 0;//起始行

	    private Map paramMap = new HashMap();//参数

	    private int startColumn = 0;//起始列

	    private boolean isInsertRow = true;//是否允许插入行

	    //支持多Sheet输出
	    private String sheetName="Sheet1";

	    private int sheetNum=0;//输出的sheet数量

	    private ConfigParam param;//配置参数

	   // private boolean isCellAutoHeight = false;//是否需要自动计算单元换行高度
	   private static final String EXCEL_MODEL_PATH = SystemContextHolder.getAppRootPath() + "WEB-INF" + File.separator + "templates"+File.separator+"excel"+File.separator;

	    public ModelToExcel() {}

	    /**
	     * 构造方法
	     * @param param 配置参数
	     */
	    public ModelToExcel(ConfigParam param){
	    	this.param = param;
	    }

	    public ConfigParam getParam() {
			return param;
		}

		public void setParam(ConfigParam param) {
			this.param = param;
		}

		/**
	     * 初始化头部样式
	     *
	     */
	    private void initFormat() {
	        if (this.headerFormat == null) {
	            this.headerFormat = this.getDefaultHeaderFormat();
	        }
	        if (this.titleFormat == null) {
	            this.titleFormat = this.getDefaultTitleFormat();
	        }
	        if (this.normolFormat == null) {
	            this.normolFormat = this.getDefaultNormolFormat();
	        }
			if (this.secondHeaderFormat == null) {
				this.secondHeaderFormat = this.getDefaultSecondHeaderFormat();
			}
	    }


		/**
	     * 获取模板文件
	     * @return
	     */
	    public File getExcelfile() {
	        initFormat();
	        if (isDynamicTitle) {
	            return getExcelfileByDynamicTitle(); // 动态传入的表头及列
	        } else if(isTemplate){
	            return getExcelfileByTemplate();//模板方式
	        }else{
	            return getExcelfileByConfig(); // 配制文件的列，可改变表头
	        }
	    }

	    /**
	     * 按模板文件方式导出Excel文件
	     * @return 替换模板文件后的文件
	     */
	    public File getExcelfileByTemplate(){
	        try {
	        	//取出目标文件
	            if (param.getExcelFile()!=null&&!param.getExcelFile().exists()) {
	            	param.getExcelFile().createNewFile();
	            }
	            //复制模板内容到导出文件中
	            Workbook book;
	            WritableWorkbook wbook;
	            if(param.getExcelFile()!=null){
	                book = Workbook.getWorkbook(new File(this.templateFile));
	                wbook = Workbook.createWorkbook(param.getExcelFile() , book);
	            }else{
	                book = Workbook.getWorkbook(new File(this.templateFile));
	                wbook = Workbook.createWorkbook(param.getExcelOutputStream(),book);
	            }
	            WritableSheet wsheet = null;
	            if(wbook.getSheets().length>this.sheetNum){
	                wsheet = wbook.getSheet(this.sheetNum);
	            }else{
	                //logger.error("模板文件中不存在 Sheet(" + this.sheetNum + ")");
	                //return null;
					wbook.createSheet(this.sheetName,this.sheetNum);
	            }

	            int rows = wsheet.getRows();
	            int columns = wsheet.getColumns();

	            //替换模板中 # #　之间的内容。
	            for(int i=0;i<rows;i++){
	                for(int j=0;j<columns;j++){
	                    WritableCell wc = wsheet.getWritableCell(j, i);
	                    String strCell = wc.getContents().toString();
	                    logger.debug("get strCell :{}" ,strCell );
	                    if(StringUtils.isNotBlank(strCell)&&strCell.indexOf("#")>=0){
	                        strCell = this.getParamValue(strCell);
	                        if(wc.getType() == CellType.LABEL){
	                            Label l = (Label)wc;
	                            l.setString(strCell);
	                        }
	                    }
	                }
	            }
	            //行，列重新取值　行取modelList.size()　+ startRow
	            columns = param.getExcelConfig().getColumnMap().size();
	            rows = param.getModelList().size() + startRow;

	            String[] propertyName = new String[columns];
	            String[] propertyDataType = new String[columns];
	            String[] codeTableName = new String[columns];
	            for (int j = 0; j < columns; j++) {
	                ExcelConfigPropertyParam propertyBean = (ExcelConfigPropertyParam) param.getExcelConfig().getColumnMap().get(String.valueOf(j + 1));
	                // 每列 Model中对应的属性
	                propertyName[j] = propertyBean.getName();
	                propertyDataType[j] = propertyBean.getDataType();
	                codeTableName[j] = propertyBean.getCodeTableName();
	            }


	          //处理数据到单元格
	            doFillinDateToCell(wsheet,startRow,rows,columns, propertyName,codeTableName,propertyDataType,true);

	            book.close();
	            wbook.write();
	            wbook.close();
	        } catch (IOException e) {
	            logger.error(e.getMessage(),e.fillInStackTrace());
	        } catch (RowsExceededException e) {
	            logger.error(e.getMessage(), e.fillInStackTrace());
	        } catch (WriteException e) {
	        	logger.error(e.getMessage(), e.fillInStackTrace());
	        } catch (BiffException e) {
	        	logger.error(e.getMessage(), e.fillInStackTrace());
	        }
	        return param.getExcelFile();
	    }

	    /**
	     * 动态生成标题方式导出excel文件
	     * @return
	     */
	    public File getExcelfileByDynamicTitle() {
	        try {
	            /*
	             * 在支持多页的过程中，对于每个工作薄不能每次都要从新建立　Workbook.createWorkbook(this.excelFile);
	             * 要从传进入的文件的基础上进行修改
	             * Workbook book = Workbook.getWorkbook(this.excelFile);
	             * wbook = Workbook.createWorkbook(this.excelFile,book);
	             * 但是对于刚刚建立的文件，this.excelFile.createNewFile();
	             * 在执行Workbook.getWorkbook(this.excelFile); 时报错。
	             * 所以设立 isNewFileFlag = false;
	             *
	             */
	            initFormat();
	            boolean isNewFileFlag = false;
	            if (param.getExcelFile() != null && !param.getExcelFile().exists()) {
	            	param.getExcelFile().createNewFile();
	                isNewFileFlag = true;
	            }
	            if (this.dynamicTitleMap == null) {
	               logger.error("Error:未设置动态列！");
	                return null;
	            }
	            WritableWorkbook wbook;
	            if(param.getExcelFile()!=null){
	                if(isNewFileFlag){
	                    wbook = Workbook.createWorkbook(param.getExcelFile());
	                }else{
	                    Workbook book = Workbook.getWorkbook(param.getExcelFile());
	                    wbook = Workbook.createWorkbook(param.getExcelFile() , book);
	                }
	            }else{
	                wbook = Workbook.createWorkbook(param.getExcelOutputStream());
	            }

	            if(wbook.getSheets().length > this.sheetNum){
	                wbook.removeSheet(this.sheetNum);
	            }

	            WritableSheet wsheet = wbook.createSheet(this.sheetName, this.sheetNum);

	           logger.debug("get sheet name:{},sheet num:{}",this.sheetName , this.sheetNum);

	            int columns = this.dynamicTitleMap.size();
	            // 表头2行
				int rowStart = 2;
	            int rows = param.getModelList().size();
	            logger.debug("get columns : " , columns);

	            // 设置Excel表头
				Label cellHeader = null;
				if (this.getHeader().contains(Constants.LINE_BREAK)) {
					cellHeader = new Label(0, 0, this.getHeader().split(Constants.LINE_BREAK)[0], this.getHeaderFormat());
					wsheet.mergeCells(0, 0, columns - 1, 0);
					wsheet.addCell(cellHeader);
					cellHeader = new Label(0, 1, this.getHeader().split(Constants.LINE_BREAK)[1], this.getHeaderFormat());
					wsheet.mergeCells(0, 1, columns - 1, 1);
				} else {
					cellHeader = new Label(0, 0, this.getHeader(), this.getSecondHeaderFormat());
					wsheet.mergeCells(0, 0, columns - 1, 1);
				}
	            wsheet.addCell(cellHeader);
	            if (intHeight > 0) {
	                wsheet.setRowView(0, intHeight);
	                wsheet.setRowView(1, intHeight);
	            }
				//设置二级标题
				if(ExStringUtils.isNotEmpty(this.getSecondHeader())){
					//合并单元格
					Label cellSecondHeader = null;
					if (this.getSecondHeader().contains(Constants.LINE_BREAK)) {
						cellSecondHeader = new Label(0, 2, this.getSecondHeader().split(Constants.LINE_BREAK)[0], this.getSecondHeaderFormat());
						wsheet.mergeCells(0, 2, columns - 1, 2);
						wsheet.addCell(cellSecondHeader);
						cellSecondHeader = new Label(0, 3, this.getSecondHeader().split(Constants.LINE_BREAK)[1], this.getSecondHeaderFormat());
						wsheet.mergeCells(0, 3, columns - 1, 3);
					} else {
						cellSecondHeader = new Label(0, 2, this.getSecondHeader(), this.getSecondHeaderFormat());
						wsheet.mergeCells(0, 2, columns - 1, 3);
					}

					wsheet.addCell(cellSecondHeader);
					wsheet.setRowView(rowStart++, intSecondHeaderHeight);
					wsheet.setRowView(rowStart++, intSecondHeaderHeight);
				}

	            // String[] propertyName = new String[columns];
	            String[] propertyDataType = new String[columns];
	            String[] codeTableName = new String[columns];
	           /* for (int j = 0; j < columns; j++) {
	                ExcelConfigPropertyParam propertyBean = (ExcelConfigPropertyParam) param.getExcelConfig().getColumnMap().get(String.valueOf(j + 1));
	                // 每列 Model中对应的属性
	                propertyDataType[j] = propertyBean.getDataType();
	                codeTableName[j] = propertyBean.getCodeTableName();
	            }*/
	            Map<String, ExcelConfigPropertyParam> columnMap = param.getExcelConfig().getColumnMap();
	            int index = 0;
	            for (String key : columnMap.keySet()) {
	            	 ExcelConfigPropertyParam propertyBean =columnMap.get(key);
		                // 每列 Model中对应的属性
	                 propertyDataType[index] = propertyBean.getDataType();
	                 codeTableName[index] = propertyBean.getCodeTableName();
	                 index ++;
				}

	            String[][] title = this.getDynamicTitle();
	            String[] propertyName = title[0];
	            String[] columnWidth = title[1];
	            if (StringUtils.isBlank(propertyName[propertyName.length - 1])) {
	                logger.error("ERROR : 动态表头属性值未在配制文件中配制完全！");
	            }

	            // 设置Excel标题
	            for (int j = 0; j < propertyName.length; j++) {
	                String excelTitleName = (String) this.dynamicTitleMap.get(propertyName[j]);
	                Label cellTitle = new Label(j, rowStart, excelTitleName, this.getTitleFormat());
	                wsheet.addCell(cellTitle);
	                if (StringUtils.isNotBlank(columnWidth[j])) {
	                    wsheet.setColumnView(j, Integer.parseInt(columnWidth[j]));
	                }
	            }
	            if (intHeight > 0) {
	                wsheet.setRowView(rowStart++, intHeight);
	            }
	            //处理数据到单元格
	            doFillinDateToCell(wsheet,rowStart,rows+rowStart,columns, propertyName,codeTableName,propertyDataType,false);

	            wbook.write();
	            wbook.close();
	        } catch (IOException e) {
	            logger.error(e.getMessage(), e.fillInStackTrace());
	        } catch (RowsExceededException e) {
	        	 logger.error(e.getMessage(), e.fillInStackTrace());
	        } catch (WriteException e) {
	        	 logger.error(e.getMessage(), e.fillInStackTrace());
	        } catch (BiffException e) {
	        	 logger.error(e.getMessage(), e.fillInStackTrace());
	        }
	        return param.getExcelFile();
	    }
	    /**
	     * 动态生成标题方式导出excel文件 标题较特殊
	     * @return
	     */
	    public File getExcelfileByDynamicTitle_SpecialTitleFormat(Map<String,Object> template) {
	    	initFormat();
	    	try {
	            /*
	             * 在支持多页的过程中，对于每个工作薄不能每次都要从新建立　Workbook.createWorkbook(this.excelFile);
	             * 要从传进入的文件的基础上进行修改
	             * Workbook book = Workbook.getWorkbook(this.excelFile);
	             * wbook = Workbook.createWorkbook(this.excelFile,book);
	             * 但是对于刚刚建立的文件，this.excelFile.createNewFile();
	             * 在执行Workbook.getWorkbook(this.excelFile); 时报错。
	             * 所以设立 isNewFileFlag = false;
	             *
	             */
	            boolean isNewFileFlag = false;
	            if (param.getExcelFile() != null && !param.getExcelFile().exists()) {
	            	param.getExcelFile().createNewFile();
	                isNewFileFlag = true;
	            }
	            if (this.dynamicTitleMap == null) {
	               logger.error("Error:未设置动态列！");
	                return null;
	            }
	            WritableWorkbook wbook;
	            if(param.getExcelFile()!=null){
	                if(isNewFileFlag){
	                    wbook = Workbook.createWorkbook(param.getExcelFile());
	                }else{
	                    Workbook book = Workbook.getWorkbook(param.getExcelFile());
	                    wbook = Workbook.createWorkbook(param.getExcelFile() , book);
	                }
	            }else{
	                wbook = Workbook.createWorkbook(param.getExcelOutputStream());
	            }

	            if(wbook.getSheets().length > this.sheetNum){
	                wbook.removeSheet(this.sheetNum);
	            }

	            WritableSheet wsheet = wbook.createSheet(this.sheetName, this.sheetNum);

	           logger.debug("get sheet name:{},sheet num:{}",this.sheetName , this.sheetNum);

	            int columns = this.dynamicTitleMap.size();
	            // 前面一堆东西共6行
	            int rows = param.getModelList().size() + 6;
	            logger.debug("get columns : " , columns);

	            // 设置Excel表头
	            wsheet.mergeCells(0, 0, columns - 1, 0);
	            Label cellHeader = new Label(0, 0, this.getHeader(), this.getHeaderFormat());
	            wsheet.addCell(cellHeader);

	            wsheet.mergeCells(0, 1, columns - 1, 1);
	            WritableFont fontSubHeader = new WritableFont(WritableFont.ARIAL, 12);
	            WritableCellFormat subHeaderformat = new WritableCellFormat(fontSubHeader);
	            subHeaderformat.setAlignment(Alignment.LEFT);

	            Label cellSubHeader = new Label(0, 1, "专业:"+template.get("majorname")+"    层次:"+template.get("classicname"), subHeaderformat);

	            wsheet.addCell(cellSubHeader);
	            if (intHeight > 0) {
	                wsheet.setRowView(0, intHeight);
	                wsheet.setRowView(1, intHeight);
	            }

	            String[] propertyDataType = new String[columns];
	            String[] codeTableName = new String[columns];

	            Map<String, ExcelConfigPropertyParam> columnMap = param.getExcelConfig().getColumnMap();
	            int index = 0;
	            for (String key : columnMap.keySet()) {
	            	 ExcelConfigPropertyParam propertyBean =columnMap.get(key);
		                // 每列 Model中对应的属性
	                 propertyDataType[index] = propertyBean.getDataType();
	                 codeTableName[index] = propertyBean.getCodeTableName();
	                 index ++;
				}

	            String[][] title = this.getDynamicTitle();
	            String[] propertyName = title[0];
	            String[] columnWidth = title[1];
	            for (int i = 0 ;i<propertyName.length;i++) {
	            	if("1_学习中心".equals(propertyName[i])){
						columnWidth[i]="15";
					}else if("1_姓名".equals(propertyName[i])){
						columnWidth[i]="7";
					}else if("1_学号".equals(propertyName[i])){
						columnWidth[i]="16";
					}else if("1_联系方式".equals(propertyName[i])){
						columnWidth[i]="12";
					}else if("1_学籍状态".equals(propertyName[i])){
						columnWidth[i]="5";
					}else if("1_是否已填写学籍卡".equals(propertyName[i])){
						columnWidth[i]="4";
					}else{
						columnWidth[i]="3";
					}
				}
	            if (StringUtils.isBlank(propertyName[propertyName.length - 1])) {
	                logger.error("ERROR : 动态表头属性值未在配制文件中配制完全！");
	            }
	            int beginColumnNormalExam  =240;
	            int beginColumnSpecialExam =241;
	            int beginColumnNoExam      =242;
	            int beginColumnStatic      =243;
	            int endColumnNormalExam    =240;
	            int endinColumnSpecialExam =241;
	            int endinColumnNoExam      =242;
	            int endinColumnStatic      =243;
	            // 设置Excel标题
	            WritableFont font = new WritableFont(WritableFont.ARIAL, 10);
	            WritableCellFormat titleformat = new WritableCellFormat(font);
                titleformat.setWrap(true);
                titleformat.setAlignment(Alignment.CENTRE);
                titleformat.setVerticalAlignment(VerticalAlignment.CENTRE);
		        titleformat.setBorder(Border.ALL, BorderLineStyle.THIN);
	            for (int j = 0; j < propertyName.length; j++) {
	                String excelTitleName = (String) this.dynamicTitleMap.get(propertyName[j]);
	                String tag = propertyName[j].split("_")[0];
	                int contentBeginRow = 4;
	                if("1".equals(tag)){
	                	contentBeginRow = 3;
	                	wsheet.mergeCells(j,3,j,5);
	                }else if("2".equals(tag)){
	                	if(j<=beginColumnNormalExam){
	                		beginColumnNormalExam = j;
	                	}
	                	if(j>=beginColumnNormalExam){
	                		endColumnNormalExam = j;
	                	}
	                }else if("3".equals(tag)){
	                	if(j<=beginColumnSpecialExam){
	                		beginColumnSpecialExam = j;
	                	}
	                	if(j>beginColumnSpecialExam){
	                		endinColumnSpecialExam = j;
	                	}
	                	wsheet.mergeCells(j,4,j,5);
	                }else if("4".equals(tag)){
	                	if(j<=beginColumnNoExam){
	                		beginColumnNoExam = j;
	                	}
	                	if(j>beginColumnNoExam){
	                		endinColumnNoExam = j;
	                	}
	                	wsheet.mergeCells(j,4,j,5);
	                }else if("5".equals(tag)){
	                	if(j<=beginColumnStatic){
	                		beginColumnStatic = j;
	                	}
	                	if(j>beginColumnStatic){
	                		endinColumnStatic = j;
	                	}
	                	wsheet.mergeCells(j,4,j,5);
	                }
	                //课程的成绩用的是 LinkedHashMap  一般的成绩和特殊的成绩不混在一起
	                Label cellTitle = new Label(j, contentBeginRow, excelTitleName, titleformat);
	                wsheet.addCell(cellTitle);
	                if("2".equals(tag)){
	                	cellTitle = new Label(j, contentBeginRow+1, propertyName[j].split("_")[2], titleformat);
		                wsheet.addCell(cellTitle);
	                }
	                if (StringUtils.isNotBlank(columnWidth[j])) {
	                    wsheet.setColumnView(j, Integer.parseInt(columnWidth[j]));
	                }
	            }
	            wsheet.mergeCells(beginColumnNormalExam,3,endColumnNormalExam,3);
	            wsheet.mergeCells(beginColumnSpecialExam,3,endinColumnSpecialExam,3);
	            wsheet.mergeCells(beginColumnNoExam,3,endinColumnNoExam,3);
	            wsheet.mergeCells(beginColumnStatic,3,endinColumnStatic,3);
                Label cellTitle_t = new Label(beginColumnNormalExam, 3, "课程成绩", titleformat);
                wsheet.addCell(cellTitle_t);
                cellTitle_t = new Label(beginColumnSpecialExam, 3, "统考科目", titleformat);
                wsheet.addCell(cellTitle_t);
                cellTitle_t = new Label(beginColumnNoExam, 3, "免考成绩", titleformat);
                wsheet.addCell(cellTitle_t);
                cellTitle_t = new Label(beginColumnStatic, 3, "修读学分状态", titleformat);
                wsheet.addCell(cellTitle_t);
	            if (intHeight > 0) {
	                wsheet.setRowView(2, intHeight);
	            }

	            //处理数据到单元格
	            doFillinDateToCell_WithSpecialFormat(wsheet,6,rows,columns, propertyName,codeTableName,propertyDataType,false);

	            wbook.write();
	            wbook.close();
	        } catch (IOException e) {
	            logger.error(e.getMessage(), e.fillInStackTrace());
	        } catch (RowsExceededException e) {
	        	 logger.error(e.getMessage(), e.fillInStackTrace());
	        } catch (WriteException e) {
	        	 logger.error(e.getMessage(), e.fillInStackTrace());
	        } catch (BiffException e) {
	        	 logger.error(e.getMessage(), e.fillInStackTrace());
	        }
	        return param.getExcelFile();
	    }

	    /**
	     * 动态生成标题方式导出excel文件 标题更特殊了
	     * @return
	     */
	    public File getExcelfileByDynamicTitle_SpecialTitleFormatWithDegree(Map<String,Object> template) {
	    	initFormat();
	    	try {
	            /*
	             * 在支持多页的过程中，对于每个工作薄不能每次都要从新建立　Workbook.createWorkbook(this.excelFile);
	             * 要从传进入的文件的基础上进行修改
	             * Workbook book = Workbook.getWorkbook(this.excelFile);
	             * wbook = Workbook.createWorkbook(this.excelFile,book);
	             * 但是对于刚刚建立的文件，this.excelFile.createNewFile();
	             * 在执行Workbook.getWorkbook(this.excelFile); 时报错。
	             * 所以设立 isNewFileFlag = false;
	             *
	             */
	            boolean isNewFileFlag = false;
	            if (param.getExcelFile() != null && !param.getExcelFile().exists()) {
	            	param.getExcelFile().createNewFile();
	                isNewFileFlag = true;
	            }
	            if (this.dynamicTitleMap == null) {
	               logger.error("Error:未设置动态列！");
	                return null;
	            }
	            WritableWorkbook wbook;
	            if(param.getExcelFile()!=null){
	                if(isNewFileFlag){
	                    wbook = Workbook.createWorkbook(param.getExcelFile());
	                }else{
	                    Workbook book = Workbook.getWorkbook(param.getExcelFile());
	                    wbook = Workbook.createWorkbook(param.getExcelFile() , book);
	                }
	            }else{
	                wbook = Workbook.createWorkbook(param.getExcelOutputStream());
	            }

	            if(wbook.getSheets().length > this.sheetNum){
	                wbook.removeSheet(this.sheetNum);
	            }

	            WritableSheet wsheet = wbook.createSheet(this.sheetName, this.sheetNum);

	           logger.debug("get sheet name:{},sheet num:{}",this.sheetName , this.sheetNum);

	            int columns = this.dynamicTitleMap.size();
	            // 前面一堆东西共7行
	            int rows = param.getModelList().size() + 7;
	            logger.debug("get columns : " , columns);

	            // 设置Excel表头
	            // 此处的合并不可通过 column的值的大小而定。如果是主干课程，那么就会有两列
	            String[] titlestrs = this.getDynamicTitle()[0];
	            Integer actualColumnsNum = columns - 1;
	            for (String titlestr : titlestrs) {
					if("3".equals(titlestr.split("_")[0])){
						actualColumnsNum++;
					}
				}
	            wsheet.mergeCells(0, 0, actualColumnsNum, 0);//按主干课的多少而定的实际列数
	            Label cellHeader = new Label(0, 0, this.getHeader(), this.getHeaderFormat());
	            wsheet.addCell(cellHeader);

	            wsheet.mergeCells(0, 1, actualColumnsNum, 1);//按主干课的多少而定的实际列数
	            wsheet.mergeCells(0, 2, actualColumnsNum+6, 2);//按主干课的多少而定的实际列数 6只是怕不够空间
	            WritableFont fontSubHeader = new WritableFont(WritableFont.ARIAL, 10);
	            WritableCellFormat subHeaderformat = new WritableCellFormat(fontSubHeader);
	            subHeaderformat.setAlignment(Alignment.LEFT);
	            String mbcourse = "";
	            String mcourse1 = "";
	            String mcourse2 = "";
	            for (String str : this.getDynamicTitle()[0]) {
	            	String excelTitleName_tmp = (String) this.dynamicTitleMap.get(str);
	            	if(excelTitleName_tmp.contains("学位专业基础课")){
	            		mbcourse= excelTitleName_tmp.replace("(学位专业基础课)", "");
	            	}else if(excelTitleName_tmp.contains("学位主干课1")){
	            		mcourse1= excelTitleName_tmp.replace("(学位主干课1)", "");
	            	}else if(excelTitleName_tmp.contains("学位主干课2")){
	            		mcourse2= excelTitleName_tmp.replace("(学位主干课2)", "");
	            	}
	            }


	            Label cellSubHeader = new Label(0, 2, "学习中心:"+template.get("unitname")+"    专业:"+template.get("majorname")+"    层次:"+template.get("classicname")+"    年级:"+template.get("gradename")+"    最低毕业学分:"+template.get("lowTotal")+"    最低必修学分:"+template.get("lowNesTotal")+(ExStringUtils.isNotEmpty(mbcourse)?("    学位专业基础课:"+mbcourse):"")+(ExStringUtils.isNotEmpty(mcourse1)?("    学位主干课1:"+mcourse1):"")+(ExStringUtils.isNotEmpty(mcourse2)?("    学位主干课2:"+mcourse2):"")    , subHeaderformat);
	            wsheet.addCell(cellSubHeader);
	            if (intHeight > 0) {
	                wsheet.setRowView(0, intHeight);
	                wsheet.setRowView(1, intHeight);
	            }

	            String[] propertyDataType = new String[columns];
	            String[] codeTableName = new String[columns];

	            Map<String, ExcelConfigPropertyParam> columnMap = param.getExcelConfig().getColumnMap();
	            int index = 0;
	            for (String key : columnMap.keySet()) {
	            	 ExcelConfigPropertyParam propertyBean =columnMap.get(key);
		                // 每列 Model中对应的属性
	                 propertyDataType[index] = propertyBean.getDataType();
	                 codeTableName[index] = propertyBean.getCodeTableName();
	                 index ++;
				}

	            String[][] title = this.getDynamicTitle();
	            String[] propertyName = title[0];
	            String[] columnWidth = title[1];
	            for (int i = 0 ;i<propertyName.length;i++) {
	            	if("1_学习中心".equals(propertyName[i])){
						columnWidth[i]="15";
					}else if("1_姓名".equals(propertyName[i])){
						columnWidth[i]="7";
					}else if("1_学号".equals(propertyName[i])){
						columnWidth[i]="16";
					}else if("1_联系方式".equals(propertyName[i])){
						columnWidth[i]="12";
					}else if("1_学籍状态".equals(propertyName[i])){
						columnWidth[i]="5";
					}else if("1_是否已填写学籍卡".equals(propertyName[i])){
						columnWidth[i]="4";
					}else if("1_学位外语成绩合格证号".equals(propertyName[i])){
						columnWidth[i]="12";
					}else if("1_本学期预约考试课程总学分".equals(propertyName[i])){
						columnWidth[i]="4";
					}else{
						columnWidth[i]="3";
					}
				}
	            if (StringUtils.isBlank(propertyName[propertyName.length - 1])) {
	                logger.error("ERROR : 动态表头属性值未在配制文件中配制完全！");
	            }
	            /*
	            WritableFont font_nor = new WritableFont(WritableFont.ARIAL, 10);

	            WritableCellFormat mctitleformat = new WritableCellFormat(font_nor);
	            font_nor.setBoldStyle(WritableFont.BOLD);
	            mctitleformat.setAlignment(Alignment.RIGHT);
	            mctitleformat.setVerticalAlignment(VerticalAlignment.BOTTOM);
	            WritableFont font_bigred = new WritableFont(WritableFont.TIMES, 10);
	            font_bigred.setColour(Colour.RED);
	            font_bigred.setBoldStyle(WritableFont.BOLD);
	            WritableCellFormat mctitleformatcon = new WritableCellFormat(font_bigred);
	            mctitleformatcon.setAlignment(Alignment.LEFT);
	            mctitleformatcon.setVerticalAlignment(VerticalAlignment.BOTTOM);

	            Integer endmainbase = 0;
	            Integer endmaincourse1 = 0;
	            Label mcLabel = new Label(0, 2, "学位专业基础课", mctitleformat);
	            for (String str : propertyName) {
	            	String excelTitleName_tmp = (String) this.dynamicTitleMap.get(str);
	            	if(excelTitleName_tmp.contains("学位专业基础课")){
	            		wsheet.mergeCells(0,2,2,2);//学位专业基础课
	    	            wsheet.addCell(mcLabel);

	            		excelTitleName_tmp = excelTitleName_tmp.replace("(学位专业基础课)", "");
	            		Double d = Math.ceil(excelTitleName_tmp.length()/1.5);
	            		String len = String.valueOf(d).split("\\.")[0];
	            		endmainbase = 3+Integer.parseInt(len)-1;
	            		wsheet.mergeCells(3, 2,endmainbase  , 2);
	            		mcLabel = new Label(3, 2, excelTitleName_tmp, mctitleformatcon);
	     	            wsheet.addCell(mcLabel);
	            	}
	            }

	            for (String str : propertyName) {
	            	String excelTitleName_tmp = (String) this.dynamicTitleMap.get(str);
	            	if(excelTitleName_tmp.contains("学位主干课1")){
	            		wsheet.mergeCells(endmainbase+1, 2,endmainbase+4  , 2);//学位主干课1
	     	            mcLabel = new Label(endmainbase+1, 2, "学位主干课1", mctitleformat);
	     	            wsheet.addCell(mcLabel);

	            		excelTitleName_tmp = excelTitleName_tmp.replace("(学位主干课1)", "");
	            		Double d = Math.ceil(excelTitleName_tmp.length()/1.5);
	            		String len = String.valueOf(d).split("\\.")[0];
	            		endmaincourse1 = endmainbase+5+Integer.parseInt(len)-1;
	            		wsheet.mergeCells(endmainbase+5, 2, endmaincourse1 , 2);
	            		mcLabel = new Label(endmainbase+5, 2, excelTitleName_tmp, mctitleformatcon);
	     	            wsheet.addCell(mcLabel);
	            	}
	            }

	            for (String str : propertyName) {
	            	String excelTitleName_tmp = (String) this.dynamicTitleMap.get(str);
	            	if(excelTitleName_tmp.contains("学位主干课2")){
	            		wsheet.mergeCells(endmaincourse1+1, 2,endmaincourse1+4  , 2);//学位主干课2
	     	            mcLabel = new Label(endmaincourse1+1, 2, "学位主干课2", mctitleformat);
	     	            wsheet.addCell(mcLabel);

	            		excelTitleName_tmp = excelTitleName_tmp.replace("(学位主干课2)", "");
	            		Double d = Math.ceil(excelTitleName_tmp.length()/1.5);
	            		String len = String.valueOf(d).split("\\.")[0];
	            		wsheet.mergeCells(endmaincourse1+5, 2,endmaincourse1+5+Integer.parseInt(len)-1  , 2);
	            		mcLabel = new Label(endmaincourse1+5, 2, excelTitleName_tmp, mctitleformatcon);
	     	            wsheet.addCell(mcLabel);
	            	}
	            }
	            */
	            // 设置Excel标题
	            WritableFont font = new WritableFont(WritableFont.ARIAL, 10);
	            WritableCellFormat titleformat = new WritableCellFormat(font);
                titleformat.setWrap(true);
                titleformat.setAlignment(Alignment.CENTRE);
                titleformat.setVerticalAlignment(VerticalAlignment.CENTRE);
		        titleformat.setBorder(Border.ALL, BorderLineStyle.THIN);
	            int actualColumn = 0;
		        for (int j = 0; j < propertyName.length; j++) {
	                String excelTitleName = (String) this.dynamicTitleMap.get(propertyName[j]);
	                String tag = propertyName[j].split("_")[0];
	                int contentBeginRow = 3;
	                if("1".equals(tag)){
	                	wsheet.mergeCells(actualColumn,contentBeginRow,actualColumn,contentBeginRow+3);
	                }else if("2".equals(tag)){

	                }else if("3".equals(tag)){
	                	wsheet.mergeCells(actualColumn,contentBeginRow,actualColumn+1,contentBeginRow);
	                	wsheet.mergeCells(actualColumn,contentBeginRow+1,actualColumn+1,contentBeginRow+1);
	                	wsheet.mergeCells(actualColumn,contentBeginRow+2,actualColumn+1,contentBeginRow+2);
	                }
	                //课程的成绩用的是 LinkedHashMap  一般的成绩和特殊的成绩不混在一起
	                WritableFont font_blue = new WritableFont(WritableFont.ARIAL, 10);
	                font_blue.setColour(Colour.BLUE);
	                font_blue.setBoldStyle(WritableFont.BOLD);
	            	WritableCellFormat titleformat_blue = new WritableCellFormat(font_blue);
	            	titleformat_blue.setWrap(true);
	            	titleformat_blue.setAlignment(Alignment.CENTRE);
	            	titleformat_blue.setVerticalAlignment(VerticalAlignment.CENTRE);
	            	titleformat_blue.setBorder(Border.ALL, BorderLineStyle.THIN);
	            	WritableFont font_red = new WritableFont(WritableFont.ARIAL, 10);
	            	font_red.setColour(Colour.RED);
	            	font_red.setBoldStyle(WritableFont.BOLD);
	            	WritableCellFormat titleformat_red = new WritableCellFormat(font_red);
	            	titleformat_red.setWrap(true);
	            	titleformat_red.setAlignment(Alignment.CENTRE);
	            	titleformat_red.setVerticalAlignment(VerticalAlignment.CENTRE);
	            	titleformat_red.setBorder(Border.ALL, BorderLineStyle.THIN);
	 	            if("3".equals(tag)){
	 	            	Label cellTitle = new Label(actualColumn, contentBeginRow, excelTitleName, titleformat_blue);
	 	            	wsheet.addCell(cellTitle);
	 	            }else{
	 	            	Label cellTitle = new Label(actualColumn, contentBeginRow, excelTitleName, titleformat);
		                wsheet.addCell(cellTitle);
	 	            }

		            if("2".equals(tag)){
		            	Label cellTitle = new Label(actualColumn, contentBeginRow+1, propertyName[j].split("_")[2], titleformat);
			            wsheet.addCell(cellTitle);
			            cellTitle = new Label(actualColumn, contentBeginRow+2, propertyName[j].split("_")[3], titleformat);
			            wsheet.addCell(cellTitle);
		                cellTitle = new Label(actualColumn, contentBeginRow+3,"综合成绩", titleformat);
			            wsheet.addCell(cellTitle);
		            }else if("3".equals(tag)){
		            	Label cellTitle = new Label(actualColumn, contentBeginRow+1, propertyName[j].split("_")[2], titleformat);
			            wsheet.addCell(cellTitle);
			            cellTitle = new Label(actualColumn, contentBeginRow+2, propertyName[j].split("_")[3], titleformat);
			            wsheet.addCell(cellTitle);
		                cellTitle = new Label(actualColumn, contentBeginRow+3,"卷面成绩", titleformat);
			            wsheet.addCell(cellTitle);
			            cellTitle = new Label(actualColumn+1, contentBeginRow+3,"综合成绩", titleformat);
			            wsheet.addCell(cellTitle);
		            }

	                if (StringUtils.isNotBlank(columnWidth[j])) {
	                    wsheet.setColumnView(actualColumn, Integer.parseInt(columnWidth[j]));
	                    if("3".equals(tag)){
	                    	 wsheet.setColumnView(actualColumn+1, Integer.parseInt(columnWidth[j]));
		                }
	                }
	                if("3".equals(tag)){
	                	actualColumn +=2;
	                }else{
	                	actualColumn++;
	                }

	            }

	            if (intHeight > 0) {
	                wsheet.setRowView(2, intHeight);
	            }
	            try{
	            //处理数据到单元格
	            doFillinDateToCell_WithSpecialFormatWithDegree(wsheet,7,rows,columns, propertyName,codeTableName,propertyDataType,false);
	            }catch(Exception e){
	            	logger.error(e.getMessage());
	            }
	            wbook.write();
	            wbook.close();
	        } catch (IOException e) {
	            logger.error(e.getMessage(), e.fillInStackTrace());
	        } catch (RowsExceededException e) {
	        	 logger.error(e.getMessage(), e.fillInStackTrace());
	        } catch (WriteException e) {
	        	 logger.error(e.getMessage(), e.fillInStackTrace());
	        } catch (BiffException e) {
	        	 logger.error(e.getMessage(), e.fillInStackTrace());
	        }
	        return param.getExcelFile();
	    }
	    /**
	     * 配置文件列方式导出Excel文件，可以动态改变表头
	     * @return
	     */
	    public File getExcelfileByConfig() {
	        try {

	            /*
	             * 在支持多页的过程中，对于每个工作薄不能每次都要从新建立　Workbook.createWorkbook(this.excelFile);
	             * 要从传进入的文件的基础上进行修改
	             * Workbook book = Workbook.getWorkbook(this.excelFile);
	             * wbook = Workbook.createWorkbook(this.excelFile,book);
	             * 但是对于刚刚建立的文件，this.excelFile.createNewFile();
	             * 在执行Workbook.getWorkbook(this.excelFile); 时报错。
	             * 所以设立 isNewFileFlag = false;
	             *
	             */

	            boolean isNewFileFlag = false;
	            if (param.getExcelFile() !=null &&  !param.getExcelFile().exists()) {
	            	param.getExcelFile().createNewFile();
	                isNewFileFlag = true;
	            }

	            WritableWorkbook wbook;
	            if(param.getExcelFile() !=null ){
	                if(isNewFileFlag){
	                    wbook = Workbook.createWorkbook(param.getExcelFile());
	                }else{
	                    Workbook book = Workbook.getWorkbook(param.getExcelFile());
	                    wbook = Workbook.createWorkbook(param.getExcelFile() , book);
	                }

	            }else{
	                wbook = Workbook.createWorkbook(param.getExcelOutputStream());
	            }

	            if(wbook.getSheets().length > this.sheetNum){
	                wbook.removeSheet(this.sheetNum);
	            }
	            WritableSheet wsheet = wbook.createSheet(this.sheetName, this.sheetNum);
	            logger.debug("get sheet name:{},sheet num:{}",this.sheetName , this.sheetNum);

	            int columns = param.getExcelConfig().getColumnMap().size();
	            // 加上表头2行，标题1行
	            int rows = param.getModelList().size();
	            logger.debug("columns :{} " , columns);

	            // 设置Excel表头
	            wsheet.mergeCells(0, 0, columns - 1, 1);
	            Label cellHeader = new Label(0, 0, this.getHeader(), this.getHeaderFormat());
	            wsheet.addCell(cellHeader);

	            if (intHeight > 0) {
	                wsheet.setRowView(0, intHeight);
	                wsheet.setRowView(1, intHeight);
	            }
	            int rowStart = 2;
	            //设置二级标题
	            String secondHeader = ExStringUtils.trimToEmpty(this.getSecondHeader());
	            if(ExStringUtils.isNotEmpty(secondHeader)){
	            	//合并单元格
		            wsheet.mergeCells(0, 2, columns - 1, 3);
		            Label cellSecondHeader = new Label(0, 2, secondHeader, this.getSecondHeaderFormat());
		            wsheet.addCell(cellSecondHeader);
		            wsheet.setRowView(2, intSecondHeaderHeight);
		            wsheet.setRowView(3, intSecondHeaderHeight);
		            rowStart = rowStart+2;
	            }

	            String[] propertyName = new String[columns];
	            String[] propertyDataType = new String[columns];
	            String[] codeTableName = new String[columns];

	            // 设置Excel标题
	            for (int j = 0; j < columns; j++) {
	                ExcelConfigPropertyParam propertyBean = (ExcelConfigPropertyParam) param.getExcelConfig().getColumnMap().get(String.valueOf(j + 1));
	                String excelTitleName = propertyBean.getExcelTitleName();
	                String name = propertyBean.getName().trim();

	                //每列 Model中对应的属性
	                propertyName[j] = propertyBean.getName();
	                propertyDataType[j] = propertyBean.getDataType();
	                codeTableName[j] = propertyBean.getCodeTableName();

	                // 如果动态表头含有记录属性值，取动态传入的表头的值，但是传入的值不能为空，如果为空，取配制文件的值
	                if (this.dynamicTitleMap.containsKey(name)) {
	                    String strTitle = (String) this.dynamicTitleMap.get(name);
	                    if (StringUtils.isNotBlank(strTitle)) {
	                        excelTitleName = strTitle;
	                    }
	                }
	                Label cellTitle = new Label(j, rowStart, excelTitleName, this.getTitleFormat());
	              	wsheet.addCell(cellTitle);

	                // 设置每列的宽度
	                if (StringUtils.isNotBlank(propertyBean.getColumnWidth())) {
	                    wsheet.setColumnView(j, Integer.parseInt(propertyBean.getColumnWidth()));
	                }
	            }

              	rowStart ++ ;

	            //填充数据到单元格
	            doFillinDateToCell(wsheet,rowStart,rows + rowStart,columns, propertyName,codeTableName,propertyDataType,false);
	            //区分 只有学籍状态统计学籍才使用以下合并单元格的功能
	            if("按学籍状态进行统计——学籍信息统计导出表".equals(this.getHeader())){
		            List<Integer> columnToMerge = new ArrayList<Integer>(0);
		            columnToMerge.add(0);
		            mergeCellByColumnNum(wsheet,rowStart,columnToMerge);
	            }
	            wbook.write();
	            wbook.close();

	        } catch (IOException e) {
	           logger.error(e.getMessage(),e.fillInStackTrace());
	        } catch (RowsExceededException e) {
	        	logger.error(e.getMessage(),e.fillInStackTrace());
	        } catch (WriteException e) {
	        	logger.error(e.getMessage(),e.fillInStackTrace());
	        } catch (BiffException e) {
	        	logger.error(e.getMessage(),e.fillInStackTrace());
	        }
	        return param.getExcelFile();
	    }

	    /*
	     * 填充单元格数据
	     */
	    private void doFillinDateToCell(WritableSheet wsheet,
	    								int startRow,int endRow,
	    								int columns,String[] propertyName,
	    								String[] codeTableName,
	    								String[] propertyDataType,
	    								boolean isTemplate) throws RowsExceededException, WriteException{
	    	boolean isMap = false;
            if (param.getModelList().size()>0) {
                Object tmpObj = param.getModelList().get(0);
                if(tmpObj instanceof Map){
                    isMap = true;
                }
            }
	    	for (int i = startRow; i < endRow; i++) {//循环行
	    		if(isTemplate){
	    			  //每一次操作，都要插入一行
	                if(this.isInsertRow){
	                    wsheet.insertRow(i);
	                }
	    		}
                Object modelObj = param.getModelList().get(i - startRow);
                BeanWrapper bw = null;
                if(isMap!=true){
                     bw= new BeanWrapperImpl(modelObj);
                }
                for (int j = 0; j < columns; j++) {//循环列
                    String strCell = "";
                    Object obj;
                    try {
                    	if(isMap ==true){
                            obj =((Map) modelObj).get(propertyName[j]);
                        }else{
                        	obj= bw.getPropertyValue(propertyName[j]);
                        	
                        }
					} catch (NullValueInNestedPathException e) {
						//如果bw报错，直接跳过
						logger.debug(propertyName[j]+" 的bean 为空，跳过");
						continue;
					}
                    
                    if (obj != null) {
                        strCell = obj.toString().trim();//默认转成String
                        //如果为日期类型，则转换对应的pattern格式
						if (obj instanceof Date && propertyDataType[j].indexOf("Date") >= 0) {
							String[] strTemp = propertyDataType[j].split("\\ ");
							String pattern = "";
							if (strTemp.length > 1) {
								pattern = strTemp[1];
							}
							//校验
							if (ExStringUtils.isNotBlank(pattern)) {
								try {
									strCell = ExDateUtils.formatDateStr((Date) obj, pattern);
								} catch (ParseException e) {
									logger.error("转换日期出错:{}", e);
								}
							}
						}
                    }

                    logger.debug("get strCell:{}",strCell);

                  //转换键值对
                    if(null != codeTableName[j] && codeTableName[j].length()>1 && null != param.getValueMap()){
                    	strCell = (String)this.param.getValueMap().get(codeTableName[j]+"_"+strCell);
                    }

                    Label cellNormol = new Label(j, i, strCell, this.getNormolFormat());


                    if(NumberUtils.isNumber(strCell)&&(propertyDataType[j].indexOf("Integer")>=0)){
                        Number ncellNormol;
                        if(strCell.indexOf(".")>=0){
                            ncellNormol = new Number(this.startColumn + j, i, Double.parseDouble(strCell), this.getNormolFormat());
                        }else{
                            ncellNormol = new Number(this.startColumn + j, i, Long.parseLong(strCell), this.getNormolFormat());
                        }
                        wsheet.addCell(ncellNormol);
                    }else{
						wsheet.addCell(cellNormol);
					}
                }
                if (intHeight > 0) {
                    wsheet.setRowView(i, intHeight);
                }
            }
	    	//2011-9-27 新增合并单元格
            if(null != param.getMergeCellsParams() && !param.getMergeCellsParams().isEmpty()){
            	for (MergeCellsParam mergeCellsParam : param.getMergeCellsParams()) {

            		wsheet.mergeCells(mergeCellsParam.getStartColumnNum(), mergeCellsParam.getStartRowNum(),
            				mergeCellsParam.getEndColumnNum(), mergeCellsParam.getEndRowNum());
				}
            }
	    }
	    /*
	     * 填充单元格数据——带特殊格式
	     */
	    private void doFillinDateToCell_WithSpecialFormat(WritableSheet wsheet,
	    								int startRow,int endRow,
	    								int columns,String[] propertyName,
	    								String[] codeTableName,
	    								String[] propertyDataType,
	    								boolean isTemplate) throws RowsExceededException, WriteException{
	    	//字体
	    	WritableFont font = new WritableFont(WritableFont.ARIAL, 10);
	    	WritableFont font_red = new WritableFont(WritableFont.ARIAL, 10);
	    	font_red.setColour(Colour.RED);
	        WritableCellFormat format = new WritableCellFormat(font);
	        format.setWrap(true);
            format.setAlignment(Alignment.CENTRE);
            format.setVerticalAlignment(VerticalAlignment.CENTRE);
		    format.setBorder(Border.ALL, BorderLineStyle.THIN);
		    WritableCellFormat format_red = new WritableCellFormat(font_red);
		    format_red.setWrap(true);
		    format_red.setAlignment(Alignment.CENTRE);
		    format_red.setVerticalAlignment(VerticalAlignment.CENTRE);
            format_red.setBorder(Border.ALL, BorderLineStyle.THIN);

	    	boolean isMap = false;
            if (param.getModelList().size()>0) {
                Object tmpObj = param.getModelList().get(0);
                if(tmpObj instanceof Map){
                    isMap = true;
                }
            }
	    	for (int i = startRow; i < endRow; i++) {//循环行
	    		if(isTemplate){
	    			  //每一次操作，都要插入一行
	                if(this.isInsertRow){
	                    wsheet.insertRow(i);
	                }
	    		}
                Object modelObj = param.getModelList().get(i - startRow);
                BeanWrapper bw = null;
                if(isMap!=true){
                     bw= new BeanWrapperImpl(modelObj);
                }
                for (int j = 0; j < columns; j++) {//循环列
                    String strCell = "";
                    Object obj;
                    if(isMap ==true){
                        obj =((Map) modelObj).get(propertyName[j]);
                    }else{
                         obj= bw.getPropertyValue(propertyName[j]);
                    }
                    if (obj != null) {
                        strCell = obj.toString().trim();//默认转成String
                        //如果为日期类型，则转换对应的pattern格式
                        if(obj instanceof Date && propertyDataType[j].indexOf("Date")>=0){
                        	 String[] strTemp = propertyDataType[j].split("\\ ");
                        	 String pattern = "";
                        	 if(strTemp.length > 1){
                        		 pattern = strTemp[1];
                        	 }
                        	 //校验
                        	if(ExStringUtils.isNotBlank(pattern)){
    							try {
    								strCell = ExDateUtils.formatDateStr((Date)obj, pattern);
    							} catch (ParseException e) {
    								logger.error("转换日期出错:{}",e);
    							}
                        	}
                        }
                    }

                    logger.debug("get strCell:{}",strCell);

                  //转换键值对
                    if(null != codeTableName[j] && codeTableName[j].length()>1 && null != param.getValueMap()){
                    	strCell = (String)this.param.getValueMap().get(codeTableName[j]+"_"+strCell);
                    }
                    Label cellNormol = new Label(j, i, ExStringUtils.isNotEmpty(strCell)?strCell:"-", j==6&&"否".equals(strCell)||j>=7&&j<columns-2&&NumberUtils.isDigits(strCell)&&Double.valueOf(strCell)<60?format_red:format);

                    wsheet.addCell(cellNormol);

                }
                if (intHeight > 0) {
                    wsheet.setRowView(i, intHeight);
                }
            }
	    }

	    /*
	     * 填充单元格数据——带更特殊的格式
	     */
	    public void doFillinDateToCell_WithSpecialFormatWithDegree(WritableSheet wsheet,
	    								int startRow,int endRow,
	    								int columns,String[] propertyName,
	    								String[] codeTableName,
	    								String[] propertyDataType,
	    								boolean isTemplate) throws RowsExceededException, WriteException{
	    	//字体
	    	WritableFont font = new WritableFont(WritableFont.ARIAL, 10);
	    	WritableFont font_red = new WritableFont(WritableFont.ARIAL, 10);
	    	font_red.setColour(Colour.RED);
	        WritableCellFormat format = new WritableCellFormat(font);
	        format.setWrap(true);
            format.setAlignment(Alignment.CENTRE);
            format.setVerticalAlignment(VerticalAlignment.CENTRE);
		    format.setBorder(Border.ALL, BorderLineStyle.THIN);
		    WritableCellFormat formatb = new WritableCellFormat(font);
	        formatb.setWrap(true);
            formatb.setAlignment(Alignment.CENTRE);
            formatb.setVerticalAlignment(VerticalAlignment.CENTRE);
		    formatb.setBorder(Border.ALL, BorderLineStyle.THIN);
		    formatb.setBackground(Colour.LIGHT_BLUE);
		    WritableCellFormat format_red = new WritableCellFormat(font_red);
		    format_red.setWrap(true);
		    format_red.setAlignment(Alignment.CENTRE);
		    format_red.setVerticalAlignment(VerticalAlignment.CENTRE);
            format_red.setBorder(Border.ALL, BorderLineStyle.THIN);
            WritableCellFormat format_redb = new WritableCellFormat(font_red);
		    format_redb.setWrap(true);
		    format_redb.setAlignment(Alignment.CENTRE);
		    format_redb.setVerticalAlignment(VerticalAlignment.CENTRE);
            format_redb.setBorder(Border.ALL, BorderLineStyle.THIN);
            format_redb.setBackground(Colour.LIGHT_BLUE);
	    	boolean isMap = false;
            if (param.getModelList().size()>0) {
                Object tmpObj = param.getModelList().get(0);
                if(tmpObj instanceof Map){
                    isMap = true;
                }
            }
	    	for (int i = startRow; i < endRow; i++) {//循环行
	    		if(isTemplate){
	    			  //每一次操作，都要插入一行
	                if(this.isInsertRow){
	                    wsheet.insertRow(i);
	                }
	    		}
                Object modelObj = param.getModelList().get(i - startRow);
                BeanWrapper bw = null;
                if(isMap!=true){
                     bw= new BeanWrapperImpl(modelObj);
                }
                for (int j = 0; j < columns; j++) {//循环列
                    String strCell = "";
                    Object obj;
                    if(isMap ==true){
                        obj =((Map) modelObj).get(propertyName[j]);
                    }else{
                         obj= bw.getPropertyValue(propertyName[j]);
                    }
                    if (obj != null) {
                        strCell = obj.toString().trim();//默认转成String
                        //如果为日期类型，则转换对应的pattern格式
                        if(obj instanceof Date && propertyDataType[j].indexOf("Date")>=0){
                        	 String[] strTemp = propertyDataType[j].split("\\ ");
                        	 String pattern = "";
                        	 if(strTemp.length > 1){
                        		 pattern = strTemp[1];
                        	 }
                        	 //校验
                        	if(ExStringUtils.isNotBlank(pattern)){
    							try {
    								strCell = ExDateUtils.formatDateStr((Date)obj, pattern);
    							} catch (ParseException e) {
    								logger.error("转换日期出错:{}",e);
    							}
                        	}
                        }
                    }

                    logger.debug("get strCell:{}",strCell);

                    //如果是有四个# 说明是有成绩记录的主干课成绩 需要区分其卷面成绩和综合成绩   [0]综合成绩 [1]卷面成绩
                    if(strCell.contains("####")){
                    	int begincolumn = j;
                    	for (int index =0 ;index<j;index++) {
							if("3".equals(propertyName[index].split("_")[0])){
								begincolumn++;
							}
						}
                    	WritableCellFormat f1 = format;
                    	WritableCellFormat f2 = format_red;
                    	//如果包含有****blue说明是本学期预约的成绩
                    	if(strCell.contains("****blue")){
                    		strCell= strCell .replace("****blue","");
                    		f1 = formatb;
                        	f2 = format_redb;
                    	}
                    	Label cellNormol = new Label(begincolumn, i, ExStringUtils.isNotEmpty(strCell.split("####")[1])?("null".equals(strCell.split("####")[1])||"0".equals(strCell.split("####")[1])?"-":strCell.split("####")[1]):"-", "否".equals(strCell.split("####")[1])||j>=6&&j<columns-4&&NumberUtils.isDigits(strCell.split("####")[1])&&!"0".equals(strCell.split("####")[1])&&Double.valueOf(strCell.split("####")[1])<60?format_red:format);
                    	wsheet.addCell(cellNormol);
                    	cellNormol = new Label(begincolumn+1, i, ExStringUtils.isNotEmpty(strCell.split("####")[0])?strCell.split("####")[0]:"-", "否".equals(strCell.split("####")[0])||j>=6&&j<columns-4&&NumberUtils.isDigits(strCell.split("####")[0])&&Double.valueOf(strCell.split("####")[0])<60?f2:f1);
                    	wsheet.addCell(cellNormol);
                    }else{//说明没有成绩记录的主干课成绩或一般的课程成绩
                    	int begincolumn = j;
                    	for (int index =0 ;index<j;index++) {
							if("3".equals(propertyName[index].split("_")[0])){
								begincolumn++;
							}
						}
                    	WritableCellFormat f1 = format;
                    	WritableCellFormat f2 = format_red;
                    	//如果包含有****blue说明是本学期预约的成绩
                    	if(strCell.contains("****blue")){
                    		strCell= strCell .replace("****blue","");
                    		f1 = formatb;
                        	f2 = format_redb;
                    	}
                    	Label cellNormol = new Label(begincolumn, i, ExStringUtils.isNotEmpty(strCell)?strCell:"-", "否".equals(strCell)||j>=6&&j<columns-4&&NumberUtils.isDigits(strCell)&&Double.valueOf(strCell)<60?f2:f1);
                    	wsheet.addCell(cellNormol);
                    	//有些学生行没有课程之成绩记录，所以就不能通过成绩值有没有四个#来确定是要填一个格还是两个格。
                    	if("3".equals(propertyName[j].split("_")[0])){
                    		cellNormol = new Label(begincolumn+1, i, ExStringUtils.isNotEmpty(strCell)?strCell:"-", "否".equals(strCell)||j>=6&&j<columns-4&&NumberUtils.isDigits(strCell)&&Double.valueOf(strCell)<60?f2:f1);
                        	wsheet.addCell(cellNormol);
						}
                    }


                }
                if (intHeight > 0) {
                    wsheet.setRowView(i, intHeight);
                }
            }

	    	//如果projectname[] 中 含1_学位十门主干课程平均分 说明是本科的成绩
	    	boolean isUnderGraduate = false;
	    	for (String pn : propertyName) {
				if("1_学位十门主干课程平均分".equals(pn)){
					isUnderGraduate = true;
				}
			}
	    	for (int i = 0; i < (isUnderGraduate?6:5); i++) {
	    		wsheet.mergeCells(0,endRow+1+i , 24, endRow+1+i);
			}
	    	WritableFont fontend = new WritableFont(WritableFont.ARIAL, 10);
	    	fontend.setBoldStyle(WritableFont.BOLD);
	        WritableCellFormat formatend = new WritableCellFormat(fontend);
	        formatend.setBackground(Colour.YELLOW);
	    	Label cellEnd = new Label(0, endRow+2, "注：1.打印版需A3格式;",formatend);
	    	wsheet.addCell(cellEnd);
	    		  cellEnd = new Label(0, endRow+3, "       2.学生各门课程成绩以学生历次考试最高分计，不及格成绩以红色字体标记；",formatend);
	    		  wsheet.addCell(cellEnd);
	    		  if(isUnderGraduate){
	    			  cellEnd = new Label(0, endRow+4, "       3.学位十门主干课课程名称用蓝色标记。",formatend);
		    		  wsheet.addCell(cellEnd);
	    		  }
	    		  cellEnd = new Label(0, endRow+(isUnderGraduate?1:0)+4, (isUnderGraduate?"       4.本学期学生预约考试的科目已在该课程“综合成绩”处填充蓝色。":"       3.本学期学生预约考试的科目已在该课程“综合成绩”处填充蓝色。"),formatend);
	    		  wsheet.addCell(cellEnd);
	    		  cellEnd = new Label(0, endRow+1, "",formatend);
	    		  wsheet.addCell(cellEnd);
	    		  cellEnd = new Label(0, endRow+(isUnderGraduate?1:0)+5, "",formatend);
	    		  wsheet.addCell(cellEnd);
	    }
	    /**
	     * 获取动态标题
	     * @return
	     */
	    public String[][] getDynamicTitle() {
	        int intColumn = param.getExcelConfig().getColumnMap().size();
	        String[][] propertyName = new String[2][this.dynamicTitleMap.size()];

	        int j = -1;
	       /* for (int i = 0; i < intColumn; i++) {
	            ExcelConfigPropertyParam propertyBean = (ExcelConfigPropertyParam) param.getExcelConfig().getColumnMap().get(String.valueOf(i + 1));
	            String name = propertyBean.getName().trim();
	            String columnWidth = propertyBean.getColumnWidth();
	            if (this.dynamicTitleMap.containsKey(name)) {
	                j++;
	                propertyName[0][j] = name;
	                propertyName[1][j] = columnWidth;

	            }
	        }*/
	        Map<String, ExcelConfigPropertyParam> columnMap = param.getExcelConfig().getColumnMap();
	        for (String key : columnMap.keySet()) {
	            ExcelConfigPropertyParam propertyBean = columnMap.get(key);
	            String name = propertyBean.getName().trim();
	            String columnWidth = propertyBean.getColumnWidth();
	            if (this.dynamicTitleMap.containsKey(name)) {
	                j++;
	                propertyName[0][j] = name;
	                propertyName[1][j] = columnWidth;

	            }
	        }
	        // 这里可以判断j的值，来判断动态传入的map属性设置是否正确，
	        if (j+1 != this.dynamicTitleMap.size()) {
	           logger.info("动态表头属性值未在配制文件中配制！");
	        }
	        return propertyName;
	    }

	    public void setHeaderCellFormat(WritableCellFormat headerFormat) {
	        this.headerFormat = headerFormat;
	    }

	    public void setNormolCellFormat(WritableCellFormat normolFormat) {
	        this.normolFormat = normolFormat;
	    }

	    public void setRowHeight(int intHeight) {
	        this.intHeight = intHeight;
	    }

	    public void setTitleCellFormat(WritableCellFormat titleFormat) {
	        this.titleFormat = titleFormat;
	    }

	    public void setHeader(String header) {
	        this.header = header;
	    }

	    public String getHeader() {
	        return header;
	    }

	    public WritableCellFormat getHeaderFormat() {
	        return headerFormat;
	    }

	    public int getIntHeight() {
	        return intHeight;
	    }


	    public String getSecondHeader() {
			return secondHeader;
		}

		public void setSecondHeader(String secondHeader) {
			this.secondHeader = secondHeader;
		}

		public WritableCellFormat getSecondHeaderFormat() {
			return secondHeaderFormat;
		}

		public void setSecondHeaderFormat(WritableCellFormat secondHeaderFormat) {
			this.secondHeaderFormat = secondHeaderFormat;
		}

		public int getIntSecondHeaderHeight() {
			return intSecondHeaderHeight;
		}

		public void setIntSecondHeaderHeight(int intSecondHeaderHeight) {
			this.intSecondHeaderHeight = intSecondHeaderHeight;
		}

		public WritableCellFormat getNormolFormat() {
	        return normolFormat;
	    }

	    public WritableCellFormat getTitleFormat() {
	        return titleFormat;
	    }

	    /**
	     * 获取默认头部样式
	     * @return
	     */
	    public WritableCellFormat getDefaultHeaderFormat() {
	        WritableFont font = new WritableFont(WritableFont.TIMES, 18, WritableFont.BOLD);// 定义字体
	        try {
	            font.setColour(Colour.BLACK);// 蓝色字体
	        } catch (WriteException e1) {
	            logger.error(e1.getMessage(),e1.fillInStackTrace());
	        }
	        WritableCellFormat format = new WritableCellFormat(font);
	        try {
	            format.setAlignment(jxl.format.Alignment.CENTRE);// 左右居中
	            format.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 上下居中
	            format.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);// 黑色边框
	            // format.setBackground(Colour.YELLOW);// 黄色背景
	            format.setBackground(Colour.GRAY_50);

	        } catch (WriteException e) {
	        	logger.error(e.getMessage(),e.fillInStackTrace());
	        }
	        return format;
	    }

	    /**
	     * 获取默认标题样式
	     * @return
	     */
	    public WritableCellFormat getDefaultTitleFormat() {
	        // 字体：TIMES,大小为12号,粗体
	        WritableFont font = new WritableFont(WritableFont.TIMES, 12, WritableFont.BOLD);
	        try {
	            font.setColour(Colour.BLACK);// 蓝色字体
	        } catch (WriteException e1) {
	           logger.error(e1.getMessage(),e1.fillInStackTrace());
	        }
	        WritableCellFormat format = new WritableCellFormat(font);

	        try {
	            format.setAlignment(jxl.format.Alignment.CENTRE);
	            format.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
	            format.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);
	            format.setBackground(Colour.YELLOW);
	        } catch (WriteException e) {
	           logger.error(e.getMessage(),e.fillInStackTrace());
	        }
	        return format;
	    }

	public WritableCellFormat getDefaultSecondHeaderFormat() {
		WritableFont font = new WritableFont(WritableFont.TIMES, 16, WritableFont.BOLD);// 定义字体
		try {
			font.setColour(Colour.BLACK);// 蓝色字体
		} catch (WriteException e1) {
			logger.error(e1.getMessage(),e1.fillInStackTrace());
		}
		WritableCellFormat format = new WritableCellFormat(font);
		try {
			format.setAlignment(jxl.format.Alignment.CENTRE);// 左右居中
			format.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 上下居中
			format.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);// 黑色边框
			format.setBackground(Colour.GRAY_25);

		} catch (WriteException e) {
			logger.error(e.getMessage(),e.fillInStackTrace());
		}
		return format;
	}

	    /**
	     * 获取默认内容样式
	     * @return
	     */
	    public WritableCellFormat getDefaultNormolFormat() {
	        WritableFont font = new WritableFont(WritableFont.TIMES, 10);
	        WritableCellFormat format = new WritableCellFormat(font);
	        try {
	            format.setAlignment(jxl.format.Alignment.CENTRE);
	            format.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
	            format.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);
	        } catch (WriteException e) {
	            logger.error(e.getMessage(),e.fillInStackTrace());
	        }
	        return format;
	    }


	    /**
	     * 得到参数值
	     * @param strTemp
	     * @return
	     */
	    private String getParamValue(String strTemp){
	        String str = strTemp;
	        Object[] keyarr = this.paramMap.keySet().toArray();
	        for(int i=0;i<keyarr.length;i++){
	            String strkey = "#" + keyarr[i].toString() + "#";
	            str = StringUtils.replace(str, strkey, paramMap.get(keyarr[i]).toString());
	        }
	        return str;
	    }

	    public Map getDynamicTitleMap() {
	        return dynamicTitleMap;
	    }

	    public void setDynamicTitleMap(Map dynamicTitleMap) {
	        this.dynamicTitleMap = dynamicTitleMap;
	    }

	    public boolean isDynamicTitle() {
	        return isDynamicTitle;
	    }

	    public void setDynamicTitle(boolean isDynamicTitle) {
	        this.isDynamicTitle = isDynamicTitle;
	    }

	/**
	 * 设置文件参数
	 * @param templateFile 模版文件路径，默认相对路径为"WEB-INF/templates/excel/"，只传入文件全名即可
	 * @param startRow 数据起始行，0行为标题行
	 * @param paramMap 其它参数值，如当前日期，学校名称等
	 */
	public void setTemplateParam(String templateFile, int startRow, Map paramMap){
		if(!templateFile.startsWith(SystemContextHolder.getAppRootPath())){
			templateFile = EXCEL_MODEL_PATH + templateFile;
		}
		this.setTemplateParam(true, templateFile, startRow, paramMap);
	}
	public void setTemplateParam(boolean isTemplate,String templateFile,int startRow,Map paramMap){
		this.setTemplateParam(isTemplate, templateFile, startRow, 0, paramMap, true);
	}

	//edit YJM　2008-03-29
	public void setTemplateParam(String templateFile,int startRow,int startColumn,Map paramMap,boolean isInsertRow){
		this.setTemplateParam(true, templateFile, startRow, startColumn, paramMap, isInsertRow);
	}

	/**
	 * 设置模板参数
	 * @param isTemplate
	 * @param templateFile
	 * @param startRow
	 * @param startColumn
	 * @param paramMap
	 * @param isInsertRow
	 */
	public void setTemplateParam(boolean isTemplate,String templateFile,int startRow,int startColumn,Map paramMap,boolean isInsertRow){
		this.isTemplate = isTemplate;
		this.templateFile = templateFile;
		this.startRow = startRow;
		this.paramMap = paramMap;
		this.startColumn = startColumn;
		this.isInsertRow = isInsertRow;
	}

	public Map getParamMap() {
		return paramMap;
	}

	public void setParamMap(Map paramMap) {
		this.paramMap = paramMap;
	}

	public int getStartRow() {
		return startRow;
	}

	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}

	public String getTemplateFile() {
		return templateFile;
	}

	public void setTemplateFile(String templateFile) {
		this.templateFile = templateFile;
	}

	public boolean isTemplate() {
		return isTemplate;
	}

	public void setTemplate(boolean isTemplate) {
		this.isTemplate = isTemplate;
	}

	public void setSheet(int sheetNum, String sheetName) {
		this.sheetName=sheetName;
		this.sheetNum=sheetNum;
	}




	public static void main(String[] args) {
//	        List modelList = new ArrayList();
//	        for (int i = 0; i < 65; i++) {
//	            DeptModel dept = new DeptModel();
//	            dept.setDeptName("总部");
//	            dept.setDeptCode("A10" + i);
//	            dept.setDeptNo(i);
//	            dept.setSendFileName("交南发");
//	            modelList.add(dept);
//	        }
//	        // Colour[] color = Colour.getAllColours();
//	        // for(int i=0;i<color.length;i++){
//	        // System.out.println(color[i].getDescription() + " = " +
//	        // color[i].getDefaultRGB().getRed() + " "
//	        // +color[i].getDefaultRGB().getGreen() +" " +
//	        // color[i].getDefaultRGB().getBlue());
//	        // }
//	        Map map = new HashMap();
//	        map.put("deptName", "部门名称1");
//	        map.put("deptCode", "部门编号1");
//	        map.put("deptNo", "排序1");
//
//	        ExcelConfigManager configManager = ExcelConfigFactory.createExcelConfigManger();
//	        File excelfile = new File("E:\\workspace\\modeltoexcel.xls");
//	        ModelToExcelImpl mte = new ModelToExcelImpl(excelfile, configManager.getModel("deptModel", ""), modelList);
//	        mte.setHeader("部门发文简称（2007）");
//	        mte.setRowHeight(500);
//	        mte.setDynamicTitle(true);
//	        mte.setDynamicTitleMap(map);
//	        mte.getExcelfile();

	}
	/**
	 * 按模板文件方式导出Excel文件——试室安排总表(合并单元格)
	 * @return 替换模板文件后的文件
	 */
	public File getExcelfileByTemplate2(){
		initFormat();
		try {
			//取出目标文件
			if (param.getExcelFile()!=null&&!param.getExcelFile().exists()) {
				param.getExcelFile().createNewFile();
			}
			//复制模板内容到导出文件中
			Workbook book;
			WritableWorkbook wbook;
			if(param.getExcelFile()!=null){
				book = Workbook.getWorkbook(new File(this.templateFile));
				wbook = Workbook.createWorkbook(param.getExcelFile() , book);
			}else{
				book = Workbook.getWorkbook(new File(this.templateFile));
				wbook = Workbook.createWorkbook(param.getExcelOutputStream(),book);
			}
			WritableSheet wsheet;
			if(wbook.getSheets().length>this.sheetNum){
				wsheet = wbook.getSheet(this.sheetNum);
			}else{
				//logger.error("模板文件中不存在 Sheet(" + this.sheetNum + ")");
				wsheet = wbook.createSheet(this.sheetName,this.sheetNum);
			}
			int rows = wsheet.getRows();
			int columns = wsheet.getColumns();
			//替换模板中 # #　之间的内容。
			for(int i=0;i<rows;i++){
				for(int j=0;j<columns;j++){
					WritableCell wc = wsheet.getWritableCell(j, i);
					String strCell = wc.getContents().toString();
					logger.debug("get strCell :{}" ,strCell );
					if(StringUtils.isNotBlank(strCell)&&strCell.indexOf("#")>=0){
						strCell = this.getParamValue(strCell);
						if(wc.getType() == CellType.LABEL){
							Label l = (Label)wc;
							l.setString(strCell);
						}
					}
				}
			}
			//行，列重新取值　行取modelList.size()　+ startRow
			columns = param.getExcelConfig().getColumnMap().size();
			rows = param.getModelList().size() + startRow;

			String[] propertyName = new String[columns];
			String[] propertyDataType = new String[columns];
			String[] codeTableName = new String[columns];
			for (int j = 0; j < columns; j++) {
				ExcelConfigPropertyParam propertyBean = (ExcelConfigPropertyParam) param.getExcelConfig().getColumnMap().get(String.valueOf(j + 1));
				// 每列 Model中对应的属性
				propertyName[j] = propertyBean.getName();
				propertyDataType[j] = propertyBean.getDataType();
				codeTableName[j] = propertyBean.getCodeTableName();
			}
			//处理数据到单元格
			doFillinDateToCell(wsheet,startRow,rows,columns, propertyName,codeTableName,propertyDataType,true);
			//合并单元格操作
			int row = wsheet.getRows();
			List<Map<Integer,Integer>> list = new ArrayList<Map<Integer,Integer>>(0);
			for(int j = 0 ; j < 4 ; j++){
				Integer start=3  ;
				Integer end;
				String pre="";
				String after="";
				for(int i = 3 ; i < row ; i++){
					Cell[] cells=wsheet.getRow(i);
					if(i==3){
						pre = cells[j].getContents();
						start = i;
						continue;
					}else{
						after = cells[j].getContents();
						if(after.equals(pre)){
							if(i==row-1){
								wsheet.mergeCells(j, start, j, i);
								continue;
							}
							continue;
						}else{
							end = i-1;
							wsheet.mergeCells(j, start, j, end);
							start=i;
							pre = cells[j].getContents();
						}
					}
				}
			}
			book.close();
			wbook.write();
			wbook.close();
		} catch (IOException e) {
			logger.error(e.getMessage(),e.fillInStackTrace());
		} catch (RowsExceededException e) {
			logger.error(e.getMessage(), e.fillInStackTrace());
		} catch (WriteException e) {
			logger.error(e.getMessage(), e.fillInStackTrace());
		} catch (BiffException e) {
			logger.error(e.getMessage(), e.fillInStackTrace());
		}
		return param.getExcelFile();
	}
	/**
	 * 按模板文件方式导出带多个Sheet的Excel文件 (datas与params的key必须保持一至)
	 * @param datas
	 * @param params
	 * @return
	 */
	public File getExcelfileByTemplateForSheets1(Map<String, List<Object>> datas,Map<String, Object> params){
		initFormat();
		try {
				//取出目标文件
				if (param.getExcelFile()!=null&&!param.getExcelFile().exists()) {
					param.getExcelFile().createNewFile();
				}
				//复制模板内容到导出文件中
				Workbook book;
				WritableWorkbook wbook;
				if(param.getExcelFile()!=null){
					book = Workbook.getWorkbook(new File(this.templateFile));
					wbook = Workbook.createWorkbook(param.getExcelFile() , book);
				}else{
					book = Workbook.getWorkbook(new File(this.templateFile));
					wbook = Workbook.createWorkbook(param.getExcelOutputStream(),book);
				}

				int curSheetNum = 0;

				for(String key: datas.keySet()){

					List newList  = datas.get(key);
					this.sheetNum = curSheetNum;
					this.paramMap = (Map) params.get(key);

					param.setModelList(newList);
					WritableSheet wsheet;
					wsheet = wbook.getSheet(this.sheetNum);
					//当不为最后一页时，就拷贝新页。
					if(curSheetNum!= (datas.values().size()-1)){
						wbook.copySheet(this.sheetNum,String.valueOf(this.sheetNum+1), this.sheetNum+1);
					}
					int rows = wsheet.getRows();
					int columns = wsheet.getColumns();

					for(int i=0;i<rows;i++){
						for(int j=0;j<columns;j++){
							WritableCell wc = wsheet.getWritableCell(j, i);
							String strCell = wc.getContents().toString();
							logger.debug("get strCell :{}" ,strCell );
							if(StringUtils.isNotBlank(strCell)&&strCell.indexOf("#")>=0){
								strCell = this.getParamValue(strCell);
								if(wc.getType() == CellType.LABEL){
									Label l = (Label)wc;
									l.setString(strCell);
								}
							}
						}
					}
					//行，列重新取值　行取modelList.size()　+ startRow
					columns = param.getExcelConfig().getColumnMap().size();
					rows    = param.getModelList().size() + startRow;

					String[] propertyName     = new String[columns];
					String[] propertyDataType = new String[columns];
					String[] codeTableName = new String[columns];
					for (int j = 0; j < columns; j++) {
						ExcelConfigPropertyParam propertyBean = (ExcelConfigPropertyParam) param.getExcelConfig().getColumnMap().get(String.valueOf(j + 1));
						// 每列 Model中对应的属性
						propertyName[j] = propertyBean.getName();
						propertyDataType[j] = propertyBean.getDataType();
						codeTableName[j] = propertyBean.getCodeTableName();
					}

				  //处理数据到单元格
					doFillinDateToCell(wsheet,startRow,rows,columns, propertyName,codeTableName,propertyDataType,true);
					curSheetNum ++;
				}
				book.close();
				wbook.write();
				wbook.close();
			} catch (IOException e) {
				logger.error(e.getMessage(),e.fillInStackTrace());
			} catch (RowsExceededException e) {
				logger.error(e.getMessage(), e.fillInStackTrace());
			} catch (WriteException e) {
				logger.error(e.getMessage(), e.fillInStackTrace());
			} catch (BiffException e) {
				logger.error(e.getMessage(), e.fillInStackTrace());
			}
			return param.getExcelFile();
	}
	/**
	 * 按模板文件方式导出带多个Sheet的Excel文件
	 * @return 替换模板文件后的文件
	 */
	public File getExcelfileByTemplateForSheets(List<String> list,String str){
		initFormat();
		try {
				//取出目标文件
				if (param.getExcelFile()!=null&&!param.getExcelFile().exists()) {
					param.getExcelFile().createNewFile();
				}
				//复制模板内容到导出文件中
				Workbook book;
				WritableWorkbook wbook;
				if(param.getExcelFile()!=null){
					book = Workbook.getWorkbook(new File(this.templateFile));
					wbook = Workbook.createWorkbook(param.getExcelFile() , book);
				}else{
					book = Workbook.getWorkbook(new File(this.templateFile));
					wbook = Workbook.createWorkbook(param.getExcelOutputStream(),book);
				}
				List modelList=param.getModelList();

				for(int num=0 ;num<list.size();num++){
					this.sheetNum=num;
					List newList = new ArrayList(0);
					String range = list.get(num);
					Integer begin = Integer.valueOf(range.split("#")[0]);
					Integer end   = Integer.valueOf(range.split("#")[1]);
					if ("entranceExam".equals(str)) {
						//加入层次名和专业名
						this.paramMap.put("classicName", range.split("#")[2]);
						this.paramMap.put("recruitMajorName", range.split("#")[3]);
					}
					if ("finalExam".equals(str)){
//原来写成E+课程编码，以此生成的考试编码有误 应为edu_teach_examinfo的examcoursecode		this.paramMap.put("examCode","E"+range.split("#")[2]);//考试批次
						this.paramMap.put("examCode",range.split("#")[2]);//考试批次
						this.paramMap.put("courseName",range.split("#")[3]);//课程名
						this.paramMap.put("examType",range.split("#")[4]);//考试类型
						this.paramMap.put("unitcodename", range.split("#")[5]+"——"+range.split("#")[6]);//考场编号——名称
						this.paramMap.put("examRoom", range.split("#")[7]);  //考试室
						this.paramMap.put("examDate",range.split("#")[8]);//考试日期
						this.paramMap.put("examTime",range.split("#")[9]);//考试时间
						this.paramMap.put("examMode", range.split("#")[10]);
						this.paramMap.put("num",(Integer.valueOf(range.split("#")[1])-Integer.valueOf(range.split("#")[0])+1));//人数统计
					}
					for (int j = begin ;j<=end;j++) {
						newList.add(modelList.get(j));
					}
					param.setModelList(newList);
					WritableSheet wsheet;
					wsheet = wbook.getSheet(this.sheetNum);
					//当不为最后一页时，就拷贝新页。
					if(num!=list.size()-1){
						wbook.copySheet(this.sheetNum,String.valueOf(this.sheetNum+1), this.sheetNum+1);
					}
					int rows = wsheet.getRows();
					int columns = wsheet.getColumns();

					//替换模板中 # #　之间的内容。
					for(int i=0;i<rows;i++){
						for(int j=0;j<columns;j++){
							WritableCell wc = wsheet.getWritableCell(j, i);
							String strCell = wc.getContents().toString();
							logger.debug("get strCell :{}" ,strCell );
							if(StringUtils.isNotBlank(strCell)&&strCell.indexOf("#")>=0){
								strCell = this.getParamValue(strCell);
								if(wc.getType() == CellType.LABEL){
									Label l = (Label)wc;
									l.setString(strCell);
								}
							}
						}
					}
					//行，列重新取值　行取modelList.size()　+ startRow
					columns = param.getExcelConfig().getColumnMap().size();
					rows = param.getModelList().size() + startRow;

					String[] propertyName = new String[columns];
					String[] propertyDataType = new String[columns];
					String[] codeTableName = new String[columns];
					for (int j = 0; j < columns; j++) {
						ExcelConfigPropertyParam propertyBean = (ExcelConfigPropertyParam) param.getExcelConfig().getColumnMap().get(String.valueOf(j + 1));
						// 每列 Model中对应的属性
						propertyName[j] = propertyBean.getName();
						propertyDataType[j] = propertyBean.getDataType();
						codeTableName[j] = propertyBean.getCodeTableName();
					}


				  //处理数据到单元格
					doFillinDateToCell(wsheet,startRow,rows,columns, propertyName,codeTableName,propertyDataType,true);
				}
				book.close();
				wbook.write();
				wbook.close();
			} catch (IOException e) {
				logger.error(e.getMessage(),e.fillInStackTrace());
			} catch (RowsExceededException e) {
				logger.error(e.getMessage(), e.fillInStackTrace());
			} catch (WriteException e) {
				logger.error(e.getMessage(), e.fillInStackTrace());
			} catch (BiffException e) {
				logger.error(e.getMessage(), e.fillInStackTrace());
			}
			return param.getExcelFile();
	}
	/**
	 * 通过列数合并该列内容相同的单元格
	 * @param wsheet
	 * @throws WriteException
	 * @throws RowsExceededException
	 */
	private	void mergeCellByColumnNum(WritableSheet wsheet,Integer start,List<Integer> columnsToMerge ) throws RowsExceededException, WriteException{
		int row = wsheet.getRows();
		int column = wsheet.getColumns();
		List<Map<Integer,Integer>> list = new ArrayList<Map<Integer,Integer>>(0);
		for(int j = 0 ; j < column ; j++){
			if(!columnsToMerge.contains(j)) {
				continue;
			}
			start = 3  ;
			Integer end;
			String pre="";
			String after="";
			for(int i = 3 ; i < row ; i++){
				Cell[] cells=wsheet.getRow(i);
				if(i==3){
					pre = cells[j].getContents();
					start = i;
					continue;
				}else{
					after = cells[j].getContents();
					if(after.equals(pre)){
						if(i==row-1){
							wsheet.mergeCells(j, start, j, i);
							continue;
						}
						continue;
					}else{
						end = i-1;
						wsheet.mergeCells(j, start, j, end);
						start=i;
						pre = cells[j].getContents();
					}
				}
			}
		}
	}


	/**
	 * 动态生成标题方式导出excel文件 标题更特殊了   根据班级打印成绩单
	 * @return
	 */
	public File getExcelfileByDynamicTitle_SpecialTitleFormatWithClasses(Map<String,Object> template) {
		initFormat();
		try {
			/*
			 * 在支持多页的过程中，对于每个工作薄不能每次都要从新建立　Workbook.createWorkbook(this.excelFile);
			 * 要从传进入的文件的基础上进行修改
			 * Workbook book = Workbook.getWorkbook(this.excelFile);
			 * wbook = Workbook.createWorkbook(this.excelFile,book);
			 * 但是对于刚刚建立的文件，this.excelFile.createNewFile();
			 * 在执行Workbook.getWorkbook(this.excelFile); 时报错。
			 * 所以设立 isNewFileFlag = false;
			 *
			 */
			boolean isNewFileFlag = false;
			if (param.getExcelFile() != null && !param.getExcelFile().exists()) {
				param.getExcelFile().createNewFile();
				isNewFileFlag = true;
			}
			if (this.dynamicTitleMap == null) {
			   logger.error("Error:未设置动态列！");
				return null;
			}
			WritableWorkbook wbook;
			if(param.getExcelFile()!=null){
				if(isNewFileFlag){
					wbook = Workbook.createWorkbook(param.getExcelFile());
				}else{
					Workbook book = Workbook.getWorkbook(param.getExcelFile());
					wbook = Workbook.createWorkbook(param.getExcelFile() , book);
				}
			}else{
				wbook = Workbook.createWorkbook(param.getExcelOutputStream());
			}

			if(wbook.getSheets().length > this.sheetNum){
				wbook.removeSheet(this.sheetNum);
			}

			WritableSheet wsheet = wbook.createSheet(this.getSheetName(), this.getSheetNum());

		   logger.debug("get sheet name:{},sheet num:{}",this.sheetName , this.sheetNum);

			int columns = this.dynamicTitleMap.size();
			// 前面一堆东西共7行
			int rows = param.getModelList().size() + 8;
			logger.debug("get columns : " , columns);

			// 设置Excel表头
			// 此处的合并不可通过 column的值的大小而定。如果是主干课程，那么就会有两列
			String[] titlestrs = this.getDynamicTitle()[0];
			Integer actualColumnsNum = columns - 1;
			for (String titlestr : titlestrs) {
				if("3".equals(titlestr.split("_")[0])){
					actualColumnsNum++;
				}
			}
			wsheet.mergeCells(0, 0, actualColumnsNum, 1);//按主干课的多少而定的实际列数
			Label cellHeader = new Label(0, 0, this.getHeader(), this.getHeaderFormat());
			wsheet.addCell(cellHeader);

			//按主干课的多少而定的实际列数 使actualColumnsNum接近10只是怕不够空间
			wsheet.mergeCells(0, 2, actualColumnsNum<10?actualColumnsNum+20/actualColumnsNum:actualColumnsNum, 2);
			WritableFont fontSubHeader = new WritableFont(WritableFont.ARIAL, 10);
			WritableCellFormat subHeaderformat = new WritableCellFormat(fontSubHeader);
			subHeaderformat.setAlignment(Alignment.LEFT);

			Label cellSubHeader = new Label(0, 2,	template.get("unitname")+"       "+template.get("xz")+"       "+template.get("majorname")+"       "+template.get("classicname"), subHeaderformat);
			wsheet.addCell(cellSubHeader);
			if (intHeight > 0) {
				wsheet.setRowView(0, intHeight);
				wsheet.setRowView(1, intHeight);
			}

			String[] propertyDataType = new String[columns];
			String[] codeTableName = new String[columns];

			Map<String, ExcelConfigPropertyParam> columnMap = param.getExcelConfig().getColumnMap();
			int index = 0;
			for (String key : columnMap.keySet()) {
				 ExcelConfigPropertyParam propertyBean =columnMap.get(key);
				 // 每列 Model中对应的属性
				 propertyDataType[index] = propertyBean.getDataType();
				 codeTableName[index] = propertyBean.getCodeTableName();
				 index ++;
			}

			String[][] title = this.getDynamicTitle();
			String[] propertyName = title[0];
			String[] columnWidth = title[1];
			for (int i = 0 ;i<propertyName.length;i++) {
				if("1_姓名".equals(propertyName[i])){
					columnWidth[i]="8";
				}else if("1_学号".equals(propertyName[i])){
					columnWidth[i]="18";
				}else{
					columnWidth[i]="10";
				}
			}
			if (StringUtils.isBlank(propertyName[propertyName.length - 1])) {
				logger.error("ERROR : 动态表头属性值未在配制文件中配制完全！");
			}
			// 设置Excel标题
			int courseCodeRow = 1;//课程编码占据一行
			WritableFont font = new WritableFont(WritableFont.ARIAL, 10);
			WritableCellFormat titleformat = new WritableCellFormat(font);
			titleformat.setWrap(true);
			titleformat.setAlignment(Alignment.CENTRE);
			titleformat.setVerticalAlignment(VerticalAlignment.CENTRE);
			titleformat.setBorder(Border.ALL, BorderLineStyle.THIN);
			int actualColumn = 0;
			for (int j = 0; j < propertyName.length; j++) {
				String excelTitleName = (String) this.dynamicTitleMap.get(propertyName[j]);
				String tag = propertyName[j].split("_")[0];
				int contentBeginRow = 3;
				if("1".equals(tag)){
					wsheet.mergeCells(actualColumn,contentBeginRow,actualColumn,contentBeginRow+3+courseCodeRow);
				}
				if("2".equals(tag)){
					wsheet.mergeCells(actualColumn,contentBeginRow,actualColumn,contentBeginRow+1);
					wsheet.mergeCells(actualColumn,contentBeginRow+2+courseCodeRow,actualColumn,contentBeginRow+3+courseCodeRow);

					String isOutPlanCourse = "";
					if(propertyName[j].split("_").length >= 5 && Constants.BOOLEAN_YES.equals(propertyName[j].split("_")[4])){
						isOutPlanCourse = "(外)";
					}

					Label cellTitle = new Label(actualColumn, contentBeginRow, propertyName[j].split("_")[3]+isOutPlanCourse, titleformat);
					wsheet.addCell(cellTitle);
					if (courseCodeRow == 1) {
						wsheet.mergeCells(actualColumn,contentBeginRow+2,actualColumn,contentBeginRow+2);
						cellTitle = new Label(actualColumn, contentBeginRow+2, propertyName[j].split ("_")[1].replaceAll("#","_"), titleformat);
						wsheet.addCell(cellTitle);
					}

					cellTitle = new Label(actualColumn, contentBeginRow+2+courseCodeRow, propertyName[j].split("_")[2], titleformat);
					wsheet.addCell(cellTitle);
				}else{
					Label cellTitle = new Label(actualColumn, contentBeginRow, excelTitleName, titleformat);
					wsheet.addCell(cellTitle);
				}

				if (StringUtils.isNotBlank(columnWidth[j])) {
					wsheet.setColumnView(actualColumn, Integer.parseInt(columnWidth[j]));
				}
				actualColumn++;

			}

			if (intHeight > 0) {
				wsheet.setRowView(2, intHeight);
			}
			try{
			//处理数据到单元格
				doFillinDateToCell_WithSpecialFormatWithClasses(wsheet,8,rows,columns, propertyName,codeTableName,propertyDataType,false);
			}catch(Exception e){
				logger.error(e.getMessage());
			}
			wbook.write();
			wbook.close();
		} catch (IOException e) {
			logger.error(e.getMessage(), e.fillInStackTrace());
		} catch (RowsExceededException e) {
			 logger.error(e.getMessage(), e.fillInStackTrace());
		} catch (WriteException e) {
			 logger.error(e.getMessage(), e.fillInStackTrace());
		} catch (BiffException e) {
			 logger.error(e.getMessage(), e.fillInStackTrace());
		}
		return param.getExcelFile();
	}
	/*
	 * 填充单元格数据——带更特殊的格式
	 */
	private void doFillinDateToCell_WithSpecialFormatWithClasses(WritableSheet wsheet,
									int startRow,int endRow,
									int columns,String[] propertyName,
									String[] codeTableName,
									String[] propertyDataType,
									boolean isTemplate) throws RowsExceededException, WriteException{
		//字体
		WritableFont font = new WritableFont(WritableFont.ARIAL, 10);
		WritableFont font_red = new WritableFont(WritableFont.ARIAL, 10);
		font_red.setColour(Colour.RED);
		WritableCellFormat format = new WritableCellFormat(font);
		format.setWrap(true);
		format.setAlignment(Alignment.CENTRE);
		format.setVerticalAlignment(VerticalAlignment.CENTRE);
		format.setBorder(Border.ALL, BorderLineStyle.THIN);
		boolean isMap = false;
		if (param.getModelList().size()>0) {
			Object tmpObj = param.getModelList().get(0);
			if(tmpObj instanceof Map){
				isMap = true;
			}
		}
		for (int i = startRow; i < endRow; i++) {//循环行
			if(isTemplate){
				  //每一次操作，都要插入一行
				if(this.isInsertRow){
					wsheet.insertRow(i);
				}
			}
			Object modelObj = param.getModelList().get(i - startRow);
			BeanWrapper bw = null;
			if(isMap!=true){
				 bw= new BeanWrapperImpl(modelObj);
			}
			for (int j = 0; j < columns; j++) {//循环列
				String strCell = "";
				Object obj;
				if(isMap ==true){
					obj =((Map) modelObj).get(propertyName[j]);
				}else{
					 obj= bw.getPropertyValue(propertyName[j]);
				}
				if (obj != null) {
					strCell = obj.toString().trim();//默认转成String
					//如果为日期类型，则转换对应的pattern格式
					if(obj instanceof Date && propertyDataType[j].indexOf("Date")>=0){
						 String[] strTemp = propertyDataType[j].split("\\ ");
						 String pattern = "";
						 if(strTemp.length > 1){
							 pattern = strTemp[1];
						 }
						 //校验
						if(ExStringUtils.isNotBlank(pattern)){
							try {
								strCell = ExDateUtils.formatDateStr((Date)obj, pattern);
							} catch (ParseException e) {
								logger.error("转换日期出错:{}",e);
							}
						}
					}
				}

				logger.debug("get strCell:{}",strCell);

				//说明没有成绩记录的主干课成绩或一般的课程成绩
				int begincolumn = j;
				for (int index =0 ;index<j;index++) {
					if("3".equals(propertyName[index].split("_")[0])){
						begincolumn++;
					}
				}

				Label cellNormol = new Label(begincolumn, i, ExStringUtils.isNotEmpty(strCell)?strCell:" ", format);
				wsheet.addCell(cellNormol);
			}
			if (intHeight > 0) {
				wsheet.setRowView(i, intHeight);
			}
		}
	}

	public int getSheetNum() {
		return sheetNum;
	}

	public void setSheetNum(int sheetNum) {
		this.sheetNum = sheetNum;
	}

	public String getSheetName() {
		return sheetName==null?"sheet"+sheetNum:sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public boolean isInsertRow() {
		return isInsertRow;
	}

	public void setInsertRow(boolean isInsertRow) {
		this.isInsertRow = isInsertRow;
	}

	public void setHeaderCellFormat(int fonSize) {
		WritableFont font = new WritableFont(WritableFont.ARIAL, fonSize);
		WritableCellFormat format = new WritableCellFormat(font);
		try {
			format.setAlignment(Alignment.CENTRE);
			format.setVerticalAlignment(VerticalAlignment.CENTRE);
		} catch (WriteException e) {
			e.printStackTrace();
		}
		this.headerFormat = format;
	}
}
