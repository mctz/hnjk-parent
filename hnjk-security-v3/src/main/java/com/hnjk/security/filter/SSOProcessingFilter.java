package com.hnjk.security.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.Authentication;
import org.springframework.security.AuthenticationException;
import org.springframework.security.SpringSecurityMessageSource;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.event.authentication.AuthenticationSwitchUserEvent;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;
import org.springframework.security.ui.AuthenticationDetailsSource;
import org.springframework.security.ui.FilterChainOrder;
import org.springframework.security.ui.SpringSecurityFilter;
import org.springframework.security.ui.WebAuthenticationDetailsSource;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.security.userdetails.UserDetailsChecker;
import org.springframework.security.userdetails.checker.AccountStatusUserDetailsChecker;
import org.springframework.security.util.RedirectUtils;
import org.springframework.util.Assert;

import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;
import com.hnjk.security.service.IUserService;
import com.hnjk.security.service.impl.UserDetailsServiceImpl;
import com.hnjk.security.sso.SSOServerHelper;
import com.hnjk.security.sso.client.SSOServiceException;
import com.hnjk.security.sso.client.WebTicket;

/**
 * 单点登录过滤器.
 * <code>SSOProcessingFilter</code><p>
 * 
 * @author：广东学苑教育发展有限公司.
 * @since： 2011-8-26 下午04:20:02
 * @see 
 * @version 1.0
 */
public class SSOProcessingFilter  extends SpringSecurityFilter implements InitializingBean,ApplicationEventPublisherAware, MessageSourceAware {

	public static final String SPRING_SECURITY_SSO_TICKET = "j_ticket";//票据参数名
	
	protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();
	private AuthenticationDetailsSource authenticationDetailsSource = new WebAuthenticationDetailsSource();
	private UserDetailsChecker userDetailsChecker = new AccountStatusUserDetailsChecker();
	private ApplicationEventPublisher eventPublisher;
	private String ssoUrl = "/j_spring_security_sso";//请求URL	
	//private UserDetailsService userDetailsService;
	private IUserService userService;
	private String targetUrl;
	private String ssoFailureUrl;
	private boolean useRelativeContext;
	private boolean disaysn = false;//是否异步模式
	
	
	@Override
	public int getOrder() {		
		return FilterChainOrder.SWITCH_USER_FILTER + 100 * 1;
	}

	
	@Override
	public void setMessageSource(MessageSource messageSource) {
		 this.messages = new MessageSourceAccessor(messageSource);		
	}


	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		 this.eventPublisher = eventPublisher;		
	}

	
	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(ssoUrl,"sso url must be specified");
		Assert.notNull(targetUrl,"targetUrl must be specified");
		Assert.notNull(userService,"userDetailsService must be specified");
		Assert.notNull(messages, "A message source must be set");
	}

	
	@Override
	protected void doFilterHttp(HttpServletRequest request,	HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
		// 检查是否为SSO请求URL
        if (requiresSSO(request)) {           
        	Map<String, Object> outMap = new HashMap<String, Object>();
        	String jsoncallback = ExStringUtils.trimToEmpty(request.getParameter("jsoncallback"));
        	response.setContentType(";charset=UTF-8");
            try {
                Authentication targetUser = authenticationUser(request);
                User user = (User)targetUser.getPrincipal();
                if(null != user) {
					UserDetailsServiceImpl.loadUserAuthorities(user);
				}

                SpringSecurityHelper.saveUserDetailsToContext(user, request); 
                
                if(isDisaysn()){//若为异步模式，则输出Json字符串，否则跳转页面
                	outMap.put("statusCode",200);
                	outMap.put("msg", "认证成功");
                	response.getWriter().write(jsoncallback+"("+JsonUtils.mapToJson(outMap)+")");
        			
                }else{
                	 RedirectUtils.sendRedirect(request, response, targetUrl, useRelativeContext);
                }
               
                
            } catch (Exception e) {
            	logger.error("SSO  logined error:"+e.fillInStackTrace());
            	if(isDisaysn()){
            		if(e instanceof SSOServiceException){
            			outMap.put("statusCode",503);
                    	outMap.put("msg", e.getMessage());
            		}else{
            			outMap.put("statusCode",500);
            			outMap.put("msg", "认证失败");
            		}
            		
                	response.getWriter().write(jsoncallback+"("+JsonUtils.mapToJson(outMap)+")");
            	}else{
            		 if(null != ssoFailureUrl){
                     	RedirectUtils.sendRedirect(request, response, ssoFailureUrl, useRelativeContext);
                     }
            	}
               
            }finally{
            	response.getWriter().flush();
            	response.getWriter().close();
            }

            return;
        } 

        chain.doFilter(request, response);
		
	}
	
	 protected Authentication authenticationUser(HttpServletRequest request) throws AuthenticationException,SSOServiceException {
	        UsernamePasswordAuthenticationToken targetUserRequest = null;

	        String ticket = request.getParameter(SPRING_SECURITY_SSO_TICKET);
	        
	        if(ExStringUtils.isNotEmpty(ticket)){
	        	//解析ticket	        
	        	WebTicket webTicket	= SSOServerHelper.parseTicket(ticket);		
	        	//设置是否异步模式
	        	if(webTicket.getAsynMode().equals(WebTicket.NOASYN_MODE)){
	        		setDisaysn(true);
	        	}else{
	        		setDisaysn(false);
	        	}
	        	
	        	//验证	        	
	        	List<User> list = userService.findByHql(" from "+User.class.getSimpleName()+ " where isDeleted = ?" +
	        			" and username = ? " +
	        			" and orgUnit.unitCode = ? " +
	        			//" and userType = ? " +
	        			" and enabled = ? ", 
	        						0,webTicket.getUsername()
	        						,webTicket.getUnitCode()
	        		//				,"student"
	        						,true
	        	);
	        	
	        	if(null == list){//如果根据用户名查找为空，则改为用昵称
	        		list = userService.findByHql(" from "+User.class.getSimpleName()+ " where isDeleted = ?" +
		        			" and customUsername = ? " +
		        			" and orgUnit.unitCode = ? " +
		        	//		" and userType = ? " +
		        			" and enabled = ? ", 
		        						0,webTicket.getUsername()
		        						,webTicket.getUnitCode()
		        		//				,"student"
		        						,true
		        	);
	        	}
	        	
	        	if(null != list && list.size() == 1){
	        		  UserDetails targetUser = list.get(0);
	        		  userDetailsChecker.check(targetUser);	  	 	        
	  	 	        // 创建SSO用户
	  	 	        targetUserRequest = createSSOUserToken(request, targetUser);

	  	 	        logger.debug("创建SSO用户[ "+targetUser+" ]");

	  	 	        // 发布到事件监听
	  	 	        if (this.eventPublisher != null) {
	  	 	            eventPublisher.publishEvent(new AuthenticationSwitchUserEvent(
	  	 	                    SecurityContextHolder.getContext().getAuthentication(), targetUser));
	  	 	        }
	  	 	        //如果传入了跳转链接，则设置目标跳转链接
	  	 	        if(ExStringUtils.isNotBlank(webTicket.getForwardUrl()) && !"null".equals(webTicket.getForwardUrl())){
	  	 	        	setTargetUrl(webTicket.getForwardUrl());
	  	 	        }	
	        	}else{
	        		//没有这个用户
	        		throw new SSOServiceException("登录出错：用户 ["+webTicket.getUsername()+"] - 组织 [ "+webTicket.getUnitCode()+" ]不存在或已禁止登陆!");
	        	}
	 	      
	        }
	        
	       

	        return targetUserRequest;
	    }
	
	 private UsernamePasswordAuthenticationToken createSSOUserToken(HttpServletRequest request,  UserDetails targetUser) {

	        UsernamePasswordAuthenticationToken targetUserRequest;

	        //Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();	       
  
	        // create the new authentication token
	        targetUserRequest = new UsernamePasswordAuthenticationToken(targetUser, targetUser.getPassword());

	        // set details
	        targetUserRequest.setDetails(authenticationDetailsSource.buildDetails(request));

	        return targetUserRequest;
	    }
	 
	  protected boolean requiresSSO(HttpServletRequest request) {
	        String uri = stripUri(request);
	        return uri.endsWith(request.getContextPath() + ssoUrl);
	    }

	  private static String stripUri(HttpServletRequest request) {
	        String uri = request.getRequestURI();
	        int idx = uri.indexOf(';');

	        if (idx > 0) {
	            uri = uri.substring(0, idx);
	        }

	        return uri;
	    }
	  
	  public void setUseRelativeContext(boolean useRelativeContext) {
	        this.useRelativeContext = useRelativeContext;
	    }


	/**
	 * @param ssoFailureUrl the ssoFailureUrl to set
	 */
	public void setSsoFailureUrl(String ssoFailureUrl) {
		this.ssoFailureUrl = ssoFailureUrl;
	}
		
	/**
	 * @param userService the userService to set
	 */
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}


	public void setAuthenticationDetailsSource(AuthenticationDetailsSource authenticationDetailsSource) {
	       Assert.notNull(authenticationDetailsSource, "AuthenticationDetailsSource required");
	       this.authenticationDetailsSource = authenticationDetailsSource;
	  }
	

	/**
	 * @param eventPublisher the eventPublisher to set
	 */
	public void setEventPublisher(ApplicationEventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}


	/**
	 * @param targetUrl the targetUrl to set
	 */
	public void setTargetUrl(String targetUrl) {
		this.targetUrl = targetUrl;
	}


	/**
	 * @param disaysn the disaysn to set
	 */
	public void setDisaysn(boolean disaysn) {
		this.disaysn = disaysn;
	}


	/**
	 * @return the disaysn
	 */
	public boolean isDisaysn() {
		return disaysn;
	}
		  
}
