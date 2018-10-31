package com.hnjk.edu.roll.controller;

import java.io.File;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.utils.ExBeanUtils;
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.GUIDUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.BaseSupportController;
import com.hnjk.core.support.base.controller.FileUploadAndDownloadSupportController;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.Classic;
import com.hnjk.edu.basedata.model.YearInfo;
import com.hnjk.edu.basedata.service.IYearInfoService;
import com.hnjk.edu.roll.model.GraduateNograduate;
import com.hnjk.edu.roll.model.GraduateNograduateSetting;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.roll.service.IGraduateNoSettingService;
import com.hnjk.edu.roll.service.IGraduateNograduateService;
import com.hnjk.edu.roll.service.IStudentInfoService;
import com.hnjk.extend.plugin.excel.service.IExportExcelService;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.taglib.JstlCustomFunction;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.Role;
import com.hnjk.security.model.User;

/**
 * 学生不毕业申请信息表
 * <code>GraduateNograduateController</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2010-10-28 下午03:59:39
 * @see 
 * @version 1.0
 */
@Controller
public class GraduateNograduateController extends BaseSupportController {

	private static final long serialVersionUID = -3134626603448139901L;
	
	@Autowired
	@Qualifier("exportExcelService")
	private IExportExcelService exportExcelService;
	/**
	 * 获得可用的非删除的延迟毕业申请时间段
	 * @return
	 */
	private List<GraduateNograduateSetting>  getAvailableSetting(){
		Date currentDate =new Date(ExDateUtils.getCurrentDateTime().getTime());
		List<GraduateNograduateSetting> settingsOrg = graduateNoSettingService.findByHql(" from " +GraduateNograduateSetting.class.getSimpleName()+" where isDeleted=0 and startDate <= to_date('"+currentDate+"','yyyy-mm-dd') and endDate>=to_date('"+currentDate+"','yyyy-mm-dd') ");
		return settingsOrg;
	}
	@RequestMapping("/edu3/roll/graduateNo/list.html")
	public String exeList(String active ,String branchSchool, String grade, String major, String name, String studyNo, String classic,String applyNoGraduationPlan_sel, Page objPage, ModelMap model) throws WebException{
		objPage.setOrderBy("applayDate");
		objPage.setOrder(Page.ASC);//设置默认排序方式
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件
		Date currentDate = new Date(System.currentTimeMillis());
		condition.put("currentDate", currentDate);
		User currentUser = SpringSecurityHelper.getCurrentUser();
		//招生人员--学习中心
		if(currentUser.getOrgUnit().getUnitType().
				indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){
			branchSchool = currentUser.getOrgUnit().getResourceid();
			model.addAttribute("isBrschool", true);
		}
		if(ExStringUtils.isNotEmpty(active)) {
			condition.put("active", active);
		}
		if(ExStringUtils.isNotEmpty(branchSchool)) {
			condition.put("branchSchool", branchSchool);
		}
		if(ExStringUtils.isNotEmpty(grade)) {
			condition.put("grade", grade);
		}
		if(ExStringUtils.isNotEmpty(major)) {
			condition.put("major", major);
		}
		if(ExStringUtils.isNotEmpty(name)) {
			condition.put("name", name);
		}
		if(ExStringUtils.isNotEmpty(studyNo)) {
			condition.put("studyNo", studyNo);
		}
		if(ExStringUtils.isNotEmpty(classic)) {
			condition.put("classic", classic);
		}
		if(ExStringUtils.isNotEmpty(applyNoGraduationPlan_sel)) {
			condition.put("applyNoGraduationPlan_sel", applyNoGraduationPlan_sel);
		}
		
		Page page = graduateNograduateService.findGraduateByCondition(condition, objPage);
		List<GraduateNograduateSetting> settings = new ArrayList<GraduateNograduateSetting>(0);
		settings = graduateNoSettingService.findByHql(" from " +GraduateNograduateSetting.class.getSimpleName()+" where isDeleted=0  ");
		
		model.addAttribute("currentDate", currentDate);
		
		model.addAttribute("condition", condition);		
		model.addAttribute("nolist", page);
		model.addAttribute("settings",settings);
		
		return "/edu3/roll/graduateNo/graduateNo-list";
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
	@RequestMapping("/edu3/roll/graduateNo/edit.html")
	public String exeEdit(String resourceid,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		//判断用户类型：
		//如果是学籍办 可选的时间段为自动恢复日期未超过当前日期的的非删除的时间段 
		//如果是其他用户 可选择的时间段为可用的非删除的时间段
		User currentUser = SpringSecurityHelper.getCurrentUser();
		Set<Role> roles = currentUser.getRoles();
		boolean isRole_Roll = false;
		for (Role role : roles) {
			String roleName = role.getRoleCode();
			if(roleName.contains("ROLE_ROLL")||"ROLE_XUEJIMANAGER".equals(roleName)){
				isRole_Roll = true;
				break;
			}
		}
		List<GraduateNograduateSetting> settings = new ArrayList<GraduateNograduateSetting>(0);
		if(!isRole_Roll){//当不为学籍办人员时
			settings = getAvailableSetting();
		}else{//当为学籍办人员时
			Date currentDate =new Date(ExDateUtils.getCurrentDateTime().getTime());
			settings = graduateNoSettingService.findByHql(" from " +GraduateNograduateSetting.class.getSimpleName()+" where isDeleted=0 and revokeDate>=to_date('"+currentDate+"','yyyy-mm-dd')");
		}
		if(ExStringUtils.isNotBlank(resourceid)){ //-----编辑
			GraduateNograduate graduateNograduate = graduateNograduateService.get(resourceid);	
			model.addAttribute("settings",settings);
			model.addAttribute("graduate", graduateNograduate);
		}else{ //----------------------------------------新增
			model.addAttribute("settings",settings);
			model.addAttribute("graduate", new GraduateNograduate());			
		}
		return "/edu3/roll/graduateNo/graduateNo-form";
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
	@RequestMapping("/edu3/roll/graduateNo/save.html")
	public void exeSave(GraduateNograduate gradeate,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		String stuNum = request.getParameter("stuNum");
		String applyNoGraduationPlanid = ExStringUtils.trimToEmpty(request.getParameter("applyNoGraduationPlan"));
		GraduateNograduateSetting setting = graduateNoSettingService.get(applyNoGraduationPlanid);
		StudentInfo info = null;
		if(ExStringUtils.isNotEmpty(stuNum)){
			String hql = "from StudentInfo s where s.studyNo=? and isDeleted=0";
			info = studentInfoService.findUnique(hql, new Object[]{stuNum});
		}
		boolean isAfterEndDate = false;
		Date currentDate =new Date(ExDateUtils.getCurrentDateTime().getTime());
		if(setting.getEndDate().before(currentDate)){
			isAfterEndDate = true;
		}
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			String deletedResourceid = "";
			if(ExStringUtils.isNotBlank(gradeate.getResourceid())){ //--------------------更新
				GraduateNograduate p_gradeate = graduateNograduateService.get(gradeate.getResourceid());
				if(null!=info) {
					gradeate.setStudentInfo(info);
				}
				ExBeanUtils.copyProperties(p_gradeate, gradeate);
				graduateNograduateService.update(p_gradeate);
				
			}else{ //-------------------------------------------------------------------保存
				
				if(null!=info){ 
					List<GraduateNograduate> list = graduateNograduateService.findByHql(" from " +GraduateNograduate.class.getSimpleName()+" where 1=1 and studentInfo.resourceid='"+info.getResourceid()+"'");		
					for (GraduateNograduate graduateNograduate : list) {
						if(setting.equals(graduateNograduate.getGraduateNograduateSetting())){
							//如果发现这个批次下的已删除的延迟毕业申请，那么就更新申请原因以及删除标识字段即可。
							if(1==graduateNograduate.getIsDeleted()){
								deletedResourceid = graduateNograduate.getResourceid();
								if(isAfterEndDate){
									info.setIsApplyGraduate(Constants.BOOLEAN_WAIT);
								}
								graduateNograduate.setStudentInfo(info);
								graduateNograduate.setApplayDate(gradeate.getApplayDate());
								graduateNograduate.setApplayCause(gradeate.getApplayCause());
								graduateNograduate.setIsDeleted(0);
								graduateNograduateService.update(graduateNograduate);
							}else{
								//如果该学生在该年度学期有记录 不保存
								throw new Exception("isExist");
							}
						}
					}
					//以下是当未在当前批次下找到该名学生的申请时的保存
					if(isAfterEndDate){
						info.setIsApplyGraduate(Constants.BOOLEAN_WAIT);
					}
					gradeate.setStudentInfo(info);
					gradeate.setGraduateNograduateSetting(setting);
					if("".equals(deletedResourceid)){
						graduateNograduateService.save(gradeate);
					}
				}
			}
			map.put("statusCode", 200);
			map.put("message", "保存成功！");
			//map.put("navTabId", "RES_GRADUATE_NO_GRADUATE");
			//map.put("reloadUrl", request.getContextPath() +"/edu3/roll/graduateNo/list.html");//"/edu3/roll/graduateNo/edit.html?resourceid="+gradeate.getResourceid());
		}catch (Exception e) {
			if(e.getMessage().indexOf("isExist")>-1){
				logger.error("学号："+stuNum+"在该年度学期已经存在！",e.fillInStackTrace());
				map.put("message", "学号："+stuNum+"在该年度学期已经存在！");
			}else{
				logger.error("保存栏目出错：{}",e.fillInStackTrace());
				map.put("message", "保存失败！");
			}
			map.put("statusCode", 300);
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	
	/**
	 * 删除列表对象——撤销延迟毕业申请
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/roll/graduateNo/delete.html")
	public void exeDelete(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		String resourceid = request.getParameter("resourceid");
		StringBuffer messageStr = new StringBuffer();
		Map<String ,Object> map = new HashMap<String, Object>();
		User currentUser = SpringSecurityHelper.getCurrentUser();
		Set<Role> roles = currentUser.getRoles();
		boolean isRole_Roll = false;
		boolean has_pro = false;
		for (Role role : roles) {
			String roleName = role.getRoleCode();
			if(roleName.contains("ROLE_ROLL")||"ROLE_XUEJIMANAGER".equals(roleName)){
				isRole_Roll = true;
				break;
			}
		}
		try {
			if(ExStringUtils.isNotBlank(resourceid)){
				if(resourceid.split("\\,").length >1){//批量删除
					String[] resid = resourceid.split("\\,");
					for (String id : resid) {
						GraduateNograduate graduateUnit = graduateNograduateService.get(id);
						GraduateNograduateSetting settingUnit = graduateUnit.getGraduateNograduateSetting();
						Date currentDate = new Date (new java.util.Date(System.currentTimeMillis()).getTime());
						Date targetEndDate = new Date(settingUnit.getEndDate().getTime());
						Date targetRevokeDate = new Date(settingUnit.getRevokeDate().getTime());
						//应该分为以下几种情况
						//1 非学籍办用户 在结束后 恢复前 删除 不允许
						//2 学籍办用户     在结束后 恢复前 删除 允许 且撤销操作立即生效
						//3 学籍办用户&非学籍办用户     在恢复后 删除 不需要
						//4 在结束之前 可以删除
						if(currentDate.after(targetEndDate)&&currentDate.before(targetRevokeDate)){
							if(!isRole_Roll){
								messageStr.append(graduateUnit.getStudentInfo().getStudentName()+"不可删除，因为已过该批次的结束申请日期，如有需要请与学籍办联系。<br>");
								has_pro = true ;
							}else{
								//因为已经过了申请结束时间，所以这个时候，学籍办删除延迟毕业申请需要立即生效
								messageStr.append(graduateUnit.getStudentInfo().getStudentName()+"可删除。<br>");
								StudentInfo stuInfo = graduateUnit.getStudentInfo();
								String classicCode = stuInfo.getClassic().getClassicCode();
								if(!"3".equals(classicCode)){
									stuInfo.setIsApplyGraduate(Constants.BOOLEAN_YES);
								}else{
									stuInfo.setIsApplyGraduate(Constants.BOOLEAN_NO);
								}
								studentInfoService.saveOrUpdate(stuInfo);
								graduateNograduateService.delete(resourceid);
								
							}
						}else if(currentDate.after(targetRevokeDate)){
							messageStr.append(graduateUnit.getStudentInfo().getStudentName()+"已经过自动恢复日期，延迟毕业申请状态已取消。<br>");
						}else{
							messageStr.append(graduateUnit.getStudentInfo().getStudentName()+"可删除。<br>");
							StudentInfo stuInfo = graduateUnit.getStudentInfo();
							String classicCode = stuInfo.getClassic().getClassicCode();
							if(!"3".equals(classicCode)){
								stuInfo.setIsApplyGraduate(Constants.BOOLEAN_YES);
							}else{
								stuInfo.setIsApplyGraduate(Constants.BOOLEAN_NO);
							}
							studentInfoService.update(stuInfo);
							graduateNograduateService.delete(resourceid);
						}	
					}
//					graduateNograduateService.batchCascadeDelete(resourceid.split("\\,"));
				}else{//单个删除
					GraduateNograduate graduateUnit = graduateNograduateService.get(resourceid);
					GraduateNograduateSetting settingUnit = graduateUnit.getGraduateNograduateSetting();
					Date currentDate = new Date (new java.util.Date(System.currentTimeMillis()).getTime());
					Date targetEndDate = new Date(settingUnit.getEndDate().getTime());
					Date targetRevokeDate = new Date(settingUnit.getRevokeDate().getTime());
					if(currentDate.after(targetEndDate)&&currentDate.before(targetRevokeDate)){
						if(!isRole_Roll){
							messageStr.append(graduateUnit.getStudentInfo().getStudentName()+"不可删除，因为已过该批次的结束申请日期，如有需要请与学籍办联系。<br>");
							has_pro = true ;
						}else{
							//因为已经过了申请结束时间，所以这个时候，学籍办删除延迟毕业申请需要立即生效
							messageStr.append(graduateUnit.getStudentInfo().getStudentName()+"可删除。<br>");
							StudentInfo stuInfo = graduateUnit.getStudentInfo();
							String classicCode = stuInfo.getClassic().getClassicCode();
							if(!"3".equals(classicCode)){
								stuInfo.setIsApplyGraduate(Constants.BOOLEAN_YES);
							}else{
								stuInfo.setIsApplyGraduate(Constants.BOOLEAN_NO);
							}
							studentInfoService.update(stuInfo);
							graduateNograduateService.delete(resourceid);
						}
					}else if(currentDate.after(targetRevokeDate)){
						messageStr.append(graduateUnit.getStudentInfo().getStudentName()+"已经过自动恢复日期，延迟毕业申请状态已取消。<br>");
					}else{
						messageStr.append(graduateUnit.getStudentInfo().getStudentName()+"可删除。<br>");
						StudentInfo stuInfo = graduateUnit.getStudentInfo();
						String classicCode = stuInfo.getClassic().getClassicCode();
						if(!"3".equals(classicCode)){
							stuInfo.setIsApplyGraduate(Constants.BOOLEAN_YES);
						}else{
							stuInfo.setIsApplyGraduate(Constants.BOOLEAN_NO);
						}
						studentInfoService.update(stuInfo);
						graduateNograduateService.delete(resourceid);
					}
//					graduateNograduateService.delete(resourceid);
				}
				if(has_pro){
					map.put("statusCode", 300);
				}else{
					map.put("statusCode", 200);
				}
				map.put("message", messageStr.toString()+"删除完成！");				
				map.put("forward", request.getContextPath()+"/edu3/roll/graduateNo/list.html");
			}
		} catch (Exception e) {
			logger.error("删除栏目出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	
	/**
	 * 审核列表对象——暂废弃
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	/*
	@RequestMapping("/edu3/roll/graduateNo/audit.html")
	public void exeAudit(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		String resourceid = request.getParameter("resourceid");
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(resourceid)){
				User user = SpringSecurityHelper.getCurrentUser();
				if(resourceid.split("\\,").length >1){//审核			
					graduateNograduateService.batchAudit(resourceid.split("\\,"), user);
				}else{//审核
					graduateNograduateService.audit(resourceid, user);
				}
				map.put("statusCode", 200);
				map.put("message", "审核成功！");				
				map.put("forward", request.getContextPath()+"/edu3/teaching/graduateNo/list.html");
			}
		} catch (Exception e) {
			logger.error("审核出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "审核出错！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	*/
	
	/**
	 * Ajax 查询
	 * @param stuNum
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/edu3/roll/graduateNo/queryStudentInfo.html")
	public void exeQuery(String stuNum, HttpServletRequest request, HttpServletResponse response) throws Exception{
		if(ExStringUtils.isNotEmpty(stuNum)) {
			String hql = "from StudentInfo s where s.studyNo=? and isDeleted=0";
			StudentInfo info = studentInfoService.findUnique(hql, new Object[]{stuNum});
			//Gson gson = new Gson();
			Map<String,String> map = new HashMap<String, String>();
			if(info != null){
				map.put("studyNo", info.getStudyNo());
				map.put("studentName", info.getStudentBaseInfo().getName());
				map.put("schoolCenter", info.getBranchSchool().getUnitName());
				map.put("grade", info.getGrade().getGradeName());
				map.put("major", info.getMajor().getMajorName());
				map.put("classic", info.getClassic().getClassicName());
				Classic classic = info.getClassic();
				map.put("classic", classic.getShortName());
				map.put("msg", "success");
				if(!"2".equals(classic.getClassicCode())&&!"1".equals(classic.getClassicCode())){
					map.put("classic", classic.getShortName());
					map.put("msg", "classicerr");
				}
			}
			//String stu = gson.toJson(map);
			String stu = JsonUtils.mapToJson(map);
			renderText(response, stu);
		}
	}

	/**
	 * 验证非学籍办用户是否在非申请时间段内提交延迟毕业申请
	 * @param request
	 * @param response
	 * @param model
	 * @throws WebException
	 */
	@RequestMapping("/edu3/roll/graduateNo/validateDuringApply.html")
	public void validateDuringApply(HttpServletRequest request,HttpServletResponse response,ModelMap model)throws WebException{
		Map<String,Object> result = new HashMap<String, Object>(0); 
		result.put("isPromiss", false);
		//控制：如果是学习中心用户，则判断是否在申请时间段内 如果是学籍办的人员 任何时候均可申请
		User currentUser = SpringSecurityHelper.getCurrentUser();
		Set<Role> roles = currentUser.getRoles();
		boolean isRole_Roll = false;
		for (Role role : roles) {
			String roleName = role.getRoleCode();
			if(roleName.contains("ROLE_ROLL")||"ROLE_XUEJIMANAGER".equals(roleName)){
				isRole_Roll = true;
				break;
			}
		}
		if(!isRole_Roll){//当不为学籍办人员时
			//检查是否在申请时间段内
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			java.util.Date ndate = ExDateUtils.getCurrentDateTime();
			try {	
				String ndateStr = ExDateUtils.formatDateStr(ndate, ExDateUtils.PATTREN_DATE);
				ndate = format.parse(ndateStr);
				List<GraduateNograduateSetting> settings = graduateNoSettingService.findByHql(" from "+GraduateNograduateSetting.class.getSimpleName()+" where isDeleted =0");
				for (GraduateNograduateSetting setting : settings) {
					if(!setting.getStartDate().after(ndate)&&!setting.getEndDate().before(ndate)){
						result.put("isPromiss", true);
						break;
					}
				}
			} catch (ParseException e) {
				e.printStackTrace();
				result.put("problem", "时间解析失败。");
			}
		}else{//当为学籍办人员时
			result.put("isPromiss", true);
		}
		renderJson(response,JsonUtils.mapToJson(result));
	}
	/**
	 * 申请时间段列表
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/roll/graduateNo/noGraduateApplyArrangement.html")
	public String listNoGraduateApplyArrangement(Page objPage,HttpServletRequest request,ModelMap model){
		objPage.setOrderBy("yearInfo.yearName");
		objPage.setOrder("DESC");
		String yearInfo = ExStringUtils.trimToEmpty(request.getParameter("yearInfo")) ;
		String term     = ExStringUtils.trimToEmpty(request.getParameter("term"))     ;
		String startDate= ExStringUtils.trimToEmpty(request.getParameter("startDate"));
		String endDate  = ExStringUtils.trimToEmpty(request.getParameter("endDate"))  ;
		String revokeDate  = ExStringUtils.trimToEmpty(request.getParameter("revokeDate"))  ;
		
		Map<String,Object> condition = new HashMap<String,Object>();
		
		if(ExStringUtils.isNotEmpty(yearInfo)) {condition.put("yearInfo", yearInfo);}
		if(ExStringUtils.isNotEmpty(term))     {condition.put("term",term);}
		if(ExStringUtils.isNotEmpty(startDate)){ condition.put("startDate",startDate);}
		if(ExStringUtils.isNotEmpty(endDate))  { condition.put("endDate", endDate);}
		if(ExStringUtils.isNotEmpty(revokeDate))  { condition.put("revokeDate", revokeDate);}
		
		//查询申请时间段设置
		Page page = graduateNoSettingService.findGraduateByCondition(condition, objPage);
		
		model.addAttribute("condition",condition);
		model.addAttribute("page", page);
		
		return"/edu3/roll/graduateNo/graduateNo-arrangement";
	}
	/**
	 * 新增编辑延迟毕业申请时间段
	 * @param resourceid
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	
	@RequestMapping("/edu3/roll/graduateNoSetting/edit.html")
	public String exeEditGraduateNoSetting(String resourceid,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		String method = "";
		if(ExStringUtils.isNotBlank(resourceid)){ //-----编辑
			GraduateNograduateSetting graduateNoSetting = graduateNoSettingService.get(resourceid);	
			method = "modify";
			model.addAttribute("method", method);
			model.addAttribute("graduateNoSetting", graduateNoSetting);
		}else{ //----------------------------------------新增
			method = "new";
			model.addAttribute("method", method);
			model.addAttribute("graduateNoSetting", new GraduateNograduateSetting());			
		}
		return "/edu3/roll/graduateNo/graduateNo-arrangementForm";
	}
	
	/**
	 * 延迟毕业申请时间段保存前的检查
	 * @param resourceid
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	
	@RequestMapping("/edu3/roll/graduateNoSetting/checkBeforeSave.html")
	public void checkBeforeSaveGraduateNoSetting(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		
		Map<String,Object> map =new HashMap<String,Object>(0);
		String type = ExStringUtils.trimToEmpty(request.getParameter("type"));
		String yearInfo = ExStringUtils.trimToEmpty(request.getParameter("yearInfo"));
		String term     = ExStringUtils.trimToEmpty(request.getParameter("term"));
		String beginDate = ExStringUtils.trimToEmpty(request.getParameter("beginDate"));
		String endDate = ExStringUtils.trimToEmpty(request.getParameter("endDate"));
		String revokeDate = ExStringUtils.trimToEmpty(request.getParameter("revokeDate"));
		String resourceid = ExStringUtils.trimToEmpty(request.getParameter("resourceid"));
		if(ExStringUtils.isBlank(type)
				||ExStringUtils.isBlank(yearInfo)
				||ExStringUtils.isBlank(term)
				||ExStringUtils.isBlank(beginDate)
				||ExStringUtils.isBlank(endDate)
				||ExStringUtils.isBlank(revokeDate)){
			map.put("isOk", false);
			map.put("reason", "未完整填写信息");
		}
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		//如果是新建 查询该年度学期有无设置时间段
		if("new".equals(type)){
			String condition ="  isDeleted = 0 "+
			"and yearInfo.resourceid = '"+yearInfo+
			"' and term =   '"+term+"' ";
			List<GraduateNograduateSetting> tmp = graduateNoSettingService.findByHql(" from "+GraduateNograduateSetting.class.getSimpleName()+" where "+condition );
			if(tmp.size()>0){
				map.put("isOk", false);
				map.put("reason", "该年度已经设置了申请延迟毕业时间段。");
			}else{
				try {
					java.util.Date ndatesrc = ExDateUtils.getCurrentDateTime(); 
					String ndatestr = ExDateUtils.formatDateStr(ndatesrc, ExDateUtils.PATTREN_DATE);
					java.util.Date ndate = formatter.parse(ndatestr);
					java.util.Date bdate = formatter.parse(beginDate); 
					java.util.Date edate = formatter.parse(endDate); 
					java.util.Date rdate = formatter.parse(revokeDate); 
					if(rdate.before(edate)||edate.before(bdate)){
						map.put("isOk", false);
						map.put("reason", "恢复时间早于结束时间或结束之间早于开始时间。");
					} else if(edate.before(ndate)){
						map.put("isOk", false);
						map.put("reason", "结束时间早于当前日期，不可更新。");
					} else if(rdate.before(edate)){
						map.put("isOk", false);
						map.put("reason", "自动恢复时间早于结束时间，不可更新。");
					}else{
						//可以新建
						map.put("isOk", true);
					}
				}catch (Exception e) {
					map.put("isOk", false);
					map.put("reason", "时间无法解析。");
					renderJson(response, JsonUtils.mapToJson(map));
				}
			}
			renderJson(response, JsonUtils.mapToJson(map));
		}
		//如果是编辑 恢复时间有无不早于结束时间
		//如果是编辑 结束时间有无早于今日
		//如果是编辑 开始时间有无修改 若修改检查已存在的记录有无早于新设置的开始时间
		if("modify".equals(type)){
			String condition =" isDeleted = 0 "+
					" and resourceid = '"+resourceid+"' ";
			List<GraduateNograduateSetting> settings = graduateNoSettingService.findByHql(" from "+GraduateNograduateSetting.class.getSimpleName()+" where "+condition );
			if(settings.size()>1){
				map.put("isOk", false);
				map.put("reason", "该年度的申请延迟毕业时间段不唯一，不可更新。");
			}else if (settings.size()==0){
				map.put("isOk", false);
				map.put("reason", "该年度的申请延迟毕业时间段意外丢失。");
			}else {
				GraduateNograduateSetting setting = settings.get(0);
				try {
					java.util.Date ndatesrc = ExDateUtils.getCurrentDateTime(); 
					String ndatestr = ExDateUtils.formatDateStr(ndatesrc, ExDateUtils.PATTREN_DATE);
					java.util.Date ndate = formatter.parse(ndatestr);
					java.util.Date bdate = formatter.parse(beginDate); 
					java.util.Date edate = formatter.parse(endDate); 
					java.util.Date rdate = formatter.parse(revokeDate); 
					if(rdate.before(edate)||edate.before(bdate)){
						map.put("isOk", false);
						map.put("reason", "恢复时间早于结束时间或结束之间早于开始时间。");
					} else if(!edate.equals(setting.getEndDate())&&edate.before(ndate)){
						map.put("isOk", false);
						map.put("reason", "结束时间已修改，结束时间早于当前日期，不可更新。");
					} else if(rdate.before(edate)){
						map.put("isOk", false);
						map.put("reason", "自动恢复时间早于结束时间，不可更新。");
					} else {
						List<GraduateNograduate> gApplys = graduateNograduateService.findByHql(" from "+GraduateNograduate.class.getSimpleName()+" where isDeleted=0 and graduateNograduateSetting.resourceid= '"+setting.getResourceid()+"' ");
						if(gApplys.size()>0&&!bdate.equals(setting.getStartDate())){
							map.put("isOk", false);
							map.put("reason", "该时间段已有学生申请延迟毕业，不可修改开始时间。");
						}else{
							map.put("isOk", true);
						}
					}
					
				} catch (Exception e) {
					map.put("isOk", false);
					map.put("reason", "时间无法解析。");
					renderJson(response, JsonUtils.mapToJson(map));
				}
				renderJson(response, JsonUtils.mapToJson(map));	
			}
		}
		map.put("isOk", false);
		map.put("reason", "操作方式无法解析。");
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 保存延迟毕业申请时间段
	 * @param resourceid
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	
	@RequestMapping("/edu3/roll/graduateNoSetting/doSave.html")
	public void doSaveGraduateNoSetting(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		Map<String,Object> map =new HashMap<String,Object>(0);
		String type = ExStringUtils.trimToEmpty(request.getParameter("type"));
		String yearInfo = ExStringUtils.trimToEmpty(request.getParameter("yearInfo"));
		String term     = ExStringUtils.trimToEmpty(request.getParameter("term"));
		String beginDate = ExStringUtils.trimToEmpty(request.getParameter("beginDate"));
		String endDate = ExStringUtils.trimToEmpty(request.getParameter("endDate"));
		String revokeDate = ExStringUtils.trimToEmpty(request.getParameter("revokeDate"));
		String resourceid = ExStringUtils.trimToEmpty(request.getParameter("resourceid"));
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date bdate;
		java.util.Date edate;
		java.util.Date rdate;
		try {
			bdate = formatter.parse(beginDate);
			edate = formatter.parse(endDate); 
			rdate = formatter.parse(revokeDate);
			if("new".equals(type)){//新建
				GraduateNograduateSetting setting = new GraduateNograduateSetting();
				YearInfo year = yearInfoService.findUniqueByProperty("resourceid", yearInfo);
				setting.setYearInfo(year);
				setting.setTerm(term);
				setting.setStartDate(bdate);
				setting.setEndDate(edate);
				setting.setRevokeDate(rdate);
				graduateNoSettingService.save(setting);
				map.put("result", "新建成功。");
			}else if("modify".equals(type)){//更新
				String condition =" isDeleted = 0 "+
						" and resourceid = '"+resourceid+"' ";
				List<GraduateNograduateSetting> settings = graduateNoSettingService.findByHql(" from "+GraduateNograduateSetting.class.getSimpleName()+" where "+condition );
				YearInfo year = yearInfoService.findUniqueByProperty("resourceid", yearInfo);
				GraduateNograduateSetting setting = settings.get(0);
				setting.setYearInfo(year);
				setting.setTerm(term);
				setting.setStartDate(bdate);
				setting.setEndDate(edate);
				setting.setRevokeDate(rdate);
				graduateNoSettingService.saveOrUpdate(setting);	
				map.put("result", "更新成功。");
			}
			renderJson(response, JsonUtils.mapToJson(map));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			map.put("result", "操作失败。");
			renderJson(response, JsonUtils.mapToJson(map));
		} 
	}
	/**
	 * 延迟毕业申请时间段撤销
	 * @param resourceid
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/roll/graduateNoSetting/delete.html")
	public void doDelGraduateNoSetting(String resourceid,HttpServletRequest request,HttpServletResponse response){
	    Map<String,Object> map = new HashMap<String, Object>(0);
	    StringBuffer result = new StringBuffer();
	    try {
	    	if(ExStringUtils.isNotEmpty(resourceid)){
	    		String[] resids = resourceid.split(",");
	    		
	    		for (int i = 0; i < resids.length; i++) {
	    			List<GraduateNograduate> gApplys = graduateNograduateService.findByHql(" from "+GraduateNograduate.class.getSimpleName()+" where  isDeleted = 0 and graduateNograduateSetting.resourceid= '"+resids[i]+"' ");
	    			GraduateNograduateSetting setting = graduateNoSettingService.get(resids[i]);
	    			if(gApplys.size()>0){
	    				result.append(setting.getYearInfo().getYearName()+"-"+JstlCustomFunction.dictionaryCode2Value("CodeTerm", setting.getTerm())+"时间段已有学生申请延迟毕业，不可删除。<br>");
					}else{
						setting.setIsDeleted(1);
						graduateNoSettingService.update(setting);
						result.append(setting.getYearInfo().getYearName()+"-"+JstlCustomFunction.dictionaryCode2Value("CodeTerm", setting.getTerm())+"，已删除。<br>");
					
					}
	    		}
	    		map.put("result", result.toString());
	    		
	    	}
		} catch (Exception e) {
			e.printStackTrace();
			result.append("删除过程中断。<br>");
			map.put("result", result.toString());
			
		}
		renderJson(response, JsonUtils.mapToJson(map));

	}
	@Autowired
	@Qualifier("graduatenograduateservice")
	private IGraduateNograduateService graduateNograduateService;
	@Autowired
	@Qualifier("studentinfoservice")
	private IStudentInfoService studentInfoService;
	@Autowired
	@Qualifier("graduateNoSettingService")
	private IGraduateNoSettingService graduateNoSettingService;
	@Autowired
	@Qualifier("yearInfoService")
	private IYearInfoService yearInfoService;
//	/**
//	 * 批量生效的延迟毕业申请
//	 * @throws WebException
//	 */
//	@RequestMapping("/edu3/roll/graduateNoSetting/batchNoGraduate.html")
//	public void batchNoGraduate(HttpServletRequest request,HttpServletResponse response) throws WebException{
//		Map<String,Object> map = new HashMap<String, Object>(0);
//		StringBuffer result = new StringBuffer();
//		//当前日期
//		Date currentDate = new Date(new java.util.Date(System.currentTimeMillis()).getTime());
//		String parttern="yyyy-MM-dd";
//		String currentdate=new SimpleDateFormat(parttern).format(currentDate); 
//		
//		//所有的延迟毕业申请时间段
//		List<GraduateNograduateSetting> settings = graduateNoSettingService.getAll(); 
//		//如果当前时间等于结束时间，批量设置
//		try{
//			for (GraduateNograduateSetting setting : settings) {
//				Date endDate = new Date(setting.getEndDate().getTime());
//				String enddate=new SimpleDateFormat(parttern).format(endDate); 
//				if(enddate.equals(currentdate)){
//					String settingid = setting.getResourceid();
//					List<GraduateNograduate> noGraduateApplys =  graduateNograduateService.findByHql(" from "+GraduateNograduate.class.getSimpleName()+" where  isDeleted = 0 and  graduateNograduateSetting.resourceid = '"+settingid+"' ");
//					for (GraduateNograduate graduateNograduate : noGraduateApplys) {
//						StudentInfo info = graduateNograduate.getStudentInfo();
//						info.setIsApplyGraduate(Constants.BOOLEAN_WAIT);
//						studentInfoService.saveOrUpdate(info);
//					}
//				}
//			}
//			result.append("批量生效完成。<br>");
//			map.put("result", result.toString());
//		}catch (Exception e) {
//			e.printStackTrace();
//			result.append("批量生效过程意外中断。<br>");
//			map.put("result", result.toString());
//			
//		}
//		renderJson(response, JsonUtils.mapToJson(map));
//	}
//	/**
//	 * 批量恢复的延迟毕业申请
//	 * @throws WebException
//	 */
//	@RequestMapping("/edu3/roll/graduateNoSetting/batchRevoke.html")
//	public void batchRevoke(HttpServletRequest request,HttpServletResponse response) throws WebException{
//		Map<String,Object> map = new HashMap<String, Object>(0);
//		StringBuffer result = new StringBuffer();
//		//当前日期
//		Date currentDate = new Date(new java.util.Date(System.currentTimeMillis()).getTime());
//		String parttern="yyyy-MM-dd";
//		String currentdate=new SimpleDateFormat(parttern).format(currentDate); 
//		
//		//所有的延迟毕业申请时间段
//		List<GraduateNograduateSetting> settings = graduateNoSettingService.getAll(); 
//		//如果当前时间等于自动恢复时间，批量恢复
//		try{
//			for (GraduateNograduateSetting setting : settings) {
//				Date revokeDate = new Date(setting.getRevokeDate().getTime());
//				String revokedate=new SimpleDateFormat(parttern).format(revokeDate); 
//				if(revokedate.equals(currentdate)){
//					String settingid = setting.getResourceid();
//					//专科
//					List<GraduateNograduate> noGraduateApplys =  graduateNograduateService.findByHql(" from "+GraduateNograduate.class.getSimpleName()+" where  isDeleted = 0 and graduateNograduateSetting.resourceid = '"+settingid+"' " +
//							" and studentInfo.classic.classicCode ='3' ");
//					for (GraduateNograduate graduateNograduate : noGraduateApplys) {
//						StudentInfo info = graduateNograduate.getStudentInfo();
//						info.setIsApplyGraduate(Constants.BOOLEAN_NO);
//						studentInfoService.saveOrUpdate(info);
//					}
//					//专科以上
//					noGraduateApplys =  graduateNograduateService.findByHql(" from "+GraduateNograduate.class.getSimpleName()+" where  isDeleted = 0 and graduateNograduateSetting.resourceid = '"+settingid+"' " +
//							" and studentInfo.classic.classicCode !='3' ");
//					for (GraduateNograduate graduateNograduate : noGraduateApplys) {
//						StudentInfo info = graduateNograduate.getStudentInfo();
//						info.setIsApplyGraduate(Constants.BOOLEAN_YES);
//						studentInfoService.saveOrUpdate(info);
//						
//					}
//				}
//			}
//			result.append("批量恢复完成。<br>");
//			map.put("result", result.toString());
//		}catch (Exception e) {
//			e.printStackTrace();
//			result.append("批量恢复过程意外中断。<br>");
//			map.put("result", result.toString());
//			
//		}
//		renderJson(response, JsonUtils.mapToJson(map));
//	}
	/**
	 * 批量操作延迟毕业申请生效状态
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/roll/graduateNo/batchEffect.html")
	public void batchEffective(HttpServletRequest request,HttpServletResponse response,ModelMap model,String operation ) throws WebException{
		String resourceid = request.getParameter("resourceid");
		StringBuffer messageStr = new StringBuffer();
		Map<String ,Object> map = new HashMap<String, Object>();
		User currentUser = SpringSecurityHelper.getCurrentUser();
		Set<Role> roles = currentUser.getRoles();
		boolean isRole_Roll = false;
		for (Role role : roles) {
			String roleName = role.getRoleCode();
			if(roleName.contains("ROLE_ROLL")||"ROLE_XUEJIMANAGER".equals(roleName)){
				isRole_Roll = true;
				break;
			}
		}
		Date currentDate = new Date(System.currentTimeMillis());
		StringBuffer errorMsg = new StringBuffer();
		try {
			if(isRole_Roll&&ExStringUtils.isNotBlank(operation)){
				if(ExStringUtils.isNotBlank(resourceid)){					
					String[] resid = resourceid.split("\\,");
					for (String id : resid) {
						GraduateNograduate graduateUnit = graduateNograduateService.get(id);
						StudentInfo stuInfo = graduateUnit.getStudentInfo();
						java.util.Date revokeDate = graduateUnit.getGraduateNograduateSetting().getRevokeDate();
						if (currentDate.after(revokeDate)&&"Y".equals(operation)){
							errorMsg.append("<br>"+stuInfo.getStudentName()+"生效失败,原因:关联批次已过期，无法生效。");
							continue;
						}
						if("Y".equals(operation)){
							stuInfo.setIsApplyGraduate(Constants.BOOLEAN_WAIT);
						} else if ("N".equals(operation)){
							//撤销生效时，专科生 延迟毕业字段设为N-申请毕业 专科生以上学历设为Y-申请毕业+申请学位
							if("3".equals(stuInfo.getClassic().getClassicCode())){
								stuInfo.setIsApplyGraduate(Constants.BOOLEAN_NO);
							}else{
								stuInfo.setIsApplyGraduate(Constants.BOOLEAN_YES);
							}
						}
						studentInfoService.update(stuInfo);
						graduateUnit.setStudentInfo(stuInfo);
						graduateNograduateService.update(graduateUnit);
					}
				}
				map.put("statusCode", 200);
				map.put("message", messageStr.toString()+"</br><!--批量-->操作完成！"+errorMsg.toString());				
				map.put("forward", request.getContextPath()+"/edu3/roll/graduateNo/list.html");
			}else{
				messageStr.append("您不是学籍办用户，不具备此权限。");
				map.put("statusCode", 300);
				map.put("message", messageStr.toString()+"</br><!--批量-->操作中止！");		
			}
			
		} catch (Exception e) {
			logger.error("批量生效出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", messageStr+toString()+"<!--批量-->生效出错！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 导出学位审核信息
	 * @param request
	 * @param response
	 * @param map
	 * @throws WebException
	 */
	@RequestMapping("/edu3/roll/graduateNo/exportExcel.html")
	public void doExportDegreeAuditExcel(HttpServletRequest request,HttpServletResponse response,ModelMap map) throws WebException{
		Page objPage = new Page();
		objPage.setOrderBy("applayDate");
		objPage.setOrder(Page.ASC);//设置默认排序方式
		Map<String,Object> condition = new HashMap<String, Object>(0);
			
		String active = ExStringUtils.trimToEmpty(request.getParameter("active"));//
		String branchSchool = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));//
		String grade = ExStringUtils.trimToEmpty(request.getParameter("grade"));//
		String major = ExStringUtils.trimToEmpty(request.getParameter("major"));//
		String name = ExStringUtils.trimToEmpty(request.getParameter("name"));//
		String studyNo = ExStringUtils.trimToEmpty(request.getParameter("studyNo"));//
		String classic = ExStringUtils.trimToEmpty(request.getParameter("classic"));//
		String applyNoGraduationPlan_sel = ExStringUtils.trimToEmpty(request.getParameter("applyNoGraduationPlan_sel"));//
		Date currentDate = new Date(System.currentTimeMillis());
		condition.put("currentDate", currentDate);
			
		if(ExStringUtils.isNotEmpty(active)) {
			condition.put("active", active);
		}
		if(ExStringUtils.isNotEmpty(branchSchool)) {
			condition.put("branchSchool", branchSchool);
		}
		if(ExStringUtils.isNotEmpty(grade)) {
			condition.put("grade", grade);
		}
		if(ExStringUtils.isNotEmpty(major)) {
			condition.put("major", major);
		}
		if(ExStringUtils.isNotEmpty(name)) {
			condition.put("name", name);
		}
		if(ExStringUtils.isNotEmpty(studyNo)) {
			condition.put("studyNo", studyNo);
		}
		if(ExStringUtils.isNotEmpty(classic)) {
			condition.put("classic", classic);
		}
		if(ExStringUtils.isNotEmpty(applyNoGraduationPlan_sel)) {
			condition.put("applyNoGraduationPlan", applyNoGraduationPlan_sel);
		}
			
		List<GraduateNograduate> graduateNograduates = graduateNograduateService.findGraduateByCondition_List(condition, objPage);
	    List<Map<String, Object>> resultList 		 = new ArrayList<Map<String,Object>>(0);
		for (GraduateNograduate gn : graduateNograduates) {
			Map<String,Object> map_tmp = new HashMap<String, Object>(0);
			StudentInfo st = gn.getStudentInfo();
			GraduateNograduateSetting gns = gn.getGraduateNograduateSetting();
			YearInfo year = gns.getYearInfo();
			String yearInfo = year.getYearName();
			String term = gns.getTerm();
			map_tmp.put("studyNo", st.getStudyNo());
			map_tmp.put("studentName", st.getStudentName());
			map_tmp.put("grade", st.getGrade().getGradeName());
			map_tmp.put("major", st.getMajor().getMajorName());
			map_tmp.put("classic", st.getClassic().getClassicName());
			map_tmp.put("unit", st.getBranchSchool().getUnitName());
			map_tmp.put("graduateSetting",yearInfo+("1".equals(term)?"第一学期":"第二学期"));
			map_tmp.put("applyDate", gn.getApplayDate());
			if("W".equals(st.getIsApplyGraduate())&& !currentDate.after( gns.getRevokeDate()) && !currentDate.before(gns.getEndDate()) ){
				map_tmp.put("active", "生效");
			}else{
				map_tmp.put("active", "未生效");
			}
			resultList.add(map_tmp);
		}
		FileUploadAndDownloadSupportController fileup = new FileUploadAndDownloadSupportController();
		//文件输出服务器路径
		fileup.setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
		try{
			if(0==resultList.size()){
				throw new Exception("此查询条件下的数据为空。");
			}
			//导出
			File excelFile = null;
			GUIDUtils.init();
			File disFile = new File(fileup.getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(false) + ".xls");
		
			exportExcelService.initParmasByfile(disFile, "noGraduateApplys", resultList,null);
			exportExcelService.getModelToExcel().setHeader("延迟毕业申请表");//设置大标题
			exportExcelService.getModelToExcel().setRowHeight(500);//设置行高
			excelFile = exportExcelService.getExcelFile();//获取导出的文件
			logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());
			fileup.downloadFile(response, "延迟毕业申请表.xls", excelFile.getAbsolutePath(),true);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("导出excel文件出错："+e.fillInStackTrace());
		}
		
	}
	
}
