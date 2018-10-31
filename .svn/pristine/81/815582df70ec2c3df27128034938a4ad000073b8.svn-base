package com.hnjk.edu.teaching.controller;

import java.util.Date;
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
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.FileUploadAndDownloadSupportController;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.basedata.model.YearInfo;
import com.hnjk.edu.basedata.service.IYearInfoService;
import com.hnjk.edu.learning.model.CourseNotice;
import com.hnjk.edu.learning.service.ICourseNoticeService;
import com.hnjk.edu.roll.model.Classes;
import com.hnjk.edu.roll.service.IClassesService;
import com.hnjk.edu.teaching.service.ICourseService;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.Attachs;
import com.hnjk.platform.system.service.IAttachsService;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;

/**
 * 课程公告管理
 * <code>CourseNoticeController</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-9-2 上午11:36:27
 * @see 
 * @version 1.0
 */
@Controller
public class CourseNoticeController extends FileUploadAndDownloadSupportController {

	private static final long serialVersionUID = 4327027817850661513L;
	
	@Autowired
	@Qualifier("courseService")
	private ICourseService courseService;
	
	@Autowired
	@Qualifier("classesService")
	private IClassesService classesService;
	
	@Autowired
	@Qualifier("courseNoticeService")
	private ICourseNoticeService courseNoticeService;	
	
	@Autowired
	@Qualifier("yearInfoService")
	private IYearInfoService yearInfoService;
	
//	@Autowired
//	@Qualifier("teachtaskservice")
//	private ITeachTaskService teachTaskService;
	
	@Autowired
	@Qualifier("attachsService")
	private IAttachsService attachsService;
	
	/**
	 * 课程公告列表
	 * @param courseId
	 * @param noticeTitle
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/coursenotice/list.html")
	public String listCourseNotice(String courseId,String noticeTitle,String yearInfoId,String term, String classesId,Page objPage, ModelMap model) throws WebException{
		objPage.setOrderBy("course.courseCode,fillinDate");
		objPage.setOrder(Page.DESC);//设置默认排序方式
		
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件
		User user = SpringSecurityHelper.getCurrentUser();
		
		try {
			if(ExStringUtils.isNotEmpty(courseId)){
				condition.put("courseId", courseId);
			}
			if(ExStringUtils.isNotEmpty(noticeTitle)){
				condition.put("noticeTitle", noticeTitle);
			}
			if(ExStringUtils.isNotEmpty(yearInfoId)){
				condition.put("yearInfoId", yearInfoId);
			}
			if(ExStringUtils.isNotEmpty(term)){
				condition.put("term", term);
			}
			if(ExStringUtils.isNotEmpty(classesId)){
				condition.put("classesId", classesId);
			}
			
			if(!SpringSecurityHelper.isUserInRole("ROLE_BRS_STUDENTSTATUS")){// 不是教务员
				String classesIds = classesService.findByMasterId(user.getResourceid());
				if(ExStringUtils.isNotEmpty(classesIds)){
					condition.put("classesIds",classesIds);
				}
				
				if(SpringSecurityHelper.isUserInRole(CacheAppManager.getSysConfigurationByCode("sysuser.role.teacher").getParamValue())){//老师
					condition.put("teacherId", user.getResourceid());
				}
			}
			
			Page courseNoticePage = courseNoticeService.findCourseNoticeByCondition(condition, objPage);
			
			model.addAttribute("courseNoticePage", courseNoticePage);
			model.addAttribute("condition", condition);
			
			String classesCondition = "";
			if (CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())
					|| SpringSecurityHelper.isTeachingCentreTeacher(user)) {
//			classesCondition = "brSchool.resourceid='" + curUser.getOrgUnit().getResourceid() + "'";
				if(!SpringSecurityHelper.isUserInRole("ROLE_BRS_STUDENTSTATUS")){
					classesCondition += " and (resourceid in (select tt.classes.resourceid from TeachingPlanCourseTimetable tt where tt.teacherId='"+user.getResourceid()+"') "
											   + " or classesmasterid='"+user.getResourceid()+"')";
				}
			}else{
				model.addAttribute("isadmin", "Y");
//			if(ExStringUtils.isNotEmpty(branchSchool)){
//				classesCondition = "brSchool.resourceid='" + branchSchool + "'";
//			}
			}
			model.addAttribute("classesCondition", classesCondition);
		} catch (Exception e) {
			logger.error("课程公告列表出错：", e);
		} 
		
		return "/edu3/teaching/coursenotice/coursenotice-list";
	}
	
	/**
	 * 新增编辑课程公告
	 * @param courseId
	 * @param resourceid
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/coursenotice/input.html")
	public String editCourseNotice(String courseId, String resourceid, String yearInfoId,String term, ModelMap model) throws WebException{
		CourseNotice courseNotice = null;
		User user = SpringSecurityHelper.getCurrentUser();
		model.addAttribute("storeDir", user.getUsername());
		if(ExStringUtils.isNotBlank(resourceid)){ //-----编辑
			courseNotice = courseNoticeService.load(resourceid);	
			List<Attachs> attachs = attachsService.findAttachsByFormId(resourceid);
			courseNotice.setAttachs(attachs);
		}else{ //----------------------------------------新增			
			courseNotice = new CourseNotice();
			if(SpringSecurityHelper.isUserInRole(CacheAppManager.getSysConfigurationByCode("sysuser.role.teacher").getParamValue())){//老师
				//TeachTask task = teachTaskService.findLastTeachTaskByTeacherId(user.getResourceid());
//				List<TeachTask> list = teachTaskService.findByHql(" from "+TeachTask.class.getSimpleName()+" where isDeleted=? and taskStatus=3 and (teacherId like ? or assistantIds like ? ) order by yearInfo.firstYear desc,term desc ", 0,"%"+user.getResourceid()+"%","%"+user.getResourceid()+"%");
//				if(list !=null && list.size()==1 ){
//					courseNotice.setCourse(list.get(0).getCourse());
//					courseNotice.setYearInfo(list.get(0).getYearInfo());
//					courseNotice.setTerm(list.get(0).getTerm());
//				}
			}			
		}
		model.addAttribute("courseNotice", courseNotice);		
		
		long year  = ExDateUtils.getCurrentYear();
		long startYear = year-10;
		model.addAttribute("year", year);		
		model.addAttribute("startYear", startYear);	
		
		String classesCondition = "";
		User curUser 		 = SpringSecurityHelper.getCurrentUser();
		if (CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(curUser.getOrgUnit().getUnitType())
				|| SpringSecurityHelper.isTeachingCentreTeacher(curUser)) {
//			classesCondition = "brSchool.resourceid='" + curUser.getOrgUnit().getResourceid() + "'";
			if(!SpringSecurityHelper.isUserInRole("ROLE_BRS_STUDENTSTATUS")){
				classesCondition += " and resourceid in (select tt.classes.resourceid from TeachingPlanCourseTimetable tt where tt.teacherId='"+curUser.getResourceid()+"')";
			}
		}else{
			model.addAttribute("isadmin", "Y");
//			if(ExStringUtils.isNotEmpty(branchSchool)){
//				classesCondition = "brSchool.resourceid='" + branchSchool + "'";
//			}
		}
		model.addAttribute("classesCondition", classesCondition);
		
		return "/edu3/teaching/coursenotice/coursenotice-form";
	}
	/**
	 * 保存课程公告
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/coursenotice/save.html")
	public void saveCourseNotice(HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			String courseId = request.getParameter("courseId");
			String noticeTitle = request.getParameter("noticeTitle");
			String noticeContent = request.getParameter("noticeContent");
			String resourceid = request.getParameter("resourceid");
			String yearInfoId = request.getParameter("yearInfoId");
			String term = request.getParameter("term");
			String classesId = request.getParameter("classesId");
			String[] uploadfileids = request.getParameterValues("uploadfileid");
			
			YearInfo yearInfo = null;
			if(ExStringUtils.isNotEmpty(yearInfoId)){
				yearInfo = yearInfoService.load(yearInfoId);
			}
			Classes classes = null;
			if(ExStringUtils.isNotEmpty(classesId)){
				classes = classesService.load(classesId);
			}
			CourseNotice courseNotice = new CourseNotice();
			if(ExStringUtils.isNotBlank(resourceid)){ //--------------------更新
				courseNotice = courseNoticeService.load(resourceid);
				courseNotice.setNoticeTitle(noticeTitle);
				courseNotice.setNoticeContent(noticeContent);
				courseNotice.setTerm(term);
				courseNotice.setYearInfo(yearInfo);
				courseNotice.setIsPersisted(true);
				courseNotice.setClasses(classes);
				courseNoticeService.saveOrUpdateCourseNotice(courseNotice,uploadfileids);
			}else{ //-------------------------------------------------------------------保存
				Course course = courseService.load(courseId);
				courseNotice.setTerm(term);
				courseNotice.setYearInfo(yearInfo);
				User user = SpringSecurityHelper.getCurrentUser();				
				courseNotice.setCourse(course);
				courseNotice.setNoticeTitle(noticeTitle);
				courseNotice.setNoticeContent(noticeContent);
				courseNotice.setFillinDate(new Date());
				courseNotice.setFillinMan(user.getCnName());
				courseNotice.setFillinManId(user.getResourceid());
				courseNotice.setClasses(classes);
				courseNoticeService.saveOrUpdateCourseNotice(courseNotice,uploadfileids);
			}
			map.put("statusCode", 200);
			map.put("message", "保存成功！");
			map.put("navTabId", "RES_TEACHING_COURSENOTICE_INPUT");
			map.put("reloadUrl", request.getContextPath() +"/edu3/teaching/coursenotice/input.html?resourceid="+courseNotice.getResourceid());
		}catch (Exception e) {
			logger.error("保存课程公告出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 删除课程公告
	 * @param resourceid
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/coursenotice/delete.html")
	public void removeCourseNotice(String resourceid,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(resourceid)){				
				courseNoticeService.batchCascadeDelete(resourceid.split("\\,"));				
				map.put("statusCode", 200);
				map.put("message", "删除成功！");				
				map.put("forward", request.getContextPath()+"/edu3/teaching/coursenotice/list.html");
			}
		} catch (Exception e) {
			logger.error("删除课程公告出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}

}
