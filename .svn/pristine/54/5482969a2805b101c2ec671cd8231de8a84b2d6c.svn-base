package com.hnjk.edu.teaching.service;

import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.teaching.model.GraduateMentor;

/**
 * 毕业论文批次与指导老师关系维护表.
 * <code>IGraduateMentor</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2010-10-28 下午04:54:38
 * @see 
 * @version 1.0
 */
public interface IGraduateMentorService extends IBaseService<GraduateMentor>{

	Page findGraduateMentorByCondition(Map<String, Object> condition, Page objPage);

	void batchCascadeDelete(String[] ids);

	void deleteDetails(String gid,String[] arr) throws ServiceException;
}
