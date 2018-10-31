package com.hnjk.platform.system.service.impl;

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
import com.hnjk.platform.system.model.Version;
import com.hnjk.platform.system.service.IVersionService;

/**
 * VersionServiceImpl
 * @author zik, 广东学苑教育发展有限公司
 *
 */
@Transactional
@Service("versionService")
public class VersionServiceImpl extends BaseServiceImpl<Version> implements IVersionService {

	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private  IBaseSupportJdbcDao baseSupportJdbcDao;
	
	@SuppressWarnings("unchecked")
	@Override
	public Version getLastVersion() {
		Version lastVersion = null;
		String hql = "from "+Version.class.getSimpleName()+" v where v.isDeleted=0 and v.isPublish='Y' order by v.versionNo desc";
//		String hql = "from "+Version.class.getSimpleName()+" v where v.isDeleted=0 and v.isPublish='Y' and v.versionNo in (select max(vs.versionNo) from "+Version.class.getSimpleName()+" vs)";
		List<Version> versionList = (List<Version>)exGeneralHibernateDao.findByHql(hql.toString());
		if(ExCollectionUtils.isNotEmpty(versionList)){
			lastVersion = versionList.get(0);
		}
		return lastVersion;
	}

	@Override
	public Page findByCondition(Map<String, Object> condition, Page objPage){
		Map<String, Object> values = new HashMap<String, Object>();
		
		StringBuffer hql = findByConditionHql(condition, values);
		
		return findByHql(objPage, hql.toString(), values);
	}

	@Override
	public void batchCascadeDelete(String[] ids) throws ServiceException {
		if(ids!=null && ids.length>0){
			for(String id : ids){
				delete(id);				
			}
		}
	}

	@Override
	public List<Version> findByCondition(Map<String, Object> condition)
			throws ServiceException {
		Map<String, Object> values = new HashMap<String, Object>();
		StringBuffer hql = findByConditionHql(condition, values);
		return findByHql(hql.toString(), values);
	}
	
	private StringBuffer findByConditionHql(Map<String, Object> condition, Map<String, Object> values) {
		StringBuffer hql = new StringBuffer("from "+Version.class.getSimpleName()+" v where v.isDeleted=0 ");
		if(condition.containsKey("versionNo")){
			hql.append("and v.versionNo = :versionNo ");
			values.put("versionNo", condition.get("versionNo"));
		}
		if(condition.containsKey("versionName")){
			hql.append("and v.versionName like :versionName ");
			values.put("versionName", "%"+condition.get("versionName")+"%");
		}
		if(condition.containsKey("isPublish")){
			hql.append("and v.isPublish = :isPublish ");
			values.put("isPublish", condition.get("isPublish"));
		}
		return hql;
	}
}
