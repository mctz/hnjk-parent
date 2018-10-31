package com.hnjk.edu.learning.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.learning.model.ActiveCourseExam;
import com.hnjk.edu.learning.model.CourseExam;
import com.hnjk.edu.learning.model.StudentActiveCourseExam;
import com.hnjk.edu.learning.model.StudentLearnPlan;
import com.hnjk.edu.learning.service.IStudentActiveCourseExamService;
import com.hnjk.edu.roll.model.StudentInfo;
//import com.hnjk.edu.teaching.model.TeachTask;
/**
 * 随堂练习学生回答情况服务接口实现
 * <code>StudentActiveCourseExamServiceImpl</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-8-23 上午09:48:38
 * @see 
 * @version 1.0
 */
@Transactional
@Service("studentActiveCourseExamService")
public class StudentActiveCourseExamServiceImpl extends BaseServiceImpl<StudentActiveCourseExam> implements IStudentActiveCourseExamService {
	@Override
	public Long finishedActiveCourseExam(String syllabusId, String studentInfoId, String type) throws ServiceException {
		Map<String, Object> values = new HashMap<String, Object>();
		values.put("isDeleted", 0);
		values.put("syllabusId", syllabusId);
		values.put("studentInfoId", studentInfoId);
		values.put("booleanYes", Constants.BOOLEAN_YES);
		
		String hql = "select count(distinct s.activeCourseExam.resourceid) from "+StudentActiveCourseExam.class.getName()+" s where s.isDeleted=:isDeleted and s.activeCourseExam.syllabus.resourceid=:syllabusId and s.studentInfo.resourceid=:studentInfoId and s.activeCourseExam.isPublished=:booleanYes and s.activeCourseExam.isDeleted=:isDeleted ";
		if("correct".equals(type)){//答对题目数
			hql += " and s.isCorrect=:booleanYes ";
		} else if("finished".equals(type)){//已提交题目数
			hql += "and s.result is not null ";
		} else if("all".equals(type)){
			hql = "select count(t.resourceid) from "+ActiveCourseExam.class.getName()+" t where t.isDeleted=:isDeleted and t.syllabus.resourceid=:syllabusId and t.isPublished=:booleanYes and t.courseExam.examType!='6' ";
		} else if("old".equals(type)){
			hql = "select count(s) from "+StudentActiveCourseExam.class.getName()+" s where s.isDeleted=:isDeleted and s.activeCourseExam.syllabus.resourceid=:syllabusId and s.studentInfo.resourceid=:studentInfoId and s.isCorrect=:booleanYes and (s.activeCourseExam.isDeleted=1 or s.activeCourseExam.isPublished<>:booleanYes) ";
		}
		return exGeneralHibernateDao.findUnique(hql, values);
	}
	/*
	 * 答题正确率=新旧答题正确题目数/(旧的答题正确数+新的随堂练习数)
	 * (non-Javadoc)
	 * @see com.hnjk.edu.learning.service.IStudentActiveCourseExamService#avgStudentActiveCourseExamResult(java.lang.String, java.lang.String)
	 */
	@Override
	public Double avgStudentActiveCourseExamResult(String courseId, String studentInfoId) throws ServiceException {
		String hql1 = "select count(s) from "+ActiveCourseExam.class.getName()+" s where s.isDeleted=? and s.syllabus.course.resourceid=? and s.isPublished=? and s.courseExam.examType!='6' ";
		Long totalCount = exGeneralHibernateDao.findUnique(hql1, 0,courseId,Constants.BOOLEAN_YES);//已发布的随堂练习总题目数
		totalCount = totalCount!=null?totalCount: 0L;
		
		String hql2 = "select count(distinct s.activeCourseExam.resourceid) from "+StudentActiveCourseExam.class.getName()+" s where s.isDeleted=? and s.activeCourseExam.syllabus.course.resourceid=? and s.studentInfo.resourceid=? and s.isCorrect='Y' and (s.activeCourseExam.isDeleted=1 or s.activeCourseExam.isPublished<>'Y') ";
		Long oldCorrectCount = exGeneralHibernateDao.findUnique(hql2, 0,courseId,studentInfoId);//旧的随堂练习正确题目数
		oldCorrectCount = oldCorrectCount!=null?oldCorrectCount: 0L;
		totalCount = totalCount.longValue()+oldCorrectCount.longValue();
		if(totalCount.longValue()==0){
			return 0.0;
		}
		String hql3 = "select count(distinct s.activeCourseExam.resourceid) from "+StudentActiveCourseExam.class.getName()+" s where s.isDeleted=? and s.activeCourseExam.syllabus.course.resourceid=? and s.studentInfo.resourceid=? and s.isCorrect=? and s.result is not null ";
		Long correctCount = exGeneralHibernateDao.findUnique(hql3, 0,courseId,studentInfoId,Constants.BOOLEAN_YES);//新旧答题正确题目数
		correctCount = correctCount!=null?correctCount: 0L;
		return correctCount.doubleValue()/totalCount.doubleValue();
	}
	
	@Override
	@Transactional(readOnly=true)
	public Page findStudentActiveCourseExamByCondition(	Map<String, Object> condition, Page objPage) throws ServiceException {
		Map<String,Object> values =  new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer(" from "+StudentActiveCourseExam.class.getSimpleName()+" s where 1=1 ");
		hql.append(" and s.isDeleted = :isDeleted ");
		values.put("isDeleted", 0);
		
		if(condition.containsKey("studyNo")){//学号
			hql.append(" and s.studentInfo.studyNo =:studyNo ");
			values.put("studyNo", condition.get("studyNo"));
		}
		if(condition.containsKey("classesId")){//班级
			hql.append(" and s.studentInfo.classes.resourceid =:classesId ");
			values.put("classesId", condition.get("classesId"));
		}
		if(condition.containsKey("studentName")){//姓名
			hql.append(" and s.studentInfo.studentName =:studentName ");
			values.put("studentName", condition.get("studentName"));
		}
		if(condition.containsKey("syllabusId")){//知识节点
			hql.append(" and s.activeCourseExam.syllabus.resourceid =:syllabusId ");
			values.put("syllabusId", condition.get("syllabusId"));
		}	
		if(condition.containsKey("courseId")){//所属课程
			hql.append(" and s.activeCourseExam.syllabus.course.resourceid =:courseId ");
			values.put("courseId", condition.get("courseId"));
		}
		if(condition.containsKey("yearInfoId") || condition.containsKey("term")){
			hql.append(" and exists ( from "+StudentLearnPlan.class.getSimpleName()+" p where p.isDeleted=0 and p.studentInfo.resourceid=s.studentInfo.resourceid and p.teachingPlanCourse.course.resourceid=s.activeCourseExam.syllabus.course.resourceid ");
			if(condition.containsKey("yearInfoId")){
				hql.append(" and p.yearInfo.resourceid=:yearInfoId ");
				values.put("yearInfoId", condition.get("yearInfoId"));
			}
			if(condition.containsKey("term")){
				hql.append(" and p.term=:term ");
				values.put("term", condition.get("term"));
			}
			hql.append(" ) "); 			
		}
		if(condition.containsKey("schoolId")){
			hql.append(" and s.studentInfo.classes.brSchool.resourceid in (:schoolId) ");
			values.put("schoolId", condition.get("schoolId"));
		}
		if(condition.containsKey("studentStatus")){
			hql.append(" and s.studentInfo.studentStatus in("+condition.get("studentStatus")+") ");
		}
		if(condition.containsKey("teacherId") && condition.containsKey("classesIds")){
//			hql.append(" and exists ( from "+TeachTask.class.getSimpleName()+" task where task.isDeleted=0 and task.taskStatus=3 and task.course.resourceid=s.activeCourseExam.syllabus.course.resourceid and task.teacherId=:teacherId) ");
//			values.put("teacherId", condition.get("teacherId"));
			hql.append(" and (s.studentInfo.classes.resourceid in (select tpct.classes.resourceid from TeachingPlanCourseTimetable tpct where tpct.isDeleted=0 and (tpct.teacherId=:teacherId or s.studentInfo.classes.resourceid in (:classesIds)) group by tpct.classes.resourceid) ");
			values.put("teacherId", condition.get("teacherId"));
			values.put("classesIds", condition.get("classesIds"));
		} else {
			if(condition.containsKey("teacherId")){
				hql.append(" and s.studentInfo.classes.resourceid in (select tpct.classes.resourceid from TeachingPlanCourseTimetable tpct where tpct.isDeleted=0 and tpct.teacherId=:teacherId group by tpct.classes.resourceid) ");
				values.put("teacherId", condition.get("teacherId"));
			}
			if(condition.containsKey("classesIds")){
				hql.append(" and s.studentInfo.classes.resourceid in (:classesIds) ");
				values.put("classesIds", condition.get("classesIds"));
			}
		}
		
		if(condition.containsKey("isHasResult")){
			hql.append(" and s.result is not null ");
		}
		hql.append(" and exists( from "+ActiveCourseExam.class.getName()+" t where t.isDeleted=0 and t.resourceid=s.activeCourseExam.resourceid ) ");
		hql.append(" order by "+objPage.getOrderBy() +" "+ objPage.getOrder());
		return findByHql(objPage, hql.toString(), values);
	}
	
	@Override
	public void redoActiveCourseExam(String[] ids) throws ServiceException {
		if(ids != null && ids.length>0){
			for (String id : ids) {
				StudentActiveCourseExam answer = get(id);
				if(ExStringUtils.isNotBlank(answer.getAnswer())){
					answer.setResult(null);
					answer.setIsCorrect(null);
				} else {
					truncate(answer);
				}
			}
		}		
	}
	
	@Override
	public void scoreActiveCourseExam(String[] ids) throws ServiceException {
		if(ids != null && ids.length>0){
			Map<String, Object> values = new HashMap<String, Object>();
			values.put("courseExamIds", Arrays.asList(ids));
			
			List<StudentActiveCourseExam> list = findByHql("from "+StudentActiveCourseExam.class.getSimpleName()+" where isDeleted=0 and activeCourseExam.resourceid in (:courseExamIds) ", values);
			for (StudentActiveCourseExam studentActiveCourseExam : list) {
				studentActiveCourseExam.setIsCorrect(Constants.BOOLEAN_YES);
			}
		}
	}
	
	@Override
	public void saveAllStudentActiveCourseExam(List<StudentActiveCourseExam> currentlist,StudentInfo studentInfo,String syllabusId,String type) throws ServiceException {
		batchSaveOrUpdate(currentlist);
		
		if("submit_done".equals(type)){		//提交已做试题	
			saveOrUpdateHasDoneCourseExam(studentInfo, syllabusId);
		} else if("submit_all".equals(type)){//提交所有
			String hql = " from "+ActiveCourseExam.class.getSimpleName()+" t where t.isDeleted=0 and t.syllabus.resourceid=? and t.isPublished='Y' ";
			hql += " and not exists ( from "+StudentActiveCourseExam.class.getSimpleName()+" s where s.isDeleted=0 and s.activeCourseExam.resourceid=t.resourceid and s.studentInfo.resourceid=?) ";
			List<ActiveCourseExam> list = (List<ActiveCourseExam>) exGeneralHibernateDao.findByHql(hql, syllabusId,studentInfo.getResourceid());
			List<StudentActiveCourseExam> answers = new ArrayList<StudentActiveCourseExam>();
			for (ActiveCourseExam activeCourseExam : list) {
				StudentActiveCourseExam stuActiveCourseExam = new StudentActiveCourseExam();							
				stuActiveCourseExam.setActiveCourseExam(activeCourseExam);
				stuActiveCourseExam.setStudentInfo(studentInfo);
				stuActiveCourseExam.setShowOrder(activeCourseExam.getShowOrder());	
				stuActiveCourseExam.setAnswer("");	
				stuActiveCourseExam.setIsCorrect(Constants.BOOLEAN_NO);	
				stuActiveCourseExam.setResult(0.0);
				stuActiveCourseExam.setAnswerTime(new Date());
				
				answers.add(stuActiveCourseExam);
			}
			batchSaveOrUpdate(answers);
			saveOrUpdateHasDoneCourseExam(studentInfo, syllabusId);
		}		
	}
	private void saveOrUpdateHasDoneCourseExam(StudentInfo studentInfo, String syllabusId) {
		List<StudentActiveCourseExam> stuExams = findByHql("from "+StudentActiveCourseExam.class.getSimpleName()+" s where s.isDeleted=0 and s.activeCourseExam.syllabus.resourceid=? and s.studentInfo.resourceid=? and s.result is null ",syllabusId,studentInfo.getResourceid());
		for (StudentActiveCourseExam exam : stuExams) {
			String stu_answer = ExStringUtils.trimToEmpty(exam.getAnswer());
			String answer = ExStringUtils.trimToEmpty(exam.getActiveCourseExam().getCourseExam().getAnswer());
			if("3".equals(exam.getActiveCourseExam().getCourseExam().getExamType())){//判断题
				stu_answer = CourseExam.covertToCorrectAnswer(stu_answer);
				answer = CourseExam.covertToCorrectAnswer(answer);
			} 
			if(stu_answer.equalsIgnoreCase(answer)){
				exam.setIsCorrect(Constants.BOOLEAN_YES);	
				Long exam_score = exam.getActiveCourseExam().getScore();
				exam.setResult((exam_score!=null)?exam_score.doubleValue():0.0);	
			} else {
				exam.setIsCorrect(Constants.BOOLEAN_NO);	
				exam.setResult(0.0);
			}
			exam.setAnswerTime(new Date());
		}
		batchSaveOrUpdate(stuExams);
	}
}
