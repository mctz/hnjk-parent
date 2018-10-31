package com.hnjk.edu.roll.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.ExCollectionUtils;
import com.hnjk.core.foundation.utils.ExNumberUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.recruit.util.ReplaceStr;
import com.hnjk.edu.roll.model.Classes;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.roll.service.IClassesService;
import com.hnjk.edu.roll.service.IStudentInfoService;
import com.hnjk.edu.teaching.service.ILinkageQueryService;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.taglib.JstlCustomFunction;
/**
 * 班级服务接口实现.
 * <code>ClassesServiceImpl</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2013-4-8 上午09:35:47
 * @see 
 * @version 1.0
 */
@Transactional
@Service("classesService")
public class ClassesServiceImpl extends BaseServiceImpl<Classes> implements IClassesService {

	@Autowired 
	@Qualifier("baseSupportJdbcDao")	
	private IBaseSupportJdbcDao jdbcDao;//注入jdbc template 支持
	
	@Autowired
	@Qualifier("studentinfoservice")
	private IStudentInfoService studentInfoService;
	
	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;
	
	@Autowired
	@Qualifier("linkageQueryService")
	private ILinkageQueryService linkageQueryService;
	
	@Override
	public Page findClassesByCondition(Map<String, Object> condition, Page objPage) throws ServiceException {
		Map<String, Object> values = new HashMap<String, Object>();
		StringBuilder hql = getClassesHqlByCondition(condition, values);
		if(objPage.isOrderBySetted()){
			hql.append(" order by ").append(objPage.getOrderBy()).append(" ").append(objPage.getOrder());
		}
		Page page = findByHql(objPage, hql.toString(), values);
		List<Classes> results = page.getResult();
		if (results != null && results.size() > 0) {
			for (Classes result : results) {
				Map<String, Object> parameter = new HashMap<String, Object>(1);
				parameter.put("classesid", result.getResourceid());
				result.setStudentNum(studentInfoService.getStudentNum(parameter));
			}
		}
		return findByHql(objPage, hql.toString(), values);
	}
	
	@Override
	public List<Classes> findClassesByCondition(Map<String, Object> condition) throws ServiceException {
		Map<String, Object> values = new HashMap<String, Object>();
		StringBuilder hql = getClassesHqlByCondition(condition, values);
		if(condition.containsKey("orderBy")){
			hql.append("order by ").append(condition.get("orderBy"));
		}
		return findByHql(hql.toString(), values);
	}
	
	private StringBuilder getClassesHqlByCondition(	Map<String, Object> condition, Map<String, Object> values) {
		StringBuilder hql = new StringBuilder();
		hql.append(" from ").append(Classes.class.getSimpleName()).append(" where isDeleted=:isDeleted ");
		values.put("isDeleted", 0);
		if(condition.containsKey("classname")){
			hql.append(" and classname like :classname ");
			values.put("classname", "%"+condition.get("classname")+"%");
		}
		if(condition.containsKey("classCode")){
			hql.append(" and classCode =:classCode ");
			values.put("classCode", condition.get("classCode"));
		}
		if(condition.containsKey("classid")){
			hql.append(" and resourceid =:classid ");
			values.put("classid", condition.get("classid"));
		} else if (condition.containsKey("classesid")) {
			hql.append(" and resourceid =:classesid ");
			values.put("classesid", condition.get("classesid"));
		}
		if(condition.containsKey("brSchoolid")){
			hql.append(" and brSchool.resourceid =:brSchoolid ");
			values.put("brSchoolid", condition.get("brSchoolid"));
		}
		if(condition.containsKey("gradeid")){
			hql.append(" and grade.resourceid =:gradeid ");
			values.put("gradeid", condition.get("gradeid"));
		}
		if(condition.containsKey("gradeName")){
			hql.append(" and grade.gradeName =:gradeName ");
			values.put("gradeName", condition.get("gradeName"));
		}
		if(condition.containsKey("classicid")){
			hql.append(" and classic.resourceid =:classicid ");
			values.put("classicid", condition.get("classicid"));
		}
		if(condition.containsKey("classicName")){
			hql.append(" and classic.classicName =:classicName ");
			values.put("classicName", condition.get("classicName"));
		}
		if(condition.containsKey("majorid")){
			hql.append(" and major.resourceid =:majorid ");
			values.put("majorid", condition.get("majorid"));
		}
		if(condition.containsKey("majorName")){
			hql.append(" and major.majorName =:majorName ");
			values.put("majorName", condition.get("majorName"));
		}
		if(condition.containsKey("teachingType")){
			hql.append(" and teachingType =:teachingType ");
			values.put("teachingType", condition.get("teachingType"));
		}
		if(condition.containsKey("classesIds")){
			hql.append(" and resourceid in (" + condition.get("classesIds").toString() + ") ");
//			values.put("teachingType", condition.get("teachingType"));
		}
		if(condition.containsKey("hasMemo")){
			hql.append(" and memo is "+("Y".equals(condition.get("hasMemo"))?"not":"")+" null ");
//			values.put("teachingType", condition.get("teachingType"));
		}
		if(condition.containsKey("addSql")){
			hql.append(" "+condition.get("addSql")+" ");
		}
		return hql;
	}

	@Override
	public void batchDelete(String[] ids) throws ServiceException {
		for (String id : ids) {
			Classes classes = get(id);
			Map<String, Object> param = new HashMap<String, Object>(1);
			param.put("classesid", classes.getResourceid());
			int stuNum = studentInfoService.getStudentNum(param);
//			if(classes.getStudentNum()==null || classes.getStudentNum()==0){
			if(stuNum == 0){
				delete(classes);
			} else {
				throw new ServiceException("班级 "+classes.getClassname()+" 人数不为零，无法删除");
			}
		}
	}
	
	@Override
	public void assignStudentClasses(String brSchoolid) throws ServiceException {
		// 学校代码
		String schoolCode = CacheAppManager.getSysConfigurationByCode("graduateData.schoolCode").getParamValue();
		//1.查询未分班的学生
		Map<String, Object> param = new HashMap<String, Object>();
		String hql = "from "+StudentInfo.class.getSimpleName()+" where isDeleted=:isDeleted and classes is null ";
		String hql2 = " from "+Classes.class.getSimpleName()+" where isDeleted=:isDeleted ";
		param.put("isDeleted", 0);
		if(StringUtils.isNotEmpty(brSchoolid)){
			hql += " and branchSchool.resourceid = :brSchoolid ";
			hql2 += " and brSchool.resourceid=:brSchoolid ";
			param.put("brSchoolid", brSchoolid);
		}
		hql += " order by branchSchool,grade,major,classic,teachingType,resourceid desc";
		hql2 += " order by brSchool,classname ";
		List<StudentInfo> studentList = studentInfoService.findByHql(hql,param);
		if(null != studentList && studentList.size() > 0){
			//2.列出已创建班级
			Map<String, Classes> classesMap = new HashMap<String, Classes>();
			List<Classes> classesList = findByHql(hql2, param);			
			if(CollectionUtils.isNotEmpty(classesList)){
				for (Classes c : classesList) {
					String[] key = {c.getBrSchool().getResourceid(),c.getGrade().getResourceid(),c.getMajor().getResourceid(),c.getClassic().getResourceid(),StringUtils.trimToEmpty(c.getTeachingType())};
					try {
						classesMap.put(StringUtils.join(key,"|"), c);
					} catch (Exception e) {
						System.out.println(c.getClassname());
					}
					
				}
			}
			//3.默认分班
			for (StudentInfo stu : studentList) {				
				String[] cl = {stu.getBranchSchool().getResourceid(),stu.getGrade().getResourceid(),stu.getMajor().getResourceid(),stu.getClassic().getResourceid(),
						StringUtils.trimToEmpty(stu.getTeachingType())};
				String ck = StringUtils.join(cl, "|");
				/**
				 * 	默认：
				 * 如果相同年级、相同专业、相同层次、相同学习形式的班级
				 * 在此学习中心不存在则自动生成相应的班级，
				 * 班级名称为年级名称+专业名称+层次简称+学习形式名称+“1班”
				 * 
				 * 广外：
				 * 教学点简称+年级+专业+层次+班级流水号
				 */
				if(!classesMap.containsKey(ck)){
					String majorName = stu.getMajor().getMajorName();
					// 去掉专业后面带[]的内容
					majorName = ReplaceStr.replace(new String[]{"["}, new String[]{"]"}, majorName);
					StringBuilder classesName = new StringBuilder(100);
					if("11846".equals(schoolCode)){
						classesName.append(stu.getBranchSchool().getUnitName())
						.append(stu.getGrade().getGradeName()).append(majorName)
						.append(stu.getClassic().getClassicName()).append("1班");
					} else {
						classesName.append(stu.getGrade().getGradeName())
						.append(majorName).append(stu.getClassic().getShortName())
						.append(JstlCustomFunction.dictionaryCode2Value("CodeTeachingType",stu.getTeachingType()))
						.append("1班");
//						String classname = stu.getGrade().getGradeName()+majorName+stu.getClassic().getShortName()
//						+JstlCustomFunction.dictionaryCode2Value("CodeTeachingType",stu.getTeachingType())+"1班";
					}
					Classes newClasses = new Classes(classesName.toString(),stu.getBranchSchool(), stu.getGrade(), stu.getMajor(), stu.getClassic(), stu.getTeachingType());
					save(newClasses);
					classesMap.put(ck, newClasses);
				}
				//如果可匹配的班级超过1个，则根据班级名称自动匹配第一个班级
				Classes classes = classesMap.get(ck);
				stu.setClasses(classes);//分配班级
			}
		}
	}
	
	@Override
	public void adjustStudentClasses(String[] ids, String classesid) throws ServiceException {		
		if(ids != null && ids.length>0){
			Classes classes = get(classesid);
			for (String id : ids) {
				StudentInfo student = studentInfoService.get(id);
				if(!student.getBranchSchool().getResourceid().equals(classes.getBrSchool().getResourceid())
					|| !student.getGrade().getResourceid().equals(classes.getGrade().getResourceid())	
					|| !student.getMajor().getResourceid().equals(classes.getMajor().getResourceid())
					|| !student.getClassic().getResourceid().equals(classes.getClassic().getResourceid())
					|| !StringUtils.equals(student.getTeachingType(), classes.getTeachingType()) ){
					throw new ServiceException("学生"+student.getStudentName()+"无法加入班级: "+classes.getClassname());
				}
				student.setClasses(classes);
			}
		}
	}
	
	@Override
	public void removeStudentClasses(String[] ids) throws ServiceException {
		if(ids != null && ids.length>0){
			for (String id : ids) {
				StudentInfo student = studentInfoService.get(id);
				student.setClasses(null);
			}
		}
	}
	
	@Override
	public List<Map<String,Object>> getClassesByCondition(Map<String,Object> condition) throws ServiceException{
		StringBuffer sql = new StringBuffer();
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>(0);
		sql.append(" select distinct c.resourceid ,c.classesname ");
		sql.append(" from edu_roll_classes c ");
		sql.append(" where c.isdeleted = 0 "); 
		if(condition.containsKey("unitId")){
			sql.append(" and c.orgunitid= '"+condition.get("unitId")+"' ");
		}
		if(condition.containsKey("majorId")){
			sql.append(" and c.majorid= '"+condition.get("majorId")+"' ");
		}
		if(condition.containsKey("classicId")){
			sql.append(" and c.classicid= '"+condition.get("classicId")+"' ");
		}
		if(condition.containsKey("gradeId")){
			sql.append(" and c.gradeid= '"+condition.get("gradeId")+"' ");
		}
		if(condition.containsKey("classesMasterId")){
			sql.append(" and c.classesMasterId='"+condition.get("classesMasterId")+"' ");
		}
		sql.append(" order by c.resourceid asc ");
		try {
			result = this.baseSupportJdbcDao.getBaseJdbcTemplate().findForList(sql.toString(),null);
		} catch (Exception e) {
			logger.error("获取班级数据异常：{}"+e.fillInStackTrace());
;		}
		return result;
	}

	/**
	 * 获取班级信息（包括教学计划和年级教学计划信息）
	 */
	@Override
	public List<Map<String, Object>> findClassInfo(Map<String, Object> condition)
			throws ServiceException {
		Map<String, Object> param = new HashMap<String, Object>();
		List<Map<String, Object>> classInfoList = new ArrayList<Map<String,Object>>();
		
		try {
			StringBuffer sql = new StringBuffer("");
			sql.append("select cl.resourceid,u.unitname,g.gradename,m.majorname,cl.classesname,cc.classicname,cl.teachingtype, "); 
			sql.append("cl.gradeid,cl.majorid,cl.classicid,cl.orgunitid,gpp.resourceid guiplanid "); 
			sql.append("from edu_roll_classes cl ");
			sql.append("inner join edu_base_grade g on cl.gradeid=g.resourceid and g.isdeleted=0 ");
			sql.append("inner join edu_base_classic cc on cl.classicid=cc.resourceid and cc.isdeleted=0 ");
			sql.append("inner join hnjk_sys_unit u on cl.orgunitid=u.resourceid and u.isdeleted=0 ");
			sql.append("inner join edu_base_major m on cl.majorid=m.resourceid and m.isdeleted=0 ");
			sql.append("left join (select gp.resourceid,p.eduyear,p.schooltype,p.majorid,p.classicid,gp.gradeid "); 
			sql.append("from edu_teach_plan p,edu_teach_guiplan gp ");
			sql.append("where gp.planid=p.resourceid and gp.isdeleted=0 and p.isdeleted=0) gpp ");
			sql.append("on cl.gradeid=gpp.gradeid ");
			sql.append("and cl.majorid=gpp.majorid ");
			sql.append("and cl.classicid=gpp.classicid ");
			sql.append("and cl.teachingtype=gpp.schooltype ");
			sql.append("where cl.isdeleted=0 ");
			
			if(condition.containsKey("classIds")){
				sql.append("and  cl.resourceid in ('"+condition.get("classIds")+"') ");
			}
			if(condition.containsKey("classId")){
				sql.append("and  cl.resourceid =:classId ");
				param.put("classId", condition.get("classId"));
			}
			if(condition.containsKey("gradeId")){
				sql.append("and  g.resourceid =:gradeId ");
				param.put("gradeId", condition.get("gradeId"));
			}

			classInfoList = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql.toString(), param);
		} catch (Exception e) {
			logger.error("获取班级信息出错：", e);
		}
		return classInfoList;
	}

	/**
	 * 根据条件构造成select标签中的option(只供select标签用)
	 * 
	 * @param condition
	 * @param defaultValue
	 * @param hasSelect
	 * @param classesInfo
	 * @return
	 */
	@Override
	public String constructOptions(Map<String, Object> condition, String defaultValue,boolean hasSelect, Map<String, Object> classesInfo) {
		StringBuffer options = new StringBuffer(4000);
		String selAttribute = "";
		if(!hasSelect){// 没有select标签
			options.append("<select ");
			// 构造select标签属性
			selAttribute = linkageQueryService.constructSelectAttribute(classesInfo);
		}
		try {
			List<Classes> classesList = findClassesByCondition(condition);

			StringBuffer buffer = new StringBuffer();
			if(CollectionUtils.isNotEmpty(classesList)){
				for(Classes classes : classesList){
					buffer.append("<option value='"+classes.getResourceid()+"'");
					if(StringUtils.isNotEmpty(defaultValue) && defaultValue.equals(classes.getResourceid())){
						buffer.append(" selected ");
					}
					buffer.append(">"+classes.getClassname()+"</option>");
				}
			}

			options.append(selAttribute+ " >");
			options.append("<option value=''></option>");
			options.append(buffer);
		} catch (Exception e) {
			logger.error("根据条件构造成select标签中的option出错", e);
		}
		return options.toString();
	}
	
	/**
	 * 根据班级ID集合获取名称集合
	 * 
	 * @param ids
	 * @return
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String findNamesByIds(String ids) throws ServiceException {
		String classesNames = "";
		try {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("ids", Arrays.asList(ids.split(",")));
			String sql = "select max(name) classesNames from (select wm_concat(cl.classesname) over (order by cl.classesname) name  from edu_roll_classes cl where cl.resourceid in (:ids)) ";
			Map<String, Object> classesNamesMap = jdbcDao.getBaseJdbcTemplate().findForMap(sql, param);
			if(classesNamesMap!=null && classesNamesMap.size()>0){
				classesNames = (String)classesNamesMap.get("classesNames");
			}
		} catch (Exception e) {
			logger.error("根据班级ID集合获取名称集合出错", e);
		}
		return classesNames;
	}
	
	/**
	 * 获取该用户作为班主任的所有班级
	 * @param userId
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String findByMasterId(String userId) throws Exception{
		String classesIds = "";
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("userId", userId);
		String sql = "select wm_concat(cl.resourceid) classesIds from edu_roll_classes cl where cl.isdeleted=0 and cl.classesmasterid=:userId ";
		Map<String, Object> classesIdsMap = jdbcDao.getBaseJdbcTemplate().findForMap(sql, param);
		if(classesIdsMap != null && classesIdsMap.size() > 0){
			classesIds = (String)classesIdsMap.get("classesIds");
		}
		return classesIds;
	}

	/**
	 * 获取某教学点的所有班级（用“，”连接）
	 * @param schoolId
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String findBySchoolId(String schoolId) {
		String classesIds = "";
		try {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("schoolId", schoolId);
			String sql = "select wm_concat(cl.resourceid) classesIds from edu_roll_classes cl where cl.isdeleted=0 and cl.orgunitid=:schoolId ";
			Map<String, Object> classesIdsMap = jdbcDao.getBaseJdbcTemplate().findForMap(sql, param);
			if(classesIdsMap != null && classesIdsMap.size() > 0){
				classesIds = (String)classesIdsMap.get("classesIds");
			}
		} catch (Exception e) {
			logger.error("获取某教学点的所有班级出错", e);
		}
		return classesIds;
	}

	@Override
	public String getClassCode(String prefix,int bits) {
		// TODO Auto-generated method stub
		String sql = "select ";
		if(bits==0){
			sql += "max(serial)+1";
		}else {
			sql += "lpad(max(serial)+1,"+bits+",'0')";
		}
		sql += " classCode from(select cl.classcode,replace(cl.classcode,'"+prefix+"','') serial";
		sql += " from edu_roll_classes cl where cl.isdeleted=0 and cl.classcode like '"+prefix+"%')";
		String classCode = "";
		try {
			Map<String, Object> classes = jdbcDao.getBaseJdbcTemplate().findForMap(sql, new HashMap<String, Object>());
			classCode = ExStringUtils.toString(classes.get("classCode"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return classCode;
	}
}
