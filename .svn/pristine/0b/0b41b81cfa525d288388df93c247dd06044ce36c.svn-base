package com.hnjk.edu.teaching.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.learning.model.StudentLearnPlan;
import com.hnjk.edu.learning.service.IStudentLearnPlanService;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.teaching.vo.StudentExamResultsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.teaching.model.TeachingPlanCourse;
import com.hnjk.edu.teaching.service.ITeachingPlanCourseService;
/**
 * 
 * <code>教学计划课程ServiceImpl</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-8-17 上午09:38:43
 * @see 
 * @version 1.0
 */
@Service("teachingPlanCourseService")
@Transactional
public class TeachingPlanCourseServiceImpl extends BaseServiceImpl<TeachingPlanCourse> implements ITeachingPlanCourseService {

	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;//JDBC 支持

	@Autowired
	@Qualifier("studentLearnPlanService")
	private IStudentLearnPlanService studentLearnPlanService;//注入学习计划服务
	
	@Override
	@Transactional(readOnly=true)
	public Page findTeachingPlanCourseByCondition(Map<String, Object> condition, Page objPage) throws ServiceException{
		Map<String,Object> values =  new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer(" from "+TeachingPlanCourse.class.getSimpleName()+" g where g.isDeleted = :isDeleted ");
		values.put("isDeleted", 0);		
		
		if(condition.containsKey("teachingPlanId")){//教学计划
			sb.append(" and g.teachingPlan.resourceid = :teachingPlanId ");
			values.put("teachingPlanId", condition.get("teachingPlanId"));
		}
		if(condition.containsKey("courseId")){//课程
			sb.append(" and g.course.resourceid = :courseId ");
			values.put("courseId", condition.get("courseId"));
		}		
		if(condition.containsKey("teachType")){//教学方式
			sb.append(" and g.teachType =:teachType ");
			values.put("teachType", condition.get("teachType"));
		}
		if (condition.containsKey("beforeTerm")) {
			sb.append(" and cast( g.term as int ) <= :beforeTerm ");
			values.put("beforeTerm", condition.get("beforeTerm"));
		}
		sb.append(" order by "+objPage.getOrderBy() +" "+ objPage.getOrder());
		return exGeneralHibernateDao.findByHql(objPage, sb.toString(), values);
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<TeachingPlanCourse> findTeachingPlanCourse(Map<String, Object> condition) throws ServiceException{
		Map<String,Object> values = new HashMap<String,Object>();
		StringBuffer sb = new StringBuffer("select pc.*,c.coursecode,c.coursename from edu_teach_plancourse pc ");
		sb.append(" join edu_teach_plan p on p.isdeleted = 0 and  p.resourceid = pc.planid ");
		sb.append(" join edu_base_course cs on cs.isdeleted = 0 and cs.resourceid = pc.courseid ");
		sb.append(" join edu_base_major m on m.isdeleted = 0 and m.resourceid = p.majorid ");
		sb.append(" join edu_base_classic c on c.isdeleted = 0 and c.resourceid  = p.classicid ");
		sb.append(" join edu_base_course c on c.resourceid = pc.courseid and c.isdeleted = 0");
		sb.append(" left join edu_teach_guiplan gp on gp.isdeleted = 0 and gp.planid = p.resourceid ");
		sb.append(" join edu_base_grade g on g.isdeleted = 0 and g.resourceid = gp.gradeid ");
		sb.append(" left join edu_roll_classes cl on cl.gradeid=g.resourceid and cl.classicid=c.resourceid and cl.majorid=m.resourceid");
		sb.append(" where pc.isdeleted = 0");
		if(condition.containsKey("gradeid")){
			sb.append(" and g.resourceid =:gradeid");
			values.put("gradeid", condition.get("gradeid"));
		}
		if(condition.containsKey("course")){
			sb.append(" and cs.resourceid =:course");
			values.put("course", condition.get("course"));
		}
		if(condition.containsKey("majorName")){
			sb.append(" and m.majorname =:majorName");
			values.put("majorName", condition.get("majorName"));
		}
		if(condition.containsKey("majorid")){
			sb.append(" and m.resourceid =:majorid");
			values.put("majorid", condition.get("majorid"));
		}
		if(condition.containsKey("classicName")){
			sb.append(" and c.classicname =:classicName");
			values.put("classicName", condition.get("classicName"));
		}
		if (condition.containsKey("classesid")) {
			sb.append(" and cl.resourceid =:classesid");
			values.put("classesid", condition.get("classesid"));
		}
		List<TeachingPlanCourse> tPlanCourses = null;
		try {
			tPlanCourses =  baseSupportJdbcDao.getBaseJdbcTemplate().findList(sb.toString(), TeachingPlanCourse.class, values);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tPlanCourses;
	}

	/**
	 * 成绩计划内/计划外 混打或单打
	 */
	public List<StudentExamResultsVo> printOutOrInExam(List<StudentExamResultsVo> list, List<String> tempList, StudentInfo stu, String degreeUnitExam){
		List<StudentExamResultsVo> printList = new ArrayList<StudentExamResultsVo>(0);
		if(null != tempList && tempList.size() > 0){ //开始过滤
			if(tempList.contains("-1")){//-1为打印计划外课程成绩
				if(tempList.size() > 1){
					printList = getOutOrInExam(list,"all",stu,tempList,degreeUnitExam); //打印混合
				}else{
					printList = getOutOrInExam(list,"out",stu,tempList,degreeUnitExam); //打印计划内外
				}
			}else{
				printList = getOutOrInExam(list,"in",stu,tempList,degreeUnitExam); //打印计划内
			}


		}else{ //无过滤  20160527 修改为：默认只打印计划内
			printList = getOutOrInExam(list,"in",stu,tempList,degreeUnitExam);
		}
		return printList;
	}


	/**
	 * 取出计划外 or 计划内成绩
	 * @return
	 */
	private List<StudentExamResultsVo> getOutOrInExam(List<StudentExamResultsVo> list, String type, StudentInfo stu, List<String> tempList, String degreeUnitExam){
		List<StudentLearnPlan> slpList = new ArrayList<StudentLearnPlan>(0);
		List<StudentExamResultsVo> rtIntList = new ArrayList<StudentExamResultsVo>(0); //计划内
		List<StudentExamResultsVo> rtOutList = new ArrayList<StudentExamResultsVo>(0); //计划外
		List<StudentExamResultsVo> rtList = new ArrayList<StudentExamResultsVo>(0);
		try {
			/*slpList = studentLearnPlanService.getStudentLearnPlanListBySql(stu.getResourceid(), null);
			List<StudentLearnPlan> _planOutlearnPlanList = new ArrayList<StudentLearnPlan>();
			if(null !=slpList && !slpList.isEmpty()){
				for(StudentLearnPlan plan : slpList){
					if(ExStringUtils.isNotBlank(plan.getPlanOutcourseId()) && ExStringUtils.isBlank(plan.getPlanSourceId())){
						_planOutlearnPlanList.add(plan);
					}
				}
			}*/

			for(StudentExamResultsVo vo : list){
				/*if (null != _planOutlearnPlanList && _planOutlearnPlanList.size() > 0) {
					for (StudentLearnPlan plan : _planOutlearnPlanList) {
						if (vo.getCourseId().equals(plan.getCourseId())) {
							vo.setCourseTerm(vo.getCourseTerm() + "(外)");
							rtOutList.add(vo);
						}
					}
				}*/
				if("Y".equals(vo.getIsOutplancourse())){
					vo.setCourseTerm(vo.getCourseTerm() + "(外)");
					rtOutList.add(vo);
				}
			}

			list.removeAll(rtOutList);
			rtIntList = list;

		} catch (Exception e) {
			e.printStackTrace();
		}

		//过滤
		if("in".equals(type)){ //返回计划内成绩
			rtIntList = getTermList(rtIntList,tempList);
		}else if("out".equals(type)){ //返回计划外成绩
			rtIntList = rtOutList;
		}else{//混打
			rtList = getTermList(list,tempList);
			rtList.addAll(rtOutList);//返回混合成绩
			rtIntList = rtList;
		}
		if("Y".equals(degreeUnitExam)){//在学期的条件下添加学位外语
			for(StudentExamResultsVo stuplan : list){
				if(Constants.BOOLEAN_YES.equals(stuplan.getIsDegreeUnitExam())){
					if(rtIntList.contains(stuplan)){
						break;
					}else{
						rtIntList.add(0, stuplan);
					}
				}
			}
		}
		return rtIntList;
	}

	/**
	 * 按学期过滤成绩
	 * @param list
	 * @param tempList
	 * @return
	 */
	private List<StudentExamResultsVo> getTermList(List<StudentExamResultsVo> list, List<String> tempList){
		List<StudentExamResultsVo> rtlist = new ArrayList<StudentExamResultsVo>(0);
		try {
			if(null != tempList && tempList.size() > 0){
				for(StudentExamResultsVo vo : list){
					if(tempList.contains(vo.getCourseTerm())){//判断要导出的数组里是否有这个学期
						rtlist.add(vo);
					}else{
						continue;
					}
				}
			}else{
				return list;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rtlist;
	}

	@Override
	public String constructOptions(Map<String, Object> condition, String defaultValue) throws Exception{
		StringBuilder courseOption = new StringBuilder();
		List<TeachingPlanCourse> planCourseList = findTeachingPlanCourse(condition);
		if(null != planCourseList && planCourseList.size()>0){
			for(TeachingPlanCourse planCourse : planCourseList){
				if(ExStringUtils.isNotEmpty(defaultValue)&&defaultValue.equals(planCourse.getResourceid())){
					courseOption.append("<option selected='selected' value='"+planCourse.getResourceid()+"'");
					courseOption.append(">"+planCourse.getCourseCode()+"-"+planCourse.getCourseName()+"</option>");
				}else {
					courseOption.append("<option value='"+planCourse.getResourceid()+"'");
					courseOption.append(">"+planCourse.getCourseCode()+"-"+planCourse.getCourseName()+"</option>");
				}
			}
		}
		return courseOption.toString();
	}
}
