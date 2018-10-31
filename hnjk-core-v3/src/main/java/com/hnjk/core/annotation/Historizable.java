package com.hnjk.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.hnjk.core.rao.dao.listener.OperationType;

/**
 * 定义是否需要记录审计日志.
 * <code>Historizable</code><p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2011-11-17 下午03:47:36
 * @see 
 * @version 1.0
 */
@Target(ElementType.TYPE)   
@Retention(RetentionPolicy.RUNTIME)   
@Documented  
@Inherited  
public @interface Historizable {
	boolean logable() default false;//是否允许记录日志,默认不记录
	OperationType[] value();//记录日志类型
}
