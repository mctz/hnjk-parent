package com.hnjk.core.foundation;

import java.util.Map;

import org.junit.Test;

import com.hnjk.core.BaseTest;
import com.hnjk.core.dao.ITestService;
import com.hnjk.core.dao.model.Resource;
import com.hnjk.core.foundation.utils.ExBeanUtils;

public class BeanUtilsTest extends BaseTest {

	//测试bean --> map
	@Test
	public void testConvertBeanToMap() throws Exception{
		ITestService testService = (ITestService)springContextForUnitTestHolder.getBean("testService");
		Resource course = (Resource)testService.get(Resource.class, "FBECB1BCE75B171EE030007F01001614");
		Map<String, Object> map = ExBeanUtils.convertBeanToMap(course);
		if(null != map){
			for(String key : map.keySet()){
				System.out.println(key +" - "+ map.get(key));
			}
		}
	}
}
