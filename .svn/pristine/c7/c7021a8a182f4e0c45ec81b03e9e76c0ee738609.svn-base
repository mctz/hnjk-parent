package com.hnjk.edu.recruit.controller;

import java.util.ArrayList;
import java.util.Date;
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

import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.utils.ExBeanUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.JsonModel;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.controller.BaseSupportController;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.Classic;
import com.hnjk.edu.basedata.model.Major;
import com.hnjk.edu.basedata.service.IClassicService;
import com.hnjk.edu.basedata.service.IMajorService;
import com.hnjk.edu.recruit.model.BrschMajorSetting;
import com.hnjk.edu.recruit.service.IBrschMajorSettingService;
import com.hnjk.extend.taglib.tree.TreeBuilder;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.security.model.OrgUnit;
import com.hnjk.security.model.OrgUnitExtends;
import com.hnjk.security.service.IOrgUnitService;

/**
 * 招生单位管理.
 * <code>RecruitOrgUnitController</code><p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-10-28 上午10:00:40
 * @see 
 * @version 1.0
 */
@Controller
public class RecruitOrgUnitController extends BaseSupportController{

	private static final long serialVersionUID = 1538815753773167008L;
	
	@Autowired
	@Qualifier("orgUnitService")
	private IOrgUnitService orgUnitService;
	
	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;
	

	@Autowired
	@Qualifier("majorService")
	private IMajorService majorService;
	
	@Autowired
	@Qualifier("classicService")
	private IClassicService classicService;
	
	@Autowired
	@Qualifier("brschMajorSettingService")
	private IBrschMajorSettingService brschMajorSettingService;
	
	/**
	 * 办学单位列表
	 * @param request
	 * @param objPage
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/recruit/recruitmanage/orgunit/list.html")
	public String listRecruitUnit(HttpServletRequest request,Page objPage,  HttpServletResponse response,ModelMap model) throws WebException{
		String isSubPage = ExStringUtils.trimToEmpty(request.getParameter("isSubPage"));//是否子页面请求
		String unitId = ExStringUtils.trimToNull(request.getParameter("unitId"));	
				
		if(ExStringUtils.isNotEmpty(isSubPage) && "y".equalsIgnoreCase(isSubPage)){//如果子页面请求
			objPage.setOrderBy("unitCode");
			objPage.setOrder("asc");
			
			String schoolType = ExStringUtils.trimToEmpty(request.getParameter("schoolType"));			
			String unitName = ExStringUtils.trimToEmpty(request.getParameter("unitName"));
			String status   = ExStringUtils.trimToEmpty(request.getParameter("status"));
			String isChild = ExStringUtils.trimToEmpty(request.getParameter("isChild"));
			String isRoot = ExStringUtils.trimToEmpty(request.getParameter("isRoot"));
			
			Map<String,Object> condition = new HashMap<String,Object>();//查询条件
			if(!ExStringUtils.isEmpty(unitName)) {
				condition.put("unitName", unitName);
			}
			if(!ExStringUtils.isEmpty(status)) {
				condition.put("status", status);
			}
			if(!ExStringUtils.isEmpty(schoolType)) {
				condition.put("schoolType", schoolType);
			}
			if(!"y".equals(isRoot)){
				if(!ExStringUtils.isEmpty(unitId)) {
					condition.put("unitId", unitId);
				}
				if(!ExStringUtils.isEmpty(isChild)) {
					condition.put("isChild",isChild);
				}
			}
			
			
			condition.put("unitType", CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue());
			
			Page orgUnitList=orgUnitService.findOrgByConditionByHql(condition, objPage);
	
			model.addAttribute("condition", condition);
			model.addAttribute("orgUnitList", orgUnitList);	
			
			return "/edu3/recruit/recruitmanage/branchschool-list-sub";
		}else{
			String defaultCheckedValue = ExStringUtils.trimToEmpty(request.getParameter("checkedValue"));			
			try {
				List<OrgUnit> orgList = orgUnitService.findOrgTree(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.root").getParamValue());
				String jsonString = JsonUtils.objectToJson(TreeBuilder.buildTreeById(orgList,CacheAppManager.getSysConfigurationByCode("orgunit.brschool.root").getParamValue(),defaultCheckedValue));
				model.addAttribute("unitTree", jsonString);
			} catch (Exception e) {
				logger.error("输出办学单位列表-组织树出错:"+e.fillInStackTrace());
			}
			
			
			return "/edu3/recruit/recruitmanage/branchschool-list";
		}
		
	}
	
	/**
	 * 办学单位编辑表单
	 * @param resourceid
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/recruit/recruitmanage/orgunit/input.html")
	public String addRecruitUnit(String resourceid,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		String unitId = ExStringUtils.trimToEmpty(request.getParameter("unitId"));
		Map<String,Object> condition = new HashMap<String, Object>();
		if(ExStringUtils.isNotBlank(resourceid)){
			condition.put("brschoolId", resourceid);
			OrgUnit orgunit = orgUnitService.load(resourceid);	
			model.addAttribute("orgunit", orgunit);
			
			if(ExStringUtils.isEmpty(unitId) && null!=orgunit.getParent()){
				unitId = orgunit.getParent().getResourceid();
			}	
			
			List<BrschMajorSetting> list = brschMajorSettingService.findBranchSchoolLimitMajorList(condition);
			model.addAttribute("brschMajorSettingList", list);
			
		}else{
			OrgUnit orgUnit =  new OrgUnit();
			orgUnit.setUnitType("brSchool");
			model.addAttribute("orgunit",orgUnit);			
		}
		OrgUnit parentOrg = orgUnitService.load(unitId);	
		//orgunit.brschool.root 校外学习中心根组织
		model.addAttribute("rootCode", CacheAppManager.getSysConfigurationByCode("orgunit.brschool.root").getParamValue());
		model.addAttribute("parentOrg", parentOrg);
		
		return "/edu3/recruit/recruitmanage/branchschool-form";
	}
	
	/**
	 * 保存办学单位
	 * @param orgUnit
	 * @param request
	 * @param response
	 * @param model
	 * @throws WebException
	 */
	@RequestMapping("/edu3/recruit/recruitmanage/orgunit/save.html")
	public void saveRecruitUnit(OrgUnit orgUnit,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		
		Map<String,Object> map = new HashMap<String, Object>();
		
		try {
			String teachingMan	= ExStringUtils.trimToEmpty(request.getParameter("teachingMan"));//教务员姓名
			String teachingTel	= ExStringUtils.trimToEmpty(request.getParameter("teachingTel"));//教务员电话
			String teachingEmail= ExStringUtils.trimToEmpty(request.getParameter("teachingEmail"));//教务员e-mail
			String teachingQQ	= ExStringUtils.trimToEmpty(request.getParameter("teachingQQ"));//教务员QQ
			String majorDirector= ExStringUtils.trimToEmpty(request.getParameter("majorDirector"));//专业主任信息
			String[] exCodes = {OrgUnitExtends.UNIT_EXTENDCODE_TEACHINGMAN,OrgUnitExtends.UNIT_EXTENDCODE_TEACHINGTEL,OrgUnitExtends.UNIT_EXTENDCODE_TEACHINGEMAIL,OrgUnitExtends.UNIT_EXTENDCODE_TEACHINGQQ,OrgUnitExtends.UNIT_EXTENDCODE_MAJORDIRECTOR};
			String[] exValues = {teachingMan,teachingTel,teachingEmail,teachingQQ,majorDirector};
			
			if(ExStringUtils.isNotEmpty(orgUnit.getResourceid())){ //------------编辑
				
				OrgUnit persistOrgUnit = orgUnitService.get(orgUnit.getResourceid());
				orgUnit.setOrgUnitExtends(persistOrgUnit.getOrgUnitExtends());
				ExBeanUtils.copyProperties(persistOrgUnit, orgUnit);
				
				if(ExStringUtils.isNotEmpty(orgUnit.getParentId())){
					OrgUnit parentOrg = orgUnitService.get(orgUnit.getParentId());
					parentOrg.setIsChild(Constants.BOOLEAN_YES);
					persistOrgUnit.setParent(parentOrg);
					persistOrgUnit.setUnitLevel(parentOrg.getUnitLevel()+1);
				}
						
				OrgUnitExtends unitExtends = null;	
				for (int i = 0; i < exCodes.length; i++) {
					if(null != persistOrgUnit.getOrgUnitExtends().get(exCodes[i])){
						unitExtends  = persistOrgUnit.getOrgUnitExtends().get(exCodes[i]);
						unitExtends.setExValue(exValues[i]);
					}else{
						unitExtends  = new OrgUnitExtends(exCodes[i], exValues[i], persistOrgUnit);
					}				
					persistOrgUnit.getOrgUnitExtends().put(exCodes[i], unitExtends);
				}
				orgUnitService.updateOrgUnit(persistOrgUnit);						
				
				
			}else{ //----------------------------------------------------------新增
				OrgUnit org = orgUnitService.findUniqueByProperty("unitCode", orgUnit.getUnitCode());
				if(null != org){
					throw new WebException("存在相同编码的组织单位，请重新输入！");
				}
				if(ExStringUtils.isNotEmpty(orgUnit.getParentId())){
					OrgUnit parentOrg = orgUnitService.load(orgUnit.getParentId());
					parentOrg.setIsChild(Constants.BOOLEAN_YES);
					orgUnit.setParent(parentOrg);
					orgUnit.setUnitLevel(parentOrg.getUnitLevel()+1);
				}
				orgUnit.setIsChild(Constants.BOOLEAN_NO);
				orgUnit.setCreateTime(new Date());
							
				OrgUnitExtends unitExtends = null;	
				for (int i = 0; i < exCodes.length; i++) {
					if(null != orgUnit.getOrgUnitExtends().get(exCodes[i])){
						unitExtends  = orgUnit.getOrgUnitExtends().get(exCodes[i]);
						unitExtends.setExValue(exValues[i]);
					}else{
						unitExtends  = new OrgUnitExtends(exCodes[i], exValues[i], orgUnit);
					}				
					orgUnit.getOrgUnitExtends().put(exCodes[i], unitExtends);
				}
				
				orgUnitService.addOrgUnit(orgUnit);
			}
			saveRecuitLimitMajor(request,orgUnit);//保存专业限制
			map.put("statusCode", 200);
			map.put("message", "保存成功！");			
			map.put("reloadUrl", request.getContextPath() +"/edu3/recruit/recruitmanage/orgunit/input.html?resourceid="+orgUnit.getResourceid());
		} catch (Exception e) {				
			logger.error("保存用户错误:"+e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败:<br/>"+e.getMessage());
		}		
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	private void saveRecuitLimitMajor(HttpServletRequest request,OrgUnit schoolOrgUnit)throws Exception{
		Map<String ,Object> map = new HashMap<String, Object>();
		if(null != schoolOrgUnit){
											
				String [] teachingTypes   = request.getParameterValues("teachingType");
				String [] majorSettingIds = request.getParameterValues("brschMajorSettingId");
				String [] classics        = request.getParameterValues("classic");
				String [] majors          = request.getParameterValues("major");
				String [] limitNums       = request.getParameterValues("limitNum");
				String [] isOpeneds       = request.getParameterValues("isOpened");
				String [] memos  		  = request.getParameterValues("memo");

				map.put("brschool", schoolOrgUnit.getResourceid());
				String delSQL     = " delete from EDU_RECRUIT_BRSCHMAJORSET setting where setting.isDeleted=0 ";
					   delSQL    += " and setting.BRSCHOOLID=:brschool ";
				String checkExist = " select count(setting.resourceid) from EDU_RECRUIT_BRSCHMAJORSET setting where setting.isDeleted=0";
					   checkExist+= " and setting.BRSCHOOLID=:brschool and setting.CLASSICID=:classicid ";
					   checkExist+= " and setting.MAJORID=:majorId and setting.SCHOOLTYPE=:teachingType";
				//检查提交的数据是否已存在
				if (null!=majorSettingIds && majorSettingIds.length>0) {
					for (int j = 0; j < majorSettingIds.length; j++) {
						
						String majorsId  = majors[j];
						String type      = teachingTypes[j];
						String classicId = classics[j];
						
						map.put("majorId", majorsId);
						map.put("teachingType", type);
						map.put("classicid", classicId);
						
						Long counts      = baseSupportJdbcDao.getBaseJdbcTemplate().findForLong(checkExist, map);
						if (counts>1) {
							throw new Exception("允许的招生专业有重复数据!");
						}
					}
				}
					   
				
				//清除原有专业（每次提交都清除，再根据页面中提交的数据重新设置）
				baseSupportJdbcDao.getBaseJdbcTemplate().executeForMap(delSQL, map);
				if (null!=majorSettingIds && majorSettingIds.length>0) {
					for (int i = 0; i < majorSettingIds.length; i++) {			
						
						String type 	 = teachingTypes[i];
						String classicId = classics[i];
						String majorsId  = majors[i];
						String limitNum  = limitNums[i];
						String isOpened  = isOpeneds[i];
						String memo      = memos[i];
						
						Major major      = majorService.get(majorsId);
						Classic classic  = classicService.get(classicId);
						
						BrschMajorSetting setting = new BrschMajorSetting();
						OrgUnit orgUnit = orgUnitService.get(schoolOrgUnit.getResourceid());
						setting.setSchoolType(type);
						setting.setBrSchool(orgUnit);
						setting.setClassic(classic);
						setting.setMajor(major);
						setting.setIsOpened(isOpened);
						setting.setLimitNum(Integer.parseInt(limitNum));
						setting.setMemo(memo);
						
						brschMajorSettingService.save(setting);
					}
				}
				
		}
	}
	
	/**
	 * 删除办学单位
	 * @param request
	 * @param response
	 * @param model
	 * @throws WebException
	 */
	@RequestMapping("/edu3/recruit/recruitmanage/orgunit/delete.html")
	public void deleteRecruitUnit(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		String resourceid = ExStringUtils.trimToEmpty(request.getParameter("resourceid"));
		Map<String ,Object> map = new HashMap<String, Object>();	
		try {
			if(!ExStringUtils.isEmpty(resourceid)){
				if(resourceid.split("\\,").length >1){//批量删除					
					orgUnitService.batchCascadeDelete(resourceid.split("\\,"));
				}else{//单个删除
					orgUnitService.delete(resourceid);
				}
				map.put("statusCode", 200);
				map.put("message", "删除成功！");	
				map.put("localArea", "_branchschoolListContent");
				//map.put("forward", request.getContextPath()+"/edu3/system/org/unit/list.html");
			}	

		} catch (Exception e) {
			logger.error("删除用户出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错:<br/>"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 办学单位管理中设置招生专业限制过滤专业
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/framework/branschoollimit/getmajor.html")
	public void queryMajorForBranschoolLimit(HttpServletRequest request,HttpServletResponse response) throws WebException{
		List<JsonModel> list  = new ArrayList<JsonModel>();
		String exceptids      = ExStringUtils.trimToEmpty(request.getParameter("exceptids")); 
		List<Major> majorList = majorService.findMajorForBranSchoolLimt(exceptids);
		for (Major m:majorList) {
			JsonModel jsonModel= new JsonModel();
			jsonModel.setKey(m.getResourceid());
			jsonModel.setValue(m.getMajorName());
			if (m.getMajorName().length()>9) {
				jsonModel.setName(m.getMajorName().substring(0, 9)+"...");
			}else {
				jsonModel.setName(m.getMajorName());
			}
			list.add(jsonModel);
		}
		renderJson(response,JsonUtils.listToJson(list));
	}
}
