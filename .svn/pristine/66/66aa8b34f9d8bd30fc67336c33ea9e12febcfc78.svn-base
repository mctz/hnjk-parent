package com.hnjk.core.support.base.controller;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomBooleanEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.servlet.DispatcherServlet;

import com.hnjk.core.foundation.utils.CustomDateEditor;


/**
 * 实现Spring MVC <code>DispatcherServlet</code>的控制器基类.<p>
 * 其他的Controller均继承此基类，默认情况下提供一个统一资源管理的注入.<p>
 * @author： 广东学苑教育发展有限公司.
 * @since： 2009-2-27下午02:56:48
 * @see #org.springframework.web.servlet.DispatcherServlet()
 * @version 1.0
 */
public class BaseController extends DispatcherServlet{
	
	private static final long serialVersionUID = 3590817951907508588L;

	protected Logger logger = LoggerFactory.getLogger(getClass());
		
	/**
	 * 注册自定义的日期格式化编辑器
	 * @param binder
	 */
	@InitBinder
    public void initBinder(WebDataBinder binder) {	
        binder.registerCustomEditor(Date.class, new CustomDateEditor(true));
        binder.registerCustomEditor(Boolean.class, new CustomBooleanEditor(true));
		binder.registerCustomEditor(Integer.class, null,new CustomNumberEditor(Integer.class, null, true));
		binder.registerCustomEditor(Long.class, null, new CustomNumberEditor(Long.class, null, true));
		binder.registerCustomEditor(Float.class, new CustomNumberEditor(Float.class, true));
		binder.registerCustomEditor(Double.class, new CustomNumberEditor(Double.class, true));
		binder.registerCustomEditor(BigInteger.class, new CustomNumberEditor(BigInteger.class, true));
		binder.registerCustomEditor(BigDecimal.class, new CustomNumberEditor(BigDecimal.class, false));
    }
	
	
	
}
