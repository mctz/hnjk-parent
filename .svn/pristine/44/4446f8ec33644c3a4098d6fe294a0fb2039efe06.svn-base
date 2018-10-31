package com.hnjk.edu.teaching.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.learning.model.StudentLearnPlan;
import com.hnjk.edu.teaching.model.CourseOrderStat;
import com.hnjk.edu.teaching.model.TeachTask;
import com.hnjk.edu.teaching.model.TeachTaskDetails;
import com.hnjk.edu.teaching.service.ITeachTaskService;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;

/**
 * 教学计划 - 教学任务书.
 * <code>TeachTaskServiceImpl</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2010-7-8 下午03:47:58
 * @see 
 * @version 1.0
 */
@Transactional
@Service("teachtaskservice")
public class TeachTaskServiceImpl extends BaseServiceImpl<TeachTask> implements ITeachTaskService {
	
	@Override
	@Transactional(readOnly=true)
	public Page findTeachTaskByCondition(Map<String, Object> condition, Page objPage)throws ServiceException {
		Map<String,Object> values =  new HashMap<String, Object>();
		String hql = " from "+TeachTask.class.getSimpleName()+" where 1=1 ";
		hql += " and isDeleted = :isDeleted ";
		values.put("isDeleted", 0);
		
		if(condition.containsKey("yearInfoid")){
			hql += " and yearInfo.resourceid=:yearInfoid ";
			values.put("yearInfoid", condition.get("yearInfoid"));
		}
		if(condition.containsKey("term")){
			hql += " and term=:term ";
			values.put("term", condition.get("term"));
		}
		if(condition.containsKey("isTemplate")){
			if(Constants.BOOLEAN_NO.equals(condition.get("isTemplate").toString())){
				hql += " and (isTemplate is null or isTemplate=:isTemplate) ";
			} else {
				hql += " and isTemplate=:isTemplate ";
			}			
			values.put("isTemplate", condition.get("isTemplate"));
		}
		if(condition.containsKey("courseId")){
			hql += " and course.resourceid=:courseId ";
			values.put("courseId", condition.get("courseId"));
		}
		if(condition.containsKey("courseName")){
			hql += " and lower(course.courseName) like:courseName ";
			values.put("courseName", "%"+condition.get("courseName").toString().toLowerCase()+"%");
		}
		if(condition.containsKey("teacherName")){
			hql += " and teacherName like :teacherName ";
			values.put("teacherName", "%"+condition.get("teacherName").toString().toLowerCase()+"%");
		}
		if(condition.containsKey("assistantName")){
			hql += " and assistantNames like :assistantName ";
			values.put("assistantName", "%"+condition.get("assistantName").toString().toLowerCase()+"%");
		}
		if(condition.containsKey("teacherId")){
			hql += " and teacherId =:teacherId ";
			values.put("teacherId", condition.get("teacherId"));
		}
		if(condition.containsKey("taskStatus")){
			hql += " and taskStatus =:taskStatus ";
			values.put("taskStatus", Long.parseLong(condition.get("taskStatus").toString()));
		}
		if(condition.containsKey("gtTaskStatus")){
			hql += " and taskStatus >:gtTaskStatus ";
			values.put("gtTaskStatus", Long.parseLong(condition.get("gtTaskStatus").toString()));
		}
		if(condition.containsKey("teacher")){//老师id
			hql += " and (teacherId like :teacher or assistantIds like :teacher )  ";
			values.put("teacher", "%"+condition.get("teacher")+"%");
		}
		hql += " order by "+objPage.getOrderBy() +" "+ objPage.getOrder();
		return exGeneralHibernateDao.findByHql(objPage, hql, values);
	}

	@Override
	public void batchSend(int type, String[] ids)throws ServiceException {
		for(int index=0;index<ids.length;index++){
			TeachTask task = get(ids[index]);
			if(type==1){
				task.setTaskStatus(1L); //发送给课程负责人
			}
			if(type==2){
				task.setTaskStatus(2L); //负责人发回
				task.setRealReturnTime(new Date()); //实际返回时间
			}
			if(type==3){
				User user = SpringSecurityHelper.getCurrentUser();
				task.setTaskStatus(3L); //审核发布
				task.setAuditDate(new Date());
				task.setAuditMan(user.getCnName());
				task.setAuditManId(user.getResourceid());
			}
			update(task);
		}
	}
	
	

	@Override
	public void batchCascadeDelete(String[] ids)throws ServiceException {
		if(ids!=null && ids.length>0){
			for(String id : ids){
				TeachTask task = get(id);
				CourseOrderStat stat = exGeneralHibernateDao.findUnique(" from "+CourseOrderStat.class.getSimpleName()+" where isDeleted=0 and course.resourceid=? and yearInfo.resourceid=? and term=? ", task.getCourse().getResourceid(),task.getYearInfo().getResourceid(),task.getTerm());
				stat.setGeneratorFlag(Constants.BOOLEAN_NO);
				for (TeachTaskDetails d : task.getTeachTaskDetails()) {
					d.setIsDeleted(1);
				}
				delete(id);	
				logger.info("批量删除="+id);
			}
		}
	}

	@Override
	public void deleteDetail(String c_id)throws ServiceException {
		String[] ids = c_id.split(",");
		for(int index=0;index<ids.length;index++){
			exGeneralHibernateDao.delete(TeachTaskDetails.class, ids[index]);
		}
	}

	//type 0-不区分 1-负责老师 2-主讲老师 3-辅导老师
	@Override
	public boolean isCourseTeacher(String courseId, String teacherId, int type) throws ServiceException {
		String hql = "select count(t) from "+TeachTask.class.getName()+" t where t.isDeleted=0 and t.taskStatus=3 and t.course.resourceid=? ";
		switch (type) {
			case 1:
				hql += " and t.teacherId='"+teacherId+"' ";
				break;
			case 2:
				hql += " and t.defaultTeacherIds like '%"+teacherId+"%' ";
				break;
			case 3:
				hql += " and t.assistantIds like '%"+teacherId+"%' ";
				break;
			default:
				hql += " and (t.teacherId = '"+teacherId+"' or t.assistantIds like '%"+teacherId+"%') ";
				break;
		}	
		Long count = exGeneralHibernateDao.findUnique(hql, courseId);
		return count!=null&&count.intValue()>0?true:false;
	}

	@Override
	@Transactional(readOnly=true)
	public TeachTask findLastTeachTask(String courseId) throws ServiceException {
		List<TeachTask> list = findByHql(" from "+TeachTask.class.getSimpleName()+" where isDeleted=? and course.resourceid=? order by yearInfo.firstYear desc,term desc ", 0,courseId);
		return (list!=null && !list.isEmpty())?list.get(0):null;
	}
	
	@Override
	@Transactional(readOnly=true)
	public TeachTask findLastTeachTaskByTeacherId(String teacherId) throws ServiceException {
		List<TeachTask> list = findByHql(" from "+TeachTask.class.getSimpleName()+" where isDeleted=? and taskStatus=3 and (teacherId like ? or assistantIds like ? ) order by yearInfo.firstYear desc,term desc ", 0,"%"+teacherId+"%","%"+teacherId+"%");
		return (list!=null && !list.isEmpty())?list.get(0):null;
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<TeachTask> findTeachTaskByCondition(Map<String, Object> condition) throws ServiceException {
		Map<String,Object> values =  new HashMap<String, Object>();
		String hql = " from "+TeachTask.class.getSimpleName()+" t where 1=1 ";
		hql += " and t.isDeleted = :isDeleted ";
		values.put("isDeleted", 0);
		
		if(condition.containsKey("yearInfoid")){
			hql += " and t.yearInfo.resourceid=:yearInfoid ";
			values.put("yearInfoid", condition.get("yearInfoid"));
		}
		if(condition.containsKey("term")){
			hql += " and t.term=:term ";
			values.put("term", condition.get("term"));
		}
		if(condition.containsKey("courseId")){
			hql += " and t.course.resourceid=:courseId ";
			values.put("courseId", condition.get("courseId"));
		}
		if(condition.containsKey("taskStatus")){
			hql += " and t.taskStatus =:taskStatus ";
			values.put("taskStatus", Long.parseLong(condition.get("taskStatus").toString()));
		}
		if(condition.containsKey("isTemplate")){
			if(Constants.BOOLEAN_NO.equals(condition.get("isTemplate").toString())){
				hql += " and (isTemplate is null or isTemplate=:isTemplate) ";
			} else {
				hql += " and isTemplate=:isTemplate ";
			}			
			values.put("isTemplate", condition.get("isTemplate"));
		}
		if(condition.containsKey("studentId")){
			hql += " and exists (from "+StudentLearnPlan.class.getSimpleName()+" s where s.isDeleted=0 and s.teachingPlanCourse.course.resourceid=t.course.resourceid and s.studentInfo.sysUser.resourceid=:studentId ) ";
			values.put("studentId", condition.get("studentId"));
		}
		if(condition.containsKey("orderBy")){
			hql += " order by  t."+condition.get("orderBy").toString().replace(",", ",t.");
		}
		return findByHql(hql, values);
	}
	
	@Override
	public TeachTask findTeachTaskTemplate(String yearInfoId, String term) throws ServiceException {
		List<TeachTask> list = findByHql(" from "+TeachTask.class.getSimpleName()+" where isDeleted=0 and isTemplate=? and yearInfo.resourceid=? and term=? ",Constants.BOOLEAN_YES,yearInfoId,term);
		if(list != null && !list.isEmpty()){
			return list.get(0);
		}
		return null;
	}
	
	@Override
	public List<TeachTask> findStudentTeacherByCondition(Map<String, Object> condition) throws ServiceException {
		List<TeachTask> list = null;
		Session session  = exGeneralHibernateDao.getSessionFactory().getCurrentSession();
		String sql = "select * from edu_teach_teachtask t where t.isdeleted=0 and t.yearid=:yearInfoid and t.term=:term and t.taskstatus='3' and exists (select p.resourceid from edu_learn_stuplan p left join edu_teach_plancourse pc on p.plansourceid=pc.resourceid where p.isdeleted=0 and (pc.courseid=t.courseid or p.planoutcourseid=t.courseid) and p.studentid=:studentId)";
		SQLQuery query = session.createSQLQuery(sql).addEntity(TeachTask.class);
		query.setParameter("yearInfoid", condition.get("yearInfoid"));
		query.setParameter("term", condition.get("term"));
		query.setParameter("studentId", condition.get("studentId"));
		list = query.list();
		return list;
	}
}
