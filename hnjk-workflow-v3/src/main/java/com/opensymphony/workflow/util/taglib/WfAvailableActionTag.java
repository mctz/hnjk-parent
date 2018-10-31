package com.opensymphony.workflow.util.taglib;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import org.apache.commons.lang.StringUtils;

import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.support.context.SpringContextHolder;
import com.hnjk.security.model.UserAdaptor;
import com.hnjk.security.service.IUserService;
import com.opensymphony.workflow.Workflow;
import com.opensymphony.workflow.spi.WfAgent;
import com.opensymphony.workflow.spi.hibernate3.HibernateCurrentStep;
import com.opensymphony.workflow.spi.hibernate3.HibernateHistoryStep;

/**
 * 可执行操作显示
 * @author dk
 *
 */
public class WfAvailableActionTag extends AbstractWfTag {

	private static final long serialVersionUID = 2247850388767474991L;
	private JspWriter out;	

	@Override
	public int doStartTag() throws JspException {
		StringBuffer sb = new StringBuffer();
		this.out = this.pageContext.getOut();
		try {
			HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
			Workflow wf = WfAgent.getWorkflow(request);
			// 提交按钮
			if(!"YB".equalsIgnoreCase(appFrom)) {
				if(ExStringUtils.isEmpty(appFrom) && (ExStringUtils.isEmpty(appWfId) || "0".equalsIgnoreCase(appWfId))){
					sb.append("<span class=\"button\" style=\"margin-right:4px\"><span class=\"buttonContent\" onclick=\" return  "+appType+"_submitForm(0);\"><button>保 存</button></span></span>");
				}else{
					List<HibernateCurrentStep> list = wf.getCurrentSteps(Long.parseLong(appWfId));
					if (null!=list && !list.isEmpty()) {
						HibernateCurrentStep currentStep = list.get(0);
						int[] actionIds = wf.getAvailableActions(Long.parseLong(appWfId));
						if(userAuthCondition(currentStep.getOwner()) && actionIds.length > 0) {
							//sb.append("<input type=button value=保存 onclick=\""+appType+"_submitForm(0);\">");
							//sb.append("<span class=\"button\" style=\"margin-right:4px\"><span class=\"buttonContent\" onclick=\"return "+appType+"_submitForm(0);\"><button>保 存</button></span></span>");
							
							for(int i : actionIds) {
								sb.append("<span class=\"button\" style=\"margin-right:4px\"><span class=\"buttonContent\" onclick=\"document.getElementById('"+WfAgent.ACTION_DO+"').value="+i+";return "+appType+"_submitForm(1);\"><button>"
										+wf.getWorkflowDescriptor(appType).getAction(i).getName()+"</button></span></span>");
								
//								sb.append("<input type=button value=")
//								.append(wf.getWorkflowDescriptor(appType).getAction(i).getName())
//								.append(" onclick=\"document.getElementById('"+WfAgent.ACTION_DO+"').value="+i+";"+appType+"_submitForm(1);\">");
							}
						}
					}
					
				}
				
				/*
				if(ExStringUtils.isNotEmpty(appFrom) && !ExStringUtils.isEmpty(appWfId)  && !appWfId.equalsIgnoreCase("0")) {
					List<HibernateCurrentStep> list = wf.getCurrentSteps(Long.parseLong(appWfId));
					HibernateCurrentStep currentStep = wf.getCurrentSteps(Long.parseLong(appWfId)).get(0);
					int[] actionIds = wf.getAvailableActions(Long.parseLong(appWfId));
					if(userAuthCondition(currentStep.getOwner()) && actionIds.length > 0) {
						//sb.append("<input type=button value=保存 onclick=\""+appType+"_submitForm(0);\">");
						//sb.append("<span class=\"button\" style=\"margin-right:4px\"><span class=\"buttonContent\" onclick=\"return "+appType+"_submitForm(0);\"><button>保 存</button></span></span>");
						
						for(int i : actionIds) {
							sb.append("<span class=\"button\" style=\"margin-right:4px\"><span class=\"buttonContent\" onclick=\"document.getElementById('"+WfAgent.ACTION_DO+"').value="+i+";return "+appType+"_submitForm(1);\"><button>"
									+wf.getWorkflowDescriptor(appType).getAction(i).getName()+"</button></span></span>");
							
//							sb.append("<input type=button value=")
//							.append(wf.getWorkflowDescriptor(appType).getAction(i).getName())
//							.append(" onclick=\"document.getElementById('"+WfAgent.ACTION_DO+"').value="+i+";"+appType+"_submitForm(1);\">");
						}
					}
				}else {
					//sb.append("<input type='button' value='保存' onclick=\""+appType+"_submitForm(0);\">");
					sb.append("<span class=\"button\" style=\"margin-right:4px\"><span class=\"buttonContent\" onclick=\" return  "+appType+"_submitForm(0);\"><button>保 存</button></span></span>");
				}
				*/
			}
			// 回退按钮
			if(!ExStringUtils.isEmpty(appWfId) && !"0".equalsIgnoreCase(appWfId)) {
				//List<HibernateHistoryStep> previousSteps =  wf.getHistorySteps(Long.parseLong(appWfId));
				//if(previousSteps != null && previousSteps.size() > 0) {
				//	int[] actionIds = wf.getAvailableActions(Long.parseLong(appWfId));
				//	if(userAuthCondition(previousSteps.get(previousSteps.size()-1).getOwner()) && actionIds.length > 0) {
						//sb.append("<input type='button' value='取回'  onclick='"+appType+"_getBack()'>");
						//sb.append("<span class=\"button\" style=\"margin-right:4px\"><span class=\"buttonContent\" onclick='return "+appType+"_getBack()' title='审批人未审批情况下可以取回'><button>取 回</button></span></span>");
					//}
				//}
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
	
	/**
	 * 判断是否有权限
	 * @param owner
	 * @return
	 */
	private boolean userAuthCondition(String owner) {
		String userid = WfAgent.getLogonId((HttpServletRequest)pageContext.getRequest());
		if(owner == null) {
			return false;
		}
		
		//UserAdaptor ui = CacheSecManager.userInfoMap.get(userid);
		//User user = SpringSecurityHelper.getUserByUserId(userid);
		//UserAdaptor ui = user.getUserAdaptor();
		IUserService userService = (IUserService)SpringContextHolder.getBean("userService");//获取用户服务接口
    	UserAdaptor ui = userService.getUserAdaptor(userid);	
		if(owner.startsWith(Workflow.TYPE_USER)) {
			return owner.indexOf(userid) >= 0;
		}else if(owner.startsWith(Workflow.TYPE_DEPT)) {
			return StringUtils.join(ui.getDeptIdArr(),",").indexOf(owner.split(Workflow.TYPE_DEPT)[1])>=0;
		}else if(owner.startsWith(Workflow.TYPE_ROLE)) {
			return StringUtils.join(ui.getRoleIdArr(),",").indexOf(owner.split(Workflow.TYPE_ROLE)[1])>=0;
		}else {
			return false;
		}
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
