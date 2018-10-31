package com.hnjk.security.taglib;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.GrantedAuthority;

import com.hnjk.core.foundation.cache.EhCacheManager;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.support.context.Constants;
import com.hnjk.extend.taglib.BaseTagSupport;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.cache.CacheSecManager;
import com.hnjk.security.model.Resource;
import com.hnjk.security.model.User;

/**
 * 菜单点输出标签
 * <code>ResourceTag</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： Jun 8, 2010 2:36:25 PM
 * @see 
 * @version 1.0
 */
public class ResourceTag extends BaseTagSupport{
	private static final long serialVersionUID = 2340288793427770419L;

	private JspWriter out;
	
	private String parentCode;
	private String pageType;
	
	@Override
	public int doStartTag() throws JspException {
		out = pageContext.getOut();
		try {
			out.write("<div class='panelBar'>\r\n");
			out.write("<ul class='toolBar'>\r\n");
			out.write(this.buildHTML());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return super.doStartTag();
	}
	

	@Override
	public int doEndTag() throws JspException {
		try {
			out.write("</ul>\r\n");
			out.write("</div>\r\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return super.doEndTag();
	}

	
	private String buildHTML() throws JspException {
		List<String> pageTypeList = Arrays.asList(this.pageType.toLowerCase().split(","));
		StringBuffer sb = new StringBuffer();
		
		//Map<String, Object> userMap = UserDetailsServiceImpl.getUserMap();
		//if(null != userMap){
			// 用户有权限的id
		User user = SpringSecurityHelper.getCurrentUser();
			String rightIds = user.getUserRightsIds();
			if(null == rightIds){
				logger.error("获取当前用户权限集合失败!");
				return "";
			}
			
			
			List<Resource> list = CacheSecManager.getChildren(this.parentCode);
			int i=1, j = 0;
			if(null != list) {
				Map<String, Set<String>> resRoleMap = (Map<String, Set<String>>)EhCacheManager.getCache(CacheSecManager.CACHE_SEC_ROLE_RESOURCE)
														.get(CacheSecManager.CACHE_SEC_ROLE_RESOURCE);
				int length = 0;
				for(Resource res : list) {
					// 有权限操作该资源得角色编码
					Set<String> authRoles = resRoleMap.get(res.getResourcePath());					  
					if(null == authRoles  || 
						Constants.BOOLEAN_TRUE == res.getIsDeleted() || 
						(StringUtils.isEmpty(res.getPageType()) || 
//						!this.pageType.equalsIgnoreCase(res.getPageType())) ||
						!pageTypeList.contains(res.getPageType().toLowerCase())) ||
						rightIds.indexOf(res.getResourceid())<0) {
						continue;
					}
					// 当前用户所拥有得角色编码
					for(GrantedAuthority ag : SpringSecurityHelper.getCurrentUser().getAuthorities()) {
						
						//if(authRoles.indexOf(ag.getAuthority())>=0) {
						if(authRoles.contains(ag.getAuthority())){
							//if (j != 0 && j % 12 == 0) {//换行
							length += res.getResourceName().replaceAll("\\w", "").length()+4;
							if(length>(70+12*4)){
								length = 0;
								sb.append("</ul></div><div class='panelBar'><ul class='toolBar'>");
							}
							sb.append("<li><a  title='"+ExStringUtils.defaultIfEmpty(res.getResourceDescript(), "")+"' class='").append(res.getStyle()).append("'");
							if(!StringUtils.isEmpty(res.getJsFunction())) {
								sb.append(" href='#' onclick=\"").append(res.getJsFunction()).append("\"");
							}else {
								sb.append(" href='").append((res.getResourcePath().indexOf("http:")>=0 ? res.getResourcePath() : getBaseUrl().concat(res.getResourcePath()))).append("'");
							}
							sb.append("><span>").append(res.getResourceName()).append("</span></a></li>");
							if(i==3) {
								sb.append("<li class=\"line\">line</li>");
							}
							j++;
							break;
						}
					}
					i++;
				}			
			}		
		//}
	
		return sb.toString();
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
}
