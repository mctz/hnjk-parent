package com.hnjk.core.extend.plugin.excel;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import com.hnjk.core.support.context.SpringContextHolder;
import com.hnjk.extend.plugin.excel.service.IExportExcelService;

/**
 * 导出excel测试 <p>
 * 
 * @author： hzg ,
 * @since： 2009-4-30上午11:14:02

 * @see 
 * @version 1.0
 */
public class TestExportExcel2  {
//	 form 测试
	private static List formModelList = new ArrayList();

	// map 测试
	private static List mapModelList = new ArrayList();
	
	// map　成绩
	private static List mapScoreList = new ArrayList();
	
	private static IExportExcelService exportexcel;
	 
	//@BeforeClass
	 public static void setUpBeforeClass() throws Exception {
		exportexcel = (IExportExcelService)SpringContextHolder.getBean("exportExcelService");
		formModelList = StudentUtils.getStudentFormList();
		mapModelList = StudentUtils.getStudentMapList();
		mapScoreList = StudentUtils.getScoreMapList();
	}

	// 按配制文件输出，不改变列头。 传入form list 输出
	//@Test
	public void testFormConfig() {
		String fileName = "D:\\usr\\excelcomponent\\excelfile\\student\\student_FormConfig.xls";
		exportexcel.initParmasByfile(new File(fileName), "importStudentInfo",formModelList,null);
		
		exportexcel.getModelToExcel().setHeader("高二一班(2008)级学生名单");
		exportexcel.getModelToExcel().setRowHeight(500);
		exportexcel.getExcelFile();
	}

	// 改变其中部门或者全部列标题输出文字，传入form list 输出
	//@Test
	public void testFormConfig_title() {
		Map map = new HashMap();
		map.put("className", "班级名称_改变");
		map.put("classCode", "班级编号_改变");
		map.put("studentName", "姓名_改变");
		map.put("sex", "性别_改变");
		map.put("sort", "排序_改变");
		String fileName = "D:\\usr\\excelcomponent\\excelfile\\student\\student_FormConfig_title.xls";
		exportexcel.initParmasByfile(new File(fileName), "importStudentInfo",	formModelList,null);
	
		exportexcel.getModelToExcel().setHeader("高二一班(2008)级学生名单");
		exportexcel.getModelToExcel().setRowHeight(500);
		exportexcel.getModelToExcel().setDynamicTitleMap(map);
		exportexcel.getExcelFile();
	}

	// 按配制文件输出，不改变列头。 传入map list 输出
	//@Test
	public void testMapConfig() {
		String fileName = "D:\\usr\\excelcomponent\\excelfile\\student\\student_MapConfig.xls";
		exportexcel.initParmasByfile(new File(fileName), "importStudentInfo",	mapModelList,null);
		
		exportexcel.getModelToExcel().setHeader("高二一班(2008)级学生名单");
		exportexcel.getModelToExcel().setRowHeight(500);

		exportexcel.getExcelFile();
	}

	// 改变列标题输出文字，传入Map list 输出
	//@Test
	public void testMapConfig_title() {
		Map map = new HashMap();
		map.put("className", "班级名称_改变");
		map.put("classCode", "班级编号_改变");
		map.put("studentName", "姓名_改变");
		map.put("sex", "性别_改变");
		map.put("sort", "排序_改变");

		String fileName = "D:\\usr\\excelcomponent\\excelfile\\student\\student_MapConfig_title.xls";
		exportexcel.initParmasByfile(new File(fileName), "importStudentInfo",mapModelList,null);
		
		exportexcel.getModelToExcel().setHeader("高二一班(2008)级学生名单");
		exportexcel.getModelToExcel().setRowHeight(500);

		exportexcel.getModelToExcel().setDynamicTitleMap(map);

		exportexcel.getExcelFile();
	}

	// 按传入列输出，传入 form list 输出
	//@Test
	public void testFormColumn() {
		Map map = new HashMap();
		map.put("className", "班级名称_定义");
		map.put("classCode", "班级编号_定义");
		map.put("studentName", "姓名_定义");
		map.put("sex", "性别_定义");
		map.put("sort", "排序_定义");

		String fileName = "D:\\usr\\excelcomponent\\excelfile\\student\\student_FormColumn.xls";
		exportexcel.initParmasByfile(new File(fileName), "importStudentInfo",formModelList,null);
	
		exportexcel.getModelToExcel().setHeader("高二一班(2008)级学生名单");
		exportexcel.getModelToExcel().setRowHeight(500);
		exportexcel.getModelToExcel().setDynamicTitle(true);
		exportexcel.getModelToExcel().setDynamicTitleMap(map);

		exportexcel.getExcelFile();
	}

	// 按传入列输出，传入 map list 输出
	//@Test
	public void testMapColumn() {
		Map map = new HashMap();
		map.put("className", "班级名称_定义");
		map.put("classCode", "班级编号_定义");
		map.put("studentName", "姓名_定义");
		map.put("sex", "性别_定义");
		map.put("sort", "排序_定义");

		String fileName = "D:\\usr\\excelcomponent\\excelfile\\student\\student_MapColumn.xls";
		exportexcel.initParmasByfile(new File(fileName),"importStudentInfo",	mapModelList,null);
		
		exportexcel.getModelToExcel().setHeader("高二一班(2008)级学生名单");
		exportexcel.getModelToExcel().setRowHeight(500);

		exportexcel.getModelToExcel().setDynamicTitle(true);
		exportexcel.getModelToExcel().setDynamicTitleMap(map);

		exportexcel.getExcelFile();
	}

	// 加载模板，按配制文件，传入form list 输出
	//@Test
	public void testFormTemplate() {
		Map paramMap = new HashMap();
		paramMap.put("Year", "2007");
		paramMap.put("Name", "hzg");
		paramMap.put("Date", "2008-1-23");

		String fileName = "D:\\usr\\excelcomponent\\excelfile\\student\\student_FormTemplate.xls";
		exportexcel.initParmasByfile(new File(fileName), "importStudentInfo",	formModelList,null);
		
		exportexcel.getModelToExcel().setHeader("高二一班(2008)级学生名单");
		exportexcel.getModelToExcel().setRowHeight(400);

		exportexcel.getModelToExcel().setTemplateParam("D:\\usr\\excelcomponent\\excelfile\\studentModelTemplate.xls", 3,paramMap);
		exportexcel.getExcelFile();
	}

	// 加载模板，按配制文件，传入map list 输出
	//@Test
	public void testMapTemplate() {
		Map paramMap = new HashMap();
		paramMap.put("Year", "2007");
		paramMap.put("Name", "hzg");
		paramMap.put("Date", "2008-1-23");

		String fileName = "D:\\usr\\excelcomponent\\excelfile\\student\\student_MapTemplate.xls";
		exportexcel.initParmasByfile(new File(fileName), "importStudentInfo",	mapModelList,null);
		
		exportexcel.getModelToExcel().setHeader("高二一班(2008)级学生名单");
		exportexcel.getModelToExcel().setRowHeight(500);

		exportexcel.getModelToExcel().setTemplateParam("D:\\usr\\excelcomponent\\excelfile\\studentModelTemplate.xls", 3,	paramMap);
		exportexcel.getExcelFile();
	}

	// 多 Sheet 输出
	//@Test
	public void testMultipleSheet() {

		// modeltoexcel_Multiple.xls做为模板文件传入时，文件必须存在，必须有足够多的sheet
		String fileName = "D:\\usr\\excelcomponent\\excelfile\\student\\student_Multiple.xls";
		
		exportexcel.initParmasByfile(new File(fileName), "importStudentInfo", formModelList,null);
		exportexcel.getModelToExcel().setHeader("高二一班(2008)级学生名单");
		exportexcel.getModelToExcel().setRowHeight(500);
		exportexcel.getModelToExcel().setSheet(0, "第一");
		exportexcel.getExcelFile();	
		
		Map map = new HashMap();
		map.put("className", "班级名称_改变");
		map.put("classCode", "班级编号_改变");
		map.put("studentName", "姓名_改变");
		map.put("sex", "性别_改变");
		map.put("sort", "排序_改变");
		exportexcel.setModelName_List("importStudentInfo", formModelList);
		exportexcel.getModelToExcel().setHeader("高二一班(2008)级学生名单");
		exportexcel.getModelToExcel().setRowHeight(500);
		exportexcel.getModelToExcel().setDynamicTitleMap(map);
		exportexcel.getModelToExcel().setSheet(1, "第二");
		exportexcel.getExcelFile();	
		
		Map paramMap = new HashMap();
		paramMap.put("Year", "2007");
		paramMap.put("Name", "hzg");
		paramMap.put("Date", "2008-1-23");
		exportexcel.setModelName_List("importStudentInfo", mapModelList);
		exportexcel.getModelToExcel().setHeader("高二一班(2008)级学生名单");
		exportexcel.getModelToExcel().setRowHeight(500);
		exportexcel.getModelToExcel().setTemplateParam("D:\\usr\\excelcomponent\\excelfile\\student\\student_Multiple.xls", 3,	paramMap);
		exportexcel.getModelToExcel().setSheet(2, "第三");
		exportexcel.getExcelFile();

	}

	// 设置从每行，第几列开始操作
	//@Test
	public void testRowColumn() {
		
		exportexcel.initParmasByfile(new File("D:\\usr\\excelcomponent\\excelfile\\student\\student_rowcolumn.xls"),"importStudentInfo", mapModelList,null);
		exportexcel.getModelToExcel().setHeader("高二一班(2008)级学生名单");
		exportexcel.getModelToExcel().setRowHeight(500);

		Map paramMap = new HashMap();
		paramMap.put("Year", "2007");
		paramMap.put("Name", "hzg");
		paramMap.put("Date", "2008-1-23");
		exportexcel.getModelToExcel().setTemplateParam("D:\\usr\\excelcomponent\\excelfile\\studentModelTemplate2.xls", 3,1, paramMap, false);
		exportexcel.getExcelFile();
	}

	// 设置开始行，开始列操作，多次导出
	//@Test
	public void testMultipleRowColumn() {
		exportexcel.initParmasByfile(new File("D:\\usr\\excelcomponent\\excelfile\\student\\student_Multiplerowcolumn.xls"),"importStudentInfo", formModelList,null);		
		exportexcel.getModelToExcel().setRowHeight(400);

		Map paramMap = new HashMap();
		paramMap.put("Year", "2007");
		paramMap.put("Name", "hzg");
		paramMap.put("Date", "2008-1-23");
		//第一次操作
		exportexcel.getModelToExcel().setTemplateParam("D:\\usr\\excelcomponent\\excelfile\\studentModelTemplate3.xls", 3,1, paramMap, false);
		exportexcel.getExcelFile();
		
		//第二次操作
		exportexcel.setModelName_List("importStudentInfo_score", mapScoreList);
		exportexcel.getModelToExcel().setTemplateParam("D:\\usr\\excelcomponent\\excelfile\\student\\student_Multiplerowcolumn.xls", 3,12,paramMap,false);		
		exportexcel.getExcelFile();
	}
}
