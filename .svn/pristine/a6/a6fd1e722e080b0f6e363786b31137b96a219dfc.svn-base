package com.hnjk.edu.learning.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.GUIDUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.FileUploadAndDownloadSupportController;
import com.hnjk.core.support.context.Constants;
import com.hnjk.core.support.context.SystemContextHolder;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.basedata.model.Grade;
import com.hnjk.edu.basedata.model.YearInfo;
import com.hnjk.edu.basedata.service.IGradeService;
import com.hnjk.edu.basedata.service.IYearInfoService;
import com.hnjk.edu.learning.model.StudentLearnPlan;
import com.hnjk.edu.learning.service.ILearningJDBCService;
import com.hnjk.edu.learning.service.IStudentLearnPlanService;
import com.hnjk.edu.teaching.model.UsualResults;
import com.hnjk.edu.teaching.service.ICourseService;
import com.hnjk.edu.teaching.service.IUsualResultsService;
import com.hnjk.extend.plugin.excel.service.IExportExcelService;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.service.IDictionaryService;
import com.hnjk.platform.taglib.JstlCustomFunction;
/**
 * 论坛帖子统计
 * <code>StatController</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2011-3-17 上午10:46:19
 * @see 
 * @version 1.0
 */
@Controller
public class BbstatController extends FileUploadAndDownloadSupportController {

	private static final long serialVersionUID = -4866053297304950394L;
	
	@Autowired
	@Qualifier("learningJDBCService")
	private ILearningJDBCService learningJDBCService;
	
	@Autowired
	@Qualifier("studentLearnPlanService")
	private IStudentLearnPlanService studentLearnPlanService;
	
	@Autowired
	@Qualifier("usualResultsService")
	private IUsualResultsService usualResultsService;
	
	@Autowired
	@Qualifier("exportExcelService")
	private IExportExcelService exportExcelService;
	
	@Autowired
	@Qualifier("dictService")
	private IDictionaryService dictionaryService;
	
	@Autowired
	@Qualifier("courseService")
	private ICourseService courseService;
	
	@Autowired
	@Qualifier("yearInfoService")
	private IYearInfoService yearInfoService;
	
	@Autowired
	@Qualifier("gradeService")
	private IGradeService gradeService;
	
	/**
	 * 学生发帖情况统计列表
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/learning/bbs/student/stat.html")
	public String listStudnetBbsStat(HttpServletRequest request, Page objPage, ModelMap model) throws WebException{
		String unitId = request.getParameter("unitId");
		String gradeid = request.getParameter("gradeid");
		String studentName = request.getParameter("studentName");
		String studyNo = request.getParameter("studyNo");
		String classic = request.getParameter("classic");
		String major = request.getParameter("major");
		String courseName = request.getParameter("courseName");
		String courseId = request.getParameter("courseId");
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		//String isBest = request.getParameter("isBest");
		
		String orderBy = ExStringUtils.trimToEmpty(request.getParameter("orderBy"));
		String orderType = ExStringUtils.defaultIfEmpty(ExStringUtils.trimToEmpty(request.getParameter("orderType")), Page.ASC);
		if(ExStringUtils.isNotEmpty(orderBy) && ExStringUtils.isNotEmpty(orderType)){//如果排序条件不为空，则加入排序
			objPage.setOrderBy(orderBy);
			objPage.setOrder(orderType);
		}else{
			objPage.setOrderBy(" course.courseName,unit.unitname,grade.gradeName,major.majorname,classic.classicname,stuinfo.studentname ");
			objPage.setOrder(Page.ASC);
		}	
		
		String advanseCon = ExStringUtils.trimToEmpty(request.getParameter("con"));//高级查询页面
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件
		if(ExStringUtils.isNotEmpty(unitId)) {
			condition.put("unitId", unitId);
		}
		if(ExStringUtils.isNotEmpty(gradeid)) {
			condition.put("gradeid", gradeid);
		}
		if(ExStringUtils.isNotEmpty(studentName)) {
			condition.put("studentName", studentName);
		}
		if(ExStringUtils.isNotEmpty(studyNo)) {
			condition.put("studyNo", studyNo);
		}
		if(ExStringUtils.isNotEmpty(classic)) {
			condition.put("classic", classic);
		}
		if(ExStringUtils.isNotEmpty(major)) {
			condition.put("major", major);
		}
		if(ExStringUtils.isNotEmpty(courseName)) {
			condition.put("courseName", courseName);
		}
		if(ExStringUtils.isNotEmpty(courseId)) {
			condition.put("courseId", courseId);
		}
		if(ExStringUtils.isNotEmpty(startTime)) {
			condition.put("startTime", startTime);
		}
		if(ExStringUtils.isNotEmpty(endTime)) {
			condition.put("endTime", endTime);
		}
//		if(ExStringUtils.isNotEmpty(isBest))
//			condition.put("isBest", isBest);
		condition.put("orderBy", orderBy);
		condition.put("orderType", orderType);
		model.addAttribute("condition", condition);	
	
		if ("advance".equals(advanseCon)) {
			return "/edu3/learning/bbsstat/student-search";// 返回到高级检索
		}
		Page bbsStatList = learningJDBCService.statStudentBbsTopicAndReply(condition, objPage);
		model.addAttribute("bbsStatList", bbsStatList);
		return "/edu3/learning/bbsstat/student-bbsstat";
	}

	/**
	 * 教师发帖情况统计
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/learning/bbs/edumanager/stat.html")
	public String listBbsTopic(HttpServletRequest request, Page objPage, ModelMap model) throws WebException{
		String unitId = request.getParameter("unitId");
		String cnName = request.getParameter("cnName");
		String username = request.getParameter("username");
		String courseName = request.getParameter("courseName");
		String courseId = request.getParameter("courseId");
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		
		String orderBy = ExStringUtils.trimToEmpty(request.getParameter("orderBy"));
		String orderType = ExStringUtils.defaultIfEmpty(ExStringUtils.trimToEmpty(request.getParameter("orderType")), Page.ASC);
		if(ExStringUtils.isNotEmpty(orderBy) && ExStringUtils.isNotEmpty(orderType)){//如果排序条件不为空，则加入排序
			objPage.setOrderBy(orderBy);
			objPage.setOrder(orderType);
		}else{
			objPage.setOrderBy(" course.courseName,users.cnname,unit.unitname ");
			objPage.setOrder(Page.ASC);
		}	
		
		String advanseCon = ExStringUtils.trimToEmpty(request.getParameter("con"));//高级查询页面
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件
		if(ExStringUtils.isNotEmpty(unitId)) {
			condition.put("unitId", unitId);
		}
		if(ExStringUtils.isNotEmpty(cnName)) {
			condition.put("cnName", cnName);
		}
		if(ExStringUtils.isNotEmpty(username)) {
			condition.put("username", username);
		}
		if(ExStringUtils.isNotEmpty(courseName)) {
			condition.put("courseName", courseName);
		}
		if(ExStringUtils.isNotEmpty(courseId)) {
			condition.put("courseId", courseId);
		}
		if(ExStringUtils.isNotEmpty(startTime)) {
			condition.put("startTime", startTime);
		}
		if(ExStringUtils.isNotEmpty(endTime)) {
			condition.put("endTime", endTime);
		}
		
		model.addAttribute("condition", condition);		

		if ("advance".equals(advanseCon)) {
			return "/edu3/learning/bbsstat/edumanager-search";// 返回到高级检索
		}
		Page bbsStatList = learningJDBCService.statEdumanagerBbsTopicAndReply(condition, objPage);
		model.addAttribute("bbsStatList", bbsStatList);
		return "/edu3/learning/bbsstat/edumanager-bbsstat";
	}
	/**
	 * 学生网络辅导成绩登记
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/learning/bbs/student/score.html")
	public String listStudnetBbsScore(HttpServletRequest request, Page objPage, ModelMap model) throws WebException{
		String yearInfoId = request.getParameter("yearInfoId");
		String term = request.getParameter("term");
		String courseId = request.getParameter("courseId");
		String unitId = request.getParameter("unitId");
		String studentName = ExStringUtils.trimToEmpty(request.getParameter("studentName"));
		String studyNo = ExStringUtils.trimToEmpty(request.getParameter("studyNo"));		
		
		if(ExStringUtils.isBlank(yearInfoId) || ExStringUtils.isBlank(term)){//设置为当前年度学期
			/*Grade grade = gradeService.getDefaultGrade();
			if(grade != null){
				if(ExStringUtils.isBlank(yearInfoId)){
					yearInfoId = grade.getYearInfo().getResourceid();
				}
				if(ExStringUtils.isBlank(term)){
					term = grade.getTerm();
				}
			}*/
			String yearTerm = CacheAppManager.getSysConfigurationByCode("sysCurrentGrade").getParamValue();

			if (null!=yearTerm) {
				String[] ARRYyterm = yearTerm.split("\\.");
				if(ExStringUtils.isBlank(yearInfoId)){
					yearInfoId = yearInfoService.getByFirstYear(Long.parseLong(ARRYyterm[0])).getResourceid();
				}
				if(ExStringUtils.isBlank(term)){
					term = ARRYyterm[1];
				}
				
			}
		}
		
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件
		if(ExStringUtils.isNotEmpty(unitId)) {
			condition.put("unitId", unitId);
		}
		if(ExStringUtils.isNotEmpty(studentName)) {
			condition.put("studentName", studentName);
		}
		if(ExStringUtils.isNotEmpty(studyNo)) {
			condition.put("studyNo", studyNo);
		}
		if(ExStringUtils.isNotEmpty(courseId)) {
			condition.put("courseId", courseId);
		}
		if(ExStringUtils.isNotEmpty(yearInfoId)) {
			condition.put("yearInfoId", yearInfoId);
		}
		if(ExStringUtils.isNotEmpty(term)) {
			condition.put("term", term);
		}
		condition.put("studentStatus", "'11','16','25'");
		if(ExStringUtils.isNotBlank(courseId) && ExStringUtils.isNotBlank(yearInfoId) && ExStringUtils.isNotBlank(term)){
			Page bbsScoreList = learningJDBCService.statStudentBbsScore(condition, objPage);
			model.addAttribute("bbsScoreList", bbsScoreList);
		}		
		model.addAttribute("condition", condition);	
		return "/edu3/learning/bbsstat/student-bbsscore";
	}
	/**
	 * 保存学生网络辅导成绩
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/learning/bbs/student/score/save.html")
	public void saveStudnetBbsScore(HttpServletRequest request, HttpServletResponse response) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		try {	
			String[] planIds = request.getParameterValues("resourceid");
			if(planIds != null && planIds.length > 0){
				List<StudentLearnPlan> planList = studentLearnPlanService.findByCriteria(Restrictions.eq("isDeleted", 0),Restrictions.in("resourceid", planIds));
				List<UsualResults> usualResultsList = new ArrayList<UsualResults>();
				for (StudentLearnPlan plan : planList) {
					UsualResults usualResults = plan.getUsualResults();
					if(usualResults == null){
						usualResults = new UsualResults();
						usualResults.setYearInfo(plan.getYearInfo());
						usualResults.setTerm(plan.getTerm());
						usualResults.setStudentInfo(plan.getStudentInfo());
						if(plan.getTeachingPlanCourse() !=  null){
							usualResults.setCourse(plan.getTeachingPlanCourse().getCourse());
							usualResults.setMajorCourseId(plan.getTeachingPlanCourse().getResourceid());
						} else {
							usualResults.setCourse(plan.getPlanoutCourse());
						}		
						
						plan.setUsualResults(usualResults);
					} 
					String bbsResults = ExStringUtils.defaultIfEmpty(ExStringUtils.trimToEmpty(request.getParameter("bbsResults"+plan.getResourceid())), "0");
					usualResults.setBbsResults(bbsResults);//保存网络辅导成绩
					
					usualResultsList.add(usualResults);
				}				
				
				usualResultsService.saveOrUpdateUsualResultsAndLearnPlan(usualResultsList, planList);
				map.put("statusCode", 200);
				map.put("message", "保存成功！");
			}
		}catch (Exception e) {
			logger.error("保存学生网络辅导成绩出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败！<br/>"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 导出网络辅导成绩
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/learning/bbs/student/score/export.html")
	public void exportStudnetBbsScore(HttpServletRequest request, HttpServletResponse response) throws WebException {
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件
		String yearInfoId = request.getParameter("yearInfoId");
		String term = request.getParameter("term");
		String courseId = request.getParameter("courseId");
		
		if(ExStringUtils.isNotEmpty(courseId)) {
			condition.put("courseId", courseId);
		}
		if(ExStringUtils.isNotEmpty(yearInfoId)) {
			condition.put("yearInfoId", yearInfoId);
		}
		if(ExStringUtils.isNotEmpty(term)) {
			condition.put("term", term);
		}
		if(ExStringUtils.isNotBlank(courseId) && ExStringUtils.isNotBlank(yearInfoId) && ExStringUtils.isNotBlank(term)){
			List<Map<String,Object>> list = learningJDBCService.statStudentBbsScore(condition);		
			//文件输出服务器路径
			setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
			try{
				YearInfo yearInfo = yearInfoService.get(yearInfoId);
				Course course = courseService.get(courseId);
				String title = yearInfo.getYearName()+JstlCustomFunction.dictionaryCode2Value("CodeTerm", term)+course.getCourseName();
				//导出
				File excelFile = null;
				GUIDUtils.init();
				File disFile = new File(getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(false) + ".xls");
				
				List<String> dictCodeList = new ArrayList<String>();
				dictCodeList.add("CodeTerm");
				
				Map<String,Object> templateMap = new HashMap<String, Object>();
				templateMap.put("yearInfoAndCourse", title);
				//模板文件路径
				String templateFilepathString = "bbsResultsExport.xls";
				//初始化配置参数1
				exportExcelService.initParmasByfile(disFile, "bbsResultsExport", list,dictionaryService.getDictionByMap(dictCodeList, true,IDictionaryService.PREKEY_TYPE_BYCODE));
				exportExcelService.getModelToExcel().setTemplateParam(templateFilepathString, 3, templateMap);
				excelFile = exportExcelService.getExcelFile();//获取导出的文件
		      
				logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());
				downloadFile(response, title+"网络辅导成绩.xls", excelFile.getAbsolutePath(),true);
			}catch(Exception e){
				logger.error("导出excel文件出错："+e.fillInStackTrace());
			}
		}			
	}
}
