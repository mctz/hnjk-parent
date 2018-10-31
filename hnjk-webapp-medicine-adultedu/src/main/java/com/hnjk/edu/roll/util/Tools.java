package com.hnjk.edu.roll.util;

import com.hnjk.core.foundation.utils.ExStringUtils;

/**
 * 
 * @author Zik
 *  工具类
 */
public class Tools {

	/**
	 * 给某些内容添加颜色
	 * @author Zik
	 * @param text 内容
	 * @param color 颜色
	 * @return 处理完的内容
	 */
	public static String colorStr(String content, String color){
		String handledContent = "<font ";
		if(content == null) {
			content = "";
		}
		if(ExStringUtils.isEmpty(color)) {
			color = "black";
		}
		handledContent += "color='"+color+"' >"+content+"</font>";
		
		return handledContent;
		
	}
	
	/**
	 * 设置虚线
	 * @author Zik
	 * @param color 颜色
	 * @param size 大小
	 * @return
	 */
	public static String dashLine(String color, int size){
		String dashLine = "<hr style='";
	    if(size == 0){
	    	size =1;
	    }
	    dashLine += "border: "+size+"px ";
	    if(ExStringUtils.isNotEmpty(color)){
	    	dashLine += " dashed " +color;
	    }
	    dashLine += ";'>";
	    
	    return dashLine;
	}
	
	public static String getDigitalTerm(String firstYear,String term){
		String digitalTerm="";		
		try {
			if(ExStringUtils.isNotBlank(term)){			
				int tmpTerm=0;
//			String tmpGrade = gradeName.substring(0,4);
				String tmp1 = term.substring(0,4);
				String tmp2 = term.substring(5,7);
//			tmpTerm = (Integer.parseInt(tmp1)-Integer.parseInt(tmpGrade))*2+Integer.parseInt(tmp2);
				tmpTerm = (Integer.parseInt(tmp1)-Integer.parseInt(firstYear))*2+Integer.parseInt(tmp2);
				switch (tmpTerm) {
				case 1:
					digitalTerm = "第一学期";
					break;
				case 2:
					digitalTerm = "第二学期";
					break;
				case 3:
					digitalTerm = "第三学期";
					break;
				case 4:
					digitalTerm = "第四学期";
					break;
				case 5:
					digitalTerm = "第五学期";
					break;
				case 6:
					digitalTerm = "第六学期";
					break;
				case 7:
					digitalTerm = "第七学期";
					break;
				case 8:
					digitalTerm = "第八学期";
					break;
				case 9:
					digitalTerm = "第九学期";
					break;
				case 10:
					digitalTerm = "第十学期";
					break;
				
				default:
					digitalTerm ="学期转换错误";
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return digitalTerm;
	}
	
	/**
	 * 学制转换
	 * @param eduyear
	 * @return
	 * 
	 * 例子：3 -> 三年
	 */
	public static String eduyearTransfer(String eduyear){
		String eduyearStr = "";
		try {
			int eduyearInt = 0;
			if(ExStringUtils.isNotEmpty(eduyear)){
				eduyearInt = Integer.valueOf(eduyear);
			}
			
			switch (eduyearInt) {
			case 1:
				eduyearStr = "一年";
				break;
			case 2:
				eduyearStr = "两年";
				break;
			case 3:
				eduyearStr = "三年";
				break;
			case 4:
				eduyearStr = "四年";
				break;
			case 5:
				eduyearStr = "五年";
				break;
			case 6:
				eduyearStr = "六年";
				break;
			case 7:
				eduyearStr = "七年";
				break;
			case 8:
				eduyearStr = "八年";
				break;
			case 9:
				eduyearStr = "九年";
				break;
			case 10:
				eduyearStr = "十年";
				break;
			
			default:
				eduyearStr = eduyear;
				break;
			}
		} catch (NumberFormatException e) {
			eduyearStr = eduyear;
		}
		return eduyearStr;
	}
	
	/**
	 * 获取最大数
	 * @param array
	 * @return
	 */
	public static int getMax(int[] array){
	    int max = 0;
	    for (int j = 0; j < array.length; j++) {
	    	if(max < array[j]){
	    		max = array[j];
	    	}
	    }
	    return max;
   }
	
	/**
	 * 获取某一段字符串以length的长度可以划分的行数
	 * @param content
	 * @param length 每行的长度(字节长度)
	 * @return
	 */
	public static int getRowNum(String content, int length){
		int rowNum = 1;
		if(ExStringUtils.isNotEmpty(content)){
			rowNum = (int)((content.getBytes().length-1)/length) + 1;
		}
		return rowNum;
	}
}
