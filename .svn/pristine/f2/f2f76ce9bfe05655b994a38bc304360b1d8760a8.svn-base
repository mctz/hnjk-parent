package com.hnjk.security.filter;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.event.authentication.AuthenticationSuccessEvent;
import org.springframework.security.ui.session.HttpSessionCreatedEvent;
import org.springframework.security.userdetails.UserDetails;

import com.hnjk.core.foundation.utils.ExDateUtils;

/**
 * 自定义登录成功处理过滤器
 * @author Zik, 广东学苑教育发展有限公司.
 *
 */
public class CustomLoginFilter implements ApplicationListener {
	
	private Logger userLonginLog =LoggerFactory.getLogger("userLoginFile");
	private Logger logger = LoggerFactory.getLogger(CustomLoginFilter.class);
	private static ServletContext application = null;
	private static Map<String, String> userNameMap = null;

	@SuppressWarnings("unchecked")
	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		if(event  instanceof HttpSessionCreatedEvent){
			HttpSessionCreatedEvent sessionCreatedEvent = (HttpSessionCreatedEvent) event;  
			application = sessionCreatedEvent.getSession().getServletContext();
		}
		
		if (event instanceof AuthenticationSuccessEvent) {  
            AuthenticationSuccessEvent authEvent = (AuthenticationSuccessEvent) event;  
            UserDetails user = (UserDetails) authEvent.getAuthentication().getPrincipal(); 
            
            userNameMap = (Map<String, String>)application.getAttribute("userNameMap");
            if(userNameMap == null ){
            	userNameMap = new HashMap<String, String>();
            }
            if(!userNameMap.containsKey(user.getUsername())){
            	userNameMap.put(user.getUsername(), user.getUsername());
            	application.setAttribute("userNameMap", userNameMap);
            	logger.info("user:"+user.getUsername()+" logined  at {"+ExDateUtils.getCurrentDateTimeStr()+"}");	
            	int totalLonginNum = application.getAttribute("totalLonginNum")==null?1:((Integer)application.getAttribute("totalLonginNum")+1);
            	application.setAttribute("totalLonginNum", totalLonginNum);
            	userLonginLog.info("login:"+ExDateUtils.getCurrentDateTimeStr()+" 目前在线总人数： "+ totalLonginNum);
            }
        }  

	}

}
