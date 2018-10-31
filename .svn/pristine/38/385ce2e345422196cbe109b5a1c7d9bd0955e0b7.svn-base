package com.hnjk.edu.learning.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
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
import com.hnjk.core.foundation.utils.GUIDUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.BaseSupportController;
import com.hnjk.core.support.base.controller.FileUploadAndDownloadSupportController;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.service.IGradeService;
import com.hnjk.edu.learning.vo.AppUseConditionVo;
import com.hnjk.edu.roll.service.IStudentInfoService;
import com.hnjk.extend.plugin.excel.service.IExportExcelService;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.service.IDictionaryService;
import com.hnjk.platform.system.service.IVersionService;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;
import com.hnjk.security.service.IOrgUnitService;
import com.hnjk.security.service.IUserService;
/**
 * app使用情况
 * <code>BbsController</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-9-21 上午10:22:00
 * @see 
 * @version 1.0
 */
@Controller
public class AppUseConditionController extends BaseSupportController {

	@Autowired
	@Qualifier("studentinfoservice")
	private IStudentInfoService studentInfoService;
	
	@Qualifier("versionService")
	@Autowired
	private IVersionService versionService;
	
	@Qualifier("userService")
	@Autowired
	private IUserService userService;
	
	@Qualifier("gradeService")
	@Autowired
	private IGradeService gradeService;
	
	@Autowired
	@Qualifier("orgUnitService")
	private IOrgUnitService orgUnitService;
	
	@Autowired
	@Qualifier("dictService")
	private IDictionaryService dictionaryService;
	
	@Autowired
	@Qualifier("exportExcelService")
	private IExportExcelService exportExcelService;
	
	/**
	 * app使用情况
	 * @param request
	 * @param model
	 * @param objPage
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/learning/app/useCondition/list.html")
	public String listAppUseCondition(HttpServletRequest request,ModelMap model,Page objPage) throws WebException {
		
		// 参数处理
		String brSchoolId = request.getParameter("brSchoolId");
		String gradeId = request.getParameter("gradeId");
		String classicId = request.getParameter("classicId");
		String teachingType = request.getParameter("teachingType");
		String majorId = request.getParameter("majorId");
		String classesId = request.getParameter("classesId");
		String terminalType = request.getParameter("terminalType");
		String loginVersion = request.getParameter("loginVersion");
		String isUsemobileTerminal = request.getParameter("isUsemobileTerminal");
		String flag = request.getParameter("flag");
		
		User user = SpringSecurityHelper.getCurrentUser();
		if(user.getOrgUnit().getUnitType().indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员
			brSchoolId = user.getOrgUnit().getResourceid();
			model.addAttribute("isBrschool", true);
		}
		Map<String, Object> condition = new HashMap<String, Object>();
		
		if(ExStringUtils.isNotEmpty(brSchoolId)){
			condition.put("brSchoolId", brSchoolId);
		}
		if(ExStringUtils.isNotEmpty(gradeId)){
			condition.put("gradeId", gradeId);
		}else {
			if(ExStringUtils.isEmpty(flag)){
				Calendar a=Calendar.getInstance();
				condition.put("gradeId", gradeService.findUniqueByProperty("gradeName", a.get(Calendar.YEAR)+"级").getResourceid());
			}
		}
		if(ExStringUtils.isNotEmpty(classicId)){
			condition.put("classicId", classicId);
		}
		if(ExStringUtils.isNotEmpty(teachingType)){
			condition.put("teachingType", teachingType);
		}
		if(ExStringUtils.isNotEmpty(majorId)){
			condition.put("majorId", majorId);
		}
		if(ExStringUtils.isNotEmpty(classesId)){
			condition.put("classesId", classesId);
		}
		if(ExStringUtils.isNotEmpty(terminalType)){
			condition.put("terminalType", terminalType);
		}
		if(ExStringUtils.isNotEmpty(loginVersion)){
			condition.put("loginVersion", loginVersion);
		}
		if(ExStringUtils.isNotEmpty(isUsemobileTerminal)){
			condition.put("isUsemobileTerminal", isUsemobileTerminal);
		}
		
		Page appUsePage = userService.findAppUseConditionByJDBC(condition, objPage);
		model.addAttribute("condition", condition);
		model.addAttribute("appUsePage", appUsePage);
		
		return "/edu3/learning/appUse/appUseConditionList";
	}
	
	/**
	 * 导出app使用情况
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/learning/app/useCondition/export.html")
	public void doExcelExport(HttpServletRequest request,HttpServletResponse response,ModelMap map,Page objPage) throws WebException{
		// 参数处理
		String brSchoolId = request.getParameter("brSchoolId");
 		String gradeId = request.getParameter("gradeId");
		String classicId = request.getParameter("classicId");
		String teachingType = request.getParameter("teachingType");
		String majorId = request.getParameter("majorId");
		String classesId = request.getParameter("classesId");
		String terminalType = request.getParameter("terminalType");
		String loginVersion = request.getParameter("loginVersion");
		String isUsemobileTerminal = request.getParameter("isUsemobileTerminal");
		String exportType   = ExStringUtils.trimToEmpty(request.getParameter("exportType"));//导出类型
		Map<String,Object> condition = new HashMap<String, Object>(0);
		
		User curUser          = SpringSecurityHelper.getCurrentUser();
		if(curUser.getOrgUnit().getUnitType().indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员
			brSchoolId = curUser.getOrgUnit().getResourceid();
		}
		if("1".equals(exportType)){//勾选导出
			String ids = ExStringUtils.trimToEmpty(request.getParameter("ids"));//
			condition.put("ids", "'"+ids.replaceAll(",", "','")+"'");
		}else{
			if(ExStringUtils.isNotEmpty(brSchoolId)){
				condition.put("brSchoolId", brSchoolId);
			}
			if(ExStringUtils.isNotEmpty(gradeId)){
				condition.put("gradeId", gradeId);
			}
			if(ExStringUtils.isNotEmpty(classicId)){
				condition.put("classicId", classicId);
			}
			if(ExStringUtils.isNotEmpty(teachingType)){
				condition.put("teachingType", teachingType);
			}
			if(ExStringUtils.isNotEmpty(majorId)){
				condition.put("majorId", majorId);
			}
			if(ExStringUtils.isNotEmpty(classesId)){
				condition.put("classesId", classesId);
			}
			if(ExStringUtils.isNotEmpty(terminalType)){
				condition.put("terminalType", terminalType);
			}
			if(ExStringUtils.isNotEmpty(loginVersion)){
				condition.put("loginVersion", loginVersion);
			}
			if(ExStringUtils.isNotEmpty(isUsemobileTerminal)){
				condition.put("isUsemobileTerminal", isUsemobileTerminal);
			}
		}
		
		List<Map<String, Object>> list = userService.findAppUseConditionByCondition(condition);
		List<AppUseConditionVo> newlist = new ArrayList<AppUseConditionVo>(0);
		
		if(0<list.size()){
			for (Map<String, Object> m : list) {
				AppUseConditionVo appUseCondition = new AppUseConditionVo();
				try {
					appUseCondition.setBrSchoolName(m.get("unitname")!=null?m.get("unitname").toString():"-");
					appUseCondition.setGrade(m.get("gradeName")!=null?m.get("gradeName").toString():"-");
					appUseCondition.setClassic(m.get("classicName")!=null?m.get("classicName").toString():"-");
					appUseCondition.setTeachingType(m.get("teachingType")!=null?m.get("teachingType").toString():"-");
					appUseCondition.setMajor(m.get("majorName")!=null?m.get("majorName").toString():"-");
					appUseCondition.setClasses(m.get("classesname")!=null?m.get("classesname").toString():"-");
					appUseCondition.setStudentName(m.get("studentname")!=null?m.get("studentname").toString():"-");
					appUseCondition.setTerminalType(m.get("terminalType")!=null?m.get("terminalType").toString():"-");
					appUseCondition.setLoginVersion(m.get("loginVersion")!=null?Integer.parseInt(m.get("loginVersion").toString()):null);
					appUseCondition.setIsUsemobileTerminal(m.get("isUsemobileTerminal")!=null?m.get("isUsemobileTerminal").toString():"-");
				
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				newlist.add(appUseCondition);
			}
		}
		
		FileUploadAndDownloadSupportController fileup = new FileUploadAndDownloadSupportController();
		//文件输出服务器路径
		fileup.setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");//存放路径
		try{
			//导出
			File excelFile = null;
			GUIDUtils.init();
			File disFile = new File(fileup.getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(false) + ".xls");
			
			List<String> dictCodeList = new ArrayList<String>();
			dictCodeList.add("CodeLearningStyle");
			dictCodeList.add("terminalType");
			dictCodeList.add("yesOrNo");
			Map<String , Object> mapdict = dictionaryService.getDictionByMap(dictCodeList, true, IDictionaryService.PREKEY_TYPE_BYCODE);
			
			exportExcelService.initParmasByfile(disFile, "appUseConditionVo", newlist,mapdict);
			exportExcelService.getModelToExcel().setHeader("app使用情况统计表");//设置大标题
			exportExcelService.getModelToExcel().setRowHeight(500);//设置行高 
	
			excelFile = exportExcelService.getExcelFile();//获取导出的文件
			logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());
			fileup.downloadFile(response, "app使用情况统计表.xls", excelFile.getAbsolutePath(),true);
			
		}catch(Exception e){
			e.printStackTrace();
			logger.error("导出excel文件出错："+e.fillInStackTrace());
		}
	}
}
