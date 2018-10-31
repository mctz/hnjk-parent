package com.hnjk.edu.util;

import java.util.List;

import lombok.Cleanup;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.hnjk.core.foundation.utils.FileUtils;

public class ExcelUtil {
	// test 对应 flag
	// test1 对应flag1
	public static final String filepath = "E:\\test1.xlsx";
	
	public static final String exportFilepath = "E:\\test1.sql";
	
	public static final String flag = "（本）";
	
	public static final String flag1 = "（专）";
	
	public static final int startRow = 2;//开始行
	
	public static final int startcol = 2;//开始列
	
	public static final int endcol = 7;//结束列
	
	//列数要与title 对应，不然会报错的
	public static final	String[] title = {"courseName","bookserial","bookname","press","editor","price"};
	
	public static List<Map<String,Object>> readExcel(){
		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		
		Workbook wb = null;

		do{
			try {
				@Cleanup InputStream inputStream = new FileInputStream(filepath);
				wb= WorkbookFactory.create(inputStream);
				int sheetCount = wb.getNumberOfSheets();
				if(sheetCount==0){
					System.out.println("文件表格为空！");
					break;
				}
				for(int sheetIndex=0;sheetIndex<sheetCount;sheetIndex++){
					Sheet sheet =(Sheet) wb.getSheetAt(sheetIndex);
					for(int rowIndex = startRow;rowIndex<sheet.getLastRowNum();rowIndex++){
						Row row = sheet.getRow(rowIndex);
						if(row==null){
							break;
						}
						Map<String, Object> map = new HashMap<String, Object>();
						for(int colIndex =startcol;colIndex<=endcol;colIndex++){
							Cell cell = row.getCell(colIndex);							
							if(cell==null){
								break;//遇到有空格，就直接退出这一行的循环
							}
							String cellValue="";
							switch (cell.getCellType()) {
							case Cell.CELL_TYPE_NUMERIC://数字类型
								cellValue=String.valueOf(cell.getNumericCellValue()).trim();
								map.put(title[colIndex-startcol], cellValue);
								break;
							case Cell.CELL_TYPE_STRING :
								cellValue = verifyCell(cell);
								map.put(title[colIndex-startcol], cellValue);
								break;
							default:
								break;
							}
						}
						if(!resultList.contains(map)&&!map.isEmpty()){//去重
							resultList.add(map);
						}
//						resultList.add(map);
						
					}					
				}
			}catch(Exception e){
				e.fillInStackTrace();
			}
		}while(false);
		
		return resultList;
	}
	/**
	 * @param cell
	 * @return
	 */
	private static String verifyCell(Cell cell) {
		String cellValue;
		cellValue=cell.getStringCellValue().trim().replaceAll(" （", "（").replaceAll("\\(", "（").replaceAll("\\)", "）");
		if("协和医科大".equals(cellValue)){
			cellValue="协和医科大学";
		}
		if("中大".equals(cellValue) || "中山大学出版社".equals(cellValue)){
			cellValue="中山大学";
		}
		if("自解辩证法".equals(cellValue)){
			cellValue="自然辩证法";
		}
		if("病源生物学".equals(cellValue)){
			cellValue="病原生物学";
		}
		if("皮肤性美学".equals(cellValue)){
			cellValue="皮肤性病学";
		}
		if("卫生法规".equals(cellValue)){
			cellValue="卫生法学";
		}
		if("中药跑制学".equals(cellValue)){
			cellValue="中药炮制学";
		}
		if("老科护理学".equals(cellValue)){
			cellValue="老年护理学";
		}
		if("医学影像设备技术".equals(cellValue)){
			cellValue="医学影像检查技术";
		}
		if("药用植物与生药学".equals(cellValue)){
			cellValue="药用植物学与生药学";
		}
		if("中医基础理论".equals(cellValue)){
			cellValue="中医学基础";
		}
		if("中药制剂学".equals(cellValue)){
			cellValue="中药制剂分析";
		}
		if("药用植物学".equals(cellValue)){
			cellValue="药用植物学与生药学";
		}
		return cellValue;
	}
	public static void main(String[] avgs) throws Exception{
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		list = readExcel();
		int lineCount=0;
		StringBuffer fileContent = new StringBuffer();
		for(int i=0;i<list.size();i++){
			
			if(!list.get(i).get("bookname").toString().contains("英语")){
				System.out.println(list.get(i));
				fileContent.append("insert into edu_base_textbook(resourceid, version, isdeleted, bookname, courseid, bookserial, press, editor, price, updatedate, updatemanid, isused) ");
				fileContent.append("select sys_guid(),0,0,'"+list.get(i).get("bookname")+"',c.resourceid,'"+list.get(i).get("bookserial")+"','"+list.get(i).get("press")+"','");
				fileContent.append(list.get(i).get("editor")+"','"+list.get(i).get("price")+"',TO_DATE('06-12-2017','dd-mm-yyyy'),'ff80808107e784fa0107e9234c78012e','Y' ");
				fileContent.append("from edu_base_course c where c.isdeleted=0 and (c.coursename='"+list.get(i).get("courseName")+"' and length(c.coursecode)>4) or (c.coursename like '"+list.get(i).get("courseName")+"%' and c.coursename like '%"+flag1+"%') ;\r\n");
				lineCount++;
			}
		}
		FileUtils.createFile(exportFilepath,fileContent.toString(),"GB2312");
		System.out.println("共有 "+lineCount+ "行");
		System.out.println("#########################  all done!  #################################");
	}
}
