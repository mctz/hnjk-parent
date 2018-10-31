package com.hnjk.edu.teaching.controller;

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
import com.hnjk.core.foundation.utils.ExBeanUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.BaseSupportController;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.service.IEdumanagerService;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.roll.service.IStudentInfoService;
import com.hnjk.edu.teaching.model.ExamSub;
import com.hnjk.edu.teaching.model.GraduateMentor;
import com.hnjk.edu.teaching.model.GraduateMentorDetails;
import com.hnjk.edu.teaching.service.IExamSubService;
import com.hnjk.edu.teaching.service.IGraduateMentorDetailsService;
import com.hnjk.edu.teaching.service.IGraduateMentorService;
import com.hnjk.edu.teaching.service.IGraduatePapersOrderService;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;

/**
 * 毕业论文批次与指导老师关系维护表.
 * <code>GraduateMentorController</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2010-10-28 下午05:00:55
 * @see 
 * @version 1.0
 */
@Controller
public class GraduateMentorController extends BaseSupportController {

	private static final long serialVersionUID = -3611223387950193217L;
	

	@RequestMapping("/edu3/teaching/graduateMentor/list.html")
	public String exeList(String mentor, String batchId,HttpServletRequest request, Page objPage, ModelMap model) throws WebException{
		objPage.setOrderBy("g.examSub.startTime");
		objPage.setOrder(Page.DESC);//设置默认排序方式
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件
		
		String branchSchool = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));
		
		User user = SpringSecurityHelper.getCurrentUser();
		if(user.getOrgUnit().getUnitType().indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员
			branchSchool = user.getOrgUnit().getResourceid();
			model.addAttribute("isBrschool", true);
		}
		
		if (ExStringUtils.isNotEmpty(branchSchool)) {
			condition.put("branchSchool", branchSchool);
		}
		if(ExStringUtils.isNotEmpty(mentor)) {
			condition.put("mentor", mentor);
		}
		if(ExStringUtils.isNotEmpty(batchId)) {
			condition.put("batchId", batchId);
		}
		

		Page page = graduateMentorService.findGraduateMentorByCondition(condition, objPage);
		
		model.addAttribute("condition", condition);
		model.addAttribute("mentorList", page);
		return "/edu3/teaching/graduateMentor/graduateMentor-list";
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
	@RequestMapping("/edu3/teaching/graduateMentor/edit.html")
	public String exeEdit(String resourceid,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{	
		List<ExamSub> examSubs = examSubService.findByHql("from "+ExamSub.class.getSimpleName()+" where isDeleted= ? " +
				" and batchType = ? and examsubStatus = ?", 0,"thesis","2");
		model.addAttribute("examSubs", examSubs);
		if(ExStringUtils.isNotBlank(resourceid)){ //-----编辑
			GraduateMentor graduateMentor = graduateMentorService.get(resourceid);
			model.addAttribute("graduate", graduateMentor);
		}else{ //----------------------------------------新增
			model.addAttribute("graduate", new GraduateMentor());
		}
		return "/edu3/teaching/graduateMentor/graduateMentor-form";
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
	@RequestMapping("/edu3/teaching/graduateMentor/save.html")
	public void exeSave(GraduateMentor graduate,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		String[] stuIds = request.getParameterValues("stuId7");
		try {
			if(ExStringUtils.isNotBlank(graduate.getResourceid())){ //--------------------更新
				GraduateMentor p_gradeate = graduateMentorService.get(graduate.getResourceid());
				ExBeanUtils.copyProperties(p_gradeate, graduate);
				relationStu(p_gradeate, stuIds);
				p_gradeate.setExamSub(examSubService.get(graduate.getExamSubId()));
				p_gradeate.setEdumanager(edumanagerService.get(graduate.getGuidTeacherId()));
				graduateMentorService.update(p_gradeate);
			}else{ //-------------------------------------------------------------------保存
				graduate.setExamSub(examSubService.get(graduate.getExamSubId()));
				graduate.setEdumanager(edumanagerService.get(graduate.getGuidTeacherId()));
				if(null !=stuIds && stuIds.length>0){
					for (int i = 0; i < stuIds.length; i++) {
						GraduateMentorDetails stuDetail = new GraduateMentorDetails();
						StudentInfo stu = studentInfoService.get(stuIds[i]);
						stuDetail.setStudentInfo(stu);
						stuDetail.setGraduateMentor(graduate);
						graduate.getGraduateMentorDetails().add(stuDetail);
					}
				}
				graduateMentorService.save(graduate);
			}
			map.put("statusCode", 200);
			map.put("message", "保存成功！");
			map.put("navTabId", "RES_GRADUATE_DATA");
			map.put("reloadUrl", request.getContextPath() +"/edu3/teaching/graduateMentor/edit.html?resourceid="+graduate.getResourceid());
		}catch (Exception e) {
			logger.error("保存出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败:<br/>"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}

	private void relationStu(GraduateMentor graduate, String[] stuIds) {
		if(null !=stuIds && stuIds.length>0){
			for (int i = 0; i < stuIds.length; i++) {
				GraduateMentorDetails stuDetail = graduateMentorDetailsService.findUnique("from GraduateMentorDetails g where g.studentInfo.resourceid=? and isDeleted=0", new String[]{stuIds[i]});
				if(null == stuDetail) {
					stuDetail = new GraduateMentorDetails();
				}
				StudentInfo stu = studentInfoService.get(stuIds[i]);
				stuDetail.setStudentInfo(stu);
				stuDetail.setGraduateMentor(graduate);
				graduate.getGraduateMentorDetails().add(stuDetail);
			}
		}
	}
	
	
	/**
	 * 删除列表对象
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/graduateMentor/delete.html")
	public void exeDelete(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		String resourceid = request.getParameter("resourceid");		
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(resourceid)){
				if(resourceid.split("\\,").length >1){//批量删除					
					graduateMentorService.batchCascadeDelete(resourceid.split("\\,"));
				}else{//单个删除
					graduateMentorService.delete(resourceid);
				}
				map.put("statusCode", 200);
				map.put("message", "删除成功！");				
				map.put("forward", request.getContextPath()+"/edu3/teaching/graduateDate/list.html");
			}		
		} catch (Exception e) {
			logger.error("删除出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 新增学生
	 * @param request
	 * @param response
	 * @param model
	 * @throws WebException
	 */
	@RequestMapping("/edu3/framework/teaching/graduateMentor/stu/add.html")
	public void exeSaveStu(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		String detailId = ExStringUtils.trimToEmpty(request.getParameter("detailId"));
		String gid = ExStringUtils.trimToEmpty(request.getParameter("gid"));
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotEmpty(gid) && null != detailId){
				String[] arr = detailId.split("\\,");
				GraduateMentor graduateMentor = graduateMentorService.get(gid);
				Set<GraduateMentorDetails> graduateMentorDetails = graduateMentor.getGraduateMentorDetails();
				for(int i=0;i<arr.length;i++){
					GraduateMentorDetails gDetails = new GraduateMentorDetails();
					gDetails.setGraduateMentor(graduateMentor);
					gDetails.setStudentInfo(studentInfoService.get(arr[i]));
					graduateMentorDetails.add(gDetails);
				}
				graduateMentorService.update(graduateMentor);
				map.put("statusCode", 200);
				map.put("message", "新增成功！");
				map.put("navTabId", "RES_TEACHING_THESIS_MENTOR_EDIT");	
				map.put("reloadTabUrl",  request.getContextPath()+"/edu3/teaching/graduateMentor/edit.html?resourceid="+gid);
			}
		} catch (Exception e) {
			logger.error("新增出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "新增出错:<br/>"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	

	/**
	 * 删除学生
	 * @param request
	 * @param response
	 * @param model
	 * @throws WebException
	 */
	@RequestMapping("/edu3/framework/teaching/graduateMentor/stu/delete.html")
	public void exeDeleteStu(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		String detailId = ExStringUtils.trimToEmpty(request.getParameter("detailId"));
		String gid = ExStringUtils.trimToEmpty(request.getParameter("gid"));
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotEmpty(gid) && null != detailId){
				String[] arr = detailId.split("\\,");
				graduateMentorService.deleteDetails(gid,arr);
				map.put("statusCode", 200);
				map.put("message", "删除成功！");				
			
				map.put("navTabId", "RES_TEACHING_THESIS_MENTOR_EDIT");	
				map.put("reloadTabUrl",  request.getContextPath()+"/edu3/teaching/graduateMentor/edit.html?resourceid="+gid);
				//map.put("forwardUrl", request.getContextPath()+"/edu3/teaching/graduateMentor/edit.html?resourceid="+gid);
			}
		} catch (Exception e) {
			logger.error("删除出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错:<br/>"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 选择学生
	 * @param objPage
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/edu3/teaching/graduateMentor/chooseStudent.html")
	public String exeChooseStudent(HttpServletRequest request, Page objPage, ModelMap model) throws WebException {		
		String branchSchool = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));//学习中心
		String major = ExStringUtils.trimToEmpty(request.getParameter("major"));//专业
		String classic = ExStringUtils.trimToEmpty(request.getParameter("classic"));//层次
		String grade = ExStringUtils.trimToEmpty(request.getParameter("grade"));//年级
		String name = ExStringUtils.trimToEmpty(request.getParameter("name"));//姓名
		String batchId = ExStringUtils.trimToEmpty(request.getParameter("batchId"));//批次
		
		objPage.setOrderBy("auditTime,resourceid");
		objPage.setOrder(Page.ASC);//设置默认排序方式
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件

		if(ExStringUtils.isNotEmpty(grade)) {
			condition.put("grade", grade);
		}
		if(ExStringUtils.isNotEmpty(major)) {
			condition.put("major", major);
		}
		if(ExStringUtils.isNotEmpty(name)) {
			condition.put("name", name);
		}
		if(ExStringUtils.isNotEmpty(batchId)) {
			condition.put("batchId", batchId);
		}
		if(ExStringUtils.isNotEmpty(classic)) {
			condition.put("classic", classic);
		}
		
		String center = "show";
		User cureentUser = SpringSecurityHelper.getCurrentUser();
		//招生人员--学习中心
		if(cureentUser.getOrgUnit().getUnitType().
				indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){
			branchSchool = cureentUser.getOrgUnit().getResourceid();
			center = "hide";
		}
		if(ExStringUtils.isNotEmpty(branchSchool)) {
			condition.put("branchSchool", branchSchool);
		}
		//审核通过的
		condition.put("status", Constants.BOOLEAN_TRUE);
		//过滤的同一批次里，已分配过的学生
		condition.put("isExclude", Constants.BOOLEAN_TRUE);
		Page page = graduatePapersOrderService.findGraduateByCondition(condition, objPage);
		
		model.addAttribute("showCenter", center);
		model.addAttribute("condition", condition);
		model.addAttribute("ordercList", page);
		return "/edu3/teaching/graduateMentor/schoolroll2-list";
	}
	

	@Autowired
	@Qualifier("graduatementorservice")
	private IGraduateMentorService graduateMentorService;
	@Autowired
	@Qualifier("examSubService")
	private IExamSubService examSubService;
	@Autowired
	@Qualifier("studentinfoservice")
	private IStudentInfoService studentInfoService;
	@Autowired
	@Qualifier("edumanagerService")
	private IEdumanagerService edumanagerService;
	@Autowired
	@Qualifier("graduatementordetailsservice")
	private IGraduateMentorDetailsService graduateMentorDetailsService;
	@Autowired
	@Qualifier("graduatepapersorderservice")
	private IGraduatePapersOrderService graduatePapersOrderService;

}
