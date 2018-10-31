package com.hnjk.core.foundation.utils;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 获取当交日期的汉字格式 如:二0一0年七月一日
 * <code>DateChineseFormart</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2010-7-1 下午05:39:57
 * @see  
 * @version 1.0
 */
public class DateChineseFormat implements Serializable{
	
	private static final long serialVersionUID = -2318949957248397739L;

	public static String getCurrentDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return change(sdf.format(new Date()));
	}
	/**
	 * 
	 * @param date   yyyy-MM-dd
	 * @return
	 */
	public static String getChineseDate(String date){
		return change(date);
	}
	//传参数的格式为 2009-02-06
	public static String change(String ss){
		   String k="";
		   String[] s=ss.split("-");
		   //s[0]为年 ，s[1]为月，s[2]为日  
		  
		   for(int j=0;j<s[0].length();j++){   
		    switch(s[0].charAt(j)){
		    case '0': k+="〇";break;
		    case '1': k+="一";break;
		    case '2': k+="二";break;
		    case '3': k+="三";break;
		    case '4': k+="四";break;
		    case '5': k+="五";break;
		    case '6': k+="六";break;
		    case '7': k+="七";break;
		    case '8': k+="八";break;
		    case '9': k+="九";break;   
		    }
		   }
		   k=k+"年";
		  
		   if("01".equals(s[1])){
		    k+="一";
		   }else if("02".equals(s[1])){
		    k+="二";
		   }else if("03".equals(s[1])){
		    k+="三";
		   }else if("04".equals(s[1])){
		    k+="四";
		   }else if("05".equals(s[1])){
		    k+="五";
		   }else if("06".equals(s[1])){
		    k+="六";
		   }else if("07".equals(s[1])){
		    k+="七";
		   }else if("08".equals(s[1])){
		    k+="八";
		   }else if("09".equals(s[1])){
		    k+="九";
		   }else if("10".equals(s[1])){
		    k+="十";
		   }else if("11".equals(s[1])){
		    k+="十一";
		   }else{
		    k+="十二";
		   }  
		   k+="月";
		  
		   if("01".equals(s[2])){
		    k+="一";
		   }else if("02".equals(s[2])){
		    k+="二";
		   }else if("03".equals(s[2])){
		    k+="三";
		   }else if("04".equals(s[2])){
		    k+="四";
		   }else if("05".equals(s[2])){
		    k+="五";
		   }else if("06".equals(s[2])){
		    k+="六";
		   }else if("07".equals(s[2])){
		    k+="七";
		   }else if("08".equals(s[2])){
		    k+="八";
		   }else if("09".equals(s[2])){
		    k+="九";
		   }else if("10".equals(s[2])){
		    k+="十";
		   }else if("11".equals(s[2])){
		    k+="十一";
		   }else if("12".equals(s[2])){
		    k+="十二";
		   }else if("13".equals(s[2])){
		    k+="十三";
		   }else if("14".equals(s[2])){
		    k+="十四";
		   }else if("15".equals(s[2])){
		    k+="十五";
		   }else if("16".equals(s[2])){
		    k+="十六";
		   }else if("17".equals(s[2])){
		    k+="十七";
		   }else if("18".equals(s[2])){
		    k+="十八";
		   }else if("19".equals(s[2])){
		    k+="十九";
		   }else if("20".equals(s[2])){
		    k+="二十";
		   }else if("21".equals(s[2])){
		    k+="二十一";
		   }else if("22".equals(s[2])){
		    k+="二十二";
		   }else if("23".equals(s[2])){
		    k+="二十三";
		   }else if("24".equals(s[2])){
		    k+="二十四";
		   }else if("25".equals(s[2])){
		    k+="二十五";
		   }else if("26".equals(s[2])){
		    k+="二十六";
		   }else if("27".equals(s[2])){
		    k+="二十七";
		   }else if("28".equals(s[2])){
		    k+="二十八";
		   }else if("29".equals(s[2])){
		    k+="二十九";
		   }else if("30".equals(s[2])){
		    k+="三十";
		   }else if("31".equals(s[2])){
		    k+="三十一";
		   }
		   k+="日";
		   return k;
		}

	/**
	 * 把总时间(秒)转换为可读时间,如x天x小时x分钟x秒
	 * @param seconds
	 * @return
	 */
	public static String getHumanReadableTime(Long seconds){
		if(seconds == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		long sec = seconds.longValue();
		long d = sec / (24*60*60);
		if(d >= 1){
			sb.append(d).append("天");
			sec -= d * (24*60*60);
		}
		d = sec / (60*60);
		if(d >= 1){
			sb.append(d).append("小时");
			sec -= d * (60*60);
		}
		d = sec / 60;
		if(d >= 1){
			sb.append(d).append("分");
			sec -= d * 60;
		}
		d = sec;
		if(d >= 1){
			sb.append(d).append("秒");
		} else {
			sb.append(d);
		}
		return sb.toString();
	}
}
