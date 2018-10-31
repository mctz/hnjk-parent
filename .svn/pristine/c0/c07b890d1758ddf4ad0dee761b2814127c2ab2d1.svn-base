package com.hnjk.edu.recruit.service.jdbcimpl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.ListOrderedMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.BigDecimalUtil;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.BaseSupportJdbcDao;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.YearInfo;
import com.hnjk.edu.recruit.model.RecruitPlan;
import com.hnjk.edu.recruit.service.IRecruitJDBCService;
import com.hnjk.edu.recruit.service.IRecruitPlanService;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.Dictionary;
import com.hnjk.platform.taglib.JstlCustomFunction;

@Transactional
@SuppressWarnings("unchecked")
@Service("recruitJDBCService")
public class RecruitJDBCServiceImpl extends BaseSupportJdbcDao implements IRecruitJDBCService{
	
	@Autowired
	@Qualifier("recruitPlanService")
	private IRecruitPlanService recruitPlanService;
	
	@Override
	@Transactional(readOnly=true)
	public String getMaxStudentid(String prefix){
		String sql = " select inner.code from (select ei.enrolleeCode as code from EDU_RECRUIT_ENROLLEEINFO  ei where  ei.isDeleted ='0' "
					 + " and ei.enrolleeCode like '"+prefix+"____'"
					 + " order by ei.enrolleeCode desc)inner where rownum=1 ";
		String studentID="";
		try {

			Map map  =	this.baseJdbcTemplate.findForMap(sql,null);
			if(null!=map){
				studentID = map.get("CODE").toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return studentID;
	}
	
	

	@Override
	@Transactional(readOnly=true)
	public Double statisticsStuTotal(String year, String school, String major, String classic, String type) {
		String sql = "";
		StringBuffer criteria = new StringBuffer();
		if(ExStringUtils.isNotEmpty(school)) {
			criteria.append(" and e.branchschoolid='"+school+"' ");
		}
		if(ExStringUtils.isNotEmpty(major)) {
			criteria.append(" and rm.majorid='"+major+"' ");
		}
		if(ExStringUtils.isNotEmpty(classic)) {
			criteria.append(" and rm.classic='"+classic+"' ");
		}
		
		if("1".equals(type)){
			sql = "select count(*) stu_num from EDU_RECRUIT_ENROLLEEINFO e,EDU_RECRUIT_MAJOR rm,EDU_BASE_MAJOR m,EDU_RECRUIT_RECRUITPLAN p,EDU_BASE_GRADE g " +
					"where e.RECRUITMAJORID=rm.resourceid and rm.majorid=m.resourceid and rm.Recruitplanid=p.resourceid and e.isDeleted=0 "+criteria.toString()+
					" and e.grade = g.resourceid  "+
					" and (p.resourceid=:pid or g.yearid=:pid)";
		}else if("2".equals(type)){
			sql = "select count(*) stu_num from EDU_RECRUIT_ENROLLEEINFO e,EDU_RECRUIT_MAJOR rm,EDU_BASE_CLASSIC c,EDU_RECRUIT_RECRUITPLAN p ,EDU_BASE_GRADE g " +
					"where e.RECRUITMAJORID=rm.resourceid and rm.classic=c.resourceid and rm.Recruitplanid=p.resourceid and e.isDeleted=0 "+criteria.toString()+
					" and  e.grade = g.resourceid "+
					" and (p.resourceid =:pid or g.yearid=:pid)";
		}else if("3".equals(type)){
			sql = "select count(*) stu_num from EDU_RECRUIT_ENROLLEEINFO e,EDU_RECRUIT_MAJOR rm,EDU_RECRUIT_RECRUITPLAN p,EDU_BASE_GRADE g " +
					"where e.RECRUITMAJORID=rm.resourceid and rm.Recruitplanid=p.resourceid and e.isDeleted=0 "+criteria.toString()+
					" and  e.grade = g.resourceid "+
					" and (p.resourceid =:pid or g.yearid=:pid)";
		}else if("4".equals(type)){
			sql = "select count(*) stu_num from EDU_RECRUIT_ENROLLEEINFO e,EDU_RECRUIT_MAJOR rm,EDU_BASE_MAJOR m,EDU_RECRUIT_RECRUITPLAN p,EDU_BASE_GRADE g " +
					"where e.RECRUITMAJORID=rm.resourceid and rm.majorid=m.resourceid and rm.Recruitplanid=p.resourceid and e.isDeleted=0 "+criteria.toString()+
					" and  e.grade = g.resourceid "+
					" and (p.resourceid=:pid or g.yearid=:pid) and e.ismatriculate='Y'";
		}else if("5".equals(type)){
			sql = "select count(*) stu_num from EDU_RECRUIT_ENROLLEEINFO e,EDU_RECRUIT_MAJOR rm,EDU_BASE_CLASSIC c,EDU_RECRUIT_RECRUITPLAN  p,EDU_BASE_GRADE g " +
					"where e.RECRUITMAJORID=rm.resourceid and rm.classic=c.resourceid and rm.Recruitplanid=p.resourceid and e.isDeleted=0 "+criteria.toString()+
					" and  e.grade = g.resourceid "+
					" and (p.resourceid =:pid or g.yearid=:pid) and e.ismatriculate='Y'";
		}else if("6".equals(type)){
			sql = "select count(*) stu_num from EDU_RECRUIT_ENROLLEEINFO e,EDU_RECRUIT_MAJOR rm,EDU_RECRUIT_RECRUITPLAN  p,EDU_BASE_GRADE g" +
					" where e.RECRUITMAJORID=rm.resourceid and rm.Recruitplanid=p.resourceid and e.ENROLLEETYPE=0 and e.isDeleted=0 "+criteria.toString()+
					" and  e.grade = g.resourceid "+
					" and (p.resourceid = :pid or g.yearid=:pid)";
		}else if("7".equals(type)){
			sql = "select count(*) stu_num from EDU_RECRUIT_ENROLLEEINFO e,EDU_RECRUIT_MAJOR rm,EDU_RECRUIT_RECRUITPLAN p,EDU_RECRUIT_EXAMSCORE ec,EDU_BASE_GRADE g " +
					"where e.RECRUITMAJORID=rm.resourceid and rm.Recruitplanid=p.resourceid and ec.enrolleeinfoid=e.resourceid and e.isDeleted=0 "+criteria.toString()+
					" and  e.grade = g.resourceid "+
					" and (p.resourceid = :pid or g.yearid=:pid)";
		}else if("8".equals(type)){
			sql = "select count(*) stu_num from EDU_RECRUIT_ENROLLEEINFO e,EDU_BASE_STUDENT bs,EDU_RECRUIT_MAJOR rm,EDU_RECRUIT_RECRUITPLAN p,EDU_BASE_GRADE g " +
					"where e.RECRUITMAJORID = rm.resourceid and e.studentbaseinfoid = bs.resourceid and rm.Recruitplanid = p.resourceid and e.isDeleted = 0 "+criteria.toString()+
					" and  e.grade = g.resourceid "+
					" and (p.resourceid = :pid or g.yearid=:pid)";
		}
		
		Map<String, String> para = new HashMap<String, String>();
		para.put("pid", year);
		Double d = 0.0;
		try {
			List list = this.baseJdbcTemplate.findForListMap(sql, para);
			if(!list.isEmpty()){
				ListOrderedMap map = (ListOrderedMap) list.get(0);
				BigDecimal b = (BigDecimal) map.get("STU_NUM");
				d = b.doubleValue();
			}
			return d;
		} catch (Exception e) {
			e.printStackTrace();
			return d;
		}
	}



	@Override
	@Transactional(readOnly=true)
	public List listStudymodel(String year, String school, String major, String classic) {
		StringBuffer criteria = new StringBuffer();
		if(ExStringUtils.isNotEmpty(school)) {
			criteria.append(" and e.branchschoolid='"+school+"' ");
		}
		if(ExStringUtils.isNotEmpty(major)) {
			criteria.append(" and rm.majorid='"+major+"' ");
		}
		if(ExStringUtils.isNotEmpty(classic)) {
			criteria.append(" and rm.classic='"+classic+"' ");
		}
		
		String sql = "select count(*) stu_num,m.isadult from EDU_RECRUIT_ENROLLEEINFO e,EDU_RECRUIT_MAJOR rm,EDU_BASE_MAJOR m,EDU_RECRUIT_RECRUITPLAN p where e.RECRUITMAJORID=rm.resourceid and rm.majorid=m.resourceid and rm.Recruitplanid=p.resourceid and e.isDeleted=0 "+criteria.toString()+" and p.resourceid=:pid group by m.isadult";
		Map<String, String> para = new HashMap<String, String>();
		List list = new ArrayList();
		para.put("pid", year);
		try {
			if(ExStringUtils.isNotEmpty(year)){
				list = this.baseJdbcTemplate.findForListMap(sql, para);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	@Transactional(readOnly=true)
	public List listMajorType(String year, String school, String major, String classic) {
		StringBuffer criteria = new StringBuffer();
		if(ExStringUtils.isNotEmpty(school)) {
			criteria.append(" and e.branchschoolid='"+school+"' ");
		}
		if(ExStringUtils.isNotEmpty(major)) {
			criteria.append(" and rm.majorid='"+major+"' ");
		}
		if(ExStringUtils.isNotEmpty(classic)) {
			criteria.append(" and rm.classic='"+classic+"' ");
		}
		
		String sql2 = "select count(*) stu_num,m.resourceid,m.majorname from EDU_RECRUIT_ENROLLEEINFO e,EDU_RECRUIT_MAJOR rm,EDU_BASE_MAJOR m,EDU_RECRUIT_RECRUITPLAN p where e.RECRUITMAJORID=rm.resourceid and rm.majorid=m.resourceid and rm.Recruitplanid=p.resourceid and e.isDeleted=0 "+criteria.toString()+" and p.resourceid=:pid group by m.resourceid,m.majorname";
		Map<String, String> para = new HashMap<String, String>();
		para.put("pid", year);
		List list = new ArrayList();
		try {
			if(ExStringUtils.isNotEmpty(year)){
				list = this.baseJdbcTemplate.findForListMap(sql2, para);				
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	@Override
	@Transactional(readOnly=true)
	public String findBranchSchoolPlanIdList(Map<String,Object> paramMap) {
		
		StringBuffer sql = new StringBuffer();
		sql.append("select resourceid from EDU_RECRUIT_BRSCHPLAN plan where plan.RECRUITPLANID=:recruitPlanId ");
		sql.append(" and plan.BRANCHSCHOOLID=:branchSchoolId and plan.ISAUDITED='Y'");
		String ids = "";
		List list = new ArrayList();
		try {
			list = this.baseJdbcTemplate.findForListMap(sql.toString(), paramMap);
		} catch (Exception e) {
			e.printStackTrace();
			return ids;
		}
		if(null!=list&&!list.isEmpty()){
			for (int i = 0; i < list.size(); i++) {
				String idA = list.get(i).toString().replace("{","");
				String idB = idA.replace("}","");
				String [] idArray = idB.split("=");
				ids += ","+"'"+idArray[1]+"'";
			}
		}
		if (!"".equals(ids) &&ids.length()>0) {
			return ids.substring(1).trim();
		}else {
			return ids;
		}
	}


	@Override
	@Transactional(readOnly=true)
	public List listClassicLevel(String year, String school, String major, String classic) {
		StringBuffer criteria = new StringBuffer();
		if(ExStringUtils.isNotEmpty(school)) {
			criteria.append(" and e.branchschoolid='"+school+"' ");
		}
		if(ExStringUtils.isNotEmpty(major)) {
			criteria.append(" and rm.majorid='"+major+"' ");
		}
		if(ExStringUtils.isNotEmpty(classic)) {
			criteria.append(" and rm.classic='"+classic+"' ");
		}
		
		String sql = "select count(*) stu_num,c.resourceid,c.classicname from EDU_RECRUIT_ENROLLEEINFO e,EDU_RECRUIT_MAJOR rm,EDU_BASE_CLASSIC c,EDU_RECRUIT_RECRUITPLAN p where e.RECRUITMAJORID=rm.resourceid and rm.classic=c.resourceid and rm.Recruitplanid=p.resourceid and e.isDeleted=0 "+criteria.toString()+" and p.resourceid=:pid group by c.resourceid,c.classicname";
		Map<String, String> para = new HashMap<String, String>();
		para.put("pid", year);
		List list = new ArrayList();
		try {
			if(ExStringUtils.isNotEmpty(year)){
				list = this.baseJdbcTemplate.findForListMap(sql, para);				
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	@Override
	@Transactional(readOnly=true)
	public List listExamStatistic(String year, String school, String major, String classic) {
		StringBuffer criteria = new StringBuffer();
		if(ExStringUtils.isNotEmpty(school)) {
			criteria.append(" and e.branchschoolid='"+school+"' ");
		}
		if(ExStringUtils.isNotEmpty(major)) {
			criteria.append(" and rm.majorid='"+major+"' ");
		}
		if(ExStringUtils.isNotEmpty(classic)) {
			criteria.append(" and rm.classic='"+classic+"' ");
		}
		
		String sql = "select count(*) stu_num, e.isapplynoexam from EDU_RECRUIT_ENROLLEEINFO e,EDU_RECRUIT_MAJOR rm,EDU_RECRUIT_RECRUITPLAN  p where e.RECRUITMAJORID = rm.resourceid and rm.Recruitplanid = p.resourceid and e.isDeleted = 0 "+criteria.toString()+" and p.resourceid = :pid group by e.isapplynoexam";
		Map<String, String> para = new HashMap<String, String>();
		para.put("pid", year);
		List list = new ArrayList();
		try {
			if(ExStringUtils.isNotEmpty(year)){
				list = this.baseJdbcTemplate.findForListMap(sql, para);				
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	
	@Override
	@Transactional(readOnly=true)
	public List listFromMedia(String year, String school, String major, String classic) {
		StringBuffer criteria = new StringBuffer();
		if(ExStringUtils.isNotEmpty(school)) {
			criteria.append(" and e.branchschoolid='"+school+"' ");
		}
		if(ExStringUtils.isNotEmpty(major)) {
			criteria.append(" and rm.majorid='"+major+"' ");
		}
		if(ExStringUtils.isNotEmpty(classic)) {
			criteria.append(" and rm.classic='"+classic+"' ");
		}
		
		String sql = "select count(*) stu_num, e.frommedia from EDU_RECRUIT_ENROLLEEINFO e,EDU_RECRUIT_MAJOR rm,EDU_RECRUIT_RECRUITPLAN  p where e.RECRUITMAJORID = rm.resourceid and rm.Recruitplanid = p.resourceid and e.isDeleted = 0 "+criteria.toString()+" and p.resourceid = :pid group by e.FROMMEDIA";
		Map<String, String> para = new HashMap<String, String>();
		para.put("pid", year);
		List list = new ArrayList();
		try {
			if(ExStringUtils.isNotEmpty(year)){
				list = this.baseJdbcTemplate.findForListMap(sql, para);				
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	@Override
	@Transactional(readOnly=true)
	public List listApplyType(String year, String school, String major, String classic) {
		StringBuffer criteria = new StringBuffer();
		if(ExStringUtils.isNotEmpty(school)) {
			criteria.append(" and e.branchschoolid='"+school+"' ");
		}
		if(ExStringUtils.isNotEmpty(major)) {
			criteria.append(" and rm.majorid='"+major+"' ");
		}
		if(ExStringUtils.isNotEmpty(classic)) {
			criteria.append(" and rm.classic='"+classic+"' ");
		}
		
		String sql = "select count(*) stu_num, e.enrolleetype from EDU_RECRUIT_ENROLLEEINFO e,EDU_RECRUIT_MAJOR rm,EDU_RECRUIT_RECRUITPLAN  p where e.RECRUITMAJORID = rm.resourceid and rm.Recruitplanid = p.resourceid and e.isDeleted = 0 "+criteria.toString()+" and p.resourceid = :pid group by e.enrolleetype";
		Map<String, String> para = new HashMap<String, String>();
		para.put("pid", year);
		List list = new ArrayList();
		try {
			if(ExStringUtils.isNotEmpty(year)){
				list = this.baseJdbcTemplate.findForListMap(sql, para);				
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	@Override
	@Transactional(readOnly=true)
	public List listEnrollStatus(String year, String school, String major, String classic) {
		StringBuffer criteria = new StringBuffer();
		if(ExStringUtils.isNotEmpty(school)) {
			criteria.append(" and e.branchschoolid='"+school+"' ");
		}
		if(ExStringUtils.isNotEmpty(major)) {
			criteria.append(" and rm.majorid='"+major+"' ");
		}
		if(ExStringUtils.isNotEmpty(classic)) {
			criteria.append(" and rm.classic='"+classic+"' ");
		}
		
		String sql = "select count(*) stu_num, e.Ismatriculate from EDU_RECRUIT_ENROLLEEINFO e,EDU_RECRUIT_MAJOR rm,EDU_RECRUIT_RECRUITPLAN  p,EDU_BASE_GRADE g " +
				" where e.RECRUITMAJORID = rm.resourceid and rm.Recruitplanid = p.resourceid and e.ENROLLEETYPE=0 and e.isDeleted = 0 "+criteria.toString()+
				" and e.grade = g.resourceid "+
				" and (p.resourceid = :pid or g.yearid=:pid) group by e.Ismatriculate";
		Map<String, String> para = new HashMap<String, String>();
		para.put("pid", year);
		List list = new ArrayList();
		try {
			if(ExStringUtils.isNotEmpty(year)){
				list = this.baseJdbcTemplate.findForListMap(sql, para);				
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	@Override
	@Transactional(readOnly=true)
	public List listEnrollStudy(String year,  String school, String major, String classic) {
		StringBuffer criteria = new StringBuffer();
		if(ExStringUtils.isNotEmpty(school)) {
			criteria.append(" and e.branchschoolid='"+school+"' ");
		}
		if(ExStringUtils.isNotEmpty(major)) {
			criteria.append(" and rm.majorid='"+major+"' ");
		}
		if(ExStringUtils.isNotEmpty(classic)) {
			criteria.append(" and rm.classic='"+classic+"' ");
		}
		
		String sql = "select count(*) stu_num,m.isadult from EDU_RECRUIT_ENROLLEEINFO e,EDU_RECRUIT_MAJOR rm,EDU_BASE_MAJOR m,EDU_RECRUIT_RECRUITPLAN p,EDU_BASE_GRADE g " +
				"where e.RECRUITMAJORID=rm.resourceid and rm.majorid=m.resourceid and rm.Recruitplanid=p.resourceid and e.isDeleted=0 "+criteria.toString()+
				" and e.grade = g.resourceid "+
				" and e.ismatriculate='Y' and (p.resourceid=:pid or g.yearid=:pid) group by m.isadult";
		Map<String, String> para = new HashMap<String, String>();
		List list = new ArrayList();
		para.put("pid", year);
		try {
			if(ExStringUtils.isNotEmpty(year)){
				list = this.baseJdbcTemplate.findForListMap(sql, para);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	@Override
	@Transactional(readOnly=true)
	public List listEnrollMajor(String year,  String school, String major, String classic) {
		StringBuffer criteria = new StringBuffer();
		if(ExStringUtils.isNotEmpty(school)) {
			criteria.append(" and e.branchschoolid='"+school+"' ");
		}
		if(ExStringUtils.isNotEmpty(major)) {
			criteria.append(" and rm.majorid='"+major+"' ");
		}
		if(ExStringUtils.isNotEmpty(classic)) {
			criteria.append(" and rm.classic='"+classic+"' ");
		}
		
		String sql2 = "select count(*) stu_num,m.resourceid,m.majorname from EDU_RECRUIT_ENROLLEEINFO e,EDU_RECRUIT_MAJOR rm,EDU_BASE_MAJOR m,EDU_RECRUIT_RECRUITPLAN p,EDU_BASE_GRADE g " +
				"where e.RECRUITMAJORID=rm.resourceid and rm.majorid=m.resourceid and rm.Recruitplanid=p.resourceid and e.isDeleted=0 "+criteria.toString()+
				" and e.grade = g.resourceid "+
				" and e.ismatriculate='Y' and (p.resourceid=:pid or g.yearid=:pid) group by m.resourceid,m.majorname";
		Map<String, String> para = new HashMap<String, String>();
		para.put("pid", year);
		List list = new ArrayList();
		try {
			if(ExStringUtils.isNotEmpty(year)){
				list = this.baseJdbcTemplate.findForListMap(sql2, para);				
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	@Override
	@Transactional(readOnly=true)
	public List listEnrollClassic(String year,  String school, String major, String classic) {
		StringBuffer criteria = new StringBuffer();
		if(ExStringUtils.isNotEmpty(school)) {
			criteria.append(" and e.branchschoolid='"+school+"' ");
		}
		if(ExStringUtils.isNotEmpty(major)) {
			criteria.append(" and rm.majorid='"+major+"' ");
		}
		if(ExStringUtils.isNotEmpty(classic)) {
			criteria.append(" and rm.classic='"+classic+"' ");
		}
		
		String sql = "select count(*) stu_num,c.resourceid,c.classicname from EDU_RECRUIT_ENROLLEEINFO e,EDU_RECRUIT_MAJOR rm,EDU_BASE_CLASSIC c,EDU_RECRUIT_RECRUITPLAN p,EDU_BASE_GRADE g " +
				"where e.RECRUITMAJORID=rm.resourceid and rm.classic=c.resourceid and rm.Recruitplanid=p.resourceid and e.isDeleted=0 "+criteria.toString()+
				" and e.grade = g.resourceid "+
				" and e.ismatriculate='Y' and (p.resourceid=:pid or g.yearid=:pid) group by c.resourceid,c.classicname";
		Map<String, String> para = new HashMap<String, String>();
		para.put("pid", year);
		List list = new ArrayList();
		try {
			if(ExStringUtils.isNotEmpty(year)){
				list = this.baseJdbcTemplate.findForListMap(sql, para);				
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	
	


	@Override
	@Transactional(readOnly=true)
	public List listEnrollSchool(String year, String school, String major, String classic) {
		StringBuffer criteria = new StringBuffer();
		if(ExStringUtils.isNotEmpty(school)) {
			criteria.append(" and e.branchschoolid='"+school+"' ");
		}
		if(ExStringUtils.isNotEmpty(major)) {
			criteria.append(" and rm.majorid='"+major+"' ");
		}
		if(ExStringUtils.isNotEmpty(classic)) {
			criteria.append(" and rm.classic='"+classic+"' ");
		}
		
		String sql = "select count(*) stu_num, nvl(ec.originalpoint,-9) originalpoint from EDU_RECRUIT_ENROLLEEINFO e,EDU_RECRUIT_MAJOR rm,EDU_RECRUIT_RECRUITPLAN p,EDU_RECRUIT_EXAMSCORE ec where e.RECRUITMAJORID = rm.resourceid and rm.Recruitplanid = p.resourceid and ec.enrolleeinfoid=e.resourceid and e.isDeleted = 0 "+criteria.toString()+" and p.resourceid=:pid  group by ec.originalpoint order by ec.originalpoint";
		Map<String, String> para = new HashMap<String, String>();
		para.put("pid", year);
		List list = new ArrayList();
		try {
			if(ExStringUtils.isNotEmpty(year)){
				list = this.baseJdbcTemplate.findForListMap(sql, para);				
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}



	@Override
	@Transactional(readOnly=true)
	public Double statisStudentBySql(String year, String sql) {
		Map<String, String> para = new HashMap<String, String>();
		para.put("pid", year);
		Double d = 0.0;
		try {
			List list = this.baseJdbcTemplate.findForListMap(sql, para);
			if(!list.isEmpty()){
				ListOrderedMap map = (ListOrderedMap) list.get(0);
				BigDecimal b = (BigDecimal) map.get("STU_NUM");
				d = b.doubleValue();
			}
			return d;
		} catch (Exception e) {
			e.printStackTrace();
			return d;
		}
	}


	@Override
	@Transactional(readOnly=true)
	public List listEnrollSex(String year, String school, String major, String classic) {
		StringBuffer criteria = new StringBuffer();
		if(ExStringUtils.isNotEmpty(school)) {
			criteria.append(" and e.branchschoolid='"+school+"' ");
		}
		if(ExStringUtils.isNotEmpty(major)) {
			criteria.append(" and rm.majorid='"+major+"' ");
		}
		if(ExStringUtils.isNotEmpty(classic)) {
			criteria.append(" and rm.classic='"+classic+"' ");
		}
		
		String sql = "select bs.gender,count(*) stu_num from EDU_RECRUIT_ENROLLEEINFO e,EDU_BASE_STUDENT bs,EDU_RECRUIT_MAJOR rm,EDU_RECRUIT_RECRUITPLAN p where e.RECRUITMAJORID=rm.resourceid and e.studentbaseinfoid=bs.resourceid and rm.Recruitplanid=p.resourceid and e.isDeleted=0 "+criteria.toString()+" and p.resourceid=:pid group by bs.gender";
		Map<String, String> para = new HashMap<String, String>();
		List list = new ArrayList();
		para.put("pid", year);
		try {
			if(ExStringUtils.isNotEmpty(year)){
				list = this.baseJdbcTemplate.findForListMap(sql, para);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}



	@Override
	@Transactional(readOnly=true)
	public List listEnrollPlace(String year, String school, String major, String classic) {
		StringBuffer criteria = new StringBuffer();
		if(ExStringUtils.isNotEmpty(school)) {
			criteria.append(" and e.branchschoolid='"+school+"' ");
		}
		if(ExStringUtils.isNotEmpty(major)) {
			criteria.append(" and rm.majorid='"+major+"' ");
		}
		if(ExStringUtils.isNotEmpty(classic)) {
			criteria.append(" and rm.classic='"+classic+"' ");
		}
		
		String sql = "select bs.homeplace,count(*) stu_num from EDU_RECRUIT_ENROLLEEINFO e,EDU_BASE_STUDENT bs,EDU_RECRUIT_MAJOR rm,EDU_RECRUIT_RECRUITPLAN p where e.RECRUITMAJORID=rm.resourceid and e.studentbaseinfoid=bs.resourceid and rm.Recruitplanid=p.resourceid and e.isDeleted=0 "+criteria.toString()+" and p.resourceid=:pid group by bs.homeplace order by 2 desc";
		Map<String, String> para = new HashMap<String, String>();
		List list = new ArrayList();
		para.put("pid", year);
		try {
			if(ExStringUtils.isNotEmpty(year)){
				list = this.baseJdbcTemplate.findForListMap(sql, para);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	@Override
	@Transactional(readOnly=true)
	public List listEnrollResidence(String year, String school, String major, String classic) {
		StringBuffer criteria = new StringBuffer();
		if(ExStringUtils.isNotEmpty(school)) {
			criteria.append(" and e.branchschoolid='"+school+"' ");
		}
		if(ExStringUtils.isNotEmpty(major)) {
			criteria.append(" and rm.majorid='"+major+"' ");
		}
		if(ExStringUtils.isNotEmpty(classic)) {
			criteria.append(" and rm.classic='"+classic+"' ");
		}
		
		String sql = "select bs.residence,count(*) stu_num from EDU_RECRUIT_ENROLLEEINFO e,EDU_BASE_STUDENT bs,EDU_RECRUIT_MAJOR rm,EDU_RECRUIT_RECRUITPLAN p where e.RECRUITMAJORID=rm.resourceid and e.studentbaseinfoid=bs.resourceid and rm.Recruitplanid=p.resourceid and e.isDeleted=0 "+criteria.toString()+" and p.resourceid=:pid group by bs.residence order by 2 desc";
		Map<String, String> para = new HashMap<String, String>();
		List list = new ArrayList();
		para.put("pid", year);
		try {
			if(ExStringUtils.isNotEmpty(year)){
				list = this.baseJdbcTemplate.findForListMap(sql, para);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	@Override
	@Transactional(readOnly=true)
	public List listEnrollNation(String year, String school, String major, String classic) {
		StringBuffer criteria = new StringBuffer();
		if(ExStringUtils.isNotEmpty(school)) {
			criteria.append(" and e.branchschoolid='"+school+"' ");
		}
		if(ExStringUtils.isNotEmpty(major)) {
			criteria.append(" and rm.majorid='"+major+"' ");
		}
		if(ExStringUtils.isNotEmpty(classic)) {
			criteria.append(" and rm.classic='"+classic+"' ");
		}
		
		String sql = "select bs.nation,count(*) stu_num from EDU_RECRUIT_ENROLLEEINFO e,EDU_BASE_STUDENT bs,EDU_RECRUIT_MAJOR rm,EDU_RECRUIT_RECRUITPLAN p where e.RECRUITMAJORID=rm.resourceid and e.studentbaseinfoid=bs.resourceid and rm.Recruitplanid=p.resourceid and e.isDeleted=0 "+criteria.toString()+" and p.resourceid=:pid group by bs.nation order by 2 desc";
		Map<String, String> para = new HashMap<String, String>();
		List list = new ArrayList();
		para.put("pid", year);
		try {
			if(ExStringUtils.isNotEmpty(year)){
				list = this.baseJdbcTemplate.findForListMap(sql, para);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}



	@Override
	@Transactional(readOnly=true)
	public List listEnrollMarry(String year, String school, String major, String classic) {
		StringBuffer criteria = new StringBuffer();
		if(ExStringUtils.isNotEmpty(school)) {
			criteria.append(" and e.branchschoolid='"+school+"' ");
		}
		if(ExStringUtils.isNotEmpty(major)) {
			criteria.append(" and rm.majorid='"+major+"' ");
		}
		if(ExStringUtils.isNotEmpty(classic)) {
			criteria.append(" and rm.classic='"+classic+"' ");
		}
		
		String sql = "select bs.marriage,count(*) stu_num from EDU_RECRUIT_ENROLLEEINFO e,EDU_BASE_STUDENT bs,EDU_RECRUIT_MAJOR rm,EDU_RECRUIT_RECRUITPLAN p where e.RECRUITMAJORID=rm.resourceid and e.studentbaseinfoid=bs.resourceid and rm.Recruitplanid=p.resourceid and e.isDeleted=0 "+criteria.toString()+" and p.resourceid=:pid group by bs.marriage order by 2 desc";
		Map<String, String> para = new HashMap<String, String>();
		List list = new ArrayList();
		para.put("pid", year);
		try {
			if(ExStringUtils.isNotEmpty(year)){
				list = this.baseJdbcTemplate.findForListMap(sql, para);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}



	@Override
	@Transactional(readOnly=true)
	public List listEnrollPolitics(String year, String school, String major, String classic) {
		StringBuffer criteria = new StringBuffer();
		if(ExStringUtils.isNotEmpty(school)) {
			criteria.append(" and e.branchschoolid='"+school+"' ");
		}
		if(ExStringUtils.isNotEmpty(major)) {
			criteria.append(" and rm.majorid='"+major+"' ");
		}
		if(ExStringUtils.isNotEmpty(classic)) {
			criteria.append(" and rm.classic='"+classic+"' ");
		}
		
		String sql = "select bs.politics,count(*) stu_num from EDU_RECRUIT_ENROLLEEINFO e,EDU_BASE_STUDENT bs,EDU_RECRUIT_MAJOR rm,EDU_RECRUIT_RECRUITPLAN p where e.RECRUITMAJORID=rm.resourceid and e.studentbaseinfoid=bs.resourceid and rm.Recruitplanid=p.resourceid and e.isDeleted=0 "+criteria.toString()+" and p.resourceid=:pid group by bs.politics order by 2 desc";
		Map<String, String> para = new HashMap<String, String>();
		List list = new ArrayList();
		para.put("pid", year);
		try {
			if(ExStringUtils.isNotEmpty(year)){
				list = this.baseJdbcTemplate.findForListMap(sql, para);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}



	
	/**
	 * 招生综合统计
	 */
	@Override
	public Map<String,Object> listEnrollStatisticComplex(Map<String, Object> condition)throws ServiceException {
	
		Map<String,Object> resultMap = new HashMap<String, Object>();//返回结果MAP
		RecruitPlan plan             = null;						 //招生批次对象
		List<Dictionary> dict        = new ArrayList<Dictionary>();  //字典集合
		StringBuffer resultStr 	     = new StringBuffer();			 //报表数据 (Jquery Highcharts.Chart数据形式)
		Map<String, String> dictMap  = new HashMap<String, String>();//字典MAP
		List list 			   		 = new ArrayList();				 //根据条件查询出的数据集合
		String statisticType   		 = condition.get("statisticType").toString();//选择的统计方向(必选参数)
		String recruitPlan     	     = ""; 									     //招生批次ID
		String reportName     		 = JstlCustomFunction.dictionaryCode2Value("CodeStatisticType",statisticType);//根据选择的统计方向获取当次报表的名称
		String reportTitle 		     = "";
		Map<String, String> prefixMap= genStatisticPrefixSQL(statisticType);     //根据选择的统计方向获取统计SQL的前半部分，并把当前的统计列、前端明细列表的表头以
																				 //Key=statisticColumn、Key=tdStr的形式放入返回的MAP中
		String statisticColumn       = prefixMap.get("statisticColumn");         //前端明细列表的统计列名称
		Double total 				 = genStatisticTotalSQL(statisticType, condition);//当前条件下的数据总数
		String querySQL              = prefixMap.get("refixSQL")+genStatisticSuffixSQL(statisticType, condition);
		
		if ("4".equals(statisticType)) {
			dict = CacheAppManager.getChildren("CodeFromMedia");   //从缓存中获取媒体信息来源字典对象
		}
		if ("5".equals(statisticType)) {
			dict = CacheAppManager.getChildren("CodeEnrolleeType");//从缓存中获取报名方式字典对象
		}
		if ("8".equals(statisticType)) {
			dict = CacheAppManager.getChildren("CodePolitics");    //从缓存中获取政治面貌字典对象
		}
		if ("9".equals(statisticType)) {
			dict = CacheAppManager.getChildren("CodeNation");      //从缓存中获取民族字典对象
		}
		if ("10".equals(statisticType)) {
			dict = CacheAppManager.getChildren("CodeMarriage");    //从缓存中获取婚否字典对象
		}
        if ("11".equals(statisticType)) {
			dict = CacheAppManager.getChildren("CodeSex");         //从缓存中获取性别字典对象
		}
       
        if(condition.containsKey("recruitPlan")) {
			recruitPlan = condition.get("recruitPlan").toString();
		}
       
        if (ExStringUtils.isNotEmpty(recruitPlan))  {
        	plan 	    = recruitPlanService.get(recruitPlan);
        	if(null == plan){
        		YearInfo year = (YearInfo)recruitPlanService.get(YearInfo.class, recruitPlan);
        		if(null != year) {
					reportName = year.getYearName()+"-"+reportName;
				}
        	}
        }
        
        if (null!=plan) {
        	reportTitle = plan.getRecruitPlanname() +"-"+ reportName;
		}else {
			reportTitle = reportName;
		}
        if(dict != null){
			for(Dictionary childDict : dict){
				dictMap.put(childDict.getDictValue(), childDict.getDictName());
			}
		} 
        try {
			
        	list = this.baseJdbcTemplate.findForListMap(querySQL, condition);
			if (null!= list && !list.isEmpty()) {
				String chatType = "pie";
				if("1".equals(statisticType) || "6".equals(statisticType) || "7".equals(statisticType)) {
					chatType = "bar";
				}
				resultStr.append("{type: '"+chatType+"',name: '"+reportName+"',data: [");
				for(int index=0;index<list.size();index++){
					Map obj			  = (Map) list.get(index);
					
					double percentage = BigDecimalUtil.mul(BigDecimalUtil.div(Double.parseDouble(obj.get("stu_num").toString()), total, 4),100);				
					if ("0".equals(statisticType)&&Constants.BOOLEAN_YES.equalsIgnoreCase(obj.get(statisticColumn).toString())){
						
						resultStr.append("['网络成人直属班"+obj.get("stu_num").toString()+"',"+percentage+"],");	//学习模式-网络成人直属班
						obj.put(statisticColumn, "网络成人直属班");
					
					}else if("0".equals(statisticType)&&Constants.BOOLEAN_NO.equalsIgnoreCase(obj.get(statisticColumn).toString())){ //学习模式-网络教育
						
						resultStr.append("['网络教育"+obj.get("stu_num").toString()+"',"+percentage+"],");
						obj.put(statisticColumn, "网络教育");
						
					}else if("3".equals(statisticType)&&Constants.BOOLEAN_YES.equalsIgnoreCase(obj.get(statisticColumn).toString())){ //考试类别-免试
							
						resultStr.append("['免试"+obj.get("stu_num").toString()+"',"+percentage+"],");	
						obj.put(statisticColumn, "免试");
							
					}else if("3".equals(statisticType)&&Constants.BOOLEAN_NO.equalsIgnoreCase(obj.get(statisticColumn).toString())){ //考试类别-正常
					
						resultStr.append("['正常"+obj.get("stu_num").toString()+"',"+percentage+"],");
						obj.put(statisticColumn, "正常");		
						
					//4.媒体信息来源    5.报名方式   8.政治面貌   9.民族   10.婚否   11.性别	
					}else if ("4".equals(statisticType) ||  "5".equals(statisticType) ||  "8".equals(statisticType) || 
							  "9".equals(statisticType) || "10".equals(statisticType) || "11".equals(statisticType)){
						
						if (dictMap.containsKey(obj.get(statisticColumn).toString())) {
							
							resultStr.append("['"+dictMap.get(obj.get(statisticColumn).toString()) + obj.get("stu_num").toString()+"',"+percentage+"],");
							obj.put(statisticColumn, dictMap.get(obj.get(statisticColumn).toString()));
							
						}else {
							resultStr.append("['"+obj.get(statisticColumn) + obj.get("stu_num").toString()+"',"+percentage+"],");
						}
						
					
					}else  {
						
						resultStr.append("['"+obj.get(statisticColumn).toString() + obj.get("stu_num").toString()+"',"+percentage+"],");
					}
					
					obj.put("percent", BigDecimalUtil.div(Double.parseDouble(obj.get("stu_num").toString()), total, 4));
					//为了页面美观，超过20个则跳出
					if(index>20){
						break;
					}
				}
				resultStr.append("]}");
				System.out.println(resultStr);
				resultMap.put("resultList", list);
			}
			
		} catch (Exception e) {
			throw new ServiceException("统计招生数据出错:{}"+e.fillInStackTrace());
		}
		resultMap.put("tdStr", prefixMap.get("tdStr"));
		resultMap.put("statisticColumn", prefixMap.get("statisticColumn"));
		resultMap.put("resultStr", resultStr.toString());
		resultMap.put("reportTitle",reportTitle);
		return resultMap;
	}
	/**
	 * 生成总和SQL
	 * @param condition
	 * @return
	 */
	private Double  genStatisticTotalSQL(String statisticType,Map<String, Object> condition){
		StringBuffer sql = new StringBuffer(" select count(*) stu_num ");
		
		sql.append("   from EDU_RECRUIT_ENROLLEEINFO e,EDU_BASE_STUDENT bs,EDU_BASE_CLASSIC c,EDU_RECRUIT_MAJOR rm,EDU_BASE_MAJOR m,EDU_RECRUIT_RECRUITPLAN p,EDU_BASE_GRADE g  ")
		.append("  where e.RECRUITMAJORID    = rm.resourceid  ")
		.append("	and rm.majorid          = m.resourceid  ")
		.append("	and rm.classic			= c.resourceid ")
		.append("	and e.studentbaseinfoid = bs.resourceid  ")
		.append("	and rm.Recruitplanid    = p.resourceid ")
		.append("  and e.grade = g.resourceid ")
		.append("	and e.isDeleted         = 0 ")
		.append(genStatisticParamSQL(condition));

		Double d 	  = 0.0;
		try {
			List list = this.baseJdbcTemplate.findForListMap(sql.toString(), condition);
			if(null  != list&&!list.isEmpty()){
				ListOrderedMap map = (ListOrderedMap) list.get(0);
				BigDecimal b	   = (BigDecimal) map.get("STU_NUM");
				d 				   = b.doubleValue();
			}
			return d;
		} catch (Exception e) {
			e.printStackTrace();
			return d;
		}
	}
	/**
	 * 生成统计SQL的条件部分
	 * @param condition
	 * @return
	 */
	private String genStatisticParamSQL(Map<String, Object> condition){
		
		StringBuffer sql = new StringBuffer() ;
		//招生批次或所在年度
		if (condition.containsKey("recruitPlan") && ExStringUtils.isNotEmpty(condition.get("recruitPlan").toString())) {
			sql.append(" and (p.resourceid=:recruitPlan or g.YEARID = :recruitPlan) ");
		}
		
		if (condition.containsKey("brSchool") ) {
			sql.append(" and e.branchschoolid =:brSchool");
		}
		
		if (condition.containsKey("major")) {
			sql.append(" and m.resourceid=:major");
		}
		
		if (condition.containsKey("classic")) {
			sql.append(" and c.resourceid=:classic");
		}
		
		if (condition.containsKey("sex")) {
			sql.append(" and bs.gender=:sex");
		}
		
		if (condition.containsKey("nation")) {
			sql.append(" and bs.nation=:nation");
		}
		
		if (condition.containsKey("marriage")) {
			sql.append(" and bs.marriage=:marriage");
		}
		
		if (condition.containsKey("politics")) {
			sql.append(" and bs.politics=:politics");
		}
		
		if (condition.containsKey("homePlace_city")){ 
			
			sql.append(" and bs.homeplace='"+condition.get("homePlace_province").toString().trim());
			sql.append(	condition.get("homePlace_city").toString().trim());
			sql.append(	condition.get("homePlace_county").toString().trim()+"'");
		}
		if (condition.containsKey("residence_city")) {
			
			sql.append(" and bs.residence='"+condition.get("residence_province").toString().trim());
			sql.append(	condition.get("residence_city").toString().trim());
			sql.append(	condition.get("residence_county").toString().trim()+"'");
		
		}
		return sql.toString();
		
	}
	/**
	 * 生成统计SQL的前半部分
	 * @param statisticType
	 * @return
	 */
	private Map<String,String > genStatisticPrefixSQL(String statisticType){
		
		StringBuffer sql  		     = new StringBuffer();
		Map<String,String> resultMap = new HashMap<String, String>();
		if ("0".equals(statisticType)) {//学习模式
			sql.append(" select count(*) stu_num,nvl2(m.isadult,m.isadult ,'N') isadult");
			resultMap.put("statisticColumn", "isadult");
			resultMap.put("tdStr", "学习模式");
			
		}else if ("1".equals(statisticType)) {//专业类别
			sql.append(" select count(*) stu_num,m.resourceid,m.majorname ");
			resultMap.put("statisticColumn", "majorname");
			resultMap.put("tdStr", "专业名称");
			
		}else if ("2".equals(statisticType)) {//学习层次
			sql.append(" select count(*) stu_num,c.resourceid,c.classicname  ");
			resultMap.put("statisticColumn", "classicname");
			resultMap.put("tdStr", "层次名称");
			
		}else if ("3".equals(statisticType)) {//考试类别
			sql.append(" select count(*) stu_num, nvl2(e.isapplynoexam,e.isapplynoexam,'N') isapplynoexam");
			resultMap.put("statisticColumn", "isapplynoexam");
			resultMap.put("tdStr", "考试类别");
			
		}else if ("4".equals(statisticType)) {//信息媒体来源
			sql.append(" select count(*) stu_num, nvl2(e.frommedia,e.frommedia,'未选择信息媒体来源') frommedia");
			resultMap.put("statisticColumn", "frommedia");
			resultMap.put("tdStr", "媒体类别");
			
		}else if ("5".equals(statisticType)) {//报名方式
			sql.append(" select count(*) stu_num, nvl2(e.enrolleetype,e.enrolleetype,0) enrolleetype");
			resultMap.put("statisticColumn", "enrolleetype");
			resultMap.put("tdStr", "报名方式");
			
		}else if ("6".equals(statisticType)) {//户口所在地
			sql.append(" select nvl2(bs.residence,bs.residence,'未填写') residence,count(*) stu_num ");
			resultMap.put("statisticColumn", "residence");
			resultMap.put("tdStr", "户口所在地");
			
		}else if ("7".equals(statisticType)) {//考生籍贯
			sql.append(" select nvl2(bs.homeplace,bs.homeplace,'未填写') homeplace ,count(*) stu_num ");
			resultMap.put("statisticColumn", "homeplace");
			resultMap.put("tdStr", "籍贯");
			
		}else if ("8".equals(statisticType)) {//政治面貌
			sql.append(" select nvl2(bs.politics,bs.politics,'其它') politics,count(*) stu_num ");
			resultMap.put("statisticColumn", "politics");
			resultMap.put("tdStr", "政治面貌");
			
		}else if ("9".equals(statisticType)) {//民族
			sql.append(" select nvl2(bs.nation,bs.nation,'其它') nation,count(*) stu_num ");
			resultMap.put("statisticColumn", "nation");
			resultMap.put("tdStr", "民族");
			
		}else if ("10".equals(statisticType)) {//婚否
			sql.append(" select nvl2(bs.marriage,bs.marriage,'未填写') marriage,count(*) stu_num ");
			resultMap.put("statisticColumn", "marriage");
			resultMap.put("tdStr", "婚姻状况");
			
		}else if ("11".equals(statisticType)) {//性别
			sql.append(" select nvl2(bs.gender,bs.gender,'其它') gender,count(*) stu_num ");
			resultMap.put("statisticColumn", "gender");
			resultMap.put("tdStr", "性别");
		}
		resultMap.put("refixSQL", sql.toString());
		return resultMap;
	}
	/**
	 * 生成统计SQL的后半部分
	 * @param statisticType
	 * @param condition
	 * @return
	 */
	private String genStatisticSuffixSQL(String statisticType,Map<String, Object> condition){
		
		StringBuffer sql  = new StringBuffer();
			//FIXED by hzg 新增所在年度统计
			sql.append("   from EDU_RECRUIT_ENROLLEEINFO e,EDU_BASE_STUDENT bs,EDU_BASE_CLASSIC c,EDU_RECRUIT_MAJOR rm,EDU_BASE_MAJOR m,EDU_RECRUIT_RECRUITPLAN p,EDU_BASE_GRADE g ")
			.append("  where e.RECRUITMAJORID    = rm.resourceid  ")
			.append("	and rm.majorid          = m.resourceid  ")
			.append("	and rm.classic			= c.resourceid ")
			.append("	and e.studentbaseinfoid = bs.resourceid  ")
			.append("	and rm.Recruitplanid    = p.resourceid ")
			.append(" and e.GRADE = g.resourceid ")
			.append("	and e.isDeleted         = 0 ")
			.append(genStatisticParamSQL(condition));
			
		int type = Integer.parseInt(statisticType);	
		switch (type) {
			case 0 ://学习模式
				sql.append(" group by m.isadult");	
				break;
			case 1 ://专业类别
				sql.append(" group by m.resourceid,m.majorname");
				break;
			case 2 ://学习层次
				sql.append(" group by c.resourceid,c.classicname");
				break;	
			case 3 ://考试类别
				sql.append(" group by e.isapplynoexam ");
				break;	
			case 4 ://信息媒体来源
				sql.append(" group by e.FROMMEDIA ");	
				break;	
			case 5 ://报名方式
				sql.append(" group by e.enrolleetype ");
				break;	
			case 6 ://户口所在地
				sql.append(" group by bs.residence  ");
				break;	
			case 7 ://考生籍贯
				sql.append(" group by bs.homeplace  ");
				break;	
			case 8 ://政治面貌
				sql.append(" group by bs.politics    ");
				break;	
			case 9 ://民族
				sql.append(" group by bs.nation   ");
				break;	
			case 10 ://婚否
				sql.append(" group by bs.marriage  ");
				break;	
			case 11 : //性别
				sql.append(" group by bs.gender  ");
				break;	
		}	
		return sql.append(" order by stu_num desc").toString();
	}


	/**
	 * 根据招生批次及校外学习中心查询给定批次下考场的安排情况
	 * @param recruitPlan
	 * @param branchSchool
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public List<Map<String,Object>>  findExamRoomAssignByRecruitPlanAndBrschool(Map<String,Object> paramMap ) throws ServiceException {
		List<Map<String,Object>> list 	= new ArrayList<Map<String,Object>>();
		String sql  = " select er.resourceid,er.EXAMROOMNAME,er.EXAMROOMSIZE,count(rp.resourceid) AS assigned ";
		       sql += "   from EDU_BASE_EXAMROOM er left join EDU_RECRUIT_EXAMROOMPLAN rp";
		       sql += "     on rp.examroomid = er.resourceid";
		       sql += "    and rp.recruitplanid=:selectExamRoom_recruitPlan";
		       sql += "    and rp.isdeleted=0";
		       sql += "  where er.isDeleted=0";
		       sql += "    and er.BRANCHSCHOOLID=:selectExamRoom_brSchool and er.isComputerRoom=:selectExamRoom_isComputerRoom";
		       sql += " group by er.EXAMROOMNAME,er.EXAMROOMSIZE,er.resourceid";
		       sql += " order by assigned desc";
		try {
			list    = this.baseJdbcTemplate.findForListMap(sql, paramMap);
		} catch (Exception e) {
			e.printStackTrace();
		}       
		return list;
	}

	/**
	 * 查询入学考试座位分配列表
	 * @param paramMap
	 * @param page
	 * @return
	 * @throws ServiceException
	 */

	@Override
	public Page findEntranceExamSeatList(Map<String, Object> paramMap, Page page)throws ServiceException {
		/*
		StringBuffer sql = new StringBuffer();
		sql.append(" select i.examcertificateno,erp.seatnum,s.name,s.gender,r.examroomname,c.classicname,m.majorname,u.unitname");
		sql.append("   from EDU_RECRUIT_EXAMROOMPLAN erp,EDU_BASE_EXAMROOM r,edu_recruit_enrolleeinfo i, ");
		sql.append("        edu_base_student s,edu_recruit_major rm,edu_base_classic c ,edu_base_major m ,hnjk_sys_unit u");
		sql.append("  where erp.isdeleted = 0 and i.isdeleted = 0 ");
		sql.append("    and erp.examroomid = r.resourceid  ");
		sql.append("    and erp.enrolleeinfoid = i.resourceid ");
		sql.append("    and i.studentbaseinfoid = s.resourceid ");
		sql.append("    and i.recruitmajorid = rm.resourceid  ");
		sql.append("    and rm.classic = c.resourceid ");
		sql.append("    and rm.majorid = m.resourceid ");
		sql.append("    and i.branchschoolid = u.resourceid");
		
		List<Object> param = new ArrayList<Object>();
		
		if (paramMap.containsKey("classicId")) {
		   sql.append(" and c.resourceid = ?  ");
		   param.add(paramMap.get("classicId"));
		}
		if (paramMap.containsKey("majorId")) {
		   sql.append(" and m.resourceid = ?  ");
		   param.add(paramMap.get("majorId"));
		}
		if (paramMap.containsKey("brSchoolId")) {
		   sql.append(" and r.branchschoolid = ? ");
		   param.add(paramMap.get("brSchoolId"));
		}
		// 学习中心 (没有考场合并记录)
		if (paramMap.containsKey("brSchoolId") && !paramMap.containsKey("mergeExamBrschool")) {
			 sql.append(" and r.branchschoolid = ? ");
			 param.add(paramMap.get("brSchoolId"));
		// 有其它学习中心需要合并到当前学习中习	
		}else if(paramMap.containsKey("brSchoolId") && paramMap.containsKey("mergeExamBrschool")){
			 sql.append(" and r.branchschoolid in( "+paramMap.get("mergeExamBrschool")+")");
		}
		if (paramMap.containsKey("recruitPlan")) {
		   sql.append(" and rm.recruitplanid = ?  ");
		   param.add(paramMap.get("recruitPlan"));
		}

		return this.baseJdbcTemplate.findList(page, sql.toString(), param.toArray(), EntranceExamSeatVo.class);
		*/
		return null;
	}


	/**
	 * 查询入学考试座位分配总数
	 * @param paramMap
	 * @return
	 * @throws Exception 
	 */
	@Override
	public Long findEntranceExamSeatListCount(Map<String, Object> paramMap)throws Exception {
		
		StringBuffer sql = new StringBuffer();
		sql.append(" select count(i.examcertificateno)");
		sql.append("   from EDU_RECRUIT_EXAMROOMPLAN erp,EDU_BASE_EXAMROOM r,edu_recruit_enrolleeinfo i, ");
		sql.append("        edu_base_student s,edu_recruit_major rm,edu_base_classic c ,edu_base_major m ");
		sql.append("  where erp.isdeleted = 0 and i.isdeleted = 0 ");
		sql.append("    and erp.examroomid = r.resourceid  ");
		sql.append("    and erp.enrolleeinfoid = i.resourceid ");
		sql.append("    and i.studentbaseinfoid = s.resourceid ");
		sql.append("    and i.recruitmajorid = rm.resourceid  ");
		sql.append("    and rm.classic = c.resourceid ");
		sql.append("    and rm.majorid = m.resourceid ");
		

		
		if (paramMap.containsKey("classicId")) {
		   sql.append(" and c.resourceid = :classicId  ");

		}
		if (paramMap.containsKey("majorId")) {
		   sql.append(" and m.resourceid = :majorId  ");

		}
		if (paramMap.containsKey("brSchoolId")) {
		   sql.append(" and r.branchschoolid = :brSchoolId ");

		}
		if (paramMap.containsKey("recruitPlanId")) {
		   sql.append(" and rm.recruitplanid = :recruitPlanId  ");

		}
		
		return this.baseJdbcTemplate.findForLong(sql.toString(), paramMap);
	}


	/**
	 * 查询校外学习中心的试室座位按排情况
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<Map<String, Object>> findBrSchoolExamRoomSeatList(Map<String, Object> condition)throws Exception {
		
		StringBuffer sql = new StringBuffer();
			sql.append("  select r.resourceid,r.examroomname,count(p.resourceid) as counts ");
			sql.append("    from edu_base_examroom r ");
			sql.append(" left join edu_recruit_examroomplan p on  r.resourceid = p.examroomid and p.isdeleted=0 ");
		//---left join 条件---	
		if (condition.containsKey("recruitPlan")) {
			sql.append("   and p.recruitplanid   = :recruitPlan ");
		}
		//---left join 条件---
		
		//---主表 条件---
			sql.append(" where r.isdeleted = 0");
		if (condition.containsKey("branchSchool")) {
			sql.append("   and r.branchschoolid  = :branchSchool ");
		}
		if (condition.containsKey("examRoomId")) {
			sql.append("   and r.resourceid =:examRoomId");
		}
		//---主表 条件---
			sql.append(" group by r.examroomname,r.resourceid");
			sql.append(" order by counts desc");
			
		return this.baseJdbcTemplate.findForListMap(sql.toString(), condition);
	}


	/**
	 * 某个招生批次的所有考试课程
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<Map<String, Object>> findAllExamSubJectByRecruitPlanId(Map<String, Object> condition) throws Exception {
		String sql = " select distinct(F_DICTIONARY('CodeEntranceExam',s.coursename)) as COURSENAME,s.starttime,s.endtime "
		           + " from edu_recruit_major m,edu_recruit_examsubject s "
		           + " where s.isdeleted = 0  and  s.recruitmajorid = m.resourceid "
		           + " and m.recruitplanid =:recruitPlan"
			       + " order by s.starttime ";
		return this.baseJdbcTemplate.findForListMap(sql, condition);
	}

	/**
	 * 查询指定招生批次的所有考试科目组名称(字典值)
	 * @param recruitPlanId
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<Map<String,Object>> findRecruitPlansCourseGroupName(Map<String,Object> condition) {
		
		StringBuffer sql         = new StringBuffer();
		
		try {
			sql.append("select distinct t.coursegroupname ");
			sql.append("  from edu_recruit_examsubject t ");
			sql.append(" where t.isdeleted = 0  ");
			sql.append("   and exists( ");
			sql.append("       select m.resourceid from edu_recruit_major m where m.recruitplanid =:recruitPlan");
			sql.append("          and m.isdeleted = 0 ");
			sql.append("          and m.resourceid = t.recruitmajorid ) ");

			return  baseJdbcTemplate.findForListMap(sql.toString(), condition);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<Map<String,Object>>();
	}



	/**
	 * 查询报名信息表，供资格审核用(按W\N\Y排序)
	 * @param paramMap
	 * @param page
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public Page findEnrolleeInfoForAudit(Map<String, Object> condition, Page page)throws ServiceException {
		
		Map<String,Object> param = new HashMap<String, Object>();
		StringBuffer sql 		 = new StringBuffer();
		
		param.put("isDeleted", 0);
		sql.append(" select i.resourceid,u.unitname,m.majorname,c.classicname,s.name,s.gender,i.examCertificateNo,i.matriculatenoticeno,s.certnum,");
		sql.append(" i.signupflag,i.noexamflag,i.entranceflag,i.grade,i.entrancecheckman,i.isapplynoexam ,i.noexamcheckmemo");
		sql.append("   from edu_recruit_enrolleeinfo i");
		sql.append("  inner join edu_base_student s  on i.studentbaseinfoid = s.resourceid ");
		if (condition.containsKey("name")) {
			sql.append(" and s.name like '%"+condition.get("name")+"%'");
		}
		if (condition.containsKey("certNum")) {
			sql.append(" and s.certnum= :certNum");
			param.put("certNum", condition.get("certNum"));
		}
		sql.append("  inner join hnjk_sys_unit u on i.branchschoolid = u.resourceid ");
		if (condition.containsKey("branchSchool")) {
			sql.append(" and u.resourceid=:branchSchool ");
			param.put("branchSchool", condition.get("branchSchool"));
		}
		sql.append("  inner join edu_recruit_major rm on i.recruitmajorid = rm.resourceid ");
		if (condition.containsKey("recruitPlan")) {
			sql.append(" and rm.recruitplanid =:recruitPlan");
			param.put("recruitPlan", condition.get("recruitPlan"));
		}
		if (condition.containsKey("recruitMajor")) {
			sql.append(" and rm.resourceid =:recruitMajor");
			param.put("recruitMajor", condition.get("recruitMajor"));
		}
		sql.append("  inner join edu_base_major m on rm.majorid = m.resourceid ");
		sql.append("  inner join edu_base_classic c on rm.classic = c.resourceid ");
		sql.append("  where i.isdeleted =:isDeleted");
		
		if (condition.containsKey("isApplyNoexam")) {
			sql.append("  and i.isapplynoexam=:isApplyNoexam");
			param.put("isApplyNoexam", condition.get("isApplyNoexam"));
		}
		if (condition.containsKey("signupFlag")) {
			sql.append("  and i.signupflag=:signupFlag");
			param.put("signupFlag", condition.get("signupFlag"));
		}
		if (condition.containsKey("entranceFlag")) {
			sql.append("  and i.entranceflag=:entranceFlag");
			param.put("entranceFlag", condition.get("entranceFlag"));
		}
		if (condition.containsKey("entranceFlag")) {
			sql.append("  and i.entranceflag=:entranceFlag");
			param.put("entranceFlag", condition.get("entranceFlag"));
		}
		if (condition.containsKey("learningStyle")) {
			sql.append(" and i.stutymode=:learningStyle");
			param.put("learningStyle", condition.get("learningStyle"));
		}
		if (condition.containsKey("examCertificateNo")) {
			sql.append(" and i.examcertificateno=:examCertificateNo");
			param.put("examCertificateNo", condition.get("examCertificateNo"));
		}
		
		if (condition.containsKey("entranceAuditSQL")) {
			sql.append(condition.get("entranceAuditSQL"));
		}
		if (condition.containsKey("noExamAuditSQL")) {
			sql.append(condition.get("noExamAuditSQL"));
		}
		
		sql.append(" order by decode(i."+condition.get("orderByFlag")+",'W','1','N','2','Y','3','1') ,u.unitcode");
		return findBySql(page, sql.toString(), param);
	}
	public Page findBySql(Page page, String sql, Map<String, Object> values) {
		Assert.notNull(page, "page不能为空");
				
		if (page.isAutoCount()) {
			Long totalCount = countSqlResult(sql.toString(), values);
			page.setTotalCount(totalCount.intValue());
		}
		
		if(page.isOrderBySetted()){
			sql += " order by "+page.getOrderBy()+" "+ page.getOrder()+" ";
		}		
		try {			
			List resutList = baseJdbcTemplate.findForListMap(" select * from (select r.*,rownum rn from ("+sql.toString()+") r where rownum<="+(page.getFirst()+page.getPageSize())+") where rn>"+page.getFirst(), values);
			page.setResult(resutList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return page;
	}	
	private Long countSqlResult(String sql, Map<String, Object> condition ){
		String countSql = "select count(*) from (" + sql+" ) ";
		try {
			Long count = baseJdbcTemplate.findForLong(countSql, condition);
			return count;
		} catch (Exception e) {
			throw new RuntimeException("sql can't be auto count, hql is:" + countSql, e);
		}
	}
	
	@Override
	public Map<String, Object> statFinalExamResult(String examSubId, String courseId, String brSchool) throws ServiceException {
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("examSubId", examSubId);
		StringBuffer sql = new StringBuffer();		
		sql.append("select nvl(sum(rt.totalCount),0) totalCount, nvl(sum(rt.passCount),0) passCount, nvl(sum(rt.nopassCount),0) nopassCount, nvl(sum(rt.noscoreCount),0) noscoreCount from ( ");
		sql.append("    select decode(rs.examstatus,'pass',count(rs.examstatus)) passCount, ");
		sql.append("    decode(rs.examstatus,'nopass',count(rs.examstatus)) nopassCount, ");
		sql.append("    decode(rs.examstatus,'noscore',count(rs.examstatus)) noscoreCount,count(*) totalCount from ( ");
		sql.append("        select case when t.examscore>=60 then 'pass' ");
		sql.append("                    when t.examscore<60 then 'nopass' ");
		sql.append("	                else 'noscore' end examstatus ");
		sql.append("        from edu_recruit_stustates t ");
		sql.append("        join Edu_Teach_Examinfo info on info.resourceid=t.examinfoid ");
		sql.append("        where t.isdeleted=0 and info.ismachineexam='Y' ");		
		sql.append("        and info.examsubid=:examSubId ");
		if(ExStringUtils.isNotBlank(courseId)){
			sql.append("    and info.courseid=:courseId ");
			condition.put("courseId", courseId);
		}
		if(ExStringUtils.isNotBlank(brSchool)){
			sql.append("    and t.brschoolid=:brSchool ");
			condition.put("brSchool", brSchool);
		}
		sql.append("    ) rs ");
		sql.append("    group by rs.examstatus ");
		sql.append(") rt ");
		
		try {
			List<Map<String,Object>> list = this.baseJdbcTemplate.findForListMap(sql.toString(), condition);
			if(null != list&&!list.isEmpty()){
				return list.get(0);				
			}
		} catch (Exception e) {
			e.printStackTrace();			
		}
		return null;
	}
	@Override
	public List<Map<String, Object>> getRecruitExamSeatByCondition(Map<String, Object> condition) throws ServiceException {
		StringBuffer sql = new StringBuffer();
		Map<String,Object> param = new HashMap<String, Object>(0);
		sql.append(" select m.majorname,c.classicname,rp.recruitplanname,u.unitname,i.examcertificateno,erp.seatnum,s.name, ");
		sql.append("        f_dictionary('CodeSex',s.gender) as GENDER,r.examroomname ");
		sql.append("   from EDU_RECRUIT_EXAMROOMPLAN erp,EDU_BASE_EXAMROOM r,edu_recruit_enrolleeinfo i, ");
		sql.append("        edu_base_student s,edu_recruit_major rm,edu_base_classic c ,edu_base_major m ,hnjk_sys_unit u,edu_recruit_recruitplan rp ");
		sql.append("  where erp.isdeleted       = 0 ");
		sql.append("    and i.isdeleted         = 0 ");
		sql.append("    and erp.examroomid      = r.resourceid ");
		sql.append("    and erp.enrolleeinfoid  = i.resourceid ");
		sql.append("    and i.studentbaseinfoid = s.resourceid ");
		sql.append("    and i.recruitmajorid    = rm.resourceid ");
		sql.append("    and rm.classic          = c.resourceid ");
		sql.append("    and rm.majorid          = m.resourceid ");
		sql.append("    and u.resourceid        = r.branchschoolid ");
		sql.append("    and rm.recruitplanid = rp.resourceid ");
		if(condition.containsKey("recruitPlan")){
			sql.append("    and  rm.recruitplanid = :recruitPlanId ");
			param.put("recruitPlanId", condition.get("recruitPlan"));
		}
		if(condition.containsKey("branchSchool")){
			sql.append("    and  r.branchschoolid = :brSchoolId ");
			param.put("brSchoolId", condition.get("branchSchool"));
		}
//		sql.append("    and  c.resourceid     = :classicId ");
//		sql.append("    and  m.resourceid     = :majorId ");
		sql.append(" order by m.majorname,c.classicname,i.examcertificateno desc ");
		//sql.append(" order by i.examcertificateno desc ");
		
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>(0);
		try {
			list = this.baseJdbcTemplate.findForListMap(sql.toString(), param);		
		} catch (Exception e) {
			e.printStackTrace();			
		}
		return list;
	}
	
	@Override
	public List<Map<String, Object>> statRecruitExamByCondition(Map<String, Object> condition) throws ServiceException{
		Map<String, Object> values = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();	
		if(condition.containsKey("statDate")){
			values.put("statDate", condition.get("statDate"));			
			//					场次			应考			实考			缺考
			sql.append(" select rs.examplanname statname,sum(nvl(allCount,0)) allCount,sum(nvl(realCount1,0)+nvl(oneCount,0)) realCount,sum(nvl(oneCount,0)) oneCount,sum(nvl(absentCount,0)) absentCount ");
			sql.append(" from (select r.examplanname,r.examType,count(r.examType) allCount,decode(r.examType,2,count(r.examType)) realCount1, ");
			sql.append("       decode(r.examType,1,count(r.examType)) oneCount,decode(r.examType,0,count(r.examType)) absentCount ");
			sql.append("       from (select p.examplanname,t.resourceid,sum(case when s.status>-1 then 1 else 0 end) examType  ");
			sql.append("             from edu_recruit_examroomplan t  ");
			sql.append("             join edu_recruit_stustates s on s.examroomplanid=t.resourceid     ");
			sql.append("             join edu_recruit_examplan p on p.resourceid=t.examplanid    ");
			sql.append("             where t.isdeleted=0 ");
			sql.append("             and exists ( ");
			sql.append("                 select d.resourceid from edu_recruit_explandetails d ");
			sql.append("                 where d.isdeleted=0 and d.examplanid=t.examplanid and (to_char(d.starttime,'yyyy-MM-dd')=:statDate or to_char(d.endtime,'yyyy-MM-dd')=:statDate) ");
			sql.append("             )          ");
			sql.append("             group by p.examplanname,t.resourceid ) r ");
			sql.append("       group by r.examplanname,r.examType ) rs ");
			sql.append(" group by rs.examplanname ");
			sql.append(" order by rs.examplanname ");
		} else if(condition.containsKey("recruitPlanId")){
			values.put("recruitPlanId", condition.get("recruitPlanId"));				
			sql.append(" select rs.unitname statname,sum(nvl(allCount,0)) allCount,sum(nvl(realCount1,0)+nvl(oneCount,0)) realCount,sum(nvl(oneCount,0)) oneCount,sum(nvl(absentCount,0)) absentCount ");
			sql.append(" from (select r.unitcode,r.unitname,r.examType,count(r.examType) allCount,decode(r.examType,2,count(r.examType)) realCount1, ");
			sql.append("       decode(r.examType,1,count(r.examType)) oneCount,decode(r.examType,0,count(r.examType)) absentCount ");
			sql.append("       from (select u.unitcode,u.unitname,t.resourceid,sum(case when s.status>-1 then 1 else 0 end) examType  ");
			sql.append("             from edu_recruit_examroomplan t  ");
			sql.append("             join edu_recruit_stustates s on s.examroomplanid=t.resourceid     ");
			sql.append("             join edu_recruit_recruitplan p on p.resourceid=t.recruitplanid ");
			sql.append("             join edu_recruit_enrolleeinfo ei on ei.resourceid=t.enrolleeinfoid ");
			sql.append("             join hnjk_sys_unit u on u.resourceid=ei.branchschoolid ");
			sql.append("             where t.isdeleted=0 ");
			sql.append("             and p.resourceid=:recruitPlanId ");
			if(condition.containsKey("examDate")){
				values.put("examDate", condition.get("examDate"));
				sql.append("             and exists (select d.resourceid from edu_recruit_explandetails d where d.isdeleted=0 and d.examplanid=t.examplanid and to_char(d.starttime,'yyyy-MM-dd')=:examDate) ");
			}			
			sql.append("             group by u.unitcode,u.unitname,t.resourceid ) r ");
			sql.append("       group by r.unitcode,r.unitname,r.examType ) rs ");
			sql.append(" group by rs.unitcode,rs.unitname ");
			sql.append(" order by rs.unitcode ");			
		}

		try {	
			if(sql.length()>0){
				return baseJdbcTemplate.findForListMap(sql.toString(), values);
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Collections.EMPTY_LIST;
	}


	/**
	 * 找到和条件相似的学生
	 * @param condition
	 * @return
	 */
	@Override
	public String getStudentInfoByQuery(Map<String, Object> condition)
			throws ServiceException {
		Map<String,Object> param = new HashMap<String, Object>();
		String syudentno = "";
		StringBuffer sql = new StringBuffer(" select s.STUDYNO from (select * from EDU_ROLL_STUDENTINFO stu where 1 = 1 and length(stu.STUDYNO) = 9 ");
		
		if( condition.containsKey("classic") ){
			sql.append(" and stu.CLASSICID = :classic ");
			param.put("classic", condition.get("classic"));
		}
		if( condition.containsKey("grade") ){
			sql.append(" and stu.GRADEID = :grade ");
			param.put("grade", condition.get("grade"));		
		}
		if( condition.containsKey("major") ){
			sql.append(" and stu.MAJORID = :major ");
			param.put("major", condition.get("major"));
		}
		sql.append(" order by stu.STUDYNO desc ) s where rownum = 1 ");
		try {
			@SuppressWarnings("rawtypes")
			Map map  =	this.baseJdbcTemplate.findForMap(sql.toString(),param);
			if(null!=map){
				syudentno = map.get("STUDYNO").toString();
			}
		} catch (Exception e) {	
			e.printStackTrace();
		}
		
		return syudentno;
	}


	/**
	 * 查找学号是否可用
	 * @param no 学号
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public Boolean checkStudentInfoByNo(String no) throws ServiceException {
		Map<String,Object> param = new HashMap<String, Object>();
		Boolean flag = false;
		StringBuffer sql = new StringBuffer(" select count(*) num from EDU_ROLL_STUDENTINFO stu where stu.studyno = :studyno");
		param.put("studyno", no);
		try {
			System.out.println("sql:"+sql);
			@SuppressWarnings("rawtypes")
			Map map  =	this.baseJdbcTemplate.findForMap(sql.toString(),param);
			if( null != map ){
				System.out.println(map.get("num"));
				if( Integer.parseInt(map.get("num").toString()) == 1){
					flag = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
}
