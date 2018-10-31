package com.hnjk.edu.work.service.impl;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.work.model.Appraising;
import com.hnjk.edu.work.service.IAppraisingService;
import com.hnjk.platform.system.model.UserOperationLogs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Function : 学生评优信息 - 接口实现类
 * <p>Author : msl
 * <p>Date   : 2018-07-24
 * <p>Description : AppraisingServiceImpl
 */
@Transactional
@Service("appraisingService")
public class AppraisingServiceImpl extends BaseServiceImpl<Appraising> implements IAppraisingService {

	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;

	@Override
	public int operateAppraisingInfo(String operatingType, Map<String, Object> condition) throws Exception {
		int count = 0;
		String reids = ExStringUtils.toString(condition.get("resourceids"));
		StringBuilder builder = new StringBuilder( "update edu_work_appraising a");
		String auditStatus = "W";
		String org_auditStatus = "W";
		Map<String,Object> values = new HashMap<String, Object>();
		if("pass".equals(operatingType)){
			auditStatus = "R";//改为待复审
		} else if("notpass".equals(operatingType)) {
			auditStatus = "N";//改为不通过
		} else if ("recheck".equals(operatingType)) {
			auditStatus = "Y";//改为通过
			org_auditStatus = "R";
		}
		if ("delete".equalsIgnoreCase(operatingType)) {
			builder.append(" set a.isdeleted=1 ");
		}else {
			values.put("auditStatus", auditStatus);
			builder.append(" set a.auditStatus = :auditStatus ");
		}
		builder.append(" where a.isdeleted=0 and a.auditStatus='"+org_auditStatus+"'");
		if (ExStringUtils.isNotEmpty(reids)) {
			String[] resourceid = ExStringUtils.trimToEmpty(reids).split("\\,");
			builder.append(" and a.resourceid in(" + ExStringUtils.addSymbol(resourceid, "'", "'") + ")");
		} else {
			if (condition.containsKey("yearInfo.resourceid")) {
				builder.append(" and a.yearid = :yearInfoid");
				values.put("yearInfoid",condition.get("yearInfo.resourceid"));
			}
			if (condition.containsKey("studyNo-like")) {
				builder.append(" and a.studyNo = :studyNo");
				values.put("studyNo",condition.get("studyNo-like"));
			}
			if (condition.containsKey("studentName-like")) {
				builder.append(" and a.studentName = :studentName");
				values.put("studentName",condition.get("studentName-like"));
			}
			if (condition.containsKey("type")) {
				builder.append(" and a.type = :type");
				values.put("type",condition.get("type"));
			}
			if (condition.containsKey("avgScore-begin")) {
				builder.append(" and a.avgScore >= :avgScore");
				values.put("avgScore",condition.get("avgScore-begin"));
			}

			//查询与学籍有关的信息
			builder.append(" and a.studentid in(select si.resourceid from edu_roll_studentinfo si where si.isdeleted=0");
			if (condition.containsKey("studentInfo.branchSchool.resourceid")) {
				builder.append(" and si.branchschoolid = :brSchoolId");
				values.put("brSchoolId",condition.get("studentInfo.branchSchool.resourceid"));
			}
			if(condition.containsKey("studentInfo.grade.resourceid")){
				builder.append(" and si.gradeId = :gradeId");
				values.put("gradeId",condition.get("studentInfo.grade.resourceid"));
			}
			if(condition.containsKey("studentInfo.classes.resourceid")){
				builder.append(" and si.classesId = :classesId");
				values.put("classesId",condition.get("studentInfo.classes.resourceid"));
			}
			builder.append(")");
		}

		return baseSupportJdbcDao.getBaseJdbcTemplate().executeForMap(builder.toString(), values);
	}

	@Override
	public List<Map<String, Object>> getApplicationFormInfo(Map<String, Object> condition) {
		StringBuilder builder = new StringBuilder();
		Map<String,Object> values = new HashMap<String, Object>();
		builder.append("select u.UNITNAME,u.UNITSHORTNAME,g.GRADENAME,bs.GENDER,cl.CLASSCODE,cl.CLASSESNAME,bs.MOBILE,pos.JOBTIME,pos.POSITION,pos.POSITION_CURRENT,ap.* ");
		builder.append(" from EDU_WORK_APPRAISING ap");
		builder.append(" join EDU_ROLL_STUDENTINFO si on si.resourceid=ap.studentid");
		builder.append(" join EDU_BASE_STUDENT bs on bs.resourceid=si.studentbaseinfoid");
		builder.append(" join HNJK_SYS_UNIT u on u.resourceid=si.branchschoolid");
		builder.append(" join EDU_BASE_GRADE g on g.resourceid=si.gradeid");
		builder.append(" join EDU_ROLL_CLASSES cl on cl.resourceid=si.classesid");
		//连接查询申请职位信息
		builder.append(" left join (select ca.*,row_number() over(partition by ca.studentid,ca.yearid order by ca.term desc) rn");
		builder.append(" from EDU_WORK_CADREINFO ca)pos on pos.STUDENTID=ap.STUDENTID and pos.YEARID=ap.YEARID and pos.rn=1");
		builder.append(" where ap.isdeleted=0 and si.isdeleted=0");
		if (condition.containsKey("branchSchoolid")) {
			builder.append(" and u.resourceid = :branchSchoolid");
			values.put("branchSchoolid",condition.get("branchSchoolid"));
		}
		if (condition.containsKey("yearInfoid")) {
			builder.append(" and ap.yearid = :yearid");
			values.put("yearid",condition.get("yearInfoid"));
		}
		if (condition.containsKey("gradeid")) {
			builder.append(" and g.resourceid = :gradeid");
			values.put("gradeid",condition.get("gradeid"));
		}
		if (condition.containsKey("classesid")) {
			builder.append(" and cl.resourceid = :classesid");
			values.put("classesid",condition.get("classesid"));
		}
		if (condition.containsKey("type")) {
			builder.append(" and ap.type = :type");
			values.put("type",condition.get("type"));
		}
		if (condition.containsKey("auditStatus")) {
			builder.append(" and ap.auditStatus = :auditStatus");
			values.put("auditStatus",condition.get("auditStatus"));
		}
		if (condition.containsKey("avgScore-begin")) {
			builder.append(" and ap.avgScore >= :avgScore");
			values.put("avgScore",condition.get("avgScore-begin"));
		} else if (condition.containsKey("avgScore")) {
			builder.append(" and ap.avgScore >= :avgScore");
			values.put("avgScore",condition.get("avgScore"));
		}
		if (condition.containsKey("resourceids")) {
			builder.append(" and ap.resourceid in ("+ExStringUtils.addSymbol(condition.get("resourceids").toString().split(","),"'","'")+")");
		}
		List<Map<String,Object>> result = new ArrayList<Map<String, Object>>();
		try {
			result = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(builder.toString(),values);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
