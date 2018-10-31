package com.hnjk.edu.learning.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
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
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.HttpHeaderUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.BaseSupportController;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.Classic;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.basedata.service.IClassicService;
import com.hnjk.edu.learning.model.CourseExam;
import com.hnjk.edu.learning.model.CourseExamPaperDetails;
import com.hnjk.edu.learning.model.CourseExamPapers;
import com.hnjk.edu.learning.service.ICourseExamPaperDetailsService;
import com.hnjk.edu.learning.service.ICourseExamPapersService;
import com.hnjk.edu.learning.service.ICourseExamService;
import com.hnjk.edu.teaching.service.ICourseService;
import com.hnjk.platform.system.UserOperationLogsHelper;
import com.hnjk.platform.system.model.UserOperationLogs;
import com.hnjk.platform.taglib.JstlCustomFunction;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;
/**
 * 试卷管理
 * <code>CourseExamPapersController</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2011-4-13 上午11:24:32
 * @see 
 * @version 1.0
 */
@Controller
public class CourseExamPapersController extends BaseSupportController {

	private static final long serialVersionUID = -2964034477342944204L;
	
	@Autowired
	@Qualifier("courseExamPapersService")
	private ICourseExamPapersService courseExamPapersService;
	
	@Autowired
	@Qualifier("courseService")
	private ICourseService courseService;
	
	@Autowired
	@Qualifier("classicService")
	private IClassicService classicService;
	
	@Autowired
	@Qualifier("courseExamService")
	private ICourseExamService courseExamService;
	
	@Autowired
	@Qualifier("courseExamPaperDetailsService")
	private ICourseExamPaperDetailsService courseExamPaperDetailsService;
	
	/**
	 * 试卷列表
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/metares/courseexampapers/list.html")
	public String listCourseExamPapers(HttpServletRequest request,Page objPage, ModelMap model) throws WebException {
		objPage.setOrderBy("courseName,course.resourceid,classic.resourceid,fillinDate");
		objPage.setOrder(Page.DESC);//设置默认排序方式			
		
		String courseName = request.getParameter("courseName");
		String courseId = request.getParameter("courseId");
		String paperName = request.getParameter("paperName");	
		String paperType = request.getParameter("paperType");
		String classicid = request.getParameter("classicid");
		String isOpened = request.getParameter("isOpened");
				
		//查询条件
		Map<String,Object> condition = new HashMap<String,Object>();
		if(ExStringUtils.isNotEmpty(paperType)) {
			condition.put("paperType", paperType);			
		} 
		if("entrance_exam".equalsIgnoreCase(paperType)){//如果是入学考试
			courseId = "";
		} else {
			courseName = "";
		}
		if(ExStringUtils.isNotEmpty(courseName)) {
			condition.put("courseName", courseName);
		}
		if(ExStringUtils.isNotEmpty(courseId)) {
			condition.put("courseId", courseId);
		}
		if(ExStringUtils.isNotEmpty(paperName)) {
			condition.put("paperName", paperName);
		}
		if(ExStringUtils.isNotEmpty(classicid)) {
			condition.put("classicid", classicid);
		}
		if(ExStringUtils.isNotEmpty(isOpened)) {
			condition.put("isOpened", isOpened);
		}
		
		Page page = courseExamPapersService.findCourseExamPapersByCondition(condition, objPage);	
		
		model.addAttribute("courseExamPapersPage", page);
		model.addAttribute("condition", condition);
		return "/edu3/learning/courseexampapers/courseexampapers-list";
	}
	/**
	 * 新增编辑试卷
	 * @param resourceid
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/metares/courseexampapers/input.html")
	public String editCourseExamPapers(String resourceid,String act, ModelMap model) throws WebException{		
		CourseExamPapers courseExamPapers = null;
		if(ExStringUtils.isNotBlank(resourceid)){ //-----编辑
			courseExamPapers = courseExamPapersService.get(resourceid);
			
			if(ExStringUtils.isNotBlank(act) && "view".equals(act)){//查看
				model.addAttribute("courseExamPapers", courseExamPapers);
				
				Map<String,Integer> showOrders = new HashMap<String, Integer>();
				int index = 0;
				double score = 0.0;
				for (CourseExamPaperDetails detail : courseExamPapers.getCourseExamPaperDetails()) {
					if(!CourseExam.COMPREHENSION.equals(detail.getCourseExam().getExamType())){
						showOrders.put(detail.getResourceid(), ++index);
						score += detail.getScore();
					}
				}
				model.addAttribute("showOrders", showOrders);
				model.addAttribute("examCount", index);
				model.addAttribute("examScore", score);
				return "/edu3/learning/courseexampapers/courseexampapers-view";
			}
		}else{ //----------------------------------------新增
			courseExamPapers = new CourseExamPapers();
		}	
		model.addAttribute("courseExamPapers", courseExamPapers);
		return "/edu3/learning/courseexampapers/courseexampapers-form";
	}
	/**
	 * 保存
	 * @param courseExamPapers
	 * @param courseId
	 * @param classicid
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/metares/courseexampapers/save.html")
	public void saveCourseExamPapers(CourseExamPapers courseExamPapers,String courseId,String classicid,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			if(!"entrance_exam".equalsIgnoreCase(courseExamPapers.getPaperType()) //非入学考试
					&& ExStringUtils.isNotEmpty(courseId)){
				Course course  = courseService.load(courseId);
				courseExamPapers.setCourse(course);
				courseExamPapers.setCourseName(null);
			} else {
				courseExamPapers.setCourse(null);
			}
			if(ExStringUtils.isNotEmpty(classicid)){
				Classic classic = classicService.load(classicid);
				courseExamPapers.setClassic(classic);
			}
			boolean isUpdate = ExStringUtils.isNotBlank(courseExamPapers.getResourceid());
			if(ExStringUtils.isNotBlank(courseExamPapers.getResourceid())){ //-------------更新
				CourseExamPapers pre_courseExamPapers = courseExamPapersService.get(courseExamPapers.getResourceid());
				
				courseExamPapers.setCourse(courseExamPapers.getCourse());
				
				ExBeanUtils.copyProperties(pre_courseExamPapers,courseExamPapers);
				courseExamPapersService.update(pre_courseExamPapers);
			}else{ //-------------------------------------------------------------------保存
				User user = SpringSecurityHelper.getCurrentUser();
				courseExamPapers.setFillinDate(new Date());
				courseExamPapers.setFillinMan(user.getCnName());
				courseExamPapers.setFillinManId(user.getResourceid());
				
				courseExamPapersService.save(courseExamPapers);
			}
			map.put("statusCode", 200);
			map.put("message", "保存成功！");
			map.put("navTabId", "CourseExamPapers");
			map.put("reloadUrl", request.getContextPath() +"/edu3/metares/courseexampapers/input.html?resourceid="+courseExamPapers.getResourceid());
			
			UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), "9", isUpdate?UserOperationLogs.UPDATE:UserOperationLogs.INSERT, courseExamPapers);
		}catch (Exception e) {
			logger.error("保存试卷出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 删除试卷
	 * @param resourceid
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/metares/courseexampapers/remove.html")
	public void removeCourseExamPapers(String resourceid,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		try {			
			if(ExStringUtils.isNotBlank(resourceid)){			
				courseExamPapersService.batchDelete(resourceid.split("\\,"));
				map.put("statusCode", 200);
				map.put("message", "删除成功！");				
				map.put("forward", request.getContextPath()+"/edu3/metares/courseexampapers/list.html");
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), "9", UserOperationLogs.DELETE, "CourseExamPapers: "+resourceid);
			}
		} catch (Exception e) {
			logger.error("删除试卷出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 试卷可用试题列表
	 * @param paperId
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/metares/courseexampapers/courseexam/list.html")
	public String dialogCourseExam(String paperId,HttpServletRequest request,ModelMap model,Page objPage) throws WebException{		
		objPage.setOrderBy("examType,resourceid");
		objPage.setOrder(Page.ASC);
		if(ExStringUtils.isNotEmpty(paperId)){			
			String keywords = ExStringUtils.trimToEmpty(request.getParameter("keywords"));
			String examType = request.getParameter("examType");	
			String difficult = request.getParameter("difficult");
			String currentIndex = ExStringUtils.defaultIfEmpty(request.getParameter("currentIndex"), "0");
			
			//查询条件
			Map<String,Object> condition = new HashMap<String,Object>();
			if(ExStringUtils.isNotEmpty(keywords)) {
				condition.put("keywords", keywords);
			}
			if(ExStringUtils.isNotEmpty(examType)) {
				condition.put("examType", examType);
			}
			if(ExStringUtils.isNotEmpty(difficult)) {
				condition.put("difficult", difficult);
			}
			condition.put("currentIndex", currentIndex);
			
			CourseExamPapers paper = courseExamPapersService.get(paperId);
			model.addAttribute("courseExamPaper", paper);
			
			if("entrance_exam".equalsIgnoreCase(paper.getPaperType())){//入学考试
				condition.put("isEnrolExam", Constants.BOOLEAN_YES);
				condition.put("courseName", paper.getCourseName());
				condition.put("notInExamType", "('4','5')");
			} else {
				condition.put("isEnrolExam", Constants.BOOLEAN_NO);
				condition.put("courseId", paper.getCourse().getResourceid());
				condition.put("examform", paper.getPaperType());
			}
//			String existCourseExamIds = "";//试卷已存在的试题
//			for (CourseExamPaperDetails detail : paper.getCourseExamPaperDetails()) {
//				existCourseExamIds += detail.getCourseExam().getResourceid()+",";
//			}
//			model.addAttribute("existCourseExamIds", existCourseExamIds);
			
			condition.put("paperId", paperId);
			
			Page list = courseExamService.findCourseExamByCondition(condition,objPage);
			model.addAttribute("courseExamList", list);
			model.addAttribute("condition", condition);
		}
		return "/edu3/learning/courseexampapers/courseexam-list";
	}
	/**
	 * 保存试卷试题
	 * @param paperId
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/metares/courseexampapers/courseexam/save.html")
	public void savePapersCourseExam(String paperId,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();		
		try {
			if(ExStringUtils.isNotEmpty(paperId)){
				CourseExamPapers paper = courseExamPapersService.get(paperId);
				
				String from = request.getParameter("from");
				if(ExStringUtils.isNotEmpty(from)){ //--------------------更新
					String[] paperExamIds = request.getParameterValues("paperExamId");
					String[] showOrders = request.getParameterValues("showOrder");
					String[] scores = request.getParameterValues("score");
					
					if(null!=paperExamIds && paperExamIds.length>0){
						for (int i = 0; i < paperExamIds.length; i++) {
							CourseExamPaperDetails paperExam = courseExamPaperDetailsService.get(paperExamIds[i]);
							paperExam.setShowOrder(Integer.parseInt(ExStringUtils.defaultIfEmpty(showOrders[i], "0")));
							paperExam.setScore(Double.parseDouble(ExStringUtils.defaultIfEmpty(scores[i], "0")));
							
							paper.getCourseExamPaperDetails().add(paperExam);
						}
						courseExamPapersService.saveOrUpdate(paper);
						map.put("statusCode", 200);
						map.put("message", "保存成功！");
						map.put("reloadUrl", request.getContextPath() +"/edu3/metares/courseexampapers/courseexam/list.html?currentIndex=1&paperId="+paperId);
						UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), "9", UserOperationLogs.UPDATE, "CourseExamPaperDetails: "+ExStringUtils.join(paperExamIds,","));
					}	
				}else{ //------------------------------------------------添加
					String random = request.getParameter("random");	
					if(ExStringUtils.isNotEmpty(random)){ //随机生成
						randomPapersCourseExam(request, paper);
						courseExamPapersService.saveOrUpdate(paper);
						map.put("statusCode", 200);
						map.put("message", "保存成功！");
						UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), "9", UserOperationLogs.INSERT, new ArrayList<CourseExamPaperDetails>(paper.getCourseExamPaperDetails()));
					} else {
						String resourceid = request.getParameter("resourceid");	
						int nextShowOrder = paper.getNextShowOrder();
						if(ExStringUtils.isNotEmpty(resourceid)){
							List<CourseExamPaperDetails> addList = new ArrayList<CourseExamPaperDetails>();
							for (String id : resourceid.split(",")) {
								if(!paper.isExistCourseExam(id)){ //试题可以加入
									CourseExam courseExam = courseExamService.get(id);
									CourseExamPaperDetails detail = new CourseExamPaperDetails();
									detail.setCourseExam(courseExam);
									detail.setCourseExamPapers(paper);
									detail.setShowOrder(nextShowOrder++);
									detail.setScore(Double.parseDouble(ExStringUtils.defaultIfEmpty(request.getParameter("score"+courseExam.getExamType()), "0")));
									
									paper.getCourseExamPaperDetails().add(detail);
									addList.add(detail);
									
									if(CourseExam.COMPREHENSION.equals(courseExam.getExamType())){//材料题
										Set<CourseExam> set = courseExam.getChilds();
										double childscore = detail.getScore() / set.size();
										for (CourseExam child : set) {
											CourseExamPaperDetails d = new CourseExamPaperDetails();
											d.setCourseExam(child);
											d.setCourseExamPapers(paper);
											d.setShowOrder(nextShowOrder++);
											d.setScore(childscore);
											
											paper.getCourseExamPaperDetails().add(d);
										}
									}
								}
							}
							courseExamPapersService.saveOrUpdate(paper);
							map.put("statusCode", 200);
							map.put("message", "保存成功！");
							map.put("reloadUrl", request.getContextPath() +"/edu3/metares/courseexampapers/courseexam/list.html?currentIndex=0&paperId="+paperId);
							UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), "/edu3/metares/courseexampapers/courseexam/save.html", UserOperationLogs.INSERT, addList);
						}
					}
				}
			}						
		}catch (Exception e) {
			logger.error("保存试卷试题出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败！<br/>"+e.getLocalizedMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 随机生成试卷试题
	 * @param request
	 * @param paper
	 */
	private void randomPapersCourseExam(HttpServletRequest request, CourseExamPapers paper) throws WebException {
		Map<String,Object> condition = new HashMap<String,Object>();
		if("entrance_exam".equalsIgnoreCase(paper.getPaperType())){//入学考试
			condition.put("isEnrolExam", Constants.BOOLEAN_YES);
			condition.put("courseName", paper.getCourseName());
			condition.put("notInExamType", "('4','5')");
		} else {
			condition.put("isEnrolExam", Constants.BOOLEAN_NO);
			condition.put("courseId", paper.getCourse().getResourceid());
			condition.put("examform", paper.getPaperType());
		}
		Set<String> existCourseExamIds = new HashSet<String>(0);//试卷已存在的试题
		for (CourseExamPaperDetails detail : paper.getCourseExamPaperDetails()) {
			existCourseExamIds.add(detail.getCourseExam().getResourceid());
		}
		if(!existCourseExamIds.isEmpty()) {
			condition.put("existCourseExamIds", "'"+ExStringUtils.join(existCourseExamIds, "','")+"'");
		}
				
		int nextShowOrder = paper.getNextShowOrder();
		String[] examNodeTypes = request.getParameterValues("examNodeType");
		String[] examTypes = request.getParameterValues("examType");
		String[] nums = request.getParameterValues("num");
		String[] scores = request.getParameterValues("score");
		
		if(null!=examNodeTypes && examNodeTypes.length>0){
			for (int i = 0; i < examNodeTypes.length; i++) {
				if(ExStringUtils.isNotBlank(nums[i]) && ExStringUtils.isNotBlank(scores[i])){
					int examnum = Integer.parseInt(ExStringUtils.trimToEmpty(nums[i]));
					double score = Double.parseDouble(ExStringUtils.trimToEmpty(scores[i]));
					if(examnum > 0 && score > 0){
						if(ExStringUtils.isNotBlank(examNodeTypes[i])){
							condition.put("examNodeType", examNodeTypes[i]);
						}
						condition.put("examType", examTypes[i]);
						List<CourseExam> list = courseExamService.findCourseExamByCondition(condition);
						if(null != list){
							if(examnum > list.size()){
								throw new WebException(JstlCustomFunction.dictionaryCode2Value("CodeExamNodeType", examNodeTypes[i])+" - "+JstlCustomFunction.dictionaryCode2Value("CodeExamType", examTypes[i])+":填写题目数大于可用数");
							}
							Collections.shuffle(list);//打乱顺序，取前examnum个
							for (int j = 0; j < examnum; j++) {
								CourseExamPaperDetails detail = new CourseExamPaperDetails();
								detail.setCourseExam(list.get(j));
								detail.setCourseExamPapers(paper);
								detail.setShowOrder(nextShowOrder++);
								detail.setScore(score);
								
								paper.getCourseExamPaperDetails().add(detail);
								
								if(CourseExam.COMPREHENSION.equals(list.get(j).getExamType())){//材料题
									Set<CourseExam> set = list.get(j).getChilds();
									if(set.size()>0){
										double childscore = score / set.size();
										for (CourseExam child : set) {
											CourseExamPaperDetails d = new CourseExamPaperDetails();
											d.setCourseExam(child);
											d.setCourseExamPapers(paper);
											d.setShowOrder(nextShowOrder++);
											d.setScore(childscore);
											
											paper.getCourseExamPaperDetails().add(d);
										}
									}
								}
							}
						}
					}
				}
			}
		}
		
	}
	/**
	 * 成卷选择界面
	 * @param paperId
	 * @param request
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/framework/courseexampapers/random.html")
	public String dialogCourseExamPapers(String paperId,HttpServletRequest request,ModelMap model) throws WebException{		
		if(ExStringUtils.isNotEmpty(paperId)){
			CourseExamPapers paper = courseExamPapersService.get(paperId);
			Map<String,Object> condition = new HashMap<String,Object>();
			if("entrance_exam".equalsIgnoreCase(paper.getPaperType())){//入学考试
				condition.put("isEnrolExam", Constants.BOOLEAN_YES);
				condition.put("courseName", paper.getCourseName());
				condition.put("notInExamType", "('4','5')");
			} else {
				condition.put("isEnrolExam", Constants.BOOLEAN_NO);
				condition.put("courseId", paper.getCourse().getResourceid());
				condition.put("examform", paper.getPaperType());
			}
			List<Map<String, Object>> list = courseExamService.getCourseExamTypeAndCount(condition);
			
			model.addAttribute("courseExamPaper", paper);
			model.addAttribute("courseExamTypeAndCounts", list);
		}
		return "/edu3/learning/courseexampapers/courseexampapers-random";
	}
	
	/**
	 * 删除试卷试题
	 * @param resourceid
	 * @param paperId
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/metares/courseexampapers/courseexam/remove.html")
	public void removePapersCourseExam(String resourceid,String paperId,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		try {			
			if(ExStringUtils.isNotEmpty(paperId) && ExStringUtils.isNotBlank(resourceid) ){			
				courseExamPaperDetailsService.batchDelete(resourceid.split("\\,"));
				map.put("statusCode", 200);
				map.put("message", "删除成功！");		
				map.put("reloadUrl", request.getContextPath() +"/edu3/metares/courseexampapers/courseexam/list.html?currentIndex=1&paperId="+paperId);
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), "9", UserOperationLogs.DELETE, "CourseExamPaperDetails: "+resourceid);
			}
		} catch (Exception e) {
			logger.error("删除出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}	
}
