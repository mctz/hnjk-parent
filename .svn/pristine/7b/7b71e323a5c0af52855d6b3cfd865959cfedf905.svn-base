package com.hnjk.platform.system.controller;

import java.io.IOException;
import java.io.PrintWriter;
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
import com.hnjk.core.foundation.utils.HttpHeaderUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.BaseSupportController;
import com.hnjk.edu.util.Condition2SQLHelper;
import com.hnjk.platform.system.UserOperationLogsHelper;
import com.hnjk.platform.system.model.Dictionary;
import com.hnjk.platform.system.model.UserOperationLogs;
import com.hnjk.platform.system.service.IDictionaryService;

/**
 * 
 * <code>DictionaryController</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： Mar 10, 2010 3:35:01 PM
 * @see 
 * @version 1.0
 */
@Controller
public class DictionaryController extends BaseSupportController{
	

	private static final long serialVersionUID = -6848978503057358761L;
	
	@Autowired
	@Qualifier("dictService")
	private IDictionaryService dictionaryService;
	
	/**
	 * 返回字典列表
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/edu3/system/dictionary/list.html")
	public String exeList(String module,String dictCode,String dictName,Page objPage,ModelMap model) throws WebException{
		//查询字典列表
		objPage.setOrderBy("resourceid");
		objPage.setOrder("asc");
		
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件
		if(ExStringUtils.isNotEmpty(module)) {
			condition.put("module", module);
		}
		if(ExStringUtils.isNotEmpty(dictCode)) {
			condition.put("dictCode", dictCode);
		}
		if(ExStringUtils.isNotEmpty(dictName)) {
			condition.put("dictName", dictName);
		}
		Page page = dictionaryService.findDictionaryByCondition(condition, objPage);
		
		model.addAttribute("dictList", page);
		model.addAttribute("condition", condition);
		return "/system/dict/dictionary-list";
	}
	
	/**
	 * 新增字典
	 * @param resourceid
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping(value={"/edu3/system/dictionary/edit.html"})
	public String exeEdit(String resourceid,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		if(ExStringUtils.isNotBlank(resourceid)){
			Dictionary dict = dictionaryService.get(resourceid);		
			model.addAttribute("dict", dict);
		}else{
			model.addAttribute("dict", new Dictionary());			
		}
		return "/system/dict/dictionary-form";
	}
	
	/**
	 * 保存
	 * @param resourceid
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/system/dictionary/save.html")
	public void exeSave(String resourceid,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		Dictionary dictionary;
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			String dictCode  = request.getParameter("dictCode"); //字典编码
			String module  = request.getParameter("module"); //所属模块
			String dictName  = request.getParameter("dictName"); //字典名
					
			if(ExStringUtils.isNotBlank(resourceid)){//编辑
				dictionary = dictionaryService.get(resourceid);
				dictionary.setDictCode(dictCode);
				dictionary.setModule(module);
				dictionary.setDictName(dictName);

				dictionaryService.updateDict(dictionary,convertDictChilds(request,dictCode));
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "8", UserOperationLogs.UPDATE,"修改字典：参数："+JsonUtils.mapToJson(Condition2SQLHelper.getConditionFromResquestByIterator(request)));
			}else{
				dictionary = new Dictionary();
				dictionary.setDictCode(dictCode);
				dictionary.setModule(module);
				dictionary.setDictName(dictName);			

				List<Dictionary> childs = convertDictChilds(request,dictCode);
				if(childs.size()>0){
					for (Dictionary child : childs) {
						dictionary.getDictSets().add(child);
						child.setParentDict(dictionary);
					}
					
				}
				
				dictionaryService.saveDict(dictionary);	
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "8", UserOperationLogs.INSERT,"新增字典：参数："+JsonUtils.mapToJson(Condition2SQLHelper.getConditionFromResquestByIterator(request)));
			}
			map.put("statusCode", 200);
			map.put("message", "保存成功！");
			map.put("navTabId", "RES_SYS_DICTIONARY");
			map.put("reloadUrl", request.getContextPath() +"/edu3/system/dictionary/edit.html?resourceid="+dictionary.getResourceid());
		}catch (Exception e) {
			logger.error("保存出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败:<br/>"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	private List<Dictionary> convertDictChilds(HttpServletRequest request,String dictCode){
		
		String[] dictIds = request.getParameterValues("dictChildId"); //子id
		String[] dictNames = request.getParameterValues("dictChildName");//选项名
		String[] dictValues = request.getParameterValues("dictChildValue");//选项值
		String[] isUseds = request.getParameterValues("isUsedChild");//是否可用
		String[] isDefaults = request.getParameterValues("isDefault");//默认值
		String[] showOrders = request.getParameterValues("showOrder");//排序
		
		List<Dictionary> list = new ArrayList<Dictionary>();
		//Dictionary dictionary = new Dictionary();
		Dictionary childDict = null;
		if(dictIds != null && dictIds.length > 0){			
			for(int index=0;index<dictIds.length;index++){	
			
				//if(dictIds[index].equals("")){ 
					childDict = new Dictionary(); 
				//}else{ 
				//	childDict = dictionaryService.get(dictIds[index]); 
				//}	
				childDict.setResourceid(dictIds[index]);
				childDict.setDictName(dictNames[index]);
				childDict.setDictCode(dictCode+"_"+index);
				childDict.setDictValue(dictValues[index]);
				childDict.setIsUsed(isUseds[index]);
				childDict.setIsDefault(isDefaults[index]);
				childDict.setShowOrder("".equals(showOrders[index]) ?0:Integer.parseInt(showOrders[index]));
				//dictionary.getDictSets().add(childDict);
				//childDict.setParentDict(dictionary);
				list.add(childDict);
			}
		}
		
		return list;
	}
	
	/**
	 * 删除
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/system/dictionary/delete.html")
	public void exeDelete(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		String resourceid = request.getParameter("resourceid");
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(resourceid)){
				dictionaryService.batchCascadeDelete(resourceid.split("\\,"));
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "8", UserOperationLogs.DELETE,"删除字典：参数："+JsonUtils.mapToJson(Condition2SQLHelper.getConditionFromResquestByIterator(request)));
				map.put("statusCode", 200);
				map.put("message", "删除成功！");				
				map.put("forward", request.getContextPath()+"/edu3/system/dictionary/list.html");
			}
			
		} catch (Exception e) {
			logger.error("删除栏目出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错:<br/>"+e.getLocalizedMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 检查编码的唯一性
	 * @param request
	 * @param response
	 * @param model
	 * @throws WebException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/edu3/framework/system/sys/dictionary/validateCode.html")
	public void validateCode(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		String dictCode = request.getParameter("dictCode");
		String resourceid = request.getParameter("resourceid");
		String msg = "";
		if(ExStringUtils.isNotBlank(dictCode)){
			String hql = "from Dictionary d where d.dictCode=?";
			List list = dictionaryService.findByHql(hql, dictCode.trim());
			if(ExStringUtils.isEmpty(resourceid) && !list.isEmpty()){					
				msg = "exist";
			}else if(ExStringUtils.isNotEmpty(resourceid)){
				Dictionary dict = dictionaryService.load(resourceid);
				if(!list.isEmpty() && !dict.getDictCode().equals(dictCode.trim())){
					msg = "exist";
				}
			}
		}			
		try {
			PrintWriter writer = response.getWriter();
			writer.append(msg);
			writer.close();
		} catch (IOException e) {
		   logger.error("校验字典编码出错：{}",e.fillInStackTrace());
		}
	}
	
}
