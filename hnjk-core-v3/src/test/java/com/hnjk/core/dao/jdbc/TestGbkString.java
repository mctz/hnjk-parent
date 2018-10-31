package com.hnjk.core.dao.jdbc;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.hnjk.core.BaseTest;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;

public class TestGbkString  extends BaseTest{

	@Test
	public void test() throws Exception{
		IBaseSupportJdbcDao jdbcDao = (IBaseSupportJdbcDao)springContextForUnitTestHolder.getBean("baseSupportJdbcDao");
		List<Map<String, Object>> list = jdbcDao.getBaseJdbcTemplate().findForList("select studentname from students where studentno in ('201021536190','200920134629')", null);
		for(Map<String, Object> map : list){
			System.out.println(map.get("studentname").toString());
		}
	}
}
