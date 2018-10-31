package com.hnjk.edu.finance.controller;

import java.math.BigDecimal;
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
import com.hnjk.core.foundation.utils.BigDecimalUtil;
import com.hnjk.core.foundation.utils.ExBeanUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.BaseSupportController;
import com.hnjk.edu.basedata.service.IClassicService;
import com.hnjk.edu.basedata.service.IGradeService;
import com.hnjk.edu.basedata.service.IMajorService;
import com.hnjk.edu.finance.model.StudentFeeRule;
import com.hnjk.edu.finance.model.StudentFeeRuleDetails;
import com.hnjk.edu.finance.service.IStudentFeeRuleService;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;
import com.hnjk.security.service.IOrgUnitService;

/**
 * 年级预交费用设置表.
 * <code>StudentFeeRuleController</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2010-8-13 下午02:08:28
 * @see 
 * @version 1.0
 */
@Controller
public class StudentFeeRuleController extends BaseSupportController{
	
	private static final long serialVersionUID = -9143127361917897958L;
	
	/**
	 * 查询分页列表 年级预交费用设置列表方法
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/schoolroll/studentfeerule/list.html")
	public String exeList(String grade, String branchSchool, String major, String classic, Page objPage, ModelMap model) throws WebException{
		objPage.setOrderBy("resourceid");
		objPage.setOrder(Page.ASC);//设置默认排序方式
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件
		
		User user = SpringSecurityHelper.getCurrentUser();
		if(user.getOrgUnit().getUnitType().indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员
			branchSchool = user.getOrgUnit().getResourceid();
			model.addAttribute("isBrschool", true);
		}
		
		if(ExStringUtils.isNotEmpty(grade)) {
			condition.put("grade", grade);
		}
		if(ExStringUtils.isNotEmpty(branchSchool)) {
			condition.put("branchSchool", branchSchool);
		}
		if(ExStringUtils.isNotEmpty(major)) {
			condition.put("major", major);
		}
		if(ExStringUtils.isNotEmpty(classic)) {
			condition.put("classic", classic);
		}

		Page page = studentFeeRuleService.findFeeRuleByCondition(condition, objPage);
		
		model.addAttribute("condition", condition);
		model.addAttribute("feeList", page);
		return "/edu3/finance/studentfeerule/studentfeerule-list";
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
	@RequestMapping("/edu3/schoolroll/studentfeerule/edit.html")
	public String exeEidt(String resourceid,ModelMap model) throws WebException{
		if(ExStringUtils.isNotBlank(resourceid)){ //-----编辑
			StudentFeeRule fee = studentFeeRuleService.load(resourceid);	
			BigDecimal sum = studentFeeRuleService.AjaxSumPoint(fee.getGrade().getResourceid(), fee.getMajor().getResourceid(), fee.getClassic().getResourceid());
			model.addAttribute("fee", fee);
			model.addAttribute("sum", sum);
		}else{ //----------------------------------------新增
			model.addAttribute("fee", new StudentFeeRule());			
		}
		return "/edu3/finance/studentfeerule/studentfeerule-form";
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
	@RequestMapping("/edu3/schoolroll/studentfeerule/save.html")
	public void exeSave(StudentFeeRule fee,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(fee.getResourceid())){ //---------------------更新
				StudentFeeRule p_Fee = studentFeeRuleService.get(fee.getResourceid());
				ExBeanUtils.copyProperties(p_Fee, fee);
			//	saveRelation(p_Fee);
				relationFeeDetail(p_Fee, request);
				studentFeeRuleService.update(p_Fee,request);
			}else{ //-------------------------------------------------------------------保存
				saveRelation(fee);
				relationFeeDetail(fee, request);
				studentFeeRuleService.save(fee);
			}
			map.put("statusCode", 200);
			map.put("message", "保存成功！");
			map.put("navTabId", "RES_SCHOOLFEE_RULE");
			map.put("reloadUrl", request.getContextPath() +"/edu3/schoolroll/studentfeerule/edit.html?resourceid="+fee.getResourceid());
		}catch (Exception e) {
			logger.error("保存出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败:<br/>"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	
	/** 保存与子表之间的关系 **/
	private void relationFeeDetail(StudentFeeRule fee, HttpServletRequest request) {
		String[] feeDids = request.getParameterValues("feeDid");
		String[] feeTermNums = request.getParameterValues("feeTermNum");
		String[] feeScales = request.getParameterValues("feeScale");
		String[] showOrders = request.getParameterValues("showOrder");
		StudentFeeRuleDetails feeDetail =  null;
		if(feeDids!=null){
		for(int index=0;index<feeDids.length;index++){
			if(ExStringUtils.isNotEmpty(feeDids[index])){
				feeDetail = (StudentFeeRuleDetails) studentFeeRuleService.get(StudentFeeRuleDetails.class, feeDids[index]);
			}else{
				feeDetail = new StudentFeeRuleDetails();
			}
			feeDetail.setFeeTermNum(ExStringUtils.isEmpty(feeTermNums[index]) ? 0 : Integer.parseInt(feeTermNums[index]));
			feeDetail.setFeeScale(ExStringUtils.isEmpty(feeScales[index]) ? 0.0 : Double.parseDouble(feeScales[index]));
			feeDetail.setShowOrder(ExStringUtils.isEmpty(showOrders[index]) ? 0 : Integer.parseInt(showOrders[index]));
			
			feeDetail.setStudentFeeRule(fee);
			fee.getStudentFeeRuleDetails().add(feeDetail);
		
		}  } //end if
	}
	/** 建立对象之间的关系 **/
	private void saveRelation(StudentFeeRule fee) {
		if(ExStringUtils.isNotEmpty(fee.getGradeid()) && ExStringUtils.isNotEmpty(fee.getBranchSchoolId()) 
				&& ExStringUtils.isNotEmpty(fee.getMajorid()) && ExStringUtils.isNotEmpty(fee.getClassicid())){
			fee.setGrade(gradeService.get(fee.getGradeid()));
			fee.setBranchSchool(orgUnitService.get(fee.getBranchSchoolId()));
			fee.setMajor(majorService.get(fee.getMajorid()));
			fee.setClassic(classicService.get(fee.getClassicid()));
		
			BigDecimal sum = studentFeeRuleService.AjaxSumPoint(fee.getGradeid(), fee.getMajorid(), fee.getClassicid());
			fee.setTotalFee(BigDecimalUtil.mul(sum.doubleValue(), fee.getCreditFee()));
			fee.setFeeRuleName(fee.getGrade().getGradeName()+"-"+fee.getBranchSchool().getUnitName()
					+"-"+fee.getMajor().getMajorName()+"-"+fee.getClassic().getClassicName());
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
	@RequestMapping("/edu3/schoolroll/studentfeerule/delete.html")
	public void exeDelete(HttpServletRequest request,HttpServletResponse response) throws WebException{
		String resourceid = request.getParameter("resourceid");//--单个删除id
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(resourceid)){
				studentFeeRuleService.batchCascadeDelete(resourceid.split("\\,"));				
				map.put("statusCode", 200);
				map.put("message", "删除成功！");				
				map.put("forward", request.getContextPath()+"/edu3/schoolroll/studentfeerule/list.html");
			}
		} catch (Exception e) {
			logger.error("删除栏目出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错:<br/>"+e.getLocalizedMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 学生缴费标准设置明细
	 * @param request
	 * @param response
	 * @param model
	 * @throws WebException
	 */
	@RequestMapping("/edu3/schoolroll/studentfeerule/deleteDetail.html")
	public void exeDeleteDetail(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		String c_id = request.getParameter("c_id");
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(c_id)){
				studentFeeRuleService.deleteDetail(c_id);
			}
		} catch (Exception e) {
			logger.error("删除学生缴费标准设置明细出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	} 
	
	/**
	 * AJAX请求，计算总学分
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/schoolroll/studentfeerule/ajaxsumpoint.html")
	public void exeAjaxSumPoint(HttpServletRequest request,HttpServletResponse response) throws WebException{
		String grade = request.getParameter("grade"); //年级
		String major = request.getParameter("major"); //专业
		String classic = request.getParameter("classic"); //层级
		Map<String, Object> map = new HashMap<String, Object>();
		if(ExStringUtils.isNotEmpty(grade) && ExStringUtils.isNotEmpty(major) && ExStringUtils.isNotEmpty(classic)){
			BigDecimal sum = studentFeeRuleService.AjaxSumPoint(grade, major, classic);
			if(sum.doubleValue() == 0d){
				map.put("error", "没有对应的教学计划，请手动填写总学分.");				
			}else{
				map.put("error", "");
				map.put("sumPoint", sum.intValue());
			}
			renderJson(response, JsonUtils.mapToJson(map));
		}
	}
	
	/**
	 * 生成学生收费明细
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/schoolroll/studentfeerule/generatorFeeDetail.html")
	public void exeGeneratorFee(HttpServletRequest request,HttpServletResponse response) throws WebException{
		String resourceids = request.getParameter("resourceids");//--单个id
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(resourceids)){
				studentFeeRuleService.exeGeneratorFee(resourceids);
				map.put("statusCode", 200);
				map.put("message", "操作成功！");				
				map.put("forward", request.getContextPath()+"/edu3/schoolroll/studentfeerule/list.html");
			}
		} catch (Exception e) {
			logger.error("生成出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "生成出错:<br/>"+e.getLocalizedMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	@Autowired
	@Qualifier("studentfeeruleservice")
	private IStudentFeeRuleService studentFeeRuleService;
	@Autowired
	@Qualifier("gradeService")
	private IGradeService gradeService;
	@Autowired
	@Qualifier("orgUnitService")
	private IOrgUnitService orgUnitService;
	@Autowired
	@Qualifier("majorService")
	private IMajorService majorService;
	@Autowired
	@Qualifier("classicService")
	private IClassicService classicService;

}
