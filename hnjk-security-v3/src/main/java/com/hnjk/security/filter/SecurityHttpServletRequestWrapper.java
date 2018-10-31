package com.hnjk.security.filter;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContext;
import org.springframework.security.context.SecurityContextHolder;

/**
 *  <code>SecurityHttpServletRequestWrapper</code><p>
 * 自定义Request Wrapper,赋予request用户认证信息.<p>
 * @author：广东学苑教育发展有限公司.
 * @since： 2011-8-3 下午04:17:15
 * @see 
 * @version 1.0
 */
public class SecurityHttpServletRequestWrapper extends HttpServletRequestWrapper {

	public SecurityHttpServletRequestWrapper(HttpServletRequest request) {
		super(request);
		
	}

	
	@Override
	public String getRemoteUser() {
		Authentication authentication = getAuthentication();		
		if(null != authentication){
			return authentication.getName();
		}		
		return null;
	}

	@Override
	public Principal getUserPrincipal() {
		Authentication authentication = getAuthentication();
		if(null != authentication){
			return authentication;
		}
		return null;
	}
	
	


	@Override
	public boolean isUserInRole(String role) {
		return isGranted(role);
	}

	private Authentication getAuthentication() {
		if(SecurityContextHolder.getContext() != null && SecurityContextHolder.getContext() instanceof SecurityContext){
			return SecurityContextHolder.getContext().getAuthentication();
		}
		return null;
		
	}

	private boolean isGranted(String role) { 
		Authentication auth = getAuthentication(); 
		if (auth == null) {
			return false;
		}
		for (int i=0; i < auth.getAuthorities().length; i++) { 
			if (role.equals(auth.getAuthorities()[i].getAuthority())) {
				return true;
			}
			} 
		return false; 
		} 
	 
}
