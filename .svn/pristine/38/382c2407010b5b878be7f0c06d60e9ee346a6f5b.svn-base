package com.hnjk.extend.plugin.excel.view;

import java.io.File;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.write.WritableWorkbook;

import org.springframework.web.servlet.view.document.AbstractJExcelView;

/**
 * 模板JEexcel视图.<p>
 * 
 * @author 广东学苑教育发展有限公司
 * @since:2009-5-8下午12:14:52
 * @see 
 * @version 1.0
 */
public class TemplateJExcelView extends AbstractJExcelView{
	
	public TemplateJExcelView(){
		
	}
	
	public TemplateJExcelView(String url){
		super.setUrl(url);
	}
	
	/**
	 * dsg
	 */
	@Override
	protected Workbook getTemplateSource(String url, HttpServletRequest request) throws Exception {
		Workbook wb=Workbook.getWorkbook(new File(url));
		return wb;
	}

	@Override
	protected void buildExcelDocument(Map arg0, WritableWorkbook arg1, HttpServletRequest arg2, HttpServletResponse arg3) throws Exception {
		// TODO Auto-generated method stub
		
	}

}

