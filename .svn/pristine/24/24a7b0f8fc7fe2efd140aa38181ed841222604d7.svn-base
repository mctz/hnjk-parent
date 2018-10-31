package com.hnjk.edu.teaching.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.teaching.model.ExamPaperBag;

/**
 * 
 * <code>试卷袋标签统计Service接口</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-8-20 上午10:47:02
 * @see 
 * @version 1.0
 */
public interface IExamPaperBagService  extends IBaseService<ExamPaperBag>{
	
	/**
	 * 根据条件查询试卷袋标签,返回分页对象
	 * @param page
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	public Page findExamPaperBagByCondition(Page page,Map<String,Object> condition)throws ServiceException;
	
	/**
	 * 根据条件统计试卷袋标签总袋数
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	public List<Object> findExamPaperBagCountByCondition(Map<String,Object> condition)throws ServiceException;
	
	/**
	 * 生成试卷袋标签数据
	 * @param condition
	 * @throws ServiceException
	 */
	public void genExamPagerBag(Map<String,Object> condition)throws ServiceException;
	
}
