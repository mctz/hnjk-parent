package com.hnjk.edu.finance.controller;

import java.io.File;
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
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.utils.ExCollectionUtils;
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.GUIDUtils;
import com.hnjk.core.foundation.utils.HttpHeaderUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.FileUploadAndDownloadSupportController;
import com.hnjk.core.support.context.Constants;
import com.hnjk.core.support.context.SystemContextHolder;
import com.hnjk.edu.basedata.model.YearInfo;
import com.hnjk.edu.basedata.service.IYearInfoService;
import com.hnjk.edu.finance.model.Refundback;
import com.hnjk.edu.finance.service.IRefundbackService;
import com.hnjk.edu.finance.vo.RefundbackVO;
import com.hnjk.edu.util.Condition2SQLHelper;
import com.hnjk.extend.plugin.excel.service.IExportExcelService;
import com.hnjk.platform.system.UserOperationLogsHelper;
import com.hnjk.platform.system.model.UserOperationLogs;
import com.hnjk.platform.system.service.IDictionaryService;

/** 
 * @author Zik, 广东学苑教育发展有限公司
 * @since 2018年7月31日 下午4:43:31 
 * 
 */
@Controller
public class RefundbackController extends FileUploadAndDownloadSupportController {

	private static final long serialVersionUID = -3703182581717657155L;
	
	@Autowired
	@Qualifier("refundbackService")
	private IRefundbackService refundbackService;
	
	@Autowired
	@Qualifier("exportExcelService")
	private IExportExcelService exportExcelService;
	
	@Autowired
	@Qualifier("dictService")
	private IDictionaryService dictionaryService;
	
	@Autowired
	@Qualifier("yearInfoService")
	private IYearInfoService yearInfoService; 
	
	
	/**
	 * 查询分页列表
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/finance/refundback/list.html")
	public String refundbackList(HttpServletRequest request, Page objPage, ModelMap model) throws WebException{
		objPage.setOrderBy("createDate");
		objPage.setOrder(Page.DESC);//设置默认排序方式
		
		Map<String,Object> condition = Condition2SQLHelper.getConditionFromResquestByIterator(request);
		
		Page page = refundbackService.findByCondition(condition, objPage);
		
		// 层次
		model.addAttribute("refundbackList", page);
		model.addAttribute("condition", condition);
		
		return "/edu3/finance/refundback/refundback-list";
	}
	
	/**
	 * 设置为已处理或未处理
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/finance/refundback/handle.html")
	public void handleRefundback(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		String resourceids = request.getParameter("resourceid");
		String type = request.getParameter("type");
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			Map<String, Object> resultMap = refundbackService.handleOrRollbackRefundback(type,resourceids);
			int statusCode = (Integer)resultMap.get("statusCode");
			String message =  (String)resultMap.get("message");
			if(statusCode == 200 ){
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "6", UserOperationLogs.UPDATE,"处理或回退退费补交订单：参数："+JsonUtils.mapToJson(getConditionFromResquestByIterator(request)));
				map.put("statusCode", 200);
				map.put("message", "设置成功！");				
				map.put("forward", request.getContextPath()+"/edu3/finance/refundback/list.html");
			} else {
				map.put("statusCode", statusCode);
				map.put("message", message);		
			}
		} catch (Exception e) {
			logger.error("设置为已处理或未处理出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "设置出错！");
		}
		
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	
	/**
	 * 删除未处理订单
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/finance/refundback/delete.html")
	public void exeDelete(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		String resourceid = request.getParameter("resourceid");
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(resourceid)){
				if(resourceid.split("\\,").length > 0){//批量删除					
					refundbackService.batchCascadeDelete(resourceid);
				}
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "6", UserOperationLogs.DELETE,"删除预退费补交订单：参数："+JsonUtils.mapToJson(getConditionFromResquestByIterator(request)));
				map.put("statusCode", 200);
				map.put("message", "删除成功！");				
				map.put("forward", request.getContextPath()+"/edu3/finance/refundback/list.html");
			}
		} catch (Exception e) {
			logger.error("删除预退费补交订单出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错！");
		}
		
		renderJson(response, JsonUtils.mapToJson(map));
	}

	/**
	 * 导出预退费补交记录或退费汇总表
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/finance/refundback/export.html")
	public void exportRefundback(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		Map<String,Object> condition = Condition2SQLHelper.getConditionFromResquestByIterator(request);
		try {
			// 处理参数
			String operateType = request.getParameter("operateType");
			String refundbackIds = (String)condition.get("refundbackIds");
			String yearId = (String)condition.get("yearId");
			String beginDate = (String)condition.get("beginDate");
			String endDate = (String)condition.get("endDate");
			
			if(ExStringUtils.isNotBlank(refundbackIds)){
				condition.clear();
				condition.put("refundbackIds", CollectionUtils.arrayToList(refundbackIds.split(",")));
			}
			
			setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
	 	    GUIDUtils.init();
	 	    //导出
			File excelFile = null;
			String header = "";
			String tmpId = null;
			File disFile   = new File(getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(false) + ".xls");
			// 模块路径
			StringBuffer path = new StringBuffer(200);
			path.append(SystemContextHolder.getAppRootPath()).append("WEB-INF").append(File.separator)
			.append("templates").append(File.separator).append("excel").append(File.separator).append("refundback")
			.append(File.separator);
			
			// 数据字典
			List<String> dictCodeList = new ArrayList<String>();
			dictCodeList.add("CodeStudentStatusChange");
			dictCodeList.add("CodeChargingItems");
			dictCodeList.add("CodeProcessType");
			dictCodeList.add("CodeProcessStatus");
			Map<String, Object> dicMap = dictionaryService.getDictionByMap(dictCodeList, true,IDictionaryService.PREKEY_TYPE_BYCODE);
			
			if("refundSummary".equals(operateType)){
				// 由于退费汇总表只涉及退费并且是学费，所以要进入条件过滤
				condition.put("processType", "returnPremium");
				condition.put("chargingItems", "tuition");
				// 确定标题名称
				if(ExStringUtils.isNotBlank(beginDate) && ExStringUtils.isNotBlank(endDate)){
					header +=ExDateUtils.formatDateStr(ExDateUtils.convertToDateTime(beginDate), 4);
					header +=" - "+ExDateUtils.formatDateStr(ExDateUtils.convertToDateTime(endDate), 4);
				}else if(ExStringUtils.isNotBlank(beginDate)){
					header +=ExDateUtils.formatDateStr(ExDateUtils.convertToDateTime(beginDate), 4)+"之后";
				}else if(ExStringUtils.isNotBlank(beginDate)){
					header +=ExDateUtils.formatDateStr(ExDateUtils.convertToDateTime(endDate), 4)+"之前";
				}else {
					YearInfo year = yearInfoService.get(yearId);
					header += year.getFirstYear()+"年";
				}
				// 导出退费汇总表
				header += "成教学生退费汇总表";
				path.append("refundSummaryExport.xls");
				 tmpId="exportRefundSummary";
			}else {
				header = "预退费补交明细表";
				path.append("refundbackInfoExport.xls");
				 tmpId="exportRefundbackInfo";
			}
			// 查询信息
			List<RefundbackVO> refundbackVOList = refundbackService.findRefundbackInfoByCondition(condition);
			if(ExCollectionUtils.isEmpty(refundbackVOList)){
				renderHtml(response, "<script>alert('对不起，没有符合条件的数据导出')</script>");
			}
			exportExcelService.initParamsByfile(disFile, tmpId, refundbackVOList, dicMap,null,null);
			
			// excel模板参数
			Map<String,Object> tempMap = new HashMap<String, Object>(5);
			tempMap.put("title", header);
			
			//初始化配置参数
			exportExcelService.getModelToExcel().setRowHeight(300);//设置行高
			exportExcelService.getModelToExcel().setHeader(header);
			exportExcelService.getModelToExcel().setTemplateParam(path.toString(), 2, tempMap);
			
			excelFile = exportExcelService.getExcelFile();//获取导出的文件
			logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());
			String downloadFilePath = excelFile.getAbsolutePath();
			downloadFile(response, header+".xls",downloadFilePath,true);	
	 	 			
		} catch (Exception e) {
			logger.error("导出预退费补交记录或退费汇总表出错", e);
			renderHtml(response, "<script>alert('导出excel文件出错,请联系管理员')</script>");
		} 
	}
	
	/**
	 * 编辑
	 * @param resourceid
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/finance/refundback/edit.html")
	public String editRefundback(String resourceid,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		Refundback refundback = null;
		if(ExStringUtils.isNotBlank(resourceid)){ //-----编辑
			refundback = refundbackService.load(resourceid);	
		}else{ //----------------------------------------新增
			refundback = new Refundback();
		}
		model.addAttribute("refundback", refundback);			
				
		return "/edu3/finance/refundback/refundback-form";
	}
	
	/**
	 * 保存
	 * @param grade
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/finance/refundback/save.html")
	public void saveRefundback(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		int statusCode = 200;
		String message = "保存成功！";
		try {
			String resourceid = request.getParameter("resourceid");
			String money = request.getParameter("money");
			Double payMoney = Double.valueOf(money);
			
			if(ExStringUtils.isNotBlank(resourceid)){ 
				Map<String,Object> returnMap = refundbackService.editRefundback(resourceid, payMoney);
				statusCode = (Integer)returnMap.get("statusCode");
				if(statusCode!=200){
					message = (String)returnMap.get("message");
				}
			}
			UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "7", UserOperationLogs.UPDATE,"编辑退补订单：参数："+JsonUtils.mapToJson(getConditionFromResquestByIterator(request)));
			map.put("navTabId", "RES_FINANCE_REFUNDBACK");
			map.put("reloadUrl", request.getContextPath() +"/edu3/finance/refundback/edit.html?resourceid="+resourceid);
		}catch (Exception e) {
			logger.error("保存退补订单信息出错：{}",e.fillInStackTrace());
			statusCode = 300;
			message =  "保存失败";
		} finally {
			map.put("statusCode", statusCode);
			map.put("message", message);
		}
		
		renderJson(response, JsonUtils.mapToJson(map));
	}
}
