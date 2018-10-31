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

import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.BaseSupportController;
//import com.hnjk.edu.recruit.model.ExamRoomPlan;
//import com.hnjk.edu.recruit.model.RecruitExamPlan;
import com.hnjk.edu.recruit.model.RecruitPlan;
//import com.hnjk.edu.recruit.service.IExamRoomPlanService;
//import com.hnjk.edu.recruit.service.IRecruitExamPlanService;
import com.hnjk.edu.recruit.service.IRecruitPlanService;
import com.hnjk.edu.teaching.model.ExamSub;
import com.hnjk.edu.teaching.model.MergeExamroom;
import com.hnjk.edu.teaching.service.IExamSubService;
import com.hnjk.edu.teaching.service.IMergeExamRoomService;
import com.hnjk.edu.teaching.service.ITeachingJDBCService;
import com.hnjk.security.model.OrgUnit;
import com.hnjk.security.service.IOrgUnitService;
/**
 * 合并考场Controller
 * <p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2011-8-23 下午16:00:37
 * @see
 * @version 1.0
 */
@Controller
public class MergeExamRoomController extends BaseSupportController{

	private static final long serialVersionUID = -1075919390418024817L;

	@Autowired
	@Qualifier("mrgeExamRoomService")
	private IMergeExamRoomService mrgeExamRoomService;
	
	@Autowired
	@Qualifier("orgUnitService")
	private IOrgUnitService orgUnitService;
	
	@Autowired
	@Qualifier("examSubService")
	private IExamSubService examSubService;
	
	@Autowired
	@Qualifier("recruitPlanService")
	private IRecruitPlanService recruitPlanService;
	
//	@Autowired
//	@Qualifier("recruitExamPlanService")
//	private IRecruitExamPlanService recruitExamPlanService;
//	
//	@Autowired
//	@Qualifier("examRoomPlanService")
//	private IExamRoomPlanService examRoomPlanService;
	
	@Autowired
	@Qualifier("teachingJDBCService")
	private ITeachingJDBCService teachingJDBCService;
	/**
	 * 合并考场-表单
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/teaching/mergeExamRoom/form.html")
	public String mergeExamRoomEditForm(HttpServletRequest request,ModelMap model){
		String resourceid = ExStringUtils.trimToEmpty(request.getParameter("resourceid"));
		if (ExStringUtils.isNotEmpty(resourceid)) {
			MergeExamroom room = mrgeExamRoomService.get(resourceid);
			model.put("mergeExamroom", room);
		}
		return "/edu3/teaching/mergeexamroom/mergeexamroom-form";
	}
	/**
	 * 合并考场-保存
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/teaching/mergeExamRoom/save.html")
	public void mergeExamRoomEdit(HttpServletRequest request,HttpServletResponse response){
		
		Map<String,Object> map    = new HashMap<String, Object>();
		String resourceid 	      = ExStringUtils.trimToEmpty(request.getParameter("resourceid"));
		String examType 	      = ExStringUtils.trimToEmpty(request.getParameter("examType"));
		String outBranchSchool    = ExStringUtils.trimToEmpty(request.getParameter("outBranchSchool"));
		String inBranchSchool     = ExStringUtils.trimToEmpty(request.getParameter("inBranchSchool"));
		String recruitPlan        = ExStringUtils.trimToEmpty(request.getParameter("recruitPlan"));
		String examSub    	      = ExStringUtils.trimToEmpty(request.getParameter("examSub"));
		String examPlan    	      = ExStringUtils.trimToEmpty(request.getParameter("examPlan"));
		try {
			
			MergeExamroom room 	  = new MergeExamroom();
			RecruitPlan plan   	  = null;
			ExamSub sub        	  = null;
			//RecruitExamPlan ePlan = null;
			OrgUnit inUnit     	  = orgUnitService.get(inBranchSchool);
			OrgUnit outUnit    	  = orgUnitService.get(outBranchSchool);
			
			if (ExStringUtils.isNotEmpty(recruitPlan)) {
				plan           	  = recruitPlanService.get(recruitPlan);
			}
			if (ExStringUtils.isNotEmpty(examPlan)) {
				//ePlan             = recruitExamPlanService.get(examPlan);
			}
			if (ExStringUtils.isNotEmpty(examSub)) {
				sub            	  = examSubService.get(examSub);
			}
			
			//修改
			if (ExStringUtils.isNotEmpty(resourceid)) {
//				int seatNum       = queryExamSeat(examType,plan,ePlan,sub);
//				if (seatNum>0) {
//					throw new Exception("已安排座位不允许修改考场合并记录！");
//				}
				room              = mrgeExamRoomService.get(resourceid);
			}
			
			room.setExamType(Integer.valueOf(examType));
			room.setInBrSchool(inUnit);
			room.setOutBrSchool(outUnit);
			room.setRecruitPlan(plan);
			//room.setRecruitExamPlan(ePlan);
			room.setExamSub(sub);
				
			mrgeExamRoomService.saveOrUpdate(room);
			
			map.put("statusCode", 200);
			map.put("message", "保存成功！");
			map.put("navTabId", "RES_TEACHING_EXAM_MERGEEXAMROOM_EIDT");
			map.put("reloadUrl", request.getContextPath() +"/edu3/teaching/mergeExamRoom/form.html?resourceid="+room.getResourceid() );
		} catch (Exception e) {
			logger.error("保存考场合并出错:{}"+e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败:<br/>"+e.getMessage());
		}
		renderJson(response,JsonUtils.mapToJson(map));
	}

	/**
	 * 合并考场-删除
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/teaching/mergeExamRoom/del.html")
	public void mergeExamRoomDel (HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> map    = new HashMap<String, Object>();
		String resourceid 	      = ExStringUtils.trimToEmpty(request.getParameter("resourceid"));
		try {
			MergeExamroom room 	  = mrgeExamRoomService.get(resourceid);
//			int seatNum   		  = queryExamSeat(room.getExamType().toString(),room.getRecruitPlan(),room.getRecruitExamPlan(),room.getExamSub());
//			if (seatNum>0) {
//				throw new Exception("已安排座位不允许删除考场合并记录！");
//			}else {
				mrgeExamRoomService.delete(room);
//			}
		} catch (Exception e) {
			logger.error("删除考场合并出错:{}"+e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除失败:<br/>"+e.getMessage());
		}
		renderJson(response,JsonUtils.mapToJson(map));
	}
	/**
	 * 查询合并考场对应的考试是否已安排座位
	 * @param examType
	 * @param plan
	 * @param ePlan
	 * @param sub
	 * @return
	 */
/*	private int queryExamSeat(String examType,RecruitPlan plan,RecruitExamPlan ePlan ,ExamSub sub ){
		StringBuffer hql    = new StringBuffer();
		if ("1".equals(examType)) {
			hql.append(" from "+ExamRoomPlan.class.getSimpleName()+" roomPlan where roomPlan.isDeleted=0 and roomPlan.recruitPlanId='"+plan.getResourceid()+"'");
			if (null!=ePlan) {
				hql.append(" and roomPlan.recruitExamPlan.resourceid='"+ePlan.getResourceid()+"'"); 
			}
			return examRoomPlanService.findByHql(hql.toString()).size();
		}else if("2".equals(examType)){
			
		}

		return 0;
	} */
	/**
	 * 合并考场-列表
	 * @param request
	 * @param model
	 * @param page
	 * @return
	 */
	@RequestMapping("/edu3/teaching/mergeExamRoom/list.html")
	public String mergeExamRoomList(HttpServletRequest request,ModelMap model,Page page){
		page.setOrder(Page.ASC);
		page.setOrderBy(" examType ");
		Map<String,Object> condition = new HashMap<String, Object>();
		
		String examType              = ExStringUtils.trimToEmpty(request.getParameter("examType"));
		String outBranchSchool       = ExStringUtils.trimToEmpty(request.getParameter("outBranchSchool"));
		String inBranchSchool        = ExStringUtils.trimToEmpty(request.getParameter("inBranchSchool"));
		String recruitPlan           = ExStringUtils.trimToEmpty(request.getParameter("recruitPlan"));
		String examSub               = ExStringUtils.trimToEmpty(request.getParameter("examSub"));


		if(ExStringUtils.isNotEmpty(examSub)) {
			condition.put("examSub", examSub);
		}
		if(ExStringUtils.isNotEmpty(examType)) {
			condition.put("examType", Integer.valueOf(examType));
		}
		if(ExStringUtils.isNotEmpty(recruitPlan)) {
			condition.put("recruitPlan", recruitPlan);
		}
		if(ExStringUtils.isNotEmpty(inBranchSchool)) {
			condition.put("inBranchSchool", inBranchSchool);
		}
		if(ExStringUtils.isNotEmpty(outBranchSchool)) {
			condition.put("outBranchSchool", outBranchSchool);
		}
	
		
		page = mrgeExamRoomService.findMergeExamRoomByCondition(page, condition);
		
		model.put("page", page);
		model.put("condition", condition);
		
		return "/edu3/teaching/mergeexamroom/mergeexamroom-list";
	}
	
	/**
	 * 查询入学考试和期末考试的考试人数和预约人数
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/mergeExamRoom/statExamNumStudent.html")
	public void statExam(HttpServletRequest request,HttpServletResponse response)throws WebException{
		String recruitPlanId = ExStringUtils.trimToEmpty(request.getParameter("recruitPlanId"));
		String examTypeId = ExStringUtils.trimToEmpty(request.getParameter("examTypeId"));
		String examPlanId = ExStringUtils.trimToEmpty(request.getParameter("examPlanId"));
		String srcSchool = ExStringUtils.trimToEmpty(request.getParameter("srcSchool"));
		String desSchool = ExStringUtils.trimToEmpty(request.getParameter("desSchool"));
		List<Map<String, Object>> jsonList = new ArrayList<Map<String,Object>>(0);
		if(("1").equals(examTypeId)){
			Map<String, Object> condition = new HashMap<String, Object>(0);
			//入学考试以招生批次为条件
			if(!"".equals(recruitPlanId)) {
				condition.put("recruitplanid", recruitPlanId);
			}
			if(!"".equals(srcSchool)) {
				condition.put("srcschool", srcSchool);
			}
			if(!"".equals(desSchool)) {
				condition.put("desschool", desSchool);
			}
			jsonList = teachingJDBCService.statRecruitExamNum(condition);
		}
		if(("2").equals(examTypeId)){
			Map<String, Object> condition = new HashMap<String, Object>(0);
			//期末考试以考试批次为条件
			if(!"".equals(examPlanId)) {
				condition.put("examsubid", examPlanId);
			}
			if(!"".equals(srcSchool)) {
				condition.put("srcschool", srcSchool);
			}
			if(!"".equals(desSchool)) {
				condition.put("desschool", desSchool);
			}
			jsonList=teachingJDBCService.statExamNum(condition);
		}
		renderJson(response, JsonUtils.listToJson(jsonList));
	}
}
