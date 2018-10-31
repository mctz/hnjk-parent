package com.hnjk.edu.work.service;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.work.model.CadreInfo;

import java.util.List;
import java.util.Map;

/**
 * Function : 学生干部信息 - 接口
 * <p>Author : msl
 * <p>Date   : 2018-07-23
 * <p>Description : ICadreInfoService
 */
public interface ICadreInfoService extends IBaseService<CadreInfo> {

	/**
	 * 获取学生招新报名表信息
	 * @param condition
	 * @return
	 */
	List<Map<String, Object>> getApplicationFormInfo(Map<String, Object> condition);

	/**
	 * 获取学生当年竞选职位信息
	 * @param studyNo
	 * @param yearid
	 * @return
	 */
	CadreInfo getPositionByYear(String studyNo, String yearid);
}
