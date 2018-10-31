package com.hnjk.edu.roll.controller;

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
import com.hnjk.edu.roll.model.GraduateData;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.roll.service.IGraduateDataService;
import com.hnjk.edu.roll.service.IStudentInfoService;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;

/**
 * 毕业生数据表.
 * <code>GraduateDataController</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2010-10-28 下午04:09:56
 * @see 
 * @version 1.0
 */
@Controller
public class GraduateDataController extends BaseSupportController{

	private static final long serialVersionUID = 7815374415210912967L;
	
	/*
	@RequestMapping("/edu3/teaching/graduateData/list.html")

	public String exeList(String branchSchool, String grade, String major, String name, String studyNo, String classic, Page objPage, ModelMap model) throws WebException{
		objPage.setOrderBy("graduateDate");
		objPage.setOrder(Page.ASC);//设置默认排序方式
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件
		
		if(ExStringUtils.isNotEmpty(branchSchool)) condition.put("branchSchool", branchSchool);
		if(ExStringUtils.isNotEmpty(grade)) condition.put("grade", grade);
		if(ExStringUtils.isNotEmpty(major)) condition.put("major", major);
		if(ExStringUtils.isNotEmpty(name)) condition.put("name", name);
		if(ExStringUtils.isNotEmpty(studyNo)) condition.put("studyNo", studyNo);
		if(ExStringUtils.isNotEmpty(classic)) condition.put("classic", classic);
		
		String center = "show";
		User cureentUser = SpringSecurityHelper.getCurrentUser();
				
		if(cureentUser.getOrgUnit().getUnitType().
				indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){
			branchSchool = cureentUser.getOrgUnit().getResourceid();
			condition.put("branchSchool", branchSchool);
			center = "hide";
		}

		Page page = graduateDataService.findGraduateDataByCondition(condition, objPage);
		
		model.addAttribute("condition", condition);
		model.addAttribute("showCenter", center);
		model.addAttribute("graduateList", page);
		return "/edu3/teaching/graduateData/graduateData-list";
	}
	*/
	/**
	 * 新增编辑表单
	 * @param resourceid
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/graduateData/edit.html")
	public String exeEdit(String resourceid,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		if(ExStringUtils.isNotBlank(resourceid)){ //-----编辑
			GraduateData graduateData = graduateDataService.get(resourceid);	
			model.addAttribute("graduate", graduateData);
		}else{ //----------------------------------------新增
			model.addAttribute("graduate", new GraduateData());			
		}
		return "/edu3/teaching/graduateData/graduateData-form";
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
	@RequestMapping("/edu3/teaching/graduateData/save.html")
	public void exeSave(GraduateData graduateData,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		String stuNum = request.getParameter("stuNum");
		StudentInfo info = null;
		if(ExStringUtils.isNotEmpty(stuNum)){
			String hql = "from StudentInfo s where s.studyNo=? and isDeleted=0";
			info = studentInfoService.findUnique(hql, new Object[]{stuNum});
		}
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(graduateData.getResourceid())){ //--------------------更新
				GraduateData p_gradeate = graduateDataService.get(graduateData.getResourceid());
				
				if(null!=info) {
					graduateData.setStudentInfo(info);
				}
				ExBeanUtils.copyProperties(p_gradeate, graduateData);
				graduateDataService.update(p_gradeate);
			}else{ //-------------------------------------------------------------------保存
				if(null!=info) {
					graduateData.setStudentInfo(info);
				}
				graduateDataService.save(graduateData);
			}
			map.put("statusCode", 200);
			map.put("message", "保存成功！");
			map.put("navTabId", "RES_GRADUATE_DATA");
			map.put("reloadUrl", request.getContextPath() +"/edu3/teaching/graduateData/edit.html?resourceid="+graduateData.getResourceid());
		}catch (Exception e) {
			if(e.getCause().toString().indexOf("ConstraintViolationException")>-1){
				logger.error("学号："+stuNum+"已经存在：{}",e.fillInStackTrace());
				map.put("message", "学号："+stuNum+"已经存在！");
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
	@RequestMapping("/edu3/teaching/graduateData/delete.html")
	public void exeDelete(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		String resourceid = request.getParameter("resourceid");
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(resourceid)){
				if(resourceid.split("\\,").length >1){//批量删除					
					graduateDataService.batchCascadeDelete(resourceid.split("\\,"));
				}else{//单个删除
					graduateDataService.delete(resourceid);
				}
				map.put("statusCode", 200);
				map.put("message", "删除成功！");				
				map.put("forward", request.getContextPath()+"/edu3/teaching/graduateData/list.html");
			}
		} catch (Exception e) {
			logger.error("删除栏目出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	

	@Autowired
	@Qualifier("graduatedataservice")
	private IGraduateDataService graduateDataService;
	
	@Autowired
	@Qualifier("studentinfoservice")
	private IStudentInfoService studentInfoService;

}
