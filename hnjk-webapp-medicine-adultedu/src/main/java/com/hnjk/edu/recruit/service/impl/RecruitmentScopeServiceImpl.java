package com.hnjk.edu.recruit.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.ExCollectionUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.recruit.model.RecruitmentScope;
import com.hnjk.edu.recruit.service.IRecruitmentScopeService;

/** 
 * @author Zik, 广东学苑教育发展有限公司
 * @since 2018年6月28日 下午4:48:39 
 * 
 */
@Service("recruitmentScopeService")
@Transactional
public class RecruitmentScopeServiceImpl extends BaseServiceImpl<RecruitmentScope> implements
		IRecruitmentScopeService {
	
	@Autowired 
	@Qualifier("baseSupportJdbcDao")	
	private IBaseSupportJdbcDao jdbcDao;//注入jdbc template 支持
	
	@Transactional(readOnly=true)
	@Override
	public Page findByCondition(Map<String, Object> condition, Page objPage) throws ServiceException{
		Map<String,Object> values =  new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer(1024);
		hql.append(" from "+RecruitmentScope.class.getSimpleName()+" where 1=1 and isDeleted = :isDeleted ");
		values.put("isDeleted", 0);
		if(condition.containsKey("unitId")){
			hql.append(" and unit.resourceid=:unitId ");
			values.put("unitId", condition.get("unitId"));
		}
		if(condition.containsKey("areaScope")){
			hql.append(" and areaScope=:areaScope ");
			values.put("areaScope", condition.get("areaScope"));
		}
		hql.append(" order by "+objPage.getOrderBy() +" "+ objPage.getOrder());
		
		return exGeneralHibernateDao.findByHql(objPage, hql.toString(), values);
	}

	@Override
	public void batchCascadeDelete(String[] ids) throws ServiceException{
		if(ids!=null && ids.length>0){
			for(String id : ids){
				delete(id);				
				logger.info("批量删除="+id);
			}
		}
	}

	/**
	 * 获取某个教学点的招生范围
	 * @param unitId
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<String> findScopeByUnitId(String unitId) throws Exception {
		List<String> scopeList = null;
		StringBuffer sql = new StringBuffer(250);
		sql.append("select areascope from EDU_ENROLLMENTBOOK_SCOPE where isdeleted=0 and unitid=?");
		List<Map<String,Object>> list = jdbcDao.getBaseJdbcTemplate().getOriginalJdbcTemplate().queryForList(sql.toString(), new Object[]{unitId});
		if(ExCollectionUtils.isNotEmpty(list)){
			scopeList = new ArrayList<String>(list.size());
			for(Map<String,Object> m : list){
				scopeList.add((String)m.get("areascope"));
			}
		}
		return scopeList;
	}

}
