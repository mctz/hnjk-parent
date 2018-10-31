package com.opensymphony.workflow.util;

import java.util.Map;

import com.hnjk.core.support.context.SpringContextHolder;
import com.hnjk.security.model.UserAdaptor;
import com.hnjk.security.service.IUserService;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.Condition;
import com.opensymphony.workflow.WorkflowContext;


/**
 * Simple utility class that uses OSUser to determine if the caller is in
 * the required argument "group".
 *
 * @author <a href="mailto:plightbo@hotmail.com">Pat Lightbody</a>
 * 权限扩展说明,参数名是 validation
 * 目前只开放用户ID,角色ID,部门ID,配置格式如下
 * userIds:user1,user2,user3|roleIds:role1,role2|deptIds:dptid1,dptid2
 * 也可选用或者不用,但格式一定要正确
 */
public class OSUserGroupCondition implements Condition {
	
    @Override
	@SuppressWarnings("unchecked")
	public boolean passesCondition(Map transientVars, Map args, PropertySet ps) {
        WorkflowContext context = (WorkflowContext) transientVars.get("context");
        String userid = context.getCaller();
        
        Object validation = args.get("validation");
        if(validation!=null && validation.toString().trim().length()>0) {
        	String[] valids = validation.toString().split("\\|");
        	try {
	        	for(String v : valids) {
					if(validator(userid, v)) {
						return true;
					}
	        	}
        	} catch (Exception e) {
				e.printStackTrace();
			}
        	return false;
        }else {
        	return true;
        }
    }
    
    
    private static boolean validator (String userid, String valid) throws Exception{
    	//UserAdaptor user = CacheSecManager.userInfoMap.get(userid);
    	//User u = SpringSecurityHelper.getUserByUserId(userid);
    	IUserService userService = (IUserService)SpringContextHolder.getBean("userService");//获取用户服务接口
    	UserAdaptor user = userService.getUserAdaptor(userid);		
    	String[] args = valid.split(":");
    	String type = args[0];
    	String value = args[1];
    	if("userIds".equalsIgnoreCase(type)) {
    		for(String s : value.split(",")) {
    			if(s.equalsIgnoreCase(user.getId())) {
    				return true;
    			}
    		}
    	}else if("roleIds".equalsIgnoreCase(type)) {
    		for(String s : value.split(",")) {
    			for(String r : user.getRoleIdArr()) {
	    			if(s.equalsIgnoreCase(r)) {
	    				return true;
	    			}
    			}
    		}
    	}else if("deptIds".equalsIgnoreCase(type)) {
    		for(String s : value.split(",")) {
    			for(String r : user.getDeptIdArr()) {
	    			if(s.equalsIgnoreCase(r)) {
	    				return true;
	    			}
    			}
    		}
    	}else {
    		throw new Exception("验证的类型不存在");
    	}
    	return false;
    }
}
