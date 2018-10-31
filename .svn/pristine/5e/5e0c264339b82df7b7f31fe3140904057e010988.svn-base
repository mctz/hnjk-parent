package com.hnjk.core.support.context;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hnjk.core.foundation.utils.ExStringUtils;

/**
 * 重载spring OpenSessionInViewFilter. <p>
 * 缩窄OpenSessionInViewFilter的过滤范围,请求css、js、图片等静态内容时不创建Session连接.<p>
 * 在web.xml中可配置excludeSuffixs参数,多个后缀名以','分割.
 * @author： 广东学苑教育发展有限公司.<p>
 * @since： 2009-4-17上午11:59:37
 * @see org.springframework.orm.hibernate3.support.OpenSessionInViewFilter
 * @version 1.0
 */
public class OpenSessionInViewFilter extends org.springframework.orm.hibernate3.support.OpenSessionInViewFilter{
	/**不必过滤的后缀param-name常量 与web.xml中的配置同名*/
	private static final String EXCLUDE_SUFFIXS_NAME = "excludeSuffixs";
	/**默认不必过滤的后缀*/
	private static final String[] DEFAULT_EXCLUDE_SUFFIXS = { ".js", ".css", ".jpg", ".gif" ,".png",".htm"};
	/**默认过滤URL后缀*/
	private static final String[] DEFAULT_INCLUDE_SUFFIXS = { ".action", ".do",".html" };
	//排除过滤的资源后缀
	private static String[] excludeSuffixs = DEFAULT_EXCLUDE_SUFFIXS;

	/**
	 * 重载getSession()方法，为了解决主子表级联删除的问题.
	 */
//	@Override
//	protected Session getSession(SessionFactory sessionFactory) throws DataAccessResourceFailureException {   
//	        Session session = SessionFactoryUtils.getSession(sessionFactory, true);   
//	         session.setFlushMode(FlushMode.AUTO);  	        
//	         return session;   
//	 }   
	
	/**
	 * 重载closeSession()方法，将session flush
	 */
//	@Override	  
//	 protected void closeSession(Session session, SessionFactory factory) {   
//	        // session.flush();   
//	         super.closeSession(session, factory);   
//	 }   


	/**
	 * 重载过滤控制函数,忽略特定后缀名的请求.
	 * @param request web请求
	 */
	@Override
	protected boolean shouldNotFilter(final HttpServletRequest request) throws ServletException {
		String path = request.getServletPath();

		//对必须INCLUDE的URL直接返回false，省略后面对EXCLUDE后缀名的判断.
		for (String suffix : DEFAULT_INCLUDE_SUFFIXS) {
			if (path.endsWith(suffix)) {
				return false;
			}
		}

		for (String suffix : excludeSuffixs) {
			if (path.endsWith(suffix.toLowerCase())) {
				return true;
			}
		}

		return false;
	}

	//解决IE6下跨域session问题
	@Override
	protected void doFilterInternal(HttpServletRequest request,			HttpServletResponse response, FilterChain filterChain)			throws ServletException, IOException {
		response.setHeader("P3P", "CP=CAO PSA OUR");
		super.doFilterInternal(request, response, filterChain);
	}



	/**
	 * 初始化excludeSuffixs参数.
	 */
	@Override
	protected void initFilterBean() throws ServletException {
		//从web.xml中获取配置
		String excludeSuffixStr = getFilterConfig().getInitParameter(EXCLUDE_SUFFIXS_NAME);

		if (ExStringUtils.isNotBlank(excludeSuffixStr)) {
			excludeSuffixs = excludeSuffixStr.split(",");
			//处理匹配字符串为".后缀名"
			for (int i = 0; i < excludeSuffixs.length; i++) {
				excludeSuffixs[i] = "." + excludeSuffixs[i];
			}
		} else {
			excludeSuffixs = DEFAULT_EXCLUDE_SUFFIXS;
		}
	}

	
}
