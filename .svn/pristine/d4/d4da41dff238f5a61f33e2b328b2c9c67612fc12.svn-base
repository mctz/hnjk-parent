package com.hnjk.edu.recruit.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.hnjk.edu.basedata.model.Classic;
import com.hnjk.edu.basedata.model.Major;
import com.hnjk.edu.basedata.service.IClassicService;
import com.hnjk.edu.basedata.service.IMajorService;
import com.hnjk.edu.recruit.model.RecruitMajorSetting;
import com.hnjk.edu.recruit.service.IRecruitMajorSettingService;
import com.hnjk.edu.teaching.model.TeachingPlan;
import com.hnjk.edu.teaching.service.ITeachingPlanService;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.Dictionary;

/**
 * 招生专业设置Controller
 * @author luof
 * @since： 2011-4-7 下午15:21:30
 * @see 
 * @version 1.0
 */
@Controller
public class RecruitMajorSettingController extends BaseSupportController{
	
	private static final long serialVersionUID = 4344420557405912643L;

	@Autowired
	@Qualifier("recruitMajorSettingService")
	private IRecruitMajorSettingService recruitMajorSettingService;
	
	@Autowired
	@Qualifier("majorService")
	private IMajorService majorService;
	
	@Autowired
	@Qualifier("classicService")
	private IClassicService classicService;
	
	@Autowired
	@Qualifier("teachingplanservice")
	private ITeachingPlanService teachingPlanService;
	
	/**
	 * 招生专业设置-列表
	 * @param request
	 * @param response
	 * @param model
	 * @param page
	 * @return
	 */
	@RequestMapping("/edu3/recruit/recruitmajorsetting/recruitmajorsetting-list.html")
	public String  findRecruitMajorSettingList(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		
		List<Dictionary> teachingType = CacheAppManager.getChildren("CodeTeachingType");
		List<Classic> classic     	  = classicService.findByHql("from "+Classic.class.getSimpleName()+"  c where c.isDeleted=0");
		
		model.addAttribute("teachingType",teachingType);
		model.addAttribute("classic",classic);
		
		return "/edu3/recruit/recruitmanage/recruitmajorsetting-list";
	}
	
	/**
	 * 招生专业设置-表单
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/recruit/recruitmajorsetting/recruitmajorsetting-form.html")
	public String recruitMajorSettingModify(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		
		Map<String,Object> condition 		  = new HashMap<String, Object>(); 
		String teachingTypeValue   		 	  = ExStringUtils.trimToEmpty(request.getParameter("teachingTypeValue"));
		String classicId		  		 	  = ExStringUtils.trimToEmpty(request.getParameter("classicId"));
		
		if (ExStringUtils.isNotEmpty(classicId)) 	{      
			condition.put("classic",classicId);
			Classic classic = classicService.get(classicId);
			model.addAttribute("classicName", classic.getClassicName());
		}
		if (ExStringUtils.isNotEmpty(teachingTypeValue)) {
			condition.put("teachingType",teachingTypeValue);
		}
		
		String hql           				  = "from "+Major.class.getSimpleName()+" major where  major.isDeleted=0 ";
		if ("net".equals(teachingTypeValue) || "direct".equals(teachingTypeValue)) {
			hql+= " and major.isAdult='N'";
		}else if ("face".equals(teachingTypeValue)) {
			hql+= " and major.isAdult='Y'";
		}
		List<Major> majorList 				  = majorService.findByHql(hql);
		List<RecruitMajorSetting> settingList = recruitMajorSettingService.findRecruitMajorSettingByCondition(condition);
		
		model.addAttribute("settingList", settingList);
		model.addAttribute("majorList", majorList);
		model.addAttribute("teachingType", teachingTypeValue);		
		model.addAttribute("classic", classicId);
		
		return "/edu3/recruit/recruitmanage/recruitmajorsetting-form";
	}
	/**
	 * 招生专业设置-保存
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/recruit/recruitmajorsetting/recruitmajorsetting-save.html")
	public void recruitMajorSettingAdd(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		
		Map<String,Object> map = new HashMap<String, Object>();
		String[] majors  	   = request.getParameterValues("majorms2side__dx");
		String classic   	   = ExStringUtils.trimToEmpty(request.getParameter("classic"));
		String teachingType    = ExStringUtils.trimToEmpty(request.getParameter("teachingType"));
		String classicName     = ExStringUtils.trimToEmpty(request.getParameter("classicName"));
		String teachingTypeName= ExStringUtils.trimToEmpty(request.getParameter("teachingTypeName"));
		
		if (ExStringUtils.isNotEmpty(teachingType)) {
			map.put("teachingType", teachingType);
		}
		if (ExStringUtils.isNotEmpty(classic)) {
			map.put("classic", classic);
		}
		try {
			String hql 	      = " delete from "+RecruitMajorSetting.class.getSimpleName()+
								" setting where setting.classic.resourceid=:classic and setting.teachingType=:teachingType";
			
			if (null!=majors && majors.length>0) {
				boolean validate = true;
				List<RecruitMajorSetting> majorSettings = new ArrayList<RecruitMajorSetting>();
				Classic c 		  = classicService.load(classic);
				for (int i = 0; i < majors.length; i++) {
					Major major   = majorService.load(majors[i]);
					RecruitMajorSetting setting = new RecruitMajorSetting();
					setting.setClassic(c);
					setting.setMajor(major);
					setting.setTeachingType(teachingType);
					//判断是否存在基础教学计划
					Map<String, Object> condition = new HashMap<String, Object>();
					condition.put("classic", c.getResourceid());
					condition.put("major", major.getResourceid());
					condition.put("schoolType", teachingType);
					
					List<TeachingPlan> teachingPlans = teachingPlanService.findTeachingPlanByCondition(condition);
					if(teachingPlans!=null && teachingPlans.size()>0){
						//recruitMajorSettingService.save(setting);
						majorSettings.add(setting);
					}else {
						validate = false;
						throw new Exception("请先添加【"+c.getClassicName()+"-"+teachingTypeName+"-"+major.getMajorName()+"】基础教学计划！");
					}
				}
				if(validate){
					recruitMajorSettingService.getGeneralHibernateDao().executeHQL(hql, map);
					recruitMajorSettingService.batchSaveOrUpdate(majorSettings);
				}
				map.put("statusCode", 200);
				map.put("message", "保存成功！");
				map.put("reloadUrl", request.getContextPath()
									 +"/edu3/recruit/recruitmajorsetting/recruitmajorsetting-form.html"
									 +"?classicId="+classic+"&classicName="+classicName
									 +"&teachingTypeValue="+teachingType+"&teachingTypeName="+teachingTypeName);
			}else {
				
				map.put("statusCode", 200);
				map.put("message", "未设置专业!");
				map.put("reloadUrl", request.getContextPath()
									 +"/edu3/recruit/recruitmajorsetting/recruitmajorsetting-form.html"
									 +"?classicId="+classic+"&classicName="+classicName
									 +"&teachingTypeValue="+teachingType+"&teachingTypeName="+teachingTypeName);
			}
			
		} catch (Exception e) {
			logger.error("保存招生专业设置出错：{}"+e.fillInStackTrace());
			 map.put("statusCode", 300);
			 map.put("message", e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	@RequestMapping("/edu3/recruit/recruitmajorsetting/query-recruitmajorsetting.html")
	public void queryRecruitMajorSetting(HttpServletRequest request,HttpServletResponse response){
		
		Map<String,Object> condition = new HashMap<String, Object>(); 
		String classic   	   		 = ExStringUtils.trimToEmpty(request.getParameter("classic"));
		String teachingType    		 = ExStringUtils.trimToEmpty(request.getParameter("teachingType"));
		String exceptMajor           = ExStringUtils.trimToEmpty(request.getParameter("exceptMajor"));
		String recruitPlanId         = ExStringUtils.trimToEmpty(request.getParameter("recruitPlanId"));
		String [] exceptIds		     = null;
		if (ExStringUtils.isNotEmpty(exceptMajor)) {
			exceptIds				 = exceptMajor.split(",");
		}
		
		if (ExStringUtils.isNotEmpty(teachingType)) {
			condition.put("teachingType", teachingType);
		}
		if (ExStringUtils.isNotEmpty(classic)) {
			condition.put("classic", classic);
		}
//		if (ExStringUtils.isNotEmpty(recruitPlanId)) 		condition.put("recruitPlanId",recruitPlanId);
		
		List<RecruitMajorSetting> settingList = recruitMajorSettingService.findRecruitMajorSettingListForAddMajor(condition);
		List<JsonModel> jsonList     		  = new ArrayList<JsonModel>();
		
		if (null!=exceptIds && exceptIds.length>0) {
			for (int i = 0; i < exceptIds.length; i++) {
				condition.put(exceptIds[i].trim(), exceptIds[i].trim());
			}
		}
		
		for (int i = 0; i < settingList.size(); i++) {
			if (condition.containsKey(settingList.get(i).getMajor().getResourceid())) {
 				continue;
			}else {
				JsonModel model = new JsonModel();
				Major _major = settingList.get(i).getMajor();
				model.setKey(_major.getResourceid());
				model.setValue(_major.getMajorCode()+"-"+_major.getMajorName());
				jsonList.add(model);
			}
		}
		
		renderJson(response,JsonUtils.listToJson(jsonList));
	}
	
}	
