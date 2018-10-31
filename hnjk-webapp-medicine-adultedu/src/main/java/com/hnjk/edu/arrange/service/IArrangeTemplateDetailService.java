package com.hnjk.edu.arrange.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.arrange.model.ArrangeTemplateDetail;
import com.hnjk.edu.teaching.model.TeachingPlanCourseTimePeriod;

public interface IArrangeTemplateDetailService extends IBaseService<ArrangeTemplateDetail>{
	//通过条件查询返回排课模版详情页
	Page findArrangeTemplateDetailByHql(Map<String, Object> condition, Page objPage) throws ServiceException;

}
