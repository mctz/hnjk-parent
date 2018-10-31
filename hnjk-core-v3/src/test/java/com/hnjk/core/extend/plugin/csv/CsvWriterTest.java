package com.hnjk.core.extend.plugin.csv;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.hnjk.core.BaseTest;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.extend.plugin.csv.CsvWriter;

public class CsvWriterTest extends BaseTest{

	@Test
	public void testWriter() throws Exception{
		long startTime = System.currentTimeMillis();
		try {            
            String csvFilePath = "c:/test.csv";
             CsvWriter wr =new CsvWriter(csvFilePath,',',Charset.forName("GB2312"));           
             String[] title = {"姓名","学号","性别","身份证","民族","	年级","培养层次","专业","学习中心","学籍状态","帐号状态"};
             wr.writeRecord(title);
             for (int i = 0; i < 20000; i++) {
            	 String[] contents = {"禹菁菁\t","201204922023013\t","男\t","440204198510013077\t","汉族","2012春","专升本","工商管理","韶关粤北学习中心","在学","激活"};                    
                 wr.writeRecord(contents);
			}             
             wr.close();             
             System.out.println("total time:"+(System.currentTimeMillis()-startTime)+"ms.");
         } catch (IOException e) {
            e.printStackTrace();
         }
	}
	
	@Test
	public void testQueryWriter() throws Exception{			
		 IBaseSupportJdbcDao jdbcDao = (IBaseSupportJdbcDao)springContextForUnitTestHolder.getBean("baseSupportJdbcDao");
		 StringBuffer sql = new StringBuffer();
		 sql.append("select t.studentname,t.studyno,b.gender, b.certnum,b.nation,g.gradename,")
		 	.append("c.classicname,m.majorname,u.unitname as brschool,t.studentstatus,t.accoutstatus")
		 	.append(" from edu_roll_studentinfo t,edu_base_student b,edu_base_grade g,edu_base_classic c,edu_base_major m,hnjk_sys_unit u")
		 	.append(" where ")
		 .append(" t.studentbaseinfoid=b.resourceid") 
		 .append(" and t.gradeid=g.resourceid")  
		 .append(" and t.classicid=c.resourceid ") 
		 .append(" and t.majorid=m.resourceid ") 
		 .append(" and t.branchschoolid=u.resourceid ") 
		 .append(" and t.studentstatus=:status") 
		 .append( " and t.isdeleted = :isdeleted ");
		 Map<String, Object> parameters = new HashMap<String, Object>();
		 parameters.put("status", "11");
		 parameters.put("isdeleted",0);
		 long statime  = System.currentTimeMillis();
		 
		List<TestStudentInfo> list =  jdbcDao.getBaseJdbcTemplate().findList(sql.toString(), TestStudentInfo.class, parameters);
		
		 String csvFilePath = "c:/test.csv";
         CsvWriter wr =new CsvWriter(csvFilePath,',',Charset.forName("GB2312"));           
         String[] title = {"姓名","学号","性别","身份证","民族","	年级","培养层次","专业","学习中心","学籍状态","帐号状态"};
         wr.writeRecord(title);
         
		if(null != list && !list.isEmpty()){
			for(TestStudentInfo testStudentInfo : list){
				String[] content = testStudentInfo.toArray();
				wr.writeRecord(content);
			}
		}
		 wr.close();
		System.out.println("total time :"+(System.currentTimeMillis()-statime)+" ms.");	 
	}
}
