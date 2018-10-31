package com.hnjk.edu.roll.controller;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hnjk.edu.roll.service.IStudentInfoService;
import com.hnjk.edu.util.Condition2SQLHelper;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
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
import com.hnjk.core.foundation.utils.HttpHeaderUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.FileUploadAndDownloadSupportController;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.Edumanager;
import com.hnjk.edu.basedata.model.Grade;
import com.hnjk.edu.basedata.model.YearInfo;
import com.hnjk.edu.basedata.service.IClassicService;
import com.hnjk.edu.basedata.service.IEdumanagerService;
import com.hnjk.edu.basedata.service.IGradeService;
import com.hnjk.edu.basedata.service.IMajorService;
import com.hnjk.edu.basedata.service.IYearInfoService;
import com.hnjk.edu.roll.model.Classes;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.roll.service.IClassesService;
import com.hnjk.edu.roll.vo.ClaeeseMemo;
import com.hnjk.edu.roll.vo.ClassesVo;
import com.hnjk.edu.teaching.service.IStudentMakeupListService;
import com.hnjk.edu.teaching.service.ITeachingJDBCService;
import com.hnjk.extend.plugin.excel.service.IExportExcelService;
import com.hnjk.platform.system.UserOperationLogsHelper;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.Dictionary;
import com.hnjk.platform.system.model.UserOperationLogs;
import com.hnjk.platform.system.service.IDictionaryService;
import com.hnjk.platform.taglib.JstlCustomFunction;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.OrgUnit;
import com.hnjk.security.model.User;
import com.hnjk.security.service.IOrgUnitService;
/**
 * 班级管理.
 * <code>ClassesController</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2013-4-8 上午09:45:25
 * @see 
 * @version 1.0
 */
@Controller
public class ClassesController extends FileUploadAndDownloadSupportController {

	@Autowired
	@Qualifier("classesService")
	private IClassesService classesService;
	
	@Autowired
	@Qualifier("orgUnitService")
	private IOrgUnitService orgUnitService;
	
	@Autowired
	@Qualifier("yearInfoService")
	private IYearInfoService yearInfoService;
	
	@Autowired
	@Qualifier("gradeService")
	private IGradeService gradeService;
	
	@Autowired
	@Qualifier("majorService")
	private IMajorService majorService;
	
	@Autowired
	@Qualifier("classicService")
	private IClassicService classicService;

	@Autowired
	@Qualifier("studentinfoservice")
	private IStudentInfoService studentInfoService;

	@Autowired
	@Qualifier("edumanagerService")
	private IEdumanagerService edumanagerService;

	@Autowired
	@Qualifier("exportExcelService")
	private IExportExcelService exportExcelService;//Excel导出服务
	
	@Autowired
	@Qualifier("teachingJDBCService")
	private ITeachingJDBCService teachingJDBCService;
	
	@Autowired
	@Qualifier("studentMakeupListService")
	private IStudentMakeupListService studentMakeupListService;
	
	/**
	 * 班级列表
	 * @param classname
	 * @param gradeid
	 * @param majorid
	 * @param classicid
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/roll/classes/list.html")
	public String listClasses(String fromPage,String classname,String classCode ,String brSchoolid, String gradeid, String majorid, String classicid, String teachingType,String hasMemo,
			Page objPage, ModelMap model) throws WebException{
		//objPage.setOrderBy("grade.gradeName desc,brSchool.unitCode,major.majorCode,classic,classname,resourceid");
		objPage.setOrderBy("brSchool.unitCode,grade.gradeName desc,classCode");
		objPage.setOrder(Page.ASC);
		
		User user = SpringSecurityHelper.getCurrentUser();
		if(user.getOrgUnit().getUnitType().equals(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())){//如果为校外学习中心人员
			brSchoolid = user.getOrgUnit().getResourceid();
			model.addAttribute("isBrschool", true);
		}
		
		Map<String, Object> condition = new HashMap<String, Object>();
		if(ExStringUtils.isNotBlank(classname)) {
			condition.put("classname", ExStringUtils.trimToEmpty(classname));
		}
		if(ExStringUtils.isNotBlank(classCode)) {
			condition.put("classCode", ExStringUtils.trimToEmpty(classCode));
		}
		if(ExStringUtils.isNotBlank(brSchoolid)){
			condition.put("brSchoolid", brSchoolid);
		}
		if(ExStringUtils.isNotBlank(gradeid)){
			condition.put("gradeid", gradeid);
		}
		if(ExStringUtils.isNotBlank(majorid)){
			condition.put("majorid", majorid);
		}
		if(ExStringUtils.isNotBlank(classicid)){
			condition.put("classicid", classicid);
		}
		if(ExStringUtils.isNotBlank(teachingType)){
			condition.put("teachingType", teachingType);
		}
		if(ExStringUtils.isNotBlank(hasMemo)){
			condition.put("hasMemo", hasMemo);
		}
		Page classesPage = classesService.findClassesByCondition(condition, objPage);
		model.addAttribute("fromPage", fromPage);
		model.addAttribute("classesPage", classesPage);
		model.addAttribute("condition", condition);
		return "/edu3/roll/classesmanage/classes-list";
	}
	
	 /**
	  * 导出班级信息XLS
	  * @param request
	  * @param response
	  * @throws WebException
	  */
	 @RequestMapping("/edu3/roll/classes/exportXls.html")
	 public void doXlsExort(HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String, Object> condition = new HashMap<String, Object>();

		String classname = ExStringUtils.trimToEmpty(request.getParameter("classname"));//班级名称
		String gradeid 		= ExStringUtils.trimToEmpty(request.getParameter("stuGrade"));//年级
		String brSchoolid 		= ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));//教学站
		String majorid     = ExStringUtils.trimToEmpty(request.getParameter("major"));//专业		
		String classicid    = ExStringUtils.trimToEmpty(request.getParameter("classic"));//层次
		String teachingType 		= ExStringUtils.trimToEmpty(request.getParameter("teachingType"));//办学模式
		String exportType   = ExStringUtils.trimToEmpty(request.getParameter("exportType"));//导出类型
		User user 		    = SpringSecurityHelper.getCurrentUser();					
		if(user.getOrgUnit().getUnitType().equals(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())){//如果为校外学习中心人员
			brSchoolid = user.getOrgUnit().getResourceid();
		}	

		StringBuffer titleStrbuf = new StringBuffer();
		
		if("1".equals(exportType)){//勾选导出
			String classesIds = request.getParameter("selectedid");//学生学籍ID
			classesIds  = ExStringUtils.replace(classesIds, ",", "','");
			classesIds  = "'"+classesIds+"'";
			condition.put("classesIds", classesIds);
		}else{
			if(ExStringUtils.isNotBlank(classname)) {
				try {
					classname = java.net.URLDecoder.decode(classname , "UTF-8");
					condition.put("classname", classname);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			if(ExStringUtils.isNotBlank(brSchoolid)){
				condition.put("brSchoolid", brSchoolid);
			}
			if(ExStringUtils.isNotBlank(gradeid)){
				condition.put("gradeid", gradeid);
			}
			if(ExStringUtils.isNotBlank(majorid)){
				condition.put("majorid", majorid);
			}
			if(ExStringUtils.isNotBlank(classicid)){
				condition.put("classicid", classicid);
			}
			if(ExStringUtils.isNotBlank(teachingType)){
				condition.put("teachingType", teachingType);
			}
		}
			titleStrbuf.append("班级信息表");
			
			GUIDUtils.init();
			setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
			try{
				Page objPage = new Page();
				objPage.setPageSize(1000000);
				objPage.setOrderBy("grade.gradeName desc,brSchool.unitCode,major.majorCode,classic,classname,resourceid");
				objPage.setOrder(Page.DESC);
				List<Classes> classesList = classesService.findClassesByCondition(condition, objPage).getResult();
				List<ClassesVo> list = new ArrayList<ClassesVo>(classesList.size());
				
				for (Classes classes : classesList) {
					ClassesVo classesVo = new ClassesVo();				
					classesVo.setClassname(classes.getClassname());
					classesVo.setGrade(classes.getGrade().getGradeName());
					classesVo.setBrSchool(classes.getBrSchool().getUnitName());
					classesVo.setMajor(classes.getMajor().getMajorName());
					classesVo.setClassic(classes.getClassic().getClassicName());
					classesVo.setTeachingType(JstlCustomFunction.dictionaryCode2Value("CodeTeachingType", classes.getTeachingType()));
					classesVo.setStudentNum(classes.getStudentNum().toString());
					classesVo.setClassesMaster(classes.getClassesMaster());
					list.add(classesVo);
				}
				
				File excelFile  = null;
				File disFile 	= new File(getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(false) + ".xls");
				
				exportExcelService.initParmasByfile(disFile, "ClassesVo", list,null,null);
				exportExcelService.getModelToExcel().setHeader(titleStrbuf.toString());//设置大标题
				exportExcelService.getModelToExcel().setRowHeight(500);//设置行高
				WritableFont font = new WritableFont(WritableFont.ARIAL, 10);
				font.setBoldStyle(WritableFont.BOLD);
		        WritableCellFormat format = new WritableCellFormat(font);
				format.setAlignment(Alignment.CENTRE);
		        format.setBackground(Colour.YELLOW);
		        format.setBorder(Border.ALL, BorderLineStyle.THIN);
				exportExcelService.getModelToExcel().setTitleCellFormat(format);
				exportExcelService.getModelToExcel().setRowHeight(300);
				excelFile 	= exportExcelService.getExcelFile();//获取导出的文件
				
				logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());
				downloadFile(response,titleStrbuf+".xls", excelFile.getAbsolutePath(),true);
				
			}catch (Exception e) {
				logger.error("导出excel文件出错："+e.fillInStackTrace());
				renderHtml(response, "<script>alert('导出excel文件出错:"+e.getMessage()+"')</script>");
			}
			
		} 
	 /**
	 * 新增编辑班级
	 * @param resourceid
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/roll/classes/input.html")
	public String editClasses(String resourceid, ModelMap model) throws WebException{
		Classes classes = null;
		String classCodeRule = CacheAppManager.getSysConfigurationByCode("classCode.Rule").getParamValue();
		if(ExStringUtils.isNotBlank(resourceid)){
			classes = classesService.get(resourceid);
		}else {
			classes = new Classes();
			//获取班号流水号
			if("1".equals(classCodeRule)){
				classes.setClassCode(classesService.getClassCode("",0));
			}
		}
		
		model.addAttribute("classCodeRule",classCodeRule);
		model.addAttribute("classes",classes);
		User user = SpringSecurityHelper.getCurrentUser();
		if(user.getOrgUnit().getUnitType().equals(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())){//如果为校外学习中心人员
			model.addAttribute("isBrschool", true);
			if(ExStringUtils.isBlank(resourceid)){
				classes.setBrSchool(user.getOrgUnit());
			}
		}
		return "/edu3/roll/classesmanage/classes-form";
	}
	
	@RequestMapping("/edu3/roll/classes/getClassCode.html")
	public void getClassCode(HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> map = new HashMap<String, Object>();
		String classCodeRule = request.getParameter("classCodeRule");
		String brSchoolid = request.getParameter("brSchoolid");
		String gradeid = request.getParameter("gradeid");
	
		String prefix = "";//班级编号前缀
		String classCode = "";//返回班级编号
		try {
			int bits = 0;
			if("1".equals(classCodeRule)){
				
			}else if ("2".equals(classCodeRule)) {
				//汕大班号生成规则：年级+教学点编码+N+两位流水号（201801N01）
				OrgUnit orgUnit = orgUnitService.get(brSchoolid);
				Grade grade = gradeService.get(gradeid);
				if(orgUnit==null){
					new Throwable("无法获取教学点信息！");
				}else if (grade==null) {
					new Throwable("无法获取年级信息！");
				}else {
					bits = 2;
					prefix = grade.getYearInfo().getFirstYear()+orgUnit.getUnitCode()+"N";
				}
			}
			classCode = prefix + classesService.getClassCode(prefix,bits);
			map.put("statusCode", 300);
			map.put("message", "获取成功！");
		} catch (Exception e) {
			// TODO: handle exception
			map.put("statusCode", 300);
			map.put("message", e.getMessage());
		}
		map.put("classCode", classCode);
		
		renderJson(response, JsonUtils.mapToJson(map));
	}

	/**
	 * 班主任选择对话框
	 * @param objPage
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/edu3/roll/classes/select-classesmaster.html")
	public String listClassesMaster(Page objPage,HttpServletRequest request,ModelMap model) throws Exception{
		objPage.setOrderBy("unitId");
		objPage.setOrder(Page.ASC);//设置默认排序方式
		//查询条件
		Map<String,Object> condition = new HashMap<String,Object>();
		String unitId=request.getParameter("unitId");//学习中心
		String teachingType=request.getParameter("teachingType");
		String teacherCode=request.getParameter("teacherCode");
		String cnName=request.getParameter("cnName");//姓名
		String classIds = ExStringUtils.trim(request.getParameter("classIds"));
		
		User user = SpringSecurityHelper.getCurrentUser();
		if(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())){//如果为校外学习中心人员
			unitId = user.getOrgUnit().getResourceid();
		}
		
		if(ExStringUtils.isNotEmpty(unitId)) {
			condition.put("unitId", unitId);
		}
		if(ExStringUtils.isNotEmpty(teachingType)) {
			condition.put("teachingType", teachingType);
		}
		if(ExStringUtils.isNotEmpty(teacherCode)) {
			condition.put("teacherCode", teacherCode);
		}
		if(ExStringUtils.isNotEmpty(cnName)) {
			condition.put("cnName", cnName);
		}
		condition.put("isDeleted", 0);
		condition.put("roleCode", CacheAppManager.getSysConfigurationByCode("sysuser.role.master").getParamValue());
		
		Page page = edumanagerService.findEdumanagerByCondition(condition, objPage);		
			
		String idsN=request.getParameter("idsN");
		String namesN=request.getParameter("namesN");
		model.addAttribute("idsN", idsN);
		model.addAttribute("namesN", namesN);
		condition.put("idsN", idsN);
		condition.put("namesN", namesN);
		model.addAttribute("teacherlist", page);
		model.addAttribute("classIds", classIds);
		model.addAttribute("condition", condition);		
		
		return "/edu3/roll/classesmanage/select-classes-master";
	}

	/**
	 * 班长选择对话框
	 * @param objPage
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/edu3/roll/classes/select-classesLeader.html")
	public String listClassesLeader(Page objPage,HttpServletRequest request,ModelMap model) throws Exception{
		objPage.setOrderBy("studyNo");
		objPage.setOrder(Page.ASC);//设置默认排序方式
		//查询条件
		Map<String,Object> condition = Condition2SQLHelper.getConditionFromResquestByIterator(request);
		String classesid = ExStringUtils.trim(request.getParameter("classesid"));
		condition.put("classes.resourceid",classesid);
		Page page = studentInfoService.findByCondition(objPage,condition);
		Classes classes = classesService.get(classesid);
		String idsN = classes.getClassesLeaderId();
		String namesN = classes.getClassesLeader();
		model.addAttribute("idsN", idsN);
		model.addAttribute("namesN", namesN);
		model.addAttribute("studentPage", page);
		model.addAttribute("classesid", classesid);
		model.addAttribute("condition", condition);

		return "/edu3/roll/classesmanage/select-classesLeader";
	}
	
	/**
	 * 保存班级
	 * @param classes
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/roll/classes/save.html")
	public void saveClasses(Classes classes, HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();		
		String classIds = ExStringUtils.trim(request.getParameter("classIds"));
		String classesMasterId = request.getParameter("classesMasterId");
		String classesLeaderId = request.getParameter("classesLeaderId");
		String classesLeaderName = request.getParameter("classesLeaderName");

		try {			
			List<Classes> result = null;
			if(ExStringUtils.isNotEmpty(classes.getResourceid())){
				result = classesService.findByCriteria(Restrictions.eq("classCode", classes.getClassCode()),
						Restrictions.eq("isDeleted", 0),Restrictions.ne("resourceid", classes.getResourceid()));
			}else {
				result = classesService.findByCriteria(Restrictions.eq("classCode", classes.getClassCode()),Restrictions.eq("isDeleted", 0));
			}
			if(result!=null && result.size()>0){
				throw new WebException("该班级编号已经存在！");
			}else if(ExStringUtils.isNotEmpty(classIds)){
				List<Classes> classesList = new ArrayList<Classes>();
				if (ExStringUtils.isNotEmpty(classesMasterId)) {
					Map<String, Object> condition = new HashMap<String, Object>();
					classIds  = ExStringUtils.replace(classIds, ",", "','");
					classIds  = "'"+classIds+"'";
					condition.put("classesIds", classIds);
					Page objPage = new Page();
					objPage.setPageSize(10000);
					objPage.setOrderBy("grade.gradeName desc,brSchool.unitCode,major.majorCode,classic,classname,resourceid");
					objPage.setOrder(Page.DESC);
					classesList = classesService.findClassesByCondition(condition, objPage).getResult();
					Edumanager teacher = edumanagerService.get(classesMasterId);
					for (Classes _class : classesList) {
						_class.setClassesMaster(teacher.getCnName());
						_class.setClassesMasterId(teacher.getResourceid());
						_class.setClassesMasterPhone(teacher.getMobile());
					}
				}
				if (ExStringUtils.isNotBlank(classesLeaderId)) {
					Classes classes_temp = classesService.get(classIds);
					classes_temp.setClassesLeaderId(classesLeaderId);
					classes_temp.setClassesLeader(classesLeaderName);
					classesList.add(classes_temp);
				}
				classesService.batchSaveOrUpdate(classesList);
			}else{
				if(ExStringUtils.isBlank(classes.getClassname())) {
					throw new WebException("班级名称不能为空");
				}
				classes.setClassname(ExStringUtils.trimToEmpty(classes.getClassname()));
				if(ExStringUtils.isNotBlank(classes.getResourceid())){
					Criterion[] criterion1 = {
							Restrictions.eq("classname", classes.getClassname()),
							Restrictions.eq("isDeleted", 0),
							Restrictions.eq("brSchool.resourceid", classes.getBrSchoolid())/*,
							Restrictions.eq("grade.resourceid", classes.getGradeid()),
							Restrictions.eq("major.resourceid", classes.getMajorid()),
							Restrictions.eq("classic.resourceid", classes.getClassicid()),
							Restrictions.eq("teachingType", classes.getTeachingType())*/
					};	
					
					String claname=classesService.get(classes.getResourceid()).getClassname();
				
					List<Classes> list1 = classesService.findByCriteria(criterion1);
					
					if(ExCollectionUtils.isNotEmpty(list1)&&!claname.equals(classes.getClassname())) {
						throw new WebException("同一教学点下，班级名称不能重复！");
					}
					
					Classes preClasses = classesService.get(classes.getResourceid());
					
					preClasses.setClassname(classes.getClassname());
					preClasses.setClassCode(classes.getClassCode());
					preClasses.setClassesMaster(classes.getClassesMaster());
					preClasses.setClassesMasterId(classes.getClassesMasterId());
					preClasses.setClassesMasterPhone(classes.getClassesMasterPhone());
					preClasses.setMemo(classes.getMemo());
					classesService.update(preClasses);
					UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), UserOperationLogs.UPDATE, "3", preClasses);
				} else {
					User user = SpringSecurityHelper.getCurrentUser();
					if(user.getOrgUnit().getUnitType().equals(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())){//如果为校外学习中心人员
						classes.setBrSchool(user.getOrgUnit());
						classes.setBrSchoolid(user.getOrgUnit().getResourceid());
					} else if(ExStringUtils.isNotBlank(classes.getBrSchoolid())){
						classes.setBrSchool(orgUnitService.get(classes.getBrSchoolid()));
					}
					if(ExStringUtils.isNotBlank(classes.getGradeid())){
						classes.setGrade(gradeService.get(classes.getGradeid()));
					}
					if(ExStringUtils.isNotBlank(classes.getMajorid())){
						classes.setMajor(majorService.get(classes.getMajorid()));
					}
					if(ExStringUtils.isNotBlank(classes.getClassicid())){
						classes.setClassic(classicService.get(classes.getClassicid()));
					}
					//System.out.println("1classes="+classes.getClassname());
					//System.out.println("1classesid="+classes.getBrSchoolid());
					Criterion[] criterion = {
							Restrictions.eq("classname", classes.getClassname()),Restrictions.eq("isDeleted", 0),
							Restrictions.eq("brSchool.resourceid", classes.getBrSchoolid())/*,
							Restrictions.eq("grade.resourceid", classes.getGradeid()),
							Restrictions.eq("major.resourceid", classes.getMajorid()),
							Restrictions.eq("classic.resourceid", classes.getClassicid()),
							Restrictions.eq("teachingType", classes.getTeachingType())*/
					};				
					List<Classes> list = classesService.findByCriteria(criterion);
					if(ExCollectionUtils.isNotEmpty(list)) {
						throw new WebException("班级已经存在");
					}
					
					classesService.save(classes);
					UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), UserOperationLogs.INSERT, "3", classes);
				}	
			}
					
			map.put("statusCode", 200);
			map.put("message", "保存成功！");
			map.put("navTabId", "RES_ROLL_CLASSES_INPUT");
			map.put("reloadUrl", request.getContextPath() +"/edu3/roll/classes/input.html?resourceid="+classes.getResourceid());
			
		} catch (Exception e) {
			logger.error("保存班级出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败！"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 删除班级
	 * @param resourceid
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/roll/classes/remove.html")
	public void removeClasses(String resourceid, HttpServletResponse response,HttpServletRequest request) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		try {			
			if(ExStringUtils.isNotBlank(resourceid)){			
				classesService.batchDelete(resourceid.split("\\,"));
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),"3",  UserOperationLogs.DELETE, "删除班级：resourceids"+resourceid);
				map.put("statusCode", 200);
				map.put("message", "删除成功！");		
			}
		} catch (Exception e) {
			logger.error("删除出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错:<br/>"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 默认分班
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/roll/studentinfo/classes/assign.html")
	public void assignStudentClasses(HttpServletRequest request, HttpServletResponse response) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		int statusCode = 200;
		String message ="默认分班执行成功！";
		try {	
			do{
				String brSchoolid = null;
				User user = SpringSecurityHelper.getCurrentUser();
				if(user.getOrgUnit().getUnitType().equals(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())){//如果为校外学习中心人员
					brSchoolid = user.getOrgUnit().getResourceid();
				}
				classesService.assignStudentClasses(brSchoolid);
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "3", UserOperationLogs.UPDATE,"执行默认分班：是否教学点："+brSchoolid==null?"否":"是");
			} while(false);
			
		} catch (Exception e) {
			logger.error("默认分班出错:{}",e.fillInStackTrace());
			statusCode = 300;
			message= "默认分班执行失败！";
		} finally {
			map.put("statusCode", statusCode);
			map.put("message", message);
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 班级选择列表
	 * @param resourceid
	 * @param classname
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/framework/studentinfo/classes/adjust.html")
	public String listStudentClasses(String resourceid,String classname, Page objPage, ModelMap model) throws WebException{
		objPage.setOrderBy("grade.gradeName desc,brSchool.unitCode,major.majorCode,classic,classname,resourceid");
		objPage.setOrder(Page.DESC);
		
		Map<String, Object> condition = new HashMap<String, Object>();
		if(ExStringUtils.isNotBlank(resourceid)){
			condition.put("resourceid", resourceid);
			StudentInfo stu = (StudentInfo) classesService.get(StudentInfo.class, resourceid.split("\\,")[0]);
						
			User user = SpringSecurityHelper.getCurrentUser();
			if(user.getOrgUnit().getUnitType().equals(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())){//如果为校外学习中心人员
				condition.put("brSchoolid", user.getOrgUnit().getResourceid());
			} else {
				condition.put("brSchoolid", stu.getBranchSchool().getResourceid());
			}
			if(ExStringUtils.isNotBlank(classname)) {
				condition.put("classname", ExStringUtils.trimToEmpty(classname));
			}
			condition.put("gradeid", stu.getGrade().getResourceid());
			condition.put("majorid", stu.getMajor().getResourceid());
			condition.put("classicid", stu.getClassic().getResourceid());
			condition.put("teachingType", stu.getTeachingType());
			Page classesPage = classesService.findClassesByCondition(condition, objPage);
			model.addAttribute("classesPage", classesPage);
		}		
		model.addAttribute("condition", condition);
		return "/edu3/roll/classesmanage/student-classes-list";
	}
	/**
	 * 调整班级
	 * @param resourceid
	 * @param classesid
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/roll/studentinfo/classes/adjust.html")
	public void adjustStudentClasses(String resourceid, String classesid, HttpServletResponse response,HttpServletRequest request) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		try {	
			if(ExStringUtils.isNotBlank(resourceid) && ExStringUtils.isNotBlank(classesid)){
				classesService.adjustStudentClasses(resourceid.split("\\,"), classesid);
				map.put("statusCode", 200);
				map.put("message", "调整分班成功！");
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "3",UserOperationLogs.UPDATE, "调整分班：resourceids"+resourceid);
			}
		} catch (Exception e) {
			logger.error("调整分班出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "调整分班失败:<br/>"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 删除分班
	 * @param resourceid
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/roll/studentinfo/classes/remove.html")
	public void removeStudentClasses(String resourceid, HttpServletResponse response, HttpServletRequest request) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		try {		
			if(ExStringUtils.isNotBlank(resourceid)){
				classesService.removeStudentClasses(resourceid.split("\\,"));
				map.put("statusCode", 200);
				map.put("message", "删除分班成功！");
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "3",UserOperationLogs.UPDATE, "删除学生的分班信息：resourceids"+resourceid);
			}			
		} catch (Exception e) {
			logger.error("删除分班出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除分班失败:<br/>"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 选择打印成绩册的条件
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/roll/classes/printReportBookCondition.html")
	public String printReportBookCondition(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws WebException {
		String classIds = ExStringUtils.trim(request.getParameter("classIds"));
		String _gradeFirstYear = ExStringUtils.trim(request.getParameter("gradeFirstYear"));
		int gradeFirstYear = 0;
		if(ExStringUtils.isNotEmpty(_gradeFirstYear)){
			gradeFirstYear = Integer.valueOf(_gradeFirstYear);
			List<YearInfo> yearInfos = yearInfoService.findByHql("from YearInfo y where y.isDeleted=0 and y.firstYear=?", Long.parseLong(_gradeFirstYear));
			model.addAttribute("yearId", yearInfos.get(0).getResourceid());
		}
		
		/*List<Map<String,String>> schoolYearList = new ArrayList<Map<String,String>>();
		for(int i=0;i<3;i++){
			Map<String,String> schoolYearMap = new HashMap<String, String>();
			_gradeFirstYear =String.valueOf(gradeFirstYear+i);
			schoolYearMap.put(_gradeFirstYear,_gradeFirstYear+"年");
			schoolYearList.add(schoolYearMap);
		}*/
		model.addAttribute("gradeFirstYear", gradeFirstYear);
		model.addAttribute("classIds", classIds);
		//model.addAttribute("schoolYearList", schoolYearList);
		
		return "/edu3/roll/classesmanage/printReportBookCondition";
	}
	
	/**
	 * 打印成绩册-预览
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/roll/classes/printReportBook-view.html")
	public String printReportBookView(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws WebException {
		String classIds = ExStringUtils.trim(request.getParameter("classIds"));
		String yearId = ExStringUtils.trim(request.getParameter("yearId"));
		String term = ExStringUtils.trim(request.getParameter("term"));
		String examType = ExStringUtils.trim(request.getParameter("examType"));
		String yid = ExStringUtils.getEncodeURIComponentByOne(yearId);
		model.addAttribute("classIds",classIds);
		model.addAttribute("yearId",yearId);
		model.addAttribute("term",term);
		model.addAttribute("examType",examType);
		
		return "/edu3/roll/classesmanage/printReportBook-view";
	}
	
	/**
	 * 打印成绩册-打印
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/roll/classes/printReportBook.html")
	public void printReportBook(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws WebException {
		String classIds = ExStringUtils.trim(request.getParameter("classIds"));
		String yearId = ExStringUtils.trim(request.getParameter("yearId"));
		String term = ExStringUtils.trim(request.getParameter("term"));
		String examType = ExStringUtils.trim(request.getParameter("examType"));
		JasperPrint jasperPrint = null;
		Map<String, Object> condition = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotEmpty(classIds)){
				classIds = classIds.replaceAll(",", "','");
				condition.put("classIds", classIds);
			}
			String yid = ExStringUtils.getEncodeURIComponentByOne(yearId);
			if (ExStringUtils.isMessyCode(yearId)) {
				yearId = ExStringUtils.getEncodeURIComponentByOne(yearId);
			}
			YearInfo yearInfo = yearInfoService.get(yearId);
			String openTerm = yearInfo.getFirstYear()+"_0"+term;
			// 获取所选班级的有关信息
			List<Map<String,Object>> classInfoList =  classesService.findClassInfo(condition);
			String title = CacheAppManager.getSysConfigurationByCode("print.msg.schoolname").getParamValue();
			String logoPath = CacheAppManager.getSysConfigurationByCode("web.scutlogo.path").getParamValue();
			// 学习形式
			List<Dictionary> teachTypeList = CacheAppManager.getChildren("CodeTeachingType");
			Map<String, String> teachTypeMap = new HashMap<String, String>();
			for(Dictionary d : teachTypeList){
				teachTypeMap.put(d.getDictValue(), d.getDictName());
			}
			//考试类型
			List<Dictionary> examTypeList = CacheAppManager.getChildren("ExamResult");
			Map<String, Object> examTypeMap = new HashMap<String, Object>();
			for (Dictionary d : examTypeList) {
				examTypeMap.put(d.getDictValue(), d.getDictName());
			}
			for(Map<String,Object> classInfo : classInfoList){
				Map<String,Object> param = new HashMap<String, Object>();
				// 数据
				param.put("title", title);
				param.put("logoPath", logoPath);
				param.put("majorName", classInfo.get("majorname"));
				param.put("className", classInfo.get("classesname"));
				String classicAndTeachType = classInfo.get("classicname")+teachTypeMap.get(classInfo.get("teachingtype"));
				param.put("classicAndTeachType", classicAndTeachType);
				param.put("unitName", classInfo.get("unitname"));
				param.put("schoolYear", yearInfo.getFirstYear()+"年");
				param.put("term", "第"+term+"学期"+("N".equals(examType)?"":" "+examTypeMap.get(examType)));
				
				// 根据条件获取所以课程
				condition.clear();
				
				List<Map<String, Object>> courseInfoList = new ArrayList<Map<String,Object>>();
				if("N".equals(examType)){// 正考
					condition.put("openTerm",openTerm);// 开课学期
					condition.put("guiplanid", classInfo.get("guiplanid"));// 年级教学计划
					condition.put("orgunitid", classInfo.get("orgunitid"));// 教学站
					courseInfoList = teachingJDBCService.findOPenedCourseInfo(condition);
				}else {// 补考
					condition.put("term", term);// 学期
					condition.put("yearId", yearId);// 年度
					condition.put("examType", examType);// 考试类型
					condition.put("classesid", classInfo.get("resourceid"));// 班级
					courseInfoList = studentMakeupListService.findCourseByCondition(condition);
				}
				JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(courseInfoList);
				String reportPath = File.separator+"reports"+File.separator+"classes"+File.separator+"printReportBook.jasper";
				String reprotFile = URLDecoder.decode(request.getSession().getServletContext().getRealPath(reportPath),"utf-8");
				
				if (null!=jasperPrint&&jasperPrint.getPages().size()>0) {
					JasperPrint jasperPage = JasperFillManager.fillReport(reprotFile, param, dataSource); // 填充报表
					List jsperPageList=jasperPage.getPages();
					for (int j = 0; j < jsperPageList.size(); j++) {
						jasperPrint.addPage((JRPrintPage) jsperPageList.get(j));
					}
					jasperPage = null;//清除临时报表的内存占用
				}else {
					jasperPrint = JasperFillManager.fillReport(reprotFile, param, dataSource); // 填充报表
				}
				
			}
			// 打印
			if (null!=jasperPrint) {
				renderStream(response, jasperPrint);
			}else {
				renderHtml(response,"缺少打印数据！");
			}
		} catch (UnsupportedEncodingException e) {
			logger.error("编码方式不支持", e);
		} catch (Exception e) {
			logger.error("打印成绩册-打印出错", e);
		}
		
	}
	
	/**
	 * 班级备注信息
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/roll/classes/classesMemoInfo.html")
	public String classesMemoInfo(HttpServletRequest request,ModelMap model){
		String classesid = request.getParameter("classesid");
		if (ExStringUtils.isNotBlank(classesid)) {
			Classes classes = classesService.get(classesid);
			List<Object> memoList = JsonUtils.jsonToBean(classes.getMemo());
			if(memoList!=null && memoList.size()>0){
				model.addAttribute("memoList", memoList);
			}
			model.addAttribute("classes", classes);
			model.addAttribute("date", ExDateUtils.getCurrentDate());
			model.addAttribute("user", SpringSecurityHelper.getCurrentUser());
		}
		return "/edu3/roll/classesmanage/memoInfo";
	}
	
	/**
	 * 保存班级备注
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/roll/classes/saveMemoInfo.html")
	public void saveMemoInfo(HttpServletRequest request,HttpServletResponse response){
		String classesid = request.getParameter("classesid");
		String[] userNames = request.getParameterValues("userName");
		String[] cnNames = request.getParameterValues("cnName");
		String[] memos = request.getParameterValues("memo");
		String[] dates = request.getParameterValues("date");
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			if (ExStringUtils.isNotBlank(classesid)) {
				Classes classes = classesService.get(classesid);
				List<ClaeeseMemo> memoList = new ArrayList<ClaeeseMemo>();
				for (int i=0;i<userNames.length;i++) {
					if(ExStringUtils.isNotBlank4All(userNames[i],memos[i],dates[i])){
						ClaeeseMemo memo = new ClaeeseMemo();
						memo.setUserName(userNames[i]);
						memo.setCnName(cnNames[i]);
						memo.setMemo(memos[i]);
						memo.setDate(dates[i]);
						memoList.add(memo);
					}
				}
				String memoInfo = JsonUtils.listToJson(memoList);
				classes.setMemo(memoInfo);
				classesService.update(classes);
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), "3", UserOperationLogs.UPDATE, "保存班级备注信息："+classesid);
				map.put("statusCode", 200);
				map.put("message", "保存成功！");
			}
		} catch (Exception e) {
			logger.error("保存班级出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败！"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
}
