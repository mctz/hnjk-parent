package com.hnjk.security.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.security.model.OrgUnit;

/**
 * 组织单位服务接口. <code>IOrgUnitService</code><p>;
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2009-12-4 上午09:54:15
 * @see 
 * @version 1.0 
 */
public interface IOrgUnitService extends IBaseService<OrgUnit>{
	
	/**
	 * 新增组织单位
	 * @param orgUnit
	 * @throws ServiceException
	 */
	void addOrgUnit(OrgUnit orgUnit) throws ServiceException;
	
	/**
	 * 更新组织单位
	 * @param orgUnit
	 * @throws ServiceException
	 */
	void updateOrgUnit(OrgUnit orgUnit) throws ServiceException;	
	
	/**
	 * 获取组织树
	 * @param parentcode 父编码
	 * @return
	 * @throws ServiceException
	 */
	List<OrgUnit> findOrgTree(String parentCode) throws Exception;
	
	/**
	 * 通过 parentId 获取组织架构数据
	 * @param parentId			父节点id
	 * @param containSelf		数据中是否包含本身?
	 * @param containChildrenOrg数据中是否要进行递归查询?
	 * @return
	 * @throws ServiceException
	 */
	List<OrgUnit> findOrgByParentId(String parentId, boolean containSelf, boolean containChildrenOrg) throws ServiceException;

	/**
	 * 分页列表
	 * @param condition
	 * @param order
	 * @param objPage
	 * @return
	 */
	Page findOrgByCondition(Map<String, Object> condition, Page objPage) throws ServiceException;
	
	/**
	 * 批量删除
	 * @param split
	 */
	void batchCascadeDelete(String[] split) throws ServiceException;

	/**
	 * 根据类型查找列表
	 * @param unitType
	 * @return
	 * @throws ServiceException
	 */
	List<OrgUnit> findOrgUnitListByType(String unitType) throws ServiceException;
	
	/**
	 * 分页列表
	 * 暂时用一种新的方法替代findOrgByCondition，因为OrgUnit中有orgUnitExtend EAGER 导致加载unit时同时加载extend，又因为one2many，会有多条记录出现。
	 * @param condition
	 * @param page
	 * @return
	 * @throws ServiceException
	 */
	Page findOrgByConditionByHql(Map<String, Object> condition, Page page) throws ServiceException;
	
	/**
	 * 根据教学点编码集合获取名称集合
	 * 
	 * @param codes
	 * @return
	 * @throws ServiceException
	 */
	String findNamesByCodes(String codes) throws ServiceException;
	
	/**
	 * 设置教学点收费形式
	 * @param payForm
	 * @param resourceids
	 * @throws Exception
	 */
	void setUnitPayForm(String payForm, String resourceids) throws Exception;
	
	/**
	 * 设置分成比例
	 * @param royaltyRate
	 * @param resourceids
	 * @throws Exception
	 */
	void setRoyaltyRate(BigDecimal royaltyRate,BigDecimal royaltyRate2,BigDecimal reserveRatio, String resourceids) throws Exception;

	/**
	 * 教学点
	 * @param condition
	 * @param defaultValue
	 * @param unique 1:一个选择项
	 * @return
	 * @throws ServiceException
	 */
	String constructOptions(Map<String, Object> condition,String defaultValue, int unique) throws ServiceException;

	OrgUnit findOrgByUnitName(String unitNameStr);
}
