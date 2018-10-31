package com.hnjk.edu.teaching.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.teaching.model.GraduatePapersNotice;
import com.hnjk.edu.teaching.model.GraduatePapersOrder;
import com.hnjk.edu.teaching.service.IGraduatePapersNoticeService;
import com.hnjk.platform.system.model.Attachs;
import com.hnjk.platform.system.service.IAttachsService;
import com.hnjk.security.model.User;

/**
 * 毕业导师公告表.
 * <code>GraduatePapersNoticeServiceImpl</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2010-10-28 下午04:39:37
 * @see 
 * @version 1.0
 */
@Transactional
@Service("graduatepapersnoticeservice")
public class GraduatePapersNoticeServiceImpl extends BaseServiceImpl<GraduatePapersNotice> implements IGraduatePapersNoticeService {

	@Override
	public Page findgraduateNoticeByCondition(Map<String, Object> condition, Page objPage) {
		Map<String,Object> values =  new HashMap<String, Object>();
		String hql = " from "+GraduatePapersNotice.class.getSimpleName()+" n where  n.isDeleted = :isDeleted  ";	
		values.put("isDeleted", 0);
		
		if(condition.containsKey("examSub")){
			hql += " and n.examSub.resourceid = :id ";
			values.put("id", condition.get("examSub"));
		}
		if(condition.containsKey("title")){
			hql += " and n.title like :title ";
			values.put("title", "%"+condition.get("title")+"%");
		}
		if(condition.containsKey("guidTeacherId")){//导师ID
			hql += " and n.guidTeacherId = :guidTeacherId ";
			values.put("guidTeacherId", condition.get("guidTeacherId"));
		}
		
		if(condition.containsKey("isStudent") && condition.get("isStudent").equals(Constants.BOOLEAN_YES)){//如果学生	
			hql += " and exists ( select o.teacher.resourceid from "+GraduatePapersOrder.class.getSimpleName()+" o  where o.isDeleted=0 and n.guidTeacherId = o.teacher.resourceid and o.examSub.resourceid=n.examSub.resourceid and o.studentInfo.sysUser.resourceid = :studentId )"; 
			if(condition.containsKey("studentId")){
				values.put("studentId", condition.get("studentId"));
			}
			/*
			if(condition.containsKey("guidTeacherIds") && ExStringUtils.isNotEmpty(condition.get("guidTeacherIds").toString())){
				String str = condition.get("guidTeacherIds").toString().substring(0, condition.get("guidTeacherIds").toString().length()-1);
				hql += " and guidTeacherId in ("+str+") ";
				//values.put("guidTeacherId",str);
			}
			*/
		}
		
		hql += " order by "+objPage.getOrderBy() +" "+ objPage.getOrder();
		
		return exGeneralHibernateDao.findByHql(objPage, hql, values);
	}

	@Override
	public void batchCascadeDelete(String[] ids) {
		if(ids!=null && ids.length>0){
			for(String id : ids){
				delete(id);	
				logger.info("批量删除="+id);
			}
		}
	}

	@Override
	public void saveOrUpdate(GraduatePapersNotice graduate, String[] files, User user) {
		saveOrUpdate(graduate);
		if(null!=files && files.length>0){
			for(String id : files){
				Attachs attach = attachsService.get(id);
				attach.setFillinNameId(user.getResourceid());
				attach.setFillinName(user.getCnName());
				attach.setFormId(graduate.getResourceid());
				attach.setFormType("GraduatePapersNotice");
				attachsService.saveOrUpdate(attach);
			}
		}
	}
	
	@Autowired
	@Qualifier("attachsService")
	IAttachsService attachsService;
	
}
