package com.hnjk.core.dao.jdbc;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.hnjk.core.BaseTest;
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;

public class BaseJdbcSupportDaoTest extends BaseTest{

	
	@Test
	public void testJdbcInsert() throws Exception{
		IBaseSupportJdbcDao jdbcDao = (IBaseSupportJdbcDao)springContextForUnitTestHolder.getBean("baseSupportJdbcDao");
		//清空测试数据
		jdbcDao.getBaseJdbcTemplate().executeForObject("delete from t_hnjk_sys_resources", null);
		
		List<Map<String, Object>> resList = jdbcDao.getBaseJdbcTemplate().findForListMap("select * from hnjk_sys_resources", null);
		//构造插入SQL
		String inserSqlValueSql = ":resourceid,:resourcecode,:parentid,:resourcetype,:resourcename,:resourcepath,:resourcedescript,:showorder,:version,:isdeleted,:resourcelevel,:style,:jsfunction,:ischild";
		String insertSql = "insert into t_hnjk_sys_resources (" +
				"resourceid,resourcecode,parentid,resourcetype,resourcename,resourcepath,resourcedescript," +
				"showorder,version,isdeleted,resourcelevel,style,jsfunction,ischild)" +
				"values("+inserSqlValueSql.toUpperCase()+")";
		int count = 0;
		//批量插入
		for(Map<String, Object> map : resList){
			count += jdbcDao.getBaseJdbcTemplate().executeForMap(insertSql, map);
		}
		System.out.println("执行成功:"+count+"行...");
	}
	
	@Test
	public void testJdbcUpdate() throws Exception{
		IBaseSupportJdbcDao jdbcDao = (IBaseSupportJdbcDao)springContextForUnitTestHolder.getBean("baseSupportJdbcDao");
		Map<String, Object> parameters = new HashMap<String, Object>();
		
		parameters.put("classicid", "EB43B13C75AAF518E030007F0100743D");
		
		String sql = "select st.studyno,en.graduateschool,en.graduateschoolcode,en.graduatemajor,en.graduateid,en.graduatedate ";
		sql += " from edu_roll_studentinfo st,edu_recruit_enrolleeinfo en ";
		sql += " where st.studyno=en.matriculatenoticeno and st.classicid=:classicid";
		
	   List<Map<String,Object>> list = jdbcDao.getBaseJdbcTemplate().findForListMap(sql, parameters);
	   
	   String sql1 = "update edu_roll_studentinfo info " +
	   		" set info.graduateschool=:graduateschool," +
	   		"info.graduateschoolcode=:graduateschoolcode," +
	   		"info.graduatemajor=:graduatemajor," +
	   		"info.graduateid=:graduateid," +
	   		"info.graduatedate=:graduatedate " +
	   		"where info.studyno=:studyno";
	   Map<String, Object> parameters1 = new HashMap<String, Object>();
	   
	   for(Map<String, Object> map : list){
		   parameters1.put("graduateschool", null != map.get("graduateschool") ? map.get("graduateschool").toString(): "" );
		   parameters1.put("graduateschoolcode",null != map.get("graduateschoolcode") ? map.get("graduateschoolcode").toString():"");
		   parameters1.put("graduatemajor", null != map.get("graduatemajor")?map.get("graduatemajor").toString():"");
		   parameters1.put("graduateid", null != map.get("graduateid")?map.get("graduateid").toString():"");
		   parameters1.put("graduatedate", ExStringUtils.isNotEmpty(map.get("graduatedate").toString()) ?  ExDateUtils.convertToDate(map.get("graduatedate").toString()):null);
		   parameters1.put("studyno", null != map.get("studyno") ? map.get("studyno").toString():"");
		  System.out.println("执行结果:"+jdbcDao.getBaseJdbcTemplate().executeForMap(sql1, parameters1));
	   }
	}

	//测试研究生院数据同步
	@Test
	public void testGraduateion() throws Exception{
		IBaseSupportJdbcDao jdbcDao = (IBaseSupportJdbcDao)springContextForUnitTestHolder.getBean("baseSupportJdbcDao");
		
		try {
			//1.对比两边的总人数是否相同	
			long count1  = jdbcDao.getBaseJdbcTemplate().findForLong("select count(resourceid) from hnjk_sys_users2", null);
			long count2  = jdbcDao.getBaseJdbcTemplate().findForLong("select count(resourceid) from hnjk_sys_users", null);
			
			if(count1!=count2){
				//2.同步USER数据
				List<Map<String, Object>> noexistsUserList = jdbcDao.getBaseJdbcTemplate().findForListMap("select resourceid,unitid,username,userpassword,enabled,accoutexpired,accoutlocked,credentialsexpired,to_date(lastlogintime,'yyyy-mm-dd hh24:mi:ss') as lastlogintime," +
								"loginip,showorder,version,isdeleted,cnname,usertype,fromnet,customusername from hnjk_sys_users2  where RESOURCEID " +
								"not in(select resourceid from hnjk_sys_users)", null);
				
				String insertNoexistUserValueSql = ":resourceid,:unitid,:username,:userpassword,:enabled,:accoutexpired,:accoutlocked,:credentialsexpired,:lastlogintime,:loginip,:showorder,:version,:isdeleted,:cnname,:usertype,:fromnet,:customusername";
				String insertNoexistUserSql = "insert into hnjk_sys_users (resourceid,unitid,username,userpassword,enabled,accoutexpired,accoutlocked,credentialsexpired,lastlogintime," +
								"loginip,showorder,version,isdeleted,cnname,usertype,fromnet,customusername) values("+insertNoexistUserValueSql.toUpperCase()+")";
				int count = 0;
				int countArr[] = {};
				int i=0;
				Map[] m = new Map[1];
				if(null != noexistsUserList && !noexistsUserList.isEmpty()){					
					//Map[] maps = new HashMap[noexistsUserList.size()];	//用户表参数
					//Map[] roleUserMaps = new HashMap[noexistsUserList.size()];//用户角色表参数
					List<Map<String, Object>> maps = new ArrayList<Map<String,Object>>();
					List<Map<String, Object>> roleUserMaps = new ArrayList<Map<String,Object>>();
					Map<String,Object> roleUserMap = null;
					for(Map<String, Object> map :noexistsUserList){
						
						//count += jdbcDao.getBaseJdbcTemplate().executeForMap(insertNoexistUserSql, map);
						maps.add(map);
						roleUserMap = new HashMap<String, Object>();
						roleUserMap.put("userid", map.get("RESOURCEID").toString());
						if(map.get("USERTYPE").equals("student")){
							roleUserMap.put("roleid", "402880ed27adfc190127ae0c1ef90002");
						}else{
							roleUserMap.put("roleid", "402893d424ec83f60124ec8422590001");
						}
						roleUserMaps.add(roleUserMap);
						i++;
					}
					
					//批量执行插入用户表					
					countArr = jdbcDao.getBaseJdbcTemplate().getJdbcTemplate().batchUpdate(insertNoexistUserSql, maps.toArray(m));
					//向用户-角色表中插入数据 管理员-4aa663663147335201314b37618806ba,学生-402880ed27adfc190127ae0c1ef90002						
					jdbcDao.getBaseJdbcTemplate().getJdbcTemplate().batchUpdate("insert into hnjk_sys_roleusers (userid,roleid) values (:userid,:roleid)", roleUserMaps.toArray(m));
					maps.clear();
					roleUserMaps.clear();
				}
				
				System.out.println("同步用户数据成功,共同步"+countArr.length+"条...");				
				//3.同步USEREXTEND数据
				List<Map<String, Object>> noextisUserExtendList = jdbcDao.getBaseJdbcTemplate().findForListMap("select * from hnjk_sys_usersextend2 t where t.RESOURCEID not in(select resourceid from hnjk_sys_usersextend)", null);
				insertNoexistUserValueSql = ":resourceid,:sysuserid,:excode,:exvalue,:version,:isdeleted";
				insertNoexistUserSql =  "insert into hnjk_sys_usersextend (resourceid,sysuserid,excode,exvalue,version,isdeleted) values("+insertNoexistUserValueSql.toUpperCase()+")";
				count = 0;
				if(null != noextisUserExtendList && !noextisUserExtendList.isEmpty()){
					List<Map<String, Object>> maps = new ArrayList<Map<String,Object>>();
					
					 i = 0;
					for (Map<String, Object> map : noextisUserExtendList) {
						maps.add(map);
						//count += jdbcDao.getBaseJdbcTemplate().executeForMap(insertNoexistUserSql, map);
						i++;
					}
					countArr = jdbcDao.getBaseJdbcTemplate().getJdbcTemplate().batchUpdate(insertNoexistUserSql,maps.toArray(m));
					maps.clear();
				}
				System.out.println("同步用户扩展数据成功,共同步"+countArr.length+"条...");
			}
		} catch (Exception e) {
			e.printStackTrace();
			
		}
	}

	
	
}
