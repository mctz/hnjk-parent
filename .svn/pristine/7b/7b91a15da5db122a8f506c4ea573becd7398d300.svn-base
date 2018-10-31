package com.hnjk.core.foundation.template;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * 模板生成. <p>
 * 提供一个根据freemarker模板构建html的辅助类.
 * @author： 广东学苑教育发展有限公司.
 * @since： 2009-3-11下午08:44:33
 * @see 
 * @version 1.0
 */
public class FreemarkBuildHelper {
	protected static Logger logger = LoggerFactory.getLogger(FreemarkBuildHelper.class);
	
	/**
	 * 根据模板生产html文件.
	 * @param clazz	实体.
	 * @param templateParam 模板参数
	 * @return 生产的文件路径.
	 */
	public static String makeFile(Class clazz,TemplateParam templateParam ){
		//TODO
		return null;
	}
	
	/**
	 * 使用原来的文件名更新html内容.
	 * @param clazz
	 * @param templateParam
	 * @return
	 */
	public static void updateFile(Class clazz,TemplateParam templateParam){
		//TODO
	}
	
	/**
	 * 根据模板内容生成HTML字符串
	 * @param templatePath 模板路径，如/etc/template/mail/mail.ftl
	 * @param content hashMap 用来标记模板中的替换位置
	 * @return
	 */
	public static String buildTemplate(String templatePath,Map content){
		String ret = "";
		try {
			Template template = FreeMarkerConfig.getTemplate(templatePath);
			ret = FreeMarkerTemplateUtils.processTemplateIntoString(template, content);			
		} catch (IOException e) {		
			logger.error("没有找到模板文件",e);
		}catch (TemplateException e) {
			logger.error("构建模板文件失败",e);		
		}
		return ret;
	}
	/***
	 * 根据模板内容生成HTML字符串
	 * @主要功能：
	 * @author: Snoopy Chen (ctfzh@yahoo.com.cn)
	 * @since： 2009-7-30 
	 * @param templateFile  模板文件(存放到/WEB-INF/templates目录下)，如: user_info.ftl
	 * @param content 模板要填充的数据集
	 * @return
	 */
	public static String buildDefaultTemplate(String templateFile,Map content){
		String ret = "";
		try {
			Template template = FreeMarkerConfig.getTemplate(templateFile);
			ret = FreeMarkerTemplateUtils.processTemplateIntoString(template, content);			
		} catch (IOException e) {		
			logger.error("没有找到模板文件",e);
		}catch (TemplateException e) {
			logger.error("构建模板文件失败",e);		
		}
		return ret;
	}
	
}
