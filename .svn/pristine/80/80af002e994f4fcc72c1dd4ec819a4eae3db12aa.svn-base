package com.hnjk.edu.recruit.service.impl;

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
import com.hnjk.core.foundation.utils.ExCollectionUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.basedata.model.Classic;
import com.hnjk.edu.basedata.model.Grade;
import com.hnjk.edu.basedata.model.Major;
import com.hnjk.edu.basedata.service.IClassicService;
import com.hnjk.edu.basedata.service.IGradeService;
import com.hnjk.edu.basedata.service.IMajorService;
import com.hnjk.edu.recruit.model.EnrollmentBookInfo;
import com.hnjk.edu.recruit.service.IEnrollmentBookInfoService;
import com.hnjk.edu.recruit.service.IRecruitmentScopeService;
import com.hnjk.edu.recruit.vo.EnrollmentBookInfoVO;
import com.hnjk.edu.teaching.model.TeachingActivityTimeSetting;
import com.hnjk.edu.teaching.service.ITeachingActivityTimeSettingService;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.OrgUnit;
import com.hnjk.security.model.User;
import com.hnjk.security.service.IOrgUnitService;

/** 
 * @author Zik, 广东学苑教育发展有限公司
 * @since 2018年6月28日 下午4:49:57 
 * 
 */
@Service("enrollmentBookInfoService")
@Transactional
public class EnrollmentBookInfoServiceImpl extends BaseServiceImpl<EnrollmentBookInfo> implements
		IEnrollmentBookInfoService {
	
	@Autowired 
	@Qualifier("baseSupportJdbcDao")	
	private IBaseSupportJdbcDao jdbcDao;//注入jdbc template 支持
	
	@Autowired
	@Qualifier("teachingActivityTimeSettingService")
	private ITeachingActivityTimeSettingService teachingActivityTimeSettingService;
	
	@Autowired
	@Qualifier("recruitmentScopeService")
	private IRecruitmentScopeService recruitmentScopeService;
	
	@Autowired
	@Qualifier("orgUnitService")
	private IOrgUnitService orgUnitService;
	
	@Autowired
	@Qualifier("gradeService")
	private IGradeService gradeService;
	
	@Autowired
	@Qualifier("classicService")
	private IClassicService classicService;
	
	@Autowired
	@Qualifier("majorService")
	private IMajorService majorService;
	
	@Transactional(readOnly=true)
	@Override
	public Page findByCondition(Map<String, Object> condition, Page objPage) throws ServiceException{
		Map<String,Object> values =  new HashMap<String, Object>();
		StringBuffer hql = findByConditionHql(condition, values);
		hql.append(" order by "+objPage.getOrderBy() +" "+ objPage.getOrder());
		
		return exGeneralHibernateDao.findByHql(objPage, hql.toString(), values);
	}

	/**
	 * 查询列表hql
	 * 
	 * @param condition
	 * @param values
	 * @return
	 */
	private StringBuffer findByConditionHql(Map<String, Object> condition, Map<String, Object> values) {
		StringBuffer hql = new StringBuffer(1024);
		hql.append(" from "+EnrollmentBookInfo.class.getSimpleName()+" where isDeleted = :isDeleted ");
		values.put("isDeleted", 0);
		if(condition.containsKey("unitId")){
			hql.append(" and unit.resourceid=:unitId ");
			values.put("unitId", condition.get("unitId"));
		}
		if(condition.containsKey("gradeId")){
			hql.append(" and grade.resourceid=:gradeId ");
			values.put("gradeId", condition.get("gradeId"));
		}
		if(condition.containsKey("classicId")){
			hql.append(" and classic.resourceid=:classicId ");
			values.put("classicId", condition.get("classicId"));
		}
		if(condition.containsKey("majorId")){
			hql.append(" and major.resourceid=:majorId ");
			values.put("majorId", condition.get("majorId"));
		}
		if(condition.containsKey("studentName")){
			hql.append(" and studentName=:studentName ");
			values.put("studentName", "%"+condition.get("studentName")+"%");
		}
		if(condition.containsKey("certNum")){
			hql.append(" and certNum=:certNum ");
			values.put("certNum", condition.get("certNum"));
		}
		if(condition.containsKey("operatorName")){
			hql.append(" and operatorName=:operatorName ");
			values.put("operatorName", "%"+condition.get("operatorName")+"%");
		}
		if(condition.containsKey("resourceids")){
			hql.append(" and resourceid in ('"+condition.get("resourceids")+"') ");
		}
		return hql;
	}

	@Override
	public void batchCascadeDelete(String[] ids) throws ServiceException{
		if(ids!=null && ids.length>0){
			for(String id : ids){
				delete(id);				
				logger.info("批量删除="+id);
			}
		}
	}

	/**
	 * 是否允许操作
	 * @param yearInfoId
	 * @return
	 */
	@Override
	public Map<String, Object> isAllowOperate(String yearInfoId) {
		Map<String, Object> resultMap  = new HashMap<String, Object>(2);
		int statusCode = 200;
		String message = null;
		do{
			// 获取当前时间
			Date today = new Date();
			// 先判断是否在规定的时间段内
			Map<String,Object> condition = new HashMap<String, Object>(5);
			condition.put("mainProcessType", "enrollmentBooking");
			condition.put("yearInfo", yearInfoId);
			condition.put("orderBy", "term");
			condition.put("order", "DESC");
			List<TeachingActivityTimeSetting> tafList = teachingActivityTimeSettingService.findTeachingActivityTimeSettingByCondition(condition);
			if(ExCollectionUtils.isEmpty(tafList)){
				 statusCode = 300;
				 message = "请联系管理员设置教学活动时间！";
				 continue;
			}
			TeachingActivityTimeSetting taf = tafList.get(0);
			if(!(today.after(taf.getStartTime()) && today.before(taf.getEndTime()))){
				statusCode = 300;
				 message = "对不起，该操作不在教学活动期间内！";
				 continue;
			}
		} while(false);
		
		resultMap.put("statusCode", statusCode);
		resultMap.put("message", message);
		
		return resultMap;
	}

	/**
	 * 获取某个年级所有的招生报读学生的身份证号
	 * @param gradeId
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<String> findCertNumByGradeId(String gradeId) throws Exception {
		List<String> certNumList = null;
		StringBuffer sql = new StringBuffer(250);
		sql.append("select b.certnum from EDU_ENROLLMENTBOOK_INFO b where b.isdeleted=0 and b.gradeid=?");
		List<Map<String,Object>> list = jdbcDao.getBaseJdbcTemplate().getOriginalJdbcTemplate().queryForList(sql.toString(), new Object[]{gradeId});
		if(ExCollectionUtils.isNotEmpty(list)){
			certNumList = new ArrayList<String>(list.size());
			for(Map<String,Object> m : list){
				certNumList.add((String)m.get("certnum"));
			}
		}
		return certNumList;
	}

	/**
	 * 根据身份证号判断该学生是否属于该教学点的招生范围
	 * @param certNum
	 * @param scopeList
	 * @return
	 */
	@Override
	public boolean isInScope(String certNum, List<String> scopeList) {
		boolean inScope = false;
		if(ExStringUtils.isNotBlank(certNum) && ExCollectionUtils.isNotEmpty(scopeList)){
			if(scopeList.contains(certNum.substring(0, 6))){
				inScope = true;
			}
		}
		
		return inScope;
	}

	/**
	 * 处理导入招生预约报读信息
	 * 
	 * @param modelList
	 * @param gradeId
	 * @return
	 */
	@Override
	public Map<String, Object> handleEnrollmentBookInfoImport( List<EnrollmentBookInfoVO> modelList, String gradeId) {
		Map<String, Object> retrunMap = new HashMap<String, Object>();
		int statusCode = 200;
		StringBuffer msg = new StringBuffer(""); // 出错信息
		// 失败记录
		List<EnrollmentBookInfoVO> failList = new ArrayList<EnrollmentBookInfoVO>(50);
		List<EnrollmentBookInfo> saveList = new ArrayList<EnrollmentBookInfo>(100);
		try {
			do {
				if (ExCollectionUtils.isEmpty(modelList)) {
					statusCode = 300;
					msg.append("没有招生预约报读信息数据");
					continue;
				}
				// 获取年级信息
				Grade grade = gradeService.load(gradeId);
				// 判断角色
				boolean isAdmin = true;
				OrgUnit unit = null;
				// 招生范围
				List<String> scopeList = null;
				// 操作人
				User curUser = SpringSecurityHelper.getCurrentUser();
				if (CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(curUser.getOrgUnit().getUnitType())) {
					isAdmin = false;
					unit = curUser.getOrgUnit();
					scopeList = recruitmentScopeService.findScopeByUnitId(unit.getResourceid());
				}
				// 获取教学点列表
				Map<String, OrgUnit> unitMap = getUnitMap();
				// 获取层次列表
				Map<String, Classic> classicMap = getClassicMap();
				// 获取专业列表
				Map<String, Major> majorMap = getMajorMap();
				// 获取同一个年级，所有学生的身份证号
				List<String> hasExistStudentList = findCertNumByGradeId(gradeId);
				
				List<String> existList = new ArrayList<String>(500);
				EnrollmentBookInfo ebi = null;
				String certNum = null;
				Classic classic = null;
				Major major = null;
				Date today = new Date();
				for(EnrollmentBookInfoVO eb : modelList){
					// 如果是管理员，则所属教学点不能为空
					if(isAdmin){
						if(ExStringUtils.isBlank(eb.getUnitName())){
							msg.append("[<font color='red'>身份证号：").append(certNum).append("</font>],原因：所属教学点不能为空; </br>");
							eb.setErrorMsg("所属教学点不能为空");
							failList.add(eb);
							continue;
						}
						// 判断对应教学点是否存在
						unit = unitMap.get(eb.getUnitName());
						if(unit == null ){
							msg.append("[<font color='red'>身份证号：").append(certNum).append("</font>],原因：所属教学点不存在; </br>");
							eb.setErrorMsg("所属教学点不存在");
							failList.add(eb);
							continue;
						}
					}
					// 判断是否重复导入
					certNum = eb.getCertNum();
					if(existList.contains(certNum)){
						msg.append("[<font color='red'>身份证号：").append(certNum).append("</font>],原因：有重复的记录; </br>");
						eb.setErrorMsg("有重复的记录");
						failList.add(eb);
						continue;
					}
					existList.add(certNum);
					// 判断层次是否存在
					classic  = classicMap.get(eb.getClassicName());
					if(classic == null){
						msg.append("[<font color='red'>身份证号：").append(certNum).append("</font>],原因：预报读层次不存在！</br>");
						eb.setErrorMsg("预报读层次不存在！");
						failList.add(eb);
						continue;
					}
					// 判断专业是否存在
					major = majorMap.get(classic.getClassicCode()+eb.getMajorName());
					if(major == null){
						msg.append("[<font color='red'>身份证号：").append(certNum).append("</font>],原因：预报读专业不存在！</br>");
						eb.setErrorMsg("预报读专业不存在！");
						failList.add(eb);
						continue;
					}
					// 如果是教学点账号，则判断该学生是否在该教学点所属招生范围
					if(!isAdmin && !isInScope(eb.getCertNum(), scopeList)){
						msg.append("[<font color='red'>身份证号：").append(certNum).append("</font>],原因：该学生不属于您的招生范围内！</br>");
						eb.setErrorMsg("该学生不属于您的招生范围内！");
						failList.add(eb);
						continue;
					}
					// 判断该学生是否已存在
					if(hasExistStudentList.contains(certNum)){
						msg.append("[<font color='red'>身份证号：").append(certNum).append("</font>],原因：该学生已经预约了招生报读！</br>");
						eb.setErrorMsg("该学生已经预约了招生报读！");
						failList.add(eb);
						continue;
					}
					// 创建新对象
					ebi = new EnrollmentBookInfo();
					ebi.setCertNum(certNum);
					ebi.setStudentName(eb.getStudentName());
					ebi.setGrade(grade);
					ebi.setUnit(unit);
					ebi.setClassic(classic);
					ebi.setMajor(major);
					ebi.setPhone(eb.getPhone());
					ebi.setOperator(curUser);
					ebi.setOperatorName(curUser.getCnName());
					ebi.setCreateDate(today);
					// 加入列表
					saveList.add(ebi);
				}
				
				if (ExCollectionUtils.isNotEmpty(saveList)) {
					batchSaveOrUpdate(saveList);
				}
				
			} while (false);
			
			if(msg.length() >0) {
				statusCode = 400;
			}
		} catch (Exception e) {
			logger.error("处理导入招生预约报读信息出错", e);
			statusCode = 300;
			msg.setLength(0);
			msg.append("处理导入招生预约报读信息失败！");
		} finally {
			retrunMap.put("statusCode", statusCode);
			retrunMap.put("message", msg.toString());
			retrunMap.put("failList", failList);
		}
		
		return retrunMap;
	}
	
	
	private Map<String, OrgUnit> getUnitMap(){
		Map<String, OrgUnit> unitMap = new HashMap<String, OrgUnit>(50);
		List<OrgUnit> unitList = orgUnitService.findOrgUnitListByType("brSchool");
		if(ExCollectionUtils.isNotEmpty(unitList)){
			for(OrgUnit u : unitList){
				unitMap.put(u.getUnitName(), u);
			}
		}
		
		return unitMap;
	}

	private Map<String, Classic> getClassicMap(){
		Map<String, Classic> classicMap = new HashMap<String, Classic>(3);
		List<Classic> classicList = classicService.findByHql("from Classic where isDeleted=0");
		if(ExCollectionUtils.isNotEmpty(classicList)){
			for(Classic c : classicList){
				classicMap.put(c.getClassicName(), c);
			}
		}
		
		return classicMap;
	}
	
	private Map<String, Major> getMajorMap(){
		Map<String, Major> majorMap = new HashMap<String, Major>(100);
		List<Major> majorList = majorService.findByHql("from Major where isDeleted=0");
		if(ExCollectionUtils.isNotEmpty(majorList)){
			for(Major m : majorList){
				majorMap.put(m.getClassicCode()+m.getMajorName(), m);
			}
		}
		
		return majorMap;
	}

	/**
	 * 需要导出的信息
	 * 
	 * @param condition
	 * @return
	 * @throws Exception 
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<EnrollmentBookInfoVO> findVoByCondition(Map<String, Object> condition) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>(10);
		StringBuffer sql = new StringBuffer(500);
		sql.append("select  g.gradename,u.unitname,cc.classicname,m.majorname,i.studentname,i.certnum,i.phone from edu_enrollmentbook_info i ")
		.append("inner join edu_base_grade g on i.gradeid=g.resourceid and g.isdeleted=0 ")
		.append("inner join edu_base_classic cc on cc.isdeleted=0 and cc.resourceid=i.classicid ")
		.append("inner join edu_base_major m on m.isdeleted=0 and m.resourceid=i.majorid ")
		.append("inner join hnjk_sys_unit u on u.isdeleted=0 and u.resourceid=i.unitid ")
		.append("where i.isdeleted=0");
		
		if(condition.containsKey("unitId")){
			sql.append(" and u.resourceid=:unitId ");
			params.put("unitId", condition.get("unitId"));
		}
		if(condition.containsKey("gradeId")){
			sql.append(" and g.resourceid=:gradeId ");
			params.put("gradeId", condition.get("gradeId"));
		}
		if(condition.containsKey("classicId")){
			sql.append(" and cc.resourceid=:classicId ");
			params.put("classicId", condition.get("classicId"));
		}
		if(condition.containsKey("majorId")){
			sql.append(" and m.resourceid=:majorId ");
			params.put("majorId", condition.get("majorId"));
		}
		if(condition.containsKey("studentName")){
			sql.append(" and i.studentname=:studentName ");
			params.put("studentName", "%"+condition.get("studentName")+"%");
		}
		if(condition.containsKey("certNum")){
			sql.append(" and i.certnum=:certNum ");
			params.put("certNum", condition.get("certNum"));
		}
		if(condition.containsKey("operatorName")){
			sql.append(" and i.operatorname=:operatorName ");
			params.put("operatorName", "%"+condition.get("operatorName")+"%");
		}
		if(condition.containsKey("resourceids")){
			sql.append(" and i.resourceid in ('"+condition.get("resourceids")+"') ");
		}
		sql.append(" order by u.unitcode,g.gradename,cc.classiccode,m.majorcode,i.studentname ");
		
		return jdbcDao.getBaseJdbcTemplate().findList(sql.toString(), EnrollmentBookInfoVO.class, params);
	}
	
	
	
}
