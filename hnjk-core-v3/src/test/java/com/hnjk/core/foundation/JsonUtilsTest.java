package com.hnjk.core.foundation;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.hnjk.core.rao.configuration.json.JsonBinder;
import com.hnjk.core.support.base.model.BaseBizModel;

/**
 * Jackson utils 单元测试.
 * <code>JsonUtilsTest</code><p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2011-1-20 上午11:30:44
 * @see 
 * @version 1.0
 */
public class JsonUtilsTest {

	private static JsonBinder binder = JsonBinder.buildNonDefaultBinder();
	
	//@Test	
	public void toJson() throws Exception{
		//bean to json
		BaseBizModel bizModel = new BaseBizModel();
		bizModel.setFillinman("张三");		
		String beanStr = binder.toJson(bizModel);
		System.out.println("bean to json : "+beanStr);
		assertEquals("{\"fillinman\":\"张三\"}", beanStr);
		
		//list to json
		List<BaseBizModel> list = new ArrayList<BaseBizModel>();
		bizModel = new BaseBizModel();
		bizModel.setFillinman("张三");
		list.add(bizModel);
		
		BaseBizModel bizModel2 = new BaseBizModel();
		bizModel2.setFillinman("李四");
		list.add(bizModel2);
		beanStr = binder.toJson(list) ;
		System.out.println("list benn to json:"+beanStr);
		assertEquals("[{\"fillinman\":\"张三\"},{\"fillinman\":\"李四\"}]", beanStr);
		
		//map to json
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("name", "张三");
		map.put("age", 2);
		String mapString = binder.toJson(map);
		System.out.println("map to json:" + mapString);
		assertEquals("{\"name\":\"张三\",\"age\":2}", mapString);
		
		
		
	}
	
	//@Test
	public void fromJson() throws Exception{
		
	}
	
	
	@Test
	public void testString() throws Exception{
		Set<String> set = new HashSet<String>();
		set.add("ROLE1");
		set.add("ROLE2");
		set.add("ROLE1_1");
		
		System.out.println(Arrays.toString(set.toArray()));
	}
}
