package com.hnjk.edu.teaching.service.impl;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.BaseSupportJdbcDao;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.teaching.helper.ComparatorExamSettingDetails;
import com.hnjk.edu.teaching.model.ExamInfo;
import com.hnjk.edu.teaching.model.ExamResults;
import com.hnjk.edu.teaching.model.ExamSetting;
import com.hnjk.edu.teaching.model.ExamSettingDetails;
import com.hnjk.edu.teaching.model.ExamSub;
import com.hnjk.edu.teaching.service.IExamResultsService;
import com.hnjk.edu.teaching.service.IExamSettingService;
import com.hnjk.edu.teaching.service.IExamSubService;
import com.hnjk.extend.plugin.excel.util.DateUtils;

import edu.emory.mathcs.backport.java.util.Arrays;

/**
 * 
 * <code>考试/毕业论文批次预约Service实现</code>
 * <p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-8-23 上午10:05:52
 * @see
 * @version 1.0
 */
@Service("examSubService")
@Transactional
public class ExamSubServiceImpl extends BaseServiceImpl<ExamSub> implements
		IExamSubService {

	@Autowired
	@Qualifier("examSettingService")
	private IExamSettingService examSettingService;
	
	@Autowired
	@Qualifier("examResultsService")
	private IExamResultsService examResultsService;
	
	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private BaseSupportJdbcDao baseSupportJdbcDao;
	
	@Override
	@Transactional(readOnly = true)
	public Page findExamSubByCondition(Map<String, Object> condition,Page objPage) {
		
		Map<String, Object> values = new HashMap<String, Object>();
		String hql = " from " + ExamSub.class.getSimpleName() + " sub where 1=1 ";
		hql += " and isDeleted = :isDeleted ";
		values.put("isDeleted", 0);

		if (condition.containsKey("yearId")) {
			hql += " and sub.yearInfo.resourceid =:yearId ";
			values.put("yearId", condition.get("yearId"));
		}
		if (condition.containsKey("examType")) {
			hql += " and sub.examType =:examType ";
			values.put("examType", condition.get("examType"));
		}
		if (condition.containsKey("term")) {
			hql += " and sub.term like :term ";
			values.put("term", condition.get("term") + "%");
		}
		if (condition.containsKey("brshSchool")) {
			hql += " and sub.brSchool.resourceid=:brshSchool ";
			values.put("brshSchool", condition.get("brshSchool"));
		}
		if (condition.containsKey("batchType")) {
			hql += " and sub.batchType = :batchType ";
			values.put("batchType", condition.get("batchType"));
		}else {
			hql += " and sub.batchType = :batchType ";
			values.put("batchType", "exam");
		}
		if (condition.containsKey("resourceid")) {
			hql += " and sub.resourceid in ("+condition.get("resourceid")+") ";
		}
		if (condition.containsKey("startTime")) {
			hql += " and sub.startTime >= to_date('"+condition.get("startTime")+"','yyyy-MM-dd HH24:mi:ss')";
		}
		if (condition.containsKey("endTime")) {
			hql += " and sub.endTime <= to_date('"+condition.get("endTime")+"','yyyy-MM-dd HH24:mi:ss')";
		}
		if (condition.containsKey("status")) {
			hql += " and sub.examsubStatus = "+condition.get("status");
		}
		if (condition.containsKey("branchSchool")) {
			values.put("branchSchool",condition.get("branchSchool"));
			hql += " and exists ( select rs.resourceid from "+ExamResults.class.getSimpleName()+" rs where rs.examsubId=sub.resourceid and rs.studentInfo.branchSchool.resourceid=:branchSchool and rs.isDeleted=:isDeleted )";
		}
		hql += " order by " + objPage.getOrderBy() + " " + objPage.getOrder();
		return exGeneralHibernateDao.findByHql(objPage, hql, values);
	}

	
	
	/* (non-Javadoc)
	 * @see com.hnjk.core.support.base.service.BaseServiceImpl#delete(java.io.Serializable)
	 */
	@Override
	public void delete(Serializable id) throws ServiceException {
		ExamSub examSub = get(id);
		if(!"1".equals(examSub.getExamsubStatus())){//如果为发布或关闭状态，则不能删除
			throw new ServiceException("批次'"+examSub.getBatchName()+"'已发布或关闭，不能删除！");
		}
		super.delete(examSub);
	}



	@Override
	public void batchCascadeDelete(String[] ids)  throws ServiceException{
		
		if (ids != null && ids.length > 0) {
			for (String id : ids) {
				boolean isLinked = this.isLinkedByExamResults(id);
				if (isLinked) {
					throw new ServiceException("不允许删除，所选批次已有预约考试记录!");
				}else{
					delete(id);		
				}
						
			}
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public void batchClose(String[] ids) {
		if (ids != null && ids.length > 0) {
			List<ExamSub> ls = (List<ExamSub>) exGeneralHibernateDao
					.findEntitysByIds(ExamSub.class, Arrays.asList(ids));
			for (ExamSub e : ls) {
				e.setExamsubStatus("3");				
			}
		}
	}

	@Override
	public void relationChildMethod(ExamSub examSub, HttpServletRequest request)throws ParseException {
		
		String[] dates 		    = request.getParameterValues("date");
		String[] examSettings   = request.getParameterValues("examSetting");
		DateFormat fmtTime      = new SimpleDateFormat("HH:mm:ss");
		DateFormat fmtTimeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<ExamSetting> list  = new ArrayList<ExamSetting>();
		
		
		String startTime 		= null;
		String endTime 			= null;
		Date startDate 			= null;
		Date endDate 			= null;

		if (examSettings != null && examSettings.length > 0) { 
			for (int i = 0; i < examSettings.length; i++) {
				ExamSetting es  = examSettingService.get(examSettings[i]);
				es.setExamDate(ExDateUtils.parseDate(dates[i], ExDateUtils.PATTREN_DATE));
				es.setExamDateStr(dates[i]);
				list.add(es);
			}
	
			for (int i = 0;i < list.size();i++) {
				
				ExamSetting e             = list.get(i);
				StringBuilder sbStartTime = new StringBuilder();
				StringBuilder sbEndTime   = new StringBuilder();

				startTime = fmtTime.format(e.getStartTime());
				sbStartTime.append(e.getExamDateStr()).append(" ").append(startTime);
				startDate = fmtTimeStamp.parse(sbStartTime.toString());

				endTime   = fmtTime.format(e.getEndTime());
				sbEndTime.append(e.getExamDateStr()).append(" ").append(endTime);
				endDate   = fmtTimeStamp.parse(sbEndTime.toString());
				
				Set<ExamSettingDetails> set     = e.getDetails();
				List<ExamSettingDetails> details= new ArrayList(set);
				Collections.sort(details,new ComparatorExamSettingDetails());
				
				for (int j=0;j<details.size();j++) {
					ExamSettingDetails ed = details.get(j);

					ExamInfo examInfo = generateExamInfo(examSub, ed);
					examInfo.setCourse(ed.getCourseId());
					examInfo.setExamSub(examSub);
					examInfo.setExamStartTime(startDate);				
					examInfo.setExamEndTime(endDate);		
					examInfo.setExamCourseType(0);
					if(ExStringUtils.isBlank(examInfo.getCourseScoreType())) {
						examInfo.setCourseScoreType(examSub.getCourseScoreType());
					}
					if(null==examInfo.getStudyScorePer()) {
						examInfo.setStudyScorePer(examSub.getWrittenScorePer());
					}
	
					examSub.getExamInfo().add(examInfo);
				}

			}
			this.update(examSub);
		}
	}

	@Transactional(readOnly = true)
	public ExamInfo generateExamInfo(ExamSub examSub, ExamSettingDetails esd) {
		ExamInfo ei = null;
		ei = exGeneralHibernateDao.findUnique("from ExamInfo ei where ei.course = ? and ei.examSub = ? and ei.isDeleted = 0 and ei.examCourseType = 0",esd.getCourseId(), examSub);
		if (ei == null) {
			ei = new ExamInfo();
		}
		return ei;
	}

	/**
	 * 根据条件查询 考试/毕业论文批次预约表
	 */
	@Override
	@Transactional(readOnly = true)
	public List<ExamSub> findByCondition(Map<String, Object> paramMap)
			throws ServiceException {
		StringBuffer hql = new StringBuffer();
		Map<String,Object> condition = new HashMap<String, Object>();
		hql.append("from ExamSub examsub where examsub.isDeleted =0");
		if (paramMap != null) {
			if (paramMap.containsKey("year")) {
				hql.append(" and examsub.yearInfo.firstYear =:year");
				condition.put("year",paramMap.get("year"));
			}
			if(paramMap.containsKey("yearId")) {
				hql.append(" and examsub.yearInfo.resourceid =:yearId");
				condition.put("yearId",paramMap.get("yearId"));
			}
			if (paramMap.containsKey("batchName")) {
				hql.append(" and examsub.batchName =:batchName");
				condition.put("batchName",paramMap.get("batchName"));
			}
			if (paramMap.containsKey("term")) {
				hql.append(" and examsub.term =:term");
				condition.put("term",paramMap.get("term"));
			}
			if (paramMap.containsKey("examType")) {
				hql.append(" and examsub.examType =:examType");
				condition.put("examType",paramMap.get("examType"));
			}
			if (paramMap.containsKey("examsubStatus")) {
				hql.append(" and examsub.examsubStatus =:examsubStatus");
				condition.put("examsubStatus",paramMap.get("examsubStatus"));
			}
			if (paramMap.containsKey("batchType")) {
				hql.append(" and examsub.batchType =:batchType");
				condition.put("batchType",paramMap.get("batchType"));
			}
			if (paramMap.containsKey("yearInfoId")) {
				hql.append(" and examsub.yearInfo.resourceid =:yearInfoId");
				condition.put("yearInfoId",paramMap.get("yearInfoId"));
			}
		}
		hql.append(" order by examsub.yearInfo.firstYear");
		List<ExamSub> list = findByHql(hql.toString(),condition);
		return list;
	}

	/**
	 * 查询当前年度的 考试/毕业论文批次预约 对象
	 */
	@Override
	@Transactional(readOnly = true)
	public List<ExamSub> findExamSubByYear() throws ServiceException {
		Date curDate = new Date();
		String orderDate = DateUtils.getFormatDate(curDate, "yyyy-MM-dd");
		int month = Integer.parseInt(DateUtils.getFormatDate(curDate, "MM"));

		StringBuffer hql = new StringBuffer();
		hql.append("from ExamSub examSub where examSub.isDeleted=0");
		hql
				.append(" and to_char(to_date('"
						+ orderDate
						+ "','yyyy-MM-dd'),'yyyy-MM-dd') >= (to_char(setting.yearInfo.firstMondayOffirstTerm,'yyyy-MM-dd'))");
		hql
				.append(" and to_char(to_date('"
						+ orderDate
						+ "','yyyy-MM-dd'),'yyyy-MM-dd') <= (to_char(setting.yearInfo.firstMondayOfSecondTerm+setting.yearInfo.secondTermWeekNum*7,'yyyy-MM-dd'))");

		if (month < 8) {
			hql.append(" and examSub.term='2'");
		} else {
			hql.append(" and examSub.term='1'");
		}

		return (List<ExamSub>) this.exGeneralHibernateDao.findByHql(hql.toString());
	}

	
	@Override
	@Transactional(readOnly = true)
	public boolean isUnique(ExamSub es) throws ServiceException {
		StringBuilder hql = new StringBuilder(
				"from ExamSub where examsubStatus='2' and batchType='exam' and  isDeleted=0");
		if (ExStringUtils.isNotBlank(es.getResourceid())) {
			hql.append("and resourceid <> ").append(
					"'" + es.getResourceid() + "'");
		}
		List<ExamSub> ls = findByHql(hql.toString());
		if(ls!=null&&ls.size()>0) {
			return false;
		}
//		Date startTime = es.getStartTime();
//		Date endTime = es.getEndTime();
//		Date itemStartTime = null;
//		Date itemEndTime = null;
//		if (ls == null || ls.size() == 0)
//			return true;
//		for (ExamSub item : ls) {
//			itemStartTime = item.getStartTime();
//			itemEndTime = item.getEndTime();
//			if ((itemStartTime.before(startTime) && itemEndTime
//					.after(startTime))
//					|| (itemStartTime.before(endTime) && itemEndTime
//							.after(endTime))
//					|| (itemStartTime.after(startTime) && itemEndTime
//							.before(endTime))) {
//				return false;
//			}
//		}
		return true;
	}

	@Override
	@Transactional(readOnly = true)
	public List<ExamSub> findOpenedExamSub(String subType) throws ServiceException {
		String hql  = "from ExamSub examSub where examSub.isDeleted=0 and examSub.examsubStatus='2'";
			   hql += "and examSub.batchType=? ";	
			   hql += "and examSub.isSpecial='N' ";	
		return (List<ExamSub>) this.exGeneralHibernateDao.findByHql(hql,subType);
	}

	@Override
	@Transactional(readOnly = true)
	public Page findGraduateThesisByCondition(Map<String, Object> condition, Page objPage) {		
		List<Criterion> objCriterion = new ArrayList<Criterion>();
		if(condition.containsKey("batchName")) {
			objCriterion.add(Restrictions.ilike("batchName","%"+condition.get("batchName")+"%"));
		}
		if (condition.containsKey("yearId")) {
			objCriterion.add(Restrictions.eq("yearInfo.resourceid",condition.get("yearId")));
		}
		if (condition.containsKey("term")) {
			objCriterion.add(Restrictions.eq("term",condition.get("term")));
		}
		objCriterion.add(Restrictions.eq("isDeleted",0));
		objCriterion.add(Restrictions.eq("batchType","thesis"));	
	
		return exGeneralHibernateDao.findByCriteria(ExamSub.class, objPage, objCriterion.toArray(new Criterion[objCriterion.size()]));
	}

	@Override
	public void changeStates(String resourceid, String states) {
		if(resourceid.indexOf(",")>-1){
			String[] ids = resourceid.split(",");
			for (int i = 0; i < ids.length; i++) {
				ExamSub es = get(ids[i]);
				es.setExamsubStatus(states);
			}
		}else{
			ExamSub es = get(resourceid);
			es.setExamsubStatus(states);
		}
	}

	/**
	 * 检查考试批次是否被成绩关联
	 * @param resourceid
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public boolean isLinkedByExamResults(String resourceid) throws ServiceException {
		
		Map<String,Object> map = new HashMap<String, Object>();
		boolean isLinked       = false;
		String sql 		       = "select count(0) from edu_teach_examresults rs where rs.examsubid = :examSubId and rs.isdeleted =:isDeleted ";
		map.put("isDeleted", 0);
		map.put("examSubId", resourceid);
		try {
			Long counts 	   = baseSupportJdbcDao.getBaseJdbcTemplate().findForLong(sql, map);
			if (counts>0) {
				isLinked = true;
			}
		} catch (Exception e) {
			logger.error(" 检查考试批次是否被成绩关联出错：{}"+e.fillInStackTrace());
			return false;
		}

		return isLinked;
	}
	
	@Override
	public List<Map<String,Object>> getExamSubByGradeId(String gradeId) {
		String sql = "select a.* "
			+ "from EDU_TEACH_EXAMSUB a join EDU_BASE_GRADE b on a.YEARID=b.YEARID "
			+ "where a.isdeleted='0' and b.resourceid=:gradeId";
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("gradeId", gradeId);
		List examSubs = new ArrayList();
		try {
			examSubs = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql, map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return examSubs;
	}
	
	/**
	 *  根据考试类型、年度和学期查询考试批次
	 *  
	 * @param nextExamSub
	 * @param nextYear
	 * @param nextTerm
	 * @return
	 */
	@Override
	public ExamSub findExamSubBycondition(String examType, Long nextYear, String nextTerm) {
		ExamSub examSubNext = null;
		String hqlexamsub = "from "+ExamSub.class.getSimpleName()+" where isDeleted=0 and examType=? "
			+" and yearInfo.firstYear=? and term=?";
		List<ExamSub> examSubList = findByHql(hqlexamsub, examType, nextYear, nextTerm);
		if (examSubList != null && examSubList.size() > 0) {
			examSubNext = examSubList.get(0);
		}
		return examSubNext;
	}
	
}
