package com.hnjk.edu.util;

import com.hnjk.core.foundation.utils.ExStringUtils;
import org.springframework.ui.ModelMap;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 属性名称
 * 年度：yearInfo
 * 学期：term
 * 教学点：unit
 * 开课学期：openTerm
 * 层次：classic
 * 学习形式：teachingType
 * 教学类型：teachType
 * 班级：classes
 * 课程：course
 * 状态：status
 * 名称：name
 * @author Administrator
 *
 */
public class Condition2SQLHelper {
	

	@Deprecated
	public static Map<String, Object> getConditionFromResquest(HttpServletRequest request) {
		Map<String, Object> conditionMap = new HashMap<String, Object>();
		String yearInfoId		=		ExStringUtils.trim(request.getParameter("yearInfoId"));//年度
		String term				=		ExStringUtils.trim(request.getParameter("term"));//学期
		String brSchoolid		=		ExStringUtils.trim(request.getParameter("brSchoolid"));//学习中心
		String brSchoolId 		= 		ExStringUtils.trim(request.getParameter("brSchoolId"));
		String branchSchool 	= 		ExStringUtils.trim(request.getParameter("branchSchool"));
		String gradeid 			= 		ExStringUtils.trim(request.getParameter("gradeid"));//年级
		String gradeId 			= 		ExStringUtils.trim(request.getParameter("gradeId"));
		String majorid 			= 		ExStringUtils.trim(request.getParameter("majorid"));//专业
		String majorId 			= 		ExStringUtils.trim(request.getParameter("majorId"));
		String classicid		=		ExStringUtils.trim(request.getParameter("classicid"));//层次
		String classicId 		= 		ExStringUtils.trim(request.getParameter("classicId"));
		String teachingType		=		ExStringUtils.trim(request.getParameter("teachingType"));//学习形式
		String teachType		=		ExStringUtils.trim(request.getParameter("teachType"));//教学类型
		String classesid		=		ExStringUtils.trim(request.getParameter("classesid"));//班级
		String classesId		=		ExStringUtils.trim(request.getParameter("classesId"));
		String classid			=		ExStringUtils.trim(request.getParameter("classid"));
		String openTerm			=		ExStringUtils.trim(request.getParameter("openTerm"));//上课学期
		String courseid			=		ExStringUtils.trim(request.getParameter("courseid"));//课程
		String courseId			=		ExStringUtils.trim(request.getParameter("courseId"));
		String status			=		ExStringUtils.trim(request.getParameter("status"));//状态
		String name				=		ExStringUtils.trim(request.getParameter("name"));//名称
		//排课模块
		String schoolCalendarid	=		ExStringUtils.trim(request.getParameter("schoolCalendarid"));//院历
		String schoolCalendar	=		ExStringUtils.trim(request.getParameter("schoolCalendar"));
		String teachCourseid	=		ExStringUtils.trim(request.getParameter("teachCourseid"));//教学班
		String teachingClassname=		ExStringUtils.trim(request.getParameter("teachingClassname"));//教学班名
		String publishStatus	=		ExStringUtils.trim(request.getParameter("publishStatus"));//发布状态
		String arrangeStatus	=		ExStringUtils.trim(request.getParameter("arrangeStatus"));//排课状态
		String templateName	=		ExStringUtils.trim(request.getParameter("templateName"));//模版名称
		
		if(ExStringUtils.isNotBlank(yearInfoId)) {//年度
			conditionMap.put("yearInfoId", yearInfoId);
		}
		if(ExStringUtils.isNotBlank(term)) {//学期
			conditionMap.put("term", term);
		}
		if(ExStringUtils.isNotBlank(brSchoolId)){//学习中心
			conditionMap.put("brSchoolId", brSchoolId);
		}
		if (ExStringUtils.isNotBlank(branchSchool)) {
			conditionMap.put("branchSchool", branchSchool);
		}
		if(ExStringUtils.isNotBlank(brSchoolid)){
			conditionMap.put("brSchoolid", brSchoolid);
		}
		if(ExStringUtils.isNotBlank(gradeId)){//年级
			conditionMap.put("gradeId", gradeId);
		}
		if(ExStringUtils.isNotBlank(gradeid)){
			conditionMap.put("gradeid", gradeid);
		}
		if(ExStringUtils.isNotBlank(classicid)){//层次
			conditionMap.put("classicid", classicid);
		}
		if(ExStringUtils.isNotBlank(classicId)){
			conditionMap.put("classicId", classicId);
		}
		if(ExStringUtils.isNotBlank(teachingType)){//学习形式
			conditionMap.put("teachingType", teachingType);
		}
		if(ExStringUtils.isNotBlank(teachType)){//教学类型
			conditionMap.put("teachType", teachType);
		}
		if(ExStringUtils.isNotBlank(majorId)){//专业
			conditionMap.put("majorId", majorId);
		}
		if(ExStringUtils.isNotBlank(majorid)){
			conditionMap.put("majorid", majorid);
		}
		if(ExStringUtils.isNotBlank(classesId)){//班级
			conditionMap.put("classesId", classesId);
		}
		if(ExStringUtils.isNotBlank(classesid)){
			conditionMap.put("classesid", classesid);
		}
		if(ExStringUtils.isNotBlank(classid)){
			conditionMap.put("classid", classid);
		}
		if(ExStringUtils.isNotBlank(openTerm)){//开课学期
			conditionMap.put("openTerm", openTerm);
		}
		if(ExStringUtils.isNotBlank(courseid)){//课程
			conditionMap.put("courseid", courseid);
		}
		if(ExStringUtils.isNotBlank(courseId)){
			conditionMap.put("courseId", courseId);
		}
		if(ExStringUtils.isNotBlank(status)){
			conditionMap.put("status", status);//状态
		}
		if(ExStringUtils.isNotBlank(name)){//名称
			conditionMap.put("name", name);
		}
		//排课模块
		if(ExStringUtils.isNotBlank(schoolCalendarid)) {
			conditionMap.put("schoolCalendarid", schoolCalendarid);
		}else if(ExStringUtils.isNotBlank(schoolCalendar)) {
			conditionMap.put("schoolCalendarid", schoolCalendar);
		}
		if (ExStringUtils.isNotBlank(teachCourseid)) {
			conditionMap.put("teachCourseid", teachCourseid);
		}
		if (ExStringUtils.isNotBlank(teachingClassname)) {
			conditionMap.put("teachingClassname", teachingClassname);
		}
		if (ExStringUtils.isNotBlank(publishStatus)) {
			conditionMap.put("publishStatus", publishStatus);
		}
		if (ExStringUtils.isNotBlank(arrangeStatus)) {
			conditionMap.put("arrangeStatus", arrangeStatus);
		}
		if (ExStringUtils.isNotBlank(templateName)) {
			conditionMap.put("templateName", templateName);
		}
		return conditionMap;
	}
	
	/**
	 * 遍历request中的参数（会过滤的值：null、""、undefined）
	 * @param request
	 * @return
	 */
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
	
	/**
	 * 表别名：c
	 * 属性名称
	 * 年度：yearInfo
	 * 学期：term
	 * 教学点：unit
	 * 年级：grade
	 * 层次：classic
	 * 学习形式：teachingType
	 * 教学类型：teachType
	 * 专业：major
	 * 班级：classes
	 * 课程：course
	 * 开课学期：openTerm
	 * 状态：status
	 * @param condition
	 * @param values
	 * @param hql
	 * @param objectName
	 * @return
	 */
	@Deprecated
	public static StringBuffer getHqlByCondition(Map<String, Object> condition,Map<String, Object> values,StringBuffer hql,String objectName) {
		if(values==null){
			values = new HashMap<String, Object>();
		}
		if(hql==null){
			hql = new StringBuffer();
		}
		hql.append(" from "+objectName+" where isDeleted = :isDeleted ");
		values.put("isDeleted", 0);
		if(condition.containsKey("yearInfoId")){//年度
			hql.append(" and yearInfo.resourceid=:yearInfoId ");
			values.put("yearInfoId", condition.get("yearInfoId"));
		}
		if(condition.containsKey("term")){//学期
			hql.append(" and term=:term ");
			values.put("term", condition.get("term"));
		}
		if(condition.containsKey("brSchoolid")){//教学点
			hql.append(" and unit.resourceid=:brSchoolid ");
			values.put("brSchoolid", condition.get("brSchoolid"));
		}else if (condition.containsKey("brSchoolId")){
			hql.append(" and unit.resourceid=:brSchoolId ");
			values.put("brSchoolId", condition.get("brSchoolId"));
		}else if (condition.containsKey("branchSchool")) {
			hql.append(" and unit.resourceid=:branchSchool ");
			values.put("branchSchool", condition.get("branchSchool"));
		}
		if(condition.containsKey("gradeId")){//年级
			hql.append(" and grade.resourceid=:gradeId ");
			values.put("gradeId", condition.get("gradeId"));
		}else if (condition.containsKey("gradeid")){
			hql.append(" and grade.resourceid=:gradeid ");
			values.put("gradeid", condition.get("gradeid"));
		}
		if(condition.containsKey("classicid")){//层次
			hql.append(" and classicid.resourceid=:classicid ");
			values.put("classicid", condition.get("classicid"));
		}else if (condition.containsKey("classicId")) {
			hql.append(" and classiresourceid=:classicId ");
			values.put("classicId", condition.get("classicId"));
		}
		if(condition.containsKey("teachingType")){//学习形式
			hql.append(" and teachingType=:teachingType ");
			values.put("teachingType", condition.get("teachingType"));
		}
		if(condition.containsKey("teachType")){//教学类型
			hql.append(" and teachType=:teachType ");
			values.put("teachType", condition.get("teachType"));
		}
		if(condition.containsKey("majorId")){//专业
			hql.append(" and major.resourceid=:majorId ");
			values.put("majorId", condition.get("majorId"));
		}else if (condition.containsKey("majorid")) {
			hql.append(" and major.resourceid=:majorid ");
			values.put("majorid", condition.get("majorid"));
		}
		if(condition.containsKey("classesId")){//班级
			hql.append(" and classes.resourceid=:classesId ");
			values.put("classesId", condition.get("classesId"));
		}else if (condition.containsKey("classesid")){
			hql.append(" and classes.resourceid=:classesid ");
			values.put("classesid", condition.get("classesid"));
		}else if (condition.containsKey("classid")) {
			hql.append(" and classes.resourceid=:classid ");
			values.put("classid", condition.get("classid"));
		}
		if(condition.containsKey("openTerm")){//开课学期
			hql.append(" and openTerm=:openTerm ");
			values.put("openTerm", condition.get("openTerm"));
		}
		if(condition.containsKey("courseid")){//课程
			hql.append(" and course.resourceid=:courseid ");
			values.put("courseid", condition.get("courseid"));
		}else if (condition.containsKey("courseId")) {
			hql.append(" and course.resourceid=:courseId ");
			values.put("courseId", condition.get("courseId"));
		}
		if(condition.containsKey("status")){//状态
			hql.append(" and to_char(status)=:status ");
			values.put("status", condition.get("status"));
		}
		if(condition.containsKey("name")){//名称
			hql.append(" and name like :name ");
			values.put("name", "%"+condition.get("name")+"%");
		}
		if(condition.containsKey("type")){//类型
			hql.append(" and to_char(type) =:type ");
			values.put("type", condition.get("type"));
		}
		if(condition.containsKey("generateStatus")){//生成状态
			hql.append(" and to_char(generateStatus)=:generateStatus ");
			values.put("generateStatus", condition.get("generateStatus"));
		}
		if(condition.containsKey("publishStatus")){//发布状态
			hql.append(" and to_char(publishStatus)=:publishStatus ");
			values.put("publishStatus", condition.get("publishStatus"));
		}
		if(condition.containsKey("selectedStatus")){//选课状态
			hql.append(" and to_char(selectedStatus)=:selectedStatus ");
			values.put("selectedStatus", condition.get("selectedStatus"));
		}
		if(condition.containsKey("arrangeStatus")){//排课状态
			hql.append(" and to_char(arrangeStatus)=:arrangeStatus ");
			values.put("arrangeStatus", condition.get("arrangeStatus"));
		}
		if(condition.containsKey("resourceids")){
			hql.append(" and resourceid in (" + condition.get("resourceids").toString() + ") ");
		}else if(condition.containsKey("resourceid")){
			hql.append(" and resourceid =:resourceid ");
			values.put("resourceid", condition.get("resourceid"));
		}
		return hql;
	}

	public static void addMapFromResquestByIterator(HttpServletRequest request, ModelMap model) {
		// TODO Auto-generated method stub
		if(model!=null){
			Enumeration em = request.getParameterNames();
			while (em.hasMoreElements()) {
				String name = (String) em.nextElement();
				Object value = request.getParameter(name);
				if(value!=null && !"undefined".equals(value) && !"".equals(value)){
					model.addAttribute(name, value);
				}
			}
		}
	}
	
	public static ModelMap getModelMapFromResquestByIterator(HttpServletRequest request, ModelMap model) {
		// TODO Auto-generated method stub
		if(model==null){
			model = new ModelMap();
		}
		Enumeration em = request.getParameterNames();
		while (em.hasMoreElements()) {
			String name = (String) em.nextElement();
			Object value = request.getParameter(name);
			if(value!=null && !"undefined".equals(value) && !"".equals(value)){
				model.addAttribute(name, value);
			}
		}
		return model;
	}
}
