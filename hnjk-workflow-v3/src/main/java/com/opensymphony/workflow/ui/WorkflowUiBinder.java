package com.opensymphony.workflow.ui;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.hnjk.core.rao.configuration.json.JsonBinder;
import com.hnjk.core.support.context.SpringContextHolder;
import com.opensymphony.workflow.FactoryException;
import com.opensymphony.workflow.loader.ActionDescriptor;
import com.opensymphony.workflow.loader.StepDescriptor;
import com.opensymphony.workflow.loader.WorkflowDescriptor;
import com.opensymphony.workflow.loader.WorkflowFactory;
import com.opensymphony.workflow.ui.model.WfStatus;
import com.opensymphony.workflow.ui.model.WfStatusPath;
import com.opensymphony.workflow.ui.model.WfStatusProps;
import com.opensymphony.workflow.ui.model.WfStatusRect;

/**
 * 流程图JSON转换器.
 * <code>WorkflowUiBinder</code><p>
 * 
 * @author：广东学苑教育发展有限公司.
 * @since： 2011-3-30 上午10:33:28
 * @see 
 * @version 1.0
 */
public class WorkflowUiBinder {

	
	/**
	 * 根据流程定义名，生成流程图的Json字符串
	 * @param wfname
	 * @return
	 * @throws FactoryException
	 */
	public static  String createWfTraceJson(String wfname) throws FactoryException{
		JsonBinder binder = JsonBinder.buildNonDefaultBinder();
		WorkflowFactory workflowFactory = (WorkflowFactory)SpringContextHolder.getBean("workflowFactory");
		WorkflowDescriptor workflowDescript = workflowFactory.getWorkflow(wfname);
	
		//初始化动作
		List<ActionDescriptor> initialActions  = workflowDescript.getInitialActions();
		
		WfStatus wfstats = new WfStatus();
		Map<String, WfStatusRect> wfRectsMap = new LinkedHashMap<String, WfStatusRect>();//states
		Map<String, WfStatusPath> wfPathMap = new LinkedHashMap<String, WfStatusPath>();//paths
		
		WfStatusRect rect = new WfStatusRect();		
		
		WfStatusProps props = new WfStatusProps();
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		
		WfStatusPath wfStatusPath = null;
		
		if(null != initialActions && initialActions.size()>0){
			ActionDescriptor actionDescriptor = initialActions.get(0);
			rect.setType(WfStatusRect.STATUSRECT_TYPE_START);
			map.put("text", actionDescriptor.getName());
			rect.setText(map);
			map = new LinkedHashMap<String, Object>();// x:211, y:117, width:50, height:50
			map.put("x", 2);map.put("y", 117);map.put("width", 50);map.put("height", 50);
			rect.setAttr(map);
			
			map = new LinkedHashMap<String, Object>();
			map.put("value", actionDescriptor.getName());
			props.setText(map);
			rect.setProps(props);
			//起始path
			wfStatusPath = new WfStatusPath();				
			wfStatusPath.setFrom("rect1");
			wfStatusPath.setTo("rect2");
			map = new LinkedHashMap<String, Object>();
			//获取第一个action
						
			
			map.put("text", actionDescriptor.getName());
			wfStatusPath.setText(map);
			map = new LinkedHashMap<String, Object>();
			map.put("x",-6);map.put("y", -6);
			wfStatusPath.setTextPos(map);
			wfStatusPath.setDots(new String[]{});
			
		}
		
		//加入其实节点		
		wfRectsMap.put("rect1", rect);
		wfPathMap.put("path1", wfStatusPath);	
		//步骤
		List<StepDescriptor> steps = workflowDescript.getSteps();
		int x = 1;
		ActionDescriptor actionDescriptor = null;
		if(null != steps && steps.size()>0){
			for(StepDescriptor stepDescriptor : steps){
				rect = new WfStatusRect();	
				map = new LinkedHashMap<String, Object>();
				
				map.put("text", stepDescriptor.getName());
				rect.setText(map);
				//attr
				map = new LinkedHashMap<String, Object>();
				if("endflow".equals(stepDescriptor.getName())){
					rect.setType(WfStatusRect.STATUSRECT_TYPE_END);
					map.put("x", 2+(200*x));map.put("y", 117);map.put("width", 50);map.put("height", 50);
				}else{
					rect.setType(WfStatusRect.STATUSRECT_TYPE_TASK);
					map.put("x", 2+(200*x));map.put("y", 117);map.put("width", 100);map.put("height", 50);
				}
				
				rect.setAttr(map);
				map = new LinkedHashMap<String, Object>();
				map.put("value", stepDescriptor.getName());
				props.setText(map);
				rect.setProps(props);
				x++;
				if(steps.size()>=x){
					//paths
					wfStatusPath = new WfStatusPath();				
					wfStatusPath.setFrom("rect"+x);
					wfStatusPath.setTo("rect"+(x+1));
					map = new LinkedHashMap<String, Object>();
					//获取第一个action
					List<ActionDescriptor> actionDescriptors = stepDescriptor.getActions();
					
					if(null != actionDescriptors && actionDescriptors.size()>0){
						actionDescriptor = actionDescriptors.get(0);
					}
					map.put("text", actionDescriptor.getName());
					wfStatusPath.setText(map);
					map = new LinkedHashMap<String, Object>();
					map.put("x",-6);map.put("y", -6);
					wfStatusPath.setTextPos(map);
					wfStatusPath.setDots(new String[]{});
					wfPathMap.put("path"+x, wfStatusPath);			
				}
				
				wfRectsMap.put("rect"+x, rect);
			}
		}
		
		wfstats.setStates(wfRectsMap);
		wfstats.setPaths(wfPathMap);
		return binder.toJson(wfstats);
	}
	
	
}
