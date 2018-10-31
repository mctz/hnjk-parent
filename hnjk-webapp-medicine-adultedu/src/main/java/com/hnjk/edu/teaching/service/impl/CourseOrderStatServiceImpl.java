package com.hnjk.edu.teaching.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.teaching.model.CourseOrderStat;
import com.hnjk.edu.teaching.service.ICourseOrderStatService;
import com.hnjk.security.service.IUserService;

/**
 * 学生预约情况统计表，主要用来生成教学任务书。
 * <code>CourseOrderStatServiceImpl</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2010-7-19 上午11:47:25
 * @see 
 * @version 1.0
 */
@Transactional
@Service("courseorderstatservice")
public class CourseOrderStatServiceImpl extends BaseServiceImpl<CourseOrderStat> implements ICourseOrderStatService {

//	@Autowired
//	@Qualifier("teachtaskservice")
//	private ITeachTaskService teachTaskService;
	
	@Autowired
	@Qualifier("userService")
	private IUserService userService;
	
	@Override
	@Transactional(readOnly=true)
	public Page findCourseOrderStatByCondition(Map<String, Object> condition, Page objPage) {
		Map<String,Object> values =  new HashMap<String, Object>();
		String hql = " from "+CourseOrderStat.class.getSimpleName()+" where 1=1 ";
		hql += " and isDeleted = :isDeleted ";
		values.put("isDeleted", 0);
		
		if(condition.containsKey("yearInfoid")){
			hql += " and yearInfo.resourceid = :yearInfoid ";
			values.put("yearInfoid", condition.get("yearInfoid"));
		}
		if(condition.containsKey("term")){
			hql += " and term = :term ";
			values.put("term", condition.get("term"));
		}
		if(condition.containsKey("courseid")){
			hql += " and course.resourceid = :courseid ";
			values.put("courseid", condition.get("courseid"));
		}
		if(condition.containsKey("courseName")){
			hql += " and lower(course.courseName) like :courseName ";
			values.put("courseName", "%"+condition.get("courseName").toString().toLowerCase()+"%");
		}
		if(condition.containsKey("generatorFlag")){
			hql += " and generatorFlag = :generatorFlag ";
			values.put("generatorFlag", condition.get("generatorFlag"));
		}
//		if(condition.containsKey("teacher")){
//			hql += " and course.defaultTeacherName like :teacher ";
//			values.put("teacher", condition.get("teacher")+"%");
//		}
	
		hql += " order by "+objPage.getOrderBy() +" "+ objPage.getOrder();
		return exGeneralHibernateDao.findByHql(objPage, hql, values);
	}

//	@Override
//	public void saveCollections(List<CourseOrderStat> list,List<TeachTask> taskList) {
//		exGeneralHibernateDao.saveOrUpdateCollection(list);
//		exGeneralHibernateDao.saveOrUpdateCollection(taskList);
//	}
	
	@Override
	public CourseOrderStat findCourseOrderStatByYearInfoAndCourse(String yearInfo, String courseId, String term) throws ServiceException {
		
		StringBuffer hql = new StringBuffer();
		hql.append(" from CourseOrderStat stat where stat.isDeleted=0");
		hql.append(" and stat.yearInfo.resourceid=?");
		hql.append(" and stat.course.resourceid=?");
		hql.append(" and stat.term=?");
		
		List <CourseOrderStat> list = (List<CourseOrderStat>) this.exGeneralHibernateDao.findByHql(hql.toString(),new String []{yearInfo,courseId,term} );
		if (null!=list&&!list.isEmpty()) {
			return list.get(0);
		}else {
			return null;
		}
	}


	@Override
	public void batchSaveOrUpdateCourseOrdersAndTeachTask(String[] resourceids) throws ServiceException {
//		if(resourceids!=null&&resourceids.length>0){
//			List<CourseOrderStat> list = new ArrayList<CourseOrderStat>(0);
//			List<TeachTask> taskList = new ArrayList<TeachTask>(0);
//			for (String id : resourceids) {
//				CourseOrderStat stat = get(id);
//				if(Constants.BOOLEAN_NO.equalsIgnoreCase(stat.getGeneratorFlag())){//未生成
//					stat.setGeneratorFlag(Constants.BOOLEAN_YES);
//					list.add(stat);
//					
//					TeachTask task = new TeachTask();
//					task.setCourse(stat.getCourse());
//					task.setTerm(stat.getTerm());
//					task.setYearInfo(stat.getYearInfo());
//					task.setReturnTime(ExDateUtils.addDays(new Date(), 7));
//					task.setTaskStatus(0l); //生成状态
//					
//					TeachTask lastTask = teachTaskService.findLastTeachTask(stat.getCourse().getResourceid());
//					if(lastTask!=null){
//						Map<String, String> map = excludeDisabledTeacherIds(lastTask);
//						
//						task.setTeacherId(map.get("teacherId"));
//						task.setTeacherName(map.get("teacherName"));
//						task.setDefaultTeacherIds(map.get("defaultTeacherIds"));
//						task.setDefaultTeacherNames(map.get("defaultTeacherNames"));
//						task.setAssistantIds(map.get("assistantIds"));
//						task.setAssistantNames(map.get("assistantNames"));
//					}
//					
//					//模板
//					TeachTask taskTemplate = teachTaskService.findTeachTaskTemplate(stat.getYearInfo().getResourceid(), stat.getTerm());
//					if(taskTemplate != null){
//						for (TeachTaskDetails d : taskTemplate.getTeachTaskDetails()) {
//							TeachTaskDetails detail = new TeachTaskDetails();
//							ExBeanUtils.copyProperties(detail, d);
//							detail.setResourceid(null);
//							detail.setTeachTask(task);
//							detail.setStatus("unfinished");
//							
//							task.getTeachTaskDetails().add(detail);
//						}
//					} else {
//						throw new ServiceException("请先设置"+stat.getYearInfo().getYearName()+JstlCustomFunction.dictionaryCode2Value("CodeTerm", stat.getTerm())+"的教学任务书模板");
//					}
//					
//					taskList.add(task);
//				}
//			}
//			saveCollections(list,taskList);	
//		}
	}

//	private Map<String, String> excludeDisabledTeacherIds(TeachTask lastTask) {
//		Map<String, String> map = new HashMap<String, String>();
//		if(ExStringUtils.isNotBlank(lastTask.getTeacherId())){
//			User user = userService.get(lastTask.getTeacherId());
//			if(user != null && user.getIsDeleted()!=1 && user.isEnabled()){//可用
//				map.put("teacherId", user.getResourceid());
//				map.put("teacherName", user.getCnName());
//			}
//		}
//		if(ExStringUtils.isNotBlank(lastTask.getAssistantIds())){
//			String assistantIds = "";
//			String assistantNames = "";
//			String[] aids = lastTask.getAssistantIds().split("\\,");
//			if(aids != null && aids.length>0){
//				for (String aid : aids) {
//					if(ExStringUtils.isNotBlank(aid)){
//						User user = userService.get(aid);
//						if(user != null && user.getIsDeleted()!=1 && user.isEnabled()){//可用
//							assistantIds += user.getResourceid()+",";
//							assistantNames += user.getCnName()+",";
//						}
//					}									
//				}
//			}
//			map.put("assistantIds", assistantIds);
//			map.put("assistantNames", assistantNames);
//		}						
//		if(ExStringUtils.isNotBlank(lastTask.getDefaultTeacherIds())){
//			String defaultTeacherIds = "";
//			String defaultTeacherNames = "";
//			String[] dids = lastTask.getDefaultTeacherIds().split("\\,");
//			if(dids != null && dids.length>0){
//				for (String did : dids) {
//					if(ExStringUtils.isNotBlank(did)){
//						User user = userService.get(did);
//						if(user != null && user.getIsDeleted()!=1 && user.isEnabled()){//可用
//							defaultTeacherIds += user.getResourceid()+",";
//							defaultTeacherNames += user.getCnName()+",";
//						}
//					}									
//				}
//			}
//			map.put("defaultTeacherIds", defaultTeacherIds);
//			map.put("defaultTeacherNames", defaultTeacherNames);
//		}
//		return map;
//	}

}
