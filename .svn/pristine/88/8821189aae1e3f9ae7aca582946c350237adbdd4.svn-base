package com.hnjk.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 远程调用注解.
 * @author Administrator
 *
 */
@Target(ElementType.TYPE)   
@Retention(RetentionPolicy.RUNTIME)   
@Documented  
@Inherited  
public @interface Remoting {
	
	public String servicename();//名字
	
	public Class<?> serviceInterface();//接口
	
	public String urlMapping() ; //Remoting URL mapping
	
	public RemotingType executor() default RemotingType.HTTPINVOKER;//使用远程调用方式
	
	public String[] methods() default "";//需要暴露的方法
}
