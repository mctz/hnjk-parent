package com.hnjk.edu.teaching.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.teaching.model.BachelorExamResults;
import com.hnjk.edu.teaching.model.TeachingRecords;

public interface ITeachingRecordsService extends IBaseService<TeachingRecords>{
	
	//通过条件查询返回学士学位英语成绩集合
	Page findTeachingRecordsByHql(Map<String, Object> condition, Page objPage) throws ServiceException;
	List<TeachingRecords> findTeachingRecordsListByCondition(Map<String, Object> condition) throws ServiceException;
	
}
