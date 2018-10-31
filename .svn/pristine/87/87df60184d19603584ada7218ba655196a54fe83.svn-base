package com.hnjk.core.foundation;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class BeanWapperTest {

	@Test
	public void getValueTest()throws Exception{
		Child child = new Child();
		child.setResourceid("1");
		child.setSuperid("s1");
		child.setName("hzg");
		child.setAge(20);
		
		BeanWrapper wrapper = new BeanWrapperImpl(child);
		
		Assert.assertEquals("s1", wrapper.getPropertyValue("superid"));
			
		
	}
	
	@Test
	public void setValueTest() throws Exception{
		Child child = new Child();
		BeanWrapper wrapper = new BeanWrapperImpl(child);
		
		wrapper.setPropertyValue("superid", "s1");
		wrapper.setPropertyValue("resourceid","1");
		wrapper.setPropertyValue("name","hzg");
		
		Assert.assertEquals("s1", child.getSuperid());
		Assert.assertEquals("1", child.getResourceid());
		Assert.assertEquals("hzg", child.getName());
	}
}
