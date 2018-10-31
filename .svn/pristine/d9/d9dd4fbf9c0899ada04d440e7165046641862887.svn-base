package com.hnjk.core.support.remoting;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import com.hnjk.core.annotation.Remoting;
import com.hnjk.core.annotation.Schedule;
import com.hnjk.core.foundation.cache.MemcachedManager;
import com.hnjk.core.support.schedule.quartz.QuartzTrigger;

/**
 * 使用注解方式的远程服务管理.需要配置在applicationContext中.<p>
 * 使用场景：<p>
 * 1、需要公布本地服务为远程服务;<p>
 * 2、需要将本地服务中的方法加入调度服务器<p>
 * @author hzg
 * @see com.hnjk.core.annotation.Remoting
 * @see com.hnjk.core.annotation.Schedule
 */
public class HttpinvokerRemoteServiceFactory implements  InitializingBean,ApplicationContextAware {	
	
	//private long defaultTimeout = TimeUnit.SECONDS.toMillis(5*60);//默认超时时间	
	public final static String URLHANDLER_NAME= "simpleUrlHandlerMapping";
	
	private  ApplicationContext applicationContext;//spring context
	
	private List<String> packagesToScan = new ArrayList<String>();//扫描包路径
	
	private MemcachedManager memcachedManager;//memcached，用来缓存调度任务
	
	@Override
	public void afterPropertiesSet() throws Exception {
		memcachedManager = (MemcachedManager)applicationContext.getBean("spyMemcachedClient");
		setApplicationContext(initRemoteService());
	}
		
	public ExHttpInvokerServiceExporter getServiceExporter(String remoteBeanName) throws Exception {
		ExHttpInvokerServiceExporter exporter = (ExHttpInvokerServiceExporter)applicationContext.getBean(remoteBeanName);
		exporter.afterPropertiesSet();
		return exporter;
	}

		
	//初始化远程服务
	private  ApplicationContext initRemoteService() throws InstantiationException, IllegalAccessException, ClassNotFoundException{
		//扫描包						
		DefaultListableBeanFactory acf = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();		
		ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(acf, false);	
		
		scanner.addIncludeFilter(new AnnotationTypeFilter(Remoting.class));
		scanner.setScopedProxyMode(ScopedProxyMode.TARGET_CLASS);
		
		if(getPackagesToScan() != null ){				
			
				for(String pkg : getPackagesToScan()){
					Set<BeanDefinition> candidates = scanner.findCandidateComponents(pkg);
					for(BeanDefinition bean :candidates){		
						//获取远程服务的本地服务
						Object service = Class.forName(bean.getBeanClassName()).newInstance();
						Remoting info = service.getClass().getAnnotation(Remoting.class);
												
						//注册httpinvokerExecutor
						RootBeanDefinition executor = new RootBeanDefinition(BeanUtils.instantiateClass(ExHttpInvokerServiceExporter.class).getClass());						
						acf.registerBeanDefinition(info.urlMapping(), executor);
						
						//设置依赖remoting service 与httpinvokerExporter 的依赖
						MutablePropertyValues executorPropertyValues = new MutablePropertyValues();  		
						executorPropertyValues.addPropertyValue(new PropertyValue("serviceInterface",info.serviceInterface()));  //service interface 
						executorPropertyValues.addPropertyValue(new PropertyValue("service",applicationContext.getBean(info.servicename())));  //注入service 												
						executor.setPropertyValues(executorPropertyValues);		
																		
						//处理调度
						Method [] methods = service.getClass().getDeclaredMethods(); 
						if(null != methods){
							for(int i=0;i<methods.length;i++){
								Method method = methods[i];
								if(method.isAnnotationPresent(Schedule.class)){//如果有调度注释
									Schedule schedule = method.getAnnotation(Schedule.class);
									//生成调度：1.构造调度对象
									QuartzTrigger quartzTrigger = new QuartzTrigger();
									quartzTrigger.setCronExpression(schedule.expression());
									quartzTrigger.setJobName(schedule.name());
									quartzTrigger.setJobGroup(schedule.group());
									quartzTrigger.setDesc(schedule.desc());
									quartzTrigger.setServiceUrl(info.urlMapping());
									//2.推送到中央缓存
									memcachedManager.put(schedule.name(), 0, quartzTrigger);
								}//end if
							}//end for
						}//end if						
						
						
					}//end for
				}//end for				
				
		}
		return applicationContext;
		
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
	

	/**
	 * @return the applicationContext
	 */
	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	 * @return the packagesToScan
	 */
	public List<String> getPackagesToScan() {
		return packagesToScan;
	}



	/**
	 * @param packagesToScan the packagesToScan to set
	 */
	public void setPackagesToScan(List<String> packagesToScan) {
		this.packagesToScan = packagesToScan;
	}


	

		
	
}
