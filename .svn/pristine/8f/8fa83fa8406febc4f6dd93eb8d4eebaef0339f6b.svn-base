package com.hnjk.edu.teaching.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.teaching.model.StudentMakeupList;
import com.hnjk.edu.teaching.service.IStudentMakeupListService;

/**
 * 
 * <code>学生补考表Service实现</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2014-5-24 下午01:40:49
 * @see 
 * @version 1.0
 */
@Service("studentMakeupListService")
@Transactional
public class StudentMakeupListServiceImpl extends BaseServiceImpl<StudentMakeupList> implements IStudentMakeupListService{

	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;
	
	@Override
	public StudentMakeupList findStudentMakeupListByStuIdAndCourseId(
			String stuId, String courseId) throws ServiceException {
		
		
		
		return null;
	}

	/**
	 * 根据学籍ID和课程ID并按考试批次类型的升序排列获取补考名单
	 */
	@Override
	public List<Map<String, Object>> findByStudentIdAndCourseId(
			String studentId, String courseId) throws ServiceException {
		List<Map<String, Object>> makeupList = null;
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("studentId", studentId);
			parameters.put("courseId", courseId);
			
			String lastMakeup_sql="select mk.resourceid,mk.nextexamsubid,mk.resultsid from edu_teach_makeuplist mk, edu_teach_examsub es where "
					+ " mk.nextexamsubid=es.resourceid and mk.isdeleted=0 and es.isdeleted=0 and mk.studentid=:studentId "
					+ " and mk.courseid=:courseId order by es.examtype ";
			
			makeupList = baseSupportJdbcDao.getBaseJdbcTemplate().
					findForListMap(lastMakeup_sql, parameters);
		} catch (Exception e) {
			logger.error("根据学籍ID和课程ID并按考试批次类型的升序排列获取补考名单", e);
		}
		
		return makeupList;
	}
	
	/**
	 * 删除某个学生某门课程的补考名单
	 * @param studentInfoId
	 * @param courseId
	 * @param planCourseId
	 */
	@Override
	public void deleteByStuIdAndCourseId(String studentInfoId, String courseId, String planCourseId){
		try {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("studentInfoId", studentInfoId);
			param.put("courseId", courseId);
			param.put("planCourseId", planCourseId);
			String sql = "update edu_teach_makeuplist mk set  mk.isdeleted=1 where mk.studentid=:studentInfoId and mk.courseid=:courseId and mk.plansourceid=:planCourseId ";
			baseSupportJdbcDao.getBaseJdbcTemplate().executeForMap(sql, param);
		} catch (Exception e) {
			logger.error("删除某个学生某门课程的补考名单出错", e);
		}
	}

	/**
	 * 获取某个学生某门课程某个批次的补考名单
	 * @param studentInfoId
	 * @param planCourseId
	 * @param examSubId
	 * @return
	 */
	@Override
	public StudentMakeupList getByCondition(String studentInfoId,String planCourseId, String examSubId) {
		String hql = "from "+StudentMakeupList.class.getSimpleName()+" mk where mk.isDeleted=0 and mk.studentInfo.resourceid=? and mk.teachingPlanCourse.resourceid=? and mk.nextExamSubId=?";
		return exGeneralHibernateDao.findUnique(hql, studentInfoId,planCourseId,examSubId );
	}

	@Override
	public List<Map<String, Object>> findCourseByCondition(Map<String, Object> condition) {
		List<Map<String, Object>> result = null;
		StringBuffer hql = new StringBuffer();
		hql.append("select c.resourceid,c.coursename from edu_teach_makeuplist ml");
		hql.append(" join edu_base_course c on c.resourceid=ml.courseid and c.isdeleted=0");
		hql.append(" join edu_teach_examsub es on es.resourceid=ml.nextexamsubid and es.isdeleted=0");
		hql.append(" join edu_roll_studentinfo si on si.resourceid=ml.studentid");
		hql.append(" where si.isdeleted=0");
		Map<String, Object> param =  new HashMap<String, Object>();
		if(condition.containsKey("yearId")){
			hql.append(" and es.yearid=:yearId");
			param.put("yearId", condition.get("yearId"));
		}
		if(condition.containsKey("term")){
			hql.append(" and es.term=:term");
			param.put("term", condition.get("term"));
		}
		if(condition.containsKey("examType")){
			hql.append(" and es.examtype=:examType");
			param.put("examType", condition.get("examType"));
		}
		if(condition.containsKey("classesid")){
			hql.append(" and si.classesid=:classesid");
			param.put("classesid", condition.get("classesid"));
		}
		hql.append(" group by c.resourceid,c.coursename ");
		try {
			result = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(hql.toString(), param);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
}
