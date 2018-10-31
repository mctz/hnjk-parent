package com.hnjk.core.extend.plugin.csv;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.hnjk.extend.plugin.csv.CsvReader;

public class CsvReaderTest{

	@Test
	public void testReader() throws Exception{
		//from file
		String filePath = "c:\\test1.csv";
		CsvReader reader = new CsvReader(new InputStreamReader(new FileInputStream(filePath), Charset.forName("UTF-8")));		
	
		List<Object[]> datas = new ArrayList<Object[]>();
		while(reader.readRecord()){
			datas.add(reader.getValues());
		}
		System.out.println("total record:"+datas.size());
		reader.close();
		//from string
		reader = CsvReader.parse("姓名,学号,性别,身份证,民族,年级,培养层次,专业,学习中心,学籍状态,帐号状态\r\n" +
				"叶海城,201015413394010,男,441523199006167019,汉族,2010秋,高中起点专科,计算机科学与技术（软件技术方向）B,广大,在学,停用");
		
		
		System.out.println("can heders:"+reader.readHeaders()+" - headercount :"+reader.getHeaderCount());
		System.out.println(reader.getRawRecord());
		reader.close();
	}
	
	public void testReaderToDatabase() throws Exception{
		
	}
}
