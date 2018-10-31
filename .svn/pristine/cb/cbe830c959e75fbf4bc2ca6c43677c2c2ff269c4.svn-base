package com.hnjk.extend.plugin.fileconvert;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hnjk.core.exception.ServiceException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.html.simpleparser.HTMLWorker;
import com.lowagie.text.html.simpleparser.StyleSheet;
import com.lowagie.text.rtf.RtfWriter2;

/**
 * 文档转换器 --> RTF文件类型.
 * @author hzg
 *
 */
public class DocumentToRtfConvert {
	
	protected static Logger logger = LoggerFactory.getLogger(DocumentToRtfConvert.class);
	
	/**
	 * Html内容转换为rtf文件
	 * @param storePath 文件存储在服务器上的路径
	 * @param htmlContent html内容
	 * @param rectangle 纸张大小，默认为A4
	 * @return
	 * @throws FileNotFoundException 
	 * @throws IOException 
	 * @throws DocumentException 
	 */
	public static void htmlToRtfBuilder(String storePath,String htmlContent,Rectangle rectangle)throws ServiceException{
	
		Paragraph context = new Paragraph();  
		try {
			Document document = getDocument(rectangle, storePath);
			
			StyleSheet ss = new StyleSheet();  //默认样式
			
	        List htmlList = HTMLWorker.parseToList(new StringReader(htmlContent), ss);  
	        for(int i=0;i<htmlList.size();i++){  //解析HTML
	           com.lowagie.text.Element e = (com.lowagie.text.Element)htmlList.get(i);  
	           context.add(e);  
	       }  
	        document.add(context);  //创建
	        closeDocument(document);//关闭
		} catch (Exception e) {
			logger.error("HTML-->RTF出错:"+e.fillInStackTrace());
			throw new ServiceException("创建文档出错.");
		}	
	}
	
	private static Document getDocument(Rectangle rectangle,String storePath) throws FileNotFoundException{
		Rectangle defaultRectangel = PageSize.A4;
		if(null != rectangle){
			defaultRectangel = rectangle;
		}
		Document document = new Document(defaultRectangel);
		RtfWriter2.getInstance(document, new FileOutputStream(storePath));
		document.open();
		
		return document;
	}
	
	private static void closeDocument(Document document){
		if (null != document) {
			document.close();
		}
	}
}
