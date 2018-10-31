package com.hnjk.core.dao;

import java.util.List;
import java.util.Map;

import com.hnjk.core.dao.model.HelpArticle;
import com.hnjk.core.dao.model.Resource;
import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;

public interface ITestService extends IBaseService<HelpArticle>{

	int testExcuteByHql(String hql ,Map<String, Object> paramter) throws ServiceException;
	
	 @Override
	 boolean isExistEntity(String entityName);
	
	int createInexe(Class<?> clazz) throws ServiceException;
	
	void batchUpdate(List<Resource> list) throws ServiceException;
	
	Page findArticleByFullText(Page page,String[] field,String text,Class<?> clazz,Map<String, Object> queryParaMap) throws ServiceException;
}
