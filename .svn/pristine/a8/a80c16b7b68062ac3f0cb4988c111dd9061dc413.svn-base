package com.hnjk.edu.learning.service.impl;

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
import com.hnjk.edu.learning.model.Exercise;
import com.hnjk.edu.learning.service.IExerciseService;
import com.hnjk.edu.teaching.service.ITeachingPlanCourseTimetableService;

/**
 * 课后作业管理服务接口实现
 * <code>ExerciseServiceImpl</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-8-6 下午05:09:41
 * @see 
 * @version 1.0
 */
@Transactional
@Service("exerciseService")
public class ExerciseServiceImpl extends BaseServiceImpl<Exercise> implements IExerciseService {	

	@Autowired
	@Qualifier("teachingPlanCourseTimetableService")
	private ITeachingPlanCourseTimetableService tpctService;
	
	@Override
	@Transactional(readOnly=true)
	public Page findExerciseByCondition(Map<String, Object> condition, Page objPage) throws ServiceException {
		Map<String,Object> values =  new HashMap<String, Object>();
		String hql = " from "+Exercise.class.getSimpleName()+" where 1=1 ";
		hql += " and isDeleted = :isDeleted ";
		values.put("isDeleted", 0);
		
		if(condition.containsKey("keywords")){//关键字
			hql += " and keywords like :keywords ";
			values.put("keywords", "%"+condition.get("keywords")+"%");
		}
		if(condition.containsKey("exerciseType")){//作业类型
			hql += " and exerciseType =:exerciseType ";
			values.put("exerciseType", condition.get("exerciseType"));
		}
		if(condition.containsKey("difficult")){//作业批次状态
			hql += " and difficult =:difficult ";
			values.put("difficult", condition.get("difficult"));
		}
		if(condition.containsKey("colsId")){//所属作业批次
			hql += " and exerciseBatch.resourceid =:colsId ";
			values.put("colsId", condition.get("colsId"));
		}
		hql += " order by "+objPage.getOrderBy() +" "+ objPage.getOrder();
		return exGeneralHibernateDao.findByHql(objPage, hql, values);
	}
		
	@Override
	public void deleteExercise(String[] ids) throws ServiceException {
		if(ids!=null && ids.length>0){
			for(String id : ids){				
				delete(id);	
				logger.info("删除课后作业="+id);
			}
		}
	}
	
	@Override
	@Transactional(readOnly=true)
	public Integer getNextShowOrder(String exerciseBatchId) throws ServiceException {
		Integer showOrder = exGeneralHibernateDao.findUnique("select max(c.showOrder) from "+Exercise.class.getSimpleName()+" c where c.isDeleted=0 and c.exerciseBatch.resourceid=? ", exerciseBatchId);
		if(showOrder==null) {
			showOrder = 0;
		}
		return ++showOrder;
	}

	@Override
	@Transactional(readOnly=true)
	public List<Exercise> findExerciseByCondition(Map<String, Object> condition) throws ServiceException {
		Map<String,Object> values =  new HashMap<String, Object>();
		String hql = " from "+Exercise.class.getSimpleName()+" where 1=1 ";
		hql += " and isDeleted = :isDeleted ";
		values.put("isDeleted", 0);
		
		if(condition.containsKey("colsId")){//所属作业批次
			hql += " and exerciseBatch.resourceid =:colsId ";
			values.put("colsId", condition.get("colsId"));
		}
		hql += " order by showOrder ";
		return  findByHql( hql,values);
	}

	@Override
	public boolean isExsitExercise(String batchId, String courseExamId) throws ServiceException {
		List list= exGeneralHibernateDao.findByHql(" from "+Exercise.class.getSimpleName()+" where isDeleted=0 and exerciseBatch.resourceid=? and courseExam.resourceid=? ", batchId,courseExamId);
		return (null!=list && !list.isEmpty())?true:false;
	}

}
