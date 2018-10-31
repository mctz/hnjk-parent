package com.hnjk.security.filter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.Authentication;
import org.springframework.security.AuthenticationCredentialsNotFoundException;
import org.springframework.security.AuthenticationException;
import org.springframework.security.ui.switchuser.SwitchUserProcessingFilter;

import com.hnjk.core.foundation.utils.GUIDUtils;
import com.hnjk.core.foundation.utils.HttpHeaderUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;
import com.hnjk.security.service.impl.UserDetailsServiceImpl;

/**
 * 
 * <code>ExSwitchUserProcessingFilter</code><p>
 * 用户切换过滤器扩展.
 * @author：广东学苑教育发展有限公司.
 * @since： 2011-7-21 下午05:54:56
 * @see 
 * @version 1.0
 */
public class ExSwitchUserProcessingFilter extends SwitchUserProcessingFilter{
	
	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;
	
	@Override
	protected Authentication attemptExitUser(HttpServletRequest request)throws AuthenticationCredentialsNotFoundException {		
		User currentUser = SpringSecurityHelper.getCurrentUser();
		Authentication original = super.attemptExitUser(request);       
        if(null != original){
        	User user = (User)original.getPrincipal();        
        	if(null != currentUser) {
				user.setFromNet(currentUser.getFromNet());
			}
        	UserDetailsServiceImpl.loadUserAuthorities(user);
        }
        String sql = "insert into edu_sys_operatelogs (resourceid,ipaddress,userid,username,modules,operationtype,operationcontent,exportflag,recordtime,isdeleted,version) values(:resourceid,:ipaddress,:userId,:userName,:modules,:operationType,:operationContent,:exportFlag,:recordTime,:isDeleted,:version)";
		Map<String, Object> parameters = new HashMap<String, Object>();
		GUIDUtils.init();
		parameters.put("resourceid", GUIDUtils.buildMd5GUID(false));
		parameters.put("ipaddress",HttpHeaderUtils.getIpAddr(request));
		parameters.put("userId", currentUser.getResourceid());
		parameters.put("userName",currentUser.getUsername());
		parameters.put("modules","8");
		parameters.put("operationType","SWITCH");
		parameters.put("operationContent","当前切换用户："+currentUser.getUsername()+" 切换回原用户 ："+original.getName());
		parameters.put("exportFlag","0");
		parameters.put("recordTime",new Date());
		parameters.put("isDeleted",0);
		parameters.put("version",0L);
		baseSupportJdbcDao.getBaseJdbcTemplate().getJdbcTemplate().update(sql, parameters);
		return original;
	}


	@Override
	protected Authentication attemptSwitchUser(HttpServletRequest request)	throws AuthenticationException {
		User currentUser = SpringSecurityHelper.getCurrentUser();
		Authentication targetUser = super.attemptSwitchUser(request); 
		if(null != currentUser){
			((User)targetUser.getPrincipal()).setFromNet(currentUser.getFromNet());
		}
		String sql = "insert into edu_sys_operatelogs (resourceid,ipaddress,userid,username,modules,operationtype,operationcontent,exportflag,recordtime,isdeleted,version) values(:resourceid,:ipaddress,:userId,:userName,:modules,:operationType,:operationContent,:exportFlag,:recordTime,:isDeleted,:version)";
		Map<String, Object> parameters = new HashMap<String, Object>();
		GUIDUtils.init();
		parameters.put("resourceid", GUIDUtils.buildMd5GUID(false));
		parameters.put("ipaddress",HttpHeaderUtils.getIpAddr(request));
		parameters.put("userId", currentUser.getResourceid());
		parameters.put("userName",currentUser.getUsername());
		parameters.put("modules","8");
		parameters.put("operationType","SWITCH");
		parameters.put("operationContent","当前用户："+currentUser.getUsername()+" 切换用户至用户 ："+targetUser.getName());
		parameters.put("exportFlag","0");
		parameters.put("recordTime",new Date());
		parameters.put("isDeleted",0);
		parameters.put("version",0L);
		baseSupportJdbcDao.getBaseJdbcTemplate().getJdbcTemplate().update(sql, parameters);
		
		return targetUser;
	}

	
	
	
}
