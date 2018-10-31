package com.hnjk.edu.teaching.util;

import java.io.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.hnjk.core.foundation.utils.ExStringUtils;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

/**
 * 
 * <code>操作导入的成绩文件的工具类</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-12-10 下午02:04:15
 * @see 
 * @version 1.0
 */
public class ExamResultImportFileOperateUtil {
	
	private  static ExamResultImportFileOperateUtil instance;
	private  Logger logger = LoggerFactory.getLogger(getClass());
	
	private ExamResultImportFileOperateUtil(){
		
		
	}
	public  static ExamResultImportFileOperateUtil getInstance() {
		if (null==instance) {
			instance = new ExamResultImportFileOperateUtil();
		}
		return instance;
	}
	/**
	 * 获取上传的Excel文件中隐藏的考试批次ID及课程ID
	 * @param excel
	 * @return
	 */
	public String getExamResultsFileTitleInfo (File excel){
		
		String excelFileTitleInfo = "";
		try {
			Workbook rwb          = Workbook.getWorkbook(excel);
			Sheet sheet           = rwb.getSheet(0);
			Cell examSubCell      = sheet.getCell(0, 2);
			Cell courseCell       = sheet.getCell(0, 4);
			String examSubId      = examSubCell.getContents();
			String courseId       = courseCell.getContents();
			if (ExStringUtils.isNotEmpty(examSubId) && ExStringUtils.isNotEmpty(courseId)) {
			    excelFileTitleInfo=examSubId+"$"+courseId;
			}
		} catch (Exception e) {
			logger.error("读取导入的成绩文件出错，文件路径:"+excel.getAbsolutePath()+"{}");
		} 
		return excelFileTitleInfo;
	}
}
