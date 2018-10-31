package com.hnjk.security.service.impl;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.jfree.util.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.cache.EhCacheManager;
import com.hnjk.core.foundation.utils.BaseSecurityCodeUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.BaseSupportJdbcDao;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.security.cache.CacheSecManager;
import com.hnjk.security.model.Resource;
import com.hnjk.security.model.Role;
import com.hnjk.security.model.User;
import com.hnjk.security.model.UserAdaptor;
import com.hnjk.security.service.IUserService;

/**
 * 系统安全权限-用户管理服务. <code>UserServiceImpl</code><p>;
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2009-11-11 下午05:50:57
 * @see 
 * @version 1.0
 */
@Service("userService")
@Transactional
public class UserServiceImpl extends BaseServiceImpl<User> implements IUserService{
	
	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private BaseSupportJdbcDao baseSupportJdbcDao;
	
	/*
	 * 保存用户
	 */	
	@Override
	public User save(User user) throws ServiceException {
		//单向MD5加密，考虑到与2.0密码兼容 by hzg
		//user.setUserPassword(BaseSecurityCodeUtils.getMd5PasswordEncoder(user.getUserPassword(), user.getUserName()));		
		try {
			user.setPassword(BaseSecurityCodeUtils.getMD5(user.getPassword()));
		} catch (NoSuchAlgorithmException e) {			
		}				
		//插入缓存
		EhCacheManager.getCache(CacheSecManager.CACHE_SEC_USERS).put(user.getResourceid(), user);
		exGeneralHibernateDao.save(user);
		return get(user.getResourceid());
	}
		
	
	/*
	 * 更新	
	 */
	@Override
	public void update(User user) throws ServiceException{
		exGeneralHibernateDao.update(user);
		EhCacheManager.getCache(CacheSecManager.CACHE_SEC_USERS).remove(user.getResourceid());
		EhCacheManager.getCache(CacheSecManager.CACHE_SEC_USERS).put(user.getResourceid(), user);
	}
	
	@Override
	public void saveOrUpdate(User user) throws ServiceException{
		exGeneralHibernateDao.saveOrUpdate(user);
		EhCacheManager.getCache(CacheSecManager.CACHE_SEC_USERS).remove(user.getResourceid());
		EhCacheManager.getCache(CacheSecManager.CACHE_SEC_USERS).put(user.getResourceid(), user);
	}
	
	/*
	 * 根据用户登录ID获取用户
	 */
	@Override
	@Transactional(readOnly=true)
	public User getUserByLoginId(String userName) throws ServiceException {		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("username", userName);
		map.put("isDeleted", 0);
		//查用户名
		//User user = (User) exGeneralHibernateDao.findUnique("from "+User.class.getName()+" where username = :username  and isDeleted=:isDeleted",map);
		User user = (User) exGeneralHibernateDao.findUnique("from "+User.class.getName()+" where username = :username ",map);
		if( null != user){
			return user;
		}else{//如果用户名为空，再查昵称
			//user = (User) exGeneralHibernateDao.findUnique("from "+User.class.getName()+" where customUsername = :username  and isDeleted=:isDeleted",map); //or customUsername = :username)
			user = (User) exGeneralHibernateDao.findUnique("from "+User.class.getName()+" where customUsername = :username ",map); //or customUsername = :username)
			return user;
		}		
	}
	
	/*
	 *	判断是否存在该用户账号的用户 	
	 */
	@Override
	@Transactional(readOnly=true)
	public boolean isExistsUser(String userName) throws ServiceException {		
		if(null != getUserByLoginId(userName)){
			return true;
		}
		return false;
	}

	/*
	 * 根据用户登录名获取用户不同级别的授权资源
	 */
	@Override
	@Transactional(readOnly = true)
	public List<Resource> getUserAuthoritys(String username,int resourceLevel) throws ServiceException{
		List<Resource> authsList = new ArrayList<Resource>();
		User user = getUserByLoginId(username);
		for (Role role : user.getRoles()) {//遍历角色
			for(Resource res : role.getAuthoritys()){//遍历角色授权资源	
				if(resourceLevel != 0 && resourceLevel == res.getResourceLevel()){
					authsList.add(res);	
				}							
			}
		}
		return authsList;
	}

	//根据条件查找用户--分页
	@Override
	@Transactional(readOnly = true)
	public Page findUserByCondition(Map<String, Object> condition, Page page) throws ServiceException {
		Criteria objCriterion = findByConditionCriterion(condition);
		return exGeneralHibernateDao.findByCriteriaSession(User.class, page, objCriterion);
	}

	private Criteria findByConditionCriterion(Map<String, Object> condition) {
		Criteria objCriterion = exGeneralHibernateDao.getSessionFactory().getCurrentSession().createCriteria(User.class);
		objCriterion.setFetchMode("userExtends", FetchMode.SELECT);
		if(condition.containsKey("userName")){//用户名
			objCriterion.add(Restrictions.like("username","%"+condition.get("userName")+"%"));
		}
		if(condition.containsKey("cnName")){//资源名称
			objCriterion.add(Restrictions.like("cnName","%"+condition.get("cnName")+"%"));
		}
		if(condition.containsKey("unitId") || condition.containsKey("unitCode") || condition.containsKey("unitType") ){
			objCriterion.createAlias("orgUnit", "ou");
			if(condition.containsKey("unitId")){//组织id
				objCriterion.add(Restrictions.eq("ou.resourceid",condition.get("unitId")));
			}
			if(condition.containsKey("unitCode")){//组织unitCode
				objCriterion.add(Restrictions.eq("ou.unitCode",condition.get("unitCode")));
			}
			if(condition.containsKey("unitType")){//组织类型
				objCriterion.add(Restrictions.eq("ou.unitType",condition.get("unitType")));
			}
			objCriterion.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
		}
		if(condition.containsKey("userType")){//用户类型
			objCriterion.add(Restrictions.eq("userType", condition.get("userType")));
		}
		if(condition.containsKey("isDeleted")){//是否删除 =0
			objCriterion.add(Restrictions.eq("isDeleted", 0));
		}
		if (condition.containsKey("enabled")) {//账号是否禁用
			objCriterion.add(Restrictions.eq("enabled", condition.get("enabled")));
		}
		if (condition.containsKey("userRole")){//查询用户角色
			objCriterion.createAlias("roles", "r");
			objCriterion.add(Restrictions.eq("r.roleCode", condition.get("userRole")));
		}
		if(condition.containsKey("studentunitid")){//某学习中心用户
			objCriterion.add(Restrictions.or(Restrictions.and(Restrictions.or(Restrictions.eq("userType", "student"), Restrictions.isNull("userType")),Restrictions.eq("unitId", condition.get("studentunitid"))),Restrictions.eq("userType", "edumanager")));
		}
		if(condition.containsKey("enableduser")){//账号是否禁用
			objCriterion.add(Restrictions.eq("enabled", "1".equals(condition.get("enableduser")) ?true:false));
		}
		return objCriterion;
	}
		
	/*
	 * 删除
	 */
	@Override
	public void delete(User user) throws ServiceException{
		exGeneralHibernateDao.delete(user);
		//从缓存中移除
		EhCacheManager.getCache(CacheSecManager.CACHE_SEC_USERS).remove(user.getResourceid());
	}

	//批量删除
	@Override
	public void batchCascadeDelete(String[] ids) {
		if(ids.length>0){
			for(int index=0;index<ids.length;index++){
				delete(get(ids[index]));
			}
		}
	}
		

	/*
	 * 用户对象适配，用于工作流	 
	 */
	@Override
	public UserAdaptor getUserAdaptor(String userId) throws ServiceException {
		User user = this.get(userId);
		if(null != user){
			UserAdaptor userAdaptor = new UserAdaptor();
			if(null != user.getRoles()) {
				String[] roleIdArr = new String[user.getRoles().size()];
				int i = 0;
				for(Role role : user.getRoles()) {
					roleIdArr[i++] = role.getResourceid();
				}
				userAdaptor.setRoleIdArr(roleIdArr);
			}
			if(null != user.getOrgUnit()) {
				userAdaptor.setDeptIdArr(new String[] {user.getOrgUnit().getResourceid()});
			}
			
			
			userAdaptor.setId(user.getResourceid());
			userAdaptor.setName(user.getCnName());
			return userAdaptor;
		}
		return null;
	}


	//修改用户密码
	@Override
	public void changedUserPassword(String userId, String oldPwd, String newPwd) throws ServiceException {
		User user = get(userId);
		if(null == user) {
			throw new ServiceException("没有找到用户："+userId);
		}
		try {
			if(!user.getPassword().equals(BaseSecurityCodeUtils.getMD5(oldPwd))){
				throw new ServiceException("旧密码不正确");
			}			
			user.setPassword(BaseSecurityCodeUtils.getMD5(newPwd));
		} catch (NoSuchAlgorithmException e) {
			//noting
		}
		update(user);
		
	}
	@Override
	public void resetUserPassword(String[] resourceids,String newPwd) throws ServiceException{
		for(String resourceid:resourceids){
			User user = get(resourceid);
			if(null==user){
				Log.error("Reset user password but user not found!The userid is:"+resourceid+" ;do nothing and continue.");
				continue;
			}
			try{
				user.setPassword(BaseSecurityCodeUtils.getMD5(newPwd));
			}catch (NoSuchAlgorithmException e) {
				//noting
			}
			update(user);
		}
	}
	//用户角色列表
	@Override
	@Transactional(readOnly=true)
	public List<Role> findUserRoles(String userId) throws ServiceException {
		User user = get(userId);
		if(null == user) {
			throw new ServiceException("没有找到用户："+userId);
		}
		
		return new ArrayList<Role>(user.getRoles());
	}

	/**
	 * 根据用户名集合获取名称集合
	 * 
	 * @param accounts
	 * @return
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String findNamesByAccounts(String accounts) throws ServiceException {
		String userCNNames = "";
		try {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("accounts", Arrays.asList(accounts.split(",")));
			String sql = "select wm_concat(u.cnname) userCNNames from hnjk_sys_users u where u.username in (:accounts) ";
			Map<String, Object> userCNNamesMap = baseSupportJdbcDao.getBaseJdbcTemplate().findForMap(sql, param);
			if(userCNNamesMap!=null && userCNNamesMap.size()>0){
				userCNNames = (String)userCNNamesMap.get("userCNNames");
			}
		} catch (Exception e) {
			logger.error("根据用户名集合获取名称集合出错", e);
		}
		return userCNNames;
	}
	
	/**
	 * 根据用户名称获取不在范围类的用户名称
	 * 
	 * @param accounts
	 * @return
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String findNamesByOutResourceids(String cnname,String unitid) throws ServiceException {
		String userCNNames = "";
		try {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("cnname", cnname);
			param.put("unitid", unitid);
			//字符穿>4000：rtrim(xmlagg(xmlparse(content u.cnname || ',' wellformed) ORDER BY cnname).getclobval(),',') attributes,
			String sql = "select wm_concat(u.cnname) userCNNames from hnjk_sys_users u"
					+ " join hnjk_sys_roleusers ru on ru.userid=u.resourceid join hnjk_sys_roles r on r.resourceid=ru.roleid"
					+ " where u.cnname not in (:cnname) and u.unitid=:unitid"
					+ " and u.isdeleted=0 and r.rolecode in('ROLE_LINE','ROLE_TEACHER_DUTY','ROLE_TEACHER')";
			Map<String, Object> userCNNamesMap = baseSupportJdbcDao.getBaseJdbcTemplate().findForMap(sql, param);
			if(userCNNamesMap!=null && userCNNamesMap.size()>0){
				userCNNames = (String)userCNNamesMap.get("userCNNames");
			}
		} catch (Exception e) {
			logger.error("根据用户名集合获取名称集合出错", e);
		}
		return userCNNames;
	}

	//根据条件查找用户--列表
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Override
	public List<User> findUserByCondition(Map<String, Object> condition) throws ServiceException {
		Criteria objCriterion = findByConditionCriterion(condition);
		return (List<User>)exGeneralHibernateDao.findByCriteriaSession(objCriterion);
	}
	
	/**
	 * 根据条件获取用户列表--分页
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public Page findByCondition(Map<String, Object> condition,Page page) throws ServiceException {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer("from "+User.class.getSimpleName()+" u where u.isDeleted=0 ");
		
		if(condition.containsKey("unitId")){
			hql.append(" and u.orgUnit.resourceid=:unitId ");
			params.put("unitId", condition.get("unitId"));
		}
		if(condition.containsKey("userName")){
			hql.append(" and u.username=:userName ");
			params.put("userName", condition.get("userName"));
		}
		if(condition.containsKey("chinessName")){
			hql.append(" and u.cnName like :chinessName ");
			params.put("chinessName", "%"+condition.get("chinessName")+"%");
		}
		if(condition.containsKey("userType")){
			hql.append(" and u.userType=:userType ");
			params.put("userType", condition.get("userType"));
		}
		if(condition.containsKey("addsql")){
			hql.append(condition.get("addsql"));
		}
		
		return findByHql(page, hql.toString(), params);
	}

	/**
	 * 更新用户在线时长信息
	 * @param sql
	 * @param params
	 * @throws Exception
	 */
	@Override
	public void updateLoginLongInfo(String sql, Object... params) throws Exception {
		baseSupportJdbcDao.getBaseJdbcTemplate().getJdbcTemplate().update(sql, params);
	}

	@Override
	public Page findAppUseConditionByJDBC(Map<String, Object> condition,Page objPage) throws ServiceException{
		StringBuffer sql     = new StringBuffer();
		List<String> values = new ArrayList<String>();
		//Map<String, Object> values = new HashMap<String, Object>();
		sql.append("select u.resourceid,u.terminaltype,u.isusemobileterminal,u.loginversion,su.unitName,bg.gradeName,bc.classicName,so.teachingtype,bm.majorName,rc.classesname,so.studentname"
			+ " from edu_roll_studentinfo so"
			+ " left join hnjk_sys_unit su on so.branchschoolid = su.resourceid and su.isdeleted=0"
			+ " left join edu_base_grade bg on so.gradeid = bg.resourceid and bg.isdeleted=0"
			+ " left join edu_base_classic bc on so.classicid = bc.resourceid and bc.isdeleted=0"
			+ " left join edu_base_major bm on so.majorid = bm.resourceid and bm.isdeleted=0"
			+ " left join edu_roll_classes rc on so.classesid = rc.resourceid and rc.isdeleted=0"
			+ " inner join hnjk_sys_usersextend ue on ue.exvalue = so.resourceid and so.sysuserid = ue.sysuserid and ue.excode = 'defalutrollid' and ue.isdeleted = 0"
			+ " inner join hnjk_sys_users u on u.resourceid = ue.sysuserid "
			+ " where so.isdeleted = 0");
		if (condition.containsKey("brSchoolId")){
			sql.append(" and so.branchschoolid =?");
			values.add(condition.get("brSchoolId").toString());
		}
		if (condition.containsKey("gradeId")){
			sql.append(" and so.gradeid =?");
			values.add(condition.get("gradeId").toString());
		}
		if (condition.containsKey("classicId")){
			sql.append(" and so.classicid =?");
			values.add(condition.get("classicId").toString());
		}
		if (condition.containsKey("teachingType")){
			sql.append(" and so.teachingType =?");
			values.add(condition.get("teachingType").toString());
		}
		if (condition.containsKey("majorId")){
			sql.append(" and so.majorid =?");
			values.add(condition.get("majorId").toString());
		}
		if (condition.containsKey("classesId")){
			sql.append(" and so.classesid =?");
			values.add(condition.get("classesId").toString());
		}
		if (condition.containsKey("terminalType")){
			sql.append(" and u.terminaltype =?");
			values.add(condition.get("terminalType").toString());
		}
		if (condition.containsKey("loginVersion")){
			sql.append(" and u.loginVersion =?");
			values.add(condition.get("loginVersion").toString());
		}
		if (condition.containsKey("isUsemobileTerminal")){
			sql.append(" and u.isusemobileterminal =?");
			values.add(condition.get("isUsemobileTerminal").toString());
		}
		try {
			objPage = baseSupportJdbcDao.getBaseJdbcTemplate().findListMap(objPage, sql.toString(), values.toArray());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return objPage;
	}


	@Override
	public List<Map<String, Object>> findAppUseConditionByCondition(Map<String, Object> condition) {
		StringBuffer sql = new StringBuffer();
		sql.append("select u.resourceid,u.terminaltype,u.isusemobileterminal,u.loginversion,su.unitName,bg.gradeName,bc.classicName,so.teachingtype,bm.majorName,rc.classesname,so.studentname"
				+ " from edu_roll_studentinfo so"
				+ " left join hnjk_sys_unit su on so.branchschoolid = su.resourceid and su.isdeleted=0"
				+ " left join edu_base_grade bg on so.gradeid = bg.resourceid and bg.isdeleted=0"
				+ " left join edu_base_classic bc on so.classicid = bc.resourceid and bc.isdeleted=0"
				+ " left join edu_base_major bm on so.majorid = bm.resourceid and bm.isdeleted=0"
				+ " left join edu_roll_classes rc on so.classesid = rc.resourceid and rc.isdeleted=0"
				+ " inner join hnjk_sys_usersextend ue on ue.exvalue = so.resourceid and so.sysuserid = ue.sysuserid and ue.excode = 'defalutrollid' and ue.isdeleted = 0"
				+ " inner join hnjk_sys_users u on u.resourceid = ue.sysuserid "
				+ " where so.isdeleted = 0");
		
		List<Object> values = new ArrayList<Object>();
		if (condition.containsKey("ids")){
			sql.append(" and u.resourceid in ("+condition.get("ids")+")");
		}
		if (condition.containsKey("brSchoolId")){
			sql.append(" and so.branchschoolid =?");
			values.add(condition.get("brSchoolId").toString());
		}
		if (condition.containsKey("gradeId")){
			sql.append(" and so.gradeid =?");
			values.add(condition.get("gradeId").toString());
		}
		if (condition.containsKey("classicId")){
			sql.append(" and so.classicid =?");
			values.add(condition.get("classicId").toString());
		}
		if (condition.containsKey("teachingType")){
			sql.append(" and so.teachingType =?");
			values.add(condition.get("teachingType").toString());
		}
		if (condition.containsKey("majorId")){
			sql.append(" and so.majorid =?");
			values.add(condition.get("majorId").toString());
		}
		if (condition.containsKey("classesId")){
			sql.append(" and so.classesid =?");
			values.add(condition.get("classesId").toString());
		}
		if (condition.containsKey("terminalType")){
			sql.append(" and u.terminaltype =?");
			values.add(condition.get("terminalType").toString());
		}
		if (condition.containsKey("loginVersion")){
			sql.append(" and u.loginVersion =?");
			values.add(condition.get("loginVersion").toString());
		}
		if (condition.containsKey("isUsemobileTerminal")){
			sql.append(" and u.isusemobileterminal =?");
			values.add(condition.get("isUsemobileTerminal").toString());
		}
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		try {
			list = baseSupportJdbcDao.getBaseJdbcTemplate().findForList(sql.toString(), values.toArray());
		} catch (Exception e) {
			logger.error("查询app使用情况列表出错:{}", e.fillInStackTrace());
		}
		return list;
	}
}
