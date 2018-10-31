package com.hnjk.security.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.security.service.ILoginExtendService;

/**
 * 组织单位服务实现. <code>OrgUnitServiceImpl</code><p>;
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2009-12-4 上午09:58:47
 * @see 
 * @version 1.0
 */
@Service("loginExtendService")
@Transactional
public class LoginExtendServiceImpl implements ILoginExtendService{
	
	@Autowired 
	@Qualifier("baseSupportJdbcDao")	
	private IBaseSupportJdbcDao jdbcDao;//注入jdbc template 支持

	/**
	 * 根据ID查找缴费状态
	 * @param userId
	 * @return
	 * @throws Exception 
	 * @author Git
	 */
	@Override
	public String getchargeStatusByUserId(String userId) throws DataAccessException {
		String chargeStatus = null;
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT chargeStatus FROM EDU_FEE_STUFEE ");
		sb.append("	WHERE isdeleted = '0' AND studentid = ");
		sb.append("	(select exvalue from hnjk_sys_usersextend where isdeleted=0 and excode='defalutrollid' and sysuserid=:userId) ");
		/*	sb.append("	(SELECT resourceid FROM EDU_ROLL_STUDENTINFO ");
		sb.append("		WHERE isdeleted = '0' AND sysuserid = :userId");*/
//		sb.append("		(SELECT resourceid FROM hnjk_sys_users ");
//		sb.append("     	WHERE usertype = 'student' AND username = :userId ");
//		sb.append(" 	 )");
//		sb.append("	)");
		
		List<Map<String, Object>> list = jdbcDao.getBaseJdbcTemplate().getJdbcTemplate().queryForList(sb.toString(), userId);

		if(list != null && list.size() > 0){
			Map<String, Object> map = list.get(0);
			/*if(map == null || !map.containsKey("chargeStatus")){
				throw new ServiceException("缴费数据异常");
			}*/
			if(map != null && map.containsKey("chargeStatus")){
				chargeStatus = map.get("chargeStatus").toString();
			}
		}
		return chargeStatus;
	}
	@Override
	public String getUnitWord() throws DataAccessException{
		String unitWord = "";
		StringBuffer sb = new StringBuffer("select c.paramValue from hnjk_sys_config c where c.paramcode = 'graduateData.schoolCode' and c.isdeleted=0 ");
		List<Map<String, Object>> list = jdbcDao.getBaseJdbcTemplate().getJdbcTemplate().queryForList(sb.toString());
		if(list != null && list.size() > 0){
			Map<String, Object> map = list.get(0);
			if(map == null || !map.containsKey("paramValue")){
				throw new ServiceException("系统全局参数：“学校代码” 参数异常，请联系系统管理人员进行处理");
			}
			unitWord = map.get("paramValue").toString();
		}
		return unitWord;
	}
	
	/**
	 * 登录时是否需要检验缴费情况
	 * @return
	 * @throws DataAccessException
	 */
	@Override
	public boolean isValidateLoginPay() throws DataAccessException {
		boolean flag = false;
		StringBuffer sb = new StringBuffer("select c.paramValue from hnjk_sys_config c where c.paramcode = 'isValidatePayFeeLogin' and c.isdeleted=0 ");
		List<Map<String, Object>> list = jdbcDao.getBaseJdbcTemplate().getJdbcTemplate().queryForList(sb.toString());
		if(list != null && list.size() > 0){
			Map<String, Object> map = list.get(0);
			if(map != null && map.containsKey("paramValue") 
					&& ExStringUtils.isNotEmpty(map.get("paramValue").toString())
					&& "Y".equals(map.get("paramValue").toString())){
				flag = true;
			}
		}
		
		return flag;
	}
}
