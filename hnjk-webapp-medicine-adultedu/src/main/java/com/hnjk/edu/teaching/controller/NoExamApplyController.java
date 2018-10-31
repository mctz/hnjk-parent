package com.hnjk.edu.teaching.controller;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
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
import com.hnjk.core.foundation.utils.ExCollectionUtils;
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.GUIDUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.FileUploadAndDownloadSupportController;
import com.hnjk.core.support.context.Constants;
import com.hnjk.core.support.context.SystemContextHolder;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.basedata.model.StudentBaseInfo;
import com.hnjk.edu.portal.model.Message;
import com.hnjk.edu.portal.model.MessageReceiver;
import com.hnjk.edu.portal.model.MessageSender;
import com.hnjk.edu.portal.service.IMessageReceiverService;
import com.hnjk.edu.portal.service.IMessageSenderService;
import com.hnjk.edu.portal.service.ISysMsgService;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.roll.service.IGraduationQualifService;
import com.hnjk.edu.roll.service.IRollJDBCService;
import com.hnjk.edu.roll.service.IStudentInfoService;
import com.hnjk.edu.teaching.model.ExamResults;
import com.hnjk.edu.teaching.model.NoExamApply;
import com.hnjk.edu.teaching.service.ICourseService;
import com.hnjk.edu.teaching.service.IExamResultsService;
import com.hnjk.edu.teaching.service.IExamSubService;
import com.hnjk.edu.teaching.service.INoExamApplyService;
import com.hnjk.edu.teaching.service.IStudentExamResultsService;
import com.hnjk.edu.teaching.vo.NoExamApplyVo;
import com.hnjk.edu.teaching.vo.StudentExamResultsVo;
import com.hnjk.extend.plugin.excel.service.IExportExcelService;
import com.hnjk.extend.plugin.excel.service.IImportExcelService;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.Attachs;
import com.hnjk.platform.system.model.Dictionary;
import com.hnjk.platform.system.model.SysConfiguration;
import com.hnjk.platform.system.service.IAttachsService;
import com.hnjk.platform.system.service.IDictionaryService;
import com.hnjk.platform.system.service.ISysConfigurationService;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.OrgUnit;
import com.hnjk.security.model.User;
import com.hnjk.security.model.UserExtends;
import com.hnjk.security.service.IOrgUnitService;
import com.hnjk.security.service.IUserService;

/**
 * 学生免考申请表
 * <code>NoExamApplyController</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2010-12-6 上午11:50:44
 * @see 
 * @version 1.0
 */
@Controller
public class NoExamApplyController extends FileUploadAndDownloadSupportController{

	private static final long serialVersionUID = 9096242579660944918L;
	
	@Autowired
	@Qualifier("attachsService")
	private IAttachsService attachsService;
	
	@Autowired
	@Qualifier("noexamapplyservice")
	private INoExamApplyService noExamApplyService;

	@Autowired
	@Qualifier("studentinfoservice")
	private IStudentInfoService studentInfoService;
	
	@Autowired
	@Qualifier("sysMsgService")
	private ISysMsgService sysMsgService;
	
	@Autowired
	@Qualifier("messageReceiverService")
	private IMessageReceiverService messageReceiverService;
	
	@Autowired
	@Qualifier("messageSenderService")
	private IMessageSenderService messageSenderService;
	
	@Autowired
	@Qualifier("userService")
	private IUserService userService;
	
	@Autowired
	@Qualifier("dictService")
	private IDictionaryService dictionaryService;
	
	@Autowired
	@Qualifier("sysConfigurationService")
	private ISysConfigurationService sysConfigurationService;
	@Autowired
	@Qualifier("rollJDBCService")
	private IRollJDBCService rollJDBCService;
	@Autowired
	@Qualifier("courseService")
	private ICourseService courseService;
	
	@Autowired
	@Qualifier("importExcelService")
	private IImportExcelService importExcelService;
	
	@Autowired
	@Qualifier("exportExcelService")
	private IExportExcelService exportExcelService;
	
	@Autowired
	@Qualifier("studentExamResultsService")
	private IStudentExamResultsService studentExamResultsService;
	
	@Autowired
	@Qualifier("examResultsService")
	private IExamResultsService examResultsService;
	
	@Autowired
	@Qualifier("examSubService")
	private IExamSubService examSubService;

	@Autowired
	@Qualifier("orgUnitService")
	private IOrgUnitService orgUnitService;
	
	@Autowired
	@Qualifier("graduationQualifService")
	private IGraduationQualifService graduationQualifService;
	
	@RequestMapping("/edu3/teaching/noexamapply/list.html")
	public String exeList(HttpServletRequest request,Page objPage, ModelMap model) throws WebException{
		
		objPage.setOrderBy("subjectTime");
		objPage.setOrder(Page.DESC);//设置默认排序方式
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件
		
		String branchSchool 		 = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));
		String grade 		         = ExStringUtils.trimToEmpty(request.getParameter("grade"));
		String major 		         = ExStringUtils.trimToEmpty(request.getParameter("major")); 
		String name 		         = ExStringUtils.trimToEmpty(request.getParameter("name")); 
		String classic 		         = ExStringUtils.trimToEmpty(request.getParameter("classic")); 
		String checkStatus 		     = ExStringUtils.trimToEmpty(request.getParameter("checkStatus"));
		String stuNo 		         = ExStringUtils.trimToEmpty(request.getParameter("stuNo"));
		String appStartTime 		 = ExStringUtils.trimToEmpty(request.getParameter("appStartTime"));
		String appEndTime 		     = ExStringUtils.trimToEmpty(request.getParameter("appEndTime"));
		String auditStartTime 		 = ExStringUtils.trimToEmpty(request.getParameter("auditStartTime"));
		String auditEndTime 		 = ExStringUtils.trimToEmpty(request.getParameter("auditEndTime"));
		
		if(ExStringUtils.isNotEmpty(grade)){  	    condition.put("grade", grade);}
		if(ExStringUtils.isNotEmpty(major)){  	    condition.put("major", major);}
		if(ExStringUtils.isNotEmpty(name)){  	    condition.put("name", name);}
		if(ExStringUtils.isNotEmpty(classic)){ 	    condition.put("classic", classic);}
		if(ExStringUtils.isNotEmpty(checkStatus)){   condition.put("checkStatus", checkStatus);}
		if(ExStringUtils.isNotEmpty(stuNo)){  	    condition.put("stuNo", stuNo);}
		if (ExStringUtils.isNotEmpty(appStartTime)) {
			condition.put("appStartTime", appStartTime);
		}
		if (ExStringUtils.isNotEmpty(appEndTime)) {
			condition.put("appEndTime", appEndTime);
		}
		if (ExStringUtils.isNotEmpty(auditStartTime)) {
			condition.put("auditStartTime", auditStartTime);
		}
		if (ExStringUtils.isNotEmpty(auditEndTime)) {
			condition.put("auditEndTime", auditEndTime);
		}
		
		User user = SpringSecurityHelper.getCurrentUser();
		//招生人员--学习中心
		if(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())){
			branchSchool = user.getOrgUnit().getResourceid();
			condition.put("brshSchool", "Y");
		}

		if (ExStringUtils.isNotEmpty(branchSchool)) {
			condition.put("branchSchool", branchSchool);
		}
		
		if(CacheAppManager.getSysConfigurationByCode("sysuser.usertype.student").getParamValue().equals(user.getUserType())){//如果为学生，则跳转学生页面
			if(null != user.getUserExtends() && null != user.getPropertys(UserExtends.USER_EXTENDCODE_DEFAULTROLLID)){
				condition.put("studentId",user.getPropertys(UserExtends.USER_EXTENDCODE_DEFAULTROLLID).getExValue());
			}else{
				StudentInfo stu = studentInfoService.findUniqueByProperty("sysUser.resourceid", user.getResourceid());
				condition.put("studentId",stu.getResourceid());
			}
		}
		
		Page page = noExamApplyService.findNoExamApplyByCondition(condition, objPage);
		
		
		model.addAttribute("condition", condition);
		model.addAttribute("applyList", page);
		model.addAttribute("schoolCode", CacheAppManager.getSysConfigurationByCode("graduateData.schoolCode").getParamValue());
		if(CacheAppManager.getSysConfigurationByCode("sysuser.usertype.student").getParamValue().equals(user.getUserType())){//如果为学生，则跳转学生页面
					
			return "/edu3/teaching/noexamapply/noexamapply-stu-list";
		}
		return "/edu3/teaching/noexamapply/noexamapply-list";
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
	@RequestMapping("/edu3/teaching/noexamapply/edit.html")
	public String exeEdit(String resourceid,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		User user = SpringSecurityHelper.getCurrentUser();
		//StudentInfo stu = studentInfoService.findUniqueByProperty("sysUser.resourceid", user.getResourceid());
		StudentInfo stu = studentInfoService.findUniqueByProperty("sysUser.resourceid", user.getResourceid());
		if(CacheAppManager.getSysConfigurationByCode("sysuser.usertype.student").getParamValue().equals(user.getUserType())){//sysuser.usertype.student
			model.addAttribute("isStudent", true);
		}
				
		if(ExStringUtils.isNotBlank(resourceid)){ //-----编辑
			NoExamApply noExamApply = noExamApplyService.get(resourceid);	
			model.addAttribute("noExamApply", noExamApply);
			//增加上传附件的支持
			List<Attachs> attachList = attachsService.findByHql("from "+Attachs.class.getName()+ " where isDeleted=0 and formId = ? order by uploadTime desc", resourceid);
			model.addAttribute("attachList", attachList);
		}else{ //----------------------------------------新增			
			model.addAttribute("noExamApply", new NoExamApply(stu));			
		}
		model.addAttribute("storeDir", ExDateUtils.DATE_PATH_FORMAT.format(new Date()));//设置附件上传自动创建目录
		model.addAttribute("schoolCode", CacheAppManager.getSysConfigurationByCode("graduateData.schoolCode").getParamValue());
		if(null!=request.getParameter("onlyView")){//查看
			return "/edu3/teaching/noexamapply/noexamapply-view";
		}else{
			return "/edu3/teaching/noexamapply/noexamapply-form";
		}
	}
	
	/**
	 * 保存更新表单
	 * @param noExamApply
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/noexamapply/save.html")
	public void exeSave(NoExamApply noExamApply,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		String studentId 	   = request.getParameter("studentId");
		String courseId 	   = request.getParameter("courseId");
		//获取附件
		String[] attachIds 	   = request.getParameterValues("attachId");
		User user 			   = SpringSecurityHelper.getCurrentUser();
		try {
			if(ExStringUtils.isNotBlank(noExamApply.getResourceid())){ //--------------------更新
				NoExamApply p_noExamApply = noExamApplyService.get(noExamApply.getResourceid());
				if(!"1".equals(p_noExamApply.getCheckStatus())){

					if (ExStringUtils.isNotEmpty(studentId)) {
						noExamApply.setStudentInfo(studentInfoService.get(studentId));
					}
					if (ExStringUtils.isNotEmpty(courseId)) {
						noExamApply.setCourse(courseService.get(courseId));
					}
					ExBeanUtils.copyProperties(p_noExamApply, noExamApply);
				}
				//更新前，如果是学生进行更新,检查如果checksStatus不为0时，则不可显示更新
				if(!"0".equals(p_noExamApply.getCheckStatus())&&CacheAppManager.getSysConfigurationByCode("sysuser.usertype.student").getParamValue().equals(user.getUserType())){
					map.put("statusCode", 300);
					map.put("message", "保存失败！该条申请已进入到审核阶段，学生用户不可修改。");	
					throw new Exception("checkStatus不允许执行更新");
				}
				noExamApplyService.update(p_noExamApply);
				if(null != attachIds){
					attachsService.updateAttachByFormId(attachIds, p_noExamApply.getResourceid(), "NOEXAMAPPLY", user.getResourceid(),user.getCnName());
				}
				map.put("statusCode", 200);
				map.put("message", "保存成功！");		
				map.put("reloadUrl", request.getContextPath() +"/edu3/teaching/noexamapply/edit.html?resourceid="+noExamApply.getResourceid());
			}else{ //-------------------------------------------------------------------保存				
				
				StudentInfo studentInfo = null;
				Course course = null;
				if(ExStringUtils.isNotEmpty(studentId) && ExStringUtils.isNotEmpty(courseId)){
					course = (Course) noExamApplyService.get(Course.class, courseId);
					noExamApply.setCourse(course);					
					
					studentInfo = studentInfoService.get(studentId);
					noExamApply.setStudentInfo(studentInfo);	
					
					//校验该用户不能重复申请 :一个学籍只能申请一次相同课程的免修免考				
					List<NoExamApply> list = noExamApplyService.findByHql("from "+NoExamApply.class.getSimpleName()+" where isDeleted = ? and studentInfo.resourceid = ? and course.resourceid = ?",
							0,studentInfo.getResourceid(),courseId);
					if(ExCollectionUtils.isNotEmpty(list)){
						throw new WebException("学生 "+studentInfo.getStudentName()+" 已申请过 <font color='blue'>"+course.getCourseName()+"</font> 的免修免考！<br/>如未审核，请联系教务管理人员.");
					}
					//保存时不妨设置checkedStatus为0
					noExamApplyService.save(noExamApply);
					if(null != attachIds){
						attachsService.updateAttachByFormId(attachIds, noExamApply.getResourceid(), "NOEXAMAPPLY", user.getResourceid(),user.getCnName());
					}
					map.put("statusCode", 200);
					map.put("message", "保存成功！");		
					map.put("reloadUrl", request.getContextPath() +"/edu3/teaching/noexamapply/edit.html?resourceid="+noExamApply.getResourceid());
				}else{
					map.put("statusCode", 300);
					map.put("message", "保存失败!<br/>学生或课程不能为空！");
				}
				
				
			}
			
		}catch (Exception e) {
			logger.error("保存出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败!<br/>"+e.getMessage());
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
	@RequestMapping("/edu3/teaching/noexamapply/delete.html")
	public void exeDelete(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		String resourceid 	    = request.getParameter("resourceid");
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(resourceid)){
				if(resourceid.split("\\,").length >1){//批量删除					
					noExamApplyService.batchCascadeDelete(resourceid.split("\\,"));
				}else{//单个删除
					noExamApplyService.delete(resourceid);
				}
				map.put("statusCode", 200);
				map.put("message", "删除成功！");				
				map.put("forward", request.getContextPath()+"/edu3/teaching/noexamapply/list.html");
			}
		} catch (Exception e) {
			logger.error("删除栏目出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 审核通过
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/noexamapply/audit.html")
	public void exeAudit(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		String resourceid 			= request.getParameter("resourceid");
		User user 					= SpringSecurityHelper.getCurrentUser();
		Map<String ,Object> map 	= new HashMap<String, Object>();
		StringBuffer messageStr 	= new StringBuffer();
		
		try {
			
			if(ExStringUtils.isNotBlank(resourceid)){
				if(resourceid.split("\\,").length >1){//批量审核				
					
					noExamApplyService.batchAudit(resourceid.split("\\,"), user.getResourceid(), user.getCnName(),messageStr);

					String[] applyids = resourceid.split("\\,");
					for (String applyid : applyids) {
						NoExamApply apply = noExamApplyService.get(applyid);
						if(messageStr.toString().contains(apply.getStudentInfo().getStudentName()+apply.getCourse().getCourseName()+"审核成功。")){
							sendMsg(apply,'Y');
						}
				    }
					
				}else{//单个审核
					
					noExamApplyService.audit(resourceid, user.getResourceid(), user.getCnName(),messageStr,new Date());
					if(messageStr.toString().contains("成功")){
						NoExamApply apply = noExamApplyService.get(resourceid);
						sendMsg(apply,'Y');
					}
					
				}
				map.put("statusCode", 200);
				map.put("message", messageStr);				
				map.put("forward", request.getContextPath()+"/edu3/teaching/noexamapply/list.html");
			}
		} catch (Exception e) {
			logger.error("审核栏目出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "审核出错:"+e.getMessage());
			
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 撤消审核
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/noexamapply/cancel.html")
	public void exeCancel(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		String resourceid 			= request.getParameter("resourceid");
		User user 		        	= SpringSecurityHelper.getCurrentUser();
		Map<String ,Object> map 	= new HashMap<String, Object>();
		StringBuffer messageStr 	= new StringBuffer();
		
		try {
		
			if(ExStringUtils.isNotBlank(resourceid)){
			
				if(resourceid.split("\\,").length >1){//批量取消				
					noExamApplyService.batchCancel(resourceid.split("\\,"), user.getResourceid(), user.getCnName(),messageStr);
					String[] applyids = resourceid.split("\\,");
					for (String applyid : applyids) {
						NoExamApply apply = noExamApplyService.get(applyid);
						if(messageStr.toString().contains(apply.getStudentInfo().getStudentName()+apply.getCourse().getCourseName()+"撤销成功。")){
							sendMsg(apply,'C');
						}
					}
					
				}else{//单个取消
					
					noExamApplyService.cancel(resourceid, user.getResourceid(), user.getCnName(),messageStr,new Date());
					if(messageStr.toString().contains("成功")){
						NoExamApply apply = noExamApplyService.get(resourceid);
						sendMsg(apply,'C');
					}
				
				}
				map.put("statusCode", 200);
				map.put("message", messageStr);				
				map.put("forward", request.getContextPath()+"/edu3/teaching/noexamapply/list.html");
			}
		} catch (Exception e) {
			logger.error("取消审核栏目出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "取消审核出错！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 审核不通过
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/noexamapply/unAcceptable.html")
	public void exeUnAcceptable(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		String resourceid 			= request.getParameter("resourceid");
		Map<String ,Object> map 	= new HashMap<String, Object>();
		try {
			StringBuffer messageStr = new StringBuffer();
			//User user 				= SpringSecurityHelper.getCurrentUser();
			
			if(ExStringUtils.isNotBlank(resourceid)){
		
				if(resourceid.split("\\,").length >1){//批量审核不通过			
					
					String[] resArray = resourceid.split("\\,");
					for (String resUnit : resArray) {
						NoExamApply apply = noExamApplyService.get(resUnit);
						String checkStatus = apply.getCheckStatus();
						if("0".equals(checkStatus)){
							apply.setCheckStatus("2");
							noExamApplyService.update(apply);
							messageStr.append(apply.getStudentInfo().getStudentName()+"<font color='red'>《"+apply.getCourse().getCourseName()+"》</font>审核不通过成功<br>");
						}else{
							messageStr.append(apply.getStudentInfo().getStudentName()+"<font color='red'>《"+apply.getCourse().getCourseName()+"》</font>此记录已审核状态,如需更改请撤销审核后再执行此操作！<br>");
							continue;
						}
					}
					String[] applyids = resourceid.split("\\,");
					for (String applyid : applyids) {
						NoExamApply apply = noExamApplyService.get(applyid);
						if(messageStr.toString().contains(apply.getStudentInfo().getStudentName()+apply.getCourse().getCourseName()+"审核不通过成功")){
							sendMsg(apply,'N');
						}
					}
				
				}else{//单个审核不通过
					
					NoExamApply apply  = noExamApplyService.get(resourceid);
					String checkStatus = apply.getCheckStatus();
					
					if("0".equals(checkStatus)){
						apply.setCheckStatus("2");
						noExamApplyService.update(apply);
						messageStr.append(apply.getStudentInfo().getStudentName()+"<font color='red'>《"+apply.getCourse().getCourseName()+"》</font>审核不通过成功<br>");
					}else{
						messageStr.append(apply.getStudentInfo().getStudentName()+"<font color='red'>《"+apply.getCourse().getCourseName()+"》</font>此记录已审核状态,如需更改请撤销审核后再执行此操作！<br>");
					}
				
					if(messageStr.toString().contains("审核不通过成功")){
						sendMsg(apply,'N');
					}
				}
				map.put("statusCode", 200);
				map.put("message", messageStr);				
				map.put("forward", request.getContextPath()+"/edu3/teaching/noexamapply/list.html");
			}
		} catch (Exception e) {
			logger.error("取消审核栏目出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "取消审核出错！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 发送成功、取消、失败提示
	 * @param apply 免试申请记录
	 * @param tipsType 提示属性
	 */
	private void sendMsg(NoExamApply apply,char tipsType){
		
		StudentInfo studentTmp = apply.getStudentInfo();
		String unitCode 	   = studentTmp.getBranchSchool().getUnitCode();
		String student = "";
		if (studentTmp.getSysUser() != null) {
			student 		   = studentTmp.getSysUser().getUsername();
		}
		String subject 		   = apply.getCourse().getCourseName();
		String stuName 		   = studentTmp.getStudentName();
		char noExamTypeByte    = apply.getUnScore().charAt(0);
		String noExamType      = "";
		
		switch(noExamTypeByte){
			case '0':noExamType="通过";break;
			case '1':noExamType="免考";break;
			case '2':noExamType="免修";break;
			case '3':noExamType="代修";break;
			default:break;
		}
		switch(tipsType){
			case 'Y':sendTipsMessage(unitCode, "1",stuName, student, subject, noExamType, "Y");
					 sendTipsMessage(unitCode, "2",stuName, student, subject, noExamType, "Y");
					 break;
			case 'N':sendTipsMessage(unitCode, "1",stuName, student, subject, noExamType, "N");
					 sendTipsMessage(unitCode, "2",stuName, student, subject, noExamType, "N");
					 break;
			case 'C':sendTipsMessage(unitCode, "1",stuName, student, subject, noExamType, "C");
					 sendTipsMessage(unitCode, "2",stuName, student, subject, noExamType, "C");
					 break;
		}
		
	}
	/**
	 * 发送有关免试申请的温馨提示
	 * @param unitCode
	 * @param receiver
	 * @param stuName
	 * @param student
	 * @param subject
	 * @param noExamType
	 * @param result
	 * @throws ServiceException
	 */
	private void sendTipsMessage(String unitCode,String receiver,String stuName,String student,String subject,String noExamType,String result) throws ServiceException {
		Message message 		    	= new Message();
//		MessageSender messageSender 	= new MessageSender();
		MessageReceiver messageReceiver = new MessageReceiver();		
		String content 					= "";
		User user 						= SpringSecurityHelper.getCurrentUser();
		if("1".equals(receiver)){
			content 					= stuName+"同学:您的"+noExamType+subject+"申请";
			if("Y".equals(result)){
				content += "已通过。";
			}else if("N".equals(result)){
				content += "未通过审核。";
			}else if("C".equals(result)){
				content += "取消审核通过。";
			}
		}else if("2".equals(receiver)){
			content 					= "贵中心的"+stuName+"同学"+noExamType+subject+"的申请"+"。";
			if("Y".equals(result)){
				content = "已同意"+content;
			}else if("N".equals(result)){
				content = "不同意"+content;
			}else if("C".equals(result)){
				content = "取消"+content.substring(0,content.lastIndexOf('。'))+"通过审核的结果。";
			}
		}
		
		message.setContent(content);
		message.setMsgTitle(stuName+"同学的申请通过、免修、免考、代修结果提示。");
		message.setMsgType("tips");
		message.setSendTime(new Date());
		
		message.setSender(user);
		message.setSenderName(user.getCnName());	
		message.setOrgUnit(user.getOrgUnit());
		/*messageSender.setMessage(message);
		messageSender.setSender(user.getCnName());
		messageSender.setSenderId(user.getResourceid());
		messageSender.setOrgUnit(user.getOrgUnit());*/
			
		messageReceiver.setMessage(message);
		messageReceiver.setReceiveType("user");
		
		if("1".equals(receiver)){//如果是给学生发送
			messageReceiver.setUserNames(student+",");
		}else if("2".equals(receiver)){//如果给教务人员发送
			/*messageReceiver.setReceiveType("org,role");
			messageReceiver.setOrgUnitCodes(unitCode);//可以以后设置成全局参数
			messageReceiver.setRoleCodes("ROLE_BRS_STUDENTSTATUS");*/
			Map<String, Object> condition = new HashMap<String, Object>();
			StringBuffer userNames = new StringBuffer();
			condition.put("unitCode", unitCode);
			condition.put("userRole", "ROLE_BRS_STUDENTSTATUS");
			Page _page = new Page();
			_page.setPageSize(1000);
			Page userPage = userService.findUserByCondition(condition, _page);
			if(ExCollectionUtils.isNotEmpty(userPage.getResult())){
				for(Object o:userPage.getResult()){
					User u = (User)o;
					userNames.append(u.getUsername()+",");
				}
			}
			messageReceiver.setUserNames(userNames.toString());
		}
		
		sysMsgService.saveMessage(message, messageReceiver,null);
//		sysMsgService.saveMessage_old(messageSender, messageReceiver,null);
		
//		sysMsgService.save(message);
//		messageSenderService.save(messageSender);
//		messageReceiverService.save(messageReceiver);
		
	}
	
	
	/**
	 * 选择学生
	 * @param objPage
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	/*@RequestMapping("/edu3/teaching/noexamapply/chooseStudent.html")
	public String exeChooseStudent(HttpServletRequest request, Page objPage, ModelMap model) throws WebException {
		objPage.setOrderBy("resourceid");
		objPage.setOrder(Page.ASC);//设置默认排序方式
		//查询条件
		Map<String,Object> condition = new HashMap<String,Object>();
		String branchSchool=request.getParameter("branchSchool");//学习中心
		String major=request.getParameter("major");//专业
		String classic=request.getParameter("classic");//层次
		String stuStatus=request.getParameter("stuStatus");//学籍状态
		String name=request.getParameter("name");//姓名
		String matriculateNoticeNo=request.getParameter("matriculateNoticeNo");//学号
		
		if(ExStringUtils.isNotEmpty(branchSchool)) condition.put("branchSchool", branchSchool);
		if(ExStringUtils.isNotEmpty(major)) condition.put("major", major);
		if(ExStringUtils.isNotEmpty(classic)) condition.put("classic", classic);
		if(ExStringUtils.isNotEmpty(stuStatus)) condition.put("stuStatus", stuStatus);
		if(ExStringUtils.isNotEmpty(name)) condition.put("name", name);
		if(ExStringUtils.isNotEmpty(matriculateNoticeNo)) condition.put("matriculateNoticeNo", matriculateNoticeNo);
		
		Page page = studentInfoService.findStuByCondition(condition, objPage);
		
		model.addAttribute("stulist", page);
		model.addAttribute("condition", condition);
		return "/edu3/teaching/noexamapply/schoolroll3-list";
	}*/


	/**
	 * 免修、免考参数设置-列表
	 * @param modle
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value={"/edu3/teaching/noexamapply/config-list.html","/edu3/teaching/nostudyapply/config-list.html"})
	public String noExamApplyConfigList(HttpServletRequest request,HttpServletResponse response,ModelMap modle)throws WebException{

		String uri       = request.getRequestURI();
		String flag      = "";
		String dictName1 = "";
		String dictValue1= "";
		String dictName2 = "";
		String dictValue2= "";
		
		if(uri.indexOf("noexamapply")>=0){
			flag 	   = "noExam"; 
		}else if(uri.indexOf("nostudyapply")>=0){
			flag 	   = "noStudy"; 
		}
		
		String dictCode1       = "noExam".equals(flag)?"CodeNoExamApplyOperatingUser":"CodeNoStudyApplyOperatingUser";
		Dictionary dict1       = CacheAppManager.getDictionaryByCode(dictCode1);
		List<Dictionary> list1 = CacheAppManager.getChildren(dictCode1);

		if (null!=dict1) {
			dictName1          = dict1.getDictName();
		}
		if (null!=list1&&!list1.isEmpty()) {
			dictValue1         = list1.get(0).getDictName();
		}
		
		String dictCode2       = "noExam".equals(flag)?"CodeNoExamApplyTime":"CodeNoStudyApplyTime";
		Dictionary dict2       = CacheAppManager.getDictionaryByCode(dictCode2);
		List<Dictionary> list2 = CacheAppManager.getChildren(dictCode2);
		if (null!=dict2) {
			dictName2          = dict2.getDictName();
		}
		if (null!=list2&&!list2.isEmpty()) {
			for (int i = 0; i < list2.size(); i++) {
				dictValue2        += list2.get(i).getDictName()+"至"+list2.get(i).getDictValue()+"&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp";
			}
			
		}
		
		modle.put("flag", flag);
		
		modle.put("dictName1", dictName1);
		modle.put("dictValue1", dictValue1);
		modle.put("dictName2", dictName2);
		modle.put("dictValue2", dictValue2);
		
		return "/edu3/teaching/noexamapply/noexamapply-config-list";
	}
	/**
	 *  免修、免考参数设置-表单
	 * @param request
	 * @param response
	 * @param modle
	 * @return
	 * @throws WebException
	 */
	@RequestMapping(value={"/edu3/teaching/noexamapply/config-edit.html","/edu3/teaching/nostudyapply/config-edit.html"})
	public String noExamApplyConfigEdit(HttpServletRequest request,HttpServletResponse response,ModelMap modle)throws WebException{
		
		String flag 		 = ExStringUtils.trimToEmpty(request.getParameter("flag"));
		String operatingFlag = ExStringUtils.trimToEmpty(request.getParameter("operatingFlag"));
		
		
		noExamApplyConfigEnter(flag,operatingFlag,modle);
	
		
		modle.put("flag",flag);
		modle.put("operatingFlag",operatingFlag);
		
		return "/edu3/teaching/noexamapply/noexamapply-config-form";
	}
	/**
	 * 免修、免考参数设置-表单入口
	 * @param flag
	 * @param operatingFlag
	 * @param modle
	 */
	private void noExamApplyConfigEnter(String flag,String operatingFlag,ModelMap modle){
		Dictionary dict  		  =  new Dictionary();
		//用户设置
		if("userConfig".equals(operatingFlag)){
			String dictCode       = "noExam".equals(flag)?"CodeNoExamApplyOperatingUser":"CodeNoStudyApplyOperatingUser";
			dict  				  = CacheAppManager.getDictionaryByCode(dictCode);
			List<Dictionary> list = CacheAppManager.getChildren(dictCode);
			if (null!=dict&&null!=list&&!list.isEmpty()) {
				modle.put("childDict",list.get(0));
			}
		//时间设置	
		}else if("timeConfig".equals(operatingFlag)){
			String dictCode       = "noExam".equals(flag)?"CodeNoExamApplyTime":"CodeNoStudyApplyTime";
			dict  				  = CacheAppManager.getDictionaryByCode(dictCode);
			List<Dictionary> list = CacheAppManager.getChildren(dictCode);
			modle.put("childDict",list);
			
		}
		modle.put("dict",dict);
	}
	/**
	 * 免修、免考参数设置-保存
	 * @param request
	 * @param response
	 * @param modle
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/noexamapply/config-save.html")
	public void noExamApplyConfigSave(HttpServletRequest request,HttpServletResponse response,ModelMap modle)throws WebException{
		
		String resourceid    = ExStringUtils.trimToEmpty(request.getParameter("resourceid"));
		String flag 		 = ExStringUtils.trimToEmpty(request.getParameter("flag"));
		String operatingFlag = ExStringUtils.trimToEmpty(request.getParameter("operatingFlag"));
		
		String dictCode      = "";
		String dictName      = "";
		String dictModule    = "";
		

		if ("timeConfig".equals(operatingFlag)) {
			dictCode     = "noExam".equals(flag)?"CodeNoExamApplyTime":"CodeNoStudyApplyTime";
			dictName     = "noExam".equals(flag)?"免修免考认定时间":"免修免考认定时间";
			dictModule   = "noExam".equals(flag)?"学籍":"教学";
		}else if("userConfig".equals(operatingFlag)){
			dictCode     = "noExam".equals(flag)?"CodeNoExamApplyOperatingUser":"CodeNoStudyApplyOperatingUser";
			dictName     = "noExam".equals(flag)?"免修免考认定老师":"免修免考认定老师";
			dictModule   = "noExam".equals(flag)?"学籍":"教学";
		}  
		
		noExamApplyConfigSaveSub(resourceid,operatingFlag,flag,dictCode,dictName,dictModule,response,request,modle);	
	}

	/**
	 * 免修、免考设置-保存
	 * @param resourceid
	 * @param request
	 * @param model
	 */
	private void noExamApplyConfigSaveSub(String resourceid,String operatingFlag,String flag,
										  String dictCode,String dictName,String module,
										  HttpServletResponse response,HttpServletRequest request,ModelMap model){
		
		Map<String,Object>map= new HashMap<String, Object>();
		Dictionary dictionary;
		
		if(ExStringUtils.isNotBlank(resourceid)){//编辑
			dictionary = dictionaryService.get(resourceid);
			dictionary.setDictCode(dictCode);
			dictionary.setModule(module);
			dictionary.setDictName(dictName);

			dictionaryService.updateDict(dictionary,convertDictChilds(operatingFlag,dictCode,request));		
		}else{
			dictionary = new Dictionary();
			dictionary.setDictCode(dictCode);
			dictionary.setModule(module);
			dictionary.setDictName(dictName);			

			List<Dictionary> childs = convertDictChilds(operatingFlag,dictCode,request);
			if(childs.size()>0){
				for (Dictionary child : childs) {
					dictionary.getDictSets().add(child);
					child.setParentDict(dictionary);
				}
				
			}
			
			dictionaryService.saveDict(dictionary);		
		}
		map.put("statusCode", 200);
		map.put("message", "保存成功！");
		//map.put("navTabId","RES_TEACHING_RESULT_NOEXAM_CONFIG_SUB");
		map.put("reloadUrl", request.getContextPath() +"/edu3/teaching/noexamapply/config-edit.html?flag="+flag+"&operatingFlag"+operatingFlag);
		
		renderJson(response, JsonUtils.mapToJson(map));
	}

	/**
	 * 免修、免考参数设置-生成子字典
	 * @param operatingFlag
	 * @param dictCode
	 * @param request
	 * @return
	 */
	private List<Dictionary> convertDictChilds(String operatingFlag,String dictCode,HttpServletRequest request){
		
		List<Dictionary> list= new ArrayList<Dictionary>();
		Dictionary childDict = null;
		if ("userConfig".equals(operatingFlag)) {
			
			String dictChildId   = ExStringUtils.trimToEmpty(request.getParameter("dictChildId"));
			String dictChildName = ExStringUtils.trimToEmpty(request.getParameter("dictChildName"));
			String dictChildValue= ExStringUtils.trimToEmpty(request.getParameter("dictChildValue"));
			
			childDict 		     = new Dictionary(); 
			childDict.setResourceid(dictChildId);
			childDict.setDictName(dictChildName);
			childDict.setDictCode(dictCode+"_1");
			childDict.setDictValue(dictChildValue);
			childDict.setIsUsed(Constants.BOOLEAN_YES);
			childDict.setIsDefault(Constants.BOOLEAN_NO);
			childDict.setShowOrder(0);

			list.add(childDict);
			
			
		}else if("timeConfig".equals(operatingFlag)){
			
			String[] dictIds      = request.getParameterValues("dictChildId"); //子id
			String[] dictValues1  = request.getParameterValues("dictChildValue1");//选项名
			String[] dictValues2  = request.getParameterValues("dictChildValue2");//选项值
			
			if(dictIds != null && dictIds.length > 0){			
				for(int index=0;index<dictIds.length;index++){	
				
					childDict = new Dictionary(); 

					childDict.setResourceid(dictIds[index]);
					childDict.setDictName(dictValues1[index]);
					childDict.setDictCode(dictCode+"_"+index);
					childDict.setDictValue(dictValues2[index]);
					childDict.setIsUsed(Constants.BOOLEAN_YES);
					childDict.setIsDefault(Constants.BOOLEAN_NO);
					childDict.setShowOrder(index);

					list.add(childDict);
				}
			}
		}
		
		
		return list;
	}
	/*
	 * 免修、免考参数设置-删除
	 */
	@RequestMapping("/edu3/teaching/noexamapply/config-del.html")
	public void noExamApplyConfigDel(HttpServletRequest request,HttpServletResponse response,ModelMap modle)throws WebException{
		
		// flag = ExStringUtils.trimToEmpty(request.getParameter("flag"));
		//String operatingFlag = ExStringUtils.trimToEmpty(request.getParameter("operatingFlag"));
		
		String resourceid = request.getParameter("resourceid");
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(resourceid)){
				dictionaryService.batchCascadeDelete(resourceid.split("\\,"));				
				map.put("statusCode", 200);
				map.put("message", "删除成功！");				
			}
		} catch (Exception e) {
			logger.error("删除栏目出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错:<br/>"+e.getLocalizedMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 *  免修、免考用户设置-用户选择
	 * @param tId
	 * @param request
	 * @param model
	 * @param page
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/noexamapply/config-choose-user.html")
	public String noExamApplyConfigSelectUser(String tId,String cnName, HttpServletRequest request,ModelMap model,Page page)throws WebException{
		
		Map<String,Object> condition = new HashMap<String, Object>();
		SysConfiguration config      = sysConfigurationService.findUniqueByProperty("paramCode", "sysuser.usertype.edumanager");

		if (ExStringUtils.isNotBlank(cnName)) {
			condition.put("cnName", cnName);
		}
		condition.put("userType",config.getParamValue());	  
		condition.put("isDeleted", 0);
		condition.put("enabled",true);
		
		page 						 = userService.findUserByCondition(condition,page);
		
		if (ExStringUtils.isNotEmpty(tId)){    condition.put("tId",tId);}
		if (condition.containsKey("enabled")){ condition.remove("enabled");}
		
		model.put("page", page);
		model.put("condition", condition);

		return "/edu3/teaching/noexamapply/noexamapply-config-select-user";
	}
	/**
	 * 学习中心替学生申请免修免考-列表
	 * @param request
	 * @param model
	 * @param objPage
	 * @return
	 */
	@RequestMapping("/edu3/teaching/noexamapply/noexamapp-brschool-stu-list.html")
	public String noExamAppByBrSchoolStuList(HttpServletRequest request,ModelMap model,Page objPage){
		objPage.setOrderBy("grade.yearInfo.firstYear DESC,studyNo");
		objPage.setOrder(Page.ASC);//设置默认排序方式
		//查询条件
		Map<String,Object> condition = new HashMap<String,Object>();
		String branchSchool 		 = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));//学习中心
		String major 	             = ExStringUtils.trimToEmpty(request.getParameter("major"));//专业
		String classic 				 = ExStringUtils.trimToEmpty(request.getParameter("classic"));//层次
		String stuStatus 			 = ExStringUtils.trimToEmpty(request.getParameter("stuStatus"));//学籍状态
		String name 				 = ExStringUtils.trimToEmpty(request.getParameter("name"));//姓名
		String studyNo				 = ExStringUtils.trimToEmpty(request.getParameter("studyNo"));//学号
		String stuGrade				 = ExStringUtils.trimToEmpty(request.getParameter("stuGrade"));
		String certNum 				 = ExStringUtils.trimToEmpty(request.getParameter("certNum"));
		String classesid 			 = ExStringUtils.trimToEmpty(request.getParameter("classesid"));
		
		User user 					 = SpringSecurityHelper.getCurrentUser();
		if(user.getOrgUnit().getUnitType().indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员
			branchSchool = user.getOrgUnit().getResourceid();
			model.addAttribute("isBrschool", true);
		}
		if (ExStringUtils.isBlank(stuStatus)){ 	   stuStatus = "11";}
		if (ExStringUtils.isNotEmpty(branchSchool)) {
			condition.put("branchSchool", branchSchool);
		}
		if(ExStringUtils.isNotEmpty(major)){ 	   condition.put("major", major);}
		if(ExStringUtils.isNotEmpty(classic)){      condition.put("classic", classic);}
		if (ExStringUtils.isNotEmpty(stuStatus)) {
			condition.put("stuStatus", stuStatus);
		}
		if(ExStringUtils.isNotEmpty(name)){         condition.put("name", name);}
		if (ExStringUtils.isNotEmpty(studyNo)) {
			condition.put("studyNo", studyNo);
		}
		if (ExStringUtils.isNotEmpty(stuGrade)) {
			condition.put("stuGrade", stuGrade);
		}
		if (ExStringUtils.isNotEmpty(certNum)) {
			condition.put("certNum", certNum);
		}
		if (ExStringUtils.isNotEmpty(classesid)) {
			condition.put("classesid", classesid);
		}
		
		Page page = studentInfoService.findStuByParam(condition, objPage);		

		model.addAttribute("stulist", page);
		model.addAttribute("condition", condition);
		
		/*StringBuffer unitOption = new StringBuffer("<option value=''></option>");
		//List<OrgUnit> orgList = (List<OrgUnit>)EhCacheManager.getCache(CacheSecManager.CACHE_SEC_ORGS).get(CacheSecManager.CACHE_SEC_ORGS);
		List<OrgUnit> orgList = orgUnitService.findByHql(" from "+OrgUnit.class.getSimpleName()+" where isDeleted= 0 and unitType ='brSchool' order by unitCode asc ");
		if(null != orgList && orgList.size()>0){
			for(OrgUnit orgUnit : orgList){
				if(ExStringUtils.isNotEmpty(branchSchool)&&branchSchool.equals(orgUnit.getResourceid())){
					unitOption.append("<option selected='selected' value='"+orgUnit.getResourceid()+"'");				
					unitOption.append(">"+orgUnit.getUnitCode()+"-"+orgUnit.getUnitName()+"</option>");
				}else{
					unitOption.append("<option value='"+orgUnit.getResourceid()+"'");				
					unitOption.append(">"+orgUnit.getUnitCode()+"-"+orgUnit.getUnitName()+"</option>");
				}
			}
		}
		model.addAttribute("unitOption", unitOption);
		
		model.addAttribute("majorSelect4",graduationQualifService.getGradeToMajor1(stuGrade,major,"brschool_noexamapp_major","major","searchExamResultMajorClick()",classic));
		model.addAttribute("classesSelect4",graduationQualifService.getGradeToMajorToClasses1(stuGrade,major,classesid,branchSchool,"brschool_noexamapp_classesid","classesId",classic));
		*/
		return "/edu3/teaching/noexamapply/brschool-noexamapp-stu-list";
	}
	/**
	 * 学习中心替学生申请免修免考-新增编辑表单
	 * @param resourceid
	 * @param request
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/noexamapply/noexamapp-brschool-form.html")
	public String noExamAppByBrSchoolForm(String resourceid,HttpServletRequest request,ModelMap model) throws WebException{
		try {
			if (ExStringUtils.isNotBlank(resourceid)) {
				List<Attachs> attachList      = null;
				StringBuffer ids              = new StringBuffer();   
				Map<String,List<Attachs>> attMap       = new HashMap<String,List<Attachs>>();  
				List<Attachs> attList 				  = null;
				User user                     = SpringSecurityHelper.getCurrentUser();
				StudentInfo studentInfo       = studentInfoService.get(resourceid);
				StudentBaseInfo stb =  studentInfo.getStudentBaseInfo();
				String isMinority = "0";
				String isOver40   = "0";
				if(null!=stb){
					String nation = stb.getNation();
					if(ExStringUtils.isNotEmpty(nation)&&!"01".equals(nation)){
						isMinority = "1";
					}else if(ExStringUtils.isEmpty(nation)){
						model.addAttribute("nationStrTip","您的民族信息为空。");
					}else if("01".equals(nation)){
						model.addAttribute("nationStrTip","您的民族为汉族。");
					}
					String entranceTime = "1".equals(studentInfo.getGrade().getTerm()) ?studentInfo.getGrade().getYearInfo().getFirstYear().toString()+"-09-30":String.valueOf(studentInfo.getGrade().getYearInfo().getFirstYear()+1)+"-03-31";
					String bornDay = ExDateUtils.formatDateStr(stb.getBornDay(), ExDateUtils.PATTREN_DATE);
					Integer entranceTime_y = Integer.valueOf(entranceTime.split("-")[0]);
					Integer entranceTime_m = Integer.valueOf(entranceTime.split("-")[1]);
					Integer entranceTime_d = Integer.valueOf(entranceTime.split("-")[2]);
					Integer bornDay_y = Integer.valueOf(bornDay.split("-")[0]);
					Integer bornDay_m = Integer.valueOf(bornDay.split("-")[1]);
					Integer bornDay_d = Integer.valueOf(bornDay.split("-")[2]);
					if(entranceTime_y-bornDay_y>40){
						isOver40 = "1";
					}else if(entranceTime_y-bornDay_y==40){
						if(entranceTime_m-bornDay_m>0){
							isOver40 = "1";
						}else if(entranceTime_m-bornDay_m==0){
							if(entranceTime_d-bornDay_d>=0){
								isOver40 = "1";
							}
						}
					}
					if("0".equals(isOver40)){
						model.addAttribute("ageStrTip", "学生入学注册为："+entranceTime_y+"年"+entranceTime_m+"月"+entranceTime_d+"日,出生日期为："+bornDay_y+"年"+bornDay_m+"月"+bornDay_d+"日。");
					}
				}
				model.addAttribute("isOver40",isOver40);
				model.addAttribute("isMinority",isMinority);
				List<Map<String,Object>> list_o = rollJDBCService.findStudentPlanCourseForNoExamApp(resourceid);
				List<Map<String,Object>> list = new ArrayList<Map<String,Object>>(0);
				List<StudentExamResultsVo> examResults =  studentExamResultsService.studentExamResultsList(studentInfo,null);
				List<String> coursePassList = new ArrayList<String>(0);
				//获得学生通过的成绩
				for (StudentExamResultsVo studentExamResultsVo : examResults) {
					if(null!=studentExamResultsVo.getIntegratedScoreL()&&studentExamResultsVo.getIntegratedScoreL()>=60){
						String courseId = studentExamResultsVo.getCourseId();
						if(!coursePassList.contains(courseId)){
							coursePassList.add(courseId);
						}
					}
				}
				
				for (Map<String,Object> noexam : list_o) {
					String cid =noexam.get("courseid").toString(); 
					if(coursePassList.contains(cid)){
						noexam.put("isPassExam", "1");
					}else{
						noexam.put("isPassExam", "0");
					}
					list.add(noexam);
				}
				for (Map<String,Object> map :list) {
					String noexamid = null==map.get("noexamid")?"":map.get("noexamid").toString();
					if (ExStringUtils.isNotBlank(noexamid)) {
						ids.append(",'"+noexamid+"'");
						attMap.put(noexamid, null);
					}
				}
				if (ExStringUtils.isNotBlank(ids.toString())) {
					attachList     			  = attachsService.findByHql(" from "+Attachs.class.getSimpleName()+" where isDeleted = ? and formType=? and formId in("+ids.toString().substring(1)+") ",0,"NOEXAMAPPLY");
					for (Attachs att : attachList) {
						if (null==attMap.get(att.getFormId())) {
							attList = new ArrayList<Attachs>();
							attList.add(att);
						}else {
							attList = attMap.get(att.getFormId());
							attList.add(att);
						}
						attMap.put(att.getFormId(),attList );
					}
				}
				String schoolCode = CacheAppManager.getSysConfigurationByCode("graduateData.schoolCode").getParamValue();
				model.addAttribute("schoolCode", schoolCode);
				model.addAttribute("studentInfo", studentInfo);
				model.addAttribute("list", list);
				model.addAttribute("user", user);
				model.addAttribute("attMap", attMap);
			}
		} catch (Exception e) {
			logger.error("学习中心替学生申请免修免考,查找学生的教学计划课程出错：{}"+e.fillInStackTrace());
		}
		List<Dictionary> p_d   = dictionaryService.findByHql(" from "+Dictionary.class.getSimpleName()+" where isDeleted = 0 and dictCode = 'CodeNoExamReason' ");
		StringBuffer unScoreReason = new StringBuffer("<option value=\"\">请选择</option>");
		if(p_d.size()>0){
			Dictionary p = p_d.get(0);
			List<Dictionary> dicts = dictionaryService.findByHql(" from "+Dictionary.class.getSimpleName()+" where isDeleted = 0 and dictCode like '%CodeNoExamReason_%' and parentDict.resourceid = '"+p.getResourceid()+"' and dictValue<>'Other'  order by showOrder asc ");
			for (Dictionary d : dicts) {			
				unScoreReason.append("<option value=\""+d.getDictValue()+"\">"+d.getDictName()+"</option>");
			}
			List<Dictionary> other_dict = dictionaryService.findByHql(" from "+Dictionary.class.getSimpleName()+" where isDeleted = 0 and dictCode like '%CodeNoExamReason_%' and parentDict.resourceid = '"+p.getResourceid()+"' and dictValue='Other'  order by showOrder asc ");
			if(0<other_dict.size()){
				StringBuffer otherReason = new StringBuffer("<option value=\"\">请选择</option>");
				Dictionary o_d = other_dict.get(0);
				otherReason.append("<option value=\""+o_d.getDictValue()+"\">"+o_d.getDictName()+"</option>");
				model.addAttribute("otherReason",otherReason);
			}
		
		}
		model.addAttribute("unScoreReason",unScoreReason);
		
		return "/edu3/teaching/noexamapply/brschool-noexamapply-form";
	}

	@RequestMapping("/edu3/teaching/noexamapply/getNoHisScore.html")
	public void getNoHisScore(HttpServletRequest request,HttpServletResponse response) throws WebException{
		
		Map<String,Object> map = new HashMap<String, Object>();
		String noScoreType = ExStringUtils.trimToEmpty(request.getParameter("noScoreType"));
		String courseId  = ExStringUtils.trimToEmpty(request.getParameter("courseId"));
		String studentId = ExStringUtils.trimToEmpty(request.getParameter("studentId"));
		StudentInfo studentInfo = studentInfoService.get(studentId);
		StudentBaseInfo sb = studentInfo.getStudentBaseInfo();
		
		if("HaveScore".equals(noScoreType)){
			try {
				if(null==sb){
					map.put("message", "无法获取学生基本信息<br/>");
				}else{
					String certNum = sb.getCertNum();
					List<StudentInfo> studentInfos_bk = studentInfoService.findByHql(" from "+StudentInfo.class.getSimpleName()+" where isDeleted= 0 and classic.classicCode='3' and studentBaseInfo.certNum='"+certNum+"' ");
					Double maxScore = 0D; 
					for (StudentInfo stu : studentInfos_bk) {
						List<ExamResults> examResults = examResultsService.findByHql(" from "+ExamResults.class.getSimpleName()+" where isDeleted= 0 and course.resourceid = ? and studentInfo.resourceid = ?  ", new Object[]{courseId,stu.getResourceid()});
						for (ExamResults es : examResults) {
							String integerScore = es.getIntegratedScore();
							try {
								Double tmp = Double.parseDouble(integerScore);
								if(tmp>maxScore){
									maxScore = tmp;
								}
							} catch (Exception e) {
								continue;
							}
						}
					}
					if(60<=maxScore){
						map.put("maxScore", String.valueOf(maxScore));
					}else{
						map.put("notEnoughScore", "1");
					}
				}
				
			}catch (Exception e) {
				logger.error("获取成绩出错：{}",e.fillInStackTrace());
				map.put("statusCode", 300);
				map.put("message", "获取成绩失败!<br/>"+e.getMessage());
			}
		}else{
			//获取数据字典的分值对应
			List<Dictionary> children  = CacheAppManager.getChildren("CodeNoExamReasonCorrespondScore");
			Map<String,String> scoreMap= new HashMap<String, String>();
			for (Dictionary dict :children) {
				if (Constants.BOOLEAN_YES.equals(ExStringUtils.trimToEmpty(dict.getIsUsed()))) {
					scoreMap.put(dict.getDictName(), dict.getDictValue());
				}
			}
			if(ExStringUtils.isEmpty(scoreMap.get(noScoreType))){
				map.put("message", "数据字典缺少此原因的对应分值!<br/>");
			}else{
				map.put("maxScore", scoreMap.get(noScoreType));
			}
		
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 学习中心替学生申请免修免考-保存
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/noexamapply/noexamapp-brschool-save.html")
	public void noExamAppByBrSchoolSave(HttpServletRequest request,HttpServletResponse response) throws WebException{
		
		Map<String,Object> map = new HashMap<String, Object>();
		
		String[] courseTypes   = request.getParameterValues("courseType");
		String[] noExamIds     = request.getParameterValues("noExamId");
		String[] courseIds 	   = request.getParameterValues("courseId");
		String[] memos 	   	   = request.getParameterValues("memo");
		String[] unScores      = request.getParameterValues("unScore");
		String[] other_memos   = request.getParameterValues("other_memo");
		String[] scores        = request.getParameterValues("score"); 
		String studentId 	   = request.getParameter("studentId");
		
		try {
			if (null!=courseIds&&ExStringUtils.isNotBlank(studentId)) {
				noExamApplyService.noExamAppSaveByBrSchool(map, courseTypes, noExamIds, courseIds,memos,other_memos, unScores,scores, studentId);
			}else {
				map.put("message", "保存失败，请选择学生及要申请的课程!<br/>");
			}
		}catch (Exception e) {
			logger.error("保存出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败!<br/>"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 导出免修免考申请
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/teaching/noexamapply/list-export.html")
	public void exportNoExamList(HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件
		
		String branchSchool 		 = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));
		String grade 		         = ExStringUtils.trimToEmpty(request.getParameter("grade"));
		String major 		         = ExStringUtils.trimToEmpty(request.getParameter("major")); 
		String name 		         = ExStringUtils.trimToEmpty(request.getParameter("name")); 
		String classic 		         = ExStringUtils.trimToEmpty(request.getParameter("classic")); 
		String checkStatus 		     = ExStringUtils.trimToEmpty(request.getParameter("checkStatus"));
		String stuNo 		         = ExStringUtils.trimToEmpty(request.getParameter("stuNo"));
		String appStartTime 		 = ExStringUtils.trimToEmpty(request.getParameter("appStartTime"));
		String appEndTime 		     = ExStringUtils.trimToEmpty(request.getParameter("appEndTime"));
		String auditStartTime 		 = ExStringUtils.trimToEmpty(request.getParameter("auditStartTime"));
		String auditEndTime 		 = ExStringUtils.trimToEmpty(request.getParameter("auditEndTime"));
		

		User user = SpringSecurityHelper.getCurrentUser();
		//招生人员--学习中心
		if(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())){
			branchSchool = user.getOrgUnit().getResourceid();
		}

		if (ExStringUtils.isNotEmpty(grade)) {
			condition.put("grade", grade);
		}
		if (ExStringUtils.isNotEmpty(major)) {
			condition.put("major", major);
		}
		if(ExStringUtils.isNotEmpty(name)){
			condition.put("name", name);
		}
		if (ExStringUtils.isNotEmpty(classic)) {
			condition.put("classic", classic);
		}
		if (ExStringUtils.isNotEmpty(checkStatus)) {
			condition.put("checkStatus", checkStatus);
		}
		if (ExStringUtils.isNotEmpty(stuNo)) {
			condition.put("stuNo", stuNo);
		}
		if (ExStringUtils.isNotEmpty(appStartTime)) {
			condition.put("appStartTime", appStartTime);
		}
		if (ExStringUtils.isNotEmpty(appEndTime)) {
			condition.put("appEndTime", appEndTime);
		}
		if (ExStringUtils.isNotEmpty(auditStartTime)) {
			condition.put("auditStartTime", auditStartTime);
		}
		if (ExStringUtils.isNotEmpty(auditEndTime)) {
			condition.put("auditEndTime", auditEndTime);
		}
		if (ExStringUtils.isNotEmpty(branchSchool)) {
			condition.put("branchSchool", branchSchool);
		}
		
		List<NoExamApply>  list = noExamApplyService.findNoExamApplyByCondition(condition);
		
		//文件输出服务器路径
		setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
		try{
			
			GUIDUtils.init();
			File excelFile = null;
			File disFile   = new File(getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(false) + ".xls");
			//字典转换列
			List<String> dictCodeList = new ArrayList<String>();
			dictCodeList.add("CodeUnScoreStyle");
			dictCodeList.add("CodeCheckStatus");
			
			//模板文件路径
			String templateFilepathString = "noExamAppList.xls";
			//初始化参数
			exportExcelService.initParmasByfile(disFile,"noExamAppList", list,dictionaryService.getDictionByMap(dictCodeList, true,IDictionaryService.PREKEY_TYPE_BYCODE),null);//初始化配置参数
			exportExcelService.getModelToExcel().setRowHeight(400);//设置行高
			exportExcelService.getModelToExcel().setTemplateParam(templateFilepathString,1, null);				
			excelFile = exportExcelService.getModelToExcel().getExcelfile();
			
			logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());
			downloadFile(response, "免修免考申请.xls", excelFile.getAbsolutePath(),true);
			
		}catch (Exception e) {
			String msg = "导出excel文件出错："+e.fillInStackTrace();
			logger.error(msg);
			renderHtml(response, "<script>alert("+"\""+msg+"\""+")</script>");
		}
	}
	/**
	 * 导入免修免考-表单
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/teaching/noexamapply/import/form.html")
	public String importNoExamApplyForm(ModelMap model){
		User curUser       = SpringSecurityHelper.getCurrentUser();
		List<Attachs> list = attachsService.findByCriteria(Restrictions.eq("isDeleted",0),Restrictions.eq("formId",curUser.getResourceid()),Restrictions.eq("formType","noExamApplyFileUpload"));
		model.put("user",curUser );
		model.put("attachsList", list);
		return "/edu3/teaching/noexamapply/noexam-upload-view";
	}
	
	/**
	 * 导入免修免考-保存
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/teaching/noexamapply/import/upload.html")
	public void importNoExamApply(HttpServletRequest request,HttpServletResponse response){
		
		Map<String,Object> map      = new HashMap<String, Object>();
		String attId 				= ExStringUtils.trimToEmpty(request.getParameter("attachId"));
		String msg                  = "";
		try {
			
			 Attachs attach  		= attachsService.get(attId);
			 File excel          	= attach.getFile();
			 
			 importExcelService.initParmas(excel,"noExamApplyImport",map);
			 importExcelService.getExcelToModel().setSheet(0);
			 List<NoExamApplyVo> list= (List<NoExamApplyVo>)importExcelService.getModelList();
			 
			 noExamApplyService.noExamApplyImport(list,map);
			 
			 if ((Boolean)map.get("success")==true) {
				 if (excel.exists()){ excel.delete();}
				 attachsService.delete(attach);
			 }
		} catch (Exception e) {
			msg                     = "导入免修免考申请出错:{}"+e.fillInStackTrace();
			logger.error(msg);
			e.printStackTrace();
			map.put("success", false);
			map.put("msg", msg);
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
}
