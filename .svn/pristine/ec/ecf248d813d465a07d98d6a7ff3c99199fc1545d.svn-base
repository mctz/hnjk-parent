package com.hnjk.edu.recruit.controller;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hnjk.core.support.context.Constants;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExFileUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.HttpHeaderUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.BaseSupportController;
import com.hnjk.edu.basedata.model.Grade;
import com.hnjk.edu.basedata.model.YearInfo;
import com.hnjk.edu.basedata.service.IClassicService;
import com.hnjk.edu.basedata.service.IGradeService;
import com.hnjk.edu.basedata.service.IYearInfoService;
import com.hnjk.edu.recruit.model.RecruitMajor;
import com.hnjk.edu.recruit.model.RecruitPlan;
import com.hnjk.edu.recruit.service.IBranchSchoolPlanService;
import com.hnjk.edu.recruit.service.IRecruitJDBCService;
import com.hnjk.edu.recruit.service.IRecruitMajorService;
import com.hnjk.edu.recruit.service.IRecruitPlanService;
import com.hnjk.edu.util.Condition2SQLHelper;
import com.hnjk.platform.system.UserOperationLogsHelper;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.Attachs;
import com.hnjk.platform.system.model.Dictionary;
import com.hnjk.platform.system.model.UserOperationLogs;
import com.hnjk.platform.system.service.IAttachsService;
import com.hnjk.platform.system.service.IDictionaryService;
import com.hnjk.platform.taglib.JstlCustomFunction;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.OrgUnit;
import com.hnjk.security.model.User;
import com.hnjk.security.service.IOrgUnitService;

/**
 * 招生计划Controller<p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2010-3-10 上午10:59:06
 * @see
 * @version 1.0
 */

@Controller
public class ReruitPlanController extends BaseSupportController{

	private static final long serialVersionUID = 1607816750458113077L;

	@Autowired
	@Qualifier("recruitPlanService")
	private IRecruitPlanService recruitPlanService;
	
	@Autowired
	@Qualifier("yearInfoService")
	private IYearInfoService yearInfoService;
	
	@Autowired
	@Qualifier("gradeService")
	private IGradeService gradeService;

	@Autowired
	@Qualifier("orgUnitService")
	private IOrgUnitService orgUnitService;
	
	@Autowired
	@Qualifier("classicService")
	private IClassicService classicService;
			
	@Autowired
	@Qualifier("recruitMajorService")
	private IRecruitMajorService recruitMajorService;
	
	@Autowired
	@Qualifier("branchSchoolPlanService")
	private IBranchSchoolPlanService branchSchoolPlanService;
	
//	@Autowired
//	@Qualifier("examSubjectSetService")
//	private IExamSubjectSetService examSubjectSetService;
	
	@Autowired
	@Qualifier("recruitJDBCService")
	private IRecruitJDBCService recruitJDBCService;
	
	@Autowired
	@Qualifier("attachsService")
	private IAttachsService attachsService;
	
	@Autowired
	@Qualifier("dictService")
	private IDictionaryService dictionaryService;
	
	/**
	 * 获取所有招生计划列表
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/recruit/recruitplan/recruitplan-list.html")
	public String listRecruitplan(HttpServletRequest request,HttpServletResponse response,Page objPage, ModelMap model) throws WebException {
	
		objPage.setOrderBy("isPublished desc,recruitPlanname desc");
		//objPage.setOrder(Page.DESC);

		String recruitPlanname  	 = ExStringUtils.trimToEmpty(request.getParameter("recruitPlanname"));
		String startDate 			 = ExStringUtils.trimToEmpty(request.getParameter("startDate"));
		String publishDate 			 = ExStringUtils.trimToEmpty(request.getParameter("publishDate"));
		String endDate 				 = ExStringUtils.trimToEmpty(request.getParameter("endDate"));
		String isPublished 			 = ExStringUtils.trimToEmpty(request.getParameter("isPublished"));
		String grade 				 = ExStringUtils.trimToEmpty(request.getParameter("grade"));
		String teachingType 		 = ExStringUtils.trimToEmpty(request.getParameter("teachingType"));
		
		Map<String,Object> condition = new HashMap<String,Object>();
		
		if(!StringUtils.isEmpty(recruitPlanname)) {
			condition.put("recruitPlanname", recruitPlanname);
		}
		if (!StringUtils.isEmpty(publishDate)) {
			condition.put("publishDate", publishDate);
		}
		if (!StringUtils.isEmpty(startDate)) {
			condition.put("startDate", startDate);
		}
		if (!StringUtils.isEmpty(endDate)) {
			condition.put("endDate", endDate);
		}
		if (!StringUtils.isEmpty(isPublished)) {
			condition.put("isPublished", isPublished);
		}
		if (!StringUtils.isEmpty(grade)) {
			condition.put("grade", grade);
		}
		if (!StringUtils.isEmpty(teachingType)) {
			condition.put("teachingType", teachingType);
		}
		
		Page page = recruitPlanService.findPlanByCondition(condition, objPage);
		
		model.addAttribute("planlist", page);
		model.addAttribute("condition", condition);
		
		return "/edu3/recruit/recruitplan/recruitplan-list";
	}

	/**
	 * 招生计划表单
	 * 
	 * @param resourceid
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/recruit/recruitplan/plan-input.html")
	public String addRecruitplan(String resourceid, HttpServletRequest request,HttpServletResponse response, ModelMap model) throws WebException {
		
		RecruitPlan plan 		   = null;
		StringBuffer hql           = new StringBuffer();
		hql.append(" from "+OrgUnit.class.getSimpleName()+" unit where unit.isDeleted=0 and unit.status='normal' ");
		hql.append(" and unit.unitType='"+CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue()+"'");
		List<OrgUnit> brSchoolList = orgUnitService.findByHql(hql.toString());
		if (StringUtils.isNotEmpty(resourceid)) {
			
			plan 		 		   = recruitPlanService.get(resourceid);
			String brSchoolIds     = plan.getBrSchoolIds();
			if(ExStringUtils.isNotEmpty(brSchoolIds)) {
				model.put("selectedBrSchool", brSchoolIds.split(","));
			}
			
			List<Attachs> attachs  = attachsService.findByHql(" from "+Attachs.class.getName()+" where isDeleted=0 and formId=? and formType=?", resourceid,"entranceMemoAttach");
			User user 			   = SpringSecurityHelper.getCurrentUser();
			model.put("storeDir", user.getUsername());
			model.put("attachList", attachs);
			model.put("plan", plan);
		}
		model.put("brSchoolList", brSchoolList);
		model.put("resourceid", resourceid);
	
		
		return "/edu3/recruit/recruitplan/recruitplan-form";
	}
	
	/**
	 * 查看生计划信息
	 * @param resourceid
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/recruit/recruitplan/into-plan.html")
	public String intoRecruitplan(String resourceid, HttpServletRequest request,HttpServletResponse response, ModelMap model)throws WebException{
		
		RecruitPlan plan = recruitPlanService.get(resourceid);
		model.addAttribute("plan", plan);
		
		return "/edu3/recruit/recruitplan/recruitplan-view";
	}
	/**
	 * 根据年度及学期查询年级
	 * @param yearInfo
	 * @param term
	 * @param response
	 */
	@RequestMapping("/edu3/recruit/recruitplan/getGrade.html")
	public void getGradeByYearInfoAndTerm(String yearInfo,String term,HttpServletResponse response,Page objPage){
		
		Map<String,Object> condition = new HashMap<String,Object>();
		condition.put("yearInfoId", yearInfo);
		condition.put("term", term);
		objPage = gradeService.findGradeByCondition(condition,objPage);
		
		try {
			Grade grade = (Grade) objPage.getResult().get(0);
			condition.put("status", true);
			condition.put("gradeid", grade.getResourceid());
		} catch (Exception e) {
			condition.put("status", false);
			condition.put("msg","没有相关的年级基础数据，请与管理员联系！");
		}
		renderJson(response,JsonUtils.mapToJson(condition));
	}
	/**
	 * 保存招生计划
	 * 
	 * @param resourceid
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 * @throws ParseException 
	 */
	@RequestMapping("/edu3/recruit/recruitplan/saveplan.html")
	public void saveRecruitplan(String resourceid, HttpServletRequest request,HttpServletResponse response, ModelMap model) throws WebException, ParseException {
		
		Map<String,Object> map 		  = new HashMap<String, Object>();
		String recruitPlanname 		  = ExStringUtils.trimToEmpty(request.getParameter("recruitPlanname"));
		String startDate 	   		  = ExStringUtils.trimToEmpty(request.getParameter("startDate"));
		String endDate 		   		  = ExStringUtils.trimToEmpty(request.getParameter("endDate"));
		String publishDate 	   		  = ExStringUtils.trimToEmpty(request.getParameter("publishDate"));
		String brSchoolApplyCloseTime = ExStringUtils.trimToEmpty(request.getParameter("brSchoolApplyCloseTime"));
		//String enrollStartTime = ExStringUtils.trimToEmpty(request.getParameter("enrollStartTime"));
		//String enrollEndTime   = ExStringUtils.trimToEmpty(request.getParameter("enrollEndTime"));
		String yearguid        		  = ExStringUtils.trimToEmpty(request.getParameter("yearInfo"));
		String term 		   		  = ExStringUtils.trimToEmpty(request.getParameter("term"));
		String webregMemo      		  = ExStringUtils.trimToEmpty(request.getParameter("webregMemo"));
		String isPublished     		  = ExStringUtils.trimToEmpty(request.getParameter("isPublished"));
		String isSpecial       		  = ExStringUtils.trimToEmpty(request.getParameter("isSpecial"));
		String gradeid         		  = ExStringUtils.trimToEmpty(request.getParameter("gradeid"));
		
		String[] teachingTypes = request.getParameterValues("teachingType");
		String[] brSchool      = request.getParameterValues("brSchoolms2side__dx");	
		String  brSchoolIds    = "";
		String  brSchoolNames  = "";
		String teachingType    = "";
		String teachingTypeStr = "";
	
		try{
			Date p_Date        = ExDateUtils.parseDate(publishDate, ExDateUtils.PATTREN_DATE);            //发布时间
			Date s_Date        = ExDateUtils.parseDate(startDate, ExDateUtils.PATTREN_DATE);              //报名开始时间
			Date e_Date        = ExDateUtils.parseDate(endDate, ExDateUtils.PATTREN_DATE);         		  //报名结束时间
			Date c_Date        = ExDateUtils.parseDate(brSchoolApplyCloseTime, ExDateUtils.PATTREN_DATE); //学习中心申报结束时间
			//Date es_Date       = ExDateUtils.parseDate(enrollStartTime, ExDateUtils.PATTREN_DATE); 	  //录取查询开放时间
			//Date ee_Date       = ExDateUtils.parseDate(enrollEndTime, ExDateUtils.PATTREN_DATE);        //录取查询结束时间
			
			Long p_DateTime    = p_Date.getTime();//发布时间
			Long s_DateTime    = s_Date.getTime();//报名开始时间
			Long e_DateTime    = e_Date.getTime();//报名结束时间
			Long c_DateTime    = c_Date.getTime();//学习中心申报结束时间
			//Long es_DateTime   = es_Date.getTime();
			//Long ee_DateTime   = ee_Date.getTime();
			//学习中心申报结束时间要大于等于发布时间,小于报名开始时间
			//报名结束时间要小于报开始时间
			if ((c_DateTime >= p_DateTime && c_DateTime<s_DateTime)&&(e_DateTime>s_DateTime)) {
				
				RecruitPlan plan 	   = null;
				User user 			   = SpringSecurityHelper.getCurrentUser();
				if (StringUtils.isNotEmpty(resourceid)) {
					plan 			   = recruitPlanService.get(resourceid);
					plan.setUpdateMan(user.getCnName()); //更新人
					plan.setUpdateManId(user.getResourceid());
					plan.setUpdateDate(ExDateUtils.getCurrentDateTime());//更新日期
				}else {
					plan 			   = new RecruitPlan();
					plan.setFillinMan(user.getCnName()); //填写人
					plan.setFillinManId(user.getResourceid());
					plan.setFillinDate(ExDateUtils.getCurrentDateTime());//填写日期
				}
				
				
				Grade grade 		   = gradeService.findUniqueByProperty("resourceid", gradeid);
				YearInfo yearInfo 	   = yearInfoService.get(yearguid);
				
				if (null==grade) {
					throw new WebException("没有相关的年级基础数据，请与管理员联系！");
				}
				
				if (null!=teachingTypes && teachingTypes.length>0) {
					for (int i = 0; i < teachingTypes.length; i++) {
						teachingType    += ","+teachingTypes[i];
						teachingTypeStr += ","+JstlCustomFunction.dictionaryCode2Value("CodeTeachingType", teachingTypes[i]);
					}
					//招生批生的模式，只有在批次是否发布值为空或是否发布值不等于Y时才允许设置
					if(ExStringUtils.isEmpty(plan.getIsPublished()) || !"Y".equals(plan.getIsPublished())){
						plan.setTeachingType(teachingType.substring(1));
					}
				}
				
				if (null!=brSchool && brSchool.length>0 && "Y".equals(isSpecial)) {
					for (int i = 0; i < brSchool.length; i++) {
						brSchoolIds  += ","+brSchool[i].trim();
						OrgUnit unit  = orgUnitService.get(brSchool[i].trim());
						brSchoolNames+= ","+unit.getUnitShortName(); 
					}
					plan.setBrSchoolIds(brSchoolIds.substring(1));
				}
				
				
				if (!ExStringUtils.isNotEmpty(resourceid) && null!=teachingTypes && teachingTypes.length>0) {
					plan.setRecruitPlanname(recruitPlanname+"-("+teachingTypeStr.substring(1)+")");
				}else {
					plan.setRecruitPlanname(recruitPlanname);
				}
		
				plan.setGrade(grade);
				plan.setStartDate(s_Date);
				plan.setEndDate(e_Date);
				plan.setPublishDate(p_Date);
				plan.setBrSchoolApplyCloseTime(c_Date);
				//plan.setEnrollStartTime(es_Date);
				//plan.setEnrollEndTime(ee_Date);
				plan.setYearInfo(yearInfo);
				plan.setTerm(term);
				plan.setWebregMemo(webregMemo);
				plan.setIsPublished(isPublished);
				plan.setIsSpecial(isSpecial);
				
			    recruitPlanService.saveOrUpdate(plan);
			    UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), "2", UserOperationLogs.INSERT, plan);
			    map.put("statusCode", 200);
				map.put("message", "保存成功！");
				map.put("reloadUrl", request.getContextPath()+"/edu3/recruit/recruitplan/plan-input.html?resourceid="+plan.getResourceid());
			}else{
				map.put("statusCode", 300);
				map.put("message", "保存失败，请填入正确的时间：<br/>学习中心申报结束时间>=招生批次发布日期<报名开始时间，且报名开始时间<报名截止时间 <br/>");
			}
	 }catch (Exception e) {
		 logger.error("保存招生计划失败",e.fillInStackTrace());
		 map.put("statusCode", 300);
		 map.put("message", "保存失败！"+e.fillInStackTrace());
	}
	 renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 复制招生计划
	 * @param resourceid
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/recruit/recruitplan/copyplan.html")
	public void copyRecruitplan(String resourceid,HttpServletRequest request,HttpServletResponse response) throws WebException {
		
		Map<String,Object> map    			= new HashMap<String, Object>();
		try{
			  RecruitPlan newPlan 			= new RecruitPlan();
			  Set<RecruitMajor> newMajorSet = new HashSet<RecruitMajor>();
			  Criterion[] criterions        = new Criterion[2];
			  User user 		  			= SpringSecurityHelper.getCurrentUser();
			  RecruitPlan oldPlan 			= recruitPlanService.get(resourceid);
			  Set<RecruitMajor> oldMajorSet = oldPlan.getRecruitMajor();//复制招生计划下的招生专业
			
			  if(oldPlan.getEnrollStartTime()==null|| oldPlan.getEnrollEndTime()==null || oldPlan.getBrSchoolApplyCloseTime()== null){
				  map.put("statusCode", 300);
				  map.put("message", "复制失败，请把【录取查询开放时间】、【录取查询截止时间】、【学习中心申报结束时间】填写完整。");
				  renderJson(response, JsonUtils.mapToJson(map));
				  return;
			  }
			  
			  if (oldPlan.getGrade()==null) {
				  map.put("statusCode", 300);
				  map.put("message", "复制失败，复制的招生批次没有关联年级数据，请修改后再点击复制！");
				  renderJson(response, JsonUtils.mapToJson(map));
				  return;
			  }
			  
			  newPlan.setRecruitPlanname(oldPlan.getRecruitPlanname()+"(复制)");
			  newPlan.setStartDate(oldPlan.getStartDate());
			  newPlan.setEndDate(oldPlan.getEndDate());
			  newPlan.setIsPublished("N");
			  newPlan.setEnrollStartTime(oldPlan.getEnrollStartTime());
			  newPlan.setEnrollEndTime(oldPlan.getEnrollEndTime());
			  newPlan.setYearInfo(oldPlan.getYearInfo());
			  newPlan.setTerm(oldPlan.getTerm());
			  newPlan.setWebregMemo(oldPlan.getWebregMemo());
			  newPlan.setFillinDate(ExDateUtils.getCurrentDateTime());
			  newPlan.setFillinMan(user.getCnName());
			  newPlan.setFillinManId(user.getResourceid());
			  newPlan.setGrade(oldPlan.getGrade());
			  newPlan.setTeachingType(oldPlan.getTeachingType());
			  newPlan.setIsSpecial(oldPlan.getIsSpecial());
			  
			 for(RecruitMajor oldMajor : oldMajorSet){
			   
			   String old_courseGroupName   = "";
			  // Set<ExamSubject>old_subject 	= oldMajor.getExamSubject();
			   
			  // for (ExamSubject sub :old_subject) {
			//	   old_courseGroupName      = sub.getCourseGroupName();
			//	   break;
			 //  }
			   
			   criterions[0] 			    = Restrictions.eq("courseGroupName", old_courseGroupName);
			   criterions[1]                = Restrictions.eq("isDeleted",0);
			   //List<ExamSubjectSet>  setList= examSubjectSetService.findByCriteria(criterions);
			   //List<ExamSubject> subList    = new ArrayList<ExamSubject>();
						   
			  // if (null!=setList&&!setList.isEmpty()) {
			//	   subList					= examSubjectSetService.findExamSubjectBySet(setList.get(0));
			  // }
			   
			   
			   RecruitMajor newMajor 		= new RecruitMajor();
			   
			   newMajor.setRecruitMajorName(oldMajor.getRecruitMajorName());
			   newMajor.setTeachingType(oldMajor.getTeachingType());
			   newMajor.setClassic(oldMajor.getClassic());
			   newMajor.setMajor(oldMajor.getMajor());
			   newMajor.setStudyperiod(oldMajor.getStudyperiod());
			   newMajor.setLimitNum(oldMajor.getLimitNum());
			   newMajor.setLowerNum(oldMajor.getLowerNum());
			   newMajor.setRecruitPlan(newPlan);
			   newMajor.setFillinDate(ExDateUtils.getCurrentDateTime());
			   newMajor.setFillinMan(user.getCnName());
			   newMajor.setFillinManId(user.getResourceid());
			   /*
			   for (ExamSubject sub:subList) {
				   sub.setStartTime(null);
				   sub.setEndTime(null);
				   sub.setCourseGroupName(old_courseGroupName);
				   sub.setRecruitMajor(newMajor);
			   }
			   */
			  // newMajor.setExamSubject(new HashSet<ExamSubject>(subList));
			   newMajorSet.add(newMajor);
			   
			 }
			 
			 newPlan.setRecruitMajor(newMajorSet);
			 recruitPlanService.save(newPlan);
			 UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), "2", UserOperationLogs.COPY, newPlan);
			 map.put("statusCode", 200);
			 map.put("message", "复制成功，请到<font color='red'>招生计划-入学考试科目</font>中更改考试时间！");
			 map.put("forward", request.getContextPath()+"/edu3/recruit/recruitplan/recruitplan-list.hmtl");
			 
		}catch (NullPointerException e) {
			logger.error("没有可以复制的招生计划！");
			map.put("statusCode", 300);
			map.put("message", "没有可以复制的招生计划");
		}catch (Exception e) {
			logger.error("复制招生计划出错",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "复制招生计划出错:"+e.getMessage());
		}
		renderJson(response,JsonUtils.mapToJson(map));
	}
	
	/**
	 * 删除招生计划
	 * @param resourceid
	 * @param request
	 * @param response
	 * @param model
	 * @throws WebException
	 */
	@RequestMapping("/edu3/recruit/recruitplan/delplan.html")
	public void delete(String resourceid,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();	
		String message          = "";
		try {
			if(!ExStringUtils.isEmpty(resourceid)){
				Map<String,Object> resultMap = branchSchoolPlanService.isBrSchoolAPPAuditPlan(resourceid);
				
				if (null!=resultMap) {
					
					String [] ids = resourceid.split(",");
					
					for (int i = 0; i < ids.length; i++) {
						boolean isAllowDel = (Boolean) resultMap.get(ids[i]);
						if (isAllowDel) {
							recruitPlanService.delete(ids[i]);
						}else {
							message += recruitPlanService.load(ids[i]).getRecruitPlanname()+"，已有申报记录不允许删除！<br/>";
						}
					}
					if (ExStringUtils.isNotEmpty(message)) {
						map.put("statusCode", 300);
					}else {
						map.put("statusCode", 200);
					}
				
					map.put("message", message);				
					map.put("forward", request.getContextPath()+"/edu3/recruit/recruitplan/recruitplan-list.html");
					
				}else{
					
					message    = "请选择要删除的批次！";
					map.put("statusCode", 300);
					map.put("message", message);				
					map.put("forward", request.getContextPath()+"/edu3/recruit/recruitplan/recruitplan-list.html");
				}
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), "2", UserOperationLogs.DELETE, "删除招生计划：resourceid:"+resourceid);
			}else {
				message    = "请选择要删除的批次！";
				map.put("statusCode", 300);
				map.put("message", message);				
				map.put("forward", request.getContextPath()+"/edu3/recruit/recruitplan/recruitplan-list.html");
			}
		} catch (Exception e) {
			logger.error("删除招生计划出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 批次标准分可调系数X列表
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/recruit/recruitplan/coefficient-list.html")
	public String listCoefficient(HttpServletRequest request,ModelMap model,Page objPage)throws WebException{
		
		objPage.setOrder(Page.DESC);
		objPage.setOrderBy("startDate");
		
		Map<String, Object> condition = new HashMap<String, Object>();
		String recruitPlan 			  = ExStringUtils.defaultIfEmpty(request.getParameter("recruitPlan"), "");
		if(!"".equals(recruitPlan)) {
			condition.put("recruitPlan", recruitPlan);
		}

		Page page 					  = recruitPlanService.findPlanByCondition(condition, objPage);
		
		model.addAttribute("page", page);
		model.addAttribute("condition",condition);
		return "/edu3/recruit/recruitplan/coefficient-list";
	}
	
	/**批次标准分可调系数X表单
	 * @param resourceid
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/recruit/recruitplan/coefficient-form.html")
	public String inputCoefficient(String resourceid,ModelMap model)throws WebException{
		if(StringUtils.isNotEmpty(resourceid)){
			RecruitPlan plan = recruitPlanService.get(resourceid);
			model.addAttribute("plan", plan);
		}
		return "/edu3/recruit/recruitplan/coefficient-form";
	}
	
	/**保存批次标准分可调系数X
	 * @param resourceid
	 * @param coefficient
	 * @param request
	 * @param response
	 * @param model
	 * @throws WebException
	 */
	@RequestMapping("/edu3/recruit/recruitplan/coefficient-save.html")
	public void saveCoefficient(String resourceid,String coefficient,HttpServletRequest request,HttpServletResponse response, ModelMap model) throws WebException{
		
		Map<String,Object> map = new HashMap<String, Object>();
		if(StringUtils.isNotEmpty(resourceid)){
			try{
				RecruitPlan plan = recruitPlanService.get(resourceid);
				//可调系数X修改，暂时注释 plan.setCoefficient(Double.valueOf(coefficient));
				recruitPlanService.update(plan);
			    map.put("statusCode", 200);
				map.put("message", "保存成功！");
				map.put("navTabId", "list");
				map.put("reloadUrl", request.getContextPath() +"/edu3/recruit/recruitplan/coefficient-form.html?resourceid="+plan.getResourceid());
			 }catch (Exception e) {
				 logger.error("保存标准分可调系数失败",e.fillInStackTrace());
				 map.put("statusCode", 300);
				 map.put("message", "保存失败！");
			}
			 renderJson(response, JsonUtils.mapToJson(map));
		}
	}
	
	/**
	 * 设置录取查询开放时间及结束时间-列表
	 * @param request
	 * @param response
	 * @param model
	 * @param objPage
	 * @return
	 */
	@RequestMapping("/edu3/recruit/matriculate/enrollStartTimeAndEndTime-list.html")
	public String enrollStartTimeAndEndTimeList(HttpServletRequest request,HttpServletResponse response, ModelMap model,Page objPage){
		
		objPage.setOrderBy("yearInfo.firstYear");
		objPage.setOrder(Page.DESC);

		String recruitPlanname  	 = ExStringUtils.trimToEmpty(request.getParameter("recruitPlanname"));
		String startDate 			 = ExStringUtils.trimToEmpty(request.getParameter("startDate"));
		String endDate 				 = ExStringUtils.trimToEmpty(request.getParameter("endDate"));
		String isPublished 			 = ExStringUtils.trimToEmpty(request.getParameter("isPublished"));
		String grade 				 = ExStringUtils.trimToEmpty(request.getParameter("grade"));
		String teachingType 		 = ExStringUtils.trimToEmpty(request.getParameter("teachingType"));
		
		Map<String,Object> condition = new HashMap<String,Object>();
		
		if(!StringUtils.isEmpty(recruitPlanname)) {
			condition.put("recruitPlanname", recruitPlanname);
		}
		if (!StringUtils.isEmpty(startDate)) {
			condition.put("startDate", startDate);
		}
		if (!StringUtils.isEmpty(endDate)) {
			condition.put("endDate", endDate);
		}
		if (!StringUtils.isEmpty(isPublished)) {
			condition.put("isPublished", isPublished);
		}
		if (!StringUtils.isEmpty(grade)) {
			condition.put("grade", grade);
		}
		if (!StringUtils.isEmpty(teachingType)) {
			condition.put("teachingType", teachingType);
		}
		
		Page page = recruitPlanService.findPlanByCondition(condition, objPage);
		
		model.addAttribute("planlist", page);
		model.addAttribute("condition", condition);
		
		return "/edu3/recruit/matriculate/enrollstartTimeAndEndTime-list";
	}
	/**
	 * 设置录取查询开放时间及结束时间-表单
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/recruit/matriculate/enrollStartTimeAndEndTime-form.html")
	public String enrollStartTimeAndEndTimeForm(HttpServletRequest request,HttpServletResponse response, ModelMap model){
		
		String resourceid = ExStringUtils.trimToEmpty(request.getParameter("recruitPlanId"));
		RecruitPlan plan  = recruitPlanService.get(resourceid);
		model.put("recruitPlan", plan);
		
		return "/edu3/recruit/matriculate/enrollstartTimeAndEndTime-form";
	}
	/**
	 * 设置录取查询开放时间及结束时间-保存
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/recruit/matriculate/enrollStartTimeAndEndTime-save.html")
	public void enrollStartTimeAndEndTimeSave(HttpServletRequest request,HttpServletResponse response){
		
		Map<String,Object> m = new HashMap<String, Object>();
		String resourceid    = ExStringUtils.trimToEmpty(request.getParameter("recruitPlanId"));
		String startTimeStr  = ExStringUtils.trimToEmpty(request.getParameter("enrollStartTime"));
		String endTimeStr    = ExStringUtils.trimToEmpty(request.getParameter("enrollEndTime"));
		String reportTimeStr    = ExStringUtils.trimToEmpty(request.getParameter("reportTime"));
		String endtTimeStr    = ExStringUtils.trimToEmpty(request.getParameter("endTime"));
		String message       = "";
		try {
			RecruitPlan plan = recruitPlanService.get(resourceid);
			if (!"Y".equals(plan.getIsPublished())) {
				throw new Exception("批次已关闭不允许更改录取查询时间！");
			}
			Date startTime   = ExDateUtils.convertToDate(startTimeStr);
			Date endTime     = ExDateUtils.convertToDate(endTimeStr);
			Date reportTime  = ExDateUtils.convertToDate(reportTimeStr);
			Date endendtie  = ExDateUtils.convertToDate(endtTimeStr);
			if (endTime.getTime() < startTime.getTime()) {
				throw new Exception("录取查询结束时间必需小于开始时间！");
			}
			plan.setEnrollStartTime(startTime);
			plan.setEnrollEndTime(endTime);
			plan.setReportTime(reportTime);
			plan.setEndTime(endendtie);
			recruitPlanService.update(plan);
			message          = "设置成功!";
			UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), "2", UserOperationLogs.UPDATE, "修改录取查询时间："+startTime+"||"+endTime+"||"+reportTime+"||"+endendtie);
			m.put("statusCode", 200);
			m.put("message", "保存成功！");
			m.put("navTabId", "RES_MATRICALATE_BATCHMATRICULATE_ENROLLEEDATE");
			//m.put("callbackType","closeCurrent");
			m.put("reloadTabUrl", request.getContextPath()+"/edu3/recruit/matriculate/enrollStartTimeAndEndTime-list.html");
			
		} catch (Exception e) {
			logger.error("设置录取查询时间出错：{}"+e.fillInStackTrace());
			message          = "操作成败："+e.getMessage();
			m.put("statusCode", 300);
			m.put("message", message);
			
		}
		m.put("message", message);
		
		renderJson(response,JsonUtils.mapToJson(m));
	}
	
	/**
	 * 录取文件上传表单
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/recruit/matriculate/uploadPackageOffer-form.html")
	public String uploadPackageOffer(HttpServletRequest request,ModelMap model){
		List<Dictionary> dictionaries = CacheAppManager.getChildren("CodePackageOffer");
		String relativePath = File.separator+"TheAdmissionNotice";
		//String objUrl_temp = CacheAppManager.getSysConfigurationByCode("web.uploadfile.rootpath").getParamValue()+relativePath;
		String objUrl = Constants.EDU3_DATAS_LOCALROOTPATH + relativePath;
		File file = new File(objUrl);
		if(!file.exists()){//第一次使用这个功能时调用
			file.mkdir();
			//将原先的录取文件移动到新目录下
			User user = SpringSecurityHelper.getCurrentUser();
			List<Attachs> attachlList = new ArrayList<Attachs>();
			for (Dictionary dictionary : dictionaries) {
				String orgUrl = request.getSession().getServletContext().getRealPath(relativePath+File.separator+dictionary.getDictValue());
				File dicFile = new File(orgUrl);
				if(dicFile.exists()){
					List<Attachs> tempList = new ArrayList<Attachs>();
					ExFileUtils.copy(orgUrl, objUrl+File.separator+dictionary.getDictValue());
					Attachs attachs = attachsService.findUniqueByProperty("formId", dictionary.getDictCode());
					if(attachs==null) {
						attachs = new Attachs();
					}
					attachs.setAttName(dictionary.getDictValue());
					attachs.setSerPath(objUrl);
					attachs.setSerName(dictionary.getDictValue());
					attachs.setSaveType("local");
					attachs.setFormType("packageOfferUpload");
					attachs.setAttSize(file.length());
					attachs.setAttType(ExStringUtils.getPostfix(dictionary.getDictValue()));
					attachs.setFormId(dictionary.getDictCode());
					attachs.setFillinName(user.getCnName());
					attachs.setFillinNameId(user.getResourceid());
					attachs.setUploadTime(new Date());
					tempList.add(attachs);
					attachlList.add(attachs);
					dictionary.setAttachs(tempList);
				}
			}
			attachsService.batchSaveOrUpdate(attachlList);
		}else {
			for (Dictionary dic : dictionaries) {
				List<Attachs> attachs = new ArrayList<Attachs>();
				if(ExStringUtils.isNotBlank(dic.getDictValue())){
					Attachs temp = attachsService.findUniqueByProperty("formId",dic.getDictCode());
					if(temp!=null) {
						attachs.add(temp);
					}
				}
				dic.setAttachs(attachs);
			}
		}
		model.addAttribute("dictionaries", dictionaries);
		return "/edu3/recruit/matriculate/uploadPackageOffer-form";
	}
	
	/**
	 * 保存录取文件
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping("/edu3/recruit/matriculate/uploadPackageOffer-save.html")
	public void savePackageOffer(HttpServletRequest request,HttpServletResponse response, ModelMap model){
		List<Dictionary> dictionaries = CacheAppManager.getChildren("CodePackageOffer");
		Map<String, Object> condition = Condition2SQLHelper.getConditionFromResquestByIterator(request);
		Map<String,Object> m = new HashMap<String, Object>();
		try {
			for (Dictionary dic : dictionaries) {
				if(condition.containsKey(dic.getDictCode())){
					Attachs attachs = attachsService.findUniqueByProperty("formId", dic.getDictCode());
					dic.setDictValue(attachs.getAttName());
					//保存中文文件名，供打包下载
					String serPath = attachs.getSerPath();
					ExFileUtils.copy(serPath+File.separator+attachs.getSerName(), serPath+File.separator+attachs.getAttName());
				}
			}
			m.put("statusCode", 200);
			m.put("message", "保存成功！");
		} catch (Exception e) {
			logger.error("保存录取文件出错：{}"+e.fillInStackTrace());
			m.put("statusCode", 300);
			m.put("message", "保存失败");
		}
		
		dictionaryService.batchSaveOrUpdate(dictionaries);
		UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), "2", UserOperationLogs.INSERT, "上传录取文件");
		renderJson(response,JsonUtils.mapToJson(m));
	}
}
