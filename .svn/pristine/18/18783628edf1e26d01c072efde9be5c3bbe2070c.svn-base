package com.hnjk.edu.teaching.controller;

import java.io.File;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperPrint;

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
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.basedata.model.Examroom;
import com.hnjk.edu.basedata.service.IExamroomService;
import com.hnjk.edu.teaching.model.ExamInfo;
import com.hnjk.edu.teaching.model.ExamResults;
import com.hnjk.edu.teaching.model.ExamSub;
import com.hnjk.edu.teaching.service.IExamDistribuService;
import com.hnjk.edu.teaching.service.IExamResultsService;
import com.hnjk.edu.teaching.service.IExamSubService;
import com.hnjk.edu.teaching.service.IMergeExamRoomService;
import com.hnjk.edu.teaching.service.ITeachingJDBCService;
import com.hnjk.edu.teaching.vo.ExamRoomPlanVo;
import com.hnjk.edu.teaching.vo.FinalExamSeatVo;
import com.hnjk.extend.plugin.excel.service.IExportExcelService;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;

/**
 * 考试座位按排
 * @author luof
 *
 */
@Controller
public class ExamSeatController extends FileUploadAndDownloadSupportController {
	
	@Autowired
	@Qualifier("examResultsService")
 	private IExamResultsService examResultsService;
	
	@Autowired
	@Qualifier("examroomService")
	private IExamroomService examroomService;
	
	@Autowired
	@Qualifier("teachingJDBCService")
	private ITeachingJDBCService teachingJDBCService;
	
	@Autowired
	@Qualifier("examDistribuService")
	private IExamDistribuService examDistribuService;
	
	@Autowired
	@Qualifier("examSubService")
	private IExamSubService examSubService;
	
	@Autowired
	@Qualifier("mrgeExamRoomService")
	private IMergeExamRoomService mergeExamRoomService;
	
	@Autowired
	@Qualifier("exportExcelService")
	private IExportExcelService exportExcelService;
	
	
	/**
	 * 批量座位按排-列表
	 * @param request
	 * @param response
	 * @param model
	 * @param objPage
	 * @return
	 */
	@RequestMapping("/edu3/teaching/exam/seat/list.html")
	public String batchExamSeatList (HttpServletRequest request,HttpServletResponse response ,ModelMap model,Page objPage){

		Map<String,Object> condition = new HashMap<String, Object>(); 
		List examRoomList 			 = null;
		
		String branchSchool = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));
		String gradeid      = ExStringUtils.trimToEmpty(request.getParameter("gradeid"));
		String major        = ExStringUtils.trimToEmpty(request.getParameter("major"));
		String classic      = ExStringUtils.trimToEmpty(request.getParameter("classic"));
		String examSub      = ExStringUtils.trimToEmpty(request.getParameter("examSub"));
		String courseId     = ExStringUtils.trimToEmpty(request.getParameter("courseId"));
		String timeSegment  = ExStringUtils.trimToEmpty(request.getParameter("examSub_examTimeSegment"));
		String examMode     = ExStringUtils.trimToEmpty(request.getParameter("examMode"));
		
		Date startTime      = null;
		Date endTime        = null;
		
		User user = SpringSecurityHelper.getCurrentUser();
		if(user.getOrgUnit().getUnitType().indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员
			branchSchool = user.getOrgUnit().getResourceid();
			model.addAttribute("isBrschool", true);
		}
		
		//如果学习中习不为空，则查出学习中心的考场合并记录
		if (ExStringUtils.isNotEmpty(branchSchool)) {
			List<String> mergeExamBrschool  = mergeExamRoomService.findMergeExamRoomByUnitId(2, "'"+branchSchool+"'","","", examSub);
			if (null!=mergeExamBrschool && mergeExamBrschool.size()>0) {
				String mgrgeExamBrschoolIds = "";
				for (String id :mergeExamBrschool) {
					mgrgeExamBrschoolIds   += ",'"+id+"'";
				}
				mgrgeExamBrschoolIds       += ",'"+branchSchool+"'";
				condition.put("mergeExamBrschool", mgrgeExamBrschoolIds.substring(1));
			}
		}
		if (ExStringUtils.isNotEmpty(timeSegment)) {
			condition.put("examSub_examTimeSegment", timeSegment);
			String [] segment = timeSegment.split("TO");
			if (null!=segment && segment.length>1) {
				String s_t     = segment[0];
				String e_t     = segment[1];
				startTime      = ExDateUtils.convertToDateTime(s_t);
				endTime        = ExDateUtils.convertToDateTime(e_t);
			}
		}
		
		//获取所选择考试批次的考试时间段
		if (ExStringUtils.isNotEmpty(examSub)){
			condition.put("examSub",      examSub);
			List<Map<String, Object>> list = teachingJDBCService.findExamTimeSegment(examSub,examMode,Constants.BOOLEAN_YES);
			model.addAttribute("timeSegmentList",list);
			
		}      
		if (ExStringUtils.isNotEmpty(examMode)) {
			condition.put("examMode", examMode);
		}
		if (ExStringUtils.isNotEmpty(branchSchool)) {
			condition.put("branchSchool", branchSchool);
		}
		if (ExStringUtils.isNotEmpty(gradeid)) {
			condition.put("gradeid",      gradeid);
		}
		if (ExStringUtils.isNotEmpty(major)) {
			condition.put("major",        major);
		}
		if (ExStringUtils.isNotEmpty(classic)) {
			condition.put("classic",      classic);
		}
		if (ExStringUtils.isNotEmpty(courseId)) {
			condition.put("courseId",     courseId);
		}
		if (null!=startTime) {
			condition.put("startTime",    startTime);
		}
		if (null!=startTime) {
			condition.put("endTime",      endTime);
		}

		if (condition.containsKey("examSub")&&condition.containsKey("examSub_examTimeSegment")&&condition.containsKey("branchSchool")) {
			objPage           = teachingJDBCService.findExamCourseInfoByCondition(objPage, condition);
			examRoomList      = teachingJDBCService.findExamRoomInfoByBranchSchoolAndExamSub(condition);
		}
		model.addAttribute("objPage",objPage);
		model.addAttribute("examRoomList", examRoomList);
		model.addAttribute("condition", condition);
		
		if (condition.containsKey("startTime")) {
			condition.remove("startTime");
		}
		if (condition.containsKey("endTime")) {
			condition.remove("endTime");
		}
		
		return "/edu3/teaching/examSeat/batchExamSeat-list";
	}
	/**
	 * 批量座位按排-分配座位
	 * @param examResultIds  要分配座位的考试课程ID串
	 * @param examRoomIds    要分配座位的考场ID串
	 * @param reset          当同一个考场按排多门课程座位号需要从1开始
	 * @param examSubId      考试预约批次ID
	 * @param branchSchool   学习中心
	 * @param response         
	 * @return
	 */
	@RequestMapping("/edu3/teaching/exam/seat/assign.html")
	public void assignSeat(String examInfos,String examRoomIds,String examSubId,String reset,String branchSchool,HttpServletResponse response){
		
		Map<String,Object> resultMap = new HashMap<String, Object>();
		if (ExStringUtils.isNotEmpty(examInfos) && ExStringUtils.isNotEmpty(branchSchool) && ExStringUtils.isNotEmpty(examRoomIds) ) {
			resultMap       			 = examDistribuService.assignSeat(examInfos, examRoomIds, examSubId, branchSchool,Boolean.parseBoolean(reset));
		}else {
			resultMap.put("msg","考试课程、学习中心、试室是必需的条件!");
			resultMap.put("isAssignSuccess",false);
		}
		renderJson(response,JsonUtils.mapToJson(resultMap));
	}
	/**
	 * 查询考试批次对应的考试时间段
	 * @param request
	 * @param response
	 * @param resourceid  考试批次ID
	 */
	@RequestMapping("/edu3/teaching/exam/seat/getExameTimeSegment.html")
	public void getExameTimeSegment(HttpServletRequest request,HttpServletResponse response){
		
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		String examSubId               = ExStringUtils.trimToEmpty(request.getParameter("examSubId")); 
		String examMode                = ExStringUtils.trimToEmpty(request.getParameter("examMode")); 
		if (ExStringUtils.isNotEmpty(examSubId)) {
			list                       = teachingJDBCService.findExamTimeSegment(examSubId,examMode,Constants.BOOLEAN_YES);
		}
		renderJson(response,JsonUtils.listToJson(list));
	}
	
	/**
	 * 查看考场已按排座位的考试预约记录列表
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/teaching/exam/seat/viewExmaRoomResult.html")
	public String findExamResultsByExamRooId(HttpServletRequest request,ModelMap model){
		
		Map<String,Object> condition = new HashMap<String, Object> ();
		String examRoomId   = ExStringUtils.trimToEmpty(request.getParameter("examRoomId"));
		String examsubId    = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
		String examInfoId   = ExStringUtils.trimToEmpty(request.getParameter("examInfoId"));
		String branchSchool = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));
		String flag         = ExStringUtils.trimToEmpty(request.getParameter("flag"));
		String timeSegment  = ExStringUtils.trimToEmpty(request.getParameter("examSub_examTimeSegment"));
		Date startTime      = null;
		Date endTime        = null;
		if(ExStringUtils.isNotEmpty(examsubId)) {
			condition.put("examSub", examsubId);
		}
		if(ExStringUtils.isNotEmpty(examRoomId)) {
			condition.put("examRoomId", examRoomId);
		}
		if(ExStringUtils.isNotEmpty(examInfoId)) {
			condition.put("examInfoId", examInfoId);
		}
		if (ExStringUtils.isNotEmpty(flag)) {
			condition.put("flag", flag);
		}
		
		User user = SpringSecurityHelper.getCurrentUser();
		if(user.getOrgUnit().getUnitType().indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员
			branchSchool    = user.getOrgUnit().getResourceid();
		}
		if (ExStringUtils.isNotEmpty(branchSchool)) {
			
			List<String> mergeExamBrschool  = mergeExamRoomService.findMergeExamRoomByUnitId(2, "'"+branchSchool+"'","","", examsubId);
			if (null!=mergeExamBrschool && mergeExamBrschool.size()>0) {
				String mgrgeExamBrschoolIds = "";
				for (String id :mergeExamBrschool) {
					mgrgeExamBrschoolIds   += ",'"+id+"'";
				}
				mgrgeExamBrschoolIds       += ",'"+branchSchool+"'";
				condition.put("mergeExamBrschool", mgrgeExamBrschoolIds.substring(1));
			}
			if (!condition.containsKey("mergeExamBrschool")) {
				condition.put("branchSchool", branchSchool);
			}
		}
		
		if (ExStringUtils.isNotEmpty(timeSegment)) {
			String [] segment = timeSegment.split("TO");
			if (null!=segment && segment.length>1) {
				String s_t     = segment[0];
				String e_t     = segment[1];
				startTime      = ExDateUtils.convertToDateTime(s_t);
				endTime        = ExDateUtils.convertToDateTime(e_t);
				
				condition.put("startTime",startTime);
				condition.put("endTime",endTime);
			}
		}
		List<ExamResults> list = examResultsService.findHaveSeatExamResultList(condition);
		
		model.put("examResultList", list);
		model.put("condition", condition);
		
		return"/edu3/teaching/examSeat/assignedSeat-list";
	}
	/**
	 * 清空座位   可以同时清除课程、考场，也可以只清除课程或只清除考场
	 * @param examRoomId
	 * @param examSubId
	 * @param response
	 */
	@RequestMapping("/edu3/teaching/exam/seat/clearnExamSeat.html")
	public void clearnExamRoomSeat(HttpServletRequest request,HttpServletResponse response){
		
		Map<String,Object> condition = new HashMap<String, Object>();
		String examSubId             = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
		String examRoomId            = ExStringUtils.trimToEmpty(request.getParameter("examRoomId"));
		String examInfoId            = ExStringUtils.trimToEmpty(request.getParameter("examInfoId"));
		String branchSchool          = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));
		String timeSegment  		 = ExStringUtils.trimToEmpty(request.getParameter("examTimeSegment"));
		
		StringBuffer examResultId	 = new StringBuffer(); 
		StringBuffer roomIds         = new StringBuffer();
		StringBuffer infoIds         = new StringBuffer();
		
		String msg                   = "";
		boolean isSuccess            = false;    
		Date startTime      		 = null;
		Date endTime       			 = null;
		Date currentDate             = new Date();
		
		if(ExStringUtils.isNotEmpty(examSubId)) {
			condition.put("examSubId",examSubId);
		}
		
		try {
			
			User user 					 = SpringSecurityHelper.getCurrentUser();
			if(user.getOrgUnit().getUnitType().indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员
				branchSchool    = user.getOrgUnit().getResourceid();
			}
			if (ExStringUtils.isNotEmpty(branchSchool)) {
				condition.put("branchSchool", branchSchool);
				List<String> mergeExamBrschool  = mergeExamRoomService.findMergeExamRoomByUnitId(2, "'"+branchSchool+"'","","", examSubId);
				if (null!=mergeExamBrschool && mergeExamBrschool.size()>0) {
					String mgrgeExamBrschoolIds = "";
					for (String id :mergeExamBrschool) {
						mgrgeExamBrschoolIds   += ",'"+id+"'";
					}
					mgrgeExamBrschoolIds       += ",'"+branchSchool+"'";
					condition.put("mergeExamBrschool", mgrgeExamBrschoolIds.substring(1));
				}
			}
			//要清除座位的 考试时间段
			if (ExStringUtils.isNotEmpty(timeSegment)) {
				String [] segment  = timeSegment.split("TO");
				if (null!=segment && segment.length>1) {
					String s_t     = segment[0];
					String e_t     = segment[1];
					startTime      = ExDateUtils.convertToDateTime(s_t);
					endTime        = ExDateUtils.convertToDateTime(e_t);
					
					condition.put("startTime",startTime);
					condition.put("endTime",endTime);
				}
			}
			//要清除座位的考场
			if(ExStringUtils.isNotEmpty(examRoomId)){
				if (examRoomId.split(",").length>1) {
					for (String id:examRoomId.split(",")) {
						roomIds.append(",'"+id+"'");
					}
				}
			}
			//要清除座位的考试课程
			if(ExStringUtils.isNotEmpty(examInfoId)){
				if (examInfoId.split(",").length>1) {
					for (String id:examInfoId.split(",")) {
						infoIds.append(",'"+id+"'");
					}
				}
			}
			//在考试开始时间前允许清空座位
			if (null!=startTime&&currentDate.getTime()<startTime.getTime()) {
				//1.清空考场、考试课程 同时清空
				if (ExStringUtils.isNotEmpty(examInfoId)&&ExStringUtils.isNotEmpty(examRoomId)) {
					//1.1清空考场
					if (ExStringUtils.isNotEmpty(roomIds.toString())) {
						condition.put("examRoomIds", roomIds.substring(1));
					}else {
						condition.put("examRoomId", examRoomId);
					}
					
					examResultId.append(examResultsService.findExamResultIdsForClearnExamRoom(condition));
					isSuccess  		    = examDistribuService.clearnSeat(examResultId.toString());
				
					if (condition.containsKey("examRoomIds")) {
						condition.remove("examRoomIds");
					}
					if (condition.containsKey("examRoomId")) {
						condition.remove("examRoomId");
					}
					examResultId        = new StringBuffer();
					
					//1.2清空考试课程
					if (ExStringUtils.isNotEmpty(infoIds.toString())) {
						condition.put("examInfoIds", infoIds.substring(1));
					}else {
						condition.put("examInfoId",examInfoId);
					}
					
					examResultId.append(examResultsService.findExamResultIdsForClearnExamRoom(condition));
					isSuccess  		    = examDistribuService.clearnSeat(examResultId.toString());
					
				//2.清空考试课程	
				}else if (ExStringUtils.isNotEmpty(examInfoId)&&ExStringUtils.isEmpty(examRoomId)) {
					
					if (ExStringUtils.isNotEmpty(infoIds.toString())) {
						condition.put("examInfoIds", infoIds.substring(1));
					}else {
						condition.put("examInfoId",examInfoId);
					}
					
					examResultId.append(examResultsService.findExamResultIdsForClearnExamRoom(condition));
					isSuccess  		    = examDistribuService.clearnSeat(examResultId.toString());
					
				//3.清空考场
				}else if (ExStringUtils.isNotEmpty(examRoomId)&&ExStringUtils.isEmpty(examInfoId)) {
					if (ExStringUtils.isNotEmpty(roomIds.toString())) {
						condition.put("examRoomIds", roomIds.substring(1));
					}else {
						condition.put("examRoomId", examRoomId);
					}
					
					examResultId.append(examResultsService.findExamResultIdsForClearnExamRoom(condition));
					isSuccess  		    = examDistribuService.clearnSeat(examResultId.toString());
					
				}
				
			}else {
				msg                      = "考试已开始，不允许清空座位，考试时间:</br>"+ExDateUtils.formatDateStr(startTime,ExDateUtils.PATTREN_DATE_TIME );
			}
		} catch (Exception e) {
			logger.error("清空座位出错：{}"+e.fillInStackTrace());
			isSuccess  				     = false;
			msg                          = "清空座位出错:"+e.getMessage();
		}
		
		condition.clear();
		condition.put("isSuccess", isSuccess);
		condition.put("msg",msg);
	
		renderJson(response,JsonUtils.mapToJson(condition));
 	}
	/**
	 * 根据课程清空考场
	 * @param examRoomId
	 * @param examSubId
	 * @param courseId
	 * @param response
	 */
	@RequestMapping("/edu3/teaching/exam/seat/clearnRommSeatByCourse.html")
	public void clearnExamRoomSeatByCourse(HttpServletRequest request,HttpServletResponse response){

		Map<String,Object> condition = new HashMap<String, Object>();
		
		String major                 = ExStringUtils.trimToEmpty(request.getParameter("major"));
		String classic               = ExStringUtils.trimToEmpty(request.getParameter("classic"));
		String examSub 			 	 = ExStringUtils.trimToEmpty(request.getParameter("examSub"));
		String gradeid               = ExStringUtils.trimToEmpty(request.getParameter("gradeid"));
		String courseId 			 = ExStringUtils.trimToEmpty(request.getParameter("courseId"));
		String branchSchool          = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));
		
		User user 					 = SpringSecurityHelper.getCurrentUser();
		if(user.getOrgUnit().getUnitType().indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员
			branchSchool 			 = user.getOrgUnit().getResourceid();
		}
		
		if(ExStringUtils.isNotEmpty(major)) {
			condition.put("major",major);
		}
		if(ExStringUtils.isNotEmpty(classic)) {
			condition.put("classic",classic);
		}
		if(ExStringUtils.isNotEmpty(examSub)) {
			condition.put("examSubId",examSub);
		}
		if(ExStringUtils.isNotEmpty(gradeid)) {
			condition.put("gradeid",gradeid);
		}
		if(ExStringUtils.isNotEmpty(courseId)) {
			condition.put("courseId",courseId);
		}
		if(ExStringUtils.isNotEmpty(branchSchool)) {
			condition.put("branchSchool",branchSchool);
		}
		
		String examResultId			 = examResultsService.findExamResultIdsForClearnExamRoom(condition);
		boolean isSuccess  			 = examDistribuService.clearnSeat(examResultId);
		
		renderJson(response,JsonUtils.objectToJson(isSuccess));	
	}
	/**
	 * 打印考场座位-考场列表
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/edu3/teaching/exam/seat/examRoomSeatList.html")
	public String printExamRoomSeatList(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		
		Map<String,Object> condition = new HashMap<String, Object>(); 
		List examRoomList 			 = null;
		String branchSchool 		 = ExStringUtils.defaultIfEmpty(request.getParameter("branchSchool"),"");
		String examSub      		 = ExStringUtils.defaultIfEmpty(request.getParameter("examSub"),"");
		
		User user					 = SpringSecurityHelper.getCurrentUser();
		if(user.getOrgUnit().getUnitType().indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员
			branchSchool = user.getOrgUnit().getResourceid();
			model.addAttribute("isBrschool", true);
		}
		
		if (ExStringUtils.isNotEmpty(branchSchool)) {
			condition.put("branchSchool", branchSchool);
		}
		if (ExStringUtils.isNotEmpty(examSub)) {
			condition.put("examSub",      examSub);
		}

		
		if (!condition.isEmpty()&&condition.containsKey("examSub")) {
			examRoomList      = teachingJDBCService.findExamRoomInfoByBranchSchoolAndExamSub(condition);
		}

		model.addAttribute("examRoomList", examRoomList);
		model.addAttribute("condition", condition);
		
		return "/edu3/teaching/examSeat/examRoomSeat-list";
	}
	/**
	 * 打印考场座位表-预览
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/teaching/exam/seat/printRoomSeatList-view.html")
	public String printExamSeatView(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		model.addAttribute("examRoomIds", ExStringUtils.trimToEmpty(request.getParameter("examRoomIds")));
		model.addAttribute("examSubId",ExStringUtils.trimToEmpty(request.getParameter("examSubId")));
		model.addAttribute("branchSchool", ExStringUtils.trimToEmpty(request.getParameter("branchSchool")));
		return"/edu3/teaching/examSeat/examSeat-printview";
	}
	/**
	 * 打印考场座位表
	 * @param examRoomIds
	 * @param examSubId
	 * @param response
	 */
	@RequestMapping("/edu3/teaching/exam/seat/printRoomSeatList.html")
	public void printExamSeat(HttpServletRequest request,HttpServletResponse response){
		
		Connection conn         	 = null;
		try {
			Map<String,Object> param = new HashMap<String, Object>();
			JasperPrint jasperPrint  = null;
			
			String examRoomIds       = ExStringUtils.defaultIfEmpty(request.getParameter("examRoomIds"), "");
			String examSubId         = ExStringUtils.defaultIfEmpty(request.getParameter("examSubId"), "");
			String branchSchool      = ExStringUtils.defaultIfEmpty(request.getParameter("branchSchool"), "");
		
			if (!"".equals(examRoomIds)&&!"".equals(examSubId)) {
				
				String [] roomIdArray   = examRoomIds.split(",");
				conn 					= examDistribuService.getConn();
				String reprotFile       = URLDecoder.decode(request.getSession().getServletContext().getRealPath(File.separator+"reports"+
										  File.separator+"examSeat"+File.separator+"examSeatList.jasper"),	"utf-8");
				File reprot_file        = new File(reprotFile);
				
				ExamSub sub=examSubService.get(examSubId);
				param.put("examSubID",examSubId);
				param.put("examSubName", sub.getBatchName());
				param.put("branchSchool",branchSchool);
				param.put("scutLogoPath", CacheAppManager.getSysConfigurationByCode("web.scutlogo.path").getParamValue());
				param.put("school", CacheAppManager.getSysConfigurationByCode("graduateData.schoolName").getParamValue());//学校名称
				//param.put("scutLogoPath", "C:\\scut_logo.jpg");
				
				List<Examroom> roomList = examroomService.findByCriteria(Restrictions.eq("isDeleted", 0),Restrictions.in("resourceid", roomIdArray));
				for (int i=0;i<roomList.size();i++){

					Examroom room		= roomList.get(i);
					param.put("examRoomID", room.getResourceid());
					param.put("examRoomName", room.getExamroomName());
					
					if (i==0) {//第一次生成JasperPrint文件
						jasperPrint     = examDistribuService.doJasperPrint(reprot_file, param, conn);
					}else {   //如果选择多个考场一起打印则累加到第一次生成JasperPrint文件中，一起输出
						JasperPrint jasperPage = examDistribuService.doJasperPrint(reprot_file, param, conn);
						List jsperPageList	   = jasperPage.getPages();
						for (int j = 0; j < jsperPageList.size(); j++) {
							jasperPrint.addPage((JRPrintPage) jsperPageList.get(j));
						}
						jasperPage = null;//清除临时报表的内存占用
					}
				}
				
			}else {
				String msg = "考试批次和试室不能为空，请选择要打印座位的考试批次、试室!";
				
				renderHtml(response, msg);
			}
			if (null!=jasperPrint) {
				renderStream(response, jasperPrint);
			}else {
				renderHtml(response,"缺少打印数据！");
			}
		} catch (Exception e) {
			logger.error("打印考场座位表Controller出错{}",e.fillInStackTrace());
			renderHtml(response, "打印考场座位表出错!");
		}finally{
			if (null!=conn) {
				try {
					conn.close();
				} catch (SQLException e) {
					logger.error("打印考场座位表Controller关闭Connection连接出错:{}"+e.fillInStackTrace());
				}
			}
		}
	}
	/**
	 * 导出期末考试座位表
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/teaching/exam/seat/exportRoomSeatList.html")
	public void exportExamSeat(HttpServletRequest request,HttpServletResponse response){
	
			Map<String,List<Object>> data= new HashMap<String, List<Object>>();
			Map<String,Object> condition = new HashMap<String, Object>();
			String examRoomIds       	 = ExStringUtils.trimToEmpty(request.getParameter("examRoomIds"));
			String examSubId         	 = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
			examRoomIds 			 	 = examRoomIds.replace(",", "','");
			//String branchSchool      	 = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));
			
			if (ExStringUtils.isNotBlank(examSubId)) {
				condition.put("examSubID",examSubId);
			}
			if (ExStringUtils.isNotBlank(examRoomIds)) {
				condition.put("examRoomID", examRoomIds);
			}
			
			if (ExStringUtils.isNotBlank(examRoomIds)&&ExStringUtils.isNotBlank(examSubId)) {
				
				List<Map<String,Object>> result = teachingJDBCService.getExamRoomSeat(condition);
				String examPlan           = examSubService.get(examSubId).getBatchName(); //考试批次
				String unitCodeName 	  = result.get(0).get("unitname").toString();//教学点
				condition.clear();
				
				try{
					//将查出来的数据根据试室ID+考试课程ID分页
					for (Map<String,Object> tmp : result) {
						List<Object> list =  null;
						if (data.containsKey(tmp.get("classroomid").toString()+tmp.get("examinfoid").toString())) {
							list 		  = data.get(tmp.get("classroomid").toString()+tmp.get("examinfoid").toString());
						}else {
							list 		  = new ArrayList<Object>(0);
						}
						FinalExamSeatVo tmp_finalExamSeatVo = new FinalExamSeatVo();
						tmp_finalExamSeatVo.setExammode(tmp.get("exammode").toString());
						tmp_finalExamSeatVo.setClassicname(null==tmp.get("classicname")?"-":tmp.get("classicname").toString());
						tmp_finalExamSeatVo.setClassroomid(null==tmp.get("classroomid")?"-":tmp.get("classroomid").toString());
						tmp_finalExamSeatVo.setCoursecode(null==tmp.get("examcoursecode")?"":tmp.get("examcoursecode").toString()+"--");
						tmp_finalExamSeatVo.setCoursename(null==tmp.get("coursename")?"-":tmp.get("coursename").toString());
						tmp_finalExamSeatVo.setExamendtime(null==tmp.get("examtime")?"-":tmp.get("examtime").toString());
						tmp_finalExamSeatVo.setExamseatnum(null==tmp.get("examseatnum")?"-":tmp.get("examseatnum").toString());
						tmp_finalExamSeatVo.setExamstarttime(null==tmp.get("examdate")?"-":tmp.get("examdate").toString());
						tmp_finalExamSeatVo.setExamtype(null==tmp.get("examtype")?"-":tmp.get("examtype").toString());
						tmp_finalExamSeatVo.setMajorname(null==tmp.get("majorname")?"-":tmp.get("majorname").toString());
						tmp_finalExamSeatVo.setStudentname(null==tmp.get("studentname")?"-":tmp.get("studentname").toString());
						tmp_finalExamSeatVo.setStudyno(null==tmp.get("studyno")?"-":tmp.get("studyno").toString());
						tmp_finalExamSeatVo.setExamroomname(null==tmp.get("examroomname")?"-":tmp.get("examroomname").toString());
						list.add(tmp_finalExamSeatVo);
						data.put(tmp.get("classroomid").toString()+tmp.get("examinfoid").toString(), list);
					}
					//生成每页的参数
					Map paramMap;
					FinalExamSeatVo vo;
					for (String key : data.keySet()) {
						paramMap  = new HashMap();
						vo = (FinalExamSeatVo)data.get(key).get(0);
						
						paramMap.put("examPlan", examPlan);  //考试计划
						paramMap.put("unitCodeName", unitCodeName);//学习中心
						paramMap.put("examCode",vo.getCoursecode());//考试批次
						paramMap.put("courseName",vo.getCoursename());//课程名
						paramMap.put("examType",vo.getExamtype());//考试类型
						paramMap.put("examRoom", vo.getExamroomname());  //考试室
						paramMap.put("examDate",vo.getExamstarttime());//考试日期
						paramMap.put("examTime",vo.getExamendtime());//考试时间
						paramMap.put("examMode",vo.getExammode());
						paramMap.put("num",data.get(key).size());//人数统计
						condition.put(key, paramMap);
					}
					
					/*if(0<result.size()){
						//获得考试批次,教学点名
						
						for (Map<String,Object> tmp : result) {
							FinalExamSeatVo tmp_finalExamSeatVo = new FinalExamSeatVo();
							tmp_finalExamSeatVo.setExammode(tmp.get("exammode").toString());
							tmp_finalExamSeatVo.setClassicname(null==tmp.get("classicname")?"-":tmp.get("classicname").toString());
							tmp_finalExamSeatVo.setClassroomid(null==tmp.get("classroomid")?"-":tmp.get("classroomid").toString());
							//考试编码不是E+course的coursecode 而是  examinfo的examcoursecode
							tmp_finalExamSeatVo.setCoursecode(null==tmp.get("examcoursecode")?"-":tmp.get("examcoursecode").toString());
							tmp_finalExamSeatVo.setCoursename(null==tmp.get("coursename")?"-":tmp.get("coursename").toString());
							tmp_finalExamSeatVo.setExamendtime(null==tmp.get("examtime")?"-":tmp.get("examtime").toString());
							tmp_finalExamSeatVo.setExamseatnum(null==tmp.get("examseatnum")?"-":tmp.get("examseatnum").toString());
							tmp_finalExamSeatVo.setExamstarttime(null==tmp.get("examdate")?"-":tmp.get("examdate").toString());
							tmp_finalExamSeatVo.setExamtype(null==tmp.get("examtype")?"-":tmp.get("examtype").toString());
							tmp_finalExamSeatVo.setMajorname(null==tmp.get("majorname")?"-":tmp.get("majorname").toString());
							tmp_finalExamSeatVo.setStudentname(null==tmp.get("studentname")?"-":tmp.get("studentname").toString());
							tmp_finalExamSeatVo.setStudyno(null==tmp.get("studyno")?"-":tmp.get("studyno").toString());
							tmp_finalExamSeatVo.setExamroomname(null==tmp.get("examroomname")?"-":tmp.get("examroomname").toString());
							list.add(tmp_finalExamSeatVo);
						}
					}
					//分段信息
					if(list.size()>0){
						Integer begin = 0;
						Integer end   = 0;
						String key    = list.get(0).getCoursecode()+"#"+list.get(0).getCoursename()+"#"
									  + list.get(0).getExamtype()+"#"+list.get(0).getUnitcode()+"#"
								      + list.get(0).getUnitname()+"#"+list.get(0).getExamroomname()+"#"
								      + list.get(0).getExamstarttime()+"#"+list.get(0).getExamendtime()+"#"
								      + list.get(0).getExammode()+"#";
					
						for (int t=0;t<list.size();t++) {
							FinalExamSeatVo tmp_finalExamSeatVo= list.get(t);
							String tmp = tmp_finalExamSeatVo.getCoursecode()+"#"+tmp_finalExamSeatVo.getCoursename()+"#"
									   + tmp_finalExamSeatVo.getExamtype()+"#"+tmp_finalExamSeatVo.getUnitcode()+"#"
									   + tmp_finalExamSeatVo.getUnitname()+"#"+tmp_finalExamSeatVo.getExamroomname()+"#"
									   + tmp_finalExamSeatVo.getExamstarttime()+"#"+tmp_finalExamSeatVo.getExamendtime()+"#"
									   + tmp_finalExamSeatVo.getExammode()+"#";
									
							if(key.equals(tmp)){
								if(t==list.size()-1){
									end = t;
									String map_range= begin+"#"+end+"#"+key;
									range_list.add(map_range);
								}else{
									continue;
								}
							}else{
								end=t-1;
								String map_range= begin+"#"+end+"#"+key;
								range_list.add(map_range);
								begin=t;
								key=tmp;
								
								if(t==list.size()-1){
									String end_range= t+"#"+t+"#"+tmp;
									range_list.add(end_range);
								}
							}
						}
					}
//					for (String string : range_list) {
//						System.out.println(string);
//					}
*/				
				}catch(Exception e){
					logger.error("生成期末考试座位表数据出错:{}"+e.fillInStackTrace());
				}

				//文件输出服务器路径
				setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
				try{
					
					GUIDUtils.init();
					File disFile 				   = new File(getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(false) + ".xls");
					Map<String,Object> templateMap = new HashMap<String, Object>();//设置模板
					
					templateMap.put("examPlan", examPlan);  //考试计划
					templateMap.put("unitCodeName", unitCodeName);//学习中心
					templateMap.put("schoolAndConnect", CacheAppManager.getSysConfigurationByCode("graduateData.schoolName").getParamValue()+
							CacheAppManager.getSysConfigurationByCode("graduateData.schoolConnectName").getParamValue());
					//模板文件路径
					String templateFilepathString = "export_finalExamSeatTable.xls";
					//初始化参数
					exportExcelService.initParmasByfile(disFile, "finalExamSeat", result,null);//初始化配置参数
					exportExcelService.getModelToExcel().setRowHeight(500);//设置行高
					exportExcelService.getModelToExcel().setTemplateParam(templateFilepathString, 4, templateMap);				
					File excelFile  = exportExcelService.getModelToExcel().getExcelfileByTemplateForSheets1(data,condition);//获取导出的文件
					
					logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());
					downloadFile(response, "期末考试座位.xls", excelFile.getAbsolutePath(),true);
				}catch (Exception e) {
					e.printStackTrace();
					logger.error("导出excel文件出错："+e.fillInStackTrace());
				}
			}else {
				String msg = "考试批次和试室不能为空，请选择要导出座位的考试批次、试室!";
				renderHtml(response, msg);
			}
		
	}
	/**
	 * 查看座位安排汇总情况
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/teaching/exam/seat/examSeatAssignInfo.html")
	public String examSeatAssignInfo(HttpServletRequest request,ModelMap model,Page page){
		
		Map<String, Object> condition = new HashMap<String, Object>();
		User user              		  = SpringSecurityHelper.getCurrentUser();
		String branchSchool    		  = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));
		String examSubId       		  = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
		String examTimeSegment 		  = ExStringUtils.trimToEmpty(request.getParameter("examTimeSegment"));
		String examMode               = ExStringUtils.trimToEmpty(request.getParameter("examMode"));
		
		Date examStartDate            = null;
		Date examEndDate              = null;
		
		
		if(user.getOrgUnit().getUnitType().indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员
			branchSchool 			  = user.getOrgUnit().getResourceid();
			condition.put("isBrschool", "Y");
		}else {
			condition.put("isBrschool", "N");
		}
		
		if (ExStringUtils.isNotEmpty(examSubId)){
			condition.put("examSubId", examSubId);
			List<Map<String, Object>> list = teachingJDBCService.findExamTimeSegment(examSubId,examMode,Constants.BOOLEAN_YES);
			model.put("timeSegmentList",list);
		} 	
		if (ExStringUtils.isNotEmpty(examTimeSegment)) {
			condition.put("examTimeSegment", examTimeSegment);
			String [] segment = examTimeSegment.split("TO");
			if (null!=segment && segment.length>1) {
				String s_t     = segment[0];
				String e_t     = segment[1];
				examStartDate  = ExDateUtils.convertToDateTime(s_t);
				examEndDate    = ExDateUtils.convertToDateTime(e_t);
			}
		}
		
		if (ExStringUtils.isNotEmpty(branchSchool)) {
			condition.put("branchSchool", branchSchool);
		}
		if (null!=examStartDate) {
			condition.put("examStartDate", examStartDate);
		}
		if (null!=examEndDate) {
			condition.put("examEndDate", examEndDate);
		}
		
		if (condition.containsKey("examSubId")||condition.containsKey("examTimeSegment")) {
			page = teachingJDBCService.examSeatAssignInfo(page,condition);
		}
		
		if (condition.containsKey("examStartDate")) {
			condition.remove("examStartDate");
		}
		if (condition.containsKey("examEndDate")) {
			condition.remove("examEndDate");
		}
		
		model.put("condition", condition);
		model.put("page", page);
		
		return "/edu3/teaching/examSeat/examSeatAssignedInfo-list";
	}
	/**
	 * 导出试室安排总表条件
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/exam/seat/exportExamRoomPlanExcelCondition.html")
	public String exportExamRoomPlanCondition(HttpServletRequest request,HttpServletResponse response,ModelMap model)throws WebException{
		return "/edu3/teaching/examSeat/examRoomPlanExcelCondition";
	}
	/**
	 * 导出试室安排总表
	 * @param request
	 * @param response
	 * @param model
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/exam/seat/exportExamRoomPlanExcel.html")
	public void exportExamRoomPlan(HttpServletRequest request,HttpServletResponse response,ModelMap model)throws WebException{
		//从库中查出数据		
		String brSchooId = ExStringUtils.trimToEmpty(request.getParameter("brSchooId"));
		String examPlanId = ExStringUtils.trimToEmpty(request.getParameter("examPlanId"));
		String unitShortName = "";
		String examPlanName = ""; 
		Map<String,Object> condition = new HashMap<String, Object>(0);
		if(!"".equals(brSchooId)) {
			condition.put("brSchooId",brSchooId );
		}
		if(!"".equals(examPlanId)) {
			condition.put("examPlanId",examPlanId );
		}
		List<Map<String,Object>> result = teachingJDBCService.statExamRoomPlan(condition);
		List<ExamRoomPlanVo> list = new ArrayList<ExamRoomPlanVo>(0);
		try{
			if(0<result.size()){
				//获得学习中心简称以及考试批次
				unitShortName =String.valueOf(result.get(0).get("UNITSHORTNAME"));
				examPlanName = String.valueOf(result.get(0).get("BATCHNAME"));
				//记录试室人数
				Map<String,Long> map = new HashMap<String, Long>(0);
				for (Map<String, Object> tmp : result) {
					String examTime = String.valueOf(tmp.get("examtime"));
					String eRoom = String.valueOf(tmp.get("EXAMROOMNAME"));
					Long count = Long.valueOf(String.valueOf(tmp.get("C")));
					String key = examTime+"#"+eRoom;
					if(map.containsKey(key)){
						count += map.get(key);
						map.put(key, count);
					}else{
						map.put(key, count);
					}
				}
				ExamRoomPlanVo examRoomPlanVo;
				for (Map<String, Object> tmp : result) {
					examRoomPlanVo = new ExamRoomPlanVo();
					Set<String> set = tmp.keySet();
					String examTime ="";//考试时间
					String examRoom ="";//试室
					String doubleSeatNum = "";//双隔位容量
					for (String string : set) {
						String key =string;
						String value = String.valueOf(tmp.get(string));
						if("UNITNAME".equals(key)){examRoomPlanVo.setBranchSchool(value);}//学习中心
						else if("COURSENAME".equals(key)){examRoomPlanVo.setExamSubject(value);}//考试科目
						else if("COURSECODE".equals(key)){examRoomPlanVo.setExamCode(value);}//考试编号
						else if("examtype".equals(key)){examRoomPlanVo.setExamType(value);}//考试形式
						else if("C".equals(key)){examRoomPlanVo.setExamSum(value);}//考试人数
						else if("EXAMROOMNAME".equals(key)){examRoomPlanVo.setExamRoomNo(value); //试室号
						examRoom=value;}
						else if("DOUBLESEATNUM".equals(key)){examRoomPlanVo.setDoubleSeat(value);//双隔位容量
						doubleSeatNum = value;}
						else if("examtime".equals(key)){examRoomPlanVo.setExamTime(value);examTime=value;}//考试时间
					}
					
					//教室总人数 和剩余人数
					if(map.containsKey(examTime+"#"+examRoom)){
						String total = String.valueOf(map.get(examTime+"#"+examRoom));
						if(!"null".equals(total)){
							examRoomPlanVo.setRoomSum(total);
						}else{
							examRoomPlanVo.setRoomSum("");
						}
						//有的考试室没有双隔位数量
						if(!"null".equals(doubleSeatNum)){
							String remain = String.valueOf(Integer.valueOf(doubleSeatNum)-Integer.valueOf(total));
							examRoomPlanVo.setRemainSum(remain);
						}else{
							examRoomPlanVo.setRemainSum("");
						}	
					}
					list.add(examRoomPlanVo);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		FileUploadAndDownloadSupportController fileup = new FileUploadAndDownloadSupportController();
		//文件输出服务器路径
		fileup.setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
		try{
			File excelFile = null;
			GUIDUtils.init();
			File disFile = new File(fileup.getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(false) + ".xls");
			Map<String,Object> templateMap = new HashMap<String, Object>();//设置模板
			templateMap.put("brSchool", unitShortName);
			templateMap.put("examPlan", examPlanName);
			templateMap.put("choolAndConnect", CacheAppManager.getSysConfigurationByCode("graduateData.schoolName").getParamValue()+
					CacheAppManager.getSysConfigurationByCode("graduateData.schoolConnectName").getParamValue());
			
			//模板文件路径
			String templateFilepathString = SystemContextHolder.getAppRootPath()+"WEB-INF"+File.separator+"templates"
											+File.separator+"excel"+File.separator+"export_examRoomPlan_template.xls";
			//字典
			//List<String> dictCodeList = new ArrayList<String>();
			
			//初始化参数(加字典)
			exportExcelService.initParmasByfile(disFile, "examRoomPlanTable", list,null);//初始化配置参数
			exportExcelService.getModelToExcel().setRowHeight(500);//设置行高
			exportExcelService.getModelToExcel().setTemplateParam(templateFilepathString, 3, templateMap);
			
			excelFile = exportExcelService.getModelToExcel().getExcelfileByTemplate2();
			logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());
			fileup.downloadFile(response, unitShortName+"_"+examPlanName+"试室安排总表.xls", excelFile.getAbsolutePath(),true);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("导出excel文件出错："+e.fillInStackTrace());
		}
	}
	
	//===============================安排考试座位=============================== start
	/**
	 * 通过课程批量座位按排-列表
	 * @param request
	 * @param response
	 * @param model
	 * @param objPage
	 * @return
	 */
	@RequestMapping("/edu3/teaching/exam/seat/listbycourse.html")
	public String batchExamSeatByCourseList (HttpServletRequest request,HttpServletResponse response ,ModelMap model,Page objPage){
		objPage.setPageSize(100);
		Map<String,Object> condition = new HashMap<String, Object>(); 
		List examRoomList 			 = null;
		
		String branchSchool = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));
		String gradeid      = ExStringUtils.trimToEmpty(request.getParameter("gradeid"));
		String major        = ExStringUtils.trimToEmpty(request.getParameter("major"));
		String classic      = ExStringUtils.trimToEmpty(request.getParameter("classic"));
		String examSub      = ExStringUtils.trimToEmpty(request.getParameter("examSub"));
		String courseId     = ExStringUtils.trimToEmpty(request.getParameter("courseId"));
		//String timeSegment  = ExStringUtils.trimToEmpty(request.getParameter("examSub_examTimeSegment"));
		String examMode     = ExStringUtils.trimToEmpty(request.getParameter("examMode"));
		String examSubId = ExStringUtils.trimToEmpty(request.getParameter("examSub"));
		String examPlace = ExStringUtils.trimToEmpty(request.getParameter("examplace"));
		
		
		//获得当前考试批次下课程
		List<ExamSub> examList = examSubService.findOpenedExamSub("exam");//当前考试批次
		List<Course> courseList = new ArrayList<Course>();	
		if(ExStringUtils.isNotEmpty(examSubId)){
			ExamSub sub = examSubService.get(examSubId);
				 Set<ExamInfo> examSet = sub.getExamInfo();
				 for(ExamInfo info : examSet){
					 courseList.add(info.getCourse());
				 }
			
			condition.put("examSubId", sub.getResourceid());
		}else{
			if(examList.size()>0){
				Set<ExamInfo> examSet;
				for(ExamSub sub :examList){
					 examSet = sub.getExamInfo();
					 for(ExamInfo info : examSet){
						 courseList.add(info.getCourse());
					 }
					 condition.put("examSubId", sub.getResourceid());
				}
			}
		}
		//课程排序
		 ComparatorCourse comparator=new ComparatorCourse();
		 Collections.sort(courseList, comparator);
		model.addAttribute("courseList", courseList);
		
//		Date startTime      = null;
//		Date endTime        = null;
		
		User user = SpringSecurityHelper.getCurrentUser();
		if(user.getOrgUnit().getUnitType().indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员
			branchSchool = user.getOrgUnit().getResourceid();
			model.addAttribute("isBrschool", true);
			condition.put("isBrschool", "true");
		}
		
		//如果学习中习不为空，则查出学习中心的考场合并记录
		if (ExStringUtils.isNotEmpty(branchSchool)) {
			List<String> mergeExamBrschool  = mergeExamRoomService.findMergeExamRoomByUnitId(2, "'"+branchSchool+"'","","", examSub);
			if (null!=mergeExamBrschool && mergeExamBrschool.size()>0) {
				StringBuilder mgrgeExamBrschoolIds = new StringBuilder(1000);
				for (String id :mergeExamBrschool) {
					mgrgeExamBrschoolIds.append(",'").append(id).append("'");
				}
				mgrgeExamBrschoolIds.append(",'").append(branchSchool).append("'");
				condition.put("mergeExamBrschool", mgrgeExamBrschoolIds.substring(1));
			}
		}
//		if (ExStringUtils.isNotEmpty(timeSegment)) {
//			condition.put("examSub_examTimeSegment", timeSegment);
//			String [] segment = timeSegment.split("TO");
//			if (null!=segment && segment.length>1) {
//				String s_t     = segment[0];
//				String e_t     = segment[1];
//				startTime      = ExDateUtils.convertToDateTime(s_t);
//				endTime        = ExDateUtils.convertToDateTime(e_t);
//			}
//		}
		
//		//获取所选择考试批次的考试时间段
//		if (ExStringUtils.isNotEmpty(examSub)){
//			condition.put("examSub",      examSub);
//			List<Map<String, Object>> list = teachingJDBCService.findExamTimeSegment(examSub,examMode,Constants.BOOLEAN_YES);
//			model.addAttribute("timeSegmentList",list);
//			
//		}      
		if (ExStringUtils.isNotEmpty(examMode)) {
			condition.put("examMode", examMode);
		}
		if (ExStringUtils.isNotEmpty(branchSchool)) {
			condition.put("branchSchool", branchSchool);
		}
		if (ExStringUtils.isNotEmpty(gradeid)) {
			condition.put("gradeid",      gradeid);
		}
		if (ExStringUtils.isNotEmpty(major)) {
			condition.put("major",        major);
		}
		if (ExStringUtils.isNotEmpty(classic)) {
			condition.put("classic",      classic);
		}
		if (ExStringUtils.isNotEmpty(courseId)) {
			condition.put("courseId",     courseId);
		}
		if (ExStringUtils.isNotEmpty(examPlace)) {
			condition.put("examPlace",     examPlace);
		}
//		if (null!=startTime)    					condition.put("startTime",    startTime);
//		if (null!=startTime)    					condition.put("endTime",      endTime);

		if (condition.containsKey("examSubId")&&condition.containsKey("courseId")) {
			try {
				//判断是否是校内考试
//				Page page1 = new Page();
//				String isBr = teachingJDBCService.findCourseByCondition(page1, condition);
//				
				objPage           = teachingJDBCService.findExamCourseStudentByCondition(objPage, condition);
			} catch (Exception e) {
				e.printStackTrace();
			}
			examRoomList      = teachingJDBCService.findExamRoomInfoByBranchSchoolAndExamSub(condition);
		}
		
		model.addAttribute("objPage",objPage);
		model.addAttribute("examRoomList", examRoomList);
		model.addAttribute("condition", condition);
		
		if (condition.containsKey("startTime")) {
			condition.remove("startTime");
		}
		if (condition.containsKey("endTime")) {
			condition.remove("endTime");
		}
		
		return "/edu3/teaching/examSeat/batchExamSeatByCourse-list";
	}
	
	public class ComparatorCourse implements Comparator{

		 @Override
		 public int compare(Object arg0, Object arg1) {
		  Course course0=(Course)arg0;
		  Course course1=(Course)arg1;

		   //首先比较年龄，如果年龄相同，则比较名字

		  return course0.getCourseCode().compareTo(course1.getCourseCode());
		 
		 }
		 
	}
	
	/**
	 * 按课程批量座位按排-分配座位
	 * @param examResultIds  要分配座位的考试课程ID串
	 * @param examRoomIds    要分配座位的考场ID串
	 * @param reset          当同一个考场按排多门课程座位号需要从1开始
	 * @param examSubId      考试预约批次ID
	 * @param branchSchool   学习中心
	 * @param response         
	 * @return
	 */
	@RequestMapping("/edu3/teaching/exam/seat/assignbycourse.html")
	public void assignSeatByCourse(String results,String examRoomIds,String examSubId,String reset,String branchSchool,HttpServletResponse response){
		
		Map<String,Object> resultMap = new HashMap<String, Object>();
		if (ExStringUtils.isNotEmpty(results) && ExStringUtils.isNotEmpty(examRoomIds) ) {
			resultMap       			 = examDistribuService.assignSeatByCourse(results, examRoomIds, examSubId, branchSchool,Boolean.parseBoolean(reset));
		}else {
			resultMap.put("msg","考试课程、试室是必需的条件!");
			resultMap.put("isAssignSuccess",false);
		}
		renderJson(response,JsonUtils.mapToJson(resultMap));
	}

	//===============================安排考试座位=============================== end

}
