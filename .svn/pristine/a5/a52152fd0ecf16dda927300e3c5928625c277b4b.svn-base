package com.hnjk.security.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.ui.FilterChainOrder;
import org.springframework.security.ui.SpringSecurityFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *  <code>SecurityHttpServletRequestFilter</code><p>
 * 认证用户过滤器.这个过滤器主要用来设置当前用户到request中，以供tomcat访问日志能读取到当前操作人账号.<p>
 * @author：广东学苑教育发展有限公司.
 * @since： 2011-8-3 下午04:14:37
 * @see 
 * @version 1.0
 */
public class SecurityHttpServletRequestFilter extends SpringSecurityFilter {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	
	@Override
	public int getOrder() {		
		return FilterChainOrder.ANONYMOUS_FILTER;
	}

	
	@Override
	protected void doFilterHttp(HttpServletRequest servletRequest, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		if (!(request instanceof SecurityHttpServletRequestWrapper)) {
				request = new SecurityHttpServletRequestWrapper(request);
		} 	
		logger.debug("current user : {}",request.getRemoteUser());
		request.setAttribute("_currentuser", request.getRemoteUser());
		chain.doFilter(request, response);
	}
	
}
