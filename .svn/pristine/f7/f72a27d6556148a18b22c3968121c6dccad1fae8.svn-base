package com.hnjk.edu.teaching.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.ExCollectionUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.rao.dao.listener.OperationType;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.teaching.model.TeachingPlanCourseTimetable;
import com.hnjk.edu.teaching.service.ITeachingPlanCourseTimetableService;
import com.hnjk.platform.system.model.HistoryModel;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;
/**
 * 课程排课服务.
 * <code>TeachingPlanCourseTimetableServiceImpl</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2013-4-11 上午09:36:42
 * @see 
 * @version 1.0
 */
@Transactional
@Service("teachingPlanCourseTimetableService")
public class TeachingPlanCourseTimetableServiceImpl extends BaseServiceImpl<TeachingPlanCourseTimetable> implements ITeachingPlanCourseTimetableService {
	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;
	
	@Override
	@Transactional(readOnly=true)
	public List<TeachingPlanCourseTimetable> findTeachingPlanCourseTimetableByCondition(Map<String, Object> condition) throws ServiceException {
		Map<String, Object> values = new HashMap<String, Object>();
		StringBuilder hql = new StringBuilder();
		hql.append(" from ").append(TeachingPlanCourseTimetable.class.getSimpleName()).append(" where isDeleted=:isDeleted ");
		values.put("isDeleted", 0);
		if(condition.containsKey("classesid")){
			hql.append(" and classes.resourceid=:classesid ");
			values.put("classesid", condition.get("classesid"));
		}
		if(condition.containsKey("plancourseid")){
			hql.append(" and teachingPlanCourse.resourceid=:plancourseid ");
			values.put("plancourseid", condition.get("plancourseid"));
		}
		if(condition.containsKey("course")){
			hql.append(" and course.resourceid=:course ");
			values.put("course", condition.get("course"));
		}
		if(condition.containsKey("term")){
			hql.append(" and term=:term ");
			values.put("term", condition.get("term"));
		}
		if(condition.containsKey("teacherId")){
			hql.append(" and teacherId=:teacherId ");
			values.put("teacherId", condition.get("teacherId"));
		}
		if(condition.containsKey("isStoped")){
			hql.append(" and isStoped=:isStoped ");
			values.put("isStoped", condition.get("isStoped"));
		}
		if(condition.containsKey("gradeid")){
			hql.append(" and classes.grade.resourceid=:gradeid ");
			values.put("gradeid", condition.get("gradeid"));
		}
		if(condition.containsKey("week")){
			hql.append(" and week=:week ");
			values.put("week", condition.get("week"));
		}
		if(condition.containsKey("unitTimePeriod")){
			hql.append(" and unitTimePeriod.resourceid=:unitTimePeriod");
			values.put("unitTimePeriod", condition.get("unitTimePeriod"));
		}
		hql.append(" order by classes,teachingPlanCourse.term,week,resourceid ");
		return findByHql(hql.toString(), values);
	}
	
	@Override
	public void updateCourseTimetable(TeachingPlanCourseTimetable timetable)	throws ServiceException {
		if(ExStringUtils.isNotBlank(timetable.getBeforeContent()) && ExStringUtils.isNotBlank(timetable.getAfterContent())){
			User currentUser = SpringSecurityHelper.getCurrentUser();  
			HistoryModel historyModel = new HistoryModel();
			historyModel.setOperatorType(OperationType.UPDATE);
			historyModel.setBeforeValue(timetable.getBeforeContent());
			historyModel.setAfterValue(timetable.getAfterContent());	    	 
			historyModel.setEntiryId(timetable.getResourceid());
			historyModel.setEntiryName(TeachingPlanCourseTimetable.class.getSimpleName());				 
			historyModel.setOperatorMan(currentUser.getCnName());
			historyModel.setOperatorManId(currentUser.getResourceid());
			historyModel.setOperatorIp(currentUser.getLoginIp());
			exGeneralHibernateDao.save(historyModel);//记录日志		  
		}
		update(timetable);
	}
	
	@Override
	public Page findCourseTimetableHistoryByCondition(Map<String, Object> condition, Page objPage) throws ServiceException {
		List<Object> param = new ArrayList<Object>();
		StringBuilder sql =  getCourseTimetableHistorySql(condition, param);
		return baseSupportJdbcDao.getBaseJdbcTemplate().findListMap(objPage, sql.toString(), param.toArray());
	}

	private StringBuilder getCourseTimetableHistorySql(Map<String, Object> condition,List<Object> param) {
		StringBuilder sql = new StringBuilder();
		sql.append("select cl.classesname classname,c.coursename,a.timeperiod,t.week,h.* from hnjk_sys_history h ");
		sql.append(" join edu_teach_timetable t on h.entiryid=t.resourceid ")
		.append(" join edu_teach_timeperiod a on a.resourceid=t.timeperiodid ")
		.append(" join edu_base_course c on c.resourceid=t.courseid ")
		.append(" join edu_roll_classes cl on cl.resourceid=t.classesid ")
		.append(" where h.isdeleted=0 and h.entiryname='TeachingPlanCourseTimetable' ");
		if(condition.containsKey("classesid")){
			sql.append(" and t.classesid=? ");
			param.add(condition.get("classesid"));
		}
		if(condition.containsKey("courseid")){
			sql.append(" and t.courseid=? ");
			param.add(condition.get("courseid"));
		}
		if(condition.containsKey("brSchoolid")){
			sql.append(" and cl.orgunitid=? ");
			param.add(condition.get("brSchoolid"));
		}
		if(condition.containsKey("term")){
			sql.append(" and t.term=? ");
			param.add(condition.get("term"));
		}
		sql.append(" order by h.operatortime desc,h.resourceid ");
		return sql;
	}
	@Override
	public List<Map<String, Object>> findCourseTimetableHistoryByCondition(	Map<String, Object> condition) throws ServiceException {
		List<Object> param = new ArrayList<Object>();
		StringBuilder sql =  getCourseTimetableHistorySql(condition, param);
		try {
			return baseSupportJdbcDao.getBaseJdbcTemplate().findForList(sql.toString(), param.toArray());
		} catch (Exception e) {
		}
		return null;
	}
	
	
	/**
	 * 是否权限
	 * @param userId
	 * @param courseId
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public boolean hasAuthority(String userId, String courseId, String classesId) throws ServiceException {
		boolean hasCheckAuthority = false;
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("course", courseId);
		condition.put("teacherId", userId);
		if(classesId != null){
			condition.put("classesid", classesId);
		}
		List<TeachingPlanCourseTimetable> tpctList = findTeachingPlanCourseTimetableByCondition(condition);
		if(ExCollectionUtils.isNotEmpty(tpctList)){
			hasCheckAuthority = true;
		}
		return hasCheckAuthority;
	}	
	
	/**
	 * 是否已经排课
	 * @param courseId
	 * @param term
	 * @return
	 */
	@Override
	public boolean isArrangeCourse(String courseId, String term){
		boolean isArrangeCourse = false;
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("course", courseId);
		condition.put("term", term);
		List<TeachingPlanCourseTimetable>  tpctList = findTeachingPlanCourseTimetableByCondition(condition);
		if(ExCollectionUtils.isNotEmpty(tpctList)){
			isArrangeCourse = true;
		}
		return isArrangeCourse;
	}
	
	/**
	 * 根据条件获取已经排课的课程ID列表
	 * @param condition
	 * @return
	 */
	@Override
	public List<String> findCourseIdByCondition(Map<String, Object> condition){
		List<String> courseIdList = new ArrayList<String>();
		try {
			StringBuffer sql = new StringBuffer("select tt.courseid courseId from edu_teach_timetable tt where tt.isdeleted=0 ");
			if(condition.containsKey("schoolId")){
				sql.append(" and tt.classesid in (select cl.resourceid from edu_roll_classes cl where cl.isdeleted=0 and cl.orgunitid=:schoolId) ");
			}else if (condition.containsKey("unitId")) {
				sql.append(" and tt.classesid in (select cl.resourceid from edu_roll_classes cl where cl.isdeleted=0 and cl.orgunitid=:unitId) ");
			} else {
				if(condition.containsKey("classesIds") && condition.containsKey("teacherId")){
					sql.append(" and (tt.classesid in (:classesIds) or tt.teacherid=:teacherId) ");
				} else {
					if(condition.containsKey("classesIds")){
						sql.append(" and tt.classesid in (:classesIds) ");
					}
					if(condition.containsKey("teacherId")){
						sql.append(" and tt.teacherid=:teacherId ");
					}
				}
			}
			sql.append(" group by tt.courseid ");
			List<Map<String, Object>> courseIdMapList= baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql.toString(), condition);
			if(ExCollectionUtils.isNotEmpty(courseIdMapList)){
				for(Map<String, Object> m: courseIdMapList){
					courseIdList.add((String)m.get("courseId"));
				}
			}
		} catch (Exception e) {
			logger.error("根据条件获取已经排课的课程ID列表出错", e);
		}
		return courseIdList;
	}

	/**
	 * 获取某个老师授课或教某门课程的所有教学点
	 * @param teacherId
	 * @param courseId  这个可以为空
	 * @return
	 */
	@Override
	public List<String> findSchoolIdByTeacherId(String teacherId, String courseId) {
		List<String> shoolIdList = new ArrayList<String>();
		try {
			Map<String, Object> param = new HashMap<String, Object>();
			StringBuffer sql = new StringBuffer("select cl.orgunitid unitId from edu_teach_timetable tt,edu_roll_classes cl ");
			sql.append(" where  cl.resourceid=tt.classesid and cl.isdeleted=0 and tt.isdeleted=0  ");
			sql.append(" and tt.teacherid=:teacherId  ");
			param.put("teacherId", teacherId);
			if(ExStringUtils.isNotEmpty(courseId)){
				sql.append(" and tt.courseid=:courseId  ");
				param.put("courseId", courseId);
			}
			sql.append(" group by cl.orgunitid  ");
			
			List<Map<String, Object>> schoolIdMapList= baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql.toString(), param);
			if(ExCollectionUtils.isNotEmpty(schoolIdMapList)){
				for(Map<String, Object> unit: schoolIdMapList){
					shoolIdList.add((String)unit.get("unitId"));
				}
			}
		} catch (Exception e) {
			logger.error("获取某个老师授课或教某门课程的所有教学点出错", e);
		}
		return shoolIdList;
	}

}
