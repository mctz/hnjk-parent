package com.hnjk.edu.learning.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.learning.model.CourseReference;
import com.hnjk.edu.learning.service.ICourseReferenceService;
/**
 * 课程参考资料服务接口实现
 * <code>CourseReferenceServiceImpl</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-11-19 上午10:27:00
 * @see 
 * @version 1.0
 */
@Transactional
@Service("courseReferenceService")
public class CourseReferenceServiceImpl extends BaseServiceImpl<CourseReference> implements ICourseReferenceService {

	@Override
	public void batchCascadeDelete(String[] ids) throws ServiceException {
		if(ids!=null && ids.length>0){
			for(String id : ids){
				delete(id);	
				logger.info("删除课程参考资料="+id);
			}
		}
	}

	@Override
	@Transactional(readOnly=true)
	public Page findCourseReferenceByCondition(Map<String, Object> condition, Page objPage) throws ServiceException {
		Map<String,Object> values =  new HashMap<String, Object>();
		String hql = " from "+CourseReference.class.getName()+" where 1=1 ";
		hql += " and isDeleted = :isDeleted ";
		values.put("isDeleted", 0);		
		
		if(condition.containsKey("referenceName")){//参考资料名称
			hql += " and lower(referenceName) like :referenceName ";
			values.put("referenceName", "%"+condition.get("referenceName").toString().toLowerCase()+"%");
		}
		if(condition.containsKey("referenceType")){//参考资料类型
			hql += " and referenceType =:referenceType ";
			values.put("referenceType", condition.get("referenceType").toString().toLowerCase());
		}
		if(condition.containsKey("courseId")){//所属课程
			hql += " and course.resourceid =:courseId ";
			values.put("courseId", condition.get("courseId"));
		}
		if(condition.containsKey("syllabusId")){//知识节点
			hql += " and syllabus.resourceid =:syllabusId ";
			values.put("syllabusId", condition.get("syllabusId"));
		}	
		if(condition.containsKey("nullSyllabusId")){
			hql += " and syllabus is null ";
		}
		if(condition.containsKey("notNullSyllabus")){
			hql += " and syllabus is not null ";
		}
		hql += " order by "+objPage.getOrderBy() +" "+ objPage.getOrder();
		return exGeneralHibernateDao.findByHql(objPage, hql, values);
	}

	@Override
	@Transactional(readOnly=true)
	public List<CourseReference> findCourseReferenceByCondition(Map<String, Object> condition) throws ServiceException {
		Map<String,Object> values =  new HashMap<String, Object>();
		String hql = " from "+CourseReference.class.getName()+" where 1=1 ";
		hql += " and isDeleted = :isDeleted ";
		values.put("isDeleted", 0);		
		
		if(condition.containsKey("referenceType")){//参考资料类型
			hql += " and referenceType =:referenceType ";
			values.put("referenceType", condition.get("referenceType").toString().toLowerCase());
		}		
		if(condition.containsKey("syllabusId")){//知识节点
			hql += " and syllabus.resourceid =:syllabusId ";
			values.put("syllabusId", condition.get("syllabusId"));
		}	
		if(condition.containsKey("courseId")){//所属课程
			hql += " and course.resourceid =:courseId ";
			values.put("courseId", condition.get("courseId"));
		}
		if(condition.containsKey("notNullSyllabus")){
			hql += " and syllabus is not null ";
		}
		if(condition.containsKey("orderBy")){
			hql += " order by  "+condition.get("orderBy");
		}
		return findByHql(hql,values);
	}

}
