package com.hnjk.edu.learning.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.learning.model.CourseLearningGuid;
import com.hnjk.edu.learning.service.ICourseLearningGuidService;

/**
 * 课程学习目标服务接口实现
 * <code>CourseLearningGuidServiceImpl</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-8-24 下午01:26:44
 * @see 
 * @version 1.0
 */
@Transactional
@Service("courseLearningGuidService")
public class CourseLearningGuidServiceImpl extends BaseServiceImpl<CourseLearningGuid> implements ICourseLearningGuidService {

	@Override
	@Transactional(readOnly=true)
	public Page findCourseLearningGuidByCondition(Map<String, Object> condition, Page objPage) throws ServiceException {
		Map<String,Object> values =  new HashMap<String, Object>();
		String hql = " from "+CourseLearningGuid.class.getName()+" where 1=1 ";
		hql += " and isDeleted = :isDeleted ";
		values.put("isDeleted", 0);
		
		if(condition.containsKey("type")){//类型
			hql += " and type =:type ";
			values.put("type", condition.get("type"));
		}
		if(condition.containsKey("syllabusId")){//所属知识节点
			hql += " and syllabus.resourceid =:syllabusId ";
			values.put("syllabusId", condition.get("syllabusId"));
		}
		if(condition.containsKey("courseId")){//所属课程
			hql += " and syllabus.course.resourceid =:courseId ";
			values.put("courseId", condition.get("courseId"));
		}
		hql += " order by "+objPage.getOrderBy() +" "+ objPage.getOrder();
		return exGeneralHibernateDao.findByHql(objPage, hql, values);
	}

	@Override
	public void batchCascadeDelete(String[] ids) throws ServiceException {
		if(ids!=null && ids.length>0){
			for(String id : ids){
				delete(id);	
				logger.info("批量删除课程学习目标="+id);
			}
		}
	}

	@Override
	@Transactional(readOnly=true)
	public List<CourseLearningGuid> findCourseLearningGuidByCondition(Map<String, Object> condition) throws ServiceException {
		Map<String,Object> values =  new HashMap<String, Object>();
		String hql = " from "+CourseLearningGuid.class.getName()+" where 1=1 ";
		hql += " and isDeleted = :isDeleted ";
		values.put("isDeleted", 0);
		
		if(condition.containsKey("type")){//类型
			hql += " and type =:type ";
			values.put("type", condition.get("type"));
		}
		if(condition.containsKey("syllabusId")){//所属知识节点
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
		return findByHql( hql,values);
	}
}
