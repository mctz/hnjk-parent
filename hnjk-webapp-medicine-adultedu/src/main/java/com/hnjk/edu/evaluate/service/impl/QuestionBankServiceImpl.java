package com.hnjk.edu.evaluate.service.impl;

import java.util.ArrayList;
import java.util.Date;
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
import com.hnjk.edu.evaluate.model.QuestionBank;
import com.hnjk.edu.evaluate.service.IQuestionBankService;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;

@Transactional
@Service("iQuestionBankService")
public class QuestionBankServiceImpl extends BaseServiceImpl<QuestionBank> implements IQuestionBankService{
	
	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;
	
	@Override
	@Transactional(readOnly=true)
	public Page findQuestionBankByCondition(Map<String, Object> condition,	Page objPage) throws ServiceException{
		
		Map<String,Object> values =  new HashMap<String, Object>();
		String hql = "from "+QuestionBank.class.getSimpleName()+" where isDeleted=:isDeleted order by courseType,showOrder";
		values.put("isDeleted", 0);
		return findByHql(objPage, hql, values);
		
	};
	
	@Override
	@Transactional(readOnly=true)
	public List<QuestionBank> findQuestionBankAll(Map<String, Object> condition) throws ServiceException{
		
		String hql = "from "+QuestionBank.class.getSimpleName()+" where isDeleted=:isDeleted and courseType=:courseType order by showOrder";
		
		return this.findByHql(hql, condition);
		
	};
	
	@Override
	@Transactional(readOnly=true)
	public double getFaceTotalScore(String courseType) throws ServiceException{
		
		Map<String,Object> values =  new HashMap<String, Object>();
		String sql = "select sum(score) from EDU_EVALUATE_QUESTIONBANK where isDeleted=:isDeleted and courseType=:courseType order by courseType,showOrder";
		values.put("isDeleted", 0);
		values.put("courseType", courseType);
		double sum=0.0;
		try {
			sum = this.baseSupportJdbcDao.getBaseJdbcTemplate().findForLong(sql, values);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sum;
		
	};
	@Override
	public boolean saveQuestion(QuestionBank qb) throws ServiceException{
		QuestionBank tmp ;
		if(ExStringUtils.isNotBlank(qb.getResourceid())){
			tmp = this.get(qb.getResourceid());
		}else{
			tmp = new QuestionBank();			
		}		
		tmp.setCourseType(qb.getCourseType());
		tmp.setScore(qb.getScore());
		tmp.setMemo(qb.getMemo());
		tmp.setShowOrder(qb.getShowOrder());
		tmp.setQuestionTarget(qb.getQuestionTarget());
		tmp.setQuestion(qb.getQuestion());
		tmp.setUpdatDate(new Date());
		User user = SpringSecurityHelper.getCurrentUser();
		tmp.setUpdateMan(user);
		try {
			this.saveOrUpdate(tmp);
			return true;
		} catch (Exception e) {
			logger.error("执行方法 saveQuestion() :保存 QuestionBank 实体时失败");
		}
		return false;
	}
	@Override
	public boolean deleteQuestion(String resourceids) throws ServiceException{
		
		List<QuestionBank> list = new ArrayList<QuestionBank>();
		User user = SpringSecurityHelper.getCurrentUser();
		for(String resourceid : resourceids.split("\\,")){
			QuestionBank qb = this.get(resourceid);
			if(qb!=null){
				qb.setIsDeleted(1);
				qb.setUpdatDate(new Date());
				qb.setUpdateMan(user);
				list.add(qb);
			}
		}
		if(list.size()>0){
			this.batchSaveOrUpdate(list);
			return true;
		}else{
			return false;
		}
		
	}
}
