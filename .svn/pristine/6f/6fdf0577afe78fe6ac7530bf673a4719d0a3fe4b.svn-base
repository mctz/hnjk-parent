package com.hnjk.edu.learning.controller;

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
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.HttpHeaderUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.BaseSupportController;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.learning.model.CourseMockTest;
import com.hnjk.edu.learning.service.ICourseMockTestService;
import com.hnjk.edu.teaching.service.ICourseService;
import com.hnjk.platform.system.UserOperationLogsHelper;
import com.hnjk.platform.system.model.UserOperationLogs;
/**
 * 课程模拟试题管理
 * <code>CourseMockTestController</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-11-9 下午05:02:03
 * @see 
 * @version 1.0
 */
@Controller
public class CourseMockTestController extends BaseSupportController {

	private static final long serialVersionUID = -6317459489439793629L;
	
	@Autowired
	@Qualifier("courseMockTestService")
	private ICourseMockTestService courseMockTestService;
	
	@Autowired
	@Qualifier("courseService")
	private ICourseService courseService;
	
	/**
	 * 模拟试题列表
	 * @param courseId
	 * @param resourceid
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/learning/coursemocktest/list.html")
	public String listCourseMockTest(String courseId,String resourceid,Page objPage, ModelMap model) throws WebException{
		objPage.setOrderBy("resourceid");
		objPage.setOrder(Page.ASC);//设置默认排序方式
		
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件
		if(ExStringUtils.isNotEmpty(courseId)) {
			condition.put("courseId", courseId);
		}
		
		Page courseMockTestPage = courseMockTestService.findCourseMockTestByCondition(condition, objPage);
		
		model.addAttribute("courseMockTestPage", courseMockTestPage);
		model.addAttribute("condition", condition);
		
		
		CourseMockTest courseMockTest = null;
		if(ExStringUtils.isNotBlank(resourceid)){ //-----编辑
			courseMockTest = courseMockTestService.get(resourceid);			
		}else{ //----------------------------------------新增
			Course course = courseService.get(courseId);
			courseMockTest = new CourseMockTest();
			courseMockTest.setCourse(course);
		}
		model.addAttribute("courseMockTest", courseMockTest);		
		return "/edu3/learning/coursemocktest/coursemocktest-list";
	}
	/**
	 * 保存模拟试题
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/learning/coursemocktest/save.html")
	public void saveCourseMockTest(HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			String courseId = request.getParameter("courseId");			
			String resourceid = request.getParameter("resourceid");
			Course course = courseService.get(courseId);
			if(ExStringUtils.isNotBlank(resourceid)){ //--------------------更新
				String mateUrl = request.getParameter("mateUrl");
				String mocktestName = request.getParameter("mocktestName");
				//String netMeateUrl = request.getParameter("netMeateUrl");				
				CourseMockTest courseMockTest = courseMockTestService.get(resourceid);
				courseMockTest.setMocktestName(mocktestName);
				courseMockTest.setMateUrl(mateUrl);
				//courseMockTest.setNetMeateUrl(netMeateUrl);
				courseMockTestService.update(courseMockTest);	
				
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), "9", UserOperationLogs.UPDATE, courseMockTest);
			}else{ //-------------------------------------------------------保存
				String[] mateUrls = request.getParameterValues("mateUrl");
				String[] mocktestNames = request.getParameterValues("mocktestName");
				//String[] netMeateUrls = request.getParameterValues("netMeateUrl");
				if(mocktestNames!=null&&mocktestNames.length>0&&mateUrls!=null&&mateUrls.length>0){		
					List<CourseMockTest> list = new ArrayList<CourseMockTest>();
					for (int i = 0; i < mocktestNames.length; i++) {
						if(ExStringUtils.isNotEmpty(mocktestNames[i])&&ExStringUtils.isNotEmpty(mateUrls[i])){
							CourseMockTest courseMockTest = new CourseMockTest();
							courseMockTest.setMateUrl(mateUrls[i]);
							courseMockTest.setMocktestName(mocktestNames[i]);
							//courseMockTest.setNetMeateUrl(netMeateUrls[i]);
							courseMockTest.setCourse(course);
							list.add(courseMockTest);
						}
					}
					courseMockTestService.batchSaveOrUpdate(list);
					UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), "9", UserOperationLogs.INSERT, list);
				}
			}
			String pageNum = ExStringUtils.defaultIfEmpty(request.getParameter("pageNum"), "1");
			String pageSize = ExStringUtils.defaultIfEmpty(request.getParameter("pageSize"), "1");
			map.put("statusCode", 200);
			map.put("message", "保存成功！");
			map.put("navTabId", "courseMockTest");
			map.put("reloadUrl", request.getContextPath() +"/edu3/learning/coursemocktest/list.html?courseId="+course.getResourceid()+"&pageSize="+pageSize+"&pageNum="+pageNum);
		}catch (Exception e) {
			logger.error("保存课程模拟试题出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 删除模拟试题
	 * @param resourceid
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/learning/coursemocktest/remove.html")
	public void removeCourseMockTest(String resourceid,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(resourceid)){				
				courseMockTestService.batchCascadeDelete(resourceid.split("\\,"));				
				map.put("statusCode", 200);
				map.put("message", "删除成功！");				
				map.put("forward", request.getContextPath()+"/edu3/learning/coursemocktest/list.html");
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), "9", UserOperationLogs.DELETE, "CourseMockTest: "+resourceid);
			}
		} catch (Exception e) {
			logger.error("删除课程模拟试题出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}	

}
