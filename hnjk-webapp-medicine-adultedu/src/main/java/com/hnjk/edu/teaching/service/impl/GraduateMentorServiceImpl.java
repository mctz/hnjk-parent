package com.hnjk.edu.teaching.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.teaching.model.GraduateMentor;
import com.hnjk.edu.teaching.model.GraduateMentorDetails;
import com.hnjk.edu.teaching.model.GraduatePapersOrder;
import com.hnjk.edu.teaching.service.IGraduateMentorService;
import com.hnjk.edu.teaching.service.IGraduatePapersOrderService;

/**
 * 毕业论文批次与指导老师关系维护表.
 * <code>GraduateMentorServiceImpl</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2010-10-28 下午04:59:48
 * @see 
 * @version 1.0
 */
@Transactional
@Service("graduatementorservice")
public class GraduateMentorServiceImpl extends BaseServiceImpl<GraduateMentor> implements IGraduateMentorService {

	@Autowired
	@Qualifier("graduatepapersorderservice")
	private IGraduatePapersOrderService graduatePapersOrderService;
		
	
	/* (non-Javadoc)
	 * @see com.hnjk.core.support.base.service.BaseServiceImpl#save(java.lang.Object)
	 */
	@Override
	public GraduateMentor save(GraduateMentor entity) throws ServiceException {
				
		//updateGraduatePapersOrder(entity);		
		//检查老师是否存在重复	
		List<GraduateMentor> list = findByHql("from "+GraduateMentor.class.getSimpleName()+" where isDeleted = ? and edumanager.resourceid = ? and examSub.resourceid = ?", 
									0,entity.getEdumanager().getResourceid(),entity.getExamSub().getResourceid());
		if(null != list && list.size()>0){
			throw new ServiceException("老师 "+entity.getEdumanager().getCnName()+" 已在该批次中.<br/>请在列表中选择该老师进行编辑.");
		}
		return super.save(entity);
	}

	//更新学生预约信息
	private void updateGraduatePapersOrder(GraduateMentorDetails graduateMentorDetails,Boolean isDelete) throws ServiceException{		
		
		List<GraduatePapersOrder> list = graduatePapersOrderService.findByHql("from "+GraduatePapersOrder.class.getSimpleName()+" where isDeleted = ? " +
					" and studentInfo.resourceid = ? and examSub.resourceid = ?", 0,
					graduateMentorDetails.getStudentInfo().getResourceid(),
					graduateMentorDetails.getGraduateMentor().getExamSub().getResourceid());
			if(null != list){
				if(list.size()>1){
					throw new ServiceException("分配学生指导老师出错：可能是该学生在同一批次预约了多次.");
				}
				GraduatePapersOrder grduatePapersOrder = list.get(0);
				if(isDelete){
					grduatePapersOrder.setTeacher(null);
					grduatePapersOrder.setGuidTeacherName(null);
				}else{
					grduatePapersOrder.setTeacher(graduateMentorDetails.getGraduateMentor().getEdumanager());
					grduatePapersOrder.setGuidTeacherName(graduateMentorDetails.getGraduateMentor().getEdumanager().getCnName());
				}
					
				graduatePapersOrderService.update(grduatePapersOrder);
			}			
		
		
	}
	
	/* (non-Javadoc)
	 * @see com.hnjk.core.support.base.service.BaseServiceImpl#update(java.lang.Object)
	 */
	@Override
	public void update(GraduateMentor entity) throws ServiceException {			
		if(null != entity.getGraduateMentorDetails() && entity.getGraduateMentorDetails().size()>0){
			for(GraduateMentorDetails graduateMentorDetails : entity.getGraduateMentorDetails()){
				updateGraduatePapersOrder(graduateMentorDetails,false);
			}
		}
		super.update(entity);
	}

	@Override
	@Transactional(readOnly=true)
	public Page findGraduateMentorByCondition(Map<String, Object> condition, Page objPage) {
		Map<String,Object> values =  new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer(128);
		sb.append(" from "+GraduateMentor.class.getSimpleName()+" g where g.isDeleted = :isDeleted ");	
		values.put("isDeleted", 0);
		if(condition.containsKey("mentor")){
			sb.append(" and g.edumanager.cnName like :mentor ");
			values.put("mentor", "%"+condition.get("mentor")+"%");
		}
		if(condition.containsKey("batchId")){
			sb.append("and g.examSub.resourceid = :id ");
			values.put("id", condition.get("batchId"));
		}
		if(condition.containsKey("branchSchool")){//学生所属学校中心
			sb.append(" and exists (");
			sb.append(" select graduateMentor.resourceid from "+GraduateMentorDetails.class.getSimpleName()+" gt ");
			sb.append(" where gt.graduateMentor.resourceid = g.resourceid and gt.studentInfo.branchSchool.resourceid = :branchSchool ");
			sb.append(")");
			values.put("branchSchool", condition.get("branchSchool"));
		}
		sb.append(" order by "+objPage.getOrderBy() +" "+ objPage.getOrder());
		
		return exGeneralHibernateDao.findByHql(objPage, sb.toString(), values);
	}

	@Override
	public void batchCascadeDelete(String[] ids) {
		if(ids!=null && ids.length>0){
			for(String id : ids){
				delete(id);					
			}
		}
	}
	
	@Override
	public void delete(Serializable id) throws ServiceException {
		super.delete(id);
	}
	
	@Override
	public void deleteDetails(String gid, String[] arr) throws ServiceException{
		GraduateMentor graduateMentor = get(gid);	
		if(null != graduateMentor.getGraduateMentorDetails() && graduateMentor.getGraduateMentorDetails().size()>0){
			
			List<GraduateMentorDetails> removeList = new ArrayList<GraduateMentorDetails>();
			
			for(GraduateMentorDetails grDetails : graduateMentor.getGraduateMentorDetails()){
				for(int i=0;i<arr.length;i++){
					if(grDetails.getResourceid().equals(arr[i])){
						//graduateMentorDetailsService.truncate(graduateMentorDetailsService.get(arr[i]));							
						//graduateMentor.getGraduateMentorDetails().remove(grDetails);
						removeList.add(grDetails);		
						
						break;
					}
				}
			}
			////UPDATE 论文预约信息
			graduateMentor.getGraduateMentorDetails().removeAll(removeList);
			if(null != removeList){
				for(GraduateMentorDetails grDetails : removeList){
					updateGraduatePapersOrder(grDetails,true);
				}
			}
			
		}
		//update(graduateMentor);
	}
}
