package com.opensymphony.workflow.util.taglib;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import com.opensymphony.workflow.Workflow;
import com.opensymphony.workflow.spi.WfAgent;
import com.opensymphony.workflow.spi.hibernate3.HibernateCurrentStep;


/**
 * 流程当前Action标签
 * @author dk
 *
 */
public class WfCurrentStepTag extends AbstractWfTag {

	private static final long serialVersionUID = 6077341979800675325L;
	
	private JspWriter out;

	@Override
	public int doStartTag() throws JspException {
		this.out = this.pageContext.getOut();
		StringBuffer sb = new StringBuffer();
		sb.append("<div class='wfCurrentStep'>");
		try {
			Workflow wf = WfAgent.getWorkflow((HttpServletRequest)pageContext.getRequest());
			if(appWfId != null && !"0".equalsIgnoreCase(appWfId)) {
				List<HibernateCurrentStep> currentSteps = wf.getCurrentSteps(Long.parseLong(appWfId));
				if(currentSteps.size() > 0 ) {
					if("Underway".equalsIgnoreCase(currentSteps.get(0).getStatus())) {
						sb.append("<li>").append(wf.getWorkflowDescriptor(appType).getStep(currentSteps.get(0).getStepId()).getName()).append("</li>");
					}else {
						sb.append("<li>").append("流程已经结束!").append("</li>");
					}
				}
			}else {
				sb.append("<li>").append("当前表单还未保存!").append("</li>");
			}
		} catch (Exception e) {
			sb.append("工作流标签出错!");
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
			this.out.print("</div>");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return super.doEndTag();
	}
}
