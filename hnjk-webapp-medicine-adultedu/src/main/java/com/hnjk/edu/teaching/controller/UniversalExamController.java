package com.hnjk.edu.teaching.controller;

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
import org.springframework.web.bind.annotation.RequestMapping;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.GUIDUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.FileUploadAndDownloadSupportController;
import com.hnjk.core.support.context.Constants;
import com.hnjk.core.support.context.SystemContextHolder;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.roll.service.IGraduationQualifService;
import com.hnjk.edu.roll.service.IStudentInfoService;
import com.hnjk.edu.teaching.service.ICourseService;
import com.hnjk.edu.teaching.service.IUniversalExamService;
import com.hnjk.edu.teaching.vo.UniversalExamVO;
import com.hnjk.edu.util.Condition2SQLHelper;
import com.hnjk.extend.plugin.excel.service.IExportExcelService;
import com.hnjk.extend.plugin.excel.service.IImportExcelService;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.Attachs;
import com.hnjk.platform.system.service.IAttachsService;
import com.hnjk.platform.system.service.IDictionaryService;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;

/**
 * 统考成绩表-教学计划内课程
 * <code>UniversalExamController</code>
 * 
 * @author Zik，广东学苑教育发展有限公司
 * @since 2015-7-9 上午 11:01:23
 * @see
 * @version 1.0
 */
@Controller
public class UniversalExamController extends FileUploadAndDownloadSupportController {

	private static final long serialVersionUID = 6062516455650191833L;
	
	@Autowired
	@Qualifier("universalExamService")
	private IUniversalExamService universalExamService;
	
	@Autowired
	@Qualifier("studentinfoservice")
	private IStudentInfoService studentInfoService;
	
	@Autowired
	@Qualifier("graduationQualifService")
	private IGraduationQualifService graduationQualifService;
	
	@Autowired
	@Qualifier("importExcelService")
	private IImportExcelService importExcelService;//excel导入服务
	
	@Autowired
	@Qualifier("attachsService")
	private IAttachsService attachsService;//注入附件服务
	
	@Autowired
	@Qualifier("courseService")
	private ICourseService courseService;
	
	@Autowired
	@Qualifier("exportExcelService")
	private IExportExcelService exportExcelService;
	
	@Autowired
	@Qualifier("dictService")
	private IDictionaryService dictionaryService;
	
	/**
	 * 统考成绩列表
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/universalExam/universalExam-list.html")
	public String listUniversalExam(HttpServletRequest request, HttpServletResponse response, ModelMap model, Page UEpage) 
			throws WebException{
		
		String UESchool = request.getParameter("UESchool"); // 教学点
		try {
			User user = SpringSecurityHelper.getCurrentUser();
			String unitType = CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype")
										.getParamValue();
			if(unitType !=null && unitType.equals(user.getOrgUnit().getUnitType())){
				model.addAttribute("isUEBrSchool", true);// 为教学点工作人员
				model.addAttribute("UESchoolName", user.getOrgUnit().getUnitName());
				UESchool = user.getOrgUnit().getResourceid();
			}
			// 查询条件
			Map<String, Object> condition = Condition2SQLHelper.getConditionFromResquestByIterator(request);
			
			if(ExStringUtils.isNotEmpty(UESchool)){
				condition.put("UESchool", UESchool);
			}
			
//			UEpage = universalExamService.findUniversalExamList(condition, UEpage);
			UEpage = universalExamService.findUniversalExamListByHQL(condition, UEpage);
			model.addAttribute("condition", condition);
			
		} catch (ServiceException e) {
			logger.error("获取统考成绩列表出错", e);
		}
		
		model.addAttribute("universalExamList", UEpage);
				
		return "/edu3/teaching/universalExam/universalExam-list";
	}
	
	/**
	 * 导出统考成绩录入模板
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/universalExam/downloadUEModel.html")
	public void exportUniversalExamModel(HttpServletRequest request, HttpServletResponse response, ModelMap model) 
				throws WebException {
		try{
			setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
			//模板文件路径
			String templateFilepathString = "universalExamInputModel.xls";
			downloadFile(response, "统考成绩录入模版.xls", templateFilepathString,false);
		}catch(Exception e){			
			String msg = "导出excel文件出错：找不到该文件-统考成绩录入模版.xls";
			logger.error("下载统考成绩录入模版出错", e);
			renderHtml(response, "<script>alertMsg.warn("+"\""+msg+"\""+")</script>");
		}
	}
	
	/**
	 * 导入统考成绩对话框
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/teaching/universalExam/importUESCore.html")
	public String importUEScoreDialog(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		User user = SpringSecurityHelper.getCurrentUser();
		String hasAuthority = "N";
		if(SpringSecurityHelper.isUserInRole("ROLE_BRS_score")){// 学习中心成绩管理员
			hasAuthority = "A";
		} else if(SpringSecurityHelper.isUserInRole("ROLE_BRS_STUDENTSTATUS")){// 学习中心教务员
			hasAuthority = "Y";
		}
		model.addAttribute("hasAuthority", hasAuthority);
		model.addAttribute("user", user);
		return "/edu3/teaching/universalExam/importDialog";
	}
	
	/**
	 * 处理导入统考成绩
	 * 
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/universalExam/handle-universalExam-import.html")
	public void handleUEImportScore(HttpServletRequest request,HttpServletResponse response) 
			throws WebException {
		
		StringBuffer message = new StringBuffer("");
		boolean success = true;
		String result = "";
		Map<String, Object > returnMap = new HashMap<String, Object>();
		
		try {
			do {
				String attchID = ExStringUtils.trimToEmpty(request.getParameter("attachId"));
				String UEImportCourseId = ExStringUtils.trimToEmpty(request.getParameter("UEImportCourseId"));
				String examType = ExStringUtils.trimToEmpty(request.getParameter("examType"));
				if(ExStringUtils.isEmpty(examType)){
					success = false;
					message.append("<font color='red'>你没有权限导入统考成绩</font>");
					continue;
				}
				
				User user = SpringSecurityHelper.getCurrentUser();
				Course course = courseService.get(UEImportCourseId);
				if (course != null) {
					if (null != attchID && attchID.split(",").length > 1) {
						success = false;
						message.append("一次只能导入一个成绩文件,谢谢！");
					} else if (null != attchID && attchID.split(",").length == 1) {
						Attachs attachs = attachsService.get(attchID.split(",")[0]);
						String filePath = attachs.getSerPath() + File.separator + attachs.getSerName();
						Map<String, Object> singleMap = universalExamService.analysisUniversalExamFile(filePath, course, user, examType);
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

				} else {
					success = false;
					message.append("该基础课程不存在");
					result = "导入全部失败";
				}
			} while (false);
		} catch (ServiceException e) {
			logger.error("处理导入统考成绩出错", e);
			success = false;
			result = "导入失败";
		} finally {
			returnMap.put("success",success);
			returnMap.put("msg",message);
			returnMap.put("result",result);
			renderJson(response,JsonUtils.mapToJson(returnMap));
		}
	}
	@RequestMapping("/edu3/teaching/universalExam/downloadUniversalExam.html")
	public void exportUniversalExam(HttpServletRequest request, HttpServletResponse response, ModelMap model) 
				throws WebException {
		
		String UESchool = request.getParameter("UESchool"); // 教学点
		String UEGradeid = request.getParameter("UEGradeid"); // 年级
		String UEClassic = request.getParameter("UEClassic"); // 层次
		String UEMajor = request.getParameter("UEMajor"); // 专业
		String UEClassId = request.getParameter("UEClassId"); // 班级		
		String examDate = request.getParameter("examDate"); // 考试日期
		String UECourseId = request.getParameter("UECourseId"); // 课程
		String isPass = request.getParameter("isPass");
		
		try {
			User user = SpringSecurityHelper.getCurrentUser();
			String unitType = CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype")
										.getParamValue();
			if(unitType !=null && unitType.equals(user.getOrgUnit().getUnitType())){
				model.addAttribute("isUEBrSchool", true);// 为教学点工作人员
				model.addAttribute("UESchoolName", user.getOrgUnit().getUnitName());
				UESchool = user.getOrgUnit().getResourceid();
			}
			// 查询条件
			Map<String, Object> condition = new HashMap<String, Object>();
			
			if(ExStringUtils.isNotEmpty(UESchool)){
				condition.put("UESchool", UESchool);
			}
			if(ExStringUtils.isNotEmpty(UEGradeid)){
				condition.put("UEGradeid", UEGradeid);
			}
			if(ExStringUtils.isNotEmpty(UEClassic)){
				condition.put("UEClassic", UEClassic);
			}
			if(ExStringUtils.isNotEmpty(UEMajor)){
				condition.put("UEMajor", UEMajor);
			}
			if(ExStringUtils.isNotEmpty(UEClassId)){
				condition.put("UEClassId", UEClassId);
			}			
			if(ExStringUtils.isNotEmpty(examDate)){
				condition.put("examDate", examDate);
			}
			if(ExStringUtils.isNotEmpty(UECourseId)){
				condition.put("UECourseId", UECourseId);
			}
			if(ExStringUtils.isNotEmpty(isPass)){
				condition.put("isPass", isPass);
			}
			List<UniversalExamVO> list = universalExamService.findUniversalExamVO(condition);
			setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
			File excelFile = null;
			GUIDUtils.init();
			File disFile = new File(getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(false) + ".xls");
			//模板文件路径
			String templateFilepathString = "universalExamExport.xls";
			
			List<String> dictCodeList = new ArrayList<String>();
			dictCodeList.add("yesOrNo");
			exportExcelService.initParmasByfile(disFile, "UniversalExamId", list,dictionaryService.getDictionByMap(dictCodeList, true,IDictionaryService.PREKEY_TYPE_BYCODE));
			exportExcelService.getModelToExcel().setRowHeight(500);//设置行高
			
			exportExcelService.getModelToExcel().setTemplateParam(templateFilepathString, 1,null );
			excelFile = exportExcelService.getExcelFile();//获取导出的文件
			logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());
			downloadFile(response, "统考成绩单.xls", excelFile.getAbsolutePath(),true);
		}catch(Exception e){			
			String msg = "导出excel文件出错：找不到该文件-统考成绩录入模版.xls";
			logger.error("下载统考成绩录入模版出错", e);
			renderHtml(response, "<script>alertMsg.warn("+"\""+msg+"\""+")</script>");
		}
	}

}
