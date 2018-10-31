package com.hnjk.edu.teaching.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WriteException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.utils.ExBeanUtils;
import com.hnjk.core.foundation.utils.ExCollectionUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.GUIDUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.FileUploadAndDownloadSupportController;
import com.hnjk.core.support.context.Constants;
import com.hnjk.core.support.context.SystemContextHolder;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.roll.service.IStudentInfoService;
import com.hnjk.edu.teaching.model.ExamSub;
import com.hnjk.edu.teaching.model.GraduatePapersOrder;
import com.hnjk.edu.teaching.model.TeachingPlanCourse;
import com.hnjk.edu.teaching.service.ICourseService;
import com.hnjk.edu.teaching.service.IExamSubService;
import com.hnjk.edu.teaching.service.IGraduateMentorService;
import com.hnjk.edu.teaching.service.IGraduatePapersOrderService;
import com.hnjk.edu.teaching.service.ITeachingPlanCourseService;
import com.hnjk.edu.teaching.vo.GraduatePapersOrderVo;
import com.hnjk.extend.plugin.excel.config.MergeCellsParam;
import com.hnjk.extend.plugin.excel.service.IExportExcelService;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;

/**
 * 毕业生论文预约信息表.
 * <code>GraduatePapersOrderController</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2010-10-28 下午03:16:23
 * @see 
 * @version 1.0
 */
@Controller
public class GraduatePapersOrderController extends FileUploadAndDownloadSupportController{

	private static final long serialVersionUID = 6332992461416499615L;
	
	
	@RequestMapping("/edu3/teaching/graduatePapers/list.html")
	public String exeList(String branchSchool, String grade, String major, String name, String batchId, String classic, HttpServletRequest request,Page objPage, ModelMap model) throws WebException{
		objPage.setOrderBy("examSub.yearInfo.firstYear desc,guidTeacherName,studentInfo.branchSchool.unitCode");
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
		
		String teacherName = ExStringUtils.trimToEmpty(request.getParameter("teacherName"));
		String stuStudyNo = ExStringUtils.trimToEmpty(request.getParameter("stuStudyNo"));
		if(ExStringUtils.isNotEmpty(teacherName)) {
			condition.put("teacherName", teacherName);
		}
		if(ExStringUtils.isNotEmpty(stuStudyNo)) {
			condition.put("stuStudyNo", stuStudyNo);
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
		Page page = graduatePapersOrderService.findGraduateByCondition(condition, objPage);
		
		model.addAttribute("showCenter", center);
		model.addAttribute("condition", condition);
		model.addAttribute("ordercList", page);
		return "/edu3/teaching/graduatePapers/graduatePapers-list";
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
	@RequestMapping("/edu3/teaching/graduatePapers/edit.html")
	public String exeEdit(String resourceid,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		//可用的论文批次
		List<ExamSub> examSubs = examSubService.findByHql("from "+ExamSub.class.getSimpleName()+" where isDeleted= ? " +
				" and batchType = ? and examsubStatus = ?", 0,"thesis","2");
		model.addAttribute("examSubs", examSubs);
		if(ExStringUtils.isNotBlank(resourceid)){ //-----编辑
			GraduatePapersOrder graduatePapersOrder = graduatePapersOrderService.get(resourceid);	
			model.addAttribute("graduate", graduatePapersOrder);
		}else{ //----------------------------------------新增
			model.addAttribute("graduate", new GraduatePapersOrder());			
		}
		return "/edu3/teaching/graduatePapers/graduatePapers-form";
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
	@RequestMapping("/edu3/teaching/graduatePapers/save.html")
	public void exeSave(GraduatePapersOrder graduate,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		String courseId = request.getParameter("courseId");
		try {
			if(ExStringUtils.isNotBlank(graduate.getResourceid())){ //--------------------更新
				GraduatePapersOrder p_graduate = graduatePapersOrderService.get(graduate.getResourceid());
				
				
				if(ExStringUtils.isNotEmpty(courseId)) {
					graduate.setCourse(courseService.get(courseId));
				}
				ExBeanUtils.copyProperties(p_graduate, graduate);
				graduatePapersOrderService.saveOrUpdateGraduatePaperOrder(p_graduate);
			}else{ //-------------------------------------------------------------------保存
				
				if(ExStringUtils.isNotEmpty(courseId)) {
					graduate.setCourse(courseService.get(courseId));
				}
				//2012-06-15,K0528C_毕业论文功能调整:取消毕业论文申请的审核步骤
				graduate.setStatus(Constants.BOOLEAN_TRUE);
				graduatePapersOrderService.saveOrUpdateGraduatePaperOrder(graduate);
			}
			map.put("statusCode", 200);
			map.put("message", "保存成功！");
			map.put("navTabId", "RES_GRADUATE_PAPERS_ORDER");
			map.put("reloadUrl", request.getContextPath() +"/edu3/teaching/graduatePapers/edit.html?resourceid="+graduate.getResourceid());
		}catch (Exception e) {
			logger.error("保存毕业论文预约出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败！<br/>"+e.getMessage());
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
	@RequestMapping("/edu3/teaching/graduatePapers/delete.html")
	public void exeDelete(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		String resourceid = request.getParameter("resourceid");
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(resourceid)){
				if(resourceid.split("\\,").length >1){//批量删除					
					graduatePapersOrderService.batchCascadeDelete(resourceid.split("\\,"));
				}else{//单个删除
					graduatePapersOrderService.delete(resourceid);
				}
				map.put("statusCode", 200);
				map.put("message", "删除成功！");				
				map.put("forward", request.getContextPath()+"/edu3/teaching/graduatePapers/list.html");
			}
		} catch (Exception e) {
			logger.error("删除论文预约出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错:<br/>"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 审核列表对象
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/graduatePapers/audit.html")
	public void exeAudit(HttpServletRequest request,HttpServletResponse response, ModelMap model) throws WebException{
		String resourceid = request.getParameter("resourceid");
		String auditType = ExStringUtils.trimToEmpty(request.getParameter("auditType"));//审核类型
		int status = 0;
		if(ExStringUtils.isNotEmpty(auditType)) {
			status = Integer.parseInt(auditType);
		}
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(resourceid)){
				User user = SpringSecurityHelper.getCurrentUser();
				if(resourceid.split("\\,").length >1){//批量审核				
					graduatePapersOrderService.batchAudit(resourceid.split("\\,"), user.getResourceid(), user.getCnName(),status);
				}else{//单个审核
					graduatePapersOrderService.audit(resourceid, user.getResourceid(), user.getCnName(),status);
				}
				map.put("statusCode", 200);
				map.put("message", "操作成功！");				
				map.put("forward", request.getContextPath()+"/edu3/teaching/graduatePapers/list.html");
			}
		} catch (Exception e) {
			logger.error("审核出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "审核出错！<br/>"+e.getMessage());
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
	@RequestMapping("/edu3/teaching/graduatePapers/chooseStudent.html")
	public String exeChooseStudent(HttpServletRequest request, Page objPage, ModelMap model) throws WebException {
		objPage.setOrderBy("resourceid");
		objPage.setOrder(Page.ASC);//设置默认排序方式
		//查询条件
		Map<String,Object> condition = new HashMap<String,Object>();
		String branchSchool=request.getParameter("branchSchool");//学习中心
		String major=request.getParameter("major");//专业		
		String classic=request.getParameter("classic");//层次	
		
		String stuStatus=request.getParameter("stuStatus");//学籍状态
		String name=request.getParameter("name");//姓名
		String matriculateNoticeNo=request.getParameter("matriculateNoticeNo");//学号
		
		//只有本科学生才能申请毕业论文	
		condition.put("classicCode", "1,2");
		
		if(ExStringUtils.isNotEmpty(branchSchool)) {
			condition.put("branchSchool", branchSchool);
		}
		if(ExStringUtils.isNotEmpty(major)) {
			condition.put("major", major);
		}
		if(ExStringUtils.isNotEmpty(classic)) {
			condition.put("classic", classic);
		}
		if(ExStringUtils.isNotEmpty(stuStatus)) {
			condition.put("stuStatus", stuStatus);
		}
		if(ExStringUtils.isNotEmpty(name)) {
			condition.put("name", name);
		}
		if(ExStringUtils.isNotEmpty(matriculateNoticeNo)) {
			condition.put("matriculateNoticeNo", matriculateNoticeNo);
		}
		
		Page page = studentInfoService.findStuByCondition(condition, objPage);
		if(ExCollectionUtils.isNotEmpty(page.getResult())){
			Map<String, String> courseMap = new HashMap<String, String>();
			for (Object obj : page.getResult()) {
				StudentInfo stu = (StudentInfo) obj;
				if(stu.getTeachingPlan()!=null){
					for (TeachingPlanCourse c : stu.getTeachingPlan().getTeachingPlanCourses()) {
						if("thesis".equals(c.getCourseType())){
							courseMap.put(stu.getResourceid(), c.getCourse().getResourceid()+"|"+c.getCourse().getCourseName());
							break;
						}
					}
				}
			}
			model.addAttribute("courseMap", courseMap);
		}
		
		model.addAttribute("stulist", page);
		model.addAttribute("condition", condition);
		return "/edu3/teaching/graduatePapers/schoolroll3-list";
	}
	
	/** 选择课程 */
	@RequestMapping("/edu3/teaching/graduatePapers/chooseStudentCourse.html")
	public void exeChooseStudentCourse(HttpServletRequest request, HttpServletResponse response) throws WebException {
		String studentId = request.getParameter("studentId");//学生id
		String hql = "select t from TeachingPlanCourse t,TeachingGuidePlan p where t.courseType='thesis' and t.teachingPlan.resourceid=p.teachingPlan.resourceid and t.teachingPlan.major.resourceid=? and t.teachingPlan.classic.resourceid=? and p.grade.resourceid=?";
		Course c = null;
		if(ExStringUtils.isNotEmpty(studentId)){
			StudentInfo stu = studentInfoService.get(studentId);
			if(null!=stu){
				TeachingPlanCourse course = teachingPlanCourseService.findUnique(hql, new String[]{stu.getMajor().getResourceid(),stu.getClassic().getResourceid(),stu.getGrade().getResourceid()});
				if(null!=course) {
					c = course.getCourse();
				}
			}
		}
		String jsonC = ",";
		if(null != c) {
			jsonC = c.getResourceid()+","+c.getCourseName();
		}
		renderJson(response,jsonC);
	}
	
	/**
	 * 选择已分配的老师
	 * @param mentor
	 * @param batchId
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/graduatePapers/chooseTeacher.html")
	public String exeList(String mentor, String batchId, Page objPage, ModelMap model) throws WebException{
		objPage.setOrderBy("resourceid");
		objPage.setOrder(Page.ASC);//设置默认排序方式
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件
		
		if(ExStringUtils.isNotEmpty(mentor)) {
			condition.put("mentor", mentor);
		}
		if(ExStringUtils.isNotEmpty(batchId)) {
			condition.put("batchId", batchId);
		}

		Page page = graduateMentorService.findGraduateMentorByCondition(condition, objPage);
		
		model.addAttribute("condition", condition);
		model.addAttribute("mentorList", page);
		return "/edu3/teaching/graduatePapers/graduateMentor3-list";
	}
	
	/**
	 * 预约统计
	 * @param branchSchool
	 * @param major
	 * @param batchId
	 * @param classic
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/graduatePapers/listStatis.html")
	public String exeListStatis(String branchSchool, String major, String batchId, String classic, Page objPage, ModelMap model) throws WebException{
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件
			
		if(ExStringUtils.isNotEmpty(batchId)) {
			condition.put("batchId", batchId);
		}
		if(ExStringUtils.isNotEmpty(major)) {
			condition.put("major", major);
		}
		if(ExStringUtils.isNotEmpty(classic)) {
			condition.put("classic", classic);
		}
		
		User cureentUser = SpringSecurityHelper.getCurrentUser();
		//招生人员--学习中心
		if(cureentUser.getOrgUnit().getUnitType().equals(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())){
			branchSchool = cureentUser.getOrgUnit().getResourceid();
		    model.addAttribute("isBrschool", true);
		}
		if(ExStringUtils.isNotEmpty(branchSchool)){		
			condition.put("branchSchool", branchSchool);
		}
		
		
		Page pagelist = graduatePapersOrderService.countGradePaperOrderByCondition(objPage, condition);		
		
		model.addAttribute("condition", condition);
		model.addAttribute("stuList", pagelist);
		return "/edu3/teaching/graduatePapers/graduatePapersStatis-list";
	}
	
	/**
	 * 查看统计详细信息
	 * @param resourceid
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/graduatePapers/viewStatis.html")
	public String exeView(String resourceid,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		if(ExStringUtils.isNotBlank(resourceid)){
			String[] ids = resourceid.split(",");
			String hql = "from "+GraduatePapersOrder.class.getSimpleName()+" o where " +
					"o.isDeleted=0 and o.examSub.resourceid=? and o.studentInfo.branchSchool.resourceid=? " +
					"and o.studentInfo.major.resourceid=? and o.studentInfo.classic.resourceid=? order by teacher.teacherCode";
			List stuList = graduatePapersOrderService.findByHql(hql, new Object[]{ids[0],ids[1],ids[2],ids[3]});
			model.addAttribute("stuList", stuList);
		}
		return "/edu3/teaching/graduatePapers/graduatePapersStatis-form";
	}

	/**
	 * 导出毕业论文预约数据
	 * @param batchId
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/graduatepapersorder/export.html")
	public void exportGraduatePapersOrder(String batchId, HttpServletResponse response) throws WebException {
		if(ExStringUtils.isNotBlank(batchId)){
			List<GraduatePapersOrderVo> list = graduatePapersOrderService.findGraduatePapersOrderVoByBatchId(batchId);
			List<MergeCellsParam> mergeCellsParams = new ArrayList<MergeCellsParam>();//合并坐标
			ExamSub thesis = examSubService.get(batchId);
			String batchName = thesis.getBatchName();
			int startRow = 3;//导出起始行
			
			fetchMergeCellsParams(list, mergeCellsParams, startRow,0);
			fetchMergeCellsParams(list, mergeCellsParams, startRow,1);
			fetchMergeCellsParams(list, mergeCellsParams, startRow,2);
			
			//文件输出服务器路径
			setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
			try{
				//导出
				File excelFile = null;
				GUIDUtils.init();
				File disFile = new File(getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(false) + ".xls");
				Map<String,Object> templateMap = new HashMap<String, Object>();//设置模板
				templateMap.put("batchName", batchName);
				templateMap.put("schoolAndConnect", CacheAppManager.getSysConfigurationByCode("graduateData.schoolName").getParamValue()+
						CacheAppManager.getSysConfigurationByCode("graduateData.schoolConnectName").getParamValue());
				//模板文件路径
				String templateFilepathString = "graduatePapersOrder.xls";
				
				//初始化配置参数
				exportExcelService.initParamsByfile(disFile, "graduatePapersOrder", list,null,null,mergeCellsParams);
				exportExcelService.getModelToExcel().setTemplateParam(templateFilepathString, startRow, templateMap);
				WritableFont font = new WritableFont(WritableFont.TIMES, 10);
		        WritableCellFormat format = new WritableCellFormat(font);
		        try {
		            format.setAlignment(jxl.format.Alignment.CENTRE);
		            format.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
		            format.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);
		            format.setWrap(true);//单元格内容自动换行
		        } catch (WriteException e) {
		            logger.error(e.getMessage(),e.fillInStackTrace());
		        }
				exportExcelService.getModelToExcel().setNormolCellFormat(format);
				excelFile = exportExcelService.getExcelFile();//获取导出的文件

				logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());
				downloadFile(response, batchName+".xls", excelFile.getAbsolutePath(),true);
			}catch(Exception e){			
				logger.error("导出excel文件出错："+e.fillInStackTrace());
			}
		}		
	}

	private void fetchMergeCellsParams(List<GraduatePapersOrderVo> list, List<MergeCellsParam> mergeCellsParams, int startRow,int type) {
		int start = 0;
		String tempStr = null;
		for (int i = 0; i < list.size(); i++) {
			GraduatePapersOrderVo vo = list.get(i);
			String str = null;
			if(type==0){
				str = vo.getMajorName();
			} else if(type==1){
				str = vo.getMajorName()+vo.getTeacherInfo();
			} else {
				str = vo.getMajorName()+vo.getTeacherInfo()+vo.getUnitShortName();
			}
			if(!ExStringUtils.equals(str, tempStr) && tempStr!=null){
				if(i-start>1){
					mergeCellsParams.add(new MergeCellsParam(type,start+startRow,type,i+startRow-1));						
				}									
				start = i;
			}	
			if(i==list.size()-1 && i-start>0){
				mergeCellsParams.add(new MergeCellsParam(type,start+startRow,type,i+startRow));
			}
			tempStr = str;
		}
	}
	
	@Autowired
	@Qualifier("graduatepapersorderservice")
	private IGraduatePapersOrderService graduatePapersOrderService;
	@Autowired
	@Qualifier("studentinfoservice")
	private IStudentInfoService studentInfoService;
	@Autowired
	@Qualifier("examSubService")
	private IExamSubService examSubService;
	@Autowired
	@Qualifier("courseService")
	private ICourseService courseService;
	@Autowired
	@Qualifier("graduatementorservice")
	private IGraduateMentorService graduateMentorService;
	@Autowired
	@Qualifier("teachingPlanCourseService")
	private ITeachingPlanCourseService teachingPlanCourseService;
	@Autowired
	@Qualifier("exportExcelService")
	private IExportExcelService exportExcelService;
	
	
}
