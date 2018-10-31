package com.hnjk.platform.system.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.platform.system.model.HistoryModel;
import com.hnjk.platform.system.service.IHistoryService;

/**
 *  业务审计日志.
 * @author Administrator
 *
 */
@Transactional
@Service("historyService")
public class HistoryServiceImpl extends BaseServiceImpl<HistoryModel> implements IHistoryService{
	
	@Override
	public List<HistoryModel> findHistoryList(String entityId,String entityName)	throws ServiceException {	
		return findByHql("from "+HistoryModel.class.getSimpleName()+" where isDeleted= ? and entiryId = ? and entiryName = ?", 0,entityId,entityName);
	}

	
}
