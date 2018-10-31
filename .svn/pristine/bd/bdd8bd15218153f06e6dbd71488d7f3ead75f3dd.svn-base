package com.hnjk.edu.portal.controller;

import java.io.UnsupportedEncodingException;
import java.util.Date;
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
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.utils.ExBeanUtils;
import com.hnjk.core.foundation.utils.ExCollectionUtils;
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.foundation.utils.SensitivewordFilter;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.helper.QueryParameter;
import com.hnjk.core.support.base.controller.FileUploadAndDownloadSupportController;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.Major;
import com.hnjk.edu.basedata.service.IGradeService;
import com.hnjk.edu.basedata.service.IMajorService;
import com.hnjk.edu.learning.service.IStudentLearnPlanService;
import com.hnjk.edu.portal.model.Message;
import com.hnjk.edu.portal.model.MessageReceiver;
import com.hnjk.edu.portal.model.MessageReceiverUser;
import com.hnjk.edu.portal.service.IMessageReceiverService;
import com.hnjk.edu.portal.service.IMessageReceiverUserService;
import com.hnjk.edu.portal.service.IMessageSenderService;
import com.hnjk.edu.portal.service.IMessageStatService;
import com.hnjk.edu.portal.service.ISysMsgService;
import com.hnjk.edu.roll.service.IClassesService;
import com.hnjk.extend.taglib.tree.TreeBuilder;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.Attachs;
import com.hnjk.platform.system.service.IAttachsService;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.OrgUnit;
import com.hnjk.security.model.Role;
import com.hnjk.security.model.User;
import com.hnjk.security.service.IOrgUnitService;
import com.hnjk.security.service.IRoleService;
import com.hnjk.security.service.IUserService;
/**
 * 通知管理
 * <code>SysMsgController</code><p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-4-13 上午11:59:38
 * @see 
 * @version 1.0
 */
@Controller
public class SysMsgController extends FileUploadAndDownloadSupportController{

	private static final long serialVersionUID = 7344382473564415769L;
	
	@Autowired
	@Qualifier("sysMsgService")
	private ISysMsgService sysMsgService;
	
	@Autowired
	@Qualifier("messageSenderService")
	private IMessageSenderService messageSenderService;
	
	@Autowired
	@Qualifier("messageReceiverService")
	private IMessageReceiverService messageReceiverService;
	
	@Autowired
	@Qualifier("attachsService")
	private IAttachsService attachsService;	
	
	@Autowired
	@Qualifier("userService")
	private IUserService userService;
	
	@Autowired
	@Qualifier("roleService")
	private IRoleService roleService;

	@Autowired
	@Qualifier("orgUnitService")
	private IOrgUnitService orgUnitService;	
	
	@Autowired
	@Qualifier("messageStatService")
	private IMessageStatService messageStatService;
	
	@Autowired
	@Qualifier("studentLearnPlanService")
	private IStudentLearnPlanService studentLearnPlanService;
	
	@Autowired
	@Qualifier("gradeService")
	private IGradeService gradeService;
	
	@Autowired
	@Qualifier("classesService")
	private IClassesService classesService;
	
	@Autowired
	@Qualifier("messageReceiverUserService")
	private IMessageReceiverUserService messageReceiverUserService;
	
	@Autowired
	@Qualifier("majorService")
	private IMajorService majorService;
	
//	@Autowired
//	@Qualifier("teachtaskservice")
//	private ITeachTaskService teachtaskservice;	
	
	/**
	 * 消息列表
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/edu3/portal/message/list.html")
	public String listMessage(HttpServletRequest request,HttpServletResponse response,Page objPage, ModelMap model) throws Exception{	
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件	
		User user = SpringSecurityHelper.getCurrentUser();
		
		String msgTitle = request.getParameter("msgTitle");
		String msgType = request.getParameter("msgType");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String fromtype = request.getParameter("fromtype");
		String flag		= request.getParameter("flag");
		
		if(ExStringUtils.isNotEmpty(msgTitle)){
			condition.put("msgTitle", msgTitle);
		}
		if(ExStringUtils.isNotEmpty(msgType)){
			condition.put("msgType", msgType);
		}
		if(ExStringUtils.isNotEmpty(startDate)){
			condition.put("startDate", startDate);
		}
		if(ExStringUtils.isNotEmpty(endDate)){
			condition.put("endDate", endDate);
		}
		if(ExStringUtils.isNotEmpty(fromtype)){
			condition.put("fromtype", fromtype);
		}
		
		// inbox - 收件箱 / draftbox - 草稿箱 / sendbox - 发件箱
		String type = ExStringUtils.defaultIfEmpty(request.getParameter("type"), "inbox");
		String typeName = "收件箱";
		condition.put("type", type);
		model.addAttribute("type", type);	
		if("inbox".equalsIgnoreCase(type)){// 收件箱
			if(null!=user){
				objPage.setOrderBy("messageReceiver.message.sendTime"); //按发送时间排序
				objPage.setOrder(QueryParameter.DESC);
				Map<String,Object> _condition = new HashMap<String,Object>();
				_condition.put("userId", user.getResourceid());
				_condition.put("userName", user.getUsername());
				_condition.put("isDraft", Constants.BOOLEAN_NO);
				_condition.put("sendTime", new Date());
				_condition.put("isDeleted", 0);
				Page messageReceiverUser = messageReceiverUserService.findByCondition(_condition, objPage);
				model.addAttribute("messageReceiverUserPage", messageReceiverUser);	
			}
		} else {
			objPage.setOrderBy("sendTime"); //按发送时间排序
			objPage.setOrder(QueryParameter.DESC);
			if("draftbox".equalsIgnoreCase(type)){//草稿箱
				typeName = "草稿箱";
				condition.put("isDraft", Constants.BOOLEAN_YES);					
			} else if("sendbox".equalsIgnoreCase(type)){//发件箱
				typeName = "发件箱";
				condition.put("isDraft", Constants.BOOLEAN_NO);						
			}
			condition.put("senderId", user.getResourceid());//发送人
//			Page messagePage = messageSenderService.findMessageByCondition(condition, objPage);	
			Page messagePage = sysMsgService.findMessageByCondition(condition, objPage);
			model.addAttribute("messagePage", messagePage);	
		}		
		model.addAttribute("condition", condition);
		if("export".equals(flag)){
			try {
				typeName = new String(typeName.getBytes("GBK"), "iso8859-1");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			model.addAttribute("flag", flag);
			response.setCharacterEncoding("GB2312");
			response.setContentType("application/vnd.ms-excel");
		  	response.setHeader("Content-Disposition", "attachment; filename="+typeName+".xls"); 
			return "/edu3/portal/system/message/msg-export";
		}
		return "/edu3/portal/system/message/msg-list";
	}
	
	/**
	 * 新增编辑回复消息
	 * @param resourceid
	 * @param request
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/portal/message/input.html")
	public String editMessage(String resourceid,HttpServletRequest request,ModelMap model) throws WebException{			
		Message message = null;		
		MessageReceiver messageReceiver = new MessageReceiver();
		messageReceiver.setReceiveType("user");
		//判断用户类型，校外学习中心只能发送用户消息
		User cureentUser = SpringSecurityHelper.getCurrentUser();
		model.addAttribute("storeDir", cureentUser.getUsername());
		if(cureentUser.getOrgUnit().getUnitType().
				indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){
			model.addAttribute("isBrschool", true);
		}
		String roleCode = CacheAppManager.getSysConfigurationByCode("sysuser.role.teacher").getParamValue();
		if(SpringSecurityHelper.isUserInRole(roleCode)){//老师角色					
			model.addAttribute("isTeacher", true);
		}
		String type = ExStringUtils.defaultIfEmpty(request.getParameter("type"), "sendBox");
		if(ExStringUtils.isNotBlank(resourceid)){ //编辑
			/*MessageSender messageSender = messageSenderService.findUniqueByProperty("message.resourceid", resourceid);
			message = messageSender.getMessage();*/
			message = sysMsgService.get(resourceid);
			if(Constants.BOOLEAN_NO.equals(message.getIsDraft())){
				type = "sendBox";
			}
			messageReceiver = messageReceiverService.findUniqueByProperty("message.resourceid", resourceid);
			if(messageReceiver==null) {
				messageReceiver = new MessageReceiver();
				messageReceiver.setReceiveType("user");
				messageReceiver.setMessage(message);
			}
			codesToNames(messageReceiver);
		/*	codes2Names(messageReceiver);
			List<Attachs> attachs = attachsService.findAttachsByFormId(message.getResourceid());
			message.setAttachs(attachs);
			model.addAttribute("type", request.getParameter("type"));		*/	
			if(message.getSendTime()!=null&&ExDateUtils.getCurrentDateTime().before(message.getSendTime())) {
				model.addAttribute("sendMode", 1);
			}
		}else { //新增 / 回复
			message = new Message();
			message.setMsgType("usermsg"); //默认为个人消息
			message.setIsReply(Constants.BOOLEAN_YES);
			
			String parentId = request.getParameter("parentId");
			if(ExStringUtils.isNotEmpty(parentId)){ //回复
				Message parentMessage = sysMsgService.get(parentId);//要回复的消息
				message.setParent(parentMessage);
				/*MessageSender messageSender = messageSenderService.findUniqueByProperty("message.resourceid", parentId);
				User sender = userService.get(messageSender.getSenderId());*/
				messageReceiver.setUserNames(parentMessage.getSender().getUsername()+",");
				messageReceiver.setUserCnNames(parentMessage.getSenderName()+",");
			}				
		}	
		model.addAttribute("message",message);	
		model.addAttribute("messageReceiver",messageReceiver);
		model.addAttribute("fromtype",ExStringUtils.trimToEmpty(request.getParameter("fromtype")));	
		model.addAttribute("type",type);	
		return "/edu3/portal/system/message/msg-form";
	}
	//接收人编码转名称
	private void codes2Names(MessageReceiver messageReceiver) {
		Set<MessageReceiverUser> messageReceiverUsers = messageReceiver.getMessageReceiverUsers();
		if(null!=messageReceiverUsers || messageReceiverUsers.size() > 0){
			String userCnNames = "";
			for(MessageReceiverUser mu:messageReceiverUsers){
				User user = userService.getUserByLoginId(mu.getUserName());
				if(user != null){
					userCnNames += user.getCnName()+",";
				}	
			}
			messageReceiver.setUserCnNames(userCnNames);
		}
		if(ExStringUtils.isNotEmpty(messageReceiver.getOrgUnitCodes())){
			String[] orgUnitCodes = messageReceiver.getOrgUnitCodes().split(",");
			String orgUnitNames = "";
			for (String code : orgUnitCodes) {
				orgUnitNames += orgUnitService.findUniqueByProperty("unitCode", code).getUnitName()+",";
			}
			if(ExStringUtils.isNotEmpty(orgUnitNames)) {
				orgUnitNames = orgUnitNames.substring(0, orgUnitNames.length()-1);
			}
			messageReceiver.setOrgUnitNames(orgUnitNames);
		}
		if(ExStringUtils.isNotEmpty(messageReceiver.getRoleCodes())){
			String[] roleCodes = messageReceiver.getRoleCodes().split(",");
			String roleNames = "";
			for (String code : roleCodes) {
				roleNames += roleService.getRoleByRolecode(code).getRoleName()+",";
			}
			if(ExStringUtils.isNotEmpty(roleNames)) {
				roleNames = roleNames.substring(0, roleNames.length()-1);
			}
			messageReceiver.setRoleNames(roleNames);
		}
	}
	
	/**
	 * 快捷发送消息弹出框
	 * @param resourceid
	 * @param request
	 * @param model
	 * @return
	 * @throws WebException
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping("/edu3/framework/message/dailog.html")
	public String editMessageDailog(String resourceid,HttpServletRequest request,ModelMap model) throws WebException, UnsupportedEncodingException{
		request.setCharacterEncoding("utf-8");
		String touser = ExStringUtils.trimToEmpty(request.getParameter("touser"));
		String tounit = ExStringUtils.trimToEmpty(request.getParameter("tounit"));
		String type = ExStringUtils.trimToEmpty(request.getParameter("type"));		
		String msgType = ExStringUtils.defaultIfEmpty(request.getParameter("msgType"), "usermsg");
		String msgContent = ExStringUtils.trimToEmpty(request.getParameter("msgContent"));
		String msgTitle = ExStringUtils.trimToEmpty(request.getParameter("msgTitle"));
		
		Message message = new Message();	
		message.setMsgType(msgType);
		message.setContent(msgContent);
		message.setMsgTitle(msgTitle);
		model.addAttribute("message", message);		
		
		MessageReceiver messageReceiver = new MessageReceiver();
	    if(ExStringUtils.isNotEmpty(touser)){
			User recievUser = userService.getUserByLoginId(touser);			
			
			messageReceiver.setUserCnNames(recievUser.getCnName()+",");
			messageReceiver.setUserNames(recievUser.getUsername()+",");	
		} else if(ExStringUtils.isNotEmpty(tounit)) {
			model.addAttribute("unitId",tounit);
			model.addAttribute("userType",CacheAppManager.getSysConfigurationByCode("sysuser.usertype.edumanager").getParamValue());
		} else if(ExStringUtils.isNotEmpty(type) && "batch".equalsIgnoreCase(type)) {
			String userNames = ExStringUtils.trimToEmpty(request.getParameter("userNames"));
			String cnNames = ExStringUtils.trimToEmpty(request.getParameter("cnNames"));
			cnNames = ExStringUtils.getEncodeURIComponentByOne(cnNames);
			messageReceiver.setUserCnNames(cnNames);
			messageReceiver.setUserNames(userNames);	
		}
		model.addAttribute("messageReceiver",messageReceiver);
		
		User cureentUser = SpringSecurityHelper.getCurrentUser();
		model.addAttribute("storeDir", cureentUser.getUsername());
		return "/edu3/portal/system/message/msg-dailog";
	}
	
	/**
	 * 保存消息
	 * @param resourceid
	 * @param message
	 * @param receiver
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/portal/message/saveMsg.html")
	public void saveMessage(String resourceid,Message message,MessageReceiver receiver,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		try {	
			do{
				if(ExStringUtils.isEmpty(message.getContent())){
					map.put("statusCode", 300);
					map.put("message", "内容不能为空！");
				}
				SensitivewordFilter sensitivewordFilter = (SensitivewordFilter)request.getSession().getServletContext().getAttribute("sensitivewordFilter");
				Set<String> sensitivewordSet = sensitivewordFilter.getSensitiveWord(ExStringUtils.trimToEmpty(message.getMsgTitle())+message.getContent(),SensitivewordFilter.maxMatchType);
				if(!CollectionUtils.isEmpty(sensitivewordSet)){
					map.put("statusCode", 300);
					map.put("message", "标题或内容含有敏感词："+sensitivewordSet+",请修改后再提交");
					continue;
				}
				
				receiver.setResourceid(null);
				User user = SpringSecurityHelper.getCurrentUser();
				String sendMode = ExStringUtils.defaultIfEmpty(request.getParameter("sendMode"), "0");
				if("0".equals(sendMode) ||message.getSendTime()==null){//即时发送
					message.setSendTime(new Date());
				}
				if(Constants.BOOLEAN_YES.equals(message.getIsDraft())){
					message.setSendTime(null);
				}
				String parentId = request.getParameter("parentId");
				if(ExStringUtils.isNotEmpty(parentId)){ //回复
					Message parentMessage = sysMsgService.get(parentId);//要回复的消息
					message.setParent(parentMessage);
				}
				if(ExStringUtils.isNotBlank(resourceid)){ //编辑或保存草稿
					MessageReceiver messageReceiver = messageReceiverService.findUniqueByProperty("message.resourceid", resourceid);
					if(messageReceiver!=null){
						Message _message = messageReceiver.getMessage();
						message.setSender(_message.getSender());
						message.setSenderName(_message.getSenderName());
						message.setOrgUnit(_message.getOrgUnit());
						receiver.setResourceid(messageReceiver.getResourceid());
						ExBeanUtils.copyProperties(messageReceiver, receiver);					
						if(message.getParent()!=null){//保存的是回复消息
							messageReceiver.setReceiveType("user");
							messageReceiver.setUserNames(message.getParent().getSender().getUsername());//接收人为父消息的发送人
						}
						ExBeanUtils.copyProperties(_message, message);
						messageReceiver.setMessage(_message);
						String[] uploadfileids = request.getParameterValues("uploadfileid");
						sysMsgService.updateMessage(_message, messageReceiver,uploadfileids);
					} 
				} else { //保存（发送信息或存为草稿）					
					message.setSender(user);
					message.setSenderName(user.getCnName());
					message.setOrgUnit(user.getOrgUnit());
					receiver.setMessage(message);
					
					if(message.getParent()!=null){//保存的是回复消息
						receiver.setReceiveType("user");
						receiver.setUserNames(message.getParent().getSender().getUsername());//接收人为父消息的发送人
					}
					String[] uploadfileids = request.getParameterValues("uploadfileid");
					sysMsgService.saveMessage(message, receiver,uploadfileids);
				}
				map.put("statusCode", 200);
				map.put("message", "发送成功！");
				//如果从dailog页面过来
				String from = ExStringUtils.trimToEmpty(request.getParameter("from"));
				if(!"dailog".equals(from)){
					if(Constants.BOOLEAN_NO.equals(message.getIsDraft())){
						map.put("reloadUrl", request.getContextPath()+"/edu3/portal/message/list.html?resourceid="+message.getResourceid()
								+"&fromtype="+ExStringUtils.trimToEmpty(request.getParameter("fromtype"))+"&type=sendbox");
					} else {
						map.put("navTabId", "RES_PERSON_SYSMSG_INPUT");
						map.put("reloadUrl", request.getContextPath()+"/edu3/portal/message/input.html?resourceid="+message.getResourceid()+"&fromtype="+ExStringUtils.trimToEmpty(request.getParameter("fromtype")));
					}
				}		
			} while(false);
			
		}catch (Exception e) {
			logger.error("保存消息出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "发送失败！");
		}		
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 组织单位
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/portal/message/org.html")
	public String orgUnitSelector(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{	
		try {
			String codesN = ExStringUtils.trimToEmpty(request.getParameter("codesN"));
			String namesN = ExStringUtils.trimToEmpty(request.getParameter("namesN"));
			String checkedCodes = ExStringUtils.trimToEmpty(request.getParameter("checkedCodes"));
		
			List<OrgUnit> orgList =  orgUnitService.findOrgTree(null);
		
			String jsonString = JsonUtils.objectToJson(TreeBuilder.buildTreeByCode(orgList,"UNIT_ROOT" ,checkedCodes));
			model.addAttribute("codesN",codesN);
			model.addAttribute("namesN",namesN);
			model.addAttribute("checkedCodes",checkedCodes);
			model.addAttribute("unitTree", jsonString);	
		} catch (Exception e) {
			logger.error("输出组织结构树出错:{}",e.fillInStackTrace());
		}			
		return "/edu3/portal/system/message/org-selector";
	}
	/**
	 * 角色
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/edu3/portal/message/role.html")
	public String roleSelector(HttpServletRequest request,HttpServletResponse response) throws Exception{
		String codesV = request.getParameter("codesV");
		codesV = codesV == null ? "" : codesV;
		List<Role> list = roleService.getAll();
		request.setAttribute("rolelist", list);
		request.setAttribute("codesV", codesV);
		return "/edu3/portal/system/message/role-selector";
	}
	/**
	 * 用户
	 * @param objPage
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/edu3/portal/message/user.html")
	public String userSelector(Page objPage,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws Exception{
		objPage.setOrderBy("unitId");
		objPage.setOrder(QueryParameter.ASC);//设置默认排序方式
		//查询条件
		Map<String,Object> condition = new HashMap<String,Object>();
		String userName=request.getParameter("userName");//登录名
		String cnName=request.getParameter("cnName");//姓名
		String unitId=request.getParameter("unitId");
		String userType=request.getParameter("userType");
		String usersN=request.getParameter("usersN");
		String namesN=request.getParameter("namesN");
		String sendto=request.getParameter("sendto");
		
		User user = SpringSecurityHelper.getCurrentUser();
		String student = CacheAppManager.getSysConfigurationByCode("sysuser.usertype.student").getParamValue();
		String unitType = CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue();
		
		if(ExStringUtils.isNotEmpty(userName)) {
			condition.put("userName", userName);
		}
		if(ExStringUtils.isNotEmpty(cnName)) {
			condition.put("cnName", cnName);
		}
		if(ExStringUtils.isNotEmpty(unitId)) {
			condition.put("unitId", unitId);
		}
		if(ExStringUtils.isNotEmpty(userType)) {
			condition.put("userType", userType);
		}
		if(ExStringUtils.isNotEmpty(sendto)) {
			condition.put("sendto", sendto);
		}
		if(ExStringUtils.isNotEmpty(usersN)) {
			condition.put("usersN", usersN);
		}
		if(ExStringUtils.isNotEmpty(namesN)) {
			condition.put("namesN", namesN);
		}
		if(student.equalsIgnoreCase(user.getUserType()) && user.getOrgUnit().getUnitType().indexOf(unitType)>=0){//如果为校外学习中心学生
			unitId = user.getUnitId();
			model.addAttribute("brschool_student", true);
			model.addAttribute("unitName", user.getOrgUnit().getUnitName());
		}
		if(user.getOrgUnit().getUnitType().indexOf(unitType)>=0){
			condition.put("unitId", user.getOrgUnit().getResourceid());
		}
		
		condition.put("isDeleted", 0);		
		
		Page page = userService.findUserByCondition(condition, objPage);
		
		model.addAttribute("userlist", page);
		model.addAttribute("usersN", usersN);
		model.addAttribute("namesN", namesN);
		model.addAttribute("condition", condition);
		return "/edu3/portal/system/message/user-selector";
	}
	/**
	 * 学生
	 * @param objPage
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/edu3/portal/message/student.html")
	public String studentSelector(Page objPage,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws Exception{
		objPage.setOrderBy("teachingPlanCourse,studentInfo.studyNo");
		objPage.setOrder(QueryParameter.ASC);//设置默认排序方式
		//查询条件
		Map<String,Object> condition = new HashMap<String,Object>();
		String name = ExStringUtils.trimToEmpty(request.getParameter("name"));//姓名
		String studyNo = ExStringUtils.trimToEmpty(request.getParameter("studyNo"));//学号
		String courseId = ExStringUtils.defaultIfEmpty(request.getParameter("courseId"), "");//课程	
		String branchSchool = request.getParameter("branchSchool");//学习中心	
		String major = request.getParameter("major");//专业
		String classic = request.getParameter("classic");//层次	
		String status = ExStringUtils.defaultIfEmpty(request.getParameter("status"), "1");	
		
		if(ExStringUtils.isNotBlank(name)) {
			condition.put("name", name);
		}
		if(ExStringUtils.isNotEmpty(studyNo)) {
			condition.put("studyNo", studyNo);
		}
		if(ExStringUtils.isNotEmpty(branchSchool)) {
			condition.put("branchSchool", branchSchool);
		}
		if(ExStringUtils.isNotEmpty(major)) {
			condition.put("major", major);
		}
		if(ExStringUtils.isNotEmpty(classic)) {
			condition.put("classic", classic);
		}
		
		User user = SpringSecurityHelper.getCurrentUser();
		String roleCode = CacheAppManager.getSysConfigurationByCode("sysuser.role.teacher").getParamValue();//获取角色编码
		if(SpringSecurityHelper.isUserInRole(roleCode)){
//			TeachTask teachTask = teachtaskservice.findLastTeachTaskByTeacherId(user.getResourceid());
//			if(teachTask!=null){
//				if(ExStringUtils.isEmpty(courseId)) {
//					courseId = teachTask.getCourse().getResourceid();
//				}				
				condition.put("teacherId", user.getResourceid());
//			}
		}
		condition.put("courseId", courseId);
		condition.put("status", Integer.parseInt(status));
		
		Page page = studentLearnPlanService.findStudentLearnPlanByCondition(condition, objPage);
				
		String usersN=request.getParameter("usersN");
		String namesN=request.getParameter("namesN");		
		if(ExStringUtils.isNotEmpty(usersN)) {
			condition.put("usersN", usersN);
		}
		if(ExStringUtils.isNotEmpty(namesN)) {
			condition.put("namesN", namesN);
		}
		
		model.addAttribute("studentlist", page);
		model.addAttribute("condition", condition);
		return "/edu3/portal/system/message/student-selector";
	}
	
	/**
	 * 查看消息
	 * @param msgId
	 * @param type
	 * @param request
	 * @param model
	 * @return
	 * @throws WebException
	 */	
	@RequestMapping("/edu3/framework/message/show.html")
	public String viewMessage(String msgId,String type,HttpServletRequest request,Page objPage, ModelMap model) throws WebException{			
		try {
			if(ExStringUtils.isNotEmpty(msgId)){
//				MessageSender messageSender = messageSenderService.findUniqueByProperty("message.resourceid", msgId);	
				MessageReceiver messageReceiver = messageReceiverService.findUniqueByProperty("message.resourceid", msgId); 
				if(messageReceiver!=null) {
					List<Attachs> attachs = attachsService.findAttachsByFormId(msgId);
					messageReceiver.getMessage().setAttachs(attachs);
					codesToNames(messageReceiver);
				}
				User user = SpringSecurityHelper.getCurrentUser();
				if("inbox".equalsIgnoreCase(type)){//收件箱	
					Map<String, Object> condition = new HashMap<String, Object>();
					condition.put("userId", user.getResourceid());
					condition.put("userName", user.getUsername());
					condition.put("messageId", msgId);
					List<MessageReceiverUser> messageReceiverUserList = messageReceiverUserService.findByCondition(condition);
					if(ExCollectionUtils.isNotEmpty(messageReceiverUserList)){
						MessageReceiverUser messageReceiverUser = messageReceiverUserList.get(0);
						messageReceiverUser.setReadStatus("readed");
						messageReceiverUser.setReadTime(new Date());
						messageReceiverUserService.saveOrUpdate(messageReceiverUser);
					}
				} else {
					Map<String,Object> _condition = new HashMap<String,Object>();//查询条件	
					_condition.put("messageId", msgId);
					objPage.setOrderBy("readStatus,mru.readTime ");
					objPage.setOrder(QueryParameter.DESC);
					Page messageReceiverUserPage = messageReceiverUserService.findByCondition(_condition, objPage);
					model.addAttribute("messageStats", messageReceiverUserPage);				
					model.addAttribute("condition", _condition);
				}
				model.addAttribute("messageReceiver", messageReceiver);
				model.addAttribute("type", type);
			}
		} catch (Exception e) {
			logger.error("查看消息出错", e);
		}
		return "/edu3/portal/system/message/msg-view";
	}
	/**
	 * 删除消息
	 * @param type
	 * @param resourceid
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/portal/message/remove.html")
	public void removeMessage(String type,String resourceid,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		try {			
			User user = SpringSecurityHelper.getCurrentUser();
			if(ExStringUtils.isNotBlank(resourceid)){			
				sysMsgService.deleteMessage(type,resourceid.split("\\,"),user);
				map.put("statusCode", 200);
				map.put("message", "删除成功！");	
				map.put("callbackType", "forward");
				map.put("forwardUrl", request.getContextPath()+"/edu3/portal/message/list.html?fromtype="+ExStringUtils.trimToEmpty(request.getParameter("fromtype"))+"&type="+type);
			}
		} catch (Exception e) {
			logger.error("删除消息出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 撤销消息
	 * @param type
	 * @param resourceid
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/portal/message/revoke.html")
	public void revokeMessage(String type,String resourceid,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		int statusCode = 200;
		String message = "撤销成功！";
		try {		
			do{
				if(type!=null && !"sendbox".equals(type) ){// 只有发件箱才能撤销
					statusCode = 300;
					message = "只能撤销发送消息！";
					continue;
				}
				if(ExStringUtils.isNotBlank(resourceid)){		
					// TODO:以后可能加入时间控制，如：超过多久就不能撤销消息
					sysMsgService.revokeMessage(	resourceid.split("\\,"));
					map.put("callbackType", "forward");
					map.put("forwardUrl", request.getContextPath()+"/edu3/portal/message/list.html?fromtype="+ExStringUtils.trimToEmpty(request.getParameter("fromtype"))+"&type="+type);
				}
			} while(false);
		} catch (Exception e) {
			logger.error("撤销消息出错:",e);
			statusCode = 300;
			message = "撤销出错！";
		} finally {
			map.put("statusCode", statusCode);
			map.put("message", message);	
			map.put("callbackType", "forward");
			map.put("forwardUrl", request.getContextPath()+"/edu3/portal/message/list.html?fromtype="+ExStringUtils.trimToEmpty(request.getParameter("fromtype"))+"&type="+type);
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 接收者：将编码转换为名称（供页面显示）
	 * 里面的接收者编码有多个则用","分隔
	 * @param messageReceiver 
	 * @return
	 */
	private void codesToNames(MessageReceiver messageReceiver) {
		if("user".equals(messageReceiver.getReceiveType())){
			messageReceiver.setUserCnNames(userService.findNamesByAccounts(messageReceiver.getUserNames())+",");
		}
		if("org".equals(messageReceiver.getReceiveType())){
			messageReceiver.setOrgUnitNames(orgUnitService.findNamesByCodes(messageReceiver.getOrgUnitCodes())+",");
		}
		if("role".equals(messageReceiver.getReceiveType())){
			messageReceiver.setRoleNames(roleService.findNamesByCodes(messageReceiver.getRoleCodes())+",");
		}
		if("grade".equals(messageReceiver.getReceiveType())){
			messageReceiver.setGradeNames(gradeService.findNamesByIds(messageReceiver.getGrades())+",");
		}
		if("classes".equals(messageReceiver.getReceiveType())){
			messageReceiver.setClassesNames(classesService.findNamesByIds(messageReceiver.getClasses())+",");
		}
	}
	
	/**
	 * 年级
	 * @param objPage
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/edu3/portal/message/grade.html")
	public String gradeSelector(Page objPage,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws Exception{
		objPage.setOrderBy("yearInfo.firstYear desc,term desc,resourceid");
		objPage.setOrder(Page.DESC);//设置默认排序方式
		//查询条件
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件
		String gradeName=request.getParameter("gradeName");
		if(ExStringUtils.isNotEmpty(gradeName)) {
			condition.put("gradeName", gradeName);
		}
		
		Page page = gradeService.findGradeByCondition(condition, objPage);
		
		String codesN=request.getParameter("codesN");
		String namesN=request.getParameter("namesN");		
		if(ExStringUtils.isNotEmpty(codesN)) {
			condition.put("codesN", codesN);
		}
		if(ExStringUtils.isNotEmpty(namesN)) {
			condition.put("namesN", namesN);
		}
		model.addAttribute("gradeList", page);
		model.addAttribute("condition", condition);
		return "/edu3/portal/system/message/grade-selector";
	}
	
	/**
	 * 班级
	 * @param objPage
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/edu3/portal/message/classes.html")
	public String classesSelector(Page objPage,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws Exception{
		objPage.setOrderBy("grade.gradeName desc,brSchool.unitCode,major.majorCode,classic,classname,resourceid");
		objPage.setOrder(Page.DESC);
		//查询条件
		Map<String, Object> condition = new HashMap<String, Object>();
		String classname=request.getParameter("classname");
		String gradeid=request.getParameter("gradeid");
		String brSchoolid=request.getParameter("brSchoolid");
		String majorid=request.getParameter("majorid");
		String classicid=request.getParameter("classicid");
		String teachingType=request.getParameter("teachingType");
		
		if(ExStringUtils.isNotBlank(classname)) {
			condition.put("classname", ExStringUtils.trimToEmpty(classname));
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
		Page classesPage = classesService.findClassesByCondition(condition, objPage);
		
		String codesN=request.getParameter("codesN");
		String namesN=request.getParameter("namesN");		
		if(ExStringUtils.isNotEmpty(codesN)) {
			condition.put("codesN", codesN);
		}
		if(ExStringUtils.isNotEmpty(namesN)) {
			condition.put("namesN", namesN);
		}
		
		List<Major> majors = majorService.findByHql(" from "+Major.class.getSimpleName()+" where isDeleted= 0 order by majorCode asc ");
		StringBuffer majorOption = new StringBuffer("<option value=''></option>");
		
		for (Major m : majors) {
			if(ExStringUtils.isNotEmpty(majorid)&&majorid.equals(m.getResourceid())){
				majorOption.append("<option value ='"+m.getResourceid()+"' selected='selected' >"+m.getMajorCode()+"-"+m.getMajorName()+"</option>");
			}else{
				majorOption.append("<option value ='"+m.getResourceid()+"'>"+m.getMajorCode()+"-"+m.getMajorName()+"</option>");
			}
			
		}
		model.addAttribute("majorOption", majorOption);
		model.addAttribute("classesPage", classesPage);
		model.addAttribute("condition", condition);
		return "/edu3/portal/system/message/classes-selector";
	}
}
