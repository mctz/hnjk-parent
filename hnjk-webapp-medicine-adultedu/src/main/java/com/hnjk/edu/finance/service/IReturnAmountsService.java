package com.hnjk.edu.finance.service;

import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.finance.model.ReturnAmounts;

import java.util.Map;

/**
 * Function :已返学费金额 - 接口
 * <p>Author : msl
 * <p>Date   : 2018-08-22
 * <p>Description :
 */
public interface IReturnAmountsService extends IBaseService<ReturnAmounts> {
	Map<String, Object> importExcelModel(String filePath);
}
