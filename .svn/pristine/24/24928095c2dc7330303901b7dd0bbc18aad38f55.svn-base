package com.hnjk.core.extend.plugin.dbf;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.junit.Test;

import com.hnjk.extend.plugin.dbf.DBFField;
import com.hnjk.extend.plugin.dbf.DBFReader;
import com.hnjk.extend.plugin.dbf.DBFWriter;


public class JavaDBFTest {

	@Test
	public void testReadDbf() throws Exception{
		FileInputStream fis = new FileInputStream( "c:\\test.dbf");
		DBFReader reader = new DBFReader( fis);
		reader.setCharactersetName("GB2312");
		int recordCount = reader.getRecordCount();
		System.out.println("total records:"+recordCount);
		try {
			//获取字段属性名
			//for(int i=0;i<recordCount;i++){
			//	DBFField field = reader.getField(i);
			//	System.out.print(field.getName()+"\t");
			//}
			//遍历行
			Object[] rowsValues;
			while((rowsValues = reader.nextRecord())!= null){
				for(int j = 0;j<rowsValues.length;j++){
					System.out.println(rowsValues[j]);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{		
			fis.close();
		}
		
		
	}
	
	@Test
	public void testWriteDbf() throws Exception{
		OutputStream fos = null;
		try {
			DBFField[] fields = new DBFField[3]; 
			fields[0] = new DBFField();   
			fields[0].setName("studentNo");   			  
		    fields[0].setDataType(DBFField.FIELD_TYPE_C);  		  
		    fields[0].setFieldLength(12);
		    
		    fields[1] = new DBFField();   
			fields[1].setName("stuName");   			  
		    fields[1].setDataType(DBFField.FIELD_TYPE_C);  		  
		    fields[1].setFieldLength(50);
		    
		    fields[2] = new DBFField();   
			fields[2].setName("stuFee");   			  
		    fields[2].setDataType(DBFField.FIELD_TYPE_N);  		  
		    fields[2].setFieldLength(12);
		    fields[2].setDecimalCount(2);//小数两位
		    
		    DBFWriter writer = new DBFWriter();
		    writer.setCharactersetName("GB2312");
		    writer.setFields(fields);
		    
		    Object[] value = new Object[3];
		    value[0] = "20110235654";
		    value[1] = "张三";
		    value[2] = new Double(3563.20);
		    writer.addRecord(value);
		    
		    value = new Object[3];
		    value[0] = "20110235623";
		    value[1] = "李四";
		    value[2] = new Double(4820.00);
		    writer.addRecord(value);
		    
		    value = new Object[3];
		    value[0] = "20110235689";
		    value[1] = "王五";
		    value[2] = new Double(3568.11);
		    writer.addRecord(value);
		    fos = new FileOutputStream("c:\\test1.dbf");
		    writer.write( fos);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			fos.close();
		}
		 
	}
	
}
