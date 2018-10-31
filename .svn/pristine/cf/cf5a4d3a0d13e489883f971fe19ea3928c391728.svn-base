package com.hnjk.edu.teaching.controller;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.HttpHeaderUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.FileUploadAndDownloadSupportController;
import com.hnjk.edu.basedata.model.Major;
import com.hnjk.edu.basedata.service.IClassicService;
import com.hnjk.edu.basedata.service.IGradeService;
import com.hnjk.edu.basedata.service.IMajorService;
import com.hnjk.edu.roll.model.Classes;
import com.hnjk.edu.roll.service.IClassesService;
import com.hnjk.edu.teaching.model.LinkageQuery;
import com.hnjk.edu.teaching.service.ILinkageQueryService;
import com.hnjk.edu.util.Condition2SQLHelper;
import com.hnjk.platform.system.UserOperationLogsHelper;
import com.hnjk.platform.system.model.UserOperationLogs;
import com.hnjk.security.model.OrgUnit;
import com.hnjk.security.service.IOrgUnitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 联动查询Controller
 * 
 * @author zik, 广东学苑教育发展有限公司
 *
 */
@Controller
public class LinkageQueryController extends FileUploadAndDownloadSupportController {

	private static final long serialVersionUID = 3976860153802906766L;
	
	private static final Logger logger = LoggerFactory.getLogger(LinkageQueryController.class);
	
	@Qualifier("linkageQueryService")
	@Autowired
	private ILinkageQueryService linkageQueryService;
	
	@Autowired
	@Qualifier("orgUnitService")
	private IOrgUnitService orgUnitService;
	
	@Autowired
	@Qualifier("gradeService")
	private IGradeService gradeService;
	
	@Autowired
	@Qualifier("majorService")
	private IMajorService majorService;
	
	@Autowired
	@Qualifier("classesService")
	private IClassesService classesService;
	
	@Autowired
	@Qualifier("classicService")
	private IClassicService classicService;
	
	
	/**
	 * 联动查询列表
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/linkageQuery/list.html")
	public String listLinkageQuery(HttpServletRequest request, HttpServletResponse response, Page page, ModelMap model) throws WebException {
		Map<String, Object> condition = new HashMap<String, Object>();
		try {
			// 参数处理
			String brSchoolId = request.getParameter("brSchoolId");
			String gradeId = request.getParameter("gradeId");
			String classicId = request.getParameter("classicId");
			String teachingType = request.getParameter("teachingType");
			String majorId = request.getParameter("majorId");
			
			if(ExStringUtils.isNotEmpty(brSchoolId)){
				condition.put("brSchoolId", brSchoolId);
			}
			if(ExStringUtils.isNotEmpty(gradeId)){
				condition.put("gradeId", gradeId);
			}
			if(ExStringUtils.isNotEmpty(classicId)){
				condition.put("classicId", classicId);
			}
			if(ExStringUtils.isNotEmpty(teachingType)){
				condition.put("teachingType", teachingType);
			}
			if(ExStringUtils.isNotEmpty(majorId)){
				condition.put("majorId", majorId);
			}
			
			Page linkageQueryPage = linkageQueryService.findLinkageQueryByCondition(page, condition);
			model.addAttribute("condition", condition);
			model.addAttribute("linkageQueryList", linkageQueryPage);
		} catch (ServiceException e) {
			logger.error("联动查询列表出错", e);
		}
		
		return "/edu3/teaching/linkageQuery/linkageQuery-list";
	}
	
	/**
	 * 新增或编辑
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/linkageQuery/addOrEdit.html")
	public String AddOrEditLinkageQuery(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws WebException {
		
		String linkageQueryId = request.getParameter("linkageQueryId");
		LinkageQuery linkageQuery= null;
		try {
			if(ExStringUtils.isEmpty(linkageQueryId)){// 新增
				linkageQuery = new LinkageQuery();
			} else {// 编辑
				linkageQuery = linkageQueryService.get(linkageQueryId);
			}
		} catch (ServiceException e) {
			logger.error("新增或编辑出错", e);
		}
		model.addAttribute("linkageQuery", linkageQuery);
		return "/edu3/teaching/linkageQuery/linkageQuery-form";
	}
	
	/**
	 * 保存或更新
	 * @param request
	 * @param response
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/linkageQuery/saveOrUpdate.html")
	public void saveOrUpdateLinkageQuery(HttpServletRequest request, HttpServletResponse response) throws WebException {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> params = new HashMap<String, Object>();
		int statusCode = 300;
		String message = "所有选项都不能为空！";
		String navTabId = "RES_TEACHING_LINKAGEQUERY_ADD";
		String queryString = "";
		try {
			do{
				// 参数处理
				String resouceId = request.getParameter("resourceid");
				String unitId = request.getParameter("unitId");
				String gradeId = request.getParameter("gradeId");
				String classicId = request.getParameter("classicId");
				String teachingType = request.getParameter("teachingType");
				String majorId = request.getParameter("majorId");
				LinkageQuery linkageQuery = null;
				
				if(ExStringUtils.isNotEmpty(resouceId)){
					navTabId = "RES_TEACHING_LINKAGEQUERY_EDIT";
					linkageQuery = linkageQueryService.get(resouceId);
				} else {
					linkageQuery = new LinkageQuery();
				}
				
				if(ExStringUtils.isEmpty(unitId)) {
					continue;
				}
				params.put("brSchoolId", unitId);
				if(ExStringUtils.isEmpty(gradeId)){
					continue;
				}
				params.put("gradeId", gradeId);
				if(ExStringUtils.isEmpty(classicId)){
					continue;
				}
				params.put("classicId",  classicId);
				if(ExStringUtils.isEmpty(teachingType)){
					continue;
				}
				params.put("teachingType", teachingType);
				if(ExStringUtils.isEmpty(majorId)){
					continue;
				}
				params.put("majorId", majorId);
				// 检查该条记录是否已经存在
				LinkageQuery _linkageQuery = linkageQueryService.isExisted(params);
				if( _linkageQuery== null){
					linkageQuery.setUnit(orgUnitService.get(unitId));
					linkageQuery.setGrade(gradeService.get(gradeId));
					linkageQuery.setClassic(classicService.get(classicId));
					linkageQuery.setTeachingType(teachingType);
					linkageQuery.setMajor(majorService.get(majorId));
					linkageQueryService.saveOrUpdate(linkageQuery);
				} else {
					linkageQuery = _linkageQuery;
				}
				statusCode = 200;
				message = "保存成功！";
				queryString = "?linkageQueryId="+linkageQuery.getResourceid();
			} while(false);
		} catch (Exception e) {
			logger.error("保存或更新出错", e);
			statusCode = 300;
			message = "保存失败！";
		} finally {
			map.put("statusCode", statusCode);
			map.put("message", message);
			map.put("navTabId", navTabId);
			map.put("reloadUrl", request.getContextPath() +"/edu3/teaching/linkageQuery/addOrEdit.html"+queryString);
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * （批量）删除联动查询记录
	 * 
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/linkageQuery/delete.html")
	public void deleteLinkageQuery(HttpServletRequest request, HttpServletResponse response) throws WebException {
		String linkageQueryIds = request.getParameter("linkageQueryIds");
		Map<String, Object> map = new HashMap<String, Object>();
		int statusCode = 200;
		String message ="删除成功！";
		try {
			if(ExStringUtils.isNotEmpty(linkageQueryIds)){
				String[] linkageQueryArray = linkageQueryIds.split(",");
				linkageQueryService.batchDelete(linkageQueryArray);
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "7", UserOperationLogs.DELETE,"删除查询联动：参数："+JsonUtils.mapToJson(Condition2SQLHelper.getConditionFromResquestByIterator(request)));
			}
		} catch (Exception e) {
			logger.error("（批量）删除联动查询记录出错", e);
			statusCode = 300;
			message = "删除失败！";
		} finally {
			map.put("statusCode", statusCode);
			map.put("message", message);
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 选择要同步联动信息的年级
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/linkageQuery/selectGrade.html")
	public String selectLQGrade(HttpServletRequest request, HttpServletResponse response) throws WebException {
		return "/edu3/teaching/linkageQuery/select-grade";
	}
	
	/**
	 * 同步某年级的招生专业信息到联动查询表
	 * 
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/linkageQuery/syncLinkageQuery.html")
	public void syncLinkageQuery(HttpServletRequest request, HttpServletResponse response) throws WebException {
		Map<String, Object> map = new HashMap<String, Object>();
		int statusCode = 200;
		String message ="同步成功！";
		try {
			String gradeId = request.getParameter("gradeId");
			// 同步某个年级的招生专业到联动查询表
			linkageQueryService.sysncRecruitMajorToLQ(gradeId);
			linkageQueryService.sysncStudentInfoToLQ(gradeId);
			linkageQueryService.clearCacheMap();
		} catch (ServiceException e) {
			logger.error("同步某年级的招生专业信息到联动查询表出错", e);
			statusCode = 300;
			message ="同步失败！";
		} finally {
			map.put("statusCode", statusCode);
			map.put("message", message);
		}
		
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 根据所传参数返回联动查询数据
	 * 
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/edu3/teaching/linkageQuery/cascadeQuery.html")
	public void cascadeQuery(HttpServletRequest request, HttpServletResponse response) throws WebException {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> condition = new HashMap<String, Object>();
		int statusCode = 200;
		String message ="";
		String unitOptions ="";
		String gradeOptions ="";
		String classicOptions ="";
		String teachingTypeOptions ="";
		String majorOptions ="";
		String classesOptions ="";
		//long startTime = System.currentTimeMillis();
		try {
			String operate = ExStringUtils.defaultIfEmpty(request.getParameter("operate"), "");
			String schoolId = ExStringUtils.defaultIfEmpty(request.getParameter("schoolId"), "");
			String defaultValue = ExStringUtils.defaultIfEmpty(request.getParameter("defaultValue"), "");
			String gradeId =  ExStringUtils.defaultIfEmpty(request.getParameter("gradeId"), "");
			String classicId =  ExStringUtils.defaultIfEmpty(request.getParameter("classicId"), "");
			String teachingType =  ExStringUtils.defaultIfEmpty(request.getParameter("teachingType"), "");
			String majorId =  ExStringUtils.defaultIfEmpty(request.getParameter("majorId"), "");
			String classesId =  ExStringUtils.defaultIfEmpty(request.getParameter("classesId"), "");
			String selectInfo =  ExStringUtils.defaultIfEmpty(request.getParameter("selectInfo"), "");
			// 
			Map<String,Object> selectInfoMap = new HashMap<String, Object>();
			if(ExStringUtils.isNotEmpty(selectInfo)){
				selectInfoMap = JsonUtils.jsonToMap(selectInfo);
			}
			
			if(LinkageQuery.OPERATE_BEGIN.equals(operate)){
				Map<String, Object> unitInfoMap = new HashMap<String, Object>();
				if(selectInfoMap!=null && selectInfoMap.size() > 0){
					unitInfoMap = (Map<String,Object>)selectInfoMap.get("unitInfo");
				}
				// 教学点构造的options
				unitOptions = linkageQueryService.constructUnitOptions(defaultValue, schoolId,unitInfoMap);
			}
			
			if(ExStringUtils.isNotEmpty(schoolId)){
				condition.put("brSchoolid", schoolId);
			} else {
				if(ExStringUtils.isNotEmpty(defaultValue)){
					condition.put("brSchoolid", defaultValue);
				}
			}
			if(condition.containsKey("brSchoolid")){
				OrgUnit unit = orgUnitService.get(condition.get("brSchoolid").toString());
				map.put("unitTitle", unit!=null?(unit.getUnitCode()+"-"+unit.getUnitName()):"");
			}
			
			if(LinkageQuery.OPERATE_BEGIN.equals(operate) || LinkageQuery.OPERATE_UNIT.equals(operate)){
				Map<String, Object> gradeInfoMap = new HashMap<String, Object>();
				if(selectInfoMap!=null && selectInfoMap.size() > 0){
					gradeInfoMap = (Map<String,Object>)selectInfoMap.get("gradeInfo");
				}
				// 年级构造的options
				condition.put("operate","grade");
				gradeOptions = linkageQueryService.constructOptions(condition,gradeId,"grade",gradeInfoMap);
			}
			
			if(ExStringUtils.isNotEmpty(gradeId)){
				condition.put("gradeid", gradeId);
			}
			if(LinkageQuery.OPERATE_BEGIN.equals(operate) || LinkageQuery.OPERATE_UNIT.equals(operate)
					|| LinkageQuery.OPERATE_GRADE.equals(operate)){
				Map<String, Object> classicInfoMap = new HashMap<String, Object>();
				if(selectInfoMap!=null && selectInfoMap.size() > 0){
					classicInfoMap = (Map<String,Object>)selectInfoMap.get("classicInfo");
				}
				// 层次构造的options
				condition.put("operate","classic");
				classicOptions = linkageQueryService.constructOptions(condition,classicId,"classic",classicInfoMap);
			}
					
			if(ExStringUtils.isNotEmpty(classicId)){
				condition.put("classicid", classicId);
			}
			if(!(LinkageQuery.OPERATE_MAJOR.equals(operate) || LinkageQuery.OPERATE_TEACHINGTYPE.equals(operate))){
				Map<String, Object> teachingTypeInfoMap = new HashMap<String, Object>();
				if(selectInfoMap!=null && selectInfoMap.size() > 0){
					teachingTypeInfoMap = (Map<String,Object>)selectInfoMap.get("teachingTypeInfo");
				}
				// 学习形式构造的options
				condition.put("operate","teachingType");
				teachingTypeOptions = linkageQueryService.constructOptions(condition,teachingType,"teachingType",teachingTypeInfoMap);
			}
			
			if(ExStringUtils.isNotEmpty(teachingType)){
				condition.put("teachingType", teachingType);
			}
			if(!LinkageQuery.OPERATE_MAJOR.equals(operate)){
				Map<String, Object> majorInfoMap = new HashMap<String, Object>();
				if(selectInfoMap!=null && selectInfoMap.size() > 0){
					majorInfoMap = (Map<String,Object>)selectInfoMap.get("majorInfo");
				}
				// 专业构造的options
				condition.put("operate","major");
				majorOptions = linkageQueryService.constructOptions(condition,majorId,"major",majorInfoMap);
			}
			
			if(ExStringUtils.isNotEmpty(majorId)){
				condition.put("majorid", majorId);
				Major major = majorService.get(majorId);
				map.put("majorTitle", major!=null?major.getMajorName():"");
			}
			// 班级构造的options
			Map<String, Object> classesInfoMap = new HashMap<String, Object>();
			if(selectInfoMap!=null && selectInfoMap.size() > 0){
				classesInfoMap = (Map<String,Object>)selectInfoMap.get("classesInfo");
			}
			classesOptions = classesService.constructOptions(condition, classesId,false,classesInfoMap);
			if(ExStringUtils.isNotEmpty(classesId)){
				Classes classes = classesService.get(classesId);
				map.put("classesTitle", classes!=null?classes.getClassname():"");
			}
		} catch (Exception e) {
			logger.error("根据所传参数返回联动查询数据出错", e);
			statusCode = 300;
			message = "联动查询出错";
		} finally {
			map.put("statusCode", statusCode);
			map.put("message", message);
			map.put("unitOptions", unitOptions);
			map.put("gradeOptions", gradeOptions);
			map.put("classicOptions", classicOptions);
			map.put("teachingTypeOptions", teachingTypeOptions);
			map.put("majorOptions", majorOptions);
			map.put("classesOptions", classesOptions);
		}
		//logger.info("联动查询耗时："+(System.currentTimeMillis()-startTime));
		renderJson(response, JsonUtils.mapToJson(map));
	}

}
