package com.hnjk.edu.portal.taglib;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;

import com.hnjk.core.foundation.template.FreeMarkerConfig;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.edu.portal.model.Article;
import com.hnjk.extend.taglib.BaseTagSupport;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;

import freemarker.template.Template;

/**
 * 门户网页头部便签.
 * <code>IndexHeaderTag</code>
 * <p>
 * 标签样式使用freemarker作为模板，方便在线编辑.<p>
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-4-22 上午10:08:23
 * @see 
 * @version 1.0
 */
public class PortalPageTag extends BaseTagSupport{

	private static final long serialVersionUID = -5601680293377362867L;
	
	transient private List channelList;//传入栏目列表	
	
	transient private Map<String,List<Article>> articleMap;//文章列表，用Map存入文章列表
	
	transient private List bbsList;//传入论坛帖子列表
	
	transient private List linkList;//友情链接列表
	
	private String channelType;//栏目类型，如左侧(index-leftside)，头部(index-header)，右侧(index-rightside)，底部(index-footer)等
	
	private String schoolId;//校外学习中心ID
		
	@Override
	@SuppressWarnings("unchecked")
	public int doStartTag() throws JspException {		
		try {
			Map root = new HashMap();		
			Template template = FreeMarkerConfig.getDefaultTemplate("portal"+File.separator+channelType+".ftl");		
			if(null !=channelList && !channelList.isEmpty()){//栏目列表
				root.put("navheaderList", channelList);
			}
			
			if(null != articleMap && !articleMap.isEmpty()){//设置文章列表
				if(null != articleMap.get("noticeArticleList") && articleMap.get("noticeArticleList").size()>0){//通知列表
					root.put("noticeArticleList", articleMap.get("noticeArticleList"));					
				}				
				if(null != articleMap.get("newsArticleList") &&articleMap.get("newsArticleList").size()>0){//新闻列表
					root.put("newsArticleList", articleMap.get("newsArticleList"));
				}				
				if(null != articleMap.get("dynamicArticleList") &&articleMap.get("dynamicArticleList").size()>0){//动态列表
					root.put("dynamicArticleList", articleMap.get("dynamicArticleList"));					
				}				
				if(null != articleMap.get("schoolRecruitList") &&articleMap.get("schoolRecruitList").size()>0){//招生列表
					root.put("schoolRecruitList", articleMap.get("schoolRecruitList"));	
					root.put("schoolRecruitId", articleMap.get("schoolRecruitList").get(0).getChannel().getResourceid());
				}
				
				root.put("noticeChannelId", "402880ed2838b0ec012838bc1afe0003");//更多通知链接
				root.put("dynamicChannelId", "402880ed2838b0ec012838bcb4210004");//更多动态新闻链接
				root.put("newsChannelId", "402880ed2838b0ec012838bb5eb30002");//更多新闻列表链接
				
				
				if(null != articleMap.get("photoArticleList") &&articleMap.get("photoArticleList").size()>0){//滚动图片列表
					root.put("photoArticleList", articleMap.get("photoArticleList"));
					int phtoSize = 0;
					if(!articleMap.get("photoArticleList").isEmpty()){
						phtoSize = articleMap.get("photoArticleList").size();
					}
					if(phtoSize > 5) {
						phtoSize = 5;
					}
					root.put("photoSize", phtoSize);
					
				}
				if(null != articleMap.get("articleDetail")){//文章详情				
					root.put("article", articleMap.get("articleDetail"));
				}
			}
			
			if(null !=bbsList && !bbsList.isEmpty()){
				root.put("bbsList", bbsList);
			}
			
			if(null != linkList && !linkList.isEmpty()){
				root.put("linkList", linkList);
			}
			
			if(ExStringUtils.isNotEmpty(schoolId)){
				root.put("schoolId", schoolId);
			}
			root.put("baseUrl", getBaseUrl());
			root.put("loginUrl",  getrequest().getScheme()+"s://"+getrequest().getServerName()+":8443"+getrequest().getContextPath());
			root.put("rootUrl", getrequest().getScheme()+"://"+getrequest().getServerName()+":"+getrequest().getServerPort()
					+ CacheAppManager.getSysConfigurationByCode("web.uploadfile.rooturl").getParamValue());
			//获取登录用户信息
			User user = SpringSecurityHelper.getCurrentUser();
			root.put("currentUser", user);
			//登录系统次数				
			if(null !=  getrequest().getSession(false) && null != getrequest().getSession(false).getAttribute("loginNum")){
				root.put("loginNum", (Integer)getrequest().getSession(false).getAttribute("loginNum"));				
			}else{
				root.put("loginNum", 0);	
			}
			template.process(root, super.pageContext.getOut());			
		} catch (Exception e) {
			logger.error("解析门户网站头部标签出错:"+e.fillInStackTrace());
		}
		return super.doStartTag();
	}

	
	public String getChannelType() {
		return channelType;
	}


	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}


	public List getChannelList() {
		return channelList;
	}

	public void setChannelList(List channelList) {
		this.channelList = channelList;
	}
	

	public List getBbsList() {
		return bbsList;
	}


	public void setBbsList(List bbsList) {
		this.bbsList = bbsList;
	}


	


	public List getLinkList() {
		return linkList;
	}


	public void setLinkList(List linkList) {
		this.linkList = linkList;
	}


	public Map getArticleMap() {
		return articleMap;
	}


	public void setArticleMap(Map<String,List<Article>> articleMap) {
		this.articleMap = articleMap;
	}


	public String getSchoolId() {
		return schoolId;
	}


	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}


	
	
}
