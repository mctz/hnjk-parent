package com.hnjk.edu.basedata.controller;

import java.util.Enumeration;
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
import com.hnjk.edu.basedata.model.YearInfo;
import com.hnjk.edu.basedata.service.IYearInfoService;
import com.hnjk.platform.system.UserOperationLogsHelper;
import com.hnjk.platform.system.model.UserOperationLogs;

/**
 * 年度设置
 * <code>YearInfoController</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2010-4-7 下午01:51:15
 * @see 
 * @version 1.0
 */
@Controller
public class YearInfoController extends BaseSupportController{

	private static final long serialVersionUID = -8533320347864606741L;
	
	@Autowired
	@Qualifier("yearInfoService")
	private IYearInfoService yearInfoService;
	
	
	/**
	 * 查询分页列表
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/sysmanager/yearinfo/list.html")
	public String exeList(String yearName,Page objPage,ModelMap model) throws WebException{
		objPage.setOrderBy("firstYear");
		objPage.setOrder(Page.DESC);//设置默认排序方式
		
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件
		if(ExStringUtils.isNotEmpty(yearName)) {
			condition.put("yearName", yearName);
		}

		Page page = yearInfoService.findYearInfoByCondition(condition, objPage);
		
		model.addAttribute("condition", condition);
		model.addAttribute("yearInfoList", page);		
		return "/edu3/basedata/yearinfo/yearinfo-list";
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
	@RequestMapping("/edu3/sysmanager/yearinfo/edit.html")
	public String exeEdit(String resourceid,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		if(ExStringUtils.isNotBlank(resourceid)){ //-----编辑
			YearInfo yearInfo = yearInfoService.load(resourceid);	
			model.addAttribute("yearInfo", yearInfo);
		}else{ //----------------------------------------新增
			model.addAttribute("yearInfo", new YearInfo());			
		}
		return "/edu3/basedata/yearinfo/yearinfo-form";
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
	@RequestMapping("/edu3/sysmanager/yearinfo/save.html")
	public void exeSave(YearInfo yearInfo,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
			
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			YearInfo persistYearInfo = null;
			yearInfo.setYearName(ExStringUtils.trimToEmpty(yearInfo.getYearName()));
			if(ExStringUtils.isNotBlank(yearInfo.getResourceid())){ //--------------------更新
				persistYearInfo = yearInfoService.load(yearInfo.getResourceid());
				ExBeanUtils.copyProperties(persistYearInfo, yearInfo);
				//yearInfoService.update(persistYearInfo);
			}else{ //-------------------------------------------------------------------保存
				//yearInfoService.save(yearInfo);
				persistYearInfo = new YearInfo();
				ExBeanUtils.copyProperties(persistYearInfo, yearInfo);
			}		
			yearInfoService.saveOrUpdate(persistYearInfo);
			UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "7", UserOperationLogs.INSERT,"更新年度：参数："+JsonUtils.mapToJson(getConditionFromResquestByIterator(request)));
			map.put("statusCode", 200);
			map.put("message", "保存成功！");
			map.put("navTabId", "RES_BASEDATA_MANAGER_YEARINFO");
			map.put("reloadUrl", request.getContextPath() +"/edu3/sysmanager/yearinfo/edit.html?resourceid="+persistYearInfo.getResourceid());				
						
			
		}catch (Exception e) {
			logger.error("保存出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败：<br/>年度名称或者起始年份重复！");
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
	@RequestMapping("/edu3/sysmanager/yearinfo/delete.html")
	public void exeDelete(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		String resourceid = request.getParameter("resourceid");
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(resourceid)){
				if(resourceid.split("\\,").length >1){//批量删除				
					//逻辑删除--软删除
//					yearInfoService.batchCascadeDelete(resourceid.split("\\,"));
					//物理删除--硬删除
					yearInfoService.batchTruncate(YearInfo.class,resourceid.split("\\,"));
				}else{//单个删除
					//逻辑删除--软删除
//					yearInfoService.delete(resourceid);
				    //物理删除--硬删除
					yearInfoService.truncateByProperty(YearInfo.class,"resourceid",resourceid);
				}
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "7", UserOperationLogs.DELETE,"更新年度：参数："+JsonUtils.mapToJson(getConditionFromResquestByIterator(request)));
				map.put("statusCode", 200);
				map.put("message", "删除成功！");				
				map.put("forward", request.getContextPath()+"/edu3/sysmanager/yearinfo/list.html");
			}
		} catch (Exception e) {
			logger.error("删除栏目出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错！该年度正在使用中！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	public static Map<String, Object> getConditionFromResquestByIterator(HttpServletRequest request) {
		Map<String, Object> condition = new HashMap<String, Object>();
		Enumeration em = request.getParameterNames();
		while (em.hasMoreElements()) {
			String name = (String) em.nextElement();
			Object value = request.getParameter(name);
			if(value!=null && !"undefined".equals(value) && !"".equals(value)){
				condition.put(name, value);
			}
		}
		//当 conditon 中包含 pageNum 和 pageSize 时，点击翻页时，request中将有多个pageNum和pageSize，因此，condition中移除多余的参数
		if(condition.containsKey("pageNum")){
			condition.remove("pageNum");
		}
		if(condition.containsKey("pageSize")){
			condition.remove("pageSize");
		}
		return condition;
	}
}
