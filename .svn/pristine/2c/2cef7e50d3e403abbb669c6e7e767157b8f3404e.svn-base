package com.hnjk.edu.learning.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.utils.ExBeanUtils;
import com.hnjk.core.foundation.utils.ExCollectionUtils;
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.FileUtils;
import com.hnjk.core.foundation.utils.HttpHeaderUtils;
import com.hnjk.core.foundation.utils.JsonModel;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.FileUploadAndDownloadSupportController;
import com.hnjk.core.support.context.Constants;
import com.hnjk.core.support.context.SpringContextHolder;
import com.hnjk.core.support.context.SystemContextHolder;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.jmx.client.IDocumentConvertService;
import com.hnjk.edu.learning.model.CourseExam;
import com.hnjk.edu.learning.model.CourseExercises;
import com.hnjk.edu.learning.service.ICourseExamService;
import com.hnjk.edu.learning.service.ICourseExercisesService;
import com.hnjk.edu.learning.service.impl.CourseExamServiceImpl;
import com.hnjk.edu.learning.util.CourseExamUtils;
import com.hnjk.edu.learning.vo.CourseExamExcelVo;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.teaching.model.ExamResults;
import com.hnjk.edu.teaching.service.ICourseService;
import com.hnjk.edu.teaching.util.ExamResultImportFileOperateUtil;
import com.hnjk.edu.teaching.vo.ExamResultsVo;
import com.hnjk.extend.plugin.excel.service.IImportExcelService;
import com.hnjk.extend.plugin.fileconvert.RtfReader;
import com.hnjk.platform.system.UserOperationLogsHelper;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.Attachs;
import com.hnjk.platform.system.model.Dictionary;
import com.hnjk.platform.system.model.UserOperationLogs;
import com.hnjk.platform.system.service.IAttachsService;
import com.hnjk.platform.system.service.impl.DictionaryServiceImpl;
import com.hnjk.platform.taglib.JstlCustomFunction;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;

/**
 * 题库试题管理.
 * <code>CourseExamController</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2011-3-29 上午11:16:16
 * @see 
 * @version 1.0
 */
@Controller
public class CourseExamController extends FileUploadAndDownloadSupportController {
	private static final long serialVersionUID = 6207501457313403667L;
	
	@Autowired
	@Qualifier("courseExamService")
	private ICourseExamService courseExamService;
	
	@Autowired
	@Qualifier("courseService")
	private ICourseService courseService;
	
//	@Autowired
//	@Qualifier("syllabusService")
//	private ISyllabusService syllabusService;
	
//	@Autowired
//	@Qualifier("activeCourseExamService")
//	private IActiveCourseExamService activeCourseExamService;
	
	@Autowired
	@Qualifier("attachsService")
	private IAttachsService attachsService;
	
	@Autowired
	@Qualifier("importExcelService")
	private IImportExcelService importExcelService;//excel导入服务
	
	@Autowired
	@Qualifier("courseExercisesService")
	private ICourseExercisesService courseExercisesService;
	
	/**
	 * 题库试题列表
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/metares/courseexam/list.html")
	public String listCourseExam(HttpServletRequest request,Page objPage, ModelMap model) throws WebException {
		objPage.setOrderBy("course.resourceid,fillinDate desc,showOrder");
		objPage.setOrder(Page.ASC);//设置默认排序方式			
		
		String courseId = request.getParameter("courseId");	
		String keywords = ExStringUtils.trimToEmpty(request.getParameter("keywords"));
		String examType = request.getParameter("examType");	
		String examNodeType = request.getParameter("examNodeType");	
		String difficult = request.getParameter("difficult");
		String showOrder = ExStringUtils.trimToEmpty(request.getParameter("showOrder"));
		String examform = ExStringUtils.defaultIfEmpty(ExStringUtils.trimToEmpty(request.getParameter("examform")), "unit_exam");
		//查询条件
		Map<String,Object> condition = new HashMap<String,Object>();
		condition.put("isEnrolExam", Constants.BOOLEAN_NO);
		if (ExStringUtils.isNotEmpty(courseId)) {
			condition.put("courseId", courseId);
		}
		if (ExStringUtils.isNotBlank(keywords)) {
			condition.put("keywords", keywords);
		}
		if (ExStringUtils.isNotEmpty(examType)) {
			condition.put("examType", examType);
		}
		if (ExStringUtils.isNotEmpty(difficult)) {
			condition.put("difficult", difficult);
		}
		if (ExStringUtils.isNotBlank(showOrder)) {
			condition.put("showOrder", showOrder);
		}
		if (ExStringUtils.isNotEmpty(examNodeType)) {
			condition.put("examNodeType", examNodeType);
		}
		if (ExStringUtils.isNotEmpty(examform)) {
			condition.put("examform", examform);
		}

		User user = SpringSecurityHelper.getCurrentUser();
		String roleCode = CacheAppManager.getSysConfigurationByCode("sysuser.role.teacher").getParamValue();
		if(SpringSecurityHelper.isUserInRole(roleCode)) {
			condition.put("teacherId", user.getResourceid());
		}
		
		Page page = courseExamService.findCourseExamByCondition(condition, objPage);
		
		model.addAttribute("courseExamList", page);
		model.addAttribute("condition", condition);
		
		List<Course> courseList = courseService.findByHql("from "+Course.class.getSimpleName()+" c where c.isDeleted=? and c.status=1 and exists ( from "+CourseExam.class.getSimpleName()+" exam where exam.isDeleted=0 and exam.course.resourceid=c.resourceid ) order by c.courseCode ", 0);
		model.addAttribute("courseList", courseList);
		return "/edu3/learning/courseexam/courseexam-list";
	}
	
	/**
	 * 新增编辑试题
	 * @param resourceid
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/metares/courseexam/input.html")
	public String editCourseExam(String resourceid,HttpServletRequest request,ModelMap model) throws WebException{		
		CourseExam courseExam = null;
		if(ExStringUtils.isNotBlank(resourceid)){ //-----编辑
			courseExam = courseExamService.get(resourceid);
		}else{ //----------------------------------------新增
			courseExam = new CourseExam();
			courseExam.setIsEnrolExam(Constants.BOOLEAN_NO);
			
			String courseId = request.getParameter("courseId");
			if(ExStringUtils.isNotEmpty(courseId)){					
				Course course = courseService.load(courseId);
				courseExam.setCourse(course);
			}	
		}	
		model.addAttribute("courseExam", courseExam);
		
		User user = SpringSecurityHelper.getCurrentUser();
		model.addAttribute("storeDir", user.getUsername());
		
		//随堂练习答题选项数
		String answerOptionNum = CacheAppManager.getSysConfigurationByCode("courseExam.answerOptionNum").getParamValue();
		model.addAttribute("answerOptionNum", answerOptionNum);
		
		return "/edu3/learning/courseexam/courseexam-form";
	}
	/**
	 * 保存试题
	 * @param courseId
	 * @param courseExam
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/metares/courseexam/save.html")
	public void saveCourseExam(String courseId,CourseExam courseExam,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();		
		try {
			User user = SpringSecurityHelper.getCurrentUser();
			//ActiveCourseExam activeCourseExam = null;	
			courseExam.setIsEnrolExam(Constants.BOOLEAN_NO);
			if(ExStringUtils.isNotEmpty(courseId)){
				Course course = courseService.load(courseId);
				courseExam.setCourse(course);		
				
				//activeCourseExam = getActiveCourseExam(request,courseExam.getResourceid());//关联随堂练习
			} 
			courseExam.setAnswer(ExStringUtils.trimToEmpty(courseExam.getAnswer()));
			boolean isUpdate = ExStringUtils.isNotBlank(courseExam.getResourceid());
			Date currentDay = new Date();
			if(ExStringUtils.isNotBlank(courseExam.getResourceid())){ //--------------------更新				
				CourseExam p_courseExam = courseExamService.get(courseExam.getResourceid());
				courseExam.setShowOrder(p_courseExam.getShowOrder());
				ExBeanUtils.copyProperties(p_courseExam, courseExam);
				//if(null!=activeCourseExam){
				//	activeCourseExam.setCourseExam(p_courseExam);
				//}
				p_courseExam.setModifyDate(currentDay);
				p_courseExam.setModifyMan(user.getCnName());
				
				if(CourseExam.COMPREHENSION.equals(p_courseExam.getExamType())){//材料题
					for (CourseExam child : p_courseExam.getChilds()) {
						String question = ExStringUtils.trimToEmpty(request.getParameter("question"+child.getResourceid()));
						String answer = ExStringUtils.trimToEmpty(request.getParameter("answer"+child.getResourceid()));
						String answerOptionNum = ExStringUtils.defaultIfEmpty(ExStringUtils.trimToEmpty(request.getParameter("answerOptionNum"+child.getResourceid())), "4");
						child.setQuestion(question);
						child.setAnswer(answer);
						child.setExamNodeType(p_courseExam.getExamNodeType());//材料题小题的题目类别与父题一致
						child.setIsOnlineAnswer(p_courseExam.getIsOnlineAnswer());
						if(NumberUtils.isDigits(answerOptionNum)){//答题选项数
							child.setAnswerOptionNum(Integer.valueOf(answerOptionNum));
						}
						child.setModifyDate(currentDay);
						child.setModifyMan(user.getCnName());
					}
				}				
				courseExamService.saveOrUpdate(p_courseExam);
				map.put("callbackType", "closeCurrent");
			}else{ //-------------------------------------------------------------------保存				
				courseExam.setFillinDate(currentDay);
				courseExam.setFillinMan(user.getCnName());
				courseExam.setFillinManId(user.getResourceid());
				//if(null!=activeCourseExam){
				//	activeCourseExam.setCourseExam(courseExam);
				//}
				courseExamService.saveOrUpdate(courseExam);
			}
			
			map.put("statusCode", 200);
			map.put("message", "保存成功！");
			map.put("navTabId", "courseExamInput");
			map.put("reloadUrl", request.getContextPath() +"/edu3/metares/courseexam/input.html?courseId="+courseId);
//			UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), "/edu3/metares/courseexam/save.html", isUpdate?UserOperationLogs.UPDATE:UserOperationLogs.INSERT, courseExam);
		}catch (Exception e) {
			logger.error("保存试题出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}

	/**
	 * 设置随堂练习
	 * @param request
	 * @param resourceid
	 * @return
	 */
	/*
	private ActiveCourseExam getActiveCourseExam(HttpServletRequest request,String resourceid) {
		String syllabusId = request.getParameter("syllabusId");	
		String examType = ExStringUtils.trimToEmpty(request.getParameter("examType"));
		ActiveCourseExam activeCourseExam = null;
		if(ExStringUtils.isNotEmpty(syllabusId) && CourseExam.COMPREHENSION.equals(examType)){			
			if(!activeCourseExamService.isExsitAcitveCoruseExam(syllabusId, resourceid)){
				String showOrder = ExStringUtils.defaultIfEmpty(request.getParameter("showOrder"),"1");
				String score = ExStringUtils.defaultIfEmpty(request.getParameter("score"),"0");
				String referSyllabusTreeIds = request.getParameter("referSyllabusTreeIds");
				String referSyllabusTreeNames = request.getParameter("referSyllabusTreeNames");
				
				Syllabus syllabus = syllabusService.get(syllabusId);
				activeCourseExam = new ActiveCourseExam();
				activeCourseExam.setSyllabus(syllabus);
				activeCourseExam.setScore(Long.parseLong(score));
				activeCourseExam.setReferSyllabusTreeIds(referSyllabusTreeIds);
				activeCourseExam.setReferSyllabusTreeNames(referSyllabusTreeNames);
				activeCourseExam.setShowOrder(Integer.parseInt(showOrder));
			}
		}
		return activeCourseExam;
	}
	*/

	/**
	 * 删除试题
	 * @param resourceid
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/metares/courseexam/remove.html")
	public void removeCourseExam(String resourceid,HttpServletRequest request, HttpServletResponse response) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		try {			
			if(ExStringUtils.isNotBlank(resourceid)){			
				courseExamService.batchDeleteCourseExam(resourceid.split("\\,"),Constants.BOOLEAN_NO);
				map.put("statusCode", 200);
				map.put("message", "删除成功！");
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), "9", UserOperationLogs.DELETE, "删除试题：CourseExam: "+resourceid);
				
			}
		} catch (Exception e) {
			logger.error("删除出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错:<br/>"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	
	//知识点列表，用于生成选择列表
	/*
	@RequestMapping("/edu3/framework/syllabus/list.html")
	public void isEmptySyllabus(String courseId,HttpServletResponse response) throws WebException {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>(0);
		if(ExStringUtils.isNotEmpty(courseId)){
			List<Syllabus> sybs = syllabusService.findSyllabusTreeList(courseId);
			for (Syllabus syllabus : sybs) {
				Map<String, Object> map = new HashMap<String, Object>(0);
				map.put("resourceid", syllabus.getResourceid());
				map.put("syllabusName", syllabus.getSyllabusName());
				map.put("syllabusLevel", syllabus.getSyllabusLevel());
				list.add(map);
			}			
		}		
		renderJson(response, JsonUtils.listToJson(list));
	}
	*/
	
	/**
	 * 查看试题
	 * @param courseExamId
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping(value = {"/edu3/metares/courseexam/view.html","/edu3/metares/entrance/courseexam/view.html"})
	public String viewCourseExam(String courseExamId,ModelMap model) throws WebException {
		if(ExStringUtils.isNotEmpty(courseExamId)){
			CourseExam courseExam = courseExamService.get(courseExamId);
			model.addAttribute("courseExam", courseExam);	
		}	
		return "/edu3/learning/courseexam/courseexam-view";
	}

	/**
	 * 上传试题
	 * @param request
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/framework/courseexam/upload.html")
	public String uploadCourseExam(HttpServletRequest request,ModelMap model) throws WebException{		
		String isEnrolExam = ExStringUtils.trimToEmpty(request.getParameter("isEnrolExam"));
		model.addAttribute("isEnrolExam", isEnrolExam);
		
		User user = SpringSecurityHelper.getCurrentUser();
		model.addAttribute("currentUser", user);
		try {
			List<Dictionary> dictList = CacheAppManager.getChildren("CodeExamNodeType");
			Map courseTypeMap = JsonUtils.jsonToMap("{\"YY\":\"英语\",\"YW\":\"语文\",\"SX\":\"数学\",\"NET\":\"网上学习指南\",\"JSJYYJC\":\"计算机应用基础\",\"GZSX\":\"高中数学\",\"GDSX\":\"高等数学\",\"OTHER\":\"其他\"}");
			List<String> courseCode = Arrays.asList(new String[]{"YY","YW","SX","GZSX","GDSX","NET","JSJYYJC","OTHER"});
			
			Map<String,Map<String, Object>> resultMap = new HashMap<String, Map<String,Object>>();
			for (String course : courseCode) {
				List<JsonModel> mList = new ArrayList<JsonModel>(); 
				List<Dictionary> rList = new ArrayList<Dictionary>();
				for (Dictionary dict : dictList) {
					if(dict.getDictValue().startsWith(course)){
						mList.add(new JsonModel(dict.getDictName(), dict.getDictName(), dict.getDictValue()));
						rList.add(dict);
					}
				}	
				dictList.removeAll(rList);
				
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("courseName", courseTypeMap.get(course));
				map.put("dictList", mList);
				
				resultMap.put(course, map);
			}
			//余下的归类为OTHER
			List<JsonModel> otherList = new ArrayList<JsonModel>(); 
			for (Dictionary dict : dictList) {
				otherList.add(new JsonModel(dict.getDictName(), dict.getDictName(), dict.getDictValue()));
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("courseName", courseTypeMap.get("OTHER"));
			map.put("dictList", otherList);			
			resultMap.put("OTHER", map);
			
			model.addAttribute("dictMap", resultMap);
			model.addAttribute("courseList", courseCode);
		} catch (Exception e) {			
		}		
		return "/edu3/learning/courseexam/courseexam-import";
	}
	/**
	 * 导入试题
	 * @param courseId
	 * @param uploadfileid
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/metares/courseexam/import.html")
	public void importCourseExam(String courseId,String uploadfileid,String isEnrolExam,String examform,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		String tempZipPath = null;//临时存储下载的zip文件
		String tempPicPatch = null;//临时存储复制的图片目录
		try {			
			if(ExStringUtils.isNotBlank(courseId) && ExStringUtils.isNotBlank(uploadfileid)){	
				Attachs attach = attachsService.get(uploadfileid);
				String filePath = attach.getSerPath()+File.separator+attach.getSerName();
				String text = "";//获取试题文本
				
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
							
				Course course = null;
				String courseName = null;
				if(Constants.BOOLEAN_NO.equals(isEnrolExam)){//非入学考试课程
					 course = courseService.get(courseId);
					 map.put("navTabId", "RES_METARES_COURSEEXAM");		
					 map.put("reloadUrl", request.getContextPath() +"/edu3/metares/courseexam/list.html?courseId="+courseId+"&examform="+ExStringUtils.trimToEmpty(examform));
				} else {
					courseName = courseId;
					map.put("navTabId", "RES_METARES_ENTRANCE_COURSEEXAM");
					map.put("reloadUrl", request.getContextPath() +"/edu3/metares/entrance/courseexam/list.html?courseName="+courseName);
				}				
				
				List<CourseExam> list = courseExamService.parseAndImportCourseExam(isEnrolExam,examform,course,courseName, text);	

				courseExamService.batchSaveOrUpdate(list);
				map.put("statusCode", 200);				
				map.put("message", "本次共导入条"+list.size()+"试题！");		
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), "9", UserOperationLogs.IMPORT, "导入试题, 课程: "+courseId+" 附件: "+uploadfileid+" 成功条数："+list.size());
			}
		} catch (Exception e) {
			logger.error("导入试题出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "导入出错:<br/>"+e.getMessage());
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
	 * 转换base64图片
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/metares/courseexam/image/convert.html")
	public void convertBase64Img(HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();		
		try {
			Map<String,Object> condition = new HashMap<String, Object>();	
			condition.put("question", "data:image/");//包含base64数据的图片元素
			condition.put("isEnrolExam", Constants.BOOLEAN_NO);
			List<CourseExam> list = courseExamService.findCourseExamByCondition(condition);
			
			if(ExCollectionUtils.isNotEmpty(list)){
				User user = SpringSecurityHelper.getCurrentUser();
				String ext = "users"+File.separator+user.getUsername()+File.separator+"images"+File.separator+"courseexam"+File.separator+ExDateUtils.formatDateStr(new Date(), "yyyy_MM_dd_HHmmss");
				String fileRoot = Constants.EDU3_DATAS_LOCALROOTPATH+ext;				
				String serRoot = CacheAppManager.getSysConfigurationByCode("web.uploadfile.rooturl").getParamValue()+ext.replace(File.separator, "/")+"/";
				for (CourseExam exam : list) {
					String afterQuestion = CourseExamUtils.replaceBase64Image(ExStringUtils.trimToEmpty(exam.getQuestion()), fileRoot, serRoot);
					exam.setQuestion(afterQuestion);
				}	
				courseExamService.batchSaveOrUpdate(list);
			}			
			map.put("statusCode", 200);
			map.put("message", "转换成功！本次已处理"+list.size()+"道题");
		}catch (Exception e) {
			logger.error("转换图片失败：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "转换失败！");
		}
		renderJson(response, JsonUtils.mapToJson(map));		
	}
	/**
	 * 下载试题导入模板
	 * @param course
	 * @param fileName
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/framework/courseexam/template/download.html")
	public void downloadCourseExamTemplate(String course, String fileName,HttpServletRequest request,HttpServletResponse response) throws WebException{
		try {
			if(ExStringUtils.isNotBlank(course) && ExStringUtils.isNotBlank(fileName)){
				if(fileName.endsWith(".xls")||fileName.endsWith(".xlsx")){
					String templateFile = SystemContextHolder.getAppRootPath()+"WEB-INF"+File.separator+"templates"+File.separator+"courseexam"+File.separator+"CourseExamTemplate.xlsx";
					downloadFile(response, fileName, templateFile, false);		
				}else{//doc文件的说明文档
					String templateFile = SystemContextHolder.getAppRootPath()+"WEB-INF"+File.separator+"templates"+File.separator+"courseexam"+File.separator+ExStringUtils.trimToEmpty(course)+"_CourseExamTemplate.doc";
					downloadFile(response, fileName+".doc", templateFile, false);
				}
			}else{
				logger.error("下载试题导入模板失败：{获取参数数据为空}");
				renderHtml(response, "<script type=\"text/javascript\">parent.alertMsg.warn(\"无法找到文件,请联系管理员\")</script>");
			}
		}catch (Exception e) {
			logger.error("下载试题导入模板失败：{}",e.fillInStackTrace());
			renderHtml(response, "<script type=\"text/javascript\">parent.alertMsg.warn(\"下载试题导入模板失败:"+e.getMessage()+"\")</script>");
		}
	}
	
	/**
	 * 上传试题(excel版)
	 * @param request
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/framework/courseexam/uploadExcel.html")
	public String uploadCourseExamExcel(HttpServletRequest request,ModelMap model) throws WebException{			
		User user = SpringSecurityHelper.getCurrentUser();
		model.addAttribute("currentUser", user);
			
		return "/edu3/learning/courseexam/courseexam-importexcel";
	}
	
	/**
	 * 导入试题(excel版)
	 * @param courseId
	 * @param uploadfileid
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/metares/courseexam/importByExcel.html")
	public void importCourseExamByExcel(String courseId,String uploadfileid,String isEnrolExam,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		Map<String,Object> resultMap      = new HashMap<String, Object>();
		StringBuffer message = new StringBuffer();
		try {			
			if(ExStringUtils.isNotBlank(courseId) && ExStringUtils.isNotBlank(uploadfileid)){	
				Attachs attach = attachsService.get(uploadfileid);	
				File excelFile            = attach.getFile();
				
				Course course = courseService.get(courseId);
				
				importExcelService.initParmas(excelFile, "courseExam_excel",resultMap);
				importExcelService.getExcelToModel().setSheet(0);
				importExcelService.getExcelToModel().setStartTitleRow(2);
				List modelList = importExcelService.getModelList();
				
				User user = SpringSecurityHelper.getCurrentUser();
				
				List<CourseExercises> resultList = new ArrayList<CourseExercises>();		
				if (null!=modelList && !modelList.isEmpty()) {							
					for(int j = 0;j<modelList.size();j++){						
						CourseExamExcelVo vo        = (CourseExamExcelVo)modelList.get(j);
						CourseExercises exercises = new CourseExercises();
						
						if(!course.getCourseName().equals(vo.getCourseName())){//设置所属课程
							message.append("第"+(j+1)+"条,导入失败:课程名"+vo.getCourseName()+"不匹配<br/>");
							continue;
						}else{
							exercises.setCourse(course);
						}

						if("单选题".equals(vo.getExamType())){//设置题型
							exercises.setExamType(CourseExercises.SINGLESELECTION);
						}else if("多选题".equals(vo.getExamType())){
							exercises.setExamType(CourseExercises.MUTILPCHIOCE);
						}else if("判断题".equals(vo.getExamType())){
							exercises.setExamType(CourseExercises.CHECKING);
						}else if("填空题".equals(vo.getExamType())){
							exercises.setExamType(CourseExercises.COMPLETION);
						}else if("论述题".equals(vo.getExamType())){
							exercises.setExamType(CourseExercises.ESSAYS);
						}else if("材料题".equals(vo.getExamType())){
							exercises.setExamType(CourseExercises.COMPREHENSION);
						}else{
							message.append("第"+(j+1)+"条,导入失败:暂不支持这种题目类型:"+vo.getExamType()+"<br/>");
							continue;
						}
						
						if(!ExStringUtils.isNotBlank(vo.getQuestion())){//设置题目
							message.append("第"+(j+1)+"条,导入失败:题目为空<br/>");
							continue;
						}else{
							exercises.setQuestion(vo.getQuestion());
						}
						int num = 0;
						if(ExStringUtils.isNotBlank(vo.getOptionA())){
							exercises.setOptionA(vo.getOptionA());
							num++;
						}
						if(ExStringUtils.isNotBlank(vo.getOptionB())){
							exercises.setOptionB(vo.getOptionB());
							num++;
						}
						if(ExStringUtils.isNotBlank(vo.getOptionC())){
							exercises.setOptionC(vo.getOptionC());
							num++;
						}
						if(ExStringUtils.isNotBlank(vo.getOptionD())){
							exercises.setOptionD(vo.getOptionD());
							num++;
						}
						if(ExStringUtils.isNotBlank(vo.getOptionE())){
							exercises.setOptionE(vo.getOptionE());
							num++;
						}
						if(ExStringUtils.isNotBlank(vo.getOptionF())){
							exercises.setOptionF(vo.getOptionF());
							num++;
						}
						
						if(num!=0 || exercises.getExamType()==CourseExercises.CHECKING
								|| exercises.getExamType()==CourseExercises.COMPLETION
								|| exercises.getExamType()==CourseExercises.ESSAYS
								|| exercises.getExamType()==CourseExercises.COMPREHENSION){//设置选项
							exercises.setAnswerOptionNum(num);
						}else{
							message.append("第"+(j+1)+"条,导入失败:选项为空<br/>");
							continue;
						}
						
						if(!ExStringUtils.isNotBlank(vo.getAnswer())){//设置答案
							message.append("第"+(j+1)+"条,导入失败:答案为空<br/>");
							continue;
						}else{
							exercises.setAnswer(vo.getAnswer());
						}
						
						if(ExStringUtils.isNotBlank(vo.getParser())){//设置解析
							exercises.setParser(vo.getParser());
						}
						
						if(ExStringUtils.isNotBlank(vo.getDifficult())){
							if("容易".equals(vo.getDifficult())){//设置难度
								exercises.setDifficult("easy");
							}else if("中等".equals(vo.getDifficult())){
								exercises.setDifficult("hard");
							}else if("难".equals(vo.getDifficult())){
								exercises.setDifficult("difficult");
							}else{
								message.append("第"+(j+1)+"条,导入警告:暂不支持这种难度类型:"+vo.getDifficult()+"<br/>");
							}
						}
						exercises.setExamform("unit_exam");//默认暂时都是随堂练习
						exercises.setShowOrder(resultList.size()+1);
						exercises.setFillinDate(new Date());
						exercises.setFillinMan(user.getCnName());
						exercises.setFillinManId(user.getResourceid());
						resultList.add(exercises);
						
					}
					courseExercisesService.batchSaveOrUpdate(resultList);
					message.append("导入成功:"+resultList.size()+"条<br/>");
				}		
				//String courseName = null;
				//if(Constants.BOOLEAN_NO.equals(isEnrolExam)){//非入学考试课程
					 course = courseService.get(courseId);
					 map.put("navTabId", "RES_METARES_COURSEEXAM");		
					 map.put("reloadUrl", request.getContextPath() +"/edu3/metares/courseexam/list.html?courseId="+courseId);
				/*} else {
					courseName = courseId;
					map.put("navTabId", "RES_METARES_ENTRANCE_COURSEEXAM");
					map.put("reloadUrl", request.getContextPath() +"/edu3/metares/entrance/courseexam/list.html?courseName="+courseName);
				}*/				
				
				map.put("statusCode", 200);				
				map.put("message", message);		
				//UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), "/edu3/metares/courseexam/importByExcel.html", UserOperationLogs.OTHER, "导入试题(excel), 课程: "+courseId+" 附件: "+uploadfileid);
			}
		} catch (Exception e) {
			logger.error("导入试题(excel)出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "导入出错:<br/>"+e.getMessage());
		} 
		renderJson(response, JsonUtils.mapToJson(map));
	}	
}
