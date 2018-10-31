package com.hnjk.core.support.context;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 供测试用例使用.
 * <code>SpringContext</code><p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-3-10 下午04:48:04
 * @see 
 * @version 1.0
 */
public class SpringContextForUnitTestHolder {
	
	private static AbstractApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring/test-applicationContext*.xml");
	
	/**
	 * 获取Bean
	 * @param beanName
	 * @return
	 */
	public  Object getBean(String beanName){		
		return applicationContext.getBean(beanName);
	}	
		
	public  ApplicationContext getApplicationContext(){
		return applicationContext;
	}
	
	public  void close(){
		if(null != applicationContext) {
			applicationContext.close();
		}
	}

}
