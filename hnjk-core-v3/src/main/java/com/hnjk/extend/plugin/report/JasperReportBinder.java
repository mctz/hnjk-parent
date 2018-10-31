package com.hnjk.extend.plugin.report;

import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.Collection;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.base.JRBaseReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;

import com.hnjk.core.exception.ServiceException;

/**
 * 使用jasperreport作为报表工具，输出报表的binder.<p>
 * <code>JasperReportBinder</code><p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2011-2-24 下午03:32:13
 * @see 
 * @version 1.0
 */
public class JasperReportBinder {	
	
	/**输出类型 - EXCEL*/
	public final static String REPORT_EXPORT_TYPE_EXCEL = "excel";
	
	/**输出类型 - html*/
	public final static String REPORT_EXPORT_TYPE_HTML = "html";
	
	/**输出类型 - pdf*/
	public final static String REPORT_EXPORT_TYPE_PDF = "pdf";
	
	/**输出类型 - word*/
	public final static String REPORT_EXPORT_TYPE_WORD = "word";
	
	/**输出类型 - print*/
	public final static String REPORT_EXPORT_TYPE_PRINT = "print";
	
	/**
	 * 输出之前的准备
	 * @param jasperReport
	 * @param type
	 */
	private static void prepareReport(JasperReport jasperReport, String type) {		
		if (REPORT_EXPORT_TYPE_EXCEL.equals(type)) {
			try {
				Field margin = JRBaseReport.class.getDeclaredField("leftMargin");//获取左侧边距，并设置为0
				margin.setAccessible(true);
				margin.setInt(jasperReport, 0);
				margin = JRBaseReport.class.getDeclaredField("topMargin");//获取顶侧边距,设置为0
				margin.setAccessible(true);
				margin.setInt(jasperReport, 0);
				margin = JRBaseReport.class.getDeclaredField("bottomMargin");//获取底侧边距，设置为0
				margin.setAccessible(true);
				margin.setInt(jasperReport, 0);
				Field pageHeight = JRBaseReport.class.getDeclaredField("pageHeight");//获取页面高度
				pageHeight.setAccessible(true);
				pageHeight.setInt(jasperReport, 2147483647);
			} catch (Exception exception) {
			}
		}
	}

	/**
	 * 输出为excel类型
	 * @param jasperPrint
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws JRException
	 */
	private static void exportExcel(JasperPrint jasperPrint,HttpServletRequest request, HttpServletResponse response) throws IOException, JRException {
		
		// 设置http头信息		 
		response.setContentType("application/vnd.ms-excel");
		String fileName = new String("未命名.xls".getBytes("GBK"), "ISO8859_1");
		response.setHeader("Content-disposition", "attachment; filename="+ fileName);

		ServletOutputStream ouputStream = response.getOutputStream();
		JRXlsExporter exporter = new JRXlsExporter();
		//构造excel参数
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, ouputStream);
		exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,Boolean.TRUE);
		exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET,Boolean.FALSE);
		exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND,Boolean.FALSE);
		exporter.exportReport();
		ouputStream.flush();
		ouputStream.close();
	}

	/**
	 * 输出为PDF类型	  
	 * ireport中，需要设置pdf font name ：STSong-Light ，pdf encoding ： UniGB-UCS2-H
	 *
	 * @param jasperPrint
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws JRException
	 */
	private static void exportPdf(JasperPrint jasperPrint,HttpServletRequest request, HttpServletResponse response)throws IOException, JRException {
		//设置http头
		response.setContentType("application/pdf");
		String fileName = new String("未命名.pdf".getBytes("GBK"), "ISO8859_1");
		response.setHeader("Content-disposition", "attachment; filename="+ fileName);
		
		ServletOutputStream ouputStream = response.getOutputStream();
		JasperExportManager.exportReportToPdfStream(jasperPrint, ouputStream);
		ouputStream.flush();
		ouputStream.close();
	}

	/**
	 * 输出为html类型
	 * @param jasperPrint
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws JRException
	 */
	private static void exportHtml(JasperPrint jasperPrint,HttpServletRequest request, HttpServletResponse response)	throws IOException, JRException {
		response.setContentType("text/html");
		ServletOutputStream ouputStream = response.getOutputStream();
		JRHtmlExporter exporter = new JRHtmlExporter();
		exporter.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN,	Boolean.FALSE);
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, "UTF-8");
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, ouputStream);

		exporter.exportReport();

		ouputStream.flush();
		ouputStream.close();
	}

	/**
	 * 输出为word类型
	 * @param jasperPrint
	 * @param request
	 * @param response
	 * @throws JRException
	 * @throws IOException
	 */
	private static void exportWord(JasperPrint jasperPrint,HttpServletRequest request, HttpServletResponse response)	throws JRException, IOException {
		//设置http头
		response.setContentType("application/msword;charset=utf-8");
		String fileName = new String("未命名.doc".getBytes("GBK"), "ISO8859_1");
		response.setHeader("Content-disposition", "attachment; filename="+ fileName);
		
		JRExporter exporter = new JRRtfExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM,response.getOutputStream());

		exporter.exportReport();
	}

	/**
	 * 输出到打印机
	 * @param jasperPrint
	 * @param response
	 * @param request
	 * @throws IOException
	 */
	private static void exportPrint(JasperPrint jasperPrint,	HttpServletResponse response, HttpServletRequest request)throws IOException {
		response.setContentType("application/octet-stream");
		ServletOutputStream ouputStream = response.getOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(ouputStream);
		oos.writeObject(jasperPrint);
		oos.flush();
		oos.close();
		ouputStream.flush();
		ouputStream.close();
	}
	
	/**
	 * 使用集合类型的数据，封装报表数据源
	 * @param datas
	 * @return
	 */
	public static JRDataSource jrDataSourceMapper(Collection<?> datas){		 
		 return new JRBeanCollectionDataSource(datas, false);
	}
	
	/**
	 * 报表输出
	 * @param type 输出类型，如excel,pdf,html等
	 * @param datas 输出的数据集合，如果使用jdbc connect方式，必须设置为null
	 * @param file 报表文件
	 * @param parameters 参数
	 * @param conn JDBC connect，如果使用JRDatasource，必须设置为null
	 * @param request
	 * @param response
	 */
	public static void export(String type, Collection<?> datas, File file, Map<String, Object> parameters,Connection conn,
							HttpServletRequest request, HttpServletResponse response) throws ServiceException{
		 try {
			 JasperReport jasperReport = (JasperReport) JRLoader.loadObject(file);
			 prepareReport(jasperReport, type);
			 JasperPrint jasperPrint = null;
			 if(null != datas){
				 jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, jrDataSourceMapper(datas));
			 }			 
			 
			  if(null != conn){
				  jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,conn);
			  }
			 
			  if(null == jasperPrint){
				  throw new ServiceException("缺少打印数据！");
			  }
			 if (REPORT_EXPORT_TYPE_EXCEL.equals(type)) {
				 exportExcel(jasperPrint, request, response);
			  } else if (REPORT_EXPORT_TYPE_PDF.equals(type)) {
				  exportPdf(jasperPrint, request, response);
			  } else if (REPORT_EXPORT_TYPE_HTML.equals(type)) {
				  exportHtml(jasperPrint, request, response);
			  } else if (REPORT_EXPORT_TYPE_WORD.equals(type)) {
				  exportWord(jasperPrint, request, response);
			  }else if(REPORT_EXPORT_TYPE_PRINT.equals(type)){
				  exportPrint(jasperPrint, response, request);
			  }
		 	} catch (Exception e) {		 		
			   throw new ServiceException("报表输出错误："+e.fillInStackTrace());
		 }
	 }
}
