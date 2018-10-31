package com.hnjk.security.filter;

import java.io.IOException;
import java.io.Writer;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.Authentication;
import org.springframework.security.AuthenticationException;
import org.springframework.security.BadCredentialsException;
import org.springframework.security.ui.webapp.AuthenticationProcessingFilter;

import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.HttpHeaderUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;
import com.hnjk.security.service.IUserService;
import com.hnjk.security.verifyimage.CaptchaServiceSingleton;

/**
 * 用户登录扩展过滤器.<code>UserLoginFilter</code><p>;
 * 用来处理登录过程中的事项，如验证码、载入用户安全会话等，主要是扩展spring security中不能满足的情况.
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2009-11-13 下午04:24:33
 * @see org.springframework.security.ui.webapp.AuthenticationProcessingFilter
 * @version 1.0
 */
@Deprecated
public class ExAuthenticationProcessingFilter_old extends AuthenticationProcessingFilter{		
	
	private Logger userLonginLog =LoggerFactory.getLogger("userLoginFile");
	
	@Autowired
	@Qualifier("userService")
	private IUserService userService;
	
	@Override
	protected void onPreAuthentication(HttpServletRequest request,HttpServletResponse response) throws AuthenticationException,	IOException {
		 String defaultTargetUrl = request.getParameter("defaultTargetUrl");//自定义默认跳转
		  if(ExStringUtils.isNotBlank(defaultTargetUrl)){
			  this.setDefaultTargetUrl(defaultTargetUrl);
		  }
		  logger.info("defaulturl:"+getDefaultTargetUrl());
		  
		  String authenticationFailureUrl = request.getParameter("authenticationFailureUrl");
		  if(authenticationFailureUrl != null && authenticationFailureUrl != ""){
			  this.setAuthenticationFailureUrl(authenticationFailureUrl);//自定义错误跳转url
		  }	
		super.onPreAuthentication(request, response);
	}


	/*
	 * 登录成功后的处理扩展	
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void onSuccessfulAuthentication(HttpServletRequest servletRequest, HttpServletResponse servletResponse, Authentication authentication) throws IOException {	
		//更新用户的登录信息		
		String fromNet = ExStringUtils.trimToEmpty(servletRequest.getParameter("fromNet"));		
		try {		
			HttpSession httpSession = servletRequest.getSession();
			ServletContext application = httpSession.getServletContext();
			User user = SpringSecurityHelper.getCurrentUser();
//			Map<String, String> userNameMap = (Map<String, String>)application.getAttribute("userNameMap");
			Map<String, HttpSession> userSessoinMap = (Map<String, HttpSession>)application.getAttribute("userSessionMap");
            if(userSessoinMap == null ){
            	userSessoinMap = new HashMap<String, HttpSession>();
            }
            // 获取登录方式
            
            // 将之前的session销毁
            if(userSessoinMap.containsKey(user.getUsername())){
            	HttpSession session_old = userSessoinMap.get(user.getUsername());
            	if(session_old!=null && !session_old.isNew()){
            		session_old.invalidate();
            	}
            }
            
            if(!userSessoinMap.containsKey(user.getUsername())){
            	userSessoinMap.put(user.getUsername(), httpSession);
            	application.setAttribute("userSessionMap", userSessoinMap);
            	logger.info("user:"+user.getUsername()+" logined  at {"+ExDateUtils.getCurrentDateTimeStr()+"}");	
            	int totalLonginNum = application.getAttribute("totalLonginNum")==null?1:((Integer)application.getAttribute("totalLonginNum")+1);
            	application.setAttribute("totalLonginNum", totalLonginNum);
            	httpSession.setAttribute("loginLongUser", user);
            	userLonginLog.info("login:"+ExDateUtils.getCurrentDateTimeStr()+" 目前在线总人数： "+ totalLonginNum);
            }
			
			user.setFromNet(fromNet);
			user.setLoginIp(HttpHeaderUtils.getIpAddr(servletRequest));
			user.setLastLoginTime(new Date());
			user.setStartTimeTemp(new Date());
			userService.saveOrUpdate(user);
			userService.flush();
		} catch (Exception e) {
			logger.error("更新用户登录信息出错：{}",e.fillInStackTrace());
		}
//		logger.info("user:"+authentication.getName()+" logined at {"+ExDateUtils.getToday().getTime()+"}");	
		
		//清除用户登录次数
		servletRequest.getSession(false).removeAttribute("loginNum");
		super.onSuccessfulAuthentication(servletRequest, servletResponse, authentication);
		
	}
	

	/*
	 * 登录失败后处理扩展	
	 */
	@Override
	protected void onUnsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
		
		  String byAjax = ExStringUtils.trimToEmpty(request.getParameter("byAjax"));
		  if("ajax".equals(byAjax)){
			  try {
				  HttpServletResponseWrapper responseWrapper = new HttpServletResponseWrapper(response);
				  responseWrapper.setContentType("application/json;charset=UTF-8");
				  Writer out = responseWrapper.getWriter();
				  Map<String, Object> map = new HashMap<String, Object>();
				  map.put("statusCode", 300);
				  map.put("message", "登录失败：<br/>"+exception.getMessage());
				  out.write(JsonUtils.mapToJson(map));	
				 
				  out.close();				 
			} catch (Exception e) {
				logger.warn("校验用户登录发现异常...");
			}
		  }
		 		 
		 super.onUnsuccessfulAuthentication(request, response, exception);
		
	}

	/*
	 * 登录前处理扩展，比如验证码处理等	
	 */
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request) throws AuthenticationException {
		logger.info("user login before check...");
		int loginNum = 1;
		HttpSession session = request.getSession(true);
		
		//用户尝试登录次数超过3次，则需要校验码
		if(null != session.getAttribute("loginNum")){
			loginNum = (Integer)session.getAttribute("loginNum")+1;
		}
	
		session.setAttribute("loginNum",loginNum );
		
		//处理验证码
		String checkcode = ExStringUtils.trimToEmpty(request.getParameter("j_checkcode"));
		if(loginNum>4 && !isValidateCode(session,checkcode)){//如果验证码错误			
			throw new BadCredentialsException("验证码错误");
			
		}	
		return super.attemptAuthentication(request);
	}

	
	/*
	 * 校验验证码是否正确
	 */
	private boolean isValidateCode(HttpSession  session,String validateCode) {		
		return CaptchaServiceSingleton.getInstance().validateCaptchaResponse(validateCode.trim(), session);  		 
	}
	
}
