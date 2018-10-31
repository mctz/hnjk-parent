package com.hnjk.security.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.security.BadCredentialsException;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.GrantedAuthorityImpl;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.security.userdetails.UserDetailsService;
import org.springframework.security.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.support.context.Constants;
import com.hnjk.core.support.context.SpringContextHolder;
import com.hnjk.security.model.OrgUnit;
import com.hnjk.security.model.Resource;
import com.hnjk.security.model.Role;
import com.hnjk.security.model.User;
import com.hnjk.security.service.ILoginExtendService;
import com.hnjk.security.service.IOrgUnitService;
import com.hnjk.security.service.IUserService;

/**
 * 实现用户查询验证服务. <code>UserDetailsServiceImpl</code><p>;
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2009-11-12 上午10:41:58
 * @see 
 * @version 1.0
 */
@Transactional(readOnly=true)
public class UserDetailsServiceImpl  implements UserDetailsService{	
		
	@Autowired
	@Qualifier("userService")
	private IUserService userService;
	
	@Autowired
	@Qualifier("loginExtendService")
	private ILoginExtendService loginExtendService;
	
	/**
	 * 验证用户并加载用户权限
	 */
	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException, DataAccessException {
		
		User user = userService.getUserByLoginId(userName);
		if(null == user){
			throw new UsernameNotFoundException("用户："+userName + " 不存在");
		}			
		
		//欠缴费不允许登录
		if(user !=null && "student".equalsIgnoreCase(user.getUserType())){
			if(loginExtendService.isValidateLoginPay()){
				rejectLogin(user);
			}
			/*String strkey = "";
			//获取学校代码
			strkey=loginExtendService.getUnitWord();
			int intkey = Integer.parseInt(strkey);
			*//**
			 * TODO
			 * 目前只做了广大的缴费情况处理
			 *//*
			switch (intkey) {						
				case 11078:
					//当学校为广大时，只有已经缴清费用的学生允许登录
					rejectLogin(user);
					break;				
				default:
					break;
			}*/
			
		}

		loadUserAuthorities(user);	
		return user;
	}

	/**
	 * @param user
	 * @throws BadCredentialsException
	 */
	private void rejectLogin(User user) throws BadCredentialsException {
		String status = null;
		try {
			status = loginExtendService.getchargeStatusByUserId(user.getResourceid());
		} catch (ServiceException e) {
			throw new BadCredentialsException(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}

		//领导说：没有缴费记录，就代表没欠费，流程继续。所以只判断欠费的情况
		if(ExStringUtils.isNotEmpty(status)&&!"1".equals(status)){
			throw new BadCredentialsException("学费未缴清");
		}
	}
	
	//加载用户菜单
	public static void loadUserAuthorities(User user){
		StringBuffer userRightsIds = new StringBuffer();	// 所有权限id
		StringBuffer userTopMenuIds = new StringBuffer();	// 顶级菜单id
		
		List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
		for (Role role : user.getRoles()) {//遍历角色
			grantedAuthorities.add(new GrantedAuthorityImpl(role.getRoleCode()));
			for(Resource res : role.getAuthoritys()){//遍历角色授权资源
				if(userRightsIds.indexOf(res.getResourceid())<0 && res.getIsDeleted() == Constants.BOOLEAN_FALSE) {
					userRightsIds.append(res.getResourceid()).append(",");
				}
				if("menu".equals(res.getResourceType()) && res.getResourceLevel() == 1 && userTopMenuIds.indexOf(res.getResourceid())<0){
					userTopMenuIds.append(res.getResourceid()).append(",");
				}
			}
		}
		
		user.setAuthorities(grantedAuthorities.toArray(new GrantedAuthority[user.getRoles().size()]));
		//重新载入用户所属单位
		if(ExStringUtils.isNotEmpty(user.getUnitId())){
			IOrgUnitService orgUnitService = SpringContextHolder.getBean("orgUnitService");
			OrgUnit orgUnit = orgUnitService.get(user.getUnitId());
			if(null != orgUnit) {
				user.setOrgUnit(orgUnit);
			}
		}
		user.setUserTopMenuIds(userTopMenuIds.toString());
		user.setUserRightsIds(userRightsIds.toString());		
	}

	
}
