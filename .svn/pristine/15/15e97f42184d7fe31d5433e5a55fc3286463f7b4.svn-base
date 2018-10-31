package com.hnjk.edu.work.service;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.work.model.Appraising;
import com.hnjk.edu.work.model.CadreInfo;

import java.util.List;
import java.util.Map;

/**
 * Function : 学生评优信息 - 接口
 * <p>Author : msl
 * <p>Date   : 2018-07-24
 * <p>Description :IAppraisingService
 */
public interface IAppraisingService extends IBaseService<Appraising> {

	/**
	 * 审核、删除操作
	 * @param operatingType
	 * @param condition
	 * @return
	 */
	int operateAppraisingInfo(String operatingType, Map<String, Object> condition) throws Exception;

	List<Map<String, Object>> getApplicationFormInfo(Map<String, Object> condition);
}
