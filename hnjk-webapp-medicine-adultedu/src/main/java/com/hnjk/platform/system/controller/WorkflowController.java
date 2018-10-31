package com.hnjk.platform.system.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.support.base.controller.BaseSupportController;
import com.hnjk.platform.system.model.WorkflowConfiguration;
import com.opensymphony.workflow.FactoryException;
import com.opensymphony.workflow.loader.WorkflowFactory;
import com.opensymphony.workflow.loader.XMLWorkflowFactory.WorkflowConfig;
import com.opensymphony.workflow.ui.WorkflowUiBinder;

/**
 * 工作流控制器.
 * <code>WorkflowController</code><p>
 * 
 * @author：广东学苑教育发展有限公司.
 * @since： 2011-3-23 上午10:37:42
 * @see 
 * @version 1.0
 */
@Controller
public class WorkflowController extends BaseSupportController{
	
	private static final long serialVersionUID = -6108804665755638604L;
	
	@Autowired
	@Qualifier("workflowFactory")
	private WorkflowFactory workflowFactory;
	
		
	
	/**
	 * 工作流定义列表
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/system/workflow/define/list.html")
	public String wfDefineList(HttpServletRequest request, ModelMap model) throws WebException{
		List<WorkflowConfiguration> wfList = new ArrayList<WorkflowConfiguration>();
		try {
			Map<String, WorkflowConfig> workflowsMap = workflowFactory.getWorkflows();		
			if(null != workflowsMap && !workflowsMap.isEmpty()){
				for (Object o : workflowsMap.keySet()) {
					WorkflowConfiguration workflowConfiguration = new WorkflowConfiguration();
					WorkflowConfig wfc = workflowsMap.get(o);
					workflowConfiguration.setName(o.toString());
					workflowConfiguration.setCnname(wfc.cnname);
					workflowConfiguration.setType(wfc.type);
					workflowConfiguration.setLocation(wfc.location);
					workflowConfiguration.setVersion(1);//TODO
					workflowConfiguration.setLastmodifyTime(new Date(wfc.lastModified));
					wfList.add(workflowConfiguration);
				}
			}			
		} catch (Exception e) {		
			logger.error("获取流程配置列表出错："+e.fillInStackTrace());			
		}
		model.addAttribute("wfDefineList", wfList);
		
		return "/system/workflow/define-list";
	}
	
	/**
	 * 流程图
	 * @param request
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/framework/workflow/flowimg.html")
	public String showFlowImg(HttpServletRequest request, ModelMap model) throws WebException{
		String wffile = ExStringUtils.trimToEmpty(request.getParameter("fwfile"));
		//读取定义文件，并拼装流程图JSON
			try {
			
			String json = WorkflowUiBinder.createWfTraceJson(wffile);
			model.addAttribute("restore",json);
		} catch (FactoryException e) {
			
		}
		
		return "/system/workflow/flowimg-show";
	}
	
	/**
	 * 流程实例列表
	 * @param workflowName
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/system/workflow/entry/list.html")
	public String wfentryList(String workflowName,ModelMap model) throws WebException{
		
		return "/system/workflow/entry-list";
	}
}
