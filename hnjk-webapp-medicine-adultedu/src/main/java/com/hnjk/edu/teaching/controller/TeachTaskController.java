package com.hnjk.edu.teaching.controller;

import java.text.ParseException;
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
import com.hnjk.core.foundation.utils.ExCollectionUtils;
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.BaseSupportController;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.Edumanager;
import com.hnjk.edu.basedata.model.YearInfo;
import com.hnjk.edu.basedata.service.IEdumanagerService;
import com.hnjk.edu.basedata.service.IYearInfoService;
import com.hnjk.edu.learning.service.IStudentLearnPlanService;
import com.hnjk.edu.teaching.model.TeachTask;
import com.hnjk.edu.teaching.model.TeachTaskDetails;
import com.hnjk.edu.teaching.service.ICourseService;
import com.hnjk.edu.teaching.service.ITeachTaskDetailsService;
import com.hnjk.edu.teaching.service.ITeachTaskService;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.taglib.JstlCustomFunction;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;

/**
 * 教学计划 - 教学任务书.
 * <code>TeachTaskController</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2010-7-8 下午03:51:53
 * @see 
 * @version 1.0
 */
@Controller
public class TeachTaskController extends BaseSupportController{

	private static final long serialVersionUID = -1702637331104846046L;

	/**
	 * 教学任务书列表
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/teachtask/list.html")
	public String exeList(HttpServletRequest request, Page objPage, ModelMap model) throws WebException{
		String orderBy = ExStringUtils.trimToEmpty(request.getParameter("orderBy"));
		String orderType = ExStringUtils.trimToEmpty(request.getParameter("orderType"));
		if(StringUtils.isNotEmpty(orderBy) && StringUtils.isNotEmpty(orderType)){//如果排序条件不为空，则加入排序
			objPage.setOrderBy(orderBy);
			objPage.setOrder(orderType);	
		}else{
			objPage.setOrderBy("yearInfo.firstYear desc,term desc,course,resourceid");
			objPage.setOrder(Page.ASC);//设置默认排序方式
		}	
		
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件
		
		String yearInfoid = request.getParameter("yearInfoid");
		String term = request.getParameter("term");
		String courseName = ExStringUtils.trimToEmpty(request.getParameter("courseName"));
		String teacherName = ExStringUtils.trimToEmpty(request.getParameter("teacherName"));
		String taskStatus = request.getParameter("taskStatus");
		
		if(ExStringUtils.isNotEmpty(yearInfoid)) {
			condition.put("yearInfoid", yearInfoid);
		}
		if(ExStringUtils.isNotEmpty(term)) {
			condition.put("term", term);
		}
		if(ExStringUtils.isNotEmpty(courseName)) {
			condition.put("courseName", courseName);
		}
		if(ExStringUtils.isNotEmpty(teacherName)) {
			condition.put("teacherName", teacherName);
		}
		if(ExStringUtils.isNotEmpty(taskStatus)) {
			condition.put("taskStatus", taskStatus);
		}
		condition.put("isTemplate", Constants.BOOLEAN_NO);
		condition.put("orderBy", orderBy);
		condition.put("orderType", orderType);
		model.addAttribute("condition", condition);
		
		String advanseCon = ExStringUtils.trimToEmpty(request.getParameter("con"));//高级查询页面
		if ("advance".equalsIgnoreCase(advanseCon)) {
			return "/edu3/teaching/teachtask/teachtask-search";// 返回到高级检索
		}
		
		Page page = teachTaskService.findTeachTaskByCondition(condition, objPage);		
		model.addAttribute("taskList", page);		
		return "/edu3/teaching/teachtask/teachtask-list";
	}
	
	/**
	 * 新增编辑教学任务书
	 * @param resourceid
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/teachtask/edit.html")
	public String exeEdit(String resourceid,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		if(ExStringUtils.isNotBlank(resourceid)){ //-----编辑
			TeachTask teachTask = teachTaskService.get(resourceid);	
			model.addAttribute("teachTask", teachTask);	
		}
		model.addAttribute("currentIndex", request.getParameter("currentIndex"));
		String type = ExStringUtils.trimToEmpty(request.getParameter("type"));
		if("details".equalsIgnoreCase(type)) {
			return "/edu3/teaching/teachtask/teachtask-details";
		}
		return "/edu3/teaching/teachtask/teachtask-form";
	}
	
	/**
	 *老师选择对话框
	 * @param objPage
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/edu3/teaching/teachtask/teacher.html")
	public String listTeacher(Page objPage,HttpServletRequest request,ModelMap model) throws Exception{
		objPage.setOrderBy("unitId");
		objPage.setOrder(Page.ASC);//设置默认排序方式
		//查询条件
		Map<String,Object> condition = new HashMap<String,Object>();
		String unitId=request.getParameter("unitId");//学习中心
		String teachingType=request.getParameter("teachingType");
		String teacherCode=request.getParameter("teacherCode");
		String cnName=request.getParameter("cnName");//姓名
		String type=request.getParameter("type");//教师类型
		
		if(ExStringUtils.isNotEmpty(unitId)) {
			condition.put("unitId", unitId);
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
			
		String idsN=request.getParameter("idsN");
		String namesN=request.getParameter("namesN");
		model.addAttribute("idsN", idsN);
		model.addAttribute("namesN", namesN);
		condition.put("idsN", idsN);
		condition.put("namesN", namesN);
		model.addAttribute("teacherlist", page);
		model.addAttribute("condition", condition);		
		
		return "/edu3/teaching/teachtask/selector-teacher";
	}
	
	/**
	 * 保存更新教学任务书
	 * @param grade
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/teachtask/save.html")
	public void exeSave(TeachTask teachTask,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		try {	
			String type = ExStringUtils.trimToEmpty(request.getParameter("type"));
			if(ExStringUtils.isNotBlank(teachTask.getResourceid())){ //--------------------更新
				TeachTask p_teachTask = teachTaskService.get(teachTask.getResourceid());
				
				if("details".equalsIgnoreCase(type)){
					String[] tk_ids = request.getParameterValues("tk_id");
					String[] evaluates = request.getParameterValues("evaluate");
					if(tk_ids != null && tk_ids.length>0){
						for(int index=0;index<tk_ids.length;index++){
							TeachTaskDetails detail = new TeachTaskDetails();
							if(ExStringUtils.isNotEmpty(tk_ids[index])) {
								detail = (TeachTaskDetails) teachTaskService.get(TeachTaskDetails.class, tk_ids[index]);
							}
							detail.setEvaluate(ExStringUtils.isEmpty(evaluates[index])?null:Integer.parseInt(evaluates[index]));
							detail.setTeachTask(p_teachTask);
							p_teachTask.getTeachTaskDetails().add(detail);
						}
					}				
					map.put("message", "保存评价成功！");
				} else {
					teachTask.setTeachTaskDetails(p_teachTask.getTeachTaskDetails());
					teachTask.setCourse(p_teachTask.getCourse());
					teachTask.setTerm(p_teachTask.getTerm());
					teachTask.setYearInfo(p_teachTask.getYearInfo());
					ExBeanUtils.copyProperties(p_teachTask, teachTask);
					map.put("message", "分配任务成功！");
				}	
				teachTaskService.saveOrUpdate(p_teachTask);
			}	
			map.put("statusCode", 200);
			map.put("navTabId", "RES_TEACHING_ESTAB_TEACHTASK");
			map.put("reloadUrl", request.getContextPath() +"/edu3/teaching/teachtask/edit.html?resourceid="+teachTask.getResourceid()+"&type="+type);
		}catch (Exception e) {
			logger.error("保存教学任务书出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 删除教学任务书
	 * @param request
	 * @param response
	 * @param model
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/teachtask/delete.html")
	public void exeDelete(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		String c_id = request.getParameter("resourceid");
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(c_id)){
				if(c_id.split("\\,").length >0){			
					teachTaskService.batchCascadeDelete(c_id.split("\\,"));
				}
				map.put("statusCode", 200);
				map.put("message", "删除教学任务书成功！");				
				map.put("forward", request.getContextPath()+"/edu3/teaching/teachtask/list.html");
			}
		} catch (Exception e) {
			logger.error("删除栏目出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	} 

	/**
	 * 发送任务书
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/teachtask/send.html")
	public void exeSend(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		String resourceid = request.getParameter("resourceid");
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(resourceid)){
				String[] ids = resourceid.split("\\,");
				if(ids.length >0){		
					boolean isSend = true;						
					for (String id : ids) {
						TeachTask teachTask = teachTaskService.get(id);
						if(teachTask.getTaskStatus()!=0 && teachTask.getTaskStatus()!=2){
							isSend = false;
							break;
						} else {
							if(teachTask.getTaskStatus()==0){
								if(ExStringUtils.isEmpty(teachTask.getTeacherId())){
									isSend = false;
									break;
								}
							} 
						}
					}
					if(!isSend){ //非发送状态或未分配老师
						map.put("statusCode", 300);
						map.put("message", "任务书未分配任务或已发送或已审核，请重新选择!");	
					} else {
						teachTaskService.batchSend(1,ids);
						map.put("statusCode", 200);
						map.put("message", "发送成功！");				
						map.put("forward", request.getContextPath()+"/edu3/teaching/teachtask/list.html");
					}					
				}				
			}
		} catch (Exception e) {
			logger.error("发送出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "发送失败！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 查看教学任务书
	 * @param resourceid
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/teachtask/view.html")
	public String viewTeachTask(String resourceid,ModelMap model) throws WebException{
		if(ExStringUtils.isNotBlank(resourceid)){ 
			TeachTask teachTask = teachTaskService.load(resourceid);	
			model.addAttribute("teachTask", teachTask);
			model.addAttribute("newLine", "\n");
		}
		return "/edu3/teaching/teachtask/teachtask-view";
	}
	
	/**
	 * 老师任务书查询
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/teachtask/listDetail.html")
	public String exeListDetail(HttpServletRequest request, Page objPage, ModelMap model) throws WebException{
		objPage.setOrderBy("yearInfo.firstYear desc,term desc,resourceid");
		objPage.setOrder(Page.ASC);//设置默认排序方式
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件
		
		String yearInfoid = request.getParameter("yearInfoid");
		String term = request.getParameter("term");
		String courseName = request.getParameter("courseName");
		
		if(ExStringUtils.isNotEmpty(yearInfoid)) {
			condition.put("yearInfoid", yearInfoid);
		}
		if(ExStringUtils.isNotEmpty(term)) {
			condition.put("term", term);
		}
		if(ExStringUtils.isNotEmpty(courseName)) {
			condition.put("courseName", courseName);
		}
		condition.put("gtTaskStatus", 0L); //已发送给老师
		condition.put("isTemplate", Constants.BOOLEAN_NO);
		
		model.addAttribute("condition", condition);	
		
		/* 过滤掉成人面授的老师 */		
		boolean isfaceteach = false;
		String roleTeacher = CacheAppManager.getSysConfigurationByCode("sysuser.role.teacher").getParamValue();
		User user = SpringSecurityHelper.getCurrentUser();	
		if(SpringSecurityHelper.isUserInRole(roleTeacher)){			
			Edumanager euser = edumanagerService.get(user.getResourceid());
			if("adult".equalsIgnoreCase(euser.getTeachingType())){
				isfaceteach = true;
			}
		}
		if(!isfaceteach){
			if(SpringSecurityHelper.isUserInRole(roleTeacher)){
				condition.put("teacher", user.getResourceid());
				if(!SpringSecurityHelper.isUserInRole(CacheAppManager.getSysConfigurationByCode("sysuser.role.teacher.duty").getParamValue())){//非主讲老师
					condition.put("taskStatus", 3L);//只能查看已审核发布的任务书
				}
			}			
			Page page = teachTaskService.findTeachTaskByCondition(condition, objPage);
			model.addAttribute("taskList", page);					
		}
		
		return "/edu3/teaching/teachtask/teachtaskdetail-list";
	}
	
	/**
	 * 编辑教学内容
	 * @param resourceid
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/teachtask/editDetail.html")
	public String exeEditDetail(String resourceid,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		if(ExStringUtils.isNotBlank(resourceid)){ //-----编辑
			TeachTask teachTask = teachTaskService.load(resourceid);	
			model.addAttribute("teachTask", teachTask);			
			model.addAttribute("ids", teachTask.getTeacherIds());	
			model.addAttribute("currentIndex", request.getParameter("currentIndex"));	
		}
		return "/edu3/teaching/teachtask/teachtaskdetail-form";
	}
	
	
	/**
	 * 保存教学任务内容
	 * @param grade
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/teachtask/saveDetail.html")
	public void exeSaveDetail(String resourceid,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(resourceid)){ //--------------------保存
				TeachTask task = teachTaskService.get(resourceid);
				String[] tk_ids = request.getParameterValues("tk_id");
				String[] taskContents = request.getParameterValues("taskContent");
				String[] beginTimes = request.getParameterValues("beginTime");
				String[] endTimes = request.getParameterValues("endTime");
				String[] taskTypes = request.getParameterValues("taskType");		
				String[] ownerIds = request.getParameterValues("ownerId");
				String[] ownerNames = request.getParameterValues("ownerName");
				String[] participantsIds = request.getParameterValues("participantsId");
				String[] participantsNames = request.getParameterValues("participantsName");
				String[] statuss = request.getParameterValues("status");
				String[] showOrders = request.getParameterValues("showOrder");
				if(tk_ids != null && tk_ids.length>0){
					for(int index=0;index<tk_ids.length;index++){
						TeachTaskDetails detail = new TeachTaskDetails();
						if(ExStringUtils.isNotEmpty(tk_ids[index])) {
							detail = (TeachTaskDetails) teachTaskService.get(TeachTaskDetails.class, tk_ids[index]);
						}
						detail.setTaskContent(ExStringUtils.isEmpty(taskContents[index])?"":taskContents[index]);
						detail.setStartTime(ExStringUtils.isEmpty(beginTimes[index])? null : ExDateUtils.parseDate(beginTimes[index], "yyyy-MM-dd HH:mm:ss"));
						detail.setEndTime(ExStringUtils.isEmpty(endTimes[index])? null : ExDateUtils.parseDate(endTimes[index], "yyyy-MM-dd HH:mm:ss"));
						detail.setTaskType(ExStringUtils.isEmpty(taskTypes[index])?"":taskTypes[index]);
						detail.setOwnerId(ExStringUtils.isEmpty(ownerIds[index])?"":ownerIds[index]);
						detail.setOwnerName(ExStringUtils.isEmpty(ownerNames[index])?"":ownerNames[index]);
						detail.setParticipantsId(ExStringUtils.isEmpty(participantsIds[index])?"":participantsIds[index]);
						detail.setParticipantsName(ExStringUtils.isEmpty(participantsNames[index])?"":participantsNames[index]);
						detail.setStatus(ExStringUtils.isEmpty(statuss[index])?"unfinished":statuss[index]);
						detail.setShowOrder(ExStringUtils.isEmpty(showOrders[index])?0:Long.parseLong(showOrders[index]));
						detail.setTeachTask(task);
						task.getTeachTaskDetails().add(detail);
					}
					teachTaskService.update(task);
				}
				map.put("reloadUrl", request.getContextPath() +"/edu3/teaching/teachtask/editDetail.html?resourceid="+task.getResourceid());
			}
			map.put("statusCode", 200);
			map.put("message", "保存教学任务内容成功！");
			
		}catch (Exception e) {
			logger.error("保存教学任务出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	
	
	/**
	 * 删除教学任务书明细
	 * @param request
	 * @param response
	 * @param model
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/teachtask/deleteDetail.html")
	public void exeDeleteDetail(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		String c_id = request.getParameter("c_id");
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(c_id)){
				teachTaskService.deleteDetail(c_id);
			}
		} catch (Exception e) {
			logger.error("删除教学任务出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	} 
	
	/**
	 * 教师送回任务书
	 * @param request
	 * @param response
	 * @param model
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/teachtask/sendBack.html")
	public void exeSendBack(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		String resourceid = request.getParameter("resourceid");
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(resourceid)){
				String[] ids = resourceid.split("\\,");
				if(ids.length >0){		
					boolean isSend = true;						
					for (String id : ids) {
						TeachTask teachTask = teachTaskService.get(id);
						if(teachTask.getTaskStatus()!=1){
							isSend = false;
							break;
						} else {
							if(teachTask.getTaskStatus()==1){
								if(ExCollectionUtils.isEmpty(teachTask.getTeachTaskDetails())){
									isSend = false;
									break;
								}
							}
						}
					}
					if(!isSend){ 
						map.put("statusCode", 300);
						map.put("message", "任务书未完成或已发回或已审核发布，请重新选择!");	
					} else {
						teachTaskService.batchSend(2,ids);
						map.put("statusCode", 200);
						map.put("message", "发送成功！");				
						map.put("forward", request.getContextPath()+"/edu3/teaching/teachtask/listDetail.html");
					}					
				}				
			}
		} catch (Exception e) {
			logger.error("发送出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "发送失败！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 审核发布
	 * @param request
	 * @param response
	 * @param model
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/teachtask/audit.html")
	public void auditTeachTask(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		String resourceid = request.getParameter("resourceid");
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(resourceid)){
				String[] ids = resourceid.split("\\,");
				if(ids.length >0){						
					teachTaskService.batchSend(3,ids);
					map.put("statusCode", 200);
					map.put("message", "已发布！");				
					map.put("forward", request.getContextPath()+"/edu3/teaching/teachtask/list.html");
				}				
			}
		} catch (Exception e) {
			logger.error("发送出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "发送失败！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 新增编辑任务安排
	 * @param resourceid
	 * @param teachTaskId
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/framework/teaching/teachtask/detail/input.html")
	public String editDetail(String resourceid,String teachTaskId,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		if(ExStringUtils.isNotBlank(teachTaskId)){
			TeachTask teachTask = teachTaskService.get(teachTaskId);
			model.addAttribute("teachTask", teachTask);	
			model.addAttribute("ids", teachTask.getTeacherIds());
			if(ExStringUtils.isNotBlank(resourceid)){ //-----编辑
				TeachTaskDetails teachTaskDetail = teachTaskDetailsService.get(resourceid);	
				model.addAttribute("teachTaskDetail", teachTaskDetail);								
			} else {
				TeachTaskDetails teachTaskDetail = new TeachTaskDetails();
				teachTaskDetail.setShowOrder(teachTask.getNextShowOrder());
				if(!"task".equalsIgnoreCase(ExStringUtils.trimToEmpty(request.getParameter("from")))){
					teachTaskDetail.setIsCustomTask(Constants.BOOLEAN_YES);
				}				
				teachTaskDetail.setStatus("unfinished");
				model.addAttribute("teachTaskDetail", teachTaskDetail);	
			}
			model.addAttribute("from", ExStringUtils.trimToEmpty(request.getParameter("from")));
		}		
		return "/edu3/teaching/teachtask/teachtaskdetails-form";
	}
	
	
	/**
	 * 保存教学任务
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/framework/teaching/teachtask/detail/save.html")
	public void saveDetail(String teachTaskId,TeachTaskDetails teachTaskDetail,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(teachTaskId)){
				TeachTask teachTask = teachTaskService.get(teachTaskId);
				teachTaskDetail.setTeachTask(teachTask);
				teachTaskDetail.setStatus(ExStringUtils.defaultIfEmpty(teachTaskDetail.getStatus(),"unfinished"));
				if(ExStringUtils.isNotBlank(teachTaskDetail.getResourceid())){ //--------------------保存
					TeachTaskDetails p_TeachTaskDetails = teachTaskDetailsService.get(teachTaskDetail.getResourceid());
					ExBeanUtils.copyProperties(p_TeachTaskDetails, teachTaskDetail);
					teachTaskService.update(teachTask);
					map.put("closeCurrent", "closeCurrent");
				} else {
					teachTask.getTeachTaskDetails().add(teachTaskDetail);
					teachTaskService.update(teachTask);
				}
				map.put("statusCode", 200);
				map.put("message", "保存教学任务内容成功！");				
				map.put("callbackType", "forward");
				map.put("forward", request.getContextPath() +"/edu3/framework/teaching/teachtask/detail/input.html?teachTaskId="+teachTaskId);
				if("task".equalsIgnoreCase(ExStringUtils.trimToEmpty(request.getParameter("from")))){//教学版编辑任务
					map.put("reloadUrl", request.getContextPath() +"/edu3/teaching/teachtask/edit.html?currentIndex=1&resourceid="+teachTaskId);
				} else {
					map.put("reloadUrl", request.getContextPath() +"/edu3/teaching/teachtask/editDetail.html?currentIndex=1&resourceid="+teachTaskId);
				}
			}	
		}catch (Exception e) {
			logger.error("保存教学任务出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 删除教学任务书明细
	 * @param request
	 * @param response
	 * @param model
	 * @throws WebException
	 */
	@RequestMapping("/edu3/framework/teaching/teachtask/detail/delete.html")
	public void deleteDetail(String resourceid,String teachTaskId,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(teachTaskId) && ExStringUtils.isNotBlank(resourceid)){
				teachTaskDetailsService.batchCascadeDelete(resourceid.split("\\,"),teachTaskId);
				map.put("statusCode", 200);
				map.put("message", "删除成功！");	
				map.put("callbackType", "forward");
				if("task".equalsIgnoreCase(ExStringUtils.trimToEmpty(request.getParameter("from")))){//教学版编辑任务
					map.put("forward", request.getContextPath()+"/edu3/teaching/teachtask/edit.html?currentIndex=1&resourceid="+teachTaskId);
				} else {
					map.put("forward", request.getContextPath()+"/edu3/teaching/teachtask/editDetail.html?currentIndex=1&resourceid="+teachTaskId);
				}				
			}
		} catch (Exception e) {
			logger.error("删除教学任务明细出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	} 	

	/**
	 * 课程预约情况
	 * @param taskId
	 * @param request
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/framework/teaching/studentlearnplan/list.html")
	public String listStudentLearnPlan(String taskId,HttpServletRequest request,Page objPage, ModelMap model) throws WebException{
		if(ExStringUtils.isNotBlank(taskId)){
			objPage.setOrderBy("teachingPlanCourse.resourceid,yearInfo.firstYear desc,term");
			objPage.setOrder(Page.ASC);
			TeachTask teachTask = teachTaskService.get(taskId);
			
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("courseId", teachTask.getCourse().getResourceid());
			condition.put("yearInfo", teachTask.getYearInfo().getResourceid());
			condition.put("term", teachTask.getTerm());
			//condition.put("orderBy", "teachingPlanCourse.resourceid,yearInfo.firstYear desc,term");
			condition.put("taskId", taskId);
			
			Page listStudents = studentLearnPlanService.findStudentLearnPlanByCondition(condition, objPage);
			
			model.addAttribute("listStudents",listStudents);
			model.addAttribute("condition",condition);
		}
		return "/edu3/teaching/teachtask/studentlearnplan-list";
	}
	/**
	 * 教学任务书管理列表
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/teachtask/template/list.html")
	public String listTeachTaskTemplate(HttpServletRequest request, Page objPage, ModelMap model) throws WebException{
		objPage.setOrderBy("yearInfo.firstYear desc,term");
		objPage.setOrder(Page.DESC);//设置默认排序方式
		
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件		
		String yearInfoid = request.getParameter("yearInfoid");
		String term = request.getParameter("term");
		
		if(ExStringUtils.isNotEmpty(yearInfoid)) {
			condition.put("yearInfoid", yearInfoid);
		}
		if(ExStringUtils.isNotEmpty(term)) {
			condition.put("term", term);
		}
		condition.put("isTemplate", Constants.BOOLEAN_YES);//任务书模板
		
		Page page = teachTaskService.findTeachTaskByCondition(condition, objPage);		
		model.addAttribute("taskList", page);	
		model.addAttribute("condition", condition);
		return "/edu3/teaching/teachtask/teachtask-template-list";
	}
	/**
	 * 新增编辑教学任务书模板
	 * @param resourceid
	 * @param request
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/teachtask/template/input.html")
	public String editTeachTaskTemplate(String resourceid,HttpServletRequest request,ModelMap model) throws WebException{
		if(ExStringUtils.isNotBlank(resourceid)){ //-----编辑
			TeachTask teachTask = teachTaskService.get(resourceid);	
			model.addAttribute("teachTask", teachTask);	
		} else {
			TeachTask teachTask = new TeachTask();	
			teachTask.setIsTemplate(Constants.BOOLEAN_YES);
			model.addAttribute("teachTask", teachTask);	
		}		
		return "/edu3/teaching/teachtask/teachtask-template-form";
	}
	/**
	 * 保存教学任务模板
	 * @param resourceid
	 * @param request
	 * @param response
	 * @param model
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/teachtask/template/save.html")
	public void saveTeachTemplateDetail(String resourceid,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			String yearInfoId = ExStringUtils.trimToEmpty(request.getParameter("yearInfoId"));
			String term = ExStringUtils.trimToEmpty(request.getParameter("term"));
			YearInfo yearInfo = yearInfoService.get(yearInfoId);
			if (ExStringUtils.isNotBlank(yearInfoId) && ExStringUtils.isNotEmpty(term)) {
				TeachTask task = new TeachTask();
				if(ExStringUtils.isNotBlank(resourceid)){ //--------------------保存
					task = teachTaskService.get(resourceid);
					task.setYearInfo(yearInfo);
					task.setTerm(term);
					relationTeachTaskDetails(request, task);				
				} else { //--------------------新增
					Map<String,Object> condition = new HashMap<String,Object>();//查询条件	
					condition.put("yearInfoid", yearInfoId);
					condition.put("term", term);
					condition.put("isTemplate", Constants.BOOLEAN_YES);//任务书模板
					List<TeachTask> list = teachTaskService.findTeachTaskByCondition(condition);
					if(list!=null && !list.isEmpty()){//模板已存在
						throw new WebException(yearInfo.getYearName()+JstlCustomFunction.dictionaryCode2Value("CodeTerm", term)+"的教学任务书模板已存在");
					}
					task.setYearInfo(yearInfo);
					task.setTerm(term);
					task.setIsTemplate(Constants.BOOLEAN_YES);
					relationTeachTaskDetails(request, task);
				}
				teachTaskService.saveOrUpdate(task);
				map.put("statusCode", 200);
				map.put("message", "保存教学任务模板成功！");
				map.put("reloadUrl", request.getContextPath() +"/edu3/teaching/teachtask/template/input.html?resourceid="+task.getResourceid());
			} else {
				throw new WebException("年度与学期不能为空");
			}
		}catch (Exception e) {
			logger.error("保存教学任务模板出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败：<br/>"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}

	private void relationTeachTaskDetails(HttpServletRequest request,TeachTask task) throws ParseException {
		String[] tk_ids = request.getParameterValues("tk_id");
		String[] taskContents = request.getParameterValues("taskContent");
		String[] startTimes = request.getParameterValues("startTime");
		String[] endTimes = request.getParameterValues("endTime");
		String[] taskTypes = request.getParameterValues("taskType");
		String[] isAllowModifys = request.getParameterValues("isAllowModify");
		String[] showOrders = request.getParameterValues("showOrder");
		String[] warningTimes = request.getParameterValues("warningTime");
		if(tk_ids != null && tk_ids.length>0){
			for(int index=0;index<tk_ids.length;index++){
				TeachTaskDetails detail = new TeachTaskDetails();
				if(ExStringUtils.isNotEmpty(tk_ids[index])) {
					detail = (TeachTaskDetails) teachTaskDetailsService.get(tk_ids[index]);
				}
				detail.setTaskContent(ExStringUtils.trimToEmpty(taskContents[index]));
				detail.setStartTime(ExStringUtils.isEmpty(startTimes[index])? null : ExDateUtils.parseDate(startTimes[index], "yyyy-MM-dd HH:mm:ss"));
				detail.setEndTime(ExStringUtils.isEmpty(endTimes[index])? null : ExDateUtils.parseDate(endTimes[index], "yyyy-MM-dd HH:mm:ss"));
				detail.setTaskType(ExStringUtils.trimToEmpty(taskTypes[index]));
				detail.setIsAllowModify(ExStringUtils.defaultIfEmpty(isAllowModifys[index],Constants.BOOLEAN_YES));
				detail.setShowOrder(ExStringUtils.isEmpty(showOrders[index])?0:Long.parseLong(showOrders[index]));
				detail.setWarningTime(ExStringUtils.isEmpty(warningTimes[index])? null : ExDateUtils.parseDate(warningTimes[index], "yyyy-MM-dd HH:mm:ss"));
				detail.setStatus("unfinished");
				detail.setTeachTask(task);
				
				task.getTeachTaskDetails().add(detail);
			}			
		}
	}
	/**
	 * 删除教学任务书模板
	 * @param resourceid
	 * @param teachTaskId
	 * @param request
	 * @param response
	 * @param model
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/teachtask/template/remove.html")
	public void removeTeachTaskTemplate(String resourceid,String teachTaskId,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(resourceid)){
				teachTaskService.batchDelete(resourceid.split("\\,"));
				map.put("statusCode", 200);
				map.put("message", "删除成功！");	
				map.put("callbackType", "forward");
				map.put("forwardUrl", request.getContextPath()+"/edu3/teaching/teachtask/template/list.html");
			}
		} catch (Exception e) {
			logger.error("删除教学任务书模板出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	} 	
	
	@Autowired
	@Qualifier("teachtaskservice")
	private ITeachTaskService teachTaskService;
	
	@Autowired
	@Qualifier("teachTaskDetailsService")
	private ITeachTaskDetailsService teachTaskDetailsService;
	
	@Autowired
	@Qualifier("courseService")
	private ICourseService courseService;
	
	@Autowired
	@Qualifier("edumanagerService")
	private IEdumanagerService edumanagerService;
	
	@Autowired
	@Qualifier("studentLearnPlanService")
	private IStudentLearnPlanService studentLearnPlanService;

	@Autowired
	@Qualifier("yearInfoService")
	private IYearInfoService yearInfoService;
}
