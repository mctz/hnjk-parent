package com.hnjk.core.dao.hibernate;

import java.io.InputStream;

import lombok.Cleanup;
import org.junit.Test;

import com.hnjk.core.BaseTest;
import com.hnjk.core.dao.ITestService;
import com.hnjk.core.dao.model.HelpArticle;
import com.hnjk.core.foundation.utils.FileUtils;

public class ClobORA01461Test extends BaseTest{

	//ORA-01461 错误测试,经测试，没发现网传的clog>1000 and <2000抛错
	@Test
	public void ora01461() throws Exception{
		ITestService testService = (ITestService)springContextForUnitTestHolder.getBean("testService");
		
		//构造clob 字符串
		@Cleanup InputStream in = ClobORA01461Test.class.getResourceAsStream("testchars.txt");
		String str = new String(FileUtils.readFileStream(in));
		System.out.println("clob lenght:"+str.getBytes().length);
		
		HelpArticle article = new HelpArticle();
		article.setTitle("测试CLOG");
		article.setContent(str);
		article.setFillinMan("test");
		article.setChannel("4028819f33149646013314a1e58a0005");
		
		testService.save(article);
	}
	
	
}
