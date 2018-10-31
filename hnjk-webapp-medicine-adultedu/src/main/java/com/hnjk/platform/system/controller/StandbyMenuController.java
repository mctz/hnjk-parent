package com.hnjk.platform.system.controller;

import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.utils.BlowfishSecurityCodeUtils;
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.configuration.property.ConfigPropertyUtil;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.controller.BaseSupportController;
import com.hnjk.edu.basedata.model.Major;
import com.hnjk.edu.basedata.service.IGradeService;
import com.hnjk.edu.basedata.service.IMajorService;
import com.hnjk.edu.recruit.model.RecruitMajor;
import com.hnjk.edu.recruit.service.IRecruitMajorService;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.roll.service.IStudentInfoService;
import com.hnjk.edu.teaching.model.ExamResults;
import com.hnjk.edu.teaching.model.TeachingGuidePlan;
import com.hnjk.edu.teaching.model.TeachingPlan;
import com.hnjk.edu.teaching.service.IExamInfoService;
import com.hnjk.edu.teaching.service.IExamResultsService;
import com.hnjk.edu.teaching.service.ITeachingGuidePlanService;
import com.hnjk.edu.teaching.service.ITeachingPlanService;
import com.hnjk.edu.util.Condition2SQLHelper;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.Attachs;
import com.hnjk.platform.system.model.UserOperationLogs;
import com.hnjk.platform.system.service.IAttachsService;
import com.hnjk.platform.system.service.IStandbyMenuService;
import com.hnjk.platform.system.service.IUserOperationLogsService;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;
import lombok.Cleanup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.*;

/**
 * 
 * <code>DictionaryController</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： Mar 10, 2010 3:35:01 PM
 * @see 
 * @version 1.0
 */
@Controller
public class StandbyMenuController extends BaseSupportController {
	
	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao jdbcDao;
	
	@Autowired
	@Qualifier("attachsService")
	private IAttachsService attachsService;
	
	@Autowired
	@Qualifier("examInfoService")
	private IExamInfoService examInfoService;
	
	@Autowired
	@Qualifier("examResultsService")
	private IExamResultsService examResultsService;
	
	@Autowired
	@Qualifier("gradeService")
	private IGradeService gradeService;
	
	@Autowired
	@Qualifier("majorService")
	private IMajorService majorService;
	
	@Autowired
	@Qualifier("recruitMajorService")
	private IRecruitMajorService recruitMajorService;
	
	@Autowired
	@Qualifier("teachingplanservice")
	private ITeachingPlanService teachingPlanService;
	
	@Autowired
	@Qualifier("teachingguideplanservice")
	private ITeachingGuidePlanService teachingGuidePlanService;
	
	@Autowired
	@Qualifier("studentinfoservice")
	private IStudentInfoService studentInfoService;
	
	@Autowired
	@Qualifier("standbyMenuService")
	private IStandbyMenuService standbyMenuService;
	
	@Autowired
	@Qualifier("userOperationLogsService")
	private IUserOperationLogsService userOperationLogsService;
	
	/**
	 * 系统自定义功能菜单
	 * @param request
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/edu3/system/menu/list.html")
	public String exeList(HttpServletRequest request, ModelMap model) throws WebException {
		User curUser = SpringSecurityHelper.getCurrentUser();
		Map<String, Object> condition = Condition2SQLHelper.getConditionFromResquestByIterator(request);
		String confirmPassword = ConfigPropertyUtil.getInstance().getProperty("confirmPassword");
		//BlowfishSecurityCodeUtils crypt = new BlowfishSecurityCodeUtils("confirmPassword");
		//confirmPassword = crypt.decryptString(confirmPassword);
		model.addAttribute("username", curUser.getUsername());
		model.addAttribute("confirmPassword", confirmPassword);
		model.addAttribute("condition", condition);
		return "/system/standbyMenu/menu-list";
	}
	
	/**
	 * 选择成绩文件
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/bivariateExamResult/inputScore.html")
	public String inputScore(ModelMap model) throws WebException {
		model.addAttribute("title", "导入课程成绩");
		model.addAttribute("formId", "bivariateExamResult_import");
		model.addAttribute("url", "/edu3/teaching/bivariateExamResult/importScore.html");
		return "edu3/roll/inputDialogForm";
	}
	
	/**
	 * 导入成绩（参考汕大二维成绩表）
	 * 1、数据以“序号”行开始，并且第一列为“序号”，第二列为“学号”
	 * 2、数据以“课程**”列开始，并且第一行为课程编码，第二行为课程名称，三四行可为学期、学分
	 * @param request
	 * @param response
	 * @param exportAct
	 * @throws WebException
	 * @throws IOException
	 * @throws ServletRequestBindingException
	 */
	@RequestMapping("/edu3/teaching/bivariateExamResult/importScore.html")
	public void importScore(HttpServletRequest request, HttpServletResponse response, String exportAct) throws WebException, IOException, ServletRequestBindingException {
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
					Map<String, Object> singleMap = standbyMenuService.importBivariateExamResult(filePath);
					if (singleMap != null && singleMap.size() > 0) {
						int totalCount = (Integer) singleMap.get("totalCount");
						int successCount = (Integer) singleMap.get("successCount");
						message.append((String) singleMap.get("message"));
						result = "导入共" + totalCount + "条,成功" + successCount + "条,失败" + (totalCount - successCount) + "条";
					}
					attachsService.delete(attachs);
				}
				
			} while (false);
		} catch (Exception e) {
			logger.error("处理导入成绩出错", e);
			success = false;
			result = "导入失败";
		} finally {
			returnMap.put("success",success);
			returnMap.put("msg",message);
			returnMap.put("result",result);
			renderJson(response,JsonUtils.mapToJson(returnMap));
		}
	}
	
	/**
	 * 修复学生登录问题
	 * @param response
	 */
	@RequestMapping("/edu3/system/standby/studentLogin.html")
	public void studentLogin403(HttpServletRequest request, HttpServletResponse response){
		Map<String,Object> map = new HashMap<String, Object>();
		User curUser = SpringSecurityHelper.getCurrentUser();
		String date = ExDateUtils.getCurrentDate();
		String sql = "insert into hnjk_sys_roleusers (USERID, ROLEID) select u.resourceid,r.resourceid from hnjk_sys_users u"
				+" left join hnjk_sys_roleusers ru on ru.userid=u.resourceid,hnjk_sys_roles r where ru.roleid is null";
		List<Map<String, Object>> mapList = new ArrayList<Map<String,Object>>();
		//学生
		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put("usertype", "student");
		map1.put("rolecode", "'ROLE_STUDENT'");
		mapList.add(map1);
		//教学中心主讲老师
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("unitid", "4028817655158c180155439943200205");//教学中心
		map2.put("rolecode", "'ROLE_LINE','ROLE_TEACHER_DUTY'");
		mapList.add(map2);
		//启用在学学生账号
		String sql2 = "update hnjk_sys_users us set us.enabled='1' where us.resourceid in(";
		sql2 += "select u.resourceid from edu_roll_studentinfo si join hnjk_sys_users u on u.resourceid=si.sysuserid";
		sql2 +=" where si.studentstatus='11' and u.enabled='0')";
		//修复学籍状态为休学
		String sql3 = "update edu_roll_studentinfo si set si.studentstatus='12' ";
		sql3 += " where si.studentstatus='11' and si.resourceid in(select sci.studentid from edu_roll_stuchangeinfo sci";
		sql3 += " where sci.changetype='11' and sci.finalauditstatus='Y' and sci.isdeleted=0 and sci.studentid not in(";
		sql3 += " select scinfo.studentid from edu_roll_stuchangeinfo scinfo";
		sql3 += "  where scinfo.changetype='12' and sci.finalauditstatus='Y' and scinfo.isdeleted=0))";
		String sql4 = "update hnjk_sys_users us set us.enabled='0' where us.enabled='1' and us.resourceid in(";
		sql4 += "select si.sysuserid from edu_roll_studentinfo si where si.studentstatus='12')";
		//根据学籍状态恢复毕业生数据
		String sql5 = "update edu_teach_graduatedata gd set gd.isdeleted=0 where gd.isdeleted=1 and gd.resourceid in(";
		sql5 += " select sa.graduatedataid from edu_roll_stuaudit sa join edu_roll_studentinfo si on si.resourceid=sa.studentinfoid";
		sql5 += " where si.studentstatus in('16','24') and sa.isdeleted=0 and sa.graduatedataid=gd.resourceid)";
			//毕业类型：1-毕业；2-结业；学籍状态：16-毕业；24-结业
		String sql5_1 = "update edu_teach_graduatedata gd set gd.graduatetype='2' where gd.graduatetype!='2' and gd.isdeleted=0";
		sql5_1 += " and gd.studentid in(select si.resourceid from edu_roll_studentinfo si where si.studentstatus='24')";
		
		String sql5_2 = "update edu_teach_graduatedata gd set gd.graduatetype='1' where gd.graduatetype!='1' and gd.isdeleted=0";
		sql5_2 += " and gd.studentid in(select si.resourceid from edu_roll_studentinfo si where si.studentstatus='16')";
		// update edu_teach_graduatedata gd set gd.isallowsecgraduate='Y' where gd.isallowsecgraduate!='Y' and gd.studentid in(
		//  select si.resourceid from edu_roll_studentinfo si where si.studentstatus='24');
		
		// update edu_teach_graduatedata gd set gd.isallowauditdegree='Y' where gd.isallowauditdegree!='Y' and gd.studentid in(
		//	select si.resourceid from edu_roll_studentinfo si join edu_base_classic ci on ci.resourceid=si.classicid
		//	where si.studentstatus in('16','24') and ci.classicname not in('专科','高起专'));
		try {
			int count = 0;
			int count2 = 0;
			int count3 = 0;
			int count5 = 0;
			int count5_1 = 0;
			int count5_2 = 0;
			for (Map<String, Object> tempMap : mapList) {
				StringBuffer buffer = new StringBuffer();
				if(tempMap.containsKey("usertype")){
					buffer.append(" and u.usertype=:usertype");
				}
				if(tempMap.containsKey("unitid")){
					buffer.append(" and u.unitid=:unitid");
				}
				if(tempMap.containsKey("rolecode")){
					buffer.append(" and r.rolecode in("+tempMap.get("rolecode")+")");
				}
				count += jdbcDao.getBaseJdbcTemplate().executeForMap(sql+buffer.toString(), tempMap);
			}
			count2 = jdbcDao.getBaseJdbcTemplate().executeForMap(sql2, map);
			count5 = jdbcDao.getBaseJdbcTemplate().executeForMap(sql5, map);
			count5_1 = jdbcDao.getBaseJdbcTemplate().executeForMap(sql5_1, map);
			count5_2 = jdbcDao.getBaseJdbcTemplate().executeForMap(sql5_2, map);
			if (count5 == 0) {
				count5 = count5_1;
			}
			map.put("statusCode", 200);
			if(count==0 && count2==0 && count3==0){
				map.put("message", "无需修复数据！");
			}else {
				map.put("message", (count==0?"":(count+"个用户设置权限成功！"))+(count2==0?"":(count2+"个学生启用账号成功！"))
						+(count3==0?"":(count3+"个学生修复学籍状态成功！")) + (count5==0?"":(count5+"个学生毕业数据恢复成功！")));
			}
			UserOperationLogs userOperationLog = new UserOperationLogs();
			userOperationLog.setUserName(curUser.getCnName());
			userOperationLog.setUserId(curUser.getResourceid());
			userOperationLog.setOperationContent(map.get("message").toString()+"。参数："+Condition2SQLHelper.getConditionFromResquestByIterator(request));
			userOperationLog.setOperationType(UserOperationLogs.UPDATE);
			userOperationLog.setRecordTime(new Date());
			userOperationLog.setIpaddress(request.getLocalAddr());
			userOperationLog.setModules("8");
			userOperationLogsService.persist(userOperationLog);
			logger.info("修复学生数据：操作人："+curUser.getCnName()+" 操作人ID："+curUser.getResourceid()+" 操作时间："+date);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			map.put("statusCode", 300);
			map.put("message", "操作失败！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 删除补考名单
	 * @param response
	 */
	@RequestMapping("/edu3/system/standby/deleteMakeupList.html")
	public void deleteMakeupList(HttpServletRequest request, HttpServletResponse response){
		Map<String,Object> map = new HashMap<String, Object>();
		User curUser = SpringSecurityHelper.getCurrentUser();
		String date = ExDateUtils.getCurrentDate();
		//如果成绩已删除，则删除补考名单
		String sql_1 = "update edu_teach_makeuplist ml set ml.memo=ml.memo||'#"+date+"删除',ml.isdeleted=1 where ml.isdeleted=0 and (ml.resultsid in("
				+" select er.resourceid from edu_teach_examresults er where er.isdeleted=0 and f_decrypt_score(er.integratedscore,er.studentid)>60)"
				+" or ml.resultsid in(select er.resourceid from edu_teach_examresults er where er.isdeleted=1))";
		//如果补考名单重复，则删除补考名单
		String sql_2 = "update edu_teach_makeuplist ml set ml.memo=ml.memo||'#"+date+"删除',ml.isdeleted=1 where ml.resourceid in(";
		sql_2 += "select resourceid from(select ml.resourceid,row_number()";
		sql_2 += " over(partition by ml.studentid,ml.nextexamsubid,ml.resultsid,ml.courseid order by ml.studentid,ml.resultsid) rn";
		sql_2 += " from edu_teach_makeuplist ml where ml.isdeleted=0 and (ml.studentid,ml.nextexamsubid,ml.resultsid,ml.courseid)";
		sql_2 += " in(select ml.studentid,ml.nextexamsubid,ml.resultsid,ml.courseid from edu_teach_makeuplist ml";
		sql_2 += "  join edu_teach_examresults er on er.resourceid=ml.resultsid and er.isdeleted=0 where ml.isdeleted=0 having count(*)>1";
		sql_2 += "  group by ml.studentid,ml.nextexamsubid,ml.resultsid,ml.courseid)";
		sql_2 += ")where rn=1)";
		try {
			int count1 = jdbcDao.getBaseJdbcTemplate().executeForMap(sql_1, new HashMap<String, Object>());
			int count2 = jdbcDao.getBaseJdbcTemplate().executeForMap(sql_2, new HashMap<String, Object>());
			map.put("statusCode", 200);
			map.put("message", "成功删除 "+count1+count2+" 条补考名单！");
			UserOperationLogs userOperationLog = new UserOperationLogs();
			userOperationLog.setUserName(curUser.getCnName());
			userOperationLog.setUserId(curUser.getResourceid());
			userOperationLog.setOperationContent("删除无效补考名单（"+count1+count2+"）。参数："+Condition2SQLHelper.getConditionFromResquestByIterator(request));
			userOperationLog.setOperationType(UserOperationLogs.DELETE);
			userOperationLog.setRecordTime(new Date());
			userOperationLog.setIpaddress(request.getLocalAddr());
			userOperationLog.setModules("5");
			userOperationLogsService.persist(userOperationLog);
			logger.info("删除补考名单：操作人："+curUser.getCnName()+" 操作人ID："+curUser.getResourceid()+" 操作时间："+date);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			map.put("statusCode", 300);
			map.put("message", "操作失败！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 选择sql文件
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/system/standby/inputSql.html")
	public String inputSql(ModelMap model) throws WebException {
		model.addAttribute("title", "导入sql文件");
		model.addAttribute("formId", "standbyMenu_SqlFile");
		model.addAttribute("url", "/edu3/system/standby/importSql.html");
		return "edu3/roll/inputDialogForm";
	}
	
	/**
	 * 导入sql文件（用于恢复和修改数据）
	 * 1、此功能只开放给管理员使用
	 * 2、每条sql语句必须换行，并且语句内部不能出现注释，可以在每条sql的开头或结尾
	 * 3、sql语句权限控制通过数据字典“CodeSqlOperateType”控制（update,insert,delete,alter）
	 * 4、每条‘insetr’sql不能是复合查询语句，否则会跳过验证是否存在，并且直接执行
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/system/standby/importSql.html")
	public void importSql(HttpServletRequest request, HttpServletResponse response){
		String message = "";
		Map<String, Object > returnMap = new HashMap<String, Object>();
		boolean success = true;
		String result = "";
		String date = ExDateUtils.getCurrentDate();
		User curUser = SpringSecurityHelper.getCurrentUser();
		Map<String, Object> singleMap = new HashMap<String, Object>();
		//读取文件信息
		try {
			String attchID = ExStringUtils.trimToEmpty(request.getParameter("attachId"));
			if (null != attchID && attchID.split(",").length > 1) {
				success = false;
				message = "一次只能导入一个成绩文件,谢谢！";
			} else if (null != attchID && attchID.split(",").length == 1) {
				Attachs attachs = attachsService.get(attchID.split(",")[0]);
				String filePath = attachs.getSerPath() + File.separator + attachs.getSerName();
				singleMap = standbyMenuService.importSql(filePath);
				if (singleMap != null && singleMap.size() > 0) {
					int insertCount = (Integer) singleMap.get("insertCount");
					int updateCount = (Integer) singleMap.get("updateCount");
					int deleteCount = (Integer) singleMap.get("deleteCount");
					
					message = singleMap.get("message")==null?"":singleMap.get("message").toString();
					result = "一共新增"+insertCount+"条记录，更新"+updateCount+"条记录，删除"+deleteCount+"条记录！"+"\n修改到的表："+singleMap.get("tableNames").toString();
					UserOperationLogs userOperationLog = new UserOperationLogs();
					userOperationLog.setUserName(curUser.getCnName());
					userOperationLog.setUserId(curUser.getResourceid());
					userOperationLog.setOperationContent("执行sql文件："+result+"。");
					userOperationLog.setOperationType(UserOperationLogs.EXE_FILE);
					userOperationLog.setRecordTime(new Date());
					userOperationLog.setIpaddress(request.getLocalAddr());
					userOperationLog.setModules("8");
					userOperationLog.setAttachs(attachs);
					userOperationLogsService.persist(userOperationLog);
					logger.debug(result);
					logger.debug("执行sql文件：操作人："+curUser.getCnName()+" 操作人ID："+curUser.getResourceid()+" 操作时间："+date);
				}
			}
		} catch (Exception e) {
			message = e.getMessage();
			e.printStackTrace();
		} finally {
			returnMap.put("success",success);
			returnMap.put("msg",message);
			returnMap.put("result",result);
			renderJson(response,JsonUtils.mapToJson(returnMap));
		}
	}
	
	/**
	 * 查看sql导入记录
	 * @param request
	 * @return
	 */
	@RequestMapping("/edu3/system/standby/sqlFileList.html")
	public String getSqlFiles(HttpServletRequest request, Page objPage, ModelMap model){
		Map<String, Object> condition = Condition2SQLHelper.getConditionFromResquestByIterator(request);
		objPage.setOrderBy(Page.ASC);
		objPage.setOrder("attName");
		if(condition.containsKey("order")){
			objPage.setOrderBy(condition.get("order").toString());
			objPage.setOrder(Page.DESC);
		}
		if(condition.containsKey("orderBy")){
			objPage.setOrder(condition.get("orderBy").toString());
		}
		StringBuilder builder = new StringBuilder();
		builder.append("from "+Attachs.class.getSimpleName()+" where isDeleted=0 and formId='standbyMenu_SqlFile'");
		if(condition.containsKey("fileName")){
			builder.append(" and attName like '%"+condition.get("fileName")+"'%");
		}
		if(condition.containsKey("fillinName")){
			builder.append(" and fillinName = :fillinName");
		}
		
 		Page filePage = attachsService.findByHql(objPage, builder.toString(), condition);
 		model.addAttribute("filePage", filePage);
 		model.addAttribute("condition", condition);
		return "/system/standbyMenu/sql-list";
	}
	/**
	 * 更新成绩显示（关联教学计划课程和学习计划）
	 * @param response
	 */
	@RequestMapping("/edu3/system/standby/updateExamShow.html")
	public void updateExamShow(HttpServletRequest request, HttpServletResponse response){
		Map<String,Object> map = new HashMap<String, Object>();
		String date = ExDateUtils.getCurrentDate();
		User curUser = SpringSecurityHelper.getCurrentUser();
		Map<String, Object> condition = Condition2SQLHelper.getConditionFromResquestByIterator(request);
		StringBuilder valuesBuilder = new StringBuilder();
		if(condition.containsKey("branchSchool")){
			valuesBuilder.append(" and si.branchschoolid=:branchSchool");
		}
		if(condition.containsKey("grade")){
			valuesBuilder.append(" and si.gradeid=:grade");
		}
		if(condition.containsKey("classic")){
			valuesBuilder.append(" and si.classicid=:classic");
		}
		if(condition.containsKey("major")){
			valuesBuilder.append(" and si.majorid=:major");
		}
		if(condition.containsKey("classes")){
			valuesBuilder.append(" and si.classesid=:classes");
		}
		if(condition.containsKey("studyNo")){
			valuesBuilder.append(" and si.studyno=:studyNo");
		}
		if(condition.containsKey("courseid")){
			valuesBuilder.append(" and er.courseid in(:courseid)");
		}
		//更新成绩表专业课程
		String updateMajorCourse = "update edu_teach_examresults er set er.majorcourseid=("
				+"select pc.resourceid from edu_teach_plancourse pc join edu_roll_studentinfo si on si.teachplanid=pc.planid"
				+" where si.resourceid=er.studentid and pc.courseid=er.courseid and pc.isdeleted=0) where er.isdeleted=0"
				+ " and er.majorcourseid!=(select pc.resourceid from edu_teach_plancourse pc join edu_roll_studentinfo si on si.teachplanid=pc.planid"
				+ " where si.resourceid=er.studentid and pc.courseid=er.courseid and pc.isdeleted=0)";
		//更新成绩表学时
		StringBuilder updateStudyHour = new StringBuilder();
		updateStudyHour.append("update edu_teach_examresults er set er.stydyhour=(");
		updateStudyHour.append(" select pc.stydyhour from edu_roll_studentinfo si ");
		updateStudyHour.append(" join edu_teach_plancourse pc on pc.planid=si.teachplanid and pc.isdeleted=0");
		updateStudyHour.append(" where er.stydyhour!=pc.stydyhour and si.resourceid=er.studentid and pc.courseid=er.courseid)");
		updateStudyHour.append(" where er.isdeleted=0 and (er.studentid,er.courseid) in( select si.resourceid,pc.courseid from edu_roll_studentinfo si ");
		updateStudyHour.append(" join edu_teach_plancourse pc on pc.planid=si.teachplanid and pc.isdeleted=0");
		updateStudyHour.append(" where er.stydyhour!=pc.stydyhour and si.resourceid=er.studentid and pc.courseid=er.courseid");
		updateStudyHour.append(valuesBuilder);
		updateStudyHour.append(" )");
		//更新学习计划
		String updateStudyPlan = "update edu_learn_stuplan sp set sp.status='3',(sp.examresultsid,sp.examinfoid)=(select A.resourceid,A.examinfoid from("
				+" select er.resourceid,er.studentid,er.courseid,pc.resourceid plancourseid,er.examsubid,er.examinfoid,er.ismakeupexam,"
				+" row_number() over(partition by er.studentid,er.courseid order by decode(er.ismakeupexam,'Q',1,'T',2,'Y',3,4)) rn "
				+" from edu_teach_examresults er join edu_roll_studentinfo si on si.resourceid=er.studentid "
				+" join edu_teach_plancourse pc on pc.planid=si.teachplanid and pc.courseid=er.courseid and pc.isdeleted=0"
				+"  where er.checkstatus='4' and er.isdeleted=0)A where A.rn = 1 and A.studentid=sp.studentid and A.plancourseid=sp.plansourceid"
				+" )where sp.examresultsid in(select ers.resourceid from edu_teach_examresults ers where ers.isdeleted=1)"
				+" and (sp.examresultsid is null or sp.examinfoid is null)";
		//更新考试批次
		StringBuilder updateExamSub = new StringBuilder();
		updateExamSub.append("select er.resourceid,si.studyno,si.studentname,er.courseid,c.coursename,cs.term openterm,es.batchname,");
		updateExamSub.append(" es.resourceid examsubid,ei.resourceid examinfoid,er.examinfoid org_examinfoid from edu_roll_studentinfo si");
		updateExamSub.append(" join edu_teach_guiplan gp on gp.planid=si.teachplanid and gp.gradeid=si.gradeid and gp.isdeleted=0");
		updateExamSub.append(" join edu_teach_plancourse pc on pc.planid=si.teachplanid and pc.isdeleted=0");
		updateExamSub.append(" join edu_teach_coursestatus cs on cs.guiplanid=gp.resourceid and cs.plancourseid=pc.resourceid and cs.schoolids=si.branchschoolid");
		updateExamSub.append("  and cs.isopen='Y' and cs.schoolids=si.branchschoolid and cs.isdeleted=0");
		updateExamSub.append(" join edu_base_year y on y.isdeleted=0 and y.firstyear=substr(cs.term, 0, 4) and y.isdeleted=0");
		updateExamSub.append(" join edu_teach_examresults er on er.studentid=si.resourceid and er.courseid=pc.courseid and er.isdeleted=0");
		updateExamSub.append(" join edu_base_course c on c.resourceid=pc.courseid");
		updateExamSub.append(" join edu_teach_examsub es on es.yearid=y.resourceid and es.term=substr(cs.term, 7, 1) and es.examtype=er.ismakeupexam");
		updateExamSub.append("  and er.examsubid!=es.resourceid and er.ismakeupexam='N' and es.isdeleted=0");
		updateExamSub.append(" left join edu_teach_examinfo ei on ei.examsubid=es.resourceid and ei.courseid=c.resourceid and ei.examcoursetype=decode(cs.teachtype,'networkTeach',0,'faceTeach',1,2) and ei.isdeleted=0");
		updateExamSub.append(" where si.isdeleted=0");
		updateExamSub.append(valuesBuilder);
		updateExamSub.append(" order by si.studyno,cs.term");
		try {
			int count1 = jdbcDao.getBaseJdbcTemplate().executeForMap(updateMajorCourse, new HashMap<String, Object>());
			int count2 = jdbcDao.getBaseJdbcTemplate().executeForMap(updateStudyHour.toString(), condition);
			int count3 = jdbcDao.getBaseJdbcTemplate().executeForMap(updateStudyPlan, new HashMap<String, Object>());
			List<Map<String, Object>> mapList = jdbcDao.getBaseJdbcTemplate().findForListMap(updateExamSub.toString(), condition);
			int count4 = 0;
			if(mapList!=null && mapList.size()>0){
				count4 = mapList.size();
				Set<Map<String, Object>> examSet = new HashSet<Map<String,Object>>();
				for (Map<String, Object> map2 : mapList) {
					Map<String, Object> temp = new HashMap<String, Object>();
					String resourceid = map2.get("resourceid").toString();
					String examsubid = map2.get("examsubid").toString();
					String org_examinfoid = map2.get("org_examinfoid").toString();
					String examinfoid = map2.get("examinfoid")==null?org_examinfoid:map2.get("examinfoid").toString();
					temp.put("resourceid", resourceid);
					temp.put("examsubid", examsubid);
					temp.put("examinfoid", examinfoid);
					examSet.add(temp);
				}
				String hql = "update edu_teach_examresults er set er.examsubid=:examsubid,er.examinfoid=:examinfoid where er.resourceid=:resourceid";
				jdbcDao.getBaseJdbcTemplate().getJdbcTemplate().batchUpdate(hql, examSet.toArray(new Map[count2]));
			}
			map.put("statusCode", 200);
			if(count1==0 && count2==0 && count3==0 && count4==0){
				map.put("message", "没有可更新的数据！");
			}else {
				map.put("message", (count1==0?"":(count1+"条成绩关联了专业课程！"))+(count2==0?"":(count2+"条成绩重置了学时！"))
						+(count3==0?"":(count3+"条成绩关联了学习计划！"))+(count4==0?"":(count4+"条成绩修改了考试批次！")));
			}
			UserOperationLogs userOperationLog = new UserOperationLogs();
			userOperationLog.setUserName(curUser.getCnName());
			userOperationLog.setUserId(curUser.getResourceid());
			userOperationLog.setOperationContent("更新成绩显示："+map.get("message")+"。参数："+Condition2SQLHelper.getConditionFromResquestByIterator(request));
			userOperationLog.setOperationType(UserOperationLogs.UPDATE);
			userOperationLog.setRecordTime(new Date());
			userOperationLog.setIpaddress(request.getLocalAddr());
			userOperationLog.setModules("5");
			userOperationLogsService.persist(userOperationLog);
			logger.info("更新成绩显示：操作人："+curUser.getCnName()+" 操作人ID："+curUser.getResourceid()+" 操作时间："+date);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			map.put("statusCode", 300);
			map.put("message", "操作失败！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 重新加密成绩
	 * @param response
	 */
	@RequestMapping("/edu3/system/standby/encryptScore.html")
	public void encryptScore(HttpServletRequest request, HttpServletResponse response){
		Map<String,Object> map = new HashMap<String, Object>();
		User curUser = SpringSecurityHelper.getCurrentUser();
		String date = ExDateUtils.getCurrentDate();
		StringBuffer sql = new StringBuffer();
		sql.append("select er.resourceid,er.studentid,er.usuallyscore tempusuallyScore,nvl2(er.usuallyscore,f_decrypt_score(er.usuallyscore,er.studentid),'')tempusuallyScore_d,");
		sql.append(" er.writtenscore tempwrittenScore,nvl2(er.writtenscore,f_decrypt_score(er.writtenscore,er.studentid),'')tempwrittenScore_d,");
		sql.append(" er.integratedscore tempintegratedScore,nvl2(er.integratedscore,f_decrypt_score(er.integratedscore,er.studentid),'')tempintegratedScore_d");
		sql.append(" from edu_teach_examresults er where er.isdeleted=0 and er.studentid is not null and (er.coursescoretype!='11' or length(er.studentid)<20)");
		sql.append(" and (er.integratedscore is not null or er.writtenscore is not null or er.usuallyscore is not null)");
		try {
			//Map<String, Object> condition = new HashMap<String, Object>();
			List<ExamResults> examresultList = new ArrayList<ExamResults>();
			//List<ExamResults> examResults = jdbcDao.getBaseJdbcTemplate().findList(sql.toString(),null,ExamResults.class);
			List<Map<String, Object>> examResults = jdbcDao.getBaseJdbcTemplate().findForList(sql.toString(), null);
			if(examResults!=null){
				for (Map<String, Object> m : examResults) {
					boolean isUpdate = false;
					ExamResults er = examResultsService.get(m.get("resourceid").toString());
					if(!er.getWrittenScore().equals(m.get("tempwrittenScore_d")==null?"":m.get("tempwrittenScore_d").toString())){
						er.setWrittenScore(m.get("tempwrittenScore_d").toString());
						isUpdate = true;
					}
					if(!er.getUsuallyScore().equals(m.get("tempusuallyScore_d")==null?"":m.get("tempusuallyScore_d").toString())){
						er.setUsuallyScore(m.get("tempusuallyScore_d").toString());
						isUpdate = true;
					}
					if(!er.getIntegratedScore().equals(m.get("tempintegratedScore_d")==null?"":m.get("tempintegratedScore_d").toString())){
						er.setIntegratedScore(m.get("tempintegratedScore_d").toString());
						isUpdate = true;
					}
					if(isUpdate){
						examresultList.add(er);
					}
				}
			}
			
			if(examresultList.size()>0){
				examResultsService.batchSaveOrUpdate(examresultList);
				map.put("message", "一共重新加密"+examresultList.size()+" 条成绩！");
				UserOperationLogs userOperationLog = new UserOperationLogs();
				userOperationLog.setUserName(curUser.getCnName());
				userOperationLog.setUserId(curUser.getResourceid());
				userOperationLog.setOperationContent("重新加密成绩："+map.get("message"));
				userOperationLog.setOperationType(UserOperationLogs.UPDATE);
				userOperationLog.setRecordTime(new Date());
				userOperationLog.setIpaddress(request.getLocalAddr());
				userOperationLog.setModules("5");
				userOperationLogsService.persist(userOperationLog);
			}else {
				map.put("message", "无需重新加密的成绩！");
			}
			map.put("statusCode", 200);
			logger.info("重新加密成绩：操作人："+curUser.getCnName()+" 操作人ID："+curUser.getResourceid()+" 操作时间："+date);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			map.put("statusCode", 300);
			map.put("message", "操作失败！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 拷贝课程资源
	 * @param response
	 */
	@RequestMapping("/edu3/system/standby/copyOnlineCourse.html")
	public void copyOnlineCourse(String networkCourseid, String faceCourseid, HttpServletRequest request, HttpServletResponse response){
		Map<String,Object> map = new HashMap<String, Object>();
		User curUser = SpringSecurityHelper.getCurrentUser();
		String date = ExDateUtils.getCurrentDate();
		StringBuffer buffer = new StringBuffer();
		Connection conn = null;
		PreparedStatement st = null;
		try {
			String sql = "select * from edu_teach_syllabustree st where st.courseid=?";
			List<Map<String, Object>> networkCourseList = jdbcDao.getBaseJdbcTemplate().findForList(sql, new Object[]{networkCourseid});
			List<Map<String, Object>> faceCourseList = jdbcDao.getBaseJdbcTemplate().findForList(sql, new Object[]{faceCourseid});
			if(!(networkCourseList!=null && networkCourseList.size()>0)){
				buffer.append("该网络课程无可用资源！<br>");
			}
			if(faceCourseList!=null && faceCourseList.size()>0){
				buffer.append("该面授课程已有资源！<br>");
			}else if(buffer.length()==0){
				sql = "select * from edu_base_course c where c.resourceid in('"+networkCourseid+"','"+faceCourseid+"')";
				List<Map<String, Object>> courseList = jdbcDao.getBaseJdbcTemplate().findForList(sql,null);
				if(courseList!=null && courseList.size()==2){
					conn = standbyMenuService.getConn();
	 				conn.setAutoCommit(false);//通知数据库开启事务(start transaction)
	 				List<String> sqlList = new ArrayList<String>();
	 				buffer.append("insert into edu_teach_syllabustree(resourceid,courseid,nodename,parentid,ischild,nodelevel,nodetype,version,isdeleted,showorder)");
	 				buffer.append(" select st.resourceid||'1','"+faceCourseid+"',st.nodename,st.parentid||'1',st.ischild,st.nodelevel,st.nodetype,st.version,st.isdeleted,st.showorder");
	 				buffer.append(" from edu_teach_syllabustree st where st.courseid='"+networkCourseid+"'");
					sqlList.add(buffer.toString());
					
					buffer.setLength(0);
					buffer.append("update edu_teach_syllabustree st set st.parentid='' where st.parentid='1'");
					sqlList.add(buffer.toString());
					
					buffer.setLength(0);
					buffer.append("insert into edu_lear_mate(resourceid,syllabustreeid,matename,matetype,version,isdeleted,mateurl,ispublished,showorder,channeltype,matecontent,totaltime)"); 
					buffer.append(" select m.resourceid||'1',m.syllabustreeid||'1',m.matename,m.matetype,m.version,m.isdeleted,m.mateurl,m.ispublished,");
					buffer.append(" m.showorder,m.channeltype,m.matecontent,m.totaltime");
					buffer.append(" from edu_lear_mate m where m.syllabustreeid in (select st.resourceid from edu_teach_syllabustree st where st.courseid='"+networkCourseid+"')");
					sqlList.add(buffer.toString());
					
					buffer.setLength(0);
					buffer.append("insert into edu_lear_courseguid ");
					buffer.append(" select cg.resourceid||'1',cg.syllabustreeid||'1',cg.type,cg.content,cg.fillinman,cg.fillinmanid,cg.fillindate,cg.version,cg.isdeleted");
					buffer.append(" from edu_lear_courseguid cg where cg.syllabustreeid in(select st.resourceid from edu_teach_syllabustree st where st.courseid='"+networkCourseid+"')");
					sqlList.add(buffer.toString());
					
					buffer.setLength(0);
					buffer.append("insert into edu_lear_overview ");
					buffer.append(" select ov.resourceid||'1','"+faceCourseid+"',ov.type,ov.content,ov.fillinman,ov.fillinmanid,ov.fillindate,ov.version,ov.isdeleted");
					buffer.append(" from edu_lear_overview ov where ov.courseid='"+networkCourseid+"'");
					sqlList.add(buffer.toString());
					
					buffer.setLength(0);
					buffer.append("insert into edu_lear_exams");
					buffer.append(" select e.resourceid||'1',e.examtype,e.difficult,e.requirement,'"+faceCourseid+"',e.keywords,e.question,e.answer,e.parser,e.fillindate,e.fillinman,e.fillinmanid,");
					buffer.append(" e.version,e.isdeleted,e.isenrolexam,e.coursename,e.showorder,e.parentid,e.examnodetype,e.examform,e.modifyman,e.modifydate,e.answeroptionnum,e.isonlineanswer");
					buffer.append(" from edu_lear_exams e where e.courseid='"+networkCourseid+"'");
					sqlList.add(buffer.toString());
					
					buffer.setLength(0);
					buffer.append("insert into edu_lear_courseexam");
					buffer.append(" select ce.resourceid||'1',ce.syllabustreeid||'1',ce.score,ce.version,ce.isdeleted,ce.showorder,ce.refersyllabustreeids,ce.refersyllabustreenames,");
					buffer.append(" ce.examid||'1',ce.ispublished,ce.auditman,ce.auditmanid,ce.auditdate,ce.modifyman,ce.modifydate");
					buffer.append(" from edu_lear_courseexam ce where ce.syllabustreeid in(select st.resourceid from edu_teach_syllabustree st where st.courseid='"+networkCourseid+"')");
					sqlList.add(buffer.toString());
					
					buffer.setLength(0);
					buffer.append("update edu_base_course c set c.hasresource='Y' where c.resourceid='"+faceCourseid+"'");
					sqlList.add(buffer.toString());
					for (String str : sqlList) {
						st = conn.prepareStatement(str);
						st.executeUpdate();
						st.close();
					}
					buffer.setLength(0);
					buffer.append("已成功拷贝课程资源！");
				}else {
					buffer.append("输入的课程信息有误！<br>");
				}
				conn.commit();
			}
			map.put("statusCode", 200);
			map.put("message", buffer.toString());
			UserOperationLogs userOperationLog = new UserOperationLogs();
			userOperationLog.setUserName(curUser.getCnName());
			userOperationLog.setUserId(curUser.getResourceid());
			userOperationLog.setOperationContent("拷贝课程资源：。参数："+Condition2SQLHelper.getConditionFromResquestByIterator(request));
			userOperationLog.setOperationType(UserOperationLogs.COPY);
			userOperationLog.setRecordTime(new Date());
			userOperationLog.setIpaddress(request.getLocalAddr());
			userOperationLog.setModules("9");
			userOperationLogsService.persist(userOperationLog);
			logger.info("拷贝课程资源：操作人："+curUser.getCnName()+" 操作人ID："+curUser.getResourceid()+" 操作时间："+date);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			map.put("statusCode", 300);
			map.put("message", e);
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 删除/恢复 成绩
	 * @param response
	 */
	@RequestMapping("/edu3/system/standby/deleteExamResults.html")
	public void deleteExamResults(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> condition = Condition2SQLHelper.getConditionFromResquestByIterator(request);
		Map<String,Object> map = new HashMap<String, Object>();
		User curUser = SpringSecurityHelper.getCurrentUser();
		String curDate = ExDateUtils.getCurrentDate();
		String opt = request.getParameter("opt");
		String optDate = request.getParameter("date");
		String optsString = "";
		StringBuilder builder = new StringBuilder("update edu_teach_examresults exam ");
		if("OOPS".equals(opt)){//撤销删除
			optsString = "恢复";
			builder.append("set exam.isdeleted=0,exam.memo=substr(exam.memo,0,instr(exam.memo,'#',-1,1)-1) where exam.resourceid in(");
		}else {
			optsString = "删除";
			builder.append("set exam.isdeleted=1,exam.memo=exam.memo||'#"+curDate+"修改' where exam.resourceid in(");
		}
		builder.append(" select er.resourceid");//--,si.studentname,c.coursename,es.batchname,er.checkstatus,ae.resourceid aeid,ne.resourceid neid,sp.resourceid";
		builder.append(" from edu_teach_examresults er join edu_roll_studentinfo si on si.resourceid=er.studentid and si.isdeleted=0");
		builder.append(" join edu_base_course c on c.resourceid=er.courseid and c.isdeleted=0");
		builder.append(" left join edu_teach_plancourse pc on pc.planid=si.teachplanid and pc.courseid=er.courseid and pc.isdeleted=0");
		builder.append(" left join edu_teach_abnormalexam ae on ae.plancourseid=pc.resourceid and ae.studentid=si.resourceid and ae.isdeleted=0");
		builder.append(" left join edu_teach_noexam ne on ne.studentid=si.resourceid and ne.courseid=er.courseid and ne.isdeleted=0");
		builder.append(" left join edu_learn_stuplan sp on sp.studentid=si.resourceid and sp.plansourceid=pc.resourceid and");
		builder.append(" sp.examresultsid=er.resourceid and sp.isdeleted=0 where er.isdeleted=:isdeleted");
		if("OOPS".equals(opt)){
			condition.put("isdeleted", 1);
		}else {
			condition.put("isdeleted", 0);
		}
		if(condition.containsKey("branchSchool")){
			builder.append(" and si.branchschoolid=:branchSchool");
		}
		if(condition.containsKey("grade")){
			builder.append(" and si.gradeid=:grade");
		}
		if(condition.containsKey("classic")){
			builder.append(" and si.classicid=:classic");
		}
		if(condition.containsKey("major")){
			builder.append(" and si.majorid=:major");
		}
		if(condition.containsKey("classes")){
			builder.append(" and si.classesid=:classes");
		}
		if(condition.containsKey("studyNo")){
			builder.append(" and si.studyno=:studyNo");
		}
		if(condition.containsKey("courseid")){
			builder.append(" and er.courseid=:courseid");
		}
		if(condition.containsKey("examSub")){
			builder.append(" and er.examsubid=:examSub");
		}
		builder.append(" )");
		try {
			int count1 = jdbcDao.getBaseJdbcTemplate().executeForMap(builder.toString(), condition);
			//删除补考名单
			String sql = "update edu_teach_makeuplist ml";
			if("OOPS".equals(opt)){//撤销删除
				sql += " set ml.memo=substr(ml.memo,0,instr(ml.memo,'#',-1,1)-1),ml.isdeleted=0 where ml.isdeleted=1";
			}else {
				sql += " set ml.memo=ml.memo||'#"+curDate+"修改',ml.isdeleted=1 where ml.isdeleted=0";
			}
			sql += " and ml.resultsid in("
					+" select er.resourceid from edu_teach_examresults er where er.isdeleted="+("OOPS".equals(opt)?0:1)+")";
			int count2 = jdbcDao.getBaseJdbcTemplate().executeForMap(sql, new HashMap<String, Object>());
			map.put("statusCode", 200);
			map.put("message", "成功"+optsString+count1+" 条成绩！");
			UserOperationLogs userOperationLog = new UserOperationLogs();
			userOperationLog.setUserName(curUser.getCnName());
			userOperationLog.setUserId(curUser.getResourceid());
			userOperationLog.setOperationContent(optsString+"成绩（"+count1+"）。参数："+Condition2SQLHelper.getConditionFromResquestByIterator(request));
			userOperationLog.setOperationType("OOPS".equals(opt)? UserOperationLogs.REPEAL : UserOperationLogs.DELETE);
			userOperationLog.setRecordTime(new Date());
			userOperationLog.setIpaddress(request.getLocalAddr());
			userOperationLog.setModules("5");
			userOperationLogsService.persist(userOperationLog);
			logger.info(optsString+"成绩：操作人："+curUser.getCnName()+" 操作人ID："+curUser.getResourceid()+" 操作时间："+curDate);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			map.put("statusCode", 300);
			map.put("message", "操作失败！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 删除重复成绩
	 * @param response
	 */
	@RequestMapping("/edu3/system/standby/deleteRepeatExam.html")
	public void deleteRepeatExam(HttpServletRequest request, HttpServletResponse response){
		Map<String,Object> map = new HashMap<String, Object>();
		User curUser = SpringSecurityHelper.getCurrentUser();
		String date = ExDateUtils.getCurrentDate();
		StringBuilder builder = new StringBuilder();
		builder.append("update edu_teach_examresults er set er.memo=er.memo||'#成绩去重',er.isdeleted=1 where er.resourceid in(");
		//--查找需要删除的重复成绩，即没有关联学习计划的成绩
		builder.append(" select resourceid from(select er2.resourceid,sp.examresultsid ,row_number() over");
		builder.append("  (partition by er2.studentid,er2.courseid,er2.examsubid order by sp.examresultsid desc) rn from(");
		// --分组条件
		builder.append("   select er1.studentid,er1.courseid,er1.examsubid from edu_teach_examresults er1 where er1.isdeleted=0");
		builder.append("   group by er1.studentid,er1.courseid,er1.examsubid,er1.integratedscore having count(*)>1)exam");
		builder.append(" left join edu_teach_examresults er2 on er2.studentid=exam.studentid and er2.courseid=exam.courseid and er2.examsubid=exam.examsubid");
		builder.append(" left join edu_learn_stuplan sp on sp.studentid=er2.studentid and sp.examresultsid=er2.resourceid and sp.isdeleted=0");
		builder.append(" where er2.isdeleted=0)A where A.rn = 1)");
		try {
			int count = jdbcDao.getBaseJdbcTemplate().executeForMap(builder.toString(), new HashMap<String, Object>());
			map.put("statusCode", 200);
			map.put("message", "成功删除 "+count+" 条重复成绩！");
			UserOperationLogs userOperationLog = new UserOperationLogs();
			userOperationLog.setUserName(curUser.getCnName());
			userOperationLog.setUserId(curUser.getResourceid());
			userOperationLog.setOperationContent("删除重复成绩（"+count+"）。参数："+Condition2SQLHelper.getConditionFromResquestByIterator(request));
			userOperationLog.setOperationType(UserOperationLogs.DELETE);
			userOperationLog.setRecordTime(new Date());
			userOperationLog.setIpaddress(request.getLocalAddr());
			userOperationLog.setModules("5");
			userOperationLogsService.persist(userOperationLog);
			logger.info("删除重复成绩：操作人："+curUser.getCnName()+" 操作人ID："+curUser.getResourceid()+" 操作时间："+date);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			map.put("statusCode", 300);
			map.put("message", "操作失败！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 分配专业列表
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/system/standby/RTCW-list.html")
	public String RTCW(HttpServletRequest request, HttpServletResponse response, ModelMap model, Page objPage){
		Map<String, Object> condition = Condition2SQLHelper.getConditionFromResquestByIterator(request);
		Page studentInfos = studentInfoService.findPageByCondition(condition, objPage);
		if(studentInfos.getTotalCount()==1){
			StudentInfo studentInfo = (StudentInfo) studentInfos.getResult().get(0);
			condition.put("gradeId", studentInfo.getGrade().getResourceid());
			condition.put("classicId", studentInfo.getClassic().getResourceid());
			condition.put("teachingType", studentInfo.getTeachingType());
		}
		String teachPlan = ExStringUtils.trim(request.getParameter("teachPlan"));
		List<TeachingPlan> teachingPlanList =  teachingPlanService.findTeachingPlanByCondition(condition);
		String teachPlanInfo = teachingPlanService.constructOptions(teachingPlanList, teachPlan);
		model.addAttribute("teachPlanInfo", teachPlanInfo);
		model.addAttribute("condition", condition);
		model.addAttribute("studentInfoList", studentInfos);
		return "/system/standbyMenu/RTCW-list";
	}
	
	/**
	 * 获取可选教学计划列表
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/system/standby/getTeachPlan.html")
	public void getPlan(HttpServletRequest request, HttpServletResponse response){
		Map<String,Object> map = new HashMap<String, Object>();
		int statusCode = 200;
		String message = "操作成功！";
		Map<String, Object> condition = Condition2SQLHelper.getConditionFromResquestByIterator(request);
		String teachPlan = request.getParameter("teachPlan");
		try {
			List<TeachingPlan> teachingPlanList =  teachingPlanService.findTeachingPlanByCondition(condition);
			String teachPlanInfo = teachingPlanService.constructOptions(teachingPlanList, teachPlan);
			map.put("teachPlanInfo", teachPlanInfo);
		} catch (Exception e) {
			// TODO: handle exception
			statusCode = 300;
			message = "操作成功！";
		}
		
		map.put("statusCode", statusCode);
		map.put("message", "");
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 分配专业-提交
	 * @param request
	 */
	@RequestMapping("/edu3/system/standby/saveRTCW.html")
	public void saveRTCW(HttpServletRequest request, HttpServletResponse response){
		String gradeid =  request.getParameter("gradeid");
		String classicId = ExStringUtils.trim(request.getParameter("classicId"));
		String teachingType = ExStringUtils.trim(request.getParameter("teachingType"));
		String majorid =  ExStringUtils.trim(request.getParameter("majorid"));
		String teachPlan = ExStringUtils.trim(request.getParameter("teachPlan"));
		String isUpdateMajor = request.getParameter("isUpdateMajor");
		//String isUpdateClass = request.getParameter("isUpdateClass");
		String classesid = request.getParameter("classesid");
		String studentIds = ExStringUtils.trimToEmpty(request.getParameter("studentIds"));//学籍资源ID
		
		Map<String , Object> condition = new HashMap<String, Object>();
		if(!studentIds.contains(",")){
			StudentInfo studentInfo = studentInfoService.get(studentIds);
			if(studentInfo!=null){
				if (ExStringUtils.isEmpty(gradeid)) {
					gradeid = studentInfo.getGrade().getResourceid();
				}
				if (ExStringUtils.isEmpty(classicId)) {
					classicId = studentInfo.getClassic().getResourceid();
				}
				if (ExStringUtils.isEmpty(teachingType)) {
					teachingType = studentInfo.getTeachingType();
				}
			}
		}
		int statusCode = 200;
		String message = "分配成功！";
		Map<String,Object> map = new HashMap<String, Object>();
		User curUser = SpringSecurityHelper.getCurrentUser();
		do {
			try {
				//修改专业
				if(ExStringUtils.isNotBlank(majorid)){
					condition.put("majorid", majorid);
				}else {
					message = "请选择分配专业！";
					new Throwable();
				}
				if(ExStringUtils.isNotBlank(gradeid)){
					condition.put("gradeid", gradeid);
				}
				if(ExStringUtils.isNotBlank(classicId)){
					condition.put("classicId", classicId);
				}
				if(ExStringUtils.isNotBlank(teachingType)){
					condition.put("teachingType", teachingType);
				}
				StringBuilder builder = new StringBuilder();
				//年级教学计划
				if(ExStringUtils.isNotBlank4All(teachPlan,gradeid,teachPlan)){
					builder.append(" from "+TeachingGuidePlan.class.getSimpleName()+" where isDeleted=0 and grade.resourceid=? and teachingPlan.resourceid=?");
					List<TeachingGuidePlan> teachingGuidePlans = teachingGuidePlanService.findByHql(builder.toString(), new Object[]{gradeid,teachPlan});
					if(!(teachingGuidePlans!=null && teachingGuidePlans.size()>0)){
						TeachingGuidePlan teachingGuidePlan = new TeachingGuidePlan();
						teachingGuidePlan.setGrade(gradeService.get(gradeid));
						teachingGuidePlan.setTeachingPlan(teachingPlanService.get(teachPlan));
						teachingGuidePlan.setIspublished("Y");
						teachingGuidePlan.setGenerationTask("N");
						teachingGuidePlan.setIsStatcourse("N");
						teachingGuidePlan.setIsDeleted(0);
						teachingGuidePlanService.saveOrUpdate(teachingGuidePlan);
					}
				}
				builder.setLength(0);
				builder.append("select count(*) from edu_teach_examresults er join edu_roll_studentinfo si on si.resourceid=er.studentid");
				builder.append(" where si.resourceid in('"+studentIds.replaceAll(",", "','")+"')");
				long count2 = jdbcDao.getBaseJdbcTemplate().findForLong(builder.toString(), new HashMap<String, Object>());
				if(count2>0){
					statusCode = 300;
					message = "所选学生已经录入成绩，不允许分配专业！";
				/*	如果已经录入成绩则需要修改以下数据，此处暂时不允许修改已经录入成绩的学生
 					1、开课表（不建议后台处理）
					2、更改成绩表：如果存在课程则修改majorcourseid字段，不存在则修改isoutplancourse字段
					*/
					continue;
				}
				
				Major major = majorService.get(majorid);
				builder.setLength(0);
				builder.append("update edu_roll_studentinfo si set si.majorid=:majorid ");
				//更新教学计划
				if(ExStringUtils.isNotBlank(teachPlan)){
					builder.append(",si.teachplanid='"+teachPlan+"'");
				}
				if(ExStringUtils.isNotBlank(classicId)){
					builder.append(",si.classicid='"+classicId+"'");
				}
				if(ExStringUtils.isNotBlank(teachingType)){
					builder.append(",si.teachingType='"+teachingType+"'");
				}
				if(ExStringUtils.isNotBlank(classesid)){
					builder.append(",si.classesid='"+classesid+"'");
				}
				/*if("Y".equals(isUpdateClass)){
					builder.append(",si.classesid=(select cl.resourceid from edu_roll_classes cl where cl.isdeleted=0 and rownum=1");
					builder.append(" and cl.gradeid=:gradeid and cl.classicid=:classicId and cl.teachingType=:teachingType and cl.majorid=:majorid)");
				}*/
				builder.append(" where si.resourceid in('"+studentIds.replaceAll(",", "','")+"')");
				long count = jdbcDao.getBaseJdbcTemplate().executeForMap(builder.toString(), condition);
				try {
					//招生专业
					if("Y".equals(isUpdateMajor)){
						builder.setLength(0);
						builder.append("update edu_recruit_examinee ei set ei.lqzymc='"+major.getMajorName()+"' where ei.isdeleted=0 and (ei.ksh,ei.zkzh)");
						builder.append(" in(select si.enrolleecode,si.examcertificateno from edu_roll_studentinfo si where si.resourceid in('"+studentIds.replaceAll(",", "','")+"'))");
						jdbcDao.getBaseJdbcTemplate().executeForObject(builder.toString(), new Object[]{});
						List<RecruitMajor> recruitMajors = recruitMajorService.findMajorByCondition(condition);
						if(recruitMajors!=null && recruitMajors.size()>0){
							RecruitMajor recruitMajor = recruitMajors.get(0);
							builder.setLength(0);
							builder.append("update edu_recruit_enrolleeinfo eei set eei.recruitmajorid=? where eei.isdeleted=0 and (eei.enrolleeCode,eei.examCertificateNo)");
							builder.append(" in(select si.enrolleecode,si.examcertificateno from edu_roll_studentinfo si where si.resourceid in('"+studentIds.replaceAll(",", "','")+"'))");
							jdbcDao.getBaseJdbcTemplate().executeForObject(builder.toString(), new Object[]{recruitMajor.getResourceid()});
						}
						//edu_recruit_examinee：lqzymc
						
						//edu_recruit_enrolleeinfo：recruitmajorid
					}
				} catch (Exception e) {
				}
				
				//更改学习计划表：课程相同则修改plansourceid字段；
			    //如果课程不相同并且有成绩则修改planoutcourseid字段（plansourceid置空）,否则删除学习计划
				builder.setLength(0);
				builder.append("select sp.resourceid,pcc.resourceid plansourceid,nvl2(pcc.resourceid,null,pc.resourceid) planoutcourseid");
				builder.append(" ,nvl2(er.resourceid,0,1) isdeleted from edu_roll_studentinfo si");
				builder.append(" join edu_learn_stuplan sp on sp.studentid=si.resourceid and sp.isdeleted=0");
				builder.append(" join edu_teach_plancourse pc on pc.resourceid=sp.plansourceid and pc.isdeleted=0");
				builder.append(" left join edu_teach_plancourse pcc on pcc.planid=si.teachplanid and pcc.courseid=pc.courseid and pcc.isdeleted=0");
				builder.append(" left join edu_teach_examresults er on er.resourceid=sp.examresultsid and er.isdeleted=0");
				builder.append(" where si.teachplanid!=pc.planid and si.resourceid in()");
				List<Map<String, Object>> resultList = jdbcDao.getBaseJdbcTemplate().findForList(builder.toString(), new Object[]{});
				if(resultList!=null && resultList.size()>0){
					Set<Map<String, Object>> resultSet = new HashSet<Map<String,Object>>(resultList);
					builder.setLength(0);
					builder.append("update edu_learn_stuplan sp set sp.plansourceid=:plansourceid,sp.planoutcourseid=:planoutcourseid,sp.isdeleted=:isdeleted where sp.resourceid=:resourceid");
					jdbcDao.getBaseJdbcTemplate().getJdbcTemplate().batchUpdate(builder.toString(), resultSet.toArray(new Map[resultSet.size()]));
				}
				//更新缴费表
				builder.setLength(0);
				builder.append(" update EDU_FEE_STUFEE fee set fee.majorid='"+majorid+"'");
				if(ExStringUtils.isNotBlank(classicId)){
					builder.append(",fee.classicid='"+classicId+"'");
				}
				if(ExStringUtils.isNotBlank(teachingType)){
					builder.append(",fee.teachingType='"+teachingType+"'");
				}
				builder.append(" where fee.studentid in('"+studentIds.replaceAll(",", "','")+"')");
				jdbcDao.getBaseJdbcTemplate().executeForMap(builder.toString(), condition);
				
				UserOperationLogs userOperationLog = new UserOperationLogs();
				userOperationLog.setUserName(curUser.getCnName());
				userOperationLog.setUserId(curUser.getResourceid());
				userOperationLog.setOperationContent(count+"个学生分配到【"+major.getMajorName()+"】专业{"+condition.toString()+"}");
				userOperationLog.setOperationType(UserOperationLogs.UPDATE);
				userOperationLog.setRecordTime(new Date());
				userOperationLog.setIpaddress(request.getLocalAddr());
				userOperationLog.setModules("3");
				userOperationLogsService.persist(userOperationLog);
				logger.info("分配专业：操作人："+curUser.getCnName()+" 操作人ID："+curUser.getResourceid()+" 操作时间："+ExDateUtils.getCurrentDate());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				statusCode = 300;
				message = "分配失败！"+message;
			}
		} while (false);
		
		map.put("statusCode", statusCode);
		map.put("message", message);
		renderJson(response, JsonUtils.mapToJson(map));
		
	}
	
	/**
	 * 执行SQL查询语句
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/system/standby/executeSQL.html")
	public String executeSQL(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String sql = ExStringUtils.trim(request.getParameter("sql"));
		String flag = ExStringUtils.trim(request.getParameter("flag"));
		
		List<Map<String, Object>> mapList = new ArrayList<Map<String,Object>>();
		try {
			String relativePath = File.separator+"querySQL.txt";
			String objUrl = CacheAppManager.getSysConfigurationByCode("web.uploadfile.rootpath").getParamValue()+relativePath;
			if("export".equals(flag)){
				//读取查询语句
				@Cleanup FileInputStream is = new FileInputStream(objUrl);
				@Cleanup InputStreamReader reader = new InputStreamReader(is, "UTF-8");
			    StringBuilder sb = new StringBuilder(1000);
			    while (reader.ready()) {
			      sb.append((char) reader.read());
			    }
			    sql = sb.toString();
				
			}else if(ExStringUtils.isNotBlank(sql)){
				if (sql.endsWith(";")) {
					sql = sql.substring(0, sql.length() - 1);
				}
				if(!(sql.startsWith("select") || sql.startsWith("SELECT"))){
					new Throwable("只允许执行查询语句！");
				}
				//保存查询语句
				@Cleanup OutputStream os= new FileOutputStream(objUrl);
				@Cleanup OutputStreamWriter writer = new OutputStreamWriter(os, "UTF-8");
				writer.append(sql);
			}
			if(ExStringUtils.isNotBlank(sql)){
				mapList = jdbcDao.getBaseJdbcTemplate().findForListMap(sql,null);
				if(mapList!=null && mapList.size()>0){
					model.addAttribute("map", mapList.get(0));
				}
			}
			model.addAttribute("sql", sql);
			model.addAttribute("flag", flag);
			model.addAttribute("mapList", mapList);
			
			if("export".equals(flag)){
				String fileName = "查询结果";
				try {
					fileName = new String(fileName.getBytes("GBK"), "iso8859-1");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				response.setCharacterEncoding("GB2312");
				response.setContentType("application/vnd.ms-excel");
			  	response.setHeader("Content-Disposition", "attachment; filename="+fileName+".xls"); 
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/system/standbyMenu/executeSQL";
	}
	
	/**
	 * 重新计算综合成绩
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/system/standby/calculateScore.html")
	private void calculateScore(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> condition = Condition2SQLHelper.getConditionFromResquestByIterator(request);
		StringBuilder builder = new StringBuilder();
		Map<String, Object> map = new HashMap<String, Object>();
		int statusCode = 200;
		String message = "";
		String sql = " where er.isdeleted=0 and ei.isdeleted=0 and (er.writtenscore is not null or er.usuallyscore is not null) and er.integratedscore is null"
				+ " and er.examabnormity='0' and er.checkstatus!='0' ";
		builder.append("update edu_teach_examresults exam set exam.memo=(");
		builder.append("select f_encrypt_score(round((nvl(wrscore,0)*wsp+nvl(usscore,0)*usp)/100),examscore.siid) ec_score from(");
		builder.append("select es.batchname,si.studyno,si.studentname,c.coursecode,c.coursename,");
		//卷面成绩比 优先级：考试信息>考试批次
		builder.append("nvl(nvl2(ei1.resourceid,decode(cs.teachtype,'networkTeach',ei1.studyScorePer,ei1.facestudyScorePer),");
		builder.append("decode(cs.teachtype,'networkTeach',ei.studyScorePer,ei.facestudyScorePer)),");
		builder.append("decode(cs.teachtype,'networkTeach',es.writtenScorePer,es.facestudyscoreper)) wsp,");
		//平时成绩比 优先级：考试信息>考试批次
		builder.append("nvl(nvl2(ei1.resourceid,decode(cs.teachtype,'networkTeach',ei1.netsidestudyScorePer,ei1.facestudyScorePer2),");
		builder.append("decode(cs.teachtype,'networkTeach',ei.netsidestudyScorePer,ei.facestudyScorePer2)),");
		builder.append("decode(cs.teachtype,'networkTeach',es.netsidestudyScorePer,es.facestudyScorePer2)) usp,");
		//卷面/平时/综合 成绩、课程类型（开课）
		builder.append("f_decrypt_score(er.writtenscore,er.studentid) wrscore,f_decrypt_score(er.usuallyscore,er.studentid) usscore,");
		builder.append("f_decrypt_score(er.integratedscore,er.studentid) org_intscore,cs.teachtype,");
		// 课程类型（考试信息）
		builder.append("decode(ei.examcoursetype,0,'networkTeach',1,'faceTeach',2,'faceTeach+networkTeach',ei.examcoursetype) examcoursetype,");
		builder.append("er.resourceid erid,si.resourceid siid,nvl(ei1.resourceid,ei.resourceid) eiid,nvl2(ei1.resourceid,'Y','') flag from edu_teach_examresults er");
		builder.append(" join edu_teach_examsub es on es.resourceid=er.examsubid");
		builder.append(" join edu_teach_examinfo ei on ei.resourceid=er.examinfoid and ei.courseid=er.courseid");
		builder.append(" join edu_base_course c on c.resourceid=er.courseid and c.isdeleted=0");
		//教学计划
		builder.append(" join edu_roll_studentinfo si on si.resourceid=er.studentid");
		builder.append(" join edu_teach_plancourse pc on pc.planid=si.teachplanid and pc.courseid=er.courseid");
		builder.append(" join edu_teach_guiplan gp on gp.gradeid=si.gradeid and gp.planid=si.teachplanid");
		builder.append(" join edu_teach_coursestatus cs on cs.guiplanid=gp.resourceid and cs.plancourseid=pc.resourceid and cs.schoolids=si.branchschoolid");
		//验证考试信息的课程类型是否正确，否则需要重新关联
		builder.append(" left join edu_teach_examinfo ei1 on ei1.courseid=er.courseid and ei1.examsubid=er.examsubid and ei1.isdeleted=0");
		builder.append(" and ei1.examcoursetype=decode(cs.teachtype,'networkTeach',0,'faceTeach',1,'') and ei1.examcoursetype!=ei.examcoursetype");
		builder.append(sql);//.append("order by teachtype,examcoursetype,si.studyno");
		builder.append(")examscore  where examscore.erid=exam.resourceid)");
		sql.replaceAll("er.", "exam.");
		builder.append(sql);
		builder.append(" and exam.studentid in (select si.resourceid from edu_roll_studentinfo si where si.isdeleted=0");
		if(condition.containsKey("branchSchool")){
			builder.append(" and si.branchschoolid=:branchSchool");
		}
		if(condition.containsKey("grade")){
			builder.append(" and si.gradeid=:grade");
		}
		if(condition.containsKey("classic")){
			builder.append(" and si.classicid=:classic");
		}
		if(condition.containsKey("major")){
			builder.append(" and si.majorid=:major");
		}
		if(condition.containsKey("classes")){
			builder.append(" and si.classesid=:classes");
		}
		if(condition.containsKey("studyNo")){
			builder.append(" and si.studyno=:studyNo");
		}
		builder.append(")");
		if(condition.containsKey("courseid")){
			builder.append(" and exam.courseid=:courseid");
		}
		if(condition.containsKey("examSub")){
			builder.append(" and exam.examsubid=:examSub");
		}
		try {
			User curUser = SpringSecurityHelper.getCurrentUser();
			int count = jdbcDao.getBaseJdbcTemplate().executeForMap(sql, condition);
			UserOperationLogs userOperationLog = new UserOperationLogs();
			userOperationLog.setUserName(curUser.getCnName());
			userOperationLog.setUserId(curUser.getResourceid());
			userOperationLog.setOperationContent(count+"个学生重新计算综合成绩。参数："+Condition2SQLHelper.getConditionFromResquestByIterator(request));
			userOperationLog.setOperationType(UserOperationLogs.UPDATE);
			userOperationLog.setRecordTime(new Date());
			userOperationLog.setIpaddress(request.getLocalAddr());
			userOperationLog.setModules("5");
			userOperationLogsService.persist(userOperationLog);
			message = count+"个学生综合成绩修复成功！";
			logger.info("重新计算综合成绩：操作人："+curUser.getCnName()+" 操作人ID："+curUser.getResourceid()+" 操作时间："+ExDateUtils.getCurrentDate());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			statusCode = 300;
			message = "操作失败！";
		}
		map.put("statusCode", statusCode);
		map.put("message", message);
		renderJson(response, JsonUtils.mapToJson(map));
	}
}