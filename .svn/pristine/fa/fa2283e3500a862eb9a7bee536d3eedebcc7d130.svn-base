package com.hnjk.edu.roll.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.roll.model.Classes;
/**
 * 班级服务接口.
 * <code>IClassesService</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2013-4-8 上午09:32:38
 * @see 
 * @version 1.0
 */
public interface IClassesService extends IBaseService<Classes> {
	/**
	 * 分页查询班级信息
	 * @param condition
	 * @param objPage
	 * @return
	 * @throws ServiceException
	 */
	Page findClassesByCondition(Map<String,Object> condition, Page objPage) throws ServiceException;
	List<Classes> findClassesByCondition(Map<String,Object> condition) throws ServiceException;
	/**
	 * 默认分班
	 * @throws ServiceException
	 */
	void assignStudentClasses(String brSchoolid) throws ServiceException;
	/**
	 * 调整分班
	 * @param ids
	 * @param classesid
	 * @throws ServiceException
	 */
	void adjustStudentClasses(String[] ids, String classesid) throws ServiceException;
	/**
	 * 删除分班
	 * @param ids
	 * @throws ServiceException
	 */
	void removeStudentClasses(String[] ids) throws ServiceException;
	public List<Map<String,Object>> getClassesByCondition(Map<String,Object> condition) throws ServiceException;
	
	/**
	 * 获取班级信息（包括教学计划和年级教学计划信息）
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	List<Map<String,Object>> findClassInfo(Map<String,Object> condition) throws ServiceException;
	
	/**
	 * 根据条件构造成select标签中的option(只供select标签用)
	 * 
	 * @param condition
	 * @param defaultValue
	 * @param hasSelect 是否有select
	 * @param classesInfo
	 * @return
	 */
	String constructOptions(Map<String,Object> condition, String defaultValue,boolean hasSelect, Map<String, Object> classesInfo);
	
	/**
	 * 根据班级ID集合获取名称集合
	 * 
	 * @param ids
	 * @return
	 * @throws ServiceException
	 */
	String findNamesByIds(String ids) throws ServiceException;
	
	/**
	 * 获取该用户作为班主任的所有班级
	 * @param userId
	 * @return
	 * @throws Exception 
	 */
	public String findByMasterId(String userId) throws Exception;
	
	/**
	 * 获取某教学点的所有班级（用“，”连接）
	 * @param schoolId
	 * @return
	 * @throws Exception 
	 */
	public String findBySchoolId(String schoolId);
	
	/**
	 * 根据班号生成规则获取流水号
	 * @param prefix 前缀
	 * @param bits 流水号位数（不足补0，如果bits为0则不补位）
	 * @return
	 */
	public String getClassCode(String prefix,int bits);
}
