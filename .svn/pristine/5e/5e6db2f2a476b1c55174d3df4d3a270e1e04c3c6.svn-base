package com.hnjk.edu.learning.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.learning.model.CourseOverview;
import com.hnjk.edu.learning.service.ICourseOverviewService;

/**
 * 课程概况服务接口实现
 * <code>CourseOverviewServiceImpl</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-8-24 上午11:28:37
 * @see 
 * @version 1.0
 */
@Transactional
@Service("courseOverviewService")
public class CourseOverviewServiceImpl extends BaseServiceImpl<CourseOverview> implements ICourseOverviewService {

	@Override
	@Transactional(readOnly=true)
	public Page findCourseOverviewByCondition(Map<String, Object> condition, Page objPage) throws ServiceException {
		Map<String,Object> values =  new HashMap<String, Object>();
		String hql = " from "+CourseOverview.class.getName()+" where 1=1 ";
		hql += " and isDeleted = :isDeleted ";
		values.put("isDeleted", 0);
		
		if(condition.containsKey("type")){//类型
			hql += " and type =:type ";
			values.put("type", condition.get("type"));
		}
		if(condition.containsKey("courseId")){//所属课程
			hql += " and course.resourceid =:courseId ";
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
				logger.info("批量删除课程概况="+id);
			}
		}
	}

	@Override
	public boolean isExistsType(String type, String courseId) throws ServiceException {
		String hql = "select count(s) from "+CourseOverview.class.getName()+" s where s.isDeleted=0 and s.type=? and s.course.resourceid=? ";
		Long count = exGeneralHibernateDao.findUnique(hql, type,courseId);
		return (count!=null&&count.intValue()>0)?true:false;
	}
}
