package com.hnjk.platform.system.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.platform.system.model.PictureCarousel;
import com.hnjk.platform.system.service.IPictureCarouselService;


/**
 * 图片轮播接口实现
 *
 */
@Service("pictureCarouselService")
@Transactional
public class PictureCarouselServiceImpl extends BaseServiceImpl<PictureCarousel> implements IPictureCarouselService {

	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private  IBaseSupportJdbcDao baseSupportJdbcDao;
	
	/**
	 * 查询分页
	 * @param condition
	 * @param objPage
	 * @return
	 */
	@Override
	public Page findByCondition(Map<String, Object> condition, Page objPage) throws ServiceException {
		Map<String, Object> values = new HashMap<String, Object>();
		StringBuffer hql = findByConditionHql(condition, values);
		if(ExStringUtils.isNotBlank(objPage.getOrderBy())){
			hql.append(" order by "+objPage.getOrderBy());
		}
		if(ExStringUtils.isNotBlank(objPage.getOrder())){
			hql.append(" "+objPage.getOrder());
		}
		return findByHql(objPage, hql.toString(), values);
	}

	private StringBuffer findByConditionHql(Map<String, Object> condition, Map<String, Object> values) {
		StringBuffer hql = new StringBuffer("from "+PictureCarousel.class.getSimpleName()+" pc where pc.isDeleted=0 ");
		if(condition.containsKey("picName")){
			hql.append("and pc.picName like :picName ");
			values.put("picName", "%"+condition.get("picName")+"%");
		}
		if(condition.containsKey("isShow")){
			hql.append("and pc.isShow=:isShow ");
			values.put("isShow", condition.get("isShow"));
		}
		return hql;
	}

	/**
	 * 批量删除
	 * @param ids
	 */
	@Override
	public void batchCascadeDelete(String[] ids) throws ServiceException {
		if(ids!=null && ids.length>0){
			for(String id : ids){
				delete(id);				
			}
		}
	}

	/**
	 * 根据图片名获取实体
	 * @param word
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public PictureCarousel getByPicName(String picName) throws ServiceException {
		String hql = "from "+PictureCarousel.class.getSimpleName()+" pc where pc.isDeleted=0 and pc.picName=?";
		return findUnique(hql, picName);
	}

	

	/**
	 * 查询图片轮播列表--不分页
	 * @param condition
	 * @return
	 */
	@Override
	public List<PictureCarousel> findByCondition(Map<String, Object> condition) throws ServiceException {
		Map<String, Object> values = new HashMap<String, Object>();
		StringBuffer hql = findByConditionHql(condition, values);
		return findByHql(hql.toString(), values);
	}
	
	
}
