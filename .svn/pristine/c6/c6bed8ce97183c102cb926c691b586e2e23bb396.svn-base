package com.hnjk.edu.learning.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.learning.model.StudentSelfExercise;
import com.hnjk.edu.learning.service.IStudentSelfExerciseService;
import com.hnjk.platform.system.service.IAttachsService;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;
/**
 * 学生自荐作业服务.
 * <code>StudentSelfExerciseServiceImpl</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2012-12-14 下午04:10:39
 * @see 
 * @version 1.0
 */
@Transactional
@Service("studentSelfExerciseService")
public class StudentSelfExerciseServiceImpl extends BaseServiceImpl<StudentSelfExercise> implements IStudentSelfExerciseService {
	
	@Autowired
	@Qualifier("attachsService")
	private IAttachsService attachsService;
	
	@Override
	public void saveOrUpdateStudentSelfExercise(StudentSelfExercise exercise, String[] attachids) throws ServiceException {
		saveOrUpdate(exercise);
		
		User user = SpringSecurityHelper.getCurrentUser();
		attachsService.updateAttachByFormId(attachids, exercise.getResourceid(), StudentSelfExercise.class.getSimpleName(), user.getResourceid(), user.getCnName());
	}
	
	@Override
	@Transactional(readOnly=true)
	public Page findStudentSelfExerciseByCondition(	Map<String, Object> condition, Page objPage) throws ServiceException {
		Map<String, Object> values = new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer();
		hql.append("from "+StudentSelfExercise.class.getSimpleName()+" where isDeleted=:isDeleted ");
		values.put("isDeleted", 0);
		if(condition.containsKey("courseId")){//课程
			hql.append(" and course.resourceid=:courseId ");
			values.put("courseId", condition.get("courseId"));
		}
		if(condition.containsKey("isPublished")){//是否发布
			hql.append(" and isPublished=:isPublished ");
			values.put("isPublished", condition.get("isPublished"));
		}
		if(objPage.isOrderBySetted()){
			hql.append(" order by "+objPage.getOrderBy()+" "+objPage.getOrder());
		}
		return findByHql(objPage, hql.toString(), values);
	}
	
	@Override
	public void publishStudentSelfExercise(String[] ids, String isPublished) throws ServiceException {
		if(ids != null && ids.length > 0){
			Date curDate = new Date();
			for (String id : ids) {
				StudentSelfExercise selfExercise = get(id);
				selfExercise.setIsPublished(isPublished);
				selfExercise.setPublishDate(curDate);
			}
		}
	}
}
