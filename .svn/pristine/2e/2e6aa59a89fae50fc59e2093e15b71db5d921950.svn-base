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
import org.springframework.web.bind.annotation.RequestMapping;

import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.JsonModel;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.controller.BaseSupportController;
import com.hnjk.edu.basedata.model.Classic;
import com.hnjk.edu.basedata.service.IClassicService;
import com.hnjk.edu.recruit.model.RecruitMajor;
import com.hnjk.edu.recruit.model.RecruitPlan;
import com.hnjk.edu.recruit.service.IRecruitMajorService;
import com.hnjk.edu.recruit.service.IRecruitPlanService;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.Dictionary;

/**
 * 
 * <code>DynamicMenuController</code><p>
 * 招生模块连动菜单.
 * @author：广东学苑教育发展有限公司
 * @since： 2011-4-22 下午18:19:30
 * @see 
 * @version 1.0
 */
@Controller
public class RecruitModelDynamicMenuController extends BaseSupportController{
	
	@Autowired
	@Qualifier("recruitPlanService")
	private IRecruitPlanService recruitPlanService;
	
	@Autowired
	@Qualifier("recruitMajorService")
	private IRecruitMajorService recruitMajorService;
	
	@Autowired
	@Qualifier("classicService")
	private IClassicService classicService;
	
	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;
	

	/**
	 * 根据招生批次、学习模式获取招生专业
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/recruit/dynamicmenu/recruitplan-recruitmajor.html")
	public void getRecruitMajor(HttpServletRequest request,HttpServletResponse response) {
		
		Map<String,Object> condition = new HashMap<String, Object>();
		List<JsonModel>  modelList   = new ArrayList<JsonModel>();
		String recruitPlan  		 = ExStringUtils.trimToEmpty(request.getParameter("recruitPlan"));
		String teachingType 		 = ExStringUtils.trimToEmpty(request.getParameter("teachingType"));
		
		if (ExStringUtils.isNotEmpty(recruitPlan)) {
			condition.put("recruitPlan", recruitPlan);
		}
		if (ExStringUtils.isNotEmpty(teachingType)) {
			condition.put("teachingType", teachingType);
		}
		
		List<RecruitMajor> list 	 = recruitMajorService.findMajorByCondition(condition);
		if (null!=list && !list.isEmpty()) {
			
			for (RecruitMajor major:list) {
				
				JsonModel model      = new JsonModel();
				model.setKey(major.getResourceid());
				model.setValue(major.getRecruitMajorName());
				if (major.getRecruitMajorName().length()>9) {
					model.setName(major.getRecruitMajorName().substring(0, 9)+"...");
				}else {
					model.setName(major.getRecruitMajorName());
				}
				modelList.add(model);
			}
		}
		
		renderJson(response,JsonUtils.listToJson(modelList));
	}
	/**
	 * 根据招生批次获取学习模式、招生专业
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/recruit/dynamicmenu/recruitplan-teachingtype-recruitmajor.html")
	public void getTeachingTypeAndRecruitMajor(HttpServletRequest request,HttpServletResponse response){
		
		Map<String,Object> condition  = new HashMap<String, Object>();
		String recruitPlan  		  = ExStringUtils.trimToEmpty(request.getParameter("recruitPlan"));
		String teachingType  		  = ExStringUtils.trimToEmpty(request.getParameter("teachingType"));
		String queryType              = ExStringUtils.trimToEmpty(request.getParameter("queryType")); 
		List<JsonModel> typeList      = new ArrayList<JsonModel>();
		List<JsonModel> majorList     = new ArrayList<JsonModel>();
		
		if (ExStringUtils.isNotEmpty(recruitPlan)) {
			condition.put("recruitPlan", recruitPlan);
		}
		if (ExStringUtils.isNotEmpty(teachingType)) {
			condition.put("teachingType", teachingType);
		}
		
		if (ExStringUtils.isNotEmpty(recruitPlan)) {

			if ("teachingType".equals(queryType)) {
				
				RecruitPlan plan          = recruitPlanService.load(recruitPlan);
				String planType       	  = plan.getTeachingType();
				List<Dictionary> types	  = CacheAppManager.getChildren("CodeTeachingType");
				 
				for (Dictionary dic:types) {
					if (ExStringUtils.isNotEmpty(planType)&&planType.indexOf(dic.getDictValue())<0) {
						continue;
					}else {
						JsonModel m  	  = new JsonModel();
						m.setKey(dic.getDictValue());
						m.setValue(dic.getDictName());
						
						typeList.add(m);
					}
				}
				
				condition.put("typeList", typeList);
			}else if ("recruitMajor".equals(queryType)) {
				
				List<RecruitMajor> list   = recruitMajorService.findMajorByCondition(condition);
				if (null!=list && !list.isEmpty()) {
					
					for (RecruitMajor major:list) {
						
						JsonModel model      = new JsonModel();
						model.setKey(major.getResourceid());
						model.setValue(major.getRecruitMajorName());
						if (major.getRecruitMajorName().length()>9) {
							model.setName(major.getRecruitMajorName().substring(0, 9)+"...");
						}else {
							model.setName(major.getRecruitMajorName());
						}
						majorList.add(model);
					}
				}
				
				condition.put("majorList", majorList);
			}
			
		}
		renderJson(response,JsonUtils.mapToJson(condition));
	}
}
