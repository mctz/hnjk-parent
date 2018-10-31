package com.hnjk.edu.work.service.impl;

import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.work.model.CadreInfo;
import com.hnjk.edu.work.service.ICadreInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Function : 学生干部信息 - 接口实现类
 * <p>Author : msl
 * <p>Date   : 2018-07-23
 * <p>Description :CadreInfoServiceImpl
 */
@Transactional
@Service("cadreInfoService")
public class CadreInfoServiceImpl extends BaseServiceImpl<CadreInfo> implements ICadreInfoService {

	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;

	@Override
	public List<Map<String, Object>> getApplicationFormInfo(Map<String, Object> condition) {
		StringBuilder builder = new StringBuilder();
		//Map<String,Object> values = new HashMap<String, Object>();
		builder.append("select u.unitname,m.MAJORCODE,m.MAJORENNAME,classes.CLASSCODE,classes.CLASSESNAME,classes.studentnum,bs.gender,bs.nation,bs.politics,bs.bornday,bs.officename,bs.mobile,bs.photopath,");
		builder.append("ci.*,decode(mlist.hasnopass,0,'N','Y') hasnopass");
		builder.append(" from edu_work_cadreinfo ci");
		builder.append(" join edu_roll_studentinfo si on si.resourceid=ci.studentid and si.isdeleted=0");
		builder.append(" join edu_base_student bs on bs.resourceid=si.studentbaseinfoid and si.isdeleted=0");
		builder.append(" join hnjk_sys_unit u on u.resourceid=si.branchschoolid");
		builder.append(" join EDU_BASE_MAJOR m on m.RESOURCEID=si.MAJORID and m.ISDELETED=0");
		builder.append(" left join (select cl.RESOURCEID,cl.CLASSCODE,cl.CLASSESNAME,count(*) studentnum from EDU_ROLL_STUDENTINFO stu ");
		builder.append(" join EDU_ROLL_CLASSES cl on cl.RESOURCEID=stu.CLASSESID group by cl.RESOURCEID,cl.CLASSCODE,cl.CLASSESNAME) classes on classes.RESOURCEID=si.CLASSESID");
		builder.append(" left join(select ml.studentid,count(*) hasnopass from edu_teach_makeuplist ml where ml.isdeleted=0 group by ml.studentid) mlist on mlist.studentid=si.resourceid");
		builder.append(" where ci.isdeleted=0");
		if (condition.containsKey("resourceids")) {
			builder.append(" and ci.resourceid in ("+ ExStringUtils.addSymbol(condition.get("resourceids").toString().split(","),"'","'")+")");
		}
		if (condition.containsKey("branchSchoolid")) {
			builder.append(" and si.branchschoolid = :branchSchoolid");
		}
		if (condition.containsKey("classesid")) {
			builder.append(" and si.classesid = :classesid");
		}
		if (condition.containsKey("yearInfoid")) {
			builder.append(" and ci.yearid = :yearInfoid");
		}
		if (condition.containsKey("term")) {
			builder.append(" and ci.term = :term");
		}
		if (condition.containsKey("studyNo")) {
			builder.append(" and ci.studyNo = :studyNo");
		}
		if (condition.containsKey("organization")) {
			builder.append(" and ci.organization = :organization");
		}
		if (condition.containsKey("status")) {
			builder.append(" and ci.status = :status");
		}
		if (condition.containsKey("isCandidate")) {
			builder.append(" and ci.isCandidate = :isCandidate");
		}
		if (condition.containsKey("isAppoint")) {
			builder.append(" and ci.isAppoint = :isAppoint");
		}
		try {
			return baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(builder.toString(),condition);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public CadreInfo getPositionByYear(String studyNo, String yearid) {
		Map<String,Object> condition = new HashMap<String, Object>();
		condition.put("studyNo",studyNo);
		condition.put("yearInfo.resourceid",yearid);
		condition.put("isAppoint","Y");
		condition.put("position-notNull","");
		condition.put("orderBy","term desc");
		List<CadreInfo> cadreInfoList = findByCondition(condition);
		if (cadreInfoList != null && cadreInfoList.size() > 0) {
			return cadreInfoList.get(0);
		}
		return null;
	}

}
