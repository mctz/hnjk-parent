package com.hnjk.security.service;

import org.springframework.dao.DataAccessException;

import com.hnjk.core.exception.ServiceException;

/**
 * 用户服务接口. <code>ILoginExtendService</code>
 * <p>;
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2016/1/14 下午03:01:33
 * @modify:
 * @主要功能：
 * @see
 * @version 1.0
 */
public interface ILoginExtendService {	
	
	/**
	 * 根据ID查找缴费状态
	 * @param userId
	 * @return
	 * @throws ServiceException
	 */
	String getchargeStatusByUserId(String userId) throws DataAccessException;
	/**
	 * 获取学校代码
	 * @return
	 * @throws DataAccessException
	 */
	String getUnitWord() throws DataAccessException;
	
	/**
	 * 登录时是否需要检验缴费情况
	 * @return
	 * @throws DataAccessException
	 */
	boolean isValidateLoginPay() throws DataAccessException;
}
