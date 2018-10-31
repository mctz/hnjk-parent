package com.hnjk.edu.teaching.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.teaching.model.Syllabus;

/**
 * 教学大纲管理服务接口
 * <code>ISyllabusService</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-7-29 上午11:50:35
 * @see 
 * @version 1.0
 */
public interface ISyllabusService extends IBaseService<Syllabus> {
	
	/**
	 * 获取大纲知识结构树
	 * @param courseId
	 * @return
	 * @throws ServiceException
	 */
	List<Syllabus> findSyllabusTreeList(String courseId) throws ServiceException;
	
	/**
	 * 获取教学大纲目录树
	 * @param courseId 课程id
	 * @param isCOuntResources 是否需要统计该节点材料，用于学习互动中心，知识结构 
	 * @return
	 * @throws ServiceException
	 */
	List<Syllabus> findSyllabusTreeList(String courseId,boolean isCountResources) throws ServiceException;
	
	/**
	 * 根据父节点id查找子列表
	 * @param parentId
	 * @return
	 * @throws ServiceException
	 */
	List<Syllabus> findSyllabusByParentId(String parentId) throws ServiceException;
	/**
	 * 分页查询
	 * @param condition
	 * @param objPage
	 * @return
	 * @throws ServiceException
	 */
	Page findSyllabusByCondition(Map<String, Object> condition,Page objPage) throws ServiceException;
	
	/**
	 * 删除并更新
	 * @param resourceid
	 * @param size 要删除的子节点个数
	 * @throws ServiceException
	 */
	void deleteSyllabus(String resourceid,int size) throws ServiceException;
	/**
	 * 批量删除
	 * @param ids
	 */
	void batchCascadeDelete(String[] ids) throws ServiceException;
	/**
	 * 返回根节点
	 * @return
	 * @throws ServiceException
	 */
	Syllabus getSyllabusRoot(String courseId) throws ServiceException;
	/**
	 * 获取该节点下子节点排序号
	 * @param syllabusId
	 * @return
	 * @throws ServiceException
	 */
	Integer getNextShowOrder(String syllabusId) throws ServiceException;
	/**
	 * 计算知识节点数
	 * @param courseId
	 * @return
	 * @throws ServiceException
	 */
	Long countSyllabusTreeList(String courseId) throws ServiceException;
	/**
	 * 更新知识节点
	 * @param syllabusForm
	 * @throws ServiceException
	 */
	void saveOrUpdateSyllabus(Syllabus syllabusForm) throws ServiceException;
}
