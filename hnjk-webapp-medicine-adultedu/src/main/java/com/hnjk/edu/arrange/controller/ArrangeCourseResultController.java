package com.hnjk.edu.arrange.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
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
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.BaseSupportController;
import com.hnjk.edu.arrange.model.TeachCourse;
import com.hnjk.edu.arrange.model.TeachCourseClasses;
import com.hnjk.edu.arrange.model.TeachCourseDetail;
import com.hnjk.edu.arrange.service.ITeachCourseClassesService;
import com.hnjk.edu.arrange.service.ITeachCourseDetailService;
import com.hnjk.edu.arrange.service.ITeachCourseService;
import com.hnjk.edu.arrange.vo.CourseDetailConflictVo;
import com.hnjk.edu.arrange.vo.TeachCourseDetailVo;
import com.hnjk.edu.basedata.service.IEdumanagerService;
import com.hnjk.edu.teaching.model.TeachingPlanCourseTimePeriod;
import com.hnjk.edu.teaching.service.ITeachingPlanCourseTimePeriodService;
import com.hnjk.edu.util.Condition2SQLHelper;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;

@Controller
public class ArrangeCourseResultController extends BaseSupportController{

	private static final long serialVersionUID = 1L;

	@Autowired
	@Qualifier("teachCourseDetailService")
	private ITeachCourseDetailService teachCourseDetailService;
	
	@Autowired
	@Qualifier("teachCourseService")
	private ITeachCourseService teachCourseService;
	
	@Autowired
	@Qualifier("teachCourseClassesService")
	private ITeachCourseClassesService teachCourseClassesService;
	
	@Autowired
	@Qualifier("edumanagerService")
	private IEdumanagerService edumanagerService;
	
	@Autowired
	@Qualifier("teachingPlanCourseTimePeriodService")
	private ITeachingPlanCourseTimePeriodService teachingPlanCourseTimePeriodService;
	
	/**
	 * 排课结果页面
	 * @param request
	 * @param model
	 * @param objPage
	 * @return
	 */
	@RequestMapping("/edu3/arrange/arrangecourseresult/list.html")
	public String arrangeCourseList(HttpServletRequest request,ModelMap model,Page objPage){
		Map<String,Object> condition = Condition2SQLHelper.getConditionFromResquestByIterator(request);
		boolean isBrschool = false;
		User user = SpringSecurityHelper.getCurrentUser();
		//判断是否为校外学习中心人员
		if(user.getOrgUnit().getUnitType().equals(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())){
			isBrschool = true;
			condition.put("brSchoolid", user.getUnitId());
			model.addAttribute("schoolname",ExStringUtils.trim(user.getOrgUnit().getUnitName()));
		}
		model.addAttribute("isBrschool", isBrschool);
		List<TeachCourse> teachCourseList = teachCourseService.findTeachCourseByCondition(condition);
		List<TeachCourseDetailVo> detailVos = getTeachCourseDetailVos(teachCourseList,null);
		model.addAttribute("list", detailVos);
		model.addAttribute("condition", condition);
		return "/edu3/arrange/arrangeCourseResult/arrangeCourse-list";
	}

	/**
	 * 获取排课详情Vo对象	By TeachCourse
	 * @param teachCourseList, conflictlList
	 * @return
	 */
	private List<TeachCourseDetailVo> getTeachCourseDetailVos(List<TeachCourse> teachCourseList,List<CourseDetailConflictVo> conflictlList) {
		List<TeachCourseDetailVo> detailVos = new ArrayList<TeachCourseDetailVo>();
		
		String leftSymbol = "<label class='conflict'>";
		String rightSymbol = "</label>";
		Map<String, Object> detailConflictMap = new HashMap<String, Object>();
		if(conflictlList==null){
			conflictlList = new ArrayList<CourseDetailConflictVo>();
		}
		for (CourseDetailConflictVo conflictVo : conflictlList) {//排课详情和类型
			if(conflictVo.getType().equals("room")){
				detailConflictMap.put(conflictVo.getDetailid(), conflictVo.getType());
			}
		}
		for (TeachCourse teachCourse : teachCourseList) {
			Set<TeachCourseDetail> teachCourseDetails = teachCourse.getTeachCourseDetails();
			TeachCourseDetailVo detailVo = new TeachCourseDetailVo();//排课详情vo
			List<String> timePeriodlList =  new ArrayList<String>();//时间段
			List<String> classroomList =  new ArrayList<String>();//上课地点
			List<String> weekList = new ArrayList<String>();//周数 
			List<String> dayList = new ArrayList<String>();//星期 
			String arrangementName = "";//排课人
			String teacherNames = teachCourse.getTeacherNames();//主讲老师
			String scoreTeacherNames = "";//登分老师
			Integer studyHour = 0;//学时
			boolean isConflict = false;
			detailVo.setClassTitleName(teachCourse.getClassNames());
			detailVo.setClassName(teachCourse.getClassNames());
			String hql = " from "+TeachCourseClasses.class.getSimpleName()+" where isDeleted=0 and teachCourse.resourceid=?";
			List<TeachCourseClasses> tcClassesList = teachCourseClassesService.findByHql(hql, teachCourse.getResourceid());
			for (TeachCourseClasses tcClass : tcClassesList) {
				if(tcClass.getRecordScorer()!=null && !scoreTeacherNames.contains(tcClass.getRecordScorer().getCnName())){
					scoreTeacherNames += tcClass.getRecordScorer().getCnName()+"，";
				}
			}
			if(ExStringUtils.isNotBlank(scoreTeacherNames)) scoreTeacherNames = scoreTeacherNames.substring(0, scoreTeacherNames.length()-1);
			teachCourse.setRecordScorerName(scoreTeacherNames);
			for (CourseDetailConflictVo conflictVo : conflictlList) {
				if(conflictVo.getTeachcourseid().equals(teachCourse.getResourceid())){//教学班是否冲突
					if(conflictVo.getType().equals("class")){//班级冲突
						detailVo.setClassTitleName(conflictVo.getName()+"上课时间冲突");
						detailVo.setClassName(teachCourse.getClassNames().replaceFirst(conflictVo.getName(), leftSymbol+conflictVo.getName()+rightSymbol));
						isConflict = true;
					}
					if(conflictVo.getType().equals("teacher") && !isConflict){//老师冲突
						//edumanagerService.get(conflictVo.getConflict());
						if(teacherNames!=null && teacherNames.contains(conflictVo.getName())){
							teacherNames = teacherNames.replace(conflictVo.getName(), leftSymbol+conflictVo.getName()+rightSymbol);
						}
						isConflict = true;
					}
				}
			}
			String timePeriod = "";//上课时间（时间段+星期+课室）
			String classroomTitle = "";//上课时间title(如果冲突则提示冲突信息)
			String weeks = "";//星期
			TeachCourseDetail _tcDetail = null;
			for (TeachCourseDetail tcDetail : teachCourseDetails) {
				boolean isSameDay = false;
				boolean isSameWeek = false;
				boolean isSameTimePeriod = false;
				boolean isSameClassroom = false;
				if(_tcDetail!=null){
					if(tcDetail.getDays()!=null && tcDetail.getDays().equals(_tcDetail.getDays())) isSameDay=true;
					if(tcDetail.getWeeks()!=null && tcDetail.getWeeks().equals(_tcDetail.getWeeks())) isSameWeek=true;
					if(tcDetail.getTimePeriodids()!=null && tcDetail.getTimePeriodids().equals(_tcDetail.getTimePeriodids())) isSameTimePeriod=true;
					if(tcDetail.getClassroom()!=null && tcDetail.getClassroom().getResourceid().equals(_tcDetail.getClassroom().getResourceid())) isSameClassroom=true;
				}
				if(!isSameDay){//不是同一个星期，则重新赋值
					if(_tcDetail!=null){//如果不是第一条数据，则保存
						if(ExStringUtils.isNotBlank(_tcDetail.getDays())) dayList.add(_tcDetail.getDays());
						if(ExStringUtils.isNotBlank(weeks)) weekList.add(ExStringUtils.orderDistinctDigitalString(weeks, 30));
						timePeriodlList.add(timePeriod.substring(0, timePeriod.length()-1));
						classroomList.add(classroomTitle.substring(0, classroomTitle.length()-1));
					}
					weeks = "";
					classroomTitle = "";
					timePeriod = "";
				}
				//排课员
				if(tcDetail.getOperatorName()!=null && !arrangementName.contains(tcDetail.getOperatorName())){//排课人
					arrangementName += tcDetail.getOperatorName()+",";
				}else if (tcDetail.getOperator()!=null && !arrangementName.contains(tcDetail.getOperator().getCnName())) {
					arrangementName += tcDetail.getOperator().getCnName()+",";
				}
				//上课时间
				//if(ExStringUtils.isNotBlank(timePeriods) && !(!isSameTimePeriod && isSameDay && isSameClassroom && isSameWeek)){
				if(tcDetail.getTimePeriods()!=null){
					for (TeachingPlanCourseTimePeriod tempTime : tcDetail.getTimePeriods()) {
						timePeriod += tempTime.getTimeName()+",";
					}
					//timePeriod = timePeriod.substring(0, timePeriod.length()-1);
				}
				if(tcDetail.getDateType()==0){//周期
					//timePeriod += "；"+tcDetail.getWeeksName();
					//if(!(!isSameWeek && isSameDay && isSameTimePeriod && isSameClassroom)){
						weeks += tcDetail.getWeeks()+",";
						timePeriod += tcDetail.getWeeksName()+",";
					//}
				}else {//日期
					try {
						if(tcDetail.getStartdate()!=null) timePeriod += ExDateUtils.formatDateStr(tcDetail.getStartdate(), ExDateUtils.PATTREN_DATE);
						if(tcDetail.getEnddate()!=null) timePeriod += "至"+ExDateUtils.formatDateStr(tcDetail.getEnddate(), ExDateUtils.PATTREN_DATE)+",";
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
				if(tcDetail.getClassroom()!=null){//上课地点
					//timePeriod += "；";
					String room = tcDetail.getClassroom().getLayerNo()+"楼"+tcDetail.getClassroom().getUnitNo()+"号"+tcDetail.getClassroom().getClassroomName();
					boolean hasKey = false;
					for (String key : detailConflictMap.keySet()) {
						if(key.equals(tcDetail.getResourceid()) && detailConflictMap.get(key).equals("room") && !isConflict){//课室冲突
							classroomTitle += room+"上课时间冲突,";
							timePeriod += leftSymbol+room+rightSymbol+",";
							hasKey = true;
							break;
						}
					}
					if(!hasKey) {//课室无冲突,并且班级和老师不冲突
						classroomTitle = timePeriod+room+",";
						timePeriod += room+"；";
					}
				}
				_tcDetail = tcDetail;
				studyHour += tcDetail.getStudyHour()!=null?tcDetail.getStudyHour():0;
			}
			//保存最后一条记录
			if(_tcDetail!=null){
				if(ExStringUtils.isNotBlank(_tcDetail.getDays())) dayList.add(_tcDetail.getDays());
				if(ExStringUtils.isNotBlank(weeks)) weekList.add(weeks.substring(0, weeks.length()-1));
				timePeriodlList.add(timePeriod.substring(0, timePeriod.length()-1));
				classroomList.add(classroomTitle.substring(0, classroomTitle.length()-1));
			}
			if(ExStringUtils.isNotBlank(teacherNames)) detailVo.setTeacherName(" "+teacherNames);
			if(ExStringUtils.isNotBlank(arrangementName)) arrangementName = arrangementName.substring(0, arrangementName.length()-1);
			teachCourse.setArrangeStudyHour(studyHour);
			detailVo.setTeachCourse(teachCourse);
			detailVo.setArrangementName(arrangementName);
			detailVo = orderWeekAndTime(dayList,weekList,timePeriodlList,classroomList,detailVo);
			detailVos.add(detailVo);
		}
		return detailVos;
	}
	
	/**
	 * 课周表排序：1，2，3，4，5，6，7
	 * @param weeklList
	 * @param timePeriodlList
	 */
	private TeachCourseDetailVo orderWeekAndTime(List<String> dayList,List<String> weekList,List<String> timePeriodlList,List<String> classroomList,TeachCourseDetailVo detailVo) {
		List<String> order =  Arrays.asList("1","2","3","4","5","6","7");
		List<String> _dayList = new ArrayList<String>();
		List<String> _weekList = new ArrayList<String>();
		List<String> _timePeriodlList = new ArrayList<String>();
		List<String> _classroomList = new ArrayList<String>();
		for (int i = 0; i < order.size(); i++) {
			String day = "";
			String weeks = "";
			String timePeriod = "";
			String classroom = "";
			for (int k = 0; k < dayList.size(); k++) {
				if(order.get(i).equals(dayList.get(k))){
					day = dayList.get(k);
					weeks = weekList.get(k);
					timePeriod = timePeriodlList.get(k);
					classroom = classroomList.get(k);
					break;
				}
			}
			_dayList.add(day);
			_weekList.add(weeks);
			_timePeriodlList.add(timePeriod);
			_classroomList.add(classroom);
		}
		detailVo.setWeeks(_weekList);
		detailVo.setTimePeriod(_timePeriodlList);
		detailVo.setClassroom(_classroomList);
		return detailVo;
	}
	
	/**
	 * 导出列表
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/arrange/arrangeCourse/export.html")
	public String exportPageList(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		Map<String,Object> condition = Condition2SQLHelper.getConditionFromResquestByIterator(request);
		User user = SpringSecurityHelper.getCurrentUser();
		//判断是否为校外学习中心人员
		if(user.getOrgUnit().getUnitType().equals(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())){
			condition.put("brSchoolid", user.getOrgUnit().getResourceid());
		}
		List<TeachCourse> teachCourseList = teachCourseService.findTeachCourseByCondition(condition);
		List<TeachCourseDetailVo> detailVos = getTeachCourseDetailVos(teachCourseList,null);
		String fileName = "";
		try {
			fileName = URLEncoder.encode("排课列表.xls", "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		request.getSession().setAttribute("list", detailVos);
		response.setContentType("application/vnd.ms-excel");
	  	response.setHeader("Content-Disposition", "attachment; filename="+fileName);
		return "/edu3/arrange/arrangeCourseResult/arrangeCourse-export";
	}

	/**
	 * 发布课表
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/arrange/arrangeCourse/schedule.html")
	public String publishSchedule(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		Map<String,Object> condition = Condition2SQLHelper.getConditionFromResquestByIterator(request);
		User user = SpringSecurityHelper.getCurrentUser();
		//判断是否为校外学习中心人员
		if(user.getOrgUnit().getUnitType().equals(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())){
			condition.put("brSchoolid", user.getOrgUnit().getResourceid());
		}
		List<TeachCourse> teachCourseList = teachCourseService.findTeachCourseByCondition(condition);
		List<TeachCourseDetailVo> detailVos = getTeachCourseDetailVos(teachCourseList,null);
		String fileName = "";
		try {
			fileName = URLEncoder.encode("课表安排表.xls", "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		request.getSession().setAttribute("list", detailVos);
		response.setContentType("application/vnd.ms-excel");
	  	response.setHeader("Content-Disposition", "attachment; filename="+fileName);
		return "/edu3/arrange/arrangeCourseResult/arrangeCourse-schedule";
	}
	
	/**
	 * 检查冲突
	 * @param request
	 * @param model
	 * @param objPage
	 * @return
	 */
	@RequestMapping("/edu3/arrange/arrangeCourse/checkConflict.html")
	public String checkConflict(HttpServletRequest request,ModelMap model,Page objPage){
		Map<String,Object> condition = Condition2SQLHelper.getConditionFromResquestByIterator(request);
		
		boolean isBrschool = false;
		User user = SpringSecurityHelper.getCurrentUser();
		//判断是否为校外学习中心人员
		if(user.getOrgUnit().getUnitType().equals(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())){
			isBrschool = true;
			condition.put("brSchoolid", user.getUnitId());
			model.addAttribute("schoolname",ExStringUtils.trim(user.getOrgUnit().getUnitName()));
		}
		model.addAttribute("isBrschool", isBrschool);
		condition.put("ischeck", "Y");
		List<TeachCourse> arrangeCourseList = teachCourseService.findTeachCourseByCondition(condition);
		List<Map<String, Object>> conflictMapList = teachCourseDetailService.findCourseDetailConflictMapByCondition(condition);
		
		//以教学点为单位进行冲突检查
		String conflict = "";//冲突源（班级，老师，课室）
		String days = "";//星期
		List<List<String>> weekList = null;//保存临时冲突的周期
		List<List<String>> timeperiodidList = null;//保存临时冲突的上课时间
		List<CourseDetailConflictVo> conflictVolList = new ArrayList<CourseDetailConflictVo>(); 
		for (int i=0; i<conflictMapList.size();i++) {
			boolean isConflict = true;
			Map<String, Object> map = conflictMapList.get(i);
			if(map.get("conflict")==null || !conflict.equals(map.get("conflict"))){//是否同一个冲突源
				isConflict = false;
			}
			if(isConflict && (map.get("days")==null || !days.equals(map.get("days")))){//是否同一个星期
				isConflict = false;
			}
			String[] weeks = null;
			String[] timeperiodids = null;
			if(map.get("weeks")==null){//周期是否为空
				isConflict = false;
			}else{
				weeks = map.get("weeks").toString().split(",");
			}
			if(map.get("timeperiodids")==null){//上课时间是否为空
				isConflict = false;
			}else {
				timeperiodids = map.get("timeperiodids").toString().split(",");
			}
			if(!isConflict){//如果不冲突（冲突源，星期不相同则清空周期和上课时间段）
				weekList = new ArrayList<List<String>>();
				timeperiodidList = new ArrayList<List<String>>();
			}else{	//判断相同冲突源、星期下，周期是否有冲突
				isConflict = false;
				boolean _b = false;//结束外层循环
				for (List<String> list : weekList) {
					if(_b){
						break;
					}
					for (String week : weeks) {
						if(list.contains(week)){
							isConflict = true;
							_b = true;
							break;
						}
					}
				}
				if(isConflict){
					for (List<String> list : timeperiodidList) {
						for (String timeperiodid : timeperiodids) {
							//同一条件下，如果上课时间有冲突，将map转为vo并保存
							if(list.contains(timeperiodid)){
								if(!conflictVolList.contains(getCourseDetailConflictVo(conflictMapList.get(i-1)))){//判断第一个map是否已经保存过
									conflictVolList.add(getCourseDetailConflictVo(conflictMapList.get(i-1)));
								}
								conflictVolList.add(getCourseDetailConflictVo(map));
							}
						}
					}
				}
			}
			conflict = map.get("conflict").toString();
			days = map.get("days").toString();
			if(weeks!=null) weekList.add(Arrays.asList(weeks));
			if(timeperiodids!=null) timeperiodidList.add(Arrays.asList(timeperiodids));
		}
		List<TeachCourseDetailVo> detailVos = getTeachCourseDetailVos(arrangeCourseList,conflictVolList);
		model.addAttribute("list", detailVos);
		model.addAttribute("ischeck", "Y");
		model.addAttribute("condition", condition);
		return "/edu3/arrange/arrangeCourseResult/arrangeCourse-list";
	}


	/**
	 * map转为排课详情冲突vo
	 * @param map
	 * @return
	 */
	private CourseDetailConflictVo getCourseDetailConflictVo(Map<String, Object> map) {
		CourseDetailConflictVo conflictVo = new CourseDetailConflictVo();
		conflictVo.setTeachcourseid(map.get("teachcourseid").toString());
		conflictVo.setDetailid(map.get("detailid").toString());
		conflictVo.setConflict(map.get("conflict").toString());
		conflictVo.setName(map.get("name").toString());
		conflictVo.setType(map.get("type").toString());
		conflictVo.setDays(map.get("days")!=null?map.get("days").toString():"");
		conflictVo.setWeeks(map.get("weeks")!=null?map.get("weeks").toString():"");
		conflictVo.setTimeperiodids(map.get("timeperiodids")!=null?map.get("timeperiodids").toString():"");
		return conflictVo;
	}
}
