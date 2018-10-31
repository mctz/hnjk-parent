package com.hnjk.edu.roll.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hnjk.platform.system.model.Attachs;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WriteException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.base.JRBasePrintPage;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.utils.ExBeanUtils;
import com.hnjk.core.foundation.utils.ExCollectionUtils;
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.GUIDUtils;
import com.hnjk.core.foundation.utils.HttpHeaderUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.controller.FileUploadAndDownloadSupportController;
import com.hnjk.core.support.base.model.AttachInfo;
import com.hnjk.core.support.context.Constants;
import com.hnjk.core.support.context.SystemContextHolder;
import com.hnjk.edu.basedata.model.Major;
import com.hnjk.edu.basedata.model.PersonalRalation;
import com.hnjk.edu.basedata.model.StudentBaseInfo;
import com.hnjk.edu.recruit.util.ReplaceStr;
import com.hnjk.edu.roll.model.GraduateData;
import com.hnjk.edu.roll.model.GraduateDateInfoVo;
import com.hnjk.edu.roll.model.Reginfo;
import com.hnjk.edu.roll.model.StuChangeInfo;
import com.hnjk.edu.roll.model.StudentGraduateAndDegreeAudit;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.roll.service.IGraduateDataService;
import com.hnjk.edu.roll.service.IGraduationQualifService;
import com.hnjk.edu.roll.service.IGraduationStatJDBCService;
import com.hnjk.edu.roll.service.IReginfoService;
import com.hnjk.edu.roll.service.IRollJDBCService;
import com.hnjk.edu.roll.service.IStuChangeInfoService;
import com.hnjk.edu.roll.service.IStudentGraduateAndDegreeAuditService;
import com.hnjk.edu.roll.service.IStudentInfoChangeJDBCService;
import com.hnjk.edu.roll.service.IStudentInfoService;
import com.hnjk.edu.roll.util.Tools;
import com.hnjk.edu.roll.vo.ExamResultsInfoVO;
import com.hnjk.edu.roll.vo.StudentInfoVo;
import com.hnjk.edu.teaching.model.StateExamResults;
import com.hnjk.edu.teaching.service.IExamResultsService;
import com.hnjk.edu.teaching.service.IStateExamResultsService;
import com.hnjk.edu.teaching.service.ITeachingJDBCService;
import com.hnjk.edu.teaching.vo.DegreeApplySummaryVO;
import com.hnjk.edu.teaching.vo.DegreeCourseExamVO;
import com.hnjk.edu.util.Condition2SQLHelper;
import com.hnjk.extend.plugin.excel.config.MergeCellsParam;
import com.hnjk.extend.plugin.excel.service.IExportExcelService;
import com.hnjk.extend.plugin.excel.service.IImportExcelService;
import com.hnjk.platform.system.UserOperationLogsHelper;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.Dictionary;
import com.hnjk.platform.system.model.UserOperationLogs;
import com.hnjk.platform.system.service.IAttachsService;
import com.hnjk.platform.system.service.IDictionaryService;
import com.hnjk.platform.taglib.JstlCustomFunction;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;
import com.hnjk.security.service.IUserService;

/**
 * <code>GradeFeeRuleController</code><p>
 * 毕业学员管理
 * @author： 广东学苑教育发展有限公司
 * @since： 2011-3-21 下午17:34:32
 * @see
 * @version 1.0
 */
@Controller
public class GraduationStudentController extends FileUploadAndDownloadSupportController{

	private static final long serialVersionUID = 5553342573626290807L;

	@Autowired
	@Qualifier("studentinfoservice")
	private IStudentInfoService studentInfoService;

	@Autowired
	@Qualifier("graduatedataservice")
	private IGraduateDataService graduateDataService;

	@Autowired
	@Qualifier("reginfoservice")
	private IReginfoService reginfoservice;

	@Autowired
	@Qualifier("stuchangeinfoservice")
	private IStuChangeInfoService stuChangeInfoService;

	@Autowired
	@Qualifier("studentInfoChangeJDBCService")
	private IStudentInfoChangeJDBCService studentInfoChangeJDBCService;

	@Autowired
	@Qualifier("graduationStatJDBCService")
	private IGraduationStatJDBCService graduationStatJDBCService;

	@Autowired
	@Qualifier("exportExcelService")
	private IExportExcelService exportExcelService;

	@Autowired
	@Qualifier("studentGraduateAndDegreeAuditService")
	private IStudentGraduateAndDegreeAuditService studentGraduateAndDegreeAuditService;

	@Autowired
	@Qualifier("dictService")
	private IDictionaryService dictionaryService;

	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;

	@Autowired
	@Qualifier("graduationQualifService")
	private IGraduationQualifService graduationQualifService;

	@Autowired
	@Qualifier("rollJDBCService")
	private IRollJDBCService rollJDBCService;

	@Autowired
	@Qualifier("teachingJDBCService")
	private ITeachingJDBCService teachingJDBCService;

	@Autowired
	@Qualifier("userService")
	private IUserService userService;

	@Autowired
	@Qualifier("attachsService")
	private IAttachsService attachsService;

	@Autowired
	@Qualifier("importExcelService")
	private IImportExcelService importExcelService;
	
	@Autowired
	@Qualifier("stateExamResultsService")
	private IStateExamResultsService stateExamResultsService;
	
	@Autowired
	@Qualifier("examResultsService")
	private IExamResultsService examResultsService;
	

	/**
	 * 毕业生库——查看
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/schoolroll/graduation/student/list.html")
	public String exeGraduationStudentList(HttpServletRequest request,Page objPage,ModelMap model) throws WebException
	{
		objPage.setOrderBy("studentInfo.branchSchool.unitCode,studentInfo.classic.classicCode,studentInfo.major.majorCode,studentInfo.studyNo");
		objPage.setOrder(Page.ASC);//设置默认排序方式

		//查询条件
		Map<String,Object> condition = Condition2SQLHelper.getConditionFromResquestByIterator(request);
		String branchSchool=request.getParameter("branchSchool");//学习中心
		
		model.addAttribute("schoolCode" ,CacheAppManager.getSysConfigurationByCode("graduateData.schoolCode").getParamValue());
		//是教学中心的人员操作的话，只能看到自己教学中心的毕业学生
		String center = "show";
		User cureentUser = SpringSecurityHelper.getCurrentUser();
		if(cureentUser.getOrgUnit().getUnitType().
				indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){
			branchSchool = cureentUser.getOrgUnit().getResourceid();
			condition.put("branchSchool", branchSchool);
			center = "hide";
		}
		Page page = graduateDataService.findGraduateDataByCondition(condition, objPage);
		List<GraduateData> list = page.getResult();
		List<String> studentInfoResourceids = studentInfoChangeJDBCService.getAllStudentInfoId();
		List<String> studentInfoResourceidsWithoutBaseInfo =studentInfoChangeJDBCService.getStudentInfoIdWithoutBaseInfo();
		for (GraduateData tmp : list) {
			StudentInfo studentInfo = tmp.getStudentInfo();
			String resourceid = studentInfo.getResourceid();
			if(studentInfoResourceids.contains(resourceid)){
				tmp.setHasStuChange(true);
			}
			if (studentInfoResourceidsWithoutBaseInfo.contains(resourceid)) {
				tmp.setHasBaseInfo(false);
			}else{
				tmp.setHasBaseInfo(true);
			}
		}
		//获得是否使用了全选
		String checkall = ExStringUtils.trimToEmpty(request.getParameter("checkAllFlag_dA"));//全选
		if(null!=checkall&&"1".equals(checkall)) {
			condition.put("checkAllFlag_dA", "1");
		}else{
			condition.put("checkAllFlag_dA", "0");
		}
		model.addAttribute("graduationStudentList", page);
		model.addAttribute("condition", condition);
		model.addAttribute("showCenter", center);
		List<String> gDates = new ArrayList<String>(0);
		gDates = graduationStatJDBCService.getSingleDistinctPropertyValue("TO_CHAR(GRADUATEDATE,'YYYY-MM-DD')","TO_CHAR(GRADUATEDATE,'YYYY-MM-DD')");
		model.addAttribute("gDates", gDates);
		return "/edu3/roll/graduationStudent/graduationData-list";
	}


	/**
	 * 选择已通过毕业审核的学员
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/roll/graduation/student/selectForExecute.html")
	public String selectGraduationStudentList(HttpServletRequest request,Page objPage,ModelMap model)throws WebException{
		objPage.setOrderBy("resourceid");
		objPage.setOrder(Page.ASC);//设置默认排序方式
		//查询条件
		Map<String,Object> condition = new HashMap<String,Object>();
		//16为毕业  11为在学
		condition.put("stuStatus", "16");
		condition.put("graduationStudentFilter", true);
		Page page = studentInfoService.findStuByCondition(condition, objPage);
		model.addAttribute("selectGraduationStudentList", page);
		model.addAttribute("condition", condition);
		return "/edu3/roll/graduationStudent/selectGraduationStudent-list";
	}


	/**
	 * 添加，编辑毕业学生信息准备
	 * @param request
	 * @param response
	 * @param resourceid
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/roll/graduation/student/beforeAdd.html")
	public String beforeAdd(HttpServletRequest request,HttpServletResponse response,String resourceid,String mark,ModelMap model)throws WebException{
		if(ExStringUtils.isNotEmpty(resourceid)&&"add".equals(mark)){//添加
			System.out.println(" 添加    resourceid    "+resourceid);
			StudentInfo studentInfo=	studentInfoService.get(resourceid);
			//获取当前选种学员的基本信息
			GraduateData graduateData = new GraduateData();
			graduateData.setStudentInfo(studentInfo);
			//获取注册时间
			Reginfo reginfo=reginfoservice.findUniqueByProperty("studentInfo.resourceid", resourceid);
			graduateData.setEntranceDate(reginfo.getRegDate());
			model.put("graduateData", graduateData);
		}
		else if(ExStringUtils.isNotEmpty(resourceid)){//修改
			GraduateData graduateData=graduateDataService.get(resourceid);
			//获取注册时间
			Reginfo reginfo=reginfoservice.findUniqueByProperty("studentInfo.resourceid", graduateData.getStudentInfo().getResourceid());
			graduateData.setEntranceDate(reginfo.getRegDate());
			model.put("graduateData", graduateData);
		}
		return "/edu3/roll/graduationStudent/graduationData-form";
	}

	/**
	 * 毕业人员的添加，编辑
	 * @param graduateData 毕业人员表对象
	 * @param request
	 * @param response
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/roll/graduation/student/addOrEdit.html")
	public void addOrEdit(GraduateData graduateData,String studentId ,HttpServletRequest request,HttpServletResponse response)throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		try{

			if(ExStringUtils.isNotEmpty(graduateData.getResourceid())){
				StudentInfo studentInfo = studentInfoService.get(studentId);
				graduateData.setStudentInfo(studentInfo);
				graduateDataService.update(graduateData);
				map.put("statusCode", 200);
				map.put("message", "修改成功！");
				map.put("navTabId", "RES_GRADUATE_DATA");
				map.put("reloadUrl", request.getContextPath() +"/edu3/roll/graduation/student/beforeAdd.html?resourceid="+graduateData.getResourceid());
			}else{
				StudentInfo studentInfo = studentInfoService.get(studentId);
				graduateData.setStudentInfo(studentInfo);
				if(graduateDataService.isExist(studentInfo.getStudyNo())){
					graduateDataService.save(graduateData);
					map.put("statusCode", 200);
					map.put("message", "保存成功！");
					map.put("navTabId", "RES_GRADUATE_DATA");
					map.put("reloadUrl", request.getContextPath() +"/edu3/roll/graduation/student/beforeAdd.html?resourceid="+graduateData.getResourceid());
				}else{
					map.put("statusCode", 200);
					map.put("message", "存在重复记录！");
					map.put("navTabId", "RES_GRADUATE_DATA");
				}

			}
		}catch(Exception e){
			e.printStackTrace();
			map.put("statusCode", 300);
			map.put("message", "操作失败！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}

	/**
	 * 验证选择学生毕业信息的发布状态  #未使用#
	 * @param request
	 * @param response
	 * @param model
	 * @throws WebException
	 */
	/*
	@RequestMapping("/edu3/roll/graduation/student/validatePublishStatus.html")
	public void validatePublishStatus(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		String stus =  ExStringUtils.trimToEmpty(request.getParameter("stus"));
		String[] studentResourceids = stus.split(",");
		Map<String,Object> result = new HashMap<String, Object>(0);
		result.put("isLegal", true);
		result.put("isLegal_Degree", true);
		for (int i = 0; i < studentResourceids.length; i++) {
			GraduateData graduateDataForValidate = graduateDataService.findUniqueByProperty("resourceid", studentResourceids[i]);
			if("Y".equals(graduateDataForValidate.getPublishStatus())){
				result.put("isLegal", false);
			}
			//先撤销学位 才能删除毕业信息
			String degreeStatus = graduateDataForValidate.getDegreeStatus();
			if("Y".equals(degreeStatus)){
				result.put("isLegal_Degree", false);
			}
		}
		renderJson(response,JsonUtils.mapToJson(result));
	}
	*/
	/**
	 * 毕业人员单个、批量删除
	 * @param resourceid
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/roll/graduation/student/delete.html")
	public void delete(String resourceid,HttpServletRequest request,HttpServletResponse response){
	    Map<String,Object> map = new HashMap<String, Object>(0);
	    try {
	    	if(ExStringUtils.isNotEmpty(resourceid)){
	    		String[] resids = resourceid.split(",");
	    		for (int i = 0; i < resids.length; i++) {//毕业生库的回退 修改毕业生库 修改学籍表 修改毕业审核信息
	    			GraduateData tmp_graduateData = graduateDataService.get(resids[i]);
	    			StudentInfo tmp_studentInfo = tmp_graduateData.getStudentInfo() ;
	    			tmp_graduateData.setCourseAvg("");
	    			tmp_graduateData.setGraduationthesisscore("");
	    			List<StudentGraduateAndDegreeAudit> audits =
							studentGraduateAndDegreeAuditService.findByHql(" from "+StudentGraduateAndDegreeAudit.class.getSimpleName()+" where isDeleted=0 and studentInfo.resourceid=? ",tmp_studentInfo.getResourceid() );
					if(audits.size()<1){
						//学籍状态退回为"在读"
		    			tmp_studentInfo.setStudentStatus("11");
			    		//帐号状态开放
			    		tmp_studentInfo.setAccountStatus(Constants.BOOLEAN_TRUE);
			    		//用户表帐号状态
			    		if(null != tmp_studentInfo.getSysUser()){
			    			tmp_studentInfo.getSysUser().setEnabled(true);
			    		}
		    			studentInfoService.update(tmp_studentInfo);
		    			tmp_graduateData.setStudentInfo(tmp_studentInfo);
		    			//学位状态退回为"空"
		    			tmp_graduateData.setDegreeStatus(null);
		    			//毕业生库标识为删除
		    			tmp_graduateData.setIsDeleted(1);
		    			//撤销毕业时，将是否允许结业换毕业设置为初始状态
		    			tmp_graduateData.setIsAllowSecGraduate(Constants.BOOLEAN_MOVE);
		    			graduateDataService.saveOrUpdate(tmp_graduateData);
					}else{
						StudentGraduateAndDegreeAudit audit = audits.get(0);
						if(null!=audit){
			    			String[] strs = null == audit.getStatus() ? null : audit.getStatus().split("##");
			    			if(null != strs && strs.length == 3){//正常数据
			    				String str0 = strs[0];
			    				String str1 = strs[1];
			    				//String str2 = strs[2];
			    				String str2 = "true";

			    				if(ExStringUtils.isBlank(str0) || "null".equals(str0)){
			    					tmp_studentInfo.setStudentStatus("11");
			    				}else{
			    					tmp_studentInfo.setStudentStatus(strs[0]);
			    				}
			    				if(ExStringUtils.isBlank(str1) || "null".equals(str1)){
			    					tmp_studentInfo.setAccountStatus(Constants.BOOLEAN_TRUE);
			    				}else{
			    					tmp_studentInfo.setAccountStatus(("1".equals(ExStringUtils.trim(str1))?Constants.BOOLEAN_TRUE:Constants.BOOLEAN_FALSE));
			    				}
			    				if(ExStringUtils.isBlank(str2) || "null".equals(str2)){
			    					if(null != tmp_studentInfo.getSysUser()){
			    						tmp_studentInfo.getSysUser().setEnabled(true);
			    					}
			    				}else{
			    					if(null != tmp_studentInfo.getSysUser()){
			    						tmp_studentInfo.getSysUser().setEnabled("true".equals(ExStringUtils.trim(str2))?true:false);
			    					}
			    				}
			    			}else{
			    				//学籍为在学
			    				tmp_studentInfo.setStudentStatus("11");
			    				//帐号状态开放
				    			tmp_studentInfo.setAccountStatus(Constants.BOOLEAN_TRUE);
				    			//用户表帐号状态
				    			if(null != tmp_studentInfo.getSysUser()){
				    				tmp_studentInfo.getSysUser().setEnabled(true);
		    					}
			    			}
			    			studentInfoService.update(tmp_studentInfo);
			    			tmp_graduateData.setStudentInfo(tmp_studentInfo);
		//	    			学位状态退回为"空"
			    			tmp_graduateData.setDegreeStatus(null);
		//	    			毕业生库标识为删除
			    			if(!"24".equals(strs[0])){
			    				tmp_graduateData.setIsDeleted(1);
			    			}else {//结业换毕业撤销
			    				tmp_graduateData.setGraduateType("2");
							}
			    			//撤销毕业时，将是否允许结业换毕业设置为初始状态
			    			tmp_graduateData.setIsAllowSecGraduate(Constants.BOOLEAN_MOVE);
			    			graduateDataService.saveOrUpdate(tmp_graduateData);
			    			audit.setGraduateData(tmp_graduateData);
//							if(null!=audit.getDegreeAuditMemo()){
//									audit.setDegreeAuditMemo(audit.getDegreeAuditMemo().replace("1","0"));//审核数据标记为【未确认学位】
//							}
							audit.setComfirm("0");//设置为未确认
							studentGraduateAndDegreeAuditService.saveOrUpdate(audit);
							//新需求删除记录
							if(!"24".equals(strs[0])){
								studentGraduateAndDegreeAuditService.truncate(audit);
							}
						}
					}
				}
	    		UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "3", UserOperationLogs.REPEAL,"撤销毕业：resourceids"+resourceid);
	    		map.put("statusCode", 200);
	    		map.put("message", "操作成功");
	    	}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("statusCode", 300);
			map.put("message", "操作失败！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
//		Map<String,Object> map = new HashMap<String, Object>();
//		try{
//			if(ExStringUtils.isNotEmpty(resourceid)){
//				graduateDataService.batchCascadeDelete(resourceid.split(","));
//				map.put("statusCode", 200);
//				map.put("message", "删除成功！");
//				map.put("navTabId", "RES_GRADUATE_DATA");
//				map.put("reloadUrl", request.getContextPath() +"/edu3/schoolroll/graduation/student/list.html");
//			}
//		}catch(Exception e){
//			e.printStackTrace();
//			e.printStackTrace();
//			map.put("statusCode", 300);
//			map.put("message", "操作失败！");
//		}
//
//		renderJson(response, JsonUtils.mapToJson(map));
	}

	/**
	 * 毕业人员单个、批量发布毕业状态
	 * @param resourceid
	 * @param request
	 * @param response
	 */
	/*
	@RequestMapping("/edu3/roll/graduation/student/publish.html")
	public void publish(String resourceid,HttpServletRequest request,HttpServletResponse response){
	    Map<String,Object> map = new HashMap<String, Object>(0);
	    try {
	    	if(ExStringUtils.isNotEmpty(resourceid)){
	    		String[] resids = resourceid.split(",");
	    		for (int i = 0; i < resids.length; i++) {
	    			GraduateData tmp_graduateData = graduateDataService.get(resids[i]);
	    			StudentInfo tmp_studentInfo = tmp_graduateData.getStudentInfo() ;
//	    			学籍状态改为"毕业"
	    			tmp_studentInfo.setStudentStatus("16");
	    			studentInfoService.update(tmp_studentInfo);
	    			tmp_graduateData.setStudentInfo(tmp_studentInfo);
//	    			毕业生库标识为已发布
	    			tmp_graduateData.setPublishStatus(Constants.BOOLEAN_YES);
	    			graduateDataService.saveOrUpdate(tmp_graduateData);
				}
	    		map.put("statusCode", 200);
	    		map.put("message", "发布成功");
	    	}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("statusCode", 300);
			map.put("message", "发布失败！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	*/
	/**
	 * 毕业人员单个、批量撤销发布毕业状态
	 * @param resourceid
	 * @param request
	 * @param response
	 */
	/*
	@RequestMapping("/edu3/roll/graduation/student/undoPublish.html")
	public void undoPublish(String resourceid,HttpServletRequest request,HttpServletResponse response){
	    Map<String,Object> map = new HashMap<String, Object>(0);
	    try {
	    	if(ExStringUtils.isNotEmpty(resourceid)){
	    		String[] resids = resourceid.split(",");
	    		for (int i = 0; i < resids.length; i++) {
	    			GraduateData tmp_graduateData = graduateDataService.get(resids[i]);
	    			StudentInfo tmp_studentInfo = tmp_graduateData.getStudentInfo() ;
//	    			学籍状态改为"准毕业"
	    			tmp_studentInfo.setStudentStatus("22");
	    			studentInfoService.update(tmp_studentInfo);
	    			tmp_graduateData.setStudentInfo(tmp_studentInfo);
//	    			毕业生库标识为未发布
	    			tmp_graduateData.setPublishStatus(Constants.BOOLEAN_WAIT);
	    			graduateDataService.saveOrUpdate(tmp_graduateData);
				}
	    		map.put("statusCode", 200);
	    		map.put("message", "撤销发布成功");
	    	}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("statusCode", 300);
			map.put("message", "撤销发布失败！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	*/
	/**
	 * 毕业人员单个、批量撤销学位获得状态
	 * @param resourceid
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/roll/graduation/student/undoDegree.html")
	public void undoDegree(String resourceid,HttpServletRequest request,HttpServletResponse response,ModelMap model){
	    Map<String,Object> map = new HashMap<String, Object>(0);
	    StringBuffer messageStr = new StringBuffer();
	    try {
	    	if(ExStringUtils.isNotEmpty(resourceid)){
	    		String[] resids = resourceid.split(",");
	    		for (int i = 0; i < resids.length; i++) {
	    			GraduateData tmp_graduateData = graduateDataService.get(resids[i]);
	    			StudentInfo tmp_studentInfo = tmp_graduateData.getStudentInfo() ;
	    			//如果是专科生
	    			if("3".equals(tmp_studentInfo.getClassic().getClassicCode())){
	    				messageStr.append(tmp_studentInfo.getStudentName()+"是专科生，无法撤销学位状态\n");
	    				continue;
	    			}
//	    			学位状态改为"待审核"
	    			tmp_graduateData.setDegreeStatus(Constants.BOOLEAN_WAIT);
	    			tmp_graduateData.setStudentInfo(tmp_studentInfo);
//	    			毕业生库标识为未发布
	    			//tmp_graduateData.setPublishStatus(Constants.BOOLEAN_WAIT);
	    			graduateDataService.saveOrUpdate(tmp_graduateData);
	    			//修改审核表
	    			List<StudentGraduateAndDegreeAudit> audits =
							studentGraduateAndDegreeAuditService.findByHql(" from "+StudentGraduateAndDegreeAudit.class.getSimpleName()+" where studentInfo.resourceid=? ",tmp_studentInfo.getResourceid() );
	    			StudentGraduateAndDegreeAudit audit = audits.get(0);
	    			audit.setGraduateData(tmp_graduateData);
	    			audit.setDegreeAuditMemo(audit.getDegreeAuditMemo().replace("#1", "#0"));//审核数据标记为【未确认】
	    			studentGraduateAndDegreeAuditService.update(audit);
				}
	    		map.put("statusCode", 200);
	    		messageStr.append("撤销发布成功");
	    		map.put("message", messageStr.toString());
	    	}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("statusCode", 300);
			map.put("message", "撤销发布失败！");
		}
		model.put("isflush", true);
		renderJson(response, JsonUtils.mapToJson(map));
	}

	/**
	 * 撤销学位
	 * @param resourceid
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping("/edu3/roll/graduation/student/degree_delete.html")
	public void delDegree(String resourceid,HttpServletRequest request,HttpServletResponse response,ModelMap model){
	    Map<String,Object> map = new HashMap<String, Object>(0);
	    StringBuffer messageStr = new StringBuffer();
	    try {
	    	if(ExStringUtils.isNotEmpty(resourceid)){
	    		String[] resids = resourceid.split(",");
	    		for (int i = 0; i < resids.length; i++) {
	    			GraduateData tmp_graduateData = graduateDataService.get(resids[i]);
	    			StudentInfo tmp_studentInfo = tmp_graduateData.getStudentInfo() ;
	    			//如果是专科生
	    			if(!"16".equals(tmp_studentInfo.getStudentStatus())){
	    				messageStr.append(tmp_studentInfo.getStudentName()+"非毕业生，无法撤销学位状态\n");
	    				continue;
	    			}
//	    			学位状态改为"待审核"
	    			tmp_graduateData.setDegreeStatus(Constants.BOOLEAN_WAIT);
	    			tmp_graduateData.setStudentInfo(tmp_studentInfo);
	    			//清空信息
	    			tmp_graduateData.setDegreeName("");//学位名称
	    			tmp_graduateData.setDegreeNum("");//学位编号
//	    			毕业生库标识为未发布
	    			graduateDataService.saveOrUpdate(tmp_graduateData);
	    			//修改审核表
	    			List<StudentGraduateAndDegreeAudit> audits =
							studentGraduateAndDegreeAuditService.findByHql(" from "+StudentGraduateAndDegreeAudit.class.getSimpleName()+" where studentInfo.resourceid=? and graduateData.resourceid = ? ",
									tmp_studentInfo.getResourceid(),tmp_graduateData.getResourceid() );
	    			if(null != audits && audits.size() > 0){
	    				for(StudentGraduateAndDegreeAudit ads : audits ){
	    					studentGraduateAndDegreeAuditService.truncate(ads);
	    				}
	    			}
				}
	    		UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "3", UserOperationLogs.REPEAL,"撤销学位：resourceids"+resourceid);
	    		map.put("statusCode", 200);
	    		messageStr.append("撤销成功");
	    		map.put("message", messageStr.toString());
	    	}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("statusCode", 300);
			map.put("message", "撤销失败！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}


	/**
	 * 毕业证明打印预览
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/roll/graduation/student/print-view.html")
	public String printExamExcusedListView(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		model.addAttribute("stus",ExStringUtils.trimToEmpty(request.getParameter("stus")));
//		model.addAttribute("graduateCertificationDate",ExStringUtils.trimToEmpty(request.getParameter("graduateCertificationDate")));
		return "/edu3/roll/graduationStudent/printGraduateCertification";
	}

	/**
	 * 毕业证明打印
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping("/edu3/roll/graduation/student/print.html")
	public void graduateCertificationReportPrint(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		JasperPrint jasperPrint= null;//输出的报表
		String basicPath    =	CacheAppManager.getSysConfigurationByCode("web.uploadfile.rootpath").getParamValue()+"common"+File.separator+"graduateCetification";
		String stus 	   = ExStringUtils.trimToEmpty(request.getParameter("stus"));
//		学籍办新需求：毕业证明的时间取自毕业生库中的毕业时间。
//	    String graduateCertificationDate = ExStringUtils.trimToEmpty(request.getParameter("graduateCertificationDate"));
//	    graduateCertificationDate=DateChineseFormat.change(graduateCertificationDate);
		String[] studentIds = stus.split(",");
	    StringBuffer ids = new StringBuffer();
	    ids.append("(");
	    for (String string : studentIds) {
			ids.append("'"+string+"',");
		}
	    ids=ids.deleteCharAt(ids.length()-1);
		ids.append(")");
	    //打印部分的业务
		Map<String,Object> param=new HashMap<String, Object>();
		try {
//			学籍办新需求：毕业证明的时间取自毕业生库中的毕业时间。
//			param.put("graduateCertificationDate", graduateCertificationDate);
			param.put("stus", ids.toString());
			param.put("basicPath",basicPath);
			param.put("SUBREPORT_DIR", "//");
			param.put("school", CacheAppManager.getSysConfigurationByCode("graduateData.schoolName").getParamValue() + "继续教育学院");//学校名称
			//Connection conn = graduateDataService.getConn();
			String reportfile=URLDecoder.decode(request.getSession().getServletContext().getRealPath(File.separator+"reports"+File.separator+"graduation"+File.separator+"graduateCertification.jasper"),"utf-8");
			File report_file =new File(reportfile);
			//jasperPrint=JasperFillManager.fillReport(report_file.getPath(), param,conn);
			jasperPrint = graduateDataService.printReport(report_file.getPath(), param, graduateDataService.getConn());
			if(null!=jasperPrint){
				renderStream(response, jasperPrint);
			}else {
				renderHtml(response, "缺少打印数据！");
			}
		} catch (Exception e) {
			logger.error("打印毕业证明失败{}",e.fillInStackTrace());
			renderHtml(response, "打印毕业证明失败。");
		}
	}
	/**
	 * 毕业信息统计条件选择
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/schoolroll/graduate/excel/exportGraduateStuCondition.html")
	public String gradutationStatCondition(HttpServletRequest request,HttpServletResponse response,ModelMap modelMap) throws WebException{
		String branchSchool = "";
		User cureentUser = SpringSecurityHelper.getCurrentUser();
		if(cureentUser.getOrgUnit().getUnitType().
				indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){
			branchSchool = cureentUser.getOrgUnit().getResourceid();
		}
		modelMap.addAttribute("statistical_classesSelect",graduationQualifService.getGradeToMajorToClasses("","","",branchSchool,"graduationData_classid","statistical_classesSelect"));
		return "/edu3/roll/graduationStudent/graduation-statcondition";
	}
	
	@RequestMapping("/edu3/roll/graduationStudent/graduationInfoStat.html")
	public String guaduationStat(HttpServletRequest request,HttpServletResponse respose,ModelMap modelMap,Page objPage){
		String branchSchool = request.getParameter("statistical_branchSchool");//教学点
		String classId 		= request.getParameter("statistical_classesSelect"); //班级
		String classic 		= request.getParameter("statistical_classic");//层次
		String studyType 	= request.getParameter("statistical_teachingType");//学习形式
		Map<String,Object> param = new HashMap<String, Object>();
		//条件
		if(ExStringUtils.isNotBlank(branchSchool)){
			param.put("statistical_branchSchool", branchSchool);
		}
		if(ExStringUtils.isNotBlank(classId)){
			param.put("statistical_classesSelect", classId);
		}
		if(ExStringUtils.isNotBlank(classic)){
			param.put("statistical_classic", classic);
		}
		if(ExStringUtils.isNotBlank(studyType)){
			param.put("statistical_teachingType", studyType);
		}
		objPage = graduationStatJDBCService.statisticalGraduation(param,objPage);
		modelMap.addAttribute("statistical_classesSelect",graduationQualifService.getGradeToMajorToClasses("","",classId,branchSchool,"statistical_classesSelect","statistical_classesSelect"));
		modelMap.addAttribute("statistical_page", objPage);
		modelMap.addAttribute("condition", param);
		return "/edu3/roll/graduationStudent/graduation-statcondition";
	}
	/**
	 * 导出毕业统计信息
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/roll/graduationStudent/exportGraduationInfos.html")
	public void exportGraduationInfos(HttpServletRequest request,HttpServletResponse response,Page objPage) {
	//从库中查出数据
		String statistical_branchSchool		=	ExStringUtils.trimToEmpty(request.getParameter("statistical_branchSchool"));//办学点
		String statistical_classesSelect	=	ExStringUtils.trimToEmpty(request.getParameter("statistical_classesSelect"));//班级
		String statistical_classic			=	ExStringUtils.trimToEmpty(request.getParameter("statistical_classic"));//层次
		String statistical_teachingType		=	ExStringUtils.trimToEmpty(request.getParameter("statistical_teachingType"));//学习形式

		HashMap<String,Object> map_condition = new HashMap<String,Object>(0);
		if (ExStringUtils.isNotBlank(statistical_branchSchool)) {
			map_condition.put("statistical_branchSchool", statistical_branchSchool);
		}
		if (ExStringUtils.isNotBlank(statistical_classesSelect)) {
			map_condition.put("statistical_classesSelect", statistical_classesSelect);
		}
		if(ExStringUtils.isNotBlank(statistical_classic)){
			map_condition.put("statistical_classic", statistical_classic);
		}
		if (ExStringUtils.isNotBlank(statistical_teachingType)) {
			map_condition.put("statistical_teachingType", statistical_teachingType);
		}
		User cureentUser = SpringSecurityHelper.getCurrentUser();
		if(cureentUser.getOrgUnit().getUnitType().
				indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){
			statistical_branchSchool = cureentUser.getOrgUnit().getResourceid();
		}
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		try{
			Map<String,Object> param = new HashMap<String, Object>();
			//条件
			if(ExStringUtils.isNotBlank(statistical_branchSchool)){
				param.put("statistical_branchSchool", statistical_branchSchool);
			}
			if(ExStringUtils.isNotBlank(statistical_classesSelect)){
				param.put("statistical_classesSelect", statistical_classesSelect);
			}
			if(ExStringUtils.isNotBlank(statistical_classic)){
				param.put("statistical_classic", statistical_classic);
			}
			if(ExStringUtils.isNotBlank(statistical_teachingType)){
				param.put("statistical_teachingType", statistical_teachingType);
			}
			list = graduationStatJDBCService.statisticalGraduationForExport(param);
			//文件输出服务器路径
			setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
			//导出
			File excelFile = null;
			GUIDUtils.init();
			File disFile = new File(getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(false) + ".xls");
			//模板文件路径
			String templateFilepathString = "exportStatistical.xls";
			List<String> dictCodeList = new ArrayList<String>();
			dictCodeList.add("CodeTeachingType");
			exportExcelService.initParmasByfile(disFile, "ExportGraduationInfos", list,dictionaryService.getDictionByMap(dictCodeList, true,IDictionaryService.PREKEY_TYPE_BYCODE));
			exportExcelService.getModelToExcel().setRowHeight(500);//设置行高

			exportExcelService.getModelToExcel().setTemplateParam(templateFilepathString, 2,null );
			excelFile = exportExcelService.getExcelFile();//获取导出的文件
			logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());
			downloadFile(response, "毕业生统计表.xls", excelFile.getAbsolutePath(),true);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("导出excel文件出错："+e.fillInStackTrace());
		}
	}
	/**
	 * 学位表统计导出条件
	 * @param request
	 * @param response
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/roll/graduationStudent/exportDegreeInfoCondition.html")
	public String exportDegreeInfoCondition(HttpServletRequest request,HttpServletResponse response,Page objPage,ModelMap modelMap) throws WebException {
		String branchSchoolInDegreeExport=ExStringUtils.trimToEmpty(request.getParameter("branchSchoolInDegreeExport"));
		String gradeInDegreeExport=ExStringUtils.trimToEmpty(request.getParameter("gradeInDegreeExport"));
		String classicInDegreeExport=ExStringUtils.trimToEmpty(request.getParameter("classicInDegreeExport"));
		String majorInDegreeExport=ExStringUtils.trimToEmpty(request.getParameter("majorInDegreeExport"));
		String graduateDateInDegreeExport=ExStringUtils.trimToEmpty(request.getParameter("graduateDateInDegreeExport"));
		//新关联的两个参数
		String confirmGraduateDatebInDegreeExport = ExStringUtils.trimToEmpty(request.getParameter("confirmGraduateDateb"));
		String confirmGraduateDateeInDegreeExport = ExStringUtils.trimToEmpty(request.getParameter("confirmGraduateDatee"));

		HashMap<String,Object> map_condition = new HashMap<String,Object>(0);
		if(!"".equals(branchSchoolInDegreeExport)) {
			map_condition.put("branchschoolid", branchSchoolInDegreeExport);
		}
		if(!"".equals(gradeInDegreeExport)) {
			map_condition.put("gradeid", gradeInDegreeExport);
		}
		if(!"".equals(classicInDegreeExport)) {
			map_condition.put("classicid", classicInDegreeExport);
		}
		if(!"".equals(majorInDegreeExport)) {
			map_condition.put("majorid", majorInDegreeExport);
		}
		if(!"".equals(graduateDateInDegreeExport)) {
			map_condition.put("graduatedate", graduateDateInDegreeExport);
		}
		if(!ExStringUtils.isBlank(confirmGraduateDatebInDegreeExport)) {
			map_condition.put("confirmGraduateDatebInDegreeExport", confirmGraduateDatebInDegreeExport);
		}
		if(!ExStringUtils.isBlank(confirmGraduateDateeInDegreeExport)) {
			map_condition.put("confirmGraduateDateeInDegreeExport", confirmGraduateDateeInDegreeExport);
		}


		List<Map<String,Object>> resultList = graduationStatJDBCService.exportDegreeStat(map_condition);
		Integer pageNum = 1;
		Integer pageTotal = 1;
		if(0!=objPage.getPageNum()){
			pageNum = objPage.getPageNum();
		}

		//由List转为分页
		List<Map<String, Object>> tmpList = new ArrayList<Map<String,Object>>(0);
		if(resultList.size()>0){
			Integer num = resultList.size();
			objPage.setAutoCount(true);
			objPage.setPageNum(pageNum);
			objPage.setTotalCount(num);
			Integer pageSize = objPage.getPageSize();
			if(num%pageSize>0){
				pageTotal = 1+ num/pageSize;
			}else{
				pageTotal = num/pageSize;
			}
			modelMap.addAttribute("pageTotal", pageTotal);
			//每页显示20条记录
			for (int i =0 ;i<pageSize ;i++) {
				if(num>(pageSize*(pageNum-1)+i)) {
					tmpList.add(resultList.get(pageSize*(pageNum-1)+i));
				}
			}

			objPage.setResult(tmpList);
		}else{
			objPage.setAutoCount(true);
			objPage.setPageNum(pageNum);
			objPage.setTotalCount(0);
			objPage.setResult(tmpList);
			modelMap.addAttribute("pageTotal", 0);
		}
		modelMap.addAttribute("dgInfos1", objPage);


		Map<String,Object> condition = new HashMap<String,Object>(0);
		condition.put("branchSchoolInDegreeExport",branchSchoolInDegreeExport );
		condition.put("gradeInDegreeExport",gradeInDegreeExport );
		condition.put("classicInDegreeExport",classicInDegreeExport );
		condition.put("majorInDegreeExport",majorInDegreeExport );
		condition.put("graduateDateInDegreeExport",graduateDateInDegreeExport );

		condition.put("confirmGraduateDateb",confirmGraduateDatebInDegreeExport );
		condition.put("confirmGraduateDatee",confirmGraduateDateeInDegreeExport );
		modelMap.addAttribute("condition", condition);
		resultList=new ArrayList<Map<String,Object>>(0);

		modelMap.addAttribute("dgInfos", resultList);
		modelMap.addAttribute("pageNum",pageNum);

		return "/edu3/roll/graduationStudent/exportDegreeInfoCondition";
	}

	/**
	 * 学位表统计导出
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/roll/graduationStudent/exportDegreeInfo.html")
	public void exportDegreeInfo(HttpServletRequest request,HttpServletResponse response) throws WebException {
		String branchSchoolInDegreeExport=ExStringUtils.trimToEmpty(request.getParameter("branchSchoolInDegreeExport"));
		String gradeInDegreeExport=ExStringUtils.trimToEmpty(request.getParameter("gradeInDegreeExport"));
		String classicInDegreeExport=ExStringUtils.trimToEmpty(request.getParameter("classicInDegreeExport"));
		String majorInDegreeExport=ExStringUtils.trimToEmpty(request.getParameter("majorInDegreeExport"));
		String graduateDateInDegreeExport=ExStringUtils.trimToEmpty(request.getParameter("graduateDateInDegreeExport"));
		//新关联的两个参数
		String confirmGraduateDatebInDegreeExport = ExStringUtils.trimToEmpty(request.getParameter("confirmGraduateDateb"));
		String confirmGraduateDateeInDegreeExport = ExStringUtils.trimToEmpty(request.getParameter("confirmGraduateDatee"));

		HashMap<String,Object> map_condition = new HashMap<String,Object>(0);
		if(!"".equals(branchSchoolInDegreeExport)) {
			map_condition.put("branchschoolid", branchSchoolInDegreeExport);
		}
		if(!"".equals(gradeInDegreeExport)) {
			map_condition.put("gradeid", gradeInDegreeExport);
		}
		if(!"".equals(classicInDegreeExport)) {
			map_condition.put("classicid", classicInDegreeExport);
		}
		if(!"".equals(majorInDegreeExport)) {
			map_condition.put("majorid", majorInDegreeExport);
		}
		if(!"".equals(graduateDateInDegreeExport)) {
			map_condition.put("graduatedate", graduateDateInDegreeExport);
		}
		if(!ExStringUtils.isBlank(confirmGraduateDatebInDegreeExport)) {
			map_condition.put("confirmGraduateDatebInDegreeExport", confirmGraduateDatebInDegreeExport);
		}
		if(!ExStringUtils.isBlank(confirmGraduateDateeInDegreeExport)) {
			map_condition.put("confirmGraduateDateeInDegreeExport", confirmGraduateDateeInDegreeExport);
		}

		List<Map<String,Object>> list = graduationStatJDBCService.exportDegreeStat(map_condition);
		//转为Bean形式
		List<StudentBaseInfo> results = new ArrayList<StudentBaseInfo>(0);
		for (Map<String,Object> map : list) {
			StudentBaseInfo tmpGraduateData = new StudentBaseInfo();
			if(map.containsKey("graduatedate")) {
				tmpGraduateData.setBankBook(map.get("graduatedate").toString());
			}
			if(map.containsKey("classicname")) {
				tmpGraduateData.setBloodType(map.get("classicname").toString());
			}
			if(map.containsKey("unitname")) {
				tmpGraduateData.setBornAddress(map.get("unitname").toString());
			}
			if(map.containsKey("gradename")) {
				tmpGraduateData.setCertNum(map.get("gradename").toString());
			}
			if(map.containsKey("majorname")) {
				tmpGraduateData.setContactAddress(map.get("majorname").toString());
			}
			if(map.containsKey("total")) {
				tmpGraduateData.setContactPhone(map.get("total").toString());
			}
			if(map.containsKey("man")) {
				tmpGraduateData.setContactZipcode(map.get("man").toString());
			}
			if(map.containsKey("lady")) {
				tmpGraduateData.setCountry(map.get("lady").toString());
			}
			results.add(tmpGraduateData);
		}
		FileUploadAndDownloadSupportController fileup = new FileUploadAndDownloadSupportController();
		//文件输出服务器路径
		fileup.setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
		try{
			File excelFile = null;
			GUIDUtils.init();
			File disFile = new File(fileup.getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(false) + ".xls");
			//初始化参数
			exportExcelService.initParmasByfile(disFile, "StudentBaseInfo",results,null);//初始化配置参数
			exportExcelService.getModelToExcel().setHeader("学位统计导出");//设置大标题
			exportExcelService.getModelToExcel().setRowHeight(500);//设置行高
			excelFile = exportExcelService.getExcelFile();//获取导出的文件
			logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());
			fileup.downloadFile(response, "学位统计导出"+".xls", excelFile.getAbsolutePath(),true);

		}catch (Exception e) {
			e.printStackTrace();
			logger.error("导出excel文件出错："+e.fillInStackTrace());
		}
	}
	/**
	 * 毕业数据自定义导出条件
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/roll/graduationStudent/exportCustomExcelInGBDCondition.html")
	public String doExcelExportStudentGraduateCustomCondition(HttpServletRequest request,HttpServletResponse response,ModelMap map) throws WebException{
		String exgbranchSchool= request.getParameter("unit");
		String exggrade= request.getParameter("grade");
		String exggraduateDate= request.getParameter("graduateDate");
		String exgclassic= request.getParameter("classic");
		String exgmajor= request.getParameter("major");
		//String exgbranchSchool_txt= request.getParameter("unit_txt");
		//String exggrade_txt= request.getParameter("grade_txt");
		//String exgclassic_txt= request.getParameter("classic_txt");
		//String exgmajor_txt= request.getParameter("major_txt");
		String degreeStatus = request.getParameter("degreeStatus");//学位状态
		String degreeApplyStatus	= request.getParameter("degreeApplyStatus");//学位申请状态
		
		//add
		String graduationType	= request.getParameter("graduationType");//毕业类型
		String teachingType     = request.getParameter("teachingType");//学习形式
		String classes			= request.getParameter("classes");//班级

		
		String confirmGraduateDateb = ExStringUtils.trimToEmpty(request.getParameter("confirmGraduateDateb"));
		String confirmGraduateDatee = ExStringUtils.trimToEmpty(request.getParameter("confirmGraduateDatee"));

		map.addAttribute("exgbranchSchool",exgbranchSchool);
		map.addAttribute("exggrade",exggrade);
		map.addAttribute("exggraduateDate",exggraduateDate);
		map.addAttribute("exgclassic",exgclassic);
		map.addAttribute("exgmajor",exgmajor);

		//map.addAttribute("exgbranchSchool_txt",exgbranchSchool_txt);
		//map.addAttribute("exggrade_txt",exggrade_txt);
		//map.addAttribute("exgclassic_txt",exgclassic_txt);
		//map.addAttribute("exgmajor_txt",exgmajor_txt);
		//add
		map.addAttribute("graduationType",graduationType);
		map.addAttribute("teachingType",teachingType);
		map.addAttribute("classes",classes);
		map.addAttribute("degreeStatus",degreeStatus);
		map.addAttribute("degreeApplyStatus",degreeApplyStatus);
		
		List<String> gDates = new ArrayList<String>(0);
		gDates = graduationStatJDBCService.getSingleDistinctPropertyValue("TO_CHAR(GRADUATEDATE,'YYYY-MM-DD')","TO_CHAR(GRADUATEDATE,'YYYY-MM-DD')");
		map.addAttribute("gDates", gDates);
		map.addAttribute("confirmGraduateDateb",confirmGraduateDateb);
		map.addAttribute("confirmGraduateDatee",confirmGraduateDatee);
		return "/edu3/roll/graduationStudent/exportCustomExcelInGBDCondition";
	}

	/**
	 * 自定义导出信息
	 * @param studentInfoVo
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/roll/graduationStudent/exportCustomExcel.html")
	public void exportGraduationInfos(StudentInfoVo studentInfoVo,HttpServletRequest request,HttpServletResponse response) {
	//从库中查出数据

		String unit        = ExStringUtils.trimToEmpty(request.getParameter("exgbranchSchool"));
		String grade       = ExStringUtils.trimToEmpty(request.getParameter("exggrade"));
		String graduateDate = ExStringUtils.trimToEmpty(request.getParameter("exggraduateDate"));
		String classic     = ExStringUtils.trimToEmpty(request.getParameter("exgclassic"));
		String major       = ExStringUtils.trimToEmpty(request.getParameter("exgmajor"));
		String degreeStatus       = ExStringUtils.trimToEmpty(request.getParameter("degreeStatus"));//学位状态
		String confirmGraduateDateb     = ExStringUtils.trimToEmpty(request.getParameter("confirmGraduateDateb"));
		String confirmGraduateDatee     = ExStringUtils.trimToEmpty(request.getParameter("confirmGraduateDatee"));

		String graduationType           = ExStringUtils.trimToEmpty(request.getParameter("exgraduationType")); //毕业类型
		String teachingtype             = ExStringUtils.trimToEmpty(request.getParameter("exteachingType")); //学习形式
		String classes                  = ExStringUtils.trimToEmpty(request.getParameter("exclasses")); //班级
		String degreeApplyStatus  = ExStringUtils.trimToEmpty(request.getParameter("degreeApplyStatus")); //学位申请状态
		
		List<GraduateData> results = new ArrayList<GraduateData>(0);

		Map<String,Object> condition = new HashMap<String, Object>(0);
		if(ExStringUtils.isNotEmpty(unit)) {
			condition.put("branchSchool", unit);
		}
		if(ExStringUtils.isNotEmpty(grade)) {
			condition.put("grade", grade);
		}
		if(ExStringUtils.isNotEmpty(graduateDate)) {
			condition.put("graduateDate", graduateDate);
		}
		if(ExStringUtils.isNotEmpty(classic)) {
			condition.put("classic", classic);
		}
		if(ExStringUtils.isNotEmpty(major)) {
			condition.put("major", major);
		}
		if(ExStringUtils.isNotEmpty(graduationType)) {
			condition.put("graduationType", graduationType);
		}
		if(ExStringUtils.isNotEmpty(teachingtype)) {
			condition.put("teachingtype", teachingtype);
		}
		if(ExStringUtils.isNotEmpty(classes)) {
			condition.put("classes", classes);
		}
		if(ExStringUtils.isNotEmpty(degreeStatus)) {
			condition.put("degreeStatus", degreeStatus);
		}
		if(ExStringUtils.isNotEmpty(confirmGraduateDateb) && !"undefined".equals(confirmGraduateDateb)) {
			condition.put("confirmGraduateDateb", confirmGraduateDateb);
		}
		if(ExStringUtils.isNotEmpty(confirmGraduateDatee) && !"undefined".equals(confirmGraduateDatee)) {
			condition.put("confirmGraduateDatee", confirmGraduateDatee);
		}
		if(ExStringUtils.isNotEmpty(degreeApplyStatus) && !"undefined".equals(degreeApplyStatus)) {
			condition.put("degreeApplyStatus", degreeApplyStatus);
		}
		
		if(ExStringUtils.isNotEmpty(unit)) {
			condition.put("branchSchool", unit);
		}
		if(ExStringUtils.isNotEmpty(grade)) {
			condition.put("grade", grade);
		}
		if(ExStringUtils.isNotEmpty(graduateDate)) {
			condition.put("graduateDate", graduateDate);
		}
		if(ExStringUtils.isNotEmpty(classic)) {
			condition.put("classic", classic);
		}
		if(ExStringUtils.isNotEmpty(major)) {
			condition.put("major", major);
		}
		if(ExStringUtils.isNotEmpty(graduationType)) {
			condition.put("graduationType", graduationType);
		}
		if(ExStringUtils.isNotEmpty(teachingtype)) {
			condition.put("teachingtype", teachingtype);
		}
		if(ExStringUtils.isNotEmpty(classes)) {
			condition.put("classes", classes);
		}
		if(ExStringUtils.isNotEmpty(degreeStatus)) {
			condition.put("degreeStatus", degreeStatus);
		}
		if(ExStringUtils.isNotEmpty(confirmGraduateDateb) && !"undefined".equals(confirmGraduateDateb)) {
			condition.put("confirmGraduateDateb", confirmGraduateDateb);
		}
		if(ExStringUtils.isNotEmpty(confirmGraduateDatee) && !"undefined".equals(confirmGraduateDatee)) {
			condition.put("confirmGraduateDatee", confirmGraduateDatee);
		}

		condition.put("sbiNotNull", "1");
		results = graduateDataService.findByHql(condition);
		List<StudentInfoVo> voResults = new ArrayList<StudentInfoVo>(0);
		//是否需要把XYMC特殊处理
		List<Dictionary> listDictZYDM = dictionaryService.findByHql(" from "+Dictionary.class.getSimpleName()+ " where isDeleted = ? and isUsed = ? and parentDict.dictCode = ? "
				, 0,Constants.BOOLEAN_YES,"CodeLearnwebReplaceStr");
		SimpleDateFormat df=new SimpleDateFormat("yyyy/MM/dd");
		for (GraduateData graduateData : results) {
//			System.out.println(graduateData.getSchoolCode());
			StudentInfoVo vo = new StudentInfoVo();
			vo.setStudentInfo(graduateData.getStudentInfo());
			vo.setMajorCode(graduateData.getStudentInfo().getMajor().getMajorCode());
			vo.setAttach(graduateData);
			Date time = graduateData.getStudentInfo().getInDate();
			if(null != time) {
				vo.setEntranceDate(df.format(time));
			}
			String majorName = vo.getMajorName();
			if(null != listDictZYDM && listDictZYDM.size() > 0 && ExStringUtils.isNotBlank(majorName)){
				String[] zymcReplaceArrayL = new String[listDictZYDM.size()];
				String[] zymcReplaceArrayR = new String[listDictZYDM.size()];
				for (int i = 0; i < listDictZYDM.size(); i++) {
					String rpstr1 = ExStringUtils.trim(listDictZYDM.get(i).getDictName()+"").replace("（", "(").replace("）", ")");
					String rpstr2 = ExStringUtils.trim(listDictZYDM.get(i).getDictValue()+"").replace("（", "(").replace("）", ")");
					zymcReplaceArrayL[i] = rpstr1;
					zymcReplaceArrayR[i] = rpstr2;
				}
				majorName = ReplaceStr.replace(zymcReplaceArrayL, zymcReplaceArrayR, majorName); //去除符号（含符号）里面的字符
			}
			vo.setMajorName(majorName);
			voResults.add(vo);
		}

		FileUploadAndDownloadSupportController fileup = new FileUploadAndDownloadSupportController();
		//文件输出服务器路径
		fileup.setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
		try{
			File excelFile = null;
			GUIDUtils.init();
			File disFile = new File(fileup.getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(false) + ".xls");
			List<String> filterColumnList = new ArrayList<String>();
		    //学籍以及学籍基本信息的字段
			Field[] properties = StudentInfoVo.class.getDeclaredFields();
			for (Field field : properties) {
				String propertiesName = field.getName();
				if("serialVersionUID".equals(propertiesName)){
					continue;
				}
				String methodName = "get"+propertiesName.subSequence(0, 1).toString().toUpperCase()+propertiesName.substring(1, propertiesName.length());;
				Method method = StudentInfoVo.class.getDeclaredMethod(methodName);
				if(null!=method.invoke(studentInfoVo, null) ){
					filterColumnList.add(propertiesName);
				}
			}
			//教学计划、专业、毕业信息
			List<String> otherthing = new ArrayList<String>(0);
			otherthing.add("yxmc");
			otherthing.add("yxdm");
			otherthing.add("majorName");
			otherthing.add("majorCode");
			otherthing.add("entranceDate");
			otherthing.add("eduYear");
			otherthing.add("graduateDate");
			otherthing.add("diplomaNum");
			otherthing.add("degreeNum");
			otherthing.add("degree");
			otherthing.add("bjyjl");
			otherthing.add("xzxm");
			otherthing.add("fxzy");
			otherthing.add("bz_sh");
			for (String propertiesName : otherthing) {
				String methodName = "get"+propertiesName.subSequence(0, 1).toString().toUpperCase()+propertiesName.substring(1, propertiesName.length());
				Method method = StudentInfoVo.class.getDeclaredMethod(methodName);
				if(null!=method.invoke(studentInfoVo, null) ){
					filterColumnList.add(propertiesName);
				}
			}
			//初始化参数
				List<String> dictCodeList = new ArrayList<String>();
				dictCodeList.add("CodeAttendAdvancedStudies");
				dictCodeList.add("CodeLearningStyle");
				dictCodeList.add("CodeStudyInSchool");
				dictCodeList.add("CodeStudentKind");
				dictCodeList.add("CodeEnterSchool");
				dictCodeList.add("CodeLearingStatus");
				dictCodeList.add("CodeStudentStatus");
				dictCodeList.add("CodeTeachingType");

				dictCodeList.add("CodeAccountStatus");
				dictCodeList.add("CodeAuditStatus");

				//baseinfo
				dictCodeList.add("CodeSex");
				dictCodeList.add("CodeCertType");
				dictCodeList.add("CodeBloodStyle");
				dictCodeList.add("CodeCountry");
				dictCodeList.add("CodeGAQ");
				dictCodeList.add("CodeNation");
				dictCodeList.add("CodeHealth");
				dictCodeList.add("CodeMarriage");
				dictCodeList.add("CodePolitics");
				dictCodeList.add("CodeRegisteredResidenceKind");
				dictCodeList.add("CodeWorkStatus");
				Map<String,Object> dictMap = dictionaryService.getDictionByMap(dictCodeList, true,IDictionaryService.PREKEY_TYPE_BYCODE);
				//是否预约毕业论文
				dictMap.put("CodeAutitGCCustom_"+Constants.BOOLEAN_YES, "已预约");
				dictMap.put("CodeAutitGCCustom_"+Constants.BOOLEAN_NO, "未预约");
				//是否申请学位
				dictMap.put("CodeAuditGraduateCustom_"+Constants.BOOLEAN_YES, "申请学位+毕业");
				dictMap.put("CodeAuditGraduateCustom_"+Constants.BOOLEAN_NO, "申请毕业");
				dictMap.put("CodeAuditGraduateCustom_"+Constants.BOOLEAN_WAIT, "申请延迟毕业");
				//学生帐号状态
				dictMap.put("CodeAccountStatusCustom_1", "启用");
				dictMap.put("CodeAccountStatusCustom_0", "关闭");
				//学生预约状态
				dictMap.put("CodeAuditCustom_1", "启用");
				dictMap.put("CodeAuditCustom_0", "关闭");

				exportExcelService.initParmasByfile(disFile, "customInfo",voResults,dictMap,filterColumnList);//初始化配置参数
				exportExcelService.getModelToExcel().setHeader("毕业数据自定义导出");//设置大标题
				exportExcelService.getModelToExcel().setRowHeight(500);//设置行高
				excelFile = exportExcelService.getExcelFile();//获取导出的文件
				logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());
				fileup.downloadFile(response, "毕业数据自定义导出"+".xls", excelFile.getAbsolutePath(),true);

		}catch (Exception e) {
			e.printStackTrace();
			logger.error("导出excel文件出错："+e.fillInStackTrace());
		}
	}
	/**
	 * 打印个人成绩单-预览
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/roll/graduationStudent/personalReportCard-view.html")
	public String personalReportCardView(HttpServletRequest request,ModelMap model){
		model.addAttribute("flag",ExStringUtils.trimToEmpty(request.getParameter("flag")));
		model.addAttribute("studentId",ExStringUtils.trimToEmpty(request.getParameter("studentId")));
		model.addAttribute("printDate",ExStringUtils.trimToEmpty(request.getParameter("printDate")));
		String studyTime = ExStringUtils.trimToEmpty(request.getParameter("studyTime"));

		try {
			studyTime = URLEncoder.encode(studyTime,"UTF-8");
		} catch (UnsupportedEncodingException e) {
		}
		model.addAttribute("studyTime",studyTime);
		return "/edu3/teaching/examResult/personal-reportcard-printview";
	}
	/**
	 * 学籍卡打印-预览
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 * @throws ServiceException
	 */
	@RequestMapping(value={"/edu3/roll/studentCard/print-view.html","/edu3/roll/studentCard/print-viewinstupage.html"})
	public String rollCardPrintView(HttpServletRequest request,ModelMap model) throws ServiceException, Exception{
		String name =  ExStringUtils.trimToEmpty(request.getParameter("name"));
		if (ExStringUtils.isNotBlank(name)) {
			try {
				name    = URLEncoder.encode(name,"utf-8");
			} catch (UnsupportedEncodingException e) {

			}
		}
		String stuChangeInfo            = ExStringUtils.trimToEmpty(request.getParameter("stuChangeInfo"));//
		if (ExStringUtils.isNotBlank(stuChangeInfo)) {
			try {
				stuChangeInfo    = URLEncoder.encode(stuChangeInfo,"utf-8");
			} catch (UnsupportedEncodingException e) {

			}
		}

		model.addAttribute("flag", ExStringUtils.trimToEmpty(request.getParameter("flag")));
		model.addAttribute("branchSchool", ExStringUtils.trimToEmpty(request.getParameter("branchSchool")));
		model.addAttribute("major", ExStringUtils.trimToEmpty(request.getParameter("major")));
		model.addAttribute("classic", ExStringUtils.trimToEmpty(request.getParameter("classic")));
		model.addAttribute("stuStatus", ExStringUtils.trimToEmpty(request.getParameter("stuStatus")));
		model.addAttribute("name",name);
		model.addAttribute("studyNo", ExStringUtils.trimToEmpty(request.getParameter("studyNo")));
		model.addAttribute("grade", ExStringUtils.trimToEmpty(request.getParameter("grade")));
		model.addAttribute("graduateDate", ExStringUtils.trimToEmpty(request.getParameter("graduateDate")));
		model.addAttribute("confirmGraduateDateb", ExStringUtils.trimToEmpty(request.getParameter("confirmGraduateDateb")));
		model.addAttribute("confirmGraduateDatee", ExStringUtils.trimToEmpty(request.getParameter("confirmGraduateDatee")));
		model.addAttribute("resourceid", ExStringUtils.trimToEmpty(request.getParameter("resourceid")));
		model.addAttribute("entranceTime", ExStringUtils.trimToEmpty(request.getParameter("indate")));
		//来自学籍
		model.addAttribute("stuGrade_i", ExStringUtils.trimToEmpty(request.getParameter("stuGrade_i")));
		model.addAttribute("branchSchool_i", ExStringUtils.trimToEmpty(request.getParameter("branchSchool_i")));
		model.addAttribute("major_i", ExStringUtils.trimToEmpty(request.getParameter("major_i")));
		model.addAttribute("classic_i", ExStringUtils.trimToEmpty(request.getParameter("classic_i")));
		model.addAttribute("stuStatus_i", ExStringUtils.trimToEmpty(request.getParameter("stuStatus_i")));
		model.addAttribute("name_i", ExStringUtils.trimToEmpty(request.getParameter("name_i")));
		model.addAttribute("certNum_i", ExStringUtils.trimToEmpty(request.getParameter("certNum_i")));
		model.addAttribute("studyNo_i", ExStringUtils.trimToEmpty(request.getParameter("studyNo_i")));
		model.addAttribute("rollCard_i", ExStringUtils.trimToEmpty(request.getParameter("rollCard_i")));
		model.addAttribute("teachPlan_i", ExStringUtils.trimToEmpty(request.getParameter("teachPlan_i")));
		model.addAttribute("entranceFlag_i", ExStringUtils.trimToEmpty(request.getParameter("entranceFlag_i")));
		model.addAttribute("resourceid_i", ExStringUtils.trimToEmpty(request.getParameter("resourceid_i")));
		model.addAttribute("from",ExStringUtils.trimToEmpty(request.getParameter("from")));
		model.addAttribute("stuChangeInfo",stuChangeInfo);

		model.addAttribute("classes", ExStringUtils.trimToEmpty(request.getParameter("classes")));
		model.addAttribute("degreeStatus", ExStringUtils.trimToEmpty(request.getParameter("degreeStatus")));
		return "/edu3/roll/graduationStudent/student-rollcard-printview";
	}


	/**
	 * 【学籍表】- 学籍卡打印导出
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/roll/studentCard/printAndExport.html")
	public void rollCardPrintAndExport(HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> map = new HashMap<String, Object>();

		String stuChangeInfo            = ExStringUtils.trimToEmpty(request.getParameter("stuChangeInfo"));//
		if (ExStringUtils.isNotBlank(stuChangeInfo)){
			try {
				stuChangeInfo = URLDecoder.decode(stuChangeInfo,"utf-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			map.put("stuChangeInfo",stuChangeInfo);
		}
		String flag            = ExStringUtils.trimToEmpty(request.getParameter("flag"));// 打印/导出
		String branchSchool	   = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));//学习中心
		String major	       = ExStringUtils.trimToEmpty(request.getParameter("major"));//专业
		String classic	       = ExStringUtils.trimToEmpty(request.getParameter("classic"));//层次
		String stuStatus       = ExStringUtils.trimToEmpty(request.getParameter("stuStatus"));//学籍状态
		String name			   = ExStringUtils.trimToEmpty(request.getParameter("name"));//姓名
		String studyNo		   = ExStringUtils.trimToEmpty(request.getParameter("studyNo"));//学号
		String grade		   = ExStringUtils.trimToEmpty(request.getParameter("grade"));//年级graduateDate
		String graduateDate    = ExStringUtils.trimToEmpty(request.getParameter("graduateDate"));//毕业日期
		String resourceid      = ExStringUtils.trimToEmpty(request.getParameter("resourceid"));//资源ID
		String confirmGraduateDateb = ExStringUtils.trimToEmpty(request.getParameter("gl_confirmGraduateDateb"));//确认毕业日期始
		String confirmGraduateDatee = ExStringUtils.trimToEmpty(request.getParameter("gl_confirmGraduateDatee"));//确认毕业日期终

		//修改BUG 打印学籍表报错 2014-05-27 15:51:15
		String studentids		   = ExStringUtils.trimToEmpty(request.getParameter("studentids"));//要打印的学生id
		String fromStudentRoll 	   = ExStringUtils.trimToEmpty(request.getParameter("fromStudentRoll"));//标识学籍信息管理模块打印

		String classes            = ExStringUtils.trimToEmpty(request.getParameter("classes"));//班级
		String degreeStatus       = ExStringUtils.trimToEmpty(request.getParameter("degreeStatus"));//学位状态

		String from = ExStringUtils.trimToEmpty(request.getParameter("from"));
		String school = CacheAppManager.getSysConfigurationByCode("graduateData.schoolName").getParamValue();
		String schoolCode = CacheAppManager.getSysConfigurationByCode("graduateData.schoolCode").getParamValue();
		String model = "";
		String accountStatus = "";
		if(stuStatus.contains("a")){
			accountStatus="1";
			stuStatus="11";//去掉学籍中有关帐号状态的标记
		}else if(stuStatus.contains("b")){
			accountStatus="0";
			stuStatus="11";//去掉学籍中有关帐号状态的标记
		}
		if("10601".equals(schoolCode)) {//桂林医
			model="10601";
		}

		if (ExStringUtils.isNotBlank(branchSchool)) {
			map.put("branchSchool",branchSchool);
		}
		if (ExStringUtils.isNotBlank(major)) {
			map.put("major",major);
		}
		if (ExStringUtils.isNotBlank(classic)) {
			map.put("classic",classic);
		}
		if (ExStringUtils.isNotBlank(stuStatus)) {
			map.put("stuStatus",stuStatus);
		}
		if (ExStringUtils.isNotBlank(accountStatus)) {
			map.put("accountStatus",accountStatus);
		}
		if (ExStringUtils.isNotBlank(degreeStatus)) {
			map.put("degreeStatus",degreeStatus);
		}
		if (ExStringUtils.isNotBlank(classes)) {
			map.put("classes",classes);
		}
		if (ExStringUtils.isNotBlank(stuChangeInfo)) {
			map.put("stuChangeInfo",stuChangeInfo);
		}
		if (ExStringUtils.isNotBlank(studyNo)) {
			map.put("studyNo",studyNo);
		}
		if (ExStringUtils.isNotBlank(grade)) {
			map.put("grade",grade);
		}
		if (ExStringUtils.isNotBlank(graduateDate)) {
			map.put("graduateDate",graduateDate);
		}
		if (ExStringUtils.isNotBlank(resourceid)) {
			map.put("resourceid",resourceid);
		}
		if (ExStringUtils.isNotBlank(confirmGraduateDateb)) {
			map.put("confirmGraduateDateb",confirmGraduateDateb);
		}
		if (ExStringUtils.isNotBlank(confirmGraduateDatee)) {
			map.put("confirmGraduateDatee",confirmGraduateDatee);
		}
		String entranceTime=ExStringUtils.trimToEmpty(request.getParameter("entranceTime"));
		if (ExStringUtils.isNotBlank(studentids)) {
			map.put("studentids",studentids);
		}
		if (ExStringUtils.isNotBlank(fromStudentRoll)) {
			map.put("fromStudentRoll",fromStudentRoll);
		}

		if("studentinfo".equals(from)){
			map.put("from", from);
			String stuGrade_i = ExStringUtils.trimToEmpty(request.getParameter("stuGrade_i"));
			String branchSchool_i = ExStringUtils.trimToEmpty(request.getParameter("branchSchool_i"));
			String major_i = ExStringUtils.trimToEmpty(request.getParameter("major_i"));
			String classic_i = ExStringUtils.trimToEmpty(request.getParameter("classic_i"));
			String stuStatus_i = ExStringUtils.trimToEmpty(request.getParameter("stuStatus_i"));
			String name_i = ExStringUtils.trimToEmpty(request.getParameter("name_i"));
			String certNum_i = ExStringUtils.trimToEmpty(request.getParameter("certNum_i"));
			String studyNo_i = ExStringUtils.trimToEmpty(request.getParameter("studyNo_i"));
			String rollCard_i = ExStringUtils.trimToEmpty(request.getParameter("rollCard_i"));
			String teachPlan_i = ExStringUtils.trimToEmpty(request.getParameter("teachPlan_i"));
			String entranceFlag_i = ExStringUtils.trimToEmpty(request.getParameter("entranceFlag_i"));
			String resourceid_i = ExStringUtils.trimToEmpty(request.getParameter("resourceid_i"));
			String organizationDate = ExStringUtils.trimToEmpty(request.getParameter("organizationDate"));
			String joinPartyDate = ExStringUtils.trimToEmpty(request.getParameter("joinPartyDate"));
			String accountStatus_i = "";

			if(stuStatus_i.contains("a")){
				accountStatus_i="1";
				stuStatus_i="11";//去掉学籍中有关帐号状态的标记
			}else if(stuStatus.contains("b")){
				accountStatus_i="0";
				stuStatus_i="11";//去掉学籍中有关帐号状态的标记
			}
			User user = SpringSecurityHelper.getCurrentUser();
			if(!SpringSecurityHelper.isUserInRole("ROLE_STUDENT")&&user.getOrgUnit().getUnitType().indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员
				branchSchool_i = user.getOrgUnit().getResourceid();
			}
			if (ExStringUtils.isNotEmpty(stuGrade_i)) {
				map.put("stuGrade",stuGrade_i);
			}
			if (ExStringUtils.isNotEmpty(branchSchool_i)) {
				map.put("branchSchool",branchSchool_i);
			}
			if (ExStringUtils.isNotEmpty(major_i)) {
				map.put("major",major_i);
			}
			if (ExStringUtils.isNotEmpty(classic_i)) {
				map.put("classic",classic_i);
			}
			if (ExStringUtils.isNotEmpty(stuStatus_i)) {
				map.put("stuStatus",stuStatus_i);
			}
			if (ExStringUtils.isNotEmpty(name_i)) {
				map.put("name",name_i);
			}
			if (ExStringUtils.isNotEmpty(certNum_i)) {
				map.put("certNum",certNum_i);
			}
			if (ExStringUtils.isNotEmpty(studyNo_i)) {
				map.put("studyNo",studyNo_i);
			}
			if (ExStringUtils.isNotEmpty(rollCard_i)) {
				map.put("rollCard",rollCard_i);
			}
			if (ExStringUtils.isNotEmpty(teachPlan_i)) {
				map.put("teachPlan",teachPlan_i);
			}
			if (ExStringUtils.isNotEmpty(entranceFlag_i)) {
				map.put("entranceFlag",entranceFlag_i);
			}
			if (ExStringUtils.isNotEmpty(resourceid_i)) {
				map.put("resourceid",resourceid_i);
			}
			if (ExStringUtils.isNotEmpty(accountStatus_i)) {
				map.put("accountStatus", accountStatus_i);
			}
			if (ExStringUtils.isNotEmpty(entranceTime)) {
				map.put("entranceTime", entranceTime);
			}
			try {
				if(ExStringUtils.isNotBlank(organizationDate)) {
					map.put("organizationDate", ExDateUtils.formatDateStr(ExDateUtils.parseDate(organizationDate, ExDateUtils.PATTREN_DATE),ExDateUtils.PATTREN_DATE_CN));
				}
				if(ExStringUtils.isNotBlank(joinPartyDate)) {
					map.put("joinPartyDate",ExDateUtils.formatDateStr(ExDateUtils.parseDate(joinPartyDate, ExDateUtils.PATTREN_DATE),ExDateUtils.PATTREN_DATE_CN));
				}
			} catch (ParseException e) {
				logger.error("转换入团或入党时间格式错误");
				e.printStackTrace();
			}
		}

		try {
			if (ExStringUtils.isNotBlank(name)){
				 name = URLDecoder.decode(name,"utf-8");map.put("name",name);
			}

			List<Map<String,Object>> dataList 	  = getStudentRollCardDatas(map,model);
			if(dataList.size()>0 && ExStringUtils.isEmpty((String)dataList.get(0).get("entranceTime"))){
				dataList.get(0).put("entranceTime",entranceTime);
			}

			String jasper = "";
			if("10601".equals(schoolCode)){//广西桂林医学院
				jasper = "studentInfoCards_gly.jasper";
			}else if("10560".equals(schoolCode)){//汕头大学
				jasper = "studentInfoCards_stdx.jasper";
			}else if("11846".equals(schoolCode)) {//广东外语外贸大学
				jasper = "studentInfoCards_wywm.jasper";
			}else{//默认广大
				jasper = "studentInfoCards.jasper";
			}
			JRMapCollectionDataSource dataSource  = new JRMapCollectionDataSource(dataList);
			String jasperFile     				  = URLDecoder.decode(request.getSession().getServletContext().getRealPath(File.separator+"reports"+
													File.separator+"studentinfo"+File.separator+jasper),"utf-8");
			map.put("imageRootPath",CacheAppManager.getSysConfigurationByCode("web.uploadfile.rootpath").getParamValue()+"common"+File.separator+"students");
//			map.put("unitname", CacheAppManager.getSysConfigurationByCode("graduateData.schoolName").getParamValue()); //学校
			map.put("cardtitle", school+CacheAppManager.getSysConfigurationByCode("graduateData.schoolConnectName").getParamValue()); //标题
			map.put("reportRootPath", request.getSession().getServletContext().getRealPath("/reports/studentinfo"));
			//map.put("stuId",studyNo
			if(stuChangeInfo.indexOf("<br/>")!=-1){
				stuChangeInfo=stuChangeInfo.replaceAll("<br/>","");
				map.put("stuChangeInfo", stuChangeInfo);
			}else{
				map.put("stuChangeInfo", stuChangeInfo);
			}

			JasperPrint jasperPrint               = JasperFillManager.fillReport(jasperFile, map, dataSource); // 填充报表
			setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
			if (null!=jasperPrint) {
				if ("print".equals(flag)) {
					renderStream(response, jasperPrint);
				}else {
					GUIDUtils.init();
					String filePath        		  = getDistfilepath()+File.separator+GUIDUtils.buildMd5GUID(false)+".pdf";
					JRPdfExporter exporter		  = new JRPdfExporter();
					exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
					exporter.setParameter(JRXlsExporterParameter.OUTPUT_FILE_NAME,filePath);

					//exporter.setParameter(JRXlsExporterParameter.OUTPUT_FILE_NAME,"D:\\aa.pdf");
					exporter.exportReport();
					String singlePersonInfo = "";
					if(dataList.size()==1){
						singlePersonInfo = null!=dataList.get(0).get("studyno")?dataList.get(0).get("studyno").toString():"";
					}
					downloadFile(response,singlePersonInfo+"学籍卡.pdf",filePath,true);
				}
			}else {
				renderHtml(response,"<script>alert('缺少打印数据！')</script>");
			}

		} catch (Exception e) {
			logger.error("学籍卡打印出错：{}"+e.fillInStackTrace());
			renderHtml(response,"<script>alert('学籍卡打印出错："+e.getMessage()+"')</script>");
		}
	}

	/**
	 * 组装学籍卡中的数据
	 * @param condition
	 * @return
	 */
	private List<Map<String,Object>> getStudentRollCardDatas(Map<String, Object> condition,String school) throws Exception{
		//StudentInfo studentInfo 	    = studentInfoService.getStudentInfoByUser(curUser);
		//String name = studentInfo.getStudentBaseInfo().getName();
		//List<Map<String,Object>> dataList 			     =  graduationStatJDBCService.studentCardInfo(condition,name);
		List<Map<String,Object>> dataList 			     = new ArrayList<Map<String,Object>>(0);
		//List<Map<String,Object>> dataList 			     = new ArrayList<Map<String,Object>>(0);
		if(condition.containsKey("from")&&"studentinfo".equals(condition.get("from"))){
			dataList = graduationStatJDBCService.studentRollCardInfoList_stu(condition);
		}else{
			dataList = graduationStatJDBCService.studentRollCardInfoList(condition);
		}
		Map<String,List<Map<String,Object>>> resumeMap   = new HashMap<String, List<Map<String,Object>>>();
		Map<String,List<Map<String,Object>>> ralation_f  = new HashMap<String, List<Map<String,Object>>>();
		Map<String,List<Map<String,Object>>> ralation_s  = new HashMap<String, List<Map<String,Object>>>();

		StringBuilder studentBaseInfoIds  				 = new StringBuilder();
		StringBuilder studentInfoIds      				 = new StringBuilder();
		//获取学籍ID，学生基础数据ID，转换数据字典值、日期格式，加入异动记录
		for (Map<String,Object> m : dataList) {
			//加一个判断 图片文件是否存在
			String imageRootPath = CacheAppManager.getSysConfigurationByCode("web.uploadfile.rootpath").getParamValue()+"common"+File.separator+"students";
			if(null!=m.get("photopath")){
				String photopath = m.get("photopath").toString();
				File file = null;
				if (photopath.startsWith("/")) {
					file   = new File(imageRootPath+photopath);
				}else {
					file   = new File(imageRootPath+File.separator+photopath);
				}
				if(!file.exists()){
					m.put("photopath", null);
				}
			}
			if(null!=m.get("recruitPhotoPath")){
				String recruitPhotoPath = m.get("recruitPhotoPath").toString();
				File file = null;
				if (recruitPhotoPath.startsWith("/")) {
					file   = new File(imageRootPath+recruitPhotoPath);
				}else {
					file   = new File(imageRootPath+File.separator+recruitPhotoPath);
				}
				if(!file.exists()){
					m.put("recruitPhotoPath", null);
				}
			}
			studentBaseInfoIds.append(",'"+m.get("basestudentid").toString()+"'");
			studentInfoIds.append(",'"+m.get("studentid").toString()+"'");
			m.put("grade", m.get("gradename").toString().replace("级", ""));
			m.put("sex", null==m.get("sex")?"":JstlCustomFunction.dictionaryCode2Value("CodeSex",m.get("sex").toString()));
			m.put("marriage", null==m.get("marriage")?"":JstlCustomFunction.dictionaryCode2Value("CodeMarriage",m.get("marriage").toString()));
			m.put("nation", null==m.get("nation")?"":JstlCustomFunction.dictionaryCode2Value("CodeNation",m.get("nation").toString()));
			m.put("politics", null==m.get("politics")?"":JstlCustomFunction.dictionaryCode2Value("CodePolitics",m.get("politics").toString()));
			//if(m.get("gdId")!=null && ExStringUtils.isNotBlank(m.get("gdId").toString())){//毕业数据
				m.put("graduatedate", null==m.get("graduatedate")?"":ExDateUtils.formatDateStr((Date)m.get("graduatedate"),ExDateUtils.PATTREN_DATE_CN));
				m.put("graduateResult", "Y".equals(m.get("graduatetype"))?"":JstlCustomFunction.dictionaryCode2Value("CodeGraduateType",ExStringUtils.toString(m.get("graduatetype"))));
			//}

			m.put("joinPartyDate", null==m.get("joinPartyDate")?"":ExDateUtils.formatDateStr((Date)m.get("joinPartyDate"),ExDateUtils.PATTREN_DATE_CN));
			m.put("organizationDate", null==m.get("organizationDate")?"":ExDateUtils.formatDateStr((Date)m.get("organizationDate"),ExDateUtils.PATTREN_DATE_CN));
			m.put("officename", null==m.get("officename")?"":m.get("officename"));
			m.put("title", null==m.get("title")?"":m.get("title"));

			Date bornDay = (Date)m.get("bornDay");
			Date currentDate = ExDateUtils.getToday();
			long age = ExDateUtils.dateDiff("year", currentDate, bornDay);
			m.put("age", age);
			m.put("bornDay", null==m.get("bornDay")?"":ExDateUtils.formatDateStr((Date)m.get("bornDay"),ExDateUtils.PATTREN_DATE_CN));

			if(ExStringUtils.isNotBlank(school)){
				//m.put("homeplace", m.get("homeplace")==null?"":m.get("homeplace").toString().replace(",", "   "));//籍贯
				//m.put("certnum", m.get("certnum")==null?"":ExStringUtils.appendSpace(m.get("certnum").toString()));
				//String gradate = m.get("graduatedate")==null?"":m.get("graduatedate")+"  毕 业  ";
				String graschool = m.get("graduateschool")==null?"":(String)m.get("graduateschool")+"  ";
				String gramajor = m.get("graduatemajor")==null?"":m.get("graduatemajor")+"  专 业";
				m.put("graduateschoolinfo", graschool+gramajor);
			}
			/**
			 * 入学日期
			 * 先入学日期是可以在添加年级里为该年级添加一个默认入学日期，也可以在学籍信息管理里为单个学生修改入学日期
			 * 如果学生未设置入学日期就用该学生所处年级的默认入学日期
			 */
			String indate = null == m.get("stuindate") ? ( null == m.get("gindate") ? "" : m.get("gindate")+"" ) : m.get("stuindate")+"" ;
			if(ExStringUtils.isNotBlank(indate)){
				try {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date date = sdf.parse(indate);
					m.put("entranceTime", ExDateUtils.formatDateStr(date,ExDateUtils.PATTREN_DATE_CN));
				} catch (Exception e) {
					m.put("entranceTime", "");
				}
			}else{
				m.put("entranceTime", "");
			}

			//学习形式
			m.put("teachtype", JstlCustomFunction.dictionaryCode2Value("CodeTeachingType", m.get("TEACHTYPE")+""));

			String majorName       				   = null==m.get("majorname")?"":m.get("majorname").toString();
			majorName = majorName.replaceAll("A", "").replaceAll("B", "");
			/*
			int indexA             				   = majorName.indexOf("A");
			int indexB             				   = majorName.indexOf("B");
			if (indexA>0||indexB>0) {
				majorName          				   = indexA>0?majorName.substring(0,indexA):majorName.substring(0,indexB);
			}*/
			m.put("majorname", majorName);

			StudentInfo stuInfo 				   = studentInfoService.get(m.get("studentid").toString());

			List<StuChangeInfo> changeList  = stuChangeInfoService.findByHql(" from "+StuChangeInfo.class.getSimpleName()+" sc where sc.isDeleted=? and sc.finalAuditStatus=? and sc.studentInfo.resourceid = ? "
					,0,Constants.BOOLEAN_YES,stuInfo.getResourceid());
			//Map<String,StuChangeInfo> changeRecord = stuChangeInfoService.findStudentChangeRecord(stuInfo.getResourceid());

//			StuChangeInfo majorChange              = changeRecord.get("majorChange");
//			StuChangeInfo brSchoolChange           = changeRecord.get("brSchoolChange");
//			StuChangeInfo otherChange              = changeRecord.get("otherChange");

			/**
			 * 由于在jasper用SubReport嵌套jasper 先没有解决方案  暂时学籍异动只显示6天数据
			 */
			if(null != changeList && changeList.size()>0){
				int i = 0;
				StringBuilder changeInfo = new StringBuilder();
				for(StuChangeInfo stu : changeList){
					i++;
					String changeType = JstlCustomFunction.dictionaryCode2Value("CodeStudentStatusChange",stu.getChangeType());
					String reason = stu.getReason();
					String date = null != stu.getAuditDate() ? ExDateUtils.formatDateStr(stu.getAuditDate(),ExDateUtils.PATTREN_DATE_CN):"";
					m.put("changeType_"+i, changeType);//异动类型
					m.put("reason_"+i, reason);//异动原因
					m.put("date_"+i, date);//审核时间
					changeInfo.append(changeType+"，"+date+"，"+reason+"；");
					if(i%2==0) {
						changeInfo.append("<br>");
					}
				}
				m.put("changeInfo", ExStringUtils.replaceLast(changeInfo.toString(),"；","。"));
			}

//			int i = 0;
//			if (null!=majorChange){
//				i++;
//				m.put("changeType_"+i, JstlCustomFunction.dictionaryCode2Value("CodeStudentStatusChange",majorChange.getChangeType()));//异动类型
//				m.put("reason_"+i, majorChange.getReason());//异动原因
//				m.put("date_"+i, null != majorChange.getAuditDate() ? ExDateUtils.formatDateStr(majorChange.getAuditDate(),ExDateUtils.PATTREN_DATE_CN):"");//审核时间
//				m.put("majorChange",(null!=majorChange.getAuditDate()?ExDateUtils.formatDateStr(majorChange.getAuditDate(),ExDateUtils.PATTREN_DATE_CN):"")+" 从 "+(null!=majorChange.getChangeBeforeTeachingGuidePlan()?majorChange.getChangeBeforeTeachingGuidePlan().getTeachingPlan().getMajor().getMajorName():"")+" 转至 "+stuInfo.getMajor().getMajorName());
//			}
//			if (null!=brSchoolChange){
//				i++;
//				m.put("changeType_"+i, JstlCustomFunction.dictionaryCode2Value("CodeStudentStatusChange",brSchoolChange.getChangeType()));//异动类型
//				m.put("reason_"+i, brSchoolChange.getReason());//异动原因
//				m.put("date_"+i, null != brSchoolChange.getAuditDate() ? ExDateUtils.formatDateStr(brSchoolChange.getAuditDate(),ExDateUtils.PATTREN_DATE_CN):"");//审核时间
//				m.put("brSchoolChange",(null!=brSchoolChange.getAuditDate()?ExDateUtils.formatDateStr(brSchoolChange.getAuditDate(),ExDateUtils.PATTREN_DATE_CN):"")+" 从 "+(null!=brSchoolChange.getChangeBeforeBrSchool()?brSchoolChange.getChangeBeforeBrSchool().getUnitName():"")+" 转至 "+(null!=brSchoolChange.getChangeBrschool()?brSchoolChange.getChangeBrschool().getUnitName():""));
//			}
//			if (null!=otherChange){
//				i++;
//				m.put("changeType_"+i, JstlCustomFunction.dictionaryCode2Value("CodeStudentStatusChange",otherChange.getChangeType()));//异动类型
//				m.put("reason_"+i, otherChange.getReason());//异动原因
//				m.put("date_"+i, null != otherChange.getAuditDate() ? ExDateUtils.formatDateStr(otherChange.getAuditDate(),ExDateUtils.PATTREN_DATE_CN):"");//审核时间
//				m.put("otherChange","异动类型："+JstlCustomFunction.dictionaryCode2Value("CodeStudentStatusChange", otherChange.getChangeType())+" 原因: "+ExStringUtils.trimToEmpty(otherChange.getReason())+" 审核时间:"+(null!=otherChange.getAuditDate()?ExDateUtils.formatDateStr(otherChange.getAuditDate(),ExDateUtils.PATTREN_DATE_CN):""));
//			}
			ExStringUtils.convert2BlankForMap(m);
		}
		String baseInfoIds 				  = studentBaseInfoIds.length()>1?studentBaseInfoIds.toString().substring(1):"";
		if (ExStringUtils.isNotBlank(baseInfoIds) && "standard".equals(school)) {
			List<Map<String,Object>> resumes  = baseSupportJdbcDao.getBaseJdbcTemplate().findForList(" select res.studentid,res.startyear||'年'||res.startmonth||'月 至 '||res.endyear||'年'||res.endmonth||'月' timeArea ,res.company,res.title from EDU_BASE_STUDENTRESUME res where res.isdeleted = ? and res.studentid in("+baseInfoIds+") order by res.startyear ", new Object[]{0});
			List<Map<String,Object>> ralation = baseSupportJdbcDao.getBaseJdbcTemplate().findForList(" select ral.studentbaseinfoid,ral.ralationtype,ral.ralation,ral.name,nvl(ral.gender,'0')gender,ral.workplace,ral.contact from edu_base_personanralation ral where ral.isdeleted = ? and ral.studentbaseinfoid in("+baseInfoIds+") ", new Object[]{0});
			//组装个人简历
			for (Map<String,Object> m : resumes) {
				List<Map<String,Object>> stuRes  = null;
				String studentId                 = m.get("studentid").toString();
				if (resumeMap.containsKey(studentId)) {
					stuRes                       = resumeMap.get(studentId);
				}else {
					stuRes                       = new ArrayList<Map<String,Object>>();
				}
				if (stuRes.size()<5) {
					stuRes.add(m);
					resumeMap.put(studentId,stuRes);
				}
			}
			//组装家庭关系、社会关系
			for (Map<String,Object> m : ralation) {
				List<Map<String,Object>> stuRal  = new ArrayList<Map<String,Object>>();
				String studentId                 = m.get("studentbaseinfoid").toString();
				String ralationtype              = m.get("ralationtype").toString();
				if (PersonalRalation.RALATIONTYPE_F.equals(ralationtype)) {
					m.put("gender", JstlCustomFunction.dictionaryCode2Value("CodeSex", m.get("gender").toString()));
					m.put("ralation", JstlCustomFunction.dictionaryCode2Value("CodeFamilyRalation", m.get("ralation").toString()));
					if (ralation_f.containsKey(studentId)) {
						stuRal = ralation_f.get(studentId);
					}
					if (stuRal.size()<5) {
						stuRal.add(m);
					}
					ralation_f.put(studentId,stuRal);
				}
				if (PersonalRalation.RALATIONTYPE_S.equals(ralationtype)) {
					m.put("ralation", JstlCustomFunction.dictionaryCode2Value("CodeSocialRalation", m.get("ralation").toString()));
					if (ralation_s.containsKey(studentId)) {
						stuRal = ralation_s.get(studentId);
					}
					if (stuRal.size()<5) {
						stuRal.add(m);
					}
					ralation_s.put(studentId,stuRal);
				}
			}
			//加入家庭关系、社会关系
			for (Map<String,Object> m : dataList) {
				List<Map<String,Object>> stuRes = resumeMap.get(m.get("basestudentid").toString());
				List<Map<String,Object>> ral_f  = ralation_f.get(m.get("basestudentid").toString());
				List<Map<String,Object>> ral_s  = ralation_s.get(m.get("basestudentid").toString());
				if (null!=stuRes) {
					for (int i = 0; i < stuRes.size(); i++) {
						m.put("timeArea_"+(i+1),stuRes.get(i).get("timeArea"));
						m.put("company_"+(i+1),stuRes.get(i).get("company"));
						m.put("title_"+(i+1),stuRes.get(i).get("title"));
					}
				}
				if (null!=ral_f) {
					for (int i = 0; i < ral_f.size(); i++) {
						m.put("name_f_"+(i+1),ral_f.get(i).get("name"));
						m.put("gender_f_"+(i+1),ral_f.get(i).get("gender"));
						m.put("ralation_f_"+(i+1),ral_f.get(i).get("ralation"));
						m.put("workplace_f_"+(i+1),ral_f.get(i).get("workplace"));
						m.put("title_f_"+(i+1),ral_f.get(i).get("title"));
						m.put("contact_f_"+(i+1),ral_f.get(i).get("contact"));
					}
				}
				if (null!=ral_s) {
					for (int i = 0; i < ral_s.size(); i++) {
						m.put("name_s_"+(i+1),ral_s.get(i).get("name"));
						m.put("ralation_s_"+(i+1),ral_s.get(i).get("ralation"));
						m.put("workplace_s_"+(i+1),ral_s.get(i).get("workplace"));
						m.put("contact_s_"+(i+1),ral_s.get(i).get("contact"));
					}
				}
			}
		}
		return dataList;
	}

	/**
	 * 学籍卡双面打印导出
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/roll/studentCardTwosided/printAndExport.html")
	public void rollCardTwosidedPrintAndExport(HttpServletRequest request,HttpServletResponse response){
		String type            = ExStringUtils.trimToEmpty(request.getParameter("type"));//1正面2反面
		String flag            = ExStringUtils.trimToEmpty(request.getParameter("flag"));// 打印/导出
		String entranceTime=ExStringUtils.trimToEmpty(request.getParameter("entranceTime"));
		try {
			Map<String,Object> map = checkStudentCardParameters(request);

			List<Map<String,Object>> dataList 	  = getStudentRollCardTwosidedDatas(map);
			if(dataList.size()>0 && ExStringUtils.isEmpty((String)dataList.get(0).get("entranceTime"))){
				dataList.get(0).put("entranceTime",entranceTime);
			}

			JRMapCollectionDataSource dataSource  = new JRMapCollectionDataSource(dataList);
			String fileName = "studentInfoCardsTwosided.jasper";
			if (ExStringUtils.isNotBlank(type) && "2".equals(type)) {
				fileName = "studentInfoCardsTwosided2.jasper";
				Collections.reverse(dataList);//反面打印需要倒序
			}
			String jasperFile     				  = URLDecoder.decode(request.getSession().getServletContext().getRealPath(File.separator+"reports"+
					File.separator+"graduation"+File.separator+fileName),"utf-8");
			map.put("SUBREPORT_DIR", URLDecoder.decode(request.getSession().getServletContext().getRealPath(File.separator+"reports"+
					File.separator+"graduation")+File.separator,"utf-8"));
			map.put("imageRootPath",CacheAppManager.getSysConfigurationByCode("web.uploadfile.rootpath").getParamValue()+"common"+File.separator+"students");
//				map.put("unitname", CacheAppManager.getSysConfigurationByCode("graduateData.schoolName").getParamValue()); //学校
			map.put("cardtitle", CacheAppManager.getSysConfigurationByCode("graduateData.schoolName").getParamValue()+CacheAppManager.getSysConfigurationByCode("graduateData.transactType").getParamValue()); //标题
			map.put("reportRootPath", request.getSession().getServletContext().getRealPath("/reports/graduation"));
			map.put("printDate", ExDateUtils.formatDateStr(ExDateUtils.getCurrentDateTime(), "yyyy年MM月dd日"));

			JasperPrint jasperPrint               = JasperFillManager.fillReport(jasperFile, map, dataSource); // 填充报表
			setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
			if (null!=jasperPrint) {
				if ("print".equals(flag)) {
					renderStream(response, jasperPrint);
				}else {
					GUIDUtils.init();
					String filePath        		  = getDistfilepath()+File.separator+GUIDUtils.buildMd5GUID(false)+".pdf";
					JRPdfExporter exporter		  = new JRPdfExporter();
					exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
					exporter.setParameter(JRXlsExporterParameter.OUTPUT_FILE_NAME,filePath);

					//exporter.setParameter(JRXlsExporterParameter.OUTPUT_FILE_NAME,"D:\\aa.pdf");
					exporter.exportReport();
					String singlePersonInfo = "";
					if(dataList.size()==1){
						singlePersonInfo = null!=dataList.get(0).get("studyno")?dataList.get(0).get("studyno").toString():"";
					}
					downloadFile(response,singlePersonInfo+"学籍卡双面.pdf",filePath,true);
				}
			}else {
				renderHtml(response,"<script>alert('缺少打印数据！')</script>");
			}

		} catch (Exception e) {
			logger.error("学籍卡双面打印出错：{}"+e.fillInStackTrace());
			renderHtml(response,"<script>alert('学籍卡双面打印出错："+e.getMessage()+"')</script>");
		}
	}


	/**
	 * 检查学籍卡所用到的参数，暂用于学籍卡打印相关
	 * @param request
	 * @return
	 * @throws Exception
	 * @author Git
	 */
	private Map<String, Object> checkStudentCardParameters(HttpServletRequest request)
			throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();

		String name			   = ExStringUtils.trimToEmpty(request.getParameter("name"));//姓名
		String unitId		   = ExStringUtils.trimToEmpty(request.getParameter("unitId"));
		String branchSchool	   = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));//学习中心
		String majorId         = ExStringUtils.trimToEmpty(request.getParameter("majorId"));
		String major	       = ExStringUtils.trimToEmpty(request.getParameter("major"));//专业
		String classicId	   = ExStringUtils.trimToEmpty(request.getParameter("classicId"));
		String classic	       = ExStringUtils.trimToEmpty(request.getParameter("classic"));//层次

		String stuStatus       = ExStringUtils.trimToEmpty(request.getParameter("stuStatus"));//学籍状态
		String matriculateNoticeNo = ExStringUtils.trimToEmpty(request.getParameter("matriculateNoticeNo"));
		String studyNo		   = ExStringUtils.trimToEmpty(request.getParameter("studyNo"));//学号
		String certNum		   = ExStringUtils.trimToEmpty(request.getParameter("certNum"));//身份证号
		String gradeId	  	   = ExStringUtils.trimToEmpty(request.getParameter("gradeId"));
		String grade		   = ExStringUtils.trimToEmpty(request.getParameter("grade"));//年级graduateDate
		String graduateDate    = ExStringUtils.trimToEmpty(request.getParameter("graduateDate"));//毕业日期
		String resourceid      = ExStringUtils.trimToEmpty(request.getParameter("resourceid"));//资源ID
		String confirmGraduateDateb = ExStringUtils.trimToEmpty(request.getParameter("gl_confirmGraduateDateb"));//确认毕业日期始
		String confirmGraduateDatee = ExStringUtils.trimToEmpty(request.getParameter("gl_confirmGraduateDatee"));//确认毕业日期终

		//修改BUG 打印学籍表报错 2014-05-27 15:51:15
		String studentids		  = ExStringUtils.trimToEmpty(request.getParameter("studentids"));//要打印的学生id
		String fromStudentRoll 	  = ExStringUtils.trimToEmpty(request.getParameter("fromStudentRoll"));//标识学籍信息管理模块打印
		String classesId		  = ExStringUtils.trimToEmpty(request.getParameter("classes"));
		String classes            = ExStringUtils.trimToEmpty(request.getParameter("classes"));//班级
		String rollCard			  = ExStringUtils.trimToEmpty(request.getParameter("rollCard"));
		String entranceFlag		  = ExStringUtils.trimToEmpty(request.getParameter("entranceFlag"));
		String degreeStatus       = ExStringUtils.trimToEmpty(request.getParameter("degreeStatus"));//学位状态
		String accountStatus = "";
		if(stuStatus.contains("a")){
			accountStatus="1";
			stuStatus="11";//去掉学籍中有关帐号状态的标记
		}else if(stuStatus.contains("b")){
			accountStatus="0";
			stuStatus="11";//去掉学籍中有关帐号状态的标记
		}

		if (ExStringUtils.isNotBlank(branchSchool)){
			map.put("branchSchool",branchSchool);
		}else if (ExStringUtils.isNotBlank(unitId)){
			map.put("branchSchool",unitId);
		}
		if (ExStringUtils.isNotBlank(major)){
			map.put("major",major);
		}else if (ExStringUtils.isNotBlank(majorId)) {
			map.put("major",majorId);
		}
		if (ExStringUtils.isNotBlank(classic)) {
			map.put("classic",classic);
		}else if (ExStringUtils.isNotBlank(classicId)) {
			map.put("classic",classicId);
		}
		if (ExStringUtils.isNotBlank(stuStatus)) {
			map.put("stuStatus",stuStatus);
		}
		if (ExStringUtils.isNotBlank(rollCard)) {
			map.put("rollCard",rollCard);
		}
		if (ExStringUtils.isNotBlank(entranceFlag)) {
			map.put("entranceFlag",entranceFlag);
		}
		if (ExStringUtils.isNotBlank(degreeStatus)) {
			map.put("degreeStatus",degreeStatus);
		}
		if(ExStringUtils.isNotBlank(accountStatus)) {
			map.put("accountStatus", accountStatus);
		}
		if (ExStringUtils.isNotBlank(classes)) {
			map.put("classes",classes);
		}else if (ExStringUtils.isNotBlank(classesId)) {
			map.put("classes",classesId);
		}
		if (ExStringUtils.isNotBlank(studyNo)) {
			map.put("studyNo",studyNo);
		}else if (ExStringUtils.isNotBlank(matriculateNoticeNo)) {
			map.put("studyNo",matriculateNoticeNo);
		}
		if(ExStringUtils.isNotBlank(certNum)) {
			map.put("certNum",certNum);
		}
		if (ExStringUtils.isNotBlank(grade)) {
			map.put("grade",grade);
		}else if (ExStringUtils.isNotBlank(gradeId)) {
			map.put("grade",gradeId);
		}
		if (ExStringUtils.isNotBlank(graduateDate)) {
			map.put("graduateDate",graduateDate);
		}
		if (ExStringUtils.isNotBlank(resourceid)) {
			map.put("resourceid",resourceid);
		}
		if (ExStringUtils.isNotBlank(confirmGraduateDateb)) {
			map.put("confirmGraduateDateb",confirmGraduateDateb);
		}
		if (ExStringUtils.isNotBlank(confirmGraduateDatee)) {
			map.put("confirmGraduateDatee",confirmGraduateDatee);
		}
		if (ExStringUtils.isNotBlank(studentids)) {
			map.put("studentids",studentids);
		}
		if (ExStringUtils.isNotBlank(fromStudentRoll)) {
			map.put("fromStudentRoll",fromStudentRoll);
		}

		if (ExStringUtils.isNotBlank(name)){
			name = URLDecoder.decode(name,"utf-8");
			map.put("name",name);
		}

		return map;
	}


	/**
	 * 根据查询条件和勾选数据，批量 导出 双面学籍卡
	 * @param request
	 * @param response
	 * @author Git
	 */
	@RequestMapping("/edu3/roll/studentCardTwosided/downloadPDF.html")
	public void studentCardTwosidedDownloadPDF(HttpServletRequest request, HttpServletResponse response) {
		String isPdf = ExStringUtils.trimToEmpty(request.getParameter("isPdf"));
		isPdf = "Y";
		try {
			String entranceTime = ExStringUtils.trimToEmpty(request.getParameter("entranceTime"));

			Map<String, Object> map = checkStudentCardParameters(request);

			// dataList 里面 有正、反面 全部数据
			List<Map<String, Object>> dataList = getStudentRollCardTwosidedDatas(map);
			if (dataList.size() > 0 && ExStringUtils.isEmpty((String) dataList.get(0).get("entranceTime"))) {
				dataList.get(0).put("entranceTime", entranceTime);
			}

			// 准备
			map.put("imageRootPath",
					CacheAppManager.getSysConfigurationByCode("web.uploadfile.rootpath").getParamValue() + "common"
							+ File.separator + "students");
			map.put("SUBREPORT_DIR",
					URLDecoder.decode(
							request.getSession().getServletContext().getRealPath(
									File.separator + "reports" + File.separator + "graduation") + File.separator,
					"utf-8"));
			map.put("imageRootPath",
					CacheAppManager.getSysConfigurationByCode("web.uploadfile.rootpath").getParamValue() + "common"
							+ File.separator + "students");
			// map.put("unitname",
			// CacheAppManager.getSysConfigurationByCode("graduateData.schoolName").getParamValue());
			// //学校
			map.put("cardtitle", CacheAppManager.getSysConfigurationByCode("graduateData.schoolName").getParamValue()
					+ CacheAppManager.getSysConfigurationByCode("graduateData.transactType").getParamValue()); // 标题
			map.put("reportRootPath", request.getSession().getServletContext().getRealPath("/reports/graduation"));
			map.put("printDate", ExDateUtils.formatDateStr(ExDateUtils.getCurrentDateTime(), "yyyy年MM月dd日"));

			// 排序打印的顺序
			String fileName = null;
			String jasperFile = null;
			JasperPrint _jasperPrint = null;
			JasperPrint jasperPrint = null;
			JRMapCollectionDataSource dataSource = null;
			List<Map<String, Object>> _tmpList = null;
			
			int count = 0;
			for (Map<String, Object> _map : dataList) {
				// 正反面循环
				for (int i = 0; i < 2; i++) {
					if (i == 0) {
						fileName = "studentInfoCardsTwosided.jasper";
					} else {
						fileName = "studentInfoCardsTwosided2.jasper";
					}
					_tmpList = new ArrayList<Map<String, Object>>();
					_tmpList.add(_map);
					dataSource = new JRMapCollectionDataSource(_tmpList);

					jasperFile = URLDecoder.decode(request.getSession().getServletContext().getRealPath(
							File.separator + "reports" + File.separator + "graduation" + File.separator + fileName),
							"utf-8");
					_jasperPrint = JasperFillManager.fillReport(jasperFile, map, dataSource); // 填充报表

					//第一页为封面
					if (count == 0) {
						jasperPrint = _jasperPrint;
						count++;
					} else {//后面的页为内容
						@SuppressWarnings("unchecked")
						List<JRBasePrintPage> pages = (List<JRBasePrintPage>) _jasperPrint.getPages();
						for (JRBasePrintPage page : pages) {
							jasperPrint.addPage(page);
						}
					}
				}
			}
			setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
			//构建实体pdf文件
			if (null != jasperPrint) {
				if (ExStringUtils.isNotEmpty(isPdf)) {
					GUIDUtils.init();
					String filePath = getDistfilepath() + File.separator + GUIDUtils.buildMd5GUID(false) + ".pdf";
					JRPdfExporter exporter = new JRPdfExporter();
					exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
					exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, "UTF-8");
					exporter.setParameter(JRXlsExporterParameter.OUTPUT_FILE_NAME, filePath);
					exporter.exportReport();
					downloadFile(response, "双面学籍卡.pdf", filePath, true);
				} else {
					renderStream(response, jasperPrint);
				}
			} else {
				renderHtml(response, "<script>alert('缺少数据！')</script>");
			}
		} catch (Exception e) {
			logger.error("双面学籍卡下载出错：{}" + e.fillInStackTrace());
			renderHtml(response, "<script>alert('双面学籍卡下载出错：" + e.getMessage() + "')</script>");
		}
	}

	/**
	 * 组装学籍卡双面中的数据
	 * @param condition
	 * @return
	 */
	private List<Map<String,Object>> getStudentRollCardTwosidedDatas(Map<String, Object> condition) throws Exception{
		//StudentInfo studentInfo 	    = studentInfoService.getStudentInfoByUser(curUser);
		//String name = studentInfo.getStudentBaseInfo().getName();
		//List<Map<String,Object>> dataList 			     =  graduationStatJDBCService.studentCardInfo(condition,name);
		List<Map<String,Object>> dataList 			     = new ArrayList<Map<String,Object>>(0);
		if(condition.containsKey("from")&&"studentinfo".equals(condition.get("from"))){
			dataList = graduationStatJDBCService.studentRollCardInfoList_stu(condition);
		}else{
			dataList = graduationStatJDBCService.studentRollCardInfoList(condition);
		}
		Map<String,List<Map<String,Object>>> resumeMap   = new HashMap<String, List<Map<String,Object>>>();
		Map<String,List<Map<String,Object>>> ralation_f  = new HashMap<String, List<Map<String,Object>>>();
		Map<String,List<Map<String,Object>>> ralation_s  = new HashMap<String, List<Map<String,Object>>>();

		StringBuilder studentBaseInfoIds  				 = new StringBuilder();
		StringBuilder studentInfoIds      				 = new StringBuilder();
		//获取学籍ID，学生基础数据ID，转换数据字典值、日期格式，加入异动记录
		for (Map<String,Object> m : dataList) {
			//加一个判断 图片文件是否存在
			String imageRootPath = CacheAppManager.getSysConfigurationByCode("web.uploadfile.rootpath").getParamValue()+"common"+File.separator+"students";
			if(null!=m.get("photopath")){
				String photopath = m.get("photopath").toString();
				if(ExStringUtils.isBlank(photopath)){
					photopath = m.get("recruitPhotoPath").toString();
				}
				File file = null;
				if (photopath.startsWith("/")) {
					file   = new File(imageRootPath+photopath);
				}else {
					file   = new File(imageRootPath+File.separator+photopath);
				}
				if(!file.exists()){
					m.put("photopath", null);
				}
			}
			if(null!=m.get("certPhotoPath")){
				String certPhotoPath = m.get("certPhotoPath").toString();

				File file = null;
				if (certPhotoPath.startsWith("/")) {
					file   = new File(imageRootPath+certPhotoPath);
				}else {
					file   = new File(imageRootPath+File.separator+certPhotoPath);
				}
				if(!file.exists()){
					m.put("certPhotoPath", null);
				}
			}
			if(null!=m.get("certPhotoPathReverse")){
				String certPhotoPathReverse = m.get("certPhotoPathReverse").toString();

				File file = null;
				if (certPhotoPathReverse.startsWith("/")) {
					file   = new File(imageRootPath+certPhotoPathReverse);
				}else {
					file   = new File(imageRootPath+File.separator+certPhotoPathReverse);
				}
				if(!file.exists()){
					m.put("certPhotoPathReverse", null);
				}
			}
			studentBaseInfoIds.append(",'"+m.get("basestudentid").toString()+"'");
			studentInfoIds.append(",'"+m.get("studentid").toString()+"'");
			m.put("sex", null==m.get("sex")?"":JstlCustomFunction.dictionaryCode2Value("CodeSex",m.get("sex").toString()));
			m.put("marriage", null==m.get("marriage")?"":JstlCustomFunction.dictionaryCode2Value("CodeMarriage",m.get("marriage").toString()));
			m.put("nation", null==m.get("nation")?"":JstlCustomFunction.dictionaryCode2Value("CodeNation",m.get("nation").toString()));
			m.put("politics", null==m.get("politics")?"":JstlCustomFunction.dictionaryCode2Value("CodePolitics",m.get("politics").toString()));
			m.put("graduatedate", null==m.get("graduatedate")?"":ExDateUtils.formatDateStr((Date)m.get("graduatedate"),ExDateUtils.PATTREN_DATE_CN));
			m.put("officename", null==m.get("officename")?"":m.get("officename"));
			m.put("title", null==m.get("title")?"":m.get("title"));
			m.put("mobile", null==m.get("mobile")?"":m.get("mobile"));
			m.put("homephone", null==m.get("homephone")?"":m.get("homephone"));
			m.put("homeaddress", null==m.get("homeaddress")?"":m.get("homeaddress"));
			m.put("homezipcode", null==m.get("homezipcode")?"":m.get("homezipcode"));
			m.put("nameused", null==m.get("nameused")?"":m.get("nameused"));
			String degreestatus = m.get("degreestatus") != null ? m.get("degreestatus").toString() : "";
			/*if ("Y".equals(degreestatus)) {
				m.put("degreestatus", "已获得");
			} else if ("N".equals(degreestatus)) {
				m.put("degreestatus", "未获得");
			} else if ("W".equals(degreestatus)) {
				m.put("degreestatus", "待审核");
			}*/
			if ("Y".equals(degreestatus)) {
				m.put("degreestatus", "授予学位");
			}else {
				m.put("degreestatus", " ");
			}
			Date bornDay = (Date)m.get("bornDay");
			Date currentDate = ExDateUtils.getToday();
			long age = ExDateUtils.dateDiff("year", currentDate, bornDay);
			m.put("age", age);
			m.put("bornDay", null==m.get("bornDay")?"":ExDateUtils.formatDateStr((Date)m.get("bornDay"),ExDateUtils.PATTREN_DATE_CN));

			/**
			 * 入学日期
			 * 先入学日期是可以在添加年级里为该年级添加一个默认入学日期，也可以在学籍信息管理里为单个学生修改入学日期
			 * 如果学生未设置入学日期就用该学生所处年级的默认入学日期
			 */
			String indate = null == m.get("stuindate") ? ( null == m.get("gindate") ? "" : m.get("gindate")+"" ) : m.get("stuindate")+"" ;
			if(ExStringUtils.isNotBlank(indate)){
				try {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date date = sdf.parse(indate);
					m.put("entranceTime", ExDateUtils.formatDateStr(date,ExDateUtils.PATTREN_DATE_CN));
				} catch (Exception e) {
					m.put("entranceTime", "");
				}
			}else{
				m.put("entranceTime", "");
			}

			//学习形式
			m.put("teachtype", JstlCustomFunction.dictionaryCode2Value("CodeTeachingType", m.get("teachtype")+""));

			String majorName       				   = null==m.get("majorname")?"":m.get("majorname").toString();
			majorName = majorName.replaceAll("A", "").replaceAll("B", "");
			Pattern pattern = Pattern.compile("\\[.+\\]");
		    Matcher matcher = pattern.matcher(majorName);
		    majorName = matcher.replaceAll("");
			/*
			int indexA             				   = majorName.indexOf("A");
			int indexB             				   = majorName.indexOf("B");
			if (indexA>0||indexB>0) {
				majorName          				   = indexA>0?majorName.substring(0,indexA):majorName.substring(0,indexB);
			}*/
			m.put("majorname", majorName);

			StudentInfo stuInfo 				   = studentInfoService.get(m.get("studentid").toString());

			m.put("examCertificateNo", stuInfo.getExamCertificateNo());
			m.put("classesname", stuInfo.getClasses().getClassname());
			int length = 14;
			//班级14个字换行  20151209的需求
			if (m.get("classesname").toString().length() > length) {
				String classesname = m.get("classesname").toString();
				classesname = classesname.substring(0, length) + "\r\n" + classesname.substring(length);
				m.put("classesname", classesname);
			}
			//专业14个字换行  20151209的需求
			if (m.get("majorname").toString().length() > length) {
				String majorname = m.get("majorname").toString();
				majorname = majorname.substring(0, length) + "\r\n" + majorname.substring(length);
				m.put("majorname", majorname);
			}
			length = 12;
			//教学点12个字换行  20151209的需求
			if (m.get("unitname").toString().length() > length) {
				String unitname = m.get("unitname").toString();
				unitname = unitname.substring(0, length) + "\r\n" + unitname.substring(length);
				m.put("unitname", unitname);
			}

			List<StuChangeInfo> changeList  = stuChangeInfoService.findByHql(" from "+StuChangeInfo.class.getSimpleName()+" sc where sc.isDeleted=? and sc.finalAuditStatus=? and sc.studentInfo.resourceid = ? "
					,0,Constants.BOOLEAN_YES,stuInfo.getResourceid());
			//Map<String,StuChangeInfo> changeRecord = stuChangeInfoService.findStudentChangeRecord(stuInfo.getResourceid());

//				StuChangeInfo majorChange              = changeRecord.get("majorChange");
//				StuChangeInfo brSchoolChange           = changeRecord.get("brSchoolChange");
//				StuChangeInfo otherChange              = changeRecord.get("otherChange");

			/**
			 * 由于在jasper用SubReport嵌套jasper 先没有解决方案  暂时学籍异动只显示6天数据
			 */
			if(null != changeList && changeList.size()>0){
				int i = 0;
				for(StuChangeInfo stu : changeList){
					i++;
					m.put("changeType_"+i, JstlCustomFunction.dictionaryCode2Value("CodeStudentStatusChange",stu.getChangeType()));//异动类型
					m.put("reason_"+i, stu.getReason());//异动原因
					m.put("date_"+i, null != stu.getAuditDate() ? ExDateUtils.formatDateStr(stu.getAuditDate(),ExDateUtils.PATTREN_DATE_CN):"");//审核时间
					m.put("grade_"+i, stu.getStudentInfo().getGrade().getGradeName());//年级
				}
			}

//				int i = 0;
//				if (null!=majorChange){
//					i++;
//					m.put("changeType_"+i, JstlCustomFunction.dictionaryCode2Value("CodeStudentStatusChange",majorChange.getChangeType()));//异动类型
//					m.put("reason_"+i, majorChange.getReason());//异动原因
//					m.put("date_"+i, null != majorChange.getAuditDate() ? ExDateUtils.formatDateStr(majorChange.getAuditDate(),ExDateUtils.PATTREN_DATE_CN):"");//审核时间
//					m.put("majorChange",(null!=majorChange.getAuditDate()?ExDateUtils.formatDateStr(majorChange.getAuditDate(),ExDateUtils.PATTREN_DATE_CN):"")+" 从 "+(null!=majorChange.getChangeBeforeTeachingGuidePlan()?majorChange.getChangeBeforeTeachingGuidePlan().getTeachingPlan().getMajor().getMajorName():"")+" 转至 "+stuInfo.getMajor().getMajorName());
//				}
//				if (null!=brSchoolChange){
//					i++;
//					m.put("changeType_"+i, JstlCustomFunction.dictionaryCode2Value("CodeStudentStatusChange",brSchoolChange.getChangeType()));//异动类型
//					m.put("reason_"+i, brSchoolChange.getReason());//异动原因
//					m.put("date_"+i, null != brSchoolChange.getAuditDate() ? ExDateUtils.formatDateStr(brSchoolChange.getAuditDate(),ExDateUtils.PATTREN_DATE_CN):"");//审核时间
//					m.put("brSchoolChange",(null!=brSchoolChange.getAuditDate()?ExDateUtils.formatDateStr(brSchoolChange.getAuditDate(),ExDateUtils.PATTREN_DATE_CN):"")+" 从 "+(null!=brSchoolChange.getChangeBeforeBrSchool()?brSchoolChange.getChangeBeforeBrSchool().getUnitName():"")+" 转至 "+(null!=brSchoolChange.getChangeBrschool()?brSchoolChange.getChangeBrschool().getUnitName():""));
//				}
//				if (null!=otherChange){
//					i++;
//					m.put("changeType_"+i, JstlCustomFunction.dictionaryCode2Value("CodeStudentStatusChange",otherChange.getChangeType()));//异动类型
//					m.put("reason_"+i, otherChange.getReason());//异动原因
//					m.put("date_"+i, null != otherChange.getAuditDate() ? ExDateUtils.formatDateStr(otherChange.getAuditDate(),ExDateUtils.PATTREN_DATE_CN):"");//审核时间
//					m.put("otherChange","异动类型："+JstlCustomFunction.dictionaryCode2Value("CodeStudentStatusChange", otherChange.getChangeType())+" 原因: "+ExStringUtils.trimToEmpty(otherChange.getReason())+" 审核时间:"+(null!=otherChange.getAuditDate()?ExDateUtils.formatDateStr(otherChange.getAuditDate(),ExDateUtils.PATTREN_DATE_CN):""));
//				}
		}
		String baseInfoIds 				  = studentBaseInfoIds.length()>1?studentBaseInfoIds.toString().substring(1):"";
		if (ExStringUtils.isNotBlank(baseInfoIds)) {
			List<Map<String,Object>> resumes  = baseSupportJdbcDao.getBaseJdbcTemplate().findForList(" select res.studentid,res.startyear||'年'||res.startmonth||'月 至 '||res.endyear||'年'||res.endmonth||'月' timeArea ,res.company,res.title from EDU_BASE_STUDENTRESUME res where res.isdeleted = ? and res.studentid in("+baseInfoIds+") order by res.startyear ", new Object[]{0});
			List<Map<String,Object>> ralation = baseSupportJdbcDao.getBaseJdbcTemplate().findForList(" select ral.studentbaseinfoid,ral.ralationtype,ral.ralation,ral.name,ral.workplace,ral.contact from edu_base_personanralation ral where ral.isdeleted = ? and ral.studentbaseinfoid in("+baseInfoIds+") ", new Object[]{0});
			//组装个人简历
			for (Map<String,Object> m : resumes) {
				List<Map<String,Object>> stuRes  = null;
				String studentId                 = m.get("studentid").toString();
				if (resumeMap.containsKey(studentId)) {
					stuRes                       = resumeMap.get(studentId);
				}else {
					stuRes                       = new ArrayList<Map<String,Object>>();
				}
				if (stuRes.size()<5) {
					stuRes.add(m);
					resumeMap.put(studentId,stuRes);
				}
			}
			//组装家庭关系、社会关系
			for (Map<String,Object> m : ralation) {
				List<Map<String,Object>> stuRal  = new ArrayList<Map<String,Object>>();
				String studentId                 = m.get("studentbaseinfoid").toString();
				String ralationtype              = m.get("ralationtype").toString();
				if (PersonalRalation.RALATIONTYPE_F.equals(ralationtype)) {
					m.put("ralation", JstlCustomFunction.dictionaryCode2Value("CodeFamilyRalation", m.get("ralation").toString()));
					if (ralation_f.containsKey(studentId)) {
						stuRal = ralation_f.get(studentId);
					}
					if (stuRal.size()<5) {
						stuRal.add(m);
					}
					ralation_f.put(studentId,stuRal);
				}
				if (PersonalRalation.RALATIONTYPE_S.equals(ralationtype)) {
					m.put("ralation", JstlCustomFunction.dictionaryCode2Value("CodeSocialRalation", m.get("ralation").toString()));
					if (ralation_s.containsKey(studentId)) {
						stuRal = ralation_s.get(studentId);
					}
					if (stuRal.size()<5) {
						stuRal.add(m);
					}
					ralation_s.put(studentId,stuRal);
				}
			}
			//加入家庭关系、社会关系
			for (Map<String,Object> m : dataList) {
				List<Map<String,Object>> stuRes = resumeMap.get(m.get("basestudentid").toString());
				List<Map<String,Object>> ral_f  = ralation_f.get(m.get("basestudentid").toString());
				List<Map<String,Object>> ral_s  = ralation_s.get(m.get("basestudentid").toString());
				if (null!=stuRes) {
					for (int i = 0; i < stuRes.size(); i++) {
						m.put("timeArea_"+(i+1),stuRes.get(i).get("timeArea"));
						m.put("company_"+(i+1),stuRes.get(i).get("company"));
						m.put("title_"+(i+1),stuRes.get(i).get("title"));
					}
				}
				if (null!=ral_f) {
					for (int i = 0; i < ral_f.size(); i++) {
						m.put("name_f_"+(i+1),ral_f.get(i).get("name"));
						m.put("ralation_f_"+(i+1),ral_f.get(i).get("ralation"));
						m.put("workplace_f_"+(i+1),ral_f.get(i).get("workplace"));
						m.put("contact_f_"+(i+1),ral_f.get(i).get("contact"));
					}
				}
				if (null!=ral_s) {
					for (int i = 0; i < ral_s.size(); i++) {
						m.put("name_s_"+(i+1),ral_s.get(i).get("name"));
						m.put("ralation_s_"+(i+1),ral_s.get(i).get("ralation"));
						m.put("workplace_s_"+(i+1),ral_s.get(i).get("workplace"));
						m.put("contact_s_"+(i+1),ral_s.get(i).get("contact"));
					}
				}
			}
		}

		return dataList;
	}

	/**
	 * 根据查询条件和勾选数据，批量 导出 学籍卡(安徽医)
	 * @param request
	 * @param response
	 * @author Git
	 */
	@RequestMapping("/edu3/roll/studentCardTwosided/downloadPDF1.html")
	public void studentCardTwosidedDownloadPDF1(HttpServletRequest request, HttpServletResponse response) {
		String isPdf = ExStringUtils.trimToEmpty(request.getParameter("isPdf"));
		isPdf = "Y";
//		String exportType   = ExStringUtils.trimToEmpty(request.getParameter("exportType"));//导出类型
		Map<String, Object> condition = new HashMap<String, Object>();
		try {
			String studentIds		   = ExStringUtils.trimToEmpty(request.getParameter("studentids"));//要打印的学生id
			JasperPrint jasperPrint = null; // 打印
			Map<String, Object> printParam = new HashMap<String, Object>();
			if(studentIds!=null&&!"".equals(studentIds)){//勾选导出
				studentIds  = ExStringUtils.replace(studentIds, ",", "','");
				studentIds  = "'"+studentIds+"'";
				condition.put("studentIds", studentIds);
			}else {
				getSchoolRollTableParam(request, condition);
			}
			// 获取要打印学籍表的学生
			List<Map<String,Object>> studentInfoList 	=  rollJDBCService.findStudentInfoByCondition(condition);
			if(ExCollectionUtils.isNotEmpty(studentInfoList)){
				// 报表irport模板路径
				String reportPath = File.separator+"reports"+File.separator+"studentinfo"+File.separator+"studentInfoCardsPDF_ahy.jasper";
				// 报表文件
				String reprotFile = URLDecoder.decode(request.getSession().getServletContext().getRealPath(reportPath),"utf-8");
				String schoolName = CacheAppManager.getSysConfigurationByCode("print.msg.schoolname").getParamValue();
				String schoolConnectName = CacheAppManager.getSysConfigurationByCode("graduateData.schoolConnectName").getParamValue();
				// 标题
				String title = schoolName + schoolConnectName;
				// logo路径
				String logoPath = CacheAppManager.getSysConfigurationByCode("web.scutlogo.path").getParamValue();
				int showOrder = 0;
				String _classesId = "";
				for(Map<String,Object> info : studentInfoList){
					printParam.clear();
					String classesId = (String)info.get("classesId");
					if(ExStringUtils.isNotEmpty(_classesId)){
						if(_classesId.equals(classesId)){
							showOrder++;
						}else {
							showOrder = 1;
							_classesId = classesId;
						}
					}else {
						showOrder = 1;
						_classesId = classesId;
					}
					printParam.put("title", title);
					printParam.put("logoPath", logoPath);
					// 学籍信息
					printParam.put("NO", showOrder+"");// 学籍表顺序号
					printParam.put("gradeName", ((BigDecimal)info.get("firstyear")).toString());// 年级
					printParam.put("majorName", (String)info.get("majorname"));// 专业名称
					printParam.put("teachingType", (String)info.get("teachingtype"));// 学习形式
					printParam.put("xz", Tools.eduyearTransfer((String)info.get("eduyear")));// 学制
					printParam.put("className", (String)info.get("classesname"));// 班级名称
					printParam.put("studentNo", (String)info.get("studyno"));// 学号
					printParam.put("studentName", (String)info.get("studentname"));// 学生名字
					printParam.put("gender", (String)info.get("gender"));// 性别
					printParam.put("birth", (String)info.get("bornday"));// 生日
					printParam.put("address", (String)info.get("contactaddress"));// 联系地址
					printParam.put("postcode", (String)info.get("contactzipcode"));// 邮编
					printParam.put("phone", (String)info.get("mobile"));// 联系电话
					printParam.put("indate", (String)info.get("indate"));// 入学日期
					printParam.put("graduateDate", (String)info.get("graduatedate"));// 毕业日期
					printParam.put("suspend", (String)info.get("suspInfo"));// 留级、休学信息
					printParam.put("return", (String)info.get("retInfo"));// 复学信息
					printParam.put("studentChangeInfo", (String)info.get("changeInfo"));// 学籍异动信息
					// 学生成绩信息处理
					List<Map<String, Object>> examResulsList = teachingJDBCService.findFinalExamResults((String)info.get("stuInfoId"));
					List<ExamResultsInfoVO> examResultsInfoList = studentInfoService.handleExamResultInfo(examResulsList);
					JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(examResultsInfoList);
					if (null!=jasperPrint&&jasperPrint.getPages().size()>0) {
						JasperPrint jasperPage = JasperFillManager.fillReport(reprotFile, printParam, dataSource); // 填充报表
						List jsperPageList=jasperPage.getPages();
						for (int j = 0; j < jsperPageList.size(); j++) {
							jasperPrint.addPage((JRPrintPage) jsperPageList.get(j));
						}
						jasperPage = null;//清除临时报表的内存占用
					}else {
						jasperPrint = JasperFillManager.fillReport(reprotFile, printParam, dataSource); // 填充报表
					}
				}
			}
			setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
			//构建实体pdf文件
			if (null != jasperPrint) {
				if (ExStringUtils.isNotEmpty(isPdf)) {
					GUIDUtils.init();
					String filePath = getDistfilepath() + File.separator + GUIDUtils.buildMd5GUID(false) + ".pdf";
					JRPdfExporter exporter = new JRPdfExporter();
					exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
					exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, "UTF-8");
					exporter.setParameter(JRXlsExporterParameter.OUTPUT_FILE_NAME, filePath);
					exporter.exportReport();
					downloadFile(response, "学籍卡.pdf", filePath, true);
				} else {
					renderStream(response, jasperPrint);
				}
			} else {
				renderHtml(response, "<script>alert('缺少数据！')</script>");
			}
		} catch (Exception e) {
			logger.error("学籍卡下载出错：{}" + e.fillInStackTrace());
			renderHtml(response, "<script>alert('学籍卡下载出错：" + e.getMessage() + "')</script>");
		}
	}
	/**
	 * 获取导出学籍表（PDF）的参数(安徽医)
	 * @param request
	 * @param condition
	 */
	private void getSchoolRollTableParam(HttpServletRequest request, Map<String, Object> condition) {
		String studentIds = ExStringUtils.trimToEmpty(request.getParameter("studentIds"));
		String name =  ExStringUtils.trimToEmpty(request.getParameter("name"));
		String unitId = ExStringUtils.trimToEmpty(request.getParameter("unitId"));
		String classicId = ExStringUtils.trimToEmpty(request.getParameter("classicId"));
		String majorId = ExStringUtils.trimToEmpty(request.getParameter("majorId"));
		String gradeId = ExStringUtils.trimToEmpty(request.getParameter("gradeId"));
		String classesId = ExStringUtils.trimToEmpty(request.getParameter("classesId"));
		String studentNo = ExStringUtils.trimToEmpty(request.getParameter("studentNo"));
		String matriculateNoticeNo = ExStringUtils.trimToEmpty(request.getParameter("matriculateNoticeNo"));
		String rollCard = ExStringUtils.trimToEmpty(request.getParameter("rollCard"));
		String certNum = ExStringUtils.trimToEmpty(request.getParameter("certNum"));
		String stuStatus = ExStringUtils.trimToEmpty(request.getParameter("stuStatus"));
		String entranceFlag = ExStringUtils.trimToEmpty(request.getParameter("entranceFlag"));

		if(ExStringUtils.isNotEmpty(studentIds)){
			condition.put("studentIds", Arrays.asList(studentIds.split(",")));
		} else {
			if(ExStringUtils.isNotEmpty(unitId)){
				condition.put("unitId", unitId);
			}
			if(ExStringUtils.isNotEmpty(classicId)){
				condition.put("classicId", classicId);
			}
			if(ExStringUtils.isNotEmpty(majorId)){
				condition.put("majorId", majorId);
			}
			if(ExStringUtils.isNotEmpty(gradeId)){
				condition.put("gradeId", gradeId);
			}
			if(ExStringUtils.isNotEmpty(classesId)){
				condition.put("classesId", classesId);
			}
			if(ExStringUtils.isNotEmpty(studentNo)){
				condition.put("studentNo", studentNo);
			}
			if(ExStringUtils.isNotEmpty(matriculateNoticeNo)){
				condition.put("studentNo", matriculateNoticeNo);
			}
			if(ExStringUtils.isNotEmpty(rollCard)){
				condition.put("rollCard", rollCard);
			}
			if(ExStringUtils.isNotEmpty(certNum)){
				condition.put("certNum", certNum);
			}
			if(ExStringUtils.isNotEmpty(stuStatus)){
				condition.put("stuStatus", stuStatus);
			}
			if(ExStringUtils.isNotEmpty(name)){
				condition.put("name", name);
			}
			if(ExStringUtils.isNotEmpty(entranceFlag)){
				condition.put("entranceFlag", entranceFlag);
			}
		}
	}


	/**
	 * 毕业生登记表打印-预览
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 * @throws ServiceException
	 */
	@RequestMapping(value={"/edu3/roll/RegistryForm/print-view.html"})
	public String RegistryFormPrintView(HttpServletRequest request,ModelMap model) throws ServiceException, Exception{
		String name =  ExStringUtils.trimToEmpty(request.getParameter("name"));
		if (ExStringUtils.isNotBlank(name)) {
			try {
				name    = URLEncoder.encode(name,"utf-8");
			} catch (UnsupportedEncodingException e) {

			}
		}
		String stuChangeInfo            = ExStringUtils.trimToEmpty(request.getParameter("stuChangeInfo"));//
		if (ExStringUtils.isNotBlank(stuChangeInfo)) {
			try {
				stuChangeInfo    = URLEncoder.encode(stuChangeInfo,"utf-8");
			} catch (UnsupportedEncodingException e) {

			}
		}

		Condition2SQLHelper.addMapFromResquestByIterator(request, model);
		model.addAttribute("name", name);
		model.addAttribute("stuChangeInfo",stuChangeInfo);
		model.addAttribute("entranceTime", ExStringUtils.trimToEmpty(request.getParameter("indate")));
		return "/edu3/roll/graduationStudent/student-registryform-printview";
	}

	/**
	 * 毕业生登记表打印导出
	 * @param request
	 * @param response
	 */
	@RequestMapping(value={"/edu3/roll/registryForm/printAndExport.html","/edu3/roll/studentRegistryForm/downloadPDF.html"})
	public void registryFormPrintAndExport(HttpServletRequest request,HttpServletResponse response){
		try {
			Map<String,Object> condition = new HashMap<String,Object>();
			String isPdf = ExStringUtils.trimToEmpty(request.getParameter("isPdf"));
			String branchSchool=request.getParameter("branchSchool");//学习中心
			String major=request.getParameter("major");//专业
			String classic=request.getParameter("classic");//层次
			String stuStatus=request.getParameter("stuStatus");//学籍状态
			String name=request.getParameter("name");//姓名
			String studyNo=request.getParameter("studyNo");//学号
			String grade=request.getParameter("grade");//年级graduateDate
			String graduateDate = request.getParameter("graduateDate");//毕业日期
			String graduationType = request.getParameter("graduationType");//审核类型
			String confirmGraduateDateb = request.getParameter("gl_confirmGraduateDateb");//确认毕业日期始
			String confirmGraduateDatee = request.getParameter("gl_confirmGraduateDatee");//确认毕业日期终
			String teachingType = request.getParameter("teachingType");//学习形式
			String classes = request.getParameter("classes");//班级
			String degreeStatus = request.getParameter("degreeStatus");//学位状态
			String resourceid = request.getParameter("resourceid");
			String schoolCode = CacheAppManager.getSysConfigurationByCode("graduateData.schoolCode").getParamValue();
			String schoolConnectName = CacheAppManager.getSysConfigurationByCode("graduateData.schoolConnectName").getParamValue();

			//String publishStatus = request.getParameter("publishStatus");//发布状态
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
			if(ExStringUtils.isNotEmpty(teachingType)) {
				condition.put("teachingType", teachingType);
			}
			if(ExStringUtils.isNotEmpty(classes)) {
				condition.put("classes", classes);
			}
			if(ExStringUtils.isNotEmpty(graduationType)) {
				condition.put("graduationType", graduationType);
			}
			if(ExStringUtils.isNotEmpty(graduateDate)) {
				condition.put("graduateDate", graduateDate);//毕业日期
			}
			if(ExStringUtils.isNotEmpty(degreeStatus)) {
				condition.put("degreeStatus", degreeStatus);//学位状态
			}
			if(ExStringUtils.isNotEmpty(resourceid)) {
				condition.put("resourceid", resourceid);//学位状态
			}

			if(ExStringUtils.isNotEmpty(confirmGraduateDateb)) {
				condition.put("confirmGraduateDateb", confirmGraduateDateb);
				//condition.put("gl_confirmGraduateDateb", confirmGraduateDateb);
			}
			if(ExStringUtils.isNotEmpty(confirmGraduateDatee)){
				condition.put("confirmGraduateDatee", confirmGraduateDatee);
				//condition.put("gl_confirmGraduateDatee", confirmGraduateDatee);
			}

			Map<String, Object> map1 = new HashMap<String, Object>();

			String studentids =ExStringUtils.trimToEmpty(request.getParameter("studentids"));
			if(ExStringUtils.isNotBlank(studentids)){
				map1.put("studentids", studentids);
			}else {
				List<GraduateData> gd = graduateDataService.findByHql(condition);
				StringBuilder studentidsList = new StringBuilder();
				for (GraduateData data:gd) {
					studentidsList.append(data.getStudentInfo().getResourceid()+",");
				}
				map1.put("studentids", studentidsList.toString());
			}

			List<Map<String, Object>> dataList = graduationStatJDBCService.getStudetnRegistryForm(map1);
			List<Map<String,Object>> resumes = graduationStatJDBCService.getStudetnResumes(map1);
			Map<String,List<Map<String,Object>>> resumeMap   = new HashMap<String, List<Map<String,Object>>>();
			for (Map<String,Object> m : resumes) {
				List<Map<String,Object>> stuRes  = null;
				String studentId                 = m.get("studentid").toString();
				if (resumeMap.containsKey(studentId)) {
					stuRes                       = resumeMap.get(studentId);
				}else {
					stuRes                       = new ArrayList<Map<String,Object>>();
				}
				if (stuRes.size()<5) {
					stuRes.add(m);
					resumeMap.put(studentId,stuRes);
				}
			}

			for (Map<String,Object> m : dataList) {
				m.put("gender", null==m.get("gender")?"":JstlCustomFunction.dictionaryCode2Value("CodeSex",m.get("gender").toString()));
				m.put("nation", null==m.get("nation")?"":JstlCustomFunction.dictionaryCode2Value("CodeNation",m.get("nation").toString()));
				m.put("politics", null==m.get("politics")?"":JstlCustomFunction.dictionaryCode2Value("CodePolitics",m.get("politics").toString()));
				m.put("teachingtype", null==m.get("teachingtype")?"":JstlCustomFunction.dictionaryCode2Value("CodeTeachingType",m.get("teachingtype").toString()));
				m.put("studyno", null==m.get("studyno")?"":m.get("studyno"));
				m.put("gradename", null==m.get("gradename")?"":m.get("gradename"));
				m.put("majorname", null==m.get("majorname")?"":m.get("majorname"));
				m.put("classicname", null==m.get("classicname")?"":m.get("classicname"));
				m.put("certnum", null==m.get("certnum")?"":m.get("certnum"));
				m.put("name", null==m.get("name")?"":m.get("name"));
				m.put("bornday", null==m.get("bornday")?"":ExDateUtils.formatDateStr((Date)m.get("bornday"),ExDateUtils.PATTREN_DATE_CN));
				m.put("title", null==m.get("title")?"":m.get("title"));
				m.put("mobile", null==m.get("mobile")?"":m.get("mobile"));
				m.put("homephone", null==m.get("homephone")?"":m.get("homephone"));
				m.put("officeName", null==m.get("officeName")?"":m.get("officeName"));
				m.put("homezipcode", null==m.get("homezipcode")?"":m.get("homezipcode"));
				m.put("contactzipcode", null==m.get("contactzipcode")?"":m.get("contactzipcode"));
				m.put("officephone", null==m.get("officephone")?"":m.get("officephone"));
				m.put("eduyear", null==m.get("eduyear")?"":m.get("eduyear"));
				m.put("bornAddress", null==m.get("bornAddress")?"":m.get("bornAddress"));
				m.put("homeAddress", null==m.get("homeAddress")?"":m.get("homeAddress"));
				m.put("contactAddress", null==m.get("contactAddress")?"":m.get("contactAddress"));
				m.put("selfAssessment", null==m.get("selfAssessment")?"":m.get("selfAssessment"));
				m.put("reWardsPuniShment", null==m.get("reWardsPuniShment")?"":m.get("reWardsPuniShment"));
				if ("10601".equals(schoolCode)) {//桂林医学院
					m.put("inDate", null == m.get("inDate") ? "" : ExDateUtils.formatDateStr((Date) m.get("inDate"), 8));
					m.put("graduatedate", null == m.get("graduatedate") ? "" : ExDateUtils.formatDateStr((Date) m.get("graduatedate"), 8));
				} else {
					m.put("inDate", null==m.get("inDate")?"":ExDateUtils.formatDateStr((Date)m.get("inDate"),ExDateUtils.PATTREN_DATE_CN));
					m.put("graduatedate", null==m.get("graduatedate")?"":ExDateUtils.formatDateStr((Date)m.get("graduatedate"),ExDateUtils.PATTREN_DATE_CN));
				}
				//TODO 还有一个  个人鉴定  未填写
				List<Map<String,Object>> stuRes = resumeMap.get(m.get("basestudentid").toString());
				int i = 0;
				if (null!=stuRes) {
					for (; i < stuRes.size(); i++) {
						m.put("timeArea_"+(i+1),stuRes.get(i).get("timeArea"));
						m.put("company_"+(i+1),stuRes.get(i).get("company"));
						m.put("title_"+(i+1),stuRes.get(i).get("title"));
						m.put("attestator_"+(i+1), stuRes.get(i).get("attestator"));
					}
				}
				for(;i<4;i++){
					m.put("timeArea_"+(i+1),"");
					m.put("company_"+(i+1),"");
					m.put("title_"+(i+1),"");
					m.put("attestator_"+(i+1), "");
				}
			}
//			map1.put("imageRootPath","C:");
			map1.put("imageRootPath",
					CacheAppManager.getSysConfigurationByCode("web.uploadfile.rootpath").getParamValue() + "common"
							+ File.separator + "students");
//			// map.put("unitname",
//			// CacheAppManager.getSysConfigurationByCode("graduateData.schoolName").getParamValue());
//			// //学校
//			map.put("cardtitle", CacheAppManager.getSysConfigurationByCode("graduateData.schoolName").getParamValue()
//					+ CacheAppManager.getSysConfigurationByCode("graduateData.transactType").getParamValue()); // 标题
			map1.put("reportRootPath", request.getSession().getServletContext().getRealPath("/reports/graduation"));
			map1.put("printDate", ExDateUtils.formatDateStr(ExDateUtils.getCurrentDateTime(), "yyyy年MM月dd日"));

			// 排序打印的顺序
			String fileName = null;
			String jasperFile = null;
			JasperPrint _jasperPrint = null;
			JasperPrint jasperPrint = null;
			JRMapCollectionDataSource dataSource = null;
			List<Map<String, Object>> _tmpList = null;
			String jasper1 = "";
			String jasper2 = "";

			if("10601".equals(schoolCode)){//桂林医学院
				jasper1 = "graduateStudentRegistryForm_gly.jasper";
				jasper2 = "graduateStudentRegistryForm2_gly.jasper";
			}else if ("10560".equals(schoolCode)) {//汕头大学
				jasper1 = "graduateStudentRegistryForm_stdx.jasper";
				jasper2 = "graduateStudentRegistryForm2_stdx.jasper";
			}else {//默认
				jasper1 = "graduateStudentRegistryForm_stdx.jasper";
				jasper2 = "graduateStudentRegistryForm2_stdx.jasper";
			}
			int count = 0;
			for (Map<String, Object> _map : dataList) {
				// 正反面循环
				for (int i = 0; i < 2; i++) {
					if (i == 0) {
						fileName = jasper1;
					} else {
						fileName = jasper2;
					}
					_tmpList = new ArrayList<Map<String, Object>>();
					_tmpList.add(_map);
					dataSource = new JRMapCollectionDataSource(_tmpList);

					jasperFile = URLDecoder.decode(request.getSession().getServletContext().getRealPath(
							File.separator + "reports" + File.separator + "graduation" + File.separator + fileName),
							"utf-8");
					_jasperPrint = JasperFillManager.fillReport(jasperFile, map1, dataSource); // 填充报表

					//第一页为封面
					if (count == 0) {
						jasperPrint = _jasperPrint;
						count++;
					} else {//后面的页为内容
						@SuppressWarnings("unchecked")
						List<JRBasePrintPage> pages = (List<JRBasePrintPage>) _jasperPrint.getPages();
						for (JRBasePrintPage page : pages) {
							jasperPrint.addPage(page);
						}
					}
				}
			}

			setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH + "exportfiles");
			//构建实体pdf文件
			if (null != jasperPrint) {
				if ("Y".equals(isPdf)) {
					GUIDUtils.init();
					String filePath = getDistfilepath() + File.separator + GUIDUtils.buildMd5GUID(false) + ".pdf";
					JRPdfExporter exporter = new JRPdfExporter();
					exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
					exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, "UTF-8");
					exporter.setParameter(JRXlsExporterParameter.OUTPUT_FILE_NAME, filePath);
					exporter.exportReport();
					downloadFile(response, "毕业生登记表.pdf", filePath, true);
				} else {
					renderStream(response, jasperPrint);
				}
			} else {
				renderHtml(response, "<script>alert('缺少数据！')</script>");
			}
		} catch (Exception e) {
			logger.error("毕业生登记表：{}" + e.fillInStackTrace());
			renderHtml(response, "<script>alert('毕业生登记表打印出错：" + e.getMessage() + "')</script>");
		}
	}

	//打印毕业证书，学位证书
	@RequestMapping(value={"/edu3/roll/graduation/diploma/print-view.html"})
	public String diplomaPrintView(HttpServletRequest request,ModelMap model) {
		Condition2SQLHelper.addMapFromResquestByIterator(request,model);
		return "/edu3/roll/graduationStudent/diploma-printview";
	}

	@RequestMapping(value={"/edu3/roll/graduation/diploma/print.html"})
	public void printDiploma(HttpServletRequest request,HttpServletResponse response) {
		Map<String, Object> condition = Condition2SQLHelper.getConditionFromResquestByIterator(request);
		String diplomaType = ExStringUtils.trimToEmpty(request.getParameter("diplomaType"));
		try {
			String school = CacheAppManager.getSysConfigurationByCode("graduateData.schoolName").getParamValue();
			String permitNo = CacheAppManager.getSysConfigurationByCode("graduateData.approvalNumber").getParamValue();
			//String schoolCode = CacheAppManager.getSysConfigurationByCode("graduateData.schoolCode").getParamValue();
			//String schoolConnectName = CacheAppManager.getSysConfigurationByCode("graduateData.schoolConnectName").getParamValue();
			Map<String, Object> map = new HashMap<String, Object>();
			List<String> permitNos = ExStringUtils.splitNumber(permitNo);
			map.put("schoolName", school);
			for (int i = 0; i < permitNos.size(); i++) {
				map.put("permitNo"+i,permitNos.get(i));
			}
			//排序：教学点、层次、专业、学号
			condition.put("orderBy", "studentInfo.branchSchool,studentInfo.classic,studentInfo.major,studentInfo.studyNo desc");
			List<GraduateData> graduateDataList = graduateDataService.findByHql(condition);
			List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
			if(graduateDataList!=null && graduateDataList.size()>0){
				for (GraduateData gd : graduateDataList) {
					StudentInfo studentInfo = gd.getStudentInfo();
					StudentBaseInfo baseInfo = studentInfo.getStudentBaseInfo();
					Map<String, Object> temp = new HashMap<String, Object>();
					temp.put("stuname", studentInfo.getStudentName());
					temp.put("gender", "1".equals(baseInfo.getGender()) ?"男": "2".equals(baseInfo.getGender()) ?"女":"未知");
					Date date = new Date();
					String[] dateArrays = ExDateUtils.formatDateStr(date, ExDateUtils.PATTREN_SIMPLEDATE).split("-");
					Date bornDay = baseInfo.getBornDay();
					String[] bornDayArrays = ExDateUtils.formatDateStr(bornDay, ExDateUtils.PATTREN_SIMPLEDATE).split("-");
					Date entranceDate = gd.getEntranceDate();
					String[] entranceDateArrays = ExDateUtils.formatDateStr(entranceDate, ExDateUtils.PATTREN_SIMPLEDATE).split("-");
					Date graduateDate = gd.getGraduateDate();
					String[] graduateDateArrays = ExDateUtils.formatDateStr(graduateDate, ExDateUtils.PATTREN_SIMPLEDATE).split("-");
					if("1".equals(diplomaType)){//毕业证书
						temp.put("born_year", ExStringUtils.digital2character(bornDayArrays[0]).replace("零", "○"));
						temp.put("born_month", ExStringUtils.digital2characterWithUnit(bornDayArrays[1]));
						temp.put("born_day", ExStringUtils.digital2characterWithUnit(bornDayArrays[2]));

					}else{//学位证书
						temp.put("born_year",bornDayArrays[0]);
						temp.put("born_month",bornDayArrays[1]);
						temp.put("born_day",bornDayArrays[2]);
					}
					temp.put("year", ExStringUtils.digital2character(dateArrays[0]));
					temp.put("month", ExStringUtils.digital2character(dateArrays[1]));
					temp.put("day", ExStringUtils.digital2character(dateArrays[2]));
					temp.put("enrol_year", ExStringUtils.digital2character(entranceDateArrays[0]).replace("零", "○"));
					temp.put("enrol_month", ExStringUtils.digital2characterWithUnit(entranceDateArrays[1]));
					temp.put("enrol_day", ExStringUtils.digital2characterWithUnit(entranceDateArrays[2]));
					temp.put("graduate_year", ExStringUtils.digital2character(graduateDateArrays[0]).replace("零", "○"));
					temp.put("graduate_month", ExStringUtils.digital2characterWithUnit(graduateDateArrays[1]));
					temp.put("graduate_day", ExStringUtils.digital2characterWithUnit(graduateDateArrays[2]));
					temp.put("major",studentInfo.getMajor().getMajorName());
					temp.put("teachingType",dictionaryService.dictCode2Val("CodeTeachingType", studentInfo.getTeachingType()));
					temp.put("classic", studentInfo.getClassic().getClassicName().charAt(0));
					temp.put("degree",gd.getDegreeName());
					//temp.put("degree", dictionaryService.dictCode2Val("CodeDegree", studentInfo.getTeachingPlan().getDegreeName()));
					temp.put("diplomaNum", gd.getDiplomaNum());
					temp.put("degreeNum", gd.getDegreeNum());
					dataList.add(temp);
				}
			}
			String jasper = "";
			if("1".equals(diplomaType)){//毕业证书
				jasper = "degreeDiploma.jasper";
			}else{//----------------------学位证书
				jasper = "bachelorDiploma.jasper";
			}

			JRMapCollectionDataSource dataSource  = new JRMapCollectionDataSource(dataList);
			String jasperFile     				  = URLDecoder.decode(request.getSession().getServletContext().getRealPath(File.separator+"reports"+
													File.separator+"graduation"+File.separator+jasper),"utf-8");
			map.put("imageRootPath",CacheAppManager.getSysConfigurationByCode("web.uploadfile.rootpath").getParamValue()+"common"+File.separator+"students");
			map.put("reportRootPath", request.getSession().getServletContext().getRealPath("/reports/graduation"));

			JasperPrint jasperPrint               = JasperFillManager.fillReport(jasperFile, map, dataSource); // 填充报表
			if (null!=jasperPrint) {
				renderStream(response, jasperPrint);
			}else {
				renderHtml(response,"<script>alert('缺少打印数据！')</script>");
			}
		}catch(Exception e){
			logger.error("打印证书：{}" + e.fillInStackTrace());
			renderHtml(response, "<script>alert('打印证书出错：" + e.getMessage() + "')</script>");
		}
	}

	/**
	 * 导出毕业信息/导出证书编号 -用于修改毕业证号和学位证号
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/schoolroll/graduation/student/exportCertificateNo.html")
	public void exportCertificateNo(HttpServletRequest request,HttpServletResponse response) throws WebException{
		setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
		Map<String,Object> condition = Condition2SQLHelper.getConditionFromResquestByIterator(request);

		try {
			List<GraduateData> graduateDataList = graduateDataService.findByHql(condition);

			File excelFile  = null;
			GUIDUtils.init();
			File disFile 	= new File(getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(true) + ".xls");
			exportExcelService.initParmasByfile(disFile,"graduateData", graduateDataList,null,null);
			//设置行高
			exportExcelService.getModelToExcel().setRowHeight(500);
			String pa = SystemContextHolder.getAppRootPath()+"WEB-INF"+File.separator+"templates"
					+ File.separator+"excel"+File.separator+"graduateDateInfoModel.xls";

			exportExcelService.getModelToExcel().setTemplateParam(pa,1,null);
			WritableFont font = new WritableFont(WritableFont.ARIAL, 10);
			WritableCellFormat format = new WritableCellFormat(font);
			font.setBoldStyle(WritableFont.BOLD);
			format.setAlignment(Alignment.CENTRE);
			format.setBackground(Colour.GRAY_50);
			format.setBorder(Border.ALL, BorderLineStyle.THIN);
			exportExcelService.getModelToExcel().setTitleCellFormat(null);
			exportExcelService.getModelToExcel().setRowHeight(300);
			excelFile = exportExcelService.getExcelFile();//获取导出的文件

			logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());
			downloadFile(response,"毕业信息编辑模版.xls", excelFile.getAbsolutePath(),true);
		} catch (WriteException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 导入毕业信息/导入证书编号  -选择文件窗口
	 * @param request
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/schoolroll/graduation/student/importCertificateNoForm.html")
	public String importCertificateNoForm(HttpServletRequest request,ModelMap model) throws WebException{
		model.addAttribute("url","/edu3/schoolroll/graduation/student/importCertificateNo.html");
		model.addAttribute("navTabId","RES_SCHOOL_GRADUATION_MANAGE");
		return "/edu3/roll/imputDialogForErrorInfo";
	}
	
	/**
	 * 申请学位
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping("/edu3/schoolroll/graduation/applyDegree.html")
	public void applyDegree(HttpServletRequest request,HttpServletResponse response,ModelMap model){
	    Map<String,Object> map = new HashMap<String, Object>(0);
	    int statusCode = 200;
	    String message = "申请成功！";
	    try {
	    	String applyResource = request.getParameter("applyResource");
	    	if(ExStringUtils.isNotBlank(applyResource) && "noStudent".equals(applyResource)){
	    		// 其他人帮学生申请
	    		String graduateDataId = request.getParameter("graduateDataId");
	    		int count = 0;
	    		if(ExStringUtils.isNotBlank(graduateDataId)){
	    			// 只处理学位申请通过的学生
	    			count = graduateDataService.batchUpdateApplyStatus(graduateDataId.replaceAll(",", "','"));
	    		} 
	    		
	    		if(count < 1) {
	    			statusCode = 300;
	    			message = "不存在符合学位申请的毕业生数据";
	    		}
	    	} else {
	    		// 学生本人申请
	    		String studentId = request.getParameter("studentId");
		    	if(ExStringUtils.isNotEmpty(studentId)){
		    		GraduateData gd = graduateDataService.findByStudentId(studentId);
		    		if(gd != null && "Y".equals(gd.getDegreeStatus())){
		    			gd.setDegreeApplyStatus(Constants.DEGREEAPPLYSTATUS_APPLYING);
		    			gd.setDegreeApplyDate(new Date());
		    			graduateDataService.saveOrUpdate(gd);
		    		} else {
		    			statusCode = 300;
		    			message = "不存在符合学位申请的毕业生数据";
		    		}
		    	}
	    	}
		} catch (Exception e) {
			logger.error("学位申请失败", e);
			statusCode = 300;
			message = "申请失败！";
		} finally {
			map.put("statusCode", statusCode);
    		map.put("message", message);
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 *审核学位申请
	 * @param request
	 * @param response
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/roll/graduation/degreeApply/audiate.html")
	public void degreeApplyAudiate(HttpServletRequest request,HttpServletResponse response) throws WebException{
		String resourceid = request.getParameter("resourceid");
		String degreeApplyStatus = request.getParameter("result");
		Map<String ,Object> map = new HashMap<String, Object>();
		int statusCode = 200;
		String message = "审核成功！";
		try {
			do{
				if(ExStringUtils.isBlank(resourceid) || ExStringUtils.isBlank(degreeApplyStatus)){
					statusCode = 300;
					message = "审核失败！";
					logger.info("resourceid 为空 或 degreeApplyStatus 为空");
					break;
				}	
				graduateDataService.setDegreeApplyStatus(resourceid.replaceAll(",", "','"),degreeApplyStatus);
				map.put("forward", request.getContextPath()+"/edu3/schoolroll/graduation/student/list.html");
			} while (false);
		} catch (Exception e) {
			logger.error("审核学位申请出错:{}",e.fillInStackTrace());
			statusCode = 300;
			message = "审核失败！";
		} finally {
			map.put("statusCode", statusCode);
			map.put("message", message);
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}

	/*	*
	 * 
	 * 预览学位申请表
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value={"/edu3/roll/graduation/degreeApply/print-view.html"})
	public String degreeApplyPrintView(HttpServletRequest request,ModelMap model) {
		Condition2SQLHelper.addMapFromResquestByIterator(request,model);
		return "/edu3/roll/graduationStudent/degreeApply-print";
	}
	
	/**
	 * 打印学位申请表
	 * 
	 * @param request
	 * @param response
	 */
@SuppressWarnings("unchecked")
	@RequestMapping(value={"/edu3/roll/graduation/degreeApply/print.html"})
	public void printDegreeApply(HttpServletRequest request,HttpServletResponse response) {
		Map<String, Object> condition = Condition2SQLHelper.getConditionFromResquestByIterator(request);
		try {
			JasperPrint jasperPrint = null;
			JasperPrint singlePrint = null;
			// 模板路径
			StringBuffer japserPath = new StringBuffer(50);
			japserPath.append(File.separator).append("reports")
			.append(File.separator).append("graduation").append(File.separator);
			String[] jasperFiles = {URLDecoder.decode(request.getSession().getServletContext().getRealPath(japserPath.toString()+"degreeApplyFront.jasper"),"utf-8"),
					URLDecoder.decode(request.getSession().getServletContext().getRealPath(japserPath.toString()+"degreeApplyReverse.jasper"),"utf-8")};
			
			// 只能打印审核通过的学生学位申请表
			condition.put("degreeApplyStatus", Constants.DEGREEAPPLYSTATUS_PASS);
			List<GraduateData> graduateDataList = graduateDataService.findByHql(condition);
			if(graduateDataList!=null && graduateDataList.size()>0){
				// 所有审核日期
				Map<String, Object> auditDateMap = getAuditDateMap();
				
				StudentInfo studentInfo = null;
				StudentBaseInfo baseInfo = null;
				Map<String, Object> temp = null;
				String studentId = null;
				String planId = null;
				JRBeanCollectionDataSource dataSource = null;
				String delayExamType = CacheAppManager.getSysConfigurationByCode("ExamResultCorrosion").getParamValue();
				for (GraduateData gd : graduateDataList) {
					studentInfo = gd.getStudentInfo();
					baseInfo = studentInfo.getStudentBaseInfo();
					temp = new HashMap<String, Object>(100);
					studentId = studentInfo.getResourceid();
					planId = studentInfo.getTeachingPlan().getResourceid();
					// 学位外语成绩
					StateExamResults degreeCourse = stateExamResultsService.findDegreeForeignLanguage(studentId, 
							studentInfo.getGrade().getResourceid(), planId);
					getDegreeApplyBaseInfo(auditDateMap, studentInfo, baseInfo,temp, degreeCourse, gd);
					// 成绩列表
					List<DegreeCourseExamVO> examList = examResultsService.findFinalExamForGW(studentId,planId,delayExamType);
					// 处理正反面模板
					for(int i=0;i<jasperFiles.length;i++){
						dataSource  = new JRBeanCollectionDataSource(examList);
						singlePrint = JasperFillManager.fillReport(jasperFiles[i], temp, dataSource); // 填充报表
						
						if (jasperPrint==null) {
							jasperPrint = singlePrint;
						} else {
							List<JRBasePrintPage> pages = (List<JRBasePrintPage>) singlePrint.getPages();
							for (JRBasePrintPage page : pages) {
								jasperPrint.addPage(page);
							}
						}
					}
					//清除临时报表的内存占用
					singlePrint = null;
				}
			}
			
			if (null!=jasperPrint) {
				renderStream(response, jasperPrint);
			}else {
				renderHtml(response,"<script>alert('只能打印学位申请状态为审核通过的记录！')</script>");
			}
		}catch(Exception e){
			logger.error("打印学位申请表：{}" + e.fillInStackTrace());
			renderHtml(response, "<script>alert('打印学位申请表出错：" + e.getMessage() + "')</script>");
		}
	}

	/**
	 * 获取学位申请表的基本信息
	 * 
	 * @param auditDateMap
	 * @param studentInfo
	 * @param baseInfo
	 * @param temp
	 * @param degreeCourse
	 * @param gd
	 * @throws ParseException
	 */
	private void getDegreeApplyBaseInfo(Map<String, Object> auditDateMap,
			StudentInfo studentInfo, StudentBaseInfo baseInfo,
			Map<String, Object> temp, StateExamResults degreeCourse,
			GraduateData gd) throws ParseException {
		// 基本信息
		temp.put("major",studentInfo.getMajor().getMajorName());
//		temp.put("applyDate", ExDateUtils.formatDateStr(gd.getDegreeApplyDate(),"yyyy 年 MM 月 dd 日"));
		temp.put("stuname", studentInfo.getStudentName());
		temp.put("gender", "1".equals(baseInfo.getGender()) ?"男": "2".equals(baseInfo.getGender()) ?"女":"未知");
		temp.put("bornDay", ExDateUtils.formatDateStr(baseInfo.getBornDay(), ExDateUtils.PATTREN_DATE));
		temp.put("nation",dictionaryService.dictCode2Val("CodeNation", baseInfo.getNation()));
		temp.put("certNum",baseInfo.getCertNum());
		temp.put("homePlace",baseInfo.getHomePlace());
		temp.put("title",baseInfo.getTitle());
		temp.put("politics",dictionaryService.dictCode2Val("CodePolitics", baseInfo.getPolitics()));
		temp.put("email",baseInfo.getEmail());
		temp.put("graduateDate", ExDateUtils.formatDateStr(gd.getGraduateDate(),"yyyyMM"));
		temp.put("contactPhone", baseInfo.getContactPhone());
		temp.put("degreeNum", gd.getDegreeNum());
		String examDate = null;
		String examScore = null;
		String examCertificateNo = null;
		//学位外语信息
		if(degreeCourse != null){
			examDate = degreeCourse.getPasstime()!=null?ExDateUtils.formatDateStr(degreeCourse.getPasstime(), "yyyyMM"):null;
			examScore = degreeCourse.getScore()!=null?String.valueOf(degreeCourse.getScore()):null;
			examCertificateNo = degreeCourse.getCandidateNo();
		}
		temp.put("examCertificateNo",examCertificateNo);  // 学位准考证号
		temp.put("examDate", examDate);
		temp.put("examScore", examScore);
		// 审核日期
		temp.putAll(auditDateMap);
	}

	/**
	 * 获取学位申请表中的所有审核时间
	 * @return
	 */
	private Map<String, Object> getAuditDateMap() {
		List<Dictionary> auditDateList = CacheAppManager.getChildren("CodeDegreeApplyAuditDate");
		Map<String,Object> auditDateMap = new HashMap<String, Object>(8);
		if(ExCollectionUtils.isNotEmpty(auditDateList)){
			String auditDate = "auditDate_";
			for(Dictionary d : auditDateList){
				if(d.getIsDeleted()==0 && "Y".equals(d.getIsUsed())){
					auditDateMap.put(auditDate+d.getShowOrder(), d.getDictValue());
				}
			}
		}
		
		return auditDateMap;
	}

	/**
	 * 导入毕业信息/导入证书编号  -毕业证号和学位证号
	 * @param attachId
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/schoolroll/graduation/student/importCertificateNo.html")
	public void importCertificateNo(String attachId,HttpServletRequest request,HttpServletResponse response) throws WebException{
		String exportErrorUrl = "/edu3/schoolroll/graduation/student/exportCertificateNoError.html";
		//提示信息字符串
		String  rendResponseStr = "";
		List<GraduateDateInfoVo> falseList = new ArrayList<GraduateDateInfoVo>();
		File excelFile = null;
		//设置目标文件路径
		setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH + request.getParameter("importFile"));
		try {
			//上传文件到服务器
			List<AttachInfo> list = doUploadFile(request, response, null);
			AttachInfo attachInfo = list.get(0);
			//创建EXCEL对象 获得待导入的excel的内容
			File excel = new File(attachInfo.getSerPath() + File.separator+ attachInfo.getSerName());

			importExcelService.initParmas(excel, "graduateDataInfo",null);
			importExcelService.getExcelToModel().setSheet(0);// 设置excel sheet 0
			//获得待导入excel内容的List
			List<GraduateDateInfoVo> modelList = importExcelService.getModelList();
			if(modelList==null){
				throw new Exception("导入模版错误！");
			}
			//转换为对应类型的List
			StringBuilder studyNos = new StringBuilder();

			List<GraduateDateInfoVo> volist = new ArrayList<GraduateDateInfoVo>();
			List<String> studeNoList = new ArrayList<String>();
			List<String> diplomaNumList = new ArrayList<String>();
			List<String> degreeNumList = new ArrayList<String>();
			int inNum = 0;
			for (GraduateDateInfoVo infoVo: modelList) {
				inNum++;
				if (ExStringUtils.isBlank(infoVo.getDiplomaNum())) {
					infoVo.setMessage("毕业证号不能为空！");
					falseList.add(infoVo);
					continue;
				}
				if (ExStringUtils.isNotBlank(infoVo.getDiplomaNum()) && diplomaNumList.contains(infoVo.getDiplomaNum())) {
					infoVo.setMessage("毕业证号重复！");
					falseList.add(infoVo);
					continue;
				}

				if (ExStringUtils.isNotBlank(infoVo.getDegreeNum()) && degreeNumList.contains(infoVo.getDegreeNum())) {
					infoVo.setMessage("学位证号重复！");
					falseList.add(infoVo);
					continue;
				}
				if(inNum%500==0){
					studyNos.append(") or in (");
					studyNos.append("'"+infoVo.getStudyNo()+"'");
				}else{
					studyNos.append("'"+infoVo.getStudyNo()+"'");
				}
				if(inNum!=modelList.size()){
					studyNos.append(",");
				}
				studeNoList.add(infoVo.getStudyNo());
				diplomaNumList.add(infoVo.getDiplomaNum());
				degreeNumList.add(infoVo.getDegreeNum());
				volist.add(infoVo);
			}
			if (studyNos.toString().endsWith(",")) {
				studyNos = new StringBuilder(studyNos.toString().substring(0,studyNos.toString().length()-1));
			}
			List<GraduateData> graduateDataList = new ArrayList<GraduateData>();
			//key:DIPLOMANUM	value:DEGREENUM
			Map<String,Object> graduateDataNoMap = new HashMap<String, Object>();
			if (volist!=null && volist.size()>0) {
				//根据学号查找毕业数据实体类
				StringBuilder hql = new StringBuilder();
				Map<String,Object> condition = new HashMap<String, Object>();
				condition.put("studyNoList",studeNoList);
				graduateDataList = graduateDataService.findByHql(condition);
				//从数据库中查找已经存在的毕业证号和学位证号
				hql.setLength(0);
				hql.append("select gd.DIPLOMANUM,gd.DEGREENUM from EDU_TEACH_GRADUATEDATA gd");
				hql.append(" join edu_roll_studentinfo si on si.RESOURCEID=gd.STUDENTID and si.ISDELETED=0");
				hql.append(" where gd.ISDELETED=0 and si.STUDYNO not in("+studyNos.toString().replaceAll(" or in"," and not in")+")");
				List<Map<String,Object>> mapList = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(hql.toString(),condition);
				graduateDataNoMap = ExBeanUtils.convertMapsToMap2(mapList);
			}

			//遍历需要导入的List 将对应内容写进DB 并同时检测数据的合法性
			if (null!=volist) {
				GUIDUtils.init();
				List<GraduateData> saveGraduateDate = new ArrayList<GraduateData>();
				for(GraduateDateInfoVo vo : volist){
					GraduateData graduateData = null;
					//判断是否存在该学生
					for (GraduateData info : graduateDataList) {
						if(vo.getStudyNo().equals(info.getStudentInfo().getStudyNo())){
							graduateData = info;
							break;
						}
					}
					if(graduateData==null){
						vo.setMessage("找不到该学生的毕业信息！");
						falseList.add(vo);
						continue;
					}
					if (ExStringUtils.isNotBlank(vo.getDiplomaNum()) && graduateDataNoMap.containsKey(vo.getDiplomaNum())) {
						vo.setMessage("毕业证号已存在！");
						falseList.add(vo);
						continue;
					}
					if (ExStringUtils.isNotBlank(vo.getDegreeNum()) && graduateDataNoMap.containsValue(vo.getDegreeNum())) {
						vo.setMessage("学位证号已存在！");
						falseList.add(vo);
						continue;
					}
					//更新毕业信息
					if (graduateData != null) {
						graduateData.setDiplomaNum(vo.getDiplomaNum());
						if (ExStringUtils.isNotBlank(vo.getDegreeNum())) {
							graduateData.setDegreeNum(vo.getDegreeNum());
						}
						if (ExStringUtils.isNotBlank(vo.getDegreeName())) {
							graduateData.setDegreeName(vo.getDegreeName());
						}
						saveGraduateDate.add(graduateData);
					}
				}
				graduateDataService.batchSaveOrUpdate(saveGraduateDate);
			}

			//导出失败的信息以及原因
			if(falseList!=null&&falseList.size()>0){
				setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
				//导出
				GUIDUtils.init();
				String fileName = GUIDUtils.buildMd5GUID(false);
				File disFile = new File(getDistfilepath()+ File.separator + fileName + ".xls");

				//模板文件路径
				String templateFilepathString = "graduateDateErrorInfo.xls";
				//初始化配置参数
				exportExcelService.initParmasByfile(disFile,"graduateDataErrorInfo", falseList,null);
				exportExcelService.getModelToExcel().setTemplateParam(templateFilepathString, 1, null);
				exportExcelService.getModelToExcel().setRowHeight(400);

				excelFile = exportExcelService.getExcelFile();//获取导出的文件
				logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());

				rendResponseStr = "{statusCode:200,message:'"+"导入成功"+ (modelList.size() - falseList.size())
						+"条 | 导入失败"+  falseList.size()
						+"条！',forwardUrl:'"+exportErrorUrl+"?excelFile="+fileName+"'};";
			}
			if(ExStringUtils.isBlank(rendResponseStr)){
				rendResponseStr = "{statusCode:200,message:'"+"导入成功"+ (modelList.size() - falseList.size())
						+"条 | 导入失败"+ falseList.size()
						+"条！',forwardUrl:''};";
			}
			UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), "3", UserOperationLogs.IMPORT, "导入毕业信息，成功条数："+ (volist.size() - falseList.size())+"  失败条数："+falseList.size());
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
	 * @throws Exception
	 */
	@RequestMapping("/edu3/schoolroll/graduation/student/exportCertificateNoError.html")
	public void exportUserOrderInfoerror(String excelFile,HttpServletRequest request,HttpServletResponse response) throws Exception {
		setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
		File disFile = new File(getDistfilepath()+ File.separator + excelFile+".xls");
		downloadFile(response, "导入毕业信息失败记录.xls", disFile.getAbsolutePath(),true);
	}
	
	/**
	 * 导出学位申请汇总表
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/roll/graduation/degreeApply/exportSummary.html")
	public void exportDegreeApplySummary(HttpServletRequest request, HttpServletResponse response){
		setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
		Map<String,Object> condition = Condition2SQLHelper.getConditionFromResquestByIterator(request);
		try {
			List<DegreeApplySummaryVO> summaryList = null;
			// 只能导出审核通过的学生学位申请表
			condition.put("degreeApplyStatus", Constants.DEGREEAPPLYSTATUS_PASS);
			List<GraduateData> graduateDataList = graduateDataService.findByHql(condition);
			Map<String,Object> params = new HashMap<String, Object>(1);
			User cureentUser = SpringSecurityHelper.getCurrentUser();
			String unitName = "";
			if(cureentUser.getOrgUnit().getUnitType().
					indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){
				unitName = cureentUser.getOrgUnit().getUnitName();
			}
			params.put("unitName", unitName);
			
			int i=1;
			if(ExCollectionUtils.isNotEmpty(graduateDataList)){
				summaryList = new ArrayList<DegreeApplySummaryVO>();
				StudentInfo studentInfo = null;
				StudentBaseInfo baseInfo = null;
				DegreeApplySummaryVO summaryVO = null;
				Major major = null;
				String studentId = null;
				String planId = null;
				Map<String, String> scoreMap = null;
				String delayExamType = CacheAppManager.getSysConfigurationByCode("ExamResultCorrosion").getParamValue();
				for(GraduateData gd : graduateDataList){
					studentInfo = gd.getStudentInfo();
					baseInfo = studentInfo.getStudentBaseInfo();
					major = studentInfo.getMajor();
					studentId = studentInfo.getResourceid();
					planId = studentInfo.getTeachingPlan().getResourceid();
					// 创建新汇总实体
					summaryVO = new DegreeApplySummaryVO();
					summaryVO.setSerialNumber(i);
					summaryVO.setName(baseInfo.getName());
					summaryVO.setGender("1".equals(baseInfo.getGender()) ?"男": "2".equals(baseInfo.getGender()) ?"女":"未知");
					if(ExStringUtils.isNotBlank( major.getMajorClass())){
						summaryVO.setMajorType(dictionaryService.dictCode2Val("CodeMajorClass", major.getMajorClass()));
					}
					summaryVO.setMajorName(major.getMajorName());
					// 学位外语成绩
					StateExamResults degreeCourse = stateExamResultsService.findDegreeForeignLanguage(studentId, 
							studentInfo.getGrade().getResourceid(), planId);
					if(degreeCourse != null){
						summaryVO.setCandidateNo(degreeCourse.getCandidateNo());
						summaryVO.setGraduateDate(ExDateUtils.formatDateStr(degreeCourse.getPasstime(), "yyyyMM"));
						summaryVO.setLanguageCode(degreeCourse.getCourseName());
						summaryVO.setDegreeNum(degreeCourse.getMemo());
					}
					summaryVO.setCertNum(baseInfo.getCertNum());
					//获取各科成绩平均分和毕业论文成绩
					scoreMap = examResultsService.getAvgAndThesisScore(studentId,planId,delayExamType);
					summaryVO.setAvgScore(scoreMap.get("avgScore"));
					summaryVO.setThesisScore(scoreMap.get("thesisScore"));
					summaryVO.setTitle(baseInfo.getTitle());
					summaryVO.setEmail(baseInfo.getEmail());
					summaryVO.setPhone(baseInfo.getContactPhone());
					summaryList.add(summaryVO);
					i++;
				}
			}
			// 导出逻辑
			// 初始化GUID
			GUIDUtils.init();
			File disFile 	= new File(getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(true) + ".xls");
			exportExcelService.initParmasByfile(disFile,"degreeApplySummary", summaryList,null,null);
		
			String pa = SystemContextHolder.getAppRootPath()+"WEB-INF"+File.separator+"templates"
					+ File.separator+"excel"+File.separator+"degreeApplySummary.xls";

			exportExcelService.getModelToExcel().setTemplateParam(pa,6,params);
			WritableFont font = new WritableFont(WritableFont.ARIAL, 10);
			WritableCellFormat format = new WritableCellFormat(font);
			font.setBoldStyle(WritableFont.BOLD);
			format.setAlignment(Alignment.CENTRE);
			format.setBorder(Border.ALL, BorderLineStyle.THIN);
			exportExcelService.getModelToExcel().setTitleCellFormat(null);
			//设置行高
			exportExcelService.getModelToExcel().setRowHeight(300);
			File excelFile = exportExcelService.getExcelFile();//获取导出的文件

			logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());
			downloadFile(response,"学位申请汇总表.xls", excelFile.getAbsolutePath(),true);
		} catch (WriteException e) {
			logger.error("导出学位申请汇总表写出错", e);
		} catch (IOException e) {
			logger.error("导出学位申请汇总表IO出错", e);
		} catch(Exception e) {
			logger.error("导出学位申请汇总表出错", e);
		}
	}

	/**
	 * 下载学位审核材料导入模版
	 * @param response
	 */
	@RequestMapping("/edu3/roll/graduation/degreeMaterialsModel/download.html")
	public void downloadMaterialsStatusModel(HttpServletResponse response) {
		try{
			setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
			//模板文件路径
			String templateFilepathString = "degreeMaterialsModel.xls";
			downloadFile(response, "学位审核材料导入模版.xls", templateFilepathString,false);
		}catch(Exception e){
			String msg = "导出excel文件出错：找不到该文件录入模版.xls";
			logger.error("下载文件模版出错", e);
			renderHtml(response, "<script>alertMsg.warn("+"\""+msg+"\""+")</script>");
		}
	}

	/**
	 * 导入文件选择窗口
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/roll/graduation/degreeMaterialsModel/import.html")
	public String inputDialog(ModelMap model) throws WebException {
		model.addAttribute("title", "导入学位审核材料提交状态");
		model.addAttribute("formId", "degreeMaterialsModel_import");
		model.addAttribute("url", "/edu3/roll/graduation/degreeMaterialsModel/save.html?mateType=degree");
		return "edu3/roll/inputDialogForm";
	}

	@RequestMapping( value = {"/edu3/roll/graduation/degreeMaterialsModel/save.html","/edu3/roll/sturegister/graduationPracticeMaterials/save.html"})
	public void saveGraduationPracticeMaterials(String mateType,HttpServletRequest request,HttpServletResponse response) {
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
					Map<String, Object> singleMap = studentInfoService.importPracticeMaterials(filePath,mateType);
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
			logger.error("处理导入出错", e);
			success = false;
			result = "导入失败";
		} finally {
			returnMap.put("success",success);
			returnMap.put("msg",message);
			returnMap.put("result",result);
			renderJson(response, JsonUtils.mapToJson(returnMap));
		}
	}
}

