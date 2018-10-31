package com.hnjk.edu.teaching.controller;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.utils.ExBeanUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.FileUploadAndDownloadSupportController;
import com.hnjk.core.support.context.Constants;
import com.hnjk.core.support.context.SystemContextHolder;
import com.hnjk.edu.basedata.model.Edumanager;
import com.hnjk.edu.basedata.model.Grade;
import com.hnjk.edu.basedata.model.Major;
import com.hnjk.edu.basedata.service.IEdumanagerService;
import com.hnjk.edu.basedata.service.IGradeService;
import com.hnjk.edu.basedata.service.IMajorService;
import com.hnjk.edu.teaching.model.TeachingPlanCourse;
import com.hnjk.edu.teaching.model.TeachingRecords;
import com.hnjk.edu.teaching.service.ITeachingPlanCourseService;
import com.hnjk.edu.teaching.service.ITeachingPlanService;
import com.hnjk.edu.teaching.service.ITeachingRecordsService;
import com.hnjk.edu.util.Condition2SQLHelper;
import com.hnjk.extend.plugin.excel.service.IImportExcelService;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.service.IAttachsService;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.OrgUnit;
import com.hnjk.security.model.User;
import com.hnjk.security.service.IOrgUnitService;

@Controller
public class TeachingRecordsController extends FileUploadAndDownloadSupportController{

	private static final long serialVersionUID = -6990220806955675016L;
	
	@Autowired
	@Qualifier("teachingRecordsService")
	private ITeachingRecordsService teachingRecordsService;
	
	@Autowired
	@Qualifier("importExcelService")
	private IImportExcelService importExcelService;
	
	@Autowired
	@Qualifier("attachsService")
	private IAttachsService attachsService;
	
	@Autowired
	@Qualifier("orgUnitService")
	private IOrgUnitService orgUnitService;
	
	@Autowired
	@Qualifier("majorService")
	private IMajorService majorService;	

	@Autowired
	@Qualifier("gradeService")
	private IGradeService gradeService;
	
	@Autowired
	@Qualifier("teachingplanservice")
	private ITeachingPlanService teachingPlanService;
	
	@Autowired
	@Qualifier("teachingPlanCourseService")
	private ITeachingPlanCourseService teachingPlanCourseService;
	
	@Autowired
	@Qualifier("edumanagerService")
	private IEdumanagerService edumanagerService;
	
	@RequestMapping("/edu3/teaching/teachingrecords/list.html")
	public String findTeachingRecordsPage(HttpServletRequest request,Page objPage, ModelMap model) throws WebException {
		objPage.setOrder(Page.DESC);//设置默认排序方式		
		//查询条件
		Map<String,Object> condition = Condition2SQLHelper.getConditionFromResquestByIterator(request);
		Page page=null;
		
		try {	
			User user = SpringSecurityHelper.getCurrentUser();
			if(user.getOrgUnit().getUnitType().indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员
				model.addAttribute("isBrschool", true);
				model.addAttribute("brschoolName", user.getOrgUnit().getUnitName());
				condition.put("brSchoolId", user.getOrgUnit().getResourceid());
			}	
			page = teachingRecordsService.findTeachingRecordsByHql(condition, objPage);
		}catch (ServiceException e) {
			logger.error("查询出错", e);
		}
		
		model.addAttribute("page", page);
		model.addAttribute("condition", condition);	
		
		return "/edu3/teaching/teachingRecords/teachingRecords-list";
	}
	
	
	/**
	 * 下载模板
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/teaching/teachingrecords/download-file.html")
	public void downloadERModel(HttpServletRequest request,HttpServletResponse response) throws WebException{
		try{
			setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
			//模板文件路径
			String templateFilepathString = "teachingRecordsModel.xls";
			downloadFile(response, "教学记录导入模版.xls", templateFilepathString,false);
		}catch(Exception e){			
			String msg = "导出excel文件出错：找不到该文件-教学记录导入模版.xls";
			logger.error("下载教学记录导入模版出错", e);
			renderHtml(response, "<script>alertMsg.warn("+"\""+msg+"\""+")</script>");
		}
	}
	
	/**
	 * 新增/编辑 教学记录
	 * @param resourceid
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/teaching/teachingrecords/input.html")
	public String editTeachingRecords(HttpServletRequest request,ModelMap model)  throws WebException{
		User user = SpringSecurityHelper.getCurrentUser();
		String resourceid =  request.getParameter("resourceid");
		String brSchoolId = request.getParameter("brSchoolId");
		String majorId = request.getParameter("majorId");
		if(user.getOrgUnit().getUnitType().indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员
			model.addAttribute("brschool", true);
		}
		TeachingRecords teachRecord = null;
		Map<String, Object> values = new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer(" from "+TeachingPlanCourse.class.getSimpleName()+" where isDeleted=0 ");
		if(ExStringUtils.isNotBlank(resourceid)){ //-----编辑
			teachRecord = teachingRecordsService.get(resourceid);
			majorId = teachRecord.getMajor().getResourceid();
		}else{ //----------------------------------------新增
			teachRecord = new TeachingRecords();
			if(user.getOrgUnit().getUnitType().indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员
				teachRecord.setUnit(user.getOrgUnit());
				teachRecord.setTeacher(edumanagerService.get(user.getResourceid()));
				model.addAttribute("brschool", true);
			}else if(ExStringUtils.isNotBlank(brSchoolId)){
				teachRecord.setUnit(orgUnitService.get(brSchoolId));
			}
		}
		if(ExStringUtils.isNotBlank(majorId)){
			teachRecord.setMajor(majorService.get(majorId));
			values.put("majorId", majorId);
			hql.append("and teachingPlan.major.resourceid=:majorId");
			List<TeachingPlanCourse> planCourses = teachingPlanCourseService.findByHql(hql.toString(), values);
			if(planCourses!=null && planCourses.size()>0){
				String courseIds = "";
				for (TeachingPlanCourse teachingPlanCourse : planCourses) {
					courseIds += "'"+teachingPlanCourse.getCourse().getResourceid()+"',";
				}
				courseIds = courseIds.substring(1, courseIds.length()-2);
				model.addAttribute("courseIds", courseIds);
			}
		}
		model.addAttribute("teachRecord", teachRecord);
		return "/edu3/teaching/teachingRecords/teachingRecords-form";
	}
	
	
	/**
	 * 保存 教学记录
	 * @param teachRecord
	 * @param request
	 * @param response
	 * @param model
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/teachingrecords/save.html")
	public void saveTeachingRecords(TeachingRecords teachRecord,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			do{
				String brSchoolId = request.getParameter("brSchoolId");
				String gradeId = request.getParameter("gradeId");
				String majorId = request.getParameter("majorId");
				String courseId = request.getParameter("courseId");
				String teacherId = request.getParameter("teacherId");
				OrgUnit unit = null;
				Grade grade = null;
				Major major = null;
				Edumanager user = null;
				TeachingPlanCourse planCourse = null;
				boolean flag = true;
				Map<String, Object> condition = new HashMap<String, Object>();
				if(ExStringUtils.isNotBlank(brSchoolId)){
					unit = orgUnitService.get(brSchoolId);
				}
				if(ExStringUtils.isNotBlank(gradeId)){
					grade = gradeService.get(gradeId);
					condition.put("gradeid", gradeId);
				}
				if(ExStringUtils.isNotBlank(majorId)){
					major = majorService.get(majorId);
					condition.put("majorid", majorId);
				}
				if(ExStringUtils.isNotBlank(teacherId)){
					user = edumanagerService.get(teacherId);
				}
				if(ExStringUtils.isNotBlank(courseId)){
					condition.put("course", courseId);
					List<TeachingPlanCourse> planCourseList = teachingPlanCourseService.findTeachingPlanCourse(condition);
					if(planCourseList!=null && planCourseList.size()>0){
						planCourse = planCourseList.get(0);
					}else {
						map.put("statusCode", 300);
						map.put("message", "无法关联到教学计划课程！");
						flag = false;
					}
				}
				if(flag){
					if(ExStringUtils.isNotBlank(teachRecord.getResourceid())){ //--------------------更新
						TeachingRecords _tTeachingRecords = teachingRecordsService.get(teachRecord.getResourceid());
						ExBeanUtils.copyProperties(_tTeachingRecords, teachRecord);
						teachRecord = _tTeachingRecords;
					}
					if(unit!=null) {
						teachRecord.setUnit(unit);
					}
					if(grade!=null) {
						teachRecord.setGrade(grade);
					}
					if(major!=null) {
						teachRecord.setMajor(major);
					}
					if(user!=null) {
						teachRecord.setTeacher(user);
					}
					if(planCourse!=null) {
						teachRecord.setPlanCourse(planCourse);
					}
					teachingRecordsService.saveOrUpdate(teachRecord);
					map.put("statusCode", 200);
					map.put("message", "保存成功！");
					map.put("navTabId", "RES_TEACHING_TEACHINGRECORDS_INPUT");
					//map.put("reloadUrl", request.getContextPath() +"/edu3/teaching/teachingrecords/input.html?resourceid="+teachRecord.getResourceid());
				}
			}while(false);
		}catch (Exception e) {
			logger.error("保存教学记录出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 删除 教学记录
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/teachingrecords/remove.html")
	public void deletedTeachingRecords(HttpServletRequest request,HttpServletResponse response) throws WebException{
		String resourceid = request.getParameter("resourceid");
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(resourceid)){
				if(resourceid.split("\\,").length >0) {
					teachingRecordsService.batchDelete(resourceid.split("\\,"));
				}
				map.put("statusCode", 200);
				map.put("message", "删除成功！");				
				map.put("forward", request.getContextPath()+"/edu3/teaching/teachingrecords/list.html");
			}
		} catch (Exception e) {
			logger.error("删除出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 打印预览、导出
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value={"/edu3/teaching/teachingrecords/printView.html","/edu3/teaching/teachingrecords/export.html"})
	public String printOrExport(HttpServletRequest request,HttpServletResponse response, ModelMap model) throws WebException{
		String resourceids = request.getParameter("ids");
		String flag		   = request.getParameter("flag");
		
		String url = "";
		Map<String, Object> condition = Condition2SQLHelper.getConditionFromResquestByIterator(request);
		if("export".equals(flag)){//导出
			List<TeachingRecords> teachRecordList = null;
			if(ExStringUtils.isNotBlank(resourceids)){//勾选
				teachRecordList = teachingRecordsService.findEntitysByIds(TeachingRecords.class, Arrays.asList(resourceids.split(",")));
			}else {//查询
				teachRecordList = teachingRecordsService.findTeachingRecordsListByCondition(condition);
			}
			String schoolName = CacheAppManager.getSysConfigurationByCode("graduateData.schoolName").getParamValue();
			String fileName = "教学进度表";
			try {
				fileName = URLEncoder.encode(schoolName+fileName+".xls", "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			if(teachRecordList!=null && teachRecordList.size()>0){
				TeachingRecords teachingRecords = teachRecordList.get(0);
				model.addAttribute("school", schoolName+"成人高等教育教学进度表");
				model.addAttribute("courseName", teachingRecords.getPlanCourse().getCourse().getCourseName());
				model.addAttribute("gradeName", teachingRecords.getGrade().getGradeName());
				model.addAttribute("majorName", teachingRecords.getMajor().getMajorName());
				model.addAttribute("classroom", teachingRecords.getClassroom());
			}
			model.addAttribute("list", teachRecordList);
			response.setContentType("application/vnd.ms-excel");
			//attachment
		  	response.setHeader("Content-Disposition", "attachment; filename="+fileName); 
			url = "/edu3/teaching/teachingRecords/teachingRecords-export";
		}else {//打印
			model.addAttribute("resourceids", resourceids);
			 for (String key : condition.keySet()) {
				 model.addAttribute(key, condition.get(key));
			}
			url = "/edu3/teaching/teachingRecords/teachingRecords-printView";
		}
		return url;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/edu3/teaching/teachingrecords/print.html")
	public void printTeachRecord(HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> condition = Condition2SQLHelper.getConditionFromResquestByIterator(request);
		String resourceids = request.getParameter("ids");
		try {
			List<TeachingRecords> teachRecordList = null;
			String reprotFile       = URLDecoder.decode(request.getSession().getServletContext().getRealPath(File.separator+"reports"+
					  File.separator+"teaching"+File.separator+"teachingRecords.jasper"),	"utf-8");
			File reprot_file        = new File(reprotFile);
			if(ExStringUtils.isNotBlank(resourceids)){//勾选
				teachRecordList = teachingRecordsService.findEntitysByIds(TeachingRecords.class, Arrays.asList(resourceids.split(",")));
			}else {//查询
				teachRecordList = teachingRecordsService.findTeachingRecordsListByCondition(condition);
			}
			List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
			TeachingRecords teachingRecords = null;
			if(teachRecordList!=null && teachRecordList.size()>0){
				for (TeachingRecords temp : teachRecordList) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("week", temp.getWeek());
					map.put("timePeriod", temp.getTimeperiod());
					map.put("contents", temp.getContents());
					map.put("studyHour", temp.getPlanCourse().getStydyHour());
					map.put("teachType", temp.getTeachType());
					map.put("cnName", temp.getTeacher().getCnName());
					Edumanager manager = (Edumanager) temp.getTeacher();
					map.put("technical", manager.getTitleOfTechnical());
					map.put("mobile", manager.getMobile()==null?manager.getOfficeTel():manager.getMobile());
					dataList.add(map);
				}
				teachingRecords = teachRecordList.get(0);
			}
			Map<String,Object> map  = new HashMap<String, Object>();
			if(teachingRecords!=null){
				String schoolName = CacheAppManager.getSysConfigurationByCode("graduateData.schoolName").getParamValue();
				map.put("school", schoolName+"成人高等教育教学进度表");
				map.put("courseName", teachingRecords.getPlanCourse().getCourse().getCourseName());
				map.put("gradeName", teachingRecords.getGrade().getGradeName());
				map.put("majorName", teachingRecords.getMajor().getMajorName());
				map.put("classroom", teachingRecords.getClassroom());
			}
			JRMapCollectionDataSource dataSource = new JRMapCollectionDataSource(dataList);
			JasperPrint jasperPrint = JasperFillManager.fillReport(reprot_file.getPath(), map, dataSource); // 填充报表
			
			if (null!=jasperPrint) {
				renderStream(response, jasperPrint);
			}else {
				renderHtml(response,"缺少打印数据！");
			}
			
		} catch (Exception e) {
			logger.error("打印教学记录出错{}",e.fillInStackTrace());
			renderHtml(response, "打印教学记录出错!");
		}
	}
}
