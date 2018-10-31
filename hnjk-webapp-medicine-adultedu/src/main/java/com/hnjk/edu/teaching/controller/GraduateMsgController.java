package com.hnjk.edu.teaching.controller;

import java.util.ArrayList;
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

import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.utils.ExBeanUtils;
import com.hnjk.core.foundation.utils.ExCollectionUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.BaseSupportController;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.teaching.model.ExamSub;
import com.hnjk.edu.teaching.model.GraduateMentor;
import com.hnjk.edu.teaching.model.GraduateMsg;
import com.hnjk.edu.teaching.model.GraduatePapersOrder;
import com.hnjk.edu.teaching.service.IExamSubService;
import com.hnjk.edu.teaching.service.IGraduateMentorService;
import com.hnjk.edu.teaching.service.IGraduateMsgService;
import com.hnjk.edu.teaching.service.IGraduatePapersOrderService;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.Attachs;
import com.hnjk.platform.system.service.IAttachsService;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.cache.CacheSecManager;
import com.hnjk.security.model.Resource;
import com.hnjk.security.model.User;

/**
 * 毕业生与老师毕业论文交流表.
 * <code>GraduateMsgController</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2010-10-28 下午04:27:56
 * @see 
 * @version 1.0
 */
@Controller
public class GraduateMsgController extends BaseSupportController {

	private static final long serialVersionUID = -6245153330659355802L;
	
	
	/**
	 * 交流区学生预约列表
	 * @param batchId
	 * @param stuName
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/graduateMsg/list.html")
	public String exeList(String batchId, String stuName,String stuStudyNo, Page objPage, ModelMap model) throws WebException{
		objPage.setOrderBy("auditTime");
		objPage.setOrder(Page.ASC);//设置默认排序方式
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件
						
		
		if(ExStringUtils.isNotEmpty(stuName)) {
			condition.put("name", stuName);
		}
		if(ExStringUtils.isNotEmpty(stuStudyNo)) {
			condition.put("stuStudyNo", stuStudyNo);
		}
	
		User user = SpringSecurityHelper.getCurrentUser();
	
		//判断学生与老师身份
		if(SpringSecurityHelper.isUserInRole("ROLE_STUDENT")){
			condition.put("isStudent", Constants.BOOLEAN_YES);			
		}else{
			//老师界面，找出默认批次
			condition.put("isStudent", Constants.BOOLEAN_NO);	
			List<ExamSub> list = graduatePapersOrderService.findByHql(ExamSub.class,"from "+ExamSub.class.getSimpleName()+" where isDeleted = ? and batchType = ? order by yearInfo.firstYear desc", 
					0,"thesis");
			model.addAttribute("examSubList", list);
			//如果查询批次不为空
			if(ExStringUtils.isEmpty(batchId) && null != list && !list.isEmpty()){
				for(ExamSub examSub : list){
					if("2".equals(examSub.getExamsubStatus())){//当前开放批次
						batchId = examSub.getResourceid();
						break;
					}
				}
			}
			
		}
		
		if(ExStringUtils.isNotEmpty(batchId)) {
			condition.put("batchId", batchId);
		}
		condition.put("loginId", user.getResourceid());		
		condition.put("status", Constants.BOOLEAN_TRUE);//列出已审核过的
		Page page = graduatePapersOrderService.findGraduateByCondition(condition, objPage);
				
		model.addAttribute("condition", condition);
		model.addAttribute("msgList", page);		
		
		return "/edu3/teaching/graduateMsg/graduateMsg-list";
	}
	
	/**
	 * 学生毕业论文主页面
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/framework/graduateMsg/main.html")
	public String exeStuMainPage(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		List<Resource> resList = CacheSecManager.getChildren("RES_STUDENT_GRADUATION");
		model.addAttribute("resList",resList);
		return "/edu3/teaching/graduateMsg/graduateMsg-stu-main";
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
	@RequestMapping("/edu3/teaching/graduateMsg/edit.html")
	public String exeEdit(String resourceid,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{		
	
		User user = SpringSecurityHelper.getCurrentUser();
		
		List<GraduateMsg> ListMsg = new ArrayList<GraduateMsg>();
		String msgId = ExStringUtils.trimToEmpty(request.getParameter("msgid"));//留言ID
		if(ExStringUtils.isNotBlank(resourceid)){ //-----编辑
			GraduatePapersOrder order = graduatePapersOrderService.get(resourceid);
			GraduateMsg msg = null;
			if(ExStringUtils.isNotEmpty(msgId)){
				msg = graduateMsgService.get(msgId);				
			}else{
				msg = new GraduateMsg();
				msg.setFillinManId(user.getResourceid());
				msg.setFillinMan(user.getCnName());
				msg.setCurrentTache(order.getCurrentTache());
				msg.setGraduatePapersOrder(order);
			}
			model.addAttribute("graduate", msg);
			model.addAttribute("tabIndex", Integer.parseInt(order.getCurrentTache())-1);//当前环节
			//话题列表
			ListMsg = graduateMsgService.findByHql("from "+GraduateMsg.class.getSimpleName()+" m where m.isDeleted=0 and m.graduatePapersOrder.resourceid=? order by m.sendTime asc", new String[]{resourceid});
			
			for (GraduateMsg graduateMsg : ListMsg) {
				List<Attachs> filelist =  attachsService.findAttachsByFormId(graduateMsg.getResourceid());
				if(null !=filelist && !filelist.isEmpty()) {
					graduateMsg.setAttachs(filelist);
				}
			}
		}
		model.addAttribute("userId", user.getResourceid());	
		model.addAttribute("ListMsg", ListMsg);
		model.addAttribute("storeDir", user.getUsername());
		return "/edu3/teaching/graduateMsg/graduateMsg-form";
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
	@RequestMapping("/edu3/teaching/graduateMsg/save.html")
	public void exeSave(GraduateMsg graduateMsg,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		String paperOrderId = ExStringUtils.trimToEmpty(request.getParameter("paperOrderId"));
		try {
			User user = SpringSecurityHelper.getCurrentUser();
			String[] files = request.getParameterValues("uploadfileid");
			if(ExStringUtils.isNotBlank(graduateMsg.getResourceid())){ //--------------------更新
				GraduateMsg p_graduateMsg = graduateMsgService.get(graduateMsg.getResourceid());
				
				if(ExStringUtils.isNotEmpty(paperOrderId)){
					GraduatePapersOrder order = graduatePapersOrderService.get(paperOrderId);
					graduateMsg.setGraduatePapersOrder(order);
				}
				ExBeanUtils.copyProperties(p_graduateMsg, graduateMsg);
				graduateMsgService.saveOrUpdate(p_graduateMsg, files, user);
			}else{ //-------------------------------------------------------------------保存
				if(ExStringUtils.isNotEmpty(paperOrderId)){
					GraduatePapersOrder order = graduatePapersOrderService.get(paperOrderId);
					graduateMsg.setGraduatePapersOrder(order);
				}
				graduateMsgService.saveOrUpdate(graduateMsg, files, user);
			}
			map.put("statusCode", 200);
			map.put("message", "保存成功！");
			map.put("navTabId", "RES_TEACHING_THESIS_MSG_VIEW");
			map.put("forward", request.getContextPath() +"/edu3/teaching/graduateMsg/edit.html?resourceid="+paperOrderId);
		}catch (Exception e) {
			logger.error("保存出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	
	/**
	 * 删除留言对象
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/graduateMsg/delete.html")
	public void exeDelete(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		String resourceid = ExStringUtils.trimToEmpty(request.getParameter("resourceid"));
		String paperOrderId = ExStringUtils.trimToEmpty(request.getParameter("paperOrderId"));
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(resourceid)){
				if(resourceid.split("\\,").length >1){//批量删除					
					graduateMsgService.batchCascadeDelete(resourceid.split("\\,"));
				}else{//单个删除
					graduateMsgService.delete(resourceid);
				}
				map.put("statusCode", 200);
				map.put("message", "删除成功！");				
				map.put("callbackType", "forward");
				map.put("forwardUrl", request.getContextPath()+"/edu3/teaching/graduateMsg/edit.html?resourceid="+paperOrderId);
			}
		} catch (Exception e) {
			logger.error("删除论文交流出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 进入下一环节
	 * @param request
	 * @param response
	 * @param model
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/graduateMsg/nextTache.html")
	public void exeNext(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		String resourceid = ExStringUtils.trimToEmpty(request.getParameter("resourceid"));//论文预约ID		
		String fromPage = ExStringUtils.trimToEmpty(request.getParameter("fromPage"));
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(resourceid)){
				if(resourceid.split("\\,").length >1){//批量处理				
					graduatePapersOrderService.batchCascadeNext(resourceid.split("\\,"));
				}else{//单个处理
					graduatePapersOrderService.next(resourceid);
				}
				map.put("statusCode", 200);
				map.put("message", "设置成功！");
				if("edit".equals(fromPage)){//如果是从编辑页面来
					map.put("forward", request.getContextPath()+"/edu3/teaching/graduateMsg/edit.html?resourceid="+resourceid);
				}else{
					map.put("forward", request.getContextPath()+"/edu3/teaching/graduateMsg/list.html");
				}		
				
			}
		} catch (Exception e) {
			logger.error("毕业论文进入下一环节出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "进入下一环节出错！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	
	/**
	 * 选择预约信息
	 * @param objPage
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/edu3/teaching/graduateMsg/chooseGmsg.html")
	public String chooseGmsg(String branchSchool, String grade, String major, String name, String batchId, String classic, Page objPage,  ModelMap model) throws Exception{
		objPage.setOrderBy("auditTime");
		objPage.setOrder(Page.ASC);//设置默认排序方式
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件

		if(ExStringUtils.isNotEmpty(grade)) {
			condition.put("grade", grade);
		}
		if(ExStringUtils.isNotEmpty(major)) {
			condition.put("major", major);
		}
		if(ExStringUtils.isNotEmpty(name)) {
			condition.put("name", name);
		}
		if(ExStringUtils.isNotEmpty(batchId)) {
			condition.put("batchId", batchId);
		}
		if(ExStringUtils.isNotEmpty(classic)) {
			condition.put("classic", classic);
		}
		
		String center = "show";
		User cureentUser = SpringSecurityHelper.getCurrentUser();
		//招生人员--学习中心
		if(cureentUser.getOrgUnit().getUnitType().
				indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){
			branchSchool = cureentUser.getOrgUnit().getResourceid();
			center = "hide";
		}
		if(ExStringUtils.isNotEmpty(branchSchool)) {
			condition.put("branchSchool", branchSchool);
		}
		
		Page page = graduatePapersOrderService.findGraduateByCondition(condition, objPage);
		
		model.addAttribute("showCenter", center);
		model.addAttribute("condition", condition);
		model.addAttribute("ordercList", page);
		return "/edu3/teaching/graduateMsg/graduatePapers-list2";
	}

	/**
	 * 讨论主题列表
	 * @param batchId
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/graduatemsg/group/list.html")
	public String listGraduateMsg(String batchId, Page objPage, ModelMap model) throws WebException{
		objPage.setOrderBy("sendTime");
		objPage.setOrder(Page.DESC);//设置默认排序方式
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件						
		
		User user = SpringSecurityHelper.getCurrentUser();
	
		//判断学生与老师身份
		if(SpringSecurityHelper.isUserInRole(CacheAppManager.getSysConfigurationByCode("sysuser.role.student").getParamValue())){
			condition.put("isStudent", Constants.BOOLEAN_YES);
		}else{
			//老师界面，找出默认批次
			condition.put("isStudent", Constants.BOOLEAN_NO);
			List<ExamSub> list = examSubService.findByHql("from "+ExamSub.class.getSimpleName()+" where isDeleted = ? and batchType = ? order by yearInfo.firstYear desc", 	0,"thesis");
			model.addAttribute("examSubList", list);
			//如果查询批次不为空
			if(ExStringUtils.isEmpty(batchId) && null != list && !list.isEmpty()){
				for(ExamSub examSub : list){
					if("2".equals(examSub.getExamsubStatus())){//当前开放批次
						batchId = examSub.getResourceid();
						break;
					}
				}
			}				
		}
		
		if(ExStringUtils.isNotEmpty(batchId)) {
			condition.put("batchId", batchId);
		}
		condition.put("isGroupTopic", Constants.BOOLEAN_YES);
		condition.put("isMainTopic", Constants.BOOLEAN_YES);
		condition.put("userId", user.getResourceid());
		Page page = graduateMsgService.findGraduateMsgByCondition(condition, objPage);
				
		model.addAttribute("condition", condition);
		model.addAttribute("msgList", page);		
		
		return "/edu3/teaching/graduateMsg/graduatemsg-group-list";
	}
	/**
	 * 新增编辑留言
	 * @param resourceid
	 * @param parentId
	 * @param request
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/graduatemsg/group/input.html")
	public String editGraduateMsg(String resourceid,String parentId,HttpServletRequest request,Page objPage,ModelMap model) throws WebException{		
		objPage.setOrderBy("sendTime");
		objPage.setOrder(Page.ASC);
		
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件
		GraduateMsg parentMsg = null;
		if(ExStringUtils.isNotBlank(parentId)){			
			condition.put("parentId",parentId);
			parentMsg = graduateMsgService.get(parentId);
			parentMsg.setAttachs(attachsService.findAttachsByFormId(parentMsg.getResourceid()));
			model.addAttribute("parentMsg", parentMsg);
			
			Page page = graduateMsgService.findGraduateMsgByCondition(condition, objPage);
			
			if(ExCollectionUtils.isNotEmpty(page.getResult())){
				for (Object obj : page.getResult()) {
					GraduateMsg msg = (GraduateMsg)obj;
					msg.setAttachs(attachsService.findAttachsByFormId(msg.getResourceid()));
				}
			}
			
			model.addAttribute("msgList", page);
			model.addAttribute("condition", condition);
		}		
		
		User user = SpringSecurityHelper.getCurrentUser();	
		GraduateMsg graduateMsg = new GraduateMsg();
		if(ExStringUtils.isNotBlank(resourceid)){ //-----编辑
			graduateMsg = graduateMsgService.get(resourceid);
			graduateMsg.setAttachs(attachsService.findAttachsByFormId(graduateMsg.getResourceid()));
		} else {
			if(ExStringUtils.isNotBlank(parentId)){//-----回复				
				graduateMsg.setParent(parentMsg);
			}
		}
		model.addAttribute("graduateMsg", graduateMsg);
		model.addAttribute("storeDir", user.getUsername());
		model.addAttribute("userId", user.getResourceid());
		
		boolean isStudent = true;
		if(!SpringSecurityHelper.isUserInRole(CacheAppManager.getSysConfigurationByCode("sysuser.role.student").getParamValue())){
			List<ExamSub> list = examSubService.findByHql("from "+ExamSub.class.getSimpleName()+" where isDeleted = ? and batchType = ? and examsubStatus = ? order by yearInfo.firstYear desc", 
					0,"thesis","2");//开放批次
			model.addAttribute("examSubList", list);
			isStudent = false;
		} 	
		model.addAttribute("isStudent", isStudent);
		return "/edu3/teaching/graduateMsg/graduatemsg-group-form";
	}
	/**
	 * 保存留言
	 * @param resourceid
	 * @param parentId
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/graduatemsg/group/save.html")
	public void saveGraduateMsg(String resourceid,String parentId,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			String batchId = request.getParameter("batchId");
			String title = request.getParameter("title");
			String content = request.getParameter("content");
			String[] uploadfileid = request.getParameterValues("uploadfileid");
			
			User user = SpringSecurityHelper.getCurrentUser();
			GraduateMsg msg = new GraduateMsg();
			if(ExStringUtils.isNotBlank(resourceid)){ //--------------------更新
				msg = graduateMsgService.get(resourceid);
				if(msg.getParent()==null){
					msg.setTitle(title);
				}				
				msg.setContent(content);
			}else{ //-------------------------------------------------------------------保存				
				msg.setTitle(title);
				msg.setContent(content);
				msg.setFillinMan(user.getCnName());
				msg.setFillinManId(user.getResourceid());				
				msg.setIsGroupTopic(Constants.BOOLEAN_YES);
				
				if(ExStringUtils.isNotBlank(parentId)){ //回复
					GraduateMsg pMsg = graduateMsgService.get(parentId);
					msg.setParent(pMsg);
					pMsg.getChilds().add(msg);
					batchId = pMsg.getGraduateMentor().getExamSub().getResourceid();
				} 
				
				if(SpringSecurityHelper.isUserInRole(CacheAppManager.getSysConfigurationByCode("sysuser.role.student").getParamValue())){
					msg.setIsStudent(Constants.BOOLEAN_YES);
					List<GraduatePapersOrder> orders = graduatePapersOrderService.findByHql("from "+GraduatePapersOrder.class.getSimpleName()+" where isDeleted=0 and studentInfo.sysUser.resourceid=? and examSub.examsubStatus=? ", user.getResourceid(),"2");
					if(ExCollectionUtils.isNotEmpty(orders)){
						msg.setGraduatePapersOrder(orders.get(0));
						List<GraduateMentor> mentors = graduateMentorService.findByCriteria(Restrictions.eq("isDeleted", 0),Restrictions.eq("examSub.resourceid", orders.get(0).getExamSub().getResourceid()),Restrictions.eq("edumanager.resourceid", orders.get(0).getTeacher().getResourceid()));
						if(ExCollectionUtils.isNotEmpty(mentors)){
							msg.setGraduateMentor(mentors.get(0));
						}
					} else {
						throw new WebException("未分配指导老师或论文批次已经关闭，无法再留言");
					}
				} else {
					msg.setIsStudent(Constants.BOOLEAN_NO);
					List<GraduateMentor> mentors = graduateMentorService.findByCriteria(Restrictions.eq("isDeleted", 0),Restrictions.eq("examSub.resourceid", batchId),Restrictions.eq("edumanager.resourceid", user.getResourceid()));
					if(ExCollectionUtils.isNotEmpty(mentors)){
						msg.setGraduateMentor(mentors.get(0));
					} else {
						throw new WebException("您还未被指定为指导老师，无法发言");
					}
				}					
			}			
			graduateMsgService.saveOrUpdate(msg, uploadfileid, user);	
			if(msg.getParent()==null){
				parentId = msg.getResourceid();
			}
			map.put("statusCode", 200);
			map.put("message", "保存成功！");
			map.put("navTabId", "RES_TEACHING_THESIS_MOOT_ADD");
			map.put("reloadUrl", request.getContextPath() +"/edu3/teaching/graduatemsg/group/input.html?parentId="+ExStringUtils.trimToEmpty(parentId));
		}catch (Exception e) {
			logger.error("保存出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败！<br/>"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}

	/**
	 * 删除留言
	 * @param resourceid
	 * @param parentId
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/graduatemsg/group/remove.html")
	public void removeGraduateMsg(String resourceid,String parentId,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(resourceid)){
				graduateMsgService.delete(resourceid);
				map.put("statusCode", 200);
				map.put("message", "删除成功！");		
				map.put("navTabId", "RES_TEACHING_THESIS_MOOT_ADD");
				map.put("callbackType", "forward");
				map.put("forwardUrl", request.getContextPath()+"/edu3/teaching/graduatemsg/group/input.html?parentId="+ExStringUtils.trimToEmpty(parentId));
			}
		} catch (Exception e) {
			logger.error("删除论文交流出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	@Autowired
	@Qualifier("graduatemsgservice")
	private IGraduateMsgService graduateMsgService;
	@Autowired
	@Qualifier("attachsService")
	IAttachsService attachsService;
	@Autowired
	@Qualifier("graduatepapersorderservice")
	private IGraduatePapersOrderService graduatePapersOrderService;
	@Autowired
	@Qualifier("graduatementorservice")
	private IGraduateMentorService graduateMentorService;
	@Autowired
	@Qualifier("examSubService")
	private IExamSubService examSubService;
}
