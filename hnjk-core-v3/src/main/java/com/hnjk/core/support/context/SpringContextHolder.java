package com.hnjk.core.support.context;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 提供一个获取Spring ApplicationContext支持类. <code>SpringContextHolder</code><p>;
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2009-11-27 下午04:26:31
 * @see org.springframework.context.ApplicationContextAware
 * @version 1.0
 */
public class SpringContextHolder implements ApplicationContextAware {

	private static ApplicationContext applicationContext;
	
	/*
	 * 实现ApplicationContextAware接口的context注入函数, 将其存入静态变量
	 * (non-Javadoc)
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		//String ip = ResourceBundle.getBundle("sys_conf").getString("rmi.server.ip");
		//System.setProperty("java.rmi.server.hostname", ip);
		SpringContextHolder.applicationContext = applicationContext;
	}

	/*
	 * 获取静态变量中的applicationContext
	 */
	public static ApplicationContext getApplicationContext() {
		checkApplicationContext();
		return applicationContext;
	}
	
	/*
	 * 根据bean id获取applicationContext中的Bean
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name) {
		checkApplicationContext();
		return (T) applicationContext.getBean(name);
	}
	
	/*
	 * 根据Bean class类型获取
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(Class<T> clazz) {
		checkApplicationContext();
		return (T) applicationContext.getBeansOfType(clazz);
	}
	
	/*
	 * 
	 * 监测是否注入这个类.
	 */
	private static void checkApplicationContext() {
		if (applicationContext == null) {
			throw new IllegalStateException("未注入"+SpringContextHolder.class.getName());
		}
	}
}
