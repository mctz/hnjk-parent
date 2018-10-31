package com.hnjk.edu.finance.controller;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
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
import com.hnjk.core.foundation.utils.ExCollectionUtils;
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.GUIDUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.FileUploadAndDownloadSupportController;
import com.hnjk.core.support.context.Constants;
import com.hnjk.core.support.context.SystemContextHolder;
import com.hnjk.edu.finance.model.StuPerpayfee;
import com.hnjk.edu.finance.service.IStuPerpayfeeService;
import com.hnjk.edu.roll.service.IRollJDBCService;
import com.hnjk.extend.plugin.excel.config.MergeCellsParam;
import com.hnjk.extend.plugin.excel.service.IExportExcelService;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.service.IDictionaryService;
import com.hnjk.platform.system.service.ISysConfigurationService;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;

/**
 * <code>StuPerpayfeeController</code>
 * <p>
 * 学生预交费用表.
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2010-4-26 下午03:22:25
 * @see
 * @version 1.0
 */
@Controller
public class StuPerpayfeeController extends FileUploadAndDownloadSupportController {

	private static final long serialVersionUID = 1802218469458763531L;
	
	@Autowired
	@Qualifier("stuperpayfeeservice")
	private IStuPerpayfeeService stuPerpayfeeService;
	
	@Autowired
	@Qualifier("exportExcelService")
	private IExportExcelService exportExcelService;//Excel导出服务
	
	@Autowired
	@Qualifier("dictService")
	private IDictionaryService dictionaryService;
	
	@Autowired
	@Qualifier("rollJDBCService")
	private IRollJDBCService rollJDBCService;
	
	@Autowired
	@Qualifier("sysConfigurationService")
	private ISysConfigurationService sysConfigurationService;

	/**
	 * 查询分页列表
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/schoolroll/stuperpayfee/list.html")
	public String exeList(String grade, String branchSchool, String studyNo, String studentName,String status, Page objPage, ModelMap model) throws WebException {
		objPage.setOrderBy("studentInfo.branchSchool.unitCode");
		objPage.setOrder(Page.ASC);// 设置默认排序方式
		Map<String, Object> condition = new HashMap<String, Object>();// 查询条件

		User user = SpringSecurityHelper.getCurrentUser();
		if(user.getOrgUnit().getUnitType().indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员
			branchSchool = user.getOrgUnit().getResourceid();
			model.addAttribute("isBrschool", true);
		}
		
		if (ExStringUtils.isNotEmpty(grade)) {
			condition.put("grade", grade);
		}
		if (ExStringUtils.isNotEmpty(branchSchool)) {
			condition.put("branchSchool", branchSchool);
		}
		if (ExStringUtils.isNotEmpty(studyNo)) {
			condition.put("studyNo", studyNo);
		}
		if (ExStringUtils.isNotEmpty(studentName)) {
			condition.put("studentName", studentName);
		}
		if (ExStringUtils.isNotEmpty(status)) {
			condition.put("status", status);
		}

		Page page = stuPerpayfeeService.findFactFeeByCondition(condition, objPage);
		model.addAttribute("condition", condition);
		model.addAttribute("feeList", page);
		return "/edu3/finance/stuperpayfee/stuperpayfee-list";
	}

	/**
	 * 导出Excel
	 * 
	 * @param resourceid
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/schoolroll/stuperpayfee/exeExcel.html")
	public void exeExcel(HttpServletRequest request,HttpServletResponse response,ModelMap map) throws WebException {
		Map<String, Object> condition = new HashMap<String, Object>();
		//已录取
		condition.put("isMatriculate", "Y");
		
		List<StuPerpayfee> list = stuPerpayfeeService.getAll();
		//文件输出服务器路径
		setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
		try{
			//导出
			File excelFile = null;
			GUIDUtils.init();
			File disFile = new File(getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(false) + ".xls");
				Map<String,Object> templateMap = new HashMap<String, Object>();//设置模板
				
				//模板文件路径
				String templateFilepathString = "stuperpayfee.xls";
				List<String> dictCodeList = new ArrayList<String>();
				dictCodeList.add("CodeSex");
				dictCodeList.add("CodeNation");
				dictCodeList.add("CodeLearningStyle");
				//初始化配置参数
				exportExcelService.initParmasByfile(disFile, "StuPerpayfee", list,dictionaryService.getDictionByMap(dictCodeList, true,IDictionaryService.PREKEY_TYPE_BYCODE));
				exportExcelService.getModelToExcel().setRowHeight(500);//设置行高
				
				exportExcelService.getModelToExcel().setTemplateParam(templateFilepathString, 2, templateMap);
				excelFile = exportExcelService.getExcelFile();//获取导出的文件
			logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());
			downloadFile(response, "学生信息.xls", excelFile.getAbsolutePath(),true);
		}catch(Exception e){
			logger.error("导出excel文件出错："+e.fillInStackTrace());
		}
	}

	/**
	 * 同步学费
	 * 
	 * @param grade
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/schoolroll/stuperpayfee/saveSynchron.html")
	public void exeSaveSynchron(HttpServletRequest request, HttpServletResponse response) throws WebException {
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			stuPerpayfeeService.synchronStuFee();	
			map.put("statusCode", 200);
			map.put("message", "同步成功！");			
			map.put("navTabId", "RES_STU_PAYDETAIL");
			map.put("reloadTabUrl", request.getContextPath() +"/edu3/schoolroll/stuperpayfee/list.html");
		} catch (Exception e) {
			map.put("statusCode", 300);
			map.put("message", "同步失败！<br/>请查看系统日志.");
			logger.error(""+e.fillInStackTrace());
		}
		renderJson(response, JsonUtils.mapToJson(map));	
	}

	/**
	 * 缴费情况统计
	 * @param year
	 * @param type
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/schoolroll/stuperpayfee/stat/list.html")
	public String listStudentFactFeeStat(String year, String type,String currentIndex, ModelMap model) throws WebException {
		Map<String, Object> condition = new HashMap<String, Object>();// 查询条件
		type = ExStringUtils.defaultIfEmpty(type, "baseType");
		year = ExStringUtils.defaultIfEmpty(year,Long.toString(ExDateUtils.getCurrentYear()));	
		currentIndex = ExStringUtils.defaultIfEmpty(currentIndex, "0");
		List<Integer> yearList = new ArrayList<Integer>();
		for (int i = Long.valueOf(ExDateUtils.getCurrentYear()).intValue(); i >= 2005; i--) {
			yearList.add(i);
		}
		model.addAttribute("yearList", yearList);
		condition.put("year", year);
		condition.put("type", type);
		condition.put("currentIndex", currentIndex);
		model.addAttribute("condition", condition);
		if(ExStringUtils.isNotBlank(year)){					
			User user = SpringSecurityHelper.getCurrentUser();
			String brSchool = null;
			if (CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())){//校外学习中心人员
				brSchool = user.getOrgUnit().getResourceid();
			}
			if("baseType".equals(type)){//基本报表
				Map<String, Object> map = new HashMap<String, Object>();
				String[] statTypes = {"teachingType","major","classic","brSchool"};
				for (String statType : statTypes) {
					List<Map<String, Object>> list = rollJDBCService.statStudentFactFee(Integer.valueOf(year), statType, brSchool);
					calculateTotal(list);//加入总计
					map.put(statType, list);
				}		
				model.addAttribute("statListMap", map);
				model.addAttribute("statTypes", statTypes);
			} else {//分级报表
				condition.put("currentIndex", "0");
				List<Map<String, Object>> list = rollJDBCService.statStudentFactFee(Integer.valueOf(year), "subType",brSchool);
				calculateTotal(list);//加入总计
				model.addAttribute("subTypeList", list);
			}
		}		
		return "/edu3/finance/stufee/stufee-stat";
	}

	/**
	 * 计算各项总计
	 * @param list
	 */
	private void calculateTotal(List<Map<String, Object>> list) {
		if(ExCollectionUtils.isNotEmpty(list)){
			BigDecimal recpayfeeCount = new BigDecimal(0);
			BigDecimal recpayfee = new BigDecimal(0);
			BigDecimal fullfeeCount = new BigDecimal(0);
			BigDecimal fullfeeSum = new BigDecimal(0);
			BigDecimal deratefeeCount = new BigDecimal(0);
			BigDecimal deratefeeSum = new BigDecimal(0);
			BigDecimal owefeeCount = new BigDecimal(0);
			BigDecimal owefeeSum = new BigDecimal(0);
			BigDecimal partfeeCount = new BigDecimal(0);
			BigDecimal partfeeSum = new BigDecimal(0);
			for (Map<String, Object> obj : list) {
				BigDecimal objFullfeeCount = (BigDecimal)obj.get("FULLFEECOUNT");
				BigDecimal objAllfeeCount = (BigDecimal)obj.get("ALLFEECOUNT");
				recpayfeeCount = recpayfeeCount.add((BigDecimal)obj.get("ALLFEECOUNT"));
				recpayfee = recpayfee.add((BigDecimal)obj.get("RECPAYFEE"));
				fullfeeCount = fullfeeCount.add((BigDecimal)obj.get("FULLFEECOUNT"));
				fullfeeSum = fullfeeSum.add((BigDecimal)obj.get("FULLFEESUM"));
				deratefeeCount = deratefeeCount.add((BigDecimal)obj.get("DERATEFEECOUNT"));
				deratefeeSum = deratefeeSum.add((BigDecimal)obj.get("DERATEFEESUM"));
				owefeeCount = owefeeCount.add((BigDecimal)obj.get("OWEFEECOUNT"));
				owefeeSum = owefeeSum.add((BigDecimal)obj.get("OWEFEESUM"));
				partfeeCount = partfeeCount.add((BigDecimal)obj.get("PARTFEECOUNT"));
				partfeeSum = partfeeSum.add((BigDecimal)obj.get("PARTFEESUM"));
				BigDecimal fullfeePer = new BigDecimal(0);
				if(objAllfeeCount.longValue()>0){
					fullfeePer = objFullfeeCount.divide(objAllfeeCount, 3, BigDecimal.ROUND_HALF_UP);
				}
				obj.put("fullfeePer", fullfeePer);
				obj.put("fullfeePerStr", fullfeePer.multiply(new BigDecimal(100)).divide(BigDecimal.ONE, 1, BigDecimal.ROUND_HALF_UP)+"%");
			}
			Map<String, Object> total = new HashMap<String, Object>();
			total.put("statType", "总：");
			total.put("allfeeCount", recpayfeeCount);
			total.put("recpayfee", recpayfee);
			total.put("fullfeeCount", fullfeeCount);
			total.put("fullfeeSum", fullfeeSum);
			total.put("deratefeeCount", deratefeeCount);
			total.put("deratefeeSum", deratefeeSum);
			total.put("owefeeCount", owefeeCount);
			total.put("owefeeSum", owefeeSum);
			total.put("partfeeCount", partfeeCount);
			total.put("partfeeSum", partfeeSum);
			BigDecimal totalFullfeePer = new BigDecimal(0);
			if(recpayfeeCount.longValue()>0){
				totalFullfeePer = fullfeeCount.divide(recpayfeeCount, 3, BigDecimal.ROUND_HALF_UP);
			}
			total.put("fullfeePer", totalFullfeePer);
			total.put("fullfeePerStr", totalFullfeePer.multiply(new BigDecimal(100)).divide(BigDecimal.ONE, 1, BigDecimal.ROUND_HALF_UP)+"%");
			list.add(total);
		}		
	}
	/**
	 * 导出缴费情况统计
	 * @param year
	 * @param type
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/schoolroll/stuperpayfee/stat/export.html")
	public void exportStudentFactFeeStat(String year, String type,String currentIndex,HttpServletRequest request,HttpServletResponse response) throws WebException {
		type = ExStringUtils.defaultIfEmpty(type, "baseType");
		year = ExStringUtils.defaultIfEmpty(year,"0");
		currentIndex = ExStringUtils.defaultIfEmpty(currentIndex, "0");
		String brSchool = null;
		User user = SpringSecurityHelper.getCurrentUser();
		if (CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())){//校外学习中心人员
			brSchool = user.getOrgUnit().getResourceid();
		}
		//文件输出服务器路径
		setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
		try{
			//导出
			GUIDUtils.init();
			File disFile = new File(getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(false) + ".xls");
			Map<String,Object> templateMap = new HashMap<String, Object>();//设置模板	
			templateMap.put("yearInfo", ("0".equals(year)?"":(year+"年")));
			//初始化配置参数
			List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
			String[] filterColunms = {"chargeyear","unitName","statCode","statType","allfeeCount","recpayfee","fullfeeCount","fullfeeSum","deratefeeCount","deratefeeSum","partfeeCount","partfeeSum","owefeeCount","owefeeSum","fullfeePerStr","memo"};
			String[] filterColunms2 = {"chargeyear","statCode","statType","allfeeCount","recpayfee","fullfeeCount","fullfeeSum","deratefeeCount","deratefeeSum","partfeeCount","partfeeSum","owefeeCount","owefeeSum","fullfeePerStr","memo"};
			String[] filterColunms3 = {"chargeyear","statType","allfeeCount","recpayfee","fullfeeCount","fullfeeSum","deratefeeCount","deratefeeSum","partfeeCount","partfeeSum","owefeeCount","owefeeSum","fullfeePerStr","memo"}; 
			List<String> filterColumnList = Arrays.asList(filterColunms);//定义过滤列
			List<MergeCellsParam> mergeCellsParams = new ArrayList<MergeCellsParam>();
			
			String statType = "";
			String templateFileName = "studentFactFeeStat.xls";
			if("subType".equals(type)){
				statType = "subType";
				filterColumnList = Arrays.asList(filterColunms);
				templateFileName = "studentFactFeeStat2.xls";
			} else {
				if("1".equals(currentIndex)) {
					templateMap.put("statTypeName", "专业");
					statType = "major";
					filterColumnList = Arrays.asList(filterColunms3);
				} else if("2".equals(currentIndex)) {
					templateMap.put("statTypeName", "层次");
					statType = "classic";
					filterColumnList = Arrays.asList(filterColunms3);
				} else if("3".equals(currentIndex)) {
					templateMap.put("statTypeName", "学习中心");
					statType = "brSchool";
					filterColumnList = Arrays.asList(filterColunms3);
				} else {
					templateFileName = "studentFactFeeStat1.xls";
					templateMap.put("statTypeName", "办学模式");
					statType = "teachingType";
					filterColumnList = Arrays.asList(filterColunms2);
				}
			}
			//模板文件路径
			String templateFilepathString = templateFileName;
			list = rollJDBCService.statStudentFactFee(Integer.valueOf(year), statType,brSchool);
			if(ExCollectionUtils.isNotEmpty(list)){					
				getMergeCellsParam(list, mergeCellsParams, statType);//获取合并单元格
				if("teachingType".equals(statType)){
					getMergeCellsParam(list, mergeCellsParams, "chyear");//合并年度
				}
				calculateTotal(list);//加入总计
			}
			exportExcelService.initParamsByfile(disFile, "studentFactFeeStat", list, null, filterColumnList, mergeCellsParams);		
			exportExcelService.getModelToExcel().setTemplateParam(templateFilepathString, 4, templateMap);
			disFile = exportExcelService.getExcelFile();//获取导出的文件
			logger.info("获取导出的excel文件:{}",disFile.getAbsoluteFile());
			downloadFile(response, ("0".equals(year)?"":(year+"年"))+"缴费情况统计.xls", disFile.getAbsolutePath(),true);
		}catch(Exception e){
			logger.error("导出excel文件出错："+e.fillInStackTrace());
		}
	}

	private void getMergeCellsParam(List<Map<String, Object>> list, List<MergeCellsParam> mergeCellsParams, String statType) {
		int start = 0;
		String tempStr = null;					
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> obj = list.get(i);
			String str = obj.get("CHARGEYEAR").toString();
			if("subType".equals(statType)){
				str = obj.get("CHARGEYEAR").toString()+(obj.get("UNITNAME")!=null?obj.get("UNITNAME").toString():"")+(obj.get("STATCODE")!=null?obj.get("STATCODE").toString():"");
			} else if("teachingType".equals(statType)){
				str = obj.get("CHARGEYEAR").toString()+(obj.get("STATCODE")!=null?obj.get("STATCODE").toString():"");
			}					
			if(!ExStringUtils.equals(tempStr, str) && tempStr!=null){
				if(i-start>1){					
					if("subType".equals(statType)){
						mergeCellsParams.add(new MergeCellsParam(0,start+4,0,i+4-1));
						mergeCellsParams.add(new MergeCellsParam(1,start+4,1,i+4-1));
						mergeCellsParams.add(new MergeCellsParam(2,start+4,2,i+4-1));
					} else if("teachingType".equals(statType)){
						mergeCellsParams.add(new MergeCellsParam(1,start+4,1,i+4-1));
					} else {
						mergeCellsParams.add(new MergeCellsParam(0,start+4,0,i+4-1));
					}
				} 							
				start = i;
			}	
			if(i==list.size()-1 && i-start>0){				
				if("subType".equals(statType)){
					mergeCellsParams.add(new MergeCellsParam(0,start+4,0,i+4));
					mergeCellsParams.add(new MergeCellsParam(1,start+4,1,i+4));
					mergeCellsParams.add(new MergeCellsParam(2,start+4,2,i+4));
				} else if("teachingType".equals(statType)){					
					mergeCellsParams.add(new MergeCellsParam(1,start+4,1,i+4));
				} else {
					mergeCellsParams.add(new MergeCellsParam(0,start+4,0,i+4));
				}
			}
			tempStr = str;
		}
	}
}
