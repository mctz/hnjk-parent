package com.hnjk.edu.evaluate.controller;

import java.util.HashMap;
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
import com.hnjk.edu.evaluate.model.QuestionBank;
import com.hnjk.edu.evaluate.service.IQuestionBankService;

@Controller
public class QuestionBankController extends BaseSupportController{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8405259885562154355L;
	
	@Autowired
	@Qualifier("iQuestionBankService")
	private IQuestionBankService iQuestionBankService;
	
	/**
	 * 问卷题目列表
	 * @param objPage
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/edu3/teaching/quality/evaluation/questionBank/list.html")
	public String exeQuestionList(Page objPage,ModelMap model) throws Exception{
		Map<String,Object> condition = new HashMap<String,Object>();
		objPage.setOrder(Page.ASC);
		Page page = iQuestionBankService.findQuestionBankByCondition(condition, objPage);
		double faceSum=iQuestionBankService.getFaceTotalScore("0");
		model.addAttribute("questionPage", page);
		model.addAttribute("faceSum", faceSum);
		model.addAttribute("condition", condition);
		return "/edu3/evaluate/questionBank/question-list";
	}
	/**
	 * 修改问卷题目
	 * @param resourceid
	 * @param request
	 * @param response
	 * @param model
	 * @return 
	 * @throws Exception
	 */
	@RequestMapping("/edu3/teaching/quality/evaluation/questionBank/edit.html")
	public String addQuestion(String resourceid,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws Exception{
		if(ExStringUtils.isNotBlank(resourceid)){
			QuestionBank qb = iQuestionBankService.get(resourceid);
			model.addAttribute("questionBank", qb);
		}else{
			model.addAttribute("questionBank", new QuestionBank());
		}
		return "/edu3/evaluate/questionBank/question-form";
	}
	/**
	 * 保存问卷
	 * @param qb
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/edu3/teaching/quality/evaluation/questionBank/save.html")
	public void saveQuestion(QuestionBank qb,HttpServletRequest request,HttpServletResponse response) throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		try {			
			boolean isSuccess=iQuestionBankService.saveQuestion(qb);
			if(isSuccess){
				map.put("statusCode", 200);
				map.put("message", "保存成功！");
				map.put("navTabId", "RES_TEACHING_QUALITY_EVALUATION_QUESTIONBANK");
//				map.put("reloadUrl", request.getContextPath() +"/edu3/teaching/quality/evaluation/questionBank/edit.html?resourceid="+tmp.getResourceid());
			}else{
				map.put("statusCode", 300);
				map.put("message", "保存失败！请刷新网页后再试");
			}
		} catch (Exception e) {
			logger.error("保存问卷题目出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败:<br/>"+e.getMessage());
		}		
		renderJson(response, JsonUtils.mapToJson(map));
	}	
	
	/**
	 * 删除问卷
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/edu3/teaching/quality/evaluation/questionBank/delete.html")
	public void deleteQuestion(HttpServletRequest request,HttpServletResponse response) throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		String resourceids = request.getParameter("resourceid");
		boolean isSuccess = iQuestionBankService.deleteQuestion(resourceids);
		if(isSuccess){
			map.put("statusCode", 200);
			map.put("message", "删除成功！");				
		}else{
			map.put("statusCode", 300);
			map.put("message", "删除失败！请刷新网页后再试");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
}
