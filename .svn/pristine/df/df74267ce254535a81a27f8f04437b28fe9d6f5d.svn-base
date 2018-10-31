package com.hnjk.edu.basedata.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.basedata.model.Edumanager;
import com.hnjk.security.model.OrgUnit;
import com.hnjk.security.model.User;

/**
 * <code>IEduManagerService</code>基础数据-教务管理人员信息-服务接口.<p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-3-15 下午02:07:27
 * @see 
 * @version 1.0
 */
public interface IEdumanagerService extends IBaseService<Edumanager>{
	/**
	 * 查询分页
	 * @param condition
	 * @param objPage
	 * @return
	 * @throws ServiceException
	 */
	Page findEdumanagerByCondition(Map<String, Object> condition, Page objPage) throws ServiceException;
	/**
	 * 查询分页
	 * @param condition
	 * @param objPage
	 * @return
	 * @throws ServiceException
	 */
	//Page findTeacherByCondition(Map<String, Object> condition,Page objPage) throws ServiceException;
	
	/**批量删除
	 * @param ids
	 */
	void batchCascadeDelete(String[] ids) throws ServiceException;
	
	/**
	 * 批量注册
	 */
	//void batchRegist(String[] ids);
	/**
	 * 是否存在教师编号
	 * @param teacherCode
	 * @return
	 */
	boolean isExistsTeacherCode(String teacherCode) throws ServiceException;
	
	/**
	 * 是否存在该账号的用户
	 * 
	 * @param userName
	 * @return
	 * @throws ServiceException
	 */
	boolean isExistsUser(String userName) throws ServiceException;
	
	/**
	 * 根据条件查找列表
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	List<Edumanager> findEdumanagerByCondition(Map<String, Object> condition) throws ServiceException;

	List<Edumanager> findEdumanagerListByCondition(Map<String, Object> condition)
			throws ServiceException;
	
	/**
	 * 是否校外学习中心
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 */
	boolean isBrSchool(User user) throws Exception;

	/**
	 * 获取教师编号
	 * <p>
	 * @param teacherCodeRule 编号规则
	 * @param unit 教学点
	 * @return
	 */
	String getTeacherCode(String teacherCodeRule, OrgUnit unit);
}
