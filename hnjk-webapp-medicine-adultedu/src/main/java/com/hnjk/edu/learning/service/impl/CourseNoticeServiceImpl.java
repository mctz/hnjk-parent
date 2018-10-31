package com.hnjk.edu.learning.service.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.learning.model.CourseNotice;
import com.hnjk.edu.learning.service.ICourseNoticeService;
//import com.hnjk.edu.teaching.model.TeachTask;
import com.hnjk.platform.system.service.IAttachsService;

/**
 * 课程公告服务接口实现 
 * <code>CourseNoticeServiceImpl</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-8-24 上午09:47:54
 * @see 
 * @version 1.0
 */
@Transactional
@Service("courseNoticeService")
public class CourseNoticeServiceImpl extends BaseServiceImpl<CourseNotice> implements ICourseNoticeService {

	@Autowired
	@Qualifier("attachsService")
	private IAttachsService attachsService;
	
	@Override
	@Transactional(readOnly=true)
	public Page findCourseNoticeByCondition(Map<String, Object> condition, Page objPage) throws ServiceException {
		Map<String,Object> values =  new HashMap<String, Object>();
		String hql = " from "+CourseNotice.class.getName()+" c where 1=1 ";
		hql += " and c.isDeleted = :isDeleted ";
		values.put("isDeleted", 0);
		
		if(condition.containsKey("noticeTitle")){//通知标题
			hql += " and c.noticeTitle like :noticeTitle ";
			values.put("noticeTitle", "%"+condition.get("noticeTitle")+"%");
		}
		if(condition.containsKey("courseId")){//所属课程
			hql += " and c.course.resourceid =:courseId ";
			values.put("courseId", condition.get("courseId"));
		}
		if(condition.containsKey("yearInfoId")){//年度
			hql += " and c.yearInfo.resourceid =:yearInfoId ";
			values.put("yearInfoId", condition.get("yearInfoId"));
		}
		if(condition.containsKey("term")){//学期
			hql += " and c.term =:term ";
			values.put("term", condition.get("term"));
		}
		if(condition.containsKey("classesId")){//班级
			hql += " and (c.classes.resourceid =:classesId or c.classes.resourceid is null) ";
			values.put("classesId", condition.get("classesId"));
		}
		if(condition.containsKey("teacherId") && condition.containsKey("classesIds") ){//课程老师
			hql += " and c.course.resourceid in (select tpct.course.resourceid from TeachingPlanCourseTimetable tpct where tpct.isDeleted=0 and (tpct.teacherId=:teacherId or tpct.classesid in (:classesIds))) ";
			values.put("teacherId", condition.get("teacherId"));
			values.put("classesIds", Arrays.asList(((String)condition.get("classesIds")).split(",")));
//			hql += " and exists ( from "+TeachTask.class.getSimpleName()+" t where t.isDeleted=0 and t.taskStatus=3 and t.yearInfo.resourceid=c.yearInfo.resourceid and t.term=c.term and t.course.resourceid=c.course.resourceid and (t.teacherId like :teacherId or t.assistantIds like :teacherId ) ) ";
//			values.put("teacherId", "%"+condition.get("teacherId")+"%");//老师
		}else {
			if(condition.containsKey("teacherId")){
				hql += " and c.course.resourceid in (select tpct.course.resourceid from TeachingPlanCourseTimetable tpct where tpct.isDeleted=0 and tpct.teacherId=:teacherId) ";
				values.put("teacherId", condition.get("teacherId"));
			}
			if(condition.containsKey("classesIds")){
				hql += " and c.course.resourceid in (select tpct.course.resourceid from TeachingPlanCourseTimetable tpct where tpct.isDeleted=0 and tpct.classesid in (:classesIds)) ";
				values.put("classesIds", Arrays.asList(((String)condition.get("classesIds")).split(",")));
			}
		}
		hql += " order by c."+objPage.getOrderBy() +" "+ objPage.getOrder();
		return exGeneralHibernateDao.findByHql(objPage, hql, values);
	}

	@Override
	public void batchCascadeDelete(String[] ids) throws ServiceException {
		if(ids!=null && ids.length>0){
			for(String id : ids){
				delete(id);	
				logger.info("批量删除课程公告="+id);
			}
		}
	}
	/**
	 * 此方法增加了后置通知发送温馨提示 com.hnjk.edu.learning.aspect.LearningAspect.sendMsgAfterCourseNotice()
	 */
	@Override
	public void saveOrUpdateCourseNotice(CourseNotice courseNotice, String[] attachIds) throws ServiceException{
		saveOrUpdate(courseNotice);
		attachsService.updateAttachByFormId(attachIds, courseNotice.getResourceid(), CourseNotice.class.getSimpleName(), courseNotice.getFillinManId(), courseNotice.getFillinMan());		
	}
}
