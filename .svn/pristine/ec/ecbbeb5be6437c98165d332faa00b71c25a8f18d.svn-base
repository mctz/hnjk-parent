package com.hnjk.edu.finance.controller;

import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.utils.ExBeanUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.FileUploadAndDownloadSupportController;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.YearInfo;
import com.hnjk.edu.basedata.service.IYearInfoService;
import com.hnjk.edu.finance.model.ReturnAmounts;
import com.hnjk.edu.finance.service.IReturnAmountsService;
import com.hnjk.edu.util.Condition2SQLHelper;
import com.hnjk.edu.work.model.UserTimeManage;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.Attachs;
import com.hnjk.platform.system.service.IAttachsService;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.OrgUnit;
import com.hnjk.security.model.User;
import com.hnjk.security.service.IOrgUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Function : 已返学费金额 - 控制器
 * <p>Author : msl
 * <p>Date   : 2018-08-22
 * <p>Description :
 */
@Controller
public class ReturnAmountsController extends FileUploadAndDownloadSupportController {

	@Autowired
	@Qualifier("returnAmountsService")
	private IReturnAmountsService returnAmountsService;

	@Autowired
	@Qualifier("orgUnitService")
	private IOrgUnitService orgUnitService;

	@Autowired
	@Qualifier("yearInfoService")
	private IYearInfoService yearInfoService;

	@Autowired
	@Qualifier("attachsService")
	private IAttachsService attachsService;

	/**
	 * 分页查询
	 * @param request
	 * @param model
	 * @param objPage
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/finance/studentpayment/returnAmountInfo.html")
	public String getReturnAmountsPage(HttpServletRequest request, ModelMap model, Page objPage) throws WebException {
		objPage.setOrderBy("unit.unitCode");
		objPage.setOrder(Page.ASC);
		Map<String,Object> condition = Condition2SQLHelper.getConditionFromResquestByIterator(request);
		Page pageResult = returnAmountsService.findByCondition(objPage,condition);
		model.addAttribute("condition",condition);
		model.addAttribute("pageResult",pageResult);
		return "/edu3/finance/returnAmounts/returnAmounts_list";
	}

	/**
	 * 下载模版
	 * @param response
	 */
	@RequestMapping("/edu3/finance/returnAmount/downloadModel.html")
	public void downloadModel(HttpServletResponse response) {
		try{
			setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
			//模板文件路径
			String templateFilepathString = "returnAmountInputModel.xls";
			downloadFile(response, "已返学费金额录入模版.xls", templateFilepathString,false);
		}catch(Exception e){
			String msg = "导出excel文件出错：找不到该文件-已返学费金额录入模版.xls";
			logger.error("下载成绩录入模版出错", e);
			renderHtml(response, "<script>alertMsg.warn("+"\""+msg+"\""+")</script>");
		}
	}

	/**
	 * 导入文件选择窗口
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/finance/returnAmount/inputAmounts.html")
	public String inputDialog(ModelMap model) throws WebException {
		model.addAttribute("title", "导入已返学费金额");
		model.addAttribute("formId", "returnAmount_import");
		model.addAttribute("url", "/edu3/finance/returnAmount/importAmounts.html");
		return "edu3/roll/inputDialogForm";
	}

	/**
	 * 导入Excel文件
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/finance/returnAmount/importAmounts.html")
	public void importReturnAmounts(HttpServletRequest request,HttpServletResponse response) {
		StringBuffer message = new StringBuffer("");
		boolean success = true;
		String result = "";
		Map<String, Object > returnMap = new HashMap<String, Object>();
		try {
			do {
				String attchID = ExStringUtils.trimToEmpty(request.getParameter("attachId"));

				if (null != attchID && attchID.split(",").length > 1) {
					success = false;
					message.append("一次只能导入一个成绩文件,谢谢！");
				} else if (null != attchID && attchID.split(",").length == 1) {
					Attachs attachs = attachsService.get(attchID.split(",")[0]);
					String filePath = attachs.getSerPath() + File.separator + attachs.getSerName();
					Map<String, Object> singleMap = returnAmountsService.importExcelModel(filePath);
					if (singleMap != null && singleMap.size() > 0) {
						int totalCount = (Integer) singleMap.get("totalCount");
						int successCount = (Integer) singleMap.get("successCount");
						message.append((String) singleMap.get("message"));
						result = "导入共" + totalCount + "条,成功" + successCount + "条,失败" + (totalCount - successCount) + "条";
					}
					File f = new File(filePath);
					if (f.exists()) {
						f.delete();
					}
					attachsService.delete(attachs);
				}

			} while (false);
		} catch (Exception e) {
			logger.error("处理导入成绩出错", e);
			success = false;
			result = "导入失败";
		} finally {
			returnMap.put("success",success);
			returnMap.put("msg",message);
			returnMap.put("result",result);
			renderJson(response, JsonUtils.mapToJson(returnMap));
		}
	}

	/**
	 *  新增/编辑 已返学费金额
	 * @param resourceid
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/finance/returnAmount/input.html")
	public String editReturnAmounts(String resourceid,ModelMap model) throws WebException {
		User user = SpringSecurityHelper.getCurrentUser();
		if(user.getOrgUnit().getUnitType().indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员
			model.addAttribute("brschool", true);
		}
		ReturnAmounts returnAmounts = null;
		if(ExStringUtils.isNotBlank(resourceid)){ //-----编辑
			returnAmounts = returnAmountsService.get(resourceid);
		}else{ //----------------------------------------新增
			returnAmounts = new ReturnAmounts();
			if(user.getOrgUnit().getUnitType().indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员
				returnAmounts.setUnit(user.getOrgUnit());
			}
		}
		model.addAttribute("returnAmounts", returnAmounts);
		return "/edu3/finance/returnAmounts/returnAmounts_form";
	}

	/**
	 * 保存已返学费金额
	 * @param returnAmounts
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/finance/returnAmount/save.html")
	public void saveUserTimeManage(ReturnAmounts returnAmounts, HttpServletRequest request, HttpServletResponse response) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			do{
				String branchSchool =  ExStringUtils.trimToEmpty(request.getParameter("branchSchoolId"));
				String yearInfoid = ExStringUtils.trimToEmpty(request.getParameter("yearInfoid"));
				if(ExStringUtils.isNotBlank(returnAmounts.getResourceid())){ //--------------------更新
					ReturnAmounts persistuserReturnAmounts = returnAmountsService.get(returnAmounts.getResourceid());
					ExBeanUtils.copyProperties(persistuserReturnAmounts, returnAmounts);
					returnAmounts = persistuserReturnAmounts;
				}else{ //-------------------------------------------------------------------新增
					List<ReturnAmounts> list = returnAmountsService.findByHql("from "+ReturnAmounts.class.getSimpleName()+" c where c.isDeleted=0"
									+ " and c.yearInfo.resourceid=? c.unit.resourceid=? and c.count=?",yearInfoid,branchSchool,returnAmounts.getCount());
					if(list!=null && list.size()>0 && ExStringUtils.isBlank(returnAmounts.getResourceid())){
						map.put("statusCode", 300);
						map.put("message", "年度、教学点、次数必须唯一！");
						continue;
					}
				}

				OrgUnit orgUnit = orgUnitService.get(branchSchool);
				returnAmounts.setUnit(orgUnit);
				YearInfo yearInfo = yearInfoService.get(yearInfoid);
				returnAmounts.setYearInfo(yearInfo);
				returnAmountsService.saveOrUpdate(returnAmounts);
				map.put("statusCode", 200);
				map.put("message", "保存成功！");
				//map.put("navTabId", "RES_WORK_TIMEMANAGE_INPUT");
				map.put("reloadUrl", request.getContextPath() +"/edu3/finance/studentpayment/returnAmountInfo.html");

			}while(false);
		}catch (Exception e) {
			logger.error("保存出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}

	/**
	 * 删除已返学费金额记录
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/finance/returnAmount/remove.html")
	public void deleteReturnAmount(HttpServletRequest request,HttpServletResponse response) {
		String resourceid = request.getParameter("resourceid");
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(resourceid)){
				if(resourceid.split("\\,").length >0) {
					returnAmountsService.batchDelete(resourceid.split("\\,"));
				}
				map.put("statusCode", 200);
				map.put("message", "删除成功！");
				map.put("forward", request.getContextPath()+"/edu3/finance/studentpayment/returnAmountInfo.html");
			}
		} catch (Exception e) {
			logger.error("删除出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
}
