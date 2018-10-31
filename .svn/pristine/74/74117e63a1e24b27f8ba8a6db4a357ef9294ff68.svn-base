package com.hnjk.edu.basedata.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.utils.ExBeanUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.JsonModel;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.controller.BaseSupportController;
import com.hnjk.edu.basedata.model.Building;
import com.hnjk.edu.basedata.model.ClassInfo;
import com.hnjk.edu.basedata.service.IBuildingService;
import com.hnjk.edu.basedata.service.IClassService;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;
import com.hnjk.security.service.IOrgUnitService;
/**
 * 
  * @description:班级设置
  * @author xiangy  
  * @date 2013-12-4 上午10:15:00 
  * @version V1.0
 */
@Controller
public class ClassController extends BaseSupportController {

	private static final long serialVersionUID = -1995395333825928395L;
	
	@Autowired
	@Qualifier("classService")
	private IClassService classService;
	
	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;
	
	@Autowired
	@Qualifier("orgUnitService")
	private IOrgUnitService orgUnitService;
	
	@Autowired
	@Qualifier("buildingService")
	private IBuildingService buildingService;
	
	/**
	 * 
	  * @description:班级列表
	  * @author xiangy  
	  * @date 2013-12-10 下午03:50:58 
	  * @version V1.0
	  * @param className
	  * @param objPage
	  * @param model
	  * @return
	  * @throws WebException
	 */
	@RequestMapping("/edu3/sysmanager/class/list.html")
	public String classList(String className,Page objPage,ModelMap model) throws WebException{
		objPage.setOrderBy("classname");
		objPage.setOrder(Page.ASC);//设置默认排序方式
		Map<String ,Object> condition=new HashMap<String ,Object>();
		if(ExStringUtils.isNotEmpty(className)) {
			condition.put("className", className);
		}
		String orgUnitId=SpringSecurityHelper.getCurrentUser().getOrgUnit().getResourceid();
		String isAllowClass=SpringSecurityHelper.getCurrentUser().getOrgUnit().getIsAllowClass();
		condition.put("orgUnit", orgUnitId);
		condition.put("isAllowClass", isAllowClass);
		Page page=classService.findClassByCondition(condition, objPage);
		model.addAttribute("condition", condition);
		model.addAttribute("classList", page);
		return "/edu3/basedata/class/class-list";
	}
	/**
	 * 
	  * @description:新增编辑班级
	  * @author xiangy  
	  * @date 2013-12-4 下午03:00:05 
	  * @version V1.0
	  * @param resourceid
	  * @param request
	  * @param response
	  * @param model
	  * @return
	  * @throws WebException
	 */
	@RequestMapping("/edu3/sysmanager/class/edit.html")
	public String editClass(String resourceid,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		if(ExStringUtils.isNotBlank(resourceid)){    //编辑
			ClassInfo classInfo=classService.load(resourceid);
			model.addAttribute("classInfo", classInfo);
		}else{     //新增
			model.addAttribute("classInfo", new ClassInfo());
		}
		 return "/edu3/basedata/class/class-form";
		
	}
	/**
	 * 
	  * @description:保存班级
	  * @author xiangy  
	  * @date 2013-12-4 下午04:09:30 
	  * @version V1.0
	  * @param classInfo
	  * @param request
	  * @param response
	  * @param model
	  * @throws WebException
	 */
	@RequestMapping("/edu3/sysmanager/class/save.html")
	public void saveClass(ClassInfo classInfo,HttpServletRequest request,HttpServletResponse response , ModelMap model) throws WebException{
		Map<String ,Object> map=new HashMap<String,Object>();
		ClassInfo persistClassInfo=null;
		boolean isAlive=false;      //班级名称是否存在
		User user=SpringSecurityHelper.getCurrentUser();
		try{
			if(ExStringUtils.isNotBlank(classInfo.getResourceid())){    //编辑
				List<Map<String,Object>> ClassInfoAlive     =baseSupportJdbcDao.getBaseJdbcTemplate().findForList("select resourceId from edu_base_class where isDeleted=0 and className= ? and resourceid<>? and branchSchoolId=?", new Object[]{classInfo.getClassName(),classInfo.getResourceid(),user.getOrgUnit().getResourceid()});
				if(ClassInfoAlive.size()>0){
					isAlive=true;
				}else{
					persistClassInfo = classService.load(classInfo.getResourceid());
					ExBeanUtils.copyProperties(persistClassInfo, classInfo);
					persistClassInfo.setBranchSchool(user.getOrgUnit());
					
				}
				
			}else{   //保存
				
				List<Map<String,Object>> ClassInfoAlive     =baseSupportJdbcDao.getBaseJdbcTemplate().findForList("select resourceId from edu_base_class where isDeleted=0 and className= ? and branchSchoolId=?", new Object[]{classInfo.getClassName(),user.getOrgUnit().getResourceid()});
				if(ClassInfoAlive.size()>0){
					isAlive=true;
				}else{
					persistClassInfo=new ClassInfo();
					ExBeanUtils.copyProperties(persistClassInfo, classInfo);
					persistClassInfo.setBranchSchool(user.getOrgUnit());
					
				}
				
				
			}
			if(isAlive){
				map.put("statusCode", 300);
				map.put("message", "保存失败！班级名称重复！");
			}else{
				classService.saveOrUpdate(persistClassInfo);
				map.put("statusCode", 200);
				map.put("message", "保存成功！");
				map.put("navTabId", "RES_BASEDATA_CLASS");
				map.put("reloadUrl", request.getContextPath() +"/edu3/sysmanager/class/edit.html?resourceid="+persistClassInfo.getResourceid());
			}
			
		}catch (Exception e){
			logger.error("保存出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败：<br/> 班级名称重复！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 
	  * @description:删除班级
	  * @author xiangy  
	  * @date 2013-12-4 下午04:22:55 
	  * @version V1.0
	  * @param request
	  * @param response
	  * @param model
	  * @throws WebException
	 */
	@RequestMapping("/edu3/sysmanager/class/delete.html")
	public void deleteClass(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		String resourceid=request.getParameter("resourceid");
		Map<String ,Object> map=new HashMap<String ,Object>();
		try{
			if(ExStringUtils.isNotBlank(resourceid)){
				boolean isHaveStu=false;
				if(resourceid.split("\\,").length>1){//批量删除
					for(String res:resourceid.split("\\,")){
						List<Map<String,Object>> stus     =baseSupportJdbcDao.getBaseJdbcTemplate().findForList("select resourceId from edu_roll_studentInfo where isDeleted=0 and classid= ?", new Object[]{res});
						if(stus.size()>0) {
							isHaveStu=true;
						}
					}
					classService.batchTruncate(ClassInfo.class, resourceid.split("\\,"));
				}else{//单个删除
					List<Map<String,Object>> stus     =baseSupportJdbcDao.getBaseJdbcTemplate().findForList("select resourceId from edu_roll_studentInfo where isDeleted=0 and classid= ?", new Object[]{resourceid});
					if(stus.size()>0) {
						isHaveStu=true;
					}
					classService.truncateByProperty(ClassInfo.class, "resourceid", resourceid);
				}
				if(isHaveStu){
					map.put("stutusCode", 300);
					map.put("message", "删除出错！该班级正在使用中！");
					map.put("forward", request.getContextPath()+"/edu3/sysmanager/class/list.html");
				}else{
					map.put("statusCode", 200);
					map.put("message", "删除成功！");				
					map.put("forward", request.getContextPath()+"/edu3/sysmanager/class/list.html");
				}
				
			}
		}catch(Exception e){
			logger.error("删除班级出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错！该班级正在使用中！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	
	}
	
	/**
	 * 
	  * @description:删除班级2
	  * @author chifj  
	  * @date 2014-11-27 下午04:22:55 
	  * @version V1.0
	  * @param request
	  * @param response
	  * @param model
	  * @throws WebException
	 */
	@RequestMapping("/edu3/sysmanager/class/delete2.html")
	public void deleteClass2(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		String resourceid=request.getParameter("resourceid");
		Map<String ,Object> map=new HashMap<String ,Object>();
		try{
			Map<String,Object> values 		  	  	 = new HashMap<String, Object>();
			if(ExStringUtils.isNotBlank(resourceid)){
				boolean isHaveStu   = false;
				String  usedClasses = "";
				String 	sql			= "SELECT COUNT(*) FROM(SELECT STU.CLASSID,STU.ISDELETED FROM edu_roll_studentInfo stu UNION";
				sql				   += " SELECT exa.CLASSID,EXA.ISDELETED FROM EDU_BASE_EXAMROOM exa UNION";
				sql				   += " SELECT cla.CLASSID,CLA.ISDELETED FROM EDU_BASE_CLASSROOM cla UNION";
				sql				   += " SELECT enrol.CLASSID,enrol.ISDELETED FROM EDU_RECRUIT_ENROLLEEINFO enrol) tmp";
				sql				   += " WHERE tmp.CLASSID = :classId AND tmp.ISDELETED = 0";
				if(resourceid.split("\\,").length>1){//批量删除
					for(String res:resourceid.split("\\,")){
						values.put("classId", res);
						Long   				 	num   = baseSupportJdbcDao.getBaseJdbcTemplate().findForLong(sql, values);
						if(num>0){
							ClassInfo  usedClass 	  = classService.get(res);
							usedClasses 			 += usedClass.getClassName()+"、";
							isHaveStu				  = true; 
						} 
					}
					if(!isHaveStu){
						classService.batchTruncate(ClassInfo.class, resourceid.split("\\,"));
					}
				}else{//单个删除
					values.put("classId", resourceid);
					Long   				 	num   	  = baseSupportJdbcDao.getBaseJdbcTemplate().findForLong(sql, values);
					if(num>0){
						ClassInfo  usedClass 	 	  = classService.get(resourceid);
						usedClasses 				 += usedClass.getClassName()+" ";
						isHaveStu					  = true;
					}  
					if(!isHaveStu){
						classService.truncateByProperty(ClassInfo.class, "resourceid", resourceid);
					}
				}
				if(isHaveStu){
					map.put("statusCode", 300);
					map.put("message", "删除出错！班级：<font color=red>"+usedClasses+"</font>正在使用中，不允许删除！");
					map.put("forward", request.getContextPath()+"/edu3/sysmanager/class/list.html");
				}else{
					map.put("statusCode", 200);
					map.put("message", "删除成功！");				
					map.put("forward", request.getContextPath()+"/edu3/sysmanager/class/list.html");
				}
			}
		}catch(Exception e){
			logger.error("删除班级出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错！该班级正在使用中！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	
	/**
	 * 获得学习中心的所有班级
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/edu3/sysmanager/class/getClassBySchool.html")
	public void getClassBySchool(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String branchSchoolId = request.getParameter("branchSchool");
		String buildingId = request.getParameter("buildingId");
		Map<String ,Object> map=new HashMap<String,Object>();
		if(ExStringUtils.isNotEmpty(buildingId)){
			Building building = buildingService.get(buildingId);
			branchSchoolId = building.getBranchSchool().getResourceid();
		}
		if(StringUtils.isNotEmpty(branchSchoolId)){
			String isAllowClass = orgUnitService.load(branchSchoolId).getIsAllowClass();
			if("Y".equals(isAllowClass)){//该教学点允许分班
				List<ClassInfo> ClassList = classService.findByHql(" from "+ClassInfo.class.getSimpleName()+" ci where ci.isDeleted=0 and ci.status='1' and ci.branchSchool.resourceid=? order by ci.className", branchSchoolId);
				
				List<JsonModel> jsonList = new ArrayList<JsonModel>();
				for (ClassInfo classInfo : ClassList) {
					jsonList.add(new JsonModel(classInfo.getClassName(), classInfo.getClassName(), classInfo.getResourceid()));
				}		
				renderJson(response, JsonUtils.listToJson(jsonList));
				
			}else{//该教学点不允许分班
				map.put("error", "该教学点不允许分班！");
				if(ExStringUtils.isNotEmpty(buildingId)){
					map.put("error", "该教学楼所在的学习中心不允许分班！");
				}
				renderJson(response, JsonUtils.mapToJson(map));
			}
		}else{
			map.put("error", "系统错误！");
			renderJson(response, JsonUtils.mapToJson(map));
		}
	}
	

}
