package com.hnjk.core.rao.dao.jdbc;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * 提供一个基础的JDBC DAO 支持类。 <p>
 * 如果Service层需要使用jdbc方式，可以继承或注入此类.
 * @author： 广东学苑教育发展有限公司.
 * @since： 2009-3-31上午09:12:29
 * @see {@link BaseJdbcTemplate}
 * @version 1.0
 */
@Repository("baseSupportJdbcDao")
public class BaseSupportJdbcDao implements IBaseSupportJdbcDao {

	public BaseJdbcTemplate baseJdbcTemplate;
	
	
	@Autowired
	@Resource(name="dataSource")
	public void setDataSource(DataSource dataSource){
		baseJdbcTemplate = new BaseJdbcTemplate(dataSource);
	}


	@Override
	public BaseJdbcTemplate getBaseJdbcTemplate() {
		return baseJdbcTemplate;
	}
	
	
}
