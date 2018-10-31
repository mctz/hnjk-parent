package com.hnjk.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定义一个用来标注，model的子对象是否需要删除
 * @author hzg
 *
 */
@Target(ElementType.FIELD)   
@Retention(RetentionPolicy.RUNTIME)   
@Documented  
@Inherited  
public @interface DeleteChild {
	public boolean deleteable() default true;//是否允许删除子表
}
