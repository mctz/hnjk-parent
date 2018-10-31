package com.hnjk.extend.taglib;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.tagext.TagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *  <code>BaseTagSupport</code>,基本的标签支持类. <p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-1-18 下午01:52:16
 * @see 
 * @version 1.0
 */
public class BaseTagSupport  extends TagSupport{

	private static final long serialVersionUID = 5604202780544434071L;
	
	protected  Logger logger = LoggerFactory.getLogger(getClass());
	
	public HttpServletRequest getrequest(){			
		return  (HttpServletRequest)pageContext.getRequest();
	}
	
	public HttpServletResponse getResponse(){
		return (HttpServletResponse)pageContext.getResponse();
	}	
	
	public String getRootUrl(){
		return getrequest().getScheme()+"://"+getrequest().getServerName()+":"+getrequest().getServerPort();
	}
	
	public String getBaseUrl(){
		return getrequest().getScheme()+"://"+getrequest().getServerName()+":"+getrequest().getServerPort()+getrequest().getContextPath();
	}

}
