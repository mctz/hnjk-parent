package com.hnjk.edu.basedata.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.basedata.model.Classroom;
/**
 * 课室管理服务接口
 * <code>IClassroomService</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-10-21 下午02:03:20
 * @see 
 * @version 1.0
 */
public interface IClassroomService extends IBaseService<Classroom> {
	/**
	 * 查询分页
	 * @param condition
	 * @param objPage
	 * @return
	 */
	Page findClassroomByCondition(Map<String, Object> condition, Page objPage) throws ServiceException;
	
	/**
	 * 查询
	 * @param condition
	 * @return
	 */
	List<Classroom> findClassroomByCondition(Map<String, Object> condition) throws ServiceException;
	
	/**
	 * 批量删除
	 * @param ids
	 */
	void batchCascadeDelete(String[] ids) throws ServiceException;
	/**
	 * 获取下一个排序号
	 * @return
	 * @throws ServiceException
	 */
	Long getNextShowOrder(String buildingId) throws ServiceException;
	
	Page findClassroomByHql(Map<String, Object> condition, Page objPage) throws ServiceException;

	String constructOptions(Map<String, Object> condition, String defaultValue) throws ServiceException;

	/**
	 * 查询不在范围内的课室名称
	 * @param timeValue
	 * @param brSchoolid
	 * @return
	 */
	String findNamesByOutResourceids(String ids, String brSchoolid);
}
