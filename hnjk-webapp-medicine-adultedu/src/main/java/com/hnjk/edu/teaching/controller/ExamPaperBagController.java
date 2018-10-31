package com.hnjk.edu.teaching.controller;

import java.io.File;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.GUIDUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.FileUploadAndDownloadSupportController;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.teaching.model.ExamInfo;
import com.hnjk.edu.teaching.model.ExamPaperBag;
import com.hnjk.edu.teaching.model.ExamSub;
import com.hnjk.edu.teaching.service.IExamPaperBagService;
import com.hnjk.edu.teaching.service.IExamSubService;
import com.hnjk.edu.teaching.service.ITeachingJDBCService;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.taglib.JstlCustomFunction;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;

/**
 * 试卷袋标签controller.
 * <code>CourseBookController</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2011-10-30 上午11:04:32
 * @see 
 * @version 1.0
 */
@Controller
public class ExamPaperBagController extends FileUploadAndDownloadSupportController {
	
	private static final long serialVersionUID = 9099355071161413238L;
	
	@Autowired
	@Qualifier("examPaperBagService")
	private IExamPaperBagService examPaperBagService;
	
	@Autowired
	@Qualifier("teachingJDBCService")
	private ITeachingJDBCService teachingJDBCService;
	
	@Autowired
	@Qualifier("examSubService")
	private IExamSubService examSubService;
	/**
	 * 查询试卷袋标签列表
	 * @param request
	 * @param page
	 * @param molde
	 * @return
	 */
	@RequestMapping("/edu3/teaching/exam/paperbag/list.html")
	public String examPaperBagList(HttpServletRequest request,Page page,ModelMap molde){
		
		page.setOrderBy(" bag.examInfo.examCourseCode asc ,bag.bagNum desc");
		
		Map<String,Object> condition = new HashMap<String, Object> ();
		String examsubId    		 = ExStringUtils.trimToEmpty(request.getParameter("examSub"));
		String courseId     		 = ExStringUtils.trimToEmpty(request.getParameter("courseId"));
		String branchSchool 		 = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));
		String totalBagCount         = ExStringUtils.trimToEmpty(request.getParameter("totalBagCount"));
		String isMachineExam         = ExStringUtils.trimToEmpty(request.getParameter("isMachineExam"));
		
		User user = SpringSecurityHelper.getCurrentUser();
		if(user.getOrgUnit().getUnitType().indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员
			branchSchool    		 = user.getOrgUnit().getResourceid();
		}
		
		if(ExStringUtils.isNotEmpty(examsubId)) {
			condition.put("examSub", examsubId);
		}
		if(ExStringUtils.isNotEmpty(courseId)) {
			condition.put("courseId", courseId);
		}
		if(ExStringUtils.isNotEmpty(branchSchool)) {
			condition.put("branchSchool", branchSchool);
		}
		if(ExStringUtils.isNotEmpty(totalBagCount)) {
			condition.put("totalBagCount", totalBagCount);
		}
		if(ExStringUtils.isNotEmpty(isMachineExam)) {
			condition.put("isMachineExam", isMachineExam);
		}
		
		if (!condition.isEmpty()) {
			page                         = examPaperBagService.findExamPaperBagByCondition(page, condition);
			if (ExStringUtils.isEmpty(totalBagCount)) {
				List<Object> list            = examPaperBagService.findExamPaperBagCountByCondition(condition);
				condition.put("totalBagCount", list.get(0));
			}
		}
		molde.put("condition", condition);
		molde.put("page", page);
		
		return "/edu3/teaching/examPaperBag/examPaperBag-list";
	}
	/**
	 * 查询试卷袋标签明细列表
	 * @param request
	 * @param page
	 * @param molde
	 * @return
	 */
	@RequestMapping("/edu3/teaching/exam/paperbagdetails/list.html")
	public String examPaperBagDetailsList(HttpServletRequest request,Page page,ModelMap model){
		String examPaperBagId      = ExStringUtils.trimToEmpty(request.getParameter("examPaperBagId"));
		if (ExStringUtils.isNotEmpty(examPaperBagId)) {
			ExamPaperBag bag       = examPaperBagService.get(examPaperBagId);
			model.put("bag", bag);
		}
		return "/edu3/teaching/examPaperBag/examPaperBagDetails-list";
	}
	/**
	 * 生成试卷袋标数据-表单
	 * @param request
	 * @param molde
	 * @return
	 */
	@RequestMapping("/edu3/teaching/exam/paperbag/edit.html")
	public String examPagerBagForm(HttpServletRequest request,ModelMap model){
		
		Map<String,Object> condition   = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		
		String backupCoefficient       = CacheAppManager.getSysConfigurationByCode("examPaper.backupCoefficient").getParamValue();
		String examSubId               = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
		if (ExStringUtils.isNotEmpty(examSubId)){
			condition.put("examSubId", examSubId);
			ExamSub sub 			   = examSubService.get(examSubId);
			model.put("examSub", sub);
		}
		condition.put("statDirection","forPage");
		if (ExStringUtils.isNotEmpty(examSubId)) {
			list                       = teachingJDBCService.findCourseExaminationNum(condition);
		}
		
		model.put("list",list);
		model.put("backupCoefficient",backupCoefficient);
	
		return "/edu3/teaching/examPaperBag/examPaperBag-form";
	}
	
	/**
	 * 生成试卷袋标数据
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/teaching/exam/paperbag/gen.html")
	public void genExamPagerBag(HttpServletRequest request,HttpServletResponse response){
		
		Map<String,Object> map   = new HashMap<String, Object>();
		String msg               = "";
		String examSub           = ExStringUtils.trimToEmpty(request.getParameter("examSub"));
		String backupCoefficient = ExStringUtils.trimToEmpty(request.getParameter("backupCoefficient"));
		boolean isSuccess        = false;
		try {
			if (ExStringUtils.isNotEmpty(examSub)&&ExStringUtils.isNotEmpty(backupCoefficient)) {
				map.put("examSubId",examSub);
				map.put("backupCoefficient", backupCoefficient);
				examPaperBagService.genExamPagerBag(map);
				isSuccess        = true;
			}else {
				isSuccess        = false;
				msg              = "请传入合法的参数！";
			}
		} catch (Exception e) {
			isSuccess            = false;
			msg                  = "生成试卷袋标数据出错:{}"+e.fillInStackTrace();
			logger.error("生成试卷袋标数据出错:{}"+e.fillInStackTrace());
		}
		map.put("isSuccess", isSuccess);
		map.put("msg", msg);
		
		renderJson(response,JsonUtils.mapToJson(map));
	}
	/**
	 * 打印试卷袋标签-按课程打印
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/teaching/exam/paperbag/print-bycourse.html")
	public String printExamPaperBagByCourse(HttpServletRequest request,ModelMap model,Page page){
		
		Map<String, Object> condition = new HashMap<String, Object>();
		String examSubId 			  = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
		String courseId               = ExStringUtils.trimToEmpty(request.getParameter("courseId"));
		String isMachineExam          = ExStringUtils.trimToEmpty(request.getParameter("isMachineExam"));
		String branchSchool 		  = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));
		
		String timeSegment  		  = ExStringUtils.trimToEmpty(request.getParameter("examSub_examTimeSegment"));
		Date startTime     			  = null;
		Date endTime       			  = null;
		
		if (ExStringUtils.isNotEmpty(timeSegment)) {
			condition.put("examSub_examTimeSegment", timeSegment);
			String [] segment = timeSegment.split("TO");
			if (null!=segment && segment.length>1) {
				String s_t     = segment[0];
				String e_t     = segment[1];
				startTime      = ExDateUtils.convertToDateTime(s_t);
				endTime        = ExDateUtils.convertToDateTime(e_t);
			}
		}
		
		if (null!=startTime) {
			condition.put("startTime",    startTime);
		}
		if (null!=startTime) {
			condition.put("endTime",      endTime);
		}
		if (ExStringUtils.isNotEmpty(courseId)) {
			condition.put("courseId",     courseId);
		}
		if (ExStringUtils.isNotEmpty(isMachineExam)) {
			condition.put("isMachineExam",isMachineExam);
		}
		if (ExStringUtils.isNotEmpty(branchSchool)) {
			condition.put("branchSchool",branchSchool);
		}
		
		//获取所选择考试批次的考试时间段
		if (ExStringUtils.isNotEmpty(examSubId)){
			List<Map<String, Object>> list = teachingJDBCService.findExamTimeSegment(examSubId,"","");
			model.addAttribute("timeSegmentList",list);
			
		} 
		
		if (ExStringUtils.isNotEmpty(examSubId)) {
			condition.put("examSubId", examSubId);
			condition.put("printByCourse", "printByCourse");
			
			page = teachingJDBCService.findExamPaperBagInfoForPrint(page, condition);
		}
		if (condition.containsKey("startTime")) {
			condition.remove("startTime");
		}
		if (condition.containsKey("endTime")) {
			condition.remove("endTime");
		}
		model.put("page", page);
		model.put("condition", condition);	
		
		return "/edu3/teaching/examPaperBag/print-examPaperBag-byCourse";
	}
	/**
	 * 打印试卷袋标签-按学习中心打印
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/teaching/exam/paperbag/print-byschool.html")
	public String printExamPaperBagByBrschool(HttpServletRequest request,ModelMap model,Page page){
		
		Map<String, Object> condition = new HashMap<String, Object>();
		String examSubId 			  = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
		String branchSchool 		  = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));
		String isMachineExam          = ExStringUtils.trimToEmpty(request.getParameter("isMachineExam"));
		if (ExStringUtils.isNotEmpty(branchSchool)) {
			condition.put("branchSchool",     branchSchool);
		}
		if (ExStringUtils.isNotEmpty(isMachineExam)) {
			condition.put("isMachineExam",isMachineExam);
		}
		
		if (ExStringUtils.isNotEmpty(examSubId)) {
			condition.put("examSubId", examSubId);
			condition.put("printByBrschool", "printByBrschool");
			page = teachingJDBCService.findExamPaperBagInfoForPrint(page, condition);
		}
		
		model.put("page", page);
		model.put("condition", condition);
		
		return "/edu3/teaching/examPaperBag/print-examPaperBag-byBrschool";
	}
	
	/**
	 * 打印试卷袋标签-预览
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/teaching/exam/paperbag/print-view.html")
	public String printExamPaperBagView(HttpServletRequest request,ModelMap model){
		
		model.put("flag", ExStringUtils.trimToEmpty(request.getParameter("flag")));
		model.put("examInfoIds", ExStringUtils.trimToEmpty(request.getParameter("examInfoIds")));
		model.put("examSubId", ExStringUtils.trimToEmpty(request.getParameter("examSubId")));
		model.put("unitIds", ExStringUtils.trimToEmpty(request.getParameter("unitIds")));
		model.put("isMachineExam", ExStringUtils.trimToEmpty(request.getParameter("isMachineExam")));
		
		model.put("examPaperBagDetailsId", ExStringUtils.trimToEmpty(request.getParameter("examPaperBagDetailsId")));
		
		return "/edu3/teaching/examPaperBag/examPaperBag-printview";
	}
	/**
	 * 打印试卷袋标签
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/teaching/exam/paperbag/print.html")
	public void printExamPaperBag(HttpServletRequest request,HttpServletResponse response){
		
		Map<String,Object> map 	     = new HashMap<String, Object>();
		JasperPrint jasperPrint	     = null;//输出的报表
		
		String flag 	  	   	     = ExStringUtils.trimToEmpty(request.getParameter("flag"));
		String examSubId  	   	     = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
		String unitIds    	   	     = ExStringUtils.trimToEmpty(request.getParameter("unitIds"));
		String examInfoIds 	   	     = ExStringUtils.trimToEmpty(request.getParameter("examInfoIds"));
		String examPaperBagDetailsId = ExStringUtils.trimToEmpty(request.getParameter("examPaperBagDetailsId"));
		String isMachineExam         = ExStringUtils.trimToEmpty(request.getParameter("isMachineExam"));
		
		if (ExStringUtils.isNotEmpty(isMachineExam)) {
			map.put("isMachineExam",isMachineExam);
		}
		if (ExStringUtils.isNotEmpty(examPaperBagDetailsId)) {
			map.put("examPaperBagDetailsId",examPaperBagDetailsId);
		}
		if (ExStringUtils.isNotEmpty(unitIds)){
			StringBuffer ids = new StringBuffer();
			for (String id:unitIds.split(",")) {
				ids.append(",'"+id+"'");
			}
			map.put("unidIds", ids.substring(1));
		}
			
		if (ExStringUtils.isNotEmpty(examInfoIds)){
			StringBuffer ids = new StringBuffer();
			for (String id:examInfoIds.split(",")) {
				ids.append(",'"+id+"'");
			}
			 map.put("examInfoIds", ids.substring(1));
		}

		if (ExStringUtils.isNotEmpty(examSubId)) {
			
			try {
				map.put("examSubId", examSubId);
				String dymicSQL        = getExamPaperBagPrintDymicSQL(flag,map);
				map.put("dynamicSQL", dymicSQL);
				
				String reprotFile  	   = URLDecoder.decode(request.getSession().getServletContext().getRealPath(File.separator+"reports"+
			  		     				 File.separator+"examPrint"+File.separator+"examPaperBag.jasper"),	"utf-8");;
			  		     				 		
				File reprot_file   	   = new File(reprotFile);
				jasperPrint			   = examPaperBagService.printReport(reprot_file.getPath(), map, examPaperBagService.getConn());
				
				if (null!=jasperPrint) {
					renderStream(response, jasperPrint);
				}else {
					renderHtml(response,"缺少打印数据！");
				} 
				
			} catch (Exception e) {
				logger.error("打印试卷袋标签出错:{}"+e.fillInStackTrace());
				renderHtml(response,"打印试卷袋标签出错:"+e.getMessage());
			}
		}else {
			renderHtml(response,"请输入合法参数！");
		}
		
	}
	/**
	 * 获取打印试卷袋标签动态部分的SQL
	 * @param flag
	 * @param condition
	 * @return
	 */
	private String getExamPaperBagPrintDymicSQL(String flag,Map<String,Object> condition){
		
		StringBuffer sql = new StringBuffer();
		
		
		
		//根据课程打印标签	
		if ("printByCourse".equals(flag)) {
			
			sql.append(" inner join edu_teach_examinfo info on pbs.examinfoid = info.resourceid ");
			if (condition.containsKey("examInfoIds")) {
				sql.append(" and info.resourceid in("+condition.get("examInfoIds")+")");
			}
			if (condition.containsKey("isMachineExam")) {
				sql.append(" and info.ismachineexam='"+condition.get("isMachineExam")+"'");
			}
			sql.append(" inner join edu_teach_examsub sub on info.examsubid = sub.resourceid ");
			if (condition.containsKey("examSubId")) {
				sql.append(" and sub.resourceid='"+condition.get("examSubId")+"'");
			}
			sql.append(" inner join edu_base_course c on info.courseid = c.resourceid ");
			sql.append(" inner join hnjk_sys_unit u on pbs.unitid = u.resourceid ");
			sql.append(" left join edu_base_examroom room on pbd.examroomid = room.resourceid ");
			sql.append(" where pbd.isdeleted = 0 ");
			sql.append(" order by info.examstarttime,info.examcoursecode");
			
		//根据学习中心打印标签	
		}else if ("printByBrschool".equals(flag)) {
			
			sql.append(" inner join edu_teach_examinfo info on pbs.examinfoid = info.resourceid ");
			if (condition.containsKey("isMachineExam")) {
				sql.append(" and info.ismachineexam='"+condition.get("isMachineExam")+"'");
			}
			sql.append(" inner join edu_teach_examsub sub on info.examsubid = sub.resourceid ");
			if (condition.containsKey("examSubId")) {
				sql.append(" and sub.resourceid='"+condition.get("examSubId")+"'");
			}
			sql.append(" inner join edu_base_course c on info.courseid = c.resourceid ");
			sql.append(" inner join hnjk_sys_unit u on pbs.unitid = u.resourceid ");
			
			if (condition.containsKey("unidIds")) {
				sql.append(" and u.resourceid in("+condition.get("unidIds")+")");
			}
			sql.append(" left join edu_base_examroom room on pbd.examroomid = room.resourceid ");
			sql.append(" where pbd.isdeleted = 0 ");
			sql.append(" order by u.unitname,info.examstarttime,info.examcoursecode");
		
		//打印单个标签		
		}else if("printBySingleId".equals(flag)){
			
			sql.append(" inner join edu_teach_examinfo info on pbs.examinfoid = info.resourceid ");
			sql.append(" inner join edu_teach_examsub sub on info.examsubid = sub.resourceid ");
			sql.append(" inner join edu_base_course c on info.courseid = c.resourceid ");
			sql.append(" inner join hnjk_sys_unit u on pbs.unitid = u.resourceid ");
			sql.append(" left join edu_base_examroom room on pbd.examroomid = room.resourceid ");
			sql.append(" where pbd.isdeleted = 0 ");
			if (condition.containsKey("examPaperBagDetailsId")) {
				sql.append(" and   pbd.resourceid = '"+condition.get("examPaperBagDetailsId")+"'");
			}
		}
		
		return sql.toString();
	}
	/**
	 *  打印试卷袋标签(汇总、明细)-预览
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/teaching/exam/statpaperbag/print-view.html")
	public String printStatExamPaperBagView(HttpServletRequest request,ModelMap model){
		model.put("flag",ExStringUtils.trimToEmpty(request.getParameter("flag")));
		model.put("examSubId",ExStringUtils.trimToEmpty(request.getParameter("examSubId")));
		model.put("unitId",ExStringUtils.trimToEmpty(request.getParameter("unitId")));
		model.put("courseId",ExStringUtils.trimToEmpty(request.getParameter("courseId")));
		model.put("examPaperBagStatId",ExStringUtils.trimToEmpty(request.getParameter("examPaperBagStatId")));
		model.put("isMachineExam",ExStringUtils.trimToEmpty(request.getParameter("isMachineExam")));
		
		return "/edu3/teaching/examPaperBag/statExamPaperBag-printview";
	}
	/**
	 *  打印试卷袋标签(汇总、明细)
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/teaching/exam/statpaperbag/print.html")
	public void printStatExamPaperBag(HttpServletRequest request,HttpServletResponse response){
		
		Map<String, Object> param = new HashMap<String, Object>();
		String examSubId          = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
		String flag               = ExStringUtils.trimToEmpty(request.getParameter("flag"));
		String unitId             = ExStringUtils.trimToEmpty(request.getParameter("unitId"));
		String courseId           = ExStringUtils.trimToEmpty(request.getParameter("courseId"));
		String isMachineExam 	  = ExStringUtils.trimToEmpty(request.getParameter("isMachineExam"));
		String examPaperBagStatId = ExStringUtils.trimToEmpty(request.getParameter("examPaperBagStatId"));
		
		
		JasperPrint jasperPrint   = null;//输出的报表
		String reprotFile 	      = "";
		try {
			
			ExamSub  sub   	  	   = examSubService.get(examSubId);
			
			if ("printStat".equals(flag)) {
				reprotFile         = URLDecoder.decode(request.getSession().getServletContext().getRealPath(File.separator+"reports"+
						  			 File.separator+"examPrint"+File.separator+"examPaperBagStat.jasper"),"utf-8");
				
			}else if ("printDetails".equals(flag)) {

				reprotFile    	   = URLDecoder.decode(request.getSession().getServletContext().getRealPath(File.separator+"reports"+
				  					 File.separator+"examPrint"+File.separator+"examPaperBagDetails.jasper"),"utf-8");
				ExamInfo info 	   = examPaperBagService.get(examPaperBagStatId).getExamInfo();
				if(Constants.BOOLEAN_YES.equals(info.getIsMachineExam())){
					param.put("examMode", "机考");
				}else{
					param.put("examMode",JstlCustomFunction.dictionaryCode2Value("CodeCourseExamType",info.getCourse().getExamType().toString()));
				};
			}
			
			File reprot_file 	   = new File(reprotFile);
			if (ExStringUtils.isNotEmpty(unitId)) {
				param.put("unitId", unitId);
			}
			if (ExStringUtils.isNotEmpty(courseId)) {
				param.put("courseId", courseId);
			}
			if (ExStringUtils.isNotEmpty(examPaperBagStatId)) {
				param.put("examPaperBagStatId", examPaperBagStatId);
			}
			if (ExStringUtils.isNotEmpty(isMachineExam)) {
				param.put("isMachineExam", isMachineExam);
			}
			param.put("examSubName", sub.getBatchName());
			param.put("examSubId", sub.getResourceid());
			param.put("scutLogoPath", CacheAppManager.getSysConfigurationByCode("web.scutlogo.path").getParamValue());
			//param.put("scutLogoPath", "c:\\scut_logo.jpg");
			
			jasperPrint				= examPaperBagService.printReport(reprot_file.getPath(), param, examPaperBagService.getConn());

			if (null!=jasperPrint) {
				renderStream(response, jasperPrint);
			}else {
				renderHtml(response,"缺少打印数据！");
			} 
			
		} catch (Exception e) {
			logger.error(" 打印试卷袋标签(汇总、明细){}",e.fillInStackTrace());
			renderHtml(response, "打印试卷袋标签(汇总、明细)出错!"+e.fillInStackTrace());
		}
	}
	/**
	 * 导出试卷袋标签(汇总、明细)
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/teaching/exam/paperbag/export.html")
	public void exportExamPaperBag(HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> param = new HashMap<String, Object>();
		String examSubId          = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
		String flag               = ExStringUtils.trimToEmpty(request.getParameter("flag"));
		String unitId             = ExStringUtils.trimToEmpty(request.getParameter("unitId"));
		String courseId           = ExStringUtils.trimToEmpty(request.getParameter("courseId"));
		String isMachineExam      = ExStringUtils.trimToEmpty(request.getParameter("isMachineExam"));
		String examPaperBagStatId = ExStringUtils.trimToEmpty(request.getParameter("examPaperBagStatId"));

		JasperPrint jasperPrint   = null;//输出的报表
		String reprotFile 	      = "";
		String reprotName         = "";
		try {
			GUIDUtils.init();	
			String filePath        = CacheAppManager.getSysConfigurationByCode("web.uploadfile.rootpath").getParamValue()+File.separator+"exportfiles"+File.separator+GUIDUtils.buildMd5GUID(false)+".xls";
			//String filePath        = "D:\\aa.xls";
			
			ExamSub  sub   	  	   = examSubService.get(examSubId);
			
			if ("exportStat".equals(flag)) {
				reprotName         = sub.getBatchName()+"试卷袋标签汇总表";
				reprotFile         = URLDecoder.decode(request.getSession().getServletContext().getRealPath(File.separator+"reports"+
						  			 File.separator+"examPrint"+File.separator+"examPaperBagStat.jasper"),"utf-8");
				
			}else if ("exportDetails".equals(flag)) {
				reprotName         = "试卷袋标签明细表";
				reprotFile    	   = URLDecoder.decode(request.getSession().getServletContext().getRealPath(File.separator+"reports"+
				  					 File.separator+"examPrint"+File.separator+"examPaperBagDetails.jasper"),"utf-8");
				ExamInfo info 	   = examPaperBagService.get(examPaperBagStatId).getExamInfo();
				if(Constants.BOOLEAN_YES.equals(info.getIsMachineExam())){
					param.put("examMode", "机考");
				}else{
					param.put("examMode",JstlCustomFunction.dictionaryCode2Value("CodeCourseExamType",info.getCourse().getExamType().toString()));
				};
			}
			
			File reprot_file 	   = new File(reprotFile);
			
			if (ExStringUtils.isNotEmpty(unitId)) {
				param.put("unitId", unitId);
			}
			if (ExStringUtils.isNotEmpty(courseId)) {
				param.put("courseId", courseId);
			}
			if (ExStringUtils.isNotEmpty(isMachineExam)) {
				param.put("isMachineExam",isMachineExam);
			}
			if (ExStringUtils.isNotEmpty(examPaperBagStatId)) {
				param.put("examPaperBagStatId", examPaperBagStatId);
			}
			param.put("examSubName", sub.getBatchName());
			param.put("examSubId", sub.getResourceid());
					
			jasperPrint			   = examPaperBagService.printReport(reprot_file.getPath(), param, examPaperBagService.getConn());
			JRXlsExporter exporter = new JRXlsExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);  
			exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);  
			exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);  
			exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);  
			exporter.setParameter(JRXlsExporterParameter.OUTPUT_FILE_NAME,filePath);
			
			exporter.exportReport(); 
			
			downloadFile(response,reprotName+".xls",filePath,true);
			
		} catch (Exception e) {
			logger.error(" 导出试卷袋标签(汇总、明细){}",e.fillInStackTrace());
			renderHtml(response, "导出试卷袋标签(汇总、明细)出错!"+e.fillInStackTrace());
		}
	}
	
}
