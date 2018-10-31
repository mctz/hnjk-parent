package com.hnjk.edu.learning.service.impl;

import java.math.BigDecimal;
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
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.basedata.model.Grade;
import com.hnjk.edu.basedata.model.YearInfo;
import com.hnjk.edu.basedata.service.IGradeService;
import com.hnjk.edu.basedata.service.IYearInfoService;
import com.hnjk.edu.learning.model.StudentExercise;
import com.hnjk.edu.learning.service.ILearningTimeSettingService;
import com.hnjk.edu.learning.service.IStudentExerciseService;
import com.hnjk.edu.learning.service.IStudentLearnPlanService;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.teaching.model.TeachingPlanCourse;
import com.hnjk.edu.teaching.model.TeachingPlanCourseStatus;
import com.hnjk.edu.teaching.service.ITeachingPlanCourseService;
import com.hnjk.edu.teaching.service.ITeachingPlanCourseStatusService;
import com.hnjk.edu.teaching.service.IUsualResultsService;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.Attachs;
import com.hnjk.platform.system.service.IAttachsService;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;

/**
 * 学生作业回答情况服务接口实现
 * <code>StudentExerciseServiceImpl</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-8-31 上午10:09:37
 * @see 
 * @version 1.0
 */
@Transactional
@Service("studentExerciseService")
public class StudentExerciseServiceImpl extends BaseServiceImpl<StudentExercise> implements IStudentExerciseService {

	@Autowired
	@Qualifier("attachsService")
	private IAttachsService attachsService;
	
	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;
	
	@Autowired
	@Qualifier("gradeService")
	private IGradeService gradeService;
	
	@Autowired
	@Qualifier("usualResultsService")
	private IUsualResultsService usualResultsService;
	
	@Autowired
	@Qualifier("yearInfoService")
	private IYearInfoService yearInfoService;
	
	@Qualifier("teachingPlanCourseService")
	private ITeachingPlanCourseService teachingPlanCourseService;
	
	@Autowired
	@Qualifier("learningTimeSettingService")
	private ILearningTimeSettingService learningTimeSettingService;
	
	@Autowired
	@Qualifier("studentLearnPlanService")
	private IStudentLearnPlanService studentLearnPlanService;
	
	@Autowired
	@Qualifier("teachingPlanCourseStatusService")
	private ITeachingPlanCourseStatusService teachingPlanCourseStatusService;
		
	@Override
	@Transactional(readOnly=true)
	public Page findStudentExerciseByCondition(Map<String, Object> condition, Page objPage) throws ServiceException {
		Map<String,Object> values =  new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer(" from "+StudentExercise.class.getSimpleName()+" se where 1=1 ");
		hql.append(" and se.isDeleted = :isDeleted ");
		values.put("isDeleted", 0);
		
		if(condition.containsKey("studentName")){//学生姓名
			hql.append(" and se.studentInfo.studentName like :studentName ");
			values.put("studentName", "%"+condition.get("studentName")+"%");
		}
		if(condition.containsKey("studyNo")){//学生号
			hql.append(" and se.studentInfo.studyNo =:studyNo ");
			values.put("studyNo", condition.get("studyNo"));
		}
		if(condition.containsKey("studentStatus")){
			hql.append(" and se.studentInfo.studentStatus in("+condition.get("studentStatus")+") ");
		}
		if(condition.containsKey("status")){
			hql.append(" and se.status =:status ");
			values.put("status", condition.get("status"));
		}
		if(condition.containsKey("gtstatus")){
			hql.append(" and se.status >:gtstatus ");
			values.put("gtstatus", condition.get("gtstatus"));
		}
		if(condition.containsKey("exerciseResult")){//习题总评项
			hql.append(" and se.exercise is null ");
		}
		if(condition.containsKey("exerciseBatchId")){//作业批次
			hql.append(" and se.exerciseBatch.resourceid =:exerciseBatchId ");
			values.put("exerciseBatchId", condition.get("exerciseBatchId"));
		}
		if(condition.containsKey("unitId")){//学习中心
			hql.append(" and se.studentInfo.branchSchool.resourceid =:unitId ");
			values.put("unitId", condition.get("unitId"));
		}
		if(condition.containsKey("shoolTeacherId")){//某个老师授课某门课程所属的所有教学点
			hql.append(" and exists (from TeachingPlanCourseTimetable tpct where tpct.isDeleted=0 and tpct.classes.brSchool.resourceid=se.studentInfo.branchSchool.resourceid and tpct.teacherId=:shoolTeacherId and tpct.course.resourceid=:courseId)");
			values.put("shoolTeacherId", condition.get("shoolTeacherId"));
			values.put("courseId", condition.get("courseId"));
		}
		if(condition.containsKey("classesTeacherId")){//某个老师授课某门课程所属的所有班级
			hql.append(" and exists (from TeachingPlanCourseTimetable tpct where tpct.isDeleted=0 and tpct.classes.resourceid=se.studentInfo.classes.resourceid and tpct.teacherId=:classesTeacherId )");
			values.put("classesTeacherId", condition.get("classesTeacherId"));
		}
		
		if(condition.containsKey("teacherId")){//某个老师授课某门课程所属的班级
			hql.append(" and exists (from TeachingPlanCourseTimetable tpct where tpct.isDeleted=0 and tpct.classes.resourceid=se.studentInfo.classes.resourceid and tpct.teacherId=:shoolTeacherId and tpct.course.resourceid=:courseId)");
			values.put("shoolTeacherId", condition.get("teacherId"));
			values.put("courseId", condition.get("courseId"));
		}
		
		hql.append(" order by se."+objPage.getOrderBy() +" "+ objPage.getOrder());
		return exGeneralHibernateDao.findByHql(objPage, hql.toString(), values);
	}

	@Override
	public boolean isExistStudentExercise(String exerciseBatchId, String studentInfoId) throws ServiceException {
		String hql = "select count(s) from "+StudentExercise.class.getName()+" s where s.isDeleted=0 and s.exerciseBatch.resourceid=? and s.studentInfo.resourceid=? ";
		Long count = exGeneralHibernateDao.findUnique(hql, exerciseBatchId,studentInfoId);
		return (count!=null&&count.intValue()>0)?true:false;
	}

	@Override
	public void saveOrUpdate(StudentExercise studentExercise, String[] uploadfileids) throws ServiceException {
		saveOrUpdate(studentExercise);
		List<Attachs> oldAttachs = attachsService.findAttachsByFormId(studentExercise.getResourceid());
		if(uploadfileids!=null&&oldAttachs!=null){//覆盖旧的作业
			for (Attachs attachs : oldAttachs) {
				attachsService.delete(attachs);
			}
		}
		User user = SpringSecurityHelper.getCurrentUser();
		attachsService.updateAttachByFormId(uploadfileids, studentExercise.getResourceid(), StudentExercise.class.getSimpleName(), user.getResourceid(), user.getCnName());
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<StudentExercise> findStudentExerciseByCondition(Map<String, Object> condition) throws ServiceException {
		Map<String,Object> values =  new HashMap<String, Object>();
		String hql = " from "+StudentExercise.class.getSimpleName()+" where 1=1 ";
		hql += " and isDeleted = :isDeleted ";
		values.put("isDeleted", 0);
		
		if(condition.containsKey("exerciseResult")){
			hql += " and exercise is null ";
		}
		if(condition.containsKey("exerciseBatchId")){
			hql += " and exerciseBatch.resourceid =:exerciseBatchId ";
			values.put("exerciseBatchId", condition.get("exerciseBatchId"));
		}
		if(condition.containsKey("exerciseBatchIds")){
			hql += " and exerciseBatch.resourceid in (:exerciseBatchIds) ";
			values.put("exerciseBatchIds", condition.get("exerciseBatchIds"));
		}
		if(condition.containsKey("courseId")){
			hql += " and exerciseBatch.course.resourceid =:courseId ";
			values.put("courseId", condition.get("courseId"));
		}
		if(condition.containsKey("yearInfoId")){//年度
			hql += " and exerciseBatch.yearInfo.resourceid =:yearInfoId ";
			values.put("yearInfoId", condition.get("yearInfoId"));
		}		
		if(condition.containsKey("term")){//学期
			hql += " and exerciseBatch.term =:term ";
			values.put("term", condition.get("term"));
		}
		if(condition.containsKey("isTypical")){
			hql += " and isTypical =:isTypical ";
			values.put("isTypical", condition.get("isTypical"));
		}
		if(condition.containsKey("isExcell")){
			hql += " and isExcell =:isExcell ";
			values.put("isExcell", condition.get("isExcell"));
		}
		if(condition.containsKey("studentInfoId")){
			hql += " and studentInfo.resourceid =:studentInfoId ";
			values.put("studentInfoId", condition.get("studentInfoId"));
		}
		if(condition.containsKey("orderBy")){
			hql += " order by "+ condition.get("orderBy");
		}
		
		return  findByHql(hql,values);
	}

	/*
	 * 1.如果本学期尚未布置作业，则进度框显示0%。
	 * 2.如果本学期只布置了一次作业，则取作业成绩的50%为作业进度，例如作业得分为90，则作业进度为45%；如果某次作业尚未评分，则此次作业得分视为0分。
	 * 3.如果本学期布置了两次作业，则取两次作业得分的平均分作为作业进度；如果某次作业尚未评分，则此次作业得分视为0分。
	 * 4.如果本学期布置了三次（或以上）的作业，则取作业得分中最高的两个，计算这两次的平均分作为作业进度；如果某次作业尚未评分，则此次作业得分视为0分。
	 * (non-Javadoc)
	 * @see com.hnjk.edu.learning.service.IStudentExerciseService#avgStudentExerciseResult(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Double avgStudentExerciseResult(String courseId, String studentInfoId, String yearId, String term) throws ServiceException {
		/*
		String hql1 = " select count(*) from "+ExerciseBatch.class.getName()+" s where s.isDeleted=0 and s.course.resourceid=? and s.yearInfo.resourceid=? and s.term=? ";
		Long total = exGeneralHibernateDao.findUnique(hql1, courseId, yearId, term);
		total = total!=null?total:0;
		if(total.intValue()==0)
			return 0.0;
		String hql2 = " select sum(s.result) from "+StudentExercise.class.getName()+" s where s.isDeleted=0 and s.exerciseBatch.course.resourceid=? and s.studentInfo.resourceid=? and s.exerciseBatch.yearInfo.resourceid=? and s.exerciseBatch.term=? and s.exercise is null ";
		Double totalSum = exGeneralHibernateDao.findUnique(hql2, courseId,studentInfoId, yearId, term);
		totalSum = totalSum!=null?totalSum:0.0;
		return totalSum/total;*/
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("courseId", courseId);
		param.put("studentId", studentInfoId);
		param.put("yearId", yearId);
		param.put("term", term);
		//查询时，把未做或未批改的作业视为0分
		String sql = " select nvl(decode(nvl(st.status,0),0,0,st.result),0) score,nvl(st.status,0) status from edu_lear_exercisebatch t left join edu_lear_studentexercise st on st.exercisebatchid=t.resourceid and st.studentid=:studentId and st.isdeleted=0 and st.exerciseid is null where t.isdeleted=0 and t.status>=1 and t.courseid=:courseId and t.yearid=:yearId and t.term=:term order by status desc, score desc ";
		Double result = 0.0;
		try {
			List<Map<String, Object>> list = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql, param);
			if(ExCollectionUtils.isNotEmpty(list)){
				BigDecimal avg = new BigDecimal(0.0);
				for (int i = 0; i < list.size(); i++) {
					if(i<2){ // 取前两次,适合于一次的情况
						BigDecimal score = (BigDecimal)list.get(i).get("SCORE");
						avg = avg.add(score);
					}						
				}
				result = avg.divide(new BigDecimal(2.0), 1, BigDecimal.ROUND_HALF_UP).doubleValue();//取平均分
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public Integer countStudentFinished(String exerciseBatchId) throws ServiceException {
		String hql = " select count(*) from "+StudentExercise.class.getName()+" s where s.isDeleted=0 and s.exerciseBatch.resourceid=? and s.exercise is null and s.status >0 ";
		Long total = exGeneralHibernateDao.findUnique(hql, exerciseBatchId);
		return (total!=null)?total.intValue():0;
	}
	
	@Override
	public Map avgStudentFinished(String exerciseBatchId) throws ServiceException {
		String hql = " select new map(count(*) as num,avg(s.result) as result) from "+StudentExercise.class.getName()+" s where s.isDeleted=0 and s.exerciseBatch.resourceid=? and s.exercise is null ";
		Map map = exGeneralHibernateDao.findUnique(hql, exerciseBatchId);		
		return map;
	}

	@Override
	public void batchSaveOrUpdateStudentExercise(List<StudentExercise> studentExercises) throws ServiceException {
		batchSaveOrUpdate(studentExercises);
		User user = SpringSecurityHelper.getCurrentUser();
		for (StudentExercise studentExercise : studentExercises) {
			attachsService.updateAttachByFormId(studentExercise.getAttachIds(), studentExercise.getResourceid(), StudentExercise.class.getSimpleName(), user.getResourceid(), user.getCnName());		
		}
	}
	
	@Override
	public void cancelStudentExercise(String exerciseBatchId, String[] studentIds) throws ServiceException {
		List<StudentExercise> list = findByCriteria(Restrictions.eq("isDeleted", 0),Restrictions.eq("exerciseBatch.resourceid", exerciseBatchId),Restrictions.in("studentInfo.resourceid", studentIds));
		if(ExCollectionUtils.isNotEmpty(list)){
			/*Grade grade = gradeService.getDefaultGrade();*/
			/*String yearTerm = CacheAppManager.getSysConfigurationByCode("sysCurrentGrade").getParamValue();
			String yearInfoId = "";
			String term = "";
			if (null!=yearTerm) {
				String[] ARRYyterm = yearTerm.split("\\.");
				yearInfoId = yearInfoService.getByFirstYear(Long.parseLong(ARRYyterm[0])).getResourceid();
				term = ARRYyterm[1];
			}*/
			StudentInfo stu = null;
			Course c = null;
			for (StudentExercise studentExercise : list) {				
				studentExercise.setStatus(0);//撤回未提交状态
				studentExercise.setResult(null);
				 //调整成绩
				stu = studentExercise.getStudentInfo();
				if(null==c) {c = studentExercise.getExerciseBatch().getCourse();}
				//开课学期
				TeachingPlanCourse teachingPlanCourse = studentLearnPlanService.getStudentLearnPlanByCourse(c.getResourceid(), stu.getResourceid(),"studentId").getTeachingPlanCourse();
				TeachingPlanCourseStatus teachingPlanCourseStatus = teachingPlanCourseStatusService
						.findOneByCondition(stu.getGrade()
								.getResourceid(), stu.getTeachingPlan().getResourceid(),
								teachingPlanCourse.getResourceid(), stu.getBranchSchool().getResourceid());
				/*if(teachingPlanCourseStatus==null){
					//更新学生成绩失败:{课程还没有开课}
					return;
				}*/
				String yearTermStr = teachingPlanCourseStatus.getTerm();
				String year = "";
				String term = "";
				String yearInfoId = "";
				if (null!=yearTermStr) {
					year = yearTermStr.substring(0, 4);
					term = yearTermStr.substring(6, 7);
				}
				YearInfo yearInfo = yearInfoService.getByFirstYear(Long.parseLong(year));
				yearInfoId = yearInfo.getResourceid();
				usualResultsService.saveSpecificUsualResults(stu, yearInfoId, term, c.getResourceid());
			}
		}
	}
}
