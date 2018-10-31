package com.hnjk.core.dao.jdbc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.hnjk.core.BaseTest;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;

public class ClearExamData extends BaseTest{

	@Test
	public void clearData() throws Exception{
		IBaseSupportJdbcDao jdbcDao = (IBaseSupportJdbcDao)springContextForUnitTestHolder.getBean("baseSupportJdbcDao");
		//1.清空试卷表
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("isdeleted", 1);
		List<String> deleteIds = new ArrayList<String>();
		deleteIds = jdbcDao.getBaseJdbcTemplate().findList("select resourceid from EDU_LEAR_EXPAPERDETAILS where isdelete = :isdeleted", null, param);
		
	
		
		//2.清空考试状态
		
	}
}
