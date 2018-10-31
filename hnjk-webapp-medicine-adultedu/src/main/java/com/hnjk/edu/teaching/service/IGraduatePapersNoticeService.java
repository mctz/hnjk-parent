package com.hnjk.edu.teaching.service;

import java.util.Map;

import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.teaching.model.GraduatePapersNotice;
import com.hnjk.security.model.User;

/**
 * 毕业导师公告表.
 * <code>IGraduatePapersNoticeService</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2010-10-28 下午04:38:22
 * @see 
 * @version 1.0
 */
public interface IGraduatePapersNoticeService extends IBaseService<GraduatePapersNotice>{

	Page findgraduateNoticeByCondition(Map<String, Object> condition, Page objPage);

	void batchCascadeDelete(String[] ids);

	void saveOrUpdate(GraduatePapersNotice graduate, String[] files, User user);

}
