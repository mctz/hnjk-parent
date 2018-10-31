package com.hnjk.edu.recruit.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hnjk.core.foundation.utils.JsonModel;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.support.base.controller.BaseSupportController;
import com.hnjk.edu.recruit.model.BranchSchoolPlan;
import com.hnjk.edu.recruit.model.RecruitPlan;
import com.hnjk.edu.recruit.service.IBranchSchoolPlanService;
import com.hnjk.edu.recruit.service.IRecruitPlanService;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;
/**
 * 
 * <code>校外学习中心BranchShoolPlanController</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-8-3 下午02:05:31
 * @see 
 * @version 1.0
 */
@Controller
public class BranchShoolPlanController extends BaseSupportController {
	
	private static final long serialVersionUID = 1L;
	
	@Autowired
	@Qualifier("branchSchoolPlanService")
	private IBranchSchoolPlanService branchSchoolPlanService;
	
	@Autowired
	@Qualifier("recruitPlanService")
	private IRecruitPlanService recruitPlanService;
	
	@RequestMapping("/edu3/framework/branchshoolplan/branchSchoolPlan-list.html")
	public void ListBranchSchoolPlan(String recruitPlanId,HttpServletRequest request,HttpServletResponse response, ModelMap model){
		
		Map<String,Object> condition = new HashMap<String, Object>();
		RecruitPlan recruitPlan 	 = recruitPlanService.load(recruitPlanId);
		User user 				     = SpringSecurityHelper.getCurrentUser();
		if (null != user && CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())) {
			condition.put("brSchoolId", user.getOrgUnit().getResourceid());
		}
		
		//版本一:查出所选批次下的所有通过审核的校外学习中心招生批次
		//版本二:查出所选批次对应年级下的所有招生批次中通过审核的校外学习中心招生批次(当前使用版本)
		String grade                 = null==recruitPlan.getGrade()?"":recruitPlan.getGrade().getResourceid();
		//condition.put("recruitPlanId", recruitPlanId);
		condition.put("grade", grade);
		
		List<BranchSchoolPlan> list  = branchSchoolPlanService.getBranchSchoolPlanList(condition);
		List<JsonModel> jsonList     = new ArrayList<JsonModel>();
		
		Map<String,Object> planList  = new LinkedHashMap<String, Object>();
		if(null!=list&&!list.isEmpty()){
			condition.clear();
			for (int i = 0; i < list.size(); i++) {//去初重复数据
				if ("Y".equals(recruitPlan.getIsSpecial())) {
					if (recruitPlan.getBrSchoolIds().indexOf(list.get(i).getBranchSchool().getResourceid())>=0) {
						planList.put(list.get(i).getBranchSchool().getResourceid(), list.get(i));
					}
				}else {
					planList.put(list.get(i).getBranchSchool().getResourceid(), list.get(i));
				}
			}
			Collection<Object> collection = planList.values();
			
			for (Object o:collection){
				
				BranchSchoolPlan plan 	  = (BranchSchoolPlan)o;
				JsonModel  json      	  = new JsonModel();
				json.setKey(plan.getBranchSchool().getResourceid());
				json.setValue(plan.getBranchSchool().getUnitCode()+"-"+plan.getBranchSchool().getUnitName());
				jsonList.add(json);
				
			}
			/*for (BranchSchoolPlan plan : list) {
				JsonModel  json      = new JsonModel();
				json.setKey(plan.getBranchSchool().getResourceid());
				json.setValue(plan.getBranchSchool().getUnitName());
				jsonList.add(json);
			}*/
		}
		renderJson(response, JsonUtils.listToJson(jsonList));
	}
}
