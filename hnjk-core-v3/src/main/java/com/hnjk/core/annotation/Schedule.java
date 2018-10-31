package com.hnjk.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)   
@Retention(RetentionPolicy.RUNTIME)   
@Documented  
@Inherited 
public @interface Schedule {

	public String expression(); //表达式
	
	public String name() default "";//名称
	
	public String group()  default "";//分组
	
	public String desc()  default "";//描述
	
}
