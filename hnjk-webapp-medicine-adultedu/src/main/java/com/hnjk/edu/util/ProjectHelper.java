package com.hnjk.edu.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.support.context.SpringContextHolder;
import com.hnjk.edu.roll.service.IClassesService;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.OrgUnit;
import com.hnjk.security.model.User;
import com.hnjk.security.service.IUserService;

/** 
 * @author Zik, 广东学苑教育发展有限公司
 * @since Jul 29, 2016 10:05:56 AM 
 * 
 */
public class ProjectHelper {

	/**
	 * 权限数据过滤条件
	 * @param condition
	 * @param user
	 * @return 班主任：classesIds,classesIdList；老师：teacherId；教学点账号：schoolId
	 * @throws Exception 
	 */
	public static Map<String, Object> accessDataFilterCondition(Map<String, Object> condition,User user) throws Exception{
		String teacherCode = CacheAppManager.getSysConfigurationByCode("sysuser.role.teacher").getParamValue();//	课程负责人
		String masterCode = CacheAppManager.getSysConfigurationByCode("sysuser.role.master").getParamValue();//班主任角色对应的角色编码
		if(condition==null) {
			condition = new HashMap<String, Object>();
		}
		if(!SpringSecurityHelper.isUserInRole("ROLE_BRS_STUDENTSTATUS")){
			//OrgUnit unit = user.getOrgUnit();
			//String unitCode = user.getOrgUnit().getUnitCode();
			if(SpringSecurityHelper.isUserInRoles(teacherCode,masterCode,"ROLE_LINE")){
				//boolean flag1 = SpringSecurityHelper.isUserOnlyInRoles(teacherCode);
				//boolean flag2 = "DEPT_TEACHING_CENTRE".equals(user.getOrgUnit().getUnitCode());
				if(SpringSecurityHelper.isUserOnlyInRoles(teacherCode) || "DEPT_TEACHING_CENTRE".equals(user.getOrgUnit().getUnitCode())){// 该用户为老师
					String teacherId = user.getResourceid();
					if(ExStringUtils.isNotBlank(teacherId)){
						condition.put("teacherId",teacherId);
					}
				}else {
					IClassesService classesService = (IClassesService)SpringContextHolder.getBean("classesService");
					String classesIds = classesService.findByMasterId(user.getResourceid());
					if(ExStringUtils.isNotEmpty(classesIds)){
						condition.put("classesIds",classesIds.replaceAll(",", "','"));
						//condition.put("classesIdList",Arrays.asList(classesIds.split(",")));
					}
				}
			}
		}
		if(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())) {
			condition.put("schoolId",user.getOrgUnit().getResourceid());
		}
		return condition;
	}
}
