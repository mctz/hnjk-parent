package com.hnjk.edu.recruit.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.cache.EhCacheManager;
import com.hnjk.core.foundation.utils.ExCollectionUtils;
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.GUIDUtils;
import com.hnjk.core.foundation.utils.HttpHeaderUtils;
import com.hnjk.core.foundation.utils.JsonModel;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.FileUploadAndDownloadSupportController;
import com.hnjk.core.support.base.model.AttachInfo;
import com.hnjk.core.support.context.Constants;
import com.hnjk.core.support.context.SystemContextHolder;
import com.hnjk.edu.basedata.model.Classic;
import com.hnjk.edu.basedata.model.Major;
import com.hnjk.edu.basedata.service.IClassicService;
import com.hnjk.edu.basedata.service.IMajorService;
import com.hnjk.edu.recruit.model.BranchSchoolMajor;
import com.hnjk.edu.recruit.model.BranchSchoolPlan;
import com.hnjk.edu.recruit.model.BrschMajorSetting;
import com.hnjk.edu.recruit.model.EnrolleeInfo;
import com.hnjk.edu.recruit.model.RecruitMajor;
import com.hnjk.edu.recruit.model.RecruitMajorSetting;
import com.hnjk.edu.recruit.model.RecruitPlan;
import com.hnjk.edu.recruit.service.IBranchSchoolPlanService;
import com.hnjk.edu.recruit.service.IBrschMajorSettingService;
import com.hnjk.edu.recruit.service.IEnrolleeInfoService;
import com.hnjk.edu.recruit.service.IRecruitMajorService;
import com.hnjk.edu.recruit.service.IRecruitMajorSettingService;
import com.hnjk.edu.recruit.service.IRecruitPlanService;
import com.hnjk.edu.recruit.vo.RecruitMajorVo;
import com.hnjk.extend.plugin.excel.service.IExportExcelService;
import com.hnjk.extend.plugin.excel.service.IImportExcelService;
import com.hnjk.platform.system.UserOperationLogsHelper;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.Dictionary;
import com.hnjk.platform.system.model.UserOperationLogs;
import com.hnjk.platform.system.service.IDictionaryService;
import com.hnjk.platform.taglib.JstlCustomFunction;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.cache.CacheSecManager;
import com.hnjk.security.model.OrgUnit;
import com.hnjk.security.model.User;
import com.hnjk.security.service.IOrgUnitService;

/**
 * 招生专业Controller<p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2010-3-11 上午10:59:06
 * @see
 * @version 1.0
 */
@Controller
public class ReruitMajorController extends FileUploadAndDownloadSupportController {


	private static final long serialVersionUID = 2645620699987523556L;
	
	@Autowired
	@Qualifier("recruitMajorService")
	private IRecruitMajorService recruitMajorService;
	
	@Autowired
	@Qualifier("recruitPlanService")
	private IRecruitPlanService recruitPlanService;
	
	@Autowired
	@Qualifier("majorService")
	private IMajorService majorService;
	
	@Autowired
	@Qualifier("classicService")
	private IClassicService classicService;
	
	@Autowired
	@Qualifier("branchSchoolPlanService")
	private IBranchSchoolPlanService branchSchoolPlanService;
	
	@Autowired
	@Qualifier("brschMajorSettingService")
	private IBrschMajorSettingService brschMajorSettingService;
	
//	@Autowired
//	@Qualifier("examSubjectSetService")
//	private IExamSubjectSetService examSubjectSetService;
	
//	@Autowired
//	@Qualifier("examSubjectService")
//	private IExamSubjectService examSubjectService;
	
	@Autowired
	@Qualifier("orgUnitService")
	private IOrgUnitService orgUnitService;
	
	@Autowired
	@Qualifier("importExcelService")
	private IImportExcelService importExcelService;
	
	@Autowired
	@Qualifier("exportExcelService")
	private IExportExcelService exportExcelService;
	
	@Autowired
	@Qualifier("dictService")
	private IDictionaryService dictionaryService;
	
	@Autowired
	@Qualifier("recruitMajorSettingService")
	private IRecruitMajorSettingService recruitMajorSettingService;
	
	@Autowired
	@Qualifier("enrolleeInfoService")
	private IEnrolleeInfoService enrolleeInfoService;
	
	/**
	 * 获取所有招生专业列表
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/recruit/recruitplan/major-list.html")
	public String listRecruitMajor(String planid,String recruitMajorName,
	           Page objPage, ModelMap model,HttpServletRequest request) throws WebException {
		//排序
		objPage.setOrder(Page.ASC);
		objPage.setOrderBy("recruitMajorCode");
		String brSchoolid = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));
		String status = ExStringUtils.trimToEmpty(request.getParameter("status"));
		String classic= ExStringUtils.trimToEmpty(request.getParameter("classicid"));
		String major = ExStringUtils.trimToEmpty(request.getParameter("major"));
		String teachingType =ExStringUtils.trimToEmpty(request.getParameter("teachingType"));
		
		//查询条件
		Map<String,Object> condition = new HashMap<String,Object>();
		condition.put("planid", planid);
		if(!StringUtils.isEmpty(recruitMajorName)){
			condition.put("recruitMajorName", recruitMajorName);
			model.addAttribute("recruitMajorName", recruitMajorName);
		}
		if(!StringUtils.isEmpty(brSchoolid)){
			condition.put("brSchoolid", brSchoolid);
			model.addAttribute("branchSchool", brSchoolid);
		}
		if(!StringUtils.isEmpty(status)){
			condition.put("status", status);
			model.addAttribute("status", status);
		}
		if(!StringUtils.isEmpty(classic)){
			condition.put("classic", classic);
			model.addAttribute("classicid", classic);
		}
		if(!StringUtils.isEmpty(teachingType)){
			condition.put("teachingType", teachingType);
			model.addAttribute("teachingType", teachingType);
		}
		if(!StringUtils.isEmpty(major)){
			condition.put("major", major);
			model.addAttribute("major", major);
		}
		
		Page page = recruitMajorService.findMajorByCondition(condition, objPage);
		model.addAttribute("majorlist", page);
		RecruitPlan recruitPlan = recruitPlanService.get(planid);
		model.addAttribute("planid", recruitPlan.getResourceid());
		model.addAttribute("planName", recruitPlan.getRecruitPlanname());
		
		return "/edu3/recruit/recruitplan/recruitmajor-list";
	}
	
	/**
	 * 获取招生计划专业列表
	 * @param planid
	 * @param recruitMajorName
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	
	@RequestMapping("/edu3/recruit/recruitplan/planMajor.html")
	public String listRecruitMajorForPlan(String planid,String recruitMajorName,String classic,
	           Page objPage, ModelMap model) throws WebException {
		//排序
		objPage.setOrder(Page.ASC);
		objPage.setOrderBy("recruitMajorCode");
		
		//查询条件
		Map<String,Object> condition = new HashMap<String,Object>();
		condition.put("planid", planid);
		if(!StringUtils.isEmpty(recruitMajorName)){
			condition.put("recruitMajorName", recruitMajorName);
			model.addAttribute("recruitMajorName", recruitMajorName);
		}
		if (!StringUtils.isEmpty(classic)) {
			condition.put("classic", classic);
			model.addAttribute("classic", classic);
		}
		Page page = recruitMajorService.findMajorByCondition(condition, objPage);
		model.addAttribute("majorlist", page);
		RecruitPlan recruitPlan = recruitPlanService.get(planid);
		model.addAttribute("planid", recruitPlan.getResourceid());
		model.addAttribute("planName", recruitPlan.getRecruitPlanname());
		
		return "/edu3/recruit/recruitplan/recruitmajor-view";
	}
	
	/**
	 * 招生专业表单
	 * 
	 * @param resourceid
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/recruit/recruitplan/major-input.html")
	public String addRecruitMajor(String resourceid, String planid, ModelMap model) throws WebException {
		RecruitMajor major = null;
		if (StringUtils.isNotEmpty(resourceid)) {
			major		   = recruitMajorService.get(resourceid);
			model.addAttribute("recruitmajor", major);
			if (major.getRecruitPlan() != null) {
				model.addAttribute("planid", major.getRecruitPlan().getResourceid());
			}
			model.addAttribute("planName", major.getRecruitPlan().getRecruitPlanname());
			
			return "/edu3/recruit/recruitplan/recruitmajor-form-alter";
		}else{
			
			List<Dictionary> teachingType = CacheAppManager.getChildren("CodeTeachingType");
			List<Classic> classic     	  = classicService.getAll();
			RecruitPlan recruitPlan 	  = recruitPlanService.get(planid);
			String filtrationStr          = ExStringUtils.defaultIfEmpty(recruitPlan.getTeachingType(), ""); 
			//List<ExamSubjectSet> setList  = examSubjectSetService.findByCriteria(Restrictions.eq("isDeleted", 0));
			
			model.addAttribute("teachingType", teachingType);
			model.addAttribute("planid", recruitPlan.getResourceid());
			model.addAttribute("planName", recruitPlan.getRecruitPlanname());
			model.addAttribute("filtrationStr", filtrationStr);
			//model.put("examSubjectSet",setList);
			//遍历组织单位缓存
			StringBuffer bf = new StringBuffer();
			@SuppressWarnings("unchecked")
			List<OrgUnit> orgList = (List<OrgUnit>)EhCacheManager.getCache(CacheSecManager.CACHE_SEC_ORGS).get(CacheSecManager.CACHE_SEC_ORGS);
			if(null != orgList && orgList.size()>0){
				for(OrgUnit orgUnit : orgList){
//					if("brschool".equals(scope)){
						if(!CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(orgUnit.getUnitType())){//过滤非校外学习中心的
							continue;
						}
//					}
					bf.append("<option value='"+orgUnit.getResourceid()+"'");
//					if(ExStringUtils.isNotEmpty(defaultValue) && defaultValue.equals(orgUnit.getResourceid())){
//						bf.append(" selected ");
//					}
					
//					if("name".equalsIgnoreCase(ExStringUtils.trimToEmpty(displayType))){
//						bf.append(">"+orgUnit.getUnitName()+"</option>");
//					} else {
						bf.append(">"+orgUnit.getUnitCode()+"-"+orgUnit.getUnitName()+"</option>");
//					}						
				}
			}
			model.addAttribute("brschools", bf.toString());
			return "/edu3/recruit/recruitplan/recruitmajor-form-add";
		}
		
	}
	
	/**
	 * 保存招生专业
	 * @param resourceid
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/edu3/recruit/recruitplan/savemajor.html")
	public void saveRecruitMajor(String resourceid, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws WebException {
	
		Map<String, Object> map = new HashMap<String, Object>();
		
		String planid 			= ExStringUtils.trimToEmpty(request.getParameter("planid"));	
		String classicid 		= ExStringUtils.trimToEmpty(request.getParameter("classicid"));	
		String majorid 			= ExStringUtils.trimToEmpty(request.getParameter("majorid"));	
		String studyperiod 		= ExStringUtils.trimToEmpty(request.getParameter("studyperiod"));	
		String lowerNum 		= ExStringUtils.trimToEmpty(request.getParameter("lowerNum"));	
		String limitNum 		= ExStringUtils.trimToEmpty(request.getParameter("limitNum"));	
		String teachingType     = ExStringUtils.trimToEmpty(request.getParameter("teachingType"));
		String brSchoolid     = ExStringUtils.trimToEmpty(request.getParameter("brSchoolid"));
		String tuitionFee     = ExStringUtils.trimToEmpty(request.getParameter("tuitionFee"));
		String recruitMajorCode = ExStringUtils.trimToEmpty(request.getParameter("recruitMajorCode"));
		
		User user = SpringSecurityHelper.getCurrentUser();
		RecruitPlan recruitPlan = recruitPlanService.get(planid);
		String reloadUrl = "";
		do{
			if (StringUtils.isNotEmpty(resourceid)) {//修改
				try{
					reloadUrl = request.getContextPath()+"/edu3/recruit/recruitplan/major-input.html?resourceid="+resourceid;
					if (null!=recruitPlan) {
						
						Date curDate  = new Date();
						Date planDate = recruitPlan.getBrSchoolApplyCloseTime();
						if (curDate.getTime()>planDate.getTime()) {
							map.put("statusCode", 300);
							map.put("message", "学习中心已进行申报工作，不允许修改招生专业!");
							break;
						}	
					}else {
						map.put("statusCode", 300);
						map.put("message", "请选择要新增专业的招生批次！");
						break;
					}
					
					RecruitMajor recruitMajor = recruitMajorService.get(resourceid);
					// 如果修改招生专业编码，则检查该条记录是否已被使用
					if(ExStringUtils.isNotEmpty(recruitMajorCode) && !recruitMajorCode.equals(recruitMajor.getRecruitMajorCode())){
						Map<String, Object> params = new HashMap<String, Object>();
						params.put("recruitMajor", recruitMajor.getResourceid());
						List<EnrolleeInfo> enrolleeInfoList = enrolleeInfoService.findListByCondition(params);
						if(ExCollectionUtils.isNotEmpty(enrolleeInfoList)){
							map.put("statusCode", 300);
							map.put("message", "该招生专业已被使用，不能修改招生专业编码！");
							break;
						}
					}
					recruitMajor.setRecruitPlan(recruitPlan);
					recruitMajor.setUpdateMan(user.getCnName());	//更新人
					recruitMajor.setUpdateManId(user.getResourceid());
					recruitMajor.setUpdateDate(ExDateUtils.getCurrentDateTime());//更新日期
					
					//Classic classic = classicService.get(classicid);
					//recruitMajor.setClassic(classic);
					//Major major =majorService.get(majorid);
					//recruitMajor.setMajor(major);
					//recruitMajor.setRecruitMajorName(major.getMajorName()+classic.getClassicName());		
					
					recruitMajor.setStudyperiod(Double.parseDouble(studyperiod));
					recruitMajor.setLimitNum(Long.parseLong(limitNum));
					recruitMajor.setLowerNum(Long.parseLong(lowerNum));
					if(ExStringUtils.isNotBlank(tuitionFee)){
						recruitMajor.setTuitionFee(Double.parseDouble(tuitionFee));
					}
					OrgUnit brandschool = orgUnitService.get(brSchoolid);
					recruitMajor.setBrSchool(brandschool);
					recruitMajor.setRecruitMajorCode(recruitMajorCode);
					//recruitMajorService.saveOrUpdate(recruitMajor);
					
					recruitPlan.getRecruitMajor().add(recruitMajor);
					
					recruitPlanService.update(recruitPlan);
					UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), "2", UserOperationLogs.INSERT, recruitPlan);
					map.put("statusCode", 200);
					map.put("message", "保存成功");
				}catch (Exception e) {
					logger.error("保存招生专业失败",e.fillInStackTrace());
					map.put("statusCode", 300);
					map.put("message", "保存失败!");
				}
			}else{//新增
				reloadUrl = request.getContextPath()+"/edu3/recruit/recruitplan/major-list.html?planid="+planid;
				String []  flags 	    = request.getParameterValues("flag");
				String []  types		= request.getParameterValues("teachingType");
				String []  classicids   = request.getParameterValues("classicid");
				String []  majorids     = request.getParameterValues("majorid");
				String []  studyperiods = request.getParameterValues("studyperiod");
				String []  limitNums    = request.getParameterValues("limitNum");
				String []  lowerNums    = request.getParameterValues("lowerNum");
				String [] courseGroup   = request.getParameterValues("courseGroup");
				String [] brSchoolids     = request.getParameterValues("brSchoolid");
				String [] tuitionFees     = request.getParameterValues("tuitionFee");
				String [] majoridNum     = request.getParameterValues("majoridNum");
				
				if (null!=flags&&null!=classicids&&null!=majorids) {
					
					for(int i=0;i<flags.length;i++){
						
						String t_p   = null==types[i]?"":types[i];
						String c_id  = null==classicids[i]?"":classicids[i];
						String m_id  = null==majorids[i]?"":majorids[i];
						String s_iod = null==studyperiods[i]?"0":studyperiods[i];
						String li_n  = null==limitNums[i]?"0":limitNums[i];
						String lo_n  = null==lowerNums[i]?"0":lowerNums[i];
//					String c_g   = null==courseGroup[i]?"0":courseGroup[i];
						String b_id = null==brSchoolids[i]?"":brSchoolids[i];
						String t_f = null==tuitionFees[i]?"0":tuitionFees[i];
						String m_n =null==majoridNum[i]?"":majoridNum[i];
						
						try {
							
							if (null!=recruitPlan) {
								
								Date curDate  = new Date();
								Date planDate = recruitPlan.getEndDate();//报名开始时间
								if (null==planDate) {
									planDate =curDate;
								}
								if (curDate.getTime()>planDate.getTime()) {
									map.put("statusCode", 300);
									map.put("message", "当前批次报名已结束，不允许新增招生专业!");
									break;
								}	
							}
							
							RecruitMajor recruitMajor = new RecruitMajor();
							recruitMajor.setRecruitPlan(recruitPlan);
							
							recruitMajor.setFillinMan(user.getCnName()); //填写人
							recruitMajor.setFillinManId(user.getResourceid());
							recruitMajor.setFillinDate(ExDateUtils.getCurrentDateTime());//填写日期
							
							Classic classic    			  = classicService.get(c_id);
							Major major        			  = majorService.get(m_id);
							//ExamSubjectSet set 			  = examSubjectSetService.get(c_g);
							//List<ExamSubject> subjectList = examSubjectSetService.findExamSubjectBySet(set);
							
							recruitMajor.setClassic(classic);
							recruitMajor.setMajor(major);
							recruitMajor.setTeachingType(t_p);
							recruitMajor.setRecruitMajorName(major.getMajorName()+"_"+classic.getClassicName()+
									"("+JstlCustomFunction.dictionaryCode2Value("CodeTeachingType", t_p)+")");		
							recruitMajor.setRecruitMajorCode(m_n);
							recruitMajor.setStudyperiod(Double.parseDouble(s_iod));
							recruitMajor.setLimitNum(Long.parseLong(li_n));
							recruitMajor.setLowerNum(Long.parseLong(lo_n));
							OrgUnit brandschool = orgUnitService.get(b_id);
							recruitMajor.setBrSchool(brandschool);
							if(ExStringUtils.isNotBlank(t_f)){
								recruitMajor.setTuitionFee(Double.parseDouble(t_f));
							}
							
							//recruitMajorService.saveOrUpdate(recruitMajor);
							
							//for(ExamSubject subject : subjectList){//安排入学考试科目
							//	subject.setCourseGroupName(set.getCourseGroupName());
							//	subject.setRecruitMajor(recruitMajor);
							//	recruitMajor.getExamSubject().add(subject);
							//examSubjectService.save(subject);
							//}
							recruitPlan.getRecruitMajor().add(recruitMajor);
							
						} catch (Exception e) {
							logger.error("保存招生专业失败",e.fillInStackTrace());
							map.put("statusCode", 300);
							map.put("message", "保存失败!");
							break;
						}
					}
					recruitPlanService.update(recruitPlan);
					UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), "2", UserOperationLogs.INSERT, recruitPlan);
				}
			}
		} while(false);
		map.put("reloadUrl", reloadUrl);
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 删除招生专业
	 * @param resourceid
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/recruit/recruitplan/delmajor.html")
	public void delMajor(String resourceid,HttpServletRequest request,HttpServletResponse response)throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();	
		String recruitPlanId    = ExStringUtils.trimToEmpty(request.getParameter("planid"));
		try {
			if (ExStringUtils.isNotEmpty(recruitPlanId)) {
				RecruitPlan plan= recruitPlanService.load(recruitPlanId);
				if (null!=plan) {
					if ("Y".equals(plan.getIsPublished())) {
						map.put("statusCode", 300);
						map.put("message", "当前批次已发布，不允许删除招生专业！");				
//						throw new WebException("当前批次已发布，不允许删除招生专业!");
					}
				}
			}
			if(!ExStringUtils.isEmpty(resourceid)){
				if(resourceid.split(",").length >1){//批量删除	
					recruitMajorService.batchDelete(resourceid.split(","));
				}else{//单个删除
					recruitMajorService.delete(resourceid);
				}
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), "2", UserOperationLogs.DELETE, "删除招生专业：resourceid:"+resourceid);
				map.put("statusCode", 200);
				map.put("message", "删除成功！");				
				map.put("forward", request.getContextPath()+"/edu3/recruit/recruitplan/major-list.html");
			}
		} catch (Exception e) {
			logger.error("删除招生专业出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 审核招生专业
	 * @param resourceid
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/recruit/recruitplan/checkmajor.html")
	public void checkMajor(String resourceid,String check,HttpServletRequest request,HttpServletResponse response)throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();	
		String recruitPlanId    = ExStringUtils.trimToEmpty(request.getParameter("planid"));
		try {
			if (ExStringUtils.isNotEmpty(recruitPlanId)) {
				RecruitPlan plan= recruitPlanService.load(recruitPlanId);
				if (null!=plan) {
					if ("Y".equals(plan.getIsPublished())) {
						throw new WebException("当前批次已发布，不允许审核招生专业!");
					}
				}
			}
			if(!ExStringUtils.isEmpty(resourceid)){
				if(resourceid.split(",").length >1){//批量审核
					String[] resourceids = resourceid.split(",");
					for (String id : resourceids) {
						RecruitMajor major = recruitMajorService.get(id);
						major.setStatus(Long.parseLong(check));
						recruitMajorService.update(major);
					}
				}else{//单个审核
					if(resourceid.contains(",")){
						resourceid = resourceid.substring(0, resourceid.lastIndexOf(","));
					}
					RecruitMajor major = recruitMajorService.get(resourceid);
					major.setStatus(Long.parseLong(check));
					recruitMajorService.update(major);
					
				}
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), "2", UserOperationLogs.PASS, "审批招生专业：resourceid:"+resourceid);
				map.put("statusCode", 200);
				map.put("message", "审核成功！");				
				map.put("forward", request.getContextPath()+"/edu3/recruit/recruitplan/major-list.html");
			}
		} catch (Exception e) {
			logger.error("审核招生专业出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "审核出错！<br/>"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}

	/**
	 * 根据招生批次获取招生专业列表
	 * 
	 * @param planid 招生批次ID
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/recruit/recruitmajor/query-recruitmajor-list.html")
	public void getRecruitMajorList(String planid, HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException {
		
		List<JsonModel> jsonList 	 = new ArrayList<JsonModel>();
		Map<String,Object> condition = new HashMap<String, Object>();
		String recruitPlan 			 = ExStringUtils.trimToEmpty(request.getParameter("recruitPlan"));
		String teachingType          = ExStringUtils.trimToEmpty(request.getParameter("teachingType"));
	
		if (ExStringUtils.isNotEmpty(recruitPlan)) {
			condition.put("recruitPlan",recruitPlan);
		}
		if (ExStringUtils.isNotEmpty(teachingType)) {
			condition.put("teachingType",teachingType);
		}
		List<RecruitMajor> majorList =  recruitMajorService.findMajorByCondition(condition);
		for(RecruitMajor major : majorList){
			JsonModel json = new JsonModel();
			json.setKey(major.getResourceid());
			json.setValue(major.getRecruitMajorName());
			if (major.getRecruitMajorName().length()>9) {
				json.setName(major.getRecruitMajorName().substring(0,9));
			}else {
				json.setName(major.getRecruitMajorName());
			}
			
			jsonList.add(json);	
		}
		renderJson(response, JsonUtils.listToJson(jsonList));
	}
	/**
	 * 获取学习中心允许招生的专业列表
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/framework/get-brschoolSetting-limit-major-list.html")
	public void getBrschoolSettingLimitMajorList(String planid,String exceptMajor ,HttpServletRequest request, HttpServletResponse response){
		
		List<JsonModel> jsonList 	 = new ArrayList<JsonModel>();
		Map<String, String> exceptMap= new HashMap<String, String>();
		Map<String,Object> condition = new HashMap<String, Object>();
		String [] exceptIds		     = null;
		User user					 = SpringSecurityHelper.getCurrentUser();
		
	
		condition.put("brschoolId", user.getUnitId());
		condition.put("isOpened", "Y");
		
		List<BrschMajorSetting> list = brschMajorSettingService.findBranchSchoolLimitMajorList(condition);
		if (ExStringUtils.isNotEmpty(exceptMajor)) {
			exceptIds				 = exceptMajor.split(",");
		}
		if (null!=exceptIds && exceptIds.length>0) {
			for (int i = 0; i < exceptIds.length; i++) {
				exceptMap.put(exceptIds[i].trim(), exceptIds[i].trim());
			}
		}
		
		String brschoolMajorIds 	 = ""; 
 		for (BrschMajorSetting setting : list) {
 			brschoolMajorIds 		+= ",'"+setting.getMajor().getResourceid()+"'";
		}
 		condition.put("recruitPlanId", planid);
 		if (ExStringUtils.isNotEmpty(brschoolMajorIds) && brschoolMajorIds.length()>0) {
			condition.put("brSchoolLimitMajorIds", brschoolMajorIds.substring(1));
		}
		
 		List<RecruitMajor> majorList = recruitMajorService.findBranSchoolLimitRecruitMajorListByPlanId(condition);
 		
 		for(RecruitMajor major : majorList)
		{
 			String classicId_rm   = major.getClassic().getResourceid();
 			String majorId_rm     = major.getMajor().getResourceid();
 			String teachType_rm   = major.getTeachingType();
 			String recruitMajorId = major.getResourceid();
 			if (exceptMap.containsKey(recruitMajorId.trim())) {
 				continue;
			}else {
				
				for (BrschMajorSetting setting:list) {
	 				
	 				String classicId_setting = setting.getClassic().getResourceid();
	 				String majorId_setting   = setting.getMajor().getResourceid();
	 				String teachType_setting = setting.getSchoolType();
	 				
	 				if (ExStringUtils.isNotEmpty(majorId_rm) && ExStringUtils.isNotEmpty(classicId_rm) && ExStringUtils.isNotEmpty(teachType_rm)&&
	 					ExStringUtils.isNotEmpty(majorId_setting) && ExStringUtils.isNotEmpty(classicId_rm) && ExStringUtils.isNotEmpty(teachType_setting)&&
	 					majorId_rm.equals(majorId_setting)&&classicId_rm.equals(classicId_setting)&&teachType_rm.equals(teachType_setting)) {
	 					
	 					JsonModel json = new JsonModel();
	 					json.setKey(major.getResourceid());
	 					json.setValue(major.getRecruitMajorName());
	 					jsonList.add(json);
					}
				}
			}
			
 			
		}
		renderJson(response, JsonUtils.listToJson(jsonList));
		
	}
	/**
	 * 根据学习中心获取招生专业列表
	 * 
	 * @param schoolid 
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/framework/get-branchmajorlist.html")
	public void getBranchSchoolMajorList(String schoolid, HttpServletRequest request,
			    HttpServletResponse response,ModelMap model) throws WebException {
		RecruitPlan plan = recruitPlanService.getLastPlan();
		List<JsonModel> jsonList = new ArrayList<JsonModel>();
		Map<String,Object> condition= new HashMap<String, Object>();
		condition.put("recruitPlan", plan.getResourceid());
		condition.put("branchSchool", schoolid);
		BranchSchoolPlan schoolPlan = branchSchoolPlanService.getBranchSchoolPlanByCondition(condition);
		if(schoolPlan!= null )
		{
			//审核通过
			if("Y".equals(schoolPlan.getIsAudited()))
			{
				Set<BranchSchoolMajor> majorList = schoolPlan.getBranchSchoolMajor();
					for(BranchSchoolMajor major : majorList)
					{
						JsonModel  json = new JsonModel();
						json.setKey(major.getRecruitMajor().getResourceid());
						json.setValue(major.getRecruitMajor().getRecruitMajorName());
						jsonList.add(json);	
					}
				}
		}	
		renderJson(response, JsonUtils.listToJson(jsonList));
	}
	/**
	 * 获取招生专业的起点学历
	 * @param recruitMajorId
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping("/edu3/framework/get-RecruitMajorStartPoint.html")
	public void getRecruitMajorStartPoint(String recruitMajorId, HttpServletRequest request,HttpServletResponse response,ModelMap model){
		RecruitMajor mojor =recruitMajorService.get(recruitMajorId);
		String startPoint ="";
		if(null!=mojor){
			if ("3".equals(mojor.getClassic().getClassicCode())) {
				startPoint = "高中";
			}
		}
		renderJson(response,JsonUtils.objectToJson(startPoint));
	}
	
	/**
	 * 导出模版
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/recruit/recruitplan/majormodel.html")
	public void majormodel(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
		try{
			//模板文件路径
			String templateFilepathString = "recruitmajortemplate.xls";
			downloadFile(response, "导入招生专业.xls", templateFilepathString,false);
		}catch(Exception e){			
			String msg = "导出excel文件出错：找不到该文件-导入招生专业.xls";
			logger.error("导出excel文件出错："+e.fillInStackTrace());
			renderHtml(response, "<script>alertMsg.warn("+"\""+msg+"\""+")</script>");
		}
	}

	/**
	 * 跳转到导入窗口
	 * @param request
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/recruit/recruitplan/major-imput.html")
	public String imputMajor(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws WebException{
		return "/edu3/recruit/recruitplan/major-imput";
	}
	
	/**
	 * 导入招生专业
	 * @param timetable
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/recruit/recruitplan/importmajor.html")
	public void importMajor(String exportAct,HttpServletRequest request, HttpServletResponse response) throws WebException {
//		User user = SpringSecurityHelper.getCurrentUser();
//		if(!user.getOrgUnit().getUnitType().equals(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())){//如果为校外学习中心人员
//			throw new WebException("只有教学站有权利导入！");
//		}
//		OrgUnit unit = user.getOrgUnit();
		//提示信息字符串
		String  rendResponseStr = "";
		List<RecruitMajorVo> falseList = new ArrayList<RecruitMajorVo>();
		File excelFile = null;
		
		String imurl = request.getParameter("importFile");
		//设置目标文件路径
		setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH + request.getParameter("importFile"));
		try {
			//上传文件到服务器
			List<AttachInfo> list = doUploadFile(request, response, null);
			AttachInfo attachInfo = list.get(0);
			//创建EXCEL对象 获得待导入的excel的内容
			File excel = new File(attachInfo.getSerPath() + File.separator+ attachInfo.getSerName());
			
			importExcelService.initParmas(excel, "recruitMajorVo",null);
			importExcelService.getExcelToModel().setSheet(0);// 设置excel sheet 0
			//获得待导入excel内容的List
			List modelList = importExcelService.getModelList();
			if(modelList==null){
				throw new Exception("导入模版错误！");
			}
			//转换为对应类型的List
			List<RecruitMajorVo> volist = new ArrayList<RecruitMajorVo>();
			for (int i = 0; i < modelList.size(); i++) {
				RecruitMajorVo majorVo = (RecruitMajorVo) modelList.get(i);
				volist.add(majorVo);
			}
			int duplicate = 0;//重复记录数目
			//遍历需要导入的List 将对应内容写进DB 并同时检测数据的合法性
			if (null!=volist) {
				GUIDUtils.init();
				for(RecruitMajorVo vo : volist){
					String falseMsg = "";
					//1、判断是否存在该招生批次
					Map<String,Object> condition = new HashMap<String,Object>();
					condition.put("planname", vo.getRecruitPlanname());
					List<RecruitPlan> planList = recruitPlanService.findPlanByCondition(condition);
					if(null==planList || planList.size()==0){
						falseMsg = "找不到招生批次！";
						vo.setErrorMsg(falseMsg);
						falseList.add(vo);
						continue;
					}
					RecruitPlan plan = planList.get(0);
					//2、判断是否存在该教学站
					String hql = "from "+OrgUnit.class.getSimpleName()+" where isDeleted=0 and trim(unitName)=?";
					List<OrgUnit> unitList = orgUnitService.findByHql(hql, vo.getBranchSchool());
					if(unitList.size()==0 || null == unitList){
						falseMsg = "找不到匹配的教学站！";
						vo.setErrorMsg(falseMsg);
						falseList.add(vo);
						continue;
					}
					OrgUnit orgUnit = unitList.get(0);
					//3、判断是否存在该专业
					condition.clear();
					condition.put("majorname", vo.getMajorName());
					List<Major> majorList = majorService.findMajorByCondition(condition);
					if(null==majorList || majorList.size() == 0){
						falseMsg = "找不到该专业！";
						vo.setErrorMsg(falseMsg);
						falseList.add(vo);
						continue;
					}
					// Major major = majorList.get(0);
					//4、判断层次是否存在
					hql = "from "+Classic.class.getSimpleName()+" where isDeleted=0 and classicName=?";
					List<Classic> classicList = classicService.findByHql(hql, vo.getClassicName());
					if(null==classicList || classicList.size() == 0){
						falseMsg = "找不到该层次！";
						vo.setErrorMsg(falseMsg);
						falseList.add(vo);
						continue;
					}
					Classic classic = classicList.get(0);
					//5、判断招生专业设置是否存在
					String teachingType = "";
					hql = "from "+Dictionary.class.getSimpleName()+" where dictCode like 'CodeTeachingType%' and trim(dictName)=?";
					List<Dictionary> dicList = dictionaryService.findByHql(hql, vo.getTeachingType());
					if (dicList.size() > 0) {
						teachingType = dicList.get(0).getDictValue();
					}
					hql = "from "+RecruitMajorSetting.class.getSimpleName()+" where isDeleted=0 and major.majorName=? and classic.resourceid=? and teachingType=? order by abs(major.classicCode-classic.classicCode)";
					List<RecruitMajorSetting> settingList = recruitMajorSettingService.findByHql(hql, vo.getMajorName(), classic.getResourceid(), teachingType);
					if(null==settingList || settingList.size() == 0){
						falseMsg = "找不到该招生专业设置！";
						vo.setErrorMsg(falseMsg);
						falseList.add(vo);
						continue;
					}
					// TODO：招生专业设置一定要注意本科与专科的专业名称相同的专业
					Major major = settingList.get(0).getMajor();
					//6、判断招生专业是否存在，有则修改，无则新增
					condition.clear();
					condition.put("recruitPlan", plan.getResourceid());
					condition.put("teachingType", teachingType);
					condition.put("major", major.getResourceid());
					condition.put("classic", classic.getResourceid());
					condition.put("branchSchool", orgUnit.getResourceid());
					condition.put("recruitMajorCode", vo.getRecruitMajorCode());
					List<RecruitMajor> majors = recruitMajorService.findMajorByCondition(condition);
					RecruitMajor recruitMajor = null;
					String recruitMajorName = major.getMajorName()+"_"+classic.getClassicName()+"("+vo.getTeachingType()+")";
					if (majors.size() == 0) {
						recruitMajor = new RecruitMajor();
						recruitMajor.setRecruitPlan(plan);
						recruitMajor.setBrSchool(orgUnit);
						recruitMajor.setClassic(classic);
						recruitMajor.setRecruitMajorCode(vo.getRecruitMajorCode());
						recruitMajor.setRecruitMajorName(recruitMajorName);
						recruitMajor.setMajor(major);
						recruitMajor.setTeachingType(teachingType);
						recruitMajor.setStudyperiod(vo.getStudyPeriod());
						recruitMajor.setLimitNum(vo.getLimitNum());
						recruitMajor.setLowerNum(vo.getLowerNum());
						recruitMajor.setTuitionFee(vo.getTuitionFee());
						recruitMajor.setStatus(0L);
					} else {
						duplicate++;
						recruitMajor = majors.get(0);
						recruitMajor.setRecruitPlan(plan);
						recruitMajor.setBrSchool(orgUnit);
						recruitMajor.setClassic(classic);
						recruitMajor.setRecruitMajorCode(vo.getRecruitMajorCode());
						recruitMajor.setRecruitMajorName(recruitMajorName);
						recruitMajor.setMajor(major);
						recruitMajor.setTeachingType(teachingType);
						recruitMajor.setStudyperiod(vo.getStudyPeriod());
						recruitMajor.setLimitNum(vo.getLimitNum());
						recruitMajor.setLowerNum(vo.getLowerNum());
						recruitMajor.setTuitionFee(vo.getTuitionFee());
						recruitMajor.setStatus(0L);
					}
					recruitMajorService.saveOrUpdate(recruitMajor);
				}
			}
			UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), "2", UserOperationLogs.IMPORT, "导入招生专业：附件ID："+attachInfo.getResourceid());
			//导出缴费信息导入失败的信息以及原因
			if(falseList!=null&&falseList.size()>0){
				setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
				//导出
				GUIDUtils.init();
				String fileName = GUIDUtils.buildMd5GUID(false);
				File disFile = new File(getDistfilepath()+ File.separator + fileName + ".xls");
					
				//模板文件路径
				String templateFilepathString = "recruitmajortemplateerror.xls";
				//初始化配置参数
				exportExcelService.initParmasByfile(disFile,"recruitMajorErrorVo", falseList,null);
				exportExcelService.getModelToExcel().setTemplateParam(templateFilepathString, 1, null);
				exportExcelService.getModelToExcel().setRowHeight(400);
					
				excelFile = exportExcelService.getExcelFile();//获取导出的文件
				logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());
//					downloadFile(response, "导入缴费信息失败名单.xls", excelFile.getAbsolutePath(),true);
				String upLoadurl = "/edu3/recruit/recruitplan/imputmajorerror.html";
				rendResponseStr = "{statusCode:200,message:'"+"导入成功"+ (volist.size() - falseList.size() - duplicate)
				+"条 | 导入失败"+  falseList.size()
				+"条 | 重复"+duplicate+"条！',forwardUrl:'"+upLoadurl+"?excelFile="+fileName+"'};";
			}
			if(ExStringUtils.isBlank(rendResponseStr)){
				rendResponseStr = "{statusCode:200,message:'"+"导入成功"+ (volist.size() - falseList.size() - duplicate)  
				+"条 | 导入失败"+ falseList.size()
				+"条 | 重复"+duplicate+"条！',forwardUrl:''};";
			}
		} catch (Exception e) {
			e.printStackTrace();
			rendResponseStr = "{statusCode:300,message:'操作失败!"+e.getMessage()+"'};";
		}
		StringBuffer html = new StringBuffer();
		html.append("<script>");
		html.append("var response = "+rendResponseStr);
		html.append("if(window.parent.donecallback) window.parent.donecallback(response);");
		html.append("</script>");
		renderHtml(response, html.toString());
	}
	
	/**
	 * 导出失败信息
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/recruit/recruitplan/imputmajorerror.html")
	public void uploadFalseToImport(String excelFile,HttpServletRequest request,HttpServletResponse response) throws Exception {
		setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
		File disFile = new File(getDistfilepath()+ File.separator + excelFile+".xls");
		downloadFile(response, "导入招生专业失败记录.xls", disFile.getAbsolutePath(),true);
	}	

}
