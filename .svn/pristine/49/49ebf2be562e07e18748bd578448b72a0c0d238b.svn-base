package com.hnjk.platform.taglib;

//${el:stringChange()}普通类
//传入格式：Y,Y,N,UN,M
public class StringChange {

	public static String stringChange(String str){
		if(null == str || "".equals(str)){
			return "";
		}
		
		StringBuffer strChange = new StringBuffer();
		int strY = 0;
		int strN = 0;
		int strUN = 0;
		int strM = 0;
		String[] strs = str.split("\\,");
		for(String s: strs){
			if("UN".equals(s)){
				strUN++;
			} else if("Y".equals(s)){
				strY++;
			} else if("M".equals(s)){
				strM++;
			} else {
				strN++;
			}
		}
		if (strY != 0) {
			strChange.append("<font color='green'>及格:").append(strY).append("</font>");
		}
		if (strN != 0) {
			strChange.append(", <font color='red'>不及格:").append(strN).append("</font>");
		}
		if (strUN != 0) {
			strChange.append(", <font color='#0000FF'>未考核:").append(strUN).append("</font>");
		}
		if (strM != 0) {
			strChange.append(", <font color='#0000FF'>缺考:").append(strM).append("</font>");
		}
		String stringChange = strChange.toString();
		if (",".equals(stringChange.substring(0, 1))) {
			stringChange = stringChange.substring(1);
		}
		
//		if(strY != 0){
//			strChange.append("<font color='green'>及格:").append(strY + "</font>");
//			if(strN != 0){
//				strChange.append(", <font color='red'>不及格:").append(strN + "</font>");
//			}
//			if(strUN !=0){
//				strChange.append(", <font color='#0000FF'>未考核:").append(strUN + "</font>");
//			}
//		} else if(strN != 0){
//			strChange.append("<font color='red'>不及格:").append(strN + "</font>");
//			if(strUN !=0){
//				strChange.append(", <font color='#0000FF'>未考核:").append(strUN + "</font>");
//			}
//		} else if(strUN != 0){
//			strChange.append("<font color='#0000FF'>未考核:").append(strUN + "</font>");
//		}
		
		return stringChange;
	}
	
	/**
	 * 测试
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(stringChange("N"));
		System.out.println(stringChange("Y"));
		System.out.println(stringChange(null));
		System.out.println(stringChange("Y,Y,N,UN"));
		System.out.println(stringChange("M,N,UN"));
		System.out.println(stringChange("Y,UN"));
		System.out.println(stringChange("UN,M"));
	}
}
