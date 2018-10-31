package com.hnjk.edu.learning.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.utils.BigDecimalUtil;
import com.hnjk.core.foundation.utils.ExCollectionUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.HttpHeaderUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.foundation.utils.SensitivewordFilter;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.controller.FileUploadAndDownloadSupportController;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.basedata.model.Grade;
import com.hnjk.edu.basedata.model.YearInfo;
import com.hnjk.edu.basedata.service.IGradeService;
import com.hnjk.edu.basedata.service.IYearInfoService;
import com.hnjk.edu.learning.model.ActiveCourseExam;
import com.hnjk.edu.learning.model.BbsReply;
import com.hnjk.edu.learning.model.BbsSection;
import com.hnjk.edu.learning.model.BbsTopic;
import com.hnjk.edu.learning.model.CourseExam;
import com.hnjk.edu.learning.model.CourseLearningGuid;
import com.hnjk.edu.learning.model.CourseMockTest;
import com.hnjk.edu.learning.model.CourseNotice;
import com.hnjk.edu.learning.model.CourseOverview;
import com.hnjk.edu.learning.model.CourseReference;
import com.hnjk.edu.learning.model.Exercise;
import com.hnjk.edu.learning.model.ExerciseBatch;
import com.hnjk.edu.learning.model.MateResource;
import com.hnjk.edu.learning.model.StudentActiveCourseExam;
import com.hnjk.edu.learning.model.StudentExercise;
import com.hnjk.edu.learning.model.StudentLearnPlan;
import com.hnjk.edu.learning.model.StudentSyllabus;
import com.hnjk.edu.learning.model.StudyProgress;
import com.hnjk.edu.learning.service.IActiveCourseExamService;
import com.hnjk.edu.learning.service.IBbsReplyService;
import com.hnjk.edu.learning.service.IBbsSectionService;
import com.hnjk.edu.learning.service.IBbsTopicService;
import com.hnjk.edu.learning.service.ICourseLearningGuidService;
import com.hnjk.edu.learning.service.ICourseMockTestService;
import com.hnjk.edu.learning.service.ICourseNoticeService;
import com.hnjk.edu.learning.service.ICourseOverviewService;
import com.hnjk.edu.learning.service.ICourseReferenceService;
import com.hnjk.edu.learning.service.IExerciseBatchService;
import com.hnjk.edu.learning.service.IExerciseService;
import com.hnjk.edu.learning.service.ILearningJDBCService;
import com.hnjk.edu.learning.service.ILearningTimeSettingService;
import com.hnjk.edu.learning.service.IMateResourceService;
import com.hnjk.edu.learning.service.IStudentActiveCourseExamService;
import com.hnjk.edu.learning.service.IStudentExerciseService;
import com.hnjk.edu.learning.service.IStudentLearnPlanService;
import com.hnjk.edu.learning.service.IStudentSyllabusService;
import com.hnjk.edu.learning.service.IStudyProgressService;
import com.hnjk.edu.portal.model.MessageReceiverUser;
import com.hnjk.edu.portal.service.IMessageReceiverService;
import com.hnjk.edu.portal.service.IMessageReceiverUserService;
import com.hnjk.edu.portal.service.IMessageStatService;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.roll.service.IStudentInfoService;
import com.hnjk.edu.teaching.model.LearningTimeSetting;
import com.hnjk.edu.teaching.model.Syllabus;
import com.hnjk.edu.teaching.model.TeachingPlanCourse;
import com.hnjk.edu.teaching.model.TeachingPlanCourseStatus;
import com.hnjk.edu.teaching.model.UsualResults;
import com.hnjk.edu.teaching.service.ICourseService;
import com.hnjk.edu.teaching.service.ISyllabusService;
import com.hnjk.edu.teaching.service.ITeachingPlanCourseService;
import com.hnjk.edu.teaching.service.ITeachingPlanCourseStatusService;
import com.hnjk.edu.teaching.service.ITeachingPlanCourseTimetableService;
import com.hnjk.edu.teaching.service.IUsualResultsRuleService;
import com.hnjk.edu.teaching.service.IUsualResultsService;
import com.hnjk.edu.teaching.service.impl.SyllabusServiceImpl;
import com.hnjk.edu.util.Condition2SQLHelper;
import com.hnjk.platform.system.UserOperationLogsHelper;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.Attachs;
import com.hnjk.platform.system.model.UserOperationLogs;
import com.hnjk.platform.system.service.IAttachsService;
import com.hnjk.platform.taglib.JstlCustomFunction;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;
import com.hnjk.security.model.UserExtends;

/**
 * 学生课程学习互动中心.
 * <code>LearningInteractiveController</code><p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-8-18 下午05:18:58
 * @see 
 * @version 1.0
 */
@Controller
public class LearningInteractiveController extends FileUploadAndDownloadSupportController{
	
	private static final long serialVersionUID = -6293428582501202810L;	
	
	@Autowired
	@Qualifier("usualResultsService")
	private IUsualResultsService usualResultsService;
	
	@Autowired
	@Qualifier("exerciseBatchService")
	private IExerciseBatchService exerciseBatchService;
	
	@Autowired
	@Qualifier("studentExerciseService")
	private IStudentExerciseService studentExerciseService;	
	
	@Autowired
	@Qualifier("exerciseService")
	private IExerciseService exerciseService;
	
	@Autowired
	@Qualifier("learningJDBCService")
	private ILearningJDBCService learningJDBCService;
	
	@Autowired
	@Qualifier("messageReceiverUserService")
	private IMessageReceiverUserService messageReceiverUserService;
	
	@Autowired
	@Qualifier("teachingPlanCourseTimetableService")
	private ITeachingPlanCourseTimetableService tpctService;
	
	@Autowired
	@Qualifier("yearInfoService")
	private IYearInfoService yearInfoService;
	
	@Autowired
	@Qualifier("teachingPlanCourseService")
	private ITeachingPlanCourseService teachingPlanCourseService;
	
	@Autowired
	@Qualifier("teachingPlanCourseStatusService")
	private ITeachingPlanCourseStatusService teachingPlanCourseStatusService;
	
	@Autowired
	@Qualifier("studyProgressService")
	private IStudyProgressService studyProgressService;
	
	@Autowired
	@Qualifier("studentSyllabusService")
	private IStudentSyllabusService studentSyllabusService;
	
	/**
	 * 进入课程学习页面
	 * @param courseId
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/edu3/learning/interactive/main.html")
	public String enterInteractive(String planCourseId ,String courseId,String teachType,HttpServletRequest request, ModelMap model) throws Exception {
		if(ExStringUtils.isNotEmpty(courseId)){
			User user = SpringSecurityHelper.getCurrentUser();
			request.setAttribute("user",user);
			Course course = courseService.get(courseId);
			model.addAttribute("course", course);
			String studentId = "";
			if(user!=null){
				if(SpringSecurityHelper.isUserInRole("ROLE_STUDENT")){//如果为学生，输出学生角色
					model.addAttribute("currentrole","student");
				}
				Double score = 0d;
				if(null!=user.getUserExtends()&&null!=user.getPropertys(UserExtends.USER_EXTENDCODE_DEFAULTROLLID)){
					Map<String,Object> map = null;
					studentId = user.getPropertys(UserExtends.USER_EXTENDCODE_DEFAULTROLLID).getExValue();
					try {
						List<Map<String,Object>> list = learningJDBCService.toCalculate(studentId, courseId);
						if(null != list && list.size() > 0){
							map = list.get(0);
						}
						String d = (null != map && map.containsKey("result")) ? ((BigDecimal)map.get("result")).toString() : "0";
						score = Double.parseDouble(d==null?"0":d);
						if(score > 100) {
							score = 100d;
						}
					} catch (Exception e) {
						logger.debug("获取课程"+course.getCourseName()+"的随堂练习分数失败！");
						e.printStackTrace();
					}
					model.addAttribute("teachType",teachType);
				}
				
				model.addAttribute("result",score);
			}	
					
			//知识结构树
			List<Syllabus> syllabusList =  syllabusService.findSyllabusTreeList(course.getResourceid(),true);	
			if(syllabusList!=null && !syllabusList.isEmpty()){
				Map<String,String> map = new HashMap<String, String>(0);
				String jsonString = JsonUtils.objectToJson(SyllabusServiceImpl.getSyllabusTree(syllabusList,map,false));				
				model.addAttribute("syllabusTree", jsonString);
			}		
			
			//加载课程介绍
			List<CourseOverview> courseOverviews = courseOverviewService.findByHql("from "+CourseOverview.class.getName()+" where isDeleted=0 and course.resourceid=? and type!='8' order by type", course.getResourceid());
			model.addAttribute("courseOverviews", courseOverviews);
			//学习小组
			String groupSectionCode = CacheAppManager.getSysConfigurationByCode("web.bbs.sectioncode.group").getParamValue();
			BbsSection learningGroupSection = bbsSectionService.getBySectionCode(groupSectionCode);
			model.addAttribute("learningGroupSection", learningGroupSection);
			
			//主题讨论
			String discussSectionCode = CacheAppManager.getSysConfigurationByCode("web.bbs.sectioncode.discuss").getParamValue();
			BbsSection learningDiscussSection = bbsSectionService.getBySectionCode(discussSectionCode);
			model.addAttribute("learningDiscussSection", learningDiscussSection);
			
			//学习进度
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("studentId", studentId);
			condition.put("courseId", course.getResourceid());
			List<Map<String, Object>> learningInfoList = learningJDBCService.findLearningInfoStatistics(condition);
			if(learningInfoList!=null && learningInfoList.size()>0){
				model.addAttribute("learningInfo", learningInfoList.get(0));
			}
			
		}
		request.getSession(true).setMaxInactiveInterval(60*90);
		return "/edu3/learning/interactive/main";
	}
	
	
	/**
	 * 学习目标
	 * @param syllabusId
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/learning/interactive/courselearningguid/list.html")
	public String enterCourseLearningGuid(String syllabusId, ModelMap model) throws WebException {
		if(ExStringUtils.isNotEmpty(syllabusId)){
			List<CourseLearningGuid> courseLearningGuids = courseLearningGuidService.findByHql("from "+CourseLearningGuid.class.getName()+" where isDeleted=0 and syllabus.resourceid=? order by type ", syllabusId);
			model.addAttribute("courseLearningGuids", courseLearningGuids);
		}
		return "/edu3/learning/interactive/courselearningguid-view";		
	}
	
	/**
	 * 课程学习材料
	 * @param syllabusId
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/learning/interactive/materesource/list.html")
	public String enterMateResource(String syllabusId, String courseId,ModelMap model) throws WebException {
		if(ExStringUtils.isNotEmpty(syllabusId)){
			List<MateResource> mateResources = mateResourceService.findByHql("from "+MateResource.class.getName()+" where isDeleted=0 and syllabus.resourceid=? and isPublished=? order by syllabus.syllabusLevel,syllabus.showOrder,showOrder", new Object[]{syllabusId,Constants.BOOLEAN_YES});
			model.addAttribute("mateResources", mateResources);
			if(ExStringUtils.isEmpty(courseId)){
				Syllabus syllabus = syllabusService.get(syllabusId);
				if(syllabus!=null && syllabus.getCourse()!=null){
					courseId = syllabus.getCourse().getResourceid();
				}
			}
			model.addAttribute("courseId", courseId);
		}
		return "/edu3/learning/interactive/materesource-list";		
	}
	
	/**
	 * 新窗口打开材料链接
	 * @param mateType
	 * @param mateUrl
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/learning/interactive/materesource/view.html")
	public String viewMateResource(HttpServletRequest request, ModelMap model) throws WebException {
		String mateName =  ExStringUtils.trimToEmpty(request.getParameter("mateName"));
		if(ExStringUtils.isMessyCode(mateName)){
			mateName = ExStringUtils.getEncodeURIComponentByOne(mateName);
		}
		
		String mateType = ExStringUtils.trimToEmpty(request.getParameter("mateType"));
		String mateUrl = ExStringUtils.trimToEmpty(request.getParameter("mateUrl"));		
		String courseId = ExStringUtils.trimToEmpty(request.getParameter("courseId"));		
		String mateId = ExStringUtils.trimToEmpty(request.getParameter("mateId"));		
		String resourceUrl = ExStringUtils.trimToEmpty(CacheAppManager.getSysConfigurationByCode("resourceUrl").getParamValue());
		
		User user = SpringSecurityHelper.getCurrentUser();
		if(user!=null){
			StudentInfo studentInfo =studentInfoService.getStudentInfoByUser(user);
			if(studentInfo!=null){
				String hql = "select sp from StudyProgress sp where sp.isDeleted=0 and sp.studentInfo.resourceid=? and sp.course.resourceid=? and sp.mate.resourceid=?";
				List<StudyProgress> studyProgressList = studyProgressService.findByHql(hql, studentInfo.getResourceid(),courseId,mateId);
				if(studyProgressList.size()==1){
					model.addAttribute("studyProgress",studyProgressList.get(0));
				}
			}
		}
		
		model.addAttribute("mateName",mateName);
		model.addAttribute("mateType", mateType);
		model.addAttribute("mateUrl", mateUrl);
		model.addAttribute("resourceUrl", resourceUrl);
		model.addAttribute("courseId", courseId);
		model.addAttribute("mateId", mateId);
		request.getSession(true).setMaxInactiveInterval(60*90);
		return "/edu3/learning/interactive/materesource-view";
	}
	
	/**
	 * 查看课程介绍
	 * @param courseId
	 * @param type
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/learning/interactive/courseoverview/view.html")
	public String showCourseOverview(String overviewId,ModelMap model)throws WebException{
		if(ExStringUtils.isNotEmpty(overviewId)){
			CourseOverview courseOverview = courseOverviewService.get(overviewId);
			model.addAttribute("courseOverview", courseOverview);
		}		
		return "/edu3/learning/interactive/courseoverview-view";
	}
	/**
	 * 课程模拟试题
	 * @param courseId
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/learning/interactive/coursemocktest/view.html")
	public String showCourseMockTest(String courseId,ModelMap model)throws WebException{
		List<CourseMockTest> courseMockTests = courseMockTestService.findByHql("from "+CourseMockTest.class.getName()+" where isDeleted=0 and course.resourceid=? order by resourceid ", courseId);
		model.addAttribute("courseMockTests", courseMockTests);
		return "/edu3/learning/interactive/coursemocktest-list";
	}
	
	/**
	 * 课程公告
	 * @param courseId
	 * @param request
	 * @param model
	 * @return
	 * @throws WebException
	 */	
	@RequestMapping("/edu3/learning/interactive/coursenotice/list.html")
	public String listCourseNotice(String courseId,String yearInfoId,String term,Page objPage, ModelMap model) throws WebException{		
		objPage.setOrderBy("fillinDate desc");
		objPage.setOrder(Page.DESC);//设置默认排序方式
		
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件
		if(ExStringUtils.isNotEmpty(courseId)){
			condition.put("courseId", courseId);
		}
		/*if(ExStringUtils.isBlank(yearInfoId)||ExStringUtils.isBlank(term)){
			Grade grade = gradeService.getDefaultGrade();
			if(grade!=null){
				if(ExStringUtils.isBlank(yearInfoId)){
					yearInfoId = grade.getYearInfo().getResourceid();
				}	
				if(ExStringUtils.isBlank(term)){
					term = grade.getTerm();
				}	
			}
		}*/
		if(ExStringUtils.isNotEmpty(yearInfoId)){
			condition.put("yearInfoId", yearInfoId);
		}	
		if(ExStringUtils.isNotEmpty(term)){
			condition.put("term", term);
		}	
		
		Page courseNoticePage = courseNoticeService.findCourseNoticeByCondition(condition, objPage);
		
		model.addAttribute("courseNoticePage", courseNoticePage);
		model.addAttribute("condition", condition);
		return "/edu3/learning/interactive/coursenotice-list";
	}
	
	/**
	 * 查看课程公告
	 * @param courseNoticeId
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/framework/coursenotice/view.html")
	public String showCourseNotice(String courseNoticeId,ModelMap model,HttpServletResponse response)throws WebException{
		if(ExStringUtils.isNotEmpty(courseNoticeId)){
			CourseNotice courseNotice = courseNoticeService.get(courseNoticeId);
			List<Attachs> attachs = attachsService.findAttachsByFormId(courseNoticeId);
			courseNotice.setAttachs(attachs);
			model.addAttribute("courseNotice", courseNotice);
			//renderText(response, courseNotice.getNoticeContent());
		}	
		return "/edu3/learning/interactive/coursenotice-view";
	}
	/**
	 * 课程参考资料
	 * @param courseId
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/learning/interactive/coursereference/list.html")
	public String listCourseReference(String syllabusId,String courseId,String type, ModelMap model) throws WebException{	
		List<CourseReference> courseReferences = null;
		if(ExStringUtils.isNotEmpty(syllabusId)){//知识点的参考资料
			courseReferences = courseReferenceService.findByHql(" from "+CourseReference.class.getSimpleName()+" where isDeleted=0 and syllabus.resourceid = ? order by referenceType,referenceName ", syllabusId);
			model.addAttribute("courseReferences",courseReferences);
			
			Syllabus syllabus = syllabusService.get(syllabusId);			
			model.addAttribute("title", syllabus.getFullNodeName());
		} else {
			if(ExStringUtils.isNotEmpty(courseId)){//课程的参考资料
				Syllabus syllabus = syllabusService.getSyllabusRoot(courseId);
				if(null!=syllabus){
					if(ExStringUtils.isNotEmpty(type)){//特色栏目
						courseReferences = courseReferenceService.findByHql(" from "+CourseReference.class.getSimpleName()+" where isDeleted=0 and course.resourceid = ? and syllabus is null order by referenceType,referenceName ", courseId);
					} else {//课程的参考资料
						courseReferences = courseReferenceService.findByHql(" from "+CourseReference.class.getSimpleName()+" where isDeleted=0 and course.resourceid = ? and syllabus.resourceid=? order by referenceType,referenceName ", courseId,syllabus.getResourceid());
					}
					model.addAttribute("courseReferences",courseReferences);
				}								
			}	
		}
		if(null!=courseReferences && courseReferences.size()>0){
			Set<String> referenceTypes = new HashSet<String>(0);
			for (CourseReference r : courseReferences) {
				referenceTypes.add(r.getReferenceType());
			}
			model.addAttribute("referenceTypes",referenceTypes);
		}
		return "/edu3/learning/interactive/coursereference-list";
	}	

	/**
	 * 进入随堂练习
	 * @param syllabusId
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/learning/interactive/activecourseexam/list.html")
	public String listActiveCourseExam(String syllabusId,String scopeType,String isAutoSave,HttpServletRequest request,Page objPage, ModelMap model) throws WebException {
		objPage.setOrder(Page.ASC);
		objPage.setOrderBy("showOrder,resourceid");
		objPage.setPageSize(10);
		scopeType = ExStringUtils.defaultIfEmpty(ExStringUtils.trimToEmpty(scopeType), "all");
		String teachType =  ExStringUtils.trimToEmpty(request.getParameter("teachType"));
		Map<String,Object> condition = new HashMap<String,Object>();
		condition.put("isAutoSave", ExStringUtils.defaultIfEmpty(ExStringUtils.trimToEmpty(isAutoSave), "N"));
		String studentInfoId = "";
		String hasChance = "Y";
		
		if(ExStringUtils.isNotEmpty(syllabusId)){	
			Syllabus syllabus = syllabusService.get(syllabusId);
			model.addAttribute("syllabus", syllabus);
			if(SpringSecurityHelper.isUserInRole(CacheAppManager.getSysConfigurationByCode("sysuser.role.student").getParamValue())){//学生	
				User user = SpringSecurityHelper.getCurrentUser();
				if(null!=user.getUserExtends()&&null!=user.getPropertys(UserExtends.USER_EXTENDCODE_DEFAULTROLLID)){
					studentInfoId = user.getPropertys(UserExtends.USER_EXTENDCODE_DEFAULTROLLID).getExValue();
				}
			}
			if(ExStringUtils.isNotBlank(studentInfoId)){
				condition.put("scopeType", scopeType);
				condition.put("studentInfoId", studentInfoId);
			}
						
			condition.put("syllabusId", syllabusId);
			condition.put("isPublished", Constants.BOOLEAN_YES);
			condition.put("teachType", teachType);
			Page activeCourseExams = activeCourseExamService.findActiveCourseExamByCondition(condition, objPage);
			//List<ActiveCourseExam> activeCourseExams = activeCourseExamService.findByHql("from "+ActiveCourseExam.class.getName()+" where isDeleted=0 and syllabus.resourceid=? and isPublished=? order by showOrder ", syllabusId,Constants.BOOLEAN_YES);
			
			// 增加逻辑功能：允许重做N次，以章节为单位  20170731 by lion			
			if(ExStringUtils.isNotBlank(studentInfoId)){//学生	
				int redoTimes =0; 
				StudentSyllabus ss;
				try {
					Map<String,Object> values1 = new HashMap<String,Object>();
					values1.put("studentInfoId", studentInfoId);
					values1.put("syllabusId", syllabusId);
					List<StudentSyllabus> ssList = studentSyllabusService.findListByCondition(values1);
					Map<String ,Object> values2 = new HashMap<String, Object>();
					values2.put("syllabusId", syllabusId);
					long topicCount=activeCourseExamService.getSyllabusCount(values2);
					String strRedoTimes=CacheAppManager.getSysConfigurationByCode("practice_on_the_class_redo_time").getParamValue();
					if(ssList.size()>=1){//更新
						ss = ssList.get(0);
						redoTimes = ss.getRedotimes();
						if(ss.getExercisesum()!=(int)topicCount){
							ss.setExercisesum((int)topicCount);
							studentSyllabusService.saveOrUpdate(ss);
						}
					}else{//新增记录				
						ss = new StudentSyllabus();
						StudentInfo stuInfo = studentInfoService.load(studentInfoId);
						Syllabus syllabus1 = syllabusService.load(syllabusId);
						ss.setStuInfo(stuInfo);
						ss.setSyllabus(syllabus1);
						ss.setExercisesum((int) topicCount);
						ss.setRedotimes(Integer.parseInt(strRedoTimes));
						User user = SpringSecurityHelper.getCurrentUser();
						ss.setUpdateMan(user);
						ss.setUpdateDate(new Date());
						studentSyllabusService.saveOrUpdate(ss);
						redoTimes = ss.getRedotimes();					
					}
					model.addAttribute("sumRedoTimes", strRedoTimes);//全局参数：重做次数
					model.addAttribute("studentSyllabus", ss);
				} catch (NumberFormatException e) {
					logger.error("随堂练习：转换数据类型出错!");
					e.printStackTrace();
				} catch (ServiceException e) {
					logger.error("随堂练习：baseSupportJdbcDao.getBaseJdbcTemplate().findForLong() SQL语法或参数出错");
					e.printStackTrace();
				} catch (Exception e) {
					logger.error("随堂练习重新提交三次出现逻辑错误：");
					e.printStackTrace();
				}
				model.addAttribute("redoTimes", redoTimes);//当前允许重做的次数
				model.addAttribute("isStudent", "Y");
				
				Map<String,Object> results = new HashMap<String,Object>();
				Map<String, StudentActiveCourseExam> answers = new HashMap<String, StudentActiveCourseExam>();
				if(ExCollectionUtils.isNotEmpty(activeCourseExams.getResult())){
					List<String> ids = new ArrayList<String>();
					int y1 = 0;
					for (Object obj : activeCourseExams.getResult()) {
						ActiveCourseExam exam = (ActiveCourseExam)obj;
						ids.add(exam.getResourceid());
						if(!"6".equals(exam.getCourseExam().getExamType())){
							y1++;
						}
					}
					Map<String,Object> condition1 = new HashMap<String,Object>();
					condition1.put("syllabusId", syllabusId);
					condition1.put("studentInfoId", studentInfoId);
					condition1.put("courseExamIds", ids);
					String hql = "from "+StudentActiveCourseExam.class.getSimpleName()+" s where s.isDeleted=0 and s.activeCourseExam.syllabus.resourceid=:syllabusId and s.studentInfo.resourceid=:studentInfoId  and s.activeCourseExam.resourceid in(:courseExamIds) ";
					List<StudentActiveCourseExam> stuanswers = studentActiveCourseExamService.findByHql(hql, condition1);
					
					int currentFinishedCount = 0;//本页提交题目数
					int currentCorrectCount = 0;
					for (StudentActiveCourseExam studentActiveCourseExam : stuanswers) {		
						answers.put(studentActiveCourseExam.getActiveCourseExam().getResourceid(), studentActiveCourseExam);		
						if(studentActiveCourseExam.getResult()!=null){
							currentFinishedCount++;
							if(Constants.BOOLEAN_YES.equals(studentActiveCourseExam.getIsCorrect())){//答题正确
								currentCorrectCount++;
							}
						}
					}
					//results.put("y1", activeCourseExams.getResult().size());//当前页题目数
					results.put("y1", y1);//当前页题目数
					results.put("y2", stuanswers.size());//当前页已做题目数
					results.put("y3", currentFinishedCount);//当前页已提交题目数
					results.put("y4", currentCorrectCount);//当前页答对题目数
					
					//随堂练习开放状态:-1=截止,0=未开放,1=开放
					int studyStatus = getLeaningOpenStatus(studentInfoId, syllabus.getCourse().getResourceid());					
					if(studyStatus==-1 || studyStatus ==0){
						model.addAttribute("msg", studyStatus==-1?"随堂练习已经截止":"随堂练习还未开放");
					}				
					model.addAttribute("studyStatus", studyStatus);
					model.addAttribute("isActiveCourseExamOpen", studyStatus==1?Constants.BOOLEAN_YES:Constants.BOOLEAN_NO);
				}
				
				Long allCount = studentActiveCourseExamService.finishedActiveCourseExam(syllabusId, studentInfoId,"all");//总的随堂练习
				Long allSaveCount = studentActiveCourseExamService.finishedActiveCourseExam(syllabusId, studentInfoId,"done");//已做的随堂练习
				Long allFinishedCount = studentActiveCourseExamService.finishedActiveCourseExam(syllabusId, studentInfoId,"finished");//已提交的随堂练习
				Long allCorrectCount = studentActiveCourseExamService.finishedActiveCourseExam(syllabusId, studentInfoId,"correct");
				results.put("x1", allCount);//总题目数
				results.put("x2", allSaveCount);//总已做题目数
				results.put("x3", allFinishedCount);//总已提交题目数
				results.put("x4", allCorrectCount);//总答对题目数
				
				model.addAttribute("answers", answers);//已答答案
				model.addAttribute("results", results);
				if(redoTimes == 0 || allCount.equals(allCorrectCount)){
					hasChance = "N";
				}
				model.addAttribute("hasChance", hasChance);
			}			
			
			model.addAttribute("activeCourseExams", activeCourseExams);			
			model.addAttribute("title", syllabus.getFullNodeName());
		}
		model.addAttribute("teachType", teachType);
		model.addAttribute("condition", condition);
		return "/edu3/learning/interactive/activecourseexam";
	}
	/**
	 * 随堂练习重做
	 * @param studentSyllabusid
	 * @param request
	 * @param response
	 * @param model
	 * @throws Exception
	 */
	@RequestMapping("/edu/learning/interactive/activecourseexam/currentSyllabusRedo.html")
	public void currentSyllabusRedo(String studentSyllabusid,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws Exception{
		String syllabusid = ExStringUtils.trimToEmpty(request.getParameter("syllabusid"));
		String redotimes = ExStringUtils.trimToEmpty(request.getParameter("redotimes"));
		StudentSyllabus ss = studentSyllabusService.get(studentSyllabusid);
		int tmpredotimes = Integer.parseInt(redotimes);
		if(tmpredotimes!=0){
			tmpredotimes--;
		}
		ss.setRedotimes(tmpredotimes);
		ss.setUpdateDate(new Date());
		User user = SpringSecurityHelper.getCurrentUser();
		ss.setUpdateMan(user);		
		studentSyllabusService.saveOrUpdate(ss);
		String hql = " from "+StudentActiveCourseExam.class.getSimpleName()+" where studentInfo.resourceid=:studentInfoId and isDeleted=0 and activeCourseExam.syllabus.resourceid=:syllabusId ";
		Map<String ,Object> values = new HashMap<String, Object>();
		values.put("studentInfoId", ss.getStuInfo().getResourceid());
		values.put("syllabusId", syllabusid);
		List<StudentActiveCourseExam> sacList=studentActiveCourseExamService.findByHql(hql, values);		
		if(sacList.size()>0){
			for(StudentActiveCourseExam sac:sacList){
				sac.setIsDeleted(1);
				sac.setResult(null);
//				sac.setAnswer(null);
			}
			studentActiveCourseExamService.batchSaveOrUpdate(sacList);
		}
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("statusCode", 200);
		map.put("message", "设置成功");
		UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "13", UserOperationLogs.UPDATE,"学生PC端设置随堂练习重做：结果："+map.get("message+")+"。参数："+JsonUtils.mapToJson(Condition2SQLHelper.getConditionFromResquestByIterator(request)));
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 放弃重做的机会
	 * @param studentSyllabusid
	 * @param request
	 * @param response
	 * @param model
	 * @throws Exception
	 */
	@RequestMapping("/edu/learning/interactive/activecourseexam/giveUpRedoTimes.html")
	public void giveUpRedoTimes(String studentSyllabusid,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws Exception{
		StudentSyllabus ss = studentSyllabusService.get(studentSyllabusid);
		ss.setRedotimes(0);
		ss.setUpdateDate(new Date());
		studentSyllabusService.saveOrUpdate(ss);
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("statusCode", 200);
		map.put("message", "设置成功");
		UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "13", UserOperationLogs.UPDATE,"学生PC端放弃随堂重做机会：结果："+map.get("message")+"。参数："+JsonUtils.mapToJson(Condition2SQLHelper.getConditionFromResquestByIterator(request)));
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 学习开放状态
	 * @param studentInfoId
	 * @param courseId
	 * @return 学习状态值:-1=截止,0=未开放,1=开放
	 */
	private int getLeaningOpenStatus(String studentInfoId,String courseId) {
		int status = -1;
		try {
//			StudentLearnPlan plan = studentLearnPlanService.getStudentLearnPlanByCourse(courseId,studentInfoId, "studentId");
//			if(plan != null){
//				//2013-01-10 1.没有平时分的学生，能否做随堂练习，只受“随堂练习开放时间”影响。	2.已经有平时分的学生，不能做随堂练习，但在预约考试时可以选定“重新累积平时分”，则将已有的平时分清除。清除后则按规则1处理。
//				//考试完毕 或 预约考试后选择沿用平时分
//				if(plan.getStatus()==3 || plan.getStatus()<3 && Constants.BOOLEAN_NO.equals(plan.getIsRedoCourseExam())){//考试结束
//					status = -1;
//				} else {
					String ScrrentTerm = CacheAppManager.getSysConfigurationByCode("sysCurrentGrade").getParamValue(); //当前学期
					if(ScrrentTerm!=null){
						String[] ARRAYterm = ScrrentTerm.split("\\.");
						LearningTimeSetting setting = learningTimeSettingService.findLearningTimeSetting(0,ARRAYterm[0],ARRAYterm[1]);
						if(setting!=null){
							Date today = new Date();
							if(today.before(setting.getStartTime())){//学习还未开始
								status = 0;
							} else if(today.after(setting.getEndTime())){
								status = -1;
							} else {
								status = 1;
							}
						} 
					}								
//				}	
//			}	
		} catch (Exception e) {
			logger.error("获取网上学习开放状态出错:{}", e.fillInStackTrace());
			status = -1;
		}
		return status;
	}
	
	/**
	 * 保存学生随堂练习回答情况
	 * @param syllabusId
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/learning/interactive/studentactivecourseexam/save.html")
	public void saveStudentActiveCourseExam(String syllabusId,HttpServletRequest request,HttpServletResponse response) throws WebException {
		Map<String,Object> map = new HashMap<String, Object>();	
		map.put("statusCode", 200);//默认值
		try {			
			do{
				String[] activeCourseExamId = request.getParameterValues("activeCourseExamId"); 
				String type = request.getParameter("type");
				List<StudentActiveCourseExam> list = new ArrayList<StudentActiveCourseExam>();//答题情况列表
				/*Grade grade = gradeService.getDefaultGrade();
				if(grade == null){
					map.put("statusCode", 300);
					map.put("message", "请联系教务员设置默认年级！");
					continue;
				}*/
				/*String yearTerm = CacheAppManager.getSysConfigurationByCode("sysCurrentGrade").getParamValue();
				String _yearInfoId = "";
				String _year = "";
				String _term = "";
				if (null!=yearTerm) {
					String[] ARRYyterm = yearTerm.split("\\.");
					_year = ARRYyterm[0];
					_yearInfoId = yearInfoService.getByFirstYear(Long.parseLong(ARRYyterm[0])).getResourceid();
					_term = ARRYyterm[1];
				}else{
					map.put("statusCode", 300);
					map.put("message", "请联系教务员设置当前学期全局参数！");
					continue;
				}*/
				
				int num = 0;//答对题目数
				User user = SpringSecurityHelper.getCurrentUser();
				String roleCode = CacheAppManager.getSysConfigurationByCode("sysuser.role.student").getParamValue();
				Course c = null;
				if(SpringSecurityHelper.isUserInRole(roleCode)){//学生	
					StudentInfo studentInfo = studentInfoService.getStudentInfoByUser(user);
					
					if(activeCourseExamId != null && activeCourseExamId.length > 0){	
						String year = "";
						String term = "";
						String yearInfoId = "";
						for(int index=0;index<activeCourseExamId.length;index++){ //处理每道练习题	
							String[] answers = request.getParameterValues("answer"+activeCourseExamId[index]);
							// 将答案排序
							String stuAnswer = null;
							if(answers!=null){
								Arrays.sort(answers);
								stuAnswer = ExStringUtils.trimToEmpty(ExStringUtils.join(answers, ""));
							}
							
							if(!"submit".equals(type) && ExStringUtils.isBlank(stuAnswer)){//保存时略过未作答的题目							
								continue;
							}
							ActiveCourseExam activeCourseExam = activeCourseExamService.get(activeCourseExamId[index]);
							StudentActiveCourseExam stuActiveCourseExam = new StudentActiveCourseExam();
							if(null == c){
								c = activeCourseExam.getSyllabus().getCourse();
							}
							//使用开课的学期判断排课
							TeachingPlanCourse teachingPlanCourse = studentLearnPlanService.getStudentLearnPlanByCourse(c.getResourceid(), studentInfo.getResourceid(),"studentId").getTeachingPlanCourse();
							TeachingPlanCourseStatus teachingPlanCourseStatus = teachingPlanCourseStatusService
									.findOneByCondition(studentInfo.getGrade()
											.getResourceid(), studentInfo.getTeachingPlan().getResourceid(),
											teachingPlanCourse.getResourceid(), studentInfo.getBranchSchool().getResourceid());
							if(teachingPlanCourseStatus==null){
								map.put("statusCode", 300);
								map.put("message", "对不起，该门课程还没有开课！");
								break;
							}
							String yearTermStr = teachingPlanCourseStatus.getTerm();
							
							if (null!=yearTermStr) {
								year = yearTermStr.substring(0, 4);
								term = yearTermStr.substring(6, 7);
							}
							YearInfo yearInfo = yearInfoService.getByFirstYear(Long.parseLong(year));
							yearInfoId = yearInfo.getResourceid();
							if(!tpctService.isArrangeCourse(c.getResourceid(),year+"_0"+term/* grade.getYearInfo().getFirstYear()+"_0"+grade.getTerm()*/)){
								map.put("statusCode", 300);
								map.put("message", "对不起，该门课程还没有排课！");
								break;
							}
							//这里判断是否在网上学习时间内操作
							LearningTimeSetting learningTimeSetting = learningTimeSettingService.findLearningTimeSetting(yearInfoId, term);
							Date now = new Date();
							if(learningTimeSetting == null || now.before(learningTimeSetting.getStartTime()) || now.after(learningTimeSetting.getEndTime())){
								map.put("statusCode", 300);
								map.put("message", "当前时间不在网上学习时间内或者没有设置");
								break;
							}
							List<StudentActiveCourseExam> stuExams = studentActiveCourseExamService.findByCriteria(Restrictions.eq("isDeleted",0),Restrictions.eq("studentInfo.resourceid",studentInfo.getResourceid()),Restrictions.eq("activeCourseExam.resourceid",activeCourseExamId[index]));
							if(stuExams!=null && !stuExams.isEmpty()){
								stuActiveCourseExam = stuExams.get(0);
								if(stuActiveCourseExam.getResult()!=null){//不修改已提交的
									continue;
								}
							}
							
							stuActiveCourseExam.setAnswer(stuAnswer);
							
							stuActiveCourseExam.setActiveCourseExam(activeCourseExam);
							stuActiveCourseExam.setStudentInfo(studentInfo);
							stuActiveCourseExam.setShowOrder(activeCourseExam.getShowOrder());	
							stuActiveCourseExam.setAnswerTime(new Date());
							
							if("submit".equals(type)){ //提交
								String stu_answer = ExStringUtils.trimToEmpty(stuActiveCourseExam.getAnswer());
								String answer = ExStringUtils.trimToEmpty(activeCourseExam.getCourseExam().getAnswer());
								if("3".equals(activeCourseExam.getCourseExam().getExamType())){//判断题
									stu_answer = CourseExam.covertToCorrectAnswer(stu_answer);
									answer = CourseExam.covertToCorrectAnswer(answer);
								} 
								if(stu_answer.equalsIgnoreCase(answer)){
									stuActiveCourseExam.setIsCorrect(Constants.BOOLEAN_YES);							
								} else {
									stuActiveCourseExam.setIsCorrect(Constants.BOOLEAN_NO);	
								}
								if(Constants.BOOLEAN_YES.equals(stuActiveCourseExam.getIsCorrect())){
									Long exam_score = activeCourseExam.getScore();
									stuActiveCourseExam.setResult((exam_score!=null)?exam_score.doubleValue():0.0);	
									num ++;
								} else {
									stuActiveCourseExam.setResult(0.0);
								}
							}	
							list.add(stuActiveCourseExam);						
						}
						if(((Integer)map.get("statusCode"))!=300){
							studentActiveCourseExamService.saveAllStudentActiveCourseExam(list,studentInfo, syllabusId, type);
							map.put("statusCode", 200);
							
							if("submit".equals(type)){ //提交
								map.put("message", "本次测试答对"+num+"道题,成绩已累积。");
								usualResultsService.saveSpecificUsualResults(studentInfo, yearInfoId, term, c.getResourceid());
							} else if("save".equals(type)){						
								map.put("message", "成功保存答案。");
							} else if("submit_done".equals(type)){	
								map.put("message", "成功提交已做的题目。");
								usualResultsService.saveSpecificUsualResults(studentInfo, yearInfoId, term, c.getResourceid());
							} else {
								map.put("message", "成功提交本练习所有题目。");
								usualResultsService.saveSpecificUsualResults(studentInfo, yearInfoId, term, c.getResourceid());
							}
						}
					}
				} else {
					map.put("statusCode", 300);
					map.put("message", "您不是学生，不能提交！");
				}
			} while(false);
		} catch (ServiceException e) {
//			logger.error("保存学生随堂练习答题情况出错：",e);
			map.put("statusCode", 300);
			map.put("message", e.getMessage());
		} catch (Exception e) {
			logger.error("保存学生随堂练习答题情况出错：",e);
			map.put("statusCode", 300);
			map.put("message", "网络出现异常，请尝试重新提交！如果还出现异常，可进行问题反馈");
		}
		UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "13", UserOperationLogs.UPDATE,"学生保存/提交随堂练习：结果："+map.get("message")+"。参数："+JsonUtils.mapToJson(Condition2SQLHelper.getConditionFromResquestByIterator(request)));
		renderJson(response, JsonUtils.mapToJson(map));	
	}
	/**
	 * 查看答题情况
	 * @param syllabusId
	 * @param request
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/learning/interactive/studentactivecourseexam/view.html")
	public String viewActiveCourseExam(String syllabusId,HttpServletRequest request, ModelMap model) throws WebException {
		Map<String,Object> condition = new HashMap<String,Object>();
		String studentInfoId = "";
		if(ExStringUtils.isNotEmpty(syllabusId)){	
			if(SpringSecurityHelper.isUserInRole(CacheAppManager.getSysConfigurationByCode("sysuser.role.student").getParamValue())){//学生	
				User user = SpringSecurityHelper.getCurrentUser();
				if(null!=user.getUserExtends()&&null!=user.getPropertys(UserExtends.USER_EXTENDCODE_DEFAULTROLLID)){
					studentInfoId = user.getPropertys(UserExtends.USER_EXTENDCODE_DEFAULTROLLID).getExValue();
					if(ExStringUtils.isNotBlank(studentInfoId)){//学生	
						condition.put("syllabusId", syllabusId);
						condition.put("isPublished", Constants.BOOLEAN_YES);
						List<ActiveCourseExam> activeCourseExams = activeCourseExamService.findByHql("from "+ActiveCourseExam.class.getName()+" where isDeleted=0 and syllabus.resourceid=? and isPublished=? and courseExam.examType!='6' order by showOrder,resourceid ", syllabusId,Constants.BOOLEAN_YES);
						
						if(ExCollectionUtils.isNotEmpty(activeCourseExams)){							
							Map<String,Object> condition1 = new HashMap<String,Object>();
							condition1.put("syllabusId", syllabusId);
							condition1.put("studentInfoId", studentInfoId);
							condition1.put("isDeleted", 0);
							condition1.put("isPublished", Constants.BOOLEAN_YES);
							String hql = "from "+StudentActiveCourseExam.class.getSimpleName()+" s where s.isDeleted=:isDeleted and s.activeCourseExam.syllabus.resourceid=:syllabusId and s.studentInfo.resourceid=:studentInfoId  ";
							hql += " and s.activeCourseExam.isPublished=:isPublished and s.activeCourseExam.isDeleted=:isDeleted ";
							List<StudentActiveCourseExam> stuanswers = studentActiveCourseExamService.findByHql(hql, condition1);
							
							Map<String, StudentActiveCourseExam> answers = new HashMap<String, StudentActiveCourseExam>();
							for (StudentActiveCourseExam studentActiveCourseExam : stuanswers) {
								answers.put(studentActiveCourseExam.getActiveCourseExam().getResourceid(), studentActiveCourseExam);
							}
							model.addAttribute("activeCourseExams", activeCourseExams);
							model.addAttribute("answers", answers);
							
							int totalCols = activeCourseExams.size() / 10;
							if (activeCourseExams.size() % 10 > 0) {
								totalCols++;
							}
							model.addAttribute("totalCols", totalCols);
							
							model.addAttribute("oldCorrectCount", studentActiveCourseExamService.finishedActiveCourseExam(syllabusId, studentInfoId, "old"));
						}						
					}	
				}
			}
		}			
		return "/edu3/learning/interactive/activecourseexam-view";
	}
	
	/**
	 * 进入课程作业
	 * @param courseId
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/learning/interactive/exercisebatch/list.html")
	public String listExerciseBatch(String courseId,String tCourseId, ModelMap model, Page objPage) throws WebException {
		objPage.setOrderBy("startDate,resourceid");
		objPage.setOrder(Page.ASC);//设置默认排序方式	
		Map<String,Object> condition = new HashMap<String,Object>();
		condition.put("courseId", courseId);
		condition.put("notstatus", 0);			
		
		User user = SpringSecurityHelper.getCurrentUser();
		if(SpringSecurityHelper.isUserInRole(CacheAppManager.getSysConfigurationByCode("sysuser.role.student").getParamValue())){//学生	
			String studentId = "";
			/*if(null!=user.getUserExtends()&&null!=user.getPropertys(UserExtends.USER_EXTENDCODE_DEFAULTROLLID)){
				studentId = user.getPropertys(UserExtends.USER_EXTENDCODE_DEFAULTROLLID).getExValue();
			}*/
			StudentInfo studentInfo = studentInfoService.getStudentInfoByUser(user);
			if(studentInfo != null){
				studentId = studentInfo.getResourceid();
				condition.put("studentClassesId", studentInfo.getClasses().getResourceid());
				StudentLearnPlan plan = studentLearnPlanService.getStudentLearnPlanByCourse(courseId,studentId, "studentId");
				if(plan != null){
//				if(plan.getStatus()==3 && !Constants.BOOLEAN_YES.equals(plan.getIsRedoCourseExam())){//完成学习
//					condition.put("yearInfoId", plan.getYearInfo().getResourceid());		
//					condition.put("term", plan.getTerm());	
//				} else {
//					Grade grade = gradeService.getDefaultGrade();
//					if(grade!=null){
//						condition.put("yearInfoId", grade.getYearInfo().getResourceid());		
//						condition.put("term", grade.getTerm());	
//					}
//				}
					/*Grade grade = gradeService.getDefaultGrade();
					if(grade!=null){
						condition.put("yearInfoId", grade.getYearInfo().getResourceid());		
						condition.put("term", grade.getTerm());	
					}*/
					/*String yearTerm = CacheAppManager.getSysConfigurationByCode("sysCurrentGrade").getParamValue();
					if (null!=yearTerm) {
						String[] ARRYyterm = yearTerm.split("\\.");				
						condition.put("yearInfoId", yearInfoService.getByFirstYear(Long.parseLong(ARRYyterm[0])).getResourceid());		
						condition.put("term", ARRYyterm[1]);
					}*/
					//获取的是开课学期的作业
					try {
						//List<TeachingPlanCourseStatus>  tpcss = teachingPlanCourseStatusService.findByCondition(studentInfo.getTeachingGuidePlan().getResourceid(), plan.getTeachingPlanCourse().getResourceid(), studentInfo.getBranchSchool().getResourceid(), "Y");
						TeachingPlanCourseStatus tpcs = teachingPlanCourseStatusService.findOneByCondition(studentInfo.getGrade().getResourceid(),studentInfo.getTeachingPlan().getResourceid(), plan.getTeachingPlanCourse().getResourceid(), studentInfo.getBranchSchool().getResourceid());
						
						if(tpcs!=null){
							String yearTerm = tpcs.getTerm();
							String year = yearTerm.substring(0, 4);// 截取年度
							String term = yearTerm.substring(6, 7);// 截取学期
							
							condition.put("yearInfoId", yearInfoService.getByFirstYear(Long.parseLong(year)).getResourceid());		
							condition.put("term", term);
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logger.error("listExerciseBatch 获取开课出错",e.fillInStackTrace());
					}
				}
			}
		}		
		
		Page exerciseBatchs = exerciseBatchService.findExerciseBatchByCondition(condition, objPage);
		List LISTnow = exerciseBatchs.getResult();
		if(ExCollectionUtils.isNotEmpty(LISTnow)){
			List<ExerciseBatch> LISTbatch = new ArrayList<ExerciseBatch>();
			List<String> ids = new ArrayList<String>();
			for (Object batch : LISTnow) {			
				ExerciseBatch exerciseBatch = (ExerciseBatch)batch;
				ids.add(exerciseBatch.getResourceid());
//				if(SpringSecurityHelper.isUserInRole(CacheAppManager.getSysConfigurationByCode("sysuser.role.student").getParamValue())){//学生	
//					//看这个学生是否这个班级的学生
//					String Sclassesids = exerciseBatch.getClassesIds();
//					if(ExStringUtils.isNotBlank(Sclassesids)){
//						List<StudentInfo> LISTstu = studentInfoService.findByHql(" from "+StudentInfo.class.getSimpleName()+" stu where stu.isDeleted = 0 and stu.sysUser.resourceid = ? ", user.getResourceid());
//						StudentInfo Ostu = null != LISTstu && LISTstu.size() > 0 ? LISTstu.get(0) : null;
//						if(null != Ostu && null  != Ostu.getClasses()){
//							String[] Sids = Sclassesids.split(",");
//							for(String id : Sids){
//								if((id.trim().equals(Ostu.getClasses().getResourceid()))){
									LISTbatch.add(exerciseBatch);
//									break;
//								}
//							}
//						}
//					}
//				}
				
			}
			exerciseBatchs.setResult(LISTbatch);
			if(SpringSecurityHelper.isUserInRole(CacheAppManager.getSysConfigurationByCode("sysuser.role.student").getParamValue())){//学生	
				if(null!=user.getUserExtends()&&null!=user.getPropertys(UserExtends.USER_EXTENDCODE_DEFAULTROLLID)){
					String studentInfoId = user.getPropertys(UserExtends.USER_EXTENDCODE_DEFAULTROLLID).getExValue();
					if(ExStringUtils.isNotEmpty(studentInfoId)){
						Map<String, StudentExercise> stuExercises = new HashMap<String, StudentExercise>();
						Map<String,Object> values = new HashMap<String,Object>();
						values.put("studentInfoId", studentInfoId);
						values.put("exerciseResult", Constants.BOOLEAN_YES);
						values.put("exerciseBatchIds", ids);
						//Double avg = 0.0;
						List<StudentExercise> list = studentExerciseService.findStudentExerciseByCondition(values);
						for (StudentExercise studentExercise : list) {
							stuExercises.put(studentExercise.getExerciseBatch().getResourceid(), studentExercise);
							//if(studentExercise.getStatus()!=null && studentExercise.getStatus()==2){
							//	avg += studentExercise.getResult();
							//}
						}
						model.addAttribute("stuExercises", stuExercises);
						//model.addAttribute("stuExercisesAvg", avg/exerciseBatchs.getResult().size());
						String yearInfoId = (condition.containsKey("yearInfoId") && condition.get("yearInfoId") != null)?condition.get("yearInfoId").toString():"";
						String term = (condition.containsKey("term") && condition.get("term") != null)?condition.get("term").toString():"";
						//model.addAttribute("stuExercisesAvg", studentExerciseService.avgStudentExerciseResult(courseId, studentInfoId,yearInfoId,term));
						//暂时设置分数为0分
						model.addAttribute("stuExercisesAvg","0.0");
					}						
				}					
			}			
		}			
		model.addAttribute("exerciseBatchs", exerciseBatchs);
		model.addAttribute("condition", condition);
		return "/edu3/learning/interactive/studentexercisebatch-list";
	}

	/**
	 * 课程作业
	 * @param exerciseBatchId
	 * @param model
	 * @return
	 * @throws WebException
	 */	
	@RequestMapping("/edu3/learning/interactive/exercise/list.html")
	public String listExercise(String exerciseBatchId, ModelMap model) throws WebException {
		if(ExStringUtils.isNotBlank(exerciseBatchId)){
			ExerciseBatch exerciseBatch = exerciseBatchService.get(exerciseBatchId);
			if("2".equals(exerciseBatch.getColType())){
				for (Exercise exercise : exerciseBatch.getExercises()) {
					List<Attachs> attachs = attachsService.findAttachsByFormId(exercise.getResourceid());
					exercise.setAttachs(attachs);
				}
			}
			User user = SpringSecurityHelper.getCurrentUser();
			model.addAttribute("storeDir", user.getUsername());			
		
			if(SpringSecurityHelper.isUserInRole(CacheAppManager.getSysConfigurationByCode("sysuser.role.student").getParamValue())){//学生	
				model.addAttribute("isStudent", Constants.BOOLEAN_YES);
				
				StudentInfo studentInfo = studentInfoService.getStudentInfoByUser(user);
				List<StudentExercise> list = studentExerciseService.findByHql(" from "+StudentExercise.class.getName()+" where isDeleted=0 and exerciseBatch.resourceid=? and studentInfo.resourceid=? ", exerciseBatchId,studentInfo.getResourceid());
				Map<String, StudentExercise> answers = new HashMap<String, StudentExercise>();
				for (StudentExercise studentExercise : list) {
					if(studentExercise.getExercise()==null){
						answers.put("exerciseResult", studentExercise);
					} else {
						if("2".equals(exerciseBatch.getColType())){
							List<Attachs> attachs = attachsService.findAttachsByFormId(studentExercise.getResourceid());
							studentExercise.setAttachs(attachs);
						}
						answers.put(studentExercise.getExercise().getResourceid(), studentExercise);
					}
				}
				model.addAttribute("answers", answers);		
			}				
			model.addAttribute("exerciseBatch", exerciseBatch);
		}
		return "/edu3/learning/interactive/studentexercise-list";
	}
		
	/**
	 * 保存学生作业回答情况
	 * @param exerciseBatchId
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/learning/interactive/exercise/save.html")
	public void saveStudentExercise(String exerciseBatchId,String type,String exerciseResultId,HttpServletRequest request,HttpServletResponse response) throws WebException {
		Map<String,Object> map = new HashMap<String, Object>();		
		try {	
			do{
				if(ExStringUtils.isNotBlank(exerciseBatchId) && SpringSecurityHelper.isUserInRole(CacheAppManager.getSysConfigurationByCode("sysuser.role.student").getParamValue())){
					ExerciseBatch exerciseBatch = exerciseBatchService.get(exerciseBatchId);
					User user = SpringSecurityHelper.getCurrentUser();				
					StudentInfo studentInfo = studentInfoService.getStudentInfoByUser(user);
					/*Grade grade = gradeService.getDefaultGrade();
					if(grade == null){
						map.put("statusCode", 300);
						map.put("message", "请联系教务员设置默认年级！");
						continue;
					}*/
					/*String yearTerm = CacheAppManager.getSysConfigurationByCode("sysCurrentGrade").getParamValue();

					if (null==yearTerm) {					
						map.put("statusCode", 300);
						map.put("message", "请联系教务员设置当前学期全局参数！");
						continue;
					}
					String _year = "";
					String _term = "";
					String _yearInfoId = "";
					
					String[] ARRYyterm = yearTerm.split("\\.");
					_year = ARRYyterm[0];
					_yearInfoId = yearInfoService.getByFirstYear(Long.parseLong(_year)).getResourceid();
					_term = ARRYyterm[1];
					*/
					
					//获取开课学期
					TeachingPlanCourse teachingPlanCourse = studentLearnPlanService.getStudentLearnPlanByCourse(exerciseBatch.getCourse().getResourceid(), studentInfo.getResourceid(),"studentId").getTeachingPlanCourse();
					TeachingPlanCourseStatus teachingPlanCourseStatus = teachingPlanCourseStatusService
							.findOneByCondition(studentInfo.getGrade()
									.getResourceid(), studentInfo.getTeachingPlan().getResourceid(),
									teachingPlanCourse.getResourceid(), studentInfo.getBranchSchool().getResourceid());
					if(teachingPlanCourseStatus==null){
						logger.error("更新学生成绩失败:{课程还没有开课}");
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
					//是否排课
					if(!tpctService.isArrangeCourse(exerciseBatch.getCourse().getResourceid(), year+"_0"+term/*grade.getYearInfo().getFirstYear()+"_0"+grade.getTerm()*/)){
						map.put("statusCode", 300);
						map.put("message", "对不起，该门课程还没有排课！");
						continue;
					}
					//是否在网上学习时间内
					LearningTimeSetting learningTimeSetting = learningTimeSettingService.findLearningTimeSetting(yearInfoId, term);
					Date now = new Date();
					if(learningTimeSetting == null || now.before(learningTimeSetting.getStartTime()) || now.after(learningTimeSetting.getEndTime())){
						map.put("statusCode", 300);
						map.put("message", "当前时间不在网上学习时间内或者没有设置！");
						continue;
					}				
					
					String[] exerciseIds = request.getParameterValues("exerciseId");
					if(exerciseIds!=null&&exerciseIds.length>0){	
						int correctNum = 0;
						Date currentDay = new Date();
						List<StudentExercise> studentExercises = new ArrayList<StudentExercise>();	
						List<StudentExercise> allAnswers = studentExerciseService.findByCriteria(Restrictions.eq("isDeleted",0),Restrictions.eq("studentInfo.resourceid",studentInfo.getResourceid()),Restrictions.eq("exerciseBatch.resourceid",exerciseBatchId));
						for(int index=0;index<exerciseIds.length;index++){							
							String[] answers = request.getParameterValues("answer"+exerciseIds[index]);
							String[] attachIds = request.getParameterValues("uploadfileid"+exerciseIds[index]);
							String stuAnswer = ExStringUtils.trimToEmpty(ExStringUtils.join(answers, ""));
							
							Exercise exercise = exerciseService.get(exerciseIds[index]);
							StudentExercise studentExercise = getStudentExercise(allAnswers, exerciseIds[index]);
							studentExercise.setAnswer(stuAnswer);	
							studentExercise.setStudentInfo(studentInfo);
							studentExercise.setExercise(exercise);
							studentExercise.setExerciseBatch(exerciseBatch);
							studentExercise.setAttachIds(attachIds);
							
							if("submit".equals(type)){ //提交
								if("1".equals(exerciseBatch.getColType())){//客观题
									double score = 100.0/exerciseBatch.getExercises().size();
									String stu_answer = ExStringUtils.trimToEmpty(studentExercise.getAnswer());
									String answer = ExStringUtils.trimToEmpty(exercise.getCourseExam().getAnswer());
									if("3".equals(exercise.getCourseExam().getExamType())){//判断题
										stu_answer = CourseExam.covertToCorrectAnswer(stu_answer);
										answer = CourseExam.covertToCorrectAnswer(answer);
									} 
									if(stu_answer.equalsIgnoreCase(answer)){
										studentExercise.setResult(score);	
										correctNum++;
									} else {
										studentExercise.setResult(0.0);
									}
									studentExercise.setStatus(2);//提交后改为已批改	
								} else {
									studentExercise.setStatus(1);//提交后改为待批改	
								}	
								studentExercise.setComitDate(currentDay);
							}							
							studentExercises.add(studentExercise);//设置提交时间
						}
						StudentExercise studentExercise = getStudentExercise(allAnswers, "");//创建在线作业总分记录
						studentExercise.setExercise(null);
						studentExercise.setStudentInfo(studentInfo);
						studentExercise.setExerciseBatch(exerciseBatch);						
						if("submit".equals(type)){ //提交						
							if("1".equals(exerciseBatch.getColType())){//客观题
								studentExercise.setResult(correctNum*100.0/exerciseBatch.getExercises().size());
								studentExercise.setStatus(2);
								
								int totalCorrectNum = 0;
								Map<String, Object> param = new HashMap<String, Object>();
								param.put("exercisebatchids", Arrays.asList(new String[]{exerciseBatch.getResourceid()}));
								List<Map<String, Object>> resultList = exerciseBatchService.findExerciseStatusCount(param);
								if(ExCollectionUtils.isNotEmpty(resultList)){
									totalCorrectNum = ((BigDecimal) resultList.get(0).get("CORRECTNUM")).intValue();						
								}
								if(totalCorrectNum+1<exerciseBatch.getOrderCourseNum()){
									studentExercise.getExerciseBatch().setStatus(2);
								} else {
									studentExercise.getExerciseBatch().setStatus(3);
								}
							} else {
								studentExercise.setStatus(1);
							}
							studentExercise.setComitDate(currentDay);
						}					
						studentExercises.add(studentExercise);
						
						studentExerciseService.batchSaveOrUpdateStudentExercise(studentExercises);
					}				
					map.put("statusCode", 200);
					map.put("message", "submit".equals(type)?"成功提交本次作业！":"成功保存作答答案");
					map.put("reloadUrl", request.getContextPath() +"/edu3/learning/interactive/exercise/list.html?exerciseBatchId="+exerciseBatchId);
				}			
			} while (false);
		}catch (Exception e) {
			logger.error("保存学生作业回答情况出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败！");
		}
		UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "13", UserOperationLogs.UPDATE,"学生提交课程作业：结果："+map.get("message")+"。参数："+JsonUtils.mapToJson(Condition2SQLHelper.getConditionFromResquestByIterator(request)));
		renderJson(response, JsonUtils.mapToJson(map));		
	}
	
	private StudentExercise getStudentExercise(List<StudentExercise> allAnswers,String exerciseId) {
		StudentExercise studentExercise = new StudentExercise();
		for (StudentExercise stuExercise : allAnswers) {
			if(ExStringUtils.isNotBlank(exerciseId) && exerciseId.equals(stuExercise.getExercise().getResourceid())
					||ExStringUtils.isBlank(exerciseId) && stuExercise.getExercise()==null){
				return stuExercise;
			} 
		}
		return studentExercise;
	}	
	
	/**
	 * 随堂问答列表
	 * @param courseId
	 * @param model
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/edu3/learning/interactive/bbstopic/list.html")
	public String listBbsTopic(String courseId,String syllabusId,HttpServletRequest request,ModelMap model,Page objPage) throws Exception {
		User user = SpringSecurityHelper.getCurrentUser();		
		
		//List<BbsTopic> bbsTopics = bbsTopicService.findByHql("from "+BbsTopic.class.getName()+" where isDeleted=0 and courseId=? and topicType=? and fillinManId=? order by fillinDate desc ", new Object[]{courseId,"2",user.getResourceid()});
		String type = ExStringUtils.trimToEmpty(request.getParameter("type")); //more - 更多
		String teachType =  ExStringUtils.trimToEmpty(request.getParameter("teachType"));
		//显示更多问题
		objPage.setOrderBy("fillinDate");
		objPage.setOrder(Page.DESC);//设置默认排序方式
		
		Course course = null;
		if(ExStringUtils.isNotEmpty(courseId)){
			course = courseService.get(courseId);
			
			Map<String,Object> condition = new HashMap<String,Object>();//查询条件
			if(ExStringUtils.isNotEmpty(courseId)) {
				condition.put("courseId", courseId);
			}
			if(ExStringUtils.isNotEmpty(syllabusId)) {
				condition.put("syllabusId", syllabusId);
			}
			if(ExStringUtils.isNotEmpty(type)) {
				condition.put("type", type);
			}
			condition.put("topicType", "2");
			condition.put("notEmptySyllabus", "Y");
			
			String keywords = ExStringUtils.defaultIfEmpty(request.getParameter("keywords"), "");		
			if(ExStringUtils.isNotEmpty(keywords)){		
				//bbsTopics = bbsTopicService.findBbsTopicByFullText(objPage, new String[]{"title","content","topicType","fillinMan"}, keywords);
				condition.put("keywords", keywords);
				condition.put("titlecontent", keywords);
			}else{			
				condition.put("fillinManId", user.getResourceid());
			}	
			// 获取该学生该门课程的总帖数和有效贴数
			int totalTopicNum = 0;
			int validTopicNum = 0;
			Map<String, Object> topicNum = learningJDBCService.getStuCourseTopicNum(user.getResourceid(), courseId);
			if(topicNum!=null && topicNum.size() > 0){
				totalTopicNum = topicNum.get("totalTopicNum")==null?0:((BigDecimal)topicNum.get("totalTopicNum")).intValue();
				validTopicNum = topicNum.get("validTopicNum")==null?0:((BigDecimal)topicNum.get("validTopicNum")).intValue();
			}
			
			Page bbsTopics = bbsTopicService.findBbsTopicByCondition(condition, objPage);
			model.addAttribute("bbsTopics", bbsTopics);
			model.addAttribute("course", course);	
			model.addAttribute("syllabusId", syllabusId);	
			model.addAttribute("condition", condition);
			model.addAttribute("teachType",teachType);
			model.addAttribute("totalTopicNum",totalTopicNum);
			model.addAttribute("validTopicNum",validTopicNum);
		}
		if("more".equals(type)) {
			return "/edu3/learning/interactive/bbstopicMore-list";
		} else {
			return "/edu3/learning/interactive/bbstopic-list";
		}
	}
	
	/**
	 * 保存随堂问答
	 * @param bbsTopic
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/learning/interactive/bbstopic/save.html")
	public void saveBbsTopic(BbsTopic bbsTopic, HttpServletRequest request,HttpServletResponse response) throws WebException {
		Map<String,Object> map = new HashMap<String, Object>();	
		String from = ExStringUtils.trimToEmpty(request.getParameter("from"));//如果=sidebar，则表示从右侧工具条来，为了不影响学生学习页面，不需要跳转到列表
		try {	
			do{
				if(ExStringUtils.isEmpty(bbsTopic.getTitle()) || ExStringUtils.isEmpty(bbsTopic.getContent())){
					map.put("statusCode", 300);
					map.put("message", "标题和内容都不能为空");
					continue;
				}
				// 判断是否包含敏感词
				SensitivewordFilter sensitivewordFilter = (SensitivewordFilter)request.getSession().getServletContext().getAttribute("sensitivewordFilter");
				Set<String> sensitivewordSet = sensitivewordFilter.getSensitiveWord(bbsTopic.getTitle()+bbsTopic.getContent(),SensitivewordFilter.maxMatchType);
				if(!CollectionUtils.isEmpty(sensitivewordSet)){
					map.put("statusCode", 300);
					map.put("message", "标题或内容含有敏感词："+sensitivewordSet+",请修改后再提交");
					continue;
				}
				User user = SpringSecurityHelper.getCurrentUser();				
				StudentInfo studentInfo = studentInfoService.getStudentInfoByUser(user);
				String courseId = ExStringUtils.trimToEmpty(request.getParameter("courseId"));
				Course course = courseService.get(courseId);
				String syllabusId = ExStringUtils.trimToEmpty(request.getParameter("syllabusId"));
				Syllabus syllabus = syllabusService.get(syllabusId);
				/*Grade grade = gradeService.getDefaultGrade();
				if(grade == null){
					map.put("statusCode", 300);
					map.put("message", "请联系教务员设置默认年级！");
					continue;
				}*/
				
				/*String yearTerm = CacheAppManager.getSysConfigurationByCode("sysCurrentGrade").getParamValue();

				if (null==yearTerm) {					
					map.put("statusCode", 300);
					map.put("message", "请联系教务员设置当前学期全局参数！");
					continue;
				}
				String _year = "";
				String _term = "";
				String _yearInfoId = "";
				
				String[] ARRYyterm = yearTerm.split("\\.");
				_year = ARRYyterm[0];
				_yearInfoId = yearInfoService.getByFirstYear(Long.parseLong(_year)).getResourceid();
				_term = ARRYyterm[1];*/
				
				
				//获取开课学期
				if(studentInfo==null){
					map.put("statusCode", 300);
					map.put("message", "无法获取学籍信息！");
					continue;
				}
				
				StudentLearnPlan studentLearnPlan = studentLearnPlanService.getStudentLearnPlanByCourse(courseId, studentInfo.getResourceid(),"studentId");
				if(studentLearnPlan==null){
					map.put("statusCode", 300);
					map.put("message", "请先创建学生学习计划！");
					continue;
				}
				TeachingPlanCourse teachingPlanCourse = studentLearnPlan.getTeachingPlanCourse();
				if(teachingPlanCourse==null){
					map.put("statusCode", 300);
					map.put("message", "无法从学习计划中获取教学计划信息！");
					continue;
				}
				TeachingPlanCourseStatus teachingPlanCourseStatus = teachingPlanCourseStatusService
						.findOneByCondition(studentInfo.getGrade()
								.getResourceid(), studentInfo.getTeachingPlan().getResourceid(),
								teachingPlanCourse.getResourceid(), studentInfo.getBranchSchool().getResourceid());
				if(teachingPlanCourseStatus==null){
					map.put("statusCode", 300);
					map.put("message", "对不起，该门课程还没有开课！");
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
				
				if(!tpctService.isArrangeCourse(courseId, year+"_0"+term /*grade.getYearInfo().getFirstYear()+"_0"+grade.getTerm()*/)){
					map.put("statusCode", 300);
					map.put("message", "对不起，该门课程还没有排课！");
					continue;
				}				
				//是否在网上学习时间内
				LearningTimeSetting learningTimeSetting = learningTimeSettingService.findLearningTimeSetting(yearInfoId, term);
				Date now = new Date();
				if(learningTimeSetting == null || now.before(learningTimeSetting.getStartTime()) || now.after(learningTimeSetting.getEndTime())){
					map.put("statusCode", 300);
					map.put("message", "当前时间不在网上学习时间内,请联系相关人员设置！");
					continue;
				}
				String sectionCode = CacheAppManager.getSysConfigurationByCode("web.bbs.sectioncode").getParamValue();//获取版块编码
				BbsSection bbsSection = bbsSectionService.findUniqueByProperty("sectionCode", sectionCode);
				if(bbsSection==null){
					map.put("statusCode", 300);
					map.put("message", "请先添加课程论坛版块，板块编码请参考全局参数‘web.bbs.sectioncode’！");
					continue;
				}
				bbsTopic.setFillinMan(user.getCnName());
				bbsTopic.setCourse(course);
				bbsTopic.setSyllabus(syllabus);
				bbsTopic.setTopicType("2");//设置类型为提问
				bbsTopic.setFillinDate(new Date());
				bbsTopic.setStatus(0);//普通帖	
				bbsTopic.setLastReplyDate(bbsTopic.getFillinDate());
				bbsTopic.setLastReplyMan(user.getCnName());
				bbsTopic.setBbsSection(bbsSection);
				Integer topicCount = bbsSection.getTopicCount();
				bbsSection.setTopicCount(topicCount==null?1:topicCount.intValue()+1);
				
				bbsTopicService.saveOrUpdateBbsTopic(bbsTopic, null,null);
				map.put("statusCode", 200);
				map.put("message", "提交成功！");
				String questiontype = request.getParameter("questiontype");
				if(ExStringUtils.isNotEmpty(questiontype)) {
					map.put("questiontype", questiontype);
				}
				if(ExStringUtils.isNotEmpty(from)) {
					map.put("from", from);
				}
			} while (false);
		}catch (Exception e) {
			logger.error("保存随堂问答出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败！");
		}
		UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "13", UserOperationLogs.UPDATE,"学生PC端保存/提交随堂提问：结果："+map.get("message")+"。参数："+JsonUtils.mapToJson(Condition2SQLHelper.getConditionFromResquestByIterator(request)));
		renderJson(response, JsonUtils.mapToJson(map));	
	}
	
	/**
	 * 随堂问答回复列表
	 * @param bbsTopicId
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/learning/interactive/bbsreply/list.html")
	public String listBbsReply(String bbsTopicId, ModelMap model,Page objPage) throws WebException {
		BbsTopic bbsTopic = bbsTopicService.get(bbsTopicId);
		
//		if(bbsTopic.getCourse()!=null){
//			List<TeachTask> list = teachTaskService.findByHql(" from "+TeachTask.class.getSimpleName()+" where isDeleted=0 and course.resourceid=? ", bbsTopic.getCourse().getResourceid());
//			Set<String> teacherIds = new HashSet<String>(0);
//			for (TeachTask teachTask : list) {
//				teacherIds.addAll(teachTask.getTeacherIds());
//			}
//			model.addAttribute("teacherIds", ExStringUtils.join(teacherIds, ","));
//		}
		
		objPage.setOrderBy("replyDate");
		objPage.setOrder(Page.ASC);//设置默认排序方式
		
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件
		if(ExStringUtils.isNotEmpty(bbsTopicId)) {
			condition.put("bbsTopicId", bbsTopicId);
		}
		
		Page bbsReplyListPage = bbsReplyService.findBbsReplyByCondition(condition, objPage);
		if(ExCollectionUtils.isNotEmpty(bbsReplyListPage.getResult())){
			for (Object reply : bbsReplyListPage.getResult()) {
				BbsReply bbsReply = (BbsReply)reply;
				if(!Constants.BOOLEAN_NO.equalsIgnoreCase(bbsReply.getIsAttachs())){
					List<Attachs> attachs = attachsService.findAttachsByFormId(bbsReply.getResourceid());
					bbsReply.setAttachs(attachs);
				}
			}
		}
		
		
		model.addAttribute("bbsTopic", bbsTopic);	
		model.addAttribute("bbsReplyListPage", bbsReplyListPage);	
		model.addAttribute("condition", condition);	
		return "/edu3/learning/interactive/bbsreply-list";
	}
	/**
	 * 随堂问答提问
	 * @param courseId
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/learning/interactive/bbstopic/ask.html")
	public String askQuestion(String courseId,String syllabusId,String questiontype, ModelMap model) throws WebException {				
		if(ExStringUtils.isNotEmpty(syllabusId)){
			Syllabus syllabus = syllabusService.get(syllabusId);
			model.addAttribute("syllabus", syllabus);
		}else{
			if(ExStringUtils.isNotEmpty(courseId)){
				Course course = courseService.get(courseId);
				model.addAttribute("course", course);
			}
		}
	
		model.addAttribute("questiontype", questiontype);
		User user = SpringSecurityHelper.getCurrentUser();
		model.addAttribute("storeDir", user.getUsername());
		return "/edu3/learning/interactive/question-ask";
	}
	/**
	 * 优秀作业和典型批改
	 * @param courseId
	 * @param type
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/learning/interactive/studentexercise/list.html")
	public String listStudentExercise(String courseId,String tCourseId,String type, ModelMap model) throws WebException {
		if(ExStringUtils.isNotEmpty(courseId)){
			Map<String,Object> condition = new HashMap<String,Object>();//查询条件
			condition.put("courseId", courseId);
			if(ExStringUtils.isNotEmpty(type)) {
				condition.put(type, Constants.BOOLEAN_YES);
			}
			condition.put("exerciseResult", "null");
			condition.put("orderBy", "exerciseBatch.startDate desc,studentInfo.studyNo");	
			
			List<StudentExercise> list = studentExerciseService.findStudentExerciseByCondition(condition);
			model.addAttribute("type", type);
			model.addAttribute("studentExerciseList", list);
		}
		return "/edu3/learning/interactive/specialStudentexercise-list";
	}
	/**
	 * 查看优秀作业和典型批改
	 * @param studentExerciseId
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/learning/interactive/studentexercise/view.html")
	public String viewStudentExercise(String studentExerciseId, ModelMap model) throws WebException {
		if(ExStringUtils.isNotBlank(studentExerciseId)){
			StudentExercise studentExerciseResult = studentExerciseService.get(studentExerciseId);
			List<Attachs> resultAttachs = attachsService.findAttachsByFormId(studentExerciseId);
			studentExerciseResult.setAttachs(resultAttachs);
			model.addAttribute("studentExerciseResult", studentExerciseResult);					
			
			ExerciseBatch exerciseBatch = studentExerciseResult.getExerciseBatch();
			if("2".equals(exerciseBatch.getColType())){
				for (Exercise exercise : exerciseBatch.getExercises()) {
					List<Attachs> attachs = attachsService.findAttachsByFormId(exercise.getResourceid());
					exercise.setAttachs(attachs);
				}
			}
			
			StudentInfo studentInfo = studentExerciseResult.getStudentInfo();
			List<StudentExercise> list = studentExerciseService.findByHql(" from "+StudentExercise.class.getName()+" where isDeleted=0 and exerciseBatch.resourceid=? and studentInfo.resourceid=? ", exerciseBatch.getResourceid(),studentInfo.getResourceid());
			Map<String, StudentExercise> answers = new HashMap<String, StudentExercise>();
			for (StudentExercise studentExercise : list) {
				if(studentExercise.getExercise()==null){
					answers.put("exerciseResult", studentExercise);
				} else {
					if("2".equals(exerciseBatch.getColType())){
						List<Attachs> attachs = attachsService.findAttachsByFormId(studentExercise.getResourceid());
						studentExercise.setAttachs(attachs);
					}
					answers.put(studentExercise.getExercise().getResourceid(), studentExercise);
				}
			}
			model.addAttribute("answers", answers);		
			model.addAttribute("exerciseBatch", exerciseBatch);
		}
		return "/edu3/learning/interactive/specialStudentexercise-view";
	}
	/**
	 * FAQ问题
	 * @param courseId
	 * @param model
	 * @param objPage
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/learning/interactive/faq/list.html")
	public String listFAQTopic(String courseId, ModelMap model, Page objPage) throws WebException {
		objPage.setOrderBy("syllabus,fillinDate");
		objPage.setOrder(Page.DESC);//设置默认排序方式
		
		if(ExStringUtils.isNotEmpty(courseId)){
			Map<String,Object> condition = new HashMap<String,Object>();//查询条件
			condition.put("courseId", courseId);
			condition.put("topicType", "2");
			condition.put("tags", "FAQ");
			condition.put("notEmptySyllabus", "Y");
			condition.put("sectionCode", CacheAppManager.getSysConfigurationByCode("web.bbs.sectioncode").getParamValue());
			
			Page bbsTopics = bbsTopicService.findBbsTopicByCondition(condition, objPage);
			model.addAttribute("bbsTopics", bbsTopics);	
			model.addAttribute("condition", condition);
		}
		return "/edu3/learning/interactive/faqtopic-list";
	}
	
	/**
	 * 获取平时成绩积分
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/learning/interactive/usualresults/ajax.html")
	public void ajaxUsualResults(HttpServletRequest request,HttpServletResponse response) throws WebException {
		Map<String,Object> map = new HashMap<String, Object>();	
		try {
			String courseId  = ExStringUtils.trimToEmpty(request.getParameter("courseId"));
			String teachType = ExStringUtils.trimToEmpty(request.getParameter("teachType"));
			if(ExStringUtils.isNotEmpty(courseId)){
				User user = SpringSecurityHelper.getCurrentUser();
				String studentId = null;//学生id
				if(null!=user.getUserExtends() && null!=user.getPropertys(UserExtends.USER_EXTENDCODE_DEFAULTROLLID)){
					studentId = user.getPropertys(UserExtends.USER_EXTENDCODE_DEFAULTROLLID).getExValue();				
				}				
				if(ExStringUtils.isNotEmpty(studentId)){//学生			
					StudentLearnPlan plan = studentLearnPlanService.getStudentLearnPlanByCourse(courseId,studentId, "studentId");
					String yearInfoId = "";
					String term = "";
					if(plan != null){
//						if(plan.getStatus()!=3){//已考完状态
//						
//					} else {// 正在学习 或 准备考试 或 重做随堂练习
//						Grade grade = gradeService.getDefaultGrade();
//						if(grade!=null){ //取当前学期的积分规则
//							yearInfoId = grade.getYearInfo().getResourceid();
//							term = grade.getTerm();
//						}						
//						UsualResultsRule rule = usualResultsRuleService.getUsualResultsRuleByCourseAndYear(courseId,yearInfoId,term);				
//						if(rule != null){						
//							Map<String,Object> condition = new HashMap<String,Object>();
//							condition.put("courseId", courseId);
//							condition.put("isBest", "Y");
//							condition.put("userId", user.getResourceid());	
//							String sectionCode = CacheAppManager.getSysConfigurationByCode("web.bbs.sectioncode").getParamValue();
//							
//							Double originalBbsResults = 0.0;//网络辅导分						
//							Double originalAskQuestionResults = 0.0;//随堂问答分
//							Double originalExerciseResults = 0.0;//作业练习分
//							Double originalCourseExamResults = 0.0;//随堂练习分
//							
//							if(rule.getAskQuestionResultPer()>0 && rule.getBbsBestTopicNum()>0){	//随堂问答分						
//								condition.put("sectionCode", sectionCode);
//								Integer askCount = bbsTopicService.statTopicAndReply(condition);//随堂问答
//								originalAskQuestionResults = askCount>=rule.getBbsBestTopicNum()?100.0:askCount*rule.getBbsBestTopicResult();
//							}
//							if(rule.getBbsResultPer()>0 && rule.getBbsBestTopicNum()>0){//网络辅导分
//								condition.remove("sectionCode");
//								condition.put("notSectionCode", sectionCode);
//								Integer bbsCount = bbsTopicService.statTopicAndReply(condition);//网络辅导
//								originalBbsResults = bbsCount>=rule.getBbsBestTopicNum()?100.0:bbsCount*rule.getBbsBestTopicResult();	
//							}
//							if(rule.getExerciseResultPer()>0){//作业练习分		
//								if(plan!=null){								
//									originalExerciseResults = studentExerciseService.avgStudentExerciseResult(courseId, studentId,yearInfoId,term);
//								}													
//							}												
//							if(rule.getCourseExamResultPer()>0){//随堂练习分
//								Double per = studentActiveCourseExamService.avgStudentActiveCourseExamResult(courseId, studentId);						
//								//网络面授类课程的平时分只取随堂练习的分数，且不按照计分规则中的比例来计算
//								if ("netsidestudy".equals(teachType)) {
//									originalCourseExamResults = per*100;
//								}else {
//									originalCourseExamResults = per*100>=rule.getCourseExamCorrectPer()?100:per*100;
//								}
//							}						
//							
//							Double usualResults = 0.0;
//							usualResults += originalBbsResults * rule.getBbsResultPer();
//							usualResults += originalExerciseResults * rule.getExerciseResultPer();
//							usualResults += originalCourseExamResults * rule.getCourseExamResultPer();	
//							usualResults += originalAskQuestionResults * rule.getAskQuestionResultPer();	
//							usualResults = usualResults / 100;
//							
//							map.put("bbsResults", BigDecimalUtil.round(originalBbsResults, 1));
//							map.put("askQuestionResults", BigDecimalUtil.round(originalAskQuestionResults, 1));
//							map.put("exerciseResults", BigDecimalUtil.round(originalExerciseResults, 1));
//							map.put("courseExamResults", BigDecimalUtil.round(originalCourseExamResults, 1));
//							map.put("usualResults", BigDecimalUtil.round(usualResults, 1));
//							//网络面授类课程的平时分只取随堂练习的分数，且不按照计分规则中的比例来计算
//							if ("netsidestudy".equals(teachType)) {
//								map.put("courseExamResults", BigDecimalUtil.round(originalCourseExamResults, 1));
//								map.put("usualResults", BigDecimalUtil.round(originalCourseExamResults, 1));
//							}
//							if(Constants.BOOLEAN_NO.equals(rule.getIsUsed())){ //更新规则使用状态
//								rule.setIsUsed(Constants.BOOLEAN_YES);							
//								usualResultsRuleService.update(rule);
//							}
//						}
//					}	
						/*屏蔽上部分，因为现在老师批改作业，回复随堂问答，学生提交随堂练习都会实时计算学生最新平时成绩*/
						if(plan.getUsualResults() != null){ // 直接显示平时成绩
							UsualResults r = plan.getUsualResults();
							map.put("exerciseShow", Constants.BOOLEAN_YES);
							map.put("bbsResults", BigDecimalUtil.round(Double.valueOf(ExStringUtils.defaultIfEmpty(r.getBbsResults(), "0")), 1));
							map.put("askQuestionResults", BigDecimalUtil.round(Double.valueOf(ExStringUtils.defaultIfEmpty(r.getAskQuestionResults(), "0")), 1));
							map.put("exerciseResults", BigDecimalUtil.round(Double.valueOf(ExStringUtils.defaultIfEmpty(r.getExerciseResults(), "0")), 1));
							map.put("courseExamResults", BigDecimalUtil.round(Double.valueOf(ExStringUtils.defaultIfEmpty(r.getCourseExamResults(), "0")), 1));
							map.put("usualResults", BigDecimalUtil.round(Double.valueOf(ExStringUtils.defaultIfEmpty(r.getUsualResults(), "0")), 1));
						}
					}								
				}
			}
		}catch (Exception e) {
			logger.error("获取平时成绩出错：{}",e.fillInStackTrace());
		}
		renderJson(response, JsonUtils.mapToJson(map));	
	}
	/**
	 * 温馨提示
	 */
	@RequestMapping("/edu3/learning/interactive/message/list.html")
	public String listMessage(String courseId,HttpServletRequest request,Page objPage, ModelMap model) throws Exception{	
		objPage.setOrderBy("messageReceiver.message.sendTime"); //按发送时间排序
		objPage.setOrder(Page.DESC);
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件	
		if(ExStringUtils.isNotEmpty(courseId) && SpringSecurityHelper.isUserInRole(CacheAppManager.getSysConfigurationByCode("sysuser.role.student").getParamValue())){
			User user = SpringSecurityHelper.getCurrentUser();
			if(null!=user){
				/*Map<String,Object> condition1 = new HashMap<String,Object>();
				condition1.put("studentId", user.getResourceid());
				condition1.put("courseId",courseId);
				condition1.put("taskStatus", "3");		*/		
//				List<TeachTask> list = teachTaskService.findTeachTaskByCondition(condition1);
//				Set<String> teacherIds = new HashSet<String>();
//				if(list != null && !list.isEmpty()){
//					for (TeachTask teachTask : list) {
//						teacherIds.addAll(teachTask.getTeacherIds());
//					}
//				}
				
//				if(!teacherIds.isEmpty()){
//					condition.put("senderIds", "'"+ExStringUtils.join(teacherIds, "','")+"'");
//				}				
				condition.put("msgType", "tips");
				condition.put("userName", user.getUsername());
				condition.put("unitCode", null!=user.getOrgUnit()?user.getOrgUnit().getUnitCode():"");
				condition.put("roleCode", ExCollectionUtils.fetchPropertyToString(user.getRoles(), "roleCode", ","));
				condition.put("userId", user.getResourceid());
								
//				Page messagePage = messageReceiverService.findMessageByCondition(condition, objPage);		
				Page messagePage = messageReceiverUserService.findByCondition(condition, objPage);		
				for (Object o : messagePage.getResult()) {
					MessageReceiverUser receiverUser = (MessageReceiverUser)o;
					receiverUser.getMessageReceiver().getMessage().setReaded("readed".equals(receiverUser.getReadStatus()));
				}
				model.addAttribute("messagePage", messagePage);	
				model.addAttribute("condition", condition);	
			}
		}				
		return "/edu3/learning/interactive/message-list";
	}
	
	
	/**
	 * 异步获取消息数
	 * @param courseId
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/framework/learning/getmsgcount.html")
	public void getCountMessageNum(String courseId,HttpServletRequest request,HttpServletResponse response) throws WebException{
		if(ExStringUtils.isNotEmpty(courseId)){
			Map<String, Object> parameters = new HashMap<String, Object>();
			try {
				String sql = " select count(resourceid) from edu_lear_coursenotice where isdeleted = :isdeleted and courseid = :courseid ";
				parameters.put("isdeleted", 0);
				parameters.put("courseid", courseId);
				/*Grade grade = gradeService.getDefaultGrade();
				if(grade!=null){
					sql += " and yearid = :yearInfoId and term = :term ";
					parameters.put("yearInfoId", grade.getYearInfo().getResourceid());
					parameters.put("term", grade.getTerm());
				}*/
				String yearTerm = CacheAppManager.getSysConfigurationByCode("sysCurrentGrade").getParamValue();
				String yearInfoId = "";
				String term = "";
				if (null!=yearTerm) {
					String[] ARRYyterm = yearTerm.split("\\.");
					yearInfoId = yearInfoService.getByFirstYear(Long.parseLong(ARRYyterm[0])).getResourceid();
					term = ARRYyterm[1];
					
					sql += " and yearid = :yearInfoId and term = :term ";
					parameters.put("yearInfoId", yearInfoId);
					parameters.put("term", term);
				}
				long courseNoticeNum = baseSupportJdbcDao.getBaseJdbcTemplate().
								findForLong(sql, parameters);
				parameters.clear();
				parameters.put("couseNoticeNum", courseNoticeNum);
				renderJson(response, JsonUtils.mapToJson(parameters));
			} catch (Exception e) {
			
			}
			
		}
	}
	/**
	 * 是否有资源
	 * @param courseId
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/learning/interactive/course/hasresource/ajax.html")
	public void isEmptySyllabus(String courseId,String isNeedReExamination,String from,HttpServletResponse response) throws WebException {		
		Map<String, Object> map = new HashMap<String, Object>();
		if(ExStringUtils.isNotBlank(courseId)){
			Course course = courseService.get(courseId);			
			map.put("isQualityResource", ExStringUtils.trimToEmpty(course.getIsQualityResource()));//是否精品课程
			if(Constants.BOOLEAN_YES.equals(course.getHasResource())){
				map.put("hasResource", Constants.BOOLEAN_YES);//是否有课件资源							
			} 
		}					
		renderJson(response, JsonUtils.mapToJson(map));
	}	
	/**
	 * 教师复习总结录像
	 * @param reviseCourseId
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/learning/interactive/materevise/list.html")
	public String showCourseMockTest(String reviseCourseId,Page objPage, ModelMap model)throws WebException{
		objPage.setOrderBy(" course.courseCode,showOrder,resourceid ");
		objPage.setOrder(Page.ASC);
		
		Map<String,Object> condition = new HashMap<String,Object>();
		if(ExStringUtils.isNotEmpty(reviseCourseId)){
			condition.put("reviseCourseId", reviseCourseId);
		}
		condition.put("channelType", "revise");//教师复习总结录像
		condition.put("isPublished", Constants.BOOLEAN_YES);//发布
		
		Page page = mateResourceService.findMateResourceByCondition(condition,objPage);		
		model.addAttribute("reviseList", page);
		return "/edu3/learning/interactive/materevise-list";
	}

	
	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;
	
	@Autowired
	@Qualifier("courseService")
	private ICourseService courseService;//注入课程服务
	
	@Autowired
	@Qualifier("syllabusService")
	private ISyllabusService syllabusService;
	
	@Autowired
	@Qualifier("activeCourseExamService")
	private IActiveCourseExamService activeCourseExamService;
	
	@Autowired
	@Qualifier("studentActiveCourseExamService")
	private IStudentActiveCourseExamService studentActiveCourseExamService;
	
	@Autowired
	@Qualifier("studentinfoservice")
	private IStudentInfoService studentInfoService;
	
	@Autowired
	@Qualifier("mateResourceService")
	private IMateResourceService mateResourceService;
	
	@Autowired
	@Qualifier("courseNoticeService")
	private ICourseNoticeService courseNoticeService;
	
	@Autowired
	@Qualifier("courseOverviewService")
	private ICourseOverviewService courseOverviewService;
	
	@Autowired
	@Qualifier("courseLearningGuidService")
	private ICourseLearningGuidService courseLearningGuidService;
	
//	@Autowired
//	@Qualifier("exerciseBatchService")
//	private IExerciseBatchService exerciseBatchService;
		
	@Autowired
	@Qualifier("attachsService")
	private IAttachsService attachsService;
	
//	@Autowired
//	@Qualifier("exerciseService")
//	private IExerciseService exerciseService;
	
//	@Autowired
//	@Qualifier("studentExerciseService")
//	private IStudentExerciseService studentExerciseService;	
	
	@Autowired
	@Qualifier("bbsTopicService")
	private IBbsTopicService bbsTopicService;
	
	@Autowired
	@Qualifier("bbsReplyService")
	private IBbsReplyService bbsReplyService;

	@Autowired
	@Qualifier("bbsSectionService")
	private IBbsSectionService bbsSectionService;
	
	@Autowired
	@Qualifier("courseMockTestService")
	private ICourseMockTestService courseMockTestService;
	
	@Autowired
	@Qualifier("courseReferenceService")
	private ICourseReferenceService courseReferenceService;
	
//	@Autowired
//	@Qualifier("syllabusindService")
//	private ISyllabusindService syllabusindService;
	
	@Autowired
	@Qualifier("studentLearnPlanService")
	private IStudentLearnPlanService studentLearnPlanService;
	
	@Autowired
	@Qualifier("usualResultsRuleService")
	private IUsualResultsRuleService usualResultsRuleService;
	
//	@Autowired
//	@Qualifier("teachtaskservice")
//	private ITeachTaskService teachTaskService;
	
	@Autowired
	@Qualifier("messageReceiverService")
	private IMessageReceiverService messageReceiverService;
	
	@Autowired
	@Qualifier("messageStatService")
	private IMessageStatService messageStatService;
	
	@Autowired
	@Qualifier("gradeService")
	private IGradeService gradeService;
	
	@Autowired
	@Qualifier("learningTimeSettingService")
	private ILearningTimeSettingService learningTimeSettingService;
}
