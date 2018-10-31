package com.hnjk.core.dao.hibernate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.hnjk.core.BaseTest;
import com.hnjk.core.dao.ITestService;
import com.hnjk.core.dao.model.HelpArticle;
import com.hnjk.core.rao.dao.helper.Page;

public class BaseLuceneTemplateTest extends BaseTest{

	@Test
	public void testCreateIndex() throws Exception{
		ITestService testService = (ITestService)springContextForUnitTestHolder.getBean("testService");
		long starTime = System.currentTimeMillis();
		int num = testService.createInexe(HelpArticle.class);
		System.out.println("创建索引："+num+"条,耗时："+(System.currentTimeMillis()-starTime)+"ms.");
	}
	
		
	@Test
	public void testFindPageByFullText() throws Exception{
		ITestService testService = (ITestService)springContextForUnitTestHolder.getBean("testService");
		Page p = new Page();
		p.setAutoCount(true);
		p.setPageSize(200);
		
		String[] fields = new String[]{"title","tags","content","fillinDate"};
		Map<String, Object> queryParaMap = new HashMap<String, Object>();
		queryParaMap.put("isDeleted", 0);
		
		long startTime = System.currentTimeMillis();
		
		Page page = testService.findArticleByFullText(p, fields, "隔", HelpArticle.class, queryParaMap);
		
		System.out.println("total time :"+(System.currentTimeMillis()-startTime)+" ms.");
		
		if(null != page){
			System.out.println("total search list size:"+page.getTotalCount());
			List list = page.getResult();
			System.out.println("current page size:"+list.size());
			for(int i=0;i<list.size();i++){
				HelpArticle article = (HelpArticle)list.get(i);
				System.out.println(article.getTitle()+"\t"+article.getFillinMan()+"\t"+article.getFillinDate());
			}
		}
	}
}
