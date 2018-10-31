package com.hnjk.edu.teaching.controller;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.utils.*;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.controller.FileUploadAndDownloadSupportController;
import com.hnjk.core.support.base.model.AttachInfo;
import com.hnjk.core.support.context.Constants;
import com.hnjk.core.support.context.SystemContextHolder;
import com.hnjk.edu.basedata.model.*;
import com.hnjk.edu.basedata.service.IClassroomService;
import com.hnjk.edu.basedata.service.IEdumanagerService;
import com.hnjk.edu.basedata.service.IYearInfoService;
import com.hnjk.edu.evaluate.model.Questionnaire;
import com.hnjk.edu.evaluate.model.QuestionnaireBatch;
import com.hnjk.edu.evaluate.service.IQuestionnaireBatchService;
import com.hnjk.edu.evaluate.service.IQuestionnaireService;
import com.hnjk.edu.portal.service.ISysMsgService;
import com.hnjk.edu.roll.model.Classes;
import com.hnjk.edu.roll.model.StuChangeInfo;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.roll.service.IClassesService;
import com.hnjk.edu.roll.service.IGraduationQualifService;
import com.hnjk.edu.roll.service.IStuChangeInfoService;
import com.hnjk.edu.roll.service.IStudentInfoService;
import com.hnjk.edu.roll.util.Tools;
import com.hnjk.edu.roll.vo.StudentSignatureVo;
import com.hnjk.edu.teaching.model.*;
import com.hnjk.edu.teaching.service.*;
import com.hnjk.edu.teaching.vo.CourseTimetableVo;
import com.hnjk.edu.teaching.vo.TeachingPlanCourseTimetableVo;
import com.hnjk.edu.util.Condition2SQLHelper;
import com.hnjk.extend.plugin.excel.service.IExportExcelService;
import com.hnjk.extend.plugin.excel.service.IImportExcelService;
import com.hnjk.platform.system.UserOperationLogsHelper;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.Attachs;
import com.hnjk.platform.system.model.Dictionary;
import com.hnjk.platform.system.model.UserOperationLogs;
import com.hnjk.platform.system.service.IAttachsService;
import com.hnjk.platform.system.service.IDictionaryService;
import com.hnjk.platform.taglib.JstlCustomFunction;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.OrgUnit;
import com.hnjk.security.model.OrgUnitExtends;
import com.hnjk.security.model.User;
import com.hnjk.security.model.UserExtends;
import com.hnjk.security.service.IOrgUnitService;
import com.hnjk.security.service.IUserService;
import jxl.CellView;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.*;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.Boolean;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.rmi.ServerException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 年级教学计划课程开课、排课管理.
 * <code>TeachingPlanCourseArrangeController</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2013-4-10 下午04:37:39
 * @see 
 * @version 1.0
 */
@Controller
public class TeachingPlanCourseArrangeController extends FileUploadAndDownloadSupportController {
	private static final long serialVersionUID = -197131771191281727L;
	
	@Autowired
	@Qualifier("teachingPlanCourseStatusService")
	private ITeachingPlanCourseStatusService teachingPlanCourseStatusService;
	
	@Autowired
	@Qualifier("teachingPlanCourseTimetableService")
	private ITeachingPlanCourseTimetableService teachingPlanCourseTimetableService;

	@Autowired
	@Qualifier("teachingPlanCourseExaminationService")
	private  ITeachingPlanCourseExaminationService teachingPlanCourseExaminationService;
	
	@Autowired
	@Qualifier("teachingguideplanservice")
	private ITeachingGuidePlanService teachingGuidePlanService;
	
	@Autowired
	@Qualifier("teachingPlanCourseService")
	private ITeachingPlanCourseService teachingPlanCourseService;
	
	@Autowired
	@Qualifier("classesService")
	private IClassesService classesService;
	
	@Autowired
	@Qualifier("courseService")
	private ICourseService courseService;
	
	@Autowired
	@Qualifier("classroomService")
	private IClassroomService classroomService;
	
	@Autowired
	@Qualifier("edumanagerService")
	private IEdumanagerService edumanagerService;
	
	@Autowired
	@Qualifier("studentinfoservice")
	private IStudentInfoService studentInfoService;
	
	@Autowired
	@Qualifier("exportExcelService")
	private IExportExcelService exportExcelService;
	
	@Autowired
	@Qualifier("teachingPlanCourseTimePeriodService")
	private ITeachingPlanCourseTimePeriodService teachingPlanCourseTimePeriodService;
	
	@Autowired
	@Qualifier("attachsService")
	private IAttachsService attachsService;
	
	@Autowired
	@Qualifier("dictService")
	private IDictionaryService dictionaryService;
	
	@Autowired
	@Qualifier("importExcelService")
	private IImportExcelService importExcelService;
	
	@Autowired
	@Qualifier("courseStatusClService")
	private ICourseStatusClService courseStatusClService;
	
	@Autowired
	@Qualifier("checkOpenCourseService")
	private ICheckOpenCourseService checkOpenCourseService;
	
	@Autowired
	@Qualifier("examResultsService")
	private IExamResultsService examResultsService;
	
	@Autowired
	@Qualifier("sysMsgService")
	private ISysMsgService sysMsgService;
	
	@Autowired
	@Qualifier("userService")
	private IUserService userService;
	
	@Autowired
	@Qualifier("teachingplanservice")
	private ITeachingPlanService teachingPlanService;
	
	@Autowired
	@Qualifier("orgUnitService")
	private IOrgUnitService orgUnitService;

	@Autowired
	@Qualifier("graduationQualifService")
	private IGraduationQualifService graduationQualifService;

	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;
	
	@Autowired
	@Qualifier("examSubService")
	private IExamSubService examSubService;

	@Autowired
	@Qualifier("teachingJDBCService")
	private ITeachingJDBCService teachingJDBCService;
	
	@Autowired
	@Qualifier("yearInfoService")
	private IYearInfoService yearInfoService;
	
	@Autowired
	@Qualifier("stuchangeinfoservice")
	private IStuChangeInfoService stuchangeinfoservice;
	
	@Autowired
	@Qualifier("iQuestionnaireBatchService")
	private IQuestionnaireBatchService iQuestionnaireBatchService;
	
	@Autowired
	@Qualifier("iQuestionnaireService")
	private IQuestionnaireService iQuestionnaireService;

	/**
	 * 年级教学计划课程开课状态列表
	 */
	@RequestMapping("/edu3/teaching/teachingplancoursestatus/list.html")
	public String listTeachingPlanCourseStatus(String gradeid, String major, String classicid, String brSchoolid, String teachingType, String term, String isOpen, HttpServletRequest request, ModelMap model, Page objPage) throws WebException {
//		List<Major> majors = majorService.findByHql(" from "+Major.class.getSimpleName()+" where isDeleted= 0 order by majorCode asc ");
//		StringBuffer majorOption = new StringBuffer("<option value=''></option>");
		String courseId = ExStringUtils.trimToEmpty(request.getParameter("courseId"));
		String major1=ExStringUtils.trimToEmpty(request.getParameter("major"));
//		OrgUnit unit = orgUnitService.get(null == brSchoolid ? "" : brSchoolid);
//		if(null != unit){
//			model.addAttribute("unitname", unit.getUnitName());
//		}
//		for (Major m : majors) {
//			String majorCode = ExStringUtils.isNotEmpty(m.getMajorCode()) ? m.getMajorCode() : "";
//			if(ExStringUtils.isNotEmpty(majorid)&&majorid.equals(m.getResourceid())){
//				majorOption.append("<option value ='"+m.getResourceid()+"' selected='selected' >"+majorCode+"-"+m.getMajorName()+"</option>");
//			}else{
//				majorOption.append("<option value ='"+m.getResourceid()+"'>"+majorCode+"-"+m.getMajorName()+"</option>");
//			}
//			
//		}
//		model.addAttribute("majorOption", majorOption);
		listTeachingPlanCourseArrange(gradeid, major1, classicid, brSchoolid,teachingType, term, null, "status", request, model,objPage,courseId, "", "");
		model.addAttribute("majorSelect5",graduationQualifService.getGradeToMajor1(gradeid,major1,"query_examresults_major","major","searchExamResultMajorClick()",classicid));
		return "/edu3/teaching/teachingplancoursearrange/coursestatus-list";
	}
	
	/**
	 * 教学计划课程序列号列表
	 * @param request
	 * @param model
	 * @param objPage
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/teachingplancourse_code/list.html")
	public String listTeachingPlanCourseCode(HttpServletRequest request, ModelMap model , Page objPage) throws WebException {
		Map<String, Object> condition = Condition2SQLHelper.getConditionFromResquestByIterator(request);
		User user = SpringSecurityHelper.getCurrentUser();
		if(user.getOrgUnit().getUnitType().equals(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())){//如果为校外学习中心人员
			String unitid = ExStringUtils.trim(user.getOrgUnit().getResourceid());
			model.addAttribute("isBrschool", true); 
			model.addAttribute("school",unitid);
			model.addAttribute("schoolname",ExStringUtils.trim(user.getOrgUnit().getUnitName()));
			condition.put("unitid", unitid);
		}
		Page courseCodePage = teachingPlanCourseStatusService.findTeachingPlanCourseCodeByCondition(condition,objPage);
		model.addAttribute("condition", condition);
		model.addAttribute("courseCodePage", courseCodePage);
		return "/edu3/teaching/teachingplan/coursecode-list";
	}
	
	/**
	 * 设置开课学期
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/teachingplancoursestatus/settime.html")
	 public String setTeachPlanCourseTime(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws WebException {
			
		model.addAttribute("resourceid", ExStringUtils.trimToEmpty(request.getParameter("resourceid")));
		model.addAttribute("guiplanid",ExStringUtils.trimToEmpty(request.getParameter("guiplanid")));
		model.addAttribute("plancourseid",ExStringUtils.trimToEmpty(request.getParameter("plancourseid")));
		model.addAttribute("isOpen", ExStringUtils.trimToEmpty(request.getParameter("isOpen")));
		return "/edu3/teaching/teachingplancoursearrange/setTeachPlancourseTime";
	}
	
	/**
	 * 开课/取消开课OLD
	 * @param resourceid
	 * @param guiplanid
	 * @param plancourseid
	 * @param isOpen
	 * @param response
	 * @throws WebException
	 * TODO:旧方法已废弃，在方法后面加一个_old
	 */
	@RequestMapping("/edu3/teaching/teachingplancoursestatus/save_old.html")
	public void saveTeachingPlanCourseStatus_old(String termid,String resourceid,String term,String guiplanid,String plancourseid, String isOpen, HttpServletResponse response) throws WebException {
		Map<String, Object> map 			= 	new HashMap<String, Object>();
		OrgUnit unit			= 	null;
	    for(int k=0;k<1;k++){
	    	/**
			 * 是否学校中心教务员
			 */
			User user = SpringSecurityHelper.getCurrentUser();//当前登录用户
			if(!CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())){
				map.put("statusCode", 300);
				map.put("message", "你不是该学校中心教务员，没有权限操作此功能！");
				break;
			}else{
				unit = user.getOrgUnit();
			}
			
			if(null == unit){
				map.put("statusCode", 300);
				map.put("message", "无法识别的教学点！");
				break;
			}
			String schoolId     =  unit.getResourceid(); //教学点id
			isOpen = ExStringUtils.defaultIfEmpty(isOpen, Constants.BOOLEAN_NO);
			String msg = (Constants.BOOLEAN_NO.equals(isOpen)?"取消":"")+"开课";
			try {			
				List<TeachingPlanCourseStatus> courseStatusList = new ArrayList<TeachingPlanCourseStatus>();
				List<TeachingPlanCourseTimetable> timetableList = new ArrayList<TeachingPlanCourseTimetable>();
				//Map<String, Object> condition = new HashMap<String, Object>();
				if(ExStringUtils.isNotBlank(plancourseid) && ExStringUtils.isNotBlank(guiplanid)){
					String[] res = resourceid.split("\\,");
					String[] gids = guiplanid.split("\\,");
					String[] pcids = plancourseid.split("\\,");
					String[] tids  = termid.split("\\,");
					for (int i = 0; i < pcids.length; i++) {
						TeachingPlanCourseStatus courseStatus = null;
						if(ExStringUtils.isNotBlank(res[i])){//编辑
							courseStatus = teachingPlanCourseStatusService.get(ExStringUtils.trimToEmpty(res[i]));
							if(Constants.BOOLEAN_NO.equals(isOpen)){//取消开课,排课记录也要删除
//								condition.clear();
//								condition.put("plancourseid", courseStatus.getTeachingPlanCourse().getResourceid()); 
//								condition.put("gradeid", courseStatus.getTeachingGuidePlan().getGrade().getResourceid());	
								/**
								 * 现在取消开课要审核了
								 */
//								List<TeachingPlanCourseTimetable> list = teachingPlanCourseTimetableService.findTeachingPlanCourseTimetableByCondition(condition);
//								if(ExCollectionUtils.isNotEmpty(list)){
//									timetableList.addAll(list);
//								}
							}
						} else if(ExStringUtils.isNotBlank(gids[i]) && ExStringUtils.isNotBlank(pcids[i])){//新增
							List<TeachingPlanCourseStatus> _courseStatusList = teachingPlanCourseStatusService.findByCondition(gids[i], pcids[i], schoolId,"Y");
							// 防止并发开课
							if(_courseStatusList!=null && _courseStatusList.size()>0){
								continue;
							}
							courseStatus = new TeachingPlanCourseStatus();
							courseStatus.setTeachingGuidePlan(teachingGuidePlanService.get(gids[i]));
							TeachingPlanCourse planCourse = teachingPlanCourseService.get(pcids[i]);
							courseStatus.setTeachingPlanCourse(planCourse);		
						}
						if(courseStatus != null){
							/**
							 * 不能对待审核的数据进行操作
							 */
							if("openW".equals(courseStatus.getCheckStatus())|| "cancelW".equals(courseStatus.getCheckStatus())|| "updateW".equals(courseStatus.getCheckStatus())){
								map.put("statusCode", 300);
								map.put("message", "你选择的课程处于审核状态，不能进行此操作！");
								break;
							}
							
							/**
							 * 开课审核信息表
							 */
							CheckOpenCourse coc = null;
							List<CheckOpenCourse> listCoc =  checkOpenCourseService.findByHql(" from "+CheckOpenCourse.class.getSimpleName()+" where isDeleted = 0 and courseStatusId.resourceid = ? and applyName = ? and checkStatus = 'W' "
									, courseStatus.getResourceid(),user.getUsername());						
									
							if(null == listCoc || listCoc.size() <= 0 ){
								 coc = new CheckOpenCourse();
							}else{
								coc = listCoc.get(0);
							}
									
							term 			 	=  (null == term || ExStringUtils.isBlank(term) ? "" : term); //要改变的学期
							String beforTerm 	=  (null == courseStatus.getTerm() ? "":courseStatus.getTerm()); //以前的学期
							String schoolName   =  unit.getUnitName(); //教学点名称
							//年级教学计划
							TeachingGuidePlan p = courseStatus.getTeachingGuidePlan();
							//教学计划id
							String planid = null == p.getTeachingPlan() ? "" : p.getTeachingPlan().getResourceid();
							
							if(Constants.BOOLEAN_NO.equals(isOpen)){//取消开课
								if("Y".equals(ExStringUtils.trim(courseStatus.getIsOpen()))){//取消开课 只处理已经开课的信息
									/**
									 * 录入成绩后不能取消开课
									 */					
									List<ExamResults> list = examResultsService.findByHql(" from "+ExamResults.class.getSimpleName()+" where isDeleted=0 and majorCourseId = ? and studentInfo.teachingPlan.resourceid = ? "
											+ "and studentInfo.branchSchool.resourceid = ? and studentInfo.grade.resourceid= ? "
											,null == courseStatus.getTeachingPlanCourse()?"":courseStatus.getTeachingPlanCourse().getResourceid(),planid,schoolId,p.getGrade().getResourceid() );								
									if(null != list && list.size() > 0){
										map.put("statusCode", 300);
										map.put("message", "该课程已录入分数，不允许取消开课！");
										break;
									}
									coc.setCheckStatus("W"); //待审核
									coc.setOperate("cancel"); //取消开课操作
									courseStatus.setCheckStatus("cancelW"); //取消开课待审核
								}else{
									map.put("statusCode", 300);
									map.put("message", "该课程还未开课，无法进行取消开课操作！");
									break;
								}
							} else {
								/**
								 * 如果所选的学期与教学计划的学期一致就不必审核 否则要审核
								 */
								String _year = "";
								Long   year1 = 0L;
								Double year2 = 0.0;
								String _term = "";
								Double   _addTerm = (Double.parseDouble(tids[i])-1.0)/2;
								
								
								if( null != p && null != p.getGrade() && null != p.getGrade().getYearInfo() ){									
									year1 = p.getGrade().getYearInfo().getFirstYear();
									_term = p.getGrade().getTerm();
									if("2".equals(ExStringUtils.trim(_term))){
										_year = (year1+1) + ".5";
									}else if("1".equals(ExStringUtils.trim(_term))){
										_year = year1 + ".0";
									}	
									year2 = Double.parseDouble(_year)+_addTerm;
								}
								
								String term_1 = "0.0";
								if(ExStringUtils.isNotBlank(term)){
									String[] str = term.split("_");
									if(null != str && str.length >= 2){
										if("02".equals(ExStringUtils.trim(str[1]))){
											term_1 = ExStringUtils.trim(str[0])+".5";
										}else{
											term_1 = ExStringUtils.trim(str[0])+".0";
										}
									}
								}							
								if(ExStringUtils.isBlank(courseStatus.getTerm()) && null == courseStatus.getTerm()){ //开课
									
//									if(Double.parseDouble(term_1) - year2 < 0){
//										map.put("statusCode", 300);
//										map.put("message", "你不能对原计划学期之前的数据进行开课操作！");
//										renderJson(response, JsonUtils.mapToJson(map));
//										break;
//									}
									
									if(ExStringUtils.trim(year2+"").equals(ExStringUtils.trim(term_1))){ //与教学计划学期一致
										coc.setCheckStatus("Y");//通过
										courseStatus.setOpenStatus("Y"); //开课状态
										courseStatus.setIsOpen("Y");//是否开课
										courseStatus.setTerm(term);//设置学期
										coc.setOperate("open"); //开课操作
										courseStatus.setCheckStatus("openY");//开课审核通过
										courseStatusList.add(courseStatus);
									}else{
										courseStatus.setCheckStatus("openW");//开课待审核
										coc.setCheckStatus("W");
										courseStatus.setOpenStatus("Y");
										coc.setOperate("open"); //开课操作
									}
									
								}else{ //调学期
//									if(Double.parseDouble(term_1) - year2 < 0){
//										map.put("statusCode", 300);
//										map.put("message", "你不能对原计划学期之前的数据进行调课操作！");
//										renderJson(response, JsonUtils.mapToJson(map));
//										break;
//									}
									if(ExStringUtils.trim(term).equals(ExStringUtils.trim(courseStatus.getTerm()))){ //所选学期和现在学期一致
										map.put("statusCode", 300);
										map.put("message", "你现在正处于所选学期,无需进行调课操作!");
										break;
									}
									
									/**
									 * 录入成绩后不能调整开课 
									 */					
									Map<String, Object> params = new HashMap<String, Object>();
									params.put("planCourse", null == courseStatus.getTeachingPlanCourse()?"":courseStatus.getTeachingPlanCourse().getResourceid());
									params.put("planId", planid);
									params.put("schoolId", schoolId);
									params.put("gradeId", p.getGrade().getResourceid());
									/*List<ExamResults> list = examResultsService.findByHql(" from "+ExamResults.class.getSimpleName()+" where isDeleted=0 and majorCourseId = ? and studentInfo.teachingPlan.resourceid = ? and studentInfo.branchSchool.resourceid = ? "
											,null == courseStatus.getTeachingPlanCourse()?"":courseStatus.getTeachingPlanCourse().getResourceid(),planid ,schoolId);								
									if(null != list && list.size() > 0){*/
									/*if(examResultsService.hasExamResults(params)){
										map.put("statusCode", 300);
										map.put("message", "该课程已录入分数，不允许调整开课！");
										break;
									}*/
									courseStatus.setCheckStatus("updateW");//开课待审核
									coc.setCheckStatus("W");						
									coc.setOperate("update"); //调整开课操作
								}
							}
							courseStatus.setSchoolName(schoolName);//教学点
							courseStatus.setSchoolIds(schoolId);//教学点id
							coc.setCourseStatusId(courseStatus); //年级教学计划课程开课对象
							coc.setUpdateTerm(term); //要调整的学期
							coc.setTerm(beforTerm); //之前的学期
							coc.setApplyName(user.getUsername());//申请人
							coc.setApplyTime(new Date());//申请时间
							coc.setApplyid(user.getResourceid());//申请人id
							teachingPlanCourseStatusService.saveOrUpdate(courseStatus); //修改或者添加 年级教学计划课程
							checkOpenCourseService.saveOrUpdate(coc); //修改或者添加
							//courseStatusList.add();	 //加入list				
						}
					}
				}
				teachingPlanCourseStatusService.saveOrUpdateCourseStatusList(courseStatusList, timetableList);				
				if(map.get("statusCode")==null || (Integer)map.get("statusCode")!=300){
					map.put("statusCode", 200);
					map.put("message", msg+"成功");
				}
			} catch (Exception e) {
				logger.error(msg+"失败:{}", e.fillInStackTrace());
				map.put("statusCode", 300);
				map.put("message", msg+"失败:"+e.getMessage());
			}
	    }
		
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 开课选择框
	 * TODO:这个是旧方法（目前不用，转移到getOpenCourseInfo这个方法）
	 */
	@RequestMapping("/edu3/teaching/teachingplancoursestatus/getOpenCourseSelect.html")
	public void getOpenCourseSelect(String termid,String resourceid,String term,String guiplanid,String plancourseid, String isOpen, HttpServletResponse response) throws WebException {
		Map<String, Object> map 			= 	new HashMap<String, Object>();
		StringBuffer select  = new StringBuffer("<select  id='setTeachPlanCourseTime1'  name='setTeachPlanCourseTime1' >");		
		try {
			if(ExStringUtils.isNotBlank(plancourseid) && ExStringUtils.isNotBlank(guiplanid)){
				//String[] res = resourceid.split("\\,");
				String[] gids = guiplanid.split("\\,");
				String[] pcids = plancourseid.split("\\,");
				//String[] tids  = termid.split("\\,");
				for (int i = 0; i < pcids.length; i++) {
					if(ExStringUtils.isNotBlank(gids[i])){
						//courseStatus = teachingPlanCourseStatusService.get(ExStringUtils.trimToEmpty(res[i]));
						TeachingPlanCourse p_course 		= 		teachingPlanCourseService.get(pcids[i]);//教学计划课程
						TeachingPlan plan				= 		p_course.getTeachingPlan(); //教学计划
						TeachingGuidePlan p_grade 			= 		teachingGuidePlanService.get(gids[i]);//年级教学计划
						String			   edu_year			= 		plan.getEduYear(); //学制
						Double			   num				= 		Double.parseDouble(edu_year)*2; //循环次数
						Grade grade			= 		p_grade.getGrade();//年级
						Long 			   hh 				= "1".equals(grade.getTerm()) ?grade.getYearInfo().getFirstYear():grade.getYearInfo().getFirstYear()+1L;
						List<Dictionary>   dict_tearm  		= 		CacheAppManager.getChildren("CodeCourseTermType");//从缓存中获取字典对象
						String str = "";
						for(int k = 0 ; k < num ; k ++){						
							if(null == str || "".equals(ExStringUtils.trim(str))){
								str = (hh+"_01");
							}else{
								if(ExStringUtils.trim(str).equals(hh+"_01")){
									str = (hh+"_02");
								}else if(ExStringUtils.trim(str).equals(hh+"_02")){
									hh++;
									str = (hh+"_01");
								}
							}
							for(Dictionary d : dict_tearm){
								String s=select.toString();
								
								if(ExStringUtils.trim(str).equals(ExStringUtils.trim(d.getDictValue()))&&!s.contains(d.getDictValue())){
									select.append("<option value='"+d.getDictValue()+"'>"+d.getDictName()+"</option>");
								}
							}
						}
					} 					
				}
				select.append("</select>");
				map.put("status", 200);
				map.put("msg", select.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", 300);
			map.put("msg", "加载学期下拉框失败！");
			renderJson(response, JsonUtils.mapToJson(map));
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 已开课的年级教学计划列表
	 */
	@RequestMapping("/edu3/teaching/teachingplancoursearrange/list.html")
	public String listTeachingPlanCourseArrange(String gradeid, String majorid, String classicid, String brSchoolid, String teachingType, String term, String classesid, String status, HttpServletRequest request, HttpServletResponse response, ModelMap model, Page objPage) throws WebException {
		Long start = System.currentTimeMillis();
		String courseId  = request.getParameter("courseId"); //课程
		boolean isBrschool = false;
		objPage.setPageSize(50);
		
		User user = SpringSecurityHelper.getCurrentUser();
		if(user.getOrgUnit().getUnitType().equals(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())){//如果为校外学习中心人员
			isBrschool = true;
			brSchoolid = user.getOrgUnit().getResourceid();
			model.addAttribute("school",brSchoolid);
			model.addAttribute("schoolname",ExStringUtils.trim(user.getOrgUnit().getUnitName()));
		}
	
		String showList = ExStringUtils.trim(request.getParameter("showList"));
		Long start1 = System.currentTimeMillis();
		String flag = request.getParameter("flag");
		String resIds = request.getParameter("resIds");
		
		List<Map<String, Object>> results = new ArrayList<Map<String,Object>>();
		Map<String, Object> condition = new HashMap<String, Object>();
		if("export".equals(flag)){
			if(ExStringUtils.isNotBlank(resIds)){//勾选导出
				String[] classesIds = request.getParameter("classesIds").split(",");
				String[] pcIds = request.getParameter("pcIds").split(",");
				if(classesIds.length==pcIds.length && classesIds.length>0){
					String param = "";
					for (int i = 0; i < pcIds.length; i++) {
						param += "('"+classesIds[i]+"','"+pcIds[i]+"'),";
					}
					condition.put("selectedExportParam", param.substring(0, param.length()-1));
				}
			}else {//查询导出
				condition = Condition2SQLHelper.getConditionFromResquestByIterator(request);
				if(condition.containsKey("examSubId")){
					ExamSub examSub = examSubService.get(condition.containsKey("examSubId"));
					//用简单的方式判断正考逻辑
					if(examSub != null && "N".equals(examSub.getExamType())){
						condition.put("isFinalExam", "Y");
					}else{
						condition.put("isFinalExam", "N");
					}
				}
			}
			condition.put("isOpen", Constants.BOOLEAN_YES);
			condition.put("optype", "timetable");
			results = teachingPlanCourseStatusService.findTeachingPlanCourseByCondition(condition);
			model.addAttribute("coursePage", results);
			String schoolName = CacheAppManager.getSysConfigurationByCode("graduateData.schoolName").getParamValue();
			String fileName = "排课详情一览表";
			try {
				fileName = URLEncoder.encode(schoolName+fileName+".xls", "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			response.setContentType("application/vnd.ms-excel");
		  	response.setHeader("Content-Disposition", "attachment; filename="+fileName);
			return "/edu3/teaching/teachingplancoursearrange/coursestatus-forarrange-export";
		}else if (ExStringUtils.isNotBlank(showList)) {
			listTeachingPlanCourseArrange(gradeid, majorid, classicid, brSchoolid,teachingType, term, classesid, "timetable",request, model,objPage,courseId,status,showList);
		}
		Long end1 = System.currentTimeMillis();
		logger.info("排课页面执行时间 end1-start1："+(end1-start1)+" ms");
		model.addAttribute("isBrschool", isBrschool); 
		model.addAttribute("isDefault", CacheAppManager.getSysConfigurationByCode("batchSetDefaultTeacher").getParamValue());
		Long end = System.currentTimeMillis();
		logger.info("排课页面执行时间："+(end-start)+" ms");
		return "/edu3/teaching/teachingplancoursearrange/coursestatus-forarrange-list";
	}
	
	private void listTeachingPlanCourseArrange(String gradeid, String majorid,
											   String classicid, String brSchoolid, String teachingType, String term, String classesid, String optype
			, HttpServletRequest request, ModelMap model, Page objPage, String courseId, String status, String showList) {
		String teachername = ExStringUtils.trim(request.getParameter("teachername"));
		String lecturer = ExStringUtils.trim(request.getParameter("lecturer"));
		String examSubId = ExStringUtils.trim(request.getParameter("examSubId"));
		String isAdjust = ExStringUtils.trim(request.getParameter("isAdjust"));
		String exportType = ExStringUtils.trim(request.getParameter("exportType"));
		
		Map<String, Object> condition = new HashMap<String, Object>();
		User user = SpringSecurityHelper.getCurrentUser();
		if(user.getOrgUnit().getUnitType().equals(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())){//如果为校外学习中心人员
			brSchoolid = ExStringUtils.trim(user.getOrgUnit().getResourceid());
			condition.put("isBrschool", Constants.BOOLEAN_YES);
			model.addAttribute("isBrschool", true); 
		}
		if(ExStringUtils.isNotBlank(brSchoolid)){
			String schoolname = orgUnitService.get(brSchoolid).getUnitName();
			model.addAttribute("school",brSchoolid);
			condition.put("schoolname", schoolname);
			model.addAttribute("schoolname",schoolname);
		}
		String orgterm = request.getParameter("orgterm");
		if(ExStringUtils.isNotBlank(showList)){
			condition.put("showList", showList);
		}
		if(ExStringUtils.isNotBlank(status)) {
			condition.put("status", status);
		}
		if(ExStringUtils.isNotBlank(gradeid)) {
			condition.put("gradeid", gradeid);
		}
		if(ExStringUtils.isNotBlank(courseId)) {
			condition.put("courseId", courseId);
		}
		if(ExStringUtils.isNotBlank(majorid)) {
			condition.put("majorid", majorid);
			condition.put("major", majorid);
		}
		if(ExStringUtils.isNotBlank(classicid))
		{
			condition.put("classicid", classicid);
		}
		if(ExStringUtils.isNotBlank(teachingType)) {
			condition.put("teachingType", teachingType);
		}
		if(ExStringUtils.isNotBlank(brSchoolid)) {
			condition.put("brSchoolid", brSchoolid);
		}
		//开课学期
		if(ExStringUtils.isNotBlank(term)){
			condition.put("term", term);
		}
		//原计划学期
		if(ExStringUtils.isNotBlank(orgterm)){
			condition.put("orgterm", orgterm);
		}
		if(ExStringUtils.isNotBlank(classesid)){
			condition.put("classesid", classesid);
		}
		if(ExStringUtils.isNotBlank(teachername)){
			condition.put("teachername", teachername);
		}
		String isOpenStatus = request.getParameter("isOpenStatus");
		String isOpenCourse = request.getParameter("isOpenCourse");
		//开课状态
		if(ExStringUtils.isNotBlank(isOpenStatus)) {
			condition.put("isOpenStatus", isOpenStatus);
		}
		//是否开课
		if(ExStringUtils.isNotBlank(isOpenCourse)){
			condition.put("isOpenCourse", isOpenCourse);
		}
		//讲课老师名字模糊查询
		if(ExStringUtils.isNotBlank(lecturer)){
			condition.put("lecturer", lecturer);
		}
		String teachType = request.getParameter("teachType");
		//增加课程类型
		if(ExStringUtils.isNotBlank(teachType)){
			condition.put("teachType", teachType);
		}
		//是否调整开课
		if(ExStringUtils.isNotBlank(isAdjust)){
			condition.put("isAdjust", isAdjust);
		}
		//考试批次ID
		if(ExStringUtils.isNotBlank(examSubId)){
			condition.put("examSubId", examSubId);
			ExamSub examSub = examSubService.get(examSubId);
//			List<String> dictCodeList = new ArrayList<String>();
//			dictCodeList.add("ExamResult");
//			Map<String , Object> dictMap = dictionaryService.getDictionByMap(dictCodeList, true, IDictionaryService.PREKEY_TYPE_BYCODE);
			
			//用简单的方式判断正考逻辑
			if(examSub != null && "N".equals(examSub.getExamType())){
				condition.put("isFinalExam", "Y");
			}else{
				condition.put("isFinalExam", "N");
			}
		}
		
		if("timetable".equals(optype)) {
			condition.put("isOpen", Constants.BOOLEAN_YES);
		}
		if (ExStringUtils.isNotBlank(exportType)) {
			condition.put("exportType",exportType);
		}
		condition.put("optype", optype);
		Page coursePage = null;
		Long start2 = System.currentTimeMillis();
		if(!"timetable".equals(optype)) {
			condition.put("openCourse", "Y");
			coursePage = teachingPlanCourseStatusService.findTeachingPlanCourseStatusMapByConditionNew(condition, objPage);
		}else{//导出课表
			if(objPage == null){//查询导出
				List<Map<String, Object>> courseList = teachingPlanCourseStatusService.findTeachingPlanCourseByCondition(condition);
				model.addAttribute("courseList", courseList);
			}else{//勾选导出
				coursePage = teachingPlanCourseStatusService.findTeachingPlanCourseStatusMapByCondition(condition, objPage);
			}
		}
		Long end2 = System.currentTimeMillis();
		logger.info("排课页面执行时间 end2-start2："+(end2-start2)+" ms");
		model.addAttribute("coursePage", coursePage);
		model.addAttribute("condition", condition);
	}
	
	
	/**
	 *开课审核列表
	 */
	@RequestMapping("/edu3/sysmanager/exclassroom/listcheck.html")
	public String checklistTeachingPlanCourseStatus(String gradeid, String major, String classicid, String brSchoolid, String teachingType, String term, String isOpen, String checkStatus, HttpServletRequest request, ModelMap model, Page objPage) throws WebException {
		checklistTeachingPlanCourseArrange(gradeid, major, classicid, brSchoolid,teachingType, term, null, "status", request, model,objPage,checkStatus);
		return "/edu3/teaching/teachingplancoursearrange/coursestatus-list-check";
	}
	
	private void checklistTeachingPlanCourseArrange(String gradeid, String major,
													String classicid, String brSchoolid, String teachingType, String term, String classesid, String optype, HttpServletRequest request, ModelMap model, Page objPage, String checkStatus) {
		Map<String, Object> condition = new HashMap<String, Object>();

		String checkSchool=ExStringUtils.trimToEmpty(request.getParameter("checkSchool"));//学习中心
		String checkpp=ExStringUtils.trimToEmpty(request.getParameter("checkpeople"));
		String operation=ExStringUtils.trimToEmpty(request.getParameter("operation"));
		String courseId=ExStringUtils.trimToEmpty(request.getParameter("courseId"));
		String orgterm = request.getParameter("orgterm");
		
		User user = SpringSecurityHelper.getCurrentUser();
		String schoolname = "适用于所有教学点";
		if(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())){
			checkSchool = user.getOrgUnit().getResourceid();
			condition.put("isBrschool", Constants.BOOLEAN_YES);
			model.addAttribute("isBrschool", true); 
			condition.put("schoolname", ExStringUtils.trim(user.getOrgUnit().getUnitName()));
		}
		
		if(ExStringUtils.isNotBlank(gradeid)) {
			condition.put("gradeid", gradeid);
		}
		if(ExStringUtils.isNotBlank(major)) {
			condition.put("majorid", major);
		}
		if(ExStringUtils.isNotBlank(classicid)) {
			condition.put("classicid", classicid);
		}
		if(ExStringUtils.isNotBlank(teachingType)) {
			condition.put("teachingType", teachingType);
		}
		if(ExStringUtils.isNotEmpty(checkSchool)) {
			condition.put("checkSchool", checkSchool);
		}
		//if(ExStringUtils.isNotBlank(brSchoolid)) condition.put("checkSchool", brSchoolid);
		if(ExStringUtils.isNotBlank(term)) {
			condition.put("term", term);//开课学期
		}
		if(ExStringUtils.isNotBlank(orgterm)) {
			condition.put("orgterm", orgterm);//原计划学期
		}
		if(ExStringUtils.isNotBlank(classesid)) {
			condition.put("classId", classesid);
		}
		if(ExStringUtils.isNotBlank(courseId)) {
			condition.put("courseId", courseId);
		}
		if(ExStringUtils.isNotBlank(operation)) {
			condition.put("operation", operation);
		}
		if(ExStringUtils.isNotBlank(checkpp)) {
			condition.put("checkpp", checkpp);
		}
		if("timetable".equals(optype)) {
			condition.put("isOpen", Constants.BOOLEAN_YES);
		}
		if(ExStringUtils.isNotBlank(checkStatus)) {
			condition.put("checkStatus", checkStatus);	 //审核状态
		}
		condition.put("optype", optype);
		condition.put("checklist", "Y");

		Page coursePage;
		if(!"timetable".equals(optype)) {
			coursePage = teachingPlanCourseStatusService.findTeachingPlanCourseStatusMapByConditionNew(condition, objPage);
		}else{
			coursePage = teachingPlanCourseStatusService.findTeachingPlanCourseStatusMapByCondition(condition, objPage);
		}
		
//		List<CheckOpenCourse> listCoc =  checkOpenCourseService.findByHql(" from "+CheckOpenCourse.class.getSimpleName()+" where isDeleted = 0 and checkStatus = ? ", "W");
//		coursePage.setResult(listCoc);
	//	model.addAttribute("majorSelect4",graduationQualifService.getGradeToMajor1(gradeid,major,"query_examresults_major","major","searchExamResultMajorClick()",classicid));
		model.addAttribute("coursePage", coursePage);
		model.addAttribute("condition", condition);
		model.addAttribute("schoolname",schoolname);
		model.addAttribute("checkStatus",checkStatus);//审核状态
	}
	
	/**
	 * 审核课程状态
	 * @param resourceid id
	 * @param status 审核  Y:通过，N不通过  W：待审核
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/teachingplancoursestatus/opencoursecheck.html")
	public void checkCourseStatus(String resourceid,String status,HttpServletResponse response) throws WebException {
		Map<String, Object> map = new HashMap<String, Object>();
		String mes = "";
		
		/**
		 * 是否学校中心教务员
		 */
		User user = SpringSecurityHelper.getCurrentUser();//当前登录用户
//		if(!CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())){
//			map.put("statusCode", 300);
//			map.put("message", "你不是该学校中心教务员，没有权限操作此功能！");
//			renderJson(response, JsonUtils.mapToJson(map));
//		}else{
			try {	
				if(ExStringUtils.isNotBlank(resourceid)){
					String[] res = resourceid.split("\\,");
					CheckOpenCourse c = null;
					if(null != res && res.length > 0){
						for(String str : res){
							c = checkOpenCourseService.get(str.toString().trim()); // 开课审核对象
							mes = checkCourseMes(c,status,user); //开课审核
						}
					}		
					map.put("statusCode", 200);
					map.put("message", mes);
				}else{
					map.put("statusCode", 300);
					map.put("message", "开课审核异常！");
				}
				
			} catch (Exception e) {
				logger.error("开课审核失败:{}", e.fillInStackTrace());
				map.put("statusCode", 300);
				map.put("message", "开课审核失败:"+e.getMessage());
			}
			renderJson(response, JsonUtils.mapToJson(map));
	//	}
	}
	
	private String checkCourseMes(CheckOpenCourse c, String status, User user){
		List<TeachingPlanCourseTimetable> timetableList = new ArrayList<TeachingPlanCourseTimetable>();
		List<TeachingPlanCourseStatus> courseStatusList = new ArrayList<TeachingPlanCourseStatus>();
		String mes = "";
		//获取当前操作用户
		String name =user.getUsername();
		TeachingPlanCourseStatus t 		= 	c.getCourseStatusId(); //获取年级教学计划课程对象
		TeachingGuidePlan p 		= 	t.getTeachingGuidePlan();//年级教学计划
		String 					planid	 	= 	null == p.getTeachingPlan() ? "" : p.getTeachingPlan().getResourceid(); //教学计划id
		StringBuffer			msg 		= 	new StringBuffer("");//消息信息
		String					operate		=	"";
		String					statusOp		= 	"";
		if(null != c && null != t && ExStringUtils.isNotBlank(status)){
			try {
				/**
				 * 录入成绩后不能修改上课学期
				 */		
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("planCourse", null == t.getTeachingPlanCourse()?"":t.getTeachingPlanCourse().getResourceid());
				params.put("planId", planid);
				params.put("schoolId", t.getSchoolIds());
				params.put("gradeId", p.getGrade().getResourceid());
				/*List<ExamResults> list1 = examResultsService.findByHql(" from "+ExamResults.class.getSimpleName()+" where isDeleted=0 "
						+ "and majorCourseId = ? and studentInfo.teachingPlan.resourceid = ? and studentInfo.branchSchool.resourceid = ? "
						+ "and studentInfo.grade.resourceid= ? ",null == t.getTeachingPlanCourse()?"":t.getTeachingPlanCourse().getResourceid(),
								planid ,t.getSchoolIds(),p.getGrade().getResourceid());		
				if(null != list1 && list1.size() > 0){*/
				/*if(examResultsService.hasExamResults(params)){
					mes = "该课程已录入分数，不允许再进行审核！";					
				}else{*/
					if("Y".equals(ExStringUtils.trim(status))){//通过
						if("cancel".equals(ExStringUtils.trim(c.getOperate()))){ //取消开课
							//审核通过 （取消开课）要把排课信息也删除
							Map<String, Object> condition = new HashMap<String, Object>();
							condition.put("plancourseid", t.getTeachingPlanCourse().getResourceid()); 
							condition.put("gradeid", t.getTeachingGuidePlan().getGrade().getResourceid());							
							List<TeachingPlanCourseTimetable> list = teachingPlanCourseTimetableService.findTeachingPlanCourseTimetableByCondition(condition);
							if(ExCollectionUtils.isNotEmpty(list)){
								timetableList.addAll(list);									
							}						
							c.setCheckStatus("Y"); //通过
							t.setIsOpen("N"); //是否开课
							String tmpTerm = t.getTerm();
							t.setTerm(null); //开课学期
							t.setOpenStatus("N");//开课状态
							t.setCheckStatus("cancelY");//取消开课通过
							operate	=	"取消开课";
							statusOp="<font color='green'>通过</font>";
							//取消开课通过，删除问卷
							deleteQuestionnaire(tmpTerm,c.getCourseStatusId(),c.getOperate());
						}else if("open".equals(ExStringUtils.trim(c.getOperate()))){//开课
							c.setCheckStatus("Y"); //通过
							t.setIsOpen("Y"); //开课
							t.setTerm(c.getUpdateTerm()); //开课学期
							/*//同时更新教学计划课程
							String yearTerm = t.getTerm();
							String year = yearTerm.substring(0, 4);
							String term = yearTerm.substring(6, 7);
							long  tem = Long.parseLong(year) - t.getTeachingGuidePlan().getGrade().getYearInfo().getFirstYear();
							t.getTeachingPlanCourse().setTerm(String.valueOf(tem * 2 + Long.parseLong(term)));*/
							t.setCheckStatus("updateY");//调整开课通过
							t.setAfterterm(c.getTerm());
							t.setCheckStatus("openY");//开课通过
							operate	=	"开课";
							statusOp="<font color='green'>通过</font>";	
						}else{
							c.setCheckStatus("Y"); //通过
							t.setIsOpen("Y"); //开课
							t.setTerm(c.getUpdateTerm()); //开课学期
							/*//同时更新教学计划课程
							String yearTerm = t.getTerm();
							String year = yearTerm.substring(0, 4);
							String term = yearTerm.substring(6, 7);
							long  tem = Long.parseLong(year) - t.getTeachingGuidePlan().getGrade().getYearInfo().getFirstYear();
							t.getTeachingPlanCourse().setTerm(String.valueOf(tem * 2 + Long.parseLong(term)));*/
							t.setCheckStatus("updateY");//调整开课通过
							t.setAfterterm(c.getTerm());
							operate	=	"调整开课";
							statusOp="<font color='green'>通过</font>";
							//开课审核通过时，问卷同样调整
							if("faceTeach".equalsIgnoreCase(t.getTeachType())){//面授类课程才设置问卷
								upateQuestionnaire(c.getUpdateTerm(), c.getCourseStatusId(), c.getTerm(),c.getOperate());
							}
							
						}
					}else{//不通过
						if("cancel".equals(ExStringUtils.trim(c.getOperate()))){ //取消开课
							t.setCheckStatus("cancelN");//取消开课不通过	
							operate	=	"取消开课";
							statusOp="<font color='red'>不通过</font>";	
						}else if("open".equals(ExStringUtils.trim(c.getOperate()))){//开课
							t.setCheckStatus("openN"); //开课不通过
							operate	=	"开课";
							statusOp="<font color='red'>不通过</font>";	
						}else{
							t.setCheckStatus("updateN"); //调整开课不通过
							operate	=	"调整开课";
							statusOp="<font color='red'>不通过</font>";
						}
						c.setCheckStatus("N");
					}
					c.setCheckName(name); //审核人
					c.setCheckTime(new Date());//审核时间	
					msg = buildMsg(msg,c,operate,statusOp);
					courseStatusList.add(t);
					if(ExCollectionUtils.isNotEmpty(courseStatusList)){
						teachingPlanCourseStatusService.saveOrUpdateCourseStatusList(courseStatusList, timetableList);
					}
					mes = "开课审核成功";
			//	}				
			} catch (Exception e) {
				mes = "开课审核失败！";
				e.printStackTrace();
			}	
		}
		User applyUser = userService.get(c.getApplyid());
		if(applyUser!=null){
			sysMsgService.sendMsg(user.getResourceid(),user.getCnName(),user.getOrgUnit(),
					"usermsg","<font color='green'>开课审核结果</font>",msg.toString(),applyUser.getUsername(),"user","",""); //发送信息给申请人
			
		}
		return mes;
	}
	
	private StringBuffer buildMsg(StringBuffer msg, CheckOpenCourse c, String operate, String status){
		try {
			TeachingPlanCourseStatus t 			= 		c.getCourseStatusId();
			TeachingGuidePlan p 			= 		t.getTeachingGuidePlan();
			Grade grade 		= 		p.getGrade();
			TeachingPlan teachPan	=	    p.getTeachingPlan();
			TeachingPlanCourse teachCourse =       t.getTeachingPlanCourse();
			Major major		=       null == teachPan ? null : teachPan.getMajor();
			Classic classic     =       null == teachPan ? null : teachPan.getClassic();
			Course course		= 		null == teachCourse ? null : teachCourse.getCourse();
			//清空信息
			msg.delete(0,msg.length());

			msg.append("[年级]：").append(grade.getGradeName()).append("<br/>[专业]：").append(major.getMajorName()).append("<br/>");
			msg.append("[层次]：").append(classic.getClassicName()).append("<br/>[学习方式]：").append(JstlCustomFunction.dictionaryCode2Value("CodeTeachingType", teachCourse.getTeachType())).append("<br/>");
			msg.append("[课程]：").append(course.getCourseName()).append("<br/>[原计划学期]：").append(JstlCustomFunction.dictionaryCode2Value("CodeTermType", teachCourse.getTerm())).append("<br/>");
			msg.append("[变动前学期]：").append(JstlCustomFunction.dictionaryCode2Value("CodeCourseTermType", c.getTerm())).append("<br/>[变动后学期]：").append(JstlCustomFunction.dictionaryCode2Value("CodeCourseTermType", c.getUpdateTerm())).append("<br/>");
			msg.append("[操作类型]：").append(operate).append("<br/>[审核状态]：").append(status).append("<br/>");
			msg.append("[审核人]：").append(c.getCheckName()).append("<br/>[审核时间]：").append(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(c.getCheckTime())).append("</td><tr>");
			msg.append("[申请人]：").append(c.getApplyName()).append("<br/>[申请时间]：").append(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(c.getApplyTime())).append("<br/>");
		}catch (Exception e) {
			logger.error("检查开课操作失败：", e.fillInStackTrace());
		}
		return msg;
	}
	
	
	
	/**
	 * 排课详情.
	 * @param classesid
	 * @param plancourseid
	 * @param request
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/teachingplancoursetimetable/list.html")
	public String listTeachingPlanCourseTimetable(String classesid, String plancourseid, HttpServletRequest request, ModelMap model) throws WebException {
		String stunumber = request.getParameter("stunumber");
		if(ExStringUtils.isNotBlank(classesid) && ExStringUtils.isNotBlank(plancourseid)){
			Map<String, Object> condition = new HashMap<String, Object>();
			String cid=ExStringUtils.trimToEmpty(request.getParameter("cid"));
			condition.put("classesid", classesid);
			condition.put("plancourseid", plancourseid);
			condition.put("term",ExStringUtils.trimToEmpty(request.getParameter("term")));
			
			List<TeachingPlanCourseTimetable> timetableList = teachingPlanCourseTimetableService.findTeachingPlanCourseTimetableByCondition(condition);
			model.addAttribute("timetableList", timetableList);
			model.addAttribute("condition", condition);	
			
			Classes classes = classesService.get(classesid);

			TeachingPlanCourse planCourse = teachingPlanCourseService.get(plancourseid);
			String ls=ExStringUtils.trim(request.getParameter("teacherName"));
			String lsname="";
			if(!ExStringUtils.isBlank(ls)){
				User user=userService.get(ls);
				lsname=user.getCnName();
			}
			model.addAttribute("stunumber",stunumber);
			model.addAttribute("teacherName",lsname);
			model.addAttribute("teacherid",ls);
			model.addAttribute("classes", classes);
			model.addAttribute("planCourse", planCourse);		
			model.addAttribute("term", ExStringUtils.trimToEmpty(request.getParameter("term")));
			model.addAttribute("cid",cid);//第二层做设置登分老师用
		}
		return "/edu3/teaching/teachingplancoursearrange/coursetimetable-list";
	}

	/**
	 * 新增编辑排课详情
	 * @param resourceid
	 * @param classesid
	 * @param plancourseid
	 * @param request
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/teachingplancoursetimetable/input.html")
	public String editTeachingPlanCourseTimetable(String resourceid, String classesid, String plancourseid, HttpServletRequest request, ModelMap model) throws WebException {
		TeachingPlanCourseTimetable timetable = null;
		String teacherId=ExStringUtils.trimToEmpty(request.getParameter("teacherName"));
		String teacherName="";
		if(!ExStringUtils.isBlank(teacherId)){
			User user=userService.get(teacherId);
			teacherName=user.getCnName();
		}
		
		if(ExStringUtils.isNotBlank(resourceid)){
			timetable = teachingPlanCourseTimetableService.get(resourceid);
			if(!ExStringUtils.isContainsStr(timetable.getOperatorName(), SpringSecurityHelper.getCurrentUser().getCnName())){
				String opeName = timetable.getOperatorName()==null?"":timetable.getOperatorName();
				if(ExStringUtils.isNotEmpty(opeName)){
					opeName += ",";
				}
				opeName += SpringSecurityHelper.getCurrentUser().getCnName();
				timetable.setOperatorName(opeName);
			}
		} else if(ExStringUtils.isNotBlank(classesid) && ExStringUtils.isNotBlank(plancourseid)){
			timetable = new TeachingPlanCourseTimetable();
			Classes classes = classesService.get(classesid);
			TeachingPlanCourse planCourse = teachingPlanCourseService.get(plancourseid);
			timetable.setClasses(classes);
			timetable.setTeachingPlanCourse(planCourse);
			timetable.setCourse(planCourse.getCourse());
			timetable.setOperatorName(SpringSecurityHelper.getCurrentUser().getCnName());
		}
		if(timetable != null){
			if(ExStringUtils.isBlank(timetable.getTerm())){
				timetable.setTerm(request.getParameter("term"));
			}
			model.addAttribute("timetable", timetable);
			
			//教师库
			List<Edumanager> edumanagerList = new ArrayList<Edumanager>();
			List<CourseTeacherCl> teacherList=courseStatusClService.findByHql(" from  "+CourseTeacherCl.class.getSimpleName()+" tcl where isDeleted= ? and tcl.classesId = ?  and tcl.courseid = ?", 0,timetable.getClasses(),timetable.getCourse());
			if(ExCollectionUtils.isNotEmpty(teacherList)){
				CourseTeacherCl teacher = teacherList.get(0);
				if(ExStringUtils.isNotEmpty(teacher.getLecturer())){
					Map<String, Object> param = new HashMap<String, Object>();
					param.put("teacherIds", Arrays.asList(teacher.getLecturer().split(",")));
					edumanagerList = edumanagerService.findEdumanagerByCondition(param);
				}
			}
			model.addAttribute("edumanagerList", edumanagerList);
			model.addAttribute("teacherName",teacherName);
			/*Map<String, Object> param = new HashMap<String, Object>();
			param.put("isDeleted", 0);
			param.put("unitId", timetable.getClasses().getBrSchool().getResourceid());
			param.put("roleCode", CacheAppManager.getSysConfigurationByCode("sysuser.role.teacher").getParamValue());			
			List<Edumanager> edumanagerList = edumanagerService.findEdumanagerByCondition(param);
			model.addAttribute("edumanagerList", edumanagerList);
			model.addAttribute("teacherName",teacherName);
			//TeachingPlanCourseStatus tpcs = teachingPlanCourseStatusService.get(timetable.getPlancourseid());
			List<CourseTeacherCl> list=courseStatusClService.findByHql(" from  "+CourseTeacherCl.class.getSimpleName()+" tcl where isDeleted= ? and tcl.classesId = ?  and tcl.courseid = ?", 0,timetable.getClasses(),timetable.getCourse());
			if(list.size()>0){
				CourseTeacherCl c = list.get(0);
				if(ExStringUtils.isNotBlank(c.getLecturer())){
					//model.addAttribute("lecturerid",c.getLecturer());
					Edumanager e = edumanagerService.get(c.getLecturer());
					model.addAttribute("lecturerid",e.getResourceid());
					model.addAttribute("lecturerName",e.getCnName());
					model.addAttribute("lecturerCode",e.getTeacherCode());
				}
			}*/
		}
		
		return "/edu3/teaching/teachingplancoursearrange/coursetimetable-form";
	}
	/**
	 * 保存上课安排
	 * @param timetable
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/teachingplancoursetimetable/save.html")
	public void saveTeachingPlanCourseTimetable(TeachingPlanCourseTimetable timetable, HttpServletResponse response) throws WebException {
		Map<String, Object> map = new HashMap<String, Object>();
		try {	
			timetable.setIsStoped(ExStringUtils.defaultIfEmpty(timetable.getIsStoped(), Constants.BOOLEAN_NO));
			timetable.setMemo(ExStringUtils.trimToEmpty(timetable.getMemo()));
			if(ExStringUtils.isNotBlank(timetable.getClassesid())){
				timetable.setClasses(classesService.get(timetable.getClassesid()));
			}
			if(ExStringUtils.isNotBlank(timetable.getTeacherId())){
				Edumanager teacher = edumanagerService.get(timetable.getTeacherId());
				timetable.setTeacherName(teacher.getCnName());
			}
			if(ExStringUtils.isNotBlank(timetable.getPlancourseid())){
				TeachingPlanCourse planCourse = teachingPlanCourseService.get(timetable.getPlancourseid());
				timetable.setTeachingPlanCourse(planCourse);
				timetable.setCourse(planCourse.getCourse());
			}	
			if(ExStringUtils.isNotBlank(timetable.getClassroomid())){
				timetable.setClassroom(classroomService.get(timetable.getClassroomid()));
			}
			/*if(ExStringUtils.isNotBlank(timetable.getStartTimeStr())){
				timetable.setStartTime(ExDateUtils.parseDate(timetable.getStartTimeStr(), "HH:mm"));
			}
			if(ExStringUtils.isNotBlank(timetable.getEndTimeStr())){
				timetable.setEndTime(ExDateUtils.parseDate(timetable.getEndTimeStr(), "HH:mm"));
			}*/
			if(ExStringUtils.isNotBlank(timetable.getTimePeriodid())){
				timetable.setUnitTimePeriod(teachingPlanCourseTimePeriodService.get(timetable.getTimePeriodid()));
			}
			if(ExStringUtils.isNotBlank(timetable.getResourceid())){
				TeachingPlanCourseTimetable preTimetable = teachingPlanCourseTimetableService.get(timetable.getResourceid());
				logTimetable(preTimetable,timetable);//记录改变内容
				ExBeanUtils.copyProperties(preTimetable, timetable);
				teachingPlanCourseTimetableService.updateCourseTimetable(preTimetable);
			} else {				
				teachingPlanCourseTimetableService.save(timetable);
			}
			map.put("statusCode", 200);
			map.put("message", "保存上课安排成功");
			map.put("navTabId", "timetable");
		} catch (Exception e) {
			logger.error("保存上课安排失败:{}", e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存上课安排失败:"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 跳转到导入窗口
	 * @param request
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/teachingplancoursetimetable/coursestatus-forarrang-imput.html")
	public String editTeachingPlanCourseTimetable(HttpServletRequest request, ModelMap model) throws WebException {
		return "/edu3/teaching/teachingplancoursearrange/coursestatus-forarrang-imput";
	}

	/**
	 * 排课、排考 模版导出窗口
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/teaching/teachingplancoursetimetable/selectModelExp.html")
	public String selectorDialogForExcelModelExp(HttpServletRequest request, ModelMap model){
		Condition2SQLHelper.addMapFromResquestByIterator(request,model);
		return "/edu3/teaching/teachingplancoursearrange/selectModelForm";
	}
	/**
	 * 导出模版
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/teachingplancourse/courseArrangementModelExp.html")
	public void batchRegistUserModel(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws WebException {
		setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
		try{
			String excelModel	=	ExStringUtils.trim(request.getParameter("excelModel"));
			String majorid		=	ExStringUtils.trimToEmpty(request.getParameter("majorid"));
			String classicid	=	ExStringUtils.trimToEmpty(request.getParameter("classicid"));
			String gradeid	=		ExStringUtils.trimToEmpty(request.getParameter("gradeid"));
			String term		=		ExStringUtils.trimToEmpty(request.getParameter("term"));
			String teachingType	=	ExStringUtils.trimToEmpty(request.getParameter("teachingType"));
			String brSchoolid	=	ExStringUtils.trimToEmpty(request.getParameter("brSchoolid"));
			String classesid	=	ExStringUtils.trimToEmpty(request.getParameter("classesid"));
			String courseId		=	ExStringUtils.trimToEmpty(request.getParameter("courseId"));
			String status		=	ExStringUtils.trimToEmpty(request.getParameter("status"));
			String showList 	=	ExStringUtils.trim(request.getParameter("showList"));
			String exportType = ExStringUtils.trim(request.getParameter("exportType"));
			Page objPage		=	new Page();
			String id			=	ExStringUtils.trimToEmpty(request.getParameter("couId"));
			String[] ids	=	id.split(",");
			String st	=	ids[0];
			List<Map<String,Object>> li1=new ArrayList<Map<String,Object>>();
			List<Map<String, Object>> lis = new ArrayList<Map<String,Object>>();
			int startRow = 2;
			if(st.length()<1){
				listTeachingPlanCourseArrange(gradeid, majorid, classicid, brSchoolid,teachingType, term, classesid, "timetable",request, model,null,courseId,status,showList);
				li1 = (List<Map<String, Object>>)model.get("courseList");
			}else{
				listTeachingPlanCourseArrange(gradeid, majorid, classicid, brSchoolid,teachingType, term, classesid, "timetable",request, model,objPage,courseId,status,showList);
				Page courPage=(Page)model.get("coursePage");
				lis=courPage.getResult();

				for (Map<String, Object> map : lis) {
					String cid=(String)map.get("resourceid")+"-"+(String)map.get("classesid");
					for (String s : ids) {
						if(cid.equals(s)){
							li1.add(map);
						}
					}
				}
			}
			String fileName = "导入课表模版.xls";
			String modelName = "arrangingInfoVo";
			String excelFileModelName = "studentCourseTimetableImpModel.xls";
			if("examinationInfoVo".equals(excelModel)){
				modelName = "examinationInfoVo";
				excelFileModelName = "studentCourseExaminationImpModel.xls";
				fileName = "导入课程考试安排模版.xls";
			}
			if ("data".equals(exportType)) {
				startRow = 1;
				excelFileModelName = "studentCourseTimetableExpModel.xls";
				fileName = "课表.xls";
			}
			List<String> dictCodeList = new ArrayList<String>();
			dictCodeList.add("CodeCourseTermType");
			dictCodeList.add("CodeWeek");
			dictCodeList.add("CodeCourseTimePeriod");
			Map<String , Object> map = dictionaryService.getDictionByMap(dictCodeList, true, IDictionaryService.PREKEY_TYPE_BYCODE);
			File excelFile  = null;
			GUIDUtils.init();
			File disFile 	= new File(getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(true) + ".xls");
			exportExcelService.initParmasByfile(disFile, modelName, li1,map,null);
			exportExcelService.getModelToExcel().setRowHeight(500);//设置行高
			String pa = excelFileModelName;

			exportExcelService.getModelToExcel().setTemplateParam(pa,startRow,null);
			WritableFont font = new WritableFont(WritableFont.ARIAL, 10);
			font.setBoldStyle(WritableFont.BOLD);
			WritableCellFormat format = new WritableCellFormat(font);
			format.setAlignment(Alignment.CENTRE);
			format.setBackground(Colour.GRAY_50);
			format.setBorder(Border.ALL, BorderLineStyle.THIN);
			exportExcelService.getModelToExcel().setTitleCellFormat(null);
			exportExcelService.getModelToExcel().setRowHeight(300);
			excelFile 	= exportExcelService.getExcelFile();//获取导出的文件

			logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());
			downloadFile(response,fileName, excelFile.getAbsolutePath(),true);

		}catch (Exception e) {
			logger.error("导出excel文件出错："+e.fillInStackTrace());
			renderHtml(response, "<script>alert('导出excel文件出错:"+e.getMessage()+"')</script>");
		}
		
	}
	
	/**
	 * 导入课表
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/teachingplancoursetimetable/imputcoursetimetable.html")
	public void importCourseTimetable(String exportAct, HttpServletRequest request, HttpServletResponse response) throws WebException {
		User user = SpringSecurityHelper.getCurrentUser();
		if(!user.getOrgUnit().getUnitType().equals(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())){//如果为校外学习中心人员
			throw new WebException("只有教学站有权利导入！");
		}
		OrgUnit unit = user.getOrgUnit();
		//提示信息字符串
		String  rendResponseStr = "";
		List<CourseTimetableVo> falseList = new ArrayList<CourseTimetableVo>();
		File excelFile = null;
		String upLoadurl = "/edu3/teaching/teachingplancoursetimetable/imputfalseexport.html";
		
		String imurl = request.getParameter("importFile");
		//设置目标文件路径
		setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH + request.getParameter("importFile"));
		try {
			//上传文件到服务器
			List<AttachInfo> list = doUploadFile(request, response, null);
			AttachInfo attachInfo = list.get(0);
			//创建EXCEL对象 获得待导入的excel的内容
			File excel = new File(attachInfo.getSerPath() + File.separator+ attachInfo.getSerName());
			
			importExcelService.initParmas(excel, "imputCourseTimetableVo",null);
			importExcelService.getExcelToModel().setSheet(0);// 设置excel sheet 0
			//获得待导入excel内容的List
			List modelList = importExcelService.getModelList();
			if(modelList==null){
				throw new Exception("导入模版错误！");
			}
			//转换为对应类型的List
			List<CourseTimetableVo> volist = new ArrayList<CourseTimetableVo>();
			for (int i = 0; i < modelList.size(); i++) {
				CourseTimetableVo impPayment = (CourseTimetableVo) modelList.get(i);
				if(impPayment.getTeachDate().length()>25){
					throw new Exception("上课日期长度超过25个汉字");
				}
				volist.add(impPayment);
			}
			//遍历需要导入的List 将对应内容写进DB 并同时检测数据的合法性
			if (null!=volist) {
				GUIDUtils.init();
				for(CourseTimetableVo vo : volist){
					String falseMsg = "";
					//1、判断是否存在该班
					Map<String,Object> condition = new HashMap<String,Object>();
					condition.put("majorName", vo.getMajor());
					condition.put("classicName", vo.getClassic());
					condition.put("gradeName", vo.getGrade());
					condition.put("brSchoolid",unit.getResourceid());
					condition.put("addSql", " and classname = '"+ExStringUtils.trimToEmpty(vo.getClasses()) + "' ");
					List<Classes> classeslist = classesService.findClassesByCondition(condition);
					if(null==classeslist || classeslist.size()==0){
						falseMsg = "找不到班级！";
						vo.setErrorMsg(falseMsg);
						falseList.add(vo);
						continue;
					}
					Classes classes = classeslist.get(0);
					Major major     = classes.getMajor();
					Classic classic = classes.getClassic();
					Grade grade     = classes.getGrade();
					//获取教学计划
					condition.clear();
					condition.put("major", major.getResourceid());
					condition.put("classic", classic.getResourceid());
					condition.put("grade", grade.getResourceid());
//					condition.put("orgUnitId", unit.getResourceid());
					condition.put("addSql", " and (p.orgUnit.resourceid ='"+unit.getResourceid()+"' or p.orgUnit is null)");
					condition.put("schoolType", classes.getTeachingType());
					List<TeachingPlan> teachingPlanlist = teachingPlanService.findTeachingPlanByCondition(condition);
					if(teachingPlanlist.size()==0 || null == teachingPlanlist){
						falseMsg = "找不到匹配的教学计划！";
						vo.setErrorMsg(falseMsg);
						falseList.add(vo);
						continue;
					}else if( teachingPlanlist.size() >1){
						falseMsg = "班级匹配出多条教学计划！请联系管理员！";
						vo.setErrorMsg(falseMsg);
						falseList.add(vo);
						continue;
					}
					TeachingPlan teachingPlan = teachingPlanlist.get(0);
					Set<TeachingPlanCourse> tpCourseSet = teachingPlan.getTeachingPlanCourses();
					//2、判断课室是否存在
					condition.clear();
					condition.put("branchSchool",unit.getResourceid());
					condition.put("addSql", " and cr.classroomName='" +ExStringUtils.trimToEmpty(vo.getClassroom())+"' ");
					List<Classroom> classroomlist = classroomService.findClassroomByCondition(condition);
					if(null==classroomlist||classroomlist.size() == 0){
						falseMsg = "找不到教室！";
						vo.setErrorMsg(falseMsg);
						falseList.add(vo);
						continue;
					}else if(classroomlist.size()>1){
						falseMsg = "存在同名教室，无法确定！";
						vo.setErrorMsg(falseMsg);
						falseList.add(vo);
						continue;
					}
					Classroom classroom = classroomlist.get(0);
					//3、判断教师是否存在
					condition.clear();
					condition.put("unitId", unit.getResourceid());
					condition.put("cnName", vo.getTeacher());
					// TODO: 考虑到有些学校教师没有编号，改变处理逻辑以适应这种情况，以后会优化改善这部分
					/**
					 * 分为以下两种情况处理：
					 * 1、如果教师名称和教师编码都有，则根据这两个条件和unitId来判断
					 * 2、如果只有教师名称，则根据教师名称和unitId判断；由于教师名称
					 *      可能相同，所以在教师老师名称相同的情况下，则返回存在多个该
					 *      教师名称的教师的提示信息
					 */
					if(ExStringUtils.isNotEmpty(vo.getTeacherNum())){
						condition.put("teacherCode", vo.getTeacherNum());
					}
					
					List<Edumanager> teacherlist = edumanagerService.findEdumanagerByCondition(condition);
					if(null==teacherlist ||teacherlist.size() == 0){
						falseMsg = "找不到教师！";
						vo.setErrorMsg(falseMsg);
						falseList.add(vo);
						continue;
					}else if(teacherlist.size() > 1){
						falseMsg =  "存在同名教师，请填写教师编号来区分！";
						vo.setErrorMsg(falseMsg);
						falseList.add(vo);
						continue;
					}
					Edumanager teacher = teacherlist.get(0);
					//4、判断课程是否存在
					Course course = null;
					TeachingPlanCourse tpCourse = null;
					for(TeachingPlanCourse tpc : tpCourseSet){
						if(tpc.getCourse().getCourseName().equals(vo.getCourse())){
							course = tpc.getCourse();
							tpCourse = tpc;
							break;
						}
					}
					if(null== course){
						falseMsg = "教学计划不存在此课程！";
						vo.setErrorMsg(falseMsg);
						falseList.add(vo);
						continue;
					}
					//5、判断上课时间设置,如果没有则添加，有则直接引用
					TeachingPlanCourseTimePeriod teachingPlanCourseTimePeriod = null;
					String timePeriod = JstlCustomFunction.dictionaryCode2Name("CodeCourseTimePeriod", vo.getTimePeriod());
					if(ExStringUtils.isBlank(timePeriod)){
						falseMsg = "找不到节！";
						vo.setErrorMsg(falseMsg);
						falseList.add(vo);
						continue;
					}
					List<TeachingPlanCourseTimePeriod> tpcTimePlist = teachingPlanCourseTimePeriodService.findByCriteria(Restrictions.eq("isDeleted", 0),Restrictions.eq("brSchool", unit),Restrictions.eq("timePeriod", timePeriod));
					for(TeachingPlanCourseTimePeriod tpct:tpcTimePlist){
						String classTime = ExDateUtils.formatDateStr(tpct.getStartTime(), "HH:mm")+"-"+ExDateUtils.formatDateStr(tpct.getEndTime(), "HH:mm");
						if(vo.getClassTime().equals(classTime)){
							teachingPlanCourseTimePeriod = tpct;
							break;
						}
					}
					if(null == teachingPlanCourseTimePeriod){
						teachingPlanCourseTimePeriod = new TeachingPlanCourseTimePeriod();
						teachingPlanCourseTimePeriod.setBrSchool(unit);
						try{
							String[] times = vo.getClassTime().split("-");
							teachingPlanCourseTimePeriod.setStartTime(ExDateUtils.parseDate(times[0], "HH:mm"));
							teachingPlanCourseTimePeriod.setEndTime(ExDateUtils.parseDate(times[1], "HH:mm"));
						}catch(Exception e){
							falseMsg="时间错误！";
							vo.setErrorMsg(falseMsg);
							falseList.add(vo);
							continue;
						}
						teachingPlanCourseTimePeriod.setTimePeriod(timePeriod);
						teachingPlanCourseTimePeriod.setTimeName(unit.getUnitName());
						teachingPlanCourseTimePeriodService.save(teachingPlanCourseTimePeriod);
					}
					
					//6、判断是否已经存在这个排课安排，如果已经存在，则修改，否则，则新增
					String term =  JstlCustomFunction.dictionaryCode2Name("CodeCourseTermType", vo.getTerm());
					String week =JstlCustomFunction.dictionaryCode2Name("CodeWeek", vo.getWeek());
					if(ExStringUtils.isBlank(term)){
						falseMsg="找不到学期！";
						vo.setErrorMsg(falseMsg);
						falseList.add(vo);
						continue;
					}
					if(ExStringUtils.isBlank(week)){
						falseMsg="找不到上课星期！";
						vo.setErrorMsg(falseMsg);
						falseList.add(vo);
						continue;
					}
					//校验上课学期
					TeachingPlanCourseStatus courseStatus = null;
					List<TeachingPlanCourseStatus> termlist =  teachingPlanCourseStatusService.findByCriteria(Restrictions.eq("isDeleted", 0),Restrictions.eq("teachingPlanCourse", tpCourse),
							Restrictions.eq("isOpen", "Y"),Restrictions.eq("term", term),Restrictions.eq("schoolIds", unit.getResourceid()));
					if(termlist.size()==0){
						falseMsg="上课学期错误！";
						vo.setErrorMsg(falseMsg);
						falseList.add(vo);
						continue;
					}
					courseStatus = termlist.get(0);
					condition.put("week", week);
					condition.put("term",term);
					condition.put("teacherId", teacher.getResourceid());
					condition.put("course", course.getResourceid());
					condition.put("plancourseid", tpCourse.getResourceid());
					condition.put("unitTimePeriod", teachingPlanCourseTimePeriod.getResourceid());
					condition.put("classesid", classes.getResourceid());
					List<TeachingPlanCourseTimetable> timetableList = teachingPlanCourseTimetableService.findTeachingPlanCourseTimetableByCondition(condition);
					TeachingPlanCourseTimetable timetable=null;
					if(timetableList.size() == 0){
						timetable = new TeachingPlanCourseTimetable();
						timetable.setClasses(classes);
						timetable.setCourse(course);
						timetable.setTerm(term);
						timetable.setOperatorName(user.getCnName());
						timetable.setTeachingPlanCourse(tpCourse);
					}else{
						timetable = timetableList.get(0);
						if(!ExStringUtils.isContainsStr(timetable.getOperatorName(), user.getCnName())){
							String opeName = timetable.getOperatorName();
							if(ExStringUtils.isNotEmpty(opeName)){
								opeName += ",";
							}
							opeName += user.getCnName();
							timetable.setOperatorName(opeName);
						}
					}
					timetable.setTeacherName(teacher.getCnName());
					timetable.setTeacherId(teacher.getResourceid());
					timetable.setUnitTimePeriod(teachingPlanCourseTimePeriod);
					timetable.setClassroom(classroom);
					timetable.setWeek(week);
					timetable.setTeachDate(vo.getTeachDate());
					timetable.setIsStoped("N");
					timetable.setMergeMemo(vo.getMergeMemo());
					//导入排课时将排课老师添加到任课老师
					String isSetDefaultTeacher = CacheAppManager.getSysConfigurationByCode("isSetDefaultTeacher")==null?"":CacheAppManager.getSysConfigurationByCode("isSetDefaultTeacher").getParamValue();
					if(ExStringUtils.isNotEmpty(isSetDefaultTeacher) && !"0".equals(isSetDefaultTeacher)){
						CourseTeacherCl c = null;
						List<CourseTeacherCl> tclList = courseStatusClService.findByCriteria(Restrictions.eq("isDeleted", 0),Restrictions.eq("classesId.resourceid",  classes.getResourceid()),
								Restrictions.eq("courseStatusId.resourceid",courseStatus.getResourceid()));
						if(null != tclList && tclList.size() > 0){
							c = tclList.get(0);
							classes = c.getClassesId();
						}else if(courseStatus!=null){
							c = new CourseTeacherCl();
							c.setClassesId(classes); //班级
							c.setCourseid(course); //课程
							c.setCourseStatusId(courseStatus);//年级教学计划课程
						}
						if(null != classes && null != course && null != courseStatus){
							String lecturerId = c.getLecturer()==null?"":c.getLecturer();
							String lecturerName = c.getLecturerName()==null?"":c.getLecturerName();
							//设置任课老师
							if(!ExStringUtils.isContainsStr(lecturerId, teacher.getResourceid())){
								if(ExStringUtils.isNotBlank(lecturerId)){
									lecturerId += ",";
									lecturerName += ",";
								}
								lecturerId += teacher.getResourceid();
								lecturerName += teacher.getCnName();
								c.setLecturerName(lecturerName);
								c.setLecturerId(lecturerId); 
							}
							//设置登分老师
							if("2".equals(isSetDefaultTeacher) && ExStringUtils.isBlank(c.getTeachid())){
								//当登分老师为空时才设置为登分老师
								c.setTeacherName(teacher.getCnName()); 
								c.setTeachid(teacher.getResourceid()); 
							}else if ("3".equals(isSetDefaultTeacher)) {//直接替换登分老师
								c.setTeacherName(teacher.getCnName()); 
								c.setTeachid(teacher.getResourceid());
							}
							courseStatusClService.saveOrUpdate(c);
						}else {
							falseMsg="无法关联  班级-课程-教学计划！";
							vo.setErrorMsg(falseMsg);
							falseList.add(vo);
							continue;
						}
					}
					teachingPlanCourseTimetableService.saveOrUpdate(timetable);
				}
			}
			
			//导出缴费信息导入失败的信息以及原因
			if(falseList!=null&&falseList.size()>0){
				setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
				//导出
				GUIDUtils.init();
				String fileName = GUIDUtils.buildMd5GUID(false);
				File disFile = new File(getDistfilepath()+ File.separator + fileName + ".xls");
					
				//模板文件路径
				String templateFilepathString = "studentCourseTimetableImpError.xls";
				//初始化配置参数
				exportExcelService.initParmasByfile(disFile,"imputCourseTimetableErrorVo", falseList,null);
				exportExcelService.getModelToExcel().setTemplateParam(templateFilepathString, 1, null);
				exportExcelService.getModelToExcel().setRowHeight(400);
					
				excelFile = exportExcelService.getExcelFile();//获取导出的文件
				logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());
//					downloadFile(response, "导入缴费信息失败名单.xls", excelFile.getAbsolutePath(),true);
				rendResponseStr = "{statusCode:200,message:'"+"导入成功"+ (volist.size() - falseList.size()) 
				+"条 | 导入失败"+  falseList.size()
				+"条！',forwardUrl:'"+upLoadurl+"?excelFile="+fileName+"'};";
			}
			if(ExStringUtils.isBlank(rendResponseStr)){
				rendResponseStr = "{statusCode:200,message:'"+"导入成功"+ (volist.size() - falseList.size())  
				+"条 | 导入失败"+ falseList.size()
				+"条！',forwardUrl:''};";
			}
		} catch (Exception e) {
			logger.error("导入课表出错：", e);
			rendResponseStr = "{statusCode:300,message:'操作失败!<br/>"+e.getMessage()+"'};";
		}
		StringBuffer html = new StringBuffer();
		html.append("<script>");
		html.append("var response = "+rendResponseStr);
		html.append("if(window.parent.donecallback) window.parent.donecallback(response);");
		html.append("</script>");
		renderHtml(response, html.toString());
	}
	/**
	 * 导出失败信息
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/teachingplancoursetimetable/imputfalseexport.html")
	public void uploadFalseToImport(String excelFile, HttpServletRequest request, HttpServletResponse response) throws Exception {
		setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
		File disFile = new File(getDistfilepath()+ File.separator + excelFile+".xls");
		downloadFile(response, "导入信息失败记录.xls", disFile.getAbsolutePath(),true);
	}

	
	private void logTimetable(TeachingPlanCourseTimetable timetable1, TeachingPlanCourseTimetable timetable2){
		/*Map<String, String> map1 = new HashMap<String, String>();
		Map<String, String> map2 = new HashMap<String, String>();*/
		StringBuilder content = new StringBuilder();
		try {
			if(!timetable1.getTeacherId().equals(timetable2.getTeacherId())){
				/*map1.put("上课老师", timetable1.getTeacherName());
				map2.put("上课老师", timetable2.getTeacherName());*/
				content.append("上课老师: ").append(timetable1.getTeacherName()).append("==>").append(timetable2.getTeacherName()).append("<br/>");
			}
			if(!timetable1.getClassroom().getResourceid().equals(timetable2.getClassroom().getResourceid())){
				/*map1.put("上课地点", timetable1.getClassroom().getClassroomName());
				map2.put("上课地点", timetable2.getClassroom().getClassroomName());*/
				content.append("上课地点: ").append(timetable1.getClassroom().getClassroomName()).append("==>").append(timetable2.getClassroom().getClassroomName()).append("<br/>");
			}
			if(!timetable1.getTeachDate().equals(timetable2.getTeachDate())){
				/*map1.put("上课日期", timetable1.getTeachDate());
				map2.put("上课日期", timetable2.getTeachDate());*/
				content.append("上课日期: ").append(timetable1.getTeachDate()).append("==>").append(timetable2.getTeachDate()).append("<br/>");
			}
			/*if(!timetable1.getTimePeriod().equals(timetable2.getTimePeriod())){
				map1.put("上课时段", JstlCustomFunction.dictionaryCode2Value("CodeCourseTimePeriod", timetable1.getTimePeriod()));
				map2.put("上课时段", JstlCustomFunction.dictionaryCode2Value("CodeCourseTimePeriod", timetable2.getTimePeriod()));
			}
			if(!timetable1.getStartTime().equals(timetable2.getStartTime()) ||!timetable1.getEndTime().equals(timetable2.getEndTime())){
				map1.put("上课时间", ExDateUtils.formatDateStr(timetable1.getStartTime(), "HH:mm")+"-"+ExDateUtils.formatDateStr(timetable1.getEndTime(), "HH:mm"));
				map2.put("上课时间", ExDateUtils.formatDateStr(timetable2.getStartTime(), "HH:mm")+"-"+ExDateUtils.formatDateStr(timetable2.getEndTime(), "HH:mm"));
			}*/
			if(!ExStringUtils.equals(timetable1.getWeek(), timetable2.getWeek())){
				content.append("上课星期: ").append(JstlCustomFunction.dictionaryCode2Value("CodeWeek", timetable1.getWeek())).append("==>").append(JstlCustomFunction.dictionaryCode2Value("CodeWeek", timetable2.getWeek())).append("<br/>");
			}
			if(timetable1.getUnitTimePeriod()!=null && timetable1.getUnitTimePeriod()!=null
					&& !timetable1.getUnitTimePeriod().getResourceid().equals(timetable2.getUnitTimePeriod().getResourceid())){
				content.append("上课时间: ").append(timetable1.getUnitTimePeriod().getCourseTimeName()).append("==>").append(timetable2.getUnitTimePeriod().getCourseTimeName());
			}
			if(timetable1.getIsStoped() != null && !ExStringUtils.equals(timetable1.getIsStoped(), timetable2.getIsStoped())){
				if(Constants.BOOLEAN_YES.equals(timetable2.getIsStoped())){
					content.append("停课");
				} else {
					content.append("正常上课");
				}
				content.append("<br/>");
			}
		} catch (Exception e) {			
		}		
		/*if(!map1.isEmpty() && !map2.isEmpty()){
			timetable1.setBeforeContent(JsonUtils.mapToJson(map1));
			timetable1.setAfterContent(JsonUtils.mapToJson(map2));
			timetable2.setBeforeContent(JsonUtils.mapToJson(map1));
			timetable2.setAfterContent(JsonUtils.mapToJson(map2));
		}*/
		if(content.length()>0){
			timetable1.setBeforeContent(content.toString());
			timetable1.setAfterContent(content.toString());
			timetable2.setBeforeContent(content.toString());
			timetable2.setAfterContent(content.toString());
		}
	}
	/**
	 * 删除上课安排
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/teachingplancoursetimetable/remove.html")
	public void removeTeachingPlanCourseTimetable(String term, String resourceid, String classesid, String plancourseid, HttpServletRequest request, HttpServletResponse response) throws WebException {
		Map<String, Object> map = new HashMap<String, Object>();
		try {			
			if(ExStringUtils.isNotBlank(resourceid)){
				teachingPlanCourseTimetableService.batchDelete(resourceid.split("\\,"));
				map.put("statusCode", 200);
				map.put("message", "删除上课安排成功");
				map.put("callbackType", "forward");
				map.put("forwardUrl", request.getContextPath()+"/edu3/teaching/teachingplancoursetimetable/list.html?classesid="+ExStringUtils.trimToEmpty(classesid)+"&plancourseid="+ExStringUtils.trimToEmpty(plancourseid)+"&term="+term);
			}
		} catch (Exception e) {
			logger.error("删除上课安排失败:{}", e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除上课安排失败:"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 停课、取消停课
	 * @param resourceid
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/teachingplancoursetimetable/stop.html")
	public void stopTeachingPlanCourseTimetable(String resourceid, HttpServletRequest request, HttpServletResponse response) throws WebException {
		Map<String, Object> map = new HashMap<String, Object>();
		try {			
			if(ExStringUtils.isNotBlank(resourceid)){
				TeachingPlanCourseTimetable timetable = teachingPlanCourseTimetableService.get(resourceid);
				String before = null;
				String after = null;
				if(Constants.BOOLEAN_NO.equals(timetable.getIsStoped())){
					before = "停课<br/>";
					after = "停课<br/>";
				} else if(Constants.BOOLEAN_YES.equals(timetable.getIsStoped())){
					before = "正常上课<br/>";
					after = "正常上课<br/>";
				}
				timetable.setBeforeContent(before);
				timetable.setAfterContent(after);
				if(Constants.BOOLEAN_NO.equals(timetable.getIsStoped())){
					timetable.setIsStoped(Constants.BOOLEAN_YES);
				} else {
					timetable.setIsStoped(Constants.BOOLEAN_NO);
				}
				teachingPlanCourseTimetableService.updateCourseTimetable(timetable);
				map.put("statusCode", 200);
				map.put("callbackType", "forward");
				map.put("forwardUrl", request.getContextPath()+"/edu3/teaching/teachingplancoursetimetable/list.html?classesid="+timetable.getClasses().getResourceid()+"&plancourseid="+timetable.getTeachingPlanCourse().getResourceid());
			}
		} catch (Exception e) {
			logger.error("操作失败:{}", e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "操作失败:"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 我的课表
	 * @param term
	 * @param request
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/student/study/mycoursetimetable/list.html")
	public String listStudentCourseTimetable(String term, HttpServletRequest request, ModelMap model) throws WebException {
		Map<String, Object> condition = new HashMap<String, Object>();
		if(ExStringUtils.isBlank(term)){
			term = ExDateUtils.getCurrentYear()+(ExDateUtils.getCurrentMonth()<7?"_01":"_02");
		}
		condition.put("term", term);
		condition.put("isStoped", Constants.BOOLEAN_NO);
		
		List<Dictionary> dictList = CacheAppManager.getChildren("CodeWeek");
		model.addAttribute("dictList", dictList);
		
		User user = SpringSecurityHelper.getCurrentUser();
		if(SpringSecurityHelper.isUserInRole(CacheAppManager.getSysConfigurationByCode("sysuser.role.student").getParamValue())){//学生
			if(null!=user.getUserExtends()&&null!=user.getPropertys(UserExtends.USER_EXTENDCODE_DEFAULTROLLID)){
				String studentInfoId = user.getPropertys(UserExtends.USER_EXTENDCODE_DEFAULTROLLID).getExValue();
				if(ExStringUtils.isNotBlank(studentInfoId)){
					StudentInfo studentInfo = studentInfoService.get(studentInfoId);
					model.addAttribute("studentInfo", studentInfo);
					
					List<String> yearList = new ArrayList<String>();
					long year = studentInfo.getGrade().getYearInfo().getFirstYear()+("2".equals(studentInfo.getGrade().getTerm())?1:0);
					for (long i = year; i < year+8; i++) {
						yearList.add(i+"_01");
						yearList.add(i+"_02");
					}
					model.addAttribute("yearList", ExStringUtils.join(yearList,","));
					
					List<TeachingPlanCourseTimePeriod> timePeriodList = teachingPlanCourseTimePeriodService.findByHql("from "+TeachingPlanCourseTimePeriod.class.getSimpleName()+" where isDeleted=0 and brSchool.resourceid=? order by timePeriod,startTime,endTime,resourceid ", studentInfo.getBranchSchool().getResourceid());
					model.addAttribute("timePeriodList", timePeriodList);
					
					if(studentInfo.getClasses() != null && ExCollectionUtils.isNotEmpty(timePeriodList)){
						condition.put("classesid", studentInfo.getClasses().getResourceid());
						List<TeachingPlanCourseTimetable> timetableList = teachingPlanCourseTimetableService.findTeachingPlanCourseTimetableByCondition(condition);
						
						int index = 1;
						StringBuilder memo = new StringBuilder();
						if(ExCollectionUtils.isNotEmpty(timetableList)){
							Map<String, Map<String, List<TeachingPlanCourseTimetable>>> map = new LinkedHashMap<String, Map<String,List<TeachingPlanCourseTimetable>>>();
							for (TeachingPlanCourseTimetable t : timetableList) {
								if(!map.containsKey(t.getUnitTimePeriod().getResourceid())){
									Map<String, List<TeachingPlanCourseTimetable>> timeMap = new LinkedHashMap<String, List<TeachingPlanCourseTimetable>>();
									for (Dictionary dict : dictList) {
										timeMap.put(dict.getDictValue(), new ArrayList<TeachingPlanCourseTimetable>());
									}
									map.put(t.getUnitTimePeriod().getResourceid(), timeMap);
								}
								map.get(t.getUnitTimePeriod().getResourceid()).get(t.getWeek()).add(t);
								if(ExStringUtils.isNotBlank(t.getMemo())){
									memo.append(index++).append(". ").append(JstlCustomFunction.dictionaryCode2Value("CodeWeek", t.getWeek())).append(JstlCustomFunction.dictionaryCode2Value("CodeCourseTimePeriod", t.getUnitTimePeriod().getTimePeriod())).append(t.getCourse().getCourseName()).append(":").append(t.getMemo()).append("<br/>");
								}
							}
							model.addAttribute("timetableMap", map);
						}
						try {
							Map<String, Object> param = new HashMap<String, Object>();
							param.put("classesid", studentInfo.getClasses().getResourceid());	
							param.put("term", term);			
							List<Map<String, Object>> historyList = teachingPlanCourseTimetableService.findCourseTimetableHistoryByCondition(param);
							if(ExCollectionUtils.isNotEmpty(historyList)){
								for (Map<String, Object> map : historyList) {
									memo.append(index++).append(". ").append(JstlCustomFunction.dictionaryCode2Value("CodeWeek", map.get("week").toString())).append(JstlCustomFunction.dictionaryCode2Value("CodeCourseTimePeriod", map.get("timeperiod").toString())).append(map.get("coursename")).append(":").append(map.get("aftervalue").toString());
								}
							}
							model.addAttribute("memo", memo.toString());
						} catch (Exception e) {
							e.printStackTrace();
						}	
					}
				}
			}
		}
		model.addAttribute("condition", condition);
		model.addAttribute("from", request.getParameter("from"));
		return "/edu3/teaching/teachingplancoursearrange/student-coursetimetable-list";
	}
	
	/**
	 * 班级课表（班主任角色）
	 * @param term
	 * @param request
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/student/study/mycoursetimetable/master-list.html")
	public String studentCourseTimetable(String term, HttpServletRequest request, ModelMap model) throws WebException {
		Map<String, Object> condition = new HashMap<String, Object>();
		if(ExStringUtils.isBlank(term)){
			term = ExDateUtils.getCurrentYear()+(ExDateUtils.getCurrentMonth()<7?"_01":"_02");
		}
		condition.put("term", term);
		condition.put("isStoped", Constants.BOOLEAN_NO);
		
		List<Dictionary> dictList = CacheAppManager.getChildren("CodeWeek");
		model.addAttribute("dictList", dictList);
		
		String classesId = ExStringUtils.trimToEmpty(request.getParameter("classesId"));
		
		User user = SpringSecurityHelper.getCurrentUser();
		if(SpringSecurityHelper.isUserInRole(CacheAppManager.getSysConfigurationByCode("sysuser.role.master").getParamValue())){
			//班主任角色并且班级不为空
			condition.put("classesMasterId", user.getResourceid());
			
			if(ExStringUtils.isNotEmpty(classesId)){
				condition.put("classesId", classesId);
				Classes classes =  classesService.get(classesId);
				
				List<String> yearList = new ArrayList<String>();
				long year = classes.getGrade().getYearInfo().getFirstYear()+("2".equals(classes.getGrade().getTerm())?1:0);
				for (long i = year; i < year+8; i++) {
					yearList.add(i+"_01");
					yearList.add(i+"_02");
				}
				model.addAttribute("yearList", ExStringUtils.join(yearList,","));
				
				List<TeachingPlanCourseTimePeriod> timePeriodList = teachingPlanCourseTimePeriodService.findByHql("from "+TeachingPlanCourseTimePeriod.class.getSimpleName()+" where isDeleted=0 and brSchool.resourceid=? order by timePeriod,startTime,endTime,resourceid ", classes.getBrSchool().getResourceid());
				model.addAttribute("timePeriodList", timePeriodList);
				
				if(classes != null && ExCollectionUtils.isNotEmpty(timePeriodList)){
					condition.put("classesid", classes.getResourceid());
					List<TeachingPlanCourseTimetable> timetableList = teachingPlanCourseTimetableService.findTeachingPlanCourseTimetableByCondition(condition);
					
					int index = 1;
					StringBuilder memo = new StringBuilder();
					if(ExCollectionUtils.isNotEmpty(timetableList)){
						Map<String, Map<String, List<TeachingPlanCourseTimetable>>> map = new LinkedHashMap<String, Map<String,List<TeachingPlanCourseTimetable>>>();
						for (TeachingPlanCourseTimetable t : timetableList) {
							if(!map.containsKey(t.getUnitTimePeriod().getResourceid())){
								Map<String, List<TeachingPlanCourseTimetable>> timeMap = new LinkedHashMap<String, List<TeachingPlanCourseTimetable>>();
								for (Dictionary dict : dictList) {
									timeMap.put(dict.getDictValue(), new ArrayList<TeachingPlanCourseTimetable>());
								}
								map.put(t.getUnitTimePeriod().getResourceid(), timeMap);
							}
							map.get(t.getUnitTimePeriod().getResourceid()).get(t.getWeek()).add(t);
							if(ExStringUtils.isNotBlank(t.getMemo())){
								memo.append(index++).append(". ").append(JstlCustomFunction.dictionaryCode2Value("CodeWeek", t.getWeek())).append(JstlCustomFunction.dictionaryCode2Value("CodeCourseTimePeriod", t.getUnitTimePeriod().getTimePeriod())).append(t.getCourse().getCourseName()).append(":").append(t.getMemo()).append("<br/>");
							}
						}
						model.addAttribute("timetableMap", map);
					}
					try {
						Map<String, Object> param = new HashMap<String, Object>();
						param.put("classesid", classes.getResourceid());	
						param.put("term", term);			
						List<Map<String, Object>> historyList = teachingPlanCourseTimetableService.findCourseTimetableHistoryByCondition(param);
						if(ExCollectionUtils.isNotEmpty(historyList)){
							for (Map<String, Object> map : historyList) {
								memo.append(index++).append(". ").append(JstlCustomFunction.dictionaryCode2Value("CodeWeek", map.get("week").toString())).append(JstlCustomFunction.dictionaryCode2Value("CodeCourseTimePeriod", map.get("timeperiod").toString())).append(map.get("coursename")).append(":").append(map.get("aftervalue").toString());
							}
						}
						model.addAttribute("memo", memo.toString());
					} catch (Exception e) {
						e.printStackTrace();
					}	
				}
			}
		}
		model.addAttribute("condition", condition);
		model.addAttribute("from", request.getParameter("from"));
		return "/edu3/teaching/teachingplancoursearrange/stuCoursetimetableByMaster-list";
	}
	
	/**
	 * 任课老师课表
	 * @param request
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/student/study/mycoursetimetable/teacher-list.html")
	public String teacherCourseTimetable(HttpServletRequest request, ModelMap model, Page objPage) throws WebException {
		Map<String, Object> condition = new HashMap<String, Object>();
		String term = request.getParameter("term");
		if(ExStringUtils.isBlank(term)){
			term = ExDateUtils.getCurrentYear()+(ExDateUtils.getCurrentMonth()<7?"_01":"_02");
		}
		condition.put("term", term);
		List<Dictionary> dictList = CacheAppManager.getChildren("CodeWeek");
		model.addAttribute("dictList", dictList);
		User user = SpringSecurityHelper.getCurrentUser();
		condition.put("teacherid", user.getResourceid());
		condition.put("optype", "timetable");
		objPage.setPageNum(1);
		objPage.setPageSize(100000);
		Page coursePage = teachingPlanCourseStatusService.findTeachingPlanCourseStatusMapByCondition(condition, objPage);
		List<Map> results = coursePage.getResult();
		for (int i = 0; i < results.size(); i++) {
			Map result = results.get(i);
			result.put("index", i + 1);
			Map<String, Object> condition2 = new HashMap<String, Object>();
			condition2.put("classesid", result.get("classesid")!=null?result.get("classesid").toString():"");
			condition2.put("plancourseid", result.get("plancourseid")!=null?result.get("plancourseid").toString():"");
			condition2.put("term",ExStringUtils.trimToEmpty(result.get("courseterm")!=null?result.get("courseterm").toString():""));
			condition2.put("teacherId", user.getResourceid());
			List<TeachingPlanCourseTimetable> timetableList = teachingPlanCourseTimetableService.findTeachingPlanCourseTimetableByCondition(condition2);
			Map<String, List<TeachingPlanCourseTimetable>> timeMap = new LinkedHashMap<String, List<TeachingPlanCourseTimetable>>();
			for (Dictionary dict : dictList) {
				timeMap.put(dict.getDictValue(), new ArrayList<TeachingPlanCourseTimetable>());
			}
			result.put("timetable", timeMap);
			if(ExCollectionUtils.isNotEmpty(timetableList)){
				for (TeachingPlanCourseTimetable t : timetableList) {
					((List<TeachingPlanCourseTimetable>)((Map)result.get("timetable")).get(t.getWeek())).add(t);
				}
			}
		}
		model.addAttribute("coursePage", coursePage);
		model.addAttribute("condition", condition);
		return "/edu3/teaching/teachingplancoursearrange/stuCoursetimetableByTeacher-list";
	}
	
	/**
	 * 导出学生课表
	 * @param term
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/student/study/mycoursetimetable/export.html")
	public void exportStudentTimetable(String term, HttpServletRequest request, HttpServletResponse response) throws WebException {
		setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");//文件输出服务器路径
		try{
			User user = SpringSecurityHelper.getCurrentUser();
			if(SpringSecurityHelper.isUserInRole(CacheAppManager.getSysConfigurationByCode("sysuser.role.student").getParamValue())){//学生
				if(null!=user.getUserExtends()&&null!=user.getPropertys(UserExtends.USER_EXTENDCODE_DEFAULTROLLID)){
					String studentInfoId = user.getPropertys(UserExtends.USER_EXTENDCODE_DEFAULTROLLID).getExValue();
					if(ExStringUtils.isNotBlank(studentInfoId)){
						StudentInfo studentInfo = studentInfoService.get(studentInfoId);
						if(studentInfo.getClasses()!=null){
							exportTimetable(term, response, studentInfo.getClasses());
						}
					}
				}
			}						
		}catch(Exception e){
			logger.error("导出excel文件出错："+e.fillInStackTrace());
			renderHtml(response, "<script type=\"text/javascript\">parent.alertMsg.warn(\"下载课表失败:"+e.getMessage()+"\")</script>");
		}
	}
	
	private List<Map<String, Object>> getStudentTimetableList(String term, Classes classes) {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		if(classes != null){
			//判断学习中心是否已经设定了一周上课的时间段
			List<TeachingPlanCourseTimePeriod> timePeriodList = teachingPlanCourseTimePeriodService.findByHql("from "+TeachingPlanCourseTimePeriod.class.getSimpleName()+" where isDeleted=0 and brSchool.resourceid=? order by timePeriod,startTime,endTime,resourceid ", classes.getBrSchool().getResourceid());
			if(ExCollectionUtils.isNotEmpty(timePeriodList)){
				Map<String, Object> condition = new HashMap<String, Object>();				
				if(ExStringUtils.isNotBlank(term)) {
					condition.put("term", term);
				}
				condition.put("isStoped", Constants.BOOLEAN_NO);
				condition.put("classesid", classes.getResourceid());
				//获取班级的管理信息的教学计划课程开课信息表
				List<TeachingPlanCourseTimetable> timetableList = teachingPlanCourseTimetableService.findTeachingPlanCourseTimetableByCondition(condition);
				
				if(ExCollectionUtils.isNotEmpty(timetableList)){
					List<Dictionary> dictList = CacheAppManager.getChildren("CodeWeek");//获取一周星期字典
					Map<String, Map<String, List<TeachingPlanCourseTimetable>>> map = new LinkedHashMap<String, Map<String,List<TeachingPlanCourseTimetable>>>();
					StringBuilder memo = new StringBuilder();
					int index = 1;
					for (TeachingPlanCourseTimetable t : timetableList) {
						if(!map.containsKey(t.getUnitTimePeriod().getResourceid())){
							Map<String, List<TeachingPlanCourseTimetable>> timeMap = new LinkedHashMap<String, List<TeachingPlanCourseTimetable>>();
							for (Dictionary dict : dictList) {
								timeMap.put(dict.getDictValue(), new ArrayList<TeachingPlanCourseTimetable>());
							}
							map.put(t.getUnitTimePeriod().getResourceid(), timeMap);
						}
						map.get(t.getUnitTimePeriod().getResourceid()).get(t.getWeek()).add(t);
						if(ExStringUtils.isNotBlank(t.getMemo())){
							memo.append(index++).append(". ").append(JstlCustomFunction.dictionaryCode2Value("CodeWeek", t.getWeek())).append(JstlCustomFunction.dictionaryCode2Value("CodeCourseTimePeriod", t.getUnitTimePeriod().getTimePeriod())).append(t.getCourse().getCourseName()).append(":").append(t.getMemo()).append(" \n");
						}
					}
					for (TeachingPlanCourseTimePeriod p : timePeriodList) {
						Map<String, Object> rowMap = new HashMap<String, Object>();
						rowMap.put("timePeriod", JstlCustomFunction.dictionaryCode2Value("CodeCourseTimePeriod", p.getTimePeriod()));
						String courseTimeName = p.getTimeName();
						try {
							courseTimeName += ExDateUtils.formatDateStr(p.getStartTime(), "HH:mm")+"-"+ExDateUtils.formatDateStr(p.getEndTime(), "HH:mm");
						} catch (Exception e) {
							e.printStackTrace();
						}
						rowMap.put("courseTimeName", courseTimeName);
						
						for (Dictionary dict : dictList) {
							String content = "";
							if(map.containsKey(p.getResourceid())){
								List<TeachingPlanCourseTimetable> tList = map.get(p.getResourceid()).get(dict.getDictValue());
								for (TeachingPlanCourseTimetable t : tList) {
									content += t.getCourse().getCourseName()+"\n";
									content += "("+t.getClassroom().getClassroomName()+")\n";
									content += "("+t.getTeacherName()+")\n";
									content += "("+t.getTeachDate()+")\n";
								}								
							}
							rowMap.put("week"+dict.getDictValue(), content);
						}
						rowMap.put("memo", memo.toString());
						rowMap.put("index", index);
						list.add(rowMap);
					}
					
				}
			}
		}
		return list;
	}
	/**
	 * 导出班级课表
	 * @param classesid
	 * @param term
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/teachingplancoursetimetable/export.html")
	public void exportCourseTimetable(String classesid, String term, HttpServletRequest request, HttpServletResponse response) throws WebException {
		setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");//文件输出服务器路径
		try{
			if(ExStringUtils.isNotBlank(classesid) && ExStringUtils.isNotBlank(term)){
				Classes classes = classesService.get(classesid);
				exportTimetable(term, response, classes);
			} else {
				renderHtml(response, "<script type=\"text/javascript\">parent.alertMsg.warn(\"请选择下载课表的班级和学期\")</script>");
			}
		}catch(Exception e){
			logger.error("导出excel文件出错："+e.fillInStackTrace());
			renderHtml(response, "<script type=\"text/javascript\">parent.alertMsg.warn(\"下载课表失败:"+e.getMessage()+"\")</script>");
		}
	}
	
	/**
	 * 导出课表
	 */
	@RequestMapping("/edu3/teaching/teachingplancoursetimetable/exporttimetable.html")
	public void exportTimetable(String gradeid, String majorid,
			String classicid, String brSchoolid, String teachingType,
			String term, String classesid, String courseId,
			String status, String teachername, HttpServletRequest request,
			HttpServletResponse response) throws WebException {
		try{
			Map<String, Object> condition = new HashMap<String, Object>();
			User user = SpringSecurityHelper.getCurrentUser();
			if(user.getOrgUnit().getUnitType().equals(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())){//如果为校外学习中心人员
				brSchoolid = ExStringUtils.trim(user.getOrgUnit().getResourceid());
				condition.put("isBrschool", Constants.BOOLEAN_YES);
				condition.put("schoolname", ExStringUtils.trim(user.getOrgUnit().getUnitName()));
			}
			if(ExStringUtils.isNotBlank(status)) {
				condition.put("status", status);
			}
			if(ExStringUtils.isNotBlank(gradeid)) {
				condition.put("gradeid", gradeid);
			}
			if(ExStringUtils.isNotBlank(courseId)) {
				condition.put("courseId", courseId);
			}
			if(ExStringUtils.isNotBlank(majorid)) {
				condition.put("majorid", majorid);
			}
			if(ExStringUtils.isNotBlank(classicid)) {
				condition.put("classicid", classicid);
			}
			if(ExStringUtils.isNotBlank(teachingType)) {
				condition.put("teachingType", teachingType);
			}
			if(ExStringUtils.isNotBlank(brSchoolid)) {
				condition.put("brSchoolid", brSchoolid);
			}
			if(ExStringUtils.isNotBlank(term)) {
				condition.put("term", term);//开课学期
			}
			if(ExStringUtils.isNotBlank(classesid)) {
				condition.put("classesid", classesid);
			}
			if(ExStringUtils.isNotBlank(teachername)) {
				condition.put("teachername", teachername);
			}
			condition.put("optype", "timetable");
			condition.put("status", "Y");
			Page objPage = new Page();
			objPage.setPageSize(1000000);
			List<Map<String, Object>> results = teachingPlanCourseStatusService.findTeachingPlanCourseStatusMapByCondition(condition, objPage).getResult();
			setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");//文件输出服务器路径
			if(ExStringUtils.isNotBlank(brSchoolid) && ExStringUtils.isNotBlank(term)){
				exportTimetable(results, condition, response);
			} else {
				renderHtml(response, "<script type=\"text/javascript\">parent.alertMsg.warn(\"请选择下载课表的教学站和学期\")</script>");
			}
		}catch(Exception e){
			logger.error("导出excel文件出错："+e.fillInStackTrace());
			renderHtml(response, "<script type=\"text/javascript\">parent.alertMsg.warn(\"下载课表失败:"+e.getMessage()+"\")</script>");
		}
	}
	
	/**
	 * 导出课程安排表
	 */
	@RequestMapping("/edu3/teaching/teachingplancoursetimetable/exportKCAP.html")
	public String exportCourseCalendar(String term, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws WebException {
		try{
			Map<String, Object> condition = Condition2SQLHelper.getConditionFromResquestByIterator(request);
			User user = SpringSecurityHelper.getCurrentUser();
			Condition2SQLHelper.addMapFromResquestByIterator(request, model);
			if(user.getOrgUnit().getUnitType().equals(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())){//如果为校外学习中心人员
				model.addAttribute("isBrschool", Constants.BOOLEAN_YES);
				model.addAttribute("schoolName", user.getOrgUnit().getUnitName());
			}
			String flag = request.getParameter("flag");
			String schoolConnectName = CacheAppManager.getSysConfigurationByCode("graduateData.schoolName").getParamValue()
					+ CacheAppManager.getSysConfigurationByCode("graduateData.schoolConnectName").getParamValue();
			String termName = JstlCustomFunction.dictionaryCode2Value("CodeCourseTermType", term);
			StringBuilder builder = new StringBuilder();
			builder.append("select u.unitname,g.gradename,ci.classicname,cl.teachingtype,m.majorname,cl.classesname,c.coursename");
			builder.append(" ,pc.stydyhour,pc.facestudyhour,pc.examclasstype,pc.coursetype,pc.teachtype,ctl.lecturername,ctl.teachername recordername");
			builder.append(" ,wm_concat(distinct em.titleoftechnical) titleoftechnical,wm_concat(distinct em.mobile) mobile,tt.teachername,tt.week,tp.timename,tt.teachdate,cr.classroomname,tt.mergeMemo,tt.operatorName,tt.memo");
			builder.append(" ,to_char(tp.STARTTIME,'hh24:mi')||'-'||to_char(tp.ENDTIME,'hh24:mi') time");
			builder.append(" ,(select count(*) from edu_roll_studentinfo si where si.classesid=cl.resourceid and si.studentstatus not in('13','14','15') and si.isdeleted=0) stunum");
			builder.append(" ,u.resourceid unitid,cl.resourceid classesid,pc.resourceid plancourseid from edu_roll_classes cl");
			builder.append(" join hnjk_sys_unit u on u.resourceid=cl.orgunitid and u.isdeleted=0");
			builder.append(" join edu_base_grade g on g.resourceid=cl.gradeid and g.isdeleted=0");
			builder.append(" join edu_base_classic ci on ci.resourceid=cl.classicid and ci.isdeleted=0");
			builder.append(" join edu_base_major m on m.resourceid=cl.majorid and m.isdeleted=0"); 
			builder.append(" join edu_teach_plan p on p.majorid=m.resourceid and p.classicid=ci.resourceid and p.schooltype=cl.teachingtype and p.isdeleted=0");
			builder.append(" join edu_teach_plancourse pc on pc.planid=p.resourceid and pc.isdeleted=0 ");
			builder.append(" join edu_base_course c on c.resourceid=pc.courseid and c.isdeleted=0");
			builder.append(" join edu_teach_guiplan gp on gp.gradeid=g.resourceid and gp.planid=pc.planid and gp.isdeleted=0");
			builder.append(" join edu_teach_coursestatus cs on cs.plancourseid=pc.resourceid and cs.schoolids=u.resourceid and cs.guiplanid=gp.resourceid and cs.isdeleted=0");
			builder.append(" left join edu_teach_courseteachercl ctl on ctl.coursestatusid=cs.resourceid and ctl.courseid=c.resourceid and ctl.classesid=cl.resourceid and ctl.isdeleted=0");
			builder.append(" left join edu_teach_timetable tt on tt.classesid=cl.resourceid and tt.plancourseid=pc.resourceid and tt.isdeleted=0");
			builder.append(" left join edu_teach_timeperiod tp on tp.resourceid=tt.timeperiodid and tp.isdeleted=0");
			builder.append(" left join edu_base_classroom cr on cr.resourceid=tt.classroomid and cr.isdeleted=0");
			builder.append(" left join edu_base_edumanager em on em.isdeleted=0 and instr(ctl.lecturerid,em.sysuserid,1,1)>0 ");
			builder.append(" where cl.isdeleted=0");
			if(condition.containsKey("brSchoolid")){
				builder.append(" and u.resourceid=:brSchoolid");
			}
			if(condition.containsKey("gradeid")){
				builder.append(" and g.resourceid=:gradeid");
			}
			if(condition.containsKey("classicid")){
				builder.append(" and ci.resourceid=:classicid");
			}
			if(condition.containsKey("teachingType")){
				builder.append(" and cl.teachingtype=:teachingType");
			}
			if(condition.containsKey("majorid")){
				builder.append(" and m.resourceid=:majorid");
			}
			if(condition.containsKey("classesid")){
				builder.append(" and cl.resourceid=:classesid");
			}
			if(condition.containsKey("courseId")){
				builder.append(" and c.resourceid=:courseId");
			}
			if(condition.containsKey("term")){
				builder.append(" and cs.term=:term");
			}
			if(condition.containsKey("isOpen")){
				builder.append(" and cs.isOpen=:isOpen ");
			}
			if(condition.containsKey("status")){
				builder.append(" and (select decode(count(*),0,0,1) from edu_teach_timetable a where a.isdeleted=0 and a.classesid=cl.resourceid and a.plancourseid=pc.resourceid and a.term = cs.term)");
				if ("Y".equals(condition.get("status").toString())) {
					builder.append("=1 ");
				} else {
					builder.append("=0 ");
				}
			}
			builder.append(" group by u.unitname,g.gradename,ci.classicname,cl.teachingtype,m.majorname,cl.classesname,c.coursename,");
			builder.append(" pc.stydyhour,pc.facestudyhour,pc.examclasstype,pc.coursetype,pc.teachtype,");
			builder.append(" ctl.lecturername,ctl.teachername,tt.teachername,");
			builder.append(" tt.week,tp.timename,tp.STARTTIME,tp.ENDTIME,tt.teachdate,cr.classroomname,tt.mergeMemo,tt.operatorName,tt.memo,");
			builder.append(" u.resourceid,u.unitcode,c.coursecode,cl.resourceid,pc.resourceid,cs.term");
			builder.append(" order by u.unitcode,g.gradename desc,ci.classicname,cl.teachingtype,m.majorname,cl.classesname,cs.term,c.coursecode,decode(tt.week,'7','0',tt.week)");
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String,Object>>();
			List<Map<String, Object>> results = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(builder.toString(), condition);
			results.add(new HashMap<String, Object>());
			String unitid = "";
			String classesid = "";
			String plancourseid = "";
			String week = "";
			StringBuffer message = new StringBuffer();
			List<String> operatorName = new ArrayList<String>();
			List<String> mergeMemo = new ArrayList<String>();
			int rowspanNum = 1;
			int orderNum = 1;
			Map<String, Object> map = new HashMap<String, Object>();
			List<String> timePeriodList = ExBeanUtils.getEmptyList(7);
			for (int i = 0; i < results.size(); i++) {
				map = results.get(i);
				String _unitid = map.get("unitid")==null?"":map.get("unitid").toString();
				String _classesid = map.get("classesid")==null?"":map.get("classesid").toString();
				String _plancourseid = map.get("plancourseid")==null?"":map.get("plancourseid").toString();
				String _week = map.get("week")==null?"":map.get("week").toString();
				String _operatorName = map.get("operatorName")==null?"":map.get("operatorName").toString();
				String _mergeMemo = map.get("mergeMemo")==null?"":map.get("mergeMemo").toString();
				boolean isSameWeek = false;
				boolean isSameCourse = false;
				if(i==0) {
					isSameCourse = true;
				}
				if(!classesid.equals(_classesid)){
					//orderNum++;
				}else if (plancourseid.equals(_plancourseid)) {
					isSameCourse = true;
					if(week.equals(_week)) {
						isSameWeek = true;
					}
				}
				if(!isSameWeek && i!=0 && !"".equals(week)) {//上课星期不同
					timePeriodList.set(Integer.parseInt(week)%7,ExStringUtils.removeEnd(message.toString(),"<br>"));
					message.setLength(0);
				}
				if(!isSameCourse){//教学计划课程不同，添加新纪录
					map = results.get(i-1);
					map.put("timePeriod", timePeriodList);
					map.put("orderNum", orderNum);
					map.put("mergeMemo", ExStringUtils.addSymbol(mergeMemo, "", "<br>"));
					map.put("operatorName", ExStringUtils.addSymbol(operatorName, "", "<br>"));
					mapList.add(map);
					//合并单元格
					if(unitid.equals(_unitid)){
						rowspanNum++;
					}else {
						mapList.get(mapList.size()-rowspanNum).put("rowspan", rowspanNum);
						rowspanNum = 1;
					}
					if(!classesid.equals(_classesid)) {
						orderNum++;
					}
					timePeriodList = ExBeanUtils.getEmptyList(7);
					message.setLength(0);
					mergeMemo = new ArrayList<String>();
					operatorName = new ArrayList<String>();
				}
				if(!mergeMemo.contains(_mergeMemo) && ExStringUtils.isNotBlank(_mergeMemo)) {
					mergeMemo.add(_mergeMemo);
				}
				if(!operatorName.contains(_operatorName) && ExStringUtils.isNotBlank(_operatorName)) {
					operatorName.add(_operatorName);
				}
				String timename = ExStringUtils.getMapValuesByKeys(results.get(i),"<br>","timename","teachdate","classroomname");
				if (message.indexOf(timename) < 0) {
					message.append(timename).append("<br>");
				}
				unitid = _unitid;
				classesid = _classesid;
				plancourseid = _plancourseid;
				week = _week;
			}
			//String openDate = "";
			model.addAttribute("schoolConnectName", schoolConnectName);
			model.addAttribute("termName", termName);
			model.addAttribute("mapList", mapList);
			if("export".equals(flag)){
				String fileName = schoolConnectName+termName.replace("春季", "第一").replace("秋季", "第二")+"课程安排表";
				try {
					fileName = new String(fileName.getBytes("GBK"), "iso8859-1");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				model.addAttribute("flag", flag);
				request.getSession().setAttribute("mapList", mapList);
				response.setCharacterEncoding("GB2312");
				response.setContentType("application/vnd.ms-excel");
			  	response.setHeader("Content-Disposition", "attachment; filename="+fileName+".xls"); 
			}
		}catch(Exception e){
			logger.error("导出excel文件出错："+e.fillInStackTrace());
			renderHtml(response, "<script type=\"text/javascript\">parent.alertMsg.warn(\"下载课表失败:"+e.getMessage()+"\")</script>");
		}
		return "/edu3/teaching/teachingplancoursearrange/courseCalendar-list";
	}
	
	/**
	 * 设置登分老师
	 * @param resourceids
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/teachingplancoursetimetable/setTeach.html")
	public String exeEdit(String resourceids, String guiplanids, String plancourseids, String unitids, String classesids, String courseids, HttpServletRequest request, HttpServletResponse response, ModelMap model, Page objPage) throws WebException {
//		if(ExStringUtils.isNotBlank(resourceids) && ExStringUtils.isNotBlank(guiplanids) && ExStringUtils.isNotBlank(plancourseids) && ExStringUtils.isNotBlank(unitids)){ //-----编辑
		if(ExStringUtils.isNotBlank(resourceids)){ //-----编辑
//			String[] resourceidary = resourceids.split(",");
//			String[] guiplanidary = guiplanids.split(",");
//			String[] plancourseidary = plancourseids.split(",");
//			String[] unitidary = unitids.split(",");
//			String[] classesidary = classesids.split(",");
//			String[] courseidary = courseids.split(",");
//			List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
//			for (int i=0; i<resourceidary.length; i++) {
//				String resourceid = resourceidary[i];
//				String guiplanid = guiplanidary[i];
//				String plancourseid = plancourseidary[i];
//				String unitid = unitidary[i];
//				String classesid = classesidary[i];
//				Map<String,Object> condition = new HashMap<String,Object>();
//				condition.put("optype", "timetable");
//				condition.put("isOpen", "Y");
//				condition.put("setTeach", "Y"); 
//				condition.put("res", resourceid); //id
//				condition.put("plancourseid", plancourseid); //教学计划id
//				condition.put("guiplanid", guiplanid); //教学计划课程id
//				condition.put("brSchoolid", unitid); //教学中心
//				condition.put("classesid", classesid); //班级id
//				Page coursePage = teachingPlanCourseStatusService.findTeachingPlanCourseStatusMapByCondition(condition, objPage);
//				List<Map<String, Object>> result = coursePage.getResult();
//				if (result != null && result.size() > 0) {
//					results.addAll(result);
//				}
//			}
//			StringBuffer courseids = new StringBuffer();
//			for (Map<String, Object> map : results) {
//				if (courseids.length() == 0) {
//					courseids.append(map.get("cid"));
//				} else {
//					courseids.append(",").append(map.get("cid"));
//				}
//			}
//			model.addAttribute("setTeachList",null != coursePage.getResult() ? coursePage.getResult().get(0) : null);
			String gradeid = ExStringUtils.trim(request.getParameter("gradeid"));
			String brSchoolid = ExStringUtils.trim(request.getParameter("brSchoolid"));
			String classesid = ExStringUtils.trim(request.getParameter("classesid"));
			String majorid = ExStringUtils.trim(request.getParameter("majorid"));
//			String teachername = ExStringUtils.trim(ExStringUtils.getEncodeURIComponentByOne(request.getParameter("teachername")));
			String teachername = ExStringUtils.trim(request.getParameter("teachername"));
			String teachingType = ExStringUtils.trim(request.getParameter("teachingType"));
			String classicid = ExStringUtils.trim(request.getParameter("classicid"));
			String term = ExStringUtils.trim(request.getParameter("term"));
			String courseId = ExStringUtils.trim(request.getParameter("courseId"));
			String teacherId = ExStringUtils.trim(request.getParameter("teacherId"));
			teacherId = teacherId==null?"":teacherId;
			String setteacherName = ExStringUtils.trim(request.getParameter("setteacherName"));
			setteacherName = setteacherName==null?"":setteacherName;
			Map<String, Object> condition = new HashMap<String, Object>();
			User user = SpringSecurityHelper.getCurrentUser();
			if(user.getOrgUnit().getUnitType().equals(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())){//如果为校外学习中心人员
				brSchoolid = ExStringUtils.trim(user.getOrgUnit().getResourceid());
				condition.put("isBrschool", Constants.BOOLEAN_YES);
				model.addAttribute("isBrschool", true); 
				model.addAttribute("school",brSchoolid);
				model.addAttribute("schoolname",ExStringUtils.trim(user.getOrgUnit().getUnitName()));
				condition.put("schoolname", ExStringUtils.trim(user.getOrgUnit().getUnitName()));
			}
			String orgterm = request.getParameter("orgterm");
			if(ExStringUtils.isNotBlank(gradeid)) {
				condition.put("gradeid", gradeid);
			}
			if(ExStringUtils.isNotBlank(courseId)) {
				condition.put("courseId", courseId);
			}
			if(ExStringUtils.isNotBlank(majorid)) {
				condition.put("majorid", majorid);
			}
			if(ExStringUtils.isNotBlank(classicid)) {
				condition.put("classicid", classicid);
			}
			if(ExStringUtils.isNotBlank(teachingType)) {
				condition.put("teachingType", teachingType);
			}
			if(ExStringUtils.isNotBlank(brSchoolid)) {
				condition.put("brSchoolid", brSchoolid);
			}
			if(ExStringUtils.isNotBlank(term)) {
				condition.put("term", term);//开课学期
			}
			if(ExStringUtils.isNotBlank(orgterm)) {
				condition.put("orgterm", orgterm);//原计划学期
			}
			if(ExStringUtils.isNotBlank(classesid)) {
				condition.put("classesid", classesid);
			}
			if(ExStringUtils.isNotBlank(teachername)) {
				condition.put("teachername", teachername);
			}
			String isOpenStatus = request.getParameter("isOpenStatus");
			String isOpenCourse = request.getParameter("isOpenCourse");
			if(ExStringUtils.isNotBlank(isOpenStatus)) {
				condition.put("isOpenStatus", isOpenStatus);	 //开课状态
			}
			if(ExStringUtils.isNotBlank(isOpenCourse)) {
				condition.put("isOpenCourse", isOpenCourse);	 //是否开课
			}
			String optype = "timetable";
			if("timetable".equals(optype)) {
				condition.put("isOpen", Constants.BOOLEAN_YES);
			}
			condition.put("optype", optype);
			String resourceids2 = "'" + resourceids.replaceAll(",", "','") + "'";
			condition.put("resourceids", resourceids2);
			String classesids2 = "'" + classesids.replaceAll(",", "','") + "'";
			condition.put("classesids", classesids2);
			Page coursePage = teachingPlanCourseStatusService.findTeachingPlanCourseStatusMapByCondition(condition, objPage);
			List<Map<String, Object>> results = coursePage.getResult();
			StringBuffer guiplanidbuf = new StringBuffer();
			StringBuffer plancourseidbuf = new StringBuffer();
			StringBuffer unitidbuf = new StringBuffer();
			StringBuffer classesidbuf = new StringBuffer();
			StringBuffer courseidbuf = new StringBuffer();
			for (Map<String, Object> result : results) {
				if (guiplanidbuf.length() == 0) {
					guiplanidbuf.append(result.get("guiplanid"));
					plancourseidbuf.append(result.get("plancourseid"));
					unitidbuf.append(result.get("brschoolid"));
					classesidbuf.append(result.get("classesid"));
					courseidbuf.append(result.get("cid"));
				} else {
					guiplanidbuf.append(",").append(result.get("guiplanid"));
					plancourseidbuf.append(",").append(result.get("plancourseid"));
					unitidbuf.append(",").append(result.get("brschoolid"));
					classesidbuf.append(",").append(result.get("classesid"));
					courseidbuf.append(",").append(result.get("cid"));
				}
			}
			model.addAttribute("resourceids", resourceids);
			model.addAttribute("guiplanids", guiplanidbuf.toString());
			model.addAttribute("plancourseids", plancourseidbuf.toString());
			model.addAttribute("unitids", unitidbuf.toString());
			model.addAttribute("classesids", classesidbuf.toString());
			model.addAttribute("courseids", courseidbuf.toString());
			model.addAttribute("teacherId", teacherId);
			model.addAttribute("setteacherName", setteacherName);
		}
		return "/edu3/teaching/teachingplancoursearrange/set-teach-form";
	}
	
	/**
	 * 保存排课设置的登分老师
	 * @param request
	 * @param response
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/teachingplancoursetimetable/setTeachSave.html")
	public void setTeachSave(String opt, String resids, String setteacherName, String teacherId, HttpServletRequest request, HttpServletResponse response) throws WebException {
		Map<String,Object> map = new HashMap<String,Object>();
		int statusCode = 0;
		String message = "";
		if(ExStringUtils.isNotBlank(resids)){ //-----设置登分教师
			try {
				//String guiplanids 		= ExStringUtils.trim(request.getParameter("guiplanids")); //年级教学计划
				//String plancourseids 	= ExStringUtils.trim(request.getParameter("plancourseids")); //教学计划课程
				String unitids 			= ExStringUtils.trim(request.getParameter("unitids")); //教学点
				String classesids		= ExStringUtils.trim(request.getParameter("classesids")); //班级id
				String courseids			= ExStringUtils.trim(request.getParameter("courseids")); //课程id
				String teachtype        =ExStringUtils.trim(request.getParameter("teachtype"));
				
				//String[] guiplanidary = guiplanids.split(",");
				//String[] plancourseidary = plancourseids.split(",");
				//String[] unitidary = unitids.split(",");
				String[] classesidary = classesids.split(",");
				String[] courseidary = courseids.split(",");
				String[] residary = resids.split(",");
				
				List<CourseTeacherCl> ctList = new ArrayList<CourseTeacherCl>(residary.length);
				for (int i = 0; i < residary.length; i++) {
					/*String guiplanid = guiplanidary[i];
					String plancourseid = plancourseidary[i];
					String unitid = unitidary[i];*/
					String classesid = classesidary[i];
					String courseid = courseidary[i];
					String resid = residary[i];
					Classes classes        			=  	null; //班级对象
					Course cse						= 	null; //课程对象
					TeachingPlanCourseStatus sts	    =   null;//年级教学计划课程对象
					
					//if(null != classes && null != cse && null != sts){
						CourseTeacherCl c = null;//课程-班级-教师  关联对象
						List<CourseTeacherCl> list = courseStatusClService.findByCriteria(Restrictions.eq("isDeleted", 0),Restrictions.eq("classesId.resourceid", classesid),
								Restrictions.eq("courseStatusId.resourceid",resid),Restrictions.eq("courseid.resourceid",courseid));
						if(null != list && list.size() > 0){
							c = list.get(0);
							classes = c.getClassesId();
							cse		= c.getCourseid();
							sts		= c.getCourseStatusId();
						}else if(ExStringUtils.isNotEmpty(resid)){
							sts	     =   teachingPlanCourseStatusService.get(resid);
							classes  =  classesService.get(classesid);
							cse		 = 	sts.getTeachingPlanCourse()==null?null:sts.getTeachingPlanCourse().getCourse();
							c = new CourseTeacherCl();
							c.setClassesId(classes); //班级
							c.setCourseid(cse); //课程
							c.setCourseStatusId(sts);//年级教学计划课程
						}
						if(null == classes || null == cse || null == sts){
							statusCode = 300;
							message += "第"+(i+1)+"条记录无法关联  班级-课程-教学计划！<br>";
							continue;
						}
						if("record".equals(teachtype)){
							if("del".equals(opt)){//删除
								//判断是否有录入成绩
								Map<String, Object> condition = new HashMap<String, Object>();
								condition.put("classesid", classesid);
								condition.put("courseId", cse.getResourceid());
								condition.put("studentStatus", "11','12','16','21','24','25");
								List<ExamResults> examResults = examResultsService.findExamResultsByCondition(condition);
								if(examResults!=null && examResults.size()>0){
									statusCode = 300;
									message += "第"+(i+1)+"条记录已经录入成绩！<br>";
									continue;
								}
								teacherId = "";
								setteacherName = "";
							}else if ("default".equals(opt)) {//默认
								Map<String, Object> param = new HashMap<String, Object>();
								param.put("courseid", cse.getResourceid());
								param.put("majorid", classes.getMajor().getResourceid());
								//param.put("teachingType", classes.getTeachingType());
								param.put("classicid", classes.getClassic().getResourceid());
								//param.put("gradeid", classes.getGrade().getResourceid());
								param.put("brSchoolid", classes.getBrSchool().getResourceid());
								List<Map<String, Object>> teacherList = courseStatusClService.findTeacherByCondition(param);
								if(teacherList!=null && teacherList.size()>0){
									Map<String, Object> teacher = teacherList.get(0);
									teacherId = (String) teacher.get("teacherid");
									setteacherName = (String) teacher.get("cnname");
								}else {
									teacherId = "";
									setteacherName = "";
								}
							}else if( ExStringUtils.isBlank(teacherId) || ExStringUtils.isBlank(setteacherName)){
								statusCode = 404; 
								break;
							}
							c.setTeacherName(setteacherName); //教师名称
							c.setTeachid(teacherId); //教师id	
						}else{
							if("del".equals(opt)){
								c.setLecturerName(null); //教师名称
								c.setLecturerId(null); //教师id
							}else {
								c.setLecturerName(setteacherName); //教师名称
								c.setLecturerId(teacherId); //教师id
								//map.put("lecturerid", teacherId);
								//map.put("lecturerName", setteacherName);
								if(ExStringUtils.isBlank((c.getTeachid()))){//当登分老师为空时，将任课老师默认设置为登分老师
									c.setTeacherName(setteacherName.split(",")[0]); //登分教师名称
									c.setTeachid(teacherId.split(",")[0]); //登分老师id
									//map.put("teacherId", teacherId.split(",")[0]);
									//map.put("teacherName", setteacherName.split(",")[0]);
								}
								if("networkTeach".equals(sts.getTeachType())){//如果是网络课程直接添加排课记录
									TeachingPlanCourseTimetable tpct = new TeachingPlanCourseTimetable();
									
									Map<String,Object> condition = new HashMap<String, Object>();
									condition.put("classesid", classes.getResourceid());
									condition.put("course", cse.getResourceid());
									List<TeachingPlanCourseTimetable> tpcts = teachingPlanCourseTimetableService.findTeachingPlanCourseTimetableByCondition(condition);
									
									if(tpcts!=null && tpcts.size()>0){
										tpct = tpcts.get(0);
									}
									
									tpct.setClasses(classes);
									tpct.setClassesid(classes.getResourceid());
									tpct.setCourse(cse);
									tpct.setIsPublished("Y");
									tpct.setIsStoped("N");
									tpct.setTeacherId(teacherId);
									tpct.setTeacherName(setteacherName);
									tpct.setTeachingPlanCourse(c.getCourseStatusId().getTeachingPlanCourse());
									tpct.setTerm(c.getCourseStatusId().getTerm());
									tpct.setWeek("7");
									tpct.setUnitTimePeriod(teachingPlanCourseTimePeriodService.get("402881765a87a5b9015a8cb839810af3"));
									tpct.setTimePeriodid("402881765a87a5b9015a8cb839810af3");
									
									teachingPlanCourseTimetableService.saveOrUpdate(tpct);
								}
								//20170901 增加学生问卷调查：当前需求是只做面授课程
								//如果是面授课程，按开课的年度、学期、课程类型  查找 questionnaireBatch，查找到了，就生成一条记录，查找不到就跳过
								if("faceTeach".equals(sts.getTeachType())){//面授类课程才生成记录
									createQuestionnaire(sts, c,"update");									
								}
							}
						}
						ctList.add(c);
//						courseStatusClService.saveOrUpdate(c); //添加或者更新课程-教师-班级数据
//						map.put("statusCode", 200);	
//						map.put("message", "设置登分老师成功！");
					/*}else{
						statusCode = 300;
						message = "设置失败！";
						break;
					}*/
				}
				if (ctList.size() > 0) {
					for (CourseTeacherCl courseTeacherCl : ctList) {
						courseStatusClService.saveOrUpdate(courseTeacherCl); //添加或者更新课程-教师-班级数据
					}
				}
				if(statusCode == 0) {
					statusCode = 200;
					message = "设置成功！";
					//map.put("teacherId", teacherId.split(",")[0]);
					//map.put("setteacherName", setteacherName.split(",")[0]);
				}else if(statusCode == 404){
					statusCode = 300;
					message = "老师为空！";
				}
				/*之前的逻辑使用
				 * if(ExStringUtils.isNotBlank(guiplanids) && ExStringUtils.isNotBlank(plancourseids)
						&& ExStringUtils.isNotBlank(unitids)){
					map.put("navTabId", "RES_TEACHING_TEACHINGPLANCOURSETIMETABLE_SETTEACHW");
					map.put("reloadUrl", request.getContextPath() +"/edu3/teaching/teachingplancoursetimetable/setTeach.html?" +
							"resourceids="+resids+"&classesids="+classesids+"&teacherId="+teacherId+"&setteacherName="+setteacherName);
//					+"&guiplanids="+guiplanids+"&plancourseids="+plancourseids+"&unitids="+unitids+"&classesids="+classesids
				}		*/		
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), "4", UserOperationLogs.UPDATE, ("del".equals(opt)?"删除":"修改")+"登分老师:resourceids:"+resids+"||老师ID:"+teacherId);
			} catch (Exception e) {
				logger.error("设置出错：{}",e.fillInStackTrace());
				statusCode = 300;
				message = "设置失败！";
			} finally {
				map.put("statusCode", statusCode);
				map.put("message", message);
			}
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}

	/**
	 * 生成问卷
	 * @param  courseStatus sts 开课表
	 * @param  courseStatusCl c 任课老师表
	 * @throws ServiceException
	 * @throws ServerException
	 */
	private void createQuestionnaire(TeachingPlanCourseStatus sts,
									 CourseTeacherCl c, String operate) throws ServiceException, ServerException {
		String isQuestionnaire = CacheAppManager.getSysConfigurationByCode("is_necessarily_submit_qustionnaire").getParamValue();
		if("Y".equalsIgnoreCase(isQuestionnaire)){//全局参数，是否需要学生填写问卷
			if("faceTeach".equals(sts.getTeachType())){//如果是面授课程
				Map<String, Object> condition = new HashMap<String, Object>();
				String firstYear = c.getCourseStatusId().getTerm().substring(0,4);
				String term = c.getCourseStatusId().getTerm().substring(6,7);
				condition.put("firstYear", Long.parseLong(firstYear));
				condition.put("term", term);
//				condition.put("isPublish", "Y");//教学点发布后，设置任课老师才生成问卷
				List<QuestionnaireBatch> qbList=iQuestionnaireBatchService.findQuestionnaireBatchList(condition);
				if(qbList!=null && qbList.size()==1){
					//不能使用isPublish作为判断
					//通过批次去查找，如果该教学点能找到问卷记录大于0条的问卷，就是证明已经发布过					
					//发布过的，设置任课老师才生成问卷，没发布过的，跳过
					QuestionnaireBatch qb =qbList.get(0);
					Map<String, Object> condition4 = new HashMap<String, Object>();
					condition4.put("unitId", sts.getSchoolIds());
					condition4.put("qbId",qb.getResourceid());
					List<Questionnaire> tmplist = new ArrayList<Questionnaire>();
					String hql = "from "+Questionnaire.class.getSimpleName()+" where classes.brSchool.resourceid=:unitId and isDeleted=0 and questionnaireBatch.resourceid=:qbId ";
					tmplist = iQuestionnaireService.findByHql(hql, condition4);
					if(tmplist.size()>0){//生成
						List<Questionnaire> qlist= new ArrayList<Questionnaire>();
						Map<String, Object> param = new HashMap<String, Object>();
						param.put("teachType", "faceTeach");
						if("create".equalsIgnoreCase(operate)){
							iQuestionnaireBatchService.createQuestionnaire(qlist, param, qb, c);
						}else{
							iQuestionnaireBatchService.updateQuestionnaire(qlist, param, qb, c);
						}
						
						if(qlist!=null&&qlist.size()>0){
							iQuestionnaireService.batchSaveOrUpdate(qlist);
						}
					}else{//跳过
						
					}				
					
				}
				
			}
		}
	}
	/**
	 * 保存任课老师
	 * @param request
	 * @param response
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/teachingplancoursetimetable/setLecturerSave.html")
	public void setLecturerSave(String resids, String setteacherName, String teacherId, HttpServletRequest request, HttpServletResponse response) throws WebException {
		Map<String,Object> map = new HashMap<String,Object>();
		int statusCode = 0;
		String message = "";
		if(ExStringUtils.isNotBlank(resids) && ExStringUtils.isNotBlank(teacherId) && ExStringUtils.isNotBlank(setteacherName)){ //-----设置登分教师
			try {
				String guiplanids 		= ExStringUtils.trim(request.getParameter("guiplanids")); //年级教学计划
				String plancourseids 	= ExStringUtils.trim(request.getParameter("plancourseids")); //教学计划课程
				String unitids 			= ExStringUtils.trim(request.getParameter("unitids")); //教学点
				String classesids		= ExStringUtils.trim(request.getParameter("classesids")); //班级id
				String courseids			= ExStringUtils.trim(request.getParameter("courseids")); //课程id
				String[] guiplanidary = guiplanids.split(",");
				String[] plancourseidary = plancourseids.split(",");
				String[] unitidary = unitids.split(",");
				String[] classesidary = classesids.split(",");
				String[] courseidary = courseids.split(",");
				String[] residary = resids.split(",");
				
				List<CourseTeacherCl> ctList = new ArrayList<CourseTeacherCl>(residary.length);
				for (int i = 0; i < residary.length; i++) {
					/*String guiplanid = guiplanidary[i];
					String plancourseid = plancourseidary[i];
					String unitid = unitidary[i];*/
					String classesid = classesidary[i];
					String courseid = courseidary[i];
					String resid = residary[i];
					Classes classes        			=  	classesService.get(classesid); //班级对象
					Course cse						= 	courseService.get(courseid); //课程对象
					TeachingPlanCourseStatus sts	    =   teachingPlanCourseStatusService.get(resid);//年级教学计划课程对象
					
					if(null != classes && null != cse && null != sts){
						CourseTeacherCl c = null;//课程-班级-教师  关联对象
						List<CourseTeacherCl> list 	= 	new ArrayList<CourseTeacherCl>();
						list = courseStatusClService.findByCriteria(Restrictions.eq("isDeleted", 0),Restrictions.eq("classesId", classes),Restrictions.eq("courseStatusId",
								sts),Restrictions.eq("courseid", cse));
						if(null != list && list.size() > 0){
							c = list.get(0);
						}else{
							c = new CourseTeacherCl();
							c.setClassesId(classes); //班级
							c.setCourseid(cse); //课程
							c.setCourseStatusId(sts);//年级教学计划课程
						}
							
						c.setLecturerId(teacherId);//任课老师
						c.setLecturerName(setteacherName);//任课老师名称
						if(ExStringUtils.isBlank((c.getTeachid())) && !"networkTeach".equals(sts.getTeachType())){//当登分老师为空时，将任课老师默认设置为登分老师
							c.setTeacherName(setteacherName.split(",")[0]); //登分教师名称
							c.setTeachid(teacherId.split(",")[0]); //登分老师id
							map.put("teacherId", teacherId.split(",")[0]);
							map.put("teacherName", setteacherName.split(",")[0]);
						}
						if("networkTeach".equals(sts.getTeachType())){//如果是网络课程直接添加排课记录
							TeachingPlanCourseTimetable tpct = new TeachingPlanCourseTimetable();
							
							Map<String,Object> condition = new HashMap<String, Object>();
							condition.put("classesid", classes.getResourceid());
							condition.put("course", cse.getResourceid());
							List<TeachingPlanCourseTimetable> tpcts = teachingPlanCourseTimetableService.findTeachingPlanCourseTimetableByCondition(condition);
							
							if(tpcts!=null && tpcts.size()>0){
								tpct = tpcts.get(0);
							}
							
							tpct.setClasses(classes);
							tpct.setClassesid(classes.getResourceid());
							tpct.setCourse(cse);
							tpct.setIsPublished("Y");
							tpct.setIsStoped("N");
							tpct.setTeacherId(teacherId);
							tpct.setTeacherName(setteacherName);
							tpct.setTeachingPlanCourse(c.getCourseStatusId().getTeachingPlanCourse());
							tpct.setTerm(c.getCourseStatusId().getTerm());
							tpct.setWeek("7");
							tpct.setUnitTimePeriod(teachingPlanCourseTimePeriodService.get("402881765a87a5b9015a8cb839810af3"));
							tpct.setTimePeriodid("402881765a87a5b9015a8cb839810af3");
							
							teachingPlanCourseTimetableService.saveOrUpdate(tpct);
						}
						//20170901 增加学生问卷调查：当前需求是只做面授课程
						//如果是面授课程，按开课的年度、学期、课程类型  查找 questionnaireBatch，查找到了，就生成一条记录，查找不到就跳过
						if("faceTeach".equals(sts.getTeachType())){//面授类课程才生成记录
							//先查找是否有问卷记录，如果没有，再去生成，有的话，执行替换
							createQuestionnaire(sts, c,"update");
							
						}
						ctList.add(c);
//						courseStatusClService.saveOrUpdate(c); //添加或者更新课程-教师-班级数据
//						map.put("statusCode", 200);	
//						map.put("message", "设置登分老师成功！");
					}else{
						statusCode = 300;
						message = "设置任课老师失败！";
						break;
					}
				}
				if (ctList!=null && ctList.size() > 0) {
					for (CourseTeacherCl courseTeacherCl : ctList) {
						courseStatusClService.saveOrUpdate(courseTeacherCl); //添加或者更新课程-教师-班级数据
					}
				}
				if(statusCode == 0) {
					statusCode = 200;
					message = "设置任课老师成功！";
					map.put("lecturerid", teacherId);
					map.put("lecturerName", setteacherName);					
				}
				/*之前的逻辑使用
				 * if(ExStringUtils.isNotBlank(guiplanids) && ExStringUtils.isNotBlank(plancourseids)
						&& ExStringUtils.isNotBlank(unitids)){
					map.put("navTabId", "RES_TEACHING_TEACHINGPLANCOURSETIMETABLE_SETTEACHW");
					map.put("reloadUrl", request.getContextPath() +"/edu3/teaching/teachingplancoursetimetable/setTeach.html?" +
							"resourceids="+resids+"&classesids="+classesids+"&teacherId="+teacherId+"&setteacherName="+setteacherName);
//					+"&guiplanids="+guiplanids+"&plancourseids="+plancourseids+"&unitids="+unitids+"&classesids="+classesids
				}		*/		
			} catch (Exception e) {
				logger.error("设置任课老师出错：{}",e.fillInStackTrace());
				statusCode = 300;
				message = "设置任课老师失败！";
			} finally {
				map.put("statusCode", statusCode);
				map.put("message", message);
			}
			
		} else {
			map.put("statusCode", 300);
			map.put("message", "任课老师为空！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 *老师选择对话框
	 * @param objPage
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/edu3/teaching/teachingplancoursetimetable/teacher.html")
	public String listTeacher(Page objPage, HttpServletRequest request, ModelMap model) throws Exception{
		objPage.setOrderBy("unitId");
		objPage.setOrder(Page.ASC);//设置默认排序方式
		//查询条件
		Map<String,Object> condition = new HashMap<String,Object>();
		String unitId			=		ExStringUtils.trim(request.getParameter("unitId"));//学习中心
		String unitIds			=		ExStringUtils.trim(request.getParameter("unitIds"));//学习中心
		String teachingType		=		ExStringUtils.trim(request.getParameter("teachingType"));
		String teacherCode		=		ExStringUtils.trim(request.getParameter("teacherCode"));
		String cnName			=		ExStringUtils.trim(request.getParameter("cnName"));//姓名
		String type				=		ExStringUtils.trim(request.getParameter("type"));//教师类型
		String idsN				=		ExStringUtils.trim(request.getParameter("idsN"));
		String namesN			=		ExStringUtils.trim(request.getParameter("namesN"));
		
		if(ExStringUtils.isNotEmpty(unitId)){
			condition.put("unitId", unitId);
		}else if(ExStringUtils.isNotEmpty(unitIds)){
			String[] unitIdAry = unitIds.split(",");
			condition.put("unitIds", unitIdAry);
		}
		if(ExStringUtils.isNotEmpty(teachingType)) {
			condition.put("teachingType", teachingType);
		}
		if(ExStringUtils.isNotEmpty(teacherCode)) {
			condition.put("teacherCode", teacherCode);
		}
		if(ExStringUtils.isNotEmpty(cnName)) {
			condition.put("cnName", cnName);
		}
		if(ExStringUtils.isNotEmpty(type)){
			String teacherType = "";
			if("0".equals(type)){//负责老师，改为：主讲老师
//				teacherType = CacheAppManager.getSysConfigurationByCode("sysuser.role.teacher.master").getParamValue();
//			} else if("1".equals(type)){//主讲老师
				teacherType = CacheAppManager.getSysConfigurationByCode("sysuser.role.teacher.duty").getParamValue();
			} else if("2".equals(type)){//辅导老师
				teacherType = CacheAppManager.getSysConfigurationByCode("sysuser.role.teacher.coach").getParamValue();
			}
			condition.put("type", type);
			condition.put("teacherType", teacherType);	
		}
		condition.put("isDeleted", 0);
		condition.put("roleCode", CacheAppManager.getSysConfigurationByCode("sysuser.role.teacher").getParamValue());
		Page page = edumanagerService.findEdumanagerByCondition(condition, objPage);
		if(ExStringUtils.isNotEmpty(unitIds)){
			condition.put("unitIds", unitIds);
		}
		model.addAttribute("unitId", unitId==null?"":unitId);
		model.addAttribute("unitIds", unitIds);
		model.addAttribute("idsN", idsN);
		model.addAttribute("namesN", namesN);
		condition.put("idsN", idsN);
		condition.put("namesN", namesN);
		model.addAttribute("teacherlist", page);
		model.addAttribute("condition", condition);		
		return "/edu3/teaching/teachingplancoursearrange/selector-teacher";
	}
	
	/**
	 *老师选择对话框(最新)
	 * @param objPage
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/edu3/teaching/teachingplancoursetimetable/teacher_1.html")
	public String listRecordScoreTeacher(Page objPage, HttpServletRequest request, ModelMap model) throws Exception{
		/*objPage.setOrderBy("unitId");
		objPage.setOrder(Page.ASC);//设置默认排序方式
		*/		
		//objPage.setOrderBy("orgUnit.unitCode ");
		objPage.setOrderBy("unitId ");
		objPage.setOrder(Page.DESC);//设置默认排序方式
		//查询条件
		Map<String,Object> condition = new HashMap<String,Object>();
		String unitIds			=		ExStringUtils.trim(request.getParameter("unitIds"));//学习中心
		String teacherCode	=		ExStringUtils.trim(request.getParameter("teacherCode"));
		String cnName			=		ExStringUtils.trim(request.getParameter("cnName"));//姓名
		String type				=		ExStringUtils.trim(request.getParameter("type"));//教师类型
		String teachtype    =  ExStringUtils.trim(request.getParameter("teachtype"));//教师类别   登分老师/任课老师
		if(ExStringUtils.isNotEmpty(teacherCode)) {
			condition.put("teacherCode", teacherCode);
		}
		if(ExStringUtils.isNotEmpty(cnName)) {
			condition.put("cnName", cnName);
		}
		if(ExStringUtils.isNotEmpty(unitIds)){
			OrgUnit _unit = orgUnitService.findUniqueByProperty("unitCode", "DEPT_TEACHING_CENTRE");
			condition.put("unitIds", unitIds+"','"+_unit.getResourceid());
		}
		User user = SpringSecurityHelper.getCurrentUser();

		if(user.getOrgUnit().getUnitType().equals(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())){//如果为校外学习中心人员
			model.addAttribute("jwy",user);
			model.addAttribute("isBrschool", true);
		}
		//type: 0-负责老师，1-主讲老师，2-辅导老师
		if(ExStringUtils.isNotEmpty(type)){
			String teacherType = "";
			if("0".equals(type) || "1".equals(type)){
//				teacherType = CacheAppManager.getSysConfigurationByCode("sysuser.role.teacher.master").getParamValue();
				teacherType = CacheAppManager.getSysConfigurationByCode("sysuser.role.teacher.duty").getParamValue();
			} else if("2".equals(type)){
				teacherType = CacheAppManager.getSysConfigurationByCode("sysuser.role.teacher.coach").getParamValue();
			}
			condition.put("type", type);
			condition.put("teacherType", teacherType);	
		}
		condition.put("isDeleted", 0);
		condition.put("roleCode", CacheAppManager.getSysConfigurationByCode("sysuser.role.teacher").getParamValue());
		
		Page page = edumanagerService.findEdumanagerByCondition(condition, objPage);
		
		condition.put("selectedTeachers", ExStringUtils.trimToEmpty(request.getParameter("selectedTeachers")));
		condition.put("selectedNames", ExStringUtils.trimToEmpty(request.getParameter("selectedNames")));
		condition.put("isBatch", ExStringUtils.trimToEmpty(request.getParameter("isBatch")));
		condition.put("teachtype", ExStringUtils.trimToEmpty(request.getParameter("teachtype")));
		condition.put("resIds", ExStringUtils.trimToEmpty(request.getParameter("resIds")));
		condition.put("classesIds", ExStringUtils.trimToEmpty(request.getParameter("classesIds")));
		condition.put("gpIds", ExStringUtils.trimToEmpty(request.getParameter("gpIds")));
		condition.put("pcIds", ExStringUtils.trimToEmpty(request.getParameter("pcIds")));
		condition.put("courseIds", ExStringUtils.trimToEmpty(request.getParameter("courseIds")));
		/*model.addAttribute("resIds", ExStringUtils.trim(request.getParameter("resIds")));
		model.addAttribute("classesIds", ExStringUtils.trim(request.getParameter("classesIds")));
		model.addAttribute("gpIds", ExStringUtils.trim(request.getParameter("gpIds")));
		model.addAttribute("pcIds", ExStringUtils.trim(request.getParameter("pcIds")));
		model.addAttribute("courseIds", ExStringUtils.trim(request.getParameter("courseIds")));*/
		model.addAttribute("isBatch", ExStringUtils.trim(request.getParameter("isBatch")));
		model.addAttribute("teacherlist", page);
		model.addAttribute("condition", condition);	
		return "/edu3/teaching/teachingplancoursearrange/selector-teacher_1";
	}
	
	
	/**
	 *老师选择对话框(最新)
	 * @param objPage
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/edu3/teaching/teachingplancoursetimetable/lecturer.html")
	public String listRecordlecturer(Page objPage, HttpServletRequest request, ModelMap model) throws Exception{
		
		/* objPage.setOrderBy("unitId");
		 objPage.setOrder(Page.ASC);//设置默认排序方式
		 */
		//由于后面使用了DISTINCT 关键字，所以此处不能使用除了edumanager表之外的排序
//		objPage.setOrderBy("orgUnit.unitCode ");
		objPage.setOrderBy("unitId ");
		objPage.setOrder(Page.DESC);//设置默认排序方式
		//查询条件
		Map<String,Object> condition = new HashMap<String,Object>();
		String unitIds			=		ExStringUtils.trim(request.getParameter("unitIds"));//学习中心
		String teacherCode	=		ExStringUtils.trim(request.getParameter("teacherCode"));
		String cnName			=		ExStringUtils.trim(request.getParameter("cnName"));//姓名
		String type				=		ExStringUtils.trim(request.getParameter("type"));//教师类型
		
		if(ExStringUtils.isNotEmpty(teacherCode)) {
			condition.put("teacherCode", teacherCode);
		}
		if(ExStringUtils.isNotEmpty(cnName)) {
			condition.put("cnName", cnName);
		}
		if(ExStringUtils.isNotEmpty(unitIds)){
			OrgUnit _unit = orgUnitService.findUniqueByProperty("unitCode", "DEPT_TEACHING_CENTRE");
			condition.put("unitIds", unitIds+"','"+_unit.getResourceid());
		}
		if(ExStringUtils.isNotEmpty(type)){
			String teacherType = "";
			if("0".equals(type)){//负责老师，改为：主讲老师
//				teacherType = CacheAppManager.getSysConfigurationByCode("sysuser.role.teacher.master").getParamValue();
//			} else if("1".equals(type)){//主讲老师
				teacherType = CacheAppManager.getSysConfigurationByCode("sysuser.role.teacher.duty").getParamValue();
			} else if("2".equals(type)){//辅导老师
				teacherType = CacheAppManager.getSysConfigurationByCode("sysuser.role.teacher.coach").getParamValue();
			}
			condition.put("type", type);
			condition.put("teacherType", teacherType);	
		}
		condition.put("isDeleted", 0);
		condition.put("roleCode", CacheAppManager.getSysConfigurationByCode("sysuser.role.teacher").getParamValue());
		Page page = edumanagerService.findEdumanagerByCondition(condition, objPage);
		
		condition.put("selectedTeachers", ExStringUtils.trimToEmpty(request.getParameter("selectedTeachers")));
		condition.put("selectedNames", ExStringUtils.trimToEmpty(request.getParameter("selectedNames")));
		condition.put("isBatch", ExStringUtils.trimToEmpty(request.getParameter("isBatch")));
		condition.put("teachtype", ExStringUtils.trimToEmpty(request.getParameter("teachtype")));
		//model.addAttribute("unitIds", unitIds);
		condition.put("resIds", ExStringUtils.trimToEmpty(request.getParameter("resIds")));
		condition.put("classesIds", ExStringUtils.trimToEmpty(request.getParameter("classesIds")));
		condition.put("gpIds", ExStringUtils.trimToEmpty(request.getParameter("gpIds")));
		condition.put("pcIds", ExStringUtils.trimToEmpty(request.getParameter("pcIds")));
		condition.put("courseIds", ExStringUtils.trimToEmpty(request.getParameter("courseIds")));
		/*model.addAttribute("resIds", ExStringUtils.trim(request.getParameter("resIds")));
		model.addAttribute("classesIds", ExStringUtils.trim(request.getParameter("classesIds")));
		model.addAttribute("gpIds", ExStringUtils.trim(request.getParameter("gpIds")));
		model.addAttribute("pcIds", ExStringUtils.trim(request.getParameter("pcIds")));
		model.addAttribute("courseIds", ExStringUtils.trim(request.getParameter("courseIds")));*/
		model.addAttribute("isBatch", ExStringUtils.trim(request.getParameter("isBatch")));
		model.addAttribute("teacherlist", page);
		model.addAttribute("condition", condition);		
		return "/edu3/teaching/teachingplancoursearrange/selector-lecturer";
	}
	
	/**
	 * 导出课表 
	 */
	private void exportTimetable(List<Map<String, Object>> results, Map<String, Object> condition, HttpServletResponse response) throws IOException {
		OrgUnit unit = orgUnitService.get(condition.get("brSchoolid").toString());
		String termName = condition.get("term").toString();
		String[] termAry = termName.split("_");
		termName = termAry[0] + "年" + ("01".equals(termAry[1]) ? "春" : "秋") + "季学期";
		String fileName = unit.getUnitName() + termName + "课程表";
		List<Dictionary> weekList = CacheAppManager.getChildren("CodeWeek");//星期几
		List<Dictionary> periodList = CacheAppManager.getChildren("CodeCourseTimePeriod");//上午下午晚上
		Page objPage = new Page();
		objPage.setPageSize(1000);
		Map<String,Object> condition2 = new HashMap<String,Object>();
		condition2.put("brSchoolid", condition.get("brSchoolid"));
		//时间段第几节
		List<TeachingPlanCourseTimePeriod> timeperiodList = teachingPlanCourseTimePeriodService.findTeachingPlanCourseTimePeriodByCondition(condition2, objPage).getResult();
		//组装 上午下午晚上 对应 第几节 关系
		Map<Dictionary, List<TeachingPlanCourseTimePeriod>> timeperiodMap = new LinkedHashMap<Dictionary, List<TeachingPlanCourseTimePeriod>>(periodList.size());
		for (Dictionary period : periodList) {
			List<TeachingPlanCourseTimePeriod> tpList = new ArrayList<TeachingPlanCourseTimePeriod>();
			timeperiodMap.put(period, tpList);
		}
		for (TeachingPlanCourseTimePeriod timePeriod : timeperiodList) {
			for (Dictionary tp : timeperiodMap.keySet()) {
				if (tp.getDictValue().equals(timePeriod.getTimePeriod())) {
					timeperiodMap.get(tp).add(timePeriod);
				}
			}
		}
		//组装课表数据
		Map<Classes, List<TeachingPlanCourseTimetable>> timetableMap = new LinkedHashMap<Classes, List<TeachingPlanCourseTimetable>>();
		for (Map<String, Object> result : results) {
			Classes classes = classesService.get(result.get("classesid").toString());
			Page page = new Page();
			page.setPageSize(1);
			String sql = "select count(*) c from edu_roll_studentinfo s where s.isdeleted=0 and s.classesid='"+classes.getResourceid()+"' and s.studentStatus = '11'";
			List<Map<String, Object>> resultList = baseSupportJdbcDao.getBaseJdbcTemplate().findListMap(page, sql, null).getResult();
			int studentNum = Integer.parseInt(resultList.get(0).get("c").toString());
			classes.setStudentNum(studentNum);
			Map<String, Object> condition3 = new HashMap<String, Object>();
			condition3.put("classesid", result.get("classesid").toString());
			condition3.put("plancourseid", result.get("plancourseid").toString());
			condition3.put("term", result.get("courseterm").toString());
			List<TeachingPlanCourseTimetable> timetableList = teachingPlanCourseTimetableService.findTeachingPlanCourseTimetableByCondition(condition3);
			if (timetableMap.containsKey(classes)) {
				timetableMap.get(classes).addAll(timetableList);
			} else {
				timetableMap.put(classes, timetableList);
			}
		}
		String schoolName = CacheAppManager.getSysConfigurationByCode("graduateData.schoolName").getParamValue()
			+ CacheAppManager.getSysConfigurationByCode("graduateData.schoolConnectName").getParamValue() + "学院" + termName + "课程表";
		try {
			GUIDUtils.init();
			File disFile = new File(getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(false) + ".xls");
			// 打开文件
			WritableWorkbook book = Workbook.createWorkbook(disFile);
			// 生成名为“第一页”的工作表，参数0表示这是第一页
			WritableSheet sheet = book.createSheet("课表", 0);

			WritableCellFormat format1 = new WritableCellFormat();
			// 把水平对齐方式指定为居中
			format1.setAlignment(jxl.format.Alignment.CENTRE);
			// 把垂直对齐方式指定为居中
			format1.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			// 设置边框线
			format1.setBorder(Border.ALL, BorderLineStyle.THIN);

			WritableCellFormat format2 = new WritableCellFormat();
			// 把水平对齐方式指定为居中
			format2.setAlignment(jxl.format.Alignment.CENTRE);
			// 把垂直对齐方式指定为居中
			format2.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			// 设置边框线
			format2.setBorder(Border.ALL, BorderLineStyle.THIN);
			format2.setWrap(true);//通过调整宽度和高度自动换行

			WritableCellFormat format3 = new WritableCellFormat();
			// 把水平对齐方式指定为左对齐
			format3.setAlignment(jxl.format.Alignment.LEFT);
			// 把垂直对齐方式指定为居中
			format3.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			// 设置边框线
			format3.setBorder(Border.ALL, BorderLineStyle.THIN);
			format3.setWrap(true);//通过调整宽度和高度自动换行

			WritableFont font4 = new WritableFont(WritableFont.TIMES, 16 ,WritableFont.BOLD);
			WritableCellFormat format4 = new WritableCellFormat(font4);
			// 把水平对齐方式指定为居中
			format4.setAlignment(jxl.format.Alignment.CENTRE);
			// 把垂直对齐方式指定为居中
			format4.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			// 设置边框线
			format4.setBorder(Border.ALL, BorderLineStyle.THIN);

			CellView cellView = new CellView(); //自动调整列宽
			cellView.setAutosize(true); //设置自动大小

			//标题行开始
			sheet.mergeCells(0, 1, 0, 3);
//			sheet.setColumnView(0, 40);
			sheet.setColumnView(0, cellView);//根据内容自动设置列宽
			sheet.addCell(new Label(0, 1, "年级、班别、人数", format1));

			//星期几
			int startCol = 1;
			int startCol2 = 1;
			int startCol3 = 1;
			int startCol4 = 0;
			for (int i = 0; i < weekList.size(); i++) {
				Dictionary week = weekList.get(i);
				int endCol = startCol + timeperiodList.size() - 1;
				startCol4 = endCol + 1;
//				System.out.println(startCol+"---"+endCol);
				sheet.mergeCells(startCol, 1, endCol, 1);
				sheet.addCell(new Label(startCol, 1, week.getDictName(), format1));
				startCol += timeperiodList.size();
				//上午下午晚上
				for (Dictionary tp : timeperiodMap.keySet()) {
					int endCol2 = startCol2 + timeperiodMap.get(tp).size() - 1;
//					System.out.println(startCol2+"---"+endCol2);
					sheet.mergeCells(startCol2, 2, endCol2, 2);
					sheet.addCell(new Label(startCol2, 2, tp.getDictName(), format1));
					startCol2 += timeperiodMap.get(tp).size();
					//第几节
					for (TeachingPlanCourseTimePeriod timePeriod : timeperiodMap.get(tp)) {
						int endCol3 = startCol3;
//						System.out.println(startCol3+"---"+endCol3);
						sheet.mergeCells(startCol3, 3, endCol3, 3);
						sheet.addCell(new Label(startCol3, 3, timePeriod.getTimeName(), format1));
						startCol3 += 1;
					}
				}
			}

			sheet.mergeCells(startCol4, 1, startCol4, 3);
//			sheet.setColumnView(startCol4, 30);
			sheet.setColumnView(startCol4, cellView);//根据内容自动设置列宽
			sheet.addCell(new Label(startCol4, 1, "各课程学时数\r\n【总学时（理论，实践）】", format1));
			//标题行结束

			//表头
			sheet.mergeCells(0, 0, startCol4, 0);
//			sheet.setColumnView(0, 40);
			sheet.setColumnView(0, cellView);//根据内容自动设置列宽
			sheet.addCell(new Label(0, 0, schoolName, format4));

			//输出课表数据开始
			int rowIndex = 4;
			for (Classes classes : timetableMap.keySet()) {
				sheet.addCell(new Label(0, rowIndex, classes.getClassname()+"\r\n（"+classes.getStudentNum()+"）", format2));
				List<TeachingPlanCourseTimetable> timetableList = timetableMap.get(classes);
				int colIndex = 1;
				//教学计划课程
				Set<TeachingPlanCourse> planCourseSet = new HashSet<TeachingPlanCourse>();
				for (int i = 0; i < weekList.size(); i++) {//星期几
					Dictionary week = weekList.get(i);
					for (Dictionary tp : timeperiodMap.keySet()) {//上午下午晚上
						for (TeachingPlanCourseTimePeriod timePeriod : timeperiodMap.get(tp)) {//第几节
							String timetableStr = "";
							for (TeachingPlanCourseTimetable timetable : timetableList) {//课表
								//如果是该星期和节数的课程就输出
								if (timetable.getWeek().equals(week.getDictValue()) && timetable.getUnitTimePeriod().getResourceid().equals(timePeriod.getResourceid())) {
									if (!"".equals(timetableStr)) {
										timetableStr += "，";
									}
									timetableStr += timetable.getCourse().getCourseName();
									if (!planCourseSet.contains(timetable.getTeachingPlanCourse())) {//组装教学计划课程
										planCourseSet.add(timetable.getTeachingPlanCourse());
									}
								}
							}
							sheet.addCell(new Label(colIndex, rowIndex, timetableStr, format2));
							colIndex++;
						}
					}
				}
				String studyHourStr = "";
				for (TeachingPlanCourse planCourse : planCourseSet) {
					if (!"".equals(studyHourStr)) {
						studyHourStr += "，";
					}
					studyHourStr += planCourse.getCourse().getCourseName() + " " + planCourse.getStydyHour()
						+ "（" + planCourse.getFaceStudyHour() + "，"
						+ (planCourse.getStydyHour() - planCourse.getFaceStudyHour()) + "）";
				}
				sheet.addCell(new Label(colIndex, rowIndex, studyHourStr, format3));
				rowIndex++;
			}
			//输出课表数据结束

			// 写入数据并关闭文件
            book.write();
            book.close();
            logger.info("获取导出的excel文件:{}",disFile.getAbsoluteFile());
			downloadFile(response, fileName+".xls", disFile.getAbsolutePath(),true);
		} catch (WriteException e) {
			logger.error(e.getMessage(),e.fillInStackTrace());
		}
	}

	private void exportTimetable(String term, HttpServletResponse response, Classes classes) throws IOException {
		List<Map<String, Object>> list = getStudentTimetableList(term, classes);
		if(ExCollectionUtils.isNotEmpty(list)){
			GUIDUtils.init();
			File disFile = new File(getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(false) + ".xls");
			Map<String,Object> templateMap = new HashMap<String, Object>();//设置模板
			templateMap.put("term", JstlCustomFunction.dictionaryCode2Value("CodeCourseTermType", term)+"课表");
			templateMap.put("brSchool", classes.getBrSchool().getUnitName());
			templateMap.put("teachingType", JstlCustomFunction.dictionaryCode2Value("CodeTeachingType", classes.getTeachingType()));
			templateMap.put("gradeName", classes.getGrade().getGradeName());
			templateMap.put("classicName", classes.getClassic().getClassicName());
			templateMap.put("majorName", classes.getMajor().getMajorName());
//			templateMap.put("studentNum", classes.getStudentNum());
			Map<String, Object> param = new HashMap<String, Object>(1);
			param.put("classesid", classes.getResourceid());
			int stuNum = studentInfoService.getStudentNum(param);
			templateMap.put("studentNum", stuNum);
			templateMap.put("className", classes.getClassname());
			//templateMap.put("executeDate", ExDateUtils.formatDateStr(new Date(), ExDateUtils.PATTREN_DATE_CN));
			StringBuilder memo = new StringBuilder();
			try {
				int index = (Integer) list.get(0).get("index");
				memo.append(list.get(0).get("memo"));

				Map<String, Object> condition = new HashMap<String, Object>();
				condition.put("classesid", classes.getResourceid());
				condition.put("term", term);
				List<Map<String, Object>> historyList = teachingPlanCourseTimetableService.findCourseTimetableHistoryByCondition(condition);
				if(ExCollectionUtils.isNotEmpty(historyList)){

					for (Map<String, Object> map : historyList) {
						memo.append(index++).append(". ").append(JstlCustomFunction.dictionaryCode2Value("CodeWeek", map.get("week").toString())).append(JstlCustomFunction.dictionaryCode2Value("CodeCourseTimePeriod", map.get("timeperiod").toString())).append(map.get("coursename")).append(":").append(map.get("aftervalue").toString().replaceAll("\\<br/>", " \n"));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			templateMap.put("memo", memo.toString());

			String brSchoolMan = "";
			String linkInfo = "";
			try {
				OrgUnit unit = classes.getBrSchool();
				if(unit.getOrgUnitExtends() !=null && null != unit.getOrgUnitExtends().get(OrgUnitExtends.UNIT_EXTENDCODE_TEACHINGMAN)){
					brSchoolMan = unit.getOrgUnitExtends().get(OrgUnitExtends.UNIT_EXTENDCODE_TEACHINGMAN).getExValue();
				}
				if(unit.getOrgUnitExtends() !=null && null != unit.getOrgUnitExtends().get(OrgUnitExtends.UNIT_EXTENDCODE_TEACHINGTEL)){
					linkInfo += unit.getOrgUnitExtends().get(OrgUnitExtends.UNIT_EXTENDCODE_TEACHINGTEL).getExValue();
				}
				if(unit.getOrgUnitExtends() !=null && null != unit.getOrgUnitExtends().get(OrgUnitExtends.UNIT_EXTENDCODE_TEACHINGEMAIL)){
					if(ExStringUtils.isNotBlank(linkInfo)) {
						linkInfo += "/";
					}
					linkInfo += unit.getOrgUnitExtends().get(OrgUnitExtends.UNIT_EXTENDCODE_TEACHINGEMAIL).getExValue();
				}
			} catch (Exception e) {
			}
			templateMap.put("brSchoolMan", brSchoolMan);
			templateMap.put("linkInfo", linkInfo);
			templateMap.put("schoolAndConnect", CacheAppManager.getSysConfigurationByCode("graduateData.schoolName").getParamValue()+
					CacheAppManager.getSysConfigurationByCode("graduateData.schoolConnectName").getParamValue());

			String templateFilepathString = SystemContextHolder.getAppRootPath()+"WEB-INF"+File.separator+"templates"+File.separator+"excel"+File.separator+"studentCourseTimetable.xls";
			exportExcelService.initParamsByfile(disFile, "studentCourseTimetable", list, null, null, null);
			exportExcelService.getModelToExcel().setTemplateParam(templateFilepathString, 3, templateMap);

			WritableFont font = new WritableFont(WritableFont.TIMES, 10);
		    WritableCellFormat format = new WritableCellFormat(font);
		    try {
		        format.setAlignment(jxl.format.Alignment.CENTRE);
		        format.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
		        format.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);
		        format.setWrap(true);//单元格内容自动换行
		    } catch (WriteException e) {
		        logger.error(e.getMessage(),e.fillInStackTrace());
		    }
			exportExcelService.getModelToExcel().setNormolCellFormat(format);
			
			disFile = exportExcelService.getExcelFile();//获取导出的文件
			logger.info("获取导出的excel文件:{}",disFile.getAbsoluteFile());
			downloadFile(response, classes.getClassname()+"课表.xls", disFile.getAbsolutePath(),true);
		} else {
			renderHtml(response, "<script type=\"text/javascript\">parent.alertMsg.warn(\"请先进行排课\")</script>");
		}
	}
	
	@RequestMapping("/edu3/teaching/teachingplancoursetimetable/history.html")
	public String historyTeachingPlanCourseTimetable(String classesid, String courseid, Page objPage, ModelMap model) throws WebException {
		User user = SpringSecurityHelper.getCurrentUser();
		String brSchoolid = null;
		if(user.getOrgUnit().getUnitType().equals(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())){//如果为校外学习中心人员
			brSchoolid = user.getOrgUnit().getResourceid();
			model.addAttribute("isBrschool", true);
		}
		
		Map<String, Object> condition = new HashMap<String, Object>();
		if(ExStringUtils.isNotBlank(courseid)) {
			condition.put("courseid", courseid);
		}
		if(ExStringUtils.isNotBlank(classesid)) {
			condition.put("classesid", classesid);
		}
		if(ExStringUtils.isNotBlank(brSchoolid)) {
			condition.put("brSchoolid", brSchoolid);
		}
		
		Page historyPage = teachingPlanCourseTimetableService.findCourseTimetableHistoryByCondition(condition, objPage);
		model.addAttribute("historyPage", historyPage);
		model.addAttribute("condition", condition);
		return "/edu3/teaching/teachingplancoursearrange/coursetimetable-history";
	}
	
	/**
	 * 导入排课结果 [功能点]
	 * @return
	 * @throws WebException
	 */
	@RequestMapping(value = {"/edu3/teaching/teachingplantimetable/import.html"})
	public String uploadRecruitExameeInfo(String term, String classesid, String plancourseid, HttpServletRequest request, ModelMap model) throws WebException {
		Classes classes = classesService.get(classesid);
		TeachingPlanCourse planCourse = teachingPlanCourseService.get(plancourseid);
		model.addAttribute("classes",classes);
		model.addAttribute("planCourse",planCourse);
		String jspPath = "/edu3/teaching/teachingplancoursearrange/coursetimetable-list-import";
		String title = "导入排课结果";
		if(ExStringUtils.isBlank(term)){
			title = "导入排考结果";
			jspPath = "/edu3/teaching/teachingplancoursearrange/examination_list_import";
		}else {
			//教师库
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("isDeleted", 0);
			param.put("unitId", classes.getBrSchool().getResourceid());
			param.put("roleCode", CacheAppManager.getSysConfigurationByCode("sysuser.role.teacher").getParamValue());
			List<Edumanager> edumanagerList = edumanagerService.findEdumanagerByCondition(param);
			model.addAttribute("edumanagerList", edumanagerList);
			model.addAttribute("edumanagerList", edumanagerList);model.addAttribute("edumanagerList", edumanagerList);
			model.addAttribute("term",term);
		}
		return jspPath;
	}
	
	/**
	 * 导入排课结果  [导入按钮]
	 * @param term
	 * @param courseids
	 * @param attachId
	 * @param classid
	 * @param plancourseid
	 * @param request
	 * @param response
	 * @param model
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/teachingplantimetable/import/submit.html")
	public void importExameeInfo(String term, String courseids, String attachId, String classid, String plancourseid, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws WebException {
		Map<String ,Object> map = new HashMap<String, Object>();
//		Integer statusCode            =  200;  //是否成功  200:成功/300：失败
//		String  message               =  "";   //提示信息
		String  excelErr			  =  "";   //excel导入时错误行数
		
		if(!ExStringUtils.isNotBlank(request.getParameter("teachnames")) || !ExStringUtils.isNotBlank(request.getParameter("timenames"))
				|| !ExStringUtils.isNotBlank(request.getParameter("roomnames"))){
			map.put("statusCode", 300);
			map.put("message", "没有可分配的教师，上课地点，上课时间段！");
//			statusCode = 300;		
//			message    = "没有可分配的教师，上课地点，上课时间段！";
		}else{
			String[] teachids = ExStringUtils.isNotBlank(request.getParameter("teachids"))?request.getParameter("teachids").split(","):null; //教师id
			String[] teachnames = ExStringUtils.isNotBlank(request.getParameter("teachnames"))?request.getParameter("teachnames").split(","):null; //教师名称
			String[] timeids = ExStringUtils.isNotBlank(request.getParameter("timeids"))?request.getParameter("timeids").split(","):null;//时间段id
			String[] timenames = ExStringUtils.isNotBlank(request.getParameter("timenames"))?request.getParameter("timenames").split(","):null;//
			String[] roomids = ExStringUtils.isNotBlank(request.getParameter("roomids"))?request.getParameter("roomids").split(","):null;//上课地点id
			String[] roomnames = ExStringUtils.isNotBlank(request.getParameter("roomnames"))?request.getParameter("roomnames").split(","):null;//上课地点
			
			try {			
				if(ExStringUtils.isNotBlank(attachId)){
					if( teachids.length > 0 && timeids.length > 0 && roomids.length > 0 && 
							teachids.length == teachnames.length && roomids.length == roomnames.length && timeids.length == timenames.length )
					{
						Attachs attachs 	=  attachsService.get(attachId);//获取上传的文件，因为是单个文件，所以get(0)就可
						File excel          =  new File(attachs.getSerPath()+File.separator+attachs.getSerName());				
						
						List<String> dictCodeList = new ArrayList<String>();
						//处理数据字典字段
						importExcelService.initParmas(excel, "coursetimetableImports", dictionaryService.getDictionByMap(dictCodeList, true,IDictionaryService.PREKEY_TYPE_BYNAME));
						importExcelService.getExcelToModel().setSheet(0);//设置excel sheet 0 
						User user = SpringSecurityHelper.getCurrentUser();
						//取出数据
						List<TeachingPlanCourseTimetableVo> modelList = importExcelService.getModelList();
						
						Classes cl = classesService.get(classid); //班级
						TeachingPlanCourse planCourse = teachingPlanCourseService.get(plancourseid);//教学计划
						Course c = courseService.get(courseids);//课程
						
						
						//循环save
						boolean flag = true;
						int i = 1;
						for(TeachingPlanCourseTimetableVo tv:modelList){
							i +=1;
							TeachingPlanCourseTimetable t = new TeachingPlanCourseTimetable();
							t.setOperatorName(user.getCnName());
							t.setMergeMemo(tv.getMergeMemo());
							//教师
							t.setTeacherName(tv.getTeachName());//上课老师
							for(int ti = 0; ti < teachnames.length; ti++){
								if(teachnames[ti].trim().equals(tv.getTeachName()==null?null:tv.getTeachName().trim())){
									t.setTeacherId(teachids[ti].trim()); //匹配教师id
								}
							}

							//上课地点
							//t.setAddress(tv.getAddress());//上课地址
							Classroom rm = null;
							for(int ti = 0; ti < roomnames.length; ti++){
								if(roomnames[ti].trim().equals(tv.getAddress()==null?null:tv.getAddress().trim())){
									//匹配上课地点id
									rm = classroomService.get(roomids[ti].trim());
									t.setClassroom(rm);//上课地点
								}
							}
							
							//时间段
							TeachingPlanCourseTimePeriod ttp = null;
							t.setTimePeriod(tv.getSchoolTimeSlot());//时间段	
							for(int ti = 0; ti < timenames.length; ti++){
								if(timenames[ti].trim().equals(tv.getSchoolTimeSlot()==null?null:tv.getSchoolTimeSlot().trim())){
									//匹配时间段id
									ttp = teachingPlanCourseTimePeriodService.get(timeids[ti].trim());
									t.setUnitTimePeriod(ttp);//时间段ID
								}
							}
							
							//星期  星期一-日：1-7
							List<String> weeks = Arrays.asList(new String[]{"1","2","3","4","5","6","7"});
							Boolean f = true;
							if(!weeks.contains(tv.getSchollWeek()==null?"":tv.getSchollWeek().toString().trim())){
								f = false;
							}
							
							//判断正在执行的excel行是否是正确的数据并且保证save的行能显示到排课list里
							if(ttp == null || rm == null || planCourse == null || cl == null || c == null || !ExStringUtils.isNotBlank(tv.getTeachName()) || !ExStringUtils.isNotBlank(t.getTeacherId())
									|| "".equals(tv.getAddress()) || !ExStringUtils.isNotBlank(tv.getSchollWeek())	|| !ExStringUtils.isNotBlank(tv.getSchoolTime().toString())
									|| !ExStringUtils.isNotBlank(tv.getSchoolTimeSlot()) || f == false ){
									flag = false;
									excelErr += i+",";
									continue;
							}
							
							t.setClasses(cl);//班级
							t.setTeachingPlanCourse(planCourse);//教学计划
							t.setCourse(c);//课程
							
							//处理excel的时间
							SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
							String time = fmt.format(tv.getSchoolTime());
							t.setTeachDate(time);//上课日期
							t.setWeek(tv.getSchollWeek());//上课星期
							t.setMemo(tv.getRemark());//临时调课备注
							

							t.setTerm(term == null ? "" : term);//开课学期						
							t.setIsStoped("N"); //是否停课
							t.setIsDeleted(0);
							t.setVersion(0L);
							
							//t.setStartTime(null);//开始时间
							//t.setEndTime(null);//结束时间
							//t.setIsPublished(null);
							
							//保存
							teachingPlanCourseTimetableService.save(t);
						}
						
						if(flag == true){
							map.put("statusCode", 200);
							map.put("message", "导入排课结果成功！");
//							statusCode = 200;		
//							message    = "导入排课结果成功！";	
						}else{
							map.put("statusCode", 300);
							map.put("message", "".equals(excelErr.trim())?"导入排课结果失败！":"第"+excelErr.substring(0,excelErr.length()-1)+"行数据导入失败，请仔细核对要导入的数据！");
//							statusCode  =  300;
//							message     =  "".equals(excelErr.trim())?"导入排课结果失败！":"第"+excelErr.substring(0,excelErr.length()-1)+"行数据导入失败，请仔细核对要导入的数据！";
						}
					}else{
						map.put("statusCode", 300);
						map.put("message", "无发为该课程排课!");
//						statusCode  	    =  300;
//						message      		=  "无发为该课程排课!";
					}		
				} else {
					map.put("statusCode", 300);
					map.put("message", "请选择一个要导入的excel!");
//					statusCode  			=  300;
//					message      			=  "请选择一个要导入的excel!";
				}
			} catch (Exception e) {
				map.put("statusCode", 300);
				map.put("message", "导入排课结果出错,请检查要导入的数据:{}"+e.fillInStackTrace());
//				statusCode  			  	=  300;
//				message      				=  "导入排课结果出错,请检查要导入的数据:{}"+e.fillInStackTrace();
				logger.error("导入排课结果出错:{}",e.fillInStackTrace());
			} 
		}
		
		map.put("excelErr", excelErr.trim());
//		map.put("statusCode", statusCode);
//		map.put("message", message);
		map.put("reloadUrl", request.getContextPath()+"/edu3/teaching/teachingplancoursetimetable/list.html");
		map.put("classesid", ExStringUtils.trimToEmpty(classid));
		map.put("plancourseid", ExStringUtils.trimToEmpty(plancourseid));
		map.put("term", ExStringUtils.trimToEmpty(term));
	//	map.put("navTabId", "RES_TEACHING_TEACHINGPLANTIMETABLE_IMPORT");
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	
	/**
	 *查看教学点开课情况
	 * @param objPage
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/edu3/teaching/teachingplancoursestatus/showOpenSchool.html")
	public String listOpenStatus(Page objPage, HttpServletRequest request, ModelMap model) throws Exception{
		objPage.setOrderBy("unitId");
		objPage.setOrder(Page.ASC);//设置默认排序方式
		//查询条件
		Map<String,Object> condition = new HashMap<String,Object>();
		StringBuffer hql = new StringBuffer();
		
		//Page page = teachingPlanCourseService.findByCriteria(page, Restrictions.in("studentInfo.resourceid", String[]));	
			
		
		return "/edu3/teaching/teachingplancoursearrange/show-open-list";
	}
	
	/**
	 * 选择自动开课的学期
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/teachingplancoursestatus/selectTerm.html")
	public String selectOpenCourseTerm(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws WebException {
		
		Map<String, String> openCourseTermMap= new LinkedHashMap<String, String>();
		try {
			// 获取当前时间
			Calendar calendar = Calendar.getInstance();
			int year = calendar.get(Calendar.YEAR);
			YearInfo yearInfo = yearInfoService.getByFirstYear(Long.valueOf(year));
			Date firstTermBeginDate = yearInfo.getFirstMondayOffirstTerm();
			Date secondTermBeginDate = yearInfo.getFirstMondayOfSecondTerm();
			if(calendar.getTime().after(firstTermBeginDate) && calendar.getTime().before(secondTermBeginDate)){ // 第一学期
				// 当前开课学期
				openCourseTermMap.put(year+"_01",year+"年第1学期");
				// 下个开课学期
				openCourseTermMap.put(year+"_02",year+"年第2学期");
			} else {// 第二学期
				// 当前开课学期
				openCourseTermMap.put(year+"_02",year+"年第2学期");
				// 下个开课学期
				openCourseTermMap.put((year+1)+"_01",(year+1)+"年第1学期");
			}
		} catch (Exception e) {
			logger.error("获取自动开课学期出错", e);
		}
		model.addAttribute("openCourseTermMap", openCourseTermMap);
		
		return "/edu3/teaching/teachingplancoursearrange/selectTerm";
	}
	
	/**
	 * 自动开课（默认开当前学期或下个学期的课程，注：相对当前时间）
	 * @param request
	 * @param response
	 * @param model
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/teachingplancoursestatus/autoOpenCourses.html")
	public void autoOpenCourses(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws WebException {
		
		Map<String, Object> returnInfo = new HashMap<String, Object>();
		String returnCode = "300";
		String message = "自动开课失败！";
		try {
			for(int k=0;k<1;k++){
				User user  = SpringSecurityHelper.getCurrentUser();
				// 校外学习中心单位类型编码
				String unitType = CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue();
				if(!user.getOrgUnit().getUnitType().equals(unitType)){
					returnCode = "300";
					message = "你不是该学校中心教务员，没有权限操作此功能！";
					break;
				}
				// 获取开课学期
				String terms = ExStringUtils.trim(request.getParameter("terms"));
				if(ExStringUtils.isEmpty(terms)) {
					returnCode = "300";
					message = "请选择开课学期！";
					break;
				}
				String[] openCourseTerm = terms.split(","); 
				// 要开课的年级及对应的建议学期
				Set<String> year_term = new HashSet<String>();
				for(String _term : openCourseTerm){
					String[] _yearTerm = _term.split("_0");
					int yearTemp = Integer.valueOf(_yearTerm[0]);
					int termTemp = Integer.valueOf(_yearTerm[1]);
					//TODO: 那个3是指学年，可以做成全局参数
					for(int i=0;i<3;i++){
						int _year = yearTemp - i;
						int term = termTemp+i*2;
						year_term.add(_year+"_"+term);
					}
				}
				
				// 从教学计划课程中获取所有符合条件的课程
				List<Map<String, Object>> openCourseInfoList = teachingJDBCService.findByYearAndTerm(year_term);
				// 处理自动开课逻辑
				Map<String,String> returnMap = teachingPlanCourseStatusService.handleAutoOpenCourses(openCourseInfoList,user,user.getOrgUnit());
				if(returnMap != null && returnMap.size() >0){
					returnCode = returnMap.get("returnCode");
					message = returnMap.get("message");
				}
			}
		} catch (Exception e) {
			logger.error("自动开课出错：", e);
			returnCode = "300";
			message = "自动开课失败！";
		} finally {
			returnInfo.put("returnCode", returnCode);
			returnInfo.put("message", message);
		}
		
		renderJson(response, JsonUtils.mapToJson(returnInfo));
	}
	
	/**
	 * 选择开课的教学点和开课学期
	 * @param termid
	 * @param resourceid
	 * @param guiplanid
	 * @param plancourseid
	 * @param isOpen
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/teachingplancoursestatus/select-schoolAndTerm.html")
	public String getOpenCourseInfo(String termid, String resourceid, String guiplanid, String plancourseid, String isOpen, HttpServletResponse response, ModelMap model) throws WebException {
		StringBuffer select  = new StringBuffer("<select  id='openOrAdjustCourse_term'  name='openOrAdjustCourse_term' style='width:50%;'>");		
		select.append("<option value=''>---请选择---</option>");
		try {
			// 可以选择的开课学期
			if(ExStringUtils.isNotBlank(plancourseid) && ExStringUtils.isNotBlank(guiplanid)){
				String[] gids = guiplanid.split("\\,");
				String[] pcids = plancourseid.split("\\,");
				for (int i = 0; i < pcids.length; i++) {
					if(ExStringUtils.isNotBlank(gids[i])){
						TeachingPlanCourse p_course 		= 		teachingPlanCourseService.get(pcids[i]);//教学计划课程
						TeachingPlan plan				= 		p_course.getTeachingPlan(); //教学计划
						TeachingGuidePlan p_grade 			= 		teachingGuidePlanService.get(gids[i]);//年级教学计划
						String			   edu_year			= 		plan.getEduYear(); //学制
						Double			   num				= 		Double.parseDouble(edu_year)*2; //循环次数
						Grade grade			= 		p_grade.getGrade();//年级
						//Long 			   hh 				= 		grade.getTerm().equals("1")?grade.getYearInfo().getFirstYear():grade.getYearInfo().getFirstYear()+1L;
						// 成教年度不跨年，因此第二学期的年度也不+1     --2016年9月13日 
						Long 			   hh 				= 		grade.getYearInfo().getFirstYear();
						List<Dictionary>   dict_tearm  		= 		CacheAppManager.getChildren("CodeCourseTermType");//从缓存中获取字典对象
						String str = "";
						for(int k = 0 ; k < num ; k ++){						
							if(null == str || "".equals(ExStringUtils.trim(str))){
								str = (hh+"_01");
							}else{
								if(ExStringUtils.trim(str).equals(hh+"_01")){
									str = (hh+"_02");
								}else if(ExStringUtils.trim(str).equals(hh+"_02")){
									hh++;
									str = (hh+"_01");
								}
							}
							for(Dictionary d : dict_tearm){
								String s=select.toString();
								
								if(ExStringUtils.trim(str).equals(ExStringUtils.trim(d.getDictValue()))&&!s.contains(d.getDictValue())){
									select.append("<option value='"+d.getDictValue()+"'>"+d.getDictName()+"</option>");
								}
							}
						}
					} 					
				}
			}
			select.append("</select>");
			
			User user = SpringSecurityHelper.getCurrentUser();
			String isBrschool = "N";
			String schoolId = "";
			String schoolName = "";
			if(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())){
				isBrschool = "Y";
				schoolId = user.getOrgUnit().getResourceid();
				schoolName = user.getOrgUnit().getUnitName();
			}
			
			model.addAttribute("resourceid", resourceid);
			model.addAttribute("guiplanid", guiplanid);
			model.addAttribute("plancourseid", plancourseid);
			model.addAttribute("termid", termid);
			model.addAttribute("isOpen", isOpen);
			model.addAttribute("openTerm", select.toString());
			model.addAttribute("isBrschool", isBrschool);
			model.addAttribute("schoolId", schoolId);
			model.addAttribute("schoolName", schoolName);
		} catch (Exception e) {
			logger.error("获取开课的教学点和开课学期选择框出错", e);
		}
       
		return "/edu3/teaching/teachingplancoursearrange/select-schoolAndTerm";
	}
	
	/**
	 * 开课/调整开课/取消开课
	 * @param resourceid
	 * @param guiplanid
	 * @param plancourseid
	 * @param isOpen
	 * @param response
	 * @throws WebException
	 * 
	 * TODO:排课逻辑出来后，要先删除教学任务书才能取消开课或调整开课
	 */
	@RequestMapping("/edu3/teaching/teachingplancoursestatus/saveOpenCourse.html")
	public void saveTeachingPlanCourseStatus(String termid, String resourceid, String term, String guiplanid, String plancourseid, String isOpen, String sureOpen, HttpServletRequest request, HttpServletResponse response) throws WebException {
		Map<String, Object> map = 	new HashMap<String, Object>();
		int statusCode = 200;
		String message = "";
		String schoolId = ExStringUtils.trimToEmpty(request.getParameter("schoolId"));
		String schoolName  = "";
		boolean isBrschool = true;
		OrgUnit unit	= 	null;
	   do{
	    	isOpen = ExStringUtils.defaultIfEmpty(isOpen, Constants.BOOLEAN_NO);
			String msg = (Constants.BOOLEAN_NO.equals(isOpen)?"取消":Constants.BOOLEAN_WAIT.equals(isOpen)?"调整":"")+"开课";
			if(Constants.BOOLEAN_YES.equals(isOpen)){
				if(ExStringUtils.isNotEmpty(schoolId)) {
					unit = orgUnitService.get(schoolId);
				}
				if(null == unit){
					statusCode = 300;
					message = "无法识别的教学点！";
					break;
				}
				schoolName  = unit.getUnitName(); //教学点名称
			}
			User user = SpringSecurityHelper.getCurrentUser();//当前登录用户
			if(!CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())){
				isBrschool = false;
			}
			try {			
				List<TeachingPlanCourseStatus> courseStatusList = new ArrayList<TeachingPlanCourseStatus>();
				List<TeachingPlanCourseTimetable> timetableList = new ArrayList<TeachingPlanCourseTimetable>();
				if(ExStringUtils.isNotBlank(plancourseid) && ExStringUtils.isNotBlank(guiplanid)){
					String[] res = resourceid.split("\\,");
					String[] gids = guiplanid.split("\\,");
					String[] pcids = plancourseid.split("\\,");
					String[] tids  = termid.split("\\,");
					for (int i = 0; i < pcids.length; i++) {
						TeachingPlanCourseStatus courseStatus = null;
						if((Constants.BOOLEAN_NO.equals(isOpen) || Constants.BOOLEAN_WAIT.equals(isOpen))){// 取消开课/调整开课
							if(ExStringUtils.isBlank(res[i])){
								continue;
							}
							courseStatus = teachingPlanCourseStatusService.get(ExStringUtils.trimToEmpty(res[i]));
							schoolId = courseStatus.getSchoolIds();
							schoolName = courseStatus.getSchoolName();
						} else if(Constants.BOOLEAN_YES.equals(isOpen)){// 开课
							if(ExStringUtils.isBlank(gids[i]) || ExStringUtils.isBlank(pcids[i])){
								continue;
							}
							List<TeachingPlanCourseStatus> _courseStatusList = teachingPlanCourseStatusService.findByCondition(gids[i], pcids[i], schoolId,"Y");
							// 防止重复开课
							if(_courseStatusList!=null && _courseStatusList.size()>0){
								continue;
							}
							if(res.length>0 && ExStringUtils.isNotBlank(res[i])){// 针对取消开课审核通过和开课审核不通过的情况
								courseStatus = teachingPlanCourseStatusService.get(ExStringUtils.trimToEmpty(res[i]));
								if(!schoolId.equals(courseStatus.getSchoolIds())){
									courseStatus = new TeachingPlanCourseStatus();
								}
							} else {
								courseStatus = new TeachingPlanCourseStatus();
							}
							courseStatus.setTeachingGuidePlan(teachingGuidePlanService.get(gids[i]));
							TeachingPlanCourse planCourse = teachingPlanCourseService.get(pcids[i]);
							courseStatus.setTeachingPlanCourse(planCourse);		
						} else {
							continue;
						}
						
						if(courseStatus != null){
							/** 不能对待审核的数据进行操作 **/
							if("openW".equals(courseStatus.getCheckStatus())|| "cancelW".equals(courseStatus.getCheckStatus())|| "updateW".equals(courseStatus.getCheckStatus())){
								statusCode = 300;
								message = "你选择的课程处于审核状态，不能进行此操作！";
								break;
							}
							/** 开课审核信息表 **/
							CheckOpenCourse coc = new CheckOpenCourse();
							/*CheckOpenCourse coc = null;
							List<CheckOpenCourse> listCoc =  checkOpenCourseService.findByHql(" from "+CheckOpenCourse.class.getSimpleName()+" where isDeleted = 0 and courseStatusId.resourceid = ? and applyName = ? and checkStatus = 'W' "
									, courseStatus.getResourceid(),user.getUsername());						
							if(null == listCoc || listCoc.size() <= 0 ){//开课
								 coc = new CheckOpenCourse();
							}else{// 取消开课或调整开课
								coc = listCoc.get(0);
							}*/
							// 处理上课学期		
							term =  (null == term || ExStringUtils.isBlank(term) ? "" : term); //要改变的学期
							String beforTerm 	=  (null == courseStatus.getTerm() ? "":courseStatus.getTerm()); //以前的学期，调整开课才用到
							TeachingGuidePlan p = courseStatus.getTeachingGuidePlan();//年级教学计划
							String planid	 = null == p.getTeachingPlan() ? "" : p.getTeachingPlan().getResourceid(); //教学计划id
							
							if(Constants.BOOLEAN_NO.equals(isOpen)){//取消开课
								//取消开课，只处理已经开课的信息
								if(!"Y".equals(ExStringUtils.trim(courseStatus.getIsOpen()))){
									map.put("statusCode", 300);
									map.put("message", "该课程还未开课，无法进行取消开课操作！");
									break;
								}
								/** 录入成绩后不能取消开课 **/					
								List<ExamResults> list = examResultsService.findByHql(" from "+ExamResults.class.getSimpleName()+" where isDeleted=0 and majorCourseId = ? and studentInfo.teachingPlan.resourceid = ? "
										+ "and studentInfo.branchSchool.resourceid = ? and studentInfo.grade.resourceid= ? "
										,null == courseStatus.getTeachingPlanCourse()?"":courseStatus.getTeachingPlanCourse().getResourceid(),planid,schoolId,p.getGrade().getResourceid() );								
								if(null != list && list.size() > 0){
									statusCode = 300;
									message = "该课程已录入分数，不允许取消开课！";
									break;
								}
								coc.setOperate("cancel"); //取消开课操作
								if(isBrschool){
									coc.setCheckStatus("W"); //待审核
									courseStatus.setCheckStatus("cancelW"); //取消开课待审核
								} else {
									coc.setCheckStatus("Y"); //待审核
									courseStatus.setCheckStatus("cancelY"); //取消开课待审核
									courseStatus.setIsOpen("N");
									courseStatus.setOpenStatus("N"); //开课状态
									courseStatus.setAfterterm(courseStatus.getTerm());
									courseStatus.setTerm(null);
									//（取消开课）要把排课信息也删除
									Map<String, Object> condition = new HashMap<String, Object>();
									condition.put("plancourseid", pcids[i]); 
									condition.put("gradeid", gids[i]);							
									List<TeachingPlanCourseTimetable> _timetableList = teachingPlanCourseTimetableService.findTeachingPlanCourseTimetableByCondition(condition);
									if(ExCollectionUtils.isNotEmpty(_timetableList)){
										timetableList.addAll(_timetableList);									
									}
									courseStatusList.add(courseStatus);
								}
							} else {
								if(Constants.BOOLEAN_WAIT.equals(isOpen)){
									//调整开课，只处理已经开课的信息
									if(!"Y".equals(ExStringUtils.trim(courseStatus.getIsOpen()))){
										map.put("statusCode", 300);
										map.put("message", "该课程还未开课，无法进行调整开课操作！");
										break;
									}
									/** 录入成绩后不能调整开课 **/	
									/** 2017-11-1 允许已录入成绩的课程调整开课，在开发使用菜单模块使用【更新成绩显示】功能可以更正正考考试批次 **/
									Map<String, Object> params = new HashMap<String, Object>();
									params.put("planCourse", null == courseStatus.getTeachingPlanCourse()?"":courseStatus.getTeachingPlanCourse().getResourceid());
									params.put("planId", planid);
									params.put("schoolId", schoolId);
									params.put("gradeId", p.getGrade().getResourceid());
									if(examResultsService.hasExamResults(params) && !"Y".equals(sureOpen)){
										statusCode = 400;
										message = "该课程已录入分数，确认要调整开课吗？";
										break;
									}
								}
								/** 如果所选的学期与教学计划的学期一致就不必审核 否则要审核 **/
								String _year = "";
								Long   year1 = 0L;
								Double year2 = 0.0;
								String _term = "";
								Double   _addTerm = (Double.parseDouble(tids[i])-1.0)/2;
								
								if( null != p && null != p.getGrade() && null != p.getGrade().getYearInfo() ){									
									year1 = p.getGrade().getYearInfo().getFirstYear();
									_term = p.getGrade().getTerm();
									if("2".equals(ExStringUtils.trim(_term))){
										_year = (year1+1) + ".5";
									}else if("1".equals(ExStringUtils.trim(_term))){
										_year = year1 + ".0";
									}	
									year2 = Double.parseDouble(_year)+_addTerm;
								}
								
								String term_1 = "0.0";
								if(ExStringUtils.isNotBlank(term)){
									String[] str = term.split("_");
									if(null != str && str.length >= 2){
										if("02".equals(ExStringUtils.trim(str[1]))){
											term_1 = ExStringUtils.trim(str[0])+".5";
										}else{
											term_1 = ExStringUtils.trim(str[0])+".0";
										}
									}
								}
								
								if(Constants.BOOLEAN_YES.equals(isOpen) && ExStringUtils.isBlank(courseStatus.getTerm())){ //开课
									if(!isBrschool) {
										coc.setCheckStatus("Y");//通过
										courseStatus.setOpenStatus("Y"); //开课状态
										courseStatus.setIsOpen("Y");//是否开课
										courseStatus.setTerm(term);//设置学期
										coc.setOperate("open"); //开课操作
										courseStatus.setCheckStatus("openY");//开课审核通过
										courseStatusList.add(courseStatus);
									} else {
										if(!ExStringUtils.trim(year2+"").equals(ExStringUtils.trim(term_1))){ //与教学计划学期不一致
											courseStatus.setCheckStatus("openW");//开课待审核
											coc.setCheckStatus("W");
											courseStatus.setOpenStatus("Y");
											coc.setOperate("open"); //开课操作
										}
										coc.setCheckStatus("Y");//通过
										courseStatus.setOpenStatus("Y"); //开课状态
										courseStatus.setIsOpen("Y");//是否开课
										courseStatus.setTerm(term);//设置学期
										coc.setOperate("open"); //开课操作
										courseStatus.setCheckStatus("openY");//开课审核通过
										courseStatusList.add(courseStatus);
									}
								}else if(Constants.BOOLEAN_WAIT.equals(isOpen)){ //调整开课
									if(ExStringUtils.trim(term).equals(ExStringUtils.trim(courseStatus.getTerm()))){ //所选学期和现在学期一致
										continue;
									}
									coc.setOperate("update"); //调整开课操作
									if(isBrschool){
										courseStatus.setCheckStatus("updateW");//开课待审核
										coc.setCheckStatus("W");						
									} else {
										courseStatus.setCheckStatus("updateY");//开课待审核
										courseStatus.setTerm(term);// 调整后的学期
										courseStatus.setOpenStatus("Y"); //开课状态
										courseStatus.setIsOpen("Y");//是否开课
										/*//同时更新教学计划课程
										String yearTerm = courseStatus.getTerm();
										String year = yearTerm.substring(0, 4);
										String termYear = yearTerm.substring(6, 7);
										long  tem = Long.parseLong(year) - courseStatus.getTeachingGuidePlan().getGrade().getYearInfo().getFirstYear();
										courseStatus.getTeachingPlanCourse().setTerm(String.valueOf(tem * 2 + Long.parseLong(termYear)));*/
										coc.setCheckStatus("Y");
										courseStatusList.add(courseStatus);
										String hql = "select er from "+ExamResults.class.getSimpleName()+" er ,"+StuChangeInfo.class.getSimpleName()+" stuc where er.studentInfo = stuc.studentInfo and er.majorCourseId=? and stuc.changeType='12' and er.isDeleted=0 and stuc.isDeleted=0 and stuc.changeBrschool.resourceid=? ";
										
										List<ExamResults> examList = examResultsService.findByHql(hql, new Object[]{courseStatus.getTeachingPlanCourse().getResourceid(),schoolId});
										//List<ExamResults> examList=examResultsService.findByCriteria(Restrictions.eq("isDeleted", 0),Restrictions.eq("majorCourseId", courseStatus.getTeachingPlanCourse().getResourceid()));
										if(examList!=null&&examList.size()>0){
											for(ExamResults er:examList){
												if(er.getStudentInfo().getBranchSchool().getResourceid().equals(schoolId)){
													er.getStudentInfo().setTeachingGuidePlan(courseStatus.getTeachingGuidePlan());
													stuchangeinfoservice.updateResumeSchoolResult_new(er.getStudentInfo(), courseStatus.getTeachingPlanCourse(),courseStatus);
												}
												
											}
										}
										
									}
								}
							}
							courseStatus.setSchoolName(schoolName);//教学点
							courseStatus.setSchoolIds(schoolId);//教学点id
							coc.setCourseStatusId(courseStatus); //年级教学计划课程开课对象
							coc.setUpdateTerm(term); //要调整的学期
							coc.setTerm(beforTerm); //之前的学期
							coc.setApplyName(user.getUsername());//申请人
							coc.setApplyTime(new Date());//申请时间
							coc.setApplyid(user.getResourceid());//申请人id
							teachingPlanCourseStatusService.saveOrUpdate(courseStatus); //修改或者添加 年级教学计划课程
							checkOpenCourseService.saveOrUpdate(coc); //修改或者添加	
							//面授类课程才设置问卷
							if("faceTeach".equalsIgnoreCase(courseStatus.getTeachType())){
								//调整开课，把学生问卷的学期也一起进行调整
								if(Constants.BOOLEAN_WAIT.equals(isOpen)){
									upateQuestionnaire(coc.getUpdateTerm(), courseStatus,coc.getTerm(),"update");
								}else if(Constants.BOOLEAN_NO.equals(isOpen)){//取消开课，把学生问卷也删除
									deleteQuestionnaire(coc.getTerm(), coc.getCourseStatusId(),"cancel");
								}
							}						
						}
					}
				}
				if(statusCode!=300 && statusCode!=400){					
					teachingPlanCourseStatusService.saveOrUpdateCourseStatusList(courseStatusList, timetableList);				
					if(map.get("statusCode")==null || (Integer)map.get("statusCode")!=300){
						statusCode = 200;
						message = msg+"成功";
					}
				}
			} catch (Exception e) {
				logger.error(msg+"失败:{}", e.fillInStackTrace());
				statusCode = 300;
				message = msg+"失败";
			}
	    }while(false);
	    map.put("statusCode", statusCode);
	    map.put("message", message);
		renderJson(response, JsonUtils.mapToJson(map));
	}

	/**
	 * @param courseStatusTerm
	 * @param courseStatus
	 * @throws Exception 
	 */
	public void deleteQuestionnaire(String courseStatusTerm,
									TeachingPlanCourseStatus courseStatus, String flag) throws Exception {
		Map<String, Object> condition1 = new HashMap<String, Object>();									
		condition1.put("courseStatusTerm", courseStatusTerm);
		String courseStatusid = courseStatus.getResourceid();
		condition1.put("courseStatusid", courseStatusid);
		condition1.put("courseType", courseStatus.getTeachType());
		condition1.put("flag", flag);
		//根据开课的resourceid 和 开课学期查找设置的任课老师等 相关信息
		List<Map<String,Object>> clList = courseStatusClService.findCourseTeacherCl(condition1);		
		if(clList!=null && clList.size()>0){
			for(Map<String,Object> cl:clList){
				Map<String, Object> condition2 = new HashMap<String, Object>();				
				String [] teacherid =cl.get("lecturerid").toString().split("\\,");
				condition2.put("teacherid", Arrays.asList(teacherid));										
				condition2.put("classesid", cl.get("classesid"));
				condition2.put("courseid", cl.get("courseid"));
				String tmpTerm = "";
				if("cancel".equalsIgnoreCase(flag)){//取消开课，term不为空，取term
					tmpTerm = cl.get("term").toString();
				}else{//更新开课或者开课，都是放在updateterm
					tmpTerm = cl.get("updateterm").toString();
				}
				condition2.put("firstYear",Long.parseLong(tmpTerm.substring(0,4)));										
				condition2.put("term",tmpTerm.substring(6,7));
				condition2.put("courseType", "faceTeach".equals(cl.get("teachType").toString()) ?"0":"1");
				iQuestionnaireService.batchDeleteQuestionnaire(condition2);
			}
		}
	}

	/**
	 * @param term 调整后的学期
	 * @param courseStatus
	 * @param beforTerm 调整前的学期
	 * @throws ServiceException
	 */
	public void upateQuestionnaire(String term, TeachingPlanCourseStatus courseStatus, String beforTerm, String flag)
			throws Exception {
		Map<String, Object> condition1 = new HashMap<String, Object>();	
		condition1.put("courseStatusTerm", beforTerm);//此处应传入调整前的学期
		Map<String, Object> condition3 = new HashMap<String, Object>();
		condition3.put("courseStatusTerm", term);//
		List<QuestionnaireBatch> qblistBefore= iQuestionnaireBatchService.findQuestionnaireBatchList(condition1);
		List<QuestionnaireBatch> qblistNow= iQuestionnaireBatchService.findQuestionnaireBatchList(condition3);
		if(qblistBefore!=null && qblistBefore.size()==1 && qblistNow!=null && qblistNow.size()==1){
			condition1.put("courseType", courseStatus.getTeachType());
			condition1.put("courseStatusid", courseStatus.getResourceid());
			condition1.put("flag", flag);
			List<Map<String, Object>> clList = courseStatusClService.findCourseTeacherCl(condition1);
			if(clList!=null && clList.size()>0){
				for(Map<String, Object> cl:clList){
					Map<String, Object> condition2 = new HashMap<String, Object>();
					String [] teacherid =cl.get("lecturerid").toString().split("\\,");
					condition2.put("teacherid", Arrays.asList(teacherid));	
					condition2.put("classesid", cl.get("classesid"));
					condition2.put("courseid", cl.get("courseid"));
					condition2.put("firstYear",Long.parseLong(cl.get("updateterm").toString().substring(0,4)));										
					condition2.put("term",cl.get("updateterm").toString().substring(6,7));
					condition2.put("courseType", "faceTeach".equals(cl.get("teachType").toString()) ?"0":"1");
					List<Questionnaire> qnlist = iQuestionnaireService.findQuestionnaireList(condition2);
					for(Questionnaire qn:qnlist){
						qn.setQuestionnaireBatch(qblistNow.get(0));
					}
					iQuestionnaireService.batchSaveOrUpdate(qnlist);
				}
			}
		}else if(qblistBefore!=null && qblistBefore.size()==1 && (qblistNow==null || qblistNow.size()==0)){
			//如果没有找到调整后的对应的年度、学期的问卷批次，则删除问卷
			deleteQuestionnaire(beforTerm, courseStatus,flag);
		}else if(qblistNow!=null && qblistNow.size()==1 && (qblistBefore==null || qblistBefore.size()==0)){
			//生成问卷
			Map<String, Object> condition4 = new HashMap<String, Object>();		
			condition4.put("courseType", courseStatus.getTeachType());
			condition4.put("courseStatusid", courseStatus.getResourceid());
			condition4.put("courseStatusTerm",term );
			condition4.put("flag", flag);
			List<Map<String, Object>> clList = courseStatusClService.findCourseTeacherCl(condition4);
			if(clList!=null && clList.size()>0){
				for(Map<String, Object> cl:clList){
					createQuestionnaire(courseStatus,courseStatusClService.get(cl.get("resourceid").toString()),"create");
				}
			}
			
		}
			
	}
	/**
	 * 考勤表预览
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/teaching/plancourseArrange/studentAttendance-print-view.html")
	public String printStudentAttendanceView(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		String resIds = ExStringUtils.trimToEmpty(request.getParameter("resIds"));
		String classesIds = ExStringUtils.trimToEmpty(request.getParameter("classesIds"));
		String courseIds = ExStringUtils.trimToEmpty(request.getParameter("courseIds"));
		String unitIds = ExStringUtils.trimToEmpty(request.getParameter("unitIds"));
		String terms = ExStringUtils.trimToEmpty(request.getParameter("terms"));
		String pcIds = ExStringUtils.trimToEmpty(request.getParameter("pcIds"));
		model.addAttribute("resIds", resIds);
		model.addAttribute("classesIds", classesIds);
		model.addAttribute("courseIds", courseIds);
		model.addAttribute("unitIds", unitIds);
		model.addAttribute("terms", terms);
		model.addAttribute("pcIds", pcIds);
		return "/edu3/teaching/teachingplancoursearrange/courseArrange-StudentAttendance-printview";
	}
	@RequestMapping("/edu3/teaching/plancourseArrange/studentAttendance-print.html")
	public void printStudentAttendance(HttpServletRequest request, HttpServletResponse response)throws WebException {
		String resIds = ExStringUtils.trimToEmpty(request.getParameter("resIds"));
		String classesIds = ExStringUtils.trimToEmpty(request.getParameter("classesIds"));
		String courseIds = ExStringUtils.trimToEmpty(request.getParameter("courseIds"));
		String unitIds = ExStringUtils.trimToEmpty(request.getParameter("unitIds"));
		String terms = ExStringUtils.trimToEmpty(request.getParameter("terms"));
		String pcIds = ExStringUtils.trimToEmpty(request.getParameter("pcIds"));
		Map<String,Object> condition = new HashMap<String, Object>();
		if(ExStringUtils.isNotBlank(resIds)) {
			condition.put("resIds", resIds);
		}
		if(ExStringUtils.isNotBlank(classesIds)) {
			condition.put("classesIds", classesIds);
		}
		if(ExStringUtils.isNotBlank(courseIds)) {
			condition.put("courseIds", courseIds);
		}
		if(ExStringUtils.isNotBlank(unitIds)) {
			condition.put("unitIds", unitIds);
		}
		if(ExStringUtils.isNotBlank(terms)) {
			condition.put("terms", terms);
		}
		String [] classesid = classesIds.split("\\,");
		//List<Map<String,Object>> dataList = new ArrayList<Map<String,Object>>();
		int row = 30;
		List<List<StudentSignatureVo>> datalists = new ArrayList<List<StudentSignatureVo>>();
		List<Map<String, Object>> volist = new ArrayList<Map<String,Object>>();
		String [] courseid = courseIds.split("\\,");
		String [] plancourseid =pcIds.split("\\,");
		String [] term = terms.split("\\,");
		
		if(classesid!=null&&classesid.length>0){
			
			for(int n=0;n<classesid.length;n++){				
				Course course = courseService.get(Serializable.class.cast(courseid[n]));
				TeachingPlanCourse tpc = teachingPlanCourseService.get(plancourseid[n]);
				//List<StudentInfo> list = studentInfoService.findByCriteria(Restrictions.eq("isDeleted", 0),Restrictions.eq("classes.resourceid", classesid[n]));
				String hql = " from "+StudentInfo.class.getSimpleName()+" stu where stu.studentStatus='11' and stu.isDeleted=0 and stu.classes.resourceid=? order by stu.studyNo  ";
				List<StudentInfo> list = studentInfoService.findByHql(hql,classesid[n]);
				int size = list.size();
				int pageNum =getPageNum(size,row);
				for(int i=0;i<pageNum;i++){
					Map<String, Object> map = new HashMap<String, Object>();
					List<StudentSignatureVo> datalist = tableInit(row);
					for(int j=0;j<datalist.size();j++){
						if(size>j+i*row*2){
							datalist.get(j).setStudyNo1(list.get(j+i*row*2).getStudyNo());
							datalist.get(j).setStudentName1(list.get(j+i*row*2).getStudentName());
						}
						if(size>j+row+i*row*2){
							datalist.get(j).setStudyNo2(list.get(j+row+i*row*2).getStudyNo());
							datalist.get(j).setStudentName2(list.get(j+row+i*row*2).getStudentName());
						}
					}
					//datalists.add(datalist);
					map.put("datalist", datalist);
					map.put("courseName", course.getCourseName());
					map.put("gradeName", list.get(0).getGrade().getGradeName());
					map.put("unitName",list.get(0).getBranchSchool().getUnitName() );
					map.put("classicName", list.get(0).getClassic().getClassicName());
					map.put("classesName", list.get(0).getClasses().getClassname());
					map.put("teachingType", JstlCustomFunction.dictionaryCode2Value("CodeTeachingType",list.get(0).getTeachingType()));
					map.put("facestudyhour", tpc.getFaceStudyHour().toString());
					map.put("year",replaceYear(term[n]));
					map.put("term",replaceTerm( term[n]));
					volist.add(map);
				}
			}
		}
		
		
		String reportPath = File.separator+"reports"+File.separator+"studentinfo"+File.separator+"studentAttendance.jasper";
		// 报表文件
		try {
			String reprotFile = URLDecoder.decode(request.getSession().getServletContext().getRealPath(reportPath),"utf-8");
			// logo路径
			String logoPath = CacheAppManager.getSysConfigurationByCode("web.scutlogo.path").getParamValue();
			
			JasperPrint jasperPrint = null; // 打印
			for(int i=0;i<volist.size();i++){
				Map<String, Object> tmplist = volist.get(i);
				List<StudentSignatureVo> datalist = (List<StudentSignatureVo>) volist.get(i).get("datalist");
				JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(datalist);
				Map<String, Object> printParam = new HashMap<String, Object>();	
				printParam.put("logoPath", logoPath);			
				printParam.put("courseName", tmplist.get("courseName"));
				printParam.put("gradeName", tmplist.get("gradeName"));
				printParam.put("unitName", tmplist.get("unitName"));
				printParam.put("classicName", tmplist.get("classicName"));
				printParam.put("classesName", tmplist.get("classesName"));
				printParam.put("teachingType", tmplist.get("teachingType"));
				printParam.put("facestudyhour", tmplist.get("facestudyhour"));
				printParam.put("year", tmplist.get("year"));
				printParam.put("term", tmplist.get("term"));
				String printDate = ExDateUtils.getCurrentDate();
				printParam.put("printDate", printDate);
				if (null!=jasperPrint&&jasperPrint.getPages().size()>0){
					JasperPrint jasperPage = JasperFillManager.fillReport(reprotFile, printParam, dataSource);
					List jsperPageList=jasperPage.getPages();
					for (int j = 0; j < jsperPageList.size(); j++) {
						jasperPrint.addPage((JRPrintPage) jsperPageList.get(j));
					}
					jasperPage = null;//清除临时报表的内存占用
				}
				else {
					jasperPrint = JasperFillManager.fillReport(reprotFile, printParam, dataSource); // 填充报表
				}
			}			
			if (null!=jasperPrint) {
				renderStream(response, jasperPrint);
			}else {
				renderHtml(response,"缺少打印数据！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	public List<StudentSignatureVo> tableInit(int row){
		List<StudentSignatureVo> list = new ArrayList<StudentSignatureVo>();
		for(int i=0;i<row;i++){
			StudentSignatureVo tmp = new StudentSignatureVo("","");
			list.add(tmp);
		}
		return list;
	}
	public int getPageNum(int size,int row){
		int pageNum=1;
		int divisor = size/(row*2);
		int remainder=size%(row*2);
		if(divisor>0){
			pageNum=pageNum+divisor;
			if(remainder==0){
				pageNum--;
			}
		}
		return pageNum;
	}
	public String replaceYear(String term){
		String currentyear = term.substring(0,4);
		int nextyear = Integer.parseInt(currentyear)+1;
		String year=currentyear+"-"+nextyear+"学年";
		return year;
	}
	public String replaceTerm(String term){
		term = term.substring(6,7);
		String termcn=JstlCustomFunction.dictionaryCode2Value("CodeTermType", term);
		return termcn;
	}
	
	/**
	 * 选择课程教学类型页面
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/teachingplancoursestatus/setCourseTeachType-form.html")
	public String setCourseTeachTypeForm(HttpServletRequest request, HttpServletResponse response, ModelMap model)throws WebException {
		String resourceids = request.getParameter("resourceids");
		model.addAttribute("resourceids", resourceids);
		
		return "/edu3/teaching/teachingplancoursearrange/setCourceTeachType";
	}
	
	/**
	 * 设置课程教学类型
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/teachingplancoursestatus/setCourseTeachType.html")
	public void setCourseTeachType(HttpServletRequest request, HttpServletResponse response, ModelMap model)throws WebException {
		Map<String, Object> map = new HashMap<String, Object>();
		String resourceids = request.getParameter("resourceids");
		String courseTeachType = request.getParameter("courseTeachType");
		int returnCode = 200;
		String message = "设置课程教学类型成功";
		try {
			do {
				if (ExStringUtils.isEmpty(resourceids)) {
					returnCode = 300;
					message = "请重新选择开课记录！";
				}
				if (ExStringUtils.isEmpty(courseTeachType)) {
					returnCode = 300;
					message = "请重新课程教学类型！";
				}
				// 设置课程教学类型逻辑
				
				teachingPlanCourseStatusService.setCourseTeachType(courseTeachType, resourceids);
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "4", UserOperationLogs.UPDATE,"更新教学类型：参数："+JsonUtils.mapToJson(Condition2SQLHelper.getConditionFromResquestByIterator(request)));
//				if(courseTeachType.equalsIgnoreCase("faceTeach")){//课程类型为面授类设置时设置问卷
					iQuestionnaireService.updateQuestionnaire(resourceids,courseTeachType);
//				}
			} while (false);
		} catch (Exception e) {
			logger.error("设置课程教学类型出错", e);
			returnCode = 300;
			message = "设置课程教学类型失败";
		} finally {
			map.put("returnCode", returnCode);
			map.put("message", message);
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 选择课程考试形式页面
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/teachingplancoursestatus/setCourseExamForm-form.html")
	public String setCourseExamForm(HttpServletRequest request, HttpServletResponse response, ModelMap model)throws WebException {
		String resourceids = request.getParameter("resourceids");
		model.addAttribute("resourceids", resourceids);
		
		return "/edu3/teaching/teachingplancoursearrange/setCourseExamForm";
	}
	
	/**
	 * 设置课程考试形式
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/teachingplancoursestatus/setCourseExamForm.html")
	public void saveCourseExamForm(HttpServletRequest request, HttpServletResponse response, ModelMap model)throws WebException {
		Map<String, Object> map = new HashMap<String, Object>();
		String resourceids = request.getParameter("resourceids");
		String courseExamForm = request.getParameter("courseExamForm");
		int returnCode = 200;
		String message = "设置课程考试形式成功";
		try {
			do {
				if (ExStringUtils.isEmpty(resourceids)) {
					returnCode = 300;
					message = "请重新选择开课记录！";
				}
				if (ExStringUtils.isEmpty(courseExamForm)) {
					returnCode = 300;
					message = "请重新课程考试形式！";
				}
				// 设置课程考试形式逻辑
				
				teachingPlanCourseStatusService.saveCourseExamForm(courseExamForm, resourceids);
			} while (false);
		} catch (Exception e) {
			logger.error("设置课程考试形式出错", e);
			returnCode = 300;
			message = "设置课程考试形式失败";
		} finally {
			map.put("returnCode", returnCode);
			map.put("message", message);
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	@RequestMapping("/edu3/teaching/teachingplancoursestatus/teachingPlanCourseStatus-export.html")
	public void teachingPlanCourseStatusExport(HttpServletRequest request, ModelMap model, HttpServletResponse response) throws WebException {
		String courseId = ExStringUtils.trimToEmpty(request.getParameter("courseId"));
		String majorid=ExStringUtils.trimToEmpty(request.getParameter("majorid"));
		String brSchoolid=ExStringUtils.trimToEmpty(request.getParameter("brSchoolid"));
		String gradeid=ExStringUtils.trimToEmpty(request.getParameter("gradeid"));
		String classicid=ExStringUtils.trimToEmpty(request.getParameter("classicid"));
		String teachingType=ExStringUtils.trimToEmpty(request.getParameter("teachingType"));
		String term=ExStringUtils.trimToEmpty(request.getParameter("term"));
		
		String isOpenStatus = request.getParameter("isOpenStatus");
		String isOpenCourse = request.getParameter("isOpenCourse");
		String teachType = request.getParameter("teachType");
//		String classesid=ExStringUtils.trimToEmpty(request.getParameter("classesid"));
		String optype="status";
		String teachername = ExStringUtils.trim(request.getParameter("teachername"));
		String lecturer = ExStringUtils.trim(request.getParameter("lecturer"));
		String examSubId = ExStringUtils.trim(request.getParameter("examSubId"));
		String ischecked = ExStringUtils.trim(request.getParameter("ischecked"));
		
		Map<String, Object> condition = new HashMap<String, Object>();
		User user = SpringSecurityHelper.getCurrentUser();
		if(user.getOrgUnit().getUnitType().equals(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())){//如果为校外学习中心人员
			brSchoolid = ExStringUtils.trim(user.getOrgUnit().getResourceid());
			condition.put("isBrschool", Constants.BOOLEAN_YES);
			model.addAttribute("isBrschool", true); 
			model.addAttribute("school",brSchoolid);
			model.addAttribute("schoolname",ExStringUtils.trim(user.getOrgUnit().getUnitName()));
			condition.put("schoolname", ExStringUtils.trim(user.getOrgUnit().getUnitName()));
		}
		String orgterm = request.getParameter("orgterm");
//		if(ExStringUtils.isNotBlank(showList)) condition.put("showList", showList);
//		if(ExStringUtils.isNotBlank(status)) condition.put("status", status);
		if(ExStringUtils.isNotBlank(gradeid)) {
			condition.put("gradeid", gradeid);
		}
		if(ExStringUtils.isNotBlank(courseId)) {
			condition.put("courseId", courseId);
		}
		if(ExStringUtils.isNotBlank(majorid)) {
			condition.put("majorid", majorid);
			condition.put("major", majorid);
		}
		if(ExStringUtils.isNotBlank(classicid)) {
			condition.put("classicid", classicid);
		}
		if(ExStringUtils.isNotBlank(teachingType)) {
			condition.put("teachingType", teachingType);
		}
		if(ExStringUtils.isNotBlank(brSchoolid)) {
			condition.put("brSchoolid", brSchoolid);
		}
		if(ExStringUtils.isNotBlank(term)) {
			condition.put("term", term);//开课学期
		}
		if(ExStringUtils.isNotBlank(orgterm)) {
			condition.put("orgterm", orgterm);//原计划学期
		}
//		if(ExStringUtils.isNotBlank(classesid)) condition.put("classesid", classesid);
		if(ExStringUtils.isNotBlank(teachername)) {
			condition.put("teachername", teachername);
		}
		if(ExStringUtils.isNotBlank(isOpenStatus)) {
			condition.put("isOpenStatus", isOpenStatus);	 //开课状态
		}
		if(ExStringUtils.isNotBlank(isOpenCourse)) {
			condition.put("isOpenCourse", isOpenCourse);	 //是否开课
		}
		if(ExStringUtils.isNotBlank(lecturer)) {
			condition.put("lecturer", lecturer);	//讲课老师名字模糊查询
		}
		if(ExStringUtils.isNotBlank(teachType)) {
			condition.put("teachType", teachType);//增加课程类型
		}
		
		String checkedids = ExStringUtils.trim(request.getParameter("ids"));
		String plancourseids = ExStringUtils.trim(request.getParameter("plancourseids"));
		if(ExStringUtils.isNotBlank(ischecked)&& "true".equals(ischecked)){
			String guiplanids = ExStringUtils.trim(request.getParameter("guiplanids"));
			String planids = ExStringUtils.trim(request.getParameter("planids"));
			if (ExStringUtils.isNotBlank(checkedids)) {
				String resourceids="'";
				resourceids +=checkedids.replace(",", "','");
				resourceids+="'";
				condition.put("resourceids",resourceids);
			}
			if (ExStringUtils.isNotBlank(plancourseids)) {
				String plancourseidsVo="'";
				plancourseidsVo +=plancourseids.replace(",", "','");
				plancourseidsVo+="'";
				condition.put("plancourseidsVo",plancourseidsVo);
			}
			if (ExStringUtils.isNotBlank(guiplanids)) {
				String guiplanidsVo="'";
				guiplanidsVo +=guiplanids.replace(",", "','");
				guiplanidsVo+="'";
				condition.put("guiplanidsVo",guiplanidsVo);
			}
			if (ExStringUtils.isNotBlank(planids)) {
				String planidsVo="'";
				planidsVo +=planids.replace(",", "','");
				planidsVo+="'";
				condition.put("planidsVo",planidsVo);
			}
		}
		
		//考试批次ID
		if(ExStringUtils.isNotBlank(examSubId)){
			condition.put("examSubId", examSubId);
			ExamSub examSub = examSubService.get(examSubId);
			//用简单的方式判断正考逻辑
			if(examSub != null && "N".equals(examSub.getExamType())){
				condition.put("isFinalExam", "Y");
			}else{
				condition.put("isFinalExam", "N");
			}
		}
//		if("timetable".equals(optype)) condition.put("isOpen", Constants.BOOLEAN_YES);
		condition.put("optype", optype);
//		Long start2 = System.currentTimeMillis();
		List<Map<String, Object>> courseList = teachingPlanCourseStatusService.findTeachingPlanCourseStatusMapByConditionNew(condition);
		if (courseList!=null&&courseList.size()>0) {
			//导出
			try {
				//如果是勾选的就根据专业排序
				if(ExStringUtils.isNotBlank(checkedids)||ExStringUtils.isNotBlank(plancourseids)){
					Collections.sort(courseList, new  Comparator<Map<String, Object>>() {
						@Override
						public int compare(Map<String, Object> arg0,
								Map<String, Object> arg1) {
							return ((String) arg0.get("majorName")).compareTo((String)arg1.get("majorName"));
						}
					});
				}
				
				for (int i = 0; i < courseList.size(); i++) {
					String firstYear=courseList.get(i).get("firstYear")+"";
					String courseterm=courseList.get(i).get("courseterm")+"";
					String isopenString=courseList.get(i).get("isopen")+"";
					if (ExStringUtils.isNotBlank(firstYear)&&ExStringUtils.isNotBlank(courseterm)) {
						try {
						courseList.get(i).put("nowterm", Tools.getDigitalTerm(firstYear, courseterm));
						} catch (Exception e) {
						
						}
					}
					if (ExStringUtils.isNotBlank(isopenString)&& "Y".equals(isopenString)) {
						courseList.get(i).put("isopenString","已开课");
					}else{
						courseList.get(i).put("isopenString","未开课");
					}
				}
				File excelFile = null;
				GUIDUtils.init();
				File disFile = new File(getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(false) + ".xls");
				List<String> dictCodeList = new ArrayList<String>();
				dictCodeList.add("CodeTeachingType");
				dictCodeList.add("CodeTermType");
				dictCodeList.add("CodeCourseTermType");
				dictCodeList.add("CodeCourseTeachType");
				dictCodeList.add("CodeCourseExamForm");
				dictCodeList.add("CodeOpenCourseStatus");
				Map<String , Object> map = dictionaryService.getDictionByMap(dictCodeList, true, IDictionaryService.PREKEY_TYPE_BYCODE);
				exportExcelService.initParmasByfile(disFile, "teaching_planCourse_status_export", courseList,map);
				
//				exportExcelService.initParmasByfile(disFile, "teaching_planCourse_status_export", list,map,filterColumnList);
				exportExcelService.getModelToExcel().setHeader("开课信息表");//设置大标题
				exportExcelService.getModelToExcel().setRowHeight(500);//设置行高
				WritableFont font = new WritableFont(WritableFont.ARIAL, 10);
				font.setBoldStyle(WritableFont.BOLD);
		        WritableCellFormat format = new WritableCellFormat(font);
				format.setAlignment(Alignment.CENTRE);
		        format.setBackground(Colour.YELLOW);
		        format.setBorder(Border.ALL, BorderLineStyle.THIN);
				exportExcelService.getModelToExcel().setTitleCellFormat(format);
				exportExcelService.getModelToExcel().setRowHeight(300);
				excelFile 	= exportExcelService.getExcelFile();//获取导出的文件
				
				logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());
				downloadFile(response,"开课信息表"+".xls", excelFile.getAbsolutePath(),true);
				
			}catch (Exception e) {
				logger.error("导出excel文件出错："+e.fillInStackTrace());
				renderHtml(response, "导出excel文件出错:"+e.getMessage());
			}
			
		}else{
			renderHtml(response, "<script>alert('没有数据')</script>");
		}
	}

}
