package com.opensymphony.workflow.util.taglib;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import org.apache.commons.lang.StringUtils;

import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.security.cache.CacheSecManager;
import com.opensymphony.workflow.Workflow;
import com.opensymphony.workflow.loader.WorkflowDescriptor;
import com.opensymphony.workflow.spi.WfAgent;
import com.opensymphony.workflow.spi.hibernate3.HibernateCurrentStep;
import com.opensymphony.workflow.spi.hibernate3.HibernateHistoryStep;
import com.opensymphony.workflow.spi.hibernate3.HibernateStep;

/**
 * 轨迹
 * @author dk
 *
 */
public class WfTraceTag extends AbstractWfTag {

	private static final long serialVersionUID = 7874222253895210816L;

	@Override
	public int doStartTag() throws JspException {
		StringBuffer sb = new StringBuffer();	
		try {
			sb.append("<table width='100%'><tr><td width='80%' valign='top'><table class='list' width='100%'><tr>");
			sb.append("<th width='2%'>*</th>");
			sb.append("<th width='15%'>步骤名称</th>");
			sb.append("<th width='15%'>提交时间</th>");
			sb.append("<th width='15%'>操作人</th>");
			sb.append("<th width='15%'>处理时间</th>");
			sb.append("<th width='38%'>处理意见</th>");
			sb.append("</tr>");  
			String wfcimg = "";
			if(!ExStringUtils.isEmpty(appWfId) && !"0".equalsIgnoreCase(appWfId)) {
				sb.append("<tbody>");
				Workflow wf = WfAgent.getWorkflow((HttpServletRequest)pageContext.getRequest());
				List<HibernateHistoryStep> historySteps = wf.getHistorySteps(Long.parseLong(appWfId));

				WorkflowDescriptor wfDsper = wf.getWorkflowDescriptor(wf.getWorkflowName(Long.parseLong(appWfId)));
				
				wfcimg = wfDsper.getMetaAttributes().get("wfcimg").toString();
				int i=1;
				for(HibernateStep step : historySteps){
					buildStep(sb, wfDsper, step, i++);
				}
				List<HibernateCurrentStep> currentSteps = wf.getCurrentSteps(Long.parseLong(appWfId));
				if(currentSteps.size() > 0 ) {
					if("Underway".equalsIgnoreCase(currentSteps.get(0).getStatus())) {
						buildStep(sb, wfDsper, currentSteps.get(0), i++);
					}else {
						sb.append("<tr>");
						sb.append("<td>"+(i++)+"</td>");
						sb.append("<td cols='5'><font color='red'>流程已经结束!</font></td>");
					}
				}
				sb.append("</tbody>");
			}
			sb.append("</table></td><td width='20%' valign='top'>");
			sb.append("<div style='padding-left:12px;text-align:center;border:1px solid #ccc'><img src='"+getBaseUrl()+wfcimg+"'/><div style='padding:4px'><b>流程图</b></div></div>");
			sb.append("</td></tr></table>");
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {			
			super.pageContext.getOut().print(sb.toString());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return super.doStartTag();
	}
	
	private void buildStep(StringBuffer sb,WorkflowDescriptor wfDsper,HibernateStep step,int order) {
		sb.append("<tr>");
		sb.append("<td>"+order+"</td>");
		if("endflow".equals(wfDsper.getStep(step.getStepId()).getName())){//流程最后一步：归档
			sb.append("<td>归档</td>");
		}else{
			sb.append("<td>"+wfDsper.getStep(step.getStepId()).getName()+"</td>");
		}
		
		sb.append("<td>"+ExDateUtils.DATE_TIME_FORMAT.format((step.getStartDate()))+"</td>");
		sb.append("<td>"+(getPersonName(step.getOwner()))+"</td>");
		sb.append("<td>"+(step.getFinishDate() == null ? "" : ExDateUtils.DATE_TIME_FORMAT.format((step.getFinishDate())))+"</td>");
		sb.append("<td>"+StringUtils.defaultIfEmpty(step.getOpinion(), "")+"</td>");
		sb.append("</tr>");
	}
	
	private String getPersonName(String str) {
		if(str == null) {
			return "";
		}
		if(str.startsWith(Workflow.TYPE_USER)) {
			return CacheSecManager.ids2names("user", str.split(Workflow.TYPE_USER)[1]);
		}else if(str.startsWith(Workflow.TYPE_DEPT)) {
			return "部门:".concat(CacheSecManager.ids2names("org", str.split(Workflow.TYPE_DEPT)[1]));
		}else if(str.startsWith(Workflow.TYPE_ROLE)) {
			return "角色:".concat(CacheSecManager.ids2names("role", str.split(Workflow.TYPE_ROLE)[1]));
		}else {
			return "";
		}
	}
}
