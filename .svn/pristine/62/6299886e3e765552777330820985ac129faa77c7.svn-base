package com.hnjk.edu.resources.controller;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.cache.MemcachedManager;
import com.hnjk.core.foundation.utils.ExBeanUtils;
import com.hnjk.core.foundation.utils.ExCollectionUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.FileUtils;
import com.hnjk.core.foundation.utils.GUIDUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.foundation.utils.SnatchWebContentUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.controller.FileUploadAndDownloadSupportController;
import com.hnjk.core.support.context.Constants;
import com.hnjk.core.support.context.SpringContextHolder;
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
import com.hnjk.edu.learning.model.CourseOverview;
import com.hnjk.edu.learning.model.Exercise;
import com.hnjk.edu.learning.model.ExerciseBatch;
import com.hnjk.edu.learning.model.MateResource;
import com.hnjk.edu.learning.model.StudentActiveCourseExam;
import com.hnjk.edu.learning.model.StudentExercise;
import com.hnjk.edu.learning.model.StudentLearnPlan;
import com.hnjk.edu.learning.model.StudentLearningNote;
import com.hnjk.edu.learning.model.StudentLearningTrace;
import com.hnjk.edu.learning.model.StudentSelfExercise;
import com.hnjk.edu.learning.model.TeacherCourseFiles;
import com.hnjk.edu.learning.service.IActiveCourseExamService;
import com.hnjk.edu.learning.service.IBbsReplyService;
import com.hnjk.edu.learning.service.IBbsSectionService;
import com.hnjk.edu.learning.service.IBbsTopicService;
import com.hnjk.edu.learning.service.ICourseLearningGuidService;
import com.hnjk.edu.learning.service.ICourseMockTestService;
import com.hnjk.edu.learning.service.ICourseOverviewService;
import com.hnjk.edu.learning.service.IExerciseBatchService;
import com.hnjk.edu.learning.service.IExerciseService;
import com.hnjk.edu.learning.service.ILearningTimeSettingService;
import com.hnjk.edu.learning.service.IMateResourceService;
import com.hnjk.edu.learning.service.IStudentActiveCourseExamService;
import com.hnjk.edu.learning.service.IStudentExerciseService;
import com.hnjk.edu.learning.service.IStudentLearnPlanService;
import com.hnjk.edu.learning.service.IStudentLearningNoteService;
import com.hnjk.edu.learning.service.IStudentLearningTraceService;
import com.hnjk.edu.learning.service.IStudentSelfExerciseService;
import com.hnjk.edu.learning.service.ITeacherCourseFilesService;
import com.hnjk.edu.learning.util.CourseExamUtils;
import com.hnjk.edu.portal.model.Channel;
import com.hnjk.edu.portal.service.IChannelService;
import com.hnjk.edu.resources.service.ICourseResourceService;
import com.hnjk.edu.resources.vo.SyllabusVo;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.roll.service.IStudentInfoService;
import com.hnjk.edu.teaching.model.LearningTimeSetting;
import com.hnjk.edu.teaching.model.Syllabus;
import com.hnjk.edu.teaching.model.TeachingPlanCourse;
import com.hnjk.edu.teaching.model.TeachingPlanCourseStatus;
import com.hnjk.edu.teaching.model.UsualResultsRule;
import com.hnjk.edu.teaching.service.ICourseService;
import com.hnjk.edu.teaching.service.ISyllabusService;
import com.hnjk.edu.teaching.service.ITeachingPlanCourseService;
import com.hnjk.edu.teaching.service.ITeachingPlanCourseStatusService;
import com.hnjk.edu.teaching.service.IUsualResultsRuleService;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.Attachs;
import com.hnjk.platform.system.model.SysConfiguration;
import com.hnjk.platform.system.service.IAttachsService;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;
import com.hnjk.security.model.UserExtends;

/**
 * 精品课程学习页面展示Controller.<p>
 * 说明：resource这个包暂时为精品课程用，以后要逐步合并原来的学习互动中心功能。<p>
 * @author Administrator
 *
 */
@Controller
public class CourseResourceController extends FileUploadAndDownloadSupportController{
	private static final long serialVersionUID = 930126569665868702L;

	/**
	 * 精品资源课程首页.
	 * @return
	 * @throws WebException
	 */
	@RequestMapping(value="/resource/index.html",method = RequestMethod.GET)
	public String indexPage(ModelMap model) throws WebException{
		List<Course> courseList = courseService.findByCriteria(Course.class, new Criterion[]{Restrictions.eq("isDeleted", 0),Restrictions.eq("status", 1L),Restrictions.eq("isQualityResource", Constants.BOOLEAN_YES)},Order.asc("courseCode"));
		model.addAttribute("courseList", courseList);
		return "/edu3/resources/site/main";
	}
	
	/**
	 * 精品资源课程主页.
	 * @param courseId
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping(value="/resource/course/index.html",method = RequestMethod.GET)
	public String indexPage(String courseId, ModelMap model) throws WebException{		
		if(ExStringUtils.isNotBlank(courseId)){
			getCourseOverview(courseId, model);//课程简介				
			getLeaningGuidList(courseId, model);//学习指导	
			getStudySteps(courseId, model);//学习阶段			
			getTeacherFilesList(courseId, model);//特色资源			
			getStudyFiles(courseId, model);//知识园地				
			try {
				model.addAttribute("course", courseService.get(courseId));
				model.addAttribute("syllabusRoot", syllabusService.getSyllabusRoot(courseId));	
			} catch (Exception e) {				
			}
		}
		//用户
		User user = SpringSecurityHelper.getCurrentUser();
		model.addAttribute("user", user);		
		
		SysConfiguration sysConfig = null;
		if(ExStringUtils.isNotBlank(courseId)){
			sysConfig = CacheAppManager.getSysConfigurationByCode("course_"+courseId);
		}		
		String path = sysConfig != null?ExStringUtils.trimToEmpty(sysConfig.getParamValue()):"";
		if(ExStringUtils.isNotBlank(path)){
			return "/edu3/resources/site/"+path;
		} 
		return "/edu3/resources/site/index";
	}

	/**
	 * 课程简介
	 * @param courseId
	 * @param model
	 */
	private void getCourseOverview(String courseId, ModelMap model) {
		try {
			//加载课程简介
			String courseOverviewType = CacheAppManager.getSysConfigurationByCode("course.overviewtype.introduction").getParamValue();
			List<CourseOverview> courseOverviewList = courseOverviewService.findByHql("from "+CourseOverview.class.getName()+" where isDeleted=0 and course.resourceid=? and type=?", courseId,ExStringUtils.trimToEmpty(courseOverviewType));
			if(ExCollectionUtils.isNotEmpty(courseOverviewList)){
				String courseOverviewContent = CourseExamUtils.trimHtml2Txt(courseOverviewList.get(0).getContent(), null);
				courseOverviewContent = ExStringUtils.trimToEmpty(courseOverviewContent);
				courseOverviewContent = courseOverviewContent.length() > 200 ? ExStringUtils.substring(courseOverviewContent,0,200) : courseOverviewContent;
				model.addAttribute("courseOverviewContent", courseOverviewContent+"……");
			}
		} catch (Exception e) {		
			logger.error("加载课程简介出错:{}",e.fillInStackTrace());
		}
	}

	/**
	 * @param courseId
	 * @param model
	 * @param condition
	 */
	private void getStudySteps(String courseId, ModelMap model) {
		try {
			//学习阶段
			Map<String,Object> condition = new HashMap<String,Object>();
			condition.put("courseId", courseId);
			condition.put("channelLevel", 1);
			condition.put("channelStatus", 0);
			condition.put("channelType", "special");//专栏
			condition.put("channelPosition", "CENTER");//中部
			List<Channel> channelList = channelService.findCourseChannelByCondition(condition);
			model.addAttribute("studyStepList", channelList);
			model.addAttribute("newLine", "\n");	
		} catch (Exception e) {		
			logger.error("加载学习阶段出错:{}",e.fillInStackTrace());
		}
	}

	/**
	 * 每章学习指导
	 * @param courseId
	 * @param model
	 */
	private void getLeaningGuidList(String courseId, ModelMap model) {
		try {
			//每章指导
			List<Syllabus> syllabusList =  syllabusService.findByHql("from "+Syllabus.class.getSimpleName()+" where isDeleted=0 and course.resourceid=? and syllabusLevel=? order by showOrder,resourceid", courseId,1L);
			model.addAttribute("syllabusList", syllabusList);
			//学习指导
			String courseLearningGuidType = CacheAppManager.getSysConfigurationByCode("course.learningguid.guidetype").getParamValue();
			List<CourseLearningGuid> courseLearningGuidList = courseLearningGuidService.findByHql("from "+CourseLearningGuid.class.getName()+" where isDeleted=0 and syllabus.course.resourceid=? and syllabus.syllabusLevel=? and type=? ",courseId,1L,ExStringUtils.trimToEmpty(courseLearningGuidType));
			Map<String, CourseLearningGuid> courseLearningGuidMap = new HashMap<String, CourseLearningGuid>();
			for (CourseLearningGuid courseLearningGuid : courseLearningGuidList) {
				courseLearningGuidMap.put(courseLearningGuid.getSyllabus().getResourceid(), courseLearningGuid);
			}
			model.addAttribute("courseLearningGuidMap", courseLearningGuidMap);	
		} catch (Exception e) {		
			logger.error("加载学习指导出错:{}",e.fillInStackTrace());
		}
	}

	/**
	 * 知识园地
	 * @param courseId
	 * @param model
	 * @param condition
	 */
	private void getStudyFiles(String courseId, ModelMap model) {
		try {
			//知识园地
			Map<String, Object> condition = new HashMap<String, Object>();
			String studyCenterName = CacheAppManager.getSysConfigurationByCode("course.teacherfiles.studycenter").getParamValue();
			if(ExStringUtils.isNotBlank(studyCenterName)){
				condition.put("parentFileName", studyCenterName);
			}
			condition.put("courseId", courseId);
			condition.put("fileType", 1);//只查询文件
			condition.put("isPublished", Constants.BOOLEAN_YES);//发布的			
			Page objPage = new Page(20, false, Page.ASC, "fillinDate desc,fileLevel,showOrder,resourceid");
			Page studyCenterFilesPage = teacherCourseFilesService.findTeacherFilesByCondition(condition, objPage);			
			model.addAttribute("studyCenterFilesList", studyCenterFilesPage.getResult());	
		} catch (Exception e) {		
			logger.error("加载知识园地出错:{}",e.fillInStackTrace());
		}
	}

	/**
	 * 获取特色资源
	 * @param courseId
	 * @param model
	 * @param condition
	 */
	private void getTeacherFilesList(String courseId, ModelMap model) {
		try {
			//特色资源
			Map<String, Object> condition = new HashMap<String, Object>();
			String studyCenterName = CacheAppManager.getSysConfigurationByCode("course.teacherfiles.studycenter").getParamValue();//知识园地
			String caseAnalysisName = CacheAppManager.getSysConfigurationByCode("course.teacherfiles.caseanalysis").getParamValue();//案例分析
			String lowPlainName = CacheAppManager.getSysConfigurationByCode("course.teacherfiles.lawplain").getParamValue();//司法解释
			Set<String> fileNames = new HashSet<String>();
			if(ExStringUtils.isNotBlank(studyCenterName)) {
				fileNames.add(studyCenterName);
			}
			findFilterFiles(courseId, caseAnalysisName, fileNames);	
			findFilterFiles(courseId, lowPlainName, fileNames);//排除文件夹	
			if(ExCollectionUtils.isNotEmpty(fileNames)) {
				condition.put("notInPatentFileNames", "'"+ExStringUtils.join(fileNames,"','")+"'");
			}
			condition.put("courseId", courseId);
			condition.put("fileType", 1);//只查询文件
			condition.put("isPublished", Constants.BOOLEAN_YES);//发布的			
			Page objPage = new Page(12, false, Page.ASC, "fillinDate desc,fileLevel,showOrder,resourceid");
			Page teacherFilesPage = teacherCourseFilesService.findTeacherFilesByCondition(condition, objPage);			
			model.addAttribute("teacherFilesList", teacherFilesPage.getResult());	
		} catch (Exception e) {		
			logger.error("加载特色资源出错:{}",e.fillInStackTrace());
		}
	}
	/**
	 * 课程信息
	 * @param courseId
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/resource/course/courseoverview.html")
	public String listCourseOverView(String courseId, String overviewid, ModelMap model) throws WebException{		
		if(ExStringUtils.isNotBlank(overviewid) || ExStringUtils.isNotBlank(courseId)){
			Course course = null;
			CourseOverview courseOverview = null;
			if(ExStringUtils.isNotBlank(overviewid)){
				courseOverview = courseOverviewService.get(overviewid);
				course = courseOverview.getCourse();
			} else if(ExStringUtils.isNotBlank(courseId)){
				course = courseService.get(courseId);
			}
			//加载课程简介
			List<CourseOverview> courseOverviewList = courseOverviewService.findByHql("from "+CourseOverview.class.getName()+" where isDeleted=0 and course.resourceid=? order by type,resourceid", course.getResourceid());
			if(ExStringUtils.isBlank(overviewid) && ExCollectionUtils.isNotEmpty(courseOverviewList)){
				courseOverview = courseOverviewList.get(0);
			}		
			model.addAttribute("courseOverview", courseOverview);	
			model.addAttribute("courseOverviewList", courseOverviewList);
			model.addAttribute("course", course);
		}
		return "/edu3/resources/site/courseoverview";
	}
	/**
	 * 课程学习
	 * @param courseId 
	 * @param resType 0-讲义,1-视频,2-随堂练习,3-练一练
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/resource/course/materesource.html")
	public String listMateResource(String courseId,String syllabusid, String resType, HttpServletRequest request, Page objPage, ModelMap model) throws WebException{		
		objPage.setOrder(Page.ASC);
		objPage.setOrderBy("showOrder,resourceid");		
		resType = ExStringUtils.defaultIfEmpty(resType, "0");
		Syllabus syllabus = null;
		SyllabusVo courseSyllabusVo = null;
		Course course = null;
		User user = SpringSecurityHelper.getCurrentUser();//当前用户
		if(ExStringUtils.isNotBlank(syllabusid)){
			syllabus = syllabusService.get(syllabusid);
			course = syllabus.getCourse();			
		} else if(ExStringUtils.isNotBlank(courseId)){
			course = courseService.get(courseId);
		}
		if(course != null){
			List<SyllabusVo> syllabusVoList = courseResourceService.findSyllabusVoForCourseResource(course.getResourceid(), "3".equals(resType)?"2":resType);
			//做相应处理，对应前台页面输出
			List<SyllabusVo> syllabusList = syllabusVoAfterSetting(syllabusVoList);
			model.addAttribute("syllabusList", syllabusList);			
			
			syllabus = getCurrentSyllabus(resType, syllabus, syllabusList,syllabusVoList);//获取当前显示的知识点	
			if(courseSyllabusVo ==null && syllabus !=null && ExCollectionUtils.isNotEmpty(syllabusVoList)){
				for (SyllabusVo syllabusVo : syllabusVoList) {
					if(syllabusVo.getResourceid().equals(syllabus.getResourceid())){
						courseSyllabusVo = syllabusVo;
						break;
					}					
				}	
			}
			//加载学习材料
			if(syllabus != null){					
				if(syllabus.getSyllabusLevel()==1L){//章，显示学习目标
					List<CourseLearningGuid> courseLearningGuidList = courseLearningGuidService.findByHql("from "+CourseLearningGuid.class.getName()+" where isDeleted=0 and syllabus.resourceid=? order by type ", syllabus.getResourceid());
					model.addAttribute("courseLearningGuidList", courseLearningGuidList);
					model.addAttribute("courseLearningGuidType", CacheAppManager.getSysConfigurationByCode("course.learningguid.guidetype").getParamValue());
				} else {//节，显示学习材料、习题
					Map<String, Object> condition = new HashMap<String, Object>();
					condition.put("syllabusId", syllabus.getResourceid());
					condition.put("isPublished", Constants.BOOLEAN_YES);
					List<String> mateTestType = null;
					if("2".equals(resType) || "3".equals(resType)){
						if("2".equals(resType)){//随堂练习
							objPage.setPageSize(10);
							objPage = activeCourseExamService.findActiveCourseExamByCondition(condition, objPage);
							//学生用户	
							if(user != null && SpringSecurityHelper.isUserInRole(CacheAppManager.getSysConfigurationByCode("sysuser.role.student").getParamValue())){
								if(null!=user.getUserExtends()&&null!=user.getPropertys(UserExtends.USER_EXTENDCODE_DEFAULTROLLID)){
									String studentInfoId = user.getPropertys(UserExtends.USER_EXTENDCODE_DEFAULTROLLID).getExValue();//学籍id
									if(ExStringUtils.isNotBlank(studentInfoId)){//学生	
										model.addAttribute("isStudent", "Y");								
										List<ActiveCourseExam> examList = objPage.getResult();								
										getStudentExamAnswer(studentInfoId, syllabus, examList, model);//学生答题情况
									}
								}
							}						
						}
						String mt = ExStringUtils.trimToEmpty(request.getParameter("mt"));						
						condition.put("mateTypes", "'9','11','12','13'");
						condition.put("orderBy", "mateType,resourceid");
						List<MateResource> testMateList = mateResourceService.findMateResourceByCondition(condition);
						model.addAttribute("testMateList", testMateList);
						
						Set<String> mateTypeList = new LinkedHashSet<String>();
						if(ExCollectionUtils.isNotEmpty(testMateList)){							
							for (MateResource m : testMateList) {
								mateTypeList.add(m.getMateType());
							}
						}	
						if(ExStringUtils.isBlank(mt) && ExCollectionUtils.isNotEmpty(mateTypeList)){
							mt = new ArrayList<String>(mateTypeList).get(0);
						}
						model.addAttribute("mt", mt);
						model.addAttribute("mateTypeList", mateTypeList);
					} else {//学习材料
						objPage.setPageSize(1);
						String mateTypes = "0".equals(resType)?"'6'":"'2','3','4','8','10'";
						condition.put("mateTypes", mateTypes);
						objPage = mateResourceService.findMateResourceByCondition(condition, objPage);
						
						String userAgent = ExStringUtils.trimToEmpty(request.getHeader("User-Agent"));
						String isIPadOrIphone = Constants.BOOLEAN_NO;
						if(ExStringUtils.containsIgnoreCase(userAgent, "ipad") || ExStringUtils.containsIgnoreCase(userAgent, "iphone")){
							isIPadOrIphone = Constants.BOOLEAN_YES;
						}
						model.addAttribute("isIPadOrIphone", isIPadOrIphone);//是否ipad或iphone
					}
				}				
			}
			model.addAttribute("course", course);
			model.addAttribute("syllabus", syllabus);
			model.addAttribute("courseSyllabusVo", courseSyllabusVo);
		}		
		model.addAttribute("resType",resType);
		model.addAttribute("resPage",objPage);
		model.addAttribute("user",user);
		return "/edu3/resources/site/materesource";
	}

	/**
	 * 获取当前要显示的知识节点:
	 * 1.如果第一次进入学习，默认取第一个'章';
	 * 2.如果是'章'并且有学习目标并且点击讲义，显示学习目标，其他情况取该章下的第一'
	 * @param resType
	 * @param syllabus
	 * @param syllabusList
	 * @return
	 */
	private Syllabus getCurrentSyllabus(String resType, Syllabus syllabus, List<SyllabusVo> syllabusList,List<SyllabusVo> syllabusVoList) {
		boolean isExists = false;
		if(syllabus != null && ExCollectionUtils.isNotEmpty(syllabusList)){
			List<SyllabusVo> list = (syllabus.getSyllabusLevel()==1L)?syllabusList:syllabusVoList;
			for (SyllabusVo vo : list) {				
				if(vo.getResourceid().equals(syllabus.getResourceid())){
					isExists = true;
					break;
				}
			}
		}
		if(!isExists) {
			syllabus=null;
		}
		if(syllabus == null && ExCollectionUtils.isNotEmpty(syllabusList)){
			for (SyllabusVo syllabusVo : syllabusList) {
				syllabus = syllabusService.get(syllabusVo.getResourceid());//取第一个知识点					
				break;
			}				
		}			
		if(syllabus != null && syllabus.getSyllabusLevel()==1L && ExCollectionUtils.isNotEmpty(syllabusList)){
			for (SyllabusVo syllabusVo : syllabusList) {
				if(syllabus.getResourceid().equals(syllabusVo.getResourceid())){
					if((syllabusVo.getGuidCount()==0 || "1".equals(resType) || "2".equals(resType)) && syllabusVo.getChilds().size()>0){//章没有学习目标,指向第一节
						syllabus = syllabusService.get(new ArrayList<SyllabusVo>(syllabusVo.getChilds()).get(0).getResourceid());//取第一个节点
						break;
					}
				}
			}				
		}
		if(syllabus != null && syllabus.getSyllabusLevel()==2L && ExCollectionUtils.isNotEmpty(syllabusList)){
			for (SyllabusVo syllabusVo : syllabusList) {
				if(syllabus.getParent().getResourceid().equals(syllabusVo.getResourceid())){
					for (SyllabusVo child : syllabusVo.getChilds()) {
						if(child.getResourceid().equals(syllabus.getResourceid())
								&& child.getResCount()==0 && child.getChilds().size()>0){
							syllabus = syllabusService.get(new ArrayList<SyllabusVo>(child.getChilds()).get(0).getResourceid());
						}
					}
				}
			}
		}
		if(syllabus != null && ExCollectionUtils.isNotEmpty(syllabusVoList)){
			for (SyllabusVo syllabusVo : syllabusVoList) {
				if(syllabus.getResourceid().equals(syllabusVo.getResourceid())
						||syllabus.getParent().getResourceid().equals(syllabusVo.getResourceid())
						||syllabus.getSyllabusLevel()==3L&&syllabus.getParent().getParent()!=null
						&&syllabus.getParent().getParent().getResourceid().equals(syllabusVo.getResourceid())){
					//如果选择的知识节点是但前节点或子节点
					syllabusVo.setShowMenu(true);
				}
			}
		}
		return syllabus;
	}
	
	private List<SyllabusVo> syllabusVoAfterSetting(List<SyllabusVo> syllabusList){
		List<SyllabusVo> syllabusVoList1 = new ArrayList<SyllabusVo>();
		List<SyllabusVo> syllabusVoList2 = new ArrayList<SyllabusVo>();
		List<SyllabusVo> syllabusVoList3 = new ArrayList<SyllabusVo>();
		if(ExCollectionUtils.isNotEmpty(syllabusList)){			
			for (SyllabusVo s : syllabusList) {				
				if(s.getSyllabusLevel()==1L){//章
					syllabusVoList1.add(s);
				} else if(s.getSyllabusLevel()==2L){//节
					syllabusVoList2.add(s);
				} else {
					syllabusVoList3.add(s);
				}
			}
			
			addSyllabusChildToParent(syllabusVoList3, syllabusVoList2);//把点加入节
			addSyllabusChildToParent(syllabusVoList2, syllabusVoList1);//把节加入章	
			List<SyllabusVo> rList = new ArrayList<SyllabusVo>();
			for (SyllabusVo vo : syllabusList) {//去掉无资源节点
				if(vo.isRemove()){
					rList.add(vo);
				}
			}
			syllabusList.removeAll(rList);
		}
		return syllabusVoList1;
	}
	
	private void addSyllabusChildToParent(List<SyllabusVo> childList, List<SyllabusVo> parentList){
		for (SyllabusVo vo : parentList) {//把下级知识点加入上级知识点的child列表中
			for (SyllabusVo child : childList) {
				if(!child.isRemove() && child.getParentId().equals(vo.getResourceid())){//有学习材料的节点
					vo.getChilds().add(child);
				}
			}
		}
		List<SyllabusVo> rList = new ArrayList<SyllabusVo>();
		for (SyllabusVo vo : parentList) {
			if(vo.isRemove()){
				rList.add(vo);
			}
		}
		parentList.removeAll(rList);//去除无资源的章节点
	}
	/**
	 * 学生答题情况
	 * @param studentInfoId
	 * @param syllabus
	 * @param examList
	 * @param model
	 */
	private void getStudentExamAnswer(String studentInfoId, Syllabus syllabus,	List<ActiveCourseExam> examList, ModelMap model) {
		Map<String,Object> results = new HashMap<String,Object>();
		Map<String, StudentActiveCourseExam> answers = new HashMap<String, StudentActiveCourseExam>();
		if(ExCollectionUtils.isNotEmpty(examList)){
			List<String> ids = new ArrayList<String>();
			int y1 = 0;
			for (ActiveCourseExam exam : examList) {
				ids.add(exam.getResourceid());
				if(!"6".equals(exam.getCourseExam().getExamType())){
					y1++;
				}
			}
			Map<String,Object> condition1 = new HashMap<String,Object>();
			condition1.put("syllabusId", syllabus.getResourceid());
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
			results.put("y1", y1);//当前页题目数
			results.put("y2", stuanswers.size());//当前页已做题目数
			results.put("y3", currentFinishedCount);//当前页已提交题目数
			results.put("y4", currentCorrectCount);//当前页答对题目数
			
			try {
				StudentLearnPlan plan = studentLearnPlanService.getStudentLearnPlanByCourse(syllabus.getCourse().getResourceid(),studentInfoId, "studentId");
				if(plan != null){
					String isActiveCourseExamOpen = Constants.BOOLEAN_NO;
					String msg = "随堂练习还未开放";
					if(plan.getStatus()==3 && ExStringUtils.isBlank(plan.getIsRedoCourseExam()) || Constants.BOOLEAN_NO.equals(plan.getIsRedoCourseExam())){//考试结束
						msg = "随堂练习已经截止";
					} else {
						Grade grade = gradeService.findUnique("from "+Grade.class.getSimpleName()+" where isDeleted = ? and isDefaultGrade = ?", 0,Constants.BOOLEAN_YES);
						if(grade!=null){
							LearningTimeSetting setting = learningTimeSettingService.findLearningTimeSetting(grade.getYearInfo().getResourceid(), grade.getTerm());
							if(setting!=null){
								Date today = new Date();
								if(today.before(setting.getStartTime())){//学习还未开始
									msg = "随堂练习还未开放";
								} else if(today.after(setting.getEndTime())){
									msg = "随堂练习已经截止";
								} else {
									isActiveCourseExamOpen = Constants.BOOLEAN_YES;
								}
							} 
						}
					}	
					model.addAttribute("msg", msg);
					model.addAttribute("isActiveCourseExamOpen", isActiveCourseExamOpen);
				}	
			} catch (Exception e) {
				logger.error("获取网上学习时间出错:{}", e.fillInStackTrace());
			}	
		}
		
		Long allCount = studentActiveCourseExamService.finishedActiveCourseExam(syllabus.getResourceid(), studentInfoId,"all");//总的随堂练习
		Long allSaveCount = studentActiveCourseExamService.finishedActiveCourseExam(syllabus.getResourceid(), studentInfoId,"done");//已做的随堂练习
		Long allFinishedCount = studentActiveCourseExamService.finishedActiveCourseExam(syllabus.getResourceid(), studentInfoId,"finished");//已提交的随堂练习
		Long allCorrectCount = studentActiveCourseExamService.finishedActiveCourseExam(syllabus.getResourceid(), studentInfoId,"correct");
		results.put("x1", allCount);//总题目数
		results.put("x2", allSaveCount);//总已做题目数
		results.put("x3", allFinishedCount);//总已提交题目数
		results.put("x4", allCorrectCount);//总答对题目数
		
		model.addAttribute("answers", answers);//已答答案
		model.addAttribute("results", results);
	}
	
	/**
	 * 保存学生随堂练习回答情况
	 * @param syllabusId
	 * @param type 提交类型:save-保存,submit-提交,submit_done-提交已做,test-测试
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/resource/course/studentactivecourseexam/save.html")
	public void saveStudentActiveCourseExam(String syllabusId, String type, HttpServletRequest request,HttpServletResponse response) throws WebException {
		Map<String,Object> map = new HashMap<String, Object>();		
		try {			
			String[] activeCourseExamId = request.getParameterValues("activeCourseExamId"); 
			List<StudentActiveCourseExam> list = new ArrayList<StudentActiveCourseExam>();//答题情况列表
			
			int num = 0;//答对题目数
			User user = SpringSecurityHelper.getCurrentUser();
			String roleCode = CacheAppManager.getSysConfigurationByCode("sysuser.role.student").getParamValue();
			
			if(activeCourseExamId != null && activeCourseExamId.length > 0){
				boolean isStudent = false;
				StudentInfo studentInfo = null;
				if(user != null && SpringSecurityHelper.isUserInRole(roleCode)){//学生	
					studentInfo = studentInfoService.getStudentInfoByUser(user);
					isStudent = true;
				}				
				for(int index=0;index<activeCourseExamId.length;index++){ //处理每道练习题	
					String[] answers = request.getParameterValues("answer"+activeCourseExamId[index]);
					String stuAnswer = ExStringUtils.trimToEmpty(ExStringUtils.join(answers, ""));
					if(!"submit".equals(type) && ExStringUtils.isBlank(stuAnswer)){//保存时略过未作答的题目							
						continue;
					}
					
					ActiveCourseExam activeCourseExam = activeCourseExamService.get(activeCourseExamId[index]);					
					if(isStudent && !"test".equals(type)){//学生	
						StudentActiveCourseExam stuActiveCourseExam = new StudentActiveCourseExam();					
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
						
						if("submit".equals(type)){ //提交 或 测试
							String stu_answer = ExStringUtils.trimToEmpty(stuActiveCourseExam.getAnswer());
							String answer = ExStringUtils.trimToEmpty(activeCourseExam.getCourseExam().getAnswer());
							if("3".equals(activeCourseExam.getCourseExam().getExamType())){//判断题
								stu_answer = CourseExam.covertToCorrectAnswer(stu_answer);
								answer = CourseExam.covertToCorrectAnswer(answer);
							} 
							if(stu_answer.equalsIgnoreCase(answer)){
								stuActiveCourseExam.setIsCorrect(Constants.BOOLEAN_YES);
								Long exam_score = activeCourseExam.getScore();
								stuActiveCourseExam.setResult((exam_score!=null)?exam_score.doubleValue():0.0);	
								num ++;
							} else {
								stuActiveCourseExam.setIsCorrect(Constants.BOOLEAN_NO);	
								stuActiveCourseExam.setResult(0.0);
							}
						}	
						list.add(stuActiveCourseExam);	
					} else {//匿名用户，非学生用户，只做测试
						String stu_answer = ExStringUtils.trimToEmpty(stuAnswer);
						String answer = ExStringUtils.trimToEmpty(activeCourseExam.getCourseExam().getAnswer());
						if("3".equals(activeCourseExam.getCourseExam().getExamType())){//判断题
							stu_answer = CourseExam.covertToCorrectAnswer(stu_answer);
							answer = CourseExam.covertToCorrectAnswer(answer);
						} 
						if(stu_answer.equalsIgnoreCase(answer)){
							num ++;
						}
					}
				}
				if(user != null && SpringSecurityHelper.isUserInRole(roleCode) && studentInfo != null){//学生
					studentActiveCourseExamService.saveAllStudentActiveCourseExam(list,studentInfo, syllabusId, type);
				}				
				map.put("statusCode", 200);
				if("submit".equals(type)){ //提交
					map.put("message", "本次测试答对"+num+"道题,成绩已累积。");
				} else if("save".equals(type)){						
					map.put("message", "成功保存答案。");
				} else if("submit_done".equals(type)){	
					map.put("message", "成功提交已做的题目。");
				} else if("test".equals(type)){//匿名用户测试
					map.put("message", "本次测试提交"+activeCourseExamId.length+"道题, 答对"+num+"道题。");
				} else {
					map.put("message", "成功提交本练习所有题目。");
				}
			}
		}catch (Exception e) {
			logger.error("保存学生随堂练习答题情况出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "提交失败，请尝试重新提交！"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));	
	}
	/**
	 * 作业批次列表
	 * @param courseId
	 * @param model
	 * @param objPage
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/resource/course/exercisebatch.html")
	public String listExerciseBatch(String courseId, ModelMap model, Page objPage) throws WebException {
		if(ExStringUtils.isNotBlank(courseId)){
			Map<String,Object> condition = new HashMap<String,Object>();
			condition.put("courseId", courseId);
			condition.put("notstatus", 0);			
			
			User user = SpringSecurityHelper.getCurrentUser();
			boolean isStudent = false;
			String studentId = "";
			if(user != null && SpringSecurityHelper.isUserInRole(CacheAppManager.getSysConfigurationByCode("sysuser.role.student").getParamValue())){//学生	
				isStudent = true;
				if(null!=user.getUserExtends()&&null!=user.getPropertys(UserExtends.USER_EXTENDCODE_DEFAULTROLLID)){
					studentId = user.getPropertys(UserExtends.USER_EXTENDCODE_DEFAULTROLLID).getExValue();
				}
				StudentLearnPlan plan = studentLearnPlanService.getStudentLearnPlanByCourse(courseId,studentId, "studentId");
				if(plan != null){
					if(plan.getStatus()==3 && !Constants.BOOLEAN_YES.equals(plan.getIsRedoCourseExam())){//完成学习
						condition.put("yearInfoId", plan.getYearInfo().getResourceid());		
						condition.put("term", plan.getTerm());	
					} else {
						/*Grade grade = gradeService.getDefaultGrade();
						if(grade!=null){
							condition.put("yearInfoId", grade.getYearInfo().getResourceid());		
							condition.put("term", grade.getTerm());	
						}*/
						String yearTerm = CacheAppManager.getSysConfigurationByCode("sysCurrentGrade").getParamValue();
						String yearInfoId = "";
						String term = "";
						if (null!=yearTerm) {
							String[] ARRYyterm = yearTerm.split("\\.");
							yearInfoId = yearInfoService.getByFirstYear(Long.parseLong(ARRYyterm[0])).getResourceid();
							term = ARRYyterm[1];
							
							condition.put("yearInfoId",yearInfoId);		
							condition.put("term", term);	
						}
					}
				}
			} else {
				/*Grade grade = gradeService.getDefaultGrade();
				if(grade!=null){
					condition.put("yearInfoId", grade.getYearInfo().getResourceid());		
					condition.put("term", grade.getTerm());	
				}*/
				String yearTerm = CacheAppManager.getSysConfigurationByCode("sysCurrentGrade").getParamValue();
				String yearInfoId = "";
				String term = "";
				if (null!=yearTerm) {
					String[] ARRYyterm = yearTerm.split("\\.");
					yearInfoId = yearInfoService.getByFirstYear(Long.parseLong(ARRYyterm[0])).getResourceid();
					term = ARRYyterm[1];
					
					condition.put("yearInfoId",yearInfoId);		
					condition.put("term", term);	
				}
			}
			condition.put("orderby", "c.startDate,c.resourceid");
			List<ExerciseBatch> exerciseBatchs = exerciseBatchService.findExerciseBatchByCondition(condition);
			//加载学生作业平均分
			if(isStudent && ExCollectionUtils.isNotEmpty(exerciseBatchs) && ExStringUtils.isNotEmpty(studentId)){
				List<String> ids = new ArrayList<String>();
				for (ExerciseBatch exerciseBatch : exerciseBatchs) {
					ids.add(exerciseBatch.getResourceid());
				}	
				Map<String, StudentExercise> stuExercises = new HashMap<String, StudentExercise>();
				Map<String,Object> values = new HashMap<String,Object>();
				values.put("studentInfoId", studentId);
				values.put("exerciseResult", Constants.BOOLEAN_YES);
				values.put("exerciseBatchIds", ids);
				List<StudentExercise> list = studentExerciseService.findStudentExerciseByCondition(values);
				for (StudentExercise studentExercise : list) {
					stuExercises.put(studentExercise.getExerciseBatch().getResourceid(), studentExercise);
				}
				model.addAttribute("stuExercises", stuExercises);
				String yearInfoId = (condition.containsKey("yearInfoId") && condition.get("yearInfoId") != null)?condition.get("yearInfoId").toString():"";
				String term = (condition.containsKey("term") && condition.get("term") != null)?condition.get("term").toString():"";
				model.addAttribute("stuExercisesAvg", studentExerciseService.avgStudentExerciseResult(courseId, studentId,yearInfoId,term));
			}	
					
			model.addAttribute("exerciseBatchs", exerciseBatchs);
			model.addAttribute("condition", condition);
			model.addAttribute("user",user);
			model.addAttribute("course",courseService.get(courseId));
		}
		return "/edu3/resources/site/exercisebatch";
	}
	
	/**
	 * 课程作业
	 * @param exerciseBatchId
	 * @param model
	 * @return
	 * @throws WebException
	 */	
	@RequestMapping("/resource/course/exercise.html")
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
			boolean isStudent = user != null && SpringSecurityHelper.isUserInRole(CacheAppManager.getSysConfigurationByCode("sysuser.role.student").getParamValue());
					
			if(isStudent){//学生	,加载作答答案
				model.addAttribute("isStudent", Constants.BOOLEAN_YES);
				model.addAttribute("storeDir", user.getUsername());	
				
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
			model.addAttribute("user",user);
			model.addAttribute("course",exerciseBatch.getCourse());
		}
		return "/edu3/resources/site/exercise";
	}
	
	/**
	 * 保存学生作业回答情况
	 * @param exerciseBatchId
	 * @param type 保存/提交/测试
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/resource/course/exercise/save.html")
	public void saveStudentExercise(String exerciseBatchId,String type,String exerciseResultId,HttpServletRequest request,HttpServletResponse response) throws WebException {
		Map<String,Object> map = new HashMap<String, Object>();		
		try {	
			if(ExStringUtils.isNotBlank(exerciseBatchId)){
				User user = SpringSecurityHelper.getCurrentUser();
				boolean isStudent = user != null && SpringSecurityHelper.isUserInRole(CacheAppManager.getSysConfigurationByCode("sysuser.role.student").getParamValue());
				StudentInfo studentInfo = isStudent ? studentInfoService.getStudentInfoByUser(user) : null;				
				
				ExerciseBatch exerciseBatch = exerciseBatchService.get(exerciseBatchId);
				String[] exerciseIds = request.getParameterValues("exerciseId");
				int correctNum = 0;
				if(exerciseIds!=null&&exerciseIds.length>0){	
					Date currentDay = new Date();
					List<StudentExercise> studentExercises = new ArrayList<StudentExercise>();	
					List<StudentExercise> allAnswers = studentInfo==null ? null: studentExerciseService.findByCriteria(Restrictions.eq("isDeleted",0),Restrictions.eq("studentInfo.resourceid",studentInfo.getResourceid()),Restrictions.eq("exerciseBatch.resourceid",exerciseBatchId));
					for(int index=0;index<exerciseIds.length;index++){							
						String[] answers = request.getParameterValues("answer"+exerciseIds[index]);
						String[] attachIds = request.getParameterValues("uploadfileid"+exerciseIds[index]);
						String stuAnswer = ExStringUtils.trimToEmpty(ExStringUtils.join(answers, ""));
						
						Exercise exercise = exerciseService.get(exerciseIds[index]);
						if(isStudent && !"test".equals(type)){//学生
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
							studentExercises.add(studentExercise);
						} else {//匿名用户，测试
							if("1".equals(exerciseBatch.getColType())){//测试主观题
								String stu_answer = ExStringUtils.trimToEmpty(stuAnswer);
								String answer = ExStringUtils.trimToEmpty(exercise.getCourseExam().getAnswer());
								if("3".equals(exercise.getCourseExam().getExamType())){//判断题
									stu_answer = CourseExam.covertToCorrectAnswer(stu_answer);
									answer = CourseExam.covertToCorrectAnswer(answer);
								} 
								if(stu_answer.equalsIgnoreCase(answer)){
									correctNum++;
								}
							}
						}
					}
					if(isStudent && !"test".equals(type)){//创建在线作业总分记录，保存答案	
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
					}//end 创建在线作业总分记录，保存答案			
				}				
				map.put("statusCode", 200);
				map.put("message", ("test".equals(type) && "1".equals(exerciseBatch.getColType()))?"本次作业作对"+correctNum+"道题.":("submit".equals(type)?"成功提交本次作业！":"成功保存作答答案"));
			}			
		}catch (Exception e) {
			logger.error("保存学生作业回答情况出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败！"+e.getMessage());
		}
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
	 * 优秀作业展示
	 * @param courseId
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/resource/course/exerciseshow.html")
	public String listStudentExercise(String courseId, ModelMap model) throws WebException {
		if(ExStringUtils.isNotEmpty(courseId)){
			Map<String,Object> condition = new HashMap<String,Object>();//查询条件
			condition.put("courseId", courseId);//课程
			condition.put("isExcell", Constants.BOOLEAN_YES);//优秀作业	
			condition.put("exerciseResult", "null");//作业总评项
			condition.put("orderBy", "exerciseBatch.startDate desc,studentInfo.studyNo,resourceid");	
			
			List<StudentExercise> list = studentExerciseService.findStudentExerciseByCondition(condition);
			model.addAttribute("studentExerciseList", list);
			model.addAttribute("course",courseService.get(courseId));
		}
		model.addAttribute("user", SpringSecurityHelper.getCurrentUser());
		return "/edu3/resources/site/exerciseshow";
	}
	/**
	 * 查看优秀作业
	 * @param exerciseid
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/resource/course/studentexercise.html")
	public String viewStudentExercise(String exerciseid, ModelMap model) throws WebException {
		if(ExStringUtils.isNotBlank(exerciseid)){
			StudentExercise studentExerciseResult = studentExerciseService.get(exerciseid);
			List<Attachs> resultAttachs = attachsService.findAttachsByFormId(exerciseid);
			studentExerciseResult.setAttachs(resultAttachs);
			model.addAttribute("studentExerciseResult", studentExerciseResult);					
			
			ExerciseBatch exerciseBatch = studentExerciseResult.getExerciseBatch();
			if("2".equals(exerciseBatch.getColType())){//主观题
				for (Exercise exercise : exerciseBatch.getExercises()) {
					List<Attachs> attachs = attachsService.findAttachsByFormId(exercise.getResourceid());
					exercise.setAttachs(attachs);
				}
			}
			//查询作答答案
			StudentInfo studentInfo = studentExerciseResult.getStudentInfo();
			List<StudentExercise> list = studentExerciseService.findByHql(" from "+StudentExercise.class.getName()+" where isDeleted=0 and exerciseBatch.resourceid=? and studentInfo.resourceid=? ", exerciseBatch.getResourceid(),studentInfo.getResourceid());
			Map<String, StudentExercise> answers = new HashMap<String, StudentExercise>();
			for (StudentExercise studentExercise : list) {
				if(studentExercise.getExercise()==null){//评分项
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
			model.addAttribute("course",exerciseBatch.getCourse());
		}
		return "/edu3/resources/site/studentexercise";
	}
	
	/**
	 * 缓存静态资源内容到缓存
	 * @param request
	 * @param response
	 */
	@RequestMapping("/resource/course/recached.html")
	public void putWebContentToCache(HttpServletRequest request, HttpServletResponse response){
		StringBuilder sb = new StringBuilder();
		Map<String,Object> map = new HashMap<String, Object>();		
		
		sb.append("select t.mateurl from edu_lear_mate t ,edu_teach_syllabustree s ,edu_base_course c ")
		.append("where s.resourceid = t.syllabustreeid and ")
		.append("s.courseid=c.resourceid and c.isqualityresource=:isqualityresource ")
		.append("and t.matetype in ('6','9','11','12','13') and t.isdeleted=:isdeleted ")
		.append("union all ")
		.append("select m.mateurl from edu_lear_mocktest m,edu_base_course c1 where m.isdeleted=:isdeleted ")
		.append("and m.courseid = c1.resourceid and c1.isqualityresource=:isqualityresource ")
		.append("union all ")
		.append("select f.fileurl from edu_lear_files f,edu_base_course c2 where ")
		.append("f.courseid=c2.resourceid and c2.isqualityresource=:isqualityresource ") 
		.append("and f.fileurl is not null and f.fileurl like '%.html' and f.isdeleted=:isdeleted ");
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("isqualityresource", Constants.BOOLEAN_YES);
		parameters.put("isdeleted", 0);
		
		
		try{
			//MEMCACHE					
			List<Map<String, Object>> mateUrlList =  baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sb.toString(), parameters);
			String content = "";
			long startTime = 0L;
			if(ExCollectionUtils.isNotEmpty(mateUrlList)){
				for(Map<String,Object> urlMap : mateUrlList){
					startTime = System.currentTimeMillis();
					String url  = urlMap.get("mateurl").toString();
					//获取远程内容
					 content = SnatchWebContentUtils.getWebContent(url, "UTF-8");					 
					 String baseUrl = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
					 content = replacePageUrl(content, baseUrl, url);
					 //放入缓存
					 memcachedManager.put(url, 0, content);
					 logger.info("put url :"+url +" to cache, total time :"+(System.currentTimeMillis()-startTime));
				}
			}
			map.put("statusCode", 200);
		}catch (Exception e) {
			logger.error("缓存网页出错："+e.fillInStackTrace());
			map.put("statusCode", 300);
		}
	   renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 读取网页内容输出到iframe
	 * @param exerciseid
	 * @param model
	 * @throws WebException
	 */
	@RequestMapping("/resource/course/transfer.html")
	public void transferWebContentToIframe(String url, HttpServletRequest request, HttpServletResponse response) throws WebException {
		String content = null;
		if(ExStringUtils.isNotBlank(url)){
			try {
				//先从缓存中获取			
				content = memcachedManager.get(url);
				if(ExStringUtils.isBlank(content)){
					content = SnatchWebContentUtils.getWebContent(url, "UTF-8");
					 
					 String baseUrl = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
					 content = replacePageUrl(content, baseUrl, url);
					 memcachedManager.put(url, 0, content);
				}
				 				
			} catch (IOException e) {
				logger.error("加载网页失败:"+url, e.fillInStackTrace());
			}          
		} else {
			content = "<center><b>数据加载中，请稍等...<b/></center>";
		}
		renderHtml(response, ExStringUtils.trimToEmpty(content));
	}
	/**
	 * 替换样式，图片，网页地址为绝对地址
	 * http://cs.scutde.net/Courses/course_10/page/c010300.html
	 * course_10--
     *       -css
     *       -images
     *       -page
     *           -img
     *           -c001001.html
     *           -c......html
	 * @param pageContent
	 * @param baseUrl
	 * @param url
	 * @return
	 */
	private String replacePageUrl(String pageContent,String baseUrl,String url) {
		try {
			if(ExStringUtils.isNotBlank(pageContent)){
				String bUrl = ExStringUtils.substringBefore(url, "/");//外部链接服务器地址
				String preUrl = ExStringUtils.substringBeforeLast(ExStringUtils.substringBefore(url, ".html"), "/")+"/";//上级目录
				String pUrl = ExStringUtils.substringBeforeLast(ExStringUtils.substringBeforeLast(preUrl, "/"), "/")+"/";//上上级目录
				String preIframeUrl = baseUrl + "/resource/course/transfer.html?url=";
				
				pageContent = pageContent.replace("href=\"../", "href=\""+pUrl).replace("background=\"../", "background=\""+pUrl).replace("src=\"../", "src=\""+pUrl);
				pageContent = pageContent.replace("src=\"img/", "src=\""+preUrl+"img/");
				
				StringBuffer sb = new StringBuffer();
				//Pattern pattern = Pattern.compile("href=\"[^\"]+?\\u002Ehtml\"");
				Pattern pattern = Pattern.compile("(href|src|background)=\"[^\"]+?\"(?i)");
				Matcher matcher = pattern.matcher(pageContent);		
				while(matcher.find()){			
					String str = matcher.group();		
					if(ExStringUtils.startsWithIgnoreCase(str, "href=\"http:")||ExStringUtils.startsWithIgnoreCase(str, "src=\"http:")||ExStringUtils.startsWithIgnoreCase(str, "background=\"http:"))	{
						if(str.contains(bUrl)&& str.contains(".html")){//把同域的相对地址替换成绝对地址
							matcher.appendReplacement(sb, str.replace("href=\"", "href=\""+preIframeUrl));
						}						
					} else {
						if(ExStringUtils.containsIgnoreCase(str, "href") && ExStringUtils.containsIgnoreCase(str, ".html")){
							matcher.appendReplacement(sb, str.replace("href=\"", "href=\""+preIframeUrl+preUrl));
						} else {
							matcher.appendReplacement(sb, str.replace("href=\"", "href=\""+preUrl).replace("src=\"", "src=\""+preUrl).replace("background=\"", "background=\""+preUrl));
						}						
					}
				}	
				matcher.appendTail(sb);
				pageContent = sb.toString();
			}
		} catch (Exception e) {
			logger.error("替换网页链接出错:{}",e.fillInStackTrace());
		}	
		return pageContent;
	}
	/**
	 * 在学自测
	 * @param courseId
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/resource/course/mocktest.html")
	public String listMockTest(String courseId,String mocktestid, ModelMap model) throws WebException {
		CourseMockTest courseMockTest = null;
		Course course = null;
		if(ExStringUtils.isNotBlank(mocktestid)){
			courseMockTest = courseMockTestService.get(mocktestid);
			course = courseMockTest.getCourse();
		} else if(ExStringUtils.isNotBlank(courseId)) {
			course = courseService.get(courseId);
		}
		if(course != null){
			List<CourseMockTest> courseMockTestList = courseMockTestService.findByHql("from "+CourseMockTest.class.getName()+" where isDeleted=0 and course.resourceid=? order by resourceid ", course.getResourceid());
			if(courseMockTest==null && ExCollectionUtils.isNotEmpty(courseMockTestList)){
				courseMockTest = courseMockTestList.get(0);
			}
			model.addAttribute("courseMockTestList",courseMockTestList);
		}
		model.addAttribute("courseMockTest",courseMockTest);
		model.addAttribute("course",course);
		return "/edu3/resources/site/mocktest";
	}
	/**
	 * 随堂问答、常见问题、问题反馈
	 * @param courseId
	 * @param syllabusid
	 * @param type
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/resource/course/ask.html")
	public String listAsk(String courseId,String syllabusid, String type, ModelMap model) throws WebException {
		User user = SpringSecurityHelper.getCurrentUser();	
		type = ExStringUtils.defaultIfEmpty(type, "ask");
		model.addAttribute("askType", type);
		if(user != null){				
			Map<String,Object> condition = new HashMap<String,Object>();//查询条件
			if(ExStringUtils.isNotEmpty(courseId)) {
				condition.put("courseId", courseId);
			}
			if(ExStringUtils.isNotEmpty(syllabusid)) {
				condition.put("syllabusId", syllabusid);
			}
			if("ask".equals(type)){//随堂问答
				if(ExStringUtils.isNotEmpty(syllabusid)){
					Syllabus syllabus = syllabusService.get(syllabusid);
					model.addAttribute("syllabus", syllabus);					
					
					condition.put("topicType", "2");//随堂问答	
					condition.put("sectionCode", CacheAppManager.getSysConfigurationByCode("web.bbs.sectioncode").getParamValue());
					condition.put("fillinManId", user.getResourceid());//发表人
					List<BbsTopic> topicList = bbsTopicService.findBbsTopicByCondition(condition);
					model.addAttribute("topicList", topicList);
				}	
			} else {
				if(ExStringUtils.isNotEmpty(courseId)){
					Course course = courseService.get(courseId);
					model.addAttribute("course", course);					
					
					if("faq".equals(type)){//常见问题
						condition.put("topicType", "2");
						condition.put("tags", "FAQ");
						condition.put("notEmptySyllabus", "Y");
						condition.put("sectionCode", CacheAppManager.getSysConfigurationByCode("web.bbs.sectioncode").getParamValue());	
					} else if("feedback".equals(type)){//反馈
						condition.put("fillinManId", user.getResourceid());//发表人
						condition.put("sectionCode", CacheAppManager.getSysConfigurationByCode("web.bbs.sectioncode.feedback").getParamValue());
					}
					List<BbsTopic> topicList = bbsTopicService.findBbsTopicByCondition(condition);
					model.addAttribute("topicList", topicList);
				}	
			}				
			
			model.addAttribute("storeDir", user.getUsername());
		}		
		return "/edu3/resources/site/ask";
	}
	/**
	 * 随堂问答、问题反馈
	 * @param bbsTopic
	 * @param courseId
	 * @param syllabusid
	 * @param askType
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/resource/course/ask/save.html")
	public void saveAsk(BbsTopic bbsTopic,String courseId,String syllabusid, String askType,HttpServletRequest request,HttpServletResponse response) throws WebException {
		Map<String,Object> map = new HashMap<String, Object>();	
		try {	
			User user = SpringSecurityHelper.getCurrentUser();
			if(user != null){				
				Course course = null;
				Syllabus syllabus = null;
				if(ExStringUtils.isNotBlank(syllabusid)){
					syllabus = syllabusService.get(syllabusid);
					course = syllabus.getCourse();
				} else if(ExStringUtils.isNotBlank(courseId)){
					course =courseService.get(courseId);
				}
				bbsTopic.setTitle(ExStringUtils.trimToEmpty(bbsTopic.getTitle()));
				bbsTopic.setFillinMan(user.getCnName());
				bbsTopic.setCourse(course);
				bbsTopic.setSyllabus(syllabus);
				bbsTopic.setTopicType("2");//设置类型为提问
				bbsTopic.setFillinDate(new Date());
				bbsTopic.setStatus(0);//普通帖	
				bbsTopic.setLastReplyDate(bbsTopic.getFillinDate());
				bbsTopic.setLastReplyMan(user.getCnName());
				
				
				String sectionCode = CacheAppManager.getSysConfigurationByCode("web.bbs.sectioncode").getParamValue();//获取版块编码
				if("feedback".equals(askType)){
					sectionCode = CacheAppManager.getSysConfigurationByCode("web.bbs.sectioncode.feedback").getParamValue();
					bbsTopic.setTopicType("6");
				}
				BbsSection bbsSection = bbsSectionService.findUniqueByProperty("sectionCode", sectionCode);
				bbsTopic.setBbsSection(bbsSection);
				Integer topicCount = bbsSection.getTopicCount();
				bbsSection.setTopicCount(topicCount==null?1:topicCount.intValue()+1);
					
				bbsTopicService.saveOrUpdateBbsTopic(bbsTopic, null,null);
				map.put("statusCode", 200);
				map.put("message", "提交成功！");
			} else {
				map.put("statusCode", 300);
				map.put("message", "请登录后再操作！");
			}
		}catch (Exception e) {
			logger.error("保存随堂问答/问题反馈出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败！");
		}
		renderJson(response, JsonUtils.mapToJson(map));	
	}
	/**
	 * 查看随堂问答回复、反馈回复
	 * @param topicid
	 * @param askType
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/resource/course/ask/view.html")
	public String listAsk(String topicid, String askType, ModelMap model) throws WebException {
		if(ExStringUtils.isNotBlank(topicid)){				
			BbsTopic bbsTopic = bbsTopicService.get(topicid);
			
			Map<String,Object> condition = new HashMap<String,Object>();
			condition.put("topicid", topicid);
			
			List<BbsReply> replyList = bbsReplyService.findByHql("from "+BbsReply.class.getSimpleName()+" where isDeleted=0 and bbsTopic.resourceid=:topicid order by replyDate asc,showOrder,resourceid", condition);
			if(ExCollectionUtils.isNotEmpty(replyList)){
				for (BbsReply bbsReply : replyList) {
					if(!Constants.BOOLEAN_NO.equalsIgnoreCase(bbsReply.getIsAttachs())){
						List<Attachs> attachs = attachsService.findAttachsByFormId(bbsReply.getResourceid());
						bbsReply.setAttachs(attachs);
					}
				}
			}			
			
			model.addAttribute("bbsTopic", bbsTopic);	
			model.addAttribute("replyList", replyList);	
		}		
		model.addAttribute("askType", askType);	
		return "/edu3/resources/site/ask-view";
	}
	/**
	 * 学习笔记
	 * @param courseId
	 * @param syllabusid
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/resource/course/note.html")
	public String listLearningNote(String courseId, String syllabusid, ModelMap model) throws WebException {
		if(ExStringUtils.isNotBlank(syllabusid)){				
			User user = SpringSecurityHelper.getCurrentUser();
			boolean isStudent = user != null && SpringSecurityHelper.isUserInRole(CacheAppManager.getSysConfigurationByCode("sysuser.role.student").getParamValue());
			if(isStudent && null!=user.getUserExtends()&&null!=user.getPropertys(UserExtends.USER_EXTENDCODE_DEFAULTROLLID)){//学生
				String studentId = user.getPropertys(UserExtends.USER_EXTENDCODE_DEFAULTROLLID).getExValue();
				if(ExStringUtils.isNotEmpty(studentId)){
					StudentLearningNote studentLearningNote = studentLearningNoteService.getStudentLearningNote(syllabusid, studentId);
					if(studentLearningNote == null){
						studentLearningNote = new StudentLearningNote();
						studentLearningNote.setSyllabus(syllabusService.get(syllabusid));
					}
					model.addAttribute("studentLearningNote", studentLearningNote);
				}
			}			
		}		
		return "/edu3/resources/site/note";
	}
	/**
	 * 保存学习笔记
	 * @param note
	 * @param syllabusid
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/resource/course/note/save.html")
	public void saveLearningNote(StudentLearningNote note, String syllabusid, HttpServletRequest request,HttpServletResponse response) throws WebException {
		Map<String,Object> map = new HashMap<String, Object>();	
		try {	
			User user = SpringSecurityHelper.getCurrentUser();
			if(user != null && SpringSecurityHelper.isUserInRole(CacheAppManager.getSysConfigurationByCode("sysuser.role.student").getParamValue())){				
				StudentInfo studentInfo = studentInfoService.getStudentInfoByUser(user);
				if(studentInfo != null && ExStringUtils.isNotBlank(syllabusid)){
					Date currentDay = new Date();
					note.setTitle(ExStringUtils.trimToEmpty(note.getTitle()));
					note.setStudentInfo(studentInfo);
					note.setSyllabus(syllabusService.get(syllabusid));
					note.setFillinDate(currentDay);
					note.setUpdateDate(currentDay);					
					
					if(ExStringUtils.isNotBlank(note.getResourceid())){
						StudentLearningNote studentLearningNote = studentLearningNoteService.get(note.getResourceid());
						ExBeanUtils.copyProperties(studentLearningNote, note);
						studentLearningNoteService.update(studentLearningNote);
					} else {
						studentLearningNoteService.save(note);
						map.put("noteId", note.getResourceid());
					}
					map.put("message", "保存成功！");	
					map.put("statusCode", 200);									
				}
			} else {
				map.put("statusCode", 300);
				map.put("message", "请登录后再操作！");
			}
		}catch (Exception e) {
			logger.error("保存学习笔记出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败！"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));	
	}
	/**
	 * 导出学习笔记
	 * @param resourceid
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/resource/course/note/export.html")
	public void exportLearningNote(String resourceid, HttpServletRequest request,HttpServletResponse response) throws WebException {
		setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
		try {
			if(ExStringUtils.isNotBlank(resourceid)){
				StudentLearningNote studentLearningNote = studentLearningNoteService.get(resourceid);
				GUIDUtils.init();
				String disFile = getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(false) + ".txt";
				FileUtils.writeFile(disFile, studentLearningNote.getContent(), 2);
				
				downloadFile(response, studentLearningNote.getTitle()+".txt", disFile,true);	
			}			
		}catch (Exception e) {
			logger.error("导出学习笔记失败：{}",e.fillInStackTrace());	
			renderHtml(response, "<script type=\"text/javascript\">alert(\"学习笔记另存为失败！"+e.getMessage()+"\")</script>");
		}
	}
	/**
	 * 保存学生学习跟踪记录
	 * @param syllabusid
	 * @param resType 0-讲义, 1-授课, 2-随堂练习
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/resource/course/learningtrace/log.html")
	public void logStudentLearningTrace(String syllabusid, String resType,HttpServletRequest request,HttpServletResponse response) throws WebException {
		try {	
			User user = SpringSecurityHelper.getCurrentUser();
			if(user != null && SpringSecurityHelper.isUserInRole(CacheAppManager.getSysConfigurationByCode("sysuser.role.student").getParamValue())){				
				StudentInfo studentInfo = studentInfoService.getStudentInfoByUser(user);
				if(studentInfo != null && ExStringUtils.isNotBlank(syllabusid)){
					Date currentDay = new Date();
					StudentLearningTrace studentLearningTrace = studentLearningTraceService.getStudentLearningTrace(syllabusid, studentInfo.getResourceid());							
					if(studentLearningTrace==null){
						studentLearningTrace = new StudentLearningTrace();
						studentLearningTrace.setStudentInfo(studentInfo);
						studentLearningTrace.setSyllabus(syllabusService.get(syllabusid));
					}
					if("0".equals(resType)){//未记录学习讲义时间						
						studentLearningTrace.setLearHandoutTime(currentDay);
					} else if("1".equals(resType)){//未记录学习授课时间	
						studentLearningTrace.setLearMeteTime(currentDay);
					}  if("2".equals(resType)){//未记录提交随堂练习时间	
						studentLearningTrace.setLearCourseexamTime(currentDay);						
					}
					studentLearningTraceService.saveOrUpdate(studentLearningTrace);
				}
			} 
		}catch (Exception e) {
			logger.error("保存学生学习跟踪记录出错：{}",e.fillInStackTrace());
		}
	}
	/**
	 * 学习记录
	 * @param courseId
	 * @param model
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/resource/course/learningtrace.html")
	public String listLearningTrace(String courseId, ModelMap model) throws Exception {
		if(ExStringUtils.isNotBlank(courseId)){				
			User user = SpringSecurityHelper.getCurrentUser();
			StudentInfo stu = studentInfoService.getByUserId(user.getResourceid());
			boolean isStudent = user != null && SpringSecurityHelper.isUserInRole(CacheAppManager.getSysConfigurationByCode("sysuser.role.student").getParamValue());
			if(isStudent && null!=user.getUserExtends()&&null!=user.getPropertys(UserExtends.USER_EXTENDCODE_DEFAULTROLLID)){//学生
				String studentId = user.getPropertys(UserExtends.USER_EXTENDCODE_DEFAULTROLLID).getExValue();
				if(ExStringUtils.isNotEmpty(studentId)){
					int hasStudyResource = 0, totalResource = 0;//已学内容，全部内容
					int totalFinishedExamCount = 0, totalExamCount = 0;//已提交随堂练习，全部随堂练习
					int totalFinishedExerciseCount = 0, totalExerciseCount = 0;//已提交作业，全部作业次数
					double usualResults = 0.0;//累积平时分
					
					List<SyllabusVo> syllabusVoList = courseResourceService.findSyllabusVoForLearningStat(courseId, studentId);
					model.addAttribute("syllabusVoList", syllabusVoList);
					if(ExCollectionUtils.isNotEmpty(syllabusVoList)){
						totalResource = syllabusVoList.size();//全部内容个数
						for (SyllabusVo vo : syllabusVoList) {
							if(vo.getLearHandoutTime()!=null || vo.getLearMeteTime()!=null ||vo.getLearCourseexamTime()!=null){
								hasStudyResource++;
							}
							totalFinishedExamCount += vo.getExamFinishedCount().intValue();
							totalExamCount += vo.getExamTotalCount().intValue();
						}
					}
					
					//作业
					/*Grade grade = gradeService.getDefaultGrade();
					String yearInfoId = grade.getYearInfo().getResourceid();
					String term = grade.getTerm();*/
					/*String yearTerm = CacheAppManager.getSysConfigurationByCode("sysCurrentGrade").getParamValue();
					String yearInfoId = "";
					String term = "";
					if (null!=yearTerm) {
						String[] ARRYyterm = yearTerm.split("\\.");
						yearInfoId = yearInfoService.getByFirstYear(Long.parseLong(ARRYyterm[0])).getResourceid();
						term = ARRYyterm[1];
					}*/
					TeachingPlanCourse teachingPlanCourse = studentLearnPlanService.getStudentLearnPlanByCourse(courseId, stu.getResourceid(),"studentId").getTeachingPlanCourse();
					TeachingPlanCourseStatus teachingPlanCourseStatus = teachingPlanCourseStatusService
							.findOneByCondition(stu.getGrade()
									.getResourceid(), stu.getTeachingPlan().getResourceid(),
									teachingPlanCourse.getResourceid(), stu.getBranchSchool().getResourceid());
					
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
					try {
						Map<String, Object> parameters = new HashMap<String, Object>();
						parameters.put("studentId", studentId);
						parameters.put("courseId", courseId);
						parameters.put("yearInfoId", yearInfoId);
						parameters.put("term", term);
						String sql = "select b.resourceid,b.colname,s.result,s.comitdate,s.status from edu_lear_exercisebatch b left join edu_lear_studentexercise s on b.resourceid=s.exercisebatchid and s.isdeleted=0 and s.exerciseid is null and s.status>=1 and s.studentid=:studentId where b.isdeleted=0 and b.status>=1 and b.yearid=:yearInfoId and b.term=:term and b.courseid=:courseId ";
						
						List<Map<String, Object>> exercisetList = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql, parameters);
						model.addAttribute("exercisetList", exercisetList);
						
						if(ExCollectionUtils.isNotEmpty(exercisetList)){
							totalExerciseCount = exercisetList.size();
							for (Map<String, Object> map : exercisetList) {
								if(map.get("STATUS")!=null && ("1".equals(map.get("STATUS").toString()) || "2".equals(map.get("STATUS").toString()))){//已提交作业
									totalFinishedExerciseCount ++;
								}
							}
						}
					} catch (Exception e) {
						logger.error("查询作业提交情况出错:{}",e.fillInStackTrace());
					}
					
					int bbsCount = 0, askCount = 0;
					//发帖
					String topicSql = "select count(decode(rs.topicType,'topic',1,null)) topicCount,count(decode(rs.topicType,'reply',1,null)) replyCount,count(decode(rs.isbest,'Y',1,null)) bestCount,count(case when s.sectioncode=:sectionCode and rs.isbest='Y' then 1 else null end) askCount from ( select t.resourceid,t.isbest,t.fillinmanid,t.courseid,t.isdeleted,t.sectionid,'topic' topicType from edu_bbs_topic t  union all select r.resourceid,r.isbest,t1.fillinmanid,t1.courseid,t1.isdeleted,t1.sectionid,'reply' topicType from edu_bbs_reply r join edu_bbs_topic t1 on t1.resourceid=r.topicid) rs join edu_bbs_userinfo u on u.resourceid=rs.fillinmanid and u.isdeleted=0 join edu_bbs_section s on s.resourceid=rs.sectionid where rs.isdeleted=0 and rs.courseid=:courseId and u.userid=:userId ";
					try {
						Map<String, Object> param = new HashMap<String, Object>();
						param.put("courseId", courseId);
						param.put("userId", user.getResourceid());
						param.put("sectionCode",CacheAppManager.getSysConfigurationByCode("web.bbs.sectioncode").getParamValue());
						Map<String, Object> topicCountMap = baseSupportJdbcDao.getBaseJdbcTemplate().findForMap(topicSql, param);	
						model.addAttribute("topicCountMap", topicCountMap);
						
						if(topicCountMap!=null && topicCountMap.get("askCount") != null && topicCountMap.get("bestCount") != null){
							askCount = ((BigDecimal)topicCountMap.get("askCount")).intValue();
							bbsCount = ((BigDecimal)topicCountMap.get("bestCount")).intValue()-askCount;
						}
					} catch (Exception e) {
						logger.error("查询发帖情况出错:{}",e.fillInStackTrace());
					}
					
					//累积平时分
					usualResults = getUsualResults(courseId, studentId,	yearInfoId, term, bbsCount,	askCount);
					
					//错题集
					try {
						String examSql = "select s.resourceid,s.answer stuanswer,es.question,es.answer from edu_lear_studentcourseexam s join edu_lear_courseexam c on c.resourceid=s.courseexamid and c.isdeleted=0 and c.ispublished='Y' join edu_lear_exams es on es.resourceid=c.examid and es.examtype<=5 and es.isdeleted=0 where s.isdeleted=0 and es.courseid=:courseId and s.studentid=:studentId and s.iscorrect='N' order by c.syllabustreeid,s.showorder";
						Map<String, Object> params = new HashMap<String, Object>();
						params.put("courseId", courseId);
						params.put("studentId", studentId);
						List<Map<String, Object>> mistakeExamList = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(examSql, params);	
						model.addAttribute("mistakeExamList", mistakeExamList);
					} catch (Exception e) {
						logger.error("查询错题集出错:{}",e.fillInStackTrace());
					}
					
					model.addAttribute("hasStudyResource", hasStudyResource);
					model.addAttribute("totalResource", totalResource);
					model.addAttribute("totalFinishedExamCount", totalFinishedExamCount);
					model.addAttribute("totalExamCount", totalExamCount);
					model.addAttribute("totalFinishedExerciseCount", totalFinishedExerciseCount);
					model.addAttribute("totalExerciseCount", totalExerciseCount);
					model.addAttribute("usualResults", usualResults);
				}
			}			
		}		
		return "/edu3/resources/site/learningtrace";
	}

	/**
	 * 平时分累积总分
	 * @param courseId
	 * @param studentId
	 * @param yearInfoId
	 * @param term
	 * @param bbsCount
	 * @param askCount
	 * @return
	 */
	private double getUsualResults(String courseId, String studentId, String yearInfoId, String term,int bbsCount, int askCount) {
		UsualResultsRule rule = usualResultsRuleService.getUsualResultsRuleByCourseAndYear(courseId,yearInfoId,term);
		Double usualResults = 0.0;
		if(rule != null){
			Double originalBbsResults = 0.0;//网络辅导分						
			Double originalAskQuestionResults = 0.0;//随堂问答分
			Double originalExerciseResults = 0.0;//作业练习分
			Double originalCourseExamResults = 0.0;//随堂练习分
			
			if(rule.getCourseExamResultPer()>0){//随堂练习分
				Double per = studentActiveCourseExamService.avgStudentActiveCourseExamResult(courseId, studentId);						
				originalCourseExamResults = per*100>=rule.getCourseExamCorrectPer()?100:per*100;
			}
			if(rule.getExerciseResultPer()>0){//作业练习分		
				originalExerciseResults = studentExerciseService.avgStudentExerciseResult(courseId, studentId,yearInfoId,term);
			}
			if(rule.getAskQuestionResultPer()>0 && rule.getBbsBestTopicNum()>0){	//随堂问答分						
				originalAskQuestionResults = askCount>=rule.getBbsBestTopicNum()?100.0:askCount*rule.getBbsBestTopicResult();
			}
			if(rule.getBbsResultPer()>0 && rule.getBbsBestTopicNum()>0){//网络辅导分
				originalBbsResults = bbsCount>=rule.getBbsBestTopicNum()?100.0:bbsCount*rule.getBbsBestTopicResult();	
			}				
			
			usualResults += originalBbsResults * rule.getBbsResultPer();
			usualResults += originalExerciseResults * rule.getExerciseResultPer();
			usualResults += originalCourseExamResults * rule.getCourseExamResultPer();	
			usualResults += originalAskQuestionResults * rule.getAskQuestionResultPer();	
			usualResults = usualResults / 100;
		}
		return usualResults;
	}
	
	/**
	 * 检索
	 * @param courseId
	 * @param keyword
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/resource/course/search.html")
	public String searchResourceCourse(String courseId, String keyword, ModelMap model) throws WebException {
		if(ExStringUtils.isNotBlank(courseId)){		
			if(ExStringUtils.isNotBlank(keyword)){
				List<SyllabusVo> syllabusVoList = courseResourceService.findSyllabusVoForSearch(courseId, ExStringUtils.trimToEmpty(keyword));
				model.addAttribute("searchSyllabusVoList", syllabusVoList);
			}	
			model.addAttribute("course", courseService.get(courseId));
		}	
		model.addAttribute("keyword", ExStringUtils.trimToEmpty(keyword));
		return "/edu3/resources/site/search";
	}
	/**
	 * 学生自荐作业
	 * @param courseId
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/resource/course/studentselftexercise.html")
	public String listStudentSelfExercise(String courseId, ModelMap model) throws WebException {
		if(ExStringUtils.isNotBlank(courseId)){		
			List<StudentSelfExercise> studentSelfExerciseList = null;
			
			User user = SpringSecurityHelper.getCurrentUser();
			Map<String, Object> condition = new HashMap<String, Object>();
			String hql = "from "+StudentSelfExercise.class.getSimpleName()+" where isDeleted=0 and course.resourceid=:courseId ";
			condition.put("courseId", courseId);
			if(user != null && SpringSecurityHelper.isUserInRole(CacheAppManager.getSysConfigurationByCode("sysuser.role.student").getParamValue())
					&& null!=user.getUserExtends()&&null!=user.getPropertys(UserExtends.USER_EXTENDCODE_DEFAULTROLLID)){
				String studentId = user.getPropertys(UserExtends.USER_EXTENDCODE_DEFAULTROLLID).getExValue();
				hql += " and (isPublished=:isPublished or studentInfo.resourceid=:studentId)";
				condition.put("studentId", ExStringUtils.trimToEmpty(studentId));
				
				model.addAttribute("isStudent", Constants.BOOLEAN_YES);
				model.addAttribute("storeDir", user.getUsername());				
			} else {
				hql += " and isPublished=:isPublished ";				
			}
			condition.put("isPublished", Constants.BOOLEAN_YES);
			hql += " order by publishDate,resourceid ";
			
			studentSelfExerciseList = studentSelfExerciseService.findByHql(hql, condition);
			if(ExCollectionUtils.isNotEmpty(studentSelfExerciseList)){
				for (StudentSelfExercise studentSelfExercise : studentSelfExerciseList) {
					List<Attachs> attachs = attachsService.findAttachsByFormId(studentSelfExercise.getResourceid());
					studentSelfExercise.setAttachs(attachs);
				}
			}
			
			model.addAttribute("studentSelfExerciseList", studentSelfExerciseList);
			model.addAttribute("course", courseService.get(courseId));
		}	
		model.addAttribute("user", SpringSecurityHelper.getCurrentUser());
		return "/edu3/resources/site/studentselfexercise";
	}	
	/**
	 * 保存学生自荐作业
	 * @param exercise
	 * @param courseId
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/resource/course/studentselftexercise/save.html")
	public void saveStudentSelfExercise(StudentSelfExercise exercise, String courseId, HttpServletRequest request,HttpServletResponse response) throws WebException {
		Map<String,Object> map = new HashMap<String, Object>();	
		try {	
			User user = SpringSecurityHelper.getCurrentUser();
			if(user != null && SpringSecurityHelper.isUserInRole(CacheAppManager.getSysConfigurationByCode("sysuser.role.student").getParamValue())){				
				StudentInfo studentInfo = studentInfoService.getStudentInfoByUser(user);
				if(studentInfo != null && ExStringUtils.isNotBlank(courseId)){
					String[] uploadfileid = request.getParameterValues("uploadfileid");
					Course course = courseService.get(courseId);
					
					exercise.setStudentInfo(studentInfo);
					exercise.setCourse(course);
					
					studentSelfExerciseService.saveOrUpdateStudentSelfExercise(exercise, uploadfileid);					
					map.put("message", "保存成功！");	
					map.put("statusCode", 200);									
				}
			} else {
				map.put("statusCode", 300);
				map.put("message", "请登录后再操作！");
			}
		}catch (Exception e) {
			logger.error("保存自荐作业出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败！"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));	
	}
	/**
	 * 特色资源/知识园地页面
	 * @param courseId
	 * @param type 0=特色资源,1=知识园地
	 * @param model
	 * @param objPage
	 * @return
	 * @throws WebException
	 */
	@RequestMapping(value={"/resource/course/coursefiles.html","/resource/course/knowledge.html"})
	public String listCourseOverView(String courseId, String type,HttpServletRequest request, ModelMap model, Page objPage) throws WebException{	
		String resUrl = ExStringUtils.substringAfter(request.getRequestURI(), ExStringUtils.trimToEmpty(request.getContextPath()));
		type = "/resource/course/knowledge.html".equals(resUrl)?"1":"0";
		getCourseFilesByType(courseId, type, null,null,model, objPage,null);
		return "/edu3/resources/site/coursefiles";
	}

	/**
	 * 案例分析
	 * @param courseId
	 * @param model
	 * @param objPage
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/resource/course/caseanalysis.html")
	public String listCaseAnalysis(String courseId,String caseid, String fid,ModelMap model, Page objPage) throws WebException{	
		getCourseFilesByType(courseId, "2", caseid, fid, model, objPage,null);
		model.addAttribute("type", "2");
		return "/edu3/resources/site/caseanalysis";
	}
	/**
	 * 刑法全文和司法解释
	 * @param courseId
	 * @param caseid
	 * @param fid
	 * @param model
	 * @param objPage
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/resource/course/lawplain.html")
	public String listLawPlain(String courseId,String caseid, String fid,ModelMap model, Page objPage,HttpServletRequest request) throws WebException{	
		getCourseFilesByType(courseId, "3", caseid, fid, model, objPage, request.getParameter("pageNum"));	
		model.addAttribute("type", "3");
		return "/edu3/resources/site/caseanalysis";
	}
	
	/**
	 * 特色资源/知识园地/案例分析
	 * @param courseId
	 * @param type 0=特色资源,1=知识园地,2-案例分析
	 * @param model
	 * @param objPage
	 * @return
	 * @throws WebException
	 */
	private void getCourseFilesByType(String courseId, String type,String caseid,String fid, ModelMap model, Page objPage,String pageNum) {
		objPage.setOrderBy("fillinDate desc,fileLevel,showOrder,resourceid");
		objPage.setOrder(Page.ASC);		
		
		if(ExStringUtils.isNotBlank(courseId)){
			Course course = courseService.get(courseId);
			
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("courseId", courseId);
			condition.put("fileType", 1);//只查询文件
			condition.put("isPublished", Constants.BOOLEAN_YES);//发布的		
			String studyCenterName = CacheAppManager.getSysConfigurationByCode("course.teacherfiles.studycenter").getParamValue();//知识园地
			String caseAnalysisName = CacheAppManager.getSysConfigurationByCode("course.teacherfiles.caseanalysis").getParamValue();//案例分析
			String lowPlainName = CacheAppManager.getSysConfigurationByCode("course.teacherfiles.lawplain").getParamValue();//刑法全文和司法解释
			
			if("1".equals(type)){//知识园地
				if(ExStringUtils.isNotBlank(studyCenterName)) {
					condition.put("parentFileName", studyCenterName);
				}
				//condition.put("emptyAttach",Constants.BOOLEAN_YES);
				Page teacherFilesPage = teacherCourseFilesService.findTeacherFilesByCondition(condition, objPage);			
				model.addAttribute("teacherFilesList", teacherFilesPage);	
			} else if("2".equals(type)){//案例分析
				setDirFiles("2",caseid,condition,fid,caseAnalysisName,model,null,null);
			}else if("3".equals(type)){//刑法全文和司法解释				
				setDirFiles("3",caseid,condition,fid,lowPlainName,model,objPage,pageNum);
			} else {//特色资源
				Set<String> fileNames = new HashSet<String>();
				if(ExStringUtils.isNotBlank(studyCenterName)) {
					fileNames.add(studyCenterName);
				}
				findFilterFiles(courseId, caseAnalysisName, fileNames);//排除案例分析文件夹	
				findFilterFiles(courseId, lowPlainName, fileNames);//排除文件夹	
				if(ExCollectionUtils.isNotEmpty(fileNames)) {
					condition.put("notInPatentFileNames", "'"+ExStringUtils.join(fileNames,"','")+"'");
				}
				Page teacherFilesPage = teacherCourseFilesService.findTeacherFilesByCondition(condition, objPage);			
				model.addAttribute("teacherFilesList", teacherFilesPage);	
			}					
			model.addAttribute("course", course);
		}
		model.addAttribute("curseFileType", type);
		model.addAttribute("user", SpringSecurityHelper.getCurrentUser());
	}

	/**
	 * @param courseId
	 * @param caseAnalysisName
	 * @param fileNames
	 */
	private void findFilterFiles(String courseId, String folderName, Set<String> fileNames) {
		if(ExStringUtils.isNotBlank(folderName)) {
			fileNames.add(folderName);
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("courseId", courseId);
			param.put("fileType", 0);
			param.put("isPublished", Constants.BOOLEAN_YES);
			param.put("parentFileName", folderName);
			List<TeacherCourseFiles> folderList = teacherCourseFilesService.findTeacherFilesByCondition(param);	
			if(ExCollectionUtils.isNotEmpty(folderList)){
				for (TeacherCourseFiles f : folderList) {
					fileNames.add(f.getFileName());
				}
			}
		}
	}	
	
	private void setDirFiles(String type,String caseid,Map<String, Object> condition,String fid,String dirName,ModelMap model,Page objPage,String pageNum){
		List<TeacherCourseFiles> teacherFilesList = new ArrayList<TeacherCourseFiles>();
		TeacherCourseFiles dirFiles = null;
		String dirParentName = "";
		if(ExStringUtils.isNotBlank(caseid)){
			dirFiles = teacherCourseFilesService.get(caseid);
			fid = dirFiles.getParent().getResourceid();
		}
		if(ExStringUtils.isNotBlank(dirName)) {
			condition.put("parentFileName", dirName);
		}
		model.addAttribute("dirName", dirName);
		condition.put("orderby","showOrder,resourceid");
		condition.put("emptyAttach",Constants.BOOLEAN_YES);				
		condition.put("fileType", 0);//先查询文件夹
		List<TeacherCourseFiles> folderList = teacherCourseFilesService.findTeacherFilesByCondition(condition);			
		//model.addAttribute("folderList", folderList);	
		model.addAttribute("folderList", folderList);
		 
		if(ExStringUtils.isBlank(fid) && ExCollectionUtils.isNotEmpty(folderList)){
			fid = folderList.get(0).getResourceid();			
		}
		condition.remove("parentFileName");
		condition.put("fileType", 1);
		condition.put("parentId", fid);
		if("2".equals(type)){
			teacherFilesList = teacherCourseFilesService.findTeacherFilesByCondition(condition);
			model.addAttribute("teacherFilesList", teacherFilesList);
			if(ExCollectionUtils.isNotEmpty(teacherFilesList)){
				if(ExStringUtils.isNotBlank(caseid)){
					for (TeacherCourseFiles file : teacherFilesList) {
						if(file.getResourceid().equals(caseid)){
							dirFiles = file;
							break;
						}
					}
				} else {
					dirFiles = teacherFilesList.get(0);
				}
			}				
			//model.addAttribute("caseAnalysis", caseAnalysis);	
			model.addAttribute("dirFiles", dirFiles);
			if(dirFiles != null) {
				dirParentName = dirFiles.getParent().getFileName();
			}
		}else if("3".equals(type)){
			String plainName = CacheAppManager.getSysConfigurationByCode("course.teacherfiles.lawplain.plainname").getParamValue();//司法解释文件夹
			if(ExStringUtils.isNotBlank(plainName) && ExStringUtils.isNotBlank(fid)){
				TeacherCourseFiles parent = teacherCourseFilesService.get(fid);
				if(plainName.equals(parent.getFileName())){//司法解释
					model.addAttribute("isPlainFiles", Constants.BOOLEAN_YES);//司法解释				
					if(ExStringUtils.isBlank(pageNum)){//司法解释列表
						condition.put("orderby", "fillinDate desc,fileLevel,showOrder,resourceid");
						teacherFilesList = teacherCourseFilesService.findTeacherFilesByCondition(condition);
						model.addAttribute("teacherFilesList", teacherFilesList);
						model.addAttribute("isPlainIndexPage", Constants.BOOLEAN_YES);//司法解释列表											
					} 
				}
			}
			objPage.setPageSize(1);
			//分页查询
			Page page = teacherCourseFilesService.findTeacherFilesByCondition(condition,objPage);
			model.addAttribute("lawplainPage", page);
			model.addAttribute("condition", condition);
			if(ExCollectionUtils.isNotEmpty(page.getResult())){
				dirFiles = (TeacherCourseFiles)page.getResult().get(0);
				dirParentName = dirFiles.getParent().getFileName();
			}
		}					
		//model.addAttribute("teacherFilesList", teacherFilesList);					
		model.addAttribute("dirParentName", dirParentName);
		
		
		
	}
	
	/**
	 * 打开外部网页
	 * @param courseId
	 * @param channelid
	 * @param request
	 * @param model
	 * @param objPage
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/resource/course/ref.html")
	public String tr(String courseId, String channelid, HttpServletRequest request, ModelMap model, Page objPage) throws WebException{	
		if(ExStringUtils.isNotBlank(channelid)){
			Channel channel = channelService.get(channelid);
			model.addAttribute("channel", channel);
			model.addAttribute("course", channel.getCourse());
		}
		return "/edu3/resources/site/ref";
	}
	/**
	 * 登录
	 * @param courseId
	 * @param request
	 * @param model
	 * @param objPage
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/resource/login.html")
	public String login(String courseId, HttpServletRequest request, ModelMap model) throws WebException{	
		if(ExStringUtils.isNotBlank(courseId)){			
			model.addAttribute("course", courseService.get(courseId));
		}
		return "/edu3/resources/site/login";
	}
	/**
	 * 
	 * @param courseId
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/resource/redirct.html")
	public String redirect(String courseId, HttpServletRequest request, ModelMap model) throws WebException{	
		if(ExStringUtils.isNotBlank(courseId)){			
			model.addAttribute("courseId", courseId);
		}
		return "/edu3/resources/site/redirct";
	}
	
	@Autowired
	@Qualifier("channelService")
	private IChannelService channelService;	
	
	@Autowired
	@Qualifier("courseService")
	private ICourseService courseService;
	
	@Autowired
	@Qualifier("syllabusService")
	private ISyllabusService syllabusService;
	
	@Autowired
	@Qualifier("courseLearningGuidService")
	private ICourseLearningGuidService courseLearningGuidService;
	
	@Autowired
	@Qualifier("courseOverviewService")
	private ICourseOverviewService courseOverviewService;
	
	@Autowired
	@Qualifier("courseResourceService")
	private ICourseResourceService courseResourceService;	
	
	@Autowired
	@Qualifier("mateResourceService")
	private IMateResourceService mateResourceService;
	
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
	@Qualifier("exerciseBatchService")
	private IExerciseBatchService exerciseBatchService;
	
	@Autowired
	@Qualifier("exerciseService")
	private IExerciseService exerciseService;
	
	@Autowired
	@Qualifier("studentExerciseService")
	private IStudentExerciseService studentExerciseService;	
	
	@Autowired
	@Qualifier("studentLearnPlanService")
	private IStudentLearnPlanService studentLearnPlanService;
	
	@Autowired
	@Qualifier("gradeService")
	private IGradeService gradeService;
	
	@Autowired
	@Qualifier("attachsService")
	private IAttachsService attachsService;
	
	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;
	
	@Autowired
	@Qualifier("learningTimeSettingService")
	private ILearningTimeSettingService learningTimeSettingService;
	
	@Autowired
	@Qualifier("teacherCourseFilesService")
	private ITeacherCourseFilesService teacherCourseFilesService;
	
	@Autowired
	@Qualifier("courseMockTestService")
	private ICourseMockTestService courseMockTestService;
	
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
	@Qualifier("studentLearningNoteService")
	private IStudentLearningNoteService studentLearningNoteService;
	
	@Autowired
	@Qualifier("studentLearningTraceService")
	private IStudentLearningTraceService studentLearningTraceService;
	
	@Autowired
	@Qualifier("usualResultsRuleService")
	private IUsualResultsRuleService usualResultsRuleService;
	
	@Autowired
	@Qualifier("studentSelfExerciseService")
	private IStudentSelfExerciseService studentSelfExerciseService;	
	
	@Autowired
	@Qualifier("memcacheManager")
	private MemcachedManager memcachedManager;
	
	@Autowired
	@Qualifier("yearInfoService")
	private IYearInfoService yearInfoService;
	
	@Autowired
	@Qualifier("teachingPlanCourseStatusService")
	private ITeachingPlanCourseStatusService teachingPlanCourseStatusService;
	
	@Autowired
	@Qualifier("teachingPlanCourseService")
	private ITeachingPlanCourseService teachingPlanCourseService;
	
	
}
