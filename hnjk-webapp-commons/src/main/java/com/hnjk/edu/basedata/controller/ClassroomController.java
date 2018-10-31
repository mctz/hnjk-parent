package com.hnjk.edu.basedata.controller;

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
import com.hnjk.edu.basedata.model.Building;
import com.hnjk.edu.basedata.model.ClassInfo;
import com.hnjk.edu.basedata.model.Classroom;
import com.hnjk.edu.basedata.service.IBuildingService;
import com.hnjk.edu.basedata.service.IClassService;
import com.hnjk.edu.basedata.service.IClassroomService;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.OrgUnit;
import com.hnjk.security.model.User;
import com.hnjk.security.service.IOrgUnitService;
/**
 * 课室管理
 * <code>ClassroomController</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-10-21 下午02:24:08
 * @see 
 * @version 1.0
 */
@Controller
public class ClassroomController extends BaseSupportController {
	private static final long serialVersionUID = 4398934820703915205L;
	
	@Autowired
	@Qualifier("buildingService")
	private IBuildingService buildingService;
	
	@Autowired
	@Qualifier("classroomService")
	private IClassroomService classroomService;
	
	@Autowired
	@Qualifier("orgUnitService")
	private IOrgUnitService orgUnitService;
	
	@Autowired
	@Qualifier("classService")
	private IClassService classService;
	
	/**
	 * 教学楼列表
	 * @param buildingName
	 * @param branchSchoolId
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/sysmanager/building/list.html")
	public String listBuilding(String buildingName,String branchSchoolId,Page objPage,ModelMap model) throws WebException{
		objPage.setOrderBy("showOrder");
		objPage.setOrder(Page.ASC);//设置默认排序方式
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件
		
		User user = SpringSecurityHelper.getCurrentUser();
		if(user.getOrgUnit().getUnitType().indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员
			branchSchoolId = user.getOrgUnit().getResourceid();
			model.addAttribute("brschool", true);
		}
		
		if(ExStringUtils.isNotEmpty(buildingName)) {
			condition.put("buildingName", buildingName);
		}
		if(ExStringUtils.isNotEmpty(branchSchoolId)) {
			condition.put("branchSchoolId", branchSchoolId);
		}

		Page page = buildingService.findBuildingByCondition(condition, objPage);
		
		model.addAttribute("buildingPage", page);
		model.addAttribute("condition", condition);
		return "/edu3/basedata/building/building-list";
	}	
	/**
	 * 新增编辑教学楼
	 * @param resourceid
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/sysmanager/building/input.html")
	public String editBuilding(String resourceid,ModelMap model) throws WebException{
		User user = SpringSecurityHelper.getCurrentUser();
		if(user.getOrgUnit().getUnitType().indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员
			model.addAttribute("brschool", true);
		}
		if(ExStringUtils.isNotBlank(resourceid)){ //-----编辑
			Building building = buildingService.get(resourceid);
			model.addAttribute("building", building);
		}else{ //----------------------------------------新增
			Building building = new Building();
			
			if(user.getOrgUnit().getUnitType().indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员
				building.setBranchSchool(user.getOrgUnit());
			}
			building.setShowOrder(buildingService.getNextShowOrder());
			model.addAttribute("building", building);		
		}
		return "/edu3/basedata/building/building-form";
	}
	/**
	 * 保存教学楼
	 * @param building
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/sysmanager/building/save.html")
	public void saveBuilding(Building building,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(building.getResourceid())){ //--------------------更新
				Building persistBuilding = buildingService.get(building.getResourceid());
				
				if(ExStringUtils.isNotBlank(building.getBranchSchoolId())){
					OrgUnit branchSchool = orgUnitService.get(building.getBranchSchoolId());
					building.setBranchSchool(branchSchool);
				}
				ExBeanUtils.copyProperties(persistBuilding, building);
				buildingService.update(persistBuilding);
			}else{ //-------------------------------------------------------------------保存
				if(ExStringUtils.isNotBlank(building.getBranchSchoolId())){
					OrgUnit branchSchool = orgUnitService.load(building.getBranchSchoolId());
					building.setBranchSchool(branchSchool);
				}
				buildingService.save(building);
			}
			map.put("statusCode", 200);
			map.put("message", "保存成功！");
			map.put("navTabId", "RES_BASEDATA_BUILDING_INPUT");
			map.put("reloadUrl", request.getContextPath() +"/edu3/sysmanager/building/input.html?resourceid="+building.getResourceid());
		}catch (Exception e) {
			logger.error("保存教学楼出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 删除教学楼
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/sysmanager/building/remove.html")
	public void removeBuilding(HttpServletRequest request,HttpServletResponse response) throws WebException{
		String resourceid = request.getParameter("resourceid");
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(resourceid)){
				if(resourceid.split("\\,").length >0) {
					buildingService.batchCascadeDelete(resourceid.split("\\,"));
				}
				map.put("statusCode", 200);
				map.put("message", "删除成功！");				
				map.put("forward", request.getContextPath()+"/edu3/sysmanager/building/list.html");
			}
		} catch (Exception e) {
			logger.error("删除教学楼出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 课室列表
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/sysmanager/classroom/list.html")
	public String listClassroom(HttpServletRequest request,Page objPage,ModelMap model) throws WebException{
		objPage.setOrderBy("showOrder");
		objPage.setOrder(Page.ASC);//设置默认排序方式
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件
		String buildingId = request.getParameter("buildingId");
		String classroomName = request.getParameter("classroomName");
		String classroomType = request.getParameter("classroomType");
		
		User user = SpringSecurityHelper.getCurrentUser();
		if(user.getOrgUnit().getUnitType().indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员
			model.addAttribute("branchSchoolId", user.getOrgUnit().getResourceid());
		}
		
		if(ExStringUtils.isNotEmpty(buildingId)) {
			condition.put("buildingId", buildingId);
		}
		if(ExStringUtils.isNotEmpty(classroomName)) {
			condition.put("classroomName", classroomName);
		}
		if(ExStringUtils.isNotEmpty(classroomType)) {
			condition.put("classroomType", classroomType);
		}

		Page page = classroomService.findClassroomByCondition(condition, objPage);
		
		model.addAttribute("classroomPage", page);
		model.addAttribute("condition", condition);
		return "/edu3/basedata/classroom/classroom-list";
	}	
	/**
	 * 新增编辑课室
	 * @param resourceid
	 * @param buildingId
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/sysmanager/classroom/input.html")
	public String editClassroom(String resourceid,String buildingId,ModelMap model) throws WebException{
		User user = SpringSecurityHelper.getCurrentUser();
		if(user.getOrgUnit().getUnitType().indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员
			model.addAttribute("branchSchoolId", user.getOrgUnit().getResourceid());
		}
		if(ExStringUtils.isNotBlank(resourceid)){ //-----编辑
			Classroom classroom = classroomService.get(resourceid);
			model.addAttribute("classroom", classroom);
		}else{ //----------------------------------------新增
			Classroom classroom = new Classroom();
			if(ExStringUtils.isNotEmpty(buildingId)){
				Building building = buildingService.get(buildingId);
				classroom.setBuilding(building);
				classroom.setIsUseCourse("Y");//默认可排课
				classroom.setIsUseExam("Y");//默认可排考
				classroom.setShowOrder(classroomService.getNextShowOrder(buildingId));
			}
			model.addAttribute("classroom", classroom);		
		}
		return "/edu3/basedata/classroom/classroom-form";
	}
	/**
	 * 保存课室
	 * @param building
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/sysmanager/classroom/save.html")
	public void saveClassroom(Classroom classroom,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			String classId = request.getParameter("classId");
			if(ExStringUtils.isNotBlank(classId)){
				ClassInfo classInfo = classService.load(classId);
				classroom.setClassInfo(classInfo);
			}
			if(ExStringUtils.isNotBlank(classroom.getResourceid())){ //--------------------更新
				Classroom persistClassroom = classroomService.get(classroom.getResourceid());
				ExBeanUtils.copyProperties(persistClassroom, classroom);
				if(ExStringUtils.isNotBlank(classroom.getBuildingId())){
					Building building = buildingService.get(classroom.getBuildingId());
					persistClassroom.setBuilding(building);
					if(classroom.getBuildingId().equals(persistClassroom.getBuilding().getResourceid())) {
						classroom.setShowOrder(classroomService.getNextShowOrder(building.getResourceid()));
					}
				}
				classroomService.update(persistClassroom);
			}else{ //-------------------------------------------------------------------保存
				if(ExStringUtils.isNotBlank(classroom.getBuildingId())){
					Building building = buildingService.get(classroom.getBuildingId());
					classroom.setBuilding(building);
					classroom.setShowOrder(classroomService.getNextShowOrder(building.getResourceid()));
				}
				classroomService.save(classroom);
			}
			map.put("statusCode", 200);
			map.put("message", "保存成功！");
			map.put("navTabId", "RES_BASEDATA_CLASSROOM_INPUT");
			map.put("reloadUrl", request.getContextPath() +"/edu3/sysmanager/classroom/input.html?resourceid="+classroom.getResourceid());
		}catch (Exception e) {
			logger.error("保存教室出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 删除课室
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/sysmanager/classroom/remove.html")
	public void removeClassroom(HttpServletRequest request,HttpServletResponse response) throws WebException{
		String resourceid = request.getParameter("resourceid");
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(resourceid)){
				if(resourceid.split("\\,").length >0) {
					classroomService.batchCascadeDelete(resourceid.split("\\,"));
				}
				map.put("statusCode", 200);
				map.put("message", "删除成功！");				
				map.put("forward", request.getContextPath()+"/edu3/sysmanager/classroom/list.html");
			}
		} catch (Exception e) {
			logger.error("删除教室出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
}
