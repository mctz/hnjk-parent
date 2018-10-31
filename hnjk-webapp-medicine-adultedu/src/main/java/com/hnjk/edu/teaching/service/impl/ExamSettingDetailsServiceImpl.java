package com.hnjk.edu.teaching.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.teaching.model.ExamSettingDetails;
import com.hnjk.edu.teaching.service.IExamSettingDetailsService;
/**
 * 
 * <code>考试计划设置明细表ExamSettingDetailsServiceImpl</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-8-17 下午05:19:47
 * @see 
 * @version 1.0
 */
@Transactional
@Service("examSettingDetailsService")
public class ExamSettingDetailsServiceImpl extends BaseServiceImpl<ExamSettingDetails> implements IExamSettingDetailsService{
	
	@Override
	public List<ExamSettingDetails> findAllExamSettingDetails()throws ServiceException{
		
		StringBuffer hql = new StringBuffer(" from "+ExamSettingDetails.class.getSimpleName()+" d where d.isDeleted=0");
		return (List<ExamSettingDetails>) exGeneralHibernateDao.findByHql(hql.toString());
	}
}
