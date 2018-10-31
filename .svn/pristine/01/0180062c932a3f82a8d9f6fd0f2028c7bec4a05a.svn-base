package com.hnjk.edu.teaching.controller;

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

import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.utils.ExBeanUtils;
import com.hnjk.core.foundation.utils.ExCollectionUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.HttpHeaderUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.controller.BaseSupportController;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.basedata.model.Grade;
import com.hnjk.edu.basedata.model.YearInfo;
import com.hnjk.edu.basedata.service.IGradeService;
import com.hnjk.edu.basedata.service.IYearInfoService;
import com.hnjk.edu.teaching.model.UsualResultsRule;
import com.hnjk.edu.teaching.service.ICourseService;
import com.hnjk.edu.teaching.service.IUsualResultsRuleService;
import com.hnjk.platform.system.UserOperationLogsHelper;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.UserOperationLogs;
import com.hnjk.platform.taglib.JstlCustomFunction;
/**
 * 学生平时分积分规则管理
 * <code>UsualResultsRuleController</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-12-20 下午05:04:00
 * @see 
 * @version 1.0
 */
@Controller
public class UsualResultsRuleController extends BaseSupportController {

	private static final long serialVersionUID = 1521035591347322302L;
	
	@Autowired
	@Qualifier("usualResultsRuleService")
	private IUsualResultsRuleService usualResultsRuleService;
	
	@Autowired
	@Qualifier("courseService")
	private ICourseService courseService;
	
	@Autowired
	@Qualifier("yearInfoService")
	private IYearInfoService yearInfoService;
	
	@Autowired
	@Qualifier("gradeService")
	private IGradeService gradeService;
	
	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;//注入SQL支持
	
	/**
	 * 学生平时分积分规则列表
	 * @param courseName
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/usualresultsrule/list.html")
	public String listUsualResultsRule(String courseId, String courseCode,String yearInfoId,String term,Page objPage, ModelMap model) throws WebException{
		objPage.setOrderBy("course.courseCode,versionNum");
		objPage.setOrder(Page.ASC);//设置默认排序方式
		
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件
		
		/*Grade grade = gradeService.getDefaultGrade();
		if(ExStringUtils.isEmpty(courseId) && grade!=null){
			if(ExStringUtils.isEmpty(yearInfoId)) yearInfoId = grade.getYearInfo().getResourceid();
			if(ExStringUtils.isEmpty(term)) term = grade.getTerm();
		}*/
		String yearTerm = CacheAppManager.getSysConfigurationByCode("sysCurrentGrade").getParamValue();
		String defaultRule = CacheAppManager.getSysConfigurationByCode("usualResultsDefaultRule").getParamValue().trim();
		UsualResultsRule currentRule = null;
		
		if(ExStringUtils.isNotBlank(defaultRule) && defaultRule.split(":").length==3){
			currentRule = new UsualResultsRule(defaultRule.split(":"));
		}
		model.addAttribute("currentRule", currentRule);
		if(ExStringUtils.isEmpty(courseId) && null!=yearTerm){
			String[] ARRYyterm = yearTerm.split("\\.");			
			if(ExStringUtils.isEmpty(yearInfoId)) {
				YearInfo year = yearInfoService.getByFirstYear(Long.parseLong(ARRYyterm[0]));
				if(year!=null){
					yearInfoId = year.getResourceid();
				}
			}
			if(ExStringUtils.isEmpty(term)) {
				term = ARRYyterm[1];
			}
		}
		
		courseCode = ExStringUtils.trimToEmpty(courseCode);
		if(ExStringUtils.isNotEmpty(courseId)) {
			condition.put("courseId", courseId);
		}
		if(ExStringUtils.isNotEmpty(courseCode)) {
			condition.put("courseCode", courseCode);
		}
		if(ExStringUtils.isNotEmpty(yearInfoId)) {
			condition.put("yearInfoId", yearInfoId);
		}
		if(ExStringUtils.isNotEmpty(term)) {
			condition.put("term", term);
		}
		
		Page page = usualResultsRuleService.findUsualResultsRuleByCondition(condition, objPage);
		
		model.addAttribute("defaultRule", defaultRule);
		model.addAttribute("usualResultsRuleList", page);
		model.addAttribute("condition", condition);
		return "/edu3/teaching/usualresultsrule/usualresultsrule-list";
	}
	/**
	 * 初次生成
	 * @param request
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/usualresultsrule/init.html")
	public String editUsualResultsRule(HttpServletRequest request,ModelMap model) throws WebException{
		/*Grade grade = gradeService.getDefaultGrade();
		if(grade!=null){
			usualResultsRule.setYearInfo(grade.getYearInfo());
			usualResultsRule.setTerm(grade.getTerm());
		}	*/
		String defaultRule = CacheAppManager.getSysConfigurationByCode("usualResultsDefaultRule").getParamValue().trim();
		UsualResultsRule usualResultsRule = null;
		
		if(ExStringUtils.isNotBlank(defaultRule) && defaultRule.split(":").length==3){
			usualResultsRule = new UsualResultsRule(defaultRule.split(":"));
		}else {
			usualResultsRule = new UsualResultsRule();
		}
		String yearTerm = CacheAppManager.getSysConfigurationByCode("sysCurrentGrade").getParamValue();
		
		if(null!=yearTerm){
			String[] ARRYyterm = yearTerm.split("\\.");
			usualResultsRule.setYearInfo(yearInfoService.getByFirstYear(Long.parseLong(ARRYyterm[0])));
			usualResultsRule.setTerm(ARRYyterm[1]);
		}
		model.addAttribute("defaultRule", defaultRule);
		model.addAttribute("usualResultsRule", usualResultsRule);
		return "/edu3/teaching/usualresultsrule/usualresultsrule-init";
	}	
	
	/**
	 * 新增编辑学生平时分积分规则
	 * @param resourceid
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/usualresultsrule/input.html")
	public String editUsualResultsRule(String resourceid,ModelMap model) throws WebException{
		String defaultRule = CacheAppManager.getSysConfigurationByCode("usualResultsDefaultRule").getParamValue().trim();
		
		if(ExStringUtils.isNotBlank(resourceid)){ //-----编辑
			UsualResultsRule usualResultsRule = usualResultsRuleService.get(resourceid);
			model.addAttribute("usualResultsRule", usualResultsRule);
		} else {
			UsualResultsRule usualResultsRule = null;
			/*Grade grade = gradeService.getDefaultGrade();
			if(grade!=null){
				usualResultsRule.setYearInfo(grade.getYearInfo());
				usualResultsRule.setTerm(grade.getTerm());
			}*/	
			String yearTerm = CacheAppManager.getSysConfigurationByCode("sysCurrentGrade").getParamValue();
			if(ExStringUtils.isNotBlank(defaultRule) && defaultRule.split(":").length==3){
				usualResultsRule = new UsualResultsRule(defaultRule.split(":"));
			}else {
				usualResultsRule = new UsualResultsRule();
			}
			if(null!=yearTerm){
				String[] ARRYyterm = yearTerm.split("\\.");
				usualResultsRule.setYearInfo(yearInfoService.getByFirstYear(Long.parseLong(ARRYyterm[0])));
				usualResultsRule.setTerm(ARRYyterm[1]);
			}
			model.addAttribute("defaultRule", defaultRule);
			model.addAttribute("usualResultsRule", usualResultsRule);
		}		
		return "/edu3/teaching/usualresultsrule/usualresultsrule-form";
	}		
	/**
	 * 保存学生平时分积分规则
	 * @param usualResultsRule
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/usualresultsrule/save.html")
	public void saveUsualResultsRule(UsualResultsRule usualResultsRule,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		try {	
			if(usualResultsRule.getResultPerSum()!=100){
				map.put("statusCode", 300);
				map.put("message", "请重新分配比例，各项比例总和应为100%！");
			} else {				
				String type = request.getParameter("type");		
				String courseId = request.getParameter("courseId");	
				String yearInfoId = request.getParameter("yearInfoId");				
				YearInfo yearInfo = yearInfoService.get(yearInfoId);
				usualResultsRule.setYearInfo(yearInfo);
				if(ExStringUtils.isNotEmpty(type)&&"init".equals(type)){//批量初始化
					List<UsualResultsRule> rules = new ArrayList<UsualResultsRule>();
					//查找本年度学期未生成积分规则的课程
					List<Course> courses = courseService.findByHql(" from "+Course.class.getSimpleName()+" c where c.isDeleted=0 and c.status=1 and not exists ( from "+UsualResultsRule.class.getSimpleName()+" r where r.isDeleted=0 and r.course.resourceid=c.resourceid and r.yearInfo.resourceid=? and r.term=? ) ",yearInfoId,usualResultsRule.getTerm());
					for (Course course : courses) {							
						UsualResultsRule resultsRule = new UsualResultsRule();
						usualResultsRule.setCourse(course);
						ExBeanUtils.copyProperties(resultsRule, usualResultsRule);
						rules.add(resultsRule);
					}
					usualResultsRuleService.batchSaveOrUpdate(rules);
					map.put("statusCode", 200);
					map.put("message", "本次成功生成"+rules.size()+"条基本规则。");
				} else {
					if(ExStringUtils.isBlank(courseId)){
						throw new WebException("请选择一个课程");
					}
					Course course = courseService.get(courseId);					
					Map<String, Object> param = new HashMap<String, Object>();
					param.put("courseId", courseId);
					param.put("yearInfoId", yearInfoId);
					param.put("term", usualResultsRule.getTerm());
					String hql = " from "+UsualResultsRule.class.getSimpleName()+" where isDeleted=0 and course.resourceid=:courseId and yearInfo.resourceid=:yearInfoId and term=:term ";
					if(ExStringUtils.isNotBlank(usualResultsRule.getResourceid())){
						hql += " and resourceid<>:resourceid ";
						param.put("resourceid", usualResultsRule.getResourceid());
					}
					List<UsualResultsRule> list = usualResultsRuleService.findByHql(hql,param);
					if(ExCollectionUtils.isNotEmpty(list)){
						throw new WebException("<b>"+course.getCourseName()+"</b>已存在<b>"+yearInfo.getYearName()+JstlCustomFunction.dictionaryCode2Value("CodeTerm", usualResultsRule.getTerm())+"</b>的平时分积分规则");
					}						
					
					usualResultsRule.setCourse(course);
					usualResultsRule.setVersionNum(usualResultsRuleService.getNextVersionNum(courseId));
					if(ExStringUtils.isNotBlank(usualResultsRule.getResourceid())){ //--------------------更新
						String sql = "select count(*) from edu_teach_usualresults t where t.isdeleted=0 and t.status='1' and t.yearid=:yearInfoId and t.term=:term and t.resultsruleid=:resourceid ";
						long userdCount = baseSupportJdbcDao.getBaseJdbcTemplate().findForLong(sql, param);
						if(userdCount>0){
							throw new WebException("本平时分积分规则已经使用，不能修改");
						}
						UsualResultsRule rule = usualResultsRuleService.get(usualResultsRule.getResourceid());
						usualResultsRule.setCourse(rule.getCourse());						
						ExBeanUtils.copyProperties(rule, usualResultsRule);						
						usualResultsRuleService.update(rule);
					} else {
						usualResultsRuleService.save(usualResultsRule);
					}
					map.put("statusCode", 200);
					map.put("message", "保存成功！");
					map.put("navTabId", "RES_TEACHING_ESTAB_USUALRESULTSRULE_INPUT");
					map.put("reloadUrl", request.getContextPath() +"/edu3/teaching/usualresultsrule/input.html?resourceid="+usualResultsRule.getResourceid());
				}
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), "8",UserOperationLogs.UPDATE,  "网络课程积分规则新增或修改:type:"+type+"||courseId:"+courseId+"||yearInfoId"+yearInfoId);
			}			
		}catch (Exception e) {
			logger.error("保存学生平时分积分规则出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败！<br/>"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 删除学生平时分积分规则
	 * @param resourceid
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/usualresultsrule/remove.html")
	public void removeUsualResultsRule(String resourceid,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(resourceid)){
				if(resourceid.split("\\,").length >0){			
					usualResultsRuleService.batchCascadeDelete(resourceid.split("\\,"));
					map.put("statusCode", 200);
					map.put("message", "删除学生平时分积分规则成功！");				
					map.put("forward", request.getContextPath()+"/edu3/teaching/usualresultsrule/list.html");
					UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "8",UserOperationLogs.DELETE, "删除网络课程积分规则:resourceid:"+resourceid);
				} else {
					map.put("statusCode", 300);
					map.put("message", "删除出错！");
				}
			}
		} catch (Exception e) {
			logger.error("删除学生平时分积分规则:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	} 	
	
}
