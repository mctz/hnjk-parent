package com.hnjk.edu.learning.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.learning.model.Exercise;
import com.hnjk.edu.learning.model.ExerciseBatch;
import com.hnjk.edu.learning.service.IExerciseBatchService;
import com.hnjk.edu.learning.service.IStudentExerciseService;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.teaching.model.TeachTask;
import com.hnjk.edu.teaching.model.TeachingPlanCourseTimetable;
import com.hnjk.platform.system.service.IAttachsService;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;

/**
 * 课后作业批次管理服务接口实现
 * <code>ExerciseBatchServiceImpl</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-8-6 上午11:30:44
 * @see 
 * @version 1.0
 */
@Transactional
@Service("exerciseBatchService")
public class ExerciseBatchServiceImpl extends BaseServiceImpl<ExerciseBatch> implements IExerciseBatchService {
	
	@Autowired
	@Qualifier("attachsService")
	private IAttachsService attachsService;
	
	@Autowired
	@Qualifier("studentExerciseService")
	private IStudentExerciseService studentExerciseService;
	
	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;         //JDBC服务 
	
	@Override
	@Transactional(readOnly=true)
	public Page findExerciseBatchByCondition(Map<String, Object> condition, Page objPage) throws ServiceException {
		Map<String,Object> values =  new HashMap<String, Object>();
		StringBuilder hql = getExerciseBatchHqlByCondition(condition, values);		
		if(ExStringUtils.isNotEmpty(objPage.getOrderBy())){			
			hql.append(" order by c."+objPage.getOrderBy().replace(",", ",c.") +" "+ objPage.getOrder());
		}
		return findByHql(objPage, hql.toString(), values);
	}

	@Override
	public List<ExerciseBatch> findExerciseBatchByCondition(Map<String, Object> condition) throws ServiceException {
		Map<String,Object> values =  new HashMap<String, Object>();
		StringBuilder hql = getExerciseBatchHqlByCondition(condition, values);			
		if(condition.containsKey("orderby")){			
			hql.append(" order by "+condition.get("orderby"));
		}
		return findByHql(hql.toString(), values);
	}
	
	/**
	 * 获取hql
	 * @param condition
	 * @param values
	 * @return
	 */
	private StringBuilder getExerciseBatchHqlByCondition(Map<String, Object> condition, Map<String, Object> values) {
		StringBuilder hql = new StringBuilder(" select c from "+ExerciseBatch.class.getSimpleName()+" c where c.isDeleted = :isDeleted ");
		values.put("isDeleted", 0);
		
		if(condition.containsKey("colName")){//批次名称
			hql.append(" and lower(c.colName) like lower(:colName) ");
			values.put("colName", "%"+condition.get("colName")+"%");
		}
		if(condition.containsKey("colType")){//作业类型
			hql.append(" and c.colType =:colType ");
			values.put("colType", condition.get("colType"));
		}
		if(condition.containsKey("status")){//作业批次状态
			hql.append(" and c.status =:status ");
			values.put("status", Integer.parseInt(condition.get("status").toString()));
		}
		if(condition.containsKey("notstatus")){//
			hql.append(" and c.status <>:notstatus ");
			values.put("notstatus", Integer.parseInt(condition.get("notstatus").toString()));
		}
		if(condition.containsKey("courseId")){//所属课程
			hql.append(" and c.course.resourceid =:courseId ");
			values.put("courseId", condition.get("courseId"));
		}
		if(condition.containsKey("courseName")){//所属课程名称 
			hql.append(" and lower(c.course.courseName) like lower(:courseName) ");
			values.put("courseName", "%"+condition.get("courseName")+"%");
		}
		if(condition.containsKey("yearInfoId")){//年度
			hql.append(" and c.yearInfo.resourceid =:yearInfoId ");
			values.put("yearInfoId", condition.get("yearInfoId"));
		}		
		if(condition.containsKey("term")){//学期
			hql.append(" and c.term =:term ");
			values.put("term", condition.get("term"));
		}
		if(condition.containsKey("fillinManId")){// 老师
			hql.append(" and (c.fillinManId =:fillinManId or c.classesIds is null)");
			values.put("fillinManId", condition.get("fillinManId"));
		}
		if(condition.containsKey("classesId")){//班级
			hql.append(" and ((c.classesIds is not null and c.classesIds like :classesId) or  c.classesIds is null) ");
			values.put("classesId", "'%"+condition.get("classesId")+"%'");
		} 
		if(condition.containsKey("studentClassesId")){// 学生
			hql.append(" and  ((c.classesIds is not null and c.classesIds like '%"+condition.get("studentClassesId")+"%') or c.classesIds is null)");
		}
		if(condition.containsKey("teacherId")){//课程老师
			/*hql.append(" and exists ( from "+TeachTask.class.getSimpleName()+" t where t.isDeleted=0 and t.taskStatus=3 and t.yearInfo.resourceid=c.yearInfo.resourceid and t.term=c.term and t.course.resourceid=c.course.resourceid and (t.teacherId like :teacherId  or t.assistantIds like :teacherId ) ) ");
			values.put("teacherId", "%"+condition.get("teacherId")+"%");//老师*/	
		}
		return hql;
	}
	@Override
	public void deleteExerciseBatch(String[] ids) throws ServiceException {
		if(ids!=null && ids.length>0){
			for(String id : ids){
				ExerciseBatch batch = get(id);
				if(batch.getStatus()!=0){
					throw new ServiceException("不能删除已发布或结束的作业！");
				}
				delete(batch);	
				logger.info("删除作业批次="+id);
			}
		}
	}
	
	@Override
	public void saveOrUpdateExerciseBatch(ExerciseBatch exerciseBatch, String[] attachIds) throws ServiceException {
		saveOrUpdate(exerciseBatch);
		User user = SpringSecurityHelper.getCurrentUser();
		attachsService.updateAttachByFormId(attachIds, exerciseBatch.getResourceid(), ExerciseBatch.class.getSimpleName(), user.getResourceid(), user.getCnName());		
	}

	/**
	 * 此方法增加了后置通知发送温馨提示 com.hnjk.edu.learning.aspect.LearningAspect.sendMsgAfterExercise()
	 */
	@Override
	public List<ExerciseBatch> statusExerciseBatch(String[] ids, Integer status) throws ServiceException {
		List<ExerciseBatch> list = new ArrayList<ExerciseBatch>();//不允许发布的作业
		if(ids!=null && ids.length>0){
			for(String id : ids){
				ExerciseBatch b = get(id);
				if(!b.getStatus().equals(status) && !(status==1 && b.getStatus()>1)){
					if(status==1 && b.getExercises().isEmpty()){
						throw new ServiceException("你还没有添加作业题目,不能发布!");
					} 
					if("1".equals(b.getColType())&&b.getObjectiveNum()!=null&&b.getObjectiveNum()!=b.getExercises().size()){
						throw new WebException("客观题题目数必须为<b>"+b.getObjectiveNum()+"</b>道，当前已添加"+b.getExercises().size()+"道题目,请继续添加作业题再发布");
					}					
					if(status==0){
						int count = studentExerciseService.countStudentFinished(id);
						if(count!=0){
							throw new ServiceException("已有学生提交作业,不能取消发布!");
						}
					}
					b.setStatus(status);
					b.setIsPublishOrUnpublish(true);
					
					update(b);
					list.add(b);
				}else {
					list.add(b);
				}
			}
		}
		return list;
	}

	@Override
	public void deleteExercise(String colsId, String[] ids) throws ServiceException {
		if(ids!=null && ids.length>0){
			ExerciseBatch batch = get(colsId);
			for (String id : ids) {
				Exercise exercise = (Exercise) exGeneralHibernateDao.get(Exercise.class, id);
				exGeneralHibernateDao.delete(exercise);
				
				batch.getExercises().remove(exercise);
			}			
			
			int showOrder = 1;
			int total = batch.getExercises().size();
			if(total>0){
				Double score = 100.0/total;
				for (Exercise exercise : batch.getExercises()) {
					exercise.setShowOrder(showOrder++);
					exercise.setScore(score);
				}
			}						
			update(batch);
		}
	}
	@Override
	public void saveSubjectiveExercise(ExerciseBatch exerciseBatch,Exercise exercise,String[] attachIds) throws ServiceException {
		exGeneralHibernateDao.saveOrUpdate(exercise.getCourseExam());
		exGeneralHibernateDao.saveOrUpdate(exercise);
		update(exerciseBatch);
		User user =SpringSecurityHelper.getCurrentUser();
		attachsService.updateAttachByFormId(attachIds, exercise.getResourceid(), Exercise.class.getSimpleName(), user.getResourceid(), user.getCnName());		
	}
	/**
	 * 查找需要批改的业(已提交人数大于已批改人数)
	 * @param param
	 * @return
	 * @throws Exception 
	 */
	@Override
	public List<Map<String,Object>> findNeedsCorrectExercise(Map<String,Object> param)throws Exception{
		
		StringBuffer sql 	   = new StringBuffer();
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("status0",0);
		map.put("status1",1);
		map.put("status2",2);
		map.put("status3",3);
		map.put("isDeleted",0);
	
		sql.append(" select * from ( ");
		sql.append(" 				   select t1.resourceid,t1.courseid,t1.colname,c.coursename,t1.coltype,y.yearname,t1.term,task.defaultteacherids,task.assistantids, ");
		sql.append(" 				      (select count(s1.resourceid)  from edu_lear_studentexercise s1 where s1.isdeleted=:isDeleted and s1.exerciseid is null and s1.status>:status0 and s1.exercisebatchid=t1.resourceid) handNum,");
		sql.append(" 				      (select count(s2.resourceid)  from edu_lear_studentexercise s2 where s2.isdeleted=:isDeleted and s2.exerciseid is null and s2.status=:status2 and s2.exercisebatchid=t1.resourceid) correctNum ");
		sql.append("  				from EDU_LEAR_EXERCISEBATCH t1  ");
		sql.append("  				inner join edu_base_course c on t1.courseid = c.resourceid ");
		sql.append("  				inner join edu_base_year y on t1.yearid = y.resourceid  ");
		sql.append("                left join edu_teach_teachtask task on t1.courseid = task.courseid and task.isdeleted = :isDeleted and task.taskstatus=:status3 and t1.yearid = task.yearid  and t1.term = task.term");
		
		sql.append("  				where t1.isdeleted =:isDeleted and t1.status>=:status1 ");
		
		if (param.containsKey("yearInfoId")) {
			sql.append(" and t1.yearid =:yearInfoId ");	
			map.put("yearInfoId",param.get("yearInfoId"));
		}
		if (param.containsKey("term")) {
			sql.append(" and t1.term =:term ");	
			map.put("term",param.get("term"));
		}
		if (param.containsKey("courseId")) {
			sql.append(" and t1.courseid = :courseId ");	
			map.put("courseId",param.get("courseId"));
		}
		if (param.containsKey("colType")) {
			sql.append(" and t1.coltype= = :colType ");	
			map.put("colType",param.get("colType"));
		}
		sql.append(" ) t2 ");
		sql.append(" where t2.handNum> t2.correctNum ");
		
		return baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql.toString(), map);
	}
	
	@Override
	public List<Map<String, Object>> findExerciseStatusCount(Map<String, Object> param) throws ServiceException {
		StringBuffer sql = new StringBuffer();
		sql.append(" select t.exercisebatchid,tt.term,c.courseid,tt.totalNum,sum(handNum) handNum, sum(correctNum) correctNum ");
		sql.append(" from edu_lear_exercisebatch c ");
		sql.append(" join ( select s.exercisebatchid, s.status, ");
		sql.append(" case when s.status > 0 then count(s.resourceid) else 0 end handNum, ");
		sql.append(" case when s.status = 2 then count(s.resourceid) else 0 end correctNum ");
		sql.append(" from edu_lear_studentexercise s join edu_roll_studentinfo i on i.resourceid=s.studentid and i.isdeleted=0 ");
		sql.append(" where s.isdeleted = 0 and s.exerciseid is null ");
		if(param.containsKey("unitId")){
			sql.append(" and i.branchschoolid = :unitId ");
		}
		if(param.containsKey("studentStatus")){
			sql.append(" and i.studentstatus in("+param.get("studentStatus")+") ");
		}
		sql.append(" group by s.exercisebatchid, s.status) t on t.exercisebatchid = c.resourceid ");
		sql.append(" join edu_base_year y on y.resourceid=c.yearid ");
		//查询学生总人数
		sql.append(" left join ( select cs.term,pc.courseid,count(*) totalNum from  edu_roll_studentinfo si ");
		sql.append(" join edu_base_major m on m.isdeleted=0 and m.resourceid=si.majorid "); 
		sql.append(" join edu_base_grade g on g.isdeleted=0 and g.resourceid=si.gradeid "); 
		sql.append(" join edu_roll_classes cl on cl.isdeleted=0 and si.classesid=cl.resourceid");
		sql.append(" join edu_teach_plancourse pc on pc.planid = si.teachplanid and pc.isdeleted=0 ");
		sql.append(" join edu_teach_guiplan gp on gp.planid = si.teachplanid and gp.gradeid = si.gradeid and gp.isdeleted = 0 ");
		sql.append(" join edu_teach_coursestatus cs on cs.guiplanid = gp.resourceid and cs.plancourseid = pc.resourceid and cs.schoolids = si.BRANCHSCHOOLID and cs.isdeleted = 0 and cs.teachtype = 'networkTeach' ");
		sql.append(" where si.isdeleted=0 and si.accoutstatus=1");
		if(param.containsKey("unitId")){
			sql.append(" and si.branchschoolid = :unitId ");
		}
		if(param.containsKey("studentStatus")){
			sql.append(" and si.studentstatus in("+param.get("studentStatus")+") ");
		}
		sql.append(" group by cs.term,pc.courseid ");
		sql.append(" ) tt on  tt.courseid=c.courseid and tt.term=y.firstyear||'_0'||c.term ");
		sql.append(" where c.resourceid in(:exercisebatchids) ");
		sql.append(" group by t.exercisebatchid,tt.term,tt.totalNum,c.courseid ");
		
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		try {
			list = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql.toString(), param);
		} catch (Exception e) {
			logger.error("获取作业已提交人数，已批改人数出错:{}", e.fillInStackTrace());
		}
		return list;
	}
	
	
	@Override
	public Page findonlineClasses(Map<String, Object> paramMap, Page objPage)
			throws ServiceException {
		StringBuffer hql = new StringBuffer();
		hql.append(" select te.classes.classname,te.classes.resourceid,te.classes.grade.gradeName,te.classes.classic.classicName,te.classes.major.majorName,te.classes.teachingType from "+TeachingPlanCourseTimetable.class.getSimpleName()+" te where te.isDeleted=0 ");
	
		if (paramMap.containsKey("teacherId")) {
			hql.append(" and te.teacherId = :teacherId ");
		}
		
		if (paramMap.containsKey("branchSchool")) {
			hql.append(" and te.classes.brSchool.resourceid = :branchSchool ");
		}
		
		if(paramMap.containsKey("courseId")) {
			hql.append(" and te.teachingPlanCourse.course.resourceid = :courseId ");
		}
		
		if(paramMap.containsKey("yearInfoTerm")) {
			hql.append(" and te.term like :yearInfoTerm ");
		}
		
		hql.append(" group by te.classes.classname,te.classes.resourceid,te.classes.grade.gradeName,te.classes.classic.classicName,te.classes.major.majorName,te.classes.teachingType,te.term ");
		
		hql.append(" order by te.term ");
		
		return this.exGeneralHibernateDao.findByHql(objPage, hql.toString(), paramMap);
	}
	
}
