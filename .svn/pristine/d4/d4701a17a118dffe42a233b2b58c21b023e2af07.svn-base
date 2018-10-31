package com.hnjk.extend.plugin.excel.file;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import jxl.Workbook;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import com.hnjk.extend.plugin.excel.config.ConfigParam;

/**
 * 模型转换为excel对象的消息实现类. <p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2009-4-29上午11:27:53
 * @see 
 * @version 1.0
 */
public class ModelToExcelMessage {
	protected Logger logger = LoggerFactory.getLogger(getClass());

	private int startTitleRow = 0;//标题起始行
	
	private int intSheet = 0;//起始sheet

	private ConfigParam param;//配置参数
	
	public void setStartTitleRow(int startTitleRow) {
		// TODO Auto-generated method stub
		this.startTitleRow = startTitleRow;
	}
	
	public void setIntSheet(int intSheet) {
		this.intSheet = intSheet;
	}
	
	public ModelToExcelMessage() {

	}

	/**
	 * 构造函数
	 * @param param 配置参数
	 */
	public ModelToExcelMessage(ConfigParam param) {
		this.param = param;
	}

	/**
	 * 获取错误的文件列表
	 * @return
	 */
	public File getErrorFile() {	
		getExcelFile(1);
		return param.getExcelFile();
	}

	/**
	 * 获取全部文件列表
	 * @return
	 */
	public File getFile() {	
		getExcelFile(-1);
		return param.getExcelFile();
	}

	/**
	 * 获取转换成功的excel列表
	 * @return
	 */
	public File getSuccessFile() {		
		getExcelFile(0);
		return param.getExcelFile();
	}
	
	/**
	 * 获取excel文件内容
	 * @param flag
	 */
	private void getExcelFile(int flag) {
		try {
			Workbook book = Workbook.getWorkbook(param.getExcelFile());
			WritableWorkbook wbook = Workbook.createWorkbook(param.getExcelFile(),book);
			
			WritableSheet wsheet = wbook.getSheet(intSheet);
			
			int intModelSize = param.getModelList().size();
			String propertyName = (String) param.getExcelConfig().getMessageMap().get("name");//消息标题
			String flagName = (String) param.getExcelConfig().getFlagMap().get("name");//标示

			int intColumn = wsheet.getColumns();
			//设置错误消息列宽
			wsheet.setColumnView(intColumn,60);
			
			boolean isMap = false;
            if (param.getModelList().size()>0) {
                Object tmpObj = param.getModelList().get(0);
                if(tmpObj instanceof Map){
                    isMap = true;
                }
            }			
			
			for (int i = startTitleRow; i < wsheet.getRows(); i++) {
				logger.debug("Cell[" + i + "][" + intColumn + "]");				
				// 在最后一列写标题和内容				
				if (i > startTitleRow) {
					String strMessage = "";
					if (intModelSize > i - 1 - startTitleRow) {
						if(isMap){
							strMessage = (String)((Map)param.getModelList().get(i- 1 - startTitleRow)).get(propertyName);
						}else{
							BeanWrapper bw = new BeanWrapperImpl(param.getModelList().get(i- 1 - startTitleRow));
							strMessage = (String) bw.getPropertyValue(propertyName);
						}						
					}
					Label labelC = new Label(intColumn, i, strMessage,getDefaultNormolFormat());					
					wsheet.addCell(labelC);
				} else {
					String excelTitleName = (String) param.getExcelConfig().getMessageMap().get("excelTitleName");
					Label labelC = new Label(intColumn, i, excelTitleName,getDefaultTitleFormat());
					wsheet.addCell(labelC);					
				}
				
			}
			if (flag > -1) {				
				for (int i = wsheet.getRows() - 1; i >= startTitleRow + 1; i--) {
					// 只要正确的行
					if (intModelSize >= i - startTitleRow) {
						String strFlag="";
						if(isMap){
							strFlag =(String)((Map)param.getModelList().get(i- 1 - startTitleRow)).get(flagName);
						}else{
							BeanWrapper bw = new BeanWrapperImpl(param.getModelList().get(i- 1 - startTitleRow));
							strFlag = (String) bw.getPropertyValue(flagName);							
						}
						
						// 只要错误数据，strFlag等于0的为正确数据
						logger.info("i=" + i + "  flag =" + flag + " strFlag =" + strFlag );
						if ((flag == 1) && "0".equals(strFlag)) {
							wsheet.removeRow(i);
						}
						// 只要正确数据，strFlag等于1的为错误数据
						if ((flag == 0) && "1".equals(strFlag)) {
							wsheet.removeRow(i);
						}
					}
				}
			}
			wbook.write();
			wbook.close();
		} catch (IOException e) {			
			logger.error(e.getMessage(),e);
		} catch (RowsExceededException e) {			
			logger.error(e.getMessage(),e);
		} catch (WriteException e) {
			logger.error(e.getMessage(),e);
		} catch (BiffException e) {
			logger.error(e.getMessage(),e);
		}

	}

	/**
	 * 获取默认标题样式
	 * @return
	 */
	private WritableCellFormat getDefaultTitleFormat() {
        // 字体：TIMES,大小为14号,粗体，斜体,有下划线
        WritableFont font = new WritableFont(WritableFont.TIMES, 14, WritableFont.BOLD, true, UnderlineStyle.SINGLE);
        try {
            font.setColour(Colour.RED);// 蓝色字体
        } catch (WriteException e1) {
            logger.error(e1.getMessage(),e1);
        }
        WritableCellFormat format = new WritableCellFormat(font);

        try {
            format.setAlignment(jxl.format.Alignment.CENTRE);
            format.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
            format.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);
        } catch (WriteException e) {
        	logger.error(e.getMessage(),e);
        }
        return format;
    }
	
	/**
	 * 获取写入excel格式
	 * @return
	 */
    private WritableCellFormat getDefaultNormolFormat() {
        WritableFont font = new WritableFont(WritableFont.TIMES, 12);
        WritableCellFormat format = new WritableCellFormat(font);
        try {
            //format.setAlignment(jxl.format.Alignment.CENTRE);
        	format.setAlignment(jxl.format.Alignment.LEFT);
            format.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
            format.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);
        } catch (WriteException e) {
        	logger.error(e.getMessage(),e);
        }
        return format;
    }

	
	
	public static void main(String[] args) {
//		List list = new ArrayList();
//		DeptModel dept = new DeptModel();
//		dept.setFlag("0");
//		dept.setMessage("00000");
//		DeptModel dept1 = new DeptModel();
//		dept1.setFlag("1");
//		dept1.setMessage("111111");
//		list.add(dept);
//		list.add(dept1);
//
//		ModelToExcelMessageImpl mte = new ModelToExcelMessageImpl(new File(
//				"D:\\work\\workspace\\excelfile\\test.xls"), ExcelConfigFactory
//				.createExcelConfigManger().getModel("deptModel", ""), list);
//		 mte.getFile();
//		//mte.getErrorFile();
//		//mte.getSuccessFile();

	}
}
