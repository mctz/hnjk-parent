package com.hnjk.edu.teaching.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.utils.ExBeanUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.GUIDUtils;
import com.hnjk.core.foundation.utils.HttpHeaderUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.FileUploadAndDownloadSupportController;
import com.hnjk.core.support.base.model.AttachInfo;
import com.hnjk.core.support.context.Constants;
import com.hnjk.core.support.context.SystemContextHolder;
import com.hnjk.edu.basedata.model.Classic;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.basedata.model.Grade;
import com.hnjk.edu.basedata.model.Major;
import com.hnjk.edu.basedata.service.IClassicService;
import com.hnjk.edu.basedata.service.IGradeService;
import com.hnjk.edu.basedata.service.IMajorService;
import com.hnjk.edu.roll.service.IGraduationQualifService;
import com.hnjk.edu.teaching.model.TeachingGuidePlan;
import com.hnjk.edu.teaching.model.TeachingPlan;
import com.hnjk.edu.teaching.model.TeachingPlanCourse;
import com.hnjk.edu.teaching.service.ICourseService;
import com.hnjk.edu.teaching.service.ILinkageQueryService;
import com.hnjk.edu.teaching.service.ITeachingGuidePlanService;
import com.hnjk.edu.teaching.service.ITeachingJDBCService;
import com.hnjk.edu.teaching.service.ITeachingPlanCourseService;
import com.hnjk.edu.teaching.service.ITeachingPlanCourseStatusService;
import com.hnjk.edu.teaching.service.ITeachingPlanService;
import com.hnjk.edu.teaching.vo.TeachingGradePlanVo;
import com.hnjk.extend.plugin.excel.config.MergeCellsParam;
import com.hnjk.extend.plugin.excel.service.IExportExcelService;
import com.hnjk.extend.plugin.excel.service.IImportExcelService;
import com.hnjk.platform.system.UserOperationLogsHelper;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.UserOperationLogs;
import com.hnjk.platform.taglib.JstlCustomFunction;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.OrgUnit;
import com.hnjk.security.model.Role;
import com.hnjk.security.model.User;
import com.hnjk.security.service.IOrgUnitService;


/**
 * 
 * <code>TeachingGuidePlanController</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2010-6-25 下午04:36:22
 * @see 
 * @version 1.0
 */
@Controller
public class TeachingGuidePlanController  extends FileUploadAndDownloadSupportController{

	private static final long serialVersionUID = 7631378313225945047L;
		
	@Autowired
	@Qualifier("majorService")
	private IMajorService majorService;
	
	@Autowired
	@Qualifier("orgUnitService")
	private IOrgUnitService orgUnitService;
	
	@Autowired
	@Qualifier("importExcelService")
	private IImportExcelService importExcelService;
	
	@Autowired
	@Qualifier("classicService")
	private IClassicService classicService;

	@Autowired
	@Qualifier("graduationQualifService")
	private IGraduationQualifService graduationQualifService;
	
	@Autowired
	@Qualifier("teachingPlanCourseStatusService")
	private ITeachingPlanCourseStatusService teachingPlanCourseStatusService;
	
	@Autowired
	@Qualifier("teachingJDBCService")
	private ITeachingJDBCService teachingJDBCService;

	
	/**
	 * 列表
	 * @param planName
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/teachinggrade/list.html")
	public String exeList(String brSchoolid,String gradeid,String classic,String major,String ispublished,String schoolType,String planName,String isShow, Page objPage, ModelMap model) throws WebException{
		objPage.setOrderBy("gp.resourceid");
		objPage.setOrder(Page.ASC);//设置默认排序方式
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件
		
		User cureentUser = SpringSecurityHelper.getCurrentUser();
		
		if(ExStringUtils.isNotEmpty(gradeid)) {
			condition.put("gradeid", gradeid);
		}
		if(ExStringUtils.isNotEmpty(classic)) {
			condition.put("classic", classic);
		}
		if(ExStringUtils.isNotEmpty(major)) {
			condition.put("major", major);
		}
		if(ExStringUtils.isNotEmpty(ispublished)) {
			condition.put("ispublished", ispublished);
		}
		if(ExStringUtils.isNotEmpty(planName)) {
			condition.put("plan", planName);
		}
		//如果当前角色所属教学模式不为空，则按教学模式查找
		List<Role> roleList = new ArrayList<Role>(cureentUser.getRoles());
		String roleModules = "";
		if(null != roleList && roleList.size()>0){
			for(Role role:roleList){
				if(ExStringUtils.isNotEmpty(role.getRoleModule())){
					roleModules += role.getRoleModule()+",";
				}				
			}
		}
		if(ExStringUtils.isNotEmpty(roleModules)){
			condition.put("roleModules", roleModules.substring(0, roleModules.length()-1));
		} else {
			if(ExStringUtils.isNotEmpty(schoolType)){
				condition.put("schoolType", schoolType);
			}
		}
		User user = SpringSecurityHelper.getCurrentUser();
		if(user.getOrgUnit().getUnitType().equals(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())){//如果为校外学习中心人员
			condition.put("brSchoolid", user.getOrgUnit().getResourceid());
		}
		if(ExStringUtils.isNotEmpty(brSchoolid)) {
			condition.put("brSchoolid", brSchoolid);
		}
		Page guiPlanPage = teachingGuidePlanService.findGuiPlanByCondition(condition, objPage);
		
		List<TeachingPlan> plans = teachingPlanService.findTeachingPlanByCondition(new HashMap<String,Object>());
		StringBuffer planOptions = new StringBuffer("<option value=''></option>");
		for (TeachingPlan plan : plans) {
			planOptions.append("<option value='").append(plan.getResourceid()).append("'");
			if (ExStringUtils.isNotBlank(planName) && planName.equals(plan.getResourceid())) {
				planOptions.append(" selected='selected'");
			}
			planOptions.append(">");
			if (ExStringUtils.isNotBlank(plan.getPlanName())) {
				planOptions.append(plan.getPlanName());
			} else {
				planOptions.append(plan.getMajor().getMajorName()).append("-").append(plan.getClassic().getClassicName()).append("(").append(plan.getVersionNum()).append(")");
			}
			planOptions.append("</option>");
		}
		model.addAttribute("guiPlanPage", guiPlanPage);
		model.addAttribute("planOptions", planOptions);
		model.addAttribute("condition", condition);
		
		return "/edu3/teaching/teachinggrade/teachinggrade-list";
	}
	/**
	 * 导出年级教学计划模版
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/teachinggrade/exportTGradeTemplate.html")
	public void exportTGradeTemplate(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
		try{
			//模板文件路径
			String templateFilepathString = "teachingGradeTemplate.xls";
			downloadFile(response, "导入年级教学计划模版.xls", templateFilepathString,false);
		}catch(Exception e){			
			String msg = "导出excel文件出错：找不到该文件-年级教学计划模版.xls";
			logger.error("导出excel文件出错："+e.fillInStackTrace());
			renderHtml(response, "<script>alertMsg.warn("+"\""+msg+"\""+")</script>");
		}
	}
	/**
	 * 跳转到导入窗口
	 * @param request
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/teachinggrade/inputTGradePlan.html")
	public String inputTGradePlan(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws WebException{
		return "/edu3/teaching/teachinggrade/teachinggrade-input";
	}
	/**
	 * 导入年级教学计划模版
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/teachinggrade/importTGradePlan.html")
	public void importTGradePlan(String exportAct,HttpServletRequest request, HttpServletResponse response) throws WebException {
//		User user = SpringSecurityHelper.getCurrentUser();
//		if(!user.getOrgUnit().getUnitType().equals(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())){//如果为校外学习中心人员
//			throw new WebException("只有教学站有权利导入！");
//		}
//		OrgUnit unit = user.getOrgUnit();
		//提示信息字符串
		String  rendResponseStr = "";
		List<TeachingGradePlanVo> falseList = new ArrayList<TeachingGradePlanVo>();
		File excelFile = null;
		String upLoadurl = "/edu3/teaching/teachinggrade/imputfalseexport.html";
		
		//String imurl = request.getParameter("importFile");
		//设置目标文件路径
		setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH + request.getParameter("importFile"));
		try {
			//上传文件到服务器
			List<AttachInfo> list = doUploadFile(request, response, null);
			AttachInfo attachInfo = list.get(0);
			//创建EXCEL对象 获得待导入的excel的内容
			File excel = new File(attachInfo.getSerPath() + File.separator+ attachInfo.getSerName());
			
			importExcelService.initParmas(excel, "importTGradePlanVo",null);
			importExcelService.getExcelToModel().setSheet(0);// 设置excel sheet 0
			//获得待导入excel内容的List
			List modelList = importExcelService.getModelList();
			if(modelList==null){
				throw new Exception("导入模版错误！");
			}
			//转换为对应类型的List
			List<TeachingGradePlanVo> volist = new ArrayList<TeachingGradePlanVo>();
			TeachingGradePlanVo tGPlan;
			for (int i = 0; i < modelList.size(); i++) {
				tGPlan = (TeachingGradePlanVo) modelList.get(i);
				volist.add(tGPlan);
			}
			//遍历需要导入的List 将对应内容写进DB 并同时检测数据的合法性
			if (null!=volist) {
				GUIDUtils.init();
				Page page;
				Map<String,Object> condition;
				List<OrgUnit> unitList;
				List<Major> majorList;
				List<Grade> gradeList;
				List<Classic> classicList;
				List<Course> courseList;
				for(TeachingGradePlanVo vo : volist){
					String falseMsg = "";
					//0、判断计划名称是否空
					if(ExStringUtils.isBlank(vo.getPlanName())){
						falseMsg = "计划名称不能为空！";
						vo.setErrorMsg(falseMsg);
						falseList.add(vo);
						continue;
					}
					//1、判断是否存在该办学单位
					condition = new HashMap<String,Object>();
					condition.put("unitname", vo.getOrgUnit());
					page = new Page();
					page.setPageSize(1);
					unitList = orgUnitService.findOrgByConditionByHql(condition, page).getResult();
					if(null==unitList || unitList.size()==0){
						falseMsg = "找不到办学单位！";
						vo.setErrorMsg(falseMsg);
						falseList.add(vo);
						continue;
					}
					OrgUnit orgUnit = unitList.get(0);
					//2、判断是否存在该专业
					condition.clear();
					condition.put("majorname", vo.getMajor());
					majorList = majorService.findMajorByCondition(condition);
					if(null==majorList || majorList.size()==0){
						falseMsg = "找不到专业！";
						vo.setErrorMsg(falseMsg);
						falseList.add(vo);
						continue;
					}
					Major major = majorList.get(0);
					//3、判断是否存在该年级
					condition.clear();
					condition.put("gradename", vo.getGrade());
					page = new Page();
					page.setPageSize(1);
					gradeList = gradeService.findGradeByCondition(condition, page).getResult();
					if(null==gradeList || gradeList.size()==0){
						falseMsg = "找不到年级！";
						vo.setErrorMsg(falseMsg);
						falseList.add(vo);
						continue;
					}
					Grade grade = gradeList.get(0);
					//4、判断是否存在该层次
					condition.clear();
					condition.put("classicname", vo.getClassic());
					page = new Page();
					page.setPageSize(1);
					classicList = classicService.findClassicByCondition(condition, page).getResult();
					if(null==classicList || classicList.size()==0){
						falseMsg = "找不到 层次！";
						vo.setErrorMsg(falseMsg);
						falseList.add(vo);
						continue;
					}
					Classic classic = classicList.get(0);
					//5、判断是否存在该课程
					condition.clear();
					condition.put("coursename", vo.getCourseName());
					page = new Page();
					page.setPageSize(1);
					courseList = courseService.findCourseByHql(condition, page).getResult();
					if(null==courseList || courseList.size()==0){
						falseMsg = "找不到 课程！";
						vo.setErrorMsg(falseMsg);
						falseList.add(vo);
						continue;
					}
					Course course = courseList.get(0);
					//6、判断是否存在该办学模式
					String schoolType =  JstlCustomFunction.dictionaryCode2Name("CodeTeachingType", vo.getSchoolType());
					if(ExStringUtils.isBlank(schoolType)){
						falseMsg="找不到办学模式！";
						vo.setErrorMsg(falseMsg);
						falseList.add(vo);
						continue;
					}
					//7、判断是否存在该课程类别
					String courseNature =  JstlCustomFunction.dictionaryCode2Name("courseNature", vo.getCourseNature());
					if(ExStringUtils.isBlank(courseNature)){
						falseMsg="找不到课程类别！";
						vo.setErrorMsg(falseMsg);
						falseList.add(vo);
						continue;
					}
					//8、判断是否存在该课程性质
					String courseType =  JstlCustomFunction.dictionaryCode2Name("CodeCourseType", vo.getCourseType());
					if(ExStringUtils.isBlank(courseType)){
						falseMsg="找不到课程性质！";
						vo.setErrorMsg(falseMsg);
						falseList.add(vo);
						continue;
					}
					//9、判断是否存在该开课学期
					String term =  JstlCustomFunction.dictionaryCode2Name("CodeTermType", vo.getTerm());
					if(ExStringUtils.isBlank(term)){
						falseMsg="找不到开课学期！";
						vo.setErrorMsg(falseMsg);
						falseList.add(vo);
						continue;
					}
					//10、判断是否存在该考试类别
					String examClassType =  JstlCustomFunction.dictionaryCode2Name("CodeExamClassType", vo.getExamClassType());
					if(ExStringUtils.isBlank(examClassType)){
						falseMsg="找不到考试类别！";
						vo.setErrorMsg(falseMsg);
						falseList.add(vo);
						continue;
					}
					//11、判断是否存在该是否主干课
					String isMainCourse =  JstlCustomFunction.dictionaryCode2Name("yesOrNo", vo.getIsMainCourse());
					if(ExStringUtils.isBlank(isMainCourse)){
						falseMsg="找不到是否主干课！";
						vo.setErrorMsg(falseMsg);
						falseList.add(vo);
						continue;
					}
					//12、判断是否存在该是否学位课
					String isDegreeCourse =  JstlCustomFunction.dictionaryCode2Name("yesOrNo", vo.getIsDegreeCourse());
					if(ExStringUtils.isBlank(isMainCourse)){
						falseMsg="找不到是否学位课！";
						vo.setErrorMsg(falseMsg);
						falseList.add(vo);
						continue;
					}
					//13、验证学制
					String eduYear = vo.getEduYear();
					if(ExStringUtils.isBlank(eduYear) || Integer.parseInt(eduYear) <= 0){
						falseMsg="学制必须为大于等于0的数字！";
						vo.setErrorMsg(falseMsg);
						falseList.add(vo);
						continue;
					}
					//14、验证毕业最低学分
					String minResult = vo.getMinResult();
					if(ExStringUtils.isBlank(minResult) || Integer.parseInt(minResult) <= 0){
						falseMsg="毕业最低学分必须为大于等于0的数字！";
						vo.setErrorMsg(falseMsg);
						falseList.add(vo);
						continue;
					}
					//15、验证结业最低学分
					String theGraduationScore = vo.getTheGraduationScore();
					if(ExStringUtils.isBlank(theGraduationScore) || Integer.parseInt(theGraduationScore) <= 0){
						falseMsg="结业最低学分必须为大于等于0的数字！";
						vo.setErrorMsg(falseMsg);
						falseList.add(vo);
						continue;
					}
					//16、验证学分
					String creditHour = vo.getCreditHour();
					if(ExStringUtils.isBlank(creditHour) || Integer.parseInt(creditHour) <= 0){
						falseMsg="学分必须为大于等于0的数字！";
						vo.setErrorMsg(falseMsg);
						falseList.add(vo);
						continue;
					}
					//17、验证总学时
					String stydyHour = vo.getStydyHour();
					if(ExStringUtils.isBlank(stydyHour) || Integer.parseInt(stydyHour) <= 0){
						falseMsg="总学时必须为大于等于0的数字！";
						vo.setErrorMsg(falseMsg);
						falseList.add(vo);
						continue;
					}
					//18、验证面授学时
					String faceStudyHour = vo.getFaceStudyHour();
					if(ExStringUtils.isBlank(faceStudyHour) || Integer.parseInt(faceStudyHour) <= 0){
						falseMsg="面授学时必须为大于等于0的数字！";
						vo.setErrorMsg(falseMsg);
						falseList.add(vo);
						continue;
					}
					condition.clear();
					condition.put("major", major.getResourceid());
					condition.put("classic", classic.getResourceid());
					condition.put("orgUnitId", orgUnit.getResourceid());
					condition.put("grade", grade.getResourceid());
					List<TeachingPlan> planList = teachingPlanService.findTeachingPlanByCondition(condition);
					TeachingPlan plan = null;
					if (planList.size() == 0) {
						plan = new TeachingPlan();
					} else {
						plan = planList.get(0);
					}
					plan.setPlanName(vo.getPlanName());
					plan.setSchoolType(schoolType);
					plan.setOrgUnit(orgUnit);
					plan.setMajor(major);
					plan.setClassic(classic);
					plan.setEduYear(eduYear);
					plan.setMinResult(Double.parseDouble(minResult));
					plan.setTheGraduationScore(Double.parseDouble(theGraduationScore));
					plan.setLearningDescript(ExStringUtils.isNotBlank(vo.getLearningDescript())?vo.getLearningDescript():"");
					teachingPlanService.saveOrUpdate(plan);
					condition.clear();
					condition.put("gradeid", grade.getResourceid());
					condition.put("plan", plan.getResourceid());
					page = new Page();
					page.setPageSize(1);
					List<TeachingGuidePlan> guidePlanList = teachingGuidePlanService.findTeachingGradeByCondition(condition, page).getResult();
					TeachingGuidePlan guidePlan = null;
					if (guidePlanList.size() == 0) {
						guidePlan = new TeachingGuidePlan();
					} else {
						guidePlan = guidePlanList.get(0);
					}
					guidePlan.setGrade(grade);
					guidePlan.setTeachingPlan(plan);
					guidePlan.setIspublished(Constants.BOOLEAN_YES);
					guidePlan.setGenerationTask(Constants.BOOLEAN_NO);
					guidePlan.setIsStatcourse(Constants.BOOLEAN_NO);
					teachingGuidePlanService.saveOrUpdate(guidePlan);
					condition.clear();
					condition.put("teachingPlanId", plan.getResourceid());
					condition.put("courseId", course.getResourceid());
					page = new Page();
					page.setPageSize(1);
					List<TeachingPlanCourse> planCourseList = teachingPlanCourseService.findTeachingPlanCourseByCondition(condition, page).getResult();
					TeachingPlanCourse planCourse = null;
					if (planCourseList.size() == 0) {
						planCourse = new TeachingPlanCourse();
					} else {
						planCourse = planCourseList.get(0);
					}
					planCourse.setTeachingPlan(plan);
					planCourse.setCourse(course);
					planCourse.setCourseNature(courseNature);
					planCourse.setCourseType(courseType);
					planCourse.setTerm(term);
					planCourse.setCreditHour(Double.parseDouble(creditHour));
					planCourse.setStydyHour(Long.parseLong(stydyHour));
					planCourse.setFaceStudyHour(Long.parseLong(faceStudyHour));
					planCourse.setExamClassType(examClassType);
					planCourse.setIsMainCourse(isMainCourse);
					planCourse.setIsDegreeCourse(isDegreeCourse);
					teachingPlanCourseService.saveOrUpdate(planCourse);
				}
			}
			
			//导出缴费信息导入失败的信息以及原因
			if(falseList!=null&&falseList.size()>0){
				setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
				//导出
				GUIDUtils.init();
				String fileName = GUIDUtils.buildMd5GUID(false);
				File disFile = new File(getDistfilepath()+ File.separator + fileName + ".xls");
					
				//模板文件路径
				String templateFilepathString = "teachingGradeTemplateError.xls";
				//初始化配置参数
				exportExcelService.initParmasByfile(disFile,"importTGradePlanErrorVo", falseList,null);
				exportExcelService.getModelToExcel().setTemplateParam(templateFilepathString, 1, null);
				exportExcelService.getModelToExcel().setRowHeight(400);
					
				excelFile = exportExcelService.getExcelFile();//获取导出的文件
				logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());
//					downloadFile(response, "导入缴费信息失败名单.xls", excelFile.getAbsolutePath(),true);
				rendResponseStr = "{statusCode:200,message:'"+"导入成功"+ (volist.size() - falseList.size()) 
				+"条 | 导入失败"+  falseList.size()
				+"条！',forwardUrl:'"+upLoadurl+"?excelFile="+fileName+"'};";
			}
			if(ExStringUtils.isBlank(rendResponseStr)){
				rendResponseStr = "{statusCode:200,message:'"+"导入成功"+ (volist.size() - falseList.size())  
				+"条 | 导入失败"+ falseList.size()
				+"条！',forwardUrl:''};";
			}
		} catch (Exception e) {
			e.printStackTrace();
			rendResponseStr = "{statusCode:300,message:'操作失败!"+e.getMessage()+"'};";
		}
		StringBuffer html = new StringBuffer();
		html.append("<script>");
		html.append("var response = "+rendResponseStr);
		html.append("if(window.parent.donecallback) window.parent.donecallback(response);");
		html.append("</script>");
		renderHtml(response, html.toString());
	}
	/**
	 * 导出失败信息
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/teachinggrade/imputfalseexport.html")
	public void uploadFalseToImport(String excelFile,HttpServletRequest request,HttpServletResponse response) throws Exception {
		setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
		File disFile = new File(getDistfilepath()+ File.separator + excelFile+".xls");
		downloadFile(response, "导入信息失败记录.xls", disFile.getAbsolutePath(),true);
	}
	/**
	 * 修改学习中心更新专业范围
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/teachinggrade/brschool_Major.html")
	public void brschool_Major(HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>(0);
		Map<String,Object> condition = new HashMap<String, Object>(0);
		String majorId = ExStringUtils.trimToEmpty(request.getParameter("majorId"));
		String unitId  = ExStringUtils.trimToEmpty(request.getParameter("unitId"));
		if(ExStringUtils.isNotEmpty(unitId)){
			condition.put("unitId", unitId);
		}
		List<Map<String,Object>> teachingPlanMajors = teachingPlanService.getUnitTeachingPlanMajor(condition);
	
		StringBuffer majorOption = new StringBuffer("<option value=''></option>");
		
		for (Map<String,Object> m : teachingPlanMajors) {
			if(ExStringUtils.isNotEmpty(majorId)&&majorId.equals(m.get("resourceid"))){
				majorOption.append("<option value ='"+m.get("resourceid")+"' selected='selected' >"+m.get("majorinfo")+"</option>");
			}else{
				majorOption.append("<option value ='"+m.get("resourceid")+"'>"+m.get("majorinfo")+"</option>");
			}
			
		}
		map.put("majorOption", majorOption);
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 复制表单
	 * @param resourceid
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/teachinggrade/copy.html")
	public String exeCopy(String resourceid,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		//查询出全部的教学计划（套餐）
		List<TeachingPlan> planList = teachingPlanService.findLatestTeachingPlan(new HashMap<String, Object>());
		model.addAttribute("planList", planList);
		String gradeid = ExStringUtils.trimToEmpty(request.getParameter("gradid"));
		if(ExStringUtils.isNotBlank(gradeid)){ //-----编辑
			//获取年级ID，取出当前年级下已有的教学计划			
			if(ExStringUtils.isNotEmpty(gradeid)){
				List<TeachingGuidePlan> selPlanList = teachingGuidePlanService.findByHql("from "+TeachingGuidePlan.class.getSimpleName()+" where isDeleted=0 and grade.resourceid=? ", gradeid);
				model.addAttribute("selPlanList", selPlanList);
				model.addAttribute("gradeid", gradeid);
			}
			
		}else{ //----------------------------------------新增			
			model.addAttribute("teachingGuidePlan", new TeachingGuidePlan());			
		}
		return "/edu3/teaching/teachinggrade/teachinggrade-copy-form";
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
	@RequestMapping("/edu3/teaching/teachinggrade/edit.html")
	public String exeEdit(String resourceid,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		//查询出全部的教学计划（套餐）
		
		String gradeid = ExStringUtils.trimToEmpty(request.getParameter("gradeid"));
		String classicid = ExStringUtils.trimToEmpty(request.getParameter("classicid"));
		String schoolType = ExStringUtils.trimToEmpty(request.getParameter("teachingType"));
		String majorid = ExStringUtils.trimToEmpty(request.getParameter("majorid"));
		String version = ExStringUtils.trimToEmpty(request.getParameter("version"));
		StringBuffer hql = new StringBuffer("from "+TeachingGuidePlan.class.getSimpleName()+" where isDeleted=0 and grade.resourceid=:gradeid");
		Map<String, Object> values = new HashMap<String, Object>();
		if(ExStringUtils.isNotBlank(gradeid)){ //-----编辑
			//获取年级ID，取出当前年级下已有的教学计划			
			values.put("gradeid", gradeid);
			model.addAttribute("gradeid", gradeid);
			List<TeachingGuidePlan> selPlanList = teachingGuidePlanService.findByHql(hql.toString(),values);
			model.addAttribute("selPlanList", selPlanList);
					
			//TeachingGuidePlan teachingGuidePlan = teachingGuidePlanService.load(resourceid);	
			//model.addAttribute("teachingGuidePlan", teachingGuidePlan);
		}else{ //----------------------------------------新增			
			model.addAttribute("teachingGuidePlan", new TeachingGuidePlan());			
		}
		if(ExStringUtils.isNotBlank(classicid)){
			values.put("classic", classicid);
			model.addAttribute("classicid", classicid);
		}
		if(ExStringUtils.isNotBlank(majorid)){
			values.put("major", majorid);
			model.addAttribute("majorid", majorid);
		}
		if(ExStringUtils.isNotBlank(version)){
			values.put("version", version);
			model.addAttribute("version", version);
		}
		if(ExStringUtils.isNotBlank(schoolType)){
			values.put("schoolType", schoolType);
			model.addAttribute("schoolType", schoolType);
		}
		List<TeachingPlan> planList = teachingPlanService.findTeachingPlanByCondition(values);
		model.addAttribute("planList", planList);
		return "/edu3/teaching/teachinggrade/teachinggrade-form";
	}
	
	/**
	 * 保存更新表单
	 * @param grade
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/teachinggrade/save.html")
	public void exeSave(TeachingGuidePlan teachingGuidePlan,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(teachingGuidePlan.getResourceid())){ //--------------------更新
				TeachingGuidePlan p_teachingGuidePlan = teachingGuidePlanService.load(teachingGuidePlan.getResourceid());
				ExBeanUtils.copyProperties(p_teachingGuidePlan, teachingGuidePlan);
				teachingGradeRelation(p_teachingGuidePlan);
				teachingGuidePlanService.update(p_teachingGuidePlan);
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), "4", UserOperationLogs.UPDATE, "更新年级教学计划:resourceids:"+teachingGuidePlan.getResourceid());
			}else{ //-------------------------------------------------------------------保存
				String[] teachingPlanIds = request.getParameterValues("teachingPlanId");
				//String majorIds[] = request.getParameterValues("majorId");
				//String classicIds[] = request.getParameterValues("classicId");
				//String schoolType[] = request.getParameterValues("schoolType");
				TeachingGuidePlan desTeachingGuidePlan = null;
				if(null != teachingPlanIds){
					List<TeachingGuidePlan> teachingGuidePlan2;
					for (int i = 0; i < teachingPlanIds.length; i++) {
						//如果教学计划已存在该年级中，则跳过
						teachingGuidePlan2 = teachingGuidePlanService.findByHql("from "+TeachingGuidePlan.class.getSimpleName()+" where isDeleted=0 "+
								" and grade.resourceid = ? and teachingPlan.resourceid = ?"
								, teachingGuidePlan.getGradeid(),teachingPlanIds[i]);
						if(null != teachingGuidePlan2 && teachingGuidePlan2.size()>0){
							continue;
						}
						desTeachingGuidePlan = new TeachingGuidePlan();
						ExBeanUtils.copyProperties(desTeachingGuidePlan, teachingGuidePlan);
						
						desTeachingGuidePlan.setTeachingPlanid(teachingPlanIds[i]);
						teachingGradeRelation(desTeachingGuidePlan);
						teachingGuidePlanService.save(desTeachingGuidePlan);
					}
					UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), UserOperationLogs.INSERT, "4", "新增年级教学计划:resourceids:"+teachingPlanIds);
				}
				
			}
			map.put("statusCode", 200);
			map.put("message", "保存成功！");
			map.put("navTabId", "RES_TEACHING_GRADE");
			map.put("reloadUrl", request.getContextPath() +"/edu3/teaching/teachinggrade/edit.html?gradeid="+teachingGuidePlan.getGradeid());
		}catch (Exception e) {
			logger.error("保存出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}

	private void teachingGradeRelation(TeachingGuidePlan teachingGuidePlan) {
		//增加默认学位外语
		String code =CacheAppManager.getSysConfigurationByCode("default.degree.foreignLanguage").getParamValue();
		if(ExStringUtils.isNotBlank(code)){
			List<Course> list = courseService.findByCriteria(Restrictions.eq("isDeleted",0),Restrictions.eq("courseCode", code),Restrictions.eq("isDegreeUnitExam", Constants.BOOLEAN_YES));
			if(list!=null&&list.size()>0){
				teachingGuidePlan.setCourse(list.get(0));
			}
		}
		if(ExStringUtils.isNotEmpty(teachingGuidePlan.getGradeid())){
			Grade grade = gradeService.get(teachingGuidePlan.getGradeid());
			teachingGuidePlan.setGrade(grade);					
		}
		if(ExStringUtils.isNotEmpty(teachingGuidePlan.getTeachingPlanid())){
			TeachingPlan teachingPlan = teachingPlanService.get(teachingGuidePlan.getTeachingPlanid());
			teachingGuidePlan.setTeachingPlan(teachingPlan);				
		}
	}
	
	/**
	 * 发布，取消发布
	 * @param request
	 * @param response
	 * @param model
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/teachinggrade/publish.html")
	public void exePublish(String resourceid,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{	
		Map<String ,Object> map = new HashMap<String, Object>();
		String pubType = ExStringUtils.trimToEmpty(request.getParameter("pubType"));//发布类型
		try {
			if(ExStringUtils.isNotBlank(resourceid)){
				if(resourceid.split("\\,").length >1){//批量					
					for (int i = 0; i < resourceid.split("\\,").length; i++) {
						TeachingGuidePlan plan = teachingGuidePlanService.get(resourceid.split("\\,")[i]);
						if("publish".equals(pubType)){
							plan.setIspublished(Constants.BOOLEAN_YES);
						}else{
							plan.setIspublished(Constants.BOOLEAN_NO);
						}
						teachingGuidePlanService.publishGuideTeachingPlan(plan);
					}
				}else{
					TeachingGuidePlan plan = teachingGuidePlanService.get(resourceid);
					if("publish".equals(pubType)){
						plan.setIspublished(Constants.BOOLEAN_YES);
					}else{
						plan.setIspublished(Constants.BOOLEAN_NO);
					}
					teachingGuidePlanService.publishGuideTeachingPlan(plan);
				}
				map.put("statusCode", 200);
				if("publish".equals(pubType)){
					map.put("message", "发布成功！");	
				}else if("unpublish".equals(pubType)){
					map.put("message", "取消发布成功！");	
				}
				map.put("forward", request.getContextPath()+"/edu3/teaching/teachinggrade/list.html");
			}
			UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "4",UserOperationLogs.PASS, "年级教学计划"+("publish".equals(pubType) ?"发布":"取消发布")+"||resourceids:"+resourceid);
		} catch (Exception e) {
			logger.error("发布年级教学计划出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "发布年级教学计划出错！");
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
	@RequestMapping("/edu3/teaching/teachinggrade/delete.html")
	public void exeDelete(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		String resourceid = request.getParameter("resourceid");
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(resourceid)){
				if(resourceid.split("\\,").length >1){//批量删除					
					teachingGuidePlanService.batchCascadeDelete(resourceid.split("\\,"));
				}else{//单个删除					
					teachingGuidePlanService.delete(resourceid);
				}
				map.put("statusCode", 200);
				map.put("message", "删除成功！");				
				map.put("forward", request.getContextPath()+"/edu3/teaching/teachinggrade/list.html");
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),"4", UserOperationLogs.DELETE,  "删除年级教学计划:resourceids:"+resourceid);
			}
		} catch (Exception e) {
			logger.error("删除出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错:<br/>"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 设置统考课程
	 * @param resourceid
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	/*
	@SuppressWarnings("unchecked")
	@RequestMapping("/edu3/teaching/teachinggrade/statecourse.html")
	public String exeEditStateExamCourse(String resourceid,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		if(ExStringUtils.isNotEmpty(resourceid)){
			TeachingGuidePlan teachingGuidePlan = teachingGuidePlanService.get(resourceid);
			model.addAttribute("teachingGuidePlan", teachingGuidePlan);	
			//查出年级教学计划对应的所有统考课
			List<StateExamCourse> stateExamCourseList = stateExamCourseService.findByHql("from "+StateExamCourse.class.getSimpleName()+" where isDeleted = 0 and teachingGuidePlan.resourceid=?", resourceid);
			model.addAttribute("stateExamCourseList", stateExamCourseList);
			Map<String, String> courseMap = new HashMap<String, String>();
			if(null != stateExamCourseList && !stateExamCourseList.isEmpty()){
				for(StateExamCourse stateExamCourse :stateExamCourseList){
					courseMap.put(stateExamCourse.getTeachingPlanCourse().getResourceid(), stateExamCourse.getStateExamCourse().getResourceid());
				}
			}
			model.addAttribute("courseMap", courseMap);
			//查找出所有统考课程
			List<Course> stateCourseList = courseService.findByHql("from "+Course.class.getSimpleName()+" where isDeleted = 0 and isUniteExam = ?",Constants.BOOLEAN_YES);
			model.addAttribute("stateCourseList", stateCourseList);
		}		
		return "/edu3/teaching/teachinggrade/statecourse-form";
	}
	*/
	
	/**
	 * 保存统考课程对应表
	 * @param resourceid
	 * @param request
	 * @param response
	 * @param model
	 * @throws WebException
	 */
	/*
	@RequestMapping("/edu3/teaching/teachinggrade/statecourse/save.html")
	public void exeSaveStateExamCourse(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		
		String[] teachingPlanCourseId = request.getParameterValues("teachingPlanCourseId");//统考课程对应教学计划ID
		String[] stateCourseIds = request.getParameterValues("stateCourseId");//统考课程ID		
		String teachingGuidePlanId = ExStringUtils.trimToEmpty(request.getParameter("teachingGuidePlanId"));//教学计划课程ID
		
		try {
			TeachingGuidePlan teachingGuidePlan = null;
			if(ExStringUtils.isNotEmpty(teachingGuidePlanId)){
				teachingGuidePlan = teachingGuidePlanService.get(teachingGuidePlanId);
				if(null ==teachingGuidePlan){
					throw new WebException("没有找到对应的年级教学计划!");
				}
			}				
			
			//遍历教学计划课程，更新对应表
			Map<String, String> stateCourseMap = new HashMap<String, String>();
			if(null != teachingPlanCourseId && null != stateCourseIds){
				for (int i = 0; i < teachingPlanCourseId.length; i++) {
					if(ExStringUtils.isNotEmpty(teachingPlanCourseId[i]) && ExStringUtils.isNotEmpty(stateCourseIds[i])){
						stateCourseMap.put(teachingPlanCourseId[i], stateCourseIds[i]);
					}
					
				}
			}
			
			teachingGuidePlanService.updateStateCourse(teachingGuidePlanId, stateCourseMap);
			
			map.put("statusCode", 200);
			map.put("message", "保存成功！");				
			map.put("forward", request.getContextPath()+"/edu3/teaching/teachinggrade/statecourse.html?resourceid="+teachingGuidePlanId);
		} catch (Exception e) {
			logger.error("保存统考课程对应表失败："+e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存统考课程出错:<br/>"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	*/
	/**
	 * 导出教学计划
	 * @param teachingGuidePlanId
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/teachinggrade/export.html")
	public void exportTeachingGuidePlan(String teachingGuidePlanId, HttpServletResponse response) throws WebException {
		String teachingGuidePlanName = "";//教学计划名称
		Double totalCreditHour = 0.0;//总学分
		String descript = "";//说明
		String modelNameType = "1";
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();//数据
		List<MergeCellsParam> mergeCellsParams = new ArrayList<MergeCellsParam>();//合并坐标
		int startRow = 4;//导出起始行
		
		if(ExStringUtils.isNotBlank(teachingGuidePlanId) ){
			TeachingGuidePlan teachingGuidePlan = teachingGuidePlanService.get(teachingGuidePlanId);
			
			teachingGuidePlanName =  teachingGuidePlan.getGrade().getGradeName()+" - "+teachingGuidePlan.getTeachingPlan().getMajor().getMajorName()+"（"+teachingGuidePlan.getTeachingPlan().getClassic().getClassicName()+"）";
			
			List<TeachingPlanCourse> planCourses = new ArrayList<TeachingPlanCourse>(teachingGuidePlan.getTeachingPlan().getTeachingPlanCourses());
			Collections.sort(planCourses, new Comparator<TeachingPlanCourse>() {
				@Override
				public int compare(TeachingPlanCourse o1, TeachingPlanCourse o2) {
					return o1.getCourseType().compareTo(o2.getCourseType());
				}
			});
			
			String courseType = null;
			int start = 0;
			int right = 17;
			for (int i = 0; i < planCourses.size(); i++) {
				if(planCourses.get(i).getTerm()!=null && Integer.parseInt(planCourses.get(i).getTerm())>5){
					modelNameType = "2";
					right = 20;
					break;
				}
			}
			for (int i = 0; i < planCourses.size(); i++) {
				TeachingPlanCourse planCourse = planCourses.get(i);
				if (planCourse.getCreditHour() != null) {
					totalCreditHour += planCourse.getCreditHour();
				}
								
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("courseType", JstlCustomFunction.dictionaryCode2Value("CodeCourseType", planCourse.getCourseType()));
				map.put("courseName", planCourse.getCourse().getCourseName());
				map.put("courseCode",ExStringUtils.trimToEmpty(planCourse.getCourse().getCourseCode()));
				map.put("courseNature",JstlCustomFunction.dictionaryCode2Value("courseNature", planCourse.getCourseNature()));
				map.put("isDegreeCourse", JstlCustomFunction.dictionaryCode2Value("yesOrNo",planCourse.getIsDegreeCourse()));
				map.put("examType", JstlCustomFunction.dictionaryCode2Value("CodeExamClassType",planCourse.getExamClassType()));
				if (planCourse.getFaceStudyHour() != null) {
					map.put("faceStudyHour", ExStringUtils.trimToEmpty(planCourse.getFaceStudyHour().toString()));
				}
				
				map.put("isMainCourse",Constants.BOOLEAN_YES.equals(ExStringUtils.trimToEmpty(planCourse.getIsMainCourse()))?"是":"");;
				map.put("teachType",JstlCustomFunction.dictionaryCode2Value("teachType", planCourse.getTeachType()));
				map.put("stydyHour", planCourse.getStydyHour());
				map.put("creditHour", planCourse.getCreditHour());
				map.put("term"+planCourse.getTerm(), "√");
				list.add(map);
				
				if(!planCourse.getCourseType().equals(courseType) && courseType!=null){
					if(i-start>1){
						mergeCellsParams.add(new MergeCellsParam(0,start+startRow,0,i+startRow-1));
						mergeCellsParams.add(new MergeCellsParam(right,start+startRow,right,i+startRow-1));
					}									
					start = i;
				}	
				if(i==planCourses.size()-1 && i-start>0){
					mergeCellsParams.add(new MergeCellsParam(0,start+startRow,0,i+startRow));
					mergeCellsParams.add(new MergeCellsParam(right,start+startRow,right,i+startRow));
				}
				courseType = planCourse.getCourseType();
			}
			
		}
		
		//文件输出服务器路径
		setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
		try{
			//导出
			File excelFile = null;
			GUIDUtils.init();
			File disFile = new File(getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(false) + ".xls");
			Map<String,Object> templateMap = new HashMap<String, Object>();//设置模板
			templateMap.put("teachingGuidePlanName", teachingGuidePlanName);
			templateMap.put("totalCreditHour",totalCreditHour);
			templateMap.put("descript",descript);
			//模板文件路径
			String templateFilepathString = "teachingGuidePlan"+modelNameType+".xls";
			
			//初始化配置参数
			
			exportExcelService.initParamsByfile(disFile, "teachingGuidePlan"+modelNameType, list,null,null,mergeCellsParams);
			exportExcelService.getModelToExcel().setTemplateParam(templateFilepathString, startRow, templateMap);
			exportExcelService.getModelToExcel().setRowHeight(360);
			
			excelFile = exportExcelService.getExcelFile();//获取导出的文件

			logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());
			downloadFile(response, teachingGuidePlanName+".xls", excelFile.getAbsolutePath(),true);
		}catch(Exception e){			
			logger.error("导出excel文件出错："+e.fillInStackTrace());
		}
	}
	
	
	/**
	 * 根据更改层次确定年度教学计划范围
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/teaching/teachingGuidePlan/getTeachingGuidePlan.html")
	public void findTeachingGuidePlanByClassicId(HttpServletRequest request,HttpServletResponse response){
		
		String classic = ExStringUtils.trimToEmpty(request.getParameter("classic")); 
		String gradeId = ExStringUtils.trimToEmpty(request.getParameter("gradeId"));
		String major   = ExStringUtils.trimToEmpty(request.getParameter("major"));
		//取自于原教学计划的办学模式
		String schoolType   = ExStringUtils.trimToEmpty(request.getParameter("schooltype"));
		List<TeachingGuidePlan> teachingGuidePlanList=new ArrayList<TeachingGuidePlan>(0);
		Map<String,Object> condition = new HashMap<String, Object>(0);
		if (ExStringUtils.isNotEmpty(classic)) {
			condition.put("classic", classic) ;
		}
		if (ExStringUtils.isNotEmpty(gradeId)) {
			condition.put("gradeId", gradeId) ;
		}
		if (ExStringUtils.isNotEmpty(major)) {
			condition.put("major", major) ;
		}
		if (ExStringUtils.isNotEmpty(schoolType)) {
			condition.put("schoolType", schoolType) ;
		}
		if(condition.containsKey("classic")&&condition.containsKey("gradeId")&&condition.containsKey("major")){
			teachingGuidePlanList = teachingGuidePlanService.findByHql(" from "+TeachingGuidePlan.class.getSimpleName()+" where isDeleted = 0 and grade.resourceid = '"+condition.get("gradeId")+"' "
		+ " and teachingPlan.classic.resourceid = '"+condition.get("classic")+"' "
		+ " and teachingPlan.major.resourceid = '"+condition.get("major")+"' "
		+ " and teachingPlan.schoolType = '"+ condition.get("schoolType")+"' ");
		}
		List<Map<String,String>> resultList =new ArrayList<Map<String,String>>(0);
		
		for (TeachingGuidePlan teachingGuidePlan : teachingGuidePlanList) {
			Map<String,String> result = new HashMap<String, String>(0);
			TeachingPlan tmp = teachingGuidePlan.getTeachingPlan();
			result.put("id",teachingGuidePlan.getResourceid());
			String unit = ExStringUtils.isBlank(tmp.getOrgUnit().getUnitShortName())?"通用":tmp.getOrgUnit().getUnitShortName();
			result.put("value",JstlCustomFunction.dictionaryCode2Value("CodeTeachingType", tmp.getSchoolType())+"_"+tmp.getClassic().getClassicName()+"_"+tmp.getMajor().getMajorName()+"_"+teachingGuidePlan.getGrade().getGradeName()+"_"+unit);
			resultList.add(result);
		}
		renderJson(response, JsonUtils.listToJson(resultList));
	}
	
	/**
	 * 按教学计划开课
	 * @param reqest
	 * @param response
	 * @param model
	 */
	@RequestMapping("/edu3/teaching/teachinggrade/openCourseBySuggestTerm.html")
	public void openCourseBySuggestTerm(HttpServletRequest reqest, HttpServletResponse response, ModelMap model)  {
		Map<String, Object> map = new HashMap<String, Object>();
		String schoolId = ExStringUtils.trimToEmpty(reqest.getParameter("schoolId"));
		String guiplanIds = ExStringUtils.trimToEmpty(reqest.getParameter("guiplanIds"));
	    // 是否直接开课
		String isDerectOpen = "N";
		int resultCode = 200;
		String msg = "按教学计划开课成功！";
		try {
			do{
				if(ExStringUtils.isEmpty(guiplanIds)){
					resultCode = 300;
					msg = "请选择一条记录！";
					break;
				}
				guiplanIds = "'"+guiplanIds.replaceAll(",", "','")+"'";
				User user = SpringSecurityHelper.getCurrentUser();
				// 如果是某个教学点的工作人员，则执行以下逻辑
				if(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())) {
					schoolId = user.getOrgUnit().getResourceid();
					isDerectOpen = "Y";
				} else {
					if(ExStringUtils.isEmpty(schoolId)){
						break;
					}
				}
				if(ExStringUtils.isEmpty(schoolId)){
					resultCode = 300;
					msg = "教学点不能为空！";
				}
				OrgUnit orgUnit = orgUnitService.get(schoolId);
				// 处理按教学计划建议学期开课逻辑
				// 根据年级教学计划获取要开课的课程信息
				List<Map<String, Object>> openCourseInfoList = teachingJDBCService.findOpenCourseInfoByGuiplanId(guiplanIds);
				// 处理自动开课逻辑
				Map<String,String> returnMap = teachingPlanCourseStatusService.handleAutoOpenCourses(openCourseInfoList,user,orgUnit);
				if(returnMap != null && returnMap.size() >0){
					resultCode = Integer.valueOf(returnMap.get("returnCode"));
					if(resultCode==200) {
						msg = "按教学计划开课成功！";
						String _message = returnMap.get("_message");
						if(ExStringUtils.isNotBlank(_message)){
							msg +="<br/><font color='red'>请在数据字典添加以下上课学期：</font><br/>"+_message;
						}
					} else {
						msg = "按教学计划开课失败！";
					}
				}
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(reqest), "4",UserOperationLogs.INSERT,  "按教学计划开课：年级教学计划resourceids:"+guiplanIds+"||教学点resourceid:"+schoolId);
			}while(false);
		} catch (Exception e) {
			resultCode = 300;
			msg = "按教学计划开课失败！";
			logger.error("按教学计划建议学期开课出错", e);
		} finally {
			map.put("resultCode", resultCode);
			map.put("isDerectOpen", isDerectOpen);
			map.put("msg", msg);
		}
		
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 按教学计划开课，选择教学点页面（不是具体教学点工作人员）
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/teachinggrade/openCourse-selectSchool.html")
	public String selectSchool(HttpServletRequest request,HttpServletResponse response, ModelMap model) throws WebException {
		String guiplanIds = ExStringUtils.trimToEmpty(request.getParameter("guiplanIds"));
		String selectSchool = ExStringUtils.trimToEmpty(request.getParameter("selectSchool"));
		
		model.addAttribute("guiplanIds", guiplanIds);
		model.addAttribute("selectSchool", selectSchool);
		return "/edu3/teaching/teachinggrade/openCourse-selectSchool";
	}
	/**
	 * 设置/修改教学计划的学位外语
	 */
	@RequestMapping("/edu3/teaching/teachingplan/select-setDegreeForeignLanguage.html")
	public String setDegreeForeignLanguage_select(String resourceid,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		StringBuffer select  = new StringBuffer("<select  id='setsetDegreeForeignLanguageid'  name='setDegreeForeignLanguage' style='width:50%;'>");		
		select.append("<option value=''>---请选择---</option>");
		List<Course> courses = courseService.findByCriteria(Restrictions.eq("isDeleted", 0),Restrictions.eq("isDegreeUnitExam", Constants.BOOLEAN_YES));
		for(Course c :courses){
			select.append("<option value='"+c.getResourceid()+"'>"+c.getCourseName()+"</option>");
		}
		select.append("</select>");
		model.addAttribute("resourceid",resourceid);
		model.addAttribute("selectOption",select.toString());
		return "/edu3/teaching/teachingplan/select-setDegreeForeignLanguage";
	}
	@RequestMapping("/edu3/teaching/teachingplan/setDegreeForeignLanguage.html")
	public void setDegreeForeignLanguage(String resourceid,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		Map<String, Object> map = 	new HashMap<String, Object>();
		String [] res = resourceid.split("\\,");
		int statusCode = 200;
		String message = "设置外语课程";
		List<TeachingGuidePlan> tgpList = new ArrayList<TeachingGuidePlan>();
		
		String courseid = ExStringUtils.trimToEmpty(request.getParameter("courseid"));
		if(ExStringUtils.isNotBlank(courseid)){			
			Course course = courseService.get(courseid);
			map.put("courseName",course.getCourseName());
			if(ExStringUtils.isNotBlank(resourceid)){
				for(String id :res){
					TeachingGuidePlan tgp=	teachingGuidePlanService.get(id);					
					tgp.setCourse(course);
					tgpList.add(tgp);
				}				
			}
		}
		try {
			teachingGuidePlanService.batchSaveOrUpdate(tgpList);			
			message += "成功";
		} catch (ServiceException e) {
			
			e.printStackTrace();
		}
		map.put("statusCode", statusCode);
		map.put("message", message);		
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	
	/**
	 * 按教学计划开课列表
	 * @param planName
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/teachinggrade/openCourseList.html")
	public String gplanOpenCourseList(String brSchoolid,String gradeid,String classic,String major,String ispublished,String schoolType,String planName,String isShow, Page objPage, ModelMap model) throws WebException{
		objPage.setOrderBy("gp.resourceid");
		objPage.setOrder(Page.ASC);//设置默认排序方式
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件
		
		User cureentUser = SpringSecurityHelper.getCurrentUser();
		
		if(ExStringUtils.isNotEmpty(gradeid)) {
			condition.put("gradeid", gradeid);
		}
		if(ExStringUtils.isNotEmpty(classic)) {
			condition.put("classic", classic);
		}
		if(ExStringUtils.isNotEmpty(major)) {
			condition.put("major", major);
		}
		if(ExStringUtils.isNotEmpty(ispublished)) {
			condition.put("ispublished", ispublished);
		}
		if(ExStringUtils.isNotEmpty(planName)) {
			condition.put("plan", planName);
		}
		//如果当前角色所属教学模式不为空，则按教学模式查找
		List<Role> roleList = new ArrayList<Role>(cureentUser.getRoles());
		String roleModules = "";
		if(null != roleList && roleList.size()>0){
			for(Role role:roleList){
				if(ExStringUtils.isNotEmpty(role.getRoleModule())){
					roleModules += role.getRoleModule()+",";
				}				
			}
		}
		if(ExStringUtils.isNotEmpty(roleModules)){
			condition.put("roleModules", roleModules.substring(0, roleModules.length()-1));
		} else {
			if(ExStringUtils.isNotEmpty(schoolType)){
				condition.put("schoolType", schoolType);
			}
		}
		User user = SpringSecurityHelper.getCurrentUser();
		if(user.getOrgUnit().getUnitType().equals(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())){//如果为校外学习中心人员
			brSchoolid = user.getOrgUnit().getResourceid();
			condition.put("brSchoolid", brSchoolid);
			model.addAttribute("defaultSchoolId", brSchoolid);
		}
		if(ExStringUtils.isNotEmpty(brSchoolid)) {
			condition.put("brSchoolid", brSchoolid);
		}
		Page guiPlanPage = teachingGuidePlanService.findTeachingGradeWithUnitByCondition(condition, objPage);
		
		List<TeachingPlan> plans = teachingPlanService.findTeachingPlanByCondition(new HashMap<String,Object>());
		StringBuffer planOptions = new StringBuffer("<option value=''></option>");
		for (TeachingPlan plan : plans) {
			planOptions.append("<option value='").append(plan.getResourceid()).append("'");
			if (ExStringUtils.isNotBlank(planName) && planName.equals(plan.getResourceid())) {
				planOptions.append(" selected='selected'");
			}
			planOptions.append(">");
			if (ExStringUtils.isNotBlank(plan.getPlanName())) {
				planOptions.append(plan.getPlanName());
			} else {
				planOptions.append(plan.getMajor().getMajorName()).append("-").append(plan.getClassic().getClassicName()).append("(").append(plan.getVersionNum()).append(")");
			}
			planOptions.append("</option>");
		}
		model.addAttribute("OCPage", guiPlanPage);
		model.addAttribute("planOptions", planOptions);
		model.addAttribute("condition", condition);
		
		return "/edu3/teaching/teachinggrade/openCourseList";
	}
	
	@Autowired
	@Qualifier("courseService")
	private ICourseService courseService;//课程服务接口
	
//	@Autowired
//	@Qualifier("stateExamCourseService")
//	private IStateExamCourseService stateExamCourseService;//统考课程设置接口
		
	@Autowired
	@Qualifier("teachingguideplanservice")
	private ITeachingGuidePlanService teachingGuidePlanService;
	
	@Autowired
	@Qualifier("gradeService")
	private IGradeService gradeService;
	
	@Autowired
	@Qualifier("teachingplanservice")
	private ITeachingPlanService teachingPlanService;	
	
	@Autowired
	@Qualifier("teachingPlanCourseService")
	private ITeachingPlanCourseService teachingPlanCourseService;	
	
	@Autowired
	@Qualifier("exportExcelService")
	private IExportExcelService exportExcelService;
	
}
