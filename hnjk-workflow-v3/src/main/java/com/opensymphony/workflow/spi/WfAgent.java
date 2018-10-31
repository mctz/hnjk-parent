package com.opensymphony.workflow.spi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;
import com.opensymphony.workflow.InvalidActionException;
import com.opensymphony.workflow.Workflow;
import com.opensymphony.workflow.basic.BasicWorkflow;
import com.opensymphony.workflow.loader.ActionDescriptor;
import com.opensymphony.workflow.spi.hibernate3.HibernateCurrentStep;


/**
 * 要求实现用户id放入 HttpSession
 * @author dk
 *
 */
@Transactional
public class WfAgent {
	protected static Log log = LogFactory.getLog(WfAgent.class);
	
	public static final String APP_TYPE = "APP_TYPE";
	public static final String APP_WF_ID = "APP_WF_ID";
	public static final String ACTION_DO = "ACTION_DO";
	
		
	/**
	 * 获取Workflow
	 */
	public static Workflow getWorkflow(HttpServletRequest...request) throws Exception {
		// 将ID前增加 "USER_" 字符串
		return new BasicWorkflow(Workflow.TYPE_USER.concat(getLogonId(request)));
	}

	/**
	 * 执行流程实例化 
	 */
	public static long initialize(HttpServletRequest request) throws Exception{
		return initialize(getWorkflow(request),request);
	}
	
	public static long initialize(Workflow wf,HttpServletRequest request) throws Exception{
		String workflowName = request.getParameter(APP_TYPE);
		
		// 流程都会有initialAction
		int initActionId = ((ActionDescriptor)wf.getWorkflowDescriptor(workflowName).getInitialActions().get(0)).getId();		
		User user = SpringSecurityHelper.getCurrentUser();
		return wf.initialize(workflowName,user.getResourceid(),user.getOrgUnit().getResourceid(), initActionId, null);
	}
	
	/**
	 * 执行流程动作
	 */
	public static void doAction(HttpServletRequest request) throws Exception{
		doAction(getWorkflow(request),request);
	}
	
	public static void doAction(Workflow wf,HttpServletRequest request) throws Exception{
		Map<String, Object> inputs = new HashMap<String,Object>();
		inputs.put("opinion", request.getParameter("opinion"));		
		doAction(wf, request, inputs);
	}
	
	public static void doAction(Workflow wf,HttpServletRequest request,Map<String,Object> inputs) throws Exception{
		String wfId = request.getParameter(APP_WF_ID);
		String doActionId = request.getParameter(ACTION_DO);
		
		HibernateCurrentStep curstep = null;
		String oldOwner = null;
		try {
			List<HibernateCurrentStep> cursteps = wf.getCurrentSteps(Long.parseLong(wfId));
			if(cursteps != null && cursteps.size() > 0) {
				curstep = cursteps.get(0);
				oldOwner = curstep.getOwner();
				if(!oldOwner.startsWith(Workflow.TYPE_USER)) {
					curstep.setOwner(Workflow.TYPE_USER.concat(getLogonId(request)));
					wf.updateState(curstep);
				}
			}
			wf.doAction(Long.parseLong(wfId), Integer.parseInt(doActionId), inputs);
		}catch(NumberFormatException ex) {
			log.error("WfID或者ActionID从String转换数字类型出错!如果是ActionID出错,那么可能流程已经完成!");
		}catch(InvalidActionException ex) {
			// 出现异常 还原所做修改
			if(oldOwner!=null && !oldOwner.startsWith(Workflow.TYPE_USER)) {
				curstep.setOwner(oldOwner);
				wf.updateState(curstep);
			}
			log.error("当前Action被连续重复执行");
		}
	}
	
	
	
	public static void deleteEntry(long wfId) throws Exception{
		Workflow wf = getWorkflow();
		wf.deleteEntry(wfId);
	}
	
	/**
	 * 回退操作,返回上一个Step
	 */
	public static void getBack(HttpServletRequest request) throws Exception{
		String wfId = request.getParameter(APP_WF_ID);
		Workflow wf = getWorkflow(request);
		wf.doGetBack(Long.parseLong(wfId));
	}
	
	/**
	 * 获取用户id 
	 */
	public static String getLogonId(HttpServletRequest...request){
		try {
			return SpringSecurityHelper.getCurrentUser().getResourceid();
		}catch(Exception ex) {
			log.error("not found userid in httpSession!");
			return null;
		}
	}
}