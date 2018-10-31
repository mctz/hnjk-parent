package com.hnjk.core.dao.jdbc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.hnjk.core.BaseTest;
import com.hnjk.core.foundation.utils.GUIDUtils;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;

public class ChangeOrgUnitTest extends BaseTest {
	
	@Test
	public void splitOrgUnitToExtends() throws Exception{
		IBaseSupportJdbcDao jdbcDao = (IBaseSupportJdbcDao)springContextForUnitTestHolder.getBean("baseSupportJdbcDao");
		List<Map<String,Object>> list = jdbcDao.getBaseJdbcTemplate().findForList("select * from t_orgunit_extends", null);
		
		List<Map<String, Object>> newList = new ArrayList<Map<String,Object>>();
		GUIDUtils.init();
		Map<String, Object> parameters;
		for(Map<String, Object> result : list){
			
				parameters = new HashMap<String, Object>();
				parameters.put("resourceid", GUIDUtils.buildMd5GUID(false));
				parameters.put("unitid", result.get("unitid").toString());
				parameters.put("excode", "chargedept");
				parameters.put("exvalue", null !=result.get("chargedept") ? result.get("chargedept").toString() :"");
				parameters.put("version", 0);
				parameters.put("isdeleted", 0);
				newList.add(parameters);
			
				parameters = new HashMap<String, Object>();
				parameters.put("resourceid", GUIDUtils.buildMd5GUID(false));
				parameters.put("unitid", result.get("unitid").toString());
				parameters.put("excode", "telarea");
				parameters.put("exvalue", null != result.get("telarea") ? result.get("telarea").toString():"");
				parameters.put("version", 0);
				parameters.put("isdeleted", 0);
				newList.add(parameters);
				
				parameters = new HashMap<String, Object>();
				parameters.put("resourceid", GUIDUtils.buildMd5GUID(false));
				parameters.put("unitid", result.get("unitid").toString());
				parameters.put("excode", "telephone");
				parameters.put("exvalue",null != result.get("telephone") ? result.get("telephone").toString():"");
				parameters.put("version", 0);
				parameters.put("isdeleted", 0);
				newList.add(parameters);
				
				parameters = new HashMap<String, Object>();
				parameters.put("resourceid", GUIDUtils.buildMd5GUID(false));
				parameters.put("unitid", result.get("unitid").toString());
				parameters.put("excode", "fax");
				parameters.put("exvalue", null != result.get("fax") ? result.get("fax").toString():"");
				parameters.put("version", 0);
				parameters.put("isdeleted", 0);
				newList.add(parameters);
				
				parameters = new HashMap<String, Object>();
				parameters.put("resourceid", GUIDUtils.buildMd5GUID(false));
				parameters.put("unitid", result.get("unitid").toString());
				parameters.put("excode", "schoollevel");
				parameters.put("exvalue", null != result.get("schoollevel") ? (BigDecimal)result.get("schoollevel"):null);
				parameters.put("version", 0);
				parameters.put("isdeleted", 0);
				newList.add(parameters);
			
			
		}
		
		for(Map<String, Object> parameter :newList){
			jdbcDao.getBaseJdbcTemplate().executeForMap("insert into hnjk_sys_unitextend (resourceid,unitid,excode,exvalue,version,isdeleted) " +
					" values(:resourceid,:unitid,:excode,:exvalue,:version,:isdeleted)", parameter);
		}
		
	}
}
