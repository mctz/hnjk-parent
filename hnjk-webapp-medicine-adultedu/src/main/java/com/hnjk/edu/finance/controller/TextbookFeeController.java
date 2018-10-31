package com.hnjk.edu.finance.controller;

import java.io.File;
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
import com.hnjk.core.foundation.utils.ExBeanUtils;
import com.hnjk.core.foundation.utils.ExCollectionUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.GUIDUtils;
import com.hnjk.core.foundation.utils.HttpHeaderUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.FileUploadAndDownloadSupportController;
import com.hnjk.core.support.context.Constants;
import com.hnjk.core.support.context.SystemContextHolder;
import com.hnjk.edu.basedata.model.Classic;
import com.hnjk.edu.basedata.service.IClassicService;
import com.hnjk.edu.basedata.service.IMajorService;
import com.hnjk.edu.basedata.service.IYearInfoService;
import com.hnjk.edu.finance.model.TextbookFee;
import com.hnjk.edu.finance.service.ITextbookFeeService;
import com.hnjk.edu.finance.vo.TextbookFeeVo;
import com.hnjk.extend.plugin.excel.service.IExportExcelService;
import com.hnjk.extend.plugin.excel.service.IImportExcelService;
import com.hnjk.platform.system.UserOperationLogsHelper;
import com.hnjk.platform.system.model.Attachs;
import com.hnjk.platform.system.model.UserOperationLogs;
import com.hnjk.platform.system.service.IAttachsService;

/** 
 * @author Zik, 广东学苑教育发展有限公司
 * @since 2018年7月31日 下午4:40:13 
 * 
 */
@Controller
public class TextbookFeeController extends FileUploadAndDownloadSupportController {

	private static final long serialVersionUID = -4910117602314822044L;
	
	@Autowired
	@Qualifier("textbookFeeService")
	private ITextbookFeeService textbookFeeService;
	
	@Autowired
	@Qualifier("yearInfoService")
	private IYearInfoService yearInfoService;
	
	@Autowired
	@Qualifier("majorService")
	private IMajorService majorService;
	
	@Autowired
	@Qualifier("classicService")
	private IClassicService classicService;
	
	@Autowired
	@Qualifier("attachsService")
	private IAttachsService attachsService;//注入附件服务

	@Autowired
	@Qualifier("exportExcelService")
	private IExportExcelService exportExcelService;
	
	@Autowired
	@Qualifier("importExcelService")
	private IImportExcelService importExcelService;
	
	/**
	 * 查询分页列表
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/finance/textbookFee/list.html")
	public String textbookFeeList(HttpServletRequest request, Page objPage, ModelMap model) throws WebException{
		objPage.setOrderBy("yearInfo.firstYear desc,major.majorCode");
		objPage.setOrder(Page.ASC);//设置默认排序方式
		
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件
		String yearId  = request.getParameter("yearId");
		String majorId  = request.getParameter("majorId");
		
		if(ExStringUtils.isNotEmpty(yearId)) {
			condition.put("yearId", yearId);
		}
		
		if(ExStringUtils.isNotEmpty(majorId)) {
			condition.put("majorId", majorId);
		}
		
		Page page = textbookFeeService.findByCondition(condition, objPage);
		
		// 层次
		model.addAttribute("classicMap", getClassicMap());
		model.addAttribute("textbookFeeList", page);
		model.addAttribute("condition", condition);
		return "/edu3/finance/textbookFee/textbookFee-list";
	}
	
	/**
	 * 获取层次信息
	 * @return
	 */
	private Map<String, String> getClassicMap(){
		Map<String, String> classicMap = new HashMap<String, String>(10);
		StringBuffer hql = new StringBuffer(200);
		hql.append("from ").append(Classic.class.getSimpleName()).append(" where isDeleted=0 ");
		List<Classic> classicList = classicService.findByHql(hql.toString());
		if(ExCollectionUtils.isNotEmpty(classicList)){
			for(Classic c : classicList){
				classicMap.put(c.getClassicCode(), c.getClassicName());
			}
		}
		return classicMap;
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
	@RequestMapping("/edu3/finance/textbookFee/edit.html")
	public String editTextbookFee(String resourceid,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		TextbookFee textbookFee = null;
		if(ExStringUtils.isNotBlank(resourceid)){ //-----编辑
			textbookFee = textbookFeeService.load(resourceid);	
		}else{ //----------------------------------------新增
			textbookFee = new TextbookFee();
		}
		model.addAttribute("textbookFee", textbookFee);	
		
		return "/edu3/finance/textbookFee/textbookFee-form";
	}
	
	/**
	 * 保存更新表单
	 * @param textbookFee
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/finance/textbookFee/save.html")
	public void saveTextbookFee(TextbookFee textbookFee,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		int statusCode = 200;
		String message = "保存成功！";
		
		try {
			do {
				String yearId = textbookFee.getYearId();
				String majorId = textbookFee.getMajorId();
				if (ExStringUtils.isBlank(yearId) || ExStringUtils.isBlank(majorId)) {
					statusCode = 300;
					message = "年度和专业不能为空！";
					continue;
				}
				
				TextbookFee persistTextbookFee = null;
				String pYearId = null;
				String pMajorId = null;
				if (ExStringUtils.isNotBlank(textbookFee.getResourceid())) { //--------------------更新
					persistTextbookFee = textbookFeeService.get(textbookFee.getResourceid());
					textbookFee.setYearInfo(persistTextbookFee.getYearInfo());
					textbookFee.setMajor(persistTextbookFee.getMajor());
					pYearId = persistTextbookFee.getYearInfo().getResourceid();
					pMajorId = persistTextbookFee.getMajor().getResourceid();
				} else { //-------------------------------------------------------------------保存
					persistTextbookFee = new TextbookFee();
				}
				// 判断同年级同专业是否已存在记录
				if(!(yearId.equals(pYearId) && majorId.equals(pMajorId))){
					TextbookFee hasTextbookFee = textbookFeeService.findByYearAndMajor(yearId, majorId);
					if(hasTextbookFee != null ){
						statusCode = 300;
						message = "该年度和专业已存在一条教材费标准记录！";
						continue;
					}
					textbookFee.setYearInfo(yearInfoService.get(yearId));
					textbookFee.setMajor(majorService.get(majorId));
				}
				
				ExBeanUtils.copyProperties(persistTextbookFee, textbookFee);
				textbookFeeService.saveOrUpdate(persistTextbookFee);
				
				map.put("navTabId", "RES_FINANCE_TEXTBOOKFEE");
				map.put("reloadUrl", request.getContextPath() +"/edu3/finance/textbookFee/edit.html?resourceid="+persistTextbookFee.getResourceid());
				
			} while (false);
			
		}catch (Exception e) {
			logger.error("保存教材费出错：{}",e.fillInStackTrace());
			statusCode = 300;
			message ="保存失败！";
		} finally {
			map.put("statusCode", statusCode);
			map.put("message", message);
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
	@RequestMapping("/edu3/finance/textbookFee/delete.html")
	public void exeDelete(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		String resourceid = request.getParameter("resourceid");
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(resourceid)){
				if(resourceid.split("\\,").length >1){//批量删除					
					textbookFeeService.batchCascadeDelete(resourceid.split("\\,"));
				}else{//单个删除
					textbookFeeService.delete(resourceid);
				}
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "7", UserOperationLogs.DELETE,"删除教材费标准：参数："+JsonUtils.mapToJson(getConditionFromResquestByIterator(request)));
				map.put("statusCode", 200);
				map.put("message", "删除成功！");				
				map.put("forward", request.getContextPath()+"/edu3/finance/textbookFee/list.html");
			}
		} catch (Exception e) {
			logger.error("删除教材费出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错！");
		}
		
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 下载导入年教材费标准模板
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/finance/textbookFee/downloadTemplate.html")
	public void downloadTextbookFeeTemp(HttpServletRequest request, HttpServletResponse response){
		try{
			//模板文件路径
			StringBuffer templateFilepathString = new StringBuffer(150);
			templateFilepathString.append("textbookFee").append(File.separator).append("textbookFeeImport.xls");
			
			downloadFile(response, "年教材费标准模板.xls", templateFilepathString.toString(),false);
		}catch(Exception e){
			logger.error("下载导入年教材费标准模板出错：",e);
			renderHtml(response, "<script type=\"text/javascript\">parent.alertMsg.warn(\"下载导入年教材费标准模板失败\");</script>");
		}
	}
	
	/**
	 * 进入导入年教材费标准页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/edu3/finance/textbookFee/importTextbookFee-view.html")
	public String uploadTextbookFeeForm(HttpServletRequest request,HttpServletResponse response, ModelMap model){		
		return "/edu3/finance/textbookFee/importTextbookFee-view";
	}
	
	/**
	 * 导入年教材费标准逻辑处理
	 * 
	 * @param attachId
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/edu3/finance/textbookFee/importTextbookFee.html")
	public void importTextbookFee(String attachId, HttpServletRequest request,HttpServletResponse response) throws WebException{				
		Map<String ,Object> map = new HashMap<String, Object>();
		int statusCode = 200;
		String message = "导入年教材费标准成功！";
		int totalNum =0 ;
		int failNum = 0;
		try {	
			do{
				String yearId = request.getParameter("yearId");
				if(ExStringUtils.isBlank(yearId)){
					statusCode = 300;				
					message = "年度不能为空";
					continue;
				}
				List<TextbookFeeVo> failList = null;
				if( ExStringUtils.isBlank(attachId)){		
					statusCode = 300;				
					message = "请上传附件.";
					continue;
				}
					
				Attachs attachs =  attachsService.get(attachId);//获取上传的文件，因为是单个文件，所以get(0)就可
				File excel = new File(attachs.getSerPath()+File.separator+attachs.getSerName());
				importExcelService.initParmas(excel, "importTextbookFee", null);
				importExcelService.getExcelToModel().setSheet(0);//设置excel sheet 0 
				importExcelService.getExcelToModel().setStartTitleRow(3);
				List<TextbookFeeVo> modelList = importExcelService.getModelList();
				if(ExCollectionUtils.isEmpty(modelList)){
					statusCode = 300;				
					message = "没有数据";
					continue;
				}
				Map<String, Object> returnMap = textbookFeeService.handleTextbookFee(modelList, yearId);
				totalNum = modelList.size();
				if(returnMap!=null &&returnMap.size()>0 ){
					statusCode = (Integer)returnMap.get("statusCode");
					if(statusCode!=200) {
						message = (String)returnMap.get("message");
						if(statusCode==400){
							failList = (List<TextbookFeeVo>)returnMap.get("failList");
						}
					}
				}
				
				//导出年教材费标准的失败记录
				if(ExCollectionUtils.isNotEmpty(failList)){
					failNum = failList.size();
					setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
					//导出
					GUIDUtils.init();
					String fileName = GUIDUtils.buildMd5GUID(false);
					File disFile = new File(getDistfilepath()+ File.separator + fileName + ".xls");
						
					//模板文件路径
					StringBuffer templateFilepathString = new StringBuffer(150);
					templateFilepathString.append(SystemContextHolder.getAppRootPath())
					.append("WEB-INF").append(File.separator).append("templates").append(File.separator).append("excel")
					.append(File.separator).append("textbookFee").append(File.separator).append("textbookFeeImportError.xls");
					//初始化配置参数
					exportExcelService.initParmasByfile(disFile,"importTextbookFeeError", failList,null);
					exportExcelService.getModelToExcel().setTemplateParam(templateFilepathString.toString(), 3, null);
					exportExcelService.getModelToExcel().setRowHeight(400);
					File excelFile = exportExcelService.getExcelFile();//获取导出的文件(将导出文件写到硬盘对应的目录中)
					logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());	
					String upLoadurl = "/edu3/finance/textbookFee/exportTextbookFeeError.html?excelFile="+fileName;
					map.put("exportErrorTextbookFee", upLoadurl);
				}
			} while(false);
		} catch (Exception e) {
			logger.error("导入年教材费标准出错:{}",e);
			statusCode = 300;
			message = "导入年教材费标准失败！";
		} finally {
			map.put("statusCode", statusCode);
			map.put("message", message);
			map.put("resultMsg", "导入成功："+(totalNum-failNum)+"条，失败："+failNum+"条");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 导出年教材费标准失败信息
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/finance/textbookFee/exportTextbookFeeError.html")
	public void exportTextbookFeeError(String excelFile,HttpServletRequest request,HttpServletResponse response) throws Exception {
		setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
		File disFile = new File(getDistfilepath()+ File.separator + excelFile+".xls");
		downloadFile(response, "导入年教材费标准失败记录.xls", disFile.getAbsolutePath(),true);
	}	

}
