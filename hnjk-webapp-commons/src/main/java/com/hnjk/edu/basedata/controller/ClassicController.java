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
import com.hnjk.edu.basedata.model.Classic;
import com.hnjk.edu.basedata.service.IClassicService;
import com.hnjk.platform.system.UserOperationLogsHelper;
import com.hnjk.platform.system.model.UserOperationLogs;

/**
 * 层次设置
 * <code>ClassicController</code><p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-4-7 下午02:27:36
 * @see 
 * @version 1.0
 */
@Controller
public class ClassicController extends BaseSupportController {

	private static final long serialVersionUID = -5825799915005610821L;
	
	@Autowired
	@Qualifier("classicService")
	private IClassicService classicService;
	
	
	/**
	 * 查询分页列表
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/sysmanager/classic/list.html")
	public String exeList(String classicName,Page objPage,ModelMap model) throws WebException{
		objPage.setOrderBy("classicCode");
		objPage.setOrder(Page.ASC);//设置默认排序方式
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件
		
		if(ExStringUtils.isNotEmpty(classicName)) {
			condition.put("classicName", classicName);
		}

		Page page = classicService.findClassicByCondition(condition, objPage);
		
		model.addAttribute("condition", condition);
		model.addAttribute("classicList", page);
		return "/edu3/basedata/classic/classic-list";
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
	@RequestMapping("/edu3/sysmanager/classic/edit.html")
	public String exeEdit(String resourceid,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		if(ExStringUtils.isNotBlank(resourceid)){ //-----编辑
			Classic classic = classicService.load(resourceid);	
			model.addAttribute("classic", classic);
		}else{ //----------------------------------------新增
			model.addAttribute("classic", new Classic());			
		}
		return "/edu3/basedata/classic/classic-form";
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
	@RequestMapping("/edu3/sysmanager/classic/save.html")
	public void exeSave(Classic classic,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(classic.getResourceid())){ //--------------------更新
				Classic persistClassic = classicService.load(classic.getResourceid());
				ExBeanUtils.copyProperties(persistClassic, classic);
				classicService.update(persistClassic);
			}else{ //-------------------------------------------------------------------保存
				classicService.save(classic);
			}
			UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "7", UserOperationLogs.UPDATE,"更新层次：参数："+JsonUtils.mapToJson(getConditionFromResquestByIterator(request)));
			map.put("statusCode", 200);
			map.put("message", "保存成功！");
			map.put("navTabId", "RES_BASEDATA_MANAGER_CLASSIC");
			map.put("reloadUrl", request.getContextPath() +"/edu3/sysmanager/classic/edit.html?resourceid="+classic.getResourceid());
		}catch (Exception e) {
			logger.error("保存出错：{}",e.fillInStackTrace());
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
	@RequestMapping("/edu3/sysmanager/classic/delete.html")
	public void exeDelete(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		String resourceid = request.getParameter("resourceid");
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(resourceid)){
				if(resourceid.split("\\,").length >1){//批量删除					
					classicService.batchCascadeDelete(resourceid.split("\\,"));
				}else{//单个删除
					classicService.delete(resourceid);
				}
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "7", UserOperationLogs.DELETE,"删除层次：参数："+JsonUtils.mapToJson(getConditionFromResquestByIterator(request)));
				map.put("statusCode", 200);
				map.put("message", "删除成功！");				
				map.put("forward", request.getContextPath()+"/edu3/sysmanager/classic/list.html");
			}
		} catch (Exception e) {
			logger.error("删除出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错！");
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
