package com.hnjk.edu.teaching.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.cache.InitAppDataServiceImpl;
import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.teaching.model.TeachingRecords;
import com.hnjk.edu.teaching.service.ITeachingRecordsService;
import com.hnjk.security.service.IOrgUnitService;

@Transactional
@Service("teachingRecordsService")
public class TeachingRecordsServiceImpl extends BaseServiceImpl<TeachingRecords> implements ITeachingRecordsService{
	
	@Autowired
	@Qualifier("orgUnitService")
	private IOrgUnitService orgUnitService;
	
	@Autowired	
	private InitAppDataServiceImpl initAppDataServiceImpl;
	
	/**
	 * 根据条件获取教学记录
	 * @param condition objPage
	 * @return Page
	 * @throws ServiceException
	 */
	@Override
	@Transactional(readOnly=true)
	public Page findTeachingRecordsByHql(Map<String, Object> condition,Page objPage) throws ServiceException {
		
		StringBuffer hql = new StringBuffer(" from "+TeachingRecords.class.getSimpleName()+" c where c.isDeleted = :isDeleted ");
		Map<String,Object> values =  getValuesByCondition(hql,condition);

		if(ExStringUtils.isNotEmpty(objPage.getOrderBy())){			
			hql.append(" order by c."+objPage.getOrderBy().replace(",", ",c.") +" "+ objPage.getOrder());
		}
		return exGeneralHibernateDao.findByHql(objPage, hql.toString(), values);
	}
	
	
	/**
	 * 根据条件获取教学记录
	 * @param condition
	 * @return list
	 * @throws ServiceException
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<TeachingRecords> findTeachingRecordsListByCondition(Map<String, Object> condition) throws ServiceException {
		StringBuffer hql = new StringBuffer(" from "+TeachingRecords.class.getSimpleName()+" c where c.isDeleted = :isDeleted");
		Map<String,Object> values = getValuesByCondition(hql,condition);
		return  (List<TeachingRecords>) exGeneralHibernateDao.findByHql(hql.toString(), values);
	}
	
	/**
	 * 获取查询条件
	 * @param hql
	 * @param condition
	 * @return
	 */
	private Map<String, Object> getValuesByCondition(StringBuffer hql,Map<String, Object> condition) {
		Map<String,Object> values =  new HashMap<String, Object>();
		values.put("isDeleted", 0);
		if(condition.containsKey("brSchoolId")){
			hql.append(" and c.unit.resourceid=:brSchoolId ");
			values.put("brSchoolId", condition.get("brSchoolId"));
		}
		if(condition.containsKey("term")){
			hql.append(" and c.term=:term ");
			values.put("term", condition.get("term"));
		}
		if(condition.containsKey("gradeId")){
			hql.append(" and c.grade.resourceid=:gradeId ");
			values.put("gradeId", condition.get("gradeId"));
		}
		if(condition.containsKey("teachingType")){
			hql.append(" and c.teachingType=:teachingType ");
			values.put("teachingType", condition.get("teachingType"));
		}
		if(condition.containsKey("majorId")){
			hql.append(" and c.major.resourceid=:majorId ");
			values.put("majorId", condition.get("majorId"));
		}
		if(condition.containsKey("teacherName")){
			hql.append(" and c.teacher.cnName like:teacherName ");
			values.put("teacherName", "%"+condition.get("teacherName")+"%");
		}
		if(condition.containsKey("timePeriod")){
			hql.append(" and c.timeperiod >=to_date('"+condition.get("timePeriod")+"','yyyy-MM')");
		}
		if(condition.containsKey("timePeriod2")){
			hql.append(" and c.timeperiod <=to_date('"+condition.get("timePeriod2")+"','yyyy-MM')");
		}
		if(condition.containsKey("courseId")){
			hql.append(" and c.planCourse.course.resourceid=:courseId ");
			values.put("courseId", condition.get("courseId"));
		}
		return values;
	}
}
