package com.hnjk.edu.learning.service.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.learning.model.CourseExam;
import com.hnjk.edu.learning.model.CourseExamPaperDetails;
import com.hnjk.edu.learning.model.CourseExamPapers;
import com.hnjk.edu.learning.model.CourseExamRules;
import com.hnjk.edu.learning.model.CourseExamRulesDetails;
import com.hnjk.edu.learning.service.ICourseExamPapersService;
import com.hnjk.edu.learning.service.ICourseExamService;
/**
 * 试卷管理服务接口实现.
 * <code>CourseExamPapersServiceImpl</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2011-4-13 上午11:11:54
 * @see 
 * @version 1.0
 */
@Transactional
@Service("courseExamPapersService")
public class CourseExamPapersServiceImpl extends BaseServiceImpl<CourseExamPapers> implements ICourseExamPapersService {

	@Autowired
	@Qualifier("courseExamService")
	private ICourseExamService courseExamService;
	
	@Override
	@Transactional(readOnly=true)
	public Page findCourseExamPapersByCondition(Map<String, Object> condition,Page objPage) throws ServiceException {
		Map<String,Object> values =  new HashMap<String, Object>();
		String hql = " from "+CourseExamPapers.class.getSimpleName()+" where 1=1 ";
		hql += " and isDeleted = :isDeleted ";
		values.put("isDeleted", 0);
		
		if(condition.containsKey("paperName")){//试卷名称
			hql += " and lower(paperName) like :paperName ";
			values.put("paperName", "%"+condition.get("paperName").toString().toLowerCase()+"%");
		}
		if(condition.containsKey("paperType")){//试卷类型
			hql += " and paperType=:paperType ";
			values.put("paperType", condition.get("paperType"));
		}
		if(condition.containsKey("courseId")){//关联课程
			hql += " and course.resourceid =:courseId ";
			values.put("courseId", condition.get("courseId"));
		}
		if(condition.containsKey("courseName")){//课程名
			hql += " and courseName =:courseName ";
			values.put("courseName", condition.get("courseName"));
		}
		if(condition.containsKey("classicid")){//层次
			hql += " and classic.resourceid =:classicid ";
			values.put("classicid", condition.get("classicid"));
		}
		if(condition.containsKey("isOpened")){//是否开放
			hql += " and isOpened=:isOpened ";
			values.put("isOpened", condition.get("isOpened"));
		}
		hql += " order by "+objPage.getOrderBy() +" "+ objPage.getOrder();
		return exGeneralHibernateDao.findByHql(objPage, hql, values);
	}
	
	@Override
	public CourseExamPapers fetchCourseExamPapersByRandom(CourseExamRules rule,String paperType,String paperName) throws ServiceException {
		CourseExamPapers paper = new CourseExamPapers();
		paper.setPaperType(paperType);
		paper.setPaperName(paperName);
		paper.setCourseName(rule.getCourseName());
		paper.setIsOpened(Constants.BOOLEAN_NO);
		paper.setPaperTime(rule.getExamTimeLong().longValue());
		paper.setFillinDate(new Date());
		paper.setCourse(rule.getCourse());
		
		Map<String,Object> condition = new HashMap<String,Object>();
		if(Constants.BOOLEAN_YES.equals(rule.getIsEnrolExam())){//入学考试
			condition.put("isEnrolExam", Constants.BOOLEAN_YES);
			condition.put("courseName", paper.getCourseName());
			condition.put("notInExamType", "('4','5')");
		} else {
			condition.put("isEnrolExam", Constants.BOOLEAN_NO);
			condition.put("courseId", paper.getCourse().getResourceid());
			if(ExStringUtils.isNotBlank(rule.getPaperSourse())){
				condition.put("examforms", Arrays.asList(rule.getPaperSourse().split("\\,")));
			}			
		}
		int nextShowOrder = 1;
		for (CourseExamRulesDetails r : rule.getCourseExamRulesDetails()) {
			Integer examnum = r.getExamNum();
			Double score = r.getExamValue();
			if(examnum == null || examnum <= 0 || score == null || score <= 0.0){
				throw new WebException("成卷规则不符合要求");
			}
			if(examnum > 0 && r.getExamValue() > 0){
				if(ExStringUtils.isNotBlank(r.getExamNodeType())){
					condition.put("examNodeType", r.getExamNodeType());
				}
				condition.put("examType",r.getExamType());
				List<CourseExam> list = courseExamService.findCourseExamByCondition(condition);
				
				if(null != list){
					if(examnum > list.size()){
						throw new WebException("成卷规则不符合要求");
					}
					Collections.shuffle(list);//打乱顺序，取前examnum个
					for (int j = 0; j < examnum; j++) {
						CourseExamPaperDetails detail = new CourseExamPaperDetails();
						detail.setCourseExam(list.get(j));
						detail.setCourseExamPapers(paper);
						detail.setShowOrder(nextShowOrder++);
						detail.setScore(score);
							
						paper.getCourseExamPaperDetails().add(detail);
						
						if(CourseExam.COMPREHENSION.equals(list.get(j).getExamType())){//材料题
							Set<CourseExam> set = list.get(j).getChilds();
							if(set.size()>0){
								double childscore = score / set.size();
								for (CourseExam child : set) {
									CourseExamPaperDetails d = new CourseExamPaperDetails();
									d.setCourseExam(child);
									d.setCourseExamPapers(paper);
									d.setShowOrder(nextShowOrder++);
									d.setScore(childscore);
									
									paper.getCourseExamPaperDetails().add(d);
								}
							}
						}
						
					}
				}
			}
		}
		return paper;
	}

	@Override
	public CourseExamPapers fetchCourseExamPapersByRandom(CourseExamRules rule,	String paperType, String paperName,	Map<String, List<CourseExam>> courseExamMap) throws ServiceException {
		CourseExamPapers paper = new CourseExamPapers();
		paper.setPaperType(paperType);
		paper.setPaperName(paperName);
		paper.setCourseName(rule.getCourseName());
		paper.setIsOpened(Constants.BOOLEAN_NO);
		paper.setPaperTime(rule.getExamTimeLong().longValue());
		paper.setFillinDate(new Date());
		paper.setCourse(rule.getCourse());
		
		int nextShowOrder = 1;
		for (CourseExamRulesDetails r : rule.getCourseExamRulesDetails()) {
			Integer examnum = r.getExamNum();
			Double score = r.getExamValue();
			if(examnum == null || examnum <= 0 || score == null || score <= 0.0){
				throw new WebException("成卷规则不符合要求");
			}
			if(examnum > 0 && r.getExamValue() > 0){				
				List<CourseExam> list = courseExamMap.get(r.getResourceid());
				
				if(null != list){
					if(examnum > list.size()){
						throw new WebException("成卷规则不符合要求");
					}
					Collections.shuffle(list);//打乱顺序，取前examnum个
					for (int j = 0; j < examnum; j++) {
						CourseExamPaperDetails detail = new CourseExamPaperDetails();
						detail.setCourseExam(list.get(j));
						detail.setCourseExamPapers(paper);
						detail.setShowOrder(nextShowOrder++);
						detail.setScore(score);
							
						paper.getCourseExamPaperDetails().add(detail);
						
						if(CourseExam.COMPREHENSION.equals(list.get(j).getExamType())){//材料题
							Set<CourseExam> set = list.get(j).getChilds();
							if(set.size()>0){
								double childscore = score / set.size();
								for (CourseExam child : set) {
									CourseExamPaperDetails d = new CourseExamPaperDetails();
									d.setCourseExam(child);
									d.setCourseExamPapers(paper);
									d.setShowOrder(nextShowOrder++);
									d.setScore(childscore);
									
									paper.getCourseExamPaperDetails().add(d);
								}
							}
						}
						
					}
				}
			}
		}
		return paper;
	}
}
