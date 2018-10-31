package com.hnjk.edu.learning.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
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
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.BaseSupportController;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.learning.model.BbsReply;
import com.hnjk.edu.learning.model.BbsSection;
import com.hnjk.edu.learning.model.BbsTopic;
import com.hnjk.edu.learning.model.BbsUserInfo;
import com.hnjk.edu.learning.service.IActiveCourseExamService;
import com.hnjk.edu.learning.service.IBbsGroupUsersService;
import com.hnjk.edu.learning.service.IBbsReplyService;
import com.hnjk.edu.learning.service.IBbsSectionService;
import com.hnjk.edu.learning.service.IBbsTopicService;
import com.hnjk.edu.learning.service.IBbsUserInfoService;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.roll.service.IStudentInfoService;
import com.hnjk.edu.teaching.service.ICourseService;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.Attachs;
import com.hnjk.platform.system.service.IAttachsService;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;
import com.hnjk.security.model.UserExtends;
/**
 * 论坛管理
 * <code>BbsController</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-9-21 上午10:22:00
 * @see 
 * @version 1.0
 */
@Controller
public class BbsController extends BaseSupportController {

	private static final long serialVersionUID = 997631923140343018L;
	
	@Autowired
	@Qualifier("bbsTopicService")
	private IBbsTopicService bbsTopicService;
	
	@Autowired
	@Qualifier("bbsReplyService")
	private IBbsReplyService bbsReplyService;

	@Autowired
	@Qualifier("bbsSectionService")
	private IBbsSectionService bbsSectionService;
	
	@Autowired
	@Qualifier("courseService")
	private ICourseService courseService;
	
	@Autowired
	@Qualifier("attachsService")
	private IAttachsService attachsService;
	
	@Autowired
	@Qualifier("studentinfoservice")
	private IStudentInfoService studentInfoService;
	
	@Autowired
	@Qualifier("bbsGroupUsersService")
	private IBbsGroupUsersService bbsGroupUsersService;
	
	
	@Autowired
	@Qualifier("activeCourseExamService")
	private IActiveCourseExamService activeCourseExamService;
	
	@Autowired
	@Qualifier("bbsUserInfoService")
	private IBbsUserInfoService bbsUserInfoService;
	
	/**
	 * 进入课程论坛
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/framework/bbs/entry.html")
	public String listCourseBbs(HttpServletRequest request,Page objPage, ModelMap model) throws WebException{
		objPage.setOrderBy("courseCode");
		objPage.setOrder(Page.ASC);//设置默认排序方式		
		String courseId = request.getParameter("courseId");
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件
		User user = SpringSecurityHelper.getCurrentUser();
		try {			
			String teacherId = "";
			if(SpringSecurityHelper.isUserInRole(CacheAppManager.getSysConfigurationByCode("sysuser.role.teacher").getParamValue())){
				teacherId = user.getResourceid();	
			}
			if(ExStringUtils.isNotBlank(teacherId)){
				condition.put("teacherId",teacherId);
			}
			List<Map<String, Object>> list = activeCourseExamService.getTeacherOnlineCourse(condition);
			StringBuffer SBFcourse = new StringBuffer("<select class='flexselect' id='bbsentryCourseId' name='courseId' >");
			SBFcourse.append("<option value=''>请选择</option>");
			if(ExCollectionUtils.isNotEmpty(list) && list.size() > 0){
				for(Map<String, Object> map : list){
					if(map.get("courseid").equals(courseId)){
						SBFcourse.append("<option value='"+map.get("courseid")+"' selected='selected'>"+map.get("coursename")+"</option>");
					}else{
						SBFcourse.append("<option value='"+map.get("courseid")+"'>"+map.get("coursename")+"</option>");
					}
				}
			}
			SBFcourse.append("</select>");
			model.put("bbsentryCourseSelect", SBFcourse.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		if(ExStringUtils.isNotEmpty(courseId)) {
			condition.put("courseId", courseId);
		}
				
		Page courseListPage = courseService.findCourseByHql(condition, objPage);
		
		model.addAttribute("courseListPage", courseListPage);
		model.addAttribute("condition", condition);
		
		return "/edu3/learning/bbssection/bbs-entry";
	}
	
	/**
	 * 进入论坛主页
	 * @param request
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping(value={"/edu3/learning/bbs/index.html","/edu3/course/bbs/index.html"})
	public String enterBbs(HttpServletRequest request, ModelMap model) throws WebException {
		User user = SpringSecurityHelper.getCurrentUser();
		if(user != null){
			model.addAttribute("user", user);
		}
		String courseId = request.getParameter("courseId");
		String hql = " from "+BbsSection.class.getSimpleName()+" where isDeleted=0 and parent is null ";
		if(ExStringUtils.isNotEmpty(courseId)){
			hql += " and isCourseSection = 'Y' ";
			model.addAttribute("course", courseService.get(courseId));			
		} else {
			hql += " and isCourseSection = 'N' ";
		}
		List<BbsSection> parentBbsSections = bbsSectionService.findByHql(hql);//加载一级版块
		
		for (BbsSection section : parentBbsSections) {
			for (BbsSection child : section.getChilds()) {
				if(ExStringUtils.isNotEmpty(courseId)){
					child.setTodayCount(bbsSectionService.statTodayTopicCount(child.getResourceid(),courseId));	//今日贴
					child.setStatTopicCount(bbsSectionService.statTopicCount(child.getResourceid(),courseId));	//主题
					child.setInvitationCount(bbsSectionService.statInvitationCount(child.getResourceid(),courseId));//帖子	
				} else {
					if(!ExDateUtils.isSameDay(child.getTodayTopicDate(), new Date())){
						child.setTodayCount(0);
					} else {
						child.setTodayCount(child.getTodayTopicCount());
					}					
					child.setStatTopicCount(child.getTopicCount());
					child.setInvitationCount(child.getTopicAndReplyCount());
				}
			}
		}
		
		model.addAttribute("parentBbsSections", parentBbsSections);			
		return "/edu3/learning/bbssection/bbs-index";
	}
	
	/**
	 * 异步获取今日贴，热门帖和最新主题
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping(value={"/edu3/learning/bbs/topic/ajax.html","/edu3/course/bbs/topic/ajax.html"})
	public void ajaxGetDigestAndNewTopic(HttpServletRequest request,HttpServletResponse response) throws WebException{
		String courseId = request.getParameter("courseId");
		//热门帖子
		Page hotPage = new Page();
		hotPage.setPageSize(10);	
		hotPage.setAutoCount(false);
		hotPage.setOrderBy("replyCount");
		hotPage.setOrder(Page.DESC);//设置默认排序方式	
		Map<String,Object> condition1 = new HashMap<String,Object>();						
		condition1.put("replyCount", 10);	
		if(ExStringUtils.isNotEmpty(courseId)) {
			condition1.put("courseId", courseId);
		} else {
			condition1.put("emptyCourse", "Y");
		}
		Page hotTopics = bbsTopicService.findBbsTopicByCondition(condition1, hotPage);		
		//最新帖子
		Page newPage = new Page();
		newPage.setPageSize(10);
		newPage.setAutoCount(false);
		newPage.setOrderBy("fillinDate");
		newPage.setOrder(Page.DESC);//设置默认排序方式
		Map<String,Object> condition2 = new HashMap<String,Object>();	
		condition2.put("fillinDateStart", ExDateUtils.addDays(ExDateUtils.getToday(), -3).getTime());
		if(ExStringUtils.isNotEmpty(courseId)) {
			condition2.put("courseId", courseId);
		} else {
			condition2.put("emptyCourse", "Y");
		}
		condition2.put("parenTopic", "null");
		Page newTopics = bbsTopicService.findBbsTopicByCondition(condition2, newPage);	
		//精华帖子
		Page digestPage = new Page();
		digestPage.setPageSize(10);
		digestPage.setAutoCount(false);
		digestPage.setOrderBy("fillinDate");
		digestPage.setOrder(Page.DESC);//设置默认排序方式
		Map<String,Object> condition3 = new HashMap<String,Object>();	
		condition3.put("status", 1);
		if(ExStringUtils.isNotEmpty(courseId)) {
			condition3.put("courseId", courseId);
		} else {
			condition3.put("emptyCourse", "Y");
		}
		Page digestTopics = bbsTopicService.findBbsTopicByCondition(condition3, digestPage);	
		
		Map<String, Object> results = new HashMap<String, Object>();
		List<Map<String, String>> list1 =  getTopicJsonList(hotTopics);
		List<Map<String, String>> list2 =  getTopicJsonList(newTopics);
		List<Map<String, String>> list3 =  getTopicJsonList(digestTopics);
		results.put("hotTopics", list1);
		results.put("newTopics", list2);
		results.put("digestTopics", list3);
		renderJson(response, JsonUtils.mapToJson(results));
	}
	
	private List<Map<String, String>> getTopicJsonList(Page result){
		List<Map<String, String>> list = new ArrayList<Map<String,String>>(0);
		for (Object o : result.getResult()) {
			BbsTopic topic = (BbsTopic)o; 
			Map<String, String> map = new HashMap<String, String>();
			map.put("resourceid", topic.getResourceid());
			String title = topic.getTitle();
			if(title.length()>10){
				title = title.substring(0,10);
			}
			map.put("title", title);
			map.put("cnname", topic.getBbsUserInfo().getSysUser().getCnName());
			if(topic.getBbsGroup()!=null){
				map.put("groupName", topic.getBbsGroup().getGroupName());
			}
			
			list.add(map);
		}
		return list;
	}
	/**
	 * 版块帖子列表
	 * @param request
	 * @param model
	 * @param objPage
	 * @return
	 * @throws WebException
	 */
	@RequestMapping(value={"/edu3/learning/bbs/section.html","/edu3/course/bbs/section.html"})
	public String enterBbsSection(HttpServletRequest request, ModelMap model,Page objPage) throws WebException {
		objPage.setOrderBy("lastReplyDate");
		objPage.setOrder(Page.DESC);//设置默认排序方式	
		objPage.setPageSize(30); //每页30帖子
		
		User user = SpringSecurityHelper.getCurrentUser();
		if(user != null) {
			model.addAttribute("user", user);
		}
		
		String sectionId = request.getParameter("sectionId");
		String courseId = request.getParameter("courseId");
		String search = ExStringUtils.defaultIfEmpty(request.getParameter("search"), "");
		
		Map<String,Object> condition = new HashMap<String,Object>();
		Map<String,Object> condition_query = new HashMap<String,Object>();
		condition.put("topLevel", 0);
		boolean isCouseSection = false;
		if(ExStringUtils.isNotEmpty(sectionId)){
			condition.put("bbsSectionId", sectionId);
			condition_query.put("sectionId", sectionId);
			BbsSection section = bbsSectionService.get(sectionId);
			if(Constants.BOOLEAN_YES.equalsIgnoreCase(section.getIsCourseSection())){//是课程论坛版块
				isCouseSection = true;
			}
		}
		if(ExStringUtils.isNotEmpty(courseId)){
			condition.put("courseId", courseId);
			condition_query.put("courseId", courseId);
			model.addAttribute("course", courseService.get(courseId));			
		} else {
			if(isCouseSection){
				model.addAttribute("errorMsg", "链接出错!");
				return "/edu3/learning/bbssection/bbs-error";
			}
		}
		
		if("digest".equals(search)){//精华贴
			condition.put("status", 1);
			condition_query.put("search", search);
		}
		
		condition.put("parenTopic", "null");		
		
		BbsSection bbsSection = bbsSectionService.get(sectionId);
//		bbsSection.setClickCount(bbsSection.getClickCount()+1);//更新点击量
//		bbsSectionService.update(bbsSection);
		List<BbsSection> bbsSections = new ArrayList<BbsSection>();
		if(bbsSection != null){
			for (BbsSection child : bbsSection.getChilds()) {
				if(ExStringUtils.isNotBlank(courseId)){
					child.setTodayCount(bbsSectionService.statTodayTopicCount(child.getResourceid(),courseId));	
					child.setStatTopicCount(bbsSectionService.statTopicCount(child.getResourceid(),courseId));	
					child.setInvitationCount(bbsSectionService.statInvitationCount(child.getResourceid(),courseId));
				} else {
					if(!ExDateUtils.isSameDay(child.getTodayTopicDate(), new Date())){
						child.setTodayCount(0);
					} else {
						child.setTodayCount(child.getTodayTopicCount());
					}
					child.setStatTopicCount(child.getTopicCount());
					child.setInvitationCount(child.getTopicAndReplyCount());
				}				
			}
			//获取父版块列表,导航
			bbsSections = getParentSections(bbsSection);
		}
		model.addAttribute("bbsSection", bbsSection);
		model.addAttribute("bbsSections", bbsSections);
		
		Page bbsTopicPage = bbsTopicService.findBbsTopicByCondition(condition, objPage);
		if(objPage.getPageNum()==1){ //第一页查询固顶帖
			List<Criterion> criterions = new ArrayList<Criterion>();			
			criterions.add(Restrictions.eq("bbsSection.resourceid", sectionId));
			criterions.add(Restrictions.gt("topLevel", 0));
			if(ExStringUtils.isNotEmpty(courseId)) {
				criterions.add(Restrictions.eq("course.resourceid", courseId));
			}
			List topTopics = bbsTopicService.findByCriteria(BbsTopic.class, criterions.toArray(new Criterion[criterions.size()]),Order.desc("topLevel"));
			if(topTopics!=null){
				if(objPage.getResult()!=null) {
					topTopics.addAll(objPage.getResult());
				}
				objPage.setResult(topTopics);
			}			
		}
		String sectionCode = CacheAppManager.getSysConfigurationByCode("web.bbs.sectioncode.group").getParamValue();//获取版块编码
		String questionSectionCode = CacheAppManager.getSysConfigurationByCode("web.bbs.sectioncode").getParamValue();
		model.addAttribute("groupSectionCode", sectionCode);
		model.addAttribute("questionSectionCode", questionSectionCode);
			
		model.addAttribute("bbsTopicPage", bbsTopicPage);
		model.addAttribute("condition", condition_query);	
		
		return "/edu3/learning/bbssection/bbs-section";
	}
	//获取父版块列表
	private List<BbsSection> getParentSections(BbsSection bbsSection) {
		List<BbsSection> bbsSections = new ArrayList<BbsSection>();
		BbsSection section = bbsSection;
		do {
			bbsSections.add(section);
			section = section.getParent();
		} while (section!=null);
		Collections.reverse(bbsSections);
		return bbsSections;
	}
	/**
	 * 点击率更新
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping(value={"/edu3/learning/bbs/topic/update/ajax.html","/edu3/course/bbs/topic/update/ajax.html"})
	public void ajaxUpdateCount(HttpServletRequest request,HttpServletResponse response) throws WebException{
		String type = ExStringUtils.trimToEmpty(request.getParameter("type"));//section,topic
		if("section".equalsIgnoreCase(type)){//版块更新
			String sectionId = request.getParameter("sectionId");
			if(ExStringUtils.isNotBlank(sectionId)){
				BbsSection bbsSection = bbsSectionService.get(sectionId);
				bbsSection.setClickCount(bbsSection.getClickCount()+1);//更新点击量
				bbsSectionService.update(bbsSection);
			}			
		} else if("topic".equalsIgnoreCase(type)){
			String topicId = request.getParameter("topicId");
			if(ExStringUtils.isNotBlank(topicId)){
				BbsTopic bbsTopic = bbsTopicService.get(topicId);
				bbsTopic.setClickCount(bbsTopic.getClickCount()+1);//更新点击量
				
				bbsTopicService.update(bbsTopic);
			}
		}
		
	}
	
	/**
	 * 帖子回复列表
	 * @param request
	 * @param model
	 * @param objPage
	 * @return
	 * @throws WebException
	 */
	@RequestMapping(value={"/edu3/learning/bbs/topic.html","/edu3/course/bbs/topic.html"})
	public String enterBbsTopic(HttpServletRequest request, ModelMap model,Page objPage) throws WebException {
		objPage.setOrderBy("showOrder");
		objPage.setOrder(Page.ASC);//设置默认排序方式	
		objPage.setPageSize(30); //每页30帖子	
		
		User user = SpringSecurityHelper.getCurrentUser();
		if(user != null){
			model.addAttribute("storeDir", user.getUsername());	
			model.addAttribute("user", user);
		}
		
		String topicId = ExStringUtils.trimToEmpty(request.getParameter("topicId"));
		String courseId = ExStringUtils.trimToEmpty(request.getParameter("courseId"));		
		
		Map<String,Object> condition = new HashMap<String,Object>();
		Map<String,Object> condition_query = new HashMap<String,Object>();
		
		BbsTopic bbsTopic = null;
		
		if(ExStringUtils.isNotEmpty(topicId)){
			condition.put("bbsTopicId", topicId);
			condition_query.put("topicId", topicId);
			bbsTopic = bbsTopicService.get(topicId);
			if(ExStringUtils.isEmpty(courseId) && null!=bbsTopic.getCourse()){
				courseId = bbsTopic.getCourse().getResourceid();
			}
		}		
		if(ExStringUtils.isNotEmpty(courseId)){
			condition_query.put("courseId", courseId);
			model.addAttribute("course", courseService.get(courseId));			
		}
		if(null==bbsTopic){
			model.addAttribute("condition", condition_query);
			model.addAttribute("errorMsg", "该帖子不存在或已删除");
			return "/edu3/learning/bbssection/bbs-error";
		}	
		
		BbsTopic parentTopic = bbsTopic.getParenTopic();
		if(parentTopic!=null){	//小组话题		
			if(-1!=parentTopic.getStatus()&&ExDateUtils.getTodayEnd().after(parentTopic.getEndTime())){//已经过了讨论截止时间
				parentTopic.setStatus(-1);//锁帖
				for (BbsTopic t : parentTopic.getChilds()) {
					t.setStatus(-1);
				}
				//bbsTopicService.update(parentTopic);
			}	
			String userType = CacheAppManager.getSysConfigurationByCode("sysuser.usertype.student").getParamValue();
			String stuRole = CacheAppManager.getSysConfigurationByCode("sysuser.role.student").getParamValue();
			if(user != null && (SpringSecurityHelper.isUserInRole(stuRole)||userType.equals(user.getUserType()))){ //学生
				if("1".equals(parentTopic.getViewPermiss())){//小组可见
					if(!bbsGroupUsersService.isUserInGroup(user.getResourceid(),bbsTopic.getBbsGroup().getResourceid())){//当前用户不是小组成员，不可以浏览该帖
						model.addAttribute("errorMsg", "您不是该小组成员，没有权限浏览该帖!");
						return "/edu3/learning/bbssection/bbs-error";
					} 
				} else { //全部可见
					boolean hasViewPermiss = false;
					for (BbsTopic topic : parentTopic.getChilds()) {
						if(bbsGroupUsersService.isUserInGroup(user.getResourceid(),topic.getBbsGroup().getResourceid())){
							hasViewPermiss = true;
							break;
						}
					}
					if(!hasViewPermiss){
						model.addAttribute("errorMsg", "您没有加入小组或您的小组没有权限浏览该帖");
						return "/edu3/learning/bbssection/bbs-error";
					}		
				}
			} else { //其他用户
				if(user==null ||(//!teachTaskService.isCourseTeacher(bbsTopic.getCourse().getResourceid(), user.getResourceid(), 0)&&
						!SpringSecurityHelper.isUserInRole("ROLE_ADMIN"))){//不是老师或管理员
					model.addAttribute("errorMsg", "您没有权限浏览该帖");
					return "/edu3/learning/bbssection/bbs-error";
				}
			}
						
		} 		
		
		List<BbsSection> bbsSections = new ArrayList<BbsSection>();
		if(bbsTopic.getBbsSection()!=null){
			bbsSections = getParentSections(bbsTopic.getBbsSection());//导航
		}
		model.addAttribute("bbsSections", bbsSections);
		
		BbsUserInfo topicUserInfo = bbsTopic.getBbsUserInfo();
		if(topicUserInfo != null){
			User topicUser = topicUserInfo.getSysUser();				
			String userface = getUserFacePath(request, topicUser);	
			topicUserInfo.setUserface(userface);
			String roleCode = CacheAppManager.getSysConfigurationByCode("sysuser.role.student").getParamValue();
			if(topicUser.isContainRole(roleCode)){//学生	
				StudentInfo studentInfo = studentInfoService.getStudentInfoByUser(topicUser);
				topicUserInfo.setStudentInfo(studentInfo);
			}
		} 	
		if(!Constants.BOOLEAN_NO.equalsIgnoreCase(bbsTopic.getIsAttachs())){
			List<Attachs> topicAttachs = attachsService.findAttachsByFormId(bbsTopic.getResourceid());
			bbsTopic.setAttachs(topicAttachs);
		}
		model.addAttribute("bbsTopic", bbsTopic);		
				
		Page bbsReplyPage = bbsReplyService.findBbsReplyByCondition(condition, objPage);
		if(null!=bbsReplyPage.getResult()){
			for (Object reply : bbsReplyPage.getResult()) {
				BbsReply bbsReply = (BbsReply) reply;
				//加载论坛用户信息
				BbsUserInfo bbsUserInfo = bbsReply.getBbsUserInfo();
				if(bbsUserInfo != null){
					User replyUser = bbsUserInfo.getSysUser();				
					String userface = getUserFacePath(request, replyUser);	
					bbsUserInfo.setUserface(userface);
					String roleCode = CacheAppManager.getSysConfigurationByCode("sysuser.role.student").getParamValue();
					if(replyUser.isContainRole(roleCode)){//学生	
						StudentInfo studentInfo = studentInfoService.getStudentInfoByUser(replyUser);
						bbsUserInfo.setStudentInfo(studentInfo);
					}
				}
				//加载附件
				if(!Constants.BOOLEAN_NO.equalsIgnoreCase(bbsReply.getIsAttachs())){
					List<Attachs> attachs = attachsService.findAttachsByFormId(bbsReply.getResourceid());
					bbsReply.setAttachs(attachs);
				}
			}
		}
		model.addAttribute("bbsReplyPage", bbsReplyPage);
		model.addAttribute("condition", condition_query);
		
		String questionSectionCode = CacheAppManager.getSysConfigurationByCode("web.bbs.sectioncode").getParamValue();
		model.addAttribute("questionSectionCode", questionSectionCode);
		
		return "/edu3/learning/bbssection/bbs-topic";
	}
	//获取用户自定义图像路径
	private String getUserFacePath(HttpServletRequest request, User user) {
		String userface = "";
		if(null != user.getUserExtends() && null != user.getPropertys(UserExtends.USER_EXTENDCODE_FACE)){
			userface = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()
					+ CacheAppManager.getSysConfigurationByCode("web.uploadfile.rooturl").getParamValue()
					+ "users/" + user.getUsername() + "/" + user.getPropertys(UserExtends.USER_EXTENDCODE_FACE).getExValue();
		}
		return userface;
	}
	/**
	 * 新增编辑回复
	 * @param request
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/learning/bbs/reply.html")
	public String enterNewReply(HttpServletRequest request, ModelMap model) throws WebException {
		String topicId = request.getParameter("topicId");
		String replyId = request.getParameter("replyId");//编辑
		String quoteId = request.getParameter("quoteId");//引用
		if(ExStringUtils.isNotEmpty(replyId)){      //编辑
			BbsReply bbsReply = bbsReplyService.get(replyId);
			List<Attachs> attachs = attachsService.findAttachsByFormId(replyId);
			bbsReply.setAttachs(attachs);
			model.addAttribute("bbsReply", bbsReply);
			List<BbsSection> bbsSections = getParentSections(bbsReply.getBbsTopic().getBbsSection());
			model.addAttribute("bbsSections", bbsSections);
			model.addAttribute("bbsTopic", bbsReply.getBbsTopic());
		} else {
			BbsTopic bbsTopic = bbsTopicService.get(topicId);
			if(ExStringUtils.isNotEmpty(quoteId)){//引用
				BbsReply quoteReply = bbsReplyService.get(quoteId);
				model.addAttribute("quoteReply", quoteReply);
			}						
			List<BbsSection> bbsSections = getParentSections(bbsTopic.getBbsSection());
			model.addAttribute("bbsSections", bbsSections);
			model.addAttribute("bbsTopic", bbsTopic);
		}		
		
		User user = SpringSecurityHelper.getCurrentUser();
		if(user != null){
			model.addAttribute("storeDir", user.getUsername());	
			model.addAttribute("user", user);
		}
		String pageNum = request.getParameter("pageNum");
		model.addAttribute("pageNum", pageNum);
		String courseId = request.getParameter("courseId");
		if(ExStringUtils.isNotEmpty(courseId)){
			model.addAttribute("course", courseService.get(courseId));			
		}
		return "/edu3/learning/bbssection/bbs-reply";
	}
	/**
	 * 论坛回复
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/learning/bbs/reply/save.html")
	public void saveBbsReply(HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		User user =SpringSecurityHelper.getCurrentUser();
		try {
			String bbsTopicId = request.getParameter("bbsTopicId");
			String bbsReplyId = request.getParameter("bbsReplyId");
			String replyContent = request.getParameter("replyContent");
			String[] uploadfileids = request.getParameterValues("uploadfileid");
			
			BbsReply bbsReply = null;	
			if(ExStringUtils.isNotEmpty(bbsReplyId)){   //编辑
				bbsReply = bbsReplyService.get(bbsReplyId);
				bbsReply.setReplyContent(replyContent);	
				bbsReply.setIsPersisted(true);
			} else {
				bbsReply = new BbsReply();	
				BbsTopic bbsTopic = bbsTopicService.get(bbsTopicId);
				
				bbsReply.setReplyContent(replyContent);
				bbsReply.setReplyDate(new Date());
				bbsReply.setReplyMan(user.getCnName());	
				bbsReply.setShowOrder(bbsReplyService.getNextShowOrder(bbsTopicId));//排序号
				bbsTopic.setLastReplyDate(bbsReply.getReplyDate());				
				bbsTopic.setLastReplyMan(bbsReply.getReplyMan());
				if(bbsTopic.getReplyCount()==null) {
					bbsTopic.setReplyCount(0);
				}
				bbsTopic.setReplyCount(bbsTopic.getReplyCount().intValue()+1);
				bbsReply.setBbsTopic(bbsTopic);	
			}
			bbsReplyService.saveOrUpdateBbsReply(bbsReply, uploadfileids);
			map.put("statusCode", 200);
			String pageNum = ExStringUtils.defaultIfEmpty(request.getParameter("pageNum"), "1");
			boolean isQualityResource = bbsReply != null && bbsReply.getBbsTopic().getCourse()!=null && Constants.BOOLEAN_YES.equals(bbsReply.getBbsTopic().getCourse().getIsQualityResource());//精品课程
			map.put("reloadUrl",request.getContextPath() + "/edu3/"+(isQualityResource?"course":"learning")+"/bbs/topic.html?topicId="+bbsReply.getBbsTopic().getResourceid()+"&"+(ExStringUtils.isNotEmpty(request.getParameter("courseId"))?"courseId="+request.getParameter("courseId"):"")+"&pageNum="+pageNum);
			map.put("bbsReplyId", bbsReply.getResourceid());
		}catch (Exception e) {
			logger.error("保存回复帖出错：{}",e.fillInStackTrace());
		}		
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 新增编辑主题
	 * @param request
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/learning/bbs/newtopic.html")
	public String enterNewTopic(HttpServletRequest request, ModelMap model) throws WebException {
		String sectionId = request.getParameter("sectionId");
		String topicId = request.getParameter("topicId");
		String courseId = request.getParameter("courseId");	
		List<BbsSection> bbsSections = new ArrayList<BbsSection>();
		if(ExStringUtils.isNotEmpty(courseId)){
			model.addAttribute("course", courseService.get(courseId));			
		}
		if(ExStringUtils.isNotEmpty(topicId)){ //编辑
			BbsTopic bbsTopic = bbsTopicService.get(topicId);
			List<Attachs> attachs = attachsService.findAttachsByFormId(topicId);
			bbsTopic.setAttachs(attachs);
			bbsSections = getParentSections(bbsTopic.getBbsSection());
			model.addAttribute("bbsSections", bbsSections);
			model.addAttribute("bbsTopic", bbsTopic);
		} else {
			BbsSection bbsSection = bbsSectionService.get(sectionId);
			if(bbsSection!=null){
				bbsSections = getParentSections(bbsSection);
			}
			model.addAttribute("bbsSections", bbsSections);
			model.addAttribute("bbsSection", bbsSection);
			BbsTopic bbsTopic = new BbsTopic();
			bbsTopic.setTopicType("1");
			model.addAttribute("bbsTopic", bbsTopic);
		}		
		
		User user = SpringSecurityHelper.getCurrentUser();
		if(user != null){
			model.addAttribute("storeDir", user.getUsername());	
			model.addAttribute("user", user);
		}
		return "/edu3/learning/bbssection/bbs-newtopic";
	}
	
	/**
	 * 保存主题
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/learning/bbs/topic/save.html")
	public void saveNewTopic(HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		try {	
			String sectionId = request.getParameter("sectionId");
			String bbsTopicId = request.getParameter("bbsTopicId");
			String courseId = request.getParameter("courseId");	
			String title = request.getParameter("title");
			String content = request.getParameter("content");
			String[] uploadfileids = request.getParameterValues("uploadfileid");
			String topicType = ExStringUtils.defaultIfEmpty(request.getParameter("topicType"), "1");
			BbsSection bbsSection = bbsSectionService.get(sectionId);
			User user =SpringSecurityHelper.getCurrentUser();
			
			BbsTopic bbsTopic = null;
			if(ExStringUtils.isNotEmpty(bbsTopicId)){//编辑
				bbsTopic = bbsTopicService.get(bbsTopicId);
				bbsTopic.setContent(content);
				bbsTopic.setTitle(title);
				bbsTopic.setTopicType(topicType);
				
				for (BbsTopic child : bbsTopic.getChilds()) {//更新子贴
					child.setContent(content);
					child.setTitle(title);
					child.setTopicType(topicType);
				}
			} else {
				bbsTopic = new BbsTopic();
				if(ExStringUtils.isNotEmpty(courseId)){
					Course course = courseService.get(courseId);
					bbsTopic.setCourse(course);
				}
				bbsTopic.setBbsSection(bbsSection);
				bbsTopic.setContent(content);
				bbsTopic.setTitle(title);
				bbsTopic.setTopicType(topicType);
				bbsTopic.setFillinDate(new Date());
				bbsTopic.setFillinMan(user.getCnName());
				
				bbsTopic.setLastReplyDate(bbsTopic.getFillinDate());
				bbsTopic.setLastReplyMan(user.getCnName());					
				if(bbsSection != null){
					bbsSection.setTopicCount(bbsSection.getTopicCount()+1);
				}
			}
			bbsTopicService.saveOrUpdateBbsTopic(bbsTopic, uploadfileids,null);
			map.put("statusCode", 200);
			boolean isQualityResource = bbsTopic != null && bbsTopic.getCourse()!=null && Constants.BOOLEAN_YES.equals(bbsTopic.getCourse().getIsQualityResource());//精品课程
			map.put("reloadUrl", request.getContextPath() +"/edu3/"+(isQualityResource?"course":"learning")+"/bbs/topic.html?topicId="+bbsTopic.getResourceid()+(ExStringUtils.isNotEmpty(courseId)?"&courseId="+courseId:""));
		}catch (Exception e) {
			logger.error("保存帖子出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
		}		
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 论坛搜索
	 * @param request
	 * @param model
	 * @param objPage
	 * @return
	 * @throws WebException
	 */
	@RequestMapping(value={"/edu3/learning/bbs/search.html","/edu3/course/bbs/search.html"})
	public String listBbsTopic(HttpServletRequest request,ModelMap model,Page objPage) throws WebException {
		User user = SpringSecurityHelper.getCurrentUser();
		if(user != null) {
			model.addAttribute("user", user);
		}
		String keyword = ExStringUtils.defaultIfEmpty(request.getParameter("keyword"), "");
		String courseId = request.getParameter("courseId");
		if(ExStringUtils.isNotEmpty(courseId)) {
			model.addAttribute("course", courseService.get(courseId));
		}
		if(ExStringUtils.isNotEmpty(keyword)){//搜索		
			Map<String,Object> condition = new HashMap<String,Object>();
			Map<String,Object> condition_query = new HashMap<String,Object>();
			String orderBy = request.getParameter("orderBy");//排序类型
			String order = request.getParameter("order");
			if(ExStringUtils.isNotEmpty(orderBy)) {
				condition_query.put("orderBy", orderBy);
			}
			if(ExStringUtils.isNotEmpty(order)) {
				condition_query.put("order", order);
			}
			objPage.setOrderBy(ExStringUtils.defaultIfEmpty(orderBy, "lastReplyDate"));
			objPage.setOrder(ExStringUtils.defaultIfEmpty(order, "desc"));//设置默认排序方式
			objPage.setPageSize(30); //每页30帖子			
			
			condition_query.put("keyword", keyword);
			String sType = request.getParameter("sType");//搜索方式
			if(ExStringUtils.isNotEmpty(sType)){
				condition.put(sType, keyword);
				condition_query.put("sType", sType);
			}
			if(ExStringUtils.isNotEmpty(courseId)){
				condition.put("courseId", courseId);
				condition_query.put("courseId", courseId);				
			} else {
				condition.put("emptyCourse", "Y");
			}
			
			String start = request.getParameter("start");
			String end = request.getParameter("end");			
			if(ExStringUtils.isNotEmpty(start)){
				condition_query.put("start", start);
				condition.put("fillinDateStart", ExDateUtils.convertToDate(start).getTime());
			}
			if(ExStringUtils.isNotEmpty(end)){
				condition_query.put("end", end);	
				condition.put("fillinDateEnd", ExDateUtils.addDays(ExDateUtils.convertToDate(end), 1).getTime());
			}
					
			String bbsSectionId = request.getParameter("sectionId");//搜索范围
			if(ExStringUtils.isNotEmpty(bbsSectionId)){
				condition.put("bbsSectionId", bbsSectionId);
				condition_query.put("sectionId", bbsSectionId);
			}
			String type = request.getParameter("type");//主题范围	
			if(ExStringUtils.isNotEmpty(type)) {
				condition_query.put("type", type);
			}
			type = ExStringUtils.defaultIfEmpty(type, "2");
			if("1".equals(type)){
				condition.put("status", 1);
			} else if("3".equals(type)){
				condition.put("topLevel", 3);
			}
			
			Page searchBbsTopicPage = bbsTopicService.findBbsTopicByCondition(condition, objPage);
			model.addAttribute("searchBbsTopicPage", searchBbsTopicPage);	
			model.addAttribute("condition", condition_query);
		} else {			
			objPage.setPageSize(30); //每页30帖子
			String search = ExStringUtils.defaultIfEmpty(request.getParameter("search"), "");
			Map<String,Object> condition = new HashMap<String,Object>();	
			Map<String,Object> condition_query = new HashMap<String,Object>();
			if(ExStringUtils.isNotEmpty(courseId)){
				condition.put("courseId", courseId);
				condition_query.put("courseId", courseId);					
			} else {
				condition.put("emptyCourse", "Y");
			}
			if("hot".equals(search)){	
				objPage.setOrderBy("replyCount");
				objPage.setOrder(Page.DESC);//设置默认排序方式				
				condition.put("replyCount", 10);				
			} else if("new".equals(search)){	//3天之内的帖子
				objPage.setOrderBy("fillinDate");
				objPage.setOrder(Page.DESC);//设置默认排序方式			
				condition.put("fillinDateStart", ExDateUtils.addDays(ExDateUtils.getToday(), -3).getTime());
				condition.put("parenTopic", "null");
			} else if("digest".equals(search)){	//精华帖
				objPage.setOrderBy("fillinDate");
				objPage.setOrder(Page.DESC);//设置默认排序方式
				condition.put("status", 1);
			} else if("mine".equals(search)){	//我的主题
				objPage.setOrderBy("fillinDate");
				objPage.setOrder(Page.DESC);//设置默认排序方式
				condition.put("fillinManId", user.getResourceid());
				condition.put("parenTopic", "null");
			}
			Page searchBbsTopicPage = bbsTopicService.findBbsTopicByCondition(condition, objPage);
			model.addAttribute("searchBbsTopicPage", searchBbsTopicPage);	
			
			condition_query.put("search", search);
			model.addAttribute("condition", condition_query);
		}		
		
		return "/edu3/learning/bbssection/bbs-search";
	}		
	/**
	 * 高级搜索
	 * @param request
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping(value={"/edu3/learning/bbs/advancedSearch.html","/edu3/course/bbs/advancedSearch.html"})
	public String advancedSearch(HttpServletRequest request,ModelMap model) throws WebException {
		User user = SpringSecurityHelper.getCurrentUser();
		if(user != null) {
			model.addAttribute("user", user);
		}
		String courseId = request.getParameter("courseId");
		if(ExStringUtils.isNotEmpty(courseId)){
			model.addAttribute("course", courseService.get(courseId));			
		}
//		String hql = " from "+BbsSection.class.getSimpleName()+" where isDeleted=0 and parent is null ";
//		if(ExStringUtils.isNotEmpty(courseId)){
//			hql += " and isCourseSection = 'Y' ";
//			model.addAttribute("course", courseService.get(courseId));			
//		} else {
//			hql += " and isCourseSection = 'N' ";
//		}
		
//		Map<String,Object> map = bbsSectionService.getSectionList(hql);		
//		model.addAttribute("parentBbsSections", map);	
		return "/edu3/learning/bbssection/bbs-advancedsearch";
	}
	/**
	 * 评优秀帖
	 * @param resourceid
	 * @param isBest
	 * @param type
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/learning/bbs/isbest.html")
	public void isbestBbsTopic(String resourceid,String courseId,String isBest,String type,HttpServletResponse response) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		try {	
			if(ExStringUtils.isNotBlank(resourceid) && ExStringUtils.isNotBlank(courseId)){
				User user = SpringSecurityHelper.getCurrentUser();
				String roleTeacher = CacheAppManager.getSysConfigurationByCode("sysuser.role.teacher").getParamValue();
				if(SpringSecurityHelper.isUserInRole("ROLE_ADMIN") || SpringSecurityHelper.isUserInRole(roleTeacher) //&& teachTaskService.isCourseTeacher(courseId, user.getResourceid(), 0) 
						){
					if(ExStringUtils.isNotEmpty(type)){
						if("1".equals(type)){
							BbsTopic bbsTopic = bbsTopicService.get(resourceid);
							bbsTopic.setIsBest(isBest);
							bbsTopicService.update(bbsTopic);
						} else if("2".equals(type)){
							BbsReply bbsReply = bbsReplyService.get(resourceid);
							bbsReply.setIsBest(isBest);
							bbsReplyService.update(bbsReply);							
						}
						map.put("statusCode", 200);
						map.put("message", "操作成功！");
					}	
				} else {
					map.put("statusCode", 300);
					map.put("message", "操作失败！您不是课程的老师，没有操作权限!");
				}
			} else {
				map.put("statusCode", 300);
				map.put("message", "操作失败！");
			}
		} catch (Exception e) {
			logger.error("贴子评优出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "操作出错！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 设置帖子状态
	 * @param resourceid
	 * @param status 置顶(3)、加精(1)、锁定(-1)、取消置顶(0)、取消加精(0)、解除锁定(-3)
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/learning/bbs/topic/status.html")
	public void statusBbsTopic(String resourceid,Integer status,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		try {			
			if(ExStringUtils.isNotBlank(resourceid)){			
				bbsTopicService.setBbsTopicStatus(resourceid.split("\\,"), status);				
				map.put("statusCode", 200);
				map.put("message", "操作成功！");				
			} else {
				map.put("statusCode", 300);
				map.put("message", "操作失败！");
			}
		} catch (Exception e) {
			logger.error("设置论坛帖子状态出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "操作出错！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 删除论坛帖子
	 * @param resourceid
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/learning/bbs/topic/remove.html")
	public void removeBbsTopic(String resourceid,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		try {			
			if(ExStringUtils.isNotBlank(resourceid)){			
				bbsTopicService.deleteBbsTopic(resourceid.split("\\,"));
				map.put("statusCode", 200);
				map.put("message", "删除成功！");				
			} else {
				map.put("statusCode", 300);
				map.put("message", "操作失败！");
			}
		} catch (Exception e) {
			logger.error("删除论坛帖子出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "操作失败！\n"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 删除回复
	 * @param resourceid
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/learning/bbs/reply/remove.html")
	public void removeBbsReply(String resourceid,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		try {			
			if(ExStringUtils.isNotBlank(resourceid)){			
				bbsReplyService.deleteBbsReply(resourceid.split("\\,"));
				map.put("statusCode", 200);
				map.put("message", "删除成功！");				
			} else {
				map.put("statusCode", 300);
				map.put("message", "操作失败！");
			}
		} catch (Exception e) {
			logger.error("删除论坛回复帖出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 查看用户信息
	 * @param userid
	 * @param request
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/learning/bbs/user.html")
	public String viewBbsUserInfo(String userid, HttpServletRequest request,ModelMap model) throws WebException {
		if(ExStringUtils.isNotBlank(userid)){
			BbsUserInfo bbsUserInfo = bbsUserInfoService.findUniqueByProperty("sysUser.resourceid",userid);
			if(bbsUserInfo != null){
				User topicUser = bbsUserInfo.getSysUser();				
				String userface = getUserFacePath(request, topicUser);	
				bbsUserInfo.setUserface(userface);
				String roleCode = CacheAppManager.getSysConfigurationByCode("sysuser.role.student").getParamValue();
				if(topicUser.isContainRole(roleCode)){//学生	
					StudentInfo studentInfo = studentInfoService.getStudentInfoByUser(topicUser);
					bbsUserInfo.setStudentInfo(studentInfo);
				}
			} 			
			model.addAttribute("bbsUserInfo", bbsUserInfo);
		}
		String courseId = request.getParameter("courseId");
		if(ExStringUtils.isNotBlank(courseId)){
			model.addAttribute("course", courseService.get(courseId));
		}
		model.addAttribute("user", SpringSecurityHelper.getCurrentUser());
		return "/edu3/learning/bbssection/bbs-user";
	}
}
