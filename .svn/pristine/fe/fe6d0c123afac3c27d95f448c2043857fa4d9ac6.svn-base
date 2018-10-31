package com.hnjk.edu.teaching.service.impl;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.teaching.model.TeachingGuidePlan;
import com.hnjk.edu.teaching.model.TeachingPlan;
import com.hnjk.edu.teaching.model.TeachingPlanCourse;
import com.hnjk.edu.teaching.service.ICourseService;
import com.hnjk.edu.teaching.service.ITeachingGuidePlanService;
import com.hnjk.edu.teaching.service.ITeachingPlanCourseService;
import com.hnjk.edu.teaching.service.ITeachingPlanService;
import com.hnjk.platform.taglib.JstlCustomFunction;
import org.hibernate.NonUniqueResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;

/**
 * 年度指导性教学计划表
 * <code>TeachingGuidePlanServiceImpl</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2010-6-25 下午03:44:07
 * @see 
 * @version 1.0
 */
@Transactional
@Service("teachingguideplanservice")
public class TeachingGuidePlanServiceImpl extends BaseServiceImpl<TeachingGuidePlan> implements ITeachingGuidePlanService {

	@Autowired
	@Qualifier("teachingplanservice")
	private ITeachingPlanService teachingPlanService;
	
	@Autowired
	@Qualifier("courseService")
	private ICourseService courseService;
	
	@Autowired
	@Qualifier("teachingPlanCourseService")
	private ITeachingPlanCourseService teachingPlanCourseService;
	
	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;
	
//	@Autowired
//	@Qualifier("stateExamCourseService")
//	private IStateExamCourseService stateExamCourseService;//注入统考课程服务
	
	/*
	 * 分页查找
	 * (non-Javadoc)
	 * @see com.hnjk.edu.teaching.service.ITeachingGuidePlanService#findTeachingGradeByCondition(java.util.Map, com.hnjk.core.rao.dao.helper.Page)
	 */
	@Override
	@Transactional(readOnly=true)
	public Page findTeachingGradeByCondition(Map<String, Object> condition, Page objPage) {
		Map<String,Object> values =  new HashMap<String, Object>();
		String hql = " from "+TeachingGuidePlan.class.getSimpleName()+" gp where 1=1 ";
		hql += " and gp.isDeleted = :isDeleted and gp.grade.isDeleted = 0 ";
		values.put("isDeleted", 0);
		
		if(condition.containsKey("gradeid")){
			hql += " and gp.grade.resourceid = :gradeid ";
			values.put("gradeid", condition.get("gradeid"));
		}
		if(condition.containsKey("ispublished")){
			hql += " and gp.ispublished = :ispublished ";
			values.put("ispublished", condition.get("ispublished"));
		}
		if(condition.containsKey("classic")){
			hql += " and gp.teachingPlan.classic.resourceid = :classic ";
			values.put("classic", condition.get("classic"));
		}
		if(condition.containsKey("major")){
			hql += " and gp.teachingPlan.major.resourceid = :major ";
			values.put("major", condition.get("major"));
		}
		if(condition.containsKey("plan")){
			hql += " and gp.teachingPlan.resourceid = :plan ";
			values.put("plan", condition.get("plan"));
		}
		if(condition.containsKey("roleModules")){
			hql += " and gp.teachingPlan.schoolType in (:roleModules) ";
			values.put("roleModules", condition.get("roleModules"));
		}
		if(condition.containsKey("schoolType")){
			hql += " and gp.teachingPlan.schoolType =:schoolType ";
			values.put("schoolType", condition.get("schoolType"));
		}
		if(condition.containsKey("brSchoolid")){
			hql += " and (gp.teachingPlan.orgUnit.resourceid =:brSchoolid or gp.teachingPlan.orgUnit is null) ";
			values.put("brSchoolid", condition.get("brSchoolid"));
		}
		if (condition.containsKey("branchSchool")) {
			values.put("branchSchool", condition.get("branchSchool"));
			hql += " and exists ( select resourceid from "+StudentInfo.class.getSimpleName()+" stu where stu.isDeleted=:isDeleted and stu.branchSchool.resourceid=:branchSchool and gp.teachingPlan = stu.teachingPlan ";
			if (condition.containsKey("gradeid")) {
				hql+=" and stu.grade.resourceid = :gradeid";
			}
			if (condition.containsKey("classic")) {
				hql+=" and stu.classic.resourceid = :classic";
			}
			if (condition.containsKey("major")) {
				hql+=" and stu.major.resourceid = :major";
			}
			hql+=" ) ";
		}
		hql += " order by "+objPage.getOrderBy() +" "+ objPage.getOrder();
		return exGeneralHibernateDao.findByHql(objPage, hql, values);
	}

	/*
	 * 删除
	 * (non-Javadoc)
	 * @see com.hnjk.core.support.base.service.BaseServiceImpl#delete(java.io.Serializable)
	 */
	@Override
	public void delete(Serializable id) throws ServiceException {
		TeachingGuidePlan teachingGuidePlan = get(id);
		if(teachingGuidePlan.getIspublished().equals(Constants.BOOLEAN_YES)){
			throw new ServiceException(teachingGuidePlan.getTeachingPlan().getMajor().getMajorName()+" - "+teachingGuidePlan.getTeachingPlan().getClassic().getClassicName()+"已发布，不能删除!");
		}
		super.delete(teachingGuidePlan);
	}
	
	/*
	 * 批量删除
	 * (non-Javadoc)
	 * @see com.hnjk.edu.teaching.service.ITeachingGuidePlanService#batchCascadeDelete(java.lang.String[])
	 */
	@Override
	public void batchCascadeDelete(String[] ids) throws ServiceException {
		if(ids!=null && ids.length>0){
			for(String id : ids){				
				delete(id);					
			}
		}
	}
	
	
	/*
	 * 获取年级教学计划中第一学期课程名
	 * (non-Javadoc)
	 * @see com.hnjk.edu.teaching.service.ITeachingGuidePlanService#getGuideTeahingFirstTermCourseNameAndID(java.util.Map)
	 */
	@Override
	@Transactional(readOnly=true)
	public Map<String, Object> getGuideTeahingFirstTermCourseNameAndID(Map<String, Object> condition) {
		StringBuffer alertMsg     = new StringBuffer();
		StringBuffer guidePlanHQL = new StringBuffer();
		guidePlanHQL.append("from TeachingGuidePlan guidePlan where  guidePlan.isDeleted = ?" )
					.append(" and guidePlan.grade.resourceid=?")
					.append(" and guidePlan.teachingPlan.major.resourceid=? " )
					.append(" and guidePlan.teachingPlan.classic.resourceid=?")
					.append(" and guidePlan.teachingPlan.schoolType=?")
					.append(" and guidePlan.ispublished=?");
		
		if (condition.containsKey("branchSchool")){ 
			guidePlanHQL.append(" and  guidePlan.teachingPlan.orgUnit.resourceid='"+condition.get("branchSchool")+"'");		
		}else {
			guidePlanHQL.append(" and  guidePlan.teachingPlan.orgUnit is null ");		
		}
		String gradeid     =condition.get("gradeid").toString();
		String majorid     =condition.get("majorid").toString();
		String classic     =condition.get("classic").toString();
		String schoolType  =condition.get("schoolType").toString();
		
		try {
			
			TeachingGuidePlan guidePlan = findUnique(guidePlanHQL.toString(),0,gradeid,majorid,classic,schoolType,Constants.BOOLEAN_YES);
	 		
			int i = 1;
			
			if (null!=guidePlan&&null!=guidePlan.getTeachingPlan()) {
				TeachingPlan teachingPlan = guidePlan.getTeachingPlan();
				alertMsg.append("<font color='blue'>年级：</font>"+guidePlan.getGrade().getGradeName()+"<br/>")
					   .append("<font color='blue'>层次：</font>"+teachingPlan.getClassic().getClassicName()+"<br/>")
					   .append("<font color='blue'>专业：</font>"+teachingPlan.getMajor().getMajorName()+"<br/>")
					   .append("<font color='blue'>学习模式：</font>"+JstlCustomFunction.dictionaryCode2Value("CodeTeachingType", teachingPlan.getSchoolType())+"<br/>");
					   if (null!=teachingPlan.getOrgUnit()) {
						   alertMsg.append("<font color='blue'>学习中心：</font>"+teachingPlan.getOrgUnit().getUnitShortName()+"<br/>");
					   }else {
						   alertMsg.append("<font color='blue'>教学计划版本：</font>"+teachingPlan.getVersionNum()+"<br/>");
					   }
					   alertMsg.append("<br/>").append("<font color='blue'>第一学期课程有：</font><br/>");
				Set<TeachingPlanCourse> teachingPlanCourse=teachingPlan.getTeachingPlanCourses();
				for (TeachingPlanCourse course:teachingPlanCourse) {
					if ("1".equals(course.getTerm())) {
						alertMsg.append(i+"、"+course.getCourse().getCourseName()+"<br/>");
						i++;
					}
				}
				condition.put("teachingPlanId",guidePlan.getResourceid());
			}else {
				if (!condition.containsKey("branchSchool")) {
					alertMsg.append("您所选的年级、专业、层次没有对应的<font color='red'>公共教学计划</font>，请与教务管理人员联系！");
				}else {
					alertMsg.append("您所选的<font color='red'>年级、专业、层次、办学模式、学习中心</font>没有对应的教学计划，请与教务管理人员联系！");
				}
			}
		} catch (NonUniqueResultException e) {
			alertMsg.append("您所选的查询条件下有多个教学计划，如果需要预约的第一学期课程为学习中心的教学计划请选择相应的学习中心点击查询！");
		}

		condition.put("alertMsg", alertMsg);

		return condition;
	}
	
	/*
	 * 获取年级教学计划
	 * (non-Javadoc)
	 * @see com.hnjk.edu.teaching.service.ITeachingGuidePlanService#getGuideTeachingPlanInfo(java.util.Map)
	 */
	@Override
	@Transactional(readOnly=true)
	public Map<String, Object> getGuideTeachingPlanInfo(Map<String, Object> condition) {
		
		StringBuffer guidePlanHQL = new StringBuffer();
		StringBuffer alertMsg  	  = new StringBuffer();
		guidePlanHQL.append(" from TeachingGuidePlan guidePlan where guidePlan.isDeleted='0' and guidePlan.grade.resourceid=?")
					.append(" and guidePlan.teachingPlan.major.resourceid=? ")
					.append(" and guidePlan.teachingPlan.classic.resourceid=? ")
					.append(" and guidePlan.teachingPlan.schoolType=?")
					.append(" and guidePlan.ispublished=?");
								
		//传入学习中心查指定中心的计划
		if (condition.containsKey("branchSchool")) {
			guidePlanHQL.append(" and  guidePlan.teachingPlan.orgUnit.resourceid='"+condition.get("branchSchool")+"'");
		//未选择学习中心查公共计划
		}else {
			guidePlanHQL.append(" and guidePlan.teachingPlan.orgUnit is null ");
		}
		
					  
		String gradeid   =condition.get("gradeid").toString();
		String majorid   =condition.get("major").toString();
		String classic   =condition.get("classic").toString();
		String schoolType=condition.get("schoolType").toString();
		
		try {
			
			TeachingGuidePlan guidePlan = findUnique(guidePlanHQL.toString(), new String[]{gradeid,majorid,classic,schoolType,Constants.BOOLEAN_YES});
			
			if (null!=guidePlan&&null!=guidePlan.getTeachingPlan()) {
				alertMsg.append("<strong><font color='blue'>(一)、教学计划名称：</font></strong>"+guidePlan.getTeachingPlan().getMajor().getMajorName()+"-"+guidePlan.getTeachingPlan().getClassic().getClassicName());
			       if (null!=guidePlan.getTeachingPlan().getOrgUnit()) {
			    	   alertMsg.append("("+guidePlan.getTeachingPlan().getOrgUnit().getUnitShortName()+")");
				   }else {
					   alertMsg.append("("+guidePlan.getTeachingPlan().getVersionNum()+")");
				   }
				alertMsg.append("<br / >")
			   		    .append("<strong><font color='blue'>(二)、学制：</font></strong>"+guidePlan.getTeachingPlan().getEduYear()+"<br / >")
			   		    .append("<strong><font color='blue'>(三)、层次：</font></strong>"+guidePlan.getTeachingPlan().getClassic().getClassicName()+"<br / >")
			   		    .append("<strong><font color='blue'>(四)、办学模式：</font></strong>"+JstlCustomFunction.dictionaryCode2Value("CodeTeachingType", guidePlan.getTeachingPlan().getSchoolType())+"<br / >")
			   		    .append("<strong><font color='blue'>(五)、主干课程：</font></strong>"+guidePlan.getTeachingPlan().getMainCourse()+"<br / >");
				       
				condition.put("teachingPlanId",guidePlan.getTeachingPlan().getResourceid());
			}else {
				if (!condition.containsKey("branchSchool")) {
					alertMsg.append("您所选的年级、专业、层次没有对应的<font color='red'>公共教学计划</font>，请与教务管理人员联系！");
				}else {
					alertMsg.append("您所选的<font color='red'>年级、专业、层次、办学模式、学习中心</font>没有对应的教学计划，请与教务管理人员联系！");
				}
			}
		} catch (NonUniqueResultException e) {
			alertMsg.append("您所选的查询条件下有多个教学计划，如果需要补录学习中心的教学计划请选择相应的学习中心点击查询！");
		}
		
		condition.put("alertMsg",alertMsg );
		return condition;
	}
	
	/*
	 * 获取全部年级教学计划
	 * (non-Javadoc)
	 * @see com.hnjk.edu.teaching.service.ITeachingGuidePlanService#findAlTeachingGradePlan()
	 */
	@Override
	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public List<TeachingGuidePlan> findAlTeachingGradePlan() {
		String hql = "from TeachingGuidePlan guidePlan where guidePlan.isDeleted=0";
		return (List<TeachingGuidePlan>) exGeneralHibernateDao.findByHql(hql);
	}

	/*
	 * 审核发布年级教学计划
	 * (non-Javadoc)
	 * @see com.hnjk.edu.teaching.service.ITeachingGuidePlanService#publishGuideTeachingPlan(com.hnjk.edu.teaching.model.TeachingGuidePlan)
	 */
	@Override
	public void publishGuideTeachingPlan(TeachingGuidePlan teachingGuidePlan) throws ServiceException {
		//更新
		update(teachingGuidePlan);
		//更新教学计划可以标示
		TeachingPlan teachingPlan = teachingPlanService.get(teachingGuidePlan.getTeachingPlan().getResourceid());
		if(teachingGuidePlan.getIspublished().equals(Constants.BOOLEAN_YES)){
			teachingPlan.setIsUsed(Constants.BOOLEAN_YES);
		}else{
			teachingPlan.setIsUsed(Constants.BOOLEAN_NO);
		}
		
		teachingPlanService.update(teachingPlan);
	}

	@Override
	public Page findTeachingGradeWithUnitByCondition(Map<String, Object> condition, Page objPage) {
		StringBuffer hql = new StringBuffer(" select gp.resourceid,u.unitname,g.gradename,ci.classicname,lq.teachingtype,m.majorname,p.planname,gp.planid,p.eduyear,gp.ispublished,c.coursename,p.versionnum");
		hql.append(",count(si.resourceid) stuNumber");
		hql.append(" from edu_teach_linkagequery lq join hnjk_sys_unit u on u.resourceid=lq.brschoolid and u.isdeleted=0");
		hql.append(" join edu_base_grade g on g.resourceid=lq.gradeid and g.isdeleted=0");
		hql.append(" join edu_base_classic ci on ci.resourceid=lq.classicid and ci.isdeleted=0");
		hql.append(" join edu_base_major m on m.resourceid=lq.majorid and m.isdeleted=0 ");
		hql.append(" join edu_teach_guiplan gp on gp.gradeid=lq.gradeid and gp.isdeleted=0");
		hql.append(" join edu_teach_plan p on p.resourceid=gp.planid and p.classicid=lq.classicid and p.schooltype=lq.teachingtype and p.majorid=lq.majorid and p.isused='Y' and p.isdeleted=0");
		//hql.append(" join edu_teach_plan p on p.resourceid=gp.planid and p.classicid=lq.classicid and p.schooltype=lq.teachingtype and p.majorid=lq.majorid and p.isdeleted=0");
		hql.append(" left join edu_base_course c on c.resourceid=gp.degreeforeignlanguage");
		hql.append(" ,edu_roll_studentinfo si where lq.isdeleted=0 and si.studentStatus in('11','21','25') and si.majorid=m.resourceid and si.gradeid=g.resourceid and si.branchSchoolid=u.resourceid and si.classicid=ci.resourceid");
		List<String> parameters = new ArrayList<String>();
		if(condition.containsKey("brSchoolid")){
			hql.append(" and u.resourceid=:brSchoolid");
			parameters.add(condition.get("brSchoolid").toString());
		}
		if(condition.containsKey("gradeid")){
			hql.append(" and g.resourceid=:gradeid");
			parameters.add(condition.get("gradeid").toString());
		}
		if(condition.containsKey("classic")){
			hql.append(" and ci.resourceid=:classic");
			parameters.add(condition.get("classic").toString());
		}
		if(condition.containsKey("major")){
			hql.append(" and m.resourceid=:major");
			parameters.add(condition.get("major").toString());
		}
		if(condition.containsKey("schoolType")){
			hql.append(" and lq.teachingtype=:schoolType");
			parameters.add(condition.get("schoolType").toString());
		}
		if(condition.containsKey("ispublished")){
			hql.append(" and gp.ispublished=:ispublished");
			parameters.add(condition.get("ispublished").toString());
		}
		if(condition.containsKey("plan")){
			hql.append(" and p.resourceid=:plan");
			parameters.add(condition.get("plan").toString());
		}
		hql.append(" group by gp.resourceid,u.unitname,g.gradename,ci.classicname,lq.teachingtype,m.majorname,p.planname,gp.planid,p.eduyear,gp.ispublished,c.coursename,p.versionnum");
		hql.append(" order by u.unitname,g.gradename,lq.teachingtype,ci.classicname,m.majorname");
		
		return baseSupportJdbcDao.getBaseJdbcTemplate().findListMap(objPage, hql.toString(), parameters.toArray());
	}
	
	@Transactional(readOnly=true)
	@Override
	public Page findGuiPlanByCondition(Map<String, Object> condition, Page objPage) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select gp.resourceid,g.gradename,ci.classicname,m.majorname,p.schooltype,p.planname,gp.planid,p.eduyear,gp.ispublished,c.coursename,p.versionnum from edu_teach_guiplan gp ");
		sql.append(" join edu_teach_plan p on p.resourceid=gp.planid and p.isdeleted=0");
		sql.append(" join edu_base_grade g on g.resourceid=gp.gradeid and g.isdeleted=0");
		sql.append(" join edu_base_classic ci on ci.resourceid=p.classicid and ci.isdeleted=0");
		sql.append(" join edu_base_major m on m.resourceid=p.majorid and m.isdeleted=0 ");
		sql.append(" left join edu_base_course c on c.resourceid=gp.degreeforeignlanguage and c.isdeleted=0");
		sql.append(" where gp.isdeleted=0 ");
		
		List<String> parameters = new ArrayList<String>();
		if(condition.containsKey("gradeid")){
			sql.append( " and g.resourceid=:gradeid");
			parameters.add(condition.get("gradeid").toString());
		}
		if(condition.containsKey("classic")){
			sql.append( " and ci.resourceid=:classic");
			parameters.add(condition.get("classic").toString());
		}
		if(condition.containsKey("major")){
			sql.append( " and m.resourceid=:major");
			parameters.add(condition.get("major").toString());
		}
		if(condition.containsKey("schoolType")){
			sql.append( " and p.schooltype=:schoolType");
			parameters.add(condition.get("schoolType").toString());
		}
		if(condition.containsKey("ispublished")){
			sql.append( " and gp.ispublished=:ispublished");
			parameters.add(condition.get("ispublished").toString());
		}
		if(condition.containsKey("plan")){
			sql.append( " and p.resourceid=:plan");
			parameters.add(condition.get("plan").toString());
		}
		sql.append( " order by g.gradename,p.schooltype,ci.classicname,m.majorname");
		
		return baseSupportJdbcDao.getBaseJdbcTemplate().findListMap(objPage, sql.toString(), parameters.toArray());
	}

	/*
	 * 更新统考课程对应表
	 * (non-Javadoc)
	 * @see com.hnjk.edu.teaching.service.ITeachingGuidePlanService#updateStateCourse(java.lang.String, java.lang.String[])
	 */
	/*
	public void updateStateCourse(String teachingGuiPlanId, Map<String, String> stateCourseMap) throws ServiceException {
		TeachingGuidePlan teachingGuidePlan = get(teachingGuiPlanId);
		//先删除对应表
		teachingPlanService.truncateByProperty(StateExamCourse.class, "teachingGuidePlan.resourceid", teachingGuidePlan.getResourceid());
		//遍历统考课程表，新增对应记录
		StateExamCourse stateExamCourse = null;
		if(null != stateCourseMap && !stateCourseMap.isEmpty()){
			for(Map.Entry<String, String> entry : stateCourseMap.entrySet()){
				if(ExStringUtils.isNotEmpty(entry.getKey().toString()) && ExStringUtils.isNotEmpty(entry.getValue().toString())){
					stateExamCourse = new StateExamCourse();
					stateExamCourse.setTeachingGuidePlan(teachingGuidePlan);
					stateExamCourse.setTeachingPlanCourse(teachingPlanCourseService.get(entry.getKey().toString()));
					stateExamCourse.setStateExamCourse(courseService.get(entry.getValue().toString()));	
					stateExamCourseService.saveOrUpdate(stateExamCourse);
				}
				
			}
		}	
	
	}	
	*/
	
}
