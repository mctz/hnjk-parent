package com.hnjk.edu.recruit.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.JsonModel;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.support.base.controller.BaseSupportController;
import com.hnjk.edu.recruit.helper.ComparatorBranchSchoolMajor;
import com.hnjk.edu.recruit.helper.ComparatorRecruitMajor;
import com.hnjk.edu.recruit.model.BranchSchoolMajor;
import com.hnjk.edu.recruit.model.BranchSchoolPlan;
import com.hnjk.edu.recruit.model.BrschMajorSetting;
import com.hnjk.edu.recruit.model.RecruitMajor;
import com.hnjk.edu.recruit.model.RecruitPlan;
import com.hnjk.edu.recruit.service.IBranchSchoolMajorService;
import com.hnjk.edu.recruit.service.IBranchSchoolPlanService;
import com.hnjk.edu.recruit.service.IBrschMajorSettingService;
import com.hnjk.edu.recruit.service.IRecruitJDBCService;
import com.hnjk.edu.recruit.service.IRecruitPlanService;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;
/**
 * 
 * <code>校外学习中心BranchShoolMjorController</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-8-3 下午02:29:46
 * @see 
 * @version 1.0
 */
@Controller
public class BranchShoolMjorController extends BaseSupportController {

	private static final long serialVersionUID = 1L;
	
	@Autowired
	@Qualifier("branchSchoolPlanService")
	private IBranchSchoolPlanService branchSchoolPlanService;
	
	@Autowired
	@Qualifier("branchSchoolMajorService")
	private IBranchSchoolMajorService branchSchoolMajorService;
	
	@Autowired
	@Qualifier("brschMajorSettingService")
	private IBrschMajorSettingService brschMajorSettingService;

	@Autowired
	@Qualifier("recruitJDBCService")
	private IRecruitJDBCService recruitJDBCService;
	
	@Autowired
	@Qualifier("recruitPlanService")
	private IRecruitPlanService recruitPlanService;
	
	/**
	 * 通过批次ID、学习中心获取该学习中心在对应批次中申报通过的招生专业
	 * @param branchPlanId
	 * @param branchSchoolId
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping("/edu3/framework/branchshoolMajor/branchMajor-list.html")
	public void listBranchSchoolMajor(String branchPlanId,String branchSchoolId,HttpServletRequest request,HttpServletResponse response, ModelMap model){
		
		Map<String,Object> condition = new HashMap<String, Object>();
		List<JsonModel> jsonList     = new ArrayList<JsonModel>();
		
		
		
		/*
		版本一:只查出所选招生批次学习中心申报通过的招生专业
		condition.put("recruitPlanId", branchPlanId);
		condition.put("branchSchoolId",branchSchoolId);
		String branchSchoolPlanIds   = recruitJDBCService.findBranchSchoolPlanIdList(condition);
		if (!"".equals(branchSchoolPlanIds)) {
			condition.put("brSchoolPlanIds", branchSchoolPlanIds);
		}
		List<BranchSchoolMajor> list = branchSchoolMajorService.findBranchSchoolMajorList(condition);
		if(null!=list&&!list.isEmpty()){
			for(BranchSchoolMajor major:list){
				JsonModel  json      = new JsonModel();
				json.setKey(major.getRecruitMajor().getResourceid());
				json.setValue(major.getRecruitMajor().getRecruitMajorName());
				jsonList.add(json);
			}
		}
		*/
		
		
		//--------------------------------------------------------------------------------------------------------------------
		
		
		/*
		 * 版本二:查出所选批次所对应的年级下的所有招生批次下的学习中心申报通过且有权限的招生专业(针对一个季度只申报一次的需求)
		 *
		 */
		List<RecruitMajor> rmjList     = new ArrayList<RecruitMajor>();
		Map<String,RecruitMajor> rmMap = new HashMap<String, RecruitMajor>();
		//选择的招生批次
		RecruitPlan plan  			   = recruitPlanService.get(branchPlanId);
		String gradeId    			   = null==plan.getGrade()?"":plan.getGrade().getResourceid();
		if (ExStringUtils.isNotEmpty(gradeId)) {
			condition.put("grade", gradeId);
			condition.put("brSchool",branchSchoolId);
			//获取所选招生批次对应年级下学习中心所有通过审核的申报批次
			List<BranchSchoolPlan> brspls 	  =  branchSchoolPlanService.getAuditedBrsPlanListForBranSchool(condition);
			for (BranchSchoolPlan brp:brspls) {
				Set<BranchSchoolMajor> bsmSet =   brp.getBranchSchoolMajor();
				//将通过审核的申报专业放入集合中
				for (BranchSchoolMajor bsm :bsmSet) {
					if (ExStringUtils.isNotEmpty(bsm.getIsPassed())&&"Y".equals(bsm.getIsPassed())) {
						rmjList.add(bsm.getRecruitMajor());
					}
				}
			}
			if (!rmjList.isEmpty()) {
				Set<RecruitMajor> rmSet 		  = plan.getRecruitMajor();
				for (RecruitMajor rm1:rmSet) {
					String teachingType1          = null==rm1.getTeachingType()?"":rm1.getTeachingType();			 //选择的招生批次专业的办学模式
					String classic1               = rm1.getClassic().getResourceid();							     //选择的招生批次专业的层次
					String major1                 = rm1.getMajor().getResourceid();  								 //选择的招生批次专业的专业      
					for (RecruitMajor rm2:rmjList) {
						String teachingType2      = null==rm2.getTeachingType()?"":rm2.getTeachingType();			 //学习中心申报的专业的办学模式
						String classic2           = rm2.getClassic().getResourceid();								 //学习中心申报的专业的层次
						String major2             = rm2.getMajor().getResourceid();  								 //学习中心申报的专业的专业      
						//当申报通过的专业跟选择的招生批次专业层次、办学模式、基础专业都相同时则表示该学习中心有这个招生专业的权限
						if (teachingType1.equals(teachingType2) & classic1.equals(classic2) & major1.equals(major2)) {
							rmMap.put(rm1.getResourceid(), rm1);
						}
					}
				}
				List<RecruitMajor> rmList         = new ArrayList(rmMap.values());
				ComparatorRecruitMajor crm    	  = new  ComparatorRecruitMajor();
				if (null!=rmMap.values()) {
					Collections.sort(rmList, crm);
				}
				for (RecruitMajor rm :rmList) {
					JsonModel  json      = new JsonModel();
					json.setKey(rm.getResourceid());
					json.setValue(rm.getRecruitMajorName());
					jsonList.add(json);
				}
			}
		}
		
		renderJson(response, JsonUtils.listToJson(jsonList));
	}
	/**
	 * 校外学习中心允许招生专业列表
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/edu3/branchshoolMajor/allow-branchMajor-list.html")
	public String listBranchSchoolLimitMajor(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		
		User user 			 		  = SpringSecurityHelper.getCurrentUser();
		List<BrschMajorSetting> brSet = new ArrayList<BrschMajorSetting>();
		Map<String,Object> condition  = new HashMap<String, Object>();
		
		if(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())){
			condition.put("brschoolId", user.getOrgUnit().getResourceid());
			condition.put("isOpened", "Y");
			brSet 	= brschMajorSettingService.findBranchSchoolLimitMajorList(condition);
		}
		model.put("brschMajorSetting", brSet);
		
		return "/edu3/recruit/recruitmanage/branchschool-allow-major-list";
	}
}
