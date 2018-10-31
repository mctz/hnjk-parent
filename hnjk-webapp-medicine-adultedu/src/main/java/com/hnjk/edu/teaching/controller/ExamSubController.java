package com.hnjk.edu.teaching.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.utils.ExBeanUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.HttpHeaderUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.configuration.property.ConfigPropertyUtil;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.BaseSupportJdbcDao;
import com.hnjk.core.support.base.controller.BaseSupportController;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.basedata.service.IYearInfoService;
import com.hnjk.edu.learning.model.CourseExamRules;
import com.hnjk.edu.learning.service.ICourseExamRulesService;
import com.hnjk.edu.teaching.helper.ComparatorExamDate;
import com.hnjk.edu.teaching.helper.ExamCourseCodeGenerator;
import com.hnjk.edu.teaching.model.ExamInfo;
import com.hnjk.edu.teaching.model.ExamSub;
import com.hnjk.edu.teaching.service.ICourseService;
import com.hnjk.edu.teaching.service.IExamInfoService;
import com.hnjk.edu.teaching.service.IExamSubService;
import com.hnjk.edu.util.Condition2SQLHelper;
import com.hnjk.platform.system.UserOperationLogsHelper;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.Dictionary;
import com.hnjk.platform.system.model.UserOperationLogs;
import com.hnjk.platform.system.service.IDictionaryService;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.OrgUnit;
import com.hnjk.security.model.User;

import edu.emory.mathcs.backport.java.util.Collections;


/**
 * 考试/毕业论文批次预约表
 * <code>ExamSubController</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-8-17 下午03:58:46
 * @see 
 * @version 1.0
 */
@Controller
public class ExamSubController extends BaseSupportController{
	
	private static final long serialVersionUID = 1L;

	@Autowired
	@Qualifier("examSubService")
	private IExamSubService examSubService;
	
	@Autowired
	@Qualifier("yearInfoService")
	private IYearInfoService yearInfoService;
	
	@Autowired
	@Qualifier("examInfoService")
	private IExamInfoService examInfoService;
	
	@Autowired
	@Qualifier("courseExamRulesService")
	private ICourseExamRulesService courseExamRulesService;
	
	@Autowired
	@Qualifier("courseService")
	private ICourseService courseService;
	
	@Autowired
	@Qualifier("dictService")
	private IDictionaryService dictionaryService;
	
	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private BaseSupportJdbcDao baseSupportJdbcDao;
	
	
	@RequestMapping("/edu3/teaching/exam/plan/list.html")
	public String exeList(HttpServletRequest request,HttpServletResponse response, Page objPage, ModelMap model) throws WebException{
		
//		objPage.setOrderBy("yearInfo.firstYear desc,term desc, examinputStartTime desc, examType ");
//		objPage.setOrder(Page.DESC);//设置默认排序方式
		objPage.setOrderBy("yearInfo.firstYear,term, examinputStartTime, examType ");
		objPage.setOrder(Page.ASC);//设置默认排序方式
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件
		String yearId				 = ExStringUtils.trimToEmpty(request.getParameter("yearId"));
		String term					 = ExStringUtils.trimToEmpty(request.getParameter("term"));
		String startTime 			 = ExStringUtils.trimToEmpty(request.getParameter("startTime"));
		String endTime				 = ExStringUtils.trimToEmpty(request.getParameter("endTime"));
		String examsubStatus         = ExStringUtils.trimToEmpty(request.getParameter("examsubStatus"));
		String examType         = ExStringUtils.trimToEmpty(request.getParameter("examType"));
		
		//如果是学习中心用户，只操作本学习中心的数据
		User user 					 = SpringSecurityHelper.getCurrentUser();
		if (ConfigPropertyUtil.getInstance().getProperty("brSchool.unitType").equals(user.getOrgUnit().getUnitType())) {
			condition.put("brshSchool",user.getOrgUnit().getResourceid());
		}
		
		
		if(ExStringUtils.isNotEmpty(yearId)) {
			condition.put("yearId", yearId);
		}
		if(ExStringUtils.isNotEmpty(term)) {
			condition.put("term", term);
		}
		if (ExStringUtils.isNotEmpty(startTime)) {
			condition.put("startTime", startTime);
		}
		if (ExStringUtils.isNotEmpty(endTime)) {
			condition.put("endTime", endTime);
		}
		if (ExStringUtils.isNotEmpty(examsubStatus)) {
			condition.put("status",examsubStatus);
		}
		if (ExStringUtils.isNotEmpty(examType)) {
			condition.put("examType",examType);
		}
		
		Page page = examSubService.findExamSubByCondition(condition, objPage);
		
		model.addAttribute("examSubList", page);
		model.addAttribute("condition", condition);
		return "/edu3/teaching/examSub/examSub-list";
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
	@RequestMapping("/edu3/teaching/exam/plan/input.html")
	public String exeEdit(String resourceid,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		String facestudyScorePer = CacheAppManager.getSysConfigurationByCode("facestudyScorePer").getParamValue();
		String facestudyScorePer2 = CacheAppManager.getSysConfigurationByCode("facestudyScorePer2").getParamValue();
		String facestudyScorePer3 = CacheAppManager.getSysConfigurationByCode("facestudyScorePer3").getParamValue();
		if(ExStringUtils.isNotBlank(resourceid)){ //-----编辑
			
			User user 					    = SpringSecurityHelper.getCurrentUser();
			String modifyScoreType		    = Constants.BOOLEAN_NO;//用于控制页面中成绩类型的修改的开关
			String modifyRoles              = CacheAppManager.getSysConfigurationByCode("courseScoreType.modify.roles").getParamValue();
			for (String role :modifyRoles.split(",")) {
				if (user.isContainRole(role)) {
					modifyScoreType         = Constants.BOOLEAN_YES;
				}
			}
			ExamSub examSub 			    = examSubService.get(resourceid);	
			boolean isHasOnlineExamInfo     = false;
			boolean isHasFaceExamInfo 	    = false;
			boolean isHasFaceAndNetExamInfo = false;
			boolean isHasCBSExamInfo = false;
			boolean isHasOnlineAdultExamInfo = false;
			for (ExamInfo examInfo : examSub.getExamInfo()) {
				if(examInfo.getExamCourseType()!=null && 0==examInfo.getExamCourseType()){//网络
					isHasOnlineExamInfo   = true;
					continue;
				}
				if (null!=examInfo.getExamCourseType() && 1==examInfo.getExamCourseType()) {//面授
					isHasFaceExamInfo = true;
					continue;
				}
				if (null!=examInfo.getExamCourseType() && 2==examInfo.getExamCourseType()) {//面授+网络
					isHasFaceAndNetExamInfo = true;
					continue;
				}
				if(null!=examInfo.getExamCourseType() && 3==examInfo.getExamCourseType()){//机考
					isHasCBSExamInfo   = true;
					continue;
				}
				if (null!=examInfo.getExamCourseType() && 4==examInfo.getExamCourseType()) {//网络成人课程
					isHasOnlineAdultExamInfo = true;
					continue;
				}
			}
			List<Dictionary> list_dict = CacheAppManager.getChildren("CodeExamInfoCourseType");
			model.addAttribute("examSub", examSub);
			model.addAttribute("list_dict", list_dict);
			model.addAttribute("modifyScoreType", modifyScoreType);
			
			model.addAttribute("isHasFaceExamInfo",isHasFaceExamInfo);
			model.addAttribute("isHasOnlineExamInfo", isHasOnlineExamInfo);
			model.addAttribute("isHasCBSExamInfo", isHasCBSExamInfo);
			model.addAttribute("isHasFaceAndNetExamInfo", isHasFaceAndNetExamInfo);
			model.addAttribute("isHasOnlineAdultExamInfo", isHasOnlineAdultExamInfo);
			model.addAttribute("AddOrUpdate", "update");
			model.addAttribute("currentIndex", request.getParameter("currentIndex"));
		}else{ //----------------------------------------新增
			ExamSub examSub =  new ExamSub();
			examSub.setFacestudyScorePer(60.00);
			examSub.setFacestudyScorePer2(40.00);
			if(ExStringUtils.isNumeric(facestudyScorePer,2)){//面授卷面
				examSub.setFacestudyScorePer(Double.valueOf(facestudyScorePer));
				//examSub.setFacestudyScorePer2(100-Double.valueOf(facestudyScorePer));
			}
			if (ExStringUtils.isNumeric(facestudyScorePer2,2)) {
				examSub.setFacestudyScorePer2(Double.valueOf(facestudyScorePer2));
				//examSub.setFacestudyScorePer(100-Double.valueOf(facestudyScorePer2));
			}
			if(ExStringUtils.isNumeric(facestudyScorePer3,2)){
				examSub.setNetsidestudyScorePer(100-Double.valueOf(facestudyScorePer3));
				examSub.setWrittenScorePer(Double.valueOf(facestudyScorePer3));
			}else {
				examSub.setNetsidestudyScorePer(50.00);
				examSub.setWrittenScorePer(50.00);
			}
			model.addAttribute("examSub",examSub);
			model.addAttribute("AddOrUpdate", "add");
		}
		return "/edu3/teaching/examSub/examSub-form";
	}
	
	/**
	 * 保存更新表单
	 * @param grade
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/exam/plan/save.html")
	public void exeSave(ExamSub examSub,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		if("2".equals(examSub.getExamsubStatus()) &&!examSubService.isUnique(examSub))
		{	map.put("statusCode", 300);
		    map.put("message", "同一时间范围内只能有一个开放的批次，请关闭同一时间段的开放批次或把本批次状态设为关闭！");
			renderJson(response, JsonUtils.mapToJson(map));
		    return;
		}
		try {
			if(ExStringUtils.isNotBlank(examSub.getResourceid())){ //--------------------更新
				
				ExamSub p_ExamSub = examSubService.get(examSub.getResourceid());
				String  p_type    = p_ExamSub.getCourseScoreType();
				Double  p_nws	  = p_ExamSub.getWrittenScorePer();
				Double  p_fws	  = p_ExamSub.getFacestudyScorePer();
				Double  p_fws2	  = p_ExamSub.getFacestudyScorePer2();
				Double  p_fws3	  = (Double) ExStringUtils.nvl4Obj(p_ExamSub.getFacestudyScorePer3(), 0.0);
				Double  p_tws     = p_ExamSub.getNetsidestudyScorePer();
				examSub.setYearInfo(yearInfoService.findUniqueByProperty("resourceid", request.getParameter("yearId")));
				examSub.setBatchType("exam");
				ExBeanUtils.copyProperties(p_ExamSub, examSub);
				if (ExStringUtils.isEmpty(examSub.getCourseScoreType())) {
					p_ExamSub.setCourseScoreType(p_type);
				}
				if (null==examSub.getWrittenScorePer()) {
					p_ExamSub.setWrittenScorePer(p_nws);
				}
				if (null==examSub.getFacestudyScorePer()) {
					p_ExamSub.setFacestudyScorePer(p_fws);
				}
				if (null==examSub.getFacestudyScorePer2()) {
					p_ExamSub.setFacestudyScorePer2(p_fws2);
				}
				if (null==examSub.getFacestudyScorePer3()) {
					p_ExamSub.setFacestudyScorePer3(p_fws3);
				}
				if (null==examSub.getNetsidestudyScorePer()) {
					p_ExamSub.setNetsidestudyScorePer(p_tws);
				}
				examSubService.update(p_ExamSub);
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "5",UserOperationLogs.UPDATE, "修改考试批次内容：参数：p_type:"+p_type+"||p_nws:"+p_nws+"||p_fws:"+p_fws+"||p_fws2:"+p_fws2+"||p_fws3:"+p_fws3+"||p_tws:"+p_tws);
			}else{ //-------------------------------------------------------------------保存
				User user               = SpringSecurityHelper.getCurrentUser();
				OrgUnit unit            = null;
				if (CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())) {
					unit                    = user.getOrgUnit();
				}
				if (null!=unit){
					examSub.setBrSchool(unit);
					examSub.setIsSpecial("Y");
				}
				if (null==examSub.getFacestudyScorePer3()) {
					examSub.setFacestudyScorePer3(0.0);
				}
				examSub.setYearInfo(yearInfoService.findUniqueByProperty("resourceid", request.getParameter("yearId")));
				examSub.setBatchType("exam");
				examSubService.save(examSub);
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), UserOperationLogs.INSERT, "5", examSub);
//				if ("N".equals(examSub.getExamType())) {//保存正考考试批次ID
//					examSub.setZkExamSubId(examSub.getResourceid());
//					examSubService.update(examSub);
//				}
			}
			map.put("statusCode", 200);
			map.put("message", "保存成功！");
			map.put("navTabId", "RES_TEACHING_EXAM_PLAN");
			map.put("reloadUrl", request.getContextPath() +"/edu3/teaching/exam/plan/input.html?resourceid="+examSub.getResourceid());
		}catch (Exception e) {
			logger.error("保存考试批次出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}

	
	/**
	 * 关闭列表对象
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/exam/plan/close.html")
	public void exeDelete(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		String resourceid = request.getParameter("resourceid");
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(resourceid)){
				if(resourceid.split("\\,").length >1){//批量关闭					
					examSubService.batchClose(resourceid.split("\\,"));
				}else{//单个关闭
					ExamSub es = examSubService.get(resourceid);
					es.setExamsubStatus("3");
					examSubService.update(es);
				}
				map.put("statusCode", 200);
				map.put("message", "关闭成功！");	
				map.put("forward", request.getContextPath()
						+ "/edu3/teaching/exam/plan/list.html");
			}
		} catch (Exception e) {
			logger.error("关闭考试批次出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "关闭出错！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 开放考试批次
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/teaching/exam/plan/open.html")
	public void exeOpenSub(HttpServletRequest request,HttpServletResponse response){
		
		String resourceid 	   = request.getParameter("resourceid");
		Map<String,Object> map = new HashMap<String, Object>();
		ExamSub sub            = examSubService.load(resourceid);
		if(!examSubService.isUnique(sub)){
			map.put("statusCode", 300);
			map.put("message", "同一时间范围内只能有一个开放的批次，请关闭同一时间段的开放批次或把本批次状态设为关闭！");
		}else {
			sub.setExamsubStatus("2");
			examSubService.saveOrUpdate(sub);
			map.put("statusCode", 200);
			map.put("message", "开放成功！");	
			map.put("forward", request.getContextPath()+ "/edu3/teaching/exam/plan/list.html");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 删除对象
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/exam/plan/remove.html")
	public void exeClose(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		
		String resourceid       = request.getParameter("resourceid");
		Map<String ,Object> map = new HashMap<String, Object>();
		String c_id 			= request.getParameter("c_id");
		try {
			//删除预约考试批次
			if(ExStringUtils.isNotBlank(resourceid)){
				if(resourceid.split("\\,").length >1){//批量删除					
					examSubService.batchCascadeDelete(resourceid.split("\\,"));
				}else{//单个删除
					boolean isLinked = examSubService.isLinkedByExamResults(resourceid);
					if (isLinked) {
						throw new WebException("不允许删除，所选批次已有预约考试记录!");
					}else{
						examSubService.delete(resourceid);
					}
					
				}
				map.put("statusCode", 200);
				map.put("message", "删除成功！");	
				map.put("forward", request.getContextPath()+ "/edu3/teaching/exam/plan/list.html");
			}
			UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "5", UserOperationLogs.DELETE,"删除考试批次：resourceids:"+resourceid);
			//删除考试课程
			if(ExStringUtils.isNotBlank(c_id)){
				if (c_id.split("\\,").length>1) {
					examInfoService.batchCascadeDelete(c_id.split("\\,"));
				}else {
					
					boolean isLinked = examInfoService.isLinkedByExamResults(c_id);
					if (isLinked) {
						throw new WebException("不允许删除，所选课程已有预约考试记录!");
					}else {
						examInfoService.deleteCourse(c_id);
						map.put("statusCode", 200);
						map.put("message", "删除成功！");
						UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), UserOperationLogs.DELETE, "5", "删除考试课程：resourceids:"+c_id);
					}
				}
			}
		} catch (Exception e) {
			logger.error("删除考试批次出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错！<br/>"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 设置课程表单
	 * @param resourceid
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/exam/plan/setting.html")
	public String exeSetting(String resourceid,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		
		User user               = SpringSecurityHelper.getCurrentUser();
		String brschoolId       = "";
		if (CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())) {
			brschoolId              = user.getOrgUnit().getResourceid();
		}
		
		if (ExStringUtils.isNotEmpty(brschoolId)) {
			model.addAttribute("brschoolId",brschoolId);
		}
		model.addAttribute("examSubId", resourceid);
		
		return "/edu3/teaching/examSub/setting-form";
	}
	/**
	 * 保存设置课程
	 * @param resourceid
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 * @throws ParseException 
	 */
	@RequestMapping("/edu3/teaching/exam/plan/saveSetting.html")
	public void exeSaveSetting(String resourceid,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException, ParseException{
		
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			ExamSub examSub     = examSubService.get(resourceid);
			examSubService.relationChildMethod(examSub,request);
			map.put("statusCode", 200);
			map.put("message", "设置成功！");	
			map.put("callbackType", "closeCurrent");
			Map<String ,Object> _map=Condition2SQLHelper.getConditionFromResquestByIterator(request);		
			UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "5", UserOperationLogs.UPDATE,"修改考试批次：参数："+JsonUtils.mapToJson(_map));
		} catch (Exception e) {
			logger.error("设置出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "设置出错！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
//		examSubService.update(examSub);
	}

	/**
	 * 编辑网上考试课程信息
	 * @param c_id
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/framework/teaching/onlineexam/input.html")
	public String editOnlineExamInfo(String c_id,HttpServletResponse response,ModelMap model) throws WebException{
		if(ExStringUtils.isNotBlank(c_id)){ //-----编辑

			ExamInfo examInfo = examInfoService.get(c_id);	
			model.addAttribute("examInfo", examInfo);

			List<CourseExamRules> list = courseExamRulesService.findByHql(" from "+CourseExamRules.class.getSimpleName()+" where isDeleted=0 and course.resourceid=? order by versionNum desc ", examInfo.getCourse().getResourceid());
			model.addAttribute("courseExamRules", list);
		}
		return "/edu3/teaching/examSub/examinfo-form-for-webexam";
	}	
	/**
	 * 保存考试信息
	 * @param resourceid
	 * @param examStartTime
	 * @param examEndTime
	 * @param courseExamRulesId
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/framework/teaching/onlineexam/save.html")
	public void exeSave(String resourceid, Date examStartTime,Date examEndTime, String courseExamRulesId,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(resourceid)){ //--------------------更新
				ExamInfo examInfo = examInfoService.get(resourceid);
				examInfo.setExamStartTime(examStartTime);
				examInfo.setExamEndTime(examEndTime);
				if(ExStringUtils.isNotBlank(courseExamRulesId)){
					CourseExamRules rule = courseExamRulesService.get(courseExamRulesId);
					examInfo.setCourseExamRules(rule);
				}
				map.put("examInfoId",resourceid);
				map.put("isDeleted", 0);

				examInfoService.update(examInfo);
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), UserOperationLogs.INSERT, "5",examInfo );
				map.put("statusCode", 200);
				map.put("message", "保存成功！");
				map.put("closeCurrent", "closeCurrent");
				map.put("navTabId", "RES_TEACHING_EXAM_PLANSETTING_MODIFY");
				map.put("reloadUrl", request.getContextPath() +"/edu3/teaching/exam/plan/input.html?currentIndex=1&resourceid="+examInfo.getExamSub().getResourceid());
			}			
		}catch (Exception e) {
			logger.error("保存考试批次出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败:"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 编辑网络教育考试课程信息
	 * @param c_id
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/framework/teaching/netcourse/input.html")
	public String editExamInfoForNetCourse(String c_id,String examSubId,HttpServletResponse response,ModelMap model) throws WebException{
		if(ExStringUtils.isNotBlank(c_id)){ //-----编辑
			ExamInfo examInfo = examInfoService.get(c_id);	
			model.put("examInfo", examInfo);
		}else {
			ExamSub sub = examSubService.get(examSubId);
			
			model.put("examSub", sub);
		}
		return "/edu3/teaching/examSub/examinfo-form-for-netcourse";
	}	
	
	/**
	 * 保存网络教育考试课程信息
	 * @param resourceid
	 * @param examStartTime
	 * @param examEndTime
	 * @param courseExamRulesId
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/framework/teaching/netcourse/save.html")
	public void sveExamInfoForNetCourse(String resourceid,String examCourseCode, String examSubId,String teachtype,
										String examCourseType,String memo,String course,Date examStartTime,Date examEndTime,
										HttpServletRequest request,HttpServletResponse response){
		
		Map<String,Object> map   = new HashMap<String, Object>();
		try {
			ExamInfo examInfo    = new ExamInfo();
			if(ExStringUtils.isNotBlank(resourceid)){ //--------------------更新
				examInfo 	     = examInfoService.get(resourceid);
				boolean isLinked = examInfoService.isLinkedByExamResults(resourceid);
				
				if (isLinked) {
					throw new WebException("不允许修改考试课程，所选课程已有预约考试记录！");
				}else {
					examInfo.setExamStartTime(examStartTime);
					examInfo.setExamEndTime(examEndTime);	
					examInfo.setExamCourseCode(examCourseCode);
					examInfo.setExamCourseType(Integer.parseInt(examCourseType));
					//examInfo.setTeachtype(Integer.parseInt(examCourseType));
					examInfo.setMemo(memo);
					//examInfo.setTeachtype(teachtype.contains("face")?1:2);
					examInfoService.update(examInfo);
					UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), UserOperationLogs.UPDATE, "5",examInfo );
				}

			}else {
				ExamSub sub  	  = examSubService.get(examSubId);
				Course c          = courseService.get(course);
				String hql = " from "+ExamInfo.class.getSimpleName()+" where isDeleted=0 and course.resourceid=:courseid and examSub.resourceid=:examSubid and examCourseType=:examCourseType";
				Map<String, Object> values = new HashMap<String, Object>();
				values.put("courseid", c.getResourceid());
				values.put("examSubid", sub.getResourceid());
				values.put("examCourseType", Integer.parseInt(examCourseType));
				List examInfoList = examInfoService.findByHql(hql, values);
				//List examInfoList = examInfoService.findByCriteria(Restrictions.eq("isDeleted",0),Restrictions.eq("course",c),Restrictions.eq("examSub",sub),Restrictions.eq("examCourseType",Integer.parseInt(examCourseType)));
				
				if (null!=examInfoList && examInfoList.size()>0) {
					throw new WebException(sub.getBatchName()+"已经存在考试课程:《"+c.getCourseName()+"》");
				}
				examInfo.setExamStartTime(examStartTime);
				examInfo.setExamEndTime(examEndTime);	
				examInfo.setCourse(c);
				examInfo.setExamSub(sub);
				examInfo.setMemo(memo);
				examInfo.setExamCourseType(Integer.parseInt(examCourseType));
				examInfo.setCourseScoreType(sub.getCourseScoreType());
				//examInfo.setTeachtype(teachtype.contains("face")?1:2);
				examInfo.setStudyScorePer(sub.getWrittenScorePer());
				examInfo.setFacestudyScorePer(sub.getFacestudyScorePer());
				examInfo.setFacestudyScorePer2(sub.getFacestudyScorePer2());
				examInfo.setFacestudyScorePer3(sub.getFacestudyScorePer3());
				examInfo.setStudyScorePer(sub.getWrittenScorePer());
				examInfo.setNetsidestudyScorePer(sub.getNetsidestudyScorePer());
				
				sub.getExamInfo().add(examInfo);
				
				examSubService.saveOrUpdate(sub);
			}	
			map.put("statusCode", 200);
			map.put("message", "保存成功！");
			map.put("closeCurrent", "closeCurrent");
			map.put("navTabId", "RES_TEACHING_EXAM_PLANSETTING_MODIFY");
			map.put("reloadUrl", request.getContextPath() +"/edu3/teaching/exam/plan/input.html?currentIndex=1&resourceid="+examInfo.getExamSub().getResourceid());
		}catch (Exception e) {
			logger.error("保存考试批次出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败:"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 生成批定批次有预约记录考试课程的考试课程编号
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/exam/examcode/gen.html")
	public void genExamCode(HttpServletRequest request,HttpServletResponse response)throws WebException{
		
		Map<String,Object> map     = new HashMap<String, Object>();
		Map<Date,Date> examDateMap = new HashMap<Date, Date>();
		String resourceid          = ExStringUtils.trimToEmpty(request.getParameter("resourceid"));
		if (ExStringUtils.isNotEmpty(resourceid)) {
			try {
				ExamSub sub        = examSubService.get(resourceid);
				List<ExamInfo> list1= examInfoService.findExistsExamOrderExamInfos(resourceid);//笔试考试课程
				List<ExamInfo> list2= examInfoService.findByCriteria(Restrictions.eq("isDeleted",0),Restrictions.eq("examSub", sub),Restrictions.eq("isMachineExam",Constants.BOOLEAN_YES));
				if(list1.isEmpty()&&list2.isEmpty()){
					throw new WebException("当前批次还没有预约记录,不允许生成考试编号!");
				}
				for (ExamInfo info:list1) {
					examDateMap.put(info.getExamStartTime(), info.getExamStartTime());
				}
				Set<Date> dateSet  = examDateMap.keySet();
				List<Date> dateList= new ArrayList<Date>(dateSet);
				Collections.sort(dateList, new ComparatorExamDate());
				ExamCourseCodeGenerator generator = ExamCourseCodeGenerator.getInstance();
				String [] prefix                  = generator.genCodePrefix(dateSet.size());
				//生成笔试课程考试编号
				for (int i= 0 ;i<dateList.size();i++) {
					Date examStarTime = dateList.get(i);
					String curPreFix  = prefix[i];
					for (ExamInfo info:list1) {
						if (info.getExamStartTime().getTime() == examStarTime.getTime()) {
							info.setExamCourseCode(curPreFix+info.getCourse().getCourseCode());
							//info.setExamCourseType(0);
							//info.setTeachtype(0);
						}
					}
				}
				//生成机考课程考试编号（固定以Z开始）
				for (ExamInfo info : list2) {
					info.setExamCourseCode("Z"+info.getCourse().getCourseCode());
					//info.setExamCourseType(3);
					//info.setTeachtype(3);
				}
				list1.addAll(list2);
				sub.setExamInfo(new HashSet<ExamInfo>(list1));
				examSubService.saveOrUpdate(sub);
				
				map.put("statusCode", 200);
				map.put("message", "生成成功,点击编辑查看考试课程编号!");
			} catch (Exception e) {
				logger.error("生成批定批次有预约记录考试课程的考试课程编号出错:{}"+e.fillInStackTrace());
				map.put("statusCode", 300);
				map.put("message", "生成考试课程编号失败："+e.getMessage());
			}
		}else {
			map.put("statusCode", 300);
			map.put("message", "请选择一个要生成编号的考试批次！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 设置考试课程成绩比例--课程列表
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/teaching/exam/examinfo-config-page.html")
	public String examScorePerConfigList(HttpServletRequest request,ModelMap model){

		String ids            = ExStringUtils.trimToEmpty(request.getParameter("ids"));
		String pageType       = ExStringUtils.trimToEmpty(request.getParameter("type"));
		String resourceid     = ExStringUtils.trimToEmpty(request.getParameter("resourceid"));
		String currentIndex   = ExStringUtils.trimToEmpty(request.getParameter("currentIndex"));
		String examCoureType  = ExStringUtils.trimToEmpty(request.getParameter("examCoureType"));
		String courseId  	  = ExStringUtils.trimToEmpty(request.getParameter("courseId"));
		ExamSub examSub = examSubService.get(resourceid);
		String forword 	  	  = "/edu3/teaching/examSub/examInfo-config-list";
		if ("list".equals(ExStringUtils.trimToEmpty(pageType))) {
			if (ExStringUtils.isNotEmpty(resourceid)) {
				User user 					    = SpringSecurityHelper.getCurrentUser();
				String modifyScoreType		    = Constants.BOOLEAN_NO;//用于控制页面中成绩类型的修改的开关
				String modifyRoles              = CacheAppManager.getSysConfigurationByCode("courseScoreType.modify.roles").getParamValue();
				for (String role :modifyRoles.split(",")) {
					if (user.isContainRole(role)) {
						modifyScoreType         = Constants.BOOLEAN_YES;
					}
				}
				
				boolean isHasOnlineExamInfo     = false;
				boolean isHasFaceExamInfo 	    = false;
				boolean isHasFaceAndNetExamInfo = false;
				
				Page objPage = new Page();
				objPage.setPageSize(100000);
				Map<String, Object> condition = new HashMap<String, Object>();
				condition.put("examSubId", resourceid);
				if (ExStringUtils.isNotBlank(courseId)) {
					condition.put("courseId", courseId);
				}
				List<ExamInfo> examInfoList = examInfoService.findExamInfoByCondition(condition, objPage).getResult();
				Set<ExamInfo> examInfoSet = new HashSet<ExamInfo>(examInfoList.size());
//				for (ExamInfo examInfo : examSub.getExamInfo()) {
				for (ExamInfo examInfo : examInfoList) {
					examInfoSet.add(examInfo);
					if (null !=examInfo.getExamCourseType()&&1==examInfo.getExamCourseType()) {
						isHasFaceExamInfo = true;
						continue;
					}
					if (null!=examInfo.getExamCourseType()&&2==examInfo.getExamCourseType()) {
						isHasFaceAndNetExamInfo = true;
						continue;
					}
					if(examInfo.getCourse().getExamType()!=null && examInfo.getCourse().getExamType()==6){
						isHasOnlineExamInfo   = true;
						continue;
					}
				}
				examSub.setExamInfo(examInfoSet);
				model.addAttribute("examSub", examSub);
				model.addAttribute("modifyScoreType", modifyScoreType);
				model.addAttribute("isHasFaceExamInfo",isHasFaceExamInfo);
				model.addAttribute("isHasOnlineExamInfo", isHasOnlineExamInfo);
				model.addAttribute("isHasFaceAndNetExamInfo", isHasFaceAndNetExamInfo);
				model.addAttribute("courseId", courseId);
				model.addAttribute("resourceid", resourceid);
				model.addAttribute("pageType", pageType);
			}
		}else if ("setting".equals(ExStringUtils.trimToEmpty(pageType))) {
			forword 			 = "/edu3/teaching/examSub/examInfo-config";
			if (ExStringUtils.isNotBlank(ids)) {
				List<ExamInfo> infos =  examInfoService.findByCriteria(Restrictions.in("resourceid",ids.split(",")));
				model.put("infos", infos);
			}
			User user 					    = SpringSecurityHelper.getCurrentUser();
			String modifyScoreType		    = Constants.BOOLEAN_NO;//用于控制页面中成绩类型的修改的开关
			String modifyRoles              = CacheAppManager.getSysConfigurationByCode("courseScoreType.modify.roles").getParamValue();
			for (String role :modifyRoles.split(",")) {
				if (user.isContainRole(role)) {
					modifyScoreType         = Constants.BOOLEAN_YES;
				}
			}
			model.put("examSubId",resourceid);
			model.put("modifyScoreType", modifyScoreType);
			model.put("examCoureType", examCoureType);
			model.put("ids",ids);
		}
	
		model.addAttribute("currentIndex", currentIndex);
		
		//String facestudyScorePer = CacheAppManager.getSysConfigurationByCode("facestudyScorePer").getParamValue();
		//String facestudyScorePer2 = CacheAppManager.getSysConfigurationByCode("facestudyScorePer2").getParamValue();
		//String facestudyScorePer3 = CacheAppManager.getSysConfigurationByCode("facestudyScorePer3").getParamValue();
		model.addAttribute("facestudyScorePer", examSub.getFacestudyScorePer());
		model.addAttribute("facestudyScorePer2", examSub.getFacestudyScorePer2());
		model.addAttribute("facestudyScorePer3", examSub.getFacestudyScorePer3());
		model.addAttribute("writtenScorePer", examSub.getWrittenScorePer());
		model.addAttribute("netsidestudyScorePer", examSub.getNetsidestudyScorePer());
		return forword;
	}
	/**
	 * 设置考试课程成绩比例、成绩类型
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/teaching/exam/examinfo-config.html")
	public void examScorePerConfig(HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> map  = new HashMap<String, Object>();
		StringBuffer msg         = new StringBuffer();
		String navTabId 		 = ExStringUtils.trimToEmpty(request.getParameter("navTabId"));
		String ids 			     = ExStringUtils.trimToEmpty(request.getParameter("ids"));
		String examSubId         = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
		String currentIndex      = ExStringUtils.trimToEmpty(request.getParameter("currentIndex"));
		String studyScorePer     = ExStringUtils.trimToEmpty(request.getParameter("studyScorePer"));
		String courseScoreType   = ExStringUtils.trimToEmpty(request.getParameter("courseScoreType"));
		String examCoureType     = ExStringUtils.trimToEmpty(request.getParameter("examCoureType"));
		String facestudyScorePer = ExStringUtils.trimToEmpty(request.getParameter("facestudyScorePer"));
		String facestudyScorePer2 = ExStringUtils.trimToEmpty(request.getParameter("facestudyScorePer2"));
		String facestudyScorePer3 = ExStringUtils.trimToEmpty(request.getParameter("facestudyScorePer3"));
		String networkstudy_usuallyScorePer = ExStringUtils.trimToEmpty(request.getParameter("networkstudy_usuallyScorePer"));
		ExamInfo examInfo = new ExamInfo();
		examInfo.setCourseScoreType(courseScoreType);
		if("1".equals(examCoureType)){
			examInfo.setFacestudyScorePer(ExStringUtils.isNotBlank(facestudyScorePer)?Double.parseDouble(facestudyScorePer):0);
			examInfo.setFacestudyScorePer2(ExStringUtils.isNotBlank(facestudyScorePer2)?Double.parseDouble(facestudyScorePer2):0);
		}else {
			examInfo.setStudyScorePer(ExStringUtils.isNotBlank(studyScorePer)?Double.parseDouble(studyScorePer):0);
			examInfo.setNetsidestudyScorePer(ExStringUtils.isNotBlank(networkstudy_usuallyScorePer)?Double.parseDouble(networkstudy_usuallyScorePer):0);
		}
		examInfo.setExamCourseType(ExStringUtils.isNotBlank(examCoureType)?Integer.parseInt(examCoureType):1);
		examInfo.setFacestudyScorePer3(ExStringUtils.isNotBlank(facestudyScorePer3)?Double.parseDouble(facestudyScorePer3):0);
		
		try {
			
			examInfoService.examInfoConfig(ids, examInfo, msg);
			
			if (ExStringUtils.isNotBlank(msg.toString())) {
				map.put("statusCode", 300);
			}else {
				msg.append("操作成功！");
				map.put("statusCode", 200);
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), "5", UserOperationLogs.UPDATE,JsonUtils.mapToJson(Condition2SQLHelper.getConditionFromResquestByIterator(request)) );
			}

			map.put("message", msg);
			if (ExStringUtils.isNotBlank(navTabId) && "examSub".equals(navTabId)) {
				map.put("navTabId", "RES_TEACHING_EXAM_PLANSETTING_MODIFY");
				map.put("reloadUrl", request.getContextPath()
						+ "/edu3/teaching/exam/plan/input.html?resourceid="+examSubId);
			} else {
				map.put("navTabId", "RES_TEACHING_EXAM_PLAN_EXAMSCOREPER_CONFIG");
				map.put("reloadUrl", request.getContextPath()
						+ "/edu3/teaching/exam/examinfo-config-page.html?type=list&currentIndex="+currentIndex+"&resourceid="+examSubId);
			}
		} catch (Exception e) {
			logger.error("保存考试批次出错：{}", e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败:"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}

}
