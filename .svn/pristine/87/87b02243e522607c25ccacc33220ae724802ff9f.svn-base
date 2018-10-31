package com.hnjk.edu.recruit.controller;

import java.io.File;
import java.util.Date;
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
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.FileUploadAndDownloadSupportController;
import com.hnjk.core.support.context.Constants;
import com.hnjk.core.support.context.SystemContextHolder;
import com.hnjk.edu.basedata.model.Classic;
import com.hnjk.edu.basedata.model.Grade;
import com.hnjk.edu.basedata.model.Major;
import com.hnjk.edu.basedata.service.IClassicService;
import com.hnjk.edu.basedata.service.IGradeService;
import com.hnjk.edu.basedata.service.IMajorService;
import com.hnjk.edu.recruit.model.EnrollmentBookInfo;
import com.hnjk.edu.recruit.service.IEnrollmentBookInfoService;
import com.hnjk.edu.recruit.service.IRecruitmentScopeService;
import com.hnjk.edu.recruit.vo.EnrollmentBookInfoVO;
import com.hnjk.extend.plugin.excel.service.IExportExcelService;
import com.hnjk.extend.plugin.excel.service.IImportExcelService;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.Attachs;
import com.hnjk.platform.system.service.IAttachsService;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.OrgUnit;
import com.hnjk.security.model.User;
import com.hnjk.security.service.IOrgUnitService;

/** 
 * @author Zik, 广东学苑教育发展有限公司
 * @since 2018年6月28日 下午4:10:13 
 * 
 */
@Controller
public class EnrollmentBookInfoController extends FileUploadAndDownloadSupportController {

	private static final long serialVersionUID = 7691403834321358004L;

	@Autowired
	@Qualifier("enrollmentBookInfoService")
	private IEnrollmentBookInfoService enrollmentBookInfoService;
	
	@Autowired
	@Qualifier("orgUnitService")
	private IOrgUnitService orgUnitService;
	
	@Autowired
	@Qualifier("gradeService")
	private IGradeService gradeService;
	
	@Autowired
	@Qualifier("classicService")
	private IClassicService classicService;
	
	@Autowired
	@Qualifier("majorService")
	private IMajorService majorService;
	
	@Autowired
	@Qualifier("recruitmentScopeService")
	private IRecruitmentScopeService recruitmentScopeService;
	
	@Autowired
	@Qualifier("attachsService")
	private IAttachsService attachsService;//注入附件服务
	
	@Autowired
	@Qualifier("importExcelService")
	private IImportExcelService importExcelService;
	
	@Autowired
	@Qualifier("exportExcelService")
	private IExportExcelService exportExcelService;
	
	/**
	 * 查询分页列表
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/enrollment/booking/list.html")
	public String exeList(HttpServletRequest request,Page objPage,ModelMap model) throws WebException{
		objPage.setOrderBy("unit.unitCode,classic.resourceid,major.majorCode");
		objPage.setOrder(Page.ASC);//设置默认排序方式
		// 获取处理后的参数
		Map<String, Object> condition = getParams(request);
		
		Page page = enrollmentBookInfoService.findByCondition(condition, objPage);
		
		model.addAttribute("infoList", page);
		model.addAttribute("condition", condition);
		
		return "/edu3/enrollmentBook/bookingInfo-list";
	}

	/**
	 * 获取处理后的参数
	 * 
	 * @param request
	 * @return
	 */
	private Map<String, Object> getParams(HttpServletRequest request) {
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件
		String unitId = request.getParameter("unitId");
		String gradeId = request.getParameter("gradeId");
		String classicId = request.getParameter("classicId");
		String majorId = request.getParameter("majorId");
		String studentName = request.getParameter("studentName");
		String certNum = request.getParameter("certNum");
		String operatorName = request.getParameter("operatorName");
		
		String isAdmin = "Y";
		User curUser = SpringSecurityHelper.getCurrentUser();
		if (CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(curUser.getOrgUnit().getUnitType())) {
			unitId = curUser.getOrgUnit().getResourceid();
			isAdmin = "N";
		}
		
		condition.put("isAdmin", isAdmin);
		
		if(ExStringUtils.isNotEmpty(unitId)) {
			condition.put("unitId", unitId);
		}
		if(ExStringUtils.isNotEmpty(gradeId)) {
			condition.put("gradeId", gradeId);
		}
		if(ExStringUtils.isNotEmpty(classicId)) {
			condition.put("classicId", classicId);
		}
		if(ExStringUtils.isNotEmpty(majorId)) {
			condition.put("majorId", majorId);
		}
		if(ExStringUtils.isNotEmpty(studentName)) {
			condition.put("studentName", studentName);
		}
		if(ExStringUtils.isNotEmpty(certNum)) {
			condition.put("certNum", certNum);
		}
		if(ExStringUtils.isNotEmpty(operatorName)) {
			condition.put("operatorName", operatorName);
		}
		return condition;
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
	@RequestMapping("/edu3/enrollment/booking/edit.html")
	public String exeEdit(String resourceid,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		if(ExStringUtils.isNotBlank(resourceid)){ //-----编辑
			EnrollmentBookInfo info = enrollmentBookInfoService.load(resourceid);	
			model.addAttribute("info", info);
		}else{ //----------------------------------------新增
			model.addAttribute("info", new EnrollmentBookInfo());			
		}
		String isAdmin = "Y";
		User curUser = SpringSecurityHelper.getCurrentUser();
		if (CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(curUser.getOrgUnit().getUnitType())) {
			isAdmin = "N";
		}
		
		model.addAttribute("isAdmin", isAdmin);	
		
		return "/edu3/enrollmentBook/bookingInfo-form";
	}
	
	/**
	 * 保存更新表单
	 * @param info
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/enrollment/booking/save.html")
	public void exeSave(EnrollmentBookInfo info,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		int statusCode = 200;
		String message = "保存成功！";
		try {
			EnrollmentBookInfo persistInfo = null;
			do{
				if(ExStringUtils.isBlank(info.getGradeId())){
					 statusCode = 300;
					 message = "年级不能为空！";
					 continue;
				}
				Grade grade = gradeService.get(info.getGradeId());
				// 判断是否在开放的时间端内
				Map<String,Object> ao = enrollmentBookInfoService.isAllowOperate(grade.getYearInfo().getResourceid());
				statusCode = (Integer)ao.get("statusCode");
				if(statusCode==300){
					message = (String)ao.get("message");
					continue;
				}
				// 操作人
				User curUser = SpringSecurityHelper.getCurrentUser();
				boolean isTheSame = false;
				if(ExStringUtils.isNotBlank(info.getResourceid())){ //--------------------更新
					persistInfo = enrollmentBookInfoService.get(info.getResourceid());
					if(persistInfo.getCertNum().equals(info.getCertNum())){
						isTheSame = true;
					}
				}else{ //-------------------------------------------------------------------保存
					persistInfo = new EnrollmentBookInfo();		
					info.setCreateDate(new Date());
					info.setOperator(curUser);
					info.setOperatorName(curUser.getCnName());
				}		
				// 判断同一年级，身份证号相同的学生是否已存在
				List<String> hasExistStudentList = enrollmentBookInfoService.findCertNumByGradeId(info.getGradeId());
				if(!isTheSame && hasExistStudentList.contains(info.getCertNum())){
					statusCode = 300;
					 message = "该学生已经预约了招生报读！";
					 continue;
				}
				// 检查该学生是否属于该教学点的招生范围内
				if (CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(curUser.getOrgUnit().getUnitType())) {
					String unitId = curUser.getOrgUnit().getResourceid();
					List<String> scopeList = recruitmentScopeService.findScopeByUnitId(unitId);
					info.setUnitId(unitId);
					if(!enrollmentBookInfoService.isInScope(info.getCertNum(), scopeList)){
						statusCode = 300;
						 message = "该学生不属于您的招生范围内！";
						 continue;
					}
				}
				
				if(ExStringUtils.isNotBlank(info.getUnitId())){
					OrgUnit unit = orgUnitService.get(info.getUnitId());
					info.setUnit(unit);
				}
				info.setGrade(grade);
				if(ExStringUtils.isNotBlank(info.getClassicId())){
					Classic classic = classicService.get(info.getClassicId());
					info.setClassic(classic);
				}
				if(ExStringUtils.isNotBlank(info.getMajorId())){
					Major major = majorService.get(info.getMajorId());
					info.setMajor(major);
				}
				
				ExBeanUtils.copyProperties(persistInfo, info);
				
				enrollmentBookInfoService.saveOrUpdate(persistInfo);
				// 成功
				// 提示语
				StringBuffer tip = new StringBuffer(500);
				tip.append("注意：您已在学校进行预约报读，请你在<font color='red' style='font-weight: bolder;'>")
				.append(CacheAppManager.getSysConfigurationByCode("examinationTime").getParamValue())
				// TODO:以后有其他学校也使用这个模块再做成全局参数
				.append("</font>到广西招生考试院网上进行正式报考（网站地址：<font color='red' style='font-weight: bolder;'>www.gxeea.edu.cn</font>）,并按要求进行确认和参加成人高考。");
				message = tip.toString();
				
				map.put("navTabId", "RES_ENROLLMENT_BOOKING_INFOLIST");
				map.put("reloadUrl", request.getContextPath() +"/edu3/enrollment/booking/list.html?resourceid="+persistInfo.getResourceid());
			} while(false);
		}catch (Exception e) {
			logger.error("保存招生预约报读信息出错：{}",e.fillInStackTrace());
			statusCode = 300;
			message = "保存失败!";
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
	@RequestMapping("/edu3/enrollment/booking/delete.html")
	public void exeDelete(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		String resourceid = request.getParameter("resourceid");
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(resourceid)){
				if(resourceid.split("\\,").length >1){//批量删除					
					enrollmentBookInfoService.batchCascadeDelete(resourceid.split("\\,"));
				}else{//单个删除
					enrollmentBookInfoService.delete(resourceid);
				}
				map.put("statusCode", 200);
				map.put("message", "删除成功！");				
				map.put("forward", request.getContextPath()+"/edu3/enrollment/booking/list.html");
			}
		} catch (Exception e) {
			logger.error("删除招生预约报读信息出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 下载导入招生预约报读信息模板
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/enrollment/booking/downloadTemplate.html")
	public void downloadTemplate(HttpServletRequest request, HttpServletResponse response){
		try{
			//模板文件路径
			String templateFilepathString = "enrollmentBook"+File.separator+"enrollmentBookInfoImport.xls";
			downloadFile(response, "招生预约报读信息模板.xls", templateFilepathString,false);
		}catch(Exception e){
			logger.error("下载导入招生预约报读信息模板出错：",e);
			renderHtml(response, "<script type=\"text/javascript\">parent.alertMsg.warn(\"下载导入招生预约报读信息模板失败\");</script>");
		}
	}
	
	/**
	 * 进入导入招生预约报读信息页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/edu3/enrollment/booking/batchImport-view.html")
	public String uploadEnrollmentBookForm(HttpServletRequest request,HttpServletResponse response, ModelMap model){		
		model.addAttribute("examinationTime", CacheAppManager.getSysConfigurationByCode("examinationTime").getParamValue());
		return "/edu3/enrollmentBook/batchImport-view";
	}
	
	/**
	 * 导入招生预约报读信息逻辑处理
	 * 
	 * @param attachId
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/edu3/enrollment/booking/batchImport.html")
	public void batchImportEnrollmentBook(String attachId, HttpServletRequest request,HttpServletResponse response) throws WebException{				
		Map<String ,Object> map = new HashMap<String, Object>();
		int statusCode = 200;
		String message = "导入招生预约报读信息成功！";
		int totalNum =0 ;
		int failNum = 0;
		try {			
			List<EnrollmentBookInfoVO> failList = null;
			if( ExStringUtils.isNotBlank(attachId)){			
				Attachs attachs =  attachsService.get(attachId);//获取上传的文件，因为是单个文件，所以get(0)就可
				File excel = new File(attachs.getSerPath()+File.separator+attachs.getSerName());
				importExcelService.initParmas(excel, "importEnrollmentBook", null);
				importExcelService.getExcelToModel().setSheet(0);//设置excel sheet 0 
				importExcelService.getExcelToModel().setStartTitleRow(3);
				List<EnrollmentBookInfoVO> modelList = importExcelService.getModelList();
				if(ExCollectionUtils.isNotEmpty(modelList)){
					String gradeId = ExStringUtils.defaultIfEmpty(request.getParameter("gradeId"), "");
					Map<String, Object> returnMap = enrollmentBookInfoService.handleEnrollmentBookInfoImport(modelList,gradeId);
					totalNum = modelList.size();
					if(returnMap!=null &&returnMap.size()>0 ){
						statusCode = (Integer)returnMap.get("statusCode");
						if(statusCode!=200) {
							message = (String)returnMap.get("message");
							if(statusCode==400){
								failList = (List<EnrollmentBookInfoVO>)returnMap.get("failList");
							}
						}
					}
					
					//导出招生预约报读信息的失败记录
					if(ExCollectionUtils.isNotEmpty(failList)){
						failNum = failList.size();
						setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
						//导出
						GUIDUtils.init();
						String fileName = GUIDUtils.buildMd5GUID(false);
						File disFile = new File(getDistfilepath()+ File.separator + fileName + ".xls");
							
						//模板文件路径
						String templateFilepathString = "enrollmentBook"+File.separator+"enrollmentBookInfoImportError.xls";
						//初始化配置参数
						exportExcelService.initParmasByfile(disFile,"importEnrollmentBookError", failList,null);
						exportExcelService.getModelToExcel().setTemplateParam(templateFilepathString, 3, null);
						exportExcelService.getModelToExcel().setRowHeight(400);
						File excelFile = exportExcelService.getExcelFile();//获取导出的文件(将导出文件写到硬盘对应的目录中)
						logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());	
						String upLoadurl = "/edu3/enrollment/booking/exportEnrollmentBookError.html?excelFile="+fileName;
						map.put("exportErrorEnrollmentBook", upLoadurl);
					}
				} else {
					statusCode = 300;				
					message = "没有数据";
				}
			} else {
				statusCode = 300;				
				message = "请上传附件.";
			}
		} catch (Exception e) {
			logger.error("导入招生预约报读信息出错:{}",e);
			statusCode = 300;
			message = "导入招生预约报读信息失败！";
		} finally {
			map.put("statusCode", statusCode);
			map.put("message", message);
			map.put("resultMsg", "导入成功："+(totalNum-failNum)+"条，失败："+failNum+"条");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 导出招生预约报读信息
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/enrollment/booking/exportEnrollmentBookError.html")
	public void exportEnrollmentBookError(String excelFile,HttpServletRequest request,HttpServletResponse response) throws Exception {
		setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
		File disFile = new File(getDistfilepath()+ File.separator + excelFile+".xls");
		downloadFile(response, "导入招生预约报读信息失败记录.xls", disFile.getAbsolutePath(),true);
	}
	
	/**
	 * 导出招生预约报读信息 
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/edu3/enrollment/booking/export.html")
	public void exportEnrollmentBookInfo(HttpServletRequest request,HttpServletResponse response) throws Exception {
		try {
			// 获取处理后的参数
			Map<String, Object> condition = getParams(request);
			String resourceids = request.getParameter("resourceids");
			if(ExStringUtils.isNotBlank(resourceids)){
				condition.clear();
				condition.put("resourceids", resourceids.replaceAll(",", "','"));
			}
			
			List<EnrollmentBookInfoVO> enrollmentBookInoList = enrollmentBookInfoService.findVoByCondition(condition);
			if(ExCollectionUtils.isNotEmpty(enrollmentBookInoList)){
				setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
				//导出
				GUIDUtils.init();
				String fileName = GUIDUtils.buildMd5GUID(false);
				File disFile = new File(getDistfilepath()+ File.separator + fileName + ".xls");
					
				//模板文件路径
				String templateFilepathString = "enrollmentBook"+File.separator+"exportEnrollmentBookInfo.xls";
				//初始化配置参数
				exportExcelService.initParmasByfile(disFile,"exportEnrollmentBook", enrollmentBookInoList,null);
				exportExcelService.getModelToExcel().setTemplateParam(templateFilepathString, 2, null);
				exportExcelService.getModelToExcel().setRowHeight(400);
				File excelFile = exportExcelService.getExcelFile();//获取导出的文件(将导出文件写到硬盘对应的目录中)
				logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());	
				downloadFile(response, "招生预约报读信息.xls", disFile.getAbsolutePath(),true);
			}
			
		} catch (Exception e) {
			logger.error("导出招生预约报读信息出错", e);
		}
		
	}
	
}
