package com.hnjk.extend.plugin.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.util.StringSplitUtils;

import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.support.context.SpringContextHolder;

/**
 * 组件文件加载标签.<p>
 * 
 * @author 广东学苑教育发展有限公司.
 * @since:2009-5-5下午03:16:33
 * @see 
 * @version 1.0
 */
public class LoadComponentsTag extends TagSupport {

	private static final long serialVersionUID = 6446159387285277219L;
	
	private static Logger logger = LoggerFactory.getLogger(LoadComponentsTag.class);
	
	/**组件列表，用逗号分隔符分开 比如："treeView,gridView,tabView" */
	private String components;
	
	@Override
	public int doStartTag() throws JspException {
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();	
		String baseUrl = request.getContextPath();
		
		LoadComponentsFile loadComponentsFile = SpringContextHolder.getBean("loadComponentsFile");;
		StringBuffer html = new StringBuffer("");
		String[] cs=StringSplitUtils.splitIgnoringQuotes(components, ',');
		String idString = "";
		for(String c : cs){
			// 根据组件代码获取文件列表
			List<ComponentsFile> cfs = loadComponentsFile.getComponentsFiles(c);
			for(ComponentsFile cf : cfs){
				if(!ExStringUtils.isEmpty(cf.getFileId())){
					idString = " id= '" + cf.getFileId() +" '";
				}
				if(cf.getFileType().equals(ComponentsFile.fileType_Js)){
					html.append("<script type=\"text/javascript\" "+idString+" src=\""+baseUrl+cf.getFilePath()+"\"></script>\n");
				}
				if(cf.getFileType().equals(ComponentsFile.fileType_Css)){
					html.append("<link rel=\"stylesheet\" "+idString+" href=\""+baseUrl+cf.getFilePath()+"\" />\n");
				}
			}
		}
		try {
			pageContext.getOut().print(html.toString());
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		return super.doStartTag();
	}

	public String getComponents() {
		return components;
	}

	public void setComponents(String components) {
		this.components = components;
	}
	
	
	
	

}

