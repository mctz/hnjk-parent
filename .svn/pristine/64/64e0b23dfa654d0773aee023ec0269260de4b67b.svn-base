package com.hnjk.edu.teaching.service.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.ExBeanUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.teaching.model.ExamSetting;
import com.hnjk.edu.teaching.model.ExamSettingDetails;
import com.hnjk.edu.teaching.service.ICourseService;
import com.hnjk.edu.teaching.service.IExamSettingDetailsService;
import com.hnjk.edu.teaching.service.IExamSettingService;

/**
 * 考试计划设置
 * <code>ExamSettingController</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-8-17 下午03:58:46
 * @see 
 * @version 1.0
 */
@Transactional
@Service("examSettingService")
public class ExamSettingServiceImpl extends BaseServiceImpl<ExamSetting> implements IExamSettingService {

	@Autowired
	@Qualifier("examSettingDetailsService")
	private IExamSettingDetailsService examSettingDetailsService;
	
	@Autowired
	@Qualifier("courseService")
	private ICourseService courseService;
	
	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;
	
	@Override
	@Transactional(readOnly=true)
	public Page findExamSettingByCondition(Map<String, Object> condition, Page objPage) throws ServiceException{
		Map<String,Object> values =  new HashMap<String, Object>();
		String hql = " from "+ExamSetting.class.getSimpleName()+" setting where 1=1 ";
		hql += " and setting.isDeleted = :isDeleted ";
		values.put("isDeleted", 0);
		
		if(condition.containsKey("settingName")){
			hql += " and setting.settingName like :settingName ";
			values.put("settingName", condition.get("settingName")+"%");
		}
		if (condition.containsKey("brshSchool")) {
			hql += " and setting.brSchool.resourceid=:brshSchool";
			values.put("brshSchool",condition.get("brshSchool"));
		}
		hql += " order by "+objPage.getOrderBy() +" "+ objPage.getOrder();
		return exGeneralHibernateDao.findByHql(objPage, hql, values);
	}

	@Override
	public void batchDelete(String[] ids) throws ServiceException{
		if(ids!=null && ids.length>0){
			for(String id : ids){
				delete(id);	
				logger.info("批量删除="+id);
			}
		}
	}
  @Override
  public void deleteExamCourse(String id)
  {
	  exGeneralHibernateDao.delete(ExamSettingDetails.class, id);
  }
  @Override
  public ExamSettingDetails getExamCourse(String id)
  {
	  return (ExamSettingDetails) exGeneralHibernateDao.get(ExamSettingDetails.class, id);
  }
 /**
  *  更新考试计划设置
  */
  @Override
  public void updateExamSetting(String[] courseIds, ExamSetting examSetting) throws ServiceException {
	 
	  ExamSetting p_ExamSetting = this.get(examSetting.getResourceid());
	  p_ExamSetting.getDetails().clear();
	  ExBeanUtils.copyProperties(p_ExamSetting, examSetting);
	  /*p_ExamSetting.setSettingName(examSetting.getSettingName());
	  p_ExamSetting.setStartTime(examSetting.getStartTime());
	  p_ExamSetting.setEndTime(examSetting.getEndTime());
	  p_ExamSetting.setTimeSegment(examSetting.getTimeSegment());*/
	  
	  if (courseIds!=null && courseIds.length>0) {
			
		for (int index = 0; index < courseIds.length; index++) {
		
			ExamSettingDetails examCourse = new ExamSettingDetails();
			Course course                 = courseService.get(courseIds[index]);
			examCourse.setCourseId(course);
			examCourse.setExamSetting(p_ExamSetting);
			examCourse.setCourseName(course.getCourseName());
			examCourse.setShowOrder(index);
			p_ExamSetting.getDetails().add(examCourse);
		}
	  }

	  this.update(p_ExamSetting);	
  }


  /**
	 * 关联子表查询考试计划设置
	 * @param condition
	 * @param objPage
	 * @return
	 * @throws ServiceException
	 */
  @Override
  public Page findExamSettingJoinSubTable(Map<String, Object> condition,Page objPage) throws ServiceException {
	  
	 /* StringBuffer hql = new StringBuffer(" select setting from "+ExamSetting.class.getSimpleName()+" setting ");
	  hql.append(" inner join "+ExamSettingDetails.class.getSimpleName()+ " details ");
	  hql.append(" on details.examSetting.resourceid = setting.resourceid  and details.isDeleted=0");
	  
	  //--------------------------------子表的查询条件--------------------------------
	  if (condition.containsKey("courseid"))  
	  hql.append("	and details.courseId.resourceid=:courseid");
	  //--------------------------------子表的查询条件--------------------------------
	  
	  
	  
	  //--------------------------------主表的查询条件--------------------------------
	  hql.append("  where setting.isDeleted=0");
	  
	  if (condition.containsKey("brshSchool")) 
	  hql.append(" and setting.brSchool.resourceid=:brshSchool");
	  
	  if (condition.containsKey("settingName")) 
	  hql.append(" and setting.settingName like '%"+condition.get("settingName")+"%'");
	  
	  if (condition.containsKey("timeSegment")) 
	  hql.append(" and setting.timeSegment=:timeSegment");	  
	  
	  if (condition.containsKey("startTime")) 
	  hql.append(" and setting.startTime >= to_date('"+condition.get("startTime")+"','yyyy-MM-dd')");	  
	  
	  if (condition.containsKey("endTime")) 
      hql.append(" and setting.endTime <= to_date('"+condition.get("endTime")+"','yyyy-MM-dd')");	
	  //--------------------------------主表的查询条件--------------------------------
*/  
	  
	  StringBuffer hql = new StringBuffer(" select  distinct setting from "+ExamSetting.class.getSimpleName()+" as setting ");
	  hql.append(" left join setting.details as det");
	  hql.append(" where  1=1 ");
	  hql.append(" and setting.isDeleted=0 "); 
	  if (condition.containsKey("courseid")) {
		  hql.append(" and det.courseId.resourceid=:courseid");
	  }
	 
	  if (condition.containsKey("timeSegment")) {
		  hql.append(" and setting.timeSegment=:timeSegment");
	  }
	
	  if (condition.containsKey("brshSchool")) {
		  hql.append(" and setting.brSchool.resourceid=:brshSchool");
	  }
	  
	  if (condition.containsKey("settingName")) {
		  hql.append(" and setting.settingName like '%"+condition.get("settingName")+"%'");
	  }
	  
	  if (condition.containsKey("timeSegment")) {
		  hql.append(" and setting.timeSegment=:timeSegment");
	  }
	  
	  if (condition.containsKey("startTime")) {
		  hql.append(" and setting.startTime >= to_date('1970-1-1 "+condition.get("startTime")+"','yyyy-MM-dd hh24:mi:ss')");
	  }
	  
	  if (condition.containsKey("endTime")) {
		  hql.append(" and setting.endTime <= to_date('1970-1-1 "+condition.get("endTime")+"','yyyy-MM-dd hh24:mi:ss')");
	  }
	  
	  
	  hql.append(" order by "+objPage.getOrderBy() +" "+ objPage.getOrder());
	  
	return exGeneralHibernateDao.findByHql(objPage, hql.toString(), condition);
  }

}
