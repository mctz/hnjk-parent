package com.hnjk.edu.learning.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.learning.model.CourseMockTest;
import com.hnjk.edu.learning.service.ICourseMockTestService;
/**
 * 课程模拟试题管理服务接口实现
 * <code>CourseMockTestServiceImpl</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-11-9 下午04:58:57
 * @see 
 * @version 1.0
 */
@Transactional
@Service("courseMockTestService")
public class CourseMockTestServiceImpl extends BaseServiceImpl<CourseMockTest> implements ICourseMockTestService {

	@Override
	public void batchCascadeDelete(String[] ids) throws ServiceException {
		if(ids!=null && ids.length>0){
			for(String id : ids){
				delete(id);	
				logger.info("删除课程模拟试题="+id);
			}
		}
	}

	@Override
	@Transactional(readOnly=true)
	public Page findCourseMockTestByCondition(Map<String, Object> condition, Page objPage) throws ServiceException {
		Map<String,Object> values =  new HashMap<String, Object>();
		String hql = " from "+CourseMockTest.class.getName()+" where 1=1 ";
		hql += " and isDeleted = :isDeleted ";
		values.put("isDeleted", 0);		
			
		if(condition.containsKey("courseId")){//所属课程
			hql += " and course.resourceid =:courseId ";
			values.put("courseId", condition.get("courseId"));
		}
		hql += " order by "+objPage.getOrderBy() +" "+ objPage.getOrder();
		return exGeneralHibernateDao.findByHql(objPage, hql, values);
	}

}
