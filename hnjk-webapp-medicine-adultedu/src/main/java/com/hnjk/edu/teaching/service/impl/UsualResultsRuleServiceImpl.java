package com.hnjk.edu.teaching.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.ExCollectionUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.teaching.model.UsualResultsRule;
import com.hnjk.edu.teaching.service.IUsualResultsRuleService;
/**
 * 学生平时分积分规则服务接口实现
 * <code>UsualResultsRuleService</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-12-20 下午05:02:52
 * @see 
 * @version 1.0
 */
@Transactional
@Service("usualResultsRuleService")
public class UsualResultsRuleServiceImpl extends BaseServiceImpl<UsualResultsRule> implements IUsualResultsRuleService {

	@Override
	public void batchCascadeDelete(String[] ids) throws ServiceException {
		if(ids!=null && ids.length>0){
			for(String id : ids){
				delete(id);	
				logger.info("批量删除="+id);
			}
		}		
	}

	@Override
	@Transactional(readOnly=true)
	public Page findUsualResultsRuleByCondition(Map<String, Object> condition, Page objPage) throws ServiceException {
		Map<String,Object> values =  new HashMap<String, Object>();
		String hql = " from "+UsualResultsRule.class.getSimpleName()+" where 1=1 ";
		hql += " and isDeleted = :isDeleted ";
		values.put("isDeleted", 0);

		if(condition.containsKey("courseName")){
			hql += " and lower(course.courseName) like:courseName ";
			values.put("courseName", "%"+condition.get("courseName").toString().toLowerCase()+"%");
		}
		if(condition.containsKey("courseId")){
			hql += " and course.resourceid =:courseId ";
			values.put("courseId", condition.get("courseId"));
		}
		if(condition.containsKey("yearInfoId")){
			hql += " and yearInfo.resourceid =:yearInfoId ";
			values.put("yearInfoId", condition.get("yearInfoId"));
		}
		if(condition.containsKey("term")){
			hql += " and term =:term ";
			values.put("term", condition.get("term"));
		}
		if(condition.containsKey("courseCode")){
			hql += " and course.courseCode like:courseCode ";
			values.put("courseCode", "%"+condition.get("courseCode")+"%");
		}
		if(ExStringUtils.isNotEmpty(objPage.getOrderBy())){			
			hql += " order by "+objPage.getOrderBy() +" "+ objPage.getOrder();
		}
		return exGeneralHibernateDao.findByHql(objPage, hql, values);
	}

	@Override
	public Integer getNextVersionNum(String courseId) throws ServiceException {
		Integer versionNum = exGeneralHibernateDao.findUnique(" select max(r.versionNum) from "+UsualResultsRule.class.getSimpleName()+" r where r.isDeleted=0 and r.course.resourceid=? ", courseId);
		return null!=versionNum?versionNum.intValue()+1:1;
	}

	@Override
	public UsualResultsRule getUsualResultsRuleByCourse(String courseId) throws ServiceException {
		List<UsualResultsRule> list = findByHql(" select u from "+UsualResultsRule.class.getSimpleName()+" u where u.isDeleted=0 and u.course.resourceid=? order by u.versionNum desc,u.fillinDate desc ", courseId);
		return (null!=list&&!list.isEmpty())?list.get(0):null;
	}
	
	@Override
	public UsualResultsRule getUsualResultsRuleByCourseAndYear(String courseId, String yearInfoId, String term) throws ServiceException {
		List<UsualResultsRule> list = findByHql(" from "+UsualResultsRule.class.getSimpleName()+" where isDeleted=0 and course.resourceid=? and yearInfo.resourceid=? and term=? order by versionNum desc  ", courseId,yearInfoId,term);
		return ExCollectionUtils.isNotEmpty(list)?list.get(0):null;
	}
	
}
