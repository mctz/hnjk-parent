package com.hnjk.edu.recruit.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.map.ListOrderedMap;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.utils.BigDecimalUtil;
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.configuration.property.ConfigPropertyUtil;
import com.hnjk.core.support.base.controller.BaseSupportController;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.YearInfo;
import com.hnjk.edu.recruit.model.RecruitPlan;
import com.hnjk.edu.recruit.service.IRecruitJDBCService;
import com.hnjk.edu.recruit.service.IRecruitPlanService;
import com.hnjk.edu.recruit.vo.StatisTrendRecruit;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.Dictionary;
import com.hnjk.platform.taglib.JstlCustomFunction;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;

@SuppressWarnings("unchecked")
@Controller
@Scope(value="prototype")
public class RecruitManageReportController extends BaseSupportController{
	
	private static final long serialVersionUID = 179886037631499390L;
	
	@Autowired
	@Qualifier("recruitJDBCService")
	private IRecruitJDBCService recruitJDBCService;
	
	@Autowired
	@Qualifier("recruitPlanService")
	private IRecruitPlanService recruitPlanService;
	
	/**
	 * 学习模式统计
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/recruit/recruitmanage/studymodel.html")
	public String listStudymodel(HttpServletRequest request, HttpServletResponse response, ModelMap model)throws WebException {
		
		String year    = request.getParameter("recruit_year");
		String school  = request.getParameter("recruit_school");
		String major   = request.getParameter("recruit_major");
		String classic = request.getParameter("recruit_classic");
		
		String hide    = "N";
		User user = SpringSecurityHelper.getCurrentUser();
		if (ConfigPropertyUtil.getInstance().getProperty("brSchool.unitType").equals(user.getOrgUnit().getUnitType())) {
			school     = user.getOrgUnit().getResourceid();
			hide       = "Y";
		}
		List list 	   = recruitJDBCService.listStudymodel(year, school, major, classic);
		StringBuffer sb= new StringBuffer();
		String title   = "";
		if(null != list && !list.isEmpty()){
			Double total = recruitJDBCService.statisticsStuTotal(year, school, major, classic, "1");
			title 	   = recruitPlanService.load(year).getRecruitPlanname()+" - 报名人员所属招生专业的学习模式";
			for(int index=0;index<list.size();index++){
				Map obj = (Map) list.get(index);
				double percentage = BigDecimalUtil.mul(BigDecimalUtil.div(Double.parseDouble(obj.get("stu_num").toString()), total, 4),100);
				if(Constants.BOOLEAN_YES.equalsIgnoreCase(obj.get("isadult").toString())){ //网络成人直属班
					sb.append("['网络成人直属班"+obj.get("stu_num").toString()+"',"+percentage+"],");
				}
				if(Constants.BOOLEAN_NO.equalsIgnoreCase(obj.get("isadult").toString())){ //网络教育
					sb.append("['网络教育"+obj.get("stu_num").toString()+"',"+percentage+"]");
				}
				obj.put("percent", BigDecimalUtil.div(Double.parseDouble(obj.get("stu_num").toString()), total, 4));
			}
		}
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("recruit_year", year);
		condition.put("recruit_school", school);
		condition.put("recruit_major", major);
		condition.put("recruit_classic", classic);
		model.addAttribute("condition", condition);
		model.addAttribute("title", title);
		model.addAttribute("stulist", list);
		model.addAttribute("recruit_hide", hide);
		model.addAttribute("chart", "{type: 'pie',name: '学习模式',data: ["+sb.toString()+"]}");
		
		return "/edu3/recruit/report/studymodel-list";
	}
	
	/**
	 * 专业统计
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/recruit/recruitmanage/majorType.html")
	public String listMajorType(HttpServletRequest request, HttpServletResponse response, ModelMap model)throws WebException {
		String year    = request.getParameter("major_year");
		String school  = request.getParameter("major_school");
		String major   = request.getParameter("major_major");
		String classic = request.getParameter("major_classic");
		
		String hide    = "N";
		User user      = SpringSecurityHelper.getCurrentUser();
		if (ConfigPropertyUtil.getInstance().getProperty("brSchool.unitType").equals(user.getOrgUnit().getUnitType())) {
			school     = user.getOrgUnit().getResourceid();
			hide       = "Y";
		}
		
		List list	   = recruitJDBCService.listMajorType(year, school, major, classic);
		StringBuffer sb = new StringBuffer();
		String title   = "";
		if(null != list && !list.isEmpty()){
			Double total = recruitJDBCService.statisticsStuTotal(year, school, major, classic, "1");
			title        = recruitPlanService.load(year).getRecruitPlanname()+" - 报名人员所属专业类别";
			for(int index=0;index<list.size();index++){
				Map obj = (Map) list.get(index);
				sb.append("['"+obj.get("majorname").toString()+obj.get("stu_num").toString()+"',"+BigDecimalUtil.mul(BigDecimalUtil.div(Double.parseDouble(obj.get("stu_num").toString()), total, 4),100)+"],");
				obj.put("percent", BigDecimalUtil.div(Double.parseDouble(obj.get("stu_num").toString()), total, 4));
			}
		}
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("major_year", year);
		condition.put("major_school", school);
		condition.put("major_major", major);
		condition.put("major_classic", classic);
		model.addAttribute("condition", condition);
		model.addAttribute("major_hide", hide);
		model.addAttribute("title", title);
		model.addAttribute("majorList", list);
		model.addAttribute("chart", "{type: 'pie',name: '专业类别',data: ["+sb.toString()+"]}");
		return "/edu3/recruit/report/majortype-list";
	}
	
	/**
	 * 层次统计
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/recruit/recruitmanage/classiclevel.html")
	public String listClassicLevel(HttpServletRequest request, HttpServletResponse response, ModelMap model)throws WebException {
		
		String year    = request.getParameter("classic_year");
		String school  = request.getParameter("classic_school");
		String major   = request.getParameter("classic_major");
		String classic = request.getParameter("classic_classic");
		
		String hide    = "N";
		User user      = SpringSecurityHelper.getCurrentUser();
		if (ConfigPropertyUtil.getInstance().getProperty("brSchool.unitType").equals(user.getOrgUnit().getUnitType())) {
			school     = user.getOrgUnit().getResourceid();
			hide       = "Y";
		}
		
		List list = recruitJDBCService.listClassicLevel(year, school, major, classic);
		StringBuffer sb = new StringBuffer();
		String title = "";
		if(null != list && !list.isEmpty()){
			Double total = recruitJDBCService.statisticsStuTotal(year, school, major, classic, "2");
			title = recruitPlanService.load(year).getRecruitPlanname()+" - 报名人员所属学习层次";
			for(int index=0;index<list.size();index++){
				Map obj = (Map) list.get(index);
				sb.append("['"+obj.get("classicname").toString()+obj.get("stu_num").toString()+"',"+BigDecimalUtil.mul(BigDecimalUtil.div(Double.parseDouble(obj.get("stu_num").toString()), total, 4),100)+"],");
				obj.put("percent", BigDecimalUtil.div(Double.parseDouble(obj.get("stu_num").toString()), total, 4));
			}
		}
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("classic_year", year);
		condition.put("classic_school", school);
		condition.put("classic_major", major);
		condition.put("classic_classic", classic);
		model.addAttribute("condition", condition);
		model.addAttribute("classic_hide", hide);
		model.addAttribute("title", title);
		model.addAttribute("classicList", list);
		model.addAttribute("chart", "{type: 'pie',name: '学习层次',data: ["+sb.toString()+"]}");
		return "/edu3/recruit/report/classiclevel-list";
	}
	
	/**
	 * 考试统计
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/recruit/recruitmanage/examStatistic.html")
	public String listExamStatistic(HttpServletRequest request, HttpServletResponse response, ModelMap model)throws WebException {
		String year    = request.getParameter("exam_year");
		String school  = request.getParameter("exam_school");
		String major   = request.getParameter("exam_major");
		String classic = request.getParameter("exam_classic");
		
		String hide    = "N";
		User user      = SpringSecurityHelper.getCurrentUser();
		if (ConfigPropertyUtil.getInstance().getProperty("brSchool.unitType").equals(user.getOrgUnit().getUnitType())) {
			school     = user.getOrgUnit().getResourceid();
			hide       = "Y";
		}
		
		List list      = recruitJDBCService.listExamStatistic(year, school, major, classic);
		StringBuffer sb = new StringBuffer();
		String title   = "";
		if(null != list && !list.isEmpty()){
			Double total = recruitJDBCService.statisticsStuTotal(year, school, major, classic, "3");
			title = recruitPlanService.load(year).getRecruitPlanname()+" - 报名人员申请免试";
			for(int index=0;index<list.size();index++){
				Map obj = (Map) list.get(index);
				if(Constants.BOOLEAN_YES.equalsIgnoreCase(obj.get("isapplynoexam").toString())){ //免试
					sb.append("['免试"+obj.get("stu_num").toString()+"',"+BigDecimalUtil.mul(BigDecimalUtil.div(Double.parseDouble(obj.get("stu_num").toString()), total, 4),100)+"],");
				}
				if(Constants.BOOLEAN_NO.equalsIgnoreCase(obj.get("isapplynoexam").toString())){ //正常
					sb.append("['正常"+obj.get("stu_num").toString()+"',"+BigDecimalUtil.mul(BigDecimalUtil.div(Double.parseDouble(obj.get("stu_num").toString()), total, 4),100)+"]");
				}
				obj.put("percent", BigDecimalUtil.div(Double.parseDouble(obj.get("stu_num").toString()), total, 4));
			}
		}
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("exam_year", year);
		condition.put("exam_school", school);
		condition.put("exam_major", major);
		condition.put("exam_classic", classic);
		model.addAttribute("condition", condition);
		model.addAttribute("exam_hide", hide);
		model.addAttribute("title", title);
		model.addAttribute("examList", list);
		model.addAttribute("chart", "{type: 'pie',name: '考试类别',data: ["+sb.toString()+"]}");
		return "/edu3/recruit/report/exam-list";
	}
	
	/**
	 * 信息地媒体来源
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/recruit/recruitmanage/fromMedia.html")
	public String listFromMedia(HttpServletRequest request, HttpServletResponse response, ModelMap model)throws WebException {
		String year    = request.getParameter("media_year");
		String school  = request.getParameter("media_school");
		String major   = request.getParameter("media_major");
		String classic = request.getParameter("media_classic");
		
		String hide    = "N";
		User user 	   = SpringSecurityHelper.getCurrentUser();
		if (ConfigPropertyUtil.getInstance().getProperty("brSchool.unitType").equals(user.getOrgUnit().getUnitType())) {
			school     = user.getOrgUnit().getResourceid();
			hide 	   = "Y";
		}
		
		List list = recruitJDBCService.listFromMedia(year, school, major, classic);
		StringBuffer sb = new StringBuffer();
		String title = "";
		if(null != list && !list.isEmpty()){
			Double total = recruitJDBCService.statisticsStuTotal(year, school, major, classic, "3");
			title = recruitPlanService.load(year).getRecruitPlanname()+" - 信息地媒体来源";
			for(int index=0;index<list.size();index++){
				Map obj = (Map) list.get(index);
				String dictName = "";
				if(null!=obj.get("frommedia")) {
					dictName = JstlCustomFunction.dictionaryCode2Value("CodeFromMedia", obj.get("frommedia").toString()); //从数据字典中拿
				}
				sb.append("['"+dictName+obj.get("stu_num").toString()+"',"+BigDecimalUtil.mul(BigDecimalUtil.div(Double.parseDouble(obj.get("stu_num").toString()), total, 4),100)+"],");
				obj.put("percent", BigDecimalUtil.div(Double.parseDouble(obj.get("stu_num").toString()), total, 4));
			}
		}
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("media_year", year);
		condition.put("media_school", school);
		condition.put("media_major", major);
		condition.put("media_classic", classic);
		model.addAttribute("condition", condition);
		model.addAttribute("media_hide", hide);
		model.addAttribute("title", title);
		model.addAttribute("mediaList", list);
		model.addAttribute("chart", "{type: 'pie',name: '信息来源',data: ["+sb.toString()+"]}");
		return "/edu3/recruit/report/media-list";
	}
	
	/**
	 * 报名方式
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/recruit/recruitmanage/applyType.html")
	public String listApplyType(HttpServletRequest request, HttpServletResponse response, ModelMap model)throws WebException {
		String year = request.getParameter("apply_year");
		String school = request.getParameter("apply_school");
		String major = request.getParameter("apply_major");
		String classic = request.getParameter("apply_classic");
		
		String hide = "N";
		User user = SpringSecurityHelper.getCurrentUser();
		if (ConfigPropertyUtil.getInstance().getProperty("brSchool.unitType").equals(user.getOrgUnit().getUnitType())) {
			school = user.getOrgUnit().getResourceid();
			hide = "Y";
		}
		
		List list = recruitJDBCService.listApplyType(year, school, major, classic);
		StringBuffer sb = new StringBuffer();
		String title = "";
		if(null != list && !list.isEmpty()){
			Double total = recruitJDBCService.statisticsStuTotal(year, school, major, classic, "3");
			title = recruitPlanService.load(year).getRecruitPlanname()+" - 报名方式";
			for(int index=0;index<list.size();index++){
				Map obj = (Map) list.get(index);
				System.out.println(obj.get("ENROLLEETYPE"));
				if("0".equalsIgnoreCase(obj.get("enrolleetype").toString())){ //现场报名
					sb.append("['现场报名"+obj.get("stu_num").toString()+"',"+BigDecimalUtil.mul(BigDecimalUtil.div(Double.parseDouble(obj.get("stu_num").toString()), total, 4),100)+"],");
				}
				if("1".equalsIgnoreCase(obj.get("enrolleetype").toString())){ //网上报名
					sb.append("['网上报名"+obj.get("stu_num").toString()+"',"+BigDecimalUtil.mul(BigDecimalUtil.div(Double.parseDouble(obj.get("stu_num").toString()), total, 4),100)+"],");
				}
				obj.put("percent", BigDecimalUtil.div(Double.parseDouble(obj.get("stu_num").toString()), total, 4));
			}
		}
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("apply_year", year);
		condition.put("apply_school", school);
		condition.put("apply_major", major);
		condition.put("apply_classic", classic);
		model.addAttribute("condition", condition);
		model.addAttribute("apply_hide", hide);
		model.addAttribute("title", title);
		model.addAttribute("applyList", list);
		model.addAttribute("chart", "{type: 'pie',name: '报名方式',data: ["+sb.toString()+"]}");
		return "/edu3/recruit/report/apply-list";
	}
	
	
	/**
	 * 入学考试成绩
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/recruit/recruitmanage/enrollSchoolStatis.html")
	public String listEnrollSchool(HttpServletRequest request, HttpServletResponse response, ModelMap model)throws WebException {
		String year = request.getParameter("es_year");
		String school = request.getParameter("es_school");
		String major = request.getParameter("es_major");
		String classic = request.getParameter("es_classic");
		
		String hide = "N";
		User user = SpringSecurityHelper.getCurrentUser();
		if (ConfigPropertyUtil.getInstance().getProperty("brSchool.unitType").equals(user.getOrgUnit().getUnitType())) {
			school = user.getOrgUnit().getResourceid();
			hide = "Y";
		}
		
		List list = recruitJDBCService.listEnrollSchool(year, school, major, classic);
		List<StatisTrendRecruit> resultList = new ArrayList<StatisTrendRecruit>();
		String title = "";
		StringBuffer sb = new StringBuffer();
		double one=0.0,one_six = 0.0,six_seven=0.0,seven_eight=0.0,eight_ninth=0.0,ninth=0.0,other=0.0;
		if(null != list && !list.isEmpty()){
			Double total = recruitJDBCService.statisticsStuTotal(year, school, major, classic, "7");
			title = recruitPlanService.load(year).getRecruitPlanname()+" - 入学成绩分布";
			for(int index=0; index<list.size(); index++){
				ListOrderedMap map = (ListOrderedMap) list.get(index);
				double courseNum =  Double.parseDouble(map.get("STU_NUM").toString());
				double point = Double.parseDouble(map.get("ORIGINALPOINT").toString());
				if(point == -1){
					one = BigDecimalUtil.add(one, courseNum);
				}else if(point>=1 && point <60){
					one_six = BigDecimalUtil.add(one_six, courseNum);
				}else if(point>=60 && point <70){
					six_seven = BigDecimalUtil.add(six_seven, courseNum);
				}else if(point>=70 && point <80){
					seven_eight = BigDecimalUtil.add(seven_eight, courseNum);
				}else if(point>=80 && point <90){
					eight_ninth = BigDecimalUtil.add(eight_ninth, courseNum);
				}else if(point>=90){
					ninth = BigDecimalUtil.add(ninth, courseNum);
				}else{
					other = BigDecimalUtil.add(other, courseNum);
				}
			} //end for
			if(one_six!=0.0){
				sb.append("['1-59.99',"+BigDecimalUtil.mul(BigDecimalUtil.div(one_six, total, 4),100)+"],");
				StatisTrendRecruit es1 = new StatisTrendRecruit("1-59.99",one_six,BigDecimalUtil.div(one_six, total, 4));
				resultList.add(es1);
			}
			if(six_seven!=0.0){
				sb.append("['60-69.99',"+BigDecimalUtil.mul(BigDecimalUtil.div(six_seven, total, 4),100)+"],");
				StatisTrendRecruit es2 = new StatisTrendRecruit("60-69.99",six_seven,BigDecimalUtil.div(six_seven, total, 4));
				resultList.add(es2);
			}
			if(seven_eight!=0.0){
				sb.append("['70-79.99',"+BigDecimalUtil.mul(BigDecimalUtil.div(seven_eight, total, 4),100)+"],");
				StatisTrendRecruit es3 = new StatisTrendRecruit("70-79.99",seven_eight,BigDecimalUtil.div(seven_eight, total, 4));
				resultList.add(es3);
			}
			if(eight_ninth!=0.0){
				sb.append("['80-89.99',"+BigDecimalUtil.mul(BigDecimalUtil.div(eight_ninth, total, 4),100)+"],");
				StatisTrendRecruit es4 = new StatisTrendRecruit("80-89.99",eight_ninth,BigDecimalUtil.div(eight_ninth, total, 4));
				resultList.add(es4);
			}
			if(ninth!=0.0){
				sb.append("['90',"+BigDecimalUtil.mul(BigDecimalUtil.div(ninth, total, 4),100)+"],");
				StatisTrendRecruit es5 = new StatisTrendRecruit("90",ninth,BigDecimalUtil.div(ninth, total, 4));
				resultList.add(es5);
			}
			if(one!=0.0){
				sb.append("['缺考',"+BigDecimalUtil.mul(BigDecimalUtil.div(one, total, 4),100)+"],");
				StatisTrendRecruit es6 = new StatisTrendRecruit("缺考",one,BigDecimalUtil.div(one, total, 4));
				resultList.add(es6);
			}
			if(other!=0.0){
				sb.append("['其它',"+BigDecimalUtil.mul(BigDecimalUtil.div(other, total, 4),100)+"],");
				StatisTrendRecruit es7 = new StatisTrendRecruit("其它",other,BigDecimalUtil.div(other, total, 4));
				resultList.add(es7);
			}
			
		}
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("es_year", year);
		condition.put("es_school", school);
		condition.put("es_major", major);
		condition.put("es_classic", classic);
		model.addAttribute("condition", condition);
		model.addAttribute("es_hide", hide);
		model.addAttribute("title", title);
		model.addAttribute("esList", resultList);
		model.addAttribute("chart", "{type: 'pie',name: '报名方式',data: ["+sb.toString()+"]}");
		return "/edu3/recruit/report/enrollschool-list";
	}
	
	//------------------------------------录取-------------------
	
	/**
	 * 录取情况
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/recruit/recruitmanage/enrollStatus.html")
	public String listEnrollStatus(HttpServletRequest request, HttpServletResponse response, ModelMap model)throws WebException {
		String year = request.getParameter("enroll_year");
		String school = request.getParameter("enroll_school");
		String major = request.getParameter("enroll_major");
		String classic = request.getParameter("enroll_classic");
		
		String hide = "N";
		User user = SpringSecurityHelper.getCurrentUser();
		if (ConfigPropertyUtil.getInstance().getProperty("brSchool.unitType").equals(user.getOrgUnit().getUnitType())) {
			school = user.getOrgUnit().getResourceid();
			hide = "Y";
		}
		
		List list = recruitJDBCService.listEnrollStatus(year, school, major, classic);
		StringBuffer sb = new StringBuffer();
		String title = "";
		if(null != list && !list.isEmpty()){
			Double total = recruitJDBCService.statisticsStuTotal(year, school, major, classic, "6");
			RecruitPlan plan = recruitPlanService.get(year);
			if(null != plan){
				title = plan.getRecruitPlanname()+" - 录取情况";
			}else{
				YearInfo y = (YearInfo)recruitPlanService.get(YearInfo.class, year);
				if(null != y) {
					title = y.getYearName()+" - 录取情况";
				}
			}
			
			if(total>0){
					for(int index=0;index<list.size();index++){
						Map obj = (Map) list.get(index);
						if(Constants.BOOLEAN_YES.equalsIgnoreCase(obj.get("Ismatriculate").toString())){ //录取
							sb.append("['录取"+obj.get("stu_num").toString()+"',"+BigDecimalUtil.mul(BigDecimalUtil.div(Double.parseDouble(obj.get("stu_num").toString()), total, 4),100)+"],");
						}
						if(Constants.BOOLEAN_NO.equalsIgnoreCase(obj.get("Ismatriculate").toString())){ //未录取
							sb.append("['未录取"+obj.get("stu_num").toString()+"',"+BigDecimalUtil.mul(BigDecimalUtil.div(Double.parseDouble(obj.get("stu_num").toString()), total, 4),100)+"],");
						}
						obj.put("percent", BigDecimalUtil.div(Double.parseDouble(obj.get("stu_num").toString()), total, 4));
					}
			}
		}
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("enroll_year", year);
		condition.put("enroll_school", school);
		condition.put("enroll_major", major);
		condition.put("enroll_classic", classic);
		model.addAttribute("condition", condition);
		model.addAttribute("enroll_hide", hide);
		model.addAttribute("title", title);
		model.addAttribute("enrollList", list);
		model.addAttribute("chart", "{type: 'pie',name: '录取情况',data: ["+sb.toString()+"]}");
		return "/edu3/recruit/report/enroll-list";
	}
	
	
	/**
	 * 录取学生学习模式
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/recruit/recruitmanage/enrollStudy.html")
	public String listEnrollStudy(HttpServletRequest request, HttpServletResponse response, ModelMap model)throws WebException {
		String year    = request.getParameter("enrollStudy_year");
		String school  = request.getParameter("enrollStudy_school");
		String major   = request.getParameter("enrollStudy_major");
		String classic = request.getParameter("enrollStudy_classic");
		
		String hide    = "N";
		User user      = SpringSecurityHelper.getCurrentUser();
		if (ConfigPropertyUtil.getInstance().getProperty("brSchool.unitType").equals(user.getOrgUnit().getUnitType())) {
			school     = user.getOrgUnit().getResourceid();
			hide       = "Y";
		}
		
		List list = recruitJDBCService.listEnrollStudy(year, school, major, classic);
		StringBuffer sb = new StringBuffer();
		String title = "";
		if(null != list && !list.isEmpty()){
			Double total = recruitJDBCService.statisticsStuTotal(year, school, major, classic, "4");
			RecruitPlan plan = recruitPlanService.get(year);
			if(null != plan){
				title = plan.getRecruitPlanname()+" - 学习模式";
			}else{
				YearInfo y = (YearInfo)recruitPlanService.get(YearInfo.class, year);
				if(null != y) {
					title = y.getYearName()+ " - 学习模式";
				}
			}
			
			for(int index=0;index<list.size();index++){
				Map obj = (Map) list.get(index);
				double percentage = BigDecimalUtil.mul(BigDecimalUtil.div(Double.parseDouble(obj.get("stu_num").toString()), total, 4),100);
				if(Constants.BOOLEAN_YES.equalsIgnoreCase(obj.get("isadult").toString())){ //网络成人直属班
					sb.append("['网络成人直属班"+obj.get("stu_num").toString()+"',"+percentage+"],");
				}
				if(Constants.BOOLEAN_NO.equalsIgnoreCase(obj.get("isadult").toString())){ //网络教育
					sb.append("['网络教育"+obj.get("stu_num").toString()+"',"+percentage+"]");
				}
				obj.put("percent", BigDecimalUtil.div(Double.parseDouble(obj.get("stu_num").toString()), total, 4));
			}
		}
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("enrollStudy_year", year);
		condition.put("enrollStudy_school", school);
		condition.put("enrollStudy_major", major);
		condition.put("enrollStudy_classic", classic);
		model.addAttribute("condition", condition);
		model.addAttribute("enrollStudy_hide", hide);
		model.addAttribute("title", title);
		model.addAttribute("enrollStudyList", list);
		model.addAttribute("chart", "{type: 'pie',name: '学习模式',data: ["+sb.toString()+"]}");
		return "/edu3/recruit/report/enrollstudy-list";
	}
	
	
	/**
	 * 录取学生专业统计
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/recruit/recruitmanage/enrollMajor.html")
	public String listEnrollMajor(HttpServletRequest request, HttpServletResponse response, ModelMap model)throws WebException {
		String year = request.getParameter("enrollmajor_year");
		String school = request.getParameter("enrollmajor_school");
		String major = request.getParameter("enrollmajor_major");
		String classic = request.getParameter("enrollmajor_classic");
		
		String hide = "N";
		User user = SpringSecurityHelper.getCurrentUser();
		if (ConfigPropertyUtil.getInstance().getProperty("brSchool.unitType").equals(user.getOrgUnit().getUnitType())) {
			school = user.getOrgUnit().getResourceid();
			hide = "Y";
		}
		
		List list = recruitJDBCService.listEnrollMajor(year, school, major, classic);
		StringBuffer sb = new StringBuffer();
		String title = "";
		if(null != list && !list.isEmpty()){
			Double total = recruitJDBCService.statisticsStuTotal(year, school, major, classic, "4");
			RecruitPlan plan = recruitPlanService.get(year);
			if(null != plan){
				title = plan.getRecruitPlanname()+" - 专业类别";
			}else{
				YearInfo y = (YearInfo)recruitPlanService.get(YearInfo.class, year);
				if(null != y) {
					title = y.getYearName()+ " - 专业类别";
				}
			}
		
			for(int index=0;index<list.size();index++){
				Map obj = (Map) list.get(index);
				sb.append("['"+obj.get("majorname").toString()+obj.get("stu_num").toString()+"',"+BigDecimalUtil.mul(BigDecimalUtil.div(Double.parseDouble(obj.get("stu_num").toString()), total, 4),100)+"],");
				obj.put("percent", BigDecimalUtil.div(Double.parseDouble(obj.get("stu_num").toString()), total, 4));
			}
		}
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("enrollmajor_year", year);
		condition.put("enrollmajor_school", school);
		condition.put("enrollmajor_major", major);
		condition.put("enrollmajor_classic", classic);
		model.addAttribute("condition", condition);
		model.addAttribute("enrollmajor_hide", hide);
		model.addAttribute("title", title);
		model.addAttribute("enrollmajorList", list);
		model.addAttribute("chart", "{type: 'pie',name: '专业类别',data: ["+sb.toString()+"]}");
		return "/edu3/recruit/report/enrollmajor-list";
	}
	
	
	/**
	 * 录取学生层次统计
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/recruit/recruitmanage/enrollClassic.html")
	public String listEnrollClassic(HttpServletRequest request, HttpServletResponse response, ModelMap model)throws WebException {
		String year = request.getParameter("enrollclassic_year");
		String school = request.getParameter("enrollclassic_school");
		String major = request.getParameter("enrollclassic_major");
		String classic = request.getParameter("enrollclassic_classic");
		
		String hide = "N";
		User user = SpringSecurityHelper.getCurrentUser();
		if (ConfigPropertyUtil.getInstance().getProperty("brSchool.unitType").equals(user.getOrgUnit().getUnitType())) {
			school = user.getOrgUnit().getResourceid();
			hide = "Y";
		}
		
		List list = recruitJDBCService.listEnrollClassic(year, school, major, classic);
		StringBuffer sb = new StringBuffer();
		String title = "";
		if(null != list && !list.isEmpty()){
			Double total = recruitJDBCService.statisticsStuTotal(year, school, major, classic, "5");
			RecruitPlan plan = recruitPlanService.get(year);
			if(null != plan){
				title = plan.getRecruitPlanname()+" - 学习层次";
			}else{
				YearInfo y = (YearInfo)recruitPlanService.get(YearInfo.class, year);
				if(null != y) {
					title = y.getYearName()+ " - 学习层次";
				}
			}
		
			for(int index=0;index<list.size();index++){
				Map obj = (Map) list.get(index);
				sb.append("['"+obj.get("classicname").toString()+obj.get("stu_num").toString()+"',"+BigDecimalUtil.mul(BigDecimalUtil.div(Double.parseDouble(obj.get("stu_num").toString()), total, 4),100)+"],");
				obj.put("percent", BigDecimalUtil.div(Double.parseDouble(obj.get("stu_num").toString()), total, 4));
			}
		}
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("enrollclassic_year", year);
		condition.put("enrollclassic_school", school);
		condition.put("enrollclassic_major", major);
		condition.put("enrollclassic_classic", classic);
		model.addAttribute("condition", condition);
		model.addAttribute("enrollclassic_hide", hide);
		model.addAttribute("title", title);
		model.addAttribute("enrollclassicList", list);
		model.addAttribute("chart", "{type: 'pie',name: '学习层次',data: ["+sb.toString()+"]}");
		return "/edu3/recruit/report/enrollclassic-list";
	}
	
	
	/**
	 * 报名，考试，录取，注册人数统计
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/recruit/recruitmanage/enrollStatistic.html")
	public String listEnrollStatistic(HttpServletRequest request, HttpServletResponse response, ModelMap model)throws WebException {
		String year = ExStringUtils.trimToEmpty(request.getParameter("enrollstatis_year"));
		String school = ExStringUtils.trimToEmpty(request.getParameter("enrollstatis_school"));
		String major = ExStringUtils.trimToEmpty(request.getParameter("enrollstatis_major"));
		String classic = ExStringUtils.trimToEmpty(request.getParameter("enrollstatis_classic"));
		
		String hide = "N";
		User user = SpringSecurityHelper.getCurrentUser();
		if (ConfigPropertyUtil.getInstance().getProperty("brSchool.unitType").equals(user.getOrgUnit().getUnitType())) {
			school = user.getOrgUnit().getResourceid();
			hide = "Y";
		}
		
		String title = "" , data = "";
		Map<String, Object> condition = new HashMap<String, Object>();
		if(ExStringUtils.isNotEmpty(year)){
			StringBuffer criteria = new StringBuffer();
			if(ExStringUtils.isNotEmpty(school)) {
				criteria.append(" and e.branchschoolid='"+school+"' ");
			}
			if(ExStringUtils.isNotEmpty(major)) {
				criteria.append(" and rm.majorid='"+major+"' ");
			}
			if(ExStringUtils.isNotEmpty(classic)) {
				criteria.append(" and rm.classic='"+classic+"' ");
			}
			//FIXED by hzg 招生批次或所在年度
			Double total = recruitJDBCService.statisticsStuTotal(year, school, major, classic, "3");
			String sql1 = "select count(e.ENROLLEETYPE) stu_num from EDU_RECRUIT_ENROLLEEINFO e,EDU_RECRUIT_MAJOR rm,EDU_RECRUIT_RECRUITPLAN  p,EDU_BASE_GRADE g " +
					"where e.RECRUITMAJORID = rm.resourceid and rm.Recruitplanid = p.resourceid and e.isDeleted = 0 "+criteria.toString() +
					" and e.grade = g.resourceid "+
					"and e.enrolleetype=0 and (p.resourceid = :pid or g.yearid = :pid)"; //现场报名人数
			
			String sql2 = "select count(distinct(t.enrolleeinfoid)) stu_num from edu_recruit_examscore t,EDU_RECRUIT_ENROLLEEINFO e " +
					"where t.enrolleeinfoid=e.resourceid and t.originalpoint>=0 and exists " +
					"(select e1.resourceid from EDU_RECRUIT_ENROLLEEINFO e1,EDU_RECRUIT_MAJOR rm,EDU_RECRUIT_RECRUITPLAN  p,EDU_BASE_GRADE g " +
					"where e1.RECRUITMAJORID = rm.resourceid and rm.Recruitplanid = p.resourceid and e1.isDeleted = 0 "+criteria.toString()+
					" and e1.grade = g.resourceid and e.resourceid = e1.resourceid "+
					" and (p.resourceid = :pid or g.yearid=:pid) )"; //参加入学考试
			String sql3 = "select count(e.noExamFlag) stu_num from EDU_RECRUIT_ENROLLEEINFO e,EDU_RECRUIT_MAJOR rm,EDU_RECRUIT_RECRUITPLAN  p ,EDU_BASE_GRADE g" +
					" where e.RECRUITMAJORID = rm.resourceid and rm.Recruitplanid = p.resourceid and e.isDeleted = 0 "+criteria.toString()+
					" and e.grade = g.resourceid "+
					" and e.noExamFlag = 'Y' and (p.resourceid = :pid or g.yearid=:pid)"; // 免试入学
			String sql4 = "select count(e.ismatriculate) stu_num from EDU_RECRUIT_ENROLLEEINFO e,EDU_RECRUIT_MAJOR rm,EDU_RECRUIT_RECRUITPLAN  p,EDU_BASE_GRADE g " +
					" where e.RECRUITMAJORID = rm.resourceid and rm.Recruitplanid = p.resourceid and e.isDeleted = 0 "+criteria.toString()+
					" and e.grade = g.resourceid "+
					" and e.ismatriculate = 'Y' and (p.resourceid = :pid or g.yearid=:pid)"; // 录取
			String sql5 = "select count(e.register_flag) stu_num from EDU_RECRUIT_ENROLLEEINFO e,EDU_RECRUIT_MAJOR rm,EDU_RECRUIT_RECRUITPLAN  p,EDU_BASE_GRADE g " +
					" where e.RECRUITMAJORID = rm.resourceid and rm.Recruitplanid = p.resourceid and e.isDeleted = 0 "+criteria.toString()+
					" and e.grade = g.resourceid "+
					" and e.register_flag='Y' and (p.resourceid = :pid or g.yearid=:pid)"; // 注册
			RecruitPlan plan = recruitPlanService.get(year);
			if(null == plan){
				YearInfo y = (YearInfo)recruitPlanService.get(YearInfo.class, year);
				if(null != y) {
					title = y.getYearName()+" - 招生人数统计";
				}
			}else{
				title = plan.getRecruitPlanname()+" - 招生人数统计";
			}
			
			
			Double countNum1 = recruitJDBCService.statisStudentBySql(year, sql1); //现场报名人数
			Double countNum2 = recruitJDBCService.statisStudentBySql(year, sql2); //考试人数
			Double countNum3 = recruitJDBCService.statisStudentBySql(year, sql3); //免考人数
			Double countNum4 = recruitJDBCService.statisStudentBySql(year, sql4); //录取人数
			Double countNum5 = recruitJDBCService.statisStudentBySql(year, sql5); //注册人数
			
			data = "{name: '报名人数',data: ["+total+"]}, {name: '来现场确认人数',data: ["+countNum1+"]}, {name: '参加入学考试人数',data: ["+countNum2+"]}, {name: '免考人数',data: ["+countNum3+"]}, {name: '被录取人数',data: ["+countNum4+"]}, {name: '已注册人数',data: ["+countNum5+"]}";
			condition.put("total", total);
			condition.put("countNum1", countNum1);
			condition.put("countNum2", countNum2);
			condition.put("countNum3", countNum3);
			condition.put("countNum4", countNum4);
			condition.put("countNum5", countNum5);
		}
			
		condition.put("enrollstatis_year", year);
		condition.put("enrollstatis_school", school);
		condition.put("enrollstatis_major", major);
		condition.put("enrollstatis_classic", classic);
		model.addAttribute("condition", condition);
		model.addAttribute("enrollstatis_hide", hide);
		model.addAttribute("title", title);
		model.addAttribute("chart", data);
		return "/edu3/recruit/report/enrollstatis-list";
	}
	
	
	/**
	 * 报名，考试，录取，注册人数统计趋势
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/recruit/recruitmanage/enrollStatisticTrend.html")
	public String listEnrollStatisticTrend(HttpServletRequest request, HttpServletResponse response, ModelMap model)throws WebException {
		String s_year = request.getParameter("start_enrollstatis_year");
		String e_year = request.getParameter("end_enrollstatis_year");
		String school = request.getParameter("enrollstatis2_school");
		String major = request.getParameter("enrollstatis2_major");
		String classic = request.getParameter("enrollstatis2_classic");
		String title = "报名人数趋势统计" ;
		StringBuffer xTitle = new StringBuffer();
		StringBuffer data1 = new StringBuffer("{name: '报名人数',data: [");
		StringBuffer data2 = new StringBuffer("{name: '来现场确认人数',data: [");
		StringBuffer data3 = new StringBuffer("{name: '参加入学考试人数',data: [");
		StringBuffer data4 = new StringBuffer("{name: '免考人数',data: [");
		StringBuffer data5 = new StringBuffer("{name: '被录取人数',data: [");
//		StringBuffer data6 = new StringBuffer("{name: '已注册人数',data: [");
		
		String hide = "N";
		User user = SpringSecurityHelper.getCurrentUser();
		if (ConfigPropertyUtil.getInstance().getProperty("brSchool.unitType").equals(user.getOrgUnit().getUnitType())) {
			school = user.getOrgUnit().getResourceid();
			hide = "Y";
		}
				
		List<StatisTrendRecruit> listContent = new ArrayList<StatisTrendRecruit>();
		Map<String, Object> condition = new HashMap<String, Object>();
		if(ExStringUtils.isNotEmpty(s_year) && ExStringUtils.isNotEmpty(e_year)){
			Criterion[] criterions = new Criterion[2];
			Order[] orders = new Order[1];
			criterions[0] = Restrictions.between("startDate", ExDateUtils.convertToDate(s_year), ExDateUtils.convertToDate(e_year));
			criterions[1] = Restrictions.eq("isDeleted", 0);
			orders[0] = Order.asc("startDate");
			List<RecruitPlan> rpList = recruitPlanService.findByCriteria(RecruitPlan.class, criterions, orders);
			
			StringBuffer criteria = new StringBuffer();
			if(ExStringUtils.isNotEmpty(school)) {
				criteria.append(" and e.branchschoolid='"+school+"' ");
			}
			if(ExStringUtils.isNotEmpty(major)) {
				criteria.append(" and rm.majorid='"+major+"' ");
			}
			if(ExStringUtils.isNotEmpty(classic)) {
				criteria.append(" and rm.classic='"+classic+"' ");
			}
			
			int index = 0 ;
			for(RecruitPlan rp : rpList){
				Double total = recruitJDBCService.statisticsStuTotal(rp.getResourceid(), school, major, classic, "3");
				String sql1 = "select count(e.ENROLLEETYPE) stu_num from EDU_RECRUIT_ENROLLEEINFO e,EDU_RECRUIT_MAJOR rm,EDU_RECRUIT_RECRUITPLAN  p where e.RECRUITMAJORID = rm.resourceid and rm.Recruitplanid = p.resourceid and e.isDeleted = 0 "+criteria.toString()+" and e.enrolleetype=0 and p.resourceid = :pid"; //现场报名人数
				String sql2 = "select count(distinct(t.enrolleeinfoid)) stu_num from edu_recruit_examscore t,EDU_RECRUIT_ENROLLEEINFO e where t.enrolleeinfoid=e.resourceid and t.originalpoint>=0 and exists (select e1.resourceid from EDU_RECRUIT_ENROLLEEINFO e1,EDU_RECRUIT_MAJOR rm,EDU_RECRUIT_RECRUITPLAN  p where e1.RECRUITMAJORID = rm.resourceid and rm.Recruitplanid = p.resourceid and e1.isDeleted = 0 "+criteria.toString()+" and p.resourceid = :pid and e.resourceid = e1.resourceid)"; //参加入学考试
				String sql3 = "select count(e.noExamFlag) stu_num from EDU_RECRUIT_ENROLLEEINFO e,EDU_RECRUIT_MAJOR rm,EDU_RECRUIT_RECRUITPLAN  p where e.RECRUITMAJORID = rm.resourceid and rm.Recruitplanid = p.resourceid and e.isDeleted = 0 "+criteria.toString()+" and e.noExamFlag = 'Y' and p.resourceid = :pid"; // 免试入学
				String sql4 = "select count(e.ismatriculate) stu_num from EDU_RECRUIT_ENROLLEEINFO e,EDU_RECRUIT_MAJOR rm,EDU_RECRUIT_RECRUITPLAN  p where e.RECRUITMAJORID = rm.resourceid and rm.Recruitplanid = p.resourceid and e.isDeleted = 0 "+criteria.toString()+" and e.ismatriculate = 'Y' and p.resourceid = :pid"; // 录取
//				String sql5 = "select count(e.register_flag) stu_num from EDU_RECRUIT_ENROLLEEINFO e,EDU_RECRUIT_MAJOR rm,EDU_RECRUIT_RECRUITPLAN  p where e.RECRUITMAJORID = rm.resourceid and rm.Recruitplanid = p.resourceid and e.isDeleted = 0 "+criteria.toString()+" and e.register_flag='Y' and p.resourceid = :pid"; // 注册
				
				Double countNum1 = recruitJDBCService.statisStudentBySql(rp.getResourceid(), sql1); //现场报名人数
				Double countNum2 = recruitJDBCService.statisStudentBySql(rp.getResourceid(), sql2); //考试人数
				Double countNum3 = recruitJDBCService.statisStudentBySql(rp.getResourceid(), sql3); //免考人数
				Double countNum4 = recruitJDBCService.statisStudentBySql(rp.getResourceid(), sql4); //录取人数
//				Double countNum5 = recruitJDBCService.statisStudentBySql(rp.getResourceid(), sql5); //注册人数
				Double countNum5 = 0.0;
				data1.append(total+",");
				data2.append(countNum1+",");
				data3.append(countNum2+",");
				data4.append(countNum3+",");
				data5.append(countNum4+",");
//				data6.append(countNum5+",");
				
				listContent.add(new StatisTrendRecruit(rp.getRecruitPlanname(),total,countNum1,countNum2,countNum3,countNum4,countNum5));
								
				xTitle.append(",'"+rp.getRecruitPlanname()+"'");
				index++;
			}
			data1.append("]},");
			data2.append("]},");
			data3.append("]},");
			data4.append("]},");
			data5.append("]},");
//			data6.append("]}");
		}
		
		condition.put("start_enrollstatis_year", s_year);
		condition.put("end_enrollstatis_year", e_year);
		condition.put("enrollstatis2_school", school);
		condition.put("enrollstatis2_major", major);
		condition.put("enrollstatis2_classic", classic);
		model.addAttribute("condition", condition);
		model.addAttribute("enrollstatis2_hide", hide);
		model.addAttribute("trendlist", listContent);
		model.addAttribute("title", title);
		model.addAttribute("xTitle", ExStringUtils.isNotEmpty(xTitle.toString()) ? xTitle.toString().substring(1) : "");
		model.addAttribute("chart", data1.append(data2.toString()).append(data3.toString()).append(data4.toString()).append(data5.toString()).toString());
		return "/edu3/recruit/report/enrollstatistrend-list";
	}
	
	
	/**
	 * 报名人员年龄统计
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/recruit/recruitmanage/enrollStatisticAge.html")
	public String listEnrollStatisticAge(HttpServletRequest request, HttpServletResponse response, ModelMap model)throws WebException {
		String year    = request.getParameter("age_year");
		String school  = request.getParameter("age_school");
		String major   = request.getParameter("age_major");
		String classic = request.getParameter("age_classic");
		
		String hide    = "N";
		User user      = SpringSecurityHelper.getCurrentUser();
		if (ConfigPropertyUtil.getInstance().getProperty("brSchool.unitType").equals(user.getOrgUnit().getUnitType())) {
			school     = user.getOrgUnit().getResourceid();
			hide       = "Y";
		}
		
		String title   = "" , data = "";
		Map<String, Object> condition = new HashMap<String, Object>();
		if(ExStringUtils.isNotEmpty(year)){
			StringBuffer criteria = new StringBuffer();
			if(ExStringUtils.isNotEmpty(school)) {
				criteria.append(" and e.branchschoolid='"+school+"' ");
			}
			if(ExStringUtils.isNotEmpty(major)) {
				criteria.append(" and rm.majorid='"+major+"' ");
			}
			if(ExStringUtils.isNotEmpty(classic)) {
				criteria.append(" and rm.classic='"+classic+"' ");
			}
			
			Double total = recruitJDBCService.statisticsStuTotal(year, school, major, classic, "8");
			String sql1 = "select count(*) stu_num from EDU_RECRUIT_ENROLLEEINFO e,EDU_BASE_STUDENT bs,EDU_RECRUIT_MAJOR rm,EDU_RECRUIT_RECRUITPLAN p where e.RECRUITMAJORID = rm.resourceid and e.studentbaseinfoid = bs.resourceid and rm.Recruitplanid = p.resourceid and e.isDeleted = 0 and round(months_between(sysdate,bs.bornday)/12) <=20 "+criteria.toString()+" and p.resourceid = :pid"; //20
			String sql2 = "select count(*) stu_num from EDU_RECRUIT_ENROLLEEINFO e,EDU_BASE_STUDENT bs,EDU_RECRUIT_MAJOR rm,EDU_RECRUIT_RECRUITPLAN p where e.RECRUITMAJORID = rm.resourceid and e.studentbaseinfoid = bs.resourceid and rm.Recruitplanid = p.resourceid and e.isDeleted = 0 and round(months_between(sysdate,bs.bornday)/12) >20 and round(months_between(sysdate,bs.bornday)/12) <=30"+criteria.toString()+" and p.resourceid = :pid"; //20-30
			String sql3 = "select count(*) stu_num from EDU_RECRUIT_ENROLLEEINFO e,EDU_BASE_STUDENT bs,EDU_RECRUIT_MAJOR rm,EDU_RECRUIT_RECRUITPLAN p where e.RECRUITMAJORID = rm.resourceid and e.studentbaseinfoid = bs.resourceid and rm.Recruitplanid = p.resourceid and e.isDeleted = 0 and round(months_between(sysdate,bs.bornday)/12) >30 and round(months_between(sysdate,bs.bornday)/12) <=40"+criteria.toString()+" and p.resourceid = :pid"; //30-40
			String sql4 = "select count(*) stu_num from EDU_RECRUIT_ENROLLEEINFO e,EDU_BASE_STUDENT bs,EDU_RECRUIT_MAJOR rm,EDU_RECRUIT_RECRUITPLAN p where e.RECRUITMAJORID = rm.resourceid and e.studentbaseinfoid = bs.resourceid and rm.Recruitplanid = p.resourceid and e.isDeleted = 0 and round(months_between(sysdate,bs.bornday)/12) >40 and round(months_between(sysdate,bs.bornday)/12) <=50"+criteria.toString()+" and p.resourceid = :pid"; //40-50
			String sql5 = "select count(*) stu_num from EDU_RECRUIT_ENROLLEEINFO e,EDU_BASE_STUDENT bs,EDU_RECRUIT_MAJOR rm,EDU_RECRUIT_RECRUITPLAN p where e.RECRUITMAJORID = rm.resourceid and e.studentbaseinfoid = bs.resourceid and rm.Recruitplanid = p.resourceid and e.isDeleted = 0 and round(months_between(sysdate,bs.bornday)/12) >50 "+criteria.toString()+" and p.resourceid = :pid"; //50
			title = recruitPlanService.load(year).getRecruitPlanname()+" - 招生年龄统计";
			
			Double countNum1 = recruitJDBCService.statisStudentBySql(year, sql1); //20
			Double countNum2 = recruitJDBCService.statisStudentBySql(year, sql2); //20-30
			Double countNum3 = recruitJDBCService.statisStudentBySql(year, sql3); //30-40
			Double countNum4 = recruitJDBCService.statisStudentBySql(year, sql4); //40-50
			Double countNum5 = recruitJDBCService.statisStudentBySql(year, sql5); //50
			
			data = "{name: '报名总人数',data: ["+total+"]}, {name: '20岁以下(含)',data: ["+countNum1+"]}, {name: '20岁~30岁(含)',data: ["+countNum2+"]}, {name: '30岁~40岁(含)',data: ["+countNum3+"]}, {name: '40岁~50岁(含)',data: ["+countNum4+"]}, {name: '50岁以上(含)',data: ["+countNum5+"]}";
			condition.put("total", total);
			condition.put("countNum1", countNum1);
			condition.put("countNum2", countNum2);
			condition.put("countNum3", countNum3);
			condition.put("countNum4", countNum4);
			condition.put("countNum5", countNum5);
		}
			
		condition.put("age_year", year);
		condition.put("age_school", school);
		condition.put("age_major", major);
		condition.put("age_classic", classic);
		model.addAttribute("condition", condition);
		model.addAttribute("age_hide", hide);
		model.addAttribute("title", title);
		model.addAttribute("chart", data);
		return "/edu3/recruit/report/enrollstatisage-list";
	}
	
	
	/**
	 * 招生性别统计
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/recruit/recruitmanage/enrollStatisticSex.html")
	public String listEnrollStatisticSex(HttpServletRequest request, HttpServletResponse response, ModelMap model)throws WebException {
		String year    = request.getParameter("sex_year");
		String school  = request.getParameter("sex_school");
		String major   = request.getParameter("sex_major");
		String classic = request.getParameter("sex_classic");
		
		String hide    = "N";
		User user      = SpringSecurityHelper.getCurrentUser();
		if (ConfigPropertyUtil.getInstance().getProperty("brSchool.unitType").equals(user.getOrgUnit().getUnitType())) {
			school     = user.getOrgUnit().getResourceid();
			hide       = "Y";
		}
		
		List list      = recruitJDBCService.listEnrollSex(year, school, major, classic);
		StringBuffer sb = new StringBuffer();
		String title   = "";
		Map<String, String> dictMap = new HashMap<String, String>();
		if(null != list && !list.isEmpty()){
			List<Dictionary> children  = CacheAppManager.getChildren("CodeSex");//从缓存中获取字典对象
			if(children != null){
				for(Dictionary childDict : children){
					dictMap.put(childDict.getDictValue(), childDict.getDictName());
				}
			}

			Double total = recruitJDBCService.statisticsStuTotal(year, school, major, classic, "8");
			title        = recruitPlanService.load(year).getRecruitPlanname()+" - 性别类型";
			for(int index=0;index<list.size();index++){
				Map obj = (Map) list.get(index);
				sb.append("['"+dictMap.get(obj.get("gender").toString()) + obj.get("stu_num").toString()+"',"+BigDecimalUtil.mul(BigDecimalUtil.div(Double.parseDouble(obj.get("stu_num").toString()), total, 4),100)+"],");
				obj.put("percent", BigDecimalUtil.div(Double.parseDouble(obj.get("stu_num").toString()), total, 4));
			}
		}
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("sex_year", year);
		condition.put("sex_school", school);
		condition.put("sex_major", major);
		condition.put("sex_classic", classic);
		model.addAttribute("condition", condition);
		model.addAttribute("sex_hide", hide);
		model.addAttribute("title", title);
		model.addAttribute("enrollSexList", list);
		model.addAttribute("sexMap", dictMap);
		model.addAttribute("chart", "{type: 'pie',name: '性别类型',data: ["+sb.toString()+"]}");
		return "/edu3/recruit/report/enrollstatissex-list";
	}
	
	
	/**
	 * 招生籍贯统计
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/recruit/recruitmanage/enrollStatisticPlace.html")
	public String listEnrollStatisticPlace(HttpServletRequest request, HttpServletResponse response, ModelMap model)throws WebException {
		String year    = request.getParameter("p_year");
		String school  = request.getParameter("p_school");
		String major   = request.getParameter("p_major");
		String classic = request.getParameter("p_classic");
		
		String hide    = "N";
		User user      = SpringSecurityHelper.getCurrentUser();
		if (ConfigPropertyUtil.getInstance().getProperty("brSchool.unitType").equals(user.getOrgUnit().getUnitType())) {
			school     = user.getOrgUnit().getResourceid();
			hide       = "Y";
		}
		
		List list      = recruitJDBCService.listEnrollPlace(year, school, major, classic);
		StringBuffer sb= new StringBuffer();
		String title   = "";
		if(null != list && !list.isEmpty()){
			Double total = recruitJDBCService.statisticsStuTotal(year, school, major, classic, "8");
			title        = recruitPlanService.load(year).getRecruitPlanname()+" - 籍贯";
			for(int index=0;index<list.size();index++){
				Map obj = (Map) list.get(index);
				sb.append("['"+obj.get("homeplace").toString()+obj.get("stu_num").toString()+"',"+BigDecimalUtil.mul(BigDecimalUtil.div(Double.parseDouble(obj.get("stu_num").toString()), total, 4),100)+"],");
				obj.put("percent", BigDecimalUtil.div(Double.parseDouble(obj.get("stu_num").toString()), total, 4));
			}
		}
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("p_year", year);
		condition.put("p_school", school);
		condition.put("p_major", major);
		condition.put("p_classic", classic);
		model.addAttribute("condition", condition);
		model.addAttribute("p_hide", hide);
		model.addAttribute("title", title);
		model.addAttribute("enrollPList", list);
		model.addAttribute("chart", "{type: 'pie',name: '籍贯',data: ["+sb.toString()+"]}");
		return "/edu3/recruit/report/enrollstatisP-list";
	}
	
	
	/**
	 * 招生户口统计
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/recruit/recruitmanage/enrollStatisticResidence.html")
	public String listEnrollStatisticResidence(HttpServletRequest request, HttpServletResponse response, ModelMap model)throws WebException {
		String year    = request.getParameter("r_year");
		String school  = request.getParameter("r_school");
		String major   = request.getParameter("r_major");
		String classic = request.getParameter("r_classic");
		
		String hide    = "N";
		User user      = SpringSecurityHelper.getCurrentUser();
		if (ConfigPropertyUtil.getInstance().getProperty("brSchool.unitType").equals(user.getOrgUnit().getUnitType())) {
			school     = user.getOrgUnit().getResourceid();
			hide       = "Y";
		}
		
		List list = recruitJDBCService.listEnrollResidence(year, school, major, classic);
		StringBuffer sb = new StringBuffer();
		String title = "";
		if(null != list && !list.isEmpty()){
			Double total = recruitJDBCService.statisticsStuTotal(year, school, major, classic, "8");
			title = recruitPlanService.load(year).getRecruitPlanname()+" - 户口所在地";
			for(int index=0;index<list.size();index++){
				Map obj = (Map) list.get(index);
				sb.append("['"+obj.get("residence").toString()+obj.get("stu_num").toString()+"',"+BigDecimalUtil.mul(BigDecimalUtil.div(Double.parseDouble(obj.get("stu_num").toString()), total, 4),100)+"],");
				obj.put("percent", BigDecimalUtil.div(Double.parseDouble(obj.get("stu_num").toString()), total, 4));
			}
		}
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("r_year", year);
		condition.put("r_school", school);
		condition.put("r_major", major);
		condition.put("r_classic", classic);
		model.addAttribute("condition", condition);
		model.addAttribute("r_hide", hide);
		model.addAttribute("title", title);
		model.addAttribute("enrollRList", list);
		model.addAttribute("chart", "{type: 'pie',name: '户口所在地',data: ["+sb.toString()+"]}");
		return "/edu3/recruit/report/enrollstatisR-list";
	}
	
	
	/**
	 * 招生民族统计
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/recruit/recruitmanage/enrollStatisticNation.html")
	public String listEnrollStatisticNation(HttpServletRequest request, HttpServletResponse response, ModelMap model)throws WebException {
		String year    = request.getParameter("n_year");
		String school  = request.getParameter("n_school");
		String major   = request.getParameter("n_major");
		String classic = request.getParameter("n_classic");
		
		String hide    = "N";
		User user      = SpringSecurityHelper.getCurrentUser();
		if (ConfigPropertyUtil.getInstance().getProperty("brSchool.unitType").equals(user.getOrgUnit().getUnitType())) {
			school     = user.getOrgUnit().getResourceid();
			hide       = "Y";
		}
		
		List list      = recruitJDBCService.listEnrollNation(year, school, major, classic);
		StringBuffer sb = new StringBuffer();
		String title   = "";
		Map<String, String> dictMap = new HashMap<String, String>();
		if(null != list && !list.isEmpty()){
			List<Dictionary> children  = CacheAppManager.getChildren("CodeNation");//从缓存中获取字典对象
			if(children != null){
				for(Dictionary childDict : children){
					dictMap.put(childDict.getDictValue(), childDict.getDictName());
				}
			}

			Double total = recruitJDBCService.statisticsStuTotal(year, school, major, classic, "8");
			title = recruitPlanService.load(year).getRecruitPlanname()+" - 民族类型";
			for(int index=0;index<list.size();index++){
				Map obj = (Map) list.get(index);
				sb.append("['"+dictMap.get(obj.get("nation").toString()) + obj.get("stu_num").toString()+"',"+BigDecimalUtil.mul(BigDecimalUtil.div(Double.parseDouble(obj.get("stu_num").toString()), total, 4),100)+"],");
				obj.put("percent", BigDecimalUtil.div(Double.parseDouble(obj.get("stu_num").toString()), total, 4));
			}
		}
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("n_year", year);
		condition.put("n_school", school);
		condition.put("n_major", major);
		condition.put("n_classic", classic);
		model.addAttribute("condition", condition);
		model.addAttribute("n_hide", hide);
		model.addAttribute("title", title);
		model.addAttribute("enrollNationList", list);
		model.addAttribute("NationMap", dictMap);
		model.addAttribute("chart", "{type: 'pie',name: '民族',data: ["+sb.toString()+"]}");
		return "/edu3/recruit/report/enrollstatisNation-list";
	}
	
	
	/**
	 * 政治面貌统计
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/recruit/recruitmanage/enrollStatisticPolitics.html")
	public String listEnrollStatisticPolitics(HttpServletRequest request, HttpServletResponse response, ModelMap model)throws WebException {
		
		String year    = request.getParameter("p_year");
		String school  = request.getParameter("p_school");
		String major   = request.getParameter("p_major");
		String classic = request.getParameter("p_classic");
		
		String hide    = "N";
		User user      = SpringSecurityHelper.getCurrentUser();
		if (ConfigPropertyUtil.getInstance().getProperty("brSchool.unitType").equals(user.getOrgUnit().getUnitType())) {
			school     = user.getOrgUnit().getResourceid();
			hide       = "Y";
		}
		
		List list      = recruitJDBCService.listEnrollPolitics(year, school, major, classic);
		StringBuffer sb = new StringBuffer();
		String title   = "";
		Map<String, String> dictMap = new HashMap<String, String>();
		if(null != list && !list.isEmpty()){
			List<Dictionary> children  = CacheAppManager.getChildren("CodePolitics");//从缓存中获取字典对象
			if(children != null){
				for(Dictionary childDict : children){
					dictMap.put(childDict.getDictValue(), childDict.getDictName());
				}
			}

			Double total = recruitJDBCService.statisticsStuTotal(year, school, major, classic, "8");
			title = recruitPlanService.load(year).getRecruitPlanname()+" - 政治面貌";
			for(int index=0;index<list.size();index++){
				Map obj = (Map) list.get(index);
				sb.append("['"+dictMap.get(obj.get("politics").toString()) + obj.get("stu_num").toString()+"',"+BigDecimalUtil.mul(BigDecimalUtil.div(Double.parseDouble(obj.get("stu_num").toString()), total, 4),100)+"],");
				obj.put("percent", BigDecimalUtil.div(Double.parseDouble(obj.get("stu_num").toString()), total, 4));
			}
		}
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("p_year", year);
		condition.put("p_school", school);
		condition.put("p_major", major);
		condition.put("p_classic", classic);
		model.addAttribute("condition", condition);
		model.addAttribute("p_hide", hide);
		model.addAttribute("title", title);
		model.addAttribute("enrollPoliticsList", list);
		model.addAttribute("politicsMap", dictMap);
		model.addAttribute("chart", "{type: 'pie',name: '政治面貌',data: ["+sb.toString()+"]}");
		return "/edu3/recruit/report/enrollstatisPolitics-list";
	}
	
	/**
	 * 招生婚否统计
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/recruit/recruitmanage/enrollStatisticMarry.html")
	public String listEnrollStatisticMarry(HttpServletRequest request, HttpServletResponse response, ModelMap model)throws WebException {
		String year    = request.getParameter("m_year");
		String school  = request.getParameter("m_school");
		String major   = request.getParameter("m_major");
		String classic = request.getParameter("m_classic");
		
		String hide    = "N";
		User user      = SpringSecurityHelper.getCurrentUser();
		if (ConfigPropertyUtil.getInstance().getProperty("brSchool.unitType").equals(user.getOrgUnit().getUnitType())) {
			school     = user.getOrgUnit().getResourceid();
			hide       = "Y";
		}
		
		List list = recruitJDBCService.listEnrollMarry(year, school, major, classic);
		StringBuffer sb = new StringBuffer();
		String title = "";
		Map<String, String> dictMap = new HashMap<String, String>();
		if(null != list && !list.isEmpty()){
			List<Dictionary> children  = CacheAppManager.getChildren("CodeMarriage");//从缓存中获取字典对象
			if(children != null){
				for(Dictionary childDict : children){
					dictMap.put(childDict.getDictValue(), childDict.getDictName());
				}
			}

			Double total = recruitJDBCService.statisticsStuTotal(year, school, major, classic, "8");
			title = recruitPlanService.load(year).getRecruitPlanname()+" - 婚姻状况";
			for(int index=0;index<list.size();index++){
				Map obj = (Map) list.get(index);
				sb.append("['"+dictMap.get(obj.get("marriage").toString()) + obj.get("stu_num").toString()+"',"+BigDecimalUtil.mul(BigDecimalUtil.div(Double.parseDouble(obj.get("stu_num").toString()), total, 4),100)+"],");
				obj.put("percent", BigDecimalUtil.div(Double.parseDouble(obj.get("stu_num").toString()), total, 4));
			}
		}
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("m_year", year);
		condition.put("m_school", school);
		condition.put("m_major", major);
		condition.put("m_classic", classic);
		model.addAttribute("condition", condition);
		model.addAttribute("m_hide", hide);
		model.addAttribute("title", title);
		model.addAttribute("enrollMList", list);
		model.addAttribute("MMap", dictMap);
		model.addAttribute("chart", "{type: 'pie',name: '婚姻状况',data: ["+sb.toString()+"]}");
		return "/edu3/recruit/report/enrollstatisM-list";
	}
	/**
	 * 招生综合统计
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/recruit/recruitmanage/enrollStatisticStuInfo.html")
	public String listEnrollStatisticComplex(HttpServletRequest request,HttpServletResponse response,ModelMap model){

		Map<String,Object> condition = new HashMap<String, Object>();
		Map<String,Object> resultMap = new HashMap<String, Object>();
		List<Object>      resultList = new ArrayList<Object>();
		//String recruitPlan           = ExStringUtils.trimToEmpty(request.getParameter("recruitPlan"));
		String statisticType         = ExStringUtils.trimToEmpty(request.getParameter("statisticType"));
		condition 				     = getParams(request, condition);
		User user 					 = SpringSecurityHelper.getCurrentUser();
		String resultStr 		 	 = "";
		String reportTitle           = "";    
		String isBranchSchool        = "";
		String tdStr                 = "";
		String statisticColumn       = "";
		
		if (CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())) {
			isBranchSchool           = user.getOrgUnit().getResourceid();
			condition.put("isBrschool", "Y");
			condition.put("brSchool", isBranchSchool);
		}
		
		if (ExStringUtils.isNotEmpty(statisticType)&&!"-1".equals(statisticType)) {
			
			resultMap   	= recruitJDBCService.listEnrollStatisticComplex(condition);
			resultStr   	= resultMap.get("resultStr").toString();
			tdStr       	= resultMap.get("tdStr").toString();
			statisticColumn = resultMap.get("statisticColumn").toString();
			reportTitle = resultMap.get("reportTitle").toString();
			resultList  = (List<Object>) resultMap.get("resultList");
			
		}
		
		model.put("tdStr", tdStr);
		model.put("statisticColumn", statisticColumn);
		model.put("resultList", resultList);
		model.put("condition", condition);
		model.put("reportTitle",reportTitle);
		model.put("chart", resultStr);
		
		return"/edu3/recruit/report/statistic-complex-list";
	}
	/**
	 * 获取请求参数
	 * @param request
	 * @param condition
	 * @return
	 */
	private Map<String,Object>  getParams(HttpServletRequest request,Map<String,Object> condition){
		
		String recruitPlan           = ExStringUtils.trimToEmpty(request.getParameter("recruitPlan"));
		String brSchool	             = ExStringUtils.trimToEmpty(request.getParameter("brSchool"));
		String major                 = ExStringUtils.trimToEmpty(request.getParameter("major"));
		String classic               = ExStringUtils.trimToEmpty(request.getParameter("classic"));
		String sex                   = ExStringUtils.trimToEmpty(request.getParameter("sex"));
		String nation                = ExStringUtils.trimToEmpty(request.getParameter("nation"));
		String marriage              = ExStringUtils.trimToEmpty(request.getParameter("marriage"));
		String politics              = ExStringUtils.trimToEmpty(request.getParameter("politics"));
		String homePlace_province    = ExStringUtils.trimToEmpty(request.getParameter("homePlace_province"));
		String homePlace_city        = ExStringUtils.trimToEmpty(request.getParameter("homePlace_city"));
		String homePlace_county      = ExStringUtils.trimToEmpty(request.getParameter("homePlace_county"));
		String residence_province    = ExStringUtils.trimToEmpty(request.getParameter("residence_province"));
		String residence_city        = ExStringUtils.trimToEmpty(request.getParameter("residence_city"));
		String residence_county      = ExStringUtils.trimToEmpty(request.getParameter("residence_county"));
		String statisticType         = ExStringUtils.trimToEmpty(request.getParameter("statisticType"));
																				
		
		if (ExStringUtils.isNotEmpty(recruitPlan)) {
			condition.put("recruitPlan",recruitPlan);
		}
		if (ExStringUtils.isNotEmpty(brSchool)) {
			condition.put("brSchool",brSchool);
		}
		if (ExStringUtils.isNotEmpty(major)) {
			condition.put("major",major);
		}
		if (ExStringUtils.isNotEmpty(classic)) {
			condition.put("classic",classic);
		}
		if (ExStringUtils.isNotEmpty(sex)) {
			condition.put("sex",sex);
		}
		if (ExStringUtils.isNotEmpty(nation)) {
			condition.put("nation",nation);
		}
		if (ExStringUtils.isNotEmpty(marriage)) {
			condition.put("marriage",marriage);
		}
		if (ExStringUtils.isNotEmpty(politics)) {
			condition.put("politics",politics);
		}
		if (ExStringUtils.isNotEmpty(homePlace_province)) {
			condition.put("homePlace_province",homePlace_province);
		}
		if (ExStringUtils.isNotEmpty(homePlace_city)) {
			condition.put("homePlace_city",homePlace_city);
		}
		if (ExStringUtils.isNotEmpty(homePlace_county)) {
			condition.put("homePlace_county",homePlace_county);
		}
		if (ExStringUtils.isNotEmpty(residence_province)) {
			condition.put("residence_province",residence_province);
		}
		if (ExStringUtils.isNotEmpty(residence_city)) {
			condition.put("residence_city",residence_city);
		}
		if (ExStringUtils.isNotEmpty(residence_county)) {
			condition.put("residence_county",residence_county);
		}
		if (ExStringUtils.isNotEmpty(statisticType)) {
			condition.put("statisticType",statisticType);
		}
		
		return condition;
	}
}
