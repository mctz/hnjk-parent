package com.hnjk.platform.system.controller;

import java.util.Date;
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
import com.hnjk.core.foundation.utils.HttpHeaderUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.BaseSupportController;
import com.hnjk.edu.util.Condition2SQLHelper;
import com.hnjk.platform.system.UserOperationLogsHelper;
import com.hnjk.platform.system.model.SetTime;
import com.hnjk.platform.system.model.UserOperationLogs;
import com.hnjk.platform.system.service.ISetTimeService;

/**
 * 时间设置Controller
 * @author Zik
 *
 */
@Controller
public class SetTimeController extends BaseSupportController {

	private static final long serialVersionUID = -3318391770677288687L;

	@Qualifier("setTimeService")
	@Autowired
	private ISetTimeService setTimeService;
	
	/**
	 * 获取设置时间列表
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/system/settime/list.html")
	public String listSetTime(HttpServletRequest request,Page objPage,ModelMap model) throws WebException{
		objPage.setOrderBy("businessType");
		objPage.setOrder("asc");
		
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件
		String businessType = request.getParameter("businessType");
		if(ExStringUtils.isNotEmpty(businessType)){
			condition.put("businessType", businessType);
		}
		
		Page page = setTimeService.findSetTimeByCondition(condition, objPage);
		model.addAttribute("settimeList",page);
		model.addAttribute("condition", condition);
		return "/system/settime/settime-list";
	}
	
	/**
	 * 新增/编辑表单
	 * @param resourceid
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/system/settime/edit.html")
	public String exeEdit(String resourceid,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		SetTime setTime = null;
		if(ExStringUtils.isNotBlank(resourceid)){ //-----编辑
			setTime = setTimeService.get(resourceid);	
		}else{ //----------------------------------------新增
			setTime = new SetTime();			
		}
		model.addAttribute("setTime", setTime);
		return "/system/settime/settime-form";
	}
	
	/**
	 * 保存/更新设置时间
	 * @param setTime
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/system/settime/save.html")
	public void exeSave(SetTime setTime,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		int statusCode = 200;
		String message = "保存成功！";
		try {
			do{
				SetTime _setTime = null;
				if(ExStringUtils.isNotBlank(setTime.getResourceid())){ //  更新
					_setTime = setTimeService.get(setTime.getResourceid());	
				}else{ // 保存
					_setTime = new SetTime();
				}		
				if(!setTime.getBusinessType().equals(_setTime.getBusinessType())){
					SetTime hasSetTime = setTimeService.getSetTimeByBusinessType(setTime.getBusinessType());
					if(hasSetTime != null){
						statusCode = 300;
						message = "该类型的时间设置已经存在！";
						continue;
					}
				}
				
				ExBeanUtils.copyProperties(_setTime, setTime);
				setTimeService.saveOrUpdate(_setTime);
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "6", UserOperationLogs.UPDATE,"保存/更新缴费时间：参数："+JsonUtils.mapToJson(Condition2SQLHelper.getConditionFromResquestByIterator(request)));
				map.put("navTabId", "RES_FINANCE_PAYMENT_SETTIME_UPDATE");
				map.put("reloadUrl", request.getContextPath() +"/edu3/system/settime/edit.html?resourceid="+_setTime.getResourceid());
			} while(false);
		}catch (Exception e) {
			logger.error("保存设置时间出错",e);
			statusCode = 300;
			message = "保存失败！";
		} finally {
			map.put("statusCode", statusCode);
			map.put("message", message);
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 批量删除时间设置
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/system/settime/delete.html")
	public void exeDelete(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		String resourceids = request.getParameter("resourceid");
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(resourceids)){
				if(resourceids.split(",").length >1){//批量删除					
					setTimeService.batchCascadeDelete(resourceids.split(","));
				}else{//单个删除
					setTimeService.delete(resourceids);
				}
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "6", UserOperationLogs.DELETE,"删除缴费时间：参数："+JsonUtils.mapToJson(Condition2SQLHelper.getConditionFromResquestByIterator(request)));
				map.put("statusCode", 200);
				map.put("message", "删除成功！");				
				map.put("forward", request.getContextPath()+"/edu3/ssystem/settime/list.html");
			}
		} catch (Exception e) {
			logger.error("删除时间设置出错",e);
			map.put("statusCode", 300);
			map.put("message", "删除失败！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 验证缴费时间信息
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/payTime/validData.html")
	public void validPosData(HttpServletRequest request, HttpServletResponse response) throws WebException {
		Map<String, Object> map = new HashMap<String, Object>();
		int statusCode = 200;
		String message = "验证通过！";
		try {
			do{
				SetTime payTime = setTimeService.getSetTimeByBusinessType("payTime");
				if(payTime==null){
					statusCode = 400;
					message = "请先设置缴费时间！";
					continue;
				}
				Date today = new Date();
				if(today.before(payTime.getBeginDate())){
					statusCode = 400;
					message = "还未到缴费时间，不能进行缴费操作！";
					continue;
				}
				if(today.after(payTime.getEndDate())){
					statusCode = 400;
					message = "缴费时间已结束，不能进行缴费操作！";
					continue;
				}
			
			} while(false);
		} catch (Exception e) {
			logger.error("验证缴费时间信息", e);
			statusCode = 300;
			message = "验证缴费时间信息失败！";
		} finally {
			map.put("statusCode", statusCode);
			map.put("message", message);
		}
		
		renderJson(response,JsonUtils.mapToJson(map));
	}
}
