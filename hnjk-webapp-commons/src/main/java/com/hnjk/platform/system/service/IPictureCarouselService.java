package com.hnjk.platform.system.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.platform.system.model.PictureCarousel;

/**
 * 图片轮播接口
 *
 */
public interface IPictureCarouselService extends IBaseService<PictureCarousel> {

	/**
	 * 查询分页
	 * @param condition
	 * @param objPage
	 * @return
	 */
	Page findByCondition(Map<String, Object> condition, Page objPage) throws ServiceException;
	
	/**
	 * 批量删除
	 * @param ids
	 */
	void batchCascadeDelete(String[] ids) throws ServiceException;
	
	/**
	 * 根据图片名获取实体
	 * @param word
	 * @return
	 * @throws ServiceException
	 */
	PictureCarousel getByPicName(String picName) throws ServiceException;
	
	
	/**
	 * 查询轮播图片列表--不分页
	 * @param condition
	 * @return
	 */
	@Override
	List<PictureCarousel> findByCondition(Map<String, Object> condition) throws ServiceException;
	
}
