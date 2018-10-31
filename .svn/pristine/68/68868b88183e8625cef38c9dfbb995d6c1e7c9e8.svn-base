package com.hnjk.security.taglib;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.jsp.JspException;
import org.apache.commons.lang.StringUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.extend.taglib.BaseTagSupport;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.cache.CacheSecManager;
import com.hnjk.security.model.Resource;
import com.hnjk.security.model.Role;
import com.hnjk.security.model.User;

/**
 * <code>CurrentUserSystemMenuTag</code>,根据用户登录名，获取用户的系统菜单<p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-1-18 下午02:11:34
 * @see 
 * @version 1.0
 */
public class CurrentUserSystemMenuTag extends BaseTagSupport{

	private static final long serialVersionUID = -4457310703441926570L;
	

	@Override
	public int doStartTag() throws JspException {
		User user = SpringSecurityHelper.getCurrentUser();
		if(ExStringUtils.isEmpty(user.getUsername())){
			throw new JspException("无法输出用户菜单，原因：非法的用户名.");
		}
		try{
			// 用户有权限的id
			//String rightIds = (String)MemeryCacheManager.getCache(CacheSecManager.CACHE_SEC_USER_RES_IDS).get(SpringSecurityHelper.getCurrentUserName());
			// 用户顶级菜单id
			//String topMenuId = (String)MemeryCacheManager.getCache(CacheSecManager.CACHE_SEC_USER_TOP_MENU_IDS).get(SpringSecurityHelper.getCurrentUserName());
			
			//Map<String, Object> userMap = UserDetailsServiceImpl.getUserMap();
			//if(null != userMap){
				String rightIds = user.getUserRightsIds();
				String topMenuId = user.getUserTopMenuIds();
				// 从缓存获取
				List<Resource> list = new ArrayList<Resource>();
				for(String mid : topMenuId.split(",")) {
					if(!StringUtils.isEmpty(mid)) {
						//list.add((Resource)MemeryCacheManager.getCache(CacheSecManager.CACHE_SEC_RESOURCE).get(mid));
						list.add(CacheSecManager.getResourceFromCache(mid));
					}
				}
				boolean useFirstName = false;
				if(user.getOrgUnit().getUnitType().indexOf("brSchool")>=0){//如果为校外学习中心人员
					List<Role> roles = new ArrayList<Role>(user.getRoles());
					for (int i = 0; i < roles.size(); i++) {
						Role role = roles.get(i);
						if("ROLE_TEACHER_DUTY".equals(role.getRoleCode()) || "ROLE_STUDENT".equals(role.getRoleCode()) || "ROLE_LINE".equals(role.getRoleCode())){
							useFirstName=true;
						}else{
							useFirstName=false;
							break;
						}
					}
				}
				// 构造树形html
				this.pageContext.getOut().append(printTree(list,rightIds,useFirstName));
			//}
			
		}catch(Exception e){
			logger.error("输出用户菜单出错:{}",e.fillInStackTrace());
		}
		return EVAL_PAGE;
	}
	
	/**
	 * 菜单树
	 * @param list
	 * @return
	 */
	private String printTree(List<Resource> list,String userRightsIds,boolean useFPName){
		StringBuffer sb = new StringBuffer();
		for (Resource resource : list) {
			
			// 没有权限  跳过
			if(userRightsIds.indexOf(resource.getResourceid())<0) {
				continue;
			}
			String resourceName = "";
			if(useFPName){
				resourceName = resource.getFirstPersonName()==null?resource.getResourceName():resource.getFirstPersonName();
			}else {
				resourceName = resource.getResourceName();
			}
			
			if(resource.getResourceLevel() == 1){
				sb.append("<div class=\"accordionHeader\"><h2><span>Folder</span>"+resourceName+"</h2></div>");
				sb.append("<div class=\"accordionContent\"><ul class=\"tree treeFolder\">");
			}
			if("menu".equals(resource.getResourceType())) {
				sb.append("<li><a ");
				if(!"#".equals(resource.getResourcePath())){
					sb.append("href='"+getBaseUrl()+ resource.getResourcePath()+"' title='"+resourceName+"' target=\"navTab\" rel='"+resource.getResourceCode()+"'");
				}
				sb.append(">"+resourceName+"</a>");
				
				List<Resource> children = CacheSecManager.getChildren(resource.getResourceid());
				
				// 递归 子集合
				if(!children.isEmpty() && children.size()>0){					
					if("menu".equals(children.get(0).getResourceType())) {
						sb.append("<ul>");					
					}
					sb.append(printTree(children,userRightsIds,useFPName));
					if("menu".equals(children.get(0).getResourceType())) {
						sb.append("</ul>");
					}
				}								
				sb.append("</li>");
			}
			if(resource.getResourceLevel() == 1){
				sb.append("</ul></div>");
			}
		}
		return sb.toString();
	}
}
