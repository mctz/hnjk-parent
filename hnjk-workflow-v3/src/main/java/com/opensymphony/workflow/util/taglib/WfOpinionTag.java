package com.opensymphony.workflow.util.taglib;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import com.opensymphony.workflow.spi.WfAgent;

/**
 * 工作流意见标签
 * @author dk
 *
 */
public class WfOpinionTag extends AbstractWfTag {
	
	private static final long serialVersionUID = 4185787302237764996L;
	
	private JspWriter out;

	@Override
	public int doStartTag() throws JspException {
		StringBuffer sb = new StringBuffer();
		this.out = this.pageContext.getOut();
		try {
			sb.append("<div class='wfOpinion'>\r\n");
			sb.append("<input type=hidden id=").append(WfAgent.APP_WF_ID).append(" name=").append(WfAgent.APP_WF_ID).append(" value=").append(this.appWfId).append(">\r\n");
			sb.append("<input type=hidden id=").append(WfAgent.APP_TYPE).append(" name=").append(WfAgent.APP_TYPE).append(" value=").append(appType).append(">\r\n");
			sb.append("<input type=hidden id=").append(WfAgent.ACTION_DO).append(" name=").append(WfAgent.ACTION_DO).append(">\r\n");
			if(!"YB".equalsIgnoreCase(appFrom)) {
				sb.append("<textarea name='opinion' cols=40 rows=4></textarea>\r\n");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			this.out.print(sb.toString());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return super.doStartTag();
	}

	@Override
	public int doEndTag() throws JspException {
		try {
			this.out.print("</div>\r\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return super.doEndTag();
	}
}
