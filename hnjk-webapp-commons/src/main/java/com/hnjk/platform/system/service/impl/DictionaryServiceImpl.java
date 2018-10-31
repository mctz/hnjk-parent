package com.hnjk.platform.system.service.impl;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.cache.EhCacheManager;
import com.hnjk.core.foundation.utils.ExBeanUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.Dictionary;
import com.hnjk.platform.system.service.IDictionaryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  <code>DictionaryServiceImpl</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2010-3-26 上午11:42:04
 * @see 
 * @version 1.0
 */
@Service("dictService")
@Transactional
public class DictionaryServiceImpl extends BaseServiceImpl<Dictionary> implements IDictionaryService {
		
	//批量删除
	@Override
	public void batchCascadeDelete(String[] split) throws ServiceException {
		for(String id : split){						
			deleteDict(id);
		}
	} 
		

	@Override
	public void deleteDict(String dictId) throws ServiceException {
		Dictionary dictionary = this.get(dictId);
		Dictionary parentDictionary = dictionary.getParentDict();
		if(null != parentDictionary){//如果为子，则更新父
			parentDictionary.getDictSets().remove(dictionary);
			exGeneralHibernateDao.update(parentDictionary);
		}else{
			exGeneralHibernateDao.truncate(dictionary);
		}		
		@SuppressWarnings("unchecked")List<Dictionary> all = (List<Dictionary>)EhCacheManager.getCache(CacheAppManager.CACHE_SYS_DICTIONS).get(CacheAppManager.CACHE_SYS_DICTIONS);
		all.remove(dictId);
	}



	//保存
	@Override
	public void saveDict(Dictionary dictionary) throws ServiceException {
		exGeneralHibernateDao.save(dictionary);			
		reloadCache();
		logger.info("保存字典 {} 成功!",dictionary.getDictCode());
	}
	
	private void reloadCache(){
		List<Dictionary> list = findByHql("from "+Dictionary.class.getSimpleName()+" where isDeleted = ? order by showOrder asc", 0);
		EhCacheManager.getCache(CacheAppManager.CACHE_SYS_DICTIONS).remove(CacheAppManager.CACHE_SYS_DICTIONS);
		EhCacheManager.getCache(CacheAppManager.CACHE_SYS_DICTIONS).put(CacheAppManager.CACHE_SYS_DICTIONS, list);
	}

	//更新
	@Override
	public void updateDict(Dictionary dictionary, List<Dictionary> childs) throws ServiceException {
		if(null != childs && childs.size()>0){
			for (Dictionary child : childs) {
				if(ExStringUtils.isNotEmpty(child.getResourceid())){
					Dictionary pChild = get(child.getResourceid());
					ExBeanUtils.copyProperties(pChild, child);
					dictionary.getDictSets().add(pChild);
					pChild.setParentDict(dictionary);
				}else{
					dictionary.getDictSets().add(child);
					child.setParentDict(dictionary);
				}
			}
		}
		exGeneralHibernateDao.update(dictionary);		
		reloadCache();		
		logger.info("更新字典 {} 成功!",dictionary.getDictCode());		
	}

	//根据条件查找
	@Override
	@Transactional(readOnly = true)
	public Page findDictionaryByCondition(Map<String, Object> condition, Page page) throws ServiceException {
		Map<String,Object> values =  new HashMap<String, Object>();
		String hql = " from "+Dictionary.class.getSimpleName()+" where 1=1 ";
		hql += " and isDeleted = :isDeleted and parentDict is null ";
		values.put("isDeleted", 0);
		if(condition.containsKey("dictCode")){
			hql += " and dictCode like :dictCode ";
			values.put("dictCode", "%"+condition.get("dictCode")+"%");
		}
		if(condition.containsKey("module")){
			hql += " and module like :module ";
			values.put("module", "%"+condition.get("module")+"%");
		}
		if(condition.containsKey("dictName")){
			hql += " and dictName like :dictName ";
			values.put("dictName", "%"+condition.get("dictName")+"%");
		}
		hql += " order by "+page.getOrderBy() +" "+ page.getOrder();
		return exGeneralHibernateDao.findByHql(page, hql, values);
	}
	//根据条件查找
	@Override
	@Transactional(readOnly = true)
	public List findDictionaryByCondition(Map<String, Object> condition) throws ServiceException {
		Map<String,Object> values =  new HashMap<String, Object>();
		String hql = " from "+Dictionary.class.getSimpleName()+" where 1=1 ";
		hql += " and isDeleted = :isDeleted and parentDict is null ";
		values.put("isDeleted", 0);
		if(condition.containsKey("dictCode")){
			hql += " and dictCode like :dictCode ";
			values.put("dictCode", "%"+condition.get("dictCode")+"%");
		}
		if(condition.containsKey("module")){
			hql += " and module like :module ";
			values.put("module", "%"+condition.get("module")+"%");
		}
		if(condition.containsKey("dictName")){
			hql += " and dictName like :dictName ";
			values.put("dictName", "%"+condition.get("dictName")+"%");
		}
		if(condition.containsKey("dictname")){
			hql += " and trim(dictName) = :dictname ";
			values.put("dictname", condition.get("dictname"));
		}
//		hql += " order by "+page.getOrderBy() +" "+ page.getOrder();
		return exGeneralHibernateDao.findByHql(hql, values);
	}

	@Override
	@Transactional(readOnly=true)
	public Map<String, Object> getDictionByMap(List<String> dictCodes, boolean isPreKey,String prekeyType) throws ServiceException {
		Map<String,Object> valueMap = new HashMap<String,Object>();
		
		if(null != dictCodes){
			Dictionary dictionary = null;
			for(String dictCode : dictCodes){//遍历字典编码，构造map
				dictionary = (Dictionary)exGeneralHibernateDao.findUniqueByProperty(Dictionary.class, "dictCode", dictCode);
			
				if(null != dictionary){
					Object[] objs = dictionary.getDictSets().toArray();			
					for(int index=0;index<objs.length;index++){
						Dictionary childDict = (Dictionary) objs[index];
						String key = childDict.getDictValue();
						if(isPreKey){//如果需要前缀key
							if(IDictionaryService.PREKEY_TYPE_BYNAME.equals(prekeyType)){//如果是按字典名字方式
								key = dictCode + "_" + childDict.getDictName();
							}else if(IDictionaryService.PREKEY_TYPE_BYCODE.equals(prekeyType)){
								key = dictCode + "_" + key;
							}
							
						}
						valueMap.put(key, childDict.getDictName());
					}
				}
			}
			
		}		
		return valueMap;
	}


	@Override
	public String getAllValues(String dictCode) throws ServiceException {
		List<Dictionary> dictionaryList = CacheAppManager.getChildren(dictCode);
		String resultStr = "";
		if(dictionaryList!=null && dictionaryList.size()>0){
			for(Dictionary d : dictionaryList){
				resultStr += d.getDictValue() + ",";
			}
		}
		if(ExStringUtils.isNotEmpty(resultStr)){
			resultStr = resultStr.substring(0, resultStr.lastIndexOf(","));
		}
		return resultStr;
	}
	
	@Override
	public String  dictCode2Val(String dictCode,String dictValue) {
		List<Dictionary> dictionaryList = CacheAppManager.getChildren(dictCode);
		String dictName = "";
		if(dictionaryList!=null && dictionaryList.size()>0){
			for(Dictionary d : dictionaryList){
				if(d.getDictValue().equals(dictValue)){
					dictName = d.getDictName();
					break;
				}
			}
		}
		return dictName;
	}

	@Override
	public Map<String, String> getMapByParentDictCode(String dictCode) {
		Map<String,String> returnMap = new HashMap<String, String>();
		List<Dictionary> dictionaryList = CacheAppManager.getChildren(dictCode);
		for (Dictionary dict : dictionaryList) {
			returnMap.put(dict.getDictName(),dict.getDictValue());
		}
		return returnMap;
	}

	@Override
	public Map<String, String> getDictMapByParentDictCode(String dictCode) {
		Map<String,String> returnMap = new HashMap<String, String>();
		List<Dictionary> dictionaryList = CacheAppManager.getChildren(dictCode);
		for (Dictionary dict : dictionaryList) {
			returnMap.put(dict.getDictValue(),dict.getDictName());
		}
		return returnMap;
	}
}
