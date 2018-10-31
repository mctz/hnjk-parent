package com.hnjk.platform.system.service;

import java.util.List;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.platform.system.model.Attachs;

/**
 * 公共附件服务接口.
 * <code>IAttachsService</code><p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-4-21 下午05:51:30
 * @see 
 * @version 1.0
 */
public interface IAttachsService extends IBaseService<Attachs>{

	/**
	 * 根据表单ID更新附件
	 * @param attachId
	 * @param formId
	 * @param formType
	 * @return 
	 * @throws ServiceException
	 */
	public long updateAttachByFormId(String[] attachIds,String formId,String formType,String fillinManId,String fillinMan) throws ServiceException;
	/**
	 * 根据表单ID获取附件列表
	 * @param formId
	 * @return
	 * @throws ServiceException
	 */
	List<Attachs> findAttachsByFormId(String formId) throws ServiceException;
	
	/**
	 * 根据附件存储在服务器上的名称，表单类型和附件存储在服务器上的存储路径查询附件
	 * 
	 * @param serName
	 * @param formType
	 * @param serPath
	 * @return
	 * @throws ServiceException
	 */
	Attachs getByConditions(String serName,String formType,String serPath ) throws ServiceException;
	
	/**
	 * 获取文件后缀名
	 * @param attachIds
	 * @return
	 */
	public String getPostfixAttr(String[] attachIds);
}
