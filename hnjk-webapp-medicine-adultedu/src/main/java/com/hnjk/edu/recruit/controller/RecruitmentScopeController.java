package com.hnjk.edu.recruit.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.utils.ExBeanUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.BaseSupportController;
import com.hnjk.edu.recruit.model.RecruitmentScope;
import com.hnjk.edu.recruit.service.IRecruitmentScopeService;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.OrgUnit;
import com.hnjk.security.model.User;
import com.hnjk.security.service.IOrgUnitService;

/** 
 * @author Zik, 广东学苑教育发展有限公司
 * @since 2018年6月28日 下午4:09:17 
 * 
 */
@Controller
public class RecruitmentScopeController extends BaseSupportController {

	private static final long serialVersionUID = 604508182616017754L;

	@Autowired
	@Qualifier("recruitmentScopeService")
	private IRecruitmentScopeService recruitmentScopeService;
	
	@Autowired
	@Qualifier("orgUnitService")
	private IOrgUnitService orgUnitService;
	
	/**
	 * 查询分页列表
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/enrollment/booking/recruitmentScope/list.html")
	public String exeList(HttpServletRequest request,Page objPage,ModelMap model) throws WebException{
		objPage.setOrderBy("unit.resourceid");
		objPage.setOrder(Page.ASC);//设置默认排序方式
		
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件
		String unitId = request.getParameter("unitId");
		String areaScope = request.getParameter("areaScope");
		
		User curUser = SpringSecurityHelper.getCurrentUser();
		if (CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(curUser.getOrgUnit().getUnitType())) {
			unitId = curUser.getOrgUnit().getResourceid();
		}
		
		if(ExStringUtils.isNotEmpty(unitId)) {
			condition.put("unitId", unitId);
		}
		if(ExStringUtils.isNotEmpty(areaScope)) {
			condition.put("areaScope", areaScope);
		}
		
		Page page = recruitmentScopeService.findByCondition(condition, objPage);
		
		model.addAttribute("scopeList", page);
		model.addAttribute("condition", condition);
		
		return "/edu3/enrollmentBook/recruitmentScope-list";
	}
	
	/**
	 * 新增编辑表单
	 * @param resourceid
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/enrollment/booking/recruitmentScope/edit.html")
	public String exeEdit(String resourceid,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		if(ExStringUtils.isNotBlank(resourceid)){ //-----编辑
			RecruitmentScope scope = recruitmentScopeService.load(resourceid);	
			model.addAttribute("scope", scope);
		}else{ //----------------------------------------新增
			model.addAttribute("scope", new RecruitmentScope());			
		}
				
		return "/edu3/enrollmentBook/recruitmentScope-form";
	}
	
	/**
	 * 保存更新表单
	 * @param scope
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/enrollment/booking/recruitmentScope/save.html")
	public void exeSave(RecruitmentScope scope,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			RecruitmentScope persistScope = null;
			
			if(ExStringUtils.isNotBlank(scope.getResourceid())){ //--------------------更新
				persistScope = recruitmentScopeService.get(scope.getResourceid());
			}else{ //-------------------------------------------------------------------保存
				persistScope = new RecruitmentScope();				
			}		
			
			if(ExStringUtils.isNotBlank(scope.getUnitId())){
				OrgUnit unit = orgUnitService.get(scope.getUnitId());
				scope.setUnit(unit);
			}
			ExBeanUtils.copyProperties(persistScope, scope);
			
			recruitmentScopeService.saveOrUpdate(persistScope);
			
			map.put("statusCode", 200);
			map.put("message", "保存成功！");
			map.put("navTabId", "RES_ENROLLMENT_BOOKING_RECRUITMENTSCOPE");
			map.put("reloadUrl", request.getContextPath() +"/edu3/enrollment/booking/recruitmentScope/list.html?resourceid="+persistScope.getResourceid());
		}catch (Exception e) {
			logger.error("保存招生范围出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败:<br/>"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 删除列表对象
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/enrollment/booking/recruitmentScope/delete.html")
	public void exeDelete(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		String resourceid = request.getParameter("resourceid");
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(resourceid)){
				if(resourceid.split("\\,").length >1){//批量删除					
					recruitmentScopeService.batchCascadeDelete(resourceid.split("\\,"));
				}else{//单个删除
					recruitmentScopeService.delete(resourceid);
				}
				map.put("statusCode", 200);
				map.put("message", "删除成功！");				
				map.put("forward", request.getContextPath()+"/edu3/enrollment/booking/recruitmentScope/list.html");
			}
		} catch (Exception e) {
			logger.error("删除招生范围出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
}
