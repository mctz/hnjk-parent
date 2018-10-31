package com.hnjk.edu.teaching.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.GUIDUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.FileUploadAndDownloadSupportController;
import com.hnjk.core.support.context.Constants;
import com.hnjk.core.support.context.SystemContextHolder;
import com.hnjk.edu.basedata.model.YearInfo;
import com.hnjk.edu.basedata.service.IYearInfoService;
import com.hnjk.edu.learning.model.StudentLearnPlan;
import com.hnjk.edu.learning.service.IStudentLearnPlanService;
import com.hnjk.edu.learning.vo.LearningPlanVo;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.roll.service.IStudentInfoService;
import com.hnjk.edu.teaching.model.CourseOrder;
import com.hnjk.edu.teaching.model.ExamResults;
import com.hnjk.edu.teaching.model.ExamSub;
import com.hnjk.edu.teaching.model.OrderCourseSetting;
import com.hnjk.edu.teaching.model.TeachingGuidePlan;
import com.hnjk.edu.teaching.model.TeachingPlan;
import com.hnjk.edu.teaching.model.TeachingPlanCourse;
import com.hnjk.edu.teaching.service.ICourseOrderService;
import com.hnjk.edu.teaching.service.ICourseService;
import com.hnjk.edu.teaching.service.IExamResultsService;
import com.hnjk.edu.teaching.service.IExamSubService;
import com.hnjk.edu.teaching.service.IOrderCourseSettingService;
import com.hnjk.edu.teaching.service.ITeachingGuidePlanService;
import com.hnjk.edu.teaching.service.ITeachingPlanCourseService;
import com.hnjk.edu.teaching.service.ITeachingPlanService;
import com.hnjk.edu.teaching.vo.CourseOrderSearchVo;
import com.hnjk.edu.teaching.vo.OrderCourseSettingFormVo;
import com.hnjk.extend.plugin.excel.service.IExportExcelService;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.taglib.JstlCustomFunction;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.OrgUnit;
import com.hnjk.security.model.User;
import com.hnjk.security.service.IOrgUnitService;
/**
 * 
 * <code>预约学习管理Controller</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-8-10 下午04:00:08
 * @see 
 * @version 1.0
 */
@Controller
public class CourseOrderController extends FileUploadAndDownloadSupportController{

	private static final long serialVersionUID = -949513054793690011L;

	@Autowired
	@Qualifier("studentinfoservice")
	private IStudentInfoService studentInfoService;
	
	@Autowired
	@Qualifier("courseOrderService")
	private ICourseOrderService courseOrderService;
	
	@Autowired
	@Qualifier("orderCourseSettingService")
	private IOrderCourseSettingService orderCourseSettingService;
	
	@Autowired
	@Qualifier("teachingplanservice")
	private ITeachingPlanService teachingPlanService;
	
	@Autowired
	@Qualifier("teachingguideplanservice")
	private ITeachingGuidePlanService teachingguideplanservice;
	
	@Autowired
	@Qualifier("studentLearnPlanService")
	private IStudentLearnPlanService studentLearnPlanService;
	
	@Autowired
	@Qualifier("teachingPlanCourseService")
	private ITeachingPlanCourseService teachingPlanCourseService;//注入教学计划课程服务
	
//	@Autowired
//	@Qualifier("teachingJDBCService")
//	private ITeachingJDBCService teachingJDBCService;
	
	@Autowired
	@Qualifier("examResultsService")
	private IExamResultsService examResultsService;
	
	@Autowired
	@Qualifier("courseService")
	private ICourseService courseService;
	
	@Autowired
	@Qualifier("examSubService")
	private IExamSubService examSubService;
	
	@Autowired
	@Qualifier("exportExcelService")
	private IExportExcelService exportExcelService;

	@Autowired
	@Qualifier("orgUnitService")
	private IOrgUnitService orgUnitService;
	
	@Autowired
	@Qualifier("yearInfoService")
	private IYearInfoService yearInfoService;
	/**
	 * 年度预约-列表
	 * @param vo
	 * @param request
	 * @param response
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/courseorder/gradelearnbook-list.html")
	public String gradeBookingList(OrderCourseSettingFormVo vo,HttpServletRequest request,HttpServletResponse response,Page objPage,ModelMap model)throws WebException{
		
		Map<String,Object> condition = new HashMap<String,Object>();
		
		if(null!=vo && ExStringUtils.isNotEmpty(vo.getIsOpened())) {
			condition.put("isOpened", vo.getIsOpened());
		}
		if(null!=vo &&ExStringUtils.isNotEmpty(vo.getEndDate())) {
			condition.put("endDate",vo.getEndDate());
		}
		if(null!=vo &&ExStringUtils.isNotEmpty(vo.getStartDate())) {
			condition.put("startDate",vo.getStartDate());
		}
		if(null!=vo &&ExStringUtils.isNotEmpty(vo.getYearInfo())) {
			condition.put("yearInfo", vo.getYearInfo());
		}
		if(null!=vo &&ExStringUtils.isNotEmpty(vo.getTerm())) {
			condition.put("term", vo.getTerm());
		}
		
		Page page = orderCourseSettingService.findOrderCourseSettingByCondition(condition, objPage);
		model.addAttribute("condition",condition);
		model.addAttribute("orderCourseSetting", page);
		
		return"/edu3/teaching/courseorder/gradeCourseOrder-list";
	}
	/**
	 * 年级预约-编辑
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/courseorder/gradelearnbook-edit.html")
	public String gradeBookIngEdit(OrderCourseSettingFormVo vo,HttpServletRequest request,HttpServletResponse response, ModelMap model)throws WebException{
		if (null!=vo.getResourceid()&&!"".equals(vo.getResourceid())) {
			OrderCourseSetting gradeSetting = orderCourseSettingService.get(vo.getResourceid());
			model.addAttribute("setting",gradeSetting);
		}
		return"/edu3/teaching/courseorder/gradeCourseOrder-form";
	}
	/**
	 * 年级预约-保存
	 * @param vo
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/teaching/courseorder/gradelearnbook-save.html")
	public void gradeBookSave(OrderCourseSettingFormVo vo,HttpServletRequest request,HttpServletResponse response){
		
		Map<String, Object> map = new HashMap<String, Object>();
		String msg              = "";
		boolean isAllowSave     = true;
		try {
			List<OrderCourseSetting> list = orderCourseSettingService.findOpenedSetting();
			if (ExStringUtils.isNotEmpty(vo.getResourceid())) {//修改
				if (null!=list&&list.size()>0&& !vo.getResourceid().equals(list.get(0).getResourceid())) {
					isAllowSave = false;
					msg = "同一时间只允许开放一个年度的预约权限！";
					map.put("statusCode", 300);
					map.put("message",msg);
				}
			}else {//新增
				if (null!=list&&!list.isEmpty()&&Constants.BOOLEAN_YES.equals(vo.getIsOpened())) {
					isAllowSave = false;
					msg = "同一时间只允许开放一个年度的预约权限！";
					map.put("statusCode", 300);
					map.put("message",msg);
				}
			}
			
			if (isAllowSave) {
				Map resultMap  = orderCourseSettingService.saveOrUpdateOrderCourseSetting(vo);
				boolean result = (Boolean)resultMap.get("result");
				if(resultMap.containsKey("msg")) {
					msg = (String)resultMap.get("msg");
				}
				if (result) {
					OrderCourseSetting gradeSetting = (OrderCourseSetting) resultMap.get("setting");
					msg = "操作成功！";
					map.put("statusCode", 200);
					map.put("message", msg);
					map.put("reloadUrl", request.getContextPath() +"/edu3/teaching/courseorder/gradelearnbook-edit.html?resourceid="+gradeSetting.getResourceid());
					
				}else {
					map.put("statusCode", 300);
					map.put("message","操作失败:"+msg);
				}
			}
			
		} catch (Exception e) {
			logger.error("保存年级预约权限错误:{}"+e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败:"+e.getMessage());
		}
		
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 年级预约-删除
	 * @param resourceIds
	 * @param response
	 */
	@RequestMapping("/edu3/teaching/courseorder/gradelearnbook-del.html")
	public void gradeBookDel(String resourceIds,HttpServletResponse response){
		String msg = "删除失败！";
		try {
			if (ExStringUtils.isNotEmpty(resourceIds)) {
				String []ids =resourceIds.split(",");
				for (int i = 0; i < ids.length; i++) {
					OrderCourseSetting gradeSetting = orderCourseSettingService.get(ids[i]);
					if (null!=gradeSetting) {
						gradeSetting.setIsDeleted(1);
						orderCourseSettingService.saveOrUpdate(gradeSetting);
						msg = "删除成功！";
					}else {
						msg="操作失败!";
						break;
					}
					
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg="删除失败！";
		}
		renderJson(response,JsonUtils.objectToJson(msg));	
	}
	/**
	 * 个人预约学习\考试权限-列表
	 * @param vo
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/courseorder/personallearnbook-list.html")
	public String personalBookingList(CourseOrderSearchVo vo, Page objPage, ModelMap model)throws WebException{
		
		objPage.setOrderBy("resourceid");
		objPage.setOrder(Page.ASC);//设置默认排序方式
		//查询条件
		Map<String,Object> condition = new HashMap<String,Object>();
		if(ExStringUtils.isNotEmpty(vo.getBranchSchool())) {
			condition.put("branchSchool", vo.getBranchSchool());
		}
		if(ExStringUtils.isNotEmpty(vo.getMajor())) {
			condition.put("major", vo.getMajor());
		}
		if(ExStringUtils.isNotEmpty(vo.getClassic())) {
			condition.put("classic",vo.getClassic());
		}
		if(ExStringUtils.isNotEmpty(vo.getName())) {
			condition.put("name",  vo.getName());
		}
		if(ExStringUtils.isNotEmpty(vo.getMatriculateNoticeNo())) {
			condition.put("matriculateNoticeNo", vo.getMatriculateNoticeNo());
		}
		if(ExStringUtils.isNotEmpty(vo.getGradeid())) {
			condition.put("gradeid", vo.getGradeid());
		}
		if(ExStringUtils.isNotEmpty(vo.getOrderCourseStatus())) {
			condition.put("orderCourseStatus", vo.getOrderCourseStatus());
		}
		if ("1".equals(vo.getConfigStatus())&&ExStringUtils.isNotEmpty(vo.getOrderCourseStatus())) {
			condition.put("orderCourseStatusFlag",vo.getOrderCourseStatus());
		}
		if ("2".equals(vo.getConfigStatus())&&ExStringUtils.isNotEmpty(vo.getOrderCourseStatus())) {
			condition.put("orderExamStatusFlag",vo.getOrderCourseStatus());
		}
		if(ExStringUtils.isNotEmpty(vo.getConfigStatus())) {
			condition.put("configStatus",vo.getConfigStatus());
		}
		
		Page page = studentInfoService.findStuByCondition(condition, objPage);
		
		model.addAttribute("stulist", page);
		model.addAttribute("condition", condition);
		
		return"/edu3/teaching/courseorder/personalCourseOrder-list";
	}
	
	/**
	 * 个人预约毕业论文预约权限-列表
	 * @param vo
	 * @param objPage
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/courseorder/personal-graduatepaper-bookstatus-list.html")
	public String personalGraduatePaperBookStatusList(CourseOrderSearchVo vo, Page objPage, ModelMap model)throws WebException{
		
		objPage.setOrderBy("resourceid");
		objPage.setOrder(Page.ASC);//设置默认排序方式
		//查询条件
		Map<String,Object> condition = new HashMap<String,Object>();
		if(ExStringUtils.isNotEmpty(vo.getBranchSchool())) {
			condition.put("branchSchool", vo.getBranchSchool());
		}
		if(ExStringUtils.isNotEmpty(vo.getMajor())) {
			condition.put("major", vo.getMajor());
		}
		if(ExStringUtils.isNotEmpty(vo.getClassic())) {
			condition.put("classic",vo.getClassic());
		}
		if(ExStringUtils.isNotEmpty(vo.getName())) {
			condition.put("name",  vo.getName());
		}
		if(ExStringUtils.isNotEmpty(vo.getMatriculateNoticeNo())) {
			condition.put("matriculateNoticeNo", vo.getMatriculateNoticeNo());
		}
		if(ExStringUtils.isNotEmpty(vo.getGradeid())) {
			condition.put("gradeid", vo.getGradeid());
		}
		if(ExStringUtils.isNotEmpty(vo.getIsAbleOrderSubject())) {
			condition.put("isAbleOrderSubject",vo.getIsAbleOrderSubject());
		}
		Page page = studentInfoService.findStuByCondition(condition, objPage);
		
		model.addAttribute("stulist", page);
		model.addAttribute("condition", condition);
		
		return"/edu3/teaching/courseorder/personalGraduatePaperStatus-list";
	}
	/**
	 * 个人/年级预约权限状态设置
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/courseorder/bookingStatus.html")
	public void bookingStatusConfig(CourseOrderSearchVo vo,HttpServletRequest request,HttpServletResponse response)throws WebException{
		boolean isSuccess = true;
		String msg = "操作成功！";
		if (null != vo) {
			Map<String, Object> colMap = new HashMap<String, Object>();
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("studentids", vo.getSturesourceId());
			if ("personal".equals(vo.getOrderCourseStatusConfigType())) {
				//boolean isSuccess = studentInfoService.changeStuOrderCourseStatus(vo.getSturesourceId(),vo.getConfigStatus(),Integer.parseInt(vo.getOrderCourseStatus()));
				if ("1".equals(vo.getConfigStatus())) {
					colMap.put("orderCourseStatus", Integer.parseInt(vo.getOrderCourseStatus()));
				}
				if ("2".equals(vo.getConfigStatus())) {
					colMap.put("examOrderStatus", Integer.parseInt(vo.getOrderCourseStatus()));
				}

			} else if ("yearInfo".equals(vo.getOrderCourseStatusConfigType())) {

				if ("Y".equals(vo.getIsOpen())) {//开放预约
					List list = orderCourseSettingService.findOpenedSetting();

					if (list.size() >= 1) {
						isSuccess = false;
						msg = "同一时间只允许开放一个年度的预约权限！";
					} else {
						isSuccess = orderCourseSettingService.changeGradeOrderCourseStatus(vo.getSettingId(), vo.getIsOpen());
					}
				} else if ("N".equals(vo.getIsOpen())) {//屏蔽预约
					isSuccess = orderCourseSettingService.changeGradeOrderCourseStatus(vo.getSettingId(), vo.getIsOpen());
				}

			} else if ("graduatePaper".equals(vo.getOrderCourseStatusConfigType())) {
				//boolean isSuccess = studentInfoService.changeStuGraduatePaperOrderStatus(vo.getSturesourceId(),Integer.parseInt(vo.getOrderCourseStatus()));
				colMap.put("isAbleOrderSubject", Integer.parseInt(vo.getOrderCourseStatus()));
			}
			if (!isSuccess && "操作成功！".equals(msg)) {
				msg = "操作失败！";
			}
			try {
				studentInfoService.batchSetStudendInfo(colMap, condition);
			} catch (Exception e) {
				msg = "操作失败！";
				logger.error(e.getMessage());
			}
		} else {
			msg = "获取数据失败！";
		}
		renderJson(response,JsonUtils.objectToJson(msg));
	}
	/**
	 * 查询学员的交费状况
	 * @param vo
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/courseorder/checkFeeStatus.html")
	public void checkStuFeeStatus(CourseOrderSearchVo vo,HttpServletResponse response)throws WebException{
		Map resutlMap = courseOrderService.checkFeeByMatriculateNoticeNo(vo.getMatriculateNoticeNo());
		renderJson(response,JsonUtils.mapToJson(resutlMap));
	}
	
	/**
	 * 新生第一学期课程预约-列表
	 * @param request
	 * @return
	 */
	@RequestMapping("/edu3/teaching/courseorder/firstTermCourseOrder-list.html")
	public String firstTermCourseOrderList(HttpServletRequest request,ModelMap model){
		
		Map<String,Object> condition = new HashMap<String,Object>();
		List <StudentInfo> stuList   = new ArrayList<StudentInfo>();
		
		String gradeid      = ExStringUtils.trimToEmpty(request.getParameter("gradeid"));
		String majorid      = ExStringUtils.trimToEmpty(request.getParameter("major"));
		String classic      = ExStringUtils.trimToEmpty(request.getParameter("classic"));
		String schoolType   = ExStringUtils.trimToEmpty(request.getParameter("schoolType"));
		String name         = ExStringUtils.trimToEmpty(request.getParameter("name"));
		String studyNo      = ExStringUtils.trimToEmpty(request.getParameter("studyNo"));
		String branchSchool = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));//学习中心
		
		User user 			= SpringSecurityHelper.getCurrentUser();
		if(user.getOrgUnit().getUnitType().indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员
			branchSchool = user.getOrgUnit().getResourceid();
			model.addAttribute("isBrschool", true);
		}
		
		if (ExStringUtils.isNotEmpty(gradeid)) {
			condition.put("gradeid",gradeid);
		}
		if (ExStringUtils.isNotEmpty(majorid)) {
			condition.put("majorid",majorid);
		}
		if (ExStringUtils.isNotEmpty(classic)) {
			condition.put("classic",classic);
		}
		if (ExStringUtils.isNotEmpty(schoolType)) {
			condition.put("schoolType",schoolType);
		}
		if (ExStringUtils.isNotEmpty(branchSchool)) {
			condition.put("branchSchool", branchSchool);
		}
		
		if (ExStringUtils.isNotEmpty(name)) {
			condition.put("name",name);
		}
		if (ExStringUtils.isNotEmpty(studyNo)) {
			condition.put("studyNo",studyNo);
		}
		
		if (ExStringUtils.isNotEmpty(gradeid)&ExStringUtils.isNotEmpty(majorid)&& ExStringUtils.isNotEmpty(classic)&& ExStringUtils.isNotEmpty(schoolType)) {
			stuList = studentInfoService.findNoFirstTermCourseStu(condition);
			condition = teachingguideplanservice.getGuideTeahingFirstTermCourseNameAndID(condition);
		}
		model.addAttribute("condition", condition);
		model.addAttribute("stuList", stuList);
		
		return "/edu3/teaching/courseorder/firstTermCourseOrder-list";
	}	
	
	/**
	 * 新生第一学期课程预约-保存
	 * @param teachingPlanId 教学计划ID
	 * @param stuIds         学生ID 
	 * @param gradeid        年级ID
	 * @param majorid        专业ID
	 * @param classic        层次ID
	 * @param response 
	 */
	@RequestMapping("/edu3/teaching/courseorder/firstTermCourseOrder-save.html")
	public void firstTermCourseOrderSave(String teachingPlanId,String stuIds,String gradeid,
										 String majorid,String classic,HttpServletResponse response){
		
		Map <String,Object> map             = new HashMap<String, Object>();
		TeachingGuidePlan teachingguidePlan = teachingguideplanservice.findUniqueByProperty("resourceid", teachingPlanId);
		List<StudentInfo> stuList           = studentInfoService.findStuListByIds(stuIds);
		boolean setFirstTermCourseStatus    = false;
		
		map.put("teachingGuidePlan", teachingguidePlan);
		if (null!=stuList&&!stuList.isEmpty()) {
			for (StudentInfo stu:stuList) {
				try {
					map.put("studentInfo", stu);
					map=studentLearnPlanService.setFirstTermCourseOperaterForStudentReg(map);
				} catch (Exception e) {
					logger.error("学号"+stu.getStudyNo()+"预约第一学期课程出错:{}",e.fillInStackTrace());
					map.put("setFirstTermCourseStatus", false);
					String msg = "操作出错，请联系管理员!"+e.fillInStackTrace();
					map=setResultMapMsg(map,msg);
					break;
				}
				setFirstTermCourseStatus = null==map.get("setFirstTermCourseStatus")?false:(Boolean)map.get("setFirstTermCourseStatus");
				if (!setFirstTermCourseStatus) {
					break;
				}
			}
			if (setFirstTermCourseStatus) {
				String msg = "操作成功！";
				map=setResultMapMsg(map,msg);
			}else {
				String msg = "操作失败！";
				map=setResultMapMsg(map,msg);
			}
		}else {
			map.put("setFirstTermCourseStatus", false);
			String msg = "请选择要预约课程的学生!";
			map=setResultMapMsg(map,msg);
		}
		if (map.containsKey("teachingGuidePlan")) {
			map.remove("teachingGuidePlan");
		}
		if (map.containsKey("studentInfo")) {
			map.remove("studentInfo");
		}
		if (map.containsKey("teachingPlanCourseOrderList")) {
			map.remove("teachingPlanCourseOrderList");
		}
		if (map.containsKey("updateStudentLearnPlanStatus")) {
			map.remove("updateStudentLearnPlanStatus");
		}
		if (map.containsKey("addCourseBoookOrdersStatus")) {
			map.remove("addCourseBoookOrdersStatus");
		}
		if (map.containsKey("updateCourseOrderAndCourseOrderStatus")) {
			map.remove("updateCourseOrderAndCourseOrderStatus");
		}
		
		map.put("gradeid",gradeid);
		map.put("majorid", majorid);
		map.put("classic", classic);
		
		renderJson(response,JsonUtils.mapToJson(map));
	}
	/**
	 * 个别课程预约-列表
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/teaching/examorder/individualOrder-list.html")
	public String individualCourseOrder(HttpServletRequest request,ModelMap model,Page objPage){
		
		objPage.setOrderBy("studyNo");
		objPage.setOrder(Page.DESC);
		
		Map<String,Object> condition = new HashMap<String,Object>();
		
		String name         = ExStringUtils.trimToEmpty(request.getParameter("name"));
		String majorid      = ExStringUtils.trimToEmpty(request.getParameter("major"));
		String classic      = ExStringUtils.trimToEmpty(request.getParameter("classic"));
		String gradeid      = ExStringUtils.trimToEmpty(request.getParameter("gradeid"));
		String branchSchool = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));
		String studyNo      = ExStringUtils.trimToEmpty(request.getParameter("matriculateNoticeNo"));

		User user 			= SpringSecurityHelper.getCurrentUser();
		if(user.getOrgUnit().getUnitType().indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员
			branchSchool    = user.getOrgUnit().getResourceid();
			model.addAttribute("isBrschool", true);
		}
	
		if(ExStringUtils.isNotEmpty(name)) {
			condition.put("name",name);
		}
		if(ExStringUtils.isNotEmpty(majorid)) {
			condition.put("major",majorid);
		}
		if(ExStringUtils.isNotEmpty(classic)) {
			condition.put("classic",classic);
		}
		if(ExStringUtils.isNotEmpty(gradeid)) {
			condition.put("gradeid",gradeid);
		}
		if(ExStringUtils.isNotEmpty(branchSchool)) {
			condition.put("branchSchool",branchSchool);
		}
		if(ExStringUtils.isNotEmpty(studyNo)) {
			condition.put("matriculateNoticeNo",studyNo);
		}
		
		Page page = studentInfoService.findStuByCondition(condition, objPage);
		
		model.addAttribute("page", page);
		model.addAttribute("condition", condition);
		
		
		return "/edu3/teaching/orderManager/individualCourseOrder-list";
	}
	/**
	 * 个别课程预约删除-列表
	 * @param studentId
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/teaching/courseorder/del-list.html")
	public String delIndividualCourseOrderList(String studentId,HttpServletRequest request,ModelMap model){	
		//获得学生的学籍信息
		StudentInfo stu = studentInfoService.get(studentId);
		//学生的教学计划
		TeachingPlan p = stu.getTeachingPlan();
		List<StudentLearnPlan> learnPlan = studentLearnPlanService.getStudentLearnPlanList(null==stu?"":stu.getResourceid(),null==stu.getTeachingPlan()?"":stu.getTeachingPlan().getResourceid());
		model.put("learnPlan", learnPlan);
		
		return "/edu3/teaching/orderManager/individualCourseOrderDel-list";
	}
	/**
	 * 个别课程预约删除-删除
	 * @param studentLearnPlanCourseIds
	 * @param response
	 */
	@RequestMapping("/edu3/teaching/courseorder/del.html")
	public void delIndividualCourseOrder(String studentLearnPlanCourseIds,HttpServletResponse response){
		
		Map<String,Object> condition  = new HashMap<String, Object>();
		User user                     = SpringSecurityHelper.getCurrentUser();
		List<OrderCourseSetting> list = orderCourseSettingService.findOpenedSetting();//获取开放的预约批次
		
		
		//要删除的学习计划ID不为空
		if(ExStringUtils.isNotEmpty(studentLearnPlanCourseIds)){

			String[] ids = studentLearnPlanCourseIds.split(",");
			condition.put("orderCourseSetting", list);
			condition.put("notLimitflag", "individualCourseOrderDel");
			try {
				for (String learnPlanId:ids) {	
					
						StudentLearnPlan learnPlan = studentLearnPlanService.get(learnPlanId);
						//些功能为后台管理员执行操作,删除时间为批次预约结束时间+7天
						condition.put("operater", "manager");
						condition.put("learnPlanId", learnPlanId);
						condition.put("learnPlan", learnPlan);
						condition.put("studentId", learnPlan.getStudentInfo().getResourceid());
						
						condition = courseOrderService.delCourseOrderByLearnPlanId(condition);
						
						boolean isDelSuccess = (Boolean)condition.get("isDelSuccess");
						if (isDelSuccess) {
							//日志记录
							logger.info("操作类型:删除课程预约   操作人："+user.getResourceid()+"   学号："+learnPlan.getStudentInfo().getStudyNo()+"   删除课程:"+learnPlan.getTeachingPlanCourse().getCourse().getCourseName()+"   "+
									    "操作时间:"+ExDateUtils.formatDateStr(new Date(), ExDateUtils.PATTREN_DATE_TIME));
						}
						
				}
				condition.put("isSuccess",true);
			} catch (Exception e) {
				logger.error("删除学生预约学习记录异常:{}",e.fillInStackTrace());
				condition.put("errorMsg","删除学生预约学习记录异常!");
				condition.put("isSuccess",false);
			}
		//要删除的学习计划ID为空		
		}else {
			condition.put("errorMsg","请选择要删除的预约！");
		}
		
		if (condition.containsKey("course")) {
			condition.remove("course");
		}
		if (condition.containsKey("learnPlan")) {
			condition.remove("learnPlan");
		}
		if (condition.containsKey("orderCourseSetting")) {
			condition.remove("orderCourseSetting");
		}
		
		renderJson(response,JsonUtils.mapToJson(condition));
	}
	/**
	 * 个别考试预约删除-列表
	 * @param studentId
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/teaching/examorder/del-list.html")
	public String delIndividualExamOrderList(String studentId,HttpServletRequest request,ModelMap model){
		
		//获得学生的学籍信息
		StudentInfo stu = studentInfoService.get(studentId);
		//学生的学习计划
		List<StudentLearnPlan> learnPlan = studentLearnPlanService.getStudentLearnPlanList(null==stu?"":stu.getResourceid(),null==stu.getTeachingPlan()?"":stu.getTeachingPlan().getResourceid());
		model.addAttribute("learnPlan", learnPlan);
		
		return "/edu3/teaching/orderManager/individualExamOrderDel-list";
	}
	/**
	 * 个别考试预约删除-删除
	 * @param studentLearnPlanCourseIds
	 * @param response
	 */
	@RequestMapping("/edu3/teaching/examorder/del.html")
	public void delIndividualExamOrder(String studentLearnPlanCourseIds,HttpServletResponse response){
		
	
		Map<String,Object> condition = new HashMap<String, Object>();
		List<ExamSub> subList		 = examSubService.findOpenedExamSub("exam");	//获取开放的考试批次
		User user                    = SpringSecurityHelper.getCurrentUser();
		
		//要删除的学习计划ID不为空
		if(ExStringUtils.isNotEmpty(studentLearnPlanCourseIds)){

			String[] ids = studentLearnPlanCourseIds.split(",");
			condition.put("subList",subList);
			try {
				for (String learnPlanId:ids) {	
					
						StudentLearnPlan learnPlan = studentLearnPlanService.get(learnPlanId);
						
						condition.put("operater", "manager");      //些功能为后台管理员执行操作,删除时间为批次预约结束时间+7天
						condition.put("learnPlan", learnPlan);
						condition.put("studentId", learnPlan.getStudentInfo().getResourceid());
						condition			       = courseOrderService.delExamOrderByLearnPlanId(condition);
						boolean isDelSuccess       = (Boolean)condition.get("isDelSuccess");
						if (isDelSuccess) {
							//日志记录
							logger.info("操作类型:删除考试预约   操作人："+user.getResourceid()+"   学号："+learnPlan.getStudentInfo().getStudyNo()+"   删除课程:"+learnPlan.getTeachingPlanCourse().getCourse().getCourseName()+"   "+
									    "操作时间:"+ExDateUtils.formatDateStr(new Date(), ExDateUtils.PATTREN_DATE_TIME));
						}
						
				}
				condition.put("isSuccess",true);
			} catch (Exception e) {
				try {
					logger.error("删除学生预约考试记录异常:{}",e.fillInStackTrace());
					//日志记录
					logger.info("操作类型:教务人员取消预约异常   操作人："+user.getResourceid()+"  操作时间: "+ExDateUtils.formatDateStr(new Date(), ExDateUtils.PATTREN_DATE_TIME));
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				condition.put("isSuccess",false);
				condition.put("errorMsg","删除学生预约考试记录异常!"+e.getMessage());
			}
		//要删除的学习计划ID为空	
		}else {
			condition.put("errorMsg","请选择要删除的预约！");
			condition.put("isSuccess",false);
		}
		
		if (condition.containsKey("course")) {
			condition.remove("course");
		}
		if (condition.containsKey("subList")) {
			condition.remove("subList");
		}
		if (condition.containsKey("learnPlan")) {
			condition.remove("learnPlan");
		}
		
		renderJson(response,JsonUtils.mapToJson(condition));
	}
	/**
	 * 个别课程预约-新增预约表单
	 * @param studentId
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/teaching/courseorder/makeup-form.html")
	public String addIndividualCourseOrder(String studentId,String yearInfoSettingId,HttpServletRequest request,ModelMap model){
		//获得学生的学籍信息
		StudentInfo stu 				 = studentInfoService.get(studentId);
		//教学计划
		TeachingPlan teachingPlan        = null==stu?null:stu.getTeachingPlan();
		//教学计划VO
		Map<String,Object>  voMap   	 = new HashMap<String, Object>();
		try {
			//学生的学习计划
			List<StudentLearnPlan> learnPlan = studentLearnPlanService.getStudentLearnPlanListBySql(null==stu?"":stu.getResourceid(),null==stu.getTeachingPlan()?"":stu.getTeachingPlan().getResourceid());
			
			voMap   	 				 = studentLearnPlanService.changeTeachingPlanToTeachingPlanVo(stu,learnPlan,teachingPlan);
		} catch (Exception e) {
			logger.error("个别课程预约-转换成教学计划VO出错:{}",e.fillInStackTrace());
		}
		
		List<LearningPlanVo> voList      = (List<LearningPlanVo>) voMap.get("planCourseList");
		LearningPlanVo  planTitleVo      = (LearningPlanVo)voMap.get("planTitle");

	
		model.addAttribute("studentInfo", stu);
		model.addAttribute("teachingPlanVo", voList);
		model.addAttribute("planTitleVo", planTitleVo);
		model.addAttribute("yearInfoSettingId", yearInfoSettingId);
		
		return "/edu3/teaching/orderManager/individualCourseOrder-form";
	}
	/**
	 * 个别课程预约-新增预约 保存
	 * @param sutId                     学生ID
	 * @param teachingPlanId            教学计划ID
	 * @param teachingPlanCourseId      教学计划课程ID
	 * @param teachingPlanCourseStatus  教学计划课程状态
	 * @param response
	 */
	@RequestMapping("/edu3/teaching/courseorder/makeup-save.html")
	public void saveIndividualCourseOrder(String sutId,String teachingPlanCourseId,
										  String teachingPlanId,String teachingPlanCourseStatus,
										  String yearInfoSettingId,HttpServletResponse response){
		
		Map<String, Object> checkArrowResultMap = new HashMap<String, Object>();
		User user                               = SpringSecurityHelper.getCurrentUser();
		//预约考试不需要选择预约年度
		boolean isYearInfoEmptyCheckPass        = true;
		if ("1".equals(teachingPlanCourseStatus)&&ExStringUtils.isEmpty(yearInfoSettingId)) {
			isYearInfoEmptyCheckPass            = false;
		}
		if(ExStringUtils.isNotEmpty(sutId)&&ExStringUtils.isNotEmpty(teachingPlanCourseId)
		   &&ExStringUtils.isNotEmpty(teachingPlanCourseStatus)&& isYearInfoEmptyCheckPass) {
			
			checkArrowResultMap.put("teachingPlanId", teachingPlanId);//所属教学计划ID
			checkArrowResultMap.put("teachingPlanCourseId", teachingPlanCourseId);//要预约的教学计划课程ID
			checkArrowResultMap.put("teachingPlanCourseStatus", teachingPlanCourseStatus);//教学计划课程状态
			
			//获得学生的学籍信息
			StudentInfo studentInfo          = studentInfoService.get(sutId);
			//教学计划
			TeachingPlan teachingPlan        = teachingPlanService.get(teachingPlanId);
			//学生的学习计划
			List<StudentLearnPlan> learnPlan = studentLearnPlanService.getStudentLearnPlanList(studentInfo.getResourceid(),studentInfo.getTeachingPlan().getResourceid());
			//要进行预约操作的教学计划课程
			TeachingPlanCourse orderCourse   = teachingPlanCourseService.get(teachingPlanCourseId);
			//年度预约设置对象
			OrderCourseSetting yearSetting   = orderCourseSettingService.get(yearInfoSettingId);
			
			checkArrowResultMap.put("chooseSetting", yearSetting);
			checkArrowResultMap.put("studentInfo", studentInfo);
			checkArrowResultMap.put("teachingPlan", teachingPlan);
			checkArrowResultMap.put("learnPlan", learnPlan);
			checkArrowResultMap.put("orderCourse", orderCourse);
			checkArrowResultMap.put("yearInfoSettingId",yearInfoSettingId);
			
			if ("1".equals(teachingPlanCourseStatus)) {//预约学习
				
				checkArrowResultMap.put("operateType","orderCourse");//用于前端页面判断所执行操作的结果
				checkArrowResultMap.put("operater","teacher");//用于studentLearnPlanService.isAllowOrderCourse方法中判断操作人 学生OR管理员
				
				//1.预约条件检查
				checkArrowResultMap 	   = studentLearnPlanService.isAllowOrderCourse(checkArrowResultMap);
				boolean isArrowOrderCourse = null==checkArrowResultMap.get("isArrowOrderCourse")?false:(Boolean) checkArrowResultMap.get("isArrowOrderCourse");	
				
				
				try {
					//2.如果允许预约则在预约明细、预约统计、学习计划、教材预约中增加或修改相应的记录
					if (isArrowOrderCourse) {
						checkArrowResultMap=studentLearnPlanService.allowOrderCourseOperate(checkArrowResultMap);
					}else {
						checkArrowResultMap.put("orderCourseOperateStatus", false);
					}
				
					boolean orderCourseOperateStatus = null==checkArrowResultMap.get("orderCourseOperateStatus")?false:(Boolean)checkArrowResultMap.get("orderCourseOperateStatus");
					//3.预约结果
					if (orderCourseOperateStatus) {
						//日志记录
						logger.info("预约类型:预约学习   操作人："+user.getResourceid()+"   学号："+studentInfo.getStudyNo()+"   预约课程:"+orderCourse.getCourse().getCourseName()+"   "+
								    "预约时间:"+ExDateUtils.formatDateStr(new Date(), ExDateUtils.PATTREN_DATE_TIME));
						checkArrowResultMap.put("orderCourseStatus", true);
					}else {
						checkArrowResultMap.put("orderCourseStatus", false);
					}
				} catch (Exception e) {
					checkArrowResultMap.put("orderCourseStatus", false);
					checkArrowResultMap.put("orderCourseOperateStatus", false);
					List<String> resultMapMsgList =  (List<String>) checkArrowResultMap.get("msg"); 
					if (null==resultMapMsgList ) {
						List<String> msgList = new ArrayList<String>();
						msgList.add("服务器异常，请与管理员联系!"+e.fillInStackTrace());
						checkArrowResultMap.put("msg",msgList);
					}
					logger.error("预约学习异常：{}"+e.fillInStackTrace());
				}
				
			}else if("2".equals(teachingPlanCourseStatus)) {//预约考试
				
				checkArrowResultMap.put("operateType","orderExam");//用于前端页面判断所执行操作的结果
				checkArrowResultMap.put("operater","teacher");//用于studentLearnPlanService.isAllowOrderExam方法中判断操作人 学生OR管理员
				
				//1.预约条件检查
				checkArrowResultMap=studentLearnPlanService.isAllowOrderExam(checkArrowResultMap);
				boolean isAllowOrderExam = null==checkArrowResultMap.get("isAllowOrderExam")?false:(Boolean)checkArrowResultMap.get("isAllowOrderExam");
			
				
				try {
					//2.如果允许预约则 修改学习计划及学生预约课程状态 添加一个学生预约考试记录
					if (isAllowOrderExam) {
						checkArrowResultMap=studentLearnPlanService.allowOrderExamOperate(checkArrowResultMap);
					}else {
						checkArrowResultMap.put("orderExamOperateStatus", false);
					}
					
					boolean orderExamOperateStatus = null==checkArrowResultMap.get("orderExamOperateStatus")?false:(Boolean)checkArrowResultMap.get("orderExamOperateStatus");
					
					//3.预约结果
					if (orderExamOperateStatus) {
						//日志记录
						logger.info("预约类型:预约考试   操作人："+user.getResourceid()+"   学号："+studentInfo.getStudyNo()+"   预约课程:"+orderCourse.getCourse().getCourseName()+"   "+
								    "预约时间:"+ExDateUtils.formatDateStr(new Date(), ExDateUtils.PATTREN_DATE_TIME));
						checkArrowResultMap.put("orderExamStatus", true);
					}else {
						checkArrowResultMap.put("orderExamStatus", false);
					}
				} catch (Exception e) {
					checkArrowResultMap.put("orderExamStatus", false);
					checkArrowResultMap.put("orderExamOperateStatus", false);
					List<String> resultMapMsgList =  (List<String>) checkArrowResultMap.get("msg"); 
					if (null==resultMapMsgList) {
						List<String> msgList = new ArrayList<String>();
						msgList.add("服务器异常，请与管理员联系!"+e.fillInStackTrace());
						checkArrowResultMap.put("msg",msgList);
					}
					logger.error("预约考试异常：{}"+e.fillInStackTrace());
				}
			}
		}else {
			checkArrowResultMap.put("operateError",true);
			List<String> resultMapMsgList =  (List<String>) checkArrowResultMap.get("msg"); 
			if (null==resultMapMsgList ||(null!=resultMapMsgList&& resultMapMsgList.isEmpty())) {
				List<String> msgList = new ArrayList<String>();
				msgList.add("数据格式有错...");
				checkArrowResultMap.put("msg",msgList);
			}
		}

		if (checkArrowResultMap.containsKey("studentInfo")) {
			checkArrowResultMap.remove("studentInfo");
		}
		if (checkArrowResultMap.containsKey("teachingPlan")) {
			checkArrowResultMap.remove("teachingPlan");
		}
		if (checkArrowResultMap.containsKey("learnPlan")) {
			checkArrowResultMap.remove("learnPlan");
		}
		if (checkArrowResultMap.containsKey("orderCourse")) {
			checkArrowResultMap.remove("orderCourse");
		}
		if (checkArrowResultMap.containsKey("teachingPlanId")) {
			checkArrowResultMap.remove("teachingPlanId");
		}
		if (checkArrowResultMap.containsKey("teachingPlanCourseId")) {
			checkArrowResultMap.remove("teachingPlanCourseId");
		}
		if (checkArrowResultMap.containsKey("teachingPlanCourseStatus")) {
			checkArrowResultMap.remove("teachingPlanCourseStatus");
		}
		if (checkArrowResultMap.containsKey("orderCourseSetting")) {
			checkArrowResultMap.remove("orderCourseSetting");
		}
		if (checkArrowResultMap.containsKey("examSub")) {
			checkArrowResultMap.remove("examSub");
		}
		if (checkArrowResultMap.containsKey("chooseSetting")) {
			checkArrowResultMap.remove("chooseSetting");
		}
		if (checkArrowResultMap.containsKey("examResult")) {
			checkArrowResultMap.remove("examResult");
		}
		renderJson(response, JsonUtils.mapToJson(checkArrowResultMap));
	}
	/**
	 * 预约学习统计
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/teaching/courseorder/statCourseOrder.html")
	public String statCourseOrder(HttpServletRequest request,HttpServletResponse response,Page objPage,ModelMap model){
		
		objPage.setOrderBy(" unit.UNITNAME,classic.CLASSICNAME,major.MAJORNAME,course.courseName");
		objPage.setOrder(Page.ASC);
		
		Map<String,Object> condition = new HashMap<String, Object>();
		User user                    = SpringSecurityHelper.getCurrentUser();
		String branchSchool 		 = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));
		String yearInfo     		 = ExStringUtils.trimToEmpty(request.getParameter("yearInfo"));
		String classic      		 = ExStringUtils.trimToEmpty(request.getParameter("classic"));
		String course       		 = ExStringUtils.trimToEmpty(request.getParameter("course"));
		String major        		 = ExStringUtils.trimToEmpty(request.getParameter("major"));
		String term         		 = ExStringUtils.trimToEmpty(request.getParameter("term"));
		String startTime    		 = ExStringUtils.trimToEmpty(request.getParameter("startTime"));
		String endTime      		 = ExStringUtils.trimToEmpty(request.getParameter("endTime"));
		
		if(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())){
			   branchSchool = user.getOrgUnit().getResourceid();
			   model.addAttribute("isBranchSchool", true);
		}
		if (ExStringUtils.isNotEmpty(branchSchool)) {
			condition.put("branchSchool", branchSchool);
		}
		if (ExStringUtils.isNotEmpty(classic)) {
			condition.put("classic", classic);
		}
		if (ExStringUtils.isNotEmpty(major)) {
			condition.put("major", major);
		}
		if (ExStringUtils.isNotEmpty(course)) {
			condition.put("course", course);
		}
		if (ExStringUtils.isNotEmpty(yearInfo)) {
			condition.put("yearInfo", yearInfo);
		}
		if (ExStringUtils.isNotEmpty(term)) {
			condition.put("term", term);
		}
		if (ExStringUtils.isNotEmpty(startTime)) {
			condition.put("startTime", startTime);
		}
		if (ExStringUtils.isNotEmpty(endTime)) {
			condition.put("endTime", endTime);
		}
		if (!condition.isEmpty()&&condition.containsKey("yearInfo")&&condition.containsKey("term")) {
			//objPage = teachingJDBCService.statCourseOrder(condition,objPage);
			model.addAttribute("statCourseOrderPage", objPage);
		}		
		model.addAttribute("condition", condition);
		
		return "/edu3/teaching/courseorder/statCourseOrder";
	}
	/**
	 * 导出预约学习统计
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/courseorder/statcourseorder/export.html")
	public void exportCourseOrder(HttpServletRequest request, HttpServletResponse response) throws WebException {
		Map<String,Object> condition = new HashMap<String, Object>();
		String yearInfo			     = ExStringUtils.trimToEmpty(request.getParameter("yearInfo"));
		String term 				 = ExStringUtils.trimToEmpty(request.getParameter("term"));
		String branchSchool			 = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));		
		String status                = ExStringUtils.trimToEmpty(request.getParameter("status"));		
		String startTime             = ExStringUtils.trimToEmpty(request.getParameter("startTime"));		
		String endTime               = ExStringUtils.trimToEmpty(request.getParameter("endTime"));		
		String course                = ExStringUtils.trimToEmpty(request.getParameter("course"));		
		
		User user = SpringSecurityHelper.getCurrentUser();
		if(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())) {
			 branchSchool  = user.getOrgUnit().getResourceid();
		}
		       
		String yearInfoStr = "";
		if(ExStringUtils.isNotBlank(yearInfo)){
			condition.put("yearInfo", yearInfo);
			YearInfo year  = yearInfoService.get(yearInfo);
			yearInfoStr   += year.getYearName();
		}
		if(ExStringUtils.isNotBlank(term)){		
			condition.put("term", term);
			yearInfoStr  += JstlCustomFunction.dictionaryCode2Value("CodeTerm", term);
		}
		if(ExStringUtils.isNotBlank(branchSchool)){
			condition.put("branchSchool", branchSchool);
		}
		if (ExStringUtils.isNotEmpty(status)) {
			condition.put("status", status);
		}
		if (ExStringUtils.isNotEmpty(startTime)) {
			condition.put("startTime", startTime);
		}
		if (ExStringUtils.isNotEmpty(endTime)) {
			condition.put("endTime", endTime);
		}
		if (ExStringUtils.isNotEmpty(course)) {
			condition.put("course", course);
		}
		//List<Map<String,Object>> list = teachingJDBCService.statCourseOrder(condition);
		Long totalCount   = 0L;
//		if(null!=list){
//			for (Map<String, Object> map : list) {
//				BigDecimal count = (BigDecimal)map.get("COUNTS");
//				totalCount += count.longValue();
//			}
//		}
		//文件输出服务器路径
		setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
		try{
			//导出
			File excelFile = null;
			GUIDUtils.init();
			File disFile = new File(getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(false) + ".xls");
			Map<String,Object> templateMap = new HashMap<String, Object>();//设置模板
			templateMap.put("yearInfo", yearInfoStr);
			templateMap.put("totalCount", totalCount);
			templateMap.put("orderType",JstlCustomFunction.dictionaryCode2Value("CodeCourseOrderStatus",status));
			//模板文件路径
			String templateFilepathString = "courseOrders.xls";
			
			//初始化配置参数
			//exportExcelService.initParmasByfile(disFile, "courseOrders", list,null);
			exportExcelService.getModelToExcel().setTemplateParam(templateFilepathString, 2, templateMap);
			exportExcelService.getModelToExcel().setRowHeight(500);
			excelFile = exportExcelService.getExcelFile();//获取导出的文件

			downloadFile(response, yearInfoStr+"预约学习统计.xls", excelFile.getAbsolutePath(),true);
		}catch(Exception e){			
			logger.error("导出excel文件出错："+e.fillInStackTrace());
		}
	}
	/**
	 * 预约考试统计
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/teaching/courseorder/statExamOrder.html")
	public String statExamOrder(HttpServletRequest request,HttpServletResponse response,Page objPage,ModelMap model){
		
		Map<String,Object> condition = new HashMap<String, Object>();
		User user                    = SpringSecurityHelper.getCurrentUser();
		String branchSchool 		 = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));
		String yearInfo     		 = ExStringUtils.trimToEmpty(request.getParameter("yearInfo"));
		String course       		 = ExStringUtils.trimToEmpty(request.getParameter("course"));
		String term         		 = ExStringUtils.trimToEmpty(request.getParameter("term"));
		String startTime    		 = ExStringUtils.trimToEmpty(request.getParameter("startTime"));
		String endTime      		 = ExStringUtils.trimToEmpty(request.getParameter("endTime"));
		
		if(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())){
			   branchSchool = user.getOrgUnit().getResourceid();
			   model.addAttribute("isBranchSchool", true);
		}
		if (ExStringUtils.isNotEmpty(branchSchool)) {
			condition.put("branchSchool", branchSchool);
		}
		if (ExStringUtils.isNotEmpty(course)) {
			condition.put("course", course);
		}
		if (ExStringUtils.isNotEmpty(yearInfo)) {
			condition.put("yearInfo", yearInfo);
		}
		if (ExStringUtils.isNotEmpty(term)) {
			condition.put("term", term);
		}
		if (ExStringUtils.isNotEmpty(startTime)) {
			condition.put("startTime", startTime);
		}
		if (ExStringUtils.isNotEmpty(endTime)) {
			condition.put("endTime", endTime);
		}
		
		if (!condition.isEmpty()&&condition.containsKey("yearInfo")&&condition.containsKey("term")) {
			condition.put("status", 2);
			//objPage = teachingJDBCService.statExamOrder(condition, objPage);
			

			model.addAttribute("statCourseOrderPage", objPage);
		}		
		model.addAttribute("condition", condition);
		
		return "/edu3/teaching/courseorder/statExamOrder";
	}
	/**
	 * 预约考试统计导出--条件选择页面
	 * @param request
	 * @param model
	 */
	@RequestMapping("/edu3/teaching/examorder/export-condition.html")
	public String statExamOrderExportCondition(HttpServletRequest request,ModelMap  model){
		
		Map<String,Object> condition = new HashMap<String, Object>();
		
		String flag 	   			 = ExStringUtils.trimToEmpty(request.getParameter("flag"));
		String yearInfo    			 = ExStringUtils.trimToEmpty(request.getParameter("yearInfo"));
		String term        			 = ExStringUtils.trimToEmpty(request.getParameter("term"));
		
		List<ExamSub> list 			 = examSubService.findByCriteria(Restrictions.eq("yearInfo.resourceid",yearInfo),Restrictions.eq("term",term),Restrictions.eq("batchType","exam"));
		
		//获取所选择考试批次的考试时间段
		if (null!=list&&!list.isEmpty()){
			condition.put("examSub",      list.get(0));
			//List<Map<String, Object>> timeSegmentList = teachingJDBCService.findExamTimeSegment(list.get(0).getResourceid(),"","");
			//model.addAttribute("timeSegmentList",timeSegmentList);
		}   
		if (ExStringUtils.isNotEmpty(flag)) {
			condition.put("flag", flag);
		}
		if (ExStringUtils.isNotEmpty(flag)) {
			condition.put("yearInfo", yearInfo);
		}
		if (ExStringUtils.isNotEmpty(flag)) {
			condition.put("term", term);
		}
		
		User user 				     = SpringSecurityHelper.getCurrentUser();
		
		Criterion[] criterion        = new Criterion[2];
		criterion[0]                 = Restrictions.eq("isDeleted",0);
		criterion[1]                 = Restrictions.eq("unitType", CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue());
		List<OrgUnit> brSchoolList   = orgUnitService.findByCriteria(OrgUnit.class,criterion,Order.asc("unitCode"));
		if(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())){
			brSchoolList.clear();
			OrgUnit u 			     = orgUnitService.get(user.getOrgUnit().getResourceid());
			brSchoolList.add(u);
		}	
		

		model.put("brSchoolList", brSchoolList);
		model.put("condition", condition);
		
		
		return "/edu3/teaching/courseorder/export_StatExamOrder_condition";
	}
	/**
	 * 预约考试统计导出
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/teaching/examorder/export.html")
	public void statCourseOrderExport(HttpServletRequest request,HttpServletResponse response){
		
		Map<String,Object> condition = new HashMap<String, Object>();
		
		String flag 	   			 = ExStringUtils.trimToEmpty(request.getParameter("flag"));
		String branchSchool 		 = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));
		String yearInfo     		 = ExStringUtils.trimToEmpty(request.getParameter("yearInfo"));
		String term        			 = ExStringUtils.trimToEmpty(request.getParameter("term"));
		String courseId              = ExStringUtils.trimToEmpty(request.getParameter("courseId"));
		String examTimeSegment       = ExStringUtils.trimToEmpty(request.getParameter("examTimeSegment"));
		String ismachineexam       	 = ExStringUtils.trimToEmpty(request.getParameter("ismachineexam"));
		
		String startTime     		 = "";
		String endTime      		 = "";
		
		User user 				     = SpringSecurityHelper.getCurrentUser();
		if(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())){
			branchSchool             = user.getOrgUnit().getResourceid();
		}
		
		if(ExStringUtils.isNotEmpty(branchSchool)&&!"null".equals(branchSchool)&&branchSchool.split(",").length>0){
			StringBuffer ids = new StringBuffer();
			for (String id:branchSchool.split(",")) {
				ids.append(",'"+id+"'");
			}
			condition.put("branchSchool", ids.substring(1));
		}
		
		if(ExStringUtils.isNotEmpty(courseId)&&!"null".equals(courseId)&&courseId.split(",").length>0){
			StringBuffer ids = new StringBuffer();
			for (String id:courseId.split(",")) {
				ids.append(",'"+id+"'");
			}
			condition.put("course", ids.substring(1));
		}
		if(ExStringUtils.isNotEmpty(flag)) {
			condition.put("flag", flag);
		}
		if (ExStringUtils.isNotEmpty(examTimeSegment)) {
			String [] segment = examTimeSegment.split("TO");
			if (null!=segment && segment.length>1) {
				String s_t     = segment[0];
				String e_t     = segment[1];
				startTime      = s_t;
				endTime        = e_t;
			}
		}
		if (ExStringUtils.isNotEmpty(startTime)) {
			condition.put("startTime",    startTime);
		}
		if (ExStringUtils.isNotEmpty(startTime)) {
			condition.put("endTime",      endTime);
		}
		if (ExStringUtils.isNotEmpty(ismachineexam)) {
			condition.put("ismachineexam",ismachineexam);
		}
		
		if (ExStringUtils.isNotEmpty(yearInfo) && ExStringUtils.isNotEmpty(term)) {
			
			condition.put("yearInfo",yearInfo);
			condition.put("term",term);
			String title = yearInfoService.get(yearInfo).getYearName()+JstlCustomFunction.dictionaryCode2Value("CodeTermType", term)+"预约考试统计";
			
			//List<ExamOrderStatExportVo> list = teachingJDBCService.statExamOrder(condition);
			//文件输出服务器路径
			setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
			
			try{
				//导出
				File excelFile = null;
				GUIDUtils.init();
				File disFile = new File(getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(false) + ".xls");
				//disFile 	 = new File("c:\\dd.xls");
				Map<String,Object> templateMap 		= new HashMap<String, Object>();//设置模板参数
				templateMap.put("title", title);
				
				//模板文件路径
				String templateFilepathString = "";
				if ("total".equals(flag)) {
					templateFilepathString    =  "examOrderStatExportTotal.xls";
					//初始化配置参数
				//	exportExcelService.initParmasByfile(disFile, "examOrderStatExportTotal", list,null);
				}else{
					templateFilepathString    =  "examOrderStatExportSortByCondition.xls";
				//	exportExcelService.initParmasByfile(disFile, "examOrderStatExportSortByCondition", list,null);
				}
				exportExcelService.getModelToExcel().setRowHeight(360);//设置行高
				exportExcelService.getModelToExcel().setHeader(title);
				exportExcelService.getModelToExcel().setTemplateParam(templateFilepathString, 2, templateMap);	
				
				excelFile = exportExcelService.getExcelFile();//获取导出的文件
				
				downloadFile(response,title+".xls", excelFile.getAbsolutePath(),true);
				
				
			}catch(Exception e){
				e.printStackTrace();
				logger.error("导出excel文件出错："+e.fillInStackTrace());
			}		
		}
	}
	
	/**
	 * 预约统计-查看预约明细
	 * @param request
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/courseorder/courseOrder-details.html")
	public String statCourseOrderDetails(HttpServletRequest request,ModelMap  model,Page page)throws WebException{
		
		page.setOrderBy(" o.studentInfo.studyNo ");
		page.setOrder(Page.ASC);
		
		Map<String,Object> condition = new HashMap<String, Object>();
	
		String major        		 = ExStringUtils.trimToEmpty(request.getParameter("major"));
		String grade      		     = ExStringUtils.trimToEmpty(request.getParameter("grade"));
		String status        		 = ExStringUtils.trimToEmpty(request.getParameter("status"));
		String statid      		     = ExStringUtils.trimToEmpty(request.getParameter("statid"));
		String classic      		 = ExStringUtils.trimToEmpty(request.getParameter("classic"));
		String studyNo               = ExStringUtils.trimToEmpty(request.getParameter("studyNo"));
		String studentName           = ExStringUtils.trimToEmpty(request.getParameter("studentName"));
		String branchSchool 		 = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));
		String endTime     			 = ExStringUtils.trimToEmpty(request.getParameter("endTime"));
		String startTime    		 = ExStringUtils.trimToEmpty(request.getParameter("startTime"));
		String yearInfo              = ExStringUtils.trimToEmpty(request.getParameter("yearInfo"));    
		String term                  = ExStringUtils.trimToEmpty(request.getParameter("term"));
		String courseId              = ExStringUtils.trimToEmpty(request.getParameter("courseId"));
		String flag                  = ExStringUtils.trimToEmpty(request.getParameter("flag"));
		
		User user 					 = SpringSecurityHelper.getCurrentUser();
		if(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())) {
			 branchSchool  			 = user.getOrgUnit().getResourceid();
		}
		
		if (ExStringUtils.isNotEmpty(major)) {
			condition.put("major", major);
		}
		if (ExStringUtils.isNotEmpty(grade)) {
			condition.put("grade", grade);
		}
		if (ExStringUtils.isNotEmpty(status)) {
			condition.put("status", status);
		}
		if (ExStringUtils.isNotEmpty(statid)) {
			condition.put("statid", statid);
		}
		if (ExStringUtils.isNotEmpty(classic)) {
			condition.put("classic", classic);
		}
		if (ExStringUtils.isNotEmpty(studyNo)) {
			condition.put("studyNo", studyNo);
		}
		if (ExStringUtils.isNotEmpty(endTime)) {
			condition.put("endTime", endTime);
		}
		if (ExStringUtils.isNotEmpty(startTime)) {
			condition.put("startTime",startTime);
		}
		if (ExStringUtils.isNotEmpty(studentName)) {
			condition.put("studentName", studentName);
		}
		if (ExStringUtils.isNotEmpty(branchSchool)) {
			condition.put("branchSchool", branchSchool);
		}
		if (ExStringUtils.isNotEmpty(yearInfo)) {
			condition.put("yearInfo", yearInfo);
		}
		if (ExStringUtils.isNotEmpty(term)) {
			condition.put("term", term);
		}
		if (ExStringUtils.isNotEmpty(courseId)) {
			condition.put("courseId", courseId);
		}
		if (ExStringUtils.isNotEmpty(flag)) {
			condition.put("flag", flag);
		}
		
		//预约学习时间
		if ("1".equals(status)||"1".equals(flag)) {
			if (ExStringUtils.isNotEmpty(endTime)) {
				condition.put("orderCourseEndTime", endTime);
			}
			if (ExStringUtils.isNotEmpty(startTime)) {
				condition.put("orderCourseStartTime", startTime);
			}
			if (ExStringUtils.isNotEmpty(yearInfo)) {
				condition.put("orderCourseYearInfo", yearInfo);
			}
			if (ExStringUtils.isNotEmpty(term)) {
				condition.put("orderCourseTerm", term);
			}
		//预约考试时间	
		}else if("2".equals(status)){
			if (ExStringUtils.isNotEmpty(endTime)) {
				condition.put("orderExamEndTime", endTime);
			}
			if (ExStringUtils.isNotEmpty(startTime)) {
				condition.put("orderExamStartTime", startTime);
			}
			if (ExStringUtils.isNotEmpty(yearInfo)) {
				condition.put("orderExamYearInfo", yearInfo);
			}
			if (ExStringUtils.isNotEmpty(term)) {
				condition.put("orderExamTerm", term);
			}
		}

		page = courseOrderService.findCourseOrderByCondition(condition, page);
		model.put("page", page);
		model.put("condition", condition);
		
		return "/edu3/teaching/courseorder/courseorder-details";
	}
	/**
	 * 预约统计-导出预约明细
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/teaching/courseorder/courseOrder-details-export.html")
	public void exportStatCourseOrderDetails(HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> condition = new HashMap<String, Object>();
		
		String major        		 = ExStringUtils.trimToEmpty(request.getParameter("major"));
		String grade      		     = ExStringUtils.trimToEmpty(request.getParameter("grade"));
		String status        		 = ExStringUtils.trimToEmpty(request.getParameter("status"));
		String statid      		     = ExStringUtils.trimToEmpty(request.getParameter("statid"));
		String classic      		 = ExStringUtils.trimToEmpty(request.getParameter("classic"));
		String studyNo               = ExStringUtils.trimToEmpty(request.getParameter("studyNo"));
		String studentName           = ExStringUtils.trimToEmpty(request.getParameter("studentName"));
		String branchSchool 		 = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));
		String endTime     			 = ExStringUtils.trimToEmpty(request.getParameter("endTime"));
		String startTime    		 = ExStringUtils.trimToEmpty(request.getParameter("startTime"));
		String yearInfo              = ExStringUtils.trimToEmpty(request.getParameter("yearInfo"));    
		String term                  = ExStringUtils.trimToEmpty(request.getParameter("term"));
		String courseId              = ExStringUtils.trimToEmpty(request.getParameter("courseId"));
		String flag                  = ExStringUtils.trimToEmpty(request.getParameter("flag"));
		
		User user 					 = SpringSecurityHelper.getCurrentUser();
		if(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())) {
			 branchSchool  			 = user.getOrgUnit().getResourceid();
		}
		
		if (ExStringUtils.isNotEmpty(major)) {
			condition.put("major", major);
		}
		if (ExStringUtils.isNotEmpty(grade)) {
			condition.put("grade", grade);
		}
		if (ExStringUtils.isNotEmpty(status)) {
			condition.put("status", status);
		}
		if (ExStringUtils.isNotEmpty(statid)) {
			condition.put("statid", statid);
		}
		if (ExStringUtils.isNotEmpty(classic)) {
			condition.put("classic", classic);
		}
		if (ExStringUtils.isNotEmpty(studyNo)) {
			condition.put("studyNo", studyNo);
		}
		if (ExStringUtils.isNotEmpty(endTime)) {
			condition.put("endTime", endTime);
		}
		if (ExStringUtils.isNotEmpty(startTime)) {
			condition.put("startTime",startTime);
		}
		if (ExStringUtils.isNotEmpty(studentName)) {
			condition.put("studentName", studentName);
		}
		if (ExStringUtils.isNotEmpty(branchSchool)) {
			condition.put("branchSchool", branchSchool);
		}
		if (ExStringUtils.isNotEmpty(yearInfo)) {
			condition.put("yearInfo", yearInfo);
		}
		if (ExStringUtils.isNotEmpty(term)) {
			condition.put("term", term);
		}
		if (ExStringUtils.isNotEmpty(courseId)) {
			condition.put("courseId", courseId);
		}
		if (ExStringUtils.isNotEmpty(flag)) {
			condition.put("flag", flag);
		}
		
		//预约学习时间
		if ("1".equals(status)||"1".equals(flag)) {
			if (ExStringUtils.isNotEmpty(endTime)) {
				condition.put("orderCourseEndTime", endTime);
			}
			if (ExStringUtils.isNotEmpty(startTime)) {
				condition.put("orderCourseStartTime", startTime);
			}
			if (ExStringUtils.isNotEmpty(yearInfo)) {
				condition.put("orderCourseYearInfo", yearInfo);
			}
			if (ExStringUtils.isNotEmpty(term)) {
				condition.put("orderCourseTerm", term);
			}
		//预约考试时间	
		}else if("2".equals(status)){
			if (ExStringUtils.isNotEmpty(endTime)) {
				condition.put("orderExamEndTime", endTime);
			}
			if (ExStringUtils.isNotEmpty(startTime)) {
				condition.put("orderExamStartTime", startTime);
			}
			if (ExStringUtils.isNotEmpty(yearInfo)) {
				condition.put("orderExamYearInfo", yearInfo);
			}
			if (ExStringUtils.isNotEmpty(term)) {
				condition.put("orderExamTerm", term);
			}
		}

		List<CourseOrder> list = courseOrderService.findCourseOrderByCondition(condition);
		
		//文件输出服务器路径
		setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
		try{
			//导出
			File excelFile = null;
			GUIDUtils.init();
			File disFile = new File(getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(false) + ".xls");
			Map<String,Object> templateMap = new HashMap<String, Object>();//设置模板
			if ("1".equals(flag)) {
				status        = "1";
			}
			String orderType  = JstlCustomFunction.dictionaryCode2Value("CodeCourseOrderStatus",status);
			templateMap.put("orderType",orderType);
			//模板文件路径
			String templateFilepathString = "courseOrderDetails.xls";
			
			//初始化配置参数
			exportExcelService.initParmasByfile(disFile, "courseOrderDetails", list,null);
			exportExcelService.getModelToExcel().setTemplateParam(templateFilepathString, 2, templateMap);
			exportExcelService.getModelToExcel().setRowHeight(500);
			excelFile = exportExcelService.getExcelFile();//获取导出的文件

			downloadFile(response, orderType+"预约明细.xls", excelFile.getAbsolutePath(),true);
		}catch(Exception e){			
			logger.error("导出excel文件出错："+e.fillInStackTrace());
		}
		
	}
	
	/**
	 * 预约教材统计
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/teaching/courseorder/statBookOrder.html")
	public String statBookOrder(HttpServletRequest request,HttpServletResponse response,Page objPage,ModelMap model){
		objPage.setOrderBy("unit.UNITNAME,course.courseName");
		objPage.setOrder(Page.ASC);
		
		Map<String,Object> condition  = new HashMap<String, Object>();
		User user                     = SpringSecurityHelper.getCurrentUser();
		
		String branchSchool = ExStringUtils.defaultIfEmpty(request.getParameter("branchSchool"),"");
		String yearInfo     = ExStringUtils.defaultIfEmpty(request.getParameter("yearInfo"), "");
		//String classic      = ExStringUtils.defaultIfEmpty(request.getParameter("classic"), "");
		String course       = ExStringUtils.defaultIfEmpty(request.getParameter("course"), "");
		//String major        = ExStringUtils.defaultIfEmpty(request.getParameter("major"), "");
		String term         = ExStringUtils.defaultIfEmpty(request.getParameter("term"), "");
		String startTime    = ExStringUtils.trimToEmpty(request.getParameter("startTime"));
		String endTime    = ExStringUtils.trimToEmpty(request.getParameter("endTime"));
		
		if(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType()))
		{
			   branchSchool = user.getOrgUnit().getResourceid();
			   model.addAttribute("isBranchSchool", true);
		}
		if (ExStringUtils.isNotEmpty(branchSchool)) {
			condition.put("branchSchool", branchSchool);
		}
//		if (ExStringUtils.isNotEmpty(classic))      condition.put("classic", classic);
//		if (ExStringUtils.isNotEmpty(major))        condition.put("major", major);
		if (ExStringUtils.isNotEmpty(course)) {
			condition.put("course", course);
		}
		if (ExStringUtils.isNotEmpty(yearInfo)) {
			condition.put("yearInfo", yearInfo);
		}
		if (ExStringUtils.isNotEmpty(term)) {
			condition.put("term", term);
		}
		if (ExStringUtils.isNotEmpty(startTime)) {
			condition.put("startTime", startTime);
		}
		if (ExStringUtils.isNotEmpty(endTime)) {
			condition.put("endTime", endTime);
		}
		
		if (ExStringUtils.isNotEmpty(branchSchool) || ExStringUtils.isNotEmpty(term)||
				 ExStringUtils.isNotEmpty(course)||ExStringUtils.isNotEmpty(yearInfo)) {
//			Page page = teachingJDBCService.statBookOrder(condition,objPage);
//			model.addAttribute("statBookOrderPage", page);
		}		
		model.addAttribute("condition", condition);
		return "/edu3/teaching/courseorder/statBookOrder";
	}
	/**
	 * 申请缓考-列表
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/teaching/exam/delayExam-list.html")
	public String listExamDelay(HttpServletRequest request,HttpServletResponse response,ModelMap model,Page objPage){
		
		objPage.setOrderBy("exam.studentInfo.studyNo");
		objPage.setOrder(Page.DESC);
		
		User user                    = SpringSecurityHelper.getCurrentUser();
		Map<String,Object> condition = new HashMap<String,Object>();
		
		String branchSchool = ExStringUtils.defaultIfEmpty(request.getParameter("branchSchool"),"");
		String gradeid      = ExStringUtils.defaultIfEmpty(request.getParameter("gradeid"),"");
		String majorid      = ExStringUtils.defaultIfEmpty(request.getParameter("major"),"");
		String classic      = ExStringUtils.defaultIfEmpty(request.getParameter("classic"),"");
		String name         = ExStringUtils.defaultIfEmpty(request.getParameter("name"),"");
		String studyNo      = ExStringUtils.defaultIfEmpty(request.getParameter("studyNo"),"");
		
		if(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())){
			   branchSchool = user.getOrgUnit().getResourceid();
			   condition.put("isBranchSchool", true);
		}
		if (!"".equals(branchSchool)) {
			condition.put("branchSchool", branchSchool);
		}
		if (!"".equals(gradeid)) {
			condition.put("gradeid",gradeid);
		}
		if (!"".equals(majorid)) {
			condition.put("major",majorid);
		}
		if (!"".equals(classic)) {
			condition.put("classic",classic);
		}
		if (!"".equals(name)) {
			condition.put("name",name);
		}
		if (!"".equals(studyNo)) {
			condition.put("studyNo",studyNo);
		}
		

		Page resultPage = examResultsService.findExamDelayList(condition, objPage);
		
		model.addAttribute("condition", condition);
		model.addAttribute("resultPage", resultPage);
		
		return "/edu3/teaching/orderManager/examDelay-list";
	}
	/**
	 * 申请缓考-保存    
	 * @param examResultIds  学生考试成绩表ID
	 * @param operaterType
	 * @param branchSchool
	 * @param gradeid
	 * @param major
	 * @param classic
	 * @param name
	 * @param studyNo
	 * @param response
	 */
	@RequestMapping("/edu3/teaching/exam/delayExam-save.html")
	public void editExamDelay (String examResultIds ,String operaterType,String branchSchool,String gradeid,
							   String major,String classic,String name,String studyNo,HttpServletResponse response){
		
		boolean isSuccess = false;
		String msg = "";
		Map<String,Object> condition = new HashMap<String,Object>();
		
		ExStringUtils.trimToEmpty(branchSchool);  condition.put("branchSchool", branchSchool);
		ExStringUtils.trimToEmpty(gradeid);       condition.put("gradeid",gradeid);
		ExStringUtils.trimToEmpty(major);         condition.put("major",major);
		ExStringUtils.trimToEmpty(classic);       condition.put("classic",classic);
		ExStringUtils.trimToEmpty(name);          condition.put("name",name);
		ExStringUtils.trimToEmpty(studyNo);       condition.put("studyNo",studyNo);
		
		try {
			if (ExStringUtils.isNotEmpty(examResultIds) && ExStringUtils.isNotEmpty(operaterType)) {
				String[] ids = examResultIds.split(",");
				for(String id:ids) {
					
					ExamResults examResults = examResultsService.get(id);
					
					if (null==examResults.getExamSeatNum()) {//未按排座位之前允许申请缓考
						if ("examDelay".equals(operaterType)) {
							examResults.setIsDelayExam("Y");
						}else if("disExamDelay".equals(operaterType)) {
							examResults.setIsDelayExam("N");
						}
						examResultsService.saveOrUpdate(examResults);
					}else {
						msg = examResults.getStudentInfo().getStudentName()+"已按排座位，不允许申请缓考！";
						isSuccess = false;
						break;
					}
					
				}
			}
			isSuccess = true;
		} catch (Exception e) {
			logger.error("申请缓考-保存,出错：{}",e.fillInStackTrace());
			msg = "服务器异常,请联系管理员！";
			isSuccess = false;
		}
		condition.put("msg", msg);
		condition.put("isSuccess", isSuccess);
		renderJson(response, JsonUtils.mapToJson(condition));
	}
	/**
	 * 预约学习列表(未预约、已预约)
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/teaching/courseorder-list.html")
	public String listCourseOrder(HttpServletRequest request,ModelMap model,Page objPage){
		
		objPage.setOrderBy("studentInfo.studyNo");
		objPage.setOrder(Page.DESC);

		Map<String,Object> condition = new HashMap<String, Object>();
		String branchSchool		     = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));
		String yearInfo     	     = ExStringUtils.trimToEmpty(request.getParameter("yearInfo"));
		String gradeid      	     = ExStringUtils.trimToEmpty(request.getParameter("gradeid"));
		String classic      	     = ExStringUtils.trimToEmpty(request.getParameter("classic"));
		String studyNo      	     = ExStringUtils.trimToEmpty(request.getParameter("studyNo"));
		String course       	     = ExStringUtils.trimToEmpty(request.getParameter("courseId"));
		String major        	     = ExStringUtils.trimToEmpty(request.getParameter("major"));
		String name         	     = ExStringUtils.trimToEmpty(request.getParameter("name"));
		String term         		 = ExStringUtils.trimToEmpty(request.getParameter("term"));
		
		String searchOrderFlag       = ExStringUtils.trimToEmpty(request.getParameter("searchOrderFlag"));
		
		User user 					 = SpringSecurityHelper.getCurrentUser();
		if(user.getOrgUnit().getUnitType().indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员
			branchSchool = user.getOrgUnit().getResourceid();
			model.addAttribute("isBrschool", true);
		}

		if (ExStringUtils.isNotEmpty(branchSchool)) {
			condition.put("branchSchool",branchSchool);
		}
		if (ExStringUtils.isNotEmpty(yearInfo)) {
			condition.put("yearInfo",yearInfo);
		}
		if (ExStringUtils.isNotEmpty(studyNo)) {
			condition.put("studyNo",studyNo);
		}
		if (ExStringUtils.isNotEmpty(classic)) {
			condition.put("classic",classic);
		}
		if (ExStringUtils.isNotEmpty(gradeid)) {
			condition.put("gradeid",gradeid);
		}
		if (ExStringUtils.isNotEmpty(course)) {
			condition.put("courseId",course);
		}
		if (ExStringUtils.isNotEmpty(major)) {
			condition.put("major",major);
		}
		if (ExStringUtils.isNotEmpty(name)) {
			condition.put("name",name);
		}
		if (ExStringUtils.isNotEmpty(term)) {
			condition.put("term",term);
		}
		if (ExStringUtils.isEmpty(searchOrderFlag)) {
			searchOrderFlag = "Y";
		}
		
		if (!condition.isEmpty()) {
			if (Constants.BOOLEAN_NO.equals(searchOrderFlag)) {
				//objPage = teachingJDBCService.findNotCourseOrderStudentList(condition, objPage);
			}else if(Constants.BOOLEAN_YES.equals(searchOrderFlag)) {
				//condition.put("status", 1);
				condition.put("queryHQL", " and ( plan.status = 1 or  plan.status = 2 )");
				objPage = studentLearnPlanService.findStudentLearnPlanByCondition(condition, objPage);
			}
		}	
		
		condition.put("searchOrderFlag",searchOrderFlag);
		model.addAttribute("condition", condition);
		model.addAttribute("page", objPage);
		
		return "/edu3/teaching/orderManager/courseOrder-list";
	}
	
	
	
	/**
	 * 预约考试列表(未预约、已预约)
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/teaching/examorder-list.html")
	public String listExamOrder(HttpServletRequest request,ModelMap model,Page objPage){
		
		objPage.setOrderBy("studentInfo.studyNo");
		objPage.setOrder(Page.DESC);
		
		Map<String,Object> condition = new HashMap<String, Object>();
		String searchOrderFlag       = ExStringUtils.trimToEmpty(request.getParameter("searchOrderFlag"));
		String branchSchool 		 = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));
		String courseId   		 	 = ExStringUtils.trimToEmpty(request.getParameter("courseId"));
		String yearInfo     		 = ExStringUtils.trimToEmpty(request.getParameter("yearInfo"));
		String examTime     		 = ExStringUtils.trimToEmpty(request.getParameter("examTime"));
		String gradeid      		 = ExStringUtils.trimToEmpty(request.getParameter("gradeid"));
		String classic      		 = ExStringUtils.trimToEmpty(request.getParameter("classic"));
		String studyNo      		 = ExStringUtils.trimToEmpty(request.getParameter("studyNo"));
		String major        		 = ExStringUtils.trimToEmpty(request.getParameter("major"));
		String name         		 = ExStringUtils.trimToEmpty(request.getParameter("name"));
		String term         		 = ExStringUtils.trimToEmpty(request.getParameter("term"));

		User user 					 = SpringSecurityHelper.getCurrentUser();
		if(user.getOrgUnit().getUnitType().indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员
			branchSchool = user.getOrgUnit().getResourceid();
			model.addAttribute("isBrschool", true);
		}
		if (ExStringUtils.isEmpty(searchOrderFlag)) {
			searchOrderFlag=Constants.BOOLEAN_YES;      //默认查询已预约列表
		}
		if (ExStringUtils.isNotEmpty(branchSchool)) {
			condition.put("branchSchool",branchSchool);
		}
		if (ExStringUtils.isNotEmpty(courseId)) {
			condition.put("courseId",courseId);
		}
		if (ExStringUtils.isNotEmpty(yearInfo)) {
			condition.put("yearInfo",yearInfo);
		}
		if (ExStringUtils.isNotEmpty(examTime)) {
			condition.put("examTime",examTime);
		}
		if (ExStringUtils.isNotEmpty(studyNo)) {
			condition.put("studyNo",studyNo);
		}
		if (ExStringUtils.isNotEmpty(classic)) {
			condition.put("classic",classic);
		}
		if (ExStringUtils.isNotEmpty(gradeid)) {
			condition.put("gradeid",gradeid);
		}
		if (ExStringUtils.isNotEmpty(major)) {
			condition.put("major",major);
		}
		if (ExStringUtils.isNotEmpty(name)) {
			condition.put("name",name);
		}
		if (ExStringUtils.isNotEmpty(term)) {
			condition.put("term",term);
		}
		
		if (!condition.isEmpty()) {
			
			if (Constants.BOOLEAN_YES.equals(searchOrderFlag)) {	
				condition.put("status", 2);
				if (condition.containsKey("yearInfo")) {
					condition.put("examOrderYearInfo", condition.get("yearInfo"));       condition.remove("yearInfo");
				}
				if (condition.containsKey("term")) {
					condition.put("examOrderTerm",     condition.get("term"));           condition.remove("term");
				}
				objPage = studentLearnPlanService.findStudentLearnPlanByCondition(condition, objPage);
				
				condition.put("yearInfo",yearInfo);
				condition.put("term",term);
				
			}else if(Constants.BOOLEAN_NO.equals(searchOrderFlag)){
				String hql = " and plan.status=1 and plan.orderExamYear is null and plan.orderExamTerm is null";
				condition.put("queryHQL",hql);
				objPage = studentLearnPlanService.findStudentLearnPlanByCondition(condition, objPage);
			}
			
		}
		
		//查询已预约\未预约标识
		//if (condition.containsKey("examOrderYearInfo")) condition.remove("examOrderYearInfo");  
		//if (condition.containsKey("examOrderTerm"))     condition.remove("examOrderTerm");  
		
		condition.put("searchOrderFlag",searchOrderFlag);
		model.addAttribute("condition", condition);
		model.addAttribute("page", objPage);
		
		return "/edu3/teaching/orderManager/examOrder-list";
	}
	/**
	 * 预约计划外课程-列表
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/teaching/courseorder/outplancourse-list.html")
	public String orderOutPlanCourseList(HttpServletRequest request,HttpServletResponse response,ModelMap model, Page objPage){
	
		try {
			
			if(SpringSecurityHelper.isUserInRole("ROLE_STUDENT")){//如果为学生，查出学生的教学计划
				
				Map<String,Object> condition    = new HashMap<String, Object>();
				StringBuffer coursesIds         = new StringBuffer();
				String studentId 				= ExStringUtils.trimToEmpty(request.getParameter("studentId")); 
				String courseName               = ExStringUtils.trimToEmpty(request.getParameter("courseName")); 
				String creditHour				= ExStringUtils.trimToEmpty(request.getParameter("creditHour")); 
				String examType         		= ExStringUtils.trimToEmpty(request.getParameter("examType")); 
				
				if (ExStringUtils.isNotEmpty(studentId)) {
					condition.put("studentId", studentId);
				}
				if (ExStringUtils.isNotEmpty(courseName)) {
					condition.put("courseName", courseName);
				}
				if (ExStringUtils.isNotEmpty(creditHour)) {
					condition.put("creditHour", creditHour);
				}
				if (ExStringUtils.isNotEmpty(examType)) {
					condition.put("examType", examType);
				}
				
				StudentInfo studentInfo 		= studentInfoService.get(studentId);
				//学生的教学计划
				Set<TeachingPlanCourse> courses = studentInfo.getTeachingPlan().getTeachingPlanCourses();
				for (TeachingPlanCourse c: courses) {
					condition.put(c.getCourse().getResourceid(), c.getCourse().getResourceid());
				}
				
				List<StudentLearnPlan> learnplan=  studentLearnPlanService.getStudentPlanOutLearnPlanList(studentInfo.getResourceid());	
				for (StudentLearnPlan p:learnplan) {
					String outplanCouserId = null==p.getPlanoutCourse()?"":p.getPlanoutCourse().getResourceid();
					if (ExStringUtils.isNotEmpty(outplanCouserId)) {
						condition.put(outplanCouserId,outplanCouserId );
					}
				}

				for (Object id:condition.values()) {
					coursesIds.append(",'"+id.toString()+"'");
				}
				if (coursesIds.length()>0) {
					condition.put("coursesIds", coursesIds.toString().substring(1));
				}
			
				objPage = courseService.findCourseForStudentOutPlanCourse(condition,objPage);
				model.put("condition",condition);
				model.put("studentId", studentId);
				model.put("objPage", objPage);
			}
		} catch (Exception e) {
			logger.error("学生预约计划外课程-获取教学计划外课程列表出错：{}"+e.fillInStackTrace());
			model.put("errorMsg","获取计划外课程列表出错:"+e.getMessage());
		}
		
		return "/edu3/teaching/courseorder/outplancourse-list";
	}
	/**
	 * 预约计划外课程
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/teaching/courseorder/outplancourse.html")
	public void orderOutPlanCourseProcess(HttpServletRequest request,HttpServletResponse response){
		
		Map<String,Object> results = new HashMap<String, Object>();  
		String courseId 		   = ExStringUtils.trimToEmpty(request.getParameter("courseId"));
		String studentId 		   = ExStringUtils.trimToEmpty(request.getParameter("studentId"));
		String orderType           = ExStringUtils.trimToEmpty(request.getParameter("orderType"));
		StudentInfo info 		   = studentInfoService.get(studentId);
		TeachingPlan plan		   = null==info?null:info.getTeachingPlan();
		User user                  = SpringSecurityHelper.getCurrentUser();
		try {
			if (null!=info && null!= plan) {
				results            = courseOrderService.orderOutPlanCourse(info, courseId, orderType);
				boolean status     = (Boolean)results.get("operatingStatus");
				if (status) {
					//日志记录
					logger.info("预约类型:"+orderType+"   操作人："+user.getResourceid()+"   学号："+info.getStudyNo()+"   "+
								"预约时间:"+ExDateUtils.formatDateStr(new Date(), ExDateUtils.PATTREN_DATE_TIME)+"   "+
								"预约课程Id:"+courseId);
				}
				
			}else {
				throw new Exception("非法用户，未找到学籍或教学计划信息!");
			}
		} catch (Exception e) {
			logger.error("预约计划外课程出错:{}"+e.fillInStackTrace());
			results.put("operatingStatus",false);
			List<String> resultMapMsgList =  (List<String>) results.get("msg"); 
			if (null==resultMapMsgList) {
				List<String> msgList = new ArrayList<String>();
				msgList.add("预约计划外课程出错:"+e.getMessage());
				results.put("msg", msgList);
			}	
		}
		
		if (results.containsKey("orderCourseSetting")) {
			results.remove("orderCourseSetting");
		}
		if (results.containsKey("teachingPlan")) {
			results.remove("teachingPlan");
		}
		if (results.containsKey("studentInfo")) {
			results.remove("studentInfo");
		}
		if (results.containsKey("examResult")) {
			results.remove("examResult");
		}
		if (results.containsKey("learnPlan")) {
			results.remove("learnPlan");
		}
		if (results.containsKey("examSub")) {
			results.remove("examSub");
		}
		if (results.containsKey("course")) {
			results.remove("course");
		}
		
		renderJson(response,JsonUtils.mapToJson(results));
	}
	/**
	 * 学生在当前预约开放批次的预约结束时间内取消本次的预约课程-列表
	 * @param request
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/courseorder/student-order-manager.html")
	public String studentOrderManagerList(HttpServletRequest request,ModelMap model)throws WebException{
		
//		List<StudentLearnPlan> pL = new ArrayList<StudentLearnPlan>(); 
//		List<StudentLearnPlan> li = new ArrayList<StudentLearnPlan>(); 
//		String operateType 		  = ExStringUtils.trimToEmpty(request.getParameter("operateType"));
//		User currentUser          = SpringSecurityHelper.getCurrentUser();
//		Criterion[] criterions    = null;
//		String orderYearInfoId    = "";
//		String orderTerm          = "";
//
//		List<String> stuIdList 	  =  teachingJDBCService.findUserStudentInfoIdList(currentUser);
//		if (ExStringUtils.isEmpty(operateType)) operateType = "orderCourse";
//		if ("orderCourse".equals(operateType) && null != stuIdList && !stuIdList.isEmpty()) {
//			//获取开放的预约学习批次
//			List<OrderCourseSetting> list = orderCourseSettingService.findOpenedSetting();
//			if (null!=list && !list.isEmpty()) {
//				orderYearInfoId  = list.get(0).getYearInfo().getResourceid();		 //开放批次的年度对象ID
//				orderTerm        = list.get(0).getTerm();                   		 //开放批次的学期
//				
//				criterions       = new Criterion[5];
//				criterions[0] 	 = Restrictions.eq("yearInfo.resourceid", orderYearInfoId);
//				criterions[1] 	 = Restrictions.eq("term", orderTerm);
//				criterions[2] 	 = Restrictions.eq("status", 1);
//				criterions[3] 	 = Restrictions.in("studentInfo.resourceid", stuIdList);
//				criterions[4] 	 = Restrictions.eq("isDeleted", 0);
//				
//			}
//		}else if("orderExam".equals(operateType)  && null != stuIdList && !stuIdList.isEmpty()){
//			//获取开放的考试批次
//			List<ExamSub> subList= examSubService.findOpenedExamSub("exam");
//			if (null!=subList && !subList.isEmpty()) {
//				
//				orderYearInfoId  = subList.get(0).getYearInfo().getResourceid();//开放批次的年度对象ID
//				orderTerm        = subList.get(0).getTerm();					 //开放批次的学期
//				
//				criterions       = new Criterion[5];
//				criterions[0] 	 = Restrictions.eq("orderExamYear.resourceid", orderYearInfoId);
//				criterions[1] 	 = Restrictions.eq("orderExamTerm", orderTerm);
//				criterions[2] 	 = Restrictions.eq("status",2);
//				criterions[3] 	 = Restrictions.in("studentInfo.resourceid", stuIdList);
//				criterions[4] 	 = Restrictions.eq("isDeleted", 0);
//			}
//		}else if("orderGraduatePaper".equals(operateType) ) {
//			//获取开放的考试批次
//			List<ExamSub> subList= examSubService.findOpenedExamSub("thesis");
//			if (null!=subList && !subList.isEmpty()) {
//				
//				orderYearInfoId  = subList.get(0).getYearInfo().getResourceid();//开放批次的年度对象ID
//				orderTerm        = subList.get(0).getTerm();	
//				
//				criterions       = new Criterion[5];
//				criterions[0] 	 = Restrictions.eq("yearInfo.resourceid", orderYearInfoId);
//				criterions[1] 	 = Restrictions.eq("term", orderTerm);
//				criterions[2] 	 = Restrictions.eq("status", 1);
//				criterions[3] 	 = Restrictions.in("studentInfo.resourceid", stuIdList);
//				criterions[4] 	 = Restrictions.eq("isDeleted", 0);
//				
//			}
//		}
//		
//		//没有开放的预约批次时不查询数据
//		if (null!=criterions && criterions.length>0) {
//			 pL = studentLearnPlanService.findByCriteria(criterions);
//			 if ("orderCourse".equals(operateType)) {
//				for (StudentLearnPlan plan:pL) {
//					if (null!=plan.getTeachingPlanCourse()&&"thesis".equals(plan.getTeachingPlanCourse().getCourseType())) {
//						li.add(plan);
//					}
//				}
//				
//			 }else if("orderGraduatePaper".equals(operateType)){
//				 for (StudentLearnPlan plan:pL) {
//					if (!"thesis".equals(plan.getTeachingPlanCourse().getCourseType())) {
//						li.add(plan);
//					}
//				 }
//			}
//			pL.removeAll(li);
//		}
//		model.put("operateType",operateType);
//		model.put("studentLearnPlanList", pL);
//		
		return "/edu3/teaching/courseorder/studentordermanager-list";
	}
	/**
	 * 学生在当前预约开放批次的预约结束时间内取消本次的预约课程
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/courseorder/student-order-del.html")
	public void studentOrderManagerOperate(HttpServletRequest request,HttpServletResponse response)throws WebException{
		
		Map<String,Object> condition = new HashMap<String, Object>();
		String operateType 		     = ExStringUtils.trimToEmpty(request.getParameter("operateType"));
		String stuLearnPlanId        = ExStringUtils.trimToEmpty(request.getParameter("stuLearnPlanId"));
		User user                    = SpringSecurityHelper.getCurrentUser();
		boolean isSuccess            = false;
		try {
			//要删除的学习计划ID不为空
			if (ExStringUtils.isNotEmpty(stuLearnPlanId)) {
				
				StudentLearnPlan learnPlan = studentLearnPlanService.get(stuLearnPlanId);
				String studNo     		   = learnPlan.getStudentInfo().getStudyNo();
				String courseName 		   = null==learnPlan.getTeachingPlanCourse()?learnPlan.getPlanoutCourse().getCourseName():learnPlan.getTeachingPlanCourse().getCourse().getCourseName();
				if ("orderCourse".equals(operateType)) {
					
					List<OrderCourseSetting> list = orderCourseSettingService.findOpenedSetting();//获取开放的预约学习批次
					condition.put("orderCourseSetting", list);
					
					
					//些功能为后台管理员执行操作,删除时间为批次预约结束时间+7天
					condition.put("operater", "student");
					condition.put("learnPlanId", stuLearnPlanId);
					condition.put("learnPlan", learnPlan);
					
					condition = courseOrderService.delCourseOrderByLearnPlanId(condition);
					//日志记录
					logger.info("操作类型:删除课程预约   操作人："+user.getResourceid()+"   学号："+studNo+"   "+
								"操作时间:"+ExDateUtils.formatDateStr(new Date(), ExDateUtils.PATTREN_DATE_TIME)+"   "+
								"删除课程:"+courseName);
							   
					
				
				}else if("orderExam".equals(operateType)){
					
					List<ExamSub> subList	   = examSubService.findOpenedExamSub("exam");//获取开放的考试批次
					
					condition.put("subList", subList);
					condition.put("operater", "student");      //些功能为学生自己执行操作,删除时间为批次预约结束时间
					condition.put("learnPlan", learnPlan);
					condition                  = courseOrderService.delExamOrderByLearnPlanId(condition);
					
					//日志记录
					logger.info("操作类型:删除考试预约   操作人："+user.getResourceid()+"   学号："+studNo+"   "+
							    "操作时间:"+ExDateUtils.formatDateStr(new Date(), ExDateUtils.PATTREN_DATE_TIME)+"   "+
								"删除课程:"+courseName);
					
				}else if("orderGraduatePaper".equals(operateType)){
					List<ExamSub> subList	   = examSubService.findOpenedExamSub("thesis");//获取开放的论文批次
					condition.put("subList", subList);
					condition.put("learnPlan", learnPlan);
					condition                  = courseOrderService.delGraduatePaperByLearnPlanId(condition);
					
					//日志记录
					logger.info("操作类型:删除论文   操作人："+user.getResourceid()+"   学号："+studNo+"   "+
							    "操作时间:"+ExDateUtils.formatDateStr(new Date(), ExDateUtils.PATTREN_DATE_TIME)+"   "+
								"删除课程:"+courseName);
				}
				if(!condition.containsKey("msg")){
					isSuccess = true;
				}
				
				
			//要删除的学习计划ID为空	
			}else {
				condition=setResultMapMsg(condition,"请传入一个要删除的预约记录!");
				isSuccess = false;
			}
			
		} catch (Exception e) {
			
			try {
				logger.error("学生取消预约出错：{}"+e.fillInStackTrace());
				//日志记录
				logger.info("操作类型:学生取消预约   操作人："+user.getResourceid()+"   要取消的学习计划ID: "+stuLearnPlanId+"  操作时间: "+ExDateUtils.formatDateStr(new Date(), ExDateUtils.PATTREN_DATE_TIME));
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			condition=setResultMapMsg(condition,"学生取消预约出错："+e.fillInStackTrace());
			isSuccess = false;
		}
		if (condition.containsKey("course")) {
			condition.remove("course");
		}
		if (condition.containsKey("subList")) {
			condition.remove("subList");
		}
		if (condition.containsKey("learnPlan")) {
			condition.remove("learnPlan");
		}
		if (condition.containsKey("orderCourseSetting")) {
			condition.remove("orderCourseSetting");
		}
		
		condition.put("operateType",operateType);
		condition.put("isSuccess", isSuccess);

		renderJson(response, JsonUtils.mapToJson(condition));
	}
	
	/**
	 * 设置返回的resultMap的消息数组
	 * @param resultMap
	 * @param msg
	 * @return
	 */
	private Map<String,Object> setResultMapMsg(Map <String,Object> resultMap,String msg){
		
		if (null==resultMap) {
			resultMap = new HashMap<String, Object>();
		}
		   
		    List<String> msgList =  (List<String>) resultMap.get("msg"); 

	        if (null==msgList) {
	        	msgList = new ArrayList<String>() ;
	        	msgList.add(msg);
				resultMap.put("msg",msgList);
			}else {
				msgList.add(msg);
				resultMap.put("msg",msgList);
			}
	        
			return resultMap;
	}
	
}
