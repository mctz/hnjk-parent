package com.hnjk.edu.teaching.controller;

import java.util.HashMap;
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
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.FileUploadAndDownloadSupportController;
import com.hnjk.edu.roll.service.IStudentInfoService;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;
import com.hnjk.security.service.IUserService;

/** 
 * 教学过程管理
 * 处理教学过程中的综合性问题
 * @author Zik, 广东学苑教育发展有限公司
 * @since Jul 20, 2016 9:47:58 AM 
 * 
 */
@Controller
public class TeachingManagementController extends FileUploadAndDownloadSupportController {

	private static final long serialVersionUID = 2295251521478276430L;
	
	@Qualifier("userService")
	@Autowired
	private IUserService userService;
	
	@Qualifier("studentinfoservice")
	@Autowired
	private IStudentInfoService studentinfoservice;
	
	
	/**
	 * 根据权限列出老师在线时长
	 * @param request
	 * @param response
	 * @param model
	 * @param objPage
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/teachingManagement/loginLongList_teacher.html")
	public String listTeacherLoginLong(HttpServletRequest request, HttpServletResponse response, ModelMap model, Page objPage) throws WebException {
		objPage.setOrderBy(" u.orgUnit.resourceid ");
		objPage.setOrder(Page.ASC);
		
		Map<String, Object> condition = new HashMap<String, Object>();
		String unitId = request.getParameter("unitId");
		String userName = request.getParameter("userName");
		String chinessName = request.getParameter("chinessName");
		
		User user = SpringSecurityHelper.getCurrentUser();
		if(user.getOrgUnit().getUnitType().indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){
			unitId = user.getOrgUnit().getResourceid();
			model.addAttribute("unitName", user.getOrgUnit().getUnitName());
			model.addAttribute("isManager", "Y");
		} else {
			if(!"administrator".equals(user.getUsername())){
				condition.put("addsql", " and u.username!='administrator' ");
			}
		}
		
		
		if(ExStringUtils.isNotEmpty(unitId)){
			condition.put("unitId", unitId);
		}
		if(ExStringUtils.isNotEmpty(userName)){
			condition.put("userName", userName);
		}
		if(ExStringUtils.isNotEmpty(chinessName)){
			condition.put("chinessName", chinessName);
		}
		condition.put("userType", "edumanager");
		
		Page page = userService.findByCondition(condition, objPage);
		model.addAttribute("condition", condition);
		model.addAttribute("teacherLoginLongList", page);
		
		return "/edu3/teaching/teachingManagement/teacher_loginLong-list";
	}
	
	/**
	 * 根据权限列出学生在线时长
	 * @param request
	 * @param response
	 * @param model
	 * @param objPage
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/teachingManagement/loginLongList_student.html")
	public String listStudentLoginLong(HttpServletRequest request, HttpServletResponse response, ModelMap model, Page objPage) throws WebException {
		objPage.setOrderBy(" stu.studyNo ");
		objPage.setOrder(Page.ASC);
		
		Map<String, Object> condition = new HashMap<String, Object>();
		String branchSchool = request.getParameter("branchSchool");
		String gradeid = request.getParameter("gradeid");
		String classic = request.getParameter("classic");
		String schoolType = request.getParameter("schoolType");
		String major = request.getParameter("major");
		String classesid = request.getParameter("classesid");
		String studyNo = request.getParameter("studyNo");
		String name = request.getParameter("name");
		
		
		User user = SpringSecurityHelper.getCurrentUser();
		String roleCode = CacheAppManager.getSysConfigurationByCode("sysuser.role.teacher").getParamValue();//获取角色编码
		if(user.getOrgUnit().getUnitType().indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){
			branchSchool = user.getOrgUnit().getResourceid();
			model.addAttribute("unitId", branchSchool);
		}
		if(!SpringSecurityHelper.isUserInRole("ROLE_BRS_STUDENTSTATUS")){
			if(SpringSecurityHelper.isUserInRole("ROLE_TEACHER_MASTER")){// 班主任
				condition.put("classesMasterId",user.getResourceid());
			}
			if(SpringSecurityHelper.isUserInRole(roleCode)){// 该用户为老师
				condition.put("teacherId",user.getResourceid());
			}
		}
		
		if(ExStringUtils.isNotEmpty(branchSchool)){
			condition.put("branchSchool", branchSchool);
		}
		if(ExStringUtils.isNotEmpty(gradeid)){
			condition.put("gradeid", gradeid);
		}
		if(ExStringUtils.isNotEmpty(classic)){
			condition.put("classic", classic);
		}
		if(ExStringUtils.isNotEmpty(schoolType)){
			condition.put("schoolType", schoolType);
		}
		if(ExStringUtils.isNotEmpty(major)){
			condition.put("major", major);
		}
		if(ExStringUtils.isNotEmpty(classesid)){
			condition.put("classesid", classesid);
		}
		if(ExStringUtils.isNotEmpty(studyNo)){
			condition.put("studyNo", studyNo);
		}
		if(ExStringUtils.isNotEmpty(name)){
			condition.put("name", name);
		}
		
		Page page = studentinfoservice.findStuByCondition(condition, objPage);
		model.addAttribute("condition", condition);
		model.addAttribute("studentLoginLongList", page);
		
		return "/edu3/teaching/teachingManagement/student_loginLong-list";
	}
	

}
