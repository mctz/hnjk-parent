package com.hnjk.edu.learning.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.learning.model.MateResource;

/**
 * 课程素材管理服务接口
 * <code>IMateResourceService</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-8-17 下午05:12:43
 * @see 
 * @version 1.0
 */
public interface IMateResourceService extends IBaseService<MateResource> {
	/**
	 * 课程素材分页查找
	 * @param condition
	 * @param objPage
	 * @return
	 * @throws ServiceException
	 */
	Page findMateResourceByCondition(Map<String, Object> condition,Page objPage) throws ServiceException;
	/**
	 * 删除课程素材
	 * @param ids
	 * @throws ServiceException
	 */
	void batchCascadeDelete(String[] ids) throws ServiceException;
	/**
	 * 检查编码唯一性
	 * @param mateCode
	 * @return
	 * @throws ServiceException
	 */
	boolean isExistsMateCode(String mateCode) throws ServiceException;
	/**
	 * 查询列表
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	List<MateResource> findMateResourceByCondition(Map<String, Object> condition) throws ServiceException;
	/**
	 * 复制课件资源
	 * @param courseId
	 * @throws ServiceException
	 */
	void copyCourse(String fromCourseId,String toCourseId) throws ServiceException;
	/**
	 * 获取排序号
	 * @param id
	 * @param type
	 * @return
	 * @throws ServiceException
	 */
	Integer getNextShowOrder(String id, String type) throws ServiceException;
}
