package com.hnjk.core.extend.plugin.excel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import com.hnjk.core.support.context.SpringContextHolder;
import com.hnjk.extend.plugin.excel.service.IExportExcelService;

/**
 * 数据导入测试 <p>
 * 
 * @author： hzg ,
 * @since： 2009-4-29下午06:40:36

 * @see 
 * @version 1.0
 */

public class TestExportExcel {
	 //form　测试
    private static List formModelList = new ArrayList(); 
    //map 测试
    private static List mapModelList =  new ArrayList();
    
    private static IExportExcelService exportexcel;
    
   // @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    	exportexcel = (IExportExcelService)SpringContextHolder.getBean("exportExcelService");
    	 //　form　测试
    	formModelList.clear();
        for(int i=0;i<68;i++){
          DeptModel dept = new DeptModel();
          dept.setDeptName("总部");
          dept.setDeptCode("A10" + i);
          dept.setDeptNo(i);
          dept.setReceiveFileName("12345678");
          dept.setSendFileName("交南发");
          formModelList.add(dept);
        }
        //map 测试
        mapModelList.clear();
        for(int i=0;i<68;i++){
            Map m = new HashMap();
            m.put("deptName", "总部");
            m.put("deptCode", "A10"+i);
            m.put("receiveFileName", "1234567");
            m.put("deptNo", i);
            m.put("sendFileName", "市工商发");
            mapModelList.add(m);
        }
    }
        
    //按配制文件输出，不改变列头。　传入form　list 输出
   // @Test
    public void testFormConfig(){
        String fileName = "D:\\usr\\excelcomponent\\excelfile\\modeltoexcel_FormConfig.xls";      
        try {
        	exportexcel.initParmasByStream(new FileOutputStream(new File(fileName)),"deptModel",formModelList,null);           
        } catch (FileNotFoundException e) {     
            e.printStackTrace();
        }
        exportexcel.getModelToExcel().setHeader("部门发文简称（2007）");//标题
        exportexcel.getModelToExcel().setRowHeight(500);//行高
        exportexcel.getExcelFile();//获取目标文件
    }
    
    //改变其中部门或者全部列标题输出文字，传入form list 输出
    //@SuppressWarnings("unchecked")
   // @Test
	public void testFormConfig_title(){
        Map map = new HashMap();
        map.put("deptName", "部门名称_改变");
        map.put("deptCode", "部门编号_改变");
        map.put("deptNo", "排序_改变"); 
        String fileName = "D:\\usr\\excelcomponent\\excelfile\\modeltoexcel_FormConfig_title.xls";
        exportexcel.initParmasByfile(new File(fileName),"deptModel",formModelList,null);
        
        exportexcel.getModelToExcel().setHeader("部门发文简称（2007）");
        exportexcel.getModelToExcel().setRowHeight(500);
        exportexcel.getModelToExcel().setDynamicTitleMap(map);
        exportexcel.getExcelFile();
    }
    
    //按配制文件输出，不改变列头。　传入map　list 输出
    //@Test
    public void testMapConfig(){
        String fileName = "D:\\usr\\excelcomponent\\excelfile\\modeltoexcel_MapConfig.xls";
        exportexcel.initParmasByfile(new File(fileName),"deptModel",mapModelList,null);
      
        exportexcel.getModelToExcel().setHeader("部门发文简称（2007）");
        exportexcel.getModelToExcel().setRowHeight(500);
        
        exportexcel.getExcelFile();
    }
    
    //改变列标题输出文字，传入Map list 输出
    //@Test
    public void testMapConfig_title(){
        Map map = new HashMap();
        map.put("deptName", "部门名称_改变");
        map.put("deptCode", "部门编号_改变");
        map.put("deptNo", "排序_改变"); 
        
        String fileName ="D:\\usr\\excelcomponent\\excelfile\\modeltoexcel_MapConfig_title.xls";
        exportexcel.initParmasByfile(new File(fileName),"deptModel",mapModelList,null);
        exportexcel.getModelToExcel().setHeader("部门发文简称（2007）");
        exportexcel.getModelToExcel().setRowHeight(500);
        
        exportexcel.getModelToExcel().setDynamicTitleMap(map);
        
        exportexcel.getExcelFile();
    }
    
    //按传入列输出，传入 form list 输出
   // @Test
    public void testFormColumn(){
        Map map = new HashMap();
        map.put("deptName", "部门名称_改变");
        map.put("deptCode", "部门编号_改变");
        map.put("deptNo", "排序_改变");  
        
        String fileName ="D:\\usr\\excelcomponent\\excelfile\\modeltoexcel_FormColumn.xls";
        
        exportexcel.initParmasByfile(new File(fileName),"deptModel",formModelList,null);
       
        exportexcel.getModelToExcel().setHeader("部门发文简称（2007）");
        exportexcel.getModelToExcel().setRowHeight(500);
        
        exportexcel.getModelToExcel().setDynamicTitle(true);
        exportexcel.getModelToExcel().setDynamicTitleMap(map);
        
        exportexcel.getExcelFile();
    }
    
    //按传入列输出，传入 map list 输出
    //@Test
    public void testMapColumn(){
        Map map = new HashMap();
        map.put("deptName", "部门名称_改变");
        map.put("deptCode", "部门编号_改变");
        map.put("deptNo", "排序_改变");  
        
        String fileName ="D:\\usr\\excelcomponent\\excelfile\\modeltoexcel_MapColumn.xls";
        exportexcel.initParmasByfile(new File(fileName),"deptModel",mapModelList,null);
       
        exportexcel.getModelToExcel().setHeader("部门发文简称（2007）");
        exportexcel.getModelToExcel().setRowHeight(500);
        
        exportexcel.getModelToExcel().setDynamicTitle(true);
        exportexcel.getModelToExcel().setDynamicTitleMap(map);
   
        
        exportexcel.getExcelFile();
    }
    
    //加载模板，按配制文件，传入form list 输出
    //@Test
    public void testFormTemplate(){
        Map paramMap = new HashMap();
        paramMap.put("Name", "hzg");
        paramMap.put("Date", "2008-1-23");
        
        String fileName ="D:\\usr\\excelcomponent\\excelfile\\modeltoexcel_FormTemplate.xls";
        exportexcel.initParmasByfile(new File(fileName),"deptModel",formModelList,null);

        exportexcel.getModelToExcel().setHeader("部门发文简称（2007）");
        exportexcel.getModelToExcel().setRowHeight(500);
        
        exportexcel.getModelToExcel().setTemplateParam("D:\\usr\\excelcomponent\\excelfile\\deptModelTemplate.xls", 3, paramMap);        
        exportexcel.getExcelFile();
    }
    
    //加载模板，按配制文件，传入map list 输出
   // @Test
    public void testMapTemplate(){
        Map paramMap = new HashMap();
        paramMap.put("Name", "hzg");
        paramMap.put("Date", "2008-1-23");
        
        String fileName ="D:\\usr\\excelcomponent\\excelfile\\modeltoexcel_MapTemplate.xls";
        exportexcel.initParmasByfile(new File(fileName),"deptModel",mapModelList,null);
     
        exportexcel.getModelToExcel().setHeader("部门发文简称（2007）");
        exportexcel.getModelToExcel().setRowHeight(500);
        
        exportexcel.getModelToExcel().setTemplateParam("D:\\usr\\excelcomponent\\excelfile\\deptModelTemplate.xls", 3, paramMap);        
        exportexcel.getExcelFile();
    }
    
    //多 Sheet 输出
   // @Test
    public void testMultiple(){
        
        //modeltoexcel_Multiple.xls做为模板文件传入时，文件必须存在，必须有足够多的sheet
        String fileName = "D:\\usr\\excelcomponent\\excelfile\\modeltoexcel_Multiple.xls";
        exportexcel.initParmasByfile(new File(fileName),"deptModel",formModelList,null);
        
        exportexcel.getModelToExcel().setHeader("部门发文简称（2007）");
        exportexcel.getModelToExcel().setRowHeight(500);
        exportexcel.getModelToExcel().setSheet(0, "第一");
        exportexcel.getExcelFile();
        
        
//      String fileName = "D:\\work\\workspace\\excelfile\\modeltoexcel_FormConfig_title.xls";
        //ModelManager mm = new ModelManager(fileName,"deptModel",formModelList);
        Map map = new HashMap();
        map.put("deptName", "部门名称_改变");
        map.put("deptCode", "部门编号_改变");
        map.put("deptNo", "排序_改变"); 
        
        exportexcel.setModelName_List("deptModel",formModelList);
        exportexcel.getModelToExcel().setHeader("部门发文简称（2007）");
        exportexcel.getModelToExcel().setRowHeight(500);
        exportexcel.getModelToExcel().setDynamicTitleMap(map);
        
        exportexcel.getModelToExcel().setSheet(1, "第二");
        exportexcel.getExcelFile();
        
        //ModelManager mm = new ModelManager(fileName,"deptModel",mapModelList);
        Map paramMap = new HashMap();
        paramMap.put("Name", "hzg");
        paramMap.put("Date", "2008-1-23");
        exportexcel.setModelName_List("deptModel",mapModelList);
        exportexcel.getModelToExcel().setHeader("部门发文简称（2007）");
        exportexcel.getModelToExcel().setRowHeight(500);
        
        exportexcel.getModelToExcel().setTemplateParam(fileName, 3, paramMap); 
        exportexcel.getModelToExcel().setSheet(3, "");
        exportexcel.getExcelFile();
        
    }
    
    //设置从每行，第几列开始操作
    //@Test    
    public void testRowColumn(){
    	exportexcel.initParmasByfile(new File("D:\\usr\\excelcomponent\\excelfile\\modeltoexcel_rowcolumn.xls"),"deptModel",mapModelList,null);
    	exportexcel.getModelToExcel().setHeader("部门发文简称（2007）");
    	exportexcel.getModelToExcel().setRowHeight(500);
        
        Map paramMap = new HashMap();
        paramMap.put("Name", "hzg");
        paramMap.put("Date", "2008-1-23");
        
        //mm.getModelToExcel().setTemplateParam("D:\\work\\workspace\\excelfile\\deptModelTemplate.xls", 3, paramMap);
        exportexcel.getModelToExcel().setTemplateParam("D:\\work\\workspace\\excelfile\\deptModelTemplate.xls", 3,2, paramMap,false);
        exportexcel.getExcelFile();
    }
   

}
