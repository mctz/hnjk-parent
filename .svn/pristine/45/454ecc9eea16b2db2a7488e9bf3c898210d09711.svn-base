package com.hnjk.edu.teaching.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.utils.ExBeanUtils;
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.BaseSupportController;
import com.hnjk.edu.basedata.service.IYearInfoService;
import com.hnjk.edu.teaching.model.ExamSub;
import com.hnjk.edu.teaching.model.GradendDate;
import com.hnjk.edu.teaching.service.IExamSubService;

/**
 * 毕业论文批次预约管理
 * <code>GraduateThesisController</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2010-12-7 下午03:55:05
 * @see 
 * @version 1.0
 */
@Controller
@RequestMapping
public class GraduateThesisController extends BaseSupportController{
	
	private static final long serialVersionUID = 5621716322599098796L;

	@RequestMapping("/edu3/teaching/graduatethesis/list.html")
	public String exeList(String yearId, String term,String batchName, Page objPage, ModelMap model) throws WebException{
		objPage.setOrderBy("startTime");
		objPage.setOrder(Page.DESC);//设置默认排序方式
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件
		
		if(ExStringUtils.isNotEmpty(yearId)) {
			condition.put("yearId", yearId);
		}
		if(ExStringUtils.isNotEmpty(term)) {
			condition.put("term", term);
		}
		if(ExStringUtils.isNotEmpty(batchName)) {
			condition.put("batchName", batchName);
		}
		Page page = examSubService.findGraduateThesisByCondition(condition, objPage);
		
		model.addAttribute("condition", condition);
		model.addAttribute("thesisList", page);
		return "/edu3/teaching/thesis/thesis-list";
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
	@RequestMapping("/edu3/teaching/graduatethesis/edit.html")
	public String exeEdit(String resourceid,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		if(ExStringUtils.isNotBlank(resourceid)){ //-----编辑
			ExamSub thesis = examSubService.get(resourceid);	
			model.addAttribute("thesis", thesis);
		}else{ //----------------------------------------新增
			model.addAttribute("thesis", new ExamSub());
		}
		return "/edu3/teaching/thesis/thesis-form";
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
	@RequestMapping("/edu3/teaching/graduatethesis/save.html")
	public void exeSave(ExamSub examSub,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(examSub.getResourceid())){ //--------------------更新
				ExamSub p_examSub = examSubService.get(examSub.getResourceid());
				ExBeanUtils.copyProperties(p_examSub, examSub);
				GradendDate gd = saveRelation(p_examSub,request);
				p_examSub.setGradendDate(gd);
				examSubService.update(p_examSub);
			}else{ //-------------------------------------------------------------------保存
				GradendDate gd = saveRelation(examSub,request);
				examSub.setGradendDate(gd);
				examSubService.save(examSub);
			}
			map.put("statusCode", 200);
			map.put("message", "保存成功！");
			map.put("navTabId", "RES_TEACHING_THESIS_PLAN");
			map.put("reloadUrl", request.getContextPath() +"/edu3/teaching/graduatethesis/edit.html?resourceid="+examSub.getResourceid());
		}catch (Exception e) {
			logger.error("保存栏目出错：{}",e.fillInStackTrace());
			map.put("message", "保存失败！");
			map.put("statusCode", 300);
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}

	private GradendDate saveRelation(ExamSub examSub, HttpServletRequest request) {
		String yearId = request.getParameter("yearId");
		String gdresourceid = request.getParameter("gdresourceid");
		String publishDate = request.getParameter("publishDate");
		String syllabusEndDate = request.getParameter("syllabusEndDate");
		String firstDraftEndDate = request.getParameter("firstDraftEndDate");
		String secondDraftEndDate = request.getParameter("secondDraftEndDate");
		String oralexaminputStartTime = request.getParameter("oralexaminputStartTime");
		String oralexaminputEndTime = request.getParameter("oralexaminputEndTime");		
		if(ExStringUtils.isNotEmpty(yearId)) {
			examSub.setYearInfo(yearInfoService.get(yearId));
		}
		GradendDate gd = new GradendDate();
		if(ExStringUtils.isNotEmpty(gdresourceid)){
			gd = (GradendDate) examSubService.get(GradendDate.class, gdresourceid);
		}
		gd.setPublishDate(ExStringUtils.isNotEmpty(publishDate)?ExDateUtils.convertToDateTime(publishDate):null);
		gd.setSyllabusEndDate(ExStringUtils.isNotEmpty(syllabusEndDate)?ExDateUtils.convertToDateTime(syllabusEndDate):null);
		gd.setFirstDraftEndDate(ExStringUtils.isNotEmpty(firstDraftEndDate)?ExDateUtils.convertToDateTime(firstDraftEndDate):null);
		gd.setSecondDraftEndDate(ExStringUtils.isNotEmpty(secondDraftEndDate)?ExDateUtils.convertToDateTime(secondDraftEndDate):null);
		gd.setOralexaminputStartTime(ExStringUtils.isNotEmpty(oralexaminputStartTime)?ExDateUtils.convertToDateTime(oralexaminputStartTime):null);
		gd.setOralexaminputEndTime(ExStringUtils.isNotEmpty(oralexaminputEndTime)?ExDateUtils.convertToDateTime(oralexaminputEndTime):null);
		return gd;
	}
	
	
	/**
	 * 删除列表对象
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/graduatethesis/delete.html")
	public void exeDelete(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		String resourceid = request.getParameter("resourceid");
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(resourceid)){
				if(resourceid.split("\\,").length >1){//批量删除					
					examSubService.batchCascadeDelete(resourceid.split("\\,"));
				}else{//单个删除
					examSubService.delete(resourceid);
				}
				map.put("statusCode", 200);
				map.put("message", "删除成功！");				
				map.put("forward", request.getContextPath()+"/edu3/teaching/graduatethesis/list.html");
			}
		} catch (Exception e) {
			logger.error("删除栏目出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错:<br/>"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 修改状态
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/graduatethesis/states.html")
	public void exeStates(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		String resourceid = request.getParameter("resourceid");
		String states = request.getParameter("states");
		Map<String ,Object> map = new HashMap<String, Object>();
		String chinaStates = "";
		try {
			if(ExStringUtils.isNotBlank(resourceid) && ExStringUtils.isNotBlank(states)){ 
				if("open".equalsIgnoreCase(states)){ //开放
					chinaStates = "开放";
					examSubService.changeStates(resourceid,"2");
				}else if("close".equalsIgnoreCase(states)){ // 关闭
					chinaStates = "关闭";
					examSubService.changeStates(resourceid,"3");
				}
				map.put("statusCode", 200);
				map.put("message", chinaStates+"成功！");				
				map.put("forward", request.getContextPath()+"/edu3/teaching/graduatethesis/list.html");
			}
		} catch (Exception e) {
			logger.error(chinaStates+"栏目出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", chinaStates+"出错！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	@Autowired
	@Qualifier("examSubService")
	private IExamSubService examSubService;
	@Resource
	@Qualifier("yearInfoService")
	private IYearInfoService yearInfoService;

}
