
/**
 * <code>IGradeFeeRuleService</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-4-26 下午03:04:18
 * @see 
 * @version 1.0
*/

package com.hnjk.edu.finance.service;

import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.finance.model.StudentFeeRule;

/**
 * <code>IGradeFeeRuleService</code><p>
 * 年级预交费用设置表.
 * @author： 广东学苑教育发展有限公司
 * @since： 2010-4-26 下午03:04:18
 * @see 
 * @version 1.0
 */
public interface IGradeFeeRuleService extends IBaseService<StudentFeeRule> {

	Page findClassicByCondition(Map<String, Object> condition, Page objPage) throws ServiceException;

}
