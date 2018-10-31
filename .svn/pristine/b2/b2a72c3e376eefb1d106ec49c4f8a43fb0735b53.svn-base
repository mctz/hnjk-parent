package com.hnjk.security.filter;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.ConfigAttributeDefinition;
import org.springframework.security.ConfigAttributeEditor;
import org.springframework.security.intercept.web.FilterInvocation;
import org.springframework.security.intercept.web.FilterInvocationDefinitionSource;
import org.springframework.security.util.AntUrlPathMatcher;
import org.springframework.security.util.RegexUrlPathMatcher;
import org.springframework.security.util.UrlMatcher;

import com.hnjk.core.foundation.cache.EhCacheManager;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.security.cache.CacheSecManager;

/**
 * 从cache中读取保护资源及其需要的访问权限信息 .<code>DbFilterInvocationDefinitionSource</code>
 * <p>;
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2009-10-30 下午02:46:36
 * @see SecurityResourceDetails
 * @version 1.0
 */
public class DbFilterInvocationDefinitionSource implements	FilterInvocationDefinitionSource,InitializingBean {

	private UrlMatcher urlMatcher;

	private boolean useAntPath = true;

	private boolean lowercaseComparisons = true;
	
	private boolean stripQueryStringFromUrls = true;
	
	private String excludePages;//例外页面，用于一些特殊不需要校验的页面
	
	/**
	 * 匹配权限
	 */
	@Override
	public ConfigAttributeDefinition getAttributes(Object filter)	throws IllegalArgumentException {
				
		FilterInvocation filterInvocation = (FilterInvocation) filter;
		String requestUrl = filterInvocation.getRequestUrl();
		
		if (stripQueryStringFromUrls) {
			int firstQuestionMarkIndex = requestUrl.indexOf("?");

			if (firstQuestionMarkIndex != -1) {
				requestUrl = requestUrl.substring(0, firstQuestionMarkIndex);
			}
		}
		if (urlMatcher.requiresLowerCaseUrl()) {
			requestUrl = requestUrl.toLowerCase();
		}
				
		//boolean matched = urlMatcher.pathMatchesUrl(resPath, requestURI);
		//if (matched) {
		//	authorities = SecurityResourceDetails.getAuthoritysInCache(resPath);
		//	break;
		//}
		
		//判断是否为例外页
		if(ExStringUtils.isNotEmpty(excludePages)){		
			for(String url : excludePages.split("\\,")){			
				if(requestUrl.equals(url.trim().toLowerCase())){				
					return null;
				}				
			}			
		}
				
	
		//获取资源 - 角色 映射
		Map<String, Set<String>> resRoleMap = (Map<String, Set<String>>)EhCacheManager.getCache(CacheSecManager.CACHE_SEC_ROLE_RESOURCE)
												.get(CacheSecManager.CACHE_SEC_ROLE_RESOURCE);
		for (String url : resRoleMap.keySet()) {
			if (urlMatcher.pathMatchesUrl(url.toLowerCase(), requestUrl)) {//若请求路径匹配，则查看角色权限				
				ConfigAttributeEditor configAttrEditor = new ConfigAttributeEditor();				
				configAttrEditor.setAsText(Arrays.toString(resRoleMap.get(url).toArray()).replace("[", "").replace("]", ""));			
				if(null == configAttrEditor.getValue()){
					return new ConfigAttributeDefinition("ROLE");
				}
				return (ConfigAttributeDefinition) configAttrEditor.getValue();
			}
		}	
		
		return null;
	}


	@Override
	public void afterPropertiesSet() throws Exception {
		this.urlMatcher = new RegexUrlPathMatcher();
		if (useAntPath) {
			this.urlMatcher = new AntUrlPathMatcher();
		}
		if (lowercaseComparisons) {
			if (!this.useAntPath) {
				((RegexUrlPathMatcher) this.urlMatcher).setRequiresLowerCaseUrl(true);
			}
		} else if (!lowercaseComparisons) {
			if (this.useAntPath) {
				((AntUrlPathMatcher) this.urlMatcher).setRequiresLowerCaseUrl(false);
			}
		}
	}

	public void setUseAntPath(boolean useAntPath) {
		this.useAntPath = useAntPath;
	}

	public void setLowercaseComparisons(boolean lowercaseComparisons) {
		this.lowercaseComparisons = lowercaseComparisons;
	}

	@Override
	public Collection getConfigAttributeDefinitions() {
		return null;
	}

	@Override
	public boolean supports(Class clazz) {
		return true;
	}


	/**
	 * @return the excludePages
	 */
	public String getExcludePages() {
		return excludePages;
	}


	/**
	 * @param excludePages the excludePages to set
	 */
	public void setExcludePages(String excludePages) {
		this.excludePages = excludePages;
	}
	

	//public void setStripQueryStringFromUrls(boolean stripQueryStringFromUrls) {
	//	this.stripQueryStringFromUrls = stripQueryStringFromUrls;
	//}
}
