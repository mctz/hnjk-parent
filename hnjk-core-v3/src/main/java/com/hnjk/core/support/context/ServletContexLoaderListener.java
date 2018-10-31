package com.hnjk.core.support.context;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * 提供了一个实现了<code>ServletContextListener</code>的监听器. <p>
 * 使用此监听器，可以使用ServletContext来缓存一些全局变量.
 * @author： 广东学苑教育发展有限公司.
 * @since： 2009-3-4下午06:38:52
 * @version 1.0
 */
public class ServletContexLoaderListener  implements ServletContextListener  {	
	
	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		//TODO
	}

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		//获取webroot webapps 根目录 
		String webAppRootKey = servletContextEvent.getServletContext().getRealPath("/");  
		System.setProperty("xy.root" , webAppRootKey);	
	}
}
