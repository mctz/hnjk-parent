package com.hnjk.edu.teaching.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.teaching.model.ExamSettingDetails;
import com.hnjk.edu.teaching.service.IExamCourseService;

/**
 * 考试计划设置
 * <code>ExamCousreController</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-8-17 下午03:58:46
 * @see 
 * @version 1.0
 */
@Transactional
@Service("examCourseService")
public class ExamCourseServiceImpl extends BaseServiceImpl<Course> implements IExamCourseService {

	@Override
	public void batchDelete(String[] ids) throws ServiceException{
		if(ids!=null && ids.length>0){
			for(String id : ids){
				delete(id);	
				logger.info("批量删除="+id);
			}
		}
	}


	@Override
	@Transactional(readOnly=true)
	public Page findExamCourseByCondition(Map<String, Object> condition,
			Page objPage) throws ServiceException{
		Map<String,Object> values =  new HashMap<String, Object>();
		String hql = " from "+Course.class.getSimpleName()+" where 1=1 ";
		hql += " and isDeleted = :isDeleted ";
		values.put("isDeleted", 0);
		
		if(condition.containsKey("courseName")){
			hql += " and courseName like :courseName ";
			values.put("courseName", condition.get("courseName")+"%");
		}
		if(condition.containsKey("courseCode")){
			hql += " and courseCode like :courseCode ";
			values.put("courseCode", condition.get("courseCode")+"%");
		}
		if(condition.containsKey("defaultTeacherName")){
			hql += " and defaultTeacherName like :defaultTeacherName ";
			values.put("defaultTeacherName", condition.get("defaultTeacherName")+"%");
		}
		if(condition.containsKey("status")){
			hql += " and status = :status ";
			values.put("status", Long.parseLong((condition.get("status").toString())));
		}
		hql += " order by "+objPage.getOrderBy() +" "+ objPage.getOrder();
		return exGeneralHibernateDao.findByHql(objPage, hql, values);
	}
	
}
