package com.hnjk.platform.taglib;

import java.io.IOException;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import com.hnjk.core.rao.configuration.property.ConfigPropertyUtil;
import com.hnjk.extend.taglib.BaseTagSupport;

/**
 * 系统版本信息.
 * <code>VersionTag</code><p>
 * 
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-5-13 上午11:18:49
 * @see 
 * @version 1.0
 */
public class VersionTag extends BaseTagSupport{

	private static final long serialVersionUID = -5691859415960521309L;
	
	@Override
	@SuppressWarnings("static-access")
	public int doEndTag() throws JspException {
		StringBuffer bf = new StringBuffer();
		// 版本信息
		bf.append("Version："+ConfigPropertyUtil.getInstance().getProperty("edu3.version"));
		bf.append(" (update:"+ ConfigPropertyUtil.getInstance().getProperty("version.buildingdate")+" )");
		HttpSession session = getrequest().getSession(false);
		String[] hostArr = session.getId().split("\\.");
		if(null != hostArr && hostArr.length>1){
			bf.append(" Host："+hostArr[1]);
		}
			
		JspWriter writer = this.pageContext.getOut();
		try {
			writer.append(bf.toString());
		} catch (IOException e) {
		   logger.error("获取版本信息出错：{}",e.fillInStackTrace());
		}
		return EVAL_PAGE;
	}

}
