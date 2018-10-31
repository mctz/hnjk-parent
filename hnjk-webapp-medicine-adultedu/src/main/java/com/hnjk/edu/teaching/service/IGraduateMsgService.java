package com.hnjk.edu.teaching.service;

import java.util.Map;

import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.teaching.model.GraduateMsg;
import com.hnjk.security.model.User;

/**
 * 毕业生与老师毕业论文交流表.
 * <code>IGraduateMsgService</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2010-10-28 下午04:21:22
 * @see 
 * @version 1.0
 */
public interface IGraduateMsgService extends IBaseService<GraduateMsg> {

	Page findGraduateMsgByCondition(Map<String, Object> condition, Page objPage);

	void batchCascadeDelete(String[] ids);

	void saveOrUpdate(GraduateMsg graduateMsg, String[] files, User user);

}
