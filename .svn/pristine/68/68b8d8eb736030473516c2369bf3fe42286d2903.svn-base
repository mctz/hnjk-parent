package com.hnjk.edu.teaching.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.teaching.model.BachelorExamResults;

public interface IBachelorExamResultsService extends IBaseService<BachelorExamResults>{
	
	//通过条件查询返回学士学位英语成绩集合
	Page findBachelorExamByHql(Map<String, Object> condition, Page objPage) throws ServiceException;
	@Override
	List<BachelorExamResults> findByCondition(Map<String, Object> condition) throws ServiceException;
	
	//导入学士学位英语成绩表
	Map<String, Object> analysisBachelorExamFile(String filePath);
	//通过条件查询返回唯一信息
	BachelorExamResults findUniqueBachelorExamInfo(Map<String, Object> condition);
	void updateBatch(String batch);
	//获取最高成绩
	Map<String, Object> getTopExamByStudyNo(String studyNo);
	
}
