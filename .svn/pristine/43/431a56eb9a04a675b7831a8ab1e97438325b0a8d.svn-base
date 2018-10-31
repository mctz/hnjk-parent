package com.hnjk.core.foundation.template;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.springframework.core.io.ClassPathResource;

import com.hnjk.core.support.context.SystemContextHolder;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

/**
 * freemarker配置类
 * 
 * @author 广东学苑教育发展有限公司
 * @since:2009-4-22下午04:38:09
 * @see 
 * @version 1.0
 */
public class FreeMarkerConfig {
	
	private static Configuration cfg = new Configuration();	
	
	/**
	 * 获取一个模板实例
	 * @author cp
	 * @since  2009-4-22 下午04:46:59
	 * @param url
	 * @param fileName 文件名
	 * @param encoding 编码
	 * @return
	 * @throws IOException 
	 * @throws Exception
	 */
	public static Template getTemplate(URL url,String fileName,String encoding) throws IOException{
		File file=new File(url.getFile());	
		cfg.setDirectoryForTemplateLoading(file);	
		cfg.setObjectWrapper(new DefaultObjectWrapper());
		return cfg.getTemplate(fileName,encoding);
	}
	
	/**
	 * 获取一个配置的模板实例(默认路径位于classes/templates下)
	 * @author hzg
	 * @param filePath 文件路径如:/templates/email/email.ftl
	 * @return
	 * @throws IOException 
	 */
	public static Template getTemplate(String filePath) throws IOException{
		ClassPathResource resource = new ClassPathResource(filePath);		
		return getTemplate(resource.getURL(),resource.getFilename(),"utf-8");
	}

	/**
	 * 获取一个默认的模板实例(默认路径为/WEB-INF/templates/)
	 * @param templateFile 
	 * @return
	 * @throws IOException
	 */
	public static Template getDefaultTemplate(String templateFile) throws IOException{
		//设置默认的模板文件目录 /WEB-INF/templates 
		String templatePath = SystemContextHolder.getAppRootPath()+"WEB-INF"+File.separator+"templates"+File.separator;
		cfg.setDirectoryForTemplateLoading(new File(templatePath));
		cfg.setObjectWrapper(new DefaultObjectWrapper());
		return cfg.getTemplate(templateFile,"UTF-8");
	}
}

