package com.hnjk.edu.teaching.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hnjk.core.rao.dao.jdbc.BaseSupportJdbcDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.teaching.model.TeachingPlanCourseTimePeriod;
import com.hnjk.edu.teaching.service.ITeachingPlanCourseTimePeriodService;
/**
 * 时间设置
 * <code>TeachingPlanCourseTimePeriodServiceImpl</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2013-4-13 下午04:24:11
 * @see 
 * @version 1.0
 */
@Transactional
@Service("teachingPlanCourseTimePeriodService")
public class TeachingPlanCourseTimePeriodServiceImpl extends BaseServiceImpl<TeachingPlanCourseTimePeriod> implements ITeachingPlanCourseTimePeriodService {

	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private BaseSupportJdbcDao baseSupportJdbcDao;

	@Override
	public Page findTeachingPlanCourseTimePeriodByCondition(Map<String, Object> condition, Page objPage) throws ServiceException {
		Map<String, Object> values = new HashMap<String, Object>();
		StringBuilder hql = new StringBuilder();
		hql.append(" from ").append(TeachingPlanCourseTimePeriod.class.getSimpleName()).append(" where isDeleted=:isDeleted ");
		values.put("isDeleted", 0);
		if(condition.containsKey("brSchoolid")){
			hql.append(" and brSchool.resourceid=:brSchoolid ");
			values.put("brSchoolid", condition.get("brSchoolid"));
		}
		if(condition.containsKey("addSql")){
			hql.append(condition.get("addSql"));
		}
		if(objPage.isOrderBySetted()){
			hql.append(" order by ").append(objPage.getOrderBy()).append(" ").append(objPage.getOrder());
		}
		return findByHql(objPage, hql.toString(), values);
	}


	@Override
	public void batchCascadeDelete(String[] ids) throws ServiceException {
		if (ids != null && ids.length > 0) {
			for (String id : ids) {
				delete(id);
				logger.info("批量删除=" + id);
			}
		}
		
	}


	@Override
	public String constructOptions(Map<String, Object> condition, String[] settingList) {
		StringBuffer timePeriodOption = new StringBuffer();
		Map<String, Object> values = new HashMap<String, Object>();
		String hql = " from "+TeachingPlanCourseTimePeriod.class.getSimpleName()+" where isDeleted=0";
		if(condition.containsKey("brSchoolid")){
			hql += " and brSchool.resourceid=:brSchoolid";
			values.put("brSchoolid", condition.get("brSchoolid"));
		}
		List<TeachingPlanCourseTimePeriod> timePeriodList = findByHql(hql, values);
		for (TeachingPlanCourseTimePeriod timePeriod : timePeriodList) {
			timePeriodOption.append("<option");
			if(settingList!=null && settingList.length>0){
				for (String resid : settingList) {
					if(timePeriod.getResourceid().equals(resid)){
						timePeriodOption.append(" selected='selected'");
					}
				}
			}
			timePeriodOption.append(" value='"+timePeriod.getResourceid()+"'>"+timePeriod.getCourseTimeName()+"</option>");
		}
		return timePeriodOption.toString();
	}

	@Override
	public void distinctTimePeriod() {
		StringBuilder sql = new StringBuilder("update EDU_TEACH_TIMEPERIOD t set t.ISDELETED=1");
		sql.append(" where t.ISDELETED=0 and t.RESOURCEID not in");
		sql.append(" (select tt.TIMEPERIODID from EDU_TEACH_TIMETABLE tt where tt.ISDELETED=0 and tt.TIMEPERIODID is not null group by tt.TIMEPERIODID)");
		sql.append(" and (t.ORGUNITID,t.STARTTIME,t.ENDTIME) in");
		sql.append(" (select t1.ORGUNITID,t1.STARTTIME,t1.ENDTIME from EDU_TEACH_TIMEPERIOD t1 group by t1.ORGUNITID,t1.STARTTIME,t1.ENDTIME having count(*)>1)");
		try {
			baseSupportJdbcDao.getBaseJdbcTemplate().executeForMap(sql.toString(),null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}