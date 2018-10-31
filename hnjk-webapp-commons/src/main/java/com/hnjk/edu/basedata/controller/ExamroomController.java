package com.hnjk.edu.basedata.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.utils.ExBeanUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.JsonModel;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.BaseSupportController;
import com.hnjk.edu.basedata.model.ClassInfo;
import com.hnjk.edu.basedata.model.Examroom;
import com.hnjk.edu.basedata.service.IClassService;
import com.hnjk.edu.basedata.service.IExamroomService;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.OrgUnit;
import com.hnjk.security.model.User;
import com.hnjk.security.service.IOrgUnitService;

/**
 * 考场课室
 * <code>ExamroomController</code><p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-4-7 下午03:42:03
 * @see 
 * @version 1.0
 */
@Controller
public class ExamroomController extends BaseSupportController{

	private static final long serialVersionUID = 3106449696794922125L;

	@Autowired
	@Qualifier("examroomService")
	private IExamroomService examroomService;

	@Autowired
	@Qualifier("orgUnitService")
	private IOrgUnitService orgUnitService;
	
	@Autowired
	@Qualifier("classService")
	private IClassService classService;
	
	/**
	 * 列表
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/sysmanager/examroom/list.html")
	public String exeList(String isComputerRoom,String examroomName,String schoolId,Page objPage,ModelMap model) throws WebException{
		objPage.setOrderBy("resourceid");
		objPage.setOrder(Page.ASC);//设置默认排序方式
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件
		
		User user = SpringSecurityHelper.getCurrentUser();
		if(user.getOrgUnit().getUnitType().indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员
			schoolId = user.getOrgUnit().getResourceid();
			model.addAttribute("brschool", true);
		}
		if(ExStringUtils.isNotEmpty(examroomName)) {
			condition.put("examroomName", examroomName);
		}
		if(ExStringUtils.isNotEmpty(schoolId)) {
			condition.put("schoolId", schoolId);
		}
		/*if(ExStringUtils.isNotEmpty(isComputerRoom)){
			condition.put("isComputerRoom", isComputerRoom);
		}*/
		Page page = examroomService.findExamRoomByCondition(condition, objPage);
		
		model.addAttribute("examroomList", page);
		model.addAttribute("condition", condition);
		return "/edu3/basedata/examroom/examroom-list";
	}
	
	/**
	 * 新增编辑表单
	 * @param resourceid
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/sysmanager/examroom/edit.html")
	public String exeEdit(String resourceid,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		User user = SpringSecurityHelper.getCurrentUser(); 
		
		if(user.getOrgUnit().getUnitType().indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员
			model.addAttribute("brschool", true);
			model.addAttribute("brschoolId", user.getUnitId());
		}
		if(ExStringUtils.isNotBlank(resourceid)){ //-----编辑
			Examroom examroom = examroomService.load(resourceid);
			if("N001".equals(examroom.getBranchSchool().getUnitCode())){
				model.addAttribute("examPlace", "0");
			}else{
				model.addAttribute("examPlace", "1");
			}
			model.addAttribute("isEdit", "Y");
			model.addAttribute("examroom", examroom);
		}else{ //----------------------------------------新增
			Examroom examroom = new Examroom();
			
			if(user.getOrgUnit().getUnitType().indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员
				examroom.setBranchSchool(user.getOrgUnit());
			}
			model.addAttribute("examroom", examroom);	
		}
		return "/edu3/basedata/examroom/examroom-form";
	}
	
	/**
	 * 保存更新表单
	 * @param grade
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/sysmanager/examroom/save.html")
	public void exeSave(Examroom examroom,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			String classId = request.getParameter("classId");
			if(ExStringUtils.isNotBlank(classId)){
				ClassInfo classInfo = classService.load(classId);
				examroom.setClassInfo(classInfo);
			}
			if(ExStringUtils.isNotBlank(examroom.getResourceid())){ //--------------------更新
				Examroom persistExamroom = examroomService.load(examroom.getResourceid());				
				if(ExStringUtils.isNotBlank(examroom.getBranchSchoolId())){
					OrgUnit branchSchool = orgUnitService.load(examroom.getBranchSchoolId());
					examroom.setBranchSchool(branchSchool);
				}
				ExBeanUtils.copyProperties(persistExamroom, examroom);
				examroomService.update(persistExamroom);
			}else{ //-------------------------------------------------------------------保存
				if(ExStringUtils.isNotBlank(examroom.getBranchSchoolId())){
					OrgUnit branchSchool = orgUnitService.load(examroom.getBranchSchoolId());
					examroom.setBranchSchool(branchSchool);
				}
				examroomService.save(examroom);
			}
			map.put("statusCode", 200);
			map.put("message", "保存成功！");
			map.put("navTabId", "RES_BASEDATA_MANAGER_EXAMROOM");
			map.put("reloadUrl", request.getContextPath() +"/edu3/sysmanager/examroom/edit.html?resourceid="+examroom.getResourceid());
		}catch (Exception e) {
			logger.error("保存栏目出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 删除列表对象
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/sysmanager/examroom/delete.html")
	public void exeDelete(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		String resourceid = request.getParameter("resourceid");
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(resourceid)){
				if(resourceid.split("\\,").length >1){//批量删除					
					examroomService.batchCascadeDelete(resourceid.split("\\,"));
				}else{//单个删除
					examroomService.delete(resourceid);
				}
				map.put("statusCode", 200);
				map.put("message", "删除成功！");				
				map.put("forward", request.getContextPath()+"/edu3/sysmanager/examroom/list.html");
			}
		} catch (Exception e) {
			logger.error("删除栏目出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 获得学习中心的所有所有考场
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/edu3/framework/getExamRoomBySchool.html")
	public void getExamRoomBySchool(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String branchSchool = request.getParameter("branchSchool");
		if(StringUtils.isNotEmpty(branchSchool)){
			List<Examroom> Examrooms = examroomService.findExamRoomByBranchSchool(branchSchool);
		         
			List<JsonModel> jsonList = new ArrayList<JsonModel>();
			for (Examroom room : Examrooms) {
				jsonList.add(new JsonModel(room.getExamroomName(),room.getExamroomName(), room.getResourceid()));
			}		
			renderJson(response, JsonUtils.listToJson(jsonList));
		}
	}
	
}
