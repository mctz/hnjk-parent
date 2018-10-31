package com.hnjk.edu.teaching.controller;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.SystemUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.cache.EhCacheManager;
import com.hnjk.core.foundation.utils.ExCollectionUtils;
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.GUIDUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.BaseSupportController;
import com.hnjk.core.support.context.Constants;
import com.hnjk.core.support.context.SystemContextHolder;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.basedata.service.IGradeService;
import com.hnjk.edu.learning.service.IActiveCourseExamService;
import com.hnjk.edu.learning.service.IBbsTopicService;
import com.hnjk.edu.learning.service.ILearningTimeSettingService;
import com.hnjk.edu.learning.service.IStudentActiveCourseExamService;
import com.hnjk.edu.learning.service.IStudentExerciseService;
import com.hnjk.edu.learning.service.IStudentLearnPlanService;
import com.hnjk.edu.teaching.model.ExamResults;
import com.hnjk.edu.teaching.model.ExamResultsLog;
import com.hnjk.edu.teaching.model.ExamSub;
import com.hnjk.edu.teaching.model.LearningTimeSetting;
import com.hnjk.edu.teaching.model.TeachingPlanCourse;
import com.hnjk.edu.teaching.model.UsualResults;
import com.hnjk.edu.teaching.model.UsualResultsRule;
import com.hnjk.edu.teaching.service.ICourseService;
import com.hnjk.edu.teaching.service.ITeachingPlanCourseStatusService;
import com.hnjk.edu.teaching.service.IUsualResultsRuleService;
import com.hnjk.edu.teaching.service.IUsualResultsService;
import com.hnjk.edu.teaching.vo.ExamResultsVo;
import com.hnjk.edu.teaching.vo.UsualResultsVo;
import com.hnjk.edu.util.ProjectHelper;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.taglib.JstlCustomFunction;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.cache.CacheSecManager;
import com.hnjk.security.model.OrgUnit;
import com.hnjk.security.model.Role;
import com.hnjk.security.model.User;
import com.hnjk.security.service.IOrgUnitService;
import com.hnjk.security.service.IRoleService;
import com.hnjk.security.service.impl.RoleServiceImpl;
/**
 * 学生平时成绩管理
 * <code>UsualResultsController</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-12-21 下午02:20:49
 * @see 
 * @version 1.0
 */
@Controller
public class UsualResultsController extends BaseSupportController {

	private static final long serialVersionUID = -6511266791168663160L;
	
	@Autowired
	@Qualifier("usualResultsService")
	private IUsualResultsService usualResultsService;
	
	@Autowired
	@Qualifier("usualResultsRuleService")
	private IUsualResultsRuleService usualResultsRuleService;
	
	@Autowired
	@Qualifier("bbsTopicService")
	private IBbsTopicService bbsTopicService;
	

	@Autowired
	@Qualifier("activeCourseExamService")
	private IActiveCourseExamService activeCourseExamService;
	
	@Autowired
	@Qualifier("courseService")
	private ICourseService courseService;
	
//	@Autowired
//	@Qualifier("studentExerciseService")
//	private IStudentExerciseService studentExerciseService;	
	
	@Autowired
	@Qualifier("studentActiveCourseExamService")
	private IStudentActiveCourseExamService studentActiveCourseExamService;
	
	@Autowired
	@Qualifier("gradeService")
	private IGradeService gradeService;
	
	@Autowired
	@Qualifier("studentExerciseService")
	private IStudentExerciseService studentExerciseService;
	
	@Autowired
	@Qualifier("teachingPlanCourseStatusService")
	private ITeachingPlanCourseStatusService teachingPlanCourseStatusService;
	
	@Autowired
	@Qualifier("roleService")
	private IRoleService roleService;
	
	@Autowired
	@Qualifier("orgUnitService")
	private IOrgUnitService orgUnitService;
	
	@Autowired
	@Qualifier("learningTimeSettingService")
	ILearningTimeSettingService learningTimeSettingService;
	
	/**
	 * 学生平时成绩列表
	 * @param objPage
	 * @param request
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/usualresults/list.html")
	public String listUsualResults(Page objPage,HttpServletRequest request,ModelMap model) throws WebException{
		objPage.setOrderBy("y.firstYear desc,t.orderexamterm desc,c.coursecode,s.studyNo");
		objPage.setOrder(Page.ASC);//设置默认排序方式
		
		//查询条件
		Map<String,Object> condition = new HashMap<String,Object>();
		String brSchoolid = request.getParameter("brSchoolid");//教学站id
		String yearInfo = request.getParameter("examOrderYearInfo");
		String term = request.getParameter("examOrderTerm");
		String name = request.getParameter("name");//姓名
		String studyNo = request.getParameter("studyNo");//学号
		String courseName = request.getParameter("courseName");//课程	
		String courseId = request.getParameter("courseId");//课程id
		String usualStatus = request.getParameter("usualStatus");
		
		if(ExStringUtils.isNotEmpty(brSchoolid)) {
			condition.put("brSchoolid", brSchoolid);
		}
		if(ExStringUtils.isNotEmpty(yearInfo)) {
			condition.put("examOrderYearInfo", yearInfo);
		}
		if(ExStringUtils.isNotEmpty(term)) {
			condition.put("examOrderTerm", term);
		}
		if(ExStringUtils.isNotEmpty(courseId)) {
			condition.put("courseId", courseId);
		}
		if(ExStringUtils.isNotEmpty(courseName)) {
			condition.put("courseName", courseName);
		}
		if(ExStringUtils.isNotEmpty(name)) {
			condition.put("name", name);
		}
		if(ExStringUtils.isNotEmpty(studyNo)) {
			condition.put("studyNo", studyNo);
		}
		if(ExStringUtils.isNotBlank(usualStatus)) {
			condition.put("usualStatus", usualStatus);
		}
		User user = SpringSecurityHelper.getCurrentUser();
		String defaultRule = CacheAppManager.getSysConfigurationByCode("usualResultsDefaultRule").getParamValue().trim();
		try {
			condition = ProjectHelper.accessDataFilterCondition(condition, user);
			condition.put("studentStatus", "'11','16','25'");
			condition.put("hasResource","Y");
			model.addAttribute("condition", condition);
			List<Map<String, Object>> list = activeCourseExamService.getTeacherOnlineCourse(condition);
			StringBuffer SBFcourse = new StringBuffer("<select style='width:240px;' class='flexselect' id='usualresults_courseId' name='courseId' >");
			SBFcourse.append("<option value=''>请选择</option>");
			if(ExCollectionUtils.isNotEmpty(list) && list.size() > 0){
				for(Map<String, Object> map : list){
					if(map.get("courseid").equals(courseId)){
						SBFcourse.append("<option value='"+map.get("courseid")+"' selected='selected'>"+map.get("coursecode")+"-"+map.get("coursename")+"</option>");
					}else{
						SBFcourse.append("<option value='"+map.get("courseid")+"'>"+map.get("coursecode")+"-"+map.get("coursename")+"</option>");
					}
				}
			}
			SBFcourse.append("</select>");
			model.put("usualresultslistCourseSelect", SBFcourse.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(ExStringUtils.isNotBlank(courseId) && ExStringUtils.isNotBlank(yearInfo) && ExStringUtils.isNotBlank(term)){			
			Page page = usualResultsService.findUsualResultsVoByCondition(condition, objPage);
			model.addAttribute("usualResultsList", page);
						
			UsualResultsRule currentRule = usualResultsRuleService.getUsualResultsRuleByCourseAndYear(courseId,yearInfo,term);
			
			if(currentRule==null && ExStringUtils.isNotBlank(defaultRule) && defaultRule.split(":").length==3){
				currentRule = new UsualResultsRule(defaultRule.split(":"));
			}
			model.addAttribute("currentRule", currentRule);
			LearningTimeSetting learningTime = learningTimeSettingService.findLearningTimeSetting(yearInfo, term);
			if(learningTime!=null){
				model.addAttribute("learningTime", learningTime);
			}
			try {
				if(currentRule==null){
					Course course = courseService.get(courseId);
					String errorMsg = "课程:"+course.getCourseName()+"不存在学生平时分积分规则,请先建立!";
					model.addAttribute("errorMsg", errorMsg);
				}
				
				Map<String,Object> map = new HashMap<String,Object>();					
				for (Object obj : page.getResult()) {				
					UsualResultsVo vo = (UsualResultsVo)obj;								
					if(null != currentRule){
						originalResults(map, vo, currentRule);//找出原始成绩
					} else {
						//Double originalExerciseResults = studentExerciseService.avgStudentExerciseResult(courseId, vo.getStudentId(),yearInfo,term);
						map.put(vo.getStudentLearnPlanId(), new UsualResults(0.0,0.0,0.0,0.0));//找不到规则默认为0
					}			
				}
				model.addAttribute("originalResults", map);
			} catch (Exception e) {
			}			
		}
		//遍历组织单位缓存,这边想如果是辅导老师登录,只显示该辅导老师辅导的教学点
		StringBuffer bf = new StringBuffer();
		bf.append("<select style='width:240px;' class='flexselect' id='usualresults_brSchoolid' name='brSchoolid'>");
		bf.append("<option value=\"\">请选择</option>");
		
		boolean flag = false;

		String teacherId = "";
		if(SpringSecurityHelper.isUserInRole(CacheAppManager.getSysConfigurationByCode("sysuser.role.teacher").getParamValue())){
			teacherId = user.getResourceid();
			flag = true;
		}
		/*if(SpringSecurityHelper.isUserInRole(CacheAppManager.getSysConfigurationByCode("sysuser.role.teacher").getParamValue())){
			teacherId = "";
			flag = false;
		}*/
		if(flag){
			List<OrgUnit> orgList = orgUnitService.findByHql("select distinct org from OrgUnit org,Classes c,TeachingPlanCourseTimetable t where c.brSchool.resourceid=org.resourceid and t.classes.resourceid=c.resourceid and t.teacherId=? and t.isDeleted=0", teacherId);
			if(null != orgList && orgList.size()>0){
				for(OrgUnit orgUnit : orgList){
//					if("brschool".equals(scope)){
						if(!CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(orgUnit.getUnitType())){//过滤非校外学习中心的
							continue;
						}
//					}
					bf.append("<option value='"+orgUnit.getResourceid()+"'");
					if(ExStringUtils.isNotEmpty((String) condition.get("brSchoolid")) && ((String) condition.get("brSchoolid")).equals(orgUnit.getResourceid())){
						bf.append(" selected='selected'");
					}
				
//					if("name".equalsIgnoreCase(ExStringUtils.trimToEmpty(displayType))){
//						bf.append(">"+orgUnit.getUnitName()+"</option>");
//					} else {
						bf.append(">"+orgUnit.getUnitCode()+"-"+orgUnit.getUnitName()+"</option>");
//					}						
				}
			}
		}else{
			//List<OrgUnit> orgList = (List<OrgUnit>)EhCacheManager.getCache(CacheSecManager.CACHE_SEC_ORGS).get(CacheSecManager.CACHE_SEC_ORGS);
			List<OrgUnit> orgList = orgUnitService.findByHql(" from "+OrgUnit.class.getSimpleName()+" where isDeleted= 0 and unitType ='brSchool' order by unitCode asc ");
			if(null != orgList && orgList.size()>0){
				for(OrgUnit orgUnit : orgList){
//					if("brschool".equals(scope)){
						if(!CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(orgUnit.getUnitType())){//过滤非校外学习中心的
							continue;
						}
//					}
					bf.append("<option value='"+orgUnit.getResourceid()+"'");
					if(ExStringUtils.isNotEmpty((String) condition.get("brSchoolid")) && ((String) condition.get("brSchoolid")).equals(orgUnit.getResourceid())){
						bf.append(" selected='selected'");
					}
				
//					if("name".equalsIgnoreCase(ExStringUtils.trimToEmpty(displayType))){
//						bf.append(">"+orgUnit.getUnitName()+"</option>");
//					} else {
						bf.append(">"+orgUnit.getUnitCode()+"-"+orgUnit.getUnitName()+"</option>");
//					}						
				}
			}
		}
				
		bf.append("</select>");
		model.addAttribute("brschools", bf.toString());
		model.addAttribute("defaultRule", defaultRule);
		model.addAttribute("nowYear", ExDateUtils.getCurrentYear());
		return "/edu3/teaching/usualresults/usualresults-list";
	}
	/**
	 * 计算原始分
	 * @param map
	 * @param plan
	 * @param rule
	 */
	public void originalResults(Map<String, Object> map, UsualResultsVo vo, UsualResultsRule rule) {
		String userId = vo.getSysUserId();
		Map<String,Object> condition = new HashMap<String,Object>();
		condition.put("courseId", vo.getCourseId());
		condition.put("isBest", "Y");
		condition.put("userId", ExStringUtils.trimToEmpty(userId));
		String sectionCode = CacheAppManager.getSysConfigurationByCode("web.bbs.sectioncode").getParamValue();
				
		Double originalBbsResults = 0.0;//网络辅导分						
		Double originalAskQuestionResults = 0.0;//随堂问答分
		Double originalExerciseResults = 0.0;//作业练习分
		Double originalCourseExamResults = 0.0;//随堂练习分
		if(rule.getCourseExamResultPer()>0&& (ExStringUtils.isBlank(vo.getUsualResultsId()) ||ExStringUtils.isBlank(vo.getCourseExamResults()))){//随堂练习分
			Double per = studentActiveCourseExamService.avgStudentActiveCourseExamResult(vo.getCourseId(), vo.getStudentId());				
			originalCourseExamResults = per*100>=rule.getCourseExamCorrectPer()?100:per*100;
		}
		if(rule.getExerciseResultPer()>0 && (ExStringUtils.isBlank(vo.getUsualResultsId()) ||ExStringUtils.isBlank(vo.getExerciseResults()))){//作业练习分
			originalExerciseResults = studentExerciseService.avgStudentExerciseResult(vo.getCourseId(), vo.getStudentId(),vo.getYearInfoId(),vo.getTerm());
		}
		//随堂问答列入积分 并且 成绩还未录入
		if(rule.getAskQuestionResultPer()>0  && rule.getBbsBestTopicNum()>0 && (ExStringUtils.isBlank(vo.getUsualResultsId()) ||ExStringUtils.isBlank(vo.getAskQuestionResults()))){	//随堂问答分						
			condition.put("sectionCode", sectionCode);
			Integer askCount = bbsTopicService.statTopicAndReply(condition);//随堂问答
			originalAskQuestionResults = askCount>=rule.getBbsBestTopicNum()?100.0:askCount*rule.getBbsBestTopicResult();
		}
		if(rule.getBbsResultPer()>0 && rule.getBbsBestTopicNum()>0 && (ExStringUtils.isBlank(vo.getUsualResultsId()) ||ExStringUtils.isBlank(vo.getBbsResults()))){//网络辅导分
			condition.remove("sectionCode");
			condition.put("notSectionCode", sectionCode);
			Integer bbsCount = bbsTopicService.statTopicAndReply(condition);//网络辅导
			originalBbsResults = bbsCount>=rule.getBbsBestTopicNum()?100.0:bbsCount*rule.getBbsBestTopicResult();	
		}
		map.put(vo.getStudentLearnPlanId(), new UsualResults(originalBbsResults,originalExerciseResults,originalAskQuestionResults,originalCourseExamResults));
	}
	/**
	 * 保存学生平时成绩
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/usualresults/save.html")
	public void saveUsualResults(HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		try {	
			String[] planId = request.getParameterValues("planId");	
			String[] isDealed = request.getParameterValues("isDealed");
			String[] bbsResults = request.getParameterValues("bbsResults");	
			String[] exerciseResults = request.getParameterValues("exerciseResults");	
			String[] selftestResults = request.getParameterValues("selftestResults");	
			String[] otherResults = request.getParameterValues("otherResults");	
			String[] faceResults = request.getParameterValues("faceResults");	
			String[] askQuestionResults = request.getParameterValues("askQuestionResults");	
			String[] courseExamResults = request.getParameterValues("courseExamResults");	
			
			usualResultsService.batchSaveOrUpdateUsualResults(planId, isDealed, bbsResults, exerciseResults, selftestResults, otherResults, faceResults, askQuestionResults, courseExamResults);
			map.put("statusCode", 200);
			map.put("message", "保存成功！");
			map.put("forward", request.getContextPath()+"/edu3/teaching/usualresults/list.html");
		}catch (Exception e) {
			logger.error("保存学生平时成绩出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败！<br/>"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 学生平时分积分规则是否已经建立
	 * @param courseIds
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/usualresultsrule/exist.html")
	public void ajaxIsExistUsualResultsRule(String courseIds,HttpServletRequest request,HttpServletResponse response) throws WebException{
		String msg = "";
		if(ExStringUtils.isNotEmpty(courseIds)){
			String[] ids = courseIds.split(",");			
			for (String id : ids) {
				UsualResultsRule rule =  usualResultsRuleService.getUsualResultsRuleByCourse(id);
				if(rule==null){
					Course course = courseService.get(id);
					msg += course.getCourseName()+",";
				}
			}
		}		
		if(ExStringUtils.isNotEmpty(msg)){
			msg = msg.substring(0,msg.length()-1);
			msg += "等课程不存在学生平时分积分规则,请先建立!";
		}
		renderJson(response, msg);
	}
	/**
	 * 提交平时成绩
	 * @param resourceid
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/usualresults/submit.html")
	public void submitUsualResults(String resourceid,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		try {			
			if(ExStringUtils.isNotBlank(resourceid)){			
				usualResultsService.submitUsualResults(resourceid.split("\\,"));
				map.put("statusCode", 200);
				map.put("message", "提交成功！");		
			}
		} catch (Exception e) {
			map.put("statusCode", 300);
			if(e.getClass().equals(ServiceException.class)){
				map.put("message", e.getMessage());
			}else{
				logger.error("提交课程平时成绩失败:{}",e.fillInStackTrace());
				map.put("message", "提交平时成绩失败！");
			}
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 提交当期学期课程所有平时成绩
	 * @param resourceid
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/usualresults/all/save.html")
	public void saveAllUsualResults(String yearInfoId,String term,String courseId,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		try {			
			if(ExStringUtils.isNotBlank(yearInfoId) && ExStringUtils.isNotBlank(term) && ExStringUtils.isNotBlank(courseId)){			
				usualResultsService.saveAllUsualResults(yearInfoId,term,courseId);
				map.put("statusCode", 200);
				map.put("message", "保存成功！");	
			} else {
				map.put("statusCode", 300);
				map.put("message", "请选择年度、学期和课程！");	
			}
		} catch (Exception e) {
			logger.error("保存课程平时成绩失败:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败：<br/>"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 提交课程平时成绩
	 * @param yearInfoId
	 * @param term
	 * @param courseId
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/usualresults/all/submit.html")
	public void submitAllUsualResults(String unitId,String yearInfoId,String term,String courseId,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		String teacherId = null;
		//String unitId = null;
		try {			
			if(ExStringUtils.isNotBlank(yearInfoId) && ExStringUtils.isNotBlank(term) && ExStringUtils.isNotBlank(courseId)){		
				User user = SpringSecurityHelper.getCurrentUser();
				boolean isDuty =  SpringSecurityHelper.isUserInRole(CacheAppManager.getSysConfigurationByCode("sysuser.role.teacher.duty").getParamValue());
				if(!isDuty&&CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())){//如果为校外学习中心人员
					unitId = user.getOrgUnit().getResourceid();
					teacherId = user.getResourceid();
				} else {
					if(isDuty || SpringSecurityHelper.isTeachingCentreTeacher(user)){
						teacherId = user.getResourceid();
					}
				}
				
				int submitNum = usualResultsService.submitAllUsualResults(yearInfoId,term,courseId,teacherId,unitId,"submit");
				map.put("statusCode", 200);
				map.put("message", "提交成功！共成功提交 "+submitNum+" 条平时成绩.");	
			} else {
				map.put("statusCode", 300);
				map.put("message", "请选择年度、学期和课程！");	
			}
		} catch (Exception e) {
			map.put("statusCode", 300);
			if(e.getClass().equals(ServiceException.class)){
				map.put("message", e.getMessage());
			}else{
				logger.error("提交课程平时成绩失败:{}",e.fillInStackTrace());
				map.put("message", "提交平时成绩失败！");
			}
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 查看某个学生的某门课程网上学习成绩
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/usualresults/viewOnlineScore_student.html")
	public String viewOnlineScore(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws WebException {
		String studentId = request.getParameter("studentId");
		String courseId = request.getParameter("courseId");
		String planCourseId = request.getParameter("planCourseId");
		String planId = request.getParameter("planId");
		String gradeId = request.getParameter("gradeId");
		String schoolId = request.getParameter("schoolId");
		String noOpenCourse = null;
		
		try {
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("planCourseId", planCourseId);
			condition.put("planId", planId);
			condition.put("gradeId", gradeId);
			condition.put("schoolId", schoolId);
			Map<String,Object> yearInfoTerm = teachingPlanCourseStatusService.findYearInfoAndTerm(condition);
			UsualResults usualResults = new UsualResults();
			UsualResultsRule currentRule = null;
			if(yearInfoTerm!=null && yearInfoTerm.size() > 0){
				String yearInfo = (String)yearInfoTerm.get("yearInfo");
				String term = (String)yearInfoTerm.get("term");
				condition.put("studentId", studentId);
				condition.put("yearInfoId", yearInfo);
				condition.put("term", term);
				condition.put("courseId", courseId);
				condition.put("status", "1");
				List<UsualResults> usualResultsList = usualResultsService.findByCondition(condition);
				
				if(ExCollectionUtils.isNotEmpty(usualResultsList)){
					usualResults = usualResultsList.get(0);
				}
				
				currentRule = usualResultsRuleService.getUsualResultsRuleByCourseAndYear(courseId,yearInfo,term);
			} else {
				noOpenCourse = "该门课程还未开课！";
			}
			model.addAttribute("noOpenCourse", noOpenCourse);
			model.addAttribute("usualResultsRule", currentRule);
			model.addAttribute("usualResults", usualResults);
		} catch (Exception e) {
			logger.error("查看某个学生的某门课程网上学习成绩出错", e);
		}
		
		return "/edu3/learning/studySituation/viewOnlineScore_student";
	}
	
	
}
