package com.hnjk.edu.teaching.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.utils.ExBeanUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.BaseSupportController;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.service.IEdumanagerService;
import com.hnjk.edu.teaching.model.ExamSub;
import com.hnjk.edu.teaching.model.GraduatePapersNotice;
import com.hnjk.edu.teaching.model.GraduatePapersOrder;
import com.hnjk.edu.teaching.service.IExamSubService;
import com.hnjk.edu.teaching.service.IGraduatePapersNoticeService;
import com.hnjk.edu.teaching.service.IGraduatePapersOrderService;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.Attachs;
import com.hnjk.platform.system.service.IAttachsService;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;

/**
 * 毕业导师公告表
 * <code>GraduatePapersNoticeController</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2010-10-28 下午04:41:20
 * @see 
 * @version 1.0
 */
@Controller
public class GraduatePapersNoticeController extends BaseSupportController {

	private static final long serialVersionUID = 5834289816622638694L;
	
	@RequestMapping("/edu3/teaching/graduateNotice/list.html")
	public String exeList(String examSub, String title, Page objPage, ModelMap model) throws WebException{
		objPage.setOrderBy("n.pubTime");
		objPage.setOrder(Page.ASC);//设置默认排序方式
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件
		
		if(ExStringUtils.isNotEmpty(examSub)) {
			condition.put("examSub", examSub);
		}
		if(ExStringUtils.isNotEmpty(title)) {
			condition.put("title", title);
		}

		//如果为老师，则查出老师自己发布的通告
		User user = SpringSecurityHelper.getCurrentUser();
		
		//判断学生与老师身份
		if(SpringSecurityHelper.isUserInRole("ROLE_STUDENT")){//如果为学生，则查出自己导师发布的公告
			condition.put("isStudent", Constants.BOOLEAN_YES);		
			condition.put("studentId", user.getResourceid());
			//List<GraduatePapersOrder> list = graduatePapersOrderService.findByHql("from "+GraduatePapersOrder.class.getSimpleName()+" where isDeleted = 0 " +
			//		" and studentInfo.sysUser.resourceid = ? ", user.getResourceid());
			//if(null != list && list.size()>0){
			//	for(GraduatePapersOrder graduatePapersOrder : list){
			//		condition.put("guidTeacherIds", "'"+graduatePapersOrder.getTeacher().getResourceid()+"',");
			//	}
			//}
		}else{//如果为老师，则查出老师自己发布的通告			
			condition.put("isStudent", Constants.BOOLEAN_NO);	
			condition.put("guidTeacherId", user.getResourceid());
		}
		
		
		Page page = graduatePapersNoticeService.findgraduateNoticeByCondition(condition, objPage);
		
		model.addAttribute("condition", condition);
		model.addAttribute("noticeList", page);
		return "/edu3/teaching/graduateNotice/graduateNotice-list";
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
	@RequestMapping("/edu3/teaching/graduateNotice/edit.html")
	public String exeEdit(String resourceid,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		List<Attachs> filelist = new ArrayList<Attachs>();
		User user = SpringSecurityHelper.getCurrentUser();
		
		//查出可用的预约批次
		@SuppressWarnings("unchecked")
		List<ExamSub> examSubs = examSubService.findByHql("from "+ExamSub.class.getSimpleName()+" where isDeleted= ? " +
				" and batchType = ? and examsubStatus = ?", 0,"thesis","2");
		model.addAttribute("examSubs", examSubs);
		if(ExStringUtils.isNotBlank(resourceid)){ //-----编辑
			GraduatePapersNotice graduatePapersNotice = graduatePapersNoticeService.get(resourceid);
			model.addAttribute("graduate", graduatePapersNotice);
			
			filelist = attachsService.findAttachsByFormId(graduatePapersNotice.getResourceid());
		}else{ //----------------------------------------新增
			model.addAttribute("graduate", new GraduatePapersNotice(user.getCnName(), user.getResourceid()));			
		}
		model.addAttribute("filelist", filelist);
		model.addAttribute("storeDir", user.getUsername());
		return "/edu3/teaching/graduateNotice/graduateNotice-form";
	}
	
	@RequestMapping("/edu3/framework/teaching/graduateNotice/view.html")
	public String exeView(String resourceid,HttpServletRequest request,HttpServletResponse response,ModelMap model)throws WebException{
		if(ExStringUtils.isNotEmpty(resourceid)){
			GraduatePapersNotice graduatePapersNotice = graduatePapersNoticeService.get(resourceid);
			List<Attachs> filelist = attachsService.findAttachsByFormId(graduatePapersNotice.getResourceid());
			model.addAttribute("filelist", filelist);
			model.addAttribute("graduatePapersNotice", graduatePapersNotice);
		}		
		return "/edu3/teaching/graduateNotice/graduateNotice-view";
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
	@RequestMapping("/edu3/teaching/graduateNotice/save.html")
	public void exeSave(GraduatePapersNotice graduate,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		String examSubId = request.getParameter("examSubId");
		String examSubName = request.getParameter("examSubName");
		try {
			User user = SpringSecurityHelper.getCurrentUser();
			String[] files = request.getParameterValues("uploadfileid");
			ExamSub sub = null;
			if(ExStringUtils.isNotEmpty(examSubId)){
				sub = examSubService.get(examSubId);
			}
			if(ExStringUtils.isNotBlank(graduate.getResourceid())){ //--------------------更新
				GraduatePapersNotice p_gradeate = graduatePapersNoticeService.get(graduate.getResourceid());
				
				graduate.setExamSub(sub);
				ExBeanUtils.copyProperties(p_gradeate, graduate);
				graduatePapersNoticeService.saveOrUpdate(p_gradeate, files, user);
			}else{ //-------------------------------------------------------------------保存
				graduate.setExamSub(sub);
				graduatePapersNoticeService.saveOrUpdate(graduate, files, user);
			}
			
			map.put("statusCode", 200);
			map.put("message", "保存成功！");
			map.put("navTabId", "RES_GRADUATE_DATA");
			map.put("reloadUrl", request.getContextPath() +"/edu3/teaching/graduateNotice/edit.html?resourceid="+graduate.getResourceid());
		}catch (Exception e) {
			if(e.getCause().toString().indexOf("ConstraintViolationException")>-1){
				logger.error("批次："+examSubName+"已经存在：{}",e.fillInStackTrace());
				map.put("message", "批次："+examSubName+"已经存在！");
			}else{
				logger.error("保存栏目出错：{}",e.fillInStackTrace());
				map.put("message", "保存失败！");
			}
			map.put("statusCode", 300);
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
	@RequestMapping("/edu3/teaching/graduateNotice/delete.html")
	public void exeDelete(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		String resourceid = request.getParameter("resourceid");
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(resourceid)){
				if(resourceid.split("\\,").length >1){//批量删除					
					graduatePapersNoticeService.batchCascadeDelete(resourceid.split("\\,"));
				}else{//单个删除
					graduatePapersNoticeService.delete(resourceid);
				}
				map.put("statusCode", 200);
				map.put("message", "删除成功！");				
				map.put("forward", request.getContextPath()+"/edu3/teaching/graduateNotice/list.html");
			}
		} catch (Exception e) {
			logger.error("删除栏目出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 选择批次
	 * @param objPage
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
//	@RequestMapping("/edu3/teaching/graduateNotice/chooseExam.html")
//	public String chooseExam(String yearId, String term, String type, Page objPage, HttpServletRequest request, ModelMap model) throws Exception{
//		objPage.setOrderBy("resourceid");
//		objPage.setOrder(Page.ASC);//设置默认排序方式
//		Map<String,Object> condition = new HashMap<String,Object>();//查询条件
//		String batchType = request.getParameter("batchType"); // 批次类型
//		String teacherId = request.getParameter("guidTeacherId"); // 教师ID
//		Page page = new Page();
//		if(ExStringUtils.isNotEmpty(teacherId)){
//			List list = graduatePapersNoticeService.findByHql("select examSub.resourceid from GraduateMentor where edumanager.resourceid=?", new String[]{teacherId});
//			if(!list.isEmpty()){
//				StringBuffer sb = new StringBuffer();
//				for (int i = 0; i < list.size(); i++) {
//					sb.append(",'");
//					sb.append(list.get(i));
//					sb.append("'");
//				}
//				condition.put("resourceid",sb.toString().substring(1));
//			}else{
//				condition.put("batchType", "no row!");
//			}
//			 page = examSubService.findExamSubByCondition(condition, objPage);
//		}else{
//			
//			if(ExStringUtils.isNotEmpty(yearId)) condition.put("yearId", yearId);
//			if(ExStringUtils.isNotEmpty(term)) condition.put("term", term);
//			if(ExStringUtils.isNotEmpty(batchType)) condition.put("batchType", batchType);
//			
//			 page = examSubService.findExamSubByCondition(condition, objPage);
//		}
//		
//		
//		model.addAttribute("examSubList", page);
//		model.addAttribute("condition", condition);
//		model.addAttribute("type", type);
//		model.addAttribute("batchType", batchType);
//		return "/edu3/teaching/graduateNotice/examSub1-list";
//	}
	
	/**
	 * 选择导师
	 * @param objPage
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/edu3/teaching/graduateNotice/chooseTeacher.html")
	public String chooseTeacher(String cnName, String teacherType, String type, Page objPage,  ModelMap model) throws Exception{
		objPage.setOrderBy("teacherCode");
		objPage.setOrder(Page.ASC);// 设置默认排序方式

		Map<String, Object> condition = new HashMap<String, Object>();// 查询条件
		if (ExStringUtils.isNotEmpty(cnName)) {
			condition.put("cnName", cnName);
		}
		
		if (ExStringUtils.isNotEmpty(teacherType)) {
			condition.put("teacherType", teacherType);
		}
		
		condition.put("roleCode", CacheAppManager.getSysConfigurationByCode("sysuser.role.teacher").getParamValue());
		condition.put("isDeleted", 0);
		Page edumanagerListPage = edumanagerService.findEdumanagerByCondition(condition, objPage);

		model.addAttribute("edumanagerListPage", edumanagerListPage);
		model.addAttribute("condition", condition);
		model.addAttribute("type", type);
		return "/edu3/teaching/graduateNotice/edumanager2-list";
	}

	@Autowired
	@Qualifier("graduatepapersnoticeservice")
	private IGraduatePapersNoticeService graduatePapersNoticeService;
	
	@Autowired
	@Qualifier("examSubService")
	private IExamSubService examSubService;
	
	@Autowired
	@Qualifier("edumanagerService")
	private IEdumanagerService edumanagerService;
	
	@Autowired
	@Qualifier("attachsService")
	IAttachsService attachsService;
	
	@Autowired
	@Qualifier("graduatepapersorderservice")
	private IGraduatePapersOrderService graduatePapersOrderService;
	
	
}
