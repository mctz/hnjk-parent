package com.hnjk.edu.arrange.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.controller.BaseSupportController;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.arrange.model.TeachCourse;
import com.hnjk.edu.arrange.model.TeachCourseClasses;
import com.hnjk.edu.arrange.model.TeachCourseDetail;
import com.hnjk.edu.arrange.service.ITeachCourseClassesService;
import com.hnjk.edu.arrange.service.ITeachCourseDetailService;
import com.hnjk.edu.arrange.service.ITeachCourseService;
import com.hnjk.edu.basedata.model.Classic;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.basedata.model.Grade;
import com.hnjk.edu.basedata.model.YearInfo;
import com.hnjk.edu.basedata.service.IGradeService;
import com.hnjk.edu.basedata.service.IYearInfoService;
import com.hnjk.edu.roll.model.Classes;
import com.hnjk.edu.roll.service.IClassesService;
import com.hnjk.edu.teaching.model.CourseTeacherCl;
import com.hnjk.edu.teaching.model.TeachingGuidePlan;
import com.hnjk.edu.teaching.model.TeachingPlanCourse;
import com.hnjk.edu.teaching.service.ICourseService;
import com.hnjk.edu.teaching.service.ICourseStatusClService;
import com.hnjk.edu.teaching.service.ITeachingGuidePlanService;
import com.hnjk.edu.teaching.service.ITeachingPlanCourseService;
import com.hnjk.edu.teaching.service.ITeachingPlanCourseStatusService;
import com.hnjk.edu.util.Condition2SQLHelper;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.Dictionary;
import com.hnjk.platform.system.service.IDictionaryService;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.OrgUnit;
import com.hnjk.security.model.User;
import com.hnjk.security.service.IOrgUnitService;
import com.hnjk.security.service.IUserService;

@Controller
public class OpenCourseResultController extends BaseSupportController {
	
	private static final long serialVersionUID = 1L;

	@Autowired
	@Qualifier("teachingPlanCourseService")
	private ITeachingPlanCourseService teachingPlanCourseService;
	
	@Autowired
	@Qualifier("teachingguideplanservice")
	private ITeachingGuidePlanService teachingguideplanservice;
	
	@Autowired
	@Qualifier("teachCourseClassesService")
	private ITeachCourseClassesService teachCourseClassesService;
	
	@Autowired
	@Qualifier("teachCourseService")
	private ITeachCourseService teachCourseService;
	
	@Autowired
	@Qualifier("teachCourseDetailService")
	private ITeachCourseDetailService teachCourseDetailService;
	
	@Autowired
	@Qualifier("classesService")
	private IClassesService classesService;
	
	@Autowired
	@Qualifier("orgUnitService")
	private IOrgUnitService orgUnitService;
	
	@Autowired
	@Qualifier("teachingPlanCourseStatusService")
	private ITeachingPlanCourseStatusService teachingPlanCourseStatusService;
	
	@Autowired
	@Qualifier("courseStatusClService")
	private ICourseStatusClService courseStatusClService;
	
	@Autowired
	@Qualifier("userService")
	private IUserService userService;
	
	@Autowired
	@Qualifier("gradeService")
	private IGradeService gradeService;
	
	@Autowired
	@Qualifier("yearInfoService")
	private IYearInfoService yearInfoService;
	
	@Autowired
	@Qualifier("dictService")
	private IDictionaryService dictService;
	
	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;
	
	/**
	 * 已开课的年级教学计划列表
	 * @param gradeid
	 * @param majorid
	 * @param classicid
	 * @param brSchoolid
	 * @param teachingType
	 * @param term
	 * @param classesid
	 * @param status
	 * @param request
	 * @param model
	 * @param objPage
	 * @return openCourse-list.jsp
	 * @throws WebException
	 */
	@RequestMapping("/edu3/arrange/opencourseresult/list.html")
	public String queryTeachCourseList(HttpServletRequest request, ModelMap model, Page objPage) throws WebException{
		String gradeid		=		request.getParameter("gradeid");
		String brSchoolid	=		request.getParameter("brSchoolid");
		String showList		=		request.getParameter("showList");
		Map<String, Object> condition = Condition2SQLHelper.getConditionFromResquestByIterator(request);
		boolean isBrschool = false;
		User user = SpringSecurityHelper.getCurrentUser();
		if(user.getOrgUnit().getUnitType().equals(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())){//如果为校外学习中心人员
			isBrschool = true;
			brSchoolid = ExStringUtils.trim(user.getOrgUnit().getResourceid());
			model.addAttribute("schoolname",ExStringUtils.trim(user.getOrgUnit().getUnitName()));
		}
		if(ExStringUtils.isNotBlank(brSchoolid)) condition.put("brSchoolid", brSchoolid);
		if (ExStringUtils.isNotBlank(showList)) {
			if(ExStringUtils.isBlank(gradeid)){
				Calendar a=Calendar.getInstance();
				condition.put("minYear", a.get(Calendar.YEAR)-3);
			}
			condition.put("isOpen", Constants.BOOLEAN_YES);//已开课
			condition.put("OpenCourseResultFlag", "Y");//开课结果页面请求
			
			Page coursePage = teachingPlanCourseStatusService.findTeachingPlanCourseStatusMapByCondition(condition,objPage);
			model.addAttribute("coursePage", coursePage);
		}
		model.addAttribute("isBrschool", isBrschool);
		model.addAttribute("condition", condition);
		return "/edu3/arrange/openCourseResult/openCourse-list";
	}
	
	/**
	 * 合班逻辑：课程、课时、考核、形式、层次、必选修相同
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/arrange/opencourseresult/merge.html")
	public void mergeClasses(HttpServletRequest request,HttpServletResponse response)throws WebException{
		
		//查询条件
		String resIds 			= 		ExStringUtils.trimToEmpty(request.getParameter("resIds"));
		String gpIds			=		ExStringUtils.trim(request.getParameter("gpIds"));//年级教学计划
		String pcIds			=		ExStringUtils.trim(request.getParameter("pcIds"));//教学计划课程
		String classesIds		=		ExStringUtils.trim(request.getParameter("classesIds"));//班级
		String courseIds		=		ExStringUtils.trim(request.getParameter("courseIds"));//课程
		String terms			=		ExStringUtils.trim(request.getParameter("terms"));//上课学期
		String openTerm			=		ExStringUtils.trim(request.getParameter("openTerm"));//上课学期
		String stuCounts		=		ExStringUtils.trim(request.getParameter("stuCounts"));//学生人数
		String teacherIds		=		ExStringUtils.trimToEmpty(request.getParameter("teacherIds"));//登分老师
		String isMerge			=		ExStringUtils.trim(request.getParameter("isMerge"));//是否合班
		String teachType			=		ExStringUtils.trim(request.getParameter("teachType"));//教学类型
		String unitName			=		ExStringUtils.trim(request.getParameter("unitName"));//学习中心id
		String courseNames		=		ExStringUtils.trimToEmpty(request.getParameter("courseNames"));//课程名称
		List<String> residList = Arrays.asList(resIds.split(","));
		List<String> guidPlanList = Arrays.asList(gpIds.split(","));
		List<String> classesList = Arrays.asList(classesIds.split(","));
		List<String> planCourseList = Arrays.asList(pcIds.split(","));
		List<String> courseList = Arrays.asList(courseIds.split(","));
		List<String> termList = Arrays.asList(terms.split(","));
		List<String> stuCountList = Arrays.asList(stuCounts.split(","));
		List<String> teacherList = Arrays.asList(teacherIds.split(","));
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer errorMsg = new StringBuffer("");
		int mergeStatus = 1;//-1:failure, 0:warning, 1:succeed
		try {
			do {
				List<TeachCourseClasses> tcClassesList = new ArrayList<TeachCourseClasses>();
				List<TeachCourse> deletedTeachCourses = new ArrayList<TeachCourse>();
				TeachCourse tCourse = null;
				User user = SpringSecurityHelper.getCurrentUser();
				if(!(planCourseList!=null && planCourseList.size()>0)){
					errorMsg.append("请选择教学计划"+"<br/>");
					mergeStatus=-1;
					continue;
				}
				String termStr = "";//上课学期
				String courseStr="";//课程
				Long stydyHour=null;// 学时
				String examClassType="";//考试类别
				String teachingType="";//学习形式
				String classicStr="";//层次
				String courseType=""; //课程性质
				Course course = null;
				Classic classic = null;
				int studentNums = 0;//合班学生人数
				String classNames = "";//班级信息
				for (int index = 0; index < planCourseList.size() && mergeStatus!=-1; index++) {//判断是否满足合班要求
					TeachingPlanCourse t = teachingPlanCourseService.get(planCourseList.get(index));
					TeachCourseClasses tcClasses = new TeachCourseClasses();
					if(!"Yes".equals(isMerge)){		//判断是否满足合班要求
						//判断是否毕业学期
						termStr = termList.get(index);
						if(ExStringUtils.isNotBlank(termStr) && ExStringUtils.isNotBlank(guidPlanList.get(index))){
							TeachingGuidePlan guidePlan = teachingguideplanservice.get(guidPlanList.get(index));
							int year = Integer.parseInt(termStr.substring(0, 4));
							int term = Integer.parseInt(termStr.substring(6, 7));
							int grade = Integer.parseInt(guidePlan.getGrade().getGradeName().substring(0,4));
							float eduYear = Float.parseFloat(guidePlan.getTeachingPlan().getEduYear());
							if((year-grade)*2+term==eduYear*2-1){
								errorMsg.append("有毕业教学周！<br>");
								mergeStatus = 0;
							}
						}
						if(ExStringUtils.isNotEmpty(courseStr) && !courseStr.equals(courseList.get(index)) || ExStringUtils.isEmpty(courseList.get(index))){
							errorMsg.append("不是同一门课程！<br>");
							mergeStatus = 0;
							
						}
						if(stydyHour!=null && !stydyHour.equals(t.getStydyHour()) || t.getStydyHour()==null){
							errorMsg.append("学时不相同！<br>");
							mergeStatus = 0;
						}
						if(ExStringUtils.isNotEmpty(examClassType) && !examClassType.equals(t.getExamClassType()) || ExStringUtils.isEmpty(t.getExamClassType())){
							errorMsg.append("考试类别不相同！<br>");
							mergeStatus = 0;
						}
						if(ExStringUtils.isNotEmpty(teachingType) && !teachingType.equals(t.getTeachingPlan().getSchoolType()) || ExStringUtils.isEmpty(t.getTeachingPlan().getSchoolType())){
							errorMsg.append("学习形式不相同！<br>");
							mergeStatus = 0;
						}
						if(ExStringUtils.isNotEmpty(classicStr) && !classicStr.equals(t.getTeachingPlan().getClassic().getResourceid()) || ExStringUtils.isEmpty(t.getTeachingPlan().getClassic().getResourceid())){
							errorMsg.append("层次不相同！<br>");
							mergeStatus = 0;
						}
						if(ExStringUtils.isNotEmpty(courseType) && !courseType.equals(t.getCourseType()) || ExStringUtils.isEmpty(t.getCourseType())){
							errorMsg.append("课程性质不相同！<br>");
							mergeStatus = 0;
						}
					}else{//直接合班
						if(ExStringUtils.isNotEmpty(residList.get(index)) && !residList.get(index).equals("null")){//已合班教学班
							
							TeachCourse tCourseTemp = teachCourseService.get(residList.get(index));
							List<TeachCourseClasses> tCourseClasses = teachCourseClassesService.findByHql(" from "+TeachCourseClasses.class.getSimpleName()+" where teachCourse.resourceid=?", tCourseTemp.getResourceid());
							//如果是第一个合班教学班则保留，其它删除
							if(tCourse!=null){
								deletedTeachCourses.add(tCourseTemp);
								for (TeachCourseClasses teachCourseClasses : tCourseClasses) {
									teachCourseClasses.setCourse(course);
									teachCourseClasses.setTeachCourse(tCourse);
									teachCourseClasses.setUpdateDate(new Date());
									teachCourseClasses.setOperator(user);
									teachCourseClasses.setOperatorName(user.getCnName());
								}
							}else {
								tCourse = tCourseTemp;
							}
							tcClassesList.addAll(tCourseClasses);
							studentNums += tCourse.getStudentNumbers();
							classNames += tCourse.getClassNames()+", ";
						}else {					//未合班教学班
							Classes classes = classesService.findUniqueByProperty("resourceid", classesList.get(index));//班级对象
							User teacher = null;
							if(index<teacherList.size()){ //登分老师
								teacher = userService.get(teacherList.get(index));
								if(teacher!=null){
									tcClasses.setRecordScorer(teacher);
									tcClasses.setRecordScorerName(teacher.getCnName());
								}
							}
							if(user!=null){
								tcClasses.setOperator(user);
								tcClasses.setOperatorName(user.getCnName()==null?user.getUsername():user.getCnName());
							}
							tcClasses.setClasses(classes);
							tcClasses.setPlanCourse(t);
							if(index==0){
								tcClasses.setCourse(t.getCourse());
							}else {
								tcClasses.setCourse(course);
							}
							tcClasses.setArrangeStatus(0);
							tcClasses.setUpdateDate(new Date());
							tcClasses.setTeachCourse(tCourse);
							tcClassesList.add(tcClasses);
							studentNums += Integer.parseInt(stuCountList.get(index));//班级人数总和
							classNames += classes.getClassname()+", ";
						}
					}
					if(index==0){
						courseStr=t.getCourse().getResourceid();
						stydyHour=t.getStydyHour();
						examClassType=t.getExamClassType();
						teachingType=t.getTeachingPlan().getSchoolType();
						classicStr=t.getTeachingPlan().getClassic().getResourceid();
						courseType=t.getCourseType();
						classic = t.getTeachingPlan().getClassic();//层次对象
						course = t.getCourse();	//课程对象
					}
				}//遍历结束
				if("Yes".equals(isMerge)){//保存合班信息
					if(tCourse==null){
						tCourse = new TeachCourse();
						tCourse.setClassic(classic);
						tCourse.setCourse(course);
						tCourse.setCourseName(courseNames.replace(",", "/"));
						tCourse.setExamClassType(examClassType);
						tCourse.setStudyHour(stydyHour.intValue());
						if(tcClassesList.size()==1){
							tCourse.setStatus(0);
						}else if (tcClassesList.size()>1) {
							tCourse.setStatus(1);
						}
						tCourse.setGenerateStatus(0);
						tCourse.setPublishStatus(0);
						tCourse.setSelectedStatus(0);
						tCourse.setArrangeStatus(0);
						tCourse.setTeachingtype(teachingType);
						tCourse.setOpenTerm(openTerm);
						tCourse.setCreateDate(new Date());
						String teachingTypeName = dictService.dictCode2Val("CodeTeachingType", teachingType);//学习形式名称
						//教学班命名规则：「上课学期+层次+形式+第一个课程名」
						String tCourseName = openTerm+classic.getClassicName()+teachingTypeName+course.getCourseName();
						tCourse.setTeachingClassname(tCourseName);
						//教学班班号命名规则：「上课学期+3位数随机数」
						String tCourseCode = teachCourseService.getTeachingCode(unitName,openTerm);
						tCourse.setTeachingCode(tCourseCode);
						//排课班级
						for (TeachCourseClasses tcClasses : tcClassesList) {
							tcClasses.setTeachCourse(tCourse);
						}
					}
					if(user!=null){
						tCourse.setOperator(user);
						tCourse.setOperatorName(user.getCnName()==null?user.getUsername():user.getCnName());
					}
					tCourse.setTeachtype(teachType);
					OrgUnit unit = orgUnitService.get(unitName);
					tCourse.setUnit(unit);
					tCourse.setStudentNumbers(studentNums);
					tCourse.setClassNames(classNames.substring(0, classNames.length()-2));
					tCourse.setOperator(user);
					tCourse.setOperatorName(user.getUsername());
					tCourse.setUpdateDate(new Date());
					
					YearInfo yearInfo = yearInfoService.findUniqueByProperty("firstYear", Long.parseLong(openTerm.substring(0, 4)));
					tCourse.setYearInfo(yearInfo);
					tCourse.setTerm(openTerm.substring(6,7));
					teachCourseService.saveOrUpdate(tCourse);
					teachCourseService.batchDelete(deletedTeachCourses);
					teachCourseClassesService.batchSaveOrUpdate(tcClassesList);
					errorMsg.setLength(0);
					errorMsg.append("合班成功！");
				}
			} while (false);
		} catch (Exception e) {
			logger.error("保存出错",e);
			map.put("statusCode", 300);
			map.put("message", "合班失败！");
		}
		map.put("statusCode", 200);
		map.put("message", errorMsg);
		map.put("mergeStatus", mergeStatus);
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 撤销合班
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/arrange/opencourseresult/cancelMerge.html")
	public void cancelMerge(HttpServletRequest request,HttpServletResponse response){
		String resourceid = request.getParameter("resourceid");
		String teachclassesid = request.getParameter("teachclassesid");
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(resourceid)){
				resourceid = ExStringUtils.addSymbol(resourceid.split(","), "'", "'");
				resourceid = resourceid.substring(0, resourceid.length());
				String sql1 = " update edu_arrange_teachcourse tc set tc.isdeleted=1 where tc.resourceid in("+resourceid+")";
				String sql2 = " update edu_arrange_teachclasses tc set tc.isdeleted=1 where tc.teachcourseid in("+resourceid+")";
				baseSupportJdbcDao.getBaseJdbcTemplate().executeForMap(sql1, map);
				baseSupportJdbcDao.getBaseJdbcTemplate().executeForMap(sql2, map);
			}
			if(ExStringUtils.isNotBlank(teachclassesid)){
				teachclassesid = ExStringUtils.addSymbol(teachclassesid.split(","), "'", "'");
				teachclassesid = teachclassesid.substring(0, teachclassesid.length()-1);
				String sql = " update edu_arrange_teachclasses tc set tc.isdeleted=1 where tc.resourceid in("+teachclassesid+")";
				baseSupportJdbcDao.getBaseJdbcTemplate().executeForMap(sql, map);
			}
			map.put("statusCode", 200);
			map.put("message", "撤销成功！");
		} catch (Exception e) {
			logger.error("撤销出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "撤销出错！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 预览教学班信息
	 * @param teachCourseid
	 * @param model
	 * @param objPage
	 * @return
	 */
	@RequestMapping("/edu3/arrange/opencourseresult/view.html")
	public String viewTeachCourseInfo(String teachCourseid, ModelMap model, Page objPage){
		if(ExStringUtils.isNotBlank(teachCourseid)){
			TeachCourse tCourse = teachCourseService.get(teachCourseid);
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("teachCourseid", teachCourseid);
			Page page = teachCourseClassesService.findTeachCourseByHql(condition, objPage);
			model.addAttribute("teachCourse", tCourse);
			model.addAttribute("page", page);
		}
		return "/edu3/arrange/openCourseResult/teachCourse-view";
	}
	
	/**
	 * 生成开课结果
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/arrange/opencourseresult/generateresult.html")
	public void generateResult(HttpServletRequest request,HttpServletResponse response)throws WebException{
		
		String resIds 			= 		ExStringUtils.trimToEmpty(request.getParameter("resIds"));
		String pcIds			=		ExStringUtils.trim(request.getParameter("pcIds"));//教学计划课程
		String classesIds		=		ExStringUtils.trim(request.getParameter("classesIds"));//班级
		String terms			=		ExStringUtils.trim(request.getParameter("terms"));//上课学期
		String stuCounts		=		ExStringUtils.trim(request.getParameter("stuCounts"));//学生人数
		String teacherIds		=		ExStringUtils.trimToEmpty(request.getParameter("teacherIds"));//登分老师
		
		List<String> residList = Arrays.asList(resIds.split(","));
		List<String> classesList = Arrays.asList(classesIds.split(","));
		List<String> planCourseList = Arrays.asList(pcIds.split(","));
		List<String> termList = Arrays.asList(terms.split(","));
		List<String> stuCountList = Arrays.asList(stuCounts.split(","));
		List<String> teacherList = Arrays.asList(teacherIds.split(","));
		
		Map<String,Object> map = new HashMap<String, Object>();
		int statusCode = 200;
		String message = "操作成功！";
		try {
			User user = SpringSecurityHelper.getCurrentUser();
			List<TeachCourse> teachCourseList = new ArrayList<TeachCourse>();
			List<TeachCourseClasses> tcClassesList = new ArrayList<TeachCourseClasses>();
			for (int i=0;i<residList.size();i++) {
				if(residList.get(i).equals("null")){
					TeachingPlanCourse t = teachingPlanCourseService.get(planCourseList.get(i));
					Classes classes = classesService.get(classesList.get(i));
					TeachCourse tCourse = new TeachCourse();
					TeachCourseClasses tcClasses = new TeachCourseClasses();
					if(classes!=null){
						tCourse.setClassic(classes.getClassic());//层次
					}
					if(t!=null && t.getCourse()!=null){
						tCourse.setCourse(t.getCourse());//课程
						tCourse.setCourseName(t.getCourse().getCourseName());//课程名称
					}
					String studentNumbers = stuCountList.get(i)==null?"0":stuCountList.get(i);//班级人数
					tCourse.setStudentNumbers(Integer.parseInt(studentNumbers));
					tCourse.setExamClassType(t.getExamClassType());//考试形式
					tCourse.setStudyHour(t.getStydyHour().intValue());//学时
					tCourse.setTeachingtype(t.getTeachingPlan().getSchoolType());//学习形式
					tCourse.setStatus(0);//合班状态
					tCourse.setGenerateStatus(1);//生成状态
					tCourse.setPublishStatus(0);//发布状态
					tCourse.setSelectedStatus(0);//选课状态
					tCourse.setArrangeStatus(0);//排课状态
					//教学班班号命名规则：「上课学期+3位数随机数」
					String tCourseCode = teachCourseService.getTeachingCode(classes.getBrSchool().getResourceid(),termList.get(i));
					tCourse.setTeachingCode(tCourseCode);//教学班号
					tCourse.setTeachingClassname(classes.getClassname());//教学班名
					tCourse.setOperator(user);
					tCourse.setOperatorName(user.getCnName()==null?user.getUsername():user.getCnName());
					tCourse.setUnit(classes.getBrSchool());//教学点
					tCourse.setClassNames(classes.getClassname());//班级信息
					//年度和学期
					YearInfo yearInfo = yearInfoService.findUniqueByProperty("firstYear", Long.parseLong(termList.get(i).substring(0, 4)));
					tCourse.setYearInfo(yearInfo);
					tCourse.setTerm(termList.get(i).substring(6,7));
					tCourse.setOpenTerm(termList.get(i));
					tCourse.setCreateDate(new Date());
					tCourse.setOperator(user);
					tCourse.setOperatorName(user.getUsername());
					//保存排课班级
					tcClasses.setTeachCourse(tCourse);
					//登分老师
					if(ExStringUtils.isNotBlank(teacherList.get(i))){
						User teacher = userService.get(teacherList.get(i));
						tcClasses.setRecordScorer(teacher);
						tcClasses.setRecordScorerName(teacher.getCnName());
					}
					tcClasses.setOperator(user);
					tcClasses.setOperatorName(user.getCnName()==null?user.getUsername():user.getCnName());
					tcClasses.setClasses(classes);
					tcClasses.setPlanCourse(t);
					tcClasses.setCourse(t.getCourse());
					tcClasses.setArrangeStatus(0);
					tcClasses.setCreateDate(new Date());
					tcClasses.setTeachCourse(tCourse);
					//添加到list集合
					tcClassesList.add(tcClasses);
					teachCourseList.add(tCourse);
				}else {
					TeachCourse teachCourse = teachCourseService.get(residList.get(i));
					teachCourse.setGenerateStatus(1);
					teachCourseList.add(teachCourse);
				}
			}
			//保存教学班
			teachCourseService.batchSaveOrUpdate(teachCourseList);
			teachCourseClassesService.batchSaveOrUpdate(tcClassesList);
		} catch (Exception e) {
			logger.error("保存出错",e);
			statusCode = 300;
			message = "操作失败！";
		}finally{
			map.put("statusCode", statusCode);
			map.put("message", message);
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
}
