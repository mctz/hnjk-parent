package com.hnjk.edu.learning.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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
import org.springframework.web.bind.annotation.RequestMapping;

import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.utils.ExCollectionUtils;
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.foundation.utils.SensitivewordFilter;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.BaseSupportController;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.learning.model.BbsReply;
import com.hnjk.edu.learning.model.BbsTopic;
import com.hnjk.edu.learning.model.BbsUserInfo;
import com.hnjk.edu.learning.service.IBbsReplyService;
import com.hnjk.edu.learning.service.IBbsTopicService;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.Attachs;
import com.hnjk.platform.system.service.IAttachsService;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;
import com.opensymphony.util.Data;
/**
 * 论坛帖子回复管理
 * <code>BbsReplyController</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-9-8 下午02:27:03
 * @see 
 * @version 1.0
 */
@Controller
public class BbsReplyController extends BaseSupportController {

	private static final long serialVersionUID = -5272992422791447645L;
	
	@Autowired
	@Qualifier("bbsReplyService")
	private IBbsReplyService bbsReplyService;
	
	@Autowired
	@Qualifier("bbsTopicService")
	private IBbsTopicService bbsTopicService;
	
	@Autowired
	@Qualifier("attachsService")
	private IAttachsService attachsService;
	
//	@Autowired
//	@Qualifier("teachtaskservice")
//	private ITeachTaskService teachTaskService;

	/**
	 * 论坛回复帖列表
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/metares/topicreply/bbsreply/list.html")
	public String listBbsReply(HttpServletRequest request, Page objPage, ModelMap model) throws WebException{
		objPage.setOrderBy("replyDate");
		objPage.setOrder(Page.DESC);//设置默认排序方式		
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件
		
		String bbsTopicId = request.getParameter("bbsTopicId");
		String replyMan = request.getParameter("replyMan");
		
		String replyDateStartStr = ExStringUtils.defaultIfEmpty(request.getParameter("replyDateStartStr"), "");
		String replyDateEndStr = ExStringUtils.defaultIfEmpty(request.getParameter("replyDateEndStr"),"");
		condition.put("replyDateStartStr", replyDateStartStr);
		condition.put("replyDateEndStr", replyDateEndStr);
				
		if(ExStringUtils.isNotEmpty(replyDateStartStr)) {
			condition.put("replyDateStart", ExDateUtils.convertToDate(replyDateStartStr).getTime());
		}
		if(ExStringUtils.isNotEmpty(replyDateEndStr)) {
			condition.put("replyDateEnd", ExDateUtils.addDays(ExDateUtils.convertToDate(replyDateEndStr), 1).getTime());
		}
		
		if(ExStringUtils.isNotEmpty(bbsTopicId)) {
			condition.put("bbsTopicId", bbsTopicId);
		}
		if(ExStringUtils.isNotEmpty(replyMan)) {
			condition.put("replyMan", replyMan);
		}
		
		condition.put("isDeleted", 0);
		Page bbsReplyListPage = bbsReplyService.findBbsReplyByCondition(condition, objPage);
		
		model.addAttribute("bbsReplyListPage", bbsReplyListPage);
		model.addAttribute("condition", condition);
		return "/edu3/learning/bbsreply/bbsreply-list";
	}
	
	/**
	 * 新增编辑论坛回复帖
	 * @param resourceid
	 * @param bbsTopicId
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/metares/topicreply/bbsreply/input.html")
	public String editBbsReply(String resourceid,String bbsTopicId,HttpServletRequest request, ModelMap model) throws WebException {
		User user = SpringSecurityHelper.getCurrentUser();
		BbsReply bbsReply = null;
		if(ExStringUtils.isNotBlank(resourceid)){ //-----编辑
			bbsReply = bbsReplyService.get(resourceid);	
			List<Attachs> attachs = attachsService.findAttachsByFormId(resourceid);
			bbsReply.setAttachs(attachs);
		}else{ //----------------------------------------新增
			BbsTopic bbsTopic = bbsTopicService.get(bbsTopicId);
			bbsReply = new BbsReply();
			bbsReply.setBbsTopic(bbsTopic);
			//随堂问答
			String from = ExStringUtils.trimToEmpty(request.getParameter("from"));
			if("interlocution".equals(from)){
				String roleCode = CacheAppManager.getSysConfigurationByCode("sysuser.role.teacher").getParamValue();//获取角色编码
				//boolean isTeacher = SpringSecurityHelper.isUserInRole(roleCode);
				if("edumanager".equals(user.getUserType()) //&& teachTaskService.isCourseTeacher(bbsTopic.getCourse().getResourceid(), user.getResourceid(), 0)
						){//课程负责人或辅导老师回复随堂问答
					List<BbsReply> bbsReplys = bbsReplyService.findByHql(" from "+BbsReply.class.getSimpleName()+" where isDeleted=0 and bbsTopic.resourceid=? and bbsUserInfo.sysUser.resourceid=? order by showOrder ", new Object[]{bbsTopicId,user.getResourceid()});
					if(bbsReplys!=null&&bbsReplys.size()>0){
						bbsReply = bbsReplys.get(0);
						List<Attachs> attachs = attachsService.findAttachsByFormId(bbsReply.getResourceid());
						bbsReply.setAttachs(attachs);
					}
				}
			}			 
		}	
		model.addAttribute("sectioncode", CacheAppManager.getSysConfigurationByCode("web.bbs.sectioncode").getParamValue());	
		model.addAttribute("bbsReply", bbsReply);		
		model.addAttribute("storeDir", user.getUsername());
		return "/edu3/learning/bbsreply/bbsreply-form";
	}
	/**
	 * 保存回复帖
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/edu3/metares/topicreply/bbsreply/save.html")
	public void saveBbsReply(HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();		
		int statusCode = 200;
		String message = "保存成功！";
		try {	
			do {
				User user = SpringSecurityHelper.getCurrentUser();
				String secCode = CacheAppManager.getSysConfigurationByCode("web.bbs.sectioncode").getParamValue();
				String roleCode = CacheAppManager.getSysConfigurationByCode("sysuser.role.teacher").getParamValue();
				String edumanager = CacheAppManager.getSysConfigurationByCode("sysuser.usertype.edumanager").getParamValue();
				String resourceid = request.getParameter("resourceid");
				String bbsTopicId = request.getParameter("bbsTopicId");
				String tags = ExStringUtils.trimToEmpty(request.getParameter("tags"));
				String replyContent = request.getParameter("replyContent");
				String[] uploadfileids = request.getParameterValues("uploadfileid");
				if(ExStringUtils.isEmpty(replyContent)){
					statusCode = 300;
					message = "回复内容不能为空";
					continue;
				}
				// 判断是否包含敏感词
				SensitivewordFilter sensitivewordFilter = (SensitivewordFilter)request.getSession().getServletContext().getAttribute("sensitivewordFilter");
				Set<String> sensitivewordSet = sensitivewordFilter.getSensitiveWord(ExStringUtils.trimToEmpty(replyContent),SensitivewordFilter.maxMatchType);
				if(ExCollectionUtils.isNotEmpty(sensitivewordSet)){
					statusCode = 300;
					message = "回复内容含有敏感词："+sensitivewordSet+",请修改后再提交";
					continue;
				}
				
				BbsReply bbsReply = null;
				if (ExStringUtils.isNotBlank(resourceid)) { //--------------------更新
					bbsReply = bbsReplyService.get(resourceid);
					bbsReply.setReplyContent(replyContent);
					bbsReply.setIsPersisted(true);

					if (bbsReply.getBbsTopic().getIsAnswered() == Constants.BOOLEAN_FALSE
							&& bbsReply.getBbsTopic().getBbsSection().getSectionCode().equals(secCode)
							&& edumanager.equals(user.getUserType())//&&SpringSecurityHelper.isUserInRole(roleCode)
					//&&teachTaskService.isCourseTeacher(bbsReply.getBbsTopic().getCourse().getResourceid(), user.getResourceid(), 0)
					) {
						bbsReply.getBbsTopic().setIsAnswered(Constants.BOOLEAN_TRUE);//如果是课程老师，是指应答状态
					}
				} else { //-------------------------------------------------------------------保存
					bbsReply = new BbsReply();
					BbsTopic bbsTopic = bbsTopicService.get(bbsTopicId);

					bbsReply.setReplyContent(replyContent);
					bbsReply.setReplyDate(new Date());
					bbsReply.setReplyMan(user.getCnName());
					bbsReply.setShowOrder(bbsReplyService.getNextShowOrder(bbsTopicId));//排序号
					bbsTopic.setLastReplyDate(bbsReply.getReplyDate());
					bbsTopic.setLastReplyMan(bbsReply.getReplyMan());
					if (bbsTopic.getReplyCount() == null) {
						bbsTopic.setReplyCount(0);
					}
					bbsTopic.setReplyCount(bbsTopic.getReplyCount().intValue() + 1);
					bbsReply.setBbsTopic(bbsTopic);

					if (bbsTopic.getIsAnswered() == Constants.BOOLEAN_FALSE && edumanager.equals(user.getUserType())
							&& bbsTopic.getBbsSection().getSectionCode().equals(secCode)) {
						bbsTopic.setIsAnswered(Constants.BOOLEAN_TRUE);//如果是课程老师，是指应答状态}
					}
				}
				if (ExStringUtils.isNotBlank(tags)) {
					bbsReply.getBbsTopic().setTags(tags);
				}
				bbsReplyService.saveOrUpdateBbsReply(bbsReply, uploadfileids);
				map.put("navTabId", "RES_METARES_BBS_RREPLY_EDIT");
				map.put("reloadUrl",request.getContextPath()+ "/edu3/metares/topicreply/bbsreply/input.html?resourceid="+ bbsReply.getResourceid());
			} while (false);
		}catch (Exception e) {
			logger.error("保存回复帖出错：{}",e.fillInStackTrace());
			statusCode = 300;
			message = "保存失败！";
		} finally {
			map.put("statusCode", statusCode);
			map.put("message", message);
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
	@RequestMapping("/edu3/metares/topicreply/bbsreply/remove.html")
	public void removeBbsReply(String resourceid,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		try {			
			if(ExStringUtils.isNotBlank(resourceid)){			
				bbsReplyService.deleteBbsReply(resourceid.split("\\,"));
				map.put("statusCode", 200);
				map.put("message", "删除成功！");				
				map.put("forward", request.getContextPath()+"/edu3/metares/topicreply/bbsreply/list.html");
			}
		} catch (Exception e) {
			logger.error("删除课程论坛回复帖出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
}
