package com.hnjk.edu.learning.controller;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
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
import com.hnjk.core.foundation.utils.BigDecimalUtil;
import com.hnjk.core.foundation.utils.ExCollectionUtils;
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.FileUtils;
import com.hnjk.core.foundation.utils.GUIDUtils;
import com.hnjk.core.foundation.utils.HttpHeaderUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.controller.FileUploadAndDownloadSupportController;
import com.hnjk.core.support.context.Constants;
import com.hnjk.core.support.context.SpringContextHolder;
import com.hnjk.core.support.context.SystemContextHolder;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.basedata.model.Grade;
import com.hnjk.edu.basedata.model.YearInfo;
import com.hnjk.edu.basedata.service.IGradeService;
import com.hnjk.edu.basedata.service.IYearInfoService;
import com.hnjk.edu.jmx.client.IDocumentConvertService;
import com.hnjk.edu.learning.model.ActiveCourseExam;
import com.hnjk.edu.learning.model.CourseExam;
import com.hnjk.edu.learning.model.StuActiveCourseExamCount;
import com.hnjk.edu.learning.model.StudentLearnPlan;
import com.hnjk.edu.learning.service.IActiveCourseExamService;
import com.hnjk.edu.learning.service.ICourseExamService;
import com.hnjk.edu.learning.service.ILearningJDBCService;
import com.hnjk.edu.learning.service.ILearningTimeSettingService;
import com.hnjk.edu.learning.service.IStudentActiveCourseExamService;
import com.hnjk.edu.learning.service.IStudentLearnPlanService;
import com.hnjk.edu.learning.service.impl.CourseExamServiceImpl;
import com.hnjk.edu.learning.util.CourseExamUtils;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.roll.service.IClassesService;
import com.hnjk.edu.roll.service.IStudentInfoService;
import com.hnjk.edu.teaching.model.LearningTimeSetting;
import com.hnjk.edu.teaching.model.Syllabus;
import com.hnjk.edu.teaching.model.TeachingPlanCourse;
import com.hnjk.edu.teaching.model.TeachingPlanCourseStatus;
import com.hnjk.edu.teaching.model.UsualResults;
import com.hnjk.edu.teaching.model.UsualResultsRule;
import com.hnjk.edu.teaching.service.ICourseService;
import com.hnjk.edu.teaching.service.ISyllabusService;
import com.hnjk.edu.teaching.service.ITeachingPlanCourseService;
import com.hnjk.edu.teaching.service.ITeachingPlanCourseStatusService;
import com.hnjk.edu.teaching.service.IUsualResultsRuleService;
import com.hnjk.edu.teaching.service.impl.SyllabusServiceImpl;
import com.hnjk.edu.teaching.service.impl.TeachingPlanCourseStatusServiceImpl;
import com.hnjk.edu.util.ProjectHelper;
import com.hnjk.extend.plugin.excel.service.IExportExcelService;
import com.hnjk.extend.plugin.fileconvert.DocumentToRtfConvert;
import com.hnjk.extend.plugin.fileconvert.RtfReader;
import com.hnjk.platform.system.UserOperationLogsHelper;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.Attachs;
import com.hnjk.platform.system.model.UserOperationLogs;
import com.hnjk.platform.system.service.IAttachsService;
import com.hnjk.platform.system.service.IDictionaryService;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;
import com.hnjk.security.model.UserExtends;

/**
 * 随堂练习管理
 * <code>ActiveCourseExamController</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-8-6 上午08:59:58
 * @see 
 * @version 1.0
 */
@Controller
public class ActiveCourseExamController extends FileUploadAndDownloadSupportController {

	private static final long serialVersionUID = 7279374811088302309L;
	
	@Autowired
	@Qualifier("courseService")
	private ICourseService courseService;
	
	@Autowired
	@Qualifier("syllabusService")
	private ISyllabusService syllabusService;
	
	@Autowired
	@Qualifier("activeCourseExamService")
	private IActiveCourseExamService activeCourseExamService;
	
	@Autowired
	@Qualifier("courseExamService")
	private ICourseExamService courseExamService;
	
	@Autowired
	@Qualifier("learningJDBCService")
	private ILearningJDBCService learningJDBCService;
	
	@Autowired
	@Qualifier("attachsService")
	private IAttachsService attachsService;
	
	@Autowired
	@Qualifier("studentActiveCourseExamService")
	private IStudentActiveCourseExamService studentActiveCourseExamService;
	
	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;
	
	@Autowired
	@Qualifier("gradeService")
	private IGradeService gradeService;
	
	@Autowired
	@Qualifier("yearInfoService")
	private IYearInfoService yearInfoService;

	@Autowired
	@Qualifier("studentLearnPlanService")
	private IStudentLearnPlanService studentLearnPlanService;
	
	@Autowired
	@Qualifier("usualResultsRuleService")
	private IUsualResultsRuleService usualResultsRuleService;
	
	@Autowired
	@Qualifier("classesService")
	private IClassesService classesService;
	
	@Autowired
	@Qualifier("exportExcelService")
	private IExportExcelService exportExcelService;//Excel导出服务
	
	@Autowired
	@Qualifier("dictService")
	private IDictionaryService dictionaryService;
	
	@Autowired
	@Qualifier("teachingPlanCourseStatusService")
	private ITeachingPlanCourseStatusService teachingPlanCourseStatusService;
	
	@Autowired
	@Qualifier("studentinfoservice")
	private IStudentInfoService studentInfoService;
	
	@Autowired
	@Qualifier("teachingPlanCourseService")
	private ITeachingPlanCourseService teachingPlanCourseService;
	
	@Autowired
	@Qualifier("learningTimeSettingService")
	private ILearningTimeSettingService learningTimeSettingService;
	
	/**
	 * 检测该门课程是否已建立教学大纲
	 * @param courseId
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/metares/exercise/active/checkSyllabus.html")
	public void isEmptySyllabus(String courseId,HttpServletResponse response) throws WebException {		
		List<Syllabus> sybs = syllabusService.findSyllabusTreeList(courseId);
		boolean isempty = true;
		if(sybs != null && sybs.size() > 0){	
			isempty = false;
		}	
		renderJson(response, JsonUtils.booleanToJson(isempty));
	}
	
	/**
	 * 批量添加随堂练习
	 * @param syllabusId
	 * @param request
	 * @param model
	 * @param objPage
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/metares/exercise/activeexercise/input.html")
	public String editActiveCourseExam(String syllabusId,HttpServletRequest request,ModelMap model,Page objPage) throws WebException{
		objPage.setOrderBy("examType,resourceid");
		objPage.setOrder(Page.ASC);
		String courseId = request.getParameter("courseId");	
		String keywords = ExStringUtils.trimToEmpty(request.getParameter("keywords"));
		String examType = request.getParameter("examType");	
		String difficult = request.getParameter("difficult");
		//查询条件
		Map<String,Object> condition = new HashMap<String,Object>(20);
		condition.put("notInExamType", "('5')");
		condition.put("examform", "unit_exam");
		if(ExStringUtils.isNotEmpty(courseId)) {
			condition.put("courseId", courseId);
		}
		if(ExStringUtils.isNotEmpty(keywords)) {
			condition.put("keywords", keywords);
		}
		if(ExStringUtils.isNotEmpty(examType)) {
			condition.put("examType", examType);
		}
		if(ExStringUtils.isNotEmpty(difficult)) {
			condition.put("difficult", difficult);
		}
		if(ExStringUtils.isNotEmpty(syllabusId)) {
			Syllabus syllabus = syllabusService.get(syllabusId);			
			model.addAttribute("syllabus", syllabus);
			condition.put("courseId", syllabus.getCourse().getResourceid());
			condition.put("syllabusId", syllabusId);
//			Map<String,Object> map = new HashMap<String,Object>();
//			map.put("syllabusId", syllabusId);
//			List<ActiveCourseExam> listExams = activeCourseExamService.findActiveCourseExamByCondition(map);
//			String ids = "";
//			for (ActiveCourseExam activeCourseExam : listExams) {
//				ids += activeCourseExam.getCourseExam().getResourceid()+",";				
//			}
//			model.addAttribute("existCourseExamIds", ids);//已选择的试题id
		}
		
		Page list = courseExamService.findCourseExamByCondition(condition,objPage);
		model.addAttribute("courseExamList", list);
		model.addAttribute("condition", condition);
		return "/edu3/learning/activecourseexam/activecourseexam-form";
	}
	
	/**
	 * 保存随堂练习
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/metares/exercise/activeexercise/save.html")
	public void saveActiveCourseExam(HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>(10);
		try {			
			String from = request.getParameter("from");
			String syllabusId = request.getParameter("syllabusId");
			if(ExStringUtils.isBlank(syllabusId)){
				throw new WebException("请选择一个知识节点");
			}
			Syllabus syllabus = syllabusService.get(syllabusId);
			if(ExStringUtils.isNotEmpty(from)){ //--------------------更新
				String[] examId = request.getParameterValues("examId");
				String[] showOrder = request.getParameterValues("showOrder");
				String[] score = request.getParameterValues("score");
				String[] referSyllabusTreeIds = request.getParameterValues("referSyllabusTreeIds");
				String[] referSyllabusTreeNames = request.getParameterValues("referSyllabusTreeNames");
				if(null!=examId && examId.length>0){
					List<ActiveCourseExam> list = new ArrayList<ActiveCourseExam>();
					for (int i = 0; i < examId.length; i++) {
						ActiveCourseExam activeCourseExam = activeCourseExamService.get(examId[i]);
						activeCourseExam.setShowOrder(Integer.parseInt(ExStringUtils.defaultIfEmpty(showOrder[i], "0")));
						activeCourseExam.setScore(Long.parseLong(ExStringUtils.defaultIfEmpty(score[i], "0")));
						activeCourseExam.setReferSyllabusTreeIds(referSyllabusTreeIds[i]);
						activeCourseExam.setReferSyllabusTreeNames(referSyllabusTreeNames[i]);
						list.add(activeCourseExam);
					}
					activeCourseExamService.batchSaveOrUpdate(list);
					map.put("statusCode", 200);
					map.put("message", "保存成功！");
					map.put("currentIndex", "2");
					map.put("syllabusId", syllabusId);
					map.put("courseId", syllabus.getCourse().getResourceid());
					UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), "9", UserOperationLogs.UPDATE, list);
				}
				
			} else { //-------------------------------------------------------------------保存
				String resourceid = request.getParameter("resourceid");
				if(ExStringUtils.isNotEmpty(resourceid)){
					List<ActiveCourseExam> list = new ArrayList<ActiveCourseExam>();
					int nextShowOrder = activeCourseExamService.getNextShowOrder(syllabusId);
					for (String id : resourceid.split(",")) {
						if(!activeCourseExamService.isExsitAcitveCoruseExam(syllabusId, id)){ //不存在，加入
							CourseExam courseExam = courseExamService.get(id);
							ActiveCourseExam activeCourseExam = new ActiveCourseExam();
							activeCourseExam.setSyllabus(syllabus);
							activeCourseExam.setCourseExam(courseExam);							
							list.add(activeCourseExam);
							
							if(CourseExam.COMPREHENSION.equals(courseExam.getExamType())){//材料题
								activeCourseExam.setShowOrder(nextShowOrder);
								for (CourseExam child : courseExam.getChilds()) {
									ActiveCourseExam childExam = new ActiveCourseExam();
									childExam.setSyllabus(syllabus);
									childExam.setCourseExam(child);							
									childExam.setShowOrder(nextShowOrder++);
									
									list.add(childExam);
								}
							} else {
								activeCourseExam.setShowOrder(nextShowOrder++);
							}							
						}
					}
					activeCourseExamService.batchSaveOrUpdate(list);
					map.put("statusCode", 200);
					map.put("message", "保存成功！");
					map.put("callbackType", "forward");
					map.put("forwardUrl", request.getContextPath() +"/edu3/metares/exercise/activeexercise/input.html?syllabusId="+syllabusId);
					UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), "/edu3/metares/exercise/activeexercise/save.html", UserOperationLogs.INSERT, list);
				}
			}			
		}catch (Exception e) {
			logger.error("保存随堂练习出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败！<br/>"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 知识结构树
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/metares/exercise/activeexercise/syllabustree.html")
	public String selectSyllabusTree(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		String courseId = request.getParameter("courseId");	
		String idsN = request.getParameter("idsN");
		String namesN = request.getParameter("namesN");
		String checkedIds = ExStringUtils.defaultIfEmpty(request.getParameter("checkedIds"), "");		
		if(ExStringUtils.isNotEmpty(courseId)){
			List<Syllabus> syllabusList = syllabusService.findSyllabusTreeList(courseId);
			
			if(syllabusList!=null && !syllabusList.isEmpty()){
				Map<String,String> map = new HashMap<String, String>();
				for (String str : checkedIds.split(",")) {
					map.put(str, "");
				}
				String jsonString = JsonUtils.objectToJson(SyllabusServiceImpl.getSyllabusTree(syllabusList,map,true));
				model.addAttribute("syllabusTree", jsonString);				
			}	
			model.addAttribute("idsN", idsN);
			model.addAttribute("namesN", namesN);
		}		
		return "/edu3/learning/activecourseexam/syllabus-selector";
	}	
	
	/**
	 * 获取排序号
	 * @param syllabusId
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/framework/acitvecourseexam/showorder.html")
	public void ajaxActiveCourseExamShowOrder(String syllabusId,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Integer showOrder = 0;
		if(ExStringUtils.isNotEmpty(syllabusId)){
			showOrder = activeCourseExamService.getNextShowOrder(syllabusId);
		}
		renderJson(response, showOrder.toString());
	}
	
	/**
	 * 删除随堂练习
	 * @param resourceid
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/metares/exercise/activeexercise/remove.html")
	public void removeActiveCourseExam(String resourceid,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		try {			
			if(ExStringUtils.isNotBlank(resourceid)){			
				activeCourseExamService.batchCascadeDelete(resourceid.split("\\,"));
				map.put("statusCode", 200);
				map.put("message", "删除成功！");				
				map.put("forward", request.getContextPath()+"/edu3/metares/exercise/active/addactivecourseexam.html");
				
				String courseId = ExStringUtils.trimToEmpty(request.getParameter("courseId"));
				String syllabusId = ExStringUtils.trimToEmpty(request.getParameter("syllabusId"));
				map.put("currentIndex", "2");
				map.put("syllabusId", syllabusId);
				map.put("courseId", courseId);
				
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), "9", UserOperationLogs.DELETE, "ActiveCourseExam: "+resourceid);
			}
		} catch (Exception e) {
			logger.error("删除随堂练习出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}	

	/**
	 * 导出随堂练习
	 * @param courseId
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/edu3/metares/exercise/activeexercise/export.html")
	public void exportActiveCourseExam(String courseId,HttpServletRequest request,HttpServletResponse response) throws WebException{
		try{
			if(ExStringUtils.isNotEmpty(courseId)){
				Course course = courseService.get(courseId);
				StringBuffer outStr = new StringBuffer();
				outStr.append("<h2>"+course.getCourseName()+"随堂练习</h2>");
				List<Syllabus> syllabus = syllabusService.findSyllabusTreeList(courseId);
				if(syllabus!=null && syllabus.size()>0){
					for (Syllabus sybs : syllabus) {
						if(sybs.getParent()==null) {
							continue;
						}
						List<ActiveCourseExam> list = activeCourseExamService.findByHql(" from "+ActiveCourseExam.class.getSimpleName()+" where isDeleted=0 and syllabus.resourceid=? order by showOrder,resourceid ", sybs.getResourceid());
						if(list!=null && list.size()>0){
							outStr.append("<br/><div><h3><b>"+sybs.getFullNodeName()+"</b></h3><div>");
							for (ActiveCourseExam exam : list) {
								if(CourseExam.COMPREHENSION.equals(exam.getCourseExam().getExamType())){
									outStr.append("<div>"+(ExStringUtils.isNotEmpty(exam.getReferSyllabusTreeNames())?"(知识点："+exam.getReferSyllabusTreeNames()+")":"")+exam.getCourseExam().getQuestion()+"</div>");
								} else {
									outStr.append("<div><b>"+exam.getShowOrder()+". </b>"+(ExStringUtils.isNotEmpty(exam.getReferSyllabusTreeNames())?"(知识点："+exam.getReferSyllabusTreeNames()+")":"")+exam.getCourseExam().getQuestion()+"</div>");
									outStr.append("<div>参考答案： "+exam.getCourseExam().getAnswer()+"</div>");
									if(ExStringUtils.isNotEmpty(ExStringUtils.trimToEmpty(exam.getCourseExam().getParser()))){
										outStr.append("<div>问题解析： "+exam.getCourseExam().getParser()+"</div>");
									}
								}								
								outStr.append("<br/>");
							}
							outStr.append("</div></div>");
						} 						
					}
				}
				String rootPath = Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles";
				GUIDUtils.init();	
				String tempFilePath = rootPath+File.separator+GUIDUtils.buildMd5GUID(false)+".doc";
				String htmlContent = outStr.toString().replace("src=\"/edu3/attachs/", "src=\""+request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+"/edu3/attachs/");
				
				DocumentToRtfConvert.htmlToRtfBuilder(tempFilePath, htmlContent, null);
				logger.info("导出随堂练习文件:{}",tempFilePath);
				downloadFile(response, course.getCourseName()+"随堂练习.doc", tempFilePath, true);
			} 
		}catch(Exception e){
			logger.error("导出随堂练习出错："+e.fillInStackTrace());
			renderHtml(response, "<script type='text/javascript'>alert('导出随堂练习出错!');history.go(-1);</script>");
		}
	}
	/**
	 * 下载模板
	 * @param courseId
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/metares/exercise/activeexercise/syllabus/export.html")
	public void exportSyllabus(String courseId,HttpServletRequest request,HttpServletResponse response) throws WebException{
		try{
			if(ExStringUtils.isNotEmpty(courseId)){
				Course course = courseService.get(courseId);
				StringBuffer outStr = new StringBuffer("<html><body>");	
				outStr.append("<h2>"+course.getCourseName()+"随堂练习</h2>");	
				List<Syllabus> syllabus = syllabusService.findSyllabusTreeList(courseId);
				if(syllabus!=null && syllabus.size()>0){
					for (Syllabus sybs : syllabus) {
						if(sybs.getParent()==null) {
							continue;
						}
						outStr.append("<h3>#知识点:"+sybs.getNodeName()+"(id:"+sybs.getResourceid()+")</h3>");	
						outStr.append("<h3>◎题型：单选题</h3><br/>");
						outStr.append("<h3>◎题型：多选题</h3><br/>");
						outStr.append("<h3>◎题型：判断题</h3><br/>");
						outStr.append("<h3>◎题型：填空题</h3><br/>");
						outStr.append("<h3>◎题型：材料题</h3><br/>");
						outStr.append("<h4>T题型：单选题</h4><br/>");
						outStr.append("<h4>T题型：多选题</h4><br/>");
						outStr.append("<h4>T题型：判断题</h4><br/>");
						
					}
				}
				outStr.append("</body></html>");
				String rootPath = Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles";
				GUIDUtils.init();	
				String tempFilePath = rootPath+File.separator+GUIDUtils.buildMd5GUID(false)+".doc";
				DocumentToRtfConvert.htmlToRtfBuilder(tempFilePath, outStr.toString(), null);				
				logger.info("导出知识结构树模板:{}",tempFilePath);
				downloadFile(response, course.getCourseName()+"随堂练习模板.doc", tempFilePath, true);
			} 
		}catch(Exception e){
			logger.error("导出知识结构树模板出错："+e.fillInStackTrace());
			renderHtml(response, "<script type='text/javascript'>alert('导出知识结构树模板出错!');history.go(-1);</script>");
		}
	}
	/**
	 * 上传习题
	 * @param request
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/framework/activeexercise/upload.html")
	public String uploadActiveCourseExam(String courseId,HttpServletRequest request,ModelMap model) throws WebException{			
		if(ExStringUtils.isNotBlank(courseId)){
			model.addAttribute("course", courseService.get(courseId));
			User user = SpringSecurityHelper.getCurrentUser();
			model.addAttribute("currentUser", user);
		}
		return "/edu3/learning/activecourseexam/activecourseexam-import";
	}
	/**
	 * 导入随堂练习
	 * @param courseId
	 * @param uploadfileid
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/metares/exercise/activeexercise/import.html")
	public void importActiveCourseExam(String courseId,String uploadfileid,String isEnrolExam,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		String tempZipPath = null;//临时存储下载的zip文件
		String tempPicPatch = null;//临时存储复制的图片目录
		try {				
			if(ExStringUtils.isNotBlank(courseId) && ExStringUtils.isNotBlank(uploadfileid)){	
				Attachs attach = attachsService.get(uploadfileid);
				String filePath = attach.getSerPath()+File.separator+attach.getSerName();				
				String text = "";
				
				if("doc".equalsIgnoreCase(attach.getAttType())){//doc格式
					IDocumentConvertService documentConvertService = SpringContextHolder.getBean("documentConvertServer");
					String zipPath = documentConvertService.convertDocToHtml(Constants.EDU3_DATAS_LOCALROOTPATH+"temp",filePath, attach.getSerName());
					tempZipPath = zipPath;
					
					String username = SpringSecurityHelper.getCurrentUserName();
					String picPatch = "users"+File.separator+username+File.separator+"images"+File.separator+"courseexam"+File.separator+ExDateUtils.formatDateStr(new Date(), "yyyy_MM_dd_HHmmss");
					tempPicPatch = Constants.EDU3_DATAS_LOCALROOTPATH+picPatch;//图片的目标存储目录
					
					text = CourseExamServiceImpl.getTextFromZipOfHtml(zipPath, picPatch);
				} else {
					text = RtfReader.getTextFromRtf(filePath);
				}
				int size = courseExamService.parseAndImportActiveCourseExam(text);
				
				map.put("statusCode", 200);				
				map.put("message", "本次共导入条"+size+"试题！");	
				map.put("currentIndex", "2");
				map.put("syllabusId", "");
				map.put("courseId", courseId);
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), "9", UserOperationLogs.IMPORT, "导入随堂练习, 课程: "+courseId+", 附件: "+uploadfileid);
			}
		} catch (Exception e) {
			logger.error("导入随堂练习出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", e.getMessage());		
			try {
				if(ExStringUtils.isNotBlank(tempPicPatch)){
					FileUtils.delFolder(tempPicPatch);//删除图片目录,导入失败时,当次解压复制的图片以废弃
				}
			} catch (Exception ei) {				
			}
		} finally {
			//清除临时文件夹temp中的zip文件和临时解压文件
			try {
				if(ExStringUtils.isNotBlank(tempZipPath)){
					FileUtils.delFile(tempZipPath);//删除下载的zip文件
					FileUtils.delFolder(ExStringUtils.substringBeforeLast(tempZipPath, ".zip"));//删除解压目录
				}
			} catch (Exception e2) {
			}
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}

	/**
	 * 随堂练习回答情况统计
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/metares/exercise/activeexercise/stat.html")
	public String reportActiveCourseExam(HttpServletRequest request, HttpServletResponse response, ModelMap model)throws WebException {
		String courseId = request.getParameter("courseId");
		String gradeid = request.getParameter("gradeid");
		String classicid = request.getParameter("classicid");
		String unitid = request.getParameter("unitid");
		String teachingtype = request.getParameter("teachingtype");
		
		Map<String,Object> condition = new HashMap<String,Object>(20);
		try {
			User user = SpringSecurityHelper.getCurrentUser();
			String teacherId = "";
			if(SpringSecurityHelper.isUserInRole(CacheAppManager.getSysConfigurationByCode("sysuser.role.teacher").getParamValue())){
				teacherId = user.getResourceid();	
				if(!SpringSecurityHelper.isTeachingCentreTeacher(user)){
					unitid = user.getOrgUnit().getResourceid();
					request.setAttribute("activecourseexamstatUnit", unitid);
				}
			}
			if(ExStringUtils.isNotBlank(teacherId)){
				condition.put("teacherId",teacherId);
			}
			List<Map<String, Object>> list = activeCourseExamService.getTeacherOnlineCourse(condition);
			String courseStr = getCourseSelect("activecourseexamCourseId","courseId",courseId,list);
			model.addAttribute("activecourseexamstat", courseStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		condition.clear();
		
		if(ExStringUtils.isNotEmpty(courseId)){
			condition.put("courseId", courseId);
			if(ExStringUtils.isNotEmpty(gradeid)) {
				condition.put("gradeid", gradeid);
			}
			if(ExStringUtils.isNotEmpty(classicid)) {
				condition.put("classicid", classicid);
			}
			if(ExStringUtils.isNotEmpty(unitid)) {
				condition.put("unitid", unitid);
			}
			if(ExStringUtils.isNotEmpty(teachingtype)) {
				condition.put("teachingtype", teachingtype);
			}
			condition.put("studentStatus", "'11','16','25'");
			List<Map<String,Object>> list = learningJDBCService.statStudentActiveCourseExam(condition);
			if(list != null && !list.isEmpty()){
				for (Map<String,Object> obj : list) {
					Double correctcount = Double.parseDouble(obj.get("correctcount").toString());
					Double mistakecount = Double.parseDouble(obj.get("mistakecount").toString());
					if(correctcount+mistakecount==0){
						obj.put("correctper", 0.0);
						obj.put("mistakeper", 0.0);
					} else {
						obj.put("correctper", BigDecimalUtil.div(correctcount, correctcount+mistakecount, 4));
						obj.put("mistakeper", BigDecimalUtil.div(mistakecount, correctcount+mistakecount, 4));
					}
				}
			}
			model.addAttribute("condition", condition);
			model.addAttribute("activecourseexamStatList", list);
		}		
		return "/edu3/learning/activecourseexam/activecourseexam-stat";
	}
	/**
	 * 查看学生答题情况
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/metares/exercise/activeexercise/state.html")
	public String stateActiveCourseExam(HttpServletRequest request, HttpServletResponse response, ModelMap model)throws WebException {
		String courseId = request.getParameter("courseId");
		String yearInfoId = request.getParameter("yearInfoId");
		String term = request.getParameter("term");
		String unitid = request.getParameter("unitid");
		String classesId = request.getParameter("classesId");
		
		Map<String,Object> condition = new HashMap<String,Object>(20);
		User user = SpringSecurityHelper.getCurrentUser();
		String roleCode = CacheAppManager.getSysConfigurationByCode("sysuser.role.teacher").getParamValue();//获取角色编码
		// 获取该用户作为班主任的所有班级
		String classesIds = "";
		try {
			if(!SpringSecurityHelper.isUserInRole("ROLE_BRS_STUDENTSTATUS")){
				String teacherId = "";
				classesIds = classesService.findByMasterId(user.getResourceid());
				if(ExStringUtils.isNotEmpty(classesIds)){
					condition.put("classesIds",Arrays.asList(classesIds.split(",")));
				}
				if(SpringSecurityHelper.isUserInRole(roleCode)){// 该用户为老师
					teacherId = user.getResourceid();	
					if(!SpringSecurityHelper.isTeachingCentreTeacher(user)){
						unitid = user.getOrgUnit().getResourceid();
						request.setAttribute("activecourseexamstateUnit", unitid);
					}
				}
				if(ExStringUtils.isNotBlank(teacherId)){
					condition.put("teacherId",teacherId);
				}
			}else{
				condition.put("schoolId",user.getOrgUnit().getResourceid());
			}
			
			List<Map<String, Object>> list = activeCourseExamService.getTeacherOnlineCourse(condition);
			String courseStr = getCourseSelect("activecourseexam_state_CourseId","courseId",courseId,list);
			model.addAttribute("activecourseexamCourseSelect", courseStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		condition.clear();
		if(ExStringUtils.isNotEmpty(yearInfoId)){
			condition.put("yearInfoId", yearInfoId);
		}
		if(ExStringUtils.isNotEmpty(term)){
			condition.put("term", term);
		}	
		if(ExStringUtils.isNotEmpty(unitid)){
			condition.put("unitid", unitid);
		}
		if(ExStringUtils.isNotEmpty(classesId)){
			condition.put("classesId", classesId);
		}
		condition.put("studentStatus", "'11','16','25'");
		if(!SpringSecurityHelper.isUserInRole("ROLE_BRS_STUDENTSTATUS")){
			if(SpringSecurityHelper.isUserInRole(roleCode)){
				condition.put("teacherId", user.getResourceid());	
			}		
			if(ExStringUtils.isNotEmpty(classesIds)){// 班主任
				condition.put("classesIds",Arrays.asList(classesIds.split(",")));
			}
		} else {// 教务员
			condition.put("schoolId",user.getOrgUnit().getResourceid());
		}
		
		if(ExStringUtils.isNotEmpty(courseId)){
			condition.put("courseId", courseId);	
			List<Map<String, Object>> list = learningJDBCService.stateStudentActiveCourseExam(condition);
			if(list != null && !list.isEmpty()){
				for (Map<String, Object> obj : list) {
					Double correctcount = Double.parseDouble(obj.get("correctcount").toString());
					Double mistakecount = Double.parseDouble(obj.get("mistakecount").toString());
					try {
						obj.put("correctper", BigDecimalUtil.div(correctcount, correctcount+mistakecount, 4));
					} catch (Exception e) {
						obj.put("correctper", 0);
					}
					
				}
			}
			model.addAttribute("activecourseexamStateList", list);
		}
		model.addAttribute("condition", condition);
		
		String classesCondition = "";
		if (CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())
				|| SpringSecurityHelper.isTeachingCentreTeacher(user)) {
//			classesCondition = "brSchool.resourceid='" + curUser.getOrgUnit().getResourceid() + "'";
			if(!SpringSecurityHelper.isUserInRole("ROLE_BRS_STUDENTSTATUS")){
				classesCondition += " and (resourceid in (select tt.classes.resourceid from TeachingPlanCourseTimetable tt where tt.teacherId='"+user.getResourceid()+"') "
										   + " or classesmasterid='"+user.getResourceid()+"') ";
			} else{
				classesCondition += " and brSchool.resourceid='"+user.getOrgUnit().getResourceid()+"' ";
			}
		}else{
			model.addAttribute("isadmin", "Y");
//			if(ExStringUtils.isNotEmpty(branchSchool)){
//				classesCondition = "brSchool.resourceid='" + branchSchool + "'";
//			}
		}
		model.addAttribute("classesCondition", classesCondition);
		
		return "/edu3/learning/activecourseexam/activecourseexam-state";
	}
	/**
	 * 查看学生随堂练习得分累积情况
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/metares/exercise/activeexercise/score.html")
	public String scoreActiveCourseExam(HttpServletRequest request, Page objPage, ModelMap model)throws WebException {
		String courseId = request.getParameter("courseId");
		String yearInfoId = request.getParameter("yearInfoId");
		String term = request.getParameter("term");
		String studyNo = ExStringUtils.trimToEmpty(request.getParameter("studyNo"));
		String studentName = ExStringUtils.trimToEmpty(request.getParameter("studentName"));
		String classesId = ExStringUtils.trimToEmpty(request.getParameter("classesId"));
		
		Map<String,Object> condition = new HashMap<String,Object>(20);
		User user = SpringSecurityHelper.getCurrentUser();
		// 获取该用户作为班主任的所有班级
		try {
			condition = ProjectHelper.accessDataFilterCondition(condition, user);
			List<Map<String, Object>> list = activeCourseExamService.getTeacherOnlineCourse(condition);
			String courseStr = getCourseSelect("activecourseexam_state_CourseId","courseId",courseId,list);
			model.addAttribute("activecourseexamscore", courseStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(ExStringUtils.isNotEmpty(courseId)){
			condition.put("courseId", courseId);
		}		
		if(ExStringUtils.isNotEmpty(yearInfoId)){
			condition.put("yearInfoId", yearInfoId);
		}
		if(ExStringUtils.isNotEmpty(term)){
			condition.put("term", term);
		}		
		if(ExStringUtils.isNotEmpty(studyNo)){
			condition.put("studyNo", studyNo);
		}	
		if(ExStringUtils.isNotEmpty(studentName)){
			condition.put("studentName", studentName);
		}	
		if(ExStringUtils.isNotEmpty(classesId)){
			condition.put("classesId", classesId);
		}	
		condition.put("studentStatus", "'11','16','25'");
		if(ExStringUtils.isNotEmpty(courseId))	{
			Map<String, Object> params = new HashMap<String, Object>(20);
			params.put("courseId", courseId);
			long courseExamCount = 0;
			try {
				courseExamCount = baseSupportJdbcDao.getBaseJdbcTemplate().findForLong(" select count(t.resourceid) from edu_lear_courseexam t join edu_lear_exams s on t.examid=s.resourceid where t.isdeleted=0 and t.ispublished='Y' and s.examtype<=5 and s.courseid=:courseId ", params);
			} catch (Exception e) {				
			}	
			condition.put("courseExamCount", courseExamCount);
			Page page = learningJDBCService.scoreStudentActiveCourseExam(condition, objPage);
			
			if(page.getResult() != null && !page.getResult().isEmpty()){
				Map<String, Object> obj;
				for (Object o : page.getResult()) {
					obj = (Map<String, Object>)o;
					Double correctcount = Double.parseDouble(obj.get("correctcount").toString());
					Double counts = Double.parseDouble(obj.get("counts").toString());
					Double submitNum = obj.get("submitNum")==null?0d: ((BigDecimal)obj.get("submitNum")).doubleValue();
					if(counts==null || counts == 0.0){
						obj.put("correctper", 0);
						obj.put("submitAndTotalRate", 0);
						counts = 0d;
						submitNum = 0d;
					} else {
						obj.put("correctper", BigDecimalUtil.div(correctcount, counts, 4));
						obj.put("submitAndTotalRate", BigDecimalUtil.div(submitNum, counts, 4));
					}
					obj.put("submitAndTotalNum", submitNum.intValue()+"/"+counts.intValue());
				}
			}
			model.addAttribute("activecourseexamScoreList", page);
		}	
		model.addAttribute("condition", condition);		
		
		String classesCondition = "";
		if (CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())
				|| SpringSecurityHelper.isTeachingCentreTeacher(user)) {
//			classesCondition = "brSchool.resourceid='" + curUser.getOrgUnit().getResourceid() + "'";
			if(!SpringSecurityHelper.isUserInRole("ROLE_BRS_STUDENTSTATUS")){//非教学点教务员
				classesCondition += " and (resourceid in (select tt.classes.resourceid from TeachingPlanCourseTimetable tt where tt.teacherId='"+user.getResourceid()+"') "
										   + " or classesmasterid='"+user.getResourceid()+"') ";
			} else{//教学点教务员
				classesCondition += " and brSchool.resourceid='"+user.getOrgUnit().getResourceid()+"' ";
			}
		}else{
			model.addAttribute("isadmin", "Y");
//			if(ExStringUtils.isNotEmpty(branchSchool)){
//				classesCondition = "brSchool.resourceid='" + branchSchool + "'";
//			}
		}
		model.addAttribute("classesCondition", classesCondition);
		
		return "/edu3/learning/activecourseexam/activecourseexam-score";
	}
	/**
	 * 查看随堂练习分布情况
	 * @param request
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/metares/exercise/activeexercise/distribute.html")
	public String distributeActiveCourseExam(HttpServletRequest request, ModelMap model)throws WebException {
		Map<String,Object> condition = new HashMap<String,Object>(20);
		String courseId = request.getParameter("courseId");
		
		User user = SpringSecurityHelper.getCurrentUser();
		String teacherId = "";
		if(SpringSecurityHelper.isUserInRole(CacheAppManager.getSysConfigurationByCode("sysuser.role.teacher").getParamValue())){
			teacherId = user.getResourceid();	
		}
		
		if(ExStringUtils.isNotBlank(teacherId)){
			condition.put("teacherId",teacherId);
		}
		try {
			List<Map<String, Object>> list = activeCourseExamService.getTeacherOnlineCourse(condition);
			String courseStr = getCourseSelect("activecourseexam_state_CourseId","courseId",courseId,list);
			model.addAttribute("courseSelect", courseStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
				
		if(ExStringUtils.isNotEmpty(courseId))	{
			condition.put("courseId", courseId);			
			List<Map<String,Object>> list = learningJDBCService.distributeStudentActiveCourseExam(condition);
			model.addAttribute("activecourseexamDistributeList", list); 
		}	
		model.addAttribute("condition", condition);		
		return "/edu3/learning/activecourseexam/activecourseexam-distribute";
	}
	/**
	 * 预览随堂练习	
	 * @param courseId
	 * @param syllabusId
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/metares/exercise/activeexercise/preview.html")
	public String previewActiveCourseExam(String courseId,String syllabusId, String isPublished,HttpServletRequest request,Page objPage, ModelMap model)throws WebException {
		objPage.setOrderBy("isPublished,syllabus.resourceid,showOrder,resourceid");
		objPage.setOrder(Page.ASC);
		Map<String,Object> condition = new HashMap<String,Object>(20);
		
		if(ExStringUtils.isNotEmpty(courseId))	{
			condition.put("courseId", courseId);
			if(ExStringUtils.isNotBlank(syllabusId)){
				condition.put("syllabusId", syllabusId);
			}
			if(ExStringUtils.isNotBlank(isPublished)){
				condition.put("isPublished", isPublished);
			}
			Page page = activeCourseExamService.findActiveCourseExamByCondition(condition, objPage);
			model.addAttribute("activeCourseExamList", page);
			
			if(ExCollectionUtils.isNotEmpty(page.getResult())){
				Map<String,String> map = new HashMap<String, String>(page.getTotalCount());
				for (Object	obj : page.getResult()) {
					ActiveCourseExam exam = (ActiveCourseExam)obj;
					if(!map.containsKey(exam.getSyllabus().getResourceid())){
						map.put(exam.getSyllabus().getResourceid(), exam.getResourceid());
					}
				}
				model.addAttribute("syllabusExam", map);
			}
			
			List<Syllabus> syllabusList = syllabusService.findSyllabusTreeList(courseId);
			model.addAttribute("syllabusList",syllabusList);
		}	
		model.addAttribute("condition", condition);		
		return "/edu3/learning/activecourseexam/activecourseexam-preview";
	}	
	/**
	 * 移动随堂练习
	 * @param fromSyllabusId
	 * @param toSyllabusId
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/metares/exercise/activeexercise/move.html")
	public void moveActiveCourseExam(String resourceid,String fromSyllabusId,String toSyllabusId,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		try {			
			if(ExStringUtils.isNotBlank(toSyllabusId)&&(ExStringUtils.isNotBlank(resourceid)||ExStringUtils.isNotBlank(fromSyllabusId))){			
				activeCourseExamService.moveActiveCourseExam(ExStringUtils.trimToEmpty(resourceid).split("\\,"),fromSyllabusId,toSyllabusId);
				map.put("statusCode", 200);
				map.put("message", "移动成功！");				
				map.put("forward", request.getContextPath()+"/edu3/metares/exercise/active/addactivecourseexam.html");
				
				map.put("currentIndex", "2");
				map.put("syllabusId", fromSyllabusId);
				map.put("courseId", ExStringUtils.trimToEmpty(request.getParameter("courseId")));
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), "9", UserOperationLogs.UPDATE, "移动随堂练习: \nActiveCourseExam: "+ExStringUtils.trimToEmpty(resourceid)+"\nfromSyllabusId: "+ExStringUtils.trimToEmpty(fromSyllabusId)+"\ntoSyllabusId: "+toSyllabusId);
			}
		} catch (Exception e) {
			logger.error("移动随堂练习出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "移动出错!<br/>"+e.getMessage());
		} 
		renderJson(response, JsonUtils.mapToJson(map));
	}	
	/**
	 * 学生答题情况
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/studentactivecourseexam/list.html")
	public String listStudentActiveCourseExam(HttpServletRequest request,Page objPage, ModelMap model) throws WebException {
		objPage.setOrderBy("studentInfo.studyNo,activeCourseExam.syllabus.course.courseCode,activeCourseExam.syllabus.resourceid,activeCourseExam.showOrder,activeCourseExam.resourceid");
		objPage.setOrder(Page.ASC);//设置默认排序方式			
		
		String courseId = request.getParameter("courseId");	
		String syllabusId = request.getParameter("syllabusId");
		String yearInfoId = request.getParameter("yearInfoId");
		String term = request.getParameter("term");
		String studyNo = ExStringUtils.trimToEmpty(request.getParameter("studyNo"));
		String studentName = ExStringUtils.trimToEmpty(request.getParameter("studentName"));
		String classesId = ExStringUtils.trimToEmpty(request.getParameter("classesId"));

		Map<String,Object> condition = new HashMap<String,Object>();
		User user = SpringSecurityHelper.getCurrentUser();
		String roleCode = CacheAppManager.getSysConfigurationByCode("sysuser.role.teacher").getParamValue();//获取角色编码
		// 获取该用户作为班主任的所有班级
		String classesIds = "";
		try {
			if(!SpringSecurityHelper.isUserInRole("ROLE_BRS_STUDENTSTATUS")){
				String teacherId = "";
				classesIds = classesService.findByMasterId(user.getResourceid());
				if(ExStringUtils.isNotEmpty(classesIds)){
					condition.put("classesIds",Arrays.asList(classesIds.split(",")));
				}
				
				if(SpringSecurityHelper.isUserInRole(roleCode)){// 该用户为老师
					teacherId = user.getResourceid();	
				}
				if(ExStringUtils.isNotBlank(teacherId)){
					condition.put("teacherId",teacherId);
				}
			}else{
				condition.put("schoolId",user.getOrgUnit().getResourceid());
			}
			
			List<Map<String, Object>> list = activeCourseExamService.getTeacherOnlineCourse(condition);
			String courseStr = getCourseSelect("studentanswer_courseExamCourseId","courseId",courseId,list);
			model.addAttribute("studentactivecourseexamlistcourse", courseStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		condition.clear();
		if(ExStringUtils.isNotEmpty(courseId)){
			condition.put("courseId", courseId);
			List<Syllabus> syllabusList = syllabusService.findSyllabusTreeList(courseId);
			model.addAttribute("syllabusList", syllabusList);			
		}
		if(ExStringUtils.isNotBlank(syllabusId)){
			condition.put("syllabusId", syllabusId);
		}
		if(ExStringUtils.isNotBlank(yearInfoId)){
			condition.put("yearInfoId", yearInfoId);
		}
		if(ExStringUtils.isNotBlank(term)){
			condition.put("term", term);
		}
		if(ExStringUtils.isNotEmpty(studyNo)){
			condition.put("studyNo", studyNo);
		}
		if(ExStringUtils.isNotEmpty(studentName)){
			condition.put("studentName", studentName);
		}
		if(ExStringUtils.isNotEmpty(classesId)){
			condition.put("classesId", classesId);
		}
		condition.put("studentStatus", "'11','16','25'");
		if(!SpringSecurityHelper.isUserInRole("ROLE_BRS_STUDENTSTATUS")){
			if(SpringSecurityHelper.isUserInRole(roleCode)){
				condition.put("teacherId", user.getResourceid());	
			}		
			if(ExStringUtils.isNotEmpty(classesIds)){// 班主任
				condition.put("classesIds",Arrays.asList(classesIds.split(",")));
			}
		} else {// 教务员
			condition.put("schoolId",user.getOrgUnit().getResourceid());
		}
		//condition.put("isHasResult", Constants.BOOLEAN_YES);
		
		if(ExStringUtils.isNotEmpty(courseId)||ExStringUtils.isNotEmpty(studyNo)||ExStringUtils.isNotEmpty(studentName)){
			Page page = studentActiveCourseExamService.findStudentActiveCourseExamByCondition(condition, objPage);		
			model.addAttribute("stuExamList", page);
		}		
		model.addAttribute("condition", condition);	
		
		Grade grade = gradeService.findUnique("from "+Grade.class.getSimpleName()+" where isDeleted = ? and isDefaultGrade = ?", 0,Constants.BOOLEAN_YES);
		model.addAttribute("defaultGrade", grade);	
		
		String classesCondition = "";
		User curUser 		 = SpringSecurityHelper.getCurrentUser();
		if (CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(curUser.getOrgUnit().getUnitType())
				|| SpringSecurityHelper.isTeachingCentreTeacher(curUser)) {
//			classesCondition = "brSchool.resourceid='" + curUser.getOrgUnit().getResourceid() + "'";
			if(!SpringSecurityHelper.isUserInRole("ROLE_BRS_STUDENTSTATUS")){
				classesCondition += " and (resourceid in (select tt.classes.resourceid from TeachingPlanCourseTimetable tt where tt.isDeleted=0 and tt.teacherId='"+user.getResourceid()+"') "
										   + " or classesmasterid='"+user.getResourceid()+"') ";
			} else{
				classesCondition += " and brSchool.resourceid='"+user.getOrgUnit().getResourceid()+"' ";
			}
		}else{
			model.addAttribute("isadmin", "Y");
//			if(ExStringUtils.isNotEmpty(branchSchool)){
//				classesCondition = "brSchool.resourceid='" + branchSchool + "'";
//			}
		}
		model.addAttribute("classesCondition", classesCondition);
		
		return "/edu3/learning/activecourseexam/studentactivecourseexam-list";
	}
	/**
	 * 重做随堂练习
	 * @param resourceid
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/studentactivecourseexam/redo.html")
	public void reDoStudentCourseExam(String resourceid,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		try {			
			if(ExStringUtils.isNotBlank(resourceid)){			
				studentActiveCourseExamService.redoActiveCourseExam(resourceid.split("\\,"));
				map.put("statusCode", 200);
				map.put("message", "操作成功！");				
				map.put("forward", request.getContextPath()+"/edu3/metares/exercise/active/addactivecourseexam.html");
			}
		} catch (Exception e) {
			logger.error("重做随堂练习出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "操作失败!");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}	
	/**
	 * 审核发布/取消发布随堂练习
	 * @param resourceid
	 * @param isPublished
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/metares/exercise/activeexercise/publish.html")
	public void publishStudentCourseExam(String resourceid,String isPublished,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		try {			
			if(ExStringUtils.isNotBlank(resourceid) && ExStringUtils.isNotBlank(isPublished)){			
				activeCourseExamService.publishActiveCourseExam(resourceid.split("\\,"),isPublished);
				map.put("statusCode", 200);
				map.put("message", (Constants.BOOLEAN_YES.equals(isPublished)?"审核发布":"取消发布")+"随堂练习成功！");
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), "9", UserOperationLogs.PASS, (Constants.BOOLEAN_YES.equals(isPublished)?"审核发布":"取消发布")+"随堂练习,ActiveCourseExam: "+resourceid);
			}
		} catch (Exception e) {
			logger.error("审核发布随堂练习出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", (Constants.BOOLEAN_YES.equals(isPublished)?"审核发布":"取消发布")+"随堂练习失败!");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}	
	/**
	 * 对随堂练习进行调分
	 * @param resourceid
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/metares/exercise/activeexercise/markscore.html")
	public void markscoreStudentCourseExam(String resourceid,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		try {			
			if(ExStringUtils.isNotBlank(resourceid)){			
				studentActiveCourseExamService.scoreActiveCourseExam(resourceid.split("\\,"));
				map.put("statusCode", 200);
				map.put("message","操作成功！");				
			}
		} catch (Exception e) {
			logger.error("对随堂练习进行调分:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "操作失败!");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}	
	/**
	 * 导出学生随堂练习
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/metares/courseexam/studentcourseexam/export.html")
	public void exportStudentActiveCourseExam(HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			String yearInfoId = ExStringUtils.trimToEmpty(request.getParameter("yearInfoId"));
			String term = ExStringUtils.trimToEmpty(request.getParameter("term"));
			if(ExStringUtils.isNotBlank(yearInfoId)&&ExStringUtils.isNotBlank(term)){
				String rooturl = CacheAppManager.getSysConfigurationByCode("web.uploadfile.rooturl").getParamValue();
				String fullRootUrl = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+rooturl;
				//fullRootUrl = "http://183.62.37.8"+rooturl;
				//学生id，学号，姓名
				//String stuSql = "select distinct t.studentid,si.studyno,si.studentname from edu_lear_studentcourseexam t join edu_roll_studentinfo si on t.studentid=si.resourceid join edu_lear_courseexam c on t.courseexamid=c.resourceid join edu_teach_syllabustree s on s.resourceid=c.syllabustreeid where t.isdeleted=0 and s.courseid=:courseId ";
				String stuSql = "select si.resourceid studentid,si.studyno,si.studentname from edu_roll_studentinfo si where si.isdeleted=0 and exists ( select t.resourceid from edu_lear_studentcourseexam t join edu_lear_courseexam c on t.courseexamid=c.resourceid join edu_teach_syllabustree s on c.syllabustreeid=s.resourceid where t.isdeleted=0 and t.studentid=si.resourceid and s.courseid=:courseId )";
				//练习id，学生答案
				String sql = "select t.courseexamid,t.answer from edu_lear_studentcourseexam t join edu_lear_courseexam c on t.courseexamid=c.resourceid join edu_teach_syllabustree s on s.resourceid=c.syllabustreeid where t.isdeleted=0 and s.courseid=:courseId and t.studentid=:studentId ";
				String hql = " from "+ActiveCourseExam.class.getSimpleName()+" a where a.isDeleted=0 and a.syllabus.course.resourceid=? and a.isPublished='Y' order by a.syllabus.syllabusLevel,a.syllabus.showOrder,a.showOrder,a.resourceid ";
				//把答题情况标为失效
				String delSql = "update edu_lear_studentcourseexam t set t.isdeleted=1 where t.isdeleted=0 and t.studentid=:studentId and t.courseexamid in ( select c.resourceid from edu_lear_courseexam c join edu_teach_syllabustree s on c.syllabustreeid=s.resourceid where s.courseid=:courseId )";
				
				String courseSql = "select c.* from edu_base_course c  where c.isdeleted=0 and exists ( select t.resourceid from edu_lear_studentcourseexam t join edu_lear_courseexam m on t.courseexamid=m.resourceid join edu_teach_syllabustree s on m.syllabustreeid=s.resourceid where t.isdeleted=0 and s.courseid=c.resourceid ) ";
				//List<Course> courseList = courseService.findByHql("from "+Course.class.getSimpleName()+" where isDeleted=? and status=? and hasResource=?  ", 0,1l,Constants.BOOLEAN_YES);//所有启用的课程
				List<Course> courseList = baseSupportJdbcDao.getBaseJdbcTemplate().findList(courseSql, Course.class, null);
				long success = 0;//导出文件成功数
				long failed = 0;//导出文件失败数
				Map<String, Object> param = new HashMap<String, Object>();
				for (Course course : courseList) {					
					String courseId = course.getResourceid();	
					
					param.clear();					
					param.put("courseId", courseId);
					List<Map<String, Object>> listStuIds = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(stuSql, param);
					if(ExCollectionUtils.isEmpty(listStuIds)){//没有可导出的学生
						continue;
					}
					//查出当前课程所有发布的随堂练习
					List<ActiveCourseExam> courseExamlist = activeCourseExamService.findByHql(hql, courseId);
					if(ExCollectionUtils.isEmpty(courseExamlist)){	//课程没有随堂练习，跳过
						continue;
					} 					
					
					YearInfo yearInfo = yearInfoService.get(yearInfoId);	
					String yearInfoStr = yearInfo.getFirstYear()+""+(yearInfo.getFirstYear()+1)+term;
					//文件上级目录:.../studentcourseexam/${yearInfoStr}/${courseCode}/
					String rootPath = Constants.EDU3_DATAS_LOCALROOTPATH+"studentcourseexams"+File.separator+yearInfoStr+File.separator+course.getCourseCode()+File.separator;
					if(!new File(rootPath).exists()){
						new File(rootPath).mkdirs();
					}
					//随堂练习题目
					StringBuffer sb = getCourseExamTemplateContent(course.getCourseName(), courseExamlist);
					//替换图片的相对地址为绝对地址
					String templateContent = sb.toString().replace("src=\""+rooturl, "src=\""+fullRootUrl);
										
					String tempStr = null;	
					for (Map<String, Object> stu: listStuIds) {
						if(stu.get("STUDENTID")==null){
							continue;
						}
						String id = stu.get("STUDENTID").toString();//学生id
						String studyNo = stu.get("STUDYNO").toString();//学号
						String studentName = stu.get("STUDENTNAME").toString();//姓名			
						
						//导出路径：/studentcourseexam/${yearInfoStr}/${courseCode}/${yearInfoStr}_${courseCode}_${studyNo}.doc
						String tempFilePath = rootPath+yearInfoStr+"_"+course.getCourseCode()+"_"+studyNo+".doc";
						if(new File(tempFilePath).exists()){//已存在文件，说明已导出
							continue;
						}
						
						param.put("courseId", courseId);
						param.put("studentId", id);
						//学生答案列表
						List<Map<String, Object>> list = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql, param);											
						List<String> examIds = new ArrayList<String>();
						List<String> answers = new ArrayList<String>();
						for (Map<String, Object> obj : list) {
							examIds.add("#"+obj.get("COURSEEXAMID")+"#");
							String answer = obj.get("ANSWER")!=null?obj.get("ANSWER").toString():"";
							if(ExStringUtils.equals(answer, "T")){
								answer = "对";
							} else if(ExStringUtils.equals(answer, "F")){
								answer = "错";
							}
							answers.add(answer);
						}
						tempStr = templateContent;
						tempStr = ExStringUtils.replaceEach(tempStr, examIds.toArray(new String[examIds.size()]), answers.toArray(new String[answers.size()]));
						tempStr = tempStr.replaceAll("\\#[0-9A-Za-z]{32}#", "");//替换学生未答的答案
						tempStr = tempStr.replace("#studentName#", studentName);					
						
						try {
							CourseExamUtils.htmlToDocumentByPoi(tempStr, tempFilePath);		
							success ++;
							logger.info("导出文件:{}",tempFilePath);
							//把答题情况标为失效
							int totalCount = baseSupportJdbcDao.getBaseJdbcTemplate().executeForMap(delSql, param);
							logger.info("课程:"+course.getCourseName()+",学生:"+studentName+",共删除"+totalCount+"条答题记录");
						} catch (IOException ioe) {
							failed ++;
							logger.error("导出文件("+tempFilePath+")出错:{}", ioe.fillInStackTrace());
						}	
					}					
				}
				map.put("statusCode", 200);
				map.put("message", "本次成功导出随堂练习文件:"+success+((failed==0)?"":"<br/>导出失败文件数："+failed));
			} else {
				map.put("statusCode", 300);
				map.put("message", "请选择年度和学期!");
			}
		} catch (Exception e) {
			logger.error("操作失败:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "操作失败!<br/>"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 生成整门课程的随堂练习模板
	 * @param courseName
	 * @param courseExamlist
	 * @return
	 */
	private StringBuffer getCourseExamTemplateContent(String courseName,List<ActiveCourseExam> courseExamlist){
		StringBuffer sb = new StringBuffer("<html><body>");
		//#studentName#替换为学生姓名
		sb.append("<h2>#studentName#："+courseName+"随堂练习</h2>");
		String syllabusId = null;
		for (ActiveCourseExam exam : courseExamlist) {
			if(!exam.getSyllabus().getResourceid().equals(syllabusId)){
				syllabusId = exam.getSyllabus().getResourceid();
				sb.append("<div><h3><b>"+exam.getSyllabus().getNodeName()+"</b></h3></div>");
			}
			sb.append("<div><b>"+exam.getShowOrder()+". </b>"+exam.getCourseExam().getQuestion()+"</div>");
			//可替换成学生实际的答案:#(练习id)...#
			sb.append("<div><b>学生答案：</b> #"+exam.getResourceid()+"#</div>");
			String answer = ExStringUtils.trimToEmpty(exam.getCourseExam().getAnswer());
			if(ExStringUtils.equals(answer, "√")){
				answer = "对";
			} else if(ExStringUtils.equals(answer, "×")){
				answer = "错";
			}
			sb.append("<div><b>参考答案：</b> "+answer+"</div>");
			if(ExStringUtils.isNotEmpty(ExStringUtils.trimToEmpty(exam.getCourseExam().getParser()))){
				sb.append("<div><b>问题解析：</b> "+exam.getCourseExam().getParser()+"</div>");
			}
			sb.append("<br/>");
		}			
		sb.append("</body></html>");
		return sb;
	}
	/**
	 * 老师查看随堂练习题目
	 * @param courseId
	 * @param syllabusId
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/metares/exercise/activeexercise/view.html")
	public String viewActiveCourseExam(String courseId,String syllabusId,HttpServletRequest request,Page objPage, ModelMap model)throws WebException {
		Map<String,Object> condition = new HashMap<String,Object>();
		try {
			User user = SpringSecurityHelper.getCurrentUser();
			String teacherId = "";
			if(SpringSecurityHelper.isUserInRole(CacheAppManager.getSysConfigurationByCode("sysuser.role.teacher").getParamValue())){
				teacherId = user.getResourceid();	
			}
			if(ExStringUtils.isNotBlank(teacherId)){
				condition.put("teacherId",teacherId);
			}
			List<Map<String, Object>> list = activeCourseExamService.getTeacherOnlineCourse(condition);
			String courseStr = getCourseSelect("activecourseexamview_courseId","courseId",courseId,list);
			model.addAttribute("activecourseexamallviewCourse", courseStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//如果主讲老师只有一门课程,直接选定该课程
		if(ExStringUtils.isBlank(courseId) 
				&& SpringSecurityHelper.isUserInRole(CacheAppManager.getSysConfigurationByCode("sysuser.role.teacher").getParamValue())){
			User user = SpringSecurityHelper.getCurrentUser();
			Map<String, Object> param = new HashMap<String, Object>();
			String sql = " select distinct t.courseid from edu_teach_teachtask t where t.isdeleted=0 and  t.taskstatus=3 and (t.teacherid=:teacherId or t.assistantids like :assistantid) ";
			param.put("teacherId", user.getResourceid());
			param.put("assistantid", "%"+user.getResourceid()+"%");
			try {
				List<Map<String, Object>> list = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql, param);
				if(ExCollectionUtils.isNotEmpty(list) && list.size()==1){
					courseId = list.get(0).get("COURSEID").toString();
				}
			} catch (Exception e) {
			}
		}		
		previewActiveCourseExam(courseId, syllabusId, Constants.BOOLEAN_YES, request, objPage, model);	
		return "/edu3/learning/activecourseexam/activecourseexam-allview";
	}

	/**
	 * 学生查看随堂练习回答情况
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/learning/exercise/practice-list.html")
	public String practiceList(HttpServletRequest request,Page objPage, ModelMap model)throws WebException {
		Map<String,Object> condition = new HashMap<String,Object>();
		String courseId = request.getParameter("courseId");
		String flag     = ExStringUtils.trimToEmpty(request.getParameter("flag"));
		User user = SpringSecurityHelper.getCurrentUser();
		//查处当前用户
		if (SpringSecurityHelper.isUserInRole("ROLE_STUDENT")) {
			String studentId = "";
			if (null != user.getUserExtends()
					&& null != user.getPropertys(UserExtends.USER_EXTENDCODE_DEFAULTROLLID)) {
				studentId = user.getPropertys(UserExtends.USER_EXTENDCODE_DEFAULTROLLID).getExValue();
			}
			if (ExStringUtils.isNotEmpty(courseId) && !"".equals(studentId)) {
				condition.put("studentId", studentId);
				condition.put("courseId", courseId);
				List<Map<String, Object>> list = learningJDBCService.distributeStudentActiveCourseExamFinished(condition);
				model.addAttribute("activecourseexamDistributeList", list);
				model.addAttribute("condition", condition);
				model.addAttribute("flag", flag);
				// 统计总数：
				int countAll = 0;
				int mistakeAll = 0;
				int corretAll = 0;
				for (int i = 0; i < list.size(); i++) {
					Map<String, Object> map = list.get(i);
					countAll   += Integer.valueOf(map.get("counts").toString());
					mistakeAll += Integer.valueOf(map.get("mistakecount").toString());
					corretAll  += Integer.valueOf(map.get("correctcount").toString());
				}
				model.addAttribute("countsAll", countAll);
				model.addAttribute("submitAll", mistakeAll+corretAll);
				model.addAttribute("correctAll", corretAll);

				

				Double originalCourseExamResults = 0.0;// 随堂练习分
				if (ExStringUtils.isNotEmpty(studentId)) {// 学生
					StudentLearnPlan plan = studentLearnPlanService.getStudentLearnPlanByCourse(courseId, studentId,"studentId");
					String teachType = null == plan || null == plan.getTeachingPlanCourse() ? "": plan.getTeachingPlanCourse().getTeachType();
					String yearInfoId = "";
					String term = "";
					if (plan != null) {
						if ((plan.getStatus() == 2&& Constants.BOOLEAN_NO.equals(plan.getIsRedoCourseExam()))|| plan.getStatus() == 3) {
							// 沿用平时成绩 或 已考完状态
							if (plan.getUsualResults() != null) { // 直接显示平时成绩
								UsualResults r = plan.getUsualResults();
								originalCourseExamResults = BigDecimalUtil.round(Double.valueOf(ExStringUtils.defaultIfEmpty(r.getCourseExamResults(),"0")),1);
							}
						} else {// 正在学习 或 准备考试 或 重做随堂练习
							/*Grade grade = gradeService.getDefaultGrade();
							if (grade != null) { // 取当前学期的积分规则
								yearInfoId = grade.getYearInfo().getResourceid();
								term = grade.getTerm();
							}*/
							/*String yearTerm = CacheAppManager.getSysConfigurationByCode("sysCurrentGrade").getParamValue();

							if (null!=yearTerm) {
								String[] ARRYyterm = yearTerm.split("\\.");
								yearInfoId = yearInfoService.getByFirstYear(Long.parseLong(ARRYyterm[0])).getResourceid();
								term = ARRYyterm[1];
							}*/
							//是否开课,网上学习时间内,排课
							StudentInfo studentInfo = studentInfoService.findUniqueByProperty("resourceid", studentId);
							TeachingPlanCourse teachingPlanCourse = plan.getTeachingPlanCourse();
							TeachingPlanCourseStatus teachingPlanCourseStatus = teachingPlanCourseStatusService
									.findOneByCondition(studentInfo.getGrade()
											.getResourceid(), studentInfo.getTeachingPlan().getResourceid(),
											teachingPlanCourse.getResourceid(), studentInfo.getBranchSchool().getResourceid());
							if (teachingPlanCourseStatus != null) {
								
								String yearTermStr = teachingPlanCourseStatus.getTerm();
								String year = "";
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
									
								}else{*/
									UsualResultsRule rule = usualResultsRuleService.getUsualResultsRuleByCourseAndYear(courseId, yearInfoId, term);
									if (rule != null) {
										if (rule.getCourseExamResultPer() > 0) {// 随堂练习分
											StuActiveCourseExamCount sace = plan.getStuActiveCourseExamCount();
											if (null != sace) {
												if ("netsidestudy".equals(teachType)) {
													originalCourseExamResults = sace.getCorrectper();
												} else {
													originalCourseExamResults = sace.getCorrectper() >= rule.getCourseExamCorrectPer() ? 100 : sace.getCorrectper();
												}
											} else {
												// 2014.05.07
												// 由于实时统计随堂联系分，比较耗费时间，容易导致系统崩溃
												// 目前采用直接获取随堂练习得分累计情况表中的信息来显示
												// 但是不排除找不到得分累计统计表为空的时候，当为空时，沿用旧的统计方法
												Double per = studentActiveCourseExamService.avgStudentActiveCourseExamResult(courseId, studentId);
												// 网络面授类课程的平时分只取随堂练习的分数，且不按照计分规则中的比例来计算
												if ("netsidestudy".equals(teachType)) {
													originalCourseExamResults = per * 100;
												} else {
													originalCourseExamResults = per * 100 >= rule.getCourseExamCorrectPer() ? 100 : per * 100;
												}
											}
										}
									}
								//}
							}
							
							
						}
					}
				}
				BigDecimal _courseExamResults = new BigDecimal(originalCourseExamResults);
				_courseExamResults = _courseExamResults.setScale(2, BigDecimal.ROUND_HALF_UP); 
				model.addAttribute("originalCourseExamResults",_courseExamResults);
			}
		} else {
			request.getSession().setAttribute("message", "该功能为学生专用");
		}
			
		return "/edu3/learning/exercise/practice-list";
	}
	
	/**
	 * 导出学生随堂练习得分累积情况
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/metares/exercise/activeexercise/exportStuExamInfo.html")
	public void exportStudentExamScoreInfo(HttpServletRequest request, HttpServletResponse response) throws WebException{
		Map<String, Object> condition = new HashMap<String, Object>();
		
		String courseId = request.getParameter("courseId");
		String yearInfoId = request.getParameter("yearInfoId");
		String term = request.getParameter("term");
		String studyNo = ExStringUtils.trimToEmpty(request.getParameter("studyNo"));
		String studentName = ExStringUtils.trimToEmpty(request.getParameter("studentName"));
		String classesId = ExStringUtils.trimToEmpty(request.getParameter("classesId"));
		String studentIds = ExStringUtils.trimToEmpty(request.getParameter("studentIds"));
		
		try {
			if(ExStringUtils.isNotEmpty(courseId)){
				condition.put("courseId", courseId);
			}		
			
			if(ExStringUtils.isNotEmpty(studentIds)){
				condition.put("studentIds", Arrays.asList(studentIds.split(",")));
			} else {
				if(ExStringUtils.isNotEmpty(yearInfoId)){
					condition.put("yearInfoId", yearInfoId);
				}
				if(ExStringUtils.isNotEmpty(term)){
					condition.put("term", term);
				}		
				if(ExStringUtils.isNotEmpty(studyNo)){
					condition.put("studyNo", studyNo);
				}	
				if(ExStringUtils.isNotEmpty(studentName)){
					condition.put("studentName", studentName);
				}	
				if(ExStringUtils.isNotEmpty(classesId)){
					condition.put("classesId", classesId);
				}	
			}
			
			if(ExStringUtils.isNotEmpty(courseId))	{
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("courseId", courseId);
				long courseExamCount = 0;
				try {
					courseExamCount = baseSupportJdbcDao.getBaseJdbcTemplate().findForLong(" select count(t.resourceid) from edu_lear_courseexam t join edu_lear_exams s on t.examid=s.resourceid where t.isdeleted=0 and t.ispublished='Y' and s.examtype<=5 and s.courseid=:courseId ", params);
				} catch (Exception e) {				
				}	
				condition.put("courseExamCount", courseExamCount);
				
				User user = SpringSecurityHelper.getCurrentUser();
				String roleCode = CacheAppManager.getSysConfigurationByCode("sysuser.role.teacher").getParamValue();//获取角色编码
				if(!SpringSecurityHelper.isUserInRole("ROLE_BRS_STUDENTSTATUS")){
					if(SpringSecurityHelper.isUserInRole(roleCode)){
						condition.put("teacherId", user.getResourceid());	
					}		
					String classesIds = classesService.findByMasterId(user.getResourceid());
					if(ExStringUtils.isNotEmpty(classesIds)){// 班主任
						condition.put("classesIds",Arrays.asList(classesIds.split(",")));
					}
				} else {// 教务员
					condition.put("schoolId",user.getOrgUnit().getResourceid());
				}
				// 处理学生随堂练习得分积累情况
				List<Map<String, Object>> stuExamScoreInfoList = learningJDBCService.findStuExamScoreInfo(condition);
				if(ExCollectionUtils.isNotEmpty(stuExamScoreInfoList)){
					for (Map<String, Object> ses : stuExamScoreInfoList) {
						Double correctcount = Double.parseDouble(ses.get("correctcount").toString());
						Double counts = Double.parseDouble(ses.get("counts").toString());
						Double submitNum = ses.get("submitNum")==null?0d: ((BigDecimal)ses.get("submitNum")).doubleValue();
						if(counts==null || counts == 0.0){
							ses.put("correctper",0+"%" );
							ses.put("submitAndTotalRate", "0/0("+0+"%)");
						} else {
							ses.put("correctper", BigDecimalUtil.mul(100d, BigDecimalUtil.div(correctcount, counts, 4))+"%");
							ses.put("submitAndTotalRate", submitNum.intValue()+"/"+counts.intValue()+"("+BigDecimalUtil.mul(100d, BigDecimalUtil.div(submitNum, counts, 4))+"%)");
						}					
					}
				}
				// 导出
				GUIDUtils.init();
				File disFile 	= new File(getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(true) + ".xls");
				String path = "exportStuExamScoreInfo.xls";
				// 字典表要转换
				List<String> dictCodeList = new ArrayList<String>();
				dictCodeList.add("CodeTerm");
				exportExcelService.initParamsByfile(disFile, "stuExamScoreInfoList", stuExamScoreInfoList,dictionaryService.getDictionByMap(dictCodeList, true,IDictionaryService.PREKEY_TYPE_BYCODE),null,null);//初始化配置参数
				exportExcelService.getModelToExcel().setTemplateParam(path,2,null);
				File excelFile = exportExcelService.getExcelFile();//获取导出的文件
				downloadFile(response, "学生随堂练习得分积累情况表"+".xls", excelFile.getAbsolutePath(),true);
			}
		}  catch (Exception e) {
			logger.error("导出学生随堂练习得分累积情况", e);
		}
	}
	
	private String getCourseSelect(String id, String name,String courseid,List<Map<String, Object>> list) {
		StringBuffer SBFcourse = new StringBuffer("<select style='width:240px;' class='flexselect' id='"+id+"' name='"+name+"' >");
		SBFcourse.append("<option value=''>请选择</option>");
		if(ExCollectionUtils.isNotEmpty(list) && list.size() > 0){
			for(Map<String, Object> map : list){
				if(map.get("courseid").equals(courseid)){
					SBFcourse.append("<option value='"+map.get("courseid")+"' selected='selected'>"+map.get("coursecode")+"-"+map.get("coursename")+"</option>");
				}else{
					SBFcourse.append("<option value='"+map.get("courseid")+"'>"+map.get("coursecode")+"-"+map.get("coursename")+"</option>");
				}
			}
		}
		SBFcourse.append("</select>");
		return SBFcourse.toString();
	}
}
