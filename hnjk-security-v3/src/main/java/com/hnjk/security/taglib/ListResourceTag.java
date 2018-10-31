package com.hnjk.security.taglib;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.Tag;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.GrantedAuthority;

import com.hnjk.core.foundation.cache.EhCacheManager;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.support.context.Constants;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.cache.CacheSecManager;
import com.hnjk.security.model.Resource;
import com.hnjk.security.model.User;

/**
 * 列表菜单点输出标签
 * <code>ListResourceTag</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2015-01-12
 * @see 
 * @version 1.0
 */
public class ListResourceTag extends BodyTagSupport {

	protected Logger logger = LoggerFactory.getLogger(getClass());
	private String parentCode;//父资源编码
	private String pageType;//页面类型
	private String resourceCode;//资源编码
	
	@Override
	public int doStartTag() throws JspException {
		
		User user = SpringSecurityHelper.getCurrentUser();
		String rightIds = user.getUserRightsIds();
		if(null == rightIds){
			logger.error("获取当前用户权限集合失败!");
			return Tag.SKIP_BODY;
		}
		
		List<Resource> list = CacheSecManager.getChildren(this.parentCode);
		if(null != list) {
			Map<String, Set<String>> resRoleMap = (Map<String, Set<String>>)EhCacheManager.getCache(CacheSecManager.CACHE_SEC_ROLE_RESOURCE)
													.get(CacheSecManager.CACHE_SEC_ROLE_RESOURCE);
			for(Resource res : list) {
				// 有权限操作该资源得角色编码
				Set<String> authRoles = resRoleMap.get(res.getResourcePath());					  
				if(!resourceCode.equalsIgnoreCase(res.getResourceCode()) || null == authRoles  || 
					Constants.BOOLEAN_TRUE == res.getIsDeleted() || 
					(StringUtils.isEmpty(res.getPageType()) || 
					!this.pageType.equalsIgnoreCase(res.getPageType())) ||
					rightIds.indexOf(res.getResourceid())<0) {
						continue;
				}
				// 当前用户所拥有得角色编码
//				System.out.println("authRoles==========="+authRoles);
				for(GrantedAuthority ag : SpringSecurityHelper.getCurrentUser().getAuthorities()) {
//					System.out.println("ag.getAuthority()=============="+ag.getAuthority());
					if(authRoles.contains(ag.getAuthority())){
						return Tag.EVAL_BODY_INCLUDE;
					}
				}
			}			
		}
		return Tag.SKIP_BODY;
	}
	
	@Override
	public int doEndTag() throws JspException {
		return Tag.EVAL_PAGE;
	}
	
	@Override
	public int doAfterBody() throws JspException {
		return Tag.SKIP_BODY;
	}
	
	public String getParentCode() {
		return parentCode;
	}
	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}
	public String getPageType() {
		return pageType;
	}
	public void setPageType(String pageType) {
		this.pageType = pageType;
	}

	public String getResourceCode() {
		return resourceCode;
	}

	public void setResourceCode(String resourceCode) {
		this.resourceCode = resourceCode;
	}
	
}
