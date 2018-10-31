package com.hnjk.core.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.metadata.ClassMetadata;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.dao.model.HelpArticle;
import com.hnjk.core.dao.model.Resource;
import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.BaseServiceImpl;

@Service("testService")
@Transactional
public class TestServiceImpl extends BaseServiceImpl<HelpArticle> implements ITestService{

	
	@Override
	public int testExcuteByHql(String hql, Map<String, Object> paramter)			throws ServiceException {
		return exGeneralHibernateDao.executeHQL(hql, paramter);
	}

	
	@Override
	public int createInexe(Class<?> clazz) throws ServiceException {
		return luceneTextQuery.createIndex(clazz);
	}


	@Override
	public Page findArticleByFullText(Page page, String[] field, String text,Class<?> clazz, Map<String, Object> queryParaMap)
			throws ServiceException {
		
		return luceneTextQuery.findByFullText(page, field, text, clazz);
	}


	@Override
	public void batchUpdate(List<Resource> list) throws ServiceException {
		exGeneralHibernateDao.saveOrUpdateCollection(list);
	}
	
	@Override
	public boolean isExistEntity(String entityName){
		ClassMetadata classMetadata = exGeneralHibernateDao.getSessionFactory().getClassMetadata(entityName);		
		return null != classMetadata;
	}
	
	
}
