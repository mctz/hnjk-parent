package com.hnjk.edu.evaluate.service.impl;

import java.rmi.ServerException;
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
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.YearInfo;
import com.hnjk.edu.basedata.service.IYearInfoService;
import com.hnjk.edu.evaluate.model.Questionnaire;
import com.hnjk.edu.evaluate.model.QuestionnaireBatch;
import com.hnjk.edu.evaluate.service.IQuestionnaireBatchService;
import com.hnjk.edu.evaluate.service.IQuestionnaireService;
import com.hnjk.edu.teaching.model.CourseTeacherCl;
import com.hnjk.edu.teaching.service.ICourseStatusClService;
import com.hnjk.security.service.IUserService;

@Transactional
@Service("iQuestionnaireBatchService")
public class QuestionnaireBatchServiceImpl extends BaseServiceImpl<QuestionnaireBatch> implements
		IQuestionnaireBatchService {

	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;
	
	@Autowired
	@Qualifier("courseStatusClService")
	private ICourseStatusClService courseStatusClService;
	
	@Autowired
	@Qualifier("userService")
	private IUserService iUserService;
	
	@Autowired
	@Qualifier("iQuestionnaireService")
	private IQuestionnaireService iQuestionnaireService;
	
	@Autowired
	@Qualifier("yearInfoService")
	private IYearInfoService iYearInfoService;
	
	@Override
	@Transactional(readOnly=true)
	public Page findQuestionnaireBatch(Map<String, Object> condition,	Page objPage) throws ServiceException{
		
		String hql="";
		hql = findQuestionnaireBatchHql(condition, hql);
		Page page =findByHql(objPage, hql, condition);
		return page;
		
	}

	/**
	 * @param condition
	 * @param values
	 * @param hql
	 * @return
	 */
	private String findQuestionnaireBatchHql(Map<String, Object> values, String hql) {
		
		hql+= "from "+QuestionnaireBatch.class.getSimpleName()+" where isDeleted=:isDeleted ";
		if(values.containsKey("yearId")){
			hql+= " and yearInfo.resourceid=:yearId ";
		}
		if(values.containsKey("term")){
			hql+= " and term=:term ";
		}
		if(values.containsKey("firstYear")){
			hql+= " and yearInfo.firstYear=:firstYear ";
		}
		if(values.containsKey("isPublish")){
			hql+= " and isPublish=:isPublish ";
		}
		if(values.containsKey("courseStatusTerm")){
			String firstYear=values.get("courseStatusTerm").toString().substring(0,4);
			String term=values.get("courseStatusTerm").toString().substring(6,7);
			values.put("firstYear", Long.parseLong(firstYear));
			values.put("term", term);
			values.remove("courseStatusTerm");
			hql+= " and yearInfo.firstYear=:firstYear ";
			hql+= " and term=:term ";
			
		}
		hql+=" order by yearInfo.firstYear,term ";
		values.put("isDeleted", 0);
		return hql;
	};
	
	@Override
	@Transactional(readOnly=true)
	public List<QuestionnaireBatch> findQuestionnaireBatchList(Map<String, Object> condition) throws ServiceException{
		//List<QuestionnaireBatch> qbList = new ArrayList<QuestionnaireBatch>();
		String hql="";
		hql = findQuestionnaireBatchHql(condition,hql);
		//qbList= this.findByHql(hql, condition);
		return this.findByHql(hql, condition);
		
	};
	@Override
	public boolean saveQuestionnaireBatch(QuestionnaireBatch qb, String yearId) throws ServiceException{
		QuestionnaireBatch tmp;
		YearInfo year = iYearInfoService.get(yearId);
		if(ExStringUtils.isNotBlank(qb.getResourceid())){//更新
			tmp = this.get(qb.getResourceid());
		}else{//新增			
			qb.setYearInfo(year);
			tmp = qb;			
		}
		tmp.setYearInfo(year);
		tmp.setTerm(qb.getTerm());
		tmp.setFaceStartTime(qb.getFaceStartTime());
		tmp.setFaceEndTime(qb.getFaceEndTime());
		tmp.setNetStartTime(qb.getNetStartTime());
		tmp.setNetEndTime(qb.getNetEndTime());
		tmp.setIsPublish(qb.getIsPublish());
		tmp.setUpdatDate(new Date());
		try {
			this.saveOrUpdate(tmp);
			return true;
		} catch (Exception e) {
			logger.error("执行方法 saveQuestionnaireBatch() :保存 QuestionnaireBatch 实体时出错");
		}
		return false;
	}
	
	@Override
	public boolean deleteQuestionnaireBatch(String resourceids) throws ServiceException{
		List<QuestionnaireBatch> list = new ArrayList<QuestionnaireBatch>();
		for(String resourceid : resourceids.split("\\,")){
			QuestionnaireBatch qb = this.get(resourceid);
			
			if(qb!=null){
				if("Y".equalsIgnoreCase(qb.getIsPublish())){
					//如果已经发布了，就不允许删除了					
					return false;					
				}else{
					qb.setIsDeleted(1);
					qb.setUpdatDate(new Date());
					list.add(qb);
				}
				
			}
		}
		if(list.size()>0){
			this.batchSaveOrUpdate(list);
			return true;
		}else{
			return false;
		}	
		
	}
	
	@Override
	public boolean publishQuestionnaireBatch(String resourceids,String brSchoolid) throws ServiceException, Exception{
		List<QuestionnaireBatch> list = new ArrayList<QuestionnaireBatch>();
		List<Questionnaire> qlist =  new ArrayList<Questionnaire>();
		for(String resourceid : resourceids.split("\\,")){
			Map<String,Object> param = new HashMap<String, Object>();
			QuestionnaireBatch qb = this.get(resourceid);
			if(qb!=null){
				qb.setIsPublish(Constants.BOOLEAN_YES);
				qb.setUpdatDate(new Date());
				list.add(qb);
				//按照年度、学期，找出所有已开课、并且已设置任课老师的班级课程，给这些班级课程的学生生成 问卷 edu_evaluate_questionnaire
				//List<CourseTeacherCl> cclist = new ArrayList<CourseTeacherCl>();
				param.put("firstYear", qb.getYearInfo().getFirstYear().toString());
				param.put("term", qb.getTerm());
				param.put("teachType", "faceTeach");
				//如果教学点id不为空，则以教学点为单位进行发布
				if(ExStringUtils.isNotBlank(brSchoolid)){
					param.put("brSchoolid", brSchoolid);
				}
				List<CourseTeacherCl> cclist = courseStatusClService.findCourseTeacherCl_new(param);
				if(cclist!=null && cclist.size()>0){
					for(CourseTeacherCl cl:cclist){
						createQuestionnaire(qlist, param, qb, cl);
					}
				}
				
			}
		}
		if(list.size()>0){
			this.batchSaveOrUpdate(list);
			iQuestionnaireService.batchSaveOrUpdate(qlist);
			return true;
		}else{
			return false;
		}	
		
	}
	/**
	 * @param qlist
	 * @param resourceid
	 * @param param
	 * @param qb
	 * @param cl
	 * @throws ServerException
	 * @throws ServiceException
	 */
	@Override
	public void createQuestionnaire(List<Questionnaire> qlist,
			 Map<String, Object> param,
			QuestionnaireBatch qb, CourseTeacherCl cl) throws ServiceException, ServerException {
		
		//如果已经存在一张问卷，就不再生成了
		Map<String,Object> values = new HashMap<String, Object>();
		values.put("teacherid", cl.getLecturer());
		values.put("batchid", qb.getResourceid());
		values.put("classesid", cl.getClassesId().getResourceid());
		values.put("courseid", cl.getCourseid().getResourceid());
		//查找问卷
		String[] lecturerids = cl.getLecturer().split("\\,");
		for(String lecturerid:lecturerids){
			Questionnaire qn = new Questionnaire();
			qn = createOrUpdate(param, qb, cl, qn, values,lecturerid);
			qlist.add(qn);
		}
		
	}
	@Override
	public void updateQuestionnaire(List<Questionnaire> qlist,
			 Map<String, Object> param,
			QuestionnaireBatch qb, CourseTeacherCl cl) throws ServiceException, ServerException {
		
		//如果已经存在一张问卷，就不再生成了
		Map<String,Object> values = new HashMap<String, Object>();
//		values.put("teacherid", cl.getLecturer());
		values.put("batchid", qb.getResourceid());
		values.put("classesid", cl.getClassesId().getResourceid());
		values.put("courseid", cl.getCourseid().getResourceid());
		//查找问卷
		String[] lecturerids = cl.getLecturer().split("\\,");
		for(String lecturerid:lecturerids){
			Questionnaire qn = new Questionnaire();
			qn = createOrUpdate(param, qb, cl, qn, values,lecturerid);
			qlist.add(qn);
		}
		
	}

	/**
	 * @param param
	 * @param qb
	 * @param cl
	 * @param qn
	 * @param values
	 * @return
	 * @throws ServerException
	 * @throws ServiceException
	 */
	private Questionnaire createOrUpdate(Map<String, Object> param,
			QuestionnaireBatch qb, CourseTeacherCl cl, Questionnaire qn,
			Map<String, Object> values,String lecturerid) throws ServerException,
			ServiceException {
		List<Questionnaire> tmplist = iQuestionnaireService.findQuestionnaire(values);
		if(tmplist.size()==0||tmplist==null){//问卷不存在，则生成
			qn.setClasses(cl.getClassesId());
			qn.setCourse(cl.getCourseid());
			qn.setTeacher(iUserService.get(lecturerid));
			qn.setQuestionnaireBatch(qb);
			if("faceTeach".equals(param.get("teachType"))){
				qn.setCourseType("0");
				qn.setStartTime(qb.getFaceStartTime());
				qn.setEndTime(qb.getFaceEndTime());
				qn.setIsPublish("Y");
			}else{				
				qn.setCourseType("1");
				qn.setIsPublish("N");//当前网络课程默认为不发布
				qn.setStartTime(qb.getNetStartTime());
				qn.setEndTime(qb.getNetEndTime());
			}							
			qn.setUpdatDate(new Date());
				
		}else{//问卷已经存在
			qn = tmplist.get(0);
			qn.setTeacher(iUserService.get(lecturerid));
			if(cl.getCourseStatusId().getTeachType().equals(param.get("teachType"))){
				qn.setCourseType("0");
				qn.setIsPublish("Y");
			}else{				
				qn.setCourseType("1");
				qn.setIsPublish("N");
			}							
			qn.setUpdatDate(new Date());
		}
		return qn;
	}
	
}
