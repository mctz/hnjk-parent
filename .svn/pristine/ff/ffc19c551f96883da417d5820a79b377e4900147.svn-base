package com.hnjk.edu.learning.controller;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.utils.ExCollectionUtils;
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.FileUtils;
import com.hnjk.core.foundation.utils.GUIDUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.FileUploadAndDownloadSupportController;
import com.hnjk.core.support.context.Constants;
import com.hnjk.core.support.context.SpringContextHolder;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.basedata.model.Grade;
import com.hnjk.edu.basedata.model.YearInfo;
import com.hnjk.edu.basedata.service.IGradeService;
import com.hnjk.edu.basedata.service.IYearInfoService;
import com.hnjk.edu.jmx.client.IDocumentConvertService;
import com.hnjk.edu.learning.model.CourseExam;
import com.hnjk.edu.learning.model.Exercise;
import com.hnjk.edu.learning.model.ExerciseBatch;
import com.hnjk.edu.learning.model.StudentExercise;
import com.hnjk.edu.learning.service.ICourseExamService;
import com.hnjk.edu.learning.service.IExerciseBatchService;
import com.hnjk.edu.learning.service.IExerciseService;
import com.hnjk.edu.learning.service.ILearningTimeSettingService;
import com.hnjk.edu.learning.service.IStudentExerciseService;
import com.hnjk.edu.learning.service.IStudentLearnPlanService;
import com.hnjk.edu.learning.service.impl.CourseExamServiceImpl;
import com.hnjk.edu.learning.util.CourseExamUtils;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.teaching.model.LearningTimeSetting;
import com.hnjk.edu.teaching.model.TeachingPlanCourse;
import com.hnjk.edu.teaching.model.TeachingPlanCourseStatus;
import com.hnjk.edu.teaching.service.ITeachingPlanCourseService;
import com.hnjk.edu.teaching.service.ITeachingPlanCourseStatusService;
import com.hnjk.edu.teaching.service.ITeachingPlanCourseTimetableService;
import com.hnjk.edu.teaching.service.IUsualResultsService;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.Attachs;
import com.hnjk.platform.system.service.IAttachsService;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;
import com.hnjk.security.service.IUserService;

/**
 * 课后作业及答案管理
 * <code>ExerciseController</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-8-6 下午05:11:19
 * @see 
 * @version 1.0
 */
@Controller
public class ExerciseController extends FileUploadAndDownloadSupportController {

	private static final long serialVersionUID = 1949945875845351659L;
	
	@Autowired
	@Qualifier("exerciseBatchService")
	private IExerciseBatchService exerciseBatchService;
	
	@Autowired
	@Qualifier("exerciseService")
	private IExerciseService exerciseService;
	
	@Autowired
	@Qualifier("studentExerciseService")
	private IStudentExerciseService studentExerciseService;	
	
	@Autowired
	@Qualifier("attachsService")
	private IAttachsService attachsService;
	
	@Autowired
	@Qualifier("courseExamService")
	private ICourseExamService courseExamService;
	
	@Autowired
	@Qualifier("studentLearnPlanService")
	private IStudentLearnPlanService studentLearnPlanService;
	
	@Autowired
	@Qualifier("gradeService")
	private IGradeService gradeService;
	
	@Autowired
	@Qualifier("usualResultsService")
	private IUsualResultsService usualResultsService;
	
	@Autowired
	@Qualifier("teachingPlanCourseTimetableService")
	private ITeachingPlanCourseTimetableService tpctService;
	
	@Autowired
	@Qualifier("yearInfoService")
	private IYearInfoService yearInfoService;
	
	@Autowired
	@Qualifier("teachingPlanCourseStatusService")
	private ITeachingPlanCourseStatusService teachingPlanCourseStatusService;
	
	@Autowired
	@Qualifier("teachingPlanCourseService")
	private ITeachingPlanCourseService teachingPlanCourseService;
	
	@Autowired
	@Qualifier("learningTimeSettingService")
	private ILearningTimeSettingService learningTimeSettingService;
	
	/**
	 * 课后作业题目列表
	 * @param colsId
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/metares/exercise/unactive/exercise/list.html")
	public String listExercise(String colsId,HttpServletRequest request,ModelMap model) throws WebException {
		//查询条件
		Map<String,Object> condition = new HashMap<String,Object>();
		if(ExStringUtils.isNotBlank(colsId)){
			condition.put("colsId", colsId);	
			model.addAttribute("exerciseBatch", exerciseBatchService.get(colsId));
		}		
		
		List<Exercise> list = exerciseService.findExerciseByCondition(condition);

		model.addAttribute("exerciseList", list);		
		model.addAttribute("condition", condition);		
		return "/edu3/learning/exercise/exercise-list";
	}

	/**
	 * 课后作业新增编辑
	 * @param colsId
	 * @param request
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/metares/exercise/unactive/exercise/input.html")
	public String editExercise(String colsId,HttpServletRequest request,ModelMap model) throws WebException{
		String keywords = ExStringUtils.trimToEmpty(request.getParameter("keywords"));
		String examType = request.getParameter("examType");	
		String difficult = request.getParameter("difficult");
		//查询条件
		Map<String,Object> condition = new HashMap<String,Object>();
		if(ExStringUtils.isNotEmpty(keywords)) {
			condition.put("keywords", keywords);
		}
		if(ExStringUtils.isNotEmpty(examType)) {
			condition.put("examType", examType);
		}
		if(ExStringUtils.isNotEmpty(difficult)) {
			condition.put("difficult", difficult);
		}
		if(ExStringUtils.isNotEmpty(colsId)) {
			ExerciseBatch exerciseBatch = exerciseBatchService.get(colsId);			
			model.addAttribute("exerciseBatch", exerciseBatch);
			condition.put("colsId", colsId);
			condition.put("courseId", exerciseBatch.getCourse().getResourceid());
						
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("colsId", colsId);
			List<Exercise> listExams = exerciseService.findExerciseByCondition(map);
			String ids = "";
			for (Exercise exercise : listExams) {
				ids += exercise.getCourseExam().getResourceid()+",";				
			}
			model.addAttribute("existCourseExamIds", ids);//已存在习题id
		}
		
		List<CourseExam> list = courseExamService.findCourseExamByCondition(condition);
		model.addAttribute("courseExamList", list);
		model.addAttribute("condition", condition);
		return "/edu3/learning/exercise/exercise-form";
	}
	
	/**
	 * 课后作业保存
	 * @param exercise
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/metares/exercise/unactive/exercise/save.html")
	public void saveExercise(String colsId,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();		
		try {	
			String from = request.getParameter("from");
			ExerciseBatch exerciseBatch = exerciseBatchService.get(colsId);			
			if(ExStringUtils.isNotEmpty(from)){ //--------------------更新
				String[] exerciseId = request.getParameterValues("exerciseId");
				String[] showOrder = request.getParameterValues("showOrder");
				String[] score = request.getParameterValues("score");
				if(null!=exerciseId && exerciseId.length>0){
					List<Exercise> list = new ArrayList<Exercise>();
					for (int i = 0; i < exerciseId.length; i++) {
						Exercise exercise = exerciseService.get(exerciseId[i]);
						exercise.setShowOrder(Integer.parseInt(ExStringUtils.defaultIfEmpty(showOrder[i], "0")));
						exercise.setScore(Double.parseDouble(ExStringUtils.defaultIfEmpty(score[i], "0")));
						list.add(exercise);
					}
					exerciseService.batchSaveOrUpdate(list);
					map.put("statusCode", 200);
					map.put("message", "保存成功！");
					map.put("callbackType", "forward");
					map.put("forwardUrl", request.getContextPath() +"/edu3/metares/exercise/unactive/exercise/list.html?colsId="+colsId);
				}				
			}else{ //------------------------------------------------添加
				String resourceid = request.getParameter("resourceid");
				if(ExStringUtils.isNotEmpty(resourceid)){
					List<Exercise> list = new ArrayList<Exercise>();
					int nextShowOrder = exerciseService.getNextShowOrder(colsId);
					for (String id : resourceid.split(",")) {
						if(!exerciseService.isExsitExercise(colsId, id)){ //不存在，加入
							CourseExam courseExam = courseExamService.get(id);
							Exercise exercise = new Exercise();
							exercise.setCourseExam(courseExam);
							exercise.setExerciseBatch(exerciseBatch);
							exercise.setShowOrder(nextShowOrder++);
							exercise.setScore(Double.parseDouble(ExStringUtils.defaultIfEmpty(request.getParameter("score"+courseExam.getExamType()), "0")));
							list.add(exercise);
						}
					}
					exerciseService.batchSaveOrUpdate(list);
					map.put("statusCode", 200);
					map.put("message", "保存成功！");
					map.put("reloadUrl", request.getContextPath() +"/edu3/metares/exercise/unactive/exercise/list.html?colsId="+colsId);
					map.put("callbackType", "forward");
					map.put("forwardUrl", request.getContextPath() +"/edu3/metares/exercise/unactive/exercise/input.html?colsId="+colsId);
				}
			}			
		}catch (Exception e) {
			logger.error("保存课后练习出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}		
	
	/**
	 * 删除课后作业
	 * @param resourceid
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/metares/exercise/unactive/exercise/remove.html")
	public void removeExercise(String resourceid,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		try {			
			if(ExStringUtils.isNotBlank(resourceid)){			
				exerciseService.deleteExercise(resourceid.split("\\,"));
				map.put("statusCode", 200);
				map.put("message", "删除成功！");	
				map.put("callbackType", "forward");
				map.put("forward", request.getContextPath()+"/edu3/metares/exercise/unactive/exercise/list.html");
			}
		} catch (Exception e) {
			logger.error("删除课后作业出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 下载学生作业
	 * @param exerciseBatchId
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/exercisecheck/studentexercise/download.html")
	public void downloadStudentExercise(String resourceid,HttpServletRequest request,HttpServletResponse response) throws WebException{
		String tempZipPath = null;//临时存储下载的zip文件
		String tempPicPatch = null;//临时存储复制的图片目录
		String tempFilePath = null;//临时存储复制的图片目录
		try{
			if(ExStringUtils.isNotEmpty(resourceid)){
				StudentExercise stuExercise = studentExerciseService.get(resourceid);
				String rootPath = Constants.EDU3_DATAS_LOCALROOTPATH+"temp";
					
				Map<String, Object> condition = new HashMap<String, Object>();
				condition.put("studentInfoId",stuExercise.getStudentInfo().getResourceid());
				condition.put("exerciseBatchId",stuExercise.getExerciseBatch().getResourceid());
				List<StudentExercise> list = studentExerciseService.findByHql("from "+StudentExercise.class.getSimpleName()+" where isDeleted=0 and studentInfo.resourceid=:studentInfoId and exerciseBatch.resourceid=:exerciseBatchId and exercise is not null order by exercise.showOrder ", condition);
				StringBuffer sb = new StringBuffer();	
				sb.append("<html>");
				sb.append("<head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /></head>");//指定html编码,否则会导致部分乱码
				sb.append("<h2>"+stuExercise.getStudentInfo().getStudentName()+"-"+stuExercise.getExerciseBatch().getColName()+"</h2><br/>");
				String getAnswer = null;
				for (StudentExercise studentExercise : list) {
					sb.append("<div>"+studentExercise.getExercise().getCourseExam().getQuestion()+"</div>");
					if(studentExercise.getAnswer()!=null){
						sb.append("<div>"+studentExercise.getAnswer()+"</div>");
					}
					getAnswer=studentExercise.getAnswer();
					sb.append("<br/>"); 
				}
				sb.append("</html>");
				setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
				GUIDUtils.init();	
				tempFilePath = getDistfilepath()+File.separator+GUIDUtils.buildMd5GUID(false)+".doc";
				String htmlContent = sb.toString();
				String rooturl = CacheAppManager.getSysConfigurationByCode("web.uploadfile.rooturl").getParamValue();
				String fullRootUrl = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+rooturl;
				htmlContent = htmlContent.replace("src=\""+rooturl, "src=\""+fullRootUrl);
								
				//DocumentToRtfConvert.htmlToRtfBuilder(tempFilePath, sb.toString(), null);//不支持图片导出
				CourseExamUtils.htmlToDocumentByPoi(htmlContent, tempFilePath);
				//离线答题，从上传的doc获取答案
				if(getAnswer==null){
					for (StudentExercise studentExercise : list) {
						List<Attachs> attachs = attachsService.findAttachsByFormId(studentExercise.getResourceid());
						for(Attachs attach:attachs){
//							if(attach!=null){ 
//								String filePath = attach.getSerPath()+File.separator+attach.getSerName();
//								IDocumentConvertService documentConvertService = SpringContextHolder.getBean("documentConvertServer");
//								String zipPath = documentConvertService.convertDocToHtml(Constants.EDU3_DATAS_LOCALROOTPATH+"temp",filePath, attach.getSerName());
//								tempZipPath = zipPath;
//								String username = SpringSecurityHelper.getCurrentUserName();
//								String picPatch = "users"+File.separator+username+File.separator+"images"+File.separator+"courseexam"+File.separator+ExDateUtils.formatDateStr(new Date(), "yyyy_MM_dd_HHmmss");
//								tempPicPatch = Constants.EDU3_DATAS_LOCALROOTPATH+picPatch;//图片的目标存储目录
//								
//								String text = CourseExamServiceImpl.getTextFromZipOfHtml(zipPath, picPatch);
//								text=text.replace("\n", "<br/>");
//								sb.append("<div>"+text+"</div>");
//								sb.append("<br/>"); 
//							}
							String filePath = attach.getSerPath()+File.separator+attach.getSerName();
							tempFilePath = filePath;
							logger.info("获取作业文件:{}",tempFilePath);
							downloadFile(response, stuExercise.getStudentInfo().getStudentName()+"-"+stuExercise.getExerciseBatch().getColName()+".doc", tempFilePath, false);
						}
					}
				
				}else{
					logger.info("获取作业文件:{}",tempFilePath);
					downloadFile(response, stuExercise.getStudentInfo().getStudentName()+"-"+stuExercise.getExerciseBatch().getColName()+".doc", tempFilePath, true);
				}
				
				
				
				
	
			} 
		}catch(Exception e){
			logger.error("下载作业出错："+e.fillInStackTrace());
			renderText(response, "下载作业出错！");
//			try {
//				if(ExStringUtils.isNotBlank(tempPicPatch)){
//					FileUtils.delFolder(tempPicPatch);//删除图片目录,导入失败时,当次解压复制的图片以废弃
//				}
//			} catch (Exception ei) {				
//			}
		}
//		finally {
//			//清除临时文件夹temp中的zip文件和临时解压文件
//			try {
//				if(ExStringUtils.isNotBlank(tempZipPath)){
//					FileUtils.delFile(tempZipPath);//删除下载的zip文件
//					FileUtils.delFolder(ExStringUtils.substringBeforeLast(tempZipPath, ".zip"));//删除解压目录
//				}
//			} catch (Exception e2) {
//			}
//		}
	}
	/**
	 * 上传经典批改
	 * @param resourceid
	 * @param request
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/exercisecheck/studentexercise/upload.html")
	public String uploadStudentExercise(String resourceid,HttpServletRequest request,ModelMap model) throws WebException{
		if(ExStringUtils.isNotBlank(resourceid)){
			StudentExercise stuExercise = studentExerciseService.get(resourceid);
			List<Attachs> attachs = attachsService.findAttachsByFormId(resourceid);
			stuExercise.setAttachs(attachs);
			model.addAttribute("stuExercise", stuExercise);
			
			User user = SpringSecurityHelper.getCurrentUser();
			model.addAttribute("storeDir", user.getUsername());
		}
		return "/edu3/learning/exercise/exercise-upload";
	}
	
	/**
	 * 待批改的作业列表
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/exercisecheck/list.html")
	public String listExerciseBatch(HttpServletRequest request,Page objPage, ModelMap model) throws WebException {
		objPage.setOrderBy("course.courseName,colType,startDate");//根据课程名称，作业类型，批次开始时间排序
		objPage.setOrder(Page.ASC);//设置默认排序方式			
		
		String courseName = request.getParameter("courseName");
		String colName = request.getParameter("colName");
		String colType = request.getParameter("colType");	
		String status = request.getParameter("status");
				
		//查询条件
		Map<String,Object> condition = new HashMap<String,Object>();
		if(ExStringUtils.isNotEmpty(courseName)) {
			condition.put("courseName", courseName);
		}
		if(ExStringUtils.isNotEmpty(colName)) {
			condition.put("colName", colName);
		}
		if(ExStringUtils.isNotEmpty(colType)) {
			condition.put("colType", colType);
		}
		if(ExStringUtils.isNotEmpty(status)) {
			condition.put("status", status);
		}
		condition.put("notstatus", 0);
		
		User user = SpringSecurityHelper.getCurrentUser();
		String roleCode = CacheAppManager.getSysConfigurationByCode("sysuser.role.teacher").getParamValue();//获取角色编码
		if(SpringSecurityHelper.isUserInRole(roleCode)) {
			condition.put("teacherId", user.getResourceid());
		}
		
		Page page = exerciseBatchService.findExerciseBatchByCondition(condition, objPage);
		
		Map<String,Object> map = new HashMap<String,Object>(0);
		for (Object obj : page.getResult()) {
			ExerciseBatch exerciseBatch = (ExerciseBatch)obj;
			Map m = studentExerciseService.avgStudentFinished(exerciseBatch.getResourceid());
			map.put(exerciseBatch.getResourceid(), m);
//			map.put(exerciseBatch.getResourceid(), studentExerciseService.countStudentFinished(exerciseBatch.getResourceid()));
		}
		model.addAttribute("studentFinishedCount", map);
		
		model.addAttribute("exerciseBatchListPage", page);
		model.addAttribute("condition", condition);
		return "/edu3/teaching/exercisecheck/exercisebatch-list";
	}
	
	/**
	 * 作业批次下所有学生的作业
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/exercisecheck/studentexercise/list.html")
	public String listStudentExercise(HttpServletRequest request,Page objPage, ModelMap model) throws WebException {
		objPage.setOrderBy("studentInfo.studyNo");//根据学生号排序
		objPage.setOrder(Page.ASC);//设置默认排序方式
		String exerciseBatchId = request.getParameter("exerciseBatchId");
		String studentName = ExStringUtils.trimToEmpty(request.getParameter("studentName"));
		String studyNo = ExStringUtils.trimToEmpty(request.getParameter("studyNo"));	
		String status = ExStringUtils.trimToEmpty(request.getParameter("status"));	
		String unitId = ExStringUtils.trimToEmpty(request.getParameter("unitId"));		
		String courseId = ExStringUtils.trimToEmpty(request.getParameter("courseId"));		
		User user = SpringSecurityHelper.getCurrentUser();
		//查询条件
		Map<String,Object> condition = new HashMap<String,Object>();
		if(ExStringUtils.isNotEmpty(studentName)){
			condition.put("studentName", studentName);
		}
		if(ExStringUtils.isNotEmpty(studyNo)){
			condition.put("studyNo", studyNo);
		}	
		if(ExStringUtils.isNotEmpty(courseId)){
			condition.put("courseId", courseId);
		}
		if(ExStringUtils.isNotBlank(exerciseBatchId)){			
			condition.put("exerciseBatchId", exerciseBatchId);
			ExerciseBatch exerciseBatch = exerciseBatchService.get(exerciseBatchId);
			// 对这门课是否有操作权限
			model.addAttribute("hasCheckAuthority", tpctService.hasAuthority(user.getResourceid(), exerciseBatch.getCourse().getResourceid(),null));
			model.addAttribute("exerciseBatch",exerciseBatch);
		}
		if(ExStringUtils.isNotEmpty(unitId)){
			condition.put("unitId", unitId);
		}
		condition.put("exerciseResult", "exerciseResult");
		condition.put("studentStatus", "'11','16','25'");
		if(!SpringSecurityHelper.isUserInRole(CacheAppManager.getSysConfigurationByCode("sysuser.role.teacher.duty").getParamValue())
				&&CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())){//如果为校外学习中心人员
			condition.put("branchSchool", user.getOrgUnit().getResourceid());
			condition.put("unitId", user.getOrgUnit().getResourceid());
		} else {
			if(SpringSecurityHelper.isTeachingCentreTeacher(user)){//显示辅导的学生
				//condition.put("shoolTeacherId", user.getResourceid());
				//condition.put("classesTeacherId", user.getResourceid());
				condition.put("teacherId", user.getResourceid());
			}
		}
		
		if(!"0".equals(status)){
			if(ExStringUtils.isNotEmpty(status)){
				condition.put("status", Integer.parseInt(status));
			}
			condition.put("gtstatus", 0);
			Page page = studentExerciseService.findStudentExerciseByCondition(condition, objPage);
			model.addAttribute("exerciseListPage", page);
		} else {//未提交作业学生
			ExerciseBatch exerciseBatch = exerciseBatchService.get(exerciseBatchId);
			condition.put("courseId", exerciseBatch.getCourse().getResourceid());
			condition.put("yearInfoId", exerciseBatch.getYearInfo().getResourceid());
			condition.put("terms", exerciseBatch.getTerm());
			//只保留正常注册的在学或延期或缓毕业的学生
			condition.put("queryHQL", " and plan.studentInfo.studentStatus in ('11','21','25') and plan.studentInfo.accountStatus=1 and (  plan.yearInfo.resourceid=:yearInfoId and plan.term=:terms or plan.teachingPlanCourse.course.resourceid=:courseId and plan.status<3 ) and not exists ( from "+StudentExercise.class.getSimpleName()+" s where s.isDeleted=0 and s.exerciseBatch.resourceid =:exerciseBatchId and s.exercise is null and s.status>0 and s.studentInfo.resourceid=plan.studentInfo.resourceid )");		
			condition.put("exerciseBatchId", exerciseBatchId);
			//只查询已排课 2017-7-18，界面数据同意
			condition.put("ispaike", exerciseBatch.getYearInfo().getFirstYear()+"_0"+exerciseBatch.getTerm());
			Page page = studentLearnPlanService.findStudentLearnPlanByCondition(condition,objPage);
			model.addAttribute("exerciseListPage", page);	
			if(ExStringUtils.isNotEmpty(status)){
				condition.put("status", Integer.parseInt(status));
			}
		}
		model.addAttribute("condition", condition);
		return "/edu3/teaching/exercisecheck/studentexercise-list";
	}
	
	/**
	 * 批改学生的作业，评分
	 * @param studentExerciseId
	 * @param model
	 * @return
	 * @throws WebException
	 */		
	@RequestMapping("/edu3/teaching/exercisecheck/studentexercise/check.html")
	public String checkStudentExercise(String studentExerciseId,ModelMap model) throws WebException {
		if(ExStringUtils.isNotBlank(studentExerciseId)){
			StudentExercise studentExercise = studentExerciseService.get(studentExerciseId);
			List<Attachs> resultAttachs = attachsService.findAttachsByFormId(studentExerciseId);
			studentExercise.setAttachs(resultAttachs);
			model.addAttribute("studentExercise", studentExercise);	
			
			ExerciseBatch exerciseBatch = studentExercise.getExerciseBatch();
			if("2".equals(exerciseBatch.getColType())){
				for (Exercise exercise : exerciseBatch.getExercises()) {
					List<Attachs> attachs = attachsService.findAttachsByFormId(exercise.getResourceid());
					exercise.setAttachs(attachs);
				}
			}
			List<StudentExercise> list = studentExerciseService.findByHql(" from "+StudentExercise.class.getName()+" where isDeleted=0 and exerciseBatch.resourceid=? and studentInfo.resourceid=? and exercise is not null ", exerciseBatch.getResourceid(),studentExercise.getStudentInfo().getResourceid());
			Map<String, StudentExercise> answers = new HashMap<String, StudentExercise>();
			for (StudentExercise exercise : list) {
				if("2".equals(exerciseBatch.getColType())){
					List<Attachs> attachs = attachsService.findAttachsByFormId(exercise.getResourceid());
					exercise.setAttachs(attachs);
				}
				answers.put(exercise.getExercise().getResourceid(), exercise);
			}
			model.addAttribute("answers", answers);		
			model.addAttribute("exerciseBatch", exerciseBatch);
		}	
		return "/edu3/teaching/exercisecheck/studentexercise-view";
	}
	
	/**
	 * 保存批改结果
	 * @param request
	 * @param response
	 * @throws WebException
	 */	
	@RequestMapping("/edu3/teaching/exercisecheck/studentexercise/save.html")
	public void saveStudentExercise(HttpServletRequest request,HttpServletResponse response) throws WebException {
		Map<String,Object> map = new HashMap<String, Object>();		
		try {		
			String exerciseBatchId = request.getParameter("exerciseBatchId");
			ExerciseBatch exerciseBatch = exerciseBatchService.load(exerciseBatchId);
			String isTypical = request.getParameter("isTypical");
			String isExcell = request.getParameter("isExcell");
			String teacherAdvise = request.getParameter("teacherAdvise");
			String from = request.getParameter("from");
			String result = ExStringUtils.defaultIfEmpty(ExStringUtils.trimToEmpty(request.getParameter("result")), "0");
			String studentExerciseResultId = request.getParameter("studentExerciseResultId");		
			if("fact".equalsIgnoreCase(from)){
				StudentExercise studentExerciseResult = studentExerciseService.get(studentExerciseResultId);
				studentExerciseResult.setIsExcell(Constants.BOOLEAN_NO);
				studentExerciseResult.setIsTypical(Constants.BOOLEAN_YES);
				studentExerciseResult.setTeacherAdvise(teacherAdvise);
				studentExerciseResult.setResult(Double.valueOf(result));
				studentExerciseResult.setStatus(2);
				studentExerciseService.update(studentExerciseResult);
			} else {
				if("1".equals(exerciseBatch.getScoringType())){//平均分摊
					double avgResult = Double.parseDouble(ExStringUtils.defaultIfEmpty(request.getParameter("avgResult"), "0"));
					List<StudentExercise> studentExercises = studentExerciseService.findByHql(" from "+StudentExercise.class.getSimpleName()+" where isDeleted=0 and exerciseBatch.resourceid=? and status in ('1','2') and exercise is null ", exerciseBatchId);
					for (StudentExercise exercise : studentExercises) {
						exercise.setResult(avgResult);
						exercise.setStatus(2);
					}
					studentExerciseService.batchSaveOrUpdate(studentExercises);
				}	
			}					
			map.put("statusCode", 200);
			map.put("message", "保存成功！");			
		}catch (Exception e) {
			logger.error("保存作业批改结果出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败！");
		}
		renderJson(response, JsonUtils.mapToJson(map));	
	}
	/**
	 * 批量保存成绩分数
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/exercisecheck/studentexercise/batchsave.html")
	public void batchsaveStudentExercise(HttpServletRequest request,HttpServletResponse response) throws WebException {
		Map<String,Object> map = new HashMap<String, Object>();		
		try {		
			String[] res = request.getParameterValues("resourceid");			
			if(res!=null && res.length>0){
				int currentCorrectCount = 0;
				int totalCorrectCount = 0;
				User checkUser        = SpringSecurityHelper.getCurrentUser();
				Date curDate          = ExDateUtils.getCurrentDateTime(); 
				List<StudentExercise> list = studentExerciseService.findByCriteria(Restrictions.in("resourceid", res));
				for (int i = 0; i < list.size(); i++) {
					StudentExercise studentExercise = list.get(i);
					String result = ExStringUtils.trimToEmpty(request.getParameter("result"+studentExercise.getResourceid()));
					String isTypical = request.getParameter("isTypical"+studentExercise.getResourceid());
					String isExcell = request.getParameter("isExcell"+studentExercise.getResourceid());
					String teacherAdvise = ExStringUtils.trimToEmpty(request.getParameter("teacherAdvise"+studentExercise.getResourceid()));
										
					if(i==0){//先查出已批改人数
						Map<String, Object> param = new HashMap<String, Object>();
						param.put("exercisebatchids", Arrays.asList(new String[]{studentExercise.getExerciseBatch().getResourceid()}));
						List<Map<String, Object>> resultList = exerciseBatchService.findExerciseStatusCount(param);
						if(ExCollectionUtils.isNotEmpty(resultList)){
							BigDecimal correctNum = (BigDecimal) resultList.get(0).get("CORRECTNUM");
							totalCorrectCount = correctNum.intValue();							
						}
					}					
					if(studentExercise.getStatus()!=2){
						currentCorrectCount++;
					}
					studentExercise.setResult(Double.parseDouble(result));
					studentExercise.setIsTypical(isTypical);
					studentExercise.setIsExcell(isExcell);
					studentExercise.setTeacherAdvise(teacherAdvise);
					studentExercise.setStatus(2);
					studentExercise.setCheckMAN(checkUser.getCnName());
					studentExercise.setCheckMANID(checkUser.getResourceid());
					studentExercise.setCheckTime(curDate);
					
					if(i==list.size()-1){//判断是否批改完毕
						if(totalCorrectCount+currentCorrectCount == studentExercise.getExerciseBatch().getOrderCourseNum()){
							studentExercise.getExerciseBatch().setStatus(3);
						} else {
							studentExercise.getExerciseBatch().setStatus(2);
						}
					}					
				}
				studentExerciseService.batchSaveOrUpdate(list);
				map.put("statusCode", 200);
				map.put("message", "保存成功！");
				if(null != list &&  list.size() > 0){ //保存成绩
					/*Grade grade = gradeService.getDefaultGrade();
					StudentInfo stu = null;
					Course c = null;
					for (StudentExercise st : list) {
						stu = st.getStudentInfo();
						if(null==c) {c = st.getExerciseBatch().getCourse();}
						usualResultsService.saveSpecificUsualResults(stu, grade.getYearInfo().getResourceid(), grade.getTerm(), c.getResourceid());
					}*/
					/*String yearTerm = CacheAppManager.getSysConfigurationByCode("sysCurrentGrade").getParamValue();
					String yearInfoId = "";
					String term = "";
					if (null!=yearTerm) {
						String[] ARRYyterm = yearTerm.split("\\.");
						yearInfoId = yearInfoService.getByFirstYear(Long.parseLong(ARRYyterm[0])).getResourceid();
						term = ARRYyterm[1];
					}*/
					
					StudentInfo stu = null;
					Course c = null;
					for (StudentExercise st : list) {
						stu = st.getStudentInfo();
						if(null==c) {c = st.getExerciseBatch().getCourse();}
						TeachingPlanCourse teachingPlanCourse = studentLearnPlanService.getStudentLearnPlanByCourse(c.getResourceid(), stu.getResourceid(),"studentId").getTeachingPlanCourse();
						TeachingPlanCourseStatus teachingPlanCourseStatus = teachingPlanCourseStatusService
								.findOneByCondition(stu.getGrade()
										.getResourceid(), stu.getTeachingPlan().getResourceid(),
										teachingPlanCourse.getResourceid(), stu.getBranchSchool().getResourceid());
						if(teachingPlanCourseStatus==null){
							continue;
						}
						String yearTermStr = teachingPlanCourseStatus.getTerm();
						String year = "";
						String term = "";
						String yearInfoId = "";
						if (null!=yearTermStr) {
							year = yearTermStr.substring(0, 4);
							term = yearTermStr.substring(6, 7);
						}
						YearInfo yearInfo = yearInfoService.getByFirstYear(Long.parseLong(year));
						yearInfoId = yearInfo.getResourceid();
						
						/*//是否在网上学习时间内
						LearningTimeSetting learningTimeSetting = learningTimeSettingService.findLearningTimeSetting(yearInfoId, term);
						Date now = new Date();
						if(learningTimeSetting == null || now.before(learningTimeSetting.getStartTime()) || now.after(learningTimeSetting.getEndTime())){
							logger.error("更新学生成绩失败:{当前时间不在网上学习时间内,请联系管理员设置}");
						}else{
							
						}*/
						usualResultsService.saveSpecificUsualResults(stu, yearInfoId, term, c.getResourceid());
					}
				}	
			}				
		} catch (ServiceException e) {
			logger.error("保存作业批改结果出错：",e);
			map.put("statusCode", 300);
			map.put("message", e.getMessage());
		} catch (Exception e) {
			logger.error("保存作业批改结果出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败！");
		}
		renderJson(response, JsonUtils.mapToJson(map));	
	}
	
	/**
	 * 作业提交情况
	 * @param exerciseBatchId
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/framework/exercise/student/view.html")
	public String viewStudentExerciseFinished(String exerciseBatchId,HttpServletRequest request,Page objPage,ModelMap model) throws WebException {
		Map<String ,Object> condition = new HashMap<String, Object>();		
		String currentIndex = ExStringUtils.defaultIfEmpty(request.getParameter("currentIndex"), "0");
		condition.put("currentIndex", currentIndex);
		if(ExStringUtils.isNotBlank(exerciseBatchId)){
			ExerciseBatch exerciseBatch = exerciseBatchService.get(exerciseBatchId);
			
			if("0".equals(currentIndex)){
				objPage.setOrderBy("se.studentInfo.studyNo");
				objPage.setOrder(Page.ASC);
				
				condition.put("exerciseResult", "null");
				condition.put("exerciseBatchId", exerciseBatchId);
				Page page = studentExerciseService.findStudentExerciseByCondition(condition,objPage);		
				model.addAttribute("studentExerciseFinished", page);
			} else {
				objPage.setOrderBy("studentInfo.studyNo");
				objPage.setOrder(Page.ASC);
				
				//查询条件
				condition.put("courseId", exerciseBatch.getCourse().getResourceid());
				condition.put("yearInfo", exerciseBatch.getYearInfo().getResourceid());
				condition.put("term", exerciseBatch.getTerm());
				condition.put("queryHQL", " and not exists ( from "+StudentExercise.class.getSimpleName()+" s where s.isDeleted=0 and s.exerciseBatch.resourceid =:exerciseBatchId and s.exercise is null and s.studentInfo.resourceid=plan.studentInfo.resourceid )");		
				condition.put("exerciseBatchId", exerciseBatchId);
				Page page = studentLearnPlanService.findStudentLearnPlanByCondition(condition,objPage);
				model.addAttribute("studentExerciseNotFinished", page);		
			}				
		}
		model.addAttribute("condition", condition);	
		return "/edu3/teaching/exercisecheck/studentexercise-state-view";
	}
	
	/**
	 * 撤销作业提交
	 * @param resourceid
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/studentexercise/cancel.html")
	public void cancelStudentExercise(String exerciseBatchId,String studentId,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		try {			
			if(ExStringUtils.isNotBlank(exerciseBatchId) && ExStringUtils.isNotBlank(studentId)){			
				studentExerciseService.cancelStudentExercise(exerciseBatchId,studentId.split("\\,"));
				map.put("statusCode", 200);
				map.put("message", "撤销成功！");	
			}
		} catch (ServiceException e) {
			logger.error("撤销作业提交出错:",e);
			map.put("statusCode", 300);
			map.put("message", e.getMessage());
		} catch (Exception e) {
			logger.error("撤销作业提交出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "撤销作业提交失败！"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
}
