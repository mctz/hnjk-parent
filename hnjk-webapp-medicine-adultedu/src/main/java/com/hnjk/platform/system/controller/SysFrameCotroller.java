package com.hnjk.platform.system.controller;

import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.context.SecurityContextImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.cache.MemcachedManager;
import com.hnjk.core.foundation.utils.BaseSecurityCodeUtils;
import com.hnjk.core.foundation.utils.ExCollectionUtils;
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.HttpHeaderUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.foundation.utils.Pinyin4jHelper;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.BaseSupportController;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.StudentBaseInfo;
import com.hnjk.edu.basedata.service.IStudentService;
import com.hnjk.edu.finance.model.StudentPayment;
import com.hnjk.edu.finance.service.IStudentPaymentService;
import com.hnjk.edu.learning.model.StudentLearnPlan;
import com.hnjk.edu.learning.service.IStudentLearnPlanService;
import com.hnjk.edu.portal.model.Message;
import com.hnjk.edu.portal.model.MessageReceiver;
import com.hnjk.edu.portal.model.MessageReceiverUser;
import com.hnjk.edu.portal.service.IMessageReceiverService;
import com.hnjk.edu.portal.service.IMessageReceiverUserService;
import com.hnjk.edu.portal.service.ISysMsgService;
import com.hnjk.edu.roll.model.StuChangeInfo;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.roll.service.IStuChangeInfoService;
import com.hnjk.edu.roll.service.IStudentInfoService;
import com.hnjk.edu.roll.service.impl.StuChangeInfoServiceImpl;
import com.hnjk.edu.teaching.model.ExamSub;
import com.hnjk.edu.teaching.model.TeachingPlan;
import com.hnjk.edu.teaching.service.IExamResultsService;
import com.hnjk.edu.teaching.service.IExamSubService;
import com.hnjk.edu.teaching.service.ITeachingJDBCService;
import com.hnjk.edu.teaching.service.ITeachingPlanService;
import com.hnjk.edu.teaching.vo.StudentExamResultsVo;
import com.hnjk.edu.util.Condition2SQLHelper;
import com.hnjk.extend.taglib.function.JstlCustFunction;
import com.hnjk.platform.system.UserOperationLogsHelper;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.UserOperationLogs;
import com.hnjk.platform.system.model.Version;
import com.hnjk.platform.system.service.IVersionService;
import com.hnjk.platform.system.service.WeatherReportService;
import com.hnjk.platform.taglib.JstlCustomFunction;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.Role;
import com.hnjk.security.model.User;
import com.hnjk.security.model.UserExtends;
import com.hnjk.security.service.IUserService;
import com.hnjk.security.verifyimage.CaptchaServiceSingleton;
import com.opensymphony.workflow.Workflow;
import com.opensymphony.workflow.spi.WfAgent;
import com.sun.org.apache.bcel.internal.generic.NEW;

/**
 * 系统页面框架<code>Controller</code>. <p>
 * 用于后台页面的展示.
 * @author： 广东学苑教育发展有限公司.
 * @since： 2009-3-16上午10:07:08
 * @see
 * @version 1.0
 */
@Controller
public class SysFrameCotroller extends BaseSupportController{
	
	private static final long serialVersionUID = -6461678257532531167L;
	
	protected Logger logger = LoggerFactory.getLogger(getClass());	
//	private Logger userLonginLog =LoggerFactory.getLogger("userLoginFile");
		
	@Autowired
	@Qualifier("messageReceiverService")
	private IMessageReceiverService messageReceiverService;
	
	@Autowired
	@Qualifier("studentinfoservice")
	private IStudentInfoService studentInfoService;//注入学籍服务
	
	@Autowired
	@Qualifier("studentLearnPlanService")
	private IStudentLearnPlanService studentLearnPlanService;
	
	@Autowired
	@Qualifier("teachingplanservice")
	private ITeachingPlanService teachingPlanService;//注入教学计划服务
	
	@Autowired
	@Qualifier("versionService")
	private IVersionService versionService;// app版本
	
	@Autowired
	@Qualifier("sysMsgService")
	private ISysMsgService sysMsgService;//系统消息
	
	@Autowired
	@Qualifier("memcacheManager")
	private MemcachedManager memcachedManager;
	
	@Autowired
	@Qualifier("studentService")
	private IStudentService studentService;
	
	@Autowired
	@Qualifier("userService")
	private IUserService userService;

	@Autowired
	@Qualifier("teachingJDBCService")
	private ITeachingJDBCService teachingJDBCService;

	@Autowired
	@Qualifier("messageReceiverUserService")
	private IMessageReceiverUserService messageReceiverUserService;
	
	@Autowired
	@Qualifier("examResultsService")
	private IExamResultsService examResultsService;
	
	
	@Autowired
	@Qualifier("examSubService")
	private IExamSubService examSubService;
	
	@Autowired
	@Qualifier("studentPaymentService")
	private IStudentPaymentService studentPaymentService;
	
	@Autowired
	@Qualifier("stuchangeinfoservice")
	private IStuChangeInfoService stuChangeInfoService;
	
	
	/**
	 * web页面验证
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping(value = "/j_hnjk_authorize_check.html")
	public void authorizeWebUser(HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String validateCode = ExStringUtils.defaultIfEmpty(request.getParameter("validateCode"), "");//获取验证码
		if(!isValidateCode(request.getSession(),validateCode)){
			resultMap.put("error", "错误的验证码!");
			resultMap.put("isSucess",false);			
		}else{
			//TODO 设置服务状态码，
			boolean serviceStatus = true;
			if(!serviceStatus){
				resultMap.put("isSucess", false);	
				resultMap.put("error", "网上服务已禁用，请联系管理员！");
			}else{				
				HttpSession session = request.getSession(true);//
				session.setAttribute("serviceFlag", true);
				resultMap.put("isSucess", true);	
			}				
		}	
		renderJson(response, JsonUtils.mapToJson(resultMap));
	}
	
	/**
	 * 获取天气预报(使用服务模式，JS通过jsonp来调用)<br/>
	 * 生成环境不允许服务器访问外网，这里做成一个proxy，客户端JS直接调用，so,必须放一份副本在184
	 * @param request
	 * @param response
	 * @param model
	 * @throws WebException
	 */
	@RequestMapping(value="/portal/service/weather/service.html",method = RequestMethod.GET)
	public void getWeatherReport(HttpServletRequest request,String city,HttpServletResponse response,ModelMap model) throws WebException{
				
		//String weatherStr[] = WeatherReportService.getTodayeWeather(cityStr);
		String jsoncallback = ExStringUtils.trimToEmpty(request.getParameter("jsoncallback"));
		String weaterhJsonStr = WeatherReportService.getCityWeather(Pinyin4jHelper.getPingYin(city));
		/*
		Map<String, Object> weatherMap = new HashMap<String, Object>();
		try {
			if(null!=list){
				weatherMap.put("display", "ok");
				weatherMap.put("city",city);
				weatherMap.put("w_1", list.get(0));
				weatherMap.put("w_2", list.get(1));			
			}else {
				weatherMap.put("display", "err");//无城市信息
			}
		} catch (Exception e) {
			weatherMap.put("display", "err");//错误
		}
		*/
		//renderJson(response, JsonUtils.mapToJson(weatherMap));
		renderText(response, jsoncallback+"("+weaterhJsonStr+")");
	
	}
	
	/**
	 * 系统登录页面
	 * @param request
	 * @param response
	 * @return
	 * @throws WebException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/edu3/login.html")
	public String loginPage(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{	
		ServletContext application = request.getSession().getServletContext();
		long startTime = System.currentTimeMillis();
		/*String loginType = request.getParameter("type");
		// 登出处理
		if(ExStringUtils.isNotEmpty(loginType) && "logout".equals(loginType)){
			String userName = SpringSecurityHelper.getCurrentUserName();
//			Map<String, String> userNameMap = (Map<String, String>)application.getAttribute("userNameMap");
			Map<String, HttpSession> userSessoinMap = (Map<String, HttpSession>)application.getAttribute("userSessoinMap");
			if(userName != null && userSessoinMap != null && userSessoinMap.containsKey(userName)){
				userSessoinMap.remove(userName);
				int totalLonginNum = application.getAttribute("totalLonginNum")==null?0:(Integer)application.getAttribute("totalLonginNum");
				totalLonginNum = (totalLonginNum-1)>0?(totalLonginNum-1):0;
				application.setAttribute("totalLonginNum", totalLonginNum);
				userLonginLog.info("login:"+ExDateUtils.getCurrentDateTimeStr()+" 目前在线总人数： "+ totalLonginNum);
			}
		}*/
		application.setAttribute("school", CacheAppManager.getSysConfigurationByCode("graduateData.schoolName").getParamValue());//学校名称
		application.setAttribute("schoolConnectName", null == CacheAppManager.getSysConfigurationByCode("graduateData.schoolConnectName") ? "" :CacheAppManager.getSysConfigurationByCode("graduateData.schoolConnectName").getParamValue());//后缀名
		application.setAttribute("hasAPP", null == CacheAppManager.getSysConfigurationByCode("hasAPP") ? "" :CacheAppManager.getSysConfigurationByCode("hasAPP").getParamValue());// 是否有APP端
		Version lastVersion = versionService.getLastVersion();
		if(lastVersion != null ){
			application.setAttribute("android_apk", lastVersion.getAppUrl());// app的apk路径
		}
		//登录系统次数		
		if(null !=  request.getSession(false) && null != request.getSession(false).getAttribute("loginNum")){
			model.addAttribute("loginNum", (Integer)request.getSession(false).getAttribute("loginNum"));
		}
		String url=CacheAppManager.getSysConfigurationByCode("portal.url").getParamValue();  //门户网站URL
		model.addAttribute("url",url);
		String QRCodeName =CacheAppManager.getSysConfigurationByCode("QRCodeName").getParamValue();  // 二维码图片名称
		model.addAttribute("QRCodeName",QRCodeName);
		model.addAttribute("schoolCode", CacheAppManager.getSysConfigurationByCode("graduateData.schoolCode").getParamValue());
		model.addAttribute("rootUrl", CacheAppManager.getSysConfigurationByCode("web.uploadfile.rooturl").getParamValue());
		model.addAttribute("usePayOnline", CacheAppManager.getSysConfigurationByCode("payment.usePayonline").getParamValue());
		model.addAttribute("payOnlineUrl", CacheAppManager.getSysConfigurationByCode("payment.payonline.url").getParamValue());
		logger.debug("------>>>系统登录页面:"+(System.currentTimeMillis()-startTime)+" ms...");
		return "/login";
	}
	
	
	/**
	 * 从新登录系统页面
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/relogin.html")
	public String relogin(HttpServletRequest request,ModelMap model) {
		String username = ExStringUtils.trimToEmpty(request.getParameter("user"));
		model.addAttribute("username",username);
		return "/relogin";
	}
	
	/**
	 * 系统主页面
	 * @return
	 * @throws ParseException 
	 * @throws Exception
	 */	
	@RequestMapping("/edu3/framework/index.html")
	public String loginForwardFramework(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException, ParseException{
		
		long startTime = System.currentTimeMillis();
	
		User user = SpringSecurityHelper.getCurrentUser();		
		//~~~~~~~~~~~~~~~~~~~~~常规信息(通用)~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		model.addAttribute("user", user);//当前用户
		model.addAttribute("nowHour", ExDateUtils.getCurrentDateTime().getHours());//当前小时
		model.addAttribute("nowTime",ExDateUtils.getCurrentDateTime());//当前时间
		model.addAttribute("weekStr", ExDateUtils.convertDay2Week(ExDateUtils.getCurrentDateTime()));//当前周		
				
		//获取用户自定义图像
		if(null != user.getUserExtends() && null != user.getPropertys(UserExtends.USER_EXTENDCODE_FACE)){
			model.addAttribute("rootUrl", request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()
					+ CacheAppManager.getSysConfigurationByCode("web.uploadfile.rooturl").getParamValue());//拿到附件映射的全局URL"
			model.addAttribute("userface",user.getPropertys(UserExtends.USER_EXTENDCODE_FACE).getExValue());
		}
		String url=CacheAppManager.getSysConfigurationByCode("portal.url").getParamValue();  //门户网站URL
		model.addAttribute("url",url);
		//获取用户自定义城市
		String cityStr = WeatherReportService.DEFAULT_CITY;
		if(null != user.getUserExtends() && null != user.getPropertys(UserExtends.USER_EXTENDCODE_CITY) && ExStringUtils.isNotBlank(user.getPropertys(UserExtends.USER_EXTENDCODE_CITY).getExValue())){		
//			String temp = user.getPropertys(UserExtends.USER_EXTENDCODE_CITY).getExValue().split(",")[1];
//			cityStr = temp.substring(0,temp.length()-1);
		}
		model.addAttribute("city", cityStr);
		//检查用户是否更改初始密码 
		try {
			if("student".equals(user.getUserType())){
				model.addAttribute("studentInfo", studentInfoService.getByUserId(user.getResourceid()));
			}
			// 是否提示修改初始化密码
			model.addAttribute("isTipUpdateInitPassword",CacheAppManager.getSysConfigurationByCode("isTipUpdateInitPassword").getParamValue());
			if(BaseSecurityCodeUtils.getMD5(CacheAppManager.getSysConfigurationByCode("sysuser.initpwd").getParamValue()).equals(user.getPassword())){
				model.addAttribute("isNeedModifyPwd",true);
			}else{
				model.addAttribute("isNeedModifyPwd",false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.debug("------>>>获取常规信息:"+(System.currentTimeMillis()-startTime)+" ms...");
		//~~~~~~~~~~~~~~~~~~~教学日历~~~~~~~~~~~~~~~~~~~~~~~~~~~
		GregorianCalendar currentDay = new GregorianCalendar();
		int month = currentDay.get(Calendar.MONTH);
		int year = currentDay.get(Calendar.YEAR);
		int today = currentDay.get(Calendar.DAY_OF_MONTH);
		model.addAttribute("today", today);
		model.addAttribute("current_date", year+"年"+(month+1)+"月");

		String[] days = new String[42];
		for (int i = 0; i < 42; i++) {
			days[i] = "";
		}
		logger.debug("------>>>获取教学日历:"+(System.currentTimeMillis()-startTime)+" ms...");
		//显示当前系统时间								
		Calendar thisMonth = Calendar.getInstance();
		thisMonth.set(Calendar.MONTH, month);
		thisMonth.set(Calendar.YEAR, year);
		thisMonth.setFirstDayOfWeek(Calendar.SUNDAY);
		thisMonth.set(Calendar.DAY_OF_MONTH, 1);
		int firstIndex = thisMonth.get(Calendar.DAY_OF_WEEK) - 1;
		int maxIndex = thisMonth.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		//List<TeachTaskDetails> alllist = new ArrayList<TeachTaskDetails>();
		if(!CacheAppManager.getSysConfigurationByCode("sysuser.usertype.student").getParamValue().equals(user.getUserType())){
			Map<String,Object> condition =  new HashMap<String, Object>();
			condition.put("userId", user.getResourceid());
			condition.put("orderBy", "startTime");	
			//alllist = teachTaskDetailsService.findTeachTaskDetailsByCondition(condition);
		}
		for (int i = 0; i < maxIndex; i++) {
			days[firstIndex + i] = String.valueOf(i + 1);	
			if(!CacheAppManager.getSysConfigurationByCode("sysuser.usertype.student").getParamValue().equals(user.getUserType())){//如果为老师				
				//List<TeachTaskDetails> list = getTeachTaskDetailsByTime(alllist,thisMonth.getTime());
				thisMonth.add(Calendar.DAY_OF_MONTH, 1);
				//if(null!=list && list.size()>0){
				//	String title = "";				
				//	int task = 1;
				//	for (TeachTaskDetails t : list) {
				//		title += (task++) +". "+t.getTaskContent()+"<br/>";
				//	}
					days[firstIndex + i] = String.valueOf(i + 1) ;
					//+"<a href=\"##\" title=\""+title+"\" class=\"_calendar_tips\">("+list.size()+")</a>";
				//}
			}	
			//String title = "测试测试<br/>测试测试";
			//days[firstIndex + i] = String.valueOf(i + 1) +"<a href=\"##\" title=\""+title+"\" class=\"_calendar_tips\">(2)</a>";
		}
		logger.debug("------>>>获取老师登录信息:"+(System.currentTimeMillis()-startTime)+" ms...");
		if(SpringSecurityHelper.isUserInRole("ROLE_STUDENT")){//如果为学生，查出学生的教学计划,是否需要首次确认登陆
			model.addAttribute("currentrole","student");
			model.addAttribute("firstLogin",CacheAppManager.getSysConfigurationByCode("student.firstLogin.comfirm").getParamValue());
			//是否需要填写教评问卷
			String isQuestionnaire = CacheAppManager.getSysConfigurationByCode("is_necessarily_submit_qustionnaire").getParamValue();
			model.addAttribute("isQuestionnaire",isQuestionnaire);
			getStudentTeachingPlanList(user,model);
			logger.debug("------>>>获取教学计划:"+(System.currentTimeMillis()-startTime)+" ms...");
		}else{
			/*
			try {
				for (TeachTaskDetails teachTaskDetails : alllist) {
					if("unfinished".equals(teachTaskDetails.getStatus()) && ExDateUtils.getTodayEnd().after(teachTaskDetails.getEndTime())){
						teachTaskDetails.setStatus("finished");
						teachTaskDetailsService.update(teachTaskDetails);
					}
				}
			} catch (Exception e) {
				logger.error("更新教学任务状态失败：{}",e.fillInStackTrace());
			}
			*/			
			//~~~~~~~~~~~~~~~~获取用户已办，待办~~~~~~~~~~~~~~~~~~~~~~~~~~
			try {
				Workflow wf = WfAgent.getWorkflow(request);
				
				Page objPage = new Page(1, 9, false, Page.DESC, "c.startDate");
				
				Map<String, Object> param = new HashMap<String, Object>();
				if(user.getOrgUnit().getUnitType().
						indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){
					param.put("currentUnitId", user.getOrgUnit().getResourceid());
				}
				Page currentWorks = wf.queryCurrentWorksByPage(objPage, user.getResourceid(), param);
				    	
				model.addAttribute("currentWorks", currentWorks);				
				model.addAttribute("dbNum",null !=currentWorks.getResult() ? currentWorks.getResult().size():0);
				
				objPage = new Page(1, 9, false, Page.DESC, "h.finishDate");
				
				Page historyWorks = wf.queryHistoryWorksByPage(objPage, user.getResourceid(), Collections.EMPTY_MAP);				
				model.addAttribute("historyWorks", historyWorks);
				
			} catch (Exception e) {
				logger.error("获取流程实例出错：{}",e.fillInStackTrace());
			}
			logger.debug("------>>>获取待办事项:"+(System.currentTimeMillis()-startTime)+" ms...");
		}
		//查找未录入成绩课程
		Page objPage = new Page(1, 100, false, Page.DESC, "");
		String userRole = "";
		
		Map<String, Object> condition = new HashMap<String, Object>();
		//获取用户角色
		List<Role> roleList = new ArrayList<Role>(user.getRoles());		
		for(Role role:roleList){
			if(role.getRoleCode().indexOf("ROLE_BRS_STUDENTSTATUS") >=0){
				userRole="jwy";
				break;
			}else if ("ROLE_LINE,ROLE_TEACHER_DUTY".contains(role.getRoleCode())) {
				userRole = "teacher";
			}
		}
		logger.debug("------>>>获取用户角色:"+(System.currentTimeMillis()-startTime)+" ms...");
		//广东医：是否提醒教务员提醒学生复学：整年，最多两年，休学时提醒复学时间，复学时间必须相隔一年（后改为整年，复学日期月份相同），提前一个月提示该教学点教务员这个角色
		String isNotice = CacheAppManager.getSysConfigurationByCode("is_notice_acdemicdean").getParamValue();
		if(Constants.BOOLEAN_YES.equalsIgnoreCase(isNotice)){
			if("jwy".equalsIgnoreCase(userRole)){//教务员角色
				//查找距离当前系统时间为11~13、>23个月的休学申请，以申请时间为准
				List<StuChangeInfo> sciList = stuChangeInfoService.getStuChangeInfoByMonth(user.getOrgUnit().getResourceid());
				if(sciList.size()>0 && sciList!=null){
					//由系统发送一条温馨提示给该账号，提醒该学生复学时间到了					
					for(StuChangeInfo sci:sciList){
						String msgTitle = "近期复学学生信息提醒";
						String msgType = "sysmsg";
						User admin = userService
								.getUserByLoginId("administrator");
						String content = "学生："
								+ sci.getStudentInfo().getStudyNo()
								+ "    "
								+ sci.getStudentInfo().getStudentName()
								+ "    联系电话："
								+ sci.getStudentInfo().getStudentBaseInfo()
										.getMobile()
								+ " ，在 <font color='red'>"
								+ ExDateUtils.formatDateStr(sci.getApplicationDate(),ExDateUtils.PATTREN_DATE_CN)
								+ "</font> 时申请了休学。日前已临近学生复学时间，请注意询问学生是否需要复学。（备注：学生休学时间应为整年，最多两年）";
						String hql4MsgUser = " from "
								+ MessageReceiverUser.class.getSimpleName()
								+ " m where m.messageReceiver.message.msgTitle=?  and  m.messageReceiver.message.msgType=?  and  m.messageReceiver.message.sender.resourceid=?  and add_months(m.messageReceiver.message.sendTime,12)>=current_date() and m.isDeleted=0 and m.receiver.resourceid=? and m.messageReceiver.receiveType=? and m.messageReceiver.message.content like '%"
								+ sci.getStudentInfo().getStudyNo() + "%'";
						List<MessageReceiverUser> MsgUserList = messageReceiverUserService
								.findByHql(
										hql4MsgUser,
										new Object[] { msgTitle, msgType,
												admin.getResourceid(),
												user.getResourceid(), "user" });
						//如果该消息已经存在，就不再发送，MsgUserList.size() ==0为不存在
						if (MsgUserList.size() == 0) {
							MessageReceiver receiver = new MessageReceiver();
							Message msg = new Message();
							MessageReceiverUser mru = new MessageReceiverUser();
							msg.setMsgTitle(msgTitle);
							msg.setContent(content);
							msg.setMsgType(msgType);
							msg.setSendTime(new Date());
							msg.setSender(admin);
							msg.setSenderName("系统提醒");
							receiver.setReceiveType("user");
							receiver.setMessage(msg);
							receiver.setUserNames(user.getUsername());
							mru.setMessageReceiver(receiver);
							mru.setReceiver(user);
							mru.setUserCNName(user.getCnName());
							mru.setUserName(user.getUsername());
							mru.setMsgStatus("sended");
							sysMsgService.saveOrUpdate(msg);
							messageReceiverService.saveOrUpdate(receiver);
							messageReceiverUserService.saveOrUpdate(mru);
						}
					}
						
				}
				logger.debug("------>>>获取学生近期复学提醒信息:"+(System.currentTimeMillis()-startTime)+" ms...");
			}
		}
		
		String schoolCode = CacheAppManager.getSysConfigurationByCode("graduateData.schoolCode").getParamValue();
		/*if(ExStringUtils.isNotBlank(userRole)){
			String courseInfo = "";
			String message = "<lable style='color: red;'>以下考试课程还未提交成绩，请及时录入并提交成绩！</lable><br>";
			if(userRole.equals("jwy")){
				condition.put("branchSchool", user.getOrgUnit().getResourceid());
			}else{
				condition.put("teachId", user.getResourceid());
			}
			condition.put("teachType","facestudy");
			condition.put("resultStatus","notAllInput");
			condition.put("queryNotAllInput", "Y");
			//正考
			System.out.println("查询未录入成绩所用时长："+new Date());
			objPage = teachingJDBCService.findTeachingPlanClassCourseByCondition(condition, objPage);
			List<Map<String, Object>> result = objPage.getResult();
			if(result!=null && result.size()>0){
				for (Map<String, Object> map : result) {
					message += "班级："+map.get("classesname")+"<br>考试批次："+map.get("batchname")+"<br>课程："+map.get("coursename")+"<br><br>";
				}
				courseInfo +=  "正考<lable style='color: red;'>"+result.size()+"</lable>门未录入<br>";
			}
			//补考
			condition.put("failResultStatus","notAllInput");
			objPage = examResultsService.failExamStudentListNew(objPage,condition); 
			result = objPage.getResult();
			if(result!=null && result.size()>0){
				for (Map<String, Object> map : result) {
					message += "班级："+map.get("classesname")+"<br>考试批次："+map.get("batchname")+"<br>课程："+map.get("coursename")+"<br><br>";
				}
				courseInfo +=  "补考<lable style='color: red;'>"+result.size()+"</lable>门未录入";
			}
			if(message.contains("班级")){
				model.addAttribute("examInfo", message);
			}
			model.addAttribute("courseInfo", courseInfo);
			System.out.println(new Date());
		}*/
		
		model.addAttribute("firstIndex", firstIndex);
		model.addAttribute("days", days);
		model.addAttribute("schoolCode", schoolCode);
		model.addAttribute("rootUrl", CacheAppManager.getSysConfigurationByCode("web.uploadfile.rooturl").getParamValue());
		model.addAttribute("isDisplayScore", CacheAppManager.getSysConfigurationByCode("no_schooling_no_display_score").getParamValue());
		logger.debug("------>>>系统主界面:"+(System.currentTimeMillis()-startTime)+" ms...");
		return "/framework";
	}
	/*
	private List<TeachTaskDetails> getTeachTaskDetailsByTime(List<TeachTaskDetails> alllist,Date time){
		List<TeachTaskDetails> list = new ArrayList<TeachTaskDetails>(0);
		for (TeachTaskDetails t : alllist) {
			Date start = ExDateUtils.truncate(t.getStartTime(),Calendar.DATE);
			Date end = ExDateUtils.getDayEnd(t.getEndTime());
			if(start.before(time) && end.after(time)){
				list.add(t);
			}
		}
		return list;
	}
	*/
	
	/**
	 * 异步获取教学日历
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	/*
	@RequestMapping("/edu3/framework/getteachcalendar.html")
	public void getTeachCalendar(HttpServletRequest request,HttpServletResponse response) throws WebException{
		try {
			int months = Integer.parseInt(ExStringUtils.defaultIfEmpty(request.getParameter("months"), "0"));
			Map<String, Object> map = new HashMap<String, Object>(0);
			
			User user = SpringSecurityHelper.getCurrentUser();				
			String days[] = new String[42];
			for (int i = 0; i < 42; i++) {
				days[i] = "";
			}
			
			GregorianCalendar currentDay = new GregorianCalendar();
			int today = currentDay.get(Calendar.DAY_OF_MONTH);
			
			currentDay.add(Calendar.MONTH, months);//设为实际月数
			
			int month = currentDay.get(Calendar.MONTH);
			int year = currentDay.get(Calendar.YEAR);
			map.put("current_date", year+"年"+(month+1)+"月");
										
			Calendar thisMonth = Calendar.getInstance();
			thisMonth.set(Calendar.MONTH, month);
			thisMonth.set(Calendar.YEAR, year);
			thisMonth.setFirstDayOfWeek(Calendar.SUNDAY);
			thisMonth.set(Calendar.DAY_OF_MONTH, 1);
			int firstIndex = thisMonth.get(Calendar.DAY_OF_WEEK) - 1;
			int maxIndex = thisMonth.getActualMaximum(Calendar.DAY_OF_MONTH);
			
			Map<String,Object> condition =  new HashMap<String, Object>();
			condition.put("userId", user.getResourceid());
			condition.put("orderBy", "startTime");	
			List<TeachTaskDetails> alllist = teachTaskDetailsService.findTeachTaskDetailsByCondition(condition);
			
			try {
				for (TeachTaskDetails teachTaskDetails : alllist) {
					if("unfinished".equals(teachTaskDetails.getStatus()) && ExDateUtils.getTodayEnd().after(teachTaskDetails.getEndTime())){
						teachTaskDetails.setStatus("finished");
						teachTaskDetailsService.update(teachTaskDetails);
					}
				}
			} catch (Exception e) {
				logger.error("更新教学任务状态失败：{}",e.fillInStackTrace());
			}			
			
			for (int i = 0; i < maxIndex; i++) {
				days[firstIndex + i] = String.valueOf(i + 1);
				if(months==0 && today==i+1){
					days[firstIndex + i] = "<span style='color:red;'>"+String.valueOf(i + 1)+"</span>";
				}					
				if(!CacheAppManager.getSysConfigurationByCode("sysuser.usertype.student").getParamValue().equals(user.getUserType())){//如果为老师
					List<TeachTaskDetails> list = getTeachTaskDetailsByTime(alllist,thisMonth.getTime());
					thisMonth.add(Calendar.DAY_OF_MONTH, 1);
					if(null!=list && list.size()>0){
						String title = "";				
						int task = 1;
						for (TeachTaskDetails t : list) {
							title += (task++) +". "+t.getTaskContent()+"<br/>";
						}
						days[firstIndex + i] += "<a href='##' title='"+title+"' class='_calendar_tips'>("+list.size()+")</a>";
					}
				}			
			}	
			map.put("days", Arrays.asList(days));
			renderJson(response, JsonUtils.mapToJson(map));	
		} catch (Exception e) {
			logger.error("获取教学日历出错："+e.fillInStackTrace());
		}		
	}
	*/
	
	/**
	 * 异步获取系统消息
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/framework/getMessage.html")
	public void getMessage(HttpServletRequest request,HttpServletResponse response) throws WebException{
		try {			
			//Page objPage = new Page();
			//objPage.setPageSize(15);
			//objPage.setOrderBy("message.sendTime"); //按发送时间排序
			//objPage.setOrder(Page.DESC);
			
			Map<String,Object> condition = new HashMap<String,Object>();//查询条件			
			User user = SpringSecurityHelper.getCurrentUser();
			condition.put("userName", user.getUsername());
			condition.put("unitCode", null!=user.getOrgUnit()?user.getOrgUnit().getUnitCode():"");
			condition.put("roleCode", ExCollectionUtils.fetchPropertyToString(user.getRoles(), "roleCode", ","));
			condition.put("fetchSize", 15);
			condition.put("userId", user.getResourceid());
			
			//Page messagePage = messageReceiverService.findMessageByCondition(condition, objPage);

			//List<String> statlist = messageStatService.getIsReadMsgIds(user.getResourceid());
			
			//List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();			
//			for (Object o : messagePage.getResult()) {
//				MessageSender sender = (MessageSender)o;
//				Map<String,Object> map = new HashMap<String,Object>();
//				map.put("resourceid", sender.getMessage().getResourceid());
//				map.put("msgType", JstlCustomFunction.dictionaryCode2Value("CodeMsgType", sender.getMessage().getMsgType()));
//				map.put("msgTitle", sender.getMessage().getMsgTitle());
//				map.put("sendTime", ExDateUtils.formatDateStr(sender.getMessage().getSendTime(), "MM/dd HH:mm"));
//				map.put("sender", sender.getSender());
//				map.put("isRead", statlist.contains(sender.getMessage().getResourceid()));
//				list.add(map);
//			}
			List<Map<String, Object>> list = messageReceiverService.getUserMessageList(condition);
			renderJson(response,JsonUtils.listToJson(list));			
		} catch (Exception e) {
			logger.error("获取用户消息出错："+e.fillInStackTrace());
		}		
	}
	/**
	 * 登录时如果用户角色为学生则把相应的教学计划查出
	 * @param model
	 * @param user
	 */
	private void getStudentTeachingPlanList(User user,ModelMap model )throws WebException{

		List<StudentInfo> studentInfos = studentInfoService.findStuListByUserId(user.getResourceid());
		String defaultStudentId = "";
		if(null != user.getPropertys(UserExtends.USER_EXTENDCODE_DEFAULTROLLID)){
			defaultStudentId = user.getPropertys(UserExtends.USER_EXTENDCODE_DEFAULTROLLID).getExValue();
			model.addAttribute("defaultStudentId", defaultStudentId);		
		
		}

		model.addAttribute("studentInfoList", studentInfos);	
		StudentInfo defaultStudentInfo = null;
		for (StudentInfo studentInfo : studentInfos) {
			if(studentInfo.getResourceid().equals(defaultStudentId)){
				defaultStudentInfo  = studentInfo;
				break;
			}
		}
		if(defaultStudentInfo != null){//获取学籍状态
			model.addAttribute("defaultStudentStatus", defaultStudentInfo.getStudentStatus());
		}
		model.addAttribute("currentStudent",defaultStudentInfo);
		
		Map<String, Object> voMap      = getTeachingPlanVoList(defaultStudentInfo);
		
		//检查该学生是否关联教学计划
		if(null == voMap || voMap.isEmpty()){
			model.addAttribute("noTeachingPlan", Constants.BOOLEAN_YES);
		}
		
		model.addAttribute("teachingPlanList", voMap);
		//TODO 待优化
		Map<String, Object> map 	   = studentInfoService.getStudentOrderTime();
		String yearTerm = CacheAppManager.getSysConfigurationByCode("sysCurrentGrade").getParamValue();
		StudentInfo studentInfo = studentInfoService.get(user.getPropertys(UserExtends.USER_EXTENDCODE_DEFAULTROLLID).getExValue());
		Integer term = (int) ((Integer.parseInt(yearTerm.substring(0, 4))-studentInfo.getGrade().getYearInfo().getFirstYear())*2 + Integer.parseInt(yearTerm.substring(5, 6)));
		//String term = yearTerm.replace(".", "_0");//实际开课学期
		model.addAttribute("term", term);
		if(!map.isEmpty()){
			model.addAttribute("stuOrderTime", map);
		}
	}

	//设置返回的resultMap的消息数组
	private Map<String,Object> setResultMapMsg(Map <String,Object> resultMap,String msg){
	
	   if (null==resultMap) {
		   resultMap = new HashMap<String, Object>();
	   }
	   
	    List<String> msgList =  (List<String>) resultMap.get("msg"); 

        if (null==msgList) {
        	msgList = new ArrayList<String>() ;
        	msgList.add(msg);
			resultMap.put("msg",msgList);
		}else {
			msgList.add(msg);
			resultMap.put("msg",msgList);
		}
        
		return resultMap;
	}
	
	/**
	 * 将教学计划与学习计划转换成TeachingPlanVo
	 * @param studentInfo
	 * @return
	 */
	private  Map<String, Object> getTeachingPlanVoList(StudentInfo studentInfo)throws WebException{
		
		Map<String, Object> voMap       = new HashMap<String, Object>();
		//FIXED BY HZG 优化
		//User user 					    = SpringSecurityHelper.getCurrentUser();	
		
		
		//if(null != user.getPropertys(UserExtends.USER_EXTENDCODE_DEFAULTROLLID)){
		if(null != studentInfo){
			//String defalutStudentId     = "";
			//if (ExStringUtils.isNotEmpty(studentId)) {
			//	defalutStudentId        = studentId;
		//	}else {
		//		defalutStudentId        = user.getPropertys(UserExtends.USER_EXTENDCODE_DEFAULTROLLID).getExValue();
		//	}
			//StudentInfo studentInfo     = studentInfoService.get(defalutStudentId);
			TeachingPlan teachingPlan   = teachingPlanService.get(null==studentInfo.getTeachingPlan()?"":studentInfo.getTeachingPlan().getResourceid());
			//Fixed by hzg 使用SQL方式，避免hibernate lazy 查询
			//List<StudentLearnPlan> list = studentLearnPlanService.getStudentLearnPlanList(studentInfo.getResourceid(),null==teachingPlan?"":teachingPlan.getResourceid());
			List<StudentLearnPlan> list = new ArrayList<StudentLearnPlan>();
			try {
				list = studentLearnPlanService.getStudentLearnPlanListBySql(studentInfo.getResourceid(), null);
			} catch (Exception e) {
				//忽略
			}
			
			if (null!=teachingPlan) {
				voMap = studentLearnPlanService.changeTeachingPlanToTeachingPlanVo(studentInfo,list, teachingPlan);
			}
		}
		
		//StudentInfo studentInfo = studentInfoService.findUniqueByProperty("studentBaseInfo.certNum", SpringSecurityHelper.getCurrentUserName());
		
		return voMap;
	}
				
	/*
	 * 校验验证码是否正确
	 */
	private boolean isValidateCode(HttpSession  session,String validateCode) {		
		return CaptchaServiceSingleton.getInstance().validateCaptchaResponse(validateCode.trim(), session);  		 
	}
	/**
	 * 获取学生联系方式
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	/*
	@RequestMapping(value = "/edu3/framework/studentinfo/ajax.html")
	public void ajaxStudentInfo(HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String, Object> resultMap = new HashMap<String, Object>();		
		String success = Constants.BOOLEAN_NO;
		try {			
			User user = SpringSecurityHelper.getCurrentUser();
			if(user!=null){
				StudentInfo studentInfo = studentInfoService.getStudentInfoByUser(user);
				String email = ExStringUtils.trimToEmpty(studentInfo.getStudentBaseInfo().getEmail());
				String mobile = ExStringUtils.trimToEmpty(studentInfo.getStudentBaseInfo().getMobile());
				if(ExStringUtils.isBlank(email)||ExStringUtils.isBlank(mobile)){
					success =  Constants.BOOLEAN_YES;
					resultMap.put("email",email);
					resultMap.put("mobile",mobile);
				} 
			}			
		} catch (Exception e) {	
			success = Constants.BOOLEAN_NO;
		}		
		resultMap.put("success", success);
		renderJson(response, JsonUtils.mapToJson(resultMap));
	}
	*/
	/**
	 * 获取学生学习日历
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	/*
	@RequestMapping("/edu3/framework/teachingcalendar/list.html")
	public void listTeachingCalendar(HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String, Object> resultMap = new HashMap<String, Object>();		
		try {
			if(SpringSecurityHelper.isUserInRole(CacheAppManager.getSysConfigurationByCode("sysuser.role.student").getParamValue())){//如果为学生
				Date startTime = null;
				Date endTime = null;
				Calendar ca = Calendar.getInstance(); 
				ca.set(ca.get(Calendar.YEAR),ca.get(Calendar.MONTH),1);//月初
				startTime = ca.getTime(); 
				ca.add(Calendar.MONTH,   1); 	
				ca.add(Calendar.DAY_OF_MONTH,-1);//月底
				endTime = ca.getTime();
				
				Map<String, Object> condition = new HashMap<String, Object>();
				condition.put("status", Constants.BOOLEAN_TRUE);//已发布的				
				condition.put("startTime", ExDateUtils.formatDateStr(startTime, ExDateUtils.PATTREN_DATE));
				condition.put("endTime", ExDateUtils.formatDateStr(endTime, ExDateUtils.PATTREN_DATE));
				condition.put("orderby", "startTime asc,endTime asc,resourceid");
				List<TeachingCalendar> calList = teachingCalendarService.findTeachingCalendarByCondition(condition);//本月所有教学日历
				
				int maxIndex = ca.getActualMaximum(Calendar.DAY_OF_MONTH);
				StringBuilder title = new StringBuilder();
				for (int i = 1; i <= maxIndex; i++) {
					ca.set(Calendar.DAY_OF_MONTH, i);
					List<TeachingCalendar> list = getTeachingCalendarByDay(calList, ca.getTime());
					if(ExCollectionUtils.isNotEmpty(list)){
						title.delete(0, title.length());
						for (int j = 0; j < list.size(); j++) {
							title.append("<div style='margin-bottom: 5px;'>");
							title.append("<b>");
							title.append(j+1);
							title.append(". ");
							title.append(list.get(j).getTitle());
							title.append("</b>");
							title.append("<div>");
							title.append(list.get(j).getContent());
							title.append("</div>");
							title.append("</div>");
						}
						Map<String, Object> tempMap = new HashMap<String, Object>();
						tempMap.put("calSize", list.size());
						tempMap.put("calContent", title.toString());
						
						resultMap.put(Integer.toString(i), tempMap);
					}					
				}
				
			}
		} catch (Exception e) {
			logger.error("获取学生学习日历出错："+e.fillInStackTrace());
		}
		renderJson(response, JsonUtils.mapToJson(resultMap));
	}
	*/

	@RequestMapping("/edu3/student/mystudyplan.html")
	public String myStudyPlan(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		User user = SpringSecurityHelper.getCurrentUser();		
		if(SpringSecurityHelper.isUserInRole("ROLE_STUDENT")){//如果为学生，查出学生的教学计划
			model.addAttribute("currentrole","student");
			getStudentTeachingPlanList(user,model);
		}
		model.addAttribute("isDisplayScore", CacheAppManager.getSysConfigurationByCode("no_schooling_no_display_score").getParamValue());
		return "/edu3/learning/studentleanplan/student-mystudyplan";
	}
	
	/**
	 * 获取学生的其他学习计划--主要是针对一个学生在存在不同年级的情况
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/student/otherStudyplan.html")
	public String otherStudyPlan(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		String studentId = ExStringUtils.trim(request.getParameter("studentId"));
		Map<String, Object> studyPlanVoMap = null;
		StudentInfo currentStudent = new StudentInfo();
		try {
			if(SpringSecurityHelper.isUserInRole("ROLE_STUDENT")
					&& ExStringUtils.isNotEmpty(studentId)){
				currentStudent = studentInfoService.findUniqueByProperty("resourceid", studentId);
				studyPlanVoMap =  getTeachingPlanVoList(currentStudent);
			}
			if(studyPlanVoMap == null){
				studyPlanVoMap = new HashMap<String, Object>();
			}
			model.addAttribute("currentStudent", currentStudent);
			model.addAttribute("teachingPlanList", studyPlanVoMap);
		} catch (ServiceException e) {
			logger.error("获取学生其他学习计划出错", e);
		}
		
		return "/edu3/learning/studentleanplan/student-otherstudyplan";
	}
	
	/**
	 * 确认信息界面
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/confirmMsgView.html")
	public String comfirmMsgView(HttpServletRequest request,ModelMap model) {
		try {
			User user = SpringSecurityHelper.getCurrentUser();
			String schoolCode = JstlCustomFunction.getSysConfigurationValue("graduateData.schoolCode", "server");
			if(ExStringUtils.isNotBlank(schoolCode)){
				model.addAttribute("schoolCode", schoolCode);
			}			
			if(SpringSecurityHelper.isUserInRole("ROLE_STUDENT")){//
				//model.addAttribute("studentInfo", studentInfoService.getByUserId(user.getResourceid()));//这里获取不了基本信息
				List<StudentInfo> studentInfos = studentInfoService.findStuListByUserId(user.getResourceid());
				String defaultStudentId = "";
				if(null != user.getPropertys(UserExtends.USER_EXTENDCODE_DEFAULTROLLID)){
					defaultStudentId = user.getPropertys(UserExtends.USER_EXTENDCODE_DEFAULTROLLID).getExValue();			
				}
					
				StudentInfo defaultStudentInfo = null;
				for (StudentInfo studentInfo : studentInfos) {
					if(studentInfo.getResourceid().equals(defaultStudentId)){
						defaultStudentInfo  = studentInfo;
						break;
					}
				}
				model.addAttribute("studentInfo", defaultStudentInfo);
			}
			
			model.addAttribute("phoneComfirm", CacheAppManager.getSysConfigurationByCode("phoneComfirm").getParamValue());//确认手机登陆	
		} catch (Exception e){
			logger.info("comfirmMsgView --- 获取学生出错:"+e.getStackTrace());	
		}
		return "/edu3/recruit/enroll/stuinfo-confirm";
	}
	
	/**
	 * 初次登录确认信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/edu3/confirmMsg.html")
	public void comfirmMsg(HttpServletRequest request,HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		int statusCode = 200;
		String message = "确认信息成功！";
		try {
			do{
				String stuid = ExStringUtils.trim(request.getParameter("stuid"));
				String name			= ExStringUtils.trim(request.getParameter("name"));
				String certType		= ExStringUtils.trim(request.getParameter("certType"));
				String certNum		= ExStringUtils.trim(request.getParameter("certNum"));
				String mobile		= ExStringUtils.trim(request.getParameter("mobile"));
				String mobileCode	= ExStringUtils.trim(request.getParameter("mobileCode"));
				//桂林医要求增加性别  20170405
				String gender		= ExStringUtils.trim(request.getParameter("gender"));
				String isApply		= request.getParameter("isApply");
				String _phoneComfirm		= ExStringUtils.trim(request.getParameter("phoneComfirm"));
				StudentInfo defaultStudentInfo = null;
				Set<String> certNumSet	= new HashSet<String>();
				User user = SpringSecurityHelper.getCurrentUser();		
				if(ExStringUtils.isEmpty(_phoneComfirm)){
					// 手机验证码验证
					String phoneComfirm = CacheAppManager.getSysConfigurationByCode("phoneComfirm").getParamValue();
					if("1".equals(phoneComfirm)){// 需要手机短信验证
						String cacheMsgAuthCode = memcachedManager.get(mobile);
						if(ExStringUtils.isEmpty(cacheMsgAuthCode)){
							statusCode = 300;
							message  = "手机验证码已经过期，请重新发送！";
							continue;
						}
						if(!cacheMsgAuthCode.equals(mobileCode)){
							statusCode = 300;
							message  = "手机验证码不正确！";
							 continue;
						}
					}
					
					// 检查该号码是否已被使用
					
//					List<StudentBaseInfo> studentList = studentService.findByHql("from "+StudentBaseInfo.class.getSimpleName()+" s where s.mobile=? and s.isDeleted=0", mobile);
//					if(ExCollectionUtils.isNotEmpty(studentList)) {
//						for(StudentBaseInfo stuBase : studentList){
//							certNumSet.add(stuBase.getCertNum());
//						}
//					}
					List<StudentInfo> stuList = studentInfoService.findByHql(" from "+StudentInfo.class.getSimpleName()+" where studentBaseInfo.mobile=? and firstComfirmStatus=? and studentStatus=? and isDeleted=0 ", new Object[]{mobile,"1","11"});
					if(ExCollectionUtils.isNotEmpty(stuList)) {
						for(StudentInfo stu : stuList){
							certNumSet.add(stu.getStudentBaseInfo().getCertNum());
						}
					}
					if(ExCollectionUtils.isNotEmpty(certNumSet) && !certNumSet.contains(user.getUsername())){
						statusCode = 300;
						message = "该手机号码已被使用！";
						continue;
					}
					if(!SpringSecurityHelper.isUserInRole("ROLE_STUDENT")){
						statusCode = 300;
						message = "你不是学生角色不能进行此操作！";
						continue;
					}
					//model.addAttribute("studentInfo", studentInfoService.getByUserId(user.getResourceid()));//这里获取不了基本信息
					List<StudentInfo> studentInfos = studentInfoService.findStuListByUserId(user.getResourceid());
					String defaultStudentId = "";
					if(null != user.getPropertys(UserExtends.USER_EXTENDCODE_DEFAULTROLLID)){
						defaultStudentId = user.getPropertys(UserExtends.USER_EXTENDCODE_DEFAULTROLLID).getExValue();			
					}
					
					for (StudentInfo studentInfo : studentInfos) {
						if(studentInfo.getResourceid().equals(defaultStudentId)){
							defaultStudentInfo  = studentInfo;
							break;
						}
					}
				} else{
					defaultStudentInfo = studentInfoService.get(stuid);
					user = defaultStudentInfo.getSysUser();
					// 检查该号码是否已被使用
//					List<StudentBaseInfo> studentList = studentService.findByHql("from "+StudentBaseInfo.class.getSimpleName()+" s where s.mobile=? and s.isDeleted=0", mobile);
//					if(ExCollectionUtils.isNotEmpty(studentList)) {
//						for(StudentBaseInfo stuBase : studentList){
//							certNumSet.add(stuBase.getCertNum());
//						}
//					}
					List<StudentInfo> stuList = studentInfoService.findByHql(" from "+StudentInfo.class.getSimpleName()+" where studentBaseInfo.mobile=? and firstComfirmStatus=? and studentStatus=? and isDeleted=0 ", new Object[]{mobile,"1","11"});
					if(ExCollectionUtils.isNotEmpty(stuList)) {
						for(StudentInfo stu : stuList){
							certNumSet.add(stu.getStudentBaseInfo().getCertNum());
						}
					}
					if(ExCollectionUtils.isNotEmpty(certNumSet) && !certNumSet.contains(user.getUsername())){
						statusCode = 300;
						message = "该手机号码已被使用！";
						continue;
					}
				}
				
				//然后修改
				if(defaultStudentInfo!=null && defaultStudentInfo.getResourceid().equals(stuid)){

					StringBuffer content = new StringBuffer("学号为:"+defaultStudentInfo.getStudyNo()+"的学生");
					Map<String, String> updateInfo = new HashMap<String, String>();
					if(ExStringUtils.isNotEmpty(name)&&!name.equals(defaultStudentInfo.getStudentBaseInfo().getName())){
						updateInfo.put("studentName", "'"+name+"'");
						content.append(",修改姓名:"+defaultStudentInfo.getStudentBaseInfo().getName()+" 为 <font color='green'>"+name+"</font><br/>");
					}
					if(ExStringUtils.isNotEmpty(certType)&&!certType.equals(defaultStudentInfo.getStudentBaseInfo().getCertType())){
						updateInfo.put("certType", "'"+certType+"'");
						content.append(",修改证件类型:"+defaultStudentInfo.getStudentBaseInfo().getCertType()+" 为 <font color='green'>"+certType+"</font><br/>");
					}
					if(ExStringUtils.isNotEmpty(certNum)){
						certNum = ExStringUtils.upperCase(certNum);
						if(!certNum.equals(defaultStudentInfo.getStudentBaseInfo().getCertNum())){
							updateInfo.put("certNum", "'"+certNum+"'");
							content.append(",修改证件号码:"+defaultStudentInfo.getStudentBaseInfo().getCertNum()+" 为 <font color='green'>"+certNum+"</font><br/>");
						}	
					}
					if(ExStringUtils.isNotEmpty(gender)&&!gender.equals(defaultStudentInfo.getStudentBaseInfo().getGender())){
						updateInfo.put("gender", "'"+gender+"'");
						content.append(",修改性别:"+JstlCustomFunction.dictionaryCode2Value("CodeSex", defaultStudentInfo.getStudentBaseInfo().getGender())+" 为 <font color='green'>"+JstlCustomFunction.dictionaryCode2Value("CodeSex",gender)+"</font><br/>");
					}
					if(ExStringUtils.isNotEmpty(mobile)&&!mobile.equals(defaultStudentInfo.getStudentBaseInfo().getMobile())){
						content.append(",修改手机号码:"+defaultStudentInfo.getStudentBaseInfo().getMobile()+" 为 <font color='green'>"+mobile+"</font><br/>");
						defaultStudentInfo.getStudentBaseInfo().setMobile(mobile);
					}
					
					if(!updateInfo.isEmpty()){//发送给教务员消息
						updateInfo.put("studentid", "'"+stuid+"'");
						MessageReceiver receiver = new MessageReceiver();
						receiver.setResourceid(null);
						
						Message msg = new Message();
						msg.setMsgTitle("学生("+defaultStudentInfo.getStudyNo()+")确认信息通知");
						msg.setContent(content.toString());
						msg.setMsgType("tips");
						msg.setSendTime(new Date());
						msg.setFormId("confirmInformation_studentLogin");
						msg.setFormType(JsonUtils.mapToJson(updateInfo));
						msg.setSender(user);
						msg.setSenderName(user.getCnName());
						msg.setOrgUnit(user.getOrgUnit());
						if(!"Y".equals(isApply)){//不需要申请
							msg.setStatus("Y");
						}
						String userObj=JstlCustomFunction.getSysConfigurationValue("student.confirm.sendmsg", "server");
						String [] userObjStr = userObj.split(":|：");
						List<String> userObjList = new ArrayList<String>();
						Set<MessageReceiverUser> userSet = new HashSet<MessageReceiverUser>();
						if (userObjStr.length > 0) {
							if ("role".equalsIgnoreCase(userObjStr[0].trim())) {// 以角色为单位
								receiver.setReceiveType("role");
								receiver.setRoleCodes(userObjStr[1].trim());
								receiver.setOrgUnitCodes(user.getOrgUnit().getUnitCode());
							} else if ("user".equalsIgnoreCase(userObjStr[0].trim())) {// 以用户为单位
								receiver.setReceiveType("user");
								String [] userStr = userObjStr[1].trim().split(",|，");
								for(int i=0 ;i<userStr.length;i++){
									MessageReceiverUser msgru = new MessageReceiverUser();
									User user1 =userService.getUserByLoginId(userStr[i]);
//											userService.get(userStr[i]);
									if(user1!=null ){
										msgru.setReceiver(user1);
										msgru.setUserName(user1.getUsername());
										msgru.setUserCNName(user1.getCnName());
										msgru.setMsgStatus("sended");
										msgru.setMessageReceiver(receiver);
										userSet.add(msgru);
									}
								}
								receiver.setMessageReceiverUsers(userSet);
							}
						}else{//如果参数未设置或者设置不正确时，发一条消息给超级管理员，让超级管理员处理
							msg.setMsgTitle("全局参数：student.confirm.sendmsg 未设置或设置不正确");
							msg.setContent("参考示例：第1种以角色为单位： role:ROLE_BRS_STUDENTSTATUS,ROLE_ADMIN 第2种以用户为单位： user:administrator,admin");
							receiver.setReceiveType("user");
							MessageReceiverUser msgru = new MessageReceiverUser();
							User user1 =userService.getUserByLoginId("administrator");
							if(user1!=null ){
								msgru.setReceiver(user1);
								msgru.setUserName(user1.getUsername());
								msgru.setUserCNName(user1.getCnName());
								msgru.setMsgStatus("sended");
								msgru.setMessageReceiver(receiver);
								userSet.add(msgru);
							}
							receiver.setMessageReceiverUsers(userSet);
						}
						
						receiver.setMessage(msg);
//						receiver.setReceiveType("role");
//						receiver.setRoleCodes("ROLE_BRS_STUDENTSTATUS");
//						receiver.setOrgUnitCodes(user.getOrgUnit().getUnitCode());
						sysMsgService.saveMsgAndMsgReceiver(msg, receiver);
//						List<MessageReceiverUser> userList = new ArrayList<MessageReceiverUser>();
//						userList.addAll(userSet);
//						messageReceiverUserService.batchSaveOrUpdate(userList);
					}
					if(!"Y".equals(isApply)){
						if(updateInfo.containsKey("studentName")){
							defaultStudentInfo.setStudentName(name);
							defaultStudentInfo.getStudentBaseInfo().setName(name);
						}
						if(updateInfo.containsKey("certType")){
							defaultStudentInfo.getStudentBaseInfo().setCertType(certType);
						}
						if(updateInfo.containsKey("certNum")){
							defaultStudentInfo.getStudentBaseInfo().setCertNum(certNum);
							if(("idcard").equals(defaultStudentInfo.getStudentBaseInfo().getCertType())){
								user.setUsername(certNum);
							}
						}
						if(updateInfo.containsKey("gender")){
							defaultStudentInfo.getStudentBaseInfo().setGender(gender);
						}
						userService.saveOrUpdate(user);
					}
					defaultStudentInfo.setFirstComfirmStatus("1");//设置已确认
					studentInfoService.update(defaultStudentInfo);
				}else{
					statusCode = 300;
					message = "确认信息失败！无法找到该学生信息";
				}
			} while(false);
		} catch (Exception e){
			logger.error("comfirmMsg --- 初次登录确认信息:"+e.getStackTrace());	
			statusCode = 300;
			message = "确认信息失败！请联系管理员";
		} finally {
			map.put("statusCode", statusCode);
			map.put("message", message);
			UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "13", UserOperationLogs.UPDATE,"学生首次登录确认：确认结果："+message+"。参数："+JsonUtils.mapToJson(Condition2SQLHelper.getConditionFromResquestByIterator(request)));
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 通过绑定的手机号码，自助找回密码
	 */
	@RequestMapping("/edu3/portal/user/findPassWord.html")
	public String findPassWord(HttpServletRequest request,HttpServletResponse response){
		return "/edu3/portal/help/findPassword";
	}
	/**
	 * 第一步验证 登录账号及姓名是否正确
	 */
	@RequestMapping("/edu3/portal/user/validateUserSubmit.html")
	public void validateUserSubmit(String userName,String userCnName,HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> map = new HashMap<String, Object>();
		String message="";
		String statusCode ="200";
		userName =StringEscapeUtils.escapeHtml(userName);
		userCnName =ExStringUtils.htmlEncode(userCnName);
		
		if(ExStringUtils.isNotBlank(userName)&&ExStringUtils.isNotBlank(userCnName)){
			String hql= "from "+User.class.getSimpleName()+" u where u.username=? and u.isDeleted = 0 and u.cnName=? ";
			User user = userService.findUnique(hql, new Object[]{userName.trim(),userCnName.trim()});
			String mobile ="";
			
			if(user!=null){
				if("student".equalsIgnoreCase(user.getUserType())){
					StudentInfo stu = studentInfoService.getStudentInfoByUser(user);
					//首次登陆确认情况 :0未确认,1已确认
					if("1".endsWith(stu.getFirstComfirmStatus())){
						statusCode = "200";
						mobile = stu.getStudentBaseInfo().getMobile();
						message="第一步成功：您已登录过系统并绑定过您的手机号："+mobile+"，<br>如果您确认你并未换手机号码，请点击输入验证码及发送短信至手机获取手机验证码";
						map.put("userId", user.getResourceid());
					}else{
						statusCode = "300";
						message="重置失败：您之前并未在系统中确认过手机号码的绑定，请联系教务点教务员进行人员重置密码或尝试使用初始密码 11 进行登录";
					}
				}else{
					statusCode = "300";
					message="重置失败:您输入的用户不是学生,重置功能当前只允许学生使用";
				}
				
			}else{//未找到学生
				statusCode = "300";
				message="根据您输入的账号及姓名，在系统中未找到您的信息，请检查输入信息是否正确";
			}
			map.put("mobile", mobile);
			map.put("message", message);
			map.put("statusCode", statusCode);
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 第二步   1、让学生确认手机号码是否正确，其中隐藏中间4位
	 */
	@RequestMapping("/edu3/portal/user/findPwdSecStep.html")
	public String findPwdSecStep(String userId,String mobile,HttpServletRequest request,HttpServletResponse response,ModelMap model){
		String tmpMobile = mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2");
		model.addAttribute("mobile", StringEscapeUtils.escapeHtml(mobile));
		model.addAttribute("tmpMobile", StringEscapeUtils.escapeHtml(tmpMobile));
		model.addAttribute("userId", StringEscapeUtils.escapeHtml(userId));
		return "/edu3/portal/help/findPwdSecStep";
	}
	/**
	 * 第二步，确认手机验证码是否正确，状态200 及 isCorret 为Y时，正确
	 */
	@RequestMapping("/edu3/portal/user/validateMsgAuthCode.html")
	public void validateMsgAuthCode(String userId,String mobile,String msgAuthCode,HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> map = new HashMap<String, Object>();
		String statusCode="200";
		String message="手机验证码正确";
		mobile = StringEscapeUtils.escapeHtml(mobile);
		String cacheMsgAuthCode = memcachedManager.get(mobile);
		String isCorret="Y";
		do{
			if(ExStringUtils.isEmpty(cacheMsgAuthCode) ){
				 statusCode	 = "300";
				 message  = "手机验证码不正确或已经过期，请重新发送！";
				 isCorret="N";
				 continue;
			}
			if(!cacheMsgAuthCode.equals(msgAuthCode)){
				 statusCode	 = "300";
				 message  = "手机验证码不正确！";
				 isCorret="N";
				 continue;
			}
		}while(false);
//		isCorret="Y";
//		statusCode="200";
		map.put("statusCode", statusCode);
		map.put("message", message);
		map.put("userId", StringEscapeUtils.escapeHtml(userId));		
		map.put("isCorret", isCorret);
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 第三步，重置密码
	 */
	@RequestMapping("/edu3/portal/user/reSetPassWord.html")
	public String reSetPassWord(String userId,HttpServletRequest request,HttpServletResponse response,ModelMap model){
		model.addAttribute("userId", StringEscapeUtils.escapeHtml(userId));
		return "/edu3/portal/help/reSetPassWord";
	}
	/**
	 * 重置密码
	 */
	@RequestMapping("/edu3/portal/user/validateReSetPassWord.html")
	public void validateReSetPassWord(String userId,String password,HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> map = new HashMap<String, Object>();
		User user = userService.get(userId);
		String statusCode="200";
		String message="重置密码成功！请关掉当前页面，返回首页进行登录";
		userId =StringEscapeUtils.escapeHtml(userId);
		password = StringEscapeUtils.unescapeHtml(StringEscapeUtils.escapeHtml(password));
		if(user!=null){
			if(ExStringUtils.isNotBlank(password)){
				try {
					user.setPassword(BaseSecurityCodeUtils.getMD5(password.trim()));
				} catch (NoSuchAlgorithmException e) {
					logger.error("重置密码：密码加密出错");					
				}
				userService.update(user);
			}
			
		}else{
			statusCode="300";
			message="第一步参数设置错误，请重新打开当前页面，重新进行设置";
		}
		
		map.put("statusCode", statusCode);
		map.put("message", message);
		UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "13", UserOperationLogs.UPDATE,"学生通过手机重置密码：结果："+message+"。参数："+JsonUtils.mapToJson(Condition2SQLHelper.getConditionFromResquestByIterator(request)));
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 提示修改初始化密码
	 */
	@RequestMapping("/edu3/sys/tipUpdatePassword.html")
	public String tipUpdatePassword(HttpServletResponse response){
		return "/tipUpdatePassword";
	}
	
}