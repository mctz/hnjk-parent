package com.hnjk.extend.plugin.excel.util;


import java.util.List;
import java.util.Map;

/**
 *  excel组件单元格校验工具类. <p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2009-4-29上午11:24:56
 * @see 
 * @version 1.0
 */
public class ValidateColumn {
	/**
	 * 配制文件验证时保存。如果存在一个同名的标题，取　标题_1，如果存在　标题_1,取标题_2
	 * @param map　配制文件按顺序读取时，当前属性前所有属性值map,如果当前标题是 第 5 个，map size　为4
	 * @param excelTitle
	 * @return  如果标传入标题excelTitle在map存在，返回　excelTitle_1.如果excelTitle_1也存在，返回excelTitle_2
	 */
	public static String configValidate(Map map,String excelTitle){
		String temp = excelTitle;
		for(int i=0;i<map.size();i++){			
			if (i>0){
				temp = excelTitle + "_" + i;
			}				
			if(map.containsKey(temp)){
				continue;
			}else{				
				break;
			}			
		}
		return temp;
	}
	
	/**
	 * 验证excel表头，如果存在一个同名的标题，取　标题_1，如果存在　标题_1,取标题_2,这样可以把excel列中配制成同一个名称也可以读取。
	 * @param list　excel标题按顺序读取时，当前标题前所有值list,如果当前标题是 第 5 个，list size　为4
	 * @param excelTitle
	 * @return 如果标传入标题excelTitle在list存在，返回　excelTitle_1.如果excelTitle_1也存在，返回excelTitle_2
	 */
	public static String columnValidate(List list ,String excelTitle){
		String temp = excelTitle;
		for(int i=0;i<list.size();i++){
			if(i>0){
				temp =excelTitle + "_" + i;
			}
			if(list.contains(temp)){
				continue;
			}else{
				break;
			}
		}
		return temp;
		
	}
}
