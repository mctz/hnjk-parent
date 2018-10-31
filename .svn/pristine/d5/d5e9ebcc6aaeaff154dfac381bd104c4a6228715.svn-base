
/**
 * <code>GradeFeeRuleServiceImpl</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-4-26 下午03:04:59
 * @see 
 * @version 1.0
*/

package com.hnjk.edu.finance.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.finance.model.StudentFeeRule;
import com.hnjk.edu.finance.service.IGradeFeeRuleService;

/**
 * <code>GradeFeeRuleServiceImpl</code><p>
 * 年级预交费用设置表.
 * @author： 广东学苑教育发展有限公司
 * @since： 2010-4-26 下午03:04:59
 * @see 
 * @version 1.0
 */
@Transactional
@Service("gradefeeruleservice")
public class GradeFeeRuleServiceImpl extends BaseServiceImpl<StudentFeeRule> implements IGradeFeeRuleService {

	@Override
	public Page findClassicByCondition(Map<String, Object> condition, Page objPage) throws ServiceException{
		Map<String,Object> values =  new HashMap<String, Object>();
		String hql = " from "+StudentFeeRule.class.getSimpleName()+" where 1=1 ";
		hql += " and isDeleted = :isDeleted ";
		values.put("isDeleted", 0);
		
		if(condition.containsKey("disName")){
			hql += " and disName like :disName ";
			values.put("disName", condition.get("disName"));
		}
		
		return exGeneralHibernateDao.findByHql(objPage, hql, values);
	}
	
}
