package com.hnjk.platform.system.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.cache.EhCacheManager;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.SysConfiguration;
import com.hnjk.platform.system.service.ISysConfigurationService;

/**
 * 系统参数配置接口实现.
 * <code>SysConfigurationServiceImpl</code><p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-8-26 上午10:54:45
 * @see 
 * @version 1.0
 */
@Service("sysConfigurationService")
@Transactional
public class SysConfigurationServiceImpl extends BaseServiceImpl<SysConfiguration> implements ISysConfigurationService{

	@Override
	public void batchDeleteSysConfiguration(String[] resourceids) throws ServiceException {
		Assert.noNullElements(resourceids, "ID不能为空");
		for(String resourceid : resourceids){
			deleteSysConfiguration(resourceid);
		}
	}

	@Override
	public void deleteSysConfiguration(String resourceid) throws ServiceException {
		Assert.hasText(resourceid, "resourceid不能为空");
		SysConfiguration sysConfiguration = get(resourceid);
		String key = sysConfiguration.getParamCode();
		truncate(sysConfiguration);
		EhCacheManager.getCache(CacheAppManager.CACHE_APP_CONFIG).remove(key);
		logger.debug("系统参数{}删除成功！",key);
	}
	
	@Override
	@Transactional(readOnly=true)
	public Page findSysConfigurationByCondition(Map<String, Object> condition, Page page) throws ServiceException {
		List<Criterion> objCriterion = new ArrayList<Criterion>();
		
		if(condition.containsKey("paramName")){
			objCriterion.add(Restrictions.like("paramName","%"+condition.get("paramName")+"%"));
		}
		if(condition.containsKey("paramCode")){
			objCriterion.add(Restrictions.eq("paramCode",condition.get("paramCode")));
		}
		if (condition.containsKey("prefixOfParamName")) {
			objCriterion.add(Restrictions.like("paramName",condition.get("prefixOfParamName")+"%"));
		}
		objCriterion.add(Restrictions.eq("isDeleted", 0));//是否删除 =0
		return  exGeneralHibernateDao.findByCriteria(SysConfiguration.class, page, objCriterion.toArray(new Criterion[objCriterion.size()]));
	}

	@Override
	public void saveSysConfiguration(SysConfiguration sysConfiguration) throws ServiceException {
		Assert.notNull(sysConfiguration, "对象不能为空");
		save(sysConfiguration);
		EhCacheManager.getCache(CacheAppManager.CACHE_APP_CONFIG).put(sysConfiguration.getParamCode(), sysConfiguration);
		logger.debug("系统参数{}保存成功！",sysConfiguration.getParamCode());
	}

	@Override
	public void updateSysConfiguration(SysConfiguration sysConfiguration) throws ServiceException {
		Assert.notNull(sysConfiguration, "对象不能为空");
		update(sysConfiguration);
		EhCacheManager.getCache(CacheAppManager.CACHE_APP_CONFIG).remove(sysConfiguration.getParamCode());
		EhCacheManager.getCache(CacheAppManager.CACHE_APP_CONFIG).put(sysConfiguration.getParamCode(),sysConfiguration);
		logger.debug("系统参数{}更新成功！",sysConfiguration.getParamCode());
	}

	/**
	 * 根据编码获取全局参数实体
	 * @param paramCode
	 * @return
	 */
	@Override
	public SysConfiguration findOneByCode(String paramCode) {
		String hql = "from "+SysConfiguration.class.getSimpleName()+" c where c.isDeleted=0 and c.paramCode=?";
		return findUnique(hql, paramCode);
	}

	
}
