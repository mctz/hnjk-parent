package com.hnjk.edu.learning.controller;

import java.util.HashMap;
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
import com.hnjk.edu.learning.model.BbsSection;
import com.hnjk.edu.learning.service.IBbsSectionService;
import com.hnjk.security.service.IUserService;

/**
 * 课程论坛版块管理
 * <code>BbsSectionController</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-9-6 下午04:37:59
 * @see 
 * @version 1.0
 */
@Controller
public class BbsSectionController extends BaseSupportController {
	private static final long serialVersionUID = -7440660444879281301L;

	@Autowired
	@Qualifier("bbsSectionService")
	private IBbsSectionService bbsSectionService;
	
	@Autowired
	@Qualifier("userService")
	private IUserService userService;
	
	
	/**
	 * 课程论坛版块列表
	 * @param sectionName
	 * @param masterName
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/metares/bbssection/list.html")
	public String listBbsSection(String sectionName,String masterName,String bbsSectionId, String isCourseSection, Page objPage, ModelMap model) throws WebException{
		objPage.setOrderBy("isCourseSection desc,sectionLevel,parent,showOrder");
		objPage.setOrder(Page.ASC);//设置默认排序方式		
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件
		
		if(ExStringUtils.isNotEmpty(sectionName)) {
			condition.put("sectionName", sectionName);
		}
		if(ExStringUtils.isNotEmpty(masterName)) {
			condition.put("masterName", masterName);
		}
		if(ExStringUtils.isNotEmpty(bbsSectionId)) {
			condition.put("bbsSectionId", bbsSectionId);
		}
		if(ExStringUtils.isNotEmpty(isCourseSection)) {
			condition.put("isCourseSection", isCourseSection);
		}
		
		Page bbsSectionListPage = bbsSectionService.findBbsSectionByCondition(condition, objPage);
		
		model.addAttribute("bbsSectionListPage", bbsSectionListPage);
		model.addAttribute("condition", condition);
	
		return "/edu3/learning/bbssection/bbssection-list";
	}
	
	/**
	 * 新增编辑课程论坛版块
	 * @param resourceid
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/metares/bbssection/input.html")
	public String editBbsSection(String resourceid, ModelMap model) throws WebException {
		BbsSection bbsSection = null;
		if(ExStringUtils.isNotBlank(resourceid)){ //-----编辑
			bbsSection = bbsSectionService.get(resourceid);	
		}else{ //----------------------------------------新增
			bbsSection = new BbsSection();	
		}
		model.addAttribute("bbsSection", bbsSection);	
//		String hql = " from "+BbsSection.class.getSimpleName()+" where isDeleted=0 and parent is null ";
//		Map<String, Object> map = bbsSectionService.getSectionList(hql);
//		model.addAttribute("parentBbsSections", map);
		return "/edu3/learning/bbssection/bbssection-form";
	}
	
	/**
	 * 保存课程论坛版块
	 * @param bbsSection
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/metares/bbssection/save.html")
	public void saveBbsSection(BbsSection bbsSection,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		try {		
			String parentId = request.getParameter("parentId");
			BbsSection parent = null;
			if(ExStringUtils.isNotEmpty(parentId)) {
				parent = bbsSectionService.get(parentId);
			}
			bbsSection.setParent(parent);
			if(bbsSection.getParent()!=null){
				bbsSection.setSectionLevel(bbsSection.getParent().getSectionLevel()+1);
				bbsSection.setIsCourseSection(bbsSection.getParent().getIsCourseSection());
			}
			if(ExStringUtils.isNotBlank(bbsSection.getResourceid())){ //--------------------更新
				BbsSection p_bbsSection = bbsSectionService.get(bbsSection.getResourceid());
				ExBeanUtils.copyProperties(p_bbsSection, bbsSection);
				bbsSectionService.update(p_bbsSection);
			}else{ //-------------------------------------------------------------------保存
				bbsSectionService.save(bbsSection);
			}
			map.put("statusCode", 200);
			map.put("message", "保存成功！");
			map.put("navTabId", "RES_METARES_BBS_BBSSECTION_EDIT");
			map.put("reloadUrl", request.getContextPath() +"/edu3/metares/bbssection/input.html?resourceid="+bbsSection.getResourceid());
		}catch (Exception e) {
			logger.error("保存课程论坛版块出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败！");
		}		
		renderJson(response, JsonUtils.mapToJson(map));
	}

	/**
	 * 删除课程论坛版块
	 * @param resourceid
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/metares/bbssection/remove.html")
	public void removeBbsSection(String resourceid,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		try {			
			if(ExStringUtils.isNotBlank(resourceid)){			
				bbsSectionService.deleteBbsSection(resourceid.split("\\,"));
				map.put("statusCode", 200);
				map.put("message", "删除成功！");				
				map.put("forward", request.getContextPath()+"/edu3/metares/bbssection/list.html");
			}
		} catch (Exception e) {
			logger.error("删除课程论坛版块出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 检查版块编码的唯一性
	 * @param sectionCode
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/metares/bbssection/validateCode.html")
	public void validateBbsSectionCode(String sectionCode,HttpServletResponse response) throws WebException{
		renderJson(response, Boolean.toString(bbsSectionService.isExistsSectionCode(sectionCode)));
	}
	
	/**
	 * 版主选择对话框
	 * @param objPage
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/edu3/metares/bbssection/master.html")
	public String nodePicker(Page objPage,HttpServletRequest request,HttpServletResponse response) throws Exception{
		objPage.setOrderBy("resourceid");
		objPage.setOrder(Page.ASC);//设置默认排序方式
		//查询条件
		Map<String,Object> condition = new HashMap<String,Object>();
		String userName=request.getParameter("userName");//登录名
		String cnName=request.getParameter("cnName");//姓名
		String unitId=request.getParameter("unitId");
		String userType=request.getParameter("userType");
		
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
		
		Page page = userService.findUserByCondition(condition, objPage);
		
		request.setAttribute("userlist", page);
		
		String idsN = request.getParameter("idsN");
		String namesN = request.getParameter("namesN");
		if(ExStringUtils.isNotEmpty(idsN)) {
			condition.put("idsN", idsN);
		}
		if(ExStringUtils.isNotEmpty(namesN)) {
			condition.put("namesN", namesN);
		}
		request.setAttribute("condition", condition);
		return "/edu3/learning/bbssection/selector_masters";
	}
	
	/**
	 * 获取排序号
	 * @param parentId
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/metares/bbssection/getShowOrder.html")
	public void getShowOrder(String parentId,HttpServletResponse response) throws WebException{
		renderJson(response, bbsSectionService.getNextShowOrder(parentId).toString());
	}
}
