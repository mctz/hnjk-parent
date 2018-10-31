package com.hnjk.edu.teaching.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.utils.ExBeanUtils;
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.HttpHeaderUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.BaseSupportController;
import com.hnjk.edu.basedata.model.Classic;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.basedata.model.Major;
import com.hnjk.edu.basedata.model.NationMajor;
import com.hnjk.edu.basedata.service.IClassicService;
import com.hnjk.edu.basedata.service.IMajorService;
import com.hnjk.edu.basedata.service.INationMajorService;
import com.hnjk.edu.teaching.service.ICourseService;
import com.hnjk.edu.teaching.vo.MajorInfo;
import com.hnjk.edu.teaching.vo.MajorSetInfo;
import com.hnjk.edu.util.Condition2SQLHelper;
import com.hnjk.platform.system.UserOperationLogsHelper;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.Attachs;
import com.hnjk.platform.system.model.UserOperationLogs;
import com.hnjk.platform.system.service.IAttachsService;

/**
 * 专业设置
 * <code>MajorController</code><p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-4-7 下午02:48:41
 * @see 
 * @version 1.0
 */
@Controller
public class MajorController extends BaseSupportController {

	private static final long serialVersionUID = -8356645149407330094L;
	
	@Autowired
	@Qualifier("majorService")
	private IMajorService majorService;
	
	@Autowired
	@Qualifier("courseService")
	private ICourseService courseService;
	
	@Autowired
	@Qualifier("nationMajorService")
	private INationMajorService nationMajorService;
	
	@Autowired
	@Qualifier("classicService")
	private IClassicService classicService;
	
	@Autowired
	@Qualifier("attachsService")
	private IAttachsService attachsService;//注入附件服务
	
	/**
	 * 返回专业列表
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/sysmanager/major/list.html")
	public String exeList(HttpServletRequest request,Page objPage,ModelMap model) throws WebException{
		objPage.setOrderBy("majorCode");	
		objPage.setOrder(Page.ASC);//设置默认排序方式
		
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件
		String majorName = ExStringUtils.trimToEmpty(request.getParameter("majorName"));
		String majorCode = ExStringUtils.trimToEmpty(request.getParameter("majorCode"));
		String isForeignLng = ExStringUtils.trimToEmpty(request.getParameter("isForeignLng"));
		
		if(ExStringUtils.isNotEmpty(majorName)) {
			condition.put("majorName", majorName);
		}
		if(ExStringUtils.isNotEmpty(majorCode)) {
			condition.put("majorCode", majorCode);
		}
		if(ExStringUtils.isNotEmpty(isForeignLng)) {
			condition.put("isForeignLng", isForeignLng);
		}
		
		Page page = majorService.findMajorByCondition(condition, objPage);
		
		model.addAttribute("majorList", page);
		model.addAttribute("condition", condition);
		return "/edu3/basedata/major/major-list";
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
	@RequestMapping("/edu3/sysmanager/major/edit.html")
	public String exeEdit(String resourceid,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		String settingList = "";
		if(ExStringUtils.isNotBlank(resourceid)){ //-----编辑
			Major major = majorService.load(resourceid);	
			model.addAttribute("major", major);
			List<Attachs> attachsList = attachsService.findAttachsByFormId(resourceid);
			if(attachsList.size()>0){
				model.addAttribute("attachs",attachsList.get(0));
			}
			Set<Course> courseSet = major.getCourses();
			for (Course course : courseSet) {
				settingList += course.getResourceid()+",";
			}
			if(ExStringUtils.isNotBlank(settingList)){
				settingList = settingList.substring(0, settingList.length()-1);
			}
		}else{ //----------------------------------------新增
			model.addAttribute("major", new Major());
		}
		model.addAttribute("rootUrl", request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()
				+ CacheAppManager.getSysConfigurationByCode("web.uploadfile.rooturl").getParamValue());//拿到附件映射的全局URL
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("courseType", "22");//选修课
		String courseList = courseService.constructOptions(condition, settingList.split(","));
		model.addAttribute("courseList", courseList);
		return "/edu3/basedata/major/major-form";
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
	@RequestMapping("/edu3/sysmanager/major/save.html")
	public void exeSave(Major major,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			String nationMajorId = request.getParameter("nationMajorId");
			String photoAttrId = request.getParameter("photoAttrId");
			String[] majorCourse  = request.getParameterValues("majorcourse");
			boolean updateFlag = false;
			if(ExStringUtils.isNotEmpty(nationMajorId)){
				NationMajor nationMajor = nationMajorService.get(nationMajorId);
				major.setNationMajor(nationMajor);
				major.setMajorNationCode(nationMajor.getNationMajorCode());
			} 
			Set<Course> courseSet = new HashSet<Course>();
			if(majorCourse!=null && majorCourse.length>0){
				for (String courseid : majorCourse) {
					Course course = courseService.get(courseid);
					courseSet.add(course);
				}
				major.setCourses(courseSet);
			}
			if(ExStringUtils.isNotBlank(major.getResourceid())){ //--------------------更新
				Major persistMajor = majorService.get(major.getResourceid());
				ExBeanUtils.copyProperties(persistMajor, major);
				majorService.update(persistMajor);
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "7", UserOperationLogs.UPDATE,"更新专业：参数："+JsonUtils.mapToJson(Condition2SQLHelper.getConditionFromResquestByIterator(request)));
				updateFlag = true;
			}else{ //-------------------------------------------------------------------保存
				Major m = majorService.findUniqueByProperty("majorCode", major.getMajorCode());
				if(null != m ) {
					throw new WebException("存在相同的专业编码，请重新输入！");
				}
				majorService.save(major);
			}
			Attachs attachs   = null;
			if (ExStringUtils.isNotEmpty(photoAttrId)) {
				attachs       = attachsService.get(photoAttrId);
			}
			//如果上传了照片则拷贝到新的目录
			if(null!=attachs){
				//附件保存在服务器中的路径
//				String serPath          = attachs.getSerPath().substring((attachs.getSerPath().length()-7), attachs.getSerPath().length());
				//构造路径，年度_月份
				String newSerPathName = String.valueOf(ExDateUtils.getCurrentYear() )+ "_"+ (ExDateUtils.getCurrentMonth()<10?("0"+String.valueOf(ExDateUtils.getCurrentMonth())):String.valueOf(ExDateUtils.getCurrentMonth()));
				//服务器中存储附件的根目录
				String rootPath           = CacheAppManager.getSysConfigurationByCode("web.uploadfile.rootpath").getParamValue();
				//服务器中存储培训项目照片的目录
				String stuPhotoDirectory  = "project"+File.separator+"attachs";
				//重命名后的照片路径
				String reNameSerPath      = stuPhotoDirectory+File.separator+newSerPathName+File.separator+ attachs.getSerName();
				File reNameDirectory      = new File(rootPath+File.separator+stuPhotoDirectory+File.separator+newSerPathName);
				File sourcePhoto          = new File(attachs.getSerPath()+File.separator+attachs.getSerName());
				File reNamePhoto          = new File(rootPath+File.separator+reNameSerPath);   
				if (!reNameDirectory.exists()) {
					reNameDirectory.mkdirs();
				}
				//拷贝到服务器中存储学生照片的目录，如果存在，则覆盖
				FileCopyUtils.copy(sourcePhoto,reNamePhoto);
				//重新命名路径
				attachs.setSerPath(rootPath+"/project/attachs");
				//重命名附件图片的名字
				attachs.setSerName(newSerPathName+"/"+ attachs.getSerName());
				attachs.setFormId(major.getResourceid());
				attachsService.update(attachs);
				//如果是修改更新，则删除原来的图片，留下唯一一个图片即可！
				if(updateFlag){
					List<Attachs> Attachslist = attachsService.findAttachsByFormId(major.getResourceid());
					for(Attachs att:Attachslist){
						if(!att.getResourceid().equals(attachs.getResourceid())){
							attachsService.truncate(att);
						}
					}
				}
			}
			map.put("statusCode", 200);
			map.put("message", "保存成功！");
			map.put("navTabId", "RES_BASEDATA_MANAGER_MAJOR");
			map.put("reloadUrl", request.getContextPath() +"/edu3/sysmanager/major/edit.html?resourceid="+major.getResourceid());
		}catch (Exception e) {
			logger.error("保存出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败:<br/>"+e.getLocalizedMessage());
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
	@RequestMapping("/edu3/sysmanager/major/delete.html")
	public void exeDelete(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		String resourceid = request.getParameter("resourceid");
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(resourceid)){
//				if(resourceid.split("\\,").length >1){//批量删除					
					majorService.batchCascadeDelete(resourceid.split("\\,"));
					UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "7", UserOperationLogs.DELETE,"删除专业：参数："+JsonUtils.mapToJson(Condition2SQLHelper.getConditionFromResquestByIterator(request)));
//				}else{//单个删除
//					majorService.delete(resourceid);
//				}
				map.put("statusCode", 200);
				map.put("message", "删除成功！");				
				map.put("forward", request.getContextPath()+"/edu3/sysmanager/major/list.html");
			}
		} catch (ServiceException e) {
			map.put("statusCode", 300);
			map.put("message", e.getMessage());
		} catch (Exception e) {
			logger.error("删除栏目出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 国家专业列表
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/sysmanager/nationmajor/list.html")
	public String listNationMajor(HttpServletRequest request,Page objPage,ModelMap model) throws WebException{
		objPage.setOrderBy("nationMajorCode");	
		objPage.setOrder(Page.ASC);//设置默认排序方式
		
		String nationMajorName = request.getParameter("nationMajorName");	
		String nationMajorCode = request.getParameter("nationMajorCode");
		String nationMajorType = request.getParameter("nationMajorType");
		String classicid = request.getParameter("classicid");
		
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件
		if(ExStringUtils.isNotEmpty(nationMajorName)) {
			condition.put("nationMajorName", nationMajorName);
		}
		if(ExStringUtils.isNotEmpty(nationMajorCode)) {
			condition.put("nationMajorCode", nationMajorCode);
		}
		if(ExStringUtils.isNotEmpty(nationMajorType)) {
			condition.put("nationMajorType", nationMajorType);
		}
		if(ExStringUtils.isNotEmpty(classicid)) {
			condition.put("classicid", classicid);
		}
		
		Page page = nationMajorService.findNationMajorByCondition(condition, objPage);
		
		model.addAttribute("nationMajorList", page);
		model.addAttribute("condition", condition);
		return "/edu3/basedata/major/nationmajor-list";
	}
	
	/**
	 * 新增编辑国家专业
	 * @param resourceid
	 * @param request
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/sysmanager/nationmajor/input.html")
	public String editNationMajor(String resourceid,HttpServletRequest request,ModelMap model) throws WebException{
		if(ExStringUtils.isNotBlank(resourceid)){ //-----编辑
			NationMajor nationMajor = nationMajorService.load(resourceid);	
			model.addAttribute("nationMajor", nationMajor);
		}else{ //----------------------------------------新增
			model.addAttribute("nationMajor", new NationMajor());			
		}
		return "/edu3/basedata/major/nationmajor-form";
	}
	
	/**
	 * 保存国家专业
	 * @param classicid
	 * @param nationMajor
	 * @param request
	 * @param response
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/sysmanager/nationmajor/save.html")
	public void saveNationMajor(String classicid,NationMajor nationMajor,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotEmpty(classicid)){
				Classic classic = classicService.load(classicid);
				nationMajor.setClassic(classic);
			}
			if(ExStringUtils.isNotBlank(nationMajor.getResourceid())){ //--------------------更新
				NationMajor persistNationMajor = nationMajorService.get(nationMajor.getResourceid());
				ExBeanUtils.copyProperties(persistNationMajor, nationMajor);
				nationMajorService.update(persistNationMajor);
			}else{ //-------------------------------------------------------------------保存
				if(nationMajorService.isExistsNationMajorCode(nationMajor.getMajorCode())){
					throw new WebException("该国家专业代码已存在，请重新输入！");
				}
				nationMajorService.save(nationMajor);
			}
			map.put("statusCode", 200);
			map.put("message", "保存成功！");
			map.put("navTabId", "RES_BASEDATA_MANAGER_NATIONMAJOR");
			map.put("reloadUrl", request.getContextPath() +"/edu3/sysmanager/nationmajor/input.html?resourceid="+nationMajor.getResourceid());
		}catch (Exception e) {
			logger.error("保存国家专业出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败！<br/>"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 检查专业代码唯一性
	 * @param majorCode
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/framework/nationmajor/validateCode.html")
	public void validateTeacherCode(String majorCode,HttpServletRequest request,HttpServletResponse response) throws WebException{
		String msg = "";
		if(ExStringUtils.isNotBlank(majorCode)){
			if(nationMajorService.isExistsNationMajorCode(majorCode)){
				msg = "exist";
			}
		}			
		renderText(response, msg);
	}
	
	/**
	 * 删除国家专业
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/sysmanager/nationmajor/remove.html")
	public void removeNationMajor(String resourceid,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(resourceid)){
				nationMajorService.batchDelete(resourceid.split("\\,"));				
				map.put("statusCode", 200);
				map.put("message", "删除成功！");				
				map.put("forward", request.getContextPath()+"/edu3/sysmanager/nationmajor/list.html");
			}
		} catch (Exception e) {
			logger.error("删除国家专业出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 广东医公众号获取专业列表及图片信息  
	 */
	@RequestMapping("/edu3/sysmanager/getMajorJson.html")
	public void getMajorJson(String resourceid,HttpServletRequest request,HttpServletResponse response) throws WebException{
		renderJson(response, JsonUtils.mapToJson(getMajorListJson(request)));
	}

	/**
	 * 获取专业信息列表Json
	 * 
	 * @param request
	 * @return
	 */
	private Map<String ,Object> getMajorListJson(HttpServletRequest request) {
		Map<String ,Object> map = new HashMap<String, Object>();
		int statusCode = 200;
		String message = "获取成功!";		
		List<Map<String,Object>> junior = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> undergraduate = new ArrayList<Map<String,Object>>();
		String rootUrl = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()
				+ CacheAppManager.getSysConfigurationByCode("web.uploadfile.rooturl").getParamValue();
		
		try {
			List<Criterion> criterions = new ArrayList<Criterion>(1);			
			criterions.add(Restrictions.eq("isDeleted", 0));
			@SuppressWarnings("unchecked")
			List<Major> list = majorService.findByCriteria(Major.class,criterions.toArray(new Criterion[criterions.size()] ),Order.asc("majorSchoolCode"));
			
			for(Major m:list){
				Map<String,Object> m1 = new HashMap<String, Object>();
				
				String picture4course =m.getPicture4Course()==null?"": rootUrl+"/common/plancourse/"+m.getPicture4Course();
				m1.put("picture4course", picture4course);
				String picture4textbook =m.getPicture4TextBook()==null?"": rootUrl+"/common/textbook/"+m.getPicture4TextBook();
				m1.put("picture4Textbook", picture4textbook);
				if(m.getMajorCode().startsWith("1")){//本科					
					m1.put("majorName", m.getMajorName());
					undergraduate.add(m1);
				}else{
					m1.put("majorName", m.getMajorName());
					junior.add(m1);
				}				
			}
		} catch (ServiceException e1) {
			logger.error("接口获取专业列表失败:{}",e1.fillInStackTrace());
			statusCode= 300;
			message="接口获取专业列表失败";
		} finally{
			map.put("statusCode", statusCode);
			map.put("message", message);
			map.put("junior", junior);
			map.put("undergraduate", undergraduate);			
		}
		
		return map;
	} 
	
//	@RequestMapping("/edu3/sysmanager/getMajorJson.html")
//	public void getMajorJson(HttpServletRequest request,HttpServletResponse response) throws WebException{
//		Map<String ,Object> map = new HashMap<String, Object>();
//		int statusCode = 200;
//		String message = "获取成功!";		
//		List<Map<String,Object>> junior = new ArrayList<Map<String,Object>>();
//		List<Map<String,Object>> undergraduate = new ArrayList<Map<String,Object>>();
//		String rootUrl = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()
//				+ CacheAppManager.getSysConfigurationByCode("web.uploadfile.rooturl").getParamValue();
//		
//		try {
//			List<Major> list = majorService.findByCriteria(Restrictions.eq("isDeleted", 0));
//			
//			for(Major m:list){
//				Map<String,Object> m1 = new HashMap<String, Object>();				
//				m1.put("resourceid", m.getResourceid());
//				if(m.getMajorCode().startsWith("1")){//本科					
//					m1.put("majorName", m.getMajorName());
//					undergraduate.add(m1);
//				}else{
//					m1.put("majorName", m.getMajorName());
//					junior.add(m1);
//				}				
//			}
//		} catch (ServiceException e1) {
//			logger.error("接口获取专业列表失败:{}",e1.fillInStackTrace());
//			statusCode= 300;
//			message="接口获取专业列表失败";
//		} finally{
//			map.put("statusCode", statusCode);
//			map.put("message", message);
//			map.put("junior", junior);
//			map.put("undergraduate", undergraduate);			
//		}
//		renderJson(response, JsonUtils.mapToJson(map));
//	}
	
	@RequestMapping("/edu3/sysmanager/pictureViewPlancourse.html")
	public String getPicture4course(String resourceid,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		try {
			if(ExStringUtils.isNotBlank(resourceid)){
				Major m= majorService.get(resourceid);				
				if(m!=null){
					String picture4course = "/common/plancourse/"+m.getPicture4Course();
					model.addAttribute("rootUrl", request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()
							+ CacheAppManager.getSysConfigurationByCode("web.uploadfile.rooturl").getParamValue());//拿到附件映射的全局URL
					model.addAttribute("picture4course", picture4course);
				}
			}
		} catch (Exception e) {
			logger.error("查看图片出错:{}",e.fillInStackTrace());
		
		}
		return "/edu3/basedata/major/pictureViewPlancourse";
	}
	
	@RequestMapping("/edu3/sysmanager/pictureViewTextbook.html")
	public String getPicture4Textbook(String resourceid,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		try {
			if(ExStringUtils.isNotBlank(resourceid)){
				Major m= majorService.get(resourceid);				
				if(m!=null){
					String picture4textbook = "/common/textbook/"+m.getPicture4TextBook();
					model.addAttribute("rootUrl", request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()
							+ CacheAppManager.getSysConfigurationByCode("web.uploadfile.rooturl").getParamValue());//拿到附件映射的全局URL
					model.addAttribute("picture4TextBook", picture4textbook);
				}
			}
		} catch (Exception e) {
			logger.error("查看图片出错:{}",e.fillInStackTrace());
		
		}
		return "/edu3/basedata/major/pictureViewTextbook";
	}
	
	/**
	 * 获取专业设置列表-公众号使用
	 * 
	 * @param request
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/busi/majorSet.html")
	public String getMajorSet(HttpServletRequest request,ModelMap model) throws WebException{
		try {
			model.addAttribute("majorList", getMajorSetList(request));
		} catch (Exception e) {
			logger.error("获取专业设置列表:{}",e.fillInStackTrace());
		}
		return "/business/majorSet";
	}
	
	/**
	 * 获取课程教材对照列表-公众号使用
	 * 
	 * @param request
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/busi/teachMaterialList.html")
	public String teachMaterialList(HttpServletRequest request,ModelMap model) throws WebException{
		try {
			model.addAttribute("majorList", getMajorSetList(request));
		} catch (Exception e) {
			logger.error("获取课程教材对照列表:{}",e.fillInStackTrace());
		}
		return "/business/teachMaterialList";
	}
	
	/**
	 * 或者专业设置详细列表
	 * 
	 * @return
	 */
	private List<MajorInfo> getMajorSetList(HttpServletRequest request) {
		List<MajorInfo> majorList = null;
		MajorSetInfo majorSetInfo = JsonUtils.jsonToBean(JsonUtils.mapToJson(getMajorListJson(request)), MajorSetInfo.class);
		if(majorSetInfo != null) {
			List<MajorInfo> undergraduateList = majorSetInfo.getUndergraduate();
			List<MajorInfo> juniorList = majorSetInfo.getJunior();
			majorList = new ArrayList<MajorInfo>(undergraduateList.size()+juniorList.size());
			getMajorInfo(majorList,juniorList,"专科");
			getMajorInfo(majorList,undergraduateList,"本科");
		}
		
		return majorList;
	}
	
	/**
	 * 获取专业详细的信息
	 * @param majorList
	 * @param undergraduateList
	 * @param string
	 */
	private void getMajorInfo(List<MajorInfo> majorList, List<MajorInfo> undergraduateList, String classicName) {
		if(!CollectionUtils.isEmpty(undergraduateList)){
			for(MajorInfo m : undergraduateList) {
				m.setClassicName(classicName);
				majorList.add(m);
			}
		}
	}
}
