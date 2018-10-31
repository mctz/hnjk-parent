package com.hnjk.edu.roll.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.roll.model.GraduateNograduate;
import com.hnjk.security.model.User;

/**
 * 学生不毕业申请信息表
 * <code>IGraduateNograduateService</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2010-10-28 下午03:34:46
 * @see 
 * @version 1.0
 */
public interface IGraduateNograduateService extends IBaseService<GraduateNograduate>{

	Page findGraduateByCondition(Map<String, Object> condition, Page objPage);

	void batchCascadeDelete(String[] split);

	void batchAudit(String[] ids, User user);

	void audit(String resourceid, User user);
	
	void batchNoGraduate();
	
	void batchRevoke();
	
	public List<GraduateNograduate> findGraduateByCondition_List(Map<String, Object> condition, Page objPage);
}
