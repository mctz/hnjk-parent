package com.hnjk.core.support.base.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.Cleanup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;

import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.FileUtils;
import com.hnjk.core.foundation.utils.ZipUtils;

/**
 * 提供一个基本的页面方法抽象Controller类. <p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2009-3-9下午06:36:55
 * @modify: 
 * @主要功能：
 * @see 
 * @version 1.0
 */
public abstract class BaseSupportController extends BaseController {
	
	private static final long serialVersionUID = -5568039278783942375L;


	protected static Logger logger = LoggerFactory.getLogger(BaseSupportController.class);
	
	/**进行操作后，页面是否重新加载*/
	public static final boolean ISRELOAD = true;
	
	/**encoding前缀*/
	private static final String ENCODING_PREFIX = "encoding";
	
	/**page haader cache*/
	private static final String NOCACHE_PREFIX = "no-cache";
	
	/**编码格式*/
	private static final String ENCODING_DEFAULT = "UTF-8";
	
	/**默认是否为no-cache*/
	private static final boolean NOCACHE_DEFAULT = true;
	
		
	//记录ID集合，用于批量更新/审核等情况
	public List<Serializable> resourceid;			
	
	public List<Serializable> getResourceid() {
		return resourceid;
	}

	public void setResourceid(List<Serializable> resourceid) {
		this.resourceid = resourceid;
	}

	/**
	 * 绕过JSP直接输出，用于如AJAX等请求的情况。
	 * eg.
	 * render("text/plain", "hello", "encoding:GBK");
	 * render("text/plain", "hello", "no-cache:false");
	 * render("text/plain", "hello", "encoding:GBK", "no-cache:false");
	 * @param contentType
	 * @param content
	 * @param headers 可变的header数组，目前接受的值为"encoding:"或"no-cache:",默认值分别为UTF-8和true.
	 */
	public static void render(HttpServletResponse response,final String contentType, String content, final String... headers) {
		try {
			//分析headers参数
			String encoding = ENCODING_DEFAULT;
			boolean noCache = NOCACHE_DEFAULT;
			for (String header : headers) {
				String headerName = ExStringUtils.substringBefore(header, ":");
				String headerValue = ExStringUtils.substringAfter(header, ":");

				if (ExStringUtils.equalsIgnoreCase(headerName, ENCODING_PREFIX)) {
					encoding = headerValue;
				} else if (ExStringUtils.equalsIgnoreCase(headerName, NOCACHE_PREFIX)) {
					noCache = Boolean.parseBoolean(headerValue);
				} else {
					throw new IllegalArgumentException(headerName + "不是一个合法的header类型");
				}
			}		

			//设置headers参数
			String fullContentType = contentType + ";charset=" + encoding;
			response.setContentType(fullContentType);
			if (noCache) {				
				response.addHeader("Cache-Control", "no-store, no-cache, must-revalidate");   
				response.addHeader("Cache-Control", "post-check=0, pre-check=0");  
				response.setHeader("Pragma", "No-cache");				
				response.setDateHeader("Expires", 0);
			}

			response.getWriter().write(content);			
			response.getWriter().flush();
			
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}finally{
			try {
				content = null;
				response.getWriter().close();
			} catch (IOException e) {
				logger.error("绕过JSP直接输出，用于如AJAX等请求的情况关闭PrintWriter出错:{}"+e.fillInStackTrace());
			}
		}
	}
	
	/**
	 * 直接输出文本.
	 * @see BaseSupportController#render
	 */
	public static void renderText(HttpServletResponse response ,final String text, final String... headers) {
		render(response,"text/plain", text, headers);
	}

	/**
	 * 直接输出HTML.
	 * @see BaseSupportController#render
	 */
	public static void renderHtml(HttpServletResponse response,final String html, final String... headers) {
		render(response,"text/html", html, headers);
	}

	/**
	 * 直接输出XML.
	 * @see BaseSupportController#render
	 */
	public static void renderXml(HttpServletResponse response,final String xml, final String... headers) {
		render(response,"text/xml", xml, headers);
	}

	/**
	 * 直接输出JSON.
	 * 
	 * @param string json字符串.
	 * @see BaseSupportController#render
	 */
	public static void renderJson(HttpServletResponse response,final String string, final String... headers) {
		render(response,"application/json", string, headers);
	}
	
	/**
	 * 输出为Excel文件.
	 * @param response
	 * @param fileName
	 * @param filePath
	 * @param isCleearSourceFile
	 * @param isZip
	 * @throws IOException
	 */
	public static void renderExcel(HttpServletResponse response,final String fileName,
								final String filePath,final boolean isCleearSourceFile,
								final boolean isZip,final boolean isInlineOpen)throws IOException{
		renderFile(response,"application/ms-excel", fileName, filePath,isCleearSourceFile,isZip,isInlineOpen);
	}
	
	/**
	 * 输出为PDF文件.
	 * @param response
	 * @param fileName
	 * @param filePath
	 * @param isCleearSourceFile
	 * @param isZip
	 * @throws IOException
	 */
	public static void renderPdf(HttpServletResponse response,final String fileName,
						final String filePath,final boolean isCleearSourceFile,
							final boolean isZip,final boolean isInlineOpen) throws IOException{
		renderFile(response,"application/pdf",  fileName, filePath,isCleearSourceFile,isZip,isInlineOpen);
	}
	
	/**
	 * 输出为文件.
	 * @param response
	 * @param contentType application/pdf ...
	 * @param fileName 输出的文件名
	 * @param filePath 原文件路径
	 * @param isCleearSourceFile 是否清理原文件
	 * @param isInlineOpen 是否在线打开 
	 * @param isZip 是否启用压缩
	 */
	protected static void renderFile(HttpServletResponse response,final String contentType, String fileName,String filePath,
				boolean isCleearSourceFile,boolean isZip,boolean isInlineOpen) throws IOException{
		//如果启用压缩	
		if(isZip){
			String zipFile = filePath.substring(0,filePath.lastIndexOf(".")).concat(".zip");
			//替换压缩包里的文件名
			String extName = filePath.substring(filePath.lastIndexOf(".") + 1).toLowerCase();//源文件扩展名	
			String replaceName = fileName.substring(0,fileName.lastIndexOf(".")).concat(".").concat(extName);			
			try {
				ZipUtils.zip(filePath, zipFile,replaceName);
				filePath = zipFile;
			} catch (Exception e) {				
			}
		}
		// 读到流中		
		@Cleanup InputStream inStream       = new FileInputStream(filePath);// 文件的存放路径
		@Cleanup OutputStream outStream = response.getOutputStream();
		try {		
			String encodedfileName = new String(fileName.getBytes("GBK"), "ISO8859-1");	
			// 设置输出的格式
			response.reset();			
			String fullContentType = contentType + ";charset=" + ENCODING_DEFAULT;			
			response.setContentType(fullContentType);
			String openType = "attachment";
			if (isInlineOpen) {
				openType = "inline";
			}
			response.addHeader("Content-Disposition", openType+"; filename=\"" + encodedfileName + "\"");
			// 循环取出流中的数据
			byte[] b 			   = new byte[1024];
			int len;
			
			while ((len = inStream.read(b)) > 0) {
				outStream.write(b, 0, len);
			}

		}finally{
			if (isCleearSourceFile) {
				FileUtils.delFile(filePath);
			}
		}
	}
	
	/**
	 * 输出对象为数据流
	 * @param response
	 * @param obj
	 */
	public static void renderStream(HttpServletResponse response, Object obj){
		response.setContentType("application/octet-stream");
		try {
			@Cleanup ServletOutputStream ouputStream = response.getOutputStream();
			@Cleanup ObjectOutputStream oos = new ObjectOutputStream(ouputStream);
			oos.writeObject(obj);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}finally{		
			obj = null;
		}
		
	}
	
	/**
	 * 统一异常处理(异常捕捉)
	 * @param request
	 * @param response
	 * @param handler 异常handler
	 * @param ex 异常
	 * @return
	 */
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		Map<String,Object> model = new HashMap<String,Object>();
		model.put("errHandle", handler.getClass().getSimpleName());
        model.put("ex", ex.getClass().getSimpleName());
        model.put("error", ex.fillInStackTrace());
        
        //renderJson(response,JsonUtils.mapToJson(model));
        return new ModelAndView("/common/error", model);
       
	}
	
	public static Map<String, Object> getConditionFromResquestByIterator(HttpServletRequest request) {
		Map<String, Object> condition = new HashMap<String, Object>();
		Enumeration em = request.getParameterNames();
		while (em.hasMoreElements()) {
			String name = (String) em.nextElement();
			Object value = request.getParameter(name);
			if(value!=null && !"undefined".equals(value) && !"".equals(value)){
				condition.put(name, value);
			}
		}
		//当 conditon 中包含 pageNum 和 pageSize 时，点击翻页时，request中将有多个pageNum和pageSize，因此，condition中移除多余的参数
		if(condition.containsKey("pageNum")){
			condition.remove("pageNum");
		}
		if(condition.containsKey("pageSize")){
			condition.remove("pageSize");
		}
		return condition;
	}
}
