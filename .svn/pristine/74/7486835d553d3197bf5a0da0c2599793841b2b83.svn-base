package com.hnjk.edu.learning.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.ExCollectionUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.learning.model.ActiveCourseExam;
import com.hnjk.edu.learning.model.CourseExam;
import com.hnjk.edu.learning.model.StudentActiveCourseExam;
import com.hnjk.edu.learning.service.IActiveCourseExamService;
import com.hnjk.edu.teaching.model.Syllabus;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;

/**
 * 随堂练习服务接口实现
 * <code>ActiveCourseExamServiceImpl</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-8-4 下午05:32:47
 * @see 
 * @version 1.0
 */
@Transactional
@Service("activeCourseExamService")
public class ActiveCourseExamServiceImpl extends BaseServiceImpl<ActiveCourseExam> implements IActiveCourseExamService {
	
	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;

	@Override
	@Transactional(readOnly=true)
	public Page findActiveCourseExamByCondition(Map<String, Object> condition, Page objPage) throws ServiceException {
		Map<String,Object> values =  new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer(" from "+ActiveCourseExam.class.getSimpleName()+" t where 1=1 ");
		hql.append(" and t.isDeleted = :isDeleted ");
		values.put("isDeleted", 0);
		
		if(condition.containsKey("question")){//问题主干
			hql.append(" and t.question like :question ");
			values.put("question", "%"+condition.get("question")+"%");
		}
		if(condition.containsKey("showOrder")){//排序号
			hql.append(" and t.showOrder=:showOrder ");
			values.put("showOrder", Integer.parseInt(condition.get("showOrder").toString()));
		}
		if(condition.containsKey("questionType")){//题型
			hql.append(" and t.questionType =:questionType ");
			values.put("questionType", condition.get("questionType"));
		}
		if(condition.containsKey("syllabusId")){//知识节点
			hql.append(" and t.syllabus.resourceid =:syllabusId ");
			values.put("syllabusId", condition.get("syllabusId"));
		}	
		if(condition.containsKey("courseId")){//所属课程
			hql.append(" and t.syllabus.course.resourceid =:courseId ");
			values.put("courseId", condition.get("courseId"));
		}
		if(condition.containsKey("isPublished")){
			hql.append(" and t.isPublished =:isPublished ");
			values.put("isPublished", condition.get("isPublished"));
		}
		if(condition.containsKey("notInCourseExams")){
			hql.append(" and t.courseExam.resourceid not in (:notInCourseExams) ");
			values.put("notInCourseExams", condition.get("notInCourseExams"));
		}
		if(condition.containsKey("studentInfoId")&&condition.containsKey("scopeType")){
			if("unsave".equals(condition.get("scopeType"))){//未作答的
				hql.append(" and not exists ( from "+StudentActiveCourseExam.class.getSimpleName()+" s where s.isDeleted=:isDeleted and s.activeCourseExam.resourceid=t.resourceid and s.studentInfo.resourceid=:studentInfoId ) ");
			} else if("unfinished".equals(condition.get("scopeType"))){
				hql.append(" and exists ( from "+StudentActiveCourseExam.class.getSimpleName()+" s where s.isDeleted=:isDeleted and s.activeCourseExam.resourceid=t.resourceid and s.result is null and s.studentInfo.resourceid=:studentInfoId ) ");
			}
			values.put("studentInfoId", condition.get("studentInfoId"));
		}
		hql.append(" order by "+objPage.getOrderBy() +" "+ objPage.getOrder());
		return exGeneralHibernateDao.findByHql(objPage, hql.toString(), values);
	}

	@Override
	public void batchCascadeDelete(String[] ids) throws ServiceException {
		if(ids!=null && ids.length>0){
			User user = SpringSecurityHelper.getCurrentUser();
			for(String id : ids){
				//delete(id);
				ActiveCourseExam exam = get(id);
				exam.setIsDeleted(1);
				exam.setModifyMan(user.getCnName());
				exam.setModifyDate(new Date());
				update(exam);
				logger.info("删除随堂练习="+id);
			}
		}
	}

	@Override
	public Integer getNextShowOrder(String syllabusId) throws ServiceException {
		Integer showOrder = exGeneralHibernateDao.findUnique("select max(c.showOrder) from "+ActiveCourseExam.class.getSimpleName()+" c where c.isDeleted=0 and c.syllabus.resourceid=? ", syllabusId);
		if(showOrder==null) {
			showOrder = 0;
		}
		return ++showOrder;
	}

	@Override
	public Long getTotalCount(String courseId) throws ServiceException {
		String hql = " select count(*) from "+ActiveCourseExam.class.getSimpleName()+" where isDeleted=0 and syllabus.course.resourceid = ? ";
		Long count = exGeneralHibernateDao.findUnique(hql, courseId);
		return (null!=count)?count: 0L;
	}

	@Override
	@Transactional(readOnly=true)
	public List<ActiveCourseExam> findActiveCourseExamByCondition(Map<String, Object> condition) throws ServiceException {
		Map<String,Object> values =  new HashMap<String, Object>();
		String hql = " from "+ActiveCourseExam.class.getSimpleName()+" where 1=1 ";
		hql += " and isDeleted = :isDeleted ";
		values.put("isDeleted", 0);
		
		if(condition.containsKey("questionType")){//题型
			hql += " and questionType =:questionType ";
			values.put("questionType", condition.get("questionType"));
		}
		if(condition.containsKey("syllabusId")){//知识节点
			hql += " and syllabus.resourceid =:syllabusId ";
			values.put("syllabusId", condition.get("syllabusId"));
		}	
		if(condition.containsKey("courseId")){//所属课程
			hql += " and syllabus.course.resourceid =:courseId ";
			values.put("courseId", condition.get("courseId"));
		}
		if(condition.containsKey("orderBy")){
			hql += " order by  "+condition.get("orderBy");
		}
		return (List<ActiveCourseExam>) exGeneralHibernateDao.findByHql(hql,values);
	}

	@Override
	public boolean isExsitAcitveCoruseExam(String syllabusId,String courseExamId) throws ServiceException {
		List list= exGeneralHibernateDao.findByHql(" from "+ActiveCourseExam.class.getSimpleName()+" where isDeleted=0 and syllabus.resourceid=? and courseExam.resourceid=? ", syllabusId,courseExamId);
		return (null!=list && !list.isEmpty())?true:false;
	}	

	@Override
	public void saveImportAcitveCoruseExam(List<ActiveCourseExam> list1, List<CourseExam> list2) throws ServiceException {
		exGeneralHibernateDao.batchSaveOrUpdate(list2);
		batchSaveOrUpdate(list1);
	}
	
	@Override
	public void moveActiveCourseExam(String[] ids,String fromSyllabusId, String toSyllabusId) throws ServiceException {
		List<ActiveCourseExam> list = new ArrayList<ActiveCourseExam>();
		if(ExStringUtils.isNotBlank(fromSyllabusId)){
			list = findByHql(" from "+ActiveCourseExam.class.getSimpleName()+" where isDeleted=? and syllabus.resourceid=? ", 0,fromSyllabusId);
		} else if(ids!=null && ids.length>0){
			list = findByCriteria(Restrictions.in("resourceid", ids));
		}
		if(ExCollectionUtils.isNotEmpty(list)){
			Syllabus syllabus = (Syllabus) exGeneralHibernateDao.get(Syllabus.class, toSyllabusId);
			for (ActiveCourseExam activeCourseExam : list) {
				activeCourseExam.setSyllabus(syllabus);
			}
			batchSaveOrUpdate(list);
		}
	}
	
	@Override
	public void publishActiveCourseExam(String[] ids, String isPublished) throws ServiceException {
		if(ids!=null && ids.length>0){
			User user = SpringSecurityHelper.getCurrentUser();
			for(String id : ids){
				ActiveCourseExam exam = get(id);
				if(!ExStringUtils.equals(isPublished, exam.getIsPublished())){
					exam.setIsPublished(isPublished);
					exam.setAuditDate(new Date());
					exam.setAuditMan(user.getCnName());
					exam.setAuditManId(user.getResourceid());
				}
			}
		}
	}
	
	
	@Override
	public List<Map<String, Object>> getTeacherOnlineCourse(Map<String,Object> parmas) throws ServiceException{
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		Map<String,Object> conditions = new HashMap<String, Object>();
		try {
			StringBuffer sql = new StringBuffer("  select te.courseid,ce.coursename,ce.coursecode from EDU_TEACH_TIMETABLE te ");
			sql.append(" join edu_teach_plancourse pe on pe.resourceid = te.plancourseid ");
			sql.append(" join edu_base_course ce on ce.resourceid = pe.courseid ");
			sql.append(" where 1 = 1 ");
			if(parmas.containsKey("hasResource")){
				sql.append(" and ce.hasresource = :hasResource ");
				conditions.put("hasResource", parmas.get("hasResource"));
			} 
			if(parmas.containsKey("classesId")){
				sql.append(" and te.classesid = :classesId ");
				conditions.put("classesId", parmas.get("classesId"));
			} 
			if(parmas.containsKey("classesIds")){
				sql.append(" and te.classesid in (:classesIds )");
				conditions.put("classesIds", parmas.get("classesIds"));
			}else if (parmas.containsKey("classesIdList")){
				sql.append(" and te.classesid in (:classesIdList )");
				conditions.put("classesIdList", parmas.get("classesIdList"));
			}
			if(parmas.containsKey("teacherId")){
				sql.append(" and te.teacherid = :teacherId ");
				conditions.put("teacherId", parmas.get("teacherId"));
			}
			if(parmas.containsKey("schoolId")){
				sql.append(" and te.classesid in  (select cl.resourceid from edu_roll_classes cl where cl.isdeleted=0 and cl.orgunitid=:schoolId) ");
				conditions.put("schoolId", parmas.get("schoolId"));
			}else if (parmas.containsKey("brSchoolid")) {
				sql.append(" and te.classesid in  (select cl.resourceid from edu_roll_classes cl where cl.isdeleted=0 and cl.orgunitid=:schoolId) ");
				conditions.put("schoolId", parmas.get("brSchoolid"));
			}
			sql.append(" group by te.courseid,ce.coursename,ce.coursecode ");
			list = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql.toString(), conditions);
		} catch (Exception e) {		
			e.printStackTrace();
		}
		return list;
	}
	@Override
	public long getSyllabusCount(Map<String,Object> condition) throws ServiceException{
		String sql1 = "select count(s.resourceid) from edu_lear_courseexam s join edu_lear_exams es on es.resourceid=s.examid where s.isdeleted=0 and es.examtype!='6' and s.syllabustreeid=:syllabusId";
		try {
			return baseSupportJdbcDao.getBaseJdbcTemplate().findForLong(sql1,condition);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0L;
	}
}
