package com.hnjk.core.support.context;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * HttpSessionUser过滤器，用于非spring security框架的验证。
 * 1. 如果在未登录的情况下访问需要登录才能访问的页面，自动跳转到错误提示页面
 * 2.在每次请求前将session中的user放到threadlocal变量中，这样就可以在程序的任何地方通过
 * 方法HttpSessionUserFilter.getLoginUser()得到当前登录的用户
 * 
 * @author hzg

 */
public class HttpSessionUserFilter implements Filter{
	
	/** sessionId */
	private static String loginUserKey="LOGONVO";
	
	
	/**	用户threadlocal变量定义 */
	private static ThreadLocal<Object> userThreadLocal = new ThreadLocal<Object>();
	
	private FilterConfig filterConfig = null;
	
	/**
	 * 初始化，读取参数	
	 */
	@Override
	public void init(FilterConfig config) throws ServletException {
		this.filterConfig = config;
	}
	
	/**
	 * 从进程变量中获取当前用户	
	 * @since:2009-3-27下午06:12:26
	 * @return
	 */
	public static Object getLoginUser(){
		return userThreadLocal.get();
	}
	
	/**
	 * 将当前用户加入到httpsession中，同时更新进程变量的值
	 * @since:2009-3-27下午05:58:54
	 * @param request
	 * @param user
	 */
	public static void setLoginUserToHttpSession(HttpServletRequest request, Object user){
		request.getSession().setAttribute(loginUserKey, user);
		userThreadLocal.set(user);
	}
	
	/**
	 * 删除httpsession中的当前用户，同时删除进程变量的值	
	 * @since:2009-3-27下午06:12:50
	 * @param request
	 * @param user
	 */
	public static void removeLoginUserFromHttpSession(HttpServletRequest request, Object user){
		request.getSession().removeAttribute(loginUserKey);
		userThreadLocal.remove();
	}

	/**
	 * 如果是已经登录或者不过滤的url，就通过<br>
	 * 否则，跳转到错误页面，提示需要登录
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req=(HttpServletRequest)request;
		HttpServletResponse rep=(HttpServletResponse)response;
		
		/**将当前用户加到进程局部变量中 */
		userThreadLocal.set(req.getSession().getAttribute(loginUserKey));		
		String excludingPath = filterConfig.getInitParameter("excludingPath");
		String path = req.getServletPath();
		boolean excluded = false;
		/** 不作任何过滤 */
		if (excludingPath == null || excludingPath.length() == 0 || excludingPath.indexOf(path) >= 0) {
			excluded=true;
		}
		/*CSS，JS，图片等请求也不需要过滤 by hzg*/
		if(path.indexOf(".css") >=0 || path.indexOf(".js") >= 0 
				|| path.indexOf(".jpg") >= 0 || path.indexOf(".gif") >=0 
				|| path.indexOf(".png") >=0 || path.indexOf(".jar")>=0){
			excluded=true;
		}
		if(req.getSession().getAttribute(loginUserKey)==null && !excluded){
			String errorPage = filterConfig.getInitParameter("errorPage");
			/** 非法请求 */
			rep.setContentType("text/html;charset=utf-8");
			rep.addHeader("Cache-Control", "no-store, no-cache, must-revalidate");   
			rep.addHeader("Cache-Control", "post-check=0, pre-check=0");  
			rep.setHeader("Pragma", "No-cache");				
			rep.setDateHeader("Expires", 0);
			rep.sendRedirect(req.getContextPath()+errorPage);
		}else{
			/** 请求的页面是登录表单或者是登录请求或者已经登录,通过本过滤器 */
			chain.doFilter(request, response);
		}
		/** 将用户从进程变量中移除 */
		userThreadLocal.remove();
	}
	

	@Override
	public void destroy() {userThreadLocal=null;}

}
