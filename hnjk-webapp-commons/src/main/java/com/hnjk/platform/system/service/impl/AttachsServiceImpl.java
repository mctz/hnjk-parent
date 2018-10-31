package com.hnjk.platform.system.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.platform.system.model.Attachs;
import com.hnjk.platform.system.service.IAttachsService;

/**
 * 公共附件接口实现.
 * <code>AttachsServiceImpl</code><p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-4-21 下午05:52:01
 * @see 
 * @version 1.0
 */
@Service("attachsService")
@Transactional
public class AttachsServiceImpl extends BaseServiceImpl<Attachs> implements IAttachsService {

	@Override
	public long updateAttachByFormId(String[] attachIds, String formId, String formType, String fillinManId, String fillinMan) throws ServiceException {
		long size = 0;
		if(null != attachIds && attachIds.length >0){
			for(int i = 0;i<attachIds.length;i++){
				Attachs attachs = get(attachIds[i]);
				attachs.setFormId(formId);
				attachs.setFormType(formType);
				attachs.setFillinName(fillinMan);
				attachs.setFillinNameId(fillinManId);
				size += attachs.getAttSize();
				update(attachs);
				logger.info("更新附件...,{}",attachs.getAttName());
			}
		}
		return size;
	}

	@Override
	public List<Attachs> findAttachsByFormId(String formId) throws ServiceException {
		List<Attachs> attachs = findByHql(" from "+Attachs.class.getName()+" where isDeleted=0 and formId=? ", formId);
		return attachs;
	}

	/**
	 * 根据附件存储在服务器上的名称，表单类型和附件存储在服务器上的存储路径查询附件
	 * 
	 * @param serName
	 * @param formType
	 * @param serPath
	 * @return
	 * @throws ServiceException
	 */	@Override
	public Attachs getByConditions(String serName, String formType, String serPath) throws ServiceException {
		String hql = " from "+Attachs.class.getSimpleName()+" where isDeleted=0 and serName=? and formType=? and serPath=? ";
		return findUnique(hql, serName,formType,serPath);
	}

	@Override
	public String getPostfixAttr(String[] attachIds) {
		List<String> list = new ArrayList<String>();
		if(null != attachIds && attachIds.length >0){
			for(int i = 0;i<attachIds.length;i++){
				Attachs attachs = get(attachIds[i]);
				if(!list.contains(attachs.getPostfix())){
					list.add(attachs.getPostfix());
				}
			}
		}
		return ExStringUtils.toString(list);
	}

	
}
