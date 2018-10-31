package com.hnjk.platform.system.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.utils.AesUtils;
import com.hnjk.core.foundation.utils.Base64;
import com.hnjk.core.foundation.utils.ExBeanUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.HttpUrlConnectionUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.foundation.utils.SensitivewordFilter;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.BaseSupportController;
import com.hnjk.platform.system.model.Attachs;
import com.hnjk.platform.system.model.SensitiveWord;
import com.hnjk.platform.system.service.IAttachsService;
import com.hnjk.platform.system.service.ISensitiveWordService;

/**
 * 敏感词Controller
 * @author Zik
 *
 */
@Controller
public class SensitiveWordController extends BaseSupportController {

	private static final long serialVersionUID = -154358904589757733L;
	
	@Qualifier("sensitiveWordService")
	@Autowired
	private ISensitiveWordService sensitiveWordService;
	
	@Autowired
	@Qualifier("attachsService")
	private IAttachsService attachsService;//注入附件服务
	
	/**
	 * 获取敏感词列表
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/system/sensitiveWord/list.html")
	public String listSensitiveWord(HttpServletRequest request,Page objPage, ModelMap model) throws WebException {
	
		objPage.setOrderBy("word");
		objPage.setOrder("asc");
		
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件
		String word = request.getParameter("word");
		if(ExStringUtils.isNotEmpty(word)){
			condition.put("word", word);
		}
		
		Page page = sensitiveWordService.findByCondition(condition, objPage);
		model.addAttribute("sensitiveWordList",page);
		model.addAttribute("condition", condition);
		return "/system/sensitiveWord/sensitiveWord-list";
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
	@RequestMapping("/edu3/system/sensitiveWord/edit.html")
	public String editSensitiveWord(String resourceid,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		SensitiveWord sensitiveWord = null;
		if(ExStringUtils.isNotBlank(resourceid)){ //-----编辑
			sensitiveWord = sensitiveWordService.get(resourceid);	
		}else{ //----------------------------------------新增
			sensitiveWord = new SensitiveWord();			
		}
		model.addAttribute("sensitiveWord", sensitiveWord);
		return "/system/sensitiveWord/sensitiveWord-form";
	}
	
	/**
	 * 保存/更新敏感词
	 * @param setTime
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/system/sensitiveWord/save.html")
	public void saveSensitiveWord(SensitiveWord sensitiveWord,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		int statusCode = 200;
		String message = "保存成功！";
		Date today = new Date();
		try {
			do{
				if(ExStringUtils.isEmpty(ExStringUtils.trimToEmpty(sensitiveWord.getWord()))){
					statusCode = 300;
					message = "敏感词不能为空！";
					continue;
				} 
				sensitiveWord.setWord(ExStringUtils.trim(sensitiveWord.getWord()));
				SensitiveWord _sensitiveWord = null;
				if(ExStringUtils.isNotBlank(sensitiveWord.getResourceid())){ //  更新
					_sensitiveWord = sensitiveWordService.get(sensitiveWord.getResourceid());	
					sensitiveWord.setCreateDate(_sensitiveWord.getCreateDate());
				}else{ // 保存
					_sensitiveWord = new SensitiveWord();
					sensitiveWord.setCreateDate(today);
				}		
				if(!ExStringUtils.trimToEmpty(_sensitiveWord.getWord()).equals(sensitiveWord.getWord())){
					SensitiveWord hasSensitiveWord = sensitiveWordService.getByWord(sensitiveWord.getWord());
					if(hasSensitiveWord != null){
						statusCode = 300;
						message = "该敏感词已经存在！";
						continue;
					}
				}
				
				ExBeanUtils.copyProperties(_sensitiveWord, sensitiveWord);
				_sensitiveWord.setUpdateDate(today);
				sensitiveWordService.saveOrUpdate(_sensitiveWord);
				// 重载敏感词库
				reloadSensitiveWord(request);
				// 重载APP敏感词库
				reloadAppSensitiveWord();
				map.put("navTabId", "RES_SYS_SENSITIVEWORD_UPDATE");
				map.put("reloadUrl", request.getContextPath() +"/edu3/system/sensitiveWord/edit.html?resourceid="+_sensitiveWord.getResourceid());
			} while(false);
		}catch (Exception e) {
			logger.error("保存敏感词出错",e);
			statusCode = 300;
			message = "保存失败！";
		} finally {
			map.put("statusCode", statusCode);
			map.put("message", message);
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 批量删除敏感词
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/system/sensitiveWord/delete.html")
	public void deleteSensitiveWord(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		String resourceids = request.getParameter("resourceid");
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(resourceids)){
				if(resourceids.split(",").length >1){//批量删除					
					sensitiveWordService.batchCascadeDelete(resourceids.split(","));
				}else{//单个删除
					sensitiveWordService.delete(resourceids);
				}
				// 重载敏感词库
				reloadSensitiveWord(request);
				// 重载APP敏感词库
				reloadAppSensitiveWord();
				map.put("statusCode", 200);
				map.put("message", "删除成功！");				
				map.put("forward", request.getContextPath()+"/edu3/system/sensitiveWord/list.html");
			}
		} catch (Exception e) {
			logger.error("删除敏感词出错",e);
			map.put("statusCode", 300);
			map.put("message", "删除失败！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 转到导入敏感词页面
	 * @param request
	 * @param response
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/system/sensitiveWord/import.html")
	public String importPage(HttpServletRequest request, HttpServletResponse response) throws WebException {
		
		return "/system/sensitiveWord/importSensitiveWord";
	}
	
	/**
	 * 处理导入敏感词（word,txt或xls）
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/system/sensitiveWord/handle-sensitiveWord-import.html")
	public void importSensitiveWord(HttpServletRequest request, HttpServletResponse response) throws WebException {
		Map<String ,Object> map = new HashMap<String, Object>();
		int status = 200;
		String message = "导入敏感词成功";
		try {
			do {
				String attchID = ExStringUtils.trimToEmpty(request.getParameter("attachId"));
				if (null != attchID && attchID.split(",").length > 1) {
					status = 300;
					message = "一次只能导入一个敏感词文件！";
				} else if (null != attchID && attchID.split(",").length == 1) {
					Attachs attachs = attachsService.get(attchID.split(",")[0]);
					if (attachs == null) {
						status = 300;
						message = "该敏感词文件不存在！";
						continue;
					}
					if(!("txt".equals(attachs.getAttType()) || "xls".equals(attachs.getAttType()))){
						status = 300;
						message = "只能上传txt,xls格式的文件！";
						continue;
					}
					sensitiveWordService.importSensitiveWordByAttachs(attachs);
					// 重载敏感词库
					reloadSensitiveWord(request);
					// 重载APP敏感词库
					reloadAppSensitiveWord();
					attachsService.truncate(attachs);
				}
			} while (false);
		} catch (Exception e) {
			logger.error("处理导入敏感词出错",e);
		} finally {
			map.put("status", status);
			map.put("message", message);
		}
		
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 重载APP的敏感词库
	 */
	private void reloadAppSensitiveWord() {
		String returnMsg = "重载APP的敏感词库失败";
		try {
			Properties prop = new Properties();//属性集合对象      
			InputStream ins=SensitiveWordController.class.getClassLoader().getResourceAsStream("sys_conf.properties"); 
			BufferedReader br= new BufferedReader(new InputStreamReader(ins));  
			prop.load(br);
			
			String AppUrl = prop.getProperty("AppUrl");//APP的访问地址
			String responseResult = HttpUrlConnectionUtils.getRequest(AppUrl + "/sys/updateSensitiveWord");
			if("Y".equals(prop.getProperty("isEncrypt"))){
				responseResult = appDecrypt(responseResult);
			}
			System.out.println(responseResult);
			Map<String, Object> responseMap = JsonUtils.jsonToMap(responseResult);
			if(responseMap!=null && responseMap.size()>0){
				int status = (Integer)responseMap.get("status");
				if(status==0){
					returnMsg = "重载APP的敏感词库成功";
				}
			}
			logger.info(returnMsg);
		} catch (IOException e) {
			logger.error("重载APP的敏感词库出错", e);
		}
	}
	
	/**
	 * APP数据解密
	 * @param result
	 * @return
	 */
	private String appDecrypt(String result){
		String str;
		byte[] data = AesUtils.hexStr2Bytes(result);
		data = AesUtils.decrypt(data);
		try {
			str = new String(Base64.decode(data),"UTF-8");
		} catch (Exception e) {
			return "";
		}
		return str;
	}
	
	/**
	 * 重载敏感库缓存
	 * @param request
	 */
	private void reloadSensitiveWord(HttpServletRequest request){
		SensitivewordFilter sensitivewordFilter =sensitiveWordService.getSensitivewordFilter();
		request.getSession().getServletContext().setAttribute("sensitivewordFilter", sensitivewordFilter);
	}
	
}
