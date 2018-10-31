package com.hnjk.edu.learning.controller;

import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.GUIDUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.controller.FileUploadAndDownloadSupportController;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.learning.model.MateResource;
import com.hnjk.edu.learning.model.StudyProgress;
import com.hnjk.edu.learning.service.*;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.roll.service.IClassesService;
import com.hnjk.edu.roll.service.IStudentInfoService;
import com.hnjk.edu.teaching.model.LearningTimeSetting;
import com.hnjk.edu.teaching.service.ICourseService;
import com.hnjk.edu.teaching.service.IUsualResultsService;
import com.hnjk.edu.teaching.vo.UsualResultsVo;
import com.hnjk.edu.util.ProjectHelper;
import com.hnjk.extend.plugin.excel.service.IExportExcelService;
import com.hnjk.platform.system.service.IDictionaryService;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.net.URLEncoder;
import java.util.*;


/** 
 * 学习情况
 * @author Zik, 广东学苑教育发展有限公司
 * @since Jul 25, 2016 10:15:44 AM 
 * 
 */
@Controller
public class StudySituationController extends FileUploadAndDownloadSupportController {

	private static final long serialVersionUID = -5704252868178343177L;
	
	@Autowired
	@Qualifier("activeCourseExamService")
	private IActiveCourseExamService activeCourseExamService;
	
	@Autowired
	@Qualifier("classesService")
	private IClassesService classesService;
	
	@Autowired
	@Qualifier("usualResultsService")
	private IUsualResultsService usualResultsService;
	
	@Autowired
	@Qualifier("learningJDBCService")
	private ILearningJDBCService learningJDBCService;
	
	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;
	
	@Autowired
	@Qualifier("exportExcelService")
	private IExportExcelService exportExcelService;//Excel导出服务
	
	@Autowired
	@Qualifier("dictService")
	private IDictionaryService dictionaryService;
	
	@Autowired
	@Qualifier("studentinfoservice")
	private IStudentInfoService studentInfoService;
	
	@Autowired
	@Qualifier("studyProgressService")
	private IStudyProgressService studyProgressService;
	
	@Autowired
	@Qualifier("courseService")
	private ICourseService courseService;
	
	@Autowired
	@Qualifier("mateResourceService")
	private IMateResourceService mateResourceService;
	
	@Autowired
	@Qualifier("learningTimeSettingService")
	ILearningTimeSettingService learningTimeSettingService;
	
	/**
	 * 查看在线学习情况
	 * @param request
	 * @param response
	 * @param model
	 * @param objPage
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/studySituation/view_onlineLearningInfo.html")
	public String viewOnlineLearningInfo(HttpServletRequest request, HttpServletResponse response, Page objPage, ModelMap model) throws Exception {
		objPage.setOrderBy(" y.firstYear desc,t.term desc,c.coursecode,u.unitcode,g.gradename desc,s.studyno ");
		objPage.setOrder(Page.ASC);//设置默认排序方式
		
		//查询条件
		Map<String,Object> condition = new HashMap<String,Object>();
		String yearInfo = request.getParameter("examOrderYearInfo");
		String term = request.getParameter("examOrderTerm");
		String name = request.getParameter("name");//姓名
		String studyNo = request.getParameter("studyNo");//学号
		String courseId = request.getParameter("courseId");//课程id
		String usualStatus = request.getParameter("usualStatus");
		String branchSchool = request.getParameter("branchSchool");
		String gradeId = request.getParameter("gradeId");
		String classicId = request.getParameter("classicId");
		String schoolType = request.getParameter("schoolType");
		String majorId = request.getParameter("majorId");
		String classesId = request.getParameter("classesId");
		String optType = request.getParameter("optType");
		User user = SpringSecurityHelper.getCurrentUser();
		try {
			condition = ProjectHelper.accessDataFilterCondition(condition, user);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(condition.containsKey("unitId")) {
			model.addAttribute("unitId", user.getOrgUnit().getResourceid());
			//branchSchool = user.getOrgUnit().getResourceid();
		}
		
		if(ExStringUtils.isNotEmpty(yearInfo)){
			condition.put("examOrderYearInfo", yearInfo);
		}
		if(ExStringUtils.isNotEmpty(term)) {
			condition.put("examOrderTerm", term);
		}
		if(ExStringUtils.isNotEmpty(courseId)) {
			condition.put("courseId", courseId);
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
		if(ExStringUtils.isNotBlank(branchSchool)) {
			condition.put("branchSchool", branchSchool);
		}
		if(ExStringUtils.isNotBlank(gradeId)) {
			condition.put("gradeId", gradeId);
		}
		if(ExStringUtils.isNotBlank(classicId)) {
			condition.put("classicId", classicId);
		}
		if(ExStringUtils.isNotBlank(schoolType)) {
			condition.put("schoolType", schoolType);
		}
		if(ExStringUtils.isNotBlank(majorId)) {
			condition.put("majorId", majorId);
		}
		if(ExStringUtils.isNotBlank(classesId)) {
			condition.put("classesId", classesId);
		}
		condition.put("studentStatus", "'11','16','25'");
		String url = "";
		// 获取学习情况
		if("export".equals(optType)){
			List<UsualResultsVo> infoPage = usualResultsService.findLearningInfoListByCondition(condition);
			String fileName = URLEncoder.encode("在线学习情况.xls", "UTF-8");
			request.getSession().setAttribute("onlineLearningInfoList", infoPage);
			response.setContentType("application/vnd.ms-excel");
			//attachment
		  	response.setHeader("Content-Disposition", "attachment; filename="+fileName); 
			url = "/edu3/learning/studySituation/onlineLearningInfoView-export";
		}else {
			Page infoPage = usualResultsService.findLearningInfoByCondition(condition, objPage);
			model.addAttribute("onlineLearningInfoList",infoPage);
			//获取网上学习时间
			if(ExStringUtils.isNotBlank4All(yearInfo,term)){
				LearningTimeSetting learningTime = learningTimeSettingService.findLearningTimeSetting(yearInfo, term);
				if(learningTime!=null){
					model.addAttribute("learningTime", learningTime);
				}
			}
			url = "/edu3/learning/studySituation/onlineLearningInfoView-list";
		}
		model.addAttribute("condition", condition);
		return url;
	}
	
	/**
	 * 学习情况统计
	 * @param request
	 * @param response
	 * @param model
	 * @param objPage
	 * @return
	 * @throws WebException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/edu3/studySituation/onlineLearningInfo_statistics.html")
	public String countLearningInfo(HttpServletRequest request, HttpServletResponse response, Page objPage, ModelMap model) throws WebException {
		String courseId = request.getParameter("courseId");
		String yearInfoId = request.getParameter("yearInfoId");
		String term = request.getParameter("term");
		String studyNo = ExStringUtils.trimToEmpty(request.getParameter("studyNo"));
		String studentName = ExStringUtils.trimToEmpty(request.getParameter("studentName"));
		String branchSchool = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));
		String gradeId = ExStringUtils.trimToEmpty(request.getParameter("gradeId"));
		String classicId = ExStringUtils.trimToEmpty(request.getParameter("classicId"));
		String schoolType = ExStringUtils.trimToEmpty(request.getParameter("schoolType"));
		String majorId = ExStringUtils.trimToEmpty(request.getParameter("majorId"));
		String classesId = ExStringUtils.trimToEmpty(request.getParameter("classesId"));
		
		Map<String,Object> condition = new HashMap<String,Object>();
		User user = SpringSecurityHelper.getCurrentUser();
		// 获取该用户作为班主任的所有班级
		try {
			condition = ProjectHelper.accessDataFilterCondition(condition, user);
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
		if(ExStringUtils.isNotEmpty(branchSchool)){
			condition.put("branchSchool", branchSchool);
		}	
		if(ExStringUtils.isNotEmpty(gradeId)){
			condition.put("gradeId", gradeId);
		}	
		if(ExStringUtils.isNotEmpty(classicId)){
			condition.put("classicId", classicId);
		}	
		if(ExStringUtils.isNotEmpty(schoolType)){
			condition.put("schoolType", schoolType);
		}	
		if(ExStringUtils.isNotEmpty(majorId)){
			condition.put("majorId", majorId);
		}	
		if(ExStringUtils.isNotEmpty(classesId)){
			condition.put("classesId", classesId);
		}	
		condition.put("studentStatus", "'11','16','25'");
		try {
			if(condition.containsKey("yearInfoId")&&condition.containsKey("term")){//condition.containsKey("courseId")
				Page page = learningJDBCService.findLearningInfoStatistics(condition, objPage);
				model.addAttribute("learningInfoStatisticsList", page);
				//获取网上学习时间
				LearningTimeSetting learningTime = learningTimeSettingService.findLearningTimeSetting(yearInfoId, term);
				if(learningTime!=null){
					model.addAttribute("learningTime", learningTime);
				}
			}
		} catch (Exception e) {
			logger.error("获取网上学习情况统计信息出错", e);
		}
		model.addAttribute("condition", condition);		
		
		return "/edu3/learning/studySituation/onlineLearningInfo_statistics";
	}
	
	/**
	 * 导出学生学习情况统计
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/studySituation/exportLearningInfoStatistics.html")
	public void exportLearningInfoStatistics(HttpServletRequest request, HttpServletResponse response) throws WebException {
		Map<String, Object> condition = new HashMap<String, Object>();
		String resourceids = ExStringUtils.trimToEmpty(request.getParameter("resourceids"));
		try {
			if (ExStringUtils.isNotBlank(resourceids)) {
				condition.put("resourceids", resourceids.replaceAll(",", "','"));
			} else {
				String courseId = request.getParameter("courseId");
				String yearInfoId = request.getParameter("yearInfoId");
				String term = request.getParameter("term");
				String studyNo = ExStringUtils.trimToEmpty(request.getParameter("studyNo"));
				String studentName = ExStringUtils.trimToEmpty(request.getParameter("studentName"));
				String branchSchool = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));
				String gradeId = ExStringUtils.trimToEmpty(request.getParameter("gradeId"));
				String classicId = ExStringUtils.trimToEmpty(request.getParameter("classicId"));
				String schoolType = ExStringUtils.trimToEmpty(request.getParameter("schoolType"));
				String majorId = ExStringUtils.trimToEmpty(request.getParameter("majorId"));
				String classesId = ExStringUtils.trimToEmpty(request.getParameter("classesId"));
				
				User user = SpringSecurityHelper.getCurrentUser();
				condition = ProjectHelper.accessDataFilterCondition(condition, user);
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
				if(ExStringUtils.isNotEmpty(branchSchool)){
					condition.put("branchSchool", branchSchool);
				}	
				if(ExStringUtils.isNotEmpty(gradeId)){
					condition.put("gradeId", gradeId);
				}	
				if(ExStringUtils.isNotEmpty(classicId)){
					condition.put("classicId", classicId);
				}	
				if(ExStringUtils.isNotEmpty(schoolType)){
					condition.put("schoolType", schoolType);
				}	
				if(ExStringUtils.isNotEmpty(majorId)){
					condition.put("majorId", majorId);
				}	
				if(ExStringUtils.isNotEmpty(classesId)){
					condition.put("classesId", classesId);
				}
			}
			condition.put("studentStatus", "'11','16','25'");
			List<Map<String, Object>> statiticsList = learningJDBCService.findLearningInfoStatistics(condition);
			
		 	setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
		 	GUIDUtils.init();
		 	//导出
			File excelFile = null;
			File disFile   = new File(getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(false) + ".xls");
			
			//字典转换列
			List<String> dictCodeList = new ArrayList<String>();
			dictCodeList.add("CodeTerm");
			
			//初始化配置参数
			String headerName = "学生学习情况统计";
			exportExcelService.initParmasByfile(disFile, "expLearningInfoStatitics", statiticsList,dictionaryService.getDictionByMap(dictCodeList,true,IDictionaryService.PREKEY_TYPE_BYCODE),null);
			exportExcelService.getModelToExcel().setHeader(headerName);
			exportExcelService.getModelToExcel().setRowHeight(300);//设置行高
				
			excelFile = exportExcelService.getExcelFile();//获取导出的文件
			String downloadFilePath = excelFile.getAbsolutePath();
			
			// 下载
			downloadFile(response, "学生学习情况统计表.xls",downloadFilePath,true);	
		} catch (Exception e) {
			logger.error("导出学生学习情况统计出错", e);
			renderHtml(response, "<script>alert('导出excel文件出错')</script>");
		}	
	}

	/**
	 * 记录学习进度
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/studySituation/recordStudyProgress.html")
	public void recordStudyProgress(HttpServletRequest request, HttpServletResponse response) throws WebException {
		Map<String, Object> map = new HashMap<String, Object>();
		
		String courseId = request.getParameter("courseId");
		String mateId = request.getParameter("mateId");
		String videoWatchTime = request.getParameter("videoWatchTime");
		String videoTotalTime = request.getParameter("videoTotalTime");
		String currentTime = request.getParameter("currentTime");
		
		int returnCode = 200;
		String message = "保存成功";
		logger.info("-courseId- = "+courseId+"-mateId- = "+mateId+"-videoWatchTime- = "+videoWatchTime+"-videoTotalTime- = "+videoTotalTime+"-currentTime- = "+currentTime);
		
		try{
			do{
				User user = SpringSecurityHelper.getCurrentUser();	
				if(user==null){
					returnCode = 300;
					message = "用户不存在";
					break;
				}
				
				StudentInfo studentInfo =studentInfoService.getStudentInfoByUser(user);
				if(studentInfo==null){
					returnCode = 300;
					message = "学籍不存在";
					break;
				}
				
				Course course = courseService.get(courseId);
				if(course==null){
					returnCode = 300;
					message = "课程不存在";
					break;
				}
				
				MateResource mateResource = mateResourceService.get(mateId);
				if(mateResource==null){
					returnCode = 300;
					message = "资源不存在";
					break;
				}
				
				String hql = "select sp from StudyProgress sp where sp.isDeleted=0 and sp.studentInfo.resourceid=? and sp.course.resourceid=? and sp.mate.resourceid=?";
				List<StudyProgress> studyProgressList = studyProgressService.findByHql(hql, studentInfo.getResourceid(),courseId,mateId);
				Date now = new Date();
				StudyProgress studyProgress = null;
				
				int learnedTime = Integer.parseInt(videoWatchTime);
				int totalTime = Integer.parseInt(videoTotalTime);
				int curTime = Integer.parseInt((currentTime.split("\\."))[0]);
				
				if(curTime>totalTime){
					curTime = totalTime;
				}
				
				if (studyProgressList.size()==0) {// 新进度
					// 保存进度
					studyProgress = new StudyProgress();
					studyProgress.setStudentInfo(studentInfo);
					studyProgress.setCourse(course);
					studyProgress.setMate(mateResource);
					studyProgress.setCreateDate(now);
					studyProgress.setUpdateDate(now);
					if(learnedTime<totalTime){	//学习时间不能超过总时长
						studyProgress.setLearnedTime(learnedTime);
					}else{
						studyProgress.setLearnedTime(totalTime);
					}
					studyProgress.setTotalTime(totalTime);
					studyProgress.setResourceType("video");//默认
				} else {// 已有记录,修改更新时间
					studyProgress = studyProgressList.get(0);
					studyProgress.setUpdateDate(now);
					studyProgress.setTotalTime(totalTime);
					learnedTime = learnedTime + studyProgress.getLearnedTime();
					if(learnedTime<totalTime){//学习时长不能超过总时长
						studyProgress.setLearnedTime(learnedTime);				
					}else{
						studyProgress.setLearnedTime(totalTime);
					}
				}
				studyProgress.setCurrentTime(curTime);
				
				studyProgressService.saveOrUpdate(studyProgress);
			}while(false);		
		}catch(Exception e){
			logger.error("保存学习进度出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败！");
		}
		map.put("statusCode", returnCode);
		map.put("message", message);
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
}
