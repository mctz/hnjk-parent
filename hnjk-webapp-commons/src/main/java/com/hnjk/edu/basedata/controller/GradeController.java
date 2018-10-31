package com.hnjk.edu.basedata.controller;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.utils.ExBeanUtils;
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.HttpHeaderUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.BaseSupportController;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.Grade;
import com.hnjk.edu.basedata.model.YearInfo;
import com.hnjk.edu.basedata.service.IGradeService;
import com.hnjk.edu.basedata.service.IYearInfoService;
import com.hnjk.platform.system.UserOperationLogsHelper;
import com.hnjk.platform.system.model.UserOperationLogs;

/**
 * 年级设置
 * <code>GradeController</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2010-4-7 上午11:33:29
 * @see 
 * @version 1.0
 */
@Controller
public class GradeController extends BaseSupportController{

	private static final long serialVersionUID = -8282117512486397868L;
	
	@Autowired
	@Qualifier("gradeService")
	private IGradeService gradeService;
	
	@Autowired
	@Qualifier("yearInfoService")
	private IYearInfoService yearInfoService;
	
	/**
	 * 查询分页列表
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/sysmanager/grade/list.html")
	public String exeList(String gradeName,Page objPage,ModelMap model) throws WebException{
		objPage.setOrderBy("yearInfo.firstYear desc,term desc,resourceid");
		objPage.setOrder(Page.DESC);//设置默认排序方式
		
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件
		if(ExStringUtils.isNotEmpty(gradeName)) {
			condition.put("gradeName", gradeName);
		}
		
		Page page = gradeService.findGradeByCondition(condition, objPage);
		
		model.addAttribute("gradeList", page);
		model.addAttribute("condition", condition);
		return "/edu3/basedata/grade/grade-list";
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
	@RequestMapping("/edu3/sysmanager/grade/edit.html")
	public String exeEdit(String resourceid,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		if(ExStringUtils.isNotBlank(resourceid)){ //-----编辑
			Grade grade = gradeService.load(resourceid);	
			model.addAttribute("grade", grade);
		}else{ //----------------------------------------新增
			model.addAttribute("grade", new Grade());			
		}
		long year  = ExDateUtils.getCurrentYear();
		long startYear = year-10;
		model.addAttribute("year", year);		
		model.addAttribute("startYear", startYear);		
		return "/edu3/basedata/grade/grade-form";
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
	@RequestMapping("/edu3/sysmanager/grade/save.html")
	public void exeSave(Grade grade,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			Grade persistGrade = null;
			List<Grade> list = gradeService.findByCriteria(Restrictions.eq("isDeleted",0),Restrictions.eq("isDefaultGrade","Y"));
			
			if(ExStringUtils.isNotBlank(grade.getResourceid())){ //--------------------更新
				if ("Y".equals(grade.getIsDefaultGrade())) {
					if (null!=list&&list.size()>0 && !grade.getResourceid().equals(list.get(0).getResourceid())) {
						throw new WebException("同一时间只允许设置一个默认年级！");
					}
				}else if ("N".equals(grade.getIsDefaultGrade())) {
					if (list.size()==0) {
						throw new WebException("当前时间未设置默认年级,请设定一个默认年级！");
					}
				}
				persistGrade = gradeService.get(grade.getResourceid());
				
				if(ExStringUtils.isNotBlank(grade.getYearInfoId())){
					YearInfo yearInfo = yearInfoService.get(grade.getYearInfoId());
					grade.setYearInfo(yearInfo);
				}
				ExBeanUtils.copyProperties(persistGrade, grade);
				//gradeService.update(persistGrade);
			}else{ //-------------------------------------------------------------------保存
				List<Grade> list1 = gradeService.findByCriteria(Restrictions.eq("isDeleted",0),Restrictions.eq("term", grade.getTerm()),Restrictions.eq("yearInfo.resourceid", grade.getYearInfoId()));
				if (list1!=null&&list1.size()>0) {
					throw new WebException("已存在相同的年度与学期的年级");
				}else if (list.size()==0 && "N".equals(grade.getIsDefaultGrade())) {
					throw new WebException("当前时间未设置默认年级,请设定一个默认年级！");
					//throw new WebException("当前时间未设置默认年级,请设定一个默认年级！");
				}else if ("Y".equals(grade.getIsDefaultGrade()) && null!=list && list.size()>0 ) {
					throw new WebException("同一时间只允许设置一个默认年级！");
				}
				persistGrade = new Grade();				
				if(ExStringUtils.isNotBlank(grade.getYearInfoId())){
					YearInfo yearInfo = yearInfoService.load(grade.getYearInfoId());
					grade.setYearInfo(yearInfo);
				}
				ExBeanUtils.copyProperties(persistGrade, grade);
				//gradeService.save(grade);
			}			
			List<Grade> listg=gradeService.findByCriteria(Restrictions.eq("isDeleted", 1),Restrictions.eq("term", grade.getTerm()),Restrictions.eq("yearInfo.resourceid", grade.getYearInfoId()));
			if (listg!=null&&listg.size()>0) {
				Grade gradeForUpdate=listg.get(0);
				gradeForUpdate.setTerm(grade.getTerm());
				gradeForUpdate.setYearInfo(grade.getYearInfo());
				gradeForUpdate.setYearInfoId(grade.getYearInfoId());
				gradeForUpdate.setMemo(grade.getMemo());
				gradeForUpdate.setGraduateDate(grade.getGraduateDate());
				gradeForUpdate.setIndate(grade.getIndate());
				gradeForUpdate.setIsDefaultGrade(grade.getIsDefaultGrade());
				gradeForUpdate.setIsBookingExame(grade.getIsBookingExame());
				gradeForUpdate.setIsDeleted(0);
				gradeService.saveOrUpdate(gradeForUpdate);
			}else{

				gradeService.saveOrUpdate(persistGrade);
			}
			UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "7", UserOperationLogs.UPDATE,"更新年级：参数："+JsonUtils.mapToJson(getConditionFromResquestByIterator(request)));
//			gradeService.saveOrUpdate(persistGrade);
			map.put("statusCode", 200);
			map.put("message", "保存成功！");
			map.put("navTabId", "RES_BASEDATA_MANAGER_GRADE");
			map.put("reloadUrl", request.getContextPath() +"/edu3/sysmanager/grade/edit.html?resourceid="+persistGrade.getResourceid());
		}catch (Exception e) {
			logger.error("保存年级出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败:<br/>"+e.getMessage());
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
	@RequestMapping("/edu3/sysmanager/grade/delete.html")
	public void exeDelete(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		String resourceid = request.getParameter("resourceid");
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(resourceid)){
				if(resourceid.split("\\,").length >1){//批量删除					
					gradeService.batchCascadeDelete(resourceid.split("\\,"));
				}else{//单个删除
					gradeService.delete(resourceid);
				}
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "7", UserOperationLogs.DELETE,"删除年级：参数："+JsonUtils.mapToJson(getConditionFromResquestByIterator(request)));
				map.put("statusCode", 200);
				map.put("message", "删除成功！");				
				map.put("forward", request.getContextPath()+"/edu3/sysmanager/grade/list.html");
			}
		} catch (Exception e) {
			logger.error("删除栏目出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 设置/取消默认年级
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/sysmanager/grade/setdefaultgrade.html")
	public void setDefaultGrade(String resourceid,String flag,HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> map   = new HashMap<String, Object>();
		try {
			Grade grade 	     = null;
			if (Constants.BOOLEAN_YES.equals(ExStringUtils.trimToEmpty(flag))) {
				List<Grade> list = gradeService.findByCriteria(Restrictions.eq("isDeleted",0),Restrictions.eq("isDefaultGrade","Y"),Restrictions.ne("resourceid", resourceid));
				if (list.size()>0) {
					throw new WebException("同一时间只允许设置一个默认年级！");
				}
				grade = gradeService.get(resourceid);
				grade.setIsDefaultGrade(Constants.BOOLEAN_YES);
			}else {
				grade = gradeService.get(resourceid);
				grade.setIsDefaultGrade(Constants.BOOLEAN_NO);
			}
			gradeService.update(grade);
			map.put("message", "设置成功！");
			map.put("statusCode", 200);
			map.put("navTabId", "RES_BASEDATA_GRADE");
			map.put("reloadUrl",request.getContextPath() +"/edu3/sysmanager/grade/list.html");
		} catch (Exception e) {
			logger.debug("设置/取消默认年级失败："+e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "设置/取消默认年级出错:"+e.getMessage());
		}
		renderJson(response,JsonUtils.mapToJson(map));
	}
	
	public static Map<String, Object> getConditionFromResquestByIterator(HttpServletRequest request) {
		Map<String, Object> condition = new HashMap<String, Object>();
		Enumeration em = request.getParameterNames();
		while (em.hasMoreElements()) {
			String name = (String) em.nextElement();
			Object value = request.getParameter(name);
			if(value!=null && !"undefined".equals(value) && !"".equals(value)){
				condition.put(name, value);
			}
		}
		//当 conditon 中包含 pageNum 和 pageSize 时，点击翻页时，request中将有多个pageNum和pageSize，因此，condition中移除多余的参数
		if(condition.containsKey("pageNum")){
			condition.remove("pageNum");
		}
		if(condition.containsKey("pageSize")){
			condition.remove("pageSize");
		}
		return condition;
	}
}
