package com.hnjk.edu.evaluate.controller;

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

import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.BaseSupportController;
import com.hnjk.edu.basedata.service.IYearInfoService;
import com.hnjk.edu.evaluate.model.QuestionnaireBatch;
import com.hnjk.edu.evaluate.service.IQuestionnaireBatchService;
import com.hnjk.edu.util.Condition2SQLHelper;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;

@Controller
public class QuestionnaireBatchController extends BaseSupportController{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6391160547125626263L;
	
	@Autowired
	@Qualifier("iQuestionnaireBatchService")
	private IQuestionnaireBatchService iQuestionnaireBatchService;
	
	@Autowired
	@Qualifier("yearInfoService")
	private IYearInfoService iYearInfoService;

	/**
	 * 教评批次列表
	 * @param objPage
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/edu3/teaching/quality/evaluation/questionnaireBatch/list.html")
	public String exeEvaluationBatch(Page objPage,ModelMap model,HttpServletRequest request) throws Exception{
		Map<String,Object> condition = Condition2SQLHelper.getConditionFromResquest(request);
		
		objPage.setOrder(Page.ASC);
		Page page = iQuestionnaireBatchService.findQuestionnaireBatch(condition, objPage);		
		model.addAttribute("page", page);		
		model.addAttribute("condition", condition);
		return "/edu3/evaluate/questionnaireBatch/questionnaireBatch-list";
	}
	/**
	 * 教评批次 更新/编辑
	 * @param resourceid
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/edu3/teaching/quality/evaluation/questionnaireBatch/edit.html")
	public String editQuestionnaireBatch(String resourceid,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws Exception{
		if(ExStringUtils.isNotBlank(resourceid)){
			QuestionnaireBatch qb = iQuestionnaireBatchService.get(resourceid);
			model.addAttribute("questionnaireBatch", qb);
		}else{
			model.addAttribute("questionnaireBatch", new QuestionnaireBatch());
		}
		return "/edu3/evaluate/questionnaireBatch/questionnaireBatch-form";
	}
	/**
	 * 教评批次 保存
	 * @param qb
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/edu3/teaching/quality/evaluation/questionnaireBatch/save.html")
	public void saveQuestionnaireBatch(QuestionnaireBatch qb,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String yearId = request.getParameter("yearId");
		int statusCode=200;
		String message="";
		try {
			do {
				if (ExStringUtils.isNotBlank(qb.getResourceid())) {// 更新
					QuestionnaireBatch qesb = iQuestionnaireBatchService.get(qb.getResourceid());
					if(ExStringUtils.isNotBlank(qesb.getIsPublish()) && qesb.getIsPublish().equals("Y")){
						statusCode = 300;
						message= "该教评批次已存在发布，不能进行编辑！";
						break;
					}
				} else {// 新增
						// 检查是否已经存在相同的年度、学期的教评批次
					Map<String, Object> condition = new HashMap<String, Object>();
					condition.put("yearId", yearId);
					condition.put("term", request.getParameter("term"));
					List<QuestionnaireBatch> qblist = iQuestionnaireBatchService
							.findQuestionnaireBatchList(condition);
					if (qblist.size() > 0) {
						statusCode = 300;
						message= "保存失败！该年度、学期的教评批次已存在！";
						break;
					}
					
				}
				boolean isSuccess = iQuestionnaireBatchService.saveQuestionnaireBatch(qb, yearId);
				if (isSuccess) {
					message= "保存成功";
				} else {
					statusCode = 300;
					message= "保存失败！";
				}
				map.put("navTabId","RES_TEACHING_QUALITY_EVALUATION_QUESTIONNAIREBATCH");
				// map.put("reloadUrl", request.getContextPath()
				// +"/edu3/teaching/quality/evaluation/questionBank/edit.html?resourceid="+tmp.getResourceid());

			} while (false);
		} catch (Exception e) {
			logger.error("保存问卷题目出错：{}", e.fillInStackTrace());
			statusCode = 300;
			message = "保存失败:<br/>" + e.getMessage();
		} finally{
			map.put("statusCode", statusCode);
			map.put("message", message);
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 教评批次 删除
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/edu3/teaching/quality/evaluation/questionnaireBatch/delete.html")
	public void deleteQuestionnaireBatch(HttpServletRequest request,HttpServletResponse response) throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		String resourceids = request.getParameter("resourceid");
		
		boolean isSuccess=iQuestionnaireBatchService.deleteQuestionnaireBatch(resourceids);
		if(isSuccess){
			map.put("statusCode", 200);
			map.put("message", "删除成功！");				
		}else{
			map.put("statusCode", 300);
			map.put("message", "已经发布的批次不允许删除");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 发布教评批次，发布时，会根据年度、学期生成相应的问卷
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/edu3/teaching/quality/evaluation/questionnaireBatch/publish.html")
	public void publishQuestionnaireBatch(HttpServletRequest request,HttpServletResponse response) throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		String resourceids = request.getParameter("resourceid");
		User cureentUser = SpringSecurityHelper.getCurrentUser();
		String brSchoolId="";
		if(null!=cureentUser.getOrgUnit() && cureentUser.getOrgUnit().getUnitType().
				indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){
			brSchoolId= cureentUser.getOrgUnit().getResourceid();//教学点账号
		}
		boolean isSuccess=iQuestionnaireBatchService.publishQuestionnaireBatch(resourceids,brSchoolId);
		if(isSuccess){
			map.put("statusCode", 200);
			map.put("message", "发布成功！");				
		}else{
			map.put("statusCode", 300);
			map.put("message", "发布失败！请刷新网页后再试");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
}
