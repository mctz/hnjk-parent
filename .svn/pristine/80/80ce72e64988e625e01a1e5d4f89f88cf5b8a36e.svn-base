package com.hnjk.platform.system.resources;

import java.net.URL;

import org.springframework.stereotype.Service;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.FileUtils;

/**
 * 部署公共资源.
 * @author hzg
 *
 */
@Service
public class DeployResourceService {

	public final static String RESOURCE_TYPE_JS = "js";//JS
	
	public final static String RESOURCE_TYPE_THEMES = "themes";//themes
	
	public final static String RESOURCE_TYPE_STYLE = "style";//style css
	
	public final static String RESOURCE_TYPE_IMAGES = "images";//images
	/**
	 * 部署资源
	 * @param type 资源类型 js , themes, style
	 * @param deployPath
	 * @throws ServiceException
	 */
	public void deployResource(String type,String deployPath) throws ServiceException{
		try {
			//部署jscript
			if(type.equals(RESOURCE_TYPE_JS)){
				URL url  = this.getClass().getResource("/META-INF/jscript");
				FileUtils.copyFolder(url.getFile(), deployPath);
			}else if(type.equals(RESOURCE_TYPE_THEMES)){
				URL url  = this.getClass().getResource("/META-INF/themes");
				FileUtils.copyFolder(url.getFile(), deployPath);
			}else if(type.equals(RESOURCE_TYPE_STYLE)){
				URL url  = this.getClass().getResource("/META-INF/style");
				FileUtils.copyFolder(url.getFile(), deployPath);
			}else if(type.equals(RESOURCE_TYPE_IMAGES)){
				//nothing
			}
		} catch (Exception e) {
			throw new ServiceException("部署公共资源出错："+e.fillInStackTrace());
		}
		
	}
}
