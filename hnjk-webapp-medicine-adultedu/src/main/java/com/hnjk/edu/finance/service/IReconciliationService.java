package com.hnjk.edu.finance.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.finance.model.Reconciliation;
import com.hnjk.edu.finance.model.TempStudentFee;

public interface IReconciliationService extends IBaseService<Reconciliation>{

	/**
	 * 返回对账列表
	 */
	Page getReconciliationPage(Map<String, Object> condition, Page objPage)
			throws Exception;
	/**
	 * 根据request，保存对账信息
	 */
	String saveReconciliation(String request) throws Exception;
	/**
	 * 初始化并连接FTP服务器
	 */
	String initFtpServer() throws IOException, NumberFormatException;
	/**
	 * 更新对账信息
	 */
	int saveReconciliation(List<TempStudentFee> list, String[] strArray);
	/**
	 * 删除对账信息
	 * @param resourceids
	 * @return
	 */
	boolean deleteReconciliation(String resourceids);

}
