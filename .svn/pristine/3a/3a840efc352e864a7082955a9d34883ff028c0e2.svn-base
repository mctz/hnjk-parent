package com.hnjk.edu.teaching.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.teaching.model.TeachTaskDetails;
import com.hnjk.edu.teaching.service.ITeachTaskDetailsService;
/**
 * 教学任务书明细服务接口实现
 * <code>TeachTaskDetailsServiceImpl</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2011-2-16 下午04:08:51
 * @see 
 * @version 1.0
 */
@Transactional
@Service("teachTaskDetailsService")
public class TeachTaskDetailsServiceImpl extends BaseServiceImpl<TeachTaskDetails> implements ITeachTaskDetailsService {

	@Override
	@Transactional(readOnly=true)
	public List<TeachTaskDetails> findTeachTaskDetailsByCondition(Map<String, Object> condition) throws ServiceException {
		Map<String,Object> values =  new HashMap<String, Object>();
		String hql = " from "+TeachTaskDetails.class.getSimpleName()+" where 1=1 ";
		hql += " and isDeleted = :isDeleted ";
		values.put("isDeleted", 0);
		
		hql += " and teachTask.taskStatus=3 ";//已审核发布的任务书
		
		if(condition.containsKey("userId")){
			hql += " and (ownerId =:userId or participantsId like '%"+condition.get("userId").toString()+"%')";
			values.put("userId", condition.get("userId"));
		}
		if(condition.containsKey("time")){
			hql += " and startTime <=:time and endTime >=:time";
			values.put("time", (Date)(condition.get("time")));
		}
		if(condition.containsKey("orderBy")){
			hql += " order by "+ condition.get("orderBy");
		}
		
		return (List<TeachTaskDetails>) exGeneralHibernateDao.findByHql(hql,values);
	}

	@Override
	public void batchCascadeDelete(String[] split, String teachTaskId) throws ServiceException {
		if(split!=null && split.length>0){
			batchDelete(split);
			List<TeachTaskDetails> list = (List<TeachTaskDetails>) exGeneralHibernateDao.findByHql("from "+TeachTaskDetails.class.getSimpleName()+"  where isDeleted=0 and teachTask.resourceid=? order by showOrder ", teachTaskId);
			long index = 1L;
			for (TeachTaskDetails d : list) {
				d.setShowOrder(index++);
			}
			batchSaveOrUpdate(list);
		}				
	}
	
	@Override
	@Transactional(readOnly=true)
	public Page findTeachTaskDetailsByCondition(Map<String, Object> condition, Page objPage) {
		Map<String,Object> values =  new HashMap<String, Object>();
		String hql = " from "+TeachTaskDetails.class.getSimpleName()+" where 1=1 ";
		hql += " and isDeleted = :isDeleted ";
		values.put("isDeleted", 0);
		
		if(condition.containsKey("yearInfoid")){
			hql += " and teachTask.yearInfo.resourceid=:yearInfoid ";
			values.put("yearInfoid", condition.get("yearInfoid"));
		}
		if(condition.containsKey("term")){
			hql += " and teachTask.term=:term ";
			values.put("term", condition.get("term"));
		}
		if(condition.containsKey("courseId")){
			hql += " and teachTask.course.resourceid=:courseId ";
			values.put("courseId", condition.get("courseId"));
		}
		if(condition.containsKey("courseName")){
			hql += " and lower(teachTask.course.courseName) like:courseName ";
			values.put("courseName", "%"+condition.get("courseName").toString().toLowerCase()+"%");
		}
		if(condition.containsKey("teacherName")){
			hql += " and teachTask.teacherName like :teacherName ";
			values.put("teacherName", "%"+condition.get("teacherName").toString().toLowerCase()+"%");
		}
		if(condition.containsKey("teacherId")){
			hql += " and teachTask.teacherId =:teacherId ";
			values.put("teacherId", condition.get("teacherId"));
		}
		if(condition.containsKey("taskStatus")){
			hql += " and teachTask.taskStatus =:taskStatus ";
			values.put("taskStatus", Long.parseLong(condition.get("taskStatus").toString()));
		}	
		if(condition.containsKey("taskType")){
			hql += " and taskType =:taskType ";
			values.put("taskType", condition.get("taskType"));
		}	
		
		hql += " order by "+objPage.getOrderBy() +" "+ objPage.getOrder();
		return exGeneralHibernateDao.findByHql(objPage, hql, values);
	}
}
