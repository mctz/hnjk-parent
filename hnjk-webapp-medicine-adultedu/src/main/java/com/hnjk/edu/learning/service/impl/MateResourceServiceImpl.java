package com.hnjk.edu.learning.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.ExBeanUtils;
import com.hnjk.core.foundation.utils.ExCollectionUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.learning.model.ActiveCourseExam;
import com.hnjk.edu.learning.model.CourseExam;
import com.hnjk.edu.learning.model.CourseLearningGuid;
import com.hnjk.edu.learning.model.CourseMockTest;
import com.hnjk.edu.learning.model.CourseOverview;
import com.hnjk.edu.learning.model.CourseReference;
import com.hnjk.edu.learning.model.MateResource;
import com.hnjk.edu.learning.service.IActiveCourseExamService;
import com.hnjk.edu.learning.service.ICourseExamService;
import com.hnjk.edu.learning.service.ICourseLearningGuidService;
import com.hnjk.edu.learning.service.ICourseMockTestService;
import com.hnjk.edu.learning.service.ICourseOverviewService;
import com.hnjk.edu.learning.service.ICourseReferenceService;
import com.hnjk.edu.learning.service.IMateResourceService;
import com.hnjk.edu.teaching.model.Syllabus;
import com.hnjk.edu.teaching.service.ICourseService;
import com.hnjk.edu.teaching.service.ISyllabusService;

/**
 * 课程素材管理服务接口实现
 * <code>MateResourceServiceImpl</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-8-17 下午05:14:48
 * @see 
 * @version 1.0
 */
@Transactional
@Service("mateResourceService")
public class MateResourceServiceImpl extends BaseServiceImpl<MateResource> implements IMateResourceService {

	@Autowired
	@Qualifier("courseService")
	private ICourseService courseService;//课程
	
	@Autowired
	@Qualifier("syllabusService")
	private ISyllabusService syllabusService;//知识结构树
	
	@Autowired
	@Qualifier("courseReferenceService")
	private ICourseReferenceService courseReferenceService;//参考资料
	
	@Autowired
	@Qualifier("courseLearningGuidService")
	private ICourseLearningGuidService courseLearningGuidService;//学习目标
	
	@Autowired
	@Qualifier("activeCourseExamService")
	private IActiveCourseExamService activeCourseExamService;//随堂练习
	
	@Autowired
	@Qualifier("courseMockTestService")
	private ICourseMockTestService courseMockTestService;//模拟试题
	
	@Autowired
	@Qualifier("courseOverviewService")
	private ICourseOverviewService courseOverviewService;//课程概况
	
	@Autowired
	@Qualifier("courseExamService")
	private ICourseExamService courseExamService;
	
	@Override
	@Transactional(readOnly=true)
	public Page findMateResourceByCondition(Map<String, Object> condition, Page objPage) throws ServiceException {
		Map<String,Object> values =  new HashMap<String, Object>();
		String hql = " from "+MateResource.class.getName()+" where 1=1 ";
		hql += " and isDeleted = :isDeleted ";
		values.put("isDeleted", 0);
		
		if(condition.containsKey("mateName")){//材料名称
			hql += " and lower(mateName) like :mateName ";
			values.put("mateName", "%"+condition.get("mateName").toString().toLowerCase()+"%");
		}
		if(condition.containsKey("mateCode")){//材料编码
			hql += " and mateCode=:mateCode ";
			values.put("mateCode", condition.get("mateCode"));
		}
		if(condition.containsKey("mateType")){//材料类型
			hql += " and mateType =:mateType ";
			values.put("mateType", condition.get("mateType"));
		}
		if(condition.containsKey("mateTypes")){
			hql += " and mateType in ("+condition.get("mateTypes")+") ";
		}
		if(condition.containsKey("syllabusId")){//知识节点
			hql += " and syllabus.resourceid =:syllabusId ";
			values.put("syllabusId", condition.get("syllabusId"));
		}	
		if(condition.containsKey("courseId")){//所属课程
			hql += " and syllabus.course.resourceid =:courseId ";
			values.put("courseId", condition.get("courseId"));
		}
		if(condition.containsKey("channelType")){
			hql += " and channelType =:channelType ";
			values.put("channelType", condition.get("channelType"));
		}
		if(condition.containsKey("reviseCourseId")){//教师总结录像课程
			hql += " and course.resourceid =:reviseCourseId ";
			values.put("reviseCourseId", condition.get("reviseCourseId"));
		}
		if(condition.containsKey("isPublished")){//是否发布
			hql += " and isPublished =:isPublished ";
			values.put("isPublished", condition.get("isPublished"));
		}
		hql += " order by "+objPage.getOrderBy() +" "+ objPage.getOrder();
		return exGeneralHibernateDao.findByHql(objPage, hql, values);
	}

	@Override
	public void batchCascadeDelete(String[] ids) throws ServiceException {
		if(ids!=null && ids.length>0){
			for(String id : ids){
				delete(id);	
				logger.info("删除课程素材="+id);
			}
		}
	}

	@Override
	@Transactional(readOnly=true)
	public boolean isExistsMateCode(String mateCode) throws ServiceException {
		List<MateResource> mate= findByHql(" from "+ MateResource.class.getSimpleName()+" where isDeleted=0 and mateCode=? ", mateCode);
		return (mate != null && mate.size()>0) ? true : false;
	}

	@Override
	@Transactional(readOnly=true)
	public List<MateResource> findMateResourceByCondition(Map<String, Object> condition) throws ServiceException {
		Map<String,Object> values =  new HashMap<String, Object>();
		String hql = " from "+MateResource.class.getName()+" where 1=1 ";
		hql += " and isDeleted = :isDeleted ";
		values.put("isDeleted", 0);
		
		if(condition.containsKey("mateType")){//材料类型
			hql += " and mateType =:mateType ";
			values.put("mateType", condition.get("mateType"));
		}
		if(condition.containsKey("syllabusId")){//知识节点
			hql += " and syllabus.resourceid =:syllabusId ";
			values.put("syllabusId", condition.get("syllabusId"));
		}	
		if(condition.containsKey("courseId")){//所属课程
			hql += " and syllabus.course.resourceid =:courseId ";
			values.put("courseId", condition.get("courseId"));
		}
		if(condition.containsKey("channelType")){
			hql += " and channelType =:channelType ";
			values.put("channelType", condition.get("channelType"));
		}
		if(condition.containsKey("reviseCourseId")){//教师总结录像课程
			hql += " and course.resourceid =:reviseCourseId ";
			values.put("reviseCourseId", condition.get("reviseCourseId"));
		}
		if(condition.containsKey("mateTypes")){
			hql += " and mateType in("+condition.get("mateTypes")+") ";
		}
		if(condition.containsKey("orderBy")){
			hql += " order by  "+condition.get("orderBy");
		}
		if(condition.containsKey("courseIds")){//所属课程s
			hql += " and syllabus.course.resourceid in (:courseIds) ";
			values.put("courseIds", condition.get("courseIds"));
		}
		return findByHql(hql,values);
	}
	
	@Override
	public void copyCourse(String fromCourseId,String toCourseId) throws ServiceException {
		//第1步：复制课程
//		Course oldCourse = courseService.get(fromCourseId);
//		Course course = new Course();
//		ExBeanUtils.copyProperties(course, oldCourse);
//		course.setResourceid(null);
//		course.setCourseCode(oldCourse.getCourseCode()+"_copy");
//		course.setCourseName(oldCourse.getCourseName()+"（复制）");
//		course.setStatus(2l);//停用
//		course.setStopTime(new Date());		
//		courseService.save(course);		
		
		Course course = courseService.get(toCourseId);
		//第1步：清除已有的课件资源
		List<Syllabus> oldSyllabusList = syllabusService.findSyllabusTreeList(toCourseId);
		if(!ExCollectionUtils.isEmpty(oldSyllabusList)){
			syllabusService.batchDelete(oldSyllabusList);
		}
		List<ActiveCourseExam> oldCourseExamList = activeCourseExamService.findByHql(" from "+ActiveCourseExam.class.getSimpleName()+" where isDeleted=0 and syllabus.course.resourceid=? ", toCourseId);
		if(!ExCollectionUtils.isEmpty(oldCourseExamList)){
			for (ActiveCourseExam activeCourseExam : oldCourseExamList) {
				activeCourseExam.getCourseExam().setIsDeleted(1);
			}
			activeCourseExamService.batchDelete(oldCourseExamList);
		}
		List<MateResource> oldMateResourceList = findByHql(" from "+MateResource.class.getSimpleName()+" where isDeleted=0 and syllabus.course.resourceid=? ", toCourseId);
		if(!ExCollectionUtils.isEmpty(oldMateResourceList)){
			batchDelete(oldMateResourceList);
		}
		List<CourseLearningGuid> oldCourseLearningGuidList = courseLearningGuidService.findByHql(" from "+CourseLearningGuid.class.getSimpleName()+" where isDeleted=0 and syllabus.course.resourceid=? ", toCourseId);
		if(!ExCollectionUtils.isEmpty(oldCourseLearningGuidList)){
			courseLearningGuidService.batchDelete(oldCourseLearningGuidList);
		}
		List<CourseReference> oldCourseReferenceList= courseReferenceService.findByHql(" from "+CourseReference.class.getSimpleName()+" where isDeleted=0 and course.resourceid=? ", toCourseId);
		if(!ExCollectionUtils.isEmpty(oldCourseReferenceList)){
			courseReferenceService.batchDelete(oldCourseReferenceList);
		}
		List<CourseOverview> oldCourseOverviewList = courseOverviewService.findByHql("from "+CourseOverview.class.getSimpleName()+" where isDeleted=? and course.resourceid=? ", 0,toCourseId);
		if(!ExCollectionUtils.isEmpty(oldCourseOverviewList)){
			courseOverviewService.batchDelete(oldCourseOverviewList);
		}
		List<CourseMockTest> oldCourseMockTestList = courseMockTestService.findByHql("from "+CourseMockTest.class.getSimpleName()+" where isDeleted=? and course.resourceid=? ", 0,toCourseId);
		if(!ExCollectionUtils.isEmpty(oldCourseMockTestList)){
			courseMockTestService.batchDelete(oldCourseMockTestList);
		}
		
		//第2步：知识结构树
		List<Syllabus> oldSyllabuses = syllabusService.findSyllabusTreeList(fromCourseId);
		Map<String, Syllabus> syllabusMap = new HashMap<String, Syllabus>();//旧id对应的复制对象
		if(oldSyllabuses != null && !oldSyllabuses.isEmpty()){
			for (Syllabus oldSyllabus : oldSyllabuses) {
				Syllabus syllabus = new Syllabus();
				ExBeanUtils.copyProperties(syllabus, oldSyllabus);
				syllabus.setResourceid(null);
				syllabus.setCourse(course);
				syllabus.setChildren(new HashSet<Syllabus>(0));
				if(oldSyllabus.getParent()!=null){
					Syllabus parent = syllabusMap.get(oldSyllabus.getParent().getResourceid());
					syllabus.setParent(parent);//设置父节点
					parent.getChildren().add(syllabus);//父增加子
				} else {
					syllabus.setSyllabusName(course.getCourseName());
				}
				
				syllabusService.saveOrUpdate(syllabus);
				syllabusMap.put(oldSyllabus.getResourceid(), syllabus);
			}
			course.setHasResource(Constants.BOOLEAN_YES);//更改为有课件
		}
		//第3步:随堂练习
		List<ActiveCourseExam> oldActiveCourseExams = activeCourseExamService.findByHql(" from "+ActiveCourseExam.class.getSimpleName()+" where isDeleted=0 and syllabus.course.resourceid=? ", fromCourseId);
		List<ActiveCourseExam> activeCourseExams = new ArrayList<ActiveCourseExam>();
		List<CourseExam> courseExams = new ArrayList<CourseExam>();		
		for (ActiveCourseExam oldActiveCourseExam : oldActiveCourseExams) {
			CourseExam courseExam = new CourseExam();
			ExBeanUtils.copyProperties(courseExam, oldActiveCourseExam.getCourseExam());
			courseExam.setResourceid(null);
			courseExam.setCourse(course);
			
			courseExams.add(courseExam);
			
			ActiveCourseExam activeCourseExam = new ActiveCourseExam();
			ExBeanUtils.copyProperties(activeCourseExam, oldActiveCourseExam);
			activeCourseExam.setResourceid(null);
			activeCourseExam.setCourseExam(courseExam);
			activeCourseExam.setReferSyllabusTreeIds(null);
			activeCourseExam.setReferSyllabusTreeNames(null);
			activeCourseExam.setSyllabus(syllabusMap.get(oldActiveCourseExam.getSyllabus().getResourceid()));
			
			activeCourseExams.add(activeCourseExam);
		}
		courseExamService.batchSaveOrUpdate(courseExams);
		activeCourseExamService.batchSaveOrUpdate(activeCourseExams);
		
		//第4步：学习材料
		List<MateResource> oldMateResources = findByHql(" from "+MateResource.class.getSimpleName()+" where isDeleted=0 and syllabus.course.resourceid=? ", fromCourseId);
		List<MateResource> mateResources = new ArrayList<MateResource>();
		for (MateResource oldMateResource : oldMateResources) {
			MateResource mateResource = new MateResource();
			ExBeanUtils.copyProperties(mateResource, oldMateResource);
			mateResource.setResourceid(null);
			mateResource.setSyllabus(syllabusMap.get(oldMateResource.getSyllabus().getResourceid()));
			
			mateResources.add(mateResource);
		}
		batchSaveOrUpdate(mateResources);
		
		//第5步：学习目标
		List<CourseLearningGuid> oldCourseLearningGuids = courseLearningGuidService.findByHql(" from "+CourseLearningGuid.class.getSimpleName()+" where isDeleted=0 and syllabus.course.resourceid=? ", fromCourseId);
		List<CourseLearningGuid> courseLearningGuids = new ArrayList<CourseLearningGuid>();
		for (CourseLearningGuid oldCourseLearningGuid : oldCourseLearningGuids) {
			CourseLearningGuid courseLearningGuid = new CourseLearningGuid();
			ExBeanUtils.copyProperties(courseLearningGuid, oldCourseLearningGuid);
			courseLearningGuid.setResourceid(null);
			courseLearningGuid.setSyllabus(syllabusMap.get(oldCourseLearningGuid.getSyllabus().getResourceid()));
			
			courseLearningGuids.add(courseLearningGuid);
		}
		courseLearningGuidService.batchSaveOrUpdate(courseLearningGuids);
		
		//第6步:参考资料
		List<CourseReference> oldCourseReferences = courseReferenceService.findByHql(" from "+CourseReference.class.getSimpleName()+" where isDeleted=0 and course.resourceid=? ", fromCourseId);
		List<CourseReference> courseReferences = new ArrayList<CourseReference>();
		for (CourseReference oldCourseReference : oldCourseReferences) {
			CourseReference courseReference = new CourseReference();
			ExBeanUtils.copyProperties(courseReference, oldCourseReference);
			courseReference.setResourceid(null);
			courseReference.setCourse(course);
			if(oldCourseReference.getSyllabus()!=null){
				courseReference.setSyllabus(syllabusMap.get(oldCourseReference.getSyllabus().getResourceid()));
			}			
			
			courseReferences.add(courseReference);
		}
		courseReferenceService.batchSaveOrUpdate(courseReferences);
		
		//第7步:课程概况
		List<CourseOverview> oldCourseOverviews = courseOverviewService.findByHql("from "+CourseOverview.class.getSimpleName()+" where isDeleted=? and course.resourceid=? ", 0,fromCourseId);
		List<CourseOverview> courseOverviews = new ArrayList<CourseOverview>();
		for (CourseOverview oldCourseOverview : oldCourseOverviews) {
			CourseOverview courseOverview = new CourseOverview();
			ExBeanUtils.copyProperties(courseOverview, oldCourseOverview);
			courseOverview.setResourceid(null);
			courseOverview.setCourse(course);
			
			courseOverviews.add(courseOverview);
		}
		courseOverviewService.batchSaveOrUpdate(courseOverviews);
		
		//第8步：模拟试题
		List<CourseMockTest> oldCourseMockTests = courseMockTestService.findByHql("from "+CourseMockTest.class.getSimpleName()+" where isDeleted=? and course.resourceid=? ", 0,fromCourseId);
		List<CourseMockTest> courseMockTests = new ArrayList<CourseMockTest>();
		for (CourseMockTest oldCourseMockTest : oldCourseMockTests) {
			CourseMockTest courseMockTest = new CourseMockTest();
			ExBeanUtils.copyProperties(courseMockTest, oldCourseMockTest);
			courseMockTest.setResourceid(null);
			courseMockTest.setCourse(course);
			
			courseMockTests.add(courseMockTest);
		}
		courseMockTestService.batchSaveOrUpdate(courseMockTests);
		//结束
	}
	
	@Override
	public Integer getNextShowOrder(String id, String type) throws ServiceException {
		String hql = "select max(c.showOrder) from "+MateResource.class.getSimpleName()+" c where c.isDeleted=0 ";
		hql += "meta".equals(type)?" and c.syllabus.resourceid=? ":" and c.course.resourceid=? ";
		Integer showOrder = exGeneralHibernateDao.findUnique(hql, id);
		if(showOrder==null) {
			showOrder = 0;
		}
		return ++showOrder;
	}
}
