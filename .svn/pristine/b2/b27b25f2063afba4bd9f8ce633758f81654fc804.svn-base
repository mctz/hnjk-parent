package com.hnjk.edu.roll.controller;

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

import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.GUIDUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.FileUploadAndDownloadSupportController;
import com.hnjk.core.support.context.Constants;
import com.hnjk.core.support.context.SystemContextHolder;
import com.hnjk.edu.roll.model.GraduateData;
import com.hnjk.edu.roll.service.IGraduateDataService;
import com.hnjk.extend.plugin.excel.service.IExportExcelService;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.service.IDictionaryService;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;

/**
 * <code>GradeFeeRuleController</code><p>
 * 毕业生数据上报
 * @author： 广东学苑教育发展有限公司
 * @since： 2011-4-12 下午13:34:32
 * @see 
 * @version 1.0
 */
@Controller
public class GraduationStudentReportController extends FileUploadAndDownloadSupportController{

	private static final long serialVersionUID = -6840249610144999783L;

	@Autowired
	@Qualifier("graduatedataservice")
	private IGraduateDataService graduateDataService;
	
	@Autowired
	@Qualifier("exportExcelService")
	private IExportExcelService exportExcelService;//Excel导出服务
	
	@Autowired
	@Qualifier("dictService")
	private IDictionaryService dictionaryService;// 数据字典
	
	/**
	 * 毕业生数据导出
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/schoolroll/graduation/student/report.html")
	public  void exeGraduationStudentList(HttpServletRequest request,HttpServletResponse response,Page objPage,ModelMap model) throws WebException
	{
		//查询条件
		Map<String,Object> condition = new HashMap<String,Object>();
		String branchSchool=request.getParameter("branchSchool");//学习中心
		String major=request.getParameter("major");//专业
		String classic=request.getParameter("classic");//层次
		String stuStatus=request.getParameter("stuStatus");//学籍状态
		String name=request.getParameter("name");//姓名
		String studyNo=request.getParameter("studyNo");//学号  
		String grade=request.getParameter("grade");//年级
		
		if(ExStringUtils.isNotEmpty(branchSchool)) {
			condition.put("branchSchool", branchSchool);
		}
		if(ExStringUtils.isNotEmpty(major)) {
			condition.put("major", major);
		}
		if(ExStringUtils.isNotEmpty(classic)) {
			condition.put("classic", classic);
		}
		if(ExStringUtils.isNotEmpty(stuStatus)) {
			condition.put("stuStatus", stuStatus);
		}
		if(ExStringUtils.isNotEmpty(name)) {
			condition.put("name", name);
		}
		if(ExStringUtils.isNotEmpty(studyNo)) {
			condition.put("studyNo", studyNo);
		}
		if(ExStringUtils.isNotEmpty(grade)) {
			condition.put("grade", grade);
		}
		//是教学中心的人员操作的话，只能看到自己教学中心的毕业学生
		String center = "show";
		User cureentUser = SpringSecurityHelper.getCurrentUser();
		if(cureentUser.getOrgUnit().getUnitType().
				indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){
			branchSchool = cureentUser.getOrgUnit().getResourceid();
			center = "hide";
		}
		
		
//		List<GraduateData> list = graduateDataService.getAll();
		List<GraduateData> list = graduateDataService.findByHql(condition);
		//文件输出服务器路径
		setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
		try{
			//导出
			File excelFile = null;
			GUIDUtils.init();
			File disFile = new File(getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(false) + "reportStudent.xls");
				Map<String,Object> templateMap = new HashMap<String, Object>();//设置模板
				
				//模板文件路径
				String templateFilepathString = "reportStudent.xls";
				//结果集中需要用到的数据字典 
				List<String> dictCodeList = new ArrayList<String>();
				dictCodeList.add("CodeSex");// 性别code
//				dictCodeList.add("CodePolitics");// 政治面貌code
				dictCodeList.add("CodeNation");//民族代码
				dictCodeList.add("CodeLearningStyle");//学习形式
				dictCodeList.add("CodeGraduateType");//毕业结业论
				dictCodeList.add("CodeStudentStatus");//学籍状态
				dictCodeList.add("CodeMajorClass");//专业类别
				
				//初始化配置参数
				exportExcelService.initParmasByfile(disFile, "reportStudent", list,dictionaryService.getDictionByMap(dictCodeList, true,IDictionaryService.PREKEY_TYPE_BYCODE));
				exportExcelService.getModelToExcel().setRowHeight(500);//设置行高
				exportExcelService.getModelToExcel().setTemplateParam(templateFilepathString, 2, templateMap);
				excelFile = exportExcelService.getExcelFile();//获取导出的文件
			logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());
			downloadFile(response, "毕业生数据.xls", excelFile.getAbsolutePath(),true);
		}catch(Exception e){
			logger.error("导出excel文件出错："+e.fillInStackTrace());
			e.printStackTrace();
		}
	}


	/**
	 * 毕业生数据列表
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/schoolroll/graduation/student/reportList.html")
	public String exeGraduationStudentList(HttpServletRequest request,Page objPage,ModelMap model) throws WebException
	{
		objPage.setOrderBy("graduateDate");
		objPage.setOrder(Page.ASC);//设置默认排序方式
		
		//查询条件
		Map<String,Object> condition = new HashMap<String,Object>();
		String branchSchool=request.getParameter("branchSchool");//学习中心
		String major=request.getParameter("major");//专业
		String classic=request.getParameter("classic");//层次
		String stuStatus=request.getParameter("stuStatus");//学籍状态
		String name=request.getParameter("name");//姓名
		String studyNo=request.getParameter("studyNo");//学号  
		String grade=request.getParameter("grade");//学号
		if(ExStringUtils.isNotEmpty(branchSchool)) {
			condition.put("branchSchool", branchSchool);
		}
		if(ExStringUtils.isNotEmpty(major)) {
			condition.put("major", major);
		}
		if(ExStringUtils.isNotEmpty(classic)) {
			condition.put("classic", classic);
		}
		if(ExStringUtils.isNotEmpty(stuStatus)) {
			condition.put("stuStatus", stuStatus);
		}
		if(ExStringUtils.isNotEmpty(name)) {
			condition.put("name", name);
		}
		if(ExStringUtils.isNotEmpty(studyNo)) {
			condition.put("studyNo", studyNo);
		}
		if(ExStringUtils.isNotEmpty(grade)) {
			condition.put("grade", grade);
		}
		//是教学中心的人员操作的话，只能看到自己教学中心的毕业学生
		String center = "show";
		User cureentUser = SpringSecurityHelper.getCurrentUser();
		if(cureentUser.getOrgUnit().getUnitType().
				indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){
			branchSchool = cureentUser.getOrgUnit().getResourceid();
			center = "hide";
		}
		Page page = graduateDataService.findGraduateDataByCondition(condition, objPage);

		model.addAttribute("graduationStudentList", page);
		model.addAttribute("condition", condition);
		model.addAttribute("showCenter", center);

		return "/edu3/roll/graduationStudentReport/report-list";
	}
}
