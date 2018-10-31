package com.hnjk.edu.teaching.controller;

import java.util.ArrayList;
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

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.FileUploadAndDownloadSupportController;
import com.hnjk.edu.basedata.model.StudentBaseInfo;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.roll.service.IGraduationQualifService;
import com.hnjk.edu.roll.service.IRollJDBCService;
import com.hnjk.edu.roll.service.IStudentInfoService;
import com.hnjk.edu.teaching.model.AbnormalExam;
import com.hnjk.edu.teaching.service.IAbnormalExamService;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.Attachs;
import com.hnjk.platform.system.service.IAttachsService;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;

/**
 * 学生异常成绩-缓考申请表
 * <code>AbnormalExamController</code>
 * 
 * @author Zik，广东学苑教育发展有限公司
 * @since 2015-06-17 上午 11:41:33
 * @see
 * @version 1.0
 */
@Controller
public class AbnormalExamController extends FileUploadAndDownloadSupportController{

	private static final long serialVersionUID = -8005263970194207424L;
	
	@Autowired
	@Qualifier("abnormalExamService")
	private IAbnormalExamService abnormalExamService;
	
	@Autowired
	@Qualifier("studentinfoservice")
	private IStudentInfoService studentInfoService;
	
	@Autowired
	@Qualifier("graduationQualifService")
	private IGraduationQualifService graduationQualifService;
	
	@Autowired
	@Qualifier("rollJDBCService")
	private IRollJDBCService rollJDBCService;
	
	@Autowired
	@Qualifier("attachsService")
	private IAttachsService attachsService;
	
	/**
	 * 符合申请缓考条件的学生列表
	 * 
	 * @param request
	 * @param model
	 * @param objPage
	 * @return
	 */
	@RequestMapping("/edu3/teaching/abnormalExam/abnormalExam-brschool-stu-list.html")
	public String abnorExamStuList(HttpServletRequest request, ModelMap model, Page objPage) 
			throws WebException{
		objPage.setOrderBy("grade.yearInfo.firstYear");
		objPage.setOrder(Page.DESC);//设置默认排序方式
		//查询条件
		Map<String,Object> condition = new HashMap<String,Object>(20);
		Map<String,Object> condition_temp = new HashMap<String,Object>(20);
		try {
			String abnormalExamSchool 		 = ExStringUtils.trimToEmpty(request.getParameter("abnormalExamSchool"));//学习中心
			String abnormalExamMajor 	             = ExStringUtils.trimToEmpty(request.getParameter("abnormalExamMajor"));//专业
			String abnormalExamClassic 				 = ExStringUtils.trimToEmpty(request.getParameter("abnormalExamClassic"));//层次
			String abnormalExamStatus 			 = ExStringUtils.trimToEmpty(request.getParameter("abnormalExamStatus"));//学籍状态
			String abnormalExamName 				 = ExStringUtils.trimToEmpty(request.getParameter("abnormalExamName"));//姓名
			String abnormalExamStudyNo				 = ExStringUtils.trimToEmpty(request.getParameter("abnormalExamStudyNo"));//学号
			String abnormalExamGrade				 = ExStringUtils.trimToEmpty(request.getParameter("abnormalExamGrade"));
			String abnormalExamCertNum 				 = ExStringUtils.trimToEmpty(request.getParameter("abnormalExamCertNum"));
			
			User user 					 = SpringSecurityHelper.getCurrentUser();
			//如果为校外学习中心人员
			if(user.getOrgUnit().getUnitType().indexOf(
					CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){
				abnormalExamSchool = user.getOrgUnit().getResourceid();
				model.addAttribute("isBrschool", true);
			}
			if (ExStringUtils.isBlank(abnormalExamStatus)){
				abnormalExamStatus = "11";
			}
			if(ExStringUtils.isNotEmpty(abnormalExamSchool)) {
				condition.put("branchSchool", abnormalExamSchool);
				condition_temp.put("abnormalExamSchool", abnormalExamSchool);
			}
			if(ExStringUtils.isNotEmpty(abnormalExamMajor)) {
				condition.put("major", abnormalExamMajor);
				condition_temp.put("abnormalExamMajor", abnormalExamMajor);
			}
			if(ExStringUtils.isNotEmpty(abnormalExamClassic))  {
				condition.put("classic", abnormalExamClassic);
				condition_temp.put("abnormalExamClassic", abnormalExamClassic);
			}
			if(ExStringUtils.isNotEmpty(abnormalExamStatus)) {
				condition.put("stuStatus", abnormalExamStatus);
				condition_temp.put("abnormalExamStatus", abnormalExamStatus);
			}
			if(ExStringUtils.isNotEmpty(abnormalExamName))  {
				condition.put("name", abnormalExamName);
				condition_temp.put("abnormalExamName", abnormalExamName);
			}
			if(ExStringUtils.isNotEmpty(abnormalExamStudyNo)) {
				condition.put("studyNo", abnormalExamStudyNo);
				condition_temp.put("abnormalExamStudyNo", abnormalExamStudyNo);
			}
			if(ExStringUtils.isNotEmpty(abnormalExamGrade)) {
				condition.put("stuGrade", abnormalExamGrade);
				condition_temp.put("abnormalExamGrade", abnormalExamGrade);
			}
			if(ExStringUtils.isNotEmpty(abnormalExamCertNum)){
				condition.put("certNum", abnormalExamCertNum);
				condition_temp.put("abnormalExamCertNum", abnormalExamCertNum);
			}
			
			Page page = studentInfoService.findStuByParam(condition, objPage);		

			model.addAttribute("stulist", page);
			condition.clear();
			model.addAttribute("condition", condition_temp);
			model.addAttribute("abnormalExamCascadeMajor",graduationQualifService.getGradeToMajor1(abnormalExamGrade,
					abnormalExamMajor,"query_abnormalExam_major","abnormalExamMajor","",abnormalExamClassic));
		} catch (ServiceException e) {
			logger.error("符合申请缓考条件的学生列表出错：", e);
		}
		
		return "/edu3/teaching/abnormalExam/brschool-abnormalExam-stu-list";
	}
	
	/**
	 * 学生的教学计划课程列表
	 * 
	 * @param studentId
	 * @param request
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/edu3/teaching/abnormalExam/abnormalExam-brschool-form.html")
	public String abnormalExamPlancourseList(String studentId,HttpServletRequest request,ModelMap model) 
			throws WebException{
		try {
			if (ExStringUtils.isNotBlank(studentId)) {
				List<Attachs> attachList      = null;
				List<Attachs> attList 				  = null;
				StringBuffer ids              = new StringBuffer();   
				Map<String,List<Attachs>> attMap       = new HashMap<String,List<Attachs>>();  
				StudentInfo studentInfo       = studentInfoService.get(studentId);
				StudentBaseInfo stb =  studentInfo.getStudentBaseInfo();
				List<Map<String,Object>> planCousreList = new ArrayList<Map<String,Object>>();
				if(null!=stb){
					planCousreList = rollJDBCService.findPlanCourseForAbnormalExam(studentId);
				}
				User user                     = SpringSecurityHelper.getCurrentUser();
				model.put("user", user);
				model.put("AEstudentInfo", studentInfo);
				model.put("planCousreList", planCousreList);
				String schoolCode = CacheAppManager.getSysConfigurationByCode("graduateData.schoolCode").getParamValue();
				
				for (Map<String,Object> map :planCousreList) {
					String abnormalexamid = null==map.get("abnormalexamid")?"":map.get("abnormalexamid").toString();
					if (ExStringUtils.isNotBlank(abnormalexamid)) {
						ids.append(",'"+abnormalexamid+"'");
						attMap.put(abnormalexamid, null);
					}
				}
				if (ExStringUtils.isNotBlank(ids.toString())) {
					attachList     			  = attachsService.findByHql(" from "+Attachs.class.getSimpleName()+" where isDeleted = ? and formType=? and formId in("+ids.toString().substring(1)+") ",0,"ABNORMALEXAMAPPLY");
					for (Attachs att : attachList) {
						if (null==attMap.get(att.getFormId())) {
							attList = new ArrayList<Attachs>();
							attList.add(att);
						}else {
							attList = attMap.get(att.getFormId());
							attList.add(att);
						}
						attMap.put(att.getFormId(),attList );
					}
				}
				model.addAttribute("schoolCode", schoolCode);
				model.addAttribute("attMap", attMap);
			}
		} catch (Exception e) {
			logger.error("申请缓考,查找学生的教学计划课程出错：", e);
		}
		
		return "/edu3/teaching/abnormalExam/brschool-abnormalExam-form";
	}
	
	/**
	 *  保存申请异常成绩（目前只用于缓考）
	 *  
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/abnormalExam/abnormalExam-brschool-save.html")
	public void saveAbnormalExam(HttpServletRequest request,HttpServletResponse response) 
			throws WebException {
		
		Map<String,Object> map = new HashMap<String, Object>(20);
		// 获取参数
		String[] courseTypes   = request.getParameterValues("courseTypes[]");
		String[] abnormalTypes     = request.getParameterValues("abnormalTypes[]");
		String[] courseIds 	   = request.getParameterValues("courseIds[]");
		String[] planCourseIds 	   	   = request.getParameterValues("planCourseIds[]");
		String[] abnormalExamIds      = request.getParameterValues("abnormalExamIds[]");
		String[] scores   = request.getParameterValues("scores[]");
		String[] AEexamTypes        = request.getParameterValues("AEexamTypes[]"); 
		String[] reasons        = request.getParameterValues("reasons[]"); 
		
		String studentId 	   = request.getParameter("studentId");
		
		try {
			if (null!=courseIds && null!=planCourseIds && ExStringUtils.isNotBlank(studentId)) {
				abnormalExamService.saveAbnormalExamApply(map, courseTypes, abnormalTypes, courseIds, 
						planCourseIds, abnormalExamIds, scores, AEexamTypes, reasons, studentId);
			}else {
				map.put("statusCode", 300);
				map.put("message", "保存失败，请选择学生及要申请的课程!<br/>");
			}
		}catch (Exception e) {
			logger.error("保存出错：",e);
			map.put("statusCode", 300);
			map.put("message", "保存失败!<br/>");
		}
		renderJson(response, JsonUtils.mapToJson(map));
		
	}
	
	/**
	 * 单个或批量撤销缓考申请--即删除（物理删除）
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/abnormalExam/revoke1.html")
	public void revokeAbnormalExam (HttpServletRequest request,HttpServletResponse response,ModelMap model) 
			throws WebException{
		String abnormalExamIds 	    = request.getParameter("resourceid");
		String type 	    = request.getParameter("type");
		Map<String ,Object> map = new HashMap<String, Object>(10);
		String statusCode = "200";
		String message = "撤销成功";
		try {
			if(ExStringUtils.isNotEmpty(abnormalExamIds)){
				User user = SpringSecurityHelper.getCurrentUser();
				// 状态为未审核才能撤销
				Map<String ,Object> returnMap = abnormalExamService.batchRevoke(abnormalExamIds.split("\\,"), user);
				
				if(returnMap != null && !returnMap.isEmpty()){
					statusCode = (String)returnMap.get("statusCode");
					message = (String)returnMap.get("message");
					if(ExStringUtils.isEmpty(message)) {
						message = "撤销成功";
					}
				} 	
				if(!"Single".equals(type)){
					map.put("forward", request.getContextPath()+"/edu3/teaching/abnormalExam/list.html");
				}
				// TODO:以后如果把申请缓考这个功能权限开放给学生，则会给学生发送撤销的信息
			}
		} catch (Exception e) {
			logger.error("撤销缓考申请出错:",e);
			statusCode ="300";
			message = "撤销缓考申请失败！";
		} finally {
			map.put("statusCode", statusCode);
			map.put("message", message);
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}

	/**
	 * 获取缓考申请列表
	 * 
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/abnormalExam/list.html")
	public String auditAbnormalExamList(HttpServletRequest request,Page objPage, ModelMap model) 
			throws WebException{
		
		objPage.setOrderBy("applyDate");
		objPage.setOrder(Page.DESC);//设置默认排序方式
		Map<String,Object> condition = new HashMap<String,Object>(20);//查询条件
		
		String abnormalExamListSchool = ExStringUtils.trimToEmpty(request.getParameter("abnormalExamListSchool"));
		String abnormalExamListGrade = ExStringUtils.trimToEmpty(request.getParameter("abnormalExamListGrade"));
		String abnormalExamListMajor = ExStringUtils.trimToEmpty(request.getParameter("abnormalExamListMajor")); 
		String abnormalExamListName = ExStringUtils.trimToEmpty(request.getParameter("abnormalExamListName")); 
		String abnormalExamListClassic = ExStringUtils.trimToEmpty(request.getParameter("abnormalExamListClassic")); 
		String abnormalExamListCheckStatus = ExStringUtils.trimToEmpty(request.getParameter("abnormalExamListCheckStatus"));
		String abnormalExamListStuNo = ExStringUtils.trimToEmpty(request.getParameter("abnormalExamListStuNo"));
		String abnormalExamListAppStartTime = ExStringUtils.trimToEmpty(request.getParameter("abnormalExamListAppStartTime"));
		String abnormalExamListAppEndTime = ExStringUtils.trimToEmpty(request.getParameter("abnormalExamListAppEndTime"));
		String abnormalExamListAuditStartTime = ExStringUtils.trimToEmpty(request.getParameter("abnormalExamListAuditStartTime"));
		String abnormalExamListAuditEndTime = ExStringUtils.trimToEmpty(request.getParameter("abnormalExamListAuditEndTime"));
		
		String return_url = "/edu3/teaching/abnormalExam/abnormalExam-list";
		User user = SpringSecurityHelper.getCurrentUser();
		//招生人员--学习中心
		if(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())){
			abnormalExamListSchool = user.getOrgUnit().getResourceid();
			condition.put("brshSchool", "Y");
		}
		if(ExStringUtils.isNotEmpty(abnormalExamListSchool)) {
			condition.put("abnormalExamListSchool", abnormalExamListSchool);
		}
		if(ExStringUtils.isNotEmpty(abnormalExamListGrade)) {
			condition.put("abnormalExamListGrade", abnormalExamListGrade);
		}
		if(ExStringUtils.isNotEmpty(abnormalExamListMajor)) {
			condition.put("abnormalExamListMajor", abnormalExamListMajor);
		}
		if(ExStringUtils.isNotEmpty(abnormalExamListName)) {
			condition.put("abnormalExamListName", abnormalExamListName);
		}
		if(ExStringUtils.isNotEmpty(abnormalExamListClassic)) {
			condition.put("abnormalExamListClassic", abnormalExamListClassic);
		}
		if(ExStringUtils.isNotEmpty(abnormalExamListCheckStatus)) {
			condition.put("abnormalExamListCheckStatus", abnormalExamListCheckStatus);
		}
		if(ExStringUtils.isNotEmpty(abnormalExamListStuNo)) {
			condition.put("abnormalExamListStuNo", abnormalExamListStuNo);
		}
		if(ExStringUtils.isNotEmpty(abnormalExamListAppStartTime)) {
			condition.put("abnormalExamListAppStartTime", abnormalExamListAppStartTime);
		}
		if(ExStringUtils.isNotEmpty(abnormalExamListAppEndTime)) {
			condition.put("abnormalExamListAppEndTime", abnormalExamListAppEndTime);
		}
		if(ExStringUtils.isNotEmpty(abnormalExamListAuditStartTime)) {
			condition.put("abnormalExamListAuditStartTime", abnormalExamListAuditStartTime);
		}
		if(ExStringUtils.isNotEmpty(abnormalExamListAuditEndTime)) {
			condition.put("abnormalExamListAuditEndTime", abnormalExamListAuditEndTime);
		}
		//如果为学生，则跳转学生页面
		if(CacheAppManager.getSysConfigurationByCode("sysuser.usertype.student").getParamValue().equals(user.getUserType())){
			condition.put("userId",user.getResourceid());
			/*if(null != user.getUserExtends() && null != user.getPropertys(UserExtends.USER_EXTENDCODE_DEFAULTROLLID)){
				condition.put("studentId",user.getPropertys(UserExtends.USER_EXTENDCODE_DEFAULTROLLID).getExValue());
			}else{
				StudentInfo stu = studentInfoService.findUniqueByProperty("sysUser.resourceid", user.getResourceid());
				condition.put("studentId",stu.getResourceid());
			}*/
			return_url ="/edu3/teaching/abnormalExam/abnormalExam-stu-list";
		}
		
		Page page = abnormalExamService.findAbnormalExamByCondition(condition, objPage);
		model.addAttribute("condition", condition);
		model.addAttribute("applyList", page);
		model.addAttribute("abnormalExamListCascadeMajor",graduationQualifService.getGradeToMajor1(abnormalExamListGrade,
				abnormalExamListMajor,"query_abnormalExamList_major","abnormalExamListMajor","",abnormalExamListClassic));
	
		return return_url;
	}
	
	/**
	 * 编辑以及查看缓考申请信息
	 * 
	 * @param abnormalExamId
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/abnormalExam/saveOrEdit.html")
	public String abnormalExamEdit(HttpServletRequest request,HttpServletResponse response,ModelMap model) 
			throws WebException{

		String isView = request.getParameter("view");
		String abnormalExamId = request.getParameter("resourceid");
		String selectGrageId = request.getParameter("selectGrageId");
		String return_url = "/edu3/teaching/abnormalExam/abnormalExam-form";
		User user = SpringSecurityHelper.getCurrentUser();
		// sysuser.usertype.student
		if(CacheAppManager.getSysConfigurationByCode("sysuser.usertype.student").getParamValue().equals(user.getUserType())){
			model.addAttribute("isStudent", true);
		}
		//增加上传附件的支持
		List<Attachs> attachList = attachsService.findByHql("from "+Attachs.class.getName()+ " where isDeleted=0 and formId = ? order by uploadTime desc", abnormalExamId);
		model.addAttribute("attachList", attachList);		
		if(ExStringUtils.isNotBlank(abnormalExamId)){ // 编辑或查看
			AbnormalExam abnormalExam = abnormalExamService.get(abnormalExamId);	
			if(ExStringUtils.isNotEmpty(isView) && "Y".equals(isView)){// 查看
				return_url = "/edu3/teaching/abnormalExam/abnormalExam-view";
			}
			model.addAttribute("abnormalExam", abnormalExam);
		}else{ // 新增			
			String more = request.getParameter("more");
			// 存在一个学生用户在不同的年级
			Map<String,Object> params = new HashMap<String, Object>(10);
			params.put("userId", user.getResourceid());
			if(ExStringUtils.isNotEmpty(selectGrageId)){
				params.put("selectGrageId", selectGrageId);
			}
			List<StudentInfo> studentInfoList = studentInfoService.findByUserAndGrage(params);
			if(studentInfoList != null && !studentInfoList.isEmpty()) {
				if(studentInfoList.size()>1){
					more = "Y";// 有两个学籍
					model.addAttribute("abnormalExam", new AbnormalExam(new StudentInfo()));	
				} else {
					model.addAttribute("abnormalExam", new AbnormalExam(studentInfoList.get(0)));			
				}
			}
			if(ExStringUtils.isNotEmpty(more) && "Y".equals(more)){
				if(studentInfoList.size() < 2){
					params.clear();
					params.put("userId", user.getResourceid());
					studentInfoList = studentInfoService.findByUserAndGrage(params);
				}
				model.addAttribute("studentInfoList", studentInfoList);
			}
			model.addAttribute("more", more);// 有两个学籍
		}
		
		return return_url;
	}
	
	/**
	 * 审核通过
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/abnormalExam/past.html")
	public void abnormalExamPast(HttpServletRequest request,HttpServletResponse response,ModelMap model) 
			throws WebException{
		
		String resourceid 			= request.getParameter("resourceid");
		User user 					= SpringSecurityHelper.getCurrentUser();
		Map<String ,Object> map 	= new HashMap<String, Object>(10);
		String statusCode = "200";
		String message = "审核成功";
		
		try {
			if(ExStringUtils.isNotEmpty(resourceid)){
				String[] applyids = resourceid.split("\\,");
				Map<String ,Object> returnMap = abnormalExamService.batchAudit(applyids, user, AbnormalExam.ABNORMALEXAM_CHECKSTATUS_PAST);
				if(returnMap != null && !returnMap.isEmpty()){
					statusCode = (String)returnMap.get("statusCode");
					message = (String)returnMap.get("message");
				} 
			}
		} catch (Exception e) {
			logger.error("审核通过出错:",e);
			statusCode = "300";
			message = "审核通过失败!";
		} finally {
			map.put("statusCode", statusCode);				
			map.put("message", message);	
			map.put("forward", request.getContextPath()+"/edu3/teaching/abnormalExam/list.html");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 审核不通过
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/abnormalExam/noPast.html")
	public void abnormalExamNoPast(HttpServletRequest request,HttpServletResponse response,ModelMap model) 
			throws WebException{

		String resourceid 			= request.getParameter("resourceid");
		Map<String ,Object> map 	= new HashMap<String, Object>(10);
		String statusCode = "200";
		String message = "审核成功";
		try {
			if(ExStringUtils.isNotBlank(resourceid)){
				User user 					= SpringSecurityHelper.getCurrentUser();
				String[] applyids = resourceid.split("\\,");
				Map<String ,Object> returnMap = abnormalExamService.batchAudit(applyids, user, AbnormalExam.ABNORMALEXAM_CHECKSTATUS_NOPAST);
				if(returnMap != null && !returnMap.isEmpty()){
					statusCode = (String)returnMap.get("statusCode");
					message = (String)returnMap.get("message");
				} 
			}
		} catch (Exception e) {
			logger.error("审核不通过出错:",e);
			map.put("statusCode", 300);
			map.put("message", "审核不通过失败！");
		} finally {
			map.put("statusCode", statusCode);				
			map.put("message", message);	
			map.put("forward", request.getContextPath()+"/edu3/teaching/abnormalExam/list.html");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 保存/更新缓考申请--单个
	 * @param noExamApply
	 * @param request
	 * @param response
	 * @param model
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/abnormalExam/save.html")
	public void saveStudentAbnormalExam(AbnormalExam abnormalExam,HttpServletRequest request,
			HttpServletResponse response,ModelMap model) throws WebException{

		Map<String,Object> map = new HashMap<String, Object>(10);
		String studentId = request.getParameter("studentId");
		String courseId = request.getParameter("courseId");
		String planCourseId = request.getParameter("planCourseId");
		User user = SpringSecurityHelper.getCurrentUser();
		String statusCode = "200";
		String message = "保存成功";
		try {
			if(ExStringUtils.isNotEmpty(studentId) && ExStringUtils.isNotEmpty(courseId) 
					&& ExStringUtils.isNotEmpty(planCourseId)){
				Map<String,Object> returnMap = abnormalExamService.saveAbnormalExamApply(abnormalExam, studentId, 
																	courseId, planCourseId, user);
				if(returnMap != null && !returnMap.isEmpty()){
					statusCode = (String)returnMap.get("statusCode");
					message = (String)returnMap.get("message");
				} 
			}
		}catch (Exception e) {
			logger.error("保存缓考申请出错",e);
			statusCode = "300";
			message = "保存失败!";
		} finally {
			map.put("statusCode", statusCode);
			map.put("message", message);
			map.put("reloadUrl", request.getContextPath() +"/edu3/teaching/abnormalExam/saveOrEdit.html?resourceid="
						+ abnormalExam.getResourceid());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
}
