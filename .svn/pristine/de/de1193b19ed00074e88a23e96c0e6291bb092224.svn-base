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
import com.hnjk.core.foundation.utils.ExBeanUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.BaseSupportController;
import com.hnjk.edu.teaching.model.ExamStaff;
import com.hnjk.edu.teaching.service.IExamStaffService;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.OrgUnit;
import com.hnjk.security.model.User;
import com.hnjk.security.service.IOrgUnitService;
import com.hnjk.security.service.IUserService;
/**
 * 考试考务-监巡考人员服务接口管理.
 * <code>ExamStaffController</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2011-8-19 上午11:25:24
 * @see 
 * @version 1.0
 */
@Controller
public class ExamStaffController extends BaseSupportController {

	private static final long serialVersionUID = -235263462549893305L;
	
	@Autowired
	@Qualifier("examStaffService")
	private IExamStaffService examStaffService;
	
	@Autowired
	@Qualifier("userService")
	private IUserService userService;
	
	@Autowired
	@Qualifier("orgUnitService")
	private IOrgUnitService orgUnitService;
	
	/**
	 * 监巡考人员列表
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/examstaff/list.html")
	public String listExamStaff(HttpServletRequest request, Page objPage, ModelMap model) throws WebException {
		objPage.setOrderBy("orgUnit,name");
		objPage.setOrder(Page.ASC);
		String name = ExStringUtils.trimToEmpty(request.getParameter("name"));
		String idcardNum = ExStringUtils.trimToEmpty(request.getParameter("idcardNum"));
		String orgUnitId = request.getParameter("orgUnitId");
		String hasExamstaff = request.getParameter("hasExamstaff");
		String workLevel = request.getParameter("workLevel");

		Map<String, Object> condition = new HashMap<String, Object>();// 查询条件
		if (ExStringUtils.isNotEmpty(name)) {
			condition.put("name", name);
		}
		if (ExStringUtils.isNotEmpty(idcardNum)) {
			condition.put("idcardNum", idcardNum);
		}
		if (ExStringUtils.isNotEmpty(orgUnitId)) {
			condition.put("orgUnitId", orgUnitId);
		}
		if (ExStringUtils.isNotEmpty(hasExamstaff)) {
			condition.put("hasExamstaff", hasExamstaff);
		}
		if (ExStringUtils.isNotEmpty(workLevel)) {
			condition.put("workLevel", workLevel);
		}

		Page examStaffListPage = examStaffService.findExamStaffByCondition(condition, objPage);
		model.addAttribute("examStaffListPage", examStaffListPage);			
		model.addAttribute("condition", condition);	
		return "/edu3/teaching/examstaff/examstaff-list";
	}
	/**
	 * 新增编辑监巡考人员
	 * @param resourceid
	 * @param request
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/examstaff/input.html")
	public String editExamStaff(String resourceid, HttpServletRequest request, ModelMap model) throws WebException {
		if (ExStringUtils.isNotBlank(resourceid)) { // -----编辑
			ExamStaff examStaff = examStaffService.get(resourceid);
			model.addAttribute("examStaff", examStaff);
		} else { // ----------------------------------------新增
			ExamStaff examStaff = new ExamStaff();
			model.addAttribute("examStaff", examStaff);
		}
		return "/edu3/teaching/examstaff/examstaff-form";
	}

	/**
	 * 保存监巡考人员
	 * @param examStaff
	 * @param sysUserId
	 * @param orgUnitId
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/examstaff/save.html")
	public void saveExamStaff(ExamStaff examStaff,String sysUserId,String orgUnitId,HttpServletRequest request,HttpServletResponse response) throws WebException {
		Map<String,Object> map = new HashMap<String, Object>();
		try {	
			if(ExStringUtils.isNotBlank(orgUnitId)){
				OrgUnit orgUnit = orgUnitService.get(orgUnitId);
				examStaff.setOrgUnit(orgUnit);
				examStaff.setOrgUnitName(orgUnit.getUnitName());
			}
			if(ExStringUtils.isNotBlank(sysUserId)){
				User sysUser = userService.get(sysUserId);
				examStaff.setSysUser(sysUser);
			}
			if(ExStringUtils.isNotBlank(examStaff.getResourceid())){//---------编辑					
				ExamStaff pExamStaff = examStaffService.get(examStaff.getResourceid());
				ExBeanUtils.copyProperties(pExamStaff, examStaff);
				examStaffService.update(pExamStaff);
			}else{        //---------------------------------------------------新增
				examStaffService.save(examStaff);
			}
			map.put("statusCode", 200);
			map.put("message", "保存成功！");
			map.put("navTabId", "RES_TEACHING_EXAM_EXAMSTAFF_EDIT");
			map.put("reloadUrl", request.getContextPath() +"/edu3/teaching/examstaff/input.html?resourceid="+examStaff.getResourceid());
		} catch (Exception e) {
			logger.error("保存监巡考人员错误:"+e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败:<br/>"+e.getMessage());							
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}

	/**
	 * 删除监巡考人员
	 * @param resourceid
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/examstaff/remove.html")
	public void removeExamStaff(String resourceid, HttpServletRequest request, HttpServletResponse response) throws WebException {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if (ExStringUtils.isNotBlank(resourceid)) {
				examStaffService.batchDelete(resourceid.split("\\,"));
				map.put("statusCode", 200);
				map.put("message", "删除成功！");
				map.put("forward", request.getContextPath() + "/edu3/teaching/examstaff/list.html");
			}
		} catch (Exception e) {
			logger.error("删除监巡考人员出错:{}", e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错！<br/>"+e.getLocalizedMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 系统人员选择
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/edu3/framework/examstaff/sysuser/rel.html")
	public String userSelector(HttpServletRequest request,Page objPage,ModelMap model) throws Exception{
		objPage.setOrderBy("unitId");
		objPage.setOrder(Page.ASC);//设置默认排序方式
		//查询条件
		Map<String,Object> condition = new HashMap<String,Object>();
		String userName = ExStringUtils.trimToEmpty(request.getParameter("userName"));//登录名
		String cnName = ExStringUtils.trimToEmpty(request.getParameter("cnName"));//姓名
		String unitId = request.getParameter("unitId");
		String formid = request.getParameter("formid");
		String sysUserId = request.getParameter("sysUserId");
		
		User user = SpringSecurityHelper.getCurrentUser();
		String unitType = CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue();
		if(unitType.equals(user.getOrgUnit().getUnitType())){//如果为校外学习中心人员
			unitId = user.getUnitId();
			model.addAttribute("brschool", true);
		}
		if(ExStringUtils.isNotEmpty(userName)) {
			condition.put("userName", userName);
		}
		if(ExStringUtils.isNotEmpty(cnName)) {
			condition.put("cnName", cnName);
		}
		if(ExStringUtils.isNotEmpty(unitId)) {
			condition.put("unitId", unitId);
		}
		condition.put("userType", CacheAppManager.getSysConfigurationByCode("sysuser.usertype.edumanager").getParamValue());
		condition.put("isDeleted", 0);		
		
		Page page = userService.findUserByCondition(condition, objPage);
		if(ExStringUtils.isNotEmpty(formid)) {
			condition.put("formid", formid);
		}
		if(ExStringUtils.isNotEmpty(sysUserId)) {
			condition.put("sysUserId", sysUserId);
		}
		
		model.addAttribute("userlist", page);
		model.addAttribute("condition", condition);
		return "/edu3/teaching/examstaff/user-selector";
	}
}
