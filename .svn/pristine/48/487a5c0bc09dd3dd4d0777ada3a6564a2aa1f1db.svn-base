package com.hnjk.edu.basedata.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.basedata.model.Examroom;

/**
 * 考场课室
 * <code>IExamroomService</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2010-4-7 下午03:34:04
 * @see 
 * @version 1.0
 */
public interface IExamroomService extends IBaseService<Examroom>{
	
	/**
	 * 查询分页
	 * @param condition
	 * @param objPage
	 * @return
	 */
	Page findExamRoomByCondition(Map<String, Object> condition, Page objPage) throws ServiceException;
	
	void batchCascadeDelete(String[] split) throws ServiceException;
	
	public List<Examroom> findExamRoomByBranchSchool(String schoolid) throws ServiceException;
	 /**
	  * 根据传入的ID使用IN语句查询Examroom列表
	  * @param ids
	  * @return
	  * @throws ServiceException
	  */
	public List<Examroom> findExamRoomListByIds(String ids) throws ServiceException;
	
}
