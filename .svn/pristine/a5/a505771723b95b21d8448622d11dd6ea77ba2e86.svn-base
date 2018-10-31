package com.hnjk.edu.finance.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.ExCollectionUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.finance.model.PaymentFeePrivilege;
import com.hnjk.edu.finance.service.IPaymentFeePrivilegeService;
import com.hnjk.edu.finance.vo.PaymentFeePrivilegeVo;
/**
 * 学费优惠设置服务.
 * <code>PaymentFeePrivilegeServiceImpl</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2012-11-9 上午10:24:40
 * @see 
 * @version 1.0
 */
@Transactional
@Service("paymentFeePrivilegeService")
public class PaymentFeePrivilegeServiceImpl extends BaseServiceImpl<PaymentFeePrivilege> implements IPaymentFeePrivilegeService {

	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;

	@Override
	@Transactional(readOnly=true)
	public Page findPaymentFeePrivilegeVoByCondition(Map<String, Object> condition, Page objPage) throws ServiceException {
		List<Object> paramList = new ArrayList<Object>();		
		StringBuffer sql = getPaymentFeePrivilegeVoSqlByCondition(condition, paramList);					
		return baseSupportJdbcDao.getBaseJdbcTemplate().findList(objPage, sql.toString(), paramList.toArray(), PaymentFeePrivilegeVo.class);
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<PaymentFeePrivilegeVo> findPaymentFeePrivilegeVoByCondition(Map<String, Object> condition) throws ServiceException {
		List<Object> paramList = new ArrayList<Object>();		
		StringBuffer sql = getPaymentFeePrivilegeVoSqlByCondition(condition, paramList);	
		List<PaymentFeePrivilegeVo> list = null;
		try {
			list =  baseSupportJdbcDao.getBaseJdbcTemplate().findList(sql.toString(), paramList.toArray(), PaymentFeePrivilegeVo.class);
		} catch (Exception e) {
			logger.error("查询招生专业学习中心优惠设置出错",e.fillInStackTrace());
		}
		return list;
	}
	private StringBuffer getPaymentFeePrivilegeVoSqlByCondition(Map<String, Object> condition, List<Object> paramList){
		StringBuffer sql = new StringBuffer();
		String privilegeType = condition.get("privilegeType").toString();
		if("brSchool".equals(privilegeType)){//学习中心优惠
			sql.append("select u.resourceid unitId,u.unitcode,u.unitname,u.unitShortName,p.resourceid paymentFeePrivilegeId,p.beforeprivilegefee,p.afterprivilegefee,p.memo ");
			sql.append(" from hnjk_sys_unit u left join edu_fee_privilege p on u.resourceid=p.brschollid ");
			sql.append(" where u.isdeleted=0 and u.unittype='brSchool' and p.recruitmajorid is null ");			
			if(condition.containsKey("unitCode")){
				sql.append(" and u.unitCode=? ");
				paramList.add(condition.get("unitCode"));
			}
			if(condition.containsKey("unitName")){
				sql.append(" and u.unitName like ? ");
				paramList.add("%"+condition.get("unitName")+"%");
			}
			sql.append(" order by u.unitCode");
		} else { //招生专业学习中心优惠
			sql.append("select u.resourceid unitId,u.unitcode,u.unitname,u.unitShortName,bm.recruitmajorid,m.recruitmajorname,rp.recruitplanname,p.resourceid paymentFeePrivilegeId,p.totalprivilegefee,p.memo ");
			sql.append(" from edu_recruit_brschmajor bm join edu_recruit_major m on m.resourceid=bm.recruitmajorid and m.isdeleted=0 ");
			sql.append(" join edu_recruit_brschplan bp on bm.brschplanid=bp.resourceid and bp.isdeleted=0 join edu_recruit_recruitplan rp on rp.resourceid=bp.recruitplanid join edu_base_year y on y.resourceid=rp.yearid ");
			sql.append(" join hnjk_sys_unit u on bp.branchschoolid=u.resourceid left join edu_fee_privilege p on u.resourceid=p.brschollid and m.resourceid=p.recruitmajorid and p.isdeleted=0 ");
			sql.append(" where bm.isdeleted=0 and bm.ispassed='Y' ");
			if(condition.containsKey("brSchool")){
				sql.append(" and u.resourceid=? ");
				paramList.add(condition.get("brSchool"));
			}
			if(condition.containsKey("recruitPlanId")){//招生批次
				sql.append(" and rp.resourceid=? ");
				paramList.add(condition.get("recruitPlanId"));
			}
			sql.append(" order by y.firstyear desc,rp.term desc,rp.publishDate desc,u.unitcode,m.recruitmajorname,m.resourceid ");
		}	
		return sql;
	}
	
	@Override
	public PaymentFeePrivilege getPaymentFeePrivilege(String brSchool, String recruitMajorId) throws ServiceException {
		List<Criterion> list = new ArrayList<Criterion>();
		list.add(Restrictions.eq("isDeleted", 0));
		list.add(Restrictions.eq("brSchool.resourceid", brSchool));
		if(ExStringUtils.isNotBlank(recruitMajorId)){//招生专业学习中心优惠设置
			list.add(Restrictions.eq("recruitMajor.resourceid", recruitMajorId));
		} else { //学习中心优惠设置
			list.add(Restrictions.isNull("recruitMajor"));
		}
		List<PaymentFeePrivilege> privilegeList = findByCriteria(list.toArray(new Criterion[list.size()]));
		return ExCollectionUtils.isNotEmpty(privilegeList) ? privilegeList.get(0) : null;
	}
}
