package com.hnjk.extend.taglib.page;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.jsp.JspException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hnjk.core.foundation.template.FreeMarkerConfig;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.context.Constants;
import com.hnjk.extend.taglib.BaseTagSupport;

import freemarker.template.ObjectWrapper;
import freemarker.template.SimpleHash;
import freemarker.template.Template;

/**
 * 分页标签. <code>PageTag</code><p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-3-26 下午01:33:50
 * @see 
 * @version 1.0
 */
public class PageTag extends BaseTagSupport {

	private static final long serialVersionUID = -9218823737680856684L;

	private static Logger logger = LoggerFactory.getLogger(PageTag.class);

	transient private Page page; // 分页的实体对象
	
	private String goPageUrl;//页面跳转URL
	
	private String pageType = "sys";// 分页类型：sys-系统，other - 其他，bbs - 论坛，resource - 精品课程
	
	private String targetType = "navTab";//分页类型 navTab|dialog|localArea   用来标记是navTab上的分页还是dialog上的分页
	
	private String localArea = "";//局部刷新的DIV ID，如果targetType选择localArea，这里必须赋值
	
	private String pageNumShown = "10";
	
	private String isShowPageSelector = "Y";//是否显示分页选择项,Y|N 是|否
	
	private String beforeForm = "";//分页前提交的FORM，以AJAX POST方式提交
	
	private String postBeforeForm = Constants.BOOLEAN_NO;
	
	transient private Map<String,Object> condition;//查询条件，用来动态生成分页的查询条件
	
	@Override
	@SuppressWarnings("unchecked")
	public int doStartTag() throws JspException {
		if (page == null) {
			return super.doStartTag();
		}
		try {
			Template template = null;
			Map root = new HashMap();
			PageInfo pi = PageInfo.getByPage(page);
			if("sys".equals(pageType)){
				template = FreeMarkerConfig.getDefaultTemplate("system"+File.separator+"page.ftl");				
			}else if("other".equals(pageType)){
				template = FreeMarkerConfig.getDefaultTemplate("system"+File.separator+"page1.ftl");
			}else if("bbs".equals(pageType)){
				template = FreeMarkerConfig.getDefaultTemplate("system"+File.separator+"page2.ftl");
			}else if("sp_graduate".equals(pageType)){//为毕业/结业审核设置特殊分页
				template = FreeMarkerConfig.getDefaultTemplate("system"+File.separator+"page3.ftl");
			}else if("mini".equals(pageType)){//移动版
				template = FreeMarkerConfig.getDefaultTemplate("system"+File.separator+"page4.ftl");
			} else if("resource".equals(pageType)){//精品课程
				template = FreeMarkerConfig.getDefaultTemplate("system"+File.separator+"page5.ftl");
			}else if("sp_degree".equals(pageType)){//为学位审核设置特殊分页
				template = FreeMarkerConfig.getDefaultTemplate("system"+File.separator+"page6.ftl");
			}
			root.put("baseUrl", getBaseUrl());
			root.put("goPageUrl", goPageUrl);
			root.put("beforeForm", beforeForm);//分页前的执行的FORM
			root.put("postBeforeForm", postBeforeForm);
			root.put("targetType", targetType);			
			root.put("localArea", localArea);
			root.put("pageNumShown", pageNumShown);
			root.put("isShowPageSelector", isShowPageSelector);
			if(null != condition){
				SimpleHash hashMap = new SimpleHash(ObjectWrapper.DEFAULT_WRAPPER);
				Iterator it = condition.entrySet().iterator();    
				   while(it.hasNext()){    
				   Map.Entry entry = (Map.Entry)   it.next();    
				   hashMap.put(entry.getKey().toString(),entry.getValue());    
				 }   
				 root.put("condition", hashMap);   
			}
			
			root.put("pageInfo", pi);			
			template.process(root, super.pageContext.getOut());
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return super.doStartTag();
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public String getGoPageUrl() {
		return goPageUrl;
	}

	public void setGoPageUrl(String goPageUrl) {
		this.goPageUrl = goPageUrl;
	}

	public String getPageType() {
		return pageType;
	}

	public void setPageType(String pageType) {
		this.pageType = pageType;
	}

	public Map getCondition() {
		return condition;
	}

	public void setCondition(Map<String,Object> condition) {
		this.condition = condition;
	}

	public String getTargetType() {
		return targetType;
	}

	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}

	public String getPageNumShown() {
		return pageNumShown;
	}

	public void setPageNumShown(String pageNumShown) {
		this.pageNumShown = pageNumShown;
	}

	/**
	 * @return the localArea
	 */
	public String getLocalArea() {
		return localArea;
	}

	/**
	 * @param localArea the localArea to set
	 */
	public void setLocalArea(String localArea) {
		this.localArea = localArea;
	}

	/**
	 * @return the isShowPageSelector
	 */
	public String getIsShowPageSelector() {
		return isShowPageSelector;
	}

	/**
	 * @param isShowPageSelector the isShowPageSelector to set
	 */
	public void setIsShowPageSelector(String isShowPageSelector) {
		this.isShowPageSelector = isShowPageSelector;
	}

	public String getBeforeForm() {
		return beforeForm;
	}

	public void setBeforeForm(String beforeForm) {
		this.beforeForm = beforeForm;
	}

	public String getPostBeforeForm() {
		return postBeforeForm;
	}

	public void setPostBeforeForm(String postBeforeForm) {
		this.postBeforeForm = postBeforeForm;
	}

	

}
